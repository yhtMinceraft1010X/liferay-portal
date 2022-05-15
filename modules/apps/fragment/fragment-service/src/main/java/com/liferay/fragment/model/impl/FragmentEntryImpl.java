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

package com.liferay.fragment.model.impl;

import com.liferay.document.library.util.DLURLHelperUtil;
import com.liferay.fragment.constants.FragmentConstants;
import com.liferay.fragment.constants.FragmentExportImportConstants;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.service.FragmentEntryLinkLocalServiceUtil;
import com.liferay.fragment.service.FragmentEntryLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.zip.ZipWriter;

/**
 * @author Eudaldo Alonso
 */
public class FragmentEntryImpl extends FragmentEntryBaseImpl {

	@Override
	public FragmentEntry fetchDraftFragmentEntry() {
		if (isDraft()) {
			return null;
		}

		return FragmentEntryLocalServiceUtil.fetchDraft(getFragmentEntryId());
	}

	@Override
	public String getContent() {
		return StringPool.BLANK;
	}

	@Override
	public int getGlobalUsageCount() {
		return FragmentEntryLinkLocalServiceUtil.
			getFragmentEntryLinksCountByFragmentEntryId(getFragmentEntryId());
	}

	@Override
	public String getIcon() {
		if (Validator.isNull(_icon)) {
			if (getType() == FragmentConstants.TYPE_REACT) {
				_icon = "react";
			}
			else {
				_icon = "code";
			}
		}

		return _icon;
	}

	@Override
	public String getImagePreviewURL(ThemeDisplay themeDisplay) {
		if (Validator.isNotNull(_imagePreviewURL)) {
			return _imagePreviewURL;
		}

		try {
			FileEntry fileEntry = _getPreviewFileEntry();

			if (fileEntry == null) {
				return StringPool.BLANK;
			}

			return DLURLHelperUtil.getImagePreviewURL(fileEntry, themeDisplay);
		}
		catch (Exception exception) {
			_log.error("Unable to get preview entry image URL", exception);
		}

		return StringPool.BLANK;
	}

	@JSON
	@Override
	public int getStatus() {
		if (isHead()) {
			return WorkflowConstants.STATUS_APPROVED;
		}

		return WorkflowConstants.STATUS_DRAFT;
	}

	@Override
	public String getTypeLabel() {
		return FragmentConstants.getTypeLabel(getType());
	}

	@Override
	public int getUsageCount() {
		return FragmentEntryLinkLocalServiceUtil.
			getAllFragmentEntryLinksCountByFragmentEntryId(
				getGroupId(), getFragmentEntryId());
	}

	@Override
	public boolean isApproved() {
		if (isHead()) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isDraft() {
		if (isHead()) {
			return false;
		}

		return true;
	}

	@Override
	public void populateZipWriter(ZipWriter zipWriter, String path)
		throws Exception {

		path = path + StringPool.SLASH + getFragmentEntryKey();

		JSONObject jsonObject = JSONUtil.put(
			"configurationPath", "index.json"
		).put(
			"cssPath", "index.css"
		).put(
			"htmlPath", "index.html"
		).put(
			"icon",
			() -> {
				if (Validator.isNotNull(_icon)) {
					return _icon;
				}

				return null;
			}
		).put(
			"jsPath", "index.js"
		).put(
			"name", getName()
		);

		FileEntry previewFileEntry = _getPreviewFileEntry();

		if (previewFileEntry != null) {
			jsonObject.put(
				"thumbnailPath",
				"thumbnail." + previewFileEntry.getExtension());
		}

		String typeLabel = getTypeLabel();

		if (Validator.isNotNull(typeLabel)) {
			jsonObject.put("type", typeLabel);
		}

		zipWriter.addEntry(
			path + StringPool.SLASH +
				FragmentExportImportConstants.FILE_NAME_FRAGMENT,
			jsonObject.toString());

		zipWriter.addEntry(path + "/index.css", getCss());
		zipWriter.addEntry(path + "/index.js", getJs());
		zipWriter.addEntry(path + "/index.json", getConfiguration());
		zipWriter.addEntry(path + "/index.html", getHtml());

		if (previewFileEntry != null) {
			zipWriter.addEntry(
				path + "/thumbnail." + previewFileEntry.getExtension(),
				previewFileEntry.getContentStream());
		}
	}

	@Override
	public void setIcon(String icon) {
		_icon = icon;
	}

	@Override
	public void setImagePreviewURL(String imagePreviewURL) {
		_imagePreviewURL = imagePreviewURL;
	}

	private FileEntry _getPreviewFileEntry() {
		if (getPreviewFileEntryId() <= 0) {
			return null;
		}

		try {
			return PortletFileRepositoryUtil.getPortletFileEntry(
				getPreviewFileEntryId());
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Unable to get file entry preview ", portalException);
			}
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FragmentEntryImpl.class);

	private String _icon;
	private String _imagePreviewURL;

}