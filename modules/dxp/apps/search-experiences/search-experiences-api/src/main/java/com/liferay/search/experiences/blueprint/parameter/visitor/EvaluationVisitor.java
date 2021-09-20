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
import com.liferay.search.experiences.blueprint.parameter.exception.SXPParameterException;

/**
 * @author Petteri Karttunen
 */
public interface EvaluationVisitor {

	public boolean visit(BooleanParameter booleanParameter)
		throws SXPParameterException;

	public boolean visit(DateParameter dateParameter)
		throws SXPParameterException;

	public boolean visit(DoubleParameter doubleParameter)
		throws SXPParameterException;

	public boolean visit(FloatParameter floatParameter)
		throws SXPParameterException;

	public boolean visit(IntegerArrayParameter integerArrayParameter)
		throws SXPParameterException;

	public boolean visit(IntegerParameter integerParameter)
		throws SXPParameterException;

	public boolean visit(LongArrayParameter longArrayParameter)
		throws SXPParameterException;

	public boolean visit(LongParameter longParameter)
		throws SXPParameterException;

	public boolean visit(StringArrayParameter stringArrayParameter)
		throws SXPParameterException;

	public boolean visit(StringParameter stringParameter)
		throws SXPParameterException;

}