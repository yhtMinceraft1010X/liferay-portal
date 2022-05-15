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

package com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_3;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Rafael Praxedes
 */
public class DDMFormInstanceEntriesUpgradeProcess extends UpgradeProcess {

	public DDMFormInstanceEntriesUpgradeProcess(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	@Override
	protected void doUpgrade() throws Exception {
		try (PreparedStatement preparedStatement1 = connection.prepareStatement(
				StringBundler.concat(
					"select DDMContent.contentId, DDMContent.data_ from ",
					"DDMFormInstanceRecordVersion inner join DDMFormInstance ",
					"on DDMFormInstanceRecordVersion.formInstanceId = ",
					"DDMFormInstance.formInstanceId inner join DDMContent on ",
					"DDMFormInstanceRecordVersion.storageId = DDMContent.",
					"contentId"));
			PreparedStatement preparedStatement2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update DDMContent set data_ = ? where contentId = ?");
			ResultSet resultSet = preparedStatement1.executeQuery()) {

			while (resultSet.next()) {
				String data = resultSet.getString("data_");

				preparedStatement2.setString(
					1, _updateFieldValuesToLocalizable(data));

				long contentId = resultSet.getLong("contentId");

				preparedStatement2.setLong(2, contentId);

				preparedStatement2.addBatch();
			}

			preparedStatement2.executeBatch();
		}
	}

	private String _updateFieldValuesToLocalizable(String definition)
		throws PortalException {

		JSONObject ddmFormValuesJSONObject = _jsonFactory.createJSONObject(
			definition);

		String defaultLanguageId = ddmFormValuesJSONObject.getString(
			"defaultLanguageId");

		JSONArray fieldsJSONArray = ddmFormValuesJSONObject.getJSONArray(
			"fieldValues");

		for (int i = 0; i < fieldsJSONArray.length(); i++) {
			JSONObject fieldJSONObject = fieldsJSONArray.getJSONObject(i);

			Object value = fieldJSONObject.get("value");

			if (value instanceof String) {
				JSONObject localizedValueJSONObject =
					_jsonFactory.createJSONObject();

				localizedValueJSONObject.put(
					defaultLanguageId, fieldJSONObject.getString("value"));

				fieldJSONObject.put("value", localizedValueJSONObject);
			}
		}

		return ddmFormValuesJSONObject.toString();
	}

	private final JSONFactory _jsonFactory;

}