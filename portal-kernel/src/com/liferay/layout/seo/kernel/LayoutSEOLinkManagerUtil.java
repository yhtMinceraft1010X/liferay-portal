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

package com.liferay.layout.seo.kernel;

import com.liferay.portal.kernel.util.ServiceProxyFactory;

/**
 * @author     Cristina González
 * @deprecated As of Mueller (7.2.x), replaced by {@link LayoutSEOLinkManager}
 */
@Deprecated
public class LayoutSEOLinkManagerUtil {

	public static LayoutSEOLinkManager getLayoutSEOLinkManager() {
		return _layoutSEOLinkManager;
	}

	private static volatile LayoutSEOLinkManager _layoutSEOLinkManager =
		ServiceProxyFactory.newServiceTrackedInstance(
			LayoutSEOLinkManager.class, LayoutSEOLinkManagerUtil.class,
			"_layoutSEOLinkManager", false);

}