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

import {addParams, openModal, openSelectionModal} from 'frontend-js-web';

import openDeleteArticleModal from './modals/openDeleteArticleModal';

const ACTIONS = {
	compareVersions({itemData, portletNamespace}) {
		openSelectionModal({
			onSelect: (selectedItem) => {
				let url = itemData.redirectURL;

				url = addParams(
					`${portletNamespace}sourceVersion=${selectedItem.sourceversion}`,
					url
				);
				url = addParams(
					`${portletNamespace}targetVersion=${selectedItem.targetversion}`,
					url
				);

				location.href = url;
			},
			selectEventName: `${portletNamespace}selectVersionFm`,
			title: Liferay.Language.get('compare-versions'),
			url: itemData.compareVersionsURL,
		});
	},

	copyArticle({itemData}) {
		this.send(itemData.copyArticleURL);
	},

	delete({itemData, trashEnabled}) {
		if (trashEnabled) {
			this.send(itemData.deleteURL);

			return;
		}

		openDeleteArticleModal({
			onDelete: () => {
				this.send(itemData.deleteURL);
			},
		});
	},

	deleteArticleTranslations({itemData, portletNamespace}) {
		openSelectionModal({
			buttonAddLabel: Liferay.Language.get('delete'),
			multiple: true,
			onSelect: (selectedItems) => {
				if (selectedItems?.length) {
					if (
						confirm(
							Liferay.Language.get(
								'are-you-sure-you-want-to-delete-the-selected-entries'
							)
						)
					) {
						const form = document.hrefFm;

						if (!form) {
							return;
						}

						const input = document.createElement('input');

						input.name = `${portletNamespace}rowIds`;
						input.value = selectedItems.map((item) => item.value);

						form.appendChild(input);

						submitForm(form, itemData.deleteArticleTranslationsURL);
					}
				}
			},
			title: Liferay.Language.get('delete-translations'),
			url: itemData.selectArticleTranslationsURL,
		});
	},

	discardArticleDraft({itemData}) {
		this.send(itemData.discardArticleDraftURL);
	},

	expireArticles({itemData}) {
		this.send(itemData.expireURL);
	},

	permissions({itemData}) {
		openModal({
			title: Liferay.Language.get('permissions'),
			url: itemData.permissionsURL,
		});
	},

	preview({itemData}) {
		openModal({
			iframeBodyCssClass: '',
			title: itemData.title,
			url: itemData.previewURL,
		});
	},

	publishArticleToLive({itemData}) {
		if (
			confirm(
				Liferay.Language.get(
					'are-you-sure-you-want-to-publish-the-selected-web-content'
				)
			)
		) {
			this.send(itemData.publishArticleURL);
		}
	},

	publishFolderToLive({itemData}) {
		if (
			confirm(
				Liferay.Language.get(
					'are-you-sure-you-want-to-publish-the-selected-folder'
				)
			)
		) {
			this.send(itemData.publishFolderURL);
		}
	},

	send(url) {
		submitForm(document.hrefFm, url);
	},

	subscribeArticle({itemData}) {
		this.send(itemData.subscribeArticleURL);
	},

	unsubscribeArticle({itemData}) {
		this.send(itemData.unsubscribeArticleURL);
	},
};

export default function propsTransformer({
	actions,
	additionalProps: {trashEnabled},
	items,
	portletNamespace,
	...props
}) {
	const bindAction = (item) => {
		const action = ACTIONS[item.data?.action];

		const transformedItem = {...item};

		if (typeof action === 'function') {
			transformedItem.onClick = (event) => {
				event.preventDefault();

				action.call(ACTIONS, {
					itemData: item.data,
					portletNamespace,
					trashEnabled,
				});
			};
		}

		if (Array.isArray(item.items)) {
			transformedItem.items = item.items.map(bindAction);
		}

		return transformedItem;
	};

	return {
		...props,
		actions: actions?.map(bindAction),
		items: items?.map(bindAction),
	};
}
