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

package com.liferay.headless.commerce.admin.pricing.resource.v2_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.pricing.client.dto.v2_0.Sku;
import com.liferay.headless.commerce.admin.pricing.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.pricing.client.pagination.Page;
import com.liferay.headless.commerce.admin.pricing.client.resource.v2_0.SkuResource;
import com.liferay.headless.commerce.admin.pricing.client.serdes.v2_0.SkuSerDes;
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

		sku.setBasePriceFormatted(regex);
		sku.setBasePromoPriceFormatted(regex);
		sku.setName(regex);

		String json = SkuSerDes.toJSON(sku);

		Assert.assertFalse(json.contains(regex));

		sku = SkuSerDes.toDTO(json);

		Assert.assertEquals(regex, sku.getBasePriceFormatted());
		Assert.assertEquals(regex, sku.getBasePromoPriceFormatted());
		Assert.assertEquals(regex, sku.getName());
	}

	@Test
	public void testGetDiscountSkuSku() throws Exception {
		Sku postSku = testGetDiscountSkuSku_addSku();

		Sku getSku = skuResource.getDiscountSkuSku(
			testGetDiscountSkuSku_getDiscountSkuId());

		assertEquals(postSku, getSku);
		assertValid(getSku);
	}

	protected Long testGetDiscountSkuSku_getDiscountSkuId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Sku testGetDiscountSkuSku_addSku() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetDiscountSkuSku() throws Exception {
		Sku sku = testGraphQLGetDiscountSkuSku_addSku();

		Assert.assertTrue(
			equals(
				sku,
				SkuSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"discountSkuSku",
								new HashMap<String, Object>() {
									{
										put(
											"discountSkuId",
											testGraphQLGetDiscountSkuSku_getDiscountSkuId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/discountSkuSku"))));
	}

	protected Long testGraphQLGetDiscountSkuSku_getDiscountSkuId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetDiscountSkuSkuNotFound() throws Exception {
		Long irrelevantDiscountSkuId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"discountSkuSku",
						new HashMap<String, Object>() {
							{
								put("discountSkuId", irrelevantDiscountSkuId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Sku testGraphQLGetDiscountSkuSku_addSku() throws Exception {
		return testGraphQLSku_addSku();
	}

	@Test
	public void testGetPriceEntryIdSku() throws Exception {
		Sku postSku = testGetPriceEntryIdSku_addSku();

		Sku getSku = skuResource.getPriceEntryIdSku(
			testGetPriceEntryIdSku_getPriceEntryId());

		assertEquals(postSku, getSku);
		assertValid(getSku);
	}

	protected Long testGetPriceEntryIdSku_getPriceEntryId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Sku testGetPriceEntryIdSku_addSku() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetPriceEntryIdSku() throws Exception {
		Sku sku = testGraphQLGetPriceEntryIdSku_addSku();

		Assert.assertTrue(
			equals(
				sku,
				SkuSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"priceEntryIdSku",
								new HashMap<String, Object>() {
									{
										put(
											"priceEntryId",
											testGraphQLGetPriceEntryIdSku_getPriceEntryId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/priceEntryIdSku"))));
	}

	protected Long testGraphQLGetPriceEntryIdSku_getPriceEntryId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetPriceEntryIdSkuNotFound() throws Exception {
		Long irrelevantPriceEntryId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"priceEntryIdSku",
						new HashMap<String, Object>() {
							{
								put("priceEntryId", irrelevantPriceEntryId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Sku testGraphQLGetPriceEntryIdSku_addSku() throws Exception {
		return testGraphQLSku_addSku();
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

			if (Objects.equals("basePrice", additionalAssertFieldName)) {
				if (sku.getBasePrice() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"basePriceFormatted", additionalAssertFieldName)) {

				if (sku.getBasePriceFormatted() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("basePromoPrice", additionalAssertFieldName)) {
				if (sku.getBasePromoPrice() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"basePromoPriceFormatted", additionalAssertFieldName)) {

				if (sku.getBasePromoPriceFormatted() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (sku.getName() == null) {
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
					com.liferay.headless.commerce.admin.pricing.dto.v2_0.Sku.
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

			if (Objects.equals("basePrice", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getBasePrice(), sku2.getBasePrice())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"basePriceFormatted", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						sku1.getBasePriceFormatted(),
						sku2.getBasePriceFormatted())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("basePromoPrice", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getBasePromoPrice(), sku2.getBasePromoPrice())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"basePromoPriceFormatted", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						sku1.getBasePromoPriceFormatted(),
						sku2.getBasePromoPriceFormatted())) {

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

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getName(), sku2.getName())) {
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

		if (entityFieldName.equals("basePrice")) {
			sb.append(String.valueOf(sku.getBasePrice()));

			return sb.toString();
		}

		if (entityFieldName.equals("basePriceFormatted")) {
			sb.append("'");
			sb.append(String.valueOf(sku.getBasePriceFormatted()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("basePromoPrice")) {
			sb.append(String.valueOf(sku.getBasePromoPrice()));

			return sb.toString();
		}

		if (entityFieldName.equals("basePromoPriceFormatted")) {
			sb.append("'");
			sb.append(String.valueOf(sku.getBasePromoPriceFormatted()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(sku.getName()));
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

	protected Sku randomSku() throws Exception {
		return new Sku() {
			{
				basePrice = RandomTestUtil.randomDouble();
				basePriceFormatted = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				basePromoPrice = RandomTestUtil.randomDouble();
				basePromoPriceFormatted = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
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
		com.liferay.headless.commerce.admin.pricing.resource.v2_0.SkuResource
			_skuResource;

}