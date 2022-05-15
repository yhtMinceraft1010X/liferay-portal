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

import com.liferay.headless.admin.address.client.dto.v1_0.Region;
import com.liferay.headless.admin.address.client.http.HttpInvoker;
import com.liferay.headless.admin.address.client.pagination.Page;
import com.liferay.headless.admin.address.client.pagination.Pagination;
import com.liferay.headless.admin.address.client.resource.v1_0.RegionResource;
import com.liferay.headless.admin.address.client.serdes.v1_0.RegionSerDes;
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
public abstract class BaseRegionResourceTestCase {

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

		_regionResource.setContextCompany(testCompany);

		RegionResource.Builder builder = RegionResource.builder();

		regionResource = builder.authentication(
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

		Region region1 = randomRegion();

		String json = objectMapper.writeValueAsString(region1);

		Region region2 = RegionSerDes.toDTO(json);

		Assert.assertTrue(equals(region1, region2));
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

		Region region = randomRegion();

		String json1 = objectMapper.writeValueAsString(region);
		String json2 = RegionSerDes.toJSON(region);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Region region = randomRegion();

		region.setName(regex);
		region.setRegionCode(regex);

		String json = RegionSerDes.toJSON(region);

		Assert.assertFalse(json.contains(regex));

		region = RegionSerDes.toDTO(json);

		Assert.assertEquals(regex, region.getName());
		Assert.assertEquals(regex, region.getRegionCode());
	}

	@Test
	public void testGetCountryRegionsPage() throws Exception {
		Long countryId = testGetCountryRegionsPage_getCountryId();
		Long irrelevantCountryId =
			testGetCountryRegionsPage_getIrrelevantCountryId();

		Page<Region> page = regionResource.getCountryRegionsPage(
			countryId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantCountryId != null) {
			Region irrelevantRegion = testGetCountryRegionsPage_addRegion(
				irrelevantCountryId, randomIrrelevantRegion());

			page = regionResource.getCountryRegionsPage(
				irrelevantCountryId, null, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantRegion), (List<Region>)page.getItems());
			assertValid(page);
		}

		Region region1 = testGetCountryRegionsPage_addRegion(
			countryId, randomRegion());

		Region region2 = testGetCountryRegionsPage_addRegion(
			countryId, randomRegion());

		page = regionResource.getCountryRegionsPage(
			countryId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(region1, region2), (List<Region>)page.getItems());
		assertValid(page);

		regionResource.deleteRegion(region1.getId());

		regionResource.deleteRegion(region2.getId());
	}

	@Test
	public void testGetCountryRegionsPageWithPagination() throws Exception {
		Long countryId = testGetCountryRegionsPage_getCountryId();

		Region region1 = testGetCountryRegionsPage_addRegion(
			countryId, randomRegion());

		Region region2 = testGetCountryRegionsPage_addRegion(
			countryId, randomRegion());

		Region region3 = testGetCountryRegionsPage_addRegion(
			countryId, randomRegion());

		Page<Region> page1 = regionResource.getCountryRegionsPage(
			countryId, null, null, Pagination.of(1, 2), null);

		List<Region> regions1 = (List<Region>)page1.getItems();

		Assert.assertEquals(regions1.toString(), 2, regions1.size());

		Page<Region> page2 = regionResource.getCountryRegionsPage(
			countryId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<Region> regions2 = (List<Region>)page2.getItems();

		Assert.assertEquals(regions2.toString(), 1, regions2.size());

		Page<Region> page3 = regionResource.getCountryRegionsPage(
			countryId, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(region1, region2, region3),
			(List<Region>)page3.getItems());
	}

	@Test
	public void testGetCountryRegionsPageWithSortDateTime() throws Exception {
		testGetCountryRegionsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, region1, region2) -> {
				BeanTestUtil.setProperty(
					region1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetCountryRegionsPageWithSortDouble() throws Exception {
		testGetCountryRegionsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, region1, region2) -> {
				BeanTestUtil.setProperty(region1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(region2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetCountryRegionsPageWithSortInteger() throws Exception {
		testGetCountryRegionsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, region1, region2) -> {
				BeanTestUtil.setProperty(region1, entityField.getName(), 0);
				BeanTestUtil.setProperty(region2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetCountryRegionsPageWithSortString() throws Exception {
		testGetCountryRegionsPageWithSort(
			EntityField.Type.STRING,
			(entityField, region1, region2) -> {
				Class<?> clazz = region1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						region1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						region2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						region1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						region2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						region1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						region2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetCountryRegionsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Region, Region, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long countryId = testGetCountryRegionsPage_getCountryId();

		Region region1 = randomRegion();
		Region region2 = randomRegion();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, region1, region2);
		}

		region1 = testGetCountryRegionsPage_addRegion(countryId, region1);

		region2 = testGetCountryRegionsPage_addRegion(countryId, region2);

		for (EntityField entityField : entityFields) {
			Page<Region> ascPage = regionResource.getCountryRegionsPage(
				countryId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(region1, region2),
				(List<Region>)ascPage.getItems());

			Page<Region> descPage = regionResource.getCountryRegionsPage(
				countryId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(region2, region1),
				(List<Region>)descPage.getItems());
		}
	}

	protected Region testGetCountryRegionsPage_addRegion(
			Long countryId, Region region)
		throws Exception {

		return regionResource.postCountryRegion(countryId, region);
	}

	protected Long testGetCountryRegionsPage_getCountryId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetCountryRegionsPage_getIrrelevantCountryId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostCountryRegion() throws Exception {
		Region randomRegion = randomRegion();

		Region postRegion = testPostCountryRegion_addRegion(randomRegion);

		assertEquals(randomRegion, postRegion);
		assertValid(postRegion);
	}

	protected Region testPostCountryRegion_addRegion(Region region)
		throws Exception {

		return regionResource.postCountryRegion(
			testGetCountryRegionsPage_getCountryId(), region);
	}

	@Test
	public void testGetCountryRegionByRegionCode() throws Exception {
		Region postRegion = testGetCountryRegionByRegionCode_addRegion();

		Region getRegion = regionResource.getCountryRegionByRegionCode(
			postRegion.getCountryId(), postRegion.getRegionCode());

		assertEquals(postRegion, getRegion);
		assertValid(getRegion);
	}

	protected Region testGetCountryRegionByRegionCode_addRegion()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetCountryRegionByRegionCode() throws Exception {
		Region region = testGraphQLGetCountryRegionByRegionCode_addRegion();

		Assert.assertTrue(
			equals(
				region,
				RegionSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"countryRegionByRegionCode",
								new HashMap<String, Object>() {
									{
										put("countryId", region.getCountryId());
										put(
											"regionCode",
											"\"" + region.getRegionCode() +
												"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/countryRegionByRegionCode"))));
	}

	@Test
	public void testGraphQLGetCountryRegionByRegionCodeNotFound()
		throws Exception {

		Long irrelevantCountryId = RandomTestUtil.randomLong();
		String irrelevantRegionCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"countryRegionByRegionCode",
						new HashMap<String, Object>() {
							{
								put("countryId", irrelevantCountryId);
								put("regionCode", irrelevantRegionCode);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Region testGraphQLGetCountryRegionByRegionCode_addRegion()
		throws Exception {

		return testGraphQLRegion_addRegion();
	}

	@Test
	public void testGetRegionsPage() throws Exception {
		Page<Region> page = regionResource.getRegionsPage(
			null, null, Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		Region region1 = testGetRegionsPage_addRegion(randomRegion());

		Region region2 = testGetRegionsPage_addRegion(randomRegion());

		page = regionResource.getRegionsPage(
			null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(region1, (List<Region>)page.getItems());
		assertContains(region2, (List<Region>)page.getItems());
		assertValid(page);

		regionResource.deleteRegion(region1.getId());

		regionResource.deleteRegion(region2.getId());
	}

	@Test
	public void testGetRegionsPageWithPagination() throws Exception {
		Page<Region> totalPage = regionResource.getRegionsPage(
			null, null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		Region region1 = testGetRegionsPage_addRegion(randomRegion());

		Region region2 = testGetRegionsPage_addRegion(randomRegion());

		Region region3 = testGetRegionsPage_addRegion(randomRegion());

		Page<Region> page1 = regionResource.getRegionsPage(
			null, null, Pagination.of(1, totalCount + 2), null);

		List<Region> regions1 = (List<Region>)page1.getItems();

		Assert.assertEquals(
			regions1.toString(), totalCount + 2, regions1.size());

		Page<Region> page2 = regionResource.getRegionsPage(
			null, null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<Region> regions2 = (List<Region>)page2.getItems();

		Assert.assertEquals(regions2.toString(), 1, regions2.size());

		Page<Region> page3 = regionResource.getRegionsPage(
			null, null, Pagination.of(1, totalCount + 3), null);

		assertContains(region1, (List<Region>)page3.getItems());
		assertContains(region2, (List<Region>)page3.getItems());
		assertContains(region3, (List<Region>)page3.getItems());
	}

	@Test
	public void testGetRegionsPageWithSortDateTime() throws Exception {
		testGetRegionsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, region1, region2) -> {
				BeanTestUtil.setProperty(
					region1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetRegionsPageWithSortDouble() throws Exception {
		testGetRegionsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, region1, region2) -> {
				BeanTestUtil.setProperty(region1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(region2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetRegionsPageWithSortInteger() throws Exception {
		testGetRegionsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, region1, region2) -> {
				BeanTestUtil.setProperty(region1, entityField.getName(), 0);
				BeanTestUtil.setProperty(region2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetRegionsPageWithSortString() throws Exception {
		testGetRegionsPageWithSort(
			EntityField.Type.STRING,
			(entityField, region1, region2) -> {
				Class<?> clazz = region1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						region1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						region2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						region1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						region2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						region1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						region2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetRegionsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Region, Region, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Region region1 = randomRegion();
		Region region2 = randomRegion();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, region1, region2);
		}

		region1 = testGetRegionsPage_addRegion(region1);

		region2 = testGetRegionsPage_addRegion(region2);

		for (EntityField entityField : entityFields) {
			Page<Region> ascPage = regionResource.getRegionsPage(
				null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(region1, region2),
				(List<Region>)ascPage.getItems());

			Page<Region> descPage = regionResource.getRegionsPage(
				null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(region2, region1),
				(List<Region>)descPage.getItems());
		}
	}

	protected Region testGetRegionsPage_addRegion(Region region)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetRegionsPage() throws Exception {
		GraphQLField graphQLField = new GraphQLField(
			"regions",
			new HashMap<String, Object>() {
				{
					put("page", 1);
					put("pageSize", 10);
				}
			},
			new GraphQLField("items", getGraphQLFields()),
			new GraphQLField("page"), new GraphQLField("totalCount"));

		JSONObject regionsJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/regions");

		long totalCount = regionsJSONObject.getLong("totalCount");

		Region region1 = testGraphQLGetRegionsPage_addRegion();
		Region region2 = testGraphQLGetRegionsPage_addRegion();

		regionsJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/regions");

		Assert.assertEquals(
			totalCount + 2, regionsJSONObject.getLong("totalCount"));

		assertContains(
			region1,
			Arrays.asList(
				RegionSerDes.toDTOs(regionsJSONObject.getString("items"))));
		assertContains(
			region2,
			Arrays.asList(
				RegionSerDes.toDTOs(regionsJSONObject.getString("items"))));
	}

	protected Region testGraphQLGetRegionsPage_addRegion() throws Exception {
		return testGraphQLRegion_addRegion();
	}

	@Test
	public void testDeleteRegion() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		Region region = testDeleteRegion_addRegion();

		assertHttpResponseStatusCode(
			204, regionResource.deleteRegionHttpResponse(region.getId()));

		assertHttpResponseStatusCode(
			404, regionResource.getRegionHttpResponse(region.getId()));

		assertHttpResponseStatusCode(
			404, regionResource.getRegionHttpResponse(0L));
	}

	protected Region testDeleteRegion_addRegion() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteRegion() throws Exception {
		Region region = testGraphQLDeleteRegion_addRegion();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteRegion",
						new HashMap<String, Object>() {
							{
								put("regionId", region.getId());
							}
						})),
				"JSONObject/data", "Object/deleteRegion"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"region",
					new HashMap<String, Object>() {
						{
							put("regionId", region.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected Region testGraphQLDeleteRegion_addRegion() throws Exception {
		return testGraphQLRegion_addRegion();
	}

	@Test
	public void testGetRegion() throws Exception {
		Region postRegion = testGetRegion_addRegion();

		Region getRegion = regionResource.getRegion(postRegion.getId());

		assertEquals(postRegion, getRegion);
		assertValid(getRegion);
	}

	protected Region testGetRegion_addRegion() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetRegion() throws Exception {
		Region region = testGraphQLGetRegion_addRegion();

		Assert.assertTrue(
			equals(
				region,
				RegionSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"region",
								new HashMap<String, Object>() {
									{
										put("regionId", region.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/region"))));
	}

	@Test
	public void testGraphQLGetRegionNotFound() throws Exception {
		Long irrelevantRegionId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"region",
						new HashMap<String, Object>() {
							{
								put("regionId", irrelevantRegionId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Region testGraphQLGetRegion_addRegion() throws Exception {
		return testGraphQLRegion_addRegion();
	}

	@Test
	public void testPatchRegion() throws Exception {
		Region postRegion = testPatchRegion_addRegion();

		Region randomPatchRegion = randomPatchRegion();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Region patchRegion = regionResource.patchRegion(
			postRegion.getId(), randomPatchRegion);

		Region expectedPatchRegion = postRegion.clone();

		BeanTestUtil.copyProperties(randomPatchRegion, expectedPatchRegion);

		Region getRegion = regionResource.getRegion(patchRegion.getId());

		assertEquals(expectedPatchRegion, getRegion);
		assertValid(getRegion);
	}

	protected Region testPatchRegion_addRegion() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPutRegion() throws Exception {
		Region postRegion = testPutRegion_addRegion();

		Region randomRegion = randomRegion();

		Region putRegion = regionResource.putRegion(
			postRegion.getId(), randomRegion);

		assertEquals(randomRegion, putRegion);
		assertValid(putRegion);

		Region getRegion = regionResource.getRegion(putRegion.getId());

		assertEquals(randomRegion, getRegion);
		assertValid(getRegion);
	}

	protected Region testPutRegion_addRegion() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Region testGraphQLRegion_addRegion() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(Region region, List<Region> regions) {
		boolean contains = false;

		for (Region item : regions) {
			if (equals(region, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(regions + " does not contain " + region, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Region region1, Region region2) {
		Assert.assertTrue(
			region1 + " does not equal " + region2, equals(region1, region2));
	}

	protected void assertEquals(List<Region> regions1, List<Region> regions2) {
		Assert.assertEquals(regions1.size(), regions2.size());

		for (int i = 0; i < regions1.size(); i++) {
			Region region1 = regions1.get(i);
			Region region2 = regions2.get(i);

			assertEquals(region1, region2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<Region> regions1, List<Region> regions2) {

		Assert.assertEquals(regions1.size(), regions2.size());

		for (Region region1 : regions1) {
			boolean contains = false;

			for (Region region2 : regions2) {
				if (equals(region1, region2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				regions2 + " does not contain " + region1, contains);
		}
	}

	protected void assertValid(Region region) throws Exception {
		boolean valid = true;

		if (region.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("active", additionalAssertFieldName)) {
				if (region.getActive() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("countryId", additionalAssertFieldName)) {
				if (region.getCountryId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (region.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("position", additionalAssertFieldName)) {
				if (region.getPosition() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("regionCode", additionalAssertFieldName)) {
				if (region.getRegionCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("title_i18n", additionalAssertFieldName)) {
				if (region.getTitle_i18n() == null) {
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

	protected void assertValid(Page<Region> page) {
		boolean valid = false;

		java.util.Collection<Region> regions = page.getItems();

		int size = regions.size();

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
					com.liferay.headless.admin.address.dto.v1_0.Region.class)) {

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

	protected boolean equals(Region region1, Region region2) {
		if (region1 == region2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("active", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						region1.getActive(), region2.getActive())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("countryId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						region1.getCountryId(), region2.getCountryId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(region1.getId(), region2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(region1.getName(), region2.getName())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("position", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						region1.getPosition(), region2.getPosition())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("regionCode", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						region1.getRegionCode(), region2.getRegionCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("title_i18n", additionalAssertFieldName)) {
				if (!equals(
						(Map)region1.getTitle_i18n(),
						(Map)region2.getTitle_i18n())) {

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

		if (!(_regionResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_regionResource;

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
		EntityField entityField, String operator, Region region) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("active")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("countryId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(region.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("position")) {
			sb.append(String.valueOf(region.getPosition()));

			return sb.toString();
		}

		if (entityFieldName.equals("regionCode")) {
			sb.append("'");
			sb.append(String.valueOf(region.getRegionCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("title_i18n")) {
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

	protected Region randomRegion() throws Exception {
		return new Region() {
			{
				active = RandomTestUtil.randomBoolean();
				countryId = RandomTestUtil.randomLong();
				id = RandomTestUtil.randomLong();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				position = RandomTestUtil.randomDouble();
				regionCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
			}
		};
	}

	protected Region randomIrrelevantRegion() throws Exception {
		Region randomIrrelevantRegion = randomRegion();

		return randomIrrelevantRegion;
	}

	protected Region randomPatchRegion() throws Exception {
		return randomRegion();
	}

	protected RegionResource regionResource;
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
		LogFactoryUtil.getLog(BaseRegionResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.admin.address.resource.v1_0.RegionResource
		_regionResource;

}