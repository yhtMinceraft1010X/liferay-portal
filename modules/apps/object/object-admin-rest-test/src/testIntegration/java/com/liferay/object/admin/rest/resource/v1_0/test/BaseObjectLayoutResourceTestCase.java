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

package com.liferay.object.admin.rest.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.object.admin.rest.client.dto.v1_0.ObjectLayout;
import com.liferay.object.admin.rest.client.http.HttpInvoker;
import com.liferay.object.admin.rest.client.pagination.Page;
import com.liferay.object.admin.rest.client.pagination.Pagination;
import com.liferay.object.admin.rest.client.resource.v1_0.ObjectLayoutResource;
import com.liferay.object.admin.rest.client.serdes.v1_0.ObjectLayoutSerDes;
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
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseObjectLayoutResourceTestCase {

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

		_objectLayoutResource.setContextCompany(testCompany);

		ObjectLayoutResource.Builder builder = ObjectLayoutResource.builder();

		objectLayoutResource = builder.authentication(
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

		ObjectLayout objectLayout1 = randomObjectLayout();

		String json = objectMapper.writeValueAsString(objectLayout1);

		ObjectLayout objectLayout2 = ObjectLayoutSerDes.toDTO(json);

		Assert.assertTrue(equals(objectLayout1, objectLayout2));
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

		ObjectLayout objectLayout = randomObjectLayout();

		String json1 = objectMapper.writeValueAsString(objectLayout);
		String json2 = ObjectLayoutSerDes.toJSON(objectLayout);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ObjectLayout objectLayout = randomObjectLayout();

		String json = ObjectLayoutSerDes.toJSON(objectLayout);

		Assert.assertFalse(json.contains(regex));

		objectLayout = ObjectLayoutSerDes.toDTO(json);
	}

	@Test
	public void testGetObjectDefinitionObjectLayoutsPage() throws Exception {
		Long objectDefinitionId =
			testGetObjectDefinitionObjectLayoutsPage_getObjectDefinitionId();
		Long irrelevantObjectDefinitionId =
			testGetObjectDefinitionObjectLayoutsPage_getIrrelevantObjectDefinitionId();

		Page<ObjectLayout> page =
			objectLayoutResource.getObjectDefinitionObjectLayoutsPage(
				objectDefinitionId, null, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantObjectDefinitionId != null) {
			ObjectLayout irrelevantObjectLayout =
				testGetObjectDefinitionObjectLayoutsPage_addObjectLayout(
					irrelevantObjectDefinitionId,
					randomIrrelevantObjectLayout());

			page = objectLayoutResource.getObjectDefinitionObjectLayoutsPage(
				irrelevantObjectDefinitionId, null, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantObjectLayout),
				(List<ObjectLayout>)page.getItems());
			assertValid(page);
		}

		ObjectLayout objectLayout1 =
			testGetObjectDefinitionObjectLayoutsPage_addObjectLayout(
				objectDefinitionId, randomObjectLayout());

		ObjectLayout objectLayout2 =
			testGetObjectDefinitionObjectLayoutsPage_addObjectLayout(
				objectDefinitionId, randomObjectLayout());

		page = objectLayoutResource.getObjectDefinitionObjectLayoutsPage(
			objectDefinitionId, null, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(objectLayout1, objectLayout2),
			(List<ObjectLayout>)page.getItems());
		assertValid(page);

		objectLayoutResource.deleteObjectLayout(objectLayout1.getId());

		objectLayoutResource.deleteObjectLayout(objectLayout2.getId());
	}

	@Test
	public void testGetObjectDefinitionObjectLayoutsPageWithPagination()
		throws Exception {

		Long objectDefinitionId =
			testGetObjectDefinitionObjectLayoutsPage_getObjectDefinitionId();

		ObjectLayout objectLayout1 =
			testGetObjectDefinitionObjectLayoutsPage_addObjectLayout(
				objectDefinitionId, randomObjectLayout());

		ObjectLayout objectLayout2 =
			testGetObjectDefinitionObjectLayoutsPage_addObjectLayout(
				objectDefinitionId, randomObjectLayout());

		ObjectLayout objectLayout3 =
			testGetObjectDefinitionObjectLayoutsPage_addObjectLayout(
				objectDefinitionId, randomObjectLayout());

		Page<ObjectLayout> page1 =
			objectLayoutResource.getObjectDefinitionObjectLayoutsPage(
				objectDefinitionId, null, Pagination.of(1, 2));

		List<ObjectLayout> objectLayouts1 =
			(List<ObjectLayout>)page1.getItems();

		Assert.assertEquals(
			objectLayouts1.toString(), 2, objectLayouts1.size());

		Page<ObjectLayout> page2 =
			objectLayoutResource.getObjectDefinitionObjectLayoutsPage(
				objectDefinitionId, null, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<ObjectLayout> objectLayouts2 =
			(List<ObjectLayout>)page2.getItems();

		Assert.assertEquals(
			objectLayouts2.toString(), 1, objectLayouts2.size());

		Page<ObjectLayout> page3 =
			objectLayoutResource.getObjectDefinitionObjectLayoutsPage(
				objectDefinitionId, null, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(objectLayout1, objectLayout2, objectLayout3),
			(List<ObjectLayout>)page3.getItems());
	}

	protected ObjectLayout
			testGetObjectDefinitionObjectLayoutsPage_addObjectLayout(
				Long objectDefinitionId, ObjectLayout objectLayout)
		throws Exception {

		return objectLayoutResource.postObjectDefinitionObjectLayout(
			objectDefinitionId, objectLayout);
	}

	protected Long
			testGetObjectDefinitionObjectLayoutsPage_getObjectDefinitionId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetObjectDefinitionObjectLayoutsPage_getIrrelevantObjectDefinitionId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostObjectDefinitionObjectLayout() throws Exception {
		ObjectLayout randomObjectLayout = randomObjectLayout();

		ObjectLayout postObjectLayout =
			testPostObjectDefinitionObjectLayout_addObjectLayout(
				randomObjectLayout);

		assertEquals(randomObjectLayout, postObjectLayout);
		assertValid(postObjectLayout);
	}

	protected ObjectLayout testPostObjectDefinitionObjectLayout_addObjectLayout(
			ObjectLayout objectLayout)
		throws Exception {

		return objectLayoutResource.postObjectDefinitionObjectLayout(
			testGetObjectDefinitionObjectLayoutsPage_getObjectDefinitionId(),
			objectLayout);
	}

	@Test
	public void testDeleteObjectLayout() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		ObjectLayout objectLayout = testDeleteObjectLayout_addObjectLayout();

		assertHttpResponseStatusCode(
			204,
			objectLayoutResource.deleteObjectLayoutHttpResponse(
				objectLayout.getId()));

		assertHttpResponseStatusCode(
			404,
			objectLayoutResource.getObjectLayoutHttpResponse(
				objectLayout.getId()));

		assertHttpResponseStatusCode(
			404, objectLayoutResource.getObjectLayoutHttpResponse(0L));
	}

	protected ObjectLayout testDeleteObjectLayout_addObjectLayout()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteObjectLayout() throws Exception {
		ObjectLayout objectLayout =
			testGraphQLDeleteObjectLayout_addObjectLayout();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteObjectLayout",
						new HashMap<String, Object>() {
							{
								put("objectLayoutId", objectLayout.getId());
							}
						})),
				"JSONObject/data", "Object/deleteObjectLayout"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"objectLayout",
					new HashMap<String, Object>() {
						{
							put("objectLayoutId", objectLayout.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected ObjectLayout testGraphQLDeleteObjectLayout_addObjectLayout()
		throws Exception {

		return testGraphQLObjectLayout_addObjectLayout();
	}

	@Test
	public void testGetObjectLayout() throws Exception {
		ObjectLayout postObjectLayout = testGetObjectLayout_addObjectLayout();

		ObjectLayout getObjectLayout = objectLayoutResource.getObjectLayout(
			postObjectLayout.getId());

		assertEquals(postObjectLayout, getObjectLayout);
		assertValid(getObjectLayout);
	}

	protected ObjectLayout testGetObjectLayout_addObjectLayout()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetObjectLayout() throws Exception {
		ObjectLayout objectLayout =
			testGraphQLGetObjectLayout_addObjectLayout();

		Assert.assertTrue(
			equals(
				objectLayout,
				ObjectLayoutSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"objectLayout",
								new HashMap<String, Object>() {
									{
										put(
											"objectLayoutId",
											objectLayout.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/objectLayout"))));
	}

	@Test
	public void testGraphQLGetObjectLayoutNotFound() throws Exception {
		Long irrelevantObjectLayoutId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"objectLayout",
						new HashMap<String, Object>() {
							{
								put("objectLayoutId", irrelevantObjectLayoutId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected ObjectLayout testGraphQLGetObjectLayout_addObjectLayout()
		throws Exception {

		return testGraphQLObjectLayout_addObjectLayout();
	}

	@Test
	public void testPutObjectLayout() throws Exception {
		ObjectLayout postObjectLayout = testPutObjectLayout_addObjectLayout();

		ObjectLayout randomObjectLayout = randomObjectLayout();

		ObjectLayout putObjectLayout = objectLayoutResource.putObjectLayout(
			postObjectLayout.getId(), randomObjectLayout);

		assertEquals(randomObjectLayout, putObjectLayout);
		assertValid(putObjectLayout);

		ObjectLayout getObjectLayout = objectLayoutResource.getObjectLayout(
			putObjectLayout.getId());

		assertEquals(randomObjectLayout, getObjectLayout);
		assertValid(getObjectLayout);
	}

	protected ObjectLayout testPutObjectLayout_addObjectLayout()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected ObjectLayout testGraphQLObjectLayout_addObjectLayout()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		ObjectLayout objectLayout, List<ObjectLayout> objectLayouts) {

		boolean contains = false;

		for (ObjectLayout item : objectLayouts) {
			if (equals(objectLayout, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			objectLayouts + " does not contain " + objectLayout, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ObjectLayout objectLayout1, ObjectLayout objectLayout2) {

		Assert.assertTrue(
			objectLayout1 + " does not equal " + objectLayout2,
			equals(objectLayout1, objectLayout2));
	}

	protected void assertEquals(
		List<ObjectLayout> objectLayouts1, List<ObjectLayout> objectLayouts2) {

		Assert.assertEquals(objectLayouts1.size(), objectLayouts2.size());

		for (int i = 0; i < objectLayouts1.size(); i++) {
			ObjectLayout objectLayout1 = objectLayouts1.get(i);
			ObjectLayout objectLayout2 = objectLayouts2.get(i);

			assertEquals(objectLayout1, objectLayout2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ObjectLayout> objectLayouts1, List<ObjectLayout> objectLayouts2) {

		Assert.assertEquals(objectLayouts1.size(), objectLayouts2.size());

		for (ObjectLayout objectLayout1 : objectLayouts1) {
			boolean contains = false;

			for (ObjectLayout objectLayout2 : objectLayouts2) {
				if (equals(objectLayout1, objectLayout2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				objectLayouts2 + " does not contain " + objectLayout1,
				contains);
		}
	}

	protected void assertValid(ObjectLayout objectLayout) throws Exception {
		boolean valid = true;

		if (objectLayout.getDateCreated() == null) {
			valid = false;
		}

		if (objectLayout.getDateModified() == null) {
			valid = false;
		}

		if (objectLayout.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (objectLayout.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"defaultObjectLayout", additionalAssertFieldName)) {

				if (objectLayout.getDefaultObjectLayout() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (objectLayout.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"objectDefinitionId", additionalAssertFieldName)) {

				if (objectLayout.getObjectDefinitionId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("objectLayoutTabs", additionalAssertFieldName)) {
				if (objectLayout.getObjectLayoutTabs() == null) {
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

	protected void assertValid(Page<ObjectLayout> page) {
		boolean valid = false;

		java.util.Collection<ObjectLayout> objectLayouts = page.getItems();

		int size = objectLayouts.size();

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

		for (java.lang.reflect.Field field :
				getDeclaredFields(
					com.liferay.object.admin.rest.dto.v1_0.ObjectLayout.
						class)) {

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
		ObjectLayout objectLayout1, ObjectLayout objectLayout2) {

		if (objectLayout1 == objectLayout2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)objectLayout1.getActions(),
						(Map)objectLayout2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateCreated", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectLayout1.getDateCreated(),
						objectLayout2.getDateCreated())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectLayout1.getDateModified(),
						objectLayout2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"defaultObjectLayout", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectLayout1.getDefaultObjectLayout(),
						objectLayout2.getDefaultObjectLayout())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectLayout1.getId(), objectLayout2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!equals(
						(Map)objectLayout1.getName(),
						(Map)objectLayout2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"objectDefinitionId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectLayout1.getObjectDefinitionId(),
						objectLayout2.getObjectDefinitionId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("objectLayoutTabs", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectLayout1.getObjectLayoutTabs(),
						objectLayout2.getObjectLayoutTabs())) {

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

		if (!(_objectLayoutResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_objectLayoutResource;

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
		EntityField entityField, String operator, ObjectLayout objectLayout) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("actions")) {
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
							objectLayout.getDateCreated(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							objectLayout.getDateCreated(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(objectLayout.getDateCreated()));
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
							objectLayout.getDateModified(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							objectLayout.getDateModified(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(objectLayout.getDateModified()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("defaultObjectLayout")) {
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

		if (entityFieldName.equals("objectDefinitionId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("objectLayoutTabs")) {
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

	protected ObjectLayout randomObjectLayout() throws Exception {
		return new ObjectLayout() {
			{
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				defaultObjectLayout = RandomTestUtil.randomBoolean();
				id = RandomTestUtil.randomLong();
				objectDefinitionId = RandomTestUtil.randomLong();
			}
		};
	}

	protected ObjectLayout randomIrrelevantObjectLayout() throws Exception {
		ObjectLayout randomIrrelevantObjectLayout = randomObjectLayout();

		return randomIrrelevantObjectLayout;
	}

	protected ObjectLayout randomPatchObjectLayout() throws Exception {
		return randomObjectLayout();
	}

	protected ObjectLayoutResource objectLayoutResource;
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
		LogFactoryUtil.getLog(BaseObjectLayoutResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.object.admin.rest.resource.v1_0.ObjectLayoutResource
		_objectLayoutResource;

}