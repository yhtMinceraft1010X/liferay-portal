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

package com.liferay.blogs.web.internal.trackback;

import com.liferay.blogs.linkback.LinkbackConsumer;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.comment.CommentManager;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.PropsValues;

import java.util.function.Function;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alexander Chow
 * @author André de Oliveira
 */
@Component(service = Trackback.class)
public class Trackback {

	public void addTrackback(
			BlogsEntry entry, ThemeDisplay themeDisplay, String excerpt,
			String url, String blogName, String title,
			Function<String, ServiceContext> serviceContextFunction)
		throws PortalException {

		long userId = _userLocalService.getDefaultUserId(
			themeDisplay.getCompanyId());
		long groupId = entry.getGroupId();
		String className = BlogsEntry.class.getName();
		long classPK = entry.getEntryId();

		String body = _buildBody(themeDisplay, excerpt, url);

		long commentId = _commentManager.addComment(
			null, userId, groupId, className, classPK, blogName, title, body,
			serviceContextFunction);

		String entryURL = _buildEntryURL(entry, themeDisplay);

		_linkbackConsumer.addNewTrackback(commentId, url, entryURL);
	}

	private String _buildBBCodeBody(
		ThemeDisplay themeDisplay, String excerpt, String url) {

		url = StringUtil.replace(
			url, new char[] {CharPool.CLOSE_BRACKET, CharPool.OPEN_BRACKET},
			new String[] {"%5D", "%5B"});

		return StringBundler.concat(
			"[...] ", excerpt, " [...] [url=", url, "]",
			themeDisplay.translate("read-more"), "[/url]");
	}

	private String _buildBody(
		ThemeDisplay themeDisplay, String excerpt, String url) {

		if (PropsValues.DISCUSSION_COMMENTS_FORMAT.equals("bbcode")) {
			return _buildBBCodeBody(themeDisplay, excerpt, url);
		}

		return _buildHTMLBody(themeDisplay, excerpt, url);
	}

	private String _buildEntryURL(BlogsEntry entry, ThemeDisplay themeDisplay)
		throws PortalException {

		return StringBundler.concat(
			_portal.getLayoutFullURL(themeDisplay),
			Portal.FRIENDLY_URL_SEPARATOR, "blogs/", entry.getUrlTitle());
	}

	private String _buildHTMLBody(
		ThemeDisplay themeDisplay, String excerpt, String url) {

		return StringBundler.concat(
			"[...] ", excerpt, " [...] <a href=\"", url, "\">",
			themeDisplay.translate("read-more"), "</a>");
	}

	@Reference
	private CommentManager _commentManager;

	@Reference
	private LinkbackConsumer _linkbackConsumer;

	@Reference
	private Portal _portal;

	@Reference
	private UserLocalService _userLocalService;

}