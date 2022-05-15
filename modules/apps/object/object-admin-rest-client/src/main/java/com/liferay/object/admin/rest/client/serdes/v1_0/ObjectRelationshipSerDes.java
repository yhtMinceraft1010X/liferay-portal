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

import com.liferay.object.admin.rest.client.dto.v1_0.ObjectRelationship;
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
public class ObjectRelationshipSerDes {

	public static ObjectRelationship toDTO(String json) {
		ObjectRelationshipJSONParser objectRelationshipJSONParser =
			new ObjectRelationshipJSONParser();

		return objectRelationshipJSONParser.parseToDTO(json);
	}

	public static ObjectRelationship[] toDTOs(String json) {
		ObjectRelationshipJSONParser objectRelationshipJSONParser =
			new ObjectRelationshipJSONParser();

		return objectRelationshipJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ObjectRelationship objectRelationship) {
		if (objectRelationship == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (objectRelationship.getActions() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"actions\": ");

			sb.append(_toJSON(objectRelationship.getActions()));
		}

		if (objectRelationship.getDeletionType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"deletionType\": ");

			sb.append("\"");

			sb.append(objectRelationship.getDeletionType());

			sb.append("\"");
		}

		if (objectRelationship.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(objectRelationship.getId());
		}

		if (objectRelationship.getLabel() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"label\": ");

			sb.append(_toJSON(objectRelationship.getLabel()));
		}

		if (objectRelationship.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(objectRelationship.getName()));

			sb.append("\"");
		}

		if (objectRelationship.getObjectDefinitionId1() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"objectDefinitionId1\": ");

			sb.append(objectRelationship.getObjectDefinitionId1());
		}

		if (objectRelationship.getObjectDefinitionId2() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"objectDefinitionId2\": ");

			sb.append(objectRelationship.getObjectDefinitionId2());
		}

		if (objectRelationship.getObjectDefinitionName2() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"objectDefinitionName2\": ");

			sb.append("\"");

			sb.append(_escape(objectRelationship.getObjectDefinitionName2()));

			sb.append("\"");
		}

		if (objectRelationship.getReverse() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"reverse\": ");

			sb.append(objectRelationship.getReverse());
		}

		if (objectRelationship.getType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"type\": ");

			sb.append("\"");

			sb.append(objectRelationship.getType());

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ObjectRelationshipJSONParser objectRelationshipJSONParser =
			new ObjectRelationshipJSONParser();

		return objectRelationshipJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		ObjectRelationship objectRelationship) {

		if (objectRelationship == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (objectRelationship.getActions() == null) {
			map.put("actions", null);
		}
		else {
			map.put("actions", String.valueOf(objectRelationship.getActions()));
		}

		if (objectRelationship.getDeletionType() == null) {
			map.put("deletionType", null);
		}
		else {
			map.put(
				"deletionType",
				String.valueOf(objectRelationship.getDeletionType()));
		}

		if (objectRelationship.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(objectRelationship.getId()));
		}

		if (objectRelationship.getLabel() == null) {
			map.put("label", null);
		}
		else {
			map.put("label", String.valueOf(objectRelationship.getLabel()));
		}

		if (objectRelationship.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(objectRelationship.getName()));
		}

		if (objectRelationship.getObjectDefinitionId1() == null) {
			map.put("objectDefinitionId1", null);
		}
		else {
			map.put(
				"objectDefinitionId1",
				String.valueOf(objectRelationship.getObjectDefinitionId1()));
		}

		if (objectRelationship.getObjectDefinitionId2() == null) {
			map.put("objectDefinitionId2", null);
		}
		else {
			map.put(
				"objectDefinitionId2",
				String.valueOf(objectRelationship.getObjectDefinitionId2()));
		}

		if (objectRelationship.getObjectDefinitionName2() == null) {
			map.put("objectDefinitionName2", null);
		}
		else {
			map.put(
				"objectDefinitionName2",
				String.valueOf(objectRelationship.getObjectDefinitionName2()));
		}

		if (objectRelationship.getReverse() == null) {
			map.put("reverse", null);
		}
		else {
			map.put("reverse", String.valueOf(objectRelationship.getReverse()));
		}

		if (objectRelationship.getType() == null) {
			map.put("type", null);
		}
		else {
			map.put("type", String.valueOf(objectRelationship.getType()));
		}

		return map;
	}

	public static class ObjectRelationshipJSONParser
		extends BaseJSONParser<ObjectRelationship> {

		@Override
		protected ObjectRelationship createDTO() {
			return new ObjectRelationship();
		}

		@Override
		protected ObjectRelationship[] createDTOArray(int size) {
			return new ObjectRelationship[size];
		}

		@Override
		protected void setField(
			ObjectRelationship objectRelationship, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "actions")) {
				if (jsonParserFieldValue != null) {
					objectRelationship.setActions(
						(Map)ObjectRelationshipSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "deletionType")) {
				if (jsonParserFieldValue != null) {
					objectRelationship.setDeletionType(
						ObjectRelationship.DeletionType.create(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					objectRelationship.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "label")) {
				if (jsonParserFieldValue != null) {
					objectRelationship.setLabel(
						(Map)ObjectRelationshipSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					objectRelationship.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "objectDefinitionId1")) {

				if (jsonParserFieldValue != null) {
					objectRelationship.setObjectDefinitionId1(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "objectDefinitionId2")) {

				if (jsonParserFieldValue != null) {
					objectRelationship.setObjectDefinitionId2(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "objectDefinitionName2")) {

				if (jsonParserFieldValue != null) {
					objectRelationship.setObjectDefinitionName2(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "reverse")) {
				if (jsonParserFieldValue != null) {
					objectRelationship.setReverse(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "type")) {
				if (jsonParserFieldValue != null) {
					objectRelationship.setType(
						ObjectRelationship.Type.create(
							(String)jsonParserFieldValue));
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