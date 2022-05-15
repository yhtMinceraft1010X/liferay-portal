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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.ProductChannel;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.ProductChannelResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.ProductChannelSerDes;
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
public abstract class BaseProductChannelResourceTestCase {

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

		_productChannelResource.setContextCompany(testCompany);

		ProductChannelResource.Builder builder =
			ProductChannelResource.builder();

		productChannelResource = builder.authentication(
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

		ProductChannel productChannel1 = randomProductChannel();

		String json = objectMapper.writeValueAsString(productChannel1);

		ProductChannel productChannel2 = ProductChannelSerDes.toDTO(json);

		Assert.assertTrue(equals(productChannel1, productChannel2));
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

		ProductChannel productChannel = randomProductChannel();

		String json1 = objectMapper.writeValueAsString(productChannel);
		String json2 = ProductChannelSerDes.toJSON(productChannel);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ProductChannel productChannel = randomProductChannel();

		productChannel.setCurrencyCode(regex);
		productChannel.setExternalReferenceCode(regex);
		productChannel.setName(regex);
		productChannel.setType(regex);

		String json = ProductChannelSerDes.toJSON(productChannel);

		Assert.assertFalse(json.contains(regex));

		productChannel = ProductChannelSerDes.toDTO(json);

		Assert.assertEquals(regex, productChannel.getCurrencyCode());
		Assert.assertEquals(regex, productChannel.getExternalReferenceCode());
		Assert.assertEquals(regex, productChannel.getName());
		Assert.assertEquals(regex, productChannel.getType());
	}

	@Test
	public void testDeleteProductChannel() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		ProductChannel productChannel =
			testDeleteProductChannel_addProductChannel();

		assertHttpResponseStatusCode(
			204,
			productChannelResource.deleteProductChannelHttpResponse(
				productChannel.getId()));

		assertHttpResponseStatusCode(
			404,
			productChannelResource.getProductChannelHttpResponse(
				productChannel.getId()));

		assertHttpResponseStatusCode(
			404,
			productChannelResource.getProductChannelHttpResponse(
				productChannel.getId()));
	}

	protected ProductChannel testDeleteProductChannel_addProductChannel()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteProductChannel() throws Exception {
		ProductChannel productChannel =
			testGraphQLDeleteProductChannel_addProductChannel();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteProductChannel",
						new HashMap<String, Object>() {
							{
								put("id", productChannel.getId());
							}
						})),
				"JSONObject/data", "Object/deleteProductChannel"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"productChannel",
					new HashMap<String, Object>() {
						{
							put("id", productChannel.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected ProductChannel testGraphQLDeleteProductChannel_addProductChannel()
		throws Exception {

		return testGraphQLProductChannel_addProductChannel();
	}

	@Test
	public void testGetProductChannel() throws Exception {
		ProductChannel postProductChannel =
			testGetProductChannel_addProductChannel();

		ProductChannel getProductChannel =
			productChannelResource.getProductChannel(
				postProductChannel.getId());

		assertEquals(postProductChannel, getProductChannel);
		assertValid(getProductChannel);
	}

	protected ProductChannel testGetProductChannel_addProductChannel()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetProductChannel() throws Exception {
		ProductChannel productChannel =
			testGraphQLGetProductChannel_addProductChannel();

		Assert.assertTrue(
			equals(
				productChannel,
				ProductChannelSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"productChannel",
								new HashMap<String, Object>() {
									{
										put("id", productChannel.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/productChannel"))));
	}

	@Test
	public void testGraphQLGetProductChannelNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"productChannel",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected ProductChannel testGraphQLGetProductChannel_addProductChannel()
		throws Exception {

		return testGraphQLProductChannel_addProductChannel();
	}

	@Test
	public void testGetProductByExternalReferenceCodeProductChannelsPage()
		throws Exception {

		String externalReferenceCode =
			testGetProductByExternalReferenceCodeProductChannelsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetProductByExternalReferenceCodeProductChannelsPage_getIrrelevantExternalReferenceCode();

		Page<ProductChannel> page =
			productChannelResource.
				getProductByExternalReferenceCodeProductChannelsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			ProductChannel irrelevantProductChannel =
				testGetProductByExternalReferenceCodeProductChannelsPage_addProductChannel(
					irrelevantExternalReferenceCode,
					randomIrrelevantProductChannel());

			page =
				productChannelResource.
					getProductByExternalReferenceCodeProductChannelsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantProductChannel),
				(List<ProductChannel>)page.getItems());
			assertValid(page);
		}

		ProductChannel productChannel1 =
			testGetProductByExternalReferenceCodeProductChannelsPage_addProductChannel(
				externalReferenceCode, randomProductChannel());

		ProductChannel productChannel2 =
			testGetProductByExternalReferenceCodeProductChannelsPage_addProductChannel(
				externalReferenceCode, randomProductChannel());

		page =
			productChannelResource.
				getProductByExternalReferenceCodeProductChannelsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(productChannel1, productChannel2),
			(List<ProductChannel>)page.getItems());
		assertValid(page);

		productChannelResource.deleteProductChannel(productChannel1.getId());

		productChannelResource.deleteProductChannel(productChannel2.getId());
	}

	@Test
	public void testGetProductByExternalReferenceCodeProductChannelsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetProductByExternalReferenceCodeProductChannelsPage_getExternalReferenceCode();

		ProductChannel productChannel1 =
			testGetProductByExternalReferenceCodeProductChannelsPage_addProductChannel(
				externalReferenceCode, randomProductChannel());

		ProductChannel productChannel2 =
			testGetProductByExternalReferenceCodeProductChannelsPage_addProductChannel(
				externalReferenceCode, randomProductChannel());

		ProductChannel productChannel3 =
			testGetProductByExternalReferenceCodeProductChannelsPage_addProductChannel(
				externalReferenceCode, randomProductChannel());

		Page<ProductChannel> page1 =
			productChannelResource.
				getProductByExternalReferenceCodeProductChannelsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<ProductChannel> productChannels1 =
			(List<ProductChannel>)page1.getItems();

		Assert.assertEquals(
			productChannels1.toString(), 2, productChannels1.size());

		Page<ProductChannel> page2 =
			productChannelResource.
				getProductByExternalReferenceCodeProductChannelsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<ProductChannel> productChannels2 =
			(List<ProductChannel>)page2.getItems();

		Assert.assertEquals(
			productChannels2.toString(), 1, productChannels2.size());

		Page<ProductChannel> page3 =
			productChannelResource.
				getProductByExternalReferenceCodeProductChannelsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(productChannel1, productChannel2, productChannel3),
			(List<ProductChannel>)page3.getItems());
	}

	protected ProductChannel
			testGetProductByExternalReferenceCodeProductChannelsPage_addProductChannel(
				String externalReferenceCode, ProductChannel productChannel)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetProductByExternalReferenceCodeProductChannelsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetProductByExternalReferenceCodeProductChannelsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testGetProductIdProductChannelsPage() throws Exception {
		Long id = testGetProductIdProductChannelsPage_getId();
		Long irrelevantId =
			testGetProductIdProductChannelsPage_getIrrelevantId();

		Page<ProductChannel> page =
			productChannelResource.getProductIdProductChannelsPage(
				id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			ProductChannel irrelevantProductChannel =
				testGetProductIdProductChannelsPage_addProductChannel(
					irrelevantId, randomIrrelevantProductChannel());

			page = productChannelResource.getProductIdProductChannelsPage(
				irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantProductChannel),
				(List<ProductChannel>)page.getItems());
			assertValid(page);
		}

		ProductChannel productChannel1 =
			testGetProductIdProductChannelsPage_addProductChannel(
				id, randomProductChannel());

		ProductChannel productChannel2 =
			testGetProductIdProductChannelsPage_addProductChannel(
				id, randomProductChannel());

		page = productChannelResource.getProductIdProductChannelsPage(
			id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(productChannel1, productChannel2),
			(List<ProductChannel>)page.getItems());
		assertValid(page);

		productChannelResource.deleteProductChannel(productChannel1.getId());

		productChannelResource.deleteProductChannel(productChannel2.getId());
	}

	@Test
	public void testGetProductIdProductChannelsPageWithPagination()
		throws Exception {

		Long id = testGetProductIdProductChannelsPage_getId();

		ProductChannel productChannel1 =
			testGetProductIdProductChannelsPage_addProductChannel(
				id, randomProductChannel());

		ProductChannel productChannel2 =
			testGetProductIdProductChannelsPage_addProductChannel(
				id, randomProductChannel());

		ProductChannel productChannel3 =
			testGetProductIdProductChannelsPage_addProductChannel(
				id, randomProductChannel());

		Page<ProductChannel> page1 =
			productChannelResource.getProductIdProductChannelsPage(
				id, Pagination.of(1, 2));

		List<ProductChannel> productChannels1 =
			(List<ProductChannel>)page1.getItems();

		Assert.assertEquals(
			productChannels1.toString(), 2, productChannels1.size());

		Page<ProductChannel> page2 =
			productChannelResource.getProductIdProductChannelsPage(
				id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<ProductChannel> productChannels2 =
			(List<ProductChannel>)page2.getItems();

		Assert.assertEquals(
			productChannels2.toString(), 1, productChannels2.size());

		Page<ProductChannel> page3 =
			productChannelResource.getProductIdProductChannelsPage(
				id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(productChannel1, productChannel2, productChannel3),
			(List<ProductChannel>)page3.getItems());
	}

	protected ProductChannel
			testGetProductIdProductChannelsPage_addProductChannel(
				Long id, ProductChannel productChannel)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductIdProductChannelsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductIdProductChannelsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	protected ProductChannel testGraphQLProductChannel_addProductChannel()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		ProductChannel productChannel, List<ProductChannel> productChannels) {

		boolean contains = false;

		for (ProductChannel item : productChannels) {
			if (equals(productChannel, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			productChannels + " does not contain " + productChannel, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ProductChannel productChannel1, ProductChannel productChannel2) {

		Assert.assertTrue(
			productChannel1 + " does not equal " + productChannel2,
			equals(productChannel1, productChannel2));
	}

	protected void assertEquals(
		List<ProductChannel> productChannels1,
		List<ProductChannel> productChannels2) {

		Assert.assertEquals(productChannels1.size(), productChannels2.size());

		for (int i = 0; i < productChannels1.size(); i++) {
			ProductChannel productChannel1 = productChannels1.get(i);
			ProductChannel productChannel2 = productChannels2.get(i);

			assertEquals(productChannel1, productChannel2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ProductChannel> productChannels1,
		List<ProductChannel> productChannels2) {

		Assert.assertEquals(productChannels1.size(), productChannels2.size());

		for (ProductChannel productChannel1 : productChannels1) {
			boolean contains = false;

			for (ProductChannel productChannel2 : productChannels2) {
				if (equals(productChannel1, productChannel2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				productChannels2 + " does not contain " + productChannel1,
				contains);
		}
	}

	protected void assertValid(ProductChannel productChannel) throws Exception {
		boolean valid = true;

		if (productChannel.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("channelId", additionalAssertFieldName)) {
				if (productChannel.getChannelId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("currencyCode", additionalAssertFieldName)) {
				if (productChannel.getCurrencyCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (productChannel.getExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (productChannel.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (productChannel.getType() == null) {
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

	protected void assertValid(Page<ProductChannel> page) {
		boolean valid = false;

		java.util.Collection<ProductChannel> productChannels = page.getItems();

		int size = productChannels.size();

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
						ProductChannel.class)) {

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
		ProductChannel productChannel1, ProductChannel productChannel2) {

		if (productChannel1 == productChannel2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("channelId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productChannel1.getChannelId(),
						productChannel2.getChannelId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("currencyCode", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productChannel1.getCurrencyCode(),
						productChannel2.getCurrencyCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productChannel1.getExternalReferenceCode(),
						productChannel2.getExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productChannel1.getId(), productChannel2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productChannel1.getName(), productChannel2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productChannel1.getType(), productChannel2.getType())) {

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

		if (!(_productChannelResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_productChannelResource;

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
		ProductChannel productChannel) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("channelId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("currencyCode")) {
			sb.append("'");
			sb.append(String.valueOf(productChannel.getCurrencyCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("externalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(productChannel.getExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(productChannel.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("type")) {
			sb.append("'");
			sb.append(String.valueOf(productChannel.getType()));
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

	protected ProductChannel randomProductChannel() throws Exception {
		return new ProductChannel() {
			{
				channelId = RandomTestUtil.randomLong();
				currencyCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				type = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected ProductChannel randomIrrelevantProductChannel() throws Exception {
		ProductChannel randomIrrelevantProductChannel = randomProductChannel();

		return randomIrrelevantProductChannel;
	}

	protected ProductChannel randomPatchProductChannel() throws Exception {
		return randomProductChannel();
	}

	protected ProductChannelResource productChannelResource;
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
		LogFactoryUtil.getLog(BaseProductChannelResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		ProductChannelResource _productChannelResource;

}