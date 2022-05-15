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

package com.liferay.oauth2.provider.client.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.oauth2.provider.constants.GrantType;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Collections;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.BundleActivator;

/**
 * @author Marta Medio
 */
@RunWith(Arquillian.class)
public class TokenIntrospectionTest extends BaseClientTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testTokenIntrospectionApplicationCode() {
		String applicationClientId = "oauthTestApplicationCode";

		String token = getToken(
			applicationClientId, null,
			getAuthorizationCodeBiFunction("test@liferay.com", "test", null),
			this::parseTokenString);

		Assert.assertNotNull(token);

		Invocation.Builder invocationBuilder =
			_getTokenIntrospectionWebTarget().request();

		MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();

		formData.add("client_id", applicationClientId);
		formData.add("client_secret", "oauthTestApplicationSecret");
		formData.add("token", token);

		Response response = invocationBuilder.post(Entity.form(formData));

		Assert.assertEquals(
			Response.Status.OK.getStatusCode(), response.getStatus());

		Assert.assertEquals(
			applicationClientId, parseJsonField(response, "client_id"));
	}

	@Test
	public void testTokenIntrospectionApplicationCodePKCE() {
		String applicationClientId = "oauthTestApplicationCodePKCE";

		String token = getToken(
			applicationClientId, null,
			getAuthorizationCodePKCEBiFunction(
				"test@liferay.com", "test", null),
			this::parseTokenString);

		Assert.assertNotNull(token);

		Invocation.Builder invocationBuilder =
			_getTokenIntrospectionWebTarget().request();

		MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();

		formData.add("client_id", applicationClientId);
		formData.add("client_secret", StringPool.BLANK);
		formData.add("token", token);

		Response response = invocationBuilder.post(Entity.form(formData));

		Assert.assertEquals(
			Response.Status.OK.getStatusCode(), response.getStatus());

		Assert.assertEquals(
			applicationClientId, parseJsonField(response, "client_id"));
	}

	public static class TokenIntrospectionTestPreparatorBundleActivator
		extends BaseTestPreparatorBundleActivator {

		@Override
		protected void prepareTest() throws Exception {
			long defaultCompanyId = PortalUtil.getDefaultCompanyId();

			User user = UserTestUtil.getAdminUser(defaultCompanyId);

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplicationCode",
				Collections.singletonList(GrantType.AUTHORIZATION_CODE), false,
				Collections.singletonList("everything"), false);
			createOAuth2ApplicationWithNone(
				defaultCompanyId, user, "oauthTestApplicationCodePKCE",
				Collections.singletonList(GrantType.AUTHORIZATION_CODE_PKCE),
				Collections.singletonList("http://redirecturi:8080"), false,
				Collections.singletonList("everything"), false);
		}

	}

	@Override
	protected BundleActivator getBundleActivator() {
		return new TokenIntrospectionTest.
			TokenIntrospectionTestPreparatorBundleActivator();
	}

	private WebTarget _getTokenIntrospectionWebTarget() {
		WebTarget webTarget = getWebTarget();

		webTarget = webTarget.path("o");
		webTarget = webTarget.path("oauth2");
		webTarget = webTarget.path("introspect");

		return webTarget;
	}

}