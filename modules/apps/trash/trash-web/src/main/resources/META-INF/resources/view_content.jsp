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
TrashHandler trashHandler = trashDisplayContext.getTrashHandler();
%>

<liferay-util:include page="/restore_path.jsp" servletContext="<%= application %>" />

<c:choose>
	<c:when test="<%= trashHandler.isContainerModel() %>">
		<clay:management-toolbar
			managementToolbarDisplayContext="<%= new TrashContainerManagementToolbarDisplayContext(request, liferayPortletRequest, liferayPortletResponse, trashDisplayContext) %>"
		/>

		<div class="closed sidenav-container sidenav-right" id="<portlet:namespace />infoPanelId">
			<liferay-portlet:resourceURL copyCurrentRenderParameters="<%= false %>" id="/trash/info_panel" var="sidebarPanelURL" />

			<liferay-frontend:sidebar-panel
				resourceURL="<%= sidebarPanelURL %>"
			>
				<liferay-util:include page="/view_content_info_panel.jsp" servletContext="<%= application %>" />
			</liferay-frontend:sidebar-panel>

			<div class="sidenav-content">
				<clay:container-fluid>
					<liferay-site-navigation:breadcrumb
						breadcrumbEntries="<%= trashDisplayContext.getBaseModelBreadcrumbEntries() %>"
					/>

					<liferay-ui:search-container
						id="trash"
						searchContainer="<%= trashDisplayContext.getTrashContainerSearchContainer() %>"
					>
						<liferay-ui:search-container-row
							className="com.liferay.portal.kernel.model.TrashedModel"
							modelVar="curTrashedModel"
						>

							<%
							ClassedModel classedModel = (ClassedModel)curTrashedModel;

							String modelClassName = classedModel.getModelClassName();

							TrashHandler curTrashHandler = TrashHandlerRegistryUtil.getTrashHandler(modelClassName);

							TrashRenderer curTrashRenderer = curTrashHandler.getTrashRenderer(curTrashedModel.getTrashEntryClassPK());

							PortletURL rowURL = PortletURLBuilder.createRenderURL(
								renderResponse
							).setMVCPath(
								"/view_content.jsp"
							).setParameter(
								"classNameId", PortalUtil.getClassNameId(curTrashRenderer.getClassName())
							).setParameter(
								"classPK", curTrashRenderer.getClassPK()
							).buildPortletURL();
							%>

							<c:choose>
								<c:when test="<%= trashDisplayContext.isDescriptiveView() %>">
									<liferay-ui:search-container-column-icon
										icon="<%= curTrashRenderer.getIconCssClass() %>"
										toggleRowChecker="<%= false %>"
									/>

									<liferay-ui:search-container-column-text
										colspan="<%= 2 %>"
									>
										<h5>
											<aui:a href="<%= rowURL.toString() %>">
												<%= HtmlUtil.escape(curTrashRenderer.getTitle(locale)) %>
											</aui:a>
										</h5>

										<h6 class="text-default">
											<liferay-ui:message key="type" /> <%= ResourceActionsUtil.getModelResource(locale, curTrashRenderer.getClassName()) %>
										</h6>
									</liferay-ui:search-container-column-text>

									<liferay-ui:search-container-column-text>
										<clay:dropdown-actions
											dropdownItems="<%= trashDisplayContext.getTrashViewContentActionDropdownItems(modelClassName, curTrashedModel.getTrashEntryClassPK()) %>"
											propsTransformer="js/EntriesPropsTransformer"
										/>
									</liferay-ui:search-container-column-text>
								</c:when>
								<c:when test="<%= trashDisplayContext.isIconView() %>">
									<c:choose>
										<c:when test="<%= !curTrashHandler.isContainerModel() %>">
											<liferay-ui:search-container-column-text>
												<clay:vertical-card
													propsTransformer="js/EntriesPropsTransformer"
													verticalCard="<%= new TrashContentVerticalCard(curTrashedModel, curTrashRenderer, liferayPortletResponse, renderRequest, rowURL.toString()) %>"
												/>
											</liferay-ui:search-container-column-text>
										</c:when>
										<c:otherwise>
											<liferay-ui:search-container-column-text>

												<%
												row.setCssClass("card-page-item card-page-item-directory");
												%>

												<clay:horizontal-card
													horizontalCard="<%= new TrashContentHorizontalCard(curTrashedModel, curTrashRenderer, liferayPortletResponse, renderRequest, rowURL.toString()) %>"
													propsTransformer="js/EntriesPropsTransformer"
												/>
											</liferay-ui:search-container-column-text>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="<%= trashDisplayContext.isListView() %>">
									<liferay-ui:search-container-column-text
										name="name"
										truncate="<%= true %>"
									>
										<aui:a href="<%= rowURL.toString() %>">
											<%= HtmlUtil.escape(curTrashRenderer.getTitle(locale)) %>
										</aui:a>
									</liferay-ui:search-container-column-text>

									<liferay-ui:search-container-column-text>
										<clay:dropdown-actions
											dropdownItems="<%= trashDisplayContext.getTrashViewContentActionDropdownItems(modelClassName, curTrashedModel.getTrashEntryClassPK()) %>"
											propsTransformer="js/EntriesPropsTransformer"
										/>
									</liferay-ui:search-container-column-text>
								</c:when>
							</c:choose>
						</liferay-ui:search-container-row>

						<liferay-ui:search-iterator
							displayStyle="<%= trashDisplayContext.getDisplayStyle() %>"
							markupView="lexicon"
							resultRowSplitter="<%= new TrashResultRowSplitter() %>"
						/>
					</liferay-ui:search-container>
				</clay:container-fluid>
			</div>
		</div>
	</c:when>
	<c:otherwise>

		<%
		portletDisplay.setShowBackIcon(true);
		portletDisplay.setURLBack(trashDisplayContext.getViewContentRedirectURL());

		TrashRenderer trashRenderer = trashDisplayContext.getTrashRenderer();

		renderResponse.setTitle(trashRenderer.getTitle(locale));
		%>

		<clay:container-fluid>
			<aui:fieldset-group markupView="lexicon">
				<aui:fieldset>
					<liferay-asset:asset-display
						renderer="<%= trashRenderer %>"
					/>
				</aui:fieldset>
			</aui:fieldset-group>
		</clay:container-fluid>
	</c:otherwise>
</c:choose>