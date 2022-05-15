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

package com.liferay.portlet.configuration.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.SearchDisplayStyleUtil;
import com.liferay.portal.kernel.portlet.SearchOrderByUtil;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.settings.ArchivedSettings;
import com.liferay.portal.kernel.settings.SettingsFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portlet.configuration.web.internal.constants.PortletConfigurationPortletKeys;
import com.liferay.portlet.configuration.web.internal.constants.PortletConfigurationWebKeys;
import com.liferay.portlet.configuration.web.internal.servlet.taglib.util.ArchivedSettingsActionDropdownItemsProvider;
import com.liferay.portlet.configuration.web.internal.util.comparator.ArchivedSettingsModifiedDateComparator;
import com.liferay.portlet.configuration.web.internal.util.comparator.ArchivedSettingsNameComparator;

import java.util.List;
import java.util.Objects;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class PortletConfigurationTemplatesDisplayContext {

	public PortletConfigurationTemplatesDisplayContext(
		HttpServletRequest httpServletRequest, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		_httpServletRequest = httpServletRequest;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;

		_moduleName = (String)renderRequest.getAttribute(
			PortletConfigurationWebKeys.MODULE_NAME);
	}

	public List<DropdownItem> getActionDropdownItems(
		ArchivedSettings archivedSettings) {

		ArchivedSettingsActionDropdownItemsProvider
			archivedSettingsActionDropdownItemsProvider =
				new ArchivedSettingsActionDropdownItemsProvider(
					archivedSettings, _renderRequest, _renderResponse);

		return archivedSettingsActionDropdownItemsProvider.
			getActionDropdownItems();
	}

	public SearchContainer<ArchivedSettings>
		getArchivedSettingsSearchContainer() {

		if (_archivedSettingsSearch != null) {
			return _archivedSettingsSearch;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		SearchContainer<ArchivedSettings> archivedSettingsSearch =
			new SearchContainer<>(
				_renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM,
				SearchContainer.DEFAULT_DELTA, getPortletURL(), null,
				"there-are-no-configuration-templates");

		archivedSettingsSearch.setOrderByCol(getOrderByCol());

		boolean orderByAsc = false;

		if (Objects.equals(getOrderByType(), "asc")) {
			orderByAsc = true;
		}

		OrderByComparator<ArchivedSettings> orderByComparator = null;

		if (Objects.equals(getOrderByCol(), "modified-date")) {
			orderByComparator = new ArchivedSettingsModifiedDateComparator(
				orderByAsc);
		}
		else {
			orderByComparator = new ArchivedSettingsNameComparator(orderByAsc);
		}

		archivedSettingsSearch.setOrderByComparator(orderByComparator);
		archivedSettingsSearch.setOrderByType(getOrderByType());

		Portlet selPortlet = PortletLocalServiceUtil.getPortletById(
			themeDisplay.getCompanyId(), getPortletResource());

		archivedSettingsSearch.setResultsAndTotal(
			ListUtil.sort(
				SettingsFactoryUtil.getPortletInstanceArchivedSettingsList(
					themeDisplay.getScopeGroupId(),
					selPortlet.getRootPortletId()),
				archivedSettingsSearch.getOrderByComparator()));

		archivedSettingsSearch.setRowChecker(
			new EmptyOnClickRowChecker(_renderResponse));

		_archivedSettingsSearch = archivedSettingsSearch;

		return _archivedSettingsSearch;
	}

	public String getDisplayStyle() {
		if (Validator.isNotNull(_displayStyle)) {
			return _displayStyle;
		}

		_displayStyle = SearchDisplayStyleUtil.getDisplayStyle(
			_httpServletRequest,
			PortletConfigurationPortletKeys.PORTLET_CONFIGURATION, "list");

		return _displayStyle;
	}

	public String getModuleName() {
		return _moduleName;
	}

	public String getOrderByCol() {
		if (Validator.isNotNull(_orderByCol)) {
			return _orderByCol;
		}

		_orderByCol = SearchOrderByUtil.getOrderByCol(
			_httpServletRequest,
			PortletConfigurationPortletKeys.PORTLET_CONFIGURATION, "name");

		return _orderByCol;
	}

	public String getOrderByType() {
		if (Validator.isNotNull(_orderByType)) {
			return _orderByType;
		}

		_orderByType = SearchOrderByUtil.getOrderByType(
			_httpServletRequest,
			PortletConfigurationPortletKeys.PORTLET_CONFIGURATION, "asc");

		return _orderByType;
	}

	public String getPortletResource() {
		if (_portletResource != null) {
			return _portletResource;
		}

		_portletResource = ParamUtil.getString(
			_httpServletRequest, "portletResource");

		return _portletResource;
	}

	public PortletURL getPortletURL() {
		return PortletURLBuilder.createRenderURL(
			_renderResponse
		).setMVCPath(
			"/edit_configuration_templates.jsp"
		).setRedirect(
			getRedirect()
		).setPortletResource(
			getPortletResource()
		).setParameter(
			"displayStyle",
			() -> {
				String displayStyle = getDisplayStyle();

				if (Validator.isNotNull(displayStyle)) {
					return displayStyle;
				}

				return null;
			}
		).setParameter(
			"orderByCol",
			() -> {
				String orderByCol = getOrderByCol();

				if (Validator.isNotNull(orderByCol)) {
					return orderByCol;
				}

				return null;
			}
		).setParameter(
			"orderByType",
			() -> {
				String orderByType = getOrderByType();

				if (Validator.isNotNull(orderByType)) {
					return orderByType;
				}

				return null;
			}
		).setParameter(
			"returnToFullPageURL", getReturnToFullPageURL()
		).buildPortletURL();
	}

	public String getRedirect() {
		if (_redirect != null) {
			return _redirect;
		}

		_redirect = ParamUtil.getString(_httpServletRequest, "redirect");

		return _redirect;
	}

	public String getReturnToFullPageURL() {
		if (_returnToFullPageURL != null) {
			return _returnToFullPageURL;
		}

		_returnToFullPageURL = ParamUtil.getString(
			_httpServletRequest, "returnToFullPageURL");

		return _returnToFullPageURL;
	}

	private SearchContainer<ArchivedSettings> _archivedSettingsSearch;
	private String _displayStyle;
	private final HttpServletRequest _httpServletRequest;
	private final String _moduleName;
	private String _orderByCol;
	private String _orderByType;
	private String _portletResource;
	private String _redirect;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private String _returnToFullPageURL;

}