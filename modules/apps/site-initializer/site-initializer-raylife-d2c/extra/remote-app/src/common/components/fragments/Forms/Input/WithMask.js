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

import React from 'react';
import ReactInputMask from 'react-number-format';

import {InputAreaWithError} from '../InputArea/WithError';
import {Label} from '../Label';

export const InputWithMask = React.forwardRef(
	(
		{
			allowNegative = false,
			className,
			error,
			label,
			name,
			renderActions,
			required = false,
			...props
		},
		ref
	) => {
		return (
			<InputAreaWithError className={className} error={error}>
				{label && (
					<Label label={label} name={name} required={required}>
						{renderActions}
					</Label>
				)}

				<ReactInputMask
					{...props}
					allowNegative={allowNegative}
					className="form-control input w-100"
					id={name}
					name={name}
					ref={ref}
					required={required}
				/>
			</InputAreaWithError>
		);
	}
);
