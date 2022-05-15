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

package com.liferay.object.service.impl;

import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.exception.DefaultObjectViewException;
import com.liferay.object.exception.ObjectViewColumnFieldNameException;
import com.liferay.object.exception.ObjectViewFilterColumnException;
import com.liferay.object.exception.ObjectViewSortColumnException;
import com.liferay.object.field.filter.parser.ObjectFieldFilterParser;
import com.liferay.object.field.filter.parser.ObjectFieldFilterParserServicesTracker;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectView;
import com.liferay.object.model.ObjectViewColumn;
import com.liferay.object.model.ObjectViewFilterColumn;
import com.liferay.object.model.ObjectViewSortColumn;
import com.liferay.object.service.base.ObjectViewLocalServiceBaseImpl;
import com.liferay.object.service.persistence.ObjectDefinitionPersistence;
import com.liferay.object.service.persistence.ObjectFieldPersistence;
import com.liferay.object.service.persistence.ObjectViewColumnPersistence;
import com.liferay.object.service.persistence.ObjectViewFilterColumnPersistence;
import com.liferay.object.service.persistence.ObjectViewSortColumnPersistence;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gabriel Albuquerque
 */
@Component(
	property = "model.class.name=com.liferay.object.model.ObjectView",
	service = AopService.class
)
public class ObjectViewLocalServiceImpl extends ObjectViewLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public ObjectView addObjectView(
			long userId, long objectDefinitionId, boolean defaultObjectView,
			Map<Locale, String> nameMap,
			List<ObjectViewColumn> objectViewColumns,
			List<ObjectViewFilterColumn> objectViewFilterColumns,
			List<ObjectViewSortColumn> objectViewSortColumns)
		throws PortalException {

		ObjectDefinition objectDefinition =
			_objectDefinitionPersistence.findByPrimaryKey(objectDefinitionId);

		if (defaultObjectView) {
			_validateDefaultObjectView(0, objectDefinitionId);
		}

		ObjectView objectView = objectViewPersistence.create(
			counterLocalService.increment());

		User user = _userLocalService.getUser(userId);

		objectView.setCompanyId(user.getCompanyId());
		objectView.setUserId(user.getUserId());
		objectView.setUserName(user.getFullName());

		objectView.setObjectDefinitionId(
			objectDefinition.getObjectDefinitionId());
		objectView.setDefaultObjectView(defaultObjectView);
		objectView.setNameMap(nameMap);

		objectView = objectViewPersistence.update(objectView);

		objectView.setObjectViewColumns(
			_addObjectViewColumns(
				user, objectView.getObjectViewId(), objectViewColumns));
		objectView.setObjectViewFilterColumns(
			_addObjectViewFilterColumns(
				user, objectView, objectViewFilterColumns));
		objectView.setObjectViewSortColumns(
			_addObjectViewSortColumns(
				user, objectView.getObjectViewId(), objectViewColumns,
				objectViewSortColumns));

		return objectView;
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	public ObjectView deleteObjectView(long objectViewId)
		throws PortalException {

		return deleteObjectView(
			objectViewPersistence.findByPrimaryKey(objectViewId));
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public ObjectView deleteObjectView(ObjectView objectView) {
		objectView = objectViewPersistence.remove(objectView);

		_objectViewColumnPersistence.removeByObjectViewId(
			objectView.getObjectViewId());

		_objectViewFilterColumnPersistence.removeByObjectViewId(
			objectView.getObjectViewId());

		_objectViewSortColumnPersistence.removeByObjectViewId(
			objectView.getObjectViewId());

		return objectView;
	}

	@Override
	public ObjectView fetchDefaultObjectView(long objectDefinitionId) {
		ObjectView objectView = objectViewPersistence.fetchByODI_DOV_First(
			objectDefinitionId, true, null);

		if (objectView != null) {
			objectView.setObjectViewColumns(
				_objectViewColumnPersistence.findByObjectViewId(
					objectView.getObjectViewId()));
			objectView.setObjectViewFilterColumns(
				_objectViewFilterColumnPersistence.findByObjectViewId(
					objectView.getObjectViewId()));
			objectView.setObjectViewSortColumns(
				_objectViewSortColumnPersistence.findByObjectViewId(
					objectView.getObjectViewId()));
		}

		return objectView;
	}

	@Override
	public ObjectView getObjectView(long objectViewId) throws PortalException {
		ObjectView objectView = objectViewPersistence.findByPrimaryKey(
			objectViewId);

		objectView.setObjectViewColumns(
			_objectViewColumnPersistence.findByObjectViewId(
				objectView.getObjectViewId()));
		objectView.setObjectViewFilterColumns(
			_objectViewFilterColumnPersistence.findByObjectViewId(
				objectView.getObjectViewId()));
		objectView.setObjectViewSortColumns(
			_objectViewSortColumnPersistence.findByObjectViewId(
				objectView.getObjectViewId()));

		return objectView;
	}

	@Override
	public List<ObjectView> getObjectViews(long objectDefinitionId) {
		List<ObjectView> objectViews =
			objectViewPersistence.findByObjectDefinitionId(objectDefinitionId);

		for (ObjectView objectView : objectViews) {
			objectView.setObjectViewColumns(
				_objectViewColumnPersistence.findByObjectViewId(
					objectView.getObjectViewId()));
			objectView.setObjectViewFilterColumns(
				_objectViewFilterColumnPersistence.findByObjectViewId(
					objectView.getObjectViewId()));
			objectView.setObjectViewSortColumns(
				_objectViewSortColumnPersistence.findByObjectViewId(
					objectView.getObjectViewId()));
		}

		return objectViews;
	}

	@Override
	public void unassociateObjectField(ObjectField objectField) {
		List<ObjectView> objectViews =
			objectViewPersistence.findByObjectDefinitionId(
				objectField.getObjectDefinitionId());

		for (ObjectView objectView : objectViews) {
			_objectViewColumnPersistence.removeByOVI_OFN(
				objectView.getObjectViewId(), objectField.getName());

			_objectViewFilterColumnPersistence.removeByOVI_OFN(
				objectView.getObjectViewId(), objectField.getName());

			_objectViewSortColumnPersistence.removeByOVI_OFN(
				objectView.getObjectViewId(), objectField.getName());
		}
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public ObjectView updateObjectView(
			long objectViewId, boolean defaultObjectView,
			Map<Locale, String> nameMap,
			List<ObjectViewColumn> objectViewColumns,
			List<ObjectViewFilterColumn> objectViewFilterColumns,
			List<ObjectViewSortColumn> objectViewSortColumns)
		throws PortalException {

		ObjectView objectView = objectViewPersistence.findByPrimaryKey(
			objectViewId);

		if (defaultObjectView) {
			_validateDefaultObjectView(
				objectViewId, objectView.getObjectDefinitionId());
		}

		_objectViewColumnPersistence.removeByObjectViewId(objectViewId);

		_objectViewFilterColumnPersistence.removeByObjectViewId(objectViewId);

		_objectViewSortColumnPersistence.removeByObjectViewId(objectViewId);

		objectView.setDefaultObjectView(defaultObjectView);
		objectView.setNameMap(nameMap);

		objectView = objectViewPersistence.update(objectView);

		User user = _userLocalService.getUser(objectView.getUserId());

		objectView.setObjectViewColumns(
			_addObjectViewColumns(user, objectViewId, objectViewColumns));
		objectView.setObjectViewFilterColumns(
			_addObjectViewFilterColumns(
				user, objectView, objectViewFilterColumns));
		objectView.setObjectViewSortColumns(
			_addObjectViewSortColumns(
				user, objectView.getObjectViewId(), objectViewColumns,
				objectViewSortColumns));

		return objectView;
	}

	private List<ObjectViewColumn> _addObjectViewColumns(
			User user, long objectViewId,
			List<ObjectViewColumn> objectViewColumns)
		throws PortalException {

		try {
			_validateObjectViewColumns(objectViewId, objectViewColumns);
		}
		catch (ObjectViewColumnFieldNameException
					objectViewColumnFieldNameException) {

			throw new ObjectViewColumnFieldNameException(
				objectViewColumnFieldNameException.getMessage());
		}

		return TransformUtil.transform(
			objectViewColumns,
			objectViewColumn -> {
				ObjectViewColumn newObjectViewColumn =
					_objectViewColumnPersistence.create(
						counterLocalService.increment());

				newObjectViewColumn.setCompanyId(user.getCompanyId());
				newObjectViewColumn.setUserId(user.getUserId());
				newObjectViewColumn.setUserName(user.getFullName());
				newObjectViewColumn.setObjectViewId(objectViewId);
				newObjectViewColumn.setLabelMap(objectViewColumn.getLabelMap());
				newObjectViewColumn.setObjectFieldName(
					objectViewColumn.getObjectFieldName());
				newObjectViewColumn.setPriority(objectViewColumn.getPriority());

				return _objectViewColumnPersistence.update(newObjectViewColumn);
			});
	}

	private List<ObjectViewFilterColumn> _addObjectViewFilterColumns(
			User user, ObjectView objectView,
			List<ObjectViewFilterColumn> objectViewFilterColumns)
		throws PortalException {

		_validateObjectViewFilterColumns(
			objectView.getObjectDefinitionId(), objectViewFilterColumns);

		return TransformUtil.transform(
			objectViewFilterColumns,
			objectViewFilterColumn -> {
				ObjectViewFilterColumn newObjectViewFilterColumn =
					_objectViewFilterColumnPersistence.create(
						counterLocalService.increment());

				newObjectViewFilterColumn.setCompanyId(user.getCompanyId());
				newObjectViewFilterColumn.setUserId(user.getUserId());
				newObjectViewFilterColumn.setUserName(user.getFullName());
				newObjectViewFilterColumn.setObjectViewId(
					objectView.getObjectViewId());
				newObjectViewFilterColumn.setFilterType(
					objectViewFilterColumn.getFilterType());
				newObjectViewFilterColumn.setJson(
					objectViewFilterColumn.getJson());
				newObjectViewFilterColumn.setObjectFieldName(
					objectViewFilterColumn.getObjectFieldName());

				return _objectViewFilterColumnPersistence.update(
					newObjectViewFilterColumn);
			});
	}

	private List<ObjectViewSortColumn> _addObjectViewSortColumns(
			User user, long objectViewId,
			List<ObjectViewColumn> objectViewColumns,
			List<ObjectViewSortColumn> objectViewSortColumns)
		throws PortalException {

		try {
			_validateObjectViewSortColumns(
				objectViewColumns, objectViewSortColumns);
		}
		catch (ObjectViewSortColumnException objectViewSortColumnException) {
			throw new ObjectViewSortColumnException(
				objectViewSortColumnException.getMessage());
		}

		return TransformUtil.transform(
			objectViewSortColumns,
			objectViewSortColumn -> {
				ObjectViewSortColumn newObjectViewSortColumn =
					_objectViewSortColumnPersistence.create(
						counterLocalService.increment());

				newObjectViewSortColumn.setCompanyId(user.getCompanyId());
				newObjectViewSortColumn.setUserId(user.getUserId());
				newObjectViewSortColumn.setUserName(user.getFullName());
				newObjectViewSortColumn.setObjectViewId(objectViewId);
				newObjectViewSortColumn.setObjectFieldName(
					objectViewSortColumn.getObjectFieldName());
				newObjectViewSortColumn.setPriority(
					objectViewSortColumn.getPriority());
				newObjectViewSortColumn.setSortOrder(
					objectViewSortColumn.getSortOrder());

				return _objectViewSortColumnPersistence.update(
					newObjectViewSortColumn);
			});
	}

	private void _validateDefaultObjectView(
			long objectViewId, long objectDefinitionId)
		throws PortalException {

		ObjectView objectView = objectViewPersistence.fetchByODI_DOV_First(
			objectDefinitionId, true, null);

		if ((objectView != null) &&
			(objectView.getObjectViewId() != objectViewId)) {

			throw new DefaultObjectViewException(
				"There can only be one default object view");
		}
	}

	private void _validateObjectViewColumns(
			long objectViewId, List<ObjectViewColumn> objectViewColumns)
		throws PortalException {

		ObjectView objectView = objectViewPersistence.findByPrimaryKey(
			objectViewId);

		List<ObjectField> objectFields =
			_objectFieldPersistence.findByObjectDefinitionId(
				objectView.getObjectDefinitionId());

		Set<String> objectFieldNames = new HashSet<>(_objectFieldNames);

		objectFields.forEach(
			objectField -> objectFieldNames.add(objectField.getName()));

		Set<String> objectViewColumnFieldNames = new LinkedHashSet<>();

		for (ObjectViewColumn objectViewColumn : objectViewColumns) {
			if (!objectFieldNames.contains(
					objectViewColumn.getObjectFieldName())) {

				throw new ObjectViewColumnFieldNameException(
					"There is no object field with the name: " +
						objectViewColumn.getObjectFieldName());
			}

			if (objectViewColumnFieldNames.contains(
					objectViewColumn.getObjectFieldName())) {

				throw new ObjectViewColumnFieldNameException(
					"There is already an object view column with the object " +
						"field name: " + objectViewColumn.getObjectFieldName());
			}

			objectViewColumnFieldNames.add(
				objectViewColumn.getObjectFieldName());
		}
	}

	private void _validateObjectViewFilterColumns(
			long objectDefinitionId,
			List<ObjectViewFilterColumn> objectViewFilterColumns)
		throws PortalException {

		for (ObjectViewFilterColumn objectViewFilterColumn :
				objectViewFilterColumns) {

			if (Validator.isNull(objectViewFilterColumn.getObjectFieldName())) {
				throw new ObjectViewFilterColumnException(
					"Object field name is null");
			}

			long listTypeDefinitionId = 0L;

			if (_objectFieldNames.contains(
					objectViewFilterColumn.getObjectFieldName())) {

				if (Objects.equals(
						objectViewFilterColumn.getObjectFieldName(),
						"creator") ||
					Objects.equals(
						objectViewFilterColumn.getObjectFieldName(), "id")) {

					throw new ObjectViewFilterColumnException(
						StringBundler.concat(
							"Object field name \"",
							objectViewFilterColumn.getObjectFieldName(),
							"\" is not filterable"));
				}
			}
			else {
				ObjectField objectField = _objectFieldPersistence.findByODI_N(
					objectDefinitionId,
					objectViewFilterColumn.getObjectFieldName());

				if (!Objects.equals(
						objectField.getBusinessType(),
						ObjectFieldConstants.BUSINESS_TYPE_PICKLIST)) {

					throw new ObjectViewFilterColumnException(
						StringBundler.concat(
							"Object field name \"",
							objectViewFilterColumn.getObjectFieldName(),
							"\" is not filterable"));
				}

				listTypeDefinitionId = objectField.getObjectDefinitionId();
			}

			if (Validator.isNull(objectViewFilterColumn.getFilterType()) &&
				Validator.isNull(objectViewFilterColumn.getJson())) {

				continue;
			}

			if ((Validator.isNull(objectViewFilterColumn.getFilterType()) &&
				 Validator.isNotNull(objectViewFilterColumn.getJson())) ||
				(Validator.isNotNull(objectViewFilterColumn.getFilterType()) &&
				 Validator.isNull(objectViewFilterColumn.getJson()))) {

				throw new ObjectViewFilterColumnException(
					StringBundler.concat(
						"Object field name \"",
						objectViewFilterColumn.getObjectFieldName(),
						"\" needs to have the filter type and JSON specified"));
			}

			ObjectFieldFilterParser objectFieldFilterParser =
				_objectFieldFilterParserServicesTracker.
					getObjectFieldFilterParser(
						objectViewFilterColumn.getFilterType());

			objectFieldFilterParser.validate(
				listTypeDefinitionId, objectViewFilterColumn);
		}
	}

	private void _validateObjectViewSortColumns(
			List<ObjectViewColumn> objectViewColumns,
			List<ObjectViewSortColumn> objectViewSortColumns)
		throws PortalException {

		Set<String> objectFieldNames = new LinkedHashSet<>();

		for (ObjectViewColumn objectViewColumn : objectViewColumns) {
			objectFieldNames.add(objectViewColumn.getObjectFieldName());
		}

		for (ObjectViewSortColumn objectViewSortColumn :
				objectViewSortColumns) {

			if (!objectFieldNames.contains(
					objectViewSortColumn.getObjectFieldName())) {

				throw new ObjectViewSortColumnException(
					"There is no object view column with the name: " +
						objectViewSortColumn.getObjectFieldName());
			}

			if (!(StringUtil.equals(
					objectViewSortColumn.getSortOrder(), "asc") ||
				  StringUtil.equals(
					  objectViewSortColumn.getSortOrder(), "desc"))) {

				throw new ObjectViewSortColumnException(
					"There is no sort order of type: " +
						objectViewSortColumn.getSortOrder());
			}
		}
	}

	@Reference
	private ObjectDefinitionPersistence _objectDefinitionPersistence;

	@Reference
	private ObjectFieldFilterParserServicesTracker
		_objectFieldFilterParserServicesTracker;

	private final Set<String> _objectFieldNames = Collections.unmodifiableSet(
		SetUtil.fromArray(
			"creator", "dateCreated", "dateModified", "id", "status"));

	@Reference
	private ObjectFieldPersistence _objectFieldPersistence;

	@Reference
	private ObjectViewColumnPersistence _objectViewColumnPersistence;

	@Reference
	private ObjectViewFilterColumnPersistence
		_objectViewFilterColumnPersistence;

	@Reference
	private ObjectViewSortColumnPersistence _objectViewSortColumnPersistence;

	@Reference
	private UserLocalService _userLocalService;

}