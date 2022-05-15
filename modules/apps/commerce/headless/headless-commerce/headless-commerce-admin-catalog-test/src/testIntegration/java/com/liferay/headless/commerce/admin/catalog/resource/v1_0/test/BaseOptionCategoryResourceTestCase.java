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

package com.liferay.headless.commerce.admin.catalog.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.OptionCategory;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.OptionCategoryResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.OptionCategorySerDes;
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
import com.liferay.portal.kernel.util.GetterUtil;
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

import org.apache.commons.lang.time.DateUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
public abstract class BaseOptionCategoryResourceTestCase {

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

		_optionCategoryResource.setContextCompany(testCompany);

		OptionCategoryResource.Builder builder =
			OptionCategoryResource.builder();

		optionCategoryResource = builder.authentication(
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

		OptionCategory optionCategory1 = randomOptionCategory();

		String json = objectMapper.writeValueAsString(optionCategory1);

		OptionCategory optionCategory2 = OptionCategorySerDes.toDTO(json);

		Assert.assertTrue(equals(optionCategory1, optionCategory2));
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

		OptionCategory optionCategory = randomOptionCategory();

		String json1 = objectMapper.writeValueAsString(optionCategory);
		String json2 = OptionCategorySerDes.toJSON(optionCategory);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		OptionCategory optionCategory = randomOptionCategory();

		optionCategory.setKey(regex);

		String json = OptionCategorySerDes.toJSON(optionCategory);

		Assert.assertFalse(json.contains(regex));

		optionCategory = OptionCategorySerDes.toDTO(json);

		Assert.assertEquals(regex, optionCategory.getKey());
	}

	@Test
	public void testGetOptionCategoriesPage() throws Exception {
		Page<OptionCategory> page =
			optionCategoryResource.getOptionCategoriesPage(
				null, Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		OptionCategory optionCategory1 =
			testGetOptionCategoriesPage_addOptionCategory(
				randomOptionCategory());

		OptionCategory optionCategory2 =
			testGetOptionCategoriesPage_addOptionCategory(
				randomOptionCategory());

		page = optionCategoryResource.getOptionCategoriesPage(
			null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(optionCategory1, (List<OptionCategory>)page.getItems());
		assertContains(optionCategory2, (List<OptionCategory>)page.getItems());
		assertValid(page);

		optionCategoryResource.deleteOptionCategory(optionCategory1.getId());

		optionCategoryResource.deleteOptionCategory(optionCategory2.getId());
	}

	@Test
	public void testGetOptionCategoriesPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		OptionCategory optionCategory1 = randomOptionCategory();

		optionCategory1 = testGetOptionCategoriesPage_addOptionCategory(
			optionCategory1);

		for (EntityField entityField : entityFields) {
			Page<OptionCategory> page =
				optionCategoryResource.getOptionCategoriesPage(
					getFilterString(entityField, "between", optionCategory1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(optionCategory1),
				(List<OptionCategory>)page.getItems());
		}
	}

	@Test
	public void testGetOptionCategoriesPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		OptionCategory optionCategory1 =
			testGetOptionCategoriesPage_addOptionCategory(
				randomOptionCategory());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		OptionCategory optionCategory2 =
			testGetOptionCategoriesPage_addOptionCategory(
				randomOptionCategory());

		for (EntityField entityField : entityFields) {
			Page<OptionCategory> page =
				optionCategoryResource.getOptionCategoriesPage(
					getFilterString(entityField, "eq", optionCategory1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(optionCategory1),
				(List<OptionCategory>)page.getItems());
		}
	}

	@Test
	public void testGetOptionCategoriesPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		OptionCategory optionCategory1 =
			testGetOptionCategoriesPage_addOptionCategory(
				randomOptionCategory());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		OptionCategory optionCategory2 =
			testGetOptionCategoriesPage_addOptionCategory(
				randomOptionCategory());

		for (EntityField entityField : entityFields) {
			Page<OptionCategory> page =
				optionCategoryResource.getOptionCategoriesPage(
					getFilterString(entityField, "eq", optionCategory1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(optionCategory1),
				(List<OptionCategory>)page.getItems());
		}
	}

	@Test
	public void testGetOptionCategoriesPageWithPagination() throws Exception {
		Page<OptionCategory> totalPage =
			optionCategoryResource.getOptionCategoriesPage(null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		OptionCategory optionCategory1 =
			testGetOptionCategoriesPage_addOptionCategory(
				randomOptionCategory());

		OptionCategory optionCategory2 =
			testGetOptionCategoriesPage_addOptionCategory(
				randomOptionCategory());

		OptionCategory optionCategory3 =
			testGetOptionCategoriesPage_addOptionCategory(
				randomOptionCategory());

		Page<OptionCategory> page1 =
			optionCategoryResource.getOptionCategoriesPage(
				null, Pagination.of(1, totalCount + 2), null);

		List<OptionCategory> optionCategories1 =
			(List<OptionCategory>)page1.getItems();

		Assert.assertEquals(
			optionCategories1.toString(), totalCount + 2,
			optionCategories1.size());

		Page<OptionCategory> page2 =
			optionCategoryResource.getOptionCategoriesPage(
				null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<OptionCategory> optionCategories2 =
			(List<OptionCategory>)page2.getItems();

		Assert.assertEquals(
			optionCategories2.toString(), 1, optionCategories2.size());

		Page<OptionCategory> page3 =
			optionCategoryResource.getOptionCategoriesPage(
				null, Pagination.of(1, totalCount + 3), null);

		assertContains(optionCategory1, (List<OptionCategory>)page3.getItems());
		assertContains(optionCategory2, (List<OptionCategory>)page3.getItems());
		assertContains(optionCategory3, (List<OptionCategory>)page3.getItems());
	}

	@Test
	public void testGetOptionCategoriesPageWithSortDateTime() throws Exception {
		testGetOptionCategoriesPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, optionCategory1, optionCategory2) -> {
				BeanTestUtil.setProperty(
					optionCategory1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetOptionCategoriesPageWithSortDouble() throws Exception {
		testGetOptionCategoriesPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, optionCategory1, optionCategory2) -> {
				BeanTestUtil.setProperty(
					optionCategory1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					optionCategory2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetOptionCategoriesPageWithSortInteger() throws Exception {
		testGetOptionCategoriesPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, optionCategory1, optionCategory2) -> {
				BeanTestUtil.setProperty(
					optionCategory1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					optionCategory2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetOptionCategoriesPageWithSortString() throws Exception {
		testGetOptionCategoriesPageWithSort(
			EntityField.Type.STRING,
			(entityField, optionCategory1, optionCategory2) -> {
				Class<?> clazz = optionCategory1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						optionCategory1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						optionCategory2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						optionCategory1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						optionCategory2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						optionCategory1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						optionCategory2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetOptionCategoriesPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, OptionCategory, OptionCategory, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		OptionCategory optionCategory1 = randomOptionCategory();
		OptionCategory optionCategory2 = randomOptionCategory();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, optionCategory1, optionCategory2);
		}

		optionCategory1 = testGetOptionCategoriesPage_addOptionCategory(
			optionCategory1);

		optionCategory2 = testGetOptionCategoriesPage_addOptionCategory(
			optionCategory2);

		for (EntityField entityField : entityFields) {
			Page<OptionCategory> ascPage =
				optionCategoryResource.getOptionCategoriesPage(
					null, Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(optionCategory1, optionCategory2),
				(List<OptionCategory>)ascPage.getItems());

			Page<OptionCategory> descPage =
				optionCategoryResource.getOptionCategoriesPage(
					null, Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(optionCategory2, optionCategory1),
				(List<OptionCategory>)descPage.getItems());
		}
	}

	protected OptionCategory testGetOptionCategoriesPage_addOptionCategory(
			OptionCategory optionCategory)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetOptionCategoriesPage() throws Exception {
		GraphQLField graphQLField = new GraphQLField(
			"optionCategories",
			new HashMap<String, Object>() {
				{
					put("page", 1);
					put("pageSize", 10);
				}
			},
			new GraphQLField("items", getGraphQLFields()),
			new GraphQLField("page"), new GraphQLField("totalCount"));

		JSONObject optionCategoriesJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/optionCategories");

		long totalCount = optionCategoriesJSONObject.getLong("totalCount");

		OptionCategory optionCategory1 =
			testGraphQLGetOptionCategoriesPage_addOptionCategory();
		OptionCategory optionCategory2 =
			testGraphQLGetOptionCategoriesPage_addOptionCategory();

		optionCategoriesJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/optionCategories");

		Assert.assertEquals(
			totalCount + 2, optionCategoriesJSONObject.getLong("totalCount"));

		assertContains(
			optionCategory1,
			Arrays.asList(
				OptionCategorySerDes.toDTOs(
					optionCategoriesJSONObject.getString("items"))));
		assertContains(
			optionCategory2,
			Arrays.asList(
				OptionCategorySerDes.toDTOs(
					optionCategoriesJSONObject.getString("items"))));
	}

	protected OptionCategory
			testGraphQLGetOptionCategoriesPage_addOptionCategory()
		throws Exception {

		return testGraphQLOptionCategory_addOptionCategory();
	}

	@Test
	public void testPostOptionCategory() throws Exception {
		OptionCategory randomOptionCategory = randomOptionCategory();

		OptionCategory postOptionCategory =
			testPostOptionCategory_addOptionCategory(randomOptionCategory);

		assertEquals(randomOptionCategory, postOptionCategory);
		assertValid(postOptionCategory);
	}

	protected OptionCategory testPostOptionCategory_addOptionCategory(
			OptionCategory optionCategory)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteOptionCategory() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		OptionCategory optionCategory =
			testDeleteOptionCategory_addOptionCategory();

		assertHttpResponseStatusCode(
			204,
			optionCategoryResource.deleteOptionCategoryHttpResponse(
				optionCategory.getId()));

		assertHttpResponseStatusCode(
			404,
			optionCategoryResource.getOptionCategoryHttpResponse(
				optionCategory.getId()));

		assertHttpResponseStatusCode(
			404,
			optionCategoryResource.getOptionCategoryHttpResponse(
				optionCategory.getId()));
	}

	protected OptionCategory testDeleteOptionCategory_addOptionCategory()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteOptionCategory() throws Exception {
		OptionCategory optionCategory =
			testGraphQLDeleteOptionCategory_addOptionCategory();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteOptionCategory",
						new HashMap<String, Object>() {
							{
								put("id", optionCategory.getId());
							}
						})),
				"JSONObject/data", "Object/deleteOptionCategory"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"optionCategory",
					new HashMap<String, Object>() {
						{
							put("id", optionCategory.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected OptionCategory testGraphQLDeleteOptionCategory_addOptionCategory()
		throws Exception {

		return testGraphQLOptionCategory_addOptionCategory();
	}

	@Test
	public void testGetOptionCategory() throws Exception {
		OptionCategory postOptionCategory =
			testGetOptionCategory_addOptionCategory();

		OptionCategory getOptionCategory =
			optionCategoryResource.getOptionCategory(
				postOptionCategory.getId());

		assertEquals(postOptionCategory, getOptionCategory);
		assertValid(getOptionCategory);
	}

	protected OptionCategory testGetOptionCategory_addOptionCategory()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetOptionCategory() throws Exception {
		OptionCategory optionCategory =
			testGraphQLGetOptionCategory_addOptionCategory();

		Assert.assertTrue(
			equals(
				optionCategory,
				OptionCategorySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"optionCategory",
								new HashMap<String, Object>() {
									{
										put("id", optionCategory.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/optionCategory"))));
	}

	@Test
	public void testGraphQLGetOptionCategoryNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"optionCategory",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected OptionCategory testGraphQLGetOptionCategory_addOptionCategory()
		throws Exception {

		return testGraphQLOptionCategory_addOptionCategory();
	}

	@Test
	public void testPatchOptionCategory() throws Exception {
		Assert.assertTrue(false);
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected OptionCategory testGraphQLOptionCategory_addOptionCategory()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		OptionCategory optionCategory, List<OptionCategory> optionCategories) {

		boolean contains = false;

		for (OptionCategory item : optionCategories) {
			if (equals(optionCategory, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			optionCategories + " does not contain " + optionCategory, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		OptionCategory optionCategory1, OptionCategory optionCategory2) {

		Assert.assertTrue(
			optionCategory1 + " does not equal " + optionCategory2,
			equals(optionCategory1, optionCategory2));
	}

	protected void assertEquals(
		List<OptionCategory> optionCategories1,
		List<OptionCategory> optionCategories2) {

		Assert.assertEquals(optionCategories1.size(), optionCategories2.size());

		for (int i = 0; i < optionCategories1.size(); i++) {
			OptionCategory optionCategory1 = optionCategories1.get(i);
			OptionCategory optionCategory2 = optionCategories2.get(i);

			assertEquals(optionCategory1, optionCategory2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<OptionCategory> optionCategories1,
		List<OptionCategory> optionCategories2) {

		Assert.assertEquals(optionCategories1.size(), optionCategories2.size());

		for (OptionCategory optionCategory1 : optionCategories1) {
			boolean contains = false;

			for (OptionCategory optionCategory2 : optionCategories2) {
				if (equals(optionCategory1, optionCategory2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				optionCategories2 + " does not contain " + optionCategory1,
				contains);
		}
	}

	protected void assertValid(OptionCategory optionCategory) throws Exception {
		boolean valid = true;

		if (optionCategory.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (optionCategory.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (optionCategory.getKey() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (optionCategory.getPriority() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("title", additionalAssertFieldName)) {
				if (optionCategory.getTitle() == null) {
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

	protected void assertValid(Page<OptionCategory> page) {
		boolean valid = false;

		java.util.Collection<OptionCategory> optionCategories = page.getItems();

		int size = optionCategories.size();

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
					com.liferay.headless.commerce.admin.catalog.dto.v1_0.
						OptionCategory.class)) {

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
		OptionCategory optionCategory1, OptionCategory optionCategory2) {

		if (optionCategory1 == optionCategory2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!equals(
						(Map)optionCategory1.getDescription(),
						(Map)optionCategory2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						optionCategory1.getId(), optionCategory2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						optionCategory1.getKey(), optionCategory2.getKey())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						optionCategory1.getPriority(),
						optionCategory2.getPriority())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("title", additionalAssertFieldName)) {
				if (!equals(
						(Map)optionCategory1.getTitle(),
						(Map)optionCategory2.getTitle())) {

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

		if (!(_optionCategoryResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_optionCategoryResource;

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
		OptionCategory optionCategory) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("description")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("key")) {
			sb.append("'");
			sb.append(String.valueOf(optionCategory.getKey()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("priority")) {
			sb.append(String.valueOf(optionCategory.getPriority()));

			return sb.toString();
		}

		if (entityFieldName.equals("title")) {
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

	protected OptionCategory randomOptionCategory() throws Exception {
		return new OptionCategory() {
			{
				id = RandomTestUtil.randomLong();
				key = StringUtil.toLowerCase(RandomTestUtil.randomString());
				priority = RandomTestUtil.randomDouble();
			}
		};
	}

	protected OptionCategory randomIrrelevantOptionCategory() throws Exception {
		OptionCategory randomIrrelevantOptionCategory = randomOptionCategory();

		return randomIrrelevantOptionCategory;
	}

	protected OptionCategory randomPatchOptionCategory() throws Exception {
		return randomOptionCategory();
	}

	protected OptionCategoryResource optionCategoryResource;
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
		LogFactoryUtil.getLog(BaseOptionCategoryResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		OptionCategoryResource _optionCategoryResource;

}