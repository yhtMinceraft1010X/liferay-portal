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

import ClayButton from '@clayui/button';
import ClayIcon from '@clayui/icon';
import {useIsMounted} from '@liferay/frontend-js-react-web';
import classnames from 'classnames';
import React, {useContext, useEffect, useState} from 'react';

import ServiceProvider from '../../ServiceProvider/index';
import {debouncePromise} from '../../utilities/debounce';
import {CART_PRODUCT_QUANTITY_CHANGED} from '../../utilities/eventsDefinitions';
import Price from '../price/Price';
import QuantitySelector from '../quantity_selector/QuantitySelector';
import ItemInfoView from './CartItemViews/ItemInfoView';
import MiniCartContext from './MiniCartContext';
import {
	INITIAL_ITEM_STATE,
	PRODUCT_QUANTITY_NOT_VALID_ERROR,
	REMOVAL_CANCELING_TIMEOUT,
	REMOVAL_TIMEOUT,
	UNEXPECTED_ERROR,
} from './util/constants';
import {generateProductPageURL, parseOptions} from './util/index';

const CartResource = ServiceProvider.DeliveryCartAPI('v1');

const deboncedUpdateItemQuantity = debouncePromise(
	(cartItemId, quantity, invalid) => {
		if (invalid) {
			return Promise.reject(PRODUCT_QUANTITY_NOT_VALID_ERROR);
		}

		return CartResource.updateItemById(cartItemId, {
			quantity,
		}).catch(() => {
			throw UNEXPECTED_ERROR;
		});
	},
	1000
);

function CartItem({
	adaptiveMediaImageHTMLTag,
	cartItems: childItems,
	errorMessages = [],
	id: cartItemId,
	index,
	name,
	options: rawOptions,
	price,
	productURLs,
	quantity: cartItemQuantity,
	settings,
	sku,
	skuId,
	updateCartItem,
}) {
	const [itemState, setItemState] = useState(INITIAL_ITEM_STATE);
	const [selectorQuantity, setSelectorQuantity] = useState(cartItemQuantity);
	const isMounted = useIsMounted();
	const options = parseOptions(rawOptions);

	useEffect(() => {
		setSelectorQuantity(cartItemQuantity);
	}, [cartItemQuantity]);

	const {
		actionURLs,
		cartState,
		displayDiscountLevels,
		setIsUpdating,
		updateCartModel,
	} = useContext(MiniCartContext);

	const productPageUrl = generateProductPageURL(
		actionURLs.siteDefaultURL,
		productURLs,
		actionURLs.productURLSeparator
	);

	const cancelRemoveItem = (event) => {
		event.stopPropagation();

		clearTimeout(itemState.removalTimeoutRef);

		setItemState({
			...INITIAL_ITEM_STATE,
			isRemovalCanceled: true,
			removalTimeoutRef: setTimeout(() => {
				if (isMounted()) {
					setIsUpdating(false);

					setItemState(INITIAL_ITEM_STATE);
				}
			}, REMOVAL_CANCELING_TIMEOUT),
		});
	};

	const removeItem = (event) => {
		event.stopPropagation();

		setItemState({
			...INITIAL_ITEM_STATE,
			isGettingRemoved: true,
			removalTimeoutRef: setTimeout(() => {
				if (!isMounted()) {
					return;
				}

				setIsUpdating(true);

				setItemState({
					...INITIAL_ITEM_STATE,
					isGettingRemoved: true,
					isRemoved: true,
					removalTimeoutRef: setTimeout(() => {
						CartResource.deleteItemById(cartItemId)
							.then(() => {
								if (!isMounted()) {
									return;
								}

								updateCartModel({order: {id: cartState.id}});

								Liferay.fire(CART_PRODUCT_QUANTITY_CHANGED, {
									quantity: 0,
									skuId,
								});
							})
							.catch(() => {
								updateCartItem((cartItem) => ({
									...cartItem,
									errorMessages: [UNEXPECTED_ERROR],
								}));
							})
							.finally(() => {
								if (isMounted()) {
									setIsUpdating(false);
								}
							});
					}, REMOVAL_CANCELING_TIMEOUT),
				});
			}, REMOVAL_TIMEOUT),
		});
	};

	const {isGettingRemoved, isRemovalCanceled, isRemoved} = itemState;

	return (
		<div
			className={classnames('mini-cart-item', {
				'is-removed': isRemoved,
			})}
		>
			<a className="mini-cart-item-anchor" href={productPageUrl}>
				{!!adaptiveMediaImageHTMLTag && (
					<div
						className="mini-cart-item-thumbnail"
						dangerouslySetInnerHTML={{
							__html: adaptiveMediaImageHTMLTag,
						}}
					/>
				)}

				<div
					className={classnames('mini-cart-item-info ml-3', {
						options: Boolean(options),
					})}
				>
					<ItemInfoView
						childItems={childItems}
						name={name}
						options={options}
						sku={sku}
					/>
				</div>
			</a>

			<div className="mini-cart-item-quantity">
				<QuantitySelector
					alignment={index > 0 ? 'top' : 'bottom'}
					allowedQuantities={settings.allowedQuantities}
					max={settings.maxQuantity}
					min={settings.minQuantity}
					onUpdate={({errors, value: newQuantity}) => {
						setSelectorQuantity(newQuantity);

						if (!errors.length) {
							setIsUpdating(true);
						}

						deboncedUpdateItemQuantity(
							cartItemId,
							newQuantity,
							!!errors.length
						)
							.then(() => {
								if (isMounted()) {
									setIsUpdating(false);

									updateCartModel({
										order: {id: cartState.id},
									});
								}
							})
							.catch((error) => {
								if (isMounted()) {
									setIsUpdating(false);

									if (error) {
										updateCartItem((cartItem) => ({
											...cartItem,
											errorMessages: [error],
										}));
									}
								}
							});
					}}
					quantity={selectorQuantity}
					step={settings.multipleQuantity}
					{...settings}
				/>
			</div>

			<div className="mini-cart-item-price">
				<Price
					compact={true}
					displayDiscountLevels={displayDiscountLevels}
					price={price}
				/>
			</div>

			<div className="mini-cart-item-delete">
				<button
					className="btn btn-unstyled"
					onClick={removeItem}
					type="button"
				>
					<ClayIcon symbol="times-circle-full" />
				</button>
			</div>

			{!!errorMessages.length && (
				<div className="mini-cart-item-errors">
					<div className="row">
						<div className="col-auto">
							<ClayIcon symbol="exclamation-circle" />
						</div>

						<div className="col">
							{errorMessages.map((errorMessage) => (
								<div key={errorMessage}>{errorMessage}</div>
							))}
						</div>
					</div>
				</div>
			)}

			<div
				className={classnames({
					'active': isGettingRemoved,
					'canceled': isRemovalCanceled,
					'mini-cart-item-is-removing-wrapper': true,
				})}
			>
				<div className="mini-cart-item-is-removing">
					<span>
						{Liferay.Language.get('the-item-has-been-removed')}
					</span>

					<span>
						<ClayButton
							displayType="link"
							onClick={cancelRemoveItem}
							small
							type="button"
						>
							{Liferay.Language.get('undo')}
						</ClayButton>
					</span>
				</div>
			</div>
		</div>
	);
}

export default CartItem;
