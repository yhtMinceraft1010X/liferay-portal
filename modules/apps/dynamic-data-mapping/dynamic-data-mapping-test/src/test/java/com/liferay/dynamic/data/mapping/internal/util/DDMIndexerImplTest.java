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

package com.liferay.dynamic.data.mapping.internal.util;

import com.liferay.dynamic.data.mapping.configuration.DDMIndexerConfiguration;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.internal.io.DDMFormJSONSerializer;
import com.liferay.dynamic.data.mapping.internal.test.util.DDMFixture;
import com.liferay.dynamic.data.mapping.io.DDMFormSerializerSerializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormSerializerSerializeResponse;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.impl.DDMStructureImpl;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.PropsTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.test.util.FieldValuesAssert;
import com.liferay.portal.search.test.util.indexing.DocumentFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * @author Lino Alves
 * @author André de Oliveira
 */
public class DDMIndexerImplTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		ddmFixture.setUp();
		documentFixture.setUp();
		_setUpPortalUtil();
		_setUpPropsUtil();

		ddmIndexer = _createDDMIndexer();
	}

	@After
	public void tearDown() {
		ddmFixture.tearDown();

		documentFixture.tearDown();
	}

	@Test
	public void testFormWithOneAvailableLocaleSameAsDefaultLocale() {
		Locale defaultLocale = LocaleUtil.JAPAN;
		Locale translationLocale = LocaleUtil.JAPAN;

		Set<Locale> availableLocales = Collections.singleton(defaultLocale);

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm(
			availableLocales, defaultLocale);

		String fieldName = "text1";
		String indexType = "text";

		ddmForm.addDDMFormField(_createDDMFormField(fieldName, indexType));

		String fieldValue = "新規作成";

		DDMFormFieldValue ddmFormFieldValue = createDDMFormFieldValue(
			fieldName, translationLocale, fieldValue, defaultLocale);

		Document document = createDocument();

		DDMStructure ddmStructure = _createDDMStructure(ddmForm);

		DDMFormValues ddmFormValues = createDDMFormValues(
			ddmForm, ddmFormFieldValue);

		ddmIndexer.addAttributes(document, ddmStructure, ddmFormValues);

		Map<String, String> map = _withSortableValues(
			Collections.singletonMap(
				"ddmFieldArray.ddmFieldValueText_ja_JP", fieldValue));

		FieldValuesAssert.assertFieldValues(
			map, "ddmFieldArray.ddmFieldValueText", document, fieldValue);
	}

	@Test
	public void testFormWithTwoAvailableLocalesAndFieldWithNondefaultLocale() {
		Locale defaultLocale = LocaleUtil.US;
		Locale translationLocale = LocaleUtil.JAPAN;

		Set<Locale> availableLocales = new HashSet<>(
			Arrays.asList(defaultLocale, translationLocale));

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm(
			availableLocales, defaultLocale);

		String fieldName = "text1";
		String indexType = "text";

		ddmForm.addDDMFormField(_createDDMFormField(fieldName, indexType));

		String fieldValue = "新規作成";

		DDMFormFieldValue ddmFormFieldValue = createDDMFormFieldValue(
			fieldName, translationLocale, fieldValue, defaultLocale);

		Document document = createDocument();

		DDMStructure ddmStructure = _createDDMStructure(ddmForm);

		DDMFormValues ddmFormValues = createDDMFormValues(
			ddmForm, ddmFormFieldValue);

		ddmIndexer.addAttributes(document, ddmStructure, ddmFormValues);

		Map<String, String> map = _withSortableValues(
			Collections.singletonMap(
				"ddmFieldArray.ddmFieldValueText_ja_JP", fieldValue));

		FieldValuesAssert.assertFieldValues(
			map, "ddmFieldArray.ddmFieldValueText", document, fieldValue);
	}

	@Test
	public void testFormWithTwoAvailableLocalesAndFieldWithTwoLocales() {
		Locale defaultLocale = LocaleUtil.JAPAN;
		Locale translationLocale = LocaleUtil.US;

		Set<Locale> availableLocales = new HashSet<>(
			Arrays.asList(defaultLocale, translationLocale));

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm(
			availableLocales, defaultLocale);

		String fieldName = "text1";
		String indexType = "text";

		DDMFormField ddmFormField = _createDDMFormField(fieldName, indexType);

		ddmForm.addDDMFormField(ddmFormField);

		String fieldValueJP = "新規作成";
		String fieldValueUS = "Create New";

		DDMFormFieldValue ddmFormFieldValueJP = createDDMFormFieldValue(
			fieldName, defaultLocale, fieldValueJP, defaultLocale);

		DDMFormFieldValue ddmFormFieldValueUS = createDDMFormFieldValue(
			fieldName, translationLocale, fieldValueUS, defaultLocale);

		Document document = createDocument();

		DDMStructure ddmStructure = _createDDMStructure(ddmForm);

		DDMFormValues ddmFormValues = createDDMFormValues(
			ddmForm, ddmFormFieldValueJP, ddmFormFieldValueUS);

		ddmIndexer.addAttributes(document, ddmStructure, ddmFormValues);

		Map<String, String> map = _withSortableValues(
			HashMapBuilder.put(
				"ddmFieldArray.ddmFieldValueText_en_US", fieldValueUS
			).put(
				"ddmFieldArray.ddmFieldValueText_ja_JP", fieldValueJP
			).build());

		FieldValuesAssert.assertFieldValues(
			map, "ddmFieldArray.ddmFieldValueText", document, fieldValueJP);
	}

	protected DDMFormFieldValue createDDMFormFieldValue(
		String name, Locale locale, String valueString, Locale defaultLocale) {

		LocalizedValue localizedValue = new LocalizedValue(defaultLocale);

		localizedValue.addString(locale, valueString);

		return DDMFormValuesTestUtil.createDDMFormFieldValue(
			name, localizedValue);
	}

	protected DDMFormValues createDDMFormValues(
		DDMForm ddmForm, DDMFormFieldValue... ddmFormFieldValues) {

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);
		}

		return ddmFormValues;
	}

	protected Document createDocument() {
		return DocumentFixture.newDocument(
			RandomTestUtil.randomLong(), RandomTestUtil.randomLong(),
			DDMForm.class.getName());
	}

	protected String serialize(DDMForm ddmForm) {
		DDMFormSerializerSerializeRequest.Builder builder =
			DDMFormSerializerSerializeRequest.Builder.newBuilder(ddmForm);

		DDMFormSerializerSerializeResponse ddmFormSerializerSerializeResponse =
			ddmFormJSONSerializer.serialize(builder.build());

		return ddmFormSerializerSerializeResponse.getContent();
	}

	protected final DDMFixture ddmFixture = new DDMFixture();
	protected final DDMFormJSONSerializer ddmFormJSONSerializer =
		_createDDMFormJSONSerializer();
	protected DDMIndexer ddmIndexer;
	protected final DocumentFixture documentFixture = new DocumentFixture();

	private DDMFormField _createDDMFormField(
		String fieldName, String indexType) {

		DDMFormField ddmFormField = DDMFormTestUtil.createTextDDMFormField(
			fieldName, true, false, true);

		ddmFormField.setIndexType(indexType);

		return ddmFormField;
	}

	private DDMFormJSONSerializer _createDDMFormJSONSerializer() {
		return new DDMFormJSONSerializer() {
			{
				setDDMFormFieldTypeServicesTracker(
					Mockito.mock(DDMFormFieldTypeServicesTracker.class));

				setJSONFactory(new JSONFactoryImpl());
			}
		};
	}

	private DDMIndexer _createDDMIndexer() {
		return new DDMIndexerImpl() {
			{
				DDMIndexerConfiguration ddmIndexerConfiguration = () -> false;

				ReflectionTestUtil.setFieldValue(
					this, "_ddmIndexerConfiguration", ddmIndexerConfiguration);

				setDDMFormValuesToFieldsConverter(
					new DDMFormValuesToFieldsConverterImpl());
			}
		};
	}

	private DDMStructure _createDDMStructure(DDMForm ddmForm) {
		DDMStructure ddmStructure = new DDMStructureImpl();

		ddmStructure.setDefinition(serialize(ddmForm));

		ddmStructure.setDDMForm(ddmForm);

		ddmStructure.setStructureId(RandomTestUtil.randomLong());
		ddmStructure.setName(RandomTestUtil.randomString());

		ddmFixture.whenDDMStructureLocalServiceFetchStructure(ddmStructure);

		return ddmStructure;
	}

	private void _setUpPortalUtil() {
		PortalUtil portalUtil = new PortalUtil();

		Portal portal = Mockito.mock(Portal.class);

		ResourceBundle resourceBundle = Mockito.mock(ResourceBundle.class);

		Mockito.when(
			portal.getResourceBundle(Matchers.any(Locale.class))
		).thenReturn(
			resourceBundle
		);

		portalUtil.setPortal(portal);
	}

	private void _setUpPropsUtil() {
		PropsTestUtil.setProps(
			PropsKeys.INDEX_SORTABLE_TEXT_FIELDS_TRUNCATED_LENGTH, "255");
	}

	private Map<String, String> _withSortableValues(Map<String, String> map) {
		Set<Map.Entry<String, String>> entrySet = map.entrySet();

		Stream<Map.Entry<String, String>> entries = entrySet.stream();

		Map<String, String> map2 = entries.collect(
			Collectors.toMap(
				entry -> entry.getKey() + "_String_sortable",
				entry -> StringUtil.toLowerCase(entry.getValue())));

		map2.putAll(map);

		return map2;
	}

}