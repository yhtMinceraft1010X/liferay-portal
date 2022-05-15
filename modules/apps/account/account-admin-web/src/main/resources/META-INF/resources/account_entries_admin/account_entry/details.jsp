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
AccountEntryDisplay accountEntryDisplay = (AccountEntryDisplay)request.getAttribute(AccountWebKeys.ACCOUNT_ENTRY_DISPLAY);

String backURL = ParamUtil.getString(request, "backURL", String.valueOf(renderResponse.createRenderURL()));

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(backURL);

renderResponse.setTitle((accountEntryDisplay.getAccountEntryId() == 0) ? LanguageUtil.get(request, "add-account") : LanguageUtil.format(request, "edit-x", accountEntryDisplay.getName(), false));
%>

<portlet:actionURL name="/account_admin/edit_account_entry" var="editAccountURL" />

<liferay-frontend:edit-form
	action="<%= editAccountURL %>"
>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= (accountEntryDisplay.getAccountEntryId() == 0) ? Constants.ADD : Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

	<liferay-frontend:edit-form-body>
		<h2 class="sheet-title">
			<%= LanguageUtil.get(request, "information") %>
		</h2>

		<liferay-frontend:fieldset-group>
			<liferay-util:include page="/account_entries_admin/account_entry/display_data.jsp" servletContext="<%= application %>" />

			<c:choose>
				<c:when test="<%= Objects.equals(AccountConstants.ACCOUNT_ENTRY_TYPE_BUSINESS, accountEntryDisplay.getType()) && accountEntryDisplay.isEmailDomainValidationEnabled(themeDisplay.getCompanyId()) %>">
					<div class="business-account-only">
						<liferay-util:include page="/account_entries_admin/account_entry/domains.jsp" servletContext="<%= application %>" />
					</div>
				</c:when>
				<c:when test="<%= Objects.equals(AccountConstants.ACCOUNT_ENTRY_TYPE_PERSON, accountEntryDisplay.getType()) %>">
					<div class="person-account-only">
						<liferay-util:include page="/account_entries_admin/account_entry/person_account_entry_user.jsp" servletContext="<%= application %>" />
					</div>
				</c:when>
			</c:choose>

			<c:if test="<%= accountEntryDisplay.getAccountEntryId() > 0 %>">
				<liferay-util:include page="/account_entries_admin/account_entry/default_addresses.jsp" servletContext="<%= application %>" />
			</c:if>

			<liferay-util:include page="/account_entries_admin/account_entry/categorization.jsp" servletContext="<%= application %>" />

			<liferay-util:include page="/account_entries_admin/account_entry/custom_fields.jsp" servletContext="<%= application %>" />
		</liferay-frontend:fieldset-group>
	</liferay-frontend:edit-form-body>

	<liferay-frontend:edit-form-footer>
		<aui:button type="submit" />

		<aui:button href="<%= backURL %>" type="cancel" />
	</liferay-frontend:edit-form-footer>
</liferay-frontend:edit-form>

<c:if test="<%= accountEntryDisplay.getAccountEntryId() == 0 %>">
	<liferay-frontend:component
		componentId="AccountEntriesAdminPortlet"
		module="account_entries_admin/js/AccountEntriesAdminPortlet.es"
	/>
</c:if>