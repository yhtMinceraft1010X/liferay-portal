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
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
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
		SearchRequestBuilderFactory searchRequestBuilderFactory, Sorts sorts,
		SXPBlueprintService sxpBlueprintService,
		SXPElementService sxpElementService) {

		this.liferayPortletRequest = liferayPortletRequest;
		this.liferayPortletResponse = liferayPortletResponse;
		_queries = queries;
		_searcher = searcher;
		_searchRequestBuilderFactory = searchRequestBuilderFactory;
		_sorts = sorts;
		_sxpBlueprintService = sxpBlueprintService;
		_sxpElementService = sxpElementService;

		httpServletRequest = liferayPortletRequest.getHttpServletRequest();
		portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(
			liferayPortletRequest);
		tabs1 = ParamUtil.getString(
			liferayPortletRequest, "tabs1", "sxpBlueprints");
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

	protected PortletURL getIteratorURL() {
		return PortletURLBuilder.createRenderURL(
			liferayPortletResponse
		).setMVCRenderCommandName(
			getMVCRenderCommandName()
		).setTabs1(
			tabs1
		).build();
	}

	protected abstract String getMVCRenderCommandName();

	protected String getOrderByCol() {
		return ParamUtil.getString(
			liferayPortletRequest, "orderByCol", Field.MODIFIED_DATE);
	}

	protected String getOrderByType() {
		String orderByType = ParamUtil.getString(
			liferayPortletRequest, "orderByType");

		if (Validator.isNotNull(orderByType)) {
			return orderByType;
		}

		if (Objects.equals(getOrderByCol(), Field.TITLE)) {
			return "asc";
		}

		return "desc";
	}

	protected SearchContainer<R> getSearchContainer(String emptyResultsMessage)
		throws PortalException {

		SearchContainer<R> searchContainer = new SearchContainer<>(
			liferayPortletRequest, getIteratorURL(), null,
			emptyResultsMessage);

		searchContainer.setOrderByCol(getOrderByCol());
		searchContainer.setOrderByType(getOrderByType());
		searchContainer.setRowChecker(
			new EmptyOnClickRowChecker(liferayPortletResponse));

		return searchContainer;
	}

	protected final HttpServletRequest httpServletRequest;
	protected final LiferayPortletRequest liferayPortletRequest;
	protected final LiferayPortletResponse liferayPortletResponse;
	protected final PortalPreferences portalPreferences;
	protected final String tabs1;
	protected final ThemeDisplay themeDisplay;
	private final Queries _queries;
	private final Searcher _searcher;
	private final SearchRequestBuilderFactory _searchRequestBuilderFactory;
	private final Sorts _sorts;
	private final SXPBlueprintService _sxpBlueprintService;
	private final SXPElementService _sxpElementService;

	public void populateSXPBlueprintSearchContainer(
		long groupId, String orderByCol, String orderByType,
		PortletRequest portletRequest, Queries queries, Searcher searcher,
		SearchContainer<SXPBlueprint> searchContainer,
		SearchRequestBuilderFactory searchRequestBuilderFactory, Sorts sorts,
		int status, SXPBlueprintService sxpBlueprintService) {

		_populateSXPBlueprintSearchContainer(
			groupId, orderByCol, orderByType, portletRequest, queries, searcher,
			searchContainer, searchRequestBuilderFactory, sorts, status,
			sxpBlueprintService);
	}

	public void populateSXPElementSearchContainer(
		long groupId, String orderByCol, String orderByType,
		PortletRequest portletRequest, Queries queries, Searcher searcher,
		SearchContainer<SXPElement> searchContainer,
		SearchRequestBuilderFactory searchRequestBuilderFactory, Sorts sorts,
		int status, SXPElementService sxpElementService) {

		_populateSXPElementSearchContainer(
			groupId, orderByCol, orderByType, portletRequest, queries, searcher,
			searchContainer, searchRequestBuilderFactory, sorts, status,
			sxpElementService);
	}

	private void _addGroupFilterClause(
		BooleanQuery booleanQuery, long groupId, Queries queries) {

		TermQuery groupQuery = queries.term(Field.GROUP_ID, groupId);

		booleanQuery.addFilterQueryClauses(groupQuery);
	}

	private void _addSearchClauses(
		BooleanQuery booleanQuery, String keywords, String languageId,
		Queries queries) {

		if (Validator.isBlank(keywords)) {
			booleanQuery.addMustQueryClauses(queries.matchAll());
		}
		else {
			booleanQuery.addMustQueryClauses(
				queries.multiMatch(keywords, _getSearchFields(languageId)));
		}
	}

	private void _addStatusFilterClause(
		BooleanQuery booleanQuery, Queries queries, int status) {

		booleanQuery.addFilterQueryClauses(queries.term(Field.STATUS, status));
	}

	protected abstract Class<?> getModelIndexerClass();

	private SearchRequest _createElementSearchRequest(
		PortletRequest portletRequest, long groupId, Queries queries,
		SearchRequestBuilderFactory searchRequestBuilderFactory, Sorts sorts,
		int status, int type, int start, int size, String orderByCol,
		String orderByType) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String languageId = themeDisplay.getLanguageId();

		BooleanQuery booleanQuery = queries.booleanQuery();

		_addGroupFilterClause(booleanQuery, groupId, queries);

		_addSearchClauses(
			booleanQuery, ParamUtil.getString(portletRequest, "keywords"),
			languageId, queries);

		if (status != WorkflowConstants.STATUS_ANY) {
			_addStatusFilterClause(booleanQuery, queries, status);
		}

		processBooleanQuery(booleanQuery, portletRequest, queries);

		return searchRequestBuilderFactory.builder(
		).companyId(
			themeDisplay.getCompanyId()
		).from(
			start
		).modelIndexerClasses(
			getModelIndexerClass()
		).query(
			booleanQuery
		).size(
			size
		).addSort(
			_getSort(orderByCol, orderByType, languageId, sorts)
		).build();
	}

	protected abstract void processBooleanQuery(
		BooleanQuery booleanQuery, PortletRequest portletRequest, Queries queries);

	private SearchRequest _createSXPBlueprintSearchRequest(
		PortletRequest portletRequest, long groupId, Queries queries,
		SearchRequestBuilderFactory searchRequestBuilderFactory, Sorts sorts,
		int status, int start, int size, String orderByCol,
		String orderByType) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String languageId = themeDisplay.getLanguageId();

		BooleanQuery booleanQuery = queries.booleanQuery();

		_addGroupFilterClause(booleanQuery, groupId, queries);

		if (status != WorkflowConstants.STATUS_ANY) {
			_addStatusFilterClause(booleanQuery, queries, status);
		}

		_addSearchClauses(
			booleanQuery, ParamUtil.getString(portletRequest, "keywords"),
			languageId, queries);

		processBooleanQuery(booleanQuery, portletRequest, queries);

		return searchRequestBuilderFactory.builder(
		).companyId(
			themeDisplay.getCompanyId()
		).from(
			start
		).modelIndexerClasses(
			getModelIndexerClass()
		).query(
			booleanQuery
		).size(
			size
		).addSort(
			_getSort(orderByCol, orderByType, languageId, sorts)
		).build();
	}

	private long _getEntryClassPK(SearchHit searchHit) {
		Document document = searchHit.getDocument();

		return document.getLong(Field.ENTRY_CLASS_PK);
	}

	private Set<String> _getSearchFields(String languageId) {
		Set<String> fields = new HashSet<>();

		fields.add(_getTitleField(languageId));
		fields.add(
			LocalizationUtil.getLocalizedName(Field.DESCRIPTION, languageId));

		return fields;
	}

	private Sort _getSort(
		String orderByCol, String orderByType, String languageId, Sorts sorts) {

		SortOrder sortOrder = SortOrder.ASC;

		if (orderByType.equals("desc")) {
			sortOrder = SortOrder.DESC;
		}

		if (Objects.equals(orderByCol, Field.TITLE)) {
			return sorts.field(
				_getTitleField(languageId) + "_String_sortable", sortOrder);
		}

		return sorts.field(orderByCol, sortOrder);
	}

	private String _getTitleField(String languageId) {
		return LocalizationUtil.getLocalizedName(
			"localized_" + Field.TITLE, languageId);
	}

	private void _populateSXPBlueprintSearchContainer(
		long groupId, String orderByCol, String orderByType,
		PortletRequest portletRequest, Queries queries, Searcher searcher,
		SearchContainer<SXPBlueprint> searchContainer,
		SearchRequestBuilderFactory searchRequestBuilderFactory, Sorts sorts,
		int status, SXPBlueprintService sxpBlueprintService) {

		SearchRequest searchRequest = _createSXPBlueprintSearchRequest(
			portletRequest, groupId, queries, searchRequestBuilderFactory,
			sorts, status, searchContainer.getStart(),
			searchContainer.getDelta(), orderByCol, orderByType);

		SearchResponse searchResponse = searcher.search(searchRequest);

		SearchHits searchHits = searchResponse.getSearchHits();

		searchContainer.setTotal(
			GetterUtil.getInteger(searchHits.getTotalHits()));

		List<SearchHit> hits = searchHits.getSearchHits();

		Stream<SearchHit> stream = hits.stream();

		searchContainer.setResults(
			stream.map(
				searchHit -> _toSXPBlueprintOptional(
					searchHit, sxpBlueprintService)
			).filter(
				Optional::isPresent
			).map(
				Optional::get
			).collect(
				Collectors.toList()
			));
	}

	private void _populateSXPElementSearchContainer(
		long groupId, String orderByCol, String orderByType,
		PortletRequest portletRequest, Queries queries, Searcher searcher,
		SearchContainer<SXPElement> searchContainer,
		SearchRequestBuilderFactory searchRequestBuilderFactory, Sorts sorts,
		int status, SXPElementService sxpElementService) {

		SearchRequest searchRequest = _createElementSearchRequest(
			portletRequest, groupId, queries, searchRequestBuilderFactory,
			sorts, status,
			ParamUtil.getInteger(portletRequest, "sxpElementType"),
			searchContainer.getStart(), searchContainer.getDelta(), orderByCol,
			orderByType);

		SearchResponse searchResponse = searcher.search(searchRequest);

		SearchHits searchHits = searchResponse.getSearchHits();

		searchContainer.setTotal(
			GetterUtil.getInteger(searchHits.getTotalHits()));

		List<SearchHit> hits = searchHits.getSearchHits();

		Stream<SearchHit> stream = hits.stream();

		searchContainer.setResults(
			stream.map(
				searchHit -> _toSXPElementOptional(searchHit, sxpElementService)
			).filter(
				Optional::isPresent
			).map(
				Optional::get
			).collect(
				Collectors.toList()
			));
	}

	private Optional<SXPBlueprint> _toSXPBlueprintOptional(
		SearchHit searchHit, SXPBlueprintService sxpBlueprintService) {

		long entryClassPK = _getEntryClassPK(searchHit);

		try {
			return Optional.of(
				sxpBlueprintService.getSXPBlueprint(entryClassPK));
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Search index is stale and contains a non-existent " +
						"SXPBlueprint entry " + entryClassPK);
			}
		}

		return Optional.empty();
	}

	private Optional<SXPElement> _toSXPElementOptional(
		SearchHit searchHit, SXPElementService sxpElementService) {

		long entryClassPK = _getEntryClassPK(searchHit);

		try {
			return Optional.of(sxpElementService.getSXPElement(entryClassPK));
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Search index is stale and contains a non-existent " +
						"SXPElement entry " + entryClassPK);
			}
		}

		return Optional.empty();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseDisplayContext.class);

}