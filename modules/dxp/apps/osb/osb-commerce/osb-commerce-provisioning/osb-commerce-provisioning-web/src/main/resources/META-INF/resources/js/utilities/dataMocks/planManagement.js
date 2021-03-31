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

const planManagementMock = {
	activePlan: {
		cancelPlanURL: 'http://someUrl.com',
		currency: '$',
		endDate: new Date().toDateString(),
		planName: 'Liferay Commerce Basic',
		planPrice: '20.000',
		recurrence: 'year',
		startDate: new Date().toDateString(),
		switchBillingURL: 'http://someUrl.com',
	},

	planFeatures: {
		activeFeatures: [
			{
				description:
					'This is an amazing description of the above Cool Feature',
				id: `feature-${Math.floor(Math.random() * Math.floor(1000))}`,
				name: 'Some Cool Feature',
				toggleURL: 'http://someToggleURL.com',
			},
			{
				description:
					'This is an amazing description of the above Cool Feature',
				id: `feature-${Math.floor(Math.random() * Math.floor(1000))}`,
				name: 'Some Cool Feature',
				toggleURL: 'http://someToggleURL.com',
			},
			{
				description:
					'This is an amazing description of the above Cool Feature',
				id: `feature-${Math.floor(Math.random() * Math.floor(1000))}`,
				name: 'Some Cool Feature',
				toggleURL: 'http://someToggleURL.com',
			},
			{
				description:
					'This is an amazing description of the above Cool Feature',
				id: `feature-${Math.floor(Math.random() * Math.floor(1000))}`,
				name: 'Some Cool Feature',
				toggleURL: 'http://someToggleURL.com',
			},
			{
				description:
					'This is an amazing description of the above Cool Feature',
				id: `feature-${Math.floor(Math.random() * Math.floor(1000))}`,
				name: 'Some Cool Feature',
				toggleURL: 'http://someToggleURL.com',
			},
		],
		inactiveFeatures: [
			{
				description:
					'This is an amazing description of the above Cool Feature',
				id: `feature-${Math.floor(Math.random() * Math.floor(1000))}`,
				name: 'Some Cool Feature',
				toggleURL: 'http://someToggleURL.com',
			},
			{
				description:
					'This is an amazing description of the above Cool Feature',
				id: `feature-${Math.floor(Math.random() * Math.floor(1000))}`,
				name: 'Some Cool Feature',
				toggleURL: 'http://someToggleURL.com',
			},
			{
				description:
					'This is an amazing description of the above Cool Feature',
				id: `feature-${Math.floor(Math.random() * Math.floor(1000))}`,
				name: 'Some Cool Feature',
				toggleURL: 'http://someToggleURL.com',
			},
			{
				description:
					'This is an amazing description of the above Cool Feature',
				id: `feature-${Math.floor(Math.random() * Math.floor(1000))}`,
				name: 'Some Cool Feature',
				toggleURL: 'http://someToggleURL.com',
			},
		],
	},

	spritemap: '',
};

export default planManagementMock;
