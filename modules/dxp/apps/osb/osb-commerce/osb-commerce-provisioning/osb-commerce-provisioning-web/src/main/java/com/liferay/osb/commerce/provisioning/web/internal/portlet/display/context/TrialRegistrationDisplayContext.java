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

package com.liferay.osb.commerce.provisioning.web.internal.portlet.display.context;

import com.liferay.commerce.model.CommerceCountry;
import com.liferay.commerce.model.CommerceSubscriptionEntry;
import com.liferay.commerce.product.constants.CPConstants;
import com.liferay.commerce.service.CommerceCountryService;
import com.liferay.commerce.service.CommerceSubscriptionEntryLocalService;
import com.liferay.osb.commerce.provisioning.constants.OSBCommercePortalInstanceConstants;
import com.liferay.osb.commerce.provisioning.util.OSBCommercePortalInstance;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.util.List;
import java.util.Objects;

/**
 * @author Gianmarco Brunialti Masera
 */
public class TrialRegistrationDisplayContext {

	public TrialRegistrationDisplayContext(
		CommerceCountryService commerceCountryService,
		CommerceSubscriptionEntryLocalService
			commerceSubscriptionEntryLocalService,
		OSBCommercePortalInstance osbCommercePortalInstance) {

		_commerceCountryService = commerceCountryService;
		_commerceSubscriptionEntryLocalService =
			commerceSubscriptionEntryLocalService;
		_osbCommercePortalInstance = osbCommercePortalInstance;
	}

	public List<CommerceCountry> getCommerceCountries(long companyId) {
		return _commerceCountryService.getCommerceCountries(companyId, true);
	}

	public String getPortalInstanceURL(long commerceOrderItemId) {
		CommerceSubscriptionEntry commerceSubscriptionEntry =
			_commerceSubscriptionEntryLocalService.
				fetchCommerceSubscriptionEntryByCommerceOrderItemId(
					commerceOrderItemId);

		UnicodeProperties unicodeProperties =
			commerceSubscriptionEntry.getSubscriptionTypeSettingsProperties();

		return _osbCommercePortalInstance.getPortalInstanceURL(
			unicodeProperties.get(
				OSBCommercePortalInstanceConstants.
					PORTAL_INSTANCE_VIRTUAL_HOSTNAME));
	}

	public int getTrialLengthInDays(long commerceOrderItemId) {
		CommerceSubscriptionEntry commerceSubscriptionEntry =
			_commerceSubscriptionEntryLocalService.
				fetchCommerceSubscriptionEntryByCommerceOrderItemId(
					commerceOrderItemId);

		int trialLength = commerceSubscriptionEntry.getSubscriptionLength();

		if (Objects.equals(
				commerceSubscriptionEntry.getSubscriptionType(),
				CPConstants.WEEKLY_SUBSCRIPTION_TYPE)) {

			trialLength *= 7;
		}
		else if (Objects.equals(
					commerceSubscriptionEntry.getSubscriptionType(),
					CPConstants.MONTHLY_SUBSCRIPTION_TYPE)) {

			trialLength *= 30;
		}

		return trialLength;
	}

	private final CommerceCountryService _commerceCountryService;
	private final CommerceSubscriptionEntryLocalService
		_commerceSubscriptionEntryLocalService;
	private final OSBCommercePortalInstance _osbCommercePortalInstance;

}