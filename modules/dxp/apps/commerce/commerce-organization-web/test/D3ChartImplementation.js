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

import * as d3 from 'd3';

import D3OrganizationChart from '../src/main/resources/META-INF/resources/js/D3OrganizationChart';
import {
	ACCOUNTS_PROPERTY_NAME,
	ORGANIZATIONS_PROPERTY_NAME,
	USERS_PROPERTY_NAME_IN_ORGANIZATION,
} from '../src/main/resources/META-INF/resources/js/utils/constants';

jest.mock(
	'../src/main/resources/META-INF/resources/js/data/organizations',
	() => ({
		getOrganization: (id) => {
			return Promise.resolve({
				childOrganizations: [
					{
						id: '9099',
						name: 'Organization Child 1',
					},
					{
						id: '9100',
						name: `Organization Child 2`,
					},
					{
						id: '9101',
						name: `Organization Child 3`,
					},
				],
				id,
				organizationAccounts: [
					{
						id: '1000',
						name: `Account Child 1`,
					},
				],
				type: 'organization',
				userAccounts: [
					{
						id: '2000',
						name: `User Child 1`,
					},
					{
						id: '2001',
						name: `User Child 2`,
					},
					{
						id: '2002',
						name: `User Child 3`,
					},
				],
			});
		},
	})
);

jest.mock('../src/main/resources/META-INF/resources/js/data/accounts', () => ({
	getAccount: () =>
		Promise.resolve({
			accountUsers: [
				{
					id: '2000',
					name: `User Child 1`,
				},
				{
					id: '2001',
					name: `User Child 2`,
				},
				{
					id: '2002',
					name: `User Child 3`,
				},
			],
			type: 'account',
		}),
}));

const countNodes = (root) => {
	const structure = d3.hierarchy(root, (d) => d.children);

	return structure.descendants().length;
};

const countLinks = (root) => {
	const structure = d3.hierarchy(root, (d) => d.children);

	return structure.links().length;
};

const INITIAL_DATA = {
	[ACCOUNTS_PROPERTY_NAME]: [
		{
			id: '1',
			name: `Auto Care`,
		},
		{
			id: '0000',
			name: `CC West`,
		},
		{
			id: '3',
			name: `Leo Auto`,
		},
		{
			id: '5',
			name: `Repair World`,
		},
	],
	[ORGANIZATIONS_PROPERTY_NAME]: [
		{
			id: '999',
			name: `Canada`,
		},
		{
			id: '2',
			name: `Italy`,
		},
		{
			id: '40',
			name: `Spain`,
		},
	],
	[USERS_PROPERTY_NAME_IN_ORGANIZATION]: [
		{
			id: '1',
			name: `Mark`,
		},
		{
			id: '2',
			name: `John`,
		},
	],
	id: '0',
	name: `Root`,
};

const getNodes = (wrapper, value, propertyName) => {
	const nodes = Array.from(wrapper.querySelectorAll('.chart-item'));

	return nodes.filter((node) => node.__data__.data[propertyName] === value);
};

const wait = (time = 0) =>
	new Promise((resolve) =>
		setTimeout(() => {
			resolve();
		}, time)
	);

const d3click = async (node) => {
	node.dispatchEvent(
		new MouseEvent('mousedown', {
			view: global.window,
		})
	);

	global.window.dispatchEvent(
		new MouseEvent('mouseup', {
			view: global.window,
		})
	);

	await wait();
};

describe('D3OrganizationChart implementation', () => {
	let chartSVGWrapper;
	let zoomInButton;
	let zoomOutButton;
	let defaultParams;
	const getChartNodes = (...args) => getNodes(chartSVGWrapper, ...args);

	beforeEach(() => {
		chartSVGWrapper = document.createElement('svg');
		zoomInButton = document.createElement('button');
		zoomOutButton = document.createElement('button');
		defaultParams = [
			{
				svg: chartSVGWrapper,
				zoomIn: zoomInButton,
				zoomOut: zoomOutButton,
			},
			'./assets/clay/icons.svg',
			{
				open: () => {},
			},
			{
				close: () => {},
				open: () => {},
			},
		];
	});

	it('Must create a chart', async () => {
		new D3OrganizationChart(INITIAL_DATA, ...defaultParams);

		const chartItemsWrapper = chartSVGWrapper.querySelector(
			'.chart-data-wrapper'
		);
		expect(chartItemsWrapper).toBeDefined();
	});

	it('Must display the right number of nodes', () => {
		new D3OrganizationChart(INITIAL_DATA, ...defaultParams);

		const chartOrganizations = chartSVGWrapper.querySelectorAll(
			'.chart-item-organization'
		);

		expect(chartOrganizations.length).toBe(
			INITIAL_DATA[ORGANIZATIONS_PROPERTY_NAME].length + 1
		);

		const chartAccounts = chartSVGWrapper.querySelectorAll(
			'.chart-item-account'
		);

		expect(chartAccounts.length).toBe(
			INITIAL_DATA[ACCOUNTS_PROPERTY_NAME].length
		);

		const chartUsers = chartSVGWrapper.querySelectorAll('.chart-item-user');

		expect(chartUsers.length).toBe(
			INITIAL_DATA[USERS_PROPERTY_NAME_IN_ORGANIZATION].length
		);

		const chartItems = chartSVGWrapper.querySelectorAll('.chart-item');

		expect(chartItems.length).toBe(countNodes(INITIAL_DATA));
	});

	it('Must display the right number of links', () => {
		new D3OrganizationChart(INITIAL_DATA, ...defaultParams);

		const chartLinks = chartSVGWrapper.querySelectorAll('.chart-link');

		expect(chartLinks.length).toBe(countLinks(INITIAL_DATA));
	});

	describe('Node selection', () => {
		it('Must select the clicked entity', async () => {
			new D3OrganizationChart(INITIAL_DATA, ...defaultParams);

			const rootHTMLNode = getChartNodes('Root', 'name')[0];

			expect(rootHTMLNode.classList.contains('selected')).toBe(false);

			await d3click(rootHTMLNode);

			expect(rootHTMLNode.classList.contains('selected')).toBe(true);
		});

		it('Must display the selected node children', async () => {
			new D3OrganizationChart(INITIAL_DATA, ...defaultParams);

			const parentNode = getChartNodes('Canada', 'name')[0];

			await d3click(parentNode);

			expect(parentNode.classList.contains('selected')).toBe(true);

			[
				'Organization Child 1',
				'Organization Child 2',
				'Organization Child 3',
				'Account Child 1',
				'User Child 1',
				'User Child 2',
				'User Child 3',
			].forEach((name) => {
				const node = getChartNodes(name, 'name')[0];

				expect(node).toBeTruthy();
				expect(node.__data__.parent.data.name).toBe('Canada');
			});
		});
	});
});
