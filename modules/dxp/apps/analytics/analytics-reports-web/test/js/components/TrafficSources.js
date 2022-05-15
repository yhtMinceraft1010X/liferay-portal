/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import '@testing-library/jest-dom/extend-expect';
import {fireEvent, render, waitFor} from '@testing-library/react';
import React from 'react';

import TrafficSources from '../../../src/main/resources/META-INF/resources/js/components/TrafficSources';

const noop = () => {};
const formatter = new Intl.NumberFormat();

describe('TrafficSources', () => {
	it('displays the traffic sources with buttons to view keywords', async () => {
		const mockTrafficSourcesDataProvider = jest.fn(() =>
			Promise.resolve([
				{
					endpointURL: 'http://localhost:8080/',
					helpMessage: 'Testing Help Message',
					name: 'testing',
					share: 30.0,
					title: 'Testing',
					value: 32178,
				},
				{
					endpointURL: 'http://localhost:8080/',
					helpMessage: 'Second Testing Help Message',
					name: 'second-testing',
					share: 70.0,
					title: 'Second Testing',
					value: 278256,
				},
			])
		);

		const {getByText} = render(
			<TrafficSources
				dataProvider={mockTrafficSourcesDataProvider}
				languageTag="en-US"
				onTrafficSourceClick={noop}
			/>
		);

		await waitFor(() => {
			expect(mockTrafficSourcesDataProvider).toHaveBeenCalledTimes(1);
		});

		const button1 = getByText('Testing');

		expect(button1).toBeInTheDocument();
		expect(button1).not.toBeDisabled();
		expect(button1).toHaveAttribute('type', 'button');
		expect(getByText(formatter.format(32178))).toBeInTheDocument();

		const button2 = getByText('Second Testing');

		expect(button2).toBeInTheDocument();
		expect(button2).not.toBeDisabled();
		expect(button2).toHaveAttribute('type', 'button');
		expect(getByText(formatter.format(278256))).toBeInTheDocument();
	});

	it('displays the traffic sources without buttons to view keywords when the value is 0', async () => {
		const mockTrafficSourcesDataProvider = jest.fn(() =>
			Promise.resolve([
				{
					helpMessage: 'Testing Help Message',
					name: 'testing',
					share: 0.0,
					title: 'Testing',
					value: 0,
				},
				{
					helpMessage: 'Second Testing Help Message',
					name: 'second-testing',
					share: 0.0,
					title: 'Second Testing',
					value: 0,
				},
			])
		);

		const {getAllByText, getByText} = render(
			<TrafficSources
				dataProvider={mockTrafficSourcesDataProvider}
				languageTag="en-US"
				onTrafficSourceClick={noop}
			/>
		);

		await waitFor(() =>
			expect(mockTrafficSourcesDataProvider).toHaveBeenCalledTimes(1)
		);

		const text1 = getByText('Testing');

		expect(text1).toBeInTheDocument();
		expect(text1).not.toHaveAttribute('type', 'button');

		const text2 = getByText('Second Testing');

		expect(text2).toBeInTheDocument();
		expect(text2).not.toHaveAttribute('type', 'button');

		const zeroValues = getAllByText('0');

		expect(zeroValues.length).toBe(2);
	});

	it('displays a dash instead of value when there is an endpoint error', async () => {
		const mockTrafficSourcesDataProvider = jest.fn(() =>
			Promise.resolve([
				{
					helpMessage: 'Testing Help Message',
					name: 'testing',
					title: 'Testing',
				},
				{
					helpMessage: 'Second Testing Help Message',
					name: 'second-testing',
					title: 'Second Testing',
				},
			])
		);

		const {getAllByText, getByText} = render(
			<TrafficSources
				dataProvider={mockTrafficSourcesDataProvider}
				languageTag="en-US"
				onTrafficSourceClick={noop}
			/>
		);

		await waitFor(() =>
			expect(mockTrafficSourcesDataProvider).toHaveBeenCalledTimes(1)
		);

		const text1 = getByText('Testing');
		const text2 = getByText('Second Testing');

		const dashValues = getAllByText('-');
		expect(dashValues.length).toBe(2);

		expect(text1).not.toHaveAttribute('type', 'button');
		expect(text2).not.toHaveAttribute('type', 'button');
	});

	it('displays a message informing the user that there is no incoming traffic from search engines yet', async () => {
		const mockTrafficSourcesDataProvider = jest.fn(() =>
			Promise.resolve([
				{
					helpMessage: 'Testing Help Message',
					name: 'testing',
					share: 0.0,
					title: 'Testing',
					value: 0,
				},
				{
					helpMessage: 'Second Testing Help Message',
					name: 'second-testing',
					share: 0.0,
					title: 'Second Testing',
					value: 0,
				},
			])
		);

		const {getAllByText, getByText} = render(
			<TrafficSources
				dataProvider={mockTrafficSourcesDataProvider}
				languageTag="en-US"
				onTrafficSourceClick={noop}
			/>
		);

		await waitFor(() =>
			expect(mockTrafficSourcesDataProvider).toHaveBeenCalledTimes(1)
		);

		expect(getByText('Testing')).toBeInTheDocument();
		expect(getByText('Second Testing')).toBeInTheDocument();

		const zeroValues = getAllByText('0');
		expect(zeroValues.length).toBe(2);

		expect(
			getByText(
				'your-page-has-no-incoming-traffic-from-traffic-channels-yet'
			)
		).toBeInTheDocument();
	});

	it('calls onTrafficSourceClick function when a traffic source button is clicked', async () => {
		const mockTrafficSourcesDataProvider = jest.fn(() =>
			Promise.resolve([
				{
					endpointURL: 'http://localhost:8080/',
					helpMessage: 'Testing Help Message',
					name: 'testing',
					share: 30.0,
					title: 'Testing',
					value: 32178,
				},
				{
					endpointURL: 'http://localhost:8080/',
					helpMessage: 'Second Testing Help Message',
					name: 'second-testing',
					share: 70.0,
					title: 'Second Testing',
					value: 278256,
				},
			])
		);

		const myFunc = jest.fn();
		const {getByText} = render(
			<TrafficSources
				dataProvider={mockTrafficSourcesDataProvider}
				languageTag="en-US"
				onTrafficSourceClick={myFunc}
			/>
		);

		await waitFor(() => {
			expect(mockTrafficSourcesDataProvider).toHaveBeenCalledTimes(1);
		});

		const button1 = getByText('Testing');
		fireEvent.click(button1);
		expect(myFunc).toHaveBeenCalledTimes(1);
	});
});
