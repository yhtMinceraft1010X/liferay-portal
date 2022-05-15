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

package com.liferay.journal.web.internal.portlet.action;

import com.liferay.asset.display.page.constants.AssetDisplayPageConstants;
import com.liferay.asset.display.page.portlet.AssetDisplayPageEntryFormProcessor;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.dynamic.data.mapping.form.values.factory.DDMFormValuesFactory;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.dynamic.data.mapping.util.DDMFormValuesToFieldsConverter;
import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.exception.ArticleContentSizeException;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.journal.service.JournalContentSearchLocalService;
import com.liferay.journal.util.JournalConverter;
import com.liferay.journal.util.JournalHelper;
import com.liferay.journal.web.internal.asset.model.JournalArticleAssetRenderer;
import com.liferay.journal.web.internal.util.JournalUtil;
import com.liferay.layout.model.LayoutClassedModelUsage;
import com.liferay.layout.service.LayoutClassedModelUsageLocalService;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.MultiSessionMessages;
import com.liferay.portal.kernel.upload.LiferayFileItemException;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FriendlyURLNormalizer;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.util.PropsValues;

import java.io.File;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + JournalPortletKeys.JOURNAL,
		"mvc.command.name=/journal/add_article",
		"mvc.command.name=/journal/update_article"
	},
	service = MVCActionCommand.class
)
public class UpdateArticleMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		UploadException uploadException =
			(UploadException)actionRequest.getAttribute(
				WebKeys.UPLOAD_EXCEPTION);

		if (uploadException != null) {
			Throwable throwable = uploadException.getCause();

			if (uploadException.isExceededLiferayFileItemSizeLimit()) {
				throw new LiferayFileItemException(throwable);
			}

			if (uploadException.isExceededFileSizeLimit() ||
				uploadException.isExceededUploadRequestSizeLimit()) {

				throw new ArticleContentSizeException(throwable);
			}

			throw new PortalException(throwable);
		}

		UploadPortletRequest uploadPortletRequest =
			_portal.getUploadPortletRequest(actionRequest);

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Updating article " +
					MapUtil.toString(uploadPortletRequest.getParameterMap()));
		}

		String actionName = ParamUtil.getString(
			actionRequest, ActionRequest.ACTION_NAME);

		long groupId = ParamUtil.getLong(uploadPortletRequest, "groupId");
		long folderId = ParamUtil.getLong(uploadPortletRequest, "folderId");
		String articleId = ParamUtil.getString(
			uploadPortletRequest, "articleId");

		Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(
			actionRequest, "titleMapAsXML");

		String ddmStructureKey = ParamUtil.getString(
			uploadPortletRequest, "ddmStructureKey");

		DDMStructure ddmStructure = _ddmStructureLocalService.getStructure(
			_portal.getSiteGroupId(groupId),
			_portal.getClassNameId(JournalArticle.class), ddmStructureKey,
			true);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			JournalArticle.class.getName(), uploadPortletRequest);

		DDMFormValues ddmFormValues = _ddmFormValuesFactory.create(
			actionRequest, ddmStructure.getDDMForm());

		Fields fields = _ddmFormValuesToFieldsConverter.convert(
			ddmStructure, ddmFormValues);

		String content = _journalConverter.getContent(
			ddmStructure, fields, groupId);

		Map<Locale, String> descriptionMap =
			LocalizationUtil.getLocalizationMap(
				actionRequest, "descriptionMapAsXML");
		Map<Locale, String> friendlyURLMap =
			LocalizationUtil.getLocalizationMap(actionRequest, "friendlyURL");

		String ddmTemplateKey = ParamUtil.getString(
			uploadPortletRequest, "ddmTemplateKey");
		int displayPageType = ParamUtil.getInteger(
			uploadPortletRequest, "displayPageType");

		String layoutUuid = ParamUtil.getString(
			uploadPortletRequest, "layoutUuid");

		if ((displayPageType == AssetDisplayPageConstants.TYPE_DEFAULT) ||
			(displayPageType == AssetDisplayPageConstants.TYPE_SPECIFIC)) {

			Layout targetLayout = _journalHelper.getArticleLayout(
				layoutUuid, groupId);

			JournalArticle latestArticle = _journalArticleService.fetchArticle(
				groupId, articleId);

			if ((displayPageType == AssetDisplayPageConstants.TYPE_SPECIFIC) &&
				(targetLayout == null) && (latestArticle != null) &&
				Validator.isNotNull(latestArticle.getLayoutUuid())) {

				Layout latestTargetLayout = _journalHelper.getArticleLayout(
					latestArticle.getLayoutUuid(), groupId);

				if (latestTargetLayout == null) {
					layoutUuid = latestArticle.getLayoutUuid();
				}
			}
			else if ((displayPageType ==
						AssetDisplayPageConstants.TYPE_DEFAULT) ||
					 (targetLayout == null)) {

				layoutUuid = null;
			}
		}
		else {
			layoutUuid = null;
		}

		int displayDateMonth = ParamUtil.getInteger(
			uploadPortletRequest, "displayDateMonth");
		int displayDateDay = ParamUtil.getInteger(
			uploadPortletRequest, "displayDateDay");
		int displayDateYear = ParamUtil.getInteger(
			uploadPortletRequest, "displayDateYear");
		int displayDateHour = ParamUtil.getInteger(
			uploadPortletRequest, "displayDateHour");
		int displayDateMinute = ParamUtil.getInteger(
			uploadPortletRequest, "displayDateMinute");
		int displayDateAmPm = ParamUtil.getInteger(
			uploadPortletRequest, "displayDateAmPm");

		if (displayDateAmPm == Calendar.PM) {
			displayDateHour += 12;
		}

		int expirationDateMonth = ParamUtil.getInteger(
			uploadPortletRequest, "expirationDateMonth");
		int expirationDateDay = ParamUtil.getInteger(
			uploadPortletRequest, "expirationDateDay");
		int expirationDateYear = ParamUtil.getInteger(
			uploadPortletRequest, "expirationDateYear");
		int expirationDateHour = ParamUtil.getInteger(
			uploadPortletRequest, "expirationDateHour");
		int expirationDateMinute = ParamUtil.getInteger(
			uploadPortletRequest, "expirationDateMinute");
		int expirationDateAmPm = ParamUtil.getInteger(
			uploadPortletRequest, "expirationDateAmPm");

		boolean neverExpire = ParamUtil.getBoolean(
			uploadPortletRequest, "neverExpire", displayDateYear == 0);

		if (!PropsValues.SCHEDULER_ENABLED) {
			neverExpire = true;
		}

		if (expirationDateAmPm == Calendar.PM) {
			expirationDateHour += 12;
		}

		int reviewDateMonth = ParamUtil.getInteger(
			uploadPortletRequest, "reviewDateMonth");
		int reviewDateDay = ParamUtil.getInteger(
			uploadPortletRequest, "reviewDateDay");
		int reviewDateYear = ParamUtil.getInteger(
			uploadPortletRequest, "reviewDateYear");
		int reviewDateHour = ParamUtil.getInteger(
			uploadPortletRequest, "reviewDateHour");
		int reviewDateMinute = ParamUtil.getInteger(
			uploadPortletRequest, "reviewDateMinute");
		int reviewDateAmPm = ParamUtil.getInteger(
			uploadPortletRequest, "reviewDateAmPm");

		boolean neverReview = ParamUtil.getBoolean(
			uploadPortletRequest, "neverReview", displayDateYear == 0);

		if (!PropsValues.SCHEDULER_ENABLED) {
			neverReview = true;
		}

		if (reviewDateAmPm == Calendar.PM) {
			reviewDateHour += 12;
		}

		boolean indexable = ParamUtil.getBoolean(
			uploadPortletRequest, "indexable");

		String smallImageSource = ParamUtil.getString(
			uploadPortletRequest, "smallImageSource", "none");

		boolean smallImage = !Objects.equals(smallImageSource, "none");

		String smallImageURL = StringPool.BLANK;
		File smallFile = null;

		if (Objects.equals(smallImageSource, "url")) {
			smallImageURL = ParamUtil.getString(
				uploadPortletRequest, "smallImageURL");
		}
		else if (Objects.equals(smallImageSource, "file")) {
			smallFile = uploadPortletRequest.getFile("smallFile");
		}

		String articleURL = ParamUtil.getString(
			uploadPortletRequest, "articleURL");

		JournalArticle article = null;
		String oldUrlTitle = StringPool.BLANK;

		if (actionName.equals("/journal/add_article")) {

			// Add article

			long classNameId = ParamUtil.getLong(
				uploadPortletRequest, "classNameId");
			long classPK = ParamUtil.getLong(uploadPortletRequest, "classPK");
			boolean autoArticleId = ParamUtil.getBoolean(
				uploadPortletRequest, "autoArticleId");

			article = _journalArticleService.addArticle(
				null, groupId, folderId, classNameId, classPK, articleId,
				autoArticleId, titleMap, descriptionMap, friendlyURLMap,
				content, ddmStructureKey, ddmTemplateKey, layoutUuid,
				displayDateMonth, displayDateDay, displayDateYear,
				displayDateHour, displayDateMinute, expirationDateMonth,
				expirationDateDay, expirationDateYear, expirationDateHour,
				expirationDateMinute, neverExpire, reviewDateMonth,
				reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute,
				neverReview, indexable, smallImage, smallImageURL, smallFile,
				null, articleURL, serviceContext);
		}
		else {

			// Update article

			double version = ParamUtil.getDouble(
				uploadPortletRequest, "version");

			article = _journalArticleService.getArticle(
				groupId, articleId, version);

			String tempOldUrlTitle = article.getUrlTitle();

			if (actionName.equals("/journal/update_article")) {
				article = _journalArticleService.updateArticle(
					groupId, folderId, articleId, version, titleMap,
					descriptionMap, friendlyURLMap, content, ddmStructureKey,
					ddmTemplateKey, layoutUuid, displayDateMonth,
					displayDateDay, displayDateYear, displayDateHour,
					displayDateMinute, expirationDateMonth, expirationDateDay,
					expirationDateYear, expirationDateHour,
					expirationDateMinute, neverExpire, reviewDateMonth,
					reviewDateDay, reviewDateYear, reviewDateHour,
					reviewDateMinute, neverReview, indexable, smallImage,
					smallImageURL, smallFile, null, articleURL, serviceContext);
			}

			if (!tempOldUrlTitle.equals(article.getUrlTitle())) {
				oldUrlTitle = tempOldUrlTitle;
			}
		}

		// Recent articles

		JournalUtil.addRecentArticle(actionRequest, article);

		// Journal content

		String portletResource = ParamUtil.getString(
			actionRequest, "portletResource");

		long refererPlid = ParamUtil.getLong(actionRequest, "refererPlid");

		if (Validator.isNotNull(portletResource) && (refererPlid > 0)) {
			AssetEntry assetEntry = _assetEntryLocalService.fetchEntry(
				JournalArticle.class.getName(), article.getResourcePrimKey());

			PortletPreferences portletPreferences =
				PortletPreferencesFactoryUtil.getStrictPortletSetup(
					_layoutLocalService.getLayout(refererPlid),
					portletResource);

			if (portletPreferences != null) {
				portletPreferences.setValue(
					"groupId", String.valueOf(article.getGroupId()));
				portletPreferences.setValue(
					"articleId", article.getArticleId());

				if (assetEntry != null) {
					portletPreferences.setValue(
						"assetEntryId",
						String.valueOf(assetEntry.getEntryId()));
				}

				portletPreferences.store();

				_updateContentSearch(
					refererPlid, portletResource, article.getArticleId());
			}

			if (assetEntry != null) {
				_updateLayoutClassedModelUsage(
					groupId,
					_portal.getClassNameId(JournalArticle.class.getName()),
					article.getResourcePrimKey(), portletResource, refererPlid,
					serviceContext);
			}
		}

		// Asset display page

		_assetDisplayPageEntryFormProcessor.process(
			JournalArticle.class.getName(), article.getResourcePrimKey(),
			actionRequest);

		int workflowAction = ParamUtil.getInteger(
			actionRequest, "workflowAction", WorkflowConstants.ACTION_PUBLISH);

		if (workflowAction != WorkflowConstants.ACTION_SAVE_DRAFT) {
			String referringPortletResource = ParamUtil.getString(
				actionRequest, "referringPortletResource");

			if (Validator.isNotNull(referringPortletResource)) {
				MultiSessionMessages.add(
					actionRequest,
					referringPortletResource + "requestProcessed");
			}
			else if (Validator.isNotNull(portletResource)) {
				MultiSessionMessages.add(
					actionRequest, portletResource + "requestProcessed");
			}
		}

		String friendlyURLChangedMessage = _getFriendlyURLChangedMessage(
			actionRequest, friendlyURLMap, article.getFriendlyURLMap());

		if (Validator.isNotNull(friendlyURLChangedMessage)) {
			MultiSessionMessages.add(
				actionRequest, "friendlyURLChanged", friendlyURLChangedMessage);
		}

		_sendEditArticleRedirect(actionRequest, article, oldUrlTitle);

		boolean hideDefaultSuccessMessage = ParamUtil.getBoolean(
			actionRequest, "hideDefaultSuccessMessage");

		if (hideDefaultSuccessMessage) {
			hideDefaultSuccessMessage(actionRequest);
		}
	}

	private String _getFriendlyURLChangedMessage(
		ActionRequest actionRequest, Map<Locale, String> originalFriendlyURLMap,
		Map<Locale, String> currentFriendlyURLMap) {

		List<String> messages = new ArrayList<>();

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			actionRequest);

		for (Map.Entry<Locale, String> entry :
				currentFriendlyURLMap.entrySet()) {

			Locale locale = entry.getKey();

			String originalFriendlyURL = originalFriendlyURLMap.get(locale);

			String normalizedOriginalFriendlyURL =
				_friendlyURLNormalizer.normalizeWithEncoding(
					originalFriendlyURL);

			String currentFriendlyURL = entry.getValue();

			if (Validator.isNotNull(originalFriendlyURL) &&
				!currentFriendlyURL.equals(normalizedOriginalFriendlyURL)) {

				messages.add(
					LanguageUtil.format(
						httpServletRequest, "for-locale-x-x-was-changed-to-x",
						new Object[] {
							"<strong>" + locale.getLanguage() + "</strong>",
							"<strong>" + _html.escapeURL(originalFriendlyURL) +
								"</strong>",
							"<strong>" + currentFriendlyURL + "</strong>"
						}));
			}
		}

		if (!messages.isEmpty()) {
			messages.add(
				0,
				LanguageUtil.get(
					httpServletRequest,
					"the-following-friendly-urls-were-changed-to-ensure-" +
						"uniqueness"));
		}

		return StringUtil.merge(messages, "<br />");
	}

	private String _getSaveAndContinueRedirect(
			ActionRequest actionRequest, JournalArticle article,
			String redirect)
		throws Exception {

		return PortletURLBuilder.create(
			PortletURLFactoryUtil.create(
				actionRequest, JournalPortletKeys.JOURNAL,
				PortletRequest.RENDER_PHASE)
		).setMVCPath(
			"/edit_article.jsp"
		).setRedirect(
			redirect
		).setPortletResource(
			ParamUtil.getString(actionRequest, "portletResource")
		).setParameter(
			"articleId", article.getArticleId()
		).setParameter(
			"folderId", article.getFolderId()
		).setParameter(
			"groupId", article.getGroupId()
		).setParameter(
			"languageId",
			() -> {
				String languageId = ParamUtil.getString(
					actionRequest, "languageId");

				if (Validator.isNotNull(languageId)) {
					return languageId;
				}

				return null;
			}
		).setParameter(
			"referringPortletResource",
			ParamUtil.getString(actionRequest, "referringPortletResource")
		).setParameter(
			"resourcePrimKey", article.getResourcePrimKey()
		).setParameter(
			"version", article.getVersion()
		).setWindowState(
			actionRequest.getWindowState()
		).buildString();
	}

	private void _sendEditArticleRedirect(
			ActionRequest actionRequest, JournalArticle article,
			String oldUrlTitle)
		throws Exception {

		String actionName = ParamUtil.getString(
			actionRequest, ActionRequest.ACTION_NAME);

		String redirect = ParamUtil.getString(actionRequest, "redirect");

		int workflowAction = ParamUtil.getInteger(
			actionRequest, "workflowAction", WorkflowConstants.ACTION_PUBLISH);

		String portletId = HttpComponentsUtil.getParameter(
			redirect, "portletResource", false);

		String namespace = _portal.getPortletNamespace(portletId);

		if (Validator.isNotNull(oldUrlTitle) &&
			Validator.isNotNull(portletId)) {

			String oldRedirectParam = namespace + "redirect";

			String oldRedirect = HttpComponentsUtil.getParameter(
				redirect, oldRedirectParam, false);

			if (Validator.isNotNull(oldRedirect)) {
				String newRedirect = HttpComponentsUtil.decodeURL(oldRedirect);

				newRedirect = StringUtil.replace(
					newRedirect, oldUrlTitle, article.getUrlTitle());
				newRedirect = StringUtil.replace(
					newRedirect, oldRedirectParam, "redirect");

				redirect = StringUtil.replace(
					redirect, oldRedirect, newRedirect);
			}
		}

		if ((article != null) &&
			(workflowAction == WorkflowConstants.ACTION_SAVE_DRAFT)) {

			redirect = _getSaveAndContinueRedirect(
				actionRequest, article, redirect);
		}
		else {
			redirect = _portal.escapeRedirect(redirect);

			if (Validator.isNotNull(redirect) &&
				Validator.isNotNull(portletId) &&
				actionName.equals("/journal/add_article") &&
				(article != null) && Validator.isNotNull(namespace)) {

				redirect = HttpComponentsUtil.addParameter(
					redirect, namespace + "className",
					JournalArticle.class.getName());
				redirect = HttpComponentsUtil.addParameter(
					redirect, namespace + "classPK",
					JournalArticleAssetRenderer.getClassPK(article));
			}
		}

		actionRequest.setAttribute(WebKeys.REDIRECT, redirect);
	}

	private void _updateContentSearch(
			long plid, String portletResource, String articleId)
		throws Exception {

		Layout layout = _layoutLocalService.fetchLayout(plid);

		_journalContentSearchLocalService.updateContentSearch(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			portletResource, articleId, true);
	}

	private void _updateLayoutClassedModelUsage(
		long groupId, long classNameId, long classPK, String portletResource,
		long plid, ServiceContext serviceContext) {

		LayoutClassedModelUsage layoutClassedModelUsage =
			_layoutClassedModelUsageLocalService.fetchLayoutClassedModelUsage(
				classNameId, classPK, portletResource,
				_portal.getClassNameId(Portlet.class), plid);

		if (layoutClassedModelUsage != null) {
			return;
		}

		_layoutClassedModelUsageLocalService.addLayoutClassedModelUsage(
			groupId, classNameId, classPK, portletResource,
			_portal.getClassNameId(Portlet.class), plid, serviceContext);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpdateArticleMVCActionCommand.class);

	@Reference
	private AssetDisplayPageEntryFormProcessor
		_assetDisplayPageEntryFormProcessor;

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private DDMFormValuesFactory _ddmFormValuesFactory;

	@Reference
	private DDMFormValuesToFieldsConverter _ddmFormValuesToFieldsConverter;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private FriendlyURLNormalizer _friendlyURLNormalizer;

	@Reference
	private Html _html;

	@Reference
	private JournalArticleService _journalArticleService;

	@Reference
	private JournalContentSearchLocalService _journalContentSearchLocalService;

	@Reference
	private JournalConverter _journalConverter;

	@Reference
	private JournalHelper _journalHelper;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private LayoutClassedModelUsageLocalService
		_layoutClassedModelUsageLocalService;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

}