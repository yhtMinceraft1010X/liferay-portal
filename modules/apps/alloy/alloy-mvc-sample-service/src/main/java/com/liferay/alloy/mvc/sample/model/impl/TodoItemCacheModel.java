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

package com.liferay.alloy.mvc.sample.model.impl;

import com.liferay.alloy.mvc.sample.model.TodoItem;
import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing TodoItem in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class TodoItemCacheModel
	implements CacheModel<TodoItem>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof TodoItemCacheModel)) {
			return false;
		}

		TodoItemCacheModel todoItemCacheModel = (TodoItemCacheModel)object;

		if ((todoItemId == todoItemCacheModel.todoItemId) &&
			(mvccVersion == todoItemCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, todoItemId);

		return HashUtil.hash(hashCode, mvccVersion);
	}

	@Override
	public long getMvccVersion() {
		return mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		this.mvccVersion = mvccVersion;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(23);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", todoItemId=");
		sb.append(todoItemId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", todoListId=");
		sb.append(todoListId);
		sb.append(", description=");
		sb.append(description);
		sb.append(", priority=");
		sb.append(priority);
		sb.append(", status=");
		sb.append(status);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public TodoItem toEntityModel() {
		TodoItemImpl todoItemImpl = new TodoItemImpl();

		todoItemImpl.setMvccVersion(mvccVersion);
		todoItemImpl.setTodoItemId(todoItemId);
		todoItemImpl.setCompanyId(companyId);
		todoItemImpl.setUserId(userId);

		if (userName == null) {
			todoItemImpl.setUserName("");
		}
		else {
			todoItemImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			todoItemImpl.setCreateDate(null);
		}
		else {
			todoItemImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			todoItemImpl.setModifiedDate(null);
		}
		else {
			todoItemImpl.setModifiedDate(new Date(modifiedDate));
		}

		todoItemImpl.setTodoListId(todoListId);

		if (description == null) {
			todoItemImpl.setDescription("");
		}
		else {
			todoItemImpl.setDescription(description);
		}

		todoItemImpl.setPriority(priority);
		todoItemImpl.setStatus(status);

		todoItemImpl.resetOriginalValues();

		return todoItemImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();

		todoItemId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		todoListId = objectInput.readLong();
		description = objectInput.readUTF();

		priority = objectInput.readInt();

		status = objectInput.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(todoItemId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeLong(todoListId);

		if (description == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(description);
		}

		objectOutput.writeInt(priority);

		objectOutput.writeInt(status);
	}

	public long mvccVersion;
	public long todoItemId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long todoListId;
	public String description;
	public int priority;
	public int status;

}