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

package com.liferay.portal.upload.internal;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.upload.FileItem;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.tools.ToolDependencies;
import com.liferay.portal.upload.UploadServletRequestImpl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Roberto Díaz
 */
public class UploadServletRequestImplTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		ToolDependencies.wireCaches();

		_fileItems.add(_getFileItem("A", 12));
		_fileItems.add(_getFileItem("B", 92));
		_fileItems.add(_getFileItem("F", 80));
		_fileItems.add(_getFileItem("FIRST_ELEMENT_FIELD_NAME", 1));
		_fileItems.add(_getFileItem("G", 80));
		_fileItems.add(_getFileItem("LAST_ELEMENT_FIELD_NAME", 999));
		_fileItems.add(_getFileItem("REPEATED_ELEMENT_FIELD_NAME", 2));
		_fileItems.add(_getFileItem("REPEATED_ELEMENT_FIELD_NAME", 1));
		_fileItems.add(_getFileItem("S", 65));
		_fileItems.add(_getFileItem("T", 34));
	}

	@Test
	public void testSort() {
		List<FileItem> sortedFileItems = ReflectionTestUtil.invoke(
			new UploadServletRequestImpl(
				ProxyFactory.newDummyInstance(HttpServletRequest.class), null,
				null),
			"_sort", new Class<?>[] {List.class}, _fileItems);

		Assert.assertEquals(
			sortedFileItems.toString(), 10, sortedFileItems.size());

		String previousFieldName = StringPool.BLANK;
		long previousSize = 0;

		for (FileItem sortedFileItem : sortedFileItems) {
			String fieldName = sortedFileItem.getFieldName();
			long size = sortedFileItem.getSize();

			if (!previousFieldName.equals(fieldName)) {
				Assert.assertTrue(previousSize <= size);
			}

			previousFieldName = fieldName;
			previousSize = size;
		}
	}

	@Test
	public void testSortKeepsOriginalOrderWithSameParameterName() {
		List<FileItem> sortedFileItems = ReflectionTestUtil.invoke(
			new UploadServletRequestImpl(
				ProxyFactory.newDummyInstance(HttpServletRequest.class), null,
				null),
			"_sort", new Class<?>[] {List.class}, _fileItems);

		FileItem fileItem1 = sortedFileItems.get(1);

		Assert.assertEquals(
			"REPEATED_ELEMENT_FIELD_NAME", fileItem1.getFieldName());
		Assert.assertEquals(2, fileItem1.getSize());

		FileItem fileItem2 = sortedFileItems.get(2);

		Assert.assertEquals(
			"REPEATED_ELEMENT_FIELD_NAME", fileItem2.getFieldName());
		Assert.assertEquals(1, fileItem2.getSize());
	}

	private FileItem _getFileItem(String fieldName, long size) {
		FileItem fileItem = new LiferayFileItem(
			fieldName, null, false, null, 0, null, null);

		ReflectionTestUtil.setFieldValue(fileItem, "size", size);

		return fileItem;
	}

	private final List<FileItem> _fileItems = new ArrayList<>();

}