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

package com.liferay.analytics.message.storage.model.impl;

import com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage;
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
 * The cache model class for representing AnalyticsDeleteMessage in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class AnalyticsDeleteMessageCacheModel
	implements CacheModel<AnalyticsDeleteMessage>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof AnalyticsDeleteMessageCacheModel)) {
			return false;
		}

		AnalyticsDeleteMessageCacheModel analyticsDeleteMessageCacheModel =
			(AnalyticsDeleteMessageCacheModel)object;

		if ((analyticsDeleteMessageId ==
				analyticsDeleteMessageCacheModel.analyticsDeleteMessageId) &&
			(mvccVersion == analyticsDeleteMessageCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, analyticsDeleteMessageId);

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
		StringBundler sb = new StringBundler(17);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", analyticsDeleteMessageId=");
		sb.append(analyticsDeleteMessageId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", className=");
		sb.append(className);
		sb.append(", classPK=");
		sb.append(classPK);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public AnalyticsDeleteMessage toEntityModel() {
		AnalyticsDeleteMessageImpl analyticsDeleteMessageImpl =
			new AnalyticsDeleteMessageImpl();

		analyticsDeleteMessageImpl.setMvccVersion(mvccVersion);
		analyticsDeleteMessageImpl.setAnalyticsDeleteMessageId(
			analyticsDeleteMessageId);
		analyticsDeleteMessageImpl.setCompanyId(companyId);
		analyticsDeleteMessageImpl.setUserId(userId);

		if (createDate == Long.MIN_VALUE) {
			analyticsDeleteMessageImpl.setCreateDate(null);
		}
		else {
			analyticsDeleteMessageImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			analyticsDeleteMessageImpl.setModifiedDate(null);
		}
		else {
			analyticsDeleteMessageImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (className == null) {
			analyticsDeleteMessageImpl.setClassName("");
		}
		else {
			analyticsDeleteMessageImpl.setClassName(className);
		}

		analyticsDeleteMessageImpl.setClassPK(classPK);

		analyticsDeleteMessageImpl.resetOriginalValues();

		return analyticsDeleteMessageImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();

		analyticsDeleteMessageId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		className = objectInput.readUTF();

		classPK = objectInput.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(analyticsDeleteMessageId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (className == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(className);
		}

		objectOutput.writeLong(classPK);
	}

	public long mvccVersion;
	public long analyticsDeleteMessageId;
	public long companyId;
	public long userId;
	public long createDate;
	public long modifiedDate;
	public String className;
	public long classPK;

}