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

import React from 'react';

import {getDataMock} from '../../js/utilities/index';
import ActivePlan from './views/ActivePlan';

function PlanManagement({mockData = false, ...props}) {
	const {activePlan, spritemap} = mockData
		? getDataMock(PlanManagement.name)
		: props;

	return (
		<>
			<ActivePlan {...activePlan} spritemap={spritemap} />
		</>
	);
}

export default PlanManagement;
