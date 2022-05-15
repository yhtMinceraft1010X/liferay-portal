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

package com.liferay.headless.admin.user.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.admin.user.client.dto.v1_0.UserAccount;
import com.liferay.headless.admin.user.client.http.HttpInvoker;
import com.liferay.headless.admin.user.client.pagination.Page;
import com.liferay.headless.admin.user.client.pagination.Pagination;
import com.liferay.headless.admin.user.client.resource.v1_0.UserAccountResource;
import com.liferay.headless.admin.user.client.serdes.v1_0.UserAccountSerDes;
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
import com.liferay.portal.kernel.util.GetterUtil;
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
public abstract class BaseUserAccountResourceTestCase {

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

		_userAccountResource.setContextCompany(testCompany);

		UserAccountResource.Builder builder = UserAccountResource.builder();

		userAccountResource = builder.authentication(
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

		UserAccount userAccount1 = randomUserAccount();

		String json = objectMapper.writeValueAsString(userAccount1);

		UserAccount userAccount2 = UserAccountSerDes.toDTO(json);

		Assert.assertTrue(equals(userAccount1, userAccount2));
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

		UserAccount userAccount = randomUserAccount();

		String json1 = objectMapper.writeValueAsString(userAccount);
		String json2 = UserAccountSerDes.toJSON(userAccount);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		UserAccount userAccount = randomUserAccount();

		userAccount.setAdditionalName(regex);
		userAccount.setAlternateName(regex);
		userAccount.setDashboardURL(regex);
		userAccount.setEmailAddress(regex);
		userAccount.setExternalReferenceCode(regex);
		userAccount.setFamilyName(regex);
		userAccount.setGivenName(regex);
		userAccount.setHonorificPrefix(regex);
		userAccount.setHonorificSuffix(regex);
		userAccount.setImage(regex);
		userAccount.setJobTitle(regex);
		userAccount.setName(regex);
		userAccount.setPassword(regex);
		userAccount.setProfileURL(regex);

		String json = UserAccountSerDes.toJSON(userAccount);

		Assert.assertFalse(json.contains(regex));

		userAccount = UserAccountSerDes.toDTO(json);

		Assert.assertEquals(regex, userAccount.getAdditionalName());
		Assert.assertEquals(regex, userAccount.getAlternateName());
		Assert.assertEquals(regex, userAccount.getDashboardURL());
		Assert.assertEquals(regex, userAccount.getEmailAddress());
		Assert.assertEquals(regex, userAccount.getExternalReferenceCode());
		Assert.assertEquals(regex, userAccount.getFamilyName());
		Assert.assertEquals(regex, userAccount.getGivenName());
		Assert.assertEquals(regex, userAccount.getHonorificPrefix());
		Assert.assertEquals(regex, userAccount.getHonorificSuffix());
		Assert.assertEquals(regex, userAccount.getImage());
		Assert.assertEquals(regex, userAccount.getJobTitle());
		Assert.assertEquals(regex, userAccount.getName());
		Assert.assertEquals(regex, userAccount.getPassword());
		Assert.assertEquals(regex, userAccount.getProfileURL());
	}

	@Test
	public void testDeleteAccountByExternalReferenceCodeUserAccountByExternalReferenceCode()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount =
			testDeleteAccountByExternalReferenceCodeUserAccountByExternalReferenceCode_addUserAccount();

		assertHttpResponseStatusCode(
			204,
			userAccountResource.
				deleteAccountByExternalReferenceCodeUserAccountByExternalReferenceCodeHttpResponse(
					testDeleteAccountByExternalReferenceCodeUserAccountByExternalReferenceCode_getAccountExternalReferenceCode(),
					testDeleteAccountByExternalReferenceCodeUserAccountByExternalReferenceCode_getUserAccountExternalReferenceCode()));
	}

	protected String
			testDeleteAccountByExternalReferenceCodeUserAccountByExternalReferenceCode_getAccountExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testDeleteAccountByExternalReferenceCodeUserAccountByExternalReferenceCode_getUserAccountExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected UserAccount
			testDeleteAccountByExternalReferenceCodeUserAccountByExternalReferenceCode_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPostAccountByExternalReferenceCodeUserAccountByExternalReferenceCode()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount =
			testPostAccountByExternalReferenceCodeUserAccountByExternalReferenceCode_addUserAccount();

		assertHttpResponseStatusCode(
			204,
			userAccountResource.
				postAccountByExternalReferenceCodeUserAccountByExternalReferenceCodeHttpResponse(
					null, null));

		assertHttpResponseStatusCode(
			404,
			userAccountResource.
				postAccountByExternalReferenceCodeUserAccountByExternalReferenceCodeHttpResponse(
					null, null));
	}

	protected UserAccount
			testPostAccountByExternalReferenceCodeUserAccountByExternalReferenceCode_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountUserAccountsByExternalReferenceCodePage()
		throws Exception {

		String externalReferenceCode =
			testGetAccountUserAccountsByExternalReferenceCodePage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetAccountUserAccountsByExternalReferenceCodePage_getIrrelevantExternalReferenceCode();

		Page<UserAccount> page =
			userAccountResource.
				getAccountUserAccountsByExternalReferenceCodePage(
					externalReferenceCode, null, null, Pagination.of(1, 10),
					null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			UserAccount irrelevantUserAccount =
				testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
					irrelevantExternalReferenceCode,
					randomIrrelevantUserAccount());

			page =
				userAccountResource.
					getAccountUserAccountsByExternalReferenceCodePage(
						irrelevantExternalReferenceCode, null, null,
						Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantUserAccount),
				(List<UserAccount>)page.getItems());
			assertValid(page);
		}

		UserAccount userAccount1 =
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				externalReferenceCode, randomUserAccount());

		UserAccount userAccount2 =
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				externalReferenceCode, randomUserAccount());

		page =
			userAccountResource.
				getAccountUserAccountsByExternalReferenceCodePage(
					externalReferenceCode, null, null, Pagination.of(1, 10),
					null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(userAccount1, userAccount2),
			(List<UserAccount>)page.getItems());
		assertValid(page);

		userAccountResource.deleteUserAccount(userAccount1.getId());

		userAccountResource.deleteUserAccount(userAccount2.getId());
	}

	@Test
	public void testGetAccountUserAccountsByExternalReferenceCodePageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		String externalReferenceCode =
			testGetAccountUserAccountsByExternalReferenceCodePage_getExternalReferenceCode();

		UserAccount userAccount1 = randomUserAccount();

		userAccount1 =
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				externalReferenceCode, userAccount1);

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page =
				userAccountResource.
					getAccountUserAccountsByExternalReferenceCodePage(
						externalReferenceCode, null,
						getFilterString(entityField, "between", userAccount1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetAccountUserAccountsByExternalReferenceCodePageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		String externalReferenceCode =
			testGetAccountUserAccountsByExternalReferenceCodePage_getExternalReferenceCode();

		UserAccount userAccount1 =
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				externalReferenceCode, randomUserAccount());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount2 =
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				externalReferenceCode, randomUserAccount());

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page =
				userAccountResource.
					getAccountUserAccountsByExternalReferenceCodePage(
						externalReferenceCode, null,
						getFilterString(entityField, "eq", userAccount1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetAccountUserAccountsByExternalReferenceCodePageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		String externalReferenceCode =
			testGetAccountUserAccountsByExternalReferenceCodePage_getExternalReferenceCode();

		UserAccount userAccount1 =
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				externalReferenceCode, randomUserAccount());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount2 =
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				externalReferenceCode, randomUserAccount());

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page =
				userAccountResource.
					getAccountUserAccountsByExternalReferenceCodePage(
						externalReferenceCode, null,
						getFilterString(entityField, "eq", userAccount1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetAccountUserAccountsByExternalReferenceCodePageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetAccountUserAccountsByExternalReferenceCodePage_getExternalReferenceCode();

		UserAccount userAccount1 =
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				externalReferenceCode, randomUserAccount());

		UserAccount userAccount2 =
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				externalReferenceCode, randomUserAccount());

		UserAccount userAccount3 =
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				externalReferenceCode, randomUserAccount());

		Page<UserAccount> page1 =
			userAccountResource.
				getAccountUserAccountsByExternalReferenceCodePage(
					externalReferenceCode, null, null, Pagination.of(1, 2),
					null);

		List<UserAccount> userAccounts1 = (List<UserAccount>)page1.getItems();

		Assert.assertEquals(userAccounts1.toString(), 2, userAccounts1.size());

		Page<UserAccount> page2 =
			userAccountResource.
				getAccountUserAccountsByExternalReferenceCodePage(
					externalReferenceCode, null, null, Pagination.of(2, 2),
					null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<UserAccount> userAccounts2 = (List<UserAccount>)page2.getItems();

		Assert.assertEquals(userAccounts2.toString(), 1, userAccounts2.size());

		Page<UserAccount> page3 =
			userAccountResource.
				getAccountUserAccountsByExternalReferenceCodePage(
					externalReferenceCode, null, null, Pagination.of(1, 3),
					null);

		assertEqualsIgnoringOrder(
			Arrays.asList(userAccount1, userAccount2, userAccount3),
			(List<UserAccount>)page3.getItems());
	}

	@Test
	public void testGetAccountUserAccountsByExternalReferenceCodePageWithSortDateTime()
		throws Exception {

		testGetAccountUserAccountsByExternalReferenceCodePageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetAccountUserAccountsByExternalReferenceCodePageWithSortDouble()
		throws Exception {

		testGetAccountUserAccountsByExternalReferenceCodePageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					userAccount2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetAccountUserAccountsByExternalReferenceCodePageWithSortInteger()
		throws Exception {

		testGetAccountUserAccountsByExternalReferenceCodePageWithSort(
			EntityField.Type.INTEGER,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					userAccount2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetAccountUserAccountsByExternalReferenceCodePageWithSortString()
		throws Exception {

		testGetAccountUserAccountsByExternalReferenceCodePageWithSort(
			EntityField.Type.STRING,
			(entityField, userAccount1, userAccount2) -> {
				Class<?> clazz = userAccount1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void
			testGetAccountUserAccountsByExternalReferenceCodePageWithSort(
				EntityField.Type type,
				UnsafeTriConsumer
					<EntityField, UserAccount, UserAccount, Exception>
						unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		String externalReferenceCode =
			testGetAccountUserAccountsByExternalReferenceCodePage_getExternalReferenceCode();

		UserAccount userAccount1 = randomUserAccount();
		UserAccount userAccount2 = randomUserAccount();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, userAccount1, userAccount2);
		}

		userAccount1 =
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				externalReferenceCode, userAccount1);

		userAccount2 =
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				externalReferenceCode, userAccount2);

		for (EntityField entityField : entityFields) {
			Page<UserAccount> ascPage =
				userAccountResource.
					getAccountUserAccountsByExternalReferenceCodePage(
						externalReferenceCode, null, null, Pagination.of(1, 2),
						entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(userAccount1, userAccount2),
				(List<UserAccount>)ascPage.getItems());

			Page<UserAccount> descPage =
				userAccountResource.
					getAccountUserAccountsByExternalReferenceCodePage(
						externalReferenceCode, null, null, Pagination.of(1, 2),
						entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(userAccount2, userAccount1),
				(List<UserAccount>)descPage.getItems());
		}
	}

	protected UserAccount
			testGetAccountUserAccountsByExternalReferenceCodePage_addUserAccount(
				String externalReferenceCode, UserAccount userAccount)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountUserAccountsByExternalReferenceCodePage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountUserAccountsByExternalReferenceCodePage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountUserAccountByExternalReferenceCode()
		throws Exception {

		UserAccount randomUserAccount = randomUserAccount();

		UserAccount postUserAccount =
			testPostAccountUserAccountByExternalReferenceCode_addUserAccount(
				randomUserAccount);

		assertEquals(randomUserAccount, postUserAccount);
		assertValid(postUserAccount);
	}

	protected UserAccount
			testPostAccountUserAccountByExternalReferenceCode_addUserAccount(
				UserAccount userAccount)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteAccountUserAccountsByExternalReferenceCodeByEmailAddress()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount =
			testDeleteAccountUserAccountsByExternalReferenceCodeByEmailAddress_addUserAccount();

		assertHttpResponseStatusCode(
			204,
			userAccountResource.
				deleteAccountUserAccountsByExternalReferenceCodeByEmailAddressHttpResponse(
					userAccount.getExternalReferenceCode(), null));
	}

	protected UserAccount
			testDeleteAccountUserAccountsByExternalReferenceCodeByEmailAddress_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPostAccountUserAccountsByExternalReferenceCodeByEmailAddress()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount =
			testPostAccountUserAccountsByExternalReferenceCodeByEmailAddress_addUserAccount();

		assertHttpResponseStatusCode(
			204,
			userAccountResource.
				postAccountUserAccountsByExternalReferenceCodeByEmailAddressHttpResponse(
					userAccount.getExternalReferenceCode(), null));

		assertHttpResponseStatusCode(
			404,
			userAccountResource.
				postAccountUserAccountsByExternalReferenceCodeByEmailAddressHttpResponse(
					userAccount.getExternalReferenceCode(), null));
	}

	protected UserAccount
			testPostAccountUserAccountsByExternalReferenceCodeByEmailAddress_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteAccountUserAccountByExternalReferenceCodeByEmailAddress()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount =
			testDeleteAccountUserAccountByExternalReferenceCodeByEmailAddress_addUserAccount();

		assertHttpResponseStatusCode(
			204,
			userAccountResource.
				deleteAccountUserAccountByExternalReferenceCodeByEmailAddressHttpResponse(
					userAccount.getExternalReferenceCode(),
					userAccount.getEmailAddress()));
	}

	protected UserAccount
			testDeleteAccountUserAccountByExternalReferenceCodeByEmailAddress_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPostAccountUserAccountByExternalReferenceCodeByEmailAddress()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount =
			testPostAccountUserAccountByExternalReferenceCodeByEmailAddress_addUserAccount();

		assertHttpResponseStatusCode(
			204,
			userAccountResource.
				postAccountUserAccountByExternalReferenceCodeByEmailAddressHttpResponse(
					userAccount.getExternalReferenceCode(),
					userAccount.getEmailAddress()));

		assertHttpResponseStatusCode(
			404,
			userAccountResource.
				postAccountUserAccountByExternalReferenceCodeByEmailAddressHttpResponse(
					userAccount.getExternalReferenceCode(),
					userAccount.getEmailAddress()));
	}

	protected UserAccount
			testPostAccountUserAccountByExternalReferenceCodeByEmailAddress_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountUserAccountsPage() throws Exception {
		Long accountId = testGetAccountUserAccountsPage_getAccountId();
		Long irrelevantAccountId =
			testGetAccountUserAccountsPage_getIrrelevantAccountId();

		Page<UserAccount> page = userAccountResource.getAccountUserAccountsPage(
			accountId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantAccountId != null) {
			UserAccount irrelevantUserAccount =
				testGetAccountUserAccountsPage_addUserAccount(
					irrelevantAccountId, randomIrrelevantUserAccount());

			page = userAccountResource.getAccountUserAccountsPage(
				irrelevantAccountId, null, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantUserAccount),
				(List<UserAccount>)page.getItems());
			assertValid(page);
		}

		UserAccount userAccount1 =
			testGetAccountUserAccountsPage_addUserAccount(
				accountId, randomUserAccount());

		UserAccount userAccount2 =
			testGetAccountUserAccountsPage_addUserAccount(
				accountId, randomUserAccount());

		page = userAccountResource.getAccountUserAccountsPage(
			accountId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(userAccount1, userAccount2),
			(List<UserAccount>)page.getItems());
		assertValid(page);

		userAccountResource.deleteUserAccount(userAccount1.getId());

		userAccountResource.deleteUserAccount(userAccount2.getId());
	}

	@Test
	public void testGetAccountUserAccountsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long accountId = testGetAccountUserAccountsPage_getAccountId();

		UserAccount userAccount1 = randomUserAccount();

		userAccount1 = testGetAccountUserAccountsPage_addUserAccount(
			accountId, userAccount1);

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page =
				userAccountResource.getAccountUserAccountsPage(
					accountId, null,
					getFilterString(entityField, "between", userAccount1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetAccountUserAccountsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long accountId = testGetAccountUserAccountsPage_getAccountId();

		UserAccount userAccount1 =
			testGetAccountUserAccountsPage_addUserAccount(
				accountId, randomUserAccount());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount2 =
			testGetAccountUserAccountsPage_addUserAccount(
				accountId, randomUserAccount());

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page =
				userAccountResource.getAccountUserAccountsPage(
					accountId, null,
					getFilterString(entityField, "eq", userAccount1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetAccountUserAccountsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long accountId = testGetAccountUserAccountsPage_getAccountId();

		UserAccount userAccount1 =
			testGetAccountUserAccountsPage_addUserAccount(
				accountId, randomUserAccount());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount2 =
			testGetAccountUserAccountsPage_addUserAccount(
				accountId, randomUserAccount());

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page =
				userAccountResource.getAccountUserAccountsPage(
					accountId, null,
					getFilterString(entityField, "eq", userAccount1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetAccountUserAccountsPageWithPagination()
		throws Exception {

		Long accountId = testGetAccountUserAccountsPage_getAccountId();

		UserAccount userAccount1 =
			testGetAccountUserAccountsPage_addUserAccount(
				accountId, randomUserAccount());

		UserAccount userAccount2 =
			testGetAccountUserAccountsPage_addUserAccount(
				accountId, randomUserAccount());

		UserAccount userAccount3 =
			testGetAccountUserAccountsPage_addUserAccount(
				accountId, randomUserAccount());

		Page<UserAccount> page1 =
			userAccountResource.getAccountUserAccountsPage(
				accountId, null, null, Pagination.of(1, 2), null);

		List<UserAccount> userAccounts1 = (List<UserAccount>)page1.getItems();

		Assert.assertEquals(userAccounts1.toString(), 2, userAccounts1.size());

		Page<UserAccount> page2 =
			userAccountResource.getAccountUserAccountsPage(
				accountId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<UserAccount> userAccounts2 = (List<UserAccount>)page2.getItems();

		Assert.assertEquals(userAccounts2.toString(), 1, userAccounts2.size());

		Page<UserAccount> page3 =
			userAccountResource.getAccountUserAccountsPage(
				accountId, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(userAccount1, userAccount2, userAccount3),
			(List<UserAccount>)page3.getItems());
	}

	@Test
	public void testGetAccountUserAccountsPageWithSortDateTime()
		throws Exception {

		testGetAccountUserAccountsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetAccountUserAccountsPageWithSortDouble()
		throws Exception {

		testGetAccountUserAccountsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					userAccount2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetAccountUserAccountsPageWithSortInteger()
		throws Exception {

		testGetAccountUserAccountsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					userAccount2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetAccountUserAccountsPageWithSortString()
		throws Exception {

		testGetAccountUserAccountsPageWithSort(
			EntityField.Type.STRING,
			(entityField, userAccount1, userAccount2) -> {
				Class<?> clazz = userAccount1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetAccountUserAccountsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, UserAccount, UserAccount, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long accountId = testGetAccountUserAccountsPage_getAccountId();

		UserAccount userAccount1 = randomUserAccount();
		UserAccount userAccount2 = randomUserAccount();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, userAccount1, userAccount2);
		}

		userAccount1 = testGetAccountUserAccountsPage_addUserAccount(
			accountId, userAccount1);

		userAccount2 = testGetAccountUserAccountsPage_addUserAccount(
			accountId, userAccount2);

		for (EntityField entityField : entityFields) {
			Page<UserAccount> ascPage =
				userAccountResource.getAccountUserAccountsPage(
					accountId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(userAccount1, userAccount2),
				(List<UserAccount>)ascPage.getItems());

			Page<UserAccount> descPage =
				userAccountResource.getAccountUserAccountsPage(
					accountId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(userAccount2, userAccount1),
				(List<UserAccount>)descPage.getItems());
		}
	}

	protected UserAccount testGetAccountUserAccountsPage_addUserAccount(
			Long accountId, UserAccount userAccount)
		throws Exception {

		return userAccountResource.postAccountUserAccount(
			accountId, userAccount);
	}

	protected Long testGetAccountUserAccountsPage_getAccountId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountUserAccountsPage_getIrrelevantAccountId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountUserAccount() throws Exception {
		UserAccount randomUserAccount = randomUserAccount();

		UserAccount postUserAccount = testPostAccountUserAccount_addUserAccount(
			randomUserAccount);

		assertEquals(randomUserAccount, postUserAccount);
		assertValid(postUserAccount);
	}

	protected UserAccount testPostAccountUserAccount_addUserAccount(
			UserAccount userAccount)
		throws Exception {

		return userAccountResource.postAccountUserAccount(
			testGetAccountUserAccountsPage_getAccountId(), userAccount);
	}

	@Test
	public void testDeleteAccountUserAccountsByEmailAddress() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount =
			testDeleteAccountUserAccountsByEmailAddress_addUserAccount();

		assertHttpResponseStatusCode(
			204,
			userAccountResource.
				deleteAccountUserAccountsByEmailAddressHttpResponse(
					testDeleteAccountUserAccountsByEmailAddress_getAccountId(),
					null));
	}

	protected Long testDeleteAccountUserAccountsByEmailAddress_getAccountId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected UserAccount
			testDeleteAccountUserAccountsByEmailAddress_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPostAccountUserAccountsByEmailAddress() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testDeleteAccountUserAccountByEmailAddress() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount =
			testDeleteAccountUserAccountByEmailAddress_addUserAccount();

		assertHttpResponseStatusCode(
			204,
			userAccountResource.
				deleteAccountUserAccountByEmailAddressHttpResponse(
					testDeleteAccountUserAccountByEmailAddress_getAccountId(),
					userAccount.getEmailAddress()));
	}

	protected Long testDeleteAccountUserAccountByEmailAddress_getAccountId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected UserAccount
			testDeleteAccountUserAccountByEmailAddress_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPostAccountUserAccountByEmailAddress() throws Exception {
		UserAccount randomUserAccount = randomUserAccount();

		UserAccount postUserAccount =
			testPostAccountUserAccountByEmailAddress_addUserAccount(
				randomUserAccount);

		assertEquals(randomUserAccount, postUserAccount);
		assertValid(postUserAccount);
	}

	protected UserAccount
			testPostAccountUserAccountByEmailAddress_addUserAccount(
				UserAccount userAccount)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetMyUserAccount() throws Exception {
		UserAccount postUserAccount = testGetMyUserAccount_addUserAccount();

		UserAccount getUserAccount = userAccountResource.getMyUserAccount();

		assertEquals(postUserAccount, getUserAccount);
		assertValid(getUserAccount);
	}

	protected UserAccount testGetMyUserAccount_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetMyUserAccount() throws Exception {
		UserAccount userAccount = testGraphQLGetMyUserAccount_addUserAccount();

		Assert.assertTrue(
			equals(
				userAccount,
				UserAccountSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"myUserAccount",
								new HashMap<String, Object>() {
									{
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/myUserAccount"))));
	}

	@Test
	public void testGraphQLGetMyUserAccountNotFound() throws Exception {
		Assert.assertTrue(true);
	}

	protected UserAccount testGraphQLGetMyUserAccount_addUserAccount()
		throws Exception {

		return testGraphQLUserAccount_addUserAccount();
	}

	@Test
	public void testGetOrganizationUserAccountsPage() throws Exception {
		String organizationId =
			testGetOrganizationUserAccountsPage_getOrganizationId();
		String irrelevantOrganizationId =
			testGetOrganizationUserAccountsPage_getIrrelevantOrganizationId();

		Page<UserAccount> page =
			userAccountResource.getOrganizationUserAccountsPage(
				organizationId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantOrganizationId != null) {
			UserAccount irrelevantUserAccount =
				testGetOrganizationUserAccountsPage_addUserAccount(
					irrelevantOrganizationId, randomIrrelevantUserAccount());

			page = userAccountResource.getOrganizationUserAccountsPage(
				irrelevantOrganizationId, null, null, Pagination.of(1, 2),
				null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantUserAccount),
				(List<UserAccount>)page.getItems());
			assertValid(page);
		}

		UserAccount userAccount1 =
			testGetOrganizationUserAccountsPage_addUserAccount(
				organizationId, randomUserAccount());

		UserAccount userAccount2 =
			testGetOrganizationUserAccountsPage_addUserAccount(
				organizationId, randomUserAccount());

		page = userAccountResource.getOrganizationUserAccountsPage(
			organizationId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(userAccount1, userAccount2),
			(List<UserAccount>)page.getItems());
		assertValid(page);

		userAccountResource.deleteUserAccount(userAccount1.getId());

		userAccountResource.deleteUserAccount(userAccount2.getId());
	}

	@Test
	public void testGetOrganizationUserAccountsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		String organizationId =
			testGetOrganizationUserAccountsPage_getOrganizationId();

		UserAccount userAccount1 = randomUserAccount();

		userAccount1 = testGetOrganizationUserAccountsPage_addUserAccount(
			organizationId, userAccount1);

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page =
				userAccountResource.getOrganizationUserAccountsPage(
					organizationId, null,
					getFilterString(entityField, "between", userAccount1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetOrganizationUserAccountsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		String organizationId =
			testGetOrganizationUserAccountsPage_getOrganizationId();

		UserAccount userAccount1 =
			testGetOrganizationUserAccountsPage_addUserAccount(
				organizationId, randomUserAccount());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount2 =
			testGetOrganizationUserAccountsPage_addUserAccount(
				organizationId, randomUserAccount());

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page =
				userAccountResource.getOrganizationUserAccountsPage(
					organizationId, null,
					getFilterString(entityField, "eq", userAccount1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetOrganizationUserAccountsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		String organizationId =
			testGetOrganizationUserAccountsPage_getOrganizationId();

		UserAccount userAccount1 =
			testGetOrganizationUserAccountsPage_addUserAccount(
				organizationId, randomUserAccount());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount2 =
			testGetOrganizationUserAccountsPage_addUserAccount(
				organizationId, randomUserAccount());

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page =
				userAccountResource.getOrganizationUserAccountsPage(
					organizationId, null,
					getFilterString(entityField, "eq", userAccount1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetOrganizationUserAccountsPageWithPagination()
		throws Exception {

		String organizationId =
			testGetOrganizationUserAccountsPage_getOrganizationId();

		UserAccount userAccount1 =
			testGetOrganizationUserAccountsPage_addUserAccount(
				organizationId, randomUserAccount());

		UserAccount userAccount2 =
			testGetOrganizationUserAccountsPage_addUserAccount(
				organizationId, randomUserAccount());

		UserAccount userAccount3 =
			testGetOrganizationUserAccountsPage_addUserAccount(
				organizationId, randomUserAccount());

		Page<UserAccount> page1 =
			userAccountResource.getOrganizationUserAccountsPage(
				organizationId, null, null, Pagination.of(1, 2), null);

		List<UserAccount> userAccounts1 = (List<UserAccount>)page1.getItems();

		Assert.assertEquals(userAccounts1.toString(), 2, userAccounts1.size());

		Page<UserAccount> page2 =
			userAccountResource.getOrganizationUserAccountsPage(
				organizationId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<UserAccount> userAccounts2 = (List<UserAccount>)page2.getItems();

		Assert.assertEquals(userAccounts2.toString(), 1, userAccounts2.size());

		Page<UserAccount> page3 =
			userAccountResource.getOrganizationUserAccountsPage(
				organizationId, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(userAccount1, userAccount2, userAccount3),
			(List<UserAccount>)page3.getItems());
	}

	@Test
	public void testGetOrganizationUserAccountsPageWithSortDateTime()
		throws Exception {

		testGetOrganizationUserAccountsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetOrganizationUserAccountsPageWithSortDouble()
		throws Exception {

		testGetOrganizationUserAccountsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					userAccount2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetOrganizationUserAccountsPageWithSortInteger()
		throws Exception {

		testGetOrganizationUserAccountsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					userAccount2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetOrganizationUserAccountsPageWithSortString()
		throws Exception {

		testGetOrganizationUserAccountsPageWithSort(
			EntityField.Type.STRING,
			(entityField, userAccount1, userAccount2) -> {
				Class<?> clazz = userAccount1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetOrganizationUserAccountsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, UserAccount, UserAccount, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		String organizationId =
			testGetOrganizationUserAccountsPage_getOrganizationId();

		UserAccount userAccount1 = randomUserAccount();
		UserAccount userAccount2 = randomUserAccount();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, userAccount1, userAccount2);
		}

		userAccount1 = testGetOrganizationUserAccountsPage_addUserAccount(
			organizationId, userAccount1);

		userAccount2 = testGetOrganizationUserAccountsPage_addUserAccount(
			organizationId, userAccount2);

		for (EntityField entityField : entityFields) {
			Page<UserAccount> ascPage =
				userAccountResource.getOrganizationUserAccountsPage(
					organizationId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(userAccount1, userAccount2),
				(List<UserAccount>)ascPage.getItems());

			Page<UserAccount> descPage =
				userAccountResource.getOrganizationUserAccountsPage(
					organizationId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(userAccount2, userAccount1),
				(List<UserAccount>)descPage.getItems());
		}
	}

	protected UserAccount testGetOrganizationUserAccountsPage_addUserAccount(
			String organizationId, UserAccount userAccount)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String testGetOrganizationUserAccountsPage_getOrganizationId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetOrganizationUserAccountsPage_getIrrelevantOrganizationId()
		throws Exception {

		return null;
	}

	@Test
	public void testGetSiteUserAccountsPage() throws Exception {
		Long siteId = testGetSiteUserAccountsPage_getSiteId();
		Long irrelevantSiteId =
			testGetSiteUserAccountsPage_getIrrelevantSiteId();

		Page<UserAccount> page = userAccountResource.getSiteUserAccountsPage(
			siteId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantSiteId != null) {
			UserAccount irrelevantUserAccount =
				testGetSiteUserAccountsPage_addUserAccount(
					irrelevantSiteId, randomIrrelevantUserAccount());

			page = userAccountResource.getSiteUserAccountsPage(
				irrelevantSiteId, null, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantUserAccount),
				(List<UserAccount>)page.getItems());
			assertValid(page);
		}

		UserAccount userAccount1 = testGetSiteUserAccountsPage_addUserAccount(
			siteId, randomUserAccount());

		UserAccount userAccount2 = testGetSiteUserAccountsPage_addUserAccount(
			siteId, randomUserAccount());

		page = userAccountResource.getSiteUserAccountsPage(
			siteId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(userAccount1, userAccount2),
			(List<UserAccount>)page.getItems());
		assertValid(page);

		userAccountResource.deleteUserAccount(userAccount1.getId());

		userAccountResource.deleteUserAccount(userAccount2.getId());
	}

	@Test
	public void testGetSiteUserAccountsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteUserAccountsPage_getSiteId();

		UserAccount userAccount1 = randomUserAccount();

		userAccount1 = testGetSiteUserAccountsPage_addUserAccount(
			siteId, userAccount1);

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page =
				userAccountResource.getSiteUserAccountsPage(
					siteId, null,
					getFilterString(entityField, "between", userAccount1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetSiteUserAccountsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteUserAccountsPage_getSiteId();

		UserAccount userAccount1 = testGetSiteUserAccountsPage_addUserAccount(
			siteId, randomUserAccount());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount2 = testGetSiteUserAccountsPage_addUserAccount(
			siteId, randomUserAccount());

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page =
				userAccountResource.getSiteUserAccountsPage(
					siteId, null,
					getFilterString(entityField, "eq", userAccount1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetSiteUserAccountsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteUserAccountsPage_getSiteId();

		UserAccount userAccount1 = testGetSiteUserAccountsPage_addUserAccount(
			siteId, randomUserAccount());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount2 = testGetSiteUserAccountsPage_addUserAccount(
			siteId, randomUserAccount());

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page =
				userAccountResource.getSiteUserAccountsPage(
					siteId, null,
					getFilterString(entityField, "eq", userAccount1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetSiteUserAccountsPageWithPagination() throws Exception {
		Long siteId = testGetSiteUserAccountsPage_getSiteId();

		UserAccount userAccount1 = testGetSiteUserAccountsPage_addUserAccount(
			siteId, randomUserAccount());

		UserAccount userAccount2 = testGetSiteUserAccountsPage_addUserAccount(
			siteId, randomUserAccount());

		UserAccount userAccount3 = testGetSiteUserAccountsPage_addUserAccount(
			siteId, randomUserAccount());

		Page<UserAccount> page1 = userAccountResource.getSiteUserAccountsPage(
			siteId, null, null, Pagination.of(1, 2), null);

		List<UserAccount> userAccounts1 = (List<UserAccount>)page1.getItems();

		Assert.assertEquals(userAccounts1.toString(), 2, userAccounts1.size());

		Page<UserAccount> page2 = userAccountResource.getSiteUserAccountsPage(
			siteId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<UserAccount> userAccounts2 = (List<UserAccount>)page2.getItems();

		Assert.assertEquals(userAccounts2.toString(), 1, userAccounts2.size());

		Page<UserAccount> page3 = userAccountResource.getSiteUserAccountsPage(
			siteId, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(userAccount1, userAccount2, userAccount3),
			(List<UserAccount>)page3.getItems());
	}

	@Test
	public void testGetSiteUserAccountsPageWithSortDateTime() throws Exception {
		testGetSiteUserAccountsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetSiteUserAccountsPageWithSortDouble() throws Exception {
		testGetSiteUserAccountsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					userAccount2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetSiteUserAccountsPageWithSortInteger() throws Exception {
		testGetSiteUserAccountsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					userAccount2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetSiteUserAccountsPageWithSortString() throws Exception {
		testGetSiteUserAccountsPageWithSort(
			EntityField.Type.STRING,
			(entityField, userAccount1, userAccount2) -> {
				Class<?> clazz = userAccount1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetSiteUserAccountsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, UserAccount, UserAccount, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteUserAccountsPage_getSiteId();

		UserAccount userAccount1 = randomUserAccount();
		UserAccount userAccount2 = randomUserAccount();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, userAccount1, userAccount2);
		}

		userAccount1 = testGetSiteUserAccountsPage_addUserAccount(
			siteId, userAccount1);

		userAccount2 = testGetSiteUserAccountsPage_addUserAccount(
			siteId, userAccount2);

		for (EntityField entityField : entityFields) {
			Page<UserAccount> ascPage =
				userAccountResource.getSiteUserAccountsPage(
					siteId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(userAccount1, userAccount2),
				(List<UserAccount>)ascPage.getItems());

			Page<UserAccount> descPage =
				userAccountResource.getSiteUserAccountsPage(
					siteId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(userAccount2, userAccount1),
				(List<UserAccount>)descPage.getItems());
		}
	}

	protected UserAccount testGetSiteUserAccountsPage_addUserAccount(
			Long siteId, UserAccount userAccount)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetSiteUserAccountsPage_getSiteId() throws Exception {
		return testGroup.getGroupId();
	}

	protected Long testGetSiteUserAccountsPage_getIrrelevantSiteId()
		throws Exception {

		return irrelevantGroup.getGroupId();
	}

	@Test
	public void testGetUserAccountsPage() throws Exception {
		Page<UserAccount> page = userAccountResource.getUserAccountsPage(
			null, null, Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		UserAccount userAccount1 = testGetUserAccountsPage_addUserAccount(
			randomUserAccount());

		UserAccount userAccount2 = testGetUserAccountsPage_addUserAccount(
			randomUserAccount());

		page = userAccountResource.getUserAccountsPage(
			null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(userAccount1, (List<UserAccount>)page.getItems());
		assertContains(userAccount2, (List<UserAccount>)page.getItems());
		assertValid(page);

		userAccountResource.deleteUserAccount(userAccount1.getId());

		userAccountResource.deleteUserAccount(userAccount2.getId());
	}

	@Test
	public void testGetUserAccountsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		UserAccount userAccount1 = randomUserAccount();

		userAccount1 = testGetUserAccountsPage_addUserAccount(userAccount1);

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page = userAccountResource.getUserAccountsPage(
				null, getFilterString(entityField, "between", userAccount1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetUserAccountsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		UserAccount userAccount1 = testGetUserAccountsPage_addUserAccount(
			randomUserAccount());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount2 = testGetUserAccountsPage_addUserAccount(
			randomUserAccount());

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page = userAccountResource.getUserAccountsPage(
				null, getFilterString(entityField, "eq", userAccount1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetUserAccountsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		UserAccount userAccount1 = testGetUserAccountsPage_addUserAccount(
			randomUserAccount());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount2 = testGetUserAccountsPage_addUserAccount(
			randomUserAccount());

		for (EntityField entityField : entityFields) {
			Page<UserAccount> page = userAccountResource.getUserAccountsPage(
				null, getFilterString(entityField, "eq", userAccount1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(userAccount1),
				(List<UserAccount>)page.getItems());
		}
	}

	@Test
	public void testGetUserAccountsPageWithPagination() throws Exception {
		Page<UserAccount> totalPage = userAccountResource.getUserAccountsPage(
			null, null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		UserAccount userAccount1 = testGetUserAccountsPage_addUserAccount(
			randomUserAccount());

		UserAccount userAccount2 = testGetUserAccountsPage_addUserAccount(
			randomUserAccount());

		UserAccount userAccount3 = testGetUserAccountsPage_addUserAccount(
			randomUserAccount());

		Page<UserAccount> page1 = userAccountResource.getUserAccountsPage(
			null, null, Pagination.of(1, totalCount + 2), null);

		List<UserAccount> userAccounts1 = (List<UserAccount>)page1.getItems();

		Assert.assertEquals(
			userAccounts1.toString(), totalCount + 2, userAccounts1.size());

		Page<UserAccount> page2 = userAccountResource.getUserAccountsPage(
			null, null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<UserAccount> userAccounts2 = (List<UserAccount>)page2.getItems();

		Assert.assertEquals(userAccounts2.toString(), 1, userAccounts2.size());

		Page<UserAccount> page3 = userAccountResource.getUserAccountsPage(
			null, null, Pagination.of(1, totalCount + 3), null);

		assertContains(userAccount1, (List<UserAccount>)page3.getItems());
		assertContains(userAccount2, (List<UserAccount>)page3.getItems());
		assertContains(userAccount3, (List<UserAccount>)page3.getItems());
	}

	@Test
	public void testGetUserAccountsPageWithSortDateTime() throws Exception {
		testGetUserAccountsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetUserAccountsPageWithSortDouble() throws Exception {
		testGetUserAccountsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					userAccount2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetUserAccountsPageWithSortInteger() throws Exception {
		testGetUserAccountsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, userAccount1, userAccount2) -> {
				BeanTestUtil.setProperty(
					userAccount1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					userAccount2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetUserAccountsPageWithSortString() throws Exception {
		testGetUserAccountsPageWithSort(
			EntityField.Type.STRING,
			(entityField, userAccount1, userAccount2) -> {
				Class<?> clazz = userAccount1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						userAccount1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						userAccount2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetUserAccountsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, UserAccount, UserAccount, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		UserAccount userAccount1 = randomUserAccount();
		UserAccount userAccount2 = randomUserAccount();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, userAccount1, userAccount2);
		}

		userAccount1 = testGetUserAccountsPage_addUserAccount(userAccount1);

		userAccount2 = testGetUserAccountsPage_addUserAccount(userAccount2);

		for (EntityField entityField : entityFields) {
			Page<UserAccount> ascPage = userAccountResource.getUserAccountsPage(
				null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(userAccount1, userAccount2),
				(List<UserAccount>)ascPage.getItems());

			Page<UserAccount> descPage =
				userAccountResource.getUserAccountsPage(
					null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(userAccount2, userAccount1),
				(List<UserAccount>)descPage.getItems());
		}
	}

	protected UserAccount testGetUserAccountsPage_addUserAccount(
			UserAccount userAccount)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetUserAccountsPage() throws Exception {
		GraphQLField graphQLField = new GraphQLField(
			"userAccounts",
			new HashMap<String, Object>() {
				{
					put("page", 1);
					put("pageSize", 10);
				}
			},
			new GraphQLField("items", getGraphQLFields()),
			new GraphQLField("page"), new GraphQLField("totalCount"));

		JSONObject userAccountsJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/userAccounts");

		long totalCount = userAccountsJSONObject.getLong("totalCount");

		UserAccount userAccount1 =
			testGraphQLGetUserAccountsPage_addUserAccount();
		UserAccount userAccount2 =
			testGraphQLGetUserAccountsPage_addUserAccount();

		userAccountsJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/userAccounts");

		Assert.assertEquals(
			totalCount + 2, userAccountsJSONObject.getLong("totalCount"));

		assertContains(
			userAccount1,
			Arrays.asList(
				UserAccountSerDes.toDTOs(
					userAccountsJSONObject.getString("items"))));
		assertContains(
			userAccount2,
			Arrays.asList(
				UserAccountSerDes.toDTOs(
					userAccountsJSONObject.getString("items"))));
	}

	protected UserAccount testGraphQLGetUserAccountsPage_addUserAccount()
		throws Exception {

		return testGraphQLUserAccount_addUserAccount();
	}

	@Test
	public void testPostUserAccount() throws Exception {
		UserAccount randomUserAccount = randomUserAccount();

		UserAccount postUserAccount = testPostUserAccount_addUserAccount(
			randomUserAccount);

		assertEquals(randomUserAccount, postUserAccount);
		assertValid(postUserAccount);
	}

	protected UserAccount testPostUserAccount_addUserAccount(
			UserAccount userAccount)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteUserAccountByExternalReferenceCode()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount =
			testDeleteUserAccountByExternalReferenceCode_addUserAccount();

		assertHttpResponseStatusCode(
			204,
			userAccountResource.
				deleteUserAccountByExternalReferenceCodeHttpResponse(
					userAccount.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			userAccountResource.
				getUserAccountByExternalReferenceCodeHttpResponse(
					userAccount.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			userAccountResource.
				getUserAccountByExternalReferenceCodeHttpResponse(
					userAccount.getExternalReferenceCode()));
	}

	protected UserAccount
			testDeleteUserAccountByExternalReferenceCode_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetUserAccountByExternalReferenceCode() throws Exception {
		UserAccount postUserAccount =
			testGetUserAccountByExternalReferenceCode_addUserAccount();

		UserAccount getUserAccount =
			userAccountResource.getUserAccountByExternalReferenceCode(
				postUserAccount.getExternalReferenceCode());

		assertEquals(postUserAccount, getUserAccount);
		assertValid(getUserAccount);
	}

	protected UserAccount
			testGetUserAccountByExternalReferenceCode_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetUserAccountByExternalReferenceCode()
		throws Exception {

		UserAccount userAccount =
			testGraphQLGetUserAccountByExternalReferenceCode_addUserAccount();

		Assert.assertTrue(
			equals(
				userAccount,
				UserAccountSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"userAccountByExternalReferenceCode",
								new HashMap<String, Object>() {
									{
										put(
											"externalReferenceCode",
											"\"" +
												userAccount.
													getExternalReferenceCode() +
														"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/userAccountByExternalReferenceCode"))));
	}

	@Test
	public void testGraphQLGetUserAccountByExternalReferenceCodeNotFound()
		throws Exception {

		String irrelevantExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"userAccountByExternalReferenceCode",
						new HashMap<String, Object>() {
							{
								put(
									"externalReferenceCode",
									irrelevantExternalReferenceCode);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected UserAccount
			testGraphQLGetUserAccountByExternalReferenceCode_addUserAccount()
		throws Exception {

		return testGraphQLUserAccount_addUserAccount();
	}

	@Test
	public void testPutUserAccountByExternalReferenceCode() throws Exception {
		UserAccount postUserAccount =
			testPutUserAccountByExternalReferenceCode_addUserAccount();

		UserAccount randomUserAccount = randomUserAccount();

		UserAccount putUserAccount =
			userAccountResource.putUserAccountByExternalReferenceCode(
				postUserAccount.getExternalReferenceCode(), randomUserAccount);

		assertEquals(randomUserAccount, putUserAccount);
		assertValid(putUserAccount);

		UserAccount getUserAccount =
			userAccountResource.getUserAccountByExternalReferenceCode(
				putUserAccount.getExternalReferenceCode());

		assertEquals(randomUserAccount, getUserAccount);
		assertValid(getUserAccount);

		UserAccount newUserAccount =
			testPutUserAccountByExternalReferenceCode_createUserAccount();

		putUserAccount =
			userAccountResource.putUserAccountByExternalReferenceCode(
				newUserAccount.getExternalReferenceCode(), newUserAccount);

		assertEquals(newUserAccount, putUserAccount);
		assertValid(putUserAccount);

		getUserAccount =
			userAccountResource.getUserAccountByExternalReferenceCode(
				putUserAccount.getExternalReferenceCode());

		assertEquals(newUserAccount, getUserAccount);

		Assert.assertEquals(
			newUserAccount.getExternalReferenceCode(),
			putUserAccount.getExternalReferenceCode());
	}

	protected UserAccount
			testPutUserAccountByExternalReferenceCode_createUserAccount()
		throws Exception {

		return randomUserAccount();
	}

	protected UserAccount
			testPutUserAccountByExternalReferenceCode_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteUserAccount() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount userAccount = testDeleteUserAccount_addUserAccount();

		assertHttpResponseStatusCode(
			204,
			userAccountResource.deleteUserAccountHttpResponse(
				userAccount.getId()));

		assertHttpResponseStatusCode(
			404,
			userAccountResource.getUserAccountHttpResponse(
				userAccount.getId()));

		assertHttpResponseStatusCode(
			404, userAccountResource.getUserAccountHttpResponse(0L));
	}

	protected UserAccount testDeleteUserAccount_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteUserAccount() throws Exception {
		UserAccount userAccount = testGraphQLDeleteUserAccount_addUserAccount();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteUserAccount",
						new HashMap<String, Object>() {
							{
								put("userAccountId", userAccount.getId());
							}
						})),
				"JSONObject/data", "Object/deleteUserAccount"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"userAccount",
					new HashMap<String, Object>() {
						{
							put("userAccountId", userAccount.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected UserAccount testGraphQLDeleteUserAccount_addUserAccount()
		throws Exception {

		return testGraphQLUserAccount_addUserAccount();
	}

	@Test
	public void testGetUserAccount() throws Exception {
		UserAccount postUserAccount = testGetUserAccount_addUserAccount();

		UserAccount getUserAccount = userAccountResource.getUserAccount(
			postUserAccount.getId());

		assertEquals(postUserAccount, getUserAccount);
		assertValid(getUserAccount);
	}

	protected UserAccount testGetUserAccount_addUserAccount() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetUserAccount() throws Exception {
		UserAccount userAccount = testGraphQLGetUserAccount_addUserAccount();

		Assert.assertTrue(
			equals(
				userAccount,
				UserAccountSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"userAccount",
								new HashMap<String, Object>() {
									{
										put(
											"userAccountId",
											userAccount.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/userAccount"))));
	}

	@Test
	public void testGraphQLGetUserAccountNotFound() throws Exception {
		Long irrelevantUserAccountId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"userAccount",
						new HashMap<String, Object>() {
							{
								put("userAccountId", irrelevantUserAccountId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected UserAccount testGraphQLGetUserAccount_addUserAccount()
		throws Exception {

		return testGraphQLUserAccount_addUserAccount();
	}

	@Test
	public void testPatchUserAccount() throws Exception {
		UserAccount postUserAccount = testPatchUserAccount_addUserAccount();

		UserAccount randomPatchUserAccount = randomPatchUserAccount();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		UserAccount patchUserAccount = userAccountResource.patchUserAccount(
			postUserAccount.getId(), randomPatchUserAccount);

		UserAccount expectedPatchUserAccount = postUserAccount.clone();

		BeanTestUtil.copyProperties(
			randomPatchUserAccount, expectedPatchUserAccount);

		UserAccount getUserAccount = userAccountResource.getUserAccount(
			patchUserAccount.getId());

		assertEquals(expectedPatchUserAccount, getUserAccount);
		assertValid(getUserAccount);
	}

	protected UserAccount testPatchUserAccount_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPutUserAccount() throws Exception {
		UserAccount postUserAccount = testPutUserAccount_addUserAccount();

		UserAccount randomUserAccount = randomUserAccount();

		UserAccount putUserAccount = userAccountResource.putUserAccount(
			postUserAccount.getId(), randomUserAccount);

		assertEquals(randomUserAccount, putUserAccount);
		assertValid(putUserAccount);

		UserAccount getUserAccount = userAccountResource.getUserAccount(
			putUserAccount.getId());

		assertEquals(randomUserAccount, getUserAccount);
		assertValid(getUserAccount);
	}

	protected UserAccount testPutUserAccount_addUserAccount() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected UserAccount testGraphQLUserAccount_addUserAccount()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		UserAccount userAccount, List<UserAccount> userAccounts) {

		boolean contains = false;

		for (UserAccount item : userAccounts) {
			if (equals(userAccount, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			userAccounts + " does not contain " + userAccount, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		UserAccount userAccount1, UserAccount userAccount2) {

		Assert.assertTrue(
			userAccount1 + " does not equal " + userAccount2,
			equals(userAccount1, userAccount2));
	}

	protected void assertEquals(
		List<UserAccount> userAccounts1, List<UserAccount> userAccounts2) {

		Assert.assertEquals(userAccounts1.size(), userAccounts2.size());

		for (int i = 0; i < userAccounts1.size(); i++) {
			UserAccount userAccount1 = userAccounts1.get(i);
			UserAccount userAccount2 = userAccounts2.get(i);

			assertEquals(userAccount1, userAccount2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<UserAccount> userAccounts1, List<UserAccount> userAccounts2) {

		Assert.assertEquals(userAccounts1.size(), userAccounts2.size());

		for (UserAccount userAccount1 : userAccounts1) {
			boolean contains = false;

			for (UserAccount userAccount2 : userAccounts2) {
				if (equals(userAccount1, userAccount2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				userAccounts2 + " does not contain " + userAccount1, contains);
		}
	}

	protected void assertValid(UserAccount userAccount) throws Exception {
		boolean valid = true;

		if (userAccount.getDateCreated() == null) {
			valid = false;
		}

		if (userAccount.getDateModified() == null) {
			valid = false;
		}

		if (userAccount.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("accountBriefs", additionalAssertFieldName)) {
				if (userAccount.getAccountBriefs() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (userAccount.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("additionalName", additionalAssertFieldName)) {
				if (userAccount.getAdditionalName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("alternateName", additionalAssertFieldName)) {
				if (userAccount.getAlternateName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("birthDate", additionalAssertFieldName)) {
				if (userAccount.getBirthDate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (userAccount.getCustomFields() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("dashboardURL", additionalAssertFieldName)) {
				if (userAccount.getDashboardURL() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("emailAddress", additionalAssertFieldName)) {
				if (userAccount.getEmailAddress() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (userAccount.getExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("familyName", additionalAssertFieldName)) {
				if (userAccount.getFamilyName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("givenName", additionalAssertFieldName)) {
				if (userAccount.getGivenName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("honorificPrefix", additionalAssertFieldName)) {
				if (userAccount.getHonorificPrefix() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("honorificSuffix", additionalAssertFieldName)) {
				if (userAccount.getHonorificSuffix() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("image", additionalAssertFieldName)) {
				if (userAccount.getImage() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("jobTitle", additionalAssertFieldName)) {
				if (userAccount.getJobTitle() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("keywords", additionalAssertFieldName)) {
				if (userAccount.getKeywords() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("lastLoginDate", additionalAssertFieldName)) {
				if (userAccount.getLastLoginDate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (userAccount.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"organizationBriefs", additionalAssertFieldName)) {

				if (userAccount.getOrganizationBriefs() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("password", additionalAssertFieldName)) {
				if (userAccount.getPassword() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("profileURL", additionalAssertFieldName)) {
				if (userAccount.getProfileURL() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("roleBriefs", additionalAssertFieldName)) {
				if (userAccount.getRoleBriefs() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("siteBriefs", additionalAssertFieldName)) {
				if (userAccount.getSiteBriefs() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"userAccountContactInformation",
					additionalAssertFieldName)) {

				if (userAccount.getUserAccountContactInformation() == null) {
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

	protected void assertValid(Page<UserAccount> page) {
		boolean valid = false;

		java.util.Collection<UserAccount> userAccounts = page.getItems();

		int size = userAccounts.size();

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
					com.liferay.headless.admin.user.dto.v1_0.UserAccount.
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
		UserAccount userAccount1, UserAccount userAccount2) {

		if (userAccount1 == userAccount2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("accountBriefs", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getAccountBriefs(),
						userAccount2.getAccountBriefs())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)userAccount1.getActions(),
						(Map)userAccount2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("additionalName", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getAdditionalName(),
						userAccount2.getAdditionalName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("alternateName", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getAlternateName(),
						userAccount2.getAlternateName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("birthDate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getBirthDate(),
						userAccount2.getBirthDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getCustomFields(),
						userAccount2.getCustomFields())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dashboardURL", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getDashboardURL(),
						userAccount2.getDashboardURL())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateCreated", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getDateCreated(),
						userAccount2.getDateCreated())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getDateModified(),
						userAccount2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("emailAddress", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getEmailAddress(),
						userAccount2.getEmailAddress())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						userAccount1.getExternalReferenceCode(),
						userAccount2.getExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("familyName", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getFamilyName(),
						userAccount2.getFamilyName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("givenName", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getGivenName(),
						userAccount2.getGivenName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("honorificPrefix", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getHonorificPrefix(),
						userAccount2.getHonorificPrefix())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("honorificSuffix", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getHonorificSuffix(),
						userAccount2.getHonorificSuffix())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getId(), userAccount2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("image", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getImage(), userAccount2.getImage())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("jobTitle", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getJobTitle(),
						userAccount2.getJobTitle())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("keywords", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getKeywords(),
						userAccount2.getKeywords())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("lastLoginDate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getLastLoginDate(),
						userAccount2.getLastLoginDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getName(), userAccount2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"organizationBriefs", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						userAccount1.getOrganizationBriefs(),
						userAccount2.getOrganizationBriefs())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("password", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getPassword(),
						userAccount2.getPassword())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("profileURL", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getProfileURL(),
						userAccount2.getProfileURL())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("roleBriefs", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getRoleBriefs(),
						userAccount2.getRoleBriefs())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("siteBriefs", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						userAccount1.getSiteBriefs(),
						userAccount2.getSiteBriefs())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"userAccountContactInformation",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						userAccount1.getUserAccountContactInformation(),
						userAccount2.getUserAccountContactInformation())) {

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

		if (!(_userAccountResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_userAccountResource;

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
		EntityField entityField, String operator, UserAccount userAccount) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("accountBriefs")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("actions")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("additionalName")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getAdditionalName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("alternateName")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getAlternateName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("birthDate")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(userAccount.getBirthDate(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(userAccount.getBirthDate(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(userAccount.getBirthDate()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("customFields")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("dashboardURL")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getDashboardURL()));
			sb.append("'");

			return sb.toString();
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
							userAccount.getDateCreated(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(userAccount.getDateCreated(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(userAccount.getDateCreated()));
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
							userAccount.getDateModified(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							userAccount.getDateModified(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(userAccount.getDateModified()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("emailAddress")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getEmailAddress()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("externalReferenceCode")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("familyName")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getFamilyName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("givenName")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getGivenName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("honorificPrefix")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getHonorificPrefix()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("honorificSuffix")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getHonorificSuffix()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("image")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getImage()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("jobTitle")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getJobTitle()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("keywords")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("lastLoginDate")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							userAccount.getLastLoginDate(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							userAccount.getLastLoginDate(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(userAccount.getLastLoginDate()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("organizationBriefs")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("password")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getPassword()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("profileURL")) {
			sb.append("'");
			sb.append(String.valueOf(userAccount.getProfileURL()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("roleBriefs")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("siteBriefs")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("userAccountContactInformation")) {
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

	protected UserAccount randomUserAccount() throws Exception {
		return new UserAccount() {
			{
				additionalName = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				alternateName = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				birthDate = RandomTestUtil.nextDate();
				dashboardURL = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				emailAddress =
					StringUtil.toLowerCase(RandomTestUtil.randomString()) +
						"@liferay.com";
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				familyName = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				givenName = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				honorificPrefix = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				honorificSuffix = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				image = StringUtil.toLowerCase(RandomTestUtil.randomString());
				jobTitle = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				lastLoginDate = RandomTestUtil.nextDate();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				password = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				profileURL = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
			}
		};
	}

	protected UserAccount randomIrrelevantUserAccount() throws Exception {
		UserAccount randomIrrelevantUserAccount = randomUserAccount();

		return randomIrrelevantUserAccount;
	}

	protected UserAccount randomPatchUserAccount() throws Exception {
		return randomUserAccount();
	}

	protected UserAccountResource userAccountResource;
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
		LogFactoryUtil.getLog(BaseUserAccountResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.admin.user.resource.v1_0.UserAccountResource
		_userAccountResource;

}