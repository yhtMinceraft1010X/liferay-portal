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

import ClayForm, {ClayCheckbox} from '@clayui/form';
import PropTypes from 'prop-types';
import React from 'react';

export function ShowGutterSelector({checked, handleConfigurationChanged}) {
	return (
		<ClayForm.Group small>
			<ClayCheckbox
				checked={checked}
				label={Liferay.Language.get('show-gutter')}
				onChange={({target: {checked}}) =>
					handleConfigurationChanged({
						gutters: checked,
					})
				}
			/>
		</ClayForm.Group>
	);
}

ShowGutterSelector.propTypes = {
	checked: PropTypes.bool.isRequired,
	handleConfigurationChanged: PropTypes.func.isRequired,
};
