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

package com.liferay.layout.page.template.internal.upgrade.v3_3_0;

import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.layout.page.template.internal.upgrade.v3_3_0.util.EditableValuesTransformerUtil;
import com.liferay.layout.page.template.internal.upgrade.v3_3_0.util.LayoutDataConverter;
import com.liferay.layout.util.structure.FragmentStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Eudaldo Alonso
 */
public class LayoutPageTemplateStructureRelUpgradeProcess
	extends UpgradeProcess {

	public LayoutPageTemplateStructureRelUpgradeProcess(
		FragmentEntryLinkLocalService fragmentEntryLinkLocalService,
		PortletPreferencesLocalService portletPreferencesLocalService) {

		_fragmentEntryLinkLocalService = fragmentEntryLinkLocalService;
		_portletPreferencesLocalService = portletPreferencesLocalService;
	}

	@Override
	protected void doUpgrade() throws Exception {
		_upgradeLayoutPageTemplateStructureRel();
	}

	private List<PortletPreferences> _getPortletPreferencesByPlid(long plid) {
		if (_portletPreferencesMap.containsKey(plid)) {
			return _portletPreferencesMap.get(plid);
		}

		List<PortletPreferences> portletPreferencesList =
			_portletPreferencesLocalService.getPortletPreferencesByPlid(plid);

		Stream<PortletPreferences> stream = portletPreferencesList.stream();

		portletPreferencesList = stream.filter(
			portletPreferences -> {
				String portletId = portletPreferences.getPortletId();

				return portletId.contains(_INSTANCE_SEPARATOR) &&
					   (portletId.contains(_SEGMENTS_EXPERIENCE_SEPARATOR_1) ||
						portletId.contains(_SEGMENTS_EXPERIENCE_SEPARATOR_2));
			}
		).collect(
			Collectors.toList()
		);

		_portletPreferencesMap.put(plid, portletPreferencesList);

		return _portletPreferencesMap.get(plid);
	}

	private void _updatePortletPreferences(
		String newNamespace, String oldNamespace, long plid,
		long segmentsExperienceId) {

		List<PortletPreferences> portletPreferencesList =
			_getPortletPreferencesByPlid(plid);

		for (PortletPreferences portletPreferences : portletPreferencesList) {
			String portletId = portletPreferences.getPortletId();

			if (!portletId.contains(oldNamespace) ||
				(!portletId.contains(
					_SEGMENTS_EXPERIENCE_SEPARATOR_1 + segmentsExperienceId) &&
				 !portletId.contains(
					 _SEGMENTS_EXPERIENCE_SEPARATOR_2 +
						 segmentsExperienceId))) {

				continue;
			}

			String newPortletId = StringUtil.replace(
				portletId,
				new String[] {
					oldNamespace,
					_SEGMENTS_EXPERIENCE_SEPARATOR_1 + segmentsExperienceId,
					_SEGMENTS_EXPERIENCE_SEPARATOR_2 + segmentsExperienceId
				},
				new String[] {
					newNamespace, StringPool.BLANK, StringPool.BLANK
				});

			portletPreferences.setPortletId(newPortletId);

			_portletPreferencesLocalService.updatePortletPreferences(
				portletPreferences);
		}
	}

	private String _upgradeLayoutData(String data, long segmentsExperienceId)
		throws PortalException {

		data = LayoutDataConverter.convert(data);

		LayoutStructure layoutStructure = LayoutStructure.of(data);

		for (LayoutStructureItem layoutStructureItem :
				layoutStructure.getLayoutStructureItems()) {

			if (!(layoutStructureItem instanceof
					FragmentStyledLayoutStructureItem)) {

				continue;
			}

			FragmentStyledLayoutStructureItem
				fragmentStyledLayoutStructureItem =
					(FragmentStyledLayoutStructureItem)layoutStructureItem;

			FragmentEntryLink fragmentEntryLink =
				_fragmentEntryLinkLocalService.fetchFragmentEntryLink(
					fragmentStyledLayoutStructureItem.getFragmentEntryLinkId());

			if (fragmentEntryLink == null) {
				continue;
			}

			if (segmentsExperienceId == _SEGMENTS_EXPERIENCE_ID_DEFAULT) {
				fragmentEntryLink.setEditableValues(
					EditableValuesTransformerUtil.getEditableValues(
						fragmentEntryLink.getEditableValues(),
						segmentsExperienceId));

				_fragmentEntryLinkLocalService.updateFragmentEntryLink(
					fragmentEntryLink);

				continue;
			}

			String newNamespace = StringUtil.randomId();
			String oldNamespace = fragmentEntryLink.getNamespace();

			JSONObject editableValuesJSONObject =
				JSONFactoryUtil.createJSONObject(
					fragmentEntryLink.getEditableValues());

			String instanceId = editableValuesJSONObject.getString(
				"instanceId");
			String portletId = editableValuesJSONObject.getString("portletId");

			if (Validator.isNotNull(instanceId) &&
				Validator.isNotNull(portletId)) {

				editableValuesJSONObject.remove("instanceId");

				editableValuesJSONObject.put("instanceId", newNamespace);

				oldNamespace = instanceId;
			}

			_updatePortletPreferences(
				newNamespace, oldNamespace, fragmentEntryLink.getPlid(),
				segmentsExperienceId);

			FragmentEntryLink newFragmentEntryLink =
				_fragmentEntryLinkLocalService.addFragmentEntryLink(
					fragmentEntryLink.getUserId(),
					fragmentEntryLink.getGroupId(),
					fragmentEntryLink.getOriginalFragmentEntryLinkId(),
					fragmentEntryLink.getFragmentEntryId(),
					segmentsExperienceId, fragmentEntryLink.getPlid(),
					fragmentEntryLink.getCss(), fragmentEntryLink.getHtml(),
					fragmentEntryLink.getJs(),
					fragmentEntryLink.getConfiguration(),
					EditableValuesTransformerUtil.getEditableValues(
						editableValuesJSONObject.toString(),
						segmentsExperienceId),
					newNamespace, fragmentEntryLink.getPosition(),
					fragmentEntryLink.getRendererKey(), new ServiceContext());

			fragmentStyledLayoutStructureItem.setFragmentEntryLinkId(
				newFragmentEntryLink.getFragmentEntryLinkId());
		}

		JSONObject layoutDataJSONObject = layoutStructure.toJSONObject();

		return layoutDataJSONObject.toString();
	}

	private void _upgradeLayoutPageTemplateStructureRel() throws Exception {
		try (Statement s = connection.createStatement();
			ResultSet resultSet = s.executeQuery(
				"select lPageTemplateStructureRelId, segmentsExperienceId, " +
					"data_ from LayoutPageTemplateStructureRel order by " +
						"segmentsExperienceId desc");
			PreparedStatement preparedStatement =
				AutoBatchPreparedStatementUtil.autoBatch(
					connection.prepareStatement(
						"update LayoutPageTemplateStructureRel set data_ = ? " +
							"where lPageTemplateStructureRelId = ?"))) {

			while (resultSet.next()) {
				long layoutPageTemplateStructureRelId = resultSet.getLong(
					"lPageTemplateStructureRelId");

				long segmentsExperienceId = resultSet.getLong(
					"segmentsExperienceId");

				String data = resultSet.getString("data_");

				preparedStatement.setString(
					1, _upgradeLayoutData(data, segmentsExperienceId));

				preparedStatement.setLong(2, layoutPageTemplateStructureRelId);

				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();
		}
	}

	private static final String _INSTANCE_SEPARATOR = "_INSTANCE_";

	private static final long _SEGMENTS_EXPERIENCE_ID_DEFAULT = 0;

	private static final String _SEGMENTS_EXPERIENCE_SEPARATOR_1 =
		"_SEGMENTS_EXPERIENCE_";

	private static final String _SEGMENTS_EXPERIENCE_SEPARATOR_2 =
		"SEGMENTSEXPERIENCE";

	private final FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;
	private final PortletPreferencesLocalService
		_portletPreferencesLocalService;
	private final Map<Long, List<PortletPreferences>> _portletPreferencesMap =
		new HashMap<>();

}