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

package com.liferay.layout.util.structure;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.language.LanguageResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Pavel Savinov
 */
public class CommonStylesUtil {

	public static List<String> getAvailableStyleNames() {
		if (_availableStyleNames != null) {
			return _availableStyleNames;
		}

		List<String> availableStyleNames = new ArrayList<>();

		JSONArray jsonArray = getCommonStylesJSONArray();

		Iterator<JSONObject> iterator = jsonArray.iterator();

		iterator.forEachRemaining(
			jsonObject -> {
				JSONArray stylesJSONArray = jsonObject.getJSONArray("styles");

				Iterator<JSONObject> stylesIterator =
					stylesJSONArray.iterator();

				stylesIterator.forEachRemaining(
					styleJSONObject -> availableStyleNames.add(
						styleJSONObject.getString("name")));
			});

		Collections.sort(availableStyleNames);

		_availableStyleNames = availableStyleNames;

		return _availableStyleNames;
	}

	public static JSONArray getCommonStylesJSONArray() {
		try {
			return getCommonStylesJSONArray(
				LanguageResources.getResourceBundle(LocaleUtil.getDefault()));
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public static JSONArray getCommonStylesJSONArray(
			ResourceBundle resourceBundle)
		throws Exception {

		JSONArray commonStylesJSONArray = null;

		if (resourceBundle != null) {
			commonStylesJSONArray = _commonStyles.get(
				resourceBundle.getLocale());
		}

		if (commonStylesJSONArray != null) {
			return commonStylesJSONArray;
		}

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray(
			new String(
				FileUtil.getBytes(
					CommonStylesUtil.class, "common-styles.json")));

		Iterator<JSONObject> iterator = jsonArray.iterator();

		iterator.forEachRemaining(
			jsonObject -> {
				jsonObject.put(
					"label",
					LanguageUtil.get(
						resourceBundle, jsonObject.getString("label")));

				JSONArray stylesJSONArray = jsonObject.getJSONArray("styles");

				Iterator<JSONObject> stylesIterator =
					stylesJSONArray.iterator();

				stylesIterator.forEachRemaining(
					styleJSONObject -> {
						styleJSONObject.put(
							"label",
							LanguageUtil.get(
								resourceBundle,
								styleJSONObject.getString("label")));

						JSONArray validValuesJSONArray =
							styleJSONObject.getJSONArray("validValues");

						if (validValuesJSONArray != null) {
							Iterator<JSONObject> validValuesIterator =
								validValuesJSONArray.iterator();

							validValuesIterator.forEachRemaining(
								validValueJSONObject ->
									validValueJSONObject.put(
										"label",
										LanguageUtil.get(
											resourceBundle,
											validValueJSONObject.getString(
												"label"))));
						}
					});
			});

		if (resourceBundle != null) {
			_commonStyles.put(resourceBundle.getLocale(), jsonArray);
		}

		return jsonArray;
	}

	public static String getCSSTemplate(String propertyKey) {
		if (_cssTemplates != null) {
			return _cssTemplates.get(propertyKey);
		}

		_loadCSSTemplates();

		return _cssTemplates.get(propertyKey);
	}

	public static Object getDefaultStyleValue(String name) {
		if (_defaultValues != null) {
			return _defaultValues.get(name);
		}

		Map<String, Object> defaultValues = getDefaultStyleValues();

		return defaultValues.get(name);
	}

	public static Map<String, Object> getDefaultStyleValues() {
		if (_defaultValues != null) {
			return _defaultValues;
		}

		Map<String, Object> defaultValues = new HashMap<>();

		JSONArray jsonArray = getCommonStylesJSONArray();

		Iterator<JSONObject> iterator = jsonArray.iterator();

		iterator.forEachRemaining(
			jsonObject -> {
				JSONArray stylesJSONArray = jsonObject.getJSONArray("styles");

				Iterator<JSONObject> stylesIterator =
					stylesJSONArray.iterator();

				stylesIterator.forEachRemaining(
					styleJSONObject -> defaultValues.put(
						styleJSONObject.getString("name"),
						styleJSONObject.get("defaultValue")));
			});

		_defaultValues = defaultValues;

		return _defaultValues;
	}

	public static List<String> getResponsiveStyleNames() {
		if (_responsiveStyleNames != null) {
			return _responsiveStyleNames;
		}

		List<String> responsiveStyleNames = new ArrayList<>();

		for (String availableStyleName : getAvailableStyleNames()) {
			if (isResponsive(availableStyleName)) {
				responsiveStyleNames.add(availableStyleName);
			}
		}

		_responsiveStyleNames = responsiveStyleNames;

		return _responsiveStyleNames;
	}

	public static String getResponsiveTemplate(String propertyKey) {
		if (_responsiveTemplates != null) {
			return _responsiveTemplates.get(propertyKey);
		}

		_loadResponsiveTemplates();

		return _responsiveTemplates.get(propertyKey);
	}

	public static boolean isResponsive(String propertyKey) {
		if (_responsiveTemplates != null) {
			return Validator.isNotNull(_responsiveTemplates.get(propertyKey));
		}

		_loadResponsiveTemplates();

		return Validator.isNotNull(_responsiveTemplates.get(propertyKey));
	}

	private static void _loadCSSTemplates() {
		Map<String, String> cssTemplates = new HashMap<>();

		JSONArray jsonArray = getCommonStylesJSONArray();

		Iterator<JSONObject> iterator = jsonArray.iterator();

		iterator.forEachRemaining(
			jsonObject -> {
				JSONArray stylesJSONArray = jsonObject.getJSONArray("styles");

				Iterator<JSONObject> stylesIterator =
					stylesJSONArray.iterator();

				stylesIterator.forEachRemaining(
					styleJSONObject -> cssTemplates.put(
						styleJSONObject.getString("name"),
						styleJSONObject.getString(
							"cssTemplate", StringPool.BLANK)));
			});

		_cssTemplates = cssTemplates;
	}

	private static void _loadResponsiveTemplates() {
		Map<String, String> responsiveTemplates = new HashMap<>();

		JSONArray jsonArray = getCommonStylesJSONArray();

		Iterator<JSONObject> iterator = jsonArray.iterator();

		iterator.forEachRemaining(
			jsonObject -> {
				JSONArray stylesJSONArray = jsonObject.getJSONArray("styles");

				Iterator<JSONObject> stylesIterator =
					stylesJSONArray.iterator();

				stylesIterator.forEachRemaining(
					styleJSONObject -> {
						boolean responsive = styleJSONObject.getBoolean(
							"responsive");

						if (responsive) {
							responsiveTemplates.put(
								styleJSONObject.getString("name"),
								styleJSONObject.getString(
									"responsiveTemplate", StringPool.BLANK));
						}
					});
			});

		_responsiveTemplates = responsiveTemplates;
	}

	private static List<String> _availableStyleNames;
	private static final Map<Locale, JSONArray> _commonStyles = new HashMap<>();
	private static Map<String, String> _cssTemplates;
	private static Map<String, Object> _defaultValues;
	private static List<String> _responsiveStyleNames;
	private static Map<String, String> _responsiveTemplates;

}