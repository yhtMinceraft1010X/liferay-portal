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

import {ClayButtonWithIcon} from '@clayui/button';
import {ClayInput} from '@clayui/form';
import {ManagementToolbar} from 'frontend-js-components-web';
import React from 'react';

const SearchControls = ({
	disabled,
	searchActionURL,
	searchData,
	searchFormMethod,
	searchFormName,
	searchInputAutoFocus,
	searchInputName,
	searchMobile,
	searchValue,
	setSearchMobile,
}) => {
	return (
		<>
			<ManagementToolbar.Search
				action={searchActionURL}
				method={searchFormMethod}
				name={searchFormName}
				showMobile={searchMobile}
			>
				<ClayInput.Group>
					<ClayInput.GroupItem>
						<ClayInput
							autoFocus={searchInputAutoFocus}
							className="form-control input-group-inset input-group-inset-after"
							defaultValue={searchValue}
							disabled={disabled}
							name={searchInputName}
							placeholder={Liferay.Language.get('search-for')}
							type="text"
						/>

						<ClayInput.GroupInsetItem after tag="span">
							<ClayButtonWithIcon
								className="navbar-breakpoint-d-none"
								disabled={disabled}
								displayType="unstyled"
								onClick={() => setSearchMobile(false)}
								symbol="times"
							/>

							<ClayButtonWithIcon
								disabled={disabled}
								displayType="unstyled"
								symbol="search"
								title={Liferay.Language.get('search-for')}
								type="submit"
							/>
						</ClayInput.GroupInsetItem>
					</ClayInput.GroupItem>
				</ClayInput.Group>

				{searchData &&
					Object.keys(searchData).map((key) =>
						searchData[key].map((value) => (
							<ClayInput
								key={key}
								name={key}
								type="hidden"
								value={value}
							/>
						))
					)}
			</ManagementToolbar.Search>
		</>
	);
};

const ShowMobileButton = ({disabled, setSearchMobile}) => {
	return (
		<ManagementToolbar.Item className="navbar-breakpoint-d-none">
			<ClayButtonWithIcon
				className="nav-link nav-link-monospaced"
				disabled={disabled}
				displayType="unstyled"
				onClick={() => setSearchMobile(true)}
				symbol="search"
			/>
		</ManagementToolbar.Item>
	);
};

SearchControls.ShowMobileButton = ShowMobileButton;

export default SearchControls;
