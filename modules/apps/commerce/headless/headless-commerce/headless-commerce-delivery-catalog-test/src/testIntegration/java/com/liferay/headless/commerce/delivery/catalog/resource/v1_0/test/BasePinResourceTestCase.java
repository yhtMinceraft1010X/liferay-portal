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

import com.liferay.headless.commerce.delivery.catalog.client.dto.v1_0.Pin;
import com.liferay.headless.commerce.delivery.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Page;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.delivery.catalog.client.resource.v1_0.PinResource;
import com.liferay.headless.commerce.delivery.catalog.client.serdes.v1_0.PinSerDes;
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
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public abstract class BasePinResourceTestCase {

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

		_pinResource.setContextCompany(testCompany);

		PinResource.Builder builder = PinResource.builder();

		pinResource = builder.authentication(
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

		Pin pin1 = randomPin();

		String json = objectMapper.writeValueAsString(pin1);

		Pin pin2 = PinSerDes.toDTO(json);

		Assert.assertTrue(equals(pin1, pin2));
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

		Pin pin = randomPin();

		String json1 = objectMapper.writeValueAsString(pin);
		String json2 = PinSerDes.toJSON(pin);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Pin pin = randomPin();

		pin.setSequence(regex);

		String json = PinSerDes.toJSON(pin);

		Assert.assertFalse(json.contains(regex));

		pin = PinSerDes.toDTO(json);

		Assert.assertEquals(regex, pin.getSequence());
	}

	@Test
	public void testGetChannelProductPinsPage() throws Exception {
		Long channelId = testGetChannelProductPinsPage_getChannelId();
		Long irrelevantChannelId =
			testGetChannelProductPinsPage_getIrrelevantChannelId();
		Long productId = testGetChannelProductPinsPage_getProductId();
		Long irrelevantProductId =
			testGetChannelProductPinsPage_getIrrelevantProductId();

		Page<Pin> page = pinResource.getChannelProductPinsPage(
			channelId, productId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if ((irrelevantChannelId != null) && (irrelevantProductId != null)) {
			Pin irrelevantPin = testGetChannelProductPinsPage_addPin(
				irrelevantChannelId, irrelevantProductId,
				randomIrrelevantPin());

			page = pinResource.getChannelProductPinsPage(
				irrelevantChannelId, irrelevantProductId, null, null,
				Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantPin), (List<Pin>)page.getItems());
			assertValid(page);
		}

		Pin pin1 = testGetChannelProductPinsPage_addPin(
			channelId, productId, randomPin());

		Pin pin2 = testGetChannelProductPinsPage_addPin(
			channelId, productId, randomPin());

		page = pinResource.getChannelProductPinsPage(
			channelId, productId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(pin1, pin2), (List<Pin>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetChannelProductPinsPageWithPagination() throws Exception {
		Long channelId = testGetChannelProductPinsPage_getChannelId();
		Long productId = testGetChannelProductPinsPage_getProductId();

		Pin pin1 = testGetChannelProductPinsPage_addPin(
			channelId, productId, randomPin());

		Pin pin2 = testGetChannelProductPinsPage_addPin(
			channelId, productId, randomPin());

		Pin pin3 = testGetChannelProductPinsPage_addPin(
			channelId, productId, randomPin());

		Page<Pin> page1 = pinResource.getChannelProductPinsPage(
			channelId, productId, null, null, Pagination.of(1, 2), null);

		List<Pin> pins1 = (List<Pin>)page1.getItems();

		Assert.assertEquals(pins1.toString(), 2, pins1.size());

		Page<Pin> page2 = pinResource.getChannelProductPinsPage(
			channelId, productId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<Pin> pins2 = (List<Pin>)page2.getItems();

		Assert.assertEquals(pins2.toString(), 1, pins2.size());

		Page<Pin> page3 = pinResource.getChannelProductPinsPage(
			channelId, productId, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(pin1, pin2, pin3), (List<Pin>)page3.getItems());
	}

	@Test
	public void testGetChannelProductPinsPageWithSortDateTime()
		throws Exception {

		testGetChannelProductPinsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, pin1, pin2) -> {
				BeanTestUtil.setProperty(
					pin1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetChannelProductPinsPageWithSortDouble() throws Exception {
		testGetChannelProductPinsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, pin1, pin2) -> {
				BeanTestUtil.setProperty(pin1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(pin2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetChannelProductPinsPageWithSortInteger()
		throws Exception {

		testGetChannelProductPinsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, pin1, pin2) -> {
				BeanTestUtil.setProperty(pin1, entityField.getName(), 0);
				BeanTestUtil.setProperty(pin2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetChannelProductPinsPageWithSortString() throws Exception {
		testGetChannelProductPinsPageWithSort(
			EntityField.Type.STRING,
			(entityField, pin1, pin2) -> {
				Class<?> clazz = pin1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						pin1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						pin2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						pin1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						pin2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						pin1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						pin2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetChannelProductPinsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Pin, Pin, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long channelId = testGetChannelProductPinsPage_getChannelId();
		Long productId = testGetChannelProductPinsPage_getProductId();

		Pin pin1 = randomPin();
		Pin pin2 = randomPin();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, pin1, pin2);
		}

		pin1 = testGetChannelProductPinsPage_addPin(channelId, productId, pin1);

		pin2 = testGetChannelProductPinsPage_addPin(channelId, productId, pin2);

		for (EntityField entityField : entityFields) {
			Page<Pin> ascPage = pinResource.getChannelProductPinsPage(
				channelId, productId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(pin1, pin2), (List<Pin>)ascPage.getItems());

			Page<Pin> descPage = pinResource.getChannelProductPinsPage(
				channelId, productId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(pin2, pin1), (List<Pin>)descPage.getItems());
		}
	}

	protected Pin testGetChannelProductPinsPage_addPin(
			Long channelId, Long productId, Pin pin)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelProductPinsPage_getChannelId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelProductPinsPage_getIrrelevantChannelId()
		throws Exception {

		return null;
	}

	protected Long testGetChannelProductPinsPage_getProductId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelProductPinsPage_getIrrelevantProductId()
		throws Exception {

		return null;
	}

	protected Pin testGraphQLPin_addPin() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(Pin pin, List<Pin> pins) {
		boolean contains = false;

		for (Pin item : pins) {
			if (equals(pin, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(pins + " does not contain " + pin, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Pin pin1, Pin pin2) {
		Assert.assertTrue(pin1 + " does not equal " + pin2, equals(pin1, pin2));
	}

	protected void assertEquals(List<Pin> pins1, List<Pin> pins2) {
		Assert.assertEquals(pins1.size(), pins2.size());

		for (int i = 0; i < pins1.size(); i++) {
			Pin pin1 = pins1.get(i);
			Pin pin2 = pins2.get(i);

			assertEquals(pin1, pin2);
		}
	}

	protected void assertEqualsIgnoringOrder(List<Pin> pins1, List<Pin> pins2) {
		Assert.assertEquals(pins1.size(), pins2.size());

		for (Pin pin1 : pins1) {
			boolean contains = false;

			for (Pin pin2 : pins2) {
				if (equals(pin1, pin2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(pins2 + " does not contain " + pin1, contains);
		}
	}

	protected void assertValid(Pin pin) throws Exception {
		boolean valid = true;

		if (pin.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("mappedProduct", additionalAssertFieldName)) {
				if (pin.getMappedProduct() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("positionX", additionalAssertFieldName)) {
				if (pin.getPositionX() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("positionY", additionalAssertFieldName)) {
				if (pin.getPositionY() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("sequence", additionalAssertFieldName)) {
				if (pin.getSequence() == null) {
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

	protected void assertValid(Page<Pin> page) {
		boolean valid = false;

		java.util.Collection<Pin> pins = page.getItems();

		int size = pins.size();

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
					com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Pin.
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

	protected boolean equals(Pin pin1, Pin pin2) {
		if (pin1 == pin2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(pin1.getId(), pin2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("mappedProduct", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						pin1.getMappedProduct(), pin2.getMappedProduct())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("positionX", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						pin1.getPositionX(), pin2.getPositionX())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("positionY", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						pin1.getPositionY(), pin2.getPositionY())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("sequence", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						pin1.getSequence(), pin2.getSequence())) {

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

		if (!(_pinResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_pinResource;

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
		EntityField entityField, String operator, Pin pin) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("mappedProduct")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("positionX")) {
			sb.append(String.valueOf(pin.getPositionX()));

			return sb.toString();
		}

		if (entityFieldName.equals("positionY")) {
			sb.append(String.valueOf(pin.getPositionY()));

			return sb.toString();
		}

		if (entityFieldName.equals("sequence")) {
			sb.append("'");
			sb.append(String.valueOf(pin.getSequence()));
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

	protected Pin randomPin() throws Exception {
		return new Pin() {
			{
				id = RandomTestUtil.randomLong();
				positionX = RandomTestUtil.randomDouble();
				positionY = RandomTestUtil.randomDouble();
				sequence = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
			}
		};
	}

	protected Pin randomIrrelevantPin() throws Exception {
		Pin randomIrrelevantPin = randomPin();

		return randomIrrelevantPin;
	}

	protected Pin randomPatchPin() throws Exception {
		return randomPin();
	}

	protected PinResource pinResource;
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
		LogFactoryUtil.getLog(BasePinResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private
		com.liferay.headless.commerce.delivery.catalog.resource.v1_0.PinResource
			_pinResource;

}