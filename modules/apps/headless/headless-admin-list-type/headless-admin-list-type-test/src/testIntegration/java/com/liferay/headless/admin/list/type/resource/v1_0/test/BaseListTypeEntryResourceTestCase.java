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

package com.liferay.headless.admin.list.type.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.admin.list.type.client.dto.v1_0.ListTypeEntry;
import com.liferay.headless.admin.list.type.client.http.HttpInvoker;
import com.liferay.headless.admin.list.type.client.pagination.Page;
import com.liferay.headless.admin.list.type.client.pagination.Pagination;
import com.liferay.headless.admin.list.type.client.resource.v1_0.ListTypeEntryResource;
import com.liferay.headless.admin.list.type.client.serdes.v1_0.ListTypeEntrySerDes;
import com.liferay.petra.function.UnsafeTriConsumer;
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
import com.liferay.portal.search.test.util.SearchTestRule;
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
 * @author Gabriel Albuquerque
 * @generated
 */
@Generated("")
public abstract class BaseListTypeEntryResourceTestCase {

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

		_listTypeEntryResource.setContextCompany(testCompany);

		ListTypeEntryResource.Builder builder = ListTypeEntryResource.builder();

		listTypeEntryResource = builder.authentication(
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

		ListTypeEntry listTypeEntry1 = randomListTypeEntry();

		String json = objectMapper.writeValueAsString(listTypeEntry1);

		ListTypeEntry listTypeEntry2 = ListTypeEntrySerDes.toDTO(json);

		Assert.assertTrue(equals(listTypeEntry1, listTypeEntry2));
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

		ListTypeEntry listTypeEntry = randomListTypeEntry();

		String json1 = objectMapper.writeValueAsString(listTypeEntry);
		String json2 = ListTypeEntrySerDes.toJSON(listTypeEntry);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ListTypeEntry listTypeEntry = randomListTypeEntry();

		listTypeEntry.setKey(regex);
		listTypeEntry.setName(regex);
		listTypeEntry.setType(regex);

		String json = ListTypeEntrySerDes.toJSON(listTypeEntry);

		Assert.assertFalse(json.contains(regex));

		listTypeEntry = ListTypeEntrySerDes.toDTO(json);

		Assert.assertEquals(regex, listTypeEntry.getKey());
		Assert.assertEquals(regex, listTypeEntry.getName());
		Assert.assertEquals(regex, listTypeEntry.getType());
	}

	@Test
	public void testGetListTypeDefinitionListTypeEntriesPage()
		throws Exception {

		Long listTypeDefinitionId =
			testGetListTypeDefinitionListTypeEntriesPage_getListTypeDefinitionId();
		Long irrelevantListTypeDefinitionId =
			testGetListTypeDefinitionListTypeEntriesPage_getIrrelevantListTypeDefinitionId();

		Page<ListTypeEntry> page =
			listTypeEntryResource.getListTypeDefinitionListTypeEntriesPage(
				listTypeDefinitionId, null, null, null, Pagination.of(1, 10),
				null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantListTypeDefinitionId != null) {
			ListTypeEntry irrelevantListTypeEntry =
				testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
					irrelevantListTypeDefinitionId,
					randomIrrelevantListTypeEntry());

			page =
				listTypeEntryResource.getListTypeDefinitionListTypeEntriesPage(
					irrelevantListTypeDefinitionId, null, null, null,
					Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantListTypeEntry),
				(List<ListTypeEntry>)page.getItems());
			assertValid(page);
		}

		ListTypeEntry listTypeEntry1 =
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				listTypeDefinitionId, randomListTypeEntry());

		ListTypeEntry listTypeEntry2 =
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				listTypeDefinitionId, randomListTypeEntry());

		page = listTypeEntryResource.getListTypeDefinitionListTypeEntriesPage(
			listTypeDefinitionId, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(listTypeEntry1, listTypeEntry2),
			(List<ListTypeEntry>)page.getItems());
		assertValid(page);

		listTypeEntryResource.deleteListTypeEntry(listTypeEntry1.getId());

		listTypeEntryResource.deleteListTypeEntry(listTypeEntry2.getId());
	}

	@Test
	public void testGetListTypeDefinitionListTypeEntriesPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long listTypeDefinitionId =
			testGetListTypeDefinitionListTypeEntriesPage_getListTypeDefinitionId();

		ListTypeEntry listTypeEntry1 = randomListTypeEntry();

		listTypeEntry1 =
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				listTypeDefinitionId, listTypeEntry1);

		for (EntityField entityField : entityFields) {
			Page<ListTypeEntry> page =
				listTypeEntryResource.getListTypeDefinitionListTypeEntriesPage(
					listTypeDefinitionId, null, null,
					getFilterString(entityField, "between", listTypeEntry1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(listTypeEntry1),
				(List<ListTypeEntry>)page.getItems());
		}
	}

	@Test
	public void testGetListTypeDefinitionListTypeEntriesPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long listTypeDefinitionId =
			testGetListTypeDefinitionListTypeEntriesPage_getListTypeDefinitionId();

		ListTypeEntry listTypeEntry1 =
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				listTypeDefinitionId, randomListTypeEntry());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ListTypeEntry listTypeEntry2 =
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				listTypeDefinitionId, randomListTypeEntry());

		for (EntityField entityField : entityFields) {
			Page<ListTypeEntry> page =
				listTypeEntryResource.getListTypeDefinitionListTypeEntriesPage(
					listTypeDefinitionId, null, null,
					getFilterString(entityField, "eq", listTypeEntry1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(listTypeEntry1),
				(List<ListTypeEntry>)page.getItems());
		}
	}

	@Test
	public void testGetListTypeDefinitionListTypeEntriesPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long listTypeDefinitionId =
			testGetListTypeDefinitionListTypeEntriesPage_getListTypeDefinitionId();

		ListTypeEntry listTypeEntry1 =
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				listTypeDefinitionId, randomListTypeEntry());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ListTypeEntry listTypeEntry2 =
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				listTypeDefinitionId, randomListTypeEntry());

		for (EntityField entityField : entityFields) {
			Page<ListTypeEntry> page =
				listTypeEntryResource.getListTypeDefinitionListTypeEntriesPage(
					listTypeDefinitionId, null, null,
					getFilterString(entityField, "eq", listTypeEntry1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(listTypeEntry1),
				(List<ListTypeEntry>)page.getItems());
		}
	}

	@Test
	public void testGetListTypeDefinitionListTypeEntriesPageWithPagination()
		throws Exception {

		Long listTypeDefinitionId =
			testGetListTypeDefinitionListTypeEntriesPage_getListTypeDefinitionId();

		ListTypeEntry listTypeEntry1 =
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				listTypeDefinitionId, randomListTypeEntry());

		ListTypeEntry listTypeEntry2 =
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				listTypeDefinitionId, randomListTypeEntry());

		ListTypeEntry listTypeEntry3 =
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				listTypeDefinitionId, randomListTypeEntry());

		Page<ListTypeEntry> page1 =
			listTypeEntryResource.getListTypeDefinitionListTypeEntriesPage(
				listTypeDefinitionId, null, null, null, Pagination.of(1, 2),
				null);

		List<ListTypeEntry> listTypeEntries1 =
			(List<ListTypeEntry>)page1.getItems();

		Assert.assertEquals(
			listTypeEntries1.toString(), 2, listTypeEntries1.size());

		Page<ListTypeEntry> page2 =
			listTypeEntryResource.getListTypeDefinitionListTypeEntriesPage(
				listTypeDefinitionId, null, null, null, Pagination.of(2, 2),
				null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<ListTypeEntry> listTypeEntries2 =
			(List<ListTypeEntry>)page2.getItems();

		Assert.assertEquals(
			listTypeEntries2.toString(), 1, listTypeEntries2.size());

		Page<ListTypeEntry> page3 =
			listTypeEntryResource.getListTypeDefinitionListTypeEntriesPage(
				listTypeDefinitionId, null, null, null, Pagination.of(1, 3),
				null);

		assertEqualsIgnoringOrder(
			Arrays.asList(listTypeEntry1, listTypeEntry2, listTypeEntry3),
			(List<ListTypeEntry>)page3.getItems());
	}

	@Test
	public void testGetListTypeDefinitionListTypeEntriesPageWithSortDateTime()
		throws Exception {

		testGetListTypeDefinitionListTypeEntriesPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, listTypeEntry1, listTypeEntry2) -> {
				BeanTestUtil.setProperty(
					listTypeEntry1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetListTypeDefinitionListTypeEntriesPageWithSortDouble()
		throws Exception {

		testGetListTypeDefinitionListTypeEntriesPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, listTypeEntry1, listTypeEntry2) -> {
				BeanTestUtil.setProperty(
					listTypeEntry1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					listTypeEntry2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetListTypeDefinitionListTypeEntriesPageWithSortInteger()
		throws Exception {

		testGetListTypeDefinitionListTypeEntriesPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, listTypeEntry1, listTypeEntry2) -> {
				BeanTestUtil.setProperty(
					listTypeEntry1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					listTypeEntry2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetListTypeDefinitionListTypeEntriesPageWithSortString()
		throws Exception {

		testGetListTypeDefinitionListTypeEntriesPageWithSort(
			EntityField.Type.STRING,
			(entityField, listTypeEntry1, listTypeEntry2) -> {
				Class<?> clazz = listTypeEntry1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						listTypeEntry1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						listTypeEntry2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						listTypeEntry1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						listTypeEntry2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						listTypeEntry1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						listTypeEntry2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetListTypeDefinitionListTypeEntriesPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, ListTypeEntry, ListTypeEntry, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long listTypeDefinitionId =
			testGetListTypeDefinitionListTypeEntriesPage_getListTypeDefinitionId();

		ListTypeEntry listTypeEntry1 = randomListTypeEntry();
		ListTypeEntry listTypeEntry2 = randomListTypeEntry();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, listTypeEntry1, listTypeEntry2);
		}

		listTypeEntry1 =
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				listTypeDefinitionId, listTypeEntry1);

		listTypeEntry2 =
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				listTypeDefinitionId, listTypeEntry2);

		for (EntityField entityField : entityFields) {
			Page<ListTypeEntry> ascPage =
				listTypeEntryResource.getListTypeDefinitionListTypeEntriesPage(
					listTypeDefinitionId, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(listTypeEntry1, listTypeEntry2),
				(List<ListTypeEntry>)ascPage.getItems());

			Page<ListTypeEntry> descPage =
				listTypeEntryResource.getListTypeDefinitionListTypeEntriesPage(
					listTypeDefinitionId, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(listTypeEntry2, listTypeEntry1),
				(List<ListTypeEntry>)descPage.getItems());
		}
	}

	protected ListTypeEntry
			testGetListTypeDefinitionListTypeEntriesPage_addListTypeEntry(
				Long listTypeDefinitionId, ListTypeEntry listTypeEntry)
		throws Exception {

		return listTypeEntryResource.postListTypeDefinitionListTypeEntry(
			listTypeDefinitionId, listTypeEntry);
	}

	protected Long
			testGetListTypeDefinitionListTypeEntriesPage_getListTypeDefinitionId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetListTypeDefinitionListTypeEntriesPage_getIrrelevantListTypeDefinitionId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostListTypeDefinitionListTypeEntry() throws Exception {
		ListTypeEntry randomListTypeEntry = randomListTypeEntry();

		ListTypeEntry postListTypeEntry =
			testPostListTypeDefinitionListTypeEntry_addListTypeEntry(
				randomListTypeEntry);

		assertEquals(randomListTypeEntry, postListTypeEntry);
		assertValid(postListTypeEntry);
	}

	protected ListTypeEntry
			testPostListTypeDefinitionListTypeEntry_addListTypeEntry(
				ListTypeEntry listTypeEntry)
		throws Exception {

		return listTypeEntryResource.postListTypeDefinitionListTypeEntry(
			testGetListTypeDefinitionListTypeEntriesPage_getListTypeDefinitionId(),
			listTypeEntry);
	}

	@Test
	public void testDeleteListTypeEntry() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		ListTypeEntry listTypeEntry =
			testDeleteListTypeEntry_addListTypeEntry();

		assertHttpResponseStatusCode(
			204,
			listTypeEntryResource.deleteListTypeEntryHttpResponse(
				listTypeEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			listTypeEntryResource.getListTypeEntryHttpResponse(
				listTypeEntry.getId()));

		assertHttpResponseStatusCode(
			404, listTypeEntryResource.getListTypeEntryHttpResponse(0L));
	}

	protected ListTypeEntry testDeleteListTypeEntry_addListTypeEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteListTypeEntry() throws Exception {
		ListTypeEntry listTypeEntry =
			testGraphQLDeleteListTypeEntry_addListTypeEntry();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteListTypeEntry",
						new HashMap<String, Object>() {
							{
								put("listTypeEntryId", listTypeEntry.getId());
							}
						})),
				"JSONObject/data", "Object/deleteListTypeEntry"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"listTypeEntry",
					new HashMap<String, Object>() {
						{
							put("listTypeEntryId", listTypeEntry.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected ListTypeEntry testGraphQLDeleteListTypeEntry_addListTypeEntry()
		throws Exception {

		return testGraphQLListTypeEntry_addListTypeEntry();
	}

	@Test
	public void testGetListTypeEntry() throws Exception {
		ListTypeEntry postListTypeEntry =
			testGetListTypeEntry_addListTypeEntry();

		ListTypeEntry getListTypeEntry = listTypeEntryResource.getListTypeEntry(
			postListTypeEntry.getId());

		assertEquals(postListTypeEntry, getListTypeEntry);
		assertValid(getListTypeEntry);
	}

	protected ListTypeEntry testGetListTypeEntry_addListTypeEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetListTypeEntry() throws Exception {
		ListTypeEntry listTypeEntry =
			testGraphQLGetListTypeEntry_addListTypeEntry();

		Assert.assertTrue(
			equals(
				listTypeEntry,
				ListTypeEntrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"listTypeEntry",
								new HashMap<String, Object>() {
									{
										put(
											"listTypeEntryId",
											listTypeEntry.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/listTypeEntry"))));
	}

	@Test
	public void testGraphQLGetListTypeEntryNotFound() throws Exception {
		Long irrelevantListTypeEntryId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"listTypeEntry",
						new HashMap<String, Object>() {
							{
								put(
									"listTypeEntryId",
									irrelevantListTypeEntryId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected ListTypeEntry testGraphQLGetListTypeEntry_addListTypeEntry()
		throws Exception {

		return testGraphQLListTypeEntry_addListTypeEntry();
	}

	@Test
	public void testPutListTypeEntry() throws Exception {
		ListTypeEntry postListTypeEntry =
			testPutListTypeEntry_addListTypeEntry();

		ListTypeEntry randomListTypeEntry = randomListTypeEntry();

		ListTypeEntry putListTypeEntry = listTypeEntryResource.putListTypeEntry(
			postListTypeEntry.getId(), randomListTypeEntry);

		assertEquals(randomListTypeEntry, putListTypeEntry);
		assertValid(putListTypeEntry);

		ListTypeEntry getListTypeEntry = listTypeEntryResource.getListTypeEntry(
			putListTypeEntry.getId());

		assertEquals(randomListTypeEntry, getListTypeEntry);
		assertValid(getListTypeEntry);
	}

	protected ListTypeEntry testPutListTypeEntry_addListTypeEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected ListTypeEntry testGraphQLListTypeEntry_addListTypeEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		ListTypeEntry listTypeEntry, List<ListTypeEntry> listTypeEntries) {

		boolean contains = false;

		for (ListTypeEntry item : listTypeEntries) {
			if (equals(listTypeEntry, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			listTypeEntries + " does not contain " + listTypeEntry, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ListTypeEntry listTypeEntry1, ListTypeEntry listTypeEntry2) {

		Assert.assertTrue(
			listTypeEntry1 + " does not equal " + listTypeEntry2,
			equals(listTypeEntry1, listTypeEntry2));
	}

	protected void assertEquals(
		List<ListTypeEntry> listTypeEntries1,
		List<ListTypeEntry> listTypeEntries2) {

		Assert.assertEquals(listTypeEntries1.size(), listTypeEntries2.size());

		for (int i = 0; i < listTypeEntries1.size(); i++) {
			ListTypeEntry listTypeEntry1 = listTypeEntries1.get(i);
			ListTypeEntry listTypeEntry2 = listTypeEntries2.get(i);

			assertEquals(listTypeEntry1, listTypeEntry2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ListTypeEntry> listTypeEntries1,
		List<ListTypeEntry> listTypeEntries2) {

		Assert.assertEquals(listTypeEntries1.size(), listTypeEntries2.size());

		for (ListTypeEntry listTypeEntry1 : listTypeEntries1) {
			boolean contains = false;

			for (ListTypeEntry listTypeEntry2 : listTypeEntries2) {
				if (equals(listTypeEntry1, listTypeEntry2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				listTypeEntries2 + " does not contain " + listTypeEntry1,
				contains);
		}
	}

	protected void assertValid(ListTypeEntry listTypeEntry) throws Exception {
		boolean valid = true;

		if (listTypeEntry.getDateCreated() == null) {
			valid = false;
		}

		if (listTypeEntry.getDateModified() == null) {
			valid = false;
		}

		if (listTypeEntry.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (listTypeEntry.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (listTypeEntry.getKey() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (listTypeEntry.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name_i18n", additionalAssertFieldName)) {
				if (listTypeEntry.getName_i18n() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (listTypeEntry.getType() == null) {
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

	protected void assertValid(Page<ListTypeEntry> page) {
		boolean valid = false;

		java.util.Collection<ListTypeEntry> listTypeEntries = page.getItems();

		int size = listTypeEntries.size();

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
					com.liferay.headless.admin.list.type.dto.v1_0.ListTypeEntry.
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

	protected boolean equals(
		ListTypeEntry listTypeEntry1, ListTypeEntry listTypeEntry2) {

		if (listTypeEntry1 == listTypeEntry2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)listTypeEntry1.getActions(),
						(Map)listTypeEntry2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateCreated", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						listTypeEntry1.getDateCreated(),
						listTypeEntry2.getDateCreated())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						listTypeEntry1.getDateModified(),
						listTypeEntry2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						listTypeEntry1.getId(), listTypeEntry2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						listTypeEntry1.getKey(), listTypeEntry2.getKey())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						listTypeEntry1.getName(), listTypeEntry2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name_i18n", additionalAssertFieldName)) {
				if (!equals(
						(Map)listTypeEntry1.getName_i18n(),
						(Map)listTypeEntry2.getName_i18n())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						listTypeEntry1.getType(), listTypeEntry2.getType())) {

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

		if (!(_listTypeEntryResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_listTypeEntryResource;

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
		EntityField entityField, String operator, ListTypeEntry listTypeEntry) {

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

		if (entityFieldName.equals("dateCreated")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							listTypeEntry.getDateCreated(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							listTypeEntry.getDateCreated(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(listTypeEntry.getDateCreated()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("dateModified")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							listTypeEntry.getDateModified(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							listTypeEntry.getDateModified(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(listTypeEntry.getDateModified()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("key")) {
			sb.append("'");
			sb.append(String.valueOf(listTypeEntry.getKey()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(listTypeEntry.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("name_i18n")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("type")) {
			sb.append("'");
			sb.append(String.valueOf(listTypeEntry.getType()));
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

	protected ListTypeEntry randomListTypeEntry() throws Exception {
		return new ListTypeEntry() {
			{
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				id = RandomTestUtil.randomLong();
				key = StringUtil.toLowerCase(RandomTestUtil.randomString());
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				type = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected ListTypeEntry randomIrrelevantListTypeEntry() throws Exception {
		ListTypeEntry randomIrrelevantListTypeEntry = randomListTypeEntry();

		return randomIrrelevantListTypeEntry;
	}

	protected ListTypeEntry randomPatchListTypeEntry() throws Exception {
		return randomListTypeEntry();
	}

	protected ListTypeEntryResource listTypeEntryResource;
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
		LogFactoryUtil.getLog(BaseListTypeEntryResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private
		com.liferay.headless.admin.list.type.resource.v1_0.ListTypeEntryResource
			_listTypeEntryResource;

}