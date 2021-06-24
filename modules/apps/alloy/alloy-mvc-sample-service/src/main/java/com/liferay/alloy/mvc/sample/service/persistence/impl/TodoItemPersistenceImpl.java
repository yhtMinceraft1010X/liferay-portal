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

package com.liferay.alloy.mvc.sample.service.persistence.impl;

import com.liferay.alloy.mvc.sample.exception.NoSuchTodoItemException;
import com.liferay.alloy.mvc.sample.model.TodoItem;
import com.liferay.alloy.mvc.sample.model.TodoItemTable;
import com.liferay.alloy.mvc.sample.model.impl.TodoItemImpl;
import com.liferay.alloy.mvc.sample.model.impl.TodoItemModelImpl;
import com.liferay.alloy.mvc.sample.service.persistence.TodoItemPersistence;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * The persistence implementation for the todo item service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class TodoItemPersistenceImpl
	extends BasePersistenceImpl<TodoItem> implements TodoItemPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>TodoItemUtil</code> to access the todo item persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		TodoItemImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;

	public TodoItemPersistenceImpl() {
		setModelClass(TodoItem.class);

		setModelImplClass(TodoItemImpl.class);
		setModelPKClass(long.class);

		setTable(TodoItemTable.INSTANCE);
	}

	/**
	 * Caches the todo item in the entity cache if it is enabled.
	 *
	 * @param todoItem the todo item
	 */
	@Override
	public void cacheResult(TodoItem todoItem) {
		entityCache.putResult(
			TodoItemImpl.class, todoItem.getPrimaryKey(), todoItem);
	}

	/**
	 * Caches the todo items in the entity cache if it is enabled.
	 *
	 * @param todoItems the todo items
	 */
	@Override
	public void cacheResult(List<TodoItem> todoItems) {
		for (TodoItem todoItem : todoItems) {
			if (entityCache.getResult(
					TodoItemImpl.class, todoItem.getPrimaryKey()) == null) {

				cacheResult(todoItem);
			}
		}
	}

	/**
	 * Clears the cache for all todo items.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(TodoItemImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the todo item.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(TodoItem todoItem) {
		entityCache.removeResult(TodoItemImpl.class, todoItem);
	}

	@Override
	public void clearCache(List<TodoItem> todoItems) {
		for (TodoItem todoItem : todoItems) {
			entityCache.removeResult(TodoItemImpl.class, todoItem);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(TodoItemImpl.class, primaryKey);
		}
	}

	/**
	 * Creates a new todo item with the primary key. Does not add the todo item to the database.
	 *
	 * @param todoItemId the primary key for the new todo item
	 * @return the new todo item
	 */
	@Override
	public TodoItem create(long todoItemId) {
		TodoItem todoItem = new TodoItemImpl();

		todoItem.setNew(true);
		todoItem.setPrimaryKey(todoItemId);

		todoItem.setCompanyId(CompanyThreadLocal.getCompanyId());

		return todoItem;
	}

	/**
	 * Removes the todo item with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param todoItemId the primary key of the todo item
	 * @return the todo item that was removed
	 * @throws NoSuchTodoItemException if a todo item with the primary key could not be found
	 */
	@Override
	public TodoItem remove(long todoItemId) throws NoSuchTodoItemException {
		return remove((Serializable)todoItemId);
	}

	/**
	 * Removes the todo item with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the todo item
	 * @return the todo item that was removed
	 * @throws NoSuchTodoItemException if a todo item with the primary key could not be found
	 */
	@Override
	public TodoItem remove(Serializable primaryKey)
		throws NoSuchTodoItemException {

		Session session = null;

		try {
			session = openSession();

			TodoItem todoItem = (TodoItem)session.get(
				TodoItemImpl.class, primaryKey);

			if (todoItem == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchTodoItemException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(todoItem);
		}
		catch (NoSuchTodoItemException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected TodoItem removeImpl(TodoItem todoItem) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(todoItem)) {
				todoItem = (TodoItem)session.get(
					TodoItemImpl.class, todoItem.getPrimaryKeyObj());
			}

			if (todoItem != null) {
				session.delete(todoItem);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (todoItem != null) {
			clearCache(todoItem);
		}

		return todoItem;
	}

	@Override
	public TodoItem updateImpl(TodoItem todoItem) {
		boolean isNew = todoItem.isNew();

		if (!(todoItem instanceof TodoItemModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(todoItem.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(todoItem);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in todoItem proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom TodoItem implementation " +
					todoItem.getClass());
		}

		TodoItemModelImpl todoItemModelImpl = (TodoItemModelImpl)todoItem;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (todoItem.getCreateDate() == null)) {
			if (serviceContext == null) {
				todoItem.setCreateDate(date);
			}
			else {
				todoItem.setCreateDate(serviceContext.getCreateDate(date));
			}
		}

		if (!todoItemModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				todoItem.setModifiedDate(date);
			}
			else {
				todoItem.setModifiedDate(serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(todoItem);
			}
			else {
				todoItem = (TodoItem)session.merge(todoItem);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(TodoItemImpl.class, todoItem, false, true);

		if (isNew) {
			todoItem.setNew(false);
		}

		todoItem.resetOriginalValues();

		return todoItem;
	}

	/**
	 * Returns the todo item with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the todo item
	 * @return the todo item
	 * @throws NoSuchTodoItemException if a todo item with the primary key could not be found
	 */
	@Override
	public TodoItem findByPrimaryKey(Serializable primaryKey)
		throws NoSuchTodoItemException {

		TodoItem todoItem = fetchByPrimaryKey(primaryKey);

		if (todoItem == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchTodoItemException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return todoItem;
	}

	/**
	 * Returns the todo item with the primary key or throws a <code>NoSuchTodoItemException</code> if it could not be found.
	 *
	 * @param todoItemId the primary key of the todo item
	 * @return the todo item
	 * @throws NoSuchTodoItemException if a todo item with the primary key could not be found
	 */
	@Override
	public TodoItem findByPrimaryKey(long todoItemId)
		throws NoSuchTodoItemException {

		return findByPrimaryKey((Serializable)todoItemId);
	}

	/**
	 * Returns the todo item with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param todoItemId the primary key of the todo item
	 * @return the todo item, or <code>null</code> if a todo item with the primary key could not be found
	 */
	@Override
	public TodoItem fetchByPrimaryKey(long todoItemId) {
		return fetchByPrimaryKey((Serializable)todoItemId);
	}

	/**
	 * Returns all the todo items.
	 *
	 * @return the todo items
	 */
	@Override
	public List<TodoItem> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the todo items.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TodoItemModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of todo items
	 * @param end the upper bound of the range of todo items (not inclusive)
	 * @return the range of todo items
	 */
	@Override
	public List<TodoItem> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the todo items.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TodoItemModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of todo items
	 * @param end the upper bound of the range of todo items (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of todo items
	 */
	@Override
	public List<TodoItem> findAll(
		int start, int end, OrderByComparator<TodoItem> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the todo items.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TodoItemModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of todo items
	 * @param end the upper bound of the range of todo items (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of todo items
	 */
	@Override
	public List<TodoItem> findAll(
		int start, int end, OrderByComparator<TodoItem> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<TodoItem> list = null;

		if (useFinderCache) {
			list = (List<TodoItem>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_TODOITEM);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_TODOITEM;

				sql = sql.concat(TodoItemModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<TodoItem>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the todo items from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (TodoItem todoItem : findAll()) {
			remove(todoItem);
		}
	}

	/**
	 * Returns the number of todo items.
	 *
	 * @return the number of todo items
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_TODOITEM);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "todoItemId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_TODOITEM;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return TodoItemModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the todo item persistence.
	 */
	public void afterPropertiesSet() {
		Bundle bundle = FrameworkUtil.getBundle(TodoItemPersistenceImpl.class);

		_bundleContext = bundle.getBundleContext();

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class, new TodoItemModelArgumentsResolver(),
			MapUtil.singletonDictionary(
				"model.class.name", TodoItem.class.getName()));

		_finderPathWithPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = _createFinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);
	}

	public void destroy() {
		entityCache.removeCache(TodoItemImpl.class.getName());

		_argumentsResolverServiceRegistration.unregister();

		for (ServiceRegistration<FinderPath> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	private BundleContext _bundleContext;

	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;

	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_TODOITEM =
		"SELECT todoItem FROM TodoItem todoItem";

	private static final String _SQL_COUNT_TODOITEM =
		"SELECT COUNT(todoItem) FROM TodoItem todoItem";

	private static final String _ORDER_BY_ENTITY_ALIAS = "todoItem.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No TodoItem exists with the primary key ";

	private static final Log _log = LogFactoryUtil.getLog(
		TodoItemPersistenceImpl.class);

	private FinderPath _createFinderPath(
		String cacheName, String methodName, String[] params,
		String[] columnNames, boolean baseModelResult) {

		FinderPath finderPath = new FinderPath(
			cacheName, methodName, params, columnNames, baseModelResult);

		if (!cacheName.equals(FINDER_CLASS_NAME_LIST_WITH_PAGINATION)) {
			_serviceRegistrations.add(
				_bundleContext.registerService(
					FinderPath.class, finderPath,
					MapUtil.singletonDictionary("cache.name", cacheName)));
		}

		return finderPath;
	}

	private Set<ServiceRegistration<FinderPath>> _serviceRegistrations =
		new HashSet<>();
	private ServiceRegistration<ArgumentsResolver>
		_argumentsResolverServiceRegistration;

	private static class TodoItemModelArgumentsResolver
		implements ArgumentsResolver {

		@Override
		public Object[] getArguments(
			FinderPath finderPath, BaseModel<?> baseModel, boolean checkColumn,
			boolean original) {

			String[] columnNames = finderPath.getColumnNames();

			if ((columnNames == null) || (columnNames.length == 0)) {
				if (baseModel.isNew()) {
					return FINDER_ARGS_EMPTY;
				}

				return null;
			}

			TodoItemModelImpl todoItemModelImpl = (TodoItemModelImpl)baseModel;

			long columnBitmask = todoItemModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(todoItemModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |=
						todoItemModelImpl.getColumnBitmask(columnName);
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(todoItemModelImpl, columnNames, original);
			}

			return null;
		}

		private static Object[] _getValue(
			TodoItemModelImpl todoItemModelImpl, String[] columnNames,
			boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] = todoItemModelImpl.getColumnOriginalValue(
						columnName);
				}
				else {
					arguments[i] = todoItemModelImpl.getColumnValue(columnName);
				}
			}

			return arguments;
		}

		private static final Map<FinderPath, Long>
			_finderPathColumnBitmasksCache = new ConcurrentHashMap<>();

	}

}