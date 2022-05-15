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
import com.liferay.account.admin.web.internal.security.permission.resource.AccountEntryPermission;
import com.liferay.account.admin.web.internal.util.AllowEditAccountRoleThreadLocal;
import com.liferay.account.constants.AccountActionKeys;
import com.liferay.account.constants.AccountConstants;
import com.liferay.account.model.AccountEntry;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationCategory;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;

import java.util.Locale;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;

/**
 * @author Pei-Jung Lan
 * @author Alessio Antonio Rendina
 */
@Component(
	property = {
		"screen.navigation.category.order:Integer=60",
		"screen.navigation.entry.order:Integer=10"
	},
	service = {ScreenNavigationCategory.class, ScreenNavigationEntry.class}
)
public class AccountEntryRolesScreenNavigationCategory
	extends BaseAccountEntryScreenNavigationEntry
	implements ScreenNavigationCategory {

	@Override
	public String getCategoryKey() {
		return AccountScreenNavigationEntryConstants.CATEGORY_KEY_ROLES;
	}

	@Override
	public String getEntryKey() {
		return AccountScreenNavigationEntryConstants.ENTRY_KEY_ROLES;
	}

	@Override
	public String getJspPath() {
		return "/account_entries_admin/account_entry/view_account_roles.jsp";
	}

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(locale, "roles");
	}

	@Override
	public boolean isVisible(User user, AccountEntry accountEntry) {
		if ((accountEntry == null) ||
			!AllowEditAccountRoleThreadLocal.isAllowEditAccountRole() ||
			Objects.equals(
				accountEntry.getType(),
				AccountConstants.ACCOUNT_ENTRY_TYPE_GUEST)) {

			return false;
		}

		return AccountEntryPermission.contains(
			PermissionCheckerFactoryUtil.create(user),
			accountEntry.getAccountEntryId(),
			AccountActionKeys.VIEW_ACCOUNT_ROLES);
	}

}