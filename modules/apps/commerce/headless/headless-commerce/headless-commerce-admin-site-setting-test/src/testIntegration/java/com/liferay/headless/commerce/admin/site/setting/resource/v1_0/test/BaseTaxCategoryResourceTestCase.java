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

package com.liferay.headless.commerce.admin.site.setting.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.site.setting.client.dto.v1_0.TaxCategory;
import com.liferay.headless.commerce.admin.site.setting.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.site.setting.client.pagination.Page;
import com.liferay.headless.commerce.admin.site.setting.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.site.setting.client.resource.v1_0.TaxCategoryResource;
import com.liferay.headless.commerce.admin.site.setting.client.serdes.v1_0.TaxCategorySerDes;
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
public abstract class BaseTaxCategoryResourceTestCase {

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

		_taxCategoryResource.setContextCompany(testCompany);

		TaxCategoryResource.Builder builder = TaxCategoryResource.builder();

		taxCategoryResource = builder.authentication(
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

		TaxCategory taxCategory1 = randomTaxCategory();

		String json = objectMapper.writeValueAsString(taxCategory1);

		TaxCategory taxCategory2 = TaxCategorySerDes.toDTO(json);

		Assert.assertTrue(equals(taxCategory1, taxCategory2));
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

		TaxCategory taxCategory = randomTaxCategory();

		String json1 = objectMapper.writeValueAsString(taxCategory);
		String json2 = TaxCategorySerDes.toJSON(taxCategory);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		TaxCategory taxCategory = randomTaxCategory();

		String json = TaxCategorySerDes.toJSON(taxCategory);

		Assert.assertFalse(json.contains(regex));

		taxCategory = TaxCategorySerDes.toDTO(json);
	}

	@Test
	public void testGetCommerceAdminSiteSettingGroupTaxCategoryPage()
		throws Exception {

		Long groupId =
			testGetCommerceAdminSiteSettingGroupTaxCategoryPage_getGroupId();
		Long irrelevantGroupId =
			testGetCommerceAdminSiteSettingGroupTaxCategoryPage_getIrrelevantGroupId();

		Page<TaxCategory> page =
			taxCategoryResource.getCommerceAdminSiteSettingGroupTaxCategoryPage(
				groupId, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantGroupId != null) {
			TaxCategory irrelevantTaxCategory =
				testGetCommerceAdminSiteSettingGroupTaxCategoryPage_addTaxCategory(
					irrelevantGroupId, randomIrrelevantTaxCategory());

			page =
				taxCategoryResource.
					getCommerceAdminSiteSettingGroupTaxCategoryPage(
						irrelevantGroupId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantTaxCategory),
				(List<TaxCategory>)page.getItems());
			assertValid(page);
		}

		TaxCategory taxCategory1 =
			testGetCommerceAdminSiteSettingGroupTaxCategoryPage_addTaxCategory(
				groupId, randomTaxCategory());

		TaxCategory taxCategory2 =
			testGetCommerceAdminSiteSettingGroupTaxCategoryPage_addTaxCategory(
				groupId, randomTaxCategory());

		page =
			taxCategoryResource.getCommerceAdminSiteSettingGroupTaxCategoryPage(
				groupId, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(taxCategory1, taxCategory2),
			(List<TaxCategory>)page.getItems());
		assertValid(page);

		taxCategoryResource.deleteTaxCategory(taxCategory1.getId());

		taxCategoryResource.deleteTaxCategory(taxCategory2.getId());
	}

	@Test
	public void testGetCommerceAdminSiteSettingGroupTaxCategoryPageWithPagination()
		throws Exception {

		Long groupId =
			testGetCommerceAdminSiteSettingGroupTaxCategoryPage_getGroupId();

		TaxCategory taxCategory1 =
			testGetCommerceAdminSiteSettingGroupTaxCategoryPage_addTaxCategory(
				groupId, randomTaxCategory());

		TaxCategory taxCategory2 =
			testGetCommerceAdminSiteSettingGroupTaxCategoryPage_addTaxCategory(
				groupId, randomTaxCategory());

		TaxCategory taxCategory3 =
			testGetCommerceAdminSiteSettingGroupTaxCategoryPage_addTaxCategory(
				groupId, randomTaxCategory());

		Page<TaxCategory> page1 =
			taxCategoryResource.getCommerceAdminSiteSettingGroupTaxCategoryPage(
				groupId, Pagination.of(1, 2));

		List<TaxCategory> taxCategories1 = (List<TaxCategory>)page1.getItems();

		Assert.assertEquals(
			taxCategories1.toString(), 2, taxCategories1.size());

		Page<TaxCategory> page2 =
			taxCategoryResource.getCommerceAdminSiteSettingGroupTaxCategoryPage(
				groupId, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<TaxCategory> taxCategories2 = (List<TaxCategory>)page2.getItems();

		Assert.assertEquals(
			taxCategories2.toString(), 1, taxCategories2.size());

		Page<TaxCategory> page3 =
			taxCategoryResource.getCommerceAdminSiteSettingGroupTaxCategoryPage(
				groupId, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(taxCategory1, taxCategory2, taxCategory3),
			(List<TaxCategory>)page3.getItems());
	}

	protected TaxCategory
			testGetCommerceAdminSiteSettingGroupTaxCategoryPage_addTaxCategory(
				Long groupId, TaxCategory taxCategory)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetCommerceAdminSiteSettingGroupTaxCategoryPage_getGroupId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetCommerceAdminSiteSettingGroupTaxCategoryPage_getIrrelevantGroupId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostCommerceAdminSiteSettingGroupTaxCategory()
		throws Exception {

		TaxCategory randomTaxCategory = randomTaxCategory();

		TaxCategory postTaxCategory =
			testPostCommerceAdminSiteSettingGroupTaxCategory_addTaxCategory(
				randomTaxCategory);

		assertEquals(randomTaxCategory, postTaxCategory);
		assertValid(postTaxCategory);
	}

	protected TaxCategory
			testPostCommerceAdminSiteSettingGroupTaxCategory_addTaxCategory(
				TaxCategory taxCategory)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteTaxCategory() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		TaxCategory taxCategory = testDeleteTaxCategory_addTaxCategory();

		assertHttpResponseStatusCode(
			204,
			taxCategoryResource.deleteTaxCategoryHttpResponse(
				taxCategory.getId()));

		assertHttpResponseStatusCode(
			404,
			taxCategoryResource.getTaxCategoryHttpResponse(
				taxCategory.getId()));

		assertHttpResponseStatusCode(
			404,
			taxCategoryResource.getTaxCategoryHttpResponse(
				taxCategory.getId()));
	}

	protected TaxCategory testDeleteTaxCategory_addTaxCategory()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteTaxCategory() throws Exception {
		TaxCategory taxCategory = testGraphQLDeleteTaxCategory_addTaxCategory();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteTaxCategory",
						new HashMap<String, Object>() {
							{
								put("id", taxCategory.getId());
							}
						})),
				"JSONObject/data", "Object/deleteTaxCategory"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"taxCategory",
					new HashMap<String, Object>() {
						{
							put("id", taxCategory.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected TaxCategory testGraphQLDeleteTaxCategory_addTaxCategory()
		throws Exception {

		return testGraphQLTaxCategory_addTaxCategory();
	}

	@Test
	public void testGetTaxCategory() throws Exception {
		TaxCategory postTaxCategory = testGetTaxCategory_addTaxCategory();

		TaxCategory getTaxCategory = taxCategoryResource.getTaxCategory(
			postTaxCategory.getId());

		assertEquals(postTaxCategory, getTaxCategory);
		assertValid(getTaxCategory);
	}

	protected TaxCategory testGetTaxCategory_addTaxCategory() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetTaxCategory() throws Exception {
		TaxCategory taxCategory = testGraphQLGetTaxCategory_addTaxCategory();

		Assert.assertTrue(
			equals(
				taxCategory,
				TaxCategorySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"taxCategory",
								new HashMap<String, Object>() {
									{
										put("id", taxCategory.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/taxCategory"))));
	}

	@Test
	public void testGraphQLGetTaxCategoryNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"taxCategory",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected TaxCategory testGraphQLGetTaxCategory_addTaxCategory()
		throws Exception {

		return testGraphQLTaxCategory_addTaxCategory();
	}

	@Test
	public void testPutTaxCategory() throws Exception {
		Assert.assertTrue(false);
	}

	protected TaxCategory testGraphQLTaxCategory_addTaxCategory()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		TaxCategory taxCategory, List<TaxCategory> taxCategories) {

		boolean contains = false;

		for (TaxCategory item : taxCategories) {
			if (equals(taxCategory, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			taxCategories + " does not contain " + taxCategory, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		TaxCategory taxCategory1, TaxCategory taxCategory2) {

		Assert.assertTrue(
			taxCategory1 + " does not equal " + taxCategory2,
			equals(taxCategory1, taxCategory2));
	}

	protected void assertEquals(
		List<TaxCategory> taxCategories1, List<TaxCategory> taxCategories2) {

		Assert.assertEquals(taxCategories1.size(), taxCategories2.size());

		for (int i = 0; i < taxCategories1.size(); i++) {
			TaxCategory taxCategory1 = taxCategories1.get(i);
			TaxCategory taxCategory2 = taxCategories2.get(i);

			assertEquals(taxCategory1, taxCategory2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<TaxCategory> taxCategories1, List<TaxCategory> taxCategories2) {

		Assert.assertEquals(taxCategories1.size(), taxCategories2.size());

		for (TaxCategory taxCategory1 : taxCategories1) {
			boolean contains = false;

			for (TaxCategory taxCategory2 : taxCategories2) {
				if (equals(taxCategory1, taxCategory2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				taxCategories2 + " does not contain " + taxCategory1, contains);
		}
	}

	protected void assertValid(TaxCategory taxCategory) throws Exception {
		boolean valid = true;

		if (taxCategory.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (taxCategory.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("groupId", additionalAssertFieldName)) {
				if (taxCategory.getGroupId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (taxCategory.getName() == null) {
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

	protected void assertValid(Page<TaxCategory> page) {
		boolean valid = false;

		java.util.Collection<TaxCategory> taxCategories = page.getItems();

		int size = taxCategories.size();

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
					com.liferay.headless.commerce.admin.site.setting.dto.v1_0.
						TaxCategory.class)) {

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
		TaxCategory taxCategory1, TaxCategory taxCategory2) {

		if (taxCategory1 == taxCategory2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!equals(
						(Map)taxCategory1.getDescription(),
						(Map)taxCategory2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("groupId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						taxCategory1.getGroupId(), taxCategory2.getGroupId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						taxCategory1.getId(), taxCategory2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!equals(
						(Map)taxCategory1.getName(),
						(Map)taxCategory2.getName())) {

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

		if (!(_taxCategoryResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_taxCategoryResource;

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
		EntityField entityField, String operator, TaxCategory taxCategory) {

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

		if (entityFieldName.equals("groupId")) {
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

	protected TaxCategory randomTaxCategory() throws Exception {
		return new TaxCategory() {
			{
				groupId = RandomTestUtil.randomLong();
				id = RandomTestUtil.randomLong();
			}
		};
	}

	protected TaxCategory randomIrrelevantTaxCategory() throws Exception {
		TaxCategory randomIrrelevantTaxCategory = randomTaxCategory();

		return randomIrrelevantTaxCategory;
	}

	protected TaxCategory randomPatchTaxCategory() throws Exception {
		return randomTaxCategory();
	}

	protected TaxCategoryResource taxCategoryResource;
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
		LogFactoryUtil.getLog(BaseTaxCategoryResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.site.setting.resource.v1_0.
		TaxCategoryResource _taxCategoryResource;

}