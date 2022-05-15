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
import userEvent from '@testing-library/user-event';
import React from 'react';

import {CONTAINER_DISPLAY_OPTIONS} from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/containerDisplayOptions';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/layoutDataItemTypes';
import {StoreAPIContextProvider} from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/StoreContext';
import updateItemConfig from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/thunks/updateItemConfig';
import ContainerGeneralPanel from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/plugins/browser/components/page-structure/components/item-configuration-panels/ContainerGeneralPanel';

jest.mock(
	'../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config',
	() => ({
		config: {
			availableLanguages: {
				en_US: {
					default: false,
					displayName: 'English (United States)',
					languageIcon: 'en-us',
					languageId: 'en_US',
					w3cLanguageId: 'en-US',
				},
			},
			commonStyles: [],
		},
	})
);

jest.mock(
	'../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/thunks/updateItemConfig',
	() => jest.fn()
);

function renderComponent(
	itemConfig = {
		tablet: {styles: {}},
	}
) {
	return render(
		<StoreAPIContextProvider
			dispatch={() => {}}
			getState={() => ({
				languageId: 'en_US',
				permissions: {UPDATE: true},
				segmentsExperienceId: '0',
				selectedViewportSize: 'desktop',
			})}
		>
			<ContainerGeneralPanel
				item={{
					children: [],
					config: itemConfig,
					itemId: 'some-container-id',
					type: LAYOUT_DATA_ITEM_TYPES.container,
				}}
			/>
		</StoreAPIContextProvider>,
		{
			baseElement: document.body,
		}
	);
}

describe('ContainerGeneralPanel', () => {
	it('opens link in same tab by default', () => {
		renderComponent();

		expect(screen.getByLabelText('open-in-a-new-tab')).not.toBeChecked();
	});

	it('renders existing target', () => {
		renderComponent({link: {target: '_blank'}});

		expect(screen.getByLabelText('open-in-a-new-tab')).toBeChecked();
	});

	it('renders exiting href', () => {
		renderComponent({
			link: {href: {en_US: 'https://liferay.us'}},
		});

		expect(screen.getByLabelText('url')).toHaveValue('https://liferay.us');
	});

	it('stores link target', () => {
		renderComponent();

		const targetInput = screen.getByLabelText('open-in-a-new-tab');

		userEvent.click(targetInput);

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: {
				link: {
					href: {en_US: ''},
					target: '_blank',
				},
			},
			itemId: 'some-container-id',
			segmentsExperienceId: '0',
		});
	});

	it('stores link href as localizable', () => {
		renderComponent();

		const urlInput = screen.getByLabelText('url');

		userEvent.type(urlInput, 'https://liferay.us');
		fireEvent.blur(urlInput);

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: {
				link: {
					href: {en_US: 'https://liferay.us'},
					target: '',
				},
			},
			itemId: 'some-container-id',
			segmentsExperienceId: '0',
		});
	});

	it('calls dispatch method when changing the container width', async () => {
		renderComponent();

		const containerWidthSelect = screen.getByLabelText('container-width');

		fireEvent.change(containerWidthSelect, {
			target: {value: 'fixed'},
		});

		expect(updateItemConfig).toBeCalledWith(
			expect.objectContaining({
				itemConfig: {
					widthType: 'fixed',
				},
			})
		);
	});

	it('calls dispatch method when changing the content display', async () => {
		renderComponent();

		const contentDisplaySelect = screen.getByLabelText('content-display');

		fireEvent.change(contentDisplaySelect, {
			target: {value: 'flex-row'},
		});

		expect(updateItemConfig).toBeCalledWith(
			expect.objectContaining({
				itemConfig: {
					contentDisplay: 'flex-row',
				},
			})
		);
	});

	it('does not show Flex Wrap, Align and Justify selects when item is not flex container', async () => {
		renderComponent({
			contentDisplay: CONTAINER_DISPLAY_OPTIONS.block,
		});

		expect(screen.queryByLabelText('flex-wrap')).not.toBeInTheDocument();
		expect(screen.queryByLabelText('align-items')).not.toBeInTheDocument();
		expect(
			screen.queryByLabelText('justify-content')
		).not.toBeInTheDocument();
	});

	it('shows Flex Wrap, Align and Justify selects when item is flex container', async () => {
		renderComponent({
			contentDisplay: CONTAINER_DISPLAY_OPTIONS.flexRow,
		});

		expect(screen.getByLabelText('flex-wrap')).toBeInTheDocument();
		expect(screen.getByLabelText('align-items')).toBeInTheDocument();
		expect(screen.getByLabelText('justify-content')).toBeInTheDocument();
	});

	it('sets correct default values for Flex Wrap, Align and Justify when flex is selected', async () => {
		renderComponent({
			contentDisplay: CONTAINER_DISPLAY_OPTIONS.flexRow,
		});

		const flexWrapInput = screen.getByLabelText('flex-wrap');
		const alignInput = screen.getByLabelText('align-items');
		const justifyInput = screen.getByLabelText('justify-content');

		expect(flexWrapInput).toHaveValue('flex-nowrap');
		expect(alignInput).toHaveValue('align-items-stretch');
		expect(justifyInput).toHaveValue('justify-content-start');
	});
});
