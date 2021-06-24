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

package com.liferay.alloy.mvc.sample.service.persistence;

import com.liferay.alloy.mvc.sample.model.TodoList;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the todo list service. This utility wraps <code>com.liferay.alloy.mvc.sample.service.persistence.impl.TodoListPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TodoListPersistence
 * @generated
 */
public class TodoListUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(TodoList todoList) {
		getPersistence().clearCache(todoList);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, TodoList> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<TodoList> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<TodoList> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<TodoList> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<TodoList> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static TodoList update(TodoList todoList) {
		return getPersistence().update(todoList);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static TodoList update(
		TodoList todoList, ServiceContext serviceContext) {

		return getPersistence().update(todoList, serviceContext);
	}

	/**
	 * Caches the todo list in the entity cache if it is enabled.
	 *
	 * @param todoList the todo list
	 */
	public static void cacheResult(TodoList todoList) {
		getPersistence().cacheResult(todoList);
	}

	/**
	 * Caches the todo lists in the entity cache if it is enabled.
	 *
	 * @param todoLists the todo lists
	 */
	public static void cacheResult(List<TodoList> todoLists) {
		getPersistence().cacheResult(todoLists);
	}

	/**
	 * Creates a new todo list with the primary key. Does not add the todo list to the database.
	 *
	 * @param todoListId the primary key for the new todo list
	 * @return the new todo list
	 */
	public static TodoList create(long todoListId) {
		return getPersistence().create(todoListId);
	}

	/**
	 * Removes the todo list with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param todoListId the primary key of the todo list
	 * @return the todo list that was removed
	 * @throws NoSuchTodoListException if a todo list with the primary key could not be found
	 */
	public static TodoList remove(long todoListId)
		throws com.liferay.alloy.mvc.sample.exception.NoSuchTodoListException {

		return getPersistence().remove(todoListId);
	}

	public static TodoList updateImpl(TodoList todoList) {
		return getPersistence().updateImpl(todoList);
	}

	/**
	 * Returns the todo list with the primary key or throws a <code>NoSuchTodoListException</code> if it could not be found.
	 *
	 * @param todoListId the primary key of the todo list
	 * @return the todo list
	 * @throws NoSuchTodoListException if a todo list with the primary key could not be found
	 */
	public static TodoList findByPrimaryKey(long todoListId)
		throws com.liferay.alloy.mvc.sample.exception.NoSuchTodoListException {

		return getPersistence().findByPrimaryKey(todoListId);
	}

	/**
	 * Returns the todo list with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param todoListId the primary key of the todo list
	 * @return the todo list, or <code>null</code> if a todo list with the primary key could not be found
	 */
	public static TodoList fetchByPrimaryKey(long todoListId) {
		return getPersistence().fetchByPrimaryKey(todoListId);
	}

	/**
	 * Returns all the todo lists.
	 *
	 * @return the todo lists
	 */
	public static List<TodoList> findAll() {
		return getPersistence().findAll();
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
	public static List<TodoList> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
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
	public static List<TodoList> findAll(
		int start, int end, OrderByComparator<TodoList> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
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
	public static List<TodoList> findAll(
		int start, int end, OrderByComparator<TodoList> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the todo lists from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of todo lists.
	 *
	 * @return the number of todo lists
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static TodoListPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<TodoListPersistence, TodoListPersistence>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(TodoListPersistence.class);

		ServiceTracker<TodoListPersistence, TodoListPersistence>
			serviceTracker =
				new ServiceTracker<TodoListPersistence, TodoListPersistence>(
					bundle.getBundleContext(), TodoListPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}