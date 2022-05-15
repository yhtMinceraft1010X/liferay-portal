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

package com.liferay.analytics.dxp.entity.rest.internal.dto.v1_0.converter;

import com.liferay.analytics.dxp.entity.rest.dto.v1_0.DXPEntity;
import com.liferay.analytics.dxp.entity.rest.dto.v1_0.ExpandoField;
import com.liferay.analytics.dxp.entity.rest.dto.v1_0.Field;
import com.liferay.analytics.dxp.entity.rest.dto.v1_0.converter.DXPEntityDTOConverter;
import com.liferay.analytics.settings.configuration.AnalyticsConfiguration;
import com.liferay.analytics.settings.configuration.AnalyticsConfigurationTracker;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoTableConstants;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rachael Koestartyo
 */
@Component(
	property = "dto.class.name=com.liferay.analytics.dxp.entity.rest.dto.v1_0.DXPEntity",
	service = {DTOConverter.class, DXPEntityDTOConverter.class}
)
public class DXPEntityDTOConverterImpl implements DXPEntityDTOConverter {

	@Override
	public String getContentType() {
		return DXPEntity.class.getSimpleName();
	}

	@Override
	public DXPEntity toDTO(
			DTOConverterContext dtoConverterContext, BaseModel<?> baseModel)
		throws Exception {

		Map<String, Object> modelAttributes = baseModel.getModelAttributes();

		return _toDXPEntity(
			_getExpandoFields(baseModel), _getFields(baseModel),
			String.valueOf(baseModel.getPrimaryKeyObj()),
			(Date)modelAttributes.get("modifiedDate"),
			baseModel.getModelClassName());
	}

	private void _addFieldAttributes(
		BaseModel<?> baseModel, List<Field> fields,
		List<String> includeAttributeNames) {

		Map<String, Object> modelAttributes = baseModel.getModelAttributes();

		for (Map.Entry<String, Object> entry : modelAttributes.entrySet()) {
			if (!ListUtil.isEmpty(includeAttributeNames) &&
				!includeAttributeNames.contains(entry.getKey())) {

				continue;
			}

			Field field = new Field() {
				{
					name = entry.getKey();
					value = entry.getValue();
				}
			};

			fields.add(field);
		}
	}

	private Map<String, Serializable> _getAttributes(
		ExpandoBridge expandoBridge, List<String> includeAttributeNames) {

		Map<String, Serializable> newAttributes = new HashMap<>();

		Map<String, Serializable> attributes = expandoBridge.getAttributes(
			false);

		for (Map.Entry<String, Serializable> entry : attributes.entrySet()) {
			if (!ListUtil.isEmpty(includeAttributeNames) &&
				!includeAttributeNames.contains(entry.getKey())) {

				continue;
			}

			String dataType = ExpandoColumnConstants.getDataType(
				expandoBridge.getAttributeType(entry.getKey()));

			if (Validator.isBlank(dataType)) {
				dataType = ExpandoColumnConstants.DATA_TYPE_TEXT;
			}

			newAttributes.put(
				entry.getKey() + "-" + dataType, entry.getValue());
		}

		return newAttributes;
	}

	private Field[] _getExpandoColumnFields(
		String className, String dataType, ExpandoColumn expandoColumn) {

		List<Field> fields = new ArrayList<Field>() {
			{
				add(
					new Field() {
						{
							name = "className";
							value = className;
						}
					});
				add(
					new Field() {
						{
							name = "columnId";
							value = expandoColumn.getColumnId();
						}
					});
				add(
					new Field() {
						{
							name = "dataType";
							value = dataType;
						}
					});
				add(
					new Field() {
						{
							name = "modifiedDate";
							value = expandoColumn.getModifiedDate();
						}
					});
				add(
					new Field() {
						{
							name = "name";
							value = expandoColumn.getName() + "-" + dataType;
						}
					});
			}
		};

		return fields.toArray(new Field[0]);
	}

	private ExpandoField[] _getExpandoFields(BaseModel<?> baseModel) {
		if (!StringUtil.equals(
				baseModel.getModelClassName(), Organization.class.getName()) &&
			!StringUtil.equals(
				baseModel.getModelClassName(), User.class.getName())) {

			return new ExpandoField[0];
		}

		List<ExpandoField> expandoFields = new ArrayList<>();

		List<String> includeAttributeNames = new ArrayList<>();

		ShardedModel shardedModel = (ShardedModel)baseModel;

		if (StringUtil.equals(
				baseModel.getModelClassName(), User.class.getName())) {

			AnalyticsConfiguration analyticsConfiguration =
				_analyticsConfigurationTracker.getAnalyticsConfiguration(
					shardedModel.getCompanyId());

			includeAttributeNames = ListUtil.fromArray(
				analyticsConfiguration.syncedUserFieldNames());
		}

		Map<String, Serializable> attributes = _getAttributes(
			baseModel.getExpandoBridge(), includeAttributeNames);

		for (Map.Entry<String, Serializable> entry : attributes.entrySet()) {
			String key = entry.getKey();

			ExpandoColumn expandoColumn =
				_expandoColumnLocalService.getDefaultTableColumn(
					shardedModel.getCompanyId(), baseModel.getModelClassName(),
					key.substring(0, key.indexOf("-")));

			if (expandoColumn == null) {
				continue;
			}

			ExpandoField expandoField = new ExpandoField() {
				{
					columnId = expandoColumn.getColumnId();
					name = key;
					value = String.valueOf(entry.getValue());
				}
			};

			expandoFields.add(expandoField);
		}

		return expandoFields.toArray(new ExpandoField[0]);
	}

	private Field[] _getFields(BaseModel<?> baseModel) throws Exception {
		if (StringUtil.equals(
				baseModel.getModelClassName(), ExpandoColumn.class.getName())) {

			ExpandoColumn expandoColumn = (ExpandoColumn)baseModel;

			String className = User.class.getName();

			if (_isCustomField(
					Organization.class.getName(), expandoColumn.getTableId())) {

				className = Organization.class.getName();
			}

			String dataType = ExpandoColumnConstants.getDataType(
				expandoColumn.getType());

			if (Validator.isBlank(dataType)) {
				dataType = ExpandoColumnConstants.DATA_TYPE_TEXT;
			}

			return _getExpandoColumnFields(className, dataType, expandoColumn);
		}

		List<Field> fields = new ArrayList<>();

		List<String> includeAttributeNames = new ArrayList<>();

		if (StringUtil.equals(
				baseModel.getModelClassName(), User.class.getName())) {

			User user = (User)baseModel;

			AnalyticsConfiguration analyticsConfiguration =
				_analyticsConfigurationTracker.getAnalyticsConfiguration(
					user.getCompanyId());

			_addFieldAttributes(
				user.getContact(), fields,
				ListUtil.fromArray(
					analyticsConfiguration.syncedContactFieldNames()));

			includeAttributeNames = ListUtil.fromArray(
				analyticsConfiguration.syncedUserFieldNames());
		}

		_addFieldAttributes(baseModel, fields, includeAttributeNames);

		if (StringUtil.equals(
				baseModel.getModelClassName(), Organization.class.getName())) {

			Field field = new Field();

			field.setName("parentOrganizationName");

			Organization organization = (Organization)baseModel;

			field.setValue(organization.getParentOrganizationName());

			fields.add(field);
		}

		return fields.toArray(new Field[0]);
	}

	private boolean _isCustomField(String className, long tableId) {
		long classNameId = _classNameLocalService.getClassNameId(className);

		try {
			ExpandoTable expandoTable = _expandoTableLocalService.getTable(
				tableId);

			if (Objects.equals(
					ExpandoTableConstants.DEFAULT_TABLE_NAME,
					expandoTable.getName()) &&
				(expandoTable.getClassNameId() == classNameId)) {

				return true;
			}
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to get expando table " + tableId, exception);
			}
		}

		return false;
	}

	private DXPEntity _toDXPEntity(
		ExpandoField[] expandoFields, Field[] fields, String id,
		Date modifiedDate, String type) {

		DXPEntity dxpEntity = new DXPEntity();

		if (ArrayUtil.isNotEmpty(expandoFields)) {
			dxpEntity.setExpandoFields(expandoFields);
		}

		if (ArrayUtil.isNotEmpty(fields)) {
			dxpEntity.setFields(fields);
		}

		dxpEntity.setId(id);
		dxpEntity.setModifiedDate(_dateFormat.format(modifiedDate));
		dxpEntity.setType(type);

		return dxpEntity;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DXPEntityDTOConverterImpl.class);

	private static final DateFormat _dateFormat = new SimpleDateFormat(
		"yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	@Reference
	private AnalyticsConfigurationTracker _analyticsConfigurationTracker;

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private ExpandoColumnLocalService _expandoColumnLocalService;

	@Reference
	private ExpandoTableLocalService _expandoTableLocalService;

	@Reference
	private UserLocalService _userLocalService;

}