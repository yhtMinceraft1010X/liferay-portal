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

package com.liferay.journal.internal.trash;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ContainerModel;
import com.liferay.portal.kernel.model.TrashedModel;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.trash.BaseTrashHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eudaldo Alonso
 */
public abstract class BaseJournalTrashHandler extends BaseTrashHandler {

	@Override
	public ContainerModel getContainerModel(long containerModelId)
		throws PortalException {

		return JournalFolderLocalServiceUtil.getFolder(containerModelId);
	}

	@Override
	public String getContainerModelClassName(long classPK) {
		return JournalFolder.class.getName();
	}

	@Override
	public String getContainerModelName() {
		return "folder";
	}

	@Override
	public List<ContainerModel> getContainerModels(
			long classPK, long parentContainerModelId, int start, int end)
		throws PortalException {

		List<JournalFolder> folders = JournalFolderLocalServiceUtil.getFolders(
			getGroupId(classPK), parentContainerModelId, start, end);

		List<ContainerModel> containerModels = new ArrayList<>(folders.size());

		for (JournalFolder curFolder : folders) {
			containerModels.add(curFolder);
		}

		return containerModels;
	}

	@Override
	public int getContainerModelsCount(
			long classPK, long parentContainerModelId)
		throws PortalException {

		return JournalFolderLocalServiceUtil.getFoldersCount(
			getGroupId(classPK), parentContainerModelId);
	}

	@Override
	public List<ContainerModel> getParentContainerModels(long classPK)
		throws PortalException {

		List<ContainerModel> containerModels = new ArrayList<>();

		ContainerModel containerModel = getParentContainerModel(classPK);

		if (containerModel == null) {
			return containerModels;
		}

		containerModels.add(containerModel);

		while (containerModel.getParentContainerModelId() > 0) {
			containerModel = getContainerModel(
				containerModel.getParentContainerModelId());

			if (containerModel == null) {
				break;
			}

			containerModels.add(containerModel);
		}

		return containerModels;
	}

	@Override
	public String getRootContainerModelName() {
		return "folder";
	}

	@Override
	public String getSubcontainerModelName() {
		return "folder";
	}

	@Override
	public String getTrashContainedModelName() {
		return "article";
	}

	@Override
	public int getTrashContainedModelsCount(long classPK)
		throws PortalException {

		JournalFolder folder = JournalFolderLocalServiceUtil.getFolder(classPK);

		return JournalArticleLocalServiceUtil.searchCount(
			folder.getGroupId(), classPK, WorkflowConstants.STATUS_IN_TRASH);
	}

	@Override
	public String getTrashContainerModelName() {
		return "folders";
	}

	@Override
	public int getTrashContainerModelsCount(long classPK)
		throws PortalException {

		JournalFolder folder = JournalFolderLocalServiceUtil.getFolder(classPK);

		return JournalFolderLocalServiceUtil.getFoldersCount(
			folder.getGroupId(), classPK, WorkflowConstants.STATUS_IN_TRASH);
	}

	@Override
	public int getTrashModelsCount(long classPK) throws PortalException {
		JournalFolder folder = JournalFolderLocalServiceUtil.getFolder(classPK);

		return JournalFolderLocalServiceUtil.getFoldersAndArticlesCount(
			folder.getGroupId(), classPK, WorkflowConstants.STATUS_IN_TRASH);
	}

	@Override
	public List<TrashedModel> getTrashModelTrashedModels(
			long classPK, int start, int end,
			OrderByComparator<?> orderByComparator)
		throws PortalException {

		List<TrashedModel> trashedModels = new ArrayList<>();

		JournalFolder folder = JournalFolderLocalServiceUtil.getFolder(classPK);

		List<Object> foldersAndArticles =
			JournalFolderLocalServiceUtil.getFoldersAndArticles(
				folder.getGroupId(), classPK, WorkflowConstants.STATUS_IN_TRASH,
				start, end, orderByComparator);

		for (Object folderOrArticle : foldersAndArticles) {
			if (folderOrArticle instanceof JournalFolder) {
				JournalFolder curFolder = (JournalFolder)folderOrArticle;

				trashedModels.add(curFolder);
			}
			else {
				JournalArticle article = (JournalArticle)folderOrArticle;

				trashedModels.add(article);
			}
		}

		return trashedModels;
	}

	protected abstract long getGroupId(long classPK) throws PortalException;

}