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

package com.liferay.portal.file.install.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author Matthew Tambara
 */
public class ConfigurationPropertiesFactory {

	public static ConfigurationProperties create(File file, String encoding)
		throws IOException {

		ConfigurationProperties configurationProperties = null;

		String fileName = file.getName();

		if (fileName.endsWith("cfg")) {
			configurationProperties = new CFGProperties();
		}
		else if (fileName.endsWith("config")) {
			configurationProperties = new TypedProperties();
		}
		else {
			throw new IllegalArgumentException(
				"Unknown configuration type: " + file);
		}

		try (InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, encoding)) {

			configurationProperties.load(reader);
		}

		return configurationProperties;
	}

	public static ConfigurationProperties create(
			String fileName, String content, String encoding)
		throws IOException {

		ConfigurationProperties configurationProperties = null;

		if (fileName.endsWith("cfg")) {
			configurationProperties = new CFGProperties();
		}
		else if (fileName.endsWith("config")) {
			configurationProperties = new TypedProperties();
		}
		else {
			throw new IllegalArgumentException(
				"Unknown configuration type: " + fileName);
		}

		try (Reader reader = new StringReader(content)) {
			configurationProperties.load(reader);
		}

		return configurationProperties;
	}

}