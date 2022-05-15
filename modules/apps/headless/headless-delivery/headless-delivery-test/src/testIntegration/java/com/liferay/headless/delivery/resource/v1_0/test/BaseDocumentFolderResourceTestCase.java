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

package com.liferay.headless.delivery.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.DepotEntryLocalServiceUtil;
import com.liferay.headless.delivery.client.dto.v1_0.DocumentFolder;
import com.liferay.headless.delivery.client.dto.v1_0.Field;
import com.liferay.headless.delivery.client.http.HttpInvoker;
import com.liferay.headless.delivery.client.pagination.Page;
import com.liferay.headless.delivery.client.pagination.Pagination;
import com.liferay.headless.delivery.client.permission.Permission;
import com.liferay.headless.delivery.client.resource.v1_0.DocumentFolderResource;
import com.liferay.headless.delivery.client.serdes.v1_0.DocumentFolderSerDes;
import com.liferay.petra.function.UnsafeTriConsumer;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONDeserializer;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
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
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseDocumentFolderResourceTestCase {

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

		testDepotEntry = DepotEntryLocalServiceUtil.addDepotEntry(
			Collections.singletonMap(
				LocaleUtil.getDefault(), RandomTestUtil.randomString()),
			null,
			new ServiceContext() {
				{
					setCompanyId(testGroup.getCompanyId());
					setUserId(TestPropsValues.getUserId());
				}
			});

		_documentFolderResource.setContextCompany(testCompany);

		DocumentFolderResource.Builder builder =
			DocumentFolderResource.builder();

		documentFolderResource = builder.authentication(
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

		DocumentFolder documentFolder1 = randomDocumentFolder();

		String json = objectMapper.writeValueAsString(documentFolder1);

		DocumentFolder documentFolder2 = DocumentFolderSerDes.toDTO(json);

		Assert.assertTrue(equals(documentFolder1, documentFolder2));
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

		DocumentFolder documentFolder = randomDocumentFolder();

		String json1 = objectMapper.writeValueAsString(documentFolder);
		String json2 = DocumentFolderSerDes.toJSON(documentFolder);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		DocumentFolder documentFolder = randomDocumentFolder();

		documentFolder.setAssetLibraryKey(regex);
		documentFolder.setDescription(regex);
		documentFolder.setName(regex);

		String json = DocumentFolderSerDes.toJSON(documentFolder);

		Assert.assertFalse(json.contains(regex));

		documentFolder = DocumentFolderSerDes.toDTO(json);

		Assert.assertEquals(regex, documentFolder.getAssetLibraryKey());
		Assert.assertEquals(regex, documentFolder.getDescription());
		Assert.assertEquals(regex, documentFolder.getName());
	}

	@Test
	public void testGetAssetLibraryDocumentFoldersPage() throws Exception {
		Long assetLibraryId =
			testGetAssetLibraryDocumentFoldersPage_getAssetLibraryId();
		Long irrelevantAssetLibraryId =
			testGetAssetLibraryDocumentFoldersPage_getIrrelevantAssetLibraryId();

		Page<DocumentFolder> page =
			documentFolderResource.getAssetLibraryDocumentFoldersPage(
				assetLibraryId, null, null, null, null, Pagination.of(1, 10),
				null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantAssetLibraryId != null) {
			DocumentFolder irrelevantDocumentFolder =
				testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
					irrelevantAssetLibraryId, randomIrrelevantDocumentFolder());

			page = documentFolderResource.getAssetLibraryDocumentFoldersPage(
				irrelevantAssetLibraryId, null, null, null, null,
				Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantDocumentFolder),
				(List<DocumentFolder>)page.getItems());
			assertValid(page);
		}

		DocumentFolder documentFolder1 =
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				assetLibraryId, randomDocumentFolder());

		DocumentFolder documentFolder2 =
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				assetLibraryId, randomDocumentFolder());

		page = documentFolderResource.getAssetLibraryDocumentFoldersPage(
			assetLibraryId, null, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(documentFolder1, documentFolder2),
			(List<DocumentFolder>)page.getItems());
		assertValid(page);

		documentFolderResource.deleteDocumentFolder(documentFolder1.getId());

		documentFolderResource.deleteDocumentFolder(documentFolder2.getId());
	}

	@Test
	public void testGetAssetLibraryDocumentFoldersPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long assetLibraryId =
			testGetAssetLibraryDocumentFoldersPage_getAssetLibraryId();

		DocumentFolder documentFolder1 = randomDocumentFolder();

		documentFolder1 =
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				assetLibraryId, documentFolder1);

		for (EntityField entityField : entityFields) {
			Page<DocumentFolder> page =
				documentFolderResource.getAssetLibraryDocumentFoldersPage(
					assetLibraryId, null, null, null,
					getFilterString(entityField, "between", documentFolder1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(documentFolder1),
				(List<DocumentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetAssetLibraryDocumentFoldersPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long assetLibraryId =
			testGetAssetLibraryDocumentFoldersPage_getAssetLibraryId();

		DocumentFolder documentFolder1 =
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				assetLibraryId, randomDocumentFolder());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder documentFolder2 =
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				assetLibraryId, randomDocumentFolder());

		for (EntityField entityField : entityFields) {
			Page<DocumentFolder> page =
				documentFolderResource.getAssetLibraryDocumentFoldersPage(
					assetLibraryId, null, null, null,
					getFilterString(entityField, "eq", documentFolder1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(documentFolder1),
				(List<DocumentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetAssetLibraryDocumentFoldersPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long assetLibraryId =
			testGetAssetLibraryDocumentFoldersPage_getAssetLibraryId();

		DocumentFolder documentFolder1 =
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				assetLibraryId, randomDocumentFolder());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder documentFolder2 =
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				assetLibraryId, randomDocumentFolder());

		for (EntityField entityField : entityFields) {
			Page<DocumentFolder> page =
				documentFolderResource.getAssetLibraryDocumentFoldersPage(
					assetLibraryId, null, null, null,
					getFilterString(entityField, "eq", documentFolder1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(documentFolder1),
				(List<DocumentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetAssetLibraryDocumentFoldersPageWithPagination()
		throws Exception {

		Long assetLibraryId =
			testGetAssetLibraryDocumentFoldersPage_getAssetLibraryId();

		DocumentFolder documentFolder1 =
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				assetLibraryId, randomDocumentFolder());

		DocumentFolder documentFolder2 =
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				assetLibraryId, randomDocumentFolder());

		DocumentFolder documentFolder3 =
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				assetLibraryId, randomDocumentFolder());

		Page<DocumentFolder> page1 =
			documentFolderResource.getAssetLibraryDocumentFoldersPage(
				assetLibraryId, null, null, null, null, Pagination.of(1, 2),
				null);

		List<DocumentFolder> documentFolders1 =
			(List<DocumentFolder>)page1.getItems();

		Assert.assertEquals(
			documentFolders1.toString(), 2, documentFolders1.size());

		Page<DocumentFolder> page2 =
			documentFolderResource.getAssetLibraryDocumentFoldersPage(
				assetLibraryId, null, null, null, null, Pagination.of(2, 2),
				null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<DocumentFolder> documentFolders2 =
			(List<DocumentFolder>)page2.getItems();

		Assert.assertEquals(
			documentFolders2.toString(), 1, documentFolders2.size());

		Page<DocumentFolder> page3 =
			documentFolderResource.getAssetLibraryDocumentFoldersPage(
				assetLibraryId, null, null, null, null, Pagination.of(1, 3),
				null);

		assertEqualsIgnoringOrder(
			Arrays.asList(documentFolder1, documentFolder2, documentFolder3),
			(List<DocumentFolder>)page3.getItems());
	}

	@Test
	public void testGetAssetLibraryDocumentFoldersPageWithSortDateTime()
		throws Exception {

		testGetAssetLibraryDocumentFoldersPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, documentFolder1, documentFolder2) -> {
				BeanTestUtil.setProperty(
					documentFolder1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetAssetLibraryDocumentFoldersPageWithSortDouble()
		throws Exception {

		testGetAssetLibraryDocumentFoldersPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, documentFolder1, documentFolder2) -> {
				BeanTestUtil.setProperty(
					documentFolder1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					documentFolder2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetAssetLibraryDocumentFoldersPageWithSortInteger()
		throws Exception {

		testGetAssetLibraryDocumentFoldersPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, documentFolder1, documentFolder2) -> {
				BeanTestUtil.setProperty(
					documentFolder1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					documentFolder2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetAssetLibraryDocumentFoldersPageWithSortString()
		throws Exception {

		testGetAssetLibraryDocumentFoldersPageWithSort(
			EntityField.Type.STRING,
			(entityField, documentFolder1, documentFolder2) -> {
				Class<?> clazz = documentFolder1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						documentFolder1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						documentFolder2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						documentFolder1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						documentFolder2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						documentFolder1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						documentFolder2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetAssetLibraryDocumentFoldersPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, DocumentFolder, DocumentFolder, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long assetLibraryId =
			testGetAssetLibraryDocumentFoldersPage_getAssetLibraryId();

		DocumentFolder documentFolder1 = randomDocumentFolder();
		DocumentFolder documentFolder2 = randomDocumentFolder();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, documentFolder1, documentFolder2);
		}

		documentFolder1 =
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				assetLibraryId, documentFolder1);

		documentFolder2 =
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				assetLibraryId, documentFolder2);

		for (EntityField entityField : entityFields) {
			Page<DocumentFolder> ascPage =
				documentFolderResource.getAssetLibraryDocumentFoldersPage(
					assetLibraryId, null, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(documentFolder1, documentFolder2),
				(List<DocumentFolder>)ascPage.getItems());

			Page<DocumentFolder> descPage =
				documentFolderResource.getAssetLibraryDocumentFoldersPage(
					assetLibraryId, null, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(documentFolder2, documentFolder1),
				(List<DocumentFolder>)descPage.getItems());
		}
	}

	protected DocumentFolder
			testGetAssetLibraryDocumentFoldersPage_addDocumentFolder(
				Long assetLibraryId, DocumentFolder documentFolder)
		throws Exception {

		return documentFolderResource.postAssetLibraryDocumentFolder(
			assetLibraryId, documentFolder);
	}

	protected Long testGetAssetLibraryDocumentFoldersPage_getAssetLibraryId()
		throws Exception {

		return testDepotEntry.getDepotEntryId();
	}

	protected Long
			testGetAssetLibraryDocumentFoldersPage_getIrrelevantAssetLibraryId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAssetLibraryDocumentFolder() throws Exception {
		DocumentFolder randomDocumentFolder = randomDocumentFolder();

		DocumentFolder postDocumentFolder =
			testPostAssetLibraryDocumentFolder_addDocumentFolder(
				randomDocumentFolder);

		assertEquals(randomDocumentFolder, postDocumentFolder);
		assertValid(postDocumentFolder);
	}

	protected DocumentFolder
			testPostAssetLibraryDocumentFolder_addDocumentFolder(
				DocumentFolder documentFolder)
		throws Exception {

		return documentFolderResource.postAssetLibraryDocumentFolder(
			testGetAssetLibraryDocumentFoldersPage_getAssetLibraryId(),
			documentFolder);
	}

	@Test
	public void testGetAssetLibraryDocumentFolderPermissionsPage()
		throws Exception {

		Page<Permission> page =
			documentFolderResource.getAssetLibraryDocumentFolderPermissionsPage(
				testDepotEntry.getDepotEntryId(), RoleConstants.GUEST);

		Assert.assertNotNull(page);
	}

	protected DocumentFolder
			testGetAssetLibraryDocumentFolderPermissionsPage_addDocumentFolder()
		throws Exception {

		return testPostAssetLibraryDocumentFolder_addDocumentFolder(
			randomDocumentFolder());
	}

	@Test
	public void testPutAssetLibraryDocumentFolderPermissionsPage()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder documentFolder =
			testPutAssetLibraryDocumentFolderPermissionsPage_addDocumentFolder();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		com.liferay.portal.kernel.model.Role role = RoleTestUtil.addRole(
			RoleConstants.TYPE_REGULAR);

		assertHttpResponseStatusCode(
			200,
			documentFolderResource.
				putAssetLibraryDocumentFolderPermissionsPageHttpResponse(
					testDepotEntry.getDepotEntryId(),
					new Permission[] {
						new Permission() {
							{
								setActionIds(new String[] {"PERMISSIONS"});
								setRoleName(role.getName());
							}
						}
					}));

		assertHttpResponseStatusCode(
			404,
			documentFolderResource.
				putAssetLibraryDocumentFolderPermissionsPageHttpResponse(
					testDepotEntry.getDepotEntryId(),
					new Permission[] {
						new Permission() {
							{
								setActionIds(new String[] {"-"});
								setRoleName("-");
							}
						}
					}));
	}

	protected DocumentFolder
			testPutAssetLibraryDocumentFolderPermissionsPage_addDocumentFolder()
		throws Exception {

		return documentFolderResource.postAssetLibraryDocumentFolder(
			testDepotEntry.getDepotEntryId(), randomDocumentFolder());
	}

	@Test
	public void testDeleteDocumentFolder() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder documentFolder =
			testDeleteDocumentFolder_addDocumentFolder();

		assertHttpResponseStatusCode(
			204,
			documentFolderResource.deleteDocumentFolderHttpResponse(
				documentFolder.getId()));

		assertHttpResponseStatusCode(
			404,
			documentFolderResource.getDocumentFolderHttpResponse(
				documentFolder.getId()));

		assertHttpResponseStatusCode(
			404, documentFolderResource.getDocumentFolderHttpResponse(0L));
	}

	protected DocumentFolder testDeleteDocumentFolder_addDocumentFolder()
		throws Exception {

		return documentFolderResource.postSiteDocumentFolder(
			testGroup.getGroupId(), randomDocumentFolder());
	}

	@Test
	public void testGraphQLDeleteDocumentFolder() throws Exception {
		DocumentFolder documentFolder =
			testGraphQLDeleteDocumentFolder_addDocumentFolder();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteDocumentFolder",
						new HashMap<String, Object>() {
							{
								put("documentFolderId", documentFolder.getId());
							}
						})),
				"JSONObject/data", "Object/deleteDocumentFolder"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"documentFolder",
					new HashMap<String, Object>() {
						{
							put("documentFolderId", documentFolder.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected DocumentFolder testGraphQLDeleteDocumentFolder_addDocumentFolder()
		throws Exception {

		return testGraphQLDocumentFolder_addDocumentFolder();
	}

	@Test
	public void testGetDocumentFolder() throws Exception {
		DocumentFolder postDocumentFolder =
			testGetDocumentFolder_addDocumentFolder();

		DocumentFolder getDocumentFolder =
			documentFolderResource.getDocumentFolder(
				postDocumentFolder.getId());

		assertEquals(postDocumentFolder, getDocumentFolder);
		assertValid(getDocumentFolder);
	}

	protected DocumentFolder testGetDocumentFolder_addDocumentFolder()
		throws Exception {

		return documentFolderResource.postSiteDocumentFolder(
			testGroup.getGroupId(), randomDocumentFolder());
	}

	@Test
	public void testGraphQLGetDocumentFolder() throws Exception {
		DocumentFolder documentFolder =
			testGraphQLGetDocumentFolder_addDocumentFolder();

		Assert.assertTrue(
			equals(
				documentFolder,
				DocumentFolderSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"documentFolder",
								new HashMap<String, Object>() {
									{
										put(
											"documentFolderId",
											documentFolder.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/documentFolder"))));
	}

	@Test
	public void testGraphQLGetDocumentFolderNotFound() throws Exception {
		Long irrelevantDocumentFolderId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"documentFolder",
						new HashMap<String, Object>() {
							{
								put(
									"documentFolderId",
									irrelevantDocumentFolderId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected DocumentFolder testGraphQLGetDocumentFolder_addDocumentFolder()
		throws Exception {

		return testGraphQLDocumentFolder_addDocumentFolder();
	}

	@Test
	public void testPatchDocumentFolder() throws Exception {
		DocumentFolder postDocumentFolder =
			testPatchDocumentFolder_addDocumentFolder();

		DocumentFolder randomPatchDocumentFolder = randomPatchDocumentFolder();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder patchDocumentFolder =
			documentFolderResource.patchDocumentFolder(
				postDocumentFolder.getId(), randomPatchDocumentFolder);

		DocumentFolder expectedPatchDocumentFolder = postDocumentFolder.clone();

		BeanTestUtil.copyProperties(
			randomPatchDocumentFolder, expectedPatchDocumentFolder);

		DocumentFolder getDocumentFolder =
			documentFolderResource.getDocumentFolder(
				patchDocumentFolder.getId());

		assertEquals(expectedPatchDocumentFolder, getDocumentFolder);
		assertValid(getDocumentFolder);
	}

	protected DocumentFolder testPatchDocumentFolder_addDocumentFolder()
		throws Exception {

		return documentFolderResource.postSiteDocumentFolder(
			testGroup.getGroupId(), randomDocumentFolder());
	}

	@Test
	public void testPutDocumentFolder() throws Exception {
		DocumentFolder postDocumentFolder =
			testPutDocumentFolder_addDocumentFolder();

		DocumentFolder randomDocumentFolder = randomDocumentFolder();

		DocumentFolder putDocumentFolder =
			documentFolderResource.putDocumentFolder(
				postDocumentFolder.getId(), randomDocumentFolder);

		assertEquals(randomDocumentFolder, putDocumentFolder);
		assertValid(putDocumentFolder);

		DocumentFolder getDocumentFolder =
			documentFolderResource.getDocumentFolder(putDocumentFolder.getId());

		assertEquals(randomDocumentFolder, getDocumentFolder);
		assertValid(getDocumentFolder);
	}

	protected DocumentFolder testPutDocumentFolder_addDocumentFolder()
		throws Exception {

		return documentFolderResource.postSiteDocumentFolder(
			testGroup.getGroupId(), randomDocumentFolder());
	}

	@Test
	public void testGetDocumentFolderPermissionsPage() throws Exception {
		DocumentFolder postDocumentFolder =
			testGetDocumentFolderPermissionsPage_addDocumentFolder();

		Page<Permission> page =
			documentFolderResource.getDocumentFolderPermissionsPage(
				postDocumentFolder.getId(), RoleConstants.GUEST);

		Assert.assertNotNull(page);
	}

	protected DocumentFolder
			testGetDocumentFolderPermissionsPage_addDocumentFolder()
		throws Exception {

		return testPostDocumentFolderDocumentFolder_addDocumentFolder(
			randomDocumentFolder());
	}

	@Test
	public void testPutDocumentFolderPermissionsPage() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder documentFolder =
			testPutDocumentFolderPermissionsPage_addDocumentFolder();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		com.liferay.portal.kernel.model.Role role = RoleTestUtil.addRole(
			RoleConstants.TYPE_REGULAR);

		assertHttpResponseStatusCode(
			200,
			documentFolderResource.putDocumentFolderPermissionsPageHttpResponse(
				documentFolder.getId(),
				new Permission[] {
					new Permission() {
						{
							setActionIds(new String[] {"VIEW"});
							setRoleName(role.getName());
						}
					}
				}));

		assertHttpResponseStatusCode(
			404,
			documentFolderResource.putDocumentFolderPermissionsPageHttpResponse(
				0L,
				new Permission[] {
					new Permission() {
						{
							setActionIds(new String[] {"-"});
							setRoleName("-");
						}
					}
				}));
	}

	protected DocumentFolder
			testPutDocumentFolderPermissionsPage_addDocumentFolder()
		throws Exception {

		return documentFolderResource.postSiteDocumentFolder(
			testGroup.getGroupId(), randomDocumentFolder());
	}

	@Test
	public void testPutDocumentFolderSubscribe() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder documentFolder =
			testPutDocumentFolderSubscribe_addDocumentFolder();

		assertHttpResponseStatusCode(
			204,
			documentFolderResource.putDocumentFolderSubscribeHttpResponse(
				documentFolder.getId()));

		assertHttpResponseStatusCode(
			404,
			documentFolderResource.putDocumentFolderSubscribeHttpResponse(0L));
	}

	protected DocumentFolder testPutDocumentFolderSubscribe_addDocumentFolder()
		throws Exception {

		return documentFolderResource.postSiteDocumentFolder(
			testGroup.getGroupId(), randomDocumentFolder());
	}

	@Test
	public void testPutDocumentFolderUnsubscribe() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder documentFolder =
			testPutDocumentFolderUnsubscribe_addDocumentFolder();

		assertHttpResponseStatusCode(
			204,
			documentFolderResource.putDocumentFolderUnsubscribeHttpResponse(
				documentFolder.getId()));

		assertHttpResponseStatusCode(
			404,
			documentFolderResource.putDocumentFolderUnsubscribeHttpResponse(
				0L));
	}

	protected DocumentFolder
			testPutDocumentFolderUnsubscribe_addDocumentFolder()
		throws Exception {

		return documentFolderResource.postSiteDocumentFolder(
			testGroup.getGroupId(), randomDocumentFolder());
	}

	@Test
	public void testGetDocumentFolderDocumentFoldersPage() throws Exception {
		Long parentDocumentFolderId =
			testGetDocumentFolderDocumentFoldersPage_getParentDocumentFolderId();
		Long irrelevantParentDocumentFolderId =
			testGetDocumentFolderDocumentFoldersPage_getIrrelevantParentDocumentFolderId();

		Page<DocumentFolder> page =
			documentFolderResource.getDocumentFolderDocumentFoldersPage(
				parentDocumentFolderId, null, null, null, null,
				Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantParentDocumentFolderId != null) {
			DocumentFolder irrelevantDocumentFolder =
				testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
					irrelevantParentDocumentFolderId,
					randomIrrelevantDocumentFolder());

			page = documentFolderResource.getDocumentFolderDocumentFoldersPage(
				irrelevantParentDocumentFolderId, null, null, null, null,
				Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantDocumentFolder),
				(List<DocumentFolder>)page.getItems());
			assertValid(page);
		}

		DocumentFolder documentFolder1 =
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				parentDocumentFolderId, randomDocumentFolder());

		DocumentFolder documentFolder2 =
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				parentDocumentFolderId, randomDocumentFolder());

		page = documentFolderResource.getDocumentFolderDocumentFoldersPage(
			parentDocumentFolderId, null, null, null, null,
			Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(documentFolder1, documentFolder2),
			(List<DocumentFolder>)page.getItems());
		assertValid(page);

		documentFolderResource.deleteDocumentFolder(documentFolder1.getId());

		documentFolderResource.deleteDocumentFolder(documentFolder2.getId());
	}

	@Test
	public void testGetDocumentFolderDocumentFoldersPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentDocumentFolderId =
			testGetDocumentFolderDocumentFoldersPage_getParentDocumentFolderId();

		DocumentFolder documentFolder1 = randomDocumentFolder();

		documentFolder1 =
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				parentDocumentFolderId, documentFolder1);

		for (EntityField entityField : entityFields) {
			Page<DocumentFolder> page =
				documentFolderResource.getDocumentFolderDocumentFoldersPage(
					parentDocumentFolderId, null, null, null,
					getFilterString(entityField, "between", documentFolder1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(documentFolder1),
				(List<DocumentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetDocumentFolderDocumentFoldersPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentDocumentFolderId =
			testGetDocumentFolderDocumentFoldersPage_getParentDocumentFolderId();

		DocumentFolder documentFolder1 =
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				parentDocumentFolderId, randomDocumentFolder());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder documentFolder2 =
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				parentDocumentFolderId, randomDocumentFolder());

		for (EntityField entityField : entityFields) {
			Page<DocumentFolder> page =
				documentFolderResource.getDocumentFolderDocumentFoldersPage(
					parentDocumentFolderId, null, null, null,
					getFilterString(entityField, "eq", documentFolder1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(documentFolder1),
				(List<DocumentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetDocumentFolderDocumentFoldersPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentDocumentFolderId =
			testGetDocumentFolderDocumentFoldersPage_getParentDocumentFolderId();

		DocumentFolder documentFolder1 =
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				parentDocumentFolderId, randomDocumentFolder());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder documentFolder2 =
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				parentDocumentFolderId, randomDocumentFolder());

		for (EntityField entityField : entityFields) {
			Page<DocumentFolder> page =
				documentFolderResource.getDocumentFolderDocumentFoldersPage(
					parentDocumentFolderId, null, null, null,
					getFilterString(entityField, "eq", documentFolder1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(documentFolder1),
				(List<DocumentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetDocumentFolderDocumentFoldersPageWithPagination()
		throws Exception {

		Long parentDocumentFolderId =
			testGetDocumentFolderDocumentFoldersPage_getParentDocumentFolderId();

		DocumentFolder documentFolder1 =
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				parentDocumentFolderId, randomDocumentFolder());

		DocumentFolder documentFolder2 =
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				parentDocumentFolderId, randomDocumentFolder());

		DocumentFolder documentFolder3 =
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				parentDocumentFolderId, randomDocumentFolder());

		Page<DocumentFolder> page1 =
			documentFolderResource.getDocumentFolderDocumentFoldersPage(
				parentDocumentFolderId, null, null, null, null,
				Pagination.of(1, 2), null);

		List<DocumentFolder> documentFolders1 =
			(List<DocumentFolder>)page1.getItems();

		Assert.assertEquals(
			documentFolders1.toString(), 2, documentFolders1.size());

		Page<DocumentFolder> page2 =
			documentFolderResource.getDocumentFolderDocumentFoldersPage(
				parentDocumentFolderId, null, null, null, null,
				Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<DocumentFolder> documentFolders2 =
			(List<DocumentFolder>)page2.getItems();

		Assert.assertEquals(
			documentFolders2.toString(), 1, documentFolders2.size());

		Page<DocumentFolder> page3 =
			documentFolderResource.getDocumentFolderDocumentFoldersPage(
				parentDocumentFolderId, null, null, null, null,
				Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(documentFolder1, documentFolder2, documentFolder3),
			(List<DocumentFolder>)page3.getItems());
	}

	@Test
	public void testGetDocumentFolderDocumentFoldersPageWithSortDateTime()
		throws Exception {

		testGetDocumentFolderDocumentFoldersPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, documentFolder1, documentFolder2) -> {
				BeanTestUtil.setProperty(
					documentFolder1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetDocumentFolderDocumentFoldersPageWithSortDouble()
		throws Exception {

		testGetDocumentFolderDocumentFoldersPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, documentFolder1, documentFolder2) -> {
				BeanTestUtil.setProperty(
					documentFolder1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					documentFolder2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetDocumentFolderDocumentFoldersPageWithSortInteger()
		throws Exception {

		testGetDocumentFolderDocumentFoldersPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, documentFolder1, documentFolder2) -> {
				BeanTestUtil.setProperty(
					documentFolder1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					documentFolder2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetDocumentFolderDocumentFoldersPageWithSortString()
		throws Exception {

		testGetDocumentFolderDocumentFoldersPageWithSort(
			EntityField.Type.STRING,
			(entityField, documentFolder1, documentFolder2) -> {
				Class<?> clazz = documentFolder1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						documentFolder1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						documentFolder2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						documentFolder1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						documentFolder2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						documentFolder1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						documentFolder2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetDocumentFolderDocumentFoldersPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, DocumentFolder, DocumentFolder, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentDocumentFolderId =
			testGetDocumentFolderDocumentFoldersPage_getParentDocumentFolderId();

		DocumentFolder documentFolder1 = randomDocumentFolder();
		DocumentFolder documentFolder2 = randomDocumentFolder();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, documentFolder1, documentFolder2);
		}

		documentFolder1 =
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				parentDocumentFolderId, documentFolder1);

		documentFolder2 =
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				parentDocumentFolderId, documentFolder2);

		for (EntityField entityField : entityFields) {
			Page<DocumentFolder> ascPage =
				documentFolderResource.getDocumentFolderDocumentFoldersPage(
					parentDocumentFolderId, null, null, null, null,
					Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(documentFolder1, documentFolder2),
				(List<DocumentFolder>)ascPage.getItems());

			Page<DocumentFolder> descPage =
				documentFolderResource.getDocumentFolderDocumentFoldersPage(
					parentDocumentFolderId, null, null, null, null,
					Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(documentFolder2, documentFolder1),
				(List<DocumentFolder>)descPage.getItems());
		}
	}

	protected DocumentFolder
			testGetDocumentFolderDocumentFoldersPage_addDocumentFolder(
				Long parentDocumentFolderId, DocumentFolder documentFolder)
		throws Exception {

		return documentFolderResource.postDocumentFolderDocumentFolder(
			parentDocumentFolderId, documentFolder);
	}

	protected Long
			testGetDocumentFolderDocumentFoldersPage_getParentDocumentFolderId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetDocumentFolderDocumentFoldersPage_getIrrelevantParentDocumentFolderId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostDocumentFolderDocumentFolder() throws Exception {
		DocumentFolder randomDocumentFolder = randomDocumentFolder();

		DocumentFolder postDocumentFolder =
			testPostDocumentFolderDocumentFolder_addDocumentFolder(
				randomDocumentFolder);

		assertEquals(randomDocumentFolder, postDocumentFolder);
		assertValid(postDocumentFolder);
	}

	protected DocumentFolder
			testPostDocumentFolderDocumentFolder_addDocumentFolder(
				DocumentFolder documentFolder)
		throws Exception {

		return documentFolderResource.postDocumentFolderDocumentFolder(
			testGetDocumentFolderDocumentFoldersPage_getParentDocumentFolderId(),
			documentFolder);
	}

	@Test
	public void testGetSiteDocumentFoldersPage() throws Exception {
		Long siteId = testGetSiteDocumentFoldersPage_getSiteId();
		Long irrelevantSiteId =
			testGetSiteDocumentFoldersPage_getIrrelevantSiteId();

		Page<DocumentFolder> page =
			documentFolderResource.getSiteDocumentFoldersPage(
				siteId, null, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantSiteId != null) {
			DocumentFolder irrelevantDocumentFolder =
				testGetSiteDocumentFoldersPage_addDocumentFolder(
					irrelevantSiteId, randomIrrelevantDocumentFolder());

			page = documentFolderResource.getSiteDocumentFoldersPage(
				irrelevantSiteId, null, null, null, null, Pagination.of(1, 2),
				null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantDocumentFolder),
				(List<DocumentFolder>)page.getItems());
			assertValid(page);
		}

		DocumentFolder documentFolder1 =
			testGetSiteDocumentFoldersPage_addDocumentFolder(
				siteId, randomDocumentFolder());

		DocumentFolder documentFolder2 =
			testGetSiteDocumentFoldersPage_addDocumentFolder(
				siteId, randomDocumentFolder());

		page = documentFolderResource.getSiteDocumentFoldersPage(
			siteId, null, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(documentFolder1, documentFolder2),
			(List<DocumentFolder>)page.getItems());
		assertValid(page);

		documentFolderResource.deleteDocumentFolder(documentFolder1.getId());

		documentFolderResource.deleteDocumentFolder(documentFolder2.getId());
	}

	@Test
	public void testGetSiteDocumentFoldersPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteDocumentFoldersPage_getSiteId();

		DocumentFolder documentFolder1 = randomDocumentFolder();

		documentFolder1 = testGetSiteDocumentFoldersPage_addDocumentFolder(
			siteId, documentFolder1);

		for (EntityField entityField : entityFields) {
			Page<DocumentFolder> page =
				documentFolderResource.getSiteDocumentFoldersPage(
					siteId, null, null, null,
					getFilterString(entityField, "between", documentFolder1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(documentFolder1),
				(List<DocumentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetSiteDocumentFoldersPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteDocumentFoldersPage_getSiteId();

		DocumentFolder documentFolder1 =
			testGetSiteDocumentFoldersPage_addDocumentFolder(
				siteId, randomDocumentFolder());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder documentFolder2 =
			testGetSiteDocumentFoldersPage_addDocumentFolder(
				siteId, randomDocumentFolder());

		for (EntityField entityField : entityFields) {
			Page<DocumentFolder> page =
				documentFolderResource.getSiteDocumentFoldersPage(
					siteId, null, null, null,
					getFilterString(entityField, "eq", documentFolder1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(documentFolder1),
				(List<DocumentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetSiteDocumentFoldersPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteDocumentFoldersPage_getSiteId();

		DocumentFolder documentFolder1 =
			testGetSiteDocumentFoldersPage_addDocumentFolder(
				siteId, randomDocumentFolder());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder documentFolder2 =
			testGetSiteDocumentFoldersPage_addDocumentFolder(
				siteId, randomDocumentFolder());

		for (EntityField entityField : entityFields) {
			Page<DocumentFolder> page =
				documentFolderResource.getSiteDocumentFoldersPage(
					siteId, null, null, null,
					getFilterString(entityField, "eq", documentFolder1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(documentFolder1),
				(List<DocumentFolder>)page.getItems());
		}
	}

	@Test
	public void testGetSiteDocumentFoldersPageWithPagination()
		throws Exception {

		Long siteId = testGetSiteDocumentFoldersPage_getSiteId();

		DocumentFolder documentFolder1 =
			testGetSiteDocumentFoldersPage_addDocumentFolder(
				siteId, randomDocumentFolder());

		DocumentFolder documentFolder2 =
			testGetSiteDocumentFoldersPage_addDocumentFolder(
				siteId, randomDocumentFolder());

		DocumentFolder documentFolder3 =
			testGetSiteDocumentFoldersPage_addDocumentFolder(
				siteId, randomDocumentFolder());

		Page<DocumentFolder> page1 =
			documentFolderResource.getSiteDocumentFoldersPage(
				siteId, null, null, null, null, Pagination.of(1, 2), null);

		List<DocumentFolder> documentFolders1 =
			(List<DocumentFolder>)page1.getItems();

		Assert.assertEquals(
			documentFolders1.toString(), 2, documentFolders1.size());

		Page<DocumentFolder> page2 =
			documentFolderResource.getSiteDocumentFoldersPage(
				siteId, null, null, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<DocumentFolder> documentFolders2 =
			(List<DocumentFolder>)page2.getItems();

		Assert.assertEquals(
			documentFolders2.toString(), 1, documentFolders2.size());

		Page<DocumentFolder> page3 =
			documentFolderResource.getSiteDocumentFoldersPage(
				siteId, null, null, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(documentFolder1, documentFolder2, documentFolder3),
			(List<DocumentFolder>)page3.getItems());
	}

	@Test
	public void testGetSiteDocumentFoldersPageWithSortDateTime()
		throws Exception {

		testGetSiteDocumentFoldersPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, documentFolder1, documentFolder2) -> {
				BeanTestUtil.setProperty(
					documentFolder1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetSiteDocumentFoldersPageWithSortDouble()
		throws Exception {

		testGetSiteDocumentFoldersPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, documentFolder1, documentFolder2) -> {
				BeanTestUtil.setProperty(
					documentFolder1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					documentFolder2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetSiteDocumentFoldersPageWithSortInteger()
		throws Exception {

		testGetSiteDocumentFoldersPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, documentFolder1, documentFolder2) -> {
				BeanTestUtil.setProperty(
					documentFolder1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					documentFolder2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetSiteDocumentFoldersPageWithSortString()
		throws Exception {

		testGetSiteDocumentFoldersPageWithSort(
			EntityField.Type.STRING,
			(entityField, documentFolder1, documentFolder2) -> {
				Class<?> clazz = documentFolder1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						documentFolder1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						documentFolder2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						documentFolder1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						documentFolder2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						documentFolder1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						documentFolder2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetSiteDocumentFoldersPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, DocumentFolder, DocumentFolder, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteDocumentFoldersPage_getSiteId();

		DocumentFolder documentFolder1 = randomDocumentFolder();
		DocumentFolder documentFolder2 = randomDocumentFolder();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, documentFolder1, documentFolder2);
		}

		documentFolder1 = testGetSiteDocumentFoldersPage_addDocumentFolder(
			siteId, documentFolder1);

		documentFolder2 = testGetSiteDocumentFoldersPage_addDocumentFolder(
			siteId, documentFolder2);

		for (EntityField entityField : entityFields) {
			Page<DocumentFolder> ascPage =
				documentFolderResource.getSiteDocumentFoldersPage(
					siteId, null, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(documentFolder1, documentFolder2),
				(List<DocumentFolder>)ascPage.getItems());

			Page<DocumentFolder> descPage =
				documentFolderResource.getSiteDocumentFoldersPage(
					siteId, null, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(documentFolder2, documentFolder1),
				(List<DocumentFolder>)descPage.getItems());
		}
	}

	protected DocumentFolder testGetSiteDocumentFoldersPage_addDocumentFolder(
			Long siteId, DocumentFolder documentFolder)
		throws Exception {

		return documentFolderResource.postSiteDocumentFolder(
			siteId, documentFolder);
	}

	protected Long testGetSiteDocumentFoldersPage_getSiteId() throws Exception {
		return testGroup.getGroupId();
	}

	protected Long testGetSiteDocumentFoldersPage_getIrrelevantSiteId()
		throws Exception {

		return irrelevantGroup.getGroupId();
	}

	@Test
	public void testGraphQLGetSiteDocumentFoldersPage() throws Exception {
		Long siteId = testGetSiteDocumentFoldersPage_getSiteId();

		GraphQLField graphQLField = new GraphQLField(
			"documentFolders",
			new HashMap<String, Object>() {
				{
					put("page", 1);
					put("pageSize", 10);

					put("siteKey", "\"" + siteId + "\"");
				}
			},
			new GraphQLField("items", getGraphQLFields()),
			new GraphQLField("page"), new GraphQLField("totalCount"));

		JSONObject documentFoldersJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/documentFolders");

		Assert.assertEquals(0, documentFoldersJSONObject.get("totalCount"));

		DocumentFolder documentFolder1 =
			testGraphQLGetSiteDocumentFoldersPage_addDocumentFolder();
		DocumentFolder documentFolder2 =
			testGraphQLGetSiteDocumentFoldersPage_addDocumentFolder();

		documentFoldersJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/documentFolders");

		Assert.assertEquals(2, documentFoldersJSONObject.getLong("totalCount"));

		assertEqualsIgnoringOrder(
			Arrays.asList(documentFolder1, documentFolder2),
			Arrays.asList(
				DocumentFolderSerDes.toDTOs(
					documentFoldersJSONObject.getString("items"))));
	}

	protected DocumentFolder
			testGraphQLGetSiteDocumentFoldersPage_addDocumentFolder()
		throws Exception {

		return testGraphQLDocumentFolder_addDocumentFolder();
	}

	@Test
	public void testPostSiteDocumentFolder() throws Exception {
		DocumentFolder randomDocumentFolder = randomDocumentFolder();

		DocumentFolder postDocumentFolder =
			testPostSiteDocumentFolder_addDocumentFolder(randomDocumentFolder);

		assertEquals(randomDocumentFolder, postDocumentFolder);
		assertValid(postDocumentFolder);
	}

	protected DocumentFolder testPostSiteDocumentFolder_addDocumentFolder(
			DocumentFolder documentFolder)
		throws Exception {

		return documentFolderResource.postSiteDocumentFolder(
			testGetSiteDocumentFoldersPage_getSiteId(), documentFolder);
	}

	@Test
	public void testGraphQLPostSiteDocumentFolder() throws Exception {
		DocumentFolder randomDocumentFolder = randomDocumentFolder();

		DocumentFolder documentFolder =
			testGraphQLDocumentFolder_addDocumentFolder(randomDocumentFolder);

		Assert.assertTrue(equals(randomDocumentFolder, documentFolder));
	}

	@Test
	public void testGetSiteDocumentFolderPermissionsPage() throws Exception {
		Page<Permission> page =
			documentFolderResource.getSiteDocumentFolderPermissionsPage(
				testGroup.getGroupId(), RoleConstants.GUEST);

		Assert.assertNotNull(page);
	}

	protected DocumentFolder
			testGetSiteDocumentFolderPermissionsPage_addDocumentFolder()
		throws Exception {

		return testPostSiteDocumentFolder_addDocumentFolder(
			randomDocumentFolder());
	}

	@Test
	public void testPutSiteDocumentFolderPermissionsPage() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		DocumentFolder documentFolder =
			testPutSiteDocumentFolderPermissionsPage_addDocumentFolder();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		com.liferay.portal.kernel.model.Role role = RoleTestUtil.addRole(
			RoleConstants.TYPE_REGULAR);

		assertHttpResponseStatusCode(
			200,
			documentFolderResource.
				putSiteDocumentFolderPermissionsPageHttpResponse(
					documentFolder.getSiteId(),
					new Permission[] {
						new Permission() {
							{
								setActionIds(new String[] {"PERMISSIONS"});
								setRoleName(role.getName());
							}
						}
					}));

		assertHttpResponseStatusCode(
			404,
			documentFolderResource.
				putSiteDocumentFolderPermissionsPageHttpResponse(
					documentFolder.getSiteId(),
					new Permission[] {
						new Permission() {
							{
								setActionIds(new String[] {"-"});
								setRoleName("-");
							}
						}
					}));
	}

	protected DocumentFolder
			testPutSiteDocumentFolderPermissionsPage_addDocumentFolder()
		throws Exception {

		return documentFolderResource.postSiteDocumentFolder(
			testGroup.getGroupId(), randomDocumentFolder());
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected void appendGraphQLFieldValue(StringBuilder sb, Object value)
		throws Exception {

		if (value instanceof Object[]) {
			StringBuilder arraySB = new StringBuilder("[");

			for (Object object : (Object[])value) {
				if (arraySB.length() > 1) {
					arraySB.append(", ");
				}

				arraySB.append("{");

				Class<?> clazz = object.getClass();

				for (java.lang.reflect.Field field :
						getDeclaredFields(clazz.getSuperclass())) {

					arraySB.append(field.getName());
					arraySB.append(": ");

					appendGraphQLFieldValue(arraySB, field.get(object));

					arraySB.append(", ");
				}

				arraySB.setLength(arraySB.length() - 2);

				arraySB.append("}");
			}

			arraySB.append("]");

			sb.append(arraySB.toString());
		}
		else if (value instanceof String) {
			sb.append("\"");
			sb.append(value);
			sb.append("\"");
		}
		else {
			sb.append(value);
		}
	}

	protected DocumentFolder testGraphQLDocumentFolder_addDocumentFolder()
		throws Exception {

		return testGraphQLDocumentFolder_addDocumentFolder(
			randomDocumentFolder());
	}

	protected DocumentFolder testGraphQLDocumentFolder_addDocumentFolder(
			DocumentFolder documentFolder)
		throws Exception {

		JSONDeserializer<DocumentFolder> jsonDeserializer =
			JSONFactoryUtil.createJSONDeserializer();

		StringBuilder sb = new StringBuilder("{");

		for (java.lang.reflect.Field field :
				getDeclaredFields(DocumentFolder.class)) {

			if (!ArrayUtil.contains(
					getAdditionalAssertFieldNames(), field.getName())) {

				continue;
			}

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append(field.getName());
			sb.append(": ");

			appendGraphQLFieldValue(sb, field.get(documentFolder));
		}

		sb.append("}");

		List<GraphQLField> graphQLFields = getGraphQLFields();

		graphQLFields.add(new GraphQLField("id"));

		return jsonDeserializer.deserialize(
			JSONUtil.getValueAsString(
				invokeGraphQLMutation(
					new GraphQLField(
						"createSiteDocumentFolder",
						new HashMap<String, Object>() {
							{
								put(
									"siteKey",
									"\"" + testGroup.getGroupId() + "\"");
								put("documentFolder", sb.toString());
							}
						},
						graphQLFields)),
				"JSONObject/data", "JSONObject/createSiteDocumentFolder"),
			DocumentFolder.class);
	}

	protected void assertContains(
		DocumentFolder documentFolder, List<DocumentFolder> documentFolders) {

		boolean contains = false;

		for (DocumentFolder item : documentFolders) {
			if (equals(documentFolder, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			documentFolders + " does not contain " + documentFolder, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		DocumentFolder documentFolder1, DocumentFolder documentFolder2) {

		Assert.assertTrue(
			documentFolder1 + " does not equal " + documentFolder2,
			equals(documentFolder1, documentFolder2));
	}

	protected void assertEquals(
		List<DocumentFolder> documentFolders1,
		List<DocumentFolder> documentFolders2) {

		Assert.assertEquals(documentFolders1.size(), documentFolders2.size());

		for (int i = 0; i < documentFolders1.size(); i++) {
			DocumentFolder documentFolder1 = documentFolders1.get(i);
			DocumentFolder documentFolder2 = documentFolders2.get(i);

			assertEquals(documentFolder1, documentFolder2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<DocumentFolder> documentFolders1,
		List<DocumentFolder> documentFolders2) {

		Assert.assertEquals(documentFolders1.size(), documentFolders2.size());

		for (DocumentFolder documentFolder1 : documentFolders1) {
			boolean contains = false;

			for (DocumentFolder documentFolder2 : documentFolders2) {
				if (equals(documentFolder1, documentFolder2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				documentFolders2 + " does not contain " + documentFolder1,
				contains);
		}
	}

	protected void assertValid(DocumentFolder documentFolder) throws Exception {
		boolean valid = true;

		if (documentFolder.getDateCreated() == null) {
			valid = false;
		}

		if (documentFolder.getDateModified() == null) {
			valid = false;
		}

		if (documentFolder.getId() == null) {
			valid = false;
		}

		Group group = testDepotEntry.getGroup();

		if (!Objects.equals(
				documentFolder.getAssetLibraryKey(), group.getGroupKey()) &&
			!Objects.equals(
				documentFolder.getSiteId(), testGroup.getGroupId())) {

			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (documentFolder.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("assetLibraryKey", additionalAssertFieldName)) {
				if (documentFolder.getAssetLibraryKey() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("creator", additionalAssertFieldName)) {
				if (documentFolder.getCreator() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (documentFolder.getCustomFields() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (documentFolder.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (documentFolder.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfDocumentFolders", additionalAssertFieldName)) {

				if (documentFolder.getNumberOfDocumentFolders() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfDocuments", additionalAssertFieldName)) {

				if (documentFolder.getNumberOfDocuments() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"parentDocumentFolderId", additionalAssertFieldName)) {

				if (documentFolder.getParentDocumentFolderId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("subscribed", additionalAssertFieldName)) {
				if (documentFolder.getSubscribed() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("viewableBy", additionalAssertFieldName)) {
				if (documentFolder.getViewableBy() == null) {
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

	protected void assertValid(Page<DocumentFolder> page) {
		boolean valid = false;

		java.util.Collection<DocumentFolder> documentFolders = page.getItems();

		int size = documentFolders.size();

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

		graphQLFields.add(new GraphQLField("siteId"));

		for (java.lang.reflect.Field field :
				getDeclaredFields(
					com.liferay.headless.delivery.dto.v1_0.DocumentFolder.
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
		DocumentFolder documentFolder1, DocumentFolder documentFolder2) {

		if (documentFolder1 == documentFolder2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)documentFolder1.getActions(),
						(Map)documentFolder2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("creator", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						documentFolder1.getCreator(),
						documentFolder2.getCreator())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						documentFolder1.getCustomFields(),
						documentFolder2.getCustomFields())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateCreated", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						documentFolder1.getDateCreated(),
						documentFolder2.getDateCreated())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						documentFolder1.getDateModified(),
						documentFolder2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						documentFolder1.getDescription(),
						documentFolder2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						documentFolder1.getId(), documentFolder2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						documentFolder1.getName(), documentFolder2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfDocumentFolders", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						documentFolder1.getNumberOfDocumentFolders(),
						documentFolder2.getNumberOfDocumentFolders())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfDocuments", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						documentFolder1.getNumberOfDocuments(),
						documentFolder2.getNumberOfDocuments())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"parentDocumentFolderId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						documentFolder1.getParentDocumentFolderId(),
						documentFolder2.getParentDocumentFolderId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("subscribed", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						documentFolder1.getSubscribed(),
						documentFolder2.getSubscribed())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("viewableBy", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						documentFolder1.getViewableBy(),
						documentFolder2.getViewableBy())) {

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

		if (!(_documentFolderResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_documentFolderResource;

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
		DocumentFolder documentFolder) {

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

		if (entityFieldName.equals("assetLibraryKey")) {
			sb.append("'");
			sb.append(String.valueOf(documentFolder.getAssetLibraryKey()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("creator")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("customFields")) {
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
							documentFolder.getDateCreated(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							documentFolder.getDateCreated(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(documentFolder.getDateCreated()));
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
							documentFolder.getDateModified(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							documentFolder.getDateModified(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(documentFolder.getDateModified()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(documentFolder.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(documentFolder.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("numberOfDocumentFolders")) {
			sb.append(
				String.valueOf(documentFolder.getNumberOfDocumentFolders()));

			return sb.toString();
		}

		if (entityFieldName.equals("numberOfDocuments")) {
			sb.append(String.valueOf(documentFolder.getNumberOfDocuments()));

			return sb.toString();
		}

		if (entityFieldName.equals("parentDocumentFolderId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("siteId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("subscribed")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("viewableBy")) {
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

	protected DocumentFolder randomDocumentFolder() throws Exception {
		return new DocumentFolder() {
			{
				assetLibraryKey = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				description = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				numberOfDocumentFolders = RandomTestUtil.randomInt();
				numberOfDocuments = RandomTestUtil.randomInt();
				parentDocumentFolderId = RandomTestUtil.randomLong();
				siteId = testGroup.getGroupId();
				subscribed = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected DocumentFolder randomIrrelevantDocumentFolder() throws Exception {
		DocumentFolder randomIrrelevantDocumentFolder = randomDocumentFolder();

		randomIrrelevantDocumentFolder.setSiteId(irrelevantGroup.getGroupId());

		return randomIrrelevantDocumentFolder;
	}

	protected DocumentFolder randomPatchDocumentFolder() throws Exception {
		return randomDocumentFolder();
	}

	protected DocumentFolderResource documentFolderResource;
	protected Group irrelevantGroup;
	protected Company testCompany;
	protected DepotEntry testDepotEntry;
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
		LogFactoryUtil.getLog(BaseDocumentFolderResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.delivery.resource.v1_0.DocumentFolderResource
		_documentFolderResource;

}