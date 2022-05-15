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
import {cleanup, render} from '@testing-library/react';
import React from 'react';
import {DndProvider} from 'react-dnd';
import {HTML5Backend} from 'react-dnd-html5-backend';

import {CollectionWithControls} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/components/layout-data-items';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/layoutDataItemTypes';
import {VIEWPORT_SIZES} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/viewportSizes';
import {config} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config/index';
import {
	ControlsProvider,
	useSelectItem,
} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/ControlsContext';
import {StoreAPIContextProvider} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/StoreContext';
import getLayoutDataItemClassName from '../../../../../src/main/resources/META-INF/resources/page_editor/app/utils/getLayoutDataItemClassName';
import getLayoutDataItemTopperUniqueClassName from '../../../../../src/main/resources/META-INF/resources/page_editor/app/utils/getLayoutDataItemTopperUniqueClassName';
import getLayoutDataItemUniqueClassName from '../../../../../src/main/resources/META-INF/resources/page_editor/app/utils/getLayoutDataItemUniqueClassName';

const COLLECTION_ID = 'COLLECTION_ID';

const renderCollection = ({
	isActive = true,
	collectionConfig = {styles: {}},
	viewportSize = VIEWPORT_SIZES.desktop,
	lockedSegment = false,
	hasUpdatePermission = true,
} = {}) => {
	const collection = {
		children: [],
		config: collectionConfig,
		itemId: COLLECTION_ID,
		parentId: null,
		type: LAYOUT_DATA_ITEM_TYPES.collection,
	};

	const layoutData = {
		items: {collection},
	};

	const AutoSelector = () => {
		useSelectItem()(isActive ? 'collection' : null);

		return null;
	};

	return render(
		<DndProvider backend={HTML5Backend}>
			<ControlsProvider>
				<StoreAPIContextProvider
					dispatch={() => {}}
					getState={() => ({
						permissions: {
							LOCKED_SEGMENTS_EXPERIMENT: lockedSegment,
							UPDATE: hasUpdatePermission,
						},
						selectedViewportSize: viewportSize,
					})}
				>
					<AutoSelector />

					<CollectionWithControls
						item={collection}
						layoutData={layoutData}
					/>
				</StoreAPIContextProvider>
			</ControlsProvider>
		</DndProvider>
	);
};

describe('CollectionWithControls', () => {
	afterEach(cleanup);

	it('shows a no-collection-selected message', () => {
		const {getByText} = renderCollection();

		expect(getByText('no-collection-selected-yet')).toBeInTheDocument();
	});

	it('removes all buttons if user has no permissions', () => {
		const {queryByTitle} = renderCollection({hasUpdatePermission: false});

		expect(queryByTitle('collection-display-configuration')).toBe(null);
	});

	it('removes all buttons if experience is locked', () => {
		const {queryByTitle} = renderCollection({lockedSegment: true});

		expect(queryByTitle('collection-display-configuration')).toBe(null);
	});

	it('does not allow deleting or duplicating the collection if user has no permissions', () => {
		const {queryByText} = renderCollection({
			hasUpdatePermission: false,
		});

		expect(queryByText('delete')).not.toBeInTheDocument();
		expect(queryByText('duplicate')).not.toBeInTheDocument();
	});

	it('does not show the collection if it has been hidden by the user', async () => {
		const {baseElement} = renderCollection({
			collectionConfig: {
				styles: {
					display: 'none',
				},
			},
		});

		const collection = baseElement.querySelector(
			'.page-editor__collection'
		);

		expect(collection).not.toBeVisible();
	});

	it('shows the collection if it has not been hidden by the user', async () => {
		const {baseElement} = renderCollection({
			collectionConfig: {
				styles: {
					display: 'block',
				},
			},
		});

		const collection = baseElement.querySelector(
			'.page-editor__collection'
		);

		expect(collection).toBeVisible();
	});

	it('set classes for referencing the item', () => {
		config.featureFlagLps132571 = true;

		const {baseElement} = renderCollection();

		const classes = [
			getLayoutDataItemClassName(LAYOUT_DATA_ITEM_TYPES.collection),
			getLayoutDataItemTopperUniqueClassName(COLLECTION_ID),
			getLayoutDataItemUniqueClassName(COLLECTION_ID),
		];

		classes.forEach((className) => {
			const item = baseElement.querySelector(`.${className}`);

			expect(item).toBeVisible();
		});
	});
});
