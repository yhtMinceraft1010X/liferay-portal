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

package com.liferay.headless.commerce.admin.pricing.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.pricing.client.dto.v1_0.TierPrice;
import com.liferay.headless.commerce.admin.pricing.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.pricing.client.pagination.Page;
import com.liferay.headless.commerce.admin.pricing.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.pricing.client.resource.v1_0.TierPriceResource;
import com.liferay.headless.commerce.admin.pricing.client.serdes.v1_0.TierPriceSerDes;
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
public abstract class BaseTierPriceResourceTestCase {

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

		_tierPriceResource.setContextCompany(testCompany);

		TierPriceResource.Builder builder = TierPriceResource.builder();

		tierPriceResource = builder.authentication(
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

		TierPrice tierPrice1 = randomTierPrice();

		String json = objectMapper.writeValueAsString(tierPrice1);

		TierPrice tierPrice2 = TierPriceSerDes.toDTO(json);

		Assert.assertTrue(equals(tierPrice1, tierPrice2));
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

		TierPrice tierPrice = randomTierPrice();

		String json1 = objectMapper.writeValueAsString(tierPrice);
		String json2 = TierPriceSerDes.toJSON(tierPrice);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		TierPrice tierPrice = randomTierPrice();

		tierPrice.setExternalReferenceCode(regex);
		tierPrice.setPriceEntryExternalReferenceCode(regex);

		String json = TierPriceSerDes.toJSON(tierPrice);

		Assert.assertFalse(json.contains(regex));

		tierPrice = TierPriceSerDes.toDTO(json);

		Assert.assertEquals(regex, tierPrice.getExternalReferenceCode());
		Assert.assertEquals(
			regex, tierPrice.getPriceEntryExternalReferenceCode());
	}

	@Test
	public void testGetPriceEntryByExternalReferenceCodeTierPricesPage()
		throws Exception {

		String externalReferenceCode =
			testGetPriceEntryByExternalReferenceCodeTierPricesPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetPriceEntryByExternalReferenceCodeTierPricesPage_getIrrelevantExternalReferenceCode();

		Page<TierPrice> page =
			tierPriceResource.
				getPriceEntryByExternalReferenceCodeTierPricesPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			TierPrice irrelevantTierPrice =
				testGetPriceEntryByExternalReferenceCodeTierPricesPage_addTierPrice(
					irrelevantExternalReferenceCode,
					randomIrrelevantTierPrice());

			page =
				tierPriceResource.
					getPriceEntryByExternalReferenceCodeTierPricesPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantTierPrice),
				(List<TierPrice>)page.getItems());
			assertValid(page);
		}

		TierPrice tierPrice1 =
			testGetPriceEntryByExternalReferenceCodeTierPricesPage_addTierPrice(
				externalReferenceCode, randomTierPrice());

		TierPrice tierPrice2 =
			testGetPriceEntryByExternalReferenceCodeTierPricesPage_addTierPrice(
				externalReferenceCode, randomTierPrice());

		page =
			tierPriceResource.
				getPriceEntryByExternalReferenceCodeTierPricesPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(tierPrice1, tierPrice2),
			(List<TierPrice>)page.getItems());
		assertValid(page);

		tierPriceResource.deleteTierPrice(tierPrice1.getId());

		tierPriceResource.deleteTierPrice(tierPrice2.getId());
	}

	@Test
	public void testGetPriceEntryByExternalReferenceCodeTierPricesPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetPriceEntryByExternalReferenceCodeTierPricesPage_getExternalReferenceCode();

		TierPrice tierPrice1 =
			testGetPriceEntryByExternalReferenceCodeTierPricesPage_addTierPrice(
				externalReferenceCode, randomTierPrice());

		TierPrice tierPrice2 =
			testGetPriceEntryByExternalReferenceCodeTierPricesPage_addTierPrice(
				externalReferenceCode, randomTierPrice());

		TierPrice tierPrice3 =
			testGetPriceEntryByExternalReferenceCodeTierPricesPage_addTierPrice(
				externalReferenceCode, randomTierPrice());

		Page<TierPrice> page1 =
			tierPriceResource.
				getPriceEntryByExternalReferenceCodeTierPricesPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<TierPrice> tierPrices1 = (List<TierPrice>)page1.getItems();

		Assert.assertEquals(tierPrices1.toString(), 2, tierPrices1.size());

		Page<TierPrice> page2 =
			tierPriceResource.
				getPriceEntryByExternalReferenceCodeTierPricesPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<TierPrice> tierPrices2 = (List<TierPrice>)page2.getItems();

		Assert.assertEquals(tierPrices2.toString(), 1, tierPrices2.size());

		Page<TierPrice> page3 =
			tierPriceResource.
				getPriceEntryByExternalReferenceCodeTierPricesPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(tierPrice1, tierPrice2, tierPrice3),
			(List<TierPrice>)page3.getItems());
	}

	protected TierPrice
			testGetPriceEntryByExternalReferenceCodeTierPricesPage_addTierPrice(
				String externalReferenceCode, TierPrice tierPrice)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetPriceEntryByExternalReferenceCodeTierPricesPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetPriceEntryByExternalReferenceCodeTierPricesPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostPriceEntryByExternalReferenceCodeTierPrice()
		throws Exception {

		TierPrice randomTierPrice = randomTierPrice();

		TierPrice postTierPrice =
			testPostPriceEntryByExternalReferenceCodeTierPrice_addTierPrice(
				randomTierPrice);

		assertEquals(randomTierPrice, postTierPrice);
		assertValid(postTierPrice);
	}

	protected TierPrice
			testPostPriceEntryByExternalReferenceCodeTierPrice_addTierPrice(
				TierPrice tierPrice)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetPriceEntryIdTierPricesPage() throws Exception {
		Long id = testGetPriceEntryIdTierPricesPage_getId();
		Long irrelevantId = testGetPriceEntryIdTierPricesPage_getIrrelevantId();

		Page<TierPrice> page = tierPriceResource.getPriceEntryIdTierPricesPage(
			id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			TierPrice irrelevantTierPrice =
				testGetPriceEntryIdTierPricesPage_addTierPrice(
					irrelevantId, randomIrrelevantTierPrice());

			page = tierPriceResource.getPriceEntryIdTierPricesPage(
				irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantTierPrice),
				(List<TierPrice>)page.getItems());
			assertValid(page);
		}

		TierPrice tierPrice1 = testGetPriceEntryIdTierPricesPage_addTierPrice(
			id, randomTierPrice());

		TierPrice tierPrice2 = testGetPriceEntryIdTierPricesPage_addTierPrice(
			id, randomTierPrice());

		page = tierPriceResource.getPriceEntryIdTierPricesPage(
			id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(tierPrice1, tierPrice2),
			(List<TierPrice>)page.getItems());
		assertValid(page);

		tierPriceResource.deleteTierPrice(tierPrice1.getId());

		tierPriceResource.deleteTierPrice(tierPrice2.getId());
	}

	@Test
	public void testGetPriceEntryIdTierPricesPageWithPagination()
		throws Exception {

		Long id = testGetPriceEntryIdTierPricesPage_getId();

		TierPrice tierPrice1 = testGetPriceEntryIdTierPricesPage_addTierPrice(
			id, randomTierPrice());

		TierPrice tierPrice2 = testGetPriceEntryIdTierPricesPage_addTierPrice(
			id, randomTierPrice());

		TierPrice tierPrice3 = testGetPriceEntryIdTierPricesPage_addTierPrice(
			id, randomTierPrice());

		Page<TierPrice> page1 = tierPriceResource.getPriceEntryIdTierPricesPage(
			id, Pagination.of(1, 2));

		List<TierPrice> tierPrices1 = (List<TierPrice>)page1.getItems();

		Assert.assertEquals(tierPrices1.toString(), 2, tierPrices1.size());

		Page<TierPrice> page2 = tierPriceResource.getPriceEntryIdTierPricesPage(
			id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<TierPrice> tierPrices2 = (List<TierPrice>)page2.getItems();

		Assert.assertEquals(tierPrices2.toString(), 1, tierPrices2.size());

		Page<TierPrice> page3 = tierPriceResource.getPriceEntryIdTierPricesPage(
			id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(tierPrice1, tierPrice2, tierPrice3),
			(List<TierPrice>)page3.getItems());
	}

	protected TierPrice testGetPriceEntryIdTierPricesPage_addTierPrice(
			Long id, TierPrice tierPrice)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetPriceEntryIdTierPricesPage_getId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetPriceEntryIdTierPricesPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostPriceEntryIdTierPrice() throws Exception {
		TierPrice randomTierPrice = randomTierPrice();

		TierPrice postTierPrice = testPostPriceEntryIdTierPrice_addTierPrice(
			randomTierPrice);

		assertEquals(randomTierPrice, postTierPrice);
		assertValid(postTierPrice);
	}

	protected TierPrice testPostPriceEntryIdTierPrice_addTierPrice(
			TierPrice tierPrice)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteTierPriceByExternalReferenceCode() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		TierPrice tierPrice =
			testDeleteTierPriceByExternalReferenceCode_addTierPrice();

		assertHttpResponseStatusCode(
			204,
			tierPriceResource.
				deleteTierPriceByExternalReferenceCodeHttpResponse(
					tierPrice.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			tierPriceResource.getTierPriceByExternalReferenceCodeHttpResponse(
				tierPrice.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			tierPriceResource.getTierPriceByExternalReferenceCodeHttpResponse(
				tierPrice.getExternalReferenceCode()));
	}

	protected TierPrice
			testDeleteTierPriceByExternalReferenceCode_addTierPrice()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetTierPriceByExternalReferenceCode() throws Exception {
		TierPrice postTierPrice =
			testGetTierPriceByExternalReferenceCode_addTierPrice();

		TierPrice getTierPrice =
			tierPriceResource.getTierPriceByExternalReferenceCode(
				postTierPrice.getExternalReferenceCode());

		assertEquals(postTierPrice, getTierPrice);
		assertValid(getTierPrice);
	}

	protected TierPrice testGetTierPriceByExternalReferenceCode_addTierPrice()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetTierPriceByExternalReferenceCode()
		throws Exception {

		TierPrice tierPrice =
			testGraphQLGetTierPriceByExternalReferenceCode_addTierPrice();

		Assert.assertTrue(
			equals(
				tierPrice,
				TierPriceSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"tierPriceByExternalReferenceCode",
								new HashMap<String, Object>() {
									{
										put(
											"externalReferenceCode",
											"\"" +
												tierPrice.
													getExternalReferenceCode() +
														"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/tierPriceByExternalReferenceCode"))));
	}

	@Test
	public void testGraphQLGetTierPriceByExternalReferenceCodeNotFound()
		throws Exception {

		String irrelevantExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"tierPriceByExternalReferenceCode",
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

	protected TierPrice
			testGraphQLGetTierPriceByExternalReferenceCode_addTierPrice()
		throws Exception {

		return testGraphQLTierPrice_addTierPrice();
	}

	@Test
	public void testPatchTierPriceByExternalReferenceCode() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testDeleteTierPrice() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		TierPrice tierPrice = testDeleteTierPrice_addTierPrice();

		assertHttpResponseStatusCode(
			204,
			tierPriceResource.deleteTierPriceHttpResponse(tierPrice.getId()));

		assertHttpResponseStatusCode(
			404, tierPriceResource.getTierPriceHttpResponse(tierPrice.getId()));

		assertHttpResponseStatusCode(
			404, tierPriceResource.getTierPriceHttpResponse(tierPrice.getId()));
	}

	protected TierPrice testDeleteTierPrice_addTierPrice() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteTierPrice() throws Exception {
		TierPrice tierPrice = testGraphQLDeleteTierPrice_addTierPrice();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteTierPrice",
						new HashMap<String, Object>() {
							{
								put("id", tierPrice.getId());
							}
						})),
				"JSONObject/data", "Object/deleteTierPrice"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"tierPrice",
					new HashMap<String, Object>() {
						{
							put("id", tierPrice.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected TierPrice testGraphQLDeleteTierPrice_addTierPrice()
		throws Exception {

		return testGraphQLTierPrice_addTierPrice();
	}

	@Test
	public void testGetTierPrice() throws Exception {
		TierPrice postTierPrice = testGetTierPrice_addTierPrice();

		TierPrice getTierPrice = tierPriceResource.getTierPrice(
			postTierPrice.getId());

		assertEquals(postTierPrice, getTierPrice);
		assertValid(getTierPrice);
	}

	protected TierPrice testGetTierPrice_addTierPrice() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetTierPrice() throws Exception {
		TierPrice tierPrice = testGraphQLGetTierPrice_addTierPrice();

		Assert.assertTrue(
			equals(
				tierPrice,
				TierPriceSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"tierPrice",
								new HashMap<String, Object>() {
									{
										put("id", tierPrice.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/tierPrice"))));
	}

	@Test
	public void testGraphQLGetTierPriceNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"tierPrice",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected TierPrice testGraphQLGetTierPrice_addTierPrice()
		throws Exception {

		return testGraphQLTierPrice_addTierPrice();
	}

	@Test
	public void testPatchTierPrice() throws Exception {
		Assert.assertTrue(false);
	}

	protected TierPrice testGraphQLTierPrice_addTierPrice() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		TierPrice tierPrice, List<TierPrice> tierPrices) {

		boolean contains = false;

		for (TierPrice item : tierPrices) {
			if (equals(tierPrice, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			tierPrices + " does not contain " + tierPrice, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(TierPrice tierPrice1, TierPrice tierPrice2) {
		Assert.assertTrue(
			tierPrice1 + " does not equal " + tierPrice2,
			equals(tierPrice1, tierPrice2));
	}

	protected void assertEquals(
		List<TierPrice> tierPrices1, List<TierPrice> tierPrices2) {

		Assert.assertEquals(tierPrices1.size(), tierPrices2.size());

		for (int i = 0; i < tierPrices1.size(); i++) {
			TierPrice tierPrice1 = tierPrices1.get(i);
			TierPrice tierPrice2 = tierPrices2.get(i);

			assertEquals(tierPrice1, tierPrice2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<TierPrice> tierPrices1, List<TierPrice> tierPrices2) {

		Assert.assertEquals(tierPrices1.size(), tierPrices2.size());

		for (TierPrice tierPrice1 : tierPrices1) {
			boolean contains = false;

			for (TierPrice tierPrice2 : tierPrices2) {
				if (equals(tierPrice1, tierPrice2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				tierPrices2 + " does not contain " + tierPrice1, contains);
		}
	}

	protected void assertValid(TierPrice tierPrice) throws Exception {
		boolean valid = true;

		if (tierPrice.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (tierPrice.getCustomFields() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (tierPrice.getExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("minimumQuantity", additionalAssertFieldName)) {
				if (tierPrice.getMinimumQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("price", additionalAssertFieldName)) {
				if (tierPrice.getPrice() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"priceEntryExternalReferenceCode",
					additionalAssertFieldName)) {

				if (tierPrice.getPriceEntryExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("priceEntryId", additionalAssertFieldName)) {
				if (tierPrice.getPriceEntryId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("promoPrice", additionalAssertFieldName)) {
				if (tierPrice.getPromoPrice() == null) {
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

	protected void assertValid(Page<TierPrice> page) {
		boolean valid = false;

		java.util.Collection<TierPrice> tierPrices = page.getItems();

		int size = tierPrices.size();

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
					com.liferay.headless.commerce.admin.pricing.dto.v1_0.
						TierPrice.class)) {

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

	protected boolean equals(TierPrice tierPrice1, TierPrice tierPrice2) {
		if (tierPrice1 == tierPrice2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (!equals(
						(Map)tierPrice1.getCustomFields(),
						(Map)tierPrice2.getCustomFields())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						tierPrice1.getExternalReferenceCode(),
						tierPrice2.getExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						tierPrice1.getId(), tierPrice2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("minimumQuantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						tierPrice1.getMinimumQuantity(),
						tierPrice2.getMinimumQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("price", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						tierPrice1.getPrice(), tierPrice2.getPrice())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"priceEntryExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						tierPrice1.getPriceEntryExternalReferenceCode(),
						tierPrice2.getPriceEntryExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("priceEntryId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						tierPrice1.getPriceEntryId(),
						tierPrice2.getPriceEntryId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("promoPrice", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						tierPrice1.getPromoPrice(),
						tierPrice2.getPromoPrice())) {

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

		if (!(_tierPriceResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_tierPriceResource;

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
		EntityField entityField, String operator, TierPrice tierPrice) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("customFields")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("externalReferenceCode")) {
			sb.append("'");
			sb.append(String.valueOf(tierPrice.getExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("minimumQuantity")) {
			sb.append(String.valueOf(tierPrice.getMinimumQuantity()));

			return sb.toString();
		}

		if (entityFieldName.equals("price")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("priceEntryExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(tierPrice.getPriceEntryExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("priceEntryId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("promoPrice")) {
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

	protected TierPrice randomTierPrice() throws Exception {
		return new TierPrice() {
			{
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				minimumQuantity = RandomTestUtil.randomInt();
				priceEntryExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				priceEntryId = RandomTestUtil.randomLong();
			}
		};
	}

	protected TierPrice randomIrrelevantTierPrice() throws Exception {
		TierPrice randomIrrelevantTierPrice = randomTierPrice();

		return randomIrrelevantTierPrice;
	}

	protected TierPrice randomPatchTierPrice() throws Exception {
		return randomTierPrice();
	}

	protected TierPriceResource tierPriceResource;
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
		LogFactoryUtil.getLog(BaseTierPriceResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private
		com.liferay.headless.commerce.admin.pricing.resource.v1_0.
			TierPriceResource _tierPriceResource;

}