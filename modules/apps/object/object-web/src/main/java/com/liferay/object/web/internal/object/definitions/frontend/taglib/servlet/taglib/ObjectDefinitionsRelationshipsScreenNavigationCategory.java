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

package com.liferay.object.web.internal.object.definitions.frontend.taglib.servlet.taglib;

import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationCategory;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.service.ObjectDefinitionService;
import com.liferay.object.web.internal.configuration.activator.FFOneToOneRelationshipConfigurationActivator;
import com.liferay.object.web.internal.object.definitions.constants.ObjectDefinitionsScreenNavigationEntryConstants;
import com.liferay.object.web.internal.object.definitions.display.context.ObjectDefinitionsRelationshipsDisplayContext;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(
	property = {
		"screen.navigation.category.order:Integer=30",
		"screen.navigation.entry.order:Integer=10"
	},
	service = {ScreenNavigationCategory.class, ScreenNavigationEntry.class}
)
public class ObjectDefinitionsRelationshipsScreenNavigationCategory
	extends BaseObjectDefinitionsScreenNavigationEntry
	implements ScreenNavigationCategory {

	@Override
	public String getCategoryKey() {
		return ObjectDefinitionsScreenNavigationEntryConstants.
			CATEGORY_KEY_RELATIONSHIPS;
	}

	@Override
	public String getEntryKey() {
		return ObjectDefinitionsScreenNavigationEntryConstants.
			ENTRY_KEY_RELATIONSHIPS;
	}

	@Override
	public String getJspPath() {
		return "/object_definitions/object_definition/relationships.jsp";
	}

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(locale, "relationships");
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		httpServletRequest.setAttribute(
			WebKeys.PORTLET_DISPLAY_CONTEXT,
			new ObjectDefinitionsRelationshipsDisplayContext(
				_ffOneToOneRelationshipConfigurationActivator,
				httpServletRequest, _objectDefinitionModelResourcePermission,
				_objectDefinitionService));

		super.render(httpServletRequest, httpServletResponse);
	}

	@Reference
	private FFOneToOneRelationshipConfigurationActivator
		_ffOneToOneRelationshipConfigurationActivator;

	@Reference(
		target = "(model.class.name=com.liferay.object.model.ObjectDefinition)"
	)
	private ModelResourcePermission<ObjectDefinition>
		_objectDefinitionModelResourcePermission;

	@Reference
	private ObjectDefinitionService _objectDefinitionService;

}