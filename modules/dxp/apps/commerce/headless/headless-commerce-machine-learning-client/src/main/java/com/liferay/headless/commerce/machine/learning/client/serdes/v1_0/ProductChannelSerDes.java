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

package com.liferay.headless.commerce.machine.learning.client.serdes.v1_0;

import com.liferay.headless.commerce.machine.learning.client.dto.v1_0.ProductChannel;
import com.liferay.headless.commerce.machine.learning.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Riccardo Ferrari
 * @generated
 */
@Generated("")
public class ProductChannelSerDes {

	public static ProductChannel toDTO(String json) {
		ProductChannelJSONParser productChannelJSONParser =
			new ProductChannelJSONParser();

		return productChannelJSONParser.parseToDTO(json);
	}

	public static ProductChannel[] toDTOs(String json) {
		ProductChannelJSONParser productChannelJSONParser =
			new ProductChannelJSONParser();

		return productChannelJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ProductChannel productChannel) {
		if (productChannel == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (productChannel.getCurrencyCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"currencyCode\": ");

			sb.append("\"");

			sb.append(_escape(productChannel.getCurrencyCode()));

			sb.append("\"");
		}

		if (productChannel.getExternalReferenceCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"externalReferenceCode\": ");

			sb.append("\"");

			sb.append(_escape(productChannel.getExternalReferenceCode()));

			sb.append("\"");
		}

		if (productChannel.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(productChannel.getId());
		}

		if (productChannel.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(productChannel.getName()));

			sb.append("\"");
		}

		if (productChannel.getType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"type\": ");

			sb.append("\"");

			sb.append(_escape(productChannel.getType()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ProductChannelJSONParser productChannelJSONParser =
			new ProductChannelJSONParser();

		return productChannelJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(ProductChannel productChannel) {
		if (productChannel == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (productChannel.getCurrencyCode() == null) {
			map.put("currencyCode", null);
		}
		else {
			map.put(
				"currencyCode",
				String.valueOf(productChannel.getCurrencyCode()));
		}

		if (productChannel.getExternalReferenceCode() == null) {
			map.put("externalReferenceCode", null);
		}
		else {
			map.put(
				"externalReferenceCode",
				String.valueOf(productChannel.getExternalReferenceCode()));
		}

		if (productChannel.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(productChannel.getId()));
		}

		if (productChannel.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(productChannel.getName()));
		}

		if (productChannel.getType() == null) {
			map.put("type", null);
		}
		else {
			map.put("type", String.valueOf(productChannel.getType()));
		}

		return map;
	}

	public static class ProductChannelJSONParser
		extends BaseJSONParser<ProductChannel> {

		@Override
		protected ProductChannel createDTO() {
			return new ProductChannel();
		}

		@Override
		protected ProductChannel[] createDTOArray(int size) {
			return new ProductChannel[size];
		}

		@Override
		protected void setField(
			ProductChannel productChannel, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "currencyCode")) {
				if (jsonParserFieldValue != null) {
					productChannel.setCurrencyCode(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "externalReferenceCode")) {

				if (jsonParserFieldValue != null) {
					productChannel.setExternalReferenceCode(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					productChannel.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					productChannel.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "type")) {
				if (jsonParserFieldValue != null) {
					productChannel.setType((String)jsonParserFieldValue);
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