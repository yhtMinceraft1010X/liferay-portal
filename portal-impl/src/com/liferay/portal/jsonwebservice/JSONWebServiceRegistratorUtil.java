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

package com.liferay.portal.jsonwebservice;

import com.liferay.petra.reflect.AnnotationLocator;
import com.liferay.portal.kernel.bean.ClassLoaderBeanHandler;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.jsonwebservice.JSONWebServiceActionsManagerUtil;
import com.liferay.portal.kernel.jsonwebservice.JSONWebServiceMode;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.spring.aop.AopInvocationHandler;
import com.liferay.portal.util.PropsValues;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Igor Spasic
 */
public class JSONWebServiceRegistratorUtil {

	public static void processBean(
		String contextName, String contextPath, Object bean) {

		if (!PropsValues.JSON_WEB_SERVICE_ENABLED) {
			return;
		}

		JSONWebService jsonWebService = AnnotationLocator.locate(
			_getTargetClass(bean), JSONWebService.class);

		if (jsonWebService == null) {
			return;
		}

		try {
			_onJSONWebServiceBean(
				contextName, contextPath, bean, jsonWebService);
		}
		catch (Exception exception) {
			_log.error(exception);
		}
	}

	private static Class<?> _getTargetClass(Object service) {
		while (ProxyUtil.isProxyClass(service.getClass())) {
			InvocationHandler invocationHandler =
				ProxyUtil.getInvocationHandler(service);

			if (invocationHandler instanceof AopInvocationHandler) {
				AopInvocationHandler aopInvocationHandler =
					(AopInvocationHandler)invocationHandler;

				service = aopInvocationHandler.getTarget();
			}
			else if (invocationHandler instanceof ClassLoaderBeanHandler) {
				ClassLoaderBeanHandler classLoaderBeanHandler =
					(ClassLoaderBeanHandler)invocationHandler;

				Object bean = classLoaderBeanHandler.getBean();

				if (bean instanceof ServiceWrapper) {
					ServiceWrapper<?> serviceWrapper = (ServiceWrapper<?>)bean;

					service = serviceWrapper.getWrappedService();
				}
				else {
					service = bean;
				}
			}
			else {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Unable to handle proxy of type " + invocationHandler);
				}

				return null;
			}
		}

		return service.getClass();
	}

	private static void _onJSONWebServiceBean(
			String contextName, String contextPath, Object serviceBean,
			JSONWebService jsonWebService)
		throws Exception {

		JSONWebServiceMode jsonWebServiceMode = JSONWebServiceMode.MANUAL;

		if (jsonWebService != null) {
			jsonWebServiceMode = jsonWebService.mode();
		}

		Method[] serviceMethods = JSONWebServiceScannerUtil.scan(serviceBean);

		for (Method method : serviceMethods) {
			JSONWebService methodJSONWebService = method.getAnnotation(
				JSONWebService.class);

			if (methodJSONWebService == null) {
				if (!jsonWebServiceMode.equals(JSONWebServiceMode.AUTO)) {
					continue;
				}
			}
			else {
				JSONWebServiceMode methodJSONWebServiceMode =
					methodJSONWebService.mode();

				if (methodJSONWebServiceMode.equals(
						JSONWebServiceMode.IGNORE)) {

					continue;
				}
			}

			String httpMethod =
				JSONWebServiceMappingResolverUtil.resolveHttpMethod(method);

			if (!JSONWebServiceNamingUtil.isValidHttpMethod(httpMethod)) {
				continue;
			}

			Class<?> serviceBeanClass = method.getDeclaringClass();

			String path = JSONWebServiceMappingResolverUtil.resolvePath(
				serviceBeanClass, method);

			if (!JSONWebServiceNamingUtil.isIncludedPath(contextPath, path)) {
				continue;
			}

			if (JSONWebServiceNamingUtil.isIncludedMethod(method)) {
				JSONWebServiceActionsManagerUtil.registerJSONWebServiceAction(
					contextName, contextPath, serviceBean, serviceBeanClass,
					method, path, httpMethod);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JSONWebServiceRegistratorUtil.class);

}