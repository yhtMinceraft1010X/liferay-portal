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

import com.liferay.osb.commerce.provisioning.internal.cloud.client.dto.PortalInstance;

import java.util.List;

/**
 * @author Ivica Cardic
 */
public interface DXPCloudProvisioningClient extends Client {

	public void deletePortalInstance(String portalInstanceId);

	public PortalInstance getPortalInstance(String portalInstanceId);

	public List<PortalInstance> getPortalInstances();

	public PortalInstance postPortalInstance(
		String domain, String portalInitializerKey);

	public PortalInstance updatePortalInstance(
		String domain, String portalInstanceId);

}