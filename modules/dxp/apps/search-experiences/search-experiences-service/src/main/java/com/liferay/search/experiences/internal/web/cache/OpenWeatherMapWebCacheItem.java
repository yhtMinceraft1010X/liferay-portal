/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.internal.web.cache;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.webcache.WebCacheItem;
import com.liferay.portal.kernel.webcache.WebCachePoolUtil;
import com.liferay.search.experiences.blueprint.exception.InvalidWebCacheItemException;
import com.liferay.search.experiences.internal.configuration.OpenWeatherMapConfiguration;

import java.beans.ExceptionListener;

import java.io.IOException;

/**
 * @author Brian Wing Shun Chan
 */
public class OpenWeatherMapWebCacheItem implements WebCacheItem {

	public static JSONObject get(
		ExceptionListener exceptionListener, String latitude, String longitude,
		OpenWeatherMapConfiguration openWeatherMapConfiguration) {

		if (!openWeatherMapConfiguration.enabled()) {
			return JSONFactoryUtil.createJSONObject();
		}

		try {
			return (JSONObject)WebCachePoolUtil.get(
				StringBundler.concat(
					OpenWeatherMapWebCacheItem.class.getName(),
					StringPool.POUND, openWeatherMapConfiguration.apiKey(),
					StringPool.POUND, openWeatherMapConfiguration.apiURL(),
					StringPool.POUND, latitude, StringPool.POUND, longitude),
				new OpenWeatherMapWebCacheItem(
					latitude, longitude, openWeatherMapConfiguration));
		}
		catch (Exception exception) {
			exceptionListener.exceptionThrown(exception);

			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return JSONFactoryUtil.createJSONObject();
		}
	}

	public OpenWeatherMapWebCacheItem(
		String latitude, String longitude,
		OpenWeatherMapConfiguration openWeatherMapConfiguration) {

		_latitude = latitude;
		_longitude = longitude;
		_openWeatherMapConfiguration = openWeatherMapConfiguration;
	}

	@Override
	public JSONObject convert(String key) {
		try {
			String url = StringBundler.concat(
				_openWeatherMapConfiguration.apiURL(), "?APPID=",
				_openWeatherMapConfiguration.apiKey(), "&format=json&lat=",
				_latitude, "&lon=", _longitude, "&units=",
				_openWeatherMapConfiguration.units());

			if (_log.isDebugEnabled()) {
				_log.debug("Reading " + url);
			}

			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
				HttpUtil.URLtoString(url));

			_validateResponse(jsonObject);

			return jsonObject;
		}
		catch (IOException | JSONException exception) {
			throw new InvalidWebCacheItemException(exception);
		}
	}

	@Override
	public long getRefreshTime() {
		if (_openWeatherMapConfiguration.enabled()) {
			return _openWeatherMapConfiguration.cacheTimeout();
		}

		return 0;
	}

	private void _validateResponse(JSONObject jsonObject) {
		String cod = jsonObject.getString("cod");

		if (Validator.isNull(cod) || cod.startsWith("2")) {
			return;
		}

		throw new InvalidWebCacheItemException(
			StringBundler.concat(
				"OpenWeatherMap: ",
				JSONUtil.getValueAsString(jsonObject, "Object/message"), " (",
				cod, ")"));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OpenWeatherMapWebCacheItem.class);

	private final String _latitude;
	private final String _longitude;
	private final OpenWeatherMapConfiguration _openWeatherMapConfiguration;

}