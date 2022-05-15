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

import com.liferay.dynamic.data.mapping.io.exporter.DDMFormInstanceRecordWriterRequest;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.util.PropsTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReader;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * @author Leonardo Barros
 */
public class DDMFormInstanceRecordXMLWriterTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_setUpPropsUtil();
		_setUpSAXReaderUtil();
	}

	@Test
	public void testAddFieldElement() {
		DDMFormInstanceRecordXMLWriter ddmFormInstanceRecordXMLWriter =
			new DDMFormInstanceRecordXMLWriter();

		Element element = Mockito.mock(Element.class);

		Element fieldElement = Mockito.mock(Element.class);

		Element labelElement = Mockito.mock(Element.class);

		Element valueElement = Mockito.mock(Element.class);

		Mockito.when(
			element.addElement("field")
		).thenReturn(
			fieldElement
		);

		Mockito.when(
			fieldElement.addElement("label")
		).thenReturn(
			labelElement
		);

		Mockito.when(
			fieldElement.addElement("value")
		).thenReturn(
			valueElement
		);

		ddmFormInstanceRecordXMLWriter.addFieldElement(
			element, "Label 1", "Value 1");

		InOrder inOrder = Mockito.inOrder(
			element, fieldElement, labelElement, valueElement);

		inOrder.verify(
			element, Mockito.times(1)
		).addElement(
			"field"
		);
		inOrder.verify(
			fieldElement, Mockito.times(1)
		).addElement(
			"label"
		);
		inOrder.verify(
			labelElement, Mockito.times(1)
		).addText(
			"Label 1"
		);
		inOrder.verify(
			fieldElement, Mockito.times(1)
		).addElement(
			"value"
		);
		inOrder.verify(
			valueElement, Mockito.times(1)
		).addText(
			"Value 1"
		);
	}

	@Test
	public void testAddFieldElements() {
		DDMFormInstanceRecordXMLWriter ddmFormInstanceRecordXMLWriter =
			Mockito.mock(DDMFormInstanceRecordXMLWriter.class);

		Element element = Mockito.mock(Element.class);

		Map<String, String> ddmFormFieldsLabel = LinkedHashMapBuilder.put(
			"field1", "Field 1"
		).put(
			"field2", "Field 2"
		).build();

		Map<String, String> ddmFormFieldsValue = LinkedHashMapBuilder.put(
			"field1", "Value 1"
		).put(
			"field2", "Value 2"
		).build();

		Mockito.doCallRealMethod(
		).when(
			ddmFormInstanceRecordXMLWriter
		).addFieldElements(
			element, ddmFormFieldsLabel, ddmFormFieldsValue
		);

		Mockito.doNothing(
		).when(
			ddmFormInstanceRecordXMLWriter
		).addFieldElement(
			Matchers.any(Element.class), Matchers.anyString(),
			Matchers.anyString()
		);

		ddmFormInstanceRecordXMLWriter.addFieldElements(
			element, ddmFormFieldsLabel, ddmFormFieldsValue);

		InOrder inOrder = Mockito.inOrder(ddmFormInstanceRecordXMLWriter);

		inOrder.verify(
			ddmFormInstanceRecordXMLWriter, Mockito.times(1)
		).addFieldElement(
			element, "Field 1", "Value 1"
		);

		inOrder.verify(
			ddmFormInstanceRecordXMLWriter, Mockito.times(1)
		).addFieldElement(
			element, "Field 2", "Value 2"
		);
	}

	@Test
	public void testWrite() throws Exception {
		DDMFormInstanceRecordXMLWriter ddmFormInstanceRecordXMLWriter =
			Mockito.mock(DDMFormInstanceRecordXMLWriter.class);

		Map<String, String> ddmFormFieldsLabel = LinkedHashMapBuilder.put(
			"field1", "Field 1"
		).put(
			"field2", "Field 2"
		).put(
			"field3", "Field 3"
		).put(
			"field4", "Field 4"
		).build();

		List<Map<String, String>> ddmFormFieldValues =
			new ArrayList<Map<String, String>>() {
				{
					add(
						HashMapBuilder.put(
							"field1", "2"
						).put(
							"field2", "esta é uma 'string'"
						).put(
							"field3", "false"
						).put(
							"field4", "11.7"
						).build());

					add(
						HashMapBuilder.put(
							"field1", "1"
						).put(
							"field2", "esta é uma 'string'"
						).put(
							"field3", ""
						).put(
							"field4", "10"
						).build());
				}
			};

		DDMFormInstanceRecordWriterRequest.Builder builder =
			DDMFormInstanceRecordWriterRequest.Builder.newBuilder(
				ddmFormFieldsLabel, ddmFormFieldValues);

		Document document = Mockito.mock(Document.class);

		Mockito.when(
			_saxReader.createDocument()
		).thenReturn(
			document
		);

		Element rootElement = Mockito.mock(Element.class);

		Mockito.when(
			document.addElement("root")
		).thenReturn(
			rootElement
		);

		Mockito.when(
			document.asXML()
		).thenReturn(
			StringPool.BLANK
		);

		Mockito.doNothing(
		).when(
			ddmFormInstanceRecordXMLWriter
		).addFieldElements(
			Matchers.any(Element.class), Matchers.anyMap(), Matchers.anyMap()
		);

		DDMFormInstanceRecordWriterRequest ddmFormInstanceRecordWriterRequest =
			builder.build();

		Mockito.when(
			ddmFormInstanceRecordXMLWriter.write(
				ddmFormInstanceRecordWriterRequest)
		).thenCallRealMethod();

		ddmFormInstanceRecordXMLWriter.write(
			ddmFormInstanceRecordWriterRequest);

		InOrder inOrder = Mockito.inOrder(
			_saxReader, document, rootElement, ddmFormInstanceRecordXMLWriter);

		inOrder.verify(
			_saxReader, Mockito.times(1)
		).createDocument();
		inOrder.verify(
			document, Mockito.times(1)
		).addElement(
			"root"
		);
		inOrder.verify(
			rootElement, Mockito.times(1)
		).addElement(
			"fields"
		);

		inOrder.verify(
			ddmFormInstanceRecordXMLWriter, Mockito.times(1)
		).addFieldElements(
			Matchers.any(Element.class), Matchers.anyMap(), Matchers.anyMap()
		);

		inOrder.verify(
			rootElement, Mockito.times(1)
		).addElement(
			"fields"
		);

		inOrder.verify(
			ddmFormInstanceRecordXMLWriter, Mockito.times(1)
		).addFieldElements(
			Matchers.any(Element.class), Matchers.anyMap(), Matchers.anyMap()
		);

		inOrder.verify(
			document, Mockito.times(1)
		).asXML();
	}

	private static void _setUpPropsUtil() {
		PropsTestUtil.setProps(
			PropsKeys.XML_SECURITY_ENABLED, Boolean.TRUE.toString());
	}

	private static void _setUpSAXReaderUtil() {
		SAXReaderUtil saxReaderUtil = new SAXReaderUtil();

		saxReaderUtil.setSAXReader(_saxReader);
	}

	private static final SAXReader _saxReader = Mockito.mock(SAXReader.class);

}