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

package com.liferay.osb.commerce.provisioning.internal.util;

import com.liferay.osb.commerce.provisioning.configuration.OSBCommerceProvisioningConfiguration;
import com.liferay.osb.commerce.provisioning.util.OSBCommercePortalInstance;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Ivica Cardic
 */
@Component(
	configurationPid = "com.liferay.osb.commerce.provisioning.configuration.OSBCommerceProvisioningConfiguration",
	immediate = true, service = OSBCommercePortalInstance.class
)
public class OSBCommercePortalInstanceImpl
	implements OSBCommercePortalInstance {

	@Override
	public String getPortalInstanceURL(String portalInstanceVirtualHostname) {
		StringBundler sb = new StringBundler(6);

		sb.append(
			_osbCommerceProvisioningConfiguration.
				osbCommercePortalInstanceProtocol());
		sb.append(StringPool.COLON);
		sb.append(StringPool.DOUBLE_SLASH);
		sb.append(portalInstanceVirtualHostname);
		sb.append(StringPool.COLON);
		sb.append(
			_osbCommerceProvisioningConfiguration.
				osbCommercePortalInstancePort());

		return sb.toString();
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