/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import {useOutletContext, useParams} from 'react-router-dom';

import Container from '../../../components/Layout/Container';
import ListView from '../../../components/ListView/ListView';
import StatusBadge from '../../../components/StatusBadge';
import QATable from '../../../components/Table/QATable';
import {TestrayCase, getCaseResults} from '../../../graphql/queries';
import i18n from '../../../i18n';
import {getStatusLabel} from '../../../util/constants';
import dayjs from '../../../util/date';
import useCaseActions from './useCaseActions';

const Case = () => {
	const {caseId, projectId} = useParams();
	const {testrayCase}: {testrayCase: TestrayCase} = useOutletContext();
	const {actions, formModal} = useCaseActions();

	return (
		<>
			<Container collapsable title={i18n.translate('details')}>
				<QATable
					items={[
						{
							title: i18n.translate('type'),
							value: testrayCase.caseType?.name,
						},
						{
							title: i18n.translate('priority'),
							value: testrayCase.priority,
						},
						{
							title: i18n.translate('main-component'),
							value: testrayCase.component?.name,
						},
						{
							title: i18n.translate('description'),
							value: testrayCase.description,
						},
						{
							title: i18n.translate('estimed-duration'),
							value: testrayCase.estimatedDuration,
						},
						{
							title: i18n.translate('steps'),
							value: testrayCase.steps,
						},
						{
							title: i18n.translate('date-created'),
							value: dayjs(testrayCase.dateCreated).format('lll'),
						},
						{
							title: i18n.translate('date-modified'),
							value: dayjs(testrayCase.dateModified).format(
								'lll'
							),
						},
						{
							title: i18n.translate('all-issues-found'),
							value: '-',
						},
					]}
				/>
			</Container>

			<Container className="mt-3" title={i18n.translate('test-history')}>
				<ListView
					forceRefetch={formModal.forceRefetch}
					initialContext={{
						filters: {
							columns: {
								caseType: false,
								dateCreated: false,
								dateModified: false,
								issues: false,
								team: false,
							},
						},
					}}
					managementToolbarProps={{
						visible: true,
					}}
					query={getCaseResults}
					tableProps={{
						actions,
						columns: [
							{
								clickable: true,
								key: 'dateCreated',
								render: (date) => dayjs(date).format('lll'),
								size: 'sm',
								value: i18n.translate('create-date'),
							},
							{
								clickable: true,
								key: 'build',
								render: (build) => {
									return build?.gitHash;
								},
								value: i18n.translate('git-hash'),
							},
							{
								clickable: true,
								key: 'product-version',
								render: (_, {build}) => {
									return build?.productVersion?.name;
								},
								value: i18n.translate('product-version'),
							},
							{
								clickable: true,
								key: 'run',
								render: (run) => {
									return run?.externalReferencePK;
								},
								size: 'lg',
								value: i18n.translate('environment'),
							},
							{
								clickable: true,
								key: 'routine',
								render: (_, {build}) => build?.routine?.name,
								value: i18n.translate('routine'),
							},
							{
								key: 'dueStatus',
								render: (dueStatus) => {
									return (
										<StatusBadge
											type={getStatusLabel(
												dueStatus
											)?.toLowerCase()}
										>
											{getStatusLabel(dueStatus)}
										</StatusBadge>
									);
								},
								value: i18n.translate('status'),
							},
							{
								key: 'warnings',
								value: i18n.translate('warnings'),
							},
							{key: 'issues', value: i18n.translate('issues')},
							{key: 'errors', value: i18n.translate('errors')},
						],
						navigateTo: ({build, id}) =>
							`/project/${projectId}/routines/${build?.routine?.id}/build/${build?.id}/case-result/${id}`,
					}}
					transformData={(data) => data?.caseResults}
					variables={{filter: `caseId eq ${caseId}`}}
				/>
			</Container>
		</>
	);
};

export default Case;
