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

import ClayIcon from '@clayui/icon';
import React from 'react';

import getMimeTypeRestrictionData from '../utils/getMimeTypeRestrictionData';
import DragFileIcon from './DragFileIcon';

export default function DragFilePlaceholder({mimeTypeRestriction}) {
	const {icon, text} = getMimeTypeRestrictionData(mimeTypeRestriction);

	return (
		<>
			<div className="dropzone-drag-file-icon-wrapper">
				<DragFileIcon />

				<ClayIcon
					className="dropzone-drag-file-type-icon"
					symbol={icon}
				/>
			</div>

			{text}
		</>
	);
}
