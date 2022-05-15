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

package com.liferay.blogs.service;

import com.liferay.blogs.model.BlogsEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

/**
 * Provides the remote service utility for BlogsEntry. This utility wraps
 * <code>com.liferay.blogs.service.impl.BlogsEntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see BlogsEntryService
 * @generated
 */
public class BlogsEntryServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.blogs.service.impl.BlogsEntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.kernel.repository.model.Folder
			addAttachmentsFolder(long groupId)
		throws PortalException {

		return getService().addAttachmentsFolder(groupId);
	}

	public static BlogsEntry addEntry(
			String title, String subtitle, String description, String content,
			int displayDateMonth, int displayDateDay, int displayDateYear,
			int displayDateHour, int displayDateMinute, boolean allowPingbacks,
			boolean allowTrackbacks, String[] trackbacks,
			String coverImageCaption,
			com.liferay.portal.kernel.servlet.taglib.ui.ImageSelector
				coverImageImageSelector,
			com.liferay.portal.kernel.servlet.taglib.ui.ImageSelector
				smallImageImageSelector,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addEntry(
			title, subtitle, description, content, displayDateMonth,
			displayDateDay, displayDateYear, displayDateHour, displayDateMinute,
			allowPingbacks, allowTrackbacks, trackbacks, coverImageCaption,
			coverImageImageSelector, smallImageImageSelector, serviceContext);
	}

	public static BlogsEntry addEntry(
			String externalReferenceCode, String title, String subtitle,
			String urlTitle, String description, String content,
			int displayDateMonth, int displayDateDay, int displayDateYear,
			int displayDateHour, int displayDateMinute, boolean allowPingbacks,
			boolean allowTrackbacks, String[] trackbacks,
			String coverImageCaption,
			com.liferay.portal.kernel.servlet.taglib.ui.ImageSelector
				coverImageImageSelector,
			com.liferay.portal.kernel.servlet.taglib.ui.ImageSelector
				smallImageImageSelector,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addEntry(
			externalReferenceCode, title, subtitle, urlTitle, description,
			content, displayDateMonth, displayDateDay, displayDateYear,
			displayDateHour, displayDateMinute, allowPingbacks, allowTrackbacks,
			trackbacks, coverImageCaption, coverImageImageSelector,
			smallImageImageSelector, serviceContext);
	}

	public static void deleteEntry(long entryId) throws PortalException {
		getService().deleteEntry(entryId);
	}

	public static BlogsEntry fetchBlogsEntryByExternalReferenceCode(
			long groupId, String externalReferenceCode)
		throws PortalException {

		return getService().fetchBlogsEntryByExternalReferenceCode(
			groupId, externalReferenceCode);
	}

	public static List<BlogsEntry> getCompanyEntries(
			long companyId, java.util.Date displayDate, int status, int max)
		throws PortalException {

		return getService().getCompanyEntries(
			companyId, displayDate, status, max);
	}

	public static String getCompanyEntriesRSS(
			long companyId, java.util.Date displayDate, int status, int max,
			String type, double version, String displayStyle, String feedURL,
			String entryURL,
			com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay)
		throws PortalException {

		return getService().getCompanyEntriesRSS(
			companyId, displayDate, status, max, type, version, displayStyle,
			feedURL, entryURL, themeDisplay);
	}

	public static BlogsEntry[] getEntriesPrevAndNext(long entryId)
		throws PortalException {

		return getService().getEntriesPrevAndNext(entryId);
	}

	public static BlogsEntry getEntry(long entryId) throws PortalException {
		return getService().getEntry(entryId);
	}

	public static BlogsEntry getEntry(long groupId, String urlTitle)
		throws PortalException {

		return getService().getEntry(groupId, urlTitle);
	}

	public static List<BlogsEntry> getGroupEntries(
		long groupId, java.util.Date displayDate, int status, int max) {

		return getService().getGroupEntries(groupId, displayDate, status, max);
	}

	public static List<BlogsEntry> getGroupEntries(
		long groupId, java.util.Date displayDate, int status, int start,
		int end) {

		return getService().getGroupEntries(
			groupId, displayDate, status, start, end);
	}

	public static List<BlogsEntry> getGroupEntries(
		long groupId, int status, int max) {

		return getService().getGroupEntries(groupId, status, max);
	}

	public static List<BlogsEntry> getGroupEntries(
		long groupId, int status, int start, int end) {

		return getService().getGroupEntries(groupId, status, start, end);
	}

	public static List<BlogsEntry> getGroupEntries(
		long groupId, int status, int start, int end,
		OrderByComparator<BlogsEntry> orderByComparator) {

		return getService().getGroupEntries(
			groupId, status, start, end, orderByComparator);
	}

	public static int getGroupEntriesCount(
		long groupId, java.util.Date displayDate, int status) {

		return getService().getGroupEntriesCount(groupId, displayDate, status);
	}

	public static int getGroupEntriesCount(long groupId, int status) {
		return getService().getGroupEntriesCount(groupId, status);
	}

	public static String getGroupEntriesRSS(
			long groupId, java.util.Date displayDate, int status, int max,
			String type, double version, String displayStyle, String feedURL,
			String entryURL,
			com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay)
		throws PortalException {

		return getService().getGroupEntriesRSS(
			groupId, displayDate, status, max, type, version, displayStyle,
			feedURL, entryURL, themeDisplay);
	}

	public static List<BlogsEntry> getGroupsEntries(
			long companyId, long groupId, java.util.Date displayDate,
			int status, int max)
		throws PortalException {

		return getService().getGroupsEntries(
			companyId, groupId, displayDate, status, max);
	}

	public static List<BlogsEntry> getGroupUserEntries(
		long groupId, long userId, int status, int start, int end,
		OrderByComparator<BlogsEntry> orderByComparator) {

		return getService().getGroupUserEntries(
			groupId, userId, status, start, end, orderByComparator);
	}

	public static List<BlogsEntry> getGroupUserEntries(
		long groupId, long userId, int[] statuses, int start, int end,
		OrderByComparator<BlogsEntry> orderByComparator) {

		return getService().getGroupUserEntries(
			groupId, userId, statuses, start, end, orderByComparator);
	}

	public static int getGroupUserEntriesCount(
		long groupId, long userId, int status) {

		return getService().getGroupUserEntriesCount(groupId, userId, status);
	}

	public static int getGroupUserEntriesCount(
		long groupId, long userId, int[] statuses) {

		return getService().getGroupUserEntriesCount(groupId, userId, statuses);
	}

	public static List<BlogsEntry> getOrganizationEntries(
			long organizationId, java.util.Date displayDate, int status,
			int max)
		throws PortalException {

		return getService().getOrganizationEntries(
			organizationId, displayDate, status, max);
	}

	public static String getOrganizationEntriesRSS(
			long organizationId, java.util.Date displayDate, int status,
			int max, String type, double version, String displayStyle,
			String feedURL, String entryURL,
			com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay)
		throws PortalException {

		return getService().getOrganizationEntriesRSS(
			organizationId, displayDate, status, max, type, version,
			displayStyle, feedURL, entryURL, themeDisplay);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static BlogsEntry moveEntryToTrash(long entryId)
		throws PortalException {

		return getService().moveEntryToTrash(entryId);
	}

	public static void restoreEntryFromTrash(long entryId)
		throws PortalException {

		getService().restoreEntryFromTrash(entryId);
	}

	public static void subscribe(long groupId) throws PortalException {
		getService().subscribe(groupId);
	}

	public static void unsubscribe(long groupId) throws PortalException {
		getService().unsubscribe(groupId);
	}

	public static BlogsEntry updateEntry(
			long entryId, String title, String subtitle, String description,
			String content, int displayDateMonth, int displayDateDay,
			int displayDateYear, int displayDateHour, int displayDateMinute,
			boolean allowPingbacks, boolean allowTrackbacks,
			String[] trackbacks, String coverImageCaption,
			com.liferay.portal.kernel.servlet.taglib.ui.ImageSelector
				coverImageImageSelector,
			com.liferay.portal.kernel.servlet.taglib.ui.ImageSelector
				smallImageImageSelector,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateEntry(
			entryId, title, subtitle, description, content, displayDateMonth,
			displayDateDay, displayDateYear, displayDateHour, displayDateMinute,
			allowPingbacks, allowTrackbacks, trackbacks, coverImageCaption,
			coverImageImageSelector, smallImageImageSelector, serviceContext);
	}

	public static BlogsEntry updateEntry(
			long entryId, String title, String subtitle, String urlTitle,
			String description, String content, int displayDateMonth,
			int displayDateDay, int displayDateYear, int displayDateHour,
			int displayDateMinute, boolean allowPingbacks,
			boolean allowTrackbacks, String[] trackbacks,
			String coverImageCaption,
			com.liferay.portal.kernel.servlet.taglib.ui.ImageSelector
				coverImageImageSelector,
			com.liferay.portal.kernel.servlet.taglib.ui.ImageSelector
				smallImageImageSelector,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateEntry(
			entryId, title, subtitle, urlTitle, description, content,
			displayDateMonth, displayDateDay, displayDateYear, displayDateHour,
			displayDateMinute, allowPingbacks, allowTrackbacks, trackbacks,
			coverImageCaption, coverImageImageSelector, smallImageImageSelector,
			serviceContext);
	}

	public static BlogsEntryService getService() {
		return _service;
	}

	private static volatile BlogsEntryService _service;

}