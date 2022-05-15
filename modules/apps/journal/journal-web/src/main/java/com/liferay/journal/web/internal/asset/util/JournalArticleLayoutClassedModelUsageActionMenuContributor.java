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

package com.liferay.journal.web.internal.asset.util;

import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.info.item.InfoItemIdentifier;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.journal.web.internal.security.permission.resource.JournalArticlePermission;
import com.liferay.layout.model.LayoutClassedModelUsage;
import com.liferay.layout.util.LayoutClassedModelUsageActionMenuContributor;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.List;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.journal.model.JournalArticle",
	service = LayoutClassedModelUsageActionMenuContributor.class
)
public class JournalArticleLayoutClassedModelUsageActionMenuContributor
	implements LayoutClassedModelUsageActionMenuContributor {

	@Override
	public List<DropdownItem> getLayoutClassedModelUsageActionDropdownItems(
		HttpServletRequest httpServletRequest,
		LayoutClassedModelUsage layoutClassedModelUsage) {

		JournalArticle article = _journalArticleLocalService.fetchLatestArticle(
			layoutClassedModelUsage.getClassPK(), WorkflowConstants.STATUS_ANY,
			false);

		return new DropdownItemList() {
			{
				JournalArticle approvedArticle =
					_journalArticleLocalService.fetchLatestArticle(
						layoutClassedModelUsage.getClassPK(),
						WorkflowConstants.STATUS_APPROVED);

				ThemeDisplay themeDisplay =
					(ThemeDisplay)httpServletRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				if (approvedArticle != null) {
					add(
						dropdownItem -> {
							dropdownItem.setHref(
								_getURL(
									layoutClassedModelUsage,
									AssetRendererFactory.TYPE_LATEST_APPROVED,
									InfoItemIdentifier.VERSION_LATEST_APPROVED,
									httpServletRequest));
							dropdownItem.setLabel(
								LanguageUtil.get(
									themeDisplay.getLocale(), "view-in-page"));
						});
				}

				if (article.isDraft() || article.isPending() ||
					article.isScheduled()) {

					try {
						if (JournalArticlePermission.contains(
								themeDisplay.getPermissionChecker(), article,
								ActionKeys.UPDATE)) {

							String key = "preview-draft-in-page";

							if (article.isPending()) {
								key = "preview-pending-in-page";
							}
							else if (article.isScheduled()) {
								key = "preview-scheduled-in-page";
							}

							String label = LanguageUtil.get(
								themeDisplay.getLocale(), key);

							add(
								dropdownItem -> {
									dropdownItem.setHref(
										_getURL(
											layoutClassedModelUsage,
											AssetRendererFactory.TYPE_LATEST,
											InfoItemIdentifier.VERSION_LATEST,
											httpServletRequest));
									dropdownItem.setLabel(label);
								});
						}
					}
					catch (PortalException portalException) {
						_log.error(
							"Unable to check article permission",
							portalException);
					}
				}
			}
		};
	}

	private String _getURL(
			LayoutClassedModelUsage layoutClassedModelUsage, int previewType,
			String previewVersion, HttpServletRequest httpServletRequest)
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		String layoutURL = null;

		if (layoutClassedModelUsage.getContainerType() ==
				_portal.getClassNameId(FragmentEntryLink.class)) {

			layoutURL = _portal.getLayoutFriendlyURL(
				_layoutLocalService.fetchLayout(
					layoutClassedModelUsage.getPlid()),
				themeDisplay);

			layoutURL = HttpComponentsUtil.setParameter(
				layoutURL, "previewClassNameId",
				String.valueOf(layoutClassedModelUsage.getClassNameId()));
			layoutURL = HttpComponentsUtil.setParameter(
				layoutURL, "previewClassPK",
				String.valueOf(layoutClassedModelUsage.getClassPK()));
			layoutURL = HttpComponentsUtil.setParameter(
				layoutURL, "previewType", String.valueOf(previewType));
			layoutURL = HttpComponentsUtil.setParameter(
				layoutURL, "previewVersion", previewVersion);
		}
		else {
			layoutURL = PortletURLBuilder.create(
				PortletURLFactoryUtil.create(
					httpServletRequest,
					layoutClassedModelUsage.getContainerKey(),
					layoutClassedModelUsage.getPlid(),
					PortletRequest.RENDER_PHASE)
			).setParameter(
				"previewClassNameId", layoutClassedModelUsage.getClassNameId()
			).setParameter(
				"previewClassPK", layoutClassedModelUsage.getClassPK()
			).setParameter(
				"previewType", previewType
			).setParameter(
				"previewVersion", previewVersion
			).buildString();
		}

		String portletURLString = HttpComponentsUtil.setParameter(
			layoutURL, "p_l_back_url", themeDisplay.getURLCurrent());

		return portletURLString + "#portlet_" +
			layoutClassedModelUsage.getContainerKey();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JournalArticleLayoutClassedModelUsageActionMenuContributor.class);

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

}