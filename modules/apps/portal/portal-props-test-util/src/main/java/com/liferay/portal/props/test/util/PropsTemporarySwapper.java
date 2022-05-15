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

package com.liferay.portal.props.test.util;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lourdes Fernández Besada
 */
public class PropsTemporarySwapper implements AutoCloseable {

	public PropsTemporarySwapper(
		String firstKey, Object firstValue, Object... keysAndValues) {

		_setTemporaryValue(firstKey, String.valueOf(firstValue));

		for (int i = 0; i < keysAndValues.length; i += 2) {
			String key = String.valueOf(keysAndValues[i]);
			String value = String.valueOf(keysAndValues[i + 1]);

			_setTemporaryValue(key, value);
		}
	}

	@Override
	public void close() {
		com.liferay.portal.util.PropsUtil.addProperties(
			new UnicodeProperties(_oldValues, false));
	}

	private void _setTemporaryValue(String key, String value) {
		_oldValues.put(key, GetterUtil.getString(PropsUtil.get(key)));

		com.liferay.portal.util.PropsUtil.set(key, value);
	}

	private final Map<String, String> _oldValues = new HashMap<>();

}