/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.commerce.portal.instance.initializer.internal.events;

import com.liferay.osb.commerce.portal.instance.constants.OSBCommercePortalInstanceConstants;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.util.PropsValues;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 */
@Component(
	immediate = true, property = "key=login.events.post",
	service = LifecycleAction.class
)
public class LoginPostAction extends Action {

	@Override
	public void run(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		try {
			PermissionChecker permissionChecker =
				PermissionCheckerFactoryUtil.create(
					_portal.getUser(httpServletRequest));

			User user = permissionChecker.getUser();

			Company company = _portal.getCompany(httpServletRequest);

			Role role = _roleLocalService.getRole(
				company.getCompanyId(),
				OSBCommercePortalInstanceConstants.
					ROLE_OSB_COMMERCE_ADMINISTRATOR);

			if (_roleLocalService.hasUserRole(
					user.getUserId(), role.getRoleId())) {

				httpServletResponse.sendRedirect(
					PropsValues.
						LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING +
							OSBCommercePortalInstanceConstants.
								FRIENDLY_URL_OSB_COMMERCE_PORTAL_INSTANCE_ADMIN);
			}
			else {
				httpServletResponse.sendRedirect(
					PropsValues.
						LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING +
							OSBCommercePortalInstanceConstants.
								FRIENDLY_URL_OSB_COMMERCE_PORTAL_INSTANCE_STOREFRONT);
			}
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LoginPostAction.class);

	@Reference
	private Portal _portal;

	@Reference
	private RoleLocalService _roleLocalService;

}