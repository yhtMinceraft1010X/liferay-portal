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

package com.liferay.portal.search.tuning.rankings.web.internal.portlet.action;

import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.security.permission.ResourceActions;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.FastDateFormatFactory;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.filter.ComplexQueryPartBuilderFactory;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.searcher.Searcher;
import com.liferay.portal.search.tuning.rankings.web.internal.constants.ResultRankingsPortletKeys;
import com.liferay.portal.search.tuning.rankings.web.internal.index.RankingIndexReader;
import com.liferay.portal.search.tuning.rankings.web.internal.index.name.RankingIndexName;
import com.liferay.portal.search.tuning.rankings.web.internal.index.name.RankingIndexNameBuilder;
import com.liferay.portal.search.tuning.rankings.web.internal.results.builder.RankingGetHiddenResultsBuilder;
import com.liferay.portal.search.tuning.rankings.web.internal.results.builder.RankingGetSearchResultsBuilder;
import com.liferay.portal.search.tuning.rankings.web.internal.results.builder.RankingGetVisibleResultsBuilder;
import com.liferay.portal.search.tuning.rankings.web.internal.searcher.helper.RankingSearchRequestHelper;

import java.io.IOException;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bryan Engler
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ResultRankingsPortletKeys.RESULT_RANKINGS,
		"mvc.command.name=/result_rankings/get_results"
	},
	service = MVCResourceCommand.class
)
public class GetResultsMVCResourceCommand implements MVCResourceCommand {

	@Override
	public boolean serveResource(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		try {
			writeJSONPortletResponse(
				resourceRequest, resourceResponse,
				getJSONObject(resourceRequest, resourceResponse));

			return false;
		}
		catch (RuntimeException runtimeException) {
			_log.error(runtimeException);

			throw runtimeException;
		}
	}

	protected static JSONObject getJSONObject(JSONArray jsonArray) {
		return JSONUtil.put(
			"documents", jsonArray
		).put(
			"total", jsonArray.length()
		);
	}

	protected JSONObject getHiddenResultsJSONObject(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		RankingGetHiddenResultsBuilder rankingGetHiddenResultsBuilder =
			new RankingGetHiddenResultsBuilder(
				dlAppLocalService, fastDateFormatFactory, queries,
				getRankingIndexName(resourceRequest), rankingIndexReader,
				resourceActions, resourceRequest, resourceResponse,
				searchEngineAdapter);

		RankingMVCResourceRequest rankingMVCResourceRequest =
			new RankingMVCResourceRequest(resourceRequest);

		return rankingGetHiddenResultsBuilder.rankingId(
			rankingMVCResourceRequest.getRankingId()
		).from(
			rankingMVCResourceRequest.getFrom()
		).size(
			rankingMVCResourceRequest.getSize()
		).build();
	}

	protected JSONObject getJSONObject(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		String cmd = ParamUtil.getString(resourceRequest, Constants.CMD);

		if (cmd.equals("getHiddenResultsJSONObject")) {
			return getHiddenResultsJSONObject(
				resourceRequest, resourceResponse);
		}

		if (cmd.equals("getSearchResultsJSONObject")) {
			return getSearchResultsJSONObject(
				resourceRequest, resourceResponse);
		}

		if (cmd.equals("getVisibleResultsJSONObject")) {
			return getVisibleResultsJSONObject(
				resourceRequest, resourceResponse);
		}

		return null;
	}

	protected RankingIndexName getRankingIndexName(
		ResourceRequest resourceRequest) {

		return rankingIndexNameBuilder.getRankingIndexName(
			portal.getCompanyId(resourceRequest));
	}

	protected JSONObject getSearchResultsJSONObject(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		RankingGetSearchResultsBuilder rankingGetSearchResultsBuilder =
			new RankingGetSearchResultsBuilder(
				complexQueryPartBuilderFactory, dlAppLocalService,
				fastDateFormatFactory, queries, resourceActions,
				resourceRequest, resourceResponse, searcher,
				searchRequestBuilderFactory);

		RankingMVCResourceRequest rankingMVCResourceRequest =
			new RankingMVCResourceRequest(resourceRequest);

		return rankingGetSearchResultsBuilder.companyId(
			rankingMVCResourceRequest.getCompanyId()
		).from(
			rankingMVCResourceRequest.getFrom()
		).queryString(
			rankingMVCResourceRequest.getQueryString()
		).size(
			rankingMVCResourceRequest.getSize()
		).build();
	}

	protected JSONObject getVisibleResultsJSONObject(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		RankingGetVisibleResultsBuilder rankingGetVisibleResultsBuilder =
			new RankingGetVisibleResultsBuilder(
				complexQueryPartBuilderFactory, dlAppLocalService,
				fastDateFormatFactory, getRankingIndexName(resourceRequest),
				rankingIndexReader, rankingSearchRequestHelper, resourceActions,
				resourceRequest, resourceResponse, queries, searcher,
				searchRequestBuilderFactory);

		RankingMVCResourceRequest rankingMVCResourceRequest =
			new RankingMVCResourceRequest(resourceRequest);

		return rankingGetVisibleResultsBuilder.companyId(
			rankingMVCResourceRequest.getCompanyId()
		).from(
			rankingMVCResourceRequest.getFrom()
		).queryString(
			rankingMVCResourceRequest.getQueryString()
		).rankingId(
			rankingMVCResourceRequest.getRankingId()
		).size(
			rankingMVCResourceRequest.getSize()
		).build();
	}

	protected void writeJSONPortletResponse(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse,
		JSONObject jsonObject) {

		if (jsonObject == null) {
			return;
		}

		try {
			JSONPortletResponseUtil.writeJSON(
				resourceRequest, resourceResponse, jsonObject);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	@Reference
	protected ComplexQueryPartBuilderFactory complexQueryPartBuilderFactory;

	@Reference
	protected DLAppLocalService dlAppLocalService;

	@Reference
	protected FastDateFormatFactory fastDateFormatFactory;

	@Reference
	protected Portal portal;

	@Reference
	protected Queries queries;

	@Reference
	protected RankingIndexNameBuilder rankingIndexNameBuilder;

	@Reference
	protected RankingIndexReader rankingIndexReader;

	@Reference
	protected RankingSearchRequestHelper rankingSearchRequestHelper;

	@Reference
	protected ResourceActions resourceActions;

	@Reference
	protected SearchEngineAdapter searchEngineAdapter;

	@Reference
	protected Searcher searcher;

	@Reference
	protected SearchRequestBuilderFactory searchRequestBuilderFactory;

	private static final Log _log = LogFactoryUtil.getLog(
		GetResultsMVCResourceCommand.class);

	private class RankingMVCResourceRequest {

		public RankingMVCResourceRequest(ResourceRequest resourceRequest) {
			_resourceRequest = resourceRequest;
		}

		public long getCompanyId() {
			return ParamUtil.getLong(_resourceRequest, "companyId");
		}

		public int getFrom() {
			return ParamUtil.getInteger(_resourceRequest, "from");
		}

		public String getQueryString() {
			return ParamUtil.getString(_resourceRequest, "keywords");
		}

		public String getRankingId() {
			return ParamUtil.getString(_resourceRequest, "resultsRankingUid");
		}

		public int getSize() {
			return ParamUtil.getInteger(_resourceRequest, "size", 10);
		}

		private final ResourceRequest _resourceRequest;

	}

}