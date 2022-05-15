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

package com.liferay.oauth2.provider.rest.internal.endpoint.access.token.authentication.handler;

import com.liferay.oauth2.provider.rest.internal.configuration.admin.service.OAuth2InAssertionManagedServiceFactory;
import com.liferay.oauth2.provider.rest.internal.endpoint.constants.OAuth2ProviderRESTEndpointConstants;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.rs.security.jose.common.JoseConstants;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKeys;
import org.apache.cxf.rs.security.jose.jwk.JwkUtils;
import org.apache.cxf.rs.security.jose.jws.HmacJwsSignatureVerifier;
import org.apache.cxf.rs.security.jose.jws.JwsSignatureVerifier;
import org.apache.cxf.rs.security.jose.jws.JwsUtils;
import org.apache.cxf.rs.security.jose.jwt.JwtConstants;
import org.apache.cxf.rs.security.jose.jwt.JwtToken;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.grants.jwt.Constants;
import org.apache.cxf.rs.security.oauth2.grants.jwt.JwtBearerAuthHandler;
import org.apache.cxf.rs.security.oauth2.provider.ClientRegistrationProvider;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.apache.cxf.security.SecurityContext;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

/**
 * @author Arthur Chan
 */
@Provider
public class LiferayJWTBearerAuthenticationHandler
	extends JwtBearerAuthHandler {

	@Override
	public void filter(ContainerRequestContext containerRequestContext) {
		UriInfo uriInfo = containerRequestContext.getUriInfo();

		if (!StringUtil.startsWith(uriInfo.getPath(), "token")) {
			return;
		}

		Message message = JAXRSUtils.getCurrentMessage();

		HttpServletRequest httpServletRequest = (HttpServletRequest)message.get(
			AbstractHTTPDestination.HTTP_REQUEST);

		if (!_isUsingJWTAssertionForClientAuthentication(httpServletRequest)) {
			return;
		}

		String assertion = ParamUtil.getString(
			httpServletRequest, Constants.CLIENT_AUTH_ASSERTION_PARAM);

		if (assertion == null) {
			throw new NotAuthorizedException("Missing JWT assertion");
		}

		JwtToken jwtToken = super.getJwtToken(assertion);

		String claimSubject = (String)jwtToken.getClaim(
			JwtConstants.CLAIM_SUBJECT);

		String clientId = ParamUtil.getString(
			httpServletRequest, OAuthConstants.CLIENT_ID);

		if (Validator.isNotNull(clientId) && !clientId.equals(claimSubject)) {
			throw new NotAuthorizedException(
				"Client ID parameter does not match JWT subject");
		}

		message.put(OAuthConstants.CLIENT_ID, claimSubject);

		SecurityContext securityContext = configureSecurityContext(jwtToken);

		if (securityContext != null) {
			JAXRSUtils.getCurrentMessage(
			).put(
				SecurityContext.class, securityContext
			);
		}
	}

	public void setClientRegistrationProvider(
		ClientRegistrationProvider clientRegistrationProvider) {

		_clientRegistrationProvider = clientRegistrationProvider;
	}

	public void setOAuth2InAssertionManagedServiceFactory(
		OAuth2InAssertionManagedServiceFactory
			oAuth2InAssertionManagedServiceFactory) {

		_oAuth2InAssertionManagedServiceFactory =
			oAuth2InAssertionManagedServiceFactory;
	}

	@Override
	protected JwsSignatureVerifier getInitializedSignatureVerifier(
		JwtToken jwtToken) {

		Client client = _clientRegistrationProvider.getClient(
			(String)jwtToken.getClaim(JwtConstants.CLAIM_SUBJECT));

		String tokenEndpointAuthMethod = client.getTokenEndpointAuthMethod();

		try {
			if (tokenEndpointAuthMethod.equals("client_secret_jwt")) {
				return new HmacJwsSignatureVerifier(client.getClientSecret());
			}

			if (tokenEndpointAuthMethod.equals("private_key_jwt")) {
				Map<String, String> clientProperties = client.getProperties();

				JsonWebKeys jsonWebKeys = JwkUtils.readJwkSet(
					clientProperties.get(
						OAuth2ProviderRESTEndpointConstants.
							PROPERTY_KEY_CLIENT_JWKS));

				return JwsUtils.getSignatureVerifier(
					jsonWebKeys.getKey(
						(String)jwtToken.getJwsHeader(
							JoseConstants.HEADER_KEY_ID)));
			}

			throw new IllegalArgumentException(
				"Client is configured to not use JWT as a client " +
					"authentication method");
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(exception);
			}

			throw new NotAuthorizedException(OAuthConstants.INVALID_CLIENT);
		}
	}

	private boolean _isUsingJWTAssertionForClientAuthentication(
		HttpServletRequest httpServletRequest) {

		String assertionType = ParamUtil.getString(
			httpServletRequest, Constants.CLIENT_AUTH_ASSERTION_TYPE);

		if (Validator.isNull(assertionType)) {
			return false;
		}

		if (Constants.CLIENT_AUTH_JWT_BEARER.equals(
				HttpUtils.urlDecode(assertionType))) {

			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayJWTBearerAuthenticationHandler.class);

	private ClientRegistrationProvider _clientRegistrationProvider;
	private OAuth2InAssertionManagedServiceFactory
		_oAuth2InAssertionManagedServiceFactory;

}