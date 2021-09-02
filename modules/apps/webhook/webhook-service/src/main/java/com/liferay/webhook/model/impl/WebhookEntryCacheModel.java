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

package com.liferay.webhook.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.webhook.model.WebhookEntry;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing WebhookEntry in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class WebhookEntryCacheModel
	implements CacheModel<WebhookEntry>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof WebhookEntryCacheModel)) {
			return false;
		}

		WebhookEntryCacheModel webhookEntryCacheModel =
			(WebhookEntryCacheModel)object;

		if ((webhookEntryId == webhookEntryCacheModel.webhookEntryId) &&
			(mvccVersion == webhookEntryCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, webhookEntryId);

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
		StringBundler sb = new StringBundler(25);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", uuid=");
		sb.append(uuid);
		sb.append(", webhookEntryId=");
		sb.append(webhookEntryId);
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
		sb.append(", active=");
		sb.append(active);
		sb.append(", messageBusDestinationName=");
		sb.append(messageBusDestinationName);
		sb.append(", name=");
		sb.append(name);
		sb.append(", url=");
		sb.append(url);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public WebhookEntry toEntityModel() {
		WebhookEntryImpl webhookEntryImpl = new WebhookEntryImpl();

		webhookEntryImpl.setMvccVersion(mvccVersion);

		if (uuid == null) {
			webhookEntryImpl.setUuid("");
		}
		else {
			webhookEntryImpl.setUuid(uuid);
		}

		webhookEntryImpl.setWebhookEntryId(webhookEntryId);
		webhookEntryImpl.setCompanyId(companyId);
		webhookEntryImpl.setUserId(userId);

		if (userName == null) {
			webhookEntryImpl.setUserName("");
		}
		else {
			webhookEntryImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			webhookEntryImpl.setCreateDate(null);
		}
		else {
			webhookEntryImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			webhookEntryImpl.setModifiedDate(null);
		}
		else {
			webhookEntryImpl.setModifiedDate(new Date(modifiedDate));
		}

		webhookEntryImpl.setActive(active);

		if (messageBusDestinationName == null) {
			webhookEntryImpl.setMessageBusDestinationName("");
		}
		else {
			webhookEntryImpl.setMessageBusDestinationName(
				messageBusDestinationName);
		}

		if (name == null) {
			webhookEntryImpl.setName("");
		}
		else {
			webhookEntryImpl.setName(name);
		}

		if (url == null) {
			webhookEntryImpl.setURL("");
		}
		else {
			webhookEntryImpl.setURL(url);
		}

		webhookEntryImpl.resetOriginalValues();

		return webhookEntryImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();
		uuid = objectInput.readUTF();

		webhookEntryId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		active = objectInput.readBoolean();
		messageBusDestinationName = objectInput.readUTF();
		name = objectInput.readUTF();
		url = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(webhookEntryId);

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

		objectOutput.writeBoolean(active);

		if (messageBusDestinationName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(messageBusDestinationName);
		}

		if (name == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(name);
		}

		if (url == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(url);
		}
	}

	public long mvccVersion;
	public String uuid;
	public long webhookEntryId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public boolean active;
	public String messageBusDestinationName;
	public String name;
	public String url;

}