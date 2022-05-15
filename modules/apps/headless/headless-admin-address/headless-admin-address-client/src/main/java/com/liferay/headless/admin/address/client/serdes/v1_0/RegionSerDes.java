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

package com.liferay.headless.admin.address.client.serdes.v1_0;

import com.liferay.headless.admin.address.client.dto.v1_0.Region;
import com.liferay.headless.admin.address.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Drew Brokke
 * @generated
 */
@Generated("")
public class RegionSerDes {

	public static Region toDTO(String json) {
		RegionJSONParser regionJSONParser = new RegionJSONParser();

		return regionJSONParser.parseToDTO(json);
	}

	public static Region[] toDTOs(String json) {
		RegionJSONParser regionJSONParser = new RegionJSONParser();

		return regionJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Region region) {
		if (region == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (region.getActive() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"active\": ");

			sb.append(region.getActive());
		}

		if (region.getCountryId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"countryId\": ");

			sb.append(region.getCountryId());
		}

		if (region.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(region.getId());
		}

		if (region.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(region.getName()));

			sb.append("\"");
		}

		if (region.getPosition() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"position\": ");

			sb.append(region.getPosition());
		}

		if (region.getRegionCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"regionCode\": ");

			sb.append("\"");

			sb.append(_escape(region.getRegionCode()));

			sb.append("\"");
		}

		if (region.getTitle_i18n() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"title_i18n\": ");

			sb.append(_toJSON(region.getTitle_i18n()));
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		RegionJSONParser regionJSONParser = new RegionJSONParser();

		return regionJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Region region) {
		if (region == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (region.getActive() == null) {
			map.put("active", null);
		}
		else {
			map.put("active", String.valueOf(region.getActive()));
		}

		if (region.getCountryId() == null) {
			map.put("countryId", null);
		}
		else {
			map.put("countryId", String.valueOf(region.getCountryId()));
		}

		if (region.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(region.getId()));
		}

		if (region.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(region.getName()));
		}

		if (region.getPosition() == null) {
			map.put("position", null);
		}
		else {
			map.put("position", String.valueOf(region.getPosition()));
		}

		if (region.getRegionCode() == null) {
			map.put("regionCode", null);
		}
		else {
			map.put("regionCode", String.valueOf(region.getRegionCode()));
		}

		if (region.getTitle_i18n() == null) {
			map.put("title_i18n", null);
		}
		else {
			map.put("title_i18n", String.valueOf(region.getTitle_i18n()));
		}

		return map;
	}

	public static class RegionJSONParser extends BaseJSONParser<Region> {

		@Override
		protected Region createDTO() {
			return new Region();
		}

		@Override
		protected Region[] createDTOArray(int size) {
			return new Region[size];
		}

		@Override
		protected void setField(
			Region region, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "active")) {
				if (jsonParserFieldValue != null) {
					region.setActive((Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "countryId")) {
				if (jsonParserFieldValue != null) {
					region.setCountryId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					region.setId(Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					region.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "position")) {
				if (jsonParserFieldValue != null) {
					region.setPosition(
						Double.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "regionCode")) {
				if (jsonParserFieldValue != null) {
					region.setRegionCode((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "title_i18n")) {
				if (jsonParserFieldValue != null) {
					region.setTitle_i18n(
						(Map)RegionSerDes.toMap((String)jsonParserFieldValue));
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