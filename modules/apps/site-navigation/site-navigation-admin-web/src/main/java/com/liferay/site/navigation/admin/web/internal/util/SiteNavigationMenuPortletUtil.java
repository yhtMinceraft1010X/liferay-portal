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

package com.liferay.site.navigation.admin.web.internal.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.model.SiteNavigationMenuItem;
import com.liferay.site.navigation.service.SiteNavigationMenuItemLocalServiceUtil;
import com.liferay.site.navigation.type.SiteNavigationMenuItemType;
import com.liferay.site.navigation.type.SiteNavigationMenuItemTypeRegistry;
import com.liferay.site.navigation.util.comparator.SiteNavigationMenuCreateDateComparator;
import com.liferay.site.navigation.util.comparator.SiteNavigationMenuNameComparator;

import java.util.List;

/**
 * @author Pavel Savinov
 */
public class SiteNavigationMenuPortletUtil {

	public static OrderByComparator<SiteNavigationMenu> getOrderByComparator(
		String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator<SiteNavigationMenu> orderByComparator = null;

		if (orderByCol.equals("create-date")) {
			orderByComparator = new SiteNavigationMenuCreateDateComparator(
				orderByAsc);
		}
		else if (orderByCol.equals("name")) {
			orderByComparator = new SiteNavigationMenuNameComparator(
				orderByAsc);
		}

		return orderByComparator;
	}

	public static JSONArray getSiteNavigationMenuItemsJSONArray(
		long parentSiteNavigationMenuItemId, long siteNavigationMenuId,
		SiteNavigationMenuItemTypeRegistry siteNavigationMenuItemTypeRegistry,
		ThemeDisplay themeDisplay) {

		JSONArray siteNavigationMenuItemsJSONArray =
			JSONFactoryUtil.createJSONArray();

		List<SiteNavigationMenuItem> siteNavigationMenuItems =
			SiteNavigationMenuItemLocalServiceUtil.getSiteNavigationMenuItems(
				siteNavigationMenuId, parentSiteNavigationMenuItemId);

		for (SiteNavigationMenuItem siteNavigationMenuItem :
				siteNavigationMenuItems) {

			long siteNavigationMenuItemId =
				siteNavigationMenuItem.getSiteNavigationMenuItemId();
			SiteNavigationMenuItemType siteNavigationMenuItemType =
				siteNavigationMenuItemTypeRegistry.
					getSiteNavigationMenuItemType(
						siteNavigationMenuItem.getType());

			siteNavigationMenuItemsJSONArray.put(
				JSONUtil.put(
					"children",
					getSiteNavigationMenuItemsJSONArray(
						siteNavigationMenuItemId, siteNavigationMenuId,
						siteNavigationMenuItemTypeRegistry, themeDisplay)
				).put(
					"dynamic",
					() -> {
						if (siteNavigationMenuItemType != null) {
							return siteNavigationMenuItemType.isDynamic();
						}

						return false;
					}
				).put(
					"icon",
					() -> {
						if (siteNavigationMenuItemType != null) {
							return siteNavigationMenuItemType.getStatusIcon(
								siteNavigationMenuItem);
						}

						return StringPool.BLANK;
					}
				).put(
					"parentSiteNavigationMenuItemId",
					parentSiteNavigationMenuItemId
				).put(
					"siteNavigationMenuItemId", siteNavigationMenuItemId
				).put(
					"title",
					() -> {
						if (siteNavigationMenuItemType != null) {
							return siteNavigationMenuItemType.getTitle(
								siteNavigationMenuItem,
								themeDisplay.getLocale());
						}

						return siteNavigationMenuItem.getName();
					}
				).put(
					"type",
					() -> {
						if (siteNavigationMenuItemType != null) {
							return siteNavigationMenuItemType.getSubtitle(
								siteNavigationMenuItem,
								themeDisplay.getLocale());
						}

						String typeLabel = ResourceActionsUtil.getModelResource(
							themeDisplay.getLocale(),
							siteNavigationMenuItem.getType());

						if (typeLabel.startsWith(
								ResourceActionsUtil.
									getModelResourceNamePrefix())) {

							return LanguageUtil.get(
								themeDisplay.getLocale(),
								siteNavigationMenuItem.getType());
						}

						return typeLabel;
					}
				));
		}

		return siteNavigationMenuItemsJSONArray;
	}

}