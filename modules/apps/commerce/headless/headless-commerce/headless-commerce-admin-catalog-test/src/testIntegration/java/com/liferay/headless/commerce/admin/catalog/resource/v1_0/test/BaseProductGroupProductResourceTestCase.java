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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.ProductGroupProduct;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.ProductGroupProductResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.ProductGroupProductSerDes;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
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
public abstract class BaseProductGroupProductResourceTestCase {

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

		_productGroupProductResource.setContextCompany(testCompany);

		ProductGroupProductResource.Builder builder =
			ProductGroupProductResource.builder();

		productGroupProductResource = builder.authentication(
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

		ProductGroupProduct productGroupProduct1 = randomProductGroupProduct();

		String json = objectMapper.writeValueAsString(productGroupProduct1);

		ProductGroupProduct productGroupProduct2 =
			ProductGroupProductSerDes.toDTO(json);

		Assert.assertTrue(equals(productGroupProduct1, productGroupProduct2));
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

		ProductGroupProduct productGroupProduct = randomProductGroupProduct();

		String json1 = objectMapper.writeValueAsString(productGroupProduct);
		String json2 = ProductGroupProductSerDes.toJSON(productGroupProduct);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ProductGroupProduct productGroupProduct = randomProductGroupProduct();

		productGroupProduct.setProductExternalReferenceCode(regex);
		productGroupProduct.setProductGroupExternalReferenceCode(regex);
		productGroupProduct.setProductName(regex);
		productGroupProduct.setSku(regex);

		String json = ProductGroupProductSerDes.toJSON(productGroupProduct);

		Assert.assertFalse(json.contains(regex));

		productGroupProduct = ProductGroupProductSerDes.toDTO(json);

		Assert.assertEquals(
			regex, productGroupProduct.getProductExternalReferenceCode());
		Assert.assertEquals(
			regex, productGroupProduct.getProductGroupExternalReferenceCode());
		Assert.assertEquals(regex, productGroupProduct.getProductName());
		Assert.assertEquals(regex, productGroupProduct.getSku());
	}

	@Test
	public void testDeleteProductGroupProduct() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		ProductGroupProduct productGroupProduct =
			testDeleteProductGroupProduct_addProductGroupProduct();

		assertHttpResponseStatusCode(
			204,
			productGroupProductResource.deleteProductGroupProductHttpResponse(
				productGroupProduct.getId()));
	}

	protected ProductGroupProduct
			testDeleteProductGroupProduct_addProductGroupProduct()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteProductGroupProduct() throws Exception {
		ProductGroupProduct productGroupProduct =
			testGraphQLDeleteProductGroupProduct_addProductGroupProduct();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteProductGroupProduct",
						new HashMap<String, Object>() {
							{
								put("id", productGroupProduct.getId());
							}
						})),
				"JSONObject/data", "Object/deleteProductGroupProduct"));
	}

	protected ProductGroupProduct
			testGraphQLDeleteProductGroupProduct_addProductGroupProduct()
		throws Exception {

		return testGraphQLProductGroupProduct_addProductGroupProduct();
	}

	@Test
	public void testGetProductGroupByExternalReferenceCodeProductGroupProductsPage()
		throws Exception {

		String externalReferenceCode =
			testGetProductGroupByExternalReferenceCodeProductGroupProductsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetProductGroupByExternalReferenceCodeProductGroupProductsPage_getIrrelevantExternalReferenceCode();

		Page<ProductGroupProduct> page =
			productGroupProductResource.
				getProductGroupByExternalReferenceCodeProductGroupProductsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			ProductGroupProduct irrelevantProductGroupProduct =
				testGetProductGroupByExternalReferenceCodeProductGroupProductsPage_addProductGroupProduct(
					irrelevantExternalReferenceCode,
					randomIrrelevantProductGroupProduct());

			page =
				productGroupProductResource.
					getProductGroupByExternalReferenceCodeProductGroupProductsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantProductGroupProduct),
				(List<ProductGroupProduct>)page.getItems());
			assertValid(page);
		}

		ProductGroupProduct productGroupProduct1 =
			testGetProductGroupByExternalReferenceCodeProductGroupProductsPage_addProductGroupProduct(
				externalReferenceCode, randomProductGroupProduct());

		ProductGroupProduct productGroupProduct2 =
			testGetProductGroupByExternalReferenceCodeProductGroupProductsPage_addProductGroupProduct(
				externalReferenceCode, randomProductGroupProduct());

		page =
			productGroupProductResource.
				getProductGroupByExternalReferenceCodeProductGroupProductsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(productGroupProduct1, productGroupProduct2),
			(List<ProductGroupProduct>)page.getItems());
		assertValid(page);

		productGroupProductResource.deleteProductGroupProduct(
			productGroupProduct1.getId());

		productGroupProductResource.deleteProductGroupProduct(
			productGroupProduct2.getId());
	}

	@Test
	public void testGetProductGroupByExternalReferenceCodeProductGroupProductsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetProductGroupByExternalReferenceCodeProductGroupProductsPage_getExternalReferenceCode();

		ProductGroupProduct productGroupProduct1 =
			testGetProductGroupByExternalReferenceCodeProductGroupProductsPage_addProductGroupProduct(
				externalReferenceCode, randomProductGroupProduct());

		ProductGroupProduct productGroupProduct2 =
			testGetProductGroupByExternalReferenceCodeProductGroupProductsPage_addProductGroupProduct(
				externalReferenceCode, randomProductGroupProduct());

		ProductGroupProduct productGroupProduct3 =
			testGetProductGroupByExternalReferenceCodeProductGroupProductsPage_addProductGroupProduct(
				externalReferenceCode, randomProductGroupProduct());

		Page<ProductGroupProduct> page1 =
			productGroupProductResource.
				getProductGroupByExternalReferenceCodeProductGroupProductsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<ProductGroupProduct> productGroupProducts1 =
			(List<ProductGroupProduct>)page1.getItems();

		Assert.assertEquals(
			productGroupProducts1.toString(), 2, productGroupProducts1.size());

		Page<ProductGroupProduct> page2 =
			productGroupProductResource.
				getProductGroupByExternalReferenceCodeProductGroupProductsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<ProductGroupProduct> productGroupProducts2 =
			(List<ProductGroupProduct>)page2.getItems();

		Assert.assertEquals(
			productGroupProducts2.toString(), 1, productGroupProducts2.size());

		Page<ProductGroupProduct> page3 =
			productGroupProductResource.
				getProductGroupByExternalReferenceCodeProductGroupProductsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				productGroupProduct1, productGroupProduct2,
				productGroupProduct3),
			(List<ProductGroupProduct>)page3.getItems());
	}

	protected ProductGroupProduct
			testGetProductGroupByExternalReferenceCodeProductGroupProductsPage_addProductGroupProduct(
				String externalReferenceCode,
				ProductGroupProduct productGroupProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetProductGroupByExternalReferenceCodeProductGroupProductsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetProductGroupByExternalReferenceCodeProductGroupProductsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostProductGroupByExternalReferenceCodeProductGroupProduct()
		throws Exception {

		ProductGroupProduct randomProductGroupProduct =
			randomProductGroupProduct();

		ProductGroupProduct postProductGroupProduct =
			testPostProductGroupByExternalReferenceCodeProductGroupProduct_addProductGroupProduct(
				randomProductGroupProduct);

		assertEquals(randomProductGroupProduct, postProductGroupProduct);
		assertValid(postProductGroupProduct);
	}

	protected ProductGroupProduct
			testPostProductGroupByExternalReferenceCodeProductGroupProduct_addProductGroupProduct(
				ProductGroupProduct productGroupProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetProductGroupIdProductGroupProductsPage()
		throws Exception {

		Long id = testGetProductGroupIdProductGroupProductsPage_getId();
		Long irrelevantId =
			testGetProductGroupIdProductGroupProductsPage_getIrrelevantId();

		Page<ProductGroupProduct> page =
			productGroupProductResource.
				getProductGroupIdProductGroupProductsPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			ProductGroupProduct irrelevantProductGroupProduct =
				testGetProductGroupIdProductGroupProductsPage_addProductGroupProduct(
					irrelevantId, randomIrrelevantProductGroupProduct());

			page =
				productGroupProductResource.
					getProductGroupIdProductGroupProductsPage(
						irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantProductGroupProduct),
				(List<ProductGroupProduct>)page.getItems());
			assertValid(page);
		}

		ProductGroupProduct productGroupProduct1 =
			testGetProductGroupIdProductGroupProductsPage_addProductGroupProduct(
				id, randomProductGroupProduct());

		ProductGroupProduct productGroupProduct2 =
			testGetProductGroupIdProductGroupProductsPage_addProductGroupProduct(
				id, randomProductGroupProduct());

		page =
			productGroupProductResource.
				getProductGroupIdProductGroupProductsPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(productGroupProduct1, productGroupProduct2),
			(List<ProductGroupProduct>)page.getItems());
		assertValid(page);

		productGroupProductResource.deleteProductGroupProduct(
			productGroupProduct1.getId());

		productGroupProductResource.deleteProductGroupProduct(
			productGroupProduct2.getId());
	}

	@Test
	public void testGetProductGroupIdProductGroupProductsPageWithPagination()
		throws Exception {

		Long id = testGetProductGroupIdProductGroupProductsPage_getId();

		ProductGroupProduct productGroupProduct1 =
			testGetProductGroupIdProductGroupProductsPage_addProductGroupProduct(
				id, randomProductGroupProduct());

		ProductGroupProduct productGroupProduct2 =
			testGetProductGroupIdProductGroupProductsPage_addProductGroupProduct(
				id, randomProductGroupProduct());

		ProductGroupProduct productGroupProduct3 =
			testGetProductGroupIdProductGroupProductsPage_addProductGroupProduct(
				id, randomProductGroupProduct());

		Page<ProductGroupProduct> page1 =
			productGroupProductResource.
				getProductGroupIdProductGroupProductsPage(
					id, Pagination.of(1, 2));

		List<ProductGroupProduct> productGroupProducts1 =
			(List<ProductGroupProduct>)page1.getItems();

		Assert.assertEquals(
			productGroupProducts1.toString(), 2, productGroupProducts1.size());

		Page<ProductGroupProduct> page2 =
			productGroupProductResource.
				getProductGroupIdProductGroupProductsPage(
					id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<ProductGroupProduct> productGroupProducts2 =
			(List<ProductGroupProduct>)page2.getItems();

		Assert.assertEquals(
			productGroupProducts2.toString(), 1, productGroupProducts2.size());

		Page<ProductGroupProduct> page3 =
			productGroupProductResource.
				getProductGroupIdProductGroupProductsPage(
					id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				productGroupProduct1, productGroupProduct2,
				productGroupProduct3),
			(List<ProductGroupProduct>)page3.getItems());
	}

	protected ProductGroupProduct
			testGetProductGroupIdProductGroupProductsPage_addProductGroupProduct(
				Long id, ProductGroupProduct productGroupProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductGroupIdProductGroupProductsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetProductGroupIdProductGroupProductsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostProductGroupIdProductGroupProduct() throws Exception {
		ProductGroupProduct randomProductGroupProduct =
			randomProductGroupProduct();

		ProductGroupProduct postProductGroupProduct =
			testPostProductGroupIdProductGroupProduct_addProductGroupProduct(
				randomProductGroupProduct);

		assertEquals(randomProductGroupProduct, postProductGroupProduct);
		assertValid(postProductGroupProduct);
	}

	protected ProductGroupProduct
			testPostProductGroupIdProductGroupProduct_addProductGroupProduct(
				ProductGroupProduct productGroupProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected ProductGroupProduct
			testGraphQLProductGroupProduct_addProductGroupProduct()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		ProductGroupProduct productGroupProduct,
		List<ProductGroupProduct> productGroupProducts) {

		boolean contains = false;

		for (ProductGroupProduct item : productGroupProducts) {
			if (equals(productGroupProduct, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			productGroupProducts + " does not contain " + productGroupProduct,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ProductGroupProduct productGroupProduct1,
		ProductGroupProduct productGroupProduct2) {

		Assert.assertTrue(
			productGroupProduct1 + " does not equal " + productGroupProduct2,
			equals(productGroupProduct1, productGroupProduct2));
	}

	protected void assertEquals(
		List<ProductGroupProduct> productGroupProducts1,
		List<ProductGroupProduct> productGroupProducts2) {

		Assert.assertEquals(
			productGroupProducts1.size(), productGroupProducts2.size());

		for (int i = 0; i < productGroupProducts1.size(); i++) {
			ProductGroupProduct productGroupProduct1 =
				productGroupProducts1.get(i);
			ProductGroupProduct productGroupProduct2 =
				productGroupProducts2.get(i);

			assertEquals(productGroupProduct1, productGroupProduct2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ProductGroupProduct> productGroupProducts1,
		List<ProductGroupProduct> productGroupProducts2) {

		Assert.assertEquals(
			productGroupProducts1.size(), productGroupProducts2.size());

		for (ProductGroupProduct productGroupProduct1 : productGroupProducts1) {
			boolean contains = false;

			for (ProductGroupProduct productGroupProduct2 :
					productGroupProducts2) {

				if (equals(productGroupProduct1, productGroupProduct2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				productGroupProducts2 + " does not contain " +
					productGroupProduct1,
				contains);
		}
	}

	protected void assertValid(ProductGroupProduct productGroupProduct)
		throws Exception {

		boolean valid = true;

		if (productGroupProduct.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"productExternalReferenceCode",
					additionalAssertFieldName)) {

				if (productGroupProduct.getProductExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"productGroupExternalReferenceCode",
					additionalAssertFieldName)) {

				if (productGroupProduct.
						getProductGroupExternalReferenceCode() == null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("productGroupId", additionalAssertFieldName)) {
				if (productGroupProduct.getProductGroupId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (productGroupProduct.getProductId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productName", additionalAssertFieldName)) {
				if (productGroupProduct.getProductName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("sku", additionalAssertFieldName)) {
				if (productGroupProduct.getSku() == null) {
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

	protected void assertValid(Page<ProductGroupProduct> page) {
		boolean valid = false;

		java.util.Collection<ProductGroupProduct> productGroupProducts =
			page.getItems();

		int size = productGroupProducts.size();

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
						ProductGroupProduct.class)) {

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
		ProductGroupProduct productGroupProduct1,
		ProductGroupProduct productGroupProduct2) {

		if (productGroupProduct1 == productGroupProduct2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productGroupProduct1.getId(),
						productGroupProduct2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"productExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productGroupProduct1.getProductExternalReferenceCode(),
						productGroupProduct2.
							getProductExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"productGroupExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productGroupProduct1.
							getProductGroupExternalReferenceCode(),
						productGroupProduct2.
							getProductGroupExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productGroupId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productGroupProduct1.getProductGroupId(),
						productGroupProduct2.getProductGroupId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productGroupProduct1.getProductId(),
						productGroupProduct2.getProductId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productName", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productGroupProduct1.getProductName(),
						productGroupProduct2.getProductName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("sku", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productGroupProduct1.getSku(),
						productGroupProduct2.getSku())) {

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

		if (!(_productGroupProductResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_productGroupProductResource;

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
		ProductGroupProduct productGroupProduct) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("productExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					productGroupProduct.getProductExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("productGroupExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					productGroupProduct.
						getProductGroupExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("productGroupId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("productId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("productName")) {
			sb.append("'");
			sb.append(String.valueOf(productGroupProduct.getProductName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("sku")) {
			sb.append("'");
			sb.append(String.valueOf(productGroupProduct.getSku()));
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

	protected ProductGroupProduct randomProductGroupProduct() throws Exception {
		return new ProductGroupProduct() {
			{
				id = RandomTestUtil.randomLong();
				productExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				productGroupExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				productGroupId = RandomTestUtil.randomLong();
				productId = RandomTestUtil.randomLong();
				productName = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				sku = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected ProductGroupProduct randomIrrelevantProductGroupProduct()
		throws Exception {

		ProductGroupProduct randomIrrelevantProductGroupProduct =
			randomProductGroupProduct();

		return randomIrrelevantProductGroupProduct;
	}

	protected ProductGroupProduct randomPatchProductGroupProduct()
		throws Exception {

		return randomProductGroupProduct();
	}

	protected ProductGroupProductResource productGroupProductResource;
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
		LogFactoryUtil.getLog(BaseProductGroupProductResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		ProductGroupProductResource _productGroupProductResource;

}