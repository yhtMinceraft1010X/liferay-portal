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

package com.liferay.commerce.account.web.internal.portlet.action;

import com.liferay.account.constants.AccountPortletKeys;
import com.liferay.commerce.exception.NoSuchShippingOptionAccountEntryRelException;
import com.liferay.commerce.model.CommerceShippingMethod;
import com.liferay.commerce.model.CommerceShippingOptionAccountEntryRel;
import com.liferay.commerce.service.CommerceShippingMethodService;
import com.liferay.commerce.service.CommerceShippingOptionAccountEntryRelService;
import com.liferay.commerce.shipping.engine.fixed.model.CommerceShippingFixedOption;
import com.liferay.commerce.shipping.engine.fixed.service.CommerceShippingFixedOptionService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false, immediate = true,
	property = {
		"javax.portlet.name=" + AccountPortletKeys.ACCOUNT_ENTRIES_ADMIN,
		"javax.portlet.name=" + AccountPortletKeys.ACCOUNT_ENTRIES_MANAGEMENT,
		"mvc.command.name=/account_entries_admin/edit_account_entry_commerce_shipping_option"
	},
	service = MVCActionCommand.class
)
public class EditAccountEntryCommerceShippingOptionMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.UPDATE)) {
				_updateCommerceShippingOptionAccountEntryRel(actionRequest);
			}
		}
		catch (Exception exception) {
			if (exception instanceof
					NoSuchShippingOptionAccountEntryRelException ||
				exception instanceof PrincipalException) {

				SessionErrors.add(actionRequest, exception.getClass());
			}

			_log.error(exception);
		}
	}

	private CommerceShippingOptionAccountEntryRel
			_updateCommerceShippingOptionAccountEntryRel(
				ActionRequest actionRequest)
		throws Exception {

		long accountEntryId = ParamUtil.getLong(
			actionRequest, "accountEntryId");
		long commerceChannelId = ParamUtil.getLong(
			actionRequest, "commerceChannelId");
		long commerceShippingFixedOptionId = ParamUtil.getLong(
			actionRequest, "commerceShippingFixedOptionId");

		CommerceShippingOptionAccountEntryRel
			commerceShippingOptionAccountEntryRel =
				_commerceShippingOptionAccountEntryRelService.
					fetchCommerceShippingOptionAccountEntryRel(
						accountEntryId, commerceChannelId);

		CommerceShippingFixedOption commerceShippingFixedOption =
			_commerceShippingFixedOptionService.
				fetchCommerceShippingFixedOption(commerceShippingFixedOptionId);

		if ((commerceShippingFixedOption == null) &&
			(commerceShippingOptionAccountEntryRel != null)) {

			_commerceShippingOptionAccountEntryRelService.
				deleteCommerceShippingOptionAccountEntryRel(
					commerceShippingOptionAccountEntryRel.
						getCommerceShippingOptionAccountEntryRelId());

			return null;
		}

		CommerceShippingMethod commerceShippingMethod =
			_commerceShippingMethodService.getCommerceShippingMethod(
				commerceShippingFixedOption.getCommerceShippingMethodId());

		if (commerceShippingOptionAccountEntryRel == null) {
			return _commerceShippingOptionAccountEntryRelService.
				addCommerceShippingOptionAccountEntryRel(
					accountEntryId, commerceChannelId,
					commerceShippingMethod.getEngineKey(),
					commerceShippingFixedOption.getKey());
		}

		return _commerceShippingOptionAccountEntryRelService.
			updateCommerceShippingOptionAccountEntryRel(
				commerceShippingOptionAccountEntryRel.
					getCommerceShippingOptionAccountEntryRelId(),
				commerceShippingMethod.getEngineKey(),
				commerceShippingFixedOption.getKey());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EditAccountEntryCommerceShippingOptionMVCActionCommand.class);

	@Reference
	private CommerceShippingFixedOptionService
		_commerceShippingFixedOptionService;

	@Reference
	private CommerceShippingMethodService _commerceShippingMethodService;

	@Reference
	private CommerceShippingOptionAccountEntryRelService
		_commerceShippingOptionAccountEntryRelService;

}