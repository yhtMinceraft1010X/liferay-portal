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

import com.liferay.search.experiences.blueprint.parameter.BooleanSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.DateSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.DoubleSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.FloatSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerArraySXPParameter;
import com.liferay.search.experiences.blueprint.parameter.IntegerSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.LongArraySXPParameter;
import com.liferay.search.experiences.blueprint.parameter.LongSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.StringArraySXPParameter;
import com.liferay.search.experiences.blueprint.parameter.StringSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.exception.SXPParameterException;

/**
 * @author Petteri Karttunen
 */
public interface EvaluationVisitor {

	public boolean visit(BooleanSXPParameter booleanSXPParameter)
		throws SXPParameterException;

	public boolean visit(DateSXPParameter dateSXPParameter)
		throws SXPParameterException;

	public boolean visit(DoubleSXPParameter doubleSXPParameter)
		throws SXPParameterException;

	public boolean visit(FloatSXPParameter floatSXPParameter)
		throws SXPParameterException;

	public boolean visit(IntegerArraySXPParameter integerArraySXPParameter)
		throws SXPParameterException;

	public boolean visit(IntegerSXPParameter integerSXPParameter)
		throws SXPParameterException;

	public boolean visit(LongArraySXPParameter longArraySXPParameter)
		throws SXPParameterException;

	public boolean visit(LongSXPParameter longSXPParameter)
		throws SXPParameterException;

	public boolean visit(StringArraySXPParameter stringArraySXPParameter)
		throws SXPParameterException;

	public boolean visit(StringSXPParameter stringSXPParameter)
		throws SXPParameterException;

}