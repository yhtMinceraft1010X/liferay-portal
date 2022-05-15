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

import ClayAlert from '@clayui/alert';
import React from 'react';

import AddNewTabButton from './AddNewTabButton';
import ObjectLayoutTabs from './ObjectLayoutTabs';

import './LayoutScreen.scss';

export default function LayoutScreen() {
	return (
		<div className="lfr-objects__side-panel-content-container">
			<ClayAlert
				displayType="info"
				title={`${Liferay.Language.get('info')}:`}
			>
				{Liferay.Language.get(
					'only-the-first-tab-will-be-used-when-creating-a-new-entry'
				)}
			</ClayAlert>

			<AddNewTabButton />

			<ObjectLayoutTabs />
		</div>
	);
}
