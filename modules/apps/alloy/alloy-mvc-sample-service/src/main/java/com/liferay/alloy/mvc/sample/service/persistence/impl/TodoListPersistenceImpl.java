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

import com.liferay.alloy.mvc.sample.exception.NoSuchTodoListException;
import com.liferay.alloy.mvc.sample.model.TodoList;
import com.liferay.alloy.mvc.sample.model.TodoListTable;
import com.liferay.alloy.mvc.sample.model.impl.TodoListImpl;
import com.liferay.alloy.mvc.sample.model.impl.TodoListModelImpl;
import com.liferay.alloy.mvc.sample.service.persistence.TodoListPersistence;
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
 * The persistence implementation for the todo list service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class TodoListPersistenceImpl
	extends BasePersistenceImpl<TodoList> implements TodoListPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>TodoListUtil</code> to access the todo list persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		TodoListImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;

	public TodoListPersistenceImpl() {
		setModelClass(TodoList.class);

		setModelImplClass(TodoListImpl.class);
		setModelPKClass(long.class);

		setTable(TodoListTable.INSTANCE);
	}

	/**
	 * Caches the todo list in the entity cache if it is enabled.
	 *
	 * @param todoList the todo list
	 */
	@Override
	public void cacheResult(TodoList todoList) {
		entityCache.putResult(
			TodoListImpl.class, todoList.getPrimaryKey(), todoList);
	}

	/**
	 * Caches the todo lists in the entity cache if it is enabled.
	 *
	 * @param todoLists the todo lists
	 */
	@Override
	public void cacheResult(List<TodoList> todoLists) {
		for (TodoList todoList : todoLists) {
			if (entityCache.getResult(
					TodoListImpl.class, todoList.getPrimaryKey()) == null) {

				cacheResult(todoList);
			}
		}
	}

	/**
	 * Clears the cache for all todo lists.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(TodoListImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the todo list.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(TodoList todoList) {
		entityCache.removeResult(TodoListImpl.class, todoList);
	}

	@Override
	public void clearCache(List<TodoList> todoLists) {
		for (TodoList todoList : todoLists) {
			entityCache.removeResult(TodoListImpl.class, todoList);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(TodoListImpl.class, primaryKey);
		}
	}

	/**
	 * Creates a new todo list with the primary key. Does not add the todo list to the database.
	 *
	 * @param todoListId the primary key for the new todo list
	 * @return the new todo list
	 */
	@Override
	public TodoList create(long todoListId) {
		TodoList todoList = new TodoListImpl();

		todoList.setNew(true);
		todoList.setPrimaryKey(todoListId);

		todoList.setCompanyId(CompanyThreadLocal.getCompanyId());

		return todoList;
	}

	/**
	 * Removes the todo list with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param todoListId the primary key of the todo list
	 * @return the todo list that was removed
	 * @throws NoSuchTodoListException if a todo list with the primary key could not be found
	 */
	@Override
	public TodoList remove(long todoListId) throws NoSuchTodoListException {
		return remove((Serializable)todoListId);
	}

	/**
	 * Removes the todo list with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the todo list
	 * @return the todo list that was removed
	 * @throws NoSuchTodoListException if a todo list with the primary key could not be found
	 */
	@Override
	public TodoList remove(Serializable primaryKey)
		throws NoSuchTodoListException {

		Session session = null;

		try {
			session = openSession();

			TodoList todoList = (TodoList)session.get(
				TodoListImpl.class, primaryKey);

			if (todoList == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchTodoListException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(todoList);
		}
		catch (NoSuchTodoListException noSuchEntityException) {
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
	protected TodoList removeImpl(TodoList todoList) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(todoList)) {
				todoList = (TodoList)session.get(
					TodoListImpl.class, todoList.getPrimaryKeyObj());
			}

			if (todoList != null) {
				session.delete(todoList);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (todoList != null) {
			clearCache(todoList);
		}

		return todoList;
	}

	@Override
	public TodoList updateImpl(TodoList todoList) {
		boolean isNew = todoList.isNew();

		if (!(todoList instanceof TodoListModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(todoList.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(todoList);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in todoList proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom TodoList implementation " +
					todoList.getClass());
		}

		TodoListModelImpl todoListModelImpl = (TodoListModelImpl)todoList;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (todoList.getCreateDate() == null)) {
			if (serviceContext == null) {
				todoList.setCreateDate(date);
			}
			else {
				todoList.setCreateDate(serviceContext.getCreateDate(date));
			}
		}

		if (!todoListModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				todoList.setModifiedDate(date);
			}
			else {
				todoList.setModifiedDate(serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(todoList);
			}
			else {
				todoList = (TodoList)session.merge(todoList);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(TodoListImpl.class, todoList, false, true);

		if (isNew) {
			todoList.setNew(false);
		}

		todoList.resetOriginalValues();

		return todoList;
	}

	/**
	 * Returns the todo list with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the todo list
	 * @return the todo list
	 * @throws NoSuchTodoListException if a todo list with the primary key could not be found
	 */
	@Override
	public TodoList findByPrimaryKey(Serializable primaryKey)
		throws NoSuchTodoListException {

		TodoList todoList = fetchByPrimaryKey(primaryKey);

		if (todoList == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchTodoListException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return todoList;
	}

	/**
	 * Returns the todo list with the primary key or throws a <code>NoSuchTodoListException</code> if it could not be found.
	 *
	 * @param todoListId the primary key of the todo list
	 * @return the todo list
	 * @throws NoSuchTodoListException if a todo list with the primary key could not be found
	 */
	@Override
	public TodoList findByPrimaryKey(long todoListId)
		throws NoSuchTodoListException {

		return findByPrimaryKey((Serializable)todoListId);
	}

	/**
	 * Returns the todo list with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param todoListId the primary key of the todo list
	 * @return the todo list, or <code>null</code> if a todo list with the primary key could not be found
	 */
	@Override
	public TodoList fetchByPrimaryKey(long todoListId) {
		return fetchByPrimaryKey((Serializable)todoListId);
	}

	/**
	 * Returns all the todo lists.
	 *
	 * @return the todo lists
	 */
	@Override
	public List<TodoList> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the todo lists.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TodoListModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of todo lists
	 * @param end the upper bound of the range of todo lists (not inclusive)
	 * @return the range of todo lists
	 */
	@Override
	public List<TodoList> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the todo lists.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TodoListModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of todo lists
	 * @param end the upper bound of the range of todo lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of todo lists
	 */
	@Override
	public List<TodoList> findAll(
		int start, int end, OrderByComparator<TodoList> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the todo lists.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TodoListModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of todo lists
	 * @param end the upper bound of the range of todo lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of todo lists
	 */
	@Override
	public List<TodoList> findAll(
		int start, int end, OrderByComparator<TodoList> orderByComparator,
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

		List<TodoList> list = null;

		if (useFinderCache) {
			list = (List<TodoList>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_TODOLIST);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_TODOLIST;

				sql = sql.concat(TodoListModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<TodoList>)QueryUtil.list(
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
	 * Removes all the todo lists from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (TodoList todoList : findAll()) {
			remove(todoList);
		}
	}

	/**
	 * Returns the number of todo lists.
	 *
	 * @return the number of todo lists
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_TODOLIST);

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
		return "todoListId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_TODOLIST;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return TodoListModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the todo list persistence.
	 */
	public void afterPropertiesSet() {
		Bundle bundle = FrameworkUtil.getBundle(TodoListPersistenceImpl.class);

		_bundleContext = bundle.getBundleContext();

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class, new TodoListModelArgumentsResolver(),
			MapUtil.singletonDictionary(
				"model.class.name", TodoList.class.getName()));

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
		entityCache.removeCache(TodoListImpl.class.getName());

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

	private static final String _SQL_SELECT_TODOLIST =
		"SELECT todoList FROM TodoList todoList";

	private static final String _SQL_COUNT_TODOLIST =
		"SELECT COUNT(todoList) FROM TodoList todoList";

	private static final String _ORDER_BY_ENTITY_ALIAS = "todoList.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No TodoList exists with the primary key ";

	private static final Log _log = LogFactoryUtil.getLog(
		TodoListPersistenceImpl.class);

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

	private static class TodoListModelArgumentsResolver
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

			TodoListModelImpl todoListModelImpl = (TodoListModelImpl)baseModel;

			long columnBitmask = todoListModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(todoListModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |=
						todoListModelImpl.getColumnBitmask(columnName);
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(todoListModelImpl, columnNames, original);
			}

			return null;
		}

		private static Object[] _getValue(
			TodoListModelImpl todoListModelImpl, String[] columnNames,
			boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] = todoListModelImpl.getColumnOriginalValue(
						columnName);
				}
				else {
					arguments[i] = todoListModelImpl.getColumnValue(columnName);
				}
			}

			return arguments;
		}

		private static final Map<FinderPath, Long>
			_finderPathColumnBitmasksCache = new ConcurrentHashMap<>();

	}

}