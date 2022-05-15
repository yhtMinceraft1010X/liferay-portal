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

package com.liferay.object.web.internal.object.definitions.portlet.action;

import com.liferay.object.constants.ObjectPortletKeys;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectValidationRule;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectValidationRuleLocalService;
import com.liferay.object.validation.rule.ObjectValidationRuleEngineServicesTracker;
import com.liferay.object.web.internal.constants.ObjectWebKeys;
import com.liferay.object.web.internal.object.definitions.display.context.ObjectDefinitionsValidationsDisplayContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Selton Guedes
 */
@Component(
	property = {
		"javax.portlet.name=" + ObjectPortletKeys.OBJECT_DEFINITIONS,
		"mvc.command.name=/object_definitions/edit_object_validation_rule"
	},
	service = MVCRenderCommand.class
)
public class EditObjectValidationRuleMVCRenderCommand
	implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		try {
			ObjectValidationRule objectValidationRule =
				_objectValidationRuleLocalService.getObjectValidationRule(
					ParamUtil.getLong(renderRequest, "objectValidationRuleId"));

			renderRequest.setAttribute(
				ObjectWebKeys.OBJECT_DEFINITION,
				_objectDefinitionLocalService.getObjectDefinition(
					objectValidationRule.getObjectDefinitionId()));
			renderRequest.setAttribute(
				ObjectWebKeys.OBJECT_VALIDATION, objectValidationRule);

			renderRequest.setAttribute(
				WebKeys.PORTLET_DISPLAY_CONTEXT,
				new ObjectDefinitionsValidationsDisplayContext(
					_portal.getHttpServletRequest(renderRequest),
					_objectDefinitionModelResourcePermission,
					_objectValidationRuleEngineServicesTracker));
		}
		catch (PortalException portalException) {
			SessionErrors.add(renderRequest, portalException.getClass());
		}

		return "/object_definitions/edit_object_validation.jsp";
	}

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.object.model.ObjectDefinition)"
	)
	private ModelResourcePermission<ObjectDefinition>
		_objectDefinitionModelResourcePermission;

	@Reference
	private ObjectValidationRuleEngineServicesTracker
		_objectValidationRuleEngineServicesTracker;

	@Reference
	private ObjectValidationRuleLocalService _objectValidationRuleLocalService;

	@Reference
	private Portal _portal;

}