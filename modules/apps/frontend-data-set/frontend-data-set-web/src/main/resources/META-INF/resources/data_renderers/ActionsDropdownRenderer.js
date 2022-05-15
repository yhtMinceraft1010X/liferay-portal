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

import ClayButton, {ClayButtonWithIcon} from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import ClayLink from '@clayui/link';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import {useIsMounted} from '@liferay/frontend-js-react-web';
import {openToast} from 'frontend-js-web';
import PropTypes from 'prop-types';
import React, {useContext, useState} from 'react';

import DataSetContext from '../DataSetContext';
import {ACTION_ITEM_TARGETS} from '../utils/actionItems/constants';
import {formatActionURL} from '../utils/index';
import {openPermissionsModal, resolveModalSize} from '../utils/modals/index';

const {MODAL_PERMISSIONS} = ACTION_ITEM_TARGETS;

export function isLink(target, onClick) {
	return !(target && target !== 'link') && !onClick;
}

export function handleAction(
	{
		confirmationMessage,
		event,
		itemId,
		method,
		onClick,
		setLoading,
		size,
		successMessage,
		target,
		title,
		url,
	},
	{
		executeAsyncItemAction,
		highlightItems,
		openModal,
		openSidePanel,
		toggleItemInlineEdit,
	}
) {
	if (confirmationMessage && !confirm(confirmationMessage)) {
		return;
	}

	if (target?.includes('modal')) {
		event.preventDefault();

		if (target === MODAL_PERMISSIONS) {
			openPermissionsModal(url);
		}
		else {
			openModal({
				size: resolveModalSize(target),
				title,
				url,
			});
		}
	}
	else if (target === 'sidePanel') {
		event.preventDefault();

		highlightItems([itemId]);
		openSidePanel({
			size: size || 'lg',
			title,
			url,
		});
	}
	else if (target === 'async' || target === 'headless') {
		event.preventDefault();

		setLoading(true);
		executeAsyncItemAction(url, method)
			.then(() => {
				openToast({
					message:
						successMessage ||
						Liferay.Language.get('action-completed'),
					type: 'success',
				});
				setLoading(false);
			})
			.catch((_) => {
				setLoading(false);
			});
	}
	else if (target === 'inlineEdit') {
		event.preventDefault();

		toggleItemInlineEdit(itemId);
	}
	else if (target === 'blank') {
		event.preventDefault();

		window.open(url);
	}
	else if (onClick) {
		event.preventDefault();

		event.target.setAttribute('onClick', onClick);
		event.target.onclick();
		event.target.removeAttribute('onClick');
	}
}

function ActionItem({
	action,
	closeMenu,
	handleAction,
	itemData,
	itemId,
	method,
	onClick,
	setLoading,
	size,
	title,
	url,
}) {
	const context = useContext(DataSetContext);

	const {data, icon, label, target} = action;

	function handleClickOnLink(event) {
		event.preventDefault();

		handleAction(
			{
				confirmationMessage: data?.confirmationMessage,
				event,
				itemId,
				method,
				onClick,
				setLoading,
				size: size || 'lg',
				successMessage: data?.successMessage,
				target,
				title,
				url,
			},
			context
		);

		closeMenu();
	}

	const link = isLink(target, onClick);

	const onActionDropdownItemClick = context.onActionDropdownItemClick;

	return (
		<ClayDropDown.Item
			href={link ? url : null}
			onClick={(event) => {
				if (onActionDropdownItemClick) {
					onActionDropdownItemClick({
						action,
						event,
						itemData,
					});
				}

				if (!link) {
					handleClickOnLink(event);
				}
			}}
		>
			{icon && (
				<span className="pr-2">
					<ClayIcon symbol={icon} />
				</span>
			)}

			{label}
		</ClayDropDown.Item>
	);
}

function ActionsDropdownRenderer({actions, itemData, itemId}) {
	const context = useContext(DataSetContext);
	const [menuActive, setMenuActive] = useState(false);
	const [loading, setLoading] = useState(false);
	const inlineEditingAvailable =
		context.inlineEditingSettings && itemData.actions?.update;
	const inlineEditingAlwaysOn =
		inlineEditingAvailable && context.inlineEditingSettings.alwaysOn;
	const isMounted = useIsMounted();
	const editModeActive = !!context.itemsChanges[itemId];
	const itemChanges =
		editModeActive && Object.keys(context.itemsChanges[itemId]).length
			? context.itemsChanges[itemId]
			: null;

	const inlineEditingActions = (
		<div className="d-flex">
			<ClayButtonWithIcon
				className="mr-1"
				disabled={inlineEditingAlwaysOn && !itemChanges}
				displayType="secondary"
				onClick={() => context.toggleItemInlineEdit(itemId)}
				small
				symbol="times-small"
			/>

			{loading ? (
				<ClayLoadingIndicator small />
			) : (
				<ClayButtonWithIcon
					disabled={!itemChanges}
					monospaced
					onClick={() => {
						setLoading(true);
						context.applyItemInlineUpdates(itemId).finally(() => {
							if (isMounted()) {
								setLoading(false);
							}
						});
					}}
					small
					symbol="check"
				/>
			)}
		</div>
	);

	if (!inlineEditingAlwaysOn && editModeActive) {
		return inlineEditingActions;
	}

	const formattedActions = actions
		? actions.reduce((actions, action) => {
				if (action.data?.permissionKey) {
					if (itemData.actions[action.data.permissionKey]) {
						if (action.target === 'headless') {
							return [
								...actions,
								{
									...action,
									...itemData.actions[
										action.data.permissionKey
									],
								},
							];
						}
						else {
							return [...actions, action];
						}
					}

					return actions;
				}

				return [...actions, action];
		  }, [])
		: [];

	if (inlineEditingAvailable && !inlineEditingAlwaysOn) {
		formattedActions.unshift({
			icon: 'fieldset',
			label: Liferay.Language.get('inline-edit'),
			target: 'inlineEdit',
		});
	}

	if (!formattedActions || !formattedActions.length) {
		return null;
	}

	if (!inlineEditingAlwaysOn && formattedActions.length === 1) {
		const [action] = formattedActions;
		const {data: actionData} = action;

		if (actionData?.id && !action?.href) {
			return null;
		}

		if (loading) {
			return <ClayLoadingIndicator small />;
		}

		const content = action.icon ? (
			<ClayIcon symbol={action.icon} />
		) : (
			action.label
		);

		const onActionDropdownItemClick = context.onActionDropdownItemClick;

		const url = formatActionURL(action.href, itemData);

		return isLink(action.target, action.onClick) ? (
			<ClayLink
				className="btn btn-secondary btn-sm"
				href={url}
				monospaced={Boolean(action.icon)}
				onClick={(event) => {
					if (onActionDropdownItemClick) {
						onActionDropdownItemClick({
							action,
							event,
							itemData,
						});
					}
				}}
			>
				{content}
			</ClayLink>
		) : (
			<ClayLink
				className="btn btn-secondary btn-sm"
				data-senna-off
				href="#"
				monospaced={Boolean(action.icon)}
				onClick={(event) => {
					if (onActionDropdownItemClick) {
						onActionDropdownItemClick({
							action,
							event,
							itemData,
						});
					}

					handleAction(
						{
							event,
							itemId,
							method: action.method ?? actionData?.method,
							setLoading,
							successMessage: actionData?.successMessage,
							url,
							...action,
						},
						context
					);
				}}
			>
				{content}
			</ClayLink>
		);
	}

	if (loading && !inlineEditingAlwaysOn) {
		return <ClayLoadingIndicator small />;
	}

	const renderItems = (items) =>
		items.map(({items: nestedItems = [], separator, type, ...item}, i) => {
			if (type === 'group') {
				return (
					<ClayDropDown.Group {...item}>
						{separator && <ClayDropDown.Divider />}

						{renderItems(nestedItems)}
					</ClayDropDown.Group>
				);
			}

			return (
				<ActionItem
					action={item}
					closeMenu={() => setMenuActive(false)}
					handleAction={handleAction}
					itemData={itemData}
					itemId={itemId}
					key={i}
					method={item.method ?? item.data?.method}
					setLoading={setLoading}
					url={item.href && formatActionURL(item.href, itemData)}
				/>
			);
		});

	return (
		<div className="d-flex justify-content-end ml-auto">
			{inlineEditingAlwaysOn && inlineEditingActions}

			<ClayDropDown
				active={menuActive}
				onActiveChange={setMenuActive}
				trigger={
					<ClayButton
						className="component-action dropdown-toggle ml-1"
						disabled={loading}
						displayType="unstyled"
					>
						<ClayIcon symbol="ellipsis-v" />

						<span className="sr-only">
							{Liferay.Language.get('actions')}
						</span>
					</ClayButton>
				}
			>
				<ClayDropDown.ItemList>
					{renderItems(formattedActions)}
				</ClayDropDown.ItemList>
			</ClayDropDown>
		</div>
	);
}

ActionsDropdownRenderer.propTypes = {
	actions: PropTypes.arrayOf(
		PropTypes.shape({
			data: PropTypes.shape({
				confirmationMessage: PropTypes.string,
				method: PropTypes.oneOf(['delete', 'get', 'patch', 'post']),
				permissionKey: PropTypes.string,
				successMessage: PropTypes.string,
			}),
			href: PropTypes.string,
			icon: PropTypes.string,
			label: PropTypes.string.isRequired,
			method: PropTypes.oneOf(['delete', 'get', 'patch', 'post']),
			onClick: PropTypes.string,
			target: PropTypes.oneOf([
				'async',
				'headless',
				'inlineEdit',
				'link',
				'modal',
				'modal-full-screen',
				'modal-lg',
				'modal-permissions',
				'modal-sm',
				'sidePanel',
			]),
		})
	),
	itemData: PropTypes.object,
	itemId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
};

export default ActionsDropdownRenderer;
