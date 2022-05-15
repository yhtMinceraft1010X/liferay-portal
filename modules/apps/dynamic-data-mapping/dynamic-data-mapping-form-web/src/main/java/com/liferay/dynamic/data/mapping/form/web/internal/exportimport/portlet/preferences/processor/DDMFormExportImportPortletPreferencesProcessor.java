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

package com.liferay.dynamic.data.mapping.form.web.internal.exportimport.portlet.preferences.processor;

import com.liferay.dynamic.data.mapping.constants.DDMConstants;
import com.liferay.dynamic.data.mapping.constants.DDMPortletKeys;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalService;
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataException;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.portlet.preferences.processor.Capability;
import com.liferay.exportimport.portlet.preferences.processor.ExportImportPortletPreferencesProcessor;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + DDMPortletKeys.DYNAMIC_DATA_MAPPING_FORM,
	service = ExportImportPortletPreferencesProcessor.class
)
public class DDMFormExportImportPortletPreferencesProcessor
	implements ExportImportPortletPreferencesProcessor {

	@Override
	public List<Capability> getExportCapabilities() {
		return null;
	}

	@Override
	public List<Capability> getImportCapabilities() {
		return ListUtil.fromArray(_capability);
	}

	@Override
	public boolean isPublishDisplayedContent() {
		return false;
	}

	@Override
	public PortletPreferences processExportPortletPreferences(
			PortletDataContext portletDataContext,
			PortletPreferences portletPreferences)
		throws PortletDataException {

		try {
			portletDataContext.addPortletPermissions(
				DDMConstants.RESOURCE_NAME);
		}
		catch (PortalException portalException) {
			throw new PortletDataException(
				"Unable to export portlet permissions", portalException);
		}

		String portletId = portletDataContext.getPortletId();

		long formInstanceId = GetterUtil.getLong(
			portletPreferences.getValue("formInstanceId", null));

		if (formInstanceId == 0) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"FormInstance ID is not set for preferences of portlet " +
						portletId);
			}

			return portletPreferences;
		}

		long groupId = GetterUtil.getLong(
			portletPreferences.getValue("groupId", null));

		if (groupId == 0) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"No group ID found in preferences of portlet " + portletId);
			}

			return portletPreferences;
		}

		Group group = _groupLocalService.fetchGroup(groupId);

		if (group == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("No group found with group ID " + groupId);
			}

			return portletPreferences;
		}

		if (ExportImportThreadLocal.isStagingInProcess()) {
			if (!group.isStagedPortlet(
					DDMPortletKeys.DYNAMIC_DATA_MAPPING_FORM_ADMIN)) {

				if (_log.isDebugEnabled()) {
					_log.debug(
						"Form is not staged in the site " + group.getName());
				}

				return portletPreferences;
			}

			if (!group.isCompanyStagingGroup() && !group.isStaged() &&
				!group.isStagingGroup()) {

				return portletPreferences;
			}
		}

		DDMFormInstance ddmFormInstance =
			_ddmFormInstanceLocalService.fetchFormInstance(formInstanceId);

		if (ddmFormInstance != null) {
			StagedModelDataHandlerUtil.exportReferenceStagedModel(
				portletDataContext, portletId, ddmFormInstance);
		}

		return portletPreferences;
	}

	@Override
	public PortletPreferences processImportPortletPreferences(
			PortletDataContext portletDataContext,
			PortletPreferences portletPreferences)
		throws PortletDataException {

		try {
			portletDataContext.importPortletPermissions(
				DDMConstants.RESOURCE_NAME);
		}
		catch (PortalException portalException) {
			throw new PortletDataException(
				"Unable to export portlet permissions", portalException);
		}

		Map<Long, Long> formInstanceIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DDMFormInstance.class);

		long importedFormInstanceId = GetterUtil.getLong(
			portletPreferences.getValue("formInstanceId", null));

		long formInstanceId = MapUtil.getLong(
			formInstanceIds, importedFormInstanceId, importedFormInstanceId);

		Map<Long, Long> groupIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				Group.class);

		long importedGroupId = GetterUtil.getLong(
			portletPreferences.getValue("groupId", null));

		long groupId = MapUtil.getLong(
			groupIds, importedGroupId, importedGroupId);

		try {
			portletPreferences.setValue(
				"formInstanceId", String.valueOf(formInstanceId));
			portletPreferences.setValue("groupId", String.valueOf(groupId));
		}
		catch (ReadOnlyException readOnlyException) {
			throw new PortletDataException(
				"Unable to update portlet preferences during import",
				readOnlyException);
		}

		return portletPreferences;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMFormExportImportPortletPreferencesProcessor.class);

	@Reference(target = "(name=ReferencedStagedModelImporter)")
	private Capability _capability;

	@Reference
	private DDMFormInstanceLocalService _ddmFormInstanceLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

}