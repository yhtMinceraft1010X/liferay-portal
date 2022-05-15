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

package com.liferay.portal.osgi.web.portlet.container.upload.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.osgi.web.portlet.container.test.BasePortletContainerTestCase;
import com.liferay.portal.osgi.web.portlet.container.test.util.PortletContainerTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.theme.ThemeDisplayFactory;
import com.liferay.portal.upload.LiferayServletRequest;
import com.liferay.upload.UniqueFileNameProvider;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.Portlet;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

import org.springframework.mock.web.MockMultipartHttpServletRequest;

/**
 * @author Manuel de la Peña
 */
@RunWith(Arquillian.class)
public class UploadPortletTest extends BasePortletContainerTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_testUploadPortlet = new TestUploadPortlet() {

			@Override
			public void serveResource(
					ResourceRequest resourceRequest,
					ResourceResponse resourceResponse)
				throws IOException {

				PrintWriter printWriter = resourceResponse.getWriter();

				PortletURL portletURL = resourceResponse.createActionURL();

				printWriter.write(
					MapUtil.getString(
						HttpComponentsUtil.getParameterMap(
							HttpComponentsUtil.getQueryString(
								portletURL.toString())),
						"p_auth"));
			}

		};

		registerMVCActionCommand(
			new TestUploadMVCActionCommand(
				_testUploadPortlet, _uniqueFileNameProvider));

		registerMVCPortlet(_testUploadPortlet);
	}

	@Test
	public void testUploadFile() throws Exception {
		String content = "Enterprise. Open Source. For Life.";

		PortletContainerTestUtil.Response response = testUpload(
			content.getBytes());

		Assert.assertEquals(200, response.getCode());

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			response.getBody());

		Assert.assertTrue(jsonObject.getBoolean("success"));

		String key =
			group.getGroupId() + "_0_" + TestUploadPortlet.PARAMETER_NAME;

		TestFileEntry actualTestFileEntry = _testUploadPortlet.get(key);

		Assert.assertNotNull(actualTestFileEntry);
		Assert.assertEquals(
			content, StringUtil.read(actualTestFileEntry.getInputStream()));
	}

	@Test
	public void testUploadZeroBytesFile() throws Exception {
		PortletContainerTestUtil.Response response = testUpload(new byte[0]);

		Assert.assertEquals(200, response.getCode());

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			response.getBody());

		Assert.assertTrue(jsonObject.getBoolean("success"));

		String key =
			group.getGroupId() + "_0_" + TestUploadPortlet.PARAMETER_NAME;

		TestFileEntry actualTestFileEntry = _testUploadPortlet.get(key);

		Assert.assertNull(actualTestFileEntry.getInputStream());
	}

	protected void registerMVCActionCommand(MVCActionCommand mvcActionCommand)
		throws Exception {

		Bundle bundle = FrameworkUtil.getBundle(getClass());

		BundleContext bundleContext = bundle.getBundleContext();

		ServiceRegistration<MVCActionCommand> serviceRegistration =
			bundleContext.registerService(
				MVCActionCommand.class, mvcActionCommand,
				HashMapDictionaryBuilder.<String, Object>put(
					"javax.portlet.name", TestUploadPortlet.PORTLET_NAME
				).put(
					"mvc.command.name", TestUploadPortlet.MVC_COMMAND_NAME
				).build());

		serviceRegistrations.add(serviceRegistration);
	}

	protected void registerMVCPortlet(Portlet portlet) throws Exception {
		setUpPortlet(
			portlet,
			HashMapDictionaryBuilder.<String, Object>put(
				"com.liferay.portlet.private-request-attributes",
				Boolean.FALSE.toString()
			).put(
				"com.liferay.portlet.private-session-attributes",
				Boolean.FALSE.toString()
			).put(
				"com.liferay.portlet.scopeable", Boolean.TRUE.toString()
			).put(
				"com.liferay.portlet.struts-path", TestUploadPortlet.MVC_PATH
			).put(
				"com.liferay.portlet.use-default-template",
				Boolean.TRUE.toString()
			).put(
				"javax.portlet.display-name", "Test Upload Portlet"
			).put(
				"javax.portlet.expiration-cache", "0"
			).put(
				"javax.portlet.init-param.check-auth-token",
				Boolean.FALSE.toString()
			).put(
				"javax.portlet.init-param.single-page-application-cacheable",
				Boolean.FALSE.toString()
			).put(
				"javax.portlet.init-param.template-path", "/"
			).put(
				"javax.portlet.init-param.view-template",
				"/" + TestUploadPortlet.PORTLET_NAME + "/view.jsp"
			).put(
				"javax.portlet.name", TestUploadPortlet.PORTLET_NAME
			).put(
				"javax.portlet.resource-bundle", "content.Language"
			).put(
				"javax.portlet.security-role-ref", "guest,power-user,user"
			).put(
				"javax.portlet.supports.mime-type", "text/html"
			).build(),
			TestUploadPortlet.PORTLET_NAME);
	}

	protected void setUp(
			LiferayServletRequest liferayServletRequest, Layout layout)
		throws Exception {

		if (liferayServletRequest == null) {
			throw new IllegalArgumentException(
				"Liferay servlet request is null");
		}

		if (layout == null) {
			throw new IllegalArgumentException("Layout is null");
		}

		HttpServletRequest httpServletRequest =
			(HttpServletRequest)liferayServletRequest.getRequest();

		httpServletRequest.setAttribute(WebKeys.LAYOUT, layout);

		ThemeDisplay themeDisplay = ThemeDisplayFactory.create();

		themeDisplay.setCompany(
			CompanyLocalServiceUtil.getCompany(layout.getCompanyId()));
		themeDisplay.setLayout(layout);
		themeDisplay.setLayoutSet(layout.getLayoutSet());
		themeDisplay.setPlid(layout.getPlid());
		themeDisplay.setPortalURL(TestPropsValues.PORTAL_URL);
		themeDisplay.setRequest(httpServletRequest);

		Group group = layout.getGroup();

		themeDisplay.setScopeGroupId(group.getGroupId());
		themeDisplay.setSiteGroupId(group.getGroupId());

		themeDisplay.setUser(TestPropsValues.getUser());

		httpServletRequest.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);
	}

	protected PortletContainerTestUtil.Response testUpload(byte[] bytes)
		throws Exception {

		LiferayServletRequest liferayServletRequest =
			PortletContainerTestUtil.getMultipartRequest(
				TestUploadPortlet.PARAMETER_NAME, bytes);

		setUp(liferayServletRequest, layout);

		ServletRequest servletRequest = liferayServletRequest.getRequest();

		MockMultipartHttpServletRequest mockServletRequest =
			(MockMultipartHttpServletRequest)servletRequest;

		String url = PortletURLBuilder.create(
			PortletURLFactoryUtil.create(
				mockServletRequest, TestUploadPortlet.PORTLET_NAME,
				layout.getPlid(), PortletRequest.ACTION_PHASE)
		).setActionName(
			TestUploadPortlet.MVC_COMMAND_NAME
		).setParameter(
			"randomId", RandomTestUtil.randomString()
		).buildString();

		mockServletRequest.addParameter("Cookie", new String[] {"test"});

		return PortletContainerTestUtil.postMultipart(
			url, mockServletRequest, TestUploadPortlet.PARAMETER_NAME);
	}

	private TestUploadPortlet _testUploadPortlet;

	@Inject
	private UniqueFileNameProvider _uniqueFileNameProvider;

}