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

package com.liferay.commerce.shipping.engine.fixed.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CommerceShippingFixedOptionService}.
 *
 * @author Alessio Antonio Rendina
 * @see CommerceShippingFixedOptionService
 * @generated
 */
public class CommerceShippingFixedOptionServiceWrapper
	implements CommerceShippingFixedOptionService,
			   ServiceWrapper<CommerceShippingFixedOptionService> {

	public CommerceShippingFixedOptionServiceWrapper() {
		this(null);
	}

	public CommerceShippingFixedOptionServiceWrapper(
		CommerceShippingFixedOptionService commerceShippingFixedOptionService) {

		_commerceShippingFixedOptionService =
			commerceShippingFixedOptionService;
	}

	@Override
	public
		com.liferay.commerce.shipping.engine.fixed.model.
			CommerceShippingFixedOption addCommerceShippingFixedOption(
					long groupId, long commerceShippingMethodId,
					java.math.BigDecimal amount,
					java.util.Map<java.util.Locale, String> descriptionMap,
					String key, java.util.Map<java.util.Locale, String> nameMap,
					double priority)
				throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionService.
			addCommerceShippingFixedOption(
				groupId, commerceShippingMethodId, amount, descriptionMap, key,
				nameMap, priority);
	}

	@Override
	public void deleteCommerceShippingFixedOption(
			long commerceShippingFixedOptionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		_commerceShippingFixedOptionService.deleteCommerceShippingFixedOption(
			commerceShippingFixedOptionId);
	}

	@Override
	public
		com.liferay.commerce.shipping.engine.fixed.model.
			CommerceShippingFixedOption fetchCommerceShippingFixedOption(
					long commerceShippingFixedOptionId)
				throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionService.
			fetchCommerceShippingFixedOption(commerceShippingFixedOptionId);
	}

	@Override
	public
		com.liferay.commerce.shipping.engine.fixed.model.
			CommerceShippingFixedOption fetchCommerceShippingFixedOption(
					long companyId, String key)
				throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionService.
			fetchCommerceShippingFixedOption(companyId, key);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.shipping.engine.fixed.model.
			CommerceShippingFixedOption> getCommerceShippingFixedOptions(
					long commerceShippingMethodId, int start, int end)
				throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionService.
			getCommerceShippingFixedOptions(
				commerceShippingMethodId, start, end);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.shipping.engine.fixed.model.
			CommerceShippingFixedOption> getCommerceShippingFixedOptions(
					long commerceShippingMethodId, int start, int end,
					com.liferay.portal.kernel.util.OrderByComparator
						<com.liferay.commerce.shipping.engine.fixed.model.
							CommerceShippingFixedOption> orderByComparator)
				throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionService.
			getCommerceShippingFixedOptions(
				commerceShippingMethodId, start, end, orderByComparator);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.shipping.engine.fixed.model.
			CommerceShippingFixedOption> getCommerceShippingFixedOptions(
					long companyId, long groupId, long commerceShippingMethodId,
					String keywords, int start, int end)
				throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionService.
			getCommerceShippingFixedOptions(
				companyId, groupId, commerceShippingMethodId, keywords, start,
				end);
	}

	@Override
	public int getCommerceShippingFixedOptionsCount(
			long commerceShippingMethodId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionService.
			getCommerceShippingFixedOptionsCount(commerceShippingMethodId);
	}

	@Override
	public long getCommerceShippingFixedOptionsCount(
			long companyId, long groupId, long commerceShippingMethodId,
			String keywords)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionService.
			getCommerceShippingFixedOptionsCount(
				companyId, groupId, commerceShippingMethodId, keywords);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _commerceShippingFixedOptionService.getOSGiServiceIdentifier();
	}

	@Override
	public
		com.liferay.commerce.shipping.engine.fixed.model.
			CommerceShippingFixedOption updateCommerceShippingFixedOption(
					long commerceShippingFixedOptionId,
					java.math.BigDecimal amount,
					java.util.Map<java.util.Locale, String> descriptionMap,
					String key, java.util.Map<java.util.Locale, String> nameMap,
					double priority)
				throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionService.
			updateCommerceShippingFixedOption(
				commerceShippingFixedOptionId, amount, descriptionMap, key,
				nameMap, priority);
	}

	@Override
	public CommerceShippingFixedOptionService getWrappedService() {
		return _commerceShippingFixedOptionService;
	}

	@Override
	public void setWrappedService(
		CommerceShippingFixedOptionService commerceShippingFixedOptionService) {

		_commerceShippingFixedOptionService =
			commerceShippingFixedOptionService;
	}

	private CommerceShippingFixedOptionService
		_commerceShippingFixedOptionService;

}