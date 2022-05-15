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

import {useCallback} from 'react';
import {useNavigate} from 'react-router-dom';
import i18n from '../../../../../../common/I18n';
import {Button, ButtonDropDown} from '../../../../../../common/components';
import {useApplicationProvider} from '../../../../../../common/context/AppPropertiesProvider';
import {ALERT_DOWNLOAD_TYPE} from '../../../../utils/constants';
import {getActivationKeyDownload} from '../../utils/getActivationKeyDownload';
import {getActivationKeysActionsItems} from '../../utils/getActivationKeysActionsItems';
import {getActivationKeysDownloadItems} from '../../utils/getActivationKeysDownloadItems';

const ActionButton = ({
	activationKeysByStatusPaginatedChecked,
	filterCheckedActivationKeys,
	isAbleToDownloadAggregateKeys,
	productName,
	project,
	sessionId,
	setStatus,
}) => {
	const {licenseKeyDownloadURL} = useApplicationProvider();
	const navigate = useNavigate();

	const handleAlertStatus = useCallback(
		(hasSuccessfullyDownloadedKeys) =>
			setStatus((previousStatus) => ({
				...previousStatus,
				downloadAggregated: hasSuccessfullyDownloadedKeys
					? ALERT_DOWNLOAD_TYPE.success
					: ALERT_DOWNLOAD_TYPE.danger,
			})),
		[setStatus]
	);

	const handleMultipleAlertStatus = useCallback(
		(hasSuccessfullyDownloadedKeys) =>
			setStatus((previousStatus) => ({
				...previousStatus,
				downloadMultiple: hasSuccessfullyDownloadedKeys
					? ALERT_DOWNLOAD_TYPE.success
					: ALERT_DOWNLOAD_TYPE.danger,
			})),
		[setStatus]
	);

	if (activationKeysByStatusPaginatedChecked.length > 1) {
		const activationKeysDownloadItems = getActivationKeysDownloadItems(
			isAbleToDownloadAggregateKeys,
			filterCheckedActivationKeys,
			licenseKeyDownloadURL,
			sessionId,
			handleMultipleAlertStatus,
			handleAlertStatus,
			activationKeysByStatusPaginatedChecked,
			project.name
		);

		return (
			<ButtonDropDown
				items={activationKeysDownloadItems}
				label={i18n.translate('download')}
				menuElementAttrs={{
					className: 'p-0 cp-drop-down-action-button',
				}}
			/>
		);
	}

	if (activationKeysByStatusPaginatedChecked.length === 1) {
		return (
			<Button
				className="btn btn-primary"
				onClick={async () =>
					getActivationKeyDownload(
						licenseKeyDownloadURL,
						sessionId,
						handleAlertStatus,
						activationKeysByStatusPaginatedChecked[0],
						project.name
					)
				}
			>
				{i18n.translate('download')}
			</Button>
		);
	}

	const handleRedirectPage = () => navigate('new');
	const handleDeactivatePage = () => navigate('deactivate');

	const activationKeysActionsItems = getActivationKeysActionsItems(
		project?.accountKey,
		licenseKeyDownloadURL,
		sessionId,
		handleAlertStatus,
		handleRedirectPage,
		handleDeactivatePage,
		productName
	);

	return (
		<ButtonDropDown
			items={activationKeysActionsItems}
			label={i18n.translate('actions')}
			menuElementAttrs={{
				className: 'p-0',
			}}
		/>
	);
};

export default ActionButton;
