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

package com.liferay.dynamic.data.mapping.internal.io;

import com.liferay.dynamic.data.mapping.io.DDMFormValuesDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.skyscreamer.jsonassert.JSONAssert;

/**
 * @author Marcellus Tavares
 */
public class DDMFormValuesJSONDeserializerTest extends BaseDDMTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_setUpDDMFormValuesJSONDeserializer();
	}

	@Test
	public void testDeserializationWithEmptyFields() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createTextDDMFormField(
				"Text1", false, false, false));

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createDDMFormField(
				"Select1", "Select1", "select", "string", true, false, false));

		String serializedDDMFormValues = read(
			"ddm-form-values-json-deserializer-empty-values.json");

		DDMFormValues ddmFormValues = deserialize(
			serializedDDMFormValues, ddmForm);

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			ddmFormValues.getDDMFormFieldValuesMap();

		Assert.assertEquals(
			ddmFormFieldValuesMap.toString(), 2, ddmFormFieldValuesMap.size());

		List<DDMFormFieldValue> ddmFormFieldValues = ddmFormFieldValuesMap.get(
			"Text1");

		DDMFormFieldValue ddmFormFieldValue = ddmFormFieldValues.get(0);

		Value value = ddmFormFieldValue.getValue();

		Assert.assertTrue(value instanceof UnlocalizedValue);
		Assert.assertEquals(
			StringPool.BLANK, value.getString(value.getDefaultLocale()));

		ddmFormFieldValues = ddmFormFieldValuesMap.get("Select1");

		ddmFormFieldValue = ddmFormFieldValues.get(0);

		value = ddmFormFieldValue.getValue();

		Assert.assertTrue(value instanceof LocalizedValue);
		Assert.assertEquals("[]", value.getString(value.getDefaultLocale()));
	}

	@Test
	public void testDeserializationWithInvalidInstanceId() throws Exception {
		String serializedDDMFormValues = read(
			"ddm-form-values-json-deserializer-invalid-instance-id.json");

		DDMFormValues ddmFormValues = deserialize(
			serializedDDMFormValues, DDMFormTestUtil.createDDMForm());

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			ddmFormValues.getDDMFormFieldValuesMap();

		Assert.assertEquals(
			ddmFormFieldValuesMap.toString(), 4, ddmFormFieldValuesMap.size());

		List<DDMFormFieldValue> ddmFormFieldValues = ddmFormFieldValuesMap.get(
			"Text1");

		DDMFormFieldValue ddmFormFieldValue = ddmFormFieldValues.get(0);

		Assert.assertNotEquals(
			ddmFormFieldValues.toString(),
			"<script>alert(document.location)</script>",
			ddmFormFieldValue.getInstanceId());

		ddmFormFieldValues = ddmFormFieldValuesMap.get("Text2");

		ddmFormFieldValue = ddmFormFieldValues.get(0);

		Assert.assertNotEquals(
			ddmFormFieldValues.toString(), "^%&214214JDJ",
			ddmFormFieldValue.getInstanceId());

		ddmFormFieldValues = ddmFormFieldValuesMap.get("Select1");

		ddmFormFieldValue = ddmFormFieldValues.get(0);

		Assert.assertEquals(
			ddmFormFieldValues.toString(), "yhar",
			ddmFormFieldValue.getInstanceId());

		ddmFormFieldValues = ddmFormFieldValuesMap.get("Select2");

		ddmFormFieldValue = ddmFormFieldValues.get(0);

		Assert.assertEquals(
			ddmFormFieldValues.toString(), "yhKiArYe",
			ddmFormFieldValue.getInstanceId());
	}

	@Test
	public void testDeserializationWithParentRepeatableField()
		throws Exception {

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField separatorDDMFormField =
			DDMFormTestUtil.createSeparatorDDMFormField("Separator", true);

		separatorDDMFormField.addNestedDDMFormField(
			DDMFormTestUtil.createLocalizableTextDDMFormField("Text"));

		ddmForm.addDDMFormField(separatorDDMFormField);

		String serializedDDMFormValues = read(
			"ddm-form-values-json-deserializer-parent-repeatable-field.json");

		DDMFormValues ddmFormValues = deserialize(
			serializedDDMFormValues, ddmForm);

		List<DDMFormFieldValue> ddmFormFieldValues =
			ddmFormValues.getDDMFormFieldValues();

		Assert.assertEquals(
			ddmFormFieldValues.toString(), 3, ddmFormFieldValues.size());

		for (int i = 0; i < ddmFormFieldValues.size(); i++) {
			DDMFormFieldValue separatorDDMFormFieldValue =
				ddmFormFieldValues.get(i);

			_testSeparatorDDMFormFieldValueValue(separatorDDMFormFieldValue);

			List<DDMFormFieldValue> separatorNestedDDMFormFieldValues =
				separatorDDMFormFieldValue.getNestedDDMFormFieldValues();

			Assert.assertEquals(
				separatorNestedDDMFormFieldValues.toString(), 1,
				separatorNestedDDMFormFieldValues.size());

			_testTextDDMFormFieldValue(
				separatorNestedDDMFormFieldValues.get(0), "Content " + i,
				"Conteudo " + i);
		}
	}

	@Test
	public void testDeserializationWithRepeatableField() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createTextDDMFormField("Text", true, true, false));

		String serializedDDMFormValues = read(
			"ddm-form-values-json-deserializer-repeatable-field.json");

		DDMFormValues ddmFormValues = deserialize(
			serializedDDMFormValues, ddmForm);

		List<DDMFormFieldValue> ddmFormFieldValues =
			ddmFormValues.getDDMFormFieldValues();

		Assert.assertEquals(
			ddmFormFieldValues.toString(), 3, ddmFormFieldValues.size());

		for (int i = 0; i < ddmFormFieldValues.size(); i++) {
			_testTextDDMFormFieldValue(
				ddmFormFieldValues.get(i), "Name " + i, "Nome " + i);
		}
	}

	@Test
	public void testDeserializationWithSimpleFields() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createDDMFormField(
				"Boolean", "Boolean", "checkbox", "boolean", true, false,
				false));

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createDDMFormField(
				"Documents_and_Media", "Documents_and_Media",
				"document-library", "string", true, false, false));

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createDDMFormField(
				"Geolocation", "Geolocation", "geolocation", "string", true,
				false, false));

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createDDMFormField(
				"HTML", "HTML", "html", "string", true, false, false));

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createDDMFormField(
				"Image", "Image", "image", "string", true, false, false));

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createDDMFormField(
				"Link_to_Page", "Link to Page", "link_to_page", "string", true,
				false, false));

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createDDMFormField(
				"Select", "Select", "select", "string", true, false, false));

		String serializedDDMFormValues = read(
			"ddm-form-values-json-deserializer-test-data.json");

		DDMFormValues ddmFormValues = deserialize(
			serializedDDMFormValues, ddmForm);

		testAvailableLocales(ddmFormValues);
		testDefaultLocale(ddmFormValues);

		List<DDMFormFieldValue> ddmFormFieldValues =
			ddmFormValues.getDDMFormFieldValues();

		Assert.assertEquals(
			ddmFormFieldValues.toString(), 7, ddmFormFieldValues.size());

		_testBooleanDDMFormFieldValueValues(ddmFormFieldValues.get(0));
		_testDocumentLibraryDDMFormFieldValueValues(ddmFormFieldValues.get(1));
		_testGeolocationDDMFormFieldValueValues(ddmFormFieldValues.get(2));
		_testHTMLDDMFormFieldValueValues(ddmFormFieldValues.get(3));
		_testImageDDMFormFieldValueValues(ddmFormFieldValues.get(4));
		_testLinkToPageDDMFormFieldValueValues(ddmFormFieldValues.get(5));
		_testSelectDDMFormFieldValueValues(ddmFormFieldValues.get(6));
	}

	@Test
	public void testDeserializationWithUnlocalizableField() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createDDMFormField(
				"Boolean", "Boolean", "checkbox", "boolean", false, false,
				false));

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createDDMFormField(
				"Documents_and_Media", "Documents_and_Media",
				"document-library", "string", false, false, false));

		String serializedDDMFormValues = read(
			"ddm-form-values-json-deserializer-unlocalizable-fields.json");

		DDMFormValues ddmFormValues = deserialize(
			serializedDDMFormValues, ddmForm);

		List<DDMFormFieldValue> ddmFormFieldValues =
			ddmFormValues.getDDMFormFieldValues();

		Assert.assertEquals(
			ddmFormFieldValues.toString(), 2, ddmFormFieldValues.size());

		DDMFormFieldValue booleanDDMFormFieldValue = ddmFormFieldValues.get(0);

		Assert.assertEquals("usht", booleanDDMFormFieldValue.getInstanceId());

		Value booleanValue = booleanDDMFormFieldValue.getValue();

		Assert.assertFalse(booleanValue.isLocalized());
		Assert.assertEquals("false", booleanValue.getString(LocaleUtil.US));
		Assert.assertEquals("false", booleanValue.getString(LocaleUtil.BRAZIL));

		DDMFormFieldValue documentLibraryDDMFormFieldValue =
			ddmFormFieldValues.get(1);

		Assert.assertEquals(
			"xdwp", documentLibraryDDMFormFieldValue.getInstanceId());

		Value documentLibraryValue =
			documentLibraryDDMFormFieldValue.getValue();

		Assert.assertFalse(documentLibraryValue.isLocalized());

		JSONObject expectedJSONObject = JSONUtil.put(
			"groupId", 10192
		).put(
			"uuid", "c8acdf70-e101-46a6-83e5-c5f5e087b0dc"
		).put(
			"version", 1.0
		);

		JSONAssert.assertEquals(
			expectedJSONObject.toString(),
			documentLibraryValue.getString(LocaleUtil.US), false);
		JSONAssert.assertEquals(
			expectedJSONObject.toString(),
			documentLibraryValue.getString(LocaleUtil.BRAZIL), false);
	}

	protected DDMFormValues deserialize(String content, DDMForm ddmForm) {
		DDMFormValuesDeserializerDeserializeRequest.Builder builder =
			DDMFormValuesDeserializerDeserializeRequest.Builder.newBuilder(
				content, ddmForm);

		DDMFormValuesDeserializerDeserializeResponse
			ddmFormValuesDeserializerDeserializeResponse =
				_ddmFormValuesDeserializer.deserialize(builder.build());

		return ddmFormValuesDeserializerDeserializeResponse.getDDMFormValues();
	}

	protected void testAvailableLocales(DDMFormValues ddmFormValues) {
		Set<Locale> availableLocales = ddmFormValues.getAvailableLocales();

		Assert.assertEquals(
			availableLocales.toString(), 2, availableLocales.size());
		Assert.assertTrue(
			availableLocales.toString(),
			availableLocales.contains(LocaleUtil.US));
		Assert.assertTrue(
			availableLocales.toString(),
			availableLocales.contains(LocaleUtil.BRAZIL));
	}

	protected void testDefaultLocale(DDMFormValues ddmFormValues) {
		Assert.assertEquals(LocaleUtil.US, ddmFormValues.getDefaultLocale());
	}

	private void _setUpDDMFormValuesJSONDeserializer() throws Exception {
		ReflectionTestUtil.setFieldValue(
			_ddmFormValuesDeserializer, "_jsonFactory", new JSONFactoryImpl());

		ReflectionTestUtil.setFieldValue(
			_ddmFormValuesDeserializer, "_serviceTrackerMap",
			ProxyFactory.newDummyInstance(ServiceTrackerMap.class));
	}

	private void _testBooleanDDMFormFieldValueValues(
		DDMFormFieldValue ddmFormFieldValue) {

		Assert.assertEquals("maky", ddmFormFieldValue.getInstanceId());

		Value value = ddmFormFieldValue.getValue();

		Assert.assertEquals("false", value.getString(LocaleUtil.US));
		Assert.assertEquals("true", value.getString(LocaleUtil.BRAZIL));
	}

	private void _testDocumentLibraryDDMFormFieldValueValues(
			DDMFormFieldValue ddmFormFieldValue)
		throws Exception {

		Assert.assertEquals("autx", ddmFormFieldValue.getInstanceId());

		JSONObject expectedJSONObject = JSONUtil.put(
			"groupId", 10192
		).put(
			"uuid", "c8acdf70-e101-46a6-83e5-c5f5e087b0dc"
		).put(
			"version", 1.0
		);

		Value value = ddmFormFieldValue.getValue();

		JSONAssert.assertEquals(
			expectedJSONObject.toString(), value.getString(LocaleUtil.US),
			false);
		JSONAssert.assertEquals(
			expectedJSONObject.toString(), value.getString(LocaleUtil.BRAZIL),
			false);
	}

	private void _testGeolocationDDMFormFieldValueValues(
			DDMFormFieldValue ddmFormFieldValue)
		throws Exception {

		Assert.assertEquals("powq", ddmFormFieldValue.getInstanceId());

		Value value = ddmFormFieldValue.getValue();

		JSONAssert.assertEquals(
			JSONUtil.put(
				"latitude", 34.0286226
			).put(
				"longitude", -117.8103367
			).toString(),
			value.getString(LocaleUtil.US), false);

		JSONAssert.assertEquals(
			JSONUtil.put(
				"latitude", -8.0349219
			).put(
				"longitude", -34.91922120
			).toString(),
			value.getString(LocaleUtil.BRAZIL), false);
	}

	private void _testHTMLDDMFormFieldValueValues(
		DDMFormFieldValue ddmFormFieldValue) {

		Assert.assertEquals("lamn", ddmFormFieldValue.getInstanceId());

		Value value = ddmFormFieldValue.getValue();

		Assert.assertEquals(
			"<p>This is a test.</p>", value.getString(LocaleUtil.US));
		Assert.assertEquals(
			"<p>Isto e um teste.</p>", value.getString(LocaleUtil.BRAZIL));
	}

	private void _testImageDDMFormFieldValueValues(
			DDMFormFieldValue ddmFormFieldValue)
		throws Exception {

		Assert.assertEquals("labt", ddmFormFieldValue.getInstanceId());

		Value value = ddmFormFieldValue.getValue();

		JSONAssert.assertEquals(
			JSONUtil.put(
				"alt", "This is a image description."
			).put(
				"data", "base64Value"
			).toString(),
			value.getString(LocaleUtil.US), false);

		JSONAssert.assertEquals(
			JSONUtil.put(
				"alt", "Isto e uma descricao de imagem."
			).put(
				"data", "valorEmBase64"
			).toString(),
			value.getString(LocaleUtil.BRAZIL), false);
	}

	private void _testLinkToPageDDMFormFieldValueValues(
			DDMFormFieldValue ddmFormFieldValue)
		throws Exception {

		Assert.assertEquals("nast", ddmFormFieldValue.getInstanceId());

		Value value = ddmFormFieldValue.getValue();

		JSONAssert.assertEquals(
			JSONUtil.put(
				"groupId", 10192
			).put(
				"layoutId", 1
			).put(
				"privateLayout", false
			).toString(),
			value.getString(LocaleUtil.US), false);

		JSONAssert.assertEquals(
			JSONUtil.put(
				"groupId", 10192
			).put(
				"layoutId", 2
			).put(
				"privateLayout", false
			).toString(),
			value.getString(LocaleUtil.BRAZIL), false);
	}

	private void _testSelectDDMFormFieldValueValues(
			DDMFormFieldValue ddmFormFieldValue)
		throws Exception {

		Assert.assertEquals("yhar", ddmFormFieldValue.getInstanceId());

		Value value = ddmFormFieldValue.getValue();

		JSONArray expectedJSONArray = JSONUtil.putAll("Value 1", "Value 3");

		JSONAssert.assertEquals(
			expectedJSONArray.toString(), value.getString(LocaleUtil.US),
			false);

		expectedJSONArray = JSONUtil.putAll("Value 2", "Value 3");

		JSONAssert.assertEquals(
			expectedJSONArray.toString(), value.getString(LocaleUtil.BRAZIL),
			false);
	}

	private void _testSeparatorDDMFormFieldValueValue(
		DDMFormFieldValue ddmFormFieldValue) {

		Assert.assertNull(ddmFormFieldValue.getValue());
	}

	private void _testTextDDMFormFieldValue(
		DDMFormFieldValue ddmFormFieldValue, String expectedEnUS,
		String expectedPtBR) {

		Assert.assertNotNull(ddmFormFieldValue);

		Value value = ddmFormFieldValue.getValue();

		Assert.assertEquals(expectedEnUS, value.getString(LocaleUtil.US));
		Assert.assertEquals(expectedPtBR, value.getString(LocaleUtil.BRAZIL));
	}

	private final DDMFormValuesDeserializer _ddmFormValuesDeserializer =
		new DDMFormValuesJSONDeserializer();

}