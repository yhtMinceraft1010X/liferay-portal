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
import {FormError} from '../hooks/useForm';
export default function ObjectValidationFormBase({
	disabled,
	objectValidationTypeLabel,
	setValues,
	values,
}: IProps): JSX.Element;
export declare function useObjectValidationForm({
	initialValues,
	onSubmit,
}: IUseObjectValidationForm): {
	errors: FormError<ObjectValidation>;
	handleChange: React.ChangeEventHandler<HTMLInputElement>;
	handleSubmit: React.FormEventHandler<HTMLFormElement>;
	setValues: (values: Partial<ObjectValidation>) => void;
	values: Partial<ObjectValidation>;
};
interface IUseObjectValidationForm {
	initialValues: Partial<ObjectValidation>;
	onSubmit: (validation: ObjectValidation) => void;
}
interface IProps {
	disabled: boolean;
	errors: ObjectValidationErrors;
	objectValidationTypeLabel: string;
	setValues: (values: Partial<ObjectValidation>) => void;
	values: Partial<ObjectValidation>;
}
export declare type ObjectValidationErrors = FormError<ObjectValidation>;
export {};
