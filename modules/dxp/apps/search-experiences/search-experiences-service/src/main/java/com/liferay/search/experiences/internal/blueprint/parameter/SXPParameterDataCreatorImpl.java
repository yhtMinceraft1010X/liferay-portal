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
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TimeZoneUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.search.experiences.blueprint.parameter.DateSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterData;
import com.liferay.search.experiences.blueprint.parameter.SXPParameterDataCreator;
import com.liferay.search.experiences.model.SXPBlueprint;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

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
		JSONObject jsonObject, SearchContext searchContext) {

		String type = jsonObject.getString("type");

		if (type.equals("date")) {
			return _addCustomSXPParameterDate(jsonObject, searchContext);
		}

		return null;
	}

	private SXPParameter _addCustomSXPParameterDate(
		JSONObject jsonObject, SearchContext searchContext) {

		String name = jsonObject.getString("parameter_name");

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

	private void _addCustomSXPParameters(
		JSONArray jsonArray, SearchContext searchContext,
		List<SXPParameter> sxpParameters) {

		if ((jsonArray == null) || (jsonArray.length() == 0)) {
			return;
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			SXPParameter sxpParameter = _addCustomSXPParameter(
				jsonArray.getJSONObject(i), searchContext);

			_addSXParameter(sxpParameter, sxpParameters);
		}
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

	private SearchContext _getSearchContext(
		SearchRequestBuilder searchRequestBuilder) {

		return searchRequestBuilder.withSearchContextGet(
			searchContext -> searchContext);
	}

	private String _getString(String name, SearchContext searchContext) {
		String value = GetterUtil.getString(searchContext.getAttribute(name));

		if (Validator.isBlank(value)) {
			return null;
		}

		value = StringUtil.trim(value);

		if (Validator.isBlank(value)) {
			return null;
		}

		return value;
	}

}