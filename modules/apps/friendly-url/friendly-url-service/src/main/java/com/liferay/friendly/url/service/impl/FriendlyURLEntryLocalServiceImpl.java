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

package com.liferay.friendly.url.service.impl;

import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.friendly.url.exception.DuplicateFriendlyURLEntryException;
import com.liferay.friendly.url.exception.FriendlyURLLengthException;
import com.liferay.friendly.url.exception.NoSuchFriendlyURLEntryLocalizationException;
import com.liferay.friendly.url.model.FriendlyURLEntry;
import com.liferay.friendly.url.model.FriendlyURLEntryLocalization;
import com.liferay.friendly.url.model.FriendlyURLEntryMapping;
import com.liferay.friendly.url.service.base.FriendlyURLEntryLocalServiceBaseImpl;
import com.liferay.friendly.url.util.comparator.FriendlyURLEntryCreateDateComparator;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ModelHintsUtil;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.FriendlyURLNormalizer;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	property = "model.class.name=com.liferay.friendly.url.model.FriendlyURLEntry",
	service = AopService.class
)
public class FriendlyURLEntryLocalServiceImpl
	extends FriendlyURLEntryLocalServiceBaseImpl {

	@Override
	public FriendlyURLEntry addFriendlyURLEntry(
			long groupId, Class<?> clazz, long classPK, String urlTitle,
			ServiceContext serviceContext)
		throws PortalException {

		return addFriendlyURLEntry(
			groupId, classNameLocalService.getClassNameId(clazz), classPK,
			urlTitle, serviceContext);
	}

	@Override
	public FriendlyURLEntry addFriendlyURLEntry(
			long groupId, long classNameId, long classPK,
			Map<String, String> urlTitleMap, ServiceContext serviceContext)
		throws PortalException {

		String defaultLanguageId = LocaleUtil.toLanguageId(
			LocaleUtil.getSiteDefault());

		return addFriendlyURLEntry(
			groupId, classNameId, classPK, defaultLanguageId, urlTitleMap,
			serviceContext);
	}

	@Override
	public FriendlyURLEntry addFriendlyURLEntry(
			long groupId, long classNameId, long classPK,
			String defaultLanguageId, Map<String, String> urlTitleMap,
			ServiceContext serviceContext)
		throws PortalException {

		validate(groupId, classNameId, classPK, urlTitleMap);

		FriendlyURLEntryMapping friendlyURLEntryMapping =
			friendlyURLEntryMappingPersistence.fetchByC_C(classNameId, classPK);

		if (friendlyURLEntryMapping == null) {
			long friendlyURLMappingId = counterLocalService.increment();

			friendlyURLEntryMapping = friendlyURLEntryMappingPersistence.create(
				friendlyURLMappingId);

			friendlyURLEntryMapping.setClassNameId(classNameId);
			friendlyURLEntryMapping.setClassPK(classPK);
		}

		Map<String, String> existingUrlTitleMap = _getURLTitleMap(
			friendlyURLEntryMapping);

		FriendlyURLEntry friendlyURLEntry =
			friendlyURLEntryPersistence.fetchByPrimaryKey(
				friendlyURLEntryMapping.getFriendlyURLEntryId());

		if ((friendlyURLEntry != null) &&
			_containsAllURLTitles(existingUrlTitleMap, urlTitleMap)) {

			return friendlyURLEntry;
		}

		long friendlyURLEntryId = counterLocalService.increment();

		friendlyURLEntry = friendlyURLEntryPersistence.create(
			friendlyURLEntryId);

		friendlyURLEntry.setUuid(serviceContext.getUuid());

		Group group = _groupLocalService.getGroup(groupId);

		friendlyURLEntry.setCompanyId(group.getCompanyId());

		friendlyURLEntry.setGroupId(groupId);
		friendlyURLEntry.setClassNameId(classNameId);
		friendlyURLEntry.setClassPK(classPK);
		friendlyURLEntry.setDefaultLanguageId(defaultLanguageId);

		if (!ExportImportThreadLocal.isImportInProcess()) {
			friendlyURLEntryMapping.setFriendlyURLEntryId(friendlyURLEntryId);

			friendlyURLEntryMappingPersistence.update(friendlyURLEntryMapping);
		}

		friendlyURLEntry = friendlyURLEntryPersistence.update(friendlyURLEntry);

		_updateFriendlyURLEntryLocalizations(
			friendlyURLEntry, classNameId, classPK,
			_merge(urlTitleMap, existingUrlTitleMap));

		return friendlyURLEntry;
	}

	@Override
	public FriendlyURLEntry addFriendlyURLEntry(
			long groupId, long classNameId, long classPK, String urlTitle,
			ServiceContext serviceContext)
		throws PortalException {

		String defaultLanguageId = LocaleUtil.toLanguageId(
			LocaleUtil.getSiteDefault());

		return addFriendlyURLEntry(
			groupId, classNameId, classPK, defaultLanguageId,
			Collections.singletonMap(defaultLanguageId, urlTitle),
			serviceContext);
	}

	@Override
	public FriendlyURLEntry deleteFriendlyURLEntry(
		FriendlyURLEntry friendlyURLEntry) {

		FriendlyURLEntry deletedFriendlyURLEntry =
			friendlyURLEntryPersistence.remove(friendlyURLEntry);

		FriendlyURLEntryMapping friendlyURLEntryMapping =
			friendlyURLEntryMappingPersistence.fetchByC_C(
				friendlyURLEntry.getClassNameId(),
				friendlyURLEntry.getClassPK());

		if ((friendlyURLEntryMapping != null) &&
			(friendlyURLEntryMapping.getFriendlyURLEntryId() ==
				friendlyURLEntry.getFriendlyURLEntryId())) {

			friendlyURLEntry = friendlyURLEntryPersistence.fetchByG_C_C_Last(
				friendlyURLEntry.getGroupId(),
				friendlyURLEntry.getClassNameId(),
				friendlyURLEntry.getClassPK(),
				new FriendlyURLEntryCreateDateComparator());

			if (friendlyURLEntry == null) {
				friendlyURLEntryMappingPersistence.remove(
					friendlyURLEntryMapping);
			}
			else {
				friendlyURLEntryMapping.setFriendlyURLEntryId(
					friendlyURLEntry.getFriendlyURLEntryId());

				friendlyURLEntryMappingPersistence.update(
					friendlyURLEntryMapping);
			}
		}

		return deletedFriendlyURLEntry;
	}

	@Override
	public FriendlyURLEntry deleteFriendlyURLEntry(long friendlyURLEntryId)
		throws PortalException {

		return deleteFriendlyURLEntry(
			friendlyURLEntryPersistence.findByPrimaryKey(friendlyURLEntryId));
	}

	@Override
	public void deleteFriendlyURLEntry(
		long groupId, Class<?> clazz, long classPK) {

		deleteFriendlyURLEntry(
			groupId, classNameLocalService.getClassNameId(clazz), classPK);
	}

	@Override
	public void deleteFriendlyURLEntry(
		long groupId, long classNameId, long classPK) {

		FriendlyURLEntryMapping friendlyURLEntryMapping =
			friendlyURLEntryMappingPersistence.fetchByC_C(classNameId, classPK);

		if (friendlyURLEntryMapping == null) {
			return;
		}

		List<FriendlyURLEntry> friendlyURLEntries =
			friendlyURLEntryPersistence.findByG_C_C(
				groupId, classNameId, classPK);

		for (FriendlyURLEntry friendlyURLEntry : friendlyURLEntries) {
			friendlyURLEntryPersistence.remove(friendlyURLEntry);
		}

		friendlyURLEntryMappingPersistence.remove(friendlyURLEntryMapping);
	}

	@Override
	public void deleteFriendlyURLLocalizationEntry(
			long friendlyURLEntryId, String languageId)
		throws PortalException {

		friendlyURLEntryLocalizationPersistence.
			removeByFriendlyURLEntryId_LanguageId(
				friendlyURLEntryId, languageId);
	}

	@Override
	public void deleteGroupFriendlyURLEntries(long groupId, long classNameId) {
		ActionableDynamicQuery actionableDynamicQuery =
			getActionableDynamicQuery();

		actionableDynamicQuery.setAddCriteriaMethod(
			dynamicQuery -> {
				Property property = PropertyFactoryUtil.forName("classNameId");

				dynamicQuery.add(property.eq(classNameId));
			});
		actionableDynamicQuery.setGroupId(groupId);
		actionableDynamicQuery.setPerformActionMethod(
			(FriendlyURLEntry friendlyURLEntry) -> {
				friendlyURLEntryLocalizationPersistence.
					removeByFriendlyURLEntryId(
						friendlyURLEntry.getFriendlyURLEntryId());

				friendlyURLEntryPersistence.remove(friendlyURLEntry);

				FriendlyURLEntryMapping friendlyURLEntryMapping =
					friendlyURLEntryMappingPersistence.fetchByC_C(
						classNameId, friendlyURLEntry.getClassPK());

				if ((friendlyURLEntryMapping != null) &&
					(friendlyURLEntryMapping.getFriendlyURLEntryId() ==
						friendlyURLEntry.getFriendlyURLEntryId())) {

					friendlyURLEntryMappingPersistence.remove(
						friendlyURLEntryMapping);
				}
			});

		try {
			actionableDynamicQuery.performActions();
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	@Override
	public FriendlyURLEntry fetchFriendlyURLEntry(
		long groupId, Class<?> clazz, String urlTitle) {

		return fetchFriendlyURLEntry(
			groupId, classNameLocalService.getClassNameId(clazz), urlTitle);
	}

	@Override
	public FriendlyURLEntry fetchFriendlyURLEntry(
		long groupId, long classNameId, String urlTitle) {

		FriendlyURLEntryLocalization friendlyURLEntryLocalization =
			friendlyURLEntryLocalizationPersistence.fetchByG_C_U(
				groupId, classNameId,
				_friendlyURLNormalizer.normalizeWithEncoding(urlTitle));

		if (friendlyURLEntryLocalization != null) {
			return friendlyURLEntryPersistence.fetchByPrimaryKey(
				friendlyURLEntryLocalization.getFriendlyURLEntryId());
		}

		return null;
	}

	@Override
	public FriendlyURLEntryLocalization fetchFriendlyURLEntryLocalization(
		long groupId, long classNameId, String urlTitle) {

		return friendlyURLEntryLocalizationPersistence.fetchByG_C_U(
			groupId, classNameId,
			_friendlyURLNormalizer.normalizeWithEncoding(urlTitle));
	}

	@Override
	public FriendlyURLEntry fetchMainFriendlyURLEntry(
		long classNameId, long classPK) {

		FriendlyURLEntryMapping friendlyURLEntryMapping =
			friendlyURLEntryMappingPersistence.fetchByC_C(classNameId, classPK);

		if (friendlyURLEntryMapping == null) {
			return null;
		}

		return friendlyURLEntryPersistence.fetchByPrimaryKey(
			friendlyURLEntryMapping.getFriendlyURLEntryId());
	}

	@Override
	public List<FriendlyURLEntry> getFriendlyURLEntries(
		long groupId, long classNameId, long classPK) {

		return friendlyURLEntryPersistence.findByG_C_C(
			groupId, classNameId, classPK);
	}

	@Override
	public FriendlyURLEntryLocalization getFriendlyURLEntryLocalization(
			long groupId, long classNameId, String urlTitle)
		throws NoSuchFriendlyURLEntryLocalizationException {

		return friendlyURLEntryLocalizationPersistence.findByG_C_U(
			groupId, classNameId,
			_friendlyURLNormalizer.normalizeWithEncoding(urlTitle));
	}

	@Override
	public List<FriendlyURLEntryLocalization> getFriendlyURLEntryLocalizations(
		long groupId, long classNameId, long classPK, String languageId,
		int start, int end,
		OrderByComparator<FriendlyURLEntryLocalization> orderByComparator) {

		return friendlyURLEntryLocalizationPersistence.findByG_C_C_L(
			groupId, classNameId, classPK, languageId, start, end,
			orderByComparator);
	}

	@Override
	public FriendlyURLEntry getMainFriendlyURLEntry(
			Class<?> clazz, long classPK)
		throws PortalException {

		return getMainFriendlyURLEntry(
			classNameLocalService.getClassNameId(clazz), classPK);
	}

	@Override
	public FriendlyURLEntry getMainFriendlyURLEntry(
			long classNameId, long classPK)
		throws PortalException {

		FriendlyURLEntryMapping friendlyURLEntryMapping =
			friendlyURLEntryMappingPersistence.findByC_C(classNameId, classPK);

		return friendlyURLEntryPersistence.findByPrimaryKey(
			friendlyURLEntryMapping.getFriendlyURLEntryId());
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getUniqueUrlTitle(long, long, long, String, String)}
	 */
	@Deprecated
	@Override
	public String getUniqueUrlTitle(
		long groupId, long classNameId, long classPK, String urlTitle) {

		return getUniqueUrlTitle(groupId, classNameId, classPK, urlTitle, null);
	}

	@Override
	public String getUniqueUrlTitle(
		long groupId, long classNameId, long classPK, String urlTitle,
		String languageId) {

		if (urlTitle.startsWith(StringPool.SLASH)) {
			urlTitle = urlTitle.replaceAll("^/+", StringPool.SLASH);
		}

		String normalizedUrlTitle =
			_friendlyURLNormalizer.normalizeWithEncoding(urlTitle);

		int maxLength = ModelHintsUtil.getMaxLength(
			FriendlyURLEntryLocalization.class.getName(), "urlTitle");

		String curUrlTitle = _getURLEncodedSubstring(
			urlTitle, normalizedUrlTitle, maxLength);

		String prefix = curUrlTitle;

		for (int i = 1;; i++) {
			FriendlyURLEntryLocalization friendlyURLEntryLocalization =
				friendlyURLEntryLocalizationPersistence.fetchByG_C_U(
					groupId, classNameId, curUrlTitle);

			if (Validator.isNull(languageId)) {
				if ((friendlyURLEntryLocalization == null) ||
					(friendlyURLEntryLocalization.getClassPK() == classPK)) {

					break;
				}
			}
			else {
				if ((friendlyURLEntryLocalization == null) ||
					((friendlyURLEntryLocalization.getClassPK() == classPK) &&
					 languageId.equals(
						 friendlyURLEntryLocalization.getLanguageId()))) {

					break;
				}
			}

			String suffix = StringPool.DASH + i;

			prefix = _getURLEncodedSubstring(
				urlTitle, prefix, maxLength - suffix.length());

			curUrlTitle = _friendlyURLNormalizer.normalizeWithEncoding(
				prefix + suffix);
		}

		return curUrlTitle;
	}

	@Override
	public void setMainFriendlyURLEntry(FriendlyURLEntry friendlyURLEntry) {
		FriendlyURLEntryMapping friendlyURLEntryMapping =
			friendlyURLEntryMappingPersistence.fetchByC_C(
				friendlyURLEntry.getClassNameId(),
				friendlyURLEntry.getClassPK());

		if (friendlyURLEntryMapping == null) {
			long friendlyURLEntryMappingId = counterLocalService.increment();

			friendlyURLEntryMapping = friendlyURLEntryMappingPersistence.create(
				friendlyURLEntryMappingId);

			friendlyURLEntryMapping.setClassNameId(
				friendlyURLEntry.getClassNameId());
			friendlyURLEntryMapping.setClassPK(friendlyURLEntry.getClassPK());
		}

		friendlyURLEntryMapping.setFriendlyURLEntryId(
			friendlyURLEntry.getFriendlyURLEntryId());

		friendlyURLEntryMappingPersistence.update(friendlyURLEntryMapping);
	}

	@Override
	public FriendlyURLEntry updateFriendlyURLEntry(
			long friendlyURLEntryId, long classNameId, long classPK,
			String defaultLanguageId, Map<String, String> urlTitleMap)
		throws PortalException {

		FriendlyURLEntry friendlyURLEntry =
			friendlyURLEntryPersistence.findByPrimaryKey(friendlyURLEntryId);

		validate(
			friendlyURLEntry.getGroupId(), classNameId, classPK, urlTitleMap);

		friendlyURLEntry.setClassNameId(classNameId);
		friendlyURLEntry.setClassPK(classPK);
		friendlyURLEntry.setDefaultLanguageId(defaultLanguageId);

		friendlyURLEntry = friendlyURLEntryPersistence.update(friendlyURLEntry);

		_updateFriendlyURLEntryLocalizations(
			friendlyURLEntry, classNameId, classPK, urlTitleMap);

		return friendlyURLEntry;
	}

	@Override
	public FriendlyURLEntryLocalization updateFriendlyURLLocalization(
		FriendlyURLEntryLocalization friendlyURLEntryLocalization) {

		return friendlyURLEntryLocalizationPersistence.update(
			friendlyURLEntryLocalization);
	}

	@Override
	public FriendlyURLEntryLocalization updateFriendlyURLLocalization(
			long friendlyURLLocalizationId, String urlTitle)
		throws PortalException {

		FriendlyURLEntryLocalization friendlyURLEntryLocalization =
			friendlyURLEntryLocalizationPersistence.fetchByPrimaryKey(
				friendlyURLLocalizationId);

		if (friendlyURLEntryLocalization != null) {
			friendlyURLEntryLocalization.setUrlTitle(urlTitle);

			return friendlyURLEntryLocalizationPersistence.update(
				friendlyURLEntryLocalization);
		}

		return null;
	}

	@Override
	public void validate(
			long groupId, long classNameId, long classPK,
			Map<String, String> urlTitleMap)
		throws PortalException {

		for (String urlTitle : urlTitleMap.values()) {
			validate(groupId, classNameId, classPK, urlTitle);
		}
	}

	@Override
	public void validate(
			long groupId, long classNameId, long classPK, String urlTitle)
		throws PortalException {

		int maxLength = ModelHintsUtil.getMaxLength(
			FriendlyURLEntryLocalization.class.getName(), "urlTitle");

		String normalizedUrlTitle =
			_friendlyURLNormalizer.normalizeWithEncoding(urlTitle);

		if (normalizedUrlTitle.length() > maxLength) {
			throw new FriendlyURLLengthException(
				urlTitle + " is longer than " + maxLength);
		}

		FriendlyURLEntryLocalization friendlyURLEntryLocalization =
			friendlyURLEntryLocalizationPersistence.fetchByG_C_U(
				groupId, classNameId, normalizedUrlTitle);

		if (friendlyURLEntryLocalization == null) {
			return;
		}

		if ((classPK <= 0) ||
			(friendlyURLEntryLocalization.getClassPK() != classPK)) {

			throw new DuplicateFriendlyURLEntryException(
				friendlyURLEntryLocalization.toString());
		}
	}

	@Override
	public void validate(long groupId, long classNameId, String urlTitle)
		throws PortalException {

		validate(groupId, classNameId, 0, urlTitle);
	}

	private boolean _containsAllURLTitles(
		Map<String, String> existingUrlTitleMap,
		Map<String, String> urlTitleMap) {

		for (Map.Entry<String, String> entry : urlTitleMap.entrySet()) {
			String urlTitle = _friendlyURLNormalizer.normalizeWithEncoding(
				entry.getValue());

			if (!urlTitle.equals(existingUrlTitleMap.get(entry.getKey()))) {
				return false;
			}
		}

		return true;
	}

	private String _getURLEncodedSubstring(
		String decodedString, String encodedString, int maxLength) {

		int endPos = decodedString.length();

		while (encodedString.length() > maxLength) {
			endPos--;

			if ((endPos > 0) &&
				Character.isHighSurrogate(decodedString.charAt(endPos - 1))) {

				endPos--;
			}

			encodedString = _friendlyURLNormalizer.normalizeWithEncoding(
				decodedString.substring(0, endPos));
		}

		return encodedString;
	}

	private Map<String, String> _getURLTitleMap(
		FriendlyURLEntryMapping friendlyURLEntryMapping) {

		Map<String, String> urlTitleMap = new HashMap<>();

		for (FriendlyURLEntryLocalization friendlyURLEntryLocalization :
				friendlyURLEntryLocalizationPersistence.
					findByFriendlyURLEntryId(
						friendlyURLEntryMapping.getFriendlyURLEntryId())) {

			urlTitleMap.put(
				friendlyURLEntryLocalization.getLanguageId(),
				friendlyURLEntryLocalization.getUrlTitle());
		}

		return urlTitleMap;
	}

	private Map<String, String> _merge(
		Map<String, String> masterMap, Map<String, String> copyMap) {

		Map<String, String> map = new HashMap<>(copyMap);

		MapUtil.merge(masterMap, map);

		return map;
	}

	private Map<String, String> _sortUrlTitleMap(
		long groupId, Map<String, String> urlTitleMap) {

		Map<String, String> sortedUrlTitleMap = new LinkedHashMap<>();

		for (Locale locale : LanguageUtil.getAvailableLocales(groupId)) {
			String languageId = LocaleUtil.toLanguageId(locale);

			String value = urlTitleMap.get(languageId);

			if (value == null) {
				continue;
			}

			sortedUrlTitleMap.put(languageId, value);
		}

		return sortedUrlTitleMap;
	}

	private void _updateFriendlyURLEntryLocalizations(
			FriendlyURLEntry friendlyURLEntry, long classNameId, long classPK,
			Map<String, String> urlTitleMap)
		throws PortalException {

		urlTitleMap = _sortUrlTitleMap(
			friendlyURLEntry.getGroupId(), urlTitleMap);

		for (Map.Entry<String, String> entry : urlTitleMap.entrySet()) {
			String oldURLTitle = entry.getValue();

			String normalizedUrlTitle =
				_friendlyURLNormalizer.normalizeWithEncoding(oldURLTitle);

			if (Validator.isNotNull(normalizedUrlTitle)) {
				FriendlyURLEntryLocalization
					existingFriendlyURLEntryLocalization =
						friendlyURLEntryLocalizationPersistence.fetchByG_C_U(
							friendlyURLEntry.getGroupId(), classNameId,
							normalizedUrlTitle);

				if (existingFriendlyURLEntryLocalization != null) {
					String existingLanguageId =
						existingFriendlyURLEntryLocalization.getLanguageId();

					if ((existingFriendlyURLEntryLocalization.getClassPK() ==
							classPK) &&
						existingLanguageId.equals(entry.getKey())) {

						String existingUrlTitle =
							existingFriendlyURLEntryLocalization.getUrlTitle();

						if (existingUrlTitle.equals(oldURLTitle)) {
							existingFriendlyURLEntryLocalization.
								setFriendlyURLEntryId(
									friendlyURLEntry.getFriendlyURLEntryId());

							updateFriendlyURLLocalization(
								existingFriendlyURLEntryLocalization);

							continue;
						}
					}
					else {
						normalizedUrlTitle = getUniqueUrlTitle(
							friendlyURLEntry.getGroupId(), classNameId, classPK,
							normalizedUrlTitle, entry.getKey());
					}
				}

				updateFriendlyURLEntryLocalization(
					friendlyURLEntry, entry.getKey(), normalizedUrlTitle);
			}
			else if ((normalizedUrlTitle != null) &&
					 normalizedUrlTitle.equals(StringPool.BLANK)) {

				String defaultLanguageId =
					friendlyURLEntry.getDefaultLanguageId();

				if (!defaultLanguageId.equals(entry.getKey())) {
					FriendlyURLEntryLocalization friendlyURLEntryLocalization =
						friendlyURLEntryLocalizationPersistence.
							fetchByFriendlyURLEntryId_LanguageId(
								friendlyURLEntry.getFriendlyURLEntryId(),
								entry.getKey());

					if (friendlyURLEntryLocalization != null) {
						deleteFriendlyURLLocalizationEntry(
							friendlyURLEntry.getFriendlyURLEntryId(),
							entry.getKey());
					}
				}
			}
		}
	}

	@Reference
	private FriendlyURLNormalizer _friendlyURLNormalizer;

	@Reference
	private GroupLocalService _groupLocalService;

}