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

import com.liferay.document.library.configuration.DLConfiguration;
import com.liferay.document.library.kernel.antivirus.AntivirusScannerException;
import com.liferay.document.library.kernel.exception.FileExtensionException;
import com.liferay.document.library.kernel.exception.FileSizeException;
import com.liferay.document.library.kernel.exception.NoSuchFileEntryException;
import com.liferay.document.library.kernel.exception.NoSuchFileException;
import com.liferay.image.uploader.web.internal.constants.ImageUploaderPortletKeys;
import com.liferay.image.uploader.web.internal.util.UploadImageUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.ImageTypeException;
import com.liferay.portal.kernel.exception.NoSuchRepositoryException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageTool;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.upload.UploadRequestSizeException;
import com.liferay.portal.kernel.upload.UploadServletRequestConfigurationHelper;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TempFileEntryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.users.admin.configuration.UserFileUploadsConfiguration;

import java.awt.image.RenderedImage;

import java.io.File;
import java.io.InputStream;

import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 * @author Levente Hudák
 */
@Component(
	configurationPid = {
		"com.liferay.document.library.configuration.DLConfiguration",
		"com.liferay.users.admin.configuration.UserFileUploadsConfiguration"
	},
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	property = {
		"javax.portlet.name=" + ImageUploaderPortletKeys.IMAGE_UPLOADER,
		"mvc.command.name=/image_uploader/upload_image"
	},
	service = MVCActionCommand.class
)
public class UploadImageMVCActionCommand extends BaseMVCActionCommand {

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_dlConfiguration = ConfigurableUtil.createConfigurable(
			DLConfiguration.class, properties);
		_userFileUploadsConfiguration = ConfigurableUtil.createConfigurable(
			UserFileUploadsConfiguration.class, properties);
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		hideDefaultSuccessMessage(actionRequest);

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		long maxFileSize = UploadImageUtil.getMaxFileSize(actionRequest);

		try {
			UploadPortletRequest uploadPortletRequest =
				_portal.getUploadPortletRequest(actionRequest);

			File file = uploadPortletRequest.getFile("fileName");

			if (file.length() > maxFileSize) {
				throw new FileSizeException(maxFileSize);
			}

			UploadException uploadException =
				(UploadException)actionRequest.getAttribute(
					WebKeys.UPLOAD_EXCEPTION);

			if (uploadException != null) {
				Throwable throwable = uploadException.getCause();

				if (uploadException.isExceededFileSizeLimit()) {
					throw new FileSizeException(throwable);
				}

				if (uploadException.isExceededUploadRequestSizeLimit()) {
					throw new UploadRequestSizeException(throwable);
				}

				throw new PortalException(throwable);
			}
			else if (cmd.equals(Constants.ADD_TEMP)) {
				JSONPortletResponseUtil.writeJSON(
					actionRequest, actionResponse,
					JSONUtil.put(
						"tempImageFileName",
						() -> {
							FileEntry tempImageFileEntry =
								_addTempImageFileEntry(actionRequest);

							return tempImageFileEntry.getTitle();
						}));
			}
			else {
				FileEntry fileEntry = null;

				boolean imageUploaded = ParamUtil.getBoolean(
					actionRequest, "imageUploaded");

				if (imageUploaded) {
					fileEntry = _saveTempImageFileEntry(actionRequest);
				}

				SessionMessages.add(actionRequest, "imageUploaded", fileEntry);

				sendRedirect(actionRequest, actionResponse);
			}
		}
		catch (Exception exception) {
			_handleUploadException(
				actionRequest, actionResponse, cmd, maxFileSize, exception);
		}
	}

	private FileEntry _addTempImageFileEntry(PortletRequest portletRequest)
		throws Exception {

		UploadPortletRequest uploadPortletRequest =
			_portal.getUploadPortletRequest(portletRequest);

		File file = uploadPortletRequest.getFile("fileName");

		String contentType = uploadPortletRequest.getContentType("fileName");

		String fileName = uploadPortletRequest.getFileName("fileName");

		String mimeType = MimeTypesUtil.getContentType(file, fileName);

		if (!StringUtil.equalsIgnoreCase(
				ContentTypes.APPLICATION_OCTET_STREAM, mimeType)) {

			contentType = mimeType;
		}

		if (!_webImageMimeTypes.contains(contentType)) {
			throw new ImageTypeException();
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		try {
			TempFileEntryUtil.deleteTempFileEntry(
				themeDisplay.getScopeGroupId(), themeDisplay.getUserId(),
				UploadImageUtil.getTempImageFolderName(), fileName);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return TempFileEntryUtil.addTempFileEntry(
			themeDisplay.getScopeGroupId(), themeDisplay.getUserId(),
			UploadImageUtil.getTempImageFolderName(), fileName, file,
			contentType);
	}

	private String _getTempImageFileName(PortletRequest portletRequest) {
		return ParamUtil.getString(portletRequest, "tempImageFileName");
	}

	private void _handleUploadException(
			ActionRequest actionRequest, ActionResponse actionResponse,
			String cmd, long maxFileSize, Exception exception)
		throws Exception {

		if (exception instanceof PrincipalException) {
			SessionErrors.add(actionRequest, exception.getClass());

			actionResponse.setRenderParameter("mvcPath", "/error.jsp");
		}
		else if (exception instanceof AntivirusScannerException ||
				 exception instanceof FileExtensionException ||
				 exception instanceof FileSizeException ||
				 exception instanceof ImageTypeException ||
				 exception instanceof NoSuchFileException ||
				 exception instanceof UploadException ||
				 exception instanceof UploadRequestSizeException ||
				 (exception.getCause() instanceof ImageTypeException) ||
				 (exception.getCause() instanceof UploadRequestSizeException)) {

			if (cmd.equals(Constants.ADD_TEMP)) {
				hideDefaultErrorMessage(actionRequest);

				ThemeDisplay themeDisplay =
					(ThemeDisplay)actionRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				String errorMessage = StringPool.BLANK;

				if (exception instanceof AntivirusScannerException) {
					AntivirusScannerException antivirusScannerException =
						(AntivirusScannerException)exception;

					errorMessage = themeDisplay.translate(
						antivirusScannerException.getMessageKey());
				}
				else if (exception instanceof FileExtensionException) {
					errorMessage = themeDisplay.translate(
						"please-enter-a-file-with-a-valid-extension-x",
						StringUtil.merge(_dlConfiguration.fileExtensions()));
				}
				else if (exception instanceof FileSizeException) {
					if (maxFileSize == 0) {
						maxFileSize =
							_uploadServletRequestConfigurationHelper.
								getMaxSize();
					}

					errorMessage = themeDisplay.translate(
						"please-enter-a-file-with-a-valid-file-size-no-" +
							"larger-than-x",
						LanguageUtil.formatStorageSize(
							maxFileSize, themeDisplay.getLocale()));
				}
				else if ((exception instanceof ImageTypeException) ||
						 (exception.getCause() instanceof ImageTypeException)) {

					errorMessage = themeDisplay.translate(
						"please-enter-a-file-with-a-valid-file-type");
				}
				else if (exception instanceof NoSuchFileException ||
						 exception instanceof UploadException) {

					errorMessage = themeDisplay.translate(
						"an-unexpected-error-occurred-while-uploading-your-" +
							"file");
				}
				else if ((exception instanceof UploadRequestSizeException) ||
						 (exception.getCause() instanceof
							 UploadRequestSizeException)) {

					errorMessage = themeDisplay.translate(
						"request-is-larger-than-x-and-could-not-be-processed",
						LanguageUtil.formatStorageSize(
							_uploadServletRequestConfigurationHelper.
								getMaxSize(),
							themeDisplay.getLocale()));
				}

				JSONObject jsonObject = JSONUtil.put(
					"errorMessage", errorMessage);

				JSONPortletResponseUtil.writeJSON(
					actionRequest, actionResponse, jsonObject);
			}
			else {
				SessionErrors.add(
					actionRequest, exception.getClass(), exception);
			}
		}
		else {
			throw exception;
		}
	}

	private FileEntry _saveTempImageFileEntry(ActionRequest actionRequest)
		throws Exception {

		try {
			FileEntry tempFileEntry = UploadImageUtil.getTempImageFileEntry(
				actionRequest);

			try (InputStream tempImageInputStream =
					tempFileEntry.getContentStream()) {

				ImageBag imageBag = _imageTool.read(tempImageInputStream);

				RenderedImage renderedImage = imageBag.getRenderedImage();

				String cropRegionJSON = ParamUtil.getString(
					actionRequest, "cropRegion");

				if (Validator.isNotNull(cropRegionJSON)) {
					JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
						cropRegionJSON);

					int height = jsonObject.getInt("height");
					int width = jsonObject.getInt("width");
					int x = jsonObject.getInt("x");
					int y = jsonObject.getInt("y");

					if ((x == 0) && (y == 0) &&
						(renderedImage.getHeight() == height) &&
						(renderedImage.getWidth() == width)) {

						return tempFileEntry;
					}

					if ((height + y) > renderedImage.getHeight()) {
						height = renderedImage.getHeight() - y;
					}

					if ((width + x) > renderedImage.getWidth()) {
						width = renderedImage.getWidth() - x;
					}

					renderedImage = _imageTool.crop(
						renderedImage, height, width, x, y);
				}

				byte[] bytes = _imageTool.getBytes(
					renderedImage, imageBag.getType());

				ThemeDisplay themeDisplay =
					(ThemeDisplay)actionRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				File file = FileUtil.createTempFile(bytes);

				try {
					TempFileEntryUtil.deleteTempFileEntry(
						themeDisplay.getScopeGroupId(),
						themeDisplay.getUserId(),
						UploadImageUtil.getTempImageFolderName(),
						_getTempImageFileName(actionRequest));
				}
				catch (Exception exception) {
					if (_log.isDebugEnabled()) {
						_log.debug(exception);
					}
				}

				return TempFileEntryUtil.addTempFileEntry(
					themeDisplay.getScopeGroupId(), themeDisplay.getUserId(),
					UploadImageUtil.getTempImageFolderName(),
					_getTempImageFileName(actionRequest), file,
					tempFileEntry.getMimeType());
			}
		}
		catch (NoSuchFileEntryException noSuchFileEntryException) {
			throw new UploadException(noSuchFileEntryException);
		}
		catch (NoSuchRepositoryException noSuchRepositoryException) {
			throw new UploadException(noSuchRepositoryException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UploadImageMVCActionCommand.class);

	private static final Set<String> _webImageMimeTypes = SetUtil.fromArray(
		PropsValues.MIME_TYPES_WEB_IMAGES);

	private volatile DLConfiguration _dlConfiguration;

	@Reference
	private ImageTool _imageTool;

	@Reference
	private Portal _portal;

	@Reference
	private UploadServletRequestConfigurationHelper
		_uploadServletRequestConfigurationHelper;

	private volatile UserFileUploadsConfiguration _userFileUploadsConfiguration;

}