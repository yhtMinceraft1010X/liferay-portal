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

package com.liferay.calendar.internal.trash;

import com.liferay.calendar.constants.CalendarActionKeys;
import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.service.CalendarBookingLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.trash.BaseTrashHandler;
import com.liferay.trash.constants.TrashActionKeys;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pier Paolo Ramon
 */
@Component(immediate = true, service = TrashHandler.class)
public class CalendarBookingTrashHandler extends BaseTrashHandler {

	@Override
	public void deleteTrashEntry(long classPK) throws PortalException {
		_calendarBookingLocalService.deleteCalendarBooking(classPK);
	}

	@Override
	public String getClassName() {
		return CalendarBooking.class.getName();
	}

	@Override
	public boolean isInTrash(long classPK) throws PortalException {
		CalendarBooking calendarBooking =
			_calendarBookingLocalService.getCalendarBooking(classPK);

		return calendarBooking.isInTrash();
	}

	@Override
	public boolean isRestorable(long classPK) throws PortalException {
		CalendarBooking calendarBooking =
			_calendarBookingLocalService.getCalendarBooking(classPK);

		if (!hasTrashPermission(
				PermissionThreadLocal.getPermissionChecker(),
				calendarBooking.getGroupId(), classPK,
				TrashActionKeys.RESTORE)) {

			return false;
		}

		if (calendarBooking.isMasterBooking()) {
			return true;
		}

		return false;
	}

	@Override
	public void restoreTrashEntry(long userId, long classPK)
		throws PortalException {

		_calendarBookingLocalService.restoreCalendarBookingFromTrash(
			userId, classPK);
	}

	@Override
	protected boolean hasPermission(
			PermissionChecker permissionChecker, long classPK, String actionId)
		throws PortalException {

		CalendarBooking calendarBooking =
			_calendarBookingLocalService.getCalendarBooking(classPK);

		return _calendarModelResourcePermission.contains(
			permissionChecker, calendarBooking.getCalendar(),
			CalendarActionKeys.MANAGE_BOOKINGS);
	}

	@Reference
	private CalendarBookingLocalService _calendarBookingLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.calendar.model.Calendar)"
	)
	private ModelResourcePermission<Calendar> _calendarModelResourcePermission;

}