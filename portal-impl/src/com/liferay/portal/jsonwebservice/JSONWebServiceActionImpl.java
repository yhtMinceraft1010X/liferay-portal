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
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.jsonwebservice.JSONWebServiceAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CamelCaseUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MethodParameter;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.util.PropsUtil;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import jodd.bean.BeanCopy;
import jodd.bean.BeanUtil;

import jodd.time.TimeUtil;

import jodd.typeconverter.TypeConversionException;
import jodd.typeconverter.TypeConverter;
import jodd.typeconverter.TypeConverterManager;

import jodd.util.ClassUtil;
import jodd.util.StringUtil;

import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Igor Spasic
 */
public class JSONWebServiceActionImpl implements JSONWebServiceAction {

	public JSONWebServiceActionImpl(
		JSONWebServiceActionConfig jsonWebServiceActionConfig,
		JSONWebServiceActionParameters jsonWebServiceActionParameters) {

		_jsonWebServiceActionConfig = jsonWebServiceActionConfig;
		_jsonWebServiceActionParameters = jsonWebServiceActionParameters;
	}

	@Override
	public Object invoke() throws Exception {
		JSONRPCRequest jsonRPCRequest =
			_jsonWebServiceActionParameters.getJSONRPCRequest();

		if (jsonRPCRequest == null) {
			return _invokeActionMethod();
		}

		Object result = null;
		Exception exception1 = null;

		try {
			result = _invokeActionMethod();
		}
		catch (Exception exception2) {
			exception1 = exception2;

			_log.error(exception2);
		}

		return new JSONRPCResponse(jsonRPCRequest, result, exception1);
	}

	private static ServiceTracker<Object, Object> _getServiceTracker() {
		ServiceTracker<Object, Object> serviceTracker = new ServiceTracker<>(
			SystemBundleUtil.getBundleContext(),
			SystemBundleUtil.createFilter(
				StringBundler.concat(
					"(",
					PropsKeys.
						JSONWS_WEB_SERVICE_PARAMETER_TYPE_WHITELIST_CLASS_NAMES,
					"=*)")),
			null);

		serviceTracker.open();

		return serviceTracker;
	}

	private void _checkTypeIsAssignable(
		int argumentPos, Class<?> targetClass, Class<?> parameterType) {

		String parameterTypeName = parameterType.getName();

		if (parameterTypeName.contains("com.liferay") &&
			parameterTypeName.contains("Util")) {

			throw new IllegalArgumentException(
				"Not instantiating " + parameterTypeName);
		}

		if (Objects.equals(targetClass, parameterType)) {
			return;
		}

		if (!ClassUtil.isTypeOf(parameterType, targetClass)) {
			throw new IllegalArgumentException(
				StringBundler.concat(
					"Unmatched argument type ", parameterTypeName,
					" for method argument ", argumentPos));
		}

		if (parameterType.isPrimitive() ||
			parameterTypeName.equals(
				JSONWebServiceNamingUtil.convertModelClassToImplClassName(
					targetClass)) ||
			ArrayUtil.contains(
				_JSONWS_WEB_SERVICE_PARAMETER_TYPE_WHITELIST_CLASS_NAMES,
				parameterTypeName)) {

			return;
		}

		ServiceReference<Object>[] serviceReferences =
			_serviceTracker.getServiceReferences();

		if (serviceReferences != null) {
			String key =
				PropsKeys.
					JSONWS_WEB_SERVICE_PARAMETER_TYPE_WHITELIST_CLASS_NAMES;

			for (ServiceReference<Object> serviceReference :
					serviceReferences) {

				List<String> whitelistedClassNames =
					com.liferay.portal.kernel.util.StringUtil.asList(
						serviceReference.getProperty(key));

				if (whitelistedClassNames.contains(parameterTypeName)) {
					return;
				}
			}
		}

		throw new TypeConversionException(
			parameterTypeName + " is not allowed to be instantiated");
	}

	private Object _convertListToArray(List<?> list, Class<?> componentType) {
		Object array = Array.newInstance(componentType, list.size());

		for (int i = 0; i < list.size(); i++) {
			Object entry = list.get(i);

			if (entry != null) {
				entry = _convertType(entry, componentType);
			}

			Array.set(array, i, entry);
		}

		return array;
	}

	private Object _convertType(Object inputObject, Class<?> targetType) {
		if (targetType == null) {
			return inputObject;
		}

		TypeConverterManager typeConverterManager = TypeConverterManager.get();

		Object outputObject = null;

		try {
			outputObject = typeConverterManager.convertType(
				inputObject, targetType);
		}
		catch (TypeConversionException typeConversionException) {
			if (inputObject instanceof Map) {
				try {
					if (targetType.isInterface()) {
						Class<?> clazz = getClass();

						ClassLoader classLoader = clazz.getClassLoader();

						String modelClassName =
							JSONWebServiceNamingUtil.
								convertModelClassToImplClassName(targetType);

						try {
							targetType = classLoader.loadClass(modelClassName);
						}
						catch (ClassNotFoundException classNotFoundException) {
							if (_log.isDebugEnabled()) {
								_log.debug(classNotFoundException);
							}

							Class<?> actionClass =
								_jsonWebServiceActionConfig.getActionClass();

							classLoader = actionClass.getClassLoader();

							targetType = classLoader.loadClass(modelClassName);
						}
					}

					outputObject = targetType.newInstance();

					BeanCopy beanCopy = new BeanCopy(inputObject, outputObject);

					beanCopy.copy();

					return outputObject;
				}
				catch (Exception exception) {
					throw new TypeConversionException(exception);
				}
			}

			throw typeConversionException;
		}

		return outputObject;
	}

	private Object _convertValueToParameterValue(
		Object value, Class<?> parameterType,
		Class<?>[] genericParameterTypes) {

		if (parameterType.isArray()) {
			if (parameterType.isInstance(value)) {
				return value;
			}

			if (value instanceof File) {
				try {
					return FileUtil.getBytes((File)value);
				}
				catch (IOException ioException) {
					_log.error(ioException);

					return null;
				}
			}

			List<?> list = null;

			if (value instanceof List) {
				list = (List<?>)value;
			}
			else {
				String valueString = value.toString();

				valueString = valueString.trim();

				if (!valueString.startsWith(StringPool.OPEN_BRACKET)) {
					valueString = StringBundler.concat(
						StringPool.OPEN_BRACKET, valueString,
						StringPool.CLOSE_BRACKET);
				}

				list = JSONFactoryUtil.looseDeserialize(
					valueString, ArrayList.class);
			}

			return _convertListToArray(list, parameterType.getComponentType());
		}
		else if (Enum.class.isAssignableFrom(parameterType)) {
			return Enum.valueOf((Class<Enum>)parameterType, value.toString());
		}
		else if (parameterType.equals(Calendar.class)) {
			Calendar calendar = Calendar.getInstance();

			calendar.setLenient(false);

			String valueString = value.toString();

			valueString = valueString.trim();

			calendar.setTimeInMillis(GetterUtil.getLong(valueString));

			return calendar;
		}
		else if (Collection.class.isAssignableFrom(parameterType)) {
			List<?> list = null;

			if (value instanceof List) {
				list = (List<?>)value;
			}
			else {
				String valueString = value.toString();

				valueString = valueString.trim();

				if (!valueString.startsWith(StringPool.OPEN_BRACKET)) {
					valueString = StringBundler.concat(
						StringPool.OPEN_BRACKET, valueString,
						StringPool.CLOSE_BRACKET);
				}

				list = JSONFactoryUtil.looseDeserialize(
					valueString, ArrayList.class);
			}

			return _generifyList(list, genericParameterTypes);
		}
		else if (parameterType.equals(Locale.class)) {
			String valueString = value.toString();

			valueString = valueString.trim();

			return LocaleUtil.fromLanguageId(valueString);
		}
		else if (parameterType.equals(Map.class)) {
			Map<?, ?> map = null;

			if (value instanceof Map) {
				map = (Map<Object, Object>)value;
			}
			else {
				String valueString = value.toString();

				valueString = valueString.trim();

				map = JSONFactoryUtil.looseDeserialize(
					valueString, HashMap.class);
			}

			return _generifyMap(map, genericParameterTypes);
		}
		else {
			Object parameterValue = null;

			try {
				parameterValue = _convertType(value, parameterType);
			}
			catch (Exception exception1) {
				if (value instanceof Map) {
					try {
						parameterValue = _createDefaultParameterValue(
							null, parameterType);
					}
					catch (Exception exception2) {
						ClassCastException classCastException =
							new ClassCastException(exception1.getMessage());

						classCastException.addSuppressed(exception2);

						throw classCastException;
					}

					BeanCopy beanCopy = new BeanCopy(value, parameterValue);

					beanCopy.copy();
				}
				else {
					String valueString = value.toString();

					valueString = valueString.trim();

					if (!valueString.startsWith(StringPool.OPEN_CURLY_BRACE)) {
						throw new ClassCastException(exception1.getMessage());
					}

					parameterValue = JSONFactoryUtil.looseDeserialize(
						valueString, parameterType);
				}
			}

			return parameterValue;
		}
	}

	private Object _createDefaultParameterValue(
			String parameterName, Class<?> parameterType)
		throws Exception {

		if ((parameterName != null) && parameterName.equals("serviceContext") &&
			parameterType.equals(ServiceContext.class)) {

			ServiceContext serviceContext =
				_jsonWebServiceActionParameters.getServiceContext();

			if (serviceContext == null) {
				serviceContext = new ServiceContext();
			}

			return serviceContext;
		}

		return parameterType.newInstance();
	}

	private List<?> _generifyList(List<?> list, Class<?>[] types) {
		if ((types == null) || (types.length != 1)) {
			return list;
		}

		List<Object> newList = new ArrayList<>(list.size());

		for (Object entry : list) {
			if (entry != null) {
				entry = _convertType(entry, types[0]);
			}

			newList.add(entry);
		}

		return newList;
	}

	private Map<?, ?> _generifyMap(Map<?, ?> map, Class<?>[] types) {
		if ((types == null) || (types.length != 2)) {
			return map;
		}

		Map<Object, Object> newMap = new HashMap<>();

		for (Map.Entry<?, ?> entry : map.entrySet()) {
			Object key = _convertType(entry.getKey(), types[0]);

			Object value = entry.getValue();

			if (value != null) {
				value = _convertType(value, types[1]);
			}

			newMap.put(key, value);
		}

		return newMap;
	}

	private void _injectInnerParametersIntoValue(
		String parameterName, Object parameterValue) {

		if (parameterValue == null) {
			return;
		}

		List<Map.Entry<String, Object>> innerParameters =
			_jsonWebServiceActionParameters.getInnerParameters(parameterName);

		if (innerParameters == null) {
			return;
		}

		for (Map.Entry<String, Object> innerParameter : innerParameters) {
			try {
				BeanUtil.pojo.setProperty(
					parameterValue, innerParameter.getKey(),
					innerParameter.getValue());
			}
			catch (Exception exception) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						StringBundler.concat(
							"Unable to set inner parameter ", parameterName,
							".", innerParameter.getKey()),
						exception);
				}
			}
		}
	}

	private Object _invokeActionMethod() throws Exception {
		Method actionMethod = _jsonWebServiceActionConfig.getActionMethod();

		Object[] parameters = _prepareParameters(
			_jsonWebServiceActionConfig.getActionClass());

		if (_jsonWebServiceActionConfig.isDeprecated() &&
			_log.isWarnEnabled()) {

			_log.warn("Invoking deprecated method " + actionMethod.getName());
		}

		return actionMethod.invoke(
			_jsonWebServiceActionConfig.getActionObject(), parameters);
	}

	private Object[] _prepareParameters(Class<?> actionClass) throws Exception {
		MethodParameter[] methodParameters =
			_jsonWebServiceActionConfig.getMethodParameters();

		Object[] parameters = new Object[methodParameters.length];

		for (int i = 0; i < methodParameters.length; i++) {
			String parameterName = methodParameters[i].getName();

			parameterName = CamelCaseUtil.normalizeCamelCase(parameterName);

			Object value = _jsonWebServiceActionParameters.getParameter(
				parameterName);

			Object parameterValue = null;

			if (value != null) {
				Class<?> targetClass = methodParameters[i].getType();

				Class<?> parameterType = targetClass;

				String parameterTypeName =
					_jsonWebServiceActionParameters.getParameterTypeName(
						parameterName);

				if (parameterTypeName != null) {
					ClassLoader classLoader = actionClass.getClassLoader();

					parameterType = classLoader.loadClass(parameterTypeName);

					_checkTypeIsAssignable(i, targetClass, parameterType);
				}

				if (value.equals(Void.TYPE)) {
					parameterValue = _createDefaultParameterValue(
						parameterName, parameterType);
				}
				else {
					parameterValue = _convertValueToParameterValue(
						value, parameterType,
						methodParameters[i].getGenericTypes());

					ServiceContext serviceContext =
						_jsonWebServiceActionParameters.getServiceContext();

					if ((serviceContext != null) &&
						parameterName.equals("serviceContext")) {

						if ((parameterValue != null) &&
							(parameterValue instanceof ServiceContext)) {

							serviceContext.merge(
								(ServiceContext)parameterValue);
						}

						parameterValue = serviceContext;
					}
				}
			}

			_injectInnerParametersIntoValue(parameterName, parameterValue);

			parameters[i] = parameterValue;
		}

		return parameters;
	}

	private static final String[]
		_JSONWS_WEB_SERVICE_PARAMETER_TYPE_WHITELIST_CLASS_NAMES =
			PropsUtil.getArray(
				PropsKeys.
					JSONWS_WEB_SERVICE_PARAMETER_TYPE_WHITELIST_CLASS_NAMES);

	private static final Log _log = LogFactoryUtil.getLog(
		JSONWebServiceActionImpl.class);

	private static final ServiceTracker<Object, Object> _serviceTracker =
		_getServiceTracker();

	private final JSONWebServiceActionConfig _jsonWebServiceActionConfig;
	private final JSONWebServiceActionParameters
		_jsonWebServiceActionParameters;

	private static class DateTypeConverter implements TypeConverter<Date> {

		@Override
		public Date convert(Object object) {
			if (object == null) {
				return null;
			}

			if (object instanceof Calendar) {
				Calendar calendar = (Calendar)object;

				return new Date(calendar.getTimeInMillis());
			}

			if (object instanceof Date) {
				return (Date)object;
			}

			if (object instanceof LocalDate) {
				return TimeUtil.toDate((LocalDate)object);
			}

			if (object instanceof LocalDateTime) {
				return TimeUtil.toDate((LocalDateTime)object);
			}

			if (object instanceof Number) {
				Number number = (Number)object;

				return new Date(number.longValue());
			}

			String stringValue = object.toString();

			stringValue = stringValue.trim();

			if (!StringUtil.containsOnlyDigits(stringValue)) {
				TemporalAccessor temporalAccessor =
					_dateTimeFormatter.parseBest(
						stringValue, ZonedDateTime::from, LocalDateTime::from,
						LocalDate::from);

				if (temporalAccessor instanceof LocalDate) {
					return TimeUtil.toDate((LocalDate)temporalAccessor);
				}

				if (temporalAccessor instanceof LocalDateTime) {
					return TimeUtil.toDate((LocalDateTime)temporalAccessor);
				}

				if (temporalAccessor instanceof ZonedDateTime) {
					ZonedDateTime zonedDateTime =
						(ZonedDateTime)temporalAccessor;

					return Date.from(zonedDateTime.toInstant());
				}

				throw new TypeConversionException(object);
			}

			try {
				return new Date(Long.parseLong(stringValue));
			}
			catch (NumberFormatException numberFormatException) {
				throw new TypeConversionException(
					object, numberFormatException);
			}
		}

		// May 1 is "5-1" with "M-d" while "05-01" with "MM-dd". See
		// java.time.format.DateTimeFormatterBuilder#appendPattern(String).

		private static final DateTimeFormatter _dateTimeFormatter =
			new DateTimeFormatterBuilder().parseCaseInsensitive(
			).appendPattern(
				"yyyy-[MM][M]-[dd][d]"
			).optionalStart(
			).optionalStart(
			).appendLiteral(
				' '
			).optionalEnd(
			).optionalStart(
			).appendLiteral(
				'T'
			).optionalEnd(
			).appendOptional(
				DateTimeFormatter.ISO_TIME
			).toFormatter();

	}

	private static class LocaleTypeConverter implements TypeConverter<Locale> {

		@Override
		public Locale convert(Object object) {
			if (object == null) {
				return null;
			}

			if (object instanceof Locale) {
				return (Locale)object;
			}

			return LocaleUtil.fromLanguageId(String.valueOf(object), false);
		}

	}

	static {
		TypeConverterManager typeConverterManager = TypeConverterManager.get();

		typeConverterManager.register(Date.class, new DateTypeConverter());
		typeConverterManager.register(Locale.class, new LocaleTypeConverter());
	}

}