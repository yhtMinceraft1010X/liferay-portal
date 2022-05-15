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
import com.liferay.oauth2.provider.internal.test.TestRunnablePostHandlingApplication;
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Collections;
import java.util.Dictionary;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.BundleActivator;

/**
 * @author Stian Sigvartsen
 */
@RunWith(Arquillian.class)
public class ScopeFinderTest extends BaseClientTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testUnavailableAssignedScopeAliases() {
		String token = getToken(
			"oauthTestClientCredentials", null,
			this::getClientCredentialsResponse, this::parseTokenString);

		Assert.assertNotNull(token);

		WebTarget webTarget = getWebTarget("/annotated");

		Invocation.Builder invocationBuilder = authorize(
			webTarget.request(), token);

		Assert.assertEquals(
			"everything.read", invocationBuilder.get(String.class));

		// Install the overriding scope finder which does not publish
		// "everything.read"

		invocationBuilder.post(null, String.class);

		// Check that existing tokens remain unaffected

		Assert.assertEquals(
			"everything.read", invocationBuilder.get(String.class));

		// Get a new token. This token should be restricted to current scopes
		// returned by the scope finder.

		invocationBuilder = authorize(
			webTarget.request(),
			getToken(
				"oauthTestClientCredentials", null,
				this::getClientCredentialsResponse, this::parseTokenString));

		Assert.assertEquals(
			403,
			invocationBuilder.get(
			).getStatus());
	}

	public static class ScopeFinderTestPreparatorBundleActivator
		extends BaseTestPreparatorBundleActivator {

		@Override
		protected void prepareTest() throws Exception {
			ScopeFinder scopeFinder = Collections::emptyList;

			Dictionary<String, Object> properties1 =
				HashMapDictionaryBuilder.<String, Object>put(
					"oauth2.scope.checker.type", "annotations"
				).put(
					"osgi.jaxrs.name", "Test.Application"
				).build();

			Dictionary<String, Object> properties2 =
				HashMapDictionaryBuilder.<String, Object>put(
					"osgi.jaxrs.name", "Test.Application"
				).put(
					"service.ranking", Integer.MAX_VALUE
				).build();

			registerJaxRsApplication(
				new TestRunnablePostHandlingApplication(
					() -> registerScopeFinder(scopeFinder, properties2)),
				"annotated", properties1);

			long defaultCompanyId = PortalUtil.getDefaultCompanyId();

			User user = UserTestUtil.getAdminUser(defaultCompanyId);

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestClientCredentials",
				Collections.singletonList(GrantType.CLIENT_CREDENTIALS),
				Collections.singletonList("everything.read"));
		}

	}

	@Override
	protected BundleActivator getBundleActivator() {
		return new ScopeFinderTestPreparatorBundleActivator();
	}

}