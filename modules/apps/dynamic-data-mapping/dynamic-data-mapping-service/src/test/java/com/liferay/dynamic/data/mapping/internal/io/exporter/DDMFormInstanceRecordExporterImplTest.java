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

package com.liferay.dynamic.data.mapping.internal.io.exporter;

import com.liferay.dynamic.data.mapping.exception.FormInstanceRecordExporterException;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueRenderer;
import com.liferay.dynamic.data.mapping.io.exporter.DDMFormInstanceRecordExporterRequest;
import com.liferay.dynamic.data.mapping.io.exporter.DDMFormInstanceRecordExporterResponse;
import com.liferay.dynamic.data.mapping.io.exporter.DDMFormInstanceRecordWriter;
import com.liferay.dynamic.data.mapping.io.exporter.DDMFormInstanceRecordWriterRequest;
import com.liferay.dynamic.data.mapping.io.exporter.DDMFormInstanceRecordWriterResponse;
import com.liferay.dynamic.data.mapping.io.exporter.DDMFormInstanceRecordWriterTracker;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecordVersion;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceVersion;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalService;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceVersionLocalService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.util.FastDateFormatFactoryImpl;

import java.text.Format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * @author Leonardo Barros
 */
public class DDMFormInstanceRecordExporterImplTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_setUpFastDateFormatFactoryUtil();
		_setUpLanguageUtil();
	}

	@Test
	public void testExport() throws Exception {
		DDMFormInstanceRecordExporterImpl ddmFormInstanceRecordExporterImpl =
			Mockito.mock(DDMFormInstanceRecordExporterImpl.class);

		ddmFormInstanceRecordExporterImpl.ddmFormInstanceRecordLocalService =
			_ddmFormInstanceRecordLocalService;

		DDMFormInstanceRecordExporterRequest.Builder builder =
			DDMFormInstanceRecordExporterRequest.Builder.newBuilder(1, "csv");

		OrderByComparator<DDMFormInstanceRecord> orderByComparator =
			Mockito.mock(OrderByComparator.class);

		Locale locale = new Locale("pt", "BR");

		DDMFormInstanceRecordExporterRequest
			ddmFormInstanceRecordExporterRequest = builder.withStatus(
				WorkflowConstants.STATUS_APPROVED
			).withLocale(
				locale
			).withStart(
				1
			).withEnd(
				5
			).withOrderByComparator(
				orderByComparator
			).build();

		List<DDMFormInstanceRecord> ddmFormInstanceRecords =
			Collections.emptyList();

		Mockito.when(
			_ddmFormInstanceRecordLocalService.getFormInstanceRecords(
				1, WorkflowConstants.STATUS_APPROVED, 1, 5, orderByComparator)
		).thenReturn(
			ddmFormInstanceRecords
		);

		Map<String, DDMFormField> ddmFormFields = Collections.emptyMap();

		Mockito.when(
			ddmFormInstanceRecordExporterImpl.getDistinctFields(1)
		).thenReturn(
			ddmFormFields
		);

		Map<String, String> ddmFormFieldsLabel = Collections.emptyMap();

		Mockito.when(
			ddmFormInstanceRecordExporterImpl.getDDMFormFieldsLabel(
				ddmFormFields, locale)
		).thenReturn(
			ddmFormFieldsLabel
		);

		List<Map<String, String>> ddmFormFieldsValues = Collections.emptyList();

		Mockito.when(
			ddmFormInstanceRecordExporterImpl.getDDMFormFieldValues(
				ddmFormFields, ddmFormInstanceRecords, locale)
		).thenReturn(
			ddmFormFieldsValues
		);

		Mockito.when(
			ddmFormInstanceRecordExporterImpl.write(
				"csv", ddmFormFieldsLabel, ddmFormFieldsValues)
		).thenReturn(
			new byte[] {1, 2, 3}
		);

		Mockito.when(
			ddmFormInstanceRecordExporterImpl.export(
				ddmFormInstanceRecordExporterRequest)
		).thenCallRealMethod();

		DDMFormInstanceRecordExporterResponse
			ddmFormInstanceRecordExporterResponse =
				ddmFormInstanceRecordExporterImpl.export(
					ddmFormInstanceRecordExporterRequest);

		Assert.assertArrayEquals(
			new byte[] {1, 2, 3},
			ddmFormInstanceRecordExporterResponse.getContent());

		InOrder inOrder = Mockito.inOrder(
			_ddmFormInstanceRecordLocalService,
			ddmFormInstanceRecordExporterImpl);

		inOrder.verify(
			_ddmFormInstanceRecordLocalService, Mockito.times(1)
		).getFormInstanceRecords(
			1, WorkflowConstants.STATUS_APPROVED, 1, 5, orderByComparator
		);

		inOrder.verify(
			ddmFormInstanceRecordExporterImpl, Mockito.times(1)
		).getDistinctFields(
			1
		);

		inOrder.verify(
			ddmFormInstanceRecordExporterImpl, Mockito.times(1)
		).getDDMFormFieldsLabel(
			ddmFormFields, locale
		);

		inOrder.verify(
			ddmFormInstanceRecordExporterImpl, Mockito.times(1)
		).getDDMFormFieldValues(
			ddmFormFields, ddmFormInstanceRecords, locale
		);

		inOrder.verify(
			ddmFormInstanceRecordExporterImpl, Mockito.times(1)
		).write(
			"csv", ddmFormFieldsLabel, ddmFormFieldsValues
		);
	}

	@Test(expected = FormInstanceRecordExporterException.class)
	public void testExportCatchException() throws Exception {
		DDMFormInstanceRecordExporterImpl ddmFormInstanceRecordExporterImpl =
			new DDMFormInstanceRecordExporterImpl();

		ddmFormInstanceRecordExporterImpl.ddmFormInstanceRecordLocalService =
			_ddmFormInstanceRecordLocalService;

		Mockito.when(
			_ddmFormInstanceRecordLocalService.getFormInstanceRecords(
				Matchers.anyLong(), Matchers.anyInt(), Matchers.anyInt(),
				Matchers.anyInt(), Matchers.any(OrderByComparator.class))
		).thenThrow(
			Exception.class
		);

		DDMFormInstanceRecordExporterRequest.Builder builder =
			DDMFormInstanceRecordExporterRequest.Builder.newBuilder(1, "csv");

		ddmFormInstanceRecordExporterImpl.export(builder.build());
	}

	@Test
	public void testGetDDMFormFieldsLabel() {
		DDMFormInstanceRecordExporterImpl ddmFormInstanceRecordExporterImpl =
			new DDMFormInstanceRecordExporterImpl();

		Locale locale = new Locale("pt", "BR");

		Mockito.when(
			_language.get(locale, "status")
		).thenReturn(
			"Estado"
		);

		Mockito.when(
			_language.get(locale, "modified-date")
		).thenReturn(
			"Data de Modificação"
		);

		Mockito.when(
			_language.get(locale, "author")
		).thenReturn(
			"Autor"
		);

		Mockito.when(
			_language.get(locale, "default-language")
		).thenReturn(
			"Idioma"
		);

		LocalizedValue localizedValue1 = new LocalizedValue();

		localizedValue1.addString(locale, "Campo 1");

		Map<String, String> ddmFormFieldsLabel =
			ddmFormInstanceRecordExporterImpl.getDDMFormFieldsLabel(
				HashMapBuilder.<String, DDMFormField>put(
					"field1",
					() -> {
						DDMFormField ddmFormField1 = new DDMFormField(
							"field1", "text");

						ddmFormField1.setFieldReference("reference1");

						ddmFormField1.setLabel(localizedValue1);

						return ddmFormField1;
					}
				).put(
					"field2",
					() -> {
						DDMFormField ddmFormField2 = new DDMFormField(
							"field2", "text");

						ddmFormField2.setFieldReference("reference2");

						LocalizedValue localizedValue2 = new LocalizedValue();

						localizedValue2.addString(locale, "Campo 2");

						ddmFormField2.setLabel(localizedValue2);

						return ddmFormField2;
					}
				).build(),
				locale);

		Assert.assertEquals("Autor", ddmFormFieldsLabel.get("author"));
		Assert.assertEquals("Idioma", ddmFormFieldsLabel.get("languageId"));
		Assert.assertEquals(
			"Data de Modificação", ddmFormFieldsLabel.get("modifiedDate"));
		Assert.assertEquals("Campo 1", ddmFormFieldsLabel.get("reference1"));
		Assert.assertEquals("Campo 2", ddmFormFieldsLabel.get("reference2"));
		Assert.assertEquals("Estado", ddmFormFieldsLabel.get("status"));
	}

	@Test
	public void testGetDDMFormFieldValue() throws Exception {
		DDMFormInstanceRecordExporterImpl ddmFormInstanceRecordExporterImpl =
			new DDMFormInstanceRecordExporterImpl();

		ddmFormInstanceRecordExporterImpl.ddmFormFieldTypeServicesTracker =
			_ddmFormFieldTypeServicesTracker;

		DDMFormFieldValueRenderer ddmFormFieldValueRenderer = Mockito.mock(
			DDMFormFieldValueRenderer.class);

		DDMFormField ddmFormField = new DDMFormField("field1", "text");

		ddmFormField.setFieldReference("reference1");

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValueWithReference(
				"field1", "reference1", new UnlocalizedValue("value1"));

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValueMap =
			HashMapBuilder.<String, List<DDMFormFieldValue>>put(
				"reference1", ListUtil.fromArray(ddmFormFieldValue)
			).build();

		Locale locale = new Locale("pt", "BR");

		Mockito.when(
			_ddmFormFieldTypeServicesTracker.getDDMFormFieldValueRenderer(
				"text")
		).thenReturn(
			ddmFormFieldValueRenderer
		);

		Mockito.when(
			ddmFormFieldValueRenderer.render(ddmFormFieldValue, locale)
		).thenReturn(
			"value1"
		);

		ReflectionTestUtil.setFieldValue(
			ddmFormInstanceRecordExporterImpl, "_html", _html);

		Mockito.when(
			_html.unescape("value1")
		).thenReturn(
			"value1"
		);

		String actualValue =
			ddmFormInstanceRecordExporterImpl.getDDMFormFieldValue(
				ddmFormField, ddmFormFieldValueMap, locale);

		Assert.assertEquals("value1", actualValue);

		Mockito.verify(
			_ddmFormFieldTypeServicesTracker, Mockito.times(1)
		).getDDMFormFieldValueRenderer(
			"text"
		);

		Mockito.verify(
			ddmFormFieldValueRenderer, Mockito.times(1)
		).render(
			ddmFormFieldValue, locale
		);

		Mockito.verify(
			_html, Mockito.times(1)
		).unescape(
			"value1"
		);
	}

	@Test
	public void testGetDDMFormFieldValues() throws Exception {
		DDMFormInstanceRecordExporterImpl ddmFormInstanceRecordExporterImpl =
			Mockito.mock(DDMFormInstanceRecordExporterImpl.class);

		Locale locale = new Locale("pt", "BR");

		List<DDMFormInstanceRecord> ddmFormInstanceRecords = new ArrayList<>();

		DDMFormInstanceRecord ddmFormInstanceRecord = Mockito.mock(
			DDMFormInstanceRecord.class);

		ddmFormInstanceRecords.add(ddmFormInstanceRecord);

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("field1", "text");

		ddmFormField1.setFieldReference("reference1");

		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("field2", "text");

		ddmFormField2.setFieldReference("reference2");

		ddmForm.addDDMFormField(ddmFormField2);

		Map<String, DDMFormField> ddmFormFields =
			HashMapBuilder.<String, DDMFormField>put(
				"reference1", ddmFormField1
			).put(
				"reference2", ddmFormField2
			).build();

		DDMFormValues ddmFormValues1 =
			DDMFormValuesTestUtil.createDDMFormValues(ddmForm);

		DDMFormFieldValue ddmFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValueWithReference(
				"field2", "reference2", new UnlocalizedValue("value2"));

		ddmFormValues1.addDDMFormFieldValue(ddmFormFieldValue2);

		DDMFormInstanceRecordVersion ddmFormInstanceRecordVersion =
			Mockito.mock(DDMFormInstanceRecordVersion.class);

		Mockito.when(
			ddmFormInstanceRecord.getDDMFormValues()
		).thenReturn(
			ddmFormValues1
		);

		Mockito.when(
			ddmFormInstanceRecordExporterImpl.getDDMFormFieldValue(
				Matchers.any(DDMFormField.class), Matchers.anyMap(),
				Matchers.any(Locale.class))
		).thenReturn(
			"value"
		);

		Mockito.when(
			ddmFormInstanceRecord.getFormInstanceRecordVersion()
		).thenReturn(
			ddmFormInstanceRecordVersion
		);

		Mockito.when(
			ddmFormInstanceRecordVersion.getStatus()
		).thenReturn(
			WorkflowConstants.STATUS_APPROVED
		);

		Date statusDate = new Date();

		Mockito.when(
			ddmFormInstanceRecordVersion.getStatusDate()
		).thenReturn(
			statusDate
		);

		Mockito.when(
			ddmFormInstanceRecordVersion.getUserName()
		).thenReturn(
			"User Name"
		);

		Mockito.when(
			ddmFormInstanceRecordExporterImpl.getStatusMessage(
				Matchers.anyInt(), Matchers.any(Locale.class))
		).thenReturn(
			"aprovado"
		);

		Mockito.when(
			ddmFormInstanceRecordExporterImpl.getDDMFormFieldValues(
				ddmFormFields, ddmFormInstanceRecords, locale)
		).thenCallRealMethod();

		List<Map<String, String>> ddmFormFieldValues =
			ddmFormInstanceRecordExporterImpl.getDDMFormFieldValues(
				ddmFormFields, ddmFormInstanceRecords, locale);

		Map<String, String> valuesMap = ddmFormFieldValues.get(0);

		Assert.assertEquals("User Name", valuesMap.get("author"));
		Assert.assertEquals(
			LocaleUtil.US.toString(), valuesMap.get("languageId"));

		Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(
			locale);

		String modifiedDate = dateFormatDateTime.format(statusDate);

		Assert.assertEquals(modifiedDate, valuesMap.get("modifiedDate"));

		Assert.assertEquals(StringPool.BLANK, valuesMap.get("reference1"));
		Assert.assertEquals("value", valuesMap.get("reference2"));
		Assert.assertEquals("aprovado", valuesMap.get("status"));

		InOrder inOrder = Mockito.inOrder(
			ddmFormInstanceRecordExporterImpl, ddmFormInstanceRecord,
			ddmFormInstanceRecordVersion);

		inOrder.verify(
			ddmFormInstanceRecord, Mockito.times(1)
		).getDDMFormValues();

		inOrder.verify(
			ddmFormInstanceRecordExporterImpl, Mockito.times(1)
		).getDDMFormFieldValue(
			Matchers.any(DDMFormField.class), Matchers.anyMap(),
			Matchers.any(Locale.class)
		);

		inOrder.verify(
			ddmFormInstanceRecord, Mockito.times(1)
		).getFormInstanceRecordVersion();

		inOrder.verify(
			ddmFormInstanceRecordVersion, Mockito.times(1)
		).getUserName();

		inOrder.verify(
			ddmFormInstanceRecordVersion, Mockito.times(1)
		).getStatusDate();

		inOrder.verify(
			ddmFormInstanceRecordVersion, Mockito.times(1)
		).getStatus();

		inOrder.verify(
			ddmFormInstanceRecordExporterImpl, Mockito.times(1)
		).getStatusMessage(
			Matchers.anyInt(), Matchers.any(Locale.class)
		);
	}

	@Test
	public void testGetDistinctFields() throws Exception {
		DDMFormInstanceRecordExporterImpl ddmFormInstanceRecordExporterImpl =
			Mockito.mock(DDMFormInstanceRecordExporterImpl.class);

		DDMStructureVersion ddmStructureVersion = Mockito.mock(
			DDMStructureVersion.class);

		Mockito.when(
			ddmFormInstanceRecordExporterImpl.getStructureVersions(1L)
		).thenReturn(
			ListUtil.fromArray(ddmStructureVersion)
		);

		DDMFormField ddmFormField1 = new DDMFormField("field1", "text");

		ddmFormField1.setFieldReference("reference1");

		DDMFormField ddmFormField2 = new DDMFormField("field2", "text");

		ddmFormField2.setFieldReference("reference2");

		Mockito.when(
			ddmFormInstanceRecordExporterImpl.
				getNontransientDDMFormFieldsReferencesMap(ddmStructureVersion)
		).thenReturn(
			LinkedHashMapBuilder.<String, DDMFormField>put(
				"reference1", ddmFormField1
			).put(
				"reference2", ddmFormField2
			).build()
		);

		Mockito.when(
			ddmFormInstanceRecordExporterImpl.getDistinctFields(1L)
		).thenCallRealMethod();

		Map<String, DDMFormField> distinctFields =
			ddmFormInstanceRecordExporterImpl.getDistinctFields(1);

		Assert.assertEquals(ddmFormField1, distinctFields.get("reference1"));
		Assert.assertEquals(ddmFormField2, distinctFields.get("reference2"));

		InOrder inOrder = Mockito.inOrder(ddmFormInstanceRecordExporterImpl);

		inOrder.verify(
			ddmFormInstanceRecordExporterImpl, Mockito.times(1)
		).getStructureVersions(
			1
		);

		inOrder.verify(
			ddmFormInstanceRecordExporterImpl, Mockito.times(1)
		).getNontransientDDMFormFieldsReferencesMap(
			ddmStructureVersion
		);
	}

	@Test
	public void testGetNontransientDDMFormFieldsReferencesMap() {
		DDMFormInstanceRecordExporterImpl ddmFormInstanceRecordExporterImpl =
			new DDMFormInstanceRecordExporterImpl();

		DDMStructureVersion ddmStructureVersion = Mockito.mock(
			DDMStructureVersion.class);

		DDMForm ddmForm = Mockito.mock(DDMForm.class);

		Mockito.when(
			ddmStructureVersion.getDDMForm()
		).thenReturn(
			ddmForm
		);

		ddmFormInstanceRecordExporterImpl.
			getNontransientDDMFormFieldsReferencesMap(ddmStructureVersion);

		InOrder inOrder = Mockito.inOrder(ddmStructureVersion, ddmForm);

		inOrder.verify(
			ddmStructureVersion, Mockito.times(1)
		).getDDMForm();

		inOrder.verify(
			ddmForm, Mockito.times(1)
		).getNontransientDDMFormFieldsReferencesMap(
			true
		);
	}

	@Test
	public void testGetStatusMessage() {
		DDMFormInstanceRecordExporterImpl ddmFormInstanceRecordExporterImpl =
			new DDMFormInstanceRecordExporterImpl();

		Locale locale = new Locale("pt", "BR");

		Mockito.when(
			_language.get(locale, "approved")
		).thenReturn(
			"approvado"
		);

		Assert.assertEquals(
			"approvado",
			ddmFormInstanceRecordExporterImpl.getStatusMessage(
				WorkflowConstants.STATUS_APPROVED, locale));

		Mockito.verify(
			_language, Mockito.times(1)
		).get(
			locale, "approved"
		);
	}

	@Test
	public void testGetStructureVersions() throws Exception {
		DDMFormInstanceRecordExporterImpl ddmFormInstanceRecordExporterImpl =
			new DDMFormInstanceRecordExporterImpl();

		ddmFormInstanceRecordExporterImpl.ddmFormInstanceVersionLocalService =
			_ddmFormInstanceVersionLocalService;

		List<DDMFormInstanceVersion> ddmFormInstanceVersions =
			new ArrayList<>();

		DDMFormInstanceVersion ddmFormInstanceVersion = Mockito.mock(
			DDMFormInstanceVersion.class);

		ddmFormInstanceVersions.add(ddmFormInstanceVersion);

		Mockito.when(
			_ddmFormInstanceVersionLocalService.getFormInstanceVersions(
				1, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)
		).thenReturn(
			ddmFormInstanceVersions
		);

		DDMStructureVersion ddmStructureVersion = Mockito.mock(
			DDMStructureVersion.class);

		Mockito.when(
			ddmFormInstanceVersion.getStructureVersion()
		).thenReturn(
			ddmStructureVersion
		);

		List<DDMStructureVersion> structureVersions =
			ddmFormInstanceRecordExporterImpl.getStructureVersions(1);

		Assert.assertEquals(ddmStructureVersion, structureVersions.get(0));

		InOrder inOrder = Mockito.inOrder(
			_ddmFormInstanceVersionLocalService, ddmFormInstanceVersion);

		inOrder.verify(
			_ddmFormInstanceVersionLocalService, Mockito.times(1)
		).getFormInstanceVersions(
			1, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null
		);

		inOrder.verify(
			ddmFormInstanceVersion, Mockito.times(1)
		).getStructureVersion();
	}

	@Test
	public void testWrite() throws Exception {
		DDMFormInstanceRecordExporterImpl ddmFormInstanceRecordExporterImpl =
			new DDMFormInstanceRecordExporterImpl();

		ddmFormInstanceRecordExporterImpl.ddmFormInstanceRecordWriterTracker =
			_ddmFormInstanceRecordWriterTracker;

		DDMFormInstanceRecordWriter ddmFormInstanceRecordWriter = Mockito.mock(
			DDMFormInstanceRecordWriter.class);

		Mockito.when(
			_ddmFormInstanceRecordWriterTracker.getDDMFormInstanceRecordWriter(
				"txt")
		).thenReturn(
			ddmFormInstanceRecordWriter
		);

		DDMFormInstanceRecordWriterResponse.Builder builder =
			DDMFormInstanceRecordWriterResponse.Builder.newBuilder(
				new byte[] {1, 2, 3});

		Mockito.when(
			ddmFormInstanceRecordWriter.write(
				Matchers.any(DDMFormInstanceRecordWriterRequest.class))
		).thenReturn(
			builder.build()
		);

		byte[] content = ddmFormInstanceRecordExporterImpl.write(
			"txt", Collections.emptyMap(), Collections.emptyList());

		Assert.assertArrayEquals(new byte[] {1, 2, 3}, content);

		InOrder inOrder = Mockito.inOrder(
			_ddmFormInstanceRecordWriterTracker, ddmFormInstanceRecordWriter);

		inOrder.verify(
			_ddmFormInstanceRecordWriterTracker, Mockito.times(1)
		).getDDMFormInstanceRecordWriter(
			"txt"
		);

		inOrder.verify(
			ddmFormInstanceRecordWriter, Mockito.times(1)
		).write(
			Matchers.any(DDMFormInstanceRecordWriterRequest.class)
		);
	}

	private void _setUpFastDateFormatFactoryUtil() {
		FastDateFormatFactoryUtil fastDateFormatFactoryUtil =
			new FastDateFormatFactoryUtil();

		fastDateFormatFactoryUtil.setFastDateFormatFactory(
			new FastDateFormatFactoryImpl());
	}

	private void _setUpLanguageUtil() {
		LanguageUtil languageUtil = new LanguageUtil();

		languageUtil.setLanguage(_language);
	}

	private final DDMFormFieldTypeServicesTracker
		_ddmFormFieldTypeServicesTracker = Mockito.mock(
			DDMFormFieldTypeServicesTracker.class);
	private final DDMFormInstanceRecordLocalService
		_ddmFormInstanceRecordLocalService = Mockito.mock(
			DDMFormInstanceRecordLocalService.class);
	private final DDMFormInstanceRecordWriterTracker
		_ddmFormInstanceRecordWriterTracker = Mockito.mock(
			DDMFormInstanceRecordWriterTracker.class);
	private final DDMFormInstanceVersionLocalService
		_ddmFormInstanceVersionLocalService = Mockito.mock(
			DDMFormInstanceVersionLocalService.class);
	private final Html _html = Mockito.mock(Html.class);
	private final Language _language = Mockito.mock(Language.class);

}