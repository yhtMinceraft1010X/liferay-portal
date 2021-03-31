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

package com.liferay.osb.commerce.provisioning.web.internal.portlet.action;

import com.liferay.osb.commerce.provisioning.web.internal.constants.OSBCommerceProvisioningPortletKeys;
import com.liferay.portal.kernel.exception.ContactNameException;
import com.liferay.portal.kernel.exception.UserEmailAddressException;
import com.liferay.portal.kernel.exception.UserPasswordException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.PasswordPolicy;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.session.AuthenticatedSessionManager;
import com.liferay.portal.kernel.security.ldap.LDAPSettingsUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.permission.UserPermissionUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + OSBCommerceProvisioningPortletKeys.SETTINGS,
		"mvc.command.name=editCommerceAccountUser"
	},
	service = MVCActionCommand.class
)
public class EditCommerceAccountUserMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			Callable<User> userCallable = new UserCallable(
				actionRequest, actionResponse);

			TransactionInvokerUtil.invoke(_transactionConfig, userCallable);
		}
		catch (Throwable throwable) {
			Exception exception = null;

			if (throwable.getCause() == null) {
				exception = (Exception)throwable;
			}
			else {
				exception = (Exception)throwable.getCause();
			}

			if (throwable instanceof ContactNameException ||
				throwable instanceof UserEmailAddressException ||
				throwable instanceof UserPasswordException) {

				hideDefaultErrorMessage(actionRequest);

				SessionErrors.add(
					actionRequest, throwable.getClass(), throwable);
			}
			else {
				throw exception;
			}
		}
	}

	private void _updatePassword(
			ActionRequest actionRequest, ActionResponse actionResponse,
			User user)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String newPassword1 = actionRequest.getParameter("password1");
		String newPassword2 = actionRequest.getParameter("password2");

		boolean passwordReset = ParamUtil.getBoolean(
			actionRequest, "passwordReset");

		PasswordPolicy passwordPolicy = user.getPasswordPolicy();

		boolean ldapPasswordPolicyEnabled =
			LDAPSettingsUtil.isPasswordPolicyEnabled(user.getCompanyId());

		if ((user.getLastLoginDate() == null) &&
			(((passwordPolicy == null) && !ldapPasswordPolicyEnabled) ||
			 ((passwordPolicy != null) && passwordPolicy.isChangeable() &&
			  passwordPolicy.isChangeRequired()))) {

			passwordReset = true;
		}

		boolean passwordModified = false;

		if (Validator.isNotNull(newPassword1) ||
			Validator.isNotNull(newPassword2)) {

			_userLocalService.updatePassword(
				user.getUserId(), newPassword1, newPassword2, passwordReset);

			passwordModified = true;
		}

		_userLocalService.updatePasswordReset(user.getUserId(), passwordReset);

		if ((user.getUserId() == themeDisplay.getUserId()) &&
			passwordModified) {

			String login = null;

			Company company = themeDisplay.getCompany();

			String authType = company.getAuthType();

			if (authType.equals(CompanyConstants.AUTH_TYPE_EA)) {
				login = user.getEmailAddress();
			}
			else if (authType.equals(CompanyConstants.AUTH_TYPE_SN)) {
				login = user.getScreenName();
			}
			else if (authType.equals(CompanyConstants.AUTH_TYPE_ID)) {
				login = String.valueOf(user.getUserId());
			}

			_authenticatedSessionManager.login(
				_portal.getOriginalServletRequest(
					_portal.getHttpServletRequest(actionRequest)),
				_portal.getHttpServletResponse(actionResponse), login,
				newPassword1, false, null);
		}
	}

	private User _updateUser(ActionRequest actionRequest) throws Exception {
		long userId = ParamUtil.getLong(actionRequest, "userId");

		User user = _userLocalService.getUser(userId);

		UserPermissionUtil.check(
			PermissionThreadLocal.getPermissionChecker(), userId,
			ActionKeys.UPDATE);

		String emailAddress = ParamUtil.getString(
			actionRequest, "emailAddress");
		String firstName = ParamUtil.getString(actionRequest, "firstName");
		String lastName = ParamUtil.getString(actionRequest, "lastName");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			User.class.getName(), actionRequest);

		Date birthday = user.getBirthday();

		Calendar birthdayCal = CalendarFactoryUtil.getCalendar(
			birthday.getTime());

		return _userLocalService.updateUser(
			userId, user.getPassword(), null, null, false,
			user.getReminderQueryQuestion(), user.getReminderQueryAnswer(),
			user.getScreenName(), emailAddress, false, null,
			user.getLanguageId(), user.getTimeZoneId(), user.getGreeting(),
			user.getComments(), firstName, user.getMiddleName(), lastName, 0, 0,
			user.isMale(), birthdayCal.get(Calendar.MONTH),
			birthdayCal.get(Calendar.DAY_OF_MONTH),
			birthdayCal.get(Calendar.YEAR), null, null, null, null, null,
			user.getJobTitle(), user.getGroupIds(), user.getOrganizationIds(),
			user.getRoleIds(), null, user.getUserGroupIds(), serviceContext);
	}

	private static final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRED, new Class<?>[] {Exception.class});

	@Reference
	private AuthenticatedSessionManager _authenticatedSessionManager;

	@Reference
	private Portal _portal;

	@Reference
	private UserLocalService _userLocalService;

	private class UserCallable implements Callable<User> {

		@Override
		public User call() throws Exception {
			User user = _updateUser(_actionRequest);

			_updatePassword(_actionRequest, _actionResponse, user);

			return null;
		}

		private UserCallable(
			ActionRequest actionRequest, ActionResponse actionResponse) {

			_actionRequest = actionRequest;
			_actionResponse = actionResponse;
		}

		private final ActionRequest _actionRequest;
		private final ActionResponse _actionResponse;

	}

}