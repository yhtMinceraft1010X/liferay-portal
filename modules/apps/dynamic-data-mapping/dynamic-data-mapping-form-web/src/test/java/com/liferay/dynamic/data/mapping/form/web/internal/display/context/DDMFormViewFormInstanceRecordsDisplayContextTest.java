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

package com.liferay.dynamic.data.mapping.form.web.internal.display.context;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueRenderer;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.util.PropsImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * @author Marcos Martins
 */
public class DDMFormViewFormInstanceRecordsDisplayContextTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		PropsUtil.setProps(new PropsImpl());
	}

	@Before
	public void setUp() throws PortalException {
		_setUpPortalUtil();

		_setUpDDMFormViewFormInstanceRecordsDisplayContext();
	}

	@Test
	public void testGetAvailableLocalesCount() throws Exception {
		Assert.assertEquals(
			2,
			_ddmFormViewFormInstanceRecordsDisplayContext.
				getAvailableLocalesCount());
	}

	@Test
	public void testGetColumnValue() {
		Assert.assertEquals(
			"mockedRenderResponse, mockedRenderResponse",
			_ddmFormViewFormInstanceRecordsDisplayContext.getColumnValue(
				_mockDDMFormField(DDMFormFieldTypeConstants.SEARCH_LOCATION),
				"field",
				Arrays.asList(
					DDMFormValuesTestUtil.createDDMFormFieldValue(
						"field",
						DDMFormValuesTestUtil.createLocalizedValue(
							StringUtil.randomString(), LocaleUtil.US)),
					DDMFormValuesTestUtil.createDDMFormFieldValue(
						"field",
						DDMFormValuesTestUtil.createLocalizedValue(
							StringUtil.randomString(), LocaleUtil.US)))));
	}

	@Test
	public void testGetDefaultLocale() throws Exception {
		DDMFormInstanceRecord ddmFormInstanceRecord = Mockito.mock(
			DDMFormInstanceRecord.class);

		DDMFormValues ddmFormValues = Mockito.mock(DDMFormValues.class);

		Mockito.when(
			ddmFormValues.getDefaultLocale()
		).thenReturn(
			LocaleUtil.US
		);

		Mockito.when(
			ddmFormInstanceRecord.getDDMFormValues()
		).thenReturn(
			ddmFormValues
		);

		Assert.assertEquals(
			LocaleUtil.US,
			_ddmFormViewFormInstanceRecordsDisplayContext.getDefaultLocale(
				ddmFormInstanceRecord));
	}

	@Test
	public void testGetVisibleFields() {
		Assert.assertEquals(
			ListUtil.fromArray("city", "country"),
			_ddmFormViewFormInstanceRecordsDisplayContext.getVisibleFields(
				DDMFormTestUtil.createSearchLocationDDMFormField(
					DDMFormValuesTestUtil.createLocalizedValue(
						StringPool.BLANK, LocaleUtil.US),
					"field",
					DDMFormValuesTestUtil.createLocalizedValue(
						Arrays.toString(new String[] {"city", "country"}),
						LocaleUtil.US))));
	}

	private DDMForm _mockDDMForm() {
		DDMForm ddmForm = Mockito.mock(DDMForm.class);

		Mockito.when(
			ddmForm.getAvailableLocales()
		).thenReturn(
			new HashSet<Locale>(Arrays.asList(LocaleUtil.US, LocaleUtil.BRAZIL))
		);

		return ddmForm;
	}

	private DDMFormField _mockDDMFormField(String type) {
		DDMFormField ddmFormField = Mockito.mock(DDMFormField.class);

		Mockito.when(
			ddmFormField.getType()
		).thenReturn(
			type
		);

		return ddmFormField;
	}

	private DDMFormFieldTypeServicesTracker
		_mockDDMFormFieldTypeServicesTracker() {

		DDMFormFieldTypeServicesTracker ddmFormFieldTypeServicesTracker =
			Mockito.mock(DDMFormFieldTypeServicesTracker.class);

		DDMFormFieldValueRenderer ddmFormFieldValueRenderer =
			_mockDDMFormFieldValueRenderer();

		Mockito.when(
			ddmFormFieldTypeServicesTracker.getDDMFormFieldValueRenderer(
				Matchers.anyString())
		).thenReturn(
			ddmFormFieldValueRenderer
		);

		return ddmFormFieldTypeServicesTracker;
	}

	private DDMFormFieldValueRenderer _mockDDMFormFieldValueRenderer() {
		DDMFormFieldValueRenderer ddmFormFieldValueRenderer = Mockito.mock(
			DDMFormFieldValueRenderer.class);

		Mockito.when(
			ddmFormFieldValueRenderer.render(
				Matchers.anyString(), Matchers.any(DDMFormFieldValue.class),
				Matchers.any(Locale.class))
		).thenReturn(
			"mockedRenderResponse"
		);

		return ddmFormFieldValueRenderer;
	}

	private DDMFormInstance _mockDDMFormInstance() throws PortalException {
		DDMFormInstance ddmFormInstance = Mockito.mock(DDMFormInstance.class);

		DDMForm ddmForm = _mockDDMForm();

		Mockito.when(
			ddmFormInstance.getDDMForm()
		).thenReturn(
			ddmForm
		);

		DDMStructure ddmStructure = _mockDDMStructure(ddmForm);

		Mockito.when(
			ddmFormInstance.getStructure()
		).thenReturn(
			ddmStructure
		);

		return ddmFormInstance;
	}

	private DDMStructure _mockDDMStructure(DDMForm ddmForm) {
		DDMStructure ddmStructure = Mockito.mock(DDMStructure.class);

		Mockito.when(
			ddmStructure.getDDMForm()
		).thenReturn(
			ddmForm
		);

		return ddmStructure;
	}

	private ThemeDisplay _mockThemeDisplay() {
		ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);

		Mockito.when(
			themeDisplay.getPortletDisplay()
		).thenReturn(
			new PortletDisplay()
		);

		return themeDisplay;
	}

	private void _setUpDDMFormViewFormInstanceRecordsDisplayContext()
		throws PortalException {

		RenderRequest renderRequest = Mockito.mock(RenderRequest.class);

		ThemeDisplay themeDisplay = _mockThemeDisplay();

		Mockito.when(
			renderRequest.getAttribute(WebKeys.THEME_DISPLAY)
		).thenReturn(
			themeDisplay
		);

		Mockito.when(
			renderRequest.getParameter(Mockito.eq("redirect"))
		).thenReturn(
			"test"
		);

		_ddmFormViewFormInstanceRecordsDisplayContext =
			new DDMFormViewFormInstanceRecordsDisplayContext(
				renderRequest, Mockito.mock(RenderResponse.class),
				_mockDDMFormInstance(),
				Mockito.mock(DDMFormInstanceRecordLocalService.class),
				_mockDDMFormFieldTypeServicesTracker());
	}

	private void _setUpPortalUtil() {
		PortalUtil portalUtil = new PortalUtil();

		Portal portal = Mockito.mock(Portal.class);

		HttpServletRequest httpServletRequest = Mockito.mock(
			HttpServletRequest.class);

		ThemeDisplay themeDisplay = _mockThemeDisplay();

		Mockito.when(
			httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY)
		).thenReturn(
			themeDisplay
		);

		Mockito.when(
			portal.getHttpServletRequest(Matchers.any(PortletRequest.class))
		).thenReturn(
			httpServletRequest
		);

		portalUtil.setPortal(portal);
	}

	private DDMFormViewFormInstanceRecordsDisplayContext
		_ddmFormViewFormInstanceRecordsDisplayContext;

}