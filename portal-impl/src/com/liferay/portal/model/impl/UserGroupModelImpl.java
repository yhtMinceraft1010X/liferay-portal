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

package com.liferay.portal.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.model.UserGroupModel;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.sql.Blob;
import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the UserGroup service. Represents a row in the &quot;UserGroup&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>UserGroupModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link UserGroupImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see UserGroupImpl
 * @generated
 */
@JSON(strict = true)
public class UserGroupModelImpl
	extends BaseModelImpl<UserGroup> implements UserGroupModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a user group model instance should use the <code>UserGroup</code> interface instead.
	 */
	public static final String TABLE_NAME = "UserGroup";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"ctCollectionId", Types.BIGINT},
		{"uuid_", Types.VARCHAR}, {"externalReferenceCode", Types.VARCHAR},
		{"userGroupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"parentUserGroupId", Types.BIGINT}, {"name", Types.VARCHAR},
		{"description", Types.VARCHAR}, {"addedByLDAPImport", Types.BOOLEAN}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("ctCollectionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("externalReferenceCode", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("userGroupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("parentUserGroupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("name", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("description", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("addedByLDAPImport", Types.BOOLEAN);
	}

	public static final String TABLE_SQL_CREATE =
		"create table UserGroup (mvccVersion LONG default 0 not null,ctCollectionId LONG default 0 not null,uuid_ VARCHAR(75) null,externalReferenceCode VARCHAR(75) null,userGroupId LONG not null,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,parentUserGroupId LONG,name VARCHAR(255) null,description STRING null,addedByLDAPImport BOOLEAN,primary key (userGroupId, ctCollectionId))";

	public static final String TABLE_SQL_DROP = "drop table UserGroup";

	public static final String ORDER_BY_JPQL = " ORDER BY userGroup.name ASC";

	public static final String ORDER_BY_SQL = " ORDER BY UserGroup.name ASC";

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
	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long EXTERNALREFERENCECODE_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long NAME_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long PARENTUSERGROUPID_COLUMN_BITMASK = 8L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long USERGROUPID_COLUMN_BITMASK = 16L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long UUID_COLUMN_BITMASK = 32L;

	public static final String MAPPING_TABLE_GROUPS_USERGROUPS_NAME =
		"Groups_UserGroups";

	public static final Object[][] MAPPING_TABLE_GROUPS_USERGROUPS_COLUMNS = {
		{"companyId", Types.BIGINT}, {"groupId", Types.BIGINT},
		{"userGroupId", Types.BIGINT}
	};

	public static final String MAPPING_TABLE_GROUPS_USERGROUPS_SQL_CREATE =
		"create table Groups_UserGroups (companyId LONG not null,groupId LONG not null,userGroupId LONG not null,ctCollectionId LONG default 0 not null,ctChangeType BOOLEAN,primary key (groupId, userGroupId, ctCollectionId))";

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static final boolean FINDER_CACHE_ENABLED_GROUPS_USERGROUPS = true;

	public static final String MAPPING_TABLE_USERGROUPS_TEAMS_NAME =
		"UserGroups_Teams";

	public static final Object[][] MAPPING_TABLE_USERGROUPS_TEAMS_COLUMNS = {
		{"companyId", Types.BIGINT}, {"teamId", Types.BIGINT},
		{"userGroupId", Types.BIGINT}
	};

	public static final String MAPPING_TABLE_USERGROUPS_TEAMS_SQL_CREATE =
		"create table UserGroups_Teams (companyId LONG not null,teamId LONG not null,userGroupId LONG not null,ctCollectionId LONG default 0 not null,ctChangeType BOOLEAN,primary key (teamId, userGroupId, ctCollectionId))";

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static final boolean FINDER_CACHE_ENABLED_USERGROUPS_TEAMS = true;

	public static final String MAPPING_TABLE_USERS_USERGROUPS_NAME =
		"Users_UserGroups";

	public static final Object[][] MAPPING_TABLE_USERS_USERGROUPS_COLUMNS = {
		{"companyId", Types.BIGINT}, {"userId", Types.BIGINT},
		{"userGroupId", Types.BIGINT}
	};

	public static final String MAPPING_TABLE_USERS_USERGROUPS_SQL_CREATE =
		"create table Users_UserGroups (companyId LONG not null,userId LONG not null,userGroupId LONG not null,ctCollectionId LONG default 0 not null,ctChangeType BOOLEAN,primary key (userId, userGroupId, ctCollectionId))";

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static final boolean FINDER_CACHE_ENABLED_USERS_USERGROUPS = true;

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(
		com.liferay.portal.util.PropsUtil.get(
			"lock.expiration.time.com.liferay.portal.kernel.model.UserGroup"));

	public UserGroupModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _userGroupId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setUserGroupId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _userGroupId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return UserGroup.class;
	}

	@Override
	public String getModelClassName() {
		return UserGroup.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<UserGroup, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<UserGroup, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<UserGroup, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName, attributeGetterFunction.apply((UserGroup)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<UserGroup, Object>> attributeSetterBiConsumers =
			getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<UserGroup, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(UserGroup)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<UserGroup, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<UserGroup, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<UserGroup, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<UserGroup, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<UserGroup, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<UserGroup, Object>>();
		Map<String, BiConsumer<UserGroup, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<UserGroup, ?>>();

		attributeGetterFunctions.put("mvccVersion", UserGroup::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<UserGroup, Long>)UserGroup::setMvccVersion);
		attributeGetterFunctions.put(
			"ctCollectionId", UserGroup::getCtCollectionId);
		attributeSetterBiConsumers.put(
			"ctCollectionId",
			(BiConsumer<UserGroup, Long>)UserGroup::setCtCollectionId);
		attributeGetterFunctions.put("uuid", UserGroup::getUuid);
		attributeSetterBiConsumers.put(
			"uuid", (BiConsumer<UserGroup, String>)UserGroup::setUuid);
		attributeGetterFunctions.put(
			"externalReferenceCode", UserGroup::getExternalReferenceCode);
		attributeSetterBiConsumers.put(
			"externalReferenceCode",
			(BiConsumer<UserGroup, String>)UserGroup::setExternalReferenceCode);
		attributeGetterFunctions.put("userGroupId", UserGroup::getUserGroupId);
		attributeSetterBiConsumers.put(
			"userGroupId",
			(BiConsumer<UserGroup, Long>)UserGroup::setUserGroupId);
		attributeGetterFunctions.put("companyId", UserGroup::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId", (BiConsumer<UserGroup, Long>)UserGroup::setCompanyId);
		attributeGetterFunctions.put("userId", UserGroup::getUserId);
		attributeSetterBiConsumers.put(
			"userId", (BiConsumer<UserGroup, Long>)UserGroup::setUserId);
		attributeGetterFunctions.put("userName", UserGroup::getUserName);
		attributeSetterBiConsumers.put(
			"userName", (BiConsumer<UserGroup, String>)UserGroup::setUserName);
		attributeGetterFunctions.put("createDate", UserGroup::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<UserGroup, Date>)UserGroup::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", UserGroup::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<UserGroup, Date>)UserGroup::setModifiedDate);
		attributeGetterFunctions.put(
			"parentUserGroupId", UserGroup::getParentUserGroupId);
		attributeSetterBiConsumers.put(
			"parentUserGroupId",
			(BiConsumer<UserGroup, Long>)UserGroup::setParentUserGroupId);
		attributeGetterFunctions.put("name", UserGroup::getName);
		attributeSetterBiConsumers.put(
			"name", (BiConsumer<UserGroup, String>)UserGroup::setName);
		attributeGetterFunctions.put("description", UserGroup::getDescription);
		attributeSetterBiConsumers.put(
			"description",
			(BiConsumer<UserGroup, String>)UserGroup::setDescription);
		attributeGetterFunctions.put(
			"addedByLDAPImport", UserGroup::getAddedByLDAPImport);
		attributeSetterBiConsumers.put(
			"addedByLDAPImport",
			(BiConsumer<UserGroup, Boolean>)UserGroup::setAddedByLDAPImport);

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
	public long getCtCollectionId() {
		return _ctCollectionId;
	}

	@Override
	public void setCtCollectionId(long ctCollectionId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_ctCollectionId = ctCollectionId;
	}

	@JSON
	@Override
	public String getUuid() {
		if (_uuid == null) {
			return "";
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_uuid = uuid;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalUuid() {
		return getColumnOriginalValue("uuid_");
	}

	@JSON
	@Override
	public String getExternalReferenceCode() {
		if (_externalReferenceCode == null) {
			return "";
		}
		else {
			return _externalReferenceCode;
		}
	}

	@Override
	public void setExternalReferenceCode(String externalReferenceCode) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_externalReferenceCode = externalReferenceCode;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalExternalReferenceCode() {
		return getColumnOriginalValue("externalReferenceCode");
	}

	@JSON
	@Override
	public long getUserGroupId() {
		return _userGroupId;
	}

	@Override
	public void setUserGroupId(long userGroupId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userGroupId = userGroupId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalUserGroupId() {
		return GetterUtil.getLong(
			this.<Long>getColumnOriginalValue("userGroupId"));
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

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalCompanyId() {
		return GetterUtil.getLong(
			this.<Long>getColumnOriginalValue("companyId"));
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
	public long getParentUserGroupId() {
		return _parentUserGroupId;
	}

	@Override
	public void setParentUserGroupId(long parentUserGroupId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_parentUserGroupId = parentUserGroupId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalParentUserGroupId() {
		return GetterUtil.getLong(
			this.<Long>getColumnOriginalValue("parentUserGroupId"));
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
	public void setName(String name) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_name = name;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalName() {
		return getColumnOriginalValue("name");
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
	public void setDescription(String description) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_description = description;
	}

	@JSON
	@Override
	public boolean getAddedByLDAPImport() {
		return _addedByLDAPImport;
	}

	@JSON
	@Override
	public boolean isAddedByLDAPImport() {
		return _addedByLDAPImport;
	}

	@Override
	public void setAddedByLDAPImport(boolean addedByLDAPImport) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_addedByLDAPImport = addedByLDAPImport;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(UserGroup.class.getName()));
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
			getCompanyId(), UserGroup.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public UserGroup toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, UserGroup>
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
		UserGroupImpl userGroupImpl = new UserGroupImpl();

		userGroupImpl.setMvccVersion(getMvccVersion());
		userGroupImpl.setCtCollectionId(getCtCollectionId());
		userGroupImpl.setUuid(getUuid());
		userGroupImpl.setExternalReferenceCode(getExternalReferenceCode());
		userGroupImpl.setUserGroupId(getUserGroupId());
		userGroupImpl.setCompanyId(getCompanyId());
		userGroupImpl.setUserId(getUserId());
		userGroupImpl.setUserName(getUserName());
		userGroupImpl.setCreateDate(getCreateDate());
		userGroupImpl.setModifiedDate(getModifiedDate());
		userGroupImpl.setParentUserGroupId(getParentUserGroupId());
		userGroupImpl.setName(getName());
		userGroupImpl.setDescription(getDescription());
		userGroupImpl.setAddedByLDAPImport(isAddedByLDAPImport());

		userGroupImpl.resetOriginalValues();

		return userGroupImpl;
	}

	@Override
	public UserGroup cloneWithOriginalValues() {
		UserGroupImpl userGroupImpl = new UserGroupImpl();

		userGroupImpl.setMvccVersion(
			this.<Long>getColumnOriginalValue("mvccVersion"));
		userGroupImpl.setCtCollectionId(
			this.<Long>getColumnOriginalValue("ctCollectionId"));
		userGroupImpl.setUuid(this.<String>getColumnOriginalValue("uuid_"));
		userGroupImpl.setExternalReferenceCode(
			this.<String>getColumnOriginalValue("externalReferenceCode"));
		userGroupImpl.setUserGroupId(
			this.<Long>getColumnOriginalValue("userGroupId"));
		userGroupImpl.setCompanyId(
			this.<Long>getColumnOriginalValue("companyId"));
		userGroupImpl.setUserId(this.<Long>getColumnOriginalValue("userId"));
		userGroupImpl.setUserName(
			this.<String>getColumnOriginalValue("userName"));
		userGroupImpl.setCreateDate(
			this.<Date>getColumnOriginalValue("createDate"));
		userGroupImpl.setModifiedDate(
			this.<Date>getColumnOriginalValue("modifiedDate"));
		userGroupImpl.setParentUserGroupId(
			this.<Long>getColumnOriginalValue("parentUserGroupId"));
		userGroupImpl.setName(this.<String>getColumnOriginalValue("name"));
		userGroupImpl.setDescription(
			this.<String>getColumnOriginalValue("description"));
		userGroupImpl.setAddedByLDAPImport(
			this.<Boolean>getColumnOriginalValue("addedByLDAPImport"));

		return userGroupImpl;
	}

	@Override
	public int compareTo(UserGroup userGroup) {
		int value = 0;

		value = getName().compareTo(userGroup.getName());

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

		if (!(object instanceof UserGroup)) {
			return false;
		}

		UserGroup userGroup = (UserGroup)object;

		long primaryKey = userGroup.getPrimaryKey();

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
	public CacheModel<UserGroup> toCacheModel() {
		UserGroupCacheModel userGroupCacheModel = new UserGroupCacheModel();

		userGroupCacheModel.mvccVersion = getMvccVersion();

		userGroupCacheModel.ctCollectionId = getCtCollectionId();

		userGroupCacheModel.uuid = getUuid();

		String uuid = userGroupCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			userGroupCacheModel.uuid = null;
		}

		userGroupCacheModel.externalReferenceCode = getExternalReferenceCode();

		String externalReferenceCode =
			userGroupCacheModel.externalReferenceCode;

		if ((externalReferenceCode != null) &&
			(externalReferenceCode.length() == 0)) {

			userGroupCacheModel.externalReferenceCode = null;
		}

		userGroupCacheModel.userGroupId = getUserGroupId();

		userGroupCacheModel.companyId = getCompanyId();

		userGroupCacheModel.userId = getUserId();

		userGroupCacheModel.userName = getUserName();

		String userName = userGroupCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			userGroupCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			userGroupCacheModel.createDate = createDate.getTime();
		}
		else {
			userGroupCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			userGroupCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			userGroupCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		userGroupCacheModel.parentUserGroupId = getParentUserGroupId();

		userGroupCacheModel.name = getName();

		String name = userGroupCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			userGroupCacheModel.name = null;
		}

		userGroupCacheModel.description = getDescription();

		String description = userGroupCacheModel.description;

		if ((description != null) && (description.length() == 0)) {
			userGroupCacheModel.description = null;
		}

		userGroupCacheModel.addedByLDAPImport = isAddedByLDAPImport();

		return userGroupCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<UserGroup, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 2);

		sb.append("{");

		for (Map.Entry<String, Function<UserGroup, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<UserGroup, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("\"");
			sb.append(attributeName);
			sb.append("\": ");

			Object value = attributeGetterFunction.apply((UserGroup)this);

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
		Map<String, Function<UserGroup, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<UserGroup, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<UserGroup, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((UserGroup)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, UserGroup>
			_escapedModelProxyProviderFunction =
				ProxyUtil.getProxyProviderFunction(
					UserGroup.class, ModelWrapper.class);

	}

	private long _mvccVersion;
	private long _ctCollectionId;
	private String _uuid;
	private String _externalReferenceCode;
	private long _userGroupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private long _parentUserGroupId;
	private String _name;
	private String _description;
	private boolean _addedByLDAPImport;

	public <T> T getColumnValue(String columnName) {
		columnName = _attributeNames.getOrDefault(columnName, columnName);

		Function<UserGroup, Object> function = _attributeGetterFunctions.get(
			columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((UserGroup)this);
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
		_columnOriginalValues.put("ctCollectionId", _ctCollectionId);
		_columnOriginalValues.put("uuid_", _uuid);
		_columnOriginalValues.put(
			"externalReferenceCode", _externalReferenceCode);
		_columnOriginalValues.put("userGroupId", _userGroupId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put("parentUserGroupId", _parentUserGroupId);
		_columnOriginalValues.put("name", _name);
		_columnOriginalValues.put("description", _description);
		_columnOriginalValues.put("addedByLDAPImport", _addedByLDAPImport);
	}

	private static final Map<String, String> _attributeNames;

	static {
		Map<String, String> attributeNames = new HashMap<>();

		attributeNames.put("uuid_", "uuid");
		attributeNames.put("groups_", "groups");

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

		columnBitmasks.put("ctCollectionId", 2L);

		columnBitmasks.put("uuid_", 4L);

		columnBitmasks.put("externalReferenceCode", 8L);

		columnBitmasks.put("userGroupId", 16L);

		columnBitmasks.put("companyId", 32L);

		columnBitmasks.put("userId", 64L);

		columnBitmasks.put("userName", 128L);

		columnBitmasks.put("createDate", 256L);

		columnBitmasks.put("modifiedDate", 512L);

		columnBitmasks.put("parentUserGroupId", 1024L);

		columnBitmasks.put("name", 2048L);

		columnBitmasks.put("description", 4096L);

		columnBitmasks.put("addedByLDAPImport", 8192L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private UserGroup _escapedModel;

}