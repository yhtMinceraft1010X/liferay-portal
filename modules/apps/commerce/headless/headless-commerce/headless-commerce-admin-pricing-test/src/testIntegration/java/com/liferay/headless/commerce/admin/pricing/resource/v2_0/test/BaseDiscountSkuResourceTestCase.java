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

package com.liferay.headless.commerce.admin.pricing.resource.v2_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.pricing.client.dto.v2_0.DiscountSku;
import com.liferay.headless.commerce.admin.pricing.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.pricing.client.pagination.Page;
import com.liferay.headless.commerce.admin.pricing.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.pricing.client.resource.v2_0.DiscountSkuResource;
import com.liferay.headless.commerce.admin.pricing.client.serdes.v2_0.DiscountSkuSerDes;
import com.liferay.petra.function.UnsafeTriConsumer;
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
public abstract class BaseDiscountSkuResourceTestCase {

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

		_discountSkuResource.setContextCompany(testCompany);

		DiscountSkuResource.Builder builder = DiscountSkuResource.builder();

		discountSkuResource = builder.authentication(
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

		DiscountSku discountSku1 = randomDiscountSku();

		String json = objectMapper.writeValueAsString(discountSku1);

		DiscountSku discountSku2 = DiscountSkuSerDes.toDTO(json);

		Assert.assertTrue(equals(discountSku1, discountSku2));
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

		DiscountSku discountSku = randomDiscountSku();

		String json1 = objectMapper.writeValueAsString(discountSku);
		String json2 = DiscountSkuSerDes.toJSON(discountSku);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		DiscountSku discountSku = randomDiscountSku();

		discountSku.setDiscountExternalReferenceCode(regex);
		discountSku.setSkuExternalReferenceCode(regex);

		String json = DiscountSkuSerDes.toJSON(discountSku);

		Assert.assertFalse(json.contains(regex));

		discountSku = DiscountSkuSerDes.toDTO(json);

		Assert.assertEquals(
			regex, discountSku.getDiscountExternalReferenceCode());
		Assert.assertEquals(regex, discountSku.getSkuExternalReferenceCode());
	}

	@Test
	public void testDeleteDiscountSku() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLDeleteDiscountSku() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetDiscountByExternalReferenceCodeDiscountSkusPage()
		throws Exception {

		String externalReferenceCode =
			testGetDiscountByExternalReferenceCodeDiscountSkusPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetDiscountByExternalReferenceCodeDiscountSkusPage_getIrrelevantExternalReferenceCode();

		Page<DiscountSku> page =
			discountSkuResource.
				getDiscountByExternalReferenceCodeDiscountSkusPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			DiscountSku irrelevantDiscountSku =
				testGetDiscountByExternalReferenceCodeDiscountSkusPage_addDiscountSku(
					irrelevantExternalReferenceCode,
					randomIrrelevantDiscountSku());

			page =
				discountSkuResource.
					getDiscountByExternalReferenceCodeDiscountSkusPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantDiscountSku),
				(List<DiscountSku>)page.getItems());
			assertValid(page);
		}

		DiscountSku discountSku1 =
			testGetDiscountByExternalReferenceCodeDiscountSkusPage_addDiscountSku(
				externalReferenceCode, randomDiscountSku());

		DiscountSku discountSku2 =
			testGetDiscountByExternalReferenceCodeDiscountSkusPage_addDiscountSku(
				externalReferenceCode, randomDiscountSku());

		page =
			discountSkuResource.
				getDiscountByExternalReferenceCodeDiscountSkusPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(discountSku1, discountSku2),
			(List<DiscountSku>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetDiscountByExternalReferenceCodeDiscountSkusPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetDiscountByExternalReferenceCodeDiscountSkusPage_getExternalReferenceCode();

		DiscountSku discountSku1 =
			testGetDiscountByExternalReferenceCodeDiscountSkusPage_addDiscountSku(
				externalReferenceCode, randomDiscountSku());

		DiscountSku discountSku2 =
			testGetDiscountByExternalReferenceCodeDiscountSkusPage_addDiscountSku(
				externalReferenceCode, randomDiscountSku());

		DiscountSku discountSku3 =
			testGetDiscountByExternalReferenceCodeDiscountSkusPage_addDiscountSku(
				externalReferenceCode, randomDiscountSku());

		Page<DiscountSku> page1 =
			discountSkuResource.
				getDiscountByExternalReferenceCodeDiscountSkusPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<DiscountSku> discountSkus1 = (List<DiscountSku>)page1.getItems();

		Assert.assertEquals(discountSkus1.toString(), 2, discountSkus1.size());

		Page<DiscountSku> page2 =
			discountSkuResource.
				getDiscountByExternalReferenceCodeDiscountSkusPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<DiscountSku> discountSkus2 = (List<DiscountSku>)page2.getItems();

		Assert.assertEquals(discountSkus2.toString(), 1, discountSkus2.size());

		Page<DiscountSku> page3 =
			discountSkuResource.
				getDiscountByExternalReferenceCodeDiscountSkusPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(discountSku1, discountSku2, discountSku3),
			(List<DiscountSku>)page3.getItems());
	}

	protected DiscountSku
			testGetDiscountByExternalReferenceCodeDiscountSkusPage_addDiscountSku(
				String externalReferenceCode, DiscountSku discountSku)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetDiscountByExternalReferenceCodeDiscountSkusPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetDiscountByExternalReferenceCodeDiscountSkusPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostDiscountByExternalReferenceCodeDiscountSku()
		throws Exception {

		DiscountSku randomDiscountSku = randomDiscountSku();

		DiscountSku postDiscountSku =
			testPostDiscountByExternalReferenceCodeDiscountSku_addDiscountSku(
				randomDiscountSku);

		assertEquals(randomDiscountSku, postDiscountSku);
		assertValid(postDiscountSku);
	}

	protected DiscountSku
			testPostDiscountByExternalReferenceCodeDiscountSku_addDiscountSku(
				DiscountSku discountSku)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetDiscountIdDiscountSkusPage() throws Exception {
		Long id = testGetDiscountIdDiscountSkusPage_getId();
		Long irrelevantId = testGetDiscountIdDiscountSkusPage_getIrrelevantId();

		Page<DiscountSku> page =
			discountSkuResource.getDiscountIdDiscountSkusPage(
				id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			DiscountSku irrelevantDiscountSku =
				testGetDiscountIdDiscountSkusPage_addDiscountSku(
					irrelevantId, randomIrrelevantDiscountSku());

			page = discountSkuResource.getDiscountIdDiscountSkusPage(
				irrelevantId, null, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantDiscountSku),
				(List<DiscountSku>)page.getItems());
			assertValid(page);
		}

		DiscountSku discountSku1 =
			testGetDiscountIdDiscountSkusPage_addDiscountSku(
				id, randomDiscountSku());

		DiscountSku discountSku2 =
			testGetDiscountIdDiscountSkusPage_addDiscountSku(
				id, randomDiscountSku());

		page = discountSkuResource.getDiscountIdDiscountSkusPage(
			id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(discountSku1, discountSku2),
			(List<DiscountSku>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetDiscountIdDiscountSkusPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetDiscountIdDiscountSkusPage_getId();

		DiscountSku discountSku1 = randomDiscountSku();

		discountSku1 = testGetDiscountIdDiscountSkusPage_addDiscountSku(
			id, discountSku1);

		for (EntityField entityField : entityFields) {
			Page<DiscountSku> page =
				discountSkuResource.getDiscountIdDiscountSkusPage(
					id, null,
					getFilterString(entityField, "between", discountSku1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(discountSku1),
				(List<DiscountSku>)page.getItems());
		}
	}

	@Test
	public void testGetDiscountIdDiscountSkusPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetDiscountIdDiscountSkusPage_getId();

		DiscountSku discountSku1 =
			testGetDiscountIdDiscountSkusPage_addDiscountSku(
				id, randomDiscountSku());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		DiscountSku discountSku2 =
			testGetDiscountIdDiscountSkusPage_addDiscountSku(
				id, randomDiscountSku());

		for (EntityField entityField : entityFields) {
			Page<DiscountSku> page =
				discountSkuResource.getDiscountIdDiscountSkusPage(
					id, null, getFilterString(entityField, "eq", discountSku1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(discountSku1),
				(List<DiscountSku>)page.getItems());
		}
	}

	@Test
	public void testGetDiscountIdDiscountSkusPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetDiscountIdDiscountSkusPage_getId();

		DiscountSku discountSku1 =
			testGetDiscountIdDiscountSkusPage_addDiscountSku(
				id, randomDiscountSku());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		DiscountSku discountSku2 =
			testGetDiscountIdDiscountSkusPage_addDiscountSku(
				id, randomDiscountSku());

		for (EntityField entityField : entityFields) {
			Page<DiscountSku> page =
				discountSkuResource.getDiscountIdDiscountSkusPage(
					id, null, getFilterString(entityField, "eq", discountSku1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(discountSku1),
				(List<DiscountSku>)page.getItems());
		}
	}

	@Test
	public void testGetDiscountIdDiscountSkusPageWithPagination()
		throws Exception {

		Long id = testGetDiscountIdDiscountSkusPage_getId();

		DiscountSku discountSku1 =
			testGetDiscountIdDiscountSkusPage_addDiscountSku(
				id, randomDiscountSku());

		DiscountSku discountSku2 =
			testGetDiscountIdDiscountSkusPage_addDiscountSku(
				id, randomDiscountSku());

		DiscountSku discountSku3 =
			testGetDiscountIdDiscountSkusPage_addDiscountSku(
				id, randomDiscountSku());

		Page<DiscountSku> page1 =
			discountSkuResource.getDiscountIdDiscountSkusPage(
				id, null, null, Pagination.of(1, 2), null);

		List<DiscountSku> discountSkus1 = (List<DiscountSku>)page1.getItems();

		Assert.assertEquals(discountSkus1.toString(), 2, discountSkus1.size());

		Page<DiscountSku> page2 =
			discountSkuResource.getDiscountIdDiscountSkusPage(
				id, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<DiscountSku> discountSkus2 = (List<DiscountSku>)page2.getItems();

		Assert.assertEquals(discountSkus2.toString(), 1, discountSkus2.size());

		Page<DiscountSku> page3 =
			discountSkuResource.getDiscountIdDiscountSkusPage(
				id, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(discountSku1, discountSku2, discountSku3),
			(List<DiscountSku>)page3.getItems());
	}

	@Test
	public void testGetDiscountIdDiscountSkusPageWithSortDateTime()
		throws Exception {

		testGetDiscountIdDiscountSkusPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, discountSku1, discountSku2) -> {
				BeanTestUtil.setProperty(
					discountSku1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetDiscountIdDiscountSkusPageWithSortDouble()
		throws Exception {

		testGetDiscountIdDiscountSkusPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, discountSku1, discountSku2) -> {
				BeanTestUtil.setProperty(
					discountSku1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					discountSku2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetDiscountIdDiscountSkusPageWithSortInteger()
		throws Exception {

		testGetDiscountIdDiscountSkusPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, discountSku1, discountSku2) -> {
				BeanTestUtil.setProperty(
					discountSku1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					discountSku2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetDiscountIdDiscountSkusPageWithSortString()
		throws Exception {

		testGetDiscountIdDiscountSkusPageWithSort(
			EntityField.Type.STRING,
			(entityField, discountSku1, discountSku2) -> {
				Class<?> clazz = discountSku1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						discountSku1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						discountSku2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						discountSku1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						discountSku2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						discountSku1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						discountSku2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetDiscountIdDiscountSkusPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, DiscountSku, DiscountSku, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetDiscountIdDiscountSkusPage_getId();

		DiscountSku discountSku1 = randomDiscountSku();
		DiscountSku discountSku2 = randomDiscountSku();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, discountSku1, discountSku2);
		}

		discountSku1 = testGetDiscountIdDiscountSkusPage_addDiscountSku(
			id, discountSku1);

		discountSku2 = testGetDiscountIdDiscountSkusPage_addDiscountSku(
			id, discountSku2);

		for (EntityField entityField : entityFields) {
			Page<DiscountSku> ascPage =
				discountSkuResource.getDiscountIdDiscountSkusPage(
					id, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(discountSku1, discountSku2),
				(List<DiscountSku>)ascPage.getItems());

			Page<DiscountSku> descPage =
				discountSkuResource.getDiscountIdDiscountSkusPage(
					id, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(discountSku2, discountSku1),
				(List<DiscountSku>)descPage.getItems());
		}
	}

	protected DiscountSku testGetDiscountIdDiscountSkusPage_addDiscountSku(
			Long id, DiscountSku discountSku)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetDiscountIdDiscountSkusPage_getId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetDiscountIdDiscountSkusPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostDiscountIdDiscountSku() throws Exception {
		DiscountSku randomDiscountSku = randomDiscountSku();

		DiscountSku postDiscountSku =
			testPostDiscountIdDiscountSku_addDiscountSku(randomDiscountSku);

		assertEquals(randomDiscountSku, postDiscountSku);
		assertValid(postDiscountSku);
	}

	protected DiscountSku testPostDiscountIdDiscountSku_addDiscountSku(
			DiscountSku discountSku)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected void assertContains(
		DiscountSku discountSku, List<DiscountSku> discountSkus) {

		boolean contains = false;

		for (DiscountSku item : discountSkus) {
			if (equals(discountSku, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			discountSkus + " does not contain " + discountSku, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		DiscountSku discountSku1, DiscountSku discountSku2) {

		Assert.assertTrue(
			discountSku1 + " does not equal " + discountSku2,
			equals(discountSku1, discountSku2));
	}

	protected void assertEquals(
		List<DiscountSku> discountSkus1, List<DiscountSku> discountSkus2) {

		Assert.assertEquals(discountSkus1.size(), discountSkus2.size());

		for (int i = 0; i < discountSkus1.size(); i++) {
			DiscountSku discountSku1 = discountSkus1.get(i);
			DiscountSku discountSku2 = discountSkus2.get(i);

			assertEquals(discountSku1, discountSku2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<DiscountSku> discountSkus1, List<DiscountSku> discountSkus2) {

		Assert.assertEquals(discountSkus1.size(), discountSkus2.size());

		for (DiscountSku discountSku1 : discountSkus1) {
			boolean contains = false;

			for (DiscountSku discountSku2 : discountSkus2) {
				if (equals(discountSku1, discountSku2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				discountSkus2 + " does not contain " + discountSku1, contains);
		}
	}

	protected void assertValid(DiscountSku discountSku) throws Exception {
		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (discountSku.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"discountExternalReferenceCode",
					additionalAssertFieldName)) {

				if (discountSku.getDiscountExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("discountId", additionalAssertFieldName)) {
				if (discountSku.getDiscountId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("discountSkuId", additionalAssertFieldName)) {
				if (discountSku.getDiscountSkuId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (discountSku.getProductId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productName", additionalAssertFieldName)) {
				if (discountSku.getProductName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("sku", additionalAssertFieldName)) {
				if (discountSku.getSku() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"skuExternalReferenceCode", additionalAssertFieldName)) {

				if (discountSku.getSkuExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("skuId", additionalAssertFieldName)) {
				if (discountSku.getSkuId() == null) {
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

	protected void assertValid(Page<DiscountSku> page) {
		boolean valid = false;

		java.util.Collection<DiscountSku> discountSkus = page.getItems();

		int size = discountSkus.size();

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
					com.liferay.headless.commerce.admin.pricing.dto.v2_0.
						DiscountSku.class)) {

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
		DiscountSku discountSku1, DiscountSku discountSku2) {

		if (discountSku1 == discountSku2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)discountSku1.getActions(),
						(Map)discountSku2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"discountExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						discountSku1.getDiscountExternalReferenceCode(),
						discountSku2.getDiscountExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("discountId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						discountSku1.getDiscountId(),
						discountSku2.getDiscountId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("discountSkuId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						discountSku1.getDiscountSkuId(),
						discountSku2.getDiscountSkuId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						discountSku1.getProductId(),
						discountSku2.getProductId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productName", additionalAssertFieldName)) {
				if (!equals(
						(Map)discountSku1.getProductName(),
						(Map)discountSku2.getProductName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("sku", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						discountSku1.getSku(), discountSku2.getSku())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"skuExternalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						discountSku1.getSkuExternalReferenceCode(),
						discountSku2.getSkuExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("skuId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						discountSku1.getSkuId(), discountSku2.getSkuId())) {

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

		if (!(_discountSkuResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_discountSkuResource;

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
		EntityField entityField, String operator, DiscountSku discountSku) {

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

		if (entityFieldName.equals("discountExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(discountSku.getDiscountExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("discountId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("discountSkuId")) {
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

		if (entityFieldName.equals("sku")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("skuExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(discountSku.getSkuExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("skuId")) {
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

	protected DiscountSku randomDiscountSku() throws Exception {
		return new DiscountSku() {
			{
				discountExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				discountId = RandomTestUtil.randomLong();
				discountSkuId = RandomTestUtil.randomLong();
				productId = RandomTestUtil.randomLong();
				skuExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				skuId = RandomTestUtil.randomLong();
			}
		};
	}

	protected DiscountSku randomIrrelevantDiscountSku() throws Exception {
		DiscountSku randomIrrelevantDiscountSku = randomDiscountSku();

		return randomIrrelevantDiscountSku;
	}

	protected DiscountSku randomPatchDiscountSku() throws Exception {
		return randomDiscountSku();
	}

	protected DiscountSkuResource discountSkuResource;
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
		LogFactoryUtil.getLog(BaseDiscountSkuResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.pricing.resource.v2_0.
		DiscountSkuResource _discountSkuResource;

}