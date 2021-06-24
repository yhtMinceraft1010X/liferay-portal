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

package com.liferay.alloy.mvc.sample.web.internal.portlet.route;

import com.liferay.alloy.mvc.AlloyFriendlyURLMapper;
import com.liferay.alloy.mvc.sample.web.internal.constants.AlloyMVCSamplePortletKeys;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.friendly-url-routes=com/liferay/alloy/mvc/alloy-friendly-url-routes.xml?controller=todo_lists",
		"javax.portlet.name=" + AlloyMVCSamplePortletKeys.ALLOY_MVC_SAMPLE,
		"name=alloy-mvc-sample-friendly-url-mapper"
	},
	service = FriendlyURLMapper.class
)
public class AlloyMVCSampleFriendlyURLMapper extends AlloyFriendlyURLMapper {

	@Override
	public String getMapping() {
		return _MAPPING;
	}

	private static final String _MAPPING = "todo";

}