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

package com.liferay.translation.web.internal.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.constants.MVCRenderConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.translation.constants.TranslationPortletKeys;
import com.liferay.translation.web.internal.display.context.ImportTranslationResultsDisplayContext;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alicia García
 */
@Component(
	property = {
		"javax.portlet.name=" + TranslationPortletKeys.TRANSLATION,
		"mvc.command.name=/translation/import_translation_results"
	},
	service = MVCRenderCommand.class
)
public class ImportTranslationResultsMVCRenderCommand
	implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		HttpServletRequest httpServletRequest =
			_portal.getOriginalServletRequest(
				_portal.getHttpServletRequest(renderRequest));

		HttpSession httpSession = httpServletRequest.getSession();

		ImportTranslationResultsDisplayContext
			importTranslationResultsDisplayContext =
				(ImportTranslationResultsDisplayContext)
					httpSession.getAttribute(
						ImportTranslationResultsDisplayContext.class.getName());

		if (importTranslationResultsDisplayContext == null) {
			_sendRedirect(renderRequest, renderResponse);

			return MVCRenderConstants.MVC_PATH_VALUE_SKIP_DISPATCH;
		}

		renderRequest.setAttribute(
			ImportTranslationResultsDisplayContext.class.getName(),
			importTranslationResultsDisplayContext);

		httpSession.removeAttribute(
			ImportTranslationResultsDisplayContext.class.getName());

		return "/import_translation_results.jsp";
	}

	private void _sendRedirect(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		try {
			HttpServletResponse httpServletResponse =
				_portal.getHttpServletResponse(renderResponse);

			httpServletResponse.sendRedirect(
				ParamUtil.getString(renderRequest, "redirect"));
		}
		catch (IOException ioException) {
			throw new PortletException(ioException);
		}
	}

	@Reference
	private Portal _portal;

}