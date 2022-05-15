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

package com.liferay.layout.content.page.editor.web.internal.portlet.action;

import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.layout.content.page.editor.web.internal.util.layout.structure.LayoutStructureUtil;
import com.liferay.layout.responsive.ViewportSize;
import com.liferay.layout.util.constants.LayoutDataItemTypeConstants;
import com.liferay.layout.util.structure.CollectionStyledLayoutStructureItem;
import com.liferay.layout.util.structure.ColumnLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.layout.util.structure.RowStyledLayoutStructureItem;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Objects;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Víctor Galán
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
		"mvc.command.name=/layout_content_page_editor/add_item"
	},
	service = MVCActionCommand.class
)
public class AddItemMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		try {
			jsonObject = _addItemToLayoutData(actionRequest);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			String errorMessage = "an-unexpected-error-occurred";

			jsonObject.put(
				"error",
				LanguageUtil.get(
					_portal.getHttpServletRequest(actionRequest),
					errorMessage));
		}

		hideDefaultSuccessMessage(actionRequest);

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonObject);
	}

	private LayoutStructureItem _addCollectionStyledLayoutStructureItem(
		LayoutStructure layoutStructure, String parentItemId, int position) {

		CollectionStyledLayoutStructureItem
			collectionStyledLayoutStructureItem =
				(CollectionStyledLayoutStructureItem)
					layoutStructure.addCollectionStyledLayoutStructureItem(
						parentItemId, position);

		collectionStyledLayoutStructureItem.setViewportConfiguration(
			ViewportSize.MOBILE_LANDSCAPE.getViewportSizeId(),
			JSONUtil.put("numberOfColumns", 1));

		return collectionStyledLayoutStructureItem;
	}

	private JSONObject _addItemToLayoutData(ActionRequest actionRequest)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long segmentsExperienceId = ParamUtil.getLong(
			actionRequest, "segmentsExperienceId");
		String itemType = ParamUtil.getString(actionRequest, "itemType");
		String parentItemId = ParamUtil.getString(
			actionRequest, "parentItemId");
		int position = ParamUtil.getInteger(actionRequest, "position");

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		JSONObject layoutDataJSONObject = null;

		if (Objects.equals(
				itemType, LayoutDataItemTypeConstants.TYPE_COLLECTION)) {

			layoutDataJSONObject =
				LayoutStructureUtil.updateLayoutPageTemplateData(
					themeDisplay.getScopeGroupId(), segmentsExperienceId,
					themeDisplay.getPlid(),
					layoutStructure -> {
						LayoutStructureItem layoutStructureItem =
							_addCollectionStyledLayoutStructureItem(
								layoutStructure, parentItemId, position);

						jsonObject.put(
							"addedItemId", layoutStructureItem.getItemId());
					});
		}
		else if (Objects.equals(
					itemType, LayoutDataItemTypeConstants.TYPE_ROW)) {

			layoutDataJSONObject =
				LayoutStructureUtil.updateLayoutPageTemplateData(
					themeDisplay.getScopeGroupId(), segmentsExperienceId,
					themeDisplay.getPlid(),
					layoutStructure -> {
						LayoutStructureItem layoutStructureItem =
							_addRowSyledLayoutStructureItem(
								layoutStructure, parentItemId, position);

						jsonObject.put(
							"addedItemId", layoutStructureItem.getItemId());
					});
		}
		else {
			layoutDataJSONObject =
				LayoutStructureUtil.updateLayoutPageTemplateData(
					themeDisplay.getScopeGroupId(), segmentsExperienceId,
					themeDisplay.getPlid(),
					layoutStructure -> {
						LayoutStructureItem layoutStructureItem =
							layoutStructure.addLayoutStructureItem(
								itemType, parentItemId, position);

						jsonObject.put(
							"addedItemId", layoutStructureItem.getItemId());
					});
		}

		return jsonObject.put("layoutData", layoutDataJSONObject);
	}

	private LayoutStructureItem _addRowSyledLayoutStructureItem(
		LayoutStructure layoutStructure, String parentItemId, int position) {

		RowStyledLayoutStructureItem rowStyledLayoutStructureItem =
			(RowStyledLayoutStructureItem)
				layoutStructure.addRowStyledLayoutStructureItem(
					parentItemId, position, _DEFAULT_ROW_COLUMNS);

		rowStyledLayoutStructureItem.setViewportConfiguration(
			ViewportSize.MOBILE_LANDSCAPE.getViewportSizeId(),
			JSONUtil.put("modulesPerRow", 1));

		for (int i = 0; i < _DEFAULT_ROW_COLUMNS; i++) {
			ColumnLayoutStructureItem columnLayoutStructureItem =
				(ColumnLayoutStructureItem)
					layoutStructure.addColumnLayoutStructureItem(
						rowStyledLayoutStructureItem.getItemId(), i);

			columnLayoutStructureItem.setViewportConfiguration(
				ViewportSize.MOBILE_LANDSCAPE.getViewportSizeId(),
				JSONUtil.put("size", 12));

			columnLayoutStructureItem.setSize(4);
		}

		return rowStyledLayoutStructureItem;
	}

	private static final int _DEFAULT_ROW_COLUMNS = 3;

	private static final Log _log = LogFactoryUtil.getLog(
		AddItemMVCActionCommand.class);

	@Reference
	private Portal _portal;

}