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
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

Object result = row.getObject();

FileEntry fileEntry = null;
FileShortcut fileShortcut = null;

if (result instanceof AssetEntry) {
	AssetEntry assetEntry = (AssetEntry)result;

	if (Objects.equals(assetEntry.getClassName(), DLFileEntryConstants.getClassName())) {
		fileEntry = DLAppLocalServiceUtil.getFileEntry(assetEntry.getClassPK());
	}
	else {
		fileShortcut = DLAppLocalServiceUtil.getFileShortcut(assetEntry.getClassPK());

		fileShortcut = fileShortcut.toEscapedModel();
	}
}
else if (result instanceof FileEntry) {
	fileEntry = (FileEntry)result;
}
else if (result instanceof FileShortcut) {
	fileShortcut = (FileShortcut)result;

	fileShortcut = fileShortcut.toEscapedModel();

	fileEntry = DLAppLocalServiceUtil.getFileEntry(fileShortcut.getToFileEntryId());
}

fileEntry = fileEntry.toEscapedModel();

FileVersion latestFileVersion = fileEntry.getFileVersion();

if ((user.getUserId() == fileEntry.getUserId()) || permissionChecker.isContentReviewer(user.getCompanyId(), scopeGroupId) || DLFileEntryPermission.contains(permissionChecker, fileEntry, ActionKeys.UPDATE)) {
	latestFileVersion = fileEntry.getLatestFileVersion();
}

latestFileVersion = latestFileVersion.toEscapedModel();

Date modifiedDate = latestFileVersion.getModifiedDate();

String modifiedDateDescription = LanguageUtil.getTimeDescription(request, System.currentTimeMillis() - modifiedDate.getTime(), true);

DLViewFileVersionDisplayContext dlViewFileVersionDisplayContext = null;

if (fileShortcut == null) {
	dlViewFileVersionDisplayContext = dlDisplayContextProvider.getDLViewFileVersionDisplayContext(request, response, latestFileVersion);
}
else {
	dlViewFileVersionDisplayContext = dlDisplayContextProvider.getDLViewFileVersionDisplayContext(request, response, fileShortcut);
}
%>

<h2 class="h5">
	<aui:a
		href='<%=
			PortletURLBuilder.createRenderURL(
				liferayPortletResponse
			).setMVCRenderCommandName(
				"/document_library/view_file_entry"
			).setRedirect(
				HttpComponentsUtil.removeParameter(currentURL, liferayPortletResponse.getNamespace() + "ajax")
			).setParameter(
				"fileEntryId", fileEntry.getFileEntryId()
			).buildString()
		%>'
	>
		<%= latestFileVersion.getTitle() %>
	</aui:a>
</h2>

<span>
	<liferay-ui:message arguments="<%= new String[] {HtmlUtil.escape(latestFileVersion.getUserName()), modifiedDateDescription} %>" key="x-modified-x-ago" />
</span>
<span>
	<%= DLUtil.getAbsolutePath(liferayPortletRequest, fileEntry.getFolderId()).replace(StringPool.RAQUO_CHAR, StringPool.GREATER_THAN) %>
</span>

<c:if test="<%= latestFileVersion.getModel() instanceof DLFileVersion %>">

	<%
	DLFileVersion latestDLFileVersion = (DLFileVersion)latestFileVersion.getModel();

	DLFileEntryType dlFileEntryType = latestDLFileVersion.getDLFileEntryType();
	%>

	<span>
		<%= HtmlUtil.escape(dlFileEntryType.getName(locale)) %>
	</span>
</c:if>

<span class="file-entry-status">
	<aui:workflow-status showIcon="<%= false %>" showLabel="<%= false %>" status="<%= latestFileVersion.getStatus() %>" />

	<c:choose>
		<c:when test="<%= fileShortcut != null %>">
			<span class="inline-item inline-item-after state-icon">
				<aui:icon image="shortcut" markupView="lexicon" message="shortcut" />
			</span>
		</c:when>
		<c:when test="<%= fileEntry.hasLock() || fileEntry.isCheckedOut() %>">
			<span class="inline-item inline-item-after state-icon">
				<aui:icon image="lock" markupView="lexicon" message="locked" />
			</span>
		</c:when>
	</c:choose>

	<c:if test="<%= dlViewFileVersionDisplayContext.isShared() %>">
		<span class="inline-item inline-item-after lfr-portal-tooltip state-icon" title="<%= LanguageUtil.get(request, "shared") %>">
			<aui:icon image="users" markupView="lexicon" message="shared" />
		</span>
	</c:if>
</span>