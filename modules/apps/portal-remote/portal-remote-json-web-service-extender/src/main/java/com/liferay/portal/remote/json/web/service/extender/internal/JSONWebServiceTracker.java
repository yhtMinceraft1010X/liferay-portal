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

package com.liferay.portal.remote.json.web.service.extender.internal;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.jsonwebservice.JSONWebServiceActionsManager;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Miguel Pastor
 */
@Component(immediate = true, service = JSONWebServiceTracker.class)
public class JSONWebServiceTracker
	implements ServiceTrackerCustomizer<Object, Object> {

	@Override
	public Object addingService(ServiceReference<Object> serviceReference) {
		return _registerService(serviceReference);
	}

	@Override
	public void modifiedService(
		ServiceReference<Object> serviceReference, Object service) {

		_unregisterService(service);

		_registerService(serviceReference);
	}

	@Override
	public void removedService(
		ServiceReference<Object> serviceReference, Object service) {

		_unregisterService(service);
	}

	@Activate
	protected void activate(ComponentContext componentContext) {
		_componentContext = componentContext;

		_serviceTracker = ServiceTrackerFactory.open(
			componentContext.getBundleContext(),
			StringBundler.concat(
				"(&(json.web.service.context.name=*)(json.web.service.context.",
				"path=*)(!(objectClass=", AopService.class.getName(), ")))"),
			this);
	}

	@Deactivate
	protected void deactivate() {
		_componentContext = null;

		_serviceTracker.close();

		_serviceTracker = null;
	}

	@Reference
	protected void setJSONWebServiceActionsManager(
		JSONWebServiceActionsManager jsonWebServiceActionsManager) {

		_jsonWebServiceActionsManager = jsonWebServiceActionsManager;
	}

	protected void unsetJSONWebServiceActionsManager(
		JSONWebServiceActionsManager jsonWebServiceActionsManager) {

		_jsonWebServiceActionsManager = null;
	}

	private ClassLoader _getBundleClassLoader(Bundle bundle) {
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		return bundleWiring.getClassLoader();
	}

	private Object _getService(ServiceReference<Object> serviceReference) {
		BundleContext bundleContext = _componentContext.getBundleContext();

		return bundleContext.getService(serviceReference);
	}

	private Object _registerService(ServiceReference<Object> serviceReference) {
		String contextName = (String)serviceReference.getProperty(
			"json.web.service.context.name");
		String contextPath = (String)serviceReference.getProperty(
			"json.web.service.context.path");
		Object service = _getService(serviceReference);

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		ClassLoader classLoader = _getBundleClassLoader(
			serviceReference.getBundle());

		currentThread.setContextClassLoader(classLoader);

		try {
			_jsonWebServiceActionsManager.registerService(
				contextName, contextPath, service);
		}
		finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}

		return service;
	}

	private void _unregisterService(Object service) {
		_jsonWebServiceActionsManager.unregisterJSONWebServiceActions(service);
	}

	private ComponentContext _componentContext;
	private JSONWebServiceActionsManager _jsonWebServiceActionsManager;
	private ServiceTracker<Object, Object> _serviceTracker;

}