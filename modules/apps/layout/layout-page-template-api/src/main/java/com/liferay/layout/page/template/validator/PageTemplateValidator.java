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

package com.liferay.layout.page.template.validator;

import com.liferay.layout.page.template.exception.PageTemplateValidatorException;
import com.liferay.portal.json.validator.JSONValidator;
import com.liferay.portal.json.validator.JSONValidatorException;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author Rubén Pulido
 */
public class PageTemplateValidator {

	public static void validatePageTemplate(String pageTemplateJSON)
		throws PageTemplateValidatorException {

		if (Validator.isNull(pageTemplateJSON)) {
			return;
		}

		try {
			_jsonValidator.validate(pageTemplateJSON);
		}
		catch (JSONValidatorException jsonValidatorException) {
			throw new PageTemplateValidatorException(
				jsonValidatorException.getMessage(), jsonValidatorException);
		}
	}

	private static final JSONValidator _jsonValidator = new JSONValidator(
		PageTemplateValidator.class.getResourceAsStream(
			"dependencies/page_template_json_schema.json"));

}