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

package com.liferay.commerce.payment.model.impl;

import com.liferay.commerce.payment.model.CommercePaymentMethodGroupRel;
import com.liferay.commerce.payment.model.CommercePaymentMethodGroupRelModel;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.sql.Blob;
import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the CommercePaymentMethodGroupRel service. Represents a row in the &quot;CommercePaymentMethodGroupRel&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>CommercePaymentMethodGroupRelModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link CommercePaymentMethodGroupRelImpl}.
 * </p>
 *
 * @author Luca Pellizzon
 * @see CommercePaymentMethodGroupRelImpl
 * @generated
 */
@JSON(strict = true)
public class CommercePaymentMethodGroupRelModelImpl
	extends BaseModelImpl<CommercePaymentMethodGroupRel>
	implements CommercePaymentMethodGroupRelModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a commerce payment method group rel model instance should use the <code>CommercePaymentMethodGroupRel</code> interface instead.
	 */
	public static final String TABLE_NAME = "CommercePaymentMethodGroupRel";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT},
		{"CPaymentMethodGroupRelId", Types.BIGINT}, {"groupId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"userId", Types.BIGINT},
		{"userName", Types.VARCHAR}, {"createDate", Types.TIMESTAMP},
		{"modifiedDate", Types.TIMESTAMP}, {"name", Types.VARCHAR},
		{"description", Types.VARCHAR}, {"imageId", Types.BIGINT},
		{"engineKey", Types.VARCHAR}, {"priority", Types.DOUBLE},
		{"active_", Types.BOOLEAN}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("CPaymentMethodGroupRelId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("name", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("description", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("imageId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("engineKey", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("priority", Types.DOUBLE);
		TABLE_COLUMNS_MAP.put("active_", Types.BOOLEAN);
	}

	public static final String TABLE_SQL_CREATE =
		"create table CommercePaymentMethodGroupRel (mvccVersion LONG default 0 not null,CPaymentMethodGroupRelId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,name STRING null,description STRING null,imageId LONG,engineKey VARCHAR(75) null,priority DOUBLE,active_ BOOLEAN)";

	public static final String TABLE_SQL_DROP =
		"drop table CommercePaymentMethodGroupRel";

	public static final String ORDER_BY_JPQL =
		" ORDER BY commercePaymentMethodGroupRel.priority ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY CommercePaymentMethodGroupRel.priority ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static final boolean ENTITY_CACHE_ENABLED = true;

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static final boolean FINDER_CACHE_ENABLED = true;

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static final boolean COLUMN_BITMASK_ENABLED = true;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long ACTIVE_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long ENGINEKEY_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long GROUPID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long PRIORITY_COLUMN_BITMASK = 8L;

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(
		com.liferay.commerce.payment.service.util.ServiceProps.get(
			"lock.expiration.time.com.liferay.commerce.payment.model.CommercePaymentMethodGroupRel"));

	public CommercePaymentMethodGroupRelModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _commercePaymentMethodGroupRelId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setCommercePaymentMethodGroupRelId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _commercePaymentMethodGroupRelId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return CommercePaymentMethodGroupRel.class;
	}

	@Override
	public String getModelClassName() {
		return CommercePaymentMethodGroupRel.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<CommercePaymentMethodGroupRel, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<CommercePaymentMethodGroupRel, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<CommercePaymentMethodGroupRel, Object>
				attributeGetterFunction = entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply(
					(CommercePaymentMethodGroupRel)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<CommercePaymentMethodGroupRel, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<CommercePaymentMethodGroupRel, Object>
				attributeSetterBiConsumer = attributeSetterBiConsumers.get(
					attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(CommercePaymentMethodGroupRel)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<CommercePaymentMethodGroupRel, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<CommercePaymentMethodGroupRel, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map
		<String, Function<CommercePaymentMethodGroupRel, Object>>
			_attributeGetterFunctions;
	private static final Map
		<String, BiConsumer<CommercePaymentMethodGroupRel, Object>>
			_attributeSetterBiConsumers;

	static {
		Map<String, Function<CommercePaymentMethodGroupRel, Object>>
			attributeGetterFunctions =
				new LinkedHashMap
					<String, Function<CommercePaymentMethodGroupRel, Object>>();
		Map<String, BiConsumer<CommercePaymentMethodGroupRel, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap
					<String, BiConsumer<CommercePaymentMethodGroupRel, ?>>();

		attributeGetterFunctions.put(
			"mvccVersion", CommercePaymentMethodGroupRel::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<CommercePaymentMethodGroupRel, Long>)
				CommercePaymentMethodGroupRel::setMvccVersion);
		attributeGetterFunctions.put(
			"commercePaymentMethodGroupRelId",
			CommercePaymentMethodGroupRel::getCommercePaymentMethodGroupRelId);
		attributeSetterBiConsumers.put(
			"commercePaymentMethodGroupRelId",
			(BiConsumer<CommercePaymentMethodGroupRel, Long>)
				CommercePaymentMethodGroupRel::
					setCommercePaymentMethodGroupRelId);
		attributeGetterFunctions.put(
			"groupId", CommercePaymentMethodGroupRel::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId",
			(BiConsumer<CommercePaymentMethodGroupRel, Long>)
				CommercePaymentMethodGroupRel::setGroupId);
		attributeGetterFunctions.put(
			"companyId", CommercePaymentMethodGroupRel::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<CommercePaymentMethodGroupRel, Long>)
				CommercePaymentMethodGroupRel::setCompanyId);
		attributeGetterFunctions.put(
			"userId", CommercePaymentMethodGroupRel::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<CommercePaymentMethodGroupRel, Long>)
				CommercePaymentMethodGroupRel::setUserId);
		attributeGetterFunctions.put(
			"userName", CommercePaymentMethodGroupRel::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<CommercePaymentMethodGroupRel, String>)
				CommercePaymentMethodGroupRel::setUserName);
		attributeGetterFunctions.put(
			"createDate", CommercePaymentMethodGroupRel::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<CommercePaymentMethodGroupRel, Date>)
				CommercePaymentMethodGroupRel::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", CommercePaymentMethodGroupRel::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<CommercePaymentMethodGroupRel, Date>)
				CommercePaymentMethodGroupRel::setModifiedDate);
		attributeGetterFunctions.put(
			"name", CommercePaymentMethodGroupRel::getName);
		attributeSetterBiConsumers.put(
			"name",
			(BiConsumer<CommercePaymentMethodGroupRel, String>)
				CommercePaymentMethodGroupRel::setName);
		attributeGetterFunctions.put(
			"description", CommercePaymentMethodGroupRel::getDescription);
		attributeSetterBiConsumers.put(
			"description",
			(BiConsumer<CommercePaymentMethodGroupRel, String>)
				CommercePaymentMethodGroupRel::setDescription);
		attributeGetterFunctions.put(
			"imageId", CommercePaymentMethodGroupRel::getImageId);
		attributeSetterBiConsumers.put(
			"imageId",
			(BiConsumer<CommercePaymentMethodGroupRel, Long>)
				CommercePaymentMethodGroupRel::setImageId);
		attributeGetterFunctions.put(
			"engineKey", CommercePaymentMethodGroupRel::getEngineKey);
		attributeSetterBiConsumers.put(
			"engineKey",
			(BiConsumer<CommercePaymentMethodGroupRel, String>)
				CommercePaymentMethodGroupRel::setEngineKey);
		attributeGetterFunctions.put(
			"priority", CommercePaymentMethodGroupRel::getPriority);
		attributeSetterBiConsumers.put(
			"priority",
			(BiConsumer<CommercePaymentMethodGroupRel, Double>)
				CommercePaymentMethodGroupRel::setPriority);
		attributeGetterFunctions.put(
			"active", CommercePaymentMethodGroupRel::getActive);
		attributeSetterBiConsumers.put(
			"active",
			(BiConsumer<CommercePaymentMethodGroupRel, Boolean>)
				CommercePaymentMethodGroupRel::setActive);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@JSON
	@Override
	public long getMvccVersion() {
		return _mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_mvccVersion = mvccVersion;
	}

	@JSON
	@Override
	public long getCommercePaymentMethodGroupRelId() {
		return _commercePaymentMethodGroupRelId;
	}

	@Override
	public void setCommercePaymentMethodGroupRelId(
		long commercePaymentMethodGroupRelId) {

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_commercePaymentMethodGroupRelId = commercePaymentMethodGroupRelId;
	}

	@JSON
	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_groupId = groupId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalGroupId() {
		return GetterUtil.getLong(this.<Long>getColumnOriginalValue("groupId"));
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_companyId = companyId;
	}

	@JSON
	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException portalException) {
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@JSON
	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userName = userName;
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_createDate = createDate;
	}

	@JSON
	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_modifiedDate = modifiedDate;
	}

	@JSON
	@Override
	public String getName() {
		if (_name == null) {
			return "";
		}
		else {
			return _name;
		}
	}

	@Override
	public String getName(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getName(languageId);
	}

	@Override
	public String getName(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getName(languageId, useDefault);
	}

	@Override
	public String getName(String languageId) {
		return LocalizationUtil.getLocalization(getName(), languageId);
	}

	@Override
	public String getName(String languageId, boolean useDefault) {
		return LocalizationUtil.getLocalization(
			getName(), languageId, useDefault);
	}

	@Override
	public String getNameCurrentLanguageId() {
		return _nameCurrentLanguageId;
	}

	@JSON
	@Override
	public String getNameCurrentValue() {
		Locale locale = getLocale(_nameCurrentLanguageId);

		return getName(locale);
	}

	@Override
	public Map<Locale, String> getNameMap() {
		return LocalizationUtil.getLocalizationMap(getName());
	}

	@Override
	public void setName(String name) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_name = name;
	}

	@Override
	public void setName(String name, Locale locale) {
		setName(name, locale, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setName(String name, Locale locale, Locale defaultLocale) {
		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(name)) {
			setName(
				LocalizationUtil.updateLocalization(
					getName(), "Name", name, languageId, defaultLanguageId));
		}
		else {
			setName(
				LocalizationUtil.removeLocalization(
					getName(), "Name", languageId));
		}
	}

	@Override
	public void setNameCurrentLanguageId(String languageId) {
		_nameCurrentLanguageId = languageId;
	}

	@Override
	public void setNameMap(Map<Locale, String> nameMap) {
		setNameMap(nameMap, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setNameMap(Map<Locale, String> nameMap, Locale defaultLocale) {
		if (nameMap == null) {
			return;
		}

		setName(
			LocalizationUtil.updateLocalization(
				nameMap, getName(), "Name",
				LocaleUtil.toLanguageId(defaultLocale)));
	}

	@JSON
	@Override
	public String getDescription() {
		if (_description == null) {
			return "";
		}
		else {
			return _description;
		}
	}

	@Override
	public String getDescription(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getDescription(languageId);
	}

	@Override
	public String getDescription(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getDescription(languageId, useDefault);
	}

	@Override
	public String getDescription(String languageId) {
		return LocalizationUtil.getLocalization(getDescription(), languageId);
	}

	@Override
	public String getDescription(String languageId, boolean useDefault) {
		return LocalizationUtil.getLocalization(
			getDescription(), languageId, useDefault);
	}

	@Override
	public String getDescriptionCurrentLanguageId() {
		return _descriptionCurrentLanguageId;
	}

	@JSON
	@Override
	public String getDescriptionCurrentValue() {
		Locale locale = getLocale(_descriptionCurrentLanguageId);

		return getDescription(locale);
	}

	@Override
	public Map<Locale, String> getDescriptionMap() {
		return LocalizationUtil.getLocalizationMap(getDescription());
	}

	@Override
	public void setDescription(String description) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_description = description;
	}

	@Override
	public void setDescription(String description, Locale locale) {
		setDescription(description, locale, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setDescription(
		String description, Locale locale, Locale defaultLocale) {

		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(description)) {
			setDescription(
				LocalizationUtil.updateLocalization(
					getDescription(), "Description", description, languageId,
					defaultLanguageId));
		}
		else {
			setDescription(
				LocalizationUtil.removeLocalization(
					getDescription(), "Description", languageId));
		}
	}

	@Override
	public void setDescriptionCurrentLanguageId(String languageId) {
		_descriptionCurrentLanguageId = languageId;
	}

	@Override
	public void setDescriptionMap(Map<Locale, String> descriptionMap) {
		setDescriptionMap(descriptionMap, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setDescriptionMap(
		Map<Locale, String> descriptionMap, Locale defaultLocale) {

		if (descriptionMap == null) {
			return;
		}

		setDescription(
			LocalizationUtil.updateLocalization(
				descriptionMap, getDescription(), "Description",
				LocaleUtil.toLanguageId(defaultLocale)));
	}

	@JSON
	@Override
	public long getImageId() {
		return _imageId;
	}

	@Override
	public void setImageId(long imageId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_imageId = imageId;
	}

	@JSON
	@Override
	public String getEngineKey() {
		if (_engineKey == null) {
			return "";
		}
		else {
			return _engineKey;
		}
	}

	@Override
	public void setEngineKey(String engineKey) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_engineKey = engineKey;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalEngineKey() {
		return getColumnOriginalValue("engineKey");
	}

	@JSON
	@Override
	public double getPriority() {
		return _priority;
	}

	@Override
	public void setPriority(double priority) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_priority = priority;
	}

	@JSON
	@Override
	public boolean getActive() {
		return _active;
	}

	@JSON
	@Override
	public boolean isActive() {
		return _active;
	}

	@Override
	public void setActive(boolean active) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_active = active;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public boolean getOriginalActive() {
		return GetterUtil.getBoolean(
			this.<Boolean>getColumnOriginalValue("active_"));
	}

	public long getColumnBitmask() {
		if (_columnBitmask > 0) {
			return _columnBitmask;
		}

		if ((_columnOriginalValues == null) ||
			(_columnOriginalValues == Collections.EMPTY_MAP)) {

			return 0;
		}

		for (Map.Entry<String, Object> entry :
				_columnOriginalValues.entrySet()) {

			if (!Objects.equals(
					entry.getValue(), getColumnValue(entry.getKey()))) {

				_columnBitmask |= _columnBitmasks.get(entry.getKey());
			}
		}

		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), CommercePaymentMethodGroupRel.class.getName(),
			getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public String[] getAvailableLanguageIds() {
		Set<String> availableLanguageIds = new TreeSet<String>();

		Map<Locale, String> nameMap = getNameMap();

		for (Map.Entry<Locale, String> entry : nameMap.entrySet()) {
			Locale locale = entry.getKey();
			String value = entry.getValue();

			if (Validator.isNotNull(value)) {
				availableLanguageIds.add(LocaleUtil.toLanguageId(locale));
			}
		}

		Map<Locale, String> descriptionMap = getDescriptionMap();

		for (Map.Entry<Locale, String> entry : descriptionMap.entrySet()) {
			Locale locale = entry.getKey();
			String value = entry.getValue();

			if (Validator.isNotNull(value)) {
				availableLanguageIds.add(LocaleUtil.toLanguageId(locale));
			}
		}

		return availableLanguageIds.toArray(
			new String[availableLanguageIds.size()]);
	}

	@Override
	public String getDefaultLanguageId() {
		String xml = getName();

		if (xml == null) {
			return "";
		}

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		return LocalizationUtil.getDefaultLanguageId(xml, defaultLocale);
	}

	@Override
	public void prepareLocalizedFieldsForImport() throws LocaleException {
		Locale defaultLocale = LocaleUtil.fromLanguageId(
			getDefaultLanguageId());

		Locale[] availableLocales = LocaleUtil.fromLanguageIds(
			getAvailableLanguageIds());

		Locale defaultImportLocale = LocalizationUtil.getDefaultImportLocale(
			CommercePaymentMethodGroupRel.class.getName(), getPrimaryKey(),
			defaultLocale, availableLocales);

		prepareLocalizedFieldsForImport(defaultImportLocale);
	}

	@Override
	@SuppressWarnings("unused")
	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale)
		throws LocaleException {

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		String modelDefaultLanguageId = getDefaultLanguageId();

		String name = getName(defaultLocale);

		if (Validator.isNull(name)) {
			setName(getName(modelDefaultLanguageId), defaultLocale);
		}
		else {
			setName(getName(defaultLocale), defaultLocale, defaultLocale);
		}

		String description = getDescription(defaultLocale);

		if (Validator.isNull(description)) {
			setDescription(
				getDescription(modelDefaultLanguageId), defaultLocale);
		}
		else {
			setDescription(
				getDescription(defaultLocale), defaultLocale, defaultLocale);
		}
	}

	@Override
	public CommercePaymentMethodGroupRel toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, CommercePaymentMethodGroupRel>
				escapedModelProxyProviderFunction =
					EscapedModelProxyProviderFunctionHolder.
						_escapedModelProxyProviderFunction;

			_escapedModel = escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		CommercePaymentMethodGroupRelImpl commercePaymentMethodGroupRelImpl =
			new CommercePaymentMethodGroupRelImpl();

		commercePaymentMethodGroupRelImpl.setMvccVersion(getMvccVersion());
		commercePaymentMethodGroupRelImpl.setCommercePaymentMethodGroupRelId(
			getCommercePaymentMethodGroupRelId());
		commercePaymentMethodGroupRelImpl.setGroupId(getGroupId());
		commercePaymentMethodGroupRelImpl.setCompanyId(getCompanyId());
		commercePaymentMethodGroupRelImpl.setUserId(getUserId());
		commercePaymentMethodGroupRelImpl.setUserName(getUserName());
		commercePaymentMethodGroupRelImpl.setCreateDate(getCreateDate());
		commercePaymentMethodGroupRelImpl.setModifiedDate(getModifiedDate());
		commercePaymentMethodGroupRelImpl.setName(getName());
		commercePaymentMethodGroupRelImpl.setDescription(getDescription());
		commercePaymentMethodGroupRelImpl.setImageId(getImageId());
		commercePaymentMethodGroupRelImpl.setEngineKey(getEngineKey());
		commercePaymentMethodGroupRelImpl.setPriority(getPriority());
		commercePaymentMethodGroupRelImpl.setActive(isActive());

		commercePaymentMethodGroupRelImpl.resetOriginalValues();

		return commercePaymentMethodGroupRelImpl;
	}

	@Override
	public CommercePaymentMethodGroupRel cloneWithOriginalValues() {
		CommercePaymentMethodGroupRelImpl commercePaymentMethodGroupRelImpl =
			new CommercePaymentMethodGroupRelImpl();

		commercePaymentMethodGroupRelImpl.setMvccVersion(
			this.<Long>getColumnOriginalValue("mvccVersion"));
		commercePaymentMethodGroupRelImpl.setCommercePaymentMethodGroupRelId(
			this.<Long>getColumnOriginalValue("CPaymentMethodGroupRelId"));
		commercePaymentMethodGroupRelImpl.setGroupId(
			this.<Long>getColumnOriginalValue("groupId"));
		commercePaymentMethodGroupRelImpl.setCompanyId(
			this.<Long>getColumnOriginalValue("companyId"));
		commercePaymentMethodGroupRelImpl.setUserId(
			this.<Long>getColumnOriginalValue("userId"));
		commercePaymentMethodGroupRelImpl.setUserName(
			this.<String>getColumnOriginalValue("userName"));
		commercePaymentMethodGroupRelImpl.setCreateDate(
			this.<Date>getColumnOriginalValue("createDate"));
		commercePaymentMethodGroupRelImpl.setModifiedDate(
			this.<Date>getColumnOriginalValue("modifiedDate"));
		commercePaymentMethodGroupRelImpl.setName(
			this.<String>getColumnOriginalValue("name"));
		commercePaymentMethodGroupRelImpl.setDescription(
			this.<String>getColumnOriginalValue("description"));
		commercePaymentMethodGroupRelImpl.setImageId(
			this.<Long>getColumnOriginalValue("imageId"));
		commercePaymentMethodGroupRelImpl.setEngineKey(
			this.<String>getColumnOriginalValue("engineKey"));
		commercePaymentMethodGroupRelImpl.setPriority(
			this.<Double>getColumnOriginalValue("priority"));
		commercePaymentMethodGroupRelImpl.setActive(
			this.<Boolean>getColumnOriginalValue("active_"));

		return commercePaymentMethodGroupRelImpl;
	}

	@Override
	public int compareTo(
		CommercePaymentMethodGroupRel commercePaymentMethodGroupRel) {

		int value = 0;

		if (getPriority() < commercePaymentMethodGroupRel.getPriority()) {
			value = -1;
		}
		else if (getPriority() > commercePaymentMethodGroupRel.getPriority()) {
			value = 1;
		}
		else {
			value = 0;
		}

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof CommercePaymentMethodGroupRel)) {
			return false;
		}

		CommercePaymentMethodGroupRel commercePaymentMethodGroupRel =
			(CommercePaymentMethodGroupRel)object;

		long primaryKey = commercePaymentMethodGroupRel.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		_columnOriginalValues = Collections.emptyMap();

		_setModifiedDate = false;

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<CommercePaymentMethodGroupRel> toCacheModel() {
		CommercePaymentMethodGroupRelCacheModel
			commercePaymentMethodGroupRelCacheModel =
				new CommercePaymentMethodGroupRelCacheModel();

		commercePaymentMethodGroupRelCacheModel.mvccVersion = getMvccVersion();

		commercePaymentMethodGroupRelCacheModel.
			commercePaymentMethodGroupRelId =
				getCommercePaymentMethodGroupRelId();

		commercePaymentMethodGroupRelCacheModel.groupId = getGroupId();

		commercePaymentMethodGroupRelCacheModel.companyId = getCompanyId();

		commercePaymentMethodGroupRelCacheModel.userId = getUserId();

		commercePaymentMethodGroupRelCacheModel.userName = getUserName();

		String userName = commercePaymentMethodGroupRelCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			commercePaymentMethodGroupRelCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			commercePaymentMethodGroupRelCacheModel.createDate =
				createDate.getTime();
		}
		else {
			commercePaymentMethodGroupRelCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			commercePaymentMethodGroupRelCacheModel.modifiedDate =
				modifiedDate.getTime();
		}
		else {
			commercePaymentMethodGroupRelCacheModel.modifiedDate =
				Long.MIN_VALUE;
		}

		commercePaymentMethodGroupRelCacheModel.name = getName();

		String name = commercePaymentMethodGroupRelCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			commercePaymentMethodGroupRelCacheModel.name = null;
		}

		commercePaymentMethodGroupRelCacheModel.description = getDescription();

		String description =
			commercePaymentMethodGroupRelCacheModel.description;

		if ((description != null) && (description.length() == 0)) {
			commercePaymentMethodGroupRelCacheModel.description = null;
		}

		commercePaymentMethodGroupRelCacheModel.imageId = getImageId();

		commercePaymentMethodGroupRelCacheModel.engineKey = getEngineKey();

		String engineKey = commercePaymentMethodGroupRelCacheModel.engineKey;

		if ((engineKey != null) && (engineKey.length() == 0)) {
			commercePaymentMethodGroupRelCacheModel.engineKey = null;
		}

		commercePaymentMethodGroupRelCacheModel.priority = getPriority();

		commercePaymentMethodGroupRelCacheModel.active = isActive();

		return commercePaymentMethodGroupRelCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<CommercePaymentMethodGroupRel, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 2);

		sb.append("{");

		for (Map.Entry<String, Function<CommercePaymentMethodGroupRel, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<CommercePaymentMethodGroupRel, Object>
				attributeGetterFunction = entry.getValue();

			sb.append("\"");
			sb.append(attributeName);
			sb.append("\": ");

			Object value = attributeGetterFunction.apply(
				(CommercePaymentMethodGroupRel)this);

			if (value == null) {
				sb.append("null");
			}
			else if (value instanceof Blob || value instanceof Date ||
					 value instanceof Map || value instanceof String) {

				sb.append(
					"\"" + StringUtil.replace(value.toString(), "\"", "'") +
						"\"");
			}
			else {
				sb.append(value);
			}

			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		Map<String, Function<CommercePaymentMethodGroupRel, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<CommercePaymentMethodGroupRel, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<CommercePaymentMethodGroupRel, Object>
				attributeGetterFunction = entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(
				attributeGetterFunction.apply(
					(CommercePaymentMethodGroupRel)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function
			<InvocationHandler, CommercePaymentMethodGroupRel>
				_escapedModelProxyProviderFunction =
					ProxyUtil.getProxyProviderFunction(
						CommercePaymentMethodGroupRel.class,
						ModelWrapper.class);

	}

	private long _mvccVersion;
	private long _commercePaymentMethodGroupRelId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _name;
	private String _nameCurrentLanguageId;
	private String _description;
	private String _descriptionCurrentLanguageId;
	private long _imageId;
	private String _engineKey;
	private double _priority;
	private boolean _active;

	public <T> T getColumnValue(String columnName) {
		columnName = _attributeNames.getOrDefault(columnName, columnName);

		Function<CommercePaymentMethodGroupRel, Object> function =
			_attributeGetterFunctions.get(columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((CommercePaymentMethodGroupRel)this);
	}

	public <T> T getColumnOriginalValue(String columnName) {
		if (_columnOriginalValues == null) {
			return null;
		}

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		return (T)_columnOriginalValues.get(columnName);
	}

	private void _setColumnOriginalValues() {
		_columnOriginalValues = new HashMap<String, Object>();

		_columnOriginalValues.put("mvccVersion", _mvccVersion);
		_columnOriginalValues.put(
			"CPaymentMethodGroupRelId", _commercePaymentMethodGroupRelId);
		_columnOriginalValues.put("groupId", _groupId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put("name", _name);
		_columnOriginalValues.put("description", _description);
		_columnOriginalValues.put("imageId", _imageId);
		_columnOriginalValues.put("engineKey", _engineKey);
		_columnOriginalValues.put("priority", _priority);
		_columnOriginalValues.put("active_", _active);
	}

	private static final Map<String, String> _attributeNames;

	static {
		Map<String, String> attributeNames = new HashMap<>();

		attributeNames.put(
			"CPaymentMethodGroupRelId", "commercePaymentMethodGroupRelId");
		attributeNames.put("active_", "active");

		_attributeNames = Collections.unmodifiableMap(attributeNames);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("mvccVersion", 1L);

		columnBitmasks.put("CPaymentMethodGroupRelId", 2L);

		columnBitmasks.put("groupId", 4L);

		columnBitmasks.put("companyId", 8L);

		columnBitmasks.put("userId", 16L);

		columnBitmasks.put("userName", 32L);

		columnBitmasks.put("createDate", 64L);

		columnBitmasks.put("modifiedDate", 128L);

		columnBitmasks.put("name", 256L);

		columnBitmasks.put("description", 512L);

		columnBitmasks.put("imageId", 1024L);

		columnBitmasks.put("engineKey", 2048L);

		columnBitmasks.put("priority", 4096L);

		columnBitmasks.put("active_", 8192L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private CommercePaymentMethodGroupRel _escapedModel;

}