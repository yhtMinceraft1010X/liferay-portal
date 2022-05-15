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

package com.liferay.client.extension.web.internal.portlet;

import com.liferay.client.extension.model.ClientExtensionEntry;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.portlet.DefaultFriendlyURLMapper;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.Route;
import com.liferay.portal.kernel.portlet.Router;
import com.liferay.portlet.RouterImpl;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.WindowState;

/**
 * @author Iván Zaera Avellón
 */
public class ClientExtensionEntryFriendlyURLMapper
	extends DefaultFriendlyURLMapper implements FriendlyURLMapper {

	public ClientExtensionEntryFriendlyURLMapper(
		ClientExtensionEntry clientExtensionEntry) {

		_mapping = clientExtensionEntry.getFriendlyURLMapping();

		Router router = new RouterImpl();

		Route route = router.addRoute(StringPool.BLANK);

		route.addImplicitParameter("p_p_lifecycle", "0");
		route.addImplicitParameter("p_p_state", WindowState.NORMAL.toString());

		super.router = router;
	}

	@Override
	public String buildPath(LiferayPortletURL liferayPortletURL) {
		Map<String, String> routeParameters = new HashMap<>();

		buildRouteParameters(liferayPortletURL, routeParameters);

		String friendlyURLPath = router.parametersToUrl(routeParameters);

		if (friendlyURLPath == null) {
			return null;
		}

		addParametersIncludedInPath(liferayPortletURL, routeParameters);

		return StringBundler.concat(
			StringPool.SLASH, getMapping(), friendlyURLPath);
	}

	@Override
	public String getMapping() {
		return _mapping;
	}

	@Override
	public void setRouter(Router router) {
	}

	private final String _mapping;

}