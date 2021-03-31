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

package com.liferay.osb.commerce.portal.instance.initializer.internal.instance.lifecycle;

import com.liferay.oauth2.provider.constants.ClientProfile;
import com.liferay.oauth2.provider.constants.OAuth2ProviderActionKeys;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService;
import com.liferay.oauth2.provider.util.OAuth2SecureRandomGenerator;
import com.liferay.osb.commerce.portal.instance.constants.OSBCommercePortalInstanceConstants;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Portal;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 */
@Component(immediate = true, service = PortalInstanceLifecycleListener.class)
public class OSBCommercePortalInstanceLifecycleListener
	extends BasePortalInstanceLifecycleListener {

	@Override
	public void portalInstanceRegistered(Company company) throws Exception {
		if (_portal.getDefaultCompanyId() == company.getCompanyId()) {
			return;
		}

		List<OAuth2Application> oAuth2Applications =
			_oAuth2ApplicationLocalService.getOAuth2Applications(
				company.getCompanyId());

		for (OAuth2Application oAuth2Application : oAuth2Applications) {
			if (Objects.equals(
					oAuth2Application.getName(), _APPLICATION_NAME)) {

				return;
			}
		}

		User user = _userLocalService.getDefaultUser(company.getCompanyId());

		OAuth2Application oAuth2Application =
			_oAuth2ApplicationLocalService.addOAuth2Application(
				company.getCompanyId(), user.getUserId(), user.getScreenName(),
				Collections.emptyList(), user.getUserId(),
				OAuth2SecureRandomGenerator.generateClientId(),
				ClientProfile.HEADLESS_SERVER.id(),
				OAuth2SecureRandomGenerator.generateClientSecret(), null, null,
				"https://commerce.liferay.com", 0, _APPLICATION_NAME, null,
				Collections.emptyList(), Collections.emptyList(),
				new ServiceContext());

		_addResourcePermissions(oAuth2Application);
	}

	private void _addResourcePermissions(OAuth2Application oAuth2Application)
		throws Exception {

		Role role = _roleLocalService.fetchRole(
			oAuth2Application.getCompanyId(),
			OSBCommercePortalInstanceConstants.ROLE_OSB_COMMERCE_ADMINISTRATOR);

		if (role == null) {
			return;
		}

		ResourcePermission resourcePermission =
			_resourcePermissionLocalService.fetchResourcePermission(
				oAuth2Application.getCompanyId(),
				OAuth2Application.class.getName(),
				ResourceConstants.SCOPE_INDIVIDUAL,
				String.valueOf(oAuth2Application.getPrimaryKey()),
				role.getRoleId());

		if (resourcePermission != null) {
			return;
		}

		_resourcePermissionLocalService.setResourcePermissions(
			oAuth2Application.getCompanyId(), OAuth2Application.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			String.valueOf(oAuth2Application.getPrimaryKey()), role.getRoleId(),
			new String[] {
				ActionKeys.VIEW, OAuth2ProviderActionKeys.ACTION_CREATE_TOKEN
			});
	}

	private static final String _APPLICATION_NAME = "OSB Commerce";

	@Reference
	private OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private UserLocalService _userLocalService;

}