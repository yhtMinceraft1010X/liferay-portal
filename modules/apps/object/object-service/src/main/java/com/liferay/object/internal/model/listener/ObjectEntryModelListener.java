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

package com.liferay.object.internal.model.listener;

import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(immediate = true, service = ModelListener.class)
public class ObjectEntryModelListener extends BaseModelListener<ObjectEntry> {

	@Override
	public void onAfterCreate(ObjectEntry objectEntry)
		throws ModelListenerException {

		_sendMessage("onAfterCreate", null, objectEntry);
	}

	@Override
	public void onAfterRemove(ObjectEntry objectEntry)
		throws ModelListenerException {

		_sendMessage("onAfterRemove", null, objectEntry);
	}

	@Override
	public void onAfterUpdate(
			ObjectEntry originalObjectEntry, ObjectEntry objectEntry)
		throws ModelListenerException {

		_sendMessage("onAfterUpdate", originalObjectEntry, objectEntry);
	}

	@Override
	public void onBeforeCreate(ObjectEntry objectEntry)
		throws ModelListenerException {

		_sendMessage("onBeforeCreate", null, objectEntry);
	}

	@Override
	public void onBeforeRemove(ObjectEntry objectEntry)
		throws ModelListenerException {

		_sendMessage("onBeforeRemove", null, objectEntry);
	}

	@Override
	public void onBeforeUpdate(
			ObjectEntry originalObjectEntry, ObjectEntry objectEntry)
		throws ModelListenerException {

		_sendMessage("onBeforeUpdate", originalObjectEntry, objectEntry);
	}

	private void _sendMessage(
			String command, ObjectEntry originalObjectEntry,
			ObjectEntry objectEntry)
		throws ModelListenerException {

		try {
			ObjectDefinition objectDefinition =
				_objectDefinitionLocalService.fetchObjectDefinition(
					objectEntry.getObjectDefinitionId());

			if (objectDefinition == null) {
				return;
			}

			JSONObject objectEntryJSONObject = _jsonFactory.createJSONObject(
				objectEntry.toString());

			if (command.equals("onAfterCreate") ||
				command.equals("onBeforeCreate")) {

				objectEntryJSONObject.put("values", objectEntry.getValues());
			}

			JSONObject payloadJSONObject = JSONUtil.put(
				"objectEntry", objectEntryJSONObject);

			if (originalObjectEntry != null) {
				JSONObject originalObjectEntryJSONObject =
					_jsonFactory.createJSONObject(
						originalObjectEntry.toString());

				// TODO

				originalObjectEntryJSONObject.put(
					"values", originalObjectEntry.getValues());

				payloadJSONObject.put(
					"originalObjectEntry", originalObjectEntryJSONObject);
			}

			_messageBus.sendMessage(
				objectDefinition.getDestinationName(),
				new Message() {
					{
						put("command", command);
						setPayload(payloadJSONObject.toString());
					}
				});
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new ModelListenerException(exception);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ObjectEntryModelListener.class);

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private MessageBus _messageBus;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

}