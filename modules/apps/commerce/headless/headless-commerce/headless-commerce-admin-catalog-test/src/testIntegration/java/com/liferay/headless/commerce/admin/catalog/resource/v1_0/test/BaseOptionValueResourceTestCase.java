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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.OptionValue;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.OptionValueResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.OptionValueSerDes;
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
public abstract class BaseOptionValueResourceTestCase {

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

		_optionValueResource.setContextCompany(testCompany);

		OptionValueResource.Builder builder = OptionValueResource.builder();

		optionValueResource = builder.authentication(
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

		OptionValue optionValue1 = randomOptionValue();

		String json = objectMapper.writeValueAsString(optionValue1);

		OptionValue optionValue2 = OptionValueSerDes.toDTO(json);

		Assert.assertTrue(equals(optionValue1, optionValue2));
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

		OptionValue optionValue = randomOptionValue();

		String json1 = objectMapper.writeValueAsString(optionValue);
		String json2 = OptionValueSerDes.toJSON(optionValue);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		OptionValue optionValue = randomOptionValue();

		optionValue.setExternalReferenceCode(regex);
		optionValue.setKey(regex);

		String json = OptionValueSerDes.toJSON(optionValue);

		Assert.assertFalse(json.contains(regex));

		optionValue = OptionValueSerDes.toDTO(json);

		Assert.assertEquals(regex, optionValue.getExternalReferenceCode());
		Assert.assertEquals(regex, optionValue.getKey());
	}

	@Test
	public void testDeleteOptionValueByExternalReferenceCode()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		OptionValue optionValue =
			testDeleteOptionValueByExternalReferenceCode_addOptionValue();

		assertHttpResponseStatusCode(
			204,
			optionValueResource.
				deleteOptionValueByExternalReferenceCodeHttpResponse(
					optionValue.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			optionValueResource.
				getOptionValueByExternalReferenceCodeHttpResponse(
					optionValue.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			optionValueResource.
				getOptionValueByExternalReferenceCodeHttpResponse(
					optionValue.getExternalReferenceCode()));
	}

	protected OptionValue
			testDeleteOptionValueByExternalReferenceCode_addOptionValue()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetOptionValueByExternalReferenceCode() throws Exception {
		OptionValue postOptionValue =
			testGetOptionValueByExternalReferenceCode_addOptionValue();

		OptionValue getOptionValue =
			optionValueResource.getOptionValueByExternalReferenceCode(
				postOptionValue.getExternalReferenceCode());

		assertEquals(postOptionValue, getOptionValue);
		assertValid(getOptionValue);
	}

	protected OptionValue
			testGetOptionValueByExternalReferenceCode_addOptionValue()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetOptionValueByExternalReferenceCode()
		throws Exception {

		OptionValue optionValue =
			testGraphQLGetOptionValueByExternalReferenceCode_addOptionValue();

		Assert.assertTrue(
			equals(
				optionValue,
				OptionValueSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"optionValueByExternalReferenceCode",
								new HashMap<String, Object>() {
									{
										put(
											"externalReferenceCode",
											"\"" +
												optionValue.
													getExternalReferenceCode() +
														"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/optionValueByExternalReferenceCode"))));
	}

	@Test
	public void testGraphQLGetOptionValueByExternalReferenceCodeNotFound()
		throws Exception {

		String irrelevantExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"optionValueByExternalReferenceCode",
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

	protected OptionValue
			testGraphQLGetOptionValueByExternalReferenceCode_addOptionValue()
		throws Exception {

		return testGraphQLOptionValue_addOptionValue();
	}

	@Test
	public void testPatchOptionValueByExternalReferenceCode() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testDeleteOptionValue() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		OptionValue optionValue = testDeleteOptionValue_addOptionValue();

		assertHttpResponseStatusCode(
			204,
			optionValueResource.deleteOptionValueHttpResponse(
				optionValue.getId()));

		assertHttpResponseStatusCode(
			404,
			optionValueResource.getOptionValueHttpResponse(
				optionValue.getId()));

		assertHttpResponseStatusCode(
			404,
			optionValueResource.getOptionValueHttpResponse(
				optionValue.getId()));
	}

	protected OptionValue testDeleteOptionValue_addOptionValue()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteOptionValue() throws Exception {
		OptionValue optionValue = testGraphQLDeleteOptionValue_addOptionValue();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteOptionValue",
						new HashMap<String, Object>() {
							{
								put("id", optionValue.getId());
							}
						})),
				"JSONObject/data", "Object/deleteOptionValue"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"optionValue",
					new HashMap<String, Object>() {
						{
							put("id", optionValue.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected OptionValue testGraphQLDeleteOptionValue_addOptionValue()
		throws Exception {

		return testGraphQLOptionValue_addOptionValue();
	}

	@Test
	public void testGetOptionValue() throws Exception {
		OptionValue postOptionValue = testGetOptionValue_addOptionValue();

		OptionValue getOptionValue = optionValueResource.getOptionValue(
			postOptionValue.getId());

		assertEquals(postOptionValue, getOptionValue);
		assertValid(getOptionValue);
	}

	protected OptionValue testGetOptionValue_addOptionValue() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetOptionValue() throws Exception {
		OptionValue optionValue = testGraphQLGetOptionValue_addOptionValue();

		Assert.assertTrue(
			equals(
				optionValue,
				OptionValueSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"optionValue",
								new HashMap<String, Object>() {
									{
										put("id", optionValue.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/optionValue"))));
	}

	@Test
	public void testGraphQLGetOptionValueNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"optionValue",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected OptionValue testGraphQLGetOptionValue_addOptionValue()
		throws Exception {

		return testGraphQLOptionValue_addOptionValue();
	}

	@Test
	public void testPatchOptionValue() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetOptionByExternalReferenceCodeOptionValuesPage()
		throws Exception {

		String externalReferenceCode =
			testGetOptionByExternalReferenceCodeOptionValuesPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetOptionByExternalReferenceCodeOptionValuesPage_getIrrelevantExternalReferenceCode();

		Page<OptionValue> page =
			optionValueResource.
				getOptionByExternalReferenceCodeOptionValuesPage(
					externalReferenceCode, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			OptionValue irrelevantOptionValue =
				testGetOptionByExternalReferenceCodeOptionValuesPage_addOptionValue(
					irrelevantExternalReferenceCode,
					randomIrrelevantOptionValue());

			page =
				optionValueResource.
					getOptionByExternalReferenceCodeOptionValuesPage(
						irrelevantExternalReferenceCode, null,
						Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantOptionValue),
				(List<OptionValue>)page.getItems());
			assertValid(page);
		}

		OptionValue optionValue1 =
			testGetOptionByExternalReferenceCodeOptionValuesPage_addOptionValue(
				externalReferenceCode, randomOptionValue());

		OptionValue optionValue2 =
			testGetOptionByExternalReferenceCodeOptionValuesPage_addOptionValue(
				externalReferenceCode, randomOptionValue());

		page =
			optionValueResource.
				getOptionByExternalReferenceCodeOptionValuesPage(
					externalReferenceCode, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(optionValue1, optionValue2),
			(List<OptionValue>)page.getItems());
		assertValid(page);

		optionValueResource.deleteOptionValue(optionValue1.getId());

		optionValueResource.deleteOptionValue(optionValue2.getId());
	}

	@Test
	public void testGetOptionByExternalReferenceCodeOptionValuesPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetOptionByExternalReferenceCodeOptionValuesPage_getExternalReferenceCode();

		OptionValue optionValue1 =
			testGetOptionByExternalReferenceCodeOptionValuesPage_addOptionValue(
				externalReferenceCode, randomOptionValue());

		OptionValue optionValue2 =
			testGetOptionByExternalReferenceCodeOptionValuesPage_addOptionValue(
				externalReferenceCode, randomOptionValue());

		OptionValue optionValue3 =
			testGetOptionByExternalReferenceCodeOptionValuesPage_addOptionValue(
				externalReferenceCode, randomOptionValue());

		Page<OptionValue> page1 =
			optionValueResource.
				getOptionByExternalReferenceCodeOptionValuesPage(
					externalReferenceCode, null, Pagination.of(1, 2), null);

		List<OptionValue> optionValues1 = (List<OptionValue>)page1.getItems();

		Assert.assertEquals(optionValues1.toString(), 2, optionValues1.size());

		Page<OptionValue> page2 =
			optionValueResource.
				getOptionByExternalReferenceCodeOptionValuesPage(
					externalReferenceCode, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<OptionValue> optionValues2 = (List<OptionValue>)page2.getItems();

		Assert.assertEquals(optionValues2.toString(), 1, optionValues2.size());

		Page<OptionValue> page3 =
			optionValueResource.
				getOptionByExternalReferenceCodeOptionValuesPage(
					externalReferenceCode, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(optionValue1, optionValue2, optionValue3),
			(List<OptionValue>)page3.getItems());
	}

	@Test
	public void testGetOptionByExternalReferenceCodeOptionValuesPageWithSortDateTime()
		throws Exception {

		testGetOptionByExternalReferenceCodeOptionValuesPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, optionValue1, optionValue2) -> {
				BeanTestUtil.setProperty(
					optionValue1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetOptionByExternalReferenceCodeOptionValuesPageWithSortDouble()
		throws Exception {

		testGetOptionByExternalReferenceCodeOptionValuesPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, optionValue1, optionValue2) -> {
				BeanTestUtil.setProperty(
					optionValue1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					optionValue2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetOptionByExternalReferenceCodeOptionValuesPageWithSortInteger()
		throws Exception {

		testGetOptionByExternalReferenceCodeOptionValuesPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, optionValue1, optionValue2) -> {
				BeanTestUtil.setProperty(
					optionValue1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					optionValue2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetOptionByExternalReferenceCodeOptionValuesPageWithSortString()
		throws Exception {

		testGetOptionByExternalReferenceCodeOptionValuesPageWithSort(
			EntityField.Type.STRING,
			(entityField, optionValue1, optionValue2) -> {
				Class<?> clazz = optionValue1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						optionValue1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						optionValue2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						optionValue1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						optionValue2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						optionValue1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						optionValue2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetOptionByExternalReferenceCodeOptionValuesPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, OptionValue, OptionValue, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		String externalReferenceCode =
			testGetOptionByExternalReferenceCodeOptionValuesPage_getExternalReferenceCode();

		OptionValue optionValue1 = randomOptionValue();
		OptionValue optionValue2 = randomOptionValue();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, optionValue1, optionValue2);
		}

		optionValue1 =
			testGetOptionByExternalReferenceCodeOptionValuesPage_addOptionValue(
				externalReferenceCode, optionValue1);

		optionValue2 =
			testGetOptionByExternalReferenceCodeOptionValuesPage_addOptionValue(
				externalReferenceCode, optionValue2);

		for (EntityField entityField : entityFields) {
			Page<OptionValue> ascPage =
				optionValueResource.
					getOptionByExternalReferenceCodeOptionValuesPage(
						externalReferenceCode, null, Pagination.of(1, 2),
						entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(optionValue1, optionValue2),
				(List<OptionValue>)ascPage.getItems());

			Page<OptionValue> descPage =
				optionValueResource.
					getOptionByExternalReferenceCodeOptionValuesPage(
						externalReferenceCode, null, Pagination.of(1, 2),
						entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(optionValue2, optionValue1),
				(List<OptionValue>)descPage.getItems());
		}
	}

	protected OptionValue
			testGetOptionByExternalReferenceCodeOptionValuesPage_addOptionValue(
				String externalReferenceCode, OptionValue optionValue)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetOptionByExternalReferenceCodeOptionValuesPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetOptionByExternalReferenceCodeOptionValuesPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostOptionByExternalReferenceCodeOptionValue()
		throws Exception {

		OptionValue randomOptionValue = randomOptionValue();

		OptionValue postOptionValue =
			testPostOptionByExternalReferenceCodeOptionValue_addOptionValue(
				randomOptionValue);

		assertEquals(randomOptionValue, postOptionValue);
		assertValid(postOptionValue);
	}

	protected OptionValue
			testPostOptionByExternalReferenceCodeOptionValue_addOptionValue(
				OptionValue optionValue)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetOptionIdOptionValuesPage() throws Exception {
		Long id = testGetOptionIdOptionValuesPage_getId();
		Long irrelevantId = testGetOptionIdOptionValuesPage_getIrrelevantId();

		Page<OptionValue> page =
			optionValueResource.getOptionIdOptionValuesPage(
				id, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			OptionValue irrelevantOptionValue =
				testGetOptionIdOptionValuesPage_addOptionValue(
					irrelevantId, randomIrrelevantOptionValue());

			page = optionValueResource.getOptionIdOptionValuesPage(
				irrelevantId, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantOptionValue),
				(List<OptionValue>)page.getItems());
			assertValid(page);
		}

		OptionValue optionValue1 =
			testGetOptionIdOptionValuesPage_addOptionValue(
				id, randomOptionValue());

		OptionValue optionValue2 =
			testGetOptionIdOptionValuesPage_addOptionValue(
				id, randomOptionValue());

		page = optionValueResource.getOptionIdOptionValuesPage(
			id, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(optionValue1, optionValue2),
			(List<OptionValue>)page.getItems());
		assertValid(page);

		optionValueResource.deleteOptionValue(optionValue1.getId());

		optionValueResource.deleteOptionValue(optionValue2.getId());
	}

	@Test
	public void testGetOptionIdOptionValuesPageWithPagination()
		throws Exception {

		Long id = testGetOptionIdOptionValuesPage_getId();

		OptionValue optionValue1 =
			testGetOptionIdOptionValuesPage_addOptionValue(
				id, randomOptionValue());

		OptionValue optionValue2 =
			testGetOptionIdOptionValuesPage_addOptionValue(
				id, randomOptionValue());

		OptionValue optionValue3 =
			testGetOptionIdOptionValuesPage_addOptionValue(
				id, randomOptionValue());

		Page<OptionValue> page1 =
			optionValueResource.getOptionIdOptionValuesPage(
				id, null, Pagination.of(1, 2), null);

		List<OptionValue> optionValues1 = (List<OptionValue>)page1.getItems();

		Assert.assertEquals(optionValues1.toString(), 2, optionValues1.size());

		Page<OptionValue> page2 =
			optionValueResource.getOptionIdOptionValuesPage(
				id, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<OptionValue> optionValues2 = (List<OptionValue>)page2.getItems();

		Assert.assertEquals(optionValues2.toString(), 1, optionValues2.size());

		Page<OptionValue> page3 =
			optionValueResource.getOptionIdOptionValuesPage(
				id, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(optionValue1, optionValue2, optionValue3),
			(List<OptionValue>)page3.getItems());
	}

	@Test
	public void testGetOptionIdOptionValuesPageWithSortDateTime()
		throws Exception {

		testGetOptionIdOptionValuesPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, optionValue1, optionValue2) -> {
				BeanTestUtil.setProperty(
					optionValue1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetOptionIdOptionValuesPageWithSortDouble()
		throws Exception {

		testGetOptionIdOptionValuesPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, optionValue1, optionValue2) -> {
				BeanTestUtil.setProperty(
					optionValue1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					optionValue2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetOptionIdOptionValuesPageWithSortInteger()
		throws Exception {

		testGetOptionIdOptionValuesPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, optionValue1, optionValue2) -> {
				BeanTestUtil.setProperty(
					optionValue1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					optionValue2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetOptionIdOptionValuesPageWithSortString()
		throws Exception {

		testGetOptionIdOptionValuesPageWithSort(
			EntityField.Type.STRING,
			(entityField, optionValue1, optionValue2) -> {
				Class<?> clazz = optionValue1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						optionValue1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						optionValue2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						optionValue1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						optionValue2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						optionValue1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						optionValue2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetOptionIdOptionValuesPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, OptionValue, OptionValue, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetOptionIdOptionValuesPage_getId();

		OptionValue optionValue1 = randomOptionValue();
		OptionValue optionValue2 = randomOptionValue();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, optionValue1, optionValue2);
		}

		optionValue1 = testGetOptionIdOptionValuesPage_addOptionValue(
			id, optionValue1);

		optionValue2 = testGetOptionIdOptionValuesPage_addOptionValue(
			id, optionValue2);

		for (EntityField entityField : entityFields) {
			Page<OptionValue> ascPage =
				optionValueResource.getOptionIdOptionValuesPage(
					id, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(optionValue1, optionValue2),
				(List<OptionValue>)ascPage.getItems());

			Page<OptionValue> descPage =
				optionValueResource.getOptionIdOptionValuesPage(
					id, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(optionValue2, optionValue1),
				(List<OptionValue>)descPage.getItems());
		}
	}

	protected OptionValue testGetOptionIdOptionValuesPage_addOptionValue(
			Long id, OptionValue optionValue)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetOptionIdOptionValuesPage_getId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetOptionIdOptionValuesPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostOptionIdOptionValue() throws Exception {
		OptionValue randomOptionValue = randomOptionValue();

		OptionValue postOptionValue =
			testPostOptionIdOptionValue_addOptionValue(randomOptionValue);

		assertEquals(randomOptionValue, postOptionValue);
		assertValid(postOptionValue);
	}

	protected OptionValue testPostOptionIdOptionValue_addOptionValue(
			OptionValue optionValue)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected OptionValue testGraphQLOptionValue_addOptionValue()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		OptionValue optionValue, List<OptionValue> optionValues) {

		boolean contains = false;

		for (OptionValue item : optionValues) {
			if (equals(optionValue, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			optionValues + " does not contain " + optionValue, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		OptionValue optionValue1, OptionValue optionValue2) {

		Assert.assertTrue(
			optionValue1 + " does not equal " + optionValue2,
			equals(optionValue1, optionValue2));
	}

	protected void assertEquals(
		List<OptionValue> optionValues1, List<OptionValue> optionValues2) {

		Assert.assertEquals(optionValues1.size(), optionValues2.size());

		for (int i = 0; i < optionValues1.size(); i++) {
			OptionValue optionValue1 = optionValues1.get(i);
			OptionValue optionValue2 = optionValues2.get(i);

			assertEquals(optionValue1, optionValue2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<OptionValue> optionValues1, List<OptionValue> optionValues2) {

		Assert.assertEquals(optionValues1.size(), optionValues2.size());

		for (OptionValue optionValue1 : optionValues1) {
			boolean contains = false;

			for (OptionValue optionValue2 : optionValues2) {
				if (equals(optionValue1, optionValue2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				optionValues2 + " does not contain " + optionValue1, contains);
		}
	}

	protected void assertValid(OptionValue optionValue) throws Exception {
		boolean valid = true;

		if (optionValue.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (optionValue.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (optionValue.getExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (optionValue.getKey() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (optionValue.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (optionValue.getPriority() == null) {
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

	protected void assertValid(Page<OptionValue> page) {
		boolean valid = false;

		java.util.Collection<OptionValue> optionValues = page.getItems();

		int size = optionValues.size();

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
						OptionValue.class)) {

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
		OptionValue optionValue1, OptionValue optionValue2) {

		if (optionValue1 == optionValue2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)optionValue1.getActions(),
						(Map)optionValue2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						optionValue1.getExternalReferenceCode(),
						optionValue2.getExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						optionValue1.getId(), optionValue2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						optionValue1.getKey(), optionValue2.getKey())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!equals(
						(Map)optionValue1.getName(),
						(Map)optionValue2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						optionValue1.getPriority(),
						optionValue2.getPriority())) {

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

		if (!(_optionValueResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_optionValueResource;

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
		EntityField entityField, String operator, OptionValue optionValue) {

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

		if (entityFieldName.equals("externalReferenceCode")) {
			sb.append("'");
			sb.append(String.valueOf(optionValue.getExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("key")) {
			sb.append("'");
			sb.append(String.valueOf(optionValue.getKey()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("name")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("priority")) {
			sb.append(String.valueOf(optionValue.getPriority()));

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

	protected OptionValue randomOptionValue() throws Exception {
		return new OptionValue() {
			{
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				key = StringUtil.toLowerCase(RandomTestUtil.randomString());
				priority = RandomTestUtil.randomDouble();
			}
		};
	}

	protected OptionValue randomIrrelevantOptionValue() throws Exception {
		OptionValue randomIrrelevantOptionValue = randomOptionValue();

		return randomIrrelevantOptionValue;
	}

	protected OptionValue randomPatchOptionValue() throws Exception {
		return randomOptionValue();
	}

	protected OptionValueResource optionValueResource;
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
		LogFactoryUtil.getLog(BaseOptionValueResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		OptionValueResource _optionValueResource;

}