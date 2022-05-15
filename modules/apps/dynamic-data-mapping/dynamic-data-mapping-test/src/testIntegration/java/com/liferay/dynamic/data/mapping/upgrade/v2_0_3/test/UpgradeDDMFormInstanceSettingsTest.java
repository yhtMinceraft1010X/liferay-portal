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

package com.liferay.dynamic.data.mapping.upgrade.v2_0_3.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalServiceUtil;
import com.liferay.dynamic.data.mapping.storage.StorageType;
import com.liferay.dynamic.data.mapping.test.util.DDMFormInstanceTestUtil;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Adam Brandizzi
 */
@RunWith(Arquillian.class)
public class UpgradeDDMFormInstanceSettingsTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_jsonFactory = new JSONFactoryImpl();

		setUpUpgradeDDMFormInstanceSettings();
	}

	@Test
	public void testAddRequireAuthenticationSetting() throws Exception {
		String settings = createSettings(false);

		DDMFormInstance formInstance = createFormInstance(settings);

		JSONArray fieldValuesJSONArray = getFieldValuesJSONArray(
			formInstance.getSettings());

		Assert.assertFalse(
			containsField(fieldValuesJSONArray, "requireAuthentication"));

		_ddmFormInstanceSettingsUpgradeProcess.upgrade();

		formInstance = getRecordSet(formInstance);

		Assert.assertTrue(
			containsField(
				getFieldValuesJSONArray(formInstance.getSettings()),
				"requireAuthentication"));
	}

	@Test
	public void testEnableAutosaveSetting() throws Exception {
		String settings = createSettings(false);

		DDMFormInstance formInstance = createFormInstance(settings);

		JSONArray fieldValuesJSONArray = getFieldValuesJSONArray(
			formInstance.getSettings());

		Assert.assertFalse(
			containsField(fieldValuesJSONArray, "autosaveEnabled"));

		_ddmFormInstanceSettingsUpgradeProcess.upgrade();

		formInstance = getRecordSet(formInstance);

		Assert.assertTrue(
			containsField(
				getFieldValuesJSONArray(formInstance.getSettings()),
				"autosaveEnabled"));
	}

	protected boolean containsField(
		JSONArray fieldValuesJSONArray, String field) {

		for (int i = 0; i < fieldValuesJSONArray.length(); i++) {
			JSONObject fieldValueJSONObject =
				fieldValuesJSONArray.getJSONObject(i);

			String fieldName = fieldValueJSONObject.getString("name");

			if (fieldName.equals(field)) {
				return true;
			}
		}

		return false;
	}

	protected JSONArray createFieldValues(boolean hasSetting) {
		JSONArray jsonArray = _jsonFactory.createJSONArray();

		jsonArray.put(
			getFieldValueJSONObject("requireCaptcha", "false")
		).put(
			getFieldValueJSONObject("redirectURL", "")
		).put(
			getFieldValueJSONObject(
				"storageType", StorageType.DEFAULT.getValue())
		).put(
			getFieldValueJSONObject("workflowDefinition", "")
		).put(
			getFieldValueJSONObject("sendEmailNotification", "false")
		).put(
			getFieldValueJSONObject("emailFromName", "")
		).put(
			getFieldValueJSONObject("emailFromAddress", "")
		).put(
			getFieldValueJSONObject("emailToAddress", "")
		).put(
			getFieldValueJSONObject("emailSubject", "")
		).put(
			getFieldValueJSONObject("published", "false")
		);

		if (hasSetting) {
			jsonArray.put(
				getFieldValueJSONObject("autosaveEnabled", "false")
			).put(
				getFieldValueJSONObject("requireAuthentication", "false")
			);
		}

		return jsonArray;
	}

	protected DDMFormInstance createFormInstance(String settings)
		throws Exception {

		DDMFormInstance ddmFormInstance =
			DDMFormInstanceTestUtil.addDDMFormInstance(
				_group, TestPropsValues.getUserId());

		ddmFormInstance.setSettings(settings);

		DDMFormInstanceLocalServiceUtil.updateDDMFormInstance(ddmFormInstance);

		return DDMFormInstanceLocalServiceUtil.getFormInstance(
			ddmFormInstance.getFormInstanceId());
	}

	protected String createSettings(boolean hasSetting) {
		JSONObject jsonObject = _jsonFactory.createJSONObject();

		jsonObject.put(
			"availableLanguageIdss", getAvailableLanguagesJSONArray("en_US")
		).put(
			"defaultLanguageId", "en_US"
		);

		JSONArray fieldValuesJSONArray = createFieldValues(hasSetting);

		jsonObject.put("fieldValues", fieldValuesJSONArray);

		return jsonObject.toString();
	}

	protected JSONArray getAvailableLanguagesJSONArray(String languageId) {
		JSONArray jsonArray = _jsonFactory.createJSONArray();

		jsonArray.put(languageId);

		return jsonArray;
	}

	protected JSONObject getFieldValueJSONObject(String name, String value) {
		JSONObject jsonObject = _jsonFactory.createJSONObject();

		jsonObject.put(
			"instanceId", RandomTestUtil.randomString()
		).put(
			"name", name
		).put(
			"value", value
		);

		return jsonObject;
	}

	protected JSONArray getFieldValuesJSONArray(String settings)
		throws JSONException {

		JSONObject settingsJSONObject = _jsonFactory.createJSONObject(settings);

		return settingsJSONObject.getJSONArray("fieldValues");
	}

	protected DDMFormInstance getRecordSet(DDMFormInstance formInstance)
		throws PortalException {

		EntityCacheUtil.clearCache();

		return DDMFormInstanceLocalServiceUtil.getDDMFormInstance(
			formInstance.getFormInstanceId());
	}

	protected void setUpUpgradeDDMFormInstanceSettings() {
		_upgradeStepRegistrator.register(
			new UpgradeStepRegistrator.Registry() {

				@Override
				public void register(
					String fromSchemaVersionString,
					String toSchemaVersionString, UpgradeStep... upgradeSteps) {

					for (UpgradeStep upgradeStep : upgradeSteps) {
						Class<?> clazz = upgradeStep.getClass();

						String className = clazz.getName();

						if (className.contains(_CLASS_NAME)) {
							_ddmFormInstanceSettingsUpgradeProcess =
								(UpgradeProcess)upgradeStep;
						}
					}
				}

				@Override
				public void registerInitialUpgradeSteps(
					UpgradeStep... upgradeSteps) {
				}

			});
	}

	private static final String _CLASS_NAME =
		"com.liferay.dynamic.data.mapping.internal.upgrade.v2_0_3." +
			"DDMFormInstanceSettingsUpgradeProcess";

	@Inject(
		filter = "(&(objectClass=com.liferay.dynamic.data.mapping.internal.upgrade.DDMServiceUpgrade))"
	)
	private static UpgradeStepRegistrator _upgradeStepRegistrator;

	private UpgradeProcess _ddmFormInstanceSettingsUpgradeProcess;

	@DeleteAfterTestRun
	private Group _group;

	private JSONFactory _jsonFactory;

}