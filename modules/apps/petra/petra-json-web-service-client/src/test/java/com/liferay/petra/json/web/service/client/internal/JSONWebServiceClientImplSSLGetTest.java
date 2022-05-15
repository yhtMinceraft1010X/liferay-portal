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

package com.liferay.petra.json.web.service.client.internal;

import com.liferay.petra.json.web.service.client.JSONWebServiceException;
import com.liferay.petra.json.web.service.client.keystore.KeyStoreLoader;
import com.liferay.petra.json.web.service.client.server.simulator.HTTPSServerSimulator;
import com.liferay.petra.json.web.service.client.server.simulator.constants.SimulatorConstants;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Igor Beslic
 */
public class JSONWebServiceClientImplSSLGetTest
	extends BaseJSONWebServiceClientTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void test200OKOnGetIfTLS11() throws Exception {
		JSONWebServiceClientImpl jsonWebServiceClientImpl =
			_createJsonWebServiceClient();

		HTTPSServerSimulator.start("TLSv1.1");

		String json = jsonWebServiceClientImpl.doGet(
			"/testGet/", getParameters("200"));

		HTTPSServerSimulator.stop();

		Assert.assertTrue(
			json,
			json.contains(
				SimulatorConstants.HTTP_PARAMETER_RESPOND_WITH_STATUS));
	}

	@Test
	public void test200OKOnGetIfTLS12() throws Exception {
		JSONWebServiceClientImpl jsonWebServiceClientImpl =
			_createJsonWebServiceClient();

		HTTPSServerSimulator.start("TLSv1.2");

		String json = jsonWebServiceClientImpl.doGet(
			"/testGet/", getParameters("200"));

		HTTPSServerSimulator.stop();

		Assert.assertTrue(
			json,
			json.contains(
				SimulatorConstants.HTTP_PARAMETER_RESPOND_WITH_STATUS));
	}

	@Test(expected = JSONWebServiceException.class)
	public void testJSONWebServiceExceptionOnGetIfTLS10() throws Exception {
		System.setProperty("https.protocols", "TLSv1.1");

		JSONWebServiceClientImpl jsonWebServiceClientImpl =
			_createJsonWebServiceClient();

		HTTPSServerSimulator.start("TLSv1");

		try {
			String json = jsonWebServiceClientImpl.doGet(
				"/testGet/", getParameters("200"));

			Assert.assertTrue(
				json,
				json.contains(
					SimulatorConstants.HTTP_PARAMETER_RESPOND_WITH_STATUS));
		}
		finally {
			HTTPSServerSimulator.stop();
		}
	}

	private JSONWebServiceClientImpl _createJsonWebServiceClient()
		throws Exception {

		JSONWebServiceClientImpl jsonWebServiceClientImpl =
			new JSONWebServiceClientImpl();

		Map<String, Object> properties = getBaseProperties();

		properties.put(
			"headers", "headerKey1=headerValue1;Accept=application/json;");
		properties.put("hostPort", 9443);

		KeyStoreLoader keyStoreLoader = new KeyStoreLoader();

		properties.put(
			"keyStore", keyStoreLoader.getKeyStore("localhost.jks", "liferay"));

		properties.put("protocol", "https");

		jsonWebServiceClientImpl.activate(properties);

		return jsonWebServiceClientImpl;
	}

}