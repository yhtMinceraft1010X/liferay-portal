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
import com.liferay.analytics.message.storage.model.AnalyticsDeleteMessageModel;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
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
 * The base model implementation for the AnalyticsDeleteMessage service. Represents a row in the &quot;AnalyticsDeleteMessage&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>AnalyticsDeleteMessageModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AnalyticsDeleteMessageImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AnalyticsDeleteMessageImpl
 * @generated
 */
public class AnalyticsDeleteMessageModelImpl
	extends BaseModelImpl<AnalyticsDeleteMessage>
	implements AnalyticsDeleteMessageModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a analytics delete message model instance should use the <code>AnalyticsDeleteMessage</code> interface instead.
	 */
	public static final String TABLE_NAME = "AnalyticsDeleteMessage";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT},
		{"analyticsDeleteMessageId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"createDate", Types.TIMESTAMP},
		{"modifiedDate", Types.TIMESTAMP}, {"className", Types.VARCHAR},
		{"classPK", Types.BIGINT}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("analyticsDeleteMessageId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("className", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("classPK", Types.BIGINT);
	}

	public static final String TABLE_SQL_CREATE =
		"create table AnalyticsDeleteMessage (mvccVersion LONG default 0 not null,analyticsDeleteMessageId LONG not null primary key,companyId LONG,userId LONG,createDate DATE null,modifiedDate DATE null,className VARCHAR(255) null,classPK LONG)";

	public static final String TABLE_SQL_DROP =
		"drop table AnalyticsDeleteMessage";

	public static final String ORDER_BY_JPQL =
		" ORDER BY analyticsDeleteMessage.analyticsDeleteMessageId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY AnalyticsDeleteMessage.analyticsDeleteMessageId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long MODIFIEDDATE_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long ANALYTICSDELETEMESSAGEID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setEntityCacheEnabled(boolean entityCacheEnabled) {
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setFinderCacheEnabled(boolean finderCacheEnabled) {
	}

	public AnalyticsDeleteMessageModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _analyticsDeleteMessageId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setAnalyticsDeleteMessageId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _analyticsDeleteMessageId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return AnalyticsDeleteMessage.class;
	}

	@Override
	public String getModelClassName() {
		return AnalyticsDeleteMessage.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<AnalyticsDeleteMessage, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<AnalyticsDeleteMessage, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AnalyticsDeleteMessage, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((AnalyticsDeleteMessage)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<AnalyticsDeleteMessage, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<AnalyticsDeleteMessage, Object>
				attributeSetterBiConsumer = attributeSetterBiConsumers.get(
					attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(AnalyticsDeleteMessage)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<AnalyticsDeleteMessage, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<AnalyticsDeleteMessage, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<AnalyticsDeleteMessage, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<AnalyticsDeleteMessage, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<AnalyticsDeleteMessage, Object>>
			attributeGetterFunctions =
				new LinkedHashMap
					<String, Function<AnalyticsDeleteMessage, Object>>();
		Map<String, BiConsumer<AnalyticsDeleteMessage, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap
					<String, BiConsumer<AnalyticsDeleteMessage, ?>>();

		attributeGetterFunctions.put(
			"mvccVersion", AnalyticsDeleteMessage::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<AnalyticsDeleteMessage, Long>)
				AnalyticsDeleteMessage::setMvccVersion);
		attributeGetterFunctions.put(
			"analyticsDeleteMessageId",
			AnalyticsDeleteMessage::getAnalyticsDeleteMessageId);
		attributeSetterBiConsumers.put(
			"analyticsDeleteMessageId",
			(BiConsumer<AnalyticsDeleteMessage, Long>)
				AnalyticsDeleteMessage::setAnalyticsDeleteMessageId);
		attributeGetterFunctions.put(
			"companyId", AnalyticsDeleteMessage::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<AnalyticsDeleteMessage, Long>)
				AnalyticsDeleteMessage::setCompanyId);
		attributeGetterFunctions.put(
			"userId", AnalyticsDeleteMessage::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<AnalyticsDeleteMessage, Long>)
				AnalyticsDeleteMessage::setUserId);
		attributeGetterFunctions.put(
			"createDate", AnalyticsDeleteMessage::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<AnalyticsDeleteMessage, Date>)
				AnalyticsDeleteMessage::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", AnalyticsDeleteMessage::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<AnalyticsDeleteMessage, Date>)
				AnalyticsDeleteMessage::setModifiedDate);
		attributeGetterFunctions.put(
			"className", AnalyticsDeleteMessage::getClassName);
		attributeSetterBiConsumers.put(
			"className",
			(BiConsumer<AnalyticsDeleteMessage, String>)
				AnalyticsDeleteMessage::setClassName);
		attributeGetterFunctions.put(
			"classPK", AnalyticsDeleteMessage::getClassPK);
		attributeSetterBiConsumers.put(
			"classPK",
			(BiConsumer<AnalyticsDeleteMessage, Long>)
				AnalyticsDeleteMessage::setClassPK);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

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

	@Override
	public long getAnalyticsDeleteMessageId() {
		return _analyticsDeleteMessageId;
	}

	@Override
	public void setAnalyticsDeleteMessageId(long analyticsDeleteMessageId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_analyticsDeleteMessageId = analyticsDeleteMessageId;
	}

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

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public Date getOriginalModifiedDate() {
		return getColumnOriginalValue("modifiedDate");
	}

	@Override
	public String getClassName() {
		if (_className == null) {
			return "";
		}
		else {
			return _className;
		}
	}

	@Override
	public void setClassName(String className) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_className = className;
	}

	@Override
	public long getClassPK() {
		return _classPK;
	}

	@Override
	public void setClassPK(long classPK) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_classPK = classPK;
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
			getCompanyId(), AnalyticsDeleteMessage.class.getName(),
			getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public AnalyticsDeleteMessage toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, AnalyticsDeleteMessage>
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
		AnalyticsDeleteMessageImpl analyticsDeleteMessageImpl =
			new AnalyticsDeleteMessageImpl();

		analyticsDeleteMessageImpl.setMvccVersion(getMvccVersion());
		analyticsDeleteMessageImpl.setAnalyticsDeleteMessageId(
			getAnalyticsDeleteMessageId());
		analyticsDeleteMessageImpl.setCompanyId(getCompanyId());
		analyticsDeleteMessageImpl.setUserId(getUserId());
		analyticsDeleteMessageImpl.setCreateDate(getCreateDate());
		analyticsDeleteMessageImpl.setModifiedDate(getModifiedDate());
		analyticsDeleteMessageImpl.setClassName(getClassName());
		analyticsDeleteMessageImpl.setClassPK(getClassPK());

		analyticsDeleteMessageImpl.resetOriginalValues();

		return analyticsDeleteMessageImpl;
	}

	@Override
	public AnalyticsDeleteMessage cloneWithOriginalValues() {
		AnalyticsDeleteMessageImpl analyticsDeleteMessageImpl =
			new AnalyticsDeleteMessageImpl();

		analyticsDeleteMessageImpl.setMvccVersion(
			this.<Long>getColumnOriginalValue("mvccVersion"));
		analyticsDeleteMessageImpl.setAnalyticsDeleteMessageId(
			this.<Long>getColumnOriginalValue("analyticsDeleteMessageId"));
		analyticsDeleteMessageImpl.setCompanyId(
			this.<Long>getColumnOriginalValue("companyId"));
		analyticsDeleteMessageImpl.setUserId(
			this.<Long>getColumnOriginalValue("userId"));
		analyticsDeleteMessageImpl.setCreateDate(
			this.<Date>getColumnOriginalValue("createDate"));
		analyticsDeleteMessageImpl.setModifiedDate(
			this.<Date>getColumnOriginalValue("modifiedDate"));
		analyticsDeleteMessageImpl.setClassName(
			this.<String>getColumnOriginalValue("className"));
		analyticsDeleteMessageImpl.setClassPK(
			this.<Long>getColumnOriginalValue("classPK"));

		return analyticsDeleteMessageImpl;
	}

	@Override
	public int compareTo(AnalyticsDeleteMessage analyticsDeleteMessage) {
		long primaryKey = analyticsDeleteMessage.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof AnalyticsDeleteMessage)) {
			return false;
		}

		AnalyticsDeleteMessage analyticsDeleteMessage =
			(AnalyticsDeleteMessage)object;

		long primaryKey = analyticsDeleteMessage.getPrimaryKey();

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
		return true;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isFinderCacheEnabled() {
		return true;
	}

	@Override
	public void resetOriginalValues() {
		_columnOriginalValues = Collections.emptyMap();

		_setModifiedDate = false;

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<AnalyticsDeleteMessage> toCacheModel() {
		AnalyticsDeleteMessageCacheModel analyticsDeleteMessageCacheModel =
			new AnalyticsDeleteMessageCacheModel();

		analyticsDeleteMessageCacheModel.mvccVersion = getMvccVersion();

		analyticsDeleteMessageCacheModel.analyticsDeleteMessageId =
			getAnalyticsDeleteMessageId();

		analyticsDeleteMessageCacheModel.companyId = getCompanyId();

		analyticsDeleteMessageCacheModel.userId = getUserId();

		Date createDate = getCreateDate();

		if (createDate != null) {
			analyticsDeleteMessageCacheModel.createDate = createDate.getTime();
		}
		else {
			analyticsDeleteMessageCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			analyticsDeleteMessageCacheModel.modifiedDate =
				modifiedDate.getTime();
		}
		else {
			analyticsDeleteMessageCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		analyticsDeleteMessageCacheModel.className = getClassName();

		String className = analyticsDeleteMessageCacheModel.className;

		if ((className != null) && (className.length() == 0)) {
			analyticsDeleteMessageCacheModel.className = null;
		}

		analyticsDeleteMessageCacheModel.classPK = getClassPK();

		return analyticsDeleteMessageCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<AnalyticsDeleteMessage, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 2);

		sb.append("{");

		for (Map.Entry<String, Function<AnalyticsDeleteMessage, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AnalyticsDeleteMessage, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("\"");
			sb.append(attributeName);
			sb.append("\": ");

			Object value = attributeGetterFunction.apply(
				(AnalyticsDeleteMessage)this);

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
		Map<String, Function<AnalyticsDeleteMessage, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<AnalyticsDeleteMessage, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AnalyticsDeleteMessage, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(
				attributeGetterFunction.apply((AnalyticsDeleteMessage)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, AnalyticsDeleteMessage>
			_escapedModelProxyProviderFunction =
				ProxyUtil.getProxyProviderFunction(
					AnalyticsDeleteMessage.class, ModelWrapper.class);

	}

	private long _mvccVersion;
	private long _analyticsDeleteMessageId;
	private long _companyId;
	private long _userId;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _className;
	private long _classPK;

	public <T> T getColumnValue(String columnName) {
		Function<AnalyticsDeleteMessage, Object> function =
			_attributeGetterFunctions.get(columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((AnalyticsDeleteMessage)this);
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
			"analyticsDeleteMessageId", _analyticsDeleteMessageId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put("className", _className);
		_columnOriginalValues.put("classPK", _classPK);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("mvccVersion", 1L);

		columnBitmasks.put("analyticsDeleteMessageId", 2L);

		columnBitmasks.put("companyId", 4L);

		columnBitmasks.put("userId", 8L);

		columnBitmasks.put("createDate", 16L);

		columnBitmasks.put("modifiedDate", 32L);

		columnBitmasks.put("className", 64L);

		columnBitmasks.put("classPK", 128L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private AnalyticsDeleteMessage _escapedModel;

}