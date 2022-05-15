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

import {useEffect, useRef} from 'react';

import SearchBar from '../../../../components/SearchBar';

const TeamMembersFilter = ({userAccounts, filtersState: [setFilters]}) => {
	const countFetchUserAccountsRef = useRef(0);

	useEffect(() => {
		if (userAccounts) {
			countFetchUserAccountsRef.current = ++countFetchUserAccountsRef.current;
		}
	}, [userAccounts]);

	return (
		<div className="d-flex flex-column">
			<div className="d-flex">
				<SearchBar setFilters={setFilters} />
			</div>
		</div>
	);
};

export default TeamMembersFilter;
