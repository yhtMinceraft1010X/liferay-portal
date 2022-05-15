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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.ProductConfiguration;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.ProductConfigurationResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.ProductConfigurationSerDes;
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
public abstract class BaseProductConfigurationResourceTestCase {

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

		_productConfigurationResource.setContextCompany(testCompany);

		ProductConfigurationResource.Builder builder =
			ProductConfigurationResource.builder();

		productConfigurationResource = builder.authentication(
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

		ProductConfiguration productConfiguration1 =
			randomProductConfiguration();

		String json = objectMapper.writeValueAsString(productConfiguration1);

		ProductConfiguration productConfiguration2 =
			ProductConfigurationSerDes.toDTO(json);

		Assert.assertTrue(equals(productConfiguration1, productConfiguration2));
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

		ProductConfiguration productConfiguration =
			randomProductConfiguration();

		String json1 = objectMapper.writeValueAsString(productConfiguration);
		String json2 = ProductConfigurationSerDes.toJSON(productConfiguration);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ProductConfiguration productConfiguration =
			randomProductConfiguration();

		productConfiguration.setInventoryEngine(regex);
		productConfiguration.setLowStockAction(regex);

		String json = ProductConfigurationSerDes.toJSON(productConfiguration);

		Assert.assertFalse(json.contains(regex));

		productConfiguration = ProductConfigurationSerDes.toDTO(json);

		Assert.assertEquals(regex, productConfiguration.getInventoryEngine());
		Assert.assertEquals(regex, productConfiguration.getLowStockAction());
	}

	@Test
	public void testGetProductByExternalReferenceCodeConfiguration()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetProductByExternalReferenceCodeConfiguration()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testGraphQLGetProductByExternalReferenceCodeConfigurationNotFound()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testPatchProductByExternalReferenceCodeConfiguration()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testGetProductIdConfiguration() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetProductIdConfiguration() throws Exception {
		Assert.assertTrue(true);
	}

	@Test
	public void testGraphQLGetProductIdConfigurationNotFound()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testPatchProductIdConfiguration() throws Exception {
		Assert.assertTrue(false);
	}

	protected void assertContains(
		ProductConfiguration productConfiguration,
		List<ProductConfiguration> productConfigurations) {

		boolean contains = false;

		for (ProductConfiguration item : productConfigurations) {
			if (equals(productConfiguration, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			productConfigurations + " does not contain " + productConfiguration,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ProductConfiguration productConfiguration1,
		ProductConfiguration productConfiguration2) {

		Assert.assertTrue(
			productConfiguration1 + " does not equal " + productConfiguration2,
			equals(productConfiguration1, productConfiguration2));
	}

	protected void assertEquals(
		List<ProductConfiguration> productConfigurations1,
		List<ProductConfiguration> productConfigurations2) {

		Assert.assertEquals(
			productConfigurations1.size(), productConfigurations2.size());

		for (int i = 0; i < productConfigurations1.size(); i++) {
			ProductConfiguration productConfiguration1 =
				productConfigurations1.get(i);
			ProductConfiguration productConfiguration2 =
				productConfigurations2.get(i);

			assertEquals(productConfiguration1, productConfiguration2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ProductConfiguration> productConfigurations1,
		List<ProductConfiguration> productConfigurations2) {

		Assert.assertEquals(
			productConfigurations1.size(), productConfigurations2.size());

		for (ProductConfiguration productConfiguration1 :
				productConfigurations1) {

			boolean contains = false;

			for (ProductConfiguration productConfiguration2 :
					productConfigurations2) {

				if (equals(productConfiguration1, productConfiguration2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				productConfigurations2 + " does not contain " +
					productConfiguration1,
				contains);
		}
	}

	protected void assertValid(ProductConfiguration productConfiguration)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("allowBackOrder", additionalAssertFieldName)) {
				if (productConfiguration.getAllowBackOrder() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"allowedOrderQuantities", additionalAssertFieldName)) {

				if (productConfiguration.getAllowedOrderQuantities() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"displayAvailability", additionalAssertFieldName)) {

				if (productConfiguration.getDisplayAvailability() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"displayStockQuantity", additionalAssertFieldName)) {

				if (productConfiguration.getDisplayStockQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("inventoryEngine", additionalAssertFieldName)) {
				if (productConfiguration.getInventoryEngine() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("lowStockAction", additionalAssertFieldName)) {
				if (productConfiguration.getLowStockAction() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("maxOrderQuantity", additionalAssertFieldName)) {
				if (productConfiguration.getMaxOrderQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("minOrderQuantity", additionalAssertFieldName)) {
				if (productConfiguration.getMinOrderQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("minStockQuantity", additionalAssertFieldName)) {
				if (productConfiguration.getMinStockQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"multipleOrderQuantity", additionalAssertFieldName)) {

				if (productConfiguration.getMultipleOrderQuantity() == null) {
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

	protected void assertValid(Page<ProductConfiguration> page) {
		boolean valid = false;

		java.util.Collection<ProductConfiguration> productConfigurations =
			page.getItems();

		int size = productConfigurations.size();

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
						ProductConfiguration.class)) {

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
		ProductConfiguration productConfiguration1,
		ProductConfiguration productConfiguration2) {

		if (productConfiguration1 == productConfiguration2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("allowBackOrder", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productConfiguration1.getAllowBackOrder(),
						productConfiguration2.getAllowBackOrder())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"allowedOrderQuantities", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productConfiguration1.getAllowedOrderQuantities(),
						productConfiguration2.getAllowedOrderQuantities())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"displayAvailability", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productConfiguration1.getDisplayAvailability(),
						productConfiguration2.getDisplayAvailability())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"displayStockQuantity", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productConfiguration1.getDisplayStockQuantity(),
						productConfiguration2.getDisplayStockQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("inventoryEngine", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productConfiguration1.getInventoryEngine(),
						productConfiguration2.getInventoryEngine())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("lowStockAction", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productConfiguration1.getLowStockAction(),
						productConfiguration2.getLowStockAction())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("maxOrderQuantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productConfiguration1.getMaxOrderQuantity(),
						productConfiguration2.getMaxOrderQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("minOrderQuantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productConfiguration1.getMinOrderQuantity(),
						productConfiguration2.getMinOrderQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("minStockQuantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productConfiguration1.getMinStockQuantity(),
						productConfiguration2.getMinStockQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"multipleOrderQuantity", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productConfiguration1.getMultipleOrderQuantity(),
						productConfiguration2.getMultipleOrderQuantity())) {

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

		if (!(_productConfigurationResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_productConfigurationResource;

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
		ProductConfiguration productConfiguration) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("allowBackOrder")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("allowedOrderQuantities")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("displayAvailability")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("displayStockQuantity")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("inventoryEngine")) {
			sb.append("'");
			sb.append(
				String.valueOf(productConfiguration.getInventoryEngine()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("lowStockAction")) {
			sb.append("'");
			sb.append(String.valueOf(productConfiguration.getLowStockAction()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("maxOrderQuantity")) {
			sb.append(
				String.valueOf(productConfiguration.getMaxOrderQuantity()));

			return sb.toString();
		}

		if (entityFieldName.equals("minOrderQuantity")) {
			sb.append(
				String.valueOf(productConfiguration.getMinOrderQuantity()));

			return sb.toString();
		}

		if (entityFieldName.equals("minStockQuantity")) {
			sb.append(
				String.valueOf(productConfiguration.getMinStockQuantity()));

			return sb.toString();
		}

		if (entityFieldName.equals("multipleOrderQuantity")) {
			sb.append(
				String.valueOf(
					productConfiguration.getMultipleOrderQuantity()));

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

	protected ProductConfiguration randomProductConfiguration()
		throws Exception {

		return new ProductConfiguration() {
			{
				allowBackOrder = RandomTestUtil.randomBoolean();
				displayAvailability = RandomTestUtil.randomBoolean();
				displayStockQuantity = RandomTestUtil.randomBoolean();
				inventoryEngine = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				lowStockAction = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				maxOrderQuantity = RandomTestUtil.randomInt();
				minOrderQuantity = RandomTestUtil.randomInt();
				minStockQuantity = RandomTestUtil.randomInt();
				multipleOrderQuantity = RandomTestUtil.randomInt();
			}
		};
	}

	protected ProductConfiguration randomIrrelevantProductConfiguration()
		throws Exception {

		ProductConfiguration randomIrrelevantProductConfiguration =
			randomProductConfiguration();

		return randomIrrelevantProductConfiguration;
	}

	protected ProductConfiguration randomPatchProductConfiguration()
		throws Exception {

		return randomProductConfiguration();
	}

	protected ProductConfigurationResource productConfigurationResource;
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
		LogFactoryUtil.getLog(BaseProductConfigurationResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		ProductConfigurationResource _productConfigurationResource;

}