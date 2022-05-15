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

package com.liferay.portal.osgi.web.wab.generator.internal.connection;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.osgi.web.wab.generator.WabGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;

import java.util.Map;

/**
 * @author Raymond Augé
 * @author Miguel Pastor
 * @author Gregory Amerson
 */
public class WabURLConnection extends URLConnection {

	public WabURLConnection(
		ClassLoader classLoader, WabGenerator wabGenerator, URL url) {

		super(url);

		_classLoader = classLoader;
		_wabGenerator = wabGenerator;
	}

	@Override
	public void connect() throws IOException {
	}

	@Override
	public InputStream getInputStream() throws IOException {
		URL url = getURL();

		Map<String, String[]> parameters = HttpComponentsUtil.getParameterMap(
			url.getQuery());

		if (!parameters.containsKey("Web-ContextPath")) {
			throw new IllegalArgumentException(
				"The parameter map does not contain the required parameter " +
					"Web-ContextPath");
		}

		String path = url.getPath();
		String[] protocols = parameters.get("protocol");

		if (ArrayUtil.isEmpty(protocols)) {
			if (path.startsWith("file:")) {
				path = path.substring(5);
				protocols = new String[] {"file"};
			}
			else {
				throw new IllegalArgumentException(
					"The parameter map does not contain the required " +
						"parameter protocol");
			}
		}

		String[] portalProfileNames = parameters.get(
			"liferay-portal-profile-names");

		if (ArrayUtil.isNotEmpty(portalProfileNames)) {
			path = path.concat("?liferay-portal-profile-names=");

			path = path.concat(StringUtil.merge(portalProfileNames));
		}

		final File file = _transferToTempFile(
			new URL(protocols[0], null, path));

		File processedFile = _wabGenerator.generate(
			_classLoader, file, parameters);

		return new FileInputStream(processedFile) {

			@Override
			public void close() throws IOException {
				try {
					super.close();
				}
				finally {
					FileUtil.deltree(file.getParentFile());
				}
			}

		};
	}

	private File _transferToTempFile(URL url) throws IOException {
		String path = url.getPath();

		String fileName = path.substring(
			path.lastIndexOf(StringPool.SLASH) + 1);

		File file = new File(FileUtil.createTempFolder(), fileName);

		StreamUtil.transfer(url.openStream(), new FileOutputStream(file));

		return file;
	}

	private final ClassLoader _classLoader;
	private final WabGenerator _wabGenerator;

}