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

import ClayForm from '@clayui/form';
import classNames from 'classnames';

import i18n from '../../i18n';
import InputWarning from './InputWarning';

type InputWrapper = {
	description?: string;
	error?: string;
	label?: string;
	required?: boolean;
};

const InputWrapper: React.FC<InputWrapper> = ({
	children,
	description,
	error,
	label,
	required,
}) => {
	return (
		<ClayForm.Group
			className={classNames({
				'has-error': error,
			})}
		>
			{label && (
				<label
					className={classNames(
						'font-weight-normal mb-1 mx-0 text-paragraph',
						{required}
					)}
				>
					{i18n.translate(label)}
				</label>
			)}

			{children}

			{description && (
				<small className="form-text text-muted" id="Help">
					{description}
				</small>
			)}

			{error && <InputWarning>{error}</InputWarning>}
		</ClayForm.Group>
	);
};

export default InputWrapper;
