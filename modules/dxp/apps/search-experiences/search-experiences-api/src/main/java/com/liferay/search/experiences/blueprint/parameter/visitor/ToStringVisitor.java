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

package com.liferay.search.experiences.blueprint.parameter.visitor;

import com.liferay.search.experiences.blueprint.parameter.BooleanParameter;
import com.liferay.search.experiences.blueprint.parameter.DateParameter;
import com.liferay.search.experiences.blueprint.parameter.DoubleParameter;
import com.liferay.search.experiences.blueprint.parameter.FloatParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerArrayParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerParameter;
import com.liferay.search.experiences.blueprint.parameter.LongArrayParameter;
import com.liferay.search.experiences.blueprint.parameter.LongParameter;
import com.liferay.search.experiences.blueprint.parameter.StringArrayParameter;
import com.liferay.search.experiences.blueprint.parameter.StringParameter;

import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public interface ToStringVisitor {

	public String visit(BooleanParameter parameter, Map<String, String> options)
		throws Exception;

	public String visit(DateParameter parameter, Map<String, String> options)
		throws Exception;

	public String visit(DoubleParameter parameter, Map<String, String> options)
		throws Exception;

	public String visit(FloatParameter parameter, Map<String, String> options)
		throws Exception;

	public String visit(
			IntegerArrayParameter parameter, Map<String, String> options)
		throws Exception;

	public String visit(IntegerParameter parameter, Map<String, String> options)
		throws Exception;

	public String visit(
			LongArrayParameter parameter, Map<String, String> options)
		throws Exception;

	public String visit(LongParameter parameter, Map<String, String> options)
		throws Exception;

	public String visit(
			StringArrayParameter parameter, Map<String, String> options)
		throws Exception;

	public String visit(StringParameter parameter, Map<String, String> options)
		throws Exception;

}