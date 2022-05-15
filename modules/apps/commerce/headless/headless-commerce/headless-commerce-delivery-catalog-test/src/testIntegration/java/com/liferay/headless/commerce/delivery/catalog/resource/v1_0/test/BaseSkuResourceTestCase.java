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

package com.liferay.headless.commerce.delivery.catalog.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.delivery.catalog.client.dto.v1_0.Sku;
import com.liferay.headless.commerce.delivery.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Page;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.delivery.catalog.client.resource.v1_0.SkuResource;
import com.liferay.headless.commerce.delivery.catalog.client.serdes.v1_0.SkuSerDes;
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
public abstract class BaseSkuResourceTestCase {

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

		_skuResource.setContextCompany(testCompany);

		SkuResource.Builder builder = SkuResource.builder();

		skuResource = builder.authentication(
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

		Sku sku1 = randomSku();

		String json = objectMapper.writeValueAsString(sku1);

		Sku sku2 = SkuSerDes.toDTO(json);

		Assert.assertTrue(equals(sku1, sku2));
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

		Sku sku = randomSku();

		String json1 = objectMapper.writeValueAsString(sku);
		String json2 = SkuSerDes.toJSON(sku);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Sku sku = randomSku();

		sku.setGtin(regex);
		sku.setManufacturerPartNumber(regex);
		sku.setSku(regex);

		String json = SkuSerDes.toJSON(sku);

		Assert.assertFalse(json.contains(regex));

		sku = SkuSerDes.toDTO(json);

		Assert.assertEquals(regex, sku.getGtin());
		Assert.assertEquals(regex, sku.getManufacturerPartNumber());
		Assert.assertEquals(regex, sku.getSku());
	}

	@Test
	public void testGetChannelProductSkusPage() throws Exception {
		Long channelId = testGetChannelProductSkusPage_getChannelId();
		Long irrelevantChannelId =
			testGetChannelProductSkusPage_getIrrelevantChannelId();
		Long productId = testGetChannelProductSkusPage_getProductId();
		Long irrelevantProductId =
			testGetChannelProductSkusPage_getIrrelevantProductId();

		Page<Sku> page = skuResource.getChannelProductSkusPage(
			channelId, productId, null, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if ((irrelevantChannelId != null) && (irrelevantProductId != null)) {
			Sku irrelevantSku = testGetChannelProductSkusPage_addSku(
				irrelevantChannelId, irrelevantProductId,
				randomIrrelevantSku());

			page = skuResource.getChannelProductSkusPage(
				irrelevantChannelId, irrelevantProductId, null,
				Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantSku), (List<Sku>)page.getItems());
			assertValid(page);
		}

		Sku sku1 = testGetChannelProductSkusPage_addSku(
			channelId, productId, randomSku());

		Sku sku2 = testGetChannelProductSkusPage_addSku(
			channelId, productId, randomSku());

		page = skuResource.getChannelProductSkusPage(
			channelId, productId, null, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(sku1, sku2), (List<Sku>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetChannelProductSkusPageWithPagination() throws Exception {
		Long channelId = testGetChannelProductSkusPage_getChannelId();
		Long productId = testGetChannelProductSkusPage_getProductId();

		Sku sku1 = testGetChannelProductSkusPage_addSku(
			channelId, productId, randomSku());

		Sku sku2 = testGetChannelProductSkusPage_addSku(
			channelId, productId, randomSku());

		Sku sku3 = testGetChannelProductSkusPage_addSku(
			channelId, productId, randomSku());

		Page<Sku> page1 = skuResource.getChannelProductSkusPage(
			channelId, productId, null, Pagination.of(1, 2));

		List<Sku> skus1 = (List<Sku>)page1.getItems();

		Assert.assertEquals(skus1.toString(), 2, skus1.size());

		Page<Sku> page2 = skuResource.getChannelProductSkusPage(
			channelId, productId, null, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<Sku> skus2 = (List<Sku>)page2.getItems();

		Assert.assertEquals(skus2.toString(), 1, skus2.size());

		Page<Sku> page3 = skuResource.getChannelProductSkusPage(
			channelId, productId, null, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(sku1, sku2, sku3), (List<Sku>)page3.getItems());
	}

	protected Sku testGetChannelProductSkusPage_addSku(
			Long channelId, Long productId, Sku sku)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelProductSkusPage_getChannelId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelProductSkusPage_getIrrelevantChannelId()
		throws Exception {

		return null;
	}

	protected Long testGetChannelProductSkusPage_getProductId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelProductSkusPage_getIrrelevantProductId()
		throws Exception {

		return null;
	}

	protected Sku testGraphQLSku_addSku() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(Sku sku, List<Sku> skus) {
		boolean contains = false;

		for (Sku item : skus) {
			if (equals(sku, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(skus + " does not contain " + sku, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Sku sku1, Sku sku2) {
		Assert.assertTrue(sku1 + " does not equal " + sku2, equals(sku1, sku2));
	}

	protected void assertEquals(List<Sku> skus1, List<Sku> skus2) {
		Assert.assertEquals(skus1.size(), skus2.size());

		for (int i = 0; i < skus1.size(); i++) {
			Sku sku1 = skus1.get(i);
			Sku sku2 = skus2.get(i);

			assertEquals(sku1, sku2);
		}
	}

	protected void assertEqualsIgnoringOrder(List<Sku> skus1, List<Sku> skus2) {
		Assert.assertEquals(skus1.size(), skus2.size());

		for (Sku sku1 : skus1) {
			boolean contains = false;

			for (Sku sku2 : skus2) {
				if (equals(sku1, sku2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(skus2 + " does not contain " + sku1, contains);
		}
	}

	protected void assertValid(Sku sku) throws Exception {
		boolean valid = true;

		if (sku.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"allowedOrderQuantities", additionalAssertFieldName)) {

				if (sku.getAllowedOrderQuantities() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("availability", additionalAssertFieldName)) {
				if (sku.getAvailability() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("depth", additionalAssertFieldName)) {
				if (sku.getDepth() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("displayDate", additionalAssertFieldName)) {
				if (sku.getDisplayDate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("expirationDate", additionalAssertFieldName)) {
				if (sku.getExpirationDate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("gtin", additionalAssertFieldName)) {
				if (sku.getGtin() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("height", additionalAssertFieldName)) {
				if (sku.getHeight() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"manufacturerPartNumber", additionalAssertFieldName)) {

				if (sku.getManufacturerPartNumber() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("maxOrderQuantity", additionalAssertFieldName)) {
				if (sku.getMaxOrderQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("minOrderQuantity", additionalAssertFieldName)) {
				if (sku.getMinOrderQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("neverExpire", additionalAssertFieldName)) {
				if (sku.getNeverExpire() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("price", additionalAssertFieldName)) {
				if (sku.getPrice() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("published", additionalAssertFieldName)) {
				if (sku.getPublished() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("purchasable", additionalAssertFieldName)) {
				if (sku.getPurchasable() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("sku", additionalAssertFieldName)) {
				if (sku.getSku() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("skuOptions", additionalAssertFieldName)) {
				if (sku.getSkuOptions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("weight", additionalAssertFieldName)) {
				if (sku.getWeight() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("width", additionalAssertFieldName)) {
				if (sku.getWidth() == null) {
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

	protected void assertValid(Page<Sku> page) {
		boolean valid = false;

		java.util.Collection<Sku> skus = page.getItems();

		int size = skus.size();

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
					com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Sku.
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

	protected boolean equals(Sku sku1, Sku sku2) {
		if (sku1 == sku2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"allowedOrderQuantities", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						sku1.getAllowedOrderQuantities(),
						sku2.getAllowedOrderQuantities())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("availability", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getAvailability(), sku2.getAvailability())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("depth", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getDepth(), sku2.getDepth())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("displayDate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getDisplayDate(), sku2.getDisplayDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("expirationDate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getExpirationDate(), sku2.getExpirationDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("gtin", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getGtin(), sku2.getGtin())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("height", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getHeight(), sku2.getHeight())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getId(), sku2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals(
					"manufacturerPartNumber", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						sku1.getManufacturerPartNumber(),
						sku2.getManufacturerPartNumber())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("maxOrderQuantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getMaxOrderQuantity(),
						sku2.getMaxOrderQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("minOrderQuantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getMinOrderQuantity(),
						sku2.getMinOrderQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("neverExpire", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getNeverExpire(), sku2.getNeverExpire())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("price", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getPrice(), sku2.getPrice())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("published", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getPublished(), sku2.getPublished())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("purchasable", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getPurchasable(), sku2.getPurchasable())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("sku", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getSku(), sku2.getSku())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("skuOptions", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getSkuOptions(), sku2.getSkuOptions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("weight", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getWeight(), sku2.getWeight())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("width", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getWidth(), sku2.getWidth())) {
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

		if (!(_skuResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_skuResource;

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
		EntityField entityField, String operator, Sku sku) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("allowedOrderQuantities")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("availability")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("depth")) {
			sb.append(String.valueOf(sku.getDepth()));

			return sb.toString();
		}

		if (entityFieldName.equals("displayDate")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(sku.getDisplayDate(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(sku.getDisplayDate(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(sku.getDisplayDate()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("expirationDate")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(sku.getExpirationDate(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(sku.getExpirationDate(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(sku.getExpirationDate()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("gtin")) {
			sb.append("'");
			sb.append(String.valueOf(sku.getGtin()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("height")) {
			sb.append(String.valueOf(sku.getHeight()));

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("manufacturerPartNumber")) {
			sb.append("'");
			sb.append(String.valueOf(sku.getManufacturerPartNumber()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("maxOrderQuantity")) {
			sb.append(String.valueOf(sku.getMaxOrderQuantity()));

			return sb.toString();
		}

		if (entityFieldName.equals("minOrderQuantity")) {
			sb.append(String.valueOf(sku.getMinOrderQuantity()));

			return sb.toString();
		}

		if (entityFieldName.equals("neverExpire")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("price")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("published")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("purchasable")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("sku")) {
			sb.append("'");
			sb.append(String.valueOf(sku.getSku()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("skuOptions")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("weight")) {
			sb.append(String.valueOf(sku.getWeight()));

			return sb.toString();
		}

		if (entityFieldName.equals("width")) {
			sb.append(String.valueOf(sku.getWidth()));

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

	protected Sku randomSku() throws Exception {
		return new Sku() {
			{
				depth = RandomTestUtil.randomDouble();
				displayDate = RandomTestUtil.nextDate();
				expirationDate = RandomTestUtil.nextDate();
				gtin = StringUtil.toLowerCase(RandomTestUtil.randomString());
				height = RandomTestUtil.randomDouble();
				id = RandomTestUtil.randomLong();
				manufacturerPartNumber = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				maxOrderQuantity = RandomTestUtil.randomInt();
				minOrderQuantity = RandomTestUtil.randomInt();
				neverExpire = RandomTestUtil.randomBoolean();
				published = RandomTestUtil.randomBoolean();
				purchasable = RandomTestUtil.randomBoolean();
				sku = StringUtil.toLowerCase(RandomTestUtil.randomString());
				weight = RandomTestUtil.randomDouble();
				width = RandomTestUtil.randomDouble();
			}
		};
	}

	protected Sku randomIrrelevantSku() throws Exception {
		Sku randomIrrelevantSku = randomSku();

		return randomIrrelevantSku;
	}

	protected Sku randomPatchSku() throws Exception {
		return randomSku();
	}

	protected SkuResource skuResource;
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
		LogFactoryUtil.getLog(BaseSkuResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private
		com.liferay.headless.commerce.delivery.catalog.resource.v1_0.SkuResource
			_skuResource;

}