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

package com.liferay.dynamic.data.mapping.form.web.internal.upload;

import com.liferay.document.library.kernel.exception.FileExtensionException;
import com.liferay.document.library.kernel.exception.FileSizeException;
import com.liferay.document.library.kernel.exception.InvalidFileException;
import com.liferay.dynamic.data.mapping.form.web.internal.configuration.DDMFormWebConfiguration;
import com.liferay.dynamic.data.mapping.form.web.internal.configuration.activator.DDMFormWebConfigurationActivator;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Carolina Barbosa
 */
public class DDMFormUploadValidatorTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		_setUpDDMFormWebConfigurationActivator();
	}

	@Test(expected = InvalidFileException.class)
	public void testInvalidFileException() throws Exception {
		_ddmFormUploadValidator.validateFileSize(null, "test.jpg");
	}

	@Test(expected = FileExtensionException.class)
	public void testInvalidFileExtension() throws Exception {
		_ddmFormUploadValidator.validateFileExtension("test.xml");
	}

	@Test(expected = FileSizeException.class)
	public void testInvalidFileSize() throws Exception {
		_ddmFormUploadValidator.validateFileSize(_mockFile(26), "test.jpg");
	}

	@Test
	public void testValidFileExtension() throws Exception {
		_ddmFormUploadValidator.validateFileExtension("test.JpG");
	}

	@Test
	public void testValidFileSize() throws Exception {
		_ddmFormUploadValidator.validateFileSize(_mockFile(24), "test.jpg");
	}

	private static void _setUpDDMFormWebConfigurationActivator() {
		DDMFormWebConfigurationActivator ddmFormWebConfigurationActivator =
			new DDMFormWebConfigurationActivator();

		DDMFormWebConfiguration ddmFormWebConfiguration =
			ConfigurableUtil.createConfigurable(
				DDMFormWebConfiguration.class, new HashMapDictionary<>());

		ReflectionTestUtil.setFieldValue(
			ddmFormWebConfigurationActivator, "_formWebConfiguration",
			ddmFormWebConfiguration);

		ReflectionTestUtil.setFieldValue(
			_ddmFormUploadValidator, "_ddmFormWebConfigurationActivator",
			ddmFormWebConfigurationActivator);
	}

	private File _mockFile(long length) {
		File file = Mockito.mock(File.class);

		Mockito.when(
			file.length()
		).thenReturn(
			length * _FILE_LENGTH_MB
		);

		return file;
	}

	private static final long _FILE_LENGTH_MB = 1024 * 1024;

	private static final DDMFormUploadValidator _ddmFormUploadValidator =
		new DDMFormUploadValidator();

}