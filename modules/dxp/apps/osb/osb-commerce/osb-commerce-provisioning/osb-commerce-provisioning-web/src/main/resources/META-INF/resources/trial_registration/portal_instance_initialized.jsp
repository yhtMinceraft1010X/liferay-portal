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
long commerceOrderItemId = ParamUtil.getLong(request, "commerceOrderItemId");

TrialRegistrationDisplayContext trialRegistrationDisplayContext = (TrialRegistrationDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<div class="container" id="trial-registration">
	<div class="row">
		<div class="col-md-6 col-xs-12">
			<div>
				<h1><%= LanguageUtil.get(request, "welcome-to-your-demo") %></h1>
			</div>

			<div class="instance-status">
				<p><%= LanguageUtil.format(request, "welcome-message", "hello@liferay.com") %></p>
				<p><%= LanguageUtil.get(request, "trial-period-expires-in-days") %></p>

				<a class="btn btn-primary" href="<%= trialRegistrationDisplayContext.getPortalInstanceURL(commerceOrderItemId) %>" role="button" target="_blank">
					<%= LanguageUtil.get(request, "start-your-demo") %>
				</a>
			</div>
		</div>

		<div class="col-md-6 col-xs-12">
		</div>
	</div>
</div>