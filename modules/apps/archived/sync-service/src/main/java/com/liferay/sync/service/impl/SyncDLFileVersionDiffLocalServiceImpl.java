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

package com.liferay.sync.service.impl;

import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepository;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.sync.internal.configuration.SyncServiceConfigurationValues;
import com.liferay.sync.model.SyncDLFileVersionDiff;
import com.liferay.sync.service.base.SyncDLFileVersionDiffLocalServiceBaseImpl;

import java.io.File;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Dennis Ju
 */
@Component(
	property = "model.class.name=com.liferay.sync.model.SyncDLFileVersionDiff",
	service = AopService.class
)
public class SyncDLFileVersionDiffLocalServiceImpl
	extends SyncDLFileVersionDiffLocalServiceBaseImpl {

	@Override
	public SyncDLFileVersionDiff addSyncDLFileVersionDiff(
			long fileEntryId, long sourceFileVersionId,
			long targetFileVersionId, File file)
		throws PortalException {

		long syncDLFileVersionDiffId = counterLocalService.increment();

		SyncDLFileVersionDiff syncDLFileVersionDiff =
			syncDLFileVersionDiffPersistence.create(syncDLFileVersionDiffId);

		syncDLFileVersionDiff.setFileEntryId(fileEntryId);
		syncDLFileVersionDiff.setSourceFileVersionId(sourceFileVersionId);
		syncDLFileVersionDiff.setTargetFileVersionId(targetFileVersionId);

		FileEntry fileEntry = _dlAppLocalService.getFileEntry(fileEntryId);

		Company company = _companyLocalService.getCompanyById(
			fileEntry.getCompanyId());

		FileEntry dataFileEntry = _portletFileRepository.addPortletFileEntry(
			company.getGroupId(), fileEntry.getUserId(),
			SyncDLFileVersionDiff.class.getName(),
			syncDLFileVersionDiff.getSyncDLFileVersionDiffId(),
			PortletKeys.DOCUMENT_LIBRARY,
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, file,
			getDataFileName(
				fileEntryId, sourceFileVersionId, targetFileVersionId),
			fileEntry.getMimeType(), false);

		syncDLFileVersionDiff.setDataFileEntryId(
			dataFileEntry.getFileEntryId());

		syncDLFileVersionDiff.setSize(file.length());

		Date expirationDate = new Date();

		long cacheExpirationTime =
			SyncServiceConfigurationValues.
				SYNC_FILE_DIFF_CACHE_EXPIRATION_TIME * 3600000;

		expirationDate.setTime(expirationDate.getTime() + cacheExpirationTime);

		syncDLFileVersionDiff.setExpirationDate(expirationDate);

		return syncDLFileVersionDiffPersistence.update(syncDLFileVersionDiff);
	}

	@Override
	public void deleteExpiredSyncDLFileVersionDiffs() throws PortalException {
		List<SyncDLFileVersionDiff> syncDLFileVersionDiffs =
			syncDLFileVersionDiffPersistence.findByLtExpirationDate(new Date());

		for (SyncDLFileVersionDiff syncDLFileVersionDiff :
				syncDLFileVersionDiffs) {

			deleteSyncDLFileVersionDiff(syncDLFileVersionDiff);
		}
	}

	@Override
	public SyncDLFileVersionDiff deleteSyncDLFileVersionDiff(
			SyncDLFileVersionDiff syncDLFileVersionDiff)
		throws PortalException {

		try {
			_portletFileRepository.deletePortletFileEntry(
				syncDLFileVersionDiff.getDataFileEntryId());
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to delete file entry " +
						syncDLFileVersionDiff.getDataFileEntryId(),
					exception);
			}
		}

		return super.deleteSyncDLFileVersionDiff(syncDLFileVersionDiff);
	}

	@Override
	public void deleteSyncDLFileVersionDiffs(long fileEntryId)
		throws PortalException {

		List<SyncDLFileVersionDiff> syncDLFileVersionDiffs =
			syncDLFileVersionDiffPersistence.findByFileEntryId(fileEntryId);

		for (SyncDLFileVersionDiff syncDLFileVersionDiff :
				syncDLFileVersionDiffs) {

			deleteSyncDLFileVersionDiff(syncDLFileVersionDiff);
		}
	}

	@Override
	public SyncDLFileVersionDiff fetchSyncDLFileVersionDiff(
		long fileEntryId, long sourceFileVersionId, long targetFileVersionId) {

		return syncDLFileVersionDiffPersistence.fetchByF_S_T(
			fileEntryId, sourceFileVersionId, targetFileVersionId);
	}

	@Override
	public void refreshExpirationDate(long syncDLFileVersionDiffId)
		throws PortalException {

		SyncDLFileVersionDiff syncDLFileVersionDiff =
			syncDLFileVersionDiffPersistence.findByPrimaryKey(
				syncDLFileVersionDiffId);

		Date expirationDate = new Date();

		long cacheExpirationTime =
			SyncServiceConfigurationValues.
				SYNC_FILE_DIFF_CACHE_EXPIRATION_TIME * 3600000;

		expirationDate.setTime(expirationDate.getTime() + cacheExpirationTime);

		syncDLFileVersionDiff.setExpirationDate(expirationDate);

		updateSyncDLFileVersionDiff(syncDLFileVersionDiff);
	}

	protected String getDataFileName(
		long fileEntryId, long sourceFileVersionId, long targetFileVersionId) {

		return StringBundler.concat(
			fileEntryId, StringPool.UNDERLINE, sourceFileVersionId,
			StringPool.UNDERLINE, targetFileVersionId);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SyncDLFileVersionDiffLocalServiceImpl.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private PortletFileRepository _portletFileRepository;

}