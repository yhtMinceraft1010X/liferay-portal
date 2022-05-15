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

import getCurrentEndDate from './getCurrentEndDate';

export default function getActivationStatusDateRange(accountSubscriptionTerms) {
	const dates = accountSubscriptionTerms.reduce(
		(dateAccumulator, accountSubscriptionTerm) => {
			return {
				endDates: [
					...dateAccumulator.endDates,
					accountSubscriptionTerm.endDate,
				],
				startDates: [
					...dateAccumulator.startDates,
					accountSubscriptionTerm.startDate,
				],
			};
		},
		{endDates: [], startDates: []}
	);
	const earliestStartDate = new Date(
		Math.min(...dates.startDates.map((date) => new Date(date)))
	);
	const farthestEndDate = new Date(
		Math.max(...dates.endDates.map((date) => new Date(date)))
	);
	const activationStatusDateRange = `${getCurrentEndDate(
		earliestStartDate
	)} - ${getCurrentEndDate(farthestEndDate)}`;

	return activationStatusDateRange;
}
