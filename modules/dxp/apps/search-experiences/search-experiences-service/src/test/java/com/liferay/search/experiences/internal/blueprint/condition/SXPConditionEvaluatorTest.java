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
		Assert.assertTrue(_evaluate("contains", "integer_array", 1));
		Assert.assertTrue(_evaluate("contains", "long_array", 1L));
		Assert.assertTrue(_evaluate("contains", "string_array", "one"));
	}

	@Test
	public void testEquals() throws Exception {
		Assert.assertTrue(_evaluate("equals", "boolean", true));
		Assert.assertTrue(_evaluate("equals", "double", 1.0D));
		Assert.assertTrue(_evaluate("equals", "float", 1.0F));
		Assert.assertTrue(_evaluate("equals", "integer", 1));
		Assert.assertTrue(_evaluate("equals", "long", 1L));
		Assert.assertTrue(_evaluate("equals", "string", "one"));
		Assert.assertTrue(
			_evaluate(
				JSONUtil.put(
					"equals",
					JSONUtil.put(
						"date_format", "yyyyMMdd"
					).put(
						"parameter_name", "${date}"
					).put(
						"value", _toDateString("yyyyMMdd")
					))));
	}

	@Test
	public void testNotContains() throws Exception {
		Assert.assertTrue(_evaluate("not_contains", "integer_array", 4));
		Assert.assertTrue(_evaluate("not_contains", "long_array", 4L));
		Assert.assertTrue(_evaluate("not_contains", "string_array", "four"));
	}

	@Test
	public void testNotEquals() throws Exception {
		Assert.assertTrue(_evaluate("not_equals", "boolean", false));
		Assert.assertTrue(
			_evaluate(
				JSONUtil.put(
					"not_equals",
					JSONUtil.put(
						"date_format", "yyyyMMdd"
					).put(
						"parameter_name", "${date}"
					).put(
						"value", _toDateString("yyyyMMddmmss")
					))));
		Assert.assertTrue(_evaluate("not_equals", "double", 2.0D));
		Assert.assertTrue(_evaluate("not_equals", "float", 2.0F));
		Assert.assertTrue(_evaluate("not_equals", "integer", 2));
		Assert.assertTrue(_evaluate("not_equals", "long", 2L));
		Assert.assertTrue(_evaluate("not_equals", "string", "two"));
	}

	private boolean _evaluate(JSONObject jsonObject) {
		return SXPConditionEvaluator.evaluate(jsonObject, _sxpParameterData);
	}

	private boolean _evaluate(String key, String parameterName, Object value) {
		return _evaluate(
			JSONUtil.put(
				key,
				JSONUtil.put(
					"parameter_name", "${" + parameterName + "}"
				).put(
					"value", value
				)));
	}

	private String _toDateString(String pattern) {
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