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

import {ClayTooltipProvider} from '@clayui/tooltip';
import {useCallback, useEffect, useMemo, useState} from 'react';
import {useOutletContext} from 'react-router-dom';
import i18n from '../../../../common/I18n';
import Table from '../../../../common/components/Table';
import {useCustomerPortal} from '../../context';
import useGetActivationKeysData from '../ActivationKeysTable/hooks/useGetActivationKeysData';
import usePagination from '../ActivationKeysTable/hooks/usePagination';
import useStatusCountNavigation from '../ActivationKeysTable/hooks/useStatusCountNavigation';
import {
	EnvironmentTypeColumn,
	ExpirationDateColumn,
	KeyTypeColumn,
	StatusColumn,
} from '../ActivationKeysTable/utils/constants/columns-definitions';
import {getTooltipContentRenderer} from '../ActivationKeysTable/utils/getTooltipContentRenderer';
import DeactivateKeysSkeleton from './Skeleton';
import DeactivateKeysTableFooter from './components/Footer';
import DeactivationKeysTableHeader from './components/Header';
import useFilters from './components/Header/hooks/useFilters';
import {DEACTIVATE_COLUMNS} from './utils/constants';

const DeactivateKeysTable = ({productName}) => {
	const [{project, sessionId}] = useCustomerPortal();
	const {setHasQuickLinksPanel, setHasSideMenu} = useOutletContext();

	useEffect(() => {
		setHasQuickLinksPanel(false);
		setHasSideMenu(false);
	}, [setHasSideMenu, setHasQuickLinksPanel]);

	const {
		activationKeysState: [activationKeys, setActivationKeys],
		loading,
		setFilterTerm,
	} = useGetActivationKeysData(project, sessionId, productName);

	const {
		statusfilterByTitle: [statusFilter],
	} = useStatusCountNavigation(activationKeys);

	const {activationKeysByStatusPaginated, paginationConfig} = usePagination(
		activationKeys,
		statusFilter
	);

	const [filters, setFilters] = useFilters(setFilterTerm, productName);

	const [activationKeysIdChecked, setActivationKeysIdChecked] = useState([]);

	const activationKeysByStatusPaginatedChecked = useMemo(
		() =>
			activationKeys.filter(({id}) =>
				activationKeysIdChecked.includes(id)
			) || [],
		[activationKeys, activationKeysIdChecked]
	);

	const getDeactivationKeysRows = useCallback(
		(activationKey) => ({
			envName: (
				<div title={[activationKey.name, activationKey.description]}>
					<p className="font-weight-bold m-0 text-neutral-10 text-truncate">
						{activationKey.name}
					</p>

					<p className="font-weight-normal m-0 text-neutral-7 text-paragraph-sm text-truncate">
						{activationKey.description}
					</p>
				</div>
			),
			envType: <EnvironmentTypeColumn activationKey={activationKey} />,
			expirationDate: (
				<ExpirationDateColumn activationKey={activationKey} />
			),
			id: activationKey.id,
			keyType: <KeyTypeColumn activationKey={activationKey} />,
			status: <StatusColumn activationKey={activationKey} />,
		}),
		[]
	);

	return (
		<div className="h-100 ml-auto mr-auto w-75">
			<div className="d-flex flex-column">
				<div className="text-left">
					<h3>{i18n.translate('deactivate-dxp-activation-key-s')}</h3>

					<p>
						{i18n.translate(
							'select-the-activation-key-you-wish-to-deactivate'
						)}
					</p>
				</div>
			</div>

			<ClayTooltipProvider
				contentRenderer={({title}) => getTooltipContentRenderer(title)}
				delay={100}
			>
				<div>
					<div className="mt-4 py-2">
						<DeactivationKeysTableHeader
							activationKeysState={[
								activationKeys,
								setActivationKeys,
							]}
							filterState={[filters, setFilters]}
							loading={loading}
						/>
					</div>

					{!!activationKeysByStatusPaginated.length && (
						<Table
							checkboxConfig={{
								checkboxesChecked: activationKeysIdChecked,
								setCheckboxesChecked: setActivationKeysIdChecked,
							}}
							className="border-0 cp-activation-key-table"
							columns={DEACTIVATE_COLUMNS}
							hasCheckbox
							hasPagination
							isLoading={loading}
							paginationConfig={paginationConfig}
							rows={activationKeysByStatusPaginated.map(
								(activationKey) =>
									getDeactivationKeysRows(activationKey)
							)}
						/>
					)}
				</div>
			</ClayTooltipProvider>

			<DeactivateKeysTableFooter
				accountKey={project?.accountKey}
				activationKeysByStatusPaginatedChecked={
					activationKeysByStatusPaginatedChecked
				}
				activationKeysState={[setActivationKeys]}
				productName={productName}
				sessionId={sessionId}
			/>
		</div>
	);
};

DeactivateKeysTable.Skeleton = DeactivateKeysSkeleton;

export default DeactivateKeysTable;
