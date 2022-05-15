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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.Sku;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.SkuResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.SkuSerDes;
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
public abstract class BaseSkuResourceTestCase {

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

		_skuResource.setContextCompany(testCompany);

		SkuResource.Builder builder = SkuResource.builder();

		skuResource = builder.authentication(
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

		Sku sku1 = randomSku();

		String json = objectMapper.writeValueAsString(sku1);

		Sku sku2 = SkuSerDes.toDTO(json);

		Assert.assertTrue(equals(sku1, sku2));
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

		Sku sku = randomSku();

		String json1 = objectMapper.writeValueAsString(sku);
		String json2 = SkuSerDes.toJSON(sku);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Sku sku = randomSku();

		sku.setExternalReferenceCode(regex);
		sku.setGtin(regex);
		sku.setManufacturerPartNumber(regex);
		sku.setReplacementSkuExternalReferenceCode(regex);
		sku.setSku(regex);
		sku.setUnspsc(regex);

		String json = SkuSerDes.toJSON(sku);

		Assert.assertFalse(json.contains(regex));

		sku = SkuSerDes.toDTO(json);

		Assert.assertEquals(regex, sku.getExternalReferenceCode());
		Assert.assertEquals(regex, sku.getGtin());
		Assert.assertEquals(regex, sku.getManufacturerPartNumber());
		Assert.assertEquals(
			regex, sku.getReplacementSkuExternalReferenceCode());
		Assert.assertEquals(regex, sku.getSku());
		Assert.assertEquals(regex, sku.getUnspsc());
	}

	@Test
	public void testGetProductByExternalReferenceCodeSkusPage()
		throws Exception {

		String externalReferenceCode =
			testGetProductByExternalReferenceCodeSkusPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetProductByExternalReferenceCodeSkusPage_getIrrelevantExternalReferenceCode();

		Page<Sku> page = skuResource.getProductByExternalReferenceCodeSkusPage(
			externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			Sku irrelevantSku =
				testGetProductByExternalReferenceCodeSkusPage_addSku(
					irrelevantExternalReferenceCode, randomIrrelevantSku());

			page = skuResource.getProductByExternalReferenceCodeSkusPage(
				irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantSku), (List<Sku>)page.getItems());
			assertValid(page);
		}

		Sku sku1 = testGetProductByExternalReferenceCodeSkusPage_addSku(
			externalReferenceCode, randomSku());

		Sku sku2 = testGetProductByExternalReferenceCodeSkusPage_addSku(
			externalReferenceCode, randomSku());

		page = skuResource.getProductByExternalReferenceCodeSkusPage(
			externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(sku1, sku2), (List<Sku>)page.getItems());
		assertValid(page);

		skuResource.deleteSku(sku1.getId());

		skuResource.deleteSku(sku2.getId());
	}

	@Test
	public void testGetProductByExternalReferenceCodeSkusPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetProductByExternalReferenceCodeSkusPage_getExternalReferenceCode();

		Sku sku1 = testGetProductByExternalReferenceCodeSkusPage_addSku(
			externalReferenceCode, randomSku());

		Sku sku2 = testGetProductByExternalReferenceCodeSkusPage_addSku(
			externalReferenceCode, randomSku());

		Sku sku3 = testGetProductByExternalReferenceCodeSkusPage_addSku(
			externalReferenceCode, randomSku());

		Page<Sku> page1 = skuResource.getProductByExternalReferenceCodeSkusPage(
			externalReferenceCode, Pagination.of(1, 2));

		List<Sku> skus1 = (List<Sku>)page1.getItems();

		Assert.assertEquals(skus1.toString(), 2, skus1.size());

		Page<Sku> page2 = skuResource.getProductByExternalReferenceCodeSkusPage(
			externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<Sku> skus2 = (List<Sku>)page2.getItems();

		Assert.assertEquals(skus2.toString(), 1, skus2.size());

		Page<Sku> page3 = skuResource.getProductByExternalReferenceCodeSkusPage(
			externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(sku1, sku2, sku3), (List<Sku>)page3.getItems());
	}

	protected Sku testGetProductByExternalReferenceCodeSkusPage_addSku(
			String externalReferenceCode, Sku sku)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetProductByExternalReferenceCodeSkusPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetProductByExternalReferenceCodeSkusPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostProductByExternalReferenceCodeSku() throws Exception {
		Sku randomSku = randomSku();

		Sku postSku = testPostProductByExternalReferenceCodeSku_addSku(
			randomSku);

		assertEquals(randomSku, postSku);
		assertValid(postSku);
	}

	protected Sku testPostProductByExternalReferenceCodeSku_addSku(Sku sku)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetProductIdSkusPage() throws Exception {
		Long id = testGetProductIdSkusPage_getId();
		Long irrelevantId = testGetProductIdSkusPage_getIrrelevantId();

		Page<Sku> page = skuResource.getProductIdSkusPage(
			id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			Sku irrelevantSku = testGetProductIdSkusPage_addSku(
				irrelevantId, randomIrrelevantSku());

			page = skuResource.getProductIdSkusPage(
				irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantSku), (List<Sku>)page.getItems());
			assertValid(page);
		}

		Sku sku1 = testGetProductIdSkusPage_addSku(id, randomSku());

		Sku sku2 = testGetProductIdSkusPage_addSku(id, randomSku());

		page = skuResource.getProductIdSkusPage(id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(sku1, sku2), (List<Sku>)page.getItems());
		assertValid(page);

		skuResource.deleteSku(sku1.getId());

		skuResource.deleteSku(sku2.getId());
	}

	@Test
	public void testGetProductIdSkusPageWithPagination() throws Exception {
		Long id = testGetProductIdSkusPage_getId();

		Sku sku1 = testGetProductIdSkusPage_addSku(id, randomSku());

		Sku sku2 = testGetProductIdSkusPage_addSku(id, randomSku());

		Sku sku3 = testGetProductIdSkusPage_addSku(id, randomSku());

		Page<Sku> page1 = skuResource.getProductIdSkusPage(
			id, Pagination.of(1, 2));

		List<Sku> skus1 = (List<Sku>)page1.getItems();

		Assert.assertEquals(skus1.toString(), 2, skus1.size());

		Page<Sku> page2 = skuResource.getProductIdSkusPage(
			id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<Sku> skus2 = (List<Sku>)page2.getItems();

		Assert.assertEquals(skus2.toString(), 1, skus2.size());

		Page<Sku> page3 = skuResource.getProductIdSkusPage(
			id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(sku1, sku2, sku3), (List<Sku>)page3.getItems());
	}

	protected Sku testGetProductIdSkusPage_addSku(Long id, Sku sku)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductIdSkusPage_getId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductIdSkusPage_getIrrelevantId() throws Exception {
		return null;
	}

	@Test
	public void testPostProductIdSku() throws Exception {
		Sku randomSku = randomSku();

		Sku postSku = testPostProductIdSku_addSku(randomSku);

		assertEquals(randomSku, postSku);
		assertValid(postSku);
	}

	protected Sku testPostProductIdSku_addSku(Sku sku) throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetSkusPage() throws Exception {
		Page<Sku> page = skuResource.getSkusPage(
			null, null, Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		Sku sku1 = testGetSkusPage_addSku(randomSku());

		Sku sku2 = testGetSkusPage_addSku(randomSku());

		page = skuResource.getSkusPage(null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(sku1, (List<Sku>)page.getItems());
		assertContains(sku2, (List<Sku>)page.getItems());
		assertValid(page);

		skuResource.deleteSku(sku1.getId());

		skuResource.deleteSku(sku2.getId());
	}

	@Test
	public void testGetSkusPageWithFilterDateTimeEquals() throws Exception {
		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Sku sku1 = randomSku();

		sku1 = testGetSkusPage_addSku(sku1);

		for (EntityField entityField : entityFields) {
			Page<Sku> page = skuResource.getSkusPage(
				null, getFilterString(entityField, "between", sku1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(sku1), (List<Sku>)page.getItems());
		}
	}

	@Test
	public void testGetSkusPageWithFilterDoubleEquals() throws Exception {
		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Sku sku1 = testGetSkusPage_addSku(randomSku());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Sku sku2 = testGetSkusPage_addSku(randomSku());

		for (EntityField entityField : entityFields) {
			Page<Sku> page = skuResource.getSkusPage(
				null, getFilterString(entityField, "eq", sku1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(sku1), (List<Sku>)page.getItems());
		}
	}

	@Test
	public void testGetSkusPageWithFilterStringEquals() throws Exception {
		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Sku sku1 = testGetSkusPage_addSku(randomSku());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Sku sku2 = testGetSkusPage_addSku(randomSku());

		for (EntityField entityField : entityFields) {
			Page<Sku> page = skuResource.getSkusPage(
				null, getFilterString(entityField, "eq", sku1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(sku1), (List<Sku>)page.getItems());
		}
	}

	@Test
	public void testGetSkusPageWithPagination() throws Exception {
		Page<Sku> totalPage = skuResource.getSkusPage(null, null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		Sku sku1 = testGetSkusPage_addSku(randomSku());

		Sku sku2 = testGetSkusPage_addSku(randomSku());

		Sku sku3 = testGetSkusPage_addSku(randomSku());

		Page<Sku> page1 = skuResource.getSkusPage(
			null, null, Pagination.of(1, totalCount + 2), null);

		List<Sku> skus1 = (List<Sku>)page1.getItems();

		Assert.assertEquals(skus1.toString(), totalCount + 2, skus1.size());

		Page<Sku> page2 = skuResource.getSkusPage(
			null, null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<Sku> skus2 = (List<Sku>)page2.getItems();

		Assert.assertEquals(skus2.toString(), 1, skus2.size());

		Page<Sku> page3 = skuResource.getSkusPage(
			null, null, Pagination.of(1, totalCount + 3), null);

		assertContains(sku1, (List<Sku>)page3.getItems());
		assertContains(sku2, (List<Sku>)page3.getItems());
		assertContains(sku3, (List<Sku>)page3.getItems());
	}

	@Test
	public void testGetSkusPageWithSortDateTime() throws Exception {
		testGetSkusPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, sku1, sku2) -> {
				BeanTestUtil.setProperty(
					sku1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetSkusPageWithSortDouble() throws Exception {
		testGetSkusPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, sku1, sku2) -> {
				BeanTestUtil.setProperty(sku1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(sku2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetSkusPageWithSortInteger() throws Exception {
		testGetSkusPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, sku1, sku2) -> {
				BeanTestUtil.setProperty(sku1, entityField.getName(), 0);
				BeanTestUtil.setProperty(sku2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetSkusPageWithSortString() throws Exception {
		testGetSkusPageWithSort(
			EntityField.Type.STRING,
			(entityField, sku1, sku2) -> {
				Class<?> clazz = sku1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						sku1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						sku2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						sku1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						sku2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						sku1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						sku2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetSkusPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Sku, Sku, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Sku sku1 = randomSku();
		Sku sku2 = randomSku();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, sku1, sku2);
		}

		sku1 = testGetSkusPage_addSku(sku1);

		sku2 = testGetSkusPage_addSku(sku2);

		for (EntityField entityField : entityFields) {
			Page<Sku> ascPage = skuResource.getSkusPage(
				null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(sku1, sku2), (List<Sku>)ascPage.getItems());

			Page<Sku> descPage = skuResource.getSkusPage(
				null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(sku2, sku1), (List<Sku>)descPage.getItems());
		}
	}

	protected Sku testGetSkusPage_addSku(Sku sku) throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSkusPage() throws Exception {
		GraphQLField graphQLField = new GraphQLField(
			"skus",
			new HashMap<String, Object>() {
				{
					put("page", 1);
					put("pageSize", 10);
				}
			},
			new GraphQLField("items", getGraphQLFields()),
			new GraphQLField("page"), new GraphQLField("totalCount"));

		JSONObject skusJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/skus");

		long totalCount = skusJSONObject.getLong("totalCount");

		Sku sku1 = testGraphQLGetSkusPage_addSku();
		Sku sku2 = testGraphQLGetSkusPage_addSku();

		skusJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/skus");

		Assert.assertEquals(
			totalCount + 2, skusJSONObject.getLong("totalCount"));

		assertContains(
			sku1,
			Arrays.asList(SkuSerDes.toDTOs(skusJSONObject.getString("items"))));
		assertContains(
			sku2,
			Arrays.asList(SkuSerDes.toDTOs(skusJSONObject.getString("items"))));
	}

	protected Sku testGraphQLGetSkusPage_addSku() throws Exception {
		return testGraphQLSku_addSku();
	}

	@Test
	public void testDeleteSkuByExternalReferenceCode() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		Sku sku = testDeleteSkuByExternalReferenceCode_addSku();

		assertHttpResponseStatusCode(
			204,
			skuResource.deleteSkuByExternalReferenceCodeHttpResponse(
				sku.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			skuResource.getSkuByExternalReferenceCodeHttpResponse(
				sku.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			skuResource.getSkuByExternalReferenceCodeHttpResponse(
				sku.getExternalReferenceCode()));
	}

	protected Sku testDeleteSkuByExternalReferenceCode_addSku()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetSkuByExternalReferenceCode() throws Exception {
		Sku postSku = testGetSkuByExternalReferenceCode_addSku();

		Sku getSku = skuResource.getSkuByExternalReferenceCode(
			postSku.getExternalReferenceCode());

		assertEquals(postSku, getSku);
		assertValid(getSku);
	}

	protected Sku testGetSkuByExternalReferenceCode_addSku() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSkuByExternalReferenceCode() throws Exception {
		Sku sku = testGraphQLGetSkuByExternalReferenceCode_addSku();

		Assert.assertTrue(
			equals(
				sku,
				SkuSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"skuByExternalReferenceCode",
								new HashMap<String, Object>() {
									{
										put(
											"externalReferenceCode",
											"\"" +
												sku.getExternalReferenceCode() +
													"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/skuByExternalReferenceCode"))));
	}

	@Test
	public void testGraphQLGetSkuByExternalReferenceCodeNotFound()
		throws Exception {

		String irrelevantExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"skuByExternalReferenceCode",
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

	protected Sku testGraphQLGetSkuByExternalReferenceCode_addSku()
		throws Exception {

		return testGraphQLSku_addSku();
	}

	@Test
	public void testPatchSkuByExternalReferenceCode() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testDeleteSku() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		Sku sku = testDeleteSku_addSku();

		assertHttpResponseStatusCode(
			204, skuResource.deleteSkuHttpResponse(sku.getId()));

		assertHttpResponseStatusCode(
			404, skuResource.getSkuHttpResponse(sku.getId()));

		assertHttpResponseStatusCode(
			404, skuResource.getSkuHttpResponse(sku.getId()));
	}

	protected Sku testDeleteSku_addSku() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteSku() throws Exception {
		Sku sku = testGraphQLDeleteSku_addSku();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteSku",
						new HashMap<String, Object>() {
							{
								put("id", sku.getId());
							}
						})),
				"JSONObject/data", "Object/deleteSku"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"sku",
					new HashMap<String, Object>() {
						{
							put("id", sku.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected Sku testGraphQLDeleteSku_addSku() throws Exception {
		return testGraphQLSku_addSku();
	}

	@Test
	public void testGetSku() throws Exception {
		Sku postSku = testGetSku_addSku();

		Sku getSku = skuResource.getSku(postSku.getId());

		assertEquals(postSku, getSku);
		assertValid(getSku);
	}

	protected Sku testGetSku_addSku() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSku() throws Exception {
		Sku sku = testGraphQLGetSku_addSku();

		Assert.assertTrue(
			equals(
				sku,
				SkuSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"sku",
								new HashMap<String, Object>() {
									{
										put("id", sku.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/sku"))));
	}

	@Test
	public void testGraphQLGetSkuNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"sku",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Sku testGraphQLGetSku_addSku() throws Exception {
		return testGraphQLSku_addSku();
	}

	@Test
	public void testPatchSku() throws Exception {
		Assert.assertTrue(false);
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected Sku testGraphQLSku_addSku() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(Sku sku, List<Sku> skus) {
		boolean contains = false;

		for (Sku item : skus) {
			if (equals(sku, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(skus + " does not contain " + sku, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Sku sku1, Sku sku2) {
		Assert.assertTrue(sku1 + " does not equal " + sku2, equals(sku1, sku2));
	}

	protected void assertEquals(List<Sku> skus1, List<Sku> skus2) {
		Assert.assertEquals(skus1.size(), skus2.size());

		for (int i = 0; i < skus1.size(); i++) {
			Sku sku1 = skus1.get(i);
			Sku sku2 = skus2.get(i);

			assertEquals(sku1, sku2);
		}
	}

	protected void assertEqualsIgnoringOrder(List<Sku> skus1, List<Sku> skus2) {
		Assert.assertEquals(skus1.size(), skus2.size());

		for (Sku sku1 : skus1) {
			boolean contains = false;

			for (Sku sku2 : skus2) {
				if (equals(sku1, sku2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(skus2 + " does not contain " + sku1, contains);
		}
	}

	protected void assertValid(Sku sku) throws Exception {
		boolean valid = true;

		if (sku.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("cost", additionalAssertFieldName)) {
				if (sku.getCost() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("depth", additionalAssertFieldName)) {
				if (sku.getDepth() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("discontinued", additionalAssertFieldName)) {
				if (sku.getDiscontinued() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("discontinuedDate", additionalAssertFieldName)) {
				if (sku.getDiscontinuedDate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("displayDate", additionalAssertFieldName)) {
				if (sku.getDisplayDate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("expirationDate", additionalAssertFieldName)) {
				if (sku.getExpirationDate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (sku.getExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("gtin", additionalAssertFieldName)) {
				if (sku.getGtin() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("height", additionalAssertFieldName)) {
				if (sku.getHeight() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("inventoryLevel", additionalAssertFieldName)) {
				if (sku.getInventoryLevel() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"manufacturerPartNumber", additionalAssertFieldName)) {

				if (sku.getManufacturerPartNumber() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("neverExpire", additionalAssertFieldName)) {
				if (sku.getNeverExpire() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("price", additionalAssertFieldName)) {
				if (sku.getPrice() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (sku.getProductId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productName", additionalAssertFieldName)) {
				if (sku.getProductName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("promoPrice", additionalAssertFieldName)) {
				if (sku.getPromoPrice() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("published", additionalAssertFieldName)) {
				if (sku.getPublished() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("purchasable", additionalAssertFieldName)) {
				if (sku.getPurchasable() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"replacementSkuExternalReferenceCode",
					additionalAssertFieldName)) {

				if (sku.getReplacementSkuExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("replacementSkuId", additionalAssertFieldName)) {
				if (sku.getReplacementSkuId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("sku", additionalAssertFieldName)) {
				if (sku.getSku() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("skuOptions", additionalAssertFieldName)) {
				if (sku.getSkuOptions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("unspsc", additionalAssertFieldName)) {
				if (sku.getUnspsc() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("weight", additionalAssertFieldName)) {
				if (sku.getWeight() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("width", additionalAssertFieldName)) {
				if (sku.getWidth() == null) {
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

	protected void assertValid(Page<Sku> page) {
		boolean valid = false;

		java.util.Collection<Sku> skus = page.getItems();

		int size = skus.size();

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
					com.liferay.headless.commerce.admin.catalog.dto.v1_0.Sku.
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

	protected boolean equals(Sku sku1, Sku sku2) {
		if (sku1 == sku2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("cost", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getCost(), sku2.getCost())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("depth", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getDepth(), sku2.getDepth())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("discontinued", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getDiscontinued(), sku2.getDiscontinued())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("discontinuedDate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getDiscontinuedDate(),
						sku2.getDiscontinuedDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("displayDate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getDisplayDate(), sku2.getDisplayDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("expirationDate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getExpirationDate(), sku2.getExpirationDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						sku1.getExternalReferenceCode(),
						sku2.getExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("gtin", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getGtin(), sku2.getGtin())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("height", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getHeight(), sku2.getHeight())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getId(), sku2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("inventoryLevel", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getInventoryLevel(), sku2.getInventoryLevel())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"manufacturerPartNumber", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						sku1.getManufacturerPartNumber(),
						sku2.getManufacturerPartNumber())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("neverExpire", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getNeverExpire(), sku2.getNeverExpire())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("price", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getPrice(), sku2.getPrice())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getProductId(), sku2.getProductId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productName", additionalAssertFieldName)) {
				if (!equals(
						(Map)sku1.getProductName(),
						(Map)sku2.getProductName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("promoPrice", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getPromoPrice(), sku2.getPromoPrice())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("published", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getPublished(), sku2.getPublished())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("purchasable", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getPurchasable(), sku2.getPurchasable())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"replacementSkuExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						sku1.getReplacementSkuExternalReferenceCode(),
						sku2.getReplacementSkuExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("replacementSkuId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getReplacementSkuId(),
						sku2.getReplacementSkuId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("sku", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getSku(), sku2.getSku())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("skuOptions", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getSkuOptions(), sku2.getSkuOptions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("unspsc", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getUnspsc(), sku2.getUnspsc())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("weight", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getWeight(), sku2.getWeight())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("width", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getWidth(), sku2.getWidth())) {
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

		if (!(_skuResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_skuResource;

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
		EntityField entityField, String operator, Sku sku) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("cost")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("depth")) {
			sb.append(String.valueOf(sku.getDepth()));

			return sb.toString();
		}

		if (entityFieldName.equals("discontinued")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("discontinuedDate")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(sku.getDiscontinuedDate(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(sku.getDiscontinuedDate(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(sku.getDiscontinuedDate()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("displayDate")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(sku.getDisplayDate(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(sku.getDisplayDate(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(sku.getDisplayDate()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("expirationDate")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(sku.getExpirationDate(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(sku.getExpirationDate(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(sku.getExpirationDate()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("externalReferenceCode")) {
			sb.append("'");
			sb.append(String.valueOf(sku.getExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("gtin")) {
			sb.append("'");
			sb.append(String.valueOf(sku.getGtin()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("height")) {
			sb.append(String.valueOf(sku.getHeight()));

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("inventoryLevel")) {
			sb.append(String.valueOf(sku.getInventoryLevel()));

			return sb.toString();
		}

		if (entityFieldName.equals("manufacturerPartNumber")) {
			sb.append("'");
			sb.append(String.valueOf(sku.getManufacturerPartNumber()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("neverExpire")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("price")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("productId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("productName")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("promoPrice")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("published")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("purchasable")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("replacementSkuExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(sku.getReplacementSkuExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("replacementSkuId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("sku")) {
			sb.append("'");
			sb.append(String.valueOf(sku.getSku()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("skuOptions")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("unspsc")) {
			sb.append("'");
			sb.append(String.valueOf(sku.getUnspsc()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("weight")) {
			sb.append(String.valueOf(sku.getWeight()));

			return sb.toString();
		}

		if (entityFieldName.equals("width")) {
			sb.append(String.valueOf(sku.getWidth()));

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

	protected Sku randomSku() throws Exception {
		return new Sku() {
			{
				depth = RandomTestUtil.randomDouble();
				discontinued = RandomTestUtil.randomBoolean();
				discontinuedDate = RandomTestUtil.nextDate();
				displayDate = RandomTestUtil.nextDate();
				expirationDate = RandomTestUtil.nextDate();
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				gtin = StringUtil.toLowerCase(RandomTestUtil.randomString());
				height = RandomTestUtil.randomDouble();
				id = RandomTestUtil.randomLong();
				inventoryLevel = RandomTestUtil.randomInt();
				manufacturerPartNumber = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				neverExpire = RandomTestUtil.randomBoolean();
				productId = RandomTestUtil.randomLong();
				published = RandomTestUtil.randomBoolean();
				purchasable = RandomTestUtil.randomBoolean();
				replacementSkuExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				replacementSkuId = RandomTestUtil.randomLong();
				sku = StringUtil.toLowerCase(RandomTestUtil.randomString());
				unspsc = StringUtil.toLowerCase(RandomTestUtil.randomString());
				weight = RandomTestUtil.randomDouble();
				width = RandomTestUtil.randomDouble();
			}
		};
	}

	protected Sku randomIrrelevantSku() throws Exception {
		Sku randomIrrelevantSku = randomSku();

		return randomIrrelevantSku;
	}

	protected Sku randomPatchSku() throws Exception {
		return randomSku();
	}

	protected SkuResource skuResource;
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
		LogFactoryUtil.getLog(BaseSkuResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private
		com.liferay.headless.commerce.admin.catalog.resource.v1_0.SkuResource
			_skuResource;

}