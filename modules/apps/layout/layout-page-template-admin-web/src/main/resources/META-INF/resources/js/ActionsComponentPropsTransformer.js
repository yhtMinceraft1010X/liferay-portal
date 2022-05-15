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

import {openSelectionModal} from 'frontend-js-web';

import openDeletePageTemplateModal from './modal/openDeletePageTemplateModal';

const ACTIONS = {
	deleteCollections({
		deleteLayoutPageTemplateCollectionURL,
		portletNamespace,
		viewLayoutPageTemplateCollectionURL,
	}) {
		const layoutPageTemplateCollectionsForm = document.createElement(
			'form'
		);

		layoutPageTemplateCollectionsForm.setAttribute('method', 'post');

		openSelectionModal({
			buttonAddLabel: Liferay.Language.get('delete'),
			multiple: true,
			onSelect: (selectedItems) => {
				if (selectedItems) {
					openDeletePageTemplateModal({
						onDelete: () => {
							const input = document.createElement('input');

							input.name = `${portletNamespace}rowIds`;
							input.value = selectedItems.map(
								(item) => item.value
							);

							layoutPageTemplateCollectionsForm.appendChild(
								input
							);

							submitForm(
								layoutPageTemplateCollectionsForm,
								deleteLayoutPageTemplateCollectionURL
							);
						},
						title: Liferay.Language.get('page-template-sets'),
					});
				}
			},
			selectEventName: `${portletNamespace}selectCollections`,
			title: Liferay.Language.get('delete-collection'),
			url: viewLayoutPageTemplateCollectionURL,
		});
	},
};

export default function propsTransformer({
	additionalProps: {
		deleteLayoutPageTemplateCollectionURL,
		viewLayoutPageTemplateCollectionURL,
	},
	items,
	portletNamespace,
	...otherProps
}) {
	return {
		...otherProps,
		items: items.map((item) => {
			return {
				...item,
				onClick(event) {
					const action = item.data?.action;

					if (action) {
						event.preventDefault();

						ACTIONS[action]({
							deleteLayoutPageTemplateCollectionURL,
							portletNamespace,
							viewLayoutPageTemplateCollectionURL,
						});
					}
				},
			};
		}),
	};
}
