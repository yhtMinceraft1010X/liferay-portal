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
long[] groupIds = ParamUtil.getLongValues(request, "groupIds");

Group group = null;
int currentPermissions = 0;

if (groupIds.length == 1) {
	group = GroupLocalServiceUtil.fetchGroup(groupIds[0]);

	currentPermissions = GetterUtil.getInteger(group.getTypeSettingsProperty("syncSiteMemberFilePermissions"));
}
%>

<liferay-ui:header
	localizeTitle="<%= false %>"
	title="<%= (group == null) ? StringPool.BLANK : group.getDescriptiveName() %>"
/>

<table class="table table-bordered table-hover table-striped">
	<thead class="table-columns">
		<tr>
			<th>
				<liferay-ui:message key="name" />
			</th>
			<th />
		</tr>
	</thead>

	<tbody>

		<%
		List<Integer> permissionsOptions = new ArrayList<Integer>(4);

		permissionsOptions.add(SyncPermissionsConstants.PERMISSIONS_VIEW_ONLY);
		permissionsOptions.add(SyncPermissionsConstants.PERMISSIONS_VIEW_AND_ADD_DISCUSSION);
		permissionsOptions.add(SyncPermissionsConstants.PERMISSIONS_VIEW_UPDATE_AND_ADD_DISCUSSION);
		permissionsOptions.add(SyncPermissionsConstants.PERMISSIONS_FULL_ACCESS);

		for (Integer permissions : permissionsOptions) {
		%>

			<tr class="record-row">
				<td>
					<c:choose>
						<c:when test="<%= permissions == SyncPermissionsConstants.PERMISSIONS_FULL_ACCESS %>">

							<%
							List<String> resourceActions = ListUtil.fromArray(SyncPermissionsConstants.getFileResourceActions(permissions));

							List<String> localizedResourceActions = new ArrayList<String>(resourceActions.size());

							for (String resourceAction : resourceActions) {
								localizedResourceActions.add(ResourceActionsUtil.getAction(request, resourceAction));
							}
							%>

							<liferay-ui:message arguments="<%= StringUtil.merge(localizedResourceActions, StringPool.COMMA_AND_SPACE) %>" key="full-access-x" />

							<liferay-ui:icon-help message="full-access-help" />
						</c:when>
						<c:when test="<%= permissions == SyncPermissionsConstants.PERMISSIONS_VIEW_AND_ADD_DISCUSSION %>">
							<liferay-ui:message key="view-and-add-discussion" />

							<liferay-ui:icon-help message="view-and-add-discussion-help" />
						</c:when>
						<c:when test="<%= permissions == SyncPermissionsConstants.PERMISSIONS_VIEW_ONLY %>">
							<liferay-ui:message key="view-only" />

							<liferay-ui:icon-help message="view-only-help" />
						</c:when>
						<c:when test="<%= permissions == SyncPermissionsConstants.PERMISSIONS_VIEW_UPDATE_AND_ADD_DISCUSSION %>">
							<liferay-ui:message key="view-update-and-add-discussion" />

							<liferay-ui:icon-help message="view-update-and-add-discussion-help" />
						</c:when>
					</c:choose>
				</td>
				<td>
					<portlet:actionURL name="updateSites" var="setPermissionsURL">
						<portlet:param name="groupIds" value="<%= StringUtil.merge(groupIds) %>" />
						<portlet:param name="permissions" value="<%= String.valueOf(permissions) %>" />
					</portlet:actionURL>

					<%
					String taglibSetPermissions = liferayPortletResponse.getNamespace() + "setPermissions('" + setPermissionsURL + "');";
					%>

					<aui:button disabled="<%= currentPermissions == permissions %>" onClick="<%= taglibSetPermissions %>" value="choose" />
				</td>
			</tr>

		<%
		}
		%>

	</tbody>
</table>

<aui:script>
	window['<portlet:namespace />setPermissions'] = function (uri) {
		Liferay.Util.getOpener().Liferay.fire(

			<%
			String selectEventName = ParamUtil.getString(request, "selectEventName");
			%>

			'<%= HtmlUtil.escape(selectEventName) %>',
			{
				uri: uri,
			}
		);
	};
</aui:script>