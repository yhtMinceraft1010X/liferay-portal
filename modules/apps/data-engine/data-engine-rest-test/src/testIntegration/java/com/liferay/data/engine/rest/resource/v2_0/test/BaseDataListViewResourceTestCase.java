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

package com.liferay.data.engine.rest.resource.v2_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.data.engine.rest.client.dto.v2_0.DataListView;
import com.liferay.data.engine.rest.client.http.HttpInvoker;
import com.liferay.data.engine.rest.client.pagination.Page;
import com.liferay.data.engine.rest.client.pagination.Pagination;
import com.liferay.data.engine.rest.client.resource.v2_0.DataListViewResource;
import com.liferay.data.engine.rest.client.serdes.v2_0.DataListViewSerDes;
import com.liferay.petra.function.UnsafeTriConsumer;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.Method;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.lang.time.DateUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Jeyvison Nascimento
 * @generated
 */
@Generated("")
public abstract class BaseDataListViewResourceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	@Before
	public void setUp() throws Exception {
		irrelevantGroup = GroupTestUtil.addGroup();
		testGroup = GroupTestUtil.addGroup();

		testCompany = CompanyLocalServiceUtil.getCompany(
			testGroup.getCompanyId());

		_dataListViewResource.setContextCompany(testCompany);

		DataListViewResource.Builder builder = DataListViewResource.builder();

		dataListViewResource = builder.authentication(
			"test@liferay.com", "test"
		).locale(
			LocaleUtil.getDefault()
		).build();
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testClientSerDesToDTO() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				enable(SerializationFeature.INDENT_OUTPUT);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		DataListView dataListView1 = randomDataListView();

		String json = objectMapper.writeValueAsString(dataListView1);

		DataListView dataListView2 = DataListViewSerDes.toDTO(json);

		Assert.assertTrue(equals(dataListView1, dataListView2));
	}

	@Test
	public void testClientSerDesToJSON() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		DataListView dataListView = randomDataListView();

		String json1 = objectMapper.writeValueAsString(dataListView);
		String json2 = DataListViewSerDes.toJSON(dataListView);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		DataListView dataListView = randomDataListView();

		dataListView.setSortField(regex);

		String json = DataListViewSerDes.toJSON(dataListView);

		Assert.assertFalse(json.contains(regex));

		dataListView = DataListViewSerDes.toDTO(json);

		Assert.assertEquals(regex, dataListView.getSortField());
	}

	@Test
	public void testDeleteDataDefinitionDataListView() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		DataListView dataListView =
			testDeleteDataDefinitionDataListView_addDataListView();

		assertHttpResponseStatusCode(
			204,
			dataListViewResource.deleteDataDefinitionDataListViewHttpResponse(
				dataListView.getDataDefinitionId()));
	}

	protected DataListView
			testDeleteDataDefinitionDataListView_addDataListView()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetDataDefinitionDataListViewsPage() throws Exception {
		Long dataDefinitionId =
			testGetDataDefinitionDataListViewsPage_getDataDefinitionId();
		Long irrelevantDataDefinitionId =
			testGetDataDefinitionDataListViewsPage_getIrrelevantDataDefinitionId();

		Page<DataListView> page =
			dataListViewResource.getDataDefinitionDataListViewsPage(
				dataDefinitionId, RandomTestUtil.randomString(),
				Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantDataDefinitionId != null) {
			DataListView irrelevantDataListView =
				testGetDataDefinitionDataListViewsPage_addDataListView(
					irrelevantDataDefinitionId, randomIrrelevantDataListView());

			page = dataListViewResource.getDataDefinitionDataListViewsPage(
				irrelevantDataDefinitionId, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantDataListView),
				(List<DataListView>)page.getItems());
			assertValid(page);
		}

		DataListView dataListView1 =
			testGetDataDefinitionDataListViewsPage_addDataListView(
				dataDefinitionId, randomDataListView());

		DataListView dataListView2 =
			testGetDataDefinitionDataListViewsPage_addDataListView(
				dataDefinitionId, randomDataListView());

		page = dataListViewResource.getDataDefinitionDataListViewsPage(
			dataDefinitionId, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dataListView1, dataListView2),
			(List<DataListView>)page.getItems());
		assertValid(page);

		dataListViewResource.deleteDataListView(dataListView1.getId());

		dataListViewResource.deleteDataListView(dataListView2.getId());
	}

	@Test
	public void testGetDataDefinitionDataListViewsPageWithPagination()
		throws Exception {

		Long dataDefinitionId =
			testGetDataDefinitionDataListViewsPage_getDataDefinitionId();

		DataListView dataListView1 =
			testGetDataDefinitionDataListViewsPage_addDataListView(
				dataDefinitionId, randomDataListView());

		DataListView dataListView2 =
			testGetDataDefinitionDataListViewsPage_addDataListView(
				dataDefinitionId, randomDataListView());

		DataListView dataListView3 =
			testGetDataDefinitionDataListViewsPage_addDataListView(
				dataDefinitionId, randomDataListView());

		Page<DataListView> page1 =
			dataListViewResource.getDataDefinitionDataListViewsPage(
				dataDefinitionId, null, Pagination.of(1, 2), null);

		List<DataListView> dataListViews1 =
			(List<DataListView>)page1.getItems();

		Assert.assertEquals(
			dataListViews1.toString(), 2, dataListViews1.size());

		Page<DataListView> page2 =
			dataListViewResource.getDataDefinitionDataListViewsPage(
				dataDefinitionId, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<DataListView> dataListViews2 =
			(List<DataListView>)page2.getItems();

		Assert.assertEquals(
			dataListViews2.toString(), 1, dataListViews2.size());

		Page<DataListView> page3 =
			dataListViewResource.getDataDefinitionDataListViewsPage(
				dataDefinitionId, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(dataListView1, dataListView2, dataListView3),
			(List<DataListView>)page3.getItems());
	}

	@Test
	public void testGetDataDefinitionDataListViewsPageWithSortDateTime()
		throws Exception {

		testGetDataDefinitionDataListViewsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, dataListView1, dataListView2) -> {
				BeanTestUtil.setProperty(
					dataListView1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetDataDefinitionDataListViewsPageWithSortDouble()
		throws Exception {

		testGetDataDefinitionDataListViewsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, dataListView1, dataListView2) -> {
				BeanTestUtil.setProperty(
					dataListView1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					dataListView2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetDataDefinitionDataListViewsPageWithSortInteger()
		throws Exception {

		testGetDataDefinitionDataListViewsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, dataListView1, dataListView2) -> {
				BeanTestUtil.setProperty(
					dataListView1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					dataListView2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetDataDefinitionDataListViewsPageWithSortString()
		throws Exception {

		testGetDataDefinitionDataListViewsPageWithSort(
			EntityField.Type.STRING,
			(entityField, dataListView1, dataListView2) -> {
				Class<?> clazz = dataListView1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						dataListView1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						dataListView2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						dataListView1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						dataListView2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						dataListView1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						dataListView2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetDataDefinitionDataListViewsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, DataListView, DataListView, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long dataDefinitionId =
			testGetDataDefinitionDataListViewsPage_getDataDefinitionId();

		DataListView dataListView1 = randomDataListView();
		DataListView dataListView2 = randomDataListView();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, dataListView1, dataListView2);
		}

		dataListView1 = testGetDataDefinitionDataListViewsPage_addDataListView(
			dataDefinitionId, dataListView1);

		dataListView2 = testGetDataDefinitionDataListViewsPage_addDataListView(
			dataDefinitionId, dataListView2);

		for (EntityField entityField : entityFields) {
			Page<DataListView> ascPage =
				dataListViewResource.getDataDefinitionDataListViewsPage(
					dataDefinitionId, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(dataListView1, dataListView2),
				(List<DataListView>)ascPage.getItems());

			Page<DataListView> descPage =
				dataListViewResource.getDataDefinitionDataListViewsPage(
					dataDefinitionId, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(dataListView2, dataListView1),
				(List<DataListView>)descPage.getItems());
		}
	}

	protected DataListView
			testGetDataDefinitionDataListViewsPage_addDataListView(
				Long dataDefinitionId, DataListView dataListView)
		throws Exception {

		return dataListViewResource.postDataDefinitionDataListView(
			dataDefinitionId, dataListView);
	}

	protected Long testGetDataDefinitionDataListViewsPage_getDataDefinitionId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetDataDefinitionDataListViewsPage_getIrrelevantDataDefinitionId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostDataDefinitionDataListView() throws Exception {
		DataListView randomDataListView = randomDataListView();

		DataListView postDataListView =
			testPostDataDefinitionDataListView_addDataListView(
				randomDataListView);

		assertEquals(randomDataListView, postDataListView);
		assertValid(postDataListView);
	}

	protected DataListView testPostDataDefinitionDataListView_addDataListView(
			DataListView dataListView)
		throws Exception {

		return dataListViewResource.postDataDefinitionDataListView(
			testGetDataDefinitionDataListViewsPage_getDataDefinitionId(),
			dataListView);
	}

	@Test
	public void testDeleteDataListView() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		DataListView dataListView = testDeleteDataListView_addDataListView();

		assertHttpResponseStatusCode(
			204,
			dataListViewResource.deleteDataListViewHttpResponse(
				dataListView.getId()));

		assertHttpResponseStatusCode(
			404,
			dataListViewResource.getDataListViewHttpResponse(
				dataListView.getId()));

		assertHttpResponseStatusCode(
			404, dataListViewResource.getDataListViewHttpResponse(0L));
	}

	protected DataListView testDeleteDataListView_addDataListView()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteDataListView() throws Exception {
		DataListView dataListView =
			testGraphQLDeleteDataListView_addDataListView();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteDataListView",
						new HashMap<String, Object>() {
							{
								put("dataListViewId", dataListView.getId());
							}
						})),
				"JSONObject/data", "Object/deleteDataListView"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"dataListView",
					new HashMap<String, Object>() {
						{
							put("dataListViewId", dataListView.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected DataListView testGraphQLDeleteDataListView_addDataListView()
		throws Exception {

		return testGraphQLDataListView_addDataListView();
	}

	@Test
	public void testGetDataListView() throws Exception {
		DataListView postDataListView = testGetDataListView_addDataListView();

		DataListView getDataListView = dataListViewResource.getDataListView(
			postDataListView.getId());

		assertEquals(postDataListView, getDataListView);
		assertValid(getDataListView);
	}

	protected DataListView testGetDataListView_addDataListView()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetDataListView() throws Exception {
		DataListView dataListView =
			testGraphQLGetDataListView_addDataListView();

		Assert.assertTrue(
			equals(
				dataListView,
				DataListViewSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"dataListView",
								new HashMap<String, Object>() {
									{
										put(
											"dataListViewId",
											dataListView.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/dataListView"))));
	}

	@Test
	public void testGraphQLGetDataListViewNotFound() throws Exception {
		Long irrelevantDataListViewId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"dataListView",
						new HashMap<String, Object>() {
							{
								put("dataListViewId", irrelevantDataListViewId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected DataListView testGraphQLGetDataListView_addDataListView()
		throws Exception {

		return testGraphQLDataListView_addDataListView();
	}

	@Test
	public void testPutDataListView() throws Exception {
		DataListView postDataListView = testPutDataListView_addDataListView();

		DataListView randomDataListView = randomDataListView();

		DataListView putDataListView = dataListViewResource.putDataListView(
			postDataListView.getId(), randomDataListView);

		assertEquals(randomDataListView, putDataListView);
		assertValid(putDataListView);

		DataListView getDataListView = dataListViewResource.getDataListView(
			putDataListView.getId());

		assertEquals(randomDataListView, getDataListView);
		assertValid(getDataListView);
	}

	protected DataListView testPutDataListView_addDataListView()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected DataListView testGraphQLDataListView_addDataListView()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		DataListView dataListView, List<DataListView> dataListViews) {

		boolean contains = false;

		for (DataListView item : dataListViews) {
			if (equals(dataListView, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			dataListViews + " does not contain " + dataListView, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		DataListView dataListView1, DataListView dataListView2) {

		Assert.assertTrue(
			dataListView1 + " does not equal " + dataListView2,
			equals(dataListView1, dataListView2));
	}

	protected void assertEquals(
		List<DataListView> dataListViews1, List<DataListView> dataListViews2) {

		Assert.assertEquals(dataListViews1.size(), dataListViews2.size());

		for (int i = 0; i < dataListViews1.size(); i++) {
			DataListView dataListView1 = dataListViews1.get(i);
			DataListView dataListView2 = dataListViews2.get(i);

			assertEquals(dataListView1, dataListView2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<DataListView> dataListViews1, List<DataListView> dataListViews2) {

		Assert.assertEquals(dataListViews1.size(), dataListViews2.size());

		for (DataListView dataListView1 : dataListViews1) {
			boolean contains = false;

			for (DataListView dataListView2 : dataListViews2) {
				if (equals(dataListView1, dataListView2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				dataListViews2 + " does not contain " + dataListView1,
				contains);
		}
	}

	protected void assertValid(DataListView dataListView) throws Exception {
		boolean valid = true;

		if (dataListView.getDateCreated() == null) {
			valid = false;
		}

		if (dataListView.getDateModified() == null) {
			valid = false;
		}

		if (dataListView.getId() == null) {
			valid = false;
		}

		if (!Objects.equals(dataListView.getSiteId(), testGroup.getGroupId())) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("appliedFilters", additionalAssertFieldName)) {
				if (dataListView.getAppliedFilters() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("dataDefinitionId", additionalAssertFieldName)) {
				if (dataListView.getDataDefinitionId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("fieldNames", additionalAssertFieldName)) {
				if (dataListView.getFieldNames() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (dataListView.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("sortField", additionalAssertFieldName)) {
				if (dataListView.getSortField() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("userId", additionalAssertFieldName)) {
				if (dataListView.getUserId() == null) {
					valid = false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		Assert.assertTrue(valid);
	}

	protected void assertValid(Page<DataListView> page) {
		boolean valid = false;

		java.util.Collection<DataListView> dataListViews = page.getItems();

		int size = dataListViews.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	protected List<GraphQLField> getGraphQLFields() throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		graphQLFields.add(new GraphQLField("siteId"));

		for (java.lang.reflect.Field field :
				getDeclaredFields(
					com.liferay.data.engine.rest.dto.v2_0.DataListView.class)) {

			if (!ArrayUtil.contains(
					getAdditionalAssertFieldNames(), field.getName())) {

				continue;
			}

			graphQLFields.addAll(getGraphQLFields(field));
		}

		return graphQLFields;
	}

	protected List<GraphQLField> getGraphQLFields(
			java.lang.reflect.Field... fields)
		throws Exception {

		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (java.lang.reflect.Field field : fields) {
			com.liferay.portal.vulcan.graphql.annotation.GraphQLField
				vulcanGraphQLField = field.getAnnotation(
					com.liferay.portal.vulcan.graphql.annotation.GraphQLField.
						class);

			if (vulcanGraphQLField != null) {
				Class<?> clazz = field.getType();

				if (clazz.isArray()) {
					clazz = clazz.getComponentType();
				}

				List<GraphQLField> childrenGraphQLFields = getGraphQLFields(
					getDeclaredFields(clazz));

				graphQLFields.add(
					new GraphQLField(field.getName(), childrenGraphQLFields));
			}
		}

		return graphQLFields;
	}

	protected String[] getIgnoredEntityFieldNames() {
		return new String[0];
	}

	protected boolean equals(
		DataListView dataListView1, DataListView dataListView2) {

		if (dataListView1 == dataListView2) {
			return true;
		}

		if (!Objects.equals(
				dataListView1.getSiteId(), dataListView2.getSiteId())) {

			return false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("appliedFilters", additionalAssertFieldName)) {
				if (!equals(
						(Map)dataListView1.getAppliedFilters(),
						(Map)dataListView2.getAppliedFilters())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dataDefinitionId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dataListView1.getDataDefinitionId(),
						dataListView2.getDataDefinitionId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateCreated", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dataListView1.getDateCreated(),
						dataListView2.getDateCreated())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dataListView1.getDateModified(),
						dataListView2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("fieldNames", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dataListView1.getFieldNames(),
						dataListView2.getFieldNames())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dataListView1.getId(), dataListView2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!equals(
						(Map)dataListView1.getName(),
						(Map)dataListView2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("sortField", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dataListView1.getSortField(),
						dataListView2.getSortField())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("userId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dataListView1.getUserId(), dataListView2.getUserId())) {

					return false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		return true;
	}

	protected boolean equals(
		Map<String, Object> map1, Map<String, Object> map2) {

		if (Objects.equals(map1.keySet(), map2.keySet())) {
			for (Map.Entry<String, Object> entry : map1.entrySet()) {
				if (entry.getValue() instanceof Map) {
					if (!equals(
							(Map)entry.getValue(),
							(Map)map2.get(entry.getKey()))) {

						return false;
					}
				}
				else if (!Objects.deepEquals(
							entry.getValue(), map2.get(entry.getKey()))) {

					return false;
				}
			}

			return true;
		}

		return false;
	}

	protected java.lang.reflect.Field[] getDeclaredFields(Class clazz)
		throws Exception {

		Stream<java.lang.reflect.Field> stream = Stream.of(
			ReflectionUtil.getDeclaredFields(clazz));

		return stream.filter(
			field -> !field.isSynthetic()
		).toArray(
			java.lang.reflect.Field[]::new
		);
	}

	protected java.util.Collection<EntityField> getEntityFields()
		throws Exception {

		if (!(_dataListViewResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_dataListViewResource;

		EntityModel entityModel = entityModelResource.getEntityModel(
			new MultivaluedHashMap());

		Map<String, EntityField> entityFieldsMap =
			entityModel.getEntityFieldsMap();

		return entityFieldsMap.values();
	}

	protected List<EntityField> getEntityFields(EntityField.Type type)
		throws Exception {

		java.util.Collection<EntityField> entityFields = getEntityFields();

		Stream<EntityField> stream = entityFields.stream();

		return stream.filter(
			entityField ->
				Objects.equals(entityField.getType(), type) &&
				!ArrayUtil.contains(
					getIgnoredEntityFieldNames(), entityField.getName())
		).collect(
			Collectors.toList()
		);
	}

	protected String getFilterString(
		EntityField entityField, String operator, DataListView dataListView) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("appliedFilters")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("dataDefinitionId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("dateCreated")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							dataListView.getDateCreated(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							dataListView.getDateCreated(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(dataListView.getDateCreated()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("dateModified")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							dataListView.getDateModified(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							dataListView.getDateModified(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(dataListView.getDateModified()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("fieldNames")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("siteId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("sortField")) {
			sb.append("'");
			sb.append(String.valueOf(dataListView.getSortField()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("userId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected String invoke(String query) throws Exception {
		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.body(
			JSONUtil.put(
				"query", query
			).toString(),
			"application/json");
		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);
		httpInvoker.path("http://localhost:8080/o/graphql");
		httpInvoker.userNameAndPassword("test@liferay.com:test");

		HttpInvoker.HttpResponse httpResponse = httpInvoker.invoke();

		return httpResponse.getContent();
	}

	protected JSONObject invokeGraphQLMutation(GraphQLField graphQLField)
		throws Exception {

		GraphQLField mutationGraphQLField = new GraphQLField(
			"mutation", graphQLField);

		return JSONFactoryUtil.createJSONObject(
			invoke(mutationGraphQLField.toString()));
	}

	protected JSONObject invokeGraphQLQuery(GraphQLField graphQLField)
		throws Exception {

		GraphQLField queryGraphQLField = new GraphQLField(
			"query", graphQLField);

		return JSONFactoryUtil.createJSONObject(
			invoke(queryGraphQLField.toString()));
	}

	protected DataListView randomDataListView() throws Exception {
		return new DataListView() {
			{
				dataDefinitionId = RandomTestUtil.randomLong();
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				id = RandomTestUtil.randomLong();
				siteId = testGroup.getGroupId();
				sortField = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				userId = RandomTestUtil.randomLong();
			}
		};
	}

	protected DataListView randomIrrelevantDataListView() throws Exception {
		DataListView randomIrrelevantDataListView = randomDataListView();

		randomIrrelevantDataListView.setSiteId(irrelevantGroup.getGroupId());

		return randomIrrelevantDataListView;
	}

	protected DataListView randomPatchDataListView() throws Exception {
		return randomDataListView();
	}

	protected DataListViewResource dataListViewResource;
	protected Group irrelevantGroup;
	protected Company testCompany;
	protected Group testGroup;

	protected static class BeanTestUtil {

		public static void copyProperties(Object source, Object target)
			throws Exception {

			Class<?> sourceClass = _getSuperClass(source.getClass());

			Class<?> targetClass = target.getClass();

			for (java.lang.reflect.Field field :
					sourceClass.getDeclaredFields()) {

				if (field.isSynthetic()) {
					continue;
				}

				Method getMethod = _getMethod(
					sourceClass, field.getName(), "get");

				Method setMethod = _getMethod(
					targetClass, field.getName(), "set",
					getMethod.getReturnType());

				setMethod.invoke(target, getMethod.invoke(source));
			}
		}

		public static boolean hasProperty(Object bean, String name) {
			Method setMethod = _getMethod(
				bean.getClass(), "set" + StringUtil.upperCaseFirstLetter(name));

			if (setMethod != null) {
				return true;
			}

			return false;
		}

		public static void setProperty(Object bean, String name, Object value)
			throws Exception {

			Class<?> clazz = bean.getClass();

			Method setMethod = _getMethod(
				clazz, "set" + StringUtil.upperCaseFirstLetter(name));

			if (setMethod == null) {
				throw new NoSuchMethodException();
			}

			Class<?>[] parameterTypes = setMethod.getParameterTypes();

			setMethod.invoke(bean, _translateValue(parameterTypes[0], value));
		}

		private static Method _getMethod(Class<?> clazz, String name) {
			for (Method method : clazz.getMethods()) {
				if (name.equals(method.getName()) &&
					(method.getParameterCount() == 1) &&
					_parameterTypes.contains(method.getParameterTypes()[0])) {

					return method;
				}
			}

			return null;
		}

		private static Method _getMethod(
				Class<?> clazz, String fieldName, String prefix,
				Class<?>... parameterTypes)
			throws Exception {

			return clazz.getMethod(
				prefix + StringUtil.upperCaseFirstLetter(fieldName),
				parameterTypes);
		}

		private static Class<?> _getSuperClass(Class<?> clazz) {
			Class<?> superClass = clazz.getSuperclass();

			if ((superClass == null) || (superClass == Object.class)) {
				return clazz;
			}

			return superClass;
		}

		private static Object _translateValue(
			Class<?> parameterType, Object value) {

			if ((value instanceof Integer) &&
				parameterType.equals(Long.class)) {

				Integer intValue = (Integer)value;

				return intValue.longValue();
			}

			return value;
		}

		private static final Set<Class<?>> _parameterTypes = new HashSet<>(
			Arrays.asList(
				Boolean.class, Date.class, Double.class, Integer.class,
				Long.class, Map.class, String.class));

	}

	protected class GraphQLField {

		public GraphQLField(String key, GraphQLField... graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(String key, List<GraphQLField> graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			GraphQLField... graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = Arrays.asList(graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			List<GraphQLField> graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = graphQLFields;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(_key);

			if (!_parameterMap.isEmpty()) {
				sb.append("(");

				for (Map.Entry<String, Object> entry :
						_parameterMap.entrySet()) {

					sb.append(entry.getKey());
					sb.append(": ");
					sb.append(entry.getValue());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append(")");
			}

			if (!_graphQLFields.isEmpty()) {
				sb.append("{");

				for (GraphQLField graphQLField : _graphQLFields) {
					sb.append(graphQLField.toString());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append("}");
			}

			return sb.toString();
		}

		private final List<GraphQLField> _graphQLFields;
		private final String _key;
		private final Map<String, Object> _parameterMap;

	}

	private static final com.liferay.portal.kernel.log.Log _log =
		LogFactoryUtil.getLog(BaseDataListViewResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.data.engine.rest.resource.v2_0.DataListViewResource
		_dataListViewResource;

}