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

<%@ include file="/document_library/init.jsp" %>

<%
DLInfoPanelDisplayContext dlInfoPanelDisplayContext = new DLInfoPanelDisplayContext(request);

request.setAttribute("view.jsp-repositoryId", String.valueOf(dlInfoPanelDisplayContext.getRepositoryId()));

List<FileEntry> fileEntries = dlInfoPanelDisplayContext.getFileEntries();
List<FileShortcut> fileShortcuts = dlInfoPanelDisplayContext.getFileShortcuts();
List<Folder> folders = dlInfoPanelDisplayContext.getFolders();
%>

<c:choose>
	<c:when test="<%= dlInfoPanelDisplayContext.isFolderSelected() %>">

		<%
		Folder folder = folders.get(0);

		request.setAttribute("info_panel.jsp-folder", folder);
		%>

		<div class="sidebar-header">
			<div class="autofit-row sidebar-section">
				<div class="autofit-col autofit-col-expand">
					<h1 class="component-title">
						<%= (folder != null) ? HtmlUtil.escape(folder.getName()) : LanguageUtil.get(request, "home") %>
					</h1>

					<h2 class="component-subtitle">
						<liferay-ui:message key="folder" />
					</h2>
				</div>

				<div class="autofit-col">
					<ul class="autofit-padded-no-gutters autofit-row">
						<li class="autofit-col">
							<liferay-util:include page="/document_library/subscribe.jsp" servletContext="<%= application %>" />
						</li>

						<%
						FolderActionDisplayContext folderActionDisplayContext = new FolderActionDisplayContext(dlTrashHelper, request);
						%>

						<c:if test="<%= folderActionDisplayContext.isShowActions() %>">
							<li class="autofit-col">
								<liferay-util:include page="/document_library/folder_action.jsp" servletContext="<%= application %>" />
							</li>
						</c:if>
					</ul>
				</div>
			</div>
		</div>

		<div class="sidebar-body">
			<liferay-ui:tabs
				cssClass="navbar-no-collapse"
				names="details"
				refresh="<%= false %>"
			>
				<liferay-ui:section>
					<dl class="sidebar-dl sidebar-section">
						<dt class="sidebar-dt">
							<liferay-ui:message key="num-of-items" />
						</dt>
						<dd class="sidebar-dd">
							<%= DLAppServiceUtil.getFoldersAndFileEntriesAndFileShortcutsCount(dlInfoPanelDisplayContext.getRepositoryId(), dlInfoPanelDisplayContext.getFolderId(folder), WorkflowConstants.STATUS_APPROVED, true) %>
						</dd>

						<c:if test="<%= folder != null %>">
							<dt class="sidebar-dt">
								<liferay-ui:message key="created-by" />
							</dt>
							<dd class="sidebar-dd">
								<%= HtmlUtil.escape(folder.getUserName()) %>
							</dd>

							<%
							request.setAttribute("info_panel_location.jsp-parentFolder", folder.getParentFolder());
							%>

							<liferay-util:include page="/document_library/info_panel_location.jsp" servletContext="<%= application %>" />
						</c:if>
					</dl>
				</liferay-ui:section>
			</liferay-ui:tabs>
		</div>
	</c:when>
	<c:when test="<%= dlInfoPanelDisplayContext.isFileEntrySelected() %>">

		<%
		FileEntry fileEntry = fileEntries.get(0);

		request.setAttribute("info_panel.jsp-fileEntry", fileEntry);
		request.setAttribute("info_panel.jsp-fileVersion", dlInfoPanelDisplayContext.getFileVersion(fileEntry));
		%>

		<liferay-util:include page="/document_library/info_panel_file_entry.jsp" servletContext="<%= application %>" />
	</c:when>
	<c:when test="<%= dlInfoPanelDisplayContext.isFileShortcutSelected() %>">

		<%
		FileShortcut fileShortcut = fileShortcuts.get(0);

		request.setAttribute("info_panel.jsp-fileShortcut", fileShortcut);
		%>

		<div class="sidebar-header">
			<div class="autofit-row sidebar-section">
				<div class="autofit-col autofit-col-expand">
					<h1 class="component-title">
						<%= HtmlUtil.escape(fileShortcut.getToTitle()) %>
					</h1>

					<h2 class="component-subtitle">
						<liferay-ui:message key="shortcut" />
					</h2>
				</div>

				<div class="autofit-col">
					<ul class="autofit-padded-no-gutters autofit-row">
						<li class="autofit-col">
							<liferay-util:include page="/document_library/file_entry_action.jsp" servletContext="<%= application %>" />
						</li>
					</ul>
				</div>
			</div>
		</div>

		<div class="sidebar-body">
			<liferay-ui:tabs
				cssClass="navbar-no-collapse"
				names="details"
				refresh="<%= false %>"
			>
				<liferay-ui:section>

					<%
					FileEntry fileEntry = DLAppServiceUtil.getFileEntry(fileShortcut.getToFileEntryId());
					%>

					<dl class="sidebar-dl sidebar-section">
						<dt class="sidebar-dt">
							<liferay-ui:message key="description" />
						</dt>
						<dd class="sidebar-dd">
							<%= HtmlUtil.replaceNewLine(HtmlUtil.escape(fileEntry.getDescription())) %>
						</dd>

						<%
						Group fileEntryGroup = dlInfoPanelDisplayContext.getFileEntryGroup(fileEntry.getGroupId());
						%>

						<c:if test="<%= fileEntryGroup != null %>">
							<c:choose>
								<c:when test="<%= fileEntryGroup.isSite() %>">
									<dt class="sidebar-dt">
										<liferay-ui:message key="target-site" />
									</dt>
								</c:when>
								<c:when test="<%= fileEntryGroup.isDepot() %>">
									<dt class="sidebar-dt">
										<liferay-ui:message key="target-asset-library" />
									</dt>
								</c:when>
							</c:choose>

							<dd class="sidebar-dd">
								<%= HtmlUtil.escape(fileEntryGroup.getName(locale)) %>
							</dd>
						</c:if>

						<dt class="sidebar-dt">
							<liferay-ui:message key="target-folder" />
						</dt>
						<dd class="sidebar-dd">

							<%
							Folder folder = fileEntry.getFolder();
							%>

							<portlet:renderURL var="targetFolderURL">
								<portlet:param name="mvcRenderCommand" value="/document_library/view" />
								<portlet:param name="folderId" value="<%= String.valueOf(folder.getFolderId()) %>" />
							</portlet:renderURL>

							<a href="<%= targetFolderURL %>">
								<c:choose>
									<c:when test="<%= folder.getFolderId() == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID %>">
										<liferay-ui:message key="home" />
									</c:when>
									<c:otherwise>
										<%= HtmlUtil.escape(folder.getName()) %>
									</c:otherwise>
								</c:choose>
							</a>
						</dd>
						<dt class="sidebar-dt">
							<liferay-ui:message key="size" />
						</dt>
						<dd class="sidebar-dd">
							<%= LanguageUtil.formatStorageSize(fileEntry.getSize(), locale) %>
						</dd>

						<c:if test="<%= fileEntry.getModel() instanceof DLFileEntry %>">
							<dt class="sidebar-dt">
								<liferay-ui:message key="document-type" />
							</dt>
							<dd class="sidebar-dd">
								<%= dlInfoPanelDisplayContext.getFileEntryTypeName(fileEntry, locale) %>
							</dd>
						</c:if>

						<dt class="sidebar-dt">
							<liferay-ui:message key="content-type" />
						</dt>
						<dd class="sidebar-dd">
							<%= HtmlUtil.escape(fileEntry.getMimeType()) %>
						</dd>
					</dl>
				</liferay-ui:section>
			</liferay-ui:tabs>
		</div>
	</c:when>
	<c:otherwise>
		<div class="sidebar-header">
			<div class="autofit-row sidebar-section">
				<div class="autofit-col autofit-col-expand">
					<h1 class="component-title">
						<liferay-ui:message key="selection" />
					</h1>
				</div>
			</div>
		</div>

		<div class="sidebar-body">
			<liferay-ui:tabs
				cssClass="navbar-no-collapse"
				names="details"
				refresh="<%= false %>"
			>
				<liferay-ui:section>
					<strong>
						<liferay-ui:message arguments="<%= folders.size() + fileEntries.size() + fileShortcuts.size() %>" key="x-items-are-selected" />
					</strong>
				</liferay-ui:section>
			</liferay-ui:tabs>
		</div>
	</c:otherwise>
</c:choose>