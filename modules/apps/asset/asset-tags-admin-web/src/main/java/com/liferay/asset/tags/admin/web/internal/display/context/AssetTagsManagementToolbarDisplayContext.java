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

package com.liferay.asset.tags.admin.web.internal.display.context;

import com.liferay.asset.tags.constants.AssetTagsAdminPortletKeys;
import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.SearchContainerManagementToolbarDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenuBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portlet.asset.service.permission.AssetTagsPermission;

import java.util.List;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class AssetTagsManagementToolbarDisplayContext
	extends SearchContainerManagementToolbarDisplayContext {

	public AssetTagsManagementToolbarDisplayContext(
			HttpServletRequest httpServletRequest,
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			AssetTagsDisplayContext assetTagsDisplayContext)
		throws PortalException {

		super(
			httpServletRequest, liferayPortletRequest, liferayPortletResponse,
			assetTagsDisplayContext.getTagsSearchContainer());

		_assetTagsDisplayContext = assetTagsDisplayContext;
	}

	@Override
	public List<DropdownItem> getActionDropdownItems() {
		return DropdownItemListBuilder.addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					DropdownItemListBuilder.add(
						_assetTagsDisplayContext::isShowTagsActions,
						dropdownItem -> {
							dropdownItem.putData("action", "mergeTags");
							dropdownItem.putData(
								"mergeTagsURL",
								PortletURLBuilder.createRenderURL(
									liferayPortletResponse
								).setMVCPath(
									"/merge_tag.jsp"
								).setParameter(
									"mergeTagIds", "[$MERGE_TAGS_IDS$]"
								).buildString());
							dropdownItem.setIcon("merge");
							dropdownItem.setLabel(
								LanguageUtil.get(httpServletRequest, "merge"));
							dropdownItem.setQuickAction(true);
						}
					).build());
				dropdownGroupItem.setSeparator(true);
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					DropdownItemListBuilder.add(
						dropdownItem -> {
							dropdownItem.putData("action", "deleteTags");
							dropdownItem.setIcon("trash");
							dropdownItem.setLabel(
								LanguageUtil.get(httpServletRequest, "delete"));
							dropdownItem.setQuickAction(true);
						}
					).build());
				dropdownGroupItem.setSeparator(true);
			}
		).build();
	}

	@Override
	public String getClearResultsURL() {
		return PortletURLBuilder.create(
			getPortletURL()
		).setKeywords(
			StringPool.BLANK
		).buildString();
	}

	@Override
	public String getComponentId() {
		return "assetTagsManagementToolbar";
	}

	@Override
	public CreationMenu getCreationMenu() {
		return CreationMenuBuilder.addPrimaryDropdownItem(
			dropdownItem -> {
				dropdownItem.setHref(
					liferayPortletResponse.createRenderURL(), "mvcPath",
					"/edit_tag.jsp");
				dropdownItem.setLabel(
					LanguageUtil.get(httpServletRequest, "add-tag"));
			}
		).build();
	}

	@Override
	public String getSearchActionURL() {
		PortletURL searchTagURL = getPortletURL();

		return searchTagURL.toString();
	}

	@Override
	public String getSearchContainerId() {
		return "assetTags";
	}

	@Override
	public Boolean isShowCreationMenu() {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (AssetTagsPermission.contains(
				themeDisplay.getPermissionChecker(),
				AssetTagsPermission.RESOURCE_NAME,
				AssetTagsAdminPortletKeys.ASSET_TAGS_ADMIN,
				themeDisplay.getSiteGroupId(), ActionKeys.MANAGE_TAG)) {

			return _assetTagsDisplayContext.isShowTagsActions();
		}

		return false;
	}

	@Override
	protected String getDisplayStyle() {
		return _assetTagsDisplayContext.getDisplayStyle();
	}

	@Override
	protected String[] getDisplayViews() {
		return new String[] {"list", "descriptive"};
	}

	@Override
	protected String[] getNavigationKeys() {
		return new String[] {"all"};
	}

	@Override
	protected String[] getOrderByKeys() {
		return new String[] {"name", "usages"};
	}

	private final AssetTagsDisplayContext _assetTagsDisplayContext;

}