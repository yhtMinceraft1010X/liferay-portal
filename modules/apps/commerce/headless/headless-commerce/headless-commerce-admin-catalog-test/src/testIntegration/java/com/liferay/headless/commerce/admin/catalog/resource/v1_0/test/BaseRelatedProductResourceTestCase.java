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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.RelatedProduct;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.RelatedProductResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.RelatedProductSerDes;
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
public abstract class BaseRelatedProductResourceTestCase {

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

		_relatedProductResource.setContextCompany(testCompany);

		RelatedProductResource.Builder builder =
			RelatedProductResource.builder();

		relatedProductResource = builder.authentication(
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

		RelatedProduct relatedProduct1 = randomRelatedProduct();

		String json = objectMapper.writeValueAsString(relatedProduct1);

		RelatedProduct relatedProduct2 = RelatedProductSerDes.toDTO(json);

		Assert.assertTrue(equals(relatedProduct1, relatedProduct2));
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

		RelatedProduct relatedProduct = randomRelatedProduct();

		String json1 = objectMapper.writeValueAsString(relatedProduct);
		String json2 = RelatedProductSerDes.toJSON(relatedProduct);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		RelatedProduct relatedProduct = randomRelatedProduct();

		relatedProduct.setProductExternalReferenceCode(regex);
		relatedProduct.setType(regex);

		String json = RelatedProductSerDes.toJSON(relatedProduct);

		Assert.assertFalse(json.contains(regex));

		relatedProduct = RelatedProductSerDes.toDTO(json);

		Assert.assertEquals(
			regex, relatedProduct.getProductExternalReferenceCode());
		Assert.assertEquals(regex, relatedProduct.getType());
	}

	@Test
	public void testGetProductByExternalReferenceCodeRelatedProductsPage()
		throws Exception {

		String externalReferenceCode =
			testGetProductByExternalReferenceCodeRelatedProductsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetProductByExternalReferenceCodeRelatedProductsPage_getIrrelevantExternalReferenceCode();

		Page<RelatedProduct> page =
			relatedProductResource.
				getProductByExternalReferenceCodeRelatedProductsPage(
					externalReferenceCode, RandomTestUtil.randomString(),
					Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			RelatedProduct irrelevantRelatedProduct =
				testGetProductByExternalReferenceCodeRelatedProductsPage_addRelatedProduct(
					irrelevantExternalReferenceCode,
					randomIrrelevantRelatedProduct());

			page =
				relatedProductResource.
					getProductByExternalReferenceCodeRelatedProductsPage(
						irrelevantExternalReferenceCode, null,
						Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantRelatedProduct),
				(List<RelatedProduct>)page.getItems());
			assertValid(page);
		}

		RelatedProduct relatedProduct1 =
			testGetProductByExternalReferenceCodeRelatedProductsPage_addRelatedProduct(
				externalReferenceCode, randomRelatedProduct());

		RelatedProduct relatedProduct2 =
			testGetProductByExternalReferenceCodeRelatedProductsPage_addRelatedProduct(
				externalReferenceCode, randomRelatedProduct());

		page =
			relatedProductResource.
				getProductByExternalReferenceCodeRelatedProductsPage(
					externalReferenceCode, null, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(relatedProduct1, relatedProduct2),
			(List<RelatedProduct>)page.getItems());
		assertValid(page);

		relatedProductResource.deleteRelatedProduct(relatedProduct1.getId());

		relatedProductResource.deleteRelatedProduct(relatedProduct2.getId());
	}

	@Test
	public void testGetProductByExternalReferenceCodeRelatedProductsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetProductByExternalReferenceCodeRelatedProductsPage_getExternalReferenceCode();

		RelatedProduct relatedProduct1 =
			testGetProductByExternalReferenceCodeRelatedProductsPage_addRelatedProduct(
				externalReferenceCode, randomRelatedProduct());

		RelatedProduct relatedProduct2 =
			testGetProductByExternalReferenceCodeRelatedProductsPage_addRelatedProduct(
				externalReferenceCode, randomRelatedProduct());

		RelatedProduct relatedProduct3 =
			testGetProductByExternalReferenceCodeRelatedProductsPage_addRelatedProduct(
				externalReferenceCode, randomRelatedProduct());

		Page<RelatedProduct> page1 =
			relatedProductResource.
				getProductByExternalReferenceCodeRelatedProductsPage(
					externalReferenceCode, null, Pagination.of(1, 2));

		List<RelatedProduct> relatedProducts1 =
			(List<RelatedProduct>)page1.getItems();

		Assert.assertEquals(
			relatedProducts1.toString(), 2, relatedProducts1.size());

		Page<RelatedProduct> page2 =
			relatedProductResource.
				getProductByExternalReferenceCodeRelatedProductsPage(
					externalReferenceCode, null, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<RelatedProduct> relatedProducts2 =
			(List<RelatedProduct>)page2.getItems();

		Assert.assertEquals(
			relatedProducts2.toString(), 1, relatedProducts2.size());

		Page<RelatedProduct> page3 =
			relatedProductResource.
				getProductByExternalReferenceCodeRelatedProductsPage(
					externalReferenceCode, null, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(relatedProduct1, relatedProduct2, relatedProduct3),
			(List<RelatedProduct>)page3.getItems());
	}

	protected RelatedProduct
			testGetProductByExternalReferenceCodeRelatedProductsPage_addRelatedProduct(
				String externalReferenceCode, RelatedProduct relatedProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetProductByExternalReferenceCodeRelatedProductsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetProductByExternalReferenceCodeRelatedProductsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostProductByExternalReferenceCodeRelatedProduct()
		throws Exception {

		RelatedProduct randomRelatedProduct = randomRelatedProduct();

		RelatedProduct postRelatedProduct =
			testPostProductByExternalReferenceCodeRelatedProduct_addRelatedProduct(
				randomRelatedProduct);

		assertEquals(randomRelatedProduct, postRelatedProduct);
		assertValid(postRelatedProduct);
	}

	protected RelatedProduct
			testPostProductByExternalReferenceCodeRelatedProduct_addRelatedProduct(
				RelatedProduct relatedProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetProductIdRelatedProductsPage() throws Exception {
		Long id = testGetProductIdRelatedProductsPage_getId();
		Long irrelevantId =
			testGetProductIdRelatedProductsPage_getIrrelevantId();

		Page<RelatedProduct> page =
			relatedProductResource.getProductIdRelatedProductsPage(
				id, RandomTestUtil.randomString(), Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			RelatedProduct irrelevantRelatedProduct =
				testGetProductIdRelatedProductsPage_addRelatedProduct(
					irrelevantId, randomIrrelevantRelatedProduct());

			page = relatedProductResource.getProductIdRelatedProductsPage(
				irrelevantId, null, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantRelatedProduct),
				(List<RelatedProduct>)page.getItems());
			assertValid(page);
		}

		RelatedProduct relatedProduct1 =
			testGetProductIdRelatedProductsPage_addRelatedProduct(
				id, randomRelatedProduct());

		RelatedProduct relatedProduct2 =
			testGetProductIdRelatedProductsPage_addRelatedProduct(
				id, randomRelatedProduct());

		page = relatedProductResource.getProductIdRelatedProductsPage(
			id, null, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(relatedProduct1, relatedProduct2),
			(List<RelatedProduct>)page.getItems());
		assertValid(page);

		relatedProductResource.deleteRelatedProduct(relatedProduct1.getId());

		relatedProductResource.deleteRelatedProduct(relatedProduct2.getId());
	}

	@Test
	public void testGetProductIdRelatedProductsPageWithPagination()
		throws Exception {

		Long id = testGetProductIdRelatedProductsPage_getId();

		RelatedProduct relatedProduct1 =
			testGetProductIdRelatedProductsPage_addRelatedProduct(
				id, randomRelatedProduct());

		RelatedProduct relatedProduct2 =
			testGetProductIdRelatedProductsPage_addRelatedProduct(
				id, randomRelatedProduct());

		RelatedProduct relatedProduct3 =
			testGetProductIdRelatedProductsPage_addRelatedProduct(
				id, randomRelatedProduct());

		Page<RelatedProduct> page1 =
			relatedProductResource.getProductIdRelatedProductsPage(
				id, null, Pagination.of(1, 2));

		List<RelatedProduct> relatedProducts1 =
			(List<RelatedProduct>)page1.getItems();

		Assert.assertEquals(
			relatedProducts1.toString(), 2, relatedProducts1.size());

		Page<RelatedProduct> page2 =
			relatedProductResource.getProductIdRelatedProductsPage(
				id, null, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<RelatedProduct> relatedProducts2 =
			(List<RelatedProduct>)page2.getItems();

		Assert.assertEquals(
			relatedProducts2.toString(), 1, relatedProducts2.size());

		Page<RelatedProduct> page3 =
			relatedProductResource.getProductIdRelatedProductsPage(
				id, null, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(relatedProduct1, relatedProduct2, relatedProduct3),
			(List<RelatedProduct>)page3.getItems());
	}

	protected RelatedProduct
			testGetProductIdRelatedProductsPage_addRelatedProduct(
				Long id, RelatedProduct relatedProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductIdRelatedProductsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductIdRelatedProductsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostProductIdRelatedProduct() throws Exception {
		RelatedProduct randomRelatedProduct = randomRelatedProduct();

		RelatedProduct postRelatedProduct =
			testPostProductIdRelatedProduct_addRelatedProduct(
				randomRelatedProduct);

		assertEquals(randomRelatedProduct, postRelatedProduct);
		assertValid(postRelatedProduct);
	}

	protected RelatedProduct testPostProductIdRelatedProduct_addRelatedProduct(
			RelatedProduct relatedProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteRelatedProduct() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		RelatedProduct relatedProduct =
			testDeleteRelatedProduct_addRelatedProduct();

		assertHttpResponseStatusCode(
			204,
			relatedProductResource.deleteRelatedProductHttpResponse(
				relatedProduct.getId()));

		assertHttpResponseStatusCode(
			404,
			relatedProductResource.getRelatedProductHttpResponse(
				relatedProduct.getId()));

		assertHttpResponseStatusCode(
			404,
			relatedProductResource.getRelatedProductHttpResponse(
				relatedProduct.getId()));
	}

	protected RelatedProduct testDeleteRelatedProduct_addRelatedProduct()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteRelatedProduct() throws Exception {
		RelatedProduct relatedProduct =
			testGraphQLDeleteRelatedProduct_addRelatedProduct();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteRelatedProduct",
						new HashMap<String, Object>() {
							{
								put("id", relatedProduct.getId());
							}
						})),
				"JSONObject/data", "Object/deleteRelatedProduct"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"relatedProduct",
					new HashMap<String, Object>() {
						{
							put("id", relatedProduct.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected RelatedProduct testGraphQLDeleteRelatedProduct_addRelatedProduct()
		throws Exception {

		return testGraphQLRelatedProduct_addRelatedProduct();
	}

	@Test
	public void testGetRelatedProduct() throws Exception {
		RelatedProduct postRelatedProduct =
			testGetRelatedProduct_addRelatedProduct();

		RelatedProduct getRelatedProduct =
			relatedProductResource.getRelatedProduct(
				postRelatedProduct.getId());

		assertEquals(postRelatedProduct, getRelatedProduct);
		assertValid(getRelatedProduct);
	}

	protected RelatedProduct testGetRelatedProduct_addRelatedProduct()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetRelatedProduct() throws Exception {
		RelatedProduct relatedProduct =
			testGraphQLGetRelatedProduct_addRelatedProduct();

		Assert.assertTrue(
			equals(
				relatedProduct,
				RelatedProductSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"relatedProduct",
								new HashMap<String, Object>() {
									{
										put("id", relatedProduct.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/relatedProduct"))));
	}

	@Test
	public void testGraphQLGetRelatedProductNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"relatedProduct",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected RelatedProduct testGraphQLGetRelatedProduct_addRelatedProduct()
		throws Exception {

		return testGraphQLRelatedProduct_addRelatedProduct();
	}

	protected RelatedProduct testGraphQLRelatedProduct_addRelatedProduct()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		RelatedProduct relatedProduct, List<RelatedProduct> relatedProducts) {

		boolean contains = false;

		for (RelatedProduct item : relatedProducts) {
			if (equals(relatedProduct, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			relatedProducts + " does not contain " + relatedProduct, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		RelatedProduct relatedProduct1, RelatedProduct relatedProduct2) {

		Assert.assertTrue(
			relatedProduct1 + " does not equal " + relatedProduct2,
			equals(relatedProduct1, relatedProduct2));
	}

	protected void assertEquals(
		List<RelatedProduct> relatedProducts1,
		List<RelatedProduct> relatedProducts2) {

		Assert.assertEquals(relatedProducts1.size(), relatedProducts2.size());

		for (int i = 0; i < relatedProducts1.size(); i++) {
			RelatedProduct relatedProduct1 = relatedProducts1.get(i);
			RelatedProduct relatedProduct2 = relatedProducts2.get(i);

			assertEquals(relatedProduct1, relatedProduct2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<RelatedProduct> relatedProducts1,
		List<RelatedProduct> relatedProducts2) {

		Assert.assertEquals(relatedProducts1.size(), relatedProducts2.size());

		for (RelatedProduct relatedProduct1 : relatedProducts1) {
			boolean contains = false;

			for (RelatedProduct relatedProduct2 : relatedProducts2) {
				if (equals(relatedProduct1, relatedProduct2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				relatedProducts2 + " does not contain " + relatedProduct1,
				contains);
		}
	}

	protected void assertValid(RelatedProduct relatedProduct) throws Exception {
		boolean valid = true;

		if (relatedProduct.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (relatedProduct.getPriority() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"productExternalReferenceCode",
					additionalAssertFieldName)) {

				if (relatedProduct.getProductExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (relatedProduct.getProductId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (relatedProduct.getType() == null) {
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

	protected void assertValid(Page<RelatedProduct> page) {
		boolean valid = false;

		java.util.Collection<RelatedProduct> relatedProducts = page.getItems();

		int size = relatedProducts.size();

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
						RelatedProduct.class)) {

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
		RelatedProduct relatedProduct1, RelatedProduct relatedProduct2) {

		if (relatedProduct1 == relatedProduct2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						relatedProduct1.getId(), relatedProduct2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						relatedProduct1.getPriority(),
						relatedProduct2.getPriority())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"productExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						relatedProduct1.getProductExternalReferenceCode(),
						relatedProduct2.getProductExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						relatedProduct1.getProductId(),
						relatedProduct2.getProductId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						relatedProduct1.getType(), relatedProduct2.getType())) {

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

		if (!(_relatedProductResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_relatedProductResource;

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
		RelatedProduct relatedProduct) {

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

		if (entityFieldName.equals("priority")) {
			sb.append(String.valueOf(relatedProduct.getPriority()));

			return sb.toString();
		}

		if (entityFieldName.equals("productExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					relatedProduct.getProductExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("productId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("type")) {
			sb.append("'");
			sb.append(String.valueOf(relatedProduct.getType()));
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

	protected RelatedProduct randomRelatedProduct() throws Exception {
		return new RelatedProduct() {
			{
				id = RandomTestUtil.randomLong();
				priority = RandomTestUtil.randomDouble();
				productExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				productId = RandomTestUtil.randomLong();
				type = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected RelatedProduct randomIrrelevantRelatedProduct() throws Exception {
		RelatedProduct randomIrrelevantRelatedProduct = randomRelatedProduct();

		return randomIrrelevantRelatedProduct;
	}

	protected RelatedProduct randomPatchRelatedProduct() throws Exception {
		return randomRelatedProduct();
	}

	protected RelatedProductResource relatedProductResource;
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
		LogFactoryUtil.getLog(BaseRelatedProductResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		RelatedProductResource _relatedProductResource;

}