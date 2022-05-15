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

package com.liferay.document.library.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Adolfo Pérez
 */
@ExtendedObjectClassDefinition(
	category = "documents-and-media",
	scope = ExtendedObjectClassDefinition.Scope.GROUP
)
@Meta.OCD(
	id = "com.liferay.document.library.internal.configuration.DLSizeLimitConfiguration",
	localization = "content/Language", name = "dl-size-limit-configuration-name"
)
public interface DLSizeLimitConfiguration {

	@Meta.AD(
		deflt = "0", description = "file-max-size-help",
		name = "maximum-file-size", required = false
	)
	public long fileMaxSize();

	@Meta.AD(deflt = "", name = "mime-type-size-limit-name", required = false)
	public String[] mimeTypeSizeLimit();

}