/* eslint-disable no-unused-vars */
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

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import {ClayCheckbox, ClayInput} from '@clayui/form';
import classNames from 'classnames';
import {useCallback, useEffect, useState} from 'react';
import i18n from '../../../../common/I18n';

const INVALID_NODE_MESSAGE = i18n.translate('enter-a-valid-number');
const INVALID_MIN_NODE_MESSAGE = i18n.translate(
	'enter-a-minimum-node-value-greater-than-0'
);
const INVALID_MAX_NODE_MESSAGE = i18n.translate(
	'max-nodes-must-be-greater-than-min-nodes'
);
const INVALID_NEGATIVE_NODES_MESSAGE = i18n.translate(
	'enter-nodes-values-greater-than-0'
);

const KeyTypeFilter = ({
	clearInputs,
	hasCluster,
	hasVirtualCluster,
	setFilters,
}) => {
	const [minNodesValue, setMinNodesValue] = useState('');
	const [maxNodesValue, setMaxNodesValue] = useState('');

	const [clusterChecked, setClusterChecked] = useState(false);
	const [onPromiseChecked, setOnPromiseChecked] = useState(false);

	const [errorMessage, setErrorMessage] = useState('');

	useEffect(() => {
		if (isNaN(minNodesValue) || isNaN(maxNodesValue)) {
			setErrorMessage(INVALID_NODE_MESSAGE);

			return;
		}

		if (minNodesValue === '0') {
			setErrorMessage(INVALID_MIN_NODE_MESSAGE);

			return;
		}

		if (maxNodesValue <= -1 || minNodesValue <= -1) {
			setErrorMessage(INVALID_NEGATIVE_NODES_MESSAGE);

			return;
		}

		if (maxNodesValue < minNodesValue || maxNodesValue === '0') {
			setErrorMessage(INVALID_MAX_NODE_MESSAGE);

			return;
		}

		setErrorMessage('');
	}, [maxNodesValue, minNodesValue]);

	useEffect(() => {
		if (!clusterChecked) {
			setMinNodesValue('');
			setMaxNodesValue('');
		}
	}, [clusterChecked]);

	useEffect(() => {
		if (clearInputs) {
			setMinNodesValue('');
			setMaxNodesValue('');
			setClusterChecked(false);
			setOnPromiseChecked(false);
		}
	}, [clearInputs]);

	const getClusterFilter = useCallback(() => {
		if (hasVirtualCluster) {
			return {
				hasVirtualCluster: clusterChecked,
			};
		}

		if (hasCluster) {
			return {
				hasCluster: clusterChecked,
			};
		}
	}, [clusterChecked, hasCluster, hasVirtualCluster]);

	return (
		<>
			<div className="px-3 py-2">
				<ClayCheckbox
					checked={onPromiseChecked}
					label={i18n.translate('on-premise')}
					onChange={() =>
						setOnPromiseChecked(
							(previousOnPromiseChecked) =>
								!previousOnPromiseChecked
						)
					}
				/>
			</div>

			{(hasVirtualCluster || hasCluster) && (
				<div
					className={classNames('py-2 px-3', {
						'bg-brand-primary-lighten-5': clusterChecked,
					})}
				>
					<ClayCheckbox
						checked={clusterChecked}
						label={i18n.translate('virtual-cluster')}
						onChange={() =>
							setClusterChecked(
								(previousClusterChecked) =>
									!previousClusterChecked
							)
						}
					/>

					<div className="d-flex ml-4">
						<div className="mr-2">
							<ClayInput
								className={{
									'bg-neutral-1 border-danger':
										errorMessage ===
											INVALID_MIN_NODE_MESSAGE ||
										isNaN(minNodesValue) ||
										minNodesValue <= -1,

									'bg-neutral-1 border-white': !clusterChecked,
								}}
								disabled={!clusterChecked}
								onChange={(event) => {
									setMinNodesValue(event.target.value);
								}}
								placeholder="1"
								value={minNodesValue}
							/>

							{clusterChecked && (
								<p className="m-0 text-neutral-7 text-paragraph-sm">
									{i18n.translate('min-nodes')}
								</p>
							)}
						</div>

						<div>
							<ClayInput
								className={{
									'bg-neutral-1 border-danger':
										errorMessage ===
											INVALID_MAX_NODE_MESSAGE ||
										isNaN(maxNodesValue) ||
										maxNodesValue <= -1,
									'bg-neutral-1 border-white': !clusterChecked,
								}}
								disabled={!clusterChecked}
								onChange={(event) => {
									setMaxNodesValue(event.target.value);
								}}
								placeholder="28"
								value={maxNodesValue}
							/>

							{clusterChecked && (
								<p className="m-0 text-neutral-7 text-paragraph-sm">
									{i18n.translate('max-nodes')}
								</p>
							)}
						</div>
					</div>

					{errorMessage && (
						<ClayAlert
							className="mx-0 p-2 text-paragraph-xs"
							displayType="danger"
						>
							{errorMessage}
						</ClayAlert>
					)}
				</div>
			)}

			<div className="mb-3 mt-2 mx-3">
				<ClayButton
					className="w-100"
					disabled={errorMessage}
					onClick={() => {
						setFilters((previousFilters) => ({
							...previousFilters,
							keyType: {
								...previousFilters.keyType,
								value: {
									hasOnPremise: onPromiseChecked,
									maxNodes: maxNodesValue,
									minNodes: minNodesValue,
									...getClusterFilter(),
								},
							},
						}));
					}}
					small={true}
				>
					{i18n.translate('apply')}
				</ClayButton>
			</div>
		</>
	);
};
export default KeyTypeFilter;
