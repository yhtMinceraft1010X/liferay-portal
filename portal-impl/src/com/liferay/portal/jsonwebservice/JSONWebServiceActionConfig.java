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

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.jsonwebservice.JSONWebServiceActionMapping;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MethodParameter;
import com.liferay.portal.kernel.util.MethodParametersResolverUtil;
import com.liferay.portal.kernel.util.Validator;

import java.lang.reflect.Method;

import java.util.Objects;

/**
 * @author Igor Spasic
 * @author Raymond Augé
 */
public class JSONWebServiceActionConfig
	implements Comparable<JSONWebServiceActionConfig>,
			   JSONWebServiceActionMapping {

	public JSONWebServiceActionConfig(
		String contextName, String contextPath, Object actionObject,
		Class<?> actionClass, Method actionMethod, String path, String method) {

		_contextName = GetterUtil.getString(contextName);
		_contextPath = GetterUtil.getString(contextPath);
		_actionObject = actionObject;
		_actionClass = actionClass;

		Method newActionMethod = actionMethod;

		if (actionObject != null) {
			try {
				Class<?> actionObjectClass = actionObject.getClass();

				newActionMethod = actionObjectClass.getMethod(
					actionMethod.getName(), actionMethod.getParameterTypes());
			}
			catch (NoSuchMethodException noSuchMethodException) {
				throw new IllegalArgumentException(noSuchMethodException);
			}
		}

		_actionMethod = newActionMethod;

		if (Validator.isNotNull(_contextName)) {
			path = StringBundler.concat(
				StringPool.SLASH, _contextName, StringPool.PERIOD,
				path.substring(1));
		}

		_path = path;

		_method = method;

		Deprecated deprecated = actionMethod.getAnnotation(Deprecated.class);

		if (deprecated != null) {
			_deprecated = true;
		}
		else {
			_deprecated = false;
		}

		Method realActionMethod = null;

		try {
			realActionMethod = _actionClass.getDeclaredMethod(
				actionMethod.getName(), actionMethod.getParameterTypes());
		}
		catch (NoSuchMethodException noSuchMethodException) {
			if (_log.isDebugEnabled()) {
				_log.debug(noSuchMethodException);
			}
		}

		_realActionMethod = realActionMethod;

		Class<?>[] parameterTypes = _actionMethod.getParameterTypes();

		StringBundler sb = new StringBundler((parameterTypes.length * 2) + 3);

		sb.append(_path);
		sb.append(StringPool.MINUS);
		sb.append(parameterTypes.length);

		for (Class<?> parameterType : parameterTypes) {
			sb.append(StringPool.MINUS);
			sb.append(parameterType.getName());
		}

		_signature = sb.toString();
	}

	@Override
	public int compareTo(
		JSONWebServiceActionConfig jsonWebServiceActionConfig) {

		return _signature.compareTo(jsonWebServiceActionConfig._signature);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof JSONWebServiceActionConfig)) {
			return false;
		}

		JSONWebServiceActionConfig jsonWebServiceActionConfig =
			(JSONWebServiceActionConfig)object;

		if (Objects.equals(_signature, jsonWebServiceActionConfig._signature)) {
			return true;
		}

		return false;
	}

	@Override
	public Class<?> getActionClass() {
		return _actionClass;
	}

	@Override
	public Method getActionMethod() {
		return _actionMethod;
	}

	@Override
	public Object getActionObject() {
		return _actionObject;
	}

	@Override
	public String getContextName() {
		return _contextName;
	}

	@Override
	public String getContextPath() {
		return _contextPath;
	}

	@Override
	public String getMethod() {
		return _method;
	}

	@Override
	public MethodParameter[] getMethodParameters() {
		if (_realActionMethod == null) {
			return new MethodParameter[0];
		}

		return MethodParametersResolverUtil.resolveMethodParameters(
			_realActionMethod);
	}

	@Override
	public String getPath() {
		return _path;
	}

	@Override
	public Method getRealActionMethod() {
		return _realActionMethod;
	}

	@Override
	public String getSignature() {
		return _signature;
	}

	@Override
	public int hashCode() {
		return _signature.hashCode();
	}

	@Override
	public boolean isDeprecated() {
		return _deprecated;
	}

	@Override
	public String toString() {
		return StringBundler.concat(
			"{actionClass=", _actionClass, ", actionMethod=", _actionMethod,
			", contextName=", _contextName, ", contextPath=", _contextPath,
			", deprecated=", _deprecated, ", method=", _method, ", path=",
			_path, ", realActionMethod=", _realActionMethod, ", signature=",
			_signature, "}");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JSONWebServiceActionConfig.class);

	private final Class<?> _actionClass;
	private final Method _actionMethod;
	private final Object _actionObject;
	private final String _contextName;
	private final String _contextPath;
	private final boolean _deprecated;
	private final String _method;
	private final String _path;
	private final Method _realActionMethod;
	private final String _signature;

}