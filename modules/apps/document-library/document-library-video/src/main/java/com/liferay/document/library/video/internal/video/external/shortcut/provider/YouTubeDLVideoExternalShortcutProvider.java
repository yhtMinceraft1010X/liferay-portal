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

package com.liferay.document.library.video.internal.video.external.shortcut.provider;

import com.liferay.document.library.video.external.shortcut.DLVideoExternalShortcut;
import com.liferay.document.library.video.external.shortcut.provider.DLVideoExternalShortcutProvider;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.Validator;

import java.net.HttpURLConnection;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(service = DLVideoExternalShortcutProvider.class)
public class YouTubeDLVideoExternalShortcutProvider
	implements DLVideoExternalShortcutProvider {

	@Override
	public DLVideoExternalShortcut getDLVideoExternalShortcut(String url) {
		String youTubeVideoId = _getYouTubeVideoId(url);

		if (Validator.isNull(youTubeVideoId)) {
			return null;
		}

		JSONObject jsonObject = _getEmbedJSONObject(url);

		return new DLVideoExternalShortcut() {

			@Override
			public String getThumbnailURL() {
				return jsonObject.getString("thumbnail_url");
			}

			@Override
			public String getTitle() {
				return jsonObject.getString("title");
			}

			@Override
			public String getURL() {
				return url;
			}

			@Override
			public String renderHTML(HttpServletRequest httpServletRequest) {
				String iframeSrc =
					"https://www.youtube.com/embed/" + youTubeVideoId +
						"?rel=0";
				String start = HttpComponentsUtil.getParameter(url, "t", false);

				if (Validator.isNotNull(start)) {
					iframeSrc = HttpComponentsUtil.addParameter(
						iframeSrc, "start", start);
				}

				return StringBundler.concat(
					"<iframe allow=\"autoplay; encrypted-media\" ",
					"allowfullscreen height=\"315\" frameborder=\"0\" ",
					"src=\"", iframeSrc, "\" width=\"560\"></iframe>");
			}

		};
	}

	private JSONObject _getEmbedJSONObject(String url) {
		try {
			Http.Options options = new Http.Options();

			options.addHeader("Content-Type", ContentTypes.APPLICATION_JSON);
			options.setLocation(
				"https://www.youtube.com/oembed?format=json&url=" + url);

			String responseJSON = _http.URLtoString(options);

			Http.Response response = options.getResponse();

			JSONObject jsonObject;

			if (response.getResponseCode() != HttpURLConnection.HTTP_OK) {
				jsonObject = JSONFactoryUtil.createJSONObject();
			}
			else {
				jsonObject = JSONFactoryUtil.createJSONObject(responseJSON);
			}

			return jsonObject;
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return JSONFactoryUtil.createJSONObject();
		}
	}

	private String _getYouTubeVideoId(String url) {
		for (Pattern urlPattern : _urlPatterns) {
			Matcher matcher = urlPattern.matcher(url);

			if (matcher.matches()) {
				return matcher.group(1);
			}
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		YouTubeDLVideoExternalShortcutProvider.class);

	private static final List<Pattern> _urlPatterns = Arrays.asList(
		Pattern.compile(
			"https?:\\/\\/(?:www\\.)?youtube\\.com\\/watch\\S*v=([^?&]*)\\S*$"),
		Pattern.compile(
			"https?:\\/\\/(?:www\\.)?youtube\\.com\\/\\S*\\/([^?&]*)\\S*$"),
		Pattern.compile("https?:\\/\\/(?:www\\.)?youtu\\.be\\/(\\S*)$"));

	@Reference
	private Http _http;

}