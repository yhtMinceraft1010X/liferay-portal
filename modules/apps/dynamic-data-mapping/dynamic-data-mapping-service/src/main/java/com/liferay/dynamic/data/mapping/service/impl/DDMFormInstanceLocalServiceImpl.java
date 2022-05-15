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

import com.liferay.dynamic.data.mapping.constants.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.exception.FormInstanceNameException;
import com.liferay.dynamic.data.mapping.exception.FormInstanceStructureIdException;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesSerializer;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesSerializerSerializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesSerializerSerializeResponse;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceSettings;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceVersion;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalService;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceVersionLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.base.DDMFormInstanceLocalServiceBaseImpl;
import com.liferay.dynamic.data.mapping.service.persistence.DDMFormInstanceVersionPersistence;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.StorageType;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.dynamic.data.mapping.util.DDMFormInstanceFactory;
import com.liferay.dynamic.data.mapping.validator.DDMFormValuesValidator;
import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalService;
import com.liferay.portal.kernel.service.permission.ModelPermissions;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinition;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionLocalService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.mail.internet.InternetAddress;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	property = "model.class.name=com.liferay.dynamic.data.mapping.model.DDMFormInstance",
	service = AopService.class
)
public class DDMFormInstanceLocalServiceImpl
	extends DDMFormInstanceLocalServiceBaseImpl {

	@Override
	public DDMFormInstance addFormInstance(
			long userId, long groupId, long ddmStructureId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			DDMFormValues settingsDDMFormValues, ServiceContext serviceContext)
		throws PortalException {

		Locale defaultLocale = getDDMFormDefaultLocale(ddmStructureId);

		User user = _userLocalService.getUser(userId);

		validate(
			ddmStructureId, defaultLocale, nameMap, settingsDDMFormValues,
			user.getTimeZone());

		long ddmFormInstanceId = counterLocalService.increment();

		DDMFormInstance ddmFormInstance = ddmFormInstancePersistence.create(
			ddmFormInstanceId);

		ddmFormInstance.setUuid(serviceContext.getUuid());
		ddmFormInstance.setGroupId(groupId);
		ddmFormInstance.setCompanyId(user.getCompanyId());
		ddmFormInstance.setUserId(user.getUserId());
		ddmFormInstance.setUserName(user.getFullName());
		ddmFormInstance.setStructureId(ddmStructureId);
		ddmFormInstance.setVersion(_VERSION_DEFAULT);
		ddmFormInstance.setNameMap(nameMap, defaultLocale);
		ddmFormInstance.setDescriptionMap(descriptionMap, defaultLocale);
		ddmFormInstance.setSettings(serialize(settingsDDMFormValues));

		DDMFormInstance updatedDDMFormInstance =
			ddmFormInstancePersistence.update(ddmFormInstance);

		updateWorkflowDefinitionLink(
			ddmFormInstance, settingsDDMFormValues, serviceContext);

		if (GetterUtil.getBoolean(
				serviceContext.getAttribute("addResources"), true)) {

			if (serviceContext.isAddGroupPermissions() ||
				serviceContext.isAddGuestPermissions()) {

				addFormInstanceResources(
					ddmFormInstance, serviceContext.isAddGroupPermissions(),
					serviceContext.isAddGuestPermissions());
			}
			else {
				addFormInstanceResources(
					ddmFormInstance, serviceContext.getModelPermissions());
			}
		}

		addFormInstanceVersion(
			getStructureVersionId(ddmStructureId), user, ddmFormInstance,
			_VERSION_DEFAULT, serviceContext);

		return updatedDDMFormInstance;
	}

	@Override
	public DDMFormInstance addFormInstance(
			long userId, long groupId, long ddmStructureId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			String serializedSettingsDDMFormValues,
			ServiceContext serviceContext)
		throws PortalException {

		DDMFormValues settingsDDMFormValues = getFormInstanceSettingsFormValues(
			serializedSettingsDDMFormValues);

		return addFormInstance(
			userId, groupId, ddmStructureId, nameMap, descriptionMap,
			settingsDDMFormValues, serviceContext);
	}

	@Override
	public DDMFormInstance addFormInstance(
			long userId, long groupId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap, DDMForm ddmForm,
			DDMFormLayout ddmFormLayout, DDMFormValues settingsDDMFormValues,
			ServiceContext serviceContext)
		throws PortalException {

		DDMStructure ddmStructure = _ddmStructureLocalService.addStructure(
			userId, groupId, DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
			_classNameLocalService.getClassNameId(DDMFormInstance.class),
			StringPool.BLANK, nameMap, descriptionMap, ddmForm, ddmFormLayout,
			getStorageType(settingsDDMFormValues),
			DDMStructureConstants.TYPE_AUTO, serviceContext);

		return addFormInstance(
			userId, groupId, ddmStructure.getStructureId(), nameMap,
			descriptionMap, settingsDDMFormValues, serviceContext);
	}

	@Override
	public void addFormInstanceResources(
			DDMFormInstance ddmFormInstance, boolean addGroupPermissions,
			boolean addGuestPermissions)
		throws PortalException {

		_resourceLocalService.addResources(
			ddmFormInstance.getCompanyId(), ddmFormInstance.getGroupId(),
			ddmFormInstance.getUserId(), DDMFormInstance.class.getName(),
			ddmFormInstance.getFormInstanceId(), false, addGroupPermissions,
			addGuestPermissions);
	}

	@Override
	public void addFormInstanceResources(
			DDMFormInstance ddmFormInstance, ModelPermissions modelPermissions)
		throws PortalException {

		_resourceLocalService.addModelResources(
			ddmFormInstance.getCompanyId(), ddmFormInstance.getGroupId(),
			ddmFormInstance.getUserId(), DDMFormInstance.class.getName(),
			ddmFormInstance.getFormInstanceId(), modelPermissions);
	}

	@Override
	public DDMFormInstance copyFormInstance(
			long userId, long groupId, Map<Locale, String> nameMap,
			DDMFormInstance ddmFormInstance,
			DDMFormValues settingsDDMFormValues, ServiceContext serviceContext)
		throws PortalException {

		DDMStructure ddmStructure = ddmFormInstance.getStructure();

		serviceContext.setAttribute("addResources", Boolean.FALSE);

		DDMFormInstance newDDMFormInstance = addFormInstance(
			userId, groupId, nameMap, ddmFormInstance.getDescriptionMap(),
			ddmStructure.getDDMForm(), ddmStructure.getDDMFormLayout(),
			settingsDDMFormValues, serviceContext);

		_resourceLocalService.copyModelResources(
			ddmFormInstance.getCompanyId(), DDMFormInstance.class.getName(),
			ddmFormInstance.getFormInstanceId(),
			newDDMFormInstance.getFormInstanceId());

		return newDDMFormInstance;
	}

	@Override
	@SystemEvent(
		action = SystemEventConstants.ACTION_SKIP,
		type = SystemEventConstants.TYPE_DELETE
	)
	public void deleteFormInstance(DDMFormInstance ddmFormInstance)
		throws PortalException {

		_resourceLocalService.deleteResource(
			ddmFormInstance.getCompanyId(), DDMFormInstance.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			ddmFormInstance.getFormInstanceId());

		_ddmFormInstanceRecordLocalService.deleteFormInstanceRecords(
			ddmFormInstance.getFormInstanceId());

		_ddmFormInstanceVersionLocalService.deleteByFormInstanceId(
			ddmFormInstance.getFormInstanceId());

		long structureId = ddmFormInstance.getStructureId();

		if (_ddmStructureLocalService.fetchDDMStructure(structureId) != null) {
			_ddmStructureLocalService.deleteStructure(structureId);
		}

		_workflowDefinitionLinkLocalService.deleteWorkflowDefinitionLink(
			ddmFormInstance.getCompanyId(), ddmFormInstance.getGroupId(),
			DDMFormInstance.class.getName(),
			ddmFormInstance.getFormInstanceId(), 0);

		// See LPS-97208 and
		// DDMFormInstanceRecordSearchTest#testBasicSearchWithDefaultUser.

		deleteDDMFormInstance(ddmFormInstance);
	}

	@Override
	public void deleteFormInstance(long ddmFormInstanceId)
		throws PortalException {

		DDMFormInstance ddmFormInstance =
			ddmFormInstancePersistence.findByPrimaryKey(ddmFormInstanceId);

		deleteFormInstance(ddmFormInstance);
	}

	@Override
	public void deleteFormInstances(long groupId) throws PortalException {
		List<DDMFormInstance> ddmFormInstances =
			ddmFormInstancePersistence.findByGroupId(groupId);

		for (DDMFormInstance ddmFormInstance : ddmFormInstances) {
			deleteFormInstance(ddmFormInstance);
		}
	}

	@Override
	public DDMFormInstance fetchFormInstance(long ddmFormInstanceId) {
		return ddmFormInstancePersistence.fetchByPrimaryKey(ddmFormInstanceId);
	}

	@Override
	public DDMFormInstance getFormInstance(long ddmFormInstanceId)
		throws PortalException {

		return ddmFormInstancePersistence.findByPrimaryKey(ddmFormInstanceId);
	}

	@Override
	public DDMFormInstance getFormInstance(String uuid, long ddmFormInstanceId)
		throws PortalException {

		return ddmFormInstancePersistence.findByUUID_G(uuid, ddmFormInstanceId);
	}

	@Override
	public List<DDMFormInstance> getFormInstances(long groupId) {
		return ddmFormInstancePersistence.findByGroupId(groupId);
	}

	@Override
	public int getFormInstancesCount(long groupId) {
		return ddmFormInstancePersistence.countByGroupId(groupId);
	}

	@Override
	public int getFormInstancesCount(String uuid) throws PortalException {
		return ddmFormInstancePersistence.countByUuid(uuid);
	}

	@Override
	public DDMFormValues getFormInstanceSettingsFormValues(
			DDMFormInstance formInstance)
		throws PortalException {

		return getFormInstanceSettingsFormValues(formInstance.getSettings());
	}

	@Override
	public DDMFormInstanceSettings getFormInstanceSettingsModel(
			DDMFormInstance formInstance)
		throws PortalException {

		DDMFormValues formValues = getFormInstanceSettingsFormValues(
			formInstance);

		return DDMFormInstanceFactory.create(
			DDMFormInstanceSettings.class, formValues);
	}

	@Override
	public List<DDMFormInstance> search(
		long companyId, long groupId, String keywords, int start, int end,
		OrderByComparator<DDMFormInstance> orderByComparator) {

		return ddmFormInstanceFinder.findByKeywords(
			companyId, groupId, keywords, start, end, orderByComparator);
	}

	@Override
	public List<DDMFormInstance> search(
		long companyId, long groupId, String[] names, String[] descriptions,
		boolean andOperator, int start, int end,
		OrderByComparator<DDMFormInstance> orderByComparator) {

		return ddmFormInstanceFinder.findByC_G_N_D(
			companyId, groupId, names, descriptions, andOperator, start, end,
			orderByComparator);
	}

	@Override
	public int searchCount(long companyId, long groupId, String keywords) {
		return ddmFormInstanceFinder.countByKeywords(
			companyId, groupId, keywords);
	}

	@Override
	public int searchCount(
		long companyId, long groupId, String[] names, String[] descriptions,
		boolean andOperator) {

		return ddmFormInstanceFinder.countByC_G_N_D(
			companyId, groupId, names, descriptions, andOperator);
	}

	@Override
	public void sendEmail(
			long userId, String message, String subject,
			String[] toEmailAddresses)
		throws Exception {

		User user = _userLocalService.getUser(userId);

		MailMessage mailMessage = new MailMessage(
			new InternetAddress(user.getEmailAddress(), user.getFullName()),
			subject, message, false);

		List<InternetAddress> internetAddresses = new ArrayList<>();

		for (String toEmailAddress : toEmailAddresses) {
			internetAddresses.add(new InternetAddress(toEmailAddress));
		}

		mailMessage.setTo(internetAddresses.toArray(new InternetAddress[0]));

		_mailService.sendEmail(mailMessage);
	}

	@Override
	public DDMFormInstance updateFormInstance(
			long formInstanceId, DDMFormValues settingsDDMFormValues)
		throws PortalException {

		Date date = new Date();

		validateFormInstanceSettings(settingsDDMFormValues, null);

		DDMFormInstance formInstance =
			ddmFormInstancePersistence.findByPrimaryKey(formInstanceId);

		formInstance.setModifiedDate(date);
		formInstance.setSettings(serialize(settingsDDMFormValues));

		return ddmFormInstancePersistence.update(formInstance);
	}

	@Override
	public DDMFormInstance updateFormInstance(
			long userId, long ddmFormInstanceId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap, DDMForm ddmForm,
			DDMFormLayout ddmFormLayout, DDMFormValues settingsDDMFormValues,
			ServiceContext serviceContext)
		throws PortalException {

		DDMFormInstance ddmFormInstance =
			ddmFormInstancePersistence.findByPrimaryKey(ddmFormInstanceId);

		_ddmStructureLocalService.updateStructure(
			serviceContext.getUserId(), ddmFormInstance.getStructureId(),
			DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID, nameMap,
			descriptionMap, ddmForm, ddmFormLayout, serviceContext);

		return doUpdateFormInstance(
			userId, ddmFormInstance.getStructureId(), nameMap, descriptionMap,
			settingsDDMFormValues, serviceContext, ddmFormInstance);
	}

	@Override
	public DDMFormInstance updateFormInstance(
			long ddmFormInstanceId, long ddmStructureId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			DDMFormValues settingsDDMFormValues, ServiceContext serviceContext)
		throws PortalException {

		DDMFormInstance ddmFormInstance =
			ddmFormInstancePersistence.findByPrimaryKey(ddmFormInstanceId);

		return doUpdateFormInstance(
			serviceContext.getUserId(), ddmStructureId, nameMap, descriptionMap,
			settingsDDMFormValues, serviceContext, ddmFormInstance);
	}

	protected DDMFormInstanceVersion addFormInstanceVersion(
			long ddmStructureVersionId, User user,
			DDMFormInstance ddmFormInstance, String version,
			ServiceContext serviceContext)
		throws PortalException {

		long ddmFormInstanceVersionId = counterLocalService.increment();

		DDMFormInstanceVersion ddmFormInstanceVersion =
			_ddmFormInstanceVersionPersistence.create(ddmFormInstanceVersionId);

		ddmFormInstanceVersion.setGroupId(ddmFormInstance.getGroupId());
		ddmFormInstanceVersion.setCompanyId(ddmFormInstance.getCompanyId());
		ddmFormInstanceVersion.setUserId(ddmFormInstance.getUserId());
		ddmFormInstanceVersion.setUserName(ddmFormInstance.getUserName());
		ddmFormInstanceVersion.setCreateDate(ddmFormInstance.getModifiedDate());
		ddmFormInstanceVersion.setFormInstanceId(
			ddmFormInstance.getFormInstanceId());
		ddmFormInstanceVersion.setStructureVersionId(ddmStructureVersionId);
		ddmFormInstanceVersion.setName(ddmFormInstance.getName());
		ddmFormInstanceVersion.setDescription(ddmFormInstance.getDescription());
		ddmFormInstanceVersion.setSettings(ddmFormInstance.getSettings());
		ddmFormInstanceVersion.setVersion(version);
		ddmFormInstanceVersion.setStatus(
			GetterUtil.getInteger(
				serviceContext.getAttribute("status"),
				WorkflowConstants.STATUS_APPROVED));
		ddmFormInstanceVersion.setStatusByUserId(user.getUserId());
		ddmFormInstanceVersion.setStatusByUserName(user.getFullName());
		ddmFormInstanceVersion.setStatusDate(ddmFormInstance.getModifiedDate());

		return _ddmFormInstanceVersionPersistence.update(
			ddmFormInstanceVersion);
	}

	protected DDMFormInstance doUpdateFormInstance(
			long userId, long ddmStructureId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap,
			DDMFormValues settingsDDMFormValues, ServiceContext serviceContext,
			DDMFormInstance ddmFormInstance)
		throws PortalException {

		Locale defaultLocale = getDDMFormDefaultLocale(ddmStructureId);

		User user = _userLocalService.getUser(userId);

		validate(
			ddmStructureId, defaultLocale, nameMap, settingsDDMFormValues,
			user.getTimeZone());

		DDMFormInstanceVersion latestDDMFormInstanceVersion =
			_ddmFormInstanceVersionLocalService.getLatestFormInstanceVersion(
				ddmFormInstance.getFormInstanceId());

		int status = GetterUtil.getInteger(
			serviceContext.getAttribute("status"),
			WorkflowConstants.STATUS_APPROVED);

		boolean updateVersion = false;

		if ((latestDDMFormInstanceVersion.getStatus() ==
				WorkflowConstants.STATUS_DRAFT) &&
			(status == WorkflowConstants.STATUS_DRAFT)) {

			updateVersion = true;
		}

		boolean majorVersion = GetterUtil.getBoolean(
			serviceContext.getAttribute("majorVersion"));

		String version = getNextVersion(
			latestDDMFormInstanceVersion.getVersion(), majorVersion);

		if (!updateVersion) {
			ddmFormInstance.setVersion(version);

			ddmFormInstance.setVersionUserId(user.getUserId());
			ddmFormInstance.setVersionUserName(user.getFullName());
		}

		ddmFormInstance.setNameMap(nameMap, defaultLocale);
		ddmFormInstance.setDescriptionMap(descriptionMap, defaultLocale);
		ddmFormInstance.setSettings(serialize(settingsDDMFormValues));

		DDMFormInstance updatedDDMFormInstance =
			ddmFormInstancePersistence.update(ddmFormInstance);

		if (status != WorkflowConstants.STATUS_DRAFT) {
			updateWorkflowDefinitionLink(
				ddmFormInstance, settingsDDMFormValues, serviceContext);
		}

		long ddmStructureVersionId = getStructureVersionId(ddmStructureId);

		if (updateVersion) {
			updateFormInstanceVersion(
				ddmStructureVersionId, user, ddmFormInstance);
		}
		else {
			addFormInstanceVersion(
				ddmStructureVersionId, user, ddmFormInstance, version,
				serviceContext);
		}

		return updatedDDMFormInstance;
	}

	protected Locale getDDMFormDefaultLocale(DDMFormInstance ddmFormInstance)
		throws PortalException {

		DDMStructure ddmStructure = ddmFormInstance.getStructure();

		DDMForm ddmForm = ddmStructure.getDDMForm();

		return ddmForm.getDefaultLocale();
	}

	protected Locale getDDMFormDefaultLocale(long ddmStructureId)
		throws PortalException {

		DDMStructure ddmStructure = _ddmStructureLocalService.getDDMStructure(
			ddmStructureId);

		DDMForm ddmForm = ddmStructure.getDDMForm();

		return ddmForm.getDefaultLocale();
	}

	protected DDMFormValues getFormInstanceSettingsFormValues(
			String serializedSettingsDDMFormValues)
		throws PortalException {

		DDMForm ddmForm = DDMFormFactory.create(DDMFormInstanceSettings.class);

		DDMFormValuesDeserializerDeserializeRequest.Builder builder =
			DDMFormValuesDeserializerDeserializeRequest.Builder.newBuilder(
				serializedSettingsDDMFormValues, ddmForm);

		DDMFormValuesDeserializerDeserializeResponse
			ddmFormValuesDeserializerDeserializeResponse =
				_jsonDDMFormValuesDeserializer.deserialize(builder.build());

		return ddmFormValuesDeserializerDeserializeResponse.getDDMFormValues();
	}

	protected String getNextVersion(String version, boolean majorVersion) {
		int[] versionParts = StringUtil.split(version, StringPool.PERIOD, 0);

		if (majorVersion) {
			versionParts[0]++;
			versionParts[1] = 0;
		}
		else {
			versionParts[1]++;
		}

		return versionParts[0] + StringPool.PERIOD + versionParts[1];
	}

	protected String getStorageType(DDMFormValues settingsDDMFormValues) {
		DDMFormInstanceSettings ddmFormInstanceSettings =
			DDMFormInstanceFactory.create(
				DDMFormInstanceSettings.class, settingsDDMFormValues);

		String storageType = ddmFormInstanceSettings.storageType();

		if (Validator.isNotNull(storageType)) {
			return storageType;
		}

		return StorageType.DEFAULT.toString();
	}

	protected long getStructureVersionId(long ddmStructureId)
		throws PortalException {

		DDMStructure ddmStructure = _ddmStructureLocalService.getStructure(
			ddmStructureId);

		DDMStructureVersion ddmStructureVersion =
			ddmStructure.getStructureVersion();

		return ddmStructureVersion.getStructureVersionId();
	}

	protected String getWorkflowDefinition(DDMFormValues ddmFormValues)
		throws PortalException {

		DDMFormInstanceSettings ddmFormInstanceSettings =
			DDMFormInstanceFactory.create(
				DDMFormInstanceSettings.class, ddmFormValues);

		return ddmFormInstanceSettings.workflowDefinition();
	}

	protected String serialize(DDMFormValues ddmFormValues) {
		DDMFormValuesSerializerSerializeRequest.Builder builder =
			DDMFormValuesSerializerSerializeRequest.Builder.newBuilder(
				ddmFormValues);

		DDMFormValuesSerializerSerializeResponse
			ddmFormValuesSerializerSerializeResponse =
				_jsonDDMFormValuesSerializer.serialize(builder.build());

		return ddmFormValuesSerializerSerializeResponse.getContent();
	}

	protected void updateFormInstanceVersion(
			long ddmStructureVersionId, User user,
			DDMFormInstance ddmFormInstance)
		throws PortalException {

		DDMFormInstanceVersion ddmFormInstanceVersion =
			_ddmFormInstanceVersionLocalService.getLatestFormInstanceVersion(
				ddmFormInstance.getFormInstanceId());

		ddmFormInstanceVersion.setUserId(ddmFormInstance.getUserId());
		ddmFormInstanceVersion.setUserName(ddmFormInstance.getUserName());
		ddmFormInstanceVersion.setStructureVersionId(ddmStructureVersionId);
		ddmFormInstanceVersion.setName(ddmFormInstance.getName());
		ddmFormInstanceVersion.setDescription(ddmFormInstance.getDescription());
		ddmFormInstanceVersion.setSettings(ddmFormInstance.getSettings());
		ddmFormInstanceVersion.setStatusByUserId(user.getUserId());
		ddmFormInstanceVersion.setStatusByUserName(user.getFullName());
		ddmFormInstanceVersion.setStatusDate(ddmFormInstance.getModifiedDate());

		_ddmFormInstanceVersionPersistence.update(ddmFormInstanceVersion);
	}

	protected void updateWorkflowDefinitionLink(
			DDMFormInstance formInstance, DDMFormValues settingsDDMFormValues,
			ServiceContext serviceContext)
		throws PortalException {

		String workflowDefinition = getWorkflowDefinition(
			settingsDDMFormValues);

		String latestWorkflowDefinition = "";

		if (Validator.isNotNull(workflowDefinition) &&
			!workflowDefinition.equals("no-workflow")) {

			KaleoDefinition kaleoDefinition =
				_kaleoDefinitionLocalService.getKaleoDefinition(
					workflowDefinition, serviceContext);

			latestWorkflowDefinition =
				workflowDefinition + StringPool.AT +
					kaleoDefinition.getVersion();
		}

		_workflowDefinitionLinkLocalService.updateWorkflowDefinitionLink(
			serviceContext.getUserId(), serviceContext.getCompanyId(),
			formInstance.getGroupId(), DDMFormInstance.class.getName(),
			formInstance.getFormInstanceId(), 0, latestWorkflowDefinition);
	}

	protected void validate(
			long ddmStructureId, Locale defaultLocale,
			Map<Locale, String> nameMap, DDMFormValues settingsDDMFormValues,
			TimeZone timeZone)
		throws PortalException {

		validateStructureId(ddmStructureId);

		validateName(nameMap, defaultLocale);

		validateFormInstanceSettings(settingsDDMFormValues, timeZone.getID());
	}

	protected void validateFormInstanceSettings(
			DDMFormValues settingsDDMFormValues, String timeZoneId)
		throws PortalException {

		_ddmFormValuesValidator.validate(settingsDDMFormValues, timeZoneId);
	}

	protected void validateName(
			Map<Locale, String> nameMap, Locale defaultLocale)
		throws PortalException {

		String name = nameMap.get(defaultLocale);

		if (Validator.isNull(name)) {
			throw new FormInstanceNameException(
				"Name is null for locale " + defaultLocale.getDisplayName());
		}
	}

	protected void validateStructureId(long ddmStructureId)
		throws PortalException {

		DDMStructure ddmStructure = _ddmStructureLocalService.fetchStructure(
			ddmStructureId);

		if (ddmStructure == null) {
			throw new FormInstanceStructureIdException(
				"No DDM structure exists with the DDM structure ID " +
					ddmStructureId);
		}
	}

	private static final String _VERSION_DEFAULT = "1.0";

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private DDMFormInstanceRecordLocalService
		_ddmFormInstanceRecordLocalService;

	@Reference
	private DDMFormInstanceVersionLocalService
		_ddmFormInstanceVersionLocalService;

	@Reference
	private DDMFormInstanceVersionPersistence
		_ddmFormInstanceVersionPersistence;

	@Reference
	private DDMFormValuesValidator _ddmFormValuesValidator;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference(target = "(ddm.form.values.deserializer.type=json)")
	private DDMFormValuesDeserializer _jsonDDMFormValuesDeserializer;

	@Reference(target = "(ddm.form.values.serializer.type=json)")
	private DDMFormValuesSerializer _jsonDDMFormValuesSerializer;

	@Reference
	private KaleoDefinitionLocalService _kaleoDefinitionLocalService;

	@Reference
	private MailService _mailService;

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private UserLocalService _userLocalService;

	@Reference
	private WorkflowDefinitionLinkLocalService
		_workflowDefinitionLinkLocalService;

}