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

package com.liferay.search.experiences.internal.blueprint.query;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.query.TermQuery;

import java.util.Iterator;
import java.util.Objects;

/**
 * @author Petteri Karttunen
 */
public class QueryConverter {

	public QueryConverter(Queries queries) {
		_queries = queries;
	}

	public Query toQuery(JSONObject jsonObject) {
		Iterator<String> iterator = jsonObject.keys();

		String type = iterator.next();

		if (Objects.equals(type, "term")) {
			Object object = jsonObject.get("term");

			Float boost = 0.0F;
			String keywords = null;

			if (object instanceof JSONObject) {
				JSONObject termJSONObject = (JSONObject)object;

				boost = GetterUtil.getFloat(termJSONObject.get("boost"));
				keywords = termJSONObject.getString("value");
			}
			else if (object instanceof String) {
				keywords = (String)object;
			}

			if (Validator.isNull(keywords)) {
				return null;
			}

			TermQuery termQuery = _queries.term("term", keywords);

			if (boost > 0.0) {
				termQuery.setBoost(boost);
			}

			return termQuery;
		}
		else if (Objects.equals(type, "wrapper")) {
			return _queries.wrapper(jsonObject.getString("query"));
		}

		return null;
	}

	private final Queries _queries;

}