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
String userFirstName = ParamUtil.getString(request, "userFirstName");
%>

<div class="container" id="trial-registration">
	<div class="row">
		<div class="col-md-6 col-xs-12">
			<div>
				<h1><%= LanguageUtil.get(request, "hello") %> <%= userFirstName %></h1>

				<p><%= LanguageUtil.get(request, "loading-instance") %></p>
			</div>

			<div class="instance-status" id="<portlet:namespace />instanceStatus">
				<p><%= LanguageUtil.get(request, "wait-time") %></p>

				<ul>
					<li>
						<span>
							<svg class="lexicon-icon lexicon-icon-search" focusable="false" role="presentation">
								<use xlink:href="<%= themeDisplay.getPathThemeImages() %>/lexicon/icons.svg#check"></use>
							</svg>
						</span>

						<%= LanguageUtil.get(request, "creating-an-unique-link") %>
					</li>
					<li>
						<span>
							<svg class="lexicon-icon lexicon-icon-search" focusable="false" role="presentation">
								<use xlink:href="<%= themeDisplay.getPathThemeImages() %>/lexicon/icons.svg#check"></use>
							</svg>
						</span>

						<%= LanguageUtil.get(request, "setting-up-administrator") %>
					</li>
					<li>
						<span aria-hidden="true" class="loading-animation loading-animation-sm"></span>

						<%= LanguageUtil.get(request, "setting-up-a-new-instance") %>
					</li>
				</ul>
			</div>

			<div class="hide" id="<portlet:namespace />instanceCreationFailure">
				<div class="alert alert-danger">
					<%= LanguageUtil.format(request, "unable-to-create-instance", "hello@liferay.com") %>
				</div>
			</div>
		</div>

		<div class="col-md-6 col-xs-12">
		</div>
	</div>
</div>

<portlet:resourceURL id="portalInstanceStatus" var="portalInstanceStatusResourceURL">
	<portlet:param name="commerceOrderItemId" value="<%= String.valueOf(commerceOrderItemId) %>" />
</portlet:resourceURL>

<portlet:renderURL var="portalInstanceInitializedURL">
	<portlet:param name="mvcRenderCommandName" value="portalInstanceInitialized" />
	<portlet:param name="commerceOrderItemId" value="<%= String.valueOf(commerceOrderItemId) %>" />
</portlet:renderURL>

<aui:script use="aui-base">
	setTimeout(function () {
		function callOnTimeOut() {
			var resourceURL = '<%= portalInstanceStatusResourceURL %>';

			fetch(resourceURL, {
				credentials: 'include',
				headers: new Headers({'x-csrf-token': Liferay.authToken}),
				method: 'post',
			})
				.then(function (res) {
					return res.json();
				})
				.then(function (payload) {
					if (payload.status === 0) {
						window.location = '<%= portalInstanceInitializedURL %>';
					}
					else if (payload.status === 6) {
						A.one('#<portlet:namespace />instanceStatus').hide();
						A.one(
							'#<portlet:namespace />instanceCreationFailure'
						).show();
					}
					else {
						setTimeout(function () {
							callOnTimeOut();
						}, 5000);
					}
				});
		}

		callOnTimeOut();
	}, 5000);
</aui:script>