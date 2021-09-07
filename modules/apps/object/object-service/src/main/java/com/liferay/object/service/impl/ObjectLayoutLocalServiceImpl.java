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

import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectLayout;
import com.liferay.object.service.base.ObjectLayoutLocalServiceBaseImpl;
import com.liferay.object.service.persistence.ObjectDefinitionPersistence;
import com.liferay.object.service.persistence.ObjectLayoutTabPersistence;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.liferay.object.model.ObjectLayout",
	service = AopService.class
)
public class ObjectLayoutLocalServiceImpl
	extends ObjectLayoutLocalServiceBaseImpl {

	public ObjectLayout addObjectLayout(
			long userId, long objectDefinitionId, boolean defaultObjectLayout,
			Map<Locale, String> nameMap, JSONArray objectLayoutTabsJSONArray)
		throws PortalException {

		ObjectDefinition objectDefinition =
			_objectDefinitionPersistence.findByPrimaryKey(objectDefinitionId);

		ObjectLayout objectLayout = objectLayoutPersistence.create(
			counterLocalService.increment());

		User user = _userLocalService.getUser(userId);

		objectLayout.setCompanyId(user.getCompanyId());
		objectLayout.setUserId(user.getUserId());
		objectLayout.setUserName(user.getFullName());

		objectLayout.setObjectDefinitionId(
			objectDefinition.getObjectDefinitionId());
		objectLayout.setDefaultObjectLayout(defaultObjectLayout);
		objectLayout.setNameMap(nameMap);

		objectLayout = objectLayoutPersistence.update(objectLayout);

		_addObjectLayoutTabs(objectLayoutTabsJSONArray);

		return objectLayout;
	}

	private void _addObjectLayoutBox(JSONObject jsonObject) {
		_addObjectLayoutRows(jsonObject.getJSONArray("objectLayoutRow"));
	}

	private void _addObjectLayoutColumn(JSONObject jsonObject) {
	}

	private void _addObjectLayoutColumns(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			_addObjectLayoutColumn(jsonArray.getJSONObject(i));
		}
	}

	private void _addObjectLayoutBoxes(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			_addObjectLayoutBox(jsonArray.getJSONObject(i));
		}
	}

	private void _addObjectLayoutRow(JSONObject jsonObject) {
		_addObjectLayoutColumns(
			jsonObject.getJSONArray("objectLayoutColumn"));
	}

	private void _addObjectLayoutRows(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			_addObjectLayoutRow(jsonArray.getJSONObject(i));
		}
	}

	private void _addObjectLayoutTab(JSONObject jsonObject) {
		_addObjectLayoutBoxes(jsonObject.getJSONArray("objectLayoutBox"));
	}

	private void _addObjectLayoutTabs(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			_addObjectLayoutTab(jsonArray.getJSONObject(i));
		}
	}

	@Reference
	private ObjectDefinitionPersistence _objectDefinitionPersistence;

	@Reference
	private ObjectLayoutTabPersistence _objectLayoutTabPersistence;

	@Reference
	private UserLocalService _userLocalService;

}