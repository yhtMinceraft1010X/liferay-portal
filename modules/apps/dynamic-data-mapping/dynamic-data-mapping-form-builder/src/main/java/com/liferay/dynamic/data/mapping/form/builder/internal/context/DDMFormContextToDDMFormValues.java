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

package com.liferay.dynamic.data.mapping.form.builder.internal.context;

import com.liferay.dynamic.data.mapping.form.builder.context.DDMFormContextDeserializer;
import com.liferay.dynamic.data.mapping.form.builder.context.DDMFormContextDeserializerRequest;
import com.liferay.dynamic.data.mapping.form.builder.context.DDMFormContextVisitor;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = "dynamic.data.mapping.form.builder.context.deserializer.type=formValues",
	service = DDMFormContextDeserializer.class
)
public class DDMFormContextToDDMFormValues
	implements DDMFormContextDeserializer<DDMFormValues> {

	@Override
	public DDMFormValues deserialize(
			DDMFormContextDeserializerRequest ddmFormContextDeserializerRequest)
		throws PortalException {

		String serializedFormContext =
			ddmFormContextDeserializerRequest.getProperty(
				"serializedFormContext");

		if (Validator.isNull(serializedFormContext)) {
			throw new IllegalStateException(
				"The property \"serializedFormContext\" is required");
		}

		DDMForm ddmForm = ddmFormContextDeserializerRequest.getProperty(
			"ddmForm");

		if (ddmForm == null) {
			throw new IllegalStateException(
				"The property \"ddmForm\" is required");
		}

		Locale currentLocale = ddmFormContextDeserializerRequest.getProperty(
			"currentLocale");

		if (currentLocale == null) {
			currentLocale = LocaleThreadLocal.getSiteDefaultLocale();
		}

		return deserialize(ddmForm, serializedFormContext, currentLocale);
	}

	protected DDMFormValues deserialize(
			DDMForm ddmForm, String serializedFormContext, Locale currentLocale)
		throws PortalException {

		JSONObject jsonObject = jsonFactory.createJSONObject(
			serializedFormContext);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		ddmFormValues.addAvailableLocale(currentLocale);
		ddmFormValues.setDefaultLocale(currentLocale);

		_setDDMFormValuesDDMFormFieldValues(
			jsonObject.getJSONArray("pages"), ddmFormValues);

		return ddmFormValues;
	}

	protected List<DDMFormFieldValue> getDDMFormFieldValues(
		JSONArray jsonArray, DDMForm ddmForm) {

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(true);

		DDMFormContextVisitor ddmFormTemplateContextVisitor =
			new DDMFormContextVisitor(jsonArray);

		ddmFormTemplateContextVisitor.onVisitField(
			new Consumer<JSONObject>() {

				@Override
				public void accept(JSONObject jsonObject) {
					DDMFormFieldValue ddmFormFieldValue = _getDDMFormFieldValue(
						jsonObject);

					_setDDMFormFieldValueValue(
						jsonObject,
						ddmFormFieldsMap.get(jsonObject.getString("fieldName")),
						ddmFormFieldValue);
					_setNestedDDMFormFieldValues(
						jsonObject, ddmFormFieldsMap, ddmFormFieldValue);

					ddmFormFieldValues.add(ddmFormFieldValue);
				}

			});

		ddmFormTemplateContextVisitor.visit();

		return ddmFormFieldValues;
	}

	protected Value getLocalizedValue(JSONObject jsonObject) {
		Value value = new LocalizedValue(LocaleUtil.getSiteDefault());

		Iterator<String> iterator = jsonObject.keys();

		while (iterator.hasNext()) {
			String languageId = iterator.next();

			value.addString(
				LocaleUtil.fromLanguageId(languageId),
				jsonObject.getString(languageId));
		}

		return value;
	}

	@Reference
	protected JSONFactory jsonFactory;

	private DDMFormFieldValue _getDDMFormFieldValue(JSONObject jsonObject) {
		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setFieldReference(
			jsonObject.getString("fieldReference"));
		ddmFormFieldValue.setInstanceId(jsonObject.getString("instanceId"));
		ddmFormFieldValue.setName(jsonObject.getString("fieldName"));

		return ddmFormFieldValue;
	}

	private void _setDDMFormFieldValueValue(
		JSONObject fieldJSONObject, DDMFormField ddmFormField,
		DDMFormFieldValue ddmFormFieldValue) {

		if ((ddmFormField == null) || ddmFormField.isTransient()) {
			return;
		}

		if (ddmFormField.isLocalizable()) {
			ddmFormFieldValue.setValue(
				getLocalizedValue(
					fieldJSONObject.getJSONObject("localizedValue")));
		}
		else if (Objects.equals(ddmFormField.getType(), "checkbox")) {
			ddmFormFieldValue.setValue(
				new UnlocalizedValue(
					String.valueOf(fieldJSONObject.getBoolean("value"))));
		}
		else {
			ddmFormFieldValue.setValue(
				new UnlocalizedValue(fieldJSONObject.getString("value")));
		}
	}

	private void _setDDMFormValuesDDMFormFieldValues(
		JSONArray jsonArray, DDMFormValues ddmFormValues) {

		List<DDMFormFieldValue> ddmFormFieldValues = getDDMFormFieldValues(
			jsonArray, ddmFormValues.getDDMForm());

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);
	}

	private void _setNestedDDMFormFieldValues(
		JSONObject jsonObject, Map<String, DDMFormField> ddmFormFieldsMap,
		DDMFormFieldValue ddmFormFieldValue) {

		if ((jsonObject == null) || !jsonObject.has("nestedFields")) {
			return;
		}

		JSONArray nestedFieldsJSONArray = jsonObject.getJSONArray(
			"nestedFields");

		for (int i = 0; i < nestedFieldsJSONArray.length(); i++) {
			JSONObject nestedFieldJSONObject =
				nestedFieldsJSONArray.getJSONObject(i);

			DDMFormFieldValue nestedDDMFormFieldValue = _getDDMFormFieldValue(
				nestedFieldJSONObject);

			_setDDMFormFieldValueValue(
				nestedFieldJSONObject,
				ddmFormFieldsMap.get(
					nestedFieldJSONObject.getString("fieldName")),
				nestedDDMFormFieldValue);

			ddmFormFieldValue.addNestedDDMFormFieldValue(
				nestedDDMFormFieldValue);

			_setNestedDDMFormFieldValues(
				nestedFieldJSONObject, ddmFormFieldsMap,
				nestedDDMFormFieldValue);
		}
	}

}