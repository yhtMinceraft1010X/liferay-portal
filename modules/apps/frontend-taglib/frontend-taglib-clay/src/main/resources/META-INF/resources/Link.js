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
import ClayLink from '@clayui/link';
import classNames from 'classnames';
import React from 'react';

export default function Link({
	additionalProps: _additionalProps,
	componentId: _componentId,
	cssClass,
	icon,
	label,
	locale: _locale,
	portletId: _portletId,
	portletNamespace: _portletNamespace,
	...otherProps
}) {
	return (
		<ClayLink className={cssClass} {...otherProps}>
			{icon && (
				<span
					className={classNames('inline-item', {
						'inline-item-before': label,
					})}
				>
					<ClayIcon symbol={icon} />
				</span>
			)}

			{label}
		</ClayLink>
	);
}
