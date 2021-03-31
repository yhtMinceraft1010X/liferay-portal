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

package com.liferay.headless.osb.commerce.portal.instance.internal.resource.v1_0;

import com.liferay.headless.osb.commerce.portal.instance.dto.v1_0.UserAccount;
import com.liferay.headless.osb.commerce.portal.instance.resource.v1_0.UserAccountResource;
import com.liferay.osb.commerce.portal.instance.constants.OSBCommercePortalInstanceConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserGroupRoleService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.PropsValues;

import java.util.Calendar;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Ivica Cardic
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/user-account.properties",
	scope = ServiceScope.PROTOTYPE, service = UserAccountResource.class
)
public class UserAccountResourceImpl extends BaseUserAccountResourceImpl {

	@Override
	public UserAccount postUserAccount(
			String portalInstanceId, UserAccount userAccount)
		throws Exception {

		Company company = _companyLocalService.getCompanyByWebId(
			portalInstanceId);

		Role role = _roleLocalService.getRole(
			company.getCompanyId(), "OSB Commerce Administrator");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			contextHttpServletRequest);

		serviceContext.setCompanyId(company.getCompanyId());
		serviceContext.setPlid(0);
		serviceContext.setPortalURL(
			_getPortalURL(
				contextHttpServletRequest, company.getVirtualHostname()));

		User user = _userLocalService.addUser(
			_userLocalService.getDefaultUserId(company.getCompanyId()),
			company.getCompanyId(), true, null, null, false,
			userAccount.getScreenName(), userAccount.getEmailAddress(), 0, null,
			LocaleUtil.fromLanguageId(userAccount.getLanguageId()),
			userAccount.getFirstName(), userAccount.getMiddleName(),
			userAccount.getLastName(), 0, 0, true,
			_getBirthdayMonth(userAccount), _getBirthdayDay(userAccount),
			_getBirthdayYear(userAccount), userAccount.getJobTitle(),
			new long[0], new long[0], new long[] {role.getRoleId()},
			new long[0], true, serviceContext);

		_addUserSiteOwnerGroupRole(company, user.getUserId());

		return _toUserAccount(user);
	}

	private void _addUserSiteOwnerGroupRole(Company company, long userId)
		throws Exception {

		Role role = _roleLocalService.getRole(
			company.getCompanyId(), RoleConstants.SITE_OWNER);

		Group group = _groupLocalService.getFriendlyURLGroup(
			company.getCompanyId(),
			OSBCommercePortalInstanceConstants.
				FRIENDLY_URL_OSB_COMMERCE_PORTAL_INSTANCE_ADMIN);

		_userGroupRoleService.addUserGroupRoles(
			userId, group.getGroupId(), new long[] {role.getRoleId()});

		group = _groupLocalService.getFriendlyURLGroup(
			company.getCompanyId(),
			OSBCommercePortalInstanceConstants.
				FRIENDLY_URL_OSB_COMMERCE_PORTAL_INSTANCE_STOREFRONT);

		_userGroupRoleService.addUserGroupRoles(
			userId, group.getGroupId(), new long[] {role.getRoleId()});
	}

	private int _getBirthdayDay(UserAccount userAccount) {
		return _getCalendarFieldValue(userAccount, Calendar.DAY_OF_MONTH, 1);
	}

	private int _getBirthdayMonth(UserAccount userAccount) {
		return _getCalendarFieldValue(
			userAccount, Calendar.MONTH, Calendar.JANUARY);
	}

	private int _getBirthdayYear(UserAccount userAccount) {
		return _getCalendarFieldValue(userAccount, Calendar.YEAR, 1977);
	}

	private int _getCalendarFieldValue(
		UserAccount userAccount, int calendarField, int defaultValue) {

		return Optional.ofNullable(
			userAccount.getBirthDate()
		).map(
			date -> {
				Calendar calendar = CalendarFactoryUtil.getCalendar();

				calendar.setTime(date);

				return calendar.get(calendarField);
			}
		).orElse(
			defaultValue
		);
	}

	private String _getPortalURL(
		HttpServletRequest httpServletRequest, String virtualHostname) {

		int serverPort = _portal.getForwardedPort(httpServletRequest);

		return _getPortalURL(
			virtualHostname, serverPort, _portal.isSecure(httpServletRequest));
	}

	private String _getPortalURL(
		String virtualHostname, int serverPort, boolean secure) {

		StringBundler sb = new StringBundler(4);

		boolean https = false;

		if (secure ||
			StringUtil.equalsIgnoreCase(
				Http.HTTPS, PropsValues.WEB_SERVER_PROTOCOL)) {

			https = true;
		}

		if (https) {
			sb.append(Http.HTTPS_WITH_SLASH);
		}
		else {
			sb.append(Http.HTTP_WITH_SLASH);
		}

		sb.append(virtualHostname);

		if (!https) {
			if (PropsValues.WEB_SERVER_HTTP_PORT == -1) {
				if ((serverPort != -1) && (serverPort != Http.HTTP_PORT) &&
					(serverPort != Http.HTTPS_PORT)) {

					sb.append(StringPool.COLON);
					sb.append(serverPort);
				}
			}
			else {
				if (PropsValues.WEB_SERVER_HTTP_PORT != Http.HTTP_PORT) {
					sb.append(StringPool.COLON);
					sb.append(PropsValues.WEB_SERVER_HTTP_PORT);
				}
			}
		}
		else {
			if (PropsValues.WEB_SERVER_HTTPS_PORT == -1) {
				if ((serverPort != -1) && (serverPort != Http.HTTP_PORT) &&
					(serverPort != Http.HTTPS_PORT)) {

					sb.append(StringPool.COLON);
					sb.append(serverPort);
				}
			}
			else {
				if (PropsValues.WEB_SERVER_HTTPS_PORT != Http.HTTPS_PORT) {
					sb.append(StringPool.COLON);
					sb.append(PropsValues.WEB_SERVER_HTTPS_PORT);
				}
			}
		}

		return sb.toString();
	}

	private UserAccount _toUserAccount(User user) throws Exception {
		return new UserAccount() {
			{
				birthDate = user.getBirthday();
				dateCreated = user.getCreateDate();
				dateModified = user.getModifiedDate();
				emailAddress = user.getEmailAddress();
				firstName = user.getFirstName();
				id = user.getUserId();
				jobTitle = user.getJobTitle();
				lastName = user.getLastName();
				male = user.getMale();
				middleName = user.getMiddleName();
				name = user.getFullName();
				screenName = user.getScreenName();
			}
		};
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private UserGroupRoleService _userGroupRoleService;

	@Reference
	private UserLocalService _userLocalService;

}