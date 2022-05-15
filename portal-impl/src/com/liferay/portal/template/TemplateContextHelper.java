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

package com.liferay.portal.template;

import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoRowLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.audit.AuditMessageFactoryUtil;
import com.liferay.portal.kernel.audit.AuditRouterUtil;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.language.UnicodeLanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletModeFactory_IW;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletRequestModel;
import com.liferay.portal.kernel.portlet.PortletRequestModelFactory;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.WindowStateFactory_IW;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.GroupService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutService;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.kernel.service.OrganizationService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.UserService;
import com.liferay.portal.kernel.service.permission.CommonPermissionUtil;
import com.liferay.portal.kernel.service.permission.GroupPermissionUtil;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.service.permission.OrganizationPermissionUtil;
import com.liferay.portal.kernel.service.permission.PasswordPolicyPermissionUtil;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.service.permission.RolePermissionUtil;
import com.liferay.portal.kernel.service.permission.UserGroupPermissionUtil;
import com.liferay.portal.kernel.service.permission.UserPermissionUtil;
import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.template.TemplateHandler;
import com.liferay.portal.kernel.template.TemplateHandlerRegistryUtil;
import com.liferay.portal.kernel.template.TemplateVariableGroup;
import com.liferay.portal.kernel.theme.NavItem;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil_IW;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.DateUtil_IW;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.GetterUtil_IW;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.InetAddressUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ListMergeable;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.ParamUtil_IW;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.SessionClicks_IW;
import com.liferay.portal.kernel.util.StaticFieldGetter;
import com.liferay.portal.kernel.util.StringUtil_IW;
import com.liferay.portal.kernel.util.TimeZoneUtil_IW;
import com.liferay.portal.kernel.util.URLCodec_IW;
import com.liferay.portal.kernel.util.UnicodeFormatter_IW;
import com.liferay.portal.kernel.util.Validator_IW;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.webserver.WebServerServletTokenUtil;
import com.liferay.portal.kernel.xml.SAXReader;
import com.liferay.portal.struts.Definition;
import com.liferay.portal.struts.TilesUtil;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Tina Tian
 * @author Jorge Ferrer
 * @author Raymond Augé
 */
public class TemplateContextHelper {

	public static Map<String, TemplateVariableGroup> getTemplateVariableGroups(
			long classNameId, long classPK, String language, Locale locale)
		throws Exception {

		TemplateHandler templateHandler =
			TemplateHandlerRegistryUtil.getTemplateHandler(classNameId);

		if (templateHandler == null) {
			return Collections.emptyMap();
		}

		Map<String, TemplateVariableGroup> templateVariableGroups =
			templateHandler.getTemplateVariableGroups(
				classPK, language, locale);

		TemplateVariableGroup portalServicesTemplateVariableGroup =
			new TemplateVariableGroup(
				"portal-services",
				templateHandler.getRestrictedVariables(language));

		portalServicesTemplateVariableGroup.setAutocompleteEnabled(false);

		portalServicesTemplateVariableGroup.addServiceLocatorVariables(
			GroupLocalService.class, GroupService.class,
			LayoutLocalService.class, LayoutService.class,
			OrganizationLocalService.class, OrganizationService.class,
			UserLocalService.class, UserService.class);

		templateVariableGroups.put(
			portalServicesTemplateVariableGroup.getLabel(),
			portalServicesTemplateVariableGroup);

		return templateVariableGroups;
	}

	public Map<String, Object> getHelperUtilities(
		ClassLoader classLoader, boolean restricted) {

		Map<String, Object>[] helperUtilitiesArray = _helperUtilitiesMaps.get(
			classLoader);

		if (helperUtilitiesArray == null) {
			helperUtilitiesArray = (Map<String, Object>[])new Map<?, ?>[2];

			_helperUtilitiesMaps.put(classLoader, helperUtilitiesArray);
		}
		else {
			Map<String, Object> helperUtilities = null;

			if (restricted) {
				helperUtilities = helperUtilitiesArray[1];
			}
			else {
				helperUtilities = helperUtilitiesArray[0];
			}

			if (helperUtilities != null) {
				return helperUtilities;
			}
		}

		Map<String, Object> helperUtilities = new HashMap<>();

		populateCommonHelperUtilities(helperUtilities);
		populateExtraHelperUtilities(helperUtilities, restricted);

		if (restricted) {
			Set<String> restrictedVariables = getRestrictedVariables();

			for (String restrictedVariable : restrictedVariables) {
				helperUtilities.remove(restrictedVariable);
			}

			helperUtilitiesArray[1] = helperUtilities;
		}
		else {
			helperUtilitiesArray[0] = helperUtilities;
		}

		return helperUtilities;
	}

	public Set<String> getRestrictedVariables() {
		return Collections.emptySet();
	}

	public TemplateControlContext getTemplateControlContext() {
		Thread currentThread = Thread.currentThread();

		return new TemplateControlContext(
			null, currentThread.getContextClassLoader());
	}

	public void prepare(
		Map<String, Object> contextObjects,
		HttpServletRequest httpServletRequest) {

		// Request

		contextObjects.put("request", httpServletRequest);

		// Portlet config

		PortletConfig portletConfig =
			(PortletConfig)httpServletRequest.getAttribute(
				JavaConstants.JAVAX_PORTLET_CONFIG);

		if (portletConfig != null) {
			contextObjects.put("portletConfig", portletConfig);
		}

		// Render request

		final PortletRequest portletRequest =
			(PortletRequest)httpServletRequest.getAttribute(
				JavaConstants.JAVAX_PORTLET_REQUEST);

		if ((portletRequest != null) &&
			(portletRequest instanceof RenderRequest)) {

			contextObjects.put("renderRequest", portletRequest);
		}

		// Render response

		final PortletResponse portletResponse =
			(PortletResponse)httpServletRequest.getAttribute(
				JavaConstants.JAVAX_PORTLET_RESPONSE);

		if ((portletResponse != null) &&
			(portletResponse instanceof RenderResponse)) {

			contextObjects.put("renderResponse", portletResponse);
		}

		// XML request

		if ((portletRequest != null) && (portletResponse != null)) {
			contextObjects.put(
				"portletRequestModelFactory",
				new PortletRequestModelFactory(
					portletRequest, portletResponse));

			// Deprecated

			contextObjects.put(
				"xmlRequest",
				new Object() {

					@Override
					public String toString() {
						PortletRequestModel portletRequestModel =
							new PortletRequestModel(
								portletRequest, portletResponse);

						return portletRequestModel.toXML();
					}

				});
		}

		// Theme display

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (themeDisplay != null) {
			Layout layout = themeDisplay.getLayout();

			HttpServletRequest originalHttpServletRequest =
				PortalUtil.getOriginalServletRequest(httpServletRequest);

			String namespace = PortalUtil.getPortletNamespace(
				ParamUtil.getString(httpServletRequest, "p_p_id"));

			String bodyCssClass = ParamUtil.getString(
				originalHttpServletRequest, namespace + "bodyCssClass");

			contextObjects.put("bodyCssClass", bodyCssClass);

			contextObjects.put("colorScheme", themeDisplay.getColorScheme());
			contextObjects.put("company", themeDisplay.getCompany());
			contextObjects.put("layout", layout);
			contextObjects.put("layouts", themeDisplay.getLayouts());
			contextObjects.put(
				"layoutTypePortlet", themeDisplay.getLayoutTypePortlet());
			contextObjects.put("locale", themeDisplay.getLocale());
			contextObjects.put(
				"permissionChecker", themeDisplay.getPermissionChecker());
			contextObjects.put("plid", String.valueOf(themeDisplay.getPlid()));
			contextObjects.put(
				"portletDisplay", themeDisplay.getPortletDisplay());
			contextObjects.put("realUser", themeDisplay.getRealUser());
			contextObjects.put(
				"scopeGroupId", Long.valueOf(themeDisplay.getScopeGroupId()));
			contextObjects.put("themeDisplay", themeDisplay);
			contextObjects.put("timeZone", themeDisplay.getTimeZone());

			User user = UserLocalServiceUtil.fetchUser(
				PrincipalThreadLocal.getUserId());

			if (user == null) {
				user = themeDisplay.getUser();
			}

			contextObjects.put("user", user);

			// Navigation items

			if (layout != null) {
				try {
					List<NavItem> navItems = NavItem.fromLayouts(
						httpServletRequest, themeDisplay, contextObjects);

					contextObjects.put("navItems", navItems);
				}
				catch (Exception exception) {
					_log.error(exception);
				}
			}

			// Deprecated

			contextObjects.put(
				"portletGroupId", Long.valueOf(themeDisplay.getScopeGroupId()));
		}

		// Theme

		Theme theme = (Theme)httpServletRequest.getAttribute(WebKeys.THEME);

		if ((theme == null) && (themeDisplay != null)) {
			theme = themeDisplay.getTheme();
		}

		if (theme != null) {
			contextObjects.put("theme", theme);
		}

		// Tiles attributes

		prepareTiles(contextObjects, httpServletRequest);

		// Page title and subtitle

		ListMergeable<String> pageTitleListMergeable =
			(ListMergeable<String>)httpServletRequest.getAttribute(
				WebKeys.PAGE_TITLE);

		if (pageTitleListMergeable != null) {
			String pageTitle = pageTitleListMergeable.mergeToString(
				StringPool.SPACE);

			contextObjects.put("pageTitle", pageTitle);
		}

		ListMergeable<String> pageSubtitleListMergeable =
			(ListMergeable<String>)httpServletRequest.getAttribute(
				WebKeys.PAGE_SUBTITLE);

		if (pageSubtitleListMergeable != null) {
			String pageSubtitle = pageSubtitleListMergeable.mergeToString(
				StringPool.SPACE);

			contextObjects.put("pageSubtitle", pageSubtitle);
		}
	}

	public void removeAllHelperUtilities() {
		_helperUtilitiesMaps.clear();
	}

	public void removeHelperUtilities(ClassLoader classLoader) {
		_helperUtilitiesMaps.remove(classLoader);
	}

	protected void populateCommonHelperUtilities(
		Map<String, Object> variables) {

		// Array util

		variables.put("arrayUtil", ArrayUtil_IW.getInstance());

		// Audit message factory

		try {
			variables.put(
				"auditMessageFactoryUtil",
				AuditMessageFactoryUtil.getAuditMessageFactory());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Audit router util

		try {
			variables.put("auditRouterUtil", AuditRouterUtil.getAuditRouter());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Browser sniffer

		try {
			variables.put(
				"browserSniffer", BrowserSnifferUtil.getBrowserSniffer());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Calendar factory

		try {
			variables.put(
				"calendarFactory", CalendarFactoryUtil.getCalendarFactory());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Date format

		try {
			variables.put(
				"dateFormatFactory",
				FastDateFormatFactoryUtil.getFastDateFormatFactory());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Date util

		variables.put("dateUtil", DateUtil_IW.getInstance());

		// Expando column service

		try {
			ServiceLocator serviceLocator = ServiceLocator.getInstance();

			// Service locator

			variables.put("serviceLocator", serviceLocator);

			try {
				variables.put(
					"expandoColumnLocalService",
					serviceLocator.findService(
						ExpandoColumnLocalService.class.getName()));
			}
			catch (SecurityException securityException) {
				_log.error(securityException);
			}

			// Expando row service

			try {
				variables.put(
					"expandoRowLocalService",
					serviceLocator.findService(
						ExpandoRowLocalService.class.getName()));
			}
			catch (SecurityException securityException) {
				_log.error(securityException);
			}

			// Expando table service

			try {
				variables.put(
					"expandoTableLocalService",
					serviceLocator.findService(
						ExpandoTableLocalService.class.getName()));
			}
			catch (SecurityException securityException) {
				_log.error(securityException);
			}

			// Expando value service

			try {
				variables.put(
					"expandoValueLocalService",
					serviceLocator.findService(
						ExpandoValueLocalService.class.getName()));
			}
			catch (SecurityException securityException) {
				_log.error(securityException);
			}
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Getter util

		variables.put("getterUtil", GetterUtil_IW.getInstance());

		// Html util

		try {
			variables.put("htmlUtil", HtmlUtil.getHtml());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Http util

		try {
			variables.put("httpUtil", new HttpWrapper(HttpUtil.getHttp()));
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"httpUtilUnsafe", new HttpWrapper(HttpUtil.getHttp(), false));
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Image tool util

		try {
			variables.put("imageToolUtil", ImageToolUtil.getImageTool());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// JSON factory util

		try {
			variables.put("jsonFactoryUtil", JSONFactoryUtil.getJSONFactory());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Language util

		try {
			variables.put("languageUtil", LanguageUtil.getLanguage());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"unicodeLanguageUtil",
				UnicodeLanguageUtil.getUnicodeLanguage());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Locale util

		try {
			variables.put("localeUtil", LocaleUtil.getInstance());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Param util

		variables.put("paramUtil", ParamUtil_IW.getInstance());

		// Portal util

		try {
			variables.put("portalUtil", PortalUtil.getPortal());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put("portal", PortalUtil.getPortal());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Prefs props util

		try {
			variables.put("prefsPropsUtil", PrefsPropsUtil.getPrefsProps());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Props util

		try {
			variables.put("propsUtil", PropsUtil.getProps());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Portlet mode factory

		variables.put(
			"portletModeFactory", PortletModeFactory_IW.getInstance());

		// Portlet URL factory

		try {
			variables.put(
				"portletURLFactory",
				PortletURLFactoryUtil.getPortletURLFactory());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			UtilLocator utilLocator = UtilLocator.getInstance();

			// Util locator

			variables.put("utilLocator", utilLocator);

			// SAX reader util

			try {
				variables.put(
					"saxReaderUtil",
					utilLocator.findUtil(SAXReader.class.getName()));
			}
			catch (SecurityException securityException) {
				_log.error(securityException);
			}
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Session clicks

		variables.put("sessionClicks", SessionClicks_IW.getInstance());

		// Static field getter

		variables.put("staticFieldGetter", StaticFieldGetter.getInstance());

		// String util

		variables.put("stringUtil", StringUtil_IW.getInstance());

		// Time zone util

		variables.put("timeZoneUtil", TimeZoneUtil_IW.getInstance());

		// Unicode formatter

		variables.put("unicodeFormatter", UnicodeFormatter_IW.getInstance());

		// URL codec

		variables.put("urlCodec", URLCodec_IW.getInstance());

		// Validator

		variables.put("validator", Validator_IW.getInstance());

		// Web server servlet token

		try {
			variables.put(
				"webServerToken",
				WebServerServletTokenUtil.getWebServerServletToken());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Window state factory

		variables.put(
			"windowStateFactory", WindowStateFactory_IW.getInstance());

		// Permissions

		try {
			variables.put(
				"commonPermission", CommonPermissionUtil.getCommonPermission());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"groupPermission", GroupPermissionUtil.getGroupPermission());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"layoutPermission", LayoutPermissionUtil.getLayoutPermission());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"organizationPermission",
				OrganizationPermissionUtil.getOrganizationPermission());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"passwordPolicyPermission",
				PasswordPolicyPermissionUtil.getPasswordPolicyPermission());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"portalPermission", PortalPermissionUtil.getPortalPermission());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"portletPermission",
				PortletPermissionUtil.getPortletPermission());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		Map<String, PortletProvider.Action> portletProviderActionMap =
			new HashMap<>();

		for (PortletProvider.Action action : PortletProvider.Action.values()) {
			portletProviderActionMap.put(action.name(), action);
		}

		try {
			variables.put("portletProviderAction", portletProviderActionMap);
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"rolePermission", RolePermissionUtil.getRolePermission());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"userGroupPermission",
				UserGroupPermissionUtil.getUserGroupPermission());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"userPermission", UserPermissionUtil.getUserPermission());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		// Deprecated

		populateDeprecatedCommonHelperUtilities(variables);
	}

	@SuppressWarnings("deprecation")
	protected void populateDeprecatedCommonHelperUtilities(
		Map<String, Object> variables) {

		try {
			variables.put(
				"dateFormats",
				FastDateFormatFactoryUtil.getFastDateFormatFactory());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"imageToken",
				WebServerServletTokenUtil.getWebServerServletToken());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put(
				"locationPermission",
				OrganizationPermissionUtil.getOrganizationPermission());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}

		try {
			variables.put("random", new Random());
		}
		catch (SecurityException securityException) {
			_log.error(securityException);
		}
	}

	protected void populateExtraHelperUtilities(
		Map<String, Object> variables, boolean restricted) {
	}

	protected void prepareTiles(
		Map<String, Object> contextObjects,
		HttpServletRequest httpServletRequest) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Definition definition = (Definition)httpServletRequest.getAttribute(
			TilesUtil.DEFINITION);

		if (definition == null) {
			themeDisplay.setTilesSelectable(true);

			return;
		}

		Map<String, String> attributes = definition.getAttributes();

		String tilesTitle = attributes.get("title");

		themeDisplay.setTilesTitle(tilesTitle);

		contextObjects.put("tilesTitle", tilesTitle);

		String tilesContent = attributes.get("content");

		themeDisplay.setTilesContent(tilesContent);

		contextObjects.put("tilesContent", tilesContent);

		boolean tilesSelectable = GetterUtil.getBoolean(
			attributes.get("selectable"));

		themeDisplay.setTilesSelectable(tilesSelectable);

		contextObjects.put("tilesSelectable", tilesSelectable);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		TemplateContextHelper.class);

	private final Map<ClassLoader, Map<String, Object>[]> _helperUtilitiesMaps =
		new ConcurrentHashMap<>();

	private static class HttpWrapper implements Http {

		public HttpWrapper(Http http) {
			this(http, true);
		}

		public HttpWrapper(Http http, boolean disableLocalNetworkAccess) {
			_http = http;
			_disableLocalNetworkAccess = disableLocalNetworkAccess;
		}

		@Override
		public Cookie[] getCookies() {
			return _http.getCookies();
		}

		@Override
		public boolean hasProxyConfig() {
			return _http.hasProxyConfig();
		}

		@Override
		public boolean isNonProxyHost(String host) {
			return _http.isNonProxyHost(host);
		}

		@Override
		public boolean isProxyHost(String host) {
			return _http.isProxyHost(host);
		}

		@Override
		public byte[] URLtoByteArray(Options options) throws IOException {
			if (isLocationAccessDenied(options.getLocation())) {
				throw new IOException(
					StringBundler.concat(
						"Denied access to resource ", options.getLocation(),
						" using $httpUtil variable from a template. Please ",
						"use restricted variable $httpUtilUnsafe to access ",
						"local network."));
			}

			return _http.URLtoByteArray(options);
		}

		@Override
		public byte[] URLtoByteArray(String location) throws IOException {
			if (isLocationAccessDenied(location)) {
				throw new IOException(
					StringBundler.concat(
						"Denied access to resource ", location,
						" using $httpUtil variable from a template. Please ",
						"use restricted variable $httpUtilUnsafe to access ",
						"local network."));
			}

			return _http.URLtoByteArray(location);
		}

		@Override
		public byte[] URLtoByteArray(String location, boolean post)
			throws IOException {

			if (isLocationAccessDenied(location)) {
				throw new IOException(
					StringBundler.concat(
						"Denied access to resource ", location,
						" using $httpUtil variable from a template. Please ",
						"use restricted variable $httpUtilUnsafe to access ",
						"local network."));
			}

			return _http.URLtoByteArray(location, post);
		}

		@Override
		public InputStream URLtoInputStream(Options options)
			throws IOException {

			if (isLocationAccessDenied(options.getLocation())) {
				throw new IOException(
					StringBundler.concat(
						"Denied access to resource ", options.getLocation(),
						" using $httpUtil variable from a template. Please ",
						"use restricted variable $httpUtilUnsafe to access ",
						"local network."));
			}

			return _http.URLtoInputStream(options);
		}

		@Override
		public InputStream URLtoInputStream(String location)
			throws IOException {

			if (isLocationAccessDenied(location)) {
				throw new IOException(
					StringBundler.concat(
						"Denied access to resource ", location,
						" using $httpUtil variable from a template. Please ",
						"use restricted variable $httpUtilUnsafe to access ",
						"local network."));
			}

			return _http.URLtoInputStream(location);
		}

		@Override
		public InputStream URLtoInputStream(String location, boolean post)
			throws IOException {

			if (isLocationAccessDenied(location)) {
				throw new IOException(
					StringBundler.concat(
						"Denied access to resource ", location,
						" using $httpUtil variable from a template. Please ",
						"use restricted variable $httpUtilUnsafe to access ",
						"local network."));
			}

			return _http.URLtoInputStream(location, post);
		}

		@Override
		public String URLtoString(Options options) throws IOException {
			if (isLocationAccessDenied(options.getLocation())) {
				throw new IOException(
					StringBundler.concat(
						"Denied access to resource ", options.getLocation(),
						" using $httpUtil variable from a template. Please ",
						"use restricted variable $httpUtilUnsafe to access ",
						"local network."));
			}

			return _http.URLtoString(options);
		}

		@Override
		public String URLtoString(String location) throws IOException {
			if (isLocationAccessDenied(location)) {
				throw new IOException(
					StringBundler.concat(
						"Denied access to resource ", location,
						" using $httpUtil variable from a template. Please ",
						"use restricted variable $httpUtilUnsafe to access ",
						"local network."));
			}

			return _http.URLtoString(location);
		}

		@Override
		public String URLtoString(String location, boolean post)
			throws IOException {

			if (isLocationAccessDenied(location)) {
				throw new IOException(
					StringBundler.concat(
						"Denied access to resource ", location,
						" using $httpUtil variable from a template. Please ",
						"use restricted variable $httpUtilUnsafe to access ",
						"local network."));
			}

			return _http.URLtoString(location, post);
		}

		@Override
		public String URLtoString(URL url) throws IOException {
			String protocol = url.getProtocol();

			if (!HTTP.equals(protocol) && !HTTPS.equals(protocol)) {
				throw new IOException(
					StringBundler.concat(
						"Denied access to resource ", url.toString(),
						". $httpUtil template variable supports only HTTP and ",
						"HTTPS protocols."));
			}

			if (isLocationAccessDenied(url.toString())) {
				throw new IOException(
					StringBundler.concat(
						"Denied access to resource ", url.toString(),
						" using $httpUtil variable from a template. Please ",
						"use restricted variable $httpUtilUnsafe to access ",
						"local network."));
			}

			return _http.URLtoString(url);
		}

		protected boolean isLocationAccessDenied(String location)
			throws IOException {

			if (_disableLocalNetworkAccess) {
				URL url = new URL(location);

				if (InetAddressUtil.isLocalInetAddress(
						InetAddressUtil.getInetAddressByName(url.getHost()))) {

					return true;
				}
			}

			return false;
		}

		private final boolean _disableLocalNetworkAccess;
		private final Http _http;

	}

}