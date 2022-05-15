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

package com.liferay.on.demand.admin.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Pei-Jung Lan
 */
@ExtendedObjectClassDefinition(category = "users")
@Meta.OCD(
	id = "com.liferay.on.demand.admin.internal.configuration.OnDemandAdminConfiguration",
	localization = "content/Language",
	name = "on-demand-admin-configuration-name"
)
public interface OnDemandAdminConfiguration {

	@Meta.AD(
		deflt = "5",
		description = "authentication-token-expiration-time-description",
		name = "authentication-token-expiration-time", required = false
	)
	public int authenticationTokenExpirationTime();

	@Meta.AD(
		deflt = "24", description = "clean-up-interval-description",
		name = "clean-up-interval", required = false
	)
	public int cleanUpInterval();

}