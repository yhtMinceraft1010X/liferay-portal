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

package com.liferay.journal.web.internal.display.context;

import com.liferay.depot.util.SiteConnectedGroupGroupProviderUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.util.comparator.StructureModifiedDateComparator;
import com.liferay.dynamic.data.mapping.util.comparator.StructureNameComparator;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemListBuilder;
import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.service.JournalFolderServiceUtil;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.SearchOrderByUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class JournalViewMoreMenuItemsDisplayContext {

	public JournalViewMoreMenuItemsDisplayContext(
		RenderRequest renderRequest, RenderResponse renderResponse,
		long folderId, int restrictionType) {

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_folderId = folderId;
		_restrictionType = restrictionType;

		_httpServletRequest = PortalUtil.getHttpServletRequest(_renderRequest);

		_themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public List<DDMStructure> getDDMStructures() throws PortalException {
		if (ListUtil.isNotEmpty(_ddmStructures)) {
			return _ddmStructures;
		}

		if (Validator.isNull(_getKeywords())) {
			_ddmStructures = JournalFolderServiceUtil.getDDMStructures(
				SiteConnectedGroupGroupProviderUtil.
					getCurrentAndAncestorSiteAndDepotGroupIds(
						_themeDisplay.getScopeGroupId(), true),
				_folderId, _restrictionType, _getOrderByComparator());
		}
		else {
			_ddmStructures = JournalFolderServiceUtil.searchDDMStructures(
				_themeDisplay.getCompanyId(),
				SiteConnectedGroupGroupProviderUtil.
					getCurrentAndAncestorSiteAndDepotGroupIds(
						_themeDisplay.getScopeGroupId(), true),
				_folderId, _restrictionType, _getKeywords(), QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, _getOrderByComparator());
		}

		Collections.sort(_ddmStructures, _getOrderByComparator());

		return _ddmStructures;
	}

	public String getDDMStructureScopeName(
			DDMStructure ddmStructure, Locale locale)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)_renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Group scopeGroup = themeDisplay.getScopeGroup();

		if (ddmStructure.getGroupId() == scopeGroup.getGroupId()) {
			if (scopeGroup.isDepot()) {
				return LanguageUtil.get(
					_httpServletRequest, "current-asset-library");
			}

			return LanguageUtil.get(_httpServletRequest, "current-site");
		}

		Group ddmStructureGroup = GroupLocalServiceUtil.getGroup(
			ddmStructure.getGroupId());

		return ddmStructureGroup.getName(locale);
	}

	public SearchContainer<DDMStructure> getDDMStructuresSearchContainer()
		throws PortalException {

		if (_ddmStructuresSearchContainer != null) {
			return _ddmStructuresSearchContainer;
		}

		SearchContainer<DDMStructure> searchContainer = new SearchContainer(
			_renderRequest, getPortletURL(), null, "no-results-were-found");

		searchContainer.setOrderByCol(getOrderByCol());
		searchContainer.setOrderByComparator(_getOrderByComparator());
		searchContainer.setOrderByType(getOrderByType());
		searchContainer.setResultsAndTotal(getDDMStructures());

		_ddmStructuresSearchContainer = searchContainer;

		return _ddmStructuresSearchContainer;
	}

	public String getEventName() {
		if (_eventName != null) {
			return _eventName;
		}

		_eventName = ParamUtil.getString(
			_renderRequest, "eventName",
			_renderResponse.getNamespace() + "selectAddMenuItem");

		return _eventName;
	}

	public List<NavigationItem> getNavigationItems() {
		return NavigationItemListBuilder.add(
			navigationItem -> {
				navigationItem.setActive(true);
				navigationItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "all-menu-items"));
			}
		).build();
	}

	public String getOrderByCol() {
		if (Validator.isNotNull(_orderByCol)) {
			return _orderByCol;
		}

		_orderByCol = SearchOrderByUtil.getOrderByCol(
			_httpServletRequest, JournalPortletKeys.JOURNAL,
			"view-more-items-order-by-col", "modified-date");

		return _orderByCol;
	}

	public String getOrderByType() {
		if (Validator.isNotNull(_orderByType)) {
			return _orderByType;
		}

		_orderByType = SearchOrderByUtil.getOrderByType(
			_httpServletRequest, JournalPortletKeys.JOURNAL,
			"view-more-items-order-by-type", "desc");

		return _orderByType;
	}

	public PortletURL getPortletURL() {
		return PortletURLBuilder.createRenderURL(
			_renderResponse
		).setMVCPath(
			"/view_more_menu_items.jsp"
		).setParameter(
			"eventName", getEventName()
		).setParameter(
			"folderId", _folderId
		).buildPortletURL();
	}

	public String getRedirect() {
		if (_redirect != null) {
			return _redirect;
		}

		_redirect = ParamUtil.getString(_renderRequest, "redirect");

		return _redirect;
	}

	private String _getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_renderRequest, "keywords");

		return _keywords;
	}

	private OrderByComparator<DDMStructure> _getOrderByComparator() {
		boolean orderByAsc = false;

		if (Objects.equals(getOrderByType(), "asc")) {
			orderByAsc = true;
		}

		OrderByComparator<DDMStructure> orderByComparator = null;

		if (_orderByCol.equals("modified-date")) {
			orderByComparator = new StructureModifiedDateComparator(orderByAsc);
		}
		else if (_orderByCol.equals("name")) {
			orderByComparator = new StructureNameComparator(
				orderByAsc, _themeDisplay.getLocale());
		}

		return orderByComparator;
	}

	private List<DDMStructure> _ddmStructures;
	private SearchContainer<DDMStructure> _ddmStructuresSearchContainer;
	private String _eventName;
	private final long _folderId;
	private final HttpServletRequest _httpServletRequest;
	private String _keywords;
	private String _orderByCol;
	private String _orderByType;
	private String _redirect;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final int _restrictionType;
	private final ThemeDisplay _themeDisplay;

}