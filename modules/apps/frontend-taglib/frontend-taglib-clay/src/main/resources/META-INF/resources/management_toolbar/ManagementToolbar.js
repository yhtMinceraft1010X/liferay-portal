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
import {ClayDropDownWithItems} from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import {LinkOrButton} from '@clayui/shared';
import {ManagementToolbar as FrontendManagementToolbar} from 'frontend-js-components-web';
import PropTypes from 'prop-types';
import React, {useMemo, useState} from 'react';

import ActionControls from './ActionControls';
import CreationMenu from './CreationMenu';
import FeatureFlagContext from './FeatureFlagContext';
import FilterOrderControls from './FilterOrderControls';
import InfoPanelControl from './InfoPanelControl';
import ResultsBar from './ResultsBar';
import SearchControls from './SearchControls';
import SelectionControls from './SelectionControls';

import './ManagementToolbar.scss';

const noop = () => {};

function ManagementToolbar({
	clearResultsURL,
	clearSelectionURL,
	creationMenu,
	disabled,
	filterDropdownItems,
	filterLabelItems,
	itemsTotal,
	infoPanelId,
	initialActionDropdownItems,
	initialCheckboxStatus,
	initialSelectAllButtonVisible,
	initialSelectedItems,
	onActionButtonClick = noop,
	onCheckboxChange = noop,
	onClearSelectionButtonClick = noop,
	onCreateButtonClick = noop,
	onCreationMenuItemClick = noop,
	onInfoButtonClick = noop,
	onFilterDropdownItemClick = noop,
	onOrderDropdownItemClick = noop,
	onSelectAllButtonClick = noop,
	onShowMoreButtonClick,
	orderDropdownItems,
	searchActionURL,
	searchContainerId,
	searchData,
	searchFormMethod,
	searchFormName,
	searchInputAutoFocus,
	searchInputName,
	searchValue,
	selectAllURL,
	selectable,
	showCreationMenu,
	showDesignImprovementsFF,
	showInfoButton,
	showResultsBar,
	showSearch,
	sortingOrder,
	sortingURL,
	supportsBulkActions,
	viewTypeItems,
}) {
	const [actionDropdownItems, setActionDropdownItems] = useState(
		initialActionDropdownItems
	);
	const [active, setActive] = useState(initialCheckboxStatus !== 'unchecked');
	const [searchMobile, setSearchMobile] = useState(false);
	const viewTypeIcon = useMemo(
		() => viewTypeItems?.find((item) => item.active)?.icon,
		[viewTypeItems]
	);

	return (
		<FeatureFlagContext.Provider
			value={{showDesignImprovements: showDesignImprovementsFF}}
		>
			<FrontendManagementToolbar.Container active={active}>
				<FrontendManagementToolbar.ItemList>
					{selectable && (
						<SelectionControls
							actionDropdownItems={actionDropdownItems}
							active={active}
							clearSelectionURL={clearSelectionURL}
							disabled={disabled}
							initialCheckboxStatus={initialCheckboxStatus}
							initialSelectAllButtonVisible={
								initialSelectAllButtonVisible
							}
							initialSelectedItems={initialSelectedItems}
							itemsTotal={itemsTotal}
							onCheckboxChange={onCheckboxChange}
							onClearButtonClick={onClearSelectionButtonClick}
							onSelectAllButtonClick={onSelectAllButtonClick}
							searchContainerId={searchContainerId}
							selectAllURL={selectAllURL}
							setActionDropdownItems={setActionDropdownItems}
							setActive={setActive}
							showCheckBoxLabel={
								!active &&
								!filterDropdownItems &&
								!sortingURL &&
								!showSearch
							}
							supportsBulkActions={supportsBulkActions}
						/>
					)}

					{!active && (
						<FilterOrderControls
							disabled={disabled}
							filterDropdownItems={filterDropdownItems}
							onFilterDropdownItemClick={
								onFilterDropdownItemClick
							}
							onOrderDropdownItemClick={onOrderDropdownItemClick}
							orderDropdownItems={orderDropdownItems}
							sortingOrder={sortingOrder}
							sortingURL={sortingURL}
						/>
					)}
				</FrontendManagementToolbar.ItemList>

				{!active && showSearch && (
					<SearchControls
						disabled={disabled}
						searchActionURL={searchActionURL}
						searchData={searchData}
						searchFormMethod={searchFormMethod}
						searchFormName={searchFormName}
						searchInputAutoFocus={searchInputAutoFocus}
						searchInputName={searchInputName}
						searchMobile={searchMobile}
						searchValue={searchValue}
						setSearchMobile={setSearchMobile}
					/>
				)}

				<FrontendManagementToolbar.ItemList>
					{!active && showSearch && (
						<SearchControls.ShowMobileButton
							disabled={disabled}
							setSearchMobile={setSearchMobile}
						/>
					)}

					{!showDesignImprovementsFF && showInfoButton && (
						<InfoPanelControl
							infoPanelId={infoPanelId}
							onInfoButtonClick={onInfoButtonClick}
						/>
					)}

					{active ? (
						<>
							<ActionControls
								actionDropdownItems={actionDropdownItems}
								disabled={disabled}
								onActionButtonClick={onActionButtonClick}
							/>
						</>
					) : (
						<>
							{viewTypeItems && (
								<FrontendManagementToolbar.Item>
									<ClayDropDownWithItems
										items={viewTypeItems}
										trigger={
											showDesignImprovementsFF ? (
												<ClayButton
													className="nav-link"
													displayType="unstyled"
													title={Liferay.Language.get(
														'show-view-options'
													)}
												>
													{viewTypeIcon && (
														<ClayIcon
															symbol={
																viewTypeIcon
															}
														/>
													)}

													<ClayIcon
														className="inline-item inline-item-after"
														symbol="caret-double-l"
													/>
												</ClayButton>
											) : (
												<ClayButtonWithIcon
													className="nav-link nav-link-monospaced"
													displayType="unstyled"
													symbol={viewTypeIcon}
													title={Liferay.Language.get(
														'show-view-options'
													)}
												/>
											)
										}
									/>
								</FrontendManagementToolbar.Item>
							)}

							{showCreationMenu && (
								<FrontendManagementToolbar.Item>
									{creationMenu ? (
										<CreationMenu
											{...creationMenu}
											onCreateButtonClick={
												onCreateButtonClick
											}
											onCreationMenuItemClick={
												onCreationMenuItemClick
											}
											onShowMoreButtonClick={
												onShowMoreButtonClick
											}
										/>
									) : showDesignImprovementsFF ? (
										<LinkOrButton
											className="nav-btn"
											displayType="primary"
											onClick={onCreateButtonClick}
											symbol="plus"
											wide
										>
											{Liferay.Language.get('new')}
										</LinkOrButton>
									) : (
										<ClayButtonWithIcon
											className="nav-btn nav-btn-monospaced"
											displayType="primary"
											onClick={onCreateButtonClick}
											symbol="plus"
										/>
									)}
								</FrontendManagementToolbar.Item>
							)}
						</>
					)}

					{showDesignImprovementsFF && showInfoButton && (
						<InfoPanelControl
							infoPanelId={infoPanelId}
							onInfoButtonClick={onInfoButtonClick}
							separator={active}
						/>
					)}
				</FrontendManagementToolbar.ItemList>
			</FrontendManagementToolbar.Container>

			{showResultsBar && (
				<ResultsBar
					clearResultsURL={clearResultsURL}
					filterLabelItems={filterLabelItems}
					itemsTotal={itemsTotal}
					searchValue={searchValue}
				/>
			)}
		</FeatureFlagContext.Provider>
	);
}

ManagementToolbar.propTypes = {
	actionDropdownItems: PropTypes.arrayOf(
		PropTypes.shape({
			href: PropTypes.string,
			icon: PropTypes.string,
			label: PropTypes.string,
			quickAction: PropTypes.bool,
		})
	),
	clearResultsURL: PropTypes.string,
	clearSelectionURL: PropTypes.string,
	creationMenu: PropTypes.object,
	disabled: PropTypes.bool,
	filterDropdownItems: PropTypes.array,
	initialCheckboxStatus: PropTypes.oneOf([
		'checked',
		'indeterminate',
		'unchecked',
	]),
	itemsTotal: PropTypes.number,
	onCheckboxChange: PropTypes.func,
	onCreateButtonClick: PropTypes.func,
	onInfoButtonClick: PropTypes.func,
	onViewTypeSelect: PropTypes.func,
	orderDropdownItems: PropTypes.array,
	searchActionURL: PropTypes.string,
	searchContainerId: PropTypes.string,
	searchData: PropTypes.object,
	searchFormMethod: PropTypes.string,
	searchFormName: PropTypes.string,
	searchInputName: PropTypes.string,
	searchValue: PropTypes.string,
	selectAllURL: PropTypes.string,
	selectable: PropTypes.bool,
	showCreationMenu: PropTypes.bool,
	showDesignImprovementsFF: PropTypes.bool,
	showInfoButton: PropTypes.bool,
	showResultsBar: PropTypes.bool,
	showSearch: PropTypes.bool,
	showSelectAllButton: PropTypes.bool,
	sortingOrder: PropTypes.string,
	sortingURL: PropTypes.string,
	viewTypeItems: PropTypes.array,
};

export default ManagementToolbar;
