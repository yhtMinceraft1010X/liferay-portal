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

package com.liferay.change.tracking.web.internal.display.context;

import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.web.internal.scheduler.ScheduledPublishInfo;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Samuel Trong Tran
 */
public class ReschedulePublicationDisplayContext {

	public ReschedulePublicationDisplayContext(
		CTCollection ctCollection, Language language, Portal portal,
		RenderRequest renderRequest, RenderResponse renderResponse,
		ScheduledPublishInfo scheduledPublishInfo) {

		_ctCollection = ctCollection;
		_language = language;
		_portal = portal;

		_renderRequest = renderRequest;

		_httpServletRequest = _portal.getHttpServletRequest(_renderRequest);
		_themeDisplay = (ThemeDisplay)_renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		_renderResponse = renderResponse;
		_scheduledPublishInfo = scheduledPublishInfo;
	}

	public Map<String, Object> getReactData() {
		Calendar calendar = CalendarFactoryUtil.getCalendar(
			_themeDisplay.getTimeZone(), _themeDisplay.getLocale());

		calendar.setTime(_scheduledPublishInfo.getStartDate());

		return HashMapBuilder.<String, Object>put(
			"redirect", getRedirect()
		).put(
			"rescheduleURL",
			() -> PortletURLBuilder.createActionURL(
				_renderResponse
			).setActionName(
				"/change_tracking/reschedule_publication"
			).setRedirect(
				getRedirect()
			).setParameter(
				"ctCollectionId", _ctCollection.getCtCollectionId()
			).buildString()
		).put(
			"scheduledDate",
			StringBundler.concat(
				calendar.get(Calendar.YEAR), StringPool.DASH,
				String.format("%02d", calendar.get(Calendar.MONTH) + 1),
				StringPool.DASH,
				String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)))
		).put(
			"scheduledTime",
			JSONUtil.put(
				"hours",
				String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))
			).put(
				"minutes", String.format("%02d", calendar.get(Calendar.MINUTE))
			)
		).put(
			"spritemap", _themeDisplay.getPathThemeImages() + "/clay/icons.svg"
		).put(
			"timeZone",
			() -> {
				TimeZone timeZone = _themeDisplay.getTimeZone();

				return timeZone.getID();
			}
		).put(
			"unscheduleURL",
			() -> PortletURLBuilder.createActionURL(
				_renderResponse
			).setActionName(
				"/change_tracking/unschedule_publication"
			).setRedirect(
				getRedirect()
			).setParameter(
				"ctCollectionId", _ctCollection.getCtCollectionId()
			).buildString()
		).build();
	}

	public String getRedirect() {
		String redirect = ParamUtil.getString(_renderRequest, "redirect");

		if (Validator.isNotNull(redirect)) {
			return redirect;
		}

		return PortletURLBuilder.createRenderURL(
			_renderResponse
		).setMVCRenderCommandName(
			"/change_tracking/view_scheduled"
		).buildString();
	}

	public String getTitle() {
		return StringBundler.concat(
			_language.get(_httpServletRequest, "reschedule"), ": ",
			_ctCollection.getName());
	}

	private final CTCollection _ctCollection;
	private final HttpServletRequest _httpServletRequest;
	private final Language _language;
	private final Portal _portal;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final ScheduledPublishInfo _scheduledPublishInfo;
	private final ThemeDisplay _themeDisplay;

}