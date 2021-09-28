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

package com.liferay.search.experiences.internal.blueprint.search.request.body.contributor;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.search.experiences.internal.blueprint.highlight.HighlightConverter;
import com.liferay.search.experiences.internal.blueprint.parameter.SXPParameterData;
import com.liferay.search.experiences.internal.blueprint.parameter.SXPParameterParser;
import com.liferay.search.experiences.rest.dto.v1_0.Configuration;
import com.liferay.search.experiences.rest.dto.v1_0.Highlight;
import com.liferay.search.experiences.rest.dto.v1_0.SXPBlueprint;

/**
 * @author Petteri Karttunen
 */
public class HighlightSXPSearchRequestBodyContributor
	implements SXPSearchRequestBodyContributor {

	public HighlightSXPSearchRequestBodyContributor(
		HighlightConverter highlightConverter, JSONFactory jsonFactory) {

		_highlightConverter = highlightConverter;
		_jsonFactory = jsonFactory;
	}

	@Override
	public void contribute(
		SearchRequestBuilder searchRequestBuilder, SXPBlueprint sxpBlueprint,
		SXPParameterData sxpParameterData) {

		Configuration configuration = sxpBlueprint.getConfiguration();

		searchRequestBuilder.highlight(
			_highlightConverter.toHighlight(
				_parse(configuration.getHighlight(), sxpParameterData)));
	}

	@Override
	public String getName() {
		return "highlight";
	}

	private JSONObject _createJSONObject(String string) {
		try {
			return _jsonFactory.createJSONObject(string);
		}
		catch (JSONException jsonException) {
			return ReflectionUtil.throwException(jsonException);
		}
	}

	private JSONObject _parse(
		Highlight highlight, SXPParameterData sxpParameterData) {

		if (highlight == null) {
			return null;
		}

		return SXPParameterParser.parse(
			_createJSONObject(highlight.toString()), sxpParameterData);
	}

	private final HighlightConverter _highlightConverter;
	private final JSONFactory _jsonFactory;

}