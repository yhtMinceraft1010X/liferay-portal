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

package com.liferay.object.service.http;

import com.liferay.object.service.ObjectActionServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

/**
 * Provides the HTTP utility for the
 * <code>ObjectActionServiceUtil</code> service
 * utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * <code>HttpPrincipal</code> parameter.
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for
 * tunneling without the cost of serializing to text. The drawback is that it
 * only works with Java.
 * </p>
 *
 * <p>
 * Set the property <b>tunnel.servlet.hosts.allowed</b> in portal.properties to
 * configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Marco Leo
 * @generated
 */
public class ObjectActionServiceHttp {

	public static com.liferay.object.model.ObjectAction addObjectAction(
			HttpPrincipal httpPrincipal, long objectDefinitionId,
			boolean active, String description, String name,
			String objectActionExecutorKey, String objectActionTriggerKey,
			com.liferay.portal.kernel.util.UnicodeProperties
				parametersUnicodeProperties)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				ObjectActionServiceUtil.class, "addObjectAction",
				_addObjectActionParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, objectDefinitionId, active, description, name,
				objectActionExecutorKey, objectActionTriggerKey,
				parametersUnicodeProperties);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.object.model.ObjectAction)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.object.model.ObjectAction deleteObjectAction(
			HttpPrincipal httpPrincipal, long objectActionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				ObjectActionServiceUtil.class, "deleteObjectAction",
				_deleteObjectActionParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, objectActionId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.object.model.ObjectAction)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.object.model.ObjectAction getObjectAction(
			HttpPrincipal httpPrincipal, long objectActionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				ObjectActionServiceUtil.class, "getObjectAction",
				_getObjectActionParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, objectActionId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.object.model.ObjectAction)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.object.model.ObjectAction updateObjectAction(
			HttpPrincipal httpPrincipal, long objectActionId, boolean active,
			String description, String name,
			com.liferay.portal.kernel.util.UnicodeProperties
				parametersUnicodeProperties)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				ObjectActionServiceUtil.class, "updateObjectAction",
				_updateObjectActionParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, objectActionId, active, description, name,
				parametersUnicodeProperties);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.object.model.ObjectAction)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		ObjectActionServiceHttp.class);

	private static final Class<?>[] _addObjectActionParameterTypes0 =
		new Class[] {
			long.class, boolean.class, String.class, String.class, String.class,
			String.class, com.liferay.portal.kernel.util.UnicodeProperties.class
		};
	private static final Class<?>[] _deleteObjectActionParameterTypes1 =
		new Class[] {long.class};
	private static final Class<?>[] _getObjectActionParameterTypes2 =
		new Class[] {long.class};
	private static final Class<?>[] _updateObjectActionParameterTypes3 =
		new Class[] {
			long.class, boolean.class, String.class, String.class,
			com.liferay.portal.kernel.util.UnicodeProperties.class
		};

}