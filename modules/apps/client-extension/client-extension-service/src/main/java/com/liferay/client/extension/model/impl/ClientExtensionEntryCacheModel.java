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

package com.liferay.client.extension.model.impl;

import com.liferay.client.extension.model.ClientExtensionEntry;
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
 * The cache model class for representing ClientExtensionEntry in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class ClientExtensionEntryCacheModel
	implements CacheModel<ClientExtensionEntry>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ClientExtensionEntryCacheModel)) {
			return false;
		}

		ClientExtensionEntryCacheModel clientExtensionEntryCacheModel =
			(ClientExtensionEntryCacheModel)object;

		if ((clientExtensionEntryId ==
				clientExtensionEntryCacheModel.clientExtensionEntryId) &&
			(mvccVersion == clientExtensionEntryCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, clientExtensionEntryId);

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
		StringBundler sb = new StringBundler(53);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", uuid=");
		sb.append(uuid);
		sb.append(", externalReferenceCode=");
		sb.append(externalReferenceCode);
		sb.append(", clientExtensionEntryId=");
		sb.append(clientExtensionEntryId);
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
		sb.append(", customElementCSSURLs=");
		sb.append(customElementCSSURLs);
		sb.append(", customElementHTMLElementName=");
		sb.append(customElementHTMLElementName);
		sb.append(", customElementURLs=");
		sb.append(customElementURLs);
		sb.append(", customElementUseESM=");
		sb.append(customElementUseESM);
		sb.append(", description=");
		sb.append(description);
		sb.append(", friendlyURLMapping=");
		sb.append(friendlyURLMapping);
		sb.append(", iFrameURL=");
		sb.append(iFrameURL);
		sb.append(", instanceable=");
		sb.append(instanceable);
		sb.append(", name=");
		sb.append(name);
		sb.append(", portletCategoryName=");
		sb.append(portletCategoryName);
		sb.append(", properties=");
		sb.append(properties);
		sb.append(", sourceCodeURL=");
		sb.append(sourceCodeURL);
		sb.append(", type=");
		sb.append(type);
		sb.append(", status=");
		sb.append(status);
		sb.append(", statusByUserId=");
		sb.append(statusByUserId);
		sb.append(", statusByUserName=");
		sb.append(statusByUserName);
		sb.append(", statusDate=");
		sb.append(statusDate);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public ClientExtensionEntry toEntityModel() {
		ClientExtensionEntryImpl clientExtensionEntryImpl =
			new ClientExtensionEntryImpl();

		clientExtensionEntryImpl.setMvccVersion(mvccVersion);

		if (uuid == null) {
			clientExtensionEntryImpl.setUuid("");
		}
		else {
			clientExtensionEntryImpl.setUuid(uuid);
		}

		if (externalReferenceCode == null) {
			clientExtensionEntryImpl.setExternalReferenceCode("");
		}
		else {
			clientExtensionEntryImpl.setExternalReferenceCode(
				externalReferenceCode);
		}

		clientExtensionEntryImpl.setClientExtensionEntryId(
			clientExtensionEntryId);
		clientExtensionEntryImpl.setCompanyId(companyId);
		clientExtensionEntryImpl.setUserId(userId);

		if (userName == null) {
			clientExtensionEntryImpl.setUserName("");
		}
		else {
			clientExtensionEntryImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			clientExtensionEntryImpl.setCreateDate(null);
		}
		else {
			clientExtensionEntryImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			clientExtensionEntryImpl.setModifiedDate(null);
		}
		else {
			clientExtensionEntryImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (customElementCSSURLs == null) {
			clientExtensionEntryImpl.setCustomElementCSSURLs("");
		}
		else {
			clientExtensionEntryImpl.setCustomElementCSSURLs(
				customElementCSSURLs);
		}

		if (customElementHTMLElementName == null) {
			clientExtensionEntryImpl.setCustomElementHTMLElementName("");
		}
		else {
			clientExtensionEntryImpl.setCustomElementHTMLElementName(
				customElementHTMLElementName);
		}

		if (customElementURLs == null) {
			clientExtensionEntryImpl.setCustomElementURLs("");
		}
		else {
			clientExtensionEntryImpl.setCustomElementURLs(customElementURLs);
		}

		clientExtensionEntryImpl.setCustomElementUseESM(customElementUseESM);

		if (description == null) {
			clientExtensionEntryImpl.setDescription("");
		}
		else {
			clientExtensionEntryImpl.setDescription(description);
		}

		if (friendlyURLMapping == null) {
			clientExtensionEntryImpl.setFriendlyURLMapping("");
		}
		else {
			clientExtensionEntryImpl.setFriendlyURLMapping(friendlyURLMapping);
		}

		if (iFrameURL == null) {
			clientExtensionEntryImpl.setIFrameURL("");
		}
		else {
			clientExtensionEntryImpl.setIFrameURL(iFrameURL);
		}

		clientExtensionEntryImpl.setInstanceable(instanceable);

		if (name == null) {
			clientExtensionEntryImpl.setName("");
		}
		else {
			clientExtensionEntryImpl.setName(name);
		}

		if (portletCategoryName == null) {
			clientExtensionEntryImpl.setPortletCategoryName("");
		}
		else {
			clientExtensionEntryImpl.setPortletCategoryName(
				portletCategoryName);
		}

		if (properties == null) {
			clientExtensionEntryImpl.setProperties("");
		}
		else {
			clientExtensionEntryImpl.setProperties(properties);
		}

		if (sourceCodeURL == null) {
			clientExtensionEntryImpl.setSourceCodeURL("");
		}
		else {
			clientExtensionEntryImpl.setSourceCodeURL(sourceCodeURL);
		}

		if (type == null) {
			clientExtensionEntryImpl.setType("");
		}
		else {
			clientExtensionEntryImpl.setType(type);
		}

		clientExtensionEntryImpl.setStatus(status);
		clientExtensionEntryImpl.setStatusByUserId(statusByUserId);

		if (statusByUserName == null) {
			clientExtensionEntryImpl.setStatusByUserName("");
		}
		else {
			clientExtensionEntryImpl.setStatusByUserName(statusByUserName);
		}

		if (statusDate == Long.MIN_VALUE) {
			clientExtensionEntryImpl.setStatusDate(null);
		}
		else {
			clientExtensionEntryImpl.setStatusDate(new Date(statusDate));
		}

		clientExtensionEntryImpl.resetOriginalValues();

		return clientExtensionEntryImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput)
		throws ClassNotFoundException, IOException {

		mvccVersion = objectInput.readLong();
		uuid = objectInput.readUTF();
		externalReferenceCode = objectInput.readUTF();

		clientExtensionEntryId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		customElementCSSURLs = (String)objectInput.readObject();
		customElementHTMLElementName = objectInput.readUTF();
		customElementURLs = (String)objectInput.readObject();

		customElementUseESM = objectInput.readBoolean();
		description = (String)objectInput.readObject();
		friendlyURLMapping = objectInput.readUTF();
		iFrameURL = objectInput.readUTF();

		instanceable = objectInput.readBoolean();
		name = objectInput.readUTF();
		portletCategoryName = objectInput.readUTF();
		properties = (String)objectInput.readObject();
		sourceCodeURL = objectInput.readUTF();
		type = objectInput.readUTF();

		status = objectInput.readInt();

		statusByUserId = objectInput.readLong();
		statusByUserName = objectInput.readUTF();
		statusDate = objectInput.readLong();
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

		if (externalReferenceCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(externalReferenceCode);
		}

		objectOutput.writeLong(clientExtensionEntryId);

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

		if (customElementCSSURLs == null) {
			objectOutput.writeObject("");
		}
		else {
			objectOutput.writeObject(customElementCSSURLs);
		}

		if (customElementHTMLElementName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(customElementHTMLElementName);
		}

		if (customElementURLs == null) {
			objectOutput.writeObject("");
		}
		else {
			objectOutput.writeObject(customElementURLs);
		}

		objectOutput.writeBoolean(customElementUseESM);

		if (description == null) {
			objectOutput.writeObject("");
		}
		else {
			objectOutput.writeObject(description);
		}

		if (friendlyURLMapping == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(friendlyURLMapping);
		}

		if (iFrameURL == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(iFrameURL);
		}

		objectOutput.writeBoolean(instanceable);

		if (name == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(name);
		}

		if (portletCategoryName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(portletCategoryName);
		}

		if (properties == null) {
			objectOutput.writeObject("");
		}
		else {
			objectOutput.writeObject(properties);
		}

		if (sourceCodeURL == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(sourceCodeURL);
		}

		if (type == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(type);
		}

		objectOutput.writeInt(status);

		objectOutput.writeLong(statusByUserId);

		if (statusByUserName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(statusByUserName);
		}

		objectOutput.writeLong(statusDate);
	}

	public long mvccVersion;
	public String uuid;
	public String externalReferenceCode;
	public long clientExtensionEntryId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public String customElementCSSURLs;
	public String customElementHTMLElementName;
	public String customElementURLs;
	public boolean customElementUseESM;
	public String description;
	public String friendlyURLMapping;
	public String iFrameURL;
	public boolean instanceable;
	public String name;
	public String portletCategoryName;
	public String properties;
	public String sourceCodeURL;
	public String type;
	public int status;
	public long statusByUserId;
	public String statusByUserName;
	public long statusDate;

}