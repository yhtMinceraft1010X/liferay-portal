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

package com.liferay.commerce.order.content.web.internal.portlet.action;

import com.liferay.commerce.configuration.CommerceOrderImporterTypeConfiguration;
import com.liferay.commerce.constants.CommercePortletKeys;
import com.liferay.commerce.exception.CommerceOrderImporterTypeException;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.order.content.web.internal.importer.type.util.CommerceOrderImporterTypeUtil;
import com.liferay.commerce.order.importer.type.CommerceOrderImporterTypeRegistry;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.TempFileEntryUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.upload.UniqueFileNameProvider;

import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.Charset;

import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 * @author Luca Pellizzon
 */
@Component(
	configurationPid = "com.liferay.commerce.configuration.CommerceOrderImporterTypeConfiguration",
	enabled = false, immediate = true,
	property = {
		"javax.portlet.name=" + CommercePortletKeys.COMMERCE_OPEN_ORDER_CONTENT,
		"mvc.command.name=/commerce_open_order_content/import_csv"
	},
	service = MVCActionCommand.class
)
public class ImportCSVMVCActionCommand extends BaseMVCActionCommand {

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_commerceOrderImporterTypeConfiguration =
			ConfigurableUtil.createConfigurable(
				CommerceOrderImporterTypeConfiguration.class, properties);
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			FileEntry fileEntry = _addFileEntry(actionRequest);

			CSVParser csvParser = _getCSVParser(fileEntry.getFileEntryId());

			List<String> headerNames = csvParser.getHeaderNames();

			if (!headerNames.containsAll(ListUtil.fromArray(_HEADER_NAMES))) {
				throw new CommerceOrderImporterTypeException();
			}

			for (CSVRecord csvRecord : csvParser.getRecords()) {
				if (csvRecord.size() < 2) {
					throw new CommerceOrderImporterTypeException();
				}
			}

			sendRedirect(
				actionRequest, actionResponse,
				_getPreviewCommerceOrderRedirect(
					actionRequest, fileEntry.getFileEntryId()));
		}
		catch (Exception exception) {
			if (exception instanceof CommerceOrderImporterTypeException) {
				hideDefaultErrorMessage(actionRequest);

				SessionErrors.add(
					actionRequest, CommerceOrderImporterTypeException.class,
					ParamUtil.getString(
						actionRequest, "commerceOrderImporterTypeKey"));

				sendRedirect(
					actionRequest, actionResponse,
					ParamUtil.getString(actionRequest, "redirect"));
			}
			else {
				if (_log.isDebugEnabled()) {
					_log.debug(exception);
				}

				hideDefaultErrorMessage(actionRequest);

				SessionErrors.add(
					actionRequest, "commerceOrderImporterTypeKey",
					StringPool.BLANK);

				sendRedirect(
					actionRequest, actionResponse,
					ParamUtil.getString(actionRequest, "redirect"));
			}
		}
	}

	private FileEntry _addFileEntry(ActionRequest actionRequest)
		throws Exception {

		UploadPortletRequest uploadPortletRequest =
			_portal.getUploadPortletRequest(actionRequest);

		if (uploadPortletRequest.getSize(_PARAMETER_NAME) == 0) {
			throw new CommerceOrderImporterTypeException();
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		try (InputStream inputStream = uploadPortletRequest.getFileAsStream(
				_PARAMETER_NAME)) {

			Company company = themeDisplay.getCompany();

			long groupId = company.getGroupId();

			String uniqueFileName = _uniqueFileNameProvider.provide(
				uploadPortletRequest.getFileName(_PARAMETER_NAME),
				fileName -> _exists(
					fileName, groupId, themeDisplay.getUserId()));

			return TempFileEntryUtil.addTempFileEntry(
				groupId, themeDisplay.getUserId(), _TEMP_FOLDER_NAME,
				uniqueFileName, inputStream,
				uploadPortletRequest.getContentType(_PARAMETER_NAME));
		}
	}

	private boolean _exists(String fileName, long groupId, long userId) {
		try {
			FileEntry fileEntry = TempFileEntryUtil.getTempFileEntry(
				groupId, userId, _TEMP_FOLDER_NAME, fileName);

			if (fileEntry != null) {
				return true;
			}

			return false;
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return false;
		}
	}

	private CSVParser _getCSVParser(long fileEntryId) throws Exception {
		CSVFormat csvFormat = CommerceOrderImporterTypeUtil.getCSVFormat(
			_commerceOrderImporterTypeConfiguration);

		try {
			FileEntry fileEntry = _dlAppLocalService.getFileEntry(fileEntryId);

			return CSVParser.parse(
				FileUtil.createTempFile(fileEntry.getContentStream()),
				Charset.defaultCharset(), csvFormat);
		}
		catch (IOException ioException) {
			if (_log.isDebugEnabled()) {
				_log.debug(ioException);
			}

			throw new CommerceOrderImporterTypeException();
		}
	}

	private String _getPreviewCommerceOrderRedirect(
			ActionRequest actionRequest, long fileEntryId)
		throws Exception {

		return PortletURLBuilder.create(
			PortletProviderUtil.getPortletURL(
				actionRequest, CommerceOrder.class.getName(),
				PortletProvider.Action.EDIT)
		).setMVCRenderCommandName(
			"/commerce_open_order_content/view_commerce_order_importer_type"
		).setBackURL(
			ParamUtil.getString(actionRequest, "backURL")
		).setParameter(
			"commerceOrderId",
			ParamUtil.getLong(actionRequest, "commerceOrderId")
		).setParameter(
			"commerceOrderImporterTypeKey",
			ParamUtil.getString(actionRequest, "commerceOrderImporterTypeKey")
		).setParameter(
			"fileEntryId", fileEntryId
		).setWindowState(
			LiferayWindowState.POP_UP
		).buildString();
	}

	private static final String[] _HEADER_NAMES = {"quantity", "sku"};

	private static final String _PARAMETER_NAME = "csvFileName";

	private static final String _TEMP_FOLDER_NAME =
		ImportCSVMVCActionCommand.class.getName();

	private static final Log _log = LogFactoryUtil.getLog(
		ImportCSVMVCActionCommand.class);

	private volatile CommerceOrderImporterTypeConfiguration
		_commerceOrderImporterTypeConfiguration;

	@Reference
	private CommerceOrderImporterTypeRegistry
		_commerceOrderImporterTypeRegistry;

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private UniqueFileNameProvider _uniqueFileNameProvider;

}