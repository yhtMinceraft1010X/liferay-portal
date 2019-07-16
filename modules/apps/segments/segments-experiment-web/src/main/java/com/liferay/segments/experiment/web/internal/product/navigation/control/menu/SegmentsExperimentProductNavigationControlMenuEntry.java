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

package com.liferay.segments.experiment.web.internal.product.navigation.control.menu;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.SessionClicks;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.product.navigation.control.menu.BaseProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.ProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.constants.ProductNavigationControlMenuCategoryKeys;
import com.liferay.segments.constants.SegmentsPortletKeys;
import com.liferay.segments.experiment.web.internal.constants.SegmentsExperimentWebKeys;
import com.liferay.taglib.aui.IconTag;
import com.liferay.taglib.portletext.RuntimeTag;
import com.liferay.taglib.servlet.PageContextFactoryUtil;

import java.io.IOException;
import java.io.Writer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo García
 */
@Component(
	immediate = true,
	property = {
		"product.navigation.control.menu.category.key=" + ProductNavigationControlMenuCategoryKeys.USER,
		"product.navigation.control.menu.entry.order:Integer=500"
	},
	service = ProductNavigationControlMenuEntry.class
)
public class SegmentsExperimentProductNavigationControlMenuEntry
	extends BaseProductNavigationControlMenuEntry {

	@Activate
	public void activate() {
		_portletNamespace = _portal.getPortletNamespace(
			SegmentsPortletKeys.SEGMENTS_EXPERIMENT);
	}

	@Override
	public String getLabel(Locale locale) {
		return null;
	}

	@Override
	public String getURL(HttpServletRequest httpServletRequest) {
		return null;
	}

	@Override
	public boolean includeBody(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		_processBodyBottomContent(
			PageContextFactoryUtil.create(
				httpServletRequest, httpServletResponse));

		return true;
	}

	@Override
	public boolean includeIcon(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		Map<String, String> values = new HashMap<>();

		String segmentsExperimentPanelState = SessionClicks.get(
			httpServletRequest,
			SegmentsExperimentWebKeys.SEGMENTS_EXPERIMENT_PANEL_STATE,
			"closed");

		if (Objects.equals(segmentsExperimentPanelState, "open")) {
			values.put("cssClass", "active");
			values.put("dataURL", StringPool.BLANK);
		}
		else {
			values.put("cssClass", StringPool.BLANK);

			PortletURL portletURL = _portletURLFactory.create(
				httpServletRequest, SegmentsPortletKeys.SEGMENTS_EXPERIMENT,
				RenderRequest.RENDER_PHASE);

			portletURL.setParameter(
				"mvcPath", "/segments_experiment_panel.jsp");

			try {
				portletURL.setWindowState(LiferayWindowState.EXCLUSIVE);
			}
			catch (WindowStateException wse) {
				ReflectionUtil.throwException(wse);
			}

			values.put("dataURL", "data-url='" + portletURL.toString() + "'");
		}

		IconTag iconTag = new IconTag();

		iconTag.setCssClass("icon-monospaced");
		iconTag.setImage("star-half");
		iconTag.setMarkupView("lexicon");

		try {
			values.put(
				"iconTag",
				iconTag.doTagAsString(httpServletRequest, httpServletResponse));
		}
		catch (JspException je) {
			ReflectionUtil.throwException(je);
		}

		values.put("portletNamespace", _portletNamespace);

		Writer writer = httpServletResponse.getWriter();

		writer.write(StringUtil.replace(_ICON_TMPL_CONTENT, "${", "}", values));

		return true;
	}

	@Override
	public boolean isShow(HttpServletRequest httpServletRequest)
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		if (layout.isTypeControlPanel()) {
			return false;
		}

		if (isEmbeddedPersonalApplicationLayout(layout)) {
			return false;
		}

		String layoutMode = ParamUtil.getString(
			httpServletRequest, "p_l_mode", Constants.VIEW);

		if (layoutMode.equals(Constants.EDIT)) {
			return false;
		}

		return super.isShow(httpServletRequest);
	}

	private void _processBodyBottomContent(PageContext pageContext) {
		try {
			HttpServletRequest httpServletRequest =
				(HttpServletRequest)pageContext.getRequest();

			ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
				_portal.getLocale(httpServletRequest), getClass());

			pageContext.setAttribute("resourceBundle", resourceBundle);

			JspWriter jspWriter = pageContext.getOut();

			jspWriter.write("<div class=\"");

			String segmentsExperimentPanelState = SessionClicks.get(
				httpServletRequest,
				SegmentsExperimentWebKeys.SEGMENTS_EXPERIMENT_PANEL_STATE,
				"closed");

			if (Objects.equals(segmentsExperimentPanelState, "open")) {
				jspWriter.write(
					"lfr-has-segments-experiment-panel open-admin-panel ");
			}

			jspWriter.write(
				StringBundler.concat(
					"hidden-print lfr-admin-panel lfr-product-menu-panel ",
					"lfr-segments-experiment-panel sidenav-fixed ",
					"sidenav-menu-slider sidenav-right\" id=\""));

			String portletNamespace = _portal.getPortletNamespace(
				SegmentsPortletKeys.SEGMENTS_EXPERIMENT);

			jspWriter.write(portletNamespace);

			jspWriter.write("segmentsExperimentPanelId\">");
			jspWriter.write(
				"<div class=\"product-menu sidebar sidebar-default " +
					"sidenav-menu\">");

			RuntimeTag runtimeTag = new RuntimeTag();

			runtimeTag.setPortletName(SegmentsPortletKeys.SEGMENTS_EXPERIMENT);

			runtimeTag.doTag(pageContext);

			jspWriter.write("</div></div>");
		}
		catch (Exception e) {
			ReflectionUtil.throwException(e);
		}
	}

	private static final String _ICON_TMPL_CONTENT = StringUtil.read(
		SegmentsExperimentProductNavigationControlMenuEntry.class, "icon.tmpl");

	@Reference
	private Html _html;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	private String _portletNamespace;

	@Reference
	private PortletURLFactory _portletURLFactory;

}