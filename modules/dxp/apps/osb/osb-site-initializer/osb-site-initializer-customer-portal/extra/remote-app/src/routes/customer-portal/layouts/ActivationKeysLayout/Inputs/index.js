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

import {ClaySelect} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import {useEffect, useMemo, useState} from 'react';
import client from '../../../../../apolloClient';
import i18n from '../../../../../common/I18n';
import {Button} from '../../../../../common/components';
import {useApplicationProvider} from '../../../../../common/context/AppPropertiesProvider';
import {
	getAccountSubscriptions,
	getAccountSubscriptionsTerms,
} from '../../../../../common/services/liferay/graphql/queries';
import {getCommonLicenseKey} from '../../../../../common/services/liferay/rest/raysource/LicenseKeys';
import {ROLE_TYPES} from '../../../../../common/utils/constants';
import downloadFromBlob from '../../../../../common/utils/downloadFromBlob';
import getCurrentEndDate from '../../../../../common/utils/getCurrentEndDate';
import {useCustomerPortal} from '../../../context';
import {EXTENSION_FILE_TYPES, STATUS_CODE} from '../../../utils/constants';
import getKebabCase from '../../../utils/getKebabCase';
import {getYearlyTerms} from '../../../utils/getYearlyTerms';

const ActivationKeysInputs = ({
	accountKey,
	productKey,
	productTitle,
	sessionId,
}) => {
	const [{project, userAccount}] = useCustomerPortal();

	const {
		createSupportRequest,
		licenseKeyDownloadURL,
	} = useApplicationProvider();

	const [accountSubscriptions, setAccountSubscriptions] = useState([]);

	const [
		selectedAccountSubscriptionName,
		setSelectedAccountSubscriptionName,
	] = useState('');

	const [
		accountSubscriptionsTermsDates,
		setAccountSubscriptionsTermsDates,
	] = useState([]);
	const [selectDateInterval, setSelectedDateInterval] = useState();

	const [hasLicenseDownloadError, setLicenseDownloadError] = useState(false);

	useEffect(() => {
		const fetchAccountSubscriptions = async () => {
			const {data} = await client.query({
				query: getAccountSubscriptions,
				variables: {
					filter: `accountSubscriptionGroupERC eq '${accountKey}_${productKey}'`,
				},
			});

			if (data) {
				const items = data.c?.accountSubscriptions?.items;
				setAccountSubscriptions(data.c?.accountSubscriptions?.items);

				setSelectedAccountSubscriptionName(items[0].name);
			}
		};

		fetchAccountSubscriptions();
	}, [accountKey, productKey]);

	useEffect(() => {
		const getSubscriptionTerms = async () => {
			const filterAccountSubscriptionERC = `accountSubscriptionERC eq '${accountKey}_${productKey}_${selectedAccountSubscriptionName.toLowerCase()}'`;

			const {data} = await client.query({
				query: getAccountSubscriptionsTerms,
				variables: {
					filter: filterAccountSubscriptionERC,
				},
			});

			if (data) {
				const accountSubscriptionsTerms =
					data.c?.accountSubscriptionTerms?.items || [];

				if (accountSubscriptionsTerms.length) {
					const dateIntervals = getYearlyTerms(
						accountSubscriptionsTerms[0]
					);

					setAccountSubscriptionsTermsDates(dateIntervals);
					setSelectedDateInterval(dateIntervals[0]);
				}
			}
		};

		if (selectedAccountSubscriptionName) {
			getSubscriptionTerms();
		}
	}, [accountKey, productKey, selectedAccountSubscriptionName]);

	useEffect(() => {
		if (selectedAccountSubscriptionName && selectDateInterval) {
			setLicenseDownloadError(false);
		}
	}, [selectDateInterval, selectedAccountSubscriptionName]);

	const handleClick = async () => {
		const license = await getCommonLicenseKey(
			accountKey,
			selectDateInterval.endDate.toISOString(),
			selectDateInterval.startDate.toISOString(),
			selectedAccountSubscriptionName.toLowerCase(),
			licenseKeyDownloadURL,
			encodeURI(productTitle),
			sessionId
		);

		if (license.status === STATUS_CODE.success) {
			const contentType = license.headers.get('content-type');
			const extensionFile = EXTENSION_FILE_TYPES[contentType] || '.txt';
			const licenseBlob = await license.blob();

			downloadFromBlob(licenseBlob, `license${extensionFile}`);

			return;
		}

		setLicenseDownloadError(true);
	};

	const accountBrief = userAccount.accountBriefs?.find(
		(accountBrief) =>
			accountBrief.externalReferenceCode === project?.accountKey
	);

	const errorDownloadMessage = useMemo(
		() => ({
			messageRequestersAdministrators: (
				<p className="mt-3 text-neutral-7 text-paragraph">
					{i18n.sub(
						'the-requested-activation-key-is-not-yet-available-for-more-information-about-the-availability-of-your-x-activation-keys-please',
						[getKebabCase(productTitle)]
					)}

					<a
						href={createSupportRequest}
						rel="noreferrer"
						target="_blank"
					>
						<u className="font-weight-bold text-neutral-9">
							{i18n.translate('contact-the-support-team')}
						</u>
					</a>
				</p>
			),
			messageUsers: (
				<p className="mt-3 text-neutral-7 text-paragraph">
					{i18n.sub(
						'the-requested-activation-key-is-not-yet-available-if-you-need-more-information-about-the-availability-of-your-x-activation-keys-please-ask-one-of-your-administrator-team-members-to-update-your-permissions-so-you-can-contact-liferay-support-alternatively-team-members-with-administrator-or-requester-role-can-submit-a-support-ticket-on-your-behalf',
						[getKebabCase(productTitle)]
					)}
				</p>
			),
		}),
		[createSupportRequest, productTitle]
	);

	const currentEnterpriseMessage = useMemo(() => {
		const isRequester = accountBrief?.roleBriefs?.some(
			({name}) => name === ROLE_TYPES.requester.key
		);
		if (userAccount.isAdmin || isRequester) {
			return errorDownloadMessage.messageRequestersAdministrators;
		}

		return errorDownloadMessage.messageUsers;
	}, [accountBrief, errorDownloadMessage, userAccount]);

	return (
		<div className="mt-3">
			<p className="text-paragraph">
				{i18n.sub(
					'select-an-active-liferay-x-subscription-to-download-the-activation-key',
					[getKebabCase(productTitle)]
				)}
				.
			</p>

			<div className="d-flex mb-3">
				<label className="cp-subscription-select mr-3">
					{i18n.sub('subscription')}

					<div className="position-relative">
						<ClayIcon
							className="select-icon"
							symbol="caret-bottom"
						/>

						<ClaySelect
							onChange={(event) =>
								setSelectedAccountSubscriptionName(
									event.target.value
								)
							}
							value={selectedAccountSubscriptionName}
						>
							{accountSubscriptions.map((accountSubscription) => (
								<ClaySelect.Option
									key={
										accountSubscription.accountSubscriptionId
									}
									label={i18n.translate(
										getKebabCase(accountSubscription.name)
									)}
									value={accountSubscription.name}
								/>
							))}
						</ClaySelect>
					</div>
				</label>

				<label className="cp-subscription-term-select">
					{i18n.translate('subscription-term')}

					<div className="position-relative">
						<ClayIcon
							className="select-icon"
							symbol="caret-bottom"
						/>

						<ClaySelect
							onChange={(event) => {
								setSelectedDateInterval(
									accountSubscriptionsTermsDates[
										event.target.value
									]
								);
							}}
						>
							{accountSubscriptionsTermsDates.map(
								(dateInterval, index) => {
									const formattedDate = `${getCurrentEndDate(
										dateInterval.startDate
									)} - ${getCurrentEndDate(
										dateInterval.endDate
									)}`;

									return (
										<ClaySelect.Option
											className="options"
											key={index}
											label={formattedDate}
											value={index}
										/>
									);
								}
							)}
						</ClaySelect>
					</div>
				</label>
			</div>

			<Button
				className="btn btn-outline-primary"
				disabled={
					hasLicenseDownloadError ||
					!(selectedAccountSubscriptionName && selectDateInterval)
				}
				onClick={handleClick}
				prependIcon="download"
				type="button"
			>
				{i18n.translate('download-key')}
			</Button>

			{hasLicenseDownloadError && currentEnterpriseMessage}
		</div>
	);
};

export default ActivationKeysInputs;
