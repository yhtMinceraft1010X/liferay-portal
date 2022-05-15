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
import com.liferay.portal.kernel.webcache.WebCacheItem;
import com.liferay.portal.kernel.webcache.WebCachePoolUtil;
import com.liferay.search.experiences.blueprint.exception.InvalidWebCacheItemException;
import com.liferay.search.experiences.blueprint.exception.PrivateIPAddressException;
import com.liferay.search.experiences.internal.configuration.IpstackConfiguration;

import java.beans.ExceptionListener;

import java.io.IOException;

import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * @author Brian Wing Shun Chan
 */
public class IpstackWebCacheItem implements WebCacheItem {

	public static JSONObject get(
		ExceptionListener exceptionListener, String ipAddress,
		IpstackConfiguration ipstackConfiguration) {

		try {
			if (!ipstackConfiguration.enabled() ||
				_isPrivateIPAddress(ipAddress)) {

				return JSONFactoryUtil.createJSONObject();
			}

			return (JSONObject)WebCachePoolUtil.get(
				StringBundler.concat(
					IpstackWebCacheItem.class.getName(), StringPool.POUND,
					ipstackConfiguration.apiKey(), StringPool.POUND,
					ipstackConfiguration.apiURL(), StringPool.POUND, ipAddress),
				new IpstackWebCacheItem(ipAddress, ipstackConfiguration));
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			exceptionListener.exceptionThrown(exception);

			return JSONFactoryUtil.createJSONObject();
		}
	}

	public IpstackWebCacheItem(
		String ipAddress, IpstackConfiguration ipstackConfiguration) {

		_ipAddress = ipAddress;
		_ipstackConfiguration = ipstackConfiguration;
	}

	@Override
	public JSONObject convert(String key) {
		try {
			String apiURL = _ipstackConfiguration.apiURL();

			if (!apiURL.endsWith("/")) {
				apiURL += "/";
			}

			String url = StringBundler.concat(
				apiURL, _ipAddress, "?access_key=",
				_ipstackConfiguration.apiKey());

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
		if (_ipstackConfiguration.enabled()) {
			return _ipstackConfiguration.cacheTimeout();
		}

		return 0;
	}

	private static boolean _isPrivateIPAddress(String ipAddress)
		throws Exception {

		Inet4Address inet4Address = (Inet4Address)InetAddress.getByName(
			ipAddress);

		if (inet4Address.isAnyLocalAddress() ||
			inet4Address.isLinkLocalAddress() ||
			inet4Address.isLoopbackAddress() ||
			inet4Address.isMulticastAddress() ||
			inet4Address.isSiteLocalAddress()) {

			throw new PrivateIPAddressException(
				"Unable to resolve private IP address " + ipAddress);
		}

		return false;
	}

	private void _validateResponse(JSONObject jsonObject) {
		boolean success = jsonObject.getBoolean("success", true);

		if (success) {
			return;
		}

		throw new InvalidWebCacheItemException(
			StringBundler.concat(
				"IPStack: ",
				JSONUtil.getValueAsString(
					jsonObject, "JSONObject/error", "Object/info"),
				" (",
				JSONUtil.getValueAsString(
					jsonObject, "JSONObject/error", "Object/code"),
				")"));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IpstackWebCacheItem.class);

	private final String _ipAddress;
	private final IpstackConfiguration _ipstackConfiguration;

}