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
import {fireEvent, render, screen} from '@testing-library/react';
import React from 'react';

import {ResizeContextProvider} from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/ResizeContext';
import {StoreAPIContextProvider} from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/StoreContext';
import updateItemConfig from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/thunks/updateItemConfig';
import updateRowColumns from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/thunks/updateRowColumns';
import {RowGeneralPanel} from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/plugins/browser/components/page-structure/components/item-configuration-panels/RowGeneralPanel';

const ITEM_CONFIG = {
	gutters: true,
	modulesPerRow: 2,
	numberOfColumns: 2,
	verticalAlignment: 'top',
};

const RESIZE_CONTEXT_STATE = {
	customRow: false,
	resizing: false,
	setResizing: () => null,
	setUpdatedLayoutData: () => null,
	updatedLayoutData: null,
};

const STATE = {
	layoutData: {
		items: {
			'item-1': {
				config: {
					size: 6,
				},
			},
			'item-2': {
				config: {
					size: 6,
				},
			},
		},
	},
	permissions: {UPDATE: true},
	segmentsExperienceId: '0',
	selectedViewportSize: 'desktop',
};

const renderComponent = ({
	config = ITEM_CONFIG,
	state = STATE,
	contextState = RESIZE_CONTEXT_STATE,
	dispatch = () => {},
} = {}) =>
	render(
		<StoreAPIContextProvider
			dispatch={dispatch}
			getState={() => ({...STATE, ...state})}
		>
			<ResizeContextProvider
				value={{...RESIZE_CONTEXT_STATE, ...contextState}}
			>
				<RowGeneralPanel
					item={{
						children: ['item-1', 'item-2'],
						config: {...ITEM_CONFIG, ...config},
						itemId: '0',
						parentId: '',
						type: '',
					}}
				/>
			</ResizeContextProvider>
		</StoreAPIContextProvider>
	);

jest.mock(
	'../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config',
	() => ({
		config: {
			availableViewportSizes: {
				desktop: {label: 'Desktop'},
				landscapeMobile: {label: 'landscapeMobile'},
			},
			commonStyles: [],
		},
	})
);

jest.mock(
	'../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/thunks/updateItemConfig',
	() => jest.fn()
);

jest.mock(
	'../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/thunks/updateRowColumns',
	() => jest.fn()
);

describe('RowGeneralPanel', () => {
	afterEach(() => {
		updateItemConfig.mockClear();
		updateRowColumns.mockClear();
	});

	it('allows changing the number of modules of a grid', async () => {
		renderComponent();

		const input = screen.getByLabelText('number-of-modules');

		fireEvent.change(input, {
			target: {value: '6'},
		});

		expect(updateRowColumns).toHaveBeenCalledWith({
			itemId: '0',
			numberOfColumns: 6,
			segmentsExperienceId: '0',
			viewportSizeId: 'desktop',
		});
	});

	it('allows changing the gutter', async () => {
		renderComponent();

		const input = screen.getByLabelText('show-gutter');

		fireEvent.click(input);

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: {gutters: false},
			itemId: '0',
			segmentsExperienceId: '0',
		});
	});

	it('allows changing the modules per row', async () => {
		renderComponent();

		const input = screen.getByLabelText('layout');

		fireEvent.change(input, {
			target: {value: '2'},
		});

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: {
				modulesPerRow: 2,
			},
			itemId: '0',
			segmentsExperienceId: '0',
		});
	});

	it('allows custom value in modules per row when row is customized', async () => {
		renderComponent();

		const input = screen.getByLabelText('layout');

		expect(input).toHaveValue('2');
	});

	it('change label to custom when the column configuration is customized', async () => {
		renderComponent({
			state: {
				layoutData: {
					items: {
						'item-1': {
							config: {
								size: 5,
							},
						},
						'item-2': {
							config: {
								size: 7,
							},
						},
					},
				},
			},
		});

		const input = screen.getByLabelText('layout');

		expect(input).toHaveValue('custom');
	});

	it('allows changing the vertical alignment', async () => {
		renderComponent();

		const input = screen.getByLabelText('vertical-alignment');

		fireEvent.change(input, {
			target: {value: 'middle'},
		});

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: {
				verticalAlignment: 'middle',
			},
			itemId: '0',
			segmentsExperienceId: '0',
		});
	});

	it('allows inverse order when number of modules is 2 and modules per row is 1', async () => {
		renderComponent({
			config: {
				modulesPerRow: 1,
			},
			state: {
				layoutData: {
					items: {
						'item-1': {
							config: {
								size: 12,
							},
						},
						'item-2': {
							config: {
								size: 12,
							},
						},
					},
				},
			},
		});

		const input = screen.getByLabelText('inverse-order');

		fireEvent.click(input);

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: {
				reverseOrder: true,
			},
			itemId: '0',
			segmentsExperienceId: '0',
		});
	});

	it('allows changing layout for a given viewport', async () => {
		renderComponent({
			state: {
				selectedViewportSize: 'tablet',
			},
		});
		const input = screen.getByLabelText('layout');

		fireEvent.change(input, {
			target: {value: '1'},
		});

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: {
				tablet: {modulesPerRow: 1},
			},
			itemId: '0',
			segmentsExperienceId: '0',
		});
	});
});
