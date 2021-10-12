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
import {act, cleanup, fireEvent, render} from '@testing-library/react';
import React from 'react';

import QuantitySelector from '../../../src/main/resources/META-INF/resources/components/quantity_selector/QuantitySelector';

describe('QuantitySelector', () => {
	describe('by default', () => {
		let Component;
		let onUpdateSpy;

		const defaultProps = {
			maxQuantity: 1000,
		};

		beforeEach(() => {
			jest.resetAllMocks();

			onUpdateSpy = jest.fn();
			Component = render(
				<QuantitySelector onUpdate={onUpdateSpy} {...defaultProps} />
			);
		});

		afterEach(() => {
			cleanup();

			Component = null;
			onUpdateSpy.mockReset();
		});

		it('renders as an Input element with default attributes', () => {
			const element = Component.container.querySelector('input');

			const defaultProps = {
				maxQuantity: 1000,
				minQuantity: 1,
				multipleQuantity: 1,
				quantity: 1,
			};

			expect(element).toBeInTheDocument();

			expect(element.max).toEqual(defaultProps.maxQuantity.toString());
			expect(element.min).toEqual(defaultProps.minQuantity.toString());
			expect(element.step).toEqual(
				defaultProps.multipleQuantity.toString()
			);
			expect(element.type).toEqual('number');
			expect(element.value).toEqual(defaultProps.quantity.toString());
		});

		it('accepts only number-typed input values', async () => {
			const element = Component.container.querySelector('input');
			const updatedValue = 'abc';

			await act(async () => {
				fireEvent.change(element, {target: {value: updatedValue}});
			});

			expect(element.value).not.toEqual(updatedValue);
			expect(element.value).toEqual('1');
		});

		it('updates the quantity if input value is changed', async () => {
			const element = Component.container.querySelector('input');
			const updatedValue = '2';

			await act(async () => {
				fireEvent.change(element, {target: {value: updatedValue}});
			});

			expect(element.value).toEqual(updatedValue);
		});

		it('updates the quantity to the default of 1 if input value is empty', async () => {
			const element = Component.container.querySelector('input');
			const updatedValue = '';

			await act(async () => {
				fireEvent.change(element, {target: {value: updatedValue}});
			});

			expect(element.value).toEqual('1');
		});

		it('calls the onUpdate callback on input value change', async () => {
			const element = Component.container.querySelector('input');
			const updatedValue = 2;

			await act(async () => {
				fireEvent.change(element, {
					target: {value: updatedValue.toString()},
				});
			});

			expect(onUpdateSpy).toHaveBeenCalledTimes(1);
			expect(onUpdateSpy).toHaveBeenCalledWith(updatedValue);
		});

		it('Update Maximum value when maximum exceeded', async () => {
			const element = Component.container.querySelector('input');
			const updatedValue = defaultProps.maxQuantity + 10;

			await act(async () => {
				fireEvent.change(element, {
					target: {value: updatedValue.toString()},
				});
			});

			expect(onUpdateSpy).toHaveBeenCalledTimes(1);
			expect(onUpdateSpy).toHaveBeenCalledWith(defaultProps.maxQuantity);
		});

		it('Update Minimum value when minimum exceeded', async () => {
			const element = Component.container.querySelector('input');
			const updatedValue = -1;

			await act(async () => {
				fireEvent.change(element, {
					target: {value: updatedValue.toString()},
				});
			});

			expect(onUpdateSpy).toHaveBeenCalledTimes(1);
			expect(onUpdateSpy).toHaveBeenCalledWith(1);
		});
	});

	describe('by custom props', () => {
		beforeEach(() => {
			jest.resetAllMocks();
		});

		afterEach(() => {
			cleanup();
		});

		it('floors the input value to the closest lower multiple if multipleQuantity > 1', async () => {
			const onUpdateSpy = jest.fn();

			const optionSettingsProps = {
				multipleQuantity: 2,
			};

			const defaultProps = {
				quantity: 1,
				...optionSettingsProps,
			};

			const Component = render(
				<QuantitySelector onUpdate={onUpdateSpy} {...defaultProps} />
			);

			const element = Component.getByRole('textbox');
			const updatedValue = 53;

			await act(async () => {
				fireEvent.change(element, {
					target: {value: updatedValue.toString()},
				});
			});

			expect(onUpdateSpy).toHaveBeenCalledTimes(1);
			expect(onUpdateSpy).toHaveBeenCalledWith(52);
		});

		it('updates the quantity to the default of minQuantity if defined and if input value is empty', async () => {
			const onUpdateSpy = jest.fn();

			const defaultProps = {
				minQuantity: 5,
				quantity: 1,
			};

			const Component = render(
				<QuantitySelector onUpdate={onUpdateSpy} {...defaultProps} />
			);

			const element = Component.container.querySelector('input');
			const updatedValue = '';

			await act(async () => {
				fireEvent.change(element, {target: {value: updatedValue}});
			});

			expect(element.value).toEqual(defaultProps.minQuantity.toString());
		});

		it('disables the input element if required via prop', () => {
			const Component = render(<QuantitySelector disabled={true} />);

			const element = Component.container.querySelector('input');

			expect(element).toBeInTheDocument();
			expect(element).toBeDisabled();
		});

		it('disables the select element if required via prop', () => {
			const Component = render(
				<QuantitySelector
					allowedQuantities={[1, 2, 3]}
					disabled={true}
				/>
			);

			const element = Component.container.querySelector('select');

			expect(element).toBeInTheDocument();
			expect(element).toBeDisabled();
		});
	});
});
