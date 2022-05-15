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

import i18n from '../../../../../../common/I18n';

export const ACTIVATION_STATUS = {
	activated: {
		color: 'success',
		id: 'activated',
		title: i18n.translate('activated'),
	},
	all: {
		color: 'none',
		id: 'all',
		title: i18n.translate('all'),
	},
	expired: {
		color: 'danger',
		id: 'expired',
		title: i18n.translate('expired'),
	},
	notActivated: {
		color: 'info',
		id: 'notActivated',
		title: i18n.translate('not-activated'),
	},
};
