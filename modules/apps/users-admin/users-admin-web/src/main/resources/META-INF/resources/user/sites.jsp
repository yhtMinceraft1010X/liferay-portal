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
User selUser = userDisplayContext.getSelectedUser();
List<Group> siteGroups = userDisplayContext.getSiteGroups();
List<Group> inheritedSiteGroups = userDisplayContext.getInheritedSiteGroups();

currentURLObj.setParameter("historyKey", liferayPortletResponse.getNamespace() + "sites");
%>

<liferay-ui:error-marker
	key="<%= WebKeys.ERROR_SECTION %>"
	value="sites"
/>

<liferay-ui:membership-policy-error />

<clay:content-row
	containerElement="h3"
	cssClass="sheet-subtitle"
>
	<clay:content-col
		expand="<%= true %>"
	>
		<span class="heading-text"><liferay-ui:message key="sites" /></span>
	</clay:content-col>

	<c:if test="<%= !portletName.equals(myAccountPortletId) %>">
		<clay:content-col>
			<span class="heading-end">
				<liferay-ui:icon
					cssClass="modify-link"
					id="selectSiteLink"
					label="<%= true %>"
					linkCssClass="btn btn-secondary btn-sm"
					message="select"
					url="javascript:;"
				/>
			</span>
		</clay:content-col>
	</c:if>
</clay:content-row>

<liferay-util:buffer
	var="removeGroupIcon"
>
	<liferay-ui:icon
		icon="times-circle"
		markupView="lexicon"
		message="remove"
	/>
</liferay-util:buffer>

<aui:input name="addGroupIds" type="hidden" />
<aui:input name="deleteGroupIds" type="hidden" />

<liferay-ui:search-container
	compactEmptyResultsMessage="<%= true %>"
	cssClass="lfr-search-container-sites"
	curParam="sitesCur"
	emptyResultsMessage="this-user-does-not-belong-to-a-site"
	headerNames="name,roles,null"
	iteratorURL="<%= currentURLObj %>"
	total="<%= siteGroups.size() %>"
>
	<liferay-ui:search-container-results
		calculateStartAndEnd="<%= true %>"
		results="<%= siteGroups %>"
	/>

	<liferay-ui:search-container-row
		className="com.liferay.portal.kernel.model.Group"
		escapedModel="<%= true %>"
		keyProperty="groupId"
		modelVar="group"
		rowIdProperty="friendlyURL"
	>
		<liferay-ui:search-container-column-text
			cssClass="table-cell-expand"
			name="name"
		>
			<liferay-staging:descriptive-name
				group="<%= group %>"
			/>
		</liferay-ui:search-container-column-text>

		<%
		List<UserGroupRole> userGroupRoles = new ArrayList<UserGroupRole>();
		int userGroupRolesCount = 0;

		if (selUser != null) {
			userGroupRoles = UserGroupRoleLocalServiceUtil.getUserGroupRoles(selUser.getUserId(), group.getGroupId(), 0, PropsValues.USERS_ADMIN_ROLE_COLUMN_LIMIT);
			userGroupRolesCount = UserGroupRoleLocalServiceUtil.getUserGroupRolesCount(selUser.getUserId(), group.getGroupId());
		}
		%>

		<liferay-ui:search-container-column-text
			cssClass="table-cell-expand"
			name="roles"
			value="<%= HtmlUtil.escape(UsersAdminUtil.getUserColumnText(locale, userGroupRoles, UsersAdmin.USER_GROUP_ROLE_TITLE_ACCESSOR, userGroupRolesCount)) %>"
		/>

		<c:if test="<%= !portletName.equals(myAccountPortletId) && (selUser != null) && !SiteMembershipPolicyUtil.isMembershipRequired(selUser.getUserId(), group.getGroupId()) && !SiteMembershipPolicyUtil.isMembershipProtected(permissionChecker, selUser.getUserId(), group.getGroupId()) %>">
			<liferay-ui:search-container-column-text>
				<c:if test="<%= group.isManualMembership() %>">
					<a class="modify-link" data-rowId="<%= group.getGroupId() %>" href="javascript:;"><%= removeGroupIcon %></a>
				</c:if>
			</liferay-ui:search-container-column-text>
		</c:if>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator
		markupView="lexicon"
	/>
</liferay-ui:search-container>

<c:if test="<%= !portletName.equals(myAccountPortletId) %>">
	<aui:script use="liferay-search-container">
		var AArray = A.Array;
		var Util = Liferay.Util;

		var addGroupIds = [];
		var deleteGroupIds = [];

		var searchContainer = Liferay.SearchContainer.get(
			'<portlet:namespace />groupsSearchContainer'
		);

		var searchContainerContentBox = searchContainer.get('contentBox');

		var handleOnSelect = A.one('#<portlet:namespace />selectSiteLink').on(
			'click',
			(event) => {
				var searchContainerData = searchContainer.getData();

				if (!searchContainerData.length) {
					searchContainerData = [];
				}
				else {
					searchContainerData = searchContainerData.split(',');
				}

				Util.openSelectionModal({
					onSelect: (selectedItem) => {
						if (selectedItem) {
							const entityId = selectedItem.entityid;

							const rowColumns = [];

							rowColumns.push(selectedItem.entityname);
							rowColumns.push('');
							rowColumns.push(
								'<a class="modify-link" data-rowId="' +
									entityId +
									'" href="javascript:;"><%= UnicodeFormatter.toString(removeGroupIcon) %></a>'
							);

							searchContainer.addRow(rowColumns, entityId);

							searchContainer.updateDataStore();

							addGroupIds.push(entityId);

							AArray.removeItem(deleteGroupIds, entityId);

							document.<portlet:namespace />fm.<portlet:namespace />addGroupIds.value = addGroupIds.join(
								','
							);
							document.<portlet:namespace />fm.<portlet:namespace />deleteGroupIds.value = deleteGroupIds.join(
								','
							);
						}
					},

					<%
					String eventName = liferayPortletResponse.getNamespace() + "selectSite";
					%>

					selectEventName: '<%= eventName %>',
					selectedData: [searchContainerData],
					title: '<liferay-ui:message arguments="site" key="select-x" />',

					<%
					PortletURL groupSelectorURL = PortletURLBuilder.create(
						PortletProviderUtil.getPortletURL(request, Group.class.getName(), PortletProvider.Action.BROWSE)
					).setParameter(
						"eventName", eventName
					).setParameter(
						"filterManageableGroups", false
					).setParameter(
						"includeCurrentGroup", false
					).setParameter(
						"manualMembership", true
					).setParameter(
						"p_u_i_d", (selUser == null) ? "0" : String.valueOf(selUser.getUserId())
					).setWindowState(
						LiferayWindowState.POP_UP
					).buildPortletURL();
					%>

					url: '<%= groupSelectorURL.toString() %>',
				});
			}
		);

		var handleOnModifyLink = searchContainerContentBox.delegate(
			'click',
			(event) => {
				var link = event.currentTarget;

				var rowId = link.attr('data-rowId');
				var tr = link.ancestor('tr');

				var selectGroup = Util.getWindow('<portlet:namespace />selectGroup');

				if (selectGroup) {
					var selectButton = selectGroup.iframe.node
						.get('contentWindow.document')
						.one('.selector-button[data-entityid="' + rowId + '"]');

					Util.toggleDisabled(selectButton, false);
				}

				searchContainer.deleteRow(tr, rowId);

				AArray.removeItem(addGroupIds, event.rowId);

				deleteGroupIds.push(rowId);

				document.<portlet:namespace />fm.<portlet:namespace />addGroupIds.value = addGroupIds.join(
					','
				);
				document.<portlet:namespace />fm.<portlet:namespace />deleteGroupIds.value = deleteGroupIds.join(
					','
				);
			},
			'.modify-link'
		);

		var handleEnableRemoveSite = Liferay.on(
			'<portlet:namespace />enableRemovedSites',
			(event) => {
				event.selectors.each((item, index, collection) => {
					var groupId = item.attr('data-entityid');

					if (deleteGroupIds.indexOf(groupId) != -1) {
						Util.toggleDisabled(item, false);
					}
				});
			}
		);

		var onDestroyPortlet = function (event) {
			if (event.portletId === '<%= portletDisplay.getId() %>') {
				Liferay.detach(handleOnSelect);
				Liferay.detach(handleOnModifyLink);
				Liferay.detach(handleEnableRemoveSite);

				Liferay.detach('destroyPortlet', onDestroyPortlet);
			}
		};

		Liferay.on('destroyPortlet', onDestroyPortlet);
	</aui:script>
</c:if>

<c:if test="<%= !inheritedSiteGroups.isEmpty() %>">
	<h4 class="sheet-tertiary-title"><liferay-ui:message key="inherited-sites" /></h4>

	<liferay-ui:search-container
		cssClass="lfr-search-container-inherited-sites"
		curParam="inheritedSitesCur"
		headerNames="name,roles"
		iteratorURL="<%= currentURLObj %>"
		total="<%= inheritedSiteGroups.size() %>"
	>
		<liferay-ui:search-container-results
			calculateStartAndEnd="<%= true %>"
			results="<%= inheritedSiteGroups %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.portal.kernel.model.Group"
			escapedModel="<%= true %>"
			keyProperty="groupId"
			modelVar="inheritedSite"
			rowIdProperty="friendlyURL"
		>
			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand"
				name="name"
				value="<%= HtmlUtil.escape(inheritedSite.getDescriptiveName(locale)) %>"
			/>

			<%
			List<UserGroupRole> inheritedRoles = new ArrayList<UserGroupRole>();
			int inheritedRolesCount = 0;

			if (selUser != null) {
				inheritedRoles = UserGroupRoleLocalServiceUtil.getUserGroupRoles(selUser.getUserId(), inheritedSite.getGroupId(), 0, PropsValues.USERS_ADMIN_ROLE_COLUMN_LIMIT);
				inheritedRolesCount = UserGroupRoleLocalServiceUtil.getUserGroupRolesCount(selUser.getUserId(), inheritedSite.getGroupId());
			}
			%>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand"
				name="roles"
				value="<%= HtmlUtil.escape(UsersAdminUtil.getUserColumnText(locale, inheritedRoles, UsersAdmin.USER_GROUP_ROLE_TITLE_ACCESSOR, inheritedRolesCount)) %>"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</c:if>