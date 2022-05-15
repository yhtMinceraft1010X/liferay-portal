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

import com.liferay.headless.commerce.admin.channel.client.dto.v1_0.ShippingFixedOptionTerm;
import com.liferay.headless.commerce.admin.channel.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.channel.client.pagination.Page;
import com.liferay.headless.commerce.admin.channel.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.channel.client.resource.v1_0.ShippingFixedOptionTermResource;
import com.liferay.headless.commerce.admin.channel.client.serdes.v1_0.ShippingFixedOptionTermSerDes;
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
public abstract class BaseShippingFixedOptionTermResourceTestCase {

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

		_shippingFixedOptionTermResource.setContextCompany(testCompany);

		ShippingFixedOptionTermResource.Builder builder =
			ShippingFixedOptionTermResource.builder();

		shippingFixedOptionTermResource = builder.authentication(
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

		ShippingFixedOptionTerm shippingFixedOptionTerm1 =
			randomShippingFixedOptionTerm();

		String json = objectMapper.writeValueAsString(shippingFixedOptionTerm1);

		ShippingFixedOptionTerm shippingFixedOptionTerm2 =
			ShippingFixedOptionTermSerDes.toDTO(json);

		Assert.assertTrue(
			equals(shippingFixedOptionTerm1, shippingFixedOptionTerm2));
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

		ShippingFixedOptionTerm shippingFixedOptionTerm =
			randomShippingFixedOptionTerm();

		String json1 = objectMapper.writeValueAsString(shippingFixedOptionTerm);
		String json2 = ShippingFixedOptionTermSerDes.toJSON(
			shippingFixedOptionTerm);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ShippingFixedOptionTerm shippingFixedOptionTerm =
			randomShippingFixedOptionTerm();

		shippingFixedOptionTerm.setTermExternalReferenceCode(regex);

		String json = ShippingFixedOptionTermSerDes.toJSON(
			shippingFixedOptionTerm);

		Assert.assertFalse(json.contains(regex));

		shippingFixedOptionTerm = ShippingFixedOptionTermSerDes.toDTO(json);

		Assert.assertEquals(
			regex, shippingFixedOptionTerm.getTermExternalReferenceCode());
	}

	@Test
	public void testDeleteShippingFixedOptionTerm() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLDeleteShippingFixedOptionTerm() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetShippingFixedOptionIdShippingFixedOptionTermsPage()
		throws Exception {

		Long id =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_getId();
		Long irrelevantId =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_getIrrelevantId();

		Page<ShippingFixedOptionTerm> page =
			shippingFixedOptionTermResource.
				getShippingFixedOptionIdShippingFixedOptionTermsPage(
					id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			ShippingFixedOptionTerm irrelevantShippingFixedOptionTerm =
				testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
					irrelevantId, randomIrrelevantShippingFixedOptionTerm());

			page =
				shippingFixedOptionTermResource.
					getShippingFixedOptionIdShippingFixedOptionTermsPage(
						irrelevantId, null, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantShippingFixedOptionTerm),
				(List<ShippingFixedOptionTerm>)page.getItems());
			assertValid(page);
		}

		ShippingFixedOptionTerm shippingFixedOptionTerm1 =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				id, randomShippingFixedOptionTerm());

		ShippingFixedOptionTerm shippingFixedOptionTerm2 =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				id, randomShippingFixedOptionTerm());

		page =
			shippingFixedOptionTermResource.
				getShippingFixedOptionIdShippingFixedOptionTermsPage(
					id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(shippingFixedOptionTerm1, shippingFixedOptionTerm2),
			(List<ShippingFixedOptionTerm>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_getId();

		ShippingFixedOptionTerm shippingFixedOptionTerm1 =
			randomShippingFixedOptionTerm();

		shippingFixedOptionTerm1 =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				id, shippingFixedOptionTerm1);

		for (EntityField entityField : entityFields) {
			Page<ShippingFixedOptionTerm> page =
				shippingFixedOptionTermResource.
					getShippingFixedOptionIdShippingFixedOptionTermsPage(
						id, null,
						getFilterString(
							entityField, "between", shippingFixedOptionTerm1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(shippingFixedOptionTerm1),
				(List<ShippingFixedOptionTerm>)page.getItems());
		}
	}

	@Test
	public void testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_getId();

		ShippingFixedOptionTerm shippingFixedOptionTerm1 =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				id, randomShippingFixedOptionTerm());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ShippingFixedOptionTerm shippingFixedOptionTerm2 =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				id, randomShippingFixedOptionTerm());

		for (EntityField entityField : entityFields) {
			Page<ShippingFixedOptionTerm> page =
				shippingFixedOptionTermResource.
					getShippingFixedOptionIdShippingFixedOptionTermsPage(
						id, null,
						getFilterString(
							entityField, "eq", shippingFixedOptionTerm1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(shippingFixedOptionTerm1),
				(List<ShippingFixedOptionTerm>)page.getItems());
		}
	}

	@Test
	public void testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_getId();

		ShippingFixedOptionTerm shippingFixedOptionTerm1 =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				id, randomShippingFixedOptionTerm());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ShippingFixedOptionTerm shippingFixedOptionTerm2 =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				id, randomShippingFixedOptionTerm());

		for (EntityField entityField : entityFields) {
			Page<ShippingFixedOptionTerm> page =
				shippingFixedOptionTermResource.
					getShippingFixedOptionIdShippingFixedOptionTermsPage(
						id, null,
						getFilterString(
							entityField, "eq", shippingFixedOptionTerm1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(shippingFixedOptionTerm1),
				(List<ShippingFixedOptionTerm>)page.getItems());
		}
	}

	@Test
	public void testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithPagination()
		throws Exception {

		Long id =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_getId();

		ShippingFixedOptionTerm shippingFixedOptionTerm1 =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				id, randomShippingFixedOptionTerm());

		ShippingFixedOptionTerm shippingFixedOptionTerm2 =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				id, randomShippingFixedOptionTerm());

		ShippingFixedOptionTerm shippingFixedOptionTerm3 =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				id, randomShippingFixedOptionTerm());

		Page<ShippingFixedOptionTerm> page1 =
			shippingFixedOptionTermResource.
				getShippingFixedOptionIdShippingFixedOptionTermsPage(
					id, null, null, Pagination.of(1, 2), null);

		List<ShippingFixedOptionTerm> shippingFixedOptionTerms1 =
			(List<ShippingFixedOptionTerm>)page1.getItems();

		Assert.assertEquals(
			shippingFixedOptionTerms1.toString(), 2,
			shippingFixedOptionTerms1.size());

		Page<ShippingFixedOptionTerm> page2 =
			shippingFixedOptionTermResource.
				getShippingFixedOptionIdShippingFixedOptionTermsPage(
					id, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<ShippingFixedOptionTerm> shippingFixedOptionTerms2 =
			(List<ShippingFixedOptionTerm>)page2.getItems();

		Assert.assertEquals(
			shippingFixedOptionTerms2.toString(), 1,
			shippingFixedOptionTerms2.size());

		Page<ShippingFixedOptionTerm> page3 =
			shippingFixedOptionTermResource.
				getShippingFixedOptionIdShippingFixedOptionTermsPage(
					id, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(
				shippingFixedOptionTerm1, shippingFixedOptionTerm2,
				shippingFixedOptionTerm3),
			(List<ShippingFixedOptionTerm>)page3.getItems());
	}

	@Test
	public void testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithSortDateTime()
		throws Exception {

		testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, shippingFixedOptionTerm1, shippingFixedOptionTerm2) ->{
				BeanTestUtil.setProperty(
					shippingFixedOptionTerm1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithSortDouble()
		throws Exception {

		testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, shippingFixedOptionTerm1, shippingFixedOptionTerm2) ->{
				BeanTestUtil.setProperty(
					shippingFixedOptionTerm1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					shippingFixedOptionTerm2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithSortInteger()
		throws Exception {

		testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, shippingFixedOptionTerm1, shippingFixedOptionTerm2) ->{
				BeanTestUtil.setProperty(
					shippingFixedOptionTerm1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					shippingFixedOptionTerm2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithSortString()
		throws Exception {

		testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithSort(
			EntityField.Type.STRING,
			(entityField, shippingFixedOptionTerm1, shippingFixedOptionTerm2) ->{
				Class<?> clazz = shippingFixedOptionTerm1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						shippingFixedOptionTerm1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						shippingFixedOptionTerm2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						shippingFixedOptionTerm1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						shippingFixedOptionTerm2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						shippingFixedOptionTerm1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						shippingFixedOptionTerm2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void
			testGetShippingFixedOptionIdShippingFixedOptionTermsPageWithSort(
				EntityField.Type type,
				UnsafeTriConsumer
					<EntityField, ShippingFixedOptionTerm,
					 ShippingFixedOptionTerm, Exception> unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_getId();

		ShippingFixedOptionTerm shippingFixedOptionTerm1 =
			randomShippingFixedOptionTerm();
		ShippingFixedOptionTerm shippingFixedOptionTerm2 =
			randomShippingFixedOptionTerm();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, shippingFixedOptionTerm1,
				shippingFixedOptionTerm2);
		}

		shippingFixedOptionTerm1 =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				id, shippingFixedOptionTerm1);

		shippingFixedOptionTerm2 =
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				id, shippingFixedOptionTerm2);

		for (EntityField entityField : entityFields) {
			Page<ShippingFixedOptionTerm> ascPage =
				shippingFixedOptionTermResource.
					getShippingFixedOptionIdShippingFixedOptionTermsPage(
						id, null, null, Pagination.of(1, 2),
						entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(
					shippingFixedOptionTerm1, shippingFixedOptionTerm2),
				(List<ShippingFixedOptionTerm>)ascPage.getItems());

			Page<ShippingFixedOptionTerm> descPage =
				shippingFixedOptionTermResource.
					getShippingFixedOptionIdShippingFixedOptionTermsPage(
						id, null, null, Pagination.of(1, 2),
						entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(
					shippingFixedOptionTerm2, shippingFixedOptionTerm1),
				(List<ShippingFixedOptionTerm>)descPage.getItems());
		}
	}

	protected ShippingFixedOptionTerm
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_addShippingFixedOptionTerm(
				Long id, ShippingFixedOptionTerm shippingFixedOptionTerm)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetShippingFixedOptionIdShippingFixedOptionTermsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostShippingFixedOptionIdShippingFixedOptionTerm()
		throws Exception {

		ShippingFixedOptionTerm randomShippingFixedOptionTerm =
			randomShippingFixedOptionTerm();

		ShippingFixedOptionTerm postShippingFixedOptionTerm =
			testPostShippingFixedOptionIdShippingFixedOptionTerm_addShippingFixedOptionTerm(
				randomShippingFixedOptionTerm);

		assertEquals(
			randomShippingFixedOptionTerm, postShippingFixedOptionTerm);
		assertValid(postShippingFixedOptionTerm);
	}

	protected ShippingFixedOptionTerm
			testPostShippingFixedOptionIdShippingFixedOptionTerm_addShippingFixedOptionTerm(
				ShippingFixedOptionTerm shippingFixedOptionTerm)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected void assertContains(
		ShippingFixedOptionTerm shippingFixedOptionTerm,
		List<ShippingFixedOptionTerm> shippingFixedOptionTerms) {

		boolean contains = false;

		for (ShippingFixedOptionTerm item : shippingFixedOptionTerms) {
			if (equals(shippingFixedOptionTerm, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			shippingFixedOptionTerms + " does not contain " +
				shippingFixedOptionTerm,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ShippingFixedOptionTerm shippingFixedOptionTerm1,
		ShippingFixedOptionTerm shippingFixedOptionTerm2) {

		Assert.assertTrue(
			shippingFixedOptionTerm1 + " does not equal " +
				shippingFixedOptionTerm2,
			equals(shippingFixedOptionTerm1, shippingFixedOptionTerm2));
	}

	protected void assertEquals(
		List<ShippingFixedOptionTerm> shippingFixedOptionTerms1,
		List<ShippingFixedOptionTerm> shippingFixedOptionTerms2) {

		Assert.assertEquals(
			shippingFixedOptionTerms1.size(), shippingFixedOptionTerms2.size());

		for (int i = 0; i < shippingFixedOptionTerms1.size(); i++) {
			ShippingFixedOptionTerm shippingFixedOptionTerm1 =
				shippingFixedOptionTerms1.get(i);
			ShippingFixedOptionTerm shippingFixedOptionTerm2 =
				shippingFixedOptionTerms2.get(i);

			assertEquals(shippingFixedOptionTerm1, shippingFixedOptionTerm2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ShippingFixedOptionTerm> shippingFixedOptionTerms1,
		List<ShippingFixedOptionTerm> shippingFixedOptionTerms2) {

		Assert.assertEquals(
			shippingFixedOptionTerms1.size(), shippingFixedOptionTerms2.size());

		for (ShippingFixedOptionTerm shippingFixedOptionTerm1 :
				shippingFixedOptionTerms1) {

			boolean contains = false;

			for (ShippingFixedOptionTerm shippingFixedOptionTerm2 :
					shippingFixedOptionTerms2) {

				if (equals(
						shippingFixedOptionTerm1, shippingFixedOptionTerm2)) {

					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				shippingFixedOptionTerms2 + " does not contain " +
					shippingFixedOptionTerm1,
				contains);
		}
	}

	protected void assertValid(ShippingFixedOptionTerm shippingFixedOptionTerm)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (shippingFixedOptionTerm.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"shippingFixedOptionId", additionalAssertFieldName)) {

				if (shippingFixedOptionTerm.getShippingFixedOptionId() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"shippingFixedOptionTermId", additionalAssertFieldName)) {

				if (shippingFixedOptionTerm.getShippingFixedOptionTermId() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("term", additionalAssertFieldName)) {
				if (shippingFixedOptionTerm.getTerm() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"termExternalReferenceCode", additionalAssertFieldName)) {

				if (shippingFixedOptionTerm.getTermExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("termId", additionalAssertFieldName)) {
				if (shippingFixedOptionTerm.getTermId() == null) {
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

	protected void assertValid(Page<ShippingFixedOptionTerm> page) {
		boolean valid = false;

		java.util.Collection<ShippingFixedOptionTerm> shippingFixedOptionTerms =
			page.getItems();

		int size = shippingFixedOptionTerms.size();

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
						ShippingFixedOptionTerm.class)) {

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
		ShippingFixedOptionTerm shippingFixedOptionTerm1,
		ShippingFixedOptionTerm shippingFixedOptionTerm2) {

		if (shippingFixedOptionTerm1 == shippingFixedOptionTerm2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)shippingFixedOptionTerm1.getActions(),
						(Map)shippingFixedOptionTerm2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"shippingFixedOptionId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						shippingFixedOptionTerm1.getShippingFixedOptionId(),
						shippingFixedOptionTerm2.getShippingFixedOptionId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"shippingFixedOptionTermId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						shippingFixedOptionTerm1.getShippingFixedOptionTermId(),
						shippingFixedOptionTerm2.
							getShippingFixedOptionTermId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("term", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingFixedOptionTerm1.getTerm(),
						shippingFixedOptionTerm2.getTerm())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"termExternalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						shippingFixedOptionTerm1.getTermExternalReferenceCode(),
						shippingFixedOptionTerm2.
							getTermExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("termId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingFixedOptionTerm1.getTermId(),
						shippingFixedOptionTerm2.getTermId())) {

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

		if (!(_shippingFixedOptionTermResource instanceof
				EntityModelResource)) {

			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_shippingFixedOptionTermResource;

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
		ShippingFixedOptionTerm shippingFixedOptionTerm) {

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

		if (entityFieldName.equals("shippingFixedOptionId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("shippingFixedOptionTermId")) {
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
					shippingFixedOptionTerm.getTermExternalReferenceCode()));
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

	protected ShippingFixedOptionTerm randomShippingFixedOptionTerm()
		throws Exception {

		return new ShippingFixedOptionTerm() {
			{
				shippingFixedOptionId = RandomTestUtil.randomLong();
				shippingFixedOptionTermId = RandomTestUtil.randomLong();
				termExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				termId = RandomTestUtil.randomLong();
			}
		};
	}

	protected ShippingFixedOptionTerm randomIrrelevantShippingFixedOptionTerm()
		throws Exception {

		ShippingFixedOptionTerm randomIrrelevantShippingFixedOptionTerm =
			randomShippingFixedOptionTerm();

		return randomIrrelevantShippingFixedOptionTerm;
	}

	protected ShippingFixedOptionTerm randomPatchShippingFixedOptionTerm()
		throws Exception {

		return randomShippingFixedOptionTerm();
	}

	protected ShippingFixedOptionTermResource shippingFixedOptionTermResource;
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
			BaseShippingFixedOptionTermResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.channel.resource.v1_0.
		ShippingFixedOptionTermResource _shippingFixedOptionTermResource;

}