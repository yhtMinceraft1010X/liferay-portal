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

package com.liferay.invitation.invite.members.web.internal.notifications;

import com.liferay.invitation.invite.members.constants.InviteMembersPortletKeys;
import com.liferay.invitation.invite.members.model.MemberRequest;
import com.liferay.invitation.invite.members.service.MemberRequestLocalService;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.MembershipRequestConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.notifications.BaseUserNotificationHandler;
import com.liferay.portal.kernel.notifications.UserNotificationHandler;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.UserNotificationEventLocalService;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ResourceBundle;

import javax.portlet.WindowState;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jonathan Lee
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + InviteMembersPortletKeys.INVITE_MEMBERS,
	service = UserNotificationHandler.class
)
public class InviteMembersUserNotificationHandler
	extends BaseUserNotificationHandler {

	public InviteMembersUserNotificationHandler() {
		setActionable(true);
		setPortletId(InviteMembersPortletKeys.INVITE_MEMBERS);
	}

	@Override
	protected String getBody(
			UserNotificationEvent userNotificationEvent,
			ServiceContext serviceContext)
		throws Exception {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			userNotificationEvent.getPayload());

		long memberRequestId = jsonObject.getLong("classPK");

		MemberRequest memberRequest =
			_memberRequestLocalService.fetchMemberRequest(memberRequestId);

		if (memberRequest.getStatus() !=
				MembershipRequestConstants.STATUS_PENDING) {

			return StringPool.BLANK;
		}

		Group group = null;

		if (memberRequest != null) {
			group = _groupLocalService.fetchGroup(memberRequest.getGroupId());
		}

		if ((group == null) || (memberRequest == null)) {
			_userNotificationEventLocalService.deleteUserNotificationEvent(
				userNotificationEvent.getUserNotificationEventId());

			return null;
		}

		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			serviceContext.getLocale(),
			InviteMembersUserNotificationHandler.class);

		String title = ResourceBundleUtil.getString(
			resourceBundle, "x-invited-you-to-join-x",
			_getUserNameLink(memberRequest.getUserId(), serviceContext),
			_getSiteDescriptiveName(
				memberRequest.getGroupId(), serviceContext));

		LiferayPortletResponse liferayPortletResponse =
			serviceContext.getLiferayPortletResponse();

		return StringUtil.replace(
			getBodyTemplate(),
			new String[] {
				"[$CONFIRM$]", "[$CONFIRM_URL$]", "[$IGNORE$]",
				"[$IGNORE_URL$]", "[$TITLE$]"
			},
			new String[] {
				serviceContext.translate("confirm"),
				PortletURLBuilder.createActionURL(
					liferayPortletResponse,
					InviteMembersPortletKeys.INVITE_MEMBERS
				).setActionName(
					"updateMemberRequest"
				).setParameter(
					"memberRequestId", memberRequestId
				).setParameter(
					"status", MembershipRequestConstants.STATUS_APPROVED
				).setParameter(
					"userNotificationEventId",
					userNotificationEvent.getUserNotificationEventId()
				).setWindowState(
					WindowState.NORMAL
				).buildString(),
				serviceContext.translate("ignore"),
				PortletURLBuilder.createActionURL(
					liferayPortletResponse,
					InviteMembersPortletKeys.INVITE_MEMBERS
				).setActionName(
					"updateMemberRequest"
				).setParameter(
					"memberRequestId", memberRequestId
				).setParameter(
					"status", MembershipRequestConstants.STATUS_DENIED
				).setParameter(
					"userNotificationEventId",
					userNotificationEvent.getUserNotificationEventId()
				).setWindowState(
					WindowState.NORMAL
				).buildString(),
				title
			});
	}

	@Override
	protected String getLink(
			UserNotificationEvent userNotificationEvent,
			ServiceContext serviceContext)
		throws Exception {

		return StringPool.BLANK;
	}

	@Reference(unbind = "-")
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	@Reference(unbind = "-")
	protected void setMemberRequestLocalService(
		MemberRequestLocalService memberRequestLocalService) {

		_memberRequestLocalService = memberRequestLocalService;
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	@Reference(unbind = "-")
	protected void setUserNotificationEventLocalService(
		UserNotificationEventLocalService userNotificationEventLocalService) {

		_userNotificationEventLocalService = userNotificationEventLocalService;
	}

	private String _getSiteDescriptiveName(
			long groupId, ServiceContext serviceContext)
		throws Exception {

		Group group = _groupLocalService.getGroup(groupId);

		StringBundler sb = new StringBundler(6);

		sb.append("<a");

		if (group.hasPublicLayouts()) {
			sb.append(" href=\"");
			sb.append(
				_portal.getGroupFriendlyURL(
					group.getPublicLayoutSet(),
					serviceContext.getThemeDisplay(), false, false));
			sb.append("\">");
		}
		else {
			sb.append(">");
		}

		sb.append(
			HtmlUtil.escape(
				group.getDescriptiveName(serviceContext.getLocale())));
		sb.append("</a>");

		return sb.toString();
	}

	private String _getUserNameLink(
		long userId, ServiceContext serviceContext) {

		try {
			if (userId <= 0) {
				return StringPool.BLANK;
			}

			User user = _userLocalService.getUserById(userId);

			String userName = user.getFullName();

			String userDisplayURL = user.getDisplayURL(
				serviceContext.getThemeDisplay());

			return StringBundler.concat(
				"<a href=\"", userDisplayURL, "\">", HtmlUtil.escape(userName),
				"</a>");
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return StringPool.BLANK;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		InviteMembersUserNotificationHandler.class);

	private GroupLocalService _groupLocalService;
	private MemberRequestLocalService _memberRequestLocalService;

	@Reference
	private Portal _portal;

	private UserLocalService _userLocalService;
	private UserNotificationEventLocalService
		_userNotificationEventLocalService;

}