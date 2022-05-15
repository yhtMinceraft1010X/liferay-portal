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
import ClayAlert from '@clayui/alert';
import {ButtonWithIcon} from '@clayui/core';
import {Align} from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import {useModal} from '@clayui/modal';
import React, {useEffect, useState} from 'react';
import client from '../../../../../apolloClient';
import {Button, ButtonDropDown} from '../../../../../common/components';
import {
	getAccountSubscriptionGroups,
	getAccountSubscriptionsTerms,
	getAnalyticsCloudWorkspace,
	updateAccountSubscriptionGroups,
	updateAnalyticsCloudWorkspace,
} from '../../../../../common/services/liferay/graphql/queries';
import getActivationStatusDateRange from '../../../../../common/utils/getActivationStatusDateRange';
import AnalyticsCloudModal from '../../../components/AnalyticsCloudModal';
import {ALERT_UPDATE_ANALYTICS_CLOUD_STATUS} from '../../../containers/ActivationKeysTable/utils/constants/alertUpdateAnalyticsCloudStatus';
import {useCustomerPortal} from '../../../context';
import {actionTypes} from '../../../context/reducer';
import {
	AUTO_CLOSE_ALERT_TIME,
	STATUS_TAG_TYPES,
	STATUS_TAG_TYPE_NAMES,
} from '../../../utils/constants';
import ActivationStatusLayout from '../Layout';
import AnalyticsCloudStatusModal from './AnalyticsCloudStatusModal';

const ActivationStatusAnalyticsCloud = ({
	analyticsCloudWorkspace,
	project,
	subscriptionGroupAnalyticsCloud,
	userAccount,
}) => {
	const [{assetsPath}, dispatch] = useCustomerPortal();
	const [groupIdValue, setGroupIdValue] = useState('');
	const [activationStatusDate, setActivationStatusDate] = useState('');
	const [isVisible, setIsVisible] = useState(false);
	const [visible, setVisible] = useState(false);
	const {observer: observerSetupModal, onClose} = useModal({
		onClose: () => setIsVisible(false),
	});
	const {
		observer: observerStatusModal,
		onClose: onCloseStatusModal,
	} = useModal({
		onClose: () => setVisible(false),
	});

	const [
		subscriptionGroupActivationStatus,
		setSubscriptionGroupActivationStatus,
	] = useState(subscriptionGroupAnalyticsCloud?.activationStatus);

	const onCloseSetupModal = async (isSuccess) => {
		onClose();

		if (isSuccess) {
			const getSubscriptionGroups = async (accountKey) => {
				const {data: dataSubscriptionGroups} = await client.query({
					query: getAccountSubscriptionGroups,
					variables: {
						filter: `accountKey eq '${accountKey}' and hasActivation eq true`,
					},
				});

				if (dataSubscriptionGroups) {
					const items =
						dataSubscriptionGroups?.c?.accountSubscriptionGroups
							?.items;
					dispatch({
						payload: items,
						type: actionTypes.UPDATE_SUBSCRIPTION_GROUPS,
					});

					setSubscriptionGroupActivationStatus(
						STATUS_TAG_TYPE_NAMES.inProgress
					);
				}
			};

			getSubscriptionGroups(project.accountKey);
		}
	};

	const [hasFinishedUpdate, setHasFinishedUpdate] = useState(false);

	const currentActivationStatus = {
		[STATUS_TAG_TYPE_NAMES.active]: {
			buttonLink: (
				<a
					className="font-weight-semi-bold m-0 p-0 text-brand-primary text-paragraph"
					href={`https://analytics.liferay.com/workspace/${analyticsCloudWorkspace?.workspaceGroupId}/sites`}
					rel="noopener noreferrer"
					target="_blank"
				>
					Go to Workspace
					<ClayIcon className="ml-1" symbol="order-arrow-right" />
				</a>
			),
			id: STATUS_TAG_TYPES.active,
			subtitle:
				'Your Analytics Cloud environments are ready. Visit the workspace to view Analytics Cloud details.',
			title: 'Analytics Cloud Activation',
		},
		[STATUS_TAG_TYPE_NAMES.inProgress]: {
			dropdownIcon: userAccount.isStaff && userAccount.isProvisioning && (
				<ButtonDropDown
					align={Align.BottomRight}
					customDropDownButton={
						<ButtonWithIcon
							displayType="null"
							small
							symbol="caret-bottom"
						/>
					}
					items={[
						{
							label: 'Set to Active',
							onClick: () => setVisible(true),
						},
					]}
					menuElementAttrs={{
						className: 'p-0 cp-activation-key-icon rounded-xs',
					}}
				/>
			),
			id: STATUS_TAG_TYPES.inProgress,
			subtitle:
				'Your Analytics Cloud workspace is being set up and will be available soon',
			title: 'Analytics Cloud Activation',
		},
		[STATUS_TAG_TYPE_NAMES.notActivated]: {
			buttonLink: (userAccount.isAdmin || userAccount.isStaff) && (
				<Button
					appendIcon="order-arrow-right"
					className="btn btn-link font-weight-semi-bold p-0 text-brand-primary text-paragraph"
					displayType="link"
					onClick={() => setIsVisible(true)}
				>
					Finish Activation
				</Button>
			),
			id: STATUS_TAG_TYPES.notActivated,
			subtitle:
				'Almost there! Setup Analytics Cloud by finishing the activation form',
			title: 'Analytics Cloud Activation',
		},
	};

	const activationStatus =
		currentActivationStatus[
			subscriptionGroupActivationStatus ||
				STATUS_TAG_TYPE_NAMES.notActivated
		];

	useEffect(() => {
		const getSubscriptionTerms = async () => {
			const filterAccountSubscriptionERC = `accountSubscriptionGroupERC eq '${project.accountKey}_analytics-cloud'`;
			const {data} = await client.query({
				query: getAccountSubscriptionsTerms,
				variables: {
					filter: filterAccountSubscriptionERC,
				},
			});

			if (data) {
				const activationStatusDateRange = getActivationStatusDateRange(
					data.c?.accountSubscriptionTerms?.items
				);
				setActivationStatusDate(activationStatusDateRange);
			}
		};

		getSubscriptionTerms();
	}, [project]);

	const updateAnalyticsCloudWorkspaceId = async () => {
		const {data} = await client.query({
			query: getAnalyticsCloudWorkspace,
			variables: {
				filter: `accountKey eq '${project.accountKey}'`,
				scopeKey: Liferay.ThemeDisplay.getScopeGroupId(),
			},
		});

		const analyticsCloudWorkspace =
			data.c?.analyticsCloudWorkspaces?.items[0];

		if (analyticsCloudWorkspace) {
			await client.mutate({
				mutation: updateAnalyticsCloudWorkspace,
				variables: {
					analyticsCloudWorkspace: {
						workspaceGroupId: groupIdValue,
					},
					analyticsCloudWorkspaceId:
						analyticsCloudWorkspace.analyticsCloudWorkspaceId,
				},
			});
		}
	};

	const updateGroupId = async () => {
		await Promise.all([
			await client.mutate({
				mutation: updateAccountSubscriptionGroups,
				variables: {
					accountSubscriptionGroup: {
						activationStatus: STATUS_TAG_TYPE_NAMES.active,
					},
					id:
						subscriptionGroupAnalyticsCloud?.accountSubscriptionGroupId,
				},
			}),
			await updateAnalyticsCloudWorkspaceId(),
		]);

		setSubscriptionGroupActivationStatus(STATUS_TAG_TYPE_NAMES.active);
		setVisible(false);
		setHasFinishedUpdate(true);
	};

	return (
		<>
			{isVisible && (
				<AnalyticsCloudModal
					observer={observerSetupModal}
					onClose={onCloseSetupModal}
					project={project}
					subscriptionGroupId={
						subscriptionGroupAnalyticsCloud.accountSubscriptionGroupId
					}
				/>
			)}
			<ActivationStatusLayout
				activationStatus={activationStatus}
				activationStatusDate={activationStatusDate}
				iconPath={`${assetsPath}/assets/navigation-menu/analytics_icon.svg`}
				project={project}
				subscriptionGroupActivationStatus={
					subscriptionGroupActivationStatus
				}
			/>
			{visible && (
				<AnalyticsCloudStatusModal
					groupIdValue={groupIdValue}
					observer={observerStatusModal}
					onClose={onCloseStatusModal}
					project={project}
					setGroupIdValue={setGroupIdValue}
					updateCardStatus={updateGroupId}
				/>
			)}
			{hasFinishedUpdate && (
				<ClayAlert.ToastContainer>
					<ClayAlert
						autoClose={AUTO_CLOSE_ALERT_TIME.success}
						displayType="success"
						onClose={() => setHasFinishedUpdate(false)}
					>
						{ALERT_UPDATE_ANALYTICS_CLOUD_STATUS.success}
					</ClayAlert>
				</ClayAlert.ToastContainer>
			)}
		</>
	);
};

export default ActivationStatusAnalyticsCloud;
