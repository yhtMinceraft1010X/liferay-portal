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

package com.liferay.client.extension.web.internal.portlet.action;

import com.liferay.client.extension.constants.ClientExtensionConstants;
import com.liferay.client.extension.model.ClientExtensionEntry;
import com.liferay.client.extension.service.ClientExtensionEntryService;
import com.liferay.client.extension.web.internal.constants.ClientExtensionAdminPortletKeys;
import com.liferay.client.extension.web.internal.constants.ClientExtensionAdminWebKeys;
import com.liferay.client.extension.web.internal.display.context.EditClientExtensionEntryDisplayContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ClientExtensionAdminPortletKeys.CLIENT_EXTENSION_ADMIN,
		"mvc.command.name=/client_extension_admin/edit_client_extension_entry"
	},
	service = MVCActionCommand.class
)
public class EditClientExtensionEntryMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

			if (cmd.equals(Constants.ADD)) {
				_add(actionRequest);
			}
			else if (cmd.equals(Constants.UPDATE)) {
				_update(actionRequest);
			}

			String redirect = ParamUtil.getString(actionRequest, "redirect");

			if (Validator.isNotNull(redirect)) {
				actionResponse.sendRedirect(redirect);
			}
		}
		catch (Exception exception) {
			SessionErrors.add(actionRequest, exception.getClass());

			actionRequest.setAttribute(
				ClientExtensionAdminWebKeys.
					EDIT_CLIENT_EXTENSION_ENTRY_DISPLAY_CONTEXT,
				new EditClientExtensionEntryDisplayContext(
					actionRequest, _getClientExtensionEntry(actionRequest)));

			actionResponse.setRenderParameter(
				"mvcPath", "/admin/edit_client_extension_entry.jsp");
		}
	}

	private void _add(ActionRequest actionRequest) throws PortalException {
		String description = ParamUtil.getString(actionRequest, "description");
		String friendlyURLMapping = ParamUtil.getString(
			actionRequest, "friendlyURLMapping");
		boolean instanceable = ParamUtil.getBoolean(
			actionRequest, "instanceable");
		Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(
			actionRequest, "name");
		String portletCategoryName = ParamUtil.getString(
			actionRequest, "portletCategoryName");
		String properties = ParamUtil.getString(actionRequest, "properties");
		String sourceCodeURL = ParamUtil.getString(
			actionRequest, "sourceCodeURL");
		String type = ParamUtil.getString(actionRequest, "type");

		if (type.equals(ClientExtensionConstants.TYPE_CUSTOM_ELEMENT)) {
			_clientExtensionEntryService.addCustomElementClientExtensionEntry(
				StringPool.BLANK,
				StringUtil.merge(
					ParamUtil.getStringValues(
						actionRequest, "customElementCSSURLs"),
					StringPool.NEW_LINE),
				ParamUtil.getString(
					actionRequest, "customElementHTMLElementName"),
				StringUtil.merge(
					ParamUtil.getStringValues(
						actionRequest, "customElementURLs"),
					StringPool.NEW_LINE),
				ParamUtil.getBoolean(actionRequest, "customElementUseESM"),
				description, friendlyURLMapping, instanceable, nameMap,
				portletCategoryName, properties, sourceCodeURL);
		}
		else if (type.equals(ClientExtensionConstants.TYPE_IFRAME)) {
			_clientExtensionEntryService.addIFrameClientExtensionEntry(
				description, friendlyURLMapping,
				ParamUtil.getString(actionRequest, "iFrameURL"), instanceable,
				nameMap, portletCategoryName, properties, sourceCodeURL);
		}
	}

	private ClientExtensionEntry _getClientExtensionEntry(
			ActionRequest actionRequest)
		throws PortalException {

		long clientExtensionEntryId = ParamUtil.getLong(
			actionRequest, "clientExtensionEntryId");

		if (clientExtensionEntryId != 0) {
			return _clientExtensionEntryService.getClientExtensionEntry(
				clientExtensionEntryId);
		}

		return null;
	}

	private void _update(ActionRequest actionRequest) throws PortalException {
		ClientExtensionEntry clientExtensionEntry = _getClientExtensionEntry(
			actionRequest);

		String description = ParamUtil.getString(actionRequest, "description");
		String friendlyURLMapping = ParamUtil.getString(
			actionRequest, "friendlyURLMapping");
		Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(
			actionRequest, "name");
		String portletCategoryName = ParamUtil.getString(
			actionRequest, "portletCategoryName");
		String properties = ParamUtil.getString(actionRequest, "properties");
		String sourceCodeURL = ParamUtil.getString(
			actionRequest, "sourceCodeURL");

		if (Objects.equals(
				clientExtensionEntry.getType(),
				ClientExtensionConstants.TYPE_CUSTOM_ELEMENT)) {

			_clientExtensionEntryService.
				updateCustomElementClientExtensionEntry(
					clientExtensionEntry.getClientExtensionEntryId(),
					StringUtil.merge(
						ParamUtil.getStringValues(
							actionRequest, "customElementCSSURLs"),
						StringPool.NEW_LINE),
					ParamUtil.getString(
						actionRequest, "customElementHTMLElementName"),
					StringUtil.merge(
						ParamUtil.getStringValues(
							actionRequest, "customElementURLs"),
						StringPool.NEW_LINE),
					ParamUtil.getBoolean(actionRequest, "customElementUseESM"),
					description, friendlyURLMapping, nameMap,
					portletCategoryName, properties, sourceCodeURL);
		}
		else if (Objects.equals(
					clientExtensionEntry.getType(),
					ClientExtensionConstants.TYPE_IFRAME)) {

			_clientExtensionEntryService.updateIFrameClientExtensionEntry(
				clientExtensionEntry.getClientExtensionEntryId(), description,
				friendlyURLMapping,
				ParamUtil.getString(actionRequest, "iFrameURL"), nameMap,
				portletCategoryName, properties, sourceCodeURL);
		}
	}

	@Reference
	private ClientExtensionEntryService _clientExtensionEntryService;

}