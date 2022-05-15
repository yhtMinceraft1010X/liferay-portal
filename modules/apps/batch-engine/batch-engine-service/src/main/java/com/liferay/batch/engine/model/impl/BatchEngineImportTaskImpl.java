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

package com.liferay.batch.engine.model.impl;

import com.liferay.batch.engine.model.BatchEngineImportTaskError;
import com.liferay.batch.engine.service.BatchEngineImportTaskErrorLocalServiceUtil;

import java.util.List;

/**
 * The extended model implementation for the BatchEngineImportTask service.
 * Represents a row in the &quot;BatchEngineImportTask&quot; database table,
 * with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class.
 * Whenever methods are added, rerun ServiceBuilder to copy their definitions
 * into the <code>com.liferay.batch.engine.model.BatchEngineImportTask</code>
 * interface.
 * </p>
 *
 * @author Shuyang Zhou
 */
public class BatchEngineImportTaskImpl extends BatchEngineImportTaskBaseImpl {

	public List<BatchEngineImportTaskError> getBatchEngineImportTaskErrors() {
		return BatchEngineImportTaskErrorLocalServiceUtil.
			getBatchEngineImportTaskErrors(getBatchEngineImportTaskId());
	}

	public int getBatchEngineImportTaskErrorsCount() {
		return BatchEngineImportTaskErrorLocalServiceUtil.
			getBatchEngineImportTaskErrorsCount(getBatchEngineImportTaskId());
	}

}