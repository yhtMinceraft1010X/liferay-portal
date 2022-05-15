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

package com.liferay.account.admin.web.internal.frontend.taglib.servlet.taglib;

import com.liferay.account.admin.web.internal.constants.AccountScreenNavigationEntryConstants;
import com.liferay.account.admin.web.internal.security.permission.resource.AccountPermission;
import com.liferay.account.constants.AccountActionKeys;
import com.liferay.account.constants.AccountPortletKeys;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.permission.UserPermission;

import java.io.IOException;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Albert Lee
 */
@Component(
	property = "screen.navigation.entry.order:Integer=20",
	service = ScreenNavigationEntry.class
)
public class AccountUserAccountEntriesScreenNavigationEntry
	implements ScreenNavigationEntry<User> {

	@Override
	public String getCategoryKey() {
		return AccountScreenNavigationEntryConstants.CATEGORY_KEY_GENERAL;
	}

	@Override
	public String getEntryKey() {
		return AccountScreenNavigationEntryConstants.ENTRY_KEY_ACCOUNTS;
	}

	public String getJspPath() {
		return "/account_users_admin/account_user/account_entries.jsp";
	}

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(
			locale, AccountScreenNavigationEntryConstants.ENTRY_KEY_ACCOUNTS);
	}

	@Override
	public String getScreenNavigationKey() {
		return AccountScreenNavigationEntryConstants.
			SCREEN_NAVIGATION_KEY_ACCOUNT_USER;
	}

	@Override
	public boolean isVisible(User user, User selUser) {
		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		if (AccountPermission.contains(
				permissionChecker, AccountPortletKeys.ACCOUNT_USERS_ADMIN,
				AccountActionKeys.ASSIGN_ACCOUNTS) ||
			_userPermission.contains(
				permissionChecker, selUser.getUserId(), ActionKeys.UPDATE)) {

			return true;
		}

		return false;
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		_jspRenderer.renderJSP(
			httpServletRequest, httpServletResponse, getJspPath());
	}

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference
	private UserPermission _userPermission;

}