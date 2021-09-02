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

package com.liferay.webhook.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.liferay.webhook.service.http.WebhookEntryServiceSoap}.
 *
 * @author Brian Wing Shun Chan
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 * @generated
 */
@Deprecated
public class WebhookEntrySoap implements Serializable {

	public static WebhookEntrySoap toSoapModel(WebhookEntry model) {
		WebhookEntrySoap soapModel = new WebhookEntrySoap();

		soapModel.setMvccVersion(model.getMvccVersion());
		soapModel.setUuid(model.getUuid());
		soapModel.setWebhookEntryId(model.getWebhookEntryId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setActive(model.isActive());
		soapModel.setMessageBusDestinationName(
			model.getMessageBusDestinationName());
		soapModel.setName(model.getName());
		soapModel.setURL(model.getURL());

		return soapModel;
	}

	public static WebhookEntrySoap[] toSoapModels(WebhookEntry[] models) {
		WebhookEntrySoap[] soapModels = new WebhookEntrySoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static WebhookEntrySoap[][] toSoapModels(WebhookEntry[][] models) {
		WebhookEntrySoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new WebhookEntrySoap[models.length][models[0].length];
		}
		else {
			soapModels = new WebhookEntrySoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static WebhookEntrySoap[] toSoapModels(List<WebhookEntry> models) {
		List<WebhookEntrySoap> soapModels = new ArrayList<WebhookEntrySoap>(
			models.size());

		for (WebhookEntry model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new WebhookEntrySoap[soapModels.size()]);
	}

	public WebhookEntrySoap() {
	}

	public long getPrimaryKey() {
		return _webhookEntryId;
	}

	public void setPrimaryKey(long pk) {
		setWebhookEntryId(pk);
	}

	public long getMvccVersion() {
		return _mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getWebhookEntryId() {
		return _webhookEntryId;
	}

	public void setWebhookEntryId(long webhookEntryId) {
		_webhookEntryId = webhookEntryId;
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

	public boolean getActive() {
		return _active;
	}

	public boolean isActive() {
		return _active;
	}

	public void setActive(boolean active) {
		_active = active;
	}

	public String getMessageBusDestinationName() {
		return _messageBusDestinationName;
	}

	public void setMessageBusDestinationName(String messageBusDestinationName) {
		_messageBusDestinationName = messageBusDestinationName;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getURL() {
		return _url;
	}

	public void setURL(String url) {
		_url = url;
	}

	private long _mvccVersion;
	private String _uuid;
	private long _webhookEntryId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _active;
	private String _messageBusDestinationName;
	private String _name;
	private String _url;

}