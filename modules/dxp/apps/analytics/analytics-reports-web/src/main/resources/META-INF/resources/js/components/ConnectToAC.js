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

import ClayIcon from '@clayui/icon';
import ClayLink from '@clayui/link';
import PropTypes from 'prop-types';
import React from 'react';

export default function ConnectToAC({
	analyticsCloudTrialURL,
	analyticsURL,
	isAnalyticsConnected,
	hideAnalyticsReportsPanelURL,
	pathToAssets,

	onHideAnalyticsReportsPanelClick = (event) => {
		event.preventDefault();

		if (document.hrefFm && typeof submitForm !== 'undefined') {
			submitForm(document.hrefFm, hideAnalyticsReportsPanelURL);
		}
	},
}) {
	return (
		<div className="p-3 pt-4 text-center">
			<img
				alt={Liferay.Language.get('connect-to-liferay-analytics-cloud')}
				src={`${pathToAssets}/assets/ac-icon.svg`}
			></img>

			{isAnalyticsConnected ? (
				<>
					<h4 className="font-weight-semi-bold h5 mt-3">
						{Liferay.Language.get('sync-to-analytics-cloud')}
					</h4>

					<p className="text-secondary">
						{Liferay.Language.get(
							'sync-your-liferay-dxp-instance-with-analytics-cloud-to-view-content-performance-metrics-and-build-a-successful-content-strategy'
						)}
					</p>

					<ClayLink
						button={true}
						displayType="primary"
						href={analyticsURL}
						target="_blank"
					>
						{Liferay.Language.get('open-analytics-cloud')}

						<span className="inline-item inline-item-after">
							<ClayIcon symbol="shortcut" />
						</span>
					</ClayLink>
				</>
			) : (
				<>
					<h4 className="font-weight-semi-bold h5 mt-3">
						{Liferay.Language.get(
							'connect-to-liferay-analytics-cloud'
						)}
					</h4>

					<p className="text-secondary">
						{Liferay.Language.get(
							'liferay-dxp-instance-has-to-be-connected-with-analytics-cloud-to-view-content-performance-metrics-and-build-a-successful-content-strategy'
						)}
					</p>

					<ClayLink
						button={true}
						displayType="secondary"
						href={analyticsCloudTrialURL}
						target="_blank"
					>
						{Liferay.Language.get('start-free-trial')}

						<span className="inline-item inline-item-after">
							<ClayIcon symbol="shortcut" />
						</span>
					</ClayLink>

					<ClayLink
						className="d-block font-weight-bold mb-2 mt-5"
						onClick={onHideAnalyticsReportsPanelClick}
						tabIndex="0"
					>
						{Liferay.Language.get('do-not-show-me-this-again')}
					</ClayLink>

					<p className="text-secondary">
						{Liferay.Language.get('do-not-show-me-this-again-help')}
					</p>
				</>
			)}
		</div>
	);
}

ConnectToAC.propTypes = {
	analyticsCloudTrialURL: PropTypes.string.isRequired,
	analyticsURL: PropTypes.string,
	hideAnalyticsReportsPanelURL: PropTypes.string.isRequired,
	isAnalyticsConnected: PropTypes.bool.isRequired,
	pathToAssets: PropTypes.string.isRequired,
};
