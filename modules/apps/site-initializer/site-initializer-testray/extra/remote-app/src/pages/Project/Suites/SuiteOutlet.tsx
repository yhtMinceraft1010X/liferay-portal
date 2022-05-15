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

import {useQuery} from '@apollo/client';
import {useEffect} from 'react';
import {Outlet, useOutletContext, useParams} from 'react-router-dom';

import {CType, TestraySuite, getSuite} from '../../../graphql/queries';
import {useHeader} from '../../../hooks';
import i18n from '../../../i18n';

const SuiteOutlet = () => {
	const {projectId, testraySuiteId} = useParams();
	const {testrayProject}: any = useOutletContext();
	const {setHeading} = useHeader();

	const {data} = useQuery<CType<'suite', TestraySuite>>(getSuite, {
		variables: {
			suiteId: testraySuiteId,
		},
	});

	const testraySuite = data?.c.suite;

	useEffect(() => {
		if (testraySuite && testrayProject) {
			setHeading([
				{
					category: i18n.translate('project').toUpperCase(),
					path: `/project/${testrayProject.id}/suites`,
					title: testrayProject.name,
				},
				{
					category: i18n.translate('suite').toUpperCase(),
					title: testraySuite.name,
				},
			]);
		}
	}, [testraySuite, testrayProject, setHeading]);

	if (testraySuite) {
		return <Outlet context={{projectId, testraySuite}} />;
	}

	return null;
};

export default SuiteOutlet;
