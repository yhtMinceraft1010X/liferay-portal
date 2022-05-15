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

package com.liferay.portal.kernel.dao.orm;

import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.util.ServiceProxyFactory;

import java.io.Serializable;

/**
 * @author Brian Wing Shun Chan
 */
public class EntityCacheUtil {

	public static void clearCache() {
		_entityCache.clearCache();
	}

	public static void clearCache(Class<?> clazz) {
		_entityCache.clearCache(clazz);
	}

	public static void clearLocalCache() {
		_entityCache.clearLocalCache();
	}

	public static EntityCache getEntityCache() {
		return _entityCache;
	}

	public static PortalCache<Serializable, Serializable> getPortalCache(
		Class<?> clazz) {

		return _entityCache.getPortalCache(clazz);
	}

	public static Serializable getResult(
		Class<?> clazz, Serializable primaryKey) {

		return _entityCache.getResult(clazz, primaryKey);
	}

	public static void invalidate() {
		_entityCache.invalidate();
	}

	public static void putResult(
		Class<?> clazz, BaseModel<?> baseModel, boolean quiet,
		boolean updateFinderCache) {

		_entityCache.putResult(clazz, baseModel, quiet, updateFinderCache);
	}

	public static void putResult(
		Class<?> clazz, Serializable primaryKey, Serializable result) {

		_entityCache.putResult(clazz, primaryKey, result);
	}

	public static void removeCache(String className) {
		_entityCache.removeCache(className);
	}

	public static void removeResult(Class<?> clazz, BaseModel<?> baseModel) {
		_entityCache.removeResult(clazz, baseModel);
	}

	public static void removeResult(Class<?> clazz, Serializable primaryKey) {
		_entityCache.removeResult(clazz, primaryKey);
	}

	private static volatile EntityCache _entityCache =
		ServiceProxyFactory.newServiceTrackedInstance(
			EntityCache.class, EntityCacheUtil.class, "_entityCache", false);

}