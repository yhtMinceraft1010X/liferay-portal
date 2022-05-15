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

package com.liferay.marketplace.app.manager.web.internal.display.context;

import com.liferay.marketplace.app.manager.web.internal.util.AppDisplay;
import com.liferay.marketplace.app.manager.web.internal.util.AppDisplayFactoryUtil;
import com.liferay.marketplace.app.manager.web.internal.util.BundleManagerUtil;
import com.liferay.marketplace.app.manager.web.internal.util.comparator.ModuleServiceReferenceComparator;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * @author Pei-Jung Lan
 */
public class ViewModuleManagementToolbarDisplayContext
	extends BaseAppManagerManagementToolbarDisplayContext {

	public ViewModuleManagementToolbarDisplayContext(
		HttpServletRequest httpServletRequest,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		super(
			httpServletRequest, liferayPortletRequest, liferayPortletResponse);
	}

	public String getApp() {
		return ParamUtil.getString(httpServletRequest, "app");
	}

	public AppDisplay getAppDisplay() {
		String app = ParamUtil.getString(httpServletRequest, "app");

		AppDisplay appDisplay = null;

		if (Validator.isNumber(app)) {
			appDisplay = AppDisplayFactoryUtil.getAppDisplay(
				BundleManagerUtil.getBundles(), GetterUtil.getLong(app));
		}

		if (appDisplay == null) {
			appDisplay = AppDisplayFactoryUtil.getAppDisplay(
				BundleManagerUtil.getBundles(), app,
				httpServletRequest.getLocale());
		}

		return appDisplay;
	}

	public Bundle getBundle() {
		return BundleManagerUtil.getBundle(
			ParamUtil.getString(httpServletRequest, "symbolicName"),
			ParamUtil.getString(httpServletRequest, "version"));
	}

	public String getPluginType() {
		return ParamUtil.getString(
			httpServletRequest, "pluginType", "components");
	}

	@Override
	public PortletURL getPortletURL() {
		Bundle bundle = getBundle();

		PortletURL portletURL = PortletURLBuilder.createRenderURL(
			liferayPortletResponse
		).setMVCPath(
			"/view_module.jsp"
		).setParameter(
			"app", getApp()
		).setParameter(
			"orderByType", getOrderByType()
		).setParameter(
			"pluginType", getPluginType()
		).setParameter(
			"symbolicName", bundle.getSymbolicName()
		).setParameter(
			"version", bundle.getVersion()
		).buildPortletURL();

		if (_searchContainer != null) {
			portletURL.setParameter(
				_searchContainer.getCurParam(),
				String.valueOf(_searchContainer.getCur()));
			portletURL.setParameter(
				_searchContainer.getDeltaParam(),
				String.valueOf(_searchContainer.getDelta()));
		}

		return portletURL;
	}

	@Override
	public SearchContainer<Object> getSearchContainer() throws Exception {
		if (_searchContainer != null) {
			return _searchContainer;
		}

		String emptyResultsMessage = "no-portlets-were-found";

		String pluginType = getPluginType();

		if (pluginType.equals("components")) {
			emptyResultsMessage = "no-components-were-found";
		}

		SearchContainer<Object> searchContainer = new SearchContainer(
			liferayPortletRequest, getPortletURL(), null, emptyResultsMessage);

		searchContainer.setOrderByCol(getOrderByCol());
		searchContainer.setOrderByType(getOrderByType());

		Bundle bundle = getBundle();

		BundleContext bundleContext = bundle.getBundleContext();

		List<ServiceReference<?>> serviceReferences =
			Collections.<ServiceReference<?>>emptyList();

		if (pluginType.equals("portlets")) {
			serviceReferences = ListUtil.sort(
				new ArrayList<>(
					bundleContext.getServiceReferences(
						Portlet.class,
						"(service.bundleid=" + bundle.getBundleId() + ")")),
				new ModuleServiceReferenceComparator(
					"javax.portlet.display-name", getOrderByType()));
		}
		else {
			serviceReferences = ListUtil.sort(
				ListUtil.fromArray(
					(ServiceReference<?>[])bundleContext.getServiceReferences(
						(String)null,
						"(&(component.id=*)(service.bundleid=" +
							bundle.getBundleId() + "))")),
				new ModuleServiceReferenceComparator(
					"component.name", getOrderByType()));
		}

		searchContainer.setResultsAndTotal(new ArrayList<>(serviceReferences));

		_searchContainer = searchContainer;

		return _searchContainer;
	}

	private SearchContainer<Object> _searchContainer;

}