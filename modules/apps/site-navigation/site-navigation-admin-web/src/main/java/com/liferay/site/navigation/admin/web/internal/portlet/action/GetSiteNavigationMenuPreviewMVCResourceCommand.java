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

package com.liferay.site.navigation.admin.web.internal.portlet.action;

import com.liferay.petra.io.unsync.UnsyncStringWriter;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.servlet.PipingServletResponse;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.theme.ThemeUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.site.navigation.admin.constants.SiteNavigationAdminPortletKeys;
import com.liferay.site.navigation.taglib.servlet.taglib.NavigationMenuTag;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sandro Chinea
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + SiteNavigationAdminPortletKeys.SITE_NAVIGATION_ADMIN,
		"mvc.command.name=/site_navigation_admin/get_site_navigation_menu_preview"
	},
	service = MVCResourceCommand.class
)
public class GetSiteNavigationMenuPreviewMVCResourceCommand
	extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			resourceRequest);
		HttpServletResponse httpServletResponse =
			_portal.getHttpServletResponse(resourceResponse);

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		long siteNavigationMenuId = ParamUtil.getLong(
			resourceRequest, "siteNavigationMenuId");

		String ddmTemplateKey = ParamUtil.getString(
			resourceRequest, "ddmTemplateKey");

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		PipingServletResponse pipingServletResponse = new PipingServletResponse(
			httpServletResponse, unsyncStringWriter);

		NavigationMenuTag navigationMenuTag = new NavigationMenuTag();

		navigationMenuTag.setDdmTemplateKey(ddmTemplateKey);
		navigationMenuTag.setSiteNavigationMenuId(siteNavigationMenuId);
		navigationMenuTag.setRootItemLevel(0);

		navigationMenuTag.doTag(httpServletRequest, pipingServletResponse);

		LayoutSet layoutSet = _layoutSetLocalService.getLayoutSet(
			themeDisplay.getScopeGroupId(), false);

		themeDisplay.setLayoutSet(layoutSet);
		themeDisplay.setLookAndFeel(
			layoutSet.getTheme(), layoutSet.getColorScheme());

		httpServletRequest.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);

		Document document = Jsoup.parse(
			ThemeUtil.include(
				ServletContextPool.get(StringPool.BLANK), httpServletRequest,
				httpServletResponse, "portal_normal.ftl", layoutSet.getTheme(),
				false));

		Element bodyElement = document.body();

		bodyElement.html(unsyncStringWriter.toString());

		ServletResponseUtil.write(httpServletResponse, document.html());

		ServletResponseUtil.write(
			httpServletResponse, unsyncStringWriter.toString());
	}

	@Reference
	private LayoutSetLocalService _layoutSetLocalService;

	@Reference
	private Portal _portal;

}