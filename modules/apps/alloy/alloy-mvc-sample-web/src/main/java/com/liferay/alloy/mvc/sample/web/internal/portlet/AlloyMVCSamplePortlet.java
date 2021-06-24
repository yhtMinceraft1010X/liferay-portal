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

package com.liferay.alloy.mvc.sample.web.internal.portlet;

import com.liferay.alloy.mvc.AlloyPortlet;
import com.liferay.alloy.mvc.sample.web.internal.constants.AlloyMVCSamplePortletKeys;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.tools",
		"com.liferay.portlet.requires-namespaced-parameters=false",
		"javax.portlet.display-name=Alloy MVC Sample",
		"javax.portlet.info.keywords=Alloy MVC Sample",
		"javax.portlet.info.short-title=Alloy MVC Sample",
		"javax.portlet.info.title=Alloy MVC Sample",
		"javax.portlet.mime-type=text/html",
		"javax.portlet.name=" + AlloyMVCSamplePortletKeys.ALLOY_MVC_SAMPLE,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=administrator,guest,power-user,user"
	},
	service = Portlet.class
)
public class AlloyMVCSamplePortlet extends AlloyPortlet {

	@Override
	@Reference(
		target = "(name=alloy-mvc-sample-friendly-url-mapper)", unbind = "-"
	)
	protected void setFriendlyURLMapper(FriendlyURLMapper friendlyURLMapper) {
		this.friendlyURLMapper = friendlyURLMapper;
	}

}