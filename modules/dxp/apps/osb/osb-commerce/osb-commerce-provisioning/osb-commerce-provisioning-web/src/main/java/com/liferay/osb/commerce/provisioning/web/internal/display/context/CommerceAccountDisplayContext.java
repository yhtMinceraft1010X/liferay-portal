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

package com.liferay.osb.commerce.provisioning.web.internal.display.context;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountService;
import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceCountry;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.commerce.service.CommerceCountryService;
import com.liferay.osb.commerce.provisioning.web.internal.display.context.util.CommerceAccountRequestHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ivica Cardic
 */
public class CommerceAccountDisplayContext {

	public CommerceAccountDisplayContext(
		CommerceAccountService commerceAccountService,
		CommerceAddressService commerceAddressService,
		CommerceCountryService commerceCountryService,
		HttpServletRequest httpServletRequest,
		UserLocalService userLocalService) {

		_commerceAccountService = commerceAccountService;
		_commerceAddressService = commerceAddressService;
		_commerceCountryService = commerceCountryService;
		_userLocalService = userLocalService;

		_commerceAccountRequestHelper = new CommerceAccountRequestHelper(
			httpServletRequest);

		_commerceContext = (CommerceContext)httpServletRequest.getAttribute(
			CommerceWebKeys.COMMERCE_CONTEXT);
	}

	public CommerceAddress getBillingCommerceAddress() throws PortalException {
		CommerceAddress commerceAddress = null;

		CommerceAccount commerceAccount = getCurrentCommerceAccount();

		if (commerceAccount.getDefaultBillingAddressId() != 0) {
			commerceAddress = _commerceAddressService.getCommerceAddress(
				commerceAccount.getDefaultBillingAddressId());
		}

		return commerceAddress;
	}

	public List<CommerceCountry> getCommerceCountries(long companyId) {
		return _commerceCountryService.getCommerceCountries(companyId, true);
	}

	public CommerceAccount getCurrentCommerceAccount() throws PortalException {
		long commerceAccountId = ParamUtil.getLong(
			_commerceAccountRequestHelper.getRequest(), "commerceAccountId");

		if (commerceAccountId > 0) {
			return _commerceAccountService.getCommerceAccount(
				commerceAccountId);
		}

		return _commerceContext.getCommerceAccount();
	}

	public User getSelectedUser() throws PortalException {
		return _userLocalService.getUser(
			_commerceAccountRequestHelper.getUserId());
	}

	private final CommerceAccountRequestHelper _commerceAccountRequestHelper;
	private final CommerceAccountService _commerceAccountService;
	private final CommerceAddressService _commerceAddressService;
	private final CommerceContext _commerceContext;
	private final CommerceCountryService _commerceCountryService;
	private final UserLocalService _userLocalService;

}