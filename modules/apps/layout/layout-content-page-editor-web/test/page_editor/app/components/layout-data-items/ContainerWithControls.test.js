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

import '@testing-library/jest-dom/extend-expect';
import {cleanup, queryByText, render} from '@testing-library/react';
import React from 'react';
import {DndProvider} from 'react-dnd';
import {HTML5Backend} from 'react-dnd-html5-backend';

import {ContainerWithControls} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/components/layout-data-items';
import {config} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/layoutDataItemTypes';
import {VIEWPORT_SIZES} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/viewportSizes';
import {
	ControlsProvider,
	useSelectItem,
} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/ControlsContext';
import {StoreAPIContextProvider} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/StoreContext';
import getLayoutDataItemClassName from '../../../../../src/main/resources/META-INF/resources/page_editor/app/utils/getLayoutDataItemClassName';
import getLayoutDataItemTopperUniqueClassName from '../../../../../src/main/resources/META-INF/resources/page_editor/app/utils/getLayoutDataItemTopperUniqueClassName';
import getLayoutDataItemUniqueClassName from '../../../../../src/main/resources/META-INF/resources/page_editor/app/utils/getLayoutDataItemUniqueClassName';

jest.mock(
	'../../../../../src/main/resources/META-INF/resources/page_editor/app/config',
	() => ({
		config: {
			commonStyles: [
				{
					styles: [
						{
							defaultValue: 'left',
							name: 'textAlign',
						},
					],
				},
			],
			frontendTokens: {},
		},
	})
);

const CONTAINER_ID = 'CONTAINER_ID';

const renderContainer = ({
	activeItemId = 'container',
	containerConfig = {styles: {}},
	hasUpdatePermissions = true,
	lockedExperience = false,
} = {}) => {
	const container = {
		children: [],
		config: containerConfig,
		itemId: CONTAINER_ID,
		parentId: null,
		type: LAYOUT_DATA_ITEM_TYPES.container,
	};

	const layoutData = {
		items: {container},
	};

	const AutoSelect = () => {
		useSelectItem()(activeItemId);

		return null;
	};

	return render(
		<DndProvider backend={HTML5Backend}>
			<ControlsProvider>
				<StoreAPIContextProvider
					getState={() => ({
						permissions: {
							LOCKED_SEGMENTS_EXPERIMENT: lockedExperience,
							UPDATE: hasUpdatePermissions,
						},
						selectedViewportSize: VIEWPORT_SIZES.desktop,
					})}
				>
					<AutoSelect />

					<ContainerWithControls
						item={container}
						layoutData={layoutData}
					/>
				</StoreAPIContextProvider>
			</ControlsProvider>
		</DndProvider>
	);
};

describe('ContainerWithControls', () => {
	afterEach(cleanup);

	it('does not add container class if user has no permissions', () => {
		const {baseElement} = renderContainer({hasUpdatePermissions: false});

		expect(baseElement.querySelector('.page-editor__container')).toBe(null);
	});

	it('does not allow deleting or duplicating the container if user has no permissions', () => {
		const {baseElement} = renderContainer({hasUpdatePermissions: false});

		expect(queryByText(baseElement, 'delete')).not.toBeInTheDocument();
		expect(queryByText(baseElement, 'duplicate')).not.toBeInTheDocument();
	});

	it('does not show the container if it has been hidden by the user', async () => {
		const {baseElement} = renderContainer({
			containerConfig: {
				styles: {
					display: 'none',
				},
			},
		});

		const container = baseElement.querySelector('.page-editor__container');

		expect(container).not.toBeVisible();
	});

	it('shows the container if it has not been hidden by the user', async () => {
		const {baseElement} = renderContainer({
			containerConfig: {
				styles: {
					display: 'block',
				},
			},
		});

		const container = baseElement.querySelector('.page-editor__container');

		expect(container).toBeVisible();
	});

	it('set classes for referencing the item', () => {
		config.featureFlagLps132571 = true;

		const {baseElement} = renderContainer();

		const classes = [
			getLayoutDataItemClassName(LAYOUT_DATA_ITEM_TYPES.container),
			getLayoutDataItemTopperUniqueClassName(CONTAINER_ID),
			getLayoutDataItemUniqueClassName(CONTAINER_ID),
		];

		classes.forEach((className) => {
			const item = baseElement.querySelector(`.${className}`);

			expect(item).toBeVisible();
		});
	});
});
