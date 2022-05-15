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

package com.liferay.headless.commerce.admin.channel.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.channel.client.dto.v1_0.PaymentMethodGroupRelOrderType;
import com.liferay.headless.commerce.admin.channel.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.channel.client.pagination.Page;
import com.liferay.headless.commerce.admin.channel.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.channel.client.resource.v1_0.PaymentMethodGroupRelOrderTypeResource;
import com.liferay.headless.commerce.admin.channel.client.serdes.v1_0.PaymentMethodGroupRelOrderTypeSerDes;
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
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public abstract class BasePaymentMethodGroupRelOrderTypeResourceTestCase {

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

		_paymentMethodGroupRelOrderTypeResource.setContextCompany(testCompany);

		PaymentMethodGroupRelOrderTypeResource.Builder builder =
			PaymentMethodGroupRelOrderTypeResource.builder();

		paymentMethodGroupRelOrderTypeResource = builder.authentication(
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

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType1 =
			randomPaymentMethodGroupRelOrderType();

		String json = objectMapper.writeValueAsString(
			paymentMethodGroupRelOrderType1);

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType2 =
			PaymentMethodGroupRelOrderTypeSerDes.toDTO(json);

		Assert.assertTrue(
			equals(
				paymentMethodGroupRelOrderType1,
				paymentMethodGroupRelOrderType2));
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

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType =
			randomPaymentMethodGroupRelOrderType();

		String json1 = objectMapper.writeValueAsString(
			paymentMethodGroupRelOrderType);
		String json2 = PaymentMethodGroupRelOrderTypeSerDes.toJSON(
			paymentMethodGroupRelOrderType);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType =
			randomPaymentMethodGroupRelOrderType();

		paymentMethodGroupRelOrderType.setOrderTypeExternalReferenceCode(regex);

		String json = PaymentMethodGroupRelOrderTypeSerDes.toJSON(
			paymentMethodGroupRelOrderType);

		Assert.assertFalse(json.contains(regex));

		paymentMethodGroupRelOrderType =
			PaymentMethodGroupRelOrderTypeSerDes.toDTO(json);

		Assert.assertEquals(
			regex,
			paymentMethodGroupRelOrderType.getOrderTypeExternalReferenceCode());
	}

	@Test
	public void testDeletePaymentMethodGroupRelOrderType() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLDeletePaymentMethodGroupRelOrderType()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage()
		throws Exception {

		Long id =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_getId();
		Long irrelevantId =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_getIrrelevantId();

		Page<PaymentMethodGroupRelOrderType> page =
			paymentMethodGroupRelOrderTypeResource.
				getPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage(
					id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			PaymentMethodGroupRelOrderType
				irrelevantPaymentMethodGroupRelOrderType =
					testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
						irrelevantId,
						randomIrrelevantPaymentMethodGroupRelOrderType());

			page =
				paymentMethodGroupRelOrderTypeResource.
					getPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage(
						irrelevantId, null, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantPaymentMethodGroupRelOrderType),
				(List<PaymentMethodGroupRelOrderType>)page.getItems());
			assertValid(page);
		}

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType1 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				id, randomPaymentMethodGroupRelOrderType());

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType2 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				id, randomPaymentMethodGroupRelOrderType());

		page =
			paymentMethodGroupRelOrderTypeResource.
				getPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage(
					id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(
				paymentMethodGroupRelOrderType1,
				paymentMethodGroupRelOrderType2),
			(List<PaymentMethodGroupRelOrderType>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_getId();

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType1 =
			randomPaymentMethodGroupRelOrderType();

		paymentMethodGroupRelOrderType1 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				id, paymentMethodGroupRelOrderType1);

		for (EntityField entityField : entityFields) {
			Page<PaymentMethodGroupRelOrderType> page =
				paymentMethodGroupRelOrderTypeResource.
					getPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage(
						id, null,
						getFilterString(
							entityField, "between",
							paymentMethodGroupRelOrderType1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(paymentMethodGroupRelOrderType1),
				(List<PaymentMethodGroupRelOrderType>)page.getItems());
		}
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_getId();

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType1 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				id, randomPaymentMethodGroupRelOrderType());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType2 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				id, randomPaymentMethodGroupRelOrderType());

		for (EntityField entityField : entityFields) {
			Page<PaymentMethodGroupRelOrderType> page =
				paymentMethodGroupRelOrderTypeResource.
					getPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage(
						id, null,
						getFilterString(
							entityField, "eq", paymentMethodGroupRelOrderType1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(paymentMethodGroupRelOrderType1),
				(List<PaymentMethodGroupRelOrderType>)page.getItems());
		}
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_getId();

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType1 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				id, randomPaymentMethodGroupRelOrderType());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType2 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				id, randomPaymentMethodGroupRelOrderType());

		for (EntityField entityField : entityFields) {
			Page<PaymentMethodGroupRelOrderType> page =
				paymentMethodGroupRelOrderTypeResource.
					getPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage(
						id, null,
						getFilterString(
							entityField, "eq", paymentMethodGroupRelOrderType1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(paymentMethodGroupRelOrderType1),
				(List<PaymentMethodGroupRelOrderType>)page.getItems());
		}
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithPagination()
		throws Exception {

		Long id =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_getId();

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType1 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				id, randomPaymentMethodGroupRelOrderType());

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType2 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				id, randomPaymentMethodGroupRelOrderType());

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType3 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				id, randomPaymentMethodGroupRelOrderType());

		Page<PaymentMethodGroupRelOrderType> page1 =
			paymentMethodGroupRelOrderTypeResource.
				getPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage(
					id, null, null, Pagination.of(1, 2), null);

		List<PaymentMethodGroupRelOrderType> paymentMethodGroupRelOrderTypes1 =
			(List<PaymentMethodGroupRelOrderType>)page1.getItems();

		Assert.assertEquals(
			paymentMethodGroupRelOrderTypes1.toString(), 2,
			paymentMethodGroupRelOrderTypes1.size());

		Page<PaymentMethodGroupRelOrderType> page2 =
			paymentMethodGroupRelOrderTypeResource.
				getPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage(
					id, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<PaymentMethodGroupRelOrderType> paymentMethodGroupRelOrderTypes2 =
			(List<PaymentMethodGroupRelOrderType>)page2.getItems();

		Assert.assertEquals(
			paymentMethodGroupRelOrderTypes2.toString(), 1,
			paymentMethodGroupRelOrderTypes2.size());

		Page<PaymentMethodGroupRelOrderType> page3 =
			paymentMethodGroupRelOrderTypeResource.
				getPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage(
					id, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(
				paymentMethodGroupRelOrderType1,
				paymentMethodGroupRelOrderType2,
				paymentMethodGroupRelOrderType3),
			(List<PaymentMethodGroupRelOrderType>)page3.getItems());
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithSortDateTime()
		throws Exception {

		testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, paymentMethodGroupRelOrderType1,
			 paymentMethodGroupRelOrderType2) -> {

				BeanTestUtil.setProperty(
					paymentMethodGroupRelOrderType1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithSortDouble()
		throws Exception {

		testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, paymentMethodGroupRelOrderType1,
			 paymentMethodGroupRelOrderType2) -> {

				BeanTestUtil.setProperty(
					paymentMethodGroupRelOrderType1, entityField.getName(),
					0.1);
				BeanTestUtil.setProperty(
					paymentMethodGroupRelOrderType2, entityField.getName(),
					0.5);
			});
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithSortInteger()
		throws Exception {

		testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, paymentMethodGroupRelOrderType1,
			 paymentMethodGroupRelOrderType2) -> {

				BeanTestUtil.setProperty(
					paymentMethodGroupRelOrderType1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					paymentMethodGroupRelOrderType2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithSortString()
		throws Exception {

		testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithSort(
			EntityField.Type.STRING,
			(entityField, paymentMethodGroupRelOrderType1,
			 paymentMethodGroupRelOrderType2) -> {

				Class<?> clazz = paymentMethodGroupRelOrderType1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						paymentMethodGroupRelOrderType1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						paymentMethodGroupRelOrderType2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						paymentMethodGroupRelOrderType1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						paymentMethodGroupRelOrderType2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						paymentMethodGroupRelOrderType1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						paymentMethodGroupRelOrderType2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPageWithSort(
				EntityField.Type type,
				UnsafeTriConsumer
					<EntityField, PaymentMethodGroupRelOrderType,
					 PaymentMethodGroupRelOrderType, Exception>
						unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_getId();

		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType1 =
			randomPaymentMethodGroupRelOrderType();
		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType2 =
			randomPaymentMethodGroupRelOrderType();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, paymentMethodGroupRelOrderType1,
				paymentMethodGroupRelOrderType2);
		}

		paymentMethodGroupRelOrderType1 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				id, paymentMethodGroupRelOrderType1);

		paymentMethodGroupRelOrderType2 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				id, paymentMethodGroupRelOrderType2);

		for (EntityField entityField : entityFields) {
			Page<PaymentMethodGroupRelOrderType> ascPage =
				paymentMethodGroupRelOrderTypeResource.
					getPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage(
						id, null, null, Pagination.of(1, 2),
						entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(
					paymentMethodGroupRelOrderType1,
					paymentMethodGroupRelOrderType2),
				(List<PaymentMethodGroupRelOrderType>)ascPage.getItems());

			Page<PaymentMethodGroupRelOrderType> descPage =
				paymentMethodGroupRelOrderTypeResource.
					getPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage(
						id, null, null, Pagination.of(1, 2),
						entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(
					paymentMethodGroupRelOrderType2,
					paymentMethodGroupRelOrderType1),
				(List<PaymentMethodGroupRelOrderType>)descPage.getItems());
		}
	}

	protected PaymentMethodGroupRelOrderType
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_addPaymentMethodGroupRelOrderType(
				Long id,
				PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelOrderTypesPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostPaymentMethodGroupRelIdPaymentMethodGroupRelOrderType()
		throws Exception {

		PaymentMethodGroupRelOrderType randomPaymentMethodGroupRelOrderType =
			randomPaymentMethodGroupRelOrderType();

		PaymentMethodGroupRelOrderType postPaymentMethodGroupRelOrderType =
			testPostPaymentMethodGroupRelIdPaymentMethodGroupRelOrderType_addPaymentMethodGroupRelOrderType(
				randomPaymentMethodGroupRelOrderType);

		assertEquals(
			randomPaymentMethodGroupRelOrderType,
			postPaymentMethodGroupRelOrderType);
		assertValid(postPaymentMethodGroupRelOrderType);
	}

	protected PaymentMethodGroupRelOrderType
			testPostPaymentMethodGroupRelIdPaymentMethodGroupRelOrderType_addPaymentMethodGroupRelOrderType(
				PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected void assertContains(
		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType,
		List<PaymentMethodGroupRelOrderType> paymentMethodGroupRelOrderTypes) {

		boolean contains = false;

		for (PaymentMethodGroupRelOrderType item :
				paymentMethodGroupRelOrderTypes) {

			if (equals(paymentMethodGroupRelOrderType, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			paymentMethodGroupRelOrderTypes + " does not contain " +
				paymentMethodGroupRelOrderType,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType1,
		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType2) {

		Assert.assertTrue(
			paymentMethodGroupRelOrderType1 + " does not equal " +
				paymentMethodGroupRelOrderType2,
			equals(
				paymentMethodGroupRelOrderType1,
				paymentMethodGroupRelOrderType2));
	}

	protected void assertEquals(
		List<PaymentMethodGroupRelOrderType> paymentMethodGroupRelOrderTypes1,
		List<PaymentMethodGroupRelOrderType> paymentMethodGroupRelOrderTypes2) {

		Assert.assertEquals(
			paymentMethodGroupRelOrderTypes1.size(),
			paymentMethodGroupRelOrderTypes2.size());

		for (int i = 0; i < paymentMethodGroupRelOrderTypes1.size(); i++) {
			PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType1 =
				paymentMethodGroupRelOrderTypes1.get(i);
			PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType2 =
				paymentMethodGroupRelOrderTypes2.get(i);

			assertEquals(
				paymentMethodGroupRelOrderType1,
				paymentMethodGroupRelOrderType2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<PaymentMethodGroupRelOrderType> paymentMethodGroupRelOrderTypes1,
		List<PaymentMethodGroupRelOrderType> paymentMethodGroupRelOrderTypes2) {

		Assert.assertEquals(
			paymentMethodGroupRelOrderTypes1.size(),
			paymentMethodGroupRelOrderTypes2.size());

		for (PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType1 :
				paymentMethodGroupRelOrderTypes1) {

			boolean contains = false;

			for (PaymentMethodGroupRelOrderType
					paymentMethodGroupRelOrderType2 :
						paymentMethodGroupRelOrderTypes2) {

				if (equals(
						paymentMethodGroupRelOrderType1,
						paymentMethodGroupRelOrderType2)) {

					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				paymentMethodGroupRelOrderTypes2 + " does not contain " +
					paymentMethodGroupRelOrderType1,
				contains);
		}
	}

	protected void assertValid(
			PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (paymentMethodGroupRelOrderType.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("orderType", additionalAssertFieldName)) {
				if (paymentMethodGroupRelOrderType.getOrderType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"orderTypeExternalReferenceCode",
					additionalAssertFieldName)) {

				if (paymentMethodGroupRelOrderType.
						getOrderTypeExternalReferenceCode() == null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("orderTypeId", additionalAssertFieldName)) {
				if (paymentMethodGroupRelOrderType.getOrderTypeId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"paymentMethodGroupRelId", additionalAssertFieldName)) {

				if (paymentMethodGroupRelOrderType.
						getPaymentMethodGroupRelId() == null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"paymentMethodGroupRelOrderTypeId",
					additionalAssertFieldName)) {

				if (paymentMethodGroupRelOrderType.
						getPaymentMethodGroupRelOrderTypeId() == null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (paymentMethodGroupRelOrderType.getPriority() == null) {
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

	protected void assertValid(Page<PaymentMethodGroupRelOrderType> page) {
		boolean valid = false;

		java.util.Collection<PaymentMethodGroupRelOrderType>
			paymentMethodGroupRelOrderTypes = page.getItems();

		int size = paymentMethodGroupRelOrderTypes.size();

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
					com.liferay.headless.commerce.admin.channel.dto.v1_0.
						PaymentMethodGroupRelOrderType.class)) {

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
		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType1,
		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType2) {

		if (paymentMethodGroupRelOrderType1 ==
				paymentMethodGroupRelOrderType2) {

			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)paymentMethodGroupRelOrderType1.getActions(),
						(Map)paymentMethodGroupRelOrderType2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("orderType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						paymentMethodGroupRelOrderType1.getOrderType(),
						paymentMethodGroupRelOrderType2.getOrderType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"orderTypeExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						paymentMethodGroupRelOrderType1.
							getOrderTypeExternalReferenceCode(),
						paymentMethodGroupRelOrderType2.
							getOrderTypeExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("orderTypeId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						paymentMethodGroupRelOrderType1.getOrderTypeId(),
						paymentMethodGroupRelOrderType2.getOrderTypeId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"paymentMethodGroupRelId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						paymentMethodGroupRelOrderType1.
							getPaymentMethodGroupRelId(),
						paymentMethodGroupRelOrderType2.
							getPaymentMethodGroupRelId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"paymentMethodGroupRelOrderTypeId",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						paymentMethodGroupRelOrderType1.
							getPaymentMethodGroupRelOrderTypeId(),
						paymentMethodGroupRelOrderType2.
							getPaymentMethodGroupRelOrderTypeId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						paymentMethodGroupRelOrderType1.getPriority(),
						paymentMethodGroupRelOrderType2.getPriority())) {

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

		if (!(_paymentMethodGroupRelOrderTypeResource instanceof
				EntityModelResource)) {

			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_paymentMethodGroupRelOrderTypeResource;

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
		PaymentMethodGroupRelOrderType paymentMethodGroupRelOrderType) {

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

		if (entityFieldName.equals("orderType")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("orderTypeExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					paymentMethodGroupRelOrderType.
						getOrderTypeExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("orderTypeId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("paymentMethodGroupRelId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("paymentMethodGroupRelOrderTypeId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("priority")) {
			sb.append(
				String.valueOf(paymentMethodGroupRelOrderType.getPriority()));

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

	protected PaymentMethodGroupRelOrderType
			randomPaymentMethodGroupRelOrderType()
		throws Exception {

		return new PaymentMethodGroupRelOrderType() {
			{
				orderTypeExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				orderTypeId = RandomTestUtil.randomLong();
				paymentMethodGroupRelId = RandomTestUtil.randomLong();
				paymentMethodGroupRelOrderTypeId = RandomTestUtil.randomLong();
				priority = RandomTestUtil.randomInt();
			}
		};
	}

	protected PaymentMethodGroupRelOrderType
			randomIrrelevantPaymentMethodGroupRelOrderType()
		throws Exception {

		PaymentMethodGroupRelOrderType
			randomIrrelevantPaymentMethodGroupRelOrderType =
				randomPaymentMethodGroupRelOrderType();

		return randomIrrelevantPaymentMethodGroupRelOrderType;
	}

	protected PaymentMethodGroupRelOrderType
			randomPatchPaymentMethodGroupRelOrderType()
		throws Exception {

		return randomPaymentMethodGroupRelOrderType();
	}

	protected PaymentMethodGroupRelOrderTypeResource
		paymentMethodGroupRelOrderTypeResource;
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
		LogFactoryUtil.getLog(
			BasePaymentMethodGroupRelOrderTypeResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.channel.resource.v1_0.
		PaymentMethodGroupRelOrderTypeResource
			_paymentMethodGroupRelOrderTypeResource;

}