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

import com.liferay.headless.commerce.machine.learning.client.dto.v1_0.ProductOption;
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
public class ProductOptionSerDes {

	public static ProductOption toDTO(String json) {
		ProductOptionJSONParser productOptionJSONParser =
			new ProductOptionJSONParser();

		return productOptionJSONParser.parseToDTO(json);
	}

	public static ProductOption[] toDTOs(String json) {
		ProductOptionJSONParser productOptionJSONParser =
			new ProductOptionJSONParser();

		return productOptionJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ProductOption productOption) {
		if (productOption == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (productOption.getKey() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"key\": ");

			sb.append("\"");

			sb.append(_escape(productOption.getKey()));

			sb.append("\"");
		}

		if (productOption.getOptionKey() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"optionKey\": ");

			sb.append("\"");

			sb.append(_escape(productOption.getOptionKey()));

			sb.append("\"");
		}

		if (productOption.getValues() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"values\": ");

			sb.append("[");

			for (int i = 0; i < productOption.getValues().length; i++) {
				sb.append(productOption.getValues()[i]);

				if ((i + 1) < productOption.getValues().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ProductOptionJSONParser productOptionJSONParser =
			new ProductOptionJSONParser();

		return productOptionJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(ProductOption productOption) {
		if (productOption == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (productOption.getKey() == null) {
			map.put("key", null);
		}
		else {
			map.put("key", String.valueOf(productOption.getKey()));
		}

		if (productOption.getOptionKey() == null) {
			map.put("optionKey", null);
		}
		else {
			map.put("optionKey", String.valueOf(productOption.getOptionKey()));
		}

		if (productOption.getValues() == null) {
			map.put("values", null);
		}
		else {
			map.put("values", String.valueOf(productOption.getValues()));
		}

		return map;
	}

	public static class ProductOptionJSONParser
		extends BaseJSONParser<ProductOption> {

		@Override
		protected ProductOption createDTO() {
			return new ProductOption();
		}

		@Override
		protected ProductOption[] createDTOArray(int size) {
			return new ProductOption[size];
		}

		@Override
		protected void setField(
			ProductOption productOption, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "key")) {
				if (jsonParserFieldValue != null) {
					productOption.setKey((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "optionKey")) {
				if (jsonParserFieldValue != null) {
					productOption.setOptionKey((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "values")) {
				if (jsonParserFieldValue != null) {
					productOption.setValues((Map[])jsonParserFieldValue);
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