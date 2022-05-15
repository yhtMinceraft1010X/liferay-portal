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

import classnames from 'classnames';
import PropTypes from 'prop-types';
import React, {useCallback, useEffect, useMemo, useRef, useState} from 'react';

import ServiceProvider from '../../ServiceProvider/index';
import {
	CART_PRODUCT_QUANTITY_CHANGED,
	CP_INSTANCE_CHANGED,
} from '../../utilities/eventsDefinitions';
import {useCommerceAccount, useCommerceCart} from '../../utilities/hooks';
import {getMinQuantity} from '../../utilities/quantities';
import QuantitySelector from '../quantity_selector/QuantitySelector';
import AddToCartButton from './AddToCartButton';
import {ALL} from './constants';

const CartResource = ServiceProvider.DeliveryCartAPI('v1');

function getQuantity(settings) {
	if (settings?.productConfiguration?.allowedOrderQuantities?.length) {
		return Math.min(
			...settings.productConfiguration.allowedOrderQuantities
		);
	}

	return getMinQuantity(
		settings?.productConfiguration?.minOrderQuantity,
		settings?.productConfiguration?.multipleOrderQuantity
	);
}

function AddToCart({
	accountId: initialAccountId,
	cartId: initialCartId,
	cartUUID: initialCartUUID,
	channel,
	cpInstance: initialCpInstance,
	disabled: initialDisabled,
	settings,
}) {
	const account = useCommerceAccount({id: initialAccountId});
	const cart = useCommerceCart(
		{
			UUID: initialCartUUID,
			id: initialCartId,
		},
		channel.groupId
	);
	const [cpInstance, setCpInstance] = useState({
		...initialCpInstance,
		quantity: getQuantity(settings),
		quantityValid: true,
	});
	const inputRef = useRef(null);

	const buttonDisabled = useMemo(() => {
		if (
			initialDisabled ||
			!account?.id ||
			cpInstance.disabled ||
			cpInstance.purchasable === false ||
			!cpInstance.quantity
		) {
			return true;
		}

		return false;
	}, [account, cpInstance, initialDisabled]);

	useEffect(() => {
		setCpInstance({
			...initialCpInstance,
			quantity: getQuantity(settings),
			quantityValid: true,
		});
	}, [initialCpInstance, settings]);

	const handleCPInstanceReplaced = useCallback(
		({cpInstance: incomingCpInstance}) => {
			function updateInCartState(inCart) {
				setCpInstance((cpInstance) => ({
					...cpInstance,
					backOrderAllowed: incomingCpInstance.backOrderAllowed,
					disabled: incomingCpInstance.disabled,
					inCart,
					purchasable: incomingCpInstance.purchasable,
					skuId: incomingCpInstance.skuId,
					skuOptions: Array.isArray(incomingCpInstance.skuOptions)
						? incomingCpInstance.skuOptions
						: JSON.parse(incomingCpInstance.skuOptions),
					stockQuantity: incomingCpInstance.stockQuantity,
				}));
			}

			if (cart.id) {
				CartResource.getItemsByCartId(cart.id).then(({items}) => {
					const inCart = items.some(
						({skuId}) => incomingCpInstance.skuId === skuId
					);

					updateInCartState(inCart);
				});
			}
			else {
				updateInCartState(false);
			}
		},
		[cart.id]
	);

	useEffect(() => {
		function handleQuantityChanged({quantity, skuId}) {
			setCpInstance((cpInstance) => ({
				...cpInstance,
				inCart:
					skuId === cpInstance.skuId || skuId === ALL
						? Boolean(quantity)
						: cpInstance.inCart,
			}));
		}

		Liferay.on(CART_PRODUCT_QUANTITY_CHANGED, handleQuantityChanged);

		if (settings.namespace) {
			Liferay.on(
				`${settings.namespace}${CP_INSTANCE_CHANGED}`,
				handleCPInstanceReplaced
			);
		}

		return () => {
			Liferay.detach(
				CART_PRODUCT_QUANTITY_CHANGED,
				handleQuantityChanged
			);

			if (settings.namespace) {
				Liferay.detach(
					`${settings.namespace}${CP_INSTANCE_CHANGED}`,
					handleCPInstanceReplaced
				);
			}
		};
	}, [handleCPInstanceReplaced, settings.namespace]);

	const spaceDirection = settings.inline ? 'ml' : 'mt';
	const spacer = settings.size === 'sm' ? 1 : 3;

	return (
		<div
			className={classnames({
				'add-to-cart-wrapper': true,
				'align-items-center':
					settings.alignment === 'full-width' ||
					settings.alignment === 'center',
				'd-flex': true,
				'flex-column': !settings.inline,
			})}
		>
			<QuantitySelector
				allowedQuantities={
					settings.productConfiguration?.allowedOrderQuantities
				}
				disabled={initialDisabled || !account?.id}
				max={settings.productConfiguration?.maxOrderQuantity}
				min={settings.productConfiguration?.minOrderQuantity}
				onUpdate={({errors, value: quantity}) =>
					setCpInstance({
						...cpInstance,
						quantity,
						quantityValid: !errors.length,
					})
				}
				quantity={cpInstance.quantity}
				ref={inputRef}
				size={settings.size}
				step={settings.productConfiguration?.multipleOrderQuantity}
			/>

			<AddToCartButton
				accountId={account.id}
				cartId={cart.id}
				channel={channel}
				className={`${spaceDirection}-${spacer}`}
				cpInstances={[cpInstance]}
				disabled={buttonDisabled}
				invalid={!cpInstance.quantityValid}
				onAdd={() => {
					setCpInstance({...cpInstance, inCart: true});
				}}
				onClick={
					cpInstance.quantityValid
						? null
						: (event) => {
								event.preventDefault();

								inputRef.current.focus();
						  }
				}
				settings={settings}
			/>
		</div>
	);
}

AddToCart.propTypes = {
	accountId: PropTypes.number.isRequired,
	cartId: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
	cpInstance: PropTypes.shape({
		skuId: PropTypes.oneOfType([PropTypes.number, PropTypes.string])
			.isRequired,
		skuOptions: PropTypes.array,
	}),
	disabled: PropTypes.bool,
	settings: PropTypes.shape({
		alignment: PropTypes.oneOf(['center', 'left', 'right', 'full-width']),
		inline: PropTypes.bool,
		namespace: PropTypes.string,
		productConfiguration: PropTypes.shape({
			allowedOrderQuantities: PropTypes.arrayOf(PropTypes.number),
			maxOrderQuantity: PropTypes.number,
			minOrderQuantity: PropTypes.number,
			multipleOrderQuantity: PropTypes.number,
		}),
		size: PropTypes.oneOf(['lg', 'md', 'sm']),
	}),
};

export default AddToCart;
