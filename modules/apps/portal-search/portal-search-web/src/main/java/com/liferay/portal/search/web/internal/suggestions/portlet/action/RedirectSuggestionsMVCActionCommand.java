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

package com.liferay.portal.search.web.internal.suggestions.portlet.action;

import com.liferay.petra.string.CharPool;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.search.web.internal.display.context.PortletRequestThemeDisplaySupplier;
import com.liferay.portal.search.web.internal.display.context.ThemeDisplaySupplier;
import com.liferay.portal.search.web.internal.search.bar.portlet.SearchBarPortletPreferences;
import com.liferay.portal.search.web.internal.search.bar.portlet.SearchBarPortletPreferencesImpl;
import com.liferay.portal.search.web.internal.suggestions.constants.SuggestionsPortletKeys;
import com.liferay.portal.search.web.internal.util.SearchStringUtil;

import java.util.Optional;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adam Brandizzi
 */
@Component(
	property = {
		"javax.portlet.name=" + SuggestionsPortletKeys.SUGGESTIONS,
		"mvc.command.name=/portal_search/redirect_suggestions"
	},
	service = MVCActionCommand.class
)
public class RedirectSuggestionsMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		hideDefaultSuccessMessage(actionRequest);

		SearchBarPortletPreferences searchBarPortletPreferences =
			new SearchBarPortletPreferencesImpl(
				Optional.ofNullable(actionRequest.getPreferences()));

		String redirectURL = _getRedirectURL(
			actionRequest, searchBarPortletPreferences);

		redirectURL = _addParameters(
			redirectURL, actionRequest,
			searchBarPortletPreferences.getKeywordsParameterName(),
			searchBarPortletPreferences.getScopeParameterName());

		actionResponse.sendRedirect(portal.escapeRedirect(redirectURL));
	}

	@Reference
	protected Portal portal;

	private String _addParameter(
		String url, PortletRequest portletRequest, String parameterName) {

		Optional<String> parameterValueOptional = SearchStringUtil.maybe(
			portletRequest.getParameter(parameterName));

		Optional<String> urlOptional = parameterValueOptional.map(
			parameterValue -> HttpComponentsUtil.addParameter(
				url, parameterName, parameterValue));

		return urlOptional.orElse(url);
	}

	private String _addParameters(
		String url, PortletRequest portletRequest, String... parameterNames) {

		for (String parameterName : parameterNames) {
			url = _addParameter(url, portletRequest, parameterName);
		}

		return url;
	}

	private String _getFriendlyURL(ThemeDisplay themeDisplay) {
		Layout layout = themeDisplay.getLayout();

		return layout.getFriendlyURL(themeDisplay.getLocale());
	}

	private String _getPath(String path, String destination) {
		if (destination.charAt(0) == CharPool.SLASH) {
			return path.concat(destination);
		}

		return path + CharPool.SLASH + destination;
	}

	private String _getRedirectURL(
		ActionRequest actionRequest,
		SearchBarPortletPreferences searchBarPortletPreferences) {

		ThemeDisplay themeDisplay = _getThemeDisplay(actionRequest);

		String url = themeDisplay.getURLCurrent();

		String friendlyURL = _getFriendlyURL(themeDisplay);

		String path = url.substring(0, url.indexOf(friendlyURL));

		Optional<String> destinationOptional =
			searchBarPortletPreferences.getDestination();

		String destination = destinationOptional.orElse(friendlyURL);

		return _getPath(path, destination);
	}

	private ThemeDisplay _getThemeDisplay(ActionRequest actionRequest) {
		ThemeDisplaySupplier themeDisplaySupplier =
			new PortletRequestThemeDisplaySupplier(actionRequest);

		return themeDisplaySupplier.getThemeDisplay();
	}

}