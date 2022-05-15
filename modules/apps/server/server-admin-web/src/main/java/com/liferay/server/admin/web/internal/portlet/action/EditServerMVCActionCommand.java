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

package com.liferay.server.admin.web.internal.portlet.action;

import com.liferay.configuration.admin.constants.ConfigurationAdminPortletKeys;
import com.liferay.document.library.kernel.util.DLPreviewableProcessor;
import com.liferay.mail.kernel.model.Account;
import com.liferay.mail.kernel.service.MailService;
import com.liferay.petra.log4j.Log4JUtil;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.convert.ConvertException;
import com.liferay.portal.convert.ConvertProcess;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.cache.SingleVMPool;
import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.kernel.cluster.ClusterMasterExecutor;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.image.Ghostscript;
import com.liferay.portal.kernel.image.ImageMagickUtil;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncPrintWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.log.SanitizerLogWrapper;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutRevision;
import com.liferay.portal.kernel.model.LayoutStagingHandler;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiServiceUtil;
import com.liferay.portal.kernel.portlet.LiferayActionResponse;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.scripting.Scripting;
import com.liferay.portal.kernel.scripting.ScriptingException;
import com.liferay.portal.kernel.scripting.ScriptingHelperUtil;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.membershippolicy.OrganizationMembershipPolicy;
import com.liferay.portal.kernel.security.membershippolicy.OrganizationMembershipPolicyFactory;
import com.liferay.portal.kernel.security.membershippolicy.RoleMembershipPolicy;
import com.liferay.portal.kernel.security.membershippolicy.RoleMembershipPolicyFactory;
import com.liferay.portal.kernel.security.membershippolicy.SiteMembershipPolicy;
import com.liferay.portal.kernel.security.membershippolicy.SiteMembershipPolicyFactory;
import com.liferay.portal.kernel.security.membershippolicy.UserGroupMembershipPolicy;
import com.liferay.portal.kernel.security.membershippolicy.UserGroupMembershipPolicyFactory;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutRevisionLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceComponentLocalService;
import com.liferay.portal.kernel.servlet.DirectServletRegistry;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.InstancePool;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.ThreadUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.UnsyncPrintWriterPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.MaintenanceUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.ShutdownUtil;
import com.liferay.server.admin.web.internal.constants.ImageMagickResourceLimitConstants;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;

import org.apache.logging.log4j.Level;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 * @author Philip Jones
 */
@Component(
	property = {
		"javax.portlet.name=" + ConfigurationAdminPortletKeys.INSTANCE_SETTINGS,
		"javax.portlet.name=" + PortletKeys.SERVER_ADMIN,
		"mvc.command.name=/server_admin/edit_server"
	},
	service = {IdentifiableOSGiService.class, MVCActionCommand.class}
)
public class EditServerMVCActionCommand
	extends BaseMVCActionCommand implements IdentifiableOSGiService {

	@Override
	public void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		if (!permissionChecker.isOmniadmin()) {
			SessionErrors.add(
				actionRequest,
				PrincipalException.MustBeOmniadmin.class.getName());

			actionResponse.setRenderParameter("mvcPath", "/error.jsp");

			return;
		}

		PortletPreferences portletPreferences = PrefsPropsUtil.getPreferences(
			ParamUtil.getLong(actionRequest, "preferencesCompanyId"));

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		String redirect = ParamUtil.getString(actionRequest, "redirect");

		if (cmd.equals("addLogLevel")) {
			_updateLogLevels(
				Collections.singletonMap(
					ParamUtil.getString(actionRequest, "loggerName"),
					ParamUtil.getString(actionRequest, "priority")));
		}
		else if (cmd.equals("cacheDb")) {
			_cacheDb();
		}
		else if (cmd.equals("cacheMulti")) {
			_cacheMulti();
		}
		else if (cmd.equals("cacheServlet")) {
			_cacheServlet();
		}
		else if (cmd.equals("cacheSingle")) {
			_cacheSingle();
		}
		else if (cmd.equals("cleanUpAddToPagePermissions")) {
			_cleanUpAddToPagePermissions(actionRequest);
		}
		else if (cmd.equals("cleanUpLayoutRevisionPortletPreferences")) {
			_cleanUpLayoutRevisionPortletPreferences();
		}
		else if (cmd.equals("cleanUpOrphanedPortletPreferences")) {
			_cleanUpOrphanedPortletPreferences();
		}
		else if (cmd.startsWith("convertProcess.")) {
			redirect = _convertProcess(actionRequest, actionResponse, cmd);
		}
		else if (cmd.equals("dlPreviews")) {
			DLPreviewableProcessor.deleteFiles();
		}
		else if (cmd.equals("gc")) {
			_gc();
		}
		else if (cmd.equals("runScript")) {
			_runScript(actionRequest, actionResponse);
		}
		else if (cmd.equals("shutdown")) {
			_shutdown(actionRequest);
		}
		else if (cmd.equals("threadDump")) {
			_threadDump();
		}
		else if (cmd.equals("updateExternalServices")) {
			_updateExternalServices(actionRequest, portletPreferences);
		}
		else if (cmd.equals("updateLogLevels")) {
			_updateLogLevels(actionRequest);
		}
		else if (cmd.equals("updateMail")) {
			_updateMail(actionRequest, portletPreferences);
		}
		else if (cmd.equals("verifyMembershipPolicies")) {
			_verifyMembershipPolicies();
		}
		else if (cmd.equals("verifyPluginTables")) {
			_verifyPluginTables();
		}

		sendRedirect(actionRequest, actionResponse, redirect);
	}

	@Override
	public String getOSGiServiceIdentifier() {
		return EditServerMVCActionCommand.class.getName();
	}

	private static void _resetLogLevels(
		Map<String, String> logLevels, Map<String, String> customLogSettings) {

		for (Map.Entry<String, String> logLevel : logLevels.entrySet()) {
			Log4JUtil.setLevel(
				logLevel.getKey(), logLevel.getValue(),
				customLogSettings.containsKey(logLevel.getKey()));
		}
	}

	private static void _updateLogLevels(
		Map<String, String> logLevels, String osgiServiceIdentifier) {

		EditServerMVCActionCommand editServerMVCActionCommand =
			(EditServerMVCActionCommand)
				IdentifiableOSGiServiceUtil.getIdentifiableOSGiService(
					osgiServiceIdentifier);

		if (editServerMVCActionCommand == null) {
			return;
		}

		editServerMVCActionCommand._updateLogLevels(logLevels);
	}

	private void _cacheDb() throws Exception {
		CacheRegistryUtil.clear();
	}

	private void _cacheMulti() throws Exception {
		_multiVMPool.clear();
	}

	private void _cacheServlet() throws Exception {
		_directServletRegistry.clearServlets();
	}

	private void _cacheSingle() throws Exception {
		_singleVMPool.clear();
	}

	private void _cleanUpAddToPagePermissions(ActionRequest actionRequest)
		throws Exception {

		long companyId = _portal.getCompanyId(actionRequest);

		Role role = _roleLocalService.getRole(companyId, RoleConstants.GUEST);

		_cleanUpAddToPagePermissions(companyId, role.getRoleId(), false);

		role = _roleLocalService.getRole(companyId, RoleConstants.POWER_USER);

		_cleanUpAddToPagePermissions(companyId, role.getRoleId(), true);

		role = _roleLocalService.getRole(companyId, RoleConstants.USER);

		_cleanUpAddToPagePermissions(companyId, role.getRoleId(), false);
	}

	private void _cleanUpAddToPagePermissions(
			long companyId, long roleId, boolean limitScope)
		throws Exception {

		Group userPersonalSite = _groupLocalService.getGroup(
			companyId, GroupConstants.USER_PERSONAL_SITE);

		String groupIdString = String.valueOf(userPersonalSite.getGroupId());

		for (ResourcePermission resourcePermission :
				_resourcePermissionLocalService.getRoleResourcePermissions(
					roleId)) {

			if (!resourcePermission.hasActionId(ActionKeys.ADD_TO_PAGE)) {
				continue;
			}

			_resourcePermissionLocalService.removeResourcePermission(
				companyId, resourcePermission.getName(),
				resourcePermission.getScope(), resourcePermission.getPrimKey(),
				roleId, ActionKeys.ADD_TO_PAGE);

			if (!limitScope) {
				continue;
			}

			_resourcePermissionLocalService.addResourcePermission(
				companyId, resourcePermission.getName(),
				ResourceConstants.SCOPE_GROUP, groupIdString, roleId,
				ActionKeys.ADD_TO_PAGE);
		}
	}

	private void _cleanUpLayoutRevisionPortletPreferences() throws Exception {
		boolean active = CacheRegistryUtil.isActive();

		CacheRegistryUtil.setActive(true);

		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			ActionableDynamicQuery actionableDynamicQuery =
				_portletPreferencesLocalService.getActionableDynamicQuery();

			actionableDynamicQuery.setAddCriteriaMethod(
				dynamicQuery -> {
					Property plidProperty = PropertyFactoryUtil.forName("plid");

					DynamicQuery layoutRevisionDynamicQuery =
						_layoutRevisionLocalService.dynamicQuery();

					layoutRevisionDynamicQuery.setProjection(
						ProjectionFactoryUtil.property("layoutRevisionId"));

					dynamicQuery.add(
						plidProperty.in(layoutRevisionDynamicQuery));
				});
			actionableDynamicQuery.setParallel(true);
			actionableDynamicQuery.setPerformActionMethod(
				(com.liferay.portal.kernel.model.PortletPreferences
					portletPreferences) -> {

					LayoutRevision layoutRevision =
						_layoutRevisionLocalService.getLayoutRevision(
							portletPreferences.getPlid());

					Layout layout = _layoutLocalService.getLayout(
						layoutRevision.getPlid());

					if (!layout.isTypePortlet() ||
						_containsPortlet(
							layout, portletPreferences.getPortletId())) {

						return;
					}

					LayoutStagingHandler layoutStagingHandler =
						new LayoutStagingHandler(layout);

					layoutStagingHandler.setLayoutRevision(layoutRevision);

					if (_containsPortlet(
							_proxyProviderFunction.apply(layoutStagingHandler),
							portletPreferences.getPortletId())) {

						return;
					}

					if (_log.isWarnEnabled()) {
						_log.warn(
							"Removing portlet preferences " +
								portletPreferences.getPortletPreferencesId());
					}

					_portletPreferencesLocalService.deletePortletPreferences(
						portletPreferences);
				});

			actionableDynamicQuery.performActions();
		}
		finally {
			CacheRegistryUtil.setActive(active);
		}
	}

	private void _cleanUpOrphanedPortletPreferences() throws Exception {
		boolean active = CacheRegistryUtil.isActive();

		CacheRegistryUtil.setActive(true);

		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			ActionableDynamicQuery actionableDynamicQuery =
				_portletPreferencesLocalService.getActionableDynamicQuery();

			actionableDynamicQuery.setParallel(true);
			actionableDynamicQuery.setPerformActionMethod(
				(com.liferay.portal.kernel.model.PortletPreferences pref) -> {
					if ((pref.getOwnerId() !=
							PortletKeys.PREFS_OWNER_ID_DEFAULT) ||
						(pref.getOwnerType() !=
							PortletKeys.PREFS_OWNER_TYPE_LAYOUT)) {

						return;
					}

					Layout layout = _layoutLocalService.getLayout(
						pref.getPlid());

					if (layout.isTypeContent() || layout.isTypeControlPanel()) {
						return;
					}

					UnicodeProperties typeSettingsUnicodeProperties =
						layout.getTypeSettingsProperties();

					Set<String> keys = typeSettingsUnicodeProperties.keySet();

					boolean orphan = true;

					for (String key : keys) {
						String value =
							typeSettingsUnicodeProperties.getProperty(key);

						if (value.contains(pref.getPortletId())) {
							orphan = false;

							break;
						}
					}

					if (orphan) {
						_portletPreferencesLocalService.
							deletePortletPreferences(pref);
					}
				});

			actionableDynamicQuery.performActions();
		}
		finally {
			CacheRegistryUtil.setActive(active);
		}
	}

	private boolean _containsPortlet(Layout layout, String portletId) {
		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		List<String> portletIds = ListUtil.toList(
			layoutTypePortlet.getAllPortlets(), Portlet.PORTLET_ID_ACCESSOR);

		return portletIds.contains(portletId);
	}

	private String _convertProcess(
			ActionRequest actionRequest, ActionResponse actionResponse,
			String cmd)
		throws Exception {

		String className = StringUtil.replaceFirst(
			cmd, "convertProcess.", StringPool.BLANK);

		ConvertProcess convertProcess = (ConvertProcess)InstancePool.get(
			className);

		String[] parameters = convertProcess.getParameterNames();

		if (parameters != null) {
			String[] values = new String[parameters.length];

			for (int i = 0; i < parameters.length; i++) {
				String parameter =
					className + StringPool.PERIOD + parameters[i];

				if (parameters[i].contains(StringPool.EQUAL)) {
					String[] parameterPair = StringUtil.split(
						parameters[i], CharPool.EQUAL);

					parameter =
						className + StringPool.PERIOD + parameterPair[0];
				}

				values[i] = ParamUtil.getString(actionRequest, parameter);
			}

			convertProcess.setParameterValues(values);
		}

		try {
			convertProcess.validate();
		}
		catch (ConvertException convertException) {
			SessionErrors.add(
				actionRequest, convertException.getClass(), convertException);

			return null;
		}

		String path = convertProcess.getPath();

		if (path != null) {
			LiferayActionResponse liferayActionResponse =
				(LiferayActionResponse)actionResponse;

			return PortletURLBuilder.createRenderURL(
				liferayActionResponse
			).setMVCRenderCommandName(
				path
			).setWindowState(
				WindowState.MAXIMIZED
			).buildString();
		}

		PortletSession portletSession = actionRequest.getPortletSession();

		MaintenanceUtil.maintain(portletSession.getId(), className);

		Message message = new Message();

		message.setPayload(className);

		_messageBus.sendMessage(DestinationNames.CONVERT_PROCESS, message);

		return null;
	}

	private void _gc() throws Exception {
		Runtime runtime = Runtime.getRuntime();

		runtime.gc();
	}

	private void _runScript(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String language = ParamUtil.getString(actionRequest, "language");
		String output = ParamUtil.getString(actionRequest, "output");
		String script = ParamUtil.getString(actionRequest, "script");

		PortletConfig portletConfig = getPortletConfig(actionRequest);

		Map<String, Object> portletObjects =
			ScriptingHelperUtil.getPortletObjects(
				portletConfig, portletConfig.getPortletContext(), actionRequest,
				actionResponse);

		UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
			new UnsyncByteArrayOutputStream();

		UnsyncPrintWriter unsyncPrintWriter = UnsyncPrintWriterPool.borrow(
			unsyncByteArrayOutputStream);

		portletObjects.put("out", unsyncPrintWriter);

		try {
			SessionMessages.add(actionRequest, "language", language);
			SessionMessages.add(actionRequest, "script", script);
			SessionMessages.add(actionRequest, "output", output);

			_scripting.exec(null, portletObjects, language, script);

			unsyncPrintWriter.flush();

			SessionMessages.add(
				actionRequest, "scriptOutput",
				unsyncByteArrayOutputStream.toString());
		}
		catch (ScriptingException scriptingException) {
			SessionErrors.add(
				actionRequest, ScriptingException.class.getName(),
				scriptingException);

			Log log = SanitizerLogWrapper.allowCRLF(_log);

			log.error(scriptingException.getMessage());
		}
	}

	private void _shutdown(ActionRequest actionRequest) throws Exception {
		if (ShutdownUtil.isInProcess()) {
			ShutdownUtil.cancel();
		}
		else {
			long minutes =
				ParamUtil.getInteger(actionRequest, "minutes") * Time.MINUTE;

			if (minutes <= 0) {
				SessionErrors.add(actionRequest, "shutdownMinutes");
			}
			else {
				String message = ParamUtil.getString(actionRequest, "message");

				ShutdownUtil.shutdown(minutes, message);
			}
		}
	}

	private void _threadDump() throws Exception {
		if (_log.isInfoEnabled()) {
			Log log = SanitizerLogWrapper.allowCRLF(_log);

			log.info(ThreadUtil.threadDump());
		}
		else {
			Class<?> clazz = getClass();

			_log.error(
				"Thread dumps require the log level to be at least INFO for " +
					clazz.getName());
		}
	}

	private void _updateExternalServices(
			ActionRequest actionRequest, PortletPreferences portletPreferences)
		throws Exception {

		boolean imageMagickEnabled = ParamUtil.getBoolean(
			actionRequest, "imageMagickEnabled");
		String imageMagickPath = ParamUtil.getString(
			actionRequest, "imageMagickPath");

		portletPreferences.setValue(
			PropsKeys.IMAGEMAGICK_ENABLED, String.valueOf(imageMagickEnabled));
		portletPreferences.setValue(
			PropsKeys.IMAGEMAGICK_GLOBAL_SEARCH_PATH, imageMagickPath);

		for (String name : ImageMagickResourceLimitConstants.PROPERTY_NAMES) {
			String propertyName = PropsKeys.IMAGEMAGICK_RESOURCE_LIMIT + name;

			portletPreferences.setValue(
				propertyName, ParamUtil.getString(actionRequest, propertyName));
		}

		portletPreferences.store();

		_ghostscript.reset();
		ImageMagickUtil.reset();
	}

	private void _updateLogLevels(ActionRequest actionRequest) {
		Enumeration<String> enumeration = actionRequest.getParameterNames();

		Map<String, String> logLevels = new HashMap<>();

		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();

			if (name.startsWith("logLevel")) {
				logLevels.put(
					name.substring(8),
					ParamUtil.getString(
						actionRequest, name, Level.INFO.toString()));
			}
		}

		_updateLogLevels(logLevels);
	}

	private void _updateLogLevels(Map<String, String> logLevels) {
		for (Map.Entry<String, String> logLevelEntry : logLevels.entrySet()) {
			Log4JUtil.setLevel(
				logLevelEntry.getKey(), logLevelEntry.getValue(), true);
		}

		if (!_clusterExecutor.isEnabled()) {
			return;
		}

		if (_clusterMasterExecutor.isMaster()) {
			ClusterRequest clusterRequest =
				ClusterRequest.createMulticastRequest(
					new MethodHandler(
						_resetLogLevelsMethodKey, Log4JUtil.getPriorities(),
						Log4JUtil.getCustomLogSettings()),
					true);

			clusterRequest.setFireAndForget(true);

			_clusterExecutor.execute(clusterRequest);
		}
		else {
			_clusterMasterExecutor.executeOnMaster(
				new MethodHandler(
					_updateLogLevelsMethodKey, logLevels,
					getOSGiServiceIdentifier()));
		}
	}

	private void _updateMail(
			ActionRequest actionRequest, PortletPreferences portletPreferences)
		throws Exception {

		String advancedProperties = ParamUtil.getString(
			actionRequest, "advancedProperties");
		String pop3Host = ParamUtil.getString(actionRequest, "pop3Host");
		String pop3Password = ParamUtil.getString(
			actionRequest, "pop3Password");
		int pop3Port = ParamUtil.getInteger(actionRequest, "pop3Port");
		boolean pop3Secure = ParamUtil.getBoolean(actionRequest, "pop3Secure");
		String pop3User = ParamUtil.getString(actionRequest, "pop3User");
		String smtpHost = ParamUtil.getString(actionRequest, "smtpHost");
		String smtpPassword = ParamUtil.getString(
			actionRequest, "smtpPassword");
		int smtpPort = ParamUtil.getInteger(actionRequest, "smtpPort");
		boolean smtpSecure = ParamUtil.getBoolean(actionRequest, "smtpSecure");
		boolean smtpStartTLSEnable = ParamUtil.getBoolean(
			actionRequest, "smtpStartTLSEnable");
		String smtpUser = ParamUtil.getString(actionRequest, "smtpUser");

		String storeProtocol = Account.PROTOCOL_POP;

		if (pop3Secure) {
			storeProtocol = Account.PROTOCOL_POPS;
		}

		String transportProtocol = Account.PROTOCOL_SMTP;

		if (smtpSecure) {
			transportProtocol = Account.PROTOCOL_SMTPS;
		}

		portletPreferences.setValue(PropsKeys.MAIL_SESSION_MAIL, "true");
		portletPreferences.setValue(
			PropsKeys.MAIL_SESSION_MAIL_ADVANCED_PROPERTIES,
			advancedProperties);
		portletPreferences.setValue(
			PropsKeys.MAIL_SESSION_MAIL_POP3_HOST, pop3Host);

		if (!pop3Password.equals(Portal.TEMP_OBFUSCATION_VALUE)) {
			portletPreferences.setValue(
				PropsKeys.MAIL_SESSION_MAIL_POP3_PASSWORD, pop3Password);
		}

		portletPreferences.setValue(
			PropsKeys.MAIL_SESSION_MAIL_POP3_PORT, String.valueOf(pop3Port));
		portletPreferences.setValue(
			PropsKeys.MAIL_SESSION_MAIL_POP3_USER, pop3User);
		portletPreferences.setValue(
			PropsKeys.MAIL_SESSION_MAIL_SMTP_HOST, smtpHost);

		if (!smtpPassword.equals(Portal.TEMP_OBFUSCATION_VALUE)) {
			portletPreferences.setValue(
				PropsKeys.MAIL_SESSION_MAIL_SMTP_PASSWORD, smtpPassword);
		}

		portletPreferences.setValue(
			PropsKeys.MAIL_SESSION_MAIL_SMTP_PORT, String.valueOf(smtpPort));
		portletPreferences.setValue(
			PropsKeys.MAIL_SESSION_MAIL_SMTP_STARTTLS_ENABLE,
			String.valueOf(smtpStartTLSEnable));
		portletPreferences.setValue(
			PropsKeys.MAIL_SESSION_MAIL_SMTP_USER, smtpUser);
		portletPreferences.setValue(
			PropsKeys.MAIL_SESSION_MAIL_STORE_PROTOCOL, storeProtocol);
		portletPreferences.setValue(
			PropsKeys.MAIL_SESSION_MAIL_TRANSPORT_PROTOCOL, transportProtocol);

		portletPreferences.store();

		_mailService.clearSession();
	}

	private void _verifyMembershipPolicies() throws Exception {
		OrganizationMembershipPolicy organizationMembershipPolicy =
			_organizationMembershipPolicyFactory.
				getOrganizationMembershipPolicy();

		organizationMembershipPolicy.verifyPolicy();

		RoleMembershipPolicy roleMembershipPolicy =
			_roleMembershipPolicyFactory.getRoleMembershipPolicy();

		roleMembershipPolicy.verifyPolicy();

		SiteMembershipPolicy siteMembershipPolicy =
			_siteMembershipPolicyFactory.getSiteMembershipPolicy();

		siteMembershipPolicy.verifyPolicy();

		UserGroupMembershipPolicy userGroupMembershipPolicy =
			_userGroupMembershipPolicyFactory.getUserGroupMembershipPolicy();

		userGroupMembershipPolicy.verifyPolicy();
	}

	private void _verifyPluginTables() throws Exception {
		_serviceComponentLocalService.verifyDB();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EditServerMVCActionCommand.class);

	private static final Function<InvocationHandler, Layout>
		_proxyProviderFunction = ProxyUtil.getProxyProviderFunction(
			Layout.class, ModelWrapper.class);
	private static final MethodKey _resetLogLevelsMethodKey = new MethodKey(
		EditServerMVCActionCommand.class, "_resetLogLevels", Map.class,
		Map.class);
	private static final MethodKey _updateLogLevelsMethodKey = new MethodKey(
		EditServerMVCActionCommand.class, "_updateLogLevels", Map.class,
		String.class);

	@Reference
	private ClusterExecutor _clusterExecutor;

	@Reference
	private ClusterMasterExecutor _clusterMasterExecutor;

	@Reference
	private DirectServletRegistry _directServletRegistry;

	@Reference
	private Ghostscript _ghostscript;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutRevisionLocalService _layoutRevisionLocalService;

	@Reference
	private MailService _mailService;

	@Reference
	private MessageBus _messageBus;

	@Reference
	private MultiVMPool _multiVMPool;

	@Reference
	private OrganizationMembershipPolicyFactory
		_organizationMembershipPolicyFactory;

	@Reference
	private Portal _portal;

	@Reference
	private PortletPreferencesLocalService _portletPreferencesLocalService;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private RoleMembershipPolicyFactory _roleMembershipPolicyFactory;

	@Reference
	private Scripting _scripting;

	@Reference
	private ServiceComponentLocalService _serviceComponentLocalService;

	@Reference
	private SingleVMPool _singleVMPool;

	@Reference
	private SiteMembershipPolicyFactory _siteMembershipPolicyFactory;

	@Reference
	private UserGroupMembershipPolicyFactory _userGroupMembershipPolicyFactory;

}