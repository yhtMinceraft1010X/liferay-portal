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

package com.liferay.client.extension.web.internal.frontend.data.set.provider;

import com.liferay.client.extension.web.internal.constants.ClientExtensionAdminFDSNames;
import com.liferay.client.extension.web.internal.frontend.data.set.model.ClientExtensionFDSEntry;
import com.liferay.frontend.data.set.provider.FDSActionProvider;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true,
	property = "fds.data.provider.key=" + ClientExtensionAdminFDSNames.CLIENT_EXTENSION_ENTRIES,
	service = FDSActionProvider.class
)
public class ClientExtensionEntryFDSActionProvider
	implements FDSActionProvider {

	@Override
	public List<DropdownItem> getDropdownItems(
			long groupId, HttpServletRequest httpServletRequest, Object model)
		throws PortalException {

		ClientExtensionFDSEntry clientExtensionFDSEntry =
			(ClientExtensionFDSEntry)model;

		return DropdownItemListBuilder.add(
			dropdownItem -> _buildEditClientExtensionEntryAction(
				dropdownItem, httpServletRequest, clientExtensionFDSEntry)
		).add(
			dropdownItem -> _buildDeleteClientExtensionEntryAction(
				dropdownItem, httpServletRequest, clientExtensionFDSEntry)
		).build();
	}

	private void _buildDeleteClientExtensionEntryAction(
		DropdownItem dropdownItem, HttpServletRequest httpServletRequest,
		ClientExtensionFDSEntry clientExtensionFDSEntry) {

		dropdownItem.setHref(
			PortletURLBuilder.create(
				_getActionURL(httpServletRequest)
			).setActionName(
				"/client_extension_admin/delete_client_extension_entry"
			).setParameter(
				"clientExtensionEntryId",
				clientExtensionFDSEntry.getClientExtensionEntryId()
			).buildString());

		dropdownItem.setIcon("times-circle");
		dropdownItem.setLabel(_getMessage(httpServletRequest, "delete"));
	}

	private void _buildEditClientExtensionEntryAction(
		DropdownItem dropdownItem, HttpServletRequest httpServletRequest,
		ClientExtensionFDSEntry clientExtensionFDSEntry) {

		PortletURL editClientExtensionEntryURL = PortletURLBuilder.create(
			_getRenderURL(httpServletRequest)
		).setMVCRenderCommandName(
			"/client_extension_admin/edit_client_extension_entry"
		).setParameter(
			"clientExtensionEntryId",
			clientExtensionFDSEntry.getClientExtensionEntryId()
		).buildPortletURL();

		String currentURL = ParamUtil.getString(
			httpServletRequest, "currentURL",
			_portal.getCurrentURL(httpServletRequest));

		editClientExtensionEntryURL.setParameter("redirect", currentURL);

		dropdownItem.setHref(editClientExtensionEntryURL);
		dropdownItem.setLabel(_getMessage(httpServletRequest, "edit"));
	}

	private PortletURL _getActionURL(HttpServletRequest httpServletRequest) {
		String portletId = _getPortletId(httpServletRequest);

		RequestBackedPortletURLFactory requestBackedPortletURLFactory =
			RequestBackedPortletURLFactoryUtil.create(httpServletRequest);

		return requestBackedPortletURLFactory.createActionURL(portletId);
	}

	private String _getMessage(
		HttpServletRequest httpServletRequest, String key) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", themeDisplay.getLocale(), getClass());

		return LanguageUtil.get(resourceBundle, key);
	}

	private String _getPortletId(HttpServletRequest httpServletRequest) {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		return portletDisplay.getId();
	}

	private PortletURL _getRenderURL(HttpServletRequest httpServletRequest) {
		RequestBackedPortletURLFactory requestBackedPortletURLFactory =
			RequestBackedPortletURLFactoryUtil.create(httpServletRequest);

		return requestBackedPortletURLFactory.createRenderURL(
			_getPortletId(httpServletRequest));
	}

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

}