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

package com.liferay.message.boards.web.internal.upload;

import com.liferay.document.library.kernel.util.DLValidator;
import com.liferay.message.boards.constants.MBMessageConstants;
import com.liferay.message.boards.service.MBMessageService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.TempFileEntryUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.upload.UploadFileEntryHandler;

import java.io.IOException;
import java.io.InputStream;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
public abstract class BaseMBUploadFileEntryHandler
	implements UploadFileEntryHandler {

	@Override
	public FileEntry upload(UploadPortletRequest uploadPortletRequest)
		throws IOException, PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)uploadPortletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		dlValidator.validateFileSize(
			themeDisplay.getScopeGroupId(),
			uploadPortletRequest.getFileName(getParameterName()),
			uploadPortletRequest.getContentType(getParameterName()),
			uploadPortletRequest.getSize(getParameterName()));

		long categoryId = ParamUtil.getLong(uploadPortletRequest, "categoryId");

		try (InputStream inputStream = _getFileAsInputStream(
				uploadPortletRequest)) {

			return mbMessageService.addTempAttachment(
				themeDisplay.getScopeGroupId(), categoryId,
				MBMessageConstants.TEMP_FOLDER_NAME,
				TempFileEntryUtil.getTempFileName(
					_getFileName(uploadPortletRequest)),
				inputStream, _getContentType(uploadPortletRequest));
		}
	}

	protected abstract String getParameterName();

	@Reference
	protected DLValidator dlValidator;

	@Reference
	protected MBMessageService mbMessageService;

	private String _getContentType(UploadPortletRequest uploadPortletRequest) {
		return uploadPortletRequest.getContentType(getParameterName());
	}

	private InputStream _getFileAsInputStream(
			UploadPortletRequest uploadPortletRequest)
		throws IOException {

		return uploadPortletRequest.getFileAsStream(getParameterName());
	}

	private String _getFileName(UploadPortletRequest uploadPortletRequest) {
		return uploadPortletRequest.getFileName(getParameterName());
	}

}