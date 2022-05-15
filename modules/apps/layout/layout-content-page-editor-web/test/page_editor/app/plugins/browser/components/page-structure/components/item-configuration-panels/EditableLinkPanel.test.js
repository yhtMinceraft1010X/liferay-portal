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
import {act, fireEvent, getByLabelText, render} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import React from 'react';

import {EDITABLE_FRAGMENT_ENTRY_PROCESSOR} from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/editableFragmentEntryProcessor';
import {EDITABLE_TYPES} from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/editableTypes';
import {StoreAPIContextProvider} from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/StoreContext';
import serviceFetch from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/services/serviceFetch';
import updateEditableValues from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/thunks/updateEditableValues';
import EditableLinkPanel from '../../../../../../../../../src/main/resources/META-INF/resources/page_editor/plugins/browser/components/page-structure/components/item-configuration-panels/EditableLinkPanel';

jest.mock(
	'../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/services/serviceFetch',
	() => jest.fn(() => Promise.resolve({fieldValue: 'fieldValue'}))
);

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
		},
	})
);

jest.mock(
	'../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/thunks/updateEditableValues',
	() => jest.fn()
);

const getEditableConfig = (editableValues) => {
	return editableValues[EDITABLE_FRAGMENT_ENTRY_PROCESSOR]['editable-id-0']
		.config;
};

function getStateWithConfig(config = {}) {
	return {
		fragmentEntryLinks: {
			0: {
				editableValues: {
					[EDITABLE_FRAGMENT_ENTRY_PROCESSOR]: {
						'editable-id-0': {
							config,
						},
					},
				},
			},
		},
		languageId: 'en_US',
		mappingFields: [],
		pageContents: [],
		segmentsExperienceId: 0,
	};
}

function renderLinkPanel(
	{state = getStateWithConfig(), type = EDITABLE_TYPES.text} = {},
	dispatch = () => {}
) {
	return render(
		<StoreAPIContextProvider dispatch={dispatch} getState={() => state}>
			<EditableLinkPanel
				item={{
					editableId: 'editable-id-0',
					fragmentEntryLinkId: '0',
					itemId: '',
					type,
				}}
			/>
		</StoreAPIContextProvider>,
		{
			baseElement: document.body,
		}
	);
}

describe('EditableLinkPanel', () => {
	afterEach(() => {
		serviceFetch.mockClear();
		updateEditableValues.mockClear();
	});

	it('renders manual selection panel', () => {
		const {getByLabelText, getByText, queryByText} = renderLinkPanel();

		expect(getByText('link')).toBeInTheDocument();
		expect(getByLabelText('url')).toBeInTheDocument();
		expect(getByText('open-in-a-new-tab')).toBeInTheDocument();

		expect(queryByText('item')).not.toBeInTheDocument();
	});

	it('shows mapping panel when changing link source', async () => {
		const {getByLabelText, queryByLabelText} = renderLinkPanel();

		const sourceTypeInput = getByLabelText('link');

		await act(async () => {
			fireEvent.change(sourceTypeInput, {
				target: {value: 'fromContentField'},
			});
		});

		expect(getByLabelText('item')).toBeInTheDocument();

		expect(queryByLabelText('url')).not.toBeInTheDocument();
	});

	it('shows the url and target values when previously saved', () => {
		const {getByLabelText} = renderLinkPanel({
			state: getStateWithConfig({
				href: 'http://liferay.com',
				target: '_blank',
			}),
		});

		expect(getByLabelText('url')).toHaveValue('http://liferay.com');
		expect(getByLabelText('open-in-a-new-tab')).toBeChecked();
	});

	it('does not check the target value when is different from _blank', () => {
		const {getByLabelText} = renderLinkPanel({
			state: getStateWithConfig({
				target: '_self',
			}),
		});

		expect(getByLabelText('open-in-a-new-tab')).not.toBeChecked();
	});

	it('shows mapping panel when editable link is mapped', async () => {
		await act(async () => {
			renderLinkPanel({
				state: getStateWithConfig({
					classNameId: 1,
					classPK: 1,
					fieldId: 'text',
					target: '_blank',
				}),
			});
		});

		expect(getByLabelText(document.body, 'item')).toBeInTheDocument();
		expect(
			getByLabelText(document.body, 'open-in-a-new-tab')
		).toBeChecked();
	});

	it('shows mapping panel when editable link is mapped', async () => {
		await act(async () => {
			renderLinkPanel({
				state: getStateWithConfig({
					classNameId: 1,
					classPK: 1,
					fieldId: 'text',
					target: '_blank',
				}),
			});
		});

		expect(getByLabelText(document.body, 'item')).toBeInTheDocument();
		expect(
			getByLabelText(document.body, 'open-in-a-new-tab')
		).toBeChecked();
	});

	it('shows mapped url when is available', async () => {
		serviceFetch.mockImplementation(() =>
			Promise.resolve({fieldValue: 'value'})
		);

		await act(async () => {
			renderLinkPanel({
				state: getStateWithConfig({
					classNameId: 1,
					classPK: 1,
					fieldId: 'text',
					target: '_blank',
				}),
			});
		});

		expect(getByLabelText(document.body, 'item')).toBeInTheDocument();
		expect(getByLabelText(document.body, 'url')).toBeInTheDocument();
		expect(getByLabelText(document.body, 'url')).toHaveValue('value');
		expect(
			getByLabelText(document.body, 'open-in-a-new-tab')
		).toBeChecked();
	});

	it('clear the config when changing source type', async () => {
		let editableConfig;

		updateEditableValues.mockImplementation(({editableValues}) => {
			editableConfig = getEditableConfig(editableValues);
		});

		await act(async () => {
			renderLinkPanel({
				state: getStateWithConfig({
					classNameId: 1,
					classPK: 1,
					fieldId: 'text',
					target: '_blank',
				}),
			});
		});

		await act(async () => {
			const sourceTypeInput = getByLabelText(document.body, 'link');

			fireEvent.change(sourceTypeInput, {
				target: {value: 'manual'},
			});
		});

		expect(editableConfig).toEqual({
			alt: '',
			imageConfiguration: {},
			mapperType: 'link',
		});

		expect(getByLabelText(document.body, 'url')).toHaveValue('');
		expect(
			getByLabelText(document.body, 'open-in-a-new-tab')
		).toBeChecked();
	});

	it('calls dispatch with the href value typed in manual mode', async () => {
		let editableConfig;
		updateEditableValues.mockImplementation(({editableValues}) => {
			editableConfig = getEditableConfig(editableValues);
		});

		const {getByLabelText} = renderLinkPanel({
			state: getStateWithConfig({}),
		});

		const hrefInput = getByLabelText('url');

		userEvent.type(hrefInput, 'http://google.com');

		fireEvent.blur(hrefInput);

		expect(updateEditableValues).toHaveBeenCalled();

		expect(editableConfig).toEqual({
			href: {en_US: 'http://google.com'},
			mapperType: 'link',
			target: '',
		});
	});

	it('calls dispatch without mapperType when editable type is link', async () => {
		let editableConfig;
		updateEditableValues.mockImplementation(({editableValues}) => {
			editableConfig = getEditableConfig(editableValues);
		});

		const {getByLabelText} = renderLinkPanel({
			state: getStateWithConfig({}),
			type: EDITABLE_TYPES.link,
		});

		const hrefInput = getByLabelText('url');

		userEvent.type(hrefInput, 'http://google.com');

		fireEvent.blur(hrefInput);

		expect(updateEditableValues).toHaveBeenCalled();

		expect(editableConfig).toEqual({
			href: {en_US: 'http://google.com'},
			target: '',
		});
	});
});
