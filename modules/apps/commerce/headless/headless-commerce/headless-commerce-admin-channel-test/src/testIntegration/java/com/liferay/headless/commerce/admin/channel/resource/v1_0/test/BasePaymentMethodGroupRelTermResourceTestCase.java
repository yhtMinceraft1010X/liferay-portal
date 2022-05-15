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

import com.liferay.headless.commerce.admin.channel.client.dto.v1_0.PaymentMethodGroupRelTerm;
import com.liferay.headless.commerce.admin.channel.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.channel.client.pagination.Page;
import com.liferay.headless.commerce.admin.channel.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.channel.client.resource.v1_0.PaymentMethodGroupRelTermResource;
import com.liferay.headless.commerce.admin.channel.client.serdes.v1_0.PaymentMethodGroupRelTermSerDes;
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
public abstract class BasePaymentMethodGroupRelTermResourceTestCase {

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

		_paymentMethodGroupRelTermResource.setContextCompany(testCompany);

		PaymentMethodGroupRelTermResource.Builder builder =
			PaymentMethodGroupRelTermResource.builder();

		paymentMethodGroupRelTermResource = builder.authentication(
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

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm1 =
			randomPaymentMethodGroupRelTerm();

		String json = objectMapper.writeValueAsString(
			paymentMethodGroupRelTerm1);

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm2 =
			PaymentMethodGroupRelTermSerDes.toDTO(json);

		Assert.assertTrue(
			equals(paymentMethodGroupRelTerm1, paymentMethodGroupRelTerm2));
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

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm =
			randomPaymentMethodGroupRelTerm();

		String json1 = objectMapper.writeValueAsString(
			paymentMethodGroupRelTerm);
		String json2 = PaymentMethodGroupRelTermSerDes.toJSON(
			paymentMethodGroupRelTerm);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm =
			randomPaymentMethodGroupRelTerm();

		paymentMethodGroupRelTerm.setTermExternalReferenceCode(regex);

		String json = PaymentMethodGroupRelTermSerDes.toJSON(
			paymentMethodGroupRelTerm);

		Assert.assertFalse(json.contains(regex));

		paymentMethodGroupRelTerm = PaymentMethodGroupRelTermSerDes.toDTO(json);

		Assert.assertEquals(
			regex, paymentMethodGroupRelTerm.getTermExternalReferenceCode());
	}

	@Test
	public void testDeletePaymentMethodGroupRelTerm() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLDeletePaymentMethodGroupRelTerm() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage()
		throws Exception {

		Long id =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_getId();
		Long irrelevantId =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_getIrrelevantId();

		Page<PaymentMethodGroupRelTerm> page =
			paymentMethodGroupRelTermResource.
				getPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage(
					id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			PaymentMethodGroupRelTerm irrelevantPaymentMethodGroupRelTerm =
				testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
					irrelevantId, randomIrrelevantPaymentMethodGroupRelTerm());

			page =
				paymentMethodGroupRelTermResource.
					getPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage(
						irrelevantId, null, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantPaymentMethodGroupRelTerm),
				(List<PaymentMethodGroupRelTerm>)page.getItems());
			assertValid(page);
		}

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm1 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				id, randomPaymentMethodGroupRelTerm());

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm2 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				id, randomPaymentMethodGroupRelTerm());

		page =
			paymentMethodGroupRelTermResource.
				getPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage(
					id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(
				paymentMethodGroupRelTerm1, paymentMethodGroupRelTerm2),
			(List<PaymentMethodGroupRelTerm>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_getId();

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm1 =
			randomPaymentMethodGroupRelTerm();

		paymentMethodGroupRelTerm1 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				id, paymentMethodGroupRelTerm1);

		for (EntityField entityField : entityFields) {
			Page<PaymentMethodGroupRelTerm> page =
				paymentMethodGroupRelTermResource.
					getPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage(
						id, null,
						getFilterString(
							entityField, "between", paymentMethodGroupRelTerm1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(paymentMethodGroupRelTerm1),
				(List<PaymentMethodGroupRelTerm>)page.getItems());
		}
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_getId();

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm1 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				id, randomPaymentMethodGroupRelTerm());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm2 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				id, randomPaymentMethodGroupRelTerm());

		for (EntityField entityField : entityFields) {
			Page<PaymentMethodGroupRelTerm> page =
				paymentMethodGroupRelTermResource.
					getPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage(
						id, null,
						getFilterString(
							entityField, "eq", paymentMethodGroupRelTerm1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(paymentMethodGroupRelTerm1),
				(List<PaymentMethodGroupRelTerm>)page.getItems());
		}
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_getId();

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm1 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				id, randomPaymentMethodGroupRelTerm());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm2 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				id, randomPaymentMethodGroupRelTerm());

		for (EntityField entityField : entityFields) {
			Page<PaymentMethodGroupRelTerm> page =
				paymentMethodGroupRelTermResource.
					getPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage(
						id, null,
						getFilterString(
							entityField, "eq", paymentMethodGroupRelTerm1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(paymentMethodGroupRelTerm1),
				(List<PaymentMethodGroupRelTerm>)page.getItems());
		}
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithPagination()
		throws Exception {

		Long id =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_getId();

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm1 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				id, randomPaymentMethodGroupRelTerm());

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm2 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				id, randomPaymentMethodGroupRelTerm());

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm3 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				id, randomPaymentMethodGroupRelTerm());

		Page<PaymentMethodGroupRelTerm> page1 =
			paymentMethodGroupRelTermResource.
				getPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage(
					id, null, null, Pagination.of(1, 2), null);

		List<PaymentMethodGroupRelTerm> paymentMethodGroupRelTerms1 =
			(List<PaymentMethodGroupRelTerm>)page1.getItems();

		Assert.assertEquals(
			paymentMethodGroupRelTerms1.toString(), 2,
			paymentMethodGroupRelTerms1.size());

		Page<PaymentMethodGroupRelTerm> page2 =
			paymentMethodGroupRelTermResource.
				getPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage(
					id, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<PaymentMethodGroupRelTerm> paymentMethodGroupRelTerms2 =
			(List<PaymentMethodGroupRelTerm>)page2.getItems();

		Assert.assertEquals(
			paymentMethodGroupRelTerms2.toString(), 1,
			paymentMethodGroupRelTerms2.size());

		Page<PaymentMethodGroupRelTerm> page3 =
			paymentMethodGroupRelTermResource.
				getPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage(
					id, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(
				paymentMethodGroupRelTerm1, paymentMethodGroupRelTerm2,
				paymentMethodGroupRelTerm3),
			(List<PaymentMethodGroupRelTerm>)page3.getItems());
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithSortDateTime()
		throws Exception {

		testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, paymentMethodGroupRelTerm1,
			 paymentMethodGroupRelTerm2) -> {

				BeanTestUtil.setProperty(
					paymentMethodGroupRelTerm1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithSortDouble()
		throws Exception {

		testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, paymentMethodGroupRelTerm1,
			 paymentMethodGroupRelTerm2) -> {

				BeanTestUtil.setProperty(
					paymentMethodGroupRelTerm1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					paymentMethodGroupRelTerm2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithSortInteger()
		throws Exception {

		testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, paymentMethodGroupRelTerm1,
			 paymentMethodGroupRelTerm2) -> {

				BeanTestUtil.setProperty(
					paymentMethodGroupRelTerm1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					paymentMethodGroupRelTerm2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithSortString()
		throws Exception {

		testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithSort(
			EntityField.Type.STRING,
			(entityField, paymentMethodGroupRelTerm1,
			 paymentMethodGroupRelTerm2) -> {

				Class<?> clazz = paymentMethodGroupRelTerm1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						paymentMethodGroupRelTerm1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						paymentMethodGroupRelTerm2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						paymentMethodGroupRelTerm1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						paymentMethodGroupRelTerm2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						paymentMethodGroupRelTerm1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						paymentMethodGroupRelTerm2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPageWithSort(
				EntityField.Type type,
				UnsafeTriConsumer
					<EntityField, PaymentMethodGroupRelTerm,
					 PaymentMethodGroupRelTerm, Exception> unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_getId();

		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm1 =
			randomPaymentMethodGroupRelTerm();
		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm2 =
			randomPaymentMethodGroupRelTerm();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, paymentMethodGroupRelTerm1,
				paymentMethodGroupRelTerm2);
		}

		paymentMethodGroupRelTerm1 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				id, paymentMethodGroupRelTerm1);

		paymentMethodGroupRelTerm2 =
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				id, paymentMethodGroupRelTerm2);

		for (EntityField entityField : entityFields) {
			Page<PaymentMethodGroupRelTerm> ascPage =
				paymentMethodGroupRelTermResource.
					getPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage(
						id, null, null, Pagination.of(1, 2),
						entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(
					paymentMethodGroupRelTerm1, paymentMethodGroupRelTerm2),
				(List<PaymentMethodGroupRelTerm>)ascPage.getItems());

			Page<PaymentMethodGroupRelTerm> descPage =
				paymentMethodGroupRelTermResource.
					getPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage(
						id, null, null, Pagination.of(1, 2),
						entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(
					paymentMethodGroupRelTerm2, paymentMethodGroupRelTerm1),
				(List<PaymentMethodGroupRelTerm>)descPage.getItems());
		}
	}

	protected PaymentMethodGroupRelTerm
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_addPaymentMethodGroupRelTerm(
				Long id, PaymentMethodGroupRelTerm paymentMethodGroupRelTerm)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetPaymentMethodGroupRelIdPaymentMethodGroupRelTermsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostPaymentMethodGroupRelIdPaymentMethodGroupRelTerm()
		throws Exception {

		PaymentMethodGroupRelTerm randomPaymentMethodGroupRelTerm =
			randomPaymentMethodGroupRelTerm();

		PaymentMethodGroupRelTerm postPaymentMethodGroupRelTerm =
			testPostPaymentMethodGroupRelIdPaymentMethodGroupRelTerm_addPaymentMethodGroupRelTerm(
				randomPaymentMethodGroupRelTerm);

		assertEquals(
			randomPaymentMethodGroupRelTerm, postPaymentMethodGroupRelTerm);
		assertValid(postPaymentMethodGroupRelTerm);
	}

	protected PaymentMethodGroupRelTerm
			testPostPaymentMethodGroupRelIdPaymentMethodGroupRelTerm_addPaymentMethodGroupRelTerm(
				PaymentMethodGroupRelTerm paymentMethodGroupRelTerm)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected void assertContains(
		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm,
		List<PaymentMethodGroupRelTerm> paymentMethodGroupRelTerms) {

		boolean contains = false;

		for (PaymentMethodGroupRelTerm item : paymentMethodGroupRelTerms) {
			if (equals(paymentMethodGroupRelTerm, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			paymentMethodGroupRelTerms + " does not contain " +
				paymentMethodGroupRelTerm,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm1,
		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm2) {

		Assert.assertTrue(
			paymentMethodGroupRelTerm1 + " does not equal " +
				paymentMethodGroupRelTerm2,
			equals(paymentMethodGroupRelTerm1, paymentMethodGroupRelTerm2));
	}

	protected void assertEquals(
		List<PaymentMethodGroupRelTerm> paymentMethodGroupRelTerms1,
		List<PaymentMethodGroupRelTerm> paymentMethodGroupRelTerms2) {

		Assert.assertEquals(
			paymentMethodGroupRelTerms1.size(),
			paymentMethodGroupRelTerms2.size());

		for (int i = 0; i < paymentMethodGroupRelTerms1.size(); i++) {
			PaymentMethodGroupRelTerm paymentMethodGroupRelTerm1 =
				paymentMethodGroupRelTerms1.get(i);
			PaymentMethodGroupRelTerm paymentMethodGroupRelTerm2 =
				paymentMethodGroupRelTerms2.get(i);

			assertEquals(
				paymentMethodGroupRelTerm1, paymentMethodGroupRelTerm2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<PaymentMethodGroupRelTerm> paymentMethodGroupRelTerms1,
		List<PaymentMethodGroupRelTerm> paymentMethodGroupRelTerms2) {

		Assert.assertEquals(
			paymentMethodGroupRelTerms1.size(),
			paymentMethodGroupRelTerms2.size());

		for (PaymentMethodGroupRelTerm paymentMethodGroupRelTerm1 :
				paymentMethodGroupRelTerms1) {

			boolean contains = false;

			for (PaymentMethodGroupRelTerm paymentMethodGroupRelTerm2 :
					paymentMethodGroupRelTerms2) {

				if (equals(
						paymentMethodGroupRelTerm1,
						paymentMethodGroupRelTerm2)) {

					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				paymentMethodGroupRelTerms2 + " does not contain " +
					paymentMethodGroupRelTerm1,
				contains);
		}
	}

	protected void assertValid(
			PaymentMethodGroupRelTerm paymentMethodGroupRelTerm)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (paymentMethodGroupRelTerm.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"paymentMethodGroupRelId", additionalAssertFieldName)) {

				if (paymentMethodGroupRelTerm.getPaymentMethodGroupRelId() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"paymentMethodGroupRelTermId", additionalAssertFieldName)) {

				if (paymentMethodGroupRelTerm.
						getPaymentMethodGroupRelTermId() == null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("term", additionalAssertFieldName)) {
				if (paymentMethodGroupRelTerm.getTerm() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"termExternalReferenceCode", additionalAssertFieldName)) {

				if (paymentMethodGroupRelTerm.getTermExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("termId", additionalAssertFieldName)) {
				if (paymentMethodGroupRelTerm.getTermId() == null) {
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

	protected void assertValid(Page<PaymentMethodGroupRelTerm> page) {
		boolean valid = false;

		java.util.Collection<PaymentMethodGroupRelTerm>
			paymentMethodGroupRelTerms = page.getItems();

		int size = paymentMethodGroupRelTerms.size();

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
						PaymentMethodGroupRelTerm.class)) {

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
		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm1,
		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm2) {

		if (paymentMethodGroupRelTerm1 == paymentMethodGroupRelTerm2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)paymentMethodGroupRelTerm1.getActions(),
						(Map)paymentMethodGroupRelTerm2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"paymentMethodGroupRelId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						paymentMethodGroupRelTerm1.getPaymentMethodGroupRelId(),
						paymentMethodGroupRelTerm2.
							getPaymentMethodGroupRelId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"paymentMethodGroupRelTermId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						paymentMethodGroupRelTerm1.
							getPaymentMethodGroupRelTermId(),
						paymentMethodGroupRelTerm2.
							getPaymentMethodGroupRelTermId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("term", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						paymentMethodGroupRelTerm1.getTerm(),
						paymentMethodGroupRelTerm2.getTerm())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"termExternalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						paymentMethodGroupRelTerm1.
							getTermExternalReferenceCode(),
						paymentMethodGroupRelTerm2.
							getTermExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("termId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						paymentMethodGroupRelTerm1.getTermId(),
						paymentMethodGroupRelTerm2.getTermId())) {

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

		if (!(_paymentMethodGroupRelTermResource instanceof
				EntityModelResource)) {

			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_paymentMethodGroupRelTermResource;

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
		PaymentMethodGroupRelTerm paymentMethodGroupRelTerm) {

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

		if (entityFieldName.equals("paymentMethodGroupRelId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("paymentMethodGroupRelTermId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("term")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("termExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					paymentMethodGroupRelTerm.getTermExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("termId")) {
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

	protected PaymentMethodGroupRelTerm randomPaymentMethodGroupRelTerm()
		throws Exception {

		return new PaymentMethodGroupRelTerm() {
			{
				paymentMethodGroupRelId = RandomTestUtil.randomLong();
				paymentMethodGroupRelTermId = RandomTestUtil.randomLong();
				termExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				termId = RandomTestUtil.randomLong();
			}
		};
	}

	protected PaymentMethodGroupRelTerm
			randomIrrelevantPaymentMethodGroupRelTerm()
		throws Exception {

		PaymentMethodGroupRelTerm randomIrrelevantPaymentMethodGroupRelTerm =
			randomPaymentMethodGroupRelTerm();

		return randomIrrelevantPaymentMethodGroupRelTerm;
	}

	protected PaymentMethodGroupRelTerm randomPatchPaymentMethodGroupRelTerm()
		throws Exception {

		return randomPaymentMethodGroupRelTerm();
	}

	protected PaymentMethodGroupRelTermResource
		paymentMethodGroupRelTermResource;
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
			BasePaymentMethodGroupRelTermResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.channel.resource.v1_0.
		PaymentMethodGroupRelTermResource _paymentMethodGroupRelTermResource;

}