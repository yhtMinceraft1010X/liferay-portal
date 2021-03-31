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
import classnames from 'classnames';
import {navigate} from 'frontend-js-web';
import React, {createRef, useEffect, useState} from 'react';

import {TRIAL_SKU, addToOrder} from '../../helper/index';
import SubscriptionEntry from '../subscription_entry/index';

const PRODUCT_HIGHLIGHT = 'highlightProduct';

function CardEntryRenderer({
	checkoutURL,
	commerceAccountId,
	detailURL,
	isFeatured,
	namespace,
	productId,
	sku,
	...entry
}) {
	const [isHighlighted, setIsHighlighted] = useState(isFeatured),
		cardEntryRef = createRef();

	useEffect(() => {
		const onProductHighlight = ({id}) =>
			!id
				? setIsHighlighted(isFeatured)
				: setIsHighlighted(id === productId);

		Liferay.on(`${namespace}_${PRODUCT_HIGHLIGHT}`, onProductHighlight);
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [namespace]);

	useEffect(() => {
		const cardEntryElement = cardEntryRef.current,
			onHover = () =>
				Liferay.fire(`${namespace}_${PRODUCT_HIGHLIGHT}`, {
					id: productId,
				}),
			onOut = () =>
				Liferay.fire(`${namespace}_${PRODUCT_HIGHLIGHT}`, {id: ''});

		cardEntryElement.addEventListener('mouseover', onHover);
		cardEntryElement.addEventListener('mouseout', onOut);
	}, [cardEntryRef, namespace, productId]);

	return (
		<div
			className={classnames(
				'card',
				'osb-commerce-product-card',
				'd-flex flex-column justify-content-between text-center',
				isHighlighted && 'is-highlighted'
			)}
			ref={cardEntryRef}
		>
			<SubscriptionEntry {...entry} />

			<div className={'actions'}>
				<div>
					<ClayButton
						displayType={
							isFeatured || isHighlighted
								? 'primary'
								: 'secondary'
						}
						onClick={() => {
							addToOrder(commerceAccountId, productId).then(
								() => {
									const to =
										TRIAL_SKU === sku
											? `${Liferay.ThemeDisplay.getCanonicalURL()}/trial-registration`
											: checkoutURL;

									navigate(to);
								}
							);
						}}
					>
						{Liferay.Language.get(
							TRIAL_SKU === sku ? 'start-trial' : 'subscribe'
						)}
					</ClayButton>
				</div>

				<div>
					<ClayButton
						displayType={'link'}
						onClick={() => {
							navigate(detailURL);
						}}
						type={'button'}
					>
						{Liferay.Language.get('learn-more')}
					</ClayButton>
				</div>
			</div>
		</div>
	);
}

export default CardEntryRenderer;
