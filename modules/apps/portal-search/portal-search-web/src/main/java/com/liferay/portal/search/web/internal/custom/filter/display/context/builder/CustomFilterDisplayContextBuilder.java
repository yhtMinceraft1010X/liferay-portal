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

package com.liferay.portal.search.web.internal.custom.filter.display.context.builder;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.search.web.internal.custom.filter.configuration.CustomFilterPortletInstanceConfiguration;
import com.liferay.portal.search.web.internal.custom.filter.display.context.CustomFilterDisplayContext;
import com.liferay.portal.search.web.internal.util.SearchOptionalUtil;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author André de Oliveira
 */
public class CustomFilterDisplayContextBuilder {

	public static CustomFilterDisplayContextBuilder builder() {
		return new CustomFilterDisplayContextBuilder();
	}

	public CustomFilterDisplayContext build() throws ConfigurationException {
		CustomFilterDisplayContext customFilterDisplayContext =
			new CustomFilterDisplayContext();

		customFilterDisplayContext.setCustomFilterPortletInstanceConfiguration(
			getCustomFilterPortletInstanceConfiguration());
		customFilterDisplayContext.setDisplayStyleGroupId(
			getDisplayStyleGroupId());
		customFilterDisplayContext.setFilterValue(getFilterValue());
		customFilterDisplayContext.setHeading(getHeading());
		customFilterDisplayContext.setImmutable(_immutable);
		customFilterDisplayContext.setParameterName(_parameterName);
		customFilterDisplayContext.setRenderNothing(isRenderNothing());
		customFilterDisplayContext.setSearchURL(_getURLCurrentPath());

		return customFilterDisplayContext;
	}

	public CustomFilterDisplayContextBuilder customHeadingOptional(
		Optional<String> customHeadingOptional) {

		_customHeadingOptional = customHeadingOptional;

		return this;
	}

	public CustomFilterDisplayContextBuilder disabled(boolean disabled) {
		_disabled = disabled;

		return this;
	}

	public CustomFilterDisplayContextBuilder filterFieldOptional(
		Optional<String> filterFieldOptional) {

		_filterFieldOptional = filterFieldOptional;

		return this;
	}

	public CustomFilterDisplayContextBuilder filterValueOptional(
		Optional<String> filterValueOptional) {

		_filterValueOptional = filterValueOptional;

		return this;
	}

	public CustomFilterDisplayContextBuilder immutable(boolean immutable) {
		_immutable = immutable;

		return this;
	}

	public CustomFilterDisplayContextBuilder parameterName(
		String parameterName) {

		_parameterName = parameterName;

		return this;
	}

	public CustomFilterDisplayContextBuilder parameterValueOptional(
		Optional<String> parameterValueOptional) {

		_parameterValueOptional = parameterValueOptional;

		return this;
	}

	public CustomFilterDisplayContextBuilder queryNameOptional(
		Optional<String> queryNameOptional) {

		_queryNameOptional = queryNameOptional;

		return this;
	}

	public CustomFilterDisplayContextBuilder renderNothing(
		boolean renderNothing) {

		_renderNothing = renderNothing;

		return this;
	}

	public CustomFilterDisplayContextBuilder themeDisplay(
		ThemeDisplay themeDisplay) {

		_themeDisplay = themeDisplay;

		return this;
	}

	protected CustomFilterPortletInstanceConfiguration
			getCustomFilterPortletInstanceConfiguration()
		throws ConfigurationException {

		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		return portletDisplay.getPortletInstanceConfiguration(
			CustomFilterPortletInstanceConfiguration.class);
	}

	protected long getDisplayStyleGroupId() throws ConfigurationException {
		CustomFilterPortletInstanceConfiguration
			customFilterPortletInstanceConfiguration =
				getCustomFilterPortletInstanceConfiguration();

		long displayStyleGroupId =
			customFilterPortletInstanceConfiguration.displayStyleGroupId();

		if (displayStyleGroupId <= 0) {
			displayStyleGroupId = _themeDisplay.getScopeGroupId();
		}

		return displayStyleGroupId;
	}

	protected String getFilterValue() {
		if (_immutable) {
			return SearchOptionalUtil.findFirstPresent(
				Stream.of(_filterValueOptional), StringPool.BLANK);
		}

		return SearchOptionalUtil.findFirstPresent(
			Stream.of(_parameterValueOptional, _filterValueOptional),
			StringPool.BLANK);
	}

	protected String getHeading() {
		return SearchOptionalUtil.findFirstPresent(
			Stream.of(
				_customHeadingOptional, _queryNameOptional,
				_filterFieldOptional),
			"custom");
	}

	protected boolean isRenderNothing() {
		if (_disabled || _renderNothing) {
			return true;
		}

		return false;
	}

	private String _getURLCurrentPath() {
		return HttpComponentsUtil.getPath(_themeDisplay.getURLCurrent());
	}

	private Optional<String> _customHeadingOptional = Optional.empty();
	private boolean _disabled;
	private Optional<String> _filterFieldOptional = Optional.empty();
	private Optional<String> _filterValueOptional = Optional.empty();
	private boolean _immutable;
	private String _parameterName;
	private Optional<String> _parameterValueOptional = Optional.empty();
	private Optional<String> _queryNameOptional = Optional.empty();
	private boolean _renderNothing;
	private ThemeDisplay _themeDisplay;

}