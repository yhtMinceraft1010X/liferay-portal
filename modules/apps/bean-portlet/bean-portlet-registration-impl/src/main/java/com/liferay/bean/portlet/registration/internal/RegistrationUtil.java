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

package com.liferay.bean.portlet.registration.internal;

import com.liferay.bean.portlet.extension.BeanFilterMethodFactory;
import com.liferay.bean.portlet.extension.BeanFilterMethodInvoker;
import com.liferay.bean.portlet.extension.BeanPortletMethodInvoker;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.PortletConstants;
import com.liferay.portal.kernel.resource.bundle.ClassResourceBundleLoader;
import com.liferay.portal.kernel.resource.bundle.ResourceBundleLoader;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.portlet.Portlet;
import javax.portlet.filter.PortletFilter;

import javax.servlet.ServletContext;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Neil Griffin
 */
public class RegistrationUtil {

	public static void registerBeanFilter(
		Set<String> allPortletNames, BeanFilter beanFilter,
		BeanFilterMethodFactory beanFilterMethodFactory,
		BeanFilterMethodInvoker beanFilterMethodInvoker,
		BundleContext bundleContext, String portletName,
		List<ServiceRegistration<?>> serviceRegistrations,
		ServletContext servletContext) {

		if (Objects.equals(portletName, "*")) {
			for (String curPortletName : allPortletNames) {
				serviceRegistrations.add(
					_registerBeanFilter(
						beanFilter, beanFilterMethodFactory,
						beanFilterMethodInvoker, bundleContext,
						_getPortletId(
							curPortletName,
							servletContext.getServletContextName())));
			}
		}
		else {
			if (!allPortletNames.contains(portletName)) {
				_log.error(
					StringBundler.concat(
						"Unable to register filter ",
						beanFilter.getFilterName(), " for nonexistent portlet ",
						portletName));
			}
			else {
				serviceRegistrations.add(
					_registerBeanFilter(
						beanFilter, beanFilterMethodFactory,
						beanFilterMethodInvoker, bundleContext,
						_getPortletId(
							portletName,
							servletContext.getServletContextName())));
			}
		}

		@SuppressWarnings("unchecked")
		List<String> beanFilterNames =
			(List<String>)servletContext.getAttribute(
				WebKeys.BEAN_FILTER_NAMES);

		if (beanFilterNames == null) {
			beanFilterNames = new ArrayList<>();

			servletContext.setAttribute(
				WebKeys.BEAN_FILTER_NAMES, beanFilterNames);
		}

		beanFilterNames.add(beanFilter.getFilterName());
	}

	public static ServiceRegistration<Portlet> registerBeanPortlet(
		BeanApp beanApp, BeanPortlet beanPortlet, List<String> beanPortletIds,
		BeanPortletMethodInvoker beanPortletMethodInvoker,
		BundleContext bundleContext, ServletContext servletContext) {

		try {
			String portletId = _getPortletId(
				beanPortlet.getPortletName(),
				servletContext.getServletContextName());

			if (_log.isDebugEnabled()) {
				_log.debug("Registering bean portlet " + portletId);
			}

			Dictionary<String, Object> dictionary = beanPortlet.toDictionary(
				beanApp);

			dictionary.put("javax.portlet.name", portletId);

			ServiceRegistration<Portlet> portletServiceRegistration =
				bundleContext.registerService(
					Portlet.class,
					new BeanPortletInvokerPortlet(
						beanPortlet.getBeanMethods(), beanPortletMethodInvoker),
					dictionary);

			beanPortletIds.add(portletId);

			return portletServiceRegistration;
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		return null;
	}

	public static ServiceRegistration<ResourceBundleLoader>
		registerResourceBundleLoader(
			BeanPortlet beanPortlet, BundleContext bundleContext,
			ServletContext servletContext) {

		String resourceBundle = beanPortlet.getResourceBundle();

		if (Validator.isNull(resourceBundle)) {
			return null;
		}

		ResourceBundleLoader resourceBundleLoader =
			new ClassResourceBundleLoader(
				resourceBundle, servletContext.getClassLoader());

		return bundleContext.registerService(
			ResourceBundleLoader.class, resourceBundleLoader,
			HashMapDictionaryBuilder.<String, Object>put(
				"resource.bundle.base.name", resourceBundle
			).put(
				"service.ranking", Integer.MIN_VALUE
			).put(
				"servlet.context.name", servletContext.getServletContextName()
			).build());
	}

	private static String _getPortletId(
		String portletName, String servletContextName) {

		if (Validator.isNotNull(servletContextName)) {
			portletName = StringBundler.concat(
				portletName, PortletConstants.WAR_SEPARATOR,
				servletContextName);
		}

		return PortalUtil.getJsSafePortletId(portletName);
	}

	private static ServiceRegistration<PortletFilter> _registerBeanFilter(
		BeanFilter beanFilter, BeanFilterMethodFactory beanFilterMethodFactory,
		BeanFilterMethodInvoker beanFilterMethodInvoker,
		BundleContext bundleContext, String portletId) {

		if (_log.isDebugEnabled()) {
			_log.debug(
				StringBundler.concat(
					"Registering bean filter ", beanFilter.getFilterName(),
					" for portlet ", portletId));
		}

		Dictionary<String, Object> dictionary = beanFilter.toDictionary();

		dictionary.put("javax.portlet.name", portletId);

		return bundleContext.registerService(
			PortletFilter.class,
			new BeanFilterInvokerPortletFilter(
				beanFilterMethodFactory, beanFilterMethodInvoker,
				beanFilter.getFilterClass()),
			dictionary);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RegistrationUtil.class);

}