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

package com.liferay.alloy.mvc.sample.model;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link TodoItem}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TodoItem
 * @generated
 */
public class TodoItemWrapper
	extends BaseModelWrapper<TodoItem>
	implements ModelWrapper<TodoItem>, TodoItem {

	public TodoItemWrapper(TodoItem todoItem) {
		super(todoItem);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("todoItemId", getTodoItemId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("todoListId", getTodoListId());
		attributes.put("description", getDescription());
		attributes.put("priority", getPriority());
		attributes.put("status", getStatus());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		Long todoItemId = (Long)attributes.get("todoItemId");

		if (todoItemId != null) {
			setTodoItemId(todoItemId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long todoListId = (Long)attributes.get("todoListId");

		if (todoListId != null) {
			setTodoListId(todoListId);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		Integer priority = (Integer)attributes.get("priority");

		if (priority != null) {
			setPriority(priority);
		}

		Integer status = (Integer)attributes.get("status");

		if (status != null) {
			setStatus(status);
		}
	}

	/**
	 * Returns the company ID of this todo item.
	 *
	 * @return the company ID of this todo item
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this todo item.
	 *
	 * @return the create date of this todo item
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the description of this todo item.
	 *
	 * @return the description of this todo item
	 */
	@Override
	public String getDescription() {
		return model.getDescription();
	}

	/**
	 * Returns the modified date of this todo item.
	 *
	 * @return the modified date of this todo item
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the mvcc version of this todo item.
	 *
	 * @return the mvcc version of this todo item
	 */
	@Override
	public long getMvccVersion() {
		return model.getMvccVersion();
	}

	/**
	 * Returns the primary key of this todo item.
	 *
	 * @return the primary key of this todo item
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the priority of this todo item.
	 *
	 * @return the priority of this todo item
	 */
	@Override
	public int getPriority() {
		return model.getPriority();
	}

	/**
	 * Returns the status of this todo item.
	 *
	 * @return the status of this todo item
	 */
	@Override
	public int getStatus() {
		return model.getStatus();
	}

	/**
	 * Returns the todo item ID of this todo item.
	 *
	 * @return the todo item ID of this todo item
	 */
	@Override
	public long getTodoItemId() {
		return model.getTodoItemId();
	}

	/**
	 * Returns the todo list ID of this todo item.
	 *
	 * @return the todo list ID of this todo item
	 */
	@Override
	public long getTodoListId() {
		return model.getTodoListId();
	}

	/**
	 * Returns the user ID of this todo item.
	 *
	 * @return the user ID of this todo item
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this todo item.
	 *
	 * @return the user name of this todo item
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this todo item.
	 *
	 * @return the user uuid of this todo item
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the company ID of this todo item.
	 *
	 * @param companyId the company ID of this todo item
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this todo item.
	 *
	 * @param createDate the create date of this todo item
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the description of this todo item.
	 *
	 * @param description the description of this todo item
	 */
	@Override
	public void setDescription(String description) {
		model.setDescription(description);
	}

	/**
	 * Sets the modified date of this todo item.
	 *
	 * @param modifiedDate the modified date of this todo item
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the mvcc version of this todo item.
	 *
	 * @param mvccVersion the mvcc version of this todo item
	 */
	@Override
	public void setMvccVersion(long mvccVersion) {
		model.setMvccVersion(mvccVersion);
	}

	/**
	 * Sets the primary key of this todo item.
	 *
	 * @param primaryKey the primary key of this todo item
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the priority of this todo item.
	 *
	 * @param priority the priority of this todo item
	 */
	@Override
	public void setPriority(int priority) {
		model.setPriority(priority);
	}

	/**
	 * Sets the status of this todo item.
	 *
	 * @param status the status of this todo item
	 */
	@Override
	public void setStatus(int status) {
		model.setStatus(status);
	}

	/**
	 * Sets the todo item ID of this todo item.
	 *
	 * @param todoItemId the todo item ID of this todo item
	 */
	@Override
	public void setTodoItemId(long todoItemId) {
		model.setTodoItemId(todoItemId);
	}

	/**
	 * Sets the todo list ID of this todo item.
	 *
	 * @param todoListId the todo list ID of this todo item
	 */
	@Override
	public void setTodoListId(long todoListId) {
		model.setTodoListId(todoListId);
	}

	/**
	 * Sets the user ID of this todo item.
	 *
	 * @param userId the user ID of this todo item
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this todo item.
	 *
	 * @param userName the user name of this todo item
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this todo item.
	 *
	 * @param userUuid the user uuid of this todo item
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	@Override
	protected TodoItemWrapper wrap(TodoItem todoItem) {
		return new TodoItemWrapper(todoItem);
	}

}