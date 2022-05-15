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

package com.liferay.layout.type.controller.display.page.internal.layout.type.controller;

import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.info.display.request.attributes.contributor.InfoDisplayRequestAttributesContributor;
import com.liferay.info.item.InfoItemServiceTracker;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorWebKeys;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.type.controller.BaseLayoutTypeControllerImpl;
import com.liferay.layout.type.controller.display.page.internal.constants.DisplayPageLayoutTypeControllerWebKeys;
import com.liferay.layout.type.controller.display.page.internal.display.context.DisplayPageLayoutTypeControllerDisplayContext;
import com.liferay.petra.io.unsync.UnsyncStringWriter;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutTypeController;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.servlet.PipingServletResponse;
import com.liferay.portal.kernel.servlet.TransferHeadersHelper;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Juergen Kappler
 */
@Component(
	immediate = true,
	property = "layout.type=" + LayoutConstants.TYPE_ASSET_DISPLAY,
	service = LayoutTypeController.class
)
public class DisplayPageLayoutTypeController
	extends BaseLayoutTypeControllerImpl {

	@Override
	public String getFriendlyURL(
			HttpServletRequest httpServletRequest, Layout layout)
		throws PortalException {

		if (layout.isDraftLayout()) {
			return null;
		}

		Object object = httpServletRequest.getAttribute(
			WebKeys.LAYOUT_ASSET_ENTRY);

		if ((object != null) && (object instanceof AssetEntry)) {
			AssetEntry assetEntry = (AssetEntry)object;

			ThemeDisplay themeDisplay =
				(ThemeDisplay)httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			return _assetDisplayPageFriendlyURLProvider.getFriendlyURL(
				assetEntry.getClassName(), assetEntry.getClassPK(),
				themeDisplay);
		}

		return null;
	}

	@Override
	public String getType() {
		return LayoutConstants.TYPE_PORTLET;
	}

	@Override
	public String getURL() {
		return _URL;
	}

	@Override
	public String includeEditContent(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, Layout layout) {

		return StringPool.BLANK;
	}

	@Override
	public boolean includeLayoutContent(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Layout layout)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (layout.isDraftLayout()) {
			Layout curLayout = _layoutLocalService.fetchLayout(
				layout.getClassPK());

			if (!_hasUpdatePermissions(
					themeDisplay.getPermissionChecker(), curLayout)) {

				throw new PrincipalException.MustHavePermission(
					themeDisplay.getPermissionChecker(), Layout.class.getName(),
					layout.getLayoutId(), ActionKeys.UPDATE);
			}
		}

		String layoutMode = ParamUtil.getString(
			httpServletRequest, "p_l_mode", Constants.VIEW);

		if (layoutMode.equals(Constants.EDIT) &&
			!_hasUpdatePermissions(
				themeDisplay.getPermissionChecker(), layout)) {

			layoutMode = Constants.VIEW;
		}

		DisplayPageLayoutTypeControllerDisplayContext
			displayPageLayoutTypeControllerDisplayContext =
				new DisplayPageLayoutTypeControllerDisplayContext(
					httpServletRequest, _infoItemServiceTracker);

		httpServletRequest.setAttribute(
			DisplayPageLayoutTypeControllerWebKeys.
				DISPLAY_PAGE_LAYOUT_TYPE_CONTROLLER_DISPLAY_CONTEXT,
			displayPageLayoutTypeControllerDisplayContext);

		String page = getViewPage();

		if (layoutMode.equals(Constants.EDIT)) {
			page = _EDIT_PAGE;
		}

		RequestDispatcher requestDispatcher =
			_transferHeadersHelper.getTransferHeadersRequestDispatcher(
				servletContext.getRequestDispatcher(page));

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		ServletResponse servletResponse = createServletResponse(
			httpServletResponse, unsyncStringWriter);

		String includeServletPath = (String)httpServletRequest.getAttribute(
			RequestDispatcher.INCLUDE_SERVLET_PATH);

		try {
			LayoutPageTemplateEntry layoutPageTemplateEntry =
				_fetchLayoutPageTemplateEntry(layout);

			if (layoutPageTemplateEntry != null) {
				httpServletRequest.setAttribute(
					ContentPageEditorWebKeys.CLASS_NAME,
					LayoutPageTemplateEntry.class.getName());

				httpServletRequest.setAttribute(
					ContentPageEditorWebKeys.CLASS_PK,
					layoutPageTemplateEntry.getLayoutPageTemplateEntryId());
			}

			addAttributes(httpServletRequest);

			requestDispatcher.include(httpServletRequest, servletResponse);
		}
		finally {
			removeAttributes(httpServletRequest);

			httpServletRequest.setAttribute(
				RequestDispatcher.INCLUDE_SERVLET_PATH, includeServletPath);
		}

		httpServletRequest.setAttribute(
			WebKeys.LAYOUT_CONTENT, unsyncStringWriter.getStringBundler());

		String contentType = servletResponse.getContentType();

		if (contentType != null) {
			httpServletResponse.setContentType(contentType);
		}

		if (!displayPageLayoutTypeControllerDisplayContext.hasPermission(
				themeDisplay.getPermissionChecker(), ActionKeys.VIEW)) {

			if (themeDisplay.isSignedIn()) {
				httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}
			else {
				String signInURL = themeDisplay.getURLSignIn();

				httpServletResponse.sendRedirect(
					HttpComponentsUtil.setParameter(
						signInURL, "redirect", themeDisplay.getURLCurrent()));
			}
		}

		return false;
	}

	@Override
	public boolean isFirstPageable() {
		return true;
	}

	@Override
	public boolean isFullPageDisplayable() {
		return true;
	}

	@Override
	public boolean isInstanceable() {
		return false;
	}

	@Override
	public boolean isParentable() {
		return false;
	}

	@Override
	public boolean isSitemapable() {
		return true;
	}

	@Override
	public boolean isURLFriendliable() {
		return true;
	}

	@Override
	protected void addAttributes(HttpServletRequest httpServletRequest) {
		for (InfoDisplayRequestAttributesContributor
				infoDisplayRequestAttributesContributor :
					_infoDisplayRequestAttributesContributors) {

			infoDisplayRequestAttributesContributor.addAttributes(
				httpServletRequest);
		}
	}

	@Override
	protected ServletResponse createServletResponse(
		HttpServletResponse httpServletResponse,
		UnsyncStringWriter unsyncStringWriter) {

		return new PipingServletResponse(
			httpServletResponse, unsyncStringWriter);
	}

	@Override
	protected String getEditPage() {
		return null;
	}

	@Override
	protected String getViewPage() {
		return _VIEW_PAGE;
	}

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.layout.type.controller.display.page)",
		unbind = "-"
	)
	protected void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	private LayoutPageTemplateEntry _fetchLayoutPageTemplateEntry(
		Layout layout) {

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_layoutPageTemplateEntryLocalService.
				fetchLayoutPageTemplateEntryByPlid(layout.getPlid());

		if (layoutPageTemplateEntry != null) {
			return layoutPageTemplateEntry;
		}

		if (layout.isDraftLayout()) {
			Layout publishedLayout = _layoutLocalService.fetchLayout(
				layout.getClassPK());

			return _layoutPageTemplateEntryLocalService.
				fetchLayoutPageTemplateEntryByPlid(publishedLayout.getPlid());
		}

		return null;
	}

	private boolean _hasUpdatePermissions(
		PermissionChecker permissionChecker, Layout layout) {

		try {
			if (LayoutPermissionUtil.containsLayoutUpdatePermission(
					permissionChecker, layout)) {

				return true;
			}
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}

		return false;
	}

	private static final String _EDIT_PAGE = "/layout/edit/display_page.jsp";

	private static final String _URL =
		"${liferay:mainPath}/portal/layout?p_l_id=${liferay:plid}" +
			"&p_v_l_s_g_id=${liferay:pvlsgid}";

	private static final String _VIEW_PAGE = "/layout/view/display_page.jsp";

	private static final Log _log = LogFactoryUtil.getLog(
		DisplayPageLayoutTypeController.class);

	@Reference
	private AssetDisplayPageFriendlyURLProvider
		_assetDisplayPageFriendlyURLProvider;

	@Reference
	private volatile List<InfoDisplayRequestAttributesContributor>
		_infoDisplayRequestAttributesContributors;

	@Reference
	private InfoItemServiceTracker _infoItemServiceTracker;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private TransferHeadersHelper _transferHeadersHelper;

}