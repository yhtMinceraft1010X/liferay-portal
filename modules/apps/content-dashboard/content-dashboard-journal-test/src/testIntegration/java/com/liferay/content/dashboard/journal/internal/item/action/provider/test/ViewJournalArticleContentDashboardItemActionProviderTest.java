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

package com.liferay.content.dashboard.journal.internal.item.action.provider.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.display.page.constants.AssetDisplayPageConstants;
import com.liferay.asset.display.page.service.AssetDisplayPageEntryLocalService;
import com.liferay.content.dashboard.item.action.ContentDashboardItemAction;
import com.liferay.content.dashboard.item.action.provider.ContentDashboardItemActionProvider;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.info.item.InfoItemReference;
import com.liferay.journal.constants.JournalArticleConstants;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.layout.display.page.LayoutDisplayPageProvider;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.impl.LayoutSetImpl;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Cristina González
 */
@RunWith(Arquillian.class)
public class ViewJournalArticleContentDashboardItemActionProviderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup(
			TestPropsValues.getCompanyId(), TestPropsValues.getUserId(), 0);
	}

	@Test
	public void testGetContentDashboardItemAction() throws Exception {
		try {
			JournalArticle journalArticle = JournalTestUtil.addArticle(
				_group.getGroupId(), 0);

			DDMStructure ddmStructure = journalArticle.getDDMStructure();

			ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(_group.getGroupId());

			LayoutPageTemplateEntry layoutPageTemplateEntry =
				_layoutPageTemplateEntryLocalService.addLayoutPageTemplateEntry(
					_group.getCreatorUserId(), _group.getGroupId(), 0,
					_portal.getClassNameId(JournalArticle.class.getName()),
					ddmStructure.getStructureId(),
					RandomTestUtil.randomString(),
					LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE, 0,
					true, 0, 0, 0, 0, serviceContext);

			_assetDisplayPageEntryLocalService.addAssetDisplayPageEntry(
				journalArticle.getUserId(), _group.getGroupId(),
				_portal.getClassNameId(JournalArticle.class.getName()),
				journalArticle.getResourcePrimKey(),
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
				AssetDisplayPageConstants.TYPE_DEFAULT, serviceContext);

			ThemeDisplay themeDisplay = _getThemeDisplay(LocaleUtil.US);

			themeDisplay.setCompany(
				_companyLocalService.fetchCompany(
					TestPropsValues.getCompanyId()));

			LayoutSetImpl layoutSetImpl = new LayoutSetImpl();

			layoutSetImpl.setCompanyFallbackVirtualHostname(null);

			themeDisplay.setLayoutSet(layoutSetImpl);

			MockHttpServletRequest mockHttpServletRequest =
				new MockHttpServletRequest();

			mockHttpServletRequest.setAttribute(
				WebKeys.THEME_DISPLAY, themeDisplay);
			mockHttpServletRequest.setAttribute(
				"LAYOUT_DISPLAY_PAGE_OBJECT_PROVIDER",
				_layoutDisplayPageProvider.getLayoutDisplayPageObjectProvider(
					new InfoItemReference(
						JournalArticle.class.getName(),
						journalArticle.getResourcePrimKey())));

			themeDisplay.setRequest(mockHttpServletRequest);

			themeDisplay.setURLCurrent("http://localhost:8080/currentURL");

			serviceContext.setRequest(mockHttpServletRequest);

			ServiceContextThreadLocal.pushServiceContext(serviceContext);

			ContentDashboardItemAction contentDashboardItemAction =
				_contentDashboardItemActionProvider.
					getContentDashboardItemAction(
						journalArticle, mockHttpServletRequest);

			String url = contentDashboardItemAction.getURL();

			Assert.assertTrue(
				url.contains(
					StringUtil.toLowerCase(
						journalArticle.getTitle(LocaleUtil.US))));

			String escapeURL = HtmlUtil.escapeURL(
				"http://localhost:8080/currentURL");

			Assert.assertTrue(url.contains("p_l_back_url=" + escapeURL));
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test
	public void testGetContentDashboardItemActionWithoutDisplayPage()
		throws Exception {

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(), 0);

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay(LocaleUtil.US));

		Assert.assertNull(
			_contentDashboardItemActionProvider.getContentDashboardItemAction(
				journalArticle, mockHttpServletRequest));
	}

	@Test
	public void testGetJournalArticleTranslationSpanishURL() throws Exception {
		try {
			ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(_group.getGroupId());

			JournalArticle journalArticle = JournalTestUtil.addArticle(
				_group.getGroupId(), 0,
				JournalArticleConstants.CLASS_NAME_ID_DEFAULT, "title1",
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.getSiteDefault(), false, false, serviceContext);

			DDMStructure ddmStructure = journalArticle.getDDMStructure();

			LayoutPageTemplateEntry layoutPageTemplateEntry =
				_layoutPageTemplateEntryLocalService.addLayoutPageTemplateEntry(
					_group.getCreatorUserId(), _group.getGroupId(), 0,
					_portal.getClassNameId(JournalArticle.class.getName()),
					ddmStructure.getStructureId(),
					RandomTestUtil.randomString(),
					LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE, 0,
					true, 0, 0, 0, 0, serviceContext);

			_assetDisplayPageEntryLocalService.addAssetDisplayPageEntry(
				journalArticle.getUserId(), _group.getGroupId(),
				_portal.getClassNameId(JournalArticle.class.getName()),
				journalArticle.getResourcePrimKey(),
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
				AssetDisplayPageConstants.TYPE_DEFAULT, serviceContext);

			ThemeDisplay themeDisplay = _getThemeDisplay(LocaleUtil.US);

			themeDisplay.setCompany(
				_companyLocalService.fetchCompany(
					TestPropsValues.getCompanyId()));

			LayoutSetImpl layoutSetImpl = new LayoutSetImpl();

			layoutSetImpl.setCompanyFallbackVirtualHostname(null);

			themeDisplay.setLayoutSet(layoutSetImpl);

			MockHttpServletRequest mockHttpServletRequest =
				new MockHttpServletRequest();

			mockHttpServletRequest.setAttribute(
				WebKeys.THEME_DISPLAY, themeDisplay);
			mockHttpServletRequest.setAttribute(
				"LAYOUT_DISPLAY_PAGE_OBJECT_PROVIDER",
				_layoutDisplayPageProvider.getLayoutDisplayPageObjectProvider(
					new InfoItemReference(
						JournalArticle.class.getName(),
						journalArticle.getResourcePrimKey())));

			themeDisplay.setRequest(mockHttpServletRequest);

			serviceContext.setRequest(mockHttpServletRequest);

			ServiceContextThreadLocal.pushServiceContext(serviceContext);

			ContentDashboardItemAction contentDashboardItemAction =
				_contentDashboardItemActionProvider.
					getContentDashboardItemAction(
						journalArticle, mockHttpServletRequest);

			String spainUrl = contentDashboardItemAction.getURL(
				LocaleUtil.SPAIN);

			Assert.assertTrue(
				spainUrl.contains(
					StringUtil.toLowerCase(
						journalArticle.getTitle(LocaleUtil.US))));

			Assert.assertTrue(
				spainUrl.contains(StringUtil.toLowerCase("/es/")));
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test
	public void testGetKey() {
		Assert.assertEquals(
			"view", _contentDashboardItemActionProvider.getKey());
	}

	@Test
	public void testGetType() {
		Assert.assertEquals(
			ContentDashboardItemAction.Type.VIEW,
			_contentDashboardItemActionProvider.getType());
	}

	@Test
	public void testIsShow() throws Exception {
		try {
			JournalArticle journalArticle = JournalTestUtil.addArticle(
				_group.getGroupId(), 0);

			DDMStructure ddmStructure = journalArticle.getDDMStructure();

			ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(_group.getGroupId());

			LayoutPageTemplateEntry layoutPageTemplateEntry =
				_layoutPageTemplateEntryLocalService.addLayoutPageTemplateEntry(
					_group.getCreatorUserId(), _group.getGroupId(), 0,
					_portal.getClassNameId(JournalArticle.class.getName()),
					ddmStructure.getStructureId(),
					RandomTestUtil.randomString(),
					LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE, 0,
					true, 0, 0, 0, 0, serviceContext);

			_assetDisplayPageEntryLocalService.addAssetDisplayPageEntry(
				journalArticle.getUserId(), _group.getGroupId(),
				_portal.getClassNameId(JournalArticle.class.getName()),
				journalArticle.getResourcePrimKey(),
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
				AssetDisplayPageConstants.TYPE_DEFAULT, serviceContext);

			ThemeDisplay themeDisplay = _getThemeDisplay(LocaleUtil.US);

			themeDisplay.setCompany(
				_companyLocalService.fetchCompany(
					TestPropsValues.getCompanyId()));
			themeDisplay.setLayoutSet(new LayoutSetImpl());

			MockHttpServletRequest mockHttpServletRequest =
				new MockHttpServletRequest();

			mockHttpServletRequest.setAttribute(
				WebKeys.THEME_DISPLAY, themeDisplay);
			mockHttpServletRequest.setAttribute(
				"LAYOUT_DISPLAY_PAGE_OBJECT_PROVIDER",
				_layoutDisplayPageProvider.getLayoutDisplayPageObjectProvider(
					new InfoItemReference(
						JournalArticle.class.getName(),
						journalArticle.getResourcePrimKey())));

			themeDisplay.setRequest(mockHttpServletRequest);

			themeDisplay.setURLCurrent("http://localhost:8080/currentURL");

			serviceContext.setRequest(mockHttpServletRequest);

			ServiceContextThreadLocal.pushServiceContext(serviceContext);

			mockHttpServletRequest.setAttribute(
				WebKeys.THEME_DISPLAY, themeDisplay);

			Assert.assertTrue(
				_contentDashboardItemActionProvider.isShow(
					journalArticle, mockHttpServletRequest));
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test
	public void testIsShowWithoutDisplayPage() throws Exception {
		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(), 0);

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay(LocaleUtil.US));

		Assert.assertTrue(
			!_contentDashboardItemActionProvider.isShow(
				journalArticle, mockHttpServletRequest));
	}

	private ThemeDisplay _getThemeDisplay(Locale locale) {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setLocale(locale);
		themeDisplay.setSiteGroupId(_group.getGroupId());

		return themeDisplay;
	}

	@Inject
	private AssetDisplayPageEntryLocalService
		_assetDisplayPageEntryLocalService;

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject(
		filter = "component.name=com.liferay.content.dashboard.journal.internal.item.action.provider.ViewJournalArticleContentDashboardItemActionProvider"
	)
	private ContentDashboardItemActionProvider
		_contentDashboardItemActionProvider;

	@DeleteAfterTestRun
	private Group _group;

	@Inject(filter = "component.name=*.JournalArticleLayoutDisplayPageProvider")
	private LayoutDisplayPageProvider<JournalArticle>
		_layoutDisplayPageProvider;

	@Inject
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Inject
	private Portal _portal;

}