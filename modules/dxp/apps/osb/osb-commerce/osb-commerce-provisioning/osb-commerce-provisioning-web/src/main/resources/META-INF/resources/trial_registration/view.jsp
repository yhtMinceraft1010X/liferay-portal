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
TrialRegistrationDisplayContext trialRegistrationDisplayContext = (TrialRegistrationDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

List<CommerceCountry> commerceCountries = trialRegistrationDisplayContext.getCommerceCountries(company.getCompanyId());
%>

<div class="container py-5" id="trial-registration">
	<div class="row">
		<div class="col-md-6 col-xs-12">
			<div class="punch-line">
				<h1><%= LanguageUtil.get(request, "try-liferay-commerce") %></h1>

				<p><%= LanguageUtil.get(request, "explore-liferay-commerce") %></p>
			</div>
		</div>

		<div class="col-md-6 col-xs-12">
			<div class="form-container">
				<div class="form-title">
					<h3><%= LanguageUtil.get(request, "start-your-private-demo") %></h3>
				</div>

				<liferay-ui:error exception="<%= UserEmailAddressException.MustNotBeDuplicate.class %>" message="the-email-address-you-requested-is-already-taken" />

				<div class="form-wrapper">
					<portlet:actionURL name="registerTrial" var="registerTrialURL" />

					<aui:form action="<%= registerTrialURL %>" method="post" name="fm">
						<aui:input class="form-field" id="name" label='<%= LanguageUtil.get(request, "name") %>' name="name" placeholder='<%= LanguageUtil.get(request, "name") %>' required="<%= true %>" type="input" />

						<aui:input class="form-field" id="workEmail" label='<%= LanguageUtil.get(request, "work-email") %>' name="workEmail" placeholder='<%= LanguageUtil.get(request, "your-work-email") %>' required="<%= true %>" type="email" />

						<aui:input class="form-field" id="password" label='<%= LanguageUtil.get(request, "password") %>' name="password" placeholder='<%= LanguageUtil.get(request, "password") %>' required="<%= true %>" type="password" />

						<aui:input class="form-field" id="jobTitle" label='<%= LanguageUtil.get(request, "job-title") %>' name="jobTitle" placeholder='<%= LanguageUtil.get(request, "job-title") %>' required="<%= true %>" type="text" />

						<aui:input class="form-field" id="companyName" label='<%= LanguageUtil.get(request, "company") %>' name="companyName" placeholder='<%= LanguageUtil.get(request, "company") %>' required="<%= true %>" type="text" />

						<aui:select class="form-field" id="countryCode" label='<%= LanguageUtil.get(request, "country") %>' name="countryCode" required="<%= true %>">

							<%
							for (CommerceCountry commerceCountry : commerceCountries) {
							%>

								<aui:option value="<%= commerceCountry.getTwoLettersISOCode() %>">
									<%= commerceCountry.getName(locale) %>
								</aui:option>

							<%
							}
							%>

						</aui:select>

						<p class="disclaimer">
							<%= LanguageUtil.get(request, "disclaimer") %>
						</p>

						<button class="btn btn-primary" type="submit"><%= LanguageUtil.get(request, "start-trial") %></button>
					</aui:form>
				</div>
			</div>
		</div>
	</div>
</div>