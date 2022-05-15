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

package com.liferay.layout.page.template.admin.web.internal.headless.delivery.dto.v1_0.structure.importer;

import com.liferay.document.library.util.DLURLHelperUtil;
import com.liferay.fragment.constants.FragmentEntryLinkConstants;
import com.liferay.fragment.contributor.FragmentCollectionContributorTracker;
import com.liferay.fragment.entry.processor.util.EditableFragmentEntryProcessorUtil;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.processor.DefaultFragmentEntryProcessorContext;
import com.liferay.fragment.processor.FragmentEntryProcessorContext;
import com.liferay.fragment.processor.FragmentEntryProcessorRegistry;
import com.liferay.fragment.renderer.FragmentRenderer;
import com.liferay.fragment.renderer.FragmentRendererTracker;
import com.liferay.fragment.service.FragmentCollectionService;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.fragment.service.FragmentEntryLocalService;
import com.liferay.fragment.validator.FragmentEntryValidator;
import com.liferay.headless.delivery.dto.v1_0.FragmentLink;
import com.liferay.headless.delivery.dto.v1_0.PageElement;
import com.liferay.layout.page.template.admin.web.internal.headless.delivery.dto.v1_0.structure.importer.helper.PortletConfigurationImporterHelper;
import com.liferay.layout.page.template.admin.web.internal.headless.delivery.dto.v1_0.structure.importer.helper.PortletPermissionsImporterHelper;
import com.liferay.layout.util.structure.FragmentStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepository;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.segments.service.SegmentsExperienceLocalService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(service = LayoutStructureItemImporter.class)
public class FragmentLayoutStructureItemImporter
	extends BaseLayoutStructureItemImporter
	implements LayoutStructureItemImporter {

	@Override
	public LayoutStructureItem addLayoutStructureItem(
			LayoutStructure layoutStructure,
			LayoutStructureItemImporterContext
				layoutStructureItemImporterContext,
			PageElement pageElement, Set<String> warningMessages)
		throws Exception {

		FragmentEntryLink fragmentEntryLink = _addFragmentEntryLink(
			layoutStructureItemImporterContext.getLayout(),
			layoutStructureItemImporterContext, pageElement,
			layoutStructureItemImporterContext.getPosition(), warningMessages);

		if (fragmentEntryLink == null) {
			return null;
		}

		FragmentStyledLayoutStructureItem fragmentStyledLayoutStructureItem =
			(FragmentStyledLayoutStructureItem)
				layoutStructure.addFragmentStyledLayoutStructureItem(
					fragmentEntryLink.getFragmentEntryLinkId(),
					layoutStructureItemImporterContext.getParentItemId(),
					layoutStructureItemImporterContext.getPosition());

		Map<String, Object> definitionMap = getDefinitionMap(
			pageElement.getDefinition());

		if (definitionMap == null) {
			return fragmentStyledLayoutStructureItem;
		}

		Map<String, Object> fragmentStyleMap =
			(Map<String, Object>)definitionMap.get("fragmentStyle");

		int oldVersionCompareValue = Double.compare(
			layoutStructureItemImporterContext.getPageDefinitionVersion(), 1.1);

		if (oldVersionCompareValue < 0) {
			Map<String, Object> fragmentConfigMap =
				(Map<String, Object>)definitionMap.get("fragmentConfig");

			if (MapUtil.isNotEmpty(fragmentConfigMap) ||
				MapUtil.isNotEmpty(fragmentStyleMap)) {

				JSONObject commonStylesJSONObject = toStylesJSONObject(
					layoutStructureItemImporterContext, fragmentStyleMap);
				JSONObject configStylesJSONObject = toStylesJSONObject(
					layoutStructureItemImporterContext, fragmentConfigMap);

				for (String key : commonStylesJSONObject.keySet()) {
					if (Validator.isNull(
							configStylesJSONObject.getString(key))) {

						configStylesJSONObject.put(
							key, commonStylesJSONObject.get(key));
					}
				}

				JSONObject jsonObject = JSONUtil.put(
					"styles",
					JSONUtil.merge(
						commonStylesJSONObject, configStylesJSONObject));

				fragmentStyledLayoutStructureItem.updateItemConfig(jsonObject);
			}
		}
		else if (fragmentStyleMap != null) {
			JSONObject jsonObject = JSONUtil.put(
				"styles",
				toStylesJSONObject(
					layoutStructureItemImporterContext, fragmentStyleMap));

			fragmentStyledLayoutStructureItem.updateItemConfig(jsonObject);
		}

		if (definitionMap.containsKey("fragmentViewports")) {
			List<Map<String, Object>> fragmentViewports =
				(List<Map<String, Object>>)definitionMap.get(
					"fragmentViewports");

			for (Map<String, Object> fragmentViewport : fragmentViewports) {
				JSONObject jsonObject = JSONUtil.put(
					(String)fragmentViewport.get("id"),
					toFragmentViewportStylesJSONObject(fragmentViewport));

				fragmentStyledLayoutStructureItem.updateItemConfig(jsonObject);
			}
		}

		if (definitionMap.containsKey("indexed")) {
			fragmentStyledLayoutStructureItem.setIndexed(
				GetterUtil.getBoolean(definitionMap.get("indexed")));
		}

		return fragmentStyledLayoutStructureItem;
	}

	@Override
	public PageElement.Type getPageElementType() {
		return PageElement.Type.FRAGMENT;
	}

	private FragmentEntryLink _addFragmentEntryLink(
			Layout layout,
			LayoutStructureItemImporterContext
				layoutStructureItemImporterContext,
			PageElement pageElement, int position, Set<String> warningMessages)
		throws Exception {

		Map<String, Object> definitionMap = getDefinitionMap(
			pageElement.getDefinition());

		if (definitionMap == null) {
			return null;
		}

		Map<String, Object> fragmentDefinitionMap =
			(Map<String, Object>)definitionMap.get("fragment");

		String fragmentKey = (String)fragmentDefinitionMap.get("key");

		if (Validator.isNull(fragmentKey)) {
			return null;
		}

		FragmentEntry fragmentEntry = _getFragmentEntry(
			layout.getCompanyId(), layout.getGroupId(), fragmentKey);

		FragmentRenderer fragmentRenderer =
			_fragmentRendererTracker.getFragmentRenderer(fragmentKey);

		if ((fragmentEntry == null) && (fragmentRenderer == null)) {
			warningMessages.add(
				_getWarningMessage(layout.getGroupId(), fragmentKey));

			return null;
		}

		long fragmentEntryId = 0;
		String html = StringPool.BLANK;
		String js = StringPool.BLANK;
		String css = StringPool.BLANK;
		String configuration = StringPool.BLANK;

		JSONObject defaultEditableValuesJSONObject =
			JSONFactoryUtil.createJSONObject();

		if (fragmentEntry != null) {
			fragmentEntryId = fragmentEntry.getFragmentEntryId();
			html = fragmentEntry.getHtml();
			js = fragmentEntry.getJs();
			css = fragmentEntry.getCss();
			configuration = fragmentEntry.getConfiguration();

			FragmentCollection fragmentCollection =
				_fragmentCollectionService.fetchFragmentCollection(
					fragmentEntry.getFragmentCollectionId());

			defaultEditableValuesJSONObject =
				_fragmentEntryProcessorRegistry.
					getDefaultEditableValuesJSONObject(
						_getProcessedHTML(
							fragmentEntry.getCompanyId(), configuration,
							fragmentCollection, html),
						configuration);
		}

		Map<String, String> editableTypes =
			EditableFragmentEntryProcessorUtil.getEditableTypes(html);

		JSONObject fragmentEntryProcessorValuesJSONObject = JSONUtil.put(
			"com.liferay.fragment.entry.processor.background.image." +
				"BackgroundImageFragmentEntryProcessor",
			_toBackgroundImageFragmentEntryProcessorJSONObject(
				layoutStructureItemImporterContext,
				(List<Object>)definitionMap.get("fragmentFields"))
		).put(
			"com.liferay.fragment.entry.processor.editable." +
				"EditableFragmentEntryProcessor",
			() -> {
				JSONObject editableFragmentEntryProcessorJSONObject =
					_toEditableFragmentEntryProcessorJSONObject(
						editableTypes,
						(List<Object>)definitionMap.get("fragmentFields"),
						layoutStructureItemImporterContext);

				if (editableFragmentEntryProcessorJSONObject.length() > 0) {
					return editableFragmentEntryProcessorJSONObject;
				}

				return null;
			}
		);

		Map<String, String> configurationTypes = _getConfigurationTypes(
			configuration);

		JSONObject freeMarkerFragmentEntryProcessorJSONObject =
			_toFreeMarkerFragmentEntryProcessorJSONObject(
				configurationTypes,
				(Map<String, Object>)definitionMap.get("fragmentConfig"));

		_fragmentEntryValidator.validateConfigurationValues(
			configuration, fragmentEntryProcessorValuesJSONObject);

		if (freeMarkerFragmentEntryProcessorJSONObject.length() > 0) {
			fragmentEntryProcessorValuesJSONObject.put(
				"com.liferay.fragment.entry.processor.freemarker." +
					"FreeMarkerFragmentEntryProcessor",
				freeMarkerFragmentEntryProcessorJSONObject);
		}

		JSONObject jsonObject = _deepMerge(
			defaultEditableValuesJSONObject,
			fragmentEntryProcessorValuesJSONObject);

		FragmentEntryLink fragmentEntryLink =
			_fragmentEntryLinkLocalService.addFragmentEntryLink(
				layout.getUserId(), layout.getGroupId(), 0, fragmentEntryId,
				_segmentsExperienceLocalService.
					fetchDefaultSegmentsExperienceId(layout.getPlid()),
				layout.getPlid(), css, html, js, configuration,
				jsonObject.toString(), StringUtil.randomId(), position,
				fragmentKey, ServiceContextThreadLocal.getServiceContext());

		List<Object> widgetInstances = (List<Object>)definitionMap.get(
			"widgetInstances");

		if (widgetInstances != null) {
			_processWidgetInstances(
				fragmentEntryLink, layout, warningMessages, widgetInstances);
		}

		return fragmentEntryLink;
	}

	private JSONObject _createBaseFragmentFieldJSONObject(
		LayoutStructureItemImporterContext layoutStructureItemImporterContext,
		Map<String, Object> map) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		if (map == null) {
			return jsonObject;
		}

		Map<String, Object> valueMap = (Map<String, Object>)map.get("value");

		if (valueMap != null) {
			String title = String.valueOf(valueMap.get("title"));

			if (title != null) {
				jsonObject.put("defaultValue", title);
			}
		}

		Map<String, Object> defaultFragmentInlineValueMap =
			(Map<String, Object>)map.get("defaultFragmentInlineValue");

		if (defaultFragmentInlineValueMap == null) {
			defaultFragmentInlineValueMap = (Map<String, Object>)map.get(
				"defaultValue");
		}

		if (defaultFragmentInlineValueMap != null) {
			jsonObject.put(
				"defaultValue", defaultFragmentInlineValueMap.get("value"));
		}

		Map<String, Object> valueI18nMap = (Map<String, Object>)map.get(
			"value_i18n");

		if (valueI18nMap != null) {
			for (Map.Entry<String, Object> entry : valueI18nMap.entrySet()) {
				jsonObject.put(entry.getKey(), entry.getValue());
			}

			return jsonObject;
		}

		processMapping(
			jsonObject, layoutStructureItemImporterContext,
			(Map<String, Object>)map.get("mapping"));

		return jsonObject;
	}

	private JSONObject _createFragmentConfigJSONObject(
		Map<String, Object> fragmentImageMap) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		if (fragmentImageMap == null) {
			return jsonObject;
		}

		Map<String, Object> descriptionMap =
			(Map<String, Object>)fragmentImageMap.get("description");

		if (descriptionMap == null) {
			return jsonObject;
		}

		String value = (String)descriptionMap.get("value");

		if (value != null) {
			jsonObject.put("alt", value);
		}

		Map<String, Object> localizedDescriptionMap =
			(Map<String, Object>)descriptionMap.get("value_i18n");

		if (localizedDescriptionMap == null) {
			return jsonObject;
		}

		JSONObject localizedDescriptionJSONObject =
			JSONFactoryUtil.createJSONObject();

		for (Map.Entry<String, Object> entry :
				localizedDescriptionMap.entrySet()) {

			localizedDescriptionJSONObject.put(
				entry.getKey(), entry.getValue());
		}

		jsonObject.put("alt", localizedDescriptionJSONObject);

		return jsonObject;
	}

	private JSONObject _createFragmentLinkConfigJSONObject(
		Map<String, Object> fragmentLinkMap,
		LayoutStructureItemImporterContext layoutStructureItemImporterContext) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		if (fragmentLinkMap == null) {
			return jsonObject;
		}

		Map<String, Object> valueI18nMap =
			(Map<String, Object>)fragmentLinkMap.get("value_i18n");

		if (valueI18nMap != null) {
			for (Map.Entry<String, Object> entry : valueI18nMap.entrySet()) {
				Map<String, Object> fragmentLinkValueMap =
					(Map<String, Object>)entry.getValue();

				jsonObject.put(
					entry.getKey(),
					_createFragmentLinkValueConfigJSONObject(
						fragmentLinkValueMap,
						layoutStructureItemImporterContext));
			}
		}

		Map<String, Object> valueMap = (Map<String, Object>)fragmentLinkMap.get(
			"value");

		try {
			if (valueMap != null) {
				jsonObject = JSONUtil.merge(
					jsonObject,
					_createFragmentLinkValueConfigJSONObject(
						valueMap, layoutStructureItemImporterContext));
			}

			jsonObject = JSONUtil.merge(
				jsonObject,
				_createFragmentLinkValueConfigJSONObject(
					fragmentLinkMap, layoutStructureItemImporterContext));
		}
		catch (JSONException jsonException) {
			if (_log.isWarnEnabled()) {
				_log.warn(jsonException);
			}
		}

		jsonObject.put("mapperType", "link");

		return jsonObject;
	}

	private JSONObject _createFragmentLinkValueConfigJSONObject(
		Map<String, Object> fragmentLinkValueMap,
		LayoutStructureItemImporterContext layoutStructureItemImporterContext) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		if (fragmentLinkValueMap == null) {
			return jsonObject;
		}

		Map<String, Object> hrefMap =
			(Map<String, Object>)fragmentLinkValueMap.get("href");

		if (hrefMap == null) {
			return jsonObject;
		}

		Map<String, Object> defaultFragmentInlineValueMap =
			(Map<String, Object>)hrefMap.get("defaultFragmentInlineValue");

		if (defaultFragmentInlineValueMap == null) {
			defaultFragmentInlineValueMap = (Map<String, Object>)hrefMap.get(
				"defaultValue");
		}

		String target = (String)fragmentLinkValueMap.get("target");

		if (target != null) {
			if (Objects.equals(target, FragmentLink.Target.PARENT.getValue()) ||
				Objects.equals(target, FragmentLink.Target.TOP.getValue())) {

				target = FragmentLink.Target.SELF.getValue();
			}

			jsonObject.put(
				"target", "_" + StringUtil.lowerCaseFirstLetter(target));
		}

		Object value = hrefMap.get("value");

		if (value != null) {
			jsonObject.put("href", value);

			return jsonObject;
		}

		if (defaultFragmentInlineValueMap != null) {
			value = defaultFragmentInlineValueMap.get("value");
		}

		if (value != null) {
			jsonObject.put("href", value);
		}

		Map<String, Object> valueI18nMap = (Map<String, Object>)hrefMap.get(
			"value_i18n");

		if (valueI18nMap != null) {
			jsonObject.put("href", valueI18nMap);

			return jsonObject;
		}

		processMapping(
			jsonObject, layoutStructureItemImporterContext,
			(Map<String, Object>)hrefMap.get("mapping"));

		return jsonObject;
	}

	private JSONObject _createImageJSONObject(
		Map<String, Object> classPKReferencesMap) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		if (classPKReferencesMap == null) {
			return jsonObject;
		}

		for (Map.Entry<String, Object> entry :
				classPKReferencesMap.entrySet()) {

			Map<String, Object> classPKReferenceMap =
				(Map<String, Object>)entry.getValue();

			if (Objects.equals(
					classPKReferenceMap.get("className"),
					FileEntry.class.getName())) {

				long fileEntryId = GetterUtil.getLong(
					classPKReferenceMap.get("classPK"));

				try {
					FileEntry fileEntry =
						_portletFileRepository.getPortletFileEntry(fileEntryId);

					jsonObject.put(
						entry.getKey(),
						JSONUtil.put(
							"fileEntryId", fileEntryId
						).put(
							"url",
							DLURLHelperUtil.getDownloadURL(
								fileEntry, fileEntry.getFileVersion(), null,
								StringPool.BLANK, false, false)
						));
				}
				catch (PortalException portalException) {
					if (_log.isWarnEnabled()) {
						_log.warn("Unable to get file entry", portalException);
					}
				}
			}
		}

		return jsonObject;
	}

	private JSONObject _deepMerge(
			JSONObject jsonObject1, JSONObject jsonObject2)
		throws Exception {

		if (jsonObject1 == null) {
			return JSONFactoryUtil.createJSONObject(jsonObject2.toString());
		}

		if (jsonObject2 == null) {
			return JSONFactoryUtil.createJSONObject(jsonObject1.toString());
		}

		JSONObject jsonObject3 = JSONFactoryUtil.createJSONObject(
			jsonObject1.toString());

		Iterator<String> iterator = jsonObject2.keys();

		while (iterator.hasNext()) {
			String key = iterator.next();

			if (!jsonObject3.has(key)) {
				jsonObject3.put(key, jsonObject2.get(key));
			}
			else {
				Object value1 = jsonObject1.get(key);
				Object value2 = jsonObject2.get(key);

				if ((value1 instanceof JSONObject) &&
					(value2 instanceof JSONObject)) {

					jsonObject3.put(
						key,
						_deepMerge(
							(JSONObject)value1,
							jsonObject2.getJSONObject(key)));
				}
				else {
					jsonObject3.put(key, value2);
				}
			}
		}

		return jsonObject3;
	}

	private Map<String, String> _getConfigurationTypes(String configuration)
		throws Exception {

		Map<String, String> configurationTypes = new HashMap<>();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(configuration);

		JSONArray fieldSetsJSONArray = jsonObject.getJSONArray("fieldSets");

		if (fieldSetsJSONArray == null) {
			return configurationTypes;
		}

		for (int i = 0; i < fieldSetsJSONArray.length(); i++) {
			JSONObject fieldsJSONObject = fieldSetsJSONArray.getJSONObject(i);

			JSONArray fieldsJSONArray = fieldsJSONObject.getJSONArray("fields");

			for (int j = 0; j < fieldsJSONArray.length(); j++) {
				JSONObject fieldJSONObject = fieldsJSONArray.getJSONObject(j);

				configurationTypes.put(
					fieldJSONObject.getString("name"),
					fieldJSONObject.getString("type"));
			}
		}

		return configurationTypes;
	}

	private FragmentEntry _getFragmentEntry(
			long companyId, long groupId, String fragmentKey)
		throws Exception {

		FragmentEntry fragmentEntry =
			_fragmentEntryLocalService.fetchFragmentEntry(groupId, fragmentKey);

		if (fragmentEntry != null) {
			return fragmentEntry;
		}

		Company company = _companyLocalService.getCompanyById(companyId);

		fragmentEntry = _fragmentEntryLocalService.fetchFragmentEntry(
			company.getGroupId(), fragmentKey);

		if (fragmentEntry != null) {
			return fragmentEntry;
		}

		return _fragmentCollectionContributorTracker.getFragmentEntry(
			fragmentKey);
	}

	private String _getProcessedHTML(
			long companyId, String configuration,
			FragmentCollection fragmentCollection, String html)
		throws Exception {

		String processedHTML = _replaceResources(fragmentCollection, html);

		FragmentEntryLink fragmentEntryLink =
			_fragmentEntryLinkLocalService.createFragmentEntryLink(0L);

		fragmentEntryLink.setCompanyId(companyId);
		fragmentEntryLink.setHtml(processedHTML);
		fragmentEntryLink.setConfiguration(configuration);

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		if (serviceContext == null) {
			return processedHTML;
		}

		FragmentEntryProcessorContext fragmentEntryProcessorContext =
			new DefaultFragmentEntryProcessorContext(
				serviceContext.getRequest(), serviceContext.getResponse(),
				FragmentEntryLinkConstants.EDIT,
				LocaleUtil.getMostRelevantLocale());

		return _fragmentEntryProcessorRegistry.processFragmentEntryLinkHTML(
			fragmentEntryLink, fragmentEntryProcessorContext);
	}

	private String _getWarningMessage(long groupId, String fragmentKey)
		throws Exception {

		Locale locale = null;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		if (serviceContext != null) {
			locale = serviceContext.getLocale();
		}
		else {
			locale = _portal.getSiteDefaultLocale(groupId);
		}

		return _language.format(
			locale, "fragment-with-key-x-was-ignored-because-it-does-not-exist",
			new String[] {fragmentKey});
	}

	private void _processWidgetInstances(
			FragmentEntryLink fragmentEntryLink, Layout layout,
			Set<String> warningMessages, List<Object> widgetInstances)
		throws Exception {

		for (Object widgetInstance : widgetInstances) {
			Map<String, Object> widgetInstanceMap =
				(Map<String, Object>)widgetInstance;

			String widgetName = (String)widgetInstanceMap.get("widgetName");

			if (Validator.isNull(widgetName)) {
				continue;
			}

			String widgetInstanceId = (String)widgetInstanceMap.get(
				"widgetInstanceId");

			if (widgetInstanceId != null) {
				widgetInstanceId =
					fragmentEntryLink.getNamespace() + widgetInstanceId;
			}
			else {
				Portlet portlet = _portletLocalService.getPortletById(
					widgetName);

				if ((portlet != null) && portlet.isInstanceable()) {
					widgetInstanceId = fragmentEntryLink.getNamespace();
				}
			}

			Map<String, Object> widgetConfigDefinitionMap =
				(Map<String, Object>)widgetInstanceMap.get("widgetConfig");

			_portletConfigurationImporterHelper.importPortletConfiguration(
				layout.getPlid(),
				PortletIdCodec.encode(widgetName, widgetInstanceId),
				widgetConfigDefinitionMap);

			List<Map<String, Object>> widgetPermissionsMaps =
				(List<Map<String, Object>>)widgetInstanceMap.get(
					"widgetPermissions");

			_portletPermissionsImporterHelper.importPortletPermissions(
				layout.getPlid(),
				PortletIdCodec.encode(widgetName, widgetInstanceId),
				warningMessages, widgetPermissionsMaps);
		}
	}

	private String _replaceResources(
			FragmentCollection fragmentCollection, String html)
		throws Exception {

		if (fragmentCollection == null) {
			return html;
		}

		Matcher matcher = _pattern.matcher(html);

		while (matcher.find()) {
			FileEntry fileEntry = _portletFileRepository.fetchPortletFileEntry(
				fragmentCollection.getGroupId(),
				fragmentCollection.getResourcesFolderId(), matcher.group(1));

			String fileEntryURL = StringPool.BLANK;

			if (fileEntry != null) {
				fileEntryURL = DLURLHelperUtil.getDownloadURL(
					fileEntry, fileEntry.getFileVersion(), null,
					StringPool.BLANK, false, false);
			}

			html = StringUtil.replace(html, matcher.group(), fileEntryURL);
		}

		return html;
	}

	private JSONObject _toBackgroundImageFragmentEntryProcessorJSONObject(
		LayoutStructureItemImporterContext layoutStructureItemImporterContext,
		List<Object> fragmentFields) {

		JSONObject backgroundImageFragmentEntryProcessorValuesJSONObject =
			JSONFactoryUtil.createJSONObject();

		for (Object fragmentField : fragmentFields) {
			Map<String, Object> fragmentFieldMap =
				(Map<String, Object>)fragmentField;

			Map<String, Object> fragmentFieldValueMap =
				(Map<String, Object>)fragmentFieldMap.get("value");

			Map<String, Object> backgroundFragmentImageMap =
				(Map<String, Object>)fragmentFieldValueMap.get(
					"backgroundFragmentImage");

			if (MapUtil.isEmpty(backgroundFragmentImageMap)) {
				backgroundFragmentImageMap =
					(Map<String, Object>)fragmentFieldValueMap.get(
						"backgroundImage");
			}

			if (backgroundFragmentImageMap == null) {
				continue;
			}

			Map<String, Object> urlMap =
				(Map<String, Object>)backgroundFragmentImageMap.get("url");

			JSONObject fragmentFieldValueJSONObject =
				_createBaseFragmentFieldJSONObject(
					layoutStructureItemImporterContext, urlMap);

			Map<String, Object> titleMap =
				(Map<String, Object>)backgroundFragmentImageMap.get("title");

			if (titleMap != null) {
				fragmentFieldValueJSONObject.put(
					"config",
					JSONUtil.put("imageTitle", titleMap.get("value")));
			}

			backgroundImageFragmentEntryProcessorValuesJSONObject.put(
				(String)fragmentFieldMap.get("id"),
				fragmentFieldValueJSONObject);
		}

		return backgroundImageFragmentEntryProcessorValuesJSONObject;
	}

	private JSONObject _toEditableFragmentEntryProcessorJSONObject(
		Map<String, String> editableTypes, List<Object> fragmentFields,
		LayoutStructureItemImporterContext layoutStructureItemImporterContext) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		if (fragmentFields == null) {
			return jsonObject;
		}

		for (Object fragmentField : fragmentFields) {
			JSONObject fragmentFieldJSONObject =
				JSONFactoryUtil.createJSONObject();

			Map<String, Object> fragmentFieldMap =
				(Map<String, Object>)fragmentField;

			String fragmentFieldId = (String)fragmentFieldMap.get("id");

			if (Validator.isNull(fragmentFieldId)) {
				continue;
			}

			Map<String, Object> valueMap =
				(Map<String, Object>)fragmentFieldMap.get("value");

			if (valueMap == null) {
				continue;
			}

			JSONObject editableFieldConfigJSONObject =
				_createFragmentLinkConfigJSONObject(
					(Map<String, Object>)valueMap.get("fragmentLink"),
					layoutStructureItemImporterContext);

			JSONObject baseFragmentFieldJSONObject =
				_createBaseFragmentFieldJSONObject(
					layoutStructureItemImporterContext,
					(Map<String, Object>)valueMap.get("text"));

			if (Objects.equals(editableTypes.get(fragmentFieldId), "html")) {
				baseFragmentFieldJSONObject =
					_createBaseFragmentFieldJSONObject(
						layoutStructureItemImporterContext,
						(Map<String, Object>)valueMap.get("html"));
			}

			if (Objects.equals(editableTypes.get(fragmentFieldId), "image")) {
				Map<String, Object> fragmentImageMap =
					(Map<String, Object>)valueMap.get("fragmentImage");

				baseFragmentFieldJSONObject =
					JSONFactoryUtil.createJSONObject();

				if (fragmentImageMap != null) {
					if (fragmentImageMap.containsKey("url")) {
						baseFragmentFieldJSONObject =
							_createBaseFragmentFieldJSONObject(
								layoutStructureItemImporterContext,
								(Map<String, Object>)fragmentImageMap.get(
									"url"));
					}

					if (fragmentImageMap.containsKey(
							"fragmentImageClassPKReference")) {

						Map<String, Object> fragmentImageClassPKReferenceMap =
							(Map<String, Object>)fragmentImageMap.get(
								"fragmentImageClassPKReference");

						baseFragmentFieldJSONObject = _createImageJSONObject(
							(Map<String, Object>)
								fragmentImageClassPKReferenceMap.get(
									"classPKReferences"));

						Map<String, String> fragmentImageConfigurationMap =
							(Map<String, String>)
								fragmentImageClassPKReferenceMap.get(
									"fragmentImageConfiguration");

						JSONObject amImageConfigurationJSONObject =
							JSONFactoryUtil.createJSONObject();

						for (Map.Entry<String, String> entry :
								fragmentImageConfigurationMap.entrySet()) {

							amImageConfigurationJSONObject.put(
								entry.getKey(), entry.getValue());
						}

						try {
							editableFieldConfigJSONObject = JSONUtil.merge(
								editableFieldConfigJSONObject,
								JSONUtil.put(
									"imageConfiguration",
									amImageConfigurationJSONObject));
						}
						catch (JSONException jsonException) {
							if (_log.isWarnEnabled()) {
								_log.warn(jsonException);
							}
						}
					}
				}

				try {
					editableFieldConfigJSONObject = JSONUtil.merge(
						editableFieldConfigJSONObject,
						_createFragmentConfigJSONObject(fragmentImageMap));
				}
				catch (JSONException jsonException) {
					if (_log.isWarnEnabled()) {
						_log.warn(jsonException);
					}
				}
			}

			if (editableFieldConfigJSONObject.length() > 0) {
				fragmentFieldJSONObject.put(
					"config", editableFieldConfigJSONObject);
			}

			try {
				jsonObject.put(
					fragmentFieldId,
					JSONUtil.merge(
						fragmentFieldJSONObject, baseFragmentFieldJSONObject));
			}
			catch (JSONException jsonException) {
				if (_log.isWarnEnabled()) {
					_log.warn(jsonException);
				}
			}
		}

		return jsonObject;
	}

	private JSONObject _toFreeMarkerFragmentEntryProcessorJSONObject(
		Map<String, String> configurationTypes,
		Map<String, Object> fragmentConfigMap) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		if (fragmentConfigMap == null) {
			return jsonObject;
		}

		for (Map.Entry<String, Object> entry : fragmentConfigMap.entrySet()) {
			if (entry.getValue() instanceof HashMap) {
				Map<String, Object> childFragmentConfigMap =
					(Map<String, Object>)entry.getValue();

				jsonObject.put(
					entry.getKey(),
					_toFreeMarkerFragmentEntryProcessorJSONObject(
						configurationTypes, childFragmentConfigMap));
			}
			else {
				String type = configurationTypes.get(entry.getKey());

				if (Objects.equals(type, "colorPalette")) {
					jsonObject.put(
						entry.getKey(),
						JSONUtil.put("color", entry.getValue()));
				}
				else {
					jsonObject.put(entry.getKey(), entry.getValue());
				}
			}
		}

		return jsonObject;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FragmentLayoutStructureItemImporter.class);

	private static final Pattern _pattern = Pattern.compile(
		"\\[resources:(.+?)\\]");

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private FragmentCollectionContributorTracker
		_fragmentCollectionContributorTracker;

	@Reference
	private FragmentCollectionService _fragmentCollectionService;

	@Reference
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@Reference
	private FragmentEntryLocalService _fragmentEntryLocalService;

	@Reference
	private FragmentEntryProcessorRegistry _fragmentEntryProcessorRegistry;

	@Reference
	private FragmentEntryValidator _fragmentEntryValidator;

	@Reference
	private FragmentRendererTracker _fragmentRendererTracker;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference
	private PortletConfigurationImporterHelper
		_portletConfigurationImporterHelper;

	@Reference
	private PortletFileRepository _portletFileRepository;

	@Reference
	private PortletLocalService _portletLocalService;

	@Reference
	private PortletPermissionsImporterHelper _portletPermissionsImporterHelper;

	@Reference
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

}