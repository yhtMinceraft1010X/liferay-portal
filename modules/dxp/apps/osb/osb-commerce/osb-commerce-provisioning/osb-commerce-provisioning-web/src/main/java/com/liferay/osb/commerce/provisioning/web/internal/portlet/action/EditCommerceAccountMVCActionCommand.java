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

import com.liferay.commerce.account.exception.CommerceAccountNameException;
import com.liferay.commerce.account.exception.CommerceAccountOrdersException;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountService;
import com.liferay.commerce.constants.CommerceAddressConstants;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.osb.commerce.provisioning.web.internal.constants.OSBCommerceProvisioningPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;

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
		"javax.portlet.name=" + OSBCommerceProvisioningPortletKeys.BILLING,
		"mvc.command.name=editCommerceAccount"
	},
	service = MVCActionCommand.class
)
public class EditCommerceAccountMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			long commerceAccountId = ParamUtil.getLong(
				actionRequest, "commerceAccountId");

			long commerceAddressId = _updateCommerceAddress(
				actionRequest, commerceAccountId);

			_updateCommerceAccount(
				actionRequest, commerceAccountId, commerceAddressId);
		}
		catch (Exception exception) {
			if (exception instanceof CommerceAccountNameException ||
				exception instanceof CommerceAccountOrdersException) {

				hideDefaultErrorMessage(actionRequest);

				SessionErrors.add(actionRequest, exception.getClass());
			}
			else {
				throw exception;
			}
		}
	}

	private void _updateCommerceAccount(
			ActionRequest actionRequest, long commerceAccountId,
			long commerceAddressId)
		throws Exception {

		String name = ParamUtil.getString(actionRequest, "name");
		String email = ParamUtil.getString(actionRequest, "email");
		String taxId = ParamUtil.getString(actionRequest, "taxId");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			CommerceAccount.class.getName(), actionRequest);

		_commerceAccountService.updateCommerceAccount(
			commerceAccountId, name, false, null, email, taxId, true,
			commerceAddressId, 0, serviceContext);
	}

	private long _updateCommerceAddress(
			ActionRequest actionRequest, long commerceAccountId)
		throws Exception {

		CommerceAddress commerceAddress = null;

		String name = ParamUtil.getString(actionRequest, "name");
		String street1 = ParamUtil.getString(actionRequest, "street1");
		String city = ParamUtil.getString(actionRequest, "city");
		String zip = ParamUtil.getString(actionRequest, "zip");
		long commerceRegionId = ParamUtil.getLong(
			actionRequest, "commerceRegionId");
		long commerceCountryId = ParamUtil.getLong(
			actionRequest, "commerceCountryId");

		CommerceAccount commerceAccount =
			_commerceAccountService.getCommerceAccount(commerceAccountId);

		if (commerceAccount.getDefaultBillingAddressId() == 0) {
			commerceAddress = _commerceAddressService.addCommerceAddress(
				CommerceAccount.class.getName(), commerceAccountId, name, null,
				street1, null, null, city, zip, commerceRegionId,
				commerceCountryId, null,
				CommerceAddressConstants.ADDRESS_TYPE_BILLING,
				ServiceContextFactory.getInstance(
					CommerceAddress.class.getName(), actionRequest));
		}
		else {
			commerceAddress = _commerceAddressService.updateCommerceAddress(
				commerceAccount.getDefaultBillingAddressId(), name, null,
				street1, null, null, city, zip, commerceRegionId,
				commerceCountryId, null,
				CommerceAddressConstants.ADDRESS_TYPE_BILLING, null);
		}

		return commerceAddress.getCommerceAddressId();
	}

	@Reference
	private CommerceAccountService _commerceAccountService;

	@Reference
	private CommerceAddressService _commerceAddressService;

}