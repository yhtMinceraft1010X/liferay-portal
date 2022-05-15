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

package com.liferay.dynamic.data.mapping.service.impl;

import com.liferay.dynamic.data.mapping.constants.DDMActionKeys;
import com.liferay.dynamic.data.mapping.constants.DDMConstants;
import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.service.base.DDMDataProviderInstanceServiceBaseImpl;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	property = {
		"json.web.service.context.name=ddm",
		"json.web.service.context.path=DDMDataProviderInstance"
	},
	service = AopService.class
)
public class DDMDataProviderInstanceServiceImpl
	extends DDMDataProviderInstanceServiceBaseImpl {

	@Override
	public DDMDataProviderInstance addDataProviderInstance(
			long groupId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap, DDMFormValues ddmFormValues,
			String type, ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), groupId,
			DDMActionKeys.ADD_DATA_PROVIDER_INSTANCE);

		return ddmDataProviderInstanceLocalService.addDataProviderInstance(
			getUserId(), groupId, nameMap, descriptionMap, ddmFormValues, type,
			serviceContext);
	}

	@Override
	public void deleteDataProviderInstance(long dataProviderInstanceId)
		throws PortalException {

		_ddmDataProviderInstanceModelResourcePermission.check(
			getPermissionChecker(), dataProviderInstanceId, ActionKeys.DELETE);

		ddmDataProviderInstanceLocalService.deleteDataProviderInstance(
			dataProviderInstanceId);
	}

	@Override
	public DDMDataProviderInstance fetchDataProviderInstance(
			long dataProviderInstanceId)
		throws PortalException {

		DDMDataProviderInstance dataProviderInstance =
			ddmDataProviderInstanceLocalService.fetchDataProviderInstance(
				dataProviderInstanceId);

		if (dataProviderInstance == null) {
			return null;
		}

		_ddmDataProviderInstanceModelResourcePermission.check(
			getPermissionChecker(),
			dataProviderInstance.getDataProviderInstanceId(), ActionKeys.VIEW);

		return dataProviderInstance;
	}

	@Override
	public DDMDataProviderInstance fetchDataProviderInstanceByUuid(String uuid)
		throws PortalException {

		DDMDataProviderInstance dataProviderInstance =
			ddmDataProviderInstanceLocalService.fetchDataProviderInstanceByUuid(
				uuid);

		if (dataProviderInstance == null) {
			return null;
		}

		_ddmDataProviderInstanceModelResourcePermission.check(
			getPermissionChecker(),
			dataProviderInstance.getDataProviderInstanceId(), ActionKeys.VIEW);

		return dataProviderInstance;
	}

	@Override
	public DDMDataProviderInstance getDataProviderInstance(
			long dataProviderInstanceId)
		throws PortalException {

		_ddmDataProviderInstanceModelResourcePermission.check(
			getPermissionChecker(), dataProviderInstanceId, ActionKeys.VIEW);

		return ddmDataProviderInstanceLocalService.getDataProviderInstance(
			dataProviderInstanceId);
	}

	@Override
	public DDMDataProviderInstance getDataProviderInstanceByUuid(String uuid)
		throws PortalException {

		DDMDataProviderInstance ddmDataProviderInstance =
			ddmDataProviderInstanceLocalService.getDataProviderInstanceByUuid(
				uuid);

		_ddmDataProviderInstanceModelResourcePermission.check(
			getPermissionChecker(),
			ddmDataProviderInstance.getDataProviderInstanceId(),
			ActionKeys.VIEW);

		return ddmDataProviderInstance;
	}

	@Override
	public List<DDMDataProviderInstance> getDataProviderInstances(
		long companyId, long[] groupIds, int start, int end) {

		return ddmDataProviderInstanceFinder.filterByC_G(
			companyId, groupIds, start, end);
	}

	@Override
	public int getDataProviderInstancesCount(long companyId, long[] groupIds) {
		return ddmDataProviderInstanceFinder.filterCountByC_G(
			companyId, groupIds);
	}

	@Override
	public List<DDMDataProviderInstance> search(
		long companyId, long[] groupIds, String keywords, int start, int end,
		OrderByComparator<DDMDataProviderInstance> orderByComparator) {

		List<DDMDataProviderInstance> ddmDataProviderInstances =
			ddmDataProviderInstanceFinder.filterByKeywords(
				companyId, groupIds, keywords, start, end, orderByComparator);

		Stream<DDMDataProviderInstance> ddmDataProviderInstancesStream =
			ddmDataProviderInstances.stream();

		return ddmDataProviderInstancesStream.filter(
			ddmDataProviderInstance -> {
				try {
					return _ddmDataProviderInstanceModelResourcePermission.
						contains(
							getPermissionChecker(),
							ddmDataProviderInstance.getDataProviderInstanceId(),
							ActionKeys.VIEW);
				}
				catch (PortalException portalException) {
					_log.error(portalException);

					return false;
				}
			}
		).map(
			ddmDataProviderInstance -> _removeAuthenticationData(
				ddmDataProviderInstance)
		).collect(
			Collectors.toList()
		);
	}

	@Override
	public List<DDMDataProviderInstance> search(
		long companyId, long[] groupIds, String name, String description,
		boolean andOperator, int start, int end,
		OrderByComparator<DDMDataProviderInstance> orderByComparator) {

		return ddmDataProviderInstanceFinder.filterFindByC_G_N_D(
			companyId, groupIds, name, description, andOperator, start, end,
			orderByComparator);
	}

	@Override
	public int searchCount(long companyId, long[] groupIds, String keywords) {
		return ddmDataProviderInstanceFinder.filterCountByKeywords(
			companyId, groupIds, keywords);
	}

	@Override
	public int searchCount(
		long companyId, long[] groupIds, String name, String description,
		boolean andOperator) {

		return ddmDataProviderInstanceFinder.filterCountByC_G_N_D(
			companyId, groupIds, name, description, andOperator);
	}

	@Override
	public DDMDataProviderInstance updateDataProviderInstance(
			long dataProviderInstanceId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap, DDMFormValues ddmFormValues,
			ServiceContext serviceContext)
		throws PortalException {

		_ddmDataProviderInstanceModelResourcePermission.check(
			getPermissionChecker(), dataProviderInstanceId, ActionKeys.UPDATE);

		return ddmDataProviderInstanceLocalService.updateDataProviderInstance(
			getUserId(), dataProviderInstanceId, nameMap, descriptionMap,
			ddmFormValues, serviceContext);
	}

	@Reference(unbind = "-")
	protected void setJSONFactory(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	private JSONArray _filterFieldValues(JSONArray fieldValuesJSONArray) {
		JSONArray filteredFieldValuesJSONArray = _jsonFactory.createJSONArray();

		Iterator<JSONObject> iterator = fieldValuesJSONArray.iterator();

		while (iterator.hasNext()) {
			JSONObject fieldValueJSONObject = iterator.next();

			String fieldValueName = fieldValueJSONObject.getString("name");

			if (StringUtil.equals(fieldValueName, "password") ||
				StringUtil.equals(fieldValueName, "username")) {

				continue;
			}

			filteredFieldValuesJSONArray.put(fieldValueJSONObject);
		}

		return filteredFieldValuesJSONArray;
	}

	private DDMDataProviderInstance _removeAuthenticationData(
		DDMDataProviderInstance ddmDataProviderInstance) {

		try {
			JSONObject definitionJSONObject = _jsonFactory.createJSONObject(
				ddmDataProviderInstance.getDefinition());

			if (!definitionJSONObject.has("fieldValues")) {
				return ddmDataProviderInstance;
			}

			JSONArray fieldValuesJSONArray = definitionJSONObject.getJSONArray(
				"fieldValues");

			definitionJSONObject.put(
				"fieldValues", _filterFieldValues(fieldValuesJSONArray));

			ddmDataProviderInstance.setDefinition(
				definitionJSONObject.toString());
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to remove authentication data from data " +
						"providers search",
					exception);
			}
		}

		return ddmDataProviderInstance;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMDataProviderInstanceServiceImpl.class);

	@Reference(
		target = "(model.class.name=com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance)"
	)
	private ModelResourcePermission<DDMDataProviderInstance>
		_ddmDataProviderInstanceModelResourcePermission;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference(target = "(resource.name=" + DDMConstants.RESOURCE_NAME + ")")
	private PortletResourcePermission _portletResourcePermission;

}