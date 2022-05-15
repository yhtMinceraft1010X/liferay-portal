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

package com.liferay.calendar.internal.notification;

import com.liferay.calendar.configuration.CalendarServiceConfigurationValues;
import com.liferay.calendar.constants.CalendarPortletKeys;
import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.notification.NotificationTemplateContext;
import com.liferay.calendar.notification.NotificationTemplateType;
import com.liferay.calendar.notification.NotificationType;
import com.liferay.calendar.service.CalendarBookingLocalService;
import com.liferay.calendar.service.CalendarNotificationTemplateLocalServiceUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.CalendarUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.text.Format;

import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import javax.portlet.WindowState;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo Lundgren
 */
@Component(immediate = true, service = {})
public class NotificationTemplateContextFactory {

	public static NotificationTemplateContext getInstance(
			NotificationType notificationType,
			NotificationTemplateType notificationTemplateType,
			CalendarBooking calendarBooking, User user)
		throws Exception {

		CalendarBooking parentCalendarBooking =
			calendarBooking.getParentCalendarBooking();

		Calendar calendar = parentCalendarBooking.getCalendar();

		NotificationTemplateContext notificationTemplateContext =
			new NotificationTemplateContext(
				CalendarServiceConfigurationValues.
					CALENDAR_NOTIFICATION_DEFAULT_TYPE);

		notificationTemplateContext.setCalendarNotificationTemplate(
			CalendarNotificationTemplateLocalServiceUtil.
				fetchCalendarNotificationTemplate(
					calendar.getCalendarId(), notificationType,
					notificationTemplateType));
		notificationTemplateContext.setCompanyId(
			calendarBooking.getCompanyId());
		notificationTemplateContext.setGroupId(calendarBooking.getGroupId());
		notificationTemplateContext.setCalendarId(calendar.getCalendarId());
		notificationTemplateContext.setNotificationTemplateType(
			notificationTemplateType);
		notificationTemplateContext.setNotificationType(notificationType);

		// Attributes

		Format userDateTimeFormat = _getUserDateTimeFormat(
			calendarBooking, user);

		String userTimezoneDisplayName = _getUserTimezoneDisplayName(user);

		Map<String, Serializable> attributes =
			HashMapBuilder.<String, Serializable>put(
				"calendarBookingId", calendarBooking.getCalendarBookingId()
			).put(
				"calendarName", calendar.getName(user.getLocale(), true)
			).put(
				"endTime",
				StringBundler.concat(
					userDateTimeFormat.format(calendarBooking.getEndTime()),
					StringPool.SPACE, userTimezoneDisplayName)
			).put(
				"icsFile",
				() -> {
					if (Objects.equals(
							notificationTemplateContext.
								getNotificationTemplateType(),
							NotificationTemplateType.INVITE)) {

						String calendarBookingString =
							_calendarBookingLocalService.exportCalendarBooking(
								calendarBooking.getCalendarBookingId(),
								CalendarUtil.ICAL_EXTENSION);

						return FileUtil.createTempFile(
							calendarBookingString.getBytes());
					}

					return null;
				}
			).put(
				"location", calendarBooking.getLocation()
			).put(
				"portalURL",
				() -> {
					Group group = _groupLocalService.getGroup(
						user.getCompanyId(), GroupConstants.GUEST);

					return _getPortalURL(
						group.getCompanyId(), group.getGroupId());
				}
			).put(
				"portletName",
				LanguageUtil.get(
					user.getLocale(),
					"javax.portlet.title.".concat(CalendarPortletKeys.CALENDAR))
			).put(
				"siteName",
				() -> {
					Group calendarGroup = _groupLocalService.getGroup(
						calendar.getGroupId());

					if (calendarGroup.isSite()) {
						return calendarGroup.getName(user.getLocale(), true);
					}

					return StringPool.BLANK;
				}
			).put(
				"startTime",
				StringBundler.concat(
					userDateTimeFormat.format(calendarBooking.getStartTime()),
					StringPool.SPACE, userTimezoneDisplayName)
			).put(
				"title", calendarBooking.getTitle(user.getLocale(), true)
			).put(
				"url",
				_getCalendarBookingURL(
					user, calendarBooking.getCalendarBookingId())
			).build();

		notificationTemplateContext.setAttributes(attributes);

		return notificationTemplateContext;
	}

	public static NotificationTemplateContext getInstance(
			NotificationType notificationType,
			NotificationTemplateType notificationTemplateType,
			CalendarBooking calendarBooking, User user,
			ServiceContext serviceContext)
		throws Exception {

		NotificationTemplateContext notificationTemplateContext = getInstance(
			notificationType, notificationTemplateType, calendarBooking, user);

		if ((serviceContext != null) &&
			Validator.isNotNull(
				serviceContext.getAttribute("instanceStartTime"))) {

			long instanceStartTime = (long)serviceContext.getAttribute(
				"instanceStartTime");

			Format userDateTimeFormat = _getUserDateTimeFormat(
				calendarBooking, user);

			String userTimezoneDisplayName = _getUserTimezoneDisplayName(user);

			String instanceStartTimeFormatted =
				userDateTimeFormat.format(instanceStartTime) +
					StringPool.SPACE + userTimezoneDisplayName;

			notificationTemplateContext.setAttribute(
				"instanceStartTime", instanceStartTimeFormatted);
		}

		return notificationTemplateContext;
	}

	@Reference(unbind = "-")
	protected void setCalendarBookingLocalService(
		CalendarBookingLocalService calendarBookingLocalService) {

		_calendarBookingLocalService = calendarBookingLocalService;
	}

	@Reference(unbind = "-")
	protected void setCompanyLocalService(
		CompanyLocalService companyLocalService) {

		_companyLocalService = companyLocalService;
	}

	@Reference(unbind = "-")
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	@Reference(unbind = "-")
	protected void setLayoutLocalService(
		LayoutLocalService layoutLocalService) {

		_layoutLocalService = layoutLocalService;
	}

	private static String _getCalendarBookingURL(
			User user, long calendarBookingId)
		throws Exception {

		Group group = _groupLocalService.getGroup(
			user.getCompanyId(), GroupConstants.GUEST);

		Layout layout = _layoutLocalService.fetchLayout(
			group.getDefaultPublicPlid());

		String portalURL = _getPortalURL(
			group.getCompanyId(), group.getGroupId());

		String layoutActualURL = PortalUtil.getLayoutActualURL(layout);

		String url = portalURL + layoutActualURL;

		String namespace = PortalUtil.getPortletNamespace(
			CalendarPortletKeys.CALENDAR);

		url = HttpComponentsUtil.addParameter(
			url, namespace + "mvcPath", "/view_calendar_booking.jsp");

		url = HttpComponentsUtil.addParameter(
			url, "p_p_id", CalendarPortletKeys.CALENDAR);
		url = HttpComponentsUtil.addParameter(url, "p_p_lifecycle", "0");
		url = HttpComponentsUtil.addParameter(
			url, "p_p_state", WindowState.MAXIMIZED.toString());
		url = HttpComponentsUtil.addParameter(
			url, namespace + "calendarBookingId", calendarBookingId);

		return url;
	}

	private static String _getPortalURL(long companyId, long groupId)
		throws PortalException {

		Company company = _companyLocalService.getCompany(companyId);

		return company.getPortalURL(groupId);
	}

	private static Format _getUserDateTimeFormat(
		CalendarBooking calendarBooking, User user) {

		TimeZone userTimeZone = user.getTimeZone();

		if ((calendarBooking != null) && calendarBooking.isAllDay()) {
			userTimeZone = TimeZone.getTimeZone(StringPool.UTC);
		}

		return FastDateFormatFactoryUtil.getDateTime(
			user.getLocale(), userTimeZone);
	}

	private static String _getUserTimezoneDisplayName(User user) {
		TimeZone userTimeZone = user.getTimeZone();

		return userTimeZone.getDisplayName(
			false, TimeZone.SHORT, user.getLocale());
	}

	private static CalendarBookingLocalService _calendarBookingLocalService;
	private static CompanyLocalService _companyLocalService;
	private static GroupLocalService _groupLocalService;
	private static LayoutLocalService _layoutLocalService;

}