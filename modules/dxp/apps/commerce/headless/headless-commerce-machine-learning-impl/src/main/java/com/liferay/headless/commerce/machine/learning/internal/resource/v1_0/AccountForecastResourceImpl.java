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

package com.liferay.headless.commerce.machine.learning.internal.resource.v1_0;

import com.liferay.commerce.machine.learning.forecast.CommerceAccountCommerceMLForecast;
import com.liferay.commerce.machine.learning.forecast.CommerceAccountCommerceMLForecastManager;
import com.liferay.headless.commerce.machine.learning.dto.v1_0.AccountForecast;
import com.liferay.headless.commerce.machine.learning.internal.constants.CommerceMLForecastConstants;
import com.liferay.headless.commerce.machine.learning.internal.dto.v1_0.converter.AccountForecastDTOConverter;
import com.liferay.headless.commerce.machine.learning.internal.dto.v1_0.converter.CommerceMLForecastCompositeResourcePrimaryKey;
import com.liferay.headless.commerce.machine.learning.internal.helper.v1_0.CommerceAccountPermissionHelper;
import com.liferay.headless.commerce.machine.learning.resource.v1_0.AccountForecastResource;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Riccardo Ferrari
 */
@Component(
	enabled = false,
	properties = "OSGI-INF/liferay/rest/v1_0/account-forecast.properties",
	scope = ServiceScope.PROTOTYPE, service = AccountForecastResource.class
)
public class AccountForecastResourceImpl
	extends BaseAccountForecastResourceImpl {

	@Override
	public void create(
			Collection<AccountForecast> accountForecasts,
			Map<String, Serializable> parameters)
		throws Exception {

		contextBatchUnsafeConsumer.accept(
			accountForecasts,
			accountForecast -> {
				CommerceAccountCommerceMLForecast
					commerceAccountCommerceMLForecast =
						_commerceAccountCommerceMLForecastManager.create();

				if (accountForecast.getActual() != null) {
					commerceAccountCommerceMLForecast.setActual(
						accountForecast.getActual());
				}

				commerceAccountCommerceMLForecast.setCommerceAccountId(
					accountForecast.getAccount());
				commerceAccountCommerceMLForecast.setCompanyId(
					contextCompany.getCompanyId());
				commerceAccountCommerceMLForecast.setForecast(
					accountForecast.getForecast());
				commerceAccountCommerceMLForecast.setForecastLowerBound(
					accountForecast.getForecastLowerBound());
				commerceAccountCommerceMLForecast.setForecastUpperBound(
					accountForecast.getForecastUpperBound());
				commerceAccountCommerceMLForecast.setPeriod("month");
				commerceAccountCommerceMLForecast.setScope("commerce-account");
				commerceAccountCommerceMLForecast.setTarget("revenue");
				commerceAccountCommerceMLForecast.setTimestamp(
					accountForecast.getTimestamp());

				_commerceAccountCommerceMLForecastManager.
					addCommerceAccountCommerceMLForecast(
						commerceAccountCommerceMLForecast);
			});
	}

	@Override
	public Page<AccountForecast> getAccountForecastsByMonthlyRevenuePage(
			Long[] accountIds, Integer forecastLength, Date forecastStartDate,
			Integer historyLength, Pagination pagination)
		throws Exception {

		List<Long> commerceAccountIds =
			_commerceAccountPermissionHelper.filterCommerceAccountIds(
				Arrays.asList(accountIds));

		if (commerceAccountIds.isEmpty()) {
			return Page.of(Collections.emptyList());
		}

		Date startDate = forecastStartDate;

		if (startDate == null) {
			startDate = new Date();
		}

		if (historyLength == null) {
			historyLength = CommerceMLForecastConstants.HISTORY_LENGTH_DEFAULT;
		}

		if (forecastLength == null) {
			forecastLength =
				CommerceMLForecastConstants.FORECAST_LENGTH_DEFAULT;
		}

		return Page.of(
			transform(
				_commerceAccountCommerceMLForecastManager.
					getMonthlyRevenueCommerceAccountCommerceMLForecasts(
						contextCompany.getCompanyId(),
						ArrayUtil.toLongArray(commerceAccountIds), startDate,
						historyLength, forecastLength,
						pagination.getStartPosition(),
						pagination.getEndPosition()),
				commerceAccountCommerceMLForecast ->
					_accountForecastDTOConverter.toDTO(
						new DefaultDTOConverterContext(
							new CommerceMLForecastCompositeResourcePrimaryKey(
								commerceAccountCommerceMLForecast.
									getCompanyId(),
								commerceAccountCommerceMLForecast.
									getForecastId()),
							contextAcceptLanguage.getPreferredLocale()))),
			pagination,
			_commerceAccountCommerceMLForecastManager.
				getMonthlyRevenueCommerceAccountCommerceMLForecastsCount(
					contextCompany.getCompanyId(),
					ArrayUtil.toLongArray(commerceAccountIds), startDate,
					historyLength, forecastLength));
	}

	@Reference
	private AccountForecastDTOConverter _accountForecastDTOConverter;

	@Reference
	private CommerceAccountCommerceMLForecastManager
		_commerceAccountCommerceMLForecastManager;

	@Reference
	private CommerceAccountPermissionHelper _commerceAccountPermissionHelper;

}