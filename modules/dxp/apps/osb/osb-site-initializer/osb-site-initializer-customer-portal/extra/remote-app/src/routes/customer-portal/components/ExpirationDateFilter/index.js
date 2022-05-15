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

import {ClayCheckbox} from '@clayui/form';
import {useCallback, useEffect, useState} from 'react';
import i18n from '../../../../common/I18n';
import DateFilter from '../DateFilter';

const DNE_YEARS = 100;

export default function ExpirationDateFilter({
	clearInputs,
	hasDNE,
	setFilters,
}) {
	const [dneChecked, setDNEChecked] = useState(false);

	const getOnOrAfterValue = useCallback(
		(currentValue) => {
			if (dneChecked) {
				const today = new Date();
				today.setFullYear(today.getFullYear() + DNE_YEARS);

				return today;
			}

			return currentValue;
		},
		[dneChecked]
	);

	useEffect(() => {
		if (clearInputs) {
			setDNEChecked(false);
		}
	}, [clearInputs]);

	return (
		<DateFilter
			clearInputs={clearInputs}
			onOrAfterDisabled={dneChecked}
			onOrBeforeDisabled={dneChecked}
			updateFilters={(onOrAfter, onOrBefore) =>
				setFilters((previousFilters) => ({
					...previousFilters,
					expirationDate: {
						...previousFilters.expirationDate,
						value: {
							onOrAfter: getOnOrAfterValue(onOrAfter),
							onOrBefore,
						},
					},
				}))
			}
		>
			{hasDNE && (
				<ClayCheckbox
					checked={dneChecked}
					label={i18n.translate('does-not-expire')}
					onChange={() =>
						setDNEChecked(
							(previousDNEChecked) => !previousDNEChecked
						)
					}
				/>
			)}
		</DateFilter>
	);
}
