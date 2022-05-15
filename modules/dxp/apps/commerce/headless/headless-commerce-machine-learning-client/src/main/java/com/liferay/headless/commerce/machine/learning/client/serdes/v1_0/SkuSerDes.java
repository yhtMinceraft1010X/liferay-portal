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

import com.liferay.headless.commerce.machine.learning.client.dto.v1_0.Sku;
import com.liferay.headless.commerce.machine.learning.client.json.BaseJSONParser;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
public class SkuSerDes {

	public static Sku toDTO(String json) {
		SkuJSONParser skuJSONParser = new SkuJSONParser();

		return skuJSONParser.parseToDTO(json);
	}

	public static Sku[] toDTOs(String json) {
		SkuJSONParser skuJSONParser = new SkuJSONParser();

		return skuJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Sku sku) {
		if (sku == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (sku.getCost() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"cost\": ");

			sb.append(sku.getCost());
		}

		if (sku.getDiscontinued() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"discontinued\": ");

			sb.append(sku.getDiscontinued());
		}

		if (sku.getDisplayDate() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"displayDate\": ");

			sb.append("\"");

			sb.append(liferayToJSONDateFormat.format(sku.getDisplayDate()));

			sb.append("\"");
		}

		if (sku.getExpirationDate() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"expirationDate\": ");

			sb.append("\"");

			sb.append(liferayToJSONDateFormat.format(sku.getExpirationDate()));

			sb.append("\"");
		}

		if (sku.getExternalReferenceCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"externalReferenceCode\": ");

			sb.append("\"");

			sb.append(_escape(sku.getExternalReferenceCode()));

			sb.append("\"");
		}

		if (sku.getGtin() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"gtin\": ");

			sb.append("\"");

			sb.append(_escape(sku.getGtin()));

			sb.append("\"");
		}

		if (sku.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(sku.getId());
		}

		if (sku.getManufacturerPartNumber() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"manufacturerPartNumber\": ");

			sb.append("\"");

			sb.append(_escape(sku.getManufacturerPartNumber()));

			sb.append("\"");
		}

		if (sku.getPublished() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"published\": ");

			sb.append(sku.getPublished());
		}

		if (sku.getPurchasable() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"purchasable\": ");

			sb.append(sku.getPurchasable());
		}

		if (sku.getSku() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"sku\": ");

			sb.append("\"");

			sb.append(_escape(sku.getSku()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		SkuJSONParser skuJSONParser = new SkuJSONParser();

		return skuJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Sku sku) {
		if (sku == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (sku.getCost() == null) {
			map.put("cost", null);
		}
		else {
			map.put("cost", String.valueOf(sku.getCost()));
		}

		if (sku.getDiscontinued() == null) {
			map.put("discontinued", null);
		}
		else {
			map.put("discontinued", String.valueOf(sku.getDiscontinued()));
		}

		if (sku.getDisplayDate() == null) {
			map.put("displayDate", null);
		}
		else {
			map.put(
				"displayDate",
				liferayToJSONDateFormat.format(sku.getDisplayDate()));
		}

		if (sku.getExpirationDate() == null) {
			map.put("expirationDate", null);
		}
		else {
			map.put(
				"expirationDate",
				liferayToJSONDateFormat.format(sku.getExpirationDate()));
		}

		if (sku.getExternalReferenceCode() == null) {
			map.put("externalReferenceCode", null);
		}
		else {
			map.put(
				"externalReferenceCode",
				String.valueOf(sku.getExternalReferenceCode()));
		}

		if (sku.getGtin() == null) {
			map.put("gtin", null);
		}
		else {
			map.put("gtin", String.valueOf(sku.getGtin()));
		}

		if (sku.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(sku.getId()));
		}

		if (sku.getManufacturerPartNumber() == null) {
			map.put("manufacturerPartNumber", null);
		}
		else {
			map.put(
				"manufacturerPartNumber",
				String.valueOf(sku.getManufacturerPartNumber()));
		}

		if (sku.getPublished() == null) {
			map.put("published", null);
		}
		else {
			map.put("published", String.valueOf(sku.getPublished()));
		}

		if (sku.getPurchasable() == null) {
			map.put("purchasable", null);
		}
		else {
			map.put("purchasable", String.valueOf(sku.getPurchasable()));
		}

		if (sku.getSku() == null) {
			map.put("sku", null);
		}
		else {
			map.put("sku", String.valueOf(sku.getSku()));
		}

		return map;
	}

	public static class SkuJSONParser extends BaseJSONParser<Sku> {

		@Override
		protected Sku createDTO() {
			return new Sku();
		}

		@Override
		protected Sku[] createDTOArray(int size) {
			return new Sku[size];
		}

		@Override
		protected void setField(
			Sku sku, String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "cost")) {
				if (jsonParserFieldValue != null) {
					sku.setCost(new BigDecimal((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "discontinued")) {
				if (jsonParserFieldValue != null) {
					sku.setDiscontinued((Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "displayDate")) {
				if (jsonParserFieldValue != null) {
					sku.setDisplayDate(toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "expirationDate")) {
				if (jsonParserFieldValue != null) {
					sku.setExpirationDate(toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "externalReferenceCode")) {

				if (jsonParserFieldValue != null) {
					sku.setExternalReferenceCode((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "gtin")) {
				if (jsonParserFieldValue != null) {
					sku.setGtin((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					sku.setId(Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "manufacturerPartNumber")) {

				if (jsonParserFieldValue != null) {
					sku.setManufacturerPartNumber((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "published")) {
				if (jsonParserFieldValue != null) {
					sku.setPublished((Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "purchasable")) {
				if (jsonParserFieldValue != null) {
					sku.setPurchasable((Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sku")) {
				if (jsonParserFieldValue != null) {
					sku.setSku((String)jsonParserFieldValue);
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