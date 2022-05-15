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

import {TEAM_MEMBERS_ACTION_TYPES} from '..';
import {ButtonWithIcon} from '@clayui/core';
import i18n from '../../../../../../../common/I18n';
import {Button, ButtonDropDown} from '../../../../../../../common/components';
import {getIsEditingUser} from '../../getIsEditingUser';

const MenuUserActions = ({cancelChanges, confirmChanges, userAccount}) => (
	<div className="align-items-center d-flex">
		<Button
			className="mr-2"
			displayType="secondary"
			onClick={cancelChanges}
			small
		>
			{i18n.translate('cancel')}
		</Button>

		<Button onClick={() => confirmChanges(userAccount)} small>
			{i18n.translate('save')}
		</Button>
	</div>
);

const OptionsColumnType = ({
	confirmChanges,
	setSelectedRole,
	setUserAction,
	userAccount,
	userAction,
}) => {
	const userOptions = [
		{
			label: i18n.translate('edit'),
			onClick: () =>
				setUserAction({
					type: TEAM_MEMBERS_ACTION_TYPES.edit,
					userId: userAccount?.id,
				}),
		},
		{
			customOptionStyle: 'cp-remove-member-option',
			label: i18n.translate('remove'),
			onClick: () =>
				setUserAction({
					type: TEAM_MEMBERS_ACTION_TYPES.remove,
					userId: userAccount?.id,
				}),
		},
	];

	const handleOnCancelChanges = () => {
		setSelectedRole();
		setUserAction(TEAM_MEMBERS_ACTION_TYPES.close);
	};

	const isEditingUser = getIsEditingUser(userAction, userAccount?.id);

	return isEditingUser ? (
		<MenuUserActions
			cancelChanges={handleOnCancelChanges}
			confirmChanges={confirmChanges}
			userAccount={userAccount}
		/>
	) : (
		<ButtonDropDown
			customDropDownButton={
				<ButtonWithIcon displayType="null" small symbol="ellipsis-v" />
			}
			items={userOptions}
			menuElementAttrs={{
				className: 'p-0',
			}}
		/>
	);
};

export {OptionsColumnType};
