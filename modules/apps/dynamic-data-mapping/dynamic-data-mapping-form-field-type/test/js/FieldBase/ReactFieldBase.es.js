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

import {act, fireEvent, render} from '@testing-library/react';
import {PageProvider} from 'data-engine-js-components-web';
import React from 'react';

import '@testing-library/jest-dom/extend-expect';

import {FieldBase} from '../../../src/main/resources/META-INF/resources/FieldBase/ReactFieldBase.es';

const spritemap = 'icons.svg';

const FieldBaseWithProvider = (props) => (
	<PageProvider value={{editingLanguageId: 'en_US'}}>
		<FieldBase {...props} />
	</PageProvider>
);

describe('ReactFieldBase', () => {
	// eslint-disable-next-line no-console
	const originalWarn = console.warn;

	beforeAll(() => {
		window.themeDisplay = {
			...window.themeDisplay,
			getPathThemeImages: () => 'http://localhost:8080',
		};

		// eslint-disable-next-line no-console
		console.warn = (...args) => {
			if (/DataProvider: Trying/.test(args[0])) {
				return;
			}
			originalWarn.call(console, ...args);
		};
	});

	afterAll(() => {
		// eslint-disable-next-line no-console
		console.warn = originalWarn;
	});

	beforeEach(() => {
		jest.useFakeTimers();
		fetch.mockResponseOnce(JSON.stringify({}));
	});

	it('renders the default markup', () => {
		const {container} = render(
			<FieldBaseWithProvider spritemap={spritemap} />
		);

		act(() => {
			jest.runAllTimers();
		});

		expect(container).toMatchSnapshot();
	});

	it('renders the FieldBase with required', () => {
		const {container} = render(
			<FieldBaseWithProvider required spritemap={spritemap} />
		);

		act(() => {
			jest.runAllTimers();
		});

		expect(container).toMatchSnapshot();
	});

	it('renders the FieldBase with id', () => {
		const {container} = render(
			<FieldBaseWithProvider id="Id" spritemap={spritemap} />
		);

		act(() => {
			jest.runAllTimers();
		});

		expect(container).toMatchSnapshot();
	});

	it('renders the FieldBase with help text', () => {
		const {container} = render(
			<FieldBaseWithProvider
				spritemap={spritemap}
				tip="Type something!"
			/>
		);

		act(() => {
			jest.runAllTimers();
		});

		expect(container).toMatchSnapshot();
	});

	it('renders the FieldBase with label', () => {
		const {container} = render(
			<FieldBaseWithProvider label="Text" spritemap={spritemap} />
		);

		act(() => {
			jest.runAllTimers();
		});

		expect(container).toMatchSnapshot();
	});

	it('renders the FieldBase with tooltip', () => {
		const {container} = render(
			<FieldBaseWithProvider spritemap={spritemap} tooltip="Tooltip" />
		);

		act(() => {
			jest.runAllTimers();
		});

		expect(container.querySelector('.ddm-tooltip')).not.toBeNull();
	});

	it('does not render the label if showLabel is false', () => {
		const {container} = render(
			<FieldBaseWithProvider
				label="Text"
				showLabel={false}
				spritemap={spritemap}
			/>
		);

		act(() => {
			jest.runAllTimers();
		});

		expect(container).toMatchSnapshot();
	});

	it('renders the FieldBase with contentRenderer', () => {
		const {container} = render(
			<FieldBaseWithProvider spritemap={spritemap}>
				<div>
					<h1>Foo bar</h1>
				</div>
			</FieldBaseWithProvider>
		);

		act(() => {
			jest.runAllTimers();
		});

		expect(container).toMatchSnapshot();
	});

	it('renders the add button when repeatable is true', () => {
		const {container} = render(
			<FieldBaseWithProvider
				label="Text"
				repeatable={true}
				showLabel={false}
				spritemap={spritemap}
			/>
		);
		act(() => {
			jest.runAllTimers();
		});

		expect(container).toMatchSnapshot();
	});

	it('does not render the add button when repeatable is true and the maximum limit of repetions is reached', () => {
		const {container} = render(
			<FieldBaseWithProvider
				label="Text"
				overMaximumRepetitionsLimit={true}
				repeatable={true}
				showLabel={false}
				spritemap={spritemap}
			/>
		);
		act(() => {
			jest.runAllTimers();
		});

		expect(container).toMatchSnapshot();
	});

	it('shows the popover for Format field when hovering over the tooltip icon', async () => {
		const {container, findByTestId, getByRole, getByText} = render(
			<FieldBaseWithProvider
				fieldName="inputMaskFormat"
				spritemap={spritemap}
				tooltip="Tooltip Description"
			/>
		);

		const tooltipIcon = container.querySelector('.ddm-tooltip');

		fireEvent.mouseOver(tooltipIcon);

		const clayPopover = await findByTestId('clayPopover');

		expect(clayPopover.style).toHaveProperty('maxWidth', '256px');

		expect(getByRole('img')).toHaveAttribute('height', '170');
		expect(getByRole('img')).toHaveAttribute(
			'src',
			'http://localhost:8080/forms/input_mask_format.png'
		);
		expect(getByRole('img')).toHaveAttribute('width', '232');

		expect(getByText('input-mask-format')).toBeInTheDocument();
		expect(getByText('Tooltip Description')).toBeInTheDocument();
	});

	describe('Hide Field', () => {
		it('renders the FieldBase with hideField markup', () => {
			const {getAllByText, getByText} = render(
				<FieldBaseWithProvider
					hideField
					label="Text"
					spritemap={spritemap}
				/>
			);

			expect(getByText('hidden')).toBeInTheDocument();

			const allByText = getAllByText('Text');
			expect(allByText).toHaveLength(2);
			expect(allByText[0]).toBeInTheDocument();
			expect(allByText[1]).toBeInTheDocument();

			expect(getByText('hidden').parentNode).toHaveAttribute(
				'class',
				'label ml-1 label-secondary'
			);
			expect(allByText[0]).toHaveAttribute('class', 'text-secondary');
			expect(allByText[1]).toHaveAttribute('class', 'sr-only');
		});

		it('renders the FieldBase with hideField markup when the label is empty', () => {
			const {getByText} = render(
				<FieldBaseWithProvider
					hideField
					label=""
					spritemap={spritemap}
				/>
			);

			expect(getByText('hidden')).toBeInTheDocument();

			expect(getByText('hidden').parentNode).toHaveAttribute(
				'class',
				'label ml-1 label-secondary'
			);
		});
	});
});
