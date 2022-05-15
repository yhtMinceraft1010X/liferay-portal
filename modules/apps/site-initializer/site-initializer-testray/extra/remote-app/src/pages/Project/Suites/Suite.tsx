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

import {useOutletContext} from 'react-router-dom';

import Container from '../../../components/Layout/Container';
import ListView from '../../../components/ListView/ListView';
import QATable from '../../../components/Table/QATable';
import {
	TestrayComponent,
	TestraySuite,
	getCases,
} from '../../../graphql/queries';
import i18n from '../../../i18n';
import dayjs from '../../../util/date';

const Suite = () => {
	const {
		projectId,
		testraySuite,
	}: {projectId: number; testraySuite: TestraySuite} = useOutletContext();

	return (
		<>
			<Container collapsable title={i18n.translate('details')}>
				<QATable
					items={[
						{
							title: i18n.translate('description'),
							value: testraySuite?.description,
						},
						{
							title: i18n.translate('create-date'),
							value: dayjs(testraySuite?.dateCreated).format(
								'lll'
							),
						},
						{
							title: i18n.translate('date-last-modified'),
							value: dayjs(testraySuite?.dateModified).format(
								'lll'
							),
						},
						{
							title: i18n.translate('created-by'),
							value: testraySuite.creator.name,
						},
					]}
				/>
			</Container>

			<Container
				className="mt-4"
				collapsable
				title={i18n.translate('case-parameters')}
			>
				<QATable
					items={[
						{
							title: i18n.translate('case-types'),
							value: 'Manual Test',
						},
						{
							title: i18n.translate('components'),
							value: 'Deployment',
						},
						{
							title: i18n.translate('subcomponents'),
							value: '',
						},
						{title: i18n.translate('priority'), value: '5'},
						{title: i18n.translate('requirements'), value: ''},
					]}
				/>
			</Container>

			<Container className="mt-4">
				<ListView
					query={getCases}
					tableProps={{
						columns: [
							{
								key: 'priority',
								value: i18n.translate('priority'),
							},
							{
								key: 'component',
								render: (component: TestrayComponent) =>
									component?.name,
								value: i18n.translate('component'),
							},
							{
								clickable: true,
								key: 'name',
								value: i18n.translate('case-name'),
							},
						],
						navigateTo: ({id}) =>
							`/project/${projectId}/cases/${id}`,
					}}
					transformData={(data) => data?.cases}
				/>
			</Container>
		</>
	);
};

export default Suite;
