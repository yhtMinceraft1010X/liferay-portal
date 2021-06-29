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

package com.liferay.object.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.exception.DuplicateObjectDefinitionException;
import com.liferay.object.exception.ObjectDefinitionNameException;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectDefinitionLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Collections;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marco Leo
 * @author Brian Wing Shun Chan
 */
@RunWith(Arquillian.class)
public class ObjectDefinitionLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testAddObjectDefinition() throws Exception {

		// Name is null

		try {
			_testAddObjectDefinition("", true);

			Assert.fail();
		}
		catch (ObjectDefinitionNameException objectDefinitionNameException) {
			Assert.assertEquals(
				"Name is null", objectDefinitionNameException.getMessage());
		}

		// System names must not start with "C_"

		try {
			_testAddObjectDefinition("C_Test", true);

			Assert.fail();
		}
		catch (ObjectDefinitionNameException objectDefinitionNameException) {
			Assert.assertEquals(
				"System names must not start with \"C_\"",
				objectDefinitionNameException.getMessage());
		}

		try {
			_testAddObjectDefinition("c_Test", true);

			Assert.fail();
		}
		catch (ObjectDefinitionNameException objectDefinitionNameException) {
			Assert.assertEquals(
				"System names must not start with \"C_\"",
				objectDefinitionNameException.getMessage());
		}

		// Nonsystem names are automatically prepended with with "C_"

		try {
			_testAddObjectDefinition("Test", false);
		}
		catch (ObjectDefinitionNameException objectDefinitionNameException) {
			throw objectDefinitionNameException;
		}

		// Name must only contain letters and digits

		_testAddObjectDefinition(" Test ", true);

		try {
			_testAddObjectDefinition("Tes t", true);

			Assert.fail();
		}
		catch (ObjectDefinitionNameException objectDefinitionNameException) {
			Assert.assertEquals(
				"Name must only contain letters and digits",
				objectDefinitionNameException.getMessage());
		}

		try {
			_testAddObjectDefinition("Tes-t", true);

			Assert.fail();
		}
		catch (ObjectDefinitionNameException objectDefinitionNameException) {
			Assert.assertEquals(
				"Name must only contain letters and digits",
				objectDefinitionNameException.getMessage());
		}

		_testAddObjectDefinition(" Test ", false);

		try {
			_testAddObjectDefinition("Tes t", false);

			Assert.fail();
		}
		catch (ObjectDefinitionNameException objectDefinitionNameException) {
			Assert.assertEquals(
				"Name must only contain letters and digits",
				objectDefinitionNameException.getMessage());
		}

		try {
			_testAddObjectDefinition("Tes-t", false);

			Assert.fail();
		}
		catch (ObjectDefinitionNameException objectDefinitionNameException) {
			Assert.assertEquals(
				"Name must only contain letters and digits",
				objectDefinitionNameException.getMessage());
		}

		// The first character of a name must be an upper case letter

		try {
			_testAddObjectDefinition("test", true);

			Assert.fail();
		}
		catch (ObjectDefinitionNameException objectDefinitionNameException) {
			Assert.assertEquals(
				"The first character of a name must be an upper case letter",
				objectDefinitionNameException.getMessage());
		}

		try {
			_testAddObjectDefinition("test", false);

			Assert.fail();
		}
		catch (ObjectDefinitionNameException objectDefinitionNameException) {
			Assert.assertEquals(
				"The first character of a name must be an upper case letter",
				objectDefinitionNameException.getMessage());
		}

		// Names must be less than 41 characters

		_testAddObjectDefinition(
			"A123456789a123456789a123456789a1234567891", true);

		try {
			_testAddObjectDefinition(
				"A123456789a123456789a123456789a12345678912", true);

			Assert.fail();
		}
		catch (ObjectDefinitionNameException objectDefinitionNameException) {
			Assert.assertEquals(
				"Names must be less than 41 characters",
				objectDefinitionNameException.getMessage());
		}

		// Duplicate name

		ObjectDefinitionLocalServiceUtil.addObjectDefinition(
			TestPropsValues.getUserId(), "Test", true,
			Collections.<ObjectField>emptyList());

		try {
			_testAddObjectDefinition("Test", true);
		}
		catch (DuplicateObjectDefinitionException
					duplicateObjectDefinitionException) {

			Assert.assertEquals(
				"Duplicate name Test",
				duplicateObjectDefinitionException.getMessage());
		}

		ObjectDefinitionLocalServiceUtil.addObjectDefinition(
			TestPropsValues.getUserId(), "Test", false,
			Collections.<ObjectField>emptyList());

		try {
			_testAddObjectDefinition("Test", false);
		}
		catch (DuplicateObjectDefinitionException
					duplicateObjectDefinitionException) {

			Assert.assertEquals(
				"Duplicate name C_Test",
				duplicateObjectDefinitionException.getMessage());
		}
	}

	private void _testAddObjectDefinition(String name, boolean system)
		throws Exception {

		ObjectDefinition objectDefinition = null;

		try {
			objectDefinition =
				ObjectDefinitionLocalServiceUtil.addObjectDefinition(
					TestPropsValues.getUserId(), name, system,
					Collections.<ObjectField>emptyList());
		}
		finally {
			if (objectDefinition != null) {
				ObjectDefinitionLocalServiceUtil.deleteObjectDefinition(
					objectDefinition);
			}
		}
	}

}