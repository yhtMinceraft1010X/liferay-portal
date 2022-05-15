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

package com.liferay.layout.page.template.admin.web.internal.servlet.taglib.clay;

import com.liferay.frontend.taglib.clay.servlet.taglib.soy.BaseBaseClayCard;
import com.liferay.frontend.taglib.clay.servlet.taglib.soy.VerticalCard;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.LabelItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.LabelItemListBuilder;
import com.liferay.layout.page.template.admin.web.internal.servlet.taglib.util.LayoutPrototypeActionDropdownItemsProvider;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutPrototype;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Date;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class LayoutPrototypeVerticalCard
	extends BaseBaseClayCard implements VerticalCard {

	public LayoutPrototypeVerticalCard(
		BaseModel<?> baseModel, RenderRequest renderRequest,
		RenderResponse renderResponse, RowChecker rowChecker) {

		super(baseModel, rowChecker);

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;

		_layoutPrototype = (LayoutPrototype)baseModel;
		_themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
		_httpServletRequest = PortalUtil.getHttpServletRequest(renderRequest);
	}

	@Override
	public List<DropdownItem> getActionDropdownItems() {
		try {
			LayoutPrototypeActionDropdownItemsProvider
				layoutPrototypeActionDropdownItemsProvider =
					new LayoutPrototypeActionDropdownItemsProvider(
						_layoutPrototype, _renderRequest, _renderResponse);

			return layoutPrototypeActionDropdownItemsProvider.
				getActionDropdownItems();
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
		if (_layoutPrototype == null) {
			return null;
		}

		try {
			Group layoutPrototypeGroup = _layoutPrototype.getGroup();

			String layoutFullURL = layoutPrototypeGroup.getDisplayURL(
				_themeDisplay, true);

			return HttpComponentsUtil.setParameter(
				layoutFullURL, "p_l_back_url", _themeDisplay.getURLCurrent());
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return null;
	}

	@Override
	public String getIcon() {
		return "page-template";
	}

	@Override
	public List<LabelItem> getLabels() {
		return LabelItemListBuilder.add(
			labelItem -> {
				String label = "not-active";

				if (_layoutPrototype.isActive()) {
					label = "active";
				}

				labelItem.setLabel(
					LanguageUtil.get(_httpServletRequest, label));

				String style = "warning";

				if (_layoutPrototype.isActive()) {
					style = "success";
				}

				labelItem.setStyle(style);
			}
		).build();
	}

	@Override
	public String getSubtitle() {
		Date createDate = _layoutPrototype.getModifiedDate();

		String createdDateDescription = LanguageUtil.getTimeDescription(
			_httpServletRequest,
			System.currentTimeMillis() - createDate.getTime(), true);

		return LanguageUtil.format(
			_httpServletRequest, "created-x-ago", createdDateDescription);
	}

	@Override
	public String getTitle() {
		if (_layoutPrototype != null) {
			return HtmlUtil.escape(
				_layoutPrototype.getName(_themeDisplay.getLocale()));
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutPrototypeVerticalCard.class);

	private final HttpServletRequest _httpServletRequest;
	private final LayoutPrototype _layoutPrototype;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final ThemeDisplay _themeDisplay;

}