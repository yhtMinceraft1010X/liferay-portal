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
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.object.action.executor.ObjectActionExecutor;
import com.liferay.object.action.executor.ObjectActionExecutorRegistry;
import com.liferay.object.action.trigger.ObjectActionTrigger;
import com.liferay.object.action.trigger.ObjectActionTriggerRegistry;
import com.liferay.object.model.ObjectAction;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.web.internal.constants.ObjectWebKeys;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Marco Leo
 */
public class ObjectDefinitionsActionsDisplayContext
	extends BaseObjectDefinitionsDisplayContext {

	public ObjectDefinitionsActionsDisplayContext(
		HttpServletRequest httpServletRequest,
		ObjectActionExecutorRegistry objectActionExecutorRegistry,
		ObjectActionTriggerRegistry objectActionTriggerRegistry,
		ModelResourcePermission<ObjectDefinition>
			objectDefinitionModelResourcePermission,
		JSONFactory jsonFactory) {

		super(httpServletRequest, objectDefinitionModelResourcePermission);

		_objectActionExecutorRegistry = objectActionExecutorRegistry;
		_objectActionTriggerRegistry = objectActionTriggerRegistry;
		_jsonFactory = jsonFactory;
	}

	public List<FDSActionDropdownItem> getFDSActionDropdownItems()
		throws Exception {

		return Arrays.asList(
			new FDSActionDropdownItem(
				PortletURLBuilder.create(
					getPortletURL()
				).setMVCRenderCommandName(
					"/object_definitions/edit_object_action"
				).setParameter(
					"objectActionId", "{id}"
				).setWindowState(
					LiferayWindowState.POP_UP
				).buildString(),
				"view", "view",
				LanguageUtil.get(objectRequestHelper.getRequest(), "view"),
				"get", null, "sidePanel"),
			new FDSActionDropdownItem(
				"/o/object-admin/v1.0/object-actions/{id}", "trash", "delete",
				LanguageUtil.get(objectRequestHelper.getRequest(), "delete"),
				"delete", "delete", "async"));
	}

	public ObjectAction getObjectAction() {
		HttpServletRequest httpServletRequest =
			objectRequestHelper.getRequest();

		return (ObjectAction)httpServletRequest.getAttribute(
			ObjectWebKeys.OBJECT_ACTION);
	}

	public ObjectActionExecutor getObjectActionExecutor() {
		ObjectAction objectAction = getObjectAction();

		return _objectActionExecutorRegistry.getObjectActionExecutor(
			objectAction.getObjectActionExecutorKey());
	}

	public JSONArray getObjectActionExecutorsJSONArray() {
		JSONArray objectActionExecutorsJSONArray =
			_jsonFactory.createJSONArray();

		List<ObjectActionExecutor> objectActionExecutors =
			_objectActionExecutorRegistry.getObjectActionExecutors();

		for (ObjectActionExecutor objectActionExecutor :
				objectActionExecutors) {

			objectActionExecutorsJSONArray.put(
				JSONUtil.put(
					"description",
					LanguageUtil.get(
						objectRequestHelper.getLocale(),
						"object-action-executor-help[" +
							objectActionExecutor.getKey() + "]")
				).put(
					"label",
					LanguageUtil.get(
						objectRequestHelper.getLocale(),
						"object-action-executor[" +
							objectActionExecutor.getKey() + "]")
				).put(
					"value", objectActionExecutor.getKey()
				));
		}

		return objectActionExecutorsJSONArray;
	}

	public JSONObject getObjectActionJSONObject(ObjectAction objectAction) {
		return JSONUtil.put(
			"active", objectAction.isActive()
		).put(
			"description", objectAction.getDescription()
		).put(
			"id", objectAction.getObjectActionId()
		).put(
			"name", objectAction.getName()
		).put(
			"objectActionExecutorKey", objectAction.getObjectActionExecutorKey()
		).put(
			"objectActionTriggerKey", objectAction.getObjectActionTriggerKey()
		).put(
			"parameters", objectAction.getParametersUnicodeProperties()
		);
	}

	public JSONArray getObjectActionTriggersJSONArray() {
		JSONArray objectActionTriggersJSONArray =
			_jsonFactory.createJSONArray();

		ObjectDefinition objectDefinition = getObjectDefinition();

		List<ObjectActionTrigger> objectActionTriggers =
			_objectActionTriggerRegistry.getObjectActionTriggers(
				objectDefinition.getClassName());

		objectActionTriggers.forEach(
			objectActionTrigger -> objectActionTriggersJSONArray.put(
				JSONUtil.put(
					"description",
					LanguageUtil.get(
						objectRequestHelper.getLocale(),
						"object-action-trigger-help[" +
							objectActionTrigger.getKey() + "]")
				).put(
					"label",
					LanguageUtil.get(
						objectRequestHelper.getLocale(),
						"object-action-trigger[" +
							objectActionTrigger.getKey() + "]")
				).put(
					"value", objectActionTrigger.getKey()
				)));

		return objectActionTriggersJSONArray;
	}

	public ObjectDefinition getObjectDefinition() {
		HttpServletRequest httpServletRequest =
			objectRequestHelper.getRequest();

		return (ObjectDefinition)httpServletRequest.getAttribute(
			ObjectWebKeys.OBJECT_DEFINITION);
	}

	@Override
	protected String getAPIURI() {
		return "/object-actions";
	}

	@Override
	protected UnsafeConsumer<DropdownItem, Exception>
		getCreationMenuDropdownItemUnsafeConsumer() {

		return dropdownItem -> {
			dropdownItem.setHref(
				PortletURLBuilder.create(
					getPortletURL()
				).setMVCRenderCommandName(
					"/object_definitions/add_object_action"
				).setWindowState(
					LiferayWindowState.POP_UP
				).buildString());
			dropdownItem.setLabel(
				LanguageUtil.get(
					objectRequestHelper.getRequest(), "add-object-action"));
			dropdownItem.setTarget("sidePanel");
		};
	}

	private final JSONFactory _jsonFactory;
	private final ObjectActionExecutorRegistry _objectActionExecutorRegistry;
	private final ObjectActionTriggerRegistry _objectActionTriggerRegistry;

}