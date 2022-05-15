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

import com.liferay.headless.commerce.admin.order.client.dto.v1_0.TermOrderType;
import com.liferay.headless.commerce.admin.order.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.order.client.pagination.Page;
import com.liferay.headless.commerce.admin.order.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.order.client.resource.v1_0.TermOrderTypeResource;
import com.liferay.headless.commerce.admin.order.client.serdes.v1_0.TermOrderTypeSerDes;
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
 * @author Alessio Antonio Rendina
 * @generated
 */
@Generated("")
public abstract class BaseTermOrderTypeResourceTestCase {

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

		_termOrderTypeResource.setContextCompany(testCompany);

		TermOrderTypeResource.Builder builder = TermOrderTypeResource.builder();

		termOrderTypeResource = builder.authentication(
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

		TermOrderType termOrderType1 = randomTermOrderType();

		String json = objectMapper.writeValueAsString(termOrderType1);

		TermOrderType termOrderType2 = TermOrderTypeSerDes.toDTO(json);

		Assert.assertTrue(equals(termOrderType1, termOrderType2));
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

		TermOrderType termOrderType = randomTermOrderType();

		String json1 = objectMapper.writeValueAsString(termOrderType);
		String json2 = TermOrderTypeSerDes.toJSON(termOrderType);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		TermOrderType termOrderType = randomTermOrderType();

		termOrderType.setOrderTypeExternalReferenceCode(regex);
		termOrderType.setTermExternalReferenceCode(regex);

		String json = TermOrderTypeSerDes.toJSON(termOrderType);

		Assert.assertFalse(json.contains(regex));

		termOrderType = TermOrderTypeSerDes.toDTO(json);

		Assert.assertEquals(
			regex, termOrderType.getOrderTypeExternalReferenceCode());
		Assert.assertEquals(
			regex, termOrderType.getTermExternalReferenceCode());
	}

	@Test
	public void testDeleteTermOrderType() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLDeleteTermOrderType() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetTermByExternalReferenceCodeTermOrderTypesPage()
		throws Exception {

		String externalReferenceCode =
			testGetTermByExternalReferenceCodeTermOrderTypesPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetTermByExternalReferenceCodeTermOrderTypesPage_getIrrelevantExternalReferenceCode();

		Page<TermOrderType> page =
			termOrderTypeResource.
				getTermByExternalReferenceCodeTermOrderTypesPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			TermOrderType irrelevantTermOrderType =
				testGetTermByExternalReferenceCodeTermOrderTypesPage_addTermOrderType(
					irrelevantExternalReferenceCode,
					randomIrrelevantTermOrderType());

			page =
				termOrderTypeResource.
					getTermByExternalReferenceCodeTermOrderTypesPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantTermOrderType),
				(List<TermOrderType>)page.getItems());
			assertValid(page);
		}

		TermOrderType termOrderType1 =
			testGetTermByExternalReferenceCodeTermOrderTypesPage_addTermOrderType(
				externalReferenceCode, randomTermOrderType());

		TermOrderType termOrderType2 =
			testGetTermByExternalReferenceCodeTermOrderTypesPage_addTermOrderType(
				externalReferenceCode, randomTermOrderType());

		page =
			termOrderTypeResource.
				getTermByExternalReferenceCodeTermOrderTypesPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(termOrderType1, termOrderType2),
			(List<TermOrderType>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetTermByExternalReferenceCodeTermOrderTypesPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetTermByExternalReferenceCodeTermOrderTypesPage_getExternalReferenceCode();

		TermOrderType termOrderType1 =
			testGetTermByExternalReferenceCodeTermOrderTypesPage_addTermOrderType(
				externalReferenceCode, randomTermOrderType());

		TermOrderType termOrderType2 =
			testGetTermByExternalReferenceCodeTermOrderTypesPage_addTermOrderType(
				externalReferenceCode, randomTermOrderType());

		TermOrderType termOrderType3 =
			testGetTermByExternalReferenceCodeTermOrderTypesPage_addTermOrderType(
				externalReferenceCode, randomTermOrderType());

		Page<TermOrderType> page1 =
			termOrderTypeResource.
				getTermByExternalReferenceCodeTermOrderTypesPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<TermOrderType> termOrderTypes1 =
			(List<TermOrderType>)page1.getItems();

		Assert.assertEquals(
			termOrderTypes1.toString(), 2, termOrderTypes1.size());

		Page<TermOrderType> page2 =
			termOrderTypeResource.
				getTermByExternalReferenceCodeTermOrderTypesPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<TermOrderType> termOrderTypes2 =
			(List<TermOrderType>)page2.getItems();

		Assert.assertEquals(
			termOrderTypes2.toString(), 1, termOrderTypes2.size());

		Page<TermOrderType> page3 =
			termOrderTypeResource.
				getTermByExternalReferenceCodeTermOrderTypesPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(termOrderType1, termOrderType2, termOrderType3),
			(List<TermOrderType>)page3.getItems());
	}

	protected TermOrderType
			testGetTermByExternalReferenceCodeTermOrderTypesPage_addTermOrderType(
				String externalReferenceCode, TermOrderType termOrderType)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetTermByExternalReferenceCodeTermOrderTypesPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetTermByExternalReferenceCodeTermOrderTypesPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostTermByExternalReferenceCodeTermOrderType()
		throws Exception {

		TermOrderType randomTermOrderType = randomTermOrderType();

		TermOrderType postTermOrderType =
			testPostTermByExternalReferenceCodeTermOrderType_addTermOrderType(
				randomTermOrderType);

		assertEquals(randomTermOrderType, postTermOrderType);
		assertValid(postTermOrderType);
	}

	protected TermOrderType
			testPostTermByExternalReferenceCodeTermOrderType_addTermOrderType(
				TermOrderType termOrderType)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetTermIdTermOrderTypesPage() throws Exception {
		Long id = testGetTermIdTermOrderTypesPage_getId();
		Long irrelevantId = testGetTermIdTermOrderTypesPage_getIrrelevantId();

		Page<TermOrderType> page =
			termOrderTypeResource.getTermIdTermOrderTypesPage(
				id, null, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			TermOrderType irrelevantTermOrderType =
				testGetTermIdTermOrderTypesPage_addTermOrderType(
					irrelevantId, randomIrrelevantTermOrderType());

			page = termOrderTypeResource.getTermIdTermOrderTypesPage(
				irrelevantId, null, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantTermOrderType),
				(List<TermOrderType>)page.getItems());
			assertValid(page);
		}

		TermOrderType termOrderType1 =
			testGetTermIdTermOrderTypesPage_addTermOrderType(
				id, randomTermOrderType());

		TermOrderType termOrderType2 =
			testGetTermIdTermOrderTypesPage_addTermOrderType(
				id, randomTermOrderType());

		page = termOrderTypeResource.getTermIdTermOrderTypesPage(
			id, null, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(termOrderType1, termOrderType2),
			(List<TermOrderType>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetTermIdTermOrderTypesPageWithPagination()
		throws Exception {

		Long id = testGetTermIdTermOrderTypesPage_getId();

		TermOrderType termOrderType1 =
			testGetTermIdTermOrderTypesPage_addTermOrderType(
				id, randomTermOrderType());

		TermOrderType termOrderType2 =
			testGetTermIdTermOrderTypesPage_addTermOrderType(
				id, randomTermOrderType());

		TermOrderType termOrderType3 =
			testGetTermIdTermOrderTypesPage_addTermOrderType(
				id, randomTermOrderType());

		Page<TermOrderType> page1 =
			termOrderTypeResource.getTermIdTermOrderTypesPage(
				id, null, Pagination.of(1, 2));

		List<TermOrderType> termOrderTypes1 =
			(List<TermOrderType>)page1.getItems();

		Assert.assertEquals(
			termOrderTypes1.toString(), 2, termOrderTypes1.size());

		Page<TermOrderType> page2 =
			termOrderTypeResource.getTermIdTermOrderTypesPage(
				id, null, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<TermOrderType> termOrderTypes2 =
			(List<TermOrderType>)page2.getItems();

		Assert.assertEquals(
			termOrderTypes2.toString(), 1, termOrderTypes2.size());

		Page<TermOrderType> page3 =
			termOrderTypeResource.getTermIdTermOrderTypesPage(
				id, null, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(termOrderType1, termOrderType2, termOrderType3),
			(List<TermOrderType>)page3.getItems());
	}

	protected TermOrderType testGetTermIdTermOrderTypesPage_addTermOrderType(
			Long id, TermOrderType termOrderType)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetTermIdTermOrderTypesPage_getId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetTermIdTermOrderTypesPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostTermIdTermOrderType() throws Exception {
		TermOrderType randomTermOrderType = randomTermOrderType();

		TermOrderType postTermOrderType =
			testPostTermIdTermOrderType_addTermOrderType(randomTermOrderType);

		assertEquals(randomTermOrderType, postTermOrderType);
		assertValid(postTermOrderType);
	}

	protected TermOrderType testPostTermIdTermOrderType_addTermOrderType(
			TermOrderType termOrderType)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		TermOrderType termOrderType, List<TermOrderType> termOrderTypes) {

		boolean contains = false;

		for (TermOrderType item : termOrderTypes) {
			if (equals(termOrderType, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			termOrderTypes + " does not contain " + termOrderType, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		TermOrderType termOrderType1, TermOrderType termOrderType2) {

		Assert.assertTrue(
			termOrderType1 + " does not equal " + termOrderType2,
			equals(termOrderType1, termOrderType2));
	}

	protected void assertEquals(
		List<TermOrderType> termOrderTypes1,
		List<TermOrderType> termOrderTypes2) {

		Assert.assertEquals(termOrderTypes1.size(), termOrderTypes2.size());

		for (int i = 0; i < termOrderTypes1.size(); i++) {
			TermOrderType termOrderType1 = termOrderTypes1.get(i);
			TermOrderType termOrderType2 = termOrderTypes2.get(i);

			assertEquals(termOrderType1, termOrderType2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<TermOrderType> termOrderTypes1,
		List<TermOrderType> termOrderTypes2) {

		Assert.assertEquals(termOrderTypes1.size(), termOrderTypes2.size());

		for (TermOrderType termOrderType1 : termOrderTypes1) {
			boolean contains = false;

			for (TermOrderType termOrderType2 : termOrderTypes2) {
				if (equals(termOrderType1, termOrderType2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				termOrderTypes2 + " does not contain " + termOrderType1,
				contains);
		}
	}

	protected void assertValid(TermOrderType termOrderType) throws Exception {
		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (termOrderType.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("orderType", additionalAssertFieldName)) {
				if (termOrderType.getOrderType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"orderTypeExternalReferenceCode",
					additionalAssertFieldName)) {

				if (termOrderType.getOrderTypeExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("orderTypeId", additionalAssertFieldName)) {
				if (termOrderType.getOrderTypeId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"termExternalReferenceCode", additionalAssertFieldName)) {

				if (termOrderType.getTermExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("termId", additionalAssertFieldName)) {
				if (termOrderType.getTermId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("termOrderTypeId", additionalAssertFieldName)) {
				if (termOrderType.getTermOrderTypeId() == null) {
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

	protected void assertValid(Page<TermOrderType> page) {
		boolean valid = false;

		java.util.Collection<TermOrderType> termOrderTypes = page.getItems();

		int size = termOrderTypes.size();

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
						TermOrderType.class)) {

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
		TermOrderType termOrderType1, TermOrderType termOrderType2) {

		if (termOrderType1 == termOrderType2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)termOrderType1.getActions(),
						(Map)termOrderType2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("orderType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						termOrderType1.getOrderType(),
						termOrderType2.getOrderType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"orderTypeExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						termOrderType1.getOrderTypeExternalReferenceCode(),
						termOrderType2.getOrderTypeExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("orderTypeId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						termOrderType1.getOrderTypeId(),
						termOrderType2.getOrderTypeId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"termExternalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						termOrderType1.getTermExternalReferenceCode(),
						termOrderType2.getTermExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("termId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						termOrderType1.getTermId(),
						termOrderType2.getTermId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("termOrderTypeId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						termOrderType1.getTermOrderTypeId(),
						termOrderType2.getTermOrderTypeId())) {

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

		if (!(_termOrderTypeResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_termOrderTypeResource;

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
		EntityField entityField, String operator, TermOrderType termOrderType) {

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

		if (entityFieldName.equals("orderType")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("orderTypeExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					termOrderType.getOrderTypeExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("orderTypeId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("termExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(termOrderType.getTermExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("termId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("termOrderTypeId")) {
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

	protected TermOrderType randomTermOrderType() throws Exception {
		return new TermOrderType() {
			{
				orderTypeExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				orderTypeId = RandomTestUtil.randomLong();
				termExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				termId = RandomTestUtil.randomLong();
				termOrderTypeId = RandomTestUtil.randomLong();
			}
		};
	}

	protected TermOrderType randomIrrelevantTermOrderType() throws Exception {
		TermOrderType randomIrrelevantTermOrderType = randomTermOrderType();

		return randomIrrelevantTermOrderType;
	}

	protected TermOrderType randomPatchTermOrderType() throws Exception {
		return randomTermOrderType();
	}

	protected TermOrderTypeResource termOrderTypeResource;
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
		LogFactoryUtil.getLog(BaseTermOrderTypeResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.order.resource.v1_0.
		TermOrderTypeResource _termOrderTypeResource;

}