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
DepotEntry depotEntry = (DepotEntry)request.getAttribute(DepotAdminWebKeys.DEPOT_ENTRY);

Group group = depotEntry.getGroup();

UnicodeProperties typeSettingsProperties = group.getTypeSettingsProperties();
%>

<liferay-frontend:fieldset
	collapsible="<%= true %>"
	cssClass="panel-group-flush"
	label='<%= LanguageUtil.get(request, "documents-and-media") %>'
>
	<aui:input helpMessage='<%= LanguageUtil.format(request, "can-user-with-view-permission-browse-the-asset-library-document-library-files-and-folders", new Object[] {HtmlUtil.escape(group.getDescriptiveName(themeDisplay.getLocale())), themeDisplay.getPortalURL() + "/documents" + group.getFriendlyURL()}, false) %>' inlineLabel="right" label="enable-directory-indexing" labelCssClass="simple-toggle-switch" name="TypeSettingsProperties--directoryIndexingEnabled--" type="toggle-switch" value='<%= PropertiesParamUtil.getBoolean(typeSettingsProperties, request, "directoryIndexingEnabled") %>' />

	<c:if test='<%= GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-114786")) %>'>
		<liferay-frontend:fieldset
			collapsible="<%= false %>"
			cssClass="mt-5"
			label='<%= LanguageUtil.get(request, "maximum-file-size-and-mimetypes") %>'
		>
			<div>
				<span aria-hidden="true" class="loading-animation"></span>

				<react:component
					module="js/FileSizePerMimeType"
					props='<%=
						HashMapBuilder.<String, Object>put(
							"test", "test"
						).build()
					%>'
				/>
			</div>
		</liferay-frontend:fieldset>
	</c:if>
</liferay-frontend:fieldset>