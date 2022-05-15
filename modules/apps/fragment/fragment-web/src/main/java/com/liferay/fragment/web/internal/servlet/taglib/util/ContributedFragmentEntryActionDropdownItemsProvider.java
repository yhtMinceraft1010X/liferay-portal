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

package com.liferay.fragment.web.internal.servlet.taglib.util;

import com.liferay.fragment.constants.FragmentActionKeys;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.web.internal.security.permission.resource.FragmentPermission;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class ContributedFragmentEntryActionDropdownItemsProvider {

	public ContributedFragmentEntryActionDropdownItemsProvider(
		FragmentEntry fragmentEntry, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		_fragmentEntry = fragmentEntry;
		_renderResponse = renderResponse;

		_httpServletRequest = PortalUtil.getHttpServletRequest(renderRequest);

		_themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public List<DropdownItem> getActionDropdownItems() throws Exception {
		return DropdownItemListBuilder.add(
			() -> FragmentPermission.contains(
				_themeDisplay.getPermissionChecker(),
				_themeDisplay.getScopeGroupId(),
				FragmentActionKeys.MANAGE_FRAGMENT_ENTRIES),
			_getCopyToFragmentEntryActionUnsafeConsumer()
		).build();
	}

	private UnsafeConsumer<DropdownItem, Exception>
			_getCopyToFragmentEntryActionUnsafeConsumer()
		throws Exception {

		return dropdownItem -> {
			dropdownItem.putData(
				"action", "copyContributedEntryToFragmentCollection");
			dropdownItem.putData(
				"contributedEntryKey",
				String.valueOf(_fragmentEntry.getFragmentEntryKey()));
			dropdownItem.putData(
				"copyContributedEntryURL",
				PortletURLBuilder.createActionURL(
					_renderResponse
				).setActionName(
					"/fragment/copy_contributed_entry"
				).setRedirect(
					_themeDisplay.getURLCurrent()
				).buildString());
			dropdownItem.putData(
				"selectFragmentCollectionURL",
				PortletURLBuilder.createRenderURL(
					_renderResponse
				).setMVCRenderCommandName(
					"/fragment/select_fragment_collection"
				).setWindowState(
					LiferayWindowState.POP_UP
				).buildString());
			dropdownItem.setIcon("copy");
			dropdownItem.setLabel(
				LanguageUtil.get(_httpServletRequest, "copy-to"));
		};
	}

	private final FragmentEntry _fragmentEntry;
	private final HttpServletRequest _httpServletRequest;
	private final RenderResponse _renderResponse;
	private final ThemeDisplay _themeDisplay;

}