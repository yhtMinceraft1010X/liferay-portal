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

package com.liferay.fragment.entry.processor.portlet;

import com.liferay.fragment.constants.FragmentWebKeys;
import com.liferay.fragment.exception.FragmentEntryContentException;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.processor.FragmentEntryProcessor;
import com.liferay.fragment.processor.FragmentEntryProcessorContext;
import com.liferay.fragment.processor.PortletRegistry;
import com.liferay.fragment.renderer.FragmentPortletRenderer;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.service.LayoutClassedModelUsageLocalService;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ModelHintsConstants;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.impl.DefaultLayoutTypeAccessPolicyImpl;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactory;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.PortletPreferenceValueLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.PortletPreferencesImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.PortletPreferences;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	immediate = true, property = "fragment.entry.processor.priority:Integer=3",
	service = FragmentEntryProcessor.class
)
public class PortletFragmentEntryProcessor implements FragmentEntryProcessor {

	@Override
	public void deleteFragmentEntryLinkData(
		FragmentEntryLink fragmentEntryLink) {

		for (String portletId :
				_portletRegistry.getFragmentEntryLinkPortletIds(
					fragmentEntryLink)) {

			try {
				_portletLocalService.deletePortlet(
					fragmentEntryLink.getCompanyId(), portletId,
					fragmentEntryLink.getPlid());

				_layoutClassedModelUsageLocalService.
					deleteLayoutClassedModelUsages(
						portletId, _portal.getClassNameId(Portlet.class),
						fragmentEntryLink.getPlid());
			}
			catch (Exception exception) {
				if (_log.isDebugEnabled()) {
					_log.debug(exception);
				}
			}
		}
	}

	@Override
	public JSONArray getAvailableTagsJSONArray() {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (String alias : _portletRegistry.getPortletAliases()) {
			jsonArray.put(
				JSONUtil.put(
					"content",
					StringBundler.concat(
						"<lfr-widget-", alias, "></lfr-widget-", alias, ">")
				).put(
					"name", "lfr-widget-" + alias
				));
		}

		return jsonArray;
	}

	@Override
	public String processFragmentEntryLinkHTML(
			FragmentEntryLink fragmentEntryLink, String html,
			FragmentEntryProcessorContext fragmentEntryProcessorContext)
		throws PortalException {

		Document document = _getDocument(html);

		_validateFragmentEntryHTMLDocument(document);

		HttpServletRequest httpServletRequest =
			fragmentEntryProcessorContext.getHttpServletRequest();

		if (httpServletRequest != null) {
			httpServletRequest.setAttribute(
				FragmentWebKeys.FRAGMENT_ENTRY_LINK, fragmentEntryLink);
		}

		String editableValues = fragmentEntryLink.getEditableValues();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			editableValues);

		if (Validator.isNotNull(jsonObject.getString("portletId"))) {
			return _renderWidgetHTML(
				editableValues, fragmentEntryProcessorContext);
		}

		Set<String> processedPortletIds = new HashSet<>();

		for (Element element : document.select("*")) {
			String tagName = element.tagName();

			String portletName = _getPortletName(tagName);

			if (Validator.isNull(portletName)) {
				continue;
			}

			Portlet portlet = _portletLocalService.getPortletById(portletName);

			String instanceId = String.valueOf(CharPool.NUMBER_0);

			String id = element.attr("id");

			if (portlet.isInstanceable()) {
				instanceId = _getInstanceId(
					fragmentEntryLink.getNamespace(), id);
			}
			else if (processedPortletIds.contains(portletName) ||
					 _checkNoninstanceablePortletUsed(
						 fragmentEntryLink, portletName)) {

				throw new FragmentEntryContentException(
					LanguageUtil.get(
						_resourceBundle,
						"noninstanceable-widgets-can-be-embedded-only-once-" +
							"on-the-same-page"));
			}

			long plid = ParamUtil.getLong(
				fragmentEntryProcessorContext.getHttpServletRequest(),
				"p_l_id");

			String portletHTML = _fragmentPortletRenderer.renderPortlet(
				fragmentEntryProcessorContext.getHttpServletRequest(),
				fragmentEntryProcessorContext.getHttpServletResponse(),
				portletName, instanceId,
				_getPreferences(
					plid, portletName, fragmentEntryLink, id,
					portlet.getDefaultPreferences()));

			Element portletElement = new Element("div");

			portletElement.attr("class", "portlet");

			portletElement.html(portletHTML);

			element.replaceWith(portletElement);

			processedPortletIds.add(portletName);
		}

		Element bodyElement = document.body();

		return bodyElement.html();
	}

	@Override
	public void validateFragmentEntryHTML(String html, String configuration)
		throws PortalException {

		_validateFragmentEntryHTMLDocument(_getDocument(html));
	}

	private boolean _checkNoninstanceablePortletUsed(
			FragmentEntryLink currentFragmentEntryLink,
			String currentPortletName)
		throws PortalException {

		if ((currentFragmentEntryLink.getFragmentEntryLinkId() <= 0) ||
			(currentFragmentEntryLink.getPlid() <= 0)) {

			return false;
		}

		List<FragmentEntryLink> fragmentEntryLinks =
			_fragmentEntryLinkLocalService.
				getFragmentEntryLinksBySegmentsExperienceId(
					currentFragmentEntryLink.getGroupId(),
					currentFragmentEntryLink.getSegmentsExperienceId(),
					currentFragmentEntryLink.getPlid());

		LayoutPageTemplateStructure layoutPageTemplateStructure =
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(
					currentFragmentEntryLink.getGroupId(),
					currentFragmentEntryLink.getPlid(), true);

		LayoutStructure layoutStructure = LayoutStructure.of(
			layoutPageTemplateStructure.getData(
				currentFragmentEntryLink.getSegmentsExperienceId()));

		for (FragmentEntryLink fragmentEntryLink : fragmentEntryLinks) {
			if (currentFragmentEntryLink.getFragmentEntryLinkId() ==
					fragmentEntryLink.getFragmentEntryLinkId()) {

				continue;
			}

			List<String> portletIds =
				_portletRegistry.getFragmentEntryLinkPortletIds(
					fragmentEntryLink);

			Stream<String> stream = portletIds.stream();

			List<String> portletNames = stream.map(
				portletId -> PortletIdCodec.decodePortletName(portletId)
			).distinct(
			).collect(
				Collectors.toList()
			);

			if (!portletNames.contains(currentPortletName)) {
				continue;
			}

			LayoutStructureItem layoutStructureItem =
				layoutStructure.getLayoutStructureItemByFragmentEntryLinkId(
					fragmentEntryLink.getFragmentEntryLinkId());

			if (!layoutStructure.isItemMarkedForDeletion(
					layoutStructureItem.getItemId())) {

				return true;
			}
		}

		return false;
	}

	private boolean _comparePreferences(
		PortletPreferences currentPortletPreferences,
		PortletPreferences sourcePortletPreferences) {

		Map<String, String[]> currentPreferences =
			currentPortletPreferences.getMap();

		Map<String, String[]> sourcePreferences =
			sourcePortletPreferences.getMap();

		if (currentPreferences.size() != sourcePreferences.size()) {
			return false;
		}

		for (Map.Entry<String, String[]> entry :
				currentPreferences.entrySet()) {

			if (!Arrays.equals(
					sourcePreferences.get(entry.getKey()), entry.getValue())) {

				return false;
			}
		}

		return true;
	}

	private Document _getDocument(String html) {
		Document document = Jsoup.parseBodyFragment(html);

		Document.OutputSettings outputSettings = new Document.OutputSettings();

		outputSettings.prettyPrint(false);

		document.outputSettings(outputSettings);

		return document;
	}

	private String _getInstanceId(String namespace, String id) {
		if (Validator.isNull(namespace)) {
			namespace = StringUtil.randomId();
		}

		return namespace + id;
	}

	private String _getPortletId(
		String portletName, String namespace, String id) {

		return PortletIdCodec.encode(
			PortletIdCodec.decodePortletName(portletName),
			PortletIdCodec.decodeUserId(portletName),
			_getInstanceId(namespace, id));
	}

	private String _getPortletName(String tagName) {
		if (!StringUtil.startsWith(tagName, "lfr-widget-")) {
			return StringPool.BLANK;
		}

		String alias = tagName.substring(11);

		return _portletRegistry.getPortletName(alias);
	}

	private String _getPreferences(
			long plid, String portletName, FragmentEntryLink fragmentEntryLink,
			String id, String defaultPreferences)
		throws PortalException {

		String defaultPortletId = _getPortletId(
			portletName, fragmentEntryLink.getNamespace(), id);

		PortletPreferences jxPortletPreferences =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				fragmentEntryLink.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
				fragmentEntryLink.getPlid(), defaultPortletId,
				defaultPreferences);

		String portletId = _getPortletId(
			portletName, fragmentEntryLink.getNamespace(), id);

		List<com.liferay.portal.kernel.model.PortletPreferences>
			portletPreferencesList =
				_portletPreferencesLocalService.getPortletPreferences(
					fragmentEntryLink.getCompanyId(),
					PortletKeys.PREFS_OWNER_ID_DEFAULT,
					PortletKeys.PREFS_OWNER_TYPE_LAYOUT, portletId);

		if (ListUtil.isNotEmpty(portletPreferencesList)) {
			jxPortletPreferences =
				PortletPreferencesFactoryUtil.getLayoutPortletSetup(
					fragmentEntryLink.getCompanyId(),
					PortletKeys.PREFS_OWNER_ID_DEFAULT,
					PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
					fragmentEntryLink.getPlid(), portletId,
					PortletPreferencesFactoryUtil.toXML(jxPortletPreferences));

			_updateLayoutPortletSetup(
				plid, portletPreferencesList, jxPortletPreferences);
		}

		Document preferencesDocument = _getDocument(
			PortletPreferencesFactoryUtil.toXML(jxPortletPreferences));

		Element preferencesBody = preferencesDocument.body();

		return preferencesBody.html();
	}

	private String _renderWidgetHTML(
			String editableValues,
			FragmentEntryProcessorContext fragmentEntryProcessorContext)
		throws PortalException {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			editableValues);

		String portletId = jsonObject.getString("portletId");

		if (Validator.isNull(portletId)) {
			return StringPool.BLANK;
		}

		String instanceId = jsonObject.getString("instanceId");

		String encodePortletId = PortletIdCodec.encode(portletId, instanceId);

		PortletPreferences portletPreferences =
			PortletPreferencesFactoryUtil.getPortletPreferences(
				fragmentEntryProcessorContext.getHttpServletRequest(),
				encodePortletId);

		String html = _fragmentPortletRenderer.renderPortlet(
			fragmentEntryProcessorContext.getHttpServletRequest(),
			fragmentEntryProcessorContext.getHttpServletResponse(), portletId,
			instanceId,
			PortletPreferencesFactoryUtil.toXML(portletPreferences));

		HttpServletRequest httpServletRequest =
			fragmentEntryProcessorContext.getHttpServletRequest();

		FragmentEntryLink fragmentEntryLink =
			(FragmentEntryLink)httpServletRequest.getAttribute(
				FragmentWebKeys.FRAGMENT_ENTRY_LINK);

		if (fragmentEntryLink != null) {
			String checkAccessAllowedToPortletCacheKey = StringBundler.concat(
				DefaultLayoutTypeAccessPolicyImpl.class.getName(), "#",
				ParamUtil.getLong(
					fragmentEntryProcessorContext.getHttpServletRequest(),
					"p_l_id"),
				"#", encodePortletId);

			httpServletRequest.setAttribute(
				FragmentWebKeys.ACCESS_ALLOWED_TO_FRAGMENT_ENTRY_LINK_ID +
					fragmentEntryLink.getFragmentEntryLinkId(),
				GetterUtil.getBoolean(
					httpServletRequest.getAttribute(
						checkAccessAllowedToPortletCacheKey),
					true));
		}

		return html;
	}

	private void _updateLayoutPortletSetup(
		long layoutPlid,
		List<com.liferay.portal.kernel.model.PortletPreferences>
			portletPreferencesList,
		PortletPreferences jxPortletPreferences) {

		long plid = 0;

		if (jxPortletPreferences instanceof PortletPreferencesImpl) {
			plid = layoutPlid;
		}

		for (com.liferay.portal.kernel.model.PortletPreferences
				portletPreferencesImpl : portletPreferencesList) {

			PortletPreferences currentPortletPreferences =
				_portletPreferenceValueLocalService.getPreferences(
					portletPreferencesImpl);

			if ((plid != portletPreferencesImpl.getPlid()) ||
				_comparePreferences(
					currentPortletPreferences, jxPortletPreferences)) {

				continue;
			}

			_portletPreferencesLocalService.updatePreferences(
				portletPreferencesImpl.getOwnerId(),
				portletPreferencesImpl.getOwnerType(),
				portletPreferencesImpl.getPlid(),
				portletPreferencesImpl.getPortletId(), jxPortletPreferences);
		}
	}

	private void _validateFragmentEntryHTMLDocument(Document document)
		throws PortalException {

		for (Element element : document.select("*")) {
			String htmlTagName = element.tagName();

			if (!StringUtil.startsWith(htmlTagName, "lfr-widget-")) {
				continue;
			}

			String alias = StringUtil.removeSubstring(
				htmlTagName, "lfr-widget-");

			if (Validator.isNull(_portletRegistry.getPortletName(alias))) {
				throw new FragmentEntryContentException(
					LanguageUtil.format(
						_resourceBundle,
						"there-is-no-widget-available-for-alias-x", alias));
			}

			String id = element.id();

			if (Validator.isNotNull(id) && !Validator.isAlphanumericName(id)) {
				throw new FragmentEntryContentException(
					LanguageUtil.format(
						_resourceBundle,
						"widget-id-must-contain-only-alphanumeric-characters",
						alias));
			}

			if (Validator.isNotNull(id)) {
				Elements elements = document.select("#" + id);

				if (elements.size() > 1) {
					throw new FragmentEntryContentException(
						LanguageUtil.get(
							_resourceBundle, "widget-id-must-be-unique"));
				}

				if (id.length() > GetterUtil.getInteger(
						ModelHintsConstants.TEXT_MAX_LENGTH)) {

					throw new FragmentEntryContentException(
						LanguageUtil.format(
							_resourceBundle,
							"widget-id-cannot-exceed-x-characters",
							ModelHintsConstants.TEXT_MAX_LENGTH));
				}
			}

			Elements elements = document.select(htmlTagName);

			if ((elements.size() > 1) && Validator.isNull(id)) {
				throw new FragmentEntryContentException(
					LanguageUtil.get(
						_resourceBundle,
						"duplicate-widgets-within-the-same-fragment-must-" +
							"have-an-id"));
			}

			if (elements.size() > 1) {
				Portlet portlet = _portletLocalService.getPortletById(
					_portletRegistry.getPortletName(alias));

				if (!portlet.isInstanceable()) {
					throw new FragmentEntryContentException(
						LanguageUtil.format(
							_resourceBundle,
							"you-cannot-add-the-widget-x-more-than-once",
							alias));
				}
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortletFragmentEntryProcessor.class);

	@Reference
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@Reference
	private FragmentPortletRenderer _fragmentPortletRenderer;

	@Reference
	private LayoutClassedModelUsageLocalService
		_layoutClassedModelUsageLocalService;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

	@Reference
	private PortletPreferencesFactory _portletPreferencesFactory;

	@Reference
	private PortletPreferencesLocalService _portletPreferencesLocalService;

	@Reference
	private PortletPreferenceValueLocalService
		_portletPreferenceValueLocalService;

	@Reference
	private PortletRegistry _portletRegistry;

	private final ResourceBundle _resourceBundle = ResourceBundleUtil.getBundle(
		"content.Language", getClass());

}