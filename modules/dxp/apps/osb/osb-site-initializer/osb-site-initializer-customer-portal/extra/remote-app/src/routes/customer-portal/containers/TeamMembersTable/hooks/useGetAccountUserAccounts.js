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
import client from '../../../../../apolloClient';
import {getAccountUserAccountsByExternalReferenceCode} from '../../../../../common/services/liferay/graphql/queries';

const MAX_PAGE_SIZE = 9999;

export default function useGetAccountUserAccount(project) {
	const [userAccounts, setUserAccounts] = useState([]);
	const [isLoadingUserAccounts, setIsLoadingUserAccounts] = useState(false);
	const [filterTerm, setFilterTerm] = useState('');

	useEffect(() => {
		setIsLoadingUserAccounts(true);
		const getAccountUserAccounts = async () => {
			const {data} = await client.query({
				query: getAccountUserAccountsByExternalReferenceCode,
				variables: {
					externalReferenceCode: project.accountKey,
					filter: filterTerm,
					pageSize: MAX_PAGE_SIZE,
				},
			});

			if (data) {
				const accountUserAccounts = data.accountUserAccountsByExternalReferenceCode?.items?.reduce(
					(userAccountsAccumulator, userAccount) => {
						const currentAccountBrief = userAccount.accountBriefs?.find(
							(accountBrief) =>
								accountBrief.externalReferenceCode ===
								project?.accountKey
						);
						if (currentAccountBrief) {
							userAccountsAccumulator.push({
								...userAccount,
								roles: currentAccountBrief.roleBriefs?.map(
									({name}) => name
								),
							});
						}

						return userAccountsAccumulator;
					},
					[]
				);

				setUserAccounts(accountUserAccounts);
			}

			setIsLoadingUserAccounts(false);
		};
		getAccountUserAccounts();
	}, [filterTerm, project.accountKey]);

	return {
		isLoadingUserAccounts,
		setFilterTerm,
		userAccountsState: [userAccounts, setUserAccounts],
	};
}
