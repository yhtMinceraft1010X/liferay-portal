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

import '@testing-library/jest-dom/extend-expect';

import * as Pricetests_utilities from '../../../../src/main/resources/META-INF/resources/components/price/util/index';

describe('Price tests_utilities', () => {
	beforeEach(() => {
		jest.resetAllMocks();
	});

	describe('PriceModel adapter', () => {
		it('transforms a PriceModel JSON object into a Price component compatible one, according to the Headless DeliveryCart API model', () => {
			const CheckCPInstancePriceModel = {
				cpInstance: {
					prices: {
						discountPercentage: '0',
						discountPercentages: null,
						finalPrice: 0,
						price: '$ 20.00',
						promoPrice: '$ 10.00',
					},
				},
			};

			expect(
				Pricetests_utilities.adaptLegacyPriceModel(
					CheckCPInstancePriceModel.cpInstance.prices
				)
			).toEqual({
				discountPercentage: 0.0,
				discountPercentageLevel1: 0.0,
				discountPercentageLevel2: 0.0,
				discountPercentageLevel3: 0.0,
				discountPercentageLevel4: 0.0,
				finalPriceFormatted: '$ 20.00',
				priceFormatted: '$ 20.00',
				promoPrice: '1',
				promoPriceFormatted: '$ 10.00',
			});
		});

		it('does not transform the PriceModel if it already has a Price component compatible shape', () => {
			const compatiblePriceModel = {
				discount: 0.0,
				discountFormatted: '$ 0.00',
				discountPercentage: '20.00',
				discountPercentageLevel1: 8.5,
				discountPercentageLevel2: 2.25,
				discountPercentageLevel3: 3.0,
				discountPercentageLevel4: 0.0,
				finalPrice: 10,
				finalPriceFormatted: '$ 10.00',
				promoPrice: 0,
				promoPriceFormatted: '$ 0.00',
			};

			expect(
				Pricetests_utilities.adaptLegacyPriceModel(compatiblePriceModel)
			).toEqual(compatiblePriceModel);
		});
	});

	describe('collectDiscountLevels', () => {
		it('collects the discount level percentages in a Headless DeliveryCart API price model into an array of percentages', () => {
			const price = {
				discountPercentage: 0.0,
				discountPercentageLevel1: 2.0,
				discountPercentageLevel2: 5.4,
				discountPercentageLevel3: 3.34,
				discountPercentageLevel4: 0.0,
				finalPriceFormatted: '$ 20.00',
				priceFormatted: '$ 20.00',
				promoPrice: '1',
				promoPriceFormatted: '$ 10.00',
			};

			const result = ['2.00', '5.40', '3.34', '0.00'];

			expect(Pricetests_utilities.collectDiscountLevels(price)).toEqual(
				result
			);
		});
	});

	describe('isNonnull', () => {
		it('checks that one or more numerical values are non-null', () => {
			expect(Pricetests_utilities.isNonnull('0')).toBe(false);
			expect(Pricetests_utilities.isNonnull('0', '0.000', 0)).toBe(false);
			expect(Pricetests_utilities.isNonnull('1')).toBe(true);
			expect(Pricetests_utilities.isNonnull('1.00', '2.4', '0')).toBe(
				true
			);
			expect(Pricetests_utilities.isNonnull(0)).toBe(false);
			expect(Pricetests_utilities.isNonnull(1)).toBe(true);
			expect(Pricetests_utilities.isNonnull(1.2, '3.1', 0)).toBe(true);
		});
	});
});
