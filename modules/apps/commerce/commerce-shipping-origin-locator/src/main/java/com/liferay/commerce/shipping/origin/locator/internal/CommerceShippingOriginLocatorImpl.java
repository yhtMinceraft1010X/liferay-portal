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

package com.liferay.commerce.shipping.origin.locator.internal;

import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseLocalService;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceGeocoder;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.service.CommerceAddressLocalService;
import com.liferay.commerce.shipping.origin.locator.CommerceShippingOriginLocator;
import com.liferay.commerce.shipping.origin.locator.internal.util.DistanceCalculator;
import com.liferay.portal.kernel.model.Country;
import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.service.CountryLocalService;
import com.liferay.portal.kernel.service.RegionLocalService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Di Giorgi
 * @author Alessio Antonio Rendina
 * @author Ethan Bustad
 */
@Component(
	enabled = false, immediate = true,
	service = CommerceShippingOriginLocator.class
)
public class CommerceShippingOriginLocatorImpl
	implements CommerceShippingOriginLocator {

	@Override
	public Map<CommerceAddress, List<CommerceOrderItem>> getOriginAddresses(
			CommerceOrder commerceOrder)
		throws Exception {

		CommerceAddress commerceAddress = commerceOrder.getShippingAddress();

		if (commerceAddress == null) {
			return Collections.emptyMap();
		}

		Map<CommerceInventoryWarehouse, List<CommerceOrderItem>>
			commerceInventoryWarehouseOrderItemsMap = new HashMap<>();

		List<CommerceOrderItem> commerceOrderItems =
			commerceOrder.getCommerceOrderItems();

		for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
			CommerceInventoryWarehouse commerceInventoryWarehouse =
				_getClosestCommerceInventoryWarehouse(
					commerceOrder.getGroupId(), commerceAddress,
					commerceOrderItem.getSku());

			List<CommerceOrderItem> commerceInventoryWarehouseOrderItems =
				commerceInventoryWarehouseOrderItemsMap.get(
					commerceInventoryWarehouse);

			if (commerceInventoryWarehouseOrderItems == null) {
				commerceInventoryWarehouseOrderItems = new ArrayList<>(
					commerceOrderItems.size());

				commerceInventoryWarehouseOrderItemsMap.put(
					commerceInventoryWarehouse,
					commerceInventoryWarehouseOrderItems);
			}

			commerceInventoryWarehouseOrderItems.add(commerceOrderItem);
		}

		Map<CommerceAddress, List<CommerceOrderItem>> originAddress =
			new HashMap<>();

		for (Map.Entry<CommerceInventoryWarehouse, List<CommerceOrderItem>>
				entry : commerceInventoryWarehouseOrderItemsMap.entrySet()) {

			CommerceInventoryWarehouse commerceInventoryWarehouse =
				entry.getKey();

			originAddress.put(
				_getCommerceAddress(
					commerceInventoryWarehouse, commerceOrder.getCompanyId()),
				entry.getValue());
		}

		return originAddress;
	}

	private CommerceInventoryWarehouse _getClosestCommerceInventoryWarehouse(
			long groupId, CommerceAddress commerceAddress, String sku)
		throws Exception {

		List<CommerceInventoryWarehouse> commerceInventoryWarehouses =
			_commerceInventoryWarehouseLocalService.
				getCommerceInventoryWarehouses(groupId, sku);

		CommerceInventoryWarehouse closestCommerceInventoryWarehouse = null;
		double closestDistance = Double.MAX_VALUE;

		for (CommerceInventoryWarehouse commerceInventoryWarehouse :
				commerceInventoryWarehouses) {

			if (!commerceInventoryWarehouse.isGeolocated()) {
				Country country = _getCountry(
					commerceAddress.getCompanyId(),
					commerceInventoryWarehouse.getCountryTwoLettersISOCode());

				double[] coordinates = _commerceGeocoder.getCoordinates(
					commerceInventoryWarehouse.getStreet1(),
					commerceInventoryWarehouse.getCity(),
					commerceInventoryWarehouse.getZip(),
					_getRegion(
						country.getCountryId(),
						commerceInventoryWarehouse.getCommerceRegionCode()),
					country);

				commerceInventoryWarehouse =
					_commerceInventoryWarehouseLocalService.
						geolocateCommerceInventoryWarehouse(
							commerceInventoryWarehouse.
								getCommerceInventoryWarehouseId(),
							coordinates[0], coordinates[1]);
			}

			double distance = _distanceCalculator.getDistance(
				commerceAddress.getLatitude(), commerceAddress.getLongitude(),
				commerceInventoryWarehouse.getLatitude(),
				commerceInventoryWarehouse.getLongitude());

			if (distance < closestDistance) {
				closestCommerceInventoryWarehouse = commerceInventoryWarehouse;
				closestDistance = distance;
			}
		}

		return closestCommerceInventoryWarehouse;
	}

	private CommerceAddress _getCommerceAddress(
			CommerceInventoryWarehouse commerceInventoryWarehouse, long groupId)
		throws Exception {

		CommerceAddress commerceAddress =
			_commerceAddressLocalService.createCommerceAddress(
				-commerceInventoryWarehouse.getCommerceInventoryWarehouseId());

		commerceAddress.setStreet1(commerceInventoryWarehouse.getStreet1());
		commerceAddress.setStreet2(commerceInventoryWarehouse.getStreet2());
		commerceAddress.setStreet3(commerceInventoryWarehouse.getStreet3());
		commerceAddress.setCity(commerceInventoryWarehouse.getCity());
		commerceAddress.setZip(commerceInventoryWarehouse.getZip());

		Country country = _getCountry(
			groupId, commerceInventoryWarehouse.getCountryTwoLettersISOCode());

		Region region = _getRegion(
			country.getCountryId(),
			commerceInventoryWarehouse.getCommerceRegionCode());

		commerceAddress.setRegionId(region.getRegionId());

		commerceAddress.setCountryId(country.getCountryId());

		commerceAddress.setLatitude(commerceInventoryWarehouse.getLatitude());
		commerceAddress.setLongitude(commerceInventoryWarehouse.getLongitude());

		return commerceAddress;
	}

	private Country _getCountry(long companyId, String countryCode)
		throws Exception {

		return _countryLocalService.getCountryByA2(companyId, countryCode);
	}

	private Region _getRegion(long countryId, String regionCode)
		throws Exception {

		return _regionLocalService.getRegion(countryId, regionCode);
	}

	@Reference
	private CommerceAddressLocalService _commerceAddressLocalService;

	@Reference
	private CommerceGeocoder _commerceGeocoder;

	@Reference
	private CommerceInventoryWarehouseLocalService
		_commerceInventoryWarehouseLocalService;

	@Reference
	private CountryLocalService _countryLocalService;

	private final DistanceCalculator _distanceCalculator =
		new DistanceCalculator();

	@Reference
	private RegionLocalService _regionLocalService;

}