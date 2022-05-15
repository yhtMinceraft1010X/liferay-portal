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

package com.liferay.depot.service.impl;

import com.liferay.depot.constants.DepotRolesConstants;
import com.liferay.depot.exception.DepotEntryGroupException;
import com.liferay.depot.exception.DepotEntryNameException;
import com.liferay.depot.exception.DepotEntryStagedException;
import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.model.DepotEntryGroupRel;
import com.liferay.depot.service.DepotAppCustomizationLocalService;
import com.liferay.depot.service.base.DepotEntryLocalServiceBaseImpl;
import com.liferay.depot.service.persistence.DepotEntryGroupRelPersistence;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.GroupKeyException;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.ModelHintsUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupRoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.liferay.depot.model.DepotEntry",
	service = AopService.class
)
public class DepotEntryLocalServiceImpl extends DepotEntryLocalServiceBaseImpl {

	@Override
	public DepotEntry addDepotEntry(Group group, ServiceContext serviceContext)
		throws PortalException {

		if (!group.isDepot() ||
			!ParamUtil.getBoolean(serviceContext, "staging")) {

			throw new DepotEntryGroupException(
				"Unable to create staged depot entry for group " +
					group.getGroupId());
		}

		_validateNameMap(group.getNameMap(), LocaleUtil.getDefault());

		DepotEntry depotEntry = depotEntryPersistence.create(
			counterLocalService.increment());

		depotEntry.setUuid(serviceContext.getUuid());
		depotEntry.setGroupId(group.getGroupId());
		depotEntry.setCompanyId(serviceContext.getCompanyId());
		depotEntry.setUserId(serviceContext.getUserId());

		depotEntry = depotEntryPersistence.update(depotEntry);

		_resourceLocalService.addResources(
			serviceContext.getCompanyId(), 0, serviceContext.getUserId(),
			DepotEntry.class.getName(), depotEntry.getDepotEntryId(), false,
			false, false);

		return depotEntry;
	}

	@Override
	public DepotEntry addDepotEntry(
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			ServiceContext serviceContext)
		throws PortalException {

		_validateNameMap(nameMap, LocaleUtil.getDefault());

		DepotEntry depotEntry = depotEntryPersistence.create(
			counterLocalService.increment());

		depotEntry.setUuid(serviceContext.getUuid());

		Group group = _groupLocalService.addGroup(
			serviceContext.getUserId(), GroupConstants.DEFAULT_PARENT_GROUP_ID,
			DepotEntry.class.getName(), depotEntry.getDepotEntryId(),
			GroupConstants.DEFAULT_LIVE_GROUP_ID, nameMap, descriptionMap,
			GroupConstants.TYPE_DEPOT, true,
			GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION,
			"/asset-library-" + depotEntry.getDepotEntryId(), false, false,
			true, serviceContext);

		_userLocalService.addGroupUsers(
			group.getGroupId(), new long[] {serviceContext.getUserId()});

		User user = _userLocalService.getUser(serviceContext.getUserId());

		if (!user.isDefaultUser()) {
			Role role = _roleLocalService.getRole(
				group.getCompanyId(), DepotRolesConstants.ASSET_LIBRARY_OWNER);

			_userGroupRoleLocalService.addUserGroupRoles(
				user.getUserId(), group.getGroupId(),
				new long[] {role.getRoleId()});

			_userLocalService.addGroupUsers(
				group.getGroupId(), new long[] {user.getUserId()});
		}

		depotEntry.setGroupId(group.getGroupId());

		depotEntry.setCompanyId(serviceContext.getCompanyId());
		depotEntry.setUserId(serviceContext.getUserId());

		depotEntry = depotEntryPersistence.update(depotEntry);

		_resourceLocalService.addResources(
			serviceContext.getCompanyId(), 0, serviceContext.getUserId(),
			DepotEntry.class.getName(), depotEntry.getDepotEntryId(), false,
			false, false);

		return depotEntry;
	}

	@Override
	public DepotEntry deleteDepotEntry(long depotEntryId)
		throws PortalException {

		DepotEntry depotEntry = depotEntryPersistence.fetchByPrimaryKey(
			depotEntryId);

		if (_isStaged(depotEntry)) {
			throw new DepotEntryStagedException(
				"Unstage depot entry " + depotEntryId + " before deleting it");
		}

		_resourceLocalService.deleteResource(
			depotEntry, ResourceConstants.SCOPE_INDIVIDUAL);

		return super.deleteDepotEntry(depotEntryId);
	}

	@Override
	public DepotEntry fetchGroupDepotEntry(long groupId) {
		return depotEntryPersistence.fetchByGroupId(groupId);
	}

	/**
	 * @deprecated As of Cavanaugh (7.4.x)
	 */
	@Deprecated
	@Override
	public List<DepotEntry> getDepotEntryGroupRelsByUuidAndCompanyId(
		String uuid, long companyId) {

		return depotEntryPersistence.findByUuid_C(uuid, companyId);
	}

	@Override
	public List<DepotEntry> getGroupConnectedDepotEntries(
			long groupId, boolean ddmStructuresAvailable, int start, int end)
		throws PortalException {

		List<DepotEntry> depotEntries = new ArrayList<>();

		List<DepotEntryGroupRel> depotEntryGroupRels =
			_depotEntryGroupRelPersistence.findByDDMSA_TGI(
				ddmStructuresAvailable, groupId, start, end);

		for (DepotEntryGroupRel depotEntryGroupRel : depotEntryGroupRels) {
			depotEntries.add(
				depotEntryLocalService.getDepotEntry(
					depotEntryGroupRel.getDepotEntryId()));
		}

		return depotEntries;
	}

	@Override
	public List<DepotEntry> getGroupConnectedDepotEntries(
			long groupId, int start, int end)
		throws PortalException {

		return TransformUtil.transform(
			_depotEntryGroupRelPersistence.findByToGroupId(groupId, start, end),
			depotEntryGroupRel -> depotEntryLocalService.getDepotEntry(
				depotEntryGroupRel.getDepotEntryId()));
	}

	@Override
	public int getGroupConnectedDepotEntriesCount(long groupId) {
		return _depotEntryGroupRelPersistence.countByToGroupId(groupId);
	}

	@Override
	public DepotEntry getGroupDepotEntry(long groupId) throws PortalException {
		return depotEntryPersistence.findByGroupId(groupId);
	}

	@Override
	public DepotEntry updateDepotEntry(
			long depotEntryId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap,
			Map<String, Boolean> depotAppCustomizationMap,
			UnicodeProperties typeSettingsUnicodeProperties,
			ServiceContext serviceContext)
		throws PortalException {

		DepotEntry depotEntry = getDepotEntry(depotEntryId);

		depotEntry.setModifiedDate(serviceContext.getModifiedDate());

		depotEntry = depotEntryPersistence.update(depotEntry);

		for (Map.Entry<String, Boolean> entry :
				depotAppCustomizationMap.entrySet()) {

			_depotAppCustomizationLocalService.updateDepotAppCustomization(
				depotEntryId, entry.getValue(), entry.getKey());
		}

		_validateTypeSettingsProperties(
			depotEntry, typeSettingsUnicodeProperties);

		for (String name : nameMap.values()) {
			_validateName(name);
		}

		Group group = _groupLocalService.getGroup(depotEntry.getGroupId());

		UnicodeProperties currentTypeSettingsUnicodeProperties =
			group.getTypeSettingsProperties();

		boolean inheritLocales = GetterUtil.getBoolean(
			currentTypeSettingsUnicodeProperties.getProperty("inheritLocales"),
			true);

		inheritLocales = GetterUtil.getBoolean(
			typeSettingsUnicodeProperties.getProperty("inheritLocales"),
			inheritLocales);

		if (inheritLocales) {
			typeSettingsUnicodeProperties.setProperty(
				PropsKeys.LOCALES,
				StringUtil.merge(
					LocaleUtil.toLanguageIds(
						LanguageUtil.getAvailableLocales())));
		}

		currentTypeSettingsUnicodeProperties.putAll(
			typeSettingsUnicodeProperties);

		Locale locale = LocaleUtil.fromLanguageId(
			currentTypeSettingsUnicodeProperties.getProperty("languageId"));

		Optional<String> defaultNameOptional = _getDefaultNameOptional(
			nameMap, locale);

		defaultNameOptional.ifPresent(
			defaultName -> nameMap.put(locale, defaultName));

		group = _groupLocalService.updateGroup(
			depotEntry.getGroupId(), group.getParentGroupId(), nameMap,
			descriptionMap, group.getType(), group.isManualMembership(),
			group.getMembershipRestriction(), group.getFriendlyURL(),
			group.isInheritContent(), group.isActive(), serviceContext);

		_groupLocalService.updateGroup(
			group.getGroupId(),
			currentTypeSettingsUnicodeProperties.toString());

		return depotEntry;
	}

	private Optional<String> _getDefaultNameOptional(
		Map<Locale, String> nameMap, Locale defaultLocale) {

		if (Validator.isNotNull(nameMap.get(defaultLocale))) {
			return Optional.empty();
		}

		return Optional.of(
			_language.get(defaultLocale, "unnamed-asset-library"));
	}

	private boolean _isStaged(DepotEntry depotEntry) throws PortalException {
		if (depotEntry == null) {
			return false;
		}

		Group group = _groupLocalService.fetchGroup(depotEntry.getGroupId());

		if (group == null) {
			return false;
		}

		if (group.isStaged()) {
			return true;
		}

		return false;
	}

	private void _validateName(String name) throws PortalException {
		int groupKeyMaxLength = ModelHintsUtil.getMaxLength(
			Group.class.getName(), "name");

		if (Validator.isNotNull(name) &&
			(Validator.isNumber(name) || name.contains(StringPool.STAR) ||
			 name.contains(_ORGANIZATION_NAME_SUFFIX) ||
			 (name.length() > groupKeyMaxLength))) {

			throw new GroupKeyException();
		}
	}

	private void _validateNameMap(
			Map<Locale, String> nameMap, Locale defaultLocale)
		throws DepotEntryNameException {

		if (MapUtil.isEmpty(nameMap) ||
			Validator.isNull(nameMap.get(defaultLocale))) {

			throw new DepotEntryNameException();
		}
	}

	private void _validateTypeSettingsProperties(
			DepotEntry depotEntry,
			UnicodeProperties typeSettingsUnicodeProperties)
		throws LocaleException {

		if (!typeSettingsUnicodeProperties.containsKey("inheritLocales")) {
			return;
		}

		if (typeSettingsUnicodeProperties.containsKey(PropsKeys.LOCALES) &&
			Validator.isNull(
				typeSettingsUnicodeProperties.getProperty(PropsKeys.LOCALES))) {

			throw new LocaleException(
				LocaleException.TYPE_DEFAULT,
				"Must have at least one valid locale for asset library " +
					depotEntry.getGroupId());
		}

		boolean inheritLocales = GetterUtil.getBoolean(
			typeSettingsUnicodeProperties.getProperty("inheritLocales"));

		if (!inheritLocales &&
			!typeSettingsUnicodeProperties.containsKey(PropsKeys.LOCALES)) {

			throw new LocaleException(
				LocaleException.TYPE_DEFAULT,
				"Must have at least one valid locale for asset library " +
					depotEntry.getGroupId());
		}
	}

	private static final String _ORGANIZATION_NAME_SUFFIX = " LFR_ORGANIZATION";

	@Reference
	private DepotAppCustomizationLocalService
		_depotAppCustomizationLocalService;

	@Reference
	private DepotEntryGroupRelPersistence _depotEntryGroupRelPersistence;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private Language _language;

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private UserGroupRoleLocalService _userGroupRoleLocalService;

	@Reference
	private UserLocalService _userLocalService;

}