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

package com.liferay.portal.kernel.util;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.lang.reflect.Constructor;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 */
public class MapUtil {

	public static <K, V> void copy(
		Map<? extends K, ? extends V> master, Map<? super K, ? super V> copy) {

		copy.clear();

		merge(master, copy);
	}

	public static <T> Map<T, T> fromArray(T... array) {
		if ((array.length % 2) != 0) {
			throw new IllegalArgumentException(
				"Array length is not an even number");
		}

		Map<T, T> map = new HashMap<>();

		for (int i = 0; i < array.length; i += 2) {
			T key = array[i];
			T value = array[i + 1];

			map.put(key, value);
		}

		return map;
	}

	public static boolean getBoolean(Map<String, ?> map, String key) {
		return getBoolean(map, key, GetterUtil.DEFAULT_BOOLEAN);
	}

	public static boolean getBoolean(
		Map<String, ?> map, String key, boolean defaultValue) {

		Object value = map.get(key);

		if (value == null) {
			return defaultValue;
		}

		if (value instanceof Boolean) {
			return (Boolean)value;
		}

		if (value instanceof String[]) {
			String[] array = (String[])value;

			if (array.length == 0) {
				return defaultValue;
			}

			return GetterUtil.getBoolean(array[0], defaultValue);
		}

		return GetterUtil.getBoolean(String.valueOf(value), defaultValue);
	}

	public static double getDouble(Map<String, ?> map, String key) {
		return getDouble(map, key, GetterUtil.DEFAULT_DOUBLE);
	}

	public static double getDouble(
		Map<String, ?> map, String key, double defaultValue) {

		Object value = map.get(key);

		if (value == null) {
			return defaultValue;
		}

		if (value instanceof Double) {
			return (Double)value;
		}

		if (value instanceof String[]) {
			String[] array = (String[])value;

			if (array.length == 0) {
				return defaultValue;
			}

			return GetterUtil.getDouble(array[0], defaultValue);
		}

		return GetterUtil.getDouble(String.valueOf(value), defaultValue);
	}

	public static int getInteger(Map<String, ?> map, String key) {
		return getInteger(map, key, GetterUtil.DEFAULT_INTEGER);
	}

	public static int getInteger(
		Map<String, ?> map, String key, int defaultValue) {

		Object value = map.get(key);

		if (value == null) {
			return defaultValue;
		}

		if (value instanceof Integer) {
			return (Integer)value;
		}

		if (value instanceof String[]) {
			String[] array = (String[])value;

			if (array.length == 0) {
				return defaultValue;
			}

			return GetterUtil.getInteger(array[0], defaultValue);
		}

		return GetterUtil.getInteger(String.valueOf(value), defaultValue);
	}

	public static long getLong(Map<Long, Long> map, long key) {
		return getLong(map, key, GetterUtil.DEFAULT_LONG);
	}

	public static long getLong(
		Map<Long, Long> map, long key, long defaultValue) {

		Long value = map.get(key);

		if (value == null) {
			return defaultValue;
		}

		return value;
	}

	public static long getLong(Map<String, ?> map, String key) {
		return getLong(map, key, GetterUtil.DEFAULT_LONG);
	}

	public static long getLong(
		Map<String, ?> map, String key, long defaultValue) {

		Object value = map.get(key);

		if (value == null) {
			return defaultValue;
		}

		if (value instanceof Long) {
			return (Long)value;
		}

		if (value instanceof String[]) {
			String[] array = (String[])value;

			if (array.length == 0) {
				return defaultValue;
			}

			return GetterUtil.getLong(array[0], defaultValue);
		}

		return GetterUtil.getLong(String.valueOf(value), defaultValue);
	}

	public static short getShort(Map<String, ?> map, String key) {
		return getShort(map, key, GetterUtil.DEFAULT_SHORT);
	}

	public static short getShort(
		Map<String, ?> map, String key, short defaultValue) {

		Object value = map.get(key);

		if (value == null) {
			return defaultValue;
		}

		if (value instanceof Short) {
			return (Short)value;
		}

		if (value instanceof String[]) {
			String[] array = (String[])value;

			if (array.length == 0) {
				return defaultValue;
			}

			return GetterUtil.getShort(array[0], defaultValue);
		}

		return GetterUtil.getShort(String.valueOf(value), defaultValue);
	}

	public static String getString(Map<String, ?> map, String key) {
		return getString(map, key, GetterUtil.DEFAULT_STRING);
	}

	public static String getString(
		Map<String, ?> map, String key, String defaultValue) {

		Object value = map.get(key);

		if (value == null) {
			return defaultValue;
		}

		if (value instanceof String) {
			return GetterUtil.getString((String)value, defaultValue);
		}

		if (value instanceof String[]) {
			String[] array = (String[])value;

			if (array.length == 0) {
				return defaultValue;
			}

			return GetterUtil.getString(array[0], defaultValue);
		}

		return GetterUtil.getString(String.valueOf(value), defaultValue);
	}

	public static <K, V> V getWithFallbackKey(
		Map<K, V> map, K key, K fallbackKey) {

		if (map == null) {
			return null;
		}

		V value = map.get(key);

		if ((value == null) ||
			((value instanceof String) && Validator.isBlank((String)value))) {

			return map.get(fallbackKey);
		}

		return value;
	}

	public static boolean isEmpty(Map<?, ?> map) {
		if ((map == null) || map.isEmpty()) {
			return true;
		}

		return false;
	}

	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	public static <K, V> void isNotEmptyForEach(
		Map<? extends K, ? extends V> map,
		BiConsumer<? super K, ? super V> biConsumer) {

		if (!isEmpty(map)) {
			map.forEach(biConsumer);
		}
	}

	public static <K, V> void merge(
		Map<? extends K, ? extends V> master, Map<? super K, ? super V> copy) {

		copy.putAll(master);
	}

	public static <K, V> Dictionary<K, V> singletonDictionary(K key, V value) {
		return new SingletonDictionary<>(key, value);
	}

	public static <T> LinkedHashMap<String, T> toLinkedHashMap(
		String[] params) {

		return toLinkedHashMap(params, StringPool.COLON);
	}

	public static <T> LinkedHashMap<String, T> toLinkedHashMap(
		String[] params, String delimiter) {

		LinkedHashMap<String, Object> map = new LinkedHashMap<>();

		if (params == null) {
			return (LinkedHashMap<String, T>)map;
		}

		for (String param : params) {
			String[] kvp = StringUtil.split(param, delimiter);

			if (kvp.length == 2) {
				map.put(kvp[0], kvp[1]);
			}
			else if (kvp.length == 3) {
				String type = kvp[2];

				if (StringUtil.equalsIgnoreCase(type, "boolean") ||
					type.equals(Boolean.class.getName())) {

					map.put(kvp[0], Boolean.valueOf(kvp[1]));
				}
				else if (StringUtil.equalsIgnoreCase(type, "double") ||
						 type.equals(Double.class.getName())) {

					map.put(kvp[0], Double.valueOf(kvp[1]));
				}
				else if (StringUtil.equalsIgnoreCase(type, "int") ||
						 type.equals(Integer.class.getName())) {

					map.put(kvp[0], Integer.valueOf(kvp[1]));
				}
				else if (StringUtil.equalsIgnoreCase(type, "long") ||
						 type.equals(Long.class.getName())) {

					map.put(kvp[0], Long.valueOf(kvp[1]));
				}
				else if (StringUtil.equalsIgnoreCase(type, "short") ||
						 type.equals(Short.class.getName())) {

					map.put(kvp[0], Short.valueOf(kvp[1]));
				}
				else if (type.equals(String.class.getName())) {
					map.put(kvp[0], kvp[1]);
				}
				else {
					try {
						Class<?> clazz = Class.forName(type);

						Constructor<?> constructor = clazz.getConstructor(
							String.class);

						map.put(kvp[0], constructor.newInstance(kvp[1]));
					}
					catch (Exception exception) {
						_log.error(exception);
					}
				}
			}
		}

		return (LinkedHashMap<String, T>)map;
	}

	public static String toString(Map<?, ?> map) {
		return toString(map, null, null);
	}

	public static String toString(
		Map<?, ?> map, String hideIncludesRegex, String hideExcludesRegex) {

		if (isEmpty(map)) {
			return StringPool.OPEN_CURLY_BRACE + StringPool.CLOSE_CURLY_BRACE;
		}

		StringBundler sb = new StringBundler((map.size() * 4) + 1);

		sb.append(StringPool.OPEN_CURLY_BRACE);

		for (Map.Entry<?, ?> entry : map.entrySet()) {
			Object value = entry.getValue();

			String keyString = String.valueOf(entry.getKey());

			if ((hideIncludesRegex != null) &&
				!keyString.matches(hideIncludesRegex)) {

				value = "********";
			}

			if ((hideExcludesRegex != null) &&
				keyString.matches(hideExcludesRegex)) {

				value = "********";
			}

			sb.append(keyString);
			sb.append(StringPool.EQUAL);

			if (value instanceof Map<?, ?>) {
				sb.append(toString((Map<?, ?>)value));
			}
			else if (value instanceof String[]) {
				String valueString = StringUtil.merge(
					(String[])value, StringPool.COMMA_AND_SPACE);

				sb.append(
					StringBundler.concat(
						StringPool.OPEN_BRACKET, valueString,
						StringPool.CLOSE_BRACKET));
			}
			else {
				sb.append(value);
			}

			sb.append(StringPool.COMMA_AND_SPACE);
		}

		sb.setStringAt(StringPool.CLOSE_CURLY_BRACE, sb.index() - 1);

		return sb.toString();
	}

	private static final Log _log = LogFactoryUtil.getLog(MapUtil.class);

	private static class SingletonDictionary<K, V> extends Dictionary<K, V> {

		@Override
		public Enumeration<V> elements() {
			return Collections.enumeration(Collections.singleton(_value));
		}

		@Override
		public V get(Object key) {
			if (Objects.equals(_key, key)) {
				return _value;
			}

			return null;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public Enumeration<K> keys() {
			return Collections.enumeration(Collections.singleton(_key));
		}

		@Override
		public V put(K key, V value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public V remove(Object key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int size() {
			return 1;
		}

		@Override
		public String toString() {
			return StringBundler.concat(
				StringPool.OPEN_CURLY_BRACE, _key, StringPool.EQUAL, _value,
				StringPool.CLOSE_CURLY_BRACE);
		}

		private SingletonDictionary(K key, V value) {
			_key = key;
			_value = value;
		}

		private final K _key;
		private final V _value;

	}

}