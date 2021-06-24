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

package com.liferay.osb.commerce.provisioning.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Ivica Cardic
 */
@ExtendedObjectClassDefinition(category = "osb-commerce")
@Meta.OCD(
	id = "com.liferay.osb.commerce.provisioning.configuration.OSBCommerceProvisioningConfiguration",
	localization = "content/Language",
	name = "osb-commerce-provisioning-configuration-name"
)
public interface OSBCommerceProvisioningConfiguration {

	@Meta.AD(deflt = "DEVELOPMENT", name = "environment", required = false)
	public ApplicationProfile applicationProfile();

	@Meta.AD(
		deflt = "http://localhost:9999", name = "dxp-cloud-api-url",
		required = false
	)
	public String dxpCloudAPIURL();

	@Meta.AD(deflt = "test", name = "dxp-cloud-api-password", required = false)
	public String dxpCloudAPIPassword();

	@Meta.AD(
		deflt = "test@liferay.com", name = "dxp-cloud-api-username",
		required = false
	)
	public String dxpCloudAPIUsername();

	@Meta.AD(
		deflt = "8080", name = "osb-commerce-portal-instance-port",
		required = false
	)
	public int osbCommercePortalInstancePort();

	@Meta.AD(
		deflt = "http", name = "osb-commerce-portal-instance-protocol",
		required = false
	)
	public String osbCommercePortalInstanceProtocol();

}