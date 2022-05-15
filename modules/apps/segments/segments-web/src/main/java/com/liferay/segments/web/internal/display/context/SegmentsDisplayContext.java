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

package com.liferay.segments.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenuBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.portlet.SearchDisplayStyleUtil;
import com.liferay.portal.kernel.portlet.SearchOrderByUtil;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.segments.configuration.provider.SegmentsConfigurationProvider;
import com.liferay.segments.constants.SegmentsActionKeys;
import com.liferay.segments.constants.SegmentsEntryConstants;
import com.liferay.segments.constants.SegmentsPortletKeys;
import com.liferay.segments.model.SegmentsEntry;
import com.liferay.segments.service.SegmentsEntryService;
import com.liferay.segments.web.internal.security.permission.resource.SegmentsEntryPermission;
import com.liferay.segments.web.internal.security.permission.resource.SegmentsResourcePermission;
import com.liferay.segments.web.internal.util.comparator.SegmentsEntryModifiedDateComparator;
import com.liferay.segments.web.internal.util.comparator.SegmentsEntryNameComparator;

import java.util.List;
import java.util.Objects;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eduardo García
 */
public class SegmentsDisplayContext {

	public SegmentsDisplayContext(
		HttpServletRequest httpServletRequest, RenderRequest renderRequest,
		RenderResponse renderResponse,
		SegmentsConfigurationProvider segmentsConfigurationProvider,
		SegmentsEntryService segmentsEntryService) {

		_httpServletRequest = httpServletRequest;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_segmentsConfigurationProvider = segmentsConfigurationProvider;
		_segmentsEntryService = segmentsEntryService;

		_themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public List<DropdownItem> getActionDropdownItems() {
		return DropdownItemListBuilder.add(
			dropdownItem -> {
				dropdownItem.putData("action", "deleteSegmentsEntries");
				dropdownItem.setIcon("times-circle");
				dropdownItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "delete"));
				dropdownItem.setQuickAction(true);
			}
		).build();
	}

	public String getAvailableActions(SegmentsEntry segmentsEntry)
		throws PortalException {

		if (SegmentsEntryPermission.contains(
				_themeDisplay.getPermissionChecker(), segmentsEntry,
				ActionKeys.DELETE)) {

			return "deleteSegmentsEntries";
		}

		return StringPool.BLANK;
	}

	public String getClearResultsURL() {
		return PortletURLBuilder.create(
			_getPortletURL()
		).setKeywords(
			StringPool.BLANK
		).buildString();
	}

	public CreationMenu getCreationMenu() {
		return CreationMenuBuilder.addPrimaryDropdownItem(
			dropdownItem -> {
				dropdownItem.setHref(
					_renderResponse.createRenderURL(), "mvcRenderCommandName",
					"/segments/edit_segments_entry", "type",
					User.class.getName());
				dropdownItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "user-segment"));
			}
		).build();
	}

	public String getDisplayStyle() {
		if (_displayStyle != null) {
			return _displayStyle;
		}

		_displayStyle = SearchDisplayStyleUtil.getDisplayStyle(
			_renderRequest, SegmentsPortletKeys.SEGMENTS, "list");

		return _displayStyle;
	}

	public List<DropdownItem> getFilterItemsDropdownItems() {
		return DropdownItemListBuilder.addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					_getFilterNavigationDropdownItems());
				dropdownGroupItem.setLabel(
					LanguageUtil.get(
						_httpServletRequest, "filter-by-navigation"));
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(_getOrderByDropdownItems());
				dropdownGroupItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "order-by"));
			}
		).build();
	}

	public String getOrderByType() {
		if (_orderByType != null) {
			return _orderByType;
		}

		_orderByType = SearchOrderByUtil.getOrderByType(
			_renderRequest, SegmentsPortletKeys.SEGMENTS, "asc");

		return _orderByType;
	}

	public String getSearchActionURL() {
		return String.valueOf(_getPortletURL());
	}

	public SearchContainer<SegmentsEntry> getSearchContainer()
		throws PortalException {

		if (_searchContainer != null) {
			return _searchContainer;
		}

		SearchContainer<SegmentsEntry> searchContainer = new SearchContainer(
			_renderRequest, _getPortletURL(), null, "there-are-no-segments");

		searchContainer.setId("segmentsEntries");
		searchContainer.setOrderByCol(_getOrderByCol());
		searchContainer.setOrderByComparator(_getOrderByComparator());
		searchContainer.setOrderByType(getOrderByType());

		if (_isSearch()) {
			searchContainer.setResultsAndTotal(
				_segmentsEntryService.searchSegmentsEntries(
					_themeDisplay.getCompanyId(),
					_themeDisplay.getScopeGroupId(), _getKeywords(), true,
					searchContainer.getStart(), searchContainer.getEnd(),
					_getSort()));
		}
		else {
			searchContainer.setResultsAndTotal(
				() -> _segmentsEntryService.getSegmentsEntries(
					_themeDisplay.getScopeGroupId(), true,
					searchContainer.getStart(), searchContainer.getEnd(),
					searchContainer.getOrderByComparator()),
				_segmentsEntryService.getSegmentsEntriesCount(
					_themeDisplay.getScopeGroupId(), true));
		}

		searchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_renderResponse));

		_searchContainer = searchContainer;

		return _searchContainer;
	}

	public String getSegmentsEntryURL(SegmentsEntry segmentsEntry) {
		if (segmentsEntry == null) {
			return StringPool.BLANK;
		}

		if (Objects.equals(
				segmentsEntry.getSource(),
				SegmentsEntryConstants.SOURCE_ASAH_FARO_BACKEND)) {

			String asahFaroURL = PrefsPropsUtil.getString(
				segmentsEntry.getCompanyId(), "liferayAnalyticsURL");

			if (Validator.isNull(asahFaroURL)) {
				return StringPool.BLANK;
			}

			return asahFaroURL + "/contacts/segments/" +
				segmentsEntry.getSegmentsEntryKey();
		}

		return PortletURLBuilder.createRenderURL(
			_renderResponse
		).setMVCRenderCommandName(
			"/segments/edit_segments_entry"
		).setRedirect(
			PortalUtil.getCurrentURL(_renderRequest)
		).setParameter(
			"segmentsEntryId", segmentsEntry.getSegmentsEntryId()
		).setParameter(
			"showInEditMode", false
		).buildString();
	}

	public String getSegmentsEntryURLTarget(SegmentsEntry segmentsEntry) {
		if (Objects.equals(
				segmentsEntry.getSource(),
				SegmentsEntryConstants.SOURCE_ASAH_FARO_BACKEND)) {

			return "_blank";
		}

		return "_self";
	}

	public String getSortingURL() {
		return PortletURLBuilder.create(
			_getPortletURL()
		).setParameter(
			"orderByType",
			Objects.equals(getOrderByType(), "asc") ? "desc" : "asc"
		).buildString();
	}

	public int getTotalItems() throws PortalException {
		SearchContainer<?> searchContainer = getSearchContainer();

		return searchContainer.getTotal();
	}

	public boolean isAsahEnabled(long companyId) {
		if (Validator.isNotNull(
				PrefsPropsUtil.getString(companyId, "liferayAnalyticsURL"))) {

			return true;
		}

		return false;
	}

	public boolean isDisabledManagementBar() throws PortalException {
		if (_hasResults() || _isSearch()) {
			return false;
		}

		return true;
	}

	public boolean isRoleSegmentationEnabled(long companyId) {
		try {
			return _segmentsConfigurationProvider.isRoleSegmentationEnabled(
				companyId);
		}
		catch (ConfigurationException configurationException) {
			_log.error(configurationException);
		}

		return false;
	}

	public boolean isShowCreationMenu() {
		if (SegmentsResourcePermission.contains(
				_themeDisplay.getPermissionChecker(),
				_themeDisplay.getScopeGroupId(),
				SegmentsActionKeys.MANAGE_SEGMENTS_ENTRIES)) {

			return true;
		}

		return false;
	}

	private List<DropdownItem> _getFilterNavigationDropdownItems() {
		return DropdownItemListBuilder.add(
			dropdownItem -> {
				dropdownItem.setActive(true);
				dropdownItem.setHref(_renderResponse.createRenderURL());
				dropdownItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "all"));
			}
		).build();
	}

	private String _getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_httpServletRequest, "keywords");

		return _keywords;
	}

	private String _getOrderByCol() {
		if (_orderByCol != null) {
			return _orderByCol;
		}

		_orderByCol = SearchOrderByUtil.getOrderByCol(
			_renderRequest, SegmentsPortletKeys.SEGMENTS, "modified-date");

		return _orderByCol;
	}

	private OrderByComparator<SegmentsEntry> _getOrderByComparator() {
		boolean orderByAsc = false;

		if (Objects.equals(getOrderByType(), "asc")) {
			orderByAsc = true;
		}

		String orderByCol = _getOrderByCol();

		OrderByComparator<SegmentsEntry> orderByComparator = null;

		if (orderByCol.equals("modified-date")) {
			orderByComparator = new SegmentsEntryModifiedDateComparator(
				orderByAsc);
		}
		else if (orderByCol.equals("name")) {
			orderByComparator = new SegmentsEntryNameComparator(orderByAsc);
		}

		return orderByComparator;
	}

	private List<DropdownItem> _getOrderByDropdownItems() {
		return DropdownItemListBuilder.add(
			dropdownItem -> {
				dropdownItem.setActive(
					Objects.equals(_getOrderByCol(), "modified-date"));
				dropdownItem.setHref(
					_getPortletURL(), "orderByCol", "modified-date");
				dropdownItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "modified-date"));
			}
		).add(
			dropdownItem -> {
				dropdownItem.setActive(
					Objects.equals(_getOrderByCol(), "name"));
				dropdownItem.setHref(_getPortletURL(), "orderByCol", "name");
				dropdownItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "name"));
			}
		).build();
	}

	private PortletURL _getPortletURL() {
		return PortletURLBuilder.createRenderURL(
			_renderResponse
		).setMVCPath(
			"/view.jsp"
		).setKeywords(
			() -> {
				String keywords = _getKeywords();

				if (Validator.isNotNull(keywords)) {
					return keywords;
				}

				return null;
			}
		).setParameter(
			"displayStyle", getDisplayStyle()
		).setParameter(
			"orderByCol", _getOrderByCol()
		).setParameter(
			"orderByType", getOrderByType()
		).buildPortletURL();
	}

	private Sort _getSort() {
		boolean orderByAsc = false;

		if (Objects.equals(getOrderByType(), "asc")) {
			orderByAsc = true;
		}

		Sort sort = null;

		if (Objects.equals(_getOrderByCol(), "name")) {
			sort = new Sort(
				Field.getSortableFieldName(
					"localized_name_".concat(_themeDisplay.getLanguageId())),
				Sort.STRING_TYPE, !orderByAsc);
		}
		else {
			sort = new Sort(Field.MODIFIED_DATE, Sort.LONG_TYPE, !orderByAsc);
		}

		return sort;
	}

	private boolean _hasResults() throws PortalException {
		if (getTotalItems() > 0) {
			return true;
		}

		return false;
	}

	private boolean _isSearch() {
		if (Validator.isNotNull(_getKeywords())) {
			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SegmentsDisplayContext.class);

	private String _displayStyle;
	private final HttpServletRequest _httpServletRequest;
	private String _keywords;
	private String _orderByCol;
	private String _orderByType;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private SearchContainer<SegmentsEntry> _searchContainer;
	private final SegmentsConfigurationProvider _segmentsConfigurationProvider;
	private final SegmentsEntryService _segmentsEntryService;
	private final ThemeDisplay _themeDisplay;

}