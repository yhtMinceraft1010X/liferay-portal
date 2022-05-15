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

package com.liferay.staging.processes.web.internal.portlet.action;

import com.liferay.exportimport.kernel.exception.RemoteExportException;
import com.liferay.exportimport.kernel.lar.ExportImportHelper;
import com.liferay.exportimport.kernel.staging.Staging;
import com.liferay.portal.kernel.exception.LayoutPrototypeException;
import com.liferay.portal.kernel.exception.RemoteOptionsException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lock.DuplicateLockException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.security.auth.AuthException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.auth.RemoteAuthException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.SessionTreeJSClicks;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.staging.constants.StagingProcessesPortletKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Levente Hudák
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + StagingProcessesPortletKeys.STAGING_PROCESSES,
		"mvc.command.name=/staging_processes/publish_layouts"
	},
	service = MVCActionCommand.class
)
public class PublishLayoutsMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		if (Validator.isNull(cmd)) {
			SessionMessages.add(
				actionRequest, _portal.getPortletId(actionRequest));

			return;
		}

		String redirect = ParamUtil.getString(actionRequest, "redirect");

		try {
			if (cmd.equals("copy_from_live")) {
				setLayoutIdMap(actionRequest);

				_staging.copyFromLive(actionRequest);
			}
			else if (cmd.equals(Constants.PUBLISH_TO_LIVE)) {
				setLayoutIdMap(actionRequest);

				hideDefaultSuccessMessage(actionRequest);

				_staging.publishToLive(actionRequest);
			}
			else if (cmd.equals(Constants.PUBLISH_TO_REMOTE)) {
				hideDefaultSuccessMessage(actionRequest);

				setLayoutIdMap(actionRequest);

				_staging.publishToRemote(actionRequest);
			}
			else if (cmd.equals("schedule_copy_from_live")) {
				setLayoutIdMap(actionRequest);

				_staging.scheduleCopyFromLive(actionRequest);
			}
			else if (cmd.equals("schedule_publish_to_live")) {
				setLayoutIdMap(actionRequest);

				_staging.schedulePublishToLive(actionRequest);
			}
			else if (cmd.equals("schedule_publish_to_remote")) {
				setLayoutIdMap(actionRequest);

				_staging.schedulePublishToRemote(actionRequest);
			}
			else if (cmd.equals("unschedule_copy_from_live")) {
				_staging.unscheduleCopyFromLive(actionRequest);
			}
			else if (cmd.equals("unschedule_publish_to_live")) {
				_staging.unschedulePublishToLive(actionRequest);
			}
			else if (cmd.equals("unschedule_publish_to_remote")) {
				_staging.unschedulePublishToRemote(actionRequest);
			}

			sendRedirect(actionRequest, actionResponse, redirect);
		}
		catch (Exception exception) {
			if (exception instanceof PrincipalException) {
				SessionErrors.add(actionRequest, exception.getClass());

				actionResponse.setRenderParameter(
					"mvcPath", "/error/error.jsp");
			}
			else if (exception instanceof AuthException ||
					 exception instanceof DuplicateLockException ||
					 exception instanceof LayoutPrototypeException ||
					 exception instanceof RemoteAuthException ||
					 exception instanceof RemoteExportException ||
					 exception instanceof RemoteOptionsException ||
					 exception instanceof SchedulerException ||
					 exception instanceof SystemException) {

				if (exception instanceof RemoteAuthException) {
					SessionErrors.add(
						actionRequest, AuthException.class, exception);

					sendRedirect(actionRequest, actionResponse, redirect);
				}
				else {
					SessionErrors.add(
						actionRequest, exception.getClass(), exception);
				}
			}
			else if (exception instanceof IllegalArgumentException) {
				SessionErrors.add(
					actionRequest, exception.getClass(), exception);
			}
			else {
				throw exception;
			}
		}
	}

	protected void setLayoutIdMap(ActionRequest actionRequest) {
		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			actionRequest);

		long groupId = ParamUtil.getLong(actionRequest, "groupId");
		boolean privateLayout = ParamUtil.getBoolean(
			actionRequest, "privateLayout");

		String treeId = ParamUtil.getString(actionRequest, "treeId");

		actionRequest.setAttribute(
			"layoutIdMap",
			_exportImportHelper.getSelectedLayoutsJSON(
				groupId, privateLayout,
				SessionTreeJSClicks.getOpenNodes(
					httpServletRequest, treeId + "SelectedNode")));
	}

	@Reference
	private ExportImportHelper _exportImportHelper;

	@Reference
	private Portal _portal;

	@Reference
	private Staging _staging;

}