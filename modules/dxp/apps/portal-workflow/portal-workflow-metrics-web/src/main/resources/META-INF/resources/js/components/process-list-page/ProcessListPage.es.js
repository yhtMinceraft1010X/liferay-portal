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

import {ManagementToolbar} from 'frontend-js-components-web';
import React, {useMemo} from 'react';

import HeaderKebab from '../../shared/components/header/HeaderKebab.es';
import PromisesResolver from '../../shared/components/promises-resolver/PromisesResolver.es';
import ResultsBar from '../../shared/components/results-bar/ResultsBar.es';
import {parse} from '../../shared/components/router/queryString.es';
import SearchField from '../../shared/components/search-field/SearchField.es';
import {useFetch} from '../../shared/hooks/useFetch.es';
import {usePageTitle} from '../../shared/hooks/usePageTitle.es';
import Body from './ProcessListPageBody.es';

const Header = ({page, pageSize, search, sort, totalCount}) => {
	return (
		<>
			<ManagementToolbar.Container className="mb-0">
				<SearchField disabled={!search && totalCount === 0} />
			</ManagementToolbar.Container>

			{search && (
				<ResultsBar>
					<ResultsBar.TotalCount
						search={search}
						totalCount={totalCount}
					/>

					<ResultsBar.Clear
						page={page}
						pageSize={pageSize}
						sort={sort}
					/>
				</ResultsBar>
			)}
		</>
	);
};

function ProcessListPage({history, query, routeParams}) {
	if (history.location.pathname === '/') {
		history.replace(`/processes/20/1/overdueInstanceCount:desc`);
	}

	usePageTitle(Liferay.Language.get('metrics'));

	const {page, pageSize, sort} = routeParams;
	const {search = ''} = parse(query);

	const {data, fetchData} = useFetch({
		params: {
			title: search,
			...routeParams,
		},
		url: '/processes/metrics',
	});

	const promises = useMemo(() => {
		if (page && pageSize && sort) {
			return [fetchData()];
		}

		return [new Promise(() => {})];

		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [page, pageSize, search, sort]);

	return (
		<PromisesResolver promises={promises}>
			<HeaderKebab
				kebabItems={[
					{
						label: Liferay.Language.get('settings'),
						link: `/settings/indexes`,
					},
				]}
			/>

			<ProcessListPage.Header
				search={search}
				totalCount={data?.totalCount}
				{...routeParams}
			/>

			<ProcessListPage.Body {...data} filtered={search} />
		</PromisesResolver>
	);
}

ProcessListPage.Body = Body;
ProcessListPage.Header = Header;

export default ProcessListPage;
