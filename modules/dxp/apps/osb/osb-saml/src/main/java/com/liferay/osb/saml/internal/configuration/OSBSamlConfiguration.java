/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.saml.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Marta Medio
 */
@ExtendedObjectClassDefinition(
	generateUI = false, scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.osb.saml.internal.configuration.OSBSamlConfiguration"
)
public interface OSBSamlConfiguration {

	@Meta.AD(
		deflt = "false", id = "saml.saas.production.environment",
		required = false
	)
	public boolean productionEnvironment();

	@Meta.AD(id = "saml.saas.pre.shared.key", required = false)
	public String preSharedKey();

	@Meta.AD(id = "saml.saas.target.instance.import.url", required = false)
	public String targetInstanceImportURL();

}