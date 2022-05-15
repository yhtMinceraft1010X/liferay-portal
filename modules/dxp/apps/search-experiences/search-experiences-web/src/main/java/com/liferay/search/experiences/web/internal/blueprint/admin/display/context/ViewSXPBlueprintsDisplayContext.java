/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.web.internal.blueprint.admin.display.context;

import com.liferay.frontend.data.set.model.FDSActionDropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.search.experiences.constants.SXPActionKeys;
import com.liferay.search.experiences.model.SXPBlueprint;
import com.liferay.search.experiences.web.internal.display.context.helper.SXPRequestHelper;

import java.util.Arrays;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kevin Tan
 */
public class ViewSXPBlueprintsDisplayContext {

	public ViewSXPBlueprintsDisplayContext(
		HttpServletRequest httpServletRequest,
		ModelResourcePermission<SXPBlueprint>
			sxpBlueprintModelResourcePermission) {

		_sxpBlueprintModelResourcePermission =
			sxpBlueprintModelResourcePermission;

		_sxpRequestHelper = new SXPRequestHelper(httpServletRequest);
	}

	public String getAPIURL() {
		return "/o/search-experiences-rest/v1.0/sxp-blueprints";
	}

	public List<DropdownItem> getBulkActionDropdownItems() throws Exception {
		return Arrays.asList(
			new FDSActionDropdownItem(
				PortletURLBuilder.createActionURL(
					_sxpRequestHelper.getLiferayPortletResponse()
				).setActionName(
					"/sxp_blueprint_admin/edit_sxp_blueprint"
				).setCMD(
					Constants.DELETE
				).buildString(),
				"trash", "delete",
				LanguageUtil.get(_sxpRequestHelper.getRequest(), "delete"),
				"delete", "delete", null));
	}

	public CreationMenu getCreationMenu() throws Exception {
		CreationMenu creationMenu = new CreationMenu();

		if (!_hasAddSXPBlueprintPermission()) {
			return creationMenu;
		}

		creationMenu.addDropdownItem(
			dropdownItem -> {
				dropdownItem.setHref("addSXPBlueprint");
				dropdownItem.setLabel(
					LanguageUtil.get(
						_sxpRequestHelper.getRequest(), "add-blueprint"));
				dropdownItem.setTarget("event");
			});

		return creationMenu;
	}

	public List<FDSActionDropdownItem> getFDSActionDropdownItems()
		throws Exception {

		return Arrays.asList(
			new FDSActionDropdownItem(
				PortletURLBuilder.create(
					getPortletURL()
				).setMVCRenderCommandName(
					"/sxp_blueprint_admin/edit_sxp_blueprint"
				).setParameter(
					"sxpBlueprintId", "{id}"
				).buildString(),
				"pencil", "edit",
				LanguageUtil.get(_sxpRequestHelper.getRequest(), "edit"), "get",
				"get", null),
			new FDSActionDropdownItem(
				getAPIURL() + "/{id}/copy", "copy", "copy",
				LanguageUtil.get(_sxpRequestHelper.getRequest(), "copy"),
				"post", "create", "async"),
			new FDSActionDropdownItem(
				"#", "export", "export",
				LanguageUtil.get(_sxpRequestHelper.getRequest(), "export"),
				null, "get", null),
			new FDSActionDropdownItem(
				LanguageUtil.get(
					_sxpRequestHelper.getRequest(),
					"are-you-sure-you-want-to-delete-this-entry"),
				getAPIURL() + "/{id}", "trash", "delete",
				LanguageUtil.get(_sxpRequestHelper.getRequest(), "delete"),
				"delete", "delete", "async"));
	}

	public PortletURL getPortletURL() throws PortletException {
		return PortletURLUtil.clone(
			PortletURLUtil.getCurrent(
				_sxpRequestHelper.getLiferayPortletRequest(),
				_sxpRequestHelper.getLiferayPortletResponse()),
			_sxpRequestHelper.getLiferayPortletResponse());
	}

	private boolean _hasAddSXPBlueprintPermission() {
		PortletResourcePermission portletResourcePermission =
			_sxpBlueprintModelResourcePermission.getPortletResourcePermission();

		return portletResourcePermission.contains(
			_sxpRequestHelper.getPermissionChecker(), null,
			SXPActionKeys.ADD_SXP_BLUEPRINT);
	}

	private final ModelResourcePermission<SXPBlueprint>
		_sxpBlueprintModelResourcePermission;
	private final SXPRequestHelper _sxpRequestHelper;

}