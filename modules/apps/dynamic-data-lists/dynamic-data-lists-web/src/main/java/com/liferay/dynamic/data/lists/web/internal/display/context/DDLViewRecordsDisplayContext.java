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

package com.liferay.dynamic.data.lists.web.internal.display.context;

import com.liferay.dynamic.data.lists.constants.DDLActionKeys;
import com.liferay.dynamic.data.lists.constants.DDLPortletKeys;
import com.liferay.dynamic.data.lists.constants.DDLWebKeys;
import com.liferay.dynamic.data.lists.model.DDLRecord;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordVersion;
import com.liferay.dynamic.data.lists.service.DDLRecordLocalServiceUtil;
import com.liferay.dynamic.data.lists.util.comparator.DDLRecordCreateDateComparator;
import com.liferay.dynamic.data.lists.util.comparator.DDLRecordModifiedDateComparator;
import com.liferay.dynamic.data.lists.web.internal.display.context.helper.DDLRequestHelper;
import com.liferay.dynamic.data.lists.web.internal.search.RecordSearch;
import com.liferay.dynamic.data.lists.web.internal.security.permission.resource.DDLRecordSetPermission;
import com.liferay.dynamic.data.mapping.exception.StorageException;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenuBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemListBuilder;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.portlet.SearchOrderByUtil;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlParserUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rafael Praxedes
 */
public class DDLViewRecordsDisplayContext {

	public DDLViewRecordsDisplayContext(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			long formDDMTemplateId)
		throws PortalException {

		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;
		_formDDMTemplateId = formDDMTemplateId;

		_ddlRecordSet = (DDLRecordSet)_liferayPortletRequest.getAttribute(
			DDLWebKeys.DYNAMIC_DATA_LISTS_RECORD_SET);

		_ddlRequestHelper = new DDLRequestHelper(
			PortalUtil.getHttpServletRequest(liferayPortletRequest));

		_ddmStructure = _ddlRecordSet.getDDMStructure(formDDMTemplateId);

		User user = PortalUtil.getUser(liferayPortletRequest);

		if (user == null) {
			ThemeDisplay themeDisplay = _ddlRequestHelper.getThemeDisplay();

			user = themeDisplay.getDefaultUser();
		}

		_user = user;
	}

	public List<DropdownItem> getActionItemsDropdownItems()
		throws PortalException {

		if (!hasDeletePermission()) {
			return null;
		}

		return DropdownItemListBuilder.add(
			dropdownItem -> {
				dropdownItem.putData("action", "deleteRecords");
				dropdownItem.setIcon("times-circle");
				dropdownItem.setLabel(
					LanguageUtil.get(_ddlRequestHelper.getRequest(), "delete"));
				dropdownItem.setQuickAction(true);
			}
		).build();
	}

	public String getClearResultsURL() throws PortletException {
		return PortletURLBuilder.create(
			PortletURLUtil.clone(getPortletURL(), _liferayPortletResponse)
		).setKeywords(
			StringPool.BLANK
		).buildString();
	}

	public CreationMenu getCreationMenu() throws PortalException {
		if (!isShowAddRecordButton()) {
			return null;
		}

		return CreationMenuBuilder.addPrimaryDropdownItem(
			dropdownItem -> {
				dropdownItem.setHref(
					_liferayPortletResponse.createRenderURL(), "mvcPath",
					"/edit_record.jsp", "redirect",
					PortalUtil.getCurrentURL(_ddlRequestHelper.getRequest()),
					"recordSetId",
					String.valueOf(_ddlRecordSet.getRecordSetId()),
					"formDDMTemplateId", String.valueOf(_formDDMTemplateId));

				dropdownItem.setLabel(
					LanguageUtil.format(
						_ddlRequestHelper.getRequest(), "add-x",
						HtmlUtil.escape(
							_ddmStructure.getName(
								_ddlRequestHelper.getLocale())),
						false));
			}
		).build();
	}

	public OrderByComparator<DDLRecord> getDDLRecordOrderByComparator(
		String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator<DDLRecord> orderByComparator;

		if (orderByCol.equals("modified-date")) {
			orderByComparator = new DDLRecordModifiedDateComparator(orderByAsc);
		}
		else {
			orderByComparator = new DDLRecordCreateDateComparator(orderByAsc);
		}

		return orderByComparator;
	}

	public List<DDMFormField> getDDMFormFields() {
		if (_ddmFormFields == null) {
			DDMForm ddmForm = _ddmStructure.getDDMForm();

			List<DDMFormField> ddmFormFields = new ArrayList<>();

			for (DDMFormField ddmFormField : ddmForm.getDDMFormFields()) {
				_addDDMFormField(ddmFormFields, ddmFormField);
			}

			_ddmFormFields = ddmFormFields;
		}

		return _ddmFormFields;
	}

	public Map<String, List<DDMFormFieldValue>> getDDMFormFieldValuesMap(
			DDLRecordVersion recordVersion)
		throws StorageException {

		DDMFormValues ddmFormValues = recordVersion.getDDMFormValues();

		List<DDMFormFieldValue> ddmFormFieldValues =
			ddmFormValues.getDDMFormFieldValues();

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			new LinkedHashMap<>();

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			_putDDMFormFieldValue(ddmFormFieldValuesMap, ddmFormFieldValue);
		}

		return ddmFormFieldValuesMap;
	}

	public DDMStructure getDDMStructure() {
		return _ddmStructure;
	}

	public String getDisplayStyle() {
		return "list";
	}

	public List<DropdownItem> getFilterItemsDropdownItems() {
		HttpServletRequest httpServletRequest = _ddlRequestHelper.getRequest();

		return DropdownItemListBuilder.addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					getFilterNavigationDropdownItems());
				dropdownGroupItem.setLabel(
					LanguageUtil.get(
						httpServletRequest, "filter-by-navigation"));
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(getOrderByDropdownItems());
				dropdownGroupItem.setLabel(
					LanguageUtil.get(httpServletRequest, "order-by"));
			}
		).build();
	}

	public List<NavigationItem> getNavigationItems() {
		return NavigationItemListBuilder.add(
			navigationItem -> {
				navigationItem.setActive(true);
				navigationItem.setLabel(
					HtmlParserUtil.extractText(
						_ddlRecordSet.getName(_ddlRequestHelper.getLocale())));
			}
		).build();
	}

	public String getOrderByCol() {
		if (Validator.isNotNull(_orderByCol)) {
			return _orderByCol;
		}

		_orderByCol = SearchOrderByUtil.getOrderByCol(
			_liferayPortletRequest, DDLPortletKeys.DYNAMIC_DATA_LISTS,
			"view-records-order-by-col", "modified-date");

		return _orderByCol;
	}

	public String getOrderByType() {
		if (Validator.isNotNull(_orderByType)) {
			return _orderByType;
		}

		_orderByType = SearchOrderByUtil.getOrderByType(
			_liferayPortletRequest, DDLPortletKeys.DYNAMIC_DATA_LISTS,
			"view-records-order-by-type", "asc");

		return _orderByType;
	}

	public PortletURL getPortletURL() {
		return PortletURLBuilder.create(
			PortletURLUtil.getCurrent(
				_liferayPortletRequest, _liferayPortletResponse)
		).setMVCPath(
			_getMVCPath()
		).setRedirect(
			ParamUtil.getString(_liferayPortletRequest, "redirect")
		).setKeywords(
			() -> {
				String keywords = getKeywords();

				if (Validator.isNotNull(keywords)) {
					return keywords;
				}

				return null;
			}
		).setParameter(
			"delta",
			() -> {
				String delta = ParamUtil.getString(
					_liferayPortletRequest, "delta");

				if (Validator.isNotNull(delta)) {
					return delta;
				}

				return null;
			}
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
			"recordSetId", _ddlRecordSet.getRecordSetId()
		).buildPortletURL();
	}

	public SearchContainer<?> getSearch() throws PortalException {
		PortletURL portletURL = PortletURLBuilder.create(
			getPortletURL()
		).setParameter(
			"displayStyle", getDisplayStyle()
		).buildPortletURL();

		List<String> headerNames = new ArrayList<>();

		List<DDMFormField> ddmFormFields = getDDMFormFields();

		for (DDMFormField ddmFormField : ddmFormFields) {
			LocalizedValue label = ddmFormField.getLabel();

			headerNames.add(label.getString(_ddlRequestHelper.getLocale()));
		}

		if (hasUpdatePermission()) {
			headerNames.add("status");
			headerNames.add("modified-date");
			headerNames.add("author");
		}

		headerNames.add(StringPool.BLANK);

		SearchContainer<DDLRecord> recordSearch = new RecordSearch(
			_liferayPortletRequest, portletURL, headerNames);

		if (recordSearch.isSearch()) {
			recordSearch.setEmptyResultsMessage(
				LanguageUtil.format(
					_ddlRequestHelper.getLocale(), "no-x-records-were-found",
					_ddlRecordSet.getName(), false));
		}
		else {
			recordSearch.setEmptyResultsMessage("there-are-no-records");
		}

		recordSearch.setOrderByCol(getOrderByCol());
		recordSearch.setOrderByComparator(
			getDDLRecordOrderByComparator(getOrderByCol(), getOrderByType()));
		recordSearch.setOrderByType(getOrderByType());

		DisplayTerms displayTerms = recordSearch.getDisplayTerms();

		int status = WorkflowConstants.STATUS_APPROVED;

		if (isShowAddRecordButton()) {
			status = WorkflowConstants.STATUS_ANY;
		}

		int ddlRecordStatus = status;

		if (Validator.isNull(displayTerms.getKeywords())) {
			recordSearch.setResultsAndTotal(
				() -> DDLRecordLocalServiceUtil.getRecords(
					_ddlRecordSet.getRecordSetId(), ddlRecordStatus,
					recordSearch.getStart(), recordSearch.getEnd(),
					recordSearch.getOrderByComparator()),
				DDLRecordLocalServiceUtil.getRecordsCount(
					_ddlRecordSet.getRecordSetId(), ddlRecordStatus));
		}
		else {
			SearchContext searchContext = _getSearchContext(
				recordSearch, ddlRecordStatus);

			recordSearch.setResultsAndTotal(
				DDLRecordLocalServiceUtil.searchDDLRecords(searchContext));
		}

		if (!_user.isDefaultUser()) {
			recordSearch.setRowChecker(
				new EmptyOnClickRowChecker(_liferayPortletResponse));
		}

		return recordSearch;
	}

	public String getSearchActionURL() {
		return PortletURLBuilder.createRenderURL(
			_liferayPortletResponse
		).setMVCPath(
			_getMVCPath()
		).setRedirect(
			PortalUtil.getCurrentURL(_liferayPortletRequest)
		).setParameter(
			"recordSetId", _ddlRecordSet.getRecordSetId()
		).buildString();
	}

	public String getSearchContainerId() {
		return "ddlRecord";
	}

	public String getSortingURL() throws Exception {
		return PortletURLBuilder.create(
			PortletURLUtil.clone(getPortletURL(), _liferayPortletResponse)
		).setParameter(
			"orderByType",
			() -> {
				String orderByType = ParamUtil.getString(
					_liferayPortletRequest, "orderByType");

				if (orderByType.equals("asc")) {
					return "desc";
				}

				return "asc";
			}
		).buildString();
	}

	public int getTotalItems() throws PortalException {
		SearchContainer<?> searchContainer = getSearch();

		return searchContainer.getTotal();
	}

	public boolean hasDeletePermission() throws PortalException {
		boolean hasDeletePermission = false;

		if (isEditable() || isAdminPortlet()) {
			hasDeletePermission = DDLRecordSetPermission.contains(
				getPermissionChecker(), _ddlRecordSet.getRecordSetId(),
				ActionKeys.DELETE);
		}

		return hasDeletePermission;
	}

	public boolean hasUpdatePermission() throws PortalException {
		boolean hasUpdatePermission = false;

		if (isEditable() || isAdminPortlet()) {
			hasUpdatePermission = DDLRecordSetPermission.contains(
				getPermissionChecker(), _ddlRecordSet.getRecordSetId(),
				ActionKeys.UPDATE);
		}

		return hasUpdatePermission;
	}

	public boolean isAdminPortlet() {
		String portletName = getPortletName();

		return portletName.equals(DDLPortletKeys.DYNAMIC_DATA_LISTS);
	}

	public boolean isDisabledManagementBar() throws PortalException {
		if (hasResults() || isSearch()) {
			return false;
		}

		return true;
	}

	public boolean isEditable() {
		if (isAdminPortlet()) {
			return true;
		}

		return PrefsParamUtil.getBoolean(
			_ddlRequestHelper.getPortletPreferences(),
			_ddlRequestHelper.getRenderRequest(), "editable", true);
	}

	public boolean isSelectable() {
		return !_user.isDefaultUser();
	}

	protected List<DropdownItem> getFilterNavigationDropdownItems() {
		return DropdownItemListBuilder.add(
			dropdownItem -> {
				dropdownItem.setActive(true);

				dropdownItem.setHref(getPortletURL(), "navigation", "all");

				dropdownItem.setLabel(
					LanguageUtil.get(_ddlRequestHelper.getRequest(), "all"));
			}
		).build();
	}

	protected String getKeywords() {
		return ParamUtil.getString(_liferayPortletRequest, "keywords");
	}

	protected UnsafeConsumer<DropdownItem, Exception> getOrderByDropdownItem(
		String orderByCol) {

		return dropdownItem -> {
			dropdownItem.setActive(orderByCol.equals(getOrderByCol()));
			dropdownItem.setHref(getPortletURL(), "orderByCol", orderByCol);
			dropdownItem.setLabel(
				LanguageUtil.get(_ddlRequestHelper.getRequest(), orderByCol));
		};
	}

	protected List<DropdownItem> getOrderByDropdownItems() {
		return DropdownItemListBuilder.add(
			getOrderByDropdownItem("create-date")
		).add(
			getOrderByDropdownItem("modified-date")
		).build();
	}

	protected PermissionChecker getPermissionChecker() {
		return _ddlRequestHelper.getPermissionChecker();
	}

	protected String getPortletName() {
		return _ddlRequestHelper.getPortletName();
	}

	protected boolean hasResults() throws PortalException {
		if (getTotalItems() > 0) {
			return true;
		}

		return false;
	}

	protected boolean isSearch() {
		if (Validator.isNotNull(getKeywords())) {
			return true;
		}

		return false;
	}

	protected boolean isShowAddRecordButton() throws PortalException {
		boolean showAddRecordButton = false;

		if (isEditable()) {
			showAddRecordButton = DDLRecordSetPermission.contains(
				getPermissionChecker(), _ddlRecordSet.getRecordSetId(),
				DDLActionKeys.ADD_RECORD);
		}

		return showAddRecordButton;
	}

	private void _addDDMFormField(
		List<DDMFormField> ddmFormFields, DDMFormField ddmFormField) {

		if (!_isDDMFormFieldTransient(ddmFormField)) {
			ddmFormFields.add(ddmFormField);

			return;
		}

		for (DDMFormField nestedDDMFormField :
				ddmFormField.getNestedDDMFormFields()) {

			_addDDMFormField(ddmFormFields, nestedDDMFormField);
		}
	}

	private String _getMVCPath() {
		if (isAdminPortlet()) {
			return "/view_record_set.jsp";
		}

		return "/view_selected_record_set.jsp";
	}

	private SearchContext _getSearchContext(
		SearchContainer<DDLRecord> recordSearch, int status) {

		SearchContext searchContext = SearchContextFactory.getInstance(
			_ddlRequestHelper.getRequest());

		searchContext.setAttribute(Field.STATUS, status);
		searchContext.setAttribute(
			"recordSetId", _ddlRecordSet.getRecordSetId());
		searchContext.setAttribute("recordSetScope", _ddlRecordSet.getScope());
		searchContext.setEnd(recordSearch.getEnd());
		searchContext.setKeywords(getKeywords());
		searchContext.setStart(recordSearch.getStart());

		return searchContext;
	}

	private boolean _isDDMFormFieldTransient(DDMFormField ddmFormField) {
		if (Validator.isNull(ddmFormField.getDataType())) {
			return true;
		}

		return false;
	}

	private void _putDDMFormFieldValue(
		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap,
		DDMFormFieldValue ddmFormFieldValue) {

		List<DDMFormFieldValue> ddmFormFieldValues = ddmFormFieldValuesMap.get(
			ddmFormFieldValue.getName());

		if (ddmFormFieldValues == null) {
			ddmFormFieldValues = new ArrayList<>();

			ddmFormFieldValuesMap.put(
				ddmFormFieldValue.getName(), ddmFormFieldValues);
		}

		ddmFormFieldValues.add(ddmFormFieldValue);

		for (DDMFormFieldValue nestedDDMFormFieldValue :
				ddmFormFieldValue.getNestedDDMFormFieldValues()) {

			_putDDMFormFieldValue(
				ddmFormFieldValuesMap, nestedDDMFormFieldValue);
		}
	}

	private final DDLRecordSet _ddlRecordSet;
	private final DDLRequestHelper _ddlRequestHelper;
	private List<DDMFormField> _ddmFormFields;
	private final DDMStructure _ddmStructure;
	private final long _formDDMTemplateId;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private String _orderByCol;
	private String _orderByType;
	private final User _user;

}