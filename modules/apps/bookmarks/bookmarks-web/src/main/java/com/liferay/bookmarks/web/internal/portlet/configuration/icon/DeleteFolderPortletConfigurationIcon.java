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

package com.liferay.bookmarks.web.internal.portlet.configuration.icon;

import com.liferay.bookmarks.constants.BookmarksFolderConstants;
import com.liferay.bookmarks.constants.BookmarksPortletKeys;
import com.liferay.bookmarks.model.BookmarksFolder;
import com.liferay.bookmarks.web.internal.portlet.action.ActionUtil;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.configuration.icon.BasePortletConfigurationIcon;
import com.liferay.portal.kernel.portlet.configuration.icon.PortletConfigurationIcon;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.trash.TrashHelper;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + BookmarksPortletKeys.BOOKMARKS_ADMIN,
		"path=/bookmarks/view_folder"
	},
	service = PortletConfigurationIcon.class
)
public class DeleteFolderPortletConfigurationIcon
	extends BasePortletConfigurationIcon {

	@Override
	public String getMessage(PortletRequest portletRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return LanguageUtil.get(
			getResourceBundle(themeDisplay.getLocale()), "delete");
	}

	@Override
	public String getURL(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		PortletURL deleteURL = PortletURLBuilder.create(
			_portal.getControlPanelPortletURL(
				portletRequest, BookmarksPortletKeys.BOOKMARKS_ADMIN,
				PortletRequest.ACTION_PHASE)
		).setActionName(
			"/bookmarks/edit_folder"
		).setCMD(
			() -> {
				ThemeDisplay themeDisplay =
					(ThemeDisplay)portletRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				String cmd = Constants.DELETE;

				if (_isTrashEnabled(themeDisplay.getScopeGroupId())) {
					cmd = Constants.MOVE_TO_TRASH;
				}

				return cmd;
			}
		).buildPortletURL();

		BookmarksFolder folder = null;

		try {
			folder = ActionUtil.getFolder(portletRequest);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return null;
		}

		PortletURL parentFolderURL = _portal.getControlPanelPortletURL(
			portletRequest, BookmarksPortletKeys.BOOKMARKS_ADMIN,
			PortletRequest.RENDER_PHASE);

		long parentFolderId = folder.getParentFolderId();

		if (parentFolderId ==
				BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID) {

			parentFolderURL.setParameter(
				"mvcRenderCommandName", "/bookmarks/view");
		}
		else {
			parentFolderURL.setParameter(
				"mvcRenderCommandName", "/bookmarks/view_folder");
			parentFolderURL.setParameter(
				"folderId", String.valueOf(parentFolderId));
		}

		deleteURL.setParameter("redirect", parentFolderURL.toString());

		deleteURL.setParameter(
			"folderId", String.valueOf(folder.getFolderId()));

		return deleteURL.toString();
	}

	@Override
	public double getWeight() {
		return 100;
	}

	@Override
	public boolean isShow(PortletRequest portletRequest) {
		try {
			BookmarksFolder folder = ActionUtil.getFolder(portletRequest);

			if (folder == null) {
				return false;
			}

			ThemeDisplay themeDisplay =
				(ThemeDisplay)portletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			if (_bookmarksFolderModelResourcePermission.contains(
					themeDisplay.getPermissionChecker(), folder,
					ActionKeys.DELETE)) {

				return true;
			}
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return false;
	}

	private boolean _isTrashEnabled(long groupId) {
		try {
			if (_trashHelper.isTrashEnabled(groupId)) {
				return true;
			}
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DeleteFolderPortletConfigurationIcon.class);

	@Reference(
		target = "(model.class.name=com.liferay.bookmarks.model.BookmarksFolder)"
	)
	private ModelResourcePermission<BookmarksFolder>
		_bookmarksFolderModelResourcePermission;

	@Reference
	private Portal _portal;

	@Reference
	private TrashHelper _trashHelper;

}