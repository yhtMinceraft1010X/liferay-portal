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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/asset" prefix="liferay-asset" %><%@
taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/clay" prefix="clay" %><%@
taglib uri="http://liferay.com/tld/expando" prefix="liferay-expando" %><%@
taglib uri="http://liferay.com/tld/friendly-url" prefix="liferay-friendly-url" %><%@
taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %><%@
taglib uri="http://liferay.com/tld/react" prefix="react" %><%@
taglib uri="http://liferay.com/tld/site-navigation" prefix="liferay-site-navigation" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %><%@
taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page import="com.liferay.asset.kernel.model.AssetVocabularyConstants" %><%@
page import="com.liferay.document.library.kernel.exception.FileSizeException" %><%@
page import="com.liferay.exportimport.kernel.lar.PortletDataHandlerKeys" %><%@
page import="com.liferay.exportimport.kernel.staging.LayoutStagingUtil" %><%@
page import="com.liferay.exportimport.kernel.staging.StagingUtil" %><%@
page import="com.liferay.friendly.url.exception.DuplicateFriendlyURLEntryException" %><%@
page import="com.liferay.frontend.taglib.form.navigator.constants.FormNavigatorConstants" %><%@
page import="com.liferay.layout.admin.constants.LayoutScreenNavigationEntryConstants" %><%@
page import="com.liferay.layout.admin.web.internal.constants.LayoutAdminWebKeys" %><%@
page import="com.liferay.layout.admin.web.internal.display.context.LayoutLookAndFeelDisplayContext" %><%@
page import="com.liferay.layout.admin.web.internal.display.context.LayoutsAdminDisplayContext" %><%@
page import="com.liferay.layout.admin.web.internal.display.context.LayoutsAdminManagementToolbarDisplayContext" %><%@
page import="com.liferay.layout.admin.web.internal.display.context.MillerColumnsDisplayContext" %><%@
page import="com.liferay.layout.admin.web.internal.display.context.OrphanPortletsDisplayContext" %><%@
page import="com.liferay.layout.admin.web.internal.display.context.OrphanPortletsManagementToolbarDisplayContext" %><%@
page import="com.liferay.layout.admin.web.internal.display.context.SelectCollectionManagementToolbarDisplayContext" %><%@
page import="com.liferay.layout.admin.web.internal.display.context.SelectLayoutCollectionDisplayContext" %><%@
page import="com.liferay.layout.admin.web.internal.display.context.SelectLayoutPageTemplateEntryDisplayContext" %><%@
page import="com.liferay.layout.admin.web.internal.display.context.SelectStyleBookEntryDisplayContext" %><%@
page import="com.liferay.layout.admin.web.internal.display.context.SelectThemeDisplayContext" %><%@
page import="com.liferay.layout.admin.web.internal.display.context.SelectThemeManagementToolbarDisplayContext" %><%@
page import="com.liferay.layout.admin.web.internal.servlet.taglib.clay.CollectionProvidersVerticalCard" %><%@
page import="com.liferay.layout.admin.web.internal.servlet.taglib.clay.CollectionsVerticalCard" %><%@
page import="com.liferay.layout.admin.web.internal.servlet.taglib.clay.SelectBasicTemplatesNavigationCard" %><%@
page import="com.liferay.layout.admin.web.internal.servlet.taglib.clay.SelectBasicTemplatesVerticalCard" %><%@
page import="com.liferay.layout.admin.web.internal.servlet.taglib.clay.SelectGlobalTemplatesVerticalCard" %><%@
page import="com.liferay.layout.admin.web.internal.servlet.taglib.clay.SelectLayoutMasterLayoutVerticalCard" %><%@
page import="com.liferay.layout.admin.web.internal.servlet.taglib.clay.SelectLayoutPageTemplateEntryVerticalCard" %><%@
page import="com.liferay.layout.admin.web.internal.servlet.taglib.clay.SelectMasterLayoutVerticalCard" %><%@
page import="com.liferay.layout.admin.web.internal.servlet.taglib.clay.SelectStylebookLayoutVerticalCard" %><%@
page import="com.liferay.layout.admin.web.internal.servlet.taglib.clay.SelectThemeVerticalCard" %><%@
page import="com.liferay.layout.admin.web.internal.servlet.taglib.util.LayoutActionDropdownItemsProvider" %><%@
page import="com.liferay.layout.admin.web.internal.util.CustomizationSettingsUtil" %><%@
page import="com.liferay.layout.page.template.model.LayoutPageTemplateCollection" %><%@
page import="com.liferay.layout.page.template.model.LayoutPageTemplateEntry" %><%@
page import="com.liferay.layout.page.template.service.LayoutPageTemplateCollectionLocalServiceUtil" %><%@
page import="com.liferay.layout.page.template.service.LayoutPageTemplateCollectionServiceUtil" %><%@
page import="com.liferay.layout.page.template.service.LayoutPageTemplateEntryServiceUtil" %><%@
page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.kernel.bean.BeanParamUtil" %><%@
page import="com.liferay.portal.kernel.dao.search.ResultRow" %><%@
page import="com.liferay.portal.kernel.exception.GroupInheritContentException" %><%@
page import="com.liferay.portal.kernel.exception.ImageTypeException" %><%@
page import="com.liferay.portal.kernel.exception.LayoutFriendlyURLException" %><%@
page import="com.liferay.portal.kernel.exception.LayoutFriendlyURLsException" %><%@
page import="com.liferay.portal.kernel.exception.LayoutNameException" %><%@
page import="com.liferay.portal.kernel.exception.LayoutTypeException" %><%@
page import="com.liferay.portal.kernel.exception.NoSuchGroupException" %><%@
page import="com.liferay.portal.kernel.exception.NoSuchLayoutException" %><%@
page import="com.liferay.portal.kernel.exception.NoSuchRoleException" %><%@
page import="com.liferay.portal.kernel.exception.RequiredLayoutException" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil" %><%@
page import="com.liferay.portal.kernel.model.ColorScheme" %><%@
page import="com.liferay.portal.kernel.model.Group" %><%@
page import="com.liferay.portal.kernel.model.GroupConstants" %><%@
page import="com.liferay.portal.kernel.model.Layout" %><%@
page import="com.liferay.portal.kernel.model.LayoutBranch" %><%@
page import="com.liferay.portal.kernel.model.LayoutBranchConstants" %><%@
page import="com.liferay.portal.kernel.model.LayoutConstants" %><%@
page import="com.liferay.portal.kernel.model.LayoutPrototype" %><%@
page import="com.liferay.portal.kernel.model.LayoutRevision" %><%@
page import="com.liferay.portal.kernel.model.LayoutSet" %><%@
page import="com.liferay.portal.kernel.model.LayoutSetBranch" %><%@
page import="com.liferay.portal.kernel.model.LayoutSetBranchConstants" %><%@
page import="com.liferay.portal.kernel.model.LayoutSetPrototype" %><%@
page import="com.liferay.portal.kernel.model.LayoutTemplate" %><%@
page import="com.liferay.portal.kernel.model.LayoutTemplateConstants" %><%@
page import="com.liferay.portal.kernel.model.LayoutType" %><%@
page import="com.liferay.portal.kernel.model.LayoutTypeController" %><%@
page import="com.liferay.portal.kernel.model.LayoutTypePortlet" %><%@
page import="com.liferay.portal.kernel.model.Portlet" %><%@
page import="com.liferay.portal.kernel.model.Theme" %><%@
page import="com.liferay.portal.kernel.model.ThemeSetting" %><%@
page import="com.liferay.portal.kernel.model.UserGroup" %><%@
page import="com.liferay.portal.kernel.plugin.PluginPackage" %><%@
page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %><%@
page import="com.liferay.portal.kernel.security.permission.ActionKeys" %><%@
page import="com.liferay.portal.kernel.security.permission.ResourceActionsUtil" %><%@
page import="com.liferay.portal.kernel.service.GroupLocalServiceUtil" %><%@
page import="com.liferay.portal.kernel.service.LayoutLocalServiceUtil" %><%@
page import="com.liferay.portal.kernel.service.LayoutPrototypeLocalServiceUtil" %><%@
page import="com.liferay.portal.kernel.service.LayoutPrototypeServiceUtil" %><%@
page import="com.liferay.portal.kernel.service.LayoutSetBranchLocalServiceUtil" %><%@
page import="com.liferay.portal.kernel.service.LayoutSetPrototypeLocalServiceUtil" %><%@
page import="com.liferay.portal.kernel.service.LayoutTemplateLocalServiceUtil" %><%@
page import="com.liferay.portal.kernel.service.PortletLocalServiceUtil" %><%@
page import="com.liferay.portal.kernel.service.ThemeLocalServiceUtil" %><%@
page import="com.liferay.portal.kernel.service.UserGroupLocalServiceUtil" %><%@
page import="com.liferay.portal.kernel.service.permission.GroupPermissionUtil" %><%@
page import="com.liferay.portal.kernel.service.permission.LayoutPermissionUtil" %><%@
page import="com.liferay.portal.kernel.servlet.HttpHeaders" %><%@
page import="com.liferay.portal.kernel.servlet.ServletContextPool" %><%@
page import="com.liferay.portal.kernel.servlet.SessionErrors" %><%@
page import="com.liferay.portal.kernel.template.StringTemplateResource" %><%@
page import="com.liferay.portal.kernel.util.AggregateResourceBundle" %><%@
page import="com.liferay.portal.kernel.util.GetterUtil" %><%@
page import="com.liferay.portal.kernel.util.HashMapBuilder" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.HttpComponentsUtil" %><%@
page import="com.liferay.portal.kernel.util.LocaleUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.PortalUtil" %><%@
page import="com.liferay.portal.kernel.util.PortletKeys" %><%@
page import="com.liferay.portal.kernel.util.PropertiesParamUtil" %><%@
page import="com.liferay.portal.kernel.util.ResourceBundleUtil" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %><%@
page import="com.liferay.portal.kernel.util.UnicodeProperties" %><%@
page import="com.liferay.portal.kernel.util.Validator" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %><%@
page import="com.liferay.portal.kernel.webserver.WebServerServletTokenUtil" %><%@
page import="com.liferay.portal.kernel.workflow.WorkflowConstants" %><%@
page import="com.liferay.portal.util.LayoutTypeControllerTracker" %><%@
page import="com.liferay.portal.util.PropsValues" %><%@
page import="com.liferay.segments.exception.RequiredSegmentsExperienceException" %><%@
page import="com.liferay.site.navigation.model.SiteNavigationMenu" %><%@
page import="com.liferay.sites.kernel.util.SitesUtil" %><%@
page import="com.liferay.style.book.model.StyleBookEntry" %>

<%@ page import="java.util.List" %><%@
page import="java.util.Locale" %><%@
page import="java.util.Map" %><%@
page import="java.util.Objects" %><%@
page import="java.util.ResourceBundle" %><%@
page import="java.util.TreeMap" %>

<%@ page import="javax.portlet.PortletRequest" %><%@
page import="javax.portlet.PortletURL" %>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
LayoutsAdminDisplayContext layoutsAdminDisplayContext = (LayoutsAdminDisplayContext)request.getAttribute(LayoutAdminWebKeys.LAYOUT_PAGE_LAYOUT_ADMIN_DISPLAY_CONTEXT);

portletDisplay.setShowExportImportIcon(false);
%>

<%@ include file="/init-ext.jsp" %>