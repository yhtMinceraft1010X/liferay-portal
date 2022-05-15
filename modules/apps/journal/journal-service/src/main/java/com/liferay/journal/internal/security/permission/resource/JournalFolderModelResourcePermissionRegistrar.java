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

package com.liferay.journal.internal.security.permission.resource;

import com.liferay.exportimport.kernel.staging.permission.StagingPermission;
import com.liferay.journal.constants.JournalConstants;
import com.liferay.journal.constants.JournalFolderConstants;
import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalFolderLocalService;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.DynamicInheritancePermissionLogic;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.StagedModelPermissionLogic;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.util.PropsValues;

import java.util.Dictionary;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Preston Crary
 */
@Component(immediate = true, service = {})
public class JournalFolderModelResourcePermissionRegistrar {

	@Activate
	protected void activate(BundleContext bundleContext) {
		Dictionary<String, Object> properties =
			HashMapDictionaryBuilder.<String, Object>put(
				"model.class.name", JournalFolder.class.getName()
			).build();

		_serviceRegistration = bundleContext.registerService(
			(Class<ModelResourcePermission<JournalFolder>>)
				(Class<?>)ModelResourcePermission.class,
			ModelResourcePermissionFactory.create(
				JournalFolder.class, JournalFolder::getFolderId,
				_journalFolderLocalService::getFolder,
				_portletResourcePermission,
				(modelResourcePermission, consumer) -> {
					consumer.accept(
						new StagedModelPermissionLogic<JournalFolder>(
							_stagingPermission, JournalPortletKeys.JOURNAL,
							JournalFolder::getFolderId) {

							@Override
							public Boolean contains(
								PermissionChecker permissionChecker,
								String name, JournalFolder journalFolder,
								String actionId) {

								if (actionId.equals(ActionKeys.SUBSCRIBE)) {
									return null;
								}

								return super.contains(
									permissionChecker, name, journalFolder,
									actionId);
							}

						});

					if (PropsValues.PERMISSIONS_VIEW_DYNAMIC_INHERITANCE) {
						consumer.accept(
							new DynamicInheritancePermissionLogic<>(
								modelResourcePermission,
								_getFetchParentFunction(), false));
					}
				},
				actionId -> {
					if (ActionKeys.ADD_FOLDER.equals(actionId)) {
						return ActionKeys.ADD_SUBFOLDER;
					}

					return actionId;
				}),
			properties);
	}

	@Deactivate
	protected void deactivate() {
		_serviceRegistration.unregister();
	}

	private UnsafeFunction<JournalFolder, JournalFolder, PortalException>
		_getFetchParentFunction() {

		return folder -> {
			long folderId = folder.getParentFolderId();

			if (JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID == folderId) {
				return null;
			}

			if (folder.isInTrash()) {
				return _journalFolderLocalService.fetchJournalFolder(folderId);
			}

			return _journalFolderLocalService.getFolder(folderId);
		};
	}

	@Reference
	private JournalFolderLocalService _journalFolderLocalService;

	@Reference(
		target = "(resource.name=" + JournalConstants.RESOURCE_NAME + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

	private ServiceRegistration<ModelResourcePermission<JournalFolder>>
		_serviceRegistration;

	@Reference
	private StagingPermission _stagingPermission;

}