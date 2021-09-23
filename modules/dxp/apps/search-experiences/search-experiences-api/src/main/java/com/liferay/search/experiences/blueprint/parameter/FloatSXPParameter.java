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
import com.liferay.portal.kernel.util.GetterUtil;

/**
 * @author Petteri Karttunen
 */
public class FloatSXPParameter extends BaseSXPParameter {

	public FloatSXPParameter(
		String name, boolean templateVariable, Float value) {

		super(name, templateVariable);

		_value = value;
	}

	@Override
	public boolean evaluateEquals(JSONObject jsonObject) {
		float value = GetterUtil.getFloat(jsonObject.get("value"));

		if (_value.floatValue() == value) {
			return true;
		}

		return false;
	}

	@Override
	public boolean evaluateGreaterThan(
		boolean closedRange, JSONObject jsonObject) {

		float value = GetterUtil.getFloat(jsonObject.get("value"));

		if (closedRange) {
			if (_value.compareTo(value) >= 0) {
				return true;
			}

			return false;
		}
		else if (_value.compareTo(value) > 0) {
			return true;
		}

		return false;
	}

	@Override
	public Float getValue() {
		return _value;
	}

	private final Float _value;

}