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

package com.liferay.document.library.internal.exportimport.staged.model.repository;

import com.liferay.document.library.internal.helper.DLExportableRepositoryPublisherHelper;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataException;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.exportimport.staged.model.repository.StagedModelRepository;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.Folder;

import java.util.Collection;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Akos Thurzo
 */
@Component(
	property = "model.class.name=com.liferay.document.library.kernel.model.DLFolder",
	service = StagedModelRepository.class
)
public class FolderStagedModelRepository
	implements StagedModelRepository<Folder> {

	@Override
	public Folder addStagedModel(
			PortletDataContext portletDataContext, Folder folder)
		throws PortalException {

		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteStagedModel(Folder folder) throws PortalException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteStagedModel(
			String uuid, long groupId, String className, String extraData)
		throws PortalException {

		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteStagedModels(PortletDataContext portletDataContext)
		throws PortalException {

		throw new UnsupportedOperationException();
	}

	@Override
	public Folder fetchMissingReference(String uuid, long groupId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Folder fetchStagedModelByUuidAndGroupId(String uuid, long groupId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Folder> fetchStagedModelsByUuidAndCompanyId(
		String uuid, long companyId) {

		throw new UnsupportedOperationException();
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		PortletDataContext portletDataContext) {

		Collection<Long> exportableRepositoryIds =
			_dlExportableRepositoryPublisherHelper.publish(
				portletDataContext.getScopeGroupId());

		ExportActionableDynamicQuery exportActionableDynamicQuery =
			_dlFolderLocalService.getExportActionableDynamicQuery(
				portletDataContext);

		ActionableDynamicQuery.AddCriteriaMethod addCriteriaMethod =
			exportActionableDynamicQuery.getAddCriteriaMethod();

		exportActionableDynamicQuery.setAddCriteriaMethod(
			dynamicQuery -> {
				addCriteriaMethod.addCriteria(dynamicQuery);

				Property property = PropertyFactoryUtil.forName("repositoryId");

				dynamicQuery.add(property.in(exportableRepositoryIds));
			});

		exportActionableDynamicQuery.setPerformActionMethod(
			(DLFolder dlFolder) -> {
				if (dlFolder.isInTrash()) {
					return;
				}

				StagedModelDataHandlerUtil.exportStagedModel(
					portletDataContext,
					_dlAppLocalService.getFolder(dlFolder.getFolderId()));
			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(DLFolderConstants.getClassName()));

		return exportActionableDynamicQuery;
	}

	@Override
	public Folder getStagedModel(long folderId) throws PortalException {
		return _dlAppLocalService.getFolder(folderId);
	}

	@Override
	public void restoreStagedModel(
			PortletDataContext portletDataContext, Folder folder)
		throws PortletDataException {

		throw new UnsupportedOperationException();
	}

	@Override
	public Folder saveStagedModel(Folder folder) throws PortalException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Folder updateStagedModel(
			PortletDataContext portletDataContext, Folder folder)
		throws PortalException {

		throw new UnsupportedOperationException();
	}

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private DLExportableRepositoryPublisherHelper
		_dlExportableRepositoryPublisherHelper;

	@Reference
	private DLFolderLocalService _dlFolderLocalService;

}