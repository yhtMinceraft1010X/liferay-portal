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

package com.liferay.site.navigation.menu.item.asset.vocabulary.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.props.test.util.PropsTemporarySwapper;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.site.navigation.constants.SiteNavigationConstants;
import com.liferay.site.navigation.menu.item.layout.constants.SiteNavigationMenuItemTypeConstants;
import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.model.SiteNavigationMenuItem;
import com.liferay.site.navigation.service.SiteNavigationMenuItemLocalService;
import com.liferay.site.navigation.service.SiteNavigationMenuLocalService;
import com.liferay.site.navigation.type.SiteNavigationMenuItemType;
import com.liferay.site.navigation.type.SiteNavigationMenuItemTypeRegistry;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Lourdes Fernández Besada
 */
@RunWith(Arquillian.class)
public class AssetVocabularySiteNavigationMenuItemTypeTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_serviceContext = ServiceContextTestUtil.getServiceContext(
			_group.getGroupId(), TestPropsValues.getUserId());

		_assetVocabulary = _assetVocabularyLocalService.addVocabulary(
			TestPropsValues.getUserId(), _group.getGroupId(),
			RandomTestUtil.randomString(), _serviceContext);
	}

	@Test
	public void testGetChildrenSiteNavigationMenuItems() throws Exception {
		_addAssetCategory(0);
		_addAssetCategory(0);

		AssetCategory rootAssetCategory = _addAssetCategory(0);

		_addAssetCategory(rootAssetCategory.getCategoryId());
		_addAssetCategory(rootAssetCategory.getCategoryId());

		AssetCategory firstLevelAssetCategory = _addAssetCategory(
			rootAssetCategory.getCategoryId());

		_addAssetCategory(firstLevelAssetCategory.getCategoryId());
		_addAssetCategory(firstLevelAssetCategory.getCategoryId());

		AssetCategory secondLevelAssetCategory = _addAssetCategory(
			firstLevelAssetCategory.getCategoryId());

		_addAssetCategory(secondLevelAssetCategory.getCategoryId());
		_addAssetCategory(secondLevelAssetCategory.getCategoryId());

		AssetCategory thirdLevelAssetCategory = _addAssetCategory(
			secondLevelAssetCategory.getCategoryId());

		_addAssetCategory(thirdLevelAssetCategory.getCategoryId());
		_addAssetCategory(thirdLevelAssetCategory.getCategoryId());
		_addAssetCategory(thirdLevelAssetCategory.getCategoryId());

		Assert.assertEquals(
			15,
			_assetCategoryLocalService.getVocabularyCategoriesCount(
				_assetVocabulary.getVocabularyId()));
		Assert.assertEquals(
			3,
			_assetCategoryLocalService.getChildCategoriesCount(
				rootAssetCategory.getCategoryId()));
		Assert.assertEquals(
			3,
			_assetCategoryLocalService.getChildCategoriesCount(
				firstLevelAssetCategory.getCategoryId()));
		Assert.assertEquals(
			3,
			_assetCategoryLocalService.getChildCategoriesCount(
				secondLevelAssetCategory.getCategoryId()));
		Assert.assertEquals(
			3,
			_assetCategoryLocalService.getChildCategoriesCount(
				thirdLevelAssetCategory.getCategoryId()));

		Locale locale = _portal.getSiteDefaultLocale(_group.getGroupId());

		_assertGetChildrenSiteNavigationMenuItems(
			locale, 0, _addSiteNavigationMenuItem(locale, "{}", false));
	}

	@Test
	public void testGetChildrenSiteNavigationMenuItemsEmptyAssetVocabulary()
		throws Exception {

		Assert.assertEquals(
			0,
			_assetCategoryLocalService.getVocabularyCategoriesCount(
				_assetVocabulary.getVocabularyId()));

		Locale locale = _portal.getSiteDefaultLocale(_group.getGroupId());

		_assertGetChildrenSiteNavigationMenuItems(
			locale, 0, _addSiteNavigationMenuItem(locale, "{}", false));
	}

	@Test
	public void testGetRegularURLAssetCategoryTypeWithDisplayPageTemplate()
		throws Exception {

		_layoutPageTemplateEntryLocalService.addLayoutPageTemplateEntry(
			_group.getCreatorUserId(), _group.getGroupId(), 0,
			_portal.getClassNameId(AssetCategory.class.getName()), 0,
			RandomTestUtil.randomString(),
			LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE, 0, true, 0,
			0, 0, 0, _serviceContext);

		AssetCategory assetCategory = _addAssetCategory(0);

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		ThemeDisplay themeDisplay = _getThemeDisplay();

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, themeDisplay);

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		SiteNavigationMenuItem assetCategorySiteNavigationMenuItem =
			_getAssetCategorySiteNavigationMenuItem(
				assetCategory, mockHttpServletRequest,
				_portal.getSiteDefaultLocale(_group.getGroupId()),
				siteNavigationMenuItemType);

		Assert.assertEquals(
			_assetDisplayPageFriendlyURLProvider.getFriendlyURL(
				AssetCategory.class.getName(), assetCategory.getCategoryId(),
				themeDisplay),
			siteNavigationMenuItemType.getRegularURL(
				mockHttpServletRequest, assetCategorySiteNavigationMenuItem));
	}

	@Test
	public void testGetRegularURLAssetCategoryTypeWithoutDisplayPageTemplate()
		throws Exception {

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		Assert.assertEquals(
			StringPool.BLANK,
			siteNavigationMenuItemType.getRegularURL(
				mockHttpServletRequest,
				_getAssetCategorySiteNavigationMenuItem(
					mockHttpServletRequest,
					_portal.getSiteDefaultLocale(_group.getGroupId()),
					siteNavigationMenuItemType)));
	}

	@Test
	public void testGetRegularURLAssetVocabularyType() throws Exception {
		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		Assert.assertEquals(
			StringPool.BLANK,
			siteNavigationMenuItemType.getRegularURL(
				mockHttpServletRequest,
				_addSiteNavigationMenuItem(
					_portal.getSiteDefaultLocale(_group.getGroupId()), "{}",
					false)));
	}

	@Test
	public void testGetSiteNavigationMenuItemsAssetCategoryType()
		throws Exception {

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		SiteNavigationMenuItem assetCategorySiteNavigationMenuItem =
			_getAssetCategorySiteNavigationMenuItem(
				mockHttpServletRequest,
				_portal.getSiteDefaultLocale(_group.getGroupId()),
				siteNavigationMenuItemType);

		List<SiteNavigationMenuItem> siteNavigationMenuItems =
			siteNavigationMenuItemType.getSiteNavigationMenuItems(
				mockHttpServletRequest, assetCategorySiteNavigationMenuItem);

		Assert.assertEquals(
			siteNavigationMenuItems.toString(), 1,
			siteNavigationMenuItems.size());

		Assert.assertEquals(
			assetCategorySiteNavigationMenuItem,
			siteNavigationMenuItems.get(0));
	}

	@Test
	public void testGetSiteNavigationMenuItemsAssetVocabularyTypeShowAssetVocabularyLevelDisabled()
		throws Exception {

		_addAssetCategory(0);
		_addAssetCategory(0);
		_addAssetCategory(0);

		List<AssetCategory> assetCategories =
			_assetCategoryLocalService.getVocabularyCategories(
				0, _assetVocabulary.getVocabularyId(), QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null);

		Assert.assertEquals(
			assetCategories.toString(), 3, assetCategories.size());

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		Locale locale = _portal.getSiteDefaultLocale(_group.getGroupId());

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		SiteNavigationMenuItem assetVocabularySiteNavigationMenuItem =
			_addSiteNavigationMenuItem(locale, "{}", false);

		List<SiteNavigationMenuItem> siteNavigationMenuItems =
			siteNavigationMenuItemType.getSiteNavigationMenuItems(
				mockHttpServletRequest, assetVocabularySiteNavigationMenuItem);

		Assert.assertEquals(
			siteNavigationMenuItems.toString(), 3,
			siteNavigationMenuItems.size());

		for (int i = 0; i < assetCategories.size(); i++) {
			AssetCategory assetCategory = assetCategories.get(i);

			SiteNavigationMenuItem siteNavigationMenuItem =
				siteNavigationMenuItems.get(i);

			_assertAssetCategorySiteNavigationMenuItem(
				assetCategory, locale, siteNavigationMenuItem);
		}
	}

	@Test
	public void testGetSiteNavigationMenuItemsAssetVocabularyTypeShowAssetVocabularyLevelEnabled()
		throws Exception {

		_addAssetCategory(0);
		_addAssetCategory(0);
		_addAssetCategory(0);

		Assert.assertEquals(
			3,
			_assetCategoryLocalService.getVocabularyCategoriesCount(
				_assetVocabulary.getVocabularyId()));

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		SiteNavigationMenuItem assetVocabularySiteNavigationMenuItem =
			_addSiteNavigationMenuItem(
				_portal.getSiteDefaultLocale(_group.getGroupId()), "{}", true);

		List<SiteNavigationMenuItem> siteNavigationMenuItems =
			siteNavigationMenuItemType.getSiteNavigationMenuItems(
				mockHttpServletRequest, assetVocabularySiteNavigationMenuItem);

		Assert.assertEquals(
			siteNavigationMenuItems.toString(), 1,
			siteNavigationMenuItems.size());

		Assert.assertEquals(
			assetVocabularySiteNavigationMenuItem,
			siteNavigationMenuItems.get(0));
	}

	@Test
	public void testGetStatusIconEmptyAssetVocabulary() throws Exception {
		Assert.assertEquals(
			0,
			_assetCategoryLocalService.getVocabularyCategoriesCount(
				_assetVocabulary.getVocabularyId()));

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		Assert.assertEquals(
			"warning-full",
			siteNavigationMenuItemType.getStatusIcon(
				_addSiteNavigationMenuItem(
					_portal.getSiteDefaultLocale(_group.getGroupId()), "{}",
					false)));
	}

	@Test
	public void testGetStatusIconNotEmptyAssetVocabulary() throws Exception {
		_addAssetCategory(0);

		Assert.assertEquals(
			1,
			_assetCategoryLocalService.getVocabularyCategoriesCount(
				_assetVocabulary.getVocabularyId()));

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		Assert.assertEquals(
			StringPool.BLANK,
			siteNavigationMenuItemType.getStatusIcon(
				_addSiteNavigationMenuItem(
					_portal.getSiteDefaultLocale(_group.getGroupId()), "{}",
					false)));
	}

	@Test
	public void testGetTitleAssetCategoryType() throws Exception {
		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());

		AssetCategory assetCategory = _addAssetCategory(0);

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		Locale locale = _portal.getSiteDefaultLocale(_group.getGroupId());

		SiteNavigationMenuItem assetCategorySiteNavigationMenuItem =
			_getAssetCategorySiteNavigationMenuItem(
				assetCategory, mockHttpServletRequest, locale,
				siteNavigationMenuItemType);

		Assert.assertEquals(
			assetCategory.getTitle(locale),
			siteNavigationMenuItemType.getTitle(
				assetCategorySiteNavigationMenuItem, locale));
	}

	@Test
	public void testGetTitleAssetVocabularyType() throws Exception {
		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		Locale locale = _portal.getSiteDefaultLocale(_group.getGroupId());

		SiteNavigationMenuItem assetVocabularySiteNavigationMenuItem =
			_addSiteNavigationMenuItem(locale, "{}", false);

		Assert.assertEquals(
			_assetVocabulary.getTitle(locale),
			siteNavigationMenuItemType.getTitle(
				assetVocabularySiteNavigationMenuItem, locale));
	}

	@Test
	public void testGetTitleAssetVocabularyTypeUseCustomName()
		throws Exception {

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		Locale locale = _portal.getSiteDefaultLocale(_group.getGroupId());

		String expectedTitle = RandomTestUtil.randomString();

		SiteNavigationMenuItem assetVocabularySiteNavigationMenuItem =
			_addSiteNavigationMenuItem(
				locale,
				JSONUtil.put(
					LocaleUtil.toLanguageId(locale), expectedTitle
				).toString(),
				false);

		Assert.assertEquals(
			expectedTitle,
			siteNavigationMenuItemType.getTitle(
				assetVocabularySiteNavigationMenuItem, locale));
	}

	@Test
	public void testGetTitleAssetVocabularyTypeUseCustomNameNondefaultLocale()
		throws Exception {

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		Locale locale = _portal.getSiteDefaultLocale(_group.getGroupId());

		String expectedTitle = RandomTestUtil.randomString();

		SiteNavigationMenuItem assetVocabularySiteNavigationMenuItem =
			_addSiteNavigationMenuItem(
				locale,
				JSONUtil.put(
					LocaleUtil.toLanguageId(locale),
					RandomTestUtil.randomString()
				).put(
					LocaleUtil.toLanguageId(LocaleUtil.SPAIN), expectedTitle
				).toString(),
				false);

		Assert.assertEquals(
			expectedTitle,
			siteNavigationMenuItemType.getTitle(
				assetVocabularySiteNavigationMenuItem, LocaleUtil.SPAIN));
	}

	@Test
	public void testGetTitleAssetVocabularyTypeUseCustomNameNontranslatedLocale()
		throws Exception {

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		Locale locale = _portal.getSiteDefaultLocale(_group.getGroupId());

		String expectedTitle = RandomTestUtil.randomString();

		SiteNavigationMenuItem assetVocabularySiteNavigationMenuItem =
			_addSiteNavigationMenuItem(
				locale,
				JSONUtil.put(
					LocaleUtil.toLanguageId(locale), expectedTitle
				).toString(),
				false);

		Assert.assertEquals(
			expectedTitle,
			siteNavigationMenuItemType.getTitle(
				assetVocabularySiteNavigationMenuItem, LocaleUtil.SPAIN));
	}

	@Test
	public void testIsAvailableWhenFeatureFlagDisabled() {
		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		try (PropsTemporarySwapper propsTemporarySwapper =
				new PropsTemporarySwapper("feature.flag.LPS-146502", "false")) {

			Assert.assertFalse(siteNavigationMenuItemType.isAvailable(null));
		}
	}

	@Test
	public void testIsAvailableWhenFeatureFlagEnabled() {
		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		try (PropsTemporarySwapper propsTemporarySwapper =
				new PropsTemporarySwapper("feature.flag.LPS-146502", "true")) {

			Assert.assertTrue(siteNavigationMenuItemType.isAvailable(null));
		}
	}

	@Test
	public void testIsBrowsableAssetCategoryTypeWithDisplayPageTemplate()
		throws Exception {

		_layoutPageTemplateEntryLocalService.addLayoutPageTemplateEntry(
			_group.getCreatorUserId(), _group.getGroupId(), 0,
			_portal.getClassNameId(AssetCategory.class.getName()), 0,
			RandomTestUtil.randomString(),
			LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE, 0, true, 0,
			0, 0, 0, _serviceContext);

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		Assert.assertTrue(
			siteNavigationMenuItemType.isBrowsable(
				_getAssetCategorySiteNavigationMenuItem(
					mockHttpServletRequest,
					_portal.getSiteDefaultLocale(_group.getGroupId()),
					siteNavigationMenuItemType)));
	}

	@Test
	public void testIsBrowsableAssetCategoryTypeWithoutDisplayPageTemplate()
		throws Exception {

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		Assert.assertFalse(
			siteNavigationMenuItemType.isBrowsable(
				_getAssetCategorySiteNavigationMenuItem(
					mockHttpServletRequest,
					_portal.getSiteDefaultLocale(_group.getGroupId()),
					siteNavigationMenuItemType)));
	}

	@Test
	public void testIsBrowsableAssetVocabularyType() throws Exception {
		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		SiteNavigationMenuItem siteNavigationMenuItem =
			_addSiteNavigationMenuItem(
				_portal.getSiteDefaultLocale(_group.getGroupId()), "{}", false);

		Assert.assertFalse(
			siteNavigationMenuItemType.isBrowsable(siteNavigationMenuItem));
	}

	private AssetCategory _addAssetCategory(long parentAssetCategoryId)
		throws Exception {

		return _assetCategoryLocalService.addCategory(
			null, TestPropsValues.getUserId(), _group.getGroupId(),
			parentAssetCategoryId, RandomTestUtil.randomLocaleStringMap(),
			RandomTestUtil.randomLocaleStringMap(),
			_assetVocabulary.getVocabularyId(), null, _serviceContext);
	}

	private SiteNavigationMenuItem _addSiteNavigationMenuItem(
			Locale defaultLocale, String localizedNames,
			boolean showAssetVocabularyLevel)
		throws Exception {

		SiteNavigationMenu siteNavigationMenu =
			_siteNavigationMenuLocalService.addSiteNavigationMenu(
				TestPropsValues.getUserId(), _group.getGroupId(),
				RandomTestUtil.randomString(),
				SiteNavigationConstants.TYPE_DEFAULT, true, _serviceContext);

		return _siteNavigationMenuItemLocalService.addSiteNavigationMenuItem(
			TestPropsValues.getUserId(), _group.getGroupId(),
			siteNavigationMenu.getSiteNavigationMenuId(), 0,
			SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY,
			UnicodePropertiesBuilder.create(
				true
			).put(
				Field.DEFAULT_LANGUAGE_ID,
				LocaleUtil.toLanguageId(defaultLocale)
			).put(
				"classPK", String.valueOf(_assetVocabulary.getVocabularyId())
			).put(
				"groupId", String.valueOf(_assetVocabulary.getGroupId())
			).put(
				"localizedNames", localizedNames
			).put(
				"showAssetVocabularyLevel",
				String.valueOf(showAssetVocabularyLevel)
			).put(
				"title", _assetVocabulary.getTitle(defaultLocale)
			).put(
				"type", "asset-vocabulary"
			).put(
				"useCustomName",
				String.valueOf(!Objects.equals("{}", localizedNames))
			).put(
				"uuid", _assetVocabulary.getUuid()
			).buildString(),
			_serviceContext);
	}

	private void _assertAssetCategorySiteNavigationMenuItem(
		AssetCategory assetCategory, Locale locale,
		SiteNavigationMenuItem assetCategorySiteNavigationMenuItem) {

		UnicodeProperties typeSettingsUnicodeProperties =
			UnicodePropertiesBuilder.fastLoad(
				assetCategorySiteNavigationMenuItem.getTypeSettings()
			).build();

		Assert.assertEquals(
			"asset-category", typeSettingsUnicodeProperties.get("type"));
		Assert.assertEquals(
			assetCategory.getCategoryId(),
			GetterUtil.getLong(typeSettingsUnicodeProperties.get("classPK")));
		Assert.assertEquals(
			_assetVocabulary.getVocabularyId(),
			GetterUtil.getLong(
				typeSettingsUnicodeProperties.get("assetVocabularyId")));
		Assert.assertEquals(
			assetCategory.getTitle(locale),
			typeSettingsUnicodeProperties.get("title"));
	}

	private void _assertGetChildrenSiteNavigationMenuItems(
			Locale locale, long parentAssetCategoryId,
			SiteNavigationMenuItem siteNavigationMenuItem)
		throws Exception {

		ThemeDisplay themeDisplay = _getThemeDisplay();

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, themeDisplay);

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.ASSET_VOCABULARY);

		List<SiteNavigationMenuItem> childrenSiteNavigationMenuItems =
			siteNavigationMenuItemType.getChildrenSiteNavigationMenuItems(
				mockHttpServletRequest, siteNavigationMenuItem);

		List<AssetCategory> assetCategories =
			_assetCategoryLocalService.getVocabularyCategories(
				parentAssetCategoryId, _assetVocabulary.getVocabularyId(),
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Assert.assertEquals(
			childrenSiteNavigationMenuItems.toString(), assetCategories.size(),
			childrenSiteNavigationMenuItems.size());

		for (int i = 0; i < assetCategories.size(); i++) {
			AssetCategory assetCategory = assetCategories.get(i);

			SiteNavigationMenuItem childrenSiteNavigationMenuItem =
				childrenSiteNavigationMenuItems.get(i);

			_assertAssetCategorySiteNavigationMenuItem(
				assetCategory, locale, childrenSiteNavigationMenuItem);

			_assertGetChildrenSiteNavigationMenuItems(
				locale, assetCategory.getCategoryId(),
				childrenSiteNavigationMenuItem);
		}
	}

	private SiteNavigationMenuItem _getAssetCategorySiteNavigationMenuItem(
			AssetCategory assetCategory,
			MockHttpServletRequest mockHttpServletRequest, Locale locale,
			SiteNavigationMenuItemType siteNavigationMenuItemType)
		throws Exception {

		SiteNavigationMenuItem assetVocabularySiteNavigationMenuItem =
			_addSiteNavigationMenuItem(locale, "{}", false);

		List<SiteNavigationMenuItem> childrenSiteNavigationMenuItems =
			siteNavigationMenuItemType.getChildrenSiteNavigationMenuItems(
				mockHttpServletRequest, assetVocabularySiteNavigationMenuItem);

		Assert.assertEquals(
			childrenSiteNavigationMenuItems.toString(), 1,
			childrenSiteNavigationMenuItems.size());

		SiteNavigationMenuItem assetCategorySiteNavigationMenuItem =
			childrenSiteNavigationMenuItems.get(0);

		_assertAssetCategorySiteNavigationMenuItem(
			assetCategory, locale, assetCategorySiteNavigationMenuItem);

		return assetCategorySiteNavigationMenuItem;
	}

	private SiteNavigationMenuItem _getAssetCategorySiteNavigationMenuItem(
			MockHttpServletRequest mockHttpServletRequest, Locale locale,
			SiteNavigationMenuItemType siteNavigationMenuItemType)
		throws Exception {

		AssetCategory assetCategory = _addAssetCategory(0);

		return _getAssetCategorySiteNavigationMenuItem(
			assetCategory, mockHttpServletRequest, locale,
			siteNavigationMenuItemType);
	}

	private ThemeDisplay _getThemeDisplay() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(
			_companyLocalService.getCompany(_group.getCompanyId()));
		themeDisplay.setPermissionChecker(
			PermissionThreadLocal.getPermissionChecker());
		themeDisplay.setSiteGroupId(_group.getGroupId());
		themeDisplay.setUser(TestPropsValues.getUser());

		return themeDisplay;
	}

	@Inject
	private AssetCategoryLocalService _assetCategoryLocalService;

	@Inject
	private AssetDisplayPageFriendlyURLProvider
		_assetDisplayPageFriendlyURLProvider;

	private AssetVocabulary _assetVocabulary;

	@Inject
	private AssetVocabularyLocalService _assetVocabularyLocalService;

	@Inject
	private CompanyLocalService _companyLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Inject
	private Portal _portal;

	private ServiceContext _serviceContext;

	@Inject
	private SiteNavigationMenuItemLocalService
		_siteNavigationMenuItemLocalService;

	@Inject
	private SiteNavigationMenuItemTypeRegistry
		_siteNavigationMenuItemTypeRegistry;

	@Inject
	private SiteNavigationMenuLocalService _siteNavigationMenuLocalService;

}