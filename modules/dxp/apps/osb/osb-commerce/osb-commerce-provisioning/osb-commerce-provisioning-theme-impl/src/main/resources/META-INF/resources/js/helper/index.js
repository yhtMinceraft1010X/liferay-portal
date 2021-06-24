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

import {
	CommerceFrontendUtils,
	CommerceServiceProvider
} from 'commerce-frontend-js';

export const GUEST_ID = '-1';
export const TRIAL_SKU = 'TRIAL101';

export const CHECKOUT_DEFAULT_LAYOUT_ENDPOINT = '/checkout';
export const TRIAL_DEFAULT_LAYOUT_ENDPOINT = '/trial-registration';

export const COMMERCE_CHECKOUT_PORTLET_ID =
	'com_liferay_commerce_checkout_web_internal_portlet_' +
	'CommerceCheckoutPortlet';

export const COMMERCE_ORDER_COOKIE_IDENTIFIER =
	'com.liferay.commerce.model.CommerceOrder#';

const CartResource = CommerceServiceProvider.DeliveryCartAPI('v1');
const {CommerceCookie} = CommerceFrontendUtils;

const OSBCommerceOrderCookie = new CommerceCookie(
	COMMERCE_ORDER_COOKIE_IDENTIFIER);

export function addToOrder({
	commerceAccountId: accountId,
	commerceChannelGroupId: channelGroupId,
	commerceChannelId: channelId,
	commerceCurrencyCode: currencyCode,
	skuId,
}) {
	return CartResource.createCartByChannelId(channelId, {
		accountId,
		cartItems: [{
			options: '[]',
			quantity: 1,
			skuId,
		}],
		currencyCode,
	}).then(({orderUUID}) => new Promise((resolve) => {
		OSBCommerceOrderCookie.setValue(channelGroupId, orderUUID);

		resolve({orderUUID});
	}));
}

export function mapToFeatures(stringifiedJSON) {
	try {
		const options = JSON.parse(stringifiedJSON);

		return options.map(({value}) => `${value}`);
	}
	catch (ignore) {
		return [];
	}
}

export function hasURLComponent(componentName) {
	return window.location.href.indexOf(componentName) > -1;
}
