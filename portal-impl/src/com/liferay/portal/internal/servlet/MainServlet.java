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

package com.liferay.portal.internal.servlet;

import com.liferay.petra.io.StreamUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.events.EventsProcessorUtil;
import com.liferay.portal.events.StartupAction;
import com.liferay.portal.events.StartupHelperUtil;
import com.liferay.portal.kernel.cache.thread.local.Lifecycle;
import com.liferay.portal.kernel.cache.thread.local.ThreadLocalCacheManager;
import com.liferay.portal.kernel.dependency.manager.DependencyManagerSyncUtil;
import com.liferay.portal.kernel.deploy.hot.HotDeployUtil;
import com.liferay.portal.kernel.exception.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutTemplate;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletApp;
import com.liferay.portal.kernel.model.PortletFilter;
import com.liferay.portal.kernel.model.PortletURLListener;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.module.util.ServiceLatch;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.plugin.PluginPackage;
import com.liferay.portal.kernel.portlet.PortletConfigFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletInstanceFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutTemplateLocalServiceUtil;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.InactiveRequestHandler;
import com.liferay.portal.kernel.servlet.PortalSessionThreadLocal;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.upgrade.ReleaseManager;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PortalLifecycleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.ServiceProxyFactory;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.UnsecureSAXReaderUtil;
import com.liferay.portal.plugin.PluginPackageUtil;
import com.liferay.portal.security.jaas.JAASHelper;
import com.liferay.portal.service.impl.LayoutTemplateLocalServiceImpl;
import com.liferay.portal.servlet.EncryptedServletRequest;
import com.liferay.portal.servlet.I18nServlet;
import com.liferay.portal.servlet.filters.absoluteredirects.AbsoluteRedirectsResponse;
import com.liferay.portal.servlet.filters.i18n.I18nFilter;
import com.liferay.portal.setup.SetupWizardSampleDataUtil;
import com.liferay.portal.struts.Action;
import com.liferay.portal.struts.PortalRequestProcessor;
import com.liferay.portal.struts.StrutsUtil;
import com.liferay.portal.struts.TilesUtil;
import com.liferay.portal.struts.model.ActionForward;
import com.liferay.portal.struts.model.ActionMapping;
import com.liferay.portal.struts.model.ModuleConfig;
import com.liferay.portal.util.MaintenanceUtil;
import com.liferay.portal.util.PortalInstances;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.ShutdownUtil;
import com.liferay.portlet.PortletBagFactory;
import com.liferay.portlet.PortletFilterFactory;
import com.liferay.portlet.PortletURLListenerFactory;
import com.liferay.social.kernel.util.SocialConfigurationUtil;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Brian Wing Shun Chan
 * @author Jorge Ferrer
 * @author Brian Myunghun Kim
 */
public class MainServlet extends HttpServlet {

	@Override
	public void destroy() {
		if (_log.isDebugEnabled()) {
			_log.debug("Destroy plugins");
		}

		DependencyManagerSyncUtil.sync();

		_portalInitializedModuleServiceLifecycleServiceRegistration.
			unregister();
		_portalPortletsInitializedModuleServiceLifecycleServiceRegistration.
			unregister();
		_servletContextServiceRegistration.unregister();
		_systemCheckModuleServiceLifecycleServiceRegistration.unregister();

		_licenseInstallModuleServiceLifecycleServiceRegistration.unregister();

		PortalLifecycleUtil.flushDestroys();

		List<Portlet> portlets = PortletLocalServiceUtil.getPortlets();

		if (_log.isDebugEnabled()) {
			_log.debug("Destroy portlets");
		}

		try {
			_destroyPortlets(portlets);
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Destroy companies");
		}

		try {
			_destroyCompanies();
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Process global shutdown events");
		}

		try {
			EventsProcessorUtil.process(
				PropsKeys.GLOBAL_SHUTDOWN_EVENTS,
				PropsValues.GLOBAL_SHUTDOWN_EVENTS);
		}
		catch (Exception exception) {
			_log.error(exception);
		}
	}

	@Override
	public void doGet(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		_portalRequestProcessor.process(
			httpServletRequest, httpServletResponse);
	}

	@Override
	public void doPost(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		_portalRequestProcessor.process(
			httpServletRequest, httpServletResponse);
	}

	@Override
	public void init() throws ServletException {
		if (_log.isDebugEnabled()) {
			_log.debug("Initialize");
		}

		ServletContext servletContext = getServletContext();

		servletContext.setAttribute(MainServlet.class.getName(), Boolean.TRUE);

		_portalRequestProcessor = new PortalRequestProcessor(
			servletContext, _init());

		if (_log.isDebugEnabled()) {
			_log.debug("Verify JVM configuration");
		}

		if (_log.isWarnEnabled()) {
			if (!StringPool.DEFAULT_CHARSET_NAME.startsWith("UTF-")) {
				_log.warn(
					StringBundler.concat(
						"The default JVM character set \"",
						StringPool.DEFAULT_CHARSET_NAME,
						"\" is not UTF. Please review the JVM property ",
						"\"file.encoding\"."));
			}

			TimeZone timeZone = TimeZone.getDefault();

			String timeZoneID = timeZone.getID();

			if (!Objects.equals("UTC", timeZoneID) &&
				!Objects.equals("GMT", timeZoneID)) {

				_log.warn(
					StringBundler.concat(
						"The default JVM time zone \"", timeZoneID,
						"\" is not UTC or GMT. Please review the JVM property ",
						"\"user.timezone\"."));
			}
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Process startup events");
		}

		try {
			StartupAction startupAction = new StartupAction();

			startupAction.run(null);
		}
		catch (Exception exception) {
			_log.error(exception);

			System.out.println(
				"Stopping the server due to unexpected startup errors");

			System.exit(0);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize plugin package");
		}

		PluginPackage pluginPackage = null;

		try {
			pluginPackage = PluginPackageUtil.readPluginPackageServletContext(
				servletContext);
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize portlets");
		}

		List<Portlet> portlets = new ArrayList<>();

		try {
			portlets.addAll(_initPortlets(pluginPackage));
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		for (Portlet portlet : portlets) {
			try {
				ResourceActionsUtil.populatePortletResource(
					portlet, MainServlet.class.getClassLoader(),
					PropsValues.RESOURCE_ACTIONS_CONFIGS);
			}
			catch (Exception exception) {
				_log.error(exception);
			}
		}

		try {
			_initLayoutTemplates(pluginPackage);
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize social");
		}

		try {
			SocialConfigurationUtil.read(
				PortalClassLoaderUtil.getClassLoader(),
				new String[] {
					StreamUtil.toString(
						servletContext.getResourceAsStream(
							"/WEB-INF/liferay-social.xml")),
					StreamUtil.toString(
						servletContext.getResourceAsStream(
							"/WEB-INF/liferay-social-ext.xml"))
				});
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize web settings");
		}

		try {
			String xml = StreamUtil.toString(
				servletContext.getResourceAsStream(
					"/WEB-INF/shielded-container-web.xml"));

			_checkWebSettings(xml);
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Process global startup events");
		}

		try {
			EventsProcessorUtil.process(
				PropsKeys.GLOBAL_STARTUP_EVENTS,
				PropsValues.GLOBAL_STARTUP_EVENTS);
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize resource actions");
		}

		try {
			_initCompanies();
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize plugins");
		}

		try {
			HotDeployUtil.setCapturePrematureEvents(false);

			PortalLifecycleUtil.flushInits();
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		servletContext.setAttribute(WebKeys.STARTUP_FINISHED, Boolean.TRUE);

		StartupHelperUtil.setStartupFinished(true);

		_registerPortalInitialized();

		if ((_releaseManager != null) && _log.isWarnEnabled()) {
			String message = _releaseManager.getStatusMessage(true);

			if (Validator.isNotNull(message)) {
				_log.warn(message);
			}
			else if (_log.isInfoEnabled()) {
				message = _releaseManager.getStatusMessage(false);

				if (Validator.isNotNull(message)) {
					_log.info(message);
				}
			}
		}

		if (StartupHelperUtil.isDBNew() &&
			PropsValues.SETUP_WIZARD_ADD_SAMPLE_DATA) {

			try {
				SetupWizardSampleDataUtil.addSampleData(
					PortalInstances.getDefaultCompanyId());
			}
			catch (Exception exception) {
				_log.error(exception);
			}
		}

		ThreadLocalCacheManager.clearAll(Lifecycle.REQUEST);
	}

	@Override
	public void service(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		if (_log.isDebugEnabled()) {
			_log.debug("Process service request");
		}

		if (_processShutdownRequest(httpServletRequest, httpServletResponse)) {
			if (_log.isDebugEnabled()) {
				_log.debug("Processed shutdown request");
			}

			return;
		}

		if (_processMaintenanceRequest(
				httpServletRequest, httpServletResponse)) {

			if (_log.isDebugEnabled()) {
				_log.debug("Processed maintenance request");
			}

			return;
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Get company id");
		}

		long companyId = PortalInstances.getCompanyId(httpServletRequest);

		if (_processCompanyInactiveRequest(
				httpServletRequest, httpServletResponse, companyId)) {

			if (_log.isDebugEnabled()) {
				_log.debug("Processed company inactive request");
			}

			return;
		}

		try {
			if (_processGroupInactiveRequest(
					httpServletRequest, httpServletResponse)) {

				if (_log.isDebugEnabled()) {
					_log.debug("Processed site inactive request");
				}

				return;
			}
		}
		catch (Exception exception) {
			if (exception instanceof NoSuchLayoutException) {
				if (_log.isDebugEnabled()) {
					_log.debug(exception);
				}
			}
			else {
				_log.error(exception);
			}
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Set portal port");
		}

		PortalUtil.setPortalInetSocketAddresses(httpServletRequest);

		if (_log.isDebugEnabled()) {
			_log.debug("Check variables");
		}

		httpServletRequest.setAttribute(WebKeys.CTX, getServletContext());

		if (_log.isDebugEnabled()) {
			_log.debug("Encrypt request");
		}

		httpServletRequest = _encryptRequest(httpServletRequest, companyId);

		long userId = PortalUtil.getUserId(httpServletRequest);

		String remoteUser = _getRemoteUser(httpServletRequest, userId);

		try {
			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						"Authenticate user id ", userId, " and remote user ",
						remoteUser));
			}

			userId = _loginUser(
				httpServletRequest, httpServletResponse, companyId, userId,
				remoteUser);

			if (_log.isDebugEnabled()) {
				_log.debug("Authenticated user id " + userId);
			}
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Set session thread local");
		}

		PortalSessionThreadLocal.setHttpSession(
			httpServletRequest.getSession());

		if (_log.isDebugEnabled()) {
			_log.debug("Process service pre events");
		}

		if (_processServicePre(
				httpServletRequest, httpServletResponse, userId)) {

			if (_log.isDebugEnabled()) {
				_log.debug("Processing service pre events has errors");
			}

			return;
		}

		Object classNameAttribute = httpServletRequest.getAttribute(
			AbsoluteRedirectsResponse.class.getName());

		if (classNameAttribute != null) {
			if (_log.isDebugEnabled()) {
				String currentURL = PortalUtil.getCurrentURL(
					httpServletRequest);

				_log.debug(
					"Current URL " + currentURL + " has absolute redirect");
			}

			return;
		}

		if (httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY) == null) {
			if (_log.isDebugEnabled()) {
				String currentURL = PortalUtil.getCurrentURL(
					httpServletRequest);

				_log.debug(
					"Current URL " + currentURL +
						" does not have a theme display");
			}

			return;
		}

		try {
			if (_log.isDebugEnabled()) {
				_log.debug("Call parent service");
			}

			super.service(httpServletRequest, httpServletResponse);
		}
		finally {
			if (_log.isDebugEnabled()) {
				_log.debug("Process service post events");
			}

			try {
				EventsProcessorUtil.process(
					PropsKeys.SERVLET_SERVICE_EVENTS_POST,
					PropsValues.SERVLET_SERVICE_EVENTS_POST, httpServletRequest,
					httpServletResponse);
			}
			catch (Exception exception) {
				_log.error(exception);
			}
		}
	}

	private void _checkWebSettings(String xml) throws DocumentException {
		Document doc = UnsecureSAXReaderUtil.read(xml);

		Element root = doc.getRootElement();

		int timeout = PropsValues.SESSION_TIMEOUT;

		Element sessionConfig = root.element("session-config");

		if (sessionConfig != null) {
			String sessionTimeout = sessionConfig.elementText(
				"session-timeout");

			timeout = GetterUtil.getInteger(sessionTimeout, timeout);
		}

		PropsUtil.set(PropsKeys.SESSION_TIMEOUT, String.valueOf(timeout));

		PropsValues.SESSION_TIMEOUT = timeout;

		I18nServlet.setLanguageIds(root);

		I18nFilter.setLanguageIds(I18nServlet.getLanguageIds());
	}

	private void _destroyCompanies() throws Exception {
		CompanyLocalServiceUtil.forEachCompanyId(
			companyId -> _destroyCompany(companyId));
	}

	private void _destroyCompany(long companyId) {
		if (_log.isDebugEnabled()) {
			_log.debug("Process shutdown events");
		}

		try {
			EventsProcessorUtil.process(
				PropsKeys.APPLICATION_SHUTDOWN_EVENTS,
				PropsValues.APPLICATION_SHUTDOWN_EVENTS,
				new String[] {String.valueOf(companyId)});
		}
		catch (Exception exception) {
			_log.error(exception);
		}
	}

	private void _destroyPortlets(List<Portlet> portlets) throws Exception {
		for (Portlet portlet : portlets) {
			PortletInstanceFactoryUtil.destroy(portlet);

			Map<String, PortletFilter> portletFilters =
				portlet.getPortletFilters();

			for (PortletFilter portletFilter : portletFilters.values()) {
				PortletFilterFactory.destroy(portletFilter);
			}
		}
	}

	private HttpServletRequest _encryptRequest(
		HttpServletRequest httpServletRequest, long companyId) {

		boolean encryptRequest = ParamUtil.getBoolean(
			httpServletRequest, WebKeys.ENCRYPT);

		if (!encryptRequest) {
			return httpServletRequest;
		}

		try {
			Company company = CompanyLocalServiceUtil.getCompanyById(companyId);

			httpServletRequest = new EncryptedServletRequest(
				httpServletRequest, company.getKeyObj());
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return httpServletRequest;
	}

	private String _getRemoteUser(
		HttpServletRequest httpServletRequest, long userId) {

		String remoteUser = httpServletRequest.getRemoteUser();

		if (!PropsValues.PORTAL_JAAS_ENABLE) {
			HttpSession httpSession = httpServletRequest.getSession();

			String jRemoteUser = (String)httpSession.getAttribute(
				"j_remoteuser");

			if (jRemoteUser != null) {
				remoteUser = jRemoteUser;
			}
		}

		if ((userId > 0) && (remoteUser == null)) {
			remoteUser = String.valueOf(userId);
		}

		return remoteUser;
	}

	private ModuleConfig _init() throws ServletException {
		try {
			TilesUtil.loadDefinitions(getServletContext());

			return _initModuleConfig();
		}
		catch (Exception exception) {
			throw new ServletException(exception);
		}
	}

	private void _initCompanies() throws Exception {
		if (_log.isDebugEnabled()) {
			_log.debug("Initialize companies");
		}

		if (StartupHelperUtil.isDBNew()) {
			CompanyLocalServiceUtil.addCompany(
				null, PropsValues.COMPANY_DEFAULT_WEB_ID, "localhost",
				PropsValues.COMPANY_DEFAULT_WEB_ID, false, 0, true);
		}

		ServletContext servletContext = getServletContext();

		try {
			String[] webIds = PortalInstances.getWebIds();

			for (String webId : webIds) {
				boolean skipCheck = false;

				if (StartupHelperUtil.isDBNew() &&
					webId.equals(PropsValues.COMPANY_DEFAULT_WEB_ID)) {

					skipCheck = true;
				}

				PortalInstances.initCompany(servletContext, webId, skipCheck);
			}
		}
		finally {
			CompanyThreadLocal.setCompanyId(
				PortalInstances.getDefaultCompanyId());
		}
	}

	private void _initLayoutTemplates(PluginPackage pluginPackage) {
		ServiceLatch serviceLatch = SystemBundleUtil.newServiceLatch();

		for (String langType :
				LayoutTemplateLocalServiceImpl.supportedLangTypes) {

			StringBundler sb = new StringBundler(5);

			sb.append("(&(language.type=");
			sb.append(langType);
			sb.append(")(objectClass=");
			sb.append(TemplateManager.class.getName());
			sb.append("))");

			serviceLatch.waitFor(sb.toString());
		}

		serviceLatch.openOn(
			() -> {
				try {
					if (_log.isDebugEnabled()) {
						_log.debug("Initialize layout templates");
					}

					ServletContext servletContext = getServletContext();

					List<LayoutTemplate> layoutTemplates =
						LayoutTemplateLocalServiceUtil.init(
							servletContext,
							new String[] {
								StreamUtil.toString(
									servletContext.getResourceAsStream(
										"/WEB-INF/liferay-layout-" +
											"templates.xml")),
								StreamUtil.toString(
									servletContext.getResourceAsStream(
										"/WEB-INF/liferay-layout-templates-" +
											"ext.xml"))
							},
							pluginPackage);

					servletContext.setAttribute(
						WebKeys.PLUGIN_LAYOUT_TEMPLATES, layoutTemplates);
				}
				catch (Exception exception) {
					_log.error(exception);
				}
			});
	}

	private ModuleConfig _initModuleConfig() throws Exception {
		ModuleConfig moduleConfig = new ModuleConfig();

		ServletContext servletContext = getServletContext();

		ClassLoader classLoader = MainServlet.class.getClassLoader();

		try (InputStream inputStream = servletContext.getResourceAsStream(
				"/WEB-INF/struts-config.xml")) {

			Document document = SAXReaderUtil.read(inputStream, false);

			Element rootElement = document.getRootElement();

			Element globalForwardsElement = rootElement.element(
				"global-forwards");

			for (Element forwardElement :
					globalForwardsElement.elements("forward")) {

				moduleConfig.addActionForward(
					new ActionForward(
						forwardElement.attributeValue("name"),
						forwardElement.attributeValue("path")));
			}

			Element actionMappingsElement = rootElement.element(
				"action-mappings");

			for (Element actionElement :
					actionMappingsElement.elements("action")) {

				Action action = null;

				String type = actionElement.attributeValue("type");

				if (type != null) {
					Class<? extends Action> clazz =
						(Class<? extends Action>)classLoader.loadClass(
							actionElement.attributeValue("type"));

					action = clazz.newInstance();
				}

				ActionMapping actionMapping = new ActionMapping(
					moduleConfig, actionElement.attributeValue("forward"),
					actionElement.attributeValue("path"), action);

				for (Element forwardElement :
						actionElement.elements("forward")) {

					actionMapping.addActionForward(
						new ActionForward(
							forwardElement.attributeValue("name"),
							forwardElement.attributeValue("path")));
				}

				moduleConfig.addActionMapping(actionMapping);
			}
		}

		return moduleConfig;
	}

	private void _initPortletApp(Portlet portlet, ServletContext servletContext)
		throws Exception {

		PortletApp portletApp = portlet.getPortletApp();

		PortletConfig portletConfig = PortletConfigFactoryUtil.create(
			portlet, servletContext);

		PortletContext portletContext = portletConfig.getPortletContext();

		Set<PortletFilter> portletFilters = portletApp.getPortletFilters();

		for (PortletFilter portletFilter : portletFilters) {
			PortletFilterFactory.create(portletFilter, portletContext);
		}

		Set<PortletURLListener> portletURLListeners =
			portletApp.getPortletURLListeners();

		for (PortletURLListener portletURLListener : portletURLListeners) {
			PortletURLListenerFactory.create(portletURLListener);
		}
	}

	private List<Portlet> _initPortlets(PluginPackage pluginPackage)
		throws Exception {

		ServletContext servletContext = getServletContext();

		String[] xmls = new String[PropsValues.PORTLET_CONFIGS.length];

		for (int i = 0; i < PropsValues.PORTLET_CONFIGS.length; i++) {
			xmls[i] = StreamUtil.toString(
				servletContext.getResourceAsStream(
					PropsValues.PORTLET_CONFIGS[i]));
		}

		PortletLocalServiceUtil.initEAR(servletContext, xmls, pluginPackage);

		PortletBagFactory portletBagFactory = new PortletBagFactory();

		portletBagFactory.setClassLoader(
			PortalClassLoaderUtil.getClassLoader());
		portletBagFactory.setServletContext(servletContext);
		portletBagFactory.setWARFile(false);

		List<Portlet> portlets = PortletLocalServiceUtil.getPortlets();

		for (int i = 0; i < portlets.size(); i++) {
			Portlet portlet = portlets.get(i);

			portletBagFactory.create(portlet);

			if (i == 0) {
				_initPortletApp(portlet, servletContext);
			}
		}

		servletContext.setAttribute(WebKeys.PLUGIN_PORTLETS, portlets);

		return portlets;
	}

	private long _loginUser(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, long companyId,
			long userId, String remoteUser)
		throws PortalException {

		if ((userId > 0) || (remoteUser == null)) {
			return userId;
		}

		if (PropsValues.PORTAL_JAAS_ENABLE) {
			try {
				userId = JAASHelper.getJaasUserId(companyId, remoteUser);
			}
			catch (Exception exception) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						StringBundler.concat(
							"Unable to sign in ", remoteUser, " in company ",
							companyId, " using JAAS: ", exception.getMessage()),
						exception);
				}
			}
		}
		else {
			userId = GetterUtil.getLong(remoteUser);
		}

		User user = UserLocalServiceUtil.getUserById(userId);

		if (!user.isDefaultUser()) {
			EventsProcessorUtil.process(
				PropsKeys.LOGIN_EVENTS_PRE, PropsValues.LOGIN_EVENTS_PRE,
				httpServletRequest, httpServletResponse);

			if (PropsValues.USERS_UPDATE_LAST_LOGIN ||
				(user.getLastLoginDate() == null)) {

				user = UserLocalServiceUtil.updateLastLogin(
					userId, httpServletRequest.getRemoteAddr());
			}
		}

		if (httpServletRequest.getAttribute(WebKeys.USER) != null) {
			httpServletRequest.setAttribute(WebKeys.USER, user);
			httpServletRequest.setAttribute(
				WebKeys.USER_ID, Long.valueOf(userId));
		}

		HttpSession httpSession = httpServletRequest.getSession();

		httpSession.setAttribute(WebKeys.LOCALE, user.getLocale());
		httpSession.setAttribute(WebKeys.USER, user);
		httpSession.setAttribute(WebKeys.USER_ID, Long.valueOf(userId));

		httpSession.removeAttribute("j_remoteuser");

		if (!user.isDefaultUser()) {
			EventsProcessorUtil.process(
				PropsKeys.LOGIN_EVENTS_POST, PropsValues.LOGIN_EVENTS_POST,
				httpServletRequest, httpServletResponse);
		}

		return userId;
	}

	private boolean _processCompanyInactiveRequest(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, long companyId)
		throws IOException {

		if (PortalInstances.isCompanyActive(companyId)) {
			return false;
		}

		_inactiveRequestHandler.processInactiveRequest(
			httpServletRequest, httpServletResponse,
			"this-instance-is-inactive-please-contact-the-administrator");

		return true;
	}

	private boolean _processGroupInactiveRequest(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, PortalException {

		long plid = ParamUtil.getLong(httpServletRequest, "p_l_id");

		if (plid <= 0) {
			return false;
		}

		Layout layout = LayoutLocalServiceUtil.getLayout(plid);

		if (GroupLocalServiceUtil.isLiveGroupActive(layout.getGroup())) {
			return false;
		}

		_inactiveRequestHandler.processInactiveRequest(
			httpServletRequest, httpServletResponse,
			"this-site-is-inactive-please-contact-the-administrator");

		return true;
	}

	private boolean _processMaintenanceRequest(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		if (!MaintenanceUtil.isMaintaining()) {
			return false;
		}

		RequestDispatcher requestDispatcher =
			httpServletRequest.getRequestDispatcher(
				"/html/portal/maintenance.jsp");

		requestDispatcher.include(httpServletRequest, httpServletResponse);

		return true;
	}

	private boolean _processServicePre(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, long userId)
		throws IOException, ServletException {

		try {
			EventsProcessorUtil.process(
				PropsKeys.SERVLET_SERVICE_EVENTS_PRE,
				PropsValues.SERVLET_SERVICE_EVENTS_PRE, httpServletRequest,
				httpServletResponse);
		}
		catch (Exception exception) {
			Throwable throwable = exception.getCause();

			if (throwable instanceof NoSuchLayoutException) {
				PortalUtil.sendError(
					HttpServletResponse.SC_NOT_FOUND, (Exception)throwable,
					httpServletRequest, httpServletResponse);

				return true;
			}
			else if (throwable instanceof PrincipalException) {
				_processServicePrePrincipalException(
					throwable, userId, httpServletRequest, httpServletResponse);

				return true;
			}

			_log.error(exception);

			httpServletRequest.setAttribute(StrutsUtil.EXCEPTION, exception);

			StrutsUtil.forward(
				PropsValues.SERVLET_SERVICE_EVENTS_PRE_ERROR_PAGE,
				getServletContext(), httpServletRequest, httpServletResponse);

			if (exception == httpServletRequest.getAttribute(
					StrutsUtil.EXCEPTION)) {

				httpServletRequest.removeAttribute(StrutsUtil.EXCEPTION);
				httpServletRequest.removeAttribute(
					RequestDispatcher.ERROR_EXCEPTION);
				httpServletRequest.removeAttribute(
					RequestDispatcher.ERROR_EXCEPTION_TYPE);
				httpServletRequest.removeAttribute(
					RequestDispatcher.ERROR_MESSAGE);
				httpServletRequest.removeAttribute(
					RequestDispatcher.ERROR_REQUEST_URI);
				httpServletRequest.removeAttribute(
					RequestDispatcher.ERROR_SERVLET_NAME);
				httpServletRequest.removeAttribute(
					RequestDispatcher.ERROR_STATUS_CODE);
			}

			return true;
		}

		if (_HTTP_HEADER_VERSION_VERBOSITY_DEFAULT) {
		}
		else if (_HTTP_HEADER_VERSION_VERBOSITY_PARTIAL) {
			httpServletResponse.addHeader(
				_LIFERAY_PORTAL_REQUEST_HEADER, ReleaseInfo.getName());
		}
		else {
			httpServletResponse.addHeader(
				_LIFERAY_PORTAL_REQUEST_HEADER, ReleaseInfo.getReleaseInfo());
		}

		return false;
	}

	private void _processServicePrePrincipalException(
			Throwable throwable, long userId,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		if ((userId > 0) ||
			(ParamUtil.getInteger(httpServletRequest, "p_p_lifecycle") == 2)) {

			PortalUtil.sendError(
				HttpServletResponse.SC_UNAUTHORIZED, (Exception)throwable,
				httpServletRequest, httpServletResponse);

			return;
		}

		String mainPath = PortalUtil.getPathMain();

		String redirect = mainPath.concat("/portal/login");

		redirect = HttpComponentsUtil.addParameter(
			redirect, "redirect", PortalUtil.getCurrentURL(httpServletRequest));

		long plid = ParamUtil.getLong(httpServletRequest, "p_l_id");

		if (plid > 0) {
			try {
				redirect = HttpComponentsUtil.addParameter(
					redirect, "refererPlid", plid);

				Layout layout = LayoutLocalServiceUtil.getLayout(plid);

				Group group = layout.getGroup();

				plid = group.getDefaultPublicPlid();

				if ((plid == LayoutConstants.DEFAULT_PLID) ||
					group.isStagingGroup()) {

					Group guestGroup = GroupLocalServiceUtil.getGroup(
						layout.getCompanyId(), GroupConstants.GUEST);

					plid = guestGroup.getDefaultPublicPlid();
				}

				redirect = HttpComponentsUtil.addParameter(
					redirect, "p_l_id", plid);
			}
			catch (Exception exception) {
				if (_log.isDebugEnabled()) {
					_log.debug(exception);
				}
			}
		}

		httpServletResponse.sendRedirect(redirect);
	}

	private boolean _processShutdownRequest(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		if (!ShutdownUtil.isShutdown()) {
			return false;
		}

		String messageKey = ShutdownUtil.getMessage();

		if (Validator.isNull(messageKey)) {
			messageKey = "the-system-is-shutdown-please-try-again-later";
		}

		_inactiveRequestHandler.processInactiveRequest(
			httpServletRequest, httpServletResponse, messageKey);

		return true;
	}

	private void _registerPortalInitialized() {
		BundleContext bundleContext = SystemBundleUtil.getBundleContext();

		_portalInitializedModuleServiceLifecycleServiceRegistration =
			bundleContext.registerService(
				ModuleServiceLifecycle.class,
				new ModuleServiceLifecycle() {
				},
				HashMapDictionaryBuilder.<String, Object>put(
					"module.service.lifecycle", "portal.initialized"
				).put(
					"service.vendor", ReleaseInfo.getVendor()
				).put(
					"service.version", ReleaseInfo.getVersion()
				).build());

		_portalPortletsInitializedModuleServiceLifecycleServiceRegistration =
			bundleContext.registerService(
				ModuleServiceLifecycle.class,
				new ModuleServiceLifecycle() {
				},
				HashMapDictionaryBuilder.<String, Object>put(
					"module.service.lifecycle", "portlets.initialized"
				).put(
					"service.vendor", ReleaseInfo.getVendor()
				).put(
					"service.version", ReleaseInfo.getVersion()
				).build());

		_servletContextServiceRegistration = bundleContext.registerService(
			ServletContext.class, getServletContext(),
			HashMapDictionaryBuilder.<String, Object>put(
				"bean.id", ServletContext.class.getName()
			).put(
				"original.bean", Boolean.TRUE
			).put(
				"service.vendor", ReleaseInfo.getVendor()
			).build());

		_systemCheckModuleServiceLifecycleServiceRegistration =
			bundleContext.registerService(
				ModuleServiceLifecycle.class,
				new ModuleServiceLifecycle() {
				},
				HashMapDictionaryBuilder.<String, Object>put(
					"module.service.lifecycle", "system.check"
				).put(
					"service.vendor", ReleaseInfo.getVendor()
				).put(
					"service.version", ReleaseInfo.getVersion()
				).build());

		_licenseInstallModuleServiceLifecycleServiceRegistration =
			bundleContext.registerService(
				ModuleServiceLifecycle.class,
				new ModuleServiceLifecycle() {
				},
				HashMapDictionaryBuilder.<String, Object>put(
					"module.service.lifecycle", "license.install"
				).put(
					"service.vendor", ReleaseInfo.getVendor()
				).put(
					"service.version", ReleaseInfo.getVersion()
				).build());
	}

	private static final boolean _HTTP_HEADER_VERSION_VERBOSITY_DEFAULT =
		StringUtil.equalsIgnoreCase(
			PropsValues.HTTP_HEADER_VERSION_VERBOSITY, "off");

	private static final boolean _HTTP_HEADER_VERSION_VERBOSITY_PARTIAL =
		StringUtil.equalsIgnoreCase(
			PropsValues.HTTP_HEADER_VERSION_VERBOSITY, "partial");

	private static final String _LIFERAY_PORTAL_REQUEST_HEADER =
		"Liferay-Portal";

	private static final Log _log = LogFactoryUtil.getLog(MainServlet.class);

	private static volatile InactiveRequestHandler _inactiveRequestHandler =
		ServiceProxyFactory.newServiceTrackedInstance(
			InactiveRequestHandler.class, MainServlet.class,
			"_inactiveRequestHandler", false);
	private static volatile ReleaseManager _releaseManager =
		ServiceProxyFactory.newServiceTrackedInstance(
			ReleaseManager.class, MainServlet.class, "_releaseManager", false);

	private ServiceRegistration<ModuleServiceLifecycle>
		_licenseInstallModuleServiceLifecycleServiceRegistration;
	private ServiceRegistration<ModuleServiceLifecycle>
		_portalInitializedModuleServiceLifecycleServiceRegistration;
	private ServiceRegistration<ModuleServiceLifecycle>
		_portalPortletsInitializedModuleServiceLifecycleServiceRegistration;
	private PortalRequestProcessor _portalRequestProcessor;
	private ServiceRegistration<ServletContext>
		_servletContextServiceRegistration;
	private ServiceRegistration<ModuleServiceLifecycle>
		_systemCheckModuleServiceLifecycleServiceRegistration;

}