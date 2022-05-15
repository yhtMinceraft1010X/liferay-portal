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

package com.liferay.journal.web.internal.servlet.taglib.util;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.web.internal.security.permission.resource.DDMStructurePermission;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.security.PermissionsURLTag;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Víctor Galán
 */
public class DDMStructureActionDropdownItemsProvider {

	public DDMStructureActionDropdownItemsProvider(
		DDMStructure ddmStructure, LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		_ddmStructure = ddmStructure;
		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;

		_themeDisplay = (ThemeDisplay)liferayPortletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		_httpServletRequest = PortalUtil.getHttpServletRequest(
			liferayPortletRequest);
	}

	public List<DropdownItem> getActionDropdownItems() throws Exception {
		Group scopeGroup = _themeDisplay.getScopeGroup();

		boolean hasUpdatePermission = DDMStructurePermission.contains(
			_themeDisplay.getPermissionChecker(), _ddmStructure,
			ActionKeys.UPDATE);

		return DropdownItemListBuilder.addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					DropdownItemListBuilder.add(
						() -> hasUpdatePermission,
						_getEditActionUnsafeConsumer()
					).add(
						() -> hasUpdatePermission,
						_getEditDefaultValuesActionUnsafeConsumer()
					).build());
				dropdownGroupItem.setSeparator(true);
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					DropdownItemListBuilder.add(
						() -> DDMStructurePermission.contains(
							_themeDisplay.getPermissionChecker(), _ddmStructure,
							ActionKeys.VIEW),
						_getManageTemplatesActionUnsafeConsumer()
					).build());
				dropdownGroupItem.setSeparator(true);
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					DropdownItemListBuilder.add(
						() ->
							(!scopeGroup.hasLocalOrRemoteStagingGroup() ||
							 scopeGroup.isStagingGroup()) &&
							DDMStructurePermission.
								containsAddDDMStructurePermission(
									_themeDisplay.getPermissionChecker(),
									scopeGroup.getGroupId(),
									_ddmStructure.getClassNameId()),
						_getCopyDDMStructureActionUnsafeConsumer()
					).add(
						_getExportAsJSONActionUnsafeConsumer()
					).build());
				dropdownGroupItem.setSeparator(true);
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					DropdownItemListBuilder.add(
						() -> DDMStructurePermission.contains(
							_themeDisplay.getPermissionChecker(), _ddmStructure,
							ActionKeys.PERMISSIONS),
						_getPermissionsActionUnsafeConsumer()
					).build());
				dropdownGroupItem.setSeparator(true);
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					DropdownItemListBuilder.add(
						() -> DDMStructurePermission.contains(
							_themeDisplay.getPermissionChecker(), _ddmStructure,
							ActionKeys.DELETE),
						_getDeleteActionUnsafeConsumer()
					).build());
				dropdownGroupItem.setSeparator(true);
			}
		).build();
	}

	private UnsafeConsumer<DropdownItem, Exception>
		_getCopyDDMStructureActionUnsafeConsumer() {

		return dropdownItem -> {
			dropdownItem.setHref(
				PortletURLBuilder.createRenderURL(
					_liferayPortletResponse
				).setMVCPath(
					"/copy_data_definition.jsp"
				).setRedirect(
					_themeDisplay.getURLCurrent()
				).setParameter(
					"ddmStructureId", _ddmStructure.getStructureId()
				).buildString());
			dropdownItem.setIcon("copy");
			dropdownItem.setLabel(
				LanguageUtil.get(_httpServletRequest, "copy"));
		};
	}

	private UnsafeConsumer<DropdownItem, Exception>
		_getDeleteActionUnsafeConsumer() {

		return dropdownItem -> {
			dropdownItem.putData("action", "deleteDDMStructure");
			dropdownItem.putData(
				"deleteDDMStructureURL",
				PortletURLBuilder.createActionURL(
					_liferayPortletResponse
				).setActionName(
					"/journal/delete_data_definition"
				).setMVCPath(
					"/view_ddm_structures.jsp"
				).setRedirect(
					_themeDisplay.getURLCurrent()
				).setParameter(
					"dataDefinitionId", _ddmStructure.getStructureId()
				).buildString());
			dropdownItem.setIcon("trash");
			dropdownItem.setLabel(
				LanguageUtil.get(_httpServletRequest, "delete"));
		};
	}

	private UnsafeConsumer<DropdownItem, Exception>
		_getEditActionUnsafeConsumer() {

		return dropdownItem -> {
			dropdownItem.setHref(
				PortletURLBuilder.createRenderURL(
					_liferayPortletResponse
				).setMVCPath(
					"/edit_data_definition.jsp"
				).setRedirect(
					_themeDisplay.getURLCurrent()
				).setParameter(
					"ddmStructureId", _ddmStructure.getStructureId()
				).buildString());
			dropdownItem.setIcon("pencil");
			dropdownItem.setLabel(
				LanguageUtil.get(_httpServletRequest, "edit"));
		};
	}

	private UnsafeConsumer<DropdownItem, Exception>
		_getEditDefaultValuesActionUnsafeConsumer() {

		return dropdownItem -> {
			dropdownItem.setHref(
				PortletURLBuilder.createRenderURL(
					_liferayPortletResponse
				).setMVCPath(
					"/edit_article.jsp"
				).setRedirect(
					_themeDisplay.getURLCurrent()
				).setParameter(
					"classNameId", PortalUtil.getClassNameId(DDMStructure.class)
				).setParameter(
					"classPK", _ddmStructure.getStructureId()
				).setParameter(
					"ddmStructureKey", _ddmStructure.getStructureKey()
				).setParameter(
					"groupId", _ddmStructure.getGroupId()
				).buildString());
			dropdownItem.setLabel(
				LanguageUtil.get(_httpServletRequest, "edit-default-values"));
		};
	}

	private UnsafeConsumer<DropdownItem, Exception>
		_getExportAsJSONActionUnsafeConsumer() {

		return dropdownItem -> {
			LiferayPortletURL resourceURL =
				_liferayPortletResponse.createResourceURL(
					JournalPortletKeys.JOURNAL);

			resourceURL.setCopyCurrentRenderParameters(false);
			resourceURL.setResourceID("/journal/export_data_definition");
			resourceURL.setParameter(
				"dataDefinitionId",
				String.valueOf(_ddmStructure.getStructureId()));

			dropdownItem.setHref(resourceURL.toString());

			dropdownItem.setIcon("upload");
			dropdownItem.setLabel(
				LanguageUtil.get(_httpServletRequest, "export-as-json"));
		};
	}

	private UnsafeConsumer<DropdownItem, Exception>
		_getManageTemplatesActionUnsafeConsumer() {

		return dropdownItem -> {
			dropdownItem.setHref(
				PortletURLBuilder.createRenderURL(
					_liferayPortletResponse
				).setMVCPath(
					"/view_ddm_templates.jsp"
				).setRedirect(
					_themeDisplay.getURLCurrent()
				).setParameter(
					"classPK", _ddmStructure.getStructureId()
				).buildString());
			dropdownItem.setLabel(
				LanguageUtil.get(_httpServletRequest, "manage-templates"));
		};
	}

	private UnsafeConsumer<DropdownItem, Exception>
			_getPermissionsActionUnsafeConsumer()
		throws Exception {

		String permissionsURL = PermissionsURLTag.doTag(
			StringPool.BLANK,
			DDMStructurePermission.getStructureModelResourceName(
				_ddmStructure.getClassNameId()),
			HtmlUtil.escape(_ddmStructure.getName(_themeDisplay.getLocale())),
			null, String.valueOf(_ddmStructure.getStructureId()),
			LiferayWindowState.POP_UP.toString(), null, _httpServletRequest);

		return dropdownItem -> {
			dropdownItem.putData("action", "permissionsDDMStructure");
			dropdownItem.putData("permissionsDDMStructureURL", permissionsURL);
			dropdownItem.setIcon("password-policies");
			dropdownItem.setLabel(
				LanguageUtil.get(_httpServletRequest, "permissions"));
		};
	}

	private final DDMStructure _ddmStructure;
	private final HttpServletRequest _httpServletRequest;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final ThemeDisplay _themeDisplay;

}