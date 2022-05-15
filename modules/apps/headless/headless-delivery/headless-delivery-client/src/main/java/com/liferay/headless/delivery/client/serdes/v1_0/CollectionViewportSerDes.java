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

package com.liferay.headless.delivery.client.serdes.v1_0;

import com.liferay.headless.delivery.client.dto.v1_0.CollectionViewport;
import com.liferay.headless.delivery.client.json.BaseJSONParser;

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
public class CollectionViewportSerDes {

	public static CollectionViewport toDTO(String json) {
		CollectionViewportJSONParser collectionViewportJSONParser =
			new CollectionViewportJSONParser();

		return collectionViewportJSONParser.parseToDTO(json);
	}

	public static CollectionViewport[] toDTOs(String json) {
		CollectionViewportJSONParser collectionViewportJSONParser =
			new CollectionViewportJSONParser();

		return collectionViewportJSONParser.parseToDTOs(json);
	}

	public static String toJSON(CollectionViewport collectionViewport) {
		if (collectionViewport == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (collectionViewport.getCollectionViewportDefinition() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"collectionViewportDefinition\": ");

			sb.append(
				String.valueOf(
					collectionViewport.getCollectionViewportDefinition()));
		}

		if (collectionViewport.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append("\"");

			sb.append(_escape(collectionViewport.getId()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		CollectionViewportJSONParser collectionViewportJSONParser =
			new CollectionViewportJSONParser();

		return collectionViewportJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		CollectionViewport collectionViewport) {

		if (collectionViewport == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (collectionViewport.getCollectionViewportDefinition() == null) {
			map.put("collectionViewportDefinition", null);
		}
		else {
			map.put(
				"collectionViewportDefinition",
				String.valueOf(
					collectionViewport.getCollectionViewportDefinition()));
		}

		if (collectionViewport.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(collectionViewport.getId()));
		}

		return map;
	}

	public static class CollectionViewportJSONParser
		extends BaseJSONParser<CollectionViewport> {

		@Override
		protected CollectionViewport createDTO() {
			return new CollectionViewport();
		}

		@Override
		protected CollectionViewport[] createDTOArray(int size) {
			return new CollectionViewport[size];
		}

		@Override
		protected void setField(
			CollectionViewport collectionViewport, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(
					jsonParserFieldName, "collectionViewportDefinition")) {

				if (jsonParserFieldValue != null) {
					collectionViewport.setCollectionViewportDefinition(
						CollectionViewportDefinitionSerDes.toDTO(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					collectionViewport.setId((String)jsonParserFieldValue);
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