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

package com.liferay.headless.commerce.admin.site.setting.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.site.setting.client.dto.v1_0.Warehouse;
import com.liferay.headless.commerce.admin.site.setting.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.site.setting.client.pagination.Page;
import com.liferay.headless.commerce.admin.site.setting.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.site.setting.client.resource.v1_0.WarehouseResource;
import com.liferay.headless.commerce.admin.site.setting.client.serdes.v1_0.WarehouseSerDes;
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
		warehouse.setDescription(regex);
		warehouse.setName(regex);
		warehouse.setStreet1(regex);
		warehouse.setStreet2(regex);
		warehouse.setStreet3(regex);
		warehouse.setZip(regex);

		String json = WarehouseSerDes.toJSON(warehouse);

		Assert.assertFalse(json.contains(regex));

		warehouse = WarehouseSerDes.toDTO(json);

		Assert.assertEquals(regex, warehouse.getCity());
		Assert.assertEquals(regex, warehouse.getDescription());
		Assert.assertEquals(regex, warehouse.getName());
		Assert.assertEquals(regex, warehouse.getStreet1());
		Assert.assertEquals(regex, warehouse.getStreet2());
		Assert.assertEquals(regex, warehouse.getStreet3());
		Assert.assertEquals(regex, warehouse.getZip());
	}

	@Test
	public void testGetCommerceAdminSiteSettingGroupWarehousePage()
		throws Exception {

		Long groupId =
			testGetCommerceAdminSiteSettingGroupWarehousePage_getGroupId();
		Long irrelevantGroupId =
			testGetCommerceAdminSiteSettingGroupWarehousePage_getIrrelevantGroupId();

		Page<Warehouse> page =
			warehouseResource.getCommerceAdminSiteSettingGroupWarehousePage(
				groupId, null, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantGroupId != null) {
			Warehouse irrelevantWarehouse =
				testGetCommerceAdminSiteSettingGroupWarehousePage_addWarehouse(
					irrelevantGroupId, randomIrrelevantWarehouse());

			page =
				warehouseResource.getCommerceAdminSiteSettingGroupWarehousePage(
					irrelevantGroupId, null, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantWarehouse),
				(List<Warehouse>)page.getItems());
			assertValid(page);
		}

		Warehouse warehouse1 =
			testGetCommerceAdminSiteSettingGroupWarehousePage_addWarehouse(
				groupId, randomWarehouse());

		Warehouse warehouse2 =
			testGetCommerceAdminSiteSettingGroupWarehousePage_addWarehouse(
				groupId, randomWarehouse());

		page = warehouseResource.getCommerceAdminSiteSettingGroupWarehousePage(
			groupId, null, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(warehouse1, warehouse2),
			(List<Warehouse>)page.getItems());
		assertValid(page);

		warehouseResource.deleteWarehouse(warehouse1.getId());

		warehouseResource.deleteWarehouse(warehouse2.getId());
	}

	@Test
	public void testGetCommerceAdminSiteSettingGroupWarehousePageWithPagination()
		throws Exception {

		Long groupId =
			testGetCommerceAdminSiteSettingGroupWarehousePage_getGroupId();

		Warehouse warehouse1 =
			testGetCommerceAdminSiteSettingGroupWarehousePage_addWarehouse(
				groupId, randomWarehouse());

		Warehouse warehouse2 =
			testGetCommerceAdminSiteSettingGroupWarehousePage_addWarehouse(
				groupId, randomWarehouse());

		Warehouse warehouse3 =
			testGetCommerceAdminSiteSettingGroupWarehousePage_addWarehouse(
				groupId, randomWarehouse());

		Page<Warehouse> page1 =
			warehouseResource.getCommerceAdminSiteSettingGroupWarehousePage(
				groupId, null, Pagination.of(1, 2));

		List<Warehouse> warehouses1 = (List<Warehouse>)page1.getItems();

		Assert.assertEquals(warehouses1.toString(), 2, warehouses1.size());

		Page<Warehouse> page2 =
			warehouseResource.getCommerceAdminSiteSettingGroupWarehousePage(
				groupId, null, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<Warehouse> warehouses2 = (List<Warehouse>)page2.getItems();

		Assert.assertEquals(warehouses2.toString(), 1, warehouses2.size());

		Page<Warehouse> page3 =
			warehouseResource.getCommerceAdminSiteSettingGroupWarehousePage(
				groupId, null, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(warehouse1, warehouse2, warehouse3),
			(List<Warehouse>)page3.getItems());
	}

	protected Warehouse
			testGetCommerceAdminSiteSettingGroupWarehousePage_addWarehouse(
				Long groupId, Warehouse warehouse)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetCommerceAdminSiteSettingGroupWarehousePage_getGroupId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetCommerceAdminSiteSettingGroupWarehousePage_getIrrelevantGroupId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostCommerceAdminSiteSettingGroupWarehouse()
		throws Exception {

		Warehouse randomWarehouse = randomWarehouse();

		Warehouse postWarehouse =
			testPostCommerceAdminSiteSettingGroupWarehouse_addWarehouse(
				randomWarehouse);

		assertEquals(randomWarehouse, postWarehouse);
		assertValid(postWarehouse);
	}

	protected Warehouse
			testPostCommerceAdminSiteSettingGroupWarehouse_addWarehouse(
				Warehouse warehouse)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteWarehouse() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		Warehouse warehouse = testDeleteWarehouse_addWarehouse();

		assertHttpResponseStatusCode(
			204,
			warehouseResource.deleteWarehouseHttpResponse(warehouse.getId()));

		assertHttpResponseStatusCode(
			404, warehouseResource.getWarehouseHttpResponse(warehouse.getId()));

		assertHttpResponseStatusCode(
			404, warehouseResource.getWarehouseHttpResponse(warehouse.getId()));
	}

	protected Warehouse testDeleteWarehouse_addWarehouse() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteWarehouse() throws Exception {
		Warehouse warehouse = testGraphQLDeleteWarehouse_addWarehouse();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteWarehouse",
						new HashMap<String, Object>() {
							{
								put("id", warehouse.getId());
							}
						})),
				"JSONObject/data", "Object/deleteWarehouse"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"warehouse",
					new HashMap<String, Object>() {
						{
							put("id", warehouse.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected Warehouse testGraphQLDeleteWarehouse_addWarehouse()
		throws Exception {

		return testGraphQLWarehouse_addWarehouse();
	}

	@Test
	public void testGetWarehouse() throws Exception {
		Warehouse postWarehouse = testGetWarehouse_addWarehouse();

		Warehouse getWarehouse = warehouseResource.getWarehouse(
			postWarehouse.getId());

		assertEquals(postWarehouse, getWarehouse);
		assertValid(getWarehouse);
	}

	protected Warehouse testGetWarehouse_addWarehouse() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetWarehouse() throws Exception {
		Warehouse warehouse = testGraphQLGetWarehouse_addWarehouse();

		Assert.assertTrue(
			equals(
				warehouse,
				WarehouseSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"warehouse",
								new HashMap<String, Object>() {
									{
										put("id", warehouse.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/warehouse"))));
	}

	@Test
	public void testGraphQLGetWarehouseNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"warehouse",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Warehouse testGraphQLGetWarehouse_addWarehouse()
		throws Exception {

		return testGraphQLWarehouse_addWarehouse();
	}

	@Test
	public void testPutWarehouse() throws Exception {
		Assert.assertTrue(false);
	}

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

			if (Objects.equals(
					"commerceCountryId", additionalAssertFieldName)) {

				if (warehouse.getCommerceCountryId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("commerceRegionId", additionalAssertFieldName)) {
				if (warehouse.getCommerceRegionId() == null) {
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

			if (Objects.equals("groupId", additionalAssertFieldName)) {
				if (warehouse.getGroupId() == null) {
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

			if (Objects.equals("primary", additionalAssertFieldName)) {
				if (warehouse.getPrimary() == null) {
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
					com.liferay.headless.commerce.admin.site.setting.dto.v1_0.
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

			if (Objects.equals(
					"commerceCountryId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						warehouse1.getCommerceCountryId(),
						warehouse2.getCommerceCountryId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("commerceRegionId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getCommerceRegionId(),
						warehouse2.getCommerceRegionId())) {

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

			if (Objects.equals("groupId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getGroupId(), warehouse2.getGroupId())) {

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

			if (Objects.equals("primary", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouse1.getPrimary(), warehouse2.getPrimary())) {

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

		if (entityFieldName.equals("commerceCountryId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("commerceRegionId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(warehouse.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("groupId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
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

		if (entityFieldName.equals("primary")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
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
				commerceCountryId = RandomTestUtil.randomLong();
				commerceRegionId = RandomTestUtil.randomLong();
				description = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				groupId = RandomTestUtil.randomLong();
				id = RandomTestUtil.randomLong();
				latitude = RandomTestUtil.randomDouble();
				longitude = RandomTestUtil.randomDouble();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				primary = RandomTestUtil.randomBoolean();
				street1 = StringUtil.toLowerCase(RandomTestUtil.randomString());
				street2 = StringUtil.toLowerCase(RandomTestUtil.randomString());
				street3 = StringUtil.toLowerCase(RandomTestUtil.randomString());
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
	private com.liferay.headless.commerce.admin.site.setting.resource.v1_0.
		WarehouseResource _warehouseResource;

}