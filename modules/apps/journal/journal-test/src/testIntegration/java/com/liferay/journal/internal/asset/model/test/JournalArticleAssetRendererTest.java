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

package com.liferay.journal.internal.asset.model.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.info.item.InfoItemServiceTracker;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.layout.display.page.LayoutDisplayPageProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProviderTracker;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletRenderRequest;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portletmvc4spring.test.mock.web.portlet.MockRenderRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jürgen Kappler
 */
@RunWith(Arquillian.class)
public class JournalArticleAssetRendererTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_serviceContext = ServiceContextTestUtil.getServiceContext(
			_group.getGroupId());
	}

	@Test
	public void testGetURLViewInContext() throws Exception {
		long classNameId = _portal.getClassNameId(
			JournalArticle.class.getName());

		JournalArticle article = JournalTestUtil.addArticleWithWorkflow(
			_group.getGroupId(), false);

		DDMStructure ddmStructure = article.getDDMStructure();

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_layoutPageTemplateEntryLocalService.addLayoutPageTemplateEntry(
				TestPropsValues.getUserId(), _group.getGroupId(), 0,
				classNameId, ddmStructure.getStructureId(),
				RandomTestUtil.randomString(),
				LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE, 0, true,
				0, 0, 0, WorkflowConstants.STATUS_APPROVED, _serviceContext);

		AssetRendererFactory<JournalArticle> assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClass(
				JournalArticle.class);

		AssetRenderer<JournalArticle> assetRenderer =
			assetRendererFactory.getAssetRenderer(article, 0);

		LayoutDisplayPageProvider<?> layoutDisplayPageProvider =
			_layoutDisplayPageProviderTracker.
				getLayoutDisplayPageProviderByClassName(
					JournalArticle.class.getName());

		String urlSeparator = layoutDisplayPageProvider.getURLSeparator();

		ThemeDisplay themeDisplay = _getThemeDisplay(
			layoutPageTemplateEntry.getPlid());

		String urlViewInContext = assetRenderer.getURLViewInContext(
			_getLiferayPortletRequest(themeDisplay), null, null);

		Assert.assertNotNull(urlViewInContext);

		int index = urlViewInContext.indexOf(urlSeparator);

		Assert.assertTrue(index >= 0);

		String friendlyURL = urlViewInContext.substring(
			index + urlSeparator.length());

		Assert.assertEquals(
			article.getUrlTitle(), HttpComponentsUtil.getPath(friendlyURL));

		String version = HttpComponentsUtil.getParameter(
			urlViewInContext, "version");

		Assert.assertNotNull(version);

		Assert.assertEquals(article.getId(), GetterUtil.getLong(version));

		article = JournalTestUtil.updateArticleWithWorkflow(article, true);

		assetRenderer = assetRendererFactory.getAssetRenderer(article, 0);

		urlViewInContext = assetRenderer.getURLViewInContext(
			_getLiferayPortletRequest(themeDisplay), null, null);

		Assert.assertNotNull(urlViewInContext);

		index = urlViewInContext.indexOf(urlSeparator);

		Assert.assertTrue(index >= 0);

		friendlyURL = urlViewInContext.substring(index + urlSeparator.length());

		Assert.assertEquals(
			article.getUrlTitle(), HttpComponentsUtil.getPath(friendlyURL));

		Assert.assertEquals(
			StringPool.BLANK,
			HttpComponentsUtil.getParameter(urlViewInContext, "version"));
	}

	private LiferayPortletRequest _getLiferayPortletRequest(
		ThemeDisplay themeDisplay) {

		MockRenderRequest renderRequest = new MockLiferayPortletRenderRequest();

		renderRequest.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);

		return _portal.getLiferayPortletRequest(renderRequest);
	}

	private ThemeDisplay _getThemeDisplay(long plid) throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(
			_companyLocalService.fetchCompany(TestPropsValues.getCompanyId()));

		Layout layout = _layoutLocalService.getLayout(plid);

		themeDisplay.setLayout(layout);
		themeDisplay.setLayoutSet(layout.getLayoutSet());

		themeDisplay.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(TestPropsValues.getUser()));
		themeDisplay.setScopeGroupId(_group.getGroupId());
		themeDisplay.setSiteGroupId(_group.getGroupId());

		return themeDisplay;
	}

	@Inject
	private CompanyLocalService _companyLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private InfoItemServiceTracker _infoItemServiceTracker;

	@Inject
	private LayoutDisplayPageProviderTracker _layoutDisplayPageProviderTracker;

	@Inject
	private LayoutLocalService _layoutLocalService;

	@Inject
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Inject
	private Portal _portal;

	private ServiceContext _serviceContext;

}