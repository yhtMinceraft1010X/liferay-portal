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

package com.liferay.layout.reports.web.internal.portlet.action.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.info.constants.InfoDisplayWebKeys;
import com.liferay.info.field.InfoField;
import com.liferay.info.field.InfoFieldValue;
import com.liferay.info.field.type.TextInfoFieldType;
import com.liferay.info.item.InfoItemClassDetails;
import com.liferay.info.item.InfoItemDetails;
import com.liferay.info.item.InfoItemFieldValues;
import com.liferay.info.item.provider.InfoItemFieldValuesProvider;
import com.liferay.layout.reports.web.internal.util.LayoutReportsTestUtil;
import com.liferay.layout.seo.service.LayoutSEOEntryLocalService;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.test.portlet.MockLiferayResourceRequest;
import com.liferay.portal.kernel.test.portlet.MockLiferayResourceResponse;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.io.ByteArrayOutputStream;

import java.util.Arrays;
import java.util.Locale;

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

/**
 * @author Cristina González
 * @author Alejandro Tardín
 */
@RunWith(Arquillian.class)
public class LayoutReportsDataMVCResourceCommandTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup(
			TestPropsValues.getCompanyId(), TestPropsValues.getUserId(), 0);
	}

	@Test
	public void testGetData() throws Exception {
		LayoutReportsTestUtil.
			withLayoutReportsGooglePageSpeedGroupConfiguration(
				RandomTestUtil.randomString(), true, _group.getGroupId(),
				() -> {
					Layout layout = LayoutTestUtil.addTypePortletLayout(
						_group.getGroupId());

					GroupTestUtil.updateDisplaySettings(
						_group.getGroupId(),
						Arrays.asList(LocaleUtil.BRAZIL, LocaleUtil.SPAIN),
						LocaleUtil.SPAIN);

					JSONObject jsonObject = _serveResource(layout);

					JSONArray pageURLsJSONArray = jsonObject.getJSONArray(
						"pageURLs");

					Assert.assertEquals(
						String.valueOf(pageURLsJSONArray), 2,
						pageURLsJSONArray.length());

					JSONObject pageURLJSONObject1 =
						pageURLsJSONArray.getJSONObject(0);

					String imagesPath = jsonObject.getString("imagesPath");

					Assert.assertTrue(imagesPath.contains("images"));

					Assert.assertEquals(
						LocaleUtil.toW3cLanguageId(LocaleUtil.SPAIN),
						pageURLJSONObject1.getString("languageId"));
					Assert.assertEquals(
						layout.getName(LocaleUtil.SPAIN),
						pageURLJSONObject1.getString("title"));

					JSONObject pageURLJSONObject2 =
						pageURLsJSONArray.getJSONObject(1);

					Assert.assertEquals(
						LocaleUtil.toW3cLanguageId(LocaleUtil.BRAZIL),
						pageURLJSONObject2.getString("languageId"));

					Assert.assertEquals(
						layout.getName(LocaleUtil.BRAZIL),
						pageURLJSONObject1.getString("title"));

					String configureGooglePageSpeedURL = jsonObject.getString(
						"configureGooglePageSpeedURL");

					Assert.assertTrue(
						configureGooglePageSpeedURL.contains(
							"configuration_admin"));

					Assert.assertEquals(
						LocaleUtil.toW3cLanguageId(LocaleUtil.SPAIN),
						jsonObject.getString("defaultLanguageId"));

					Assert.assertTrue(jsonObject.getBoolean("validConnection"));
				});
	}

	@Test
	public void testGetDataWithApiKeyInSiteConfiguration() throws Exception {
		LayoutReportsTestUtil.
			withLayoutReportsGooglePageSpeedGroupConfiguration(
				RandomTestUtil.randomString(), true, _group.getGroupId(),
				() -> {
					Layout layout = LayoutTestUtil.addTypePortletLayout(
						_group.getGroupId());

					JSONObject jsonObject = _serveResource(layout);

					Assert.assertTrue(jsonObject.getBoolean("validConnection"));
				});
	}

	@Test
	public void testGetDataWithLayoutTypeAssetDisplay() throws Exception {
		LayoutReportsTestUtil.
			withLayoutReportsGooglePageSpeedGroupConfiguration(
				RandomTestUtil.randomString(), true, _group.getGroupId(),
				() -> {
					Bundle bundle = FrameworkUtil.getBundle(
						LayoutReportsDataMVCResourceCommandTest.class);

					BundleContext bundleContext = bundle.getBundleContext();

					ServiceRegistration<InfoItemFieldValuesProvider<?>>
						infoItemFieldValuesProviderServiceRegistration =
							bundleContext.registerService(
								(Class<InfoItemFieldValuesProvider<?>>)
									(Class<?>)InfoItemFieldValuesProvider.class,
								new MockInfoItemFieldValuesProvider(),
								new HashMapDictionary<>());

					try {
						Layout layout = LayoutTestUtil.addTypePortletLayout(
							_group);

						layout.setType(LayoutConstants.TYPE_ASSET_DISPLAY);

						layout = _layoutLocalService.updateLayout(layout);

						GroupTestUtil.updateDisplaySettings(
							_group.getGroupId(),
							Arrays.asList(LocaleUtil.BRAZIL, LocaleUtil.SPAIN),
							LocaleUtil.SPAIN);

						InfoItemClassDetails infoItemClassDetails =
							new InfoItemClassDetails(
								MockObject.class.getName());

						InfoItemDetails infoItemDetails = new InfoItemDetails(
							infoItemClassDetails, null);

						JSONObject jsonObject = _serveResource(
							layout,
							new ObjectValuePair[] {
								new ObjectValuePair<>(
									InfoDisplayWebKeys.INFO_ITEM_DETAILS,
									infoItemDetails),
								new ObjectValuePair<>(
									InfoDisplayWebKeys.INFO_ITEM,
									new MockObject())
							});

						JSONArray pageURLsJSONArray = jsonObject.getJSONArray(
							"pageURLs");

						Assert.assertEquals(
							String.valueOf(pageURLsJSONArray), 2,
							pageURLsJSONArray.length());

						JSONObject pageURLJSONObject1 =
							pageURLsJSONArray.getJSONObject(0);

						Assert.assertEquals(
							LocaleUtil.toW3cLanguageId(LocaleUtil.SPAIN),
							pageURLJSONObject1.get("languageId"));
						Assert.assertEquals(
							"defaultMappedTitle",
							pageURLJSONObject1.get("title"));

						JSONObject pageURLJSONObject2 =
							pageURLsJSONArray.getJSONObject(1);

						String imagesPath = jsonObject.getString("imagesPath");

						Assert.assertTrue(imagesPath.contains("images"));

						Assert.assertEquals(
							LocaleUtil.toW3cLanguageId(LocaleUtil.BRAZIL),
							pageURLJSONObject2.getString("languageId"));
						Assert.assertEquals(
							"defaultMappedTitle",
							pageURLJSONObject2.getString("title"));

						String configureGooglePageSpeedURL =
							jsonObject.getString("configureGooglePageSpeedURL");

						Assert.assertTrue(
							configureGooglePageSpeedURL.contains(
								"configuration_admin"));

						Assert.assertEquals(
							LocaleUtil.toW3cLanguageId(LocaleUtil.SPAIN),
							jsonObject.getString("defaultLanguageId"));

						Assert.assertTrue(
							jsonObject.getBoolean("validConnection"));
					}
					finally {
						infoItemFieldValuesProviderServiceRegistration.
							unregister();
					}
				});
	}

	@Test
	public void testGetDataWithLocalizedCanonicalURL() throws Exception {
		LayoutReportsTestUtil.
			withLayoutReportsGooglePageSpeedGroupConfiguration(
				RandomTestUtil.randomString(), true, _group.getGroupId(),
				() -> {
					GroupTestUtil.updateDisplaySettings(
						_group.getGroupId(),
						Arrays.asList(LocaleUtil.US, LocaleUtil.SPAIN),
						LocaleUtil.US);

					Layout layout = LayoutTestUtil.addTypePortletLayout(
						_group.getGroupId());

					_layoutSEOEntryLocalService.updateLayoutSEOEntry(
						TestPropsValues.getUserId(), _group.getGroupId(),
						layout.isPrivateLayout(), layout.getLayoutId(), true,
						HashMapBuilder.put(
							LocaleUtil.SPAIN, "https://liferay.com"
						).build(),
						ServiceContextTestUtil.getServiceContext(
							_group.getGroupId()));

					JSONObject jsonObject = _serveResource(layout);

					JSONArray pageURLsJSONArray = jsonObject.getJSONArray(
						"pageURLs");

					JSONObject pageURLJSONObject =
						pageURLsJSONArray.getJSONObject(1);

					Assert.assertEquals(
						LocaleUtil.toW3cLanguageId(LocaleUtil.SPAIN),
						pageURLJSONObject.getString("languageId"));
					Assert.assertEquals(
						layout.getName(LocaleUtil.SPAIN),
						pageURLJSONObject.getString("title"));
					Assert.assertEquals(
						"https://liferay.com",
						pageURLJSONObject.getString("url"));
				});
	}

	@Test
	public void testGetDataWithoutApiKey() throws Exception {
		LayoutReportsTestUtil.
			withLayoutReportsGooglePageSpeedGroupConfiguration(
				StringPool.BLANK, true, _group.getGroupId(),
				() -> {
					Layout layout = LayoutTestUtil.addTypePortletLayout(
						_group.getGroupId());

					JSONObject jsonObject = _serveResource(layout);

					Assert.assertFalse(
						jsonObject.getBoolean("validConnection"));
				});
	}

	@Test
	public void testLanguagesAlphabeticallySorted() throws Exception {
		LayoutReportsTestUtil.
			withLayoutReportsGooglePageSpeedGroupConfiguration(
				RandomTestUtil.randomString(), true, _group.getGroupId(),
				() -> {
					Layout layout = LayoutTestUtil.addTypePortletLayout(
						_group.getGroupId());

					GroupTestUtil.updateDisplaySettings(
						_group.getGroupId(),
						Arrays.asList(
							LocaleUtil.BRAZIL, LocaleUtil.SPAIN, LocaleUtil.US),
						LocaleUtil.US);

					JSONObject jsonObject = _serveResource(layout);

					JSONArray pageURLsJSONArray = jsonObject.getJSONArray(
						"pageURLs");

					Assert.assertEquals(
						String.valueOf(pageURLsJSONArray), 3,
						pageURLsJSONArray.length());

					JSONObject englishJSONObject =
						pageURLsJSONArray.getJSONObject(0);

					Assert.assertEquals(
						"English (United States)",
						englishJSONObject.get("languageLabel"));

					JSONObject portugueseJSONObject =
						pageURLsJSONArray.getJSONObject(1);

					Assert.assertEquals(
						"Portuguese (Brazil)",
						portugueseJSONObject.get("languageLabel"));

					JSONObject spanishJSONObject =
						pageURLsJSONArray.getJSONObject(2);

					Assert.assertEquals(
						"Spanish (Spain)",
						spanishJSONObject.get("languageLabel"));
				});
	}

	private MockLiferayResourceRequest _getMockLiferayResourceRequest(
			Layout layout)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		MockLiferayResourceRequest mockLiferayPortletRenderRequest =
			new MockLiferayResourceRequest();

		serviceContext.setRequest(
			mockLiferayPortletRenderRequest.getHttpServletRequest());

		mockLiferayPortletRenderRequest.setParameter(
			"plid", String.valueOf(layout.getPlid()));

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(
			_companyLocalService.fetchCompany(TestPropsValues.getCompanyId()));
		themeDisplay.setLayoutSet(layout.getLayoutSet());
		themeDisplay.setLocale(LocaleUtil.getDefault());
		themeDisplay.setRequest(
			mockLiferayPortletRenderRequest.getHttpServletRequest());
		themeDisplay.setScopeGroupId(layout.getGroupId());
		themeDisplay.setSiteGroupId(layout.getGroupId());
		themeDisplay.setUser(TestPropsValues.getUser());

		mockLiferayPortletRenderRequest.setAttribute(
			WebKeys.THEME_DISPLAY, themeDisplay);

		return mockLiferayPortletRenderRequest;
	}

	private JSONObject _serveResource(
			Layout layout, ObjectValuePair<String, Object>... objectValuePairs)
		throws Exception {

		MockLiferayResourceRequest mockLiferayResourceRequest =
			_getMockLiferayResourceRequest(layout);

		for (ObjectValuePair<String, Object> objectValuePair :
				objectValuePairs) {

			mockLiferayResourceRequest.setAttribute(
				objectValuePair.getKey(), objectValuePair.getValue());
		}

		MockLiferayResourceResponse mockLiferayResourceResponse =
			new MockLiferayResourceResponse();

		Locale originalSiteDefaultLocale =
			LocaleThreadLocal.getSiteDefaultLocale();

		try {
			LocaleThreadLocal.setSiteDefaultLocale(
				_portal.getSiteDefaultLocale(_group.getGroupId()));

			_layoutReportsDataMVCResourceCommand.serveResource(
				mockLiferayResourceRequest, mockLiferayResourceResponse);
		}
		finally {
			LocaleThreadLocal.setSiteDefaultLocale(originalSiteDefaultLocale);
		}

		ByteArrayOutputStream byteArrayOutputStream =
			(ByteArrayOutputStream)
				mockLiferayResourceResponse.getPortletOutputStream();

		return JSONFactoryUtil.createJSONObject(
			new String(byteArrayOutputStream.toByteArray()));
	}

	@Inject
	private CompanyLocalService _companyLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private LayoutLocalService _layoutLocalService;

	@Inject(filter = "mvc.command.name=/layout_reports/data")
	private MVCResourceCommand _layoutReportsDataMVCResourceCommand;

	@Inject
	private LayoutSEOEntryLocalService _layoutSEOEntryLocalService;

	@Inject
	private Portal _portal;

	private static class MockInfoItemFieldValuesProvider
		implements InfoItemFieldValuesProvider<MockObject> {

		@Override
		public InfoItemFieldValues getInfoItemFieldValues(
			MockObject mockObject) {

			return InfoItemFieldValues.builder(
			).infoFieldValue(
				new InfoFieldValue<>(
					InfoField.builder(
					).infoFieldType(
						TextInfoFieldType.INSTANCE
					).namespace(
						StringPool.BLANK
					).name(
						"title"
					).build(),
					"defaultMappedTitle")
			).build();
		}

	}

	private static class MockObject {
	}

}