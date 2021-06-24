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

import com.liferay.alloy.mvc.sample.exception.NoSuchTodoItemException;
import com.liferay.alloy.mvc.sample.model.TodoItem;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the todo item service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TodoItemUtil
 * @generated
 */
@ProviderType
public interface TodoItemPersistence extends BasePersistence<TodoItem> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link TodoItemUtil} to access the todo item persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Caches the todo item in the entity cache if it is enabled.
	 *
	 * @param todoItem the todo item
	 */
	public void cacheResult(TodoItem todoItem);

	/**
	 * Caches the todo items in the entity cache if it is enabled.
	 *
	 * @param todoItems the todo items
	 */
	public void cacheResult(java.util.List<TodoItem> todoItems);

	/**
	 * Creates a new todo item with the primary key. Does not add the todo item to the database.
	 *
	 * @param todoItemId the primary key for the new todo item
	 * @return the new todo item
	 */
	public TodoItem create(long todoItemId);

	/**
	 * Removes the todo item with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param todoItemId the primary key of the todo item
	 * @return the todo item that was removed
	 * @throws NoSuchTodoItemException if a todo item with the primary key could not be found
	 */
	public TodoItem remove(long todoItemId) throws NoSuchTodoItemException;

	public TodoItem updateImpl(TodoItem todoItem);

	/**
	 * Returns the todo item with the primary key or throws a <code>NoSuchTodoItemException</code> if it could not be found.
	 *
	 * @param todoItemId the primary key of the todo item
	 * @return the todo item
	 * @throws NoSuchTodoItemException if a todo item with the primary key could not be found
	 */
	public TodoItem findByPrimaryKey(long todoItemId)
		throws NoSuchTodoItemException;

	/**
	 * Returns the todo item with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param todoItemId the primary key of the todo item
	 * @return the todo item, or <code>null</code> if a todo item with the primary key could not be found
	 */
	public TodoItem fetchByPrimaryKey(long todoItemId);

	/**
	 * Returns all the todo items.
	 *
	 * @return the todo items
	 */
	public java.util.List<TodoItem> findAll();

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
	public java.util.List<TodoItem> findAll(int start, int end);

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
	public java.util.List<TodoItem> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TodoItem>
			orderByComparator);

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
	public java.util.List<TodoItem> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TodoItem>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the todo items from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of todo items.
	 *
	 * @return the number of todo items
	 */
	public int countAll();

}