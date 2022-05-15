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
String tabs1 = ParamUtil.getString(request, "tabs1", "sites");

String keywords = ParamUtil.getString(request, "keywords");

int delta = ParamUtil.getInteger(request, "delta", SearchContainer.DEFAULT_DELTA);

PortletURL portletURL = PortletURLBuilder.createRenderURL(
	renderResponse
).setTabs1(
	tabs1
).setParameter(
	"delta", delta
).buildPortletURL();
%>

<liferay-frontend:management-bar
	includeCheckBox="<%= true %>"
	searchContainerId="sites"
>
	<c:if test="<%= Validator.isNull(keywords) %>">
		<liferay-frontend:management-bar-buttons>
			<liferay-frontend:management-bar-display-buttons
				displayViews='<%= new String[] {"list"} %>'
				portletURL="<%= PortletURLUtil.clone(portletURL, liferayPortletResponse) %>"
				selectedDisplayStyle="list"
			/>
		</liferay-frontend:management-bar-buttons>

		<liferay-frontend:management-bar-filters>
			<liferay-frontend:management-bar-navigation
				navigationKeys='<%= new String[] {"all"} %>'
				portletURL="<%= PortletURLUtil.clone(portletURL, liferayPortletResponse) %>"
			/>

			<li>
				<aui:form
					action="<%=
						PortletURLBuilder.createRenderURL(
							renderResponse
						).setTabs1(
							tabs1
						).buildString()
					%>"
					name="searchFm"
				>
					<liferay-ui:input-search
						markupView="lexicon"
						placeholder='<%= LanguageUtil.get(request, "search") %>'
					/>
				</aui:form>
			</li>
		</liferay-frontend:management-bar-filters>
	</c:if>

	<liferay-frontend:management-bar-action-buttons>
		<liferay-frontend:management-bar-button
			href='<%= "javascript:" + liferayPortletResponse.getNamespace() + "enableSites();" %>'
			icon="check"
			label="enable-sync-sites"
		/>

		<liferay-frontend:management-bar-button
			href='<%= "javascript:" + liferayPortletResponse.getNamespace() + "disableSites();" %>'
			icon="times"
			label="disable-sync-sites"
		/>

		<liferay-frontend:management-bar-button
			href='<%= "javascript:" + liferayPortletResponse.getNamespace() + "editSitesDefaultFilePermissions();" %>'
			icon="lock"
			label="default-file-permissions"
		/>
	</liferay-frontend:management-bar-action-buttons>
</liferay-frontend:management-bar>

<clay:container-fluid>
	<aui:form method="post" name="fm">
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="enabled" type="hidden" />
		<aui:input name="groupIds" type="hidden" />
		<aui:input name="permissions" type="hidden" />

		<%
		List<Group> groups = GroupLocalServiceUtil.search(
			themeDisplay.getCompanyId(), keywords,
			LinkedHashMapBuilder.<String, Object>put(
				"active", true
			).put(
				"site", true
			).build(),
			QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		List<String> resourceActions = ListUtil.fromArray(SyncPermissionsConstants.getFileResourceActions(SyncPermissionsConstants.PERMISSIONS_FULL_ACCESS));

		List<String> localizedResourceActions = new ArrayList<String>(resourceActions.size());

		for (String resourceAction : resourceActions) {
			localizedResourceActions.add(ResourceActionsUtil.getAction(request, resourceAction));
		}

		String fullAccessPermissionsDescription = LanguageUtil.format(request, "full-access-x", StringUtil.merge(localizedResourceActions, StringPool.COMMA_AND_SPACE));

		resourceActions = ResourceActionsUtil.getModelResourceGroupDefaultActions(DLFileEntry.class.getName());

		String defaultPermissionsDescription = null;

		if (resourceActions != null) {
			localizedResourceActions = new ArrayList<String>(resourceActions.size());

			for (String resourceAction : resourceActions) {
				localizedResourceActions.add(ResourceActionsUtil.getAction(request, resourceAction));
			}

			defaultPermissionsDescription = StringUtil.merge(localizedResourceActions, StringPool.COMMA_AND_SPACE);
		}
		%>

		<liferay-ui:search-container
			emptyResultsMessage="no-sites-were-found"
			id="sites"
			iteratorURL="<%= portletURL %>"
			rowChecker="<%= new RowChecker(renderResponse) %>"
			total="<%= groups.size() %>"
		>
			<liferay-ui:search-container-results
				calculateStartAndEnd="<%= true %>"
				results="<%= groups %>"
			/>

			<liferay-ui:search-container-row
				className="com.liferay.portal.kernel.model.Group"
				escapedModel="<%= true %>"
				keyProperty="groupId"
				modelVar="group"
			>
				<liferay-ui:search-container-column-text
					cssClass="content-column name-column title-column"
					name="name"
					property="descriptiveName"
				/>

				<liferay-ui:search-container-column-text
					name="description"
					property="description"
				/>

				<%
				boolean syncSiteEnabled = GetterUtil.getBoolean(group.getTypeSettingsProperty("syncEnabled"), true);

				String permissionsDescription = StringPool.BLANK;

				if (syncSiteEnabled) {
					int currentPermissions = GetterUtil.getInteger(group.getTypeSettingsProperty("syncSiteMemberFilePermissions"));

					if (currentPermissions == SyncPermissionsConstants.PERMISSIONS_VIEW_ONLY) {
						permissionsDescription = LanguageUtil.get(request, "view-only");
					}
					else if (currentPermissions == SyncPermissionsConstants.PERMISSIONS_VIEW_AND_ADD_DISCUSSION) {
						permissionsDescription = LanguageUtil.get(request, "view-and-add-discussion");
					}
					else if (currentPermissions == SyncPermissionsConstants.PERMISSIONS_VIEW_UPDATE_AND_ADD_DISCUSSION) {
						permissionsDescription = LanguageUtil.get(request, "view-update-and-add-discussion");
					}
					else if (currentPermissions == SyncPermissionsConstants.PERMISSIONS_FULL_ACCESS) {
						permissionsDescription = fullAccessPermissionsDescription;
					}
					else if (Validator.isNotNull(defaultPermissionsDescription)) {
						permissionsDescription = defaultPermissionsDescription;
					}
				}
				%>

				<liferay-ui:search-container-column-text
					name="default-file-permissions"
					value="<%= permissionsDescription %>"
				/>

				<liferay-ui:search-container-column-text
					name="enabled"
					translate="<%= true %>"
					value='<%= syncSiteEnabled ? "yes" : "no" %>'
				/>

				<liferay-ui:search-container-column-jsp
					align="right"
					cssClass="entry-action-column"
					path="/sites_action.jsp"
				/>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				markupView="lexicon"
			/>
		</liferay-ui:search-container>
	</aui:form>
</clay:container-fluid>

<aui:script>
	function <portlet:namespace />disableSites() {
		var form = document.querySelector('#document.<portlet:namespace />fm');

		if (form) {
			var groupIds = Liferay.Util.listCheckedExcept(
				form,
				'<portlet:namespace />allRowIds'
			);

			if (
				groupIds &&
				confirm(
					'<liferay-ui:message key="disabling-a-sync-site-will-delete-all-associated-files-from-all-clients" />'
				)
			) {
				form.querySelector(
					'#<portlet:namespace />groupIds'
				).value = groupIds;
				form.querySelector('#<portlet:namespace />enabled').value = false;

				submitForm(
					form,
					'<liferay-portlet:actionURL name="updateSites" />'
				);
			}
		}
	}

	function <portlet:namespace />editSitesDefaultFilePermissions() {
		var form = document.querySelector('#<portlet:namespace />fm');

		if (form) {
			var groupIds = Liferay.Util.listCheckedExcept(
				form,
				'<portlet:namespace />allRowIds'
			);

			if (groupIds) {

				<%
				String selectEventName = liferayPortletResponse.getNamespace() + "itemSelected";
				%>

				<portlet:renderURL var="editSitesDefaultFilePermissionsURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
					<portlet:param name="mvcPath" value="/edit_default_file_permissions.jsp" />
					<portlet:param name="groupIds" value="{groupIds}" />
					<portlet:param name="selectEventName" value="<%= selectEventName %>" />
				</portlet:renderURL>

				var url = Liferay.Util.sub(
					decodeURIComponent('<%= editSitesDefaultFilePermissionsURL %>'),
					{
						groupIds: groupIds,
					}
				);

				Liferay.Util.openSelectionModal({
					id: '<portlet:namespace />editDefaultFilePermissionsDialog',
					onSelect: function (selectedItem) {
						Liferay.Util.fetch(selectedItem.uri, {method: 'POST'})
							.then(function (response) {
								return response.text();
							})
							.then(function () {
								Liferay.Portlet.refresh(
									'#p_p_id<portlet:namespace />'
								);
							})
							.catch(function (error) {
								Liferay.Util.openToast({
									message: Liferay.Language.get(
										'an-unexpected-system-error-occurred'
									),
									type: 'danger',
								});
							});
					},
					selectEventName: '<%= selectEventName %>',
					title: '<liferay-ui:message key="default-file-permissions" />',
					url: url,
				});
			}
		}
	}

	function <portlet:namespace />enableSites() {
		var form = document.querySelector('#<portlet:namespace />fm');

		if (form) {
			var groupIds = Liferay.Util.listCheckedExcept(
				form,
				'<portlet:namespace />allRowIds'
			);

			if (groupIds) {
				form.querySelector(
					'#<portlet:namespace />groupIds'
				).value = groupIds;
				form.querySelector('#<portlet:namespace />enabled').value = true;

				submitForm(
					form,
					'<liferay-portlet:actionURL name="updateSites" />'
				);
			}
		}
	}
</aui:script>