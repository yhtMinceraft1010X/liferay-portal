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
	public void onAfterCreate(ObjectEntry objectEntry) {
		_sendMessage("onAfterCreate", objectEntry);
	}

	@Override
	public void onAfterRemove(ObjectEntry objectEntry) {
		_sendMessage("onAfterRemove", objectEntry);
	}

	@Override
	public void onAfterUpdate(ObjectEntry objectEntry) {
		_sendMessage("onAfterUpdate", objectEntry);
	}

	@Override
	public void onBeforeCreate(ObjectEntry objectEntry) {
		_sendMessage("onBeforeCreate", objectEntry);
	}

	@Override
	public void onBeforeRemove(ObjectEntry objectEntry) {
		_sendMessage("onBeforeRemove", objectEntry);
	}

	@Override
	public void onBeforeUpdate(ObjectEntry objectEntry) {
		_sendMessage("onBeforeUpdate", objectEntry);
	}

	private void _sendMessage(String command, ObjectEntry objectEntry) {
		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.fetchObjectDefinition(
				objectEntry.getObjectDefinitionId());

		if (objectDefinition == null) {
			return;
		}

		_messageBus.sendMessage(
			objectDefinition.getDestinationName(),
			new Message() {
				{
					put("command", command);
					setPayload(objectEntry.toString());
				}
			});
	}

	@Reference
	private MessageBus _messageBus;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

}