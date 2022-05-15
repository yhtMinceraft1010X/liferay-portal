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

package com.liferay.headless.commerce.admin.order.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.order.client.dto.v1_0.OrderTypeChannel;
import com.liferay.headless.commerce.admin.order.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.order.client.pagination.Page;
import com.liferay.headless.commerce.admin.order.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.order.client.resource.v1_0.OrderTypeChannelResource;
import com.liferay.headless.commerce.admin.order.client.serdes.v1_0.OrderTypeChannelSerDes;
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
public abstract class BaseOrderTypeChannelResourceTestCase {

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

		_orderTypeChannelResource.setContextCompany(testCompany);

		OrderTypeChannelResource.Builder builder =
			OrderTypeChannelResource.builder();

		orderTypeChannelResource = builder.authentication(
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

		OrderTypeChannel orderTypeChannel1 = randomOrderTypeChannel();

		String json = objectMapper.writeValueAsString(orderTypeChannel1);

		OrderTypeChannel orderTypeChannel2 = OrderTypeChannelSerDes.toDTO(json);

		Assert.assertTrue(equals(orderTypeChannel1, orderTypeChannel2));
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

		OrderTypeChannel orderTypeChannel = randomOrderTypeChannel();

		String json1 = objectMapper.writeValueAsString(orderTypeChannel);
		String json2 = OrderTypeChannelSerDes.toJSON(orderTypeChannel);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		OrderTypeChannel orderTypeChannel = randomOrderTypeChannel();

		orderTypeChannel.setChannelExternalReferenceCode(regex);
		orderTypeChannel.setOrderTypeExternalReferenceCode(regex);

		String json = OrderTypeChannelSerDes.toJSON(orderTypeChannel);

		Assert.assertFalse(json.contains(regex));

		orderTypeChannel = OrderTypeChannelSerDes.toDTO(json);

		Assert.assertEquals(
			regex, orderTypeChannel.getChannelExternalReferenceCode());
		Assert.assertEquals(
			regex, orderTypeChannel.getOrderTypeExternalReferenceCode());
	}

	@Test
	public void testDeleteOrderTypeChannel() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLDeleteOrderTypeChannel() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage()
		throws Exception {

		String externalReferenceCode =
			testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage_getIrrelevantExternalReferenceCode();

		Page<OrderTypeChannel> page =
			orderTypeChannelResource.
				getOrderTypeByExternalReferenceCodeOrderTypeChannelsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			OrderTypeChannel irrelevantOrderTypeChannel =
				testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage_addOrderTypeChannel(
					irrelevantExternalReferenceCode,
					randomIrrelevantOrderTypeChannel());

			page =
				orderTypeChannelResource.
					getOrderTypeByExternalReferenceCodeOrderTypeChannelsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantOrderTypeChannel),
				(List<OrderTypeChannel>)page.getItems());
			assertValid(page);
		}

		OrderTypeChannel orderTypeChannel1 =
			testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage_addOrderTypeChannel(
				externalReferenceCode, randomOrderTypeChannel());

		OrderTypeChannel orderTypeChannel2 =
			testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage_addOrderTypeChannel(
				externalReferenceCode, randomOrderTypeChannel());

		page =
			orderTypeChannelResource.
				getOrderTypeByExternalReferenceCodeOrderTypeChannelsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(orderTypeChannel1, orderTypeChannel2),
			(List<OrderTypeChannel>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage_getExternalReferenceCode();

		OrderTypeChannel orderTypeChannel1 =
			testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage_addOrderTypeChannel(
				externalReferenceCode, randomOrderTypeChannel());

		OrderTypeChannel orderTypeChannel2 =
			testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage_addOrderTypeChannel(
				externalReferenceCode, randomOrderTypeChannel());

		OrderTypeChannel orderTypeChannel3 =
			testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage_addOrderTypeChannel(
				externalReferenceCode, randomOrderTypeChannel());

		Page<OrderTypeChannel> page1 =
			orderTypeChannelResource.
				getOrderTypeByExternalReferenceCodeOrderTypeChannelsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<OrderTypeChannel> orderTypeChannels1 =
			(List<OrderTypeChannel>)page1.getItems();

		Assert.assertEquals(
			orderTypeChannels1.toString(), 2, orderTypeChannels1.size());

		Page<OrderTypeChannel> page2 =
			orderTypeChannelResource.
				getOrderTypeByExternalReferenceCodeOrderTypeChannelsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<OrderTypeChannel> orderTypeChannels2 =
			(List<OrderTypeChannel>)page2.getItems();

		Assert.assertEquals(
			orderTypeChannels2.toString(), 1, orderTypeChannels2.size());

		Page<OrderTypeChannel> page3 =
			orderTypeChannelResource.
				getOrderTypeByExternalReferenceCodeOrderTypeChannelsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				orderTypeChannel1, orderTypeChannel2, orderTypeChannel3),
			(List<OrderTypeChannel>)page3.getItems());
	}

	protected OrderTypeChannel
			testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage_addOrderTypeChannel(
				String externalReferenceCode, OrderTypeChannel orderTypeChannel)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetOrderTypeByExternalReferenceCodeOrderTypeChannelsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostOrderTypeByExternalReferenceCodeOrderTypeChannel()
		throws Exception {

		OrderTypeChannel randomOrderTypeChannel = randomOrderTypeChannel();

		OrderTypeChannel postOrderTypeChannel =
			testPostOrderTypeByExternalReferenceCodeOrderTypeChannel_addOrderTypeChannel(
				randomOrderTypeChannel);

		assertEquals(randomOrderTypeChannel, postOrderTypeChannel);
		assertValid(postOrderTypeChannel);
	}

	protected OrderTypeChannel
			testPostOrderTypeByExternalReferenceCodeOrderTypeChannel_addOrderTypeChannel(
				OrderTypeChannel orderTypeChannel)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetOrderTypeIdOrderTypeChannelsPage() throws Exception {
		Long id = testGetOrderTypeIdOrderTypeChannelsPage_getId();
		Long irrelevantId =
			testGetOrderTypeIdOrderTypeChannelsPage_getIrrelevantId();

		Page<OrderTypeChannel> page =
			orderTypeChannelResource.getOrderTypeIdOrderTypeChannelsPage(
				id, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			OrderTypeChannel irrelevantOrderTypeChannel =
				testGetOrderTypeIdOrderTypeChannelsPage_addOrderTypeChannel(
					irrelevantId, randomIrrelevantOrderTypeChannel());

			page = orderTypeChannelResource.getOrderTypeIdOrderTypeChannelsPage(
				irrelevantId, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantOrderTypeChannel),
				(List<OrderTypeChannel>)page.getItems());
			assertValid(page);
		}

		OrderTypeChannel orderTypeChannel1 =
			testGetOrderTypeIdOrderTypeChannelsPage_addOrderTypeChannel(
				id, randomOrderTypeChannel());

		OrderTypeChannel orderTypeChannel2 =
			testGetOrderTypeIdOrderTypeChannelsPage_addOrderTypeChannel(
				id, randomOrderTypeChannel());

		page = orderTypeChannelResource.getOrderTypeIdOrderTypeChannelsPage(
			id, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(orderTypeChannel1, orderTypeChannel2),
			(List<OrderTypeChannel>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetOrderTypeIdOrderTypeChannelsPageWithPagination()
		throws Exception {

		Long id = testGetOrderTypeIdOrderTypeChannelsPage_getId();

		OrderTypeChannel orderTypeChannel1 =
			testGetOrderTypeIdOrderTypeChannelsPage_addOrderTypeChannel(
				id, randomOrderTypeChannel());

		OrderTypeChannel orderTypeChannel2 =
			testGetOrderTypeIdOrderTypeChannelsPage_addOrderTypeChannel(
				id, randomOrderTypeChannel());

		OrderTypeChannel orderTypeChannel3 =
			testGetOrderTypeIdOrderTypeChannelsPage_addOrderTypeChannel(
				id, randomOrderTypeChannel());

		Page<OrderTypeChannel> page1 =
			orderTypeChannelResource.getOrderTypeIdOrderTypeChannelsPage(
				id, null, Pagination.of(1, 2), null);

		List<OrderTypeChannel> orderTypeChannels1 =
			(List<OrderTypeChannel>)page1.getItems();

		Assert.assertEquals(
			orderTypeChannels1.toString(), 2, orderTypeChannels1.size());

		Page<OrderTypeChannel> page2 =
			orderTypeChannelResource.getOrderTypeIdOrderTypeChannelsPage(
				id, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<OrderTypeChannel> orderTypeChannels2 =
			(List<OrderTypeChannel>)page2.getItems();

		Assert.assertEquals(
			orderTypeChannels2.toString(), 1, orderTypeChannels2.size());

		Page<OrderTypeChannel> page3 =
			orderTypeChannelResource.getOrderTypeIdOrderTypeChannelsPage(
				id, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(
				orderTypeChannel1, orderTypeChannel2, orderTypeChannel3),
			(List<OrderTypeChannel>)page3.getItems());
	}

	@Test
	public void testGetOrderTypeIdOrderTypeChannelsPageWithSortDateTime()
		throws Exception {

		testGetOrderTypeIdOrderTypeChannelsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, orderTypeChannel1, orderTypeChannel2) -> {
				BeanTestUtil.setProperty(
					orderTypeChannel1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetOrderTypeIdOrderTypeChannelsPageWithSortDouble()
		throws Exception {

		testGetOrderTypeIdOrderTypeChannelsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, orderTypeChannel1, orderTypeChannel2) -> {
				BeanTestUtil.setProperty(
					orderTypeChannel1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					orderTypeChannel2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetOrderTypeIdOrderTypeChannelsPageWithSortInteger()
		throws Exception {

		testGetOrderTypeIdOrderTypeChannelsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, orderTypeChannel1, orderTypeChannel2) -> {
				BeanTestUtil.setProperty(
					orderTypeChannel1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					orderTypeChannel2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetOrderTypeIdOrderTypeChannelsPageWithSortString()
		throws Exception {

		testGetOrderTypeIdOrderTypeChannelsPageWithSort(
			EntityField.Type.STRING,
			(entityField, orderTypeChannel1, orderTypeChannel2) -> {
				Class<?> clazz = orderTypeChannel1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						orderTypeChannel1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						orderTypeChannel2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						orderTypeChannel1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						orderTypeChannel2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						orderTypeChannel1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						orderTypeChannel2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetOrderTypeIdOrderTypeChannelsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, OrderTypeChannel, OrderTypeChannel, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetOrderTypeIdOrderTypeChannelsPage_getId();

		OrderTypeChannel orderTypeChannel1 = randomOrderTypeChannel();
		OrderTypeChannel orderTypeChannel2 = randomOrderTypeChannel();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, orderTypeChannel1, orderTypeChannel2);
		}

		orderTypeChannel1 =
			testGetOrderTypeIdOrderTypeChannelsPage_addOrderTypeChannel(
				id, orderTypeChannel1);

		orderTypeChannel2 =
			testGetOrderTypeIdOrderTypeChannelsPage_addOrderTypeChannel(
				id, orderTypeChannel2);

		for (EntityField entityField : entityFields) {
			Page<OrderTypeChannel> ascPage =
				orderTypeChannelResource.getOrderTypeIdOrderTypeChannelsPage(
					id, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(orderTypeChannel1, orderTypeChannel2),
				(List<OrderTypeChannel>)ascPage.getItems());

			Page<OrderTypeChannel> descPage =
				orderTypeChannelResource.getOrderTypeIdOrderTypeChannelsPage(
					id, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(orderTypeChannel2, orderTypeChannel1),
				(List<OrderTypeChannel>)descPage.getItems());
		}
	}

	protected OrderTypeChannel
			testGetOrderTypeIdOrderTypeChannelsPage_addOrderTypeChannel(
				Long id, OrderTypeChannel orderTypeChannel)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetOrderTypeIdOrderTypeChannelsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetOrderTypeIdOrderTypeChannelsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostOrderTypeIdOrderTypeChannel() throws Exception {
		OrderTypeChannel randomOrderTypeChannel = randomOrderTypeChannel();

		OrderTypeChannel postOrderTypeChannel =
			testPostOrderTypeIdOrderTypeChannel_addOrderTypeChannel(
				randomOrderTypeChannel);

		assertEquals(randomOrderTypeChannel, postOrderTypeChannel);
		assertValid(postOrderTypeChannel);
	}

	protected OrderTypeChannel
			testPostOrderTypeIdOrderTypeChannel_addOrderTypeChannel(
				OrderTypeChannel orderTypeChannel)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		OrderTypeChannel orderTypeChannel,
		List<OrderTypeChannel> orderTypeChannels) {

		boolean contains = false;

		for (OrderTypeChannel item : orderTypeChannels) {
			if (equals(orderTypeChannel, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			orderTypeChannels + " does not contain " + orderTypeChannel,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		OrderTypeChannel orderTypeChannel1,
		OrderTypeChannel orderTypeChannel2) {

		Assert.assertTrue(
			orderTypeChannel1 + " does not equal " + orderTypeChannel2,
			equals(orderTypeChannel1, orderTypeChannel2));
	}

	protected void assertEquals(
		List<OrderTypeChannel> orderTypeChannels1,
		List<OrderTypeChannel> orderTypeChannels2) {

		Assert.assertEquals(
			orderTypeChannels1.size(), orderTypeChannels2.size());

		for (int i = 0; i < orderTypeChannels1.size(); i++) {
			OrderTypeChannel orderTypeChannel1 = orderTypeChannels1.get(i);
			OrderTypeChannel orderTypeChannel2 = orderTypeChannels2.get(i);

			assertEquals(orderTypeChannel1, orderTypeChannel2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<OrderTypeChannel> orderTypeChannels1,
		List<OrderTypeChannel> orderTypeChannels2) {

		Assert.assertEquals(
			orderTypeChannels1.size(), orderTypeChannels2.size());

		for (OrderTypeChannel orderTypeChannel1 : orderTypeChannels1) {
			boolean contains = false;

			for (OrderTypeChannel orderTypeChannel2 : orderTypeChannels2) {
				if (equals(orderTypeChannel1, orderTypeChannel2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				orderTypeChannels2 + " does not contain " + orderTypeChannel1,
				contains);
		}
	}

	protected void assertValid(OrderTypeChannel orderTypeChannel)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (orderTypeChannel.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("channel", additionalAssertFieldName)) {
				if (orderTypeChannel.getChannel() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"channelExternalReferenceCode",
					additionalAssertFieldName)) {

				if (orderTypeChannel.getChannelExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("channelId", additionalAssertFieldName)) {
				if (orderTypeChannel.getChannelId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"orderTypeChannelId", additionalAssertFieldName)) {

				if (orderTypeChannel.getOrderTypeChannelId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"orderTypeExternalReferenceCode",
					additionalAssertFieldName)) {

				if (orderTypeChannel.getOrderTypeExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("orderTypeId", additionalAssertFieldName)) {
				if (orderTypeChannel.getOrderTypeId() == null) {
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

	protected void assertValid(Page<OrderTypeChannel> page) {
		boolean valid = false;

		java.util.Collection<OrderTypeChannel> orderTypeChannels =
			page.getItems();

		int size = orderTypeChannels.size();

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
					com.liferay.headless.commerce.admin.order.dto.v1_0.
						OrderTypeChannel.class)) {

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
		OrderTypeChannel orderTypeChannel1,
		OrderTypeChannel orderTypeChannel2) {

		if (orderTypeChannel1 == orderTypeChannel2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)orderTypeChannel1.getActions(),
						(Map)orderTypeChannel2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("channel", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						orderTypeChannel1.getChannel(),
						orderTypeChannel2.getChannel())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"channelExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						orderTypeChannel1.getChannelExternalReferenceCode(),
						orderTypeChannel2.getChannelExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("channelId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						orderTypeChannel1.getChannelId(),
						orderTypeChannel2.getChannelId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"orderTypeChannelId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						orderTypeChannel1.getOrderTypeChannelId(),
						orderTypeChannel2.getOrderTypeChannelId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"orderTypeExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						orderTypeChannel1.getOrderTypeExternalReferenceCode(),
						orderTypeChannel2.
							getOrderTypeExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("orderTypeId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						orderTypeChannel1.getOrderTypeId(),
						orderTypeChannel2.getOrderTypeId())) {

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

		if (!(_orderTypeChannelResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_orderTypeChannelResource;

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
		OrderTypeChannel orderTypeChannel) {

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

		if (entityFieldName.equals("channel")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("channelExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					orderTypeChannel.getChannelExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("channelId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("orderTypeChannelId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("orderTypeExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					orderTypeChannel.getOrderTypeExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("orderTypeId")) {
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

	protected OrderTypeChannel randomOrderTypeChannel() throws Exception {
		return new OrderTypeChannel() {
			{
				channelExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				channelId = RandomTestUtil.randomLong();
				orderTypeChannelId = RandomTestUtil.randomLong();
				orderTypeExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				orderTypeId = RandomTestUtil.randomLong();
			}
		};
	}

	protected OrderTypeChannel randomIrrelevantOrderTypeChannel()
		throws Exception {

		OrderTypeChannel randomIrrelevantOrderTypeChannel =
			randomOrderTypeChannel();

		return randomIrrelevantOrderTypeChannel;
	}

	protected OrderTypeChannel randomPatchOrderTypeChannel() throws Exception {
		return randomOrderTypeChannel();
	}

	protected OrderTypeChannelResource orderTypeChannelResource;
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
		LogFactoryUtil.getLog(BaseOrderTypeChannelResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.order.resource.v1_0.
		OrderTypeChannelResource _orderTypeChannelResource;

}