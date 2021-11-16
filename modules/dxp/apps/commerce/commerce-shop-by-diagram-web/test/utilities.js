/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import fetchMock from 'fetch-mock';

import {PINS_ENDPOINT} from '../src/main/resources/META-INF/resources/js/utilities/data';

export const defaultDiagramProps = {
	channelId: 'fake-channel-id',
	datasetDisplayId: 'testDatasetDisplayId',
	diagramId: 'fake-diagram-id',
	imageURL: '/image/test-url.jpg',
	pinsRadius: 1,
	productId: 'fake-product-id',
};

export function load(element, timeout) {
	return new Promise((resolve) => {
		setTimeout(() => {
			element.dispatchEvent(new Event('load'));

			resolve();
		}, timeout);
	});
}

export const pinsData = [
	{
		id: 1,
		mappedProduct: {
			actions: {},
			customFields: [],
			id: 1,
			productExternalReferenceCode: 'MIN93016minium-full-initializer',
			productId: 2,
			productName: {
				en_US: 'First Product',
			},
			quantity: 1,
			sequence: 'first',
			sku: 'MIN93016B',
			skuExternalReferenceCode: 'min93016b',
			skuId: 3,
			type: 'sku',
		},
		positionX: 50,
		positionY: 50,
		sequence: 'first',
	},
	{
		id: 2,
		mappedProduct: {
			actions: {},
			customFields: [],
			id: 2,
			productExternalReferenceCode: 'MIN93016minium-full-initializer',
			productId: 3,
			productName: {
				en_US: 'Second Product',
			},
			quantity: 2,
			sequence: 'second',
			sku: 'SKU_TEST_SECOND',
			skuExternalReferenceCode: 'SKU_TEST_SECOND',
			skuId: 3,
			type: 'diagram',
		},
		positionX: 25,
		positionY: 25,
		sequence: 'second',
	},
	{
		id: 2,
		mappedProduct: {
			actions: {},
			customFields: [],
			id: 3,
			productExternalReferenceCode: 'MIN93016minium-full-initializer',
			productId: 5,
			quantity: 4,
			sequence: 'third',
			sku: 'SKU_TEST_THIRD',
			skuId: 4,
			type: 'external',
		},
		positionX: 75,
		positionY: 75,
		sequence: 'third',
	},
];

function getPins() {
	return {
		actions: {},
		facets: [],
		items: pinsData,
		lastPage: 1,
		page: 1,
		pageSize: 100,
		totalCount: 3,
	};
}

function getDiagrams() {
	return {
		actions: {},
		facets: [],
		items: pinsData,
		lastPage: 1,
		page: 1,
		pageSize: 100,
		totalCount: 3,
	};
}

function getSkus() {
	return {
		actions: {},
		facets: [],
		items: [],
		lastPage: 1,
		page: 1,
		pageSize: 100,
		totalCount: 1,
	};
}

export const productData = {
	id: 12345,
	name: 'Brake Fluid',
	productConfiguration: {
		allowBackOrder: true,
		allowedOrderQuantities: [],
		inventoryEngine: '',
		maxOrderQuantity: 10000,
		minOrderQuantity: 1,
		multipleOrderQuantity: 1,
	},
	productId: 12346,
	productOptions: [
		{
			description: '',
			fieldType: 'select',
			id: 42591,
			key: 'package-quantity',
			name: 'Package Quantity',
			optionId: 42551,
			productOptionValues: [
				{
					id: 42592,
					key: '12',
					name: '12',
					priority: 1.0,
				},
				{
					id: 42593,
					key: '48',
					name: '48',
					priority: 3.0,
				},
				{
					id: 42594,
					key: '112',
					name: '112',
					priority: 4.0,
				},
			],
		},
	],
	skus: [
		{
			availability: {
				label: 'available',
				label_i18n: 'Available',
				stockQuantity: 240,
			},
			id: 42605,
			options: {
				42591: '42594',
			},
			price: {
				currency: 'US Dollar',
				price: 80.0,
				priceFormatted: '$ 80.00',
				promoPrice: 72.0,
				promoPriceFormatted: '$ 72.00',
			},
			sku: 'MIN93016C',
		},
	],
	slug: 'brake-fluid',
	urlImage: '/product-image-url/',
	urls: {
		en_US: 'brake-fluid',
	},
};

export function mockCommonEndpoints() {
	const accountsEndpointRegExp = new RegExp(
		`${PINS_ENDPOINT}/products/${defaultDiagramProps.productId}/pins`
	);
	const skusEndpointRegExp = new RegExp(
		`/o/headless-commerce-admin-catalog/v1.0/skus`
	);
	const diagramsEndpointRegExp = new RegExp(
		`/o/headless-commerce-admin-catalog/v1.0/products`
	);
	const deliveryCatalogEndpointRegExp = new RegExp(
		`/o/headless-commerce-delivery-catalog/v1.0/channels/${defaultDiagramProps.channelId}/products`
	);

	fetchMock.mock(accountsEndpointRegExp, () => getPins());
	fetchMock.mock(skusEndpointRegExp, () => getSkus());
	fetchMock.mock(diagramsEndpointRegExp, () => getDiagrams());
	fetchMock.mock(deliveryCatalogEndpointRegExp, () => productData);
}
