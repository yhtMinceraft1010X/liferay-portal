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

import com.liferay.headless.osb.commerce.portal.instance.client.dto.v1_0.UserAccount;
import com.liferay.headless.osb.commerce.portal.instance.client.resource.v1_0.UserAccountResource;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.Validator;

import org.apache.http.HttpHeaders;

/**
 * @author Ivica Cardic
 */
public class UserAccountClientImpl
	extends BaseClientImpl implements UserAccountClient {

	public UserAccountClientImpl(
		String osbCommerceInstanceDomainName,
		String osbCommerceInstanceOauthClientId,
		String osbCommerceInstanceOAuthClientSecret,
		String osbCommerceInstancePassword, int osbCommerceInstancePort,
		String osbCommerceInstanceProtocol,
		String osbCommerceInstanceUserName) {

		_osbCommerceInstanceDomainName = osbCommerceInstanceDomainName;
		_osbCommerceInstanceOauthClientId = osbCommerceInstanceOauthClientId;
		_osbCommerceInstanceOAuthClientSecret =
			osbCommerceInstanceOAuthClientSecret;
		_osbCommerceInstancePassword = osbCommerceInstancePassword;
		_osbCommerceInstancePort = osbCommerceInstancePort;
		_osbCommerceInstanceProtocol = osbCommerceInstanceProtocol;
		_osbCommerceInstanceUserName = osbCommerceInstanceUserName;
	}

	@Override
	public void destroy() {
	}

	@Override
	public UserAccount postUserAccount(
			String portalInstanceId, UserAccount userAccount)
		throws Exception {

		String authorizationHeader = null;

		if (Validator.isNull(_osbCommerceInstanceOauthClientId)) {
			authorizationHeader = getBasicAuthorizationHeader(
				_osbCommerceInstancePassword, _osbCommerceInstanceUserName);
		}
		else {
			authorizationHeader = getBearerAuthorizationHeader(
				_osbCommerceInstanceOauthClientId,
				_osbCommerceInstanceOAuthClientSecret,
				_getOSBCommerceInstanceURI(_osbCommerceInstanceDomainName));
		}

		UserAccountResource.Builder builder = UserAccountResource.builder();

		UserAccountResource userAccountResource = builder.endpoint(
			_osbCommerceInstanceDomainName, _osbCommerceInstancePort,
			_osbCommerceInstanceProtocol
		).header(
			HttpHeaders.AUTHORIZATION, authorizationHeader
		).build();

		return userAccountResource.postUserAccount(
			portalInstanceId, userAccount);
	}

	private String _getOSBCommerceInstanceURI(String domainName) {
		return StringBundler.concat(
			_osbCommerceInstanceProtocol, "://", domainName, ":",
			_osbCommerceInstancePort);
	}

	private final String _osbCommerceInstanceDomainName;
	private final String _osbCommerceInstanceOauthClientId;
	private final String _osbCommerceInstanceOAuthClientSecret;
	private final String _osbCommerceInstancePassword;
	private final int _osbCommerceInstancePort;
	private final String _osbCommerceInstanceProtocol;
	private final String _osbCommerceInstanceUserName;

}