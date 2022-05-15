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

import React, {useEffect, useState} from 'react';

import {retrieveUsersBy} from '../../../../../../util/fetchUtil';
import AssetCreator from '../select-reassignment/AssetCreator';
import ResourceActions from '../select-reassignment/ResourceActions';
import Role from '../select-reassignment/Role';
import RoleType from '../select-reassignment/RoleType';
import ScriptedReassignment from '../select-reassignment/ScriptedReassignment';
import {SelectReassignment} from '../select-reassignment/SelectReassignment';
import User from '../select-reassignment/User';

const assignmentSectionComponents = {
	assetCreator: AssetCreator,
	resourceActions: ResourceActions,
	roleId: Role,
	roleType: RoleType,
	scriptedReassignment: ScriptedReassignment,
	user: User,
};

const ActionTypeReassignment = ({
	actionData,
	actionSectionsIndex,
	identifier,
	sectionsLength,
	setActionSections,
	setContentName,
	setErrors,
}) => {
	const reassignmentType = actionData.assignmentType;
	const [subSections, setSubSections] = useState(
		actionData?.users?.length &&
			actionData.users.some(({emailAddress}) => emailAddress)
			? actionData.users
			: [{identifier: `${Date.now()}-0`}]
	);

	useEffect(() => {
		if (reassignmentType === 'user') {
			setActionSections((currentSections) => {
				const updatedSections = [...currentSections];

				updatedSections[actionSectionsIndex].assignmentType = 'user';
				updatedSections[actionSectionsIndex].users = subSections;

				return updatedSections;
			});
		}

		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [subSections]);

	useEffect(() => {
		if (
			actionData?.users?.length &&
			actionData.users.some(({emailAddress}) => emailAddress)
		) {
			const retrievedUsers = [];
			retrieveUsersBy(
				'emailAddress',
				actionData.users.map(({emailAddress}) => emailAddress)
			)
				.then((response) => response.json())
				.then(({items}) => {
					items.forEach((item, index) => {
						retrievedUsers.push({
							emailAddress: item.emailAddress,
							identifier: `${Date.now()}-${index}`,
							name: item.name,
							screenName: item.alternateName,
							userId: item.id,
						});
					});
				})
				.then(() => {
					setSubSections(retrievedUsers);
				});
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	const updateReassignmentType = (value) => {
		setActionSections((currentSections) => {
			const updatedSections = [...currentSections];

			updatedSections[actionSectionsIndex].assignmentType = value;

			return updatedSections;
		});
	};

	const ReassignmentSectionComponent =
		assignmentSectionComponents[reassignmentType];

	return (
		<>
			<SelectReassignment
				currentAssignmentType={reassignmentType}
				setSection={updateReassignmentType}
				setSubSections={setSubSections}
			/>

			{subSections.map(
				({identifier: subSectionIdentifier, ...restProps}, index) => {
					return (
						ReassignmentSectionComponent && (
							<ReassignmentSectionComponent
								actionData={actionData}
								actionSectionsIndex={actionSectionsIndex}
								currentAssignmentType={reassignmentType}
								identifier={identifier}
								index={index}
								key={`section-${subSectionIdentifier}`}
								reassignmentType={reassignmentType}
								restProps={restProps}
								sectionsLength={sectionsLength}
								setActionSections={setActionSections}
								setContentName={setContentName}
								setErrors={setErrors}
								setSections={setSubSections}
								subSectionIdentifier={subSectionIdentifier}
								subSectionsLength={subSections.length}
							/>
						)
					);
				}
			)}
		</>
	);
};

export default ActionTypeReassignment;
