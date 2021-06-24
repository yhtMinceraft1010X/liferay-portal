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

import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.impl.VirtualLayout;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.MimeResponse;
import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(immediate = true, service = PortletURLFactory.class)
public class AlloyMVCSamplePortletURLFactoryImpl implements PortletURLFactory {

	public LiferayPortletURL create(
		HttpServletRequest httpServletRequest, Portlet portlet, Layout layout,
		String lifecycle) {

		return new AlloyMVCSamplePortletURLWrapper(
			httpServletRequest, portlet, layout, lifecycle);
	}

	public LiferayPortletURL create(
		HttpServletRequest httpServletRequest, Portlet portlet, Layout layout,
		String lifecycle, MimeResponse.Copy copy) {

		return new AlloyMVCSamplePortletURLWrapper(
			httpServletRequest, portlet, layout, lifecycle, copy);
	}

	public LiferayPortletURL create(
		HttpServletRequest httpServletRequest, Portlet portlet,
		String lifecycle) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		if (layout == null) {
			layout = _getLayout(
				(Layout)httpServletRequest.getAttribute(WebKeys.LAYOUT),
				themeDisplay.getPlid());
		}

		return new AlloyMVCSamplePortletURLWrapper(
			httpServletRequest, portlet, layout, lifecycle);
	}

	@Override
	public LiferayPortletURL create(
		HttpServletRequest httpServletRequest, String portletId, Layout layout,
		String lifecycle) {

		return new AlloyMVCSamplePortletURLWrapper(
			httpServletRequest, portletId, layout, lifecycle);
	}

	@Override
	public LiferayPortletURL create(
		HttpServletRequest httpServletRequest, String portletId, long plid,
		String lifecycle) {

		return new AlloyMVCSamplePortletURLWrapper(
			httpServletRequest, portletId, plid, lifecycle);
	}

	public LiferayPortletURL create(
		HttpServletRequest httpServletRequest, String portletId,
		String lifecycle) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		if (layout != null) {
			return new AlloyMVCSamplePortletURLWrapper(
				httpServletRequest, portletId, layout, lifecycle);
		}

		return new AlloyMVCSamplePortletURLWrapper(
			httpServletRequest, portletId, themeDisplay.getPlid(), lifecycle);
	}

	public LiferayPortletURL create(
		PortletRequest portletRequest, Portlet portlet, Layout layout,
		String lifecycle) {

		return new AlloyMVCSamplePortletURLWrapper(
			portletRequest, portlet, layout, lifecycle);
	}

	public LiferayPortletURL create(
		PortletRequest portletRequest, Portlet portlet, long plid,
		String lifecycle) {

		return new AlloyMVCSamplePortletURLWrapper(
			portletRequest, portlet,
			_getLayout(
				(Layout)portletRequest.getAttribute(WebKeys.LAYOUT), plid),
			lifecycle);
	}

	public LiferayPortletURL create(
		PortletRequest portletRequest, Portlet portlet, long plid,
		String lifecycle, MimeResponse.Copy copy) {

		return new AlloyMVCSamplePortletURLWrapper(
			portletRequest, portlet,
			_getLayout(
				(Layout)portletRequest.getAttribute(WebKeys.LAYOUT), plid),
			lifecycle, copy);
	}

	@Override
	public LiferayPortletURL create(
		PortletRequest portletRequest, String portletId, Layout layout,
		String lifecycle) {

		return new AlloyMVCSamplePortletURLWrapper(
			portletRequest, portletId, layout, lifecycle);
	}

	@Override
	public LiferayPortletURL create(
		PortletRequest portletRequest, String portletId, long plid,
		String lifecycle) {

		return new AlloyMVCSamplePortletURLWrapper(
			portletRequest, portletId, plid, lifecycle);
	}

	public LiferayPortletURL create(
		PortletRequest portletRequest, String portletId, long plid,
		String lifecycle, MimeResponse.Copy copy) {

		return new AlloyMVCSamplePortletURLWrapper(
			portletRequest, portletId, plid, lifecycle, copy);
	}

	public LiferayPortletURL create(
		PortletRequest portletRequest, String portletId, String lifecycle) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		if (layout != null) {
			return new AlloyMVCSamplePortletURLWrapper(
				portletRequest, portletId, layout, lifecycle);
		}

		return new AlloyMVCSamplePortletURLWrapper(
			portletRequest, portletId, themeDisplay.getPlid(), lifecycle);
	}

	@Activate
	protected void activate() throws Exception {
		_originalPortletURLFactory =
			PortletURLFactoryUtil.getPortletURLFactory();

		PortletURLFactoryUtil portletURLFactoryUtil =
			new PortletURLFactoryUtil();

		portletURLFactoryUtil.setPortletURLFactory(this);
	}

	@Deactivate
	protected void deactivate() {
		PortletURLFactoryUtil portletURLFactoryUtil =
			new PortletURLFactoryUtil();

		portletURLFactoryUtil.setPortletURLFactory(_originalPortletURLFactory);
	}

	private Layout _getLayout(Layout layout, long plid) {
		if ((layout != null) && (layout.getPlid() == plid) &&
			(layout instanceof VirtualLayout)) {

			return layout;
		}

		if (plid > 0) {
			return _layoutLocalService.fetchLayout(plid);
		}

		return null;
	}

	@Reference
	private LayoutLocalService _layoutLocalService;

	private PortletURLFactory _originalPortletURLFactory;

}