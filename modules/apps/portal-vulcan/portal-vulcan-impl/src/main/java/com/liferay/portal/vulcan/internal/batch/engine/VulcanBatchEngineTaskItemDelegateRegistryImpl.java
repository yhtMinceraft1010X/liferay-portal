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

package com.liferay.portal.vulcan.internal.batch.engine;

import com.liferay.portal.vulcan.batch.engine.VulcanBatchEngineTaskItemDelegate;
import com.liferay.portal.vulcan.batch.engine.VulcanBatchEngineTaskItemDelegateRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Javier de Arcos
 */
@Component(
	immediate = true, service = VulcanBatchEngineTaskItemDelegateRegistry.class
)
public class VulcanBatchEngineTaskItemDelegateRegistryImpl
	implements VulcanBatchEngineTaskItemDelegateRegistry {

	@Override
	public Set<String> getEntityClassNames() {
		return _vulcanBatchEngineTaskItemDelegateMap.keySet();
	}

	@Override
	public VulcanBatchEngineTaskItemDelegate<?>
		getVulcanBatchEngineTaskItemDelegate(String entityClassName) {

		return _vulcanBatchEngineTaskItemDelegateMap.get(entityClassName);
	}

	@Activate
	protected void activate(BundleContext bundleContext)
		throws InvalidSyntaxException {

		Filter filter = bundleContext.createFilter(
			"(batch.engine.task.item.delegate=true)");

		_serviceTracker = new ServiceTracker<>(
			bundleContext, filter,
			new VulcanBatchEngineTaskItemDelegateServiceTrackerCustomizer(
				bundleContext));

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
	}

	private ServiceTracker<?, ?> _serviceTracker;
	private final Map<String, VulcanBatchEngineTaskItemDelegate<?>>
		_vulcanBatchEngineTaskItemDelegateMap = new HashMap<>();

	private class VulcanBatchEngineTaskItemDelegateServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<VulcanBatchEngineTaskItemDelegate<?>,
			 VulcanBatchEngineTaskItemDelegate<?>> {

		@Override
		public VulcanBatchEngineTaskItemDelegate<?> addingService(
			ServiceReference<VulcanBatchEngineTaskItemDelegate<?>>
				serviceReference) {

			VulcanBatchEngineTaskItemDelegate<?>
				vulcanBatchEngineTaskItemDelegate = _bundleContext.getService(
					serviceReference);

			_vulcanBatchEngineTaskItemDelegateMap.put(
				(String)serviceReference.getProperty(
					"batch.engine.entity.class.name"),
				vulcanBatchEngineTaskItemDelegate);

			return vulcanBatchEngineTaskItemDelegate;
		}

		@Override
		public void modifiedService(
			ServiceReference<VulcanBatchEngineTaskItemDelegate<?>>
				serviceReference,
			VulcanBatchEngineTaskItemDelegate<?>
				vulcanBatchEngineTaskItemDelegate) {
		}

		@Override
		public void removedService(
			ServiceReference<VulcanBatchEngineTaskItemDelegate<?>>
				serviceReference,
			VulcanBatchEngineTaskItemDelegate<?>
				vulcanBatchEngineTaskItemDelegate) {

			_vulcanBatchEngineTaskItemDelegateMap.remove(
				(String)serviceReference.getProperty(
					"batch.engine.entity.class.name"));
		}

		private VulcanBatchEngineTaskItemDelegateServiceTrackerCustomizer(
			BundleContext bundleContext) {

			_bundleContext = bundleContext;
		}

		private final BundleContext _bundleContext;

	}

}