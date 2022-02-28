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

package com.liferay.analytics.batch.exportimport.internal.manager;

import com.liferay.analytics.batch.exportimport.client.AnalyticsBatchClient;
import com.liferay.analytics.batch.exportimport.client.UploadType;
import com.liferay.analytics.batch.exportimport.manager.AnalyticsBatchExportImportManager;
import com.liferay.batch.engine.BatchEngineExportTaskExecutor;
import com.liferay.batch.engine.BatchEngineImportTaskExecutor;
import com.liferay.batch.engine.BatchEngineTaskContentType;
import com.liferay.batch.engine.BatchEngineTaskExecuteStatus;
import com.liferay.batch.engine.BatchEngineTaskOperation;
import com.liferay.batch.engine.model.BatchEngineExportTask;
import com.liferay.batch.engine.model.BatchEngineImportTask;
import com.liferay.batch.engine.service.BatchEngineExportTaskLocalService;
import com.liferay.batch.engine.service.BatchEngineImportTaskLocalService;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Field;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

import java.nio.file.Files;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(immediate = true, service = AnalyticsBatchExportImportManager.class)
public class AnalyticsBatchExportImportManagerImpl
	implements AnalyticsBatchExportImportManager {

	@Override
	public void exportToAnalyticsCloud(
			String batchEngineExportTaskItemDelegateName, long companyId,
			List<String> fieldNamesList,
			UnsafeConsumer<String, Exception> notificationUnsafeConsumer,
			Date resourceLastModifiedDate, String resourceName, long userId)
		throws Exception {

		_notify(
			notificationUnsafeConsumer, "Exporting resource " + resourceName);

		Map<String, Serializable> parameters = new HashMap<>();

		if (resourceLastModifiedDate != null) {
			parameters.put(
				"filter",
				StringBundler.concat(
					Field.getSortableFieldName(Field.MODIFIED_DATE), " ge ",
					resourceLastModifiedDate.getTime()));
		}

		BatchEngineExportTask batchEngineExportTask =
			_batchEngineExportTaskLocalService.addBatchEngineExportTask(
				companyId, userId, null, resourceName,
				BatchEngineTaskContentType.JSONL.name(),
				BatchEngineTaskExecuteStatus.INITIAL.name(), fieldNamesList,
				parameters, batchEngineExportTaskItemDelegateName);

		_batchEngineExportTaskExecutor.execute(batchEngineExportTask);

		BatchEngineTaskExecuteStatus batchEngineTaskExecuteStatus =
			BatchEngineTaskExecuteStatus.valueOf(
				batchEngineExportTask.getExecuteStatus());

		if (batchEngineTaskExecuteStatus.equals(
				BatchEngineTaskExecuteStatus.COMPLETED)) {

			_notify(
				notificationUnsafeConsumer,
				StringBundler.concat(
					"Exported ", batchEngineExportTask.getTotalItemsCount(),
					" items for resource ", resourceName));

			if (batchEngineExportTask.getTotalItemsCount() == 0) {
				_notify(
					notificationUnsafeConsumer, "There are no items to upload");

				return;
			}

			_notify(
				notificationUnsafeConsumer,
				"Uploading resource " + resourceName);

			InputStream contentInputStream =
				_batchEngineExportTaskLocalService.openContentInputStream(
					batchEngineExportTask.getBatchEngineExportTaskId());

			_analyticsBatchClient.upload(
				companyId, contentInputStream, resourceName,
				(resourceLastModifiedDate != null) ? UploadType.INCREMENTAL :
					UploadType.FULL);

			contentInputStream.close();

			_batchEngineExportTaskLocalService.deleteBatchEngineExportTask(
				batchEngineExportTask);

			_notify(
				notificationUnsafeConsumer,
				"Completed uploading resource " + resourceName);
		}
		else {
			throw new PortalException(
				"Unable to export resource " + resourceName);
		}
	}

	@Override
	public void importFromAnalyticsCloud(
			String batchEngineImportTaskItemDelegateName, long companyId,
			Map<String, String> fieldMapping,
			UnsafeConsumer<String, Exception> notificationUnsafeConsumer,
			Date resourceLastModifiedDate, String resourceName, long userId)
		throws Exception {

		_notify(
			notificationUnsafeConsumer,
			"Checking changes for resource " + resourceName);

		File resourceFile = _analyticsBatchClient.download(
			companyId, resourceLastModifiedDate, resourceName);

		if (resourceFile == null) {
			_notify(
				notificationUnsafeConsumer,
				"There are no changes for resource " + resourceName);

			return;
		}

		_notify(
			notificationUnsafeConsumer, "Importing resource " + resourceName);

		BatchEngineImportTask batchEngineImportTask =
			_batchEngineImportTaskLocalService.addBatchEngineImportTask(
				companyId, userId, 50, null, resourceName,
				Files.readAllBytes(resourceFile.toPath()),
				BatchEngineTaskContentType.JSONL.name(),
				BatchEngineTaskExecuteStatus.INITIAL.name(), fieldMapping,
				BatchEngineTaskOperation.CREATE.name(), null,
				batchEngineImportTaskItemDelegateName);

		_batchEngineImportTaskExecutor.execute(batchEngineImportTask);

		BatchEngineTaskExecuteStatus batchEngineTaskExecuteStatus =
			BatchEngineTaskExecuteStatus.valueOf(
				batchEngineImportTask.getExecuteStatus());

		if (batchEngineTaskExecuteStatus.equals(
				BatchEngineTaskExecuteStatus.COMPLETED)) {

			_notify(
				notificationUnsafeConsumer,
				StringBundler.concat(
					"Imported ", batchEngineImportTask.getTotalItemsCount(),
					" items for resource ", resourceName));

			_batchEngineImportTaskLocalService.deleteBatchEngineImportTask(
				batchEngineImportTask);
		}
		else {
			throw new PortalException(
				"Unable to import resource " + resourceName);
		}
	}

	@Reference
	protected BatchEngineExportTaskExecutor batchEngineExportTaskExecutor;

	private void _notify(
			UnsafeConsumer<String, Exception> notificationUnsafeConsumer,
			String message)
		throws Exception {

		if (_log.isDebugEnabled()) {
			_log.debug(message);
		}

		if (notificationUnsafeConsumer == null) {
			return;
		}

		notificationUnsafeConsumer.accept(message);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AnalyticsBatchExportImportManagerImpl.class);

	@Reference
	private AnalyticsBatchClient _analyticsBatchClient;

	@Reference
	private BatchEngineExportTaskExecutor _batchEngineExportTaskExecutor;

	@Reference
	private BatchEngineExportTaskLocalService
		_batchEngineExportTaskLocalService;

	@Reference
	private BatchEngineImportTaskExecutor _batchEngineImportTaskExecutor;

	@Reference
	private BatchEngineImportTaskLocalService
		_batchEngineImportTaskLocalService;

}