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

package com.liferay.asset.publisher.web.internal.display.context;

import com.liferay.asset.publisher.util.AssetPublisherHelper;
import com.liferay.item.selector.criteria.group.criterion.GroupItemSelectorCriterion;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portlet.usersadmin.search.GroupSearch;
import com.liferay.portlet.usersadmin.search.GroupSearchTerms;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class ChildSitesItemSelectorViewDisplayContext
	extends BaseItemSelectorViewDisplayContext {

	public ChildSitesItemSelectorViewDisplayContext(
		HttpServletRequest httpServletRequest,
		AssetPublisherHelper assetPublisherHelper,
		GroupItemSelectorCriterion groupItemSelectorCriterion,
		String itemSelectedEventName, PortletURL portletURL) {

		super(
			httpServletRequest, assetPublisherHelper,
			groupItemSelectorCriterion, itemSelectedEventName, portletURL);
	}

	@Override
	public GroupSearch getGroupSearch() throws Exception {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		GroupSearch groupSearch = new GroupSearch(
			getPortletRequest(), getPortletURL());

		GroupSearchTerms groupSearchTerms =
			(GroupSearchTerms)groupSearch.getSearchTerms();

		groupSearch.setResultsAndTotal(
			_filterGroups(
				GroupLocalServiceUtil.search(
					themeDisplay.getCompanyId(), _CLASS_NAME_IDS,
					groupSearchTerms.getKeywords(), _getGroupParams(),
					QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					groupSearch.getOrderByComparator()),
				themeDisplay.getPermissionChecker()));

		return groupSearch;
	}

	private List<Group> _filterGroups(
		List<Group> groups, PermissionChecker permissionChecker) {

		List<Group> filteredGroups = new ArrayList<>();

		for (Group group : groups) {
			if (permissionChecker.isGroupAdmin(group.getGroupId())) {
				filteredGroups.add(group);
			}
		}

		return filteredGroups;
	}

	private LinkedHashMap<String, Object> _getGroupParams() throws Exception {
		if (_groupParams != null) {
			return _groupParams;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		_groupParams = LinkedHashMapBuilder.<String, Object>put(
			"active", Boolean.TRUE
		).put(
			"groupsTree", ListUtil.fromArray(themeDisplay.getSiteGroup())
		).put(
			"site", Boolean.TRUE
		).put(
			"excludedGroupIds",
			() -> {
				List<Long> excludedGroupIds = new ArrayList<>();

				Group group = themeDisplay.getSiteGroup();

				if (group.isStagingGroup()) {
					excludedGroupIds.add(group.getLiveGroupId());
				}
				else {
					excludedGroupIds.add(themeDisplay.getSiteGroupId());
				}

				return excludedGroupIds;
			}
		).build();

		return _groupParams;
	}

	private static final long[] _CLASS_NAME_IDS = {
		PortalUtil.getClassNameId(Group.class),
		PortalUtil.getClassNameId(Organization.class)
	};

	private LinkedHashMap<String, Object> _groupParams;

}