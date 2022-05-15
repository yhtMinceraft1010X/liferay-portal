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

package com.liferay.commerce.account.internal.verify;

import com.liferay.commerce.account.service.CommerceAccountGroupLocalService;
import com.liferay.commerce.account.util.CommerceAccountRoleHelper;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.uuid.PortalUUID;
import com.liferay.portal.verify.VerifyProcess;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false, immediate = true,
	property = "verify.process.name=com.liferay.commerce.account.service",
	service = {CommerceAccountServiceVerifyProcess.class, VerifyProcess.class}
)
public class CommerceAccountServiceVerifyProcess extends VerifyProcess {

	@Override
	protected void doVerify() throws Exception {
		_verifyAccountRoles();
		_verifyAccountGroup();
	}

	private long _getAdminUserId(long companyId) throws PortalException {
		Role role = _roleLocalService.getRole(
			companyId, RoleConstants.ADMINISTRATOR);

		long[] userIds = _userLocalService.getRoleUserIds(role.getRoleId());

		if (userIds.length == 0) {
			throw new NoSuchUserException(
				StringBundler.concat(
					"No user exists in company ", companyId, " with role ",
					role.getName()));
		}

		return userIds[0];
	}

	private void _verifyAccountGroup() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			_companyLocalService.forEachCompanyId(
				companyId ->
					_commerceAccountGroupLocalService.
						checkGuestCommerceAccountGroup(companyId));
		}
	}

	private void _verifyAccountRoles() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			_companyLocalService.forEachCompanyId(
				companyId -> {
					ServiceContext serviceContext = new ServiceContext();

					serviceContext.setCompanyId(companyId);
					serviceContext.setUserId(_getAdminUserId(companyId));
					serviceContext.setUuid(_portalUUID.generate());

					_commerceAccountRoleHelper.checkCommerceAccountRoles(
						serviceContext);
				});
		}
	}

	@Reference
	private CommerceAccountGroupLocalService _commerceAccountGroupLocalService;

	@Reference
	private CommerceAccountRoleHelper _commerceAccountRoleHelper;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private PortalUUID _portalUUID;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private UserLocalService _userLocalService;

}