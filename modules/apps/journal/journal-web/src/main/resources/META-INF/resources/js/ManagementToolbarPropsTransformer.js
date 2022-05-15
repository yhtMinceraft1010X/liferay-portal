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

import {addParams, navigate, openSelectionModal} from 'frontend-js-web';

import openDeleteArticleModal from './modals/openDeleteArticleModal';

export default function propsTransformer({
	additionalProps: {
		addArticleURL,
		exportTranslationURL,
		moveArticlesAndFoldersURL,
		openViewMoreStructuresURL,
		selectEntityURL,
		trashEnabled,
		viewDDMStructureArticlesURL,
	},
	portletNamespace,
	...otherProps
}) {
	const deleteEntries = () => {
		if (trashEnabled) {
			Liferay.fire(`${portletNamespace}editEntry`, {
				action: '/journal/move_articles_and_folders_to_trash',
			});

			return;
		}

		openDeleteArticleModal({
			onDelete: () => {
				Liferay.fire(`${portletNamespace}editEntry`, {
					action: '/journal/delete_articles_and_folders',
				});
			},
		});
	};

	const expireEntries = () => {
		Liferay.fire(`${portletNamespace}editEntry`, {
			action: '/journal/expire_articles_and_folders',
		});
	};

	const exportTranslation = () => {
		const url = new URL(exportTranslationURL);

		const urlSearchParams = new URLSearchParams(url.search);

		const paramName = `_${urlSearchParams.get('p_p_id')}_key`;

		const searchContainer = Liferay.SearchContainer.get(
			`${portletNamespace}articles`
		);

		const keys = searchContainer.select
			.getAllSelectedElements()
			.get('value');

		for (const key of keys) {
			url.searchParams.append(paramName, key);
		}

		navigate(url.toString());
	};

	const moveEntries = () => {
		let entrySelectorNodes = document.querySelectorAll('.entry-selector');

		if (entrySelectorNodes.length === 0) {
			entrySelectorNodes = document.querySelectorAll(
				'.card-page-item input[type="checkbox"]'
			);
		}

		entrySelectorNodes.forEach((node) => {
			if (node.checked) {
				moveArticlesAndFoldersURL = addParams(
					`${node.name}=${node.value}`,
					moveArticlesAndFoldersURL
				);
			}
		});

		navigate(moveArticlesAndFoldersURL);
	};

	return {
		...otherProps,
		onActionButtonClick(event, {item}) {
			const action = item?.data?.action;

			if (action === 'deleteEntries') {
				deleteEntries();
			}
			else if (action === 'expireEntries') {
				expireEntries();
			}
			else if (action === 'exportTranslation') {
				exportTranslation();
			}
			else if (action === 'moveEntries') {
				moveEntries();
			}
		},
		onFilterDropdownItemClick(event, {item}) {
			if (item?.data?.action === 'openDDMStructuresSelector') {
				openSelectionModal({
					onSelect: (selectedItem) => {
						navigate(
							addParams(
								{
									[`${portletNamespace}ddmStructureKey`]: selectedItem.ddmstructurekey,
								},
								viewDDMStructureArticlesURL
							)
						);
					},
					selectEventName: `${portletNamespace}selectDDMStructure`,
					title: Liferay.Language.get('structures'),
					url: selectEntityURL,
				});
			}
		},
		onShowMoreButtonClick() {
			let refreshOnClose = true;

			openSelectionModal({
				onClose: () => {
					if (refreshOnClose) {
						navigate(location.href);
					}
				},
				onSelect: (selectedItem) => {
					if (selectedItem) {
						refreshOnClose = false;

						navigate(
							addParams(
								{
									[`${portletNamespace}ddmStructureKey`]: selectedItem.ddmstructurekey,
								},
								addArticleURL
							)
						);
					}
				},
				selectEventName: `${portletNamespace}selectAddMenuItem`,
				title: Liferay.Language.get('more'),
				url: openViewMoreStructuresURL,
			});
		},
	};
}
