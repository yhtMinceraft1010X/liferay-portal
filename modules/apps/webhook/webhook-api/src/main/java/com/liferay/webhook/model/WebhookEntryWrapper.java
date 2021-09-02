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

import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link WebhookEntry}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see WebhookEntry
 * @generated
 */
public class WebhookEntryWrapper
	extends BaseModelWrapper<WebhookEntry>
	implements ModelWrapper<WebhookEntry>, WebhookEntry {

	public WebhookEntryWrapper(WebhookEntry webhookEntry) {
		super(webhookEntry);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("uuid", getUuid());
		attributes.put("webhookEntryId", getWebhookEntryId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("active", isActive());
		attributes.put("destinationName", getDestinationName());
		attributes.put(
			"destinationWebhookEventKeys", getDestinationWebhookEventKeys());
		attributes.put("name", getName());
		attributes.put("secret", getSecret());
		attributes.put("url", getURL());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long webhookEntryId = (Long)attributes.get("webhookEntryId");

		if (webhookEntryId != null) {
			setWebhookEntryId(webhookEntryId);
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

		Boolean active = (Boolean)attributes.get("active");

		if (active != null) {
			setActive(active);
		}

		String destinationName = (String)attributes.get("destinationName");

		if (destinationName != null) {
			setDestinationName(destinationName);
		}

		String destinationWebhookEventKeys = (String)attributes.get(
			"destinationWebhookEventKeys");

		if (destinationWebhookEventKeys != null) {
			setDestinationWebhookEventKeys(destinationWebhookEventKeys);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String secret = (String)attributes.get("secret");

		if (secret != null) {
			setSecret(secret);
		}

		String url = (String)attributes.get("url");

		if (url != null) {
			setURL(url);
		}
	}

	@Override
	public WebhookEntry cloneWithOriginalValues() {
		return wrap(model.cloneWithOriginalValues());
	}

	/**
	 * Returns the active of this webhook entry.
	 *
	 * @return the active of this webhook entry
	 */
	@Override
	public boolean getActive() {
		return model.getActive();
	}

	/**
	 * Returns the company ID of this webhook entry.
	 *
	 * @return the company ID of this webhook entry
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this webhook entry.
	 *
	 * @return the create date of this webhook entry
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the destination name of this webhook entry.
	 *
	 * @return the destination name of this webhook entry
	 */
	@Override
	public String getDestinationName() {
		return model.getDestinationName();
	}

	/**
	 * Returns the destination webhook event keys of this webhook entry.
	 *
	 * @return the destination webhook event keys of this webhook entry
	 */
	@Override
	public String getDestinationWebhookEventKeys() {
		return model.getDestinationWebhookEventKeys();
	}

	/**
	 * Returns the modified date of this webhook entry.
	 *
	 * @return the modified date of this webhook entry
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the mvcc version of this webhook entry.
	 *
	 * @return the mvcc version of this webhook entry
	 */
	@Override
	public long getMvccVersion() {
		return model.getMvccVersion();
	}

	/**
	 * Returns the name of this webhook entry.
	 *
	 * @return the name of this webhook entry
	 */
	@Override
	public String getName() {
		return model.getName();
	}

	/**
	 * Returns the primary key of this webhook entry.
	 *
	 * @return the primary key of this webhook entry
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the secret of this webhook entry.
	 *
	 * @return the secret of this webhook entry
	 */
	@Override
	public String getSecret() {
		return model.getSecret();
	}

	/**
	 * Returns the url of this webhook entry.
	 *
	 * @return the url of this webhook entry
	 */
	@Override
	public String getURL() {
		return model.getURL();
	}

	/**
	 * Returns the user ID of this webhook entry.
	 *
	 * @return the user ID of this webhook entry
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this webhook entry.
	 *
	 * @return the user name of this webhook entry
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this webhook entry.
	 *
	 * @return the user uuid of this webhook entry
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * Returns the uuid of this webhook entry.
	 *
	 * @return the uuid of this webhook entry
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	/**
	 * Returns the webhook entry ID of this webhook entry.
	 *
	 * @return the webhook entry ID of this webhook entry
	 */
	@Override
	public long getWebhookEntryId() {
		return model.getWebhookEntryId();
	}

	/**
	 * Returns <code>true</code> if this webhook entry is active.
	 *
	 * @return <code>true</code> if this webhook entry is active; <code>false</code> otherwise
	 */
	@Override
	public boolean isActive() {
		return model.isActive();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets whether this webhook entry is active.
	 *
	 * @param active the active of this webhook entry
	 */
	@Override
	public void setActive(boolean active) {
		model.setActive(active);
	}

	/**
	 * Sets the company ID of this webhook entry.
	 *
	 * @param companyId the company ID of this webhook entry
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this webhook entry.
	 *
	 * @param createDate the create date of this webhook entry
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the destination name of this webhook entry.
	 *
	 * @param destinationName the destination name of this webhook entry
	 */
	@Override
	public void setDestinationName(String destinationName) {
		model.setDestinationName(destinationName);
	}

	/**
	 * Sets the destination webhook event keys of this webhook entry.
	 *
	 * @param destinationWebhookEventKeys the destination webhook event keys of this webhook entry
	 */
	@Override
	public void setDestinationWebhookEventKeys(
		String destinationWebhookEventKeys) {

		model.setDestinationWebhookEventKeys(destinationWebhookEventKeys);
	}

	/**
	 * Sets the modified date of this webhook entry.
	 *
	 * @param modifiedDate the modified date of this webhook entry
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the mvcc version of this webhook entry.
	 *
	 * @param mvccVersion the mvcc version of this webhook entry
	 */
	@Override
	public void setMvccVersion(long mvccVersion) {
		model.setMvccVersion(mvccVersion);
	}

	/**
	 * Sets the name of this webhook entry.
	 *
	 * @param name the name of this webhook entry
	 */
	@Override
	public void setName(String name) {
		model.setName(name);
	}

	/**
	 * Sets the primary key of this webhook entry.
	 *
	 * @param primaryKey the primary key of this webhook entry
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the secret of this webhook entry.
	 *
	 * @param secret the secret of this webhook entry
	 */
	@Override
	public void setSecret(String secret) {
		model.setSecret(secret);
	}

	/**
	 * Sets the url of this webhook entry.
	 *
	 * @param url the url of this webhook entry
	 */
	@Override
	public void setURL(String url) {
		model.setURL(url);
	}

	/**
	 * Sets the user ID of this webhook entry.
	 *
	 * @param userId the user ID of this webhook entry
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this webhook entry.
	 *
	 * @param userName the user name of this webhook entry
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this webhook entry.
	 *
	 * @param userUuid the user uuid of this webhook entry
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	/**
	 * Sets the uuid of this webhook entry.
	 *
	 * @param uuid the uuid of this webhook entry
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	/**
	 * Sets the webhook entry ID of this webhook entry.
	 *
	 * @param webhookEntryId the webhook entry ID of this webhook entry
	 */
	@Override
	public void setWebhookEntryId(long webhookEntryId) {
		model.setWebhookEntryId(webhookEntryId);
	}

	@Override
	public StagedModelType getStagedModelType() {
		return model.getStagedModelType();
	}

	@Override
	protected WebhookEntryWrapper wrap(WebhookEntry webhookEntry) {
		return new WebhookEntryWrapper(webhookEntry);
	}

}