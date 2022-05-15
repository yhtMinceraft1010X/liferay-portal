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

package com.liferay.comment.taglib.internal.struts;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.message.boards.exception.DiscussionMaxCommentsException;
import com.liferay.message.boards.exception.MessageBodyException;
import com.liferay.message.boards.exception.NoSuchMessageException;
import com.liferay.message.boards.exception.RequiredMessageException;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.comment.CommentManager;
import com.liferay.portal.kernel.comment.DiscussionPermission;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFunction;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.servlet.NamespaceServletRequest;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;

import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	immediate = true, property = "path=/portal/comment/discussion/edit",
	service = StrutsAction.class
)
public class EditDiscussionStrutsAction implements StrutsAction {

	@Override
	public String execute(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		AuthTokenUtil.checkCSRFToken(
			httpServletRequest, EditDiscussionStrutsAction.class.getName());

		String namespace = ParamUtil.getString(httpServletRequest, "namespace");

		HttpServletRequest namespacedHttpServletRequest =
			new NamespaceServletRequest(
				httpServletRequest, StringPool.BLANK, namespace);

		String cmd = ParamUtil.getString(
			namespacedHttpServletRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				long commentId = _updateComment(namespacedHttpServletRequest);

				boolean ajax = ParamUtil.getBoolean(
					namespacedHttpServletRequest, "ajax", true);

				if (ajax) {
					_writeJSON(
						httpServletResponse,
						JSONUtil.put(
							"commentId", commentId
						).put(
							"randomNamespace",
							ParamUtil.getString(
								namespacedHttpServletRequest, "randomNamespace")
						));

					return null;
				}
			}
			else if (cmd.equals(Constants.DELETE)) {
				_deleteComment(namespacedHttpServletRequest);
			}
			else if (cmd.equals(Constants.SUBSCRIBE_TO_COMMENTS)) {
				_subscribeToComments(namespacedHttpServletRequest, true);
			}
			else if (cmd.equals(Constants.UNSUBSCRIBE_FROM_COMMENTS)) {
				_subscribeToComments(namespacedHttpServletRequest, false);
			}

			String redirect = _portal.escapeRedirect(
				ParamUtil.getString(namespacedHttpServletRequest, "redirect"));

			if (Validator.isNotNull(redirect)) {
				httpServletResponse.sendRedirect(redirect);
			}
		}
		catch (DiscussionMaxCommentsException | MessageBodyException |
			   NoSuchMessageException | PrincipalException |
			   RequiredMessageException exception) {

			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			jsonObject.putException(exception);

			_writeJSON(httpServletResponse, jsonObject);
		}

		return null;
	}

	private void _deleteComment(HttpServletRequest httpServletRequest)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		long commentId = ParamUtil.getLong(httpServletRequest, "commentId");

		DiscussionPermission discussionPermission = _getDiscussionPermission(
			themeDisplay);

		discussionPermission.checkDeletePermission(commentId);

		_commentManager.deleteComment(commentId);
	}

	private DiscussionPermission _getDiscussionPermission(
			ThemeDisplay themeDisplay)
		throws Exception {

		DiscussionPermission discussionPermission =
			_commentManager.getDiscussionPermission(
				themeDisplay.getPermissionChecker());

		if (discussionPermission == null) {
			throw new PrincipalException("Discussion permission is null");
		}

		return discussionPermission;
	}

	private void _subscribeToComments(
			HttpServletRequest httpServletRequest, boolean subscribe)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		DiscussionPermission discussionPermission = _getDiscussionPermission(
			themeDisplay);

		String className = ParamUtil.getString(httpServletRequest, "className");
		long classPK = ParamUtil.getLong(httpServletRequest, "classPK");

		AssetEntry assetEntry = _assetEntryLocalService.getEntry(
			className, classPK);

		discussionPermission.checkSubscribePermission(
			assetEntry.getCompanyId(), assetEntry.getGroupId(),
			assetEntry.getClassName(), assetEntry.getClassPK());

		if (subscribe) {
			_commentManager.subscribeDiscussion(
				themeDisplay.getUserId(), assetEntry.getGroupId(),
				assetEntry.getClassName(), assetEntry.getClassPK());
		}
		else {
			_commentManager.unsubscribeDiscussion(
				themeDisplay.getUserId(), assetEntry.getClassName(),
				assetEntry.getClassPK());
		}
	}

	private long _updateComment(HttpServletRequest httpServletRequest)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		long commentId = ParamUtil.getLong(httpServletRequest, "commentId");

		String className = ParamUtil.getString(httpServletRequest, "className");
		long classPK = ParamUtil.getLong(httpServletRequest, "classPK");
		String subject = ParamUtil.getString(httpServletRequest, "subject");
		String body = ParamUtil.getString(httpServletRequest, "body");

		Function<String, ServiceContext> serviceContextFunction =
			new ServiceContextFunction(httpServletRequest);

		DiscussionPermission discussionPermission = _getDiscussionPermission(
			themeDisplay);

		if (commentId <= 0) {

			// Add message

			User user = null;

			if (themeDisplay.isSignedIn()) {
				user = themeDisplay.getUser();
			}
			else {
				String emailAddress = ParamUtil.getString(
					httpServletRequest, "emailAddress");

				user = _userLocalService.fetchUserByEmailAddress(
					themeDisplay.getCompanyId(), emailAddress);

				if ((user == null) ||
					(user.getStatus() != WorkflowConstants.STATUS_INCOMPLETE)) {

					return 0;
				}
			}

			long parentCommentId = ParamUtil.getLong(
				httpServletRequest, "parentCommentId");

			String name = PrincipalThreadLocal.getName();

			PrincipalThreadLocal.setName(user.getUserId());

			try {
				discussionPermission.checkAddPermission(
					themeDisplay.getCompanyId(), themeDisplay.getScopeGroupId(),
					className, classPK);

				commentId = _commentManager.addComment(
					null, user.getUserId(), className, classPK,
					user.getFullName(), parentCommentId, subject, body,
					serviceContextFunction);
			}
			finally {
				PrincipalThreadLocal.setName(name);
			}
		}
		else {

			// Update message

			discussionPermission.checkUpdatePermission(commentId);

			commentId = _commentManager.updateComment(
				themeDisplay.getUserId(), className, classPK, commentId,
				subject, body, serviceContextFunction);
		}

		// Subscription

		if (PropsValues.DISCUSSION_SUBSCRIBE) {
			_commentManager.subscribeDiscussion(
				themeDisplay.getUserId(), themeDisplay.getScopeGroupId(),
				className, classPK);
		}

		return commentId;
	}

	private void _writeJSON(
			HttpServletResponse httpServletResponse, Object object)
		throws IOException {

		httpServletResponse.setContentType(ContentTypes.APPLICATION_JSON);

		ServletResponseUtil.write(httpServletResponse, object.toString());

		httpServletResponse.flushBuffer();
	}

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private CommentManager _commentManager;

	@Reference
	private Portal _portal;

	@Reference
	private UserLocalService _userLocalService;

}