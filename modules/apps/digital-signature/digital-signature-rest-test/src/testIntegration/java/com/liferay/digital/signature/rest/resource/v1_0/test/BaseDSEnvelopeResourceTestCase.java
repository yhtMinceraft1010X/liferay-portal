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

package com.liferay.digital.signature.rest.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.digital.signature.rest.client.dto.v1_0.DSEnvelope;
import com.liferay.digital.signature.rest.client.http.HttpInvoker;
import com.liferay.digital.signature.rest.client.pagination.Page;
import com.liferay.digital.signature.rest.client.pagination.Pagination;
import com.liferay.digital.signature.rest.client.resource.v1_0.DSEnvelopeResource;
import com.liferay.digital.signature.rest.client.serdes.v1_0.DSEnvelopeSerDes;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONDeserializer;
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

import org.apache.commons.lang.time.DateUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author José Abelenda
 * @generated
 */
@Generated("")
public abstract class BaseDSEnvelopeResourceTestCase {

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

		_dsEnvelopeResource.setContextCompany(testCompany);

		DSEnvelopeResource.Builder builder = DSEnvelopeResource.builder();

		dsEnvelopeResource = builder.authentication(
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

		DSEnvelope dsEnvelope1 = randomDSEnvelope();

		String json = objectMapper.writeValueAsString(dsEnvelope1);

		DSEnvelope dsEnvelope2 = DSEnvelopeSerDes.toDTO(json);

		Assert.assertTrue(equals(dsEnvelope1, dsEnvelope2));
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

		DSEnvelope dsEnvelope = randomDSEnvelope();

		String json1 = objectMapper.writeValueAsString(dsEnvelope);
		String json2 = DSEnvelopeSerDes.toJSON(dsEnvelope);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		DSEnvelope dsEnvelope = randomDSEnvelope();

		dsEnvelope.setEmailBlurb(regex);
		dsEnvelope.setEmailSubject(regex);
		dsEnvelope.setId(regex);
		dsEnvelope.setName(regex);
		dsEnvelope.setSenderEmailAddress(regex);
		dsEnvelope.setStatus(regex);

		String json = DSEnvelopeSerDes.toJSON(dsEnvelope);

		Assert.assertFalse(json.contains(regex));

		dsEnvelope = DSEnvelopeSerDes.toDTO(json);

		Assert.assertEquals(regex, dsEnvelope.getEmailBlurb());
		Assert.assertEquals(regex, dsEnvelope.getEmailSubject());
		Assert.assertEquals(regex, dsEnvelope.getId());
		Assert.assertEquals(regex, dsEnvelope.getName());
		Assert.assertEquals(regex, dsEnvelope.getSenderEmailAddress());
		Assert.assertEquals(regex, dsEnvelope.getStatus());
	}

	@Test
	public void testGetSiteDSEnvelopesPage() throws Exception {
		Long siteId = testGetSiteDSEnvelopesPage_getSiteId();
		Long irrelevantSiteId =
			testGetSiteDSEnvelopesPage_getIrrelevantSiteId();

		Page<DSEnvelope> page = dsEnvelopeResource.getSiteDSEnvelopesPage(
			siteId, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantSiteId != null) {
			DSEnvelope irrelevantDSEnvelope =
				testGetSiteDSEnvelopesPage_addDSEnvelope(
					irrelevantSiteId, randomIrrelevantDSEnvelope());

			page = dsEnvelopeResource.getSiteDSEnvelopesPage(
				irrelevantSiteId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantDSEnvelope),
				(List<DSEnvelope>)page.getItems());
			assertValid(page);
		}

		DSEnvelope dsEnvelope1 = testGetSiteDSEnvelopesPage_addDSEnvelope(
			siteId, randomDSEnvelope());

		DSEnvelope dsEnvelope2 = testGetSiteDSEnvelopesPage_addDSEnvelope(
			siteId, randomDSEnvelope());

		page = dsEnvelopeResource.getSiteDSEnvelopesPage(
			siteId, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(dsEnvelope1, dsEnvelope2),
			(List<DSEnvelope>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetSiteDSEnvelopesPageWithPagination() throws Exception {
		Long siteId = testGetSiteDSEnvelopesPage_getSiteId();

		DSEnvelope dsEnvelope1 = testGetSiteDSEnvelopesPage_addDSEnvelope(
			siteId, randomDSEnvelope());

		DSEnvelope dsEnvelope2 = testGetSiteDSEnvelopesPage_addDSEnvelope(
			siteId, randomDSEnvelope());

		DSEnvelope dsEnvelope3 = testGetSiteDSEnvelopesPage_addDSEnvelope(
			siteId, randomDSEnvelope());

		Page<DSEnvelope> page1 = dsEnvelopeResource.getSiteDSEnvelopesPage(
			siteId, Pagination.of(1, 2));

		List<DSEnvelope> dsEnvelopes1 = (List<DSEnvelope>)page1.getItems();

		Assert.assertEquals(dsEnvelopes1.toString(), 2, dsEnvelopes1.size());

		Page<DSEnvelope> page2 = dsEnvelopeResource.getSiteDSEnvelopesPage(
			siteId, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<DSEnvelope> dsEnvelopes2 = (List<DSEnvelope>)page2.getItems();

		Assert.assertEquals(dsEnvelopes2.toString(), 1, dsEnvelopes2.size());

		Page<DSEnvelope> page3 = dsEnvelopeResource.getSiteDSEnvelopesPage(
			siteId, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(dsEnvelope1, dsEnvelope2, dsEnvelope3),
			(List<DSEnvelope>)page3.getItems());
	}

	protected DSEnvelope testGetSiteDSEnvelopesPage_addDSEnvelope(
			Long siteId, DSEnvelope dsEnvelope)
		throws Exception {

		return dsEnvelopeResource.postSiteDSEnvelope(siteId, dsEnvelope);
	}

	protected Long testGetSiteDSEnvelopesPage_getSiteId() throws Exception {
		return testGroup.getGroupId();
	}

	protected Long testGetSiteDSEnvelopesPage_getIrrelevantSiteId()
		throws Exception {

		return irrelevantGroup.getGroupId();
	}

	@Test
	public void testPostSiteDSEnvelope() throws Exception {
		DSEnvelope randomDSEnvelope = randomDSEnvelope();

		DSEnvelope postDSEnvelope = testPostSiteDSEnvelope_addDSEnvelope(
			randomDSEnvelope);

		assertEquals(randomDSEnvelope, postDSEnvelope);
		assertValid(postDSEnvelope);
	}

	protected DSEnvelope testPostSiteDSEnvelope_addDSEnvelope(
			DSEnvelope dsEnvelope)
		throws Exception {

		return dsEnvelopeResource.postSiteDSEnvelope(
			testGetSiteDSEnvelopesPage_getSiteId(), dsEnvelope);
	}

	@Test
	public void testGraphQLPostSiteDSEnvelope() throws Exception {
		DSEnvelope randomDSEnvelope = randomDSEnvelope();

		DSEnvelope dsEnvelope = testGraphQLDSEnvelope_addDSEnvelope(
			randomDSEnvelope);

		Assert.assertTrue(equals(randomDSEnvelope, dsEnvelope));
	}

	@Test
	public void testGetSiteDSEnvelope() throws Exception {
		DSEnvelope postDSEnvelope = testGetSiteDSEnvelope_addDSEnvelope();

		DSEnvelope getDSEnvelope = dsEnvelopeResource.getSiteDSEnvelope(
			postDSEnvelope.getSiteId(), postDSEnvelope.getId());

		assertEquals(postDSEnvelope, getDSEnvelope);
		assertValid(getDSEnvelope);
	}

	protected DSEnvelope testGetSiteDSEnvelope_addDSEnvelope()
		throws Exception {

		return dsEnvelopeResource.postSiteDSEnvelope(
			testGroup.getGroupId(), randomDSEnvelope());
	}

	@Test
	public void testGraphQLGetSiteDSEnvelope() throws Exception {
		DSEnvelope dsEnvelope = testGraphQLGetSiteDSEnvelope_addDSEnvelope();

		Assert.assertTrue(
			equals(
				dsEnvelope,
				DSEnvelopeSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"dSEnvelope",
								new HashMap<String, Object>() {
									{
										put(
											"siteKey",
											"\"" + dsEnvelope.getSiteId() +
												"\"");
										put(
											"dsEnvelopeId",
											"\"" + dsEnvelope.getId() + "\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/dSEnvelope"))));
	}

	@Test
	public void testGraphQLGetSiteDSEnvelopeNotFound() throws Exception {
		String irrelevantDsEnvelopeId =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"dSEnvelope",
						new HashMap<String, Object>() {
							{
								put(
									"siteKey",
									"\"" + irrelevantGroup.getGroupId() + "\"");
								put("dsEnvelopeId", irrelevantDsEnvelopeId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected DSEnvelope testGraphQLGetSiteDSEnvelope_addDSEnvelope()
		throws Exception {

		return testGraphQLDSEnvelope_addDSEnvelope();
	}

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

	protected DSEnvelope testGraphQLDSEnvelope_addDSEnvelope()
		throws Exception {

		return testGraphQLDSEnvelope_addDSEnvelope(randomDSEnvelope());
	}

	protected DSEnvelope testGraphQLDSEnvelope_addDSEnvelope(
			DSEnvelope dsEnvelope)
		throws Exception {

		JSONDeserializer<DSEnvelope> jsonDeserializer =
			JSONFactoryUtil.createJSONDeserializer();

		StringBuilder sb = new StringBuilder("{");

		for (java.lang.reflect.Field field :
				getDeclaredFields(DSEnvelope.class)) {

			if (!ArrayUtil.contains(
					getAdditionalAssertFieldNames(), field.getName())) {

				continue;
			}

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append(field.getName());
			sb.append(": ");

			appendGraphQLFieldValue(sb, field.get(dsEnvelope));
		}

		sb.append("}");

		List<GraphQLField> graphQLFields = getGraphQLFields();

		graphQLFields.add(new GraphQLField("id"));

		return jsonDeserializer.deserialize(
			JSONUtil.getValueAsString(
				invokeGraphQLMutation(
					new GraphQLField(
						"createSiteDSEnvelope",
						new HashMap<String, Object>() {
							{
								put(
									"siteKey",
									"\"" + testGroup.getGroupId() + "\"");
								put("dsEnvelope", sb.toString());
							}
						},
						graphQLFields)),
				"JSONObject/data", "JSONObject/createSiteDSEnvelope"),
			DSEnvelope.class);
	}

	protected void assertContains(
		DSEnvelope dsEnvelope, List<DSEnvelope> dsEnvelopes) {

		boolean contains = false;

		for (DSEnvelope item : dsEnvelopes) {
			if (equals(dsEnvelope, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			dsEnvelopes + " does not contain " + dsEnvelope, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		DSEnvelope dsEnvelope1, DSEnvelope dsEnvelope2) {

		Assert.assertTrue(
			dsEnvelope1 + " does not equal " + dsEnvelope2,
			equals(dsEnvelope1, dsEnvelope2));
	}

	protected void assertEquals(
		List<DSEnvelope> dsEnvelopes1, List<DSEnvelope> dsEnvelopes2) {

		Assert.assertEquals(dsEnvelopes1.size(), dsEnvelopes2.size());

		for (int i = 0; i < dsEnvelopes1.size(); i++) {
			DSEnvelope dsEnvelope1 = dsEnvelopes1.get(i);
			DSEnvelope dsEnvelope2 = dsEnvelopes2.get(i);

			assertEquals(dsEnvelope1, dsEnvelope2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<DSEnvelope> dsEnvelopes1, List<DSEnvelope> dsEnvelopes2) {

		Assert.assertEquals(dsEnvelopes1.size(), dsEnvelopes2.size());

		for (DSEnvelope dsEnvelope1 : dsEnvelopes1) {
			boolean contains = false;

			for (DSEnvelope dsEnvelope2 : dsEnvelopes2) {
				if (equals(dsEnvelope1, dsEnvelope2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				dsEnvelopes2 + " does not contain " + dsEnvelope1, contains);
		}
	}

	protected void assertValid(DSEnvelope dsEnvelope) throws Exception {
		boolean valid = true;

		if (dsEnvelope.getDateCreated() == null) {
			valid = false;
		}

		if (dsEnvelope.getDateModified() == null) {
			valid = false;
		}

		if (dsEnvelope.getId() == null) {
			valid = false;
		}

		if (!Objects.equals(dsEnvelope.getSiteId(), testGroup.getGroupId())) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("dsDocument", additionalAssertFieldName)) {
				if (dsEnvelope.getDsDocument() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("dsRecipient", additionalAssertFieldName)) {
				if (dsEnvelope.getDsRecipient() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("emailBlurb", additionalAssertFieldName)) {
				if (dsEnvelope.getEmailBlurb() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("emailSubject", additionalAssertFieldName)) {
				if (dsEnvelope.getEmailSubject() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (dsEnvelope.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"senderEmailAddress", additionalAssertFieldName)) {

				if (dsEnvelope.getSenderEmailAddress() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("status", additionalAssertFieldName)) {
				if (dsEnvelope.getStatus() == null) {
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

	protected void assertValid(Page<DSEnvelope> page) {
		boolean valid = false;

		java.util.Collection<DSEnvelope> dsEnvelopes = page.getItems();

		int size = dsEnvelopes.size();

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
					com.liferay.digital.signature.rest.dto.v1_0.DSEnvelope.
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

	protected boolean equals(DSEnvelope dsEnvelope1, DSEnvelope dsEnvelope2) {
		if (dsEnvelope1 == dsEnvelope2) {
			return true;
		}

		if (!Objects.equals(dsEnvelope1.getSiteId(), dsEnvelope2.getSiteId())) {
			return false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("dateCreated", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsEnvelope1.getDateCreated(),
						dsEnvelope2.getDateCreated())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsEnvelope1.getDateModified(),
						dsEnvelope2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dsDocument", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsEnvelope1.getDsDocument(),
						dsEnvelope2.getDsDocument())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dsRecipient", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsEnvelope1.getDsRecipient(),
						dsEnvelope2.getDsRecipient())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("emailBlurb", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsEnvelope1.getEmailBlurb(),
						dsEnvelope2.getEmailBlurb())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("emailSubject", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsEnvelope1.getEmailSubject(),
						dsEnvelope2.getEmailSubject())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsEnvelope1.getId(), dsEnvelope2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsEnvelope1.getName(), dsEnvelope2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"senderEmailAddress", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						dsEnvelope1.getSenderEmailAddress(),
						dsEnvelope2.getSenderEmailAddress())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("status", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsEnvelope1.getStatus(), dsEnvelope2.getStatus())) {

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

		if (!(_dsEnvelopeResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_dsEnvelopeResource;

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
		EntityField entityField, String operator, DSEnvelope dsEnvelope) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("dateCreated")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(dsEnvelope.getDateCreated(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(dsEnvelope.getDateCreated(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(dsEnvelope.getDateCreated()));
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
							dsEnvelope.getDateModified(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(dsEnvelope.getDateModified(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(dsEnvelope.getDateModified()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("dsDocument")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("dsRecipient")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("emailBlurb")) {
			sb.append("'");
			sb.append(String.valueOf(dsEnvelope.getEmailBlurb()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("emailSubject")) {
			sb.append("'");
			sb.append(String.valueOf(dsEnvelope.getEmailSubject()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			sb.append("'");
			sb.append(String.valueOf(dsEnvelope.getId()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(dsEnvelope.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("senderEmailAddress")) {
			sb.append("'");
			sb.append(String.valueOf(dsEnvelope.getSenderEmailAddress()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("siteId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("status")) {
			sb.append("'");
			sb.append(String.valueOf(dsEnvelope.getStatus()));
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

	protected DSEnvelope randomDSEnvelope() throws Exception {
		return new DSEnvelope() {
			{
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				emailBlurb =
					StringUtil.toLowerCase(RandomTestUtil.randomString()) +
						"@liferay.com";
				emailSubject =
					StringUtil.toLowerCase(RandomTestUtil.randomString()) +
						"@liferay.com";
				id = StringUtil.toLowerCase(RandomTestUtil.randomString());
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				senderEmailAddress = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				siteId = testGroup.getGroupId();
				status = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected DSEnvelope randomIrrelevantDSEnvelope() throws Exception {
		DSEnvelope randomIrrelevantDSEnvelope = randomDSEnvelope();

		randomIrrelevantDSEnvelope.setSiteId(irrelevantGroup.getGroupId());

		return randomIrrelevantDSEnvelope;
	}

	protected DSEnvelope randomPatchDSEnvelope() throws Exception {
		return randomDSEnvelope();
	}

	protected DSEnvelopeResource dsEnvelopeResource;
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
		LogFactoryUtil.getLog(BaseDSEnvelopeResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.digital.signature.rest.resource.v1_0.DSEnvelopeResource
		_dsEnvelopeResource;

}