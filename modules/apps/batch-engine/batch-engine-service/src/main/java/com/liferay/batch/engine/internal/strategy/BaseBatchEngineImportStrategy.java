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

package com.liferay.batch.engine.internal.strategy;

import com.liferay.batch.engine.service.BatchEngineImportTaskErrorLocalServiceUtil;
import com.liferay.batch.engine.strategy.BatchEngineImportStrategy;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;

/**
 * @author Matija Petanjek
 * @author Brian Wing Shun Chan
 */
public abstract class BaseBatchEngineImportStrategy
	implements BatchEngineImportStrategy {

	protected void addBatchEngineImportTaskError(
		long companyId, long userId, long batchEngineImportTaskId, String item,
		int itemIndex, String message) {

		try {
			TransactionInvokerUtil.invoke(
				_transactionConfig,
				() -> {
					BatchEngineImportTaskErrorLocalServiceUtil.
						addBatchEngineImportTaskError(
							companyId, userId, batchEngineImportTaskId, item,
							itemIndex, message);

					return null;
				});
		}
		catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	private static final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRES_NEW, new Class<?>[] {Exception.class});

}