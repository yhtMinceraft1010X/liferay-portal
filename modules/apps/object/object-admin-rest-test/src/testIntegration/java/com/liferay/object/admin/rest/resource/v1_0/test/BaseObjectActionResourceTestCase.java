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

import com.liferay.object.admin.rest.client.dto.v1_0.ObjectAction;
import com.liferay.object.admin.rest.client.http.HttpInvoker;
import com.liferay.object.admin.rest.client.pagination.Page;
import com.liferay.object.admin.rest.client.pagination.Pagination;
import com.liferay.object.admin.rest.client.resource.v1_0.ObjectActionResource;
import com.liferay.object.admin.rest.client.serdes.v1_0.ObjectActionSerDes;
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
public abstract class BaseObjectActionResourceTestCase {

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

		_objectActionResource.setContextCompany(testCompany);

		ObjectActionResource.Builder builder = ObjectActionResource.builder();

		objectActionResource = builder.authentication(
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

		ObjectAction objectAction1 = randomObjectAction();

		String json = objectMapper.writeValueAsString(objectAction1);

		ObjectAction objectAction2 = ObjectActionSerDes.toDTO(json);

		Assert.assertTrue(equals(objectAction1, objectAction2));
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

		ObjectAction objectAction = randomObjectAction();

		String json1 = objectMapper.writeValueAsString(objectAction);
		String json2 = ObjectActionSerDes.toJSON(objectAction);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ObjectAction objectAction = randomObjectAction();

		objectAction.setDescription(regex);
		objectAction.setName(regex);
		objectAction.setObjectActionExecutorKey(regex);
		objectAction.setObjectActionTriggerKey(regex);

		String json = ObjectActionSerDes.toJSON(objectAction);

		Assert.assertFalse(json.contains(regex));

		objectAction = ObjectActionSerDes.toDTO(json);

		Assert.assertEquals(regex, objectAction.getDescription());
		Assert.assertEquals(regex, objectAction.getName());
		Assert.assertEquals(regex, objectAction.getObjectActionExecutorKey());
		Assert.assertEquals(regex, objectAction.getObjectActionTriggerKey());
	}

	@Test
	public void testDeleteObjectAction() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		ObjectAction objectAction = testDeleteObjectAction_addObjectAction();

		assertHttpResponseStatusCode(
			204,
			objectActionResource.deleteObjectActionHttpResponse(
				objectAction.getId()));

		assertHttpResponseStatusCode(
			404,
			objectActionResource.getObjectActionHttpResponse(
				objectAction.getId()));

		assertHttpResponseStatusCode(
			404, objectActionResource.getObjectActionHttpResponse(0L));
	}

	protected ObjectAction testDeleteObjectAction_addObjectAction()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteObjectAction() throws Exception {
		ObjectAction objectAction =
			testGraphQLDeleteObjectAction_addObjectAction();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteObjectAction",
						new HashMap<String, Object>() {
							{
								put("objectActionId", objectAction.getId());
							}
						})),
				"JSONObject/data", "Object/deleteObjectAction"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"objectAction",
					new HashMap<String, Object>() {
						{
							put("objectActionId", objectAction.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected ObjectAction testGraphQLDeleteObjectAction_addObjectAction()
		throws Exception {

		return testGraphQLObjectAction_addObjectAction();
	}

	@Test
	public void testGetObjectAction() throws Exception {
		ObjectAction postObjectAction = testGetObjectAction_addObjectAction();

		ObjectAction getObjectAction = objectActionResource.getObjectAction(
			postObjectAction.getId());

		assertEquals(postObjectAction, getObjectAction);
		assertValid(getObjectAction);
	}

	protected ObjectAction testGetObjectAction_addObjectAction()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetObjectAction() throws Exception {
		ObjectAction objectAction =
			testGraphQLGetObjectAction_addObjectAction();

		Assert.assertTrue(
			equals(
				objectAction,
				ObjectActionSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"objectAction",
								new HashMap<String, Object>() {
									{
										put(
											"objectActionId",
											objectAction.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/objectAction"))));
	}

	@Test
	public void testGraphQLGetObjectActionNotFound() throws Exception {
		Long irrelevantObjectActionId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"objectAction",
						new HashMap<String, Object>() {
							{
								put("objectActionId", irrelevantObjectActionId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected ObjectAction testGraphQLGetObjectAction_addObjectAction()
		throws Exception {

		return testGraphQLObjectAction_addObjectAction();
	}

	@Test
	public void testPatchObjectAction() throws Exception {
		ObjectAction postObjectAction = testPatchObjectAction_addObjectAction();

		ObjectAction randomPatchObjectAction = randomPatchObjectAction();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ObjectAction patchObjectAction = objectActionResource.patchObjectAction(
			postObjectAction.getId(), randomPatchObjectAction);

		ObjectAction expectedPatchObjectAction = postObjectAction.clone();

		BeanTestUtil.copyProperties(
			randomPatchObjectAction, expectedPatchObjectAction);

		ObjectAction getObjectAction = objectActionResource.getObjectAction(
			patchObjectAction.getId());

		assertEquals(expectedPatchObjectAction, getObjectAction);
		assertValid(getObjectAction);
	}

	protected ObjectAction testPatchObjectAction_addObjectAction()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPutObjectAction() throws Exception {
		ObjectAction postObjectAction = testPutObjectAction_addObjectAction();

		ObjectAction randomObjectAction = randomObjectAction();

		ObjectAction putObjectAction = objectActionResource.putObjectAction(
			postObjectAction.getId(), randomObjectAction);

		assertEquals(randomObjectAction, putObjectAction);
		assertValid(putObjectAction);

		ObjectAction getObjectAction = objectActionResource.getObjectAction(
			putObjectAction.getId());

		assertEquals(randomObjectAction, getObjectAction);
		assertValid(getObjectAction);
	}

	protected ObjectAction testPutObjectAction_addObjectAction()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetObjectDefinitionObjectActionsPage() throws Exception {
		Long objectDefinitionId =
			testGetObjectDefinitionObjectActionsPage_getObjectDefinitionId();
		Long irrelevantObjectDefinitionId =
			testGetObjectDefinitionObjectActionsPage_getIrrelevantObjectDefinitionId();

		Page<ObjectAction> page =
			objectActionResource.getObjectDefinitionObjectActionsPage(
				objectDefinitionId, null, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantObjectDefinitionId != null) {
			ObjectAction irrelevantObjectAction =
				testGetObjectDefinitionObjectActionsPage_addObjectAction(
					irrelevantObjectDefinitionId,
					randomIrrelevantObjectAction());

			page = objectActionResource.getObjectDefinitionObjectActionsPage(
				irrelevantObjectDefinitionId, null, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantObjectAction),
				(List<ObjectAction>)page.getItems());
			assertValid(page);
		}

		ObjectAction objectAction1 =
			testGetObjectDefinitionObjectActionsPage_addObjectAction(
				objectDefinitionId, randomObjectAction());

		ObjectAction objectAction2 =
			testGetObjectDefinitionObjectActionsPage_addObjectAction(
				objectDefinitionId, randomObjectAction());

		page = objectActionResource.getObjectDefinitionObjectActionsPage(
			objectDefinitionId, null, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(objectAction1, objectAction2),
			(List<ObjectAction>)page.getItems());
		assertValid(page);

		objectActionResource.deleteObjectAction(objectAction1.getId());

		objectActionResource.deleteObjectAction(objectAction2.getId());
	}

	@Test
	public void testGetObjectDefinitionObjectActionsPageWithPagination()
		throws Exception {

		Long objectDefinitionId =
			testGetObjectDefinitionObjectActionsPage_getObjectDefinitionId();

		ObjectAction objectAction1 =
			testGetObjectDefinitionObjectActionsPage_addObjectAction(
				objectDefinitionId, randomObjectAction());

		ObjectAction objectAction2 =
			testGetObjectDefinitionObjectActionsPage_addObjectAction(
				objectDefinitionId, randomObjectAction());

		ObjectAction objectAction3 =
			testGetObjectDefinitionObjectActionsPage_addObjectAction(
				objectDefinitionId, randomObjectAction());

		Page<ObjectAction> page1 =
			objectActionResource.getObjectDefinitionObjectActionsPage(
				objectDefinitionId, null, Pagination.of(1, 2));

		List<ObjectAction> objectActions1 =
			(List<ObjectAction>)page1.getItems();

		Assert.assertEquals(
			objectActions1.toString(), 2, objectActions1.size());

		Page<ObjectAction> page2 =
			objectActionResource.getObjectDefinitionObjectActionsPage(
				objectDefinitionId, null, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<ObjectAction> objectActions2 =
			(List<ObjectAction>)page2.getItems();

		Assert.assertEquals(
			objectActions2.toString(), 1, objectActions2.size());

		Page<ObjectAction> page3 =
			objectActionResource.getObjectDefinitionObjectActionsPage(
				objectDefinitionId, null, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(objectAction1, objectAction2, objectAction3),
			(List<ObjectAction>)page3.getItems());
	}

	protected ObjectAction
			testGetObjectDefinitionObjectActionsPage_addObjectAction(
				Long objectDefinitionId, ObjectAction objectAction)
		throws Exception {

		return objectActionResource.postObjectDefinitionObjectAction(
			objectDefinitionId, objectAction);
	}

	protected Long
			testGetObjectDefinitionObjectActionsPage_getObjectDefinitionId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetObjectDefinitionObjectActionsPage_getIrrelevantObjectDefinitionId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostObjectDefinitionObjectAction() throws Exception {
		ObjectAction randomObjectAction = randomObjectAction();

		ObjectAction postObjectAction =
			testPostObjectDefinitionObjectAction_addObjectAction(
				randomObjectAction);

		assertEquals(randomObjectAction, postObjectAction);
		assertValid(postObjectAction);
	}

	protected ObjectAction testPostObjectDefinitionObjectAction_addObjectAction(
			ObjectAction objectAction)
		throws Exception {

		return objectActionResource.postObjectDefinitionObjectAction(
			testGetObjectDefinitionObjectActionsPage_getObjectDefinitionId(),
			objectAction);
	}

	protected ObjectAction testGraphQLObjectAction_addObjectAction()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		ObjectAction objectAction, List<ObjectAction> objectActions) {

		boolean contains = false;

		for (ObjectAction item : objectActions) {
			if (equals(objectAction, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			objectActions + " does not contain " + objectAction, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ObjectAction objectAction1, ObjectAction objectAction2) {

		Assert.assertTrue(
			objectAction1 + " does not equal " + objectAction2,
			equals(objectAction1, objectAction2));
	}

	protected void assertEquals(
		List<ObjectAction> objectActions1, List<ObjectAction> objectActions2) {

		Assert.assertEquals(objectActions1.size(), objectActions2.size());

		for (int i = 0; i < objectActions1.size(); i++) {
			ObjectAction objectAction1 = objectActions1.get(i);
			ObjectAction objectAction2 = objectActions2.get(i);

			assertEquals(objectAction1, objectAction2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ObjectAction> objectActions1, List<ObjectAction> objectActions2) {

		Assert.assertEquals(objectActions1.size(), objectActions2.size());

		for (ObjectAction objectAction1 : objectActions1) {
			boolean contains = false;

			for (ObjectAction objectAction2 : objectActions2) {
				if (equals(objectAction1, objectAction2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				objectActions2 + " does not contain " + objectAction1,
				contains);
		}
	}

	protected void assertValid(ObjectAction objectAction) throws Exception {
		boolean valid = true;

		if (objectAction.getDateCreated() == null) {
			valid = false;
		}

		if (objectAction.getDateModified() == null) {
			valid = false;
		}

		if (objectAction.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (objectAction.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("active", additionalAssertFieldName)) {
				if (objectAction.getActive() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (objectAction.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (objectAction.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"objectActionExecutorKey", additionalAssertFieldName)) {

				if (objectAction.getObjectActionExecutorKey() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"objectActionTriggerKey", additionalAssertFieldName)) {

				if (objectAction.getObjectActionTriggerKey() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("parameters", additionalAssertFieldName)) {
				if (objectAction.getParameters() == null) {
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

	protected void assertValid(Page<ObjectAction> page) {
		boolean valid = false;

		java.util.Collection<ObjectAction> objectActions = page.getItems();

		int size = objectActions.size();

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
					com.liferay.object.admin.rest.dto.v1_0.ObjectAction.
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
		ObjectAction objectAction1, ObjectAction objectAction2) {

		if (objectAction1 == objectAction2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)objectAction1.getActions(),
						(Map)objectAction2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("active", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectAction1.getActive(), objectAction2.getActive())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateCreated", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectAction1.getDateCreated(),
						objectAction2.getDateCreated())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectAction1.getDateModified(),
						objectAction2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectAction1.getDescription(),
						objectAction2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectAction1.getId(), objectAction2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectAction1.getName(), objectAction2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"objectActionExecutorKey", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectAction1.getObjectActionExecutorKey(),
						objectAction2.getObjectActionExecutorKey())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"objectActionTriggerKey", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectAction1.getObjectActionTriggerKey(),
						objectAction2.getObjectActionTriggerKey())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("parameters", additionalAssertFieldName)) {
				if (!equals(
						(Map)objectAction1.getParameters(),
						(Map)objectAction2.getParameters())) {

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

		if (!(_objectActionResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_objectActionResource;

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
		EntityField entityField, String operator, ObjectAction objectAction) {

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

		if (entityFieldName.equals("active")) {
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
							objectAction.getDateCreated(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							objectAction.getDateCreated(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(objectAction.getDateCreated()));
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
							objectAction.getDateModified(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							objectAction.getDateModified(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(objectAction.getDateModified()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(objectAction.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(objectAction.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("objectActionExecutorKey")) {
			sb.append("'");
			sb.append(
				String.valueOf(objectAction.getObjectActionExecutorKey()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("objectActionTriggerKey")) {
			sb.append("'");
			sb.append(String.valueOf(objectAction.getObjectActionTriggerKey()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("parameters")) {
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

	protected ObjectAction randomObjectAction() throws Exception {
		return new ObjectAction() {
			{
				active = RandomTestUtil.randomBoolean();
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				description = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				objectActionExecutorKey = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				objectActionTriggerKey = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
			}
		};
	}

	protected ObjectAction randomIrrelevantObjectAction() throws Exception {
		ObjectAction randomIrrelevantObjectAction = randomObjectAction();

		return randomIrrelevantObjectAction;
	}

	protected ObjectAction randomPatchObjectAction() throws Exception {
		return randomObjectAction();
	}

	protected ObjectActionResource objectActionResource;
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
		LogFactoryUtil.getLog(BaseObjectActionResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.object.admin.rest.resource.v1_0.ObjectActionResource
		_objectActionResource;

}