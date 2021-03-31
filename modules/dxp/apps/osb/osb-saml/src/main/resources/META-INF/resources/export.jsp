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

<portlet:actionURL name="/saml/saas/admin/export" var="exportURL">
	<portlet:param name="mvcRenderCommandName" value="/admin" />
</portlet:actionURL>

<div class="container-fluid container-fluid-max-xl sheet">
	<liferay-ui:error key="exportError" message="an-error-has-occurred-during-the-export-process" />

	<div class="button-holder">
		<h3 class="text-default">
			<liferay-ui:message key="export-the-saml-configuration-from-this-instance-to-your-production-instance" />
		</h3>

		<div class="alert alert-warning">
			<liferay-ui:message key="the-saml-configuration-of-your-production-instance-will-be-completely-overwritten" />
		</div>

		<aui:form action="<%= exportURL %>" method="post" name="fm">
			<aui:button type="submit" value="export-saml-configuration" />
		</aui:form>
	</div>
</div>