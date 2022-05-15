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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.ProductAccountGroup;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.ProductAccountGroupResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.ProductAccountGroupSerDes;
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
public abstract class BaseProductAccountGroupResourceTestCase {

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

		_productAccountGroupResource.setContextCompany(testCompany);

		ProductAccountGroupResource.Builder builder =
			ProductAccountGroupResource.builder();

		productAccountGroupResource = builder.authentication(
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

		ProductAccountGroup productAccountGroup1 = randomProductAccountGroup();

		String json = objectMapper.writeValueAsString(productAccountGroup1);

		ProductAccountGroup productAccountGroup2 =
			ProductAccountGroupSerDes.toDTO(json);

		Assert.assertTrue(equals(productAccountGroup1, productAccountGroup2));
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

		ProductAccountGroup productAccountGroup = randomProductAccountGroup();

		String json1 = objectMapper.writeValueAsString(productAccountGroup);
		String json2 = ProductAccountGroupSerDes.toJSON(productAccountGroup);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ProductAccountGroup productAccountGroup = randomProductAccountGroup();

		productAccountGroup.setExternalReferenceCode(regex);
		productAccountGroup.setName(regex);

		String json = ProductAccountGroupSerDes.toJSON(productAccountGroup);

		Assert.assertFalse(json.contains(regex));

		productAccountGroup = ProductAccountGroupSerDes.toDTO(json);

		Assert.assertEquals(
			regex, productAccountGroup.getExternalReferenceCode());
		Assert.assertEquals(regex, productAccountGroup.getName());
	}

	@Test
	public void testDeleteProductAccountGroup() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		ProductAccountGroup productAccountGroup =
			testDeleteProductAccountGroup_addProductAccountGroup();

		assertHttpResponseStatusCode(
			204,
			productAccountGroupResource.deleteProductAccountGroupHttpResponse(
				productAccountGroup.getId()));

		assertHttpResponseStatusCode(
			404,
			productAccountGroupResource.getProductAccountGroupHttpResponse(
				productAccountGroup.getId()));

		assertHttpResponseStatusCode(
			404,
			productAccountGroupResource.getProductAccountGroupHttpResponse(
				productAccountGroup.getId()));
	}

	protected ProductAccountGroup
			testDeleteProductAccountGroup_addProductAccountGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteProductAccountGroup() throws Exception {
		ProductAccountGroup productAccountGroup =
			testGraphQLDeleteProductAccountGroup_addProductAccountGroup();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteProductAccountGroup",
						new HashMap<String, Object>() {
							{
								put("id", productAccountGroup.getId());
							}
						})),
				"JSONObject/data", "Object/deleteProductAccountGroup"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"productAccountGroup",
					new HashMap<String, Object>() {
						{
							put("id", productAccountGroup.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected ProductAccountGroup
			testGraphQLDeleteProductAccountGroup_addProductAccountGroup()
		throws Exception {

		return testGraphQLProductAccountGroup_addProductAccountGroup();
	}

	@Test
	public void testGetProductAccountGroup() throws Exception {
		ProductAccountGroup postProductAccountGroup =
			testGetProductAccountGroup_addProductAccountGroup();

		ProductAccountGroup getProductAccountGroup =
			productAccountGroupResource.getProductAccountGroup(
				postProductAccountGroup.getId());

		assertEquals(postProductAccountGroup, getProductAccountGroup);
		assertValid(getProductAccountGroup);
	}

	protected ProductAccountGroup
			testGetProductAccountGroup_addProductAccountGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetProductAccountGroup() throws Exception {
		ProductAccountGroup productAccountGroup =
			testGraphQLGetProductAccountGroup_addProductAccountGroup();

		Assert.assertTrue(
			equals(
				productAccountGroup,
				ProductAccountGroupSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"productAccountGroup",
								new HashMap<String, Object>() {
									{
										put("id", productAccountGroup.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/productAccountGroup"))));
	}

	@Test
	public void testGraphQLGetProductAccountGroupNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"productAccountGroup",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected ProductAccountGroup
			testGraphQLGetProductAccountGroup_addProductAccountGroup()
		throws Exception {

		return testGraphQLProductAccountGroup_addProductAccountGroup();
	}

	@Test
	public void testGetProductByExternalReferenceCodeProductAccountGroupsPage()
		throws Exception {

		String externalReferenceCode =
			testGetProductByExternalReferenceCodeProductAccountGroupsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetProductByExternalReferenceCodeProductAccountGroupsPage_getIrrelevantExternalReferenceCode();

		Page<ProductAccountGroup> page =
			productAccountGroupResource.
				getProductByExternalReferenceCodeProductAccountGroupsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			ProductAccountGroup irrelevantProductAccountGroup =
				testGetProductByExternalReferenceCodeProductAccountGroupsPage_addProductAccountGroup(
					irrelevantExternalReferenceCode,
					randomIrrelevantProductAccountGroup());

			page =
				productAccountGroupResource.
					getProductByExternalReferenceCodeProductAccountGroupsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantProductAccountGroup),
				(List<ProductAccountGroup>)page.getItems());
			assertValid(page);
		}

		ProductAccountGroup productAccountGroup1 =
			testGetProductByExternalReferenceCodeProductAccountGroupsPage_addProductAccountGroup(
				externalReferenceCode, randomProductAccountGroup());

		ProductAccountGroup productAccountGroup2 =
			testGetProductByExternalReferenceCodeProductAccountGroupsPage_addProductAccountGroup(
				externalReferenceCode, randomProductAccountGroup());

		page =
			productAccountGroupResource.
				getProductByExternalReferenceCodeProductAccountGroupsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(productAccountGroup1, productAccountGroup2),
			(List<ProductAccountGroup>)page.getItems());
		assertValid(page);

		productAccountGroupResource.deleteProductAccountGroup(
			productAccountGroup1.getId());

		productAccountGroupResource.deleteProductAccountGroup(
			productAccountGroup2.getId());
	}

	@Test
	public void testGetProductByExternalReferenceCodeProductAccountGroupsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetProductByExternalReferenceCodeProductAccountGroupsPage_getExternalReferenceCode();

		ProductAccountGroup productAccountGroup1 =
			testGetProductByExternalReferenceCodeProductAccountGroupsPage_addProductAccountGroup(
				externalReferenceCode, randomProductAccountGroup());

		ProductAccountGroup productAccountGroup2 =
			testGetProductByExternalReferenceCodeProductAccountGroupsPage_addProductAccountGroup(
				externalReferenceCode, randomProductAccountGroup());

		ProductAccountGroup productAccountGroup3 =
			testGetProductByExternalReferenceCodeProductAccountGroupsPage_addProductAccountGroup(
				externalReferenceCode, randomProductAccountGroup());

		Page<ProductAccountGroup> page1 =
			productAccountGroupResource.
				getProductByExternalReferenceCodeProductAccountGroupsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<ProductAccountGroup> productAccountGroups1 =
			(List<ProductAccountGroup>)page1.getItems();

		Assert.assertEquals(
			productAccountGroups1.toString(), 2, productAccountGroups1.size());

		Page<ProductAccountGroup> page2 =
			productAccountGroupResource.
				getProductByExternalReferenceCodeProductAccountGroupsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<ProductAccountGroup> productAccountGroups2 =
			(List<ProductAccountGroup>)page2.getItems();

		Assert.assertEquals(
			productAccountGroups2.toString(), 1, productAccountGroups2.size());

		Page<ProductAccountGroup> page3 =
			productAccountGroupResource.
				getProductByExternalReferenceCodeProductAccountGroupsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				productAccountGroup1, productAccountGroup2,
				productAccountGroup3),
			(List<ProductAccountGroup>)page3.getItems());
	}

	protected ProductAccountGroup
			testGetProductByExternalReferenceCodeProductAccountGroupsPage_addProductAccountGroup(
				String externalReferenceCode,
				ProductAccountGroup productAccountGroup)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetProductByExternalReferenceCodeProductAccountGroupsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetProductByExternalReferenceCodeProductAccountGroupsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testGetProductIdProductAccountGroupsPage() throws Exception {
		Long id = testGetProductIdProductAccountGroupsPage_getId();
		Long irrelevantId =
			testGetProductIdProductAccountGroupsPage_getIrrelevantId();

		Page<ProductAccountGroup> page =
			productAccountGroupResource.getProductIdProductAccountGroupsPage(
				id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			ProductAccountGroup irrelevantProductAccountGroup =
				testGetProductIdProductAccountGroupsPage_addProductAccountGroup(
					irrelevantId, randomIrrelevantProductAccountGroup());

			page =
				productAccountGroupResource.
					getProductIdProductAccountGroupsPage(
						irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantProductAccountGroup),
				(List<ProductAccountGroup>)page.getItems());
			assertValid(page);
		}

		ProductAccountGroup productAccountGroup1 =
			testGetProductIdProductAccountGroupsPage_addProductAccountGroup(
				id, randomProductAccountGroup());

		ProductAccountGroup productAccountGroup2 =
			testGetProductIdProductAccountGroupsPage_addProductAccountGroup(
				id, randomProductAccountGroup());

		page = productAccountGroupResource.getProductIdProductAccountGroupsPage(
			id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(productAccountGroup1, productAccountGroup2),
			(List<ProductAccountGroup>)page.getItems());
		assertValid(page);

		productAccountGroupResource.deleteProductAccountGroup(
			productAccountGroup1.getId());

		productAccountGroupResource.deleteProductAccountGroup(
			productAccountGroup2.getId());
	}

	@Test
	public void testGetProductIdProductAccountGroupsPageWithPagination()
		throws Exception {

		Long id = testGetProductIdProductAccountGroupsPage_getId();

		ProductAccountGroup productAccountGroup1 =
			testGetProductIdProductAccountGroupsPage_addProductAccountGroup(
				id, randomProductAccountGroup());

		ProductAccountGroup productAccountGroup2 =
			testGetProductIdProductAccountGroupsPage_addProductAccountGroup(
				id, randomProductAccountGroup());

		ProductAccountGroup productAccountGroup3 =
			testGetProductIdProductAccountGroupsPage_addProductAccountGroup(
				id, randomProductAccountGroup());

		Page<ProductAccountGroup> page1 =
			productAccountGroupResource.getProductIdProductAccountGroupsPage(
				id, Pagination.of(1, 2));

		List<ProductAccountGroup> productAccountGroups1 =
			(List<ProductAccountGroup>)page1.getItems();

		Assert.assertEquals(
			productAccountGroups1.toString(), 2, productAccountGroups1.size());

		Page<ProductAccountGroup> page2 =
			productAccountGroupResource.getProductIdProductAccountGroupsPage(
				id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<ProductAccountGroup> productAccountGroups2 =
			(List<ProductAccountGroup>)page2.getItems();

		Assert.assertEquals(
			productAccountGroups2.toString(), 1, productAccountGroups2.size());

		Page<ProductAccountGroup> page3 =
			productAccountGroupResource.getProductIdProductAccountGroupsPage(
				id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				productAccountGroup1, productAccountGroup2,
				productAccountGroup3),
			(List<ProductAccountGroup>)page3.getItems());
	}

	protected ProductAccountGroup
			testGetProductIdProductAccountGroupsPage_addProductAccountGroup(
				Long id, ProductAccountGroup productAccountGroup)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductIdProductAccountGroupsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductIdProductAccountGroupsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	protected ProductAccountGroup
			testGraphQLProductAccountGroup_addProductAccountGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		ProductAccountGroup productAccountGroup,
		List<ProductAccountGroup> productAccountGroups) {

		boolean contains = false;

		for (ProductAccountGroup item : productAccountGroups) {
			if (equals(productAccountGroup, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			productAccountGroups + " does not contain " + productAccountGroup,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ProductAccountGroup productAccountGroup1,
		ProductAccountGroup productAccountGroup2) {

		Assert.assertTrue(
			productAccountGroup1 + " does not equal " + productAccountGroup2,
			equals(productAccountGroup1, productAccountGroup2));
	}

	protected void assertEquals(
		List<ProductAccountGroup> productAccountGroups1,
		List<ProductAccountGroup> productAccountGroups2) {

		Assert.assertEquals(
			productAccountGroups1.size(), productAccountGroups2.size());

		for (int i = 0; i < productAccountGroups1.size(); i++) {
			ProductAccountGroup productAccountGroup1 =
				productAccountGroups1.get(i);
			ProductAccountGroup productAccountGroup2 =
				productAccountGroups2.get(i);

			assertEquals(productAccountGroup1, productAccountGroup2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ProductAccountGroup> productAccountGroups1,
		List<ProductAccountGroup> productAccountGroups2) {

		Assert.assertEquals(
			productAccountGroups1.size(), productAccountGroups2.size());

		for (ProductAccountGroup productAccountGroup1 : productAccountGroups1) {
			boolean contains = false;

			for (ProductAccountGroup productAccountGroup2 :
					productAccountGroups2) {

				if (equals(productAccountGroup1, productAccountGroup2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				productAccountGroups2 + " does not contain " +
					productAccountGroup1,
				contains);
		}
	}

	protected void assertValid(ProductAccountGroup productAccountGroup)
		throws Exception {

		boolean valid = true;

		if (productAccountGroup.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("accountGroupId", additionalAssertFieldName)) {
				if (productAccountGroup.getAccountGroupId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (productAccountGroup.getExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (productAccountGroup.getName() == null) {
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

	protected void assertValid(Page<ProductAccountGroup> page) {
		boolean valid = false;

		java.util.Collection<ProductAccountGroup> productAccountGroups =
			page.getItems();

		int size = productAccountGroups.size();

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
						ProductAccountGroup.class)) {

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
		ProductAccountGroup productAccountGroup1,
		ProductAccountGroup productAccountGroup2) {

		if (productAccountGroup1 == productAccountGroup2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("accountGroupId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productAccountGroup1.getAccountGroupId(),
						productAccountGroup2.getAccountGroupId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productAccountGroup1.getExternalReferenceCode(),
						productAccountGroup2.getExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productAccountGroup1.getId(),
						productAccountGroup2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productAccountGroup1.getName(),
						productAccountGroup2.getName())) {

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

		if (!(_productAccountGroupResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_productAccountGroupResource;

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
		ProductAccountGroup productAccountGroup) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("accountGroupId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("externalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(productAccountGroup.getExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(productAccountGroup.getName()));
			sb.append("'");

			return sb.toString();
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

	protected ProductAccountGroup randomProductAccountGroup() throws Exception {
		return new ProductAccountGroup() {
			{
				accountGroupId = RandomTestUtil.randomLong();
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected ProductAccountGroup randomIrrelevantProductAccountGroup()
		throws Exception {

		ProductAccountGroup randomIrrelevantProductAccountGroup =
			randomProductAccountGroup();

		return randomIrrelevantProductAccountGroup;
	}

	protected ProductAccountGroup randomPatchProductAccountGroup()
		throws Exception {

		return randomProductAccountGroup();
	}

	protected ProductAccountGroupResource productAccountGroupResource;
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
		LogFactoryUtil.getLog(BaseProductAccountGroupResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		ProductAccountGroupResource _productAccountGroupResource;

}