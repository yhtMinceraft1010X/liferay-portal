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

package com.liferay.osb.commerce.provisioning.theme.internal.helper;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.util.CommerceAccountHelper;
import com.liferay.commerce.model.CommerceSubscriptionEntry;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceSubscriptionEntryLocalService;
import com.liferay.osb.commerce.provisioning.OSBCommercePortalInstanceStatus;
import com.liferay.osb.commerce.provisioning.constants.OSBCommercePortalInstanceConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 */
@Component(immediate = true, service = OSBCommerceThemeHttpHelper.class)
public class OSBCommerceThemeHttpHelper {

	public long getCommerceChannelGroupId(long commerceChannelId)
		throws PortalException {

		CommerceChannel commerceChannel =
			_commerceChannelLocalService.getCommerceChannel(commerceChannelId);

		return commerceChannel.getGroupId();
	}

	public long getCurrentCommerceAccountId(
			HttpServletRequest httpServletRequest)
		throws PortalException {

		CommerceAccount commerceAccount =
			_commerceAccountHelper.getCurrentCommerceAccount(
				httpServletRequest);

		if (commerceAccount == null) {
			return -1;
		}

		return commerceAccount.getCommerceAccountId();
	}

	public boolean showTrialButton(HttpServletRequest httpServletRequest)
		throws PortalException {

		CommerceSubscriptionEntry commerceSubscriptionEntry =
			_getCommerceSubscriptionEntry(
				getCurrentCommerceAccountId(httpServletRequest));

		if (commerceSubscriptionEntry == null) {
			return false;
		}

		UnicodeProperties subscriptionTypeSettingsUnicodeProperties =
			commerceSubscriptionEntry.getSubscriptionTypeSettingsProperties();

		OSBCommercePortalInstanceStatus portalInstanceStatus =
			OSBCommercePortalInstanceStatus.parse(
				subscriptionTypeSettingsUnicodeProperties.get(
					OSBCommercePortalInstanceConstants.PORTAL_INSTANCE_STATUS));

		if ((OSBCommercePortalInstanceStatus.ACTIVE == portalInstanceStatus) ||
			(OSBCommercePortalInstanceStatus.IN_PROGRESS ==
				portalInstanceStatus)) {

			return false;
		}

		return true;
	}

	private CommerceSubscriptionEntry _getCommerceSubscriptionEntry(
			long commerceAccountId)
		throws PortalException {

		List<CommerceSubscriptionEntry> commerceSubscriptionEntries =
			_commerceSubscriptionEntryLocalService.
				getActiveCommerceSubscriptionEntries(commerceAccountId);

		if (!commerceSubscriptionEntries.isEmpty()) {
			return commerceSubscriptionEntries.get(0);
		}

		return null;
	}

	@Reference
	private CommerceAccountHelper _commerceAccountHelper;

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceSubscriptionEntryLocalService
		_commerceSubscriptionEntryLocalService;

}