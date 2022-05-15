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

import {ClayButtonWithIcon} from '@clayui/button';
import {ClayInput} from '@clayui/form';
import {useState} from 'react';
import i18n from '../../../../common/I18n';

const SearchBar = ({setFilters}) => {
	const [searchTerm, setSearchTerm] = useState('');
	const [isSearchButton, setIsSearchButton] = useState(true);

	const updateSearchTermFilter = (searchTerm) => {
		setFilters((previousFilters) => ({
			...previousFilters,
			searchTerm,
		}));
	};

	const updateSearchFilter = () => {
		if (isSearchButton) {
			setIsSearchButton(false);
			updateSearchTermFilter(searchTerm);
		}
		else {
			setSearchTerm('');
			updateSearchTermFilter('');
			setIsSearchButton(true);
		}
	};

	return (
		<ClayInput.Group className="m-0 mr-2">
			<ClayInput.GroupItem>
				<ClayInput
					aria-label="Search"
					className="form-control input-group-inset input-group-inset-after"
					onChange={(event) => {
						setSearchTerm(event.target.value);
						setIsSearchButton(true);
					}}
					onKeyPress={(event) => {
						if (event.key === 'Enter') {
							updateSearchFilter();
						}
					}}
					placeholder={i18n.translate('search')}
					type="text"
					value={searchTerm}
				/>

				<ClayInput.GroupInsetItem after tag="span">
					{isSearchButton ? (
						<ClayButtonWithIcon
							displayType="unstyled"
							onClick={() => updateSearchFilter()}
							symbol="search"
						/>
					) : (
						<ClayButtonWithIcon
							className="navbar-breakpoint-d-none"
							displayType="unstyled"
							onClick={() => updateSearchFilter()}
							symbol="times"
						/>
					)}
				</ClayInput.GroupInsetItem>
			</ClayInput.GroupItem>
		</ClayInput.Group>
	);
};
export default SearchBar;
