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

package com.liferay.osb.commerce.provisioning.theme.internal.commerce.product.content.renderer.list;

import com.liferay.commerce.order.CommerceOrderHttpHelper;
import com.liferay.commerce.product.catalog.CPCatalogEntry;
import com.liferay.commerce.product.catalog.CPSku;
import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.commerce.product.constants.CPWebKeys;
import com.liferay.commerce.product.content.render.list.CPContentListRenderer;
import com.liferay.commerce.product.content.util.CPContentHelper;
import com.liferay.commerce.product.data.source.CPDataSourceResult;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.osb.commerce.provisioning.theme.internal.constants.OSBCommerceProvisioningThemeWebKeys;
import com.liferay.osb.commerce.provisioning.theme.internal.helper.OSBCommerceThemeHttpHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.portlet.PortletURL;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gianmarco Brunialti Masera
 * @author Ivica Cardic
 */
@Component(
	immediate = true,
	property = {
		"commerce.product.content.list.renderer.key=" + OSBCommerceProvisioningCPContentListRenderer.KEY,
		"commerce.product.content.list.renderer.order=1000",
		"commerce.product.content.list.renderer.portlet.name=" + CPPortletKeys.CP_PUBLISHER_WEB
	},
	service = CPContentListRenderer.class
)
public class OSBCommerceProvisioningCPContentListRenderer
	implements CPContentListRenderer {

	public static final String KEY = "osb-commerce-provisioning-list";

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.get(resourceBundle, "osb-commerce-provisioning");
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		httpServletRequest.setAttribute(
			OSBCommerceProvisioningThemeWebKeys.
				OSB_COMMERCE_PROVISIONING_THEME_CP_ENTRIES_MAP,
			_getCPEntriesMap(httpServletRequest));

		_jspRenderer.renderJSP(
			_servletContext, httpServletRequest, httpServletResponse,
			"/product_publisher/render/view.jsp");
	}

	private Map<String, Object> _getCPEntriesMap(
		HttpServletRequest httpServletRequest) {

		return HashMapBuilder.<String, Object>put(
			"checkoutURL",
			() -> {
				PortletURL checkoutPortletURL =
					_commerceOrderHttpHelper.getCommerceCheckoutPortletURL(
						httpServletRequest);

				checkoutPortletURL.setWindowState(LiferayWindowState.MAXIMIZED);

				return checkoutPortletURL.toString();
			}
		).put(
			"commerceAccountId",
			() -> _commerceThemeHttpHelper.getCurrentCommerceAccountId(
				httpServletRequest)
		).put(
			"cpEntries",
			() -> {
				List<Map<String, Object>> cpEntries = new ArrayList<>();

				CPDataSourceResult cpDataSourceResult =
					(CPDataSourceResult)httpServletRequest.getAttribute(
						CPWebKeys.CP_DATA_SOURCE_RESULT);

				for (CPCatalogEntry cpCatalogEntry :
						cpDataSourceResult.getCPCatalogEntries()) {

					cpEntries.add(
						_getCPEntryMap(cpCatalogEntry, httpServletRequest));
				}

				return cpEntries;
			}
		).build();
	}

	private Map<String, Object> _getCPEntryMap(
			CPCatalogEntry cpCatalogEntry,
			HttpServletRequest httpServletRequest)
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		List<CPSku> cpSkus = cpCatalogEntry.getCPSkus();

		return HashMapBuilder.<String, Object>put(
			"description", cpCatalogEntry.getShortDescription()
		).put(
			"detailURL",
			_cpContentHelper.getFriendlyURL(cpCatalogEntry, themeDisplay)
		).put(
			"name", cpCatalogEntry.getName()
		).put(
			"productId", cpCatalogEntry.getCPDefinitionId()
		).put(
			"productImageURL", cpCatalogEntry.getDefaultImageFileUrl()
		).put(
			Arrays.asList("sku", "skuId"),
			key -> {
				if (cpSkus.size() != 1) {
					return null;
				}

				CPSku cpSku = cpSkus.get(0);

				if (Objects.equals(key, "sku")) {
					return cpSku.getSku();
				}

				return cpSku.getCPInstanceId();
			}
		).put(
			"spritemap",
			themeDisplay.getPathThemeImages() + "/lexicon/icons.svg"
		).build();
	}

	@Reference
	private CommerceOrderHttpHelper _commerceOrderHttpHelper;

	@Reference
	private OSBCommerceThemeHttpHelper _commerceThemeHttpHelper;

	@Reference
	private CPContentHelper _cpContentHelper;

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.osb.commerce.provisioning.theme.impl)"
	)
	private ServletContext _servletContext;

}