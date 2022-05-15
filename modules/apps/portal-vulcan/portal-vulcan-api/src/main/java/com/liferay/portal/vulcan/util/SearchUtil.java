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

package com.liferay.portal.vulcan.util;

import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.dao.orm.QueryDefinition;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.IndexSearcherHelperUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.facet.SimpleFacet;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.MatchAllQuery;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.searcher.SearchResponse;
import com.liferay.portal.vulcan.aggregation.Aggregation;
import com.liferay.portal.vulcan.aggregation.Facet;
import com.liferay.portal.vulcan.aggregation.FacetUtil;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Brian Wing Shun Chan
 */
public class SearchUtil {

	public static <T extends BaseModel<T>> QueryDefinition<T>
		getQueryDefinition(
			Class<T> clazz, Pagination pagination, Sort[] sorts) {

		QueryDefinition<T> queryDefinition = new QueryDefinition<>();

		queryDefinition.setEnd(pagination.getEndPosition());

		Object[] orderByComparatorColumns = _getOrderByComparatorColumns(sorts);

		if (orderByComparatorColumns != null) {
			queryDefinition.setOrderByComparator(
				OrderByComparatorFactoryUtil.create(
					clazz.getSimpleName(), orderByComparatorColumns));
		}

		queryDefinition.setStart(pagination.getStartPosition());

		return queryDefinition;
	}

	public static <T> Page<T> search(
			Map<String, Map<String, String>> actions,
			UnsafeConsumer<BooleanQuery, Exception> booleanQueryUnsafeConsumer,
			Filter filter, String indexerClassName, String keywords,
			Pagination pagination,
			UnsafeConsumer<QueryConfig, Exception> queryConfigUnsafeConsumer,
			UnsafeConsumer<SearchContext, Exception>
				searchContextUnsafeConsumer,
			Sort[] sorts,
			UnsafeFunction<Document, T, Exception> transformUnsafeFunction)
		throws Exception {

		Hits hits = null;
		long totalCount = 0;

		Indexer<?> indexer = IndexerRegistryUtil.getIndexer(indexerClassName);

		if (sorts == null) {
			sorts = new Sort[] {
				new Sort(Field.ENTRY_CLASS_PK, Sort.LONG_TYPE, false)
			};
		}

		SearchContext searchContext = _createSearchContext(
			_getBooleanClause(booleanQueryUnsafeConsumer, filter), keywords,
			pagination, queryConfigUnsafeConsumer, sorts);

		searchContextUnsafeConsumer.accept(searchContext);

		if (searchContext.isVulcanCheckPermissions()) {
			hits = indexer.search(searchContext);
			totalCount = indexer.searchCount(searchContext);
		}
		else {
			Query query = indexer.getFullQuery(searchContext);

			hits = IndexSearcherHelperUtil.search(searchContext, query);
			totalCount = IndexSearcherHelperUtil.searchCount(
				searchContext, query);
		}

		List<T> items = new ArrayList<>();

		for (Document document : hits.getDocs()) {
			T item = transformUnsafeFunction.apply(document);

			if (item != null) {
				items.add(item);
			}
		}

		return Page.of(
			(actions != null) ? actions : Collections.emptyMap(),
			_getFacets(searchContext), items, pagination, totalCount);
	}

	public static class SearchContext
		extends com.liferay.portal.kernel.search.SearchContext {

		@Override
		public void addFacet(
			com.liferay.portal.kernel.search.facet.Facet facet) {

			Map<String, com.liferay.portal.kernel.search.facet.Facet> facets =
				getFacets();

			if (!facets.containsKey(facet.getFieldName())) {
				super.addFacet(facet);
			}
		}

		public void addVulcanAggregation(Aggregation aggregation) {
			if ((aggregation == null) ||
				(aggregation.getAggregationTerms() == null)) {

				return;
			}

			Map<String, String> aggregationTerms =
				aggregation.getAggregationTerms();

			for (Map.Entry<String, String> entry :
					aggregationTerms.entrySet()) {

				com.liferay.portal.kernel.search.facet.Facet facet =
					new SimpleFacet(this);

				FacetConfiguration facetConfiguration =
					facet.getFacetConfiguration();

				facetConfiguration.setLabel(entry.getKey());

				facet.setFieldName(entry.getValue());

				addFacet(facet);
			}
		}

		public boolean isVulcanCheckPermissions() {
			return _vulcanCheckPermissions;
		}

		public void setVulcanCheckPermissions(boolean vulcanCheckPermissions) {
			_vulcanCheckPermissions = vulcanCheckPermissions;
		}

		private boolean _vulcanCheckPermissions = true;

	}

	private static SearchContext _createSearchContext(
			BooleanClause<?> booleanClause, String keywords,
			Pagination pagination,
			UnsafeConsumer<QueryConfig, Exception> queryConfigUnsafeConsumer,
			Sort[] sorts)
		throws Exception {

		SearchContext searchContext = new SearchContext();

		searchContext.setBooleanClauses(new BooleanClause[] {booleanClause});

		if (pagination != null) {
			searchContext.setEnd(pagination.getEndPosition());
		}

		searchContext.setKeywords(keywords);
		searchContext.setSorts(sorts);

		if (pagination != null) {
			searchContext.setStart(pagination.getStartPosition());
		}

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		searchContext.setUserId(permissionChecker.getUserId());

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		queryConfigUnsafeConsumer.accept(queryConfig);

		return searchContext;
	}

	private static BooleanClause<?> _getBooleanClause(
			UnsafeConsumer<BooleanQuery, Exception> booleanQueryUnsafeConsumer,
			Filter filter)
		throws Exception {

		BooleanQuery booleanQuery = new BooleanQueryImpl() {
			{
				add(new MatchAllQuery(), BooleanClauseOccur.MUST);

				BooleanFilter booleanFilter = new BooleanFilter();

				if (filter != null) {
					booleanFilter.add(filter, BooleanClauseOccur.MUST);
				}

				setPreBooleanFilter(booleanFilter);
			}
		};

		booleanQueryUnsafeConsumer.accept(booleanQuery);

		return BooleanClauseFactoryUtil.create(
			booleanQuery, BooleanClauseOccur.MUST.getName());
	}

	private static List<Facet> _getFacets(SearchContext searchContext) {
		Map<String, com.liferay.portal.kernel.search.facet.Facet>
			searchContextFacets = searchContext.getFacets();

		List<Facet> facets = TransformUtil.transform(
			searchContextFacets.values(), FacetUtil::toFacet);

		SearchResponse searchResponse =
			(SearchResponse)searchContext.getAttribute("search.response");

		if (searchResponse == null) {
			return new ArrayList<>();
		}

		Map<String, AggregationResult> aggregationResultsMap =
			searchResponse.getAggregationResultsMap();

		if (aggregationResultsMap != null) {
			for (AggregationResult aggregationResult :
					aggregationResultsMap.values()) {

				facets.addAll(FacetUtil.toFacets(aggregationResult));
			}
		}

		return facets;
	}

	private static Object[] _getOrderByComparatorColumns(Sort[] sorts) {
		if (ArrayUtil.isEmpty(sorts)) {
			return null;
		}

		return Stream.of(
			sorts
		).flatMap(
			sort -> Stream.of(sort.getFieldName(), !sort.isReverse())
		).toArray();
	}

}