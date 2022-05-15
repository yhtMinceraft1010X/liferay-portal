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

package com.liferay.site.navigation.menu.item.layout.internal.model.listener;

import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.petra.string.CharPool;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.site.navigation.menu.item.layout.constants.SiteNavigationMenuItemTypeConstants;
import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.model.SiteNavigationMenuItem;
import com.liferay.site.navigation.service.SiteNavigationMenuItemLocalService;
import com.liferay.site.navigation.service.SiteNavigationMenuLocalService;
import com.liferay.site.navigation.type.SiteNavigationMenuItemType;
import com.liferay.site.navigation.type.SiteNavigationMenuItemTypeRegistry;

import java.util.List;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(immediate = true, service = ModelListener.class)
public class LayoutModelListener extends BaseModelListener<Layout> {

	@Override
	public void onAfterCreate(Layout layout) throws ModelListenerException {
		if (ExportImportThreadLocal.isStagingInProcess() ||
			!_isVisible(layout, false)) {

			return;
		}

		long[] siteNavigationMenuIds = GetterUtil.getLongValues(
			StringUtil.split(
				layout.getTypeSettingsProperty("siteNavigationMenuId"),
				CharPool.COMMA));

		_addLayoutSiteNavigationMenuItems(siteNavigationMenuIds, layout);
	}

	@Override
	public void onAfterRemove(Layout layout) throws ModelListenerException {
		if (layout == null) {
			return;
		}

		List<SiteNavigationMenu> siteNavigationMenus =
			_siteNavigationMenuLocalService.getSiteNavigationMenus(
				layout.getGroupId());

		try {
			for (SiteNavigationMenu siteNavigationMenu : siteNavigationMenus) {
				_deleteSiteNavigationMenuItem(siteNavigationMenu, layout);
			}
		}
		catch (PortalException portalException) {
			throw new ModelListenerException(portalException);
		}
	}

	@Override
	public void onAfterUpdate(Layout originalLayout, Layout layout)
		throws ModelListenerException {

		if (!_isVisible(layout, true)) {
			return;
		}

		long[] siteNavigationMenuIds = GetterUtil.getLongValues(
			StringUtil.split(
				layout.getTypeSettingsProperty("siteNavigationMenuId"),
				CharPool.COMMA));

		_addLayoutSiteNavigationMenuItems(siteNavigationMenuIds, layout);
	}

	private void _addLayoutSiteNavigationMenuItems(
		long[] siteNavigationMenuIds, Layout layout) {

		for (long siteNavigationMenuId : siteNavigationMenuIds) {
			if (siteNavigationMenuId > 0) {
				_addSiteNavigationMenuItem(siteNavigationMenuId, layout);
			}
		}

		if (Validator.isNotNull(
				layout.getTypeSettingsProperty("siteNavigationMenuId"))) {

			UnicodeProperties unicodeProperties =
				layout.getTypeSettingsProperties();

			unicodeProperties.remove("siteNavigationMenuId");

			try {
				_layoutLocalService.updateLayout(
					layout.getGroupId(), layout.isPrivateLayout(),
					layout.getLayoutId(), unicodeProperties.toString());

				Layout draftLayout = layout.fetchDraftLayout();

				if ((draftLayout != null) &&
					Validator.isNotNull(
						draftLayout.getTypeSettingsProperty(
							"siteNavigationMenuId"))) {

					unicodeProperties = draftLayout.getTypeSettingsProperties();

					unicodeProperties.remove("siteNavigationMenuId");

					_layoutLocalService.updateLayout(
						draftLayout.getGroupId(), draftLayout.isPrivateLayout(),
						draftLayout.getLayoutId(),
						unicodeProperties.toString());
				}
			}
			catch (PortalException portalException) {
				throw new ModelListenerException(portalException);
			}
		}
	}

	private void _addSiteNavigationMenuItem(
		long siteNavigationMenuId, Layout layout) {

		if (ExportImportThreadLocal.isImportInProcess() ||
			ExportImportThreadLocal.isStagingInProcess() ||
			_menuItemExists(siteNavigationMenuId, layout)) {

			return;
		}

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.LAYOUT);

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		try {
			long parentSiteNavigationMenuItemId =
				_getParentSiteNavigationMenuItemId(
					layout.getParentPlid(), siteNavigationMenuId);

			_siteNavigationMenuItemLocalService.addSiteNavigationMenuItem(
				serviceContext.getUserId(), layout.getGroupId(),
				siteNavigationMenuId, parentSiteNavigationMenuItemId,
				SiteNavigationMenuItemTypeConstants.LAYOUT,
				siteNavigationMenuItemType.getTypeSettingsFromLayout(layout),
				serviceContext);
		}
		catch (PortalException portalException) {
			throw new ModelListenerException(portalException);
		}
	}

	private void _deleteSiteNavigationMenuItem(
			SiteNavigationMenu siteNavigationMenu, Layout layout)
		throws PortalException {

		List<SiteNavigationMenuItem> siteNavigationMenuItems =
			_siteNavigationMenuItemLocalService.getSiteNavigationMenuItems(
				siteNavigationMenu.getSiteNavigationMenuId());

		for (SiteNavigationMenuItem siteNavigationMenuItem :
				siteNavigationMenuItems) {

			UnicodeProperties unicodeProperties =
				UnicodePropertiesBuilder.fastLoad(
					siteNavigationMenuItem.getTypeSettings()
				).build();

			String layoutUuid = unicodeProperties.getProperty("layoutUuid");

			if (Objects.equals(layout.getUuid(), layoutUuid)) {
				_siteNavigationMenuItemLocalService.
					deleteSiteNavigationMenuItem(
						siteNavigationMenuItem.getSiteNavigationMenuItemId());
			}
		}
	}

	private long _getParentSiteNavigationMenuItemId(
		long parentPlid, long siteNavigationMenuItemId) {

		if (parentPlid == LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {
			return 0;
		}

		Layout parentLayout = _layoutLocalService.fetchLayout(parentPlid);

		List<SiteNavigationMenuItem> siteNavigationMenuItems =
			_siteNavigationMenuItemLocalService.getSiteNavigationMenuItems(
				siteNavigationMenuItemId);

		for (SiteNavigationMenuItem siteNavigationMenuItem :
				siteNavigationMenuItems) {

			UnicodeProperties unicodeProperties =
				UnicodePropertiesBuilder.fastLoad(
					siteNavigationMenuItem.getTypeSettings()
				).build();

			String layoutUuid = unicodeProperties.getProperty("layoutUuid");

			if (Objects.equals(parentLayout.getUuid(), layoutUuid)) {
				return siteNavigationMenuItem.getSiteNavigationMenuItemId();
			}
		}

		return 0;
	}

	private boolean _isVisible(Layout layout, boolean update) {
		if (!layout.isTypeContent() && !update) {
			return true;
		}

		if (layout.isHidden() || layout.isSystem()) {
			return false;
		}

		return layout.isPublished();
	}

	private boolean _menuItemExists(long siteNavigationMenuId, Layout layout) {
		List<SiteNavigationMenuItem> siteNavigationMenuItems =
			_siteNavigationMenuItemLocalService.getSiteNavigationMenuItems(
				siteNavigationMenuId);

		for (SiteNavigationMenuItem siteNavigationMenuItem :
				siteNavigationMenuItems) {

			UnicodeProperties unicodeProperties =
				UnicodePropertiesBuilder.fastLoad(
					siteNavigationMenuItem.getTypeSettings()
				).build();

			String layoutUuid = unicodeProperties.getProperty("layoutUuid");

			if (Objects.equals(layout.getUuid(), layoutUuid)) {
				return true;
			}
		}

		return false;
	}

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private SiteNavigationMenuItemLocalService
		_siteNavigationMenuItemLocalService;

	@Reference
	private SiteNavigationMenuItemTypeRegistry
		_siteNavigationMenuItemTypeRegistry;

	@Reference
	private SiteNavigationMenuLocalService _siteNavigationMenuLocalService;

}