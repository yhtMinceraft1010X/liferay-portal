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

package com.liferay.headless.commerce.admin.inventory.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.inventory.client.dto.v1_0.Warehouse;
import com.liferay.headless.commerce.admin.inventory.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.inventory.client.pagination.Page;
import com.liferay.headless.commerce.admin.inventory.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.inventory.client.resource.v1_0.WarehouseResource;
import com.liferay.headless.commerce.admin.inventory.client.serdes.v1_0.WarehouseSerDes;
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
 * @author Alessio Antonio Rendina
 * @generated
 */
@Generated("")
public abstract class BaseWarehouseResourceTestCase {

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

		_warehouseResource.setContextCompany(testCompany);

		WarehouseResource.Builder builder = WarehouseResource.builder();

		warehouseResource = builder.authentication(
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

		Warehouse warehouse1 = randomWarehouse();

		String json = objectMapper.writeValueAsString(warehouse1);

		Warehouse warehouse2 = WarehouseSerDes.toDTO(json);

		Assert.assertTrue(equals(warehouse1, warehouse2));
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

		Warehouse warehouse = randomWarehouse();

		String json1 = objectMapper.writeValueAsString(warehouse);
		String json2 = WarehouseSerDes.toJSON(warehouse);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Warehouse warehouse = randomWarehouse();

		warehouse.setCity(regex);
		warehouse.setCountryISOCode(regex);
		warehouse.setDescription(regex);
		warehouse.setExternalReferenceCode(regex);
		warehouse.setName(regex);
		warehouse.setRegionISOCode(regex);
		warehouse.setStreet1(regex);
		warehouse.setStreet2(regex);
		warehouse.setStreet3(regex);
		warehouse.setType(regex);
		warehouse.setZip(regex);

		String json = WarehouseSerDes.toJSON(warehouse);

		Assert.assertFalse(json.contains(regex));

		warehouse = WarehouseSerDes.toDTO(json);

		Assert.assertEquals(regex, warehouse.getCity());
		Assert.assertEquals(regex, warehouse.getCountryISOCode());
		Assert.assertEquals(regex, warehouse.getDescription());
		Assert.assertEquals(regex, warehouse.getExternalReferenceCode());
		Assert.assertEquals(regex, warehouse.getName());
		Assert.assertEquals(regex, warehouse.getRegionISOCode());
		Assert.assertEquals(regex, warehouse.getStreet1());
		Assert.assertEquals(regex, warehouse.getStreet2());
		Assert.assertEquals(regex, warehouse.getStreet3());
		Assert.assertEquals(regex, warehouse.getType());
		Assert.assertEquals(regex, warehouse.getZip());
	}

	@Test
	public void testDeleteWarehousByExternalReferenceCode() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		Warehouse warehouse =
			testDeleteWarehousByExternalReferenceCode_addWarehouse();

		assertHttpResponseStatusCode(
			204,
			warehouseResource.deleteWarehousByExternalReferenceCodeHttpResponse(
				warehouse.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			warehouseResource.getWarehousByExternalReferenceCodeHttpResponse(
				warehouse.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			warehouseResource.getWarehousByExternalReferenceCodeHttpResponse(
				warehouse.getExternalReferenceCode()));
	}

	protected Warehouse testDeleteWarehousByExternalReferenceCode_addWarehouse()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetWarehousByExternalReferenceCode() throws Exception {
		Warehouse postWarehouse =
			testGetWarehousByExternalReferenceCode_addWarehouse();

		Warehouse getWarehouse =
			warehouseResource.getWarehousByExternalReferenceCode(
				postWarehouse.getExternalReferenceCode());

		assertEquals(postWarehouse, getWarehouse);
		assertValid(getWarehouse);
	}

	protected Warehouse testGetWarehousByExternalReferenceCode_addWarehouse()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetWarehousByExternalReferenceCode()
		throws Exception {

		Warehouse warehouse =
			testGraphQLGetWarehousByExternalReferenceCode_addWarehouse();

		Assert.assertTrue(
			equals(
				warehouse,
				WarehouseSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"warehousByExternalReferenceCode",
								new HashMap<String, Object>() {
									{
										put(
											"externalReferenceCode",
											"\"" +
												warehouse.
													getExternalReferenceCode() +
														"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/warehousByExternalReferenceCode"))));
	}

	@Test
	public void testGraphQLGetWarehousByExternalReferenceCodeNotFound()
		throws Exception {

		String irrelevantExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"warehousByExternalReferenceCode",
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

	protected Warehouse
			testGraphQLGetWarehousByExternalReferenceCode_addWarehouse()
		throws Exception {

		return testGraphQLWarehouse_addWarehouse();
	}

	@Test
	public void testPatchWarehousByExternalReferenceCode() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testDeleteWarehousId() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		Warehouse warehouse = testDeleteWarehousId_addWarehouse();

		assertHttpResponseStatusCode(
			204,
			warehouseResource.deleteWarehousIdHttpResponse(warehouse.getId()));

		assertHttpResponseStatusCode(
			404,
			warehouseResource.getWarehousIdHttpResponse(warehouse.getId()));

		assertHttpResponseStatusCode(
			404,
			warehouseResource.getWarehousIdHttpResponse(warehouse.getId()));
	}

	protected Warehouse testDeleteWarehousId_addWarehouse() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetWarehousId() throws Exception {
		Warehouse postWarehouse = testGetWarehousId_addWarehouse();

		Warehouse getWarehouse = warehouseResource.getWarehousId(
			postWarehouse.getId());

		assertEquals(postWarehouse, getWarehouse);
		assertValid(getWarehouse);
	}

	protected Warehouse testGetWarehousId_addWarehouse() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetWarehousId() throws Exception {
		Warehouse warehouse = testGraphQLGetWarehousId_addWarehouse();

		Assert.assertTrue(
			equals(
				warehouse,
				WarehouseSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"warehousId",
								new HashMap<String, Object>() {
									{
										put("id", warehouse.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/warehousId"))));
	}

	@Test
	public void testGraphQLGetWarehousIdNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"warehousId",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Warehouse testGraphQLGetWarehousId_addWarehouse()
		throws Exception {

		return testGraphQLWarehouse_addWarehouse();
	}

	@Test
	public void testPatchWarehousId() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetWarehousesPage() throws Exception {
		Page<Warehouse> page = warehouseResource.getWarehousesPage(
			null, Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		Warehouse warehouse1 = testGetWarehousesPage_addWarehouse(
			randomWarehouse());

		Warehouse warehouse2 = testGetWarehousesPage_addWarehouse(
			randomWarehouse());

		page = warehouseResource.getWarehousesPage(
			null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(warehouse1, (List<Warehouse>)page.getItems());
		assertContains(warehouse2, (List<Warehouse>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetWarehousesPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Warehouse warehouse1 = randomWarehouse();

		warehouse1 = testGetWarehousesPage_addWarehouse(warehouse1);

		for (EntityField entityField : entityFields) {
			Page<Warehouse> page = warehouseResource.getWarehousesPage(
				getFilterString(entityField, "between", warehouse1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(warehouse1),
				(List<Warehouse>)page.getItems());
		}
	}

	@Test
	public void testGetWarehousesPageWithFilterDoubleEquals() throws Exception {
		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Warehouse warehouse1 = testGetWarehousesPage_addWarehouse(
			randomWarehouse());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Warehouse warehouse2 = testGetWarehousesPage_addWarehouse(
			randomWarehouse());

		for (EntityField entityField : entityFields) {
			Page<Warehouse> page = warehouseResource.getWarehousesPage(
				getFilterString(entityField, "eq", warehouse1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(warehouse1),
				(List<Warehouse>)page.getItems());
		}
	}

	@Test
	public void testGetWarehousesPageWithFilterStringEquals() throws Exception {
		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Warehouse warehouse1 = testGetWarehousesPage_addWarehouse(
			randomWarehouse());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Warehouse warehouse2 = testGetWarehousesPage_addWarehouse(
			randomWarehouse());

		for (EntityField entityField : entityFields) {
			Page<Warehouse> page = warehouseResource.getWarehousesPage(
				getFilterString(entityField, "eq", warehouse1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(warehouse1),
				(List<Warehouse>)page.getItems());
		}
	}

	@Test
	public void testGetWarehousesPageWithPagination() throws Exception {
		Page<Warehouse> totalPage = warehouseResource.getWarehousesPage(
			null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		Warehouse warehouse1 = testGetWarehousesPage_addWarehouse(
			randomWarehouse());

		Warehouse warehouse2 = testGetWarehousesPage_addWarehouse(
			randomWarehouse());

		Warehouse warehouse3 = testGetWarehousesPage_addWarehouse(
			randomWarehouse());

		Page<Warehouse> page1 = warehouseResource.getWarehousesPage(
			null, Pagination.of(1, totalCount + 2), null);

		List<Warehouse> warehouses1 = (List<Warehouse>)page1.getItems();

		Assert.assertEquals(
			warehouses1.toString(), totalCount + 2, warehouses1.size());

		Page<Warehouse> page2 = warehouseResource.getWarehousesPage(
			null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<Warehouse> warehouses2 = (List<Warehouse>)page2.getItems();

		Assert.assertEquals(warehouses2.toString(), 1, warehouses2.size());

		Page<Warehouse> page3 = warehouseResource.getWarehousesPage(
			null, Pagination.of(1, totalCount + 3), null);

		assertContains(warehouse1, (List<Warehouse>)page3.getItems());
		assertContains(warehouse2, (List<Warehouse>)page3.getItems());
		assertContains(warehouse3, (List<Warehouse>)page3.getItems());
	}

	@Test
	public void testGetWarehousesPageWithSortDateTime() throws Exception {
		testGetWarehousesPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, warehouse1, warehouse2) -> {
				BeanTestUtil.setProperty(
					warehouse1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetWarehousesPageWithSortDouble() throws Exception {
		testGetWarehousesPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, warehouse1, warehouse2) -> {
				BeanTestUtil.setProperty(
					warehouse1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					warehouse2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetWarehousesPageWithSortInteger() throws Exception {
		testGetWarehousesPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, warehouse1, warehouse2) -> {
				BeanTestUtil.setProperty(warehouse1, entityField.getName(), 0);
				BeanTestUtil.setProperty(warehouse2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetWarehousesPageWithSortString() throws Exception {
		testGetWarehousesPageWithSort(
			EntityField.Type.STRING,
			(entityField, warehouse1, warehouse2) -> {
				Class<?> clazz = warehouse1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						warehouse1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						warehouse2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						warehouse1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						warehouse2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						warehouse1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						warehouse2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetWarehousesPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Warehouse, Warehouse, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Warehouse warehouse1 = randomWarehouse();
		Warehouse warehouse2 = randomWarehouse();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, warehouse1, warehouse2);
		}

		warehouse1 = testGetWarehousesPage_addWarehouse(warehouse1);

		warehouse2 = testGetWarehousesPage_addWarehouse(warehouse2);

		for (EntityField entityField : entityFields) {
			Page<Warehouse> ascPage = warehouseResource.getWarehousesPage(
				null, Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(warehouse1, warehouse2),
				(List<Warehouse>)ascPage.getItems());

			Page<Warehouse> descPage = warehouseResource.getWarehousesPage(
				null, Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(warehouse2, warehouse1),
				(List<Warehouse>)descPage.getItems());
		}
	}

	protected Warehouse testGetWarehousesPage_addWarehouse(Warehouse warehouse)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetWarehousesPage() throws Exception {
		GraphQLField graphQLField = new GraphQLField(
			"warehouses",
			new HashMap<String, Object>() {
				{
					put("page", 1);
					put("pageSize", 10);
				}
			},
			new GraphQLField("items", getGraphQLFields()),
			new GraphQLField("page"), new GraphQLField("totalCount"));

		JSONObject warehousesJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/warehouses");

		long totalCount = warehousesJSONObject.getLong("totalCount");

		Warehouse warehouse1 = testGraphQLGetWarehousesPage_addWarehouse();
		Warehouse warehouse2 = testGraphQLGetWarehousesPage_addWarehouse();

		warehousesJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/warehouses");

		Assert.assertEquals(
			totalCount + 2, warehousesJSONObject.getLong("totalCount"));

		assertContains(
			warehouse1,
			Arrays.asList(
				WarehouseSerDes.toDTOs(
					warehousesJSONObject.getString("items"))));
		assertContains(
			warehouse2,
			Arrays.asList(
				WarehouseSerDes.toDTOs(
					warehousesJSONObject.getString("items"))));
	}

	protected Warehouse testGraphQLGetWarehousesPage_addWarehouse()
		throws Exception {

		return testGraphQLWarehouse_addWarehouse();
	}

	@Test
	public void testPostWarehous() throws Exception {
		Warehouse randomWarehouse = randomWarehouse();

		Warehouse postWarehouse = testPostWarehous_addWarehouse(
			randomWarehouse);

		assertEquals(randomWarehouse, postWarehouse);
		assertValid(postWarehouse);
	}

	protected Warehouse testPostWarehous_addWarehouse(Warehouse warehouse)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected Warehouse testGraphQLWarehouse_addWarehouse() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		Warehouse warehouse, List<Warehouse> warehouses) {

		boolean contains = false;

		for (Warehouse item : warehouses) {
			if (equals(warehouse, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			warehouses + " does not contain " + warehouse, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Warehouse warehouse1, Warehouse warehouse2) {
		Assert.assertTrue(
			warehouse1 + " does not equal " + warehouse2,
			equals(warehouse1, warehouse2));
	}

	protected void assertEquals(
		List<Warehouse> warehouses1, List<Warehouse> warehouses2) {

		Assert.assertEquals(warehouses1.size(), warehouses2.size());

		for (int i = 0; i < warehouses1.size(); i++) {
			Warehouse warehouse1 = warehouses1.get(i);
			Warehouse warehouse2 = warehouses2.get(i);

			assertEquals(warehouse1, warehouse2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<Warehouse> warehouses1, List<Warehouse> warehouses2) {

		Assert.assertEquals(warehouses1.size(), warehouses2.size());

		for (Warehouse warehouse1 : warehouses1) {
			boolean contains = false;

			for (Warehouse warehouse2 : warehouses2) {
				if (equals(warehouse1, warehouse2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				warehouses2 + " does not contain " + warehouse1, contains);
		}
	}

	protected void assertValid(Warehouse warehouse) throws Exception {
		boolean valid = true;

		if (warehouse.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("active", additionalAssertFieldName)) {
				if (warehouse.getActive() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("city", additionalAssertFieldName)) {
				if (warehouse.getCity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("countryISOCode", additionalAssertFieldName)) {
				if (warehouse.getCountryISOCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (warehouse.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (warehouse.getExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("latitude", additionalAssertFieldName)) {
				if (warehouse.getLatitude() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("longitude", additionalAssertFieldName)) {
				if (warehouse.getLongitude() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("mvccVersion", additionalAssertFieldName)) {
				if (warehouse.getMvccVersion() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (warehouse.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("regionISOCode", additionalAssertFieldName)) {
				if (warehouse.getRegionISOCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("street1", additionalAssertFieldName)) {
				if (warehouse.getStreet1() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("street2", additionalAssertFieldName)) {
				if (warehouse.getStreet2() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("street3", additionalAssertFieldName)) {
				if (warehouse.getStreet3() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (warehouse.getType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("warehouseItems", additionalAssertFieldName)) {
				if (warehouse.getWarehouseItems() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("zip", additionalAssertFieldName)) {
				if (warehouse.getZip() == null) {
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

	protected void assertValid(Page<Warehouse> page) {
		boolean valid = false;

		java.util.Collection<Warehouse> warehouses = page.getItems();

		int size = warehouses.size();

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
					com.liferay.headless.commerce.admin.inventory.dto.v1_0.
						Warehouse.class)) {

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

	protected boolean equals(Warehouse warehouse1, Warehouse warehouse2) {
		if (warehouse1 == warehouse2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("active", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getActive(), warehouse2.getActive())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("city", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getCity(), warehouse2.getCity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("countryISOCode", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getCountryISOCode(),
						warehouse2.getCountryISOCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getDescription(),
						warehouse2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						warehouse1.getExternalReferenceCode(),
						warehouse2.getExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getId(), warehouse2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("latitude", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getLatitude(), warehouse2.getLatitude())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("longitude", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getLongitude(), warehouse2.getLongitude())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("mvccVersion", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getMvccVersion(),
						warehouse2.getMvccVersion())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getName(), warehouse2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("regionISOCode", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getRegionISOCode(),
						warehouse2.getRegionISOCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street1", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getStreet1(), warehouse2.getStreet1())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street2", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getStreet2(), warehouse2.getStreet2())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street3", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getStreet3(), warehouse2.getStreet3())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getType(), warehouse2.getType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("warehouseItems", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getWarehouseItems(),
						warehouse2.getWarehouseItems())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("zip", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getZip(), warehouse2.getZip())) {

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

		if (!(_warehouseResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_warehouseResource;

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
		EntityField entityField, String operator, Warehouse warehouse) {

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

		if (entityFieldName.equals("city")) {
			sb.append("'");
			sb.append(String.valueOf(warehouse.getCity()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("countryISOCode")) {
			sb.append("'");
			sb.append(String.valueOf(warehouse.getCountryISOCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(warehouse.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("externalReferenceCode")) {
			sb.append("'");
			sb.append(String.valueOf(warehouse.getExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("latitude")) {
			sb.append(String.valueOf(warehouse.getLatitude()));

			return sb.toString();
		}

		if (entityFieldName.equals("longitude")) {
			sb.append(String.valueOf(warehouse.getLongitude()));

			return sb.toString();
		}

		if (entityFieldName.equals("mvccVersion")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(warehouse.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("regionISOCode")) {
			sb.append("'");
			sb.append(String.valueOf(warehouse.getRegionISOCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("street1")) {
			sb.append("'");
			sb.append(String.valueOf(warehouse.getStreet1()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("street2")) {
			sb.append("'");
			sb.append(String.valueOf(warehouse.getStreet2()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("street3")) {
			sb.append("'");
			sb.append(String.valueOf(warehouse.getStreet3()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("type")) {
			sb.append("'");
			sb.append(String.valueOf(warehouse.getType()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("warehouseItems")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("zip")) {
			sb.append("'");
			sb.append(String.valueOf(warehouse.getZip()));
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

	protected Warehouse randomWarehouse() throws Exception {
		return new Warehouse() {
			{
				active = RandomTestUtil.randomBoolean();
				city = StringUtil.toLowerCase(RandomTestUtil.randomString());
				countryISOCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				description = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				latitude = RandomTestUtil.randomDouble();
				longitude = RandomTestUtil.randomDouble();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				regionISOCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				street1 = StringUtil.toLowerCase(RandomTestUtil.randomString());
				street2 = StringUtil.toLowerCase(RandomTestUtil.randomString());
				street3 = StringUtil.toLowerCase(RandomTestUtil.randomString());
				type = StringUtil.toLowerCase(RandomTestUtil.randomString());
				zip = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected Warehouse randomIrrelevantWarehouse() throws Exception {
		Warehouse randomIrrelevantWarehouse = randomWarehouse();

		return randomIrrelevantWarehouse;
	}

	protected Warehouse randomPatchWarehouse() throws Exception {
		return randomWarehouse();
	}

	protected WarehouseResource warehouseResource;
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
		LogFactoryUtil.getLog(BaseWarehouseResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.inventory.resource.v1_0.
		WarehouseResource _warehouseResource;

}