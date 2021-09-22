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

package com.liferay.search.experiences.blueprint.parameter;

import com.liferay.portal.kernel.json.JSONObject;

import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class LongSXPParameter extends BaseSXPParameter {

	public LongSXPParameter(String name, boolean templateVariable, Long value) {
		super(name, templateVariable);

		_value = value;
	}

	@Override
	public boolean evaluateEquals(JSONObject jsonObject) {
		long value = jsonObject.getLong("value");

		if (_value.longValue() == value) {
			return true;
		}

		return false;
	}

	@Override
	public String evaluateTemplateVariable(Map<String, String> options) {
		return null;
	}

	@Override
	public Long getValue() {
		return _value;
	}

	private final Long _value;

}