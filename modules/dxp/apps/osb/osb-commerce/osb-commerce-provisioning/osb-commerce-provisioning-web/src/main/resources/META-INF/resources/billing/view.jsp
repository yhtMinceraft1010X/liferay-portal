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
List<CommerceCountry> commerceCountries = commerceAccountDisplayContext.getCommerceCountries(company.getCompanyId());
%>

<portlet:actionURL name="editCommerceAccount" var="editCommerceAccountActionURL" />

<div class="account-management mt-3">
	<aui:form action="<%= editCommerceAccountActionURL %>" method="post" name="fm">
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="commerceAccountId" type="hidden" value="<%= commerceAccount.getCommerceAccountId() %>" />

		<liferay-ui:error-marker
			key="<%= WebKeys.ERROR_SECTION %>"
			value="details"
		/>

		<aui:model-context bean="<%= commerceAccount %>" model="<%= CommerceAccount.class %>" />

		<div class="row">
			<div class="col-lg-8">
				<aui:input label='<%= LanguageUtil.get(request, "name") %>' name="name" />
			</div>
		</div>

		<div class="row">
			<div class="col-lg-8">
				<aui:input label='<%= LanguageUtil.get(request, "email") %>' name="email" />
			</div>
		</div>

		<div class="row">
			<div class="col-lg-8">
				<aui:input label='<%= LanguageUtil.get(request, "vat-number") %>' name="taxId" />
			</div>
		</div>

		<aui:model-context bean="<%= commerceAccountDisplayContext.getBillingCommerceAddress() %>" model="<%= CommerceAddress.class %>" />

		<div class="row">
			<div class="col-lg-8">
				<aui:input label='<%= LanguageUtil.get(request, "address") %>' name="street1" />
			</div>
		</div>

		<div class="row">
			<div class="col-lg-4">
				<aui:select label='<%= LanguageUtil.get(request, "country") %>' name="commerceCountryId">

					<%
					for (CommerceCountry commerceCountry : commerceCountries) {
					%>

						<aui:option value="<%= commerceCountry.getCommerceCountryId() %>">
							<%= commerceCountry.getName(locale) %>
						</aui:option>

					<%
					}
					%>

				</aui:select>
			</div>

			<div class="col-lg-4">
				<aui:select disabled="<%= true %>" label='<%= LanguageUtil.get(request, "region") %>' name="commerceRegionId"></aui:select>
			</div>
		</div>

		<div class="row">
			<div class="col-lg-4">
				<aui:input label='<%= LanguageUtil.get(request, "city") %>' name="city" />
			</div>

			<div class="col-lg-4">
				<aui:input label='<%= LanguageUtil.get(request, "zip") %>' name="zip" />
			</div>
		</div>

		<div class="commerce-cta is-visible">
			<aui:button cssClass="btn-lg" primary="<%= true %>" type="submit" />
		</div>
	</aui:form>
</div>

<aui:script require="osb-commerce-provisioning-web@1.0.0/js/utilities/index as Utils">
	var commerceCountrySelect = document.querySelector(
		'#<portlet:namespace />commerceCountryId'
	);
	var commerceRegionSelect = document.querySelector(
		'#<portlet:namespace />commerceRegionId'
	);

	commerceCountrySelect.addEventListener('change', function (event) {
		var commerceCountryId = event.currentTarget.value;

		Utils.findCommerceRegions(commerceCountryId).then(function (regions) {
			Utils.renderRegionOptions(
				{
					labelKey: 'name',
					valueKey: 'commerceRegionId',
				},
				regions,
				commerceRegionSelect
			);
		});
	});
</aui:script>