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

<%@ include file="/wiki/init.jsp" %>

<liferay-util:dynamic-include key="com.liferay.wiki.web#/wiki/view.jsp#pre" />

<%
WikiPortletInstanceConfiguration wikiPortletInstanceConfiguration = wikiRequestHelper.getWikiPortletInstanceConfiguration();

boolean followRedirect = ParamUtil.getBoolean(request, "followRedirect", true);

WikiNode node = (WikiNode)request.getAttribute(WikiWebKeys.WIKI_NODE);

WikiPage wikiPage = (WikiPage)request.getAttribute(WikiWebKeys.WIKI_PAGE);

WikiPage originalPage = null;
WikiPage redirectPage = wikiPage.getRedirectPage();

if (followRedirect && (redirectPage != null)) {
	originalPage = wikiPage;
	wikiPage = redirectPage;
}

String title = wikiPage.getTitle();
String parentTitle = wikiPage.getParentTitle();

List<WikiPage> childPages = new ArrayList<>();

for (WikiPage curChildPage : wikiPage.getViewableChildPages()) {
	if (curChildPage.getRedirectPage() == null) {
		childPages.add(curChildPage);
	}
}

boolean preview = false;
boolean print = Objects.equals(ParamUtil.getString(request, "viewMode"), Constants.PRINT);

PortletURL viewPageURL = renderResponse.createRenderURL();

if (portletName.equals(WikiPortletKeys.WIKI_DISPLAY)) {
	viewPageURL.setParameter("mvcRenderCommandName", "/wiki/view_page");
}
else {
	viewPageURL.setParameter("mvcRenderCommandName", "/wiki/view");
}

viewPageURL.setParameter("nodeName", node.getName());
viewPageURL.setParameter("title", title);

PortletURL viewParentPageURL = null;

if (Validator.isNotNull(parentTitle)) {
	viewParentPageURL = PortletURLBuilder.create(
		PortletURLUtil.clone(viewPageURL, renderResponse)
	).setParameter(
		"title", parentTitle
	).buildPortletURL();

	parentTitle = StringUtil.shorten(parentTitle, 20);
}

PortletURL addPageURL = PortletURLBuilder.createRenderURL(
	renderResponse
).setMVCRenderCommandName(
	"/wiki/edit_page"
).setRedirect(
	currentURL
).setParameter(
	"editTitle", "1"
).setParameter(
	"nodeId", node.getNodeId()
).setParameter(
	"parentTitle", wikiPage.getTitle()
).setParameter(
	"title", StringPool.BLANK
).buildPortletURL();

PortletURL editPageURL = PortletURLBuilder.createRenderURL(
	renderResponse
).setMVCRenderCommandName(
	"/wiki/edit_page"
).setRedirect(
	currentURL
).setParameter(
	"nodeId", node.getNodeId()
).setParameter(
	"title", title
).buildPortletURL();

PortletURL printPageURL = PortletURLBuilder.create(
	PortletURLUtil.clone(viewPageURL, renderResponse)
).setParameter(
	"viewMode", Constants.PRINT
).setWindowState(
	LiferayWindowState.POP_UP
).buildPortletURL();

PortletURL categorizedPagesURL = PortletURLBuilder.createRenderURL(
	renderResponse
).setMVCRenderCommandName(
	"/wiki/view_categorized_pages"
).setParameter(
	"nodeId", node.getNodeId()
).buildPortletURL();

PortletURL taggedPagesURL = PortletURLBuilder.createRenderURL(
	renderResponse
).setMVCRenderCommandName(
	"/wiki/view_tagged_pages"
).setParameter(
	"nodeId", node.getNodeId()
).buildPortletURL();

AssetEntry layoutAssetEntry = AssetEntryLocalServiceUtil.getEntry(WikiPage.class.getName(), wikiPage.getResourcePrimKey());

AssetEntryServiceUtil.incrementViewCounter(layoutAssetEntry);

if (Validator.isNotNull(ParamUtil.getString(request, "title"))) {
	assetHelper.addLayoutTags(request, AssetTagLocalServiceUtil.getTags(WikiPage.class.getName(), wikiPage.getResourcePrimKey()));
}

request.setAttribute(WebKeys.LAYOUT_ASSET_ENTRY, layoutAssetEntry);

boolean portletTitleBasedNavigation = GetterUtil.getBoolean(portletConfig.getInitParameter("portlet-title-based-navigation"));

if (portletTitleBasedNavigation) {
	WikiURLHelper wikiURLHelper = new WikiURLHelper(wikiRequestHelper, renderResponse, wikiGroupServiceConfiguration);

	PortletURL backToViewPagesURL = wikiURLHelper.getBackToViewPagesURL(node);

	portletDisplay.setShowBackIcon(true);
	portletDisplay.setURLBack((viewParentPageURL != null) ? viewParentPageURL.toString() : backToViewPagesURL.toString());
}
%>

<c:if test="<%= portletTitleBasedNavigation %>">
	<div class="lfr-alert-container"></div>

	<liferay-frontend:info-bar>
		<aui:workflow-status markupView="lexicon" showHelpMessage="<%= false %>" showIcon="<%= false %>" showLabel="<%= false %>" status="<%= wikiPage.getStatus() %>" version="<%= String.valueOf(wikiPage.getVersion()) %>" />

		<liferay-frontend:info-bar-buttons>
			<liferay-frontend:info-bar-sidenav-toggler-button
				icon="info-circle-open"
				label="info"
			/>
		</liferay-frontend:info-bar-buttons>
	</liferay-frontend:info-bar>
</c:if>

<div <%= portletTitleBasedNavigation ? "class=\"closed sidenav-container sidenav-right\" id=\"" + liferayPortletResponse.getNamespace() + "infoPanelId\"" : StringPool.BLANK %>>
	<c:if test="<%= portletTitleBasedNavigation %>">
		<liferay-frontend:sidebar-panel>
			<liferay-util:include page="/wiki_admin/page_info_panel.jsp" servletContext="<%= application %>" />
		</liferay-frontend:sidebar-panel>
	</c:if>

	<clay:container-fluid
		cssClass='<%= portletTitleBasedNavigation ? "sidenav-content" : StringPool.BLANK %>'
	>
		<div <%= portletTitleBasedNavigation ? "class=\"container-form-lg\"" : StringPool.BLANK %>>
			<div <%= portletTitleBasedNavigation ? "class=\"sheet\"" : StringPool.BLANK %>>
				<c:if test="<%= !portletTitleBasedNavigation %>">
					<c:choose>
						<c:when test="<%= print %>">
							<aui:script>
								window.onafterprint = function () {
									window.close();
								};

								window.onfocus = function () {
									window.close();
								};

								print();
							</aui:script>
						</c:when>
						<c:otherwise>
							<aui:script>
								function <portlet:namespace />printPage() {
									window.open(
										'<%= printPageURL %>',
										'',
										'directories=0,height=480,left=80,location=1,menubar=1,resizable=1,scrollbars=yes,status=0,toolbar=0,top=180,width=640'
									);
								}
							</aui:script>
						</c:otherwise>
					</c:choose>

					<liferay-util:include page="/wiki/top_links.jsp" servletContext="<%= application %>" />
				</c:if>

				<%
				List<WikiPage> entries = new ArrayList<>();

				entries.add(wikiPage);

				String formattedContent = null;

				WikiEngineRenderer wikiEngineRenderer = (WikiEngineRenderer)request.getAttribute(WikiWebKeys.WIKI_ENGINE_RENDERER);

				try {
					formattedContent = WikiUtil.getFormattedContent(wikiEngineRenderer, renderRequest, renderResponse, wikiPage, viewPageURL, editPageURL, title, preview);
				}
				catch (Exception e) {
					formattedContent = wikiPage.getContent();
				}
				%>

				<c:if test="<%= !portletTitleBasedNavigation %>">
					<div class="lfr-alert-container"></div>
				</c:if>

				<liferay-ddm:template-renderer
					className="<%= WikiPage.class.getName() %>"
					contextObjects='<%=
						HashMapBuilder.<String, Object>put(
							"assetEntry", layoutAssetEntry
						).put(
							"formattedContent", formattedContent
						).put(
							"viewURL", viewPageURL.toString()
						).put(
							"wikiPortletInstanceConfiguration", wikiPortletInstanceConfiguration
						).put(
							"wikiPortletInstanceOverriddenConfiguration", wikiPortletInstanceConfiguration
						).build()
					%>'
					displayStyle="<%= wikiPortletInstanceSettingsHelper.getDisplayStyle() %>"
					displayStyleGroupId="<%= wikiPortletInstanceSettingsHelper.getDisplayStyleGroupId() %>"
					entries="<%= entries %>"
				>
					<c:choose>
						<c:when test="<%= !portletTitleBasedNavigation %>">
							<liferay-ui:header
								backLabel="<%= parentTitle %>"
								backURL="<%= (viewParentPageURL != null) ? viewParentPageURL.toString() : null %>"
								localizeTitle="<%= false %>"
								title="<%= title %>"
							/>
						</c:when>
						<c:otherwise>
							<h2 class="sheet-title"><%= title %></h2>
						</c:otherwise>
					</c:choose>

					<c:if test="<%= !print && !portletTitleBasedNavigation %>">
						<div class="page-actions top-actions">
							<c:if test="<%= followRedirect || (redirectPage == null) %>">
								<c:if test="<%= Validator.isNotNull(formattedContent) && WikiNodePermission.contains(permissionChecker, node, ActionKeys.ADD_PAGE) %>">
									<liferay-ui:icon
										icon="plus"
										label="<%= true %>"
										markupView="lexicon"
										message="add-child-page"
										method="get"
										url="<%= addPageURL.toString() %>"
									/>
								</c:if>

								<c:if test="<%= WikiPagePermission.contains(permissionChecker, wikiPage, ActionKeys.UPDATE) %>">
									<liferay-ui:icon
										icon="pencil"
										label="<%= true %>"
										markupView="lexicon"
										message="edit"
										url="<%= editPageURL.toString() %>"
									/>
								</c:if>
							</c:if>

							<liferay-ui:icon
								icon="document"
								label="<%= true %>"
								markupView="lexicon"
								message="details"
								method="get"
								url='<%=
									PortletURLBuilder.create(
										PortletURLUtil.clone(viewPageURL, renderResponse)
									).setMVCRenderCommandName(
										"/wiki/view_page_details"
									).setRedirect(
										currentURL
									).buildString()
%>'
							/>

							<liferay-ui:icon
								icon="print"
								label="<%= true %>"
								markupView="lexicon"
								message="print"
								url='<%= "javascript:" + liferayPortletResponse.getNamespace() + "printPage();" %>'
							/>
						</div>
					</c:if>

					<c:if test="<%= originalPage != null %>">

						<%
						PortletURL originalViewPageURL = PortletURLBuilder.createRenderURL(
							renderResponse
						).setMVCRenderCommandName(
							"/wiki/view"
						).setParameter(
							"followRedirect", "false"
						).setParameter(
							"nodeName", node.getName()
						).setParameter(
							"title", originalPage.getTitle()
						).buildPortletURL();
						%>

						<div class="page-redirect" onClick="location.href = '<%= originalViewPageURL.toString() %>';">
							(<liferay-ui:message arguments="<%= originalPage.getTitle() %>" key="redirected-from-x" translateArguments="<%= false %>" />)
						</div>
					</c:if>

					<c:if test="<%= !wikiPage.isHead() %>">
						<div class="page-old-version">
							(<liferay-ui:message key="you-are-viewing-an-archived-version-of-this-page" /> (<%= wikiPage.getVersion() %>), <aui:a href="<%= viewPageURL.toString() %>" label="go-to-the-latest-version" />)
						</div>
					</c:if>

					<%@ include file="/wiki/view_page_content.jspf" %>

					<liferay-expando:custom-attributes-available
						className="<%= WikiPage.class.getName() %>"
					>
						<liferay-expando:custom-attribute-list
							className="<%= WikiPage.class.getName() %>"
							classPK="<%= wikiPage.getPrimaryKey() %>"
							editable="<%= false %>"
							label="<%= true %>"
						/>
					</liferay-expando:custom-attributes-available>

					<c:if test="<%= followRedirect || (redirectPage == null) %>">
						<div class="page-actions">
							<div class="stats">

								<%
								AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(WikiPage.class.getName(), wikiPage.getResourcePrimKey());
								%>

								<c:choose>
									<c:when test="<%= assetEntry.getViewCount() == 1 %>">
										<%= assetEntry.getViewCount() %> <liferay-ui:message key="view" />
									</c:when>
									<c:when test="<%= assetEntry.getViewCount() > 1 %>">
										<%= assetEntry.getViewCount() %> <liferay-ui:message key="views" />
									</c:when>
								</c:choose>
							</div>

							<div class="page-categorization">
								<div class="page-categories">
									<liferay-asset:asset-categories-summary
										className="<%= WikiPage.class.getName() %>"
										classPK="<%= wikiPage.getResourcePrimKey() %>"
										portletURL="<%= PortletURLUtil.clone(categorizedPagesURL, renderResponse) %>"
									/>
								</div>

								<div class="page-tags">
									<liferay-asset:asset-tags-available
										className="<%= WikiPage.class.getName() %>"
										classPK="<%= wikiPage.getResourcePrimKey() %>"
									>
										<h5><liferay-ui:message key="tags" /></h5>

										<liferay-asset:asset-tags-summary
											className="<%= WikiPage.class.getName() %>"
											classPK="<%= wikiPage.getResourcePrimKey() %>"
											portletURL="<%= PortletURLUtil.clone(taggedPagesURL, renderResponse) %>"
										/>
									</liferay-asset:asset-tags-available>
								</div>
							</div>

							<c:if test="<%= wikiPortletInstanceSettingsHelper.isEnablePageRatings() %>">
								<div class="page-ratings">
									<liferay-ratings:ratings
										className="<%= WikiPage.class.getName() %>"
										classPK="<%= wikiPage.getResourcePrimKey() %>"
										inTrash="<%= wikiPage.isInTrash() %>"
									/>
								</div>
							</c:if>

							<liferay-util:include page="/wiki/view_attachments.jsp" servletContext="<%= application %>" />

							<c:if test="<%= wikiPortletInstanceSettingsHelper.isEnableRelatedAssets() %>">
								<div class="entry-links">
									<liferay-asset:asset-links
										assetEntryId="<%= assetEntry.getEntryId() %>"
									/>
								</div>
							</c:if>
						</div>

						<c:if test="<%= wikiPortletInstanceSettingsHelper.isEnableComments() %>">
							<div id="<portlet:namespace />wikiCommentsPanel">
								<liferay-comment:discussion
									className="<%= WikiPage.class.getName() %>"
									classPK="<%= wikiPage.getResourcePrimKey() %>"
									formName="fm2"
									ratingsEnabled="<%= wikiPortletInstanceSettingsHelper.isEnableCommentRatings() %>"
									redirect="<%= currentURL %>"
									userId="<%= wikiPage.getUserId() %>"
								/>
							</div>
						</c:if>
					</c:if>
				</liferay-ddm:template-renderer>

				<%
				if (!Objects.equals(wikiPage.getTitle(), wikiGroupServiceConfiguration.frontPageName())) {
					if (!portletName.equals(WikiPortletKeys.WIKI_DISPLAY)) {
						PortalUtil.setPageSubtitle(wikiPage.getTitle(), request);

						String description = wikiPage.getContent();

						if (Objects.equals(wikiPage.getFormat(), "html")) {
							description = HtmlUtil.stripHtml(description);
						}

						description = StringUtil.shorten(description, 200);

						PortalUtil.setPageDescription(description, request);

						PortalUtil.setPageKeywords(assetHelper.getAssetKeywords(WikiPage.class.getName(), wikiPage.getResourcePrimKey()), request);
					}

					List<WikiPage> parentPages = wikiPage.getViewableParentPages();

					for (WikiPage curParentPage : parentPages) {
						viewPageURL.setParameter("title", curParentPage.getTitle());

						PortalUtil.addPortletBreadcrumbEntry(request, curParentPage.getTitle(), viewPageURL.toString());
					}

					viewPageURL.setParameter("title", wikiPage.getTitle());

					PortalUtil.addPortletBreadcrumbEntry(request, wikiPage.getTitle(), viewPageURL.toString());
				}
				%>

				<liferay-util:dynamic-include key="com.liferay.wiki.web#/wiki/view.jsp#post" />
			</div>
		</div>

		<c:if test="<%= Validator.isNotNull(formattedContent) && (followRedirect || (redirectPage == null)) && !childPages.isEmpty() %>">
			<h4 class="text-default">
				<liferay-ui:message arguments="<%= childPages.size() %>" key="child-pages-x" translateArguments="<%= false %>" />
			</h4>

			<div>
				<ul class="list-group">

					<%
					for (WikiPage childPage : childPages) {
					%>

						<li class="list-group-item">
							<h3>
								<aui:a
									href="<%=
PortletURLBuilder.create(
									PortletURLUtil.clone(viewPageURL, renderResponse)
								).setParameter(
									"title", childPage.getTitle()
								).buildString() %>"><%= childPage.getTitle() %></aui:a
								>
							</h3>

							<%
							String childPageFormattedContent = null;

							try {
								childPageFormattedContent = WikiUtil.getFormattedContent(wikiEngineRenderer, renderRequest, renderResponse, childPage, viewPageURL, editPageURL, childPage.getTitle(), false);
							}
							catch (Exception e) {
								childPageFormattedContent = childPage.getContent();
							}
							%>

							<p class="text-default"><%= StringUtil.shorten(HtmlParserUtil.extractText(childPageFormattedContent), 200) %></p>
						</li>

					<%
					}
					%>

				</ul>
			</div>
		</c:if>
	</clay:container-fluid>
</div>