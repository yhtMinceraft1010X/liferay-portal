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

import {useEffect, useState} from 'react';
import {useOutletContext} from 'react-router-dom';
import client from '../../../../../apolloClient';
import {Liferay} from '../../../../../common/services/liferay';
import {getDXPCloudEnvironment} from '../../../../../common/services/liferay/graphql/queries';
import ActivationStatus from '../../../components/ActivationStatus/index';
import {useCustomerPortal} from '../../../context';
import DeveloperKeysLayouts from '../../../layouts/DeveloperKeysLayout';
import {LIST_TYPES, PRODUCT_TYPES} from '../../../utils/constants';

const DXPCloud = () => {
	const [
		{project, sessionId, subscriptionGroups, userAccount},
	] = useCustomerPortal();
	const {setHasQuickLinksPanel, setHasSideMenu} = useOutletContext();
	const [dxpCloudEnvironment, setDxpCloudEnvironment] = useState();

	useEffect(() => {
		setHasQuickLinksPanel(true);
		setHasSideMenu(true);
	}, [setHasSideMenu, setHasQuickLinksPanel]);

	useEffect(() => {
		const getDxpCloudEnvironmentData = async () => {
			const {data} = await client.query({
				query: getDXPCloudEnvironment,
				variables: {
					filter: `accountKey eq '${project.accountKey}'`,
					scopeKey: Liferay.ThemeDisplay.getScopeGroupId(),
				},
			});

			if (data) {
				const items = data.c?.dXPCloudEnvironments?.items;

				if (items.length) {
					setDxpCloudEnvironment(items[0]);
				}
			}
		};

		getDxpCloudEnvironmentData();
	}, [project, subscriptionGroups]);

	return (
		<div className="mr-4">
			<ActivationStatus.DXPCloud
				dxpCloudEnvironment={dxpCloudEnvironment}
				project={project}
				subscriptionGroupDXPCloud={subscriptionGroups.find(
					(subscriptionGroup) =>
						subscriptionGroup.name === PRODUCT_TYPES.dxpCloud
				)}
				userAccount={userAccount}
			/>

			<DeveloperKeysLayouts>
				<DeveloperKeysLayouts.Inputs
					accountKey={project.accountKey}
					downloadTextHelper="To activate a local instance of Liferay DXP, download a developer key for your Liferay DXP version."
					dxpVersion={project.dxpVersion}
					listType={LIST_TYPES.dxpVersion}
					productName="DXP"
					projectName={project.name}
					sessionId={sessionId}
				></DeveloperKeysLayouts.Inputs>
			</DeveloperKeysLayouts>
		</div>
	);
};

export default DXPCloud;
