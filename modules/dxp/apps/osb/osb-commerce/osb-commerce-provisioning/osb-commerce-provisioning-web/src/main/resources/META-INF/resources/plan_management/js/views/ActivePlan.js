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

import ClayPanel from '@clayui/panel';
import PropTypes from 'prop-types';
import React from 'react';

function ActivePlan({
	cancelPlanURL,
	endDate,
	planName,
	planPrice,
	recurrence,
	spritemap,
	startDate,
	switchBillingURL,
}) {
	return (
		<div className={'col-12 osb-commerce-active-plan py-5'}>
			<ClayPanel
				displayType={'secondary'}
				showCollapseIcon={false}
				spritemap={spritemap}
			>
				<ClayPanel.Body>
					<div className={'py-4 row'}>
						<div
							className={
								'active-plan align-items-center col-5 d-flex flex-column justify-content-center'
							}
						>
							<p className={'name'}>{planName}</p>
							<p className={'price'}>
								<span className={'value'}>{planPrice}</span>/
								<span className={'recurrence'}>
									{recurrence}
								</span>
							</p>
						</div>
						<div className={'active-plan-details col-7'}>
							<div className={'switch-plan'}>
								<p className={'switch-plan'}>
									<small>
										{Liferay.Language.get('switch-to')}
										<a href={switchBillingURL}>
											{` ${Liferay.Language.get(
												'monthly'
											)}`}
										</a>
									</small>
								</p>
							</div>
							<div className={'plan-duration'}>
								<p>
									<b>{Liferay.Language.get('start-date')}:</b>
									{` ${startDate}`}
								</p>
								<p>
									<b>{Liferay.Language.get('end-date')}:</b>
									{` ${endDate}`}
								</p>
							</div>
							<div className={'plan-cancellation'}>
								<p>
									<small>
										<a href={cancelPlanURL}>
											{Liferay.Language.get(
												'cancel-your-subscription'
											)}
										</a>
									</small>
								</p>
							</div>
						</div>
					</div>
				</ClayPanel.Body>
			</ClayPanel>
		</div>
	);
}

ActivePlan.propTypes = {
	cancelPlanLink: PropTypes.string,
	endDate: PropTypes.string,
	planName: PropTypes.string,
	planPrice: PropTypes.string,
	spritemap: PropTypes.string,
	startDate: PropTypes.string,
	switchBillingLink: PropTypes.string,
};

export default ActivePlan;
