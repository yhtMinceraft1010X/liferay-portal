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

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.util.CommerceAccountHelper;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.model.CommerceSubscriptionEntry;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.commerce.service.CommerceSubscriptionEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;

import java.text.Format;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Gianmarco Brunialti Masera
 * @author Ivica Cardic
 */
public class PlanManagementDisplayContext {

	public PlanManagementDisplayContext(
		CommerceAccountHelper commerceAccountHelper,
		CommerceOrderItemLocalService commerceOrderItemLocalService,
		CommerceSubscriptionEntryLocalService
			commerceSubscriptionEntryLocalService,
		Portal portal, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		_commerceAccountHelper = commerceAccountHelper;
		_commerceOrderItemLocalService = commerceOrderItemLocalService;
		_commerceSubscriptionEntryLocalService =
			commerceSubscriptionEntryLocalService;
		_portal = portal;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
	}

	public Map<String, Object> getPlanManagementData(ThemeDisplay themeDisplay)
		throws PortalException {

		return HashMapBuilder.<String, Object>put(
			"activePlan", _getActivePlanData(themeDisplay)
		).put(
			"spritemap", _getSpritemap(themeDisplay)
		).build();
	}

	private Map<String, Object> _getActivePlanData(ThemeDisplay themeDisplay)
		throws PortalException {

		CommerceSubscriptionEntry commerceSubscriptionEntry =
			_getCommerceSubscriptionEntry(_getCurrentCommerceAccountId());

		if (commerceSubscriptionEntry == null) {
			return Collections.emptyMap();
		}

		Format format = FastDateFormatFactoryUtil.getSimpleDateFormat(
			"MMMM d, yyyy", themeDisplay.getLocale());

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.getCommerceOrderItem(
				commerceSubscriptionEntry.getCommerceOrderItemId());

		return HashMapBuilder.<String, Object>put(
			"cancelPlanURL",
			_getCancelSubscriptionURL(commerceSubscriptionEntry)
		).put(
			"endDate",
			format.format(commerceSubscriptionEntry.getNextIterationDate())
		).put(
			"planName", commerceOrderItem.getName(themeDisplay.getLocale())
		).put(
			"planPrice",
			() -> {
				CommerceMoney commerceMoney =
					commerceOrderItem.getFinalPriceMoney();

				return commerceMoney.format(themeDisplay.getLocale());
			}
		).put(
			"recurrence", commerceSubscriptionEntry.getSubscriptionType()
		).put(
			"startDate", format.format(commerceSubscriptionEntry.getStartDate())
		).build();
	}

	private String _getCancelSubscriptionURL(
		CommerceSubscriptionEntry commerceSubscriptionEntry) {

		PortletURL portletURL = _renderResponse.createActionURL();

		portletURL.setParameter(ActionRequest.ACTION_NAME, "updatePlan");
		portletURL.setParameter(Constants.CMD, Constants.DELETE);
		portletURL.setParameter(
			"redirect", _portal.getCurrentURL(_renderRequest));
		portletURL.setParameter(
			"commerceSubscriptionEntryId",
			String.valueOf(
				commerceSubscriptionEntry.getCommerceSubscriptionEntryId()));

		return portletURL.toString();
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

	private long _getCurrentCommerceAccountId() throws PortalException {
		CommerceAccount commerceAccount =
			_commerceAccountHelper.getCurrentCommerceAccount(
				_portal.getHttpServletRequest(_renderRequest));

		if (commerceAccount == null) {
			return -1;
		}

		return commerceAccount.getCommerceAccountId();
	}

	private String _getSpritemap(ThemeDisplay themeDisplay) {
		return themeDisplay.getPathThemeImages() + "/lexicon/icons.svg";
	}

	private final CommerceAccountHelper _commerceAccountHelper;
	private final CommerceOrderItemLocalService _commerceOrderItemLocalService;
	private final CommerceSubscriptionEntryLocalService
		_commerceSubscriptionEntryLocalService;
	private final Portal _portal;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;

}