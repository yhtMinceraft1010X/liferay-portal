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

package com.liferay.dynamic.data.mapping.form.field.type.internal.text;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldOptionsFactory;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.form.field.type.internal.util.DDMFormFieldTypeUtil;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = "ddm.form.field.type.name=" + DDMFormFieldTypeConstants.TEXT,
	service = {
		DDMFormFieldTemplateContextContributor.class,
		TextDDMFormFieldTemplateContextContributor.class
	}
)
public class TextDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, Object> parameters = new HashMap<>();

		Locale locale = ddmFormFieldRenderingContext.getLocale();

		if (ddmFormFieldRenderingContext.isReturnFullContext()) {
			parameters = HashMapBuilder.<String, Object>put(
				"autocompleteEnabled", _isAutocompleteEnabled(ddmFormField)
			).put(
				"confirmationErrorMessage",
				DDMFormFieldTypeUtil.getPropertyValue(
					ddmFormField, locale, "confirmationErrorMessage")
			).put(
				"confirmationLabel",
				DDMFormFieldTypeUtil.getPropertyValue(
					ddmFormField, locale, "confirmationLabel")
			).put(
				"direction", ddmFormField.getProperty("direction")
			).put(
				"displayStyle", _getDisplayStyle(ddmFormField)
			).put(
				"hideField",
				GetterUtil.getBoolean(ddmFormField.getProperty("hideField"))
			).put(
				"maxLength",
				() -> {
					Object maxLength = ddmFormField.getProperty("maxLength");

					if (Validator.isNotNull(maxLength)) {
						return GetterUtil.getInteger(maxLength);
					}

					return null;
				}
			).put(
				"placeholder",
				DDMFormFieldTypeUtil.getPropertyValue(
					ddmFormField, locale, "placeholder")
			).put(
				"requireConfirmation",
				GetterUtil.getBoolean(
					ddmFormField.getProperty("requireConfirmation"))
			).put(
				"showCounter",
				() -> {
					Object showCounter = ddmFormField.getProperty(
						"showCounter");

					if (showCounter != null) {
						return GetterUtil.getBoolean(showCounter);
					}

					return null;
				}
			).put(
				"tooltip",
				DDMFormFieldTypeUtil.getPropertyValue(
					ddmFormField, locale, "tooltip")
			).build();
		}

		return HashMapBuilder.<String, Object>put(
			"invalidCharacters",
			GetterUtil.getString(ddmFormField.getProperty("invalidCharacters"))
		).put(
			"normalizeField",
			GetterUtil.getBoolean(ddmFormField.getProperty("normalizeField"))
		).put(
			"options", _getOptions(ddmFormField, ddmFormFieldRenderingContext)
		).put(
			"predefinedValue",
			DDMFormFieldTypeUtil.getPropertyValue(
				ddmFormField, ddmFormFieldRenderingContext.getLocale(),
				"predefinedValue")
		).putAll(
			parameters
		).build();
	}

	@Reference
	protected DDMFormFieldOptionsFactory ddmFormFieldOptionsFactory;

	private String _getDisplayStyle(DDMFormField ddmFormField) {
		return GetterUtil.getString(
			ddmFormField.getProperty("displayStyle"), "singleline");
	}

	private List<Object> _getOptions(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		List<Object> options = new ArrayList<>();

		DDMFormFieldOptions ddmFormFieldOptions =
			ddmFormFieldOptionsFactory.create(
				ddmFormField, ddmFormFieldRenderingContext);

		if (ddmFormFieldOptions == null) {
			return options;
		}

		for (String optionValue : ddmFormFieldOptions.getOptionsValues()) {
			options.add(
				HashMapBuilder.put(
					"label",
					() -> {
						LocalizedValue optionLabel =
							ddmFormFieldOptions.getOptionLabels(optionValue);

						return optionLabel.getString(
							ddmFormFieldRenderingContext.getLocale());
					}
				).put(
					"reference",
					ddmFormFieldOptions.getOptionReference(optionValue)
				).put(
					"value", optionValue
				).build());
		}

		return options;
	}

	private boolean _isAutocompleteEnabled(DDMFormField ddmFormField) {
		return GetterUtil.getBoolean(ddmFormField.getProperty("autocomplete"));
	}

}