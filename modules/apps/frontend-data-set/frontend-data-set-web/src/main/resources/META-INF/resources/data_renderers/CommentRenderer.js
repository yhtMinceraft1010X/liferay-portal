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

import ClayButton from '@clayui/button';
import ClayIcon from '@clayui/icon';
import React from 'react';

function CommentRenderer({children}) {
	return (
		<ClayButton
			className="comment-renderer inline-item ml-2 my-n2 px-1 text-warning"
			data-tooltip-align="top"
			data-tooltip-delay={0}
			displayType="link"
			title={children}
		>
			<ClayIcon symbol="info-circle" />
		</ClayButton>
	);
}

export default CommentRenderer;
