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

package com.liferay.message.boards.web.internal.asset.model;

import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseJSPAssetRenderer;
import com.liferay.message.boards.constants.MBPortletKeys;
import com.liferay.message.boards.model.MBMessage;
import com.liferay.message.boards.service.permission.MBDiscussionPermission;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.parsers.bbcode.BBCodeTranslatorUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletLayoutFinder;
import com.liferay.portal.kernel.portlet.PortletLayoutFinderRegistryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.trash.TrashRenderer;
import com.liferay.portal.kernel.util.HtmlParser;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Julio Camarero
 * @author Juan Fernández
 * @author Sergio González
 */
public class MBMessageAssetRenderer
	extends BaseJSPAssetRenderer<MBMessage> implements TrashRenderer {

	public MBMessageAssetRenderer(
		HtmlParser htmlParser, MBMessage message,
		ModelResourcePermission<MBMessage> messageModelResourcePermission) {

		_htmlParser = htmlParser;
		_message = message;
		_messageModelResourcePermission = messageModelResourcePermission;
	}

	@Override
	public MBMessage getAssetObject() {
		return _message;
	}

	@Override
	public String getClassName() {
		return MBMessage.class.getName();
	}

	@Override
	public long getClassPK() {
		return _message.getMessageId();
	}

	@Override
	public long getGroupId() {
		return _message.getGroupId();
	}

	@Override
	public String getJspPath(
		HttpServletRequest httpServletRequest, String template) {

		if (template.equals(TEMPLATE_ABSTRACT) ||
			template.equals(TEMPLATE_FULL_CONTENT)) {

			return "/message_boards/asset/" + template + ".jsp";
		}

		return null;
	}

	@Override
	public String getPortletId() {
		AssetRendererFactory<MBMessage> assetRendererFactory =
			getAssetRendererFactory();

		return assetRendererFactory.getPortletId();
	}

	@Override
	public String getSearchSummary(Locale locale) {
		if (_message.isFormatBBCode()) {
			return _htmlParser.extractText(
				BBCodeTranslatorUtil.getHTML(_message.getBody()));
		}

		return getSummary(null, null);
	}

	@Override
	public int getStatus() {
		return _message.getStatus();
	}

	@Override
	public String getSummary(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		return _message.getBody();
	}

	@Override
	public String getTitle(Locale locale) {
		return _message.getSubject();
	}

	@Override
	public String getType() {
		return MBMessageAssetRendererFactory.TYPE;
	}

	@Override
	public PortletURL getURLEdit(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		Group group = GroupLocalServiceUtil.fetchGroup(_message.getGroupId());

		if (group.isCompany()) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)liferayPortletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			group = themeDisplay.getScopeGroup();
		}

		return PortletURLBuilder.create(
			PortalUtil.getControlPanelPortletURL(
				liferayPortletRequest, group, MBPortletKeys.MESSAGE_BOARDS, 0,
				0, PortletRequest.RENDER_PHASE)
		).setMVCRenderCommandName(
			"/message_boards/edit_message"
		).setParameter(
			"messageId", _message.getMessageId()
		).buildPortletURL();
	}

	@Override
	public String getURLView(
			LiferayPortletResponse liferayPortletResponse,
			WindowState windowState)
		throws Exception {

		AssetRendererFactory<MBMessage> assetRendererFactory =
			getAssetRendererFactory();

		return PortletURLBuilder.create(
			assetRendererFactory.getURLView(liferayPortletResponse, windowState)
		).setMVCRenderCommandName(
			"/message_boards/view_message"
		).setParameter(
			"messageId", _message.getMessageId()
		).setWindowState(
			windowState
		).buildString();
	}

	@Override
	public String getURLViewInContext(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			String noSuchEntryRedirect)
		throws Exception {

		if (_assetDisplayPageFriendlyURLProvider != null) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)liferayPortletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			String friendlyURL =
				_assetDisplayPageFriendlyURLProvider.getFriendlyURL(
					getClassName(), getClassPK(), themeDisplay);

			if (Validator.isNotNull(friendlyURL)) {
				return friendlyURL;
			}
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)liferayPortletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (!_hasViewInContextGroupLayout(
				_message.getGroupId(), themeDisplay)) {

			return null;
		}

		return getURLViewInContext(
			liferayPortletRequest, noSuchEntryRedirect,
			"/message_boards/find_message", "messageId",
			_message.getMessageId());
	}

	@Override
	public long getUserId() {
		if (_message.isAnonymous()) {
			return 0;
		}

		return _message.getUserId();
	}

	@Override
	public String getUserName() {
		if (_message.isAnonymous()) {
			return LanguageUtil.get(
				LocaleThreadLocal.getDefaultLocale(), "anonymous");
		}

		return _message.getUserName();
	}

	@Override
	public String getUuid() {
		return _message.getUuid();
	}

	@Override
	public boolean hasEditPermission(PermissionChecker permissionChecker)
		throws PortalException {

		if (_message.isDiscussion()) {
			return MBDiscussionPermission.contains(
				permissionChecker, _message, ActionKeys.UPDATE);
		}

		return _messageModelResourcePermission.contains(
			permissionChecker, _message, ActionKeys.UPDATE);
	}

	@Override
	public boolean hasViewPermission(PermissionChecker permissionChecker)
		throws PortalException {

		if (_message.isDiscussion()) {
			return MBDiscussionPermission.contains(
				permissionChecker, _message, ActionKeys.VIEW);
		}

		return _messageModelResourcePermission.contains(
			permissionChecker, _message, ActionKeys.VIEW);
	}

	@Override
	public boolean include(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String template)
		throws Exception {

		httpServletRequest.setAttribute(
			WebKeys.MESSAGE_BOARDS_MESSAGE, _message);

		return super.include(httpServletRequest, httpServletResponse, template);
	}

	@Override
	public boolean isPrintable() {
		return true;
	}

	public void setAssetDisplayPageFriendlyURLProvider(
		AssetDisplayPageFriendlyURLProvider
			assetDisplayPageFriendlyURLProvider) {

		_assetDisplayPageFriendlyURLProvider =
			assetDisplayPageFriendlyURLProvider;
	}

	private boolean _hasViewInContextGroupLayout(
		long groupId, ThemeDisplay themeDisplay) {

		try {
			Layout layout = themeDisplay.getLayout();

			if (layout.isTypeControlPanel()) {
				return true;
			}

			PortletLayoutFinder portletLayoutFinder =
				PortletLayoutFinderRegistryUtil.getPortletLayoutFinder(
					getClassName());

			PortletLayoutFinder.Result result = portletLayoutFinder.find(
				themeDisplay, groupId);

			if ((result == null) || Validator.isNull(result.getPortletId())) {
				return false;
			}

			return true;
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return false;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MBMessageAssetRenderer.class);

	private AssetDisplayPageFriendlyURLProvider
		_assetDisplayPageFriendlyURLProvider;
	private final HtmlParser _htmlParser;
	private final MBMessage _message;
	private final ModelResourcePermission<MBMessage>
		_messageModelResourcePermission;

}