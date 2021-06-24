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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author Brian Wing Shun Chan
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 * @generated
 */
@Deprecated
public class TodoListSoap implements Serializable {

	public static TodoListSoap toSoapModel(TodoList model) {
		TodoListSoap soapModel = new TodoListSoap();

		soapModel.setMvccVersion(model.getMvccVersion());
		soapModel.setTodoListId(model.getTodoListId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setName(model.getName());

		return soapModel;
	}

	public static TodoListSoap[] toSoapModels(TodoList[] models) {
		TodoListSoap[] soapModels = new TodoListSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static TodoListSoap[][] toSoapModels(TodoList[][] models) {
		TodoListSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new TodoListSoap[models.length][models[0].length];
		}
		else {
			soapModels = new TodoListSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static TodoListSoap[] toSoapModels(List<TodoList> models) {
		List<TodoListSoap> soapModels = new ArrayList<TodoListSoap>(
			models.size());

		for (TodoList model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new TodoListSoap[soapModels.size()]);
	}

	public TodoListSoap() {
	}

	public long getPrimaryKey() {
		return _todoListId;
	}

	public void setPrimaryKey(long pk) {
		setTodoListId(pk);
	}

	public long getMvccVersion() {
		return _mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	public long getTodoListId() {
		return _todoListId;
	}

	public void setTodoListId(long todoListId) {
		_todoListId = todoListId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	private long _mvccVersion;
	private long _todoListId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private String _name;

}