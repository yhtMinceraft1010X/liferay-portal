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

package com.liferay.osb.commerce.provisioning.internal.cloud.client;

import com.liferay.osb.commerce.provisioning.configuration.ApplicationProfile;
import com.liferay.osb.commerce.provisioning.configuration.OSBCommerceProvisioningConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Ivica Cardic
 */
@Component(
	configurationPid = "com.liferay.osb.commerce.provisioning.configuration.OSBCommerceProvisioningConfiguration",
	service = UserAccountClientFactory.class
)
public class UserAccountClientFactory {

	public UserAccountClient getUserAccountClient() {
		if (_osbCommerceProvisioningConfiguration.applicationProfile() ==
				ApplicationProfile.DEVELOPMENT) {

			return new UserAccountClientMockImpl();
		}

		return new UserAccountClientImpl(
			_osbCommerceProvisioningConfiguration.
				osbCommercePortalInstanceDomainName(),
			_osbCommerceProvisioningConfiguration.
				osbCommercePortalInstanceOAuthClientId(),
			_osbCommerceProvisioningConfiguration.
				osbCommercePortalInstanceOAuthClientSecret(),
			_osbCommerceProvisioningConfiguration.
				osbCommercePortalInstancePassword(),
			_osbCommerceProvisioningConfiguration.
				osbCommercePortalInstancePort(),
			_osbCommerceProvisioningConfiguration.
				osbCommercePortalInstanceProtocol(),
			_osbCommerceProvisioningConfiguration.
				osbCommercePortalInstanceUsername());
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_osbCommerceProvisioningConfiguration =
			ConfigurableUtil.createConfigurable(
				OSBCommerceProvisioningConfiguration.class, properties);
	}

	private OSBCommerceProvisioningConfiguration
		_osbCommerceProvisioningConfiguration;

}