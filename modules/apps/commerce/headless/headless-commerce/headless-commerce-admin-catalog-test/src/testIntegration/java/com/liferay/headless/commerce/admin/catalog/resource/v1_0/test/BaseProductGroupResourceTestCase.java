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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.ProductGroup;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.ProductGroupResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.ProductGroupSerDes;
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
public abstract class BaseProductGroupResourceTestCase {

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

		_productGroupResource.setContextCompany(testCompany);

		ProductGroupResource.Builder builder = ProductGroupResource.builder();

		productGroupResource = builder.authentication(
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

		ProductGroup productGroup1 = randomProductGroup();

		String json = objectMapper.writeValueAsString(productGroup1);

		ProductGroup productGroup2 = ProductGroupSerDes.toDTO(json);

		Assert.assertTrue(equals(productGroup1, productGroup2));
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

		ProductGroup productGroup = randomProductGroup();

		String json1 = objectMapper.writeValueAsString(productGroup);
		String json2 = ProductGroupSerDes.toJSON(productGroup);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ProductGroup productGroup = randomProductGroup();

		productGroup.setExternalReferenceCode(regex);

		String json = ProductGroupSerDes.toJSON(productGroup);

		Assert.assertFalse(json.contains(regex));

		productGroup = ProductGroupSerDes.toDTO(json);

		Assert.assertEquals(regex, productGroup.getExternalReferenceCode());
	}

	@Test
	public void testGetProductGroupsPage() throws Exception {
		Page<ProductGroup> page = productGroupResource.getProductGroupsPage(
			null, null, Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		ProductGroup productGroup1 = testGetProductGroupsPage_addProductGroup(
			randomProductGroup());

		ProductGroup productGroup2 = testGetProductGroupsPage_addProductGroup(
			randomProductGroup());

		page = productGroupResource.getProductGroupsPage(
			null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(productGroup1, (List<ProductGroup>)page.getItems());
		assertContains(productGroup2, (List<ProductGroup>)page.getItems());
		assertValid(page);

		productGroupResource.deleteProductGroup(productGroup1.getId());

		productGroupResource.deleteProductGroup(productGroup2.getId());
	}

	@Test
	public void testGetProductGroupsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		ProductGroup productGroup1 = randomProductGroup();

		productGroup1 = testGetProductGroupsPage_addProductGroup(productGroup1);

		for (EntityField entityField : entityFields) {
			Page<ProductGroup> page = productGroupResource.getProductGroupsPage(
				null, getFilterString(entityField, "between", productGroup1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(productGroup1),
				(List<ProductGroup>)page.getItems());
		}
	}

	@Test
	public void testGetProductGroupsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		ProductGroup productGroup1 = testGetProductGroupsPage_addProductGroup(
			randomProductGroup());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ProductGroup productGroup2 = testGetProductGroupsPage_addProductGroup(
			randomProductGroup());

		for (EntityField entityField : entityFields) {
			Page<ProductGroup> page = productGroupResource.getProductGroupsPage(
				null, getFilterString(entityField, "eq", productGroup1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(productGroup1),
				(List<ProductGroup>)page.getItems());
		}
	}

	@Test
	public void testGetProductGroupsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		ProductGroup productGroup1 = testGetProductGroupsPage_addProductGroup(
			randomProductGroup());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ProductGroup productGroup2 = testGetProductGroupsPage_addProductGroup(
			randomProductGroup());

		for (EntityField entityField : entityFields) {
			Page<ProductGroup> page = productGroupResource.getProductGroupsPage(
				null, getFilterString(entityField, "eq", productGroup1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(productGroup1),
				(List<ProductGroup>)page.getItems());
		}
	}

	@Test
	public void testGetProductGroupsPageWithPagination() throws Exception {
		Page<ProductGroup> totalPage =
			productGroupResource.getProductGroupsPage(null, null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		ProductGroup productGroup1 = testGetProductGroupsPage_addProductGroup(
			randomProductGroup());

		ProductGroup productGroup2 = testGetProductGroupsPage_addProductGroup(
			randomProductGroup());

		ProductGroup productGroup3 = testGetProductGroupsPage_addProductGroup(
			randomProductGroup());

		Page<ProductGroup> page1 = productGroupResource.getProductGroupsPage(
			null, null, Pagination.of(1, totalCount + 2), null);

		List<ProductGroup> productGroups1 =
			(List<ProductGroup>)page1.getItems();

		Assert.assertEquals(
			productGroups1.toString(), totalCount + 2, productGroups1.size());

		Page<ProductGroup> page2 = productGroupResource.getProductGroupsPage(
			null, null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<ProductGroup> productGroups2 =
			(List<ProductGroup>)page2.getItems();

		Assert.assertEquals(
			productGroups2.toString(), 1, productGroups2.size());

		Page<ProductGroup> page3 = productGroupResource.getProductGroupsPage(
			null, null, Pagination.of(1, totalCount + 3), null);

		assertContains(productGroup1, (List<ProductGroup>)page3.getItems());
		assertContains(productGroup2, (List<ProductGroup>)page3.getItems());
		assertContains(productGroup3, (List<ProductGroup>)page3.getItems());
	}

	@Test
	public void testGetProductGroupsPageWithSortDateTime() throws Exception {
		testGetProductGroupsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, productGroup1, productGroup2) -> {
				BeanTestUtil.setProperty(
					productGroup1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetProductGroupsPageWithSortDouble() throws Exception {
		testGetProductGroupsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, productGroup1, productGroup2) -> {
				BeanTestUtil.setProperty(
					productGroup1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					productGroup2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetProductGroupsPageWithSortInteger() throws Exception {
		testGetProductGroupsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, productGroup1, productGroup2) -> {
				BeanTestUtil.setProperty(
					productGroup1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					productGroup2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetProductGroupsPageWithSortString() throws Exception {
		testGetProductGroupsPageWithSort(
			EntityField.Type.STRING,
			(entityField, productGroup1, productGroup2) -> {
				Class<?> clazz = productGroup1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						productGroup1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						productGroup2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						productGroup1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						productGroup2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						productGroup1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						productGroup2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetProductGroupsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, ProductGroup, ProductGroup, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		ProductGroup productGroup1 = randomProductGroup();
		ProductGroup productGroup2 = randomProductGroup();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, productGroup1, productGroup2);
		}

		productGroup1 = testGetProductGroupsPage_addProductGroup(productGroup1);

		productGroup2 = testGetProductGroupsPage_addProductGroup(productGroup2);

		for (EntityField entityField : entityFields) {
			Page<ProductGroup> ascPage =
				productGroupResource.getProductGroupsPage(
					null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(productGroup1, productGroup2),
				(List<ProductGroup>)ascPage.getItems());

			Page<ProductGroup> descPage =
				productGroupResource.getProductGroupsPage(
					null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(productGroup2, productGroup1),
				(List<ProductGroup>)descPage.getItems());
		}
	}

	protected ProductGroup testGetProductGroupsPage_addProductGroup(
			ProductGroup productGroup)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetProductGroupsPage() throws Exception {
		GraphQLField graphQLField = new GraphQLField(
			"productGroups",
			new HashMap<String, Object>() {
				{
					put("page", 1);
					put("pageSize", 10);
				}
			},
			new GraphQLField("items", getGraphQLFields()),
			new GraphQLField("page"), new GraphQLField("totalCount"));

		JSONObject productGroupsJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/productGroups");

		long totalCount = productGroupsJSONObject.getLong("totalCount");

		ProductGroup productGroup1 =
			testGraphQLGetProductGroupsPage_addProductGroup();
		ProductGroup productGroup2 =
			testGraphQLGetProductGroupsPage_addProductGroup();

		productGroupsJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/productGroups");

		Assert.assertEquals(
			totalCount + 2, productGroupsJSONObject.getLong("totalCount"));

		assertContains(
			productGroup1,
			Arrays.asList(
				ProductGroupSerDes.toDTOs(
					productGroupsJSONObject.getString("items"))));
		assertContains(
			productGroup2,
			Arrays.asList(
				ProductGroupSerDes.toDTOs(
					productGroupsJSONObject.getString("items"))));
	}

	protected ProductGroup testGraphQLGetProductGroupsPage_addProductGroup()
		throws Exception {

		return testGraphQLProductGroup_addProductGroup();
	}

	@Test
	public void testPostProductGroup() throws Exception {
		ProductGroup randomProductGroup = randomProductGroup();

		ProductGroup postProductGroup = testPostProductGroup_addProductGroup(
			randomProductGroup);

		assertEquals(randomProductGroup, postProductGroup);
		assertValid(postProductGroup);
	}

	protected ProductGroup testPostProductGroup_addProductGroup(
			ProductGroup productGroup)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteProductGroupByExternalReferenceCode()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ProductGroup productGroup =
			testDeleteProductGroupByExternalReferenceCode_addProductGroup();

		assertHttpResponseStatusCode(
			204,
			productGroupResource.
				deleteProductGroupByExternalReferenceCodeHttpResponse(
					productGroup.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			productGroupResource.
				getProductGroupByExternalReferenceCodeHttpResponse(
					productGroup.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			productGroupResource.
				getProductGroupByExternalReferenceCodeHttpResponse(
					productGroup.getExternalReferenceCode()));
	}

	protected ProductGroup
			testDeleteProductGroupByExternalReferenceCode_addProductGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetProductGroupByExternalReferenceCode() throws Exception {
		ProductGroup postProductGroup =
			testGetProductGroupByExternalReferenceCode_addProductGroup();

		ProductGroup getProductGroup =
			productGroupResource.getProductGroupByExternalReferenceCode(
				postProductGroup.getExternalReferenceCode());

		assertEquals(postProductGroup, getProductGroup);
		assertValid(getProductGroup);
	}

	protected ProductGroup
			testGetProductGroupByExternalReferenceCode_addProductGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetProductGroupByExternalReferenceCode()
		throws Exception {

		ProductGroup productGroup =
			testGraphQLGetProductGroupByExternalReferenceCode_addProductGroup();

		Assert.assertTrue(
			equals(
				productGroup,
				ProductGroupSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"productGroupByExternalReferenceCode",
								new HashMap<String, Object>() {
									{
										put(
											"externalReferenceCode",
											"\"" +
												productGroup.
													getExternalReferenceCode() +
														"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/productGroupByExternalReferenceCode"))));
	}

	@Test
	public void testGraphQLGetProductGroupByExternalReferenceCodeNotFound()
		throws Exception {

		String irrelevantExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"productGroupByExternalReferenceCode",
						new HashMap<String, Object>() {
							{
								put(
									"externalReferenceCode",
									irrelevantExternalReferenceCode);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected ProductGroup
			testGraphQLGetProductGroupByExternalReferenceCode_addProductGroup()
		throws Exception {

		return testGraphQLProductGroup_addProductGroup();
	}

	@Test
	public void testPatchProductGroupByExternalReferenceCode()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testDeleteProductGroup() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		ProductGroup productGroup = testDeleteProductGroup_addProductGroup();

		assertHttpResponseStatusCode(
			204,
			productGroupResource.deleteProductGroupHttpResponse(
				productGroup.getId()));

		assertHttpResponseStatusCode(
			404,
			productGroupResource.getProductGroupHttpResponse(
				productGroup.getId()));

		assertHttpResponseStatusCode(
			404,
			productGroupResource.getProductGroupHttpResponse(
				productGroup.getId()));
	}

	protected ProductGroup testDeleteProductGroup_addProductGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteProductGroup() throws Exception {
		ProductGroup productGroup =
			testGraphQLDeleteProductGroup_addProductGroup();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteProductGroup",
						new HashMap<String, Object>() {
							{
								put("id", productGroup.getId());
							}
						})),
				"JSONObject/data", "Object/deleteProductGroup"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"productGroup",
					new HashMap<String, Object>() {
						{
							put("id", productGroup.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected ProductGroup testGraphQLDeleteProductGroup_addProductGroup()
		throws Exception {

		return testGraphQLProductGroup_addProductGroup();
	}

	@Test
	public void testGetProductGroup() throws Exception {
		ProductGroup postProductGroup = testGetProductGroup_addProductGroup();

		ProductGroup getProductGroup = productGroupResource.getProductGroup(
			postProductGroup.getId());

		assertEquals(postProductGroup, getProductGroup);
		assertValid(getProductGroup);
	}

	protected ProductGroup testGetProductGroup_addProductGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetProductGroup() throws Exception {
		ProductGroup productGroup =
			testGraphQLGetProductGroup_addProductGroup();

		Assert.assertTrue(
			equals(
				productGroup,
				ProductGroupSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"productGroup",
								new HashMap<String, Object>() {
									{
										put("id", productGroup.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/productGroup"))));
	}

	@Test
	public void testGraphQLGetProductGroupNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"productGroup",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected ProductGroup testGraphQLGetProductGroup_addProductGroup()
		throws Exception {

		return testGraphQLProductGroup_addProductGroup();
	}

	@Test
	public void testPatchProductGroup() throws Exception {
		Assert.assertTrue(false);
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected ProductGroup testGraphQLProductGroup_addProductGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		ProductGroup productGroup, List<ProductGroup> productGroups) {

		boolean contains = false;

		for (ProductGroup item : productGroups) {
			if (equals(productGroup, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			productGroups + " does not contain " + productGroup, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ProductGroup productGroup1, ProductGroup productGroup2) {

		Assert.assertTrue(
			productGroup1 + " does not equal " + productGroup2,
			equals(productGroup1, productGroup2));
	}

	protected void assertEquals(
		List<ProductGroup> productGroups1, List<ProductGroup> productGroups2) {

		Assert.assertEquals(productGroups1.size(), productGroups2.size());

		for (int i = 0; i < productGroups1.size(); i++) {
			ProductGroup productGroup1 = productGroups1.get(i);
			ProductGroup productGroup2 = productGroups2.get(i);

			assertEquals(productGroup1, productGroup2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ProductGroup> productGroups1, List<ProductGroup> productGroups2) {

		Assert.assertEquals(productGroups1.size(), productGroups2.size());

		for (ProductGroup productGroup1 : productGroups1) {
			boolean contains = false;

			for (ProductGroup productGroup2 : productGroups2) {
				if (equals(productGroup1, productGroup2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				productGroups2 + " does not contain " + productGroup1,
				contains);
		}
	}

	protected void assertValid(ProductGroup productGroup) throws Exception {
		boolean valid = true;

		if (productGroup.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (productGroup.getCustomFields() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (productGroup.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (productGroup.getExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("products", additionalAssertFieldName)) {
				if (productGroup.getProducts() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productsCount", additionalAssertFieldName)) {
				if (productGroup.getProductsCount() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("title", additionalAssertFieldName)) {
				if (productGroup.getTitle() == null) {
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

	protected void assertValid(Page<ProductGroup> page) {
		boolean valid = false;

		java.util.Collection<ProductGroup> productGroups = page.getItems();

		int size = productGroups.size();

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
						ProductGroup.class)) {

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
		ProductGroup productGroup1, ProductGroup productGroup2) {

		if (productGroup1 == productGroup2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (!equals(
						(Map)productGroup1.getCustomFields(),
						(Map)productGroup2.getCustomFields())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!equals(
						(Map)productGroup1.getDescription(),
						(Map)productGroup2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productGroup1.getExternalReferenceCode(),
						productGroup2.getExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productGroup1.getId(), productGroup2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("products", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productGroup1.getProducts(),
						productGroup2.getProducts())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productsCount", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productGroup1.getProductsCount(),
						productGroup2.getProductsCount())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("title", additionalAssertFieldName)) {
				if (!equals(
						(Map)productGroup1.getTitle(),
						(Map)productGroup2.getTitle())) {

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

		if (!(_productGroupResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_productGroupResource;

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
		EntityField entityField, String operator, ProductGroup productGroup) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("customFields")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("description")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("externalReferenceCode")) {
			sb.append("'");
			sb.append(String.valueOf(productGroup.getExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("products")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("productsCount")) {
			sb.append(String.valueOf(productGroup.getProductsCount()));

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

	protected ProductGroup randomProductGroup() throws Exception {
		return new ProductGroup() {
			{
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				productsCount = RandomTestUtil.randomInt();
			}
		};
	}

	protected ProductGroup randomIrrelevantProductGroup() throws Exception {
		ProductGroup randomIrrelevantProductGroup = randomProductGroup();

		return randomIrrelevantProductGroup;
	}

	protected ProductGroup randomPatchProductGroup() throws Exception {
		return randomProductGroup();
	}

	protected ProductGroupResource productGroupResource;
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
		LogFactoryUtil.getLog(BaseProductGroupResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		ProductGroupResource _productGroupResource;

}