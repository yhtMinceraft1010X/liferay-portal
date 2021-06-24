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

import ClayButton from '@clayui/button';
import {CommerceComponents} from 'commerce-frontend-js';
import {navigate} from 'frontend-js-web';
import React, {useContext, useEffect, useState} from 'react';

import {hasURLComponent, mapToFeatures} from '../../helper/index';
import SubscriptionEntry from '../subscription_entry/index';

const {MiniCartContext} = CommerceComponents;

function CartSummary() {
	const {cartState, spritemap} = useContext(MiniCartContext);

	const {cartItems, summary} = cartState;
	const [subscriptionEntry] = cartItems;
	const [subscribeEnabled, setSubscribeEnabled] = useState(false);

	useEffect(() => {
		const canSubscribe = hasURLComponent('order-summary');

		if (canSubscribe) {
			setSubscribeEnabled(true);
		}
	}, []);

	const {name, options, skuId, thumbnail} = subscriptionEntry;

	const {
		subtotalFormatted,
		totalDiscountValueFormatted,
		totalFormatted,
	} = summary;

	return (
		<div className={'osb-commerce-cart-summary'}>
			<SubscriptionEntry
				features={mapToFeatures(options)}
				name={name}
				productImageURL={thumbnail}
				skuId={skuId}
				spritemap={spritemap}
			/>

			<div className={'cart-summary-price'}>
				<div className={'cart-summary-subtotal'}>
					<span>{Liferay.Language.get('subtotal')}:</span>
					<span>{subtotalFormatted}</span>
				</div>

				<div className={'cart-summary-coupon-code'}>
					<div className="input-group">
						<div className="input-group-item">
							<input
								aria-label={Liferay.Language.get(
									'insert-discount-code'
								)}
								className="form-control"
								placeholder={Liferay.Language.get(
									'insert-discount-code'
								)}
								type="text"
							/>
						</div>
						<span className="input-group-item input-group-item-shrink">
							<button
								className="btn btn-secondary"
								onClick={(e) => {
									e.preventDefault();
								}}
								type="button"
							>
								{Liferay.Language.get('apply')}
							</button>
						</span>
					</div>
				</div>

				<div className={'cart-summary-discount'}>
					<span>{Liferay.Language.get('discount')}</span>
					<span>{totalDiscountValueFormatted}</span>
				</div>

				<div className={'cart-summary-total'}>
					<span>{Liferay.Language.get('total')}</span>
					<span>{totalFormatted}</span>
				</div>
			</div>

			{subscribeEnabled && (
				<div className={'cart-summary-button'}>
					<ClayButton
						displayType={'primary'}
						onClick={() => navigate()}
						type={'button'}
					>
						{Liferay.Language.get('subscribe')}
					</ClayButton>
				</div>
			)}
		</div>
	);
}

export default CartSummary;
