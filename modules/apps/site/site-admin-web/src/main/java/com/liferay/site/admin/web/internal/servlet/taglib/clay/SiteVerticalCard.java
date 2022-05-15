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

package com.liferay.site.admin.web.internal.servlet.taglib.clay;

import com.liferay.frontend.taglib.clay.servlet.taglib.soy.BaseBaseClayCard;
import com.liferay.frontend.taglib.clay.servlet.taglib.soy.VerticalCard;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.site.admin.web.internal.display.context.SiteAdminDisplayContext;
import com.liferay.site.admin.web.internal.servlet.taglib.util.SiteActionDropdownItemsProvider;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class SiteVerticalCard extends BaseBaseClayCard implements VerticalCard {

	public SiteVerticalCard(
		BaseModel<?> baseModel, LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse, RowChecker rowChecker,
		SiteAdminDisplayContext siteAdminDisplayContext) {

		super(baseModel, rowChecker);

		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;
		_siteAdminDisplayContext = siteAdminDisplayContext;

		_group = (Group)baseModel;
		_httpServletRequest = PortalUtil.getHttpServletRequest(
			liferayPortletRequest);
		_themeDisplay = (ThemeDisplay)liferayPortletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Override
	public List<DropdownItem> getActionDropdownItems() {
		SiteActionDropdownItemsProvider siteActionDropdownItemsProvider =
			new SiteActionDropdownItemsProvider(
				_group, _liferayPortletRequest, _liferayPortletResponse,
				_siteAdminDisplayContext);

		try {
			return siteActionDropdownItemsProvider.getActionDropdownItems();
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return null;
	}

	@Override
	public String getHref() {
		if (_group.isCompany()) {
			return null;
		}

		return PortletURLBuilder.createRenderURL(
			_liferayPortletResponse
		).setBackURL(
			_themeDisplay.getURLCurrent()
		).setParameter(
			"groupId", _group.getGroupId()
		).buildString();
	}

	@Override
	public String getIcon() {
		return "sites";
	}

	@Override
	public String getImageSrc() {
		return _group.getLogoURL(_themeDisplay, false);
	}

	@Override
	public String getStickerIcon() {
		if (!_group.isActive()) {
			return "hidden";
		}

		return null;
	}

	@Override
	public String getStickerStyle() {
		return "light";
	}

	@Override
	public String getSubtitle() {
		if (_group.isCompany()) {
			return StringPool.DASH;
		}

		List<Group> childSites = _group.getChildren(true);

		return LanguageUtil.format(
			_httpServletRequest, "x-child-sites", childSites.size());
	}

	@Override
	public String getTitle() {
		try {
			return HtmlUtil.escape(
				_group.getDescriptiveName(_themeDisplay.getLocale()));
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return _group.getName(_themeDisplay.getLocale());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SiteVerticalCard.class);

	private final Group _group;
	private final HttpServletRequest _httpServletRequest;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final SiteAdminDisplayContext _siteAdminDisplayContext;
	private final ThemeDisplay _themeDisplay;

}