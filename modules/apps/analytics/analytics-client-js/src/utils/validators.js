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

import {
	VALIDATION_PROPERTIES_MAXIMUM_LENGTH,
	VALIDATION_PROPERTY_NAME_MAXIMUM_LENGTH,
	VALIDATION_PROPERTY_VALUE_MAXIMUM_LENGTH,
} from './constants';

const isValidEvent = ({eventId, eventProps}) => {
	const validationsEventId = _validate([
		validateIsString('eventId'),
		validateEmptyString('eventId'),
		validateMaxLength(),
	]);
	const validationsEventProps = _validate([validatePropsLength()]);
	const validationsKey = _validate([
		validateEmptyString('eventPropKey'),
		validateMaxLength(),
	]);
	const validationsValue = _validate([
		validateMaxLength(VALIDATION_PROPERTY_VALUE_MAXIMUM_LENGTH),
		validateAttributeType,
	]);
	let errors = [];

	errors = errors.concat(validationsEventId(eventId));

	if (eventProps) {
		errors = errors.concat(validationsEventProps({eventId, eventProps}));

		for (const key in eventProps) {
			errors = errors.concat(
				validationsKey(key),
				validationsValue(eventProps[key])
			);
		}
	}

	if (errors.length) {
		_showErrors(errors);

		return false;
	}

	return true;
};

const validateAttributeType = (attributeValue) => {
	let error = '';

	const valid = ['string', 'number', 'boolean'].includes(
		typeof attributeValue
	);

	if (!valid) {
		error = 'Attribute must be a String, Number, or Boolean.';
	}

	return error;
};

const validateEmptyString = (labelField) => (str) => {
	let error = '';

	if (!String(str).length) {
		error = `${labelField} is required.`;
	}

	return error;
};

const validateIsString = (labelField) => (val) => {
	let error = '';

	if (typeof val !== 'string') {
		error = `${labelField} must be a string.`;
	}

	return error;
};

const validateMaxLength = (
	maxAllowed = VALIDATION_PROPERTY_NAME_MAXIMUM_LENGTH
) => (str) => {
	let error = '';

	if (String(str).length > maxAllowed) {
		error = `${str} exceeds maximum length of ${maxAllowed}`;
	}

	return error;
};

const validatePropsLength = (
	maxAllowed = VALIDATION_PROPERTIES_MAXIMUM_LENGTH
) => ({eventId, eventProps = {}}) => {
	let error = '';

	if (Object.keys(eventProps).length > maxAllowed) {
		error = `The Event ${eventId} attributes list exceeds maximum length of ${maxAllowed}`;
	}

	return error;
};

const _validate = (validators) => (value) =>
	validators
		.map((validator) => {
			if (typeof validator === 'function') {
				return validator(value);
			}
		})
		.filter(Boolean);

const _showErrors = (errorsArr) =>
	errorsArr.forEach((errMsg) => console.error(new Error(errMsg)));

export {
	isValidEvent,
	validateAttributeType,
	validateEmptyString,
	validateMaxLength,
	validatePropsLength,
	validateIsString,
};
