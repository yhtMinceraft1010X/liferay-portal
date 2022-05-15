/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

const EMAIL_REGEX = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
const LOWCASE_NUMBERS_REGEX = /^[0-9a-z]+$/;
const FRIENDLY_URL_REGEX = /^\/[^. "]+[0-9a-z]+[^A-Z]$/;

const required = (value) => {
	if (!value) {
		return 'This field is required.';
	}
};

const maxLength = (value, max) => {
	if (value.length > max) {
		return `This field exceeded ${max} characters.`;
	}
};

const isValidEmail = (value, bannedEmailDomains) => {
	if (value && !EMAIL_REGEX.test(value)) {
		return 'Please insert a valid e-mail.';
	}

	if (bannedEmailDomains.length) {
		return 'E-mail domain not allowed.';
	}
};

const isValidEmailDomain = (bannedEmailDomains) => {
	if (bannedEmailDomains.length) {
		return 'Domain not allowed.';
	}
};

const isLowercaseAndNumbers = (value) => {
	if (value && !LOWCASE_NUMBERS_REGEX.test(value)) {
		return 'Lowercase letters and numbers only.';
	}
};

const isValidFriendlyURL = (value) => {
	if (value && value[0] !== '/') {
		return 'The Workspace URL should start with "/".';
	}

	if (value && value.indexOf(' ') > 0) {
		return 'The Workspace URL most not have spaces.';
	}

	if (value && !FRIENDLY_URL_REGEX.test(value)) {
		return 'Lowercase letters, numbers and dashes only.';
	}
};

const isValidHost = (value) => {
	if (value.indexOf(' ') > 0) {
		return 'The Workspace Host most not have spaces.';
	}
};

const isValidIp = (value) => {
	if (!value) {
		return;
	}

	const ipArray = value.split('\n');

	for (let i = 0; i < ipArray.length; i++) {
		if (ipArray[i].indexOf(' ') > 0) {
			return 'The Ip most not have spaces.';
		}

		if (
			!/^(?:(?:^|\.)(?:2(?:5[0-5]|[0-4]\d)|1?\d?\d)){4}$/.test(ipArray[i])
		) {
			return 'Invalid IP.';
		}
	}
};

const isValidMac = (value) => {
	if (!value) {
		return;
	}

	const macArray = value.split('\n');

	for (let i = 0; i < macArray.length; i++) {
		if (macArray[i].indexOf(' ') > 0) {
			return 'The Mac most not have spaces.';
		}

		if (!/^([0-9A-F]{2}[.:-]){5}[0-9A-F]{2}$/i.test(macArray[i])) {
			return 'Invalid Mac.';
		}
	}
};

const validate = (validations, value) => {
	let error;

	if (validations) {
		validations.forEach((validation) => {
			const callback = validation(value);

			if (callback) {
				error = callback;
			}
		});
	}

	return error;
};

export {
	isLowercaseAndNumbers,
	isValidEmail,
	isValidFriendlyURL,
	isValidEmailDomain,
	maxLength,
	required,
	validate,
	isValidHost,
	isValidIp,
	isValidMac,
};
