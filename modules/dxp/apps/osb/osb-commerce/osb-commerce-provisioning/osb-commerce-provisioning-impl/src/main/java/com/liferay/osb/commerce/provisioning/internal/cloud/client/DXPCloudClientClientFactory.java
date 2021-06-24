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

import com.liferay.osb.commerce.provisioning.configuration.OSBCommerceProvisioningConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.instances.initializer.PortalInstanceInitializerRegistry;
import com.liferay.portal.instances.service.PortalInstancesLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Portal;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 */
@Component(
	configurationPid = "com.liferay.osb.commerce.provisioning.configuration.OSBCommerceProvisioningConfiguration",
	service = DXPCloudClientClientFactory.class
)
public class DXPCloudClientClientFactory {

	public DXPCloudProvisioningClient getDXPCloudClient() {
		return new DXPCloudProvisioningClientImpl(
			_osbCommerceProvisioningConfiguration.dxpCloudAPIURL(),
			_osbCommerceProvisioningConfiguration.dxpCloudAPIPassword(),
			_osbCommerceProvisioningConfiguration.dxpCloudAPIUsername());
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_osbCommerceProvisioningConfiguration =
			ConfigurableUtil.createConfigurable(
				OSBCommerceProvisioningConfiguration.class, properties);
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	private OSBCommerceProvisioningConfiguration
		_osbCommerceProvisioningConfiguration;

	@Reference
	private Portal _portal;

	@Reference
	private PortalInstanceInitializerRegistry
		_portalInstanceInitializerRegistry;

	@Reference
	private PortalInstancesLocalService _portalInstancesLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private UserLocalService _userLocalService;

}