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

package com.liferay.object.web.internal.object.definitions.display.context;

import com.liferay.frontend.data.set.model.FDSActionDropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.field.business.type.ObjectFieldBusinessType;
import com.liferay.object.field.business.type.ObjectFieldBusinessTypeServicesTracker;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectFieldSetting;
import com.liferay.object.web.internal.util.ObjectFieldBusinessTypeUtil;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeFormatter;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Marco Leo
 * @author Gabriel Albuquerque
 */
public class ObjectDefinitionsFieldsDisplayContext
	extends BaseObjectDefinitionsDisplayContext {

	public ObjectDefinitionsFieldsDisplayContext(
		HttpServletRequest httpServletRequest,
		ModelResourcePermission<ObjectDefinition>
			objectDefinitionModelResourcePermission,
		ObjectFieldBusinessTypeServicesTracker
			objectFieldBusinessTypeServicesTracker) {

		super(httpServletRequest, objectDefinitionModelResourcePermission);

		_objectFieldBusinessTypeServicesTracker =
			objectFieldBusinessTypeServicesTracker;
	}

	public CreationMenu getCreationMenu(ObjectDefinition objectDefinition)
		throws PortalException {

		CreationMenu creationMenu = new CreationMenu();

		if (objectDefinition.isSystem() ||
			!hasUpdateObjectDefinitionPermission()) {

			return creationMenu;
		}

		creationMenu.addDropdownItem(
			dropdownItem -> {
				dropdownItem.setHref("addObjectField");
				dropdownItem.setLabel(
					LanguageUtil.get(
						objectRequestHelper.getRequest(), "add-object-field"));
				dropdownItem.setTarget("event");
			});

		return creationMenu;
	}

	public List<FDSActionDropdownItem> getFDSActionDropdownItems()
		throws Exception {

		return Arrays.asList(
			new FDSActionDropdownItem(
				PortletURLBuilder.create(
					getPortletURL()
				).setMVCRenderCommandName(
					"/object_definitions/edit_object_field"
				).setParameter(
					"objectFieldId", "{id}"
				).setWindowState(
					LiferayWindowState.POP_UP
				).buildString(),
				"view", "view",
				LanguageUtil.get(objectRequestHelper.getRequest(), "view"),
				"get", null, "sidePanel"),
			new FDSActionDropdownItem(
				"/o/object-admin/v1.0/object-fields/{id}", "trash", "delete",
				LanguageUtil.get(objectRequestHelper.getRequest(), "delete"),
				"delete", "delete", "async"));
	}

	public String[] getForbiddenLastCharacters() {
		List<String> forbiddenLastCharacters = new ArrayList<>();

		for (String forbiddenLastCharacter :
				PropsValues.DL_CHAR_LAST_BLACKLIST) {

			if (forbiddenLastCharacter.startsWith(
					UnicodeFormatter.UNICODE_PREFIX)) {

				forbiddenLastCharacter = UnicodeFormatter.parseString(
					forbiddenLastCharacter);
			}

			forbiddenLastCharacters.add(forbiddenLastCharacter);
		}

		return forbiddenLastCharacters.toArray(new String[0]);
	}

	public List<Map<String, String>> getObjectFieldBusinessTypeMaps(
		boolean includeRelationshipObjectFieldBusinessType, Locale locale) {

		List<ObjectFieldBusinessType> objectFieldBusinessTypes =
			_objectFieldBusinessTypeServicesTracker.
				getObjectFieldBusinessTypes();

		Stream<ObjectFieldBusinessType> stream =
			objectFieldBusinessTypes.stream();

		return ObjectFieldBusinessTypeUtil.getObjectFieldBusinessTypeMaps(
			locale,
			stream.filter(
				objectFieldBusinessType ->
					objectFieldBusinessType.isVisible() &&
					(!StringUtil.equals(
						objectFieldBusinessType.getName(),
						ObjectFieldConstants.BUSINESS_TYPE_RELATIONSHIP) ||
					 includeRelationshipObjectFieldBusinessType)
			).collect(
				Collectors.toList()
			));
	}

	public JSONObject getObjectFieldJSONObject(ObjectField objectField) {
		return JSONUtil.put(

			// TODO Return null instead of ""

			"indexedLanguageId", objectField.getIndexedLanguageId()
		).put(
			"businessType", objectField.getBusinessType()
		).put(
			"DBType", objectField.getDBType()
		).put(
			"id", Long.valueOf(objectField.getObjectFieldId())
		).put(
			"indexed", objectField.isIndexed()
		).put(
			"indexedAsKeyword", objectField.isIndexedAsKeyword()
		).put(
			"label", objectField.getLabelMap()
		).put(
			"listTypeDefinitionId",
			Long.valueOf(objectField.getListTypeDefinitionId())
		).put(
			"name", objectField.getName()
		).put(
			"objectFieldSettings", _getObjectFieldSettingsJSONArray(objectField)
		).put(
			"relationshipType", objectField.getRelationshipType()
		).put(
			"required", objectField.isRequired()
		);
	}

	@Override
	protected String getAPIURI() {
		return "/object-fields";
	}

	private JSONArray _getObjectFieldSettingsJSONArray(
		ObjectField objectField) {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		ListUtil.isNotEmptyForEach(
			objectField.getObjectFieldSettings(),
			objectFieldSetting -> _putObjectFieldSettingJSONObject(
				objectField.getBusinessType(), jsonArray, objectFieldSetting));

		return jsonArray;
	}

	private Object _getObjectFieldSettingValue(
		String businessType, ObjectFieldSetting objectFieldSetting) {

		if (Objects.equals(
				ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT, businessType)) {

			if (Objects.equals(
					objectFieldSetting.getName(), "maximumFileSize")) {

				return GetterUtil.getInteger(objectFieldSetting.getValue());
			}
			else if (Objects.equals(
						objectFieldSetting.getName(),
						"showFilesInDocumentsAndMedia")) {

				return GetterUtil.getBoolean(objectFieldSetting.getValue());
			}
		}
		else if (Objects.equals(
					ObjectFieldConstants.BUSINESS_TYPE_LONG_TEXT,
					businessType) ||
				 Objects.equals(
					 ObjectFieldConstants.BUSINESS_TYPE_TEXT, businessType)) {

			if (Objects.equals(objectFieldSetting.getName(), "maxLength")) {
				return GetterUtil.getInteger(objectFieldSetting.getValue());
			}
			else if (Objects.equals(
						objectFieldSetting.getName(), "showCounter")) {

				return GetterUtil.getBoolean(objectFieldSetting.getValue());
			}
		}

		return objectFieldSetting.getValue();
	}

	private void _putObjectFieldSettingJSONObject(
		String businessType, JSONArray jsonArray,
		ObjectFieldSetting objectFieldSetting) {

		jsonArray.put(
			JSONUtil.put(
				"name", objectFieldSetting.getName()
			).put(
				"value",
				_getObjectFieldSettingValue(businessType, objectFieldSetting)
			));
	}

	private final ObjectFieldBusinessTypeServicesTracker
		_objectFieldBusinessTypeServicesTracker;

}