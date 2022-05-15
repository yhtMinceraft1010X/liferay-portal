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

package com.liferay.object.admin.rest.client.serdes.v1_0;

import com.liferay.object.admin.rest.client.dto.v1_0.ObjectViewFilterColumn;
import com.liferay.object.admin.rest.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class ObjectViewFilterColumnSerDes {

	public static ObjectViewFilterColumn toDTO(String json) {
		ObjectViewFilterColumnJSONParser objectViewFilterColumnJSONParser =
			new ObjectViewFilterColumnJSONParser();

		return objectViewFilterColumnJSONParser.parseToDTO(json);
	}

	public static ObjectViewFilterColumn[] toDTOs(String json) {
		ObjectViewFilterColumnJSONParser objectViewFilterColumnJSONParser =
			new ObjectViewFilterColumnJSONParser();

		return objectViewFilterColumnJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ObjectViewFilterColumn objectViewFilterColumn) {
		if (objectViewFilterColumn == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (objectViewFilterColumn.getFilterType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"filterType\": ");

			sb.append("\"");

			sb.append(objectViewFilterColumn.getFilterType());

			sb.append("\"");
		}

		if (objectViewFilterColumn.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(objectViewFilterColumn.getId());
		}

		if (objectViewFilterColumn.getJson() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"json\": ");

			sb.append("\"");

			sb.append(_escape(objectViewFilterColumn.getJson()));

			sb.append("\"");
		}

		if (objectViewFilterColumn.getObjectFieldName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"objectFieldName\": ");

			sb.append("\"");

			sb.append(_escape(objectViewFilterColumn.getObjectFieldName()));

			sb.append("\"");
		}

		if (objectViewFilterColumn.getValueSummary() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"valueSummary\": ");

			sb.append("\"");

			sb.append(_escape(objectViewFilterColumn.getValueSummary()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ObjectViewFilterColumnJSONParser objectViewFilterColumnJSONParser =
			new ObjectViewFilterColumnJSONParser();

		return objectViewFilterColumnJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		ObjectViewFilterColumn objectViewFilterColumn) {

		if (objectViewFilterColumn == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (objectViewFilterColumn.getFilterType() == null) {
			map.put("filterType", null);
		}
		else {
			map.put(
				"filterType",
				String.valueOf(objectViewFilterColumn.getFilterType()));
		}

		if (objectViewFilterColumn.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(objectViewFilterColumn.getId()));
		}

		if (objectViewFilterColumn.getJson() == null) {
			map.put("json", null);
		}
		else {
			map.put("json", String.valueOf(objectViewFilterColumn.getJson()));
		}

		if (objectViewFilterColumn.getObjectFieldName() == null) {
			map.put("objectFieldName", null);
		}
		else {
			map.put(
				"objectFieldName",
				String.valueOf(objectViewFilterColumn.getObjectFieldName()));
		}

		if (objectViewFilterColumn.getValueSummary() == null) {
			map.put("valueSummary", null);
		}
		else {
			map.put(
				"valueSummary",
				String.valueOf(objectViewFilterColumn.getValueSummary()));
		}

		return map;
	}

	public static class ObjectViewFilterColumnJSONParser
		extends BaseJSONParser<ObjectViewFilterColumn> {

		@Override
		protected ObjectViewFilterColumn createDTO() {
			return new ObjectViewFilterColumn();
		}

		@Override
		protected ObjectViewFilterColumn[] createDTOArray(int size) {
			return new ObjectViewFilterColumn[size];
		}

		@Override
		protected void setField(
			ObjectViewFilterColumn objectViewFilterColumn,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "filterType")) {
				if (jsonParserFieldValue != null) {
					objectViewFilterColumn.setFilterType(
						ObjectViewFilterColumn.FilterType.create(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					objectViewFilterColumn.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "json")) {
				if (jsonParserFieldValue != null) {
					objectViewFilterColumn.setJson(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "objectFieldName")) {
				if (jsonParserFieldValue != null) {
					objectViewFilterColumn.setObjectFieldName(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "valueSummary")) {
				if (jsonParserFieldValue != null) {
					objectViewFilterColumn.setValueSummary(
						(String)jsonParserFieldValue);
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