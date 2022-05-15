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

package com.liferay.object.dynamic.data.mapping.form.field.type.internal.attachment;

import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.util.DLURLHelper;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType;
import com.liferay.item.selector.criteria.file.criterion.FileItemSelectorCriterion;
import com.liferay.object.configuration.ObjectConfiguration;
import com.liferay.object.dynamic.data.mapping.form.field.type.constants.ObjectDDMFormFieldTypeConstants;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carolina Barbosa
 */
@Component(
	configurationPid = "com.liferay.object.configuration.ObjectConfiguration",
	immediate = true,
	property = "ddm.form.field.type.name=" + ObjectDDMFormFieldTypeConstants.ATTACHMENT,
	service = DDMFormFieldTemplateContextContributor.class
)
public class AttachmentDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		int maximumFileSize = _getMaximumFileSize(
			ddmFormField, ddmFormFieldRenderingContext.getHttpServletRequest());

		return HashMapBuilder.<String, Object>put(
			"acceptedFileExtensions",
			ddmFormField.getProperty("acceptedFileExtensions")
		).put(
			"fileSource", ddmFormField.getProperty("fileSource")
		).put(
			"maximumFileSize", maximumFileSize
		).put(
			"tip",
			LanguageUtil.format(
				ddmFormFieldRenderingContext.getLocale(),
				"upload-a-x-no-larger-than-x-mb",
				new Object[] {
					ddmFormField.getProperty("acceptedFileExtensions"),
					maximumFileSize
				})
		).put(
			"url", _getURL(ddmFormField, ddmFormFieldRenderingContext)
		).putAll(
			_getFileEntryProperties(
				ddmFormFieldRenderingContext.getHttpServletRequest(),
				GetterUtil.getLong(ddmFormFieldRenderingContext.getValue()))
		).build();
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_objectConfiguration = ConfigurableUtil.createConfigurable(
			ObjectConfiguration.class, properties);
	}

	private Map<String, String> _getFileEntryProperties(
		HttpServletRequest httpServletRequest, long value) {

		try {
			FileEntry fileEntry = _dlAppLocalService.getFileEntry(value);

			ThemeDisplay themeDisplay =
				(ThemeDisplay)httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			return HashMapBuilder.put(
				"contentURL",
				_dlURLHelper.getPreviewURL(
					fileEntry, fileEntry.getFileVersion(), themeDisplay,
					StringPool.BLANK)
			).put(
				"title", fileEntry.getFileName()
			).build();
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return new HashMap<>();
		}
	}

	private long _getGroupId(
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		long groupId = GetterUtil.getLong(
			ddmFormFieldRenderingContext.getProperty("groupId"));

		if (groupId != 0) {
			return groupId;
		}

		HttpServletRequest httpServletRequest =
			ddmFormFieldRenderingContext.getHttpServletRequest();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return themeDisplay.getCompanyGroupId();
	}

	private String _getItemSelectorURL(
		long groupId, String portletNamespace,
		RequestBackedPortletURLFactory requestBackedPortletURLFactory) {

		FileItemSelectorCriterion fileItemSelectorCriterion =
			new FileItemSelectorCriterion();

		fileItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			new FileEntryItemSelectorReturnType());

		PortletURL itemSelectorURL = _itemSelector.getItemSelectorURL(
			requestBackedPortletURLFactory,
			_groupLocalService.fetchGroup(groupId), groupId,
			portletNamespace + "selectAttachmentEntry",
			fileItemSelectorCriterion);

		return itemSelectorURL.toString();
	}

	private int _getMaximumFileSize(
		DDMFormField ddmFormField, HttpServletRequest httpServletRequest) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		int maximumFileSize = GetterUtil.getInteger(
			ddmFormField.getProperty("maximumFileSize"));

		if (themeDisplay.isSignedIn() ||
			(maximumFileSize <
				_objectConfiguration.maximumFileSizeForGuestUsers())) {

			return maximumFileSize;
		}

		return _objectConfiguration.maximumFileSizeForGuestUsers();
	}

	private String _getURL(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		String url = GetterUtil.getString(ddmFormField.getProperty("url"));

		if (Validator.isNotNull(url)) {
			return url;
		}

		String fileSource = GetterUtil.getString(
			ddmFormField.getProperty("fileSource"));

		RequestBackedPortletURLFactory requestBackedPortletURLFactory =
			RequestBackedPortletURLFactoryUtil.create(
				ddmFormFieldRenderingContext.getHttpServletRequest());

		if (Objects.equals(fileSource, "documentsAndMedia")) {
			return _getItemSelectorURL(
				_getGroupId(ddmFormFieldRenderingContext),
				ddmFormFieldRenderingContext.getPortletNamespace(),
				requestBackedPortletURLFactory);
		}
		else if (Objects.equals(fileSource, "userComputer")) {
			return PortletURLBuilder.create(
				requestBackedPortletURLFactory.createActionURL(
					GetterUtil.getString(ddmFormField.getProperty("portletId")))
			).setActionName(
				"/object_entries/upload_attachment"
			).setParameter(
				"objectFieldId",
				GetterUtil.getLong(ddmFormField.getProperty("objectFieldId"))
			).buildString();
		}

		return StringPool.BLANK;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AttachmentDDMFormFieldTemplateContextContributor.class);

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private DLURLHelper _dlURLHelper;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private ItemSelector _itemSelector;

	private volatile ObjectConfiguration _objectConfiguration;

}