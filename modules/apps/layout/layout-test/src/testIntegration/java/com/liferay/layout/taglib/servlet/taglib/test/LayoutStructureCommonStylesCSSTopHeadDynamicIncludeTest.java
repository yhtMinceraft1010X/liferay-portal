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

package com.liferay.layout.taglib.servlet.taglib.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.util.PropsUtil;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Víctor Galán
 */
@RunWith(Arquillian.class)
public class LayoutStructureCommonStylesCSSTopHeadDynamicIncludeTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_originalFeatureFlagLps132571 = GetterUtil.getBoolean(
			PropsUtil.get("feature.flag.LPS-132571"));

		PropsUtil.set("feature.flag.LPS-132571", "true");

		_group = GroupTestUtil.addGroup();

		_layout = LayoutTestUtil.addTypeContentLayout(_group);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				TestPropsValues.getGroupId(), TestPropsValues.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);
	}

	@After
	public void tearDown() {
		PropsUtil.set(
			"feature.flag.LPS-132571",
			String.valueOf(_originalFeatureFlagLps132571));

		ServiceContextThreadLocal.popServiceContext();
	}

	@Test
	public void testDoesNotRender() throws Exception {
		_layoutPageTemplateStructureLocalService.
			updateLayoutPageTemplateStructureData(
				_group.getGroupId(), _layout.getPlid(),
				_read("layout_structure_container_fixed.json"));

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_dynamicInclude.include(
			_getHttpServletRequest(), mockHttpServletResponse,
			RandomTestUtil.randomString());

		Document document = Jsoup.parse(
			mockHttpServletResponse.getContentAsString());

		Element element = document.getElementById("layout-common-styles");

		Assert.assertEquals(
			_normalize(element.html()),
			_normalize(_read("expected_style_container_fixed.css")));
	}

	@Test
	public void testRenderCommonStyles() throws Exception {
		_layoutPageTemplateStructureLocalService.
			updateLayoutPageTemplateStructureData(
				_group.getGroupId(), _layout.getPlid(),
				_read("layout_structure.json"));

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_dynamicInclude.include(
			_getHttpServletRequest(), mockHttpServletResponse,
			RandomTestUtil.randomString());

		Document document = Jsoup.parse(
			mockHttpServletResponse.getContentAsString());

		Element element = document.getElementById("layout-common-styles");

		Assert.assertEquals(
			_normalize(element.html()),
			_normalize(_read("expected_style.css")));
	}

	@Test
	public void testRenderCommonStylesWithResponsive() throws Exception {
		_layoutPageTemplateStructureLocalService.
			updateLayoutPageTemplateStructureData(
				_group.getGroupId(), _layout.getPlid(),
				_read("layout_structure_with_responsive_styles.json"));

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_dynamicInclude.include(
			_getHttpServletRequest(), mockHttpServletResponse,
			RandomTestUtil.randomString());

		Document document = Jsoup.parse(
			mockHttpServletResponse.getContentAsString());

		Element element = document.getElementById("layout-common-styles");

		Assert.assertEquals(
			_normalize(element.html()),
			_normalize(_read("expected_style_with_responsive_styles.css")));
	}

	@Test
	public void testRenderEmptyTagWhenItDoesNotHaveStyles() throws Exception {
		_layoutPageTemplateStructureLocalService.
			updateLayoutPageTemplateStructureData(
				_group.getGroupId(), _layout.getPlid(),
				_read("layout_structure_without_styles.json"));

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_dynamicInclude.include(
			_getHttpServletRequest(), mockHttpServletResponse,
			RandomTestUtil.randomString());

		Document document = Jsoup.parse(
			mockHttpServletResponse.getContentAsString());

		Element element = document.getElementById("layout-common-styles");

		Assert.assertEquals(_normalize(element.html()), StringPool.BLANK);
	}

	private HttpServletRequest _getHttpServletRequest() throws Exception {
		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		ThemeDisplay themeDisplay = _getThemeDisplay();

		themeDisplay.setRequest(mockHttpServletRequest);

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, themeDisplay);

		return mockHttpServletRequest;
	}

	private ThemeDisplay _getThemeDisplay() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		themeDisplay.setCompany(company);

		themeDisplay.setLanguageId(_group.getDefaultLanguageId());
		themeDisplay.setLayout(_layout);
		themeDisplay.setLocale(
			LocaleUtil.fromLanguageId(_group.getDefaultLanguageId()));
		themeDisplay.setPlid(_layout.getPlid());
		themeDisplay.setScopeGroupId(_group.getGroupId());
		themeDisplay.setSiteGroupId(_group.getGroupId());

		return themeDisplay;
	}

	private String _normalize(String value) {
		return value.replaceAll("[\n\t ]", StringPool.BLANK);
	}

	private String _read(String fileName) throws Exception {
		Class<?> clazz = getClass();

		return StringUtil.read(
			clazz.getClassLoader(),
			"com/liferay/layout/taglib/servlet/taglib/test/dependencies/" +
				fileName);
	}

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject(
		filter = "component.name=com.liferay.layout.taglib.internal.servlet.taglib.LayoutStructureCommonStylesCSSTopHeadDynamicInclude"
	)
	private DynamicInclude _dynamicInclude;

	@DeleteAfterTestRun
	private Group _group;

	private Layout _layout;

	@Inject
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	private boolean _originalFeatureFlagLps132571;

}