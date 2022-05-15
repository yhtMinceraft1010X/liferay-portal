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

package com.liferay.document.library.web.internal.display.context;

import com.liferay.asset.auto.tagger.configuration.AssetAutoTaggerConfiguration;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryServiceUtil;
import com.liferay.asset.kernel.service.persistence.AssetEntryQuery;
import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.kernel.exception.NoSuchFolderException;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileEntryConstants;
import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.document.library.kernel.model.DLFileShortcutConstants;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryTypeLocalServiceUtil;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.document.library.kernel.util.comparator.RepositoryModelModifiedDateComparator;
import com.liferay.document.library.kernel.versioning.VersioningStrategy;
import com.liferay.document.library.web.internal.display.context.helper.DLPortletInstanceSettingsHelper;
import com.liferay.document.library.web.internal.display.context.helper.DLRequestHelper;
import com.liferay.document.library.web.internal.settings.DLPortletInstanceSettings;
import com.liferay.document.library.web.internal.util.DLFolderUtil;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.criteria.FolderItemSelectorReturnType;
import com.liferay.item.selector.criteria.folder.criterion.FolderItemSelectorCriterion;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Repository;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.repository.capabilities.TrashCapability;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.repository.model.RepositoryEntry;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.RelatedSearchResult;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchResult;
import com.liferay.portal.kernel.search.SearchResultUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.RepositoryLocalServiceUtil;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.trash.TrashHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Alejandro Tardín
 */
public class DLAdminDisplayContext {

	public DLAdminDisplayContext(
		AssetAutoTaggerConfiguration assetAutoTaggerConfiguration,
		HttpServletRequest httpServletRequest,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		VersioningStrategy versioningStrategy, TrashHelper trashHelper) {

		_assetAutoTaggerConfiguration = assetAutoTaggerConfiguration;
		_httpServletRequest = httpServletRequest;
		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;
		_versioningStrategy = versioningStrategy;
		_trashHelper = trashHelper;

		_dlRequestHelper = new DLRequestHelper(_httpServletRequest);

		_dlPortletInstanceSettings =
			_dlRequestHelper.getDLPortletInstanceSettings();

		_dlPortletInstanceSettingsHelper = new DLPortletInstanceSettingsHelper(
			_dlRequestHelper);

		_httpSession = httpServletRequest.getSession();

		_portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(
			httpServletRequest);

		_themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		_permissionChecker = _themeDisplay.getPermissionChecker();

		_computeFolders();
	}

	public String getDisplayStyle() {
		if (_displayStyle != null) {
			return _displayStyle;
		}

		String displayStyle = ParamUtil.getString(
			_httpServletRequest, "displayStyle");

		String[] displayViews = _dlPortletInstanceSettings.getDisplayViews();

		if (Validator.isNull(displayStyle)) {
			displayStyle = _getPortletPreference(
				"display-style", PropsValues.DL_DEFAULT_DISPLAY_VIEW);
		}
		else {
			if (ArrayUtil.contains(displayViews, displayStyle)) {
				_setPortletPreference("display-style", displayStyle);

				_httpServletRequest.setAttribute(
					WebKeys.SINGLE_PAGE_APPLICATION_CLEAR_CACHE, Boolean.TRUE);
			}
		}

		if (!ArrayUtil.contains(displayViews, displayStyle)) {
			displayStyle = displayViews[0];
		}

		_displayStyle = displayStyle;

		return _displayStyle;
	}

	public Folder getFolder() {
		return _folder;
	}

	public long getFolderId() {
		return _folderId;
	}

	public String getNavigation() {
		if (_navigation != null) {
			return _navigation;
		}

		_navigation = ParamUtil.getString(
			_httpServletRequest, "navigation", "home");

		return _navigation;
	}

	public String getOrderByCol() {
		if (_orderByCol != null) {
			return _orderByCol;
		}

		String orderByCol = ParamUtil.getString(
			_httpServletRequest, "orderByCol");

		long fileEntryTypeId = ParamUtil.getLong(
			_httpServletRequest, "fileEntryTypeId", -1);

		if (orderByCol.equals("downloads") && (fileEntryTypeId >= 0)) {
			orderByCol = "modifiedDate";
		}

		if (Validator.isNull(orderByCol)) {
			orderByCol = _getPortletPreference("order-by-col", "modifiedDate");
		}
		else {
			_setPortletPreference("order-by-col", orderByCol);
		}

		_orderByCol = orderByCol;

		return _orderByCol;
	}

	public String getOrderByType() {
		if (Validator.isNotNull(_orderByType)) {
			return _orderByType;
		}

		String orderByType = ParamUtil.getString(
			_httpServletRequest, "orderByType");

		if (Validator.isNull(orderByType)) {
			orderByType = _getPortletPreference("order-by-type", "desc");
		}
		else {
			_setPortletPreference("order-by-type", orderByType);
		}

		_orderByType = orderByType;

		return _orderByType;
	}

	public String getRememberCheckBoxStateURLRegex() {
		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		if (!DLPortletKeys.DOCUMENT_LIBRARY.equals(
				portletDisplay.getRootPortletId())) {

			return StringBundler.concat(
				"^(?!.*", portletDisplay.getNamespace(),
				"redirect).*(folderId=", _folderId, ")");
		}

		if (_folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			return "^[^?]+/" + portletDisplay.getInstanceId() + "\\?";
		}

		return StringBundler.concat(
			"^[^?]+/", portletDisplay.getInstanceId(), "/view/", _folderId,
			"\\?");
	}

	public long getRepositoryId() {
		if (_repositoryId != 0) {
			return _repositoryId;
		}

		long repositoryId = 0;

		Folder folder = getFolder();

		if (folder != null) {
			repositoryId = folder.getRepositoryId();
		}
		else {
			repositoryId = _dlPortletInstanceSettings.getSelectedRepositoryId();
		}

		if (repositoryId == 0) {
			repositoryId = ParamUtil.getLong(
				_httpServletRequest, "repositoryId",
				_themeDisplay.getScopeGroupId());
		}

		_repositoryId = repositoryId;

		return _repositoryId;
	}

	public long getRootFolderId() {
		return _rootFolderId;
	}

	public String getRootFolderName() {
		return _rootFolderName;
	}

	public SearchContainer<RepositoryEntry> getSearchContainer() {
		if (_searchContainer == null) {
			try {
				if (isSearch()) {
					_searchContainer = _getSearchSearchContainer();
				}
				else {
					_searchContainer = _getDLSearchContainer();
				}
			}
			catch (PortalException portalException) {
				throw new SystemException(portalException);
			}
		}

		return _searchContainer;
	}

	public PortletURL getSearchSearchContainerURL() {
		return PortletURLBuilder.createRenderURL(
			_liferayPortletResponse
		).setMVCRenderCommandName(
			"/document_library/search"
		).setRedirect(
			ParamUtil.getString(_httpServletRequest, "redirect")
		).setKeywords(
			ParamUtil.getString(_httpServletRequest, "keywords")
		).setParameter(
			"searchFolderId",
			ParamUtil.getLong(_httpServletRequest, "searchFolderId")
		).buildPortletURL();
	}

	public long getSelectedRepositoryId() {
		if (_selectedRepositoryId != 0) {
			return _selectedRepositoryId;
		}

		long repositoryId =
			_dlPortletInstanceSettings.getSelectedRepositoryId();

		if (repositoryId != 0) {
			_selectedRepositoryId = repositoryId;

			return _selectedRepositoryId;
		}

		_selectedRepositoryId = getRepositoryId();

		return _selectedRepositoryId;
	}

	public PortletURL getSelectFolderURL(HttpServletRequest httpServletRequest)
		throws PortalException {

		ItemSelector itemSelector =
			(ItemSelector)httpServletRequest.getAttribute(
				ItemSelector.class.getName());

		FolderItemSelectorCriterion folderItemSelectorCriterion =
			new FolderItemSelectorCriterion();

		folderItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			new FolderItemSelectorReturnType());
		folderItemSelectorCriterion.setFolderId(getRootFolderId());
		folderItemSelectorCriterion.setIgnoreRootFolder(true);
		folderItemSelectorCriterion.setRepositoryId(getSelectedRepositoryId());
		folderItemSelectorCriterion.setSelectedFolderId(getRootFolderId());
		folderItemSelectorCriterion.setSelectedRepositoryId(
			getSelectedRepositoryId());
		folderItemSelectorCriterion.setShowGroupSelector(true);
		folderItemSelectorCriterion.setShowMountFolder(false);

		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		long groupId = getSelectedRepositoryId();

		Repository repository = RepositoryLocalServiceUtil.fetchRepository(
			getSelectedRepositoryId());

		if (repository != null) {
			groupId = repository.getGroupId();
		}

		return itemSelector.getItemSelectorURL(
			RequestBackedPortletURLFactoryUtil.create(httpServletRequest),
			GroupLocalServiceUtil.getGroup(
				GetterUtil.getLong(groupId, _themeDisplay.getScopeGroupId())),
			_themeDisplay.getScopeGroupId(),
			portletDisplay.getNamespace() + "folderSelected",
			folderItemSelectorCriterion);
	}

	public boolean isAutoTaggingEnabled() {
		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-150762"))) {
			return false;
		}

		return _assetAutoTaggerConfiguration.isEnabled();
	}

	public boolean isDefaultFolderView() {
		return _defaultFolderView;
	}

	public boolean isRootFolderInTrash() {
		return _rootFolderInTrash;
	}

	public boolean isRootFolderNotFound() {
		return _rootFolderNotFound;
	}

	public boolean isSearch() {
		String mvcRenderCommandName = ParamUtil.getString(
			_httpServletRequest, "mvcRenderCommandName");

		return mvcRenderCommandName.equals("/document_library/search");
	}

	public boolean isUpdateAutoTags() {
		return _assetAutoTaggerConfiguration.isUpdateAutoTags();
	}

	public boolean isVersioningStrategyOverridable() {
		return _versioningStrategy.isOverridable();
	}

	private void _computeFolders() {
		try {
			_computeRootFolder();

			_folder = (Folder)_httpServletRequest.getAttribute(
				WebKeys.DOCUMENT_LIBRARY_FOLDER);

			if (_folder == null) {
				_folderId = getRootFolderId();
			}
			else {
				_folderId = _folder.getFolderId();
			}

			_defaultFolderView = false;

			if ((_folder == null) &&
				(_folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID)) {

				_defaultFolderView = true;
			}

			if (_defaultFolderView) {
				try {
					_folder = DLAppLocalServiceUtil.getFolder(_folderId);
				}
				catch (NoSuchFolderException noSuchFolderException) {
					_folderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;

					if (_log.isWarnEnabled()) {
						_log.warn(
							"Unable to get folder " + _folderId,
							noSuchFolderException);
					}
				}
			}
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	private void _computeRootFolder() {
		_rootFolderId = _dlPortletInstanceSettings.getRootFolderId();
		_rootFolderName = StringPool.BLANK;

		if (_rootFolderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			_rootFolderName = LanguageUtil.get(_httpServletRequest, "home");

			return;
		}

		try {
			Folder rootFolder = DLAppLocalServiceUtil.getFolder(_rootFolderId);

			_rootFolderName = rootFolder.getName();

			if (rootFolder.isRepositoryCapabilityProvided(
					TrashCapability.class)) {

				TrashCapability trashCapability =
					rootFolder.getRepositoryCapability(TrashCapability.class);

				_rootFolderInTrash = trashCapability.isInTrash(rootFolder);

				if (_rootFolderInTrash) {
					_rootFolderName = _trashHelper.getOriginalTitle(
						rootFolder.getName());
				}
			}

			DLFolderUtil.validateDepotFolder(
				_rootFolderId, rootFolder.getGroupId(),
				_themeDisplay.getScopeGroupId());
		}
		catch (NoSuchFolderException noSuchFolderException) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						"Could not find folder {folderId=", _rootFolderId, "}"),
					noSuchFolderException);
			}

			_rootFolderNotFound = true;
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	private SearchContainer<RepositoryEntry> _getDLSearchContainer()
		throws PortalException {

		String navigation = ParamUtil.getString(
			_httpServletRequest, "navigation", "home");

		String currentFolder = ParamUtil.getString(
			_httpServletRequest, "curFolder");
		String deltaFolder = ParamUtil.getString(
			_httpServletRequest, "deltaFolder");

		long fileEntryTypeId = ParamUtil.getLong(
			_httpServletRequest, "fileEntryTypeId", -1);

		String dlFileEntryTypeName = LanguageUtil.get(
			_httpServletRequest, "basic-document");

		int status = WorkflowConstants.STATUS_APPROVED;

		User user = _themeDisplay.getUser();

		if (_permissionChecker.isContentReviewer(
				user.getCompanyId(), _themeDisplay.getScopeGroupId())) {

			status = WorkflowConstants.STATUS_ANY;
		}

		long categoryId = ParamUtil.getLong(_httpServletRequest, "categoryId");
		String tagName = ParamUtil.getString(_httpServletRequest, "tag");

		boolean useAssetEntryQuery = false;

		if ((categoryId > 0) || Validator.isNotNull(tagName)) {
			useAssetEntryQuery = true;
		}

		PortletURL portletURL = _liferayPortletResponse.createRenderURL();

		long folderId = getFolderId();

		if (folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			portletURL.setParameter(
				"mvcRenderCommandName", "/document_library/view");
		}
		else {
			portletURL.setParameter(
				"mvcRenderCommandName", "/document_library/view_folder");
		}

		portletURL.setParameter("navigation", navigation);
		portletURL.setParameter("curFolder", currentFolder);
		portletURL.setParameter("deltaFolder", deltaFolder);
		portletURL.setParameter("folderId", String.valueOf(folderId));

		if (fileEntryTypeId >= 0) {
			portletURL.setParameter(
				"fileEntryTypeId", String.valueOf(fileEntryTypeId));

			if (fileEntryTypeId > 0) {
				DLFileEntryType dlFileEntryType =
					DLFileEntryTypeLocalServiceUtil.getFileEntryType(
						fileEntryTypeId);

				dlFileEntryTypeName = dlFileEntryType.getName(
					_httpServletRequest.getLocale());
			}
		}

		String emptyResultsMessage = null;

		if (fileEntryTypeId >= 0) {
			emptyResultsMessage = LanguageUtil.format(
				_httpServletRequest,
				"there-are-no-documents-or-media-files-of-type-x",
				HtmlUtil.escape(dlFileEntryTypeName));
		}
		else {
			emptyResultsMessage =
				"there-are-no-documents-or-media-files-in-this-folder";
		}

		SearchContainer<RepositoryEntry> dlSearchContainer =
			new SearchContainer<>(
				_liferayPortletRequest, null, null, "curEntry",
				_dlPortletInstanceSettings.getEntriesPerPage(), portletURL,
				null, emptyResultsMessage);

		dlSearchContainer.setHeaderNames(
			ListUtil.fromArray(
				_dlPortletInstanceSettingsHelper.getEntryColumns()));

		dlSearchContainer.setOrderByCol(getOrderByCol());

		boolean orderByModel = false;

		if (navigation.equals("home")) {
			orderByModel = true;
		}

		OrderByComparator<RepositoryEntry> orderByComparator =
			DLUtil.getRepositoryModelOrderByComparator(
				getOrderByCol(), getOrderByType(), orderByModel);

		if (navigation.equals("recent")) {
			orderByComparator = new RepositoryModelModifiedDateComparator();
		}

		dlSearchContainer.setOrderByComparator(orderByComparator);
		dlSearchContainer.setOrderByType(getOrderByType());

		List<RepositoryEntry> results = new ArrayList<>();

		if (fileEntryTypeId >= 0) {
			Indexer<?> indexer = IndexerRegistryUtil.getIndexer(
				DLFileEntryConstants.getClassName());

			SearchContext searchContext = SearchContextFactory.getInstance(
				_httpServletRequest);

			searchContext.setAttribute("paginationType", "none");
			searchContext.setEnd(dlSearchContainer.getEnd());

			if (folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
				searchContext.setFolderIds(new long[] {folderId});
			}

			int type = Sort.STRING_TYPE;
			String fieldName = getOrderByCol();

			if (Objects.equals(getOrderByCol(), "creationDate")) {
				fieldName = Field.CREATE_DATE;
				type = Sort.LONG_TYPE;
			}
			else if (Objects.equals(getOrderByCol(), "modifiedDate")) {
				fieldName = Field.MODIFIED_DATE;
				type = Sort.LONG_TYPE;
			}
			else if (Objects.equals(getOrderByCol(), "size")) {
				type = Sort.LONG_TYPE;
			}

			searchContext.setSorts(
				new Sort(
					fieldName, type,
					!StringUtil.equalsIgnoreCase(getOrderByType(), "asc")));

			searchContext.setStart(dlSearchContainer.getStart());

			Hits hits = indexer.search(searchContext);

			for (Document doc : hits.getDocs()) {
				long fileEntryId = GetterUtil.getLong(
					doc.get(Field.ENTRY_CLASS_PK));

				FileEntry fileEntry = null;

				try {
					fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEntryId);
				}
				catch (Exception exception) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							StringBundler.concat(
								"Documents and Media search index is stale ",
								"and contains file entry {", fileEntryId, "}"),
							exception);
					}

					continue;
				}

				results.add(fileEntry);
			}

			dlSearchContainer.setResultsAndTotal(
				() -> results, hits.getLength());
		}
		else {
			if (navigation.equals("home")) {
				if (useAssetEntryQuery) {
					long[] classNameIds = {
						PortalUtil.getClassNameId(
							DLFileEntryConstants.getClassName()),
						PortalUtil.getClassNameId(
							DLFileShortcutConstants.getClassName())
					};

					AssetEntryQuery assetEntryQuery = new AssetEntryQuery(
						classNameIds, dlSearchContainer);

					assetEntryQuery.setEnablePermissions(true);
					assetEntryQuery.setExcludeZeroViewCount(false);

					for (AssetEntry assetEntry :
							AssetEntryServiceUtil.getEntries(assetEntryQuery)) {

						if (Objects.equals(
								assetEntry.getClassName(),
								DLFileEntryConstants.getClassName())) {

							results.add(
								DLAppLocalServiceUtil.getFileEntry(
									assetEntry.getClassPK()));
						}
						else {
							results.add(
								DLAppLocalServiceUtil.getFileShortcut(
									assetEntry.getClassPK()));
						}
					}

					dlSearchContainer.setResultsAndTotal(
						() -> results,
						AssetEntryServiceUtil.getEntriesCount(assetEntryQuery));
				}
				else {
					long repositoryId = getRepositoryId();

					int dlAppStatus = status;

					dlSearchContainer.setResultsAndTotal(
						() ->
							(List)
								DLAppServiceUtil.
									getFoldersAndFileEntriesAndFileShortcuts(
										repositoryId, folderId, dlAppStatus,
										true, dlSearchContainer.getStart(),
										dlSearchContainer.getEnd(),
										dlSearchContainer.
											getOrderByComparator()),
						DLAppServiceUtil.
							getFoldersAndFileEntriesAndFileShortcutsCount(
								repositoryId, folderId, dlAppStatus, true));
				}
			}
			else if (navigation.equals("mine") || navigation.equals("recent")) {
				long groupFileEntriesUserId = 0;

				if (navigation.equals("mine") && _themeDisplay.isSignedIn()) {
					groupFileEntriesUserId = _themeDisplay.getUserId();

					status = WorkflowConstants.STATUS_ANY;
				}

				long repositoryId = getRepositoryId();

				OrderByComparator<FileEntry> fileEntryOrderByComparator =
					DLUtil.getRepositoryModelOrderByComparator(
						getOrderByCol(), getOrderByType(), orderByModel);

				int dlAppStatus = status;

				long dlAppGroupFileEntriesUserId = groupFileEntriesUserId;

				dlSearchContainer.setResultsAndTotal(
					() -> {
						results.addAll(
							DLAppServiceUtil.getGroupFileEntries(
								repositoryId, dlAppGroupFileEntriesUserId,
								folderId, null, dlAppStatus,
								dlSearchContainer.getStart(),
								dlSearchContainer.getEnd(),
								fileEntryOrderByComparator));

						return results;
					},
					DLAppServiceUtil.getGroupFileEntriesCount(
						repositoryId, dlAppGroupFileEntriesUserId, folderId,
						null, dlAppStatus));
			}
		}

		return dlSearchContainer;
	}

	private Hits _getHits(SearchContainer<RepositoryEntry> searchContainer)
		throws PortalException {

		SearchContext searchContext = SearchContextFactory.getInstance(
			_httpServletRequest);

		searchContext.setAttribute("paginationType", "regular");

		long searchRepositoryId = ParamUtil.getLong(
			_httpServletRequest, "searchRepositoryId");

		if (searchRepositoryId == 0) {
			searchRepositoryId = _themeDisplay.getScopeGroupId();
		}

		searchContext.setAttribute("searchRepositoryId", searchRepositoryId);
		searchContext.setEnd(searchContainer.getEnd());

		searchContext.setFolderIds(
			new long[] {
				ParamUtil.getLong(_httpServletRequest, "searchFolderId")
			});

		searchContext.setIncludeDiscussions(true);
		searchContext.setIncludeInternalAssetCategories(true);
		searchContext.setKeywords(
			ParamUtil.getString(_httpServletRequest, "keywords"));
		searchContext.setLocale(_themeDisplay.getSiteDefaultLocale());

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setSearchSubfolders(true);

		searchContext.setStart(searchContainer.getStart());

		return DLAppServiceUtil.search(searchRepositoryId, searchContext);
	}

	private String _getPortletPreference(String name, String defaultValue) {
		if (_themeDisplay.isSignedIn()) {
			PortletPreferences portletPreferences = _getPortletPreferences();

			return portletPreferences.getValue(name, defaultValue);
		}

		return GetterUtil.getString(
			_httpSession.getAttribute(
				_dlRequestHelper.getPortletId() + StringPool.UNDERLINE + name),
			defaultValue);
	}

	private PortletPreferences _getPortletPreferences() {
		if (_portletPreferences != null) {
			return _portletPreferences;
		}

		_portletPreferences =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				_themeDisplay.getCompanyId(), _themeDisplay.getUserId(),
				PortletKeys.PREFS_OWNER_TYPE_USER, _themeDisplay.getPlid(),
				_dlRequestHelper.getPortletId(), StringPool.BLANK);

		return _portletPreferences;
	}

	private List<RepositoryEntry> _getSearchResults(Hits hits)
		throws PortalException {

		List<RepositoryEntry> searchResults = new ArrayList<>();

		for (SearchResult searchResult :
				SearchResultUtil.getSearchResults(
					hits, _httpServletRequest.getLocale())) {

			String className = searchResult.getClassName();

			try {
				List<RelatedSearchResult<FileEntry>>
					fileEntryRelatedSearchResults =
						searchResult.getFileEntryRelatedSearchResults();

				if (!fileEntryRelatedSearchResults.isEmpty()) {
					fileEntryRelatedSearchResults.forEach(
						fileEntryRelatedSearchResult -> searchResults.add(
							fileEntryRelatedSearchResult.getModel()));
				}
				else if (className.equals(DLFileEntry.class.getName()) ||
						 FileEntry.class.isAssignableFrom(
							 Class.forName(className))) {

					searchResults.add(
						DLAppLocalServiceUtil.getFileEntry(
							searchResult.getClassPK()));
				}
				else if (className.equals(DLFolder.class.getName()) ||
						 className.equals(Folder.class.getName())) {

					searchResults.add(
						DLAppLocalServiceUtil.getFolder(
							searchResult.getClassPK()));
				}
			}
			catch (ClassNotFoundException classNotFoundException) {
				throw new PortalException(classNotFoundException);
			}
		}

		return searchResults;
	}

	private SearchContainer<RepositoryEntry> _getSearchSearchContainer()
		throws PortalException {

		SearchContainer<RepositoryEntry> searchContainer =
			new SearchContainer<>(
				_liferayPortletRequest, getSearchSearchContainerURL(), null,
				null);

		Hits hits = _getHits(searchContainer);

		searchContainer.setResultsAndTotal(
			() -> _getSearchResults(hits), hits.getLength());

		return searchContainer;
	}

	private void _setPortletPreference(String name, String value) {
		if (_themeDisplay.isSignedIn()) {
			PortletPreferences portletPreferences = _getPortletPreferences();

			try {
				portletPreferences.setValue(name, value);

				portletPreferences.store();
			}
			catch (Exception exception) {
				if (_log.isWarnEnabled()) {
					_log.warn(exception);
				}
			}
		}
		else {
			_httpSession.setAttribute(
				_dlRequestHelper.getPortletId() + StringPool.UNDERLINE + name,
				value);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DLAdminDisplayContext.class);

	private final AssetAutoTaggerConfiguration _assetAutoTaggerConfiguration;
	private boolean _defaultFolderView;
	private String _displayStyle;
	private final DLPortletInstanceSettings _dlPortletInstanceSettings;
	private final DLPortletInstanceSettingsHelper
		_dlPortletInstanceSettingsHelper;
	private final DLRequestHelper _dlRequestHelper;
	private Folder _folder;
	private long _folderId;
	private final HttpServletRequest _httpServletRequest;
	private final HttpSession _httpSession;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private String _navigation;
	private String _orderByCol;
	private String _orderByType;
	private final PermissionChecker _permissionChecker;
	private final PortalPreferences _portalPreferences;
	private PortletPreferences _portletPreferences;
	private long _repositoryId;
	private long _rootFolderId;
	private boolean _rootFolderInTrash;
	private String _rootFolderName;
	private boolean _rootFolderNotFound;
	private SearchContainer<RepositoryEntry> _searchContainer;
	private long _selectedRepositoryId;
	private final ThemeDisplay _themeDisplay;
	private final TrashHelper _trashHelper;
	private final VersioningStrategy _versioningStrategy;

}