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

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.ClassType;
import com.liferay.asset.kernel.model.ClassTypeReader;
import com.liferay.depot.util.SiteConnectedGroupGroupProviderUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.item.selector.criteria.InfoItemItemSelectorReturnType;
import com.liferay.item.selector.criteria.info.item.criterion.InfoItemItemSelectorCriterion;
import com.liferay.journal.constants.JournalArticleConstants;
import com.liferay.journal.constants.JournalFolderConstants;
import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalArticleServiceUtil;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.journal.service.JournalFolderServiceUtil;
import com.liferay.journal.util.comparator.FolderArticleArticleIdComparator;
import com.liferay.journal.util.comparator.FolderArticleModifiedDateComparator;
import com.liferay.journal.util.comparator.FolderArticleTitleComparator;
import com.liferay.journal.web.internal.configuration.JournalWebConfiguration;
import com.liferay.journal.web.internal.item.selector.JournalArticleItemSelectorView;
import com.liferay.journal.web.internal.search.JournalSearcher;
import com.liferay.journal.web.internal.util.JournalPortletUtil;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.bean.BeanParamUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.portlet.SearchDisplayStyleUtil;
import com.liferay.portal.kernel.portlet.SearchOrderByUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupServiceUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.BreadcrumbEntry;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class JournalArticleItemSelectorViewDisplayContext {

	public JournalArticleItemSelectorViewDisplayContext(
		HttpServletRequest httpServletRequest,
		InfoItemItemSelectorCriterion infoItemItemSelectorCriterion,
		String itemSelectedEventName,
		JournalArticleItemSelectorView journalArticleItemSelectorView,
		JournalWebConfiguration journalWebConfiguration, PortletURL portletURL,
		boolean search) {

		_httpServletRequest = httpServletRequest;
		_infoItemItemSelectorCriterion = infoItemItemSelectorCriterion;
		_itemSelectedEventName = itemSelectedEventName;
		_journalArticleItemSelectorView = journalArticleItemSelectorView;
		_journalWebConfiguration = journalWebConfiguration;
		_portletURL = portletURL;
		_search = search;

		_portletRequest = (PortletRequest)httpServletRequest.getAttribute(
			JavaConstants.JAVAX_PORTLET_REQUEST);
		_portletResponse = (RenderResponse)httpServletRequest.getAttribute(
			JavaConstants.JAVAX_PORTLET_RESPONSE);
		_themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public String getDDMStructureKey() {
		if (_ddmStructureKey != null) {
			return _ddmStructureKey;
		}

		String ddmStructureKey = ParamUtil.getString(
			_httpServletRequest, "ddmStructureKey");

		if (Validator.isNull(ddmStructureKey)) {
			ddmStructureKey = _infoItemItemSelectorCriterion.getItemSubtype();
		}

		_ddmStructureKey = ddmStructureKey;

		return _ddmStructureKey;
	}

	public String getDisplayStyle() {
		if (Validator.isNotNull(_displayStyle)) {
			return _displayStyle;
		}

		_displayStyle = SearchDisplayStyleUtil.getDisplayStyle(
			_httpServletRequest, JournalPortletKeys.JOURNAL,
			"item-selector-display-style", "descriptive");

		return _displayStyle;
	}

	public String getGroupCssIcon(long groupId) throws PortalException {
		Group group = GroupServiceUtil.getGroup(groupId);

		return group.getIconCssClass();
	}

	public String getGroupLabel(long groupId, Locale locale)
		throws PortalException {

		Group group = GroupServiceUtil.getGroup(groupId);

		return group.getDescriptiveName(locale);
	}

	public String getItemSelectedEventName() {
		return _itemSelectedEventName;
	}

	public Map<String, Object> getJournalArticleContext(
		JournalArticle journalArticle) {

		return HashMapBuilder.<String, Object>put(
			"returnType", InfoItemItemSelectorReturnType.class.getName()
		).put(
			"value",
			() -> {
				DDMStructure ddmStructure =
					DDMStructureLocalServiceUtil.fetchStructure(
						journalArticle.getGroupId(),
						PortalUtil.getClassNameId(JournalArticle.class),
						journalArticle.getDDMStructureKey(), true);

				return JSONUtil.put(
					"className", JournalArticle.class.getName()
				).put(
					"classNameId",
					PortalUtil.getClassNameId(JournalArticle.class.getName())
				).put(
					"classPK", journalArticle.getResourcePrimKey()
				).put(
					"classTypeId", _getClassTypeId(ddmStructure)
				).put(
					"subtype", _getSubtype(ddmStructure)
				).put(
					"title",
					journalArticle.getTitle(_themeDisplay.getLocale(), true)
				).put(
					"titleMap", journalArticle.getTitleMap()
				).put(
					"type",
					ResourceActionsUtil.getModelResource(
						_themeDisplay.getLocale(),
						JournalArticle.class.getName())
				).toString();
			}
		).build();
	}

	public String getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_httpServletRequest, "keywords");

		return _keywords;
	}

	public JournalArticle getLatestArticle(JournalArticle journalArticle) {
		JournalArticle latestArticle =
			JournalArticleLocalServiceUtil.fetchLatestArticle(
				journalArticle.getGroupId(), journalArticle.getArticleId(),
				WorkflowConstants.STATUS_ANY);

		if (latestArticle != null) {
			return latestArticle;
		}

		return journalArticle;
	}

	public List<BreadcrumbEntry> getPortletBreadcrumbEntries()
		throws Exception {

		List<BreadcrumbEntry> breadcrumbEntries = new ArrayList<>();

		breadcrumbEntries.add(_getSiteBreadcrumb());

		breadcrumbEntries.add(_getHomeBreadcrumb());

		JournalFolder folder = _getFolder();

		if (folder == null) {
			return breadcrumbEntries;
		}

		List<JournalFolder> ancestorFolders = folder.getAncestors();

		Collections.reverse(ancestorFolders);

		PortletURL portletURL = PortletURLBuilder.create(
			getPortletURL()
		).setParameter(
			"folderId", JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID
		).buildPortletURL();

		for (JournalFolder ancestorFolder : ancestorFolders) {
			BreadcrumbEntry folderBreadcrumbEntry = new BreadcrumbEntry();

			folderBreadcrumbEntry.setTitle(ancestorFolder.getName());

			portletURL.setParameter(
				"folderId", String.valueOf(ancestorFolder.getFolderId()));

			folderBreadcrumbEntry.setURL(portletURL.toString());

			breadcrumbEntries.add(folderBreadcrumbEntry);
		}

		if (folder.getFolderId() !=
				JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID) {

			BreadcrumbEntry folderBreadcrumbEntry = new BreadcrumbEntry();

			JournalFolder unescapedFolder = folder.toUnescapedModel();

			folderBreadcrumbEntry.setTitle(unescapedFolder.getName());

			portletURL.setParameter(
				"folderId", String.valueOf(folder.getFolderId()));

			folderBreadcrumbEntry.setURL(portletURL.toString());

			breadcrumbEntries.add(folderBreadcrumbEntry);
		}

		return breadcrumbEntries;
	}

	public PortletURL getPortletURL() throws PortletException {
		return PortletURLBuilder.create(
			PortletURLUtil.clone(
				_portletURL,
				PortalUtil.getLiferayPortletResponse(_portletResponse))
		).setParameter(
			"displayStyle", getDisplayStyle()
		).setParameter(
			"selectedTab", _getTitle(_httpServletRequest.getLocale())
		).buildPortletURL();
	}

	public SearchContainer<?> getSearchContainer() throws Exception {
		if (_articleSearchContainer != null) {
			return _articleSearchContainer;
		}

		if (Validator.isNotNull(getDDMStructureKey()) && !isSearch()) {
			SearchContainer<JournalArticle> articleSearchContainer =
				new SearchContainer<>(
					_portletRequest, getPortletURL(), null, null);

			articleSearchContainer.setOrderByCol(_getOrderByCol());
			articleSearchContainer.setOrderByComparator(
				JournalPortletUtil.getArticleOrderByComparator(
					_getOrderByCol(), _getOrderByType()));
			articleSearchContainer.setOrderByType(_getOrderByType());
			articleSearchContainer.setResultsAndTotal(
				() -> JournalArticleServiceUtil.getArticlesByStructureId(
					_getGroupId(), getDDMStructureKey(),
					WorkflowConstants.STATUS_APPROVED,
					articleSearchContainer.getStart(),
					articleSearchContainer.getEnd(),
					articleSearchContainer.getOrderByComparator()),
				JournalArticleServiceUtil.getArticlesCountByStructureId(
					_getGroupId(), getDDMStructureKey(),
					WorkflowConstants.STATUS_APPROVED));

			_articleSearchContainer = articleSearchContainer;

			return _articleSearchContainer;
		}

		PortletURL portletURL = PortletURLBuilder.create(
			getPortletURL()
		).setParameter(
			"folderId", _getFolderId()
		).buildPortletURL();

		SearchContainer<Object> articleAndFolderSearchContainer =
			new SearchContainer<>(_portletRequest, portletURL, null, null);

		articleAndFolderSearchContainer.setOrderByCol(_getOrderByCol());
		articleAndFolderSearchContainer.setOrderByType(_getOrderByType());

		if (isSearch()) {
			List<Long> folderIds = new ArrayList<>(1);

			if (_getFolderId() !=
					JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID) {

				folderIds.add(_getFolderId());
			}

			boolean orderByAsc = false;

			if (Objects.equals(_getOrderByType(), "asc")) {
				orderByAsc = true;
			}

			Sort sort = null;

			if (Objects.equals(_getOrderByCol(), "id")) {
				sort = new Sort(
					Field.getSortableFieldName(Field.ARTICLE_ID),
					Sort.STRING_TYPE, !orderByAsc);
			}
			else if (Objects.equals(_getOrderByCol(), "modified-date")) {
				sort = new Sort(
					Field.MODIFIED_DATE, Sort.LONG_TYPE, !orderByAsc);
			}
			else if (Objects.equals(_getOrderByCol(), "relevance")) {
				sort = new Sort(null, Sort.SCORE_TYPE, false);
			}
			else if (Objects.equals(_getOrderByCol(), "title")) {
				sort = new Sort(
					Field.getSortableFieldName(
						"localized_title_" + _themeDisplay.getLanguageId()),
					!orderByAsc);
			}

			Indexer<?> indexer = JournalSearcher.getInstance();

			SearchContext searchContext = buildSearchContext(
				folderIds, articleAndFolderSearchContainer.getStart(),
				articleAndFolderSearchContainer.getEnd(), sort);

			Hits hits = indexer.search(searchContext);

			List<Object> results = new ArrayList<>();

			Document[] documents = hits.getDocs();

			for (Document document : documents) {
				String className = document.get(Field.ENTRY_CLASS_NAME);
				long classPK = GetterUtil.getLong(
					document.get(Field.ENTRY_CLASS_PK));

				if (className.equals(JournalArticle.class.getName())) {
					results.add(
						JournalArticleLocalServiceUtil.fetchLatestArticle(
							classPK, WorkflowConstants.STATUS_ANY, false));
				}
				else if (className.equals(JournalFolder.class.getName())) {
					results.add(
						JournalFolderLocalServiceUtil.getFolder(classPK));
				}
			}

			articleAndFolderSearchContainer.setResultsAndTotal(
				() -> results, hits.getLength());
		}
		else {
			articleAndFolderSearchContainer.setResultsAndTotal(
				() -> {
					OrderByComparator<Object> folderOrderByComparator = null;

					boolean orderByAsc = false;

					if (Objects.equals(_getOrderByType(), "asc")) {
						orderByAsc = true;
					}

					if (Objects.equals(_getOrderByCol(), "id")) {
						folderOrderByComparator =
							new FolderArticleArticleIdComparator(orderByAsc);
					}
					else if (Objects.equals(
								_getOrderByCol(), "modified-date")) {

						folderOrderByComparator =
							new FolderArticleModifiedDateComparator(orderByAsc);
					}
					else if (Objects.equals(_getOrderByCol(), "title")) {
						folderOrderByComparator =
							new FolderArticleTitleComparator(orderByAsc);
					}

					return JournalFolderServiceUtil.getFoldersAndArticles(
						_getGroupId(), 0, _getFolderId(),
						_infoItemItemSelectorCriterion.getStatus(),
						_themeDisplay.getLocale(),
						articleAndFolderSearchContainer.getStart(),
						articleAndFolderSearchContainer.getEnd(),
						folderOrderByComparator);
				},
				JournalFolderServiceUtil.getFoldersAndArticlesCount(
					_getGroupId(), 0, _getFolderId(),
					_infoItemItemSelectorCriterion.getStatus()));
		}

		_articleSearchContainer = articleAndFolderSearchContainer;

		return _articleSearchContainer;
	}

	public boolean isSearch() {
		if (_isEverywhereScopeFilter()) {
			return true;
		}

		return _search;
	}

	public boolean isSearchEverywhere() {
		if (_searchEverywhere != null) {
			return _searchEverywhere;
		}

		if (Objects.equals(
				ParamUtil.getString(_httpServletRequest, "scope"),
				"everywhere")) {

			_searchEverywhere = true;
		}
		else {
			_searchEverywhere = false;
		}

		return _searchEverywhere;
	}

	public boolean showArticleId() {
		if (!_journalWebConfiguration.journalArticleForceAutogenerateId() ||
			_journalWebConfiguration.journalArticleShowId()) {

			return true;
		}

		return false;
	}

	protected SearchContext buildSearchContext(
			List<Long> folderIds, int start, int end, Sort sort)
		throws PortalException {

		SearchContext searchContext = new SearchContext();

		searchContext.setAndSearch(false);
		searchContext.setAttribute(Field.ARTICLE_ID, getKeywords());
		searchContext.setAttribute(
			Field.CLASS_NAME_ID, JournalArticleConstants.CLASS_NAME_ID_DEFAULT);
		searchContext.setAttribute(Field.CONTENT, getKeywords());
		searchContext.setAttribute(Field.DESCRIPTION, getKeywords());
		searchContext.setAttribute(
			Field.STATUS, _infoItemItemSelectorCriterion.getStatus());
		searchContext.setAttribute(Field.TITLE, getKeywords());
		searchContext.setAttribute("ddmStructureKey", getDDMStructureKey());
		searchContext.setAttribute("head", Boolean.TRUE);
		searchContext.setAttribute("latest", Boolean.TRUE);
		searchContext.setAttribute(
			"params",
			LinkedHashMapBuilder.<String, Object>put(
				"expandoAttributes", getKeywords()
			).put(
				"keywords", getKeywords()
			).build());
		searchContext.setAttribute("showNonindexable", Boolean.TRUE);
		searchContext.setCompanyId(_themeDisplay.getCompanyId());
		searchContext.setEnd(end);
		searchContext.setFolderIds(folderIds);
		searchContext.setGroupIds(_getGroupIds());
		searchContext.setIncludeInternalAssetCategories(true);
		searchContext.setKeywords(getKeywords());

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		if (sort != null) {
			searchContext.setSorts(sort);
		}

		searchContext.setStart(start);

		return searchContext;
	}

	private long _getClassTypeId(DDMStructure ddmStructure) {
		if (ddmStructure == null) {
			return 0;
		}

		return ddmStructure.getStructureId();
	}

	private JournalFolder _getFolder() {
		if (_folder != null) {
			return _folder;
		}

		_folder = JournalFolderLocalServiceUtil.fetchFolder(
			ParamUtil.getLong(_httpServletRequest, "folderId"));

		return _folder;
	}

	private long _getFolderId() {
		if (_folderId != null) {
			return _folderId;
		}

		_folderId = BeanParamUtil.getLong(
			_getFolder(), _httpServletRequest, "folderId",
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		return _folderId;
	}

	private long _getGroupId() {
		return ParamUtil.getLong(
			_portletRequest, "groupId", _themeDisplay.getScopeGroupId());
	}

	private long[] _getGroupIds() throws PortalException {
		if (_isEverywhereScopeFilter()) {
			return SiteConnectedGroupGroupProviderUtil.
				getCurrentAndAncestorSiteAndDepotGroupIds(
					_themeDisplay.getScopeGroupId());
		}

		return PortalUtil.getCurrentAndAncestorSiteGroupIds(
			_themeDisplay.getScopeGroupId());
	}

	private BreadcrumbEntry _getHomeBreadcrumb() throws Exception {
		BreadcrumbEntry breadcrumbEntry = new BreadcrumbEntry();

		Group group = GroupLocalServiceUtil.getGroup(_getGroupId());

		breadcrumbEntry.setTitle(
			group.getDescriptiveName(_themeDisplay.getLocale()));

		breadcrumbEntry.setURL(
			PortletURLBuilder.create(
				getPortletURL()
			).setParameter(
				"folderId", JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID
			).buildString());

		return breadcrumbEntry;
	}

	private String _getOrderByCol() {
		if (Validator.isNotNull(_orderByCol)) {
			return _orderByCol;
		}

		String defaultOrderByCol = "modified-date";

		if (isSearch()) {
			defaultOrderByCol = "relevance";
		}

		_orderByCol = SearchOrderByUtil.getOrderByCol(
			_httpServletRequest, JournalPortletKeys.JOURNAL,
			"item-selector-order-by-col", defaultOrderByCol);

		return _orderByCol;
	}

	private String _getOrderByType() {
		if (_orderByType != null) {
			return _orderByType;
		}

		if (Objects.equals(_getOrderByCol(), "relevance")) {
			return "desc";
		}

		_orderByType = SearchOrderByUtil.getOrderByType(
			_httpServletRequest, JournalPortletKeys.JOURNAL,
			"item-selector-order-by-type", "asc");

		return _orderByType;
	}

	private BreadcrumbEntry _getSiteBreadcrumb() throws Exception {
		BreadcrumbEntry breadcrumbEntry = new BreadcrumbEntry();

		breadcrumbEntry.setTitle(
			LanguageUtil.get(_httpServletRequest, "sites-and-libraries"));
		breadcrumbEntry.setURL(
			PortletURLBuilder.create(
				getPortletURL()
			).setParameter(
				"groupType", "site"
			).setParameter(
				"showGroupSelector", true
			).buildString());

		return breadcrumbEntry;
	}

	private String _getSubtype(DDMStructure ddmStructure)
		throws PortalException {

		if (ddmStructure == null) {
			return StringPool.BLANK;
		}

		AssetRendererFactory<?> assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				JournalArticle.class.getName());

		ClassTypeReader classTypeReader =
			assetRendererFactory.getClassTypeReader();

		ClassType classType = classTypeReader.getClassType(
			ddmStructure.getStructureId(), _themeDisplay.getLocale());

		return classType.getName();
	}

	private String _getTitle(Locale locale) {
		return _journalArticleItemSelectorView.getTitle(locale);
	}

	private boolean _isEverywhereScopeFilter() {
		if (Objects.equals(
				ParamUtil.getString(_httpServletRequest, "scope"),
				"everywhere")) {

			return true;
		}

		return false;
	}

	private SearchContainer<?> _articleSearchContainer;
	private String _ddmStructureKey;
	private String _displayStyle;
	private JournalFolder _folder;
	private Long _folderId;
	private final HttpServletRequest _httpServletRequest;
	private final InfoItemItemSelectorCriterion _infoItemItemSelectorCriterion;
	private final String _itemSelectedEventName;
	private final JournalArticleItemSelectorView
		_journalArticleItemSelectorView;
	private final JournalWebConfiguration _journalWebConfiguration;
	private String _keywords;
	private String _orderByCol;
	private String _orderByType;
	private final PortletRequest _portletRequest;
	private final PortletResponse _portletResponse;
	private final PortletURL _portletURL;
	private final boolean _search;
	private Boolean _searchEverywhere;
	private final ThemeDisplay _themeDisplay;

}