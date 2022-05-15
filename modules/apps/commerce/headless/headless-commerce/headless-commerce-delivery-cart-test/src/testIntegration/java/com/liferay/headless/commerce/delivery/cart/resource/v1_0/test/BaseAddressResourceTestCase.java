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

package com.liferay.headless.commerce.delivery.cart.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.Address;
import com.liferay.headless.commerce.delivery.cart.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Page;
import com.liferay.headless.commerce.delivery.cart.client.resource.v1_0.AddressResource;
import com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0.AddressSerDes;
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
public abstract class BaseAddressResourceTestCase {

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

		_addressResource.setContextCompany(testCompany);

		AddressResource.Builder builder = AddressResource.builder();

		addressResource = builder.authentication(
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

		Address address1 = randomAddress();

		String json = objectMapper.writeValueAsString(address1);

		Address address2 = AddressSerDes.toDTO(json);

		Assert.assertTrue(equals(address1, address2));
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

		Address address = randomAddress();

		String json1 = objectMapper.writeValueAsString(address);
		String json2 = AddressSerDes.toJSON(address);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Address address = randomAddress();

		address.setCity(regex);
		address.setCountry(regex);
		address.setCountryISOCode(regex);
		address.setDescription(regex);
		address.setName(regex);
		address.setPhoneNumber(regex);
		address.setRegion(regex);
		address.setRegionISOCode(regex);
		address.setStreet1(regex);
		address.setStreet2(regex);
		address.setStreet3(regex);
		address.setType(regex);
		address.setVatNumber(regex);
		address.setZip(regex);

		String json = AddressSerDes.toJSON(address);

		Assert.assertFalse(json.contains(regex));

		address = AddressSerDes.toDTO(json);

		Assert.assertEquals(regex, address.getCity());
		Assert.assertEquals(regex, address.getCountry());
		Assert.assertEquals(regex, address.getCountryISOCode());
		Assert.assertEquals(regex, address.getDescription());
		Assert.assertEquals(regex, address.getName());
		Assert.assertEquals(regex, address.getPhoneNumber());
		Assert.assertEquals(regex, address.getRegion());
		Assert.assertEquals(regex, address.getRegionISOCode());
		Assert.assertEquals(regex, address.getStreet1());
		Assert.assertEquals(regex, address.getStreet2());
		Assert.assertEquals(regex, address.getStreet3());
		Assert.assertEquals(regex, address.getType());
		Assert.assertEquals(regex, address.getVatNumber());
		Assert.assertEquals(regex, address.getZip());
	}

	@Test
	public void testGetCartBillingAddres() throws Exception {
		Address postAddress = testGetCartBillingAddres_addAddress();

		Address getAddress = addressResource.getCartBillingAddres(
			testGetCartBillingAddres_getCartId());

		assertEquals(postAddress, getAddress);
		assertValid(getAddress);
	}

	protected Long testGetCartBillingAddres_getCartId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Address testGetCartBillingAddres_addAddress() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetCartBillingAddres() throws Exception {
		Address address = testGraphQLGetCartBillingAddres_addAddress();

		Assert.assertTrue(
			equals(
				address,
				AddressSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"cartBillingAddres",
								new HashMap<String, Object>() {
									{
										put(
											"cartId",
											testGraphQLGetCartBillingAddres_getCartId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/cartBillingAddres"))));
	}

	protected Long testGraphQLGetCartBillingAddres_getCartId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetCartBillingAddresNotFound() throws Exception {
		Long irrelevantCartId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"cartBillingAddres",
						new HashMap<String, Object>() {
							{
								put("cartId", irrelevantCartId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Address testGraphQLGetCartBillingAddres_addAddress()
		throws Exception {

		return testGraphQLAddress_addAddress();
	}

	@Test
	public void testGetCartShippingAddres() throws Exception {
		Address postAddress = testGetCartShippingAddres_addAddress();

		Address getAddress = addressResource.getCartShippingAddres(
			testGetCartShippingAddres_getCartId());

		assertEquals(postAddress, getAddress);
		assertValid(getAddress);
	}

	protected Long testGetCartShippingAddres_getCartId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Address testGetCartShippingAddres_addAddress() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetCartShippingAddres() throws Exception {
		Address address = testGraphQLGetCartShippingAddres_addAddress();

		Assert.assertTrue(
			equals(
				address,
				AddressSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"cartShippingAddres",
								new HashMap<String, Object>() {
									{
										put(
											"cartId",
											testGraphQLGetCartShippingAddres_getCartId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/cartShippingAddres"))));
	}

	protected Long testGraphQLGetCartShippingAddres_getCartId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetCartShippingAddresNotFound() throws Exception {
		Long irrelevantCartId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"cartShippingAddres",
						new HashMap<String, Object>() {
							{
								put("cartId", irrelevantCartId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Address testGraphQLGetCartShippingAddres_addAddress()
		throws Exception {

		return testGraphQLAddress_addAddress();
	}

	protected Address testGraphQLAddress_addAddress() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(Address address, List<Address> addresses) {
		boolean contains = false;

		for (Address item : addresses) {
			if (equals(address, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(addresses + " does not contain " + address, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Address address1, Address address2) {
		Assert.assertTrue(
			address1 + " does not equal " + address2,
			equals(address1, address2));
	}

	protected void assertEquals(
		List<Address> addresses1, List<Address> addresses2) {

		Assert.assertEquals(addresses1.size(), addresses2.size());

		for (int i = 0; i < addresses1.size(); i++) {
			Address address1 = addresses1.get(i);
			Address address2 = addresses2.get(i);

			assertEquals(address1, address2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<Address> addresses1, List<Address> addresses2) {

		Assert.assertEquals(addresses1.size(), addresses2.size());

		for (Address address1 : addresses1) {
			boolean contains = false;

			for (Address address2 : addresses2) {
				if (equals(address1, address2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				addresses2 + " does not contain " + address1, contains);
		}
	}

	protected void assertValid(Address address) throws Exception {
		boolean valid = true;

		if (address.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("city", additionalAssertFieldName)) {
				if (address.getCity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("country", additionalAssertFieldName)) {
				if (address.getCountry() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("countryISOCode", additionalAssertFieldName)) {
				if (address.getCountryISOCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (address.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("latitude", additionalAssertFieldName)) {
				if (address.getLatitude() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("longitude", additionalAssertFieldName)) {
				if (address.getLongitude() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (address.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("phoneNumber", additionalAssertFieldName)) {
				if (address.getPhoneNumber() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("region", additionalAssertFieldName)) {
				if (address.getRegion() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("regionISOCode", additionalAssertFieldName)) {
				if (address.getRegionISOCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("street1", additionalAssertFieldName)) {
				if (address.getStreet1() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("street2", additionalAssertFieldName)) {
				if (address.getStreet2() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("street3", additionalAssertFieldName)) {
				if (address.getStreet3() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (address.getType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("typeId", additionalAssertFieldName)) {
				if (address.getTypeId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("vatNumber", additionalAssertFieldName)) {
				if (address.getVatNumber() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("zip", additionalAssertFieldName)) {
				if (address.getZip() == null) {
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

	protected void assertValid(Page<Address> page) {
		boolean valid = false;

		java.util.Collection<Address> addresses = page.getItems();

		int size = addresses.size();

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
					com.liferay.headless.commerce.delivery.cart.dto.v1_0.
						Address.class)) {

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

	protected boolean equals(Address address1, Address address2) {
		if (address1 == address2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("city", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getCity(), address2.getCity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("country", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getCountry(), address2.getCountry())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("countryISOCode", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getCountryISOCode(),
						address2.getCountryISOCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getDescription(), address2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(address1.getId(), address2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("latitude", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getLatitude(), address2.getLatitude())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("longitude", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getLongitude(), address2.getLongitude())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getName(), address2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("phoneNumber", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getPhoneNumber(), address2.getPhoneNumber())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("region", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getRegion(), address2.getRegion())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("regionISOCode", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getRegionISOCode(),
						address2.getRegionISOCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street1", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getStreet1(), address2.getStreet1())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street2", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getStreet2(), address2.getStreet2())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street3", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getStreet3(), address2.getStreet3())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getType(), address2.getType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("typeId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getTypeId(), address2.getTypeId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("vatNumber", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						address1.getVatNumber(), address2.getVatNumber())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("zip", additionalAssertFieldName)) {
				if (!Objects.deepEquals(address1.getZip(), address2.getZip())) {
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

		if (!(_addressResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_addressResource;

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
		EntityField entityField, String operator, Address address) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("city")) {
			sb.append("'");
			sb.append(String.valueOf(address.getCity()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("country")) {
			sb.append("'");
			sb.append(String.valueOf(address.getCountry()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("countryISOCode")) {
			sb.append("'");
			sb.append(String.valueOf(address.getCountryISOCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(address.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("latitude")) {
			sb.append(String.valueOf(address.getLatitude()));

			return sb.toString();
		}

		if (entityFieldName.equals("longitude")) {
			sb.append(String.valueOf(address.getLongitude()));

			return sb.toString();
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(address.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("phoneNumber")) {
			sb.append("'");
			sb.append(String.valueOf(address.getPhoneNumber()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("region")) {
			sb.append("'");
			sb.append(String.valueOf(address.getRegion()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("regionISOCode")) {
			sb.append("'");
			sb.append(String.valueOf(address.getRegionISOCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("street1")) {
			sb.append("'");
			sb.append(String.valueOf(address.getStreet1()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("street2")) {
			sb.append("'");
			sb.append(String.valueOf(address.getStreet2()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("street3")) {
			sb.append("'");
			sb.append(String.valueOf(address.getStreet3()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("type")) {
			sb.append("'");
			sb.append(String.valueOf(address.getType()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("typeId")) {
			sb.append(String.valueOf(address.getTypeId()));

			return sb.toString();
		}

		if (entityFieldName.equals("vatNumber")) {
			sb.append("'");
			sb.append(String.valueOf(address.getVatNumber()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("zip")) {
			sb.append("'");
			sb.append(String.valueOf(address.getZip()));
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

	protected Address randomAddress() throws Exception {
		return new Address() {
			{
				city = StringUtil.toLowerCase(RandomTestUtil.randomString());
				country = StringUtil.toLowerCase(RandomTestUtil.randomString());
				countryISOCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				description = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				latitude = RandomTestUtil.randomDouble();
				longitude = RandomTestUtil.randomDouble();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				phoneNumber = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				region = StringUtil.toLowerCase(RandomTestUtil.randomString());
				regionISOCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				street1 = StringUtil.toLowerCase(RandomTestUtil.randomString());
				street2 = StringUtil.toLowerCase(RandomTestUtil.randomString());
				street3 = StringUtil.toLowerCase(RandomTestUtil.randomString());
				type = StringUtil.toLowerCase(RandomTestUtil.randomString());
				typeId = RandomTestUtil.randomInt();
				vatNumber = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				zip = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected Address randomIrrelevantAddress() throws Exception {
		Address randomIrrelevantAddress = randomAddress();

		return randomIrrelevantAddress;
	}

	protected Address randomPatchAddress() throws Exception {
		return randomAddress();
	}

	protected AddressResource addressResource;
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
		LogFactoryUtil.getLog(BaseAddressResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private
		com.liferay.headless.commerce.delivery.cart.resource.v1_0.
			AddressResource _addressResource;

}