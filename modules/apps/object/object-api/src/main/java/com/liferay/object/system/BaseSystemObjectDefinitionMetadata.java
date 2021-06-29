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

package com.liferay.object.system;

import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectFieldLocalService;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Brian Wing Shun Chan
 */
public abstract class BaseSystemObjectDefinitionMetadata
	implements SystemObjectDefinitionMetadata {

	protected ObjectField createObjectField(String name, String type) {
		return createObjectField(null, name, type);
	}

	protected ObjectField createObjectField(
		String dbColumnName, String name, String type) {

		ObjectField objectField = objectFieldLocalService.createObjectField(0);

		objectField.setDBColumnName(dbColumnName);
		objectField.setIndexed(false);
		objectField.setIndexedAsKeyword(false);
		objectField.setName(name);
		objectField.setType(type);

		return objectField;
	}

	@Reference
	protected ObjectFieldLocalService objectFieldLocalService;

}