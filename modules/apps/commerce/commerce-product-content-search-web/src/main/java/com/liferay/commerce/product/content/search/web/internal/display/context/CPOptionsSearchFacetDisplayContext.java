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

package com.liferay.commerce.product.content.search.web.internal.display.context;

import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.product.content.search.web.internal.util.CPOptionFacetsUtil;
import com.liferay.commerce.product.display.context.helper.CPRequestHelper;
import com.liferay.commerce.product.model.CPOption;
import com.liferay.commerce.product.service.CPOptionLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchResponse;

import java.io.Serializable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.portlet.RenderRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andrea Sbarra
 */
public class CPOptionsSearchFacetDisplayContext implements Serializable {

	public CPOptionsSearchFacetDisplayContext(
			HttpServletRequest httpServletRequest)
		throws ConfigurationException {

		_httpServletRequest = httpServletRequest;

		_cpRequestHelper = new CPRequestHelper(httpServletRequest);

		_renderRequest = _cpRequestHelper.getRenderRequest();
	}

	public CPOption getCPOption(long companyId, String fieldName) {
		String cpOptionKey =
			CPOptionFacetsUtil.getCPOptionKeyFromIndexFieldName(fieldName);

		return _cpOptionLocalService.fetchCPOption(companyId, cpOptionKey);
	}

	public String getCPOptionKey(long companyId, String fieldName)
		throws PortalException {

		CPOption cpOption = getCPOption(companyId, fieldName);

		return cpOption.getKey();
	}

	public String getCPOptionName(long companyId, String fieldName)
		throws PortalException {

		CPOption cpOption = getCPOption(companyId, fieldName);

		String name = StringPool.BLANK;

		if (cpOption != null) {
			name = cpOption.getName(_locale);
		}

		return name;
	}

	public long getDisplayStyleGroupId() {
		if (_displayStyleGroupId != 0) {
			return _displayStyleGroupId;
		}

		long displayStyleGroupId = _DISPLAY_STYLE_GROUP_ID;

		if (displayStyleGroupId <= 0) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)_httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			displayStyleGroupId = themeDisplay.getScopeGroupId();
		}

		_displayStyleGroupId = displayStyleGroupId;

		return _displayStyleGroupId;
	}

	public List<Facet> getFacets() {
		return _facets;
	}

	public String getPaginationStartParameterName() {
		return _paginationStartParameterName;
	}

	public List<CPOptionsSearchFacetTermDisplayContext>
		getTermDisplayContexts() {

		return _cpOptionsSearchFacetTermDisplayContext;
	}

	public boolean hasCommerceChannel() throws PortalException {
		CommerceContext commerceContext =
			(CommerceContext)_renderRequest.getAttribute(
				CommerceWebKeys.COMMERCE_CONTEXT);

		long commerceChannelId = commerceContext.getCommerceChannelId();

		if (commerceChannelId > 0) {
			return true;
		}

		return false;
	}

	public boolean isCPOptionValueSelected(
		long companyId, String fieldName, String fieldValue) {

		CPOption cpOption = getCPOption(companyId, fieldName);

		Optional<String[]> parameterValuesOptional =
			_portletSharedSearchResponse.getParameterValues(
				cpOption.getKey(), _renderRequest);

		if (parameterValuesOptional.isPresent()) {
			String[] parameterValues = parameterValuesOptional.get();

			return ArrayUtil.contains(parameterValues, fieldValue);
		}

		return false;
	}

	public void setCPOptionLocalService(
		CPOptionLocalService cpOptionLocalService) {

		_cpOptionLocalService = cpOptionLocalService;
	}

	public void setFacets(List<Facet> facets) {
		_facets = facets;
	}

	public void setLocale(Locale locale) {
		_locale = locale;
	}

	public void setPaginationStartParameterName(
		String paginationStartParameterName) {

		_paginationStartParameterName = paginationStartParameterName;
	}

	public void setPortletSharedSearchResponse(
		PortletSharedSearchResponse portletSharedSearchResponse) {

		_portletSharedSearchResponse = portletSharedSearchResponse;
	}

	public void setTermDisplayContexts(
		List<CPOptionsSearchFacetTermDisplayContext>
			cpOptionsSearchFacetTermDisplayContext) {

		_cpOptionsSearchFacetTermDisplayContext =
			cpOptionsSearchFacetTermDisplayContext;
	}

	private static final long _DISPLAY_STYLE_GROUP_ID = 0;

	private CPOptionLocalService _cpOptionLocalService;
	private List<CPOptionsSearchFacetTermDisplayContext>
		_cpOptionsSearchFacetTermDisplayContext;
	private final CPRequestHelper _cpRequestHelper;
	private long _displayStyleGroupId;
	private List<Facet> _facets;
	private final HttpServletRequest _httpServletRequest;
	private Locale _locale;
	private String _paginationStartParameterName;
	private PortletSharedSearchResponse _portletSharedSearchResponse;
	private final RenderRequest _renderRequest;

}