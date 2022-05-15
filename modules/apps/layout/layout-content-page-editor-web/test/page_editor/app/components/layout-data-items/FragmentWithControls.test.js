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
import {act, cleanup, queryByText, render} from '@testing-library/react';
import React from 'react';
import {DndProvider} from 'react-dnd';
import {HTML5Backend} from 'react-dnd-html5-backend';

import FragmentWithControls from '../../../../../src/main/resources/META-INF/resources/page_editor/app/components/layout-data-items/FragmentWithControls';
import {config} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/layoutDataItemTypes';
import {VIEWPORT_SIZES} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/viewportSizes';
import {
	ControlsProvider,
	useSelectItem,
} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/ControlsContext';
import {EditableProcessorContextProvider} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/EditableProcessorContext';
import {StoreAPIContextProvider} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/StoreContext';
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

jest.mock(
	'../../../../../src/main/resources/META-INF/resources/page_editor/app/services/serviceFetch',
	() => jest.fn(() => Promise.resolve({}))
);

const FRAGMENT_ID = 'FRAGMENT_ID';

const FRAGMENT_CLASS_NAME = 'FRAGMENT_CLASS_NAME';

const renderFragment = ({
	activeItemId = FRAGMENT_ID,
	editableValues = {},
	fragmentConfig = {styles: {}},
	hasUpdatePermissions = true,
	lockedExperience = false,
} = {}) => {
	const fragmentEntryLink = {
		cssClass: FRAGMENT_CLASS_NAME,
		editableValues,
		fragmentEntryLinkId: 'fragmentEntryLink',
	};

	const fragment = {
		children: [],
		config: {
			...fragmentConfig,
			fragmentEntryLinkId: fragmentEntryLink.fragmentEntryLinkId,
		},
		itemId: FRAGMENT_ID,
		parentId: null,
		type: LAYOUT_DATA_ITEM_TYPES.fragment,
	};

	const layoutData = {
		items: {fragment},
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
						fragmentEntryLinks: {
							[fragmentEntryLink.fragmentEntryLinkId]: fragmentEntryLink,
						},
						permissions: {
							LOCKED_SEGMENTS_EXPERIMENT: lockedExperience,
							UPDATE: hasUpdatePermissions,
						},
						selectedViewportSize: VIEWPORT_SIZES.desktop,
					})}
				>
					<EditableProcessorContextProvider>
						<AutoSelect />

						<FragmentWithControls
							item={fragment}
							layoutData={layoutData}
						/>
					</EditableProcessorContextProvider>
				</StoreAPIContextProvider>
			</ControlsProvider>
		</DndProvider>
	);
};

describe('FragmentWithControls', () => {
	afterEach(cleanup);

	it('does not allow deleting or duplicating the fragment if user has no permissions', async () => {
		await act(async () => {
			renderFragment({hasUpdatePermissions: false});
		});

		expect(queryByText(document.body, 'delete')).not.toBeInTheDocument();
		expect(queryByText(document.body, 'duplicate')).not.toBeInTheDocument();
	});

	it('does not show the fragment if it has been hidden by the user', async () => {
		await act(async () => {
			renderFragment({
				fragmentConfig: {
					styles: {
						display: 'none',
					},
				},
			});
		});

		expect(
			document.body.querySelector('.page-editor__fragment-content')
		).not.toBeVisible();
	});

	it('shows the fragment if it has not been hidden by the user', async () => {
		await act(async () => {
			renderFragment({
				fragmentConfig: {
					styles: {
						display: 'block',
					},
				},
			});
		});

		expect(
			document.body.querySelector('.page-editor__fragment-content')
		).toBeVisible();
	});

	it('set classes for referencing the item', () => {
		config.featureFlagLps132571 = true;

		const {baseElement} = renderFragment();

		const classes = [
			FRAGMENT_CLASS_NAME,
			getLayoutDataItemTopperUniqueClassName(FRAGMENT_ID),
			getLayoutDataItemUniqueClassName(FRAGMENT_ID),
		];

		classes.forEach((className) => {
			const item = baseElement.querySelector(`.${className}`);

			expect(item).toBeVisible();
		});
	});

	it('does not set unique classNames when it has inner common styles', () => {
		config.featureFlagLps132571 = true;

		const {baseElement} = renderFragment({
			editableValues: {
				['com.liferay.fragment.entry.processor.styles.StylesFragmentEntryProcessor']: {
					hasCommonStyles: true,
				},
			},
		});

		const classes = [
			getLayoutDataItemTopperUniqueClassName(FRAGMENT_ID),
			getLayoutDataItemUniqueClassName(FRAGMENT_ID),
		];

		classes.forEach((className) => {
			const item = baseElement.querySelector(`.${className}`);

			expect(item).toBeNull();
		});
	});
});
