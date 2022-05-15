/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.cache.internal.dao.orm;

import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.cache.CacheRegistryItem;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheHelperUtil;
import com.liferay.portal.kernel.cache.PortalCacheManager;
import com.liferay.portal.kernel.cache.PortalCacheManagerListener;
import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.kernel.cluster.ClusterInvokeThreadLocal;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.servlet.filters.threadlocal.ThreadLocalFilterThreadLocal;

import java.io.Serializable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.collections.map.LRUMap;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
@Component(
	immediate = true, service = {CacheRegistryItem.class, EntityCache.class}
)
public class EntityCacheImpl
	implements CacheRegistryItem, EntityCache, PortalCacheManagerListener {

	@Override
	public void clearCache() {
		_notifyFinderCache(null, null, false);

		clearLocalCache();

		for (PortalCache<?, ?> portalCache : _portalCaches.values()) {
			portalCache.removeAll();
		}
	}

	@Override
	public void clearCache(Class<?> clazz) {
		_notifyFinderCache(clazz.getName(), null, false);

		clearLocalCache();

		PortalCache<?, ?> portalCache = getPortalCache(clazz);

		portalCache.removeAll();
	}

	@Override
	public void clearLocalCache() {
		if (_isLocalCacheEnabled()) {
			_localCache.remove();
		}
	}

	@Override
	public void dispose() {
		_notifyFinderCache(null, null, true);

		_portalCaches.clear();
	}

	@Override
	public PortalCache<Serializable, Serializable> getPortalCache(
		Class<?> clazz) {

		String className = clazz.getName();

		PortalCache<Serializable, Serializable> portalCache = _portalCaches.get(
			className);

		if (portalCache != null) {
			return portalCache;
		}

		String groupKey = _GROUP_KEY_PREFIX.concat(className);

		boolean mvcc = false;

		if (_valueObjectMVCCEntityCacheEnabled &&
			MVCCModel.class.isAssignableFrom(clazz)) {

			mvcc = true;
		}

		portalCache =
			(PortalCache<Serializable, Serializable>)
				_multiVMPool.getPortalCache(groupKey, false, mvcc);

		PortalCache<Serializable, Serializable> previousPortalCache =
			_portalCaches.putIfAbsent(className, portalCache);

		if (previousPortalCache != null) {
			return previousPortalCache;
		}

		return portalCache;
	}

	@Override
	public String getRegistryName() {
		return EntityCache.class.getName();
	}

	@Override
	public Serializable getResult(Class<?> clazz, Serializable primaryKey) {
		if (!_valueObjectEntityCacheEnabled || !CacheRegistryUtil.isActive()) {
			return null;
		}

		Serializable result = null;

		Map<Serializable, Serializable> localCache = null;

		Serializable localCacheKey = null;

		if (_isLocalCacheEnabled()) {
			localCache = _localCache.get();

			localCacheKey = new LocalCacheKey(clazz.getName(), primaryKey);

			result = localCache.get(localCacheKey);
		}

		if (result == null) {
			PortalCache<Serializable, Serializable> portalCache =
				getPortalCache(clazz);

			result = portalCache.get(primaryKey);

			if (result == null) {
				result = StringPool.BLANK;
			}

			if (localCache != null) {
				localCache.put(localCacheKey, result);
			}
		}

		return _toEntityModel(result);
	}

	@Override
	public void init() {
	}

	@Override
	public void invalidate() {
		clearCache();
	}

	@Override
	public void notifyPortalCacheAdded(String portalCacheName) {
	}

	@Override
	public void notifyPortalCacheRemoved(String portalCacheName) {
		String cacheName = portalCacheName;

		if (portalCacheName.startsWith(_GROUP_KEY_PREFIX)) {
			cacheName = portalCacheName.substring(_GROUP_KEY_PREFIX.length());
		}

		_notifyFinderCache(cacheName, null, true);

		_portalCaches.remove(cacheName);
	}

	@Override
	public void putResult(
		Class<?> clazz, BaseModel<?> baseModel, boolean quiet,
		boolean updateFinderCache) {

		_putResult(
			clazz, baseModel.getPrimaryKeyObj(), baseModel, quiet,
			updateFinderCache);
	}

	@Override
	public void putResult(
		Class<?> clazz, Serializable primaryKey, Serializable result) {

		_putResult(clazz, primaryKey, (BaseModel<?>)result, true, false);
	}

	@Override
	public void removeCache(String className) {
		_notifyFinderCache(className, null, true);

		_portalCaches.remove(className);

		String groupKey = _GROUP_KEY_PREFIX.concat(className);

		_multiVMPool.removePortalCache(groupKey);
	}

	@Override
	public void removeResult(Class<?> clazz, BaseModel<?> baseModel) {
		_removeResult(clazz, baseModel.getPrimaryKeyObj(), baseModel);
	}

	@Override
	public void removeResult(Class<?> clazz, Serializable primaryKey) {
		_removeResult(clazz, primaryKey, null);
	}

	@Activate
	protected void activate() {
		_valueObjectEntityCacheEnabled = GetterUtil.getBoolean(
			_props.get(PropsKeys.VALUE_OBJECT_ENTITY_CACHE_ENABLED));
		_valueObjectMVCCEntityCacheEnabled = GetterUtil.getBoolean(
			_props.get(PropsKeys.VALUE_OBJECT_MVCC_ENTITY_CACHE_ENABLED));

		int localCacheMaxSize = GetterUtil.getInteger(
			_props.get(
				PropsKeys.VALUE_OBJECT_ENTITY_THREAD_LOCAL_CACHE_MAX_SIZE));

		if (localCacheMaxSize > 0) {
			_localCache = new CentralizedThreadLocal<>(
				EntityCacheImpl.class + "._localCache",
				() -> new LRUMap(localCacheMaxSize));
		}
		else {
			_localCache = null;
		}

		PortalCacheManager<? extends Serializable, ? extends Serializable>
			portalCacheManager = _multiVMPool.getPortalCacheManager();

		portalCacheManager.registerPortalCacheManagerListener(this);
	}

	@Reference(unbind = "-")
	protected void setFinderCacheImpl(FinderCacheImpl finderCacheImpl) {
		_finderCacheImpl = finderCacheImpl;
	}

	private boolean _isLocalCacheEnabled() {
		if (_localCache == null) {
			return false;
		}

		return ThreadLocalFilterThreadLocal.isFilterInvoked();
	}

	private void _notify(
		String className, BaseModel<?> baseModel, Boolean removePortalCache) {

		FinderCacheImpl finderCacheImpl = _finderCacheImpl;

		if (finderCacheImpl == null) {
			return;
		}

		if (removePortalCache == null) {
			finderCacheImpl.updateByEntityCache(className, baseModel);
		}
		else if (baseModel != null) {
			finderCacheImpl.removeByEntityCache(className, baseModel);
		}
		else if (removePortalCache) {
			if (className == null) {
				finderCacheImpl.dispose();
			}
			else {
				finderCacheImpl.removeCacheByEntityCache(className);
			}
		}
		else {
			if (className == null) {
				finderCacheImpl.clearCache();
			}
			else {
				finderCacheImpl.clearByEntityCache(className);
			}
		}
	}

	private void _notifyFinderCache(
		String className, BaseModel<?> baseModel, Boolean removePortalCache) {

		_notify(className, baseModel, removePortalCache);

		if (!_clusterExecutor.isEnabled() ||
			!ClusterInvokeThreadLocal.isEnabled()) {

			return;
		}

		try {
			MethodHandler methodHandler = new MethodHandler(
				_notifyMethodKey,
				new Object[] {className, baseModel, removePortalCache});

			ClusterRequest clusterRequest =
				ClusterRequest.createMulticastRequest(methodHandler, true);

			clusterRequest.setFireAndForget(true);

			_clusterExecutor.execute(clusterRequest);
		}
		catch (Throwable throwable) {
			_log.error("Unable to notify cluster", throwable);
		}
	}

	private void _putResult(
		Class<?> clazz, Serializable primaryKey, BaseModel<?> baseModel,
		boolean quiet, boolean updateFinderCache) {

		if (!_valueObjectEntityCacheEnabled || !CacheRegistryUtil.isActive() ||
			(baseModel == null)) {

			return;
		}

		if (!quiet && updateFinderCache) {
			_notifyFinderCache(clazz.getName(), baseModel, null);
		}

		CacheModel<?> result = baseModel.toCacheModel();

		if (_isLocalCacheEnabled()) {
			Map<Serializable, Serializable> localCache = _localCache.get();

			Serializable localCacheKey = new LocalCacheKey(
				clazz.getName(), primaryKey);

			localCache.put(localCacheKey, result);
		}

		PortalCache<Serializable, Serializable> portalCache = getPortalCache(
			clazz);

		if (quiet) {
			PortalCacheHelperUtil.putWithoutReplicator(
				portalCache, primaryKey, result);
		}
		else {
			portalCache.put(primaryKey, result);
		}
	}

	private void _removeResult(
		Class<?> clazz, Serializable primaryKey, BaseModel<?> baseModel) {

		if (!_valueObjectEntityCacheEnabled || !CacheRegistryUtil.isActive()) {
			return;
		}

		if (baseModel != null) {
			_notifyFinderCache(clazz.getName(), baseModel, false);
		}

		if (_isLocalCacheEnabled()) {
			Map<Serializable, Serializable> localCache = _localCache.get();

			Serializable localCacheKey = new LocalCacheKey(
				clazz.getName(), primaryKey);

			localCache.remove(localCacheKey);
		}

		PortalCache<Serializable, Serializable> portalCache = getPortalCache(
			clazz);

		portalCache.remove(primaryKey);
	}

	private Serializable _toEntityModel(Serializable result) {
		if (result == StringPool.BLANK) {
			return null;
		}

		CacheModel<?> cacheModel = (CacheModel<?>)result;

		BaseModel<?> entityModel = (BaseModel<?>)cacheModel.toEntityModel();

		entityModel.setCachedModel(true);

		return entityModel;
	}

	private static final String _GROUP_KEY_PREFIX =
		EntityCache.class.getName() + StringPool.PERIOD;

	private static final Log _log = LogFactoryUtil.getLog(
		EntityCacheImpl.class);

	private static volatile FinderCacheImpl _finderCacheImpl;
	private static final MethodKey _notifyMethodKey = new MethodKey(
		EntityCacheImpl.class, "_notify", String.class, BaseModel.class,
		Boolean.class);

	@Reference
	private ClusterExecutor _clusterExecutor;

	private ThreadLocal<LRUMap> _localCache;

	@Reference
	private MultiVMPool _multiVMPool;

	private final ConcurrentMap<String, PortalCache<Serializable, Serializable>>
		_portalCaches = new ConcurrentHashMap<>();

	@Reference
	private Props _props;

	private boolean _valueObjectEntityCacheEnabled;
	private boolean _valueObjectMVCCEntityCacheEnabled;

	private static class LocalCacheKey implements Serializable {

		public LocalCacheKey(String className, Serializable primaryKey) {
			_className = className;
			_primaryKey = primaryKey;
		}

		@Override
		public boolean equals(Object object) {
			LocalCacheKey localCacheKey = (LocalCacheKey)object;

			if (localCacheKey._className.equals(_className) &&
				localCacheKey._primaryKey.equals(_primaryKey)) {

				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {
			return (_className.hashCode() * 11) + _primaryKey.hashCode();
		}

		private static final long serialVersionUID = 1L;

		private final String _className;
		private final Serializable _primaryKey;

	}

}