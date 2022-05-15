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

import {cleanup, render} from '@testing-library/react';
import React from 'react';

import {InstanceListContext} from '../../../src/main/resources/META-INF/resources/js/components/instance-list-page/InstanceListPageProvider.es';
import {Table} from '../../../src/main/resources/META-INF/resources/js/components/instance-list-page/InstanceListPageTable.es';
import {ModalContext} from '../../../src/main/resources/META-INF/resources/js/components/instance-list-page/modal/ModalProvider.es';
import {MockRouter} from '../../mock/MockRouter.es';
import FetchMock, {fetchMockResponse} from '../../mock/fetch.es';

const instances = [
	{
		assetTitle: 'New Post 1',
		assetType: 'Blog',
		dateCreated: new Date('2019-01-01'),
		id: 1,
		taskNames: [],
	},
	{
		assetTitle: 'New Post 2',
		assetType: 'Blog',
		creator: {
			name: 'User 1',
		},
		dateCreated: new Date('2019-01-03'),
		id: 1,
		taskNames: ['Update'],
	},
];

const fetchMock = new FetchMock({
	GET: {
		default: fetchMockResponse({}),
	},
});

describe('The instance list table should', () => {
	let container;
	let getAllByRole;

	afterEach(() => {
		fetchMock.reset();

		cleanup();
	});

	beforeEach(() => {
		fetchMock.mock();

		const renderResult = render(
			<MockRouter>
				<InstanceListContext.Provider
					value={{setInstanceId: jest.fn()}}
				>
					<ModalContext.Provider
						value={{closeModal: jest.fn(), openModal: jest.fn()}}
					>
						<Table items={instances} />
					</ModalContext.Provider>
				</InstanceListContext.Provider>
			</MockRouter>
		);

		container = renderResult.container;
		getAllByRole = renderResult.getAllByRole;
	});

	test('Be rendered with two items', () => {
		const instanceRows = getAllByRole('row');

		expect(instanceRows.length).toBe(3);
	});

	test('Should order by Assignee', () => {
		const orderLinks = container.querySelectorAll('.inline-item');

		expect(orderLinks[2].href).toContain(
			'/1/20/assigneeName%3Adesc?backPath=%2F'
		);
	});

	test('Should order by Item Subject', () => {
		const orderLinks = container.querySelectorAll('.inline-item');

		expect(orderLinks[1].href).toContain(
			'/1/20/assetType%3Adesc?backPath=%2F'
		);
	});

	test('Should order by User Creator', () => {
		const orderLinks = container.querySelectorAll('.inline-item');

		expect(orderLinks[3].href).toContain(
			'/1/20/userName%3Adesc?backPath=%2F'
		);
	});
});
