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

import {InputHTMLAttributes} from 'react';

import i18n from '../../i18n';
import InputWrapper from './InputWrapper';

type InputSelectProps = {
	defaultOption?: boolean;
	errors?: any;
	id?: string;
	label?: string;
	name: string;
	options: {label: string; value: string | number}[];
	register?: any;
	required?: boolean;
	type?: string;
} & InputHTMLAttributes<HTMLInputElement>;

const InputSelect: React.FC<InputSelectProps> = ({
	defaultOption = true,
	errors = {},
	label,
	name,
	register = () => {},
	id = name,
	options,
	required = false,
	...otherProps
}) => {
	return (
		<InputWrapper
			error={errors[name]?.message}
			label={label}
			required={required}
		>
			<select
				className="form-control rounded-xs"
				id={id}
				{...otherProps}
				{...register(name, {required})}
			>
				{defaultOption && (
					<option value="">
						{i18n.translate('choose-an-option')}
					</option>
				)}

				{options.map(({label, value}, index) => (
					<option key={index} value={value}>
						{label}
					</option>
				))}
			</select>
		</InputWrapper>
	);
};

export default InputSelect;
