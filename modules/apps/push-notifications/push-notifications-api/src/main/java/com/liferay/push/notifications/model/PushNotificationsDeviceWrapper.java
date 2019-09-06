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

package com.liferay.push.notifications.model;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class is a wrapper for {@link PushNotificationsDevice}.
 * </p>
 *
 * @author Bruno Farache
 * @see PushNotificationsDevice
 * @generated
 */
public class PushNotificationsDeviceWrapper
	implements PushNotificationsDevice, ModelWrapper<PushNotificationsDevice> {

	public PushNotificationsDeviceWrapper(
		PushNotificationsDevice pushNotificationsDevice) {

		_pushNotificationsDevice = pushNotificationsDevice;
	}

	@Override
	public Class<?> getModelClass() {
		return PushNotificationsDevice.class;
	}

	@Override
	public String getModelClassName() {
		return PushNotificationsDevice.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put(
			"pushNotificationsDeviceId", getPushNotificationsDeviceId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("createDate", getCreateDate());
		attributes.put("platform", getPlatform());
		attributes.put("token", getToken());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long pushNotificationsDeviceId = (Long)attributes.get(
			"pushNotificationsDeviceId");

		if (pushNotificationsDeviceId != null) {
			setPushNotificationsDeviceId(pushNotificationsDeviceId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		String platform = (String)attributes.get("platform");

		if (platform != null) {
			setPlatform(platform);
		}

		String token = (String)attributes.get("token");

		if (token != null) {
			setToken(token);
		}
	}

	@Override
	public Object clone() {
		return new PushNotificationsDeviceWrapper(
			(PushNotificationsDevice)_pushNotificationsDevice.clone());
	}

	@Override
	public int compareTo(PushNotificationsDevice pushNotificationsDevice) {
		return _pushNotificationsDevice.compareTo(pushNotificationsDevice);
	}

	/**
	 * Returns the company ID of this push notifications device.
	 *
	 * @return the company ID of this push notifications device
	 */
	@Override
	public long getCompanyId() {
		return _pushNotificationsDevice.getCompanyId();
	}

	/**
	 * Returns the create date of this push notifications device.
	 *
	 * @return the create date of this push notifications device
	 */
	@Override
	public Date getCreateDate() {
		return _pushNotificationsDevice.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _pushNotificationsDevice.getExpandoBridge();
	}

	/**
	 * Returns the platform of this push notifications device.
	 *
	 * @return the platform of this push notifications device
	 */
	@Override
	public String getPlatform() {
		return _pushNotificationsDevice.getPlatform();
	}

	/**
	 * Returns the primary key of this push notifications device.
	 *
	 * @return the primary key of this push notifications device
	 */
	@Override
	public long getPrimaryKey() {
		return _pushNotificationsDevice.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _pushNotificationsDevice.getPrimaryKeyObj();
	}

	/**
	 * Returns the push notifications device ID of this push notifications device.
	 *
	 * @return the push notifications device ID of this push notifications device
	 */
	@Override
	public long getPushNotificationsDeviceId() {
		return _pushNotificationsDevice.getPushNotificationsDeviceId();
	}

	/**
	 * Returns the token of this push notifications device.
	 *
	 * @return the token of this push notifications device
	 */
	@Override
	public String getToken() {
		return _pushNotificationsDevice.getToken();
	}

	/**
	 * Returns the user ID of this push notifications device.
	 *
	 * @return the user ID of this push notifications device
	 */
	@Override
	public long getUserId() {
		return _pushNotificationsDevice.getUserId();
	}

	/**
	 * Returns the user uuid of this push notifications device.
	 *
	 * @return the user uuid of this push notifications device
	 */
	@Override
	public String getUserUuid() {
		return _pushNotificationsDevice.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _pushNotificationsDevice.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _pushNotificationsDevice.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _pushNotificationsDevice.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _pushNotificationsDevice.isNew();
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a push notifications device model instance should use the <code>PushNotificationsDevice</code> interface instead.
	 */
	@Override
	public void persist() {
		_pushNotificationsDevice.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_pushNotificationsDevice.setCachedModel(cachedModel);
	}

	/**
	 * Sets the company ID of this push notifications device.
	 *
	 * @param companyId the company ID of this push notifications device
	 */
	@Override
	public void setCompanyId(long companyId) {
		_pushNotificationsDevice.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this push notifications device.
	 *
	 * @param createDate the create date of this push notifications device
	 */
	@Override
	public void setCreateDate(Date createDate) {
		_pushNotificationsDevice.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {

		_pushNotificationsDevice.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_pushNotificationsDevice.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_pushNotificationsDevice.setExpandoBridgeAttributes(serviceContext);
	}

	@Override
	public void setNew(boolean n) {
		_pushNotificationsDevice.setNew(n);
	}

	/**
	 * Sets the platform of this push notifications device.
	 *
	 * @param platform the platform of this push notifications device
	 */
	@Override
	public void setPlatform(String platform) {
		_pushNotificationsDevice.setPlatform(platform);
	}

	/**
	 * Sets the primary key of this push notifications device.
	 *
	 * @param primaryKey the primary key of this push notifications device
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		_pushNotificationsDevice.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_pushNotificationsDevice.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	 * Sets the push notifications device ID of this push notifications device.
	 *
	 * @param pushNotificationsDeviceId the push notifications device ID of this push notifications device
	 */
	@Override
	public void setPushNotificationsDeviceId(long pushNotificationsDeviceId) {
		_pushNotificationsDevice.setPushNotificationsDeviceId(
			pushNotificationsDeviceId);
	}

	/**
	 * Sets the token of this push notifications device.
	 *
	 * @param token the token of this push notifications device
	 */
	@Override
	public void setToken(String token) {
		_pushNotificationsDevice.setToken(token);
	}

	/**
	 * Sets the user ID of this push notifications device.
	 *
	 * @param userId the user ID of this push notifications device
	 */
	@Override
	public void setUserId(long userId) {
		_pushNotificationsDevice.setUserId(userId);
	}

	/**
	 * Sets the user uuid of this push notifications device.
	 *
	 * @param userUuid the user uuid of this push notifications device
	 */
	@Override
	public void setUserUuid(String userUuid) {
		_pushNotificationsDevice.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<PushNotificationsDevice>
		toCacheModel() {

		return _pushNotificationsDevice.toCacheModel();
	}

	@Override
	public PushNotificationsDevice toEscapedModel() {
		return new PushNotificationsDeviceWrapper(
			_pushNotificationsDevice.toEscapedModel());
	}

	@Override
	public String toString() {
		return _pushNotificationsDevice.toString();
	}

	@Override
	public PushNotificationsDevice toUnescapedModel() {
		return new PushNotificationsDeviceWrapper(
			_pushNotificationsDevice.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _pushNotificationsDevice.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PushNotificationsDeviceWrapper)) {
			return false;
		}

		PushNotificationsDeviceWrapper pushNotificationsDeviceWrapper =
			(PushNotificationsDeviceWrapper)obj;

		if (Objects.equals(
				_pushNotificationsDevice,
				pushNotificationsDeviceWrapper._pushNotificationsDevice)) {

			return true;
		}

		return false;
	}

	@Override
	public PushNotificationsDevice getWrappedModel() {
		return _pushNotificationsDevice;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _pushNotificationsDevice.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _pushNotificationsDevice.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_pushNotificationsDevice.resetOriginalValues();
	}

	private final PushNotificationsDevice _pushNotificationsDevice;

}