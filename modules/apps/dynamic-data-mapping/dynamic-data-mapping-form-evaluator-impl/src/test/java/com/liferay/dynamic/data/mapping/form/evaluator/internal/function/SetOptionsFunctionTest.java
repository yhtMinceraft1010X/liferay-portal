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

package com.liferay.dynamic.data.mapping.form.evaluator.internal.function;

import com.liferay.dynamic.data.mapping.expression.UpdateFieldPropertyRequest;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * @author Leonardo Barros
 */
public class SetOptionsFunctionTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		_setUpLanguageUtil();
	}

	@Test
	public void testApply() {
		Mockito.when(
			_language.getLanguageId(new Locale("pt", "BR"))
		).thenReturn(
			"pt_BR"
		);

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		jsonArray.put(
			_createJSONObject("label1", "value1")
		).put(
			_createJSONObject("label2", "value2")
		).put(
			_createJSONObject("label3", "value3")
		);

		jsonObject.put("pt_BR", jsonArray);

		String json = jsonObject.toString();

		DefaultDDMExpressionObserver spyDefaultDDMExpressionObserver =
			Mockito.spy(new DefaultDDMExpressionObserver());

		_setOptionsFunction.setDDMExpressionObserver(
			spyDefaultDDMExpressionObserver);

		_setOptionsFunction.setDDMExpressionParameterAccessor(
			new DefaultDDMExpressionParameterAccessor());

		Boolean result = _setOptionsFunction.apply("optionList", json);

		ArgumentCaptor<UpdateFieldPropertyRequest> argumentCaptor =
			ArgumentCaptor.forClass(UpdateFieldPropertyRequest.class);

		Mockito.verify(
			spyDefaultDDMExpressionObserver, Mockito.times(1)
		).updateFieldProperty(
			argumentCaptor.capture()
		);

		UpdateFieldPropertyRequest updateFieldPropertyRequest =
			argumentCaptor.getValue();

		Assert.assertEquals(
			"optionList", updateFieldPropertyRequest.getField());

		Map<String, Object> properties =
			updateFieldPropertyRequest.getProperties();

		Assert.assertTrue(properties.containsKey("options"));

		List<KeyValuePair> keyValuePairs = new ArrayList<KeyValuePair>() {
			{
				add(new KeyValuePair("value1", "label1"));
				add(new KeyValuePair("value2", "label2"));
				add(new KeyValuePair("value3", "label3"));
			}
		};

		Assert.assertEquals(keyValuePairs, properties.get("options"));

		Assert.assertTrue(result);
	}

	@Test
	public void testInvalidJSON() {
		Mockito.when(
			_language.getLanguageId(new Locale("pt", "BR"))
		).thenReturn(
			"pt_BR"
		);

		DefaultDDMExpressionObserver spyDefaultDDMExpressionObserver =
			Mockito.spy(new DefaultDDMExpressionObserver());

		_setOptionsFunction.setDDMExpressionObserver(
			spyDefaultDDMExpressionObserver);

		_setOptionsFunction.setDDMExpressionParameterAccessor(
			new DefaultDDMExpressionParameterAccessor());

		Boolean result = _setOptionsFunction.apply("optionList", "INVALID");

		ArgumentCaptor<UpdateFieldPropertyRequest> argumentCaptor =
			ArgumentCaptor.forClass(UpdateFieldPropertyRequest.class);

		Mockito.verify(
			spyDefaultDDMExpressionObserver, Mockito.times(1)
		).updateFieldProperty(
			argumentCaptor.capture()
		);

		UpdateFieldPropertyRequest updateFieldPropertyRequest =
			argumentCaptor.getValue();

		Assert.assertEquals(
			"optionList", updateFieldPropertyRequest.getField());

		Map<String, Object> properties =
			updateFieldPropertyRequest.getProperties();

		Assert.assertTrue(properties.containsKey("options"));

		List<KeyValuePair> keyValuePairs = new ArrayList<>();

		Assert.assertEquals(keyValuePairs, properties.get("options"));

		Assert.assertTrue(result);
	}

	@Test
	public void testNullObserver() {
		Assert.assertFalse(_setOptionsFunction.apply("field", "json"));
	}

	private static void _setUpLanguageUtil() {
		LanguageUtil languageUtil = new LanguageUtil();

		languageUtil.setLanguage(_language);
	}

	private JSONObject _createJSONObject(String label, String value) {
		JSONObject jsonObject = _jsonFactory.createJSONObject();

		jsonObject.put(
			"label", label
		).put(
			"value", value
		);

		return jsonObject;
	}

	private static final JSONFactory _jsonFactory = new JSONFactoryImpl();
	private static final Language _language = Mockito.mock(Language.class);

	private final SetOptionsFunction _setOptionsFunction =
		new SetOptionsFunction(_jsonFactory);

}