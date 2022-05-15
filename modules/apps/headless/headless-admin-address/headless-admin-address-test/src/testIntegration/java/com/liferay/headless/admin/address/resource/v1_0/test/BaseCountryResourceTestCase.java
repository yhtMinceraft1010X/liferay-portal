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

package com.liferay.headless.admin.address.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.admin.address.client.dto.v1_0.Country;
import com.liferay.headless.admin.address.client.http.HttpInvoker;
import com.liferay.headless.admin.address.client.pagination.Page;
import com.liferay.headless.admin.address.client.pagination.Pagination;
import com.liferay.headless.admin.address.client.resource.v1_0.CountryResource;
import com.liferay.headless.admin.address.client.serdes.v1_0.CountrySerDes;
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
 * @author Drew Brokke
 * @generated
 */
@Generated("")
public abstract class BaseCountryResourceTestCase {

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

		_countryResource.setContextCompany(testCompany);

		CountryResource.Builder builder = CountryResource.builder();

		countryResource = builder.authentication(
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

		Country country1 = randomCountry();

		String json = objectMapper.writeValueAsString(country1);

		Country country2 = CountrySerDes.toDTO(json);

		Assert.assertTrue(equals(country1, country2));
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

		Country country = randomCountry();

		String json1 = objectMapper.writeValueAsString(country);
		String json2 = CountrySerDes.toJSON(country);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Country country = randomCountry();

		country.setA2(regex);
		country.setA3(regex);
		country.setName(regex);

		String json = CountrySerDes.toJSON(country);

		Assert.assertFalse(json.contains(regex));

		country = CountrySerDes.toDTO(json);

		Assert.assertEquals(regex, country.getA2());
		Assert.assertEquals(regex, country.getA3());
		Assert.assertEquals(regex, country.getName());
	}

	@Test
	public void testGetCountriesPage() throws Exception {
		Page<Country> page = countryResource.getCountriesPage(
			null, null, Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		Country country1 = testGetCountriesPage_addCountry(randomCountry());

		Country country2 = testGetCountriesPage_addCountry(randomCountry());

		page = countryResource.getCountriesPage(
			null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(country1, (List<Country>)page.getItems());
		assertContains(country2, (List<Country>)page.getItems());
		assertValid(page);

		countryResource.deleteCountry(country1.getId());

		countryResource.deleteCountry(country2.getId());
	}

	@Test
	public void testGetCountriesPageWithPagination() throws Exception {
		Page<Country> totalPage = countryResource.getCountriesPage(
			null, null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		Country country1 = testGetCountriesPage_addCountry(randomCountry());

		Country country2 = testGetCountriesPage_addCountry(randomCountry());

		Country country3 = testGetCountriesPage_addCountry(randomCountry());

		Page<Country> page1 = countryResource.getCountriesPage(
			null, null, Pagination.of(1, totalCount + 2), null);

		List<Country> countries1 = (List<Country>)page1.getItems();

		Assert.assertEquals(
			countries1.toString(), totalCount + 2, countries1.size());

		Page<Country> page2 = countryResource.getCountriesPage(
			null, null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<Country> countries2 = (List<Country>)page2.getItems();

		Assert.assertEquals(countries2.toString(), 1, countries2.size());

		Page<Country> page3 = countryResource.getCountriesPage(
			null, null, Pagination.of(1, totalCount + 3), null);

		assertContains(country1, (List<Country>)page3.getItems());
		assertContains(country2, (List<Country>)page3.getItems());
		assertContains(country3, (List<Country>)page3.getItems());
	}

	@Test
	public void testGetCountriesPageWithSortDateTime() throws Exception {
		testGetCountriesPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, country1, country2) -> {
				BeanTestUtil.setProperty(
					country1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetCountriesPageWithSortDouble() throws Exception {
		testGetCountriesPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, country1, country2) -> {
				BeanTestUtil.setProperty(country1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(country2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetCountriesPageWithSortInteger() throws Exception {
		testGetCountriesPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, country1, country2) -> {
				BeanTestUtil.setProperty(country1, entityField.getName(), 0);
				BeanTestUtil.setProperty(country2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetCountriesPageWithSortString() throws Exception {
		testGetCountriesPageWithSort(
			EntityField.Type.STRING,
			(entityField, country1, country2) -> {
				Class<?> clazz = country1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						country1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						country2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						country1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						country2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						country1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						country2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetCountriesPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Country, Country, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Country country1 = randomCountry();
		Country country2 = randomCountry();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, country1, country2);
		}

		country1 = testGetCountriesPage_addCountry(country1);

		country2 = testGetCountriesPage_addCountry(country2);

		for (EntityField entityField : entityFields) {
			Page<Country> ascPage = countryResource.getCountriesPage(
				null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(country1, country2),
				(List<Country>)ascPage.getItems());

			Page<Country> descPage = countryResource.getCountriesPage(
				null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(country2, country1),
				(List<Country>)descPage.getItems());
		}
	}

	protected Country testGetCountriesPage_addCountry(Country country)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetCountriesPage() throws Exception {
		GraphQLField graphQLField = new GraphQLField(
			"countries",
			new HashMap<String, Object>() {
				{
					put("page", 1);
					put("pageSize", 10);
				}
			},
			new GraphQLField("items", getGraphQLFields()),
			new GraphQLField("page"), new GraphQLField("totalCount"));

		JSONObject countriesJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/countries");

		long totalCount = countriesJSONObject.getLong("totalCount");

		Country country1 = testGraphQLGetCountriesPage_addCountry();
		Country country2 = testGraphQLGetCountriesPage_addCountry();

		countriesJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/countries");

		Assert.assertEquals(
			totalCount + 2, countriesJSONObject.getLong("totalCount"));

		assertContains(
			country1,
			Arrays.asList(
				CountrySerDes.toDTOs(countriesJSONObject.getString("items"))));
		assertContains(
			country2,
			Arrays.asList(
				CountrySerDes.toDTOs(countriesJSONObject.getString("items"))));
	}

	protected Country testGraphQLGetCountriesPage_addCountry()
		throws Exception {

		return testGraphQLCountry_addCountry();
	}

	@Test
	public void testPostCountry() throws Exception {
		Country randomCountry = randomCountry();

		Country postCountry = testPostCountry_addCountry(randomCountry);

		assertEquals(randomCountry, postCountry);
		assertValid(postCountry);
	}

	protected Country testPostCountry_addCountry(Country country)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetCountryByA2() throws Exception {
		Country postCountry = testGetCountryByA2_addCountry();

		Country getCountry = countryResource.getCountryByA2(
			postCountry.getA2());

		assertEquals(postCountry, getCountry);
		assertValid(getCountry);
	}

	protected Country testGetCountryByA2_addCountry() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetCountryByA2() throws Exception {
		Country country = testGraphQLGetCountryByA2_addCountry();

		Assert.assertTrue(
			equals(
				country,
				CountrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"countryByA2",
								new HashMap<String, Object>() {
									{
										put(
											"a2",
											"\"" + country.getA2() + "\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/countryByA2"))));
	}

	@Test
	public void testGraphQLGetCountryByA2NotFound() throws Exception {
		String irrelevantA2 = "\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"countryByA2",
						new HashMap<String, Object>() {
							{
								put("a2", irrelevantA2);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Country testGraphQLGetCountryByA2_addCountry() throws Exception {
		return testGraphQLCountry_addCountry();
	}

	@Test
	public void testGetCountryByA3() throws Exception {
		Country postCountry = testGetCountryByA3_addCountry();

		Country getCountry = countryResource.getCountryByA3(
			postCountry.getA3());

		assertEquals(postCountry, getCountry);
		assertValid(getCountry);
	}

	protected Country testGetCountryByA3_addCountry() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetCountryByA3() throws Exception {
		Country country = testGraphQLGetCountryByA3_addCountry();

		Assert.assertTrue(
			equals(
				country,
				CountrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"countryByA3",
								new HashMap<String, Object>() {
									{
										put(
											"a3",
											"\"" + country.getA3() + "\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/countryByA3"))));
	}

	@Test
	public void testGraphQLGetCountryByA3NotFound() throws Exception {
		String irrelevantA3 = "\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"countryByA3",
						new HashMap<String, Object>() {
							{
								put("a3", irrelevantA3);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Country testGraphQLGetCountryByA3_addCountry() throws Exception {
		return testGraphQLCountry_addCountry();
	}

	@Test
	public void testGetCountryByName() throws Exception {
		Country postCountry = testGetCountryByName_addCountry();

		Country getCountry = countryResource.getCountryByName(
			postCountry.getName());

		assertEquals(postCountry, getCountry);
		assertValid(getCountry);
	}

	protected Country testGetCountryByName_addCountry() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetCountryByName() throws Exception {
		Country country = testGraphQLGetCountryByName_addCountry();

		Assert.assertTrue(
			equals(
				country,
				CountrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"countryByName",
								new HashMap<String, Object>() {
									{
										put(
											"name",
											"\"" + country.getName() + "\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/countryByName"))));
	}

	@Test
	public void testGraphQLGetCountryByNameNotFound() throws Exception {
		String irrelevantName = "\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"countryByName",
						new HashMap<String, Object>() {
							{
								put("name", irrelevantName);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Country testGraphQLGetCountryByName_addCountry()
		throws Exception {

		return testGraphQLCountry_addCountry();
	}

	@Test
	public void testGetCountryByNumber() throws Exception {
		Country postCountry = testGetCountryByNumber_addCountry();

		Country getCountry = countryResource.getCountryByNumber(
			postCountry.getNumber());

		assertEquals(postCountry, getCountry);
		assertValid(getCountry);
	}

	protected Country testGetCountryByNumber_addCountry() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetCountryByNumber() throws Exception {
		Country country = testGraphQLGetCountryByNumber_addCountry();

		Assert.assertTrue(
			equals(
				country,
				CountrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"countryByNumber",
								new HashMap<String, Object>() {
									{
										put("number", country.getNumber());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/countryByNumber"))));
	}

	@Test
	public void testGraphQLGetCountryByNumberNotFound() throws Exception {
		Integer irrelevantNumber = RandomTestUtil.randomInt();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"countryByNumber",
						new HashMap<String, Object>() {
							{
								put("number", irrelevantNumber);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Country testGraphQLGetCountryByNumber_addCountry()
		throws Exception {

		return testGraphQLCountry_addCountry();
	}

	@Test
	public void testDeleteCountry() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		Country country = testDeleteCountry_addCountry();

		assertHttpResponseStatusCode(
			204, countryResource.deleteCountryHttpResponse(country.getId()));

		assertHttpResponseStatusCode(
			404, countryResource.getCountryHttpResponse(country.getId()));

		assertHttpResponseStatusCode(
			404, countryResource.getCountryHttpResponse(0L));
	}

	protected Country testDeleteCountry_addCountry() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteCountry() throws Exception {
		Country country = testGraphQLDeleteCountry_addCountry();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteCountry",
						new HashMap<String, Object>() {
							{
								put("countryId", country.getId());
							}
						})),
				"JSONObject/data", "Object/deleteCountry"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"country",
					new HashMap<String, Object>() {
						{
							put("countryId", country.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected Country testGraphQLDeleteCountry_addCountry() throws Exception {
		return testGraphQLCountry_addCountry();
	}

	@Test
	public void testGetCountry() throws Exception {
		Country postCountry = testGetCountry_addCountry();

		Country getCountry = countryResource.getCountry(postCountry.getId());

		assertEquals(postCountry, getCountry);
		assertValid(getCountry);
	}

	protected Country testGetCountry_addCountry() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetCountry() throws Exception {
		Country country = testGraphQLGetCountry_addCountry();

		Assert.assertTrue(
			equals(
				country,
				CountrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"country",
								new HashMap<String, Object>() {
									{
										put("countryId", country.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/country"))));
	}

	@Test
	public void testGraphQLGetCountryNotFound() throws Exception {
		Long irrelevantCountryId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"country",
						new HashMap<String, Object>() {
							{
								put("countryId", irrelevantCountryId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Country testGraphQLGetCountry_addCountry() throws Exception {
		return testGraphQLCountry_addCountry();
	}

	@Test
	public void testPatchCountry() throws Exception {
		Country postCountry = testPatchCountry_addCountry();

		Country randomPatchCountry = randomPatchCountry();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Country patchCountry = countryResource.patchCountry(
			postCountry.getId(), randomPatchCountry);

		Country expectedPatchCountry = postCountry.clone();

		BeanTestUtil.copyProperties(randomPatchCountry, expectedPatchCountry);

		Country getCountry = countryResource.getCountry(patchCountry.getId());

		assertEquals(expectedPatchCountry, getCountry);
		assertValid(getCountry);
	}

	protected Country testPatchCountry_addCountry() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPutCountry() throws Exception {
		Country postCountry = testPutCountry_addCountry();

		Country randomCountry = randomCountry();

		Country putCountry = countryResource.putCountry(
			postCountry.getId(), randomCountry);

		assertEquals(randomCountry, putCountry);
		assertValid(putCountry);

		Country getCountry = countryResource.getCountry(putCountry.getId());

		assertEquals(randomCountry, getCountry);
		assertValid(getCountry);
	}

	protected Country testPutCountry_addCountry() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Country testGraphQLCountry_addCountry() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(Country country, List<Country> countries) {
		boolean contains = false;

		for (Country item : countries) {
			if (equals(country, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(countries + " does not contain " + country, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Country country1, Country country2) {
		Assert.assertTrue(
			country1 + " does not equal " + country2,
			equals(country1, country2));
	}

	protected void assertEquals(
		List<Country> countries1, List<Country> countries2) {

		Assert.assertEquals(countries1.size(), countries2.size());

		for (int i = 0; i < countries1.size(); i++) {
			Country country1 = countries1.get(i);
			Country country2 = countries2.get(i);

			assertEquals(country1, country2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<Country> countries1, List<Country> countries2) {

		Assert.assertEquals(countries1.size(), countries2.size());

		for (Country country1 : countries1) {
			boolean contains = false;

			for (Country country2 : countries2) {
				if (equals(country1, country2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				countries2 + " does not contain " + country1, contains);
		}
	}

	protected void assertValid(Country country) throws Exception {
		boolean valid = true;

		if (country.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("a2", additionalAssertFieldName)) {
				if (country.getA2() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("a3", additionalAssertFieldName)) {
				if (country.getA3() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("active", additionalAssertFieldName)) {
				if (country.getActive() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("billingAllowed", additionalAssertFieldName)) {
				if (country.getBillingAllowed() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"groupFilterEnabled", additionalAssertFieldName)) {

				if (country.getGroupFilterEnabled() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("idd", additionalAssertFieldName)) {
				if (country.getIdd() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (country.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("number", additionalAssertFieldName)) {
				if (country.getNumber() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("position", additionalAssertFieldName)) {
				if (country.getPosition() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("regions", additionalAssertFieldName)) {
				if (country.getRegions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("shippingAllowed", additionalAssertFieldName)) {
				if (country.getShippingAllowed() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("subjectToVAT", additionalAssertFieldName)) {
				if (country.getSubjectToVAT() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("title_i18n", additionalAssertFieldName)) {
				if (country.getTitle_i18n() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("zipRequired", additionalAssertFieldName)) {
				if (country.getZipRequired() == null) {
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

	protected void assertValid(Page<Country> page) {
		boolean valid = false;

		java.util.Collection<Country> countries = page.getItems();

		int size = countries.size();

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
					com.liferay.headless.admin.address.dto.v1_0.Country.
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

	protected boolean equals(Country country1, Country country2) {
		if (country1 == country2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("a2", additionalAssertFieldName)) {
				if (!Objects.deepEquals(country1.getA2(), country2.getA2())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("a3", additionalAssertFieldName)) {
				if (!Objects.deepEquals(country1.getA3(), country2.getA3())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("active", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						country1.getActive(), country2.getActive())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("billingAllowed", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						country1.getBillingAllowed(),
						country2.getBillingAllowed())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"groupFilterEnabled", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						country1.getGroupFilterEnabled(),
						country2.getGroupFilterEnabled())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(country1.getId(), country2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("idd", additionalAssertFieldName)) {
				if (!Objects.deepEquals(country1.getIdd(), country2.getIdd())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						country1.getName(), country2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("number", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						country1.getNumber(), country2.getNumber())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("position", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						country1.getPosition(), country2.getPosition())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("regions", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						country1.getRegions(), country2.getRegions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("shippingAllowed", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						country1.getShippingAllowed(),
						country2.getShippingAllowed())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("subjectToVAT", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						country1.getSubjectToVAT(),
						country2.getSubjectToVAT())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("title_i18n", additionalAssertFieldName)) {
				if (!equals(
						(Map)country1.getTitle_i18n(),
						(Map)country2.getTitle_i18n())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("zipRequired", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						country1.getZipRequired(), country2.getZipRequired())) {

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

		if (!(_countryResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_countryResource;

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
		EntityField entityField, String operator, Country country) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("a2")) {
			sb.append("'");
			sb.append(String.valueOf(country.getA2()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("a3")) {
			sb.append("'");
			sb.append(String.valueOf(country.getA3()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("active")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("billingAllowed")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("groupFilterEnabled")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("idd")) {
			sb.append(String.valueOf(country.getIdd()));

			return sb.toString();
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(country.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("number")) {
			sb.append(String.valueOf(country.getNumber()));

			return sb.toString();
		}

		if (entityFieldName.equals("position")) {
			sb.append(String.valueOf(country.getPosition()));

			return sb.toString();
		}

		if (entityFieldName.equals("regions")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("shippingAllowed")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("subjectToVAT")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("title_i18n")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("zipRequired")) {
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

	protected Country randomCountry() throws Exception {
		return new Country() {
			{
				a2 = StringUtil.toLowerCase(RandomTestUtil.randomString());
				a3 = StringUtil.toLowerCase(RandomTestUtil.randomString());
				active = RandomTestUtil.randomBoolean();
				billingAllowed = RandomTestUtil.randomBoolean();
				groupFilterEnabled = RandomTestUtil.randomBoolean();
				id = RandomTestUtil.randomLong();
				idd = RandomTestUtil.randomInt();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				number = RandomTestUtil.randomInt();
				position = RandomTestUtil.randomDouble();
				shippingAllowed = RandomTestUtil.randomBoolean();
				subjectToVAT = RandomTestUtil.randomBoolean();
				zipRequired = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected Country randomIrrelevantCountry() throws Exception {
		Country randomIrrelevantCountry = randomCountry();

		return randomIrrelevantCountry;
	}

	protected Country randomPatchCountry() throws Exception {
		return randomCountry();
	}

	protected CountryResource countryResource;
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
		LogFactoryUtil.getLog(BaseCountryResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.admin.address.resource.v1_0.CountryResource
		_countryResource;

}