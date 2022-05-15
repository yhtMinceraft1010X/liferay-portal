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
import ClayEmptyState from '@clayui/empty-state';
import {ClayCheckbox} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayModal, {useModal} from '@clayui/modal';
import ClayTable from '@clayui/table';
import {ManagementToolbar} from 'frontend-js-components-web';
import React, {useContext, useState} from 'react';

import ThemeContext from '../../shared/ThemeContext';
import {sub} from '../../utils/language';

function SelectTypes({
	onFrameworkConfigChange,
	onFetchSearchableTypes,
	searchableTypes = [],
	selectedTypes = [],
}) {
	const {locale} = useContext(ThemeContext);

	const [visible, setVisible] = useState(false);
	const {observer, onClose} = useModal({
		onClose: () => setVisible(false),
	});
	const [modalSelectedTypes, setModalSelectedTypes] = useState(selectedTypes);

	const searchableTypesClassNames = searchableTypes.map(
		({className}) => className
	);
	const searchableTypesSorted = searchableTypes.sort((a, b) =>
		a.displayName.localeCompare(b.displayName, locale.replace('_', '-'))
	);

	const _handleDelete = (type) => () => {
		const newSelected = modalSelectedTypes.filter((item) => item !== type);

		onFrameworkConfigChange({
			searchableAssetTypes: newSelected,
		});
		setModalSelectedTypes(newSelected);
	};

	const _handleModalDone = () => {
		onClose();

		onFrameworkConfigChange({
			searchableAssetTypes: modalSelectedTypes,
		});
	};

	const _handleRowCheck = (type) => () => {
		const isSelected = modalSelectedTypes.includes(type);

		setModalSelectedTypes(
			isSelected
				? modalSelectedTypes.filter((item) => item !== type)
				: [...modalSelectedTypes, type]
		);
	};

	return (
		<>
			<ClayButton
				className="select-types-button"
				displayType="secondary"
				onClick={() => {
					setVisible(true);
					setModalSelectedTypes(selectedTypes);
				}}
				small
			>
				{Liferay.Language.get('select-asset-types')}
			</ClayButton>

			{selectedTypes.length > 0 && (
				<ClayTable>
					<ClayTable.Body>
						{searchableTypesSorted
							.filter(({className}) =>
								selectedTypes.includes(className)
							)
							.map(({className, displayName}) => (
								<ClayTable.Row key={className}>
									<ClayTable.Cell expanded headingTitle>
										{displayName}
									</ClayTable.Cell>

									<ClayTable.Cell>
										<ClayButton
											aria-label={Liferay.Language.get(
												'delete'
											)}
											className="secondary"
											displayType="unstyled"
											onClick={_handleDelete(className)}
											small
										>
											<ClayIcon symbol="times" />
										</ClayButton>
									</ClayTable.Cell>
								</ClayTable.Row>
							))}
					</ClayTable.Body>
				</ClayTable>
			)}

			{visible && (
				<ClayModal
					className="modal-height-xl sxp-blueprint-searchable-types-modal"
					observer={observer}
					size="lg"
				>
					<ClayModal.Header>
						{Liferay.Language.get('select-types')}
					</ClayModal.Header>

					{searchableTypes.length > 0 ? (
						<>
							<ManagementToolbar.Container
								className={
									modalSelectedTypes.length > 0 &&
									'management-bar-primary'
								}
							>
								<div className="navbar-form navbar-form-autofit navbar-overlay">
									<ManagementToolbar.ItemList>
										<ManagementToolbar.Item>
											<ClayCheckbox
												checked={
													modalSelectedTypes.length >
													0
												}
												indeterminate={
													modalSelectedTypes.length >
														0 &&
													modalSelectedTypes.length !==
														searchableTypes.length
												}
												onChange={() =>
													setModalSelectedTypes(
														modalSelectedTypes.length ===
															0
															? searchableTypesClassNames
															: []
													)
												}
											/>
										</ManagementToolbar.Item>

										<ManagementToolbar.Item>
											{modalSelectedTypes.length > 0 ? (
												<>
													<span className="component-text">
														{sub(
															Liferay.Language.get(
																'x-of-x-selected'
															),
															[
																modalSelectedTypes.length,
																searchableTypes.length,
															],
															false
														)}
													</span>

													{modalSelectedTypes.length <
														searchableTypes.length && (
														<ClayButton
															displayType="link"
															onClick={() => {
																setModalSelectedTypes(
																	searchableTypesClassNames
																);
															}}
															small
														>
															{Liferay.Language.get(
																'select-all'
															)}
														</ClayButton>
													)}
												</>
											) : (
												<span className="component-text">
													{Liferay.Language.get(
														'select-all'
													)}
												</span>
											)}
										</ManagementToolbar.Item>
									</ManagementToolbar.ItemList>
								</div>
							</ManagementToolbar.Container>

							<ClayModal.Body scrollable>
								<ClayTable>
									<ClayTable.Body>
										{searchableTypesSorted.map(
											({className, displayName}) => {
												const isSelected = modalSelectedTypes.includes(
													className
												);

												return (
													<ClayTable.Row
														active={isSelected}
														className="cursor-pointer"
														key={className}
														onClick={_handleRowCheck(
															className
														)}
													>
														<ClayTable.Cell>
															<ClayCheckbox
																checked={
																	isSelected
																}
																onChange={_handleRowCheck(
																	className
																)}
															/>
														</ClayTable.Cell>

														<ClayTable.Cell
															expanded
															headingTitle
														>
															{displayName}
														</ClayTable.Cell>
													</ClayTable.Row>
												);
											}
										)}
									</ClayTable.Body>
								</ClayTable>
							</ClayModal.Body>
						</>
					) : (
						<ClayModal.Body>
							<ClayEmptyState
								description={Liferay.Language.get(
									'an-error-has-occurred-and-we-were-unable-to-load-the-results'
								)}
								imgSrc="/o/admin-theme/images/states/empty_state.gif"
								title={Liferay.Language.get(
									'no-items-were-found'
								)}
							>
								<ClayButton
									displayType="secondary"
									onClick={onFetchSearchableTypes}
								>
									{Liferay.Language.get('refresh')}
								</ClayButton>
							</ClayEmptyState>
						</ClayModal.Body>
					)}

					<ClayModal.Footer
						last={
							<ClayButton.Group spaced>
								<ClayButton
									displayType="secondary"
									onClick={onClose}
								>
									{Liferay.Language.get('cancel')}
								</ClayButton>

								<ClayButton onClick={_handleModalDone}>
									{Liferay.Language.get('done')}
								</ClayButton>
							</ClayButton.Group>
						}
					/>
				</ClayModal>
			)}
		</>
	);
}

export default React.memo(SelectTypes);
