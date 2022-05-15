<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
BlogsItemSelectorViewDisplayContext blogsItemSelectorViewDisplayContext = (BlogsItemSelectorViewDisplayContext)request.getAttribute(BlogsItemSelectorWebKeys.BLOGS_ITEM_SELECTOR_VIEW_DISPLAY_CONTEXT);

int cur = ParamUtil.getInteger(request, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_CUR);
int delta = ParamUtil.getInteger(request, SearchContainer.DEFAULT_DELTA_PARAM, SearchContainer.DEFAULT_DELTA);

int[] startAndEnd = SearchPaginationUtil.calculateStartAndEnd(cur, delta);

int start = startAndEnd[0];
int end = startAndEnd[1];

Folder folder = blogsItemSelectorViewDisplayContext.fetchAttachmentsFolder(themeDisplay.getUserId(), scopeGroupId);

List<RepositoryEntry> portletFileEntries = new ArrayList<>();
int portletFileEntriesCount = 0;

if (folder != null) {
	if (blogsItemSelectorViewDisplayContext.isSearch()) {
		SearchContext searchContext = SearchContextFactory.getInstance(request);

		searchContext.setEnd(end);
		searchContext.setFolderIds(new long[] {folder.getFolderId()});
		searchContext.setStart(start);

		Hits hits = PortletFileRepositoryUtil.searchPortletFileEntries(folder.getRepositoryId(), searchContext);

		portletFileEntriesCount = hits.getLength();

		for (Document doc : hits.getDocs()) {
			long fileEntryId = GetterUtil.getLong(doc.get(Field.ENTRY_CLASS_PK));

			FileEntry fileEntry = null;

			try {
				fileEntry = PortletFileRepositoryUtil.getPortletFileEntry(fileEntryId);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn("Documents and Media search index is stale and contains file entry {" + fileEntryId + "}");
				}

				continue;
			}

			portletFileEntries.add(fileEntry);
		}
	}
	else {
		portletFileEntries.addAll(PortletFileRepositoryUtil.getPortletFileEntries(scopeGroupId, folder.getFolderId(), WorkflowConstants.STATUS_APPROVED, start, end, blogsItemSelectorViewDisplayContext.getOrderByComparator()));
		portletFileEntriesCount = PortletFileRepositoryUtil.getPortletFileEntriesCount(scopeGroupId, folder.getFolderId(), WorkflowConstants.STATUS_APPROVED);
	}
}
%>

<liferay-item-selector:repository-entry-browser
	allowedCreationMenuUIItemKeys="<%= blogsItemSelectorViewDisplayContext.getAllowedCreationMenuUIItemKeys() %>"
	editImageURL="<%= blogsItemSelectorViewDisplayContext.getEditImageURL(liferayPortletResponse) %>"
	emptyResultsMessage='<%= LanguageUtil.get(resourceBundle, "there-are-no-blog-attachments") %>'
	extensions="<%= ListUtil.fromArray(blogsItemSelectorViewDisplayContext.getImageExtensions()) %>"
	itemSelectedEventName="<%= blogsItemSelectorViewDisplayContext.getItemSelectedEventName() %>"
	itemSelectorReturnTypeResolver="<%= blogsItemSelectorViewDisplayContext.getItemSelectorReturnTypeResolver() %>"
	maxFileSize="<%= blogsItemSelectorViewDisplayContext.getImageMaxSize() %>"
	mimeTypeRestriction="<%= blogsItemSelectorViewDisplayContext.getMimeTypeRestriction() %>"
	portletURL="<%= blogsItemSelectorViewDisplayContext.getPortletURL(request, liferayPortletResponse) %>"
	repositoryEntries="<%= portletFileEntries %>"
	repositoryEntriesCount="<%= portletFileEntriesCount %>"
	showDragAndDropZone="<%= true %>"
	tabName="<%= blogsItemSelectorViewDisplayContext.getTitle(locale) %>"
	uploadURL="<%= blogsItemSelectorViewDisplayContext.getUploadURL(liferayPortletResponse) %>"
/>

<%!
private static final Log _log = LogFactoryUtil.getLog("com_liferay_blogs_item_selector_web.blogs_attachments_jsp");
%>