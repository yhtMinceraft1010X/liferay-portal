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

package com.liferay.object.rest.internal;

import com.liferay.object.model.ObjectDefinition;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Javier de Arcos
 */
@Component(service = ObjectDefinitionRESTContextPathRegistry.class)
public class ObjectDefinitionRESTContextPathRegistry {

	public ObjectDefinition getObjectDefinition(
		long companyId, String restContextPath) {

		Map<Long, ObjectDefinition> objectDefinitions =
			_objectDefinitions.get(restContextPath);

		if (objectDefinitions != null) {
			return objectDefinitions.get(companyId);
		}

		return null;
	}

	public boolean hasObjectDefinition(String restContextPath) {
		return _objectDefinitions.containsKey(restContextPath);
	}

	public void register(ObjectDefinition objectDefinition) {
		Map<Long, ObjectDefinition> objectDefinitions =
			_objectDefinitions.get(objectDefinition.getRESTContextPath());

		if (objectDefinitions == null) {
			objectDefinitions = new HashMap<>();

			_objectDefinitions.put(
				objectDefinition.getRESTContextPath(), objectDefinitions);
		}

		objectDefinitions.put(
			objectDefinition.getCompanyId(), objectDefinition);
	}

	public void unregister(ObjectDefinition objectDefinition) {
		Map<Long, ObjectDefinition> objectDefinitions =
			_objectDefinitions.get(objectDefinition.getRESTContextPath());

		objectDefinitions.remove(objectDefinition.getCompanyId());

		if (objectDefinitions.isEmpty()) {
			_objectDefinitions.remove(objectDefinition.getRESTContextPath());
		}
	}

	private final Map<String, Map<Long, ObjectDefinition>>
		_objectDefinitions = new HashMap<>();

}