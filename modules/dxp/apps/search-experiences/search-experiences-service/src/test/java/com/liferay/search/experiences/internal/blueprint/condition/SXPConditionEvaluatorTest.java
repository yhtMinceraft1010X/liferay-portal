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

package com.liferay.search.experiences.internal.blueprint.condition;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.search.experiences.blueprint.parameter.BooleanSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.DateSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.DoubleSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.FloatSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerArraySXPParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.LongArraySXPParameter;
import com.liferay.search.experiences.blueprint.parameter.LongSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameter;
import com.liferay.search.experiences.blueprint.parameter.StringArraySXPParameter;
import com.liferay.search.experiences.blueprint.parameter.StringSXPParameter;
import com.liferay.search.experiences.internal.blueprint.parameter.SXPParameterData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Petteri Karttunen
 */
public class SXPConditionEvaluatorTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testContains() throws Exception {
		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("contains", "integer_array", 1),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("contains", "long_array", 1L),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("contains", "string_array", "one"),
				_sxpParameterData));
	}

	@Test
	public void testEquals() throws Exception {
		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("equals", "boolean", true),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				JSONUtil.put(
					"equals",
					JSONUtil.put(
						"date_format", "yyyyMMdd"
					).put(
						"parameter_name", "${date}"
					).put(
						"value", _getNowAsString("yyyyMMdd")
					)),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("equals", "double", 1.0D),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("equals", "float", 1.0F),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("equals", "integer", 1),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("equals", "long", 1L),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("equals", "string", "one"),
				_sxpParameterData));
	}

	@Test
	public void testNotContains() throws Exception {
		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("not_contains", "integer_array", 4),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("not_contains", "long_array", 4L),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject(
					"not_contains", "string_array", "four"),
				_sxpParameterData));
	}

	@Test
	public void testNotEquals() throws Exception {
		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("not_equals", "boolean", false),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				JSONUtil.put(
					"not_equals",
					JSONUtil.put(
						"date_format", "yyyyMMdd"
					).put(
						"parameter_name", "${date}"
					).put(
						"value", _getNowAsString("yyyyMMddmmss")
					)),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("not_equals", "double", 2.0D),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("not_equals", "float", 2.0F),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("not_equals", "integer", 2),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("not_equals", "long", 2L),
				_sxpParameterData));

		Assert.assertTrue(
			SXPConditionEvaluator.evaluate(
				_createConditionJSONObject("not_equals", "string", "two"),
				_sxpParameterData));
	}

	private JSONObject _createConditionJSONObject(
		String evaluationType, String parameterName, Object value) {

		return JSONUtil.put(
			evaluationType,
			JSONUtil.put(
				"parameter_name", "${" + parameterName + "}"
			).put(
				"value", value
			));
	}

	private String _getNowAsString(String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);

		return dateFormat.format(new Date());
	}

	private final SXPParameterData _sxpParameterData = new SXPParameterData(
		"test",
		SetUtil.fromArray(
			new SXPParameter[] {
				new BooleanSXPParameter("boolean", true, true),
				new DateSXPParameter("date", true, new Date()),
				new DoubleSXPParameter("double", true, 1.0D),
				new FloatSXPParameter("float", true, 1.0F),
				new IntegerSXPParameter("integer", true, 1),
				new IntegerArraySXPParameter(
					"integer_array", true, new Integer[] {1, 2, 3}),
				new LongArraySXPParameter(
					"long_array", true, new Long[] {1L, 2L, 3L}),
				new LongSXPParameter("long", true, 1L),
				new StringArraySXPParameter(
					"string_array", true, new String[] {"one", "two", "three"}),
				new StringSXPParameter("string", true, "one")
			}));

}