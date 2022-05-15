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

import ClayTabs from '@clayui/tabs';
import {useFeatureFlag} from 'data-engine-js-components-web';
import {fetch} from 'frontend-js-web';
import React, {useContext, useEffect, useState} from 'react';

import {invalidateRequired} from '../../hooks/useForm';
import {defaultLanguageId} from '../../utils/locale';
import SidePanelContent, {closeSidePanel, openToast} from '../SidePanelContent';
import BasicInfoScreen from './BasicInfoScreen/BasicInfoScreen';
import {DefaultFilterScreen} from './DefaultFilterScreen/DefaultFilterScreen';
import {DefaultSortScreen} from './DefaultSortScreen/DefaultSortScreen';
import ViewBuilderScreen from './ViewBuilderScreen/ViewBuilderScreen';
import ViewContext, {TYPES, ViewContextProvider} from './context';
import {TObjectField, TObjectView, TWorkflowStatus} from './types';

const TABS = [
	{
		Component: BasicInfoScreen,
		label: Liferay.Language.get('basic-info'),
	},
	{
		Component: ViewBuilderScreen,
		label: Liferay.Language.get('view-builder'),
	},
	{
		Component: DefaultSortScreen,
		label: Liferay.Language.get('default-sort'),
	},
];

const HEADERS = new Headers({
	'Accept': 'application/json',
	'Content-Type': 'application/json',
});

const CustomView: React.FC<React.HTMLAttributes<HTMLElement>> = () => {
	const [{isViewOnly, objectView, objectViewId}, dispatch] = useContext(
		ViewContext
	);

	const [activeIndex, setActiveIndex] = useState<number>(0);
	const [loading, setLoading] = useState<boolean>(true);

	const flags = useFeatureFlag();

	if (TABS.length < 4 && flags['LPS-144957']) {
		TABS.push({
			Component: DefaultFilterScreen,
			label: Liferay.Language.get('default-filters'),
		});
	}

	useEffect(() => {
		const makeFetch = async () => {
			const objectViewResponse = await fetch(
				`/o/object-admin/v1.0/object-views/${objectViewId}`,
				{
					headers: HEADERS,
					method: 'GET',
				}
			);

			const {
				defaultObjectView,
				name,
				objectDefinitionId,
				objectViewColumns,
				objectViewSortColumns,
			} = (await objectViewResponse.json()) as any;

			const objectFieldsResponse = await fetch(
				`/o/object-admin/v1.0/object-definitions/${objectDefinitionId}/object-fields`,
				{
					headers: HEADERS,
					method: 'GET',
				}
			);

			const objectView = {
				defaultObjectView,
				name,
				objectDefinitionId,
				objectViewColumns,
				objectViewSortColumns,
			};

			dispatch({
				payload: {
					objectView,
				},
				type: TYPES.ADD_OBJECT_VIEW,
			});

			const {
				items: objectFields,
			}: {
				items: TObjectField[];
			} = (await objectFieldsResponse.json()) as any;

			dispatch({
				payload: {
					objectFields,
					objectView,
				},
				type: TYPES.ADD_OBJECT_FIELDS,
			});

			setLoading(false);
		};

		makeFetch();
	}, [objectViewId, dispatch]);

	const removeUnnecessaryPropertiesFromObjectView = (
		objectView: TObjectView
	) => {
		const {objectViewColumns, objectViewSortColumns} = objectView;

		const newObjectViewColumns = objectViewColumns.map((viewColumn) => {
			return {
				label: viewColumn.label,
				objectFieldName: viewColumn.objectFieldName,
				priority: viewColumn.priority,
			};
		});

		const newObjectViewSortColumns = objectViewSortColumns.map(
			(sortColumn) => {
				return {
					objectFieldName: sortColumn.objectFieldName,
					priority: sortColumn.priority,
					sortOrder: sortColumn.sortOrder,
				};
			}
		);

		const newObjectView = {
			...objectView,
			objectViewColumns: newObjectViewColumns,
			objectViewSortColumns: newObjectViewSortColumns,
		};

		return newObjectView;
	};

	const handleSaveObjectView = async () => {
		const newObjectView = removeUnnecessaryPropertiesFromObjectView(
			objectView
		);

		const {objectViewColumns} = newObjectView;

		if (invalidateRequired(objectView.name[defaultLanguageId])) {
			openToast({
				message: Liferay.Language.get('a-name-is-required'),
				type: 'danger',
			});

			return;
		}

		if (!objectView.defaultObjectView || objectViewColumns.length !== 0) {
			const response = await fetch(
				`/o/object-admin/v1.0/object-views/${objectViewId}`,
				{
					body: JSON.stringify(newObjectView),
					headers: HEADERS,
					method: 'PUT',
				}
			);

			if (response.status === 401) {
				window.location.reload();
			}
			else if (response.ok) {
				closeSidePanel();

				openToast({
					message: Liferay.Language.get(
						'modifications-saved-successfully'
					),
				});
			}
			else {
				const {
					title = Liferay.Language.get('an-error-occurred'),
				} = (await response.json()) as any;

				openToast({
					message: title,
					type: 'danger',
				});
			}
		}
		else {
			openToast({
				message: Liferay.Language.get(
					'default-view-must-have-at-least-one-column'
				),
				type: 'danger',
			});
		}
	};

	return (
		<SidePanelContent
			onSave={handleSaveObjectView}
			readOnly={isViewOnly || loading}
			title={Liferay.Language.get('custom-view')}
		>
			<ClayTabs className="side-panel-iframe__tabs">
				{TABS.map(({label}, index) => (
					<ClayTabs.Item
						active={activeIndex === index}
						key={index}
						onClick={() => setActiveIndex(index)}
					>
						{label}
					</ClayTabs.Item>
				))}
			</ClayTabs>

			<ClayTabs.Content activeIndex={activeIndex} fade>
				{TABS.map(({Component}, index) => (
					<ClayTabs.TabPane key={index}>
						{!loading && <Component />}
					</ClayTabs.TabPane>
				))}
			</ClayTabs.Content>
		</SidePanelContent>
	);
};
interface ICustomViewWrapperProps extends React.HTMLAttributes<HTMLElement> {
	isViewOnly: boolean;
	objectViewId: string;
	workflowStatusJSONArray: TWorkflowStatus[];
}

const CustomViewWrapper: React.FC<ICustomViewWrapperProps> = ({
	isViewOnly,
	objectViewId,
	workflowStatusJSONArray,
}) => {
	return (
		<ViewContextProvider
			value={{
				isViewOnly,
				objectViewId,
				workflowStatusJSONArray,
			}}
		>
			<CustomView />
		</ViewContextProvider>
	);
};

export default CustomViewWrapper;
