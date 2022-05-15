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

package com.liferay.portal.webdav;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.InstancePool;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.webdav.WebDAVException;
import com.liferay.portal.kernel.webdav.WebDAVRequest;
import com.liferay.portal.kernel.webdav.WebDAVStorage;
import com.liferay.portal.kernel.webdav.WebDAVUtil;
import com.liferay.portal.kernel.webdav.methods.Method;
import com.liferay.portal.kernel.webdav.methods.MethodFactory;
import com.liferay.portal.util.PropsValues;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Brian Wing Shun Chan
 * @author Alexander Chow
 * @author Fabio Pezzutto
 */
public class WebDAVServlet extends HttpServlet {

	@Override
	public void service(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		int status = HttpServletResponse.SC_PRECONDITION_FAILED;

		String userAgent = httpServletRequest.getHeader(HttpHeaders.USER_AGENT);

		if (_log.isDebugEnabled()) {
			_log.debug("User agent " + userAgent);
		}

		try {
			if (isIgnoredResource(httpServletRequest)) {
				status = HttpServletResponse.SC_NOT_FOUND;

				return;
			}

			WebDAVStorage storage = getStorage(httpServletRequest);

			if (storage == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Invalid WebDAV path " +
							httpServletRequest.getPathInfo());
				}

				return;
			}

			// Set the path only if it has not already been set. This works if
			// and only if the servlet is not mapped to more than one URL.

			if (storage.getRootPath() == null) {
				storage.setRootPath(getRootPath(httpServletRequest));
			}

			PermissionChecker permissionChecker = null;

			String remoteUser = httpServletRequest.getRemoteUser();

			if (remoteUser != null) {
				PrincipalThreadLocal.setName(remoteUser);

				long userId = GetterUtil.getLong(remoteUser);

				User user = UserLocalServiceUtil.getUserById(userId);

				permissionChecker = PermissionCheckerFactoryUtil.create(user);

				PermissionThreadLocal.setPermissionChecker(permissionChecker);
			}

			// Get the method instance

			MethodFactory methodFactory = storage.getMethodFactory();

			Method method = methodFactory.create(httpServletRequest);

			// Process the method

			try {
				WebDAVRequest webDAVRequest = new WebDAVRequestImpl(
					storage, httpServletRequest, httpServletResponse, userAgent,
					permissionChecker);

				status = method.process(webDAVRequest);
			}
			catch (WebDAVException webDAVException) {
				boolean logError = false;

				Throwable throwable = webDAVException;

				while (throwable != null) {
					if (throwable instanceof PrincipalException) {
						logError = true;
					}

					throwable = throwable.getCause();
				}

				if (logError) {
					_log.error(webDAVException);
				}
				else if (_log.isWarnEnabled()) {
					_log.warn(webDAVException);
				}

				status = HttpServletResponse.SC_PRECONDITION_FAILED;
			}
		}
		catch (Exception exception) {
			_log.error(exception);
		}
		finally {
			httpServletResponse.setStatus(status);

			if (_log.isInfoEnabled()) {
				String xLitmus = GetterUtil.getString(
					httpServletRequest.getHeader("X-Litmus"));

				if (Validator.isNotNull(xLitmus)) {
					xLitmus += " ";
				}

				_log.info(
					StringBundler.concat(
						xLitmus, httpServletRequest.getMethod(), " ",
						httpServletRequest.getRequestURI(), " ", status));
			}
		}
	}

	protected String getRootPath(HttpServletRequest httpServletRequest) {
		String contextPath = HttpComponentsUtil.fixPath(
			PortalUtil.getPathContext(httpServletRequest), false, true);
		String servletPath = HttpComponentsUtil.fixPath(
			httpServletRequest.getServletPath(), false, true);

		return contextPath.concat(servletPath);
	}

	protected WebDAVStorage getStorage(HttpServletRequest httpServletRequest) {
		String pathInfo = WebDAVUtil.stripManualCheckInRequiredPath(
			httpServletRequest.getPathInfo());

		pathInfo = WebDAVUtil.stripOfficeExtension(pathInfo);

		String[] pathArray = WebDAVUtil.getPathArray(pathInfo, true);

		WebDAVStorage storage = null;

		if (pathArray.length == 0) {
			storage = (WebDAVStorage)InstancePool.get(
				CompanyWebDAVStorageImpl.class.getName());
		}
		else if (pathArray.length == 1) {
			storage = (WebDAVStorage)InstancePool.get(
				GroupWebDAVStorageImpl.class.getName());
		}
		else if (pathArray.length >= 2) {
			storage = WebDAVUtil.getStorage(pathArray[1]);
		}

		return storage;
	}

	protected boolean isIgnoredResource(HttpServletRequest httpServletRequest) {
		String[] pathArray = WebDAVUtil.getPathArray(
			httpServletRequest.getPathInfo(), true);

		if (ArrayUtil.isEmpty(pathArray)) {
			return false;
		}

		for (String ignore : PropsValues.WEBDAV_IGNORE) {
			String[] ignoreArray = ignore.split(StringPool.SLASH);

			if (ignoreArray.length > pathArray.length) {
				continue;
			}

			boolean match = true;

			for (int i = 1; i <= ignoreArray.length; i++) {
				if (!pathArray[pathArray.length - i].equals(
						ignoreArray[ignoreArray.length - i])) {

					match = false;

					break;
				}
			}

			if (match) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						StringBundler.concat(
							"Skipping over ", httpServletRequest.getMethod(),
							" ", httpServletRequest.getPathInfo()));
				}

				return true;
			}
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(WebDAVServlet.class);

}