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

import ClayButton, {ClayButtonWithIcon} from '@clayui/button';
import {ClayDropDownWithItems} from '@clayui/drop-down';
import {ClayCheckbox, ClayInput} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayLabel from '@clayui/label';
import {ClayTooltipProvider} from '@clayui/tooltip';
import getCN from 'classnames';
import {ManagementToolbar as FrontendManagementToolbar} from 'frontend-js-components-web';
import React, {useState} from 'react';

import {ALL, ASCENDING} from '../../utils/constants';
import {sub} from '../../utils/language';

function ManagementToolbar({
	allItems,
	category,
	filterItems,
	keyword,
	onClearCategory,
	onClearStatus,
	onReverseSort,
	onUpdateSelected,
	selected,
	setKeyword,
	setSelected,
	sortDirection,
	status,
}) {
	const [value, setValue] = useState('');

	return (
		<>
			<FrontendManagementToolbar.Container
				className={getCN('clause-contributors-management-bar', {
					'management-bar-primary': selected.length > 0,
				})}
			>
				<FrontendManagementToolbar.ItemList>
					<FrontendManagementToolbar.Item>
						<ClayCheckbox
							aria-label={Liferay.Language.get('checkbox')}
							checked={
								allItems.length > 0 &&
								selected.length === allItems.length
							}
							indeterminate={
								selected.length > 0 &&
								selected.length < allItems.length
							}
							onChange={() =>
								setSelected(
									selected.length !== allItems.length
										? allItems
										: []
								)
							}
						/>
					</FrontendManagementToolbar.Item>
				</FrontendManagementToolbar.ItemList>

				{selected.length > 0 ? (
					<>
						<FrontendManagementToolbar.ItemList expand>
							{allItems.length > 0 &&
							selected.length === allItems.length ? (
								<FrontendManagementToolbar.Item className="navbar-form">
									<span className="component-text text-truncate-inline">
										<span className="text-truncate">
											{Liferay.Language.get(
												'all-selected'
											)}
										</span>
									</span>
								</FrontendManagementToolbar.Item>
							) : (
								<>
									<FrontendManagementToolbar.Item className="navbar-form">
										<span className="component-text text-truncate-inline">
											<span className="text-truncate">
												{sub(
													Liferay.Language.get(
														'x-of-x-selected'
													),
													[
														selected.length,
														allItems.length,
													]
												)}
											</span>
										</span>
									</FrontendManagementToolbar.Item>

									<FrontendManagementToolbar.Item>
										<ClayButton
											displayType="link"
											onClick={() =>
												setSelected(allItems)
											}
											small
										>
											{Liferay.Language.get('select-all')}
										</ClayButton>
									</FrontendManagementToolbar.Item>
								</>
							)}
						</FrontendManagementToolbar.ItemList>

						<FrontendManagementToolbar.ItemList>
							<FrontendManagementToolbar.Item>
								<ClayButton.Group spaced>
									<ClayButton
										displayType="secondary"
										onClick={onUpdateSelected(true)}
										small
									>
										{Liferay.Language.get('turn-on')}
									</ClayButton>

									<ClayButton
										displayType="secondary"
										onClick={onUpdateSelected(false)}
										small
									>
										{Liferay.Language.get('turn-off')}
									</ClayButton>
								</ClayButton.Group>
							</FrontendManagementToolbar.Item>
						</FrontendManagementToolbar.ItemList>
					</>
				) : (
					<>
						<FrontendManagementToolbar.ItemList>
							<FrontendManagementToolbar.Item>
								<ClayDropDownWithItems
									items={filterItems}
									trigger={
										<ClayButton
											className="nav-link"
											displayType="unstyled"
										>
											<span className="navbar-text-truncate">
												{Liferay.Language.get(
													'filter-and-order'
												)}
											</span>

											<ClayIcon
												className="inline-item inline-item-after"
												symbol="caret-bottom"
											/>
										</ClayButton>
									}
								/>

								<ClayTooltipProvider>
									<ClayButton
										className="nav-link nav-link-monospaced"
										data-tooltip-align="bottom"
										displayType="unstyled"
										onClick={onReverseSort}
										title={Liferay.Language.get(
											'reverse-sort-direction'
										)}
									>
										<ClayIcon
											symbol={
												sortDirection === ASCENDING
													? 'order-list-down'
													: 'order-list-up'
											}
										/>
									</ClayButton>
								</ClayTooltipProvider>
							</FrontendManagementToolbar.Item>
						</FrontendManagementToolbar.ItemList>

						<FrontendManagementToolbar.ItemList expand>
							<FrontendManagementToolbar.Item className="search">
								<ClayInput.Group>
									<ClayInput.GroupItem>
										<ClayInput
											aria-label={Liferay.Language.get(
												'search'
											)}
											className="input-group-inset input-group-inset-after"
											onChange={(event) =>
												setValue(event.target.value)
											}
											onKeyDown={(event) => {
												if (event.key === 'Enter') {
													event.preventDefault();

													setKeyword(value);
												}
											}}
											placeholder={Liferay.Language.get(
												'search'
											)}
											type="text"
											value={value}
										/>

										<ClayInput.GroupInsetItem
											after
											tag="span"
										>
											<ClayButtonWithIcon
												aria-label={Liferay.Language.get(
													'search'
												)}
												displayType="unstyled"
												onClick={() =>
													setKeyword(value)
												}
												symbol="search"
											/>
										</ClayInput.GroupInsetItem>
									</ClayInput.GroupItem>
								</ClayInput.Group>
							</FrontendManagementToolbar.Item>
						</FrontendManagementToolbar.ItemList>
					</>
				)}
			</FrontendManagementToolbar.Container>

			{(!!keyword || status !== ALL || category !== ALL) && (
				<FrontendManagementToolbar.ResultsBar>
					<FrontendManagementToolbar.ResultsBarItem>
						<span className="component-text text-truncate-inline">
							<span className="text-truncate">
								{sub(
									allItems.length === 1
										? Liferay.Language.get('x-result-for-x')
										: Liferay.Language.get(
												'x-results-for-x'
										  ),
									[allItems.length, keyword]
								)}
							</span>
						</span>
					</FrontendManagementToolbar.ResultsBarItem>

					<FrontendManagementToolbar.ResultsBarItem expand>
						{status !== ALL && (
							<ClayLabel
								className="component-label tbar-label"
								closeButtonProps={{
									onClick: onClearStatus,
								}}
								displayType="unstyled"
							>
								{status}
							</ClayLabel>
						)}

						{category !== ALL && (
							<ClayLabel
								className="component-label tbar-label"
								closeButtonProps={{
									onClick: onClearCategory,
								}}
								displayType="unstyled"
							>
								{category}
							</ClayLabel>
						)}
					</FrontendManagementToolbar.ResultsBarItem>

					<FrontendManagementToolbar.ResultsBarItem>
						<ClayButton
							className="component-link tbar-link"
							displayType="unstyled"
							onClick={() => {
								setValue('');
								setKeyword('');
								onClearCategory();
								onClearStatus();
							}}
						>
							{Liferay.Language.get('clear')}
						</ClayButton>
					</FrontendManagementToolbar.ResultsBarItem>
				</FrontendManagementToolbar.ResultsBar>
			)}
		</>
	);
}

export default React.memo(ManagementToolbar);
