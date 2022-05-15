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

package com.liferay.document.library.web.internal.portlet.configuration.icon;

import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.web.internal.helper.DLTrashHelper;
import com.liferay.document.library.web.internal.portlet.action.ActionUtil;
import com.liferay.document.library.web.internal.util.DLFolderUtil;
import com.liferay.document.library.web.internal.util.DLPortletConfigurationIconUtil;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.configuration.icon.BasePortletConfigurationIcon;
import com.liferay.portal.kernel.portlet.configuration.icon.PortletConfigurationIcon;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Roberto Díaz
 */
@Component(
	property = {
		"javax.portlet.name=" + DLPortletKeys.DOCUMENT_LIBRARY_ADMIN,
		"path=/document_library/view_folder"
	},
	service = PortletConfigurationIcon.class
)
public class DeleteFolderPortletConfigurationIcon
	extends BasePortletConfigurationIcon {

	@Override
	public String getMessage(PortletRequest portletRequest) {
		return LanguageUtil.get(
			getResourceBundle(getLocale(portletRequest)), "delete");
	}

	@Override
	public String getURL(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		try {
			PortletURL portletURL = _portal.getControlPanelPortletURL(
				portletRequest, DLPortletKeys.DOCUMENT_LIBRARY_ADMIN,
				PortletRequest.ACTION_PHASE);

			Folder folder = ActionUtil.getFolder(portletRequest);

			if (DLFolderUtil.isRepositoryRoot(folder)) {
				portletURL.setParameter(
					ActionRequest.ACTION_NAME,
					"/document_library/edit_repository");
			}
			else {
				portletURL.setParameter(
					ActionRequest.ACTION_NAME, "/document_library/edit_folder");
			}

			ThemeDisplay themeDisplay =
				(ThemeDisplay)portletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			if (DLFolderUtil.isRepositoryRoot(folder) ||
				!_dlTrashHelper.isTrashEnabled(
					themeDisplay.getScopeGroupId(), folder.getRepositoryId())) {

				portletURL.setParameter(Constants.CMD, Constants.DELETE);
			}
			else {
				portletURL.setParameter(Constants.CMD, Constants.MOVE_TO_TRASH);
			}

			PortletURL redirectURL = _portal.getControlPanelPortletURL(
				portletRequest, DLPortletKeys.DOCUMENT_LIBRARY_ADMIN,
				PortletRequest.RENDER_PHASE);

			long parentFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;

			if (!folder.isRoot()) {
				parentFolderId = folder.getParentFolderId();
			}

			if (parentFolderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
				redirectURL.setParameter(
					"mvcRenderCommandName", "/document_library/view");
			}
			else {
				redirectURL.setParameter(
					"mvcRenderCommandName", "/document_library/view_folder");
			}

			redirectURL.setParameter(
				"folderId", String.valueOf(parentFolderId));

			portletURL.setParameter("redirect", redirectURL.toString());

			if (DLFolderUtil.isRepositoryRoot(folder)) {
				portletURL.setParameter(
					"repositoryId", String.valueOf(folder.getRepositoryId()));
			}
			else {
				portletURL.setParameter(
					"folderId", String.valueOf(folder.getFolderId()));
			}

			return portletURL.toString();
		}
		catch (PortalException portalException) {
			return ReflectionUtil.throwException(portalException);
		}
	}

	@Override
	public double getWeight() {
		return 120;
	}

	@Override
	public boolean isShow(PortletRequest portletRequest) {
		return DLPortletConfigurationIconUtil.runWithDefaultValueOnError(
			false,
			() -> {
				ThemeDisplay themeDisplay =
					(ThemeDisplay)portletRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				Folder folder = ActionUtil.getFolder(portletRequest);

				return ModelResourcePermissionUtil.contains(
					_folderModelResourcePermission,
					themeDisplay.getPermissionChecker(),
					themeDisplay.getScopeGroupId(), folder.getFolderId(),
					ActionKeys.DELETE);
			});
	}

	@Override
	public boolean isToolTip() {
		return false;
	}

	@Reference
	private DLTrashHelper _dlTrashHelper;

	@Reference(
		target = "(model.class.name=com.liferay.portal.kernel.repository.model.Folder)"
	)
	private ModelResourcePermission<Folder> _folderModelResourcePermission;

	@Reference
	private Portal _portal;

}