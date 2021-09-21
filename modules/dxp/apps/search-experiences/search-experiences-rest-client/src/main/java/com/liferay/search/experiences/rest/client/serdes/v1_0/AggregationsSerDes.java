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

package com.liferay.search.experiences.rest.client.serdes.v1_0;

import com.liferay.search.experiences.rest.client.dto.v1_0.Aggregations;
import com.liferay.search.experiences.rest.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
@Generated("")
public class AggregationsSerDes {

	public static Aggregations toDTO(String json) {
		AggregationsJSONParser aggregationsJSONParser =
			new AggregationsJSONParser();

		return aggregationsJSONParser.parseToDTO(json);
	}

	public static Aggregations[] toDTOs(String json) {
		AggregationsJSONParser aggregationsJSONParser =
			new AggregationsJSONParser();

		return aggregationsJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Aggregations aggregations) {
		if (aggregations == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (aggregations.getAggs() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"aggs\": ");

			sb.append(_toJSON(aggregations.getAggs()));
		}

		if (aggregations.getAvg() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"avg\": ");

			sb.append(String.valueOf(aggregations.getAvg()));
		}

		if (aggregations.getCardinality() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"cardinality\": ");

			sb.append(String.valueOf(aggregations.getCardinality()));
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		AggregationsJSONParser aggregationsJSONParser =
			new AggregationsJSONParser();

		return aggregationsJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Aggregations aggregations) {
		if (aggregations == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (aggregations.getAggs() == null) {
			map.put("aggs", null);
		}
		else {
			map.put("aggs", String.valueOf(aggregations.getAggs()));
		}

		if (aggregations.getAvg() == null) {
			map.put("avg", null);
		}
		else {
			map.put("avg", String.valueOf(aggregations.getAvg()));
		}

		if (aggregations.getCardinality() == null) {
			map.put("cardinality", null);
		}
		else {
			map.put(
				"cardinality", String.valueOf(aggregations.getCardinality()));
		}

		return map;
	}

	public static class AggregationsJSONParser
		extends BaseJSONParser<Aggregations> {

		@Override
		protected Aggregations createDTO() {
			return new Aggregations();
		}

		@Override
		protected Aggregations[] createDTOArray(int size) {
			return new Aggregations[size];
		}

		@Override
		protected void setField(
			Aggregations aggregations, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "aggs")) {
				if (jsonParserFieldValue != null) {
					aggregations.setAggs(
						(Map)AggregationsSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "avg")) {
				if (jsonParserFieldValue != null) {
					aggregations.setAvg(
						AggregationsSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "cardinality")) {
				if (jsonParserFieldValue != null) {
					aggregations.setCardinality(
						CardinalitySerDes.toDTO((String)jsonParserFieldValue));
				}
			}
		}

	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		for (String[] strings : BaseJSONParser.JSON_ESCAPE_STRINGS) {
			string = string.replace(strings[0], strings[1]);
		}

		return string;
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\": ");

			Object value = entry.getValue();

			Class<?> valueClass = value.getClass();

			if (value instanceof Map) {
				sb.append(_toJSON((Map)value));
			}
			else if (valueClass.isArray()) {
				Object[] values = (Object[])value;

				sb.append("[");

				for (int i = 0; i < values.length; i++) {
					sb.append("\"");
					sb.append(_escape(values[i]));
					sb.append("\"");

					if ((i + 1) < values.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(_escape(entry.getValue()));
				sb.append("\"");
			}
			else {
				sb.append(String.valueOf(entry.getValue()));
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

}