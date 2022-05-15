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

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseAssetRendererFactory;
import com.liferay.knowledge.base.constants.KBActionKeys;
import com.liferay.knowledge.base.constants.KBConstants;
import com.liferay.knowledge.base.constants.KBPortletKeys;
import com.liferay.knowledge.base.exception.NoSuchArticleException;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.service.KBArticleLocalService;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.util.HtmlParser;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Peter Shin
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + KBPortletKeys.KNOWLEDGE_BASE_ADMIN,
	service = AssetRendererFactory.class
)
public class KBArticleAssetRendererFactory
	extends BaseAssetRendererFactory<KBArticle> {

	public static final String TYPE = "article";

	public KBArticleAssetRendererFactory() {
		setLinkable(true);
		setSearchable(true);
	}

	@Override
	public AssetEntry getAssetEntry(String className, long classPK)
		throws PortalException {

		KBArticle kbArticle = _getKBArticle(
			classPK, WorkflowConstants.STATUS_ANY);

		return super.getAssetEntry(className, kbArticle.getClassPK());
	}

	@Override
	public AssetRenderer<KBArticle> getAssetRenderer(long classPK, int type)
		throws PortalException {

		KBArticle kbArticle = null;

		if (type == TYPE_LATEST_APPROVED) {
			kbArticle = _getKBArticle(
				classPK, WorkflowConstants.STATUS_APPROVED);
		}
		else {
			kbArticle = _getKBArticle(classPK, WorkflowConstants.STATUS_ANY);
		}

		KBArticleAssetRenderer kbArticleAssetRenderer =
			new KBArticleAssetRenderer(_htmlParser, kbArticle);

		kbArticleAssetRenderer.setAssetRendererType(type);
		kbArticleAssetRenderer.setServletContext(_servletContext);

		return kbArticleAssetRenderer;
	}

	@Override
	public String getClassName() {
		return KBArticle.class.getName();
	}

	@Override
	public String getIconCssClass() {
		return "page";
	}

	@Override
	public String getPortletId() {
		return KBPortletKeys.KNOWLEDGE_BASE_DISPLAY;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public PortletURL getURLAdd(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse, long classTypeId)
		throws PortalException {

		return PortletURLBuilder.create(
			_portal.getControlPanelPortletURL(
				liferayPortletRequest, getGroup(liferayPortletRequest),
				KBPortletKeys.KNOWLEDGE_BASE_ADMIN, 0, 0,
				PortletRequest.RENDER_PHASE)
		).setMVCPath(
			"/admin/edit_article.jsp"
		).buildPortletURL();
	}

	@Override
	public boolean hasAddPermission(
		PermissionChecker permissionChecker, long groupId, long classTypeId) {

		return _portletResourcePermission.contains(
			permissionChecker, groupId, KBActionKeys.ADD_KB_ARTICLE);
	}

	@Override
	public boolean hasPermission(
			PermissionChecker permissionChecker, long classPK, String actionId)
		throws Exception {

		return _kbArticleModelResourcePermission.contains(
			permissionChecker, classPK, actionId);
	}

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.knowledge.base.web)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	@Reference(unbind = "-")
	protected void setKBArticleLocalService(
		KBArticleLocalService kbArticleLocalService) {

		_kbArticleLocalService = kbArticleLocalService;
	}

	private KBArticle _getKBArticle(long classPK, int status)
		throws PortalException {

		KBArticle kbArticle = null;

		try {
			kbArticle = _kbArticleLocalService.getKBArticle(classPK);
		}
		catch (NoSuchArticleException noSuchArticleException) {
			if (_log.isDebugEnabled()) {
				_log.debug(noSuchArticleException);
			}

			kbArticle = _kbArticleLocalService.getLatestKBArticle(
				classPK, status);
		}

		return kbArticle;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		KBArticleAssetRendererFactory.class);

	@Reference
	private HtmlParser _htmlParser;

	private KBArticleLocalService _kbArticleLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.knowledge.base.model.KBArticle)"
	)
	private ModelResourcePermission<KBArticle>
		_kbArticleModelResourcePermission;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(resource.name=" + KBConstants.RESOURCE_NAME_ADMIN + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

	private ServletContext _servletContext;

}