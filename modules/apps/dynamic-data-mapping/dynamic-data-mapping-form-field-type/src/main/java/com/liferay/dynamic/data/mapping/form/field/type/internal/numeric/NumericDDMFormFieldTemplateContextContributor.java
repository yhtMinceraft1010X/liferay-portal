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

package com.liferay.dynamic.data.mapping.form.field.type.internal.numeric;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.form.field.type.internal.util.DDMFormFieldTypeUtil;
import com.liferay.dynamic.data.mapping.form.field.type.internal.util.NumericDDMFormFieldTypeUtil;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.dynamic.data.mapping.util.NumericDDMFormFieldUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HtmlParser;
import com.liferay.portal.kernel.util.Validator;

import java.text.DecimalFormat;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true,
	property = "ddm.form.field.type.name=" + DDMFormFieldTypeConstants.NUMERIC,
	service = {
		DDMFormFieldTemplateContextContributor.class,
		NumericDDMFormFieldTemplateContextContributor.class
	}
)
public class NumericDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		String dataType = GetterUtil.getString(
			DDMFormFieldTypeUtil.getChangedPropertyValue(
				ddmFormField, ddmFormFieldRenderingContext, "dataType"));
		Locale locale = ddmFormFieldRenderingContext.getLocale();

		return HashMapBuilder.<String, Object>put(
			"confirmationErrorMessage",
			DDMFormFieldTypeUtil.getPropertyValue(
				ddmFormField, locale, "confirmationErrorMessage")
		).put(
			"confirmationLabel",
			DDMFormFieldTypeUtil.getPropertyValue(
				ddmFormField, locale, "confirmationLabel")
		).put(
			"dataType", dataType
		).put(
			"direction", ddmFormField.getProperty("direction")
		).put(
			"hideField",
			GetterUtil.getBoolean(ddmFormField.getProperty("hideField"))
		).put(
			"placeholder",
			DDMFormFieldTypeUtil.getPropertyValue(
				ddmFormField, locale, "placeholder")
		).put(
			"predefinedValue",
			getFormattedValue(
				ddmFormFieldRenderingContext, locale,
				DDMFormFieldTypeUtil.getPropertyValue(
					ddmFormField, ddmFormFieldRenderingContext.getLocale(),
					"predefinedValue"))
		).put(
			"requireConfirmation",
			GetterUtil.getBoolean(
				ddmFormField.getProperty("requireConfirmation"))
		).put(
			"tooltip",
			DDMFormFieldTypeUtil.getPropertyValue(
				ddmFormField, locale, "tooltip")
		).put(
			"value",
			() -> {
				String value = _htmlParser.extractText(
					ddmFormFieldRenderingContext.getValue());

				if (Objects.equals(value, "NaN")) {
					return StringPool.BLANK;
				}

				return getFormattedValue(
					ddmFormFieldRenderingContext, locale, value);
			}
		).putAll(
			NumericDDMFormFieldTypeUtil.getParameters(
				dataType, ddmFormField, ddmFormFieldRenderingContext)
		).build();
	}

	protected String getFormattedValue(
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext,
		Locale locale, String value) {

		if (Validator.isNull(value)) {
			return StringPool.BLANK;
		}

		if (GetterUtil.getBoolean(
				ddmFormFieldRenderingContext.getProperty("valueChanged"))) {

			DecimalFormat decimalFormat =
				NumericDDMFormFieldUtil.getDecimalFormat(locale);

			return decimalFormat.format(GetterUtil.getNumber(value));
		}

		return value;
	}

	@Reference
	private HtmlParser _htmlParser;

}