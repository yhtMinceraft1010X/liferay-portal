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

package com.liferay.portal.search.elasticsearch7.internal.search.engine.adapter;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.elasticsearch7.internal.connection.ElasticsearchClientResolver;
import com.liferay.portal.search.elasticsearch7.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch7.internal.search.engine.adapter.index.IndexRequestExecutorFixture;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.index.AnalysisIndexResponseToken;
import com.liferay.portal.search.engine.adapter.index.AnalyzeIndexRequest;
import com.liferay.portal.search.engine.adapter.index.AnalyzeIndexResponse;
import com.liferay.portal.search.engine.adapter.index.CloseIndexRequest;
import com.liferay.portal.search.engine.adapter.index.CloseIndexResponse;
import com.liferay.portal.search.engine.adapter.index.CreateIndexRequest;
import com.liferay.portal.search.engine.adapter.index.CreateIndexResponse;
import com.liferay.portal.search.engine.adapter.index.DeleteIndexRequest;
import com.liferay.portal.search.engine.adapter.index.DeleteIndexResponse;
import com.liferay.portal.search.engine.adapter.index.FlushIndexRequest;
import com.liferay.portal.search.engine.adapter.index.FlushIndexResponse;
import com.liferay.portal.search.engine.adapter.index.GetFieldMappingIndexRequest;
import com.liferay.portal.search.engine.adapter.index.GetFieldMappingIndexResponse;
import com.liferay.portal.search.engine.adapter.index.GetIndexIndexRequest;
import com.liferay.portal.search.engine.adapter.index.GetIndexIndexResponse;
import com.liferay.portal.search.engine.adapter.index.GetMappingIndexRequest;
import com.liferay.portal.search.engine.adapter.index.GetMappingIndexResponse;
import com.liferay.portal.search.engine.adapter.index.IndexRequestExecutor;
import com.liferay.portal.search.engine.adapter.index.IndicesExistsIndexRequest;
import com.liferay.portal.search.engine.adapter.index.IndicesExistsIndexResponse;
import com.liferay.portal.search.engine.adapter.index.IndicesOptions;
import com.liferay.portal.search.engine.adapter.index.OpenIndexRequest;
import com.liferay.portal.search.engine.adapter.index.OpenIndexResponse;
import com.liferay.portal.search.engine.adapter.index.PutMappingIndexRequest;
import com.liferay.portal.search.engine.adapter.index.PutMappingIndexResponse;
import com.liferay.portal.search.engine.adapter.index.RefreshIndexRequest;
import com.liferay.portal.search.engine.adapter.index.RefreshIndexResponse;
import com.liferay.portal.search.engine.adapter.index.UpdateIndexSettingsIndexRequest;
import com.liferay.portal.search.engine.adapter.index.UpdateIndexSettingsIndexResponse;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EntityUtils;

import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.cluster.metadata.IndexMetadata;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.xcontent.XContentType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Dylan Rebelak
 */
public class ElasticsearchSearchEngineAdapterIndexRequestTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_elasticsearchFixture = new ElasticsearchFixture();

		_elasticsearchFixture.setUp();

		_searchEngineAdapter = createSearchEngineAdapter(_elasticsearchFixture);

		RestHighLevelClient restHighLevelClient =
			_elasticsearchFixture.getRestHighLevelClient();

		_indicesClient = restHighLevelClient.indices();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_elasticsearchFixture.tearDown();
	}

	@Before
	public void setUp() {
		_createIndex(_INDEX_NAME);
	}

	@After
	public void tearDown() {
		_deleteIndex(_INDEX_NAME);
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithAnalyzer() {
		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setAnalyzer("stop");

		_assertExecuteAnalyzeIndexRequest(
			analyzeIndexRequest,
			"quick,brown,foxes,jumped,over,lazy,dog,s,bone");
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithCharFilters() {
		_putSettings(
			StringBundler.concat(
				"{\n", "    \"settings\": {\n", "        \"analysis\": {\n",
				"            \"char_filter\": {\n",
				"                \"custom_cf\": {\n",
				"                    \"type\": \"mapping\",\n",
				"                    \"mappings\": [\n",
				"                        \"- => +\",\n",
				"                        \"2 => 3\"\n",
				"                    ]\n", "                }\n",
				"            }\n", "        }\n", "    }\n", "}"));

		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setCharFilters(Collections.singleton("custom_cf"));

		_assertExecuteAnalyzeIndexRequest(
			analyzeIndexRequest,
			"The 3 QUICK Brown+Foxes jumped over the lazy dog's bone.");
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithExplainAndAnalyzer() {
		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setAnalyzer("stop");
		analyzeIndexRequest.setExplain(true);
		analyzeIndexRequest.setIndexName(_INDEX_NAME);
		analyzeIndexRequest.setTexts(
			"The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.");

		AnalyzeIndexResponse analyzeIndexResponse =
			_searchEngineAdapter.execute(analyzeIndexRequest);

		AnalyzeIndexResponse.DetailsAnalyzer detailsAnalyzer =
			analyzeIndexResponse.getDetailsAnalyzer();

		Assert.assertEquals("stop", detailsAnalyzer.getAnalyzerName());

		_assertAnalysisIndexResponseTokens(
			detailsAnalyzer.getAnalysisIndexResponseTokens(),
			"quick,brown,foxes,jumped,over,lazy,dog,s,bone");
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithExplainAndWithoutAnalyzer() {
		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setExplain(true);
		analyzeIndexRequest.setIndexName(_INDEX_NAME);
		analyzeIndexRequest.setTexts(
			"The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.");
		analyzeIndexRequest.setTokenFilters(Collections.singleton("uppercase"));

		AnalyzeIndexResponse analyzeIndexResponse =
			_searchEngineAdapter.execute(analyzeIndexRequest);

		Assert.assertNull(analyzeIndexResponse.getDetailsAnalyzer());

		List<AnalyzeIndexResponse.DetailsTokenFilter> detailsTokenFilters =
			analyzeIndexResponse.getDetailsTokenFilters();

		Assert.assertEquals(
			detailsTokenFilters.toString(), 1, detailsTokenFilters.size());

		AnalyzeIndexResponse.DetailsTokenFilter detailsTokenFilter =
			detailsTokenFilters.get(0);

		Assert.assertEquals(
			"uppercase", detailsTokenFilter.getTokenFilterName());

		_assertAnalysisIndexResponseTokens(
			detailsTokenFilter.getAnalysisIndexResponseTokens(),
			"THE 2 QUICK BROWN-FOXES JUMPED OVER THE LAZY DOG'S BONE.");
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithFieldName() {
		String mappingName = "testAnalyze";
		String mappingSource =
			"{\"properties\":{\"keywordTestField\":{\"type\":\"keyword\"}}}";

		_putMapping(mappingName, mappingSource);

		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setFieldName("keywordTestField");

		_assertExecuteAnalyzeIndexRequest(
			analyzeIndexRequest,
			"The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.");
	}

	@Ignore
	@Test
	public void testExecuteAnalyzeIndexRequestWithNormalizer() {
		_putSettings(
			StringBundler.concat(
				"{\n", "    \"settings\": {\n", "        \"analysis\": {\n",
				"            \"normalizer\": {\n",
				"                \"custom_normalizer\": {\n",
				"                    \"type\": \"custom\",\n",
				"                    \"filter\": [\"uppercase\"]\n",
				"                }\n", "            }\n", "        }\n",
				"    }\n", "}"));

		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setNormalizer("custom_normalizer");

		// Elasticsearch bug? Calls AnalyzeRequest.withNormalizer()
		// Response contains tokens:
		// "the,2,quick,brown,foxes,jumped,over,the,lazy,dog's,bone"

		_assertExecuteAnalyzeIndexRequest(
			analyzeIndexRequest,
			"THE 2 QUICK BROWN-FOXES JUMPED OVER THE LAZY DOG'S BONE.");
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithTokenFilters() {
		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setTokenFilters(Collections.singleton("uppercase"));

		_assertExecuteAnalyzeIndexRequest(
			analyzeIndexRequest,
			"THE 2 QUICK BROWN-FOXES JUMPED OVER THE LAZY DOG'S BONE.");
	}

	@Test
	public void testExecuteAnalyzeIndexRequestWithTokenizer() {
		AnalyzeIndexRequest analyzeIndexRequest = new AnalyzeIndexRequest();

		analyzeIndexRequest.setTokenizer("letter");

		_assertExecuteAnalyzeIndexRequest(
			analyzeIndexRequest,
			"The,QUICK,Brown,Foxes,jumped,over,the,lazy,dog,s,bone");
	}

	@Test
	public void testExecuteCloseIndexRequest() {
		CloseIndexRequest closeIndexRequest = new CloseIndexRequest(
			_INDEX_NAME);

		IndicesOptions indicesOptions = new IndicesOptions();

		indicesOptions.setIgnoreUnavailable(true);

		closeIndexRequest.setIndicesOptions(indicesOptions);

		CloseIndexResponse closeIndexResponse = _searchEngineAdapter.execute(
			closeIndexRequest);

		Assert.assertTrue(
			"Close request not acknowledged",
			closeIndexResponse.isAcknowledged());

		_assertIndexMetadataState(_INDEX_NAME, IndexMetadata.State.CLOSE);
	}

	@Test
	public void testExecuteCreateIndexRequest() {
		CreateIndexRequest createIndexRequest = new CreateIndexRequest(
			"test_index_2");

		createIndexRequest.setSource(
			StringBundler.concat(
				"{\n", "    \"settings\": {\n",
				"        \"number_of_shards\": 1\n", "    },\n",
				"    \"mappings\": {\n", "        \"type1\": {\n",
				"            \"properties\": {\n",
				"                \"field1\": {\n",
				"                    \"type\": \"text\"\n",
				"                }\n", "            }\n", "        }\n",
				"    }\n", "}"));

		CreateIndexResponse createIndexResponse = _searchEngineAdapter.execute(
			createIndexRequest);

		Assert.assertTrue(createIndexResponse.isAcknowledged());

		Assert.assertEquals("test_index_2", createIndexResponse.getIndexName());

		Assert.assertTrue(_indiciesExists("test_index_2"));

		_deleteIndex("test_index_2");
	}

	@Test
	public void testExecuteDeleteIndexRequest() {
		_createIndex("test_index_2");

		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(
			"test_index_2");

		DeleteIndexResponse deleteIndexResponse = _searchEngineAdapter.execute(
			deleteIndexRequest);

		Assert.assertTrue(deleteIndexResponse.isAcknowledged());

		Assert.assertFalse(_indiciesExists("test_index_2"));
	}

	@Test
	public void testExecuteFlushIndexRequest() {
		FlushIndexRequest flushIndexRequest = new FlushIndexRequest(
			_INDEX_NAME);

		FlushIndexResponse flushIndexResponse = _searchEngineAdapter.execute(
			flushIndexRequest);

		Assert.assertEquals(0, flushIndexResponse.getFailedShards());
	}

	@Test
	public void testExecuteGetFieldMappingIndexRequest() throws Exception {
		String mappingName = "testGetFieldMapping";
		String mappingSource =
			"{\"properties\":{\"testField\":{\"type\":\"keyword\"}, " +
				"\"otherTestField\":{\"type\":\"keyword\"}}}";

		_putMapping(mappingName, mappingSource);

		String[] fields = {"otherTestField"};

		GetFieldMappingIndexRequest getFieldMappingIndexRequest =
			new GetFieldMappingIndexRequest(
				new String[] {_INDEX_NAME}, mappingName, fields);

		GetFieldMappingIndexResponse getFieldMappingIndexResponse =
			_searchEngineAdapter.execute(getFieldMappingIndexRequest);

		Map<String, String> fieldMappings =
			getFieldMappingIndexResponse.getFieldMappings();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			fieldMappings.get(_INDEX_NAME));

		String fieldMapping = jsonObject.getString("otherTestField");

		Assert.assertTrue(
			fieldMapping,
			fieldMapping.equals("{\"otherTestField\":{\"type\":\"keyword\"}}"));
	}

	@Test
	public void testExecuteGetIndexIndexRequest() {
		String mappingName = "testGetIndex";
		String mappingSource =
			"{\"properties\":{\"testField\":{\"type\":\"keyword\"}}}";

		_putMapping(mappingName, mappingSource);

		GetIndexIndexRequest getIndexIndexRequest = new GetIndexIndexRequest(
			_INDEX_NAME);

		GetIndexIndexResponse getIndexIndexResponse =
			_searchEngineAdapter.execute(getIndexIndexRequest);

		String[] indexNames = getIndexIndexResponse.getIndexNames();

		Assert.assertEquals(Arrays.toString(indexNames), 1, indexNames.length);
		Assert.assertEquals(_INDEX_NAME, indexNames[0]);

		String indexMappings = String.valueOf(
			getIndexIndexResponse.getMappings());

		Assert.assertTrue(indexMappings, indexMappings.contains(mappingSource));
	}

	@Test
	public void testExecuteGetMappingIndexRequest() {
		String mappingName = "testGetMapping";
		String mappingSource =
			"{\"properties\":{\"testField\":{\"type\":\"keyword\"}}}";

		_putMapping(mappingName, mappingSource);

		GetMappingIndexRequest getMappingIndexRequest =
			new GetMappingIndexRequest(new String[] {_INDEX_NAME}, mappingName);

		GetMappingIndexResponse getMappingIndexResponse =
			_searchEngineAdapter.execute(getMappingIndexRequest);

		String string = String.valueOf(
			getMappingIndexResponse.getIndexMappings());

		Assert.assertTrue(string.contains(mappingSource));
	}

	@Test
	public void testExecuteIndicesExistsIndexRequest() {
		IndicesExistsIndexRequest indicesExistsIndexRequest1 =
			new IndicesExistsIndexRequest(_INDEX_NAME);

		IndicesExistsIndexResponse indicesExistsIndexResponse1 =
			_searchEngineAdapter.execute(indicesExistsIndexRequest1);

		Assert.assertTrue(indicesExistsIndexResponse1.isExists());

		IndicesExistsIndexRequest indicesExistsIndexRequest2 =
			new IndicesExistsIndexRequest("test_index_2");

		IndicesExistsIndexResponse indicesExistsIndexResponse2 =
			_searchEngineAdapter.execute(indicesExistsIndexRequest2);

		Assert.assertFalse(indicesExistsIndexResponse2.isExists());
	}

	@Test
	public void testExecuteOpenIndexRequest() {
		_closeIndex(_INDEX_NAME);

		_assertIndexMetadataState(_INDEX_NAME, IndexMetadata.State.CLOSE);

		OpenIndexRequest openIndexRequest = new OpenIndexRequest(_INDEX_NAME);

		IndicesOptions indicesOptions = new IndicesOptions();

		indicesOptions.setIgnoreUnavailable(true);

		openIndexRequest.setIndicesOptions(indicesOptions);

		OpenIndexResponse openIndexResponse = _searchEngineAdapter.execute(
			openIndexRequest);

		Assert.assertTrue(
			"Open request not acknowledged",
			openIndexResponse.isAcknowledged());

		_assertIndexMetadataState(_INDEX_NAME, IndexMetadata.State.OPEN);
	}

	@Test
	public void testExecutePutMappingIndexRequest() {
		String mappingName = "testPutMapping";
		String mappingSource =
			"{\"properties\":{\"testField\":{\"type\":\"keyword\"}}}";

		PutMappingIndexRequest putMappingIndexRequest =
			new PutMappingIndexRequest(
				new String[] {_INDEX_NAME}, mappingName, mappingSource);

		PutMappingIndexResponse putMappingIndexResponse =
			_searchEngineAdapter.execute(putMappingIndexRequest);

		Assert.assertTrue(putMappingIndexResponse.isAcknowledged());

		GetMappingsResponse getMappingsResponse = _getGetMappingsResponse(
			_INDEX_NAME, mappingName);

		ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetadata>>
			immutableOpenMap1 = getMappingsResponse.getMappings();

		ImmutableOpenMap<String, MappingMetadata> immutableOpenMap2 =
			immutableOpenMap1.get(_INDEX_NAME);

		MappingMetadata mappingMetadata = immutableOpenMap2.get(mappingName);

		String mappingMetadataSource = String.valueOf(mappingMetadata.source());

		Assert.assertTrue(mappingMetadataSource.contains(mappingSource));
	}

	@Test
	public void testExecuteRefreshIndexRequest() {
		RefreshIndexRequest refreshIndexRequest = new RefreshIndexRequest(
			_INDEX_NAME);

		RefreshIndexResponse refreshIndexResponse =
			_searchEngineAdapter.execute(refreshIndexRequest);

		Assert.assertEquals(0, refreshIndexResponse.getFailedShards());
	}

	@Test
	public void testExecuteUpdateIndexSettingsIndexRequest() {
		_createIndex("test_index_2");

		UpdateIndexSettingsIndexRequest updateIndexSettingsIndexRequest =
			new UpdateIndexSettingsIndexRequest("test_index_2");

		updateIndexSettingsIndexRequest.setSettings(
			StringBundler.concat(
				"{\n", "    \"index\": {\n",
				"        \"refresh_interval\": \"2s\"\n", "    }\n", "}"));

		UpdateIndexSettingsIndexResponse indexSettingsIndexResponse =
			_searchEngineAdapter.execute(updateIndexSettingsIndexRequest);

		Assert.assertTrue(indexSettingsIndexResponse.isAcknowledged());

		GetSettingsResponse getSettingsResponse = _getGetSettingsResponse(
			"test_index_2");

		String refreshInterval = getSettingsResponse.getSetting(
			"test_index_2", "index.refresh_interval");

		Assert.assertEquals("2s", refreshInterval);

		_deleteIndex("test_index_2");
	}

	protected static SearchEngineAdapter createSearchEngineAdapter(
		ElasticsearchClientResolver elasticsearchClientResolver) {

		return new ElasticsearchSearchEngineAdapterImpl() {
			{
				setIndexRequestExecutor(
					_createIndexRequestExecutor(elasticsearchClientResolver));
			}
		};
	}

	private static IndexRequestExecutor _createIndexRequestExecutor(
		ElasticsearchClientResolver elasticsearchClientResolver) {

		IndexRequestExecutorFixture indexRequestExecutorFixture =
			new IndexRequestExecutorFixture() {
				{
					setElasticsearchClientResolver(elasticsearchClientResolver);
				}
			};

		indexRequestExecutorFixture.setUp();

		return indexRequestExecutorFixture.getIndexRequestExecutor();
	}

	private void _assertAnalysisIndexResponseTokens(
		List<AnalysisIndexResponseToken> analysisIndexResponseTokens,
		String expectedTokens) {

		List<String> actualTokensList = new ArrayList<>();

		for (AnalysisIndexResponseToken analysisIndexResponseToken :
				analysisIndexResponseTokens) {

			actualTokensList.add(analysisIndexResponseToken.getTerm());
		}

		String actualTokens = StringUtil.merge(
			actualTokensList, StringPool.COMMA);

		Assert.assertEquals(expectedTokens, expectedTokens, actualTokens);
	}

	private void _assertExecuteAnalyzeIndexRequest(
		AnalyzeIndexRequest analyzeIndexRequest, String expectedTokens) {

		analyzeIndexRequest.setIndexName(_INDEX_NAME);
		analyzeIndexRequest.setTexts(
			"The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.");

		AnalyzeIndexResponse analyzeIndexResponse =
			_searchEngineAdapter.execute(analyzeIndexRequest);

		_assertAnalysisIndexResponseTokens(
			analyzeIndexResponse.getAnalysisIndexResponseTokens(),
			expectedTokens);
	}

	private void _assertIndexMetadataState(
		String indexName, IndexMetadata.State indexMetadataState) {

		RestHighLevelClient restHighLevelClient =
			_elasticsearchFixture.getRestHighLevelClient();

		RestClient restLowLevelClient = restHighLevelClient.getLowLevelClient();

		Request request = new Request(
			"GET", "/_cluster/state/metadata/" + indexName);

		try {
			Response response = restLowLevelClient.performRequest(request);

			String responseBody = EntityUtils.toString(response.getEntity());

			JSONObject responseJSONObject = JSONFactoryUtil.createJSONObject(
				responseBody);

			JSONObject metadataJSONObject = responseJSONObject.getJSONObject(
				"metadata");

			JSONObject indicesJSONObject = metadataJSONObject.getJSONObject(
				"indices");

			JSONObject indexJSONObject = indicesJSONObject.getJSONObject(
				indexName);

			String state = GetterUtil.getString(indexJSONObject.get("state"));

			Assert.assertEquals(_translateState(indexMetadataState), state);
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	private void _closeIndex(String indexName) {
		org.elasticsearch.client.indices.CloseIndexRequest
			elasticsearchCloseIndexRequest =
				new org.elasticsearch.client.indices.CloseIndexRequest(
					indexName);

		try {
			_indicesClient.close(
				elasticsearchCloseIndexRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _createIndex(String indexName) {
		org.elasticsearch.client.indices.CreateIndexRequest
			elasticsearchCreateIndexRequest =
				new org.elasticsearch.client.indices.CreateIndexRequest(
					indexName);

		try {
			_indicesClient.create(
				elasticsearchCreateIndexRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private AcknowledgedResponse _deleteIndex(String indexName) {
		org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
			elasticsearchDeleteIndexRequest =
				new org.elasticsearch.action.admin.indices.delete.
					DeleteIndexRequest(indexName);

		try {
			return _indicesClient.delete(
				elasticsearchDeleteIndexRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private GetMappingsResponse _getGetMappingsResponse(
		String indexName, String mappingName) {

		GetMappingsRequest getMappingsRequest = new GetMappingsRequest();

		getMappingsRequest.indices(indexName);
		getMappingsRequest.types(mappingName);

		try {
			return _indicesClient.getMapping(
				getMappingsRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private GetSettingsResponse _getGetSettingsResponse(String indexName) {
		GetSettingsRequest getSettingsRequest = new GetSettingsRequest();

		getSettingsRequest.indices(indexName);

		try {
			return _indicesClient.getSettings(
				getSettingsRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private boolean _indiciesExists(String indexName) {
		GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);

		try {
			return _indicesClient.exists(
				getIndexRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _openIndex(String indexName) {
		org.elasticsearch.action.admin.indices.open.OpenIndexRequest
			elasticsearchOpenIndexRequest =
				new org.elasticsearch.action.admin.indices.open.
					OpenIndexRequest(indexName);

		try {
			_indicesClient.open(
				elasticsearchOpenIndexRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _putMapping(String mappingName, String mappingSource) {
		PutMappingRequest putMappingRequest = new PutMappingRequest(
			_INDEX_NAME);

		putMappingRequest.source(mappingSource, XContentType.JSON);
		putMappingRequest.type(mappingName);

		try {
			_indicesClient.putMapping(
				putMappingRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private void _putSettings(String settingsSource) {
		_closeIndex(_INDEX_NAME);

		UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest(
			_INDEX_NAME);

		updateSettingsRequest.settings(settingsSource, XContentType.JSON);

		try {
			_indicesClient.putSettings(
				updateSettingsRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}

		_openIndex(_INDEX_NAME);
	}

	private String _translateState(IndexMetadata.State state) {
		if (state == IndexMetadata.State.OPEN) {
			return "open";
		}

		if (state == IndexMetadata.State.CLOSE) {
			return "close";
		}

		throw new IllegalArgumentException("Unknown state: " + state);
	}

	private static final String _INDEX_NAME = "test_request_index";

	private static ElasticsearchFixture _elasticsearchFixture;
	private static IndicesClient _indicesClient;
	private static SearchEngineAdapter _searchEngineAdapter;

}