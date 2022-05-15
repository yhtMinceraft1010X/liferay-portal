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

package com.liferay.portal.service.impl;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.exception.LayoutFriendlyURLException;
import com.liferay.portal.kernel.exception.LayoutFriendlyURLsException;
import com.liferay.portal.kernel.exception.LayoutNameException;
import com.liferay.portal.kernel.exception.LayoutParentLayoutIdException;
import com.liferay.portal.kernel.exception.LayoutTypeException;
import com.liferay.portal.kernel.exception.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutFriendlyURL;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.LayoutSetPrototype;
import com.liferay.portal.kernel.model.LayoutType;
import com.liferay.portal.kernel.model.LayoutTypeController;
import com.liferay.portal.kernel.model.ModelHintsUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;
import com.liferay.portal.kernel.portlet.FriendlyURLResolverRegistryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.LayoutFriendlyURLEntryValidator;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutRevisionLocalService;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.persistence.LayoutFriendlyURLPersistence;
import com.liferay.portal.kernel.service.persistence.LayoutPersistence;
import com.liferay.portal.kernel.service.persistence.LayoutSetPersistence;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ServiceProxyFactory;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.comparator.LayoutPriorityComparator;
import com.liferay.portal.model.impl.LayoutImpl;
import com.liferay.portal.util.LayoutTypeControllerTracker;
import com.liferay.sites.kernel.util.SitesUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Raymond Augé
 */
public class LayoutLocalServiceHelper implements IdentifiableOSGiService {

	public String getFriendlyURL(
			long groupId, boolean privateLayout, long layoutId, String name,
			String friendlyURL)
		throws PortalException {

		return getFriendlyURL(
			groupId, privateLayout, layoutId, name, friendlyURL, null);
	}

	public String getFriendlyURL(
			long groupId, boolean privateLayout, long layoutId, String name,
			String friendlyURL, String languageId)
		throws PortalException {

		friendlyURL = getFriendlyURL(friendlyURL);

		if (Validator.isNotNull(friendlyURL)) {
			return friendlyURL;
		}

		friendlyURL = StringPool.SLASH + getFriendlyURL(name);

		String originalFriendlyURL = friendlyURL;

		Layout layout = LayoutLocalServiceUtil.fetchLayout(
			groupId, privateLayout, layoutId);

		for (int i = 1;; i++) {
			try {
				validateFriendlyURL(
					groupId, privateLayout, layoutId, friendlyURL, languageId);

				if (_layoutFriendlyURLEntryValidator != null) {
					long classPK = 0;

					if (layout != null) {
						classPK = layout.getPlid();
					}

					_layoutFriendlyURLEntryValidator.validateFriendlyURLEntry(
						groupId, privateLayout, classPK, friendlyURL);
				}

				break;
			}
			catch (LayoutFriendlyURLException layoutFriendlyURLException) {
				int type = layoutFriendlyURLException.getType();

				if (type == LayoutFriendlyURLException.DUPLICATE) {
					friendlyURL = originalFriendlyURL + i;
				}
				else {
					friendlyURL = StringPool.SLASH + layoutId;

					break;
				}
			}
		}

		return friendlyURL;
	}

	public String getFriendlyURL(String friendlyURL) {
		return FriendlyURLNormalizerUtil.normalizeWithEncoding(friendlyURL);
	}

	public Map<Locale, String> getFriendlyURLMap(
			long groupId, boolean privateLayout, long layoutId, String name,
			Map<Locale, String> friendlyURLMap)
		throws PortalException {

		Map<Locale, String> newFriendlyURLMap = new HashMap<>();

		for (Locale locale : LanguageUtil.getAvailableLocales(groupId)) {
			String friendlyURL = friendlyURLMap.get(locale);

			if (Validator.isNotNull(friendlyURL)) {
				friendlyURL = getFriendlyURL(
					groupId, privateLayout, layoutId, name, friendlyURL,
					locale.toString());

				newFriendlyURLMap.put(locale, friendlyURL);
			}
		}

		Locale siteDefaultLocale = LocaleUtil.getSiteDefault();

		if (newFriendlyURLMap.isEmpty() ||
			Validator.isNull(newFriendlyURLMap.get(siteDefaultLocale))) {

			String friendlyURL = getFriendlyURL(
				groupId, privateLayout, layoutId, name, StringPool.BLANK);

			newFriendlyURLMap.put(siteDefaultLocale, friendlyURL);
		}

		return newFriendlyURLMap;
	}

	public int getNextPriority(
		long groupId, boolean privateLayout, long parentLayoutId,
		String sourcePrototypeLayoutUuid, int defaultPriority) {

		try {
			int priority = defaultPriority;

			if (priority < 0) {
				Layout layout = layoutPersistence.findByG_P_P_First(
					groupId, privateLayout, parentLayoutId,
					new LayoutPriorityComparator(false));

				priority = layout.getPriority() + 1;
			}

			if ((priority < _PRIORITY_BUFFER) &&
				Validator.isNull(sourcePrototypeLayoutUuid)) {

				LayoutSet layoutSet = layoutSetPersistence.fetchByG_P(
					groupId, privateLayout);

				if (Validator.isNotNull(
						layoutSet.getLayoutSetPrototypeUuid()) &&
					layoutSet.isLayoutSetPrototypeLinkEnabled()) {

					priority = priority + _PRIORITY_BUFFER;
				}
			}

			return priority;
		}
		catch (NoSuchLayoutException noSuchLayoutException) {

			// LPS-52675

			if (_log.isDebugEnabled()) {
				_log.debug(noSuchLayoutException);
			}

			return 0;
		}
	}

	@Override
	public String getOSGiServiceIdentifier() {
		return LayoutLocalServiceHelper.class.getName();
	}

	public long getParentLayoutId(
		long groupId, boolean privateLayout, long parentLayoutId) {

		if (parentLayoutId != LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {

			// Ensure parent layout exists

			Layout parentLayout = layoutPersistence.fetchByG_P_L(
				groupId, privateLayout, parentLayoutId);

			if (parentLayout == null) {
				parentLayoutId = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;
			}
		}

		return parentLayoutId;
	}

	public long getUniquePlid() {
		long plid = counterLocalService.increment(Layout.class.getName());

		while (layoutRevisionLocalService.fetchLayoutRevision(plid) != null) {
			plid = counterLocalService.increment(Layout.class.getName());
		}

		return plid;
	}

	public boolean hasLayoutSetPrototypeLayout(
			LayoutSetPrototype layoutSetPrototype, String layoutUuid)
		throws PortalException {

		Layout layout = layoutPersistence.fetchByUUID_G_P(
			layoutUuid, layoutSetPrototype.getGroupId(), true);

		if (layout != null) {
			return true;
		}

		return false;
	}

	public void validate(
			long groupId, boolean privateLayout, long layoutId,
			long parentLayoutId, String name, String type, boolean hidden,
			Map<Locale, String> friendlyURLMap, ServiceContext serviceContext)
		throws PortalException {

		validateName(name);

		boolean firstLayout = false;

		if (parentLayoutId == LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {
			List<Layout> layouts = layoutPersistence.findByG_P_P(
				groupId, privateLayout, parentLayoutId, 0, 1);

			if (layouts.isEmpty()) {
				firstLayout = true;
			}
			else {
				Layout layout = layouts.get(0);

				long firstLayoutId = layout.getLayoutId();

				if (firstLayoutId == layoutId) {
					firstLayout = true;
				}
			}
		}
		else {

			// Layout cannot become a child of a layout that is not sortable
			// because it is linked to a layout set prototype

			Layout layout = layoutPersistence.fetchByG_P_L(
				groupId, privateLayout, layoutId);
			Layout parentLayout = layoutPersistence.findByG_P_L(
				groupId, privateLayout, parentLayoutId);

			if (((layout == null) ||
				 Validator.isNull(layout.getSourcePrototypeLayoutUuid())) &&
				!SitesUtil.isLayoutSortable(parentLayout)) {

				throw new LayoutParentLayoutIdException(
					LayoutParentLayoutIdException.NOT_SORTABLE);
			}
		}

		if (firstLayout) {
			validateFirstLayout(type);
		}

		LayoutTypeController layoutTypeController =
			LayoutTypeControllerTracker.getLayoutTypeController(type);

		if (!layoutTypeController.isInstanceable()) {
			boolean layoutInstanceableAllowed = GetterUtil.getBoolean(
				serviceContext.getAttribute("layout.instanceable.allowed"));

			if (!layoutInstanceableAllowed) {
				throw new LayoutTypeException(
					LayoutTypeException.NOT_INSTANCEABLE);
			}
		}

		if (!layoutTypeController.isParentable()) {
			int count = layoutPersistence.countByG_P_P(
				groupId, privateLayout, layoutId);

			if (count > 0) {
				throw new LayoutTypeException(
					LayoutTypeException.NOT_PARENTABLE);
			}
		}

		validateFriendlyURLs(groupId, privateLayout, layoutId, friendlyURLMap);
	}

	public void validateFirstLayout(Layout layout) throws PortalException {
		Group group = layout.getGroup();

		if (group.isGuest() && layout.isPublicLayout() &&
			!hasGuestViewPermission(layout)) {

			LayoutTypeException layoutTypeException = new LayoutTypeException(
				LayoutTypeException.FIRST_LAYOUT_PERMISSION);

			throw layoutTypeException;
		}

		validateFirstLayout(layout.getType());
	}

	public void validateFirstLayout(String type) throws PortalException {
		LayoutTypeController layoutTypeController =
			LayoutTypeControllerTracker.getLayoutTypeController(type);

		if (Validator.isNull(type) || !layoutTypeController.isFirstPageable()) {
			LayoutTypeException layoutTypeException = new LayoutTypeException(
				LayoutTypeException.FIRST_LAYOUT);

			layoutTypeException.setLayoutType(type);

			throw layoutTypeException;
		}
	}

	public void validateFriendlyURL(
			long groupId, boolean privateLayout, long layoutId,
			String friendlyURL)
		throws PortalException {

		validateFriendlyURL(
			groupId, privateLayout, layoutId, friendlyURL, null);
	}

	public void validateFriendlyURL(
			long groupId, boolean privateLayout, long layoutId,
			String friendlyURL, String languageId)
		throws PortalException {

		if (Validator.isNull(friendlyURL)) {
			return;
		}

		int exceptionType = LayoutImpl.validateFriendlyURL(friendlyURL);

		if (exceptionType != -1) {
			throw new LayoutFriendlyURLException(exceptionType);
		}

		List<LayoutFriendlyURL> layoutFriendlyURLs =
			layoutFriendlyURLPersistence.findByG_P_F(
				groupId, privateLayout, friendlyURL);

		for (LayoutFriendlyURL layoutFriendlyURL : layoutFriendlyURLs) {
			Layout layout = layoutPersistence.findByPrimaryKey(
				layoutFriendlyURL.getPlid());

			if ((layout.getLayoutId() != layoutId) ||
				(Validator.isNotNull(languageId) &&
				 !languageId.equals(layoutFriendlyURL.getLanguageId()))) {

				LayoutFriendlyURLException layoutFriendlyURLException =
					new LayoutFriendlyURLException(
						LayoutFriendlyURLException.DUPLICATE);

				layoutFriendlyURLException.setDuplicateClassPK(
					layout.getPlid());
				layoutFriendlyURLException.setDuplicateClassName(
					Layout.class.getName());

				throw layoutFriendlyURLException;
			}
		}

		LayoutImpl.validateFriendlyURLKeyword(friendlyURL);

		if (friendlyURL.contains(Portal.FRIENDLY_URL_SEPARATOR) ||
			friendlyURL.endsWith(_FRIENDLY_URL_SEPARATOR_HEAD)) {

			LayoutFriendlyURLException layoutFriendlyURLException =
				new LayoutFriendlyURLException(
					LayoutFriendlyURLException.KEYWORD_CONFLICT);

			layoutFriendlyURLException.setKeywordConflict(
				Portal.FRIENDLY_URL_SEPARATOR);

			throw layoutFriendlyURLException;
		}

		Matcher matcher = _urlSeparatorPattern.matcher(friendlyURL);

		if (matcher.matches()) {
			LayoutFriendlyURLException layoutFriendlyURLException =
				new LayoutFriendlyURLException(
					LayoutFriendlyURLException.KEYWORD_CONFLICT);

			layoutFriendlyURLException.setKeywordConflict(friendlyURL);

			throw layoutFriendlyURLException;
		}

		String[] urlSeparators =
			FriendlyURLResolverRegistryUtil.getURLSeparators();

		for (String urlSeparator : urlSeparators) {
			if (urlSeparator.contains(friendlyURL)) {
				LayoutFriendlyURLException layoutFriendlyURLException =
					new LayoutFriendlyURLException(
						LayoutFriendlyURLException.KEYWORD_CONFLICT);

				layoutFriendlyURLException.setKeywordConflict(urlSeparator);

				throw layoutFriendlyURLException;
			}
		}

		List<FriendlyURLMapper> friendlyURLMappers =
			PortletLocalServiceUtil.getFriendlyURLMappers();

		for (FriendlyURLMapper friendlyURLMapper : friendlyURLMappers) {
			if (friendlyURLMapper.isCheckMappingWithPrefix()) {
				continue;
			}

			String mapping = StringPool.SLASH + friendlyURLMapper.getMapping();

			if (friendlyURL.contains(mapping + StringPool.SLASH) ||
				friendlyURL.endsWith(mapping)) {

				LayoutFriendlyURLException layoutFriendlyURLException =
					new LayoutFriendlyURLException(
						LayoutFriendlyURLException.KEYWORD_CONFLICT);

				layoutFriendlyURLException.setKeywordConflict(
					friendlyURLMapper.getMapping());

				throw layoutFriendlyURLException;
			}
		}

		for (Locale locale : LanguageUtil.getAvailableLocales()) {
			languageId = StringUtil.toLowerCase(
				LocaleUtil.toLanguageId(locale));

			String i18nPathLanguageId =
				StringPool.SLASH +
					PortalUtil.getI18nPathLanguageId(locale, languageId);

			String underlineI18nPathLanguageId = StringUtil.replace(
				i18nPathLanguageId, CharPool.DASH, CharPool.UNDERLINE);

			if (friendlyURL.startsWith(i18nPathLanguageId + StringPool.SLASH) ||
				friendlyURL.startsWith(
					underlineI18nPathLanguageId + StringPool.SLASH) ||
				friendlyURL.startsWith(
					StringPool.SLASH + languageId + StringPool.SLASH) ||
				friendlyURL.equals(i18nPathLanguageId) ||
				friendlyURL.equals(underlineI18nPathLanguageId) ||
				friendlyURL.equals(StringPool.SLASH + languageId)) {

				LayoutFriendlyURLException layoutFriendlyURLException =
					new LayoutFriendlyURLException(
						LayoutFriendlyURLException.KEYWORD_CONFLICT);

				layoutFriendlyURLException.setKeywordConflict(
					i18nPathLanguageId);

				throw layoutFriendlyURLException;
			}
		}

		String layoutIdFriendlyURL = friendlyURL.substring(1);

		if (Validator.isNumber(layoutIdFriendlyURL) &&
			!layoutIdFriendlyURL.equals(String.valueOf(layoutId))) {

			LayoutFriendlyURLException layoutFriendlyURLException =
				new LayoutFriendlyURLException(
					LayoutFriendlyURLException.POSSIBLE_DUPLICATE);

			layoutFriendlyURLException.setKeywordConflict(layoutIdFriendlyURL);

			throw layoutFriendlyURLException;
		}
	}

	public void validateFriendlyURLs(
			long groupId, boolean privateLayout, long layoutId,
			Map<Locale, String> friendlyURLMap)
		throws PortalException {

		LayoutFriendlyURLsException layoutFriendlyURLsException = null;

		Set<String> friendlyURLs = new HashSet<>(friendlyURLMap.values());

		if (friendlyURLs.size() != friendlyURLMap.size()) {
			LayoutFriendlyURLException layoutFriendlyURLException =
				new LayoutFriendlyURLException(
					LayoutFriendlyURLException.DUPLICATE);

			layoutFriendlyURLsException = new LayoutFriendlyURLsException(
				layoutFriendlyURLException);
		}

		for (Map.Entry<Locale, String> entry : friendlyURLMap.entrySet()) {
			try {
				String friendlyURL = entry.getValue();
				Locale locale = entry.getKey();

				validateFriendlyURL(
					groupId, privateLayout, layoutId, friendlyURL,
					locale.toString());
			}
			catch (LayoutFriendlyURLException layoutFriendlyURLException) {
				Locale locale = entry.getKey();

				if (layoutFriendlyURLsException == null) {
					layoutFriendlyURLsException =
						new LayoutFriendlyURLsException(
							layoutFriendlyURLException);
				}
				else {
					layoutFriendlyURLsException.addSuppressed(
						layoutFriendlyURLException);
				}

				layoutFriendlyURLsException.addLocalizedException(
					locale, layoutFriendlyURLException);
			}
		}

		if (layoutFriendlyURLsException != null) {
			throw layoutFriendlyURLsException;
		}
	}

	public void validateName(String name) throws PortalException {
		if (Validator.isNull(name)) {
			throw new LayoutNameException();
		}

		int maxLength = ModelHintsUtil.getMaxLength(
			Layout.class.getName(), "friendlyURL");

		if (name.length() > maxLength) {
			throw new LayoutNameException(LayoutNameException.TOO_LONG);
		}
	}

	public void validateName(String name, String languageId)
		throws PortalException {

		String defaultLanguageId = LocaleUtil.toLanguageId(
			LocaleUtil.getSiteDefault());

		if (defaultLanguageId.equals(languageId)) {
			validateName(name);
		}
	}

	public void validateParentLayoutId(
			long groupId, boolean privateLayout, long layoutId,
			long parentLayoutId)
		throws PortalException {

		Layout layout = layoutPersistence.findByG_P_L(
			groupId, privateLayout, layoutId);

		if (parentLayoutId == layout.getParentLayoutId()) {
			return;
		}

		// Layouts can always be moved to the root level

		if (parentLayoutId == LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {
			return;
		}

		// Layout cannot become a child of a layout that is not parentable

		Layout parentLayout = layoutPersistence.findByG_P_L(
			groupId, privateLayout, parentLayoutId);

		LayoutType parentLayoutType = parentLayout.getLayoutType();

		if (!parentLayoutType.isParentable()) {
			throw new LayoutParentLayoutIdException(
				LayoutParentLayoutIdException.NOT_PARENTABLE);
		}

		// Layout cannot become a child of a layout that is not sortable because
		// it is linked to a layout set prototype

		if (Validator.isNull(layout.getSourcePrototypeLayoutUuid()) &&
			!SitesUtil.isLayoutSortable(parentLayout)) {

			throw new LayoutParentLayoutIdException(
				LayoutParentLayoutIdException.NOT_SORTABLE);
		}

		// Layout cannot become descendant of itself

		if (PortalUtil.isLayoutDescendant(layout, parentLayoutId)) {
			throw new LayoutParentLayoutIdException(
				LayoutParentLayoutIdException.SELF_DESCENDANT);
		}

		// If layout is moved, the new first layout must be valid

		if (layout.getParentLayoutId() !=
				LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {

			return;
		}

		List<Layout> layouts = layoutPersistence.findByG_P_P(
			groupId, privateLayout, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, 0,
			2);

		// You can only reach this point if there are more than two layouts
		// at the root level because of the descendant check

		Layout firstLayout = layouts.get(0);

		long firstLayoutId = firstLayout.getLayoutId();

		if (firstLayoutId == layoutId) {
			Layout secondLayout = layouts.get(1);

			LayoutType layoutType = secondLayout.getLayoutType();

			if (Validator.isNull(secondLayout.getType()) ||
				!layoutType.isFirstPageable()) {

				throw new LayoutParentLayoutIdException(
					LayoutParentLayoutIdException.FIRST_LAYOUT_TYPE);
			}
		}
	}

	protected boolean hasGuestViewPermission(Layout layout)
		throws PortalException {

		Role role = RoleLocalServiceUtil.getRole(
			layout.getCompanyId(), RoleConstants.GUEST);

		return resourcePermissionLocalService.hasResourcePermission(
			layout.getCompanyId(), Layout.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			String.valueOf(layout.getPlid()), role.getRoleId(),
			ActionKeys.VIEW);
	}

	@BeanReference(type = CounterLocalService.class)
	protected CounterLocalService counterLocalService;

	@BeanReference(type = LayoutFriendlyURLPersistence.class)
	protected LayoutFriendlyURLPersistence layoutFriendlyURLPersistence;

	@BeanReference(type = LayoutPersistence.class)
	protected LayoutPersistence layoutPersistence;

	@BeanReference(type = LayoutRevisionLocalService.class)
	protected LayoutRevisionLocalService layoutRevisionLocalService;

	@BeanReference(type = LayoutSetPersistence.class)
	protected LayoutSetPersistence layoutSetPersistence;

	@BeanReference(type = ResourcePermissionLocalService.class)
	protected ResourcePermissionLocalService resourcePermissionLocalService;

	private static final String _FRIENDLY_URL_SEPARATOR_HEAD =
		Portal.FRIENDLY_URL_SEPARATOR.substring(
			0, Portal.FRIENDLY_URL_SEPARATOR.length() - 1);

	private static final int _PRIORITY_BUFFER = 1000000;

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutLocalServiceHelper.class);

	private static volatile LayoutFriendlyURLEntryValidator
		_layoutFriendlyURLEntryValidator =
			ServiceProxyFactory.newServiceTrackedInstance(
				LayoutFriendlyURLEntryValidator.class,
				LayoutLocalServiceHelper.class,
				"_layoutFriendlyURLEntryValidator", false, true);
	private static final Pattern _urlSeparatorPattern = Pattern.compile(
		"/[A-Za-z]");

}