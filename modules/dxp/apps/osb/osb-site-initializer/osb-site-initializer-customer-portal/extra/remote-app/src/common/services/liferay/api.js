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

import {Liferay} from '.';

const HEADLESS_BASE_URL = `${window.location.origin}/o/headless-delivery/v1.0`;

const fetchHeadless = async ({resolveAsJson = true, url}) => {
	// eslint-disable-next-line @liferay/portal/no-global-fetch
	const response = await fetch(`${HEADLESS_BASE_URL}${url}`, {
		headers: {
			'Accept-Language': Liferay.ThemeDisplay.getBCP47LanguageId(),
			'Cache-Control': 'max-age=30, stale-while-revalidate=30',
			'x-csrf-token': Liferay.authToken,
		},
	});

	if (resolveAsJson) {
		return response.json();
	}

	return response;
};

export {fetchHeadless};
