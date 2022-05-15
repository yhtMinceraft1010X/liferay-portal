/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.segments.experiment.web.internal.product.navigation.control.menu;

import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.SessionClicks;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.product.navigation.control.menu.BaseProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.ProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.constants.ProductNavigationControlMenuCategoryKeys;
import com.liferay.segments.constants.SegmentsPortletKeys;
import com.liferay.segments.experiment.web.internal.util.SegmentsExperimentUtil;
import com.liferay.segments.manager.SegmentsExperienceManager;
import com.liferay.segments.service.SegmentsExperienceLocalService;
import com.liferay.taglib.aui.IconTag;
import com.liferay.taglib.portletext.RuntimeTag;
import com.liferay.taglib.util.BodyBottomTag;

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
	service = {
		ProductNavigationControlMenuEntry.class,
		SegmentsExperimentProductNavigationControlMenuEntry.class
	}
)
public class SegmentsExperimentProductNavigationControlMenuEntry
	extends BaseProductNavigationControlMenuEntry {

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

		BodyBottomTag bodyBottomTag = new BodyBottomTag();

		bodyBottomTag.setOutputKey("segmentsExperimentPanel");

		try {
			bodyBottomTag.doBodyTag(
				httpServletRequest, httpServletResponse,
				this::_processBodyBottomTagBody);
		}
		catch (JspException jspException) {
			throw new IOException(jspException);
		}

		return true;
	}

	@Override
	public boolean includeIcon(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		Map<String, String> values = new HashMap<>();

		if (isPanelStateOpen(httpServletRequest)) {
			values.put("cssClass", "active");
			values.put("dataURL", StringPool.BLANK);
		}
		else {
			values.put("cssClass", StringPool.BLANK);

			PortletURL portletURL = PortletURLBuilder.create(
				_portletURLFactory.create(
					httpServletRequest, SegmentsPortletKeys.SEGMENTS_EXPERIMENT,
					RenderRequest.RENDER_PHASE)
			).setMVCPath(
				"/segments_experiment_panel.jsp"
			).buildPortletURL();

			try {
				portletURL.setWindowState(LiferayWindowState.EXCLUSIVE);
			}
			catch (WindowStateException windowStateException) {
				ReflectionUtil.throwException(windowStateException);
			}

			SegmentsExperienceManager segmentsExperienceManager =
				new SegmentsExperienceManager(_segmentsExperienceLocalService);

			String dataURL = HttpComponentsUtil.setParameter(
				portletURL.toString(), "segmentsExperienceId",
				segmentsExperienceManager.getSegmentsExperienceId(
					httpServletRequest));

			values.put("dataURL", "data-url='" + dataURL + "'");
		}

		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			_portal.getLocale(httpServletRequest), getClass());

		values.put(
			"title", _html.escape(_language.get(resourceBundle, "ab-test")));

		IconTag iconTag = new IconTag();

		iconTag.setCssClass("icon-monospaced");
		iconTag.setImage("test");
		iconTag.setMarkupView("lexicon");

		try {
			values.put(
				"iconTag",
				iconTag.doTagAsString(httpServletRequest, httpServletResponse));
		}
		catch (JspException jspException) {
			ReflectionUtil.throwException(jspException);
		}

		values.put("portletNamespace", _portletNamespace);

		Writer writer = httpServletResponse.getWriter();

		writer.write(StringUtil.replace(_ICON_TMPL_CONTENT, "${", "}", values));

		return true;
	}

	public boolean isPanelStateOpen(HttpServletRequest httpServletRequest) {
		String segmentsExperimentPanelState = SessionClicks.get(
			httpServletRequest, _SESSION_CLICKS_KEY, "closed");

		if (Objects.equals(segmentsExperimentPanelState, "open")) {
			return true;
		}

		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		String segmentsExperimentKey = ParamUtil.getString(
			originalHttpServletRequest, "segmentsExperimentKey");

		if (Validator.isNotNull(segmentsExperimentKey)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isShow(HttpServletRequest httpServletRequest)
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (!themeDisplay.isSignedIn()) {
			return false;
		}

		Layout layout = themeDisplay.getLayout();

		if (layout.isTypeControlPanel() ||
			isEmbeddedPersonalApplicationLayout(layout) ||
			!layout.isTypeContent() ||
			!LayoutPermissionUtil.contains(
				themeDisplay.getPermissionChecker(), layout,
				ActionKeys.UPDATE)) {

			return false;
		}

		String layoutMode = ParamUtil.getString(
			httpServletRequest, "p_l_mode", Constants.VIEW);

		if (layoutMode.equals(Constants.EDIT)) {
			return false;
		}

		PortalPreferences portalPreferences =
			PortletPreferencesFactoryUtil.getPortalPreferences(
				httpServletRequest);

		boolean hidePanel = GetterUtil.getBoolean(
			portalPreferences.getValue(
				SegmentsPortletKeys.SEGMENTS_EXPERIMENT, "hide-panel"));

		if (!SegmentsExperimentUtil.isAnalyticsConnected(
				themeDisplay.getCompanyId()) &&
			hidePanel) {

			return false;
		}

		return super.isShow(httpServletRequest);
	}

	public void setPanelState(
		HttpServletRequest httpServletRequest, String panelState) {

		SessionClicks.put(httpServletRequest, _SESSION_CLICKS_KEY, panelState);
	}

	@Activate
	protected void activate() {
		_portletNamespace = _portal.getPortletNamespace(
			SegmentsPortletKeys.SEGMENTS_EXPERIMENT);
	}

	private void _processBodyBottomTagBody(PageContext pageContext) {
		try {
			HttpServletRequest httpServletRequest =
				(HttpServletRequest)pageContext.getRequest();

			ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
				_portal.getLocale(httpServletRequest), getClass());

			pageContext.setAttribute("resourceBundle", resourceBundle);

			JspWriter jspWriter = pageContext.getOut();

			jspWriter.write("<div class=\"");

			if (isPanelStateOpen(httpServletRequest)) {
				jspWriter.write(
					"lfr-has-segments-experiment-panel open-admin-panel ");
			}

			jspWriter.write(
				StringBundler.concat(
					"cadmin d-print-none lfr-admin-panel ",
					"lfr-product-menu-panel lfr-segments-experiment-panel ",
					"sidenav-fixed sidenav-menu-slider sidenav-right\" id=\""));
			jspWriter.write(
				_portal.getPortletNamespace(
					SegmentsPortletKeys.SEGMENTS_EXPERIMENT));
			jspWriter.write("segmentsExperimentPanelId\">");
			jspWriter.write(
				"<div class=\"sidebar sidebar-light sidenav-menu " +
					"sidebar-sm\">");

			RuntimeTag runtimeTag = new RuntimeTag();

			runtimeTag.setPortletName(SegmentsPortletKeys.SEGMENTS_EXPERIMENT);

			runtimeTag.doTag(pageContext);

			jspWriter.write("</div></div>");
		}
		catch (Exception exception) {
			ReflectionUtil.throwException(exception);
		}
	}

	private static final String _ICON_TMPL_CONTENT = StringUtil.read(
		SegmentsExperimentProductNavigationControlMenuEntry.class, "icon.tmpl");

	private static final String _SESSION_CLICKS_KEY =
		"com.liferay.segments.experiment.web_panelState";

	@Reference
	private Html _html;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	private String _portletNamespace;

	@Reference
	private PortletURLFactory _portletURLFactory;

	@Reference
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

}