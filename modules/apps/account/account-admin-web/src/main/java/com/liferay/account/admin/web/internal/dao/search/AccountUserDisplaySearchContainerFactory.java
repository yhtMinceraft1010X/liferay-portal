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

package com.liferay.account.admin.web.internal.dao.search;

import com.liferay.account.admin.web.internal.display.AccountUserDisplay;
import com.liferay.account.admin.web.internal.security.permission.resource.AccountEntryPermission;
import com.liferay.account.constants.AccountConstants;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.retriever.AccountUserRetriever;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalService;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.vulcan.util.TransformUtil;
import com.liferay.users.admin.kernel.util.UsersAdmin;

import java.util.List;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pei-Jung Lan
 */
@Component(service = {})
public class AccountUserDisplaySearchContainerFactory {

	public static SearchContainer<AccountUserDisplay> create(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws PortalException {

		String accountEntriesNavigation = ParamUtil.getString(
			liferayPortletRequest, "accountEntriesNavigation", "any-account");

		long[] accountEntryIds = null;

		if (accountEntriesNavigation.equals("any-account")) {
			accountEntryIds = new long[] {
				AccountConstants.ACCOUNT_ENTRY_ID_ANY
			};
		}
		else if (accountEntriesNavigation.equals("selected-accounts")) {
			accountEntryIds = ParamUtil.getLongValues(
				liferayPortletRequest, "accountEntryIds");
		}
		else if (accountEntriesNavigation.equals("no-assigned-account")) {
			accountEntryIds = new long[0];
		}

		return _create(
			accountEntryIds, "no-users-were-found", liferayPortletRequest,
			liferayPortletResponse);
	}

	public static SearchContainer<AccountUserDisplay> create(
			long accountEntryId, LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws PortalException {

		String emptyResultsMessage =
			"there-are-no-users-associated-with-this-account";

		if (_accountUserRetriever.getAccountUsersCount(accountEntryId) > 0) {
			emptyResultsMessage = "no-users-were-found";
		}

		SearchContainer<AccountUserDisplay> searchContainer = _create(
			new long[] {accountEntryId}, emptyResultsMessage,
			liferayPortletRequest, liferayPortletResponse);

		if (!AccountEntryPermission.contains(
				PermissionCheckerFactoryUtil.create(
					PortalUtil.getUser(liferayPortletRequest)),
				accountEntryId, ActionKeys.MANAGE_USERS)) {

			searchContainer.setRowChecker(null);
		}

		return searchContainer;
	}

	public static SearchContainer<AccountUserDisplay> create(
			long accountEntryId, long roleId,
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws PortalException {

		String emptyResultsMessage =
			"there-are-no-users-associated-with-this-role";

		AccountEntry accountEntry = _accountEntryLocalService.getAccountEntry(
			accountEntryId);

		List<UserGroupRole> userGroupRoles =
			_userGroupRoleLocalService.getUserGroupRolesByGroupAndRole(
				accountEntry.getAccountEntryGroupId(), roleId);

		if (!ListUtil.isEmpty(userGroupRoles)) {
			emptyResultsMessage = "no-users-were-found";
		}

		return _create(
			new long[] {accountEntryId}, emptyResultsMessage,
			liferayPortletRequest, liferayPortletResponse);
	}

	@Reference(unbind = "-")
	protected void setAccountEntryLocalService(
		AccountEntryLocalService accountEntryLocalService) {

		_accountEntryLocalService = accountEntryLocalService;
	}

	@Reference(unbind = "-")
	protected void setAccountUserRetriever(
		AccountUserRetriever accountUserRetriever) {

		_accountUserRetriever = accountUserRetriever;
	}

	@Reference(unbind = "-")
	protected void setUserGroupRoleLocalService(
		UserGroupRoleLocalService userGroupRoleLocalService) {

		_userGroupRoleLocalService = userGroupRoleLocalService;
	}

	@Reference(unbind = "-")
	protected void setUsersAdmin(UsersAdmin usersAdmin) {
		_usersAdmin = usersAdmin;
	}

	private static SearchContainer<AccountUserDisplay> _create(
			long[] accountEntryIds, String emptyResultsMessage,
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws PortalException {

		SearchContainer<AccountUserDisplay> accountUserDisplaySearchContainer =
			new SearchContainer(
				liferayPortletRequest,
				PortletURLUtil.getCurrent(
					liferayPortletRequest, liferayPortletResponse),
				null, emptyResultsMessage);

		accountUserDisplaySearchContainer.setId("accountUsers");

		String orderByCol = ParamUtil.getString(
			liferayPortletRequest, "orderByCol", "last-name");

		accountUserDisplaySearchContainer.setOrderByCol(orderByCol);

		String orderByType = ParamUtil.getString(
			liferayPortletRequest, "orderByType", "asc");

		accountUserDisplaySearchContainer.setOrderByType(orderByType);

		String keywords = ParamUtil.getString(
			liferayPortletRequest, "keywords", null);

		BaseModelSearchResult<User> baseModelSearchResult;

		long accountRoleId = ParamUtil.getLong(
			liferayPortletRequest, "accountRoleId");

		if (accountRoleId > 0) {
			baseModelSearchResult = _getBaseModelSearchResult(
				accountEntryIds[0], accountRoleId, keywords,
				accountUserDisplaySearchContainer.getStart(),
				accountUserDisplaySearchContainer.getEnd(), orderByCol,
				orderByType);
		}
		else {
			String navigation = ParamUtil.getString(
				liferayPortletRequest, "navigation", "active");

			baseModelSearchResult = _getBaseModelSearchResult(
				accountEntryIds, keywords, _getStatus(navigation),
				accountUserDisplaySearchContainer.getStart(),
				accountUserDisplaySearchContainer.getDelta(), orderByCol,
				orderByType);
		}

		accountUserDisplaySearchContainer.setResultsAndTotal(
			() -> TransformUtil.transform(
				baseModelSearchResult.getBaseModels(), AccountUserDisplay::of),
			baseModelSearchResult.getLength());
		accountUserDisplaySearchContainer.setRowChecker(
			new EmptyOnClickRowChecker(liferayPortletResponse));

		return accountUserDisplaySearchContainer;
	}

	private static BaseModelSearchResult<User> _getBaseModelSearchResult(
			long accountEntryId, long accountRoleId, String keywords, int start,
			int end, String orderByCol, String orderByType)
		throws PortalException {

		return _accountUserRetriever.searchAccountRoleUsers(
			accountEntryId, accountRoleId, keywords, start, end,
			_usersAdmin.getUserOrderByComparator(orderByCol, orderByType));
	}

	private static BaseModelSearchResult<User> _getBaseModelSearchResult(
			long[] accountEntryIds, String keywords, int status, int start,
			int delta, String orderByCol, String orderByType)
		throws PortalException {

		return _accountUserRetriever.searchAccountUsers(
			accountEntryIds, keywords, null, status, start, delta, orderByCol,
			_isReverseOrder(orderByType));
	}

	private static int _getStatus(String navigation) {
		if (Objects.equals(navigation, "inactive")) {
			return WorkflowConstants.STATUS_INACTIVE;
		}

		return WorkflowConstants.STATUS_APPROVED;
	}

	private static boolean _isReverseOrder(String orderByType) {
		if (Objects.equals(orderByType, "desc")) {
			return true;
		}

		return false;
	}

	private static AccountEntryLocalService _accountEntryLocalService;
	private static AccountUserRetriever _accountUserRetriever;
	private static UserGroupRoleLocalService _userGroupRoleLocalService;
	private static UsersAdmin _usersAdmin;

}