/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.headless.commerce.machine.learning.internal.dispatch.executor;

import com.liferay.dispatch.executor.DispatchTaskExecutor;
import com.liferay.dispatch.executor.DispatchTaskExecutorOutput;
import com.liferay.dispatch.executor.DispatchTaskStatus;
import com.liferay.dispatch.model.DispatchLog;
import com.liferay.dispatch.model.DispatchTrigger;
import com.liferay.headless.commerce.machine.learning.dto.v1_0.ProductInteractionRecommendation;
import com.liferay.headless.commerce.machine.learning.dto.v1_0.UserRecommendation;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.Date;

import org.osgi.service.component.annotations.Component;

/**
 * @author Riccardo Ferrari
 */
@Component(
	enabled = false, immediate = true,
	property = {
		"dispatch.task.executor.name=" + AnalyticsDownloadUserCommerceMLRecommendDispatchTaskExecutor.KEY,
		"dispatch.task.executor.type=" + AnalyticsDownloadUserCommerceMLRecommendDispatchTaskExecutor.KEY
	},
	service = DispatchTaskExecutor.class
)
public class AnalyticsDownloadUserCommerceMLRecommendDispatchTaskExecutor
	extends BaseDispatchTaskExecutor {

	public static final String KEY =
		"analytics-download-user-commerce-ml-recommendation";

	@Override
	public void doExecute(
			DispatchTrigger dispatchTrigger,
			DispatchTaskExecutorOutput dispatchTaskExecutorOutput)
		throws Exception {

		DispatchLog dispatchLog =
			dispatchLogLocalService.fetchLatestDispatchLog(
				dispatchTrigger.getDispatchTriggerId(),
				DispatchTaskStatus.IN_PROGRESS);

		Date resourceLastModifiedDate = getLatestSuccessfulDispatchLogEndDate(
			dispatchTrigger.getDispatchTriggerId());

		analyticsBatchExportImportManager.importFromAnalyticsCloud(
			null, dispatchTrigger.getCompanyId(),
			HashMapBuilder.put(
				"assetCategoryIds", "assetCategoryIds"
			).put(
				"createDate", "createDate"
			).put(
				"entryClassPK", "productId"
			).put(
				"jobId", "jobId"
			).put(
				"recommendedEntryClassPK", "recommendedProductId"
			).put(
				"score", "score"
			).build(),
			message -> updateDispatchLog(
				dispatchLog.getDispatchLogId(), dispatchTaskExecutorOutput,
				message),
			resourceLastModifiedDate, UserRecommendation.class.getName(),
			dispatchTrigger.getUserId());

		analyticsBatchExportImportManager.importFromAnalyticsCloud(
			null, dispatchTrigger.getCompanyId(),
			HashMapBuilder.put(
				"assetCategoryIds", "assetCategoryIds"
			).put(
				"createDate", "createDate"
			).put(
				"entryClassPK", "productId"
			).put(
				"jobId", "jobId"
			).put(
				"recommendedEntryClassPK", "recommendedProductId"
			).put(
				"score", "score"
			).build(),
			message -> updateDispatchLog(
				dispatchLog.getDispatchLogId(), dispatchTaskExecutorOutput,
				message),
			resourceLastModifiedDate,
			ProductInteractionRecommendation.class.getName(),
			dispatchTrigger.getUserId());
	}

	@Override
	public String getName() {
		return KEY;
	}

}