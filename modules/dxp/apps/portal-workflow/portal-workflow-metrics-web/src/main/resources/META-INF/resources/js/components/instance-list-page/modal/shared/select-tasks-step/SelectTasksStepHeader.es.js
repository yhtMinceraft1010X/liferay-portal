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

import {usePrevious} from '@liferay/frontend-js-react-web';
import {ManagementToolbar} from 'frontend-js-components-web';
import React, {useContext, useEffect} from 'react';

import ResultsBar from '../../../../../shared/components/results-bar/ResultsBar.es';
import ToolbarWithSelection from '../../../../../shared/components/toolbar-with-selection/ToolbarWithSelection.es';
import {useFilter} from '../../../../../shared/hooks/useFilter.es';
import {AppContext} from '../../../../AppContext.es';
import AssigneeFilter from '../../../../filter/AssigneeFilter.es';
import ProcessStepFilter from '../../../../filter/ProcessStepFilter.es';
import {ModalContext} from '../../ModalProvider.es';

function Header({items = [], instanceIds, totalCount, withoutUnassigned}) {
	const {userId, userName} = useContext(AppContext);
	const filterKeys = ['processStep', 'assignee'];
	const prefixKey = 'bulk';
	const prefixKeys = [prefixKey];
	const previousCount = usePrevious(totalCount);
	const {
		processId,
		selectTasks: {selectAll, tasks},
		setSelectTasks,
	} = useContext(ModalContext);

	const {prefixedKeys, selectedFilters} = useFilter({
		filterKeys,
		prefixKeys,
		withoutRouteParams: true,
	});

	const {
		filterValues: {assigneeIds: userIds = [], slaStatuses, taskNames},
	} = useFilter({});

	const availableUsers = withoutUnassigned ? [userId] : [userId, '-1'];
	const assigneeIds = userIds.filter((id) => availableUsers.includes(id));
	const currentAndUnassigned = [userId, '-1'].every((user) =>
		userIds.includes(user)
	);
	const hideAssigneeFilter = !currentAndUnassigned && userIds.length;

	const stepFilterOptions = {
		requestBody: {
			assigneeIds: withoutUnassigned ? availableUsers : assigneeIds,
			instanceIds,
			processId,
			slaStatuses,
			taskNames,
		},
		requestMethod: 'post',
		requestUrl: '/tasks?page=0&pageSize=0',
		withoutRouteParams: true,
	};

	const selectedOnPage = tasks.filter((item) =>
		items.find(({id}) => id === item.id)
	);

	const allPageSelected =
		items.length > 0 && items.length === selectedOnPage.length;

	const checkbox = {
		checked: allPageSelected || selectAll,
		indeterminate:
			selectedOnPage.length > 0 && !allPageSelected && !selectAll,
	};

	const remainingItems = items.filter(
		(item) => !tasks.find(({id}) => item.id === id)
	);

	const toolbarActive = tasks.length > 0;

	useEffect(() => {
		if (
			selectAll &&
			remainingItems.length > 0 &&
			previousCount === totalCount
		) {
			setSelectTasks({selectAll, tasks: items});
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [items]);

	useEffect(() => {
		setSelectTasks((selectTasks) => ({
			...selectTasks,
			selectAll: totalCount > 0 && totalCount === tasks.length,
		}));
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [totalCount]);

	const handleClear = () => {
		setSelectTasks({selectAll: false, tasks: []});
	};

	const handleCheck = (checked) => () => {
		const updatedItems = checked
			? [...tasks, ...remainingItems]
			: tasks.filter((item) => !items.find(({id}) => id === item.id));

		setSelectTasks({
			selectAll: totalCount > 0 && totalCount === updatedItems.length,
			tasks: updatedItems,
		});
	};

	return (
		<>
			<ToolbarWithSelection
				{...checkbox}
				active={toolbarActive}
				handleCheck={handleCheck(
					!checkbox.indeterminate && !selectAll && !allPageSelected
				)}
				handleClear={handleClear}
				handleSelectAll={() => {
					setSelectTasks({
						selectAll: true,
						tasks: items,
					});
				}}
				selectAll={selectAll}
				selectedCount={tasks.length}
				totalCount={totalCount}
			>
				{!toolbarActive && (
					<>
						<ManagementToolbar.Item>
							<strong className="ml-0 mr-0 navbar-text">
								{Liferay.Language.get('filter-by')}
							</strong>
						</ManagementToolbar.Item>

						<ProcessStepFilter
							options={stepFilterOptions}
							prefixKey={prefixKey}
							processId={processId}
						/>

						{!withoutUnassigned && !hideAssigneeFilter && (
							<AssigneeFilter
								options={{withoutRouteParams: true}}
								prefixKey={prefixKey}
								processId={processId}
								staticData={[{id: userId, name: userName}]}
							/>
						)}
					</>
				)}
			</ToolbarWithSelection>

			{selectedFilters.length > 0 && (
				<ResultsBar>
					<ResultsBar.TotalCount totalCount={totalCount} />

					<ResultsBar.FilterItems
						filters={selectedFilters}
						withoutRouteParams
					/>

					<ResultsBar.Clear
						filterKeys={prefixedKeys}
						filters={selectedFilters}
						withoutRouteParams
					/>
				</ResultsBar>
			)}
		</>
	);
}

export default Header;
