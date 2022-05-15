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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.ProductShippingConfiguration;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.ProductShippingConfigurationResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.ProductShippingConfigurationSerDes;
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
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
public abstract class BaseProductShippingConfigurationResourceTestCase {

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

		_productShippingConfigurationResource.setContextCompany(testCompany);

		ProductShippingConfigurationResource.Builder builder =
			ProductShippingConfigurationResource.builder();

		productShippingConfigurationResource = builder.authentication(
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

		ProductShippingConfiguration productShippingConfiguration1 =
			randomProductShippingConfiguration();

		String json = objectMapper.writeValueAsString(
			productShippingConfiguration1);

		ProductShippingConfiguration productShippingConfiguration2 =
			ProductShippingConfigurationSerDes.toDTO(json);

		Assert.assertTrue(
			equals(
				productShippingConfiguration1, productShippingConfiguration2));
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

		ProductShippingConfiguration productShippingConfiguration =
			randomProductShippingConfiguration();

		String json1 = objectMapper.writeValueAsString(
			productShippingConfiguration);
		String json2 = ProductShippingConfigurationSerDes.toJSON(
			productShippingConfiguration);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ProductShippingConfiguration productShippingConfiguration =
			randomProductShippingConfiguration();

		String json = ProductShippingConfigurationSerDes.toJSON(
			productShippingConfiguration);

		Assert.assertFalse(json.contains(regex));

		productShippingConfiguration = ProductShippingConfigurationSerDes.toDTO(
			json);
	}

	@Test
	public void testGetProductByExternalReferenceCodeShippingConfiguration()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetProductByExternalReferenceCodeShippingConfiguration()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testGraphQLGetProductByExternalReferenceCodeShippingConfigurationNotFound()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testPatchProductByExternalReferenceCodeShippingConfiguration()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testGetProductIdShippingConfiguration() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetProductIdShippingConfiguration()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testGraphQLGetProductIdShippingConfigurationNotFound()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testPatchProductIdShippingConfiguration() throws Exception {
		Assert.assertTrue(false);
	}

	protected void assertContains(
		ProductShippingConfiguration productShippingConfiguration,
		List<ProductShippingConfiguration> productShippingConfigurations) {

		boolean contains = false;

		for (ProductShippingConfiguration item :
				productShippingConfigurations) {

			if (equals(productShippingConfiguration, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			productShippingConfigurations + " does not contain " +
				productShippingConfiguration,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ProductShippingConfiguration productShippingConfiguration1,
		ProductShippingConfiguration productShippingConfiguration2) {

		Assert.assertTrue(
			productShippingConfiguration1 + " does not equal " +
				productShippingConfiguration2,
			equals(
				productShippingConfiguration1, productShippingConfiguration2));
	}

	protected void assertEquals(
		List<ProductShippingConfiguration> productShippingConfigurations1,
		List<ProductShippingConfiguration> productShippingConfigurations2) {

		Assert.assertEquals(
			productShippingConfigurations1.size(),
			productShippingConfigurations2.size());

		for (int i = 0; i < productShippingConfigurations1.size(); i++) {
			ProductShippingConfiguration productShippingConfiguration1 =
				productShippingConfigurations1.get(i);
			ProductShippingConfiguration productShippingConfiguration2 =
				productShippingConfigurations2.get(i);

			assertEquals(
				productShippingConfiguration1, productShippingConfiguration2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ProductShippingConfiguration> productShippingConfigurations1,
		List<ProductShippingConfiguration> productShippingConfigurations2) {

		Assert.assertEquals(
			productShippingConfigurations1.size(),
			productShippingConfigurations2.size());

		for (ProductShippingConfiguration productShippingConfiguration1 :
				productShippingConfigurations1) {

			boolean contains = false;

			for (ProductShippingConfiguration productShippingConfiguration2 :
					productShippingConfigurations2) {

				if (equals(
						productShippingConfiguration1,
						productShippingConfiguration2)) {

					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				productShippingConfigurations2 + " does not contain " +
					productShippingConfiguration1,
				contains);
		}
	}

	protected void assertValid(
			ProductShippingConfiguration productShippingConfiguration)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("depth", additionalAssertFieldName)) {
				if (productShippingConfiguration.getDepth() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("freeShipping", additionalAssertFieldName)) {
				if (productShippingConfiguration.getFreeShipping() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("height", additionalAssertFieldName)) {
				if (productShippingConfiguration.getHeight() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("shippable", additionalAssertFieldName)) {
				if (productShippingConfiguration.getShippable() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"shippingExtraPrice", additionalAssertFieldName)) {

				if (productShippingConfiguration.getShippingExtraPrice() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"shippingSeparately", additionalAssertFieldName)) {

				if (productShippingConfiguration.getShippingSeparately() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("weight", additionalAssertFieldName)) {
				if (productShippingConfiguration.getWeight() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("width", additionalAssertFieldName)) {
				if (productShippingConfiguration.getWidth() == null) {
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

	protected void assertValid(Page<ProductShippingConfiguration> page) {
		boolean valid = false;

		java.util.Collection<ProductShippingConfiguration>
			productShippingConfigurations = page.getItems();

		int size = productShippingConfigurations.size();

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
						ProductShippingConfiguration.class)) {

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
		ProductShippingConfiguration productShippingConfiguration1,
		ProductShippingConfiguration productShippingConfiguration2) {

		if (productShippingConfiguration1 == productShippingConfiguration2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("depth", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productShippingConfiguration1.getDepth(),
						productShippingConfiguration2.getDepth())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("freeShipping", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productShippingConfiguration1.getFreeShipping(),
						productShippingConfiguration2.getFreeShipping())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("height", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productShippingConfiguration1.getHeight(),
						productShippingConfiguration2.getHeight())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("shippable", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productShippingConfiguration1.getShippable(),
						productShippingConfiguration2.getShippable())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"shippingExtraPrice", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productShippingConfiguration1.getShippingExtraPrice(),
						productShippingConfiguration2.
							getShippingExtraPrice())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"shippingSeparately", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productShippingConfiguration1.getShippingSeparately(),
						productShippingConfiguration2.
							getShippingSeparately())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("weight", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productShippingConfiguration1.getWeight(),
						productShippingConfiguration2.getWeight())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("width", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productShippingConfiguration1.getWidth(),
						productShippingConfiguration2.getWidth())) {

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

		if (!(_productShippingConfigurationResource instanceof
				EntityModelResource)) {

			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_productShippingConfigurationResource;

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
		ProductShippingConfiguration productShippingConfiguration) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("depth")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("freeShipping")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("height")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("shippable")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("shippingExtraPrice")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("shippingSeparately")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("weight")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("width")) {
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

	protected ProductShippingConfiguration randomProductShippingConfiguration()
		throws Exception {

		return new ProductShippingConfiguration() {
			{
				freeShipping = RandomTestUtil.randomBoolean();
				shippable = RandomTestUtil.randomBoolean();
				shippingSeparately = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected ProductShippingConfiguration
			randomIrrelevantProductShippingConfiguration()
		throws Exception {

		ProductShippingConfiguration
			randomIrrelevantProductShippingConfiguration =
				randomProductShippingConfiguration();

		return randomIrrelevantProductShippingConfiguration;
	}

	protected ProductShippingConfiguration
			randomPatchProductShippingConfiguration()
		throws Exception {

		return randomProductShippingConfiguration();
	}

	protected ProductShippingConfigurationResource
		productShippingConfigurationResource;
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
		LogFactoryUtil.getLog(
			BaseProductShippingConfigurationResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		ProductShippingConfigurationResource
			_productShippingConfigurationResource;

}