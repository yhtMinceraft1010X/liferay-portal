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

package com.liferay.layout.type.controller.content.internal.servlet.taglib.ui;

import com.liferay.layout.security.permission.resource.LayoutContentModelResourcePermission;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.servlet.taglib.BaseJSPDynamicInclude;
import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Chema Balsas
 */
@Component(service = DynamicInclude.class)
public class LayoutEditorToolbarControlMenuJSPDynamicInclude
	extends BaseJSPDynamicInclude {

	@Override
	public void include(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String key)
		throws IOException {

		try {
			if (!isShow(httpServletRequest)) {
				return;
			}
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}

		super.include(httpServletRequest, httpServletResponse, key);
	}

	public boolean isShow(HttpServletRequest httpServletRequest)
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		if (layout.isSystem() && layout.isTypeContent() &&
			!_isConversionLayout(layout)) {

			layout = _layoutLocalService.getLayout(layout.getClassPK());
		}

		if (!layout.isTypeAssetDisplay() && !layout.isTypeContent()) {
			return false;
		}

		String mode = ParamUtil.getString(
			httpServletRequest, "p_l_mode", Constants.VIEW);

		if (!Objects.equals(mode, Constants.EDIT) ||
			(!LayoutPermissionUtil.containsLayoutUpdatePermission(
				themeDisplay.getPermissionChecker(), layout) &&
			 !_modelResourcePermission.contains(
				 themeDisplay.getPermissionChecker(), layout.getPlid(),
				 ActionKeys.UPDATE))) {

			return false;
		}

		return true;
	}

	@Override
	public void register(DynamicIncludeRegistry dynamicIncludeRegistry) {
		dynamicIncludeRegistry.register(
			"com.liferay.product.navigation.taglib#/page.jsp#post");
	}

	@Override
	protected String getJspPath() {
		return "/dynamic_include/entry.jsp";
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.layout.type.controller.content)",
		unbind = "-"
	)
	protected void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

	private boolean _isConversionLayout(Layout layout) throws PortalException {
		Layout publishedLayout = _layoutLocalService.getLayout(
			layout.getClassPK());

		if (layout.isTypeContent() &&
			Objects.equals(
				publishedLayout.getType(), LayoutConstants.TYPE_PORTLET)) {

			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutEditorToolbarControlMenuJSPDynamicInclude.class);

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutContentModelResourcePermission _modelResourcePermission;

}