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

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalService;
import com.liferay.commerce.constants.CommerceAddressConstants;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommerceOrderPaymentConstants;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceCountry;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.notification.util.CommerceNotificationHelper;
import com.liferay.commerce.order.engine.CommerceOrderEngine;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceAddressLocalService;
import com.liferay.commerce.service.CommerceCountryLocalService;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.osb.commerce.provisioning.constants.OSBCommerceNotificationConstants;
import com.liferay.osb.commerce.provisioning.constants.OSBCommerceProvisioningConstants;
import com.liferay.osb.commerce.provisioning.web.internal.constants.OSBCommerceProvisioningPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.UserEmailAddressException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.math.BigDecimal;

import java.util.concurrent.Callable;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.MimeResponse;
import javax.portlet.RenderURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + OSBCommerceProvisioningPortletKeys.TRIAL_REGISTRATION,
		"mvc.command.name=registerTrial"
	},
	service = MVCActionCommand.class
)
public class RegisterTrialMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String companyName = ParamUtil.getString(actionRequest, "companyName");
		String countryCode = ParamUtil.getString(actionRequest, "countryCode");
		String jobTitle = ParamUtil.getString(actionRequest, "jobTitle");
		String name = ParamUtil.getString(actionRequest, "name");
		String password = ParamUtil.getString(actionRequest, "password");
		String workEmail = ParamUtil.getString(actionRequest, "workEmail");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			actionRequest);

		long commerceOrderItemId = -1;

		try {
			_checkUser(serviceContext.getCompanyId(), workEmail);

			commerceOrderItemId = _invoke(
				() -> _add(
					companyName, countryCode, workEmail, jobTitle, name,
					password, serviceContext));
		}
		catch (Throwable throwable) {
			Exception exception = null;

			if (throwable.getCause() == null) {
				exception = (Exception)throwable;
			}
			else {
				exception = (Exception)throwable.getCause();
			}

			if (exception instanceof
					UserEmailAddressException.MustNotBeDuplicate) {

				hideDefaultErrorMessage(actionRequest);

				SessionErrors.add(actionRequest, throwable.getClass());

				return;
			}

			throw exception;
		}

		_sendRedirect(actionResponse, commerceOrderItemId, name);
	}

	private long _add(
			String commerceAccountName, String countryCode, String emailAddress,
			String jobTitle, String name, String password,
			ServiceContext serviceContext)
		throws PortalException {

		long osbCommerceProvisioningSiteGroupId =
			_getOSBCommerceProvisioningSiteGroupId(
				serviceContext.getCompanyId());

		User user = _addUser(
			serviceContext.getCompanyId(), emailAddress, jobTitle, name,
			osbCommerceProvisioningSiteGroupId, password);

		serviceContext.setUserId(user.getUserId());

		CommerceAccount commerceAccount = _addCommerceAccount(
			emailAddress, commerceAccountName, serviceContext);

		long commerceChannelGroupId = _getCommerceChannelGroupId(
			osbCommerceProvisioningSiteGroupId);

		CommerceOrder commerceOrder = _addCommerceOrder(
			commerceAccount.getCommerceAccountId(), commerceChannelGroupId,
			countryCode, name, osbCommerceProvisioningSiteGroupId,
			serviceContext);

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user));

		long commerceOrderItemId = -1;

		try {
			commerceOrderItemId = _addCommerceOrderItem(
				commerceAccount.getCommerceAccountId(), commerceChannelGroupId,
				commerceOrder.getCommerceOrderId(), serviceContext);

			_commerceOrderEngine.checkoutCommerceOrder(
				commerceOrder, user.getUserId());
		}
		finally {
			PermissionThreadLocal.setPermissionChecker(permissionChecker);
		}

		_commerceNotificationHelper.sendNotifications(
			commerceChannelGroupId, user.getUserId(),
			OSBCommerceNotificationConstants.
				OSB_COMMERCE_PROVISIONING_ACCOUNT_CREATED,
			commerceAccount);

		return commerceOrderItemId;
	}

	private CommerceAccount _addCommerceAccount(
			String emailAddress, String name, ServiceContext serviceContext)
		throws PortalException {

		return _commerceAccountLocalService.addBusinessCommerceAccount(
			name, -1, emailAddress, null, true, null,
			new long[] {serviceContext.getUserId()}, null, serviceContext);
	}

	private CommerceOrder _addCommerceOrder(
			long commerceAccountId, long commerceChannelGroupId,
			String countryCode, String name,
			long osbCommerceProvisioningSiteGroupId,
			ServiceContext serviceContext)
		throws PortalException {

		CommerceChannel commerceChannel =
			_commerceChannelLocalService.fetchCommerceChannelBySiteGroupId(
				osbCommerceProvisioningSiteGroupId);

		CommerceCurrency commerceCurrency =
			_commerceCurrencyLocalService.getCommerceCurrency(
				serviceContext.getCompanyId(),
				commerceChannel.getCommerceCurrencyCode());

		CommerceCountry commerceCountry =
			_commerceCountryLocalService.fetchCommerceCountry(
				serviceContext.getCompanyId(), countryCode);

		CommerceAddress commerceAddress =
			_commerceAddressLocalService.addCommerceAddress(
				CommerceAccount.class.getName(), commerceAccountId, name, null,
				"street1", null, null, "city", "zip", -1,
				commerceCountry.getCommerceCountryId(), null,
				CommerceAddressConstants.ADDRESS_TYPE_BILLING, serviceContext);

		return _commerceOrderLocalService.addCommerceOrder(
			serviceContext.getUserId(), commerceChannelGroupId,
			commerceAccountId, commerceCurrency.getCommerceCurrencyId(),
			commerceAddress.getCommerceAddressId(), -1, null, -1, null, null,
			new BigDecimal(0), new BigDecimal(0), new BigDecimal(0),
			CommerceOrderPaymentConstants.STATUS_COMPLETED,
			CommerceOrderConstants.ORDER_STATUS_OPEN, serviceContext);
	}

	private long _addCommerceOrderItem(
			long commerceAccountId, long commerceChannelGroupId,
			long commerceOrderId, ServiceContext serviceContext)
		throws PortalException {

		CPInstance cpInstance =
			_cpInstanceLocalService.fetchByExternalReferenceCode(
				serviceContext.getCompanyId(),
				OSBCommerceProvisioningConstants.
					TRIAL_PLAN_EXTERNAL_REFERENCE_CODE);

		CommerceContext commerceContext = _commerceContextFactory.create(
			serviceContext.getCompanyId(), commerceChannelGroupId,
			serviceContext.getUserId(), commerceOrderId, commerceAccountId);

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.addCommerceOrderItem(
				commerceOrderId, cpInstance.getCPInstanceId(), 1, 0, null,
				commerceContext, serviceContext);

		return commerceOrderItem.getCommerceOrderItemId();
	}

	private User _addUser(
			long companyId, String emailAddress, String jobTitle, String name,
			long osbCommerceProvisioningSiteGroupId, String password)
		throws PortalException {

		String[] nameItems = name.split(" ");

		String firstName = nameItems[0];
		String lastName = (nameItems.length > 1) ? nameItems[1] : nameItems[0];

		return _userLocalService.addUser(
			0, companyId, false, password, password, true, null, emailAddress,
			0, null, LocaleUtil.ENGLISH, firstName, null, lastName, 0, 0, true,
			1, 1, 1970, jobTitle,
			new long[] {osbCommerceProvisioningSiteGroupId}, null, null, null,
			false, new ServiceContext());
	}

	private void _checkUser(long companyId, String emailAddress)
		throws UserEmailAddressException.MustNotBeDuplicate {

		User user = _userLocalService.fetchUserByEmailAddress(
			companyId, emailAddress);

		if (user != null) {
			throw new UserEmailAddressException.MustNotBeDuplicate(
				companyId, emailAddress);
		}
	}

	private long _getCommerceChannelGroupId(
			long osbCommerceProvisioningSiteGroupId)
		throws PortalException {

		return _commerceChannelLocalService.
			getCommerceChannelGroupIdBySiteGroupId(
				osbCommerceProvisioningSiteGroupId);
	}

	private long _getOSBCommerceProvisioningSiteGroupId(long companyId)
		throws PortalException {

		Group osbCommerceProvisioningSiteGroup =
			_groupLocalService.getFriendlyURLGroup(
				companyId,
				OSBCommerceProvisioningConstants.
					FRIENDLY_URL_OSB_COMMERCE_PROVISIONING);

		return osbCommerceProvisioningSiteGroup.getGroupId();
	}

	private long _invoke(Callable<Long> callable) throws Throwable {
		return TransactionInvokerUtil.invoke(_transactionConfig, callable);
	}

	private void _sendRedirect(
			ActionResponse actionResponse, long commerceOrderItemId,
			String name)
		throws Exception {

		String userFirstName = name.split(" ")[0];

		RenderURL renderURL = actionResponse.createRedirectURL(
			MimeResponse.Copy.NONE);

		renderURL.setParameter(
			"mvcRenderCommandName", "initializePortalInstance");
		renderURL.setParameter(
			"commerceOrderItemId", String.valueOf(commerceOrderItemId));
		renderURL.setParameter("userFirstName", userFirstName);

		actionResponse.sendRedirect(renderURL.toString());
	}

	private static final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRED, new Class<?>[] {Exception.class});

	@Reference
	private CommerceAccountLocalService _commerceAccountLocalService;

	@Reference
	private CommerceAddressLocalService _commerceAddressLocalService;

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceCountryLocalService _commerceCountryLocalService;

	@Reference
	private CommerceCurrencyLocalService _commerceCurrencyLocalService;

	@Reference
	private CommerceNotificationHelper _commerceNotificationHelper;

	@Reference
	private CommerceOrderEngine _commerceOrderEngine;

	@Reference
	private CommerceOrderItemLocalService _commerceOrderItemLocalService;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private UserLocalService _userLocalService;

}