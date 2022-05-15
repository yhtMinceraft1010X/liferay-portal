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

package com.liferay.portal.model.impl;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.document.library.kernel.store.DLStoreUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Image;
import com.liferay.portal.kernel.service.ImageLocalServiceUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.util.PropsValues;

import java.io.InputStream;

/**
 * @author Brian Wing Shun Chan
 */
public class ImageImpl extends ImageBaseImpl {

	@Override
	public byte[] getTextObj() {
		if (_textObj != null) {
			return _textObj;
		}

		long imageId = getImageId();

		try {
			DLFileEntry dlFileEntry = null;

			if (PropsValues.WEB_SERVER_SERVLET_CHECK_IMAGE_GALLERY) {
				dlFileEntry =
					DLFileEntryLocalServiceUtil.fetchFileEntryByAnyImageId(
						imageId);
			}

			InputStream inputStream = null;

			if ((dlFileEntry != null) &&
				(dlFileEntry.getLargeImageId() == imageId)) {

				inputStream = DLStoreUtil.getFileAsStream(
					dlFileEntry.getCompanyId(),
					dlFileEntry.getDataRepositoryId(), dlFileEntry.getName());
			}
			else {
				Image image = ImageLocalServiceUtil.getImage(imageId);

				if (DLStoreUtil.hasFile(
						image.getCompanyId(), _REPOSITORY_ID, getFileName())) {

					inputStream = DLStoreUtil.getFileAsStream(
						image.getCompanyId(), _REPOSITORY_ID, getFileName());
				}
				else {
					if (_log.isDebugEnabled()) {
						_log.debug(
							"Get image " + imageId +
								" from the default company");
					}

					inputStream = DLStoreUtil.getFileAsStream(
						0, _REPOSITORY_ID, getFileName());
				}
			}

			byte[] bytes = FileUtil.getBytes(inputStream);

			_textObj = bytes;
		}
		catch (Exception exception) {
			_log.error("Unable to read image " + imageId, exception);
		}

		return _textObj;
	}

	@Override
	public void setTextObj(byte[] textObj) {
		_textObj = textObj;
	}

	protected String getFileName() {
		return getImageId() + StringPool.PERIOD + getType();
	}

	private static final long _REPOSITORY_ID = 0;

	private static final Log _log = LogFactoryUtil.getLog(ImageImpl.class);

	private byte[] _textObj;

}