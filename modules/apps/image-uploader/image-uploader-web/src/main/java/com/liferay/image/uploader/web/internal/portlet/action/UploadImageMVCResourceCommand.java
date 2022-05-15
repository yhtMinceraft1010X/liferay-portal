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

package com.liferay.image.uploader.web.internal.portlet.action;

import com.liferay.document.library.kernel.exception.NoSuchFileEntryException;
import com.liferay.image.uploader.web.internal.constants.ImageUploaderPortletKeys;
import com.liferay.image.uploader.web.internal.util.UploadImageUtil;
import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageTool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.io.InputStream;

import javax.portlet.MimeResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ImageUploaderPortletKeys.IMAGE_UPLOADER,
		"mvc.command.name=/image_uploader/upload_image"
	},
	service = MVCResourceCommand.class
)
public class UploadImageMVCResourceCommand extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		try {
			String cmd = ParamUtil.getString(resourceRequest, Constants.CMD);

			if (cmd.equals(Constants.GET_TEMP)) {
				FileEntry tempFileEntry = UploadImageUtil.getTempImageFileEntry(
					resourceRequest);

				_serveTempImageFile(
					resourceResponse, tempFileEntry.getContentStream());
			}
		}
		catch (NoSuchFileEntryException noSuchFileEntryException) {

			// LPS-52675

			if (_log.isDebugEnabled()) {
				_log.debug(noSuchFileEntryException);
			}
		}
		catch (Exception exception) {
			_log.error("Unable to serve resource", exception);
		}
	}

	private void _serveTempImageFile(
			MimeResponse mimeResponse, InputStream tempImageInputStream)
		throws Exception {

		ImageBag imageBag = _imageTool.read(tempImageInputStream);

		byte[] bytes = _imageTool.getBytes(
			imageBag.getRenderedImage(), imageBag.getType());

		mimeResponse.setContentType(
			MimeTypesUtil.getExtensionContentType(imageBag.getType()));

		PortletResponseUtil.write(mimeResponse, bytes);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UploadImageMVCResourceCommand.class);

	@Reference
	private ImageTool _imageTool;

}