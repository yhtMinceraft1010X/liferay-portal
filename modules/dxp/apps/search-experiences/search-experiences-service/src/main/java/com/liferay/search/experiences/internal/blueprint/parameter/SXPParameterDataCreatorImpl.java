/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.internal.blueprint.parameter;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.TimeZoneUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.search.experiences.blueprint.parameter.DateSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.DoubleSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.FloatSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerArraySXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterData;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterDataCreator;
import com.liferay.search.experiences.blueprint.parameter.StringArraySXPParameter;
import com.liferay.search.experiences.blueprint.parameter.StringSXPParameter;
import com.liferay.search.experiences.model.SXPBlueprint;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = SXPParameterDataCreator.class)
public class SXPParameterDataCreatorImpl implements SXPParameterDataCreator {

	@Override
	public SXPParameterData create(
		SearchRequestBuilder searchRequestBuilder, SXPBlueprint sxpBlueprint) {

		String keywords = "";

		List<SXPParameter> sxpParameters = new ArrayList<>();

		JSONObject jsonObject = JSONUtil.put("test", "test");

		_addCustomSXPParameters(
			jsonObject.getJSONArray("custom"),
			_getSearchContext(searchRequestBuilder), sxpParameters);

		return new SXPParameterDataImpl(keywords, sxpParameters);
	}

	private SXPParameter _addCustomSXPParameter(
		JSONObject jsonObject, String name, SearchContext searchContext) {

		String type = jsonObject.getString("type");

		if (type.equals("date")) {
			return _addCustomSXPParameterDate(jsonObject, name, searchContext);
		}
		else if (type.equals("double")) {
			return _addCustomSXPParameterDouble(
				jsonObject, name, searchContext);
		}
		else if (type.equals("float")) {
			return _addCustomSXPParameterFloat(jsonObject, name, searchContext);
		}
		else if (type.equals("integer_array")) {
			return _addCustomSXPParameterIntegerArray(
				jsonObject, name, searchContext);
		}
		else if (type.equals("string")) {
			return _addCustomSXPParameterString(
				jsonObject, name, searchContext);
		}
		else if (type.equals("string_array")) {
			return _addCustomSXPParameterStringArray(
				jsonObject, name, searchContext);
		}

		return null;
	}

	private SXPParameter _addCustomSXPParameterDate(
		JSONObject jsonObject, String name, SearchContext searchContext) {

		String dateString = _getString(name, searchContext);

		if (Validator.isBlank(dateString)) {
			return null;
		}

		LocalDate localDate = LocalDate.parse(
			dateString,
			DateTimeFormatter.ofPattern(jsonObject.getString("date_format")));

		TimeZone timeZone = searchContext.getTimeZone();

		if (timeZone == null) {
			timeZone = TimeZoneUtil.getDefault();
		}

		Calendar calendar = GregorianCalendar.from(
			localDate.atStartOfDay(timeZone.toZoneId()));

		Date date = calendar.getTime();

		if (date == null) {
			return null;
		}

		return new DateSXPParameter(name, true, date);
	}

	private SXPParameter _addCustomSXPParameterDouble(
		JSONObject jsonObject, String name, SearchContext searchContext) {

		Double value = _getDouble(name, searchContext);

		if ((value == null) && jsonObject.has("default")) {
			value = GetterUtil.getDouble(jsonObject.getString("default"));
		}

		if (value == null) {
			return null;
		}

		double minValue = GetterUtil.getDouble(
			jsonObject.getString("min_value"), Double.MIN_VALUE);

		if (Double.compare(value, minValue) < 0) {
			value = minValue;
		}
		else {
			double maxValue = GetterUtil.getDouble(
				jsonObject.getString("max_value"), Double.MAX_VALUE);

			if (Double.compare(value, maxValue) > 0) {
				value = maxValue;
			}
		}

		return new DoubleSXPParameter(name, true, value);
	}

	private SXPParameter _addCustomSXPParameterFloat(
		JSONObject jsonObject, String name, SearchContext searchContext) {

		Float value = _getFloat(name, searchContext);

		if ((value == null) && jsonObject.has("default")) {
			value = GetterUtil.getFloat(jsonObject.getString("default"));
		}

		if (value == null) {
			return null;
		}

		float minValue = GetterUtil.getFloat(
			jsonObject.getString("min_value"), Float.MIN_VALUE);

		if (Float.compare(value, minValue) < 0) {
			value = minValue;
		}
		else {
			float maxValue = GetterUtil.getFloat(
				jsonObject.getString("max_value"), Float.MAX_VALUE);

			if (Float.compare(value, maxValue) > 0) {
				value = maxValue;
			}
		}

		return new FloatSXPParameter(name, true, value);
	}

	private SXPParameter _addCustomSXPParameterIntegerArray(
		JSONObject jsonObject, String name, SearchContext searchContext) {

		Integer[] value = _getIntegerArray(name, searchContext);

		if ((value == null) && jsonObject.has("default")) {
			Stream<String> stream = Arrays.stream(
				JSONUtil.toStringArray(jsonObject.getJSONArray("default")));

			value = stream.map(
				GetterUtil::getInteger
			).toArray(
				Integer[]::new
			);
		}

		if (ArrayUtil.isEmpty(value)) {
			return null;
		}

		return new IntegerArraySXPParameter(name, true, value);
	}

	private void _addCustomSXPParameters(
		JSONArray jsonArray, SearchContext searchContext,
		List<SXPParameter> sxpParameters) {

		if ((jsonArray == null) || (jsonArray.length() == 0)) {
			return;
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			SXPParameter sxpParameter = _addCustomSXPParameter(
				jsonObject, jsonObject.getString("parameter_name"),
				searchContext);

			_addSXParameter(sxpParameter, sxpParameters);
		}
	}

	private SXPParameter _addCustomSXPParameterString(
		JSONObject jsonObject, String name, SearchContext searchContext) {

		String value = _getString(name, searchContext);

		if (Validator.isBlank(value) && jsonObject.has("default")) {
			value = GetterUtil.getString(jsonObject.getString("default"));
		}

		if (Validator.isBlank(value)) {
			return null;
		}

		return new StringSXPParameter(name, true, value);
	}

	private SXPParameter _addCustomSXPParameterStringArray(
		JSONObject jsonObject, String name, SearchContext searchContext) {

		String[] value = _getStringArray(name, searchContext);

		if ((value == null) && jsonObject.has("default")) {
			value = JSONUtil.toStringArray(jsonObject.getJSONArray("default"));
		}

		if (ArrayUtil.isEmpty(value)) {
			return null;
		}

		return new StringArraySXPParameter(name, true, value);
	}

	private void _addSXParameter(
		SXPParameter sxpParameter, List<SXPParameter> sxpParameters) {

		if (sxpParameter == null) {
			return;
		}

		for (SXPParameter currentSXPParameter : sxpParameters) {
			if (Objects.equals(
					currentSXPParameter.getName(), sxpParameter.getName())) {

				return;
			}
		}

		sxpParameters.add(sxpParameter);
	}

	private Double _getDouble(String name, SearchContext searchContext) {
		Object value = searchContext.getAttribute(name);

		if (Objects.isNull(value)) {
			return null;
		}

		return GetterUtil.getDouble(value);
	}

	private Float _getFloat(String name, SearchContext searchContext) {
		Object value = searchContext.getAttribute(name);

		if (Objects.isNull(value)) {
			return null;
		}

		return GetterUtil.getFloat(value);
	}

	private Integer[] _getIntegerArray(
		String name, SearchContext searchContext) {

		Object value = searchContext.getAttribute(name);

		if (value == null) {
			return null;
		}

		if (value instanceof int[]) {
			int[] array = (int[])value;

			if (ArrayUtil.isEmpty(array)) {
				return null;
			}

			return ArrayUtil.toArray(array);
		}
		else if (value instanceof Integer[]) {
			Integer[] array = (Integer[])value;

			if (ArrayUtil.isEmpty(array)) {
				return null;
			}

			return array;
		}

		return null;
	}

	private SearchContext _getSearchContext(
		SearchRequestBuilder searchRequestBuilder) {

		return searchRequestBuilder.withSearchContextGet(
			searchContext -> searchContext);
	}

	private String _getString(String name, SearchContext searchContext) {
		return GetterUtil.getString(searchContext.getAttribute(name));
	}

	private String[] _getStringArray(String name, SearchContext searchContext) {
		Object value = searchContext.getAttribute(name);

		if ((value == null) || !(value instanceof String[])) {
			return null;
		}

		String[] array = (String[])value;

		if (ArrayUtil.isEmpty(array)) {
			return null;
		}

		return array;
	}

}