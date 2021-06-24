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

import com.liferay.alloy.mvc.sample.exception.NoSuchTodoListException;
import com.liferay.alloy.mvc.sample.model.TodoList;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the todo list service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TodoListUtil
 * @generated
 */
@ProviderType
public interface TodoListPersistence extends BasePersistence<TodoList> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link TodoListUtil} to access the todo list persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Caches the todo list in the entity cache if it is enabled.
	 *
	 * @param todoList the todo list
	 */
	public void cacheResult(TodoList todoList);

	/**
	 * Caches the todo lists in the entity cache if it is enabled.
	 *
	 * @param todoLists the todo lists
	 */
	public void cacheResult(java.util.List<TodoList> todoLists);

	/**
	 * Creates a new todo list with the primary key. Does not add the todo list to the database.
	 *
	 * @param todoListId the primary key for the new todo list
	 * @return the new todo list
	 */
	public TodoList create(long todoListId);

	/**
	 * Removes the todo list with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param todoListId the primary key of the todo list
	 * @return the todo list that was removed
	 * @throws NoSuchTodoListException if a todo list with the primary key could not be found
	 */
	public TodoList remove(long todoListId) throws NoSuchTodoListException;

	public TodoList updateImpl(TodoList todoList);

	/**
	 * Returns the todo list with the primary key or throws a <code>NoSuchTodoListException</code> if it could not be found.
	 *
	 * @param todoListId the primary key of the todo list
	 * @return the todo list
	 * @throws NoSuchTodoListException if a todo list with the primary key could not be found
	 */
	public TodoList findByPrimaryKey(long todoListId)
		throws NoSuchTodoListException;

	/**
	 * Returns the todo list with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param todoListId the primary key of the todo list
	 * @return the todo list, or <code>null</code> if a todo list with the primary key could not be found
	 */
	public TodoList fetchByPrimaryKey(long todoListId);

	/**
	 * Returns all the todo lists.
	 *
	 * @return the todo lists
	 */
	public java.util.List<TodoList> findAll();

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
	public java.util.List<TodoList> findAll(int start, int end);

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
	public java.util.List<TodoList> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TodoList>
			orderByComparator);

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
	public java.util.List<TodoList> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TodoList>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the todo lists from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of todo lists.
	 *
	 * @return the number of todo lists
	 */
	public int countAll();

}