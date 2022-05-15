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

package com.liferay.fragment.entry.processor.drop.zone.listener;

import com.liferay.fragment.constants.FragmentEntryLinkConstants;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.processor.DefaultFragmentEntryProcessorContext;
import com.liferay.fragment.processor.FragmentEntryProcessorRegistry;
import com.liferay.layout.content.page.editor.listener.ContentPageEditorListener;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.util.structure.DeletedLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.Validator;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(service = ContentPageEditorListener.class)
public class DropZoneContentPageEditorListener
	implements ContentPageEditorListener {

	@Override
	public void onAddFragmentEntryLink(FragmentEntryLink fragmentEntryLink) {
		try {
			_updateLayoutPageTemplateStructure(fragmentEntryLink);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Unable to update layout page template structure",
					exception);
			}
		}
	}

	@Override
	public void onDeleteFragmentEntryLink(FragmentEntryLink fragmentEntryLink) {
	}

	@Override
	public void onUpdateFragmentEntryLink(FragmentEntryLink fragmentEntryLink) {
	}

	@Override
	public void onUpdateFragmentEntryLinkConfigurationValues(
		FragmentEntryLink fragmentEntryLink) {

		try {
			_updateLayoutPageTemplateStructure(fragmentEntryLink);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Unable to update layout page template structure",
					exception);
			}
		}
	}

	private void _addOrRestoreDropZoneLayoutStructureItem(
		LayoutStructure layoutStructure,
		LayoutStructureItem parentLayoutStructureItem) {

		LayoutStructureItem existingLayoutStructureItem = null;

		List<DeletedLayoutStructureItem> deletedLayoutStructureItems =
			layoutStructure.getDeletedLayoutStructureItems();

		for (DeletedLayoutStructureItem deletedLayoutStructureItem :
				deletedLayoutStructureItems) {

			LayoutStructureItem layoutStructureItem =
				layoutStructure.getLayoutStructureItem(
					deletedLayoutStructureItem.getItemId());

			if (Objects.equals(
					layoutStructureItem.getParentItemId(),
					parentLayoutStructureItem.getItemId())) {

				existingLayoutStructureItem = layoutStructureItem;

				break;
			}
		}

		if (existingLayoutStructureItem != null) {
			layoutStructure.unmarkLayoutStructureItemForDeletion(
				existingLayoutStructureItem.getItemId());
		}
		else {
			layoutStructure.addFragmentDropZoneLayoutStructureItem(
				parentLayoutStructureItem.getItemId(), -1);
		}
	}

	private Document _getDocument(String html) {
		Document document = Jsoup.parseBodyFragment(html);

		Document.OutputSettings outputSettings = new Document.OutputSettings();

		outputSettings.prettyPrint(false);

		document.outputSettings(outputSettings);

		return document;
	}

	private LayoutStructure _getLayoutStructure(
		FragmentEntryLink fragmentEntryLink) {

		LayoutPageTemplateStructure layoutPageTemplateStructure =
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(
					fragmentEntryLink.getGroupId(),
					fragmentEntryLink.getPlid());

		if (layoutPageTemplateStructure == null) {
			return null;
		}

		String data = layoutPageTemplateStructure.getData(
			fragmentEntryLink.getSegmentsExperienceId());

		if (Validator.isNull(data)) {
			return null;
		}

		return LayoutStructure.of(data);
	}

	private void _updateLayoutPageTemplateStructure(
			FragmentEntryLink fragmentEntryLink)
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		String processedHTML =
			_fragmentEntryProcessorRegistry.processFragmentEntryLinkHTML(
				fragmentEntryLink,
				new DefaultFragmentEntryProcessorContext(
					serviceContext.getRequest(), serviceContext.getResponse(),
					FragmentEntryLinkConstants.EDIT,
					serviceContext.getLocale()));

		Document document = _getDocument(processedHTML);

		Elements elements = document.select("lfr-drop-zone");

		if (elements.size() <= 0) {
			return;
		}

		LayoutStructure layoutStructure = _getLayoutStructure(
			fragmentEntryLink);

		if (layoutStructure == null) {
			return;
		}

		LayoutStructureItem parentLayoutStructureItem =
			layoutStructure.getLayoutStructureItemByFragmentEntryLinkId(
				fragmentEntryLink.getFragmentEntryLinkId());

		if (parentLayoutStructureItem == null) {
			return;
		}

		List<String> childrenItemIds =
			parentLayoutStructureItem.getChildrenItemIds();

		if (childrenItemIds.size() == elements.size()) {
			return;
		}

		if (childrenItemIds.size() > elements.size()) {
			List<String> childrenItemIdsToRemove = childrenItemIds.subList(
				elements.size(), childrenItemIds.size());

			childrenItemIdsToRemove.forEach(
				itemId -> layoutStructure.markLayoutStructureItemForDeletion(
					itemId, Collections.emptyList()));
		}
		else {
			for (int i = childrenItemIds.size(); i < elements.size(); i++) {
				_addOrRestoreDropZoneLayoutStructureItem(
					layoutStructure, parentLayoutStructureItem);
			}
		}

		_layoutPageTemplateStructureLocalService.
			updateLayoutPageTemplateStructureData(
				fragmentEntryLink.getGroupId(), fragmentEntryLink.getPlid(),
				fragmentEntryLink.getSegmentsExperienceId(),
				layoutStructure.toString());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DropZoneContentPageEditorListener.class);

	@Reference
	private FragmentEntryProcessorRegistry _fragmentEntryProcessorRegistry;

	@Reference
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

}