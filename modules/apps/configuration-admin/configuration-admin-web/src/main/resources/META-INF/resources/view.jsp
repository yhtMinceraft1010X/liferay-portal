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
List<ConfigurationCategorySectionDisplay> configurationCategorySectionDisplays = (List<ConfigurationCategorySectionDisplay>)request.getAttribute(ConfigurationAdminWebKeys.CONFIGURATION_CATEGORY_SECTION_DISPLAYS);
ConfigurationEntryRetriever configurationEntryRetriever = (ConfigurationEntryRetriever)request.getAttribute(ConfigurationAdminWebKeys.CONFIGURATION_ENTRY_RETRIEVER);
ConfigurationScopeDisplayContext configurationScopeDisplayContext = ConfigurationScopeDisplayContextFactory.create(renderRequest);
%>

<portlet:renderURL var="redirectURL" />

<portlet:renderURL var="searchURL">
	<portlet:param name="mvcRenderCommandName" value="/configuration_admin/search_results" />
	<portlet:param name="redirect" value="<%= redirectURL %>" />
</portlet:renderURL>

<div class="sticky-top" style="top: 56px; z-index: 999;">
	<clay:management-toolbar
		searchActionURL="<%= searchURL %>"
		selectable="<%= false %>"
		showSearch="<%= true %>"
	/>
</div>

<liferay-ui:success key='<%= ConfigurationAdminPortletKeys.SITE_SETTINGS + "requestProcessed" %>'>
	<liferay-ui:message key="site-was-successfully-added" />
</liferay-ui:success>

<clay:container-fluid
	cssClass="container-view"
>
	<c:if test="<%= configurationCategorySectionDisplays.isEmpty() %>">
		<liferay-ui:empty-result-message
			message="no-configurations-were-found"
		/>
	</c:if>

	<ul class="list-group <%= configurationCategorySectionDisplays.isEmpty() ? "hide" : StringPool.BLANK %>">

		<%
		for (ConfigurationCategorySectionDisplay configurationCategorySectionDisplay : configurationCategorySectionDisplays) {
		%>

			<li class="list-group-header">
				<h3 class="list-group-header-title text-uppercase">
					<%= HtmlUtil.escape(configurationCategorySectionDisplay.getConfigurationCategorySectionLabel(locale)) %>
				</h3>
			</li>
			<li class="list-group-card">
				<ul class="list-group">

					<%
					for (ConfigurationCategoryDisplay configurationCategoryDisplay : configurationCategorySectionDisplay.getConfigurationCategoryDisplays()) {
						ConfigurationCategoryMenuDisplay configurationCategoryMenuDisplay = configurationEntryRetriever.getConfigurationCategoryMenuDisplay(configurationCategoryDisplay.getCategoryKey(), themeDisplay.getLanguageId(), configurationScopeDisplayContext.getScope(), configurationScopeDisplayContext.getScopePK());

						if (configurationCategoryMenuDisplay.isEmpty()) {
							continue;
						}
					%>

						<li class="list-group-card-item">
							<a href="<%= ConfigurationCategoryUtil.getHREF(configurationCategoryMenuDisplay, liferayPortletResponse, renderRequest, renderResponse) %>">
								<clay:icon
									symbol="<%= configurationCategoryDisplay.getCategoryIcon() %>"
								/>

								<span class="list-group-card-item-text">
									<%= HtmlUtil.escape(configurationCategoryDisplay.getCategoryLabel(locale)) %>
								</span>
							</a>
						</li>

					<%
					}
					%>

				</ul>
			</li>

		<%
		}
		%>

	</ul>
</clay:container-fluid>