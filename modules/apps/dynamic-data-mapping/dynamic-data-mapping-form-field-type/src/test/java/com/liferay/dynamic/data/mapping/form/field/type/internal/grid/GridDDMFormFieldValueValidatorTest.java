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

package com.liferay.dynamic.data.mapping.form.field.type.internal.grid;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueValidationException;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Pedro Queiroz
 */
public class GridDDMFormFieldValueValidatorTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_setUpGridDDMFormFieldValueValidator();
	}

	@Test(expected = DDMFormFieldValueValidationException.class)
	public void testValidationWithEmptyColumns() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField = DDMFormTestUtil.createDDMFormField(
			"Grid", "Grid", "grid", "string", false, false, false);

		DDMFormFieldOptions ddmFormFieldRows = new DDMFormFieldOptions();

		ddmFormFieldRows.addOptionLabel(
			"rowValue 1", LocaleUtil.US, "rowLabel 1");
		ddmFormFieldRows.addOptionLabel(
			"rowValue 2", LocaleUtil.US, "rowLabel 2");

		ddmFormField.setProperty("rows", ddmFormFieldRows);

		ddmFormField.setProperty("columns", new DDMFormFieldOptions());

		ddmForm.addDDMFormField(ddmFormField);

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"Grid",
				new UnlocalizedValue("{\"rowValue 1\":\"columnValue 1\"}"));

		_gridDDMFormFieldValueValidator.validate(
			ddmFormField, ddmFormFieldValue.getValue());
	}

	@Test(expected = DDMFormFieldValueValidationException.class)
	public void testValidationWithInvalidColumnValue() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField = DDMFormTestUtil.createDDMFormField(
			"Grid", "Grid", "grid", "string", false, false, false);

		DDMFormFieldOptions ddmFormFieldRows = new DDMFormFieldOptions();

		ddmFormFieldRows.addOptionLabel(
			"rowValue 1", LocaleUtil.US, "rowLabel 1");
		ddmFormFieldRows.addOptionLabel(
			"rowValue 2", LocaleUtil.US, "rowLabel 2");

		ddmFormField.setProperty("rows", ddmFormFieldRows);

		DDMFormFieldOptions ddmFormFieldColumns = new DDMFormFieldOptions();

		ddmFormFieldColumns.addOptionLabel(
			"columnValue 1", LocaleUtil.US, "columnLabel 1");
		ddmFormFieldColumns.addOptionLabel(
			"columnValue 2", LocaleUtil.US, "columnLabel 2");

		ddmFormField.setProperty("columns", ddmFormFieldColumns);

		ddmForm.addDDMFormField(ddmFormField);

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"Grid",
				new UnlocalizedValue("{\"rowValue 1\":\"columnValue 4\"}"));

		_gridDDMFormFieldValueValidator.validate(
			ddmFormField, ddmFormFieldValue.getValue());
	}

	@Test(expected = DDMFormFieldValueValidationException.class)
	public void testValidationWithNoColumns() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField = DDMFormTestUtil.createDDMFormField(
			"Grid", "Grid", "grid", "string", false, false, false);

		DDMFormFieldOptions ddmFormFieldRows = new DDMFormFieldOptions();

		ddmFormFieldRows.addOptionLabel(
			"rowValue 1", LocaleUtil.US, "rowLabel 1");
		ddmFormFieldRows.addOptionLabel(
			"rowValue 2", LocaleUtil.US, "rowLabel 2");

		ddmFormField.setProperty("rows", ddmFormFieldRows);

		DDMFormFieldOptions ddmFormFieldColumns = null;

		ddmFormField.setProperty("columns", ddmFormFieldColumns);

		ddmForm.addDDMFormField(ddmFormField);

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"Grid",
				new UnlocalizedValue("{\"rowValue 1\":\"columnValue 1\"}"));

		_gridDDMFormFieldValueValidator.validate(
			ddmFormField, ddmFormFieldValue.getValue());
	}

	private static void _setUpGridDDMFormFieldValueValidator()
		throws Exception {

		_gridDDMFormFieldValueValidator = new GridDDMFormFieldValueValidator();

		ReflectionTestUtil.setFieldValue(
			_gridDDMFormFieldValueValidator, "jsonFactory",
			new JSONFactoryImpl());
	}

	private static GridDDMFormFieldValueValidator
		_gridDDMFormFieldValueValidator;

}