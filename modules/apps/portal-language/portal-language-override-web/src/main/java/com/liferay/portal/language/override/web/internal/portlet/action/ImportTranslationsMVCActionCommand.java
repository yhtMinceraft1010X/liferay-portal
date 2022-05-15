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

package com.liferay.portal.language.override.web.internal.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.language.override.service.PLOEntryService;
import com.liferay.portal.language.override.web.internal.constants.PLOPortletKeys;

import java.io.File;
import java.io.FileInputStream;

import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pei-Jung Lan
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + PLOPortletKeys.PORTAL_LANGUAGE_OVERRIDE,
		"mvc.command.name=/portal_language_override/import_translations"
	},
	service = MVCActionCommand.class
)
public class ImportTranslationsMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		UploadPortletRequest uploadPortletRequest =
			_portal.getUploadPortletRequest(actionRequest);

		_importTranslations(
			actionRequest, uploadPortletRequest.getFile("file"),
			ParamUtil.getString(actionRequest, "languageId"));

		if (!SessionErrors.isEmpty(actionRequest)) {
			actionResponse.setRenderParameter(
				"mvcPath", "/configuration/icon/import_translations.jsp");
		}
		else {
			sendRedirect(actionRequest, actionResponse);
		}
	}

	private void _importTranslations(
			ActionRequest actionRequest, File file, String languageId)
		throws Exception {

		if ((file == null) || !file.exists()) {
			SessionErrors.add(actionRequest, "fileEmpty");

			return;
		}

		if (!Objects.equals(
				FileUtil.getExtension(file.getName()), "properties")) {

			SessionErrors.add(actionRequest, "fileExtensionInvalid");

			return;
		}

		Properties languageProperties = new Properties();

		languageProperties.load(new FileInputStream(file));

		if (languageProperties.size() == 0) {
			SessionErrors.add(actionRequest, "fileInvalid");

			return;
		}

		Enumeration<String> enumeration =
			(Enumeration<String>)languageProperties.propertyNames();

		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();

			try {
				_ploEntryService.addOrUpdatePLOEntry(
					key, languageId, languageProperties.getProperty(key));
			}
			catch (PortalException portalException) {
				SessionErrors.add(
					actionRequest, portalException.getClass(), portalException);
			}
		}
	}

	@Reference
	private PLOEntryService _ploEntryService;

	@Reference
	private Portal _portal;

}