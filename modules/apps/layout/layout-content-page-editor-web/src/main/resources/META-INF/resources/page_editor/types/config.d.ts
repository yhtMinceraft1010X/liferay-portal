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

import {LayoutTypes} from './LayoutTypes';

export interface Config {
	addFragmentCompositionURL: string;
	addFragmentEntryLinkCommentURL: string;
	addFragmentEntryLinkURL: string;
	addFragmentEntryLinksURL: string;
	addItemURL: string;
	addPortletURL: string;
	addSegmentsExperienceURL: string;

	assetCategoryTreeNodeItemSelectorURL: string;

	autoExtendSessionEnabled: boolean;

	availableLanguages: {
		[key: string]: {
			languageIcon: string;
			languageLabel: string;
		};
	};

	availableSegmentsEntries: {
		[key: string]: {
			name: string;
			segmentsEntryId: string;
		};
	};

	availableViewportSizes: {
		[key: string]: {
			icon: string;
			label: string;
			maxWidth: string;
			minWidth: string;
			sizeId: string;
		};
	};

	changeMasterLayoutURL: string;
	changeStyleBookEntryURL: string;
	collectionSelectorURL: string;

	commonStyles: Array<{
		label: string;
		styles: Array<{
			dataType: string;
			defaultValue: string | object;
			dependencies: Array<{
				styleName: string;
				value: string | object;
			}>;
			label: string;
			name: string;
			responsive: boolean;
			type: string;
			validValues: Array<{
				label: string;
				value: string | object;
			}>;
		}>;
	}>;

	commonStylesFields: {
		[key: string]: {
			cssTemplate: string;
			defaultValue: string;
		};
	};

	createLayoutPageTemplateEntryURL: string;

	defaultEditorConfigurations: Record<
		'comment' | 'rich-text' | 'text',
		{
			editorConfig: object;
			editorOptions: object;
		}
	>;

	defaultLanguageId: string;
	defaultSegmentsEntryId: string;
	defaultSegmentsExperienceId: string;
	defaultStyleBookEntryImagePreviewURL: string;
	defaultStyleBookEntryName: string;
	deleteFragmentEntryLinkCommentURL: string;
	deleteSegmentsExperienceURL: string;
	discardDraftURL: string;
	draft: boolean;
	duplicateItemURL: string;
	duplicateSegmentsExperienceURL: string;
	editFragmentEntryLinkCommentURL: string;
	editFragmentEntryLinkURL: string;
	editSegmentsEntryURL: string;
	frontendTokens: {
		getAvailableImageConfigurationsURL: string;
		getAvailableListItemRenderersURL: string;
		getAvailableListRenderersURL: string;
		[key: string]: {
			cssVariable: string;
			editorType: string;
			label: string;
			name: string;
			value: string;
		};
	};
	getAvailableTemplatesURL: string;
	getCollectionConfigurationURL: string;
	getCollectionFieldURL: string;
	getCollectionFiltersURL: string;
	getCollectionItemCountURL: string;
	getCollectionMappingFieldsURL: string;
	getCollectionSupportedFiltersURL: string;
	getExperienceUsedPortletsURL: string;
	getIframeContentCssURL: string;
	getIframeContentURL: string;
	getInfoItemFieldValueURL: string;
	getInfoItemMappingFieldsURL: string;
	getLayoutFriendlyURL: string;
	getLayoutPageTemplateCollectionsURL: string;
	getPageContentsURL: string;
	getWidgetsURL: string;
	imageSelectorURL: string;
	infoItemPreviewSelectorURL: string;
	infoItemSelectorURL: string;
	isConversionDraft: boolean;
	isPrivateLayoutsEnabled: boolean;
	layoutConversionWarningMessages: string[] | null;
	layoutItemSelectorURL: String;
	layoutType: LayoutTypes[keyof LayoutTypes];
	lookAndFeelURL: string;
	mappingFieldsURL: string;
	markItemForDeletionURL: string;
	masterLayouts: Array<{
		imagePreviewURL: string;
		masterLayoutPlid: string;
		name: string;
	}>;
	masterUsed: boolean;
	moveItemURL: string;
	paddingOptions: Array<{
		label: string;
		value: string;
	}>;
	panels: string[][];
	pending: boolean;
	plid: string;
	pluginsRootPath: string;
	portletNamespace: string;
	previewPageURL: string;
	publishURL: string;
	redirectURL: string;
	renderFragmentEntryURL: string;
	restoreCollectionDisplayConfigURL: string;
	searchContainerPageMaxDelta: number;

	selectedMappingTypes?: {
		type: {
			id: string;
			label: string;
		};
		subtype: {
			id: string;
			label: string;
		};
	};

	selectedSegmentsEntryId: string;

	sidebarPanels: {
		[key: string]: {
			icon: string;
			isLink: boolean;
			label: string;
			pluginEntryPoint?: string;
			sidebarPanelId: string;
			url?: string | null;
		};
	};

	singleSegmentsExperienceMode: boolean;
	siteNavigationMenuItemSelectorURL: string;
	styleBookEnabled: boolean;
	styleBooks: Array<{
		imagePreviewURL: string;
		name: string;
		styleBookEntryId: string;
	}>;
	stylebookEntryId: string;
	themeColorCssClasses: string[];
	toolbarId: string;

	toolbarPlugins: Array<{
		loadingPlaceholder: string;
		pluginEntryPoint: string;
		toolbarPluginId: string;
	}>;

	unmarkItemForDeletionURL: string;
	updateCollectionDisplayConfigURL: string;
	updateConfigurationValuesURL: string;
	updateItemConfigURL: string;
	updateLayoutPageTemplateDataURL: string;
	updateRowColumnsURL: string;
	updateSegmentsExperiencePriorityURL: string;
	updateSegmentsExperienceURL: string;
	videoItemSelectorURL: string;
	workflowEnabled: boolean;
}
