<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/init.jsp" %>

<%
CommerceAccountDisplayContext commerceAccountDisplayContext = (CommerceAccountDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceAccount commerceAccount = commerceAccountDisplayContext.getCurrentCommerceAccount();
User selectedUser = commerceAccountDisplayContext.getSelectedUser();
%>

<portlet:actionURL name="editCommerceAccountUser" var="editCommerceAccountUserActionURL" />

<div class="mt-3 user-management">
	<aui:form action="<%= editCommerceAccountUserActionURL %>" method="post" name="fm">
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="commerceAccountId" type="hidden" value="<%= commerceAccount.getCommerceAccountId() %>" />
		<aui:input name="userId" type="hidden" value="<%= selectedUser.getUserId() %>" />

		<liferay-ui:error-marker
			key="<%= WebKeys.ERROR_SECTION %>"
			value="details"
		/>

		<aui:model-context bean="<%= selectedUser %>" model="<%= User.class %>" />

		<div class="row">
			<div class="col-lg-8">
				<aui:input bean="<%= selectedUser %>" model="<%= User.class %>" name="firstName" required="<%= true %>" />

				<aui:input bean="<%= selectedUser %>" model="<%= User.class %>" name="lastName" required="<%= true %>" />
			</div>
		</div>

		<div class="row">
			<div class="col-lg-8">
				<liferay-ui:error exception="<%= UserEmailAddressException.MustNotBeDuplicate.class %>" focusField="emailAddress" message="the-email-address-you-requested-is-already-taken" />
				<liferay-ui:error exception="<%= UserEmailAddressException.MustNotBeNull.class %>" focusField="emailAddress" message="please-enter-an-email-address" />
				<liferay-ui:error exception="<%= UserEmailAddressException.MustNotBePOP3User.class %>" focusField="emailAddress" message="the-email-address-you-requested-is-reserved" />
				<liferay-ui:error exception="<%= UserEmailAddressException.MustNotBeReserved.class %>" focusField="emailAddress" message="the-email-address-you-requested-is-reserved" />
				<liferay-ui:error exception="<%= UserEmailAddressException.MustNotUseCompanyMx.class %>" focusField="emailAddress" message="the-email-address-you-requested-is-not-valid-because-its-domain-is-reserved" />
				<liferay-ui:error exception="<%= UserEmailAddressException.MustValidate.class %>" focusField="emailAddress" message="please-enter-a-valid-email-address" />

				<%
				User displayEmailAddressUser = (User)selectedUser.clone();

				displayEmailAddressUser.setEmailAddress(displayEmailAddressUser.getDisplayEmailAddress());
				%>

				<aui:input bean="<%= displayEmailAddressUser %>" model="<%= User.class %>" name="emailAddress">

					<%
					if (PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.USERS_EMAIL_ADDRESS_REQUIRED)) {
					%>

						<aui:validator name="required" />

					<%
					}
					%>

				</aui:input>
			</div>
		</div>

		<div class="row">
			<liferay-ui:error exception="<%= UserPasswordException.MustBeLonger.class %>">

				<%
				UserPasswordException.MustBeLonger userPasswordExceptionMustBeLonger = (UserPasswordException.MustBeLonger)errorException;
				%>

				<liferay-ui:message arguments="<%= String.valueOf(userPasswordExceptionMustBeLonger.minLength) %>" key="that-password-is-too-short" translateArguments="<%= false %>" />
			</liferay-ui:error>

			<liferay-ui:error exception="<%= UserPasswordException.MustComplyWithModelListeners.class %>" message="that-password-is-invalid-please-enter-a-different-password" />

			<liferay-ui:error exception="<%= UserPasswordException.MustComplyWithRegex.class %>">

				<%
				UserPasswordException.MustComplyWithRegex userPasswordExceptionMustComplyWithRegex = (UserPasswordException.MustComplyWithRegex)errorException;
				%>

				<liferay-ui:message arguments="<%= userPasswordExceptionMustComplyWithRegex.regex %>" key="that-password-does-not-comply-with-the-regular-expression" translateArguments="<%= false %>" />
			</liferay-ui:error>

			<liferay-ui:error exception="<%= UserPasswordException.MustMatch.class %>" message="the-passwords-you-entered-do-not-match" />
			<liferay-ui:error exception="<%= UserPasswordException.MustMatchCurrentPassword.class %>" message="the-password-you-entered-for-the-current-password-does-not-match-your-current-password" />
			<liferay-ui:error exception="<%= UserPasswordException.MustNotBeChanged.class %>" message="passwords-may-not-be-changed-under-the-current-password-policy" />

			<liferay-ui:error exception="<%= UserPasswordException.MustNotBeChangedYet.class %>">

				<%
				UserPasswordException.MustNotBeChangedYet userPasswordExceptionMustNotBeChangedYet = (UserPasswordException.MustNotBeChangedYet)errorException;
				%>

				<liferay-ui:message arguments="<%= String.valueOf(userPasswordExceptionMustNotBeChangedYet.changeableDate) %>" key="you-cannot-change-your-password-yet" translateArguments="<%= false %>" />
			</liferay-ui:error>

			<liferay-ui:error exception="<%= UserPasswordException.MustNotBeEqualToCurrent.class %>" message="your-new-password-cannot-be-the-same-as-your-old-password-please-enter-a-different-password" />
			<liferay-ui:error exception="<%= UserPasswordException.MustNotBeNull.class %>" message="the-password-cannot-be-blank" />
			<liferay-ui:error exception="<%= UserPasswordException.MustNotBeRecentlyUsed.class %>" message="that-password-has-already-been-used-please-enter-a-different-password" />
			<liferay-ui:error exception="<%= UserPasswordException.MustNotBeTrivial.class %>" message="that-password-uses-common-words-please-enter-a-password-that-is-harder-to-guess-i-e-contains-a-mix-of-numbers-and-letters" />
			<liferay-ui:error exception="<%= UserPasswordException.MustNotContainDictionaryWords.class %>" message="that-password-uses-common-dictionary-words" />

			<div class="col-lg-8">
				<aui:input autocomplete="off" label="new-password" name="password1" size="30" type="password" />
			</div>
		</div>

		<div class="row">
			<div class="col-lg-8">
				<aui:input autocomplete="off" label="enter-again" name="password2" size="30" type="password">
					<aui:validator name="equalTo">
						'#<portlet:namespace />password1'
					</aui:validator>
				</aui:input>
			</div>
		</div>

		<div class="commerce-cta is-visible">
			<aui:button cssClass="btn-lg" primary="<%= true %>" type="submit" />
		</div>
	</aui:form>
</div>