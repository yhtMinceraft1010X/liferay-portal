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

package com.liferay.search.experiences.rest.dto.v1_0.util;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.search.experiences.rest.dto.v1_0.AggregationConfiguration;
import com.liferay.search.experiences.rest.dto.v1_0.Clause;
import com.liferay.search.experiences.rest.dto.v1_0.Configuration;
import com.liferay.search.experiences.rest.dto.v1_0.QueryConfiguration;
import com.liferay.search.experiences.rest.dto.v1_0.QueryEntry;
import com.liferay.search.experiences.rest.dto.v1_0.SortConfiguration;

import java.lang.reflect.Field;

import java.util.Map;

/**
 * @author André de Oliveira
 */
public class ConfigurationUtil {

	public static Configuration toConfiguration(String json) {
		Configuration configuration = Configuration.toDTO(json);

		_processAggregationConfiguration(
			configuration.getAggregationConfiguration());
		_processQueryConfiguration(configuration.getQueryConfiguration());
		_processSortConfiguration(configuration.getSortConfiguration());

		return configuration;
	}

	private static void _processAggregationConfiguration(
		AggregationConfiguration aggregationConfiguration) {

		if (aggregationConfiguration == null) {
			return;
		}

		aggregationConfiguration.setAggs(
			JSONFactoryUtil.createJSONObject(
				(Map<?, ?>)aggregationConfiguration.getAggs()));
	}

	private static void _processQueryConfiguration(
		QueryConfiguration queryConfiguration) {

		if (queryConfiguration == null) {
			return;
		}

		for (QueryEntry queryEntry : queryConfiguration.getQueryEntries()) {
			for (Clause clause : queryEntry.getClauses()) {
				clause.setQuery(
					JSONFactoryUtil.createJSONObject(
						(Map<?, ?>)clause.getQuery()));
			}
		}
	}

	private static void _processSortConfiguration(
		SortConfiguration sortConfiguration) {

		if (sortConfiguration == null) {
			return;
		}

		sortConfiguration.setSorts(
			JSONFactoryUtil.createJSONArray(
				(Object[])sortConfiguration.getSorts()));
	}

}