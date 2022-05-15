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
import {
	Outlet,
	useLocation,
	useOutletContext,
	useParams,
} from 'react-router-dom';

import {TestrayCase, getCase} from '../../../graphql/queries';
import useHeader from '../../../hooks/useHeader';
import i18n from '../../../i18n';
import {isIncludingFormPage} from '../../../util';

const CaseOutlet = () => {
	const {testrayProject}: any = useOutletContext();
	const {caseId, projectId} = useParams();
	const {pathname} = useLocation();
	const basePath = `/project/${projectId}/cases/${caseId}`;
	const isFormPage = isIncludingFormPage(pathname);

	const {setHeading, setTabs} = useHeader();

	const {data} = useQuery<{case: TestrayCase}>(getCase, {
		variables: {
			caseId,
		},
	});

	const testrayCase = data?.case;

	useEffect(() => {
		if (testrayCase && testrayProject) {
			setTimeout(() => {
				setHeading([
					{
						category: i18n.translate('project').toUpperCase(),
						path: `/project/${testrayProject.id}/cases`,
						title: testrayProject.name,
					},
					{
						category: i18n.translate('case').toUpperCase(),
						title: testrayCase.name,
					},
				]);
			}, 0);
		}
	}, [testrayProject, testrayCase, setHeading]);

	useEffect(() => {
		if (!isFormPage) {
			setTabs([
				{
					active: pathname === basePath,
					path: basePath,
					title: i18n.translate('case-details'),
				},
				{
					active: pathname === `${basePath}/requirements`,
					path: `${basePath}/requirements`,
					title: i18n.translate('requirements'),
				},
			]);
		}
	}, [basePath, isFormPage, pathname, setTabs]);

	if (testrayCase) {
		return <Outlet context={{testrayCase}} />;
	}

	return null;
};

export default CaseOutlet;
