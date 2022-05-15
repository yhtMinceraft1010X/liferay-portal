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

package com.liferay.segments.internal.model.listener;

import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoTableConstants;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.odata.entity.BooleanEntityField;
import com.liferay.portal.odata.entity.DateTimeEntityField;
import com.liferay.portal.odata.entity.DoubleEntityField;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.entity.IntegerEntityField;
import com.liferay.portal.odata.entity.StringEntityField;
import com.liferay.portal.odata.normalizer.Normalizer;
import com.liferay.portal.search.expando.ExpandoBridgeIndexer;
import com.liferay.segments.internal.odata.entity.OrganizationEntityModel;
import com.liferay.segments.service.SegmentsEntryLocalService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author David Arques
 */
@Component(immediate = true, service = ModelListener.class)
public class OrganizationExpandoColumnModelListener
	extends BaseModelListener<ExpandoColumn> {

	@Override
	public void onAfterCreate(ExpandoColumn expandoColumn)
		throws ModelListenerException {

		try {
			if (!_isOrganizationCustomField(expandoColumn)) {
				return;
			}

			Optional<EntityField> organizationEntityFieldOptional =
				_getOrganizationEntityFieldOptional(expandoColumn);

			organizationEntityFieldOptional.ifPresent(
				entityField -> {
					_organizationEntityFields.put(
						expandoColumn.getColumnId(), entityField);

					_serviceRegistration = _updateRegistry(
						_bundleContext, _serviceRegistration,
						_organizationEntityFields);
				});
		}
		catch (PortalException portalException) {
			throw new ModelListenerException(portalException);
		}
	}

	@Override
	public void onAfterRemove(ExpandoColumn expandoColumn)
		throws ModelListenerException {

		if (expandoColumn == null) {
			return;
		}

		if (_organizationEntityFields.containsKey(
				expandoColumn.getColumnId())) {

			_organizationEntityFields.remove(expandoColumn.getColumnId());

			_serviceRegistration = _updateRegistry(
				_bundleContext, _serviceRegistration,
				_organizationEntityFields);
		}
	}

	@Override
	public void onAfterUpdate(
			ExpandoColumn originalExpandoColumn, ExpandoColumn expandoColumn)
		throws ModelListenerException {

		if (expandoColumn == null) {
			return;
		}

		_organizationEntityFields.remove(expandoColumn.getColumnId());

		onAfterCreate(expandoColumn);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		try {
			_bundleContext = bundleContext;

			_organizationEntityFields = _getOrganizationEntityFields();

			_serviceRegistration = _register(
				_bundleContext, _organizationEntityFields);
		}
		catch (PortalException portalException) {
			_log.error(portalException);
		}
	}

	@Deactivate
	protected void deactivate() {
		_unregister(_serviceRegistration);
	}

	private String _encodeName(ExpandoColumn expandoColumn) {
		return StringBundler.concat(
			StringPool.UNDERLINE, expandoColumn.getColumnId(),
			StringPool.UNDERLINE,
			Normalizer.normalizeIdentifier(expandoColumn.getName()));
	}

	private Optional<EntityField> _getOrganizationEntityFieldOptional(
		ExpandoColumn expandoColumn) {

		UnicodeProperties unicodeProperties =
			expandoColumn.getTypeSettingsProperties();

		int indexType = GetterUtil.getInteger(
			unicodeProperties.get(ExpandoColumnConstants.INDEX_TYPE));

		if (indexType == ExpandoColumnConstants.INDEX_TYPE_NONE) {
			return Optional.empty();
		}

		String encodedName = _encodeName(expandoColumn);

		String encodedIndexedFieldName = _expandoBridgeIndexer.encodeFieldName(
			expandoColumn);

		EntityField entityField = null;

		if (expandoColumn.getType() == ExpandoColumnConstants.BOOLEAN) {
			entityField = new BooleanEntityField(
				encodedName, locale -> encodedIndexedFieldName);
		}
		else if (expandoColumn.getType() == ExpandoColumnConstants.DATE) {
			entityField = new DateTimeEntityField(
				encodedName,
				locale -> Field.getSortableFieldName(encodedIndexedFieldName),
				locale -> encodedIndexedFieldName);
		}
		else if ((expandoColumn.getType() == ExpandoColumnConstants.DOUBLE) ||
				 (expandoColumn.getType() ==
					 ExpandoColumnConstants.DOUBLE_ARRAY) ||
				 (expandoColumn.getType() == ExpandoColumnConstants.FLOAT) ||
				 (expandoColumn.getType() ==
					 ExpandoColumnConstants.FLOAT_ARRAY)) {

			entityField = new DoubleEntityField(
				encodedName, locale -> encodedIndexedFieldName);
		}
		else if ((expandoColumn.getType() == ExpandoColumnConstants.INTEGER) ||
				 (expandoColumn.getType() ==
					 ExpandoColumnConstants.INTEGER_ARRAY) ||
				 (expandoColumn.getType() == ExpandoColumnConstants.LONG) ||
				 (expandoColumn.getType() ==
					 ExpandoColumnConstants.LONG_ARRAY) ||
				 (expandoColumn.getType() == ExpandoColumnConstants.SHORT) ||
				 (expandoColumn.getType() ==
					 ExpandoColumnConstants.SHORT_ARRAY)) {

			entityField = new IntegerEntityField(
				encodedName, locale -> encodedIndexedFieldName);
		}
		else if (expandoColumn.getType() ==
					ExpandoColumnConstants.STRING_LOCALIZED) {

			entityField = new StringEntityField(
				encodedName,
				locale -> Field.getLocalizedName(
					locale, encodedIndexedFieldName));
		}
		else {
			entityField = new StringEntityField(
				encodedName, locale -> encodedIndexedFieldName);
		}

		return Optional.of(entityField);
	}

	private Map<Long, EntityField> _getOrganizationEntityFields()
		throws PortalException {

		Map<Long, EntityField> organizationEntityFieldsMap = new HashMap<>();

		ActionableDynamicQuery columnActionableDynamicQuery =
			_expandoColumnLocalService.getActionableDynamicQuery();

		columnActionableDynamicQuery.setAddCriteriaMethod(
			dynamicQuery -> {
				Property tableProperty = PropertyFactoryUtil.forName("tableId");

				long organizationClassNameId =
					_classNameLocalService.getClassNameId(
						Organization.class.getName());

				dynamicQuery.add(
					tableProperty.in(
						_getTableDynamicQuery(
							organizationClassNameId,
							ExpandoTableConstants.DEFAULT_TABLE_NAME)));
			});
		columnActionableDynamicQuery.setPerformActionMethod(
			(ActionableDynamicQuery.PerformActionMethod<ExpandoColumn>)
				expandoColumn -> {
					Optional<EntityField> organizationEntityFieldOptional =
						_getOrganizationEntityFieldOptional(expandoColumn);

					organizationEntityFieldOptional.ifPresent(
						entityField -> organizationEntityFieldsMap.put(
							expandoColumn.getColumnId(), entityField));
				});

		columnActionableDynamicQuery.performActions();

		return organizationEntityFieldsMap;
	}

	private DynamicQuery _getTableDynamicQuery(long classNameId, String name) {
		DynamicQuery dynamicQuery = _expandoTableLocalService.dynamicQuery();

		Property classNameIdProperty = PropertyFactoryUtil.forName(
			"classNameId");

		dynamicQuery.add(classNameIdProperty.eq(classNameId));

		Property nameProperty = PropertyFactoryUtil.forName("name");

		dynamicQuery.add(nameProperty.eq(name));

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("tableId"));

		return dynamicQuery;
	}

	private boolean _isOrganizationCustomField(ExpandoColumn expandoColumn)
		throws PortalException {

		long organizationClassNameId = _classNameLocalService.getClassNameId(
			Organization.class.getName());

		ExpandoTable expandoTable = _expandoTableLocalService.getTable(
			expandoColumn.getTableId());

		if ((expandoTable.getClassNameId() != organizationClassNameId) ||
			!ExpandoTableConstants.DEFAULT_TABLE_NAME.equals(
				expandoTable.getName())) {

			return false;
		}

		return true;
	}

	private ServiceRegistration<EntityModel> _register(
		BundleContext bundleContext,
		Map<Long, EntityField> organizationEntityFieldsMap) {

		return bundleContext.registerService(
			EntityModel.class,
			new OrganizationEntityModel(
				new ArrayList<>(organizationEntityFieldsMap.values())),
			HashMapDictionaryBuilder.<String, Object>put(
				"entity.model.name", OrganizationEntityModel.NAME
			).build());
	}

	private void _unregister(
		ServiceRegistration<EntityModel> serviceRegistration) {

		serviceRegistration.unregister();
	}

	private ServiceRegistration<EntityModel> _updateRegistry(
		BundleContext bundleContext,
		ServiceRegistration<EntityModel> serviceRegistration,
		Map<Long, EntityField> entityFieldsMap) {

		_unregister(serviceRegistration);

		return _register(bundleContext, entityFieldsMap);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OrganizationExpandoColumnModelListener.class);

	private BundleContext _bundleContext;

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private ExpandoBridgeIndexer _expandoBridgeIndexer;

	@Reference
	private ExpandoColumnLocalService _expandoColumnLocalService;

	@Reference
	private ExpandoTableLocalService _expandoTableLocalService;

	private Map<Long, EntityField> _organizationEntityFields = new HashMap<>();

	@Reference
	private SegmentsEntryLocalService _segmentsEntryLocalService;

	private ServiceRegistration<EntityModel> _serviceRegistration;

}