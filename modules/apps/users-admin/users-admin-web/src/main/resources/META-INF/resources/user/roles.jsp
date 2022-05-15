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

List<Group> groups = new ArrayList<>();

groups.addAll(userDisplayContext.getGroups());
groups.addAll(userDisplayContext.getInheritedSiteGroups());

List<Organization> organizations = userDisplayContext.getOrganizations();

Long[] organizationIds = UsersAdminUtil.getOrganizationIds(organizations);

List<Role> roles = userDisplayContext.getRoles();
List<UserGroupRole> organizationRoles = userDisplayContext.getOrganizationRoles();
List<UserGroupRole> siteRoles = userDisplayContext.getSiteRoles();
List<UserGroupGroupRole> inheritedSiteRoles = userDisplayContext.getInheritedSiteRoles();
List<Group> roleGroups = userDisplayContext.getRoleGroups();

currentURLObj.setParameter("historyKey", liferayPortletResponse.getNamespace() + "roles");
%>

<liferay-util:dynamic-include key="com.liferay.users.admin.web#/user/roles.jsp#pre" />

<liferay-ui:error-marker
	key="<%= WebKeys.ERROR_SECTION %>"
	value="roles"
/>

<liferay-ui:membership-policy-error />

<liferay-util:buffer
	var="removeRoleIcon"
>
	<liferay-ui:icon
		icon="times-circle"
		markupView="lexicon"
		message="remove"
	/>
</liferay-util:buffer>

<aui:input name="addGroupRolesGroupIds" type="hidden" />
<aui:input name="addGroupRolesRoleIds" type="hidden" />
<aui:input name="addRoleIds" type="hidden" />
<aui:input name="deleteGroupRolesGroupIds" type="hidden" />
<aui:input name="deleteGroupRolesRoleIds" type="hidden" />
<aui:input name="deleteRoleIds" type="hidden" />

<clay:sheet-section>
	<clay:content-row
		containerElement="h3"
		cssClass="sheet-subtitle"
	>
		<clay:content-col
			expand="<%= true %>"
		>
			<span class="heading-text"><liferay-ui:message key="regular-roles" /></span>
		</clay:content-col>

		<c:if test="<%= !portletName.equals(myAccountPortletId) %>">
			<clay:content-col>
				<span class="heading-end">
					<liferay-ui:icon
						cssClass="modify-link"
						id="selectRegularRoleLink"
						label="<%= true %>"
						linkCssClass="btn btn-secondary btn-sm"
						message="select"
						method="get"
						url="javascript:;"
					/>
				</span>
			</clay:content-col>
		</c:if>
	</clay:content-row>

	<liferay-ui:search-container
		compactEmptyResultsMessage="<%= true %>"
		cssClass="lfr-search-container-roles"
		curParam="regularRolesCur"
		emptyResultsMessage="this-user-is-not-assigned-any-regular-roles"
		headerNames="title,null"
		id="rolesSearchContainer"
		iteratorURL="<%= currentURLObj %>"
		total="<%= roles.size() %>"
	>
		<liferay-ui:search-container-results
			calculateStartAndEnd="<%= true %>"
			results="<%= roles %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.portal.kernel.model.Role"
			keyProperty="roleId"
			modelVar="role"
		>
			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand"
				name="title"
			>
				<liferay-ui:icon
					iconCssClass="<%= RolesAdminUtil.getIconCssClass(role) %>"
					label="<%= true %>"
					message="<%= HtmlUtil.escape(role.getTitle(locale)) %>"
				/>
			</liferay-ui:search-container-column-text>

			<c:if test="<%= !portletName.equals(myAccountPortletId) && !RoleMembershipPolicyUtil.isRoleRequired(selUser.getUserId(), role.getRoleId()) %>">
				<liferay-ui:search-container-column-text>
					<a class="modify-link" data-rowId="<%= role.getRoleId() %>" href="javascript:;"><%= removeRoleIcon %></a>
				</liferay-ui:search-container-column-text>
			</c:if>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			markupView="lexicon"
		/>
	</liferay-ui:search-container>

	<c:if test="<%= !portletName.equals(myAccountPortletId) %>">
		<aui:script sandbox="<%= true %>" use="liferay-search-container">
			var selectRegularRoleLink = document.getElementById(
				'<portlet:namespace />selectRegularRoleLink'
			);

			if (selectRegularRoleLink) {
				selectRegularRoleLink.addEventListener('click', (event) => {
					var searchContainerName = '<portlet:namespace />rolesSearchContainer';

					var searchContainer = Liferay.SearchContainer.get(searchContainerName);

					Liferay.Util.openSelectionModal({
						onSelect: function (event) {
							<portlet:namespace />selectRole(
								event.entityid,
								event.entityname,
								event.searchcontainername,
								event.groupdescriptivename,
								event.groupid,
								event.iconcssclass
							);
						},

						<%
						String regularRoleEventName = liferayPortletResponse.getNamespace() + "selectRegularRole";
						%>

						selectEventName: '<%= regularRoleEventName %>',
						selectedData: searchContainer.getData(true),
						title:
							'<liferay-ui:message arguments="regular-role" key="select-x" />',

						<%
						PortletURL selectRegularRoleURL = PortletURLBuilder.create(
							PortletProviderUtil.getPortletURL(request, Role.class.getName(), PortletProvider.Action.BROWSE)
						).setParameter(
							"eventName", regularRoleEventName
						).setParameter(
							"p_u_i_d", (selUser == null) ? "0" : String.valueOf(selUser.getUserId())
						).setWindowState(
							LiferayWindowState.POP_UP
						).buildPortletURL();
						%>

						url: '<%= selectRegularRoleURL.toString() %>',
					});
				});
			}
		</aui:script>
	</c:if>

	<c:if test="<%= !roleGroups.isEmpty() %>">
		<h4 class="sheet-tertiary-title"><liferay-ui:message key="inherited-regular-roles" /></h4>

		<liferay-ui:search-container
			cssClass="lfr-search-container-inherited-regular-roles"
			curParam="inheritedRegularRolesCur"
			headerNames="title,group"
			id="inheritedRolesSearchContainer"
			iteratorURL="<%= currentURLObj %>"
			total="<%= roleGroups.size() %>"
		>
			<liferay-ui:search-container-results
				calculateStartAndEnd="<%= true %>"
				results="<%= roleGroups %>"
			/>

			<liferay-ui:search-container-row
				className="com.liferay.portal.kernel.model.Group"
				keyProperty="groupId"
				modelVar="group"
				rowIdProperty="friendlyURL"
			>

				<%
				List<Role> groupRoles = RoleLocalServiceUtil.getGroupRoles(group.getGroupId());
				%>

				<liferay-ui:search-container-column-text
					cssClass="table-cell-expand"
					name="title"
					value="<%= HtmlUtil.escape(ListUtil.toString(groupRoles, Role.NAME_ACCESSOR)) %>"
				>
					<liferay-ui:icon
						iconCssClass="<%= RolesAdminUtil.getIconCssClass(groupRoles.get(0)) %>"
						label="<%= true %>"
						message="<%= HtmlUtil.escape(ListUtil.toString(groupRoles, Role.NAME_ACCESSOR)) %>"
					/>
				</liferay-ui:search-container-column-text>

				<liferay-ui:search-container-column-text
					name="group"
					value="<%= HtmlUtil.escape(group.getDescriptiveName(locale)) %>"
				/>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				markupView="lexicon"
			/>
		</liferay-ui:search-container>
	</c:if>
</clay:sheet-section>

<clay:sheet-section>
	<clay:content-row
		containerElement="h3"
		cssClass="sheet-subtitle"
	>
		<clay:content-col
			expand="<%= true %>"
		>
			<span class="heading-text"><liferay-ui:message key="organization-roles" /></span>
		</clay:content-col>

		<c:if test="<%= !portletName.equals(myAccountPortletId) && (!organizations.isEmpty() || !organizationRoles.isEmpty()) %>">
			<clay:content-col>
				<span class="heading-end">
					<liferay-ui:icon
						cssClass="modify-link"
						id="selectOrganizationRoleLink"
						label="<%= true %>"
						linkCssClass="btn btn-secondary btn-sm"
						message="select"
						method="get"
						url="javascript:;"
					/>
				</span>
			</clay:content-col>
		</c:if>
	</clay:content-row>

	<c:if test="<%= organizations.isEmpty() && organizationRoles.isEmpty() %>">
		<div class="text-muted"><liferay-ui:message key="this-user-does-not-belong-to-an-organization-to-which-an-organization-role-can-be-assigned" /></div>
	</c:if>

	<c:if test="<%= !organizations.isEmpty() %>">
		<liferay-ui:search-container
			compactEmptyResultsMessage="<%= true %>"
			cssClass="lfr-search-container-organization-roles"
			curParam="organizationRolesCur"
			emptyResultsMessage="this-user-is-not-assigned-any-organization-roles"
			headerNames="title,organization,null"
			id="organizationRolesSearchContainer"
			iteratorURL="<%= currentURLObj %>"
			total="<%= organizationRoles.size() %>"
		>
			<liferay-ui:search-container-results
				calculateStartAndEnd="<%= true %>"
				results="<%= organizationRoles %>"
			/>

			<liferay-ui:search-container-row
				className="com.liferay.portal.kernel.model.UserGroupRole"
				keyProperty="roleId"
				modelVar="userGroupRole"
			>
				<liferay-ui:search-container-column-text
					cssClass="table-cell-expand"
					name="title"
				>
					<liferay-ui:icon
						iconCssClass="<%= RolesAdminUtil.getIconCssClass(userGroupRole.getRole()) %>"
						label="<%= true %>"
						message="<%= HtmlUtil.escape(userGroupRole.getRole().getTitle(locale)) %>"
					/>
				</liferay-ui:search-container-column-text>

				<liferay-ui:search-container-column-text
					cssClass="table-cell-expand"
					name="organization"
					value="<%= HtmlUtil.escape(userGroupRole.getGroup().getDescriptiveName(locale)) %>"
				/>

				<%
				boolean membershipProtected = false;

				Group group = userGroupRole.getGroup();

				Role role = userGroupRole.getRole();

				if (role.getType() == RoleConstants.TYPE_ORGANIZATION) {
					membershipProtected = OrganizationMembershipPolicyUtil.isMembershipProtected(permissionChecker, userGroupRole.getUserId(), group.getOrganizationId());
				}
				else {
					membershipProtected = SiteMembershipPolicyUtil.isMembershipProtected(permissionChecker, userGroupRole.getUserId(), group.getGroupId());
				}
				%>

				<c:if test="<%= !portletName.equals(myAccountPortletId) && !membershipProtected %>">
					<liferay-ui:search-container-column-text>
						<a class="modify-link" data-groupId="<%= userGroupRole.getGroupId() %>" data-rowId="<%= userGroupRole.getRoleId() %>" href="javascript:;"><%= removeRoleIcon %></a>
					</liferay-ui:search-container-column-text>
				</c:if>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				markupView="lexicon"
			/>
		</liferay-ui:search-container>

		<c:if test="<%= !portletName.equals(myAccountPortletId) %>">
			<aui:script use="liferay-search-container">
				var Util = Liferay.Util;

				var searchContainerName =
					'<portlet:namespace />organizationRolesSearchContainer';

				var searchContainer = Liferay.SearchContainer.get(searchContainerName);

				<portlet:namespace />searchContainerUpdateDataStore(searchContainer);

				var searchContainerContentBox = searchContainer.get('contentBox');

				searchContainerContentBox.delegate(
					'click',
					(event) => {
						var link = event.currentTarget;
						var tr = link.ancestor('tr');

						var groupId = link.getAttribute('data-groupId');
						var rowId = link.getAttribute('data-rowId');
						var id = groupId + '-' + rowId;

						var selectOrganizationRole = Util.getWindow(
							'<portlet:namespace />selectOrganizationRole'
						);

						if (selectOrganizationRole) {
							var selectButton = selectOrganizationRole.iframe.node
								.get('contentWindow.document')
								.one(
									'.selector-button[data-groupid="' +
										groupId +
										'"][data-entityid="' +
										rowId +
										'"]'
								);

							Util.toggleDisabled(selectButton, false);
						}

						searchContainer.deleteRow(tr, id);

						<portlet:namespace />deleteGroupRole(rowId, groupId);
					},
					'.modify-link'
				);
			</aui:script>
		</c:if>
	</c:if>

	<c:if test="<%= !organizations.isEmpty() && !portletName.equals(myAccountPortletId) %>">
		<aui:script sandbox="<%= true %>" use="liferay-search-container">
			var selectOrganizationRoleLink = document.getElementById(
				'<portlet:namespace />selectOrganizationRoleLink'
			);

			if (selectOrganizationRoleLink) {
				selectOrganizationRoleLink.addEventListener('click', (event) => {
					var searchContainerName =
						'<portlet:namespace />organizationRolesSearchContainer';

					var searchContainer = Liferay.SearchContainer.get(searchContainerName);

					Liferay.Util.openSelectionModal({
						onSelect: function (event) {
							<portlet:namespace />selectRole(
								event.entityid,
								event.entityname,
								event.searchcontainername,
								event.groupdescriptivename,
								event.groupid,
								event.iconcssclass
							);
						},

						<%
						String groupEventName = liferayPortletResponse.getNamespace() + "selectOrganization";
						String organizationRoleEventName = liferayPortletResponse.getNamespace() + "selectOrganizationRole";
						%>

						selectEventName: '<%= organizationRoleEventName %>',
						title:
							'<liferay-ui:message arguments="organization-role" key="select-x" />',

						<%
						PortletURL selectOrganizationRoleURL = PortletURLBuilder.create(
							PortletProviderUtil.getPortletURL(request, Role.class.getName(), PortletProvider.Action.BROWSE)
						).setParameter(
							"eventName", organizationRoleEventName
						).setParameter(
							"groupEventName", groupEventName
						).setParameter(
							"organizationIds", StringUtil.merge(organizationIds)
						).setParameter(
							"p_u_i_d", (selUser == null) ? "0" : String.valueOf(selUser.getUserId())
						).setParameter(
							"roleType", RoleConstants.TYPE_ORGANIZATION
						).setParameter(
							"step", "1"
						).setWindowState(
							LiferayWindowState.POP_UP
						).buildPortletURL();
						%>

						url: '<%= selectOrganizationRoleURL.toString() %>',
					});

					Liferay.on('<%= groupEventName %>', () => {
						const iframe = document.querySelector('.liferay-modal iframe');

						if (iframe) {
							const iframeDocument = iframe.contentWindow.document;

							const selectedDataSet = new Set(searchContainer.getData(true));

							const selectButtons = iframeDocument.querySelectorAll(
								'.selector-button'
							);

							selectButtons.forEach((selectButton) => {
								const selectButtonId =
									selectButton.dataset.groupid +
									'-' +
									selectButton.dataset.entityid;

								if (selectedDataSet.has(selectButtonId)) {
									selectButton.disabled = true;
									selectButton.classList.add('disabled');
								}
								else {
									selectButton.disabled = false;
									selectButton.classList.remove('disabled');
								}
							});
						}
					});
				});
			}
		</aui:script>
	</c:if>
</clay:sheet-section>

<clay:sheet-section>
	<clay:content-row
		containerElement="h3"
		cssClass="sheet-subtitle"
	>
		<clay:content-col
			expand="<%= true %>"
		>
			<span class="heading-text"><liferay-ui:message key="site-roles" /></span>
		</clay:content-col>

		<c:if test="<%= !portletName.equals(myAccountPortletId) && (!groups.isEmpty() || !siteRoles.isEmpty()) %>">
			<clay:content-col>
				<span class="heading-end">
					<liferay-ui:icon
						cssClass="modify-link"
						id="selectSiteRoleLink"
						label="<%= true %>"
						linkCssClass="btn btn-secondary btn-sm"
						message="select"
						method="get"
						url="javascript:;"
					/>
				</span>
			</clay:content-col>
		</c:if>
	</clay:content-row>

	<c:if test="<%= groups.isEmpty() && siteRoles.isEmpty() %>">
		<div class="text-muted"><liferay-ui:message key="this-user-does-not-belong-to-a-site-to-which-a-site-role-can-be-assigned" /></div>
	</c:if>

	<c:if test="<%= !groups.isEmpty() %>">
		<liferay-ui:search-container
			compactEmptyResultsMessage="<%= true %>"
			cssClass="lfr-search-container-site-roles"
			curParam="siteRolesCur"
			emptyResultsMessage="this-user-is-not-assigned-any-site-roles"
			headerNames="title,site,null"
			id="siteRolesSearchContainer"
			iteratorURL="<%= currentURLObj %>"
			total="<%= siteRoles.size() %>"
		>
			<liferay-ui:search-container-results
				calculateStartAndEnd="<%= true %>"
				results="<%= siteRoles %>"
			/>

			<liferay-ui:search-container-row
				className="com.liferay.portal.kernel.model.UserGroupRole"
				keyProperty="roleId"
				modelVar="userGroupRole"
			>
				<liferay-ui:search-container-column-text
					cssClass="table-cell-expand"
					name="title"
				>
					<liferay-ui:icon
						iconCssClass="<%= RolesAdminUtil.getIconCssClass(userGroupRole.getRole()) %>"
						label="<%= true %>"
						message="<%= HtmlUtil.escape(userGroupRole.getRole().getTitle(locale)) %>"
					/>
				</liferay-ui:search-container-column-text>

				<liferay-ui:search-container-column-text
					cssClass="table-cell-expand"
					name="site"
				>
					<liferay-staging:descriptive-name
						group="<%= userGroupRole.getGroup() %>"
					/>
				</liferay-ui:search-container-column-text>

				<%
				boolean membershipProtected = false;

				Group group = userGroupRole.getGroup();

				Role role = userGroupRole.getRole();

				if (role.getType() == RoleConstants.TYPE_ORGANIZATION) {
					membershipProtected = OrganizationMembershipPolicyUtil.isMembershipProtected(permissionChecker, userGroupRole.getUserId(), group.getOrganizationId());
				}
				else {
					membershipProtected = SiteMembershipPolicyUtil.isMembershipProtected(permissionChecker, userGroupRole.getUserId(), group.getGroupId());
				}
				%>

				<c:if test="<%= !portletName.equals(myAccountPortletId) && !membershipProtected %>">
					<liferay-ui:search-container-column-text>
						<a class="modify-link" data-groupId="<%= userGroupRole.getGroupId() %>" data-rowId="<%= userGroupRole.getRoleId() %>" href="javascript:;"><%= removeRoleIcon %></a>
					</liferay-ui:search-container-column-text>
				</c:if>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				markupView="lexicon"
			/>
		</liferay-ui:search-container>

		<c:if test="<%= !portletName.equals(myAccountPortletId) %>">
			<aui:script use="liferay-search-container">
				var Util = Liferay.Util;

				var searchContainerName = '<portlet:namespace />siteRolesSearchContainer';

				var searchContainer = Liferay.SearchContainer.get(searchContainerName);

				<portlet:namespace />searchContainerUpdateDataStore(searchContainer);

				var searchContainerContentBox = searchContainer.get('contentBox');

				searchContainerContentBox.delegate(
					'click',
					(event) => {
						var link = event.currentTarget;
						var tr = link.ancestor('tr');

						var groupId = link.getAttribute('data-groupId');
						var rowId = link.getAttribute('data-rowId');
						var id = groupId + '-' + rowId;

						var selectSiteRole = Util.getWindow(
							'<portlet:namespace />selectSiteRole'
						);

						if (selectSiteRole) {
							var selectButton = selectSiteRole.iframe.node
								.get('contentWindow.document')
								.one(
									'.selector-button[data-groupid="' +
										groupId +
										'"][data-entityid="' +
										rowId +
										'"]'
								);

							Util.toggleDisabled(selectButton, false);
						}

						searchContainer.deleteRow(tr, id);

						<portlet:namespace />deleteGroupRole(rowId, groupId);
					},
					'.modify-link'
				);

				const selectSiteRoleLink = document.getElementById(
					'<portlet:namespace />selectSiteRoleLink'
				);

				if (selectSiteRoleLink) {
					selectSiteRoleLink.addEventListener('click', (event) => {
						Util.openSelectionModal({
							onSelect: (selectedItem) => {
								<portlet:namespace />selectRole(
									selectedItem.entityid,
									selectedItem.entityname,
									selectedItem.searchcontainername,
									selectedItem.groupdescriptivename,
									selectedItem.groupid,
									selectedItem.iconcssclass
								);
							},

							<%
							String groupEventName = liferayPortletResponse.getNamespace() + "selectSite";
							String siteRoleEventName = liferayPortletResponse.getNamespace() + "selectSiteRole";
							%>

							selectEventName: '<%= siteRoleEventName %>',

							title:
								'<liferay-ui:message arguments="site-role" key="select-x" />',
							url:
								'<%=
									PortletURLBuilder.create(
										PortletProviderUtil.getPortletURL(request, Role.class.getName(), PortletProvider.Action.BROWSE)
									).setParameter(
										"eventName", siteRoleEventName
									).setParameter(
										"groupEventName", groupEventName
									).setParameter(
										"p_u_i_d", (selUser == null) ? "0" : String.valueOf(selUser.getUserId())
									).setParameter(
										"roleType", RoleConstants.TYPE_SITE
									).setParameter(
										"step", "1"
									).setWindowState(
										LiferayWindowState.POP_UP
									).buildPortletURL()
								%>',
						});
					});

					Liferay.on('<%= groupEventName %>', () => {
						const iframe = document.querySelector('.liferay-modal iframe');

						if (iframe) {
							const iframeDocument = iframe.contentWindow.document;

							const selectedDataSet = new Set(searchContainer.getData(true));

							const selectButtons = iframeDocument.querySelectorAll(
								'.selector-button'
							);

							selectButtons.forEach((selectButton) => {
								const selectButtonId =
									selectButton.dataset.groupid +
									'-' +
									selectButton.dataset.entityid;

								if (selectedDataSet.has(selectButtonId)) {
									selectButton.disabled = true;
									selectButton.classList.add('disabled');
								}
								else {
									selectButton.disabled = false;
									selectButton.classList.remove('disabled');
								}
							});
						}
					});
				}
			</aui:script>
		</c:if>
	</c:if>

	<c:if test="<%= !inheritedSiteRoles.isEmpty() %>">
		<h4 class="sheet-tertiary-title"><liferay-ui:message key="inherited-site-roles" /></h4>

		<liferay-ui:search-container
			cssClass="lfr-search-container-inherited-site-roles"
			curParam="inheritedSiteRolesCur"
			headerNames="title,site,user-group"
			id="inheritedSiteRolesSearchContainer"
			iteratorURL="<%= currentURLObj %>"
			total="<%= inheritedSiteRoles.size() %>"
		>
			<liferay-ui:search-container-results
				calculateStartAndEnd="<%= true %>"
				results="<%= inheritedSiteRoles %>"
			/>

			<liferay-ui:search-container-row
				className="com.liferay.portal.kernel.model.UserGroupGroupRole"
				keyProperty="roleId"
				modelVar="userGroupGroupRole"
			>
				<liferay-ui:search-container-column-text
					cssClass="table-cell-expand"
					name="title"
				>
					<liferay-ui:icon
						iconCssClass="<%= RolesAdminUtil.getIconCssClass(userGroupGroupRole.getRole()) %>"
						label="<%= true %>"
						message="<%= HtmlUtil.escape(userGroupGroupRole.getRole().getTitle(locale)) %>"
					/>
				</liferay-ui:search-container-column-text>

				<liferay-ui:search-container-column-text
					cssClass="table-cell-expand"
					name="site"
				>
					<liferay-staging:descriptive-name
						group="<%= userGroupGroupRole.getGroup() %>"
					/>
				</liferay-ui:search-container-column-text>

				<liferay-ui:search-container-column-text
					cssClass="table-cell-expand"
					name="user-group"
					value="<%= HtmlUtil.escape(userGroupGroupRole.getUserGroup().getName()) %>"
				/>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				markupView="lexicon"
			/>
		</liferay-ui:search-container>
	</c:if>

	<c:if test="<%= !portletName.equals(myAccountPortletId) %>">
		<aui:script>
			var <portlet:namespace />addRoleIds = [];
			var <portlet:namespace />deleteRoleIds = [];

			var <portlet:namespace />addGroupRolesGroupIds = [];
			var <portlet:namespace />addGroupRolesRoleIds = [];
			var <portlet:namespace />deleteGroupRolesGroupIds = [];
			var <portlet:namespace />deleteGroupRolesRoleIds = [];

			function <portlet:namespace />deleteRegularRole(roleId) {
				var A = AUI();

				A.Array.removeItem(<portlet:namespace />addRoleIds, roleId);

				<portlet:namespace />deleteRoleIds.push(roleId);

				document.<portlet:namespace />fm.<portlet:namespace />addRoleIds.value = <portlet:namespace />addRoleIds.join(
					','
				);
				document.<portlet:namespace />fm.<portlet:namespace />deleteRoleIds.value = <portlet:namespace />deleteRoleIds.join(
					','
				);
			}

			function <portlet:namespace />deleteGroupRole(roleId, groupId) {
				for (var i = 0; i < <portlet:namespace />addGroupRolesRoleIds.length; i++) {
					if (
						<portlet:namespace />addGroupRolesGroupIds[i] === groupId &&
						<portlet:namespace />addGroupRolesRoleIds[i] === roleId
					) {
						<portlet:namespace />addGroupRolesGroupIds.splice(i, 1);
						<portlet:namespace />addGroupRolesRoleIds.splice(i, 1);

						break;
					}
				}

				<portlet:namespace />deleteGroupRolesGroupIds.push(groupId);
				<portlet:namespace />deleteGroupRolesRoleIds.push(roleId);

				document.<portlet:namespace />fm.<portlet:namespace />addGroupRolesGroupIds.value = <portlet:namespace />addGroupRolesGroupIds.join(
					','
				);
				document.<portlet:namespace />fm.<portlet:namespace />addGroupRolesRoleIds.value = <portlet:namespace />addGroupRolesRoleIds.join(
					','
				);
				document.<portlet:namespace />fm.<portlet:namespace />deleteGroupRolesGroupIds.value = <portlet:namespace />deleteGroupRolesGroupIds.join(
					','
				);
				document.<portlet:namespace />fm.<portlet:namespace />deleteGroupRolesRoleIds.value = <portlet:namespace />deleteGroupRolesRoleIds.join(
					','
				);
			}

			function <portlet:namespace />searchContainerUpdateDataStore(searchContainer) {
				searchContainer.updateDataStore(
					searchContainer
						.get('contentBox')
						.all('.modify-link')
						.getData()
						.map((data) => {
							return data.groupid + '-' + data.rowid;
						})
				);
			}

			window['<portlet:namespace />selectRole'] = function (
				roleId,
				name,
				searchContainer,
				groupName,
				groupId,
				iconCssClass
			) {
				var A = AUI();
				var LString = A.Lang.String;

				var searchContainerName =
					'<portlet:namespace />' + searchContainer + 'SearchContainer';

				searchContainer = Liferay.SearchContainer.get(searchContainerName);

				var rowColumns = [];

				rowColumns.push(
					'<i class="' + iconCssClass + '"></i> ' + Liferay.Util.escapeHTML(name)
				);

				if (groupName) {
					rowColumns.push(Liferay.Util.escapeHTML(groupName));
				}

				if (groupId) {
					rowColumns.push(
						'<a class="modify-link" data-groupId="' +
							groupId +
							'" data-rowId="' +
							roleId +
							'" href="javascript:;"><%= UnicodeFormatter.toString(removeRoleIcon) %></a>'
					);

					for (
						var i = 0;
						i < <portlet:namespace />deleteGroupRolesRoleIds.length;
						i++
					) {
						if (
							<portlet:namespace />deleteGroupRolesGroupIds[i] === groupId &&
							<portlet:namespace />deleteGroupRolesRoleIds[i] === roleId
						) {
							<portlet:namespace />deleteGroupRolesGroupIds.splice(i, 1);
							<portlet:namespace />deleteGroupRolesRoleIds.splice(i, 1);

							break;
						}
					}

					<portlet:namespace />addGroupRolesGroupIds.push(groupId);
					<portlet:namespace />addGroupRolesRoleIds.push(roleId);

					document.<portlet:namespace />fm.<portlet:namespace />addGroupRolesGroupIds.value = <portlet:namespace />addGroupRolesGroupIds.join(
						','
					);
					document.<portlet:namespace />fm.<portlet:namespace />addGroupRolesRoleIds.value = <portlet:namespace />addGroupRolesRoleIds.join(
						','
					);
					document.<portlet:namespace />fm.<portlet:namespace />deleteGroupRolesGroupIds.value = <portlet:namespace />deleteGroupRolesGroupIds.join(
						','
					);
					document.<portlet:namespace />fm.<portlet:namespace />deleteGroupRolesRoleIds.value = <portlet:namespace />deleteGroupRolesRoleIds.join(
						','
					);

					searchContainer.addRow(rowColumns, groupId + '-' + roleId);
				}
				else {
					rowColumns.push(
						'<a class="modify-link" data-rowId="' +
							roleId +
							'" href="javascript:;"><%= UnicodeFormatter.toString(removeRoleIcon) %></a>'
					);

					A.Array.removeItem(<portlet:namespace />deleteRoleIds, roleId);

					<portlet:namespace />addRoleIds.push(roleId);

					document.<portlet:namespace />fm.<portlet:namespace />addRoleIds.value = <portlet:namespace />addRoleIds.join(
						','
					);
					document.<portlet:namespace />fm.<portlet:namespace />deleteRoleIds.value = <portlet:namespace />deleteRoleIds.join(
						','
					);

					searchContainer.addRow(rowColumns, roleId);
				}

				searchContainer.updateDataStore();
			};
		</aui:script>

		<aui:script use="liferay-search-container">
			var Util = Liferay.Util;

			var searchContainer = Liferay.SearchContainer.get(
				'<portlet:namespace />rolesSearchContainer'
			);

			var searchContainerContentBox = searchContainer.get('contentBox');

			searchContainerContentBox.delegate(
				'click',
				(event) => {
					var link = event.currentTarget;

					var rowId = link.attr('data-rowId');

					var tr = link.ancestor('tr');

					var selectRegularRole = Util.getWindow(
						'<portlet:namespace />selectRegularRole'
					);

					if (selectRegularRole) {
						var selectButton = selectRegularRole.iframe.node
							.get('contentWindow.document')
							.one('.selector-button[data-entityid="' + rowId + '"]');

						Util.toggleDisabled(selectButton, false);
					}

					searchContainer.deleteRow(tr, link.getAttribute('data-rowId'));

					<portlet:namespace />deleteRegularRole(rowId);
				},
				'.modify-link'
			);
		</aui:script>
	</c:if>
</clay:sheet-section>

<liferay-util:dynamic-include key="com.liferay.users.admin.web#/user/roles.jsp#post" />