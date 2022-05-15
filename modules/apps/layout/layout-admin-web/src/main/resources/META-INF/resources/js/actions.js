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

import {openModal} from 'frontend-js-web';

import openDeleteLayoutModal from './openDeleteLayoutModal';

const ACTIONS = {
	copyLayout: ({copyLayoutURL}, portletNamespace) => {
		openModal({
			height: '60vh',
			id: `${portletNamespace}addLayoutDialog`,
			size: 'md',
			title: Liferay.Language.get('copy-page'),
			url: copyLayoutURL,
		});
	},

	deleteLayout: ({deleteLayoutURL, message}) => {
		openDeleteLayoutModal({
			message,
			onDelete: () => {
				Liferay.Util.navigate(deleteLayoutURL);
			},
		});
	},

	discardDraft: ({discardDraftURL}) => {
		const discardDraftMessage = Liferay.Language.get(
			'are-you-sure-you-want-to-discard-current-draft-and-apply-latest-published-changes'
		);

		if (confirm(discardDraftMessage)) {
			Liferay.Util.navigate(discardDraftURL);
		}
	},

	exportTranslation: ({exportTranslationURL}) => {
		Liferay.Util.navigate(exportTranslationURL);
	},

	permissionLayout: ({permissionLayoutURL}) => {
		openModal({
			title: Liferay.Language.get('permissions'),
			url: permissionLayoutURL,
		});
	},

	viewCollectionItems: ({viewCollectionItemsURL}) => {
		openModal({
			title: Liferay.Language.get('collection-items'),
			url: viewCollectionItemsURL,
		});
	},
};

export default ACTIONS;
