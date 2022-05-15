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

package com.liferay.dynamic.data.mapping.form.web.internal.portlet.action.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.constants.DDMPortletKeys;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceReport;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceReportLocalService;
import com.liferay.dynamic.data.mapping.test.util.DDMFormInstanceRecordTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMFormInstanceTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.dynamic.data.mapping.util.DDMFormReportDataUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletConfig;
import com.liferay.portal.kernel.portlet.PortletConfigFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.portlet.MockLiferayResourceRequest;
import com.liferay.portal.kernel.test.portlet.MockLiferayResourceResponse;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.TimeZoneUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.ByteArrayOutputStream;

import org.hamcrest.CoreMatchers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

/**
 * @author Carolina Barbosa
 */
@RunWith(Arquillian.class)
public class GetFormReportDataMVCResourceCommandTest {

	@ClassRule
	@Rule
	public static final TestRule testRule = new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_user = UserTestUtil.addGroupAdminUser(_group);

		_setUpGetFormReportDataMVCResourceCommandTest();
	}

	@Test
	public void testServeResource() throws Exception {
		DDMFormInstance ddmFormInstance =
			DDMFormInstanceTestUtil.addDDMFormInstance(
				_group, TestPropsValues.getUserId());

		DDMFormInstanceRecordTestUtil.addDDMFormInstanceRecord(
			ddmFormInstance,
			DDMFormValuesTestUtil.createDDMFormValuesWithRandomValues(
				ddmFormInstance.getDDMForm()),
			_group, _user.getUserId());

		DDMFormInstanceReport ddmFormInstanceReport =
			_ddmFormInstanceReportLocalService.
				getFormInstanceReportByFormInstanceId(
					ddmFormInstance.getFormInstanceId());

		Assert.assertNotNull(ddmFormInstanceReport);

		JSONObject jsonObject = _getJSONObject(
			ddmFormInstance.getFormInstanceId());

		Assert.assertEquals(
			ddmFormInstanceReport.getData(), jsonObject.get("data"));
		Assert.assertEquals(
			String.valueOf(
				DDMFormReportDataUtil.getFieldsJSONArray(
					ddmFormInstanceReport)),
			jsonObject.getString("fields"));
		Assert.assertThat(
			jsonObject.getString("formReportRecordsFieldValuesURL"),
			CoreMatchers.containsString(
				"formInstanceId=" + ddmFormInstanceReport.getFormInstanceId()));
		Assert.assertEquals(
			DDMFormReportDataUtil.getLastModifiedDate(
				ddmFormInstanceReport, LocaleUtil.getSiteDefault(),
				TimeZoneUtil.getDefault()),
			jsonObject.get("lastModifiedDate"));
		Assert.assertEquals(
			DDMFormReportDataUtil.getTotalItems(ddmFormInstanceReport),
			jsonObject.get("totalItems"));
	}

	@Test
	public void testServeResourceWithError() throws Exception {
		JSONObject jsonObject = _getJSONObject(RandomTestUtil.randomLong());

		Assert.assertTrue(jsonObject.has("errorMessage"));
	}

	private JSONObject _getJSONObject(long formInstanceId) throws Exception {
		MockLiferayResourceResponse mockLiferayResourceResponse =
			new MockLiferayResourceResponse();

		_mvcResourceCommand.serveResource(
			_mockLiferayResourceRequest(formInstanceId),
			mockLiferayResourceResponse);

		ByteArrayOutputStream byteArrayOutputStream =
			(ByteArrayOutputStream)
				mockLiferayResourceResponse.getPortletOutputStream();

		return JSONFactoryUtil.createJSONObject(
			new String(byteArrayOutputStream.toByteArray()));
	}

	private LiferayPortletConfig _getLiferayPortletConfig() {
		return (LiferayPortletConfig)PortletConfigFactoryUtil.create(
			_portletLocalService.getPortletById(
				DDMPortletKeys.DYNAMIC_DATA_MAPPING_FORM),
			null);
	}

	private ThemeDisplay _getThemeDisplay() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setLocale(LocaleUtil.getSiteDefault());
		themeDisplay.setTimeZone(TimeZoneUtil.getDefault());

		return themeDisplay;
	}

	private MockLiferayResourceRequest _mockLiferayResourceRequest(
			long formInstanceId)
		throws Exception {

		MockLiferayResourceRequest mockLiferayResourceRequest =
			new MockLiferayResourceRequest();

		mockLiferayResourceRequest.setAttribute(
			JavaConstants.JAVAX_PORTLET_CONFIG, _getLiferayPortletConfig());
		mockLiferayResourceRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());
		mockLiferayResourceRequest.addParameter(
			"formInstanceId", String.valueOf(formInstanceId));

		return mockLiferayResourceRequest;
	}

	private void _setUpGetFormReportDataMVCResourceCommandTest() {
		ReflectionTestUtil.setFieldValue(
			_mvcResourceCommand, "_ddmFormInstanceReportLocalService",
			_ddmFormInstanceReportLocalService);
		ReflectionTestUtil.setFieldValue(
			_mvcResourceCommand, "_portal", _portal);
	}

	@Inject
	private DDMFormInstanceReportLocalService
		_ddmFormInstanceReportLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject(
		filter = "mvc.command.name=/dynamic_data_mapping_form/get_form_report_data"
	)
	private MVCResourceCommand _mvcResourceCommand;

	@Inject
	private Portal _portal;

	@Inject
	private PortletLocalService _portletLocalService;

	@DeleteAfterTestRun
	private User _user;

}