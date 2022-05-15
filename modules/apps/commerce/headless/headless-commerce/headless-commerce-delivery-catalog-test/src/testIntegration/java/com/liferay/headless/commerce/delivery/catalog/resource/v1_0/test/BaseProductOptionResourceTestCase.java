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

import com.liferay.headless.commerce.delivery.catalog.client.dto.v1_0.ProductOption;
import com.liferay.headless.commerce.delivery.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Page;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.delivery.catalog.client.resource.v1_0.ProductOptionResource;
import com.liferay.headless.commerce.delivery.catalog.client.serdes.v1_0.ProductOptionSerDes;
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
public abstract class BaseProductOptionResourceTestCase {

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

		_productOptionResource.setContextCompany(testCompany);

		ProductOptionResource.Builder builder = ProductOptionResource.builder();

		productOptionResource = builder.authentication(
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

		ProductOption productOption1 = randomProductOption();

		String json = objectMapper.writeValueAsString(productOption1);

		ProductOption productOption2 = ProductOptionSerDes.toDTO(json);

		Assert.assertTrue(equals(productOption1, productOption2));
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

		ProductOption productOption = randomProductOption();

		String json1 = objectMapper.writeValueAsString(productOption);
		String json2 = ProductOptionSerDes.toJSON(productOption);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ProductOption productOption = randomProductOption();

		productOption.setDescription(regex);
		productOption.setFieldType(regex);
		productOption.setKey(regex);
		productOption.setName(regex);

		String json = ProductOptionSerDes.toJSON(productOption);

		Assert.assertFalse(json.contains(regex));

		productOption = ProductOptionSerDes.toDTO(json);

		Assert.assertEquals(regex, productOption.getDescription());
		Assert.assertEquals(regex, productOption.getFieldType());
		Assert.assertEquals(regex, productOption.getKey());
		Assert.assertEquals(regex, productOption.getName());
	}

	@Test
	public void testGetChannelProductOptionsPage() throws Exception {
		Long channelId = testGetChannelProductOptionsPage_getChannelId();
		Long irrelevantChannelId =
			testGetChannelProductOptionsPage_getIrrelevantChannelId();
		Long productId = testGetChannelProductOptionsPage_getProductId();
		Long irrelevantProductId =
			testGetChannelProductOptionsPage_getIrrelevantProductId();

		Page<ProductOption> page =
			productOptionResource.getChannelProductOptionsPage(
				channelId, productId, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if ((irrelevantChannelId != null) && (irrelevantProductId != null)) {
			ProductOption irrelevantProductOption =
				testGetChannelProductOptionsPage_addProductOption(
					irrelevantChannelId, irrelevantProductId,
					randomIrrelevantProductOption());

			page = productOptionResource.getChannelProductOptionsPage(
				irrelevantChannelId, irrelevantProductId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantProductOption),
				(List<ProductOption>)page.getItems());
			assertValid(page);
		}

		ProductOption productOption1 =
			testGetChannelProductOptionsPage_addProductOption(
				channelId, productId, randomProductOption());

		ProductOption productOption2 =
			testGetChannelProductOptionsPage_addProductOption(
				channelId, productId, randomProductOption());

		page = productOptionResource.getChannelProductOptionsPage(
			channelId, productId, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(productOption1, productOption2),
			(List<ProductOption>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetChannelProductOptionsPageWithPagination()
		throws Exception {

		Long channelId = testGetChannelProductOptionsPage_getChannelId();
		Long productId = testGetChannelProductOptionsPage_getProductId();

		ProductOption productOption1 =
			testGetChannelProductOptionsPage_addProductOption(
				channelId, productId, randomProductOption());

		ProductOption productOption2 =
			testGetChannelProductOptionsPage_addProductOption(
				channelId, productId, randomProductOption());

		ProductOption productOption3 =
			testGetChannelProductOptionsPage_addProductOption(
				channelId, productId, randomProductOption());

		Page<ProductOption> page1 =
			productOptionResource.getChannelProductOptionsPage(
				channelId, productId, Pagination.of(1, 2));

		List<ProductOption> productOptions1 =
			(List<ProductOption>)page1.getItems();

		Assert.assertEquals(
			productOptions1.toString(), 2, productOptions1.size());

		Page<ProductOption> page2 =
			productOptionResource.getChannelProductOptionsPage(
				channelId, productId, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<ProductOption> productOptions2 =
			(List<ProductOption>)page2.getItems();

		Assert.assertEquals(
			productOptions2.toString(), 1, productOptions2.size());

		Page<ProductOption> page3 =
			productOptionResource.getChannelProductOptionsPage(
				channelId, productId, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(productOption1, productOption2, productOption3),
			(List<ProductOption>)page3.getItems());
	}

	protected ProductOption testGetChannelProductOptionsPage_addProductOption(
			Long channelId, Long productId, ProductOption productOption)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelProductOptionsPage_getChannelId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelProductOptionsPage_getIrrelevantChannelId()
		throws Exception {

		return null;
	}

	protected Long testGetChannelProductOptionsPage_getProductId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelProductOptionsPage_getIrrelevantProductId()
		throws Exception {

		return null;
	}

	protected ProductOption testGraphQLProductOption_addProductOption()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		ProductOption productOption, List<ProductOption> productOptions) {

		boolean contains = false;

		for (ProductOption item : productOptions) {
			if (equals(productOption, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			productOptions + " does not contain " + productOption, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ProductOption productOption1, ProductOption productOption2) {

		Assert.assertTrue(
			productOption1 + " does not equal " + productOption2,
			equals(productOption1, productOption2));
	}

	protected void assertEquals(
		List<ProductOption> productOptions1,
		List<ProductOption> productOptions2) {

		Assert.assertEquals(productOptions1.size(), productOptions2.size());

		for (int i = 0; i < productOptions1.size(); i++) {
			ProductOption productOption1 = productOptions1.get(i);
			ProductOption productOption2 = productOptions2.get(i);

			assertEquals(productOption1, productOption2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ProductOption> productOptions1,
		List<ProductOption> productOptions2) {

		Assert.assertEquals(productOptions1.size(), productOptions2.size());

		for (ProductOption productOption1 : productOptions1) {
			boolean contains = false;

			for (ProductOption productOption2 : productOptions2) {
				if (equals(productOption1, productOption2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				productOptions2 + " does not contain " + productOption1,
				contains);
		}
	}

	protected void assertValid(ProductOption productOption) throws Exception {
		boolean valid = true;

		if (productOption.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("catalogId", additionalAssertFieldName)) {
				if (productOption.getCatalogId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (productOption.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("fieldType", additionalAssertFieldName)) {
				if (productOption.getFieldType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (productOption.getKey() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (productOption.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("optionId", additionalAssertFieldName)) {
				if (productOption.getOptionId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (productOption.getPriority() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"productOptionValues", additionalAssertFieldName)) {

				if (productOption.getProductOptionValues() == null) {
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

	protected void assertValid(Page<ProductOption> page) {
		boolean valid = false;

		java.util.Collection<ProductOption> productOptions = page.getItems();

		int size = productOptions.size();

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
					com.liferay.headless.commerce.delivery.catalog.dto.v1_0.
						ProductOption.class)) {

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
		ProductOption productOption1, ProductOption productOption2) {

		if (productOption1 == productOption2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("catalogId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productOption1.getCatalogId(),
						productOption2.getCatalogId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productOption1.getDescription(),
						productOption2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("fieldType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productOption1.getFieldType(),
						productOption2.getFieldType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productOption1.getId(), productOption2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productOption1.getKey(), productOption2.getKey())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productOption1.getName(), productOption2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("optionId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productOption1.getOptionId(),
						productOption2.getOptionId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productOption1.getPriority(),
						productOption2.getPriority())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"productOptionValues", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productOption1.getProductOptionValues(),
						productOption2.getProductOptionValues())) {

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

		if (!(_productOptionResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_productOptionResource;

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
		EntityField entityField, String operator, ProductOption productOption) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("catalogId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(productOption.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("fieldType")) {
			sb.append("'");
			sb.append(String.valueOf(productOption.getFieldType()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("key")) {
			sb.append("'");
			sb.append(String.valueOf(productOption.getKey()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(productOption.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("optionId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("priority")) {
			sb.append(String.valueOf(productOption.getPriority()));

			return sb.toString();
		}

		if (entityFieldName.equals("productOptionValues")) {
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

	protected ProductOption randomProductOption() throws Exception {
		return new ProductOption() {
			{
				catalogId = RandomTestUtil.randomLong();
				description = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				fieldType = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				key = StringUtil.toLowerCase(RandomTestUtil.randomString());
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				optionId = RandomTestUtil.randomLong();
				priority = RandomTestUtil.randomDouble();
			}
		};
	}

	protected ProductOption randomIrrelevantProductOption() throws Exception {
		ProductOption randomIrrelevantProductOption = randomProductOption();

		return randomIrrelevantProductOption;
	}

	protected ProductOption randomPatchProductOption() throws Exception {
		return randomProductOption();
	}

	protected ProductOptionResource productOptionResource;
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
		LogFactoryUtil.getLog(BaseProductOptionResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.delivery.catalog.resource.v1_0.
		ProductOptionResource _productOptionResource;

}