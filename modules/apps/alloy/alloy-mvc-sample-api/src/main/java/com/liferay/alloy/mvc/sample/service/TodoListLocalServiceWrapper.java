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

package com.liferay.alloy.mvc.sample.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link TodoListLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see TodoListLocalService
 * @generated
 */
public class TodoListLocalServiceWrapper
	implements ServiceWrapper<TodoListLocalService>, TodoListLocalService {

	public TodoListLocalServiceWrapper(
		TodoListLocalService todoListLocalService) {

		_todoListLocalService = todoListLocalService;
	}

	/**
	 * Adds the todo list to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TodoListLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param todoList the todo list
	 * @return the todo list that was added
	 */
	@Override
	public com.liferay.alloy.mvc.sample.model.TodoList addTodoList(
		com.liferay.alloy.mvc.sample.model.TodoList todoList) {

		return _todoListLocalService.addTodoList(todoList);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _todoListLocalService.createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new todo list with the primary key. Does not add the todo list to the database.
	 *
	 * @param todoListId the primary key for the new todo list
	 * @return the new todo list
	 */
	@Override
	public com.liferay.alloy.mvc.sample.model.TodoList createTodoList(
		long todoListId) {

		return _todoListLocalService.createTodoList(todoListId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _todoListLocalService.deletePersistedModel(persistedModel);
	}

	/**
	 * Deletes the todo list with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TodoListLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param todoListId the primary key of the todo list
	 * @return the todo list that was removed
	 * @throws PortalException if a todo list with the primary key could not be found
	 */
	@Override
	public com.liferay.alloy.mvc.sample.model.TodoList deleteTodoList(
			long todoListId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _todoListLocalService.deleteTodoList(todoListId);
	}

	/**
	 * Deletes the todo list from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TodoListLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param todoList the todo list
	 * @return the todo list that was removed
	 */
	@Override
	public com.liferay.alloy.mvc.sample.model.TodoList deleteTodoList(
		com.liferay.alloy.mvc.sample.model.TodoList todoList) {

		return _todoListLocalService.deleteTodoList(todoList);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _todoListLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _todoListLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _todoListLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _todoListLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.alloy.mvc.sample.model.impl.TodoListModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _todoListLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.alloy.mvc.sample.model.impl.TodoListModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _todoListLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _todoListLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _todoListLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.alloy.mvc.sample.model.TodoList fetchTodoList(
		long todoListId) {

		return _todoListLocalService.fetchTodoList(todoListId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _todoListLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _todoListLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _todoListLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _todoListLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the todo list with the primary key.
	 *
	 * @param todoListId the primary key of the todo list
	 * @return the todo list
	 * @throws PortalException if a todo list with the primary key could not be found
	 */
	@Override
	public com.liferay.alloy.mvc.sample.model.TodoList getTodoList(
			long todoListId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _todoListLocalService.getTodoList(todoListId);
	}

	/**
	 * Returns a range of all the todo lists.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.alloy.mvc.sample.model.impl.TodoListModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of todo lists
	 * @param end the upper bound of the range of todo lists (not inclusive)
	 * @return the range of todo lists
	 */
	@Override
	public java.util.List<com.liferay.alloy.mvc.sample.model.TodoList>
		getTodoLists(int start, int end) {

		return _todoListLocalService.getTodoLists(start, end);
	}

	/**
	 * Returns the number of todo lists.
	 *
	 * @return the number of todo lists
	 */
	@Override
	public int getTodoListsCount() {
		return _todoListLocalService.getTodoListsCount();
	}

	/**
	 * Updates the todo list in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TodoListLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param todoList the todo list
	 * @return the todo list that was updated
	 */
	@Override
	public com.liferay.alloy.mvc.sample.model.TodoList updateTodoList(
		com.liferay.alloy.mvc.sample.model.TodoList todoList) {

		return _todoListLocalService.updateTodoList(todoList);
	}

	@Override
	public TodoListLocalService getWrappedService() {
		return _todoListLocalService;
	}

	@Override
	public void setWrappedService(TodoListLocalService todoListLocalService) {
		_todoListLocalService = todoListLocalService;
	}

	private TodoListLocalService _todoListLocalService;

}