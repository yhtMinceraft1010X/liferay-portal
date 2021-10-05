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

package com.liferay.object.system.model.listener;

import com.liferay.object.action.engine.ObjectActionEngine;
import com.liferay.object.constants.ObjectActionTriggerConstants;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.io.Serializable;

import java.util.Map;
import java.util.function.Function;

/**
 * @author Brian Wing Shun Chan
 */
public class SystemObjectDefinitionMetadataModelListener
	extends BaseModelListener {

	public SystemObjectDefinitionMetadataModelListener(
		Class<?> modelClass, JSONFactory jsonFactory,
		ObjectActionEngine objectActionEngine) {

		_modelClass = modelClass;
		_jsonFactory = jsonFactory;
		_objectActionEngine = objectActionEngine;
	}

	@Override
	public Class<?> getModelClass() {
		return _modelClass;
	}

	@Override
	public void onAfterCreate(Object model) throws ModelListenerException {
		_executeObjectActions(
			ObjectActionTriggerConstants.KEY_ON_AFTER_CREATE, null, model);
	}

	@Override
	public void onAfterRemove(Object model) throws ModelListenerException {
		_executeObjectActions(
			ObjectActionTriggerConstants.KEY_ON_AFTER_REMOVE, null, model);
	}

	@Override
	public void onAfterUpdate(Object originalModel, Object model)
		throws ModelListenerException {

		_executeObjectActions(
			ObjectActionTriggerConstants.KEY_ON_AFTER_UPDATE, originalModel,
			model);
	}

	private void _executeObjectActions(
			String objectActionTriggerKey, Object originalModel, Object model)
		throws ModelListenerException {

		try {
			BaseModel<?> baseModel = (BaseModel<?>)model;

			Map<String, Function<Object, Object>> getterFunctions =
				(Map<String, Function<Object, Object>>)
					(Map<String, ?>)baseModel.getAttributeGetterFunctions();

			Function<Object, Object> function = getterFunctions.get(
				"companyId");

			if (function == null) {
				return;
			}

			long companyId = (Long)function.apply(model);

			long userId = PrincipalThreadLocal.getUserId();

			if (userId == 0) {
				function = getterFunctions.get("userId");

				if (function == null) {
					return;
				}

				userId = (Long)function.apply(model);
			}

			_objectActionEngine.executeObjectActions(
				companyId, userId, _modelClass.getName(),
				objectActionTriggerKey,
				HashMapBuilder.<String, Serializable>put(
					"payload",
					_getPayload(objectActionTriggerKey, originalModel, model)
				).build());
		}
		catch (PortalException portalException) {
			throw new ModelListenerException(portalException);
		}
	}

	private Serializable _getPayload(
			String objectActionTriggerKey, Object originalModel, Object model)
		throws JSONException {

		JSONObject payloadJSONObject = JSONUtil.put(
			"objectActionTriggerKey", objectActionTriggerKey);

		JSONObject modelJSONObject = _jsonFactory.createJSONObject(
			model.toString());

		payloadJSONObject.put(
			"model" + _modelClass.getSimpleName(), modelJSONObject);

		if (originalModel != null) {
			JSONObject originalModelJSONObject = _jsonFactory.createJSONObject(
				originalModel.toString());

			payloadJSONObject.put(
				"original" + _modelClass.getSimpleName(),
				originalModelJSONObject);
		}

		return payloadJSONObject.toString();
	}

	private final JSONFactory _jsonFactory;
	private final Class<?> _modelClass;
	private final ObjectActionEngine _objectActionEngine;

}