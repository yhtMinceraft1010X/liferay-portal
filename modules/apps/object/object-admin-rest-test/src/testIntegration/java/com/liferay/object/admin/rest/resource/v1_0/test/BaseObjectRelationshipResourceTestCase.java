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

import com.liferay.object.admin.rest.client.dto.v1_0.ObjectRelationship;
import com.liferay.object.admin.rest.client.http.HttpInvoker;
import com.liferay.object.admin.rest.client.pagination.Page;
import com.liferay.object.admin.rest.client.pagination.Pagination;
import com.liferay.object.admin.rest.client.resource.v1_0.ObjectRelationshipResource;
import com.liferay.object.admin.rest.client.serdes.v1_0.ObjectRelationshipSerDes;
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
import com.liferay.portal.search.test.util.SearchTestRule;
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
public abstract class BaseObjectRelationshipResourceTestCase {

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

		_objectRelationshipResource.setContextCompany(testCompany);

		ObjectRelationshipResource.Builder builder =
			ObjectRelationshipResource.builder();

		objectRelationshipResource = builder.authentication(
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

		ObjectRelationship objectRelationship1 = randomObjectRelationship();

		String json = objectMapper.writeValueAsString(objectRelationship1);

		ObjectRelationship objectRelationship2 = ObjectRelationshipSerDes.toDTO(
			json);

		Assert.assertTrue(equals(objectRelationship1, objectRelationship2));
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

		ObjectRelationship objectRelationship = randomObjectRelationship();

		String json1 = objectMapper.writeValueAsString(objectRelationship);
		String json2 = ObjectRelationshipSerDes.toJSON(objectRelationship);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ObjectRelationship objectRelationship = randomObjectRelationship();

		objectRelationship.setName(regex);
		objectRelationship.setObjectDefinitionName2(regex);

		String json = ObjectRelationshipSerDes.toJSON(objectRelationship);

		Assert.assertFalse(json.contains(regex));

		objectRelationship = ObjectRelationshipSerDes.toDTO(json);

		Assert.assertEquals(regex, objectRelationship.getName());
		Assert.assertEquals(
			regex, objectRelationship.getObjectDefinitionName2());
	}

	@Test
	public void testGetObjectDefinitionObjectRelationshipsPage()
		throws Exception {

		Long objectDefinitionId =
			testGetObjectDefinitionObjectRelationshipsPage_getObjectDefinitionId();
		Long irrelevantObjectDefinitionId =
			testGetObjectDefinitionObjectRelationshipsPage_getIrrelevantObjectDefinitionId();

		Page<ObjectRelationship> page =
			objectRelationshipResource.
				getObjectDefinitionObjectRelationshipsPage(
					objectDefinitionId, null, null, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantObjectDefinitionId != null) {
			ObjectRelationship irrelevantObjectRelationship =
				testGetObjectDefinitionObjectRelationshipsPage_addObjectRelationship(
					irrelevantObjectDefinitionId,
					randomIrrelevantObjectRelationship());

			page =
				objectRelationshipResource.
					getObjectDefinitionObjectRelationshipsPage(
						irrelevantObjectDefinitionId, null, null,
						Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantObjectRelationship),
				(List<ObjectRelationship>)page.getItems());
			assertValid(page);
		}

		ObjectRelationship objectRelationship1 =
			testGetObjectDefinitionObjectRelationshipsPage_addObjectRelationship(
				objectDefinitionId, randomObjectRelationship());

		ObjectRelationship objectRelationship2 =
			testGetObjectDefinitionObjectRelationshipsPage_addObjectRelationship(
				objectDefinitionId, randomObjectRelationship());

		page =
			objectRelationshipResource.
				getObjectDefinitionObjectRelationshipsPage(
					objectDefinitionId, null, null, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(objectRelationship1, objectRelationship2),
			(List<ObjectRelationship>)page.getItems());
		assertValid(page);

		objectRelationshipResource.deleteObjectRelationship(
			objectRelationship1.getId());

		objectRelationshipResource.deleteObjectRelationship(
			objectRelationship2.getId());
	}

	@Test
	public void testGetObjectDefinitionObjectRelationshipsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long objectDefinitionId =
			testGetObjectDefinitionObjectRelationshipsPage_getObjectDefinitionId();

		ObjectRelationship objectRelationship1 = randomObjectRelationship();

		objectRelationship1 =
			testGetObjectDefinitionObjectRelationshipsPage_addObjectRelationship(
				objectDefinitionId, objectRelationship1);

		for (EntityField entityField : entityFields) {
			Page<ObjectRelationship> page =
				objectRelationshipResource.
					getObjectDefinitionObjectRelationshipsPage(
						objectDefinitionId, null,
						getFilterString(
							entityField, "between", objectRelationship1),
						Pagination.of(1, 2));

			assertEquals(
				Collections.singletonList(objectRelationship1),
				(List<ObjectRelationship>)page.getItems());
		}
	}

	@Test
	public void testGetObjectDefinitionObjectRelationshipsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long objectDefinitionId =
			testGetObjectDefinitionObjectRelationshipsPage_getObjectDefinitionId();

		ObjectRelationship objectRelationship1 =
			testGetObjectDefinitionObjectRelationshipsPage_addObjectRelationship(
				objectDefinitionId, randomObjectRelationship());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ObjectRelationship objectRelationship2 =
			testGetObjectDefinitionObjectRelationshipsPage_addObjectRelationship(
				objectDefinitionId, randomObjectRelationship());

		for (EntityField entityField : entityFields) {
			Page<ObjectRelationship> page =
				objectRelationshipResource.
					getObjectDefinitionObjectRelationshipsPage(
						objectDefinitionId, null,
						getFilterString(entityField, "eq", objectRelationship1),
						Pagination.of(1, 2));

			assertEquals(
				Collections.singletonList(objectRelationship1),
				(List<ObjectRelationship>)page.getItems());
		}
	}

	@Test
	public void testGetObjectDefinitionObjectRelationshipsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long objectDefinitionId =
			testGetObjectDefinitionObjectRelationshipsPage_getObjectDefinitionId();

		ObjectRelationship objectRelationship1 =
			testGetObjectDefinitionObjectRelationshipsPage_addObjectRelationship(
				objectDefinitionId, randomObjectRelationship());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ObjectRelationship objectRelationship2 =
			testGetObjectDefinitionObjectRelationshipsPage_addObjectRelationship(
				objectDefinitionId, randomObjectRelationship());

		for (EntityField entityField : entityFields) {
			Page<ObjectRelationship> page =
				objectRelationshipResource.
					getObjectDefinitionObjectRelationshipsPage(
						objectDefinitionId, null,
						getFilterString(entityField, "eq", objectRelationship1),
						Pagination.of(1, 2));

			assertEquals(
				Collections.singletonList(objectRelationship1),
				(List<ObjectRelationship>)page.getItems());
		}
	}

	@Test
	public void testGetObjectDefinitionObjectRelationshipsPageWithPagination()
		throws Exception {

		Long objectDefinitionId =
			testGetObjectDefinitionObjectRelationshipsPage_getObjectDefinitionId();

		ObjectRelationship objectRelationship1 =
			testGetObjectDefinitionObjectRelationshipsPage_addObjectRelationship(
				objectDefinitionId, randomObjectRelationship());

		ObjectRelationship objectRelationship2 =
			testGetObjectDefinitionObjectRelationshipsPage_addObjectRelationship(
				objectDefinitionId, randomObjectRelationship());

		ObjectRelationship objectRelationship3 =
			testGetObjectDefinitionObjectRelationshipsPage_addObjectRelationship(
				objectDefinitionId, randomObjectRelationship());

		Page<ObjectRelationship> page1 =
			objectRelationshipResource.
				getObjectDefinitionObjectRelationshipsPage(
					objectDefinitionId, null, null, Pagination.of(1, 2));

		List<ObjectRelationship> objectRelationships1 =
			(List<ObjectRelationship>)page1.getItems();

		Assert.assertEquals(
			objectRelationships1.toString(), 2, objectRelationships1.size());

		Page<ObjectRelationship> page2 =
			objectRelationshipResource.
				getObjectDefinitionObjectRelationshipsPage(
					objectDefinitionId, null, null, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<ObjectRelationship> objectRelationships2 =
			(List<ObjectRelationship>)page2.getItems();

		Assert.assertEquals(
			objectRelationships2.toString(), 1, objectRelationships2.size());

		Page<ObjectRelationship> page3 =
			objectRelationshipResource.
				getObjectDefinitionObjectRelationshipsPage(
					objectDefinitionId, null, null, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				objectRelationship1, objectRelationship2, objectRelationship3),
			(List<ObjectRelationship>)page3.getItems());
	}

	protected ObjectRelationship
			testGetObjectDefinitionObjectRelationshipsPage_addObjectRelationship(
				Long objectDefinitionId, ObjectRelationship objectRelationship)
		throws Exception {

		return objectRelationshipResource.
			postObjectDefinitionObjectRelationship(
				objectDefinitionId, objectRelationship);
	}

	protected Long
			testGetObjectDefinitionObjectRelationshipsPage_getObjectDefinitionId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetObjectDefinitionObjectRelationshipsPage_getIrrelevantObjectDefinitionId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostObjectDefinitionObjectRelationship() throws Exception {
		ObjectRelationship randomObjectRelationship =
			randomObjectRelationship();

		ObjectRelationship postObjectRelationship =
			testPostObjectDefinitionObjectRelationship_addObjectRelationship(
				randomObjectRelationship);

		assertEquals(randomObjectRelationship, postObjectRelationship);
		assertValid(postObjectRelationship);
	}

	protected ObjectRelationship
			testPostObjectDefinitionObjectRelationship_addObjectRelationship(
				ObjectRelationship objectRelationship)
		throws Exception {

		return objectRelationshipResource.
			postObjectDefinitionObjectRelationship(
				testGetObjectDefinitionObjectRelationshipsPage_getObjectDefinitionId(),
				objectRelationship);
	}

	@Test
	public void testDeleteObjectRelationship() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		ObjectRelationship objectRelationship =
			testDeleteObjectRelationship_addObjectRelationship();

		assertHttpResponseStatusCode(
			204,
			objectRelationshipResource.deleteObjectRelationshipHttpResponse(
				objectRelationship.getId()));

		assertHttpResponseStatusCode(
			404,
			objectRelationshipResource.getObjectRelationshipHttpResponse(
				objectRelationship.getId()));

		assertHttpResponseStatusCode(
			404,
			objectRelationshipResource.getObjectRelationshipHttpResponse(0L));
	}

	protected ObjectRelationship
			testDeleteObjectRelationship_addObjectRelationship()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteObjectRelationship() throws Exception {
		ObjectRelationship objectRelationship =
			testGraphQLDeleteObjectRelationship_addObjectRelationship();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteObjectRelationship",
						new HashMap<String, Object>() {
							{
								put(
									"objectRelationshipId",
									objectRelationship.getId());
							}
						})),
				"JSONObject/data", "Object/deleteObjectRelationship"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"objectRelationship",
					new HashMap<String, Object>() {
						{
							put(
								"objectRelationshipId",
								objectRelationship.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected ObjectRelationship
			testGraphQLDeleteObjectRelationship_addObjectRelationship()
		throws Exception {

		return testGraphQLObjectRelationship_addObjectRelationship();
	}

	@Test
	public void testGetObjectRelationship() throws Exception {
		ObjectRelationship postObjectRelationship =
			testGetObjectRelationship_addObjectRelationship();

		ObjectRelationship getObjectRelationship =
			objectRelationshipResource.getObjectRelationship(
				postObjectRelationship.getId());

		assertEquals(postObjectRelationship, getObjectRelationship);
		assertValid(getObjectRelationship);
	}

	protected ObjectRelationship
			testGetObjectRelationship_addObjectRelationship()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetObjectRelationship() throws Exception {
		ObjectRelationship objectRelationship =
			testGraphQLGetObjectRelationship_addObjectRelationship();

		Assert.assertTrue(
			equals(
				objectRelationship,
				ObjectRelationshipSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"objectRelationship",
								new HashMap<String, Object>() {
									{
										put(
											"objectRelationshipId",
											objectRelationship.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/objectRelationship"))));
	}

	@Test
	public void testGraphQLGetObjectRelationshipNotFound() throws Exception {
		Long irrelevantObjectRelationshipId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"objectRelationship",
						new HashMap<String, Object>() {
							{
								put(
									"objectRelationshipId",
									irrelevantObjectRelationshipId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected ObjectRelationship
			testGraphQLGetObjectRelationship_addObjectRelationship()
		throws Exception {

		return testGraphQLObjectRelationship_addObjectRelationship();
	}

	@Test
	public void testPutObjectRelationship() throws Exception {
		ObjectRelationship postObjectRelationship =
			testPutObjectRelationship_addObjectRelationship();

		ObjectRelationship randomObjectRelationship =
			randomObjectRelationship();

		ObjectRelationship putObjectRelationship =
			objectRelationshipResource.putObjectRelationship(
				postObjectRelationship.getId(), randomObjectRelationship);

		assertEquals(randomObjectRelationship, putObjectRelationship);
		assertValid(putObjectRelationship);

		ObjectRelationship getObjectRelationship =
			objectRelationshipResource.getObjectRelationship(
				putObjectRelationship.getId());

		assertEquals(randomObjectRelationship, getObjectRelationship);
		assertValid(getObjectRelationship);
	}

	protected ObjectRelationship
			testPutObjectRelationship_addObjectRelationship()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected ObjectRelationship
			testGraphQLObjectRelationship_addObjectRelationship()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		ObjectRelationship objectRelationship,
		List<ObjectRelationship> objectRelationships) {

		boolean contains = false;

		for (ObjectRelationship item : objectRelationships) {
			if (equals(objectRelationship, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			objectRelationships + " does not contain " + objectRelationship,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ObjectRelationship objectRelationship1,
		ObjectRelationship objectRelationship2) {

		Assert.assertTrue(
			objectRelationship1 + " does not equal " + objectRelationship2,
			equals(objectRelationship1, objectRelationship2));
	}

	protected void assertEquals(
		List<ObjectRelationship> objectRelationships1,
		List<ObjectRelationship> objectRelationships2) {

		Assert.assertEquals(
			objectRelationships1.size(), objectRelationships2.size());

		for (int i = 0; i < objectRelationships1.size(); i++) {
			ObjectRelationship objectRelationship1 = objectRelationships1.get(
				i);
			ObjectRelationship objectRelationship2 = objectRelationships2.get(
				i);

			assertEquals(objectRelationship1, objectRelationship2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ObjectRelationship> objectRelationships1,
		List<ObjectRelationship> objectRelationships2) {

		Assert.assertEquals(
			objectRelationships1.size(), objectRelationships2.size());

		for (ObjectRelationship objectRelationship1 : objectRelationships1) {
			boolean contains = false;

			for (ObjectRelationship objectRelationship2 :
					objectRelationships2) {

				if (equals(objectRelationship1, objectRelationship2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				objectRelationships2 + " does not contain " +
					objectRelationship1,
				contains);
		}
	}

	protected void assertValid(ObjectRelationship objectRelationship)
		throws Exception {

		boolean valid = true;

		if (objectRelationship.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (objectRelationship.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("deletionType", additionalAssertFieldName)) {
				if (objectRelationship.getDeletionType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("label", additionalAssertFieldName)) {
				if (objectRelationship.getLabel() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (objectRelationship.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"objectDefinitionId1", additionalAssertFieldName)) {

				if (objectRelationship.getObjectDefinitionId1() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"objectDefinitionId2", additionalAssertFieldName)) {

				if (objectRelationship.getObjectDefinitionId2() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"objectDefinitionName2", additionalAssertFieldName)) {

				if (objectRelationship.getObjectDefinitionName2() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("reverse", additionalAssertFieldName)) {
				if (objectRelationship.getReverse() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (objectRelationship.getType() == null) {
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

	protected void assertValid(Page<ObjectRelationship> page) {
		boolean valid = false;

		java.util.Collection<ObjectRelationship> objectRelationships =
			page.getItems();

		int size = objectRelationships.size();

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
					com.liferay.object.admin.rest.dto.v1_0.ObjectRelationship.
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
		ObjectRelationship objectRelationship1,
		ObjectRelationship objectRelationship2) {

		if (objectRelationship1 == objectRelationship2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)objectRelationship1.getActions(),
						(Map)objectRelationship2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("deletionType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectRelationship1.getDeletionType(),
						objectRelationship2.getDeletionType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectRelationship1.getId(),
						objectRelationship2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("label", additionalAssertFieldName)) {
				if (!equals(
						(Map)objectRelationship1.getLabel(),
						(Map)objectRelationship2.getLabel())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectRelationship1.getName(),
						objectRelationship2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"objectDefinitionId1", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectRelationship1.getObjectDefinitionId1(),
						objectRelationship2.getObjectDefinitionId1())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"objectDefinitionId2", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectRelationship1.getObjectDefinitionId2(),
						objectRelationship2.getObjectDefinitionId2())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"objectDefinitionName2", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectRelationship1.getObjectDefinitionName2(),
						objectRelationship2.getObjectDefinitionName2())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("reverse", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectRelationship1.getReverse(),
						objectRelationship2.getReverse())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectRelationship1.getType(),
						objectRelationship2.getType())) {

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

		if (!(_objectRelationshipResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_objectRelationshipResource;

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
		EntityField entityField, String operator,
		ObjectRelationship objectRelationship) {

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

		if (entityFieldName.equals("deletionType")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("label")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(objectRelationship.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("objectDefinitionId1")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("objectDefinitionId2")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("objectDefinitionName2")) {
			sb.append("'");
			sb.append(
				String.valueOf(objectRelationship.getObjectDefinitionName2()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("reverse")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("type")) {
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

	protected ObjectRelationship randomObjectRelationship() throws Exception {
		return new ObjectRelationship() {
			{
				id = RandomTestUtil.randomLong();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				objectDefinitionId1 = RandomTestUtil.randomLong();
				objectDefinitionId2 = RandomTestUtil.randomLong();
				objectDefinitionName2 = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				reverse = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected ObjectRelationship randomIrrelevantObjectRelationship()
		throws Exception {

		ObjectRelationship randomIrrelevantObjectRelationship =
			randomObjectRelationship();

		return randomIrrelevantObjectRelationship;
	}

	protected ObjectRelationship randomPatchObjectRelationship()
		throws Exception {

		return randomObjectRelationship();
	}

	protected ObjectRelationshipResource objectRelationshipResource;
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
		LogFactoryUtil.getLog(BaseObjectRelationshipResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private
		com.liferay.object.admin.rest.resource.v1_0.ObjectRelationshipResource
			_objectRelationshipResource;

}