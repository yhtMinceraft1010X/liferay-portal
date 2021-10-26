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

package com.liferay.search.experiences.web.internal.display.context;

import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.vulcan.util.TransformUtil;
import com.liferay.search.experiences.constants.SXPPortletKeys;

import java.util.Objects;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.hits.SearchHit;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.query.BooleanQuery;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.query.TermQuery;
import com.liferay.portal.search.searcher.SearchRequest;
import com.liferay.portal.search.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.searcher.SearchResponse;
import com.liferay.portal.search.searcher.Searcher;
import com.liferay.portal.search.sort.Sort;
import com.liferay.portal.search.sort.SortOrder;
import com.liferay.portal.search.sort.Sorts;
import com.liferay.search.experiences.model.SXPBlueprint;
import com.liferay.search.experiences.model.SXPElement;
import com.liferay.search.experiences.service.SXPBlueprintService;
import com.liferay.search.experiences.service.SXPElementService;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.PortletRequest;

/**
 * @author Petteri Karttunen
 */
public abstract class BaseDisplayContext<R> {

	public BaseDisplayContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse, Queries queries,
		Searcher searcher,
		SearchRequestBuilderFactory searchRequestBuilderFactory, Sorts sorts) {

		this.liferayPortletRequest = liferayPortletRequest;
		this.liferayPortletResponse = liferayPortletResponse;
		_queries = queries;
		_searcher = searcher;
		_searchRequestBuilderFactory = searchRequestBuilderFactory;
		_sorts = sorts;

		httpServletRequest = liferayPortletRequest.getHttpServletRequest();
		portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(
			liferayPortletRequest);
		themeDisplay = (ThemeDisplay)liferayPortletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public String getDisplayStyle() {
		String displayStyle = ParamUtil.getString(
			liferayPortletRequest, "displayStyle");

		if (Validator.isNull(displayStyle)) {
			return portalPreferences.getValue(
				SXPPortletKeys.SXP_BLUEPRINT_ADMIN,
				getDisplayStylePreferenceName(), "descriptive");
		}

		portalPreferences.setValue(
			SXPPortletKeys.SXP_BLUEPRINT_ADMIN, getDisplayStylePreferenceName(),
			displayStyle);

		// TODO Verify that clearing the SPA cache is needed only after calling
		// portalPreferences#setValue

		httpServletRequest.setAttribute(
			WebKeys.SINGLE_PAGE_APPLICATION_CLEAR_CACHE, Boolean.TRUE);

		return displayStyle;
	}

	protected abstract String getDisplayStylePreferenceName();

	protected abstract String getMVCRenderCommandName();

	private String _getOrderByCol() {
		return ParamUtil.getString(
			liferayPortletRequest, "orderByCol", Field.MODIFIED_DATE);
	}

	private String _getOrderByType() {
		String orderByType = ParamUtil.getString(
			liferayPortletRequest, "orderByType");

		if (Validator.isNotNull(orderByType)) {
			return orderByType;
		}

		if (Objects.equals(_getOrderByCol(), Field.TITLE)) {
			return "asc";
		}

		return "desc";
	}

	protected SearchContainer<R> getSearchContainer(String emptyResultsMessage)
		throws PortalException {

		SearchContainer<R> searchContainer = new SearchContainer<>(
			liferayPortletRequest,
			PortletURLBuilder.createRenderURL(
				liferayPortletResponse
			).setMVCRenderCommandName(
				getMVCRenderCommandName()
			).setTabs1(
				ParamUtil.getString(
					liferayPortletRequest, "tabs1", "sxpBlueprints")
			).build(),
			null,
			emptyResultsMessage);

		searchContainer.setOrderByCol(_getOrderByCol());
		searchContainer.setOrderByType(_getOrderByType());
		searchContainer.setRowChecker(
			new EmptyOnClickRowChecker(liferayPortletResponse));

		return searchContainer;
	}

	protected final HttpServletRequest httpServletRequest;
	protected final LiferayPortletRequest liferayPortletRequest;
	protected final LiferayPortletResponse liferayPortletResponse;
	protected final PortalPreferences portalPreferences;
	protected final ThemeDisplay themeDisplay;
	private final Queries _queries;
	private final Searcher _searcher;
	private final SearchRequestBuilderFactory _searchRequestBuilderFactory;
	private final Sorts _sorts;

	protected void populateSXPBlueprintSearchContainer(
		PortletRequest portletRequest,
		SearchContainer<R> searchContainer,
		int status) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		BooleanQuery booleanQuery = _queries.booleanQuery();

		booleanQuery.addFilterQueryClauses(
			_queries.term(Field.GROUP_ID, themeDisplay.getCompanyGroupId()));

		if (status != WorkflowConstants.STATUS_ANY) {
			booleanQuery.addFilterQueryClauses(
				_queries.term(Field.STATUS, status));
		}

		String keywords = ParamUtil.getString(portletRequest, "keywords");

		String titleField = LocalizationUtil.getLocalizedName(
			"localized_" + Field.TITLE, themeDisplay.getLanguageId());

		if (Validator.isBlank(keywords)) {
			booleanQuery.addMustQueryClauses(_queries.matchAll());
		}
		else {
			booleanQuery.addMustQueryClauses(
				_queries.multiMatch(
					keywords,
					SetUtil.fromArray(titleField, LocalizationUtil.getLocalizedName(Field.DESCRIPTION, themeDisplay.getLanguageId()))));
		}

		processBooleanQuery(booleanQuery, portletRequest, _queries);

		Sort sort = null;

		SortOrder sortOrder = SortOrder.ASC;

		if (Objects.equals(_getOrderByType(), "desc")) {
			sortOrder = SortOrder.DESC;
		}

		String orderByCol = _getOrderByCol();

		if (Objects.equals(orderByCol, Field.TITLE)) {
			sort = _sorts.field(
				titleField + "_String_sortable", sortOrder);
		}
		else {
			sort = _sorts.field(orderByCol, sortOrder);
		}

		SearchResponse searchResponse = _searcher.search(
			_searchRequestBuilderFactory.builder(
			).addSort(
				sort
			).companyId(
				themeDisplay.getCompanyId()
			).from(
				searchContainer.getStart()
			).modelIndexerClasses(
				getModelIndexerClass()
			).query(
				booleanQuery
			).size(
				searchContainer.getDelta()
			).build());

		SearchHits searchHits = searchResponse.getSearchHits();

		searchContainer.setResults(
			TransformUtil.transform(
				searchHits.getSearchHits(),
				searchHit -> {
					Document document = searchHit.getDocument();

					return toBaseModel(document.getLong(Field.ENTRY_CLASS_PK));
				}));
		searchContainer.setTotal(
			GetterUtil.getInteger(searchHits.getTotalHits()));
	}

	protected abstract Class<?> getModelIndexerClass();

	protected abstract void processBooleanQuery(
		BooleanQuery booleanQuery, PortletRequest portletRequest, Queries queries);

	protected abstract R toBaseModel(long entryClassPK);

	private static final Log _log = LogFactoryUtil.getLog(
		BaseDisplayContext.class);

}