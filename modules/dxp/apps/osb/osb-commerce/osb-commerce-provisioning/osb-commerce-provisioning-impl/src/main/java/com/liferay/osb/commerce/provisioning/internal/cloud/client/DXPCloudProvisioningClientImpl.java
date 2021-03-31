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

import com.fasterxml.jackson.core.type.TypeReference;

import com.liferay.osb.commerce.provisioning.internal.cloud.client.dto.PortalInstance;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;

import org.apache.http.client.utils.URIBuilder;

/**
 * @author Ivica Cardic
 */
public class DXPCloudProvisioningClientImpl
	extends BaseClientImpl implements DXPCloudProvisioningClient {

	public DXPCloudProvisioningClientImpl(
		String dxpCloudAPIURL, String password, String userName) {

		_dxpCloudAPIURL = dxpCloudAPIURL;
		_password = password;
		_userName = userName;
	}

	@Override
	public void deletePortalInstance(String portalInstanceId) {
		executeDelete(
			getBasicAuthorizationHeader(_password, _userName),
			_getProvisioningPortalInstancesURI(portalInstanceId));
	}

	@Override
	public PortalInstance getPortalInstance(String portalInstanceId) {
		return executeGet(
			getBasicAuthorizationHeader(_password, _userName),
			_getProvisioningPortalInstancesURI(portalInstanceId),
			PortalInstance.class);
	}

	@Override
	public List<PortalInstance> getPortalInstances() {
		return executeGet(
			getBasicAuthorizationHeader(_password, _userName),
			new TypeReference<List<PortalInstance>>() {
			},
			_getProvisioningPortalInstancesURI());
	}

	@Override
	public PortalInstance postPortalInstance(
		String domain, String initializerKey) {

		try {
			URIBuilder uriBuilder = new URIBuilder(
				_getProvisioningPortalInstancesURI());

			uriBuilder.setParameter("initializerKey", initializerKey);

			URI uri = uriBuilder.build();

			return executePost(
				getBasicAuthorizationHeader(_password, _userName),
				HashMapBuilder.put(
					"domain", domain
				).build(),
				uri.toString(), PortalInstance.class);
		}
		catch (URISyntaxException uriSyntaxException) {
			throw new SystemException(uriSyntaxException);
		}
	}

	@Override
	public PortalInstance updatePortalInstance(
		String domain, String portalInstanceId) {

		return executeUpdate(
			getBasicAuthorizationHeader(_password, _userName),
			HashMapBuilder.put(
				"domain", domain
			).build(),
			PortalInstance.class,
			_getProvisioningPortalInstancesURI(portalInstanceId));
	}

	private String _getProvisioningPortalInstancesURI() {
		return _dxpCloudAPIURL + _PROVISIONING_SAAS_PORTAL_INSTANCES_PATH;
	}

	private String _getProvisioningPortalInstancesURI(String portalInstanceId) {
		return StringBundler.concat(
			_dxpCloudAPIURL, _PROVISIONING_SAAS_PORTAL_INSTANCES_PATH, "/",
			portalInstanceId);
	}

	private static final String _PROVISIONING_SAAS_PORTAL_INSTANCES_PATH =
		"/provisioning/saas/portal-instances";

	private final String _dxpCloudAPIURL;
	private final String _password;
	private final String _userName;

}