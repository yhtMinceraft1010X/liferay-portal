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

package com.liferay.alloy.mvc.sample.web.internal.hook.portlet;

import com.liferay.alloy.mvc.sample.web.internal.constants.AlloyMVCSamplePortletKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletModeFactory;
import com.liferay.portal.kernel.portlet.WindowStateFactory;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.lang.reflect.Constructor;

import javax.portlet.MimeResponse;
import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 */
public class AlloyMVCSamplePortletURLWrapper
	extends BaseAlloyMVCSamplePortletURLWrapper {

	public static final String PORTLET_NAMESPACE =
		StringPool.UNDERLINE + AlloyMVCSamplePortletKeys.ALLOY_MVC_SAMPLE +
			StringPool.UNDERLINE;

	public AlloyMVCSamplePortletURLWrapper(
		HttpServletRequest httpServletRequest, Portlet portlet, Layout layout,
		String lifecycle) {

		this(
			getLiferayPortletURL(
				new Class<?>[] {
					HttpServletRequest.class, Portlet.class, Layout.class,
					String.class, MimeResponse.Copy.class
				},
				new Object[] {
					httpServletRequest, portlet, layout, lifecycle, null
				}));
	}

	public AlloyMVCSamplePortletURLWrapper(
		HttpServletRequest httpServletRequest, Portlet portlet, Layout layout,
		String lifecycle, MimeResponse.Copy copy) {

		this(
			getLiferayPortletURL(
				new Class<?>[] {
					HttpServletRequest.class, Portlet.class, Layout.class,
					String.class, MimeResponse.Copy.class
				},
				new Object[] {
					httpServletRequest, portlet, layout, lifecycle, copy
				}));
	}

	public AlloyMVCSamplePortletURLWrapper(
		HttpServletRequest httpServletRequest, String portletId, Layout layout,
		String lifecycle) {

		this(
			getLiferayPortletURL(
				new Class<?>[] {
					HttpServletRequest.class, Portlet.class, Layout.class,
					String.class, MimeResponse.Copy.class
				},
				new Object[] {
					httpServletRequest,
					PortletLocalServiceUtil.getPortletById(
						PortalUtil.getCompanyId(httpServletRequest), portletId),
					layout, lifecycle, null
				}));
	}

	public AlloyMVCSamplePortletURLWrapper(
		HttpServletRequest httpServletRequest, String portletId, long plid,
		String lifecycle) {

		this(
			getLiferayPortletURL(
				new Class<?>[] {
					HttpServletRequest.class, Portlet.class, Layout.class,
					String.class, MimeResponse.Copy.class
				},
				new Object[] {
					httpServletRequest,
					PortletLocalServiceUtil.getPortletById(
						PortalUtil.getCompanyId(httpServletRequest), portletId),
					_getLayout(plid), lifecycle, null
				}));
	}

	public AlloyMVCSamplePortletURLWrapper(
		LiferayPortletURL liferayPortletURL) {

		super(liferayPortletURL);
	}

	public AlloyMVCSamplePortletURLWrapper(
		PortletRequest portletRequest, Portlet portlet, Layout layout,
		String lifecycle) {

		this(
			getLiferayPortletURL(
				new Class<?>[] {
					PortletRequest.class, Portlet.class, Layout.class,
					String.class, MimeResponse.Copy.class
				},
				new Object[] {
					portletRequest, portlet, layout, lifecycle, null
				}));
	}

	public AlloyMVCSamplePortletURLWrapper(
		PortletRequest portletRequest, Portlet portlet, Layout layout,
		String lifecycle, MimeResponse.Copy copy) {

		this(
			getLiferayPortletURL(
				new Class<?>[] {
					PortletRequest.class, Portlet.class, Layout.class,
					String.class, MimeResponse.Copy.class
				},
				new Object[] {
					portletRequest, portlet, layout, lifecycle, copy
				}));
	}

	public AlloyMVCSamplePortletURLWrapper(
		PortletRequest portletRequest, String portletId, Layout layout,
		String lifecycle) {

		this(
			getLiferayPortletURL(
				new Class<?>[] {
					PortletRequest.class, Portlet.class, Layout.class,
					String.class, MimeResponse.Copy.class
				},
				new Object[] {
					portletRequest,
					PortletLocalServiceUtil.getPortletById(
						PortalUtil.getCompanyId(portletRequest), portletId),
					layout, lifecycle, null
				}));
	}

	public AlloyMVCSamplePortletURLWrapper(
		PortletRequest portletRequest, String portletId, long plid,
		String lifecycle) {

		this(
			getLiferayPortletURL(
				new Class<?>[] {
					PortletRequest.class, Portlet.class, Layout.class,
					String.class, MimeResponse.Copy.class
				},
				new Object[] {
					portletRequest,
					PortletLocalServiceUtil.getPortletById(
						PortalUtil.getCompanyId(portletRequest), portletId),
					_getLayout(plid), lifecycle, null
				}));
	}

	public AlloyMVCSamplePortletURLWrapper(
		PortletRequest portletRequest, String portletId, long plid,
		String lifecycle, MimeResponse.Copy copy) {

		this(
			getLiferayPortletURL(
				new Class<?>[] {
					PortletRequest.class, Portlet.class, Layout.class,
					String.class, MimeResponse.Copy.class
				},
				new Object[] {
					portletRequest,
					PortletLocalServiceUtil.getPortletById(
						PortalUtil.getCompanyId(portletRequest), portletId),
					_getLayout(plid), lifecycle, copy
				}));
	}

	public void setPortletMode(String portletMode) throws PortletModeException {
		setPortletMode(PortletModeFactory.getPortletMode(portletMode));
	}

	public void setWindowState(String windowState) throws WindowStateException {
		setWindowState(WindowStateFactory.getWindowState(windowState));
	}

	@Override
	public String toString() {
		return StringUtil.removeSubstring(super.toString(), PORTLET_NAMESPACE);
	}

	protected static LiferayPortletURL getLiferayPortletURL(
		Class<?>[] parameterTypes, Object[] parameters) {

		try {
			ClassLoader portalClassLoader =
				PortalClassLoaderUtil.getClassLoader();

			Class<? extends LiferayPortletURL> portletURLImplClass =
				(Class<? extends LiferayPortletURL>)portalClassLoader.loadClass(
					"com.liferay.portlet.internal.PortletURLImpl");

			Constructor<? extends LiferayPortletURL> portletURLImplConstructor =
				portletURLImplClass.getConstructor(parameterTypes);

			return portletURLImplConstructor.newInstance(parameters);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			return null;
		}
	}

	private static Layout _getLayout(long plid) {
		try {
			return LayoutLocalServiceUtil.getLayout(plid);
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AlloyMVCSamplePortletURLWrapper.class);

}