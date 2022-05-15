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

package com.liferay.portal.configuration;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.SystemProperties;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

/**
 * @author Shuyang Zhou
 */
public class ClassLoaderAggregatePropertiesUtil {

	public static ClassLoaderAggregateProperties create(
		ClassLoader classLoader, String companyId, String componentName) {

		SystemProperties.set("base.path", ".");

		ClassLoaderAggregateProperties classLoaderAggregateProperties =
			new ClassLoaderAggregateProperties(
				classLoader, companyId, componentName);

		if (_log.isInfoEnabled()) {
			_log.info(
				StringBundler.concat(
					"Properties for ", componentName, " loaded from ",
					classLoaderAggregateProperties.loadedSources()));
		}

		_loadEnvOverrides(classLoaderAggregateProperties);

		return classLoaderAggregateProperties;
	}

	private static String _decode(String s) {
		int index = -1;
		int openUnderLine = -1;
		int position = 0;
		StringBundler sb = new StringBundler();

		while ((index = s.indexOf(CharPool.UNDERLINE, index + 1)) != -1) {
			if (openUnderLine == -1) {
				sb.append(s.substring(position, index));

				openUnderLine = index;
				position = index;

				continue;
			}

			String encoded = s.substring(openUnderLine + 1, index);

			Character character = _charPoolChars.get(
				StringUtil.toUpperCase(encoded));

			if (character == null) {
				int value = GetterUtil.get(encoded, -1);

				if (Character.isDefined(value)) {
					sb.append(new String(Character.toChars(value)));
				}
				else {
					if (_log.isWarnEnabled()) {
						_log.warn(
							StringBundler.concat(
								"Unable to decode part \"", encoded,
								"\" from \"", s, "\", preserve it literally"));
					}

					sb.append(s.substring(openUnderLine, index + 1));
				}
			}
			else {
				sb.append(character);
			}

			openUnderLine = -1;
			position = index + 1;
		}

		sb.append(s.substring(position));

		return sb.toString();
	}

	/**
	 * @see com.liferay.portal.tools.ToolsUtil#_charPoolChars
	 */
	private static Map<String, Character> _getCharPoolChars() {
		try {
			Map<String, Character> charPoolChars = new HashMap<>();

			for (Field field : CharPool.class.getFields()) {
				if (Modifier.isStatic(field.getModifiers()) &&
					(field.getType() == char.class)) {

					charPoolChars.put(
						StringUtil.removeChar(
							field.getName(), CharPool.UNDERLINE),
						field.getChar(null));
				}
			}

			return charPoolChars;
		}
		catch (Exception exception) {
			throw new ExceptionInInitializerError(exception);
		}
	}

	private static void _loadEnvOverrides(Configuration configuration) {
		Map<String, String> env = System.getenv();

		for (Map.Entry<String, String> entry : env.entrySet()) {
			String key = entry.getKey();

			if (!key.startsWith(_ENV_OVERRIDE_PREFIX)) {
				continue;
			}

			String newKey = _decode(
				StringUtil.toLowerCase(
					key.substring(_ENV_OVERRIDE_PREFIX.length())));

			if (newKey.equals("include-and-override")) {
				continue;
			}

			configuration.setProperty(newKey, entry.getValue());

			if (_log.isInfoEnabled()) {
				_log.info(
					StringBundler.concat(
						"Overrode property ", newKey,
						" with the value from the environment variable ", key));
			}
		}
	}

	private static final String _ENV_OVERRIDE_PREFIX = "LIFERAY_";

	private static final Log _log = LogFactoryUtil.getLog(
		ClassLoaderAggregatePropertiesUtil.class);

	private static final Map<String, Character> _charPoolChars =
		_getCharPoolChars();

}