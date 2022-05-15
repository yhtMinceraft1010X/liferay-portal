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

package com.liferay.knowledge.base.web.internal.asset.model;

import com.liferay.asset.kernel.model.BaseJSPAssetRenderer;
import com.liferay.knowledge.base.constants.KBActionKeys;
import com.liferay.knowledge.base.constants.KBArticleConstants;
import com.liferay.knowledge.base.constants.KBPortletKeys;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.util.KnowledgeBaseUtil;
import com.liferay.knowledge.base.web.internal.constants.KBWebKeys;
import com.liferay.knowledge.base.web.internal.security.permission.resource.KBArticlePermission;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlParser;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Peter Shin
 */
public class KBArticleAssetRenderer extends BaseJSPAssetRenderer<KBArticle> {

	public KBArticleAssetRenderer(HtmlParser htmlParser, KBArticle kbArticle) {
		_htmlParser = htmlParser;
		_kbArticle = kbArticle;
	}

	@Override
	public KBArticle getAssetObject() {
		return _kbArticle;
	}

	@Override
	public String getClassName() {
		return KBArticle.class.getName();
	}

	@Override
	public long getClassPK() {
		return getClassPK(_kbArticle);
	}

	@Override
	public long getGroupId() {
		return _kbArticle.getGroupId();
	}

	@Override
	public String getJspPath(
		HttpServletRequest httpServletRequest, String template) {

		if (template.equals(TEMPLATE_ABSTRACT) ||
			template.equals(TEMPLATE_FULL_CONTENT)) {

			return "/admin/asset/" + template + ".jsp";
		}

		return null;
	}

	@Override
	public int getStatus() {
		return _kbArticle.getStatus();
	}

	@Override
	public String getSummary(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		String summary = _kbArticle.getDescription();

		if (Validator.isNull(summary)) {
			summary = StringUtil.shorten(
				_htmlParser.extractText(_kbArticle.getContent()), 200);
		}

		return summary;
	}

	@Override
	public String getTitle(Locale locale) {
		return _kbArticle.getTitle();
	}

	@Override
	public PortletURL getURLEdit(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		Group group = GroupLocalServiceUtil.fetchGroup(_kbArticle.getGroupId());

		if (group.isCompany()) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)liferayPortletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			group = themeDisplay.getScopeGroup();
		}

		return PortletURLBuilder.create(
			PortalUtil.getControlPanelPortletURL(
				liferayPortletRequest, group,
				KBPortletKeys.KNOWLEDGE_BASE_ADMIN, 0, 0,
				PortletRequest.RENDER_PHASE)
		).setMVCPath(
			"/admin/edit_article.jsp"
		).setParameter(
			"resourcePrimKey", _kbArticle.getResourcePrimKey()
		).buildPortletURL();
	}

	@Override
	public String getURLViewInContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		String noSuchEntryRedirect) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)liferayPortletRequest.getAttribute(
				KBWebKeys.THEME_DISPLAY);

		return KnowledgeBaseUtil.getKBArticleURL(
			themeDisplay.getPlid(), _kbArticle.getResourcePrimKey(),
			_kbArticle.getStatus(), themeDisplay.getPortalURL(), false);
	}

	@Override
	public long getUserId() {
		return _kbArticle.getUserId();
	}

	@Override
	public String getUserName() {
		return _kbArticle.getUserName();
	}

	@Override
	public String getUuid() {
		return _kbArticle.getUuid();
	}

	@Override
	public boolean hasEditPermission(PermissionChecker permissionChecker)
		throws PortalException {

		return KBArticlePermission.contains(
			permissionChecker, _kbArticle, KBActionKeys.UPDATE);
	}

	@Override
	public boolean hasViewPermission(PermissionChecker permissionChecker)
		throws PortalException {

		return KBArticlePermission.contains(
			permissionChecker, _kbArticle, KBActionKeys.VIEW);
	}

	@Override
	public boolean include(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String template)
		throws Exception {

		httpServletRequest.setAttribute(
			KBWebKeys.KNOWLEDGE_BASE_KB_ARTICLE, _kbArticle);

		return super.include(httpServletRequest, httpServletResponse, template);
	}

	@Override
	public boolean isPrintable() {
		return true;
	}

	protected long getClassPK(KBArticle kbArticle) {
		if ((kbArticle.isDraft() || kbArticle.isPending()) &&
			(kbArticle.getVersion() != KBArticleConstants.DEFAULT_VERSION)) {

			return kbArticle.getPrimaryKey();
		}

		return kbArticle.getResourcePrimKey();
	}

	private final HtmlParser _htmlParser;
	private final KBArticle _kbArticle;

}