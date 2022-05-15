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

import SpacingBox from '../../../../src/main/resources/META-INF/resources/page_editor/common/components/SpacingBox';

const SpacingBoxTest = ({onChange = () => {}, value = {}}) => (
	<SpacingBox
		fields={{
			marginBottom: {
				defaultValue: '0',
				label: 'margin-bottom',
				validValues: [
					{label: '0', value: '0'},
					{label: '10', value: '10'},
				],
			},
			marginLeft: {
				defaultValue: '0',
				label: 'margin-left',
				validValues: [
					{label: '0', value: '0'},
					{label: '10', value: '10'},
				],
			},
			marginRight: {
				defaultValue: '0',
				label: 'margin-right',
				validValues: [
					{label: '0', value: '0'},
					{label: '10', value: '10'},
				],
			},
			marginTop: {
				defaultValue: '0',
				label: 'margin-top',
				validValues: [
					{label: '0', value: '0'},
					{label: '10', value: '10'},
				],
			},
			paddingBottom: {
				defaultValue: '0',
				label: 'padding-bottom',
				validValues: [
					{label: '0', value: '0'},
					{label: '10', value: '10'},
				],
			},
			paddingLeft: {
				defaultValue: '0',
				label: 'padding-left',
				validValues: [
					{label: '0', value: '0'},
					{label: '10', value: '10'},
				],
			},
			paddingRight: {
				defaultValue: '0',
				label: 'padding-right',
				validValues: [
					{label: '0', value: '0'},
					{label: '10', value: '10'},
				],
			},
			paddingTop: {
				defaultValue: '0',
				label: 'padding-top',
				validValues: [
					{label: '0', value: '0'},
					{label: '10', value: '10'},
				],
			},
		}}
		onChange={onChange}
		value={value}
	/>
);

describe('SpacingBox', () => {
	let _getComputedStyle;
	let _liferayUtilSub;

	beforeEach(() => {
		_getComputedStyle = window.getComputedStyle;
		_liferayUtilSub = window.Liferay.Util.sub;
	});

	afterEach(() => {
		window.getComputedStyle = _getComputedStyle;
		window.Liferay.Util.sub = _liferayUtilSub;
	});

	it('renders given spacing values', async () => {
		window.getComputedStyle = () => {
			return {
				getPropertyValue: (key) => {
					return {
						'margin-top': '111px',
						'padding-left': '0px',
					}[key];
				},
			};
		};

		render(<SpacingBoxTest value={{marginTop: '10'}} />);

		expect(screen.getByLabelText('padding-left')).toHaveTextContent('0');
		expect(screen.getByLabelText('margin-top')).toHaveTextContent('111');
	});

	it('can be navigated with keyboard', () => {
		render(<SpacingBoxTest />);

		const grid = screen.getByRole('grid');

		screen.getByLabelText('margin-top').focus();

		fireEvent.keyDown(grid, {key: 'ArrowDown'});
		fireEvent.keyDown(grid, {key: 'ArrowRight'});
		fireEvent.keyDown(grid, {key: 'ArrowRight'});
		fireEvent.keyDown(grid, {key: 'ArrowDown'});
		fireEvent.keyDown(grid, {key: 'ArrowLeft'});
		fireEvent.keyDown(grid, {key: 'ArrowRight'});

		expect(screen.getByLabelText('padding-left')).toHaveFocus();
	});

	it('can be used to update spacing', () => {
		window.Liferay.Util.sub = (key, args) =>
			args.reduce((key, arg) => key.replace('x', arg), key);

		const onChange = jest.fn();
		render(<SpacingBoxTest onChange={onChange} />);

		fireEvent.click(screen.getByLabelText('padding-left'));
		fireEvent.click(screen.getByLabelText('set-padding-left-to-10'));

		expect(onChange).toHaveBeenCalledWith('paddingLeft', '10');
	});

	it('shows token value next to token name in the dropdown', () => {
		window.getComputedStyle = () => {
			return {
				getPropertyValue: (key) => {
					return {'padding-left': '111px'}[key];
				},
			};
		};

		render(<SpacingBoxTest />);

		fireEvent.click(screen.getByLabelText('padding-left'));
		expect(screen.getByText('111')).toBeInTheDocument();
	});
});
