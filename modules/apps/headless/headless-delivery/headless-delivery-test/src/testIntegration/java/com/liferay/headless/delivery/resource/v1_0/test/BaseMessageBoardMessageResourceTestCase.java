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

import com.liferay.headless.delivery.client.dto.v1_0.Field;
import com.liferay.headless.delivery.client.dto.v1_0.MessageBoardMessage;
import com.liferay.headless.delivery.client.dto.v1_0.Rating;
import com.liferay.headless.delivery.client.http.HttpInvoker;
import com.liferay.headless.delivery.client.pagination.Page;
import com.liferay.headless.delivery.client.pagination.Pagination;
import com.liferay.headless.delivery.client.permission.Permission;
import com.liferay.headless.delivery.client.resource.v1_0.MessageBoardMessageResource;
import com.liferay.headless.delivery.client.serdes.v1_0.MessageBoardMessageSerDes;
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
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
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
public abstract class BaseMessageBoardMessageResourceTestCase {

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

		_messageBoardMessageResource.setContextCompany(testCompany);

		MessageBoardMessageResource.Builder builder =
			MessageBoardMessageResource.builder();

		messageBoardMessageResource = builder.authentication(
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

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();

		String json = objectMapper.writeValueAsString(messageBoardMessage1);

		MessageBoardMessage messageBoardMessage2 =
			MessageBoardMessageSerDes.toDTO(json);

		Assert.assertTrue(equals(messageBoardMessage1, messageBoardMessage2));
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

		MessageBoardMessage messageBoardMessage = randomMessageBoardMessage();

		String json1 = objectMapper.writeValueAsString(messageBoardMessage);
		String json2 = MessageBoardMessageSerDes.toJSON(messageBoardMessage);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		MessageBoardMessage messageBoardMessage = randomMessageBoardMessage();

		messageBoardMessage.setArticleBody(regex);
		messageBoardMessage.setEncodingFormat(regex);
		messageBoardMessage.setExternalReferenceCode(regex);
		messageBoardMessage.setFriendlyUrlPath(regex);
		messageBoardMessage.setHeadline(regex);
		messageBoardMessage.setStatus(regex);

		String json = MessageBoardMessageSerDes.toJSON(messageBoardMessage);

		Assert.assertFalse(json.contains(regex));

		messageBoardMessage = MessageBoardMessageSerDes.toDTO(json);

		Assert.assertEquals(regex, messageBoardMessage.getArticleBody());
		Assert.assertEquals(regex, messageBoardMessage.getEncodingFormat());
		Assert.assertEquals(
			regex, messageBoardMessage.getExternalReferenceCode());
		Assert.assertEquals(regex, messageBoardMessage.getFriendlyUrlPath());
		Assert.assertEquals(regex, messageBoardMessage.getHeadline());
		Assert.assertEquals(regex, messageBoardMessage.getStatus());
	}

	@Test
	public void testDeleteMessageBoardMessage() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage =
			testDeleteMessageBoardMessage_addMessageBoardMessage();

		assertHttpResponseStatusCode(
			204,
			messageBoardMessageResource.deleteMessageBoardMessageHttpResponse(
				messageBoardMessage.getId()));

		assertHttpResponseStatusCode(
			404,
			messageBoardMessageResource.getMessageBoardMessageHttpResponse(
				messageBoardMessage.getId()));

		assertHttpResponseStatusCode(
			404,
			messageBoardMessageResource.getMessageBoardMessageHttpResponse(0L));
	}

	protected MessageBoardMessage
			testDeleteMessageBoardMessage_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteMessageBoardMessage() throws Exception {
		MessageBoardMessage messageBoardMessage =
			testGraphQLDeleteMessageBoardMessage_addMessageBoardMessage();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteMessageBoardMessage",
						new HashMap<String, Object>() {
							{
								put(
									"messageBoardMessageId",
									messageBoardMessage.getId());
							}
						})),
				"JSONObject/data", "Object/deleteMessageBoardMessage"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"messageBoardMessage",
					new HashMap<String, Object>() {
						{
							put(
								"messageBoardMessageId",
								messageBoardMessage.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected MessageBoardMessage
			testGraphQLDeleteMessageBoardMessage_addMessageBoardMessage()
		throws Exception {

		return testGraphQLMessageBoardMessage_addMessageBoardMessage();
	}

	@Test
	public void testGetMessageBoardMessage() throws Exception {
		MessageBoardMessage postMessageBoardMessage =
			testGetMessageBoardMessage_addMessageBoardMessage();

		MessageBoardMessage getMessageBoardMessage =
			messageBoardMessageResource.getMessageBoardMessage(
				postMessageBoardMessage.getId());

		assertEquals(postMessageBoardMessage, getMessageBoardMessage);
		assertValid(getMessageBoardMessage);
	}

	protected MessageBoardMessage
			testGetMessageBoardMessage_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetMessageBoardMessage() throws Exception {
		MessageBoardMessage messageBoardMessage =
			testGraphQLGetMessageBoardMessage_addMessageBoardMessage();

		Assert.assertTrue(
			equals(
				messageBoardMessage,
				MessageBoardMessageSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"messageBoardMessage",
								new HashMap<String, Object>() {
									{
										put(
											"messageBoardMessageId",
											messageBoardMessage.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/messageBoardMessage"))));
	}

	@Test
	public void testGraphQLGetMessageBoardMessageNotFound() throws Exception {
		Long irrelevantMessageBoardMessageId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"messageBoardMessage",
						new HashMap<String, Object>() {
							{
								put(
									"messageBoardMessageId",
									irrelevantMessageBoardMessageId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected MessageBoardMessage
			testGraphQLGetMessageBoardMessage_addMessageBoardMessage()
		throws Exception {

		return testGraphQLMessageBoardMessage_addMessageBoardMessage();
	}

	@Test
	public void testPatchMessageBoardMessage() throws Exception {
		MessageBoardMessage postMessageBoardMessage =
			testPatchMessageBoardMessage_addMessageBoardMessage();

		MessageBoardMessage randomPatchMessageBoardMessage =
			randomPatchMessageBoardMessage();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage patchMessageBoardMessage =
			messageBoardMessageResource.patchMessageBoardMessage(
				postMessageBoardMessage.getId(),
				randomPatchMessageBoardMessage);

		MessageBoardMessage expectedPatchMessageBoardMessage =
			postMessageBoardMessage.clone();

		BeanTestUtil.copyProperties(
			randomPatchMessageBoardMessage, expectedPatchMessageBoardMessage);

		MessageBoardMessage getMessageBoardMessage =
			messageBoardMessageResource.getMessageBoardMessage(
				patchMessageBoardMessage.getId());

		assertEquals(expectedPatchMessageBoardMessage, getMessageBoardMessage);
		assertValid(getMessageBoardMessage);
	}

	protected MessageBoardMessage
			testPatchMessageBoardMessage_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPutMessageBoardMessage() throws Exception {
		MessageBoardMessage postMessageBoardMessage =
			testPutMessageBoardMessage_addMessageBoardMessage();

		MessageBoardMessage randomMessageBoardMessage =
			randomMessageBoardMessage();

		MessageBoardMessage putMessageBoardMessage =
			messageBoardMessageResource.putMessageBoardMessage(
				postMessageBoardMessage.getId(), randomMessageBoardMessage);

		assertEquals(randomMessageBoardMessage, putMessageBoardMessage);
		assertValid(putMessageBoardMessage);

		MessageBoardMessage getMessageBoardMessage =
			messageBoardMessageResource.getMessageBoardMessage(
				putMessageBoardMessage.getId());

		assertEquals(randomMessageBoardMessage, getMessageBoardMessage);
		assertValid(getMessageBoardMessage);
	}

	protected MessageBoardMessage
			testPutMessageBoardMessage_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteMessageBoardMessageMyRating() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage =
			testDeleteMessageBoardMessageMyRating_addMessageBoardMessage();

		assertHttpResponseStatusCode(
			204,
			messageBoardMessageResource.
				deleteMessageBoardMessageMyRatingHttpResponse(
					messageBoardMessage.getId()));

		assertHttpResponseStatusCode(
			404,
			messageBoardMessageResource.
				getMessageBoardMessageMyRatingHttpResponse(
					messageBoardMessage.getId()));

		assertHttpResponseStatusCode(
			404,
			messageBoardMessageResource.
				getMessageBoardMessageMyRatingHttpResponse(0L));
	}

	protected MessageBoardMessage
			testDeleteMessageBoardMessageMyRating_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetMessageBoardMessagePermissionsPage() throws Exception {
		MessageBoardMessage postMessageBoardMessage =
			testGetMessageBoardMessagePermissionsPage_addMessageBoardMessage();

		Page<Permission> page =
			messageBoardMessageResource.getMessageBoardMessagePermissionsPage(
				postMessageBoardMessage.getId(), RoleConstants.GUEST);

		Assert.assertNotNull(page);
	}

	protected MessageBoardMessage
			testGetMessageBoardMessagePermissionsPage_addMessageBoardMessage()
		throws Exception {

		return testPostMessageBoardMessageMessageBoardMessage_addMessageBoardMessage(
			randomMessageBoardMessage());
	}

	@Test
	public void testPutMessageBoardMessagePermissionsPage() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage =
			testPutMessageBoardMessagePermissionsPage_addMessageBoardMessage();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		com.liferay.portal.kernel.model.Role role = RoleTestUtil.addRole(
			RoleConstants.TYPE_REGULAR);

		assertHttpResponseStatusCode(
			200,
			messageBoardMessageResource.
				putMessageBoardMessagePermissionsPageHttpResponse(
					messageBoardMessage.getId(),
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
			messageBoardMessageResource.
				putMessageBoardMessagePermissionsPageHttpResponse(
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

	protected MessageBoardMessage
			testPutMessageBoardMessagePermissionsPage_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPutMessageBoardMessageSubscribe() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage =
			testPutMessageBoardMessageSubscribe_addMessageBoardMessage();

		assertHttpResponseStatusCode(
			204,
			messageBoardMessageResource.
				putMessageBoardMessageSubscribeHttpResponse(
					messageBoardMessage.getId()));

		assertHttpResponseStatusCode(
			404,
			messageBoardMessageResource.
				putMessageBoardMessageSubscribeHttpResponse(0L));
	}

	protected MessageBoardMessage
			testPutMessageBoardMessageSubscribe_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPutMessageBoardMessageUnsubscribe() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage =
			testPutMessageBoardMessageUnsubscribe_addMessageBoardMessage();

		assertHttpResponseStatusCode(
			204,
			messageBoardMessageResource.
				putMessageBoardMessageUnsubscribeHttpResponse(
					messageBoardMessage.getId()));

		assertHttpResponseStatusCode(
			404,
			messageBoardMessageResource.
				putMessageBoardMessageUnsubscribeHttpResponse(0L));
	}

	protected MessageBoardMessage
			testPutMessageBoardMessageUnsubscribe_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPage()
		throws Exception {

		Long parentMessageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getParentMessageBoardMessageId();
		Long irrelevantParentMessageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getIrrelevantParentMessageBoardMessageId();

		Page<MessageBoardMessage> page =
			messageBoardMessageResource.
				getMessageBoardMessageMessageBoardMessagesPage(
					parentMessageBoardMessageId, null, null, null, null,
					Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantParentMessageBoardMessageId != null) {
			MessageBoardMessage irrelevantMessageBoardMessage =
				testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
					irrelevantParentMessageBoardMessageId,
					randomIrrelevantMessageBoardMessage());

			page =
				messageBoardMessageResource.
					getMessageBoardMessageMessageBoardMessagesPage(
						irrelevantParentMessageBoardMessageId, null, null, null,
						null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantMessageBoardMessage),
				(List<MessageBoardMessage>)page.getItems());
			assertValid(page);
		}

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				parentMessageBoardMessageId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				parentMessageBoardMessageId, randomMessageBoardMessage());

		page =
			messageBoardMessageResource.
				getMessageBoardMessageMessageBoardMessagesPage(
					parentMessageBoardMessageId, null, null, null, null,
					Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(messageBoardMessage1, messageBoardMessage2),
			(List<MessageBoardMessage>)page.getItems());
		assertValid(page);

		messageBoardMessageResource.deleteMessageBoardMessage(
			messageBoardMessage1.getId());

		messageBoardMessageResource.deleteMessageBoardMessage(
			messageBoardMessage2.getId());
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentMessageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getParentMessageBoardMessageId();

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();

		messageBoardMessage1 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				parentMessageBoardMessageId, messageBoardMessage1);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				messageBoardMessageResource.
					getMessageBoardMessageMessageBoardMessagesPage(
						parentMessageBoardMessageId, null, null, null,
						getFilterString(
							entityField, "between", messageBoardMessage1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentMessageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getParentMessageBoardMessageId();

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				parentMessageBoardMessageId, randomMessageBoardMessage());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				parentMessageBoardMessageId, randomMessageBoardMessage());

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				messageBoardMessageResource.
					getMessageBoardMessageMessageBoardMessagesPage(
						parentMessageBoardMessageId, null, null, null,
						getFilterString(
							entityField, "eq", messageBoardMessage1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentMessageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getParentMessageBoardMessageId();

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				parentMessageBoardMessageId, randomMessageBoardMessage());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				parentMessageBoardMessageId, randomMessageBoardMessage());

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				messageBoardMessageResource.
					getMessageBoardMessageMessageBoardMessagesPage(
						parentMessageBoardMessageId, null, null, null,
						getFilterString(
							entityField, "eq", messageBoardMessage1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithPagination()
		throws Exception {

		Long parentMessageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getParentMessageBoardMessageId();

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				parentMessageBoardMessageId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				parentMessageBoardMessageId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage3 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				parentMessageBoardMessageId, randomMessageBoardMessage());

		Page<MessageBoardMessage> page1 =
			messageBoardMessageResource.
				getMessageBoardMessageMessageBoardMessagesPage(
					parentMessageBoardMessageId, null, null, null, null,
					Pagination.of(1, 2), null);

		List<MessageBoardMessage> messageBoardMessages1 =
			(List<MessageBoardMessage>)page1.getItems();

		Assert.assertEquals(
			messageBoardMessages1.toString(), 2, messageBoardMessages1.size());

		Page<MessageBoardMessage> page2 =
			messageBoardMessageResource.
				getMessageBoardMessageMessageBoardMessagesPage(
					parentMessageBoardMessageId, null, null, null, null,
					Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<MessageBoardMessage> messageBoardMessages2 =
			(List<MessageBoardMessage>)page2.getItems();

		Assert.assertEquals(
			messageBoardMessages2.toString(), 1, messageBoardMessages2.size());

		Page<MessageBoardMessage> page3 =
			messageBoardMessageResource.
				getMessageBoardMessageMessageBoardMessagesPage(
					parentMessageBoardMessageId, null, null, null, null,
					Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(
				messageBoardMessage1, messageBoardMessage2,
				messageBoardMessage3),
			(List<MessageBoardMessage>)page3.getItems());
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithSortDateTime()
		throws Exception {

		testGetMessageBoardMessageMessageBoardMessagesPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, messageBoardMessage1, messageBoardMessage2) -> {
				BeanTestUtil.setProperty(
					messageBoardMessage1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithSortDouble()
		throws Exception {

		testGetMessageBoardMessageMessageBoardMessagesPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, messageBoardMessage1, messageBoardMessage2) -> {
				BeanTestUtil.setProperty(
					messageBoardMessage1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					messageBoardMessage2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithSortInteger()
		throws Exception {

		testGetMessageBoardMessageMessageBoardMessagesPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, messageBoardMessage1, messageBoardMessage2) -> {
				BeanTestUtil.setProperty(
					messageBoardMessage1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					messageBoardMessage2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetMessageBoardMessageMessageBoardMessagesPageWithSortString()
		throws Exception {

		testGetMessageBoardMessageMessageBoardMessagesPageWithSort(
			EntityField.Type.STRING,
			(entityField, messageBoardMessage1, messageBoardMessage2) -> {
				Class<?> clazz = messageBoardMessage1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						messageBoardMessage1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						messageBoardMessage2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						messageBoardMessage1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						messageBoardMessage2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						messageBoardMessage1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						messageBoardMessage2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetMessageBoardMessageMessageBoardMessagesPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, MessageBoardMessage, MessageBoardMessage,
				 Exception> unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentMessageBoardMessageId =
			testGetMessageBoardMessageMessageBoardMessagesPage_getParentMessageBoardMessageId();

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();
		MessageBoardMessage messageBoardMessage2 = randomMessageBoardMessage();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, messageBoardMessage1, messageBoardMessage2);
		}

		messageBoardMessage1 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				parentMessageBoardMessageId, messageBoardMessage1);

		messageBoardMessage2 =
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				parentMessageBoardMessageId, messageBoardMessage2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> ascPage =
				messageBoardMessageResource.
					getMessageBoardMessageMessageBoardMessagesPage(
						parentMessageBoardMessageId, null, null, null, null,
						Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(messageBoardMessage1, messageBoardMessage2),
				(List<MessageBoardMessage>)ascPage.getItems());

			Page<MessageBoardMessage> descPage =
				messageBoardMessageResource.
					getMessageBoardMessageMessageBoardMessagesPage(
						parentMessageBoardMessageId, null, null, null, null,
						Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(messageBoardMessage2, messageBoardMessage1),
				(List<MessageBoardMessage>)descPage.getItems());
		}
	}

	protected MessageBoardMessage
			testGetMessageBoardMessageMessageBoardMessagesPage_addMessageBoardMessage(
				Long parentMessageBoardMessageId,
				MessageBoardMessage messageBoardMessage)
		throws Exception {

		return messageBoardMessageResource.
			postMessageBoardMessageMessageBoardMessage(
				parentMessageBoardMessageId, messageBoardMessage);
	}

	protected Long
			testGetMessageBoardMessageMessageBoardMessagesPage_getParentMessageBoardMessageId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetMessageBoardMessageMessageBoardMessagesPage_getIrrelevantParentMessageBoardMessageId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostMessageBoardMessageMessageBoardMessage()
		throws Exception {

		MessageBoardMessage randomMessageBoardMessage =
			randomMessageBoardMessage();

		MessageBoardMessage postMessageBoardMessage =
			testPostMessageBoardMessageMessageBoardMessage_addMessageBoardMessage(
				randomMessageBoardMessage);

		assertEquals(randomMessageBoardMessage, postMessageBoardMessage);
		assertValid(postMessageBoardMessage);
	}

	protected MessageBoardMessage
			testPostMessageBoardMessageMessageBoardMessage_addMessageBoardMessage(
				MessageBoardMessage messageBoardMessage)
		throws Exception {

		return messageBoardMessageResource.
			postMessageBoardMessageMessageBoardMessage(
				testGetMessageBoardMessageMessageBoardMessagesPage_getParentMessageBoardMessageId(),
				messageBoardMessage);
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPage()
		throws Exception {

		Long messageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId();
		Long irrelevantMessageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getIrrelevantMessageBoardThreadId();

		Page<MessageBoardMessage> page =
			messageBoardMessageResource.
				getMessageBoardThreadMessageBoardMessagesPage(
					messageBoardThreadId, null, null, null,
					Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantMessageBoardThreadId != null) {
			MessageBoardMessage irrelevantMessageBoardMessage =
				testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
					irrelevantMessageBoardThreadId,
					randomIrrelevantMessageBoardMessage());

			page =
				messageBoardMessageResource.
					getMessageBoardThreadMessageBoardMessagesPage(
						irrelevantMessageBoardThreadId, null, null, null,
						Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantMessageBoardMessage),
				(List<MessageBoardMessage>)page.getItems());
			assertValid(page);
		}

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		page =
			messageBoardMessageResource.
				getMessageBoardThreadMessageBoardMessagesPage(
					messageBoardThreadId, null, null, null,
					Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(messageBoardMessage1, messageBoardMessage2),
			(List<MessageBoardMessage>)page.getItems());
		assertValid(page);

		messageBoardMessageResource.deleteMessageBoardMessage(
			messageBoardMessage1.getId());

		messageBoardMessageResource.deleteMessageBoardMessage(
			messageBoardMessage2.getId());
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId();

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();

		messageBoardMessage1 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, messageBoardMessage1);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				messageBoardMessageResource.
					getMessageBoardThreadMessageBoardMessagesPage(
						messageBoardThreadId, null, null,
						getFilterString(
							entityField, "between", messageBoardMessage1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId();

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				messageBoardMessageResource.
					getMessageBoardThreadMessageBoardMessagesPage(
						messageBoardThreadId, null, null,
						getFilterString(
							entityField, "eq", messageBoardMessage1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId();

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				messageBoardMessageResource.
					getMessageBoardThreadMessageBoardMessagesPage(
						messageBoardThreadId, null, null,
						getFilterString(
							entityField, "eq", messageBoardMessage1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithPagination()
		throws Exception {

		Long messageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId();

		MessageBoardMessage messageBoardMessage1 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage2 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage3 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, randomMessageBoardMessage());

		Page<MessageBoardMessage> page1 =
			messageBoardMessageResource.
				getMessageBoardThreadMessageBoardMessagesPage(
					messageBoardThreadId, null, null, null, Pagination.of(1, 2),
					null);

		List<MessageBoardMessage> messageBoardMessages1 =
			(List<MessageBoardMessage>)page1.getItems();

		Assert.assertEquals(
			messageBoardMessages1.toString(), 2, messageBoardMessages1.size());

		Page<MessageBoardMessage> page2 =
			messageBoardMessageResource.
				getMessageBoardThreadMessageBoardMessagesPage(
					messageBoardThreadId, null, null, null, Pagination.of(2, 2),
					null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<MessageBoardMessage> messageBoardMessages2 =
			(List<MessageBoardMessage>)page2.getItems();

		Assert.assertEquals(
			messageBoardMessages2.toString(), 1, messageBoardMessages2.size());

		Page<MessageBoardMessage> page3 =
			messageBoardMessageResource.
				getMessageBoardThreadMessageBoardMessagesPage(
					messageBoardThreadId, null, null, null, Pagination.of(1, 3),
					null);

		assertEqualsIgnoringOrder(
			Arrays.asList(
				messageBoardMessage1, messageBoardMessage2,
				messageBoardMessage3),
			(List<MessageBoardMessage>)page3.getItems());
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithSortDateTime()
		throws Exception {

		testGetMessageBoardThreadMessageBoardMessagesPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, messageBoardMessage1, messageBoardMessage2) -> {
				BeanTestUtil.setProperty(
					messageBoardMessage1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithSortDouble()
		throws Exception {

		testGetMessageBoardThreadMessageBoardMessagesPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, messageBoardMessage1, messageBoardMessage2) -> {
				BeanTestUtil.setProperty(
					messageBoardMessage1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					messageBoardMessage2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithSortInteger()
		throws Exception {

		testGetMessageBoardThreadMessageBoardMessagesPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, messageBoardMessage1, messageBoardMessage2) -> {
				BeanTestUtil.setProperty(
					messageBoardMessage1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					messageBoardMessage2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetMessageBoardThreadMessageBoardMessagesPageWithSortString()
		throws Exception {

		testGetMessageBoardThreadMessageBoardMessagesPageWithSort(
			EntityField.Type.STRING,
			(entityField, messageBoardMessage1, messageBoardMessage2) -> {
				Class<?> clazz = messageBoardMessage1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						messageBoardMessage1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						messageBoardMessage2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						messageBoardMessage1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						messageBoardMessage2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						messageBoardMessage1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						messageBoardMessage2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetMessageBoardThreadMessageBoardMessagesPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, MessageBoardMessage, MessageBoardMessage,
				 Exception> unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long messageBoardThreadId =
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId();

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();
		MessageBoardMessage messageBoardMessage2 = randomMessageBoardMessage();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, messageBoardMessage1, messageBoardMessage2);
		}

		messageBoardMessage1 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, messageBoardMessage1);

		messageBoardMessage2 =
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				messageBoardThreadId, messageBoardMessage2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> ascPage =
				messageBoardMessageResource.
					getMessageBoardThreadMessageBoardMessagesPage(
						messageBoardThreadId, null, null, null,
						Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(messageBoardMessage1, messageBoardMessage2),
				(List<MessageBoardMessage>)ascPage.getItems());

			Page<MessageBoardMessage> descPage =
				messageBoardMessageResource.
					getMessageBoardThreadMessageBoardMessagesPage(
						messageBoardThreadId, null, null, null,
						Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(messageBoardMessage2, messageBoardMessage1),
				(List<MessageBoardMessage>)descPage.getItems());
		}
	}

	protected MessageBoardMessage
			testGetMessageBoardThreadMessageBoardMessagesPage_addMessageBoardMessage(
				Long messageBoardThreadId,
				MessageBoardMessage messageBoardMessage)
		throws Exception {

		return messageBoardMessageResource.
			postMessageBoardThreadMessageBoardMessage(
				messageBoardThreadId, messageBoardMessage);
	}

	protected Long
			testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetMessageBoardThreadMessageBoardMessagesPage_getIrrelevantMessageBoardThreadId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostMessageBoardThreadMessageBoardMessage()
		throws Exception {

		MessageBoardMessage randomMessageBoardMessage =
			randomMessageBoardMessage();

		MessageBoardMessage postMessageBoardMessage =
			testPostMessageBoardThreadMessageBoardMessage_addMessageBoardMessage(
				randomMessageBoardMessage);

		assertEquals(randomMessageBoardMessage, postMessageBoardMessage);
		assertValid(postMessageBoardMessage);
	}

	protected MessageBoardMessage
			testPostMessageBoardThreadMessageBoardMessage_addMessageBoardMessage(
				MessageBoardMessage messageBoardMessage)
		throws Exception {

		return messageBoardMessageResource.
			postMessageBoardThreadMessageBoardMessage(
				testGetMessageBoardThreadMessageBoardMessagesPage_getMessageBoardThreadId(),
				messageBoardMessage);
	}

	@Test
	public void testGetSiteMessageBoardMessagesPage() throws Exception {
		Long siteId = testGetSiteMessageBoardMessagesPage_getSiteId();
		Long irrelevantSiteId =
			testGetSiteMessageBoardMessagesPage_getIrrelevantSiteId();

		Page<MessageBoardMessage> page =
			messageBoardMessageResource.getSiteMessageBoardMessagesPage(
				siteId, null, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantSiteId != null) {
			MessageBoardMessage irrelevantMessageBoardMessage =
				testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
					irrelevantSiteId, randomIrrelevantMessageBoardMessage());

			page = messageBoardMessageResource.getSiteMessageBoardMessagesPage(
				irrelevantSiteId, null, null, null, null, Pagination.of(1, 2),
				null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantMessageBoardMessage),
				(List<MessageBoardMessage>)page.getItems());
			assertValid(page);
		}

		MessageBoardMessage messageBoardMessage1 =
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				siteId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage2 =
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				siteId, randomMessageBoardMessage());

		page = messageBoardMessageResource.getSiteMessageBoardMessagesPage(
			siteId, null, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(messageBoardMessage1, messageBoardMessage2),
			(List<MessageBoardMessage>)page.getItems());
		assertValid(page);

		messageBoardMessageResource.deleteMessageBoardMessage(
			messageBoardMessage1.getId());

		messageBoardMessageResource.deleteMessageBoardMessage(
			messageBoardMessage2.getId());
	}

	@Test
	public void testGetSiteMessageBoardMessagesPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteMessageBoardMessagesPage_getSiteId();

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();

		messageBoardMessage1 =
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				siteId, messageBoardMessage1);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				messageBoardMessageResource.getSiteMessageBoardMessagesPage(
					siteId, null, null, null,
					getFilterString(
						entityField, "between", messageBoardMessage1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetSiteMessageBoardMessagesPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteMessageBoardMessagesPage_getSiteId();

		MessageBoardMessage messageBoardMessage1 =
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				siteId, randomMessageBoardMessage());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage2 =
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				siteId, randomMessageBoardMessage());

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				messageBoardMessageResource.getSiteMessageBoardMessagesPage(
					siteId, null, null, null,
					getFilterString(entityField, "eq", messageBoardMessage1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetSiteMessageBoardMessagesPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteMessageBoardMessagesPage_getSiteId();

		MessageBoardMessage messageBoardMessage1 =
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				siteId, randomMessageBoardMessage());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage2 =
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				siteId, randomMessageBoardMessage());

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> page =
				messageBoardMessageResource.getSiteMessageBoardMessagesPage(
					siteId, null, null, null,
					getFilterString(entityField, "eq", messageBoardMessage1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(messageBoardMessage1),
				(List<MessageBoardMessage>)page.getItems());
		}
	}

	@Test
	public void testGetSiteMessageBoardMessagesPageWithPagination()
		throws Exception {

		Long siteId = testGetSiteMessageBoardMessagesPage_getSiteId();

		MessageBoardMessage messageBoardMessage1 =
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				siteId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage2 =
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				siteId, randomMessageBoardMessage());

		MessageBoardMessage messageBoardMessage3 =
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				siteId, randomMessageBoardMessage());

		Page<MessageBoardMessage> page1 =
			messageBoardMessageResource.getSiteMessageBoardMessagesPage(
				siteId, null, null, null, null, Pagination.of(1, 2), null);

		List<MessageBoardMessage> messageBoardMessages1 =
			(List<MessageBoardMessage>)page1.getItems();

		Assert.assertEquals(
			messageBoardMessages1.toString(), 2, messageBoardMessages1.size());

		Page<MessageBoardMessage> page2 =
			messageBoardMessageResource.getSiteMessageBoardMessagesPage(
				siteId, null, null, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<MessageBoardMessage> messageBoardMessages2 =
			(List<MessageBoardMessage>)page2.getItems();

		Assert.assertEquals(
			messageBoardMessages2.toString(), 1, messageBoardMessages2.size());

		Page<MessageBoardMessage> page3 =
			messageBoardMessageResource.getSiteMessageBoardMessagesPage(
				siteId, null, null, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(
				messageBoardMessage1, messageBoardMessage2,
				messageBoardMessage3),
			(List<MessageBoardMessage>)page3.getItems());
	}

	@Test
	public void testGetSiteMessageBoardMessagesPageWithSortDateTime()
		throws Exception {

		testGetSiteMessageBoardMessagesPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, messageBoardMessage1, messageBoardMessage2) -> {
				BeanTestUtil.setProperty(
					messageBoardMessage1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetSiteMessageBoardMessagesPageWithSortDouble()
		throws Exception {

		testGetSiteMessageBoardMessagesPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, messageBoardMessage1, messageBoardMessage2) -> {
				BeanTestUtil.setProperty(
					messageBoardMessage1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					messageBoardMessage2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetSiteMessageBoardMessagesPageWithSortInteger()
		throws Exception {

		testGetSiteMessageBoardMessagesPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, messageBoardMessage1, messageBoardMessage2) -> {
				BeanTestUtil.setProperty(
					messageBoardMessage1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					messageBoardMessage2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetSiteMessageBoardMessagesPageWithSortString()
		throws Exception {

		testGetSiteMessageBoardMessagesPageWithSort(
			EntityField.Type.STRING,
			(entityField, messageBoardMessage1, messageBoardMessage2) -> {
				Class<?> clazz = messageBoardMessage1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						messageBoardMessage1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						messageBoardMessage2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						messageBoardMessage1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						messageBoardMessage2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						messageBoardMessage1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						messageBoardMessage2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetSiteMessageBoardMessagesPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, MessageBoardMessage, MessageBoardMessage,
				 Exception> unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long siteId = testGetSiteMessageBoardMessagesPage_getSiteId();

		MessageBoardMessage messageBoardMessage1 = randomMessageBoardMessage();
		MessageBoardMessage messageBoardMessage2 = randomMessageBoardMessage();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, messageBoardMessage1, messageBoardMessage2);
		}

		messageBoardMessage1 =
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				siteId, messageBoardMessage1);

		messageBoardMessage2 =
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				siteId, messageBoardMessage2);

		for (EntityField entityField : entityFields) {
			Page<MessageBoardMessage> ascPage =
				messageBoardMessageResource.getSiteMessageBoardMessagesPage(
					siteId, null, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(messageBoardMessage1, messageBoardMessage2),
				(List<MessageBoardMessage>)ascPage.getItems());

			Page<MessageBoardMessage> descPage =
				messageBoardMessageResource.getSiteMessageBoardMessagesPage(
					siteId, null, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(messageBoardMessage2, messageBoardMessage1),
				(List<MessageBoardMessage>)descPage.getItems());
		}
	}

	protected MessageBoardMessage
			testGetSiteMessageBoardMessagesPage_addMessageBoardMessage(
				Long siteId, MessageBoardMessage messageBoardMessage)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetSiteMessageBoardMessagesPage_getSiteId()
		throws Exception {

		return testGroup.getGroupId();
	}

	protected Long testGetSiteMessageBoardMessagesPage_getIrrelevantSiteId()
		throws Exception {

		return irrelevantGroup.getGroupId();
	}

	@Test
	public void testGraphQLGetSiteMessageBoardMessagesPage() throws Exception {
		Long siteId = testGetSiteMessageBoardMessagesPage_getSiteId();

		GraphQLField graphQLField = new GraphQLField(
			"messageBoardMessages",
			new HashMap<String, Object>() {
				{
					put("page", 1);
					put("pageSize", 10);

					put("siteKey", "\"" + siteId + "\"");
				}
			},
			new GraphQLField("items", getGraphQLFields()),
			new GraphQLField("page"), new GraphQLField("totalCount"));

		JSONObject messageBoardMessagesJSONObject =
			JSONUtil.getValueAsJSONObject(
				invokeGraphQLQuery(graphQLField), "JSONObject/data",
				"JSONObject/messageBoardMessages");

		Assert.assertEquals(
			0, messageBoardMessagesJSONObject.get("totalCount"));

		MessageBoardMessage messageBoardMessage1 =
			testGraphQLGetSiteMessageBoardMessagesPage_addMessageBoardMessage();
		MessageBoardMessage messageBoardMessage2 =
			testGraphQLGetSiteMessageBoardMessagesPage_addMessageBoardMessage();

		messageBoardMessagesJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/messageBoardMessages");

		Assert.assertEquals(
			2, messageBoardMessagesJSONObject.getLong("totalCount"));

		assertEqualsIgnoringOrder(
			Arrays.asList(messageBoardMessage1, messageBoardMessage2),
			Arrays.asList(
				MessageBoardMessageSerDes.toDTOs(
					messageBoardMessagesJSONObject.getString("items"))));
	}

	protected MessageBoardMessage
			testGraphQLGetSiteMessageBoardMessagesPage_addMessageBoardMessage()
		throws Exception {

		return testGraphQLMessageBoardMessage_addMessageBoardMessage();
	}

	@Test
	public void testDeleteSiteMessageBoardMessageByExternalReferenceCode()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage =
			testDeleteSiteMessageBoardMessageByExternalReferenceCode_addMessageBoardMessage();

		assertHttpResponseStatusCode(
			204,
			messageBoardMessageResource.
				deleteSiteMessageBoardMessageByExternalReferenceCodeHttpResponse(
					messageBoardMessage.getSiteId(),
					messageBoardMessage.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			messageBoardMessageResource.
				getSiteMessageBoardMessageByExternalReferenceCodeHttpResponse(
					messageBoardMessage.getSiteId(),
					messageBoardMessage.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			messageBoardMessageResource.
				getSiteMessageBoardMessageByExternalReferenceCodeHttpResponse(
					messageBoardMessage.getSiteId(),
					messageBoardMessage.getExternalReferenceCode()));
	}

	protected MessageBoardMessage
			testDeleteSiteMessageBoardMessageByExternalReferenceCode_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetSiteMessageBoardMessageByExternalReferenceCode()
		throws Exception {

		MessageBoardMessage postMessageBoardMessage =
			testGetSiteMessageBoardMessageByExternalReferenceCode_addMessageBoardMessage();

		MessageBoardMessage getMessageBoardMessage =
			messageBoardMessageResource.
				getSiteMessageBoardMessageByExternalReferenceCode(
					postMessageBoardMessage.getSiteId(),
					postMessageBoardMessage.getExternalReferenceCode());

		assertEquals(postMessageBoardMessage, getMessageBoardMessage);
		assertValid(getMessageBoardMessage);
	}

	protected MessageBoardMessage
			testGetSiteMessageBoardMessageByExternalReferenceCode_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSiteMessageBoardMessageByExternalReferenceCode()
		throws Exception {

		MessageBoardMessage messageBoardMessage =
			testGraphQLGetSiteMessageBoardMessageByExternalReferenceCode_addMessageBoardMessage();

		Assert.assertTrue(
			equals(
				messageBoardMessage,
				MessageBoardMessageSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"messageBoardMessageByExternalReferenceCode",
								new HashMap<String, Object>() {
									{
										put(
											"siteKey",
											"\"" +
												messageBoardMessage.
													getSiteId() + "\"");
										put(
											"externalReferenceCode",
											"\"" +
												messageBoardMessage.
													getExternalReferenceCode() +
														"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/messageBoardMessageByExternalReferenceCode"))));
	}

	@Test
	public void testGraphQLGetSiteMessageBoardMessageByExternalReferenceCodeNotFound()
		throws Exception {

		String irrelevantExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"messageBoardMessageByExternalReferenceCode",
						new HashMap<String, Object>() {
							{
								put(
									"siteKey",
									"\"" + irrelevantGroup.getGroupId() + "\"");
								put(
									"externalReferenceCode",
									irrelevantExternalReferenceCode);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected MessageBoardMessage
			testGraphQLGetSiteMessageBoardMessageByExternalReferenceCode_addMessageBoardMessage()
		throws Exception {

		return testGraphQLMessageBoardMessage_addMessageBoardMessage();
	}

	@Test
	public void testPutSiteMessageBoardMessageByExternalReferenceCode()
		throws Exception {

		MessageBoardMessage postMessageBoardMessage =
			testPutSiteMessageBoardMessageByExternalReferenceCode_addMessageBoardMessage();

		MessageBoardMessage randomMessageBoardMessage =
			randomMessageBoardMessage();

		MessageBoardMessage putMessageBoardMessage =
			messageBoardMessageResource.
				putSiteMessageBoardMessageByExternalReferenceCode(
					postMessageBoardMessage.getSiteId(),
					postMessageBoardMessage.getExternalReferenceCode(),
					randomMessageBoardMessage);

		assertEquals(randomMessageBoardMessage, putMessageBoardMessage);
		assertValid(putMessageBoardMessage);

		MessageBoardMessage getMessageBoardMessage =
			messageBoardMessageResource.
				getSiteMessageBoardMessageByExternalReferenceCode(
					putMessageBoardMessage.getSiteId(),
					putMessageBoardMessage.getExternalReferenceCode());

		assertEquals(randomMessageBoardMessage, getMessageBoardMessage);
		assertValid(getMessageBoardMessage);

		MessageBoardMessage newMessageBoardMessage =
			testPutSiteMessageBoardMessageByExternalReferenceCode_createMessageBoardMessage();

		putMessageBoardMessage =
			messageBoardMessageResource.
				putSiteMessageBoardMessageByExternalReferenceCode(
					newMessageBoardMessage.getSiteId(),
					newMessageBoardMessage.getExternalReferenceCode(),
					newMessageBoardMessage);

		assertEquals(newMessageBoardMessage, putMessageBoardMessage);
		assertValid(putMessageBoardMessage);

		getMessageBoardMessage =
			messageBoardMessageResource.
				getSiteMessageBoardMessageByExternalReferenceCode(
					putMessageBoardMessage.getSiteId(),
					putMessageBoardMessage.getExternalReferenceCode());

		assertEquals(newMessageBoardMessage, getMessageBoardMessage);

		Assert.assertEquals(
			newMessageBoardMessage.getExternalReferenceCode(),
			putMessageBoardMessage.getExternalReferenceCode());
	}

	protected MessageBoardMessage
			testPutSiteMessageBoardMessageByExternalReferenceCode_createMessageBoardMessage()
		throws Exception {

		return randomMessageBoardMessage();
	}

	protected MessageBoardMessage
			testPutSiteMessageBoardMessageByExternalReferenceCode_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetSiteMessageBoardMessageByFriendlyUrlPath()
		throws Exception {

		MessageBoardMessage postMessageBoardMessage =
			testGetSiteMessageBoardMessageByFriendlyUrlPath_addMessageBoardMessage();

		MessageBoardMessage getMessageBoardMessage =
			messageBoardMessageResource.
				getSiteMessageBoardMessageByFriendlyUrlPath(
					postMessageBoardMessage.getSiteId(),
					postMessageBoardMessage.getFriendlyUrlPath());

		assertEquals(postMessageBoardMessage, getMessageBoardMessage);
		assertValid(getMessageBoardMessage);
	}

	protected MessageBoardMessage
			testGetSiteMessageBoardMessageByFriendlyUrlPath_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSiteMessageBoardMessageByFriendlyUrlPath()
		throws Exception {

		MessageBoardMessage messageBoardMessage =
			testGraphQLGetSiteMessageBoardMessageByFriendlyUrlPath_addMessageBoardMessage();

		Assert.assertTrue(
			equals(
				messageBoardMessage,
				MessageBoardMessageSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"messageBoardMessageByFriendlyUrlPath",
								new HashMap<String, Object>() {
									{
										put(
											"siteKey",
											"\"" +
												messageBoardMessage.
													getSiteId() + "\"");
										put(
											"friendlyUrlPath",
											"\"" +
												messageBoardMessage.
													getFriendlyUrlPath() +
														"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/messageBoardMessageByFriendlyUrlPath"))));
	}

	@Test
	public void testGraphQLGetSiteMessageBoardMessageByFriendlyUrlPathNotFound()
		throws Exception {

		String irrelevantFriendlyUrlPath =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"messageBoardMessageByFriendlyUrlPath",
						new HashMap<String, Object>() {
							{
								put(
									"siteKey",
									"\"" + irrelevantGroup.getGroupId() + "\"");
								put(
									"friendlyUrlPath",
									irrelevantFriendlyUrlPath);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected MessageBoardMessage
			testGraphQLGetSiteMessageBoardMessageByFriendlyUrlPath_addMessageBoardMessage()
		throws Exception {

		return testGraphQLMessageBoardMessage_addMessageBoardMessage();
	}

	@Test
	public void testGetSiteMessageBoardMessagePermissionsPage()
		throws Exception {

		Page<Permission> page =
			messageBoardMessageResource.
				getSiteMessageBoardMessagePermissionsPage(
					testGroup.getGroupId(), RoleConstants.GUEST);

		Assert.assertNotNull(page);
	}

	protected MessageBoardMessage
			testGetSiteMessageBoardMessagePermissionsPage_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPutSiteMessageBoardMessagePermissionsPage()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		MessageBoardMessage messageBoardMessage =
			testPutSiteMessageBoardMessagePermissionsPage_addMessageBoardMessage();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		com.liferay.portal.kernel.model.Role role = RoleTestUtil.addRole(
			RoleConstants.TYPE_REGULAR);

		assertHttpResponseStatusCode(
			200,
			messageBoardMessageResource.
				putSiteMessageBoardMessagePermissionsPageHttpResponse(
					messageBoardMessage.getSiteId(),
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
			messageBoardMessageResource.
				putSiteMessageBoardMessagePermissionsPageHttpResponse(
					messageBoardMessage.getSiteId(),
					new Permission[] {
						new Permission() {
							{
								setActionIds(new String[] {"-"});
								setRoleName("-");
							}
						}
					}));
	}

	protected MessageBoardMessage
			testPutSiteMessageBoardMessagePermissionsPage_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	@Test
	public void testGetMessageBoardMessageMyRating() throws Exception {
		MessageBoardMessage postMessageBoardMessage =
			testGetMessageBoardMessage_addMessageBoardMessage();

		Rating postRating = testGetMessageBoardMessageMyRating_addRating(
			postMessageBoardMessage.getId(), randomRating());

		Rating getRating =
			messageBoardMessageResource.getMessageBoardMessageMyRating(
				postMessageBoardMessage.getId());

		assertEquals(postRating, getRating);
		assertValid(getRating);
	}

	protected Rating testGetMessageBoardMessageMyRating_addRating(
			long messageBoardMessageId, Rating rating)
		throws Exception {

		return messageBoardMessageResource.postMessageBoardMessageMyRating(
			messageBoardMessageId, rating);
	}

	@Test
	public void testPostMessageBoardMessageMyRating() throws Exception {
		Assert.assertTrue(true);
	}

	@Test
	public void testPutMessageBoardMessageMyRating() throws Exception {
		MessageBoardMessage postMessageBoardMessage =
			testPutMessageBoardMessage_addMessageBoardMessage();

		testPutMessageBoardMessageMyRating_addRating(
			postMessageBoardMessage.getId(), randomRating());

		Rating randomRating = randomRating();

		Rating putRating =
			messageBoardMessageResource.putMessageBoardMessageMyRating(
				postMessageBoardMessage.getId(), randomRating);

		assertEquals(randomRating, putRating);
		assertValid(putRating);
	}

	protected Rating testPutMessageBoardMessageMyRating_addRating(
			long messageBoardMessageId, Rating rating)
		throws Exception {

		return messageBoardMessageResource.postMessageBoardMessageMyRating(
			messageBoardMessageId, rating);
	}

	protected MessageBoardMessage
			testGraphQLMessageBoardMessage_addMessageBoardMessage()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		MessageBoardMessage messageBoardMessage,
		List<MessageBoardMessage> messageBoardMessages) {

		boolean contains = false;

		for (MessageBoardMessage item : messageBoardMessages) {
			if (equals(messageBoardMessage, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			messageBoardMessages + " does not contain " + messageBoardMessage,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		MessageBoardMessage messageBoardMessage1,
		MessageBoardMessage messageBoardMessage2) {

		Assert.assertTrue(
			messageBoardMessage1 + " does not equal " + messageBoardMessage2,
			equals(messageBoardMessage1, messageBoardMessage2));
	}

	protected void assertEquals(
		List<MessageBoardMessage> messageBoardMessages1,
		List<MessageBoardMessage> messageBoardMessages2) {

		Assert.assertEquals(
			messageBoardMessages1.size(), messageBoardMessages2.size());

		for (int i = 0; i < messageBoardMessages1.size(); i++) {
			MessageBoardMessage messageBoardMessage1 =
				messageBoardMessages1.get(i);
			MessageBoardMessage messageBoardMessage2 =
				messageBoardMessages2.get(i);

			assertEquals(messageBoardMessage1, messageBoardMessage2);
		}
	}

	protected void assertEquals(Rating rating1, Rating rating2) {
		Assert.assertTrue(
			rating1 + " does not equal " + rating2, equals(rating1, rating2));
	}

	protected void assertEqualsIgnoringOrder(
		List<MessageBoardMessage> messageBoardMessages1,
		List<MessageBoardMessage> messageBoardMessages2) {

		Assert.assertEquals(
			messageBoardMessages1.size(), messageBoardMessages2.size());

		for (MessageBoardMessage messageBoardMessage1 : messageBoardMessages1) {
			boolean contains = false;

			for (MessageBoardMessage messageBoardMessage2 :
					messageBoardMessages2) {

				if (equals(messageBoardMessage1, messageBoardMessage2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				messageBoardMessages2 + " does not contain " +
					messageBoardMessage1,
				contains);
		}
	}

	protected void assertValid(MessageBoardMessage messageBoardMessage)
		throws Exception {

		boolean valid = true;

		if (messageBoardMessage.getDateCreated() == null) {
			valid = false;
		}

		if (messageBoardMessage.getDateModified() == null) {
			valid = false;
		}

		if (messageBoardMessage.getId() == null) {
			valid = false;
		}

		if (!Objects.equals(
				messageBoardMessage.getSiteId(), testGroup.getGroupId())) {

			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (messageBoardMessage.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("aggregateRating", additionalAssertFieldName)) {
				if (messageBoardMessage.getAggregateRating() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("anonymous", additionalAssertFieldName)) {
				if (messageBoardMessage.getAnonymous() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("articleBody", additionalAssertFieldName)) {
				if (messageBoardMessage.getArticleBody() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("creator", additionalAssertFieldName)) {
				if (messageBoardMessage.getCreator() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"creatorStatistics", additionalAssertFieldName)) {

				if (messageBoardMessage.getCreatorStatistics() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (messageBoardMessage.getCustomFields() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("encodingFormat", additionalAssertFieldName)) {
				if (messageBoardMessage.getEncodingFormat() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (messageBoardMessage.getExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("friendlyUrlPath", additionalAssertFieldName)) {
				if (messageBoardMessage.getFriendlyUrlPath() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("headline", additionalAssertFieldName)) {
				if (messageBoardMessage.getHeadline() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("keywords", additionalAssertFieldName)) {
				if (messageBoardMessage.getKeywords() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"messageBoardSectionId", additionalAssertFieldName)) {

				if (messageBoardMessage.getMessageBoardSectionId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"messageBoardThreadId", additionalAssertFieldName)) {

				if (messageBoardMessage.getMessageBoardThreadId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfMessageBoardAttachments",
					additionalAssertFieldName)) {

				if (messageBoardMessage.getNumberOfMessageBoardAttachments() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfMessageBoardMessages",
					additionalAssertFieldName)) {

				if (messageBoardMessage.getNumberOfMessageBoardMessages() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"parentMessageBoardMessageId", additionalAssertFieldName)) {

				if (messageBoardMessage.getParentMessageBoardMessageId() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("relatedContents", additionalAssertFieldName)) {
				if (messageBoardMessage.getRelatedContents() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("showAsAnswer", additionalAssertFieldName)) {
				if (messageBoardMessage.getShowAsAnswer() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("status", additionalAssertFieldName)) {
				if (messageBoardMessage.getStatus() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("subscribed", additionalAssertFieldName)) {
				if (messageBoardMessage.getSubscribed() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("viewableBy", additionalAssertFieldName)) {
				if (messageBoardMessage.getViewableBy() == null) {
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

	protected void assertValid(Page<MessageBoardMessage> page) {
		boolean valid = false;

		java.util.Collection<MessageBoardMessage> messageBoardMessages =
			page.getItems();

		int size = messageBoardMessages.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected void assertValid(Rating rating) {
		boolean valid = true;

		if (rating.getDateCreated() == null) {
			valid = false;
		}

		if (rating.getDateModified() == null) {
			valid = false;
		}

		if (rating.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalRatingAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (rating.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("bestRating", additionalAssertFieldName)) {
				if (rating.getBestRating() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("creator", additionalAssertFieldName)) {
				if (rating.getCreator() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("ratingValue", additionalAssertFieldName)) {
				if (rating.getRatingValue() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("worstRating", additionalAssertFieldName)) {
				if (rating.getWorstRating() == null) {
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

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	protected String[] getAdditionalRatingAssertFieldNames() {
		return new String[0];
	}

	protected List<GraphQLField> getGraphQLFields() throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		graphQLFields.add(new GraphQLField("siteId"));

		for (java.lang.reflect.Field field :
				getDeclaredFields(
					com.liferay.headless.delivery.dto.v1_0.MessageBoardMessage.
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
		MessageBoardMessage messageBoardMessage1,
		MessageBoardMessage messageBoardMessage2) {

		if (messageBoardMessage1 == messageBoardMessage2) {
			return true;
		}

		if (!Objects.equals(
				messageBoardMessage1.getSiteId(),
				messageBoardMessage2.getSiteId())) {

			return false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)messageBoardMessage1.getActions(),
						(Map)messageBoardMessage2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("aggregateRating", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getAggregateRating(),
						messageBoardMessage2.getAggregateRating())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("anonymous", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getAnonymous(),
						messageBoardMessage2.getAnonymous())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("articleBody", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getArticleBody(),
						messageBoardMessage2.getArticleBody())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("creator", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getCreator(),
						messageBoardMessage2.getCreator())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"creatorStatistics", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						messageBoardMessage1.getCreatorStatistics(),
						messageBoardMessage2.getCreatorStatistics())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getCustomFields(),
						messageBoardMessage2.getCustomFields())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateCreated", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getDateCreated(),
						messageBoardMessage2.getDateCreated())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getDateModified(),
						messageBoardMessage2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("encodingFormat", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getEncodingFormat(),
						messageBoardMessage2.getEncodingFormat())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						messageBoardMessage1.getExternalReferenceCode(),
						messageBoardMessage2.getExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("friendlyUrlPath", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getFriendlyUrlPath(),
						messageBoardMessage2.getFriendlyUrlPath())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("headline", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getHeadline(),
						messageBoardMessage2.getHeadline())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getId(),
						messageBoardMessage2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("keywords", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getKeywords(),
						messageBoardMessage2.getKeywords())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"messageBoardSectionId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						messageBoardMessage1.getMessageBoardSectionId(),
						messageBoardMessage2.getMessageBoardSectionId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"messageBoardThreadId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						messageBoardMessage1.getMessageBoardThreadId(),
						messageBoardMessage2.getMessageBoardThreadId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfMessageBoardAttachments",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						messageBoardMessage1.
							getNumberOfMessageBoardAttachments(),
						messageBoardMessage2.
							getNumberOfMessageBoardAttachments())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"numberOfMessageBoardMessages",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						messageBoardMessage1.getNumberOfMessageBoardMessages(),
						messageBoardMessage2.
							getNumberOfMessageBoardMessages())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"parentMessageBoardMessageId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						messageBoardMessage1.getParentMessageBoardMessageId(),
						messageBoardMessage2.
							getParentMessageBoardMessageId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("relatedContents", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getRelatedContents(),
						messageBoardMessage2.getRelatedContents())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("showAsAnswer", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getShowAsAnswer(),
						messageBoardMessage2.getShowAsAnswer())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("status", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getStatus(),
						messageBoardMessage2.getStatus())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("subscribed", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getSubscribed(),
						messageBoardMessage2.getSubscribed())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("viewableBy", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						messageBoardMessage1.getViewableBy(),
						messageBoardMessage2.getViewableBy())) {

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

	protected boolean equals(Rating rating1, Rating rating2) {
		if (rating1 == rating2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalRatingAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						rating1.getActions(), rating2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("bestRating", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						rating1.getBestRating(), rating2.getBestRating())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("creator", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						rating1.getCreator(), rating2.getCreator())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateCreated", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						rating1.getDateCreated(), rating2.getDateCreated())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						rating1.getDateModified(), rating2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(rating1.getId(), rating2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("ratingValue", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						rating1.getRatingValue(), rating2.getRatingValue())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("worstRating", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						rating1.getWorstRating(), rating2.getWorstRating())) {

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

		if (!(_messageBoardMessageResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_messageBoardMessageResource;

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
		MessageBoardMessage messageBoardMessage) {

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

		if (entityFieldName.equals("aggregateRating")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("anonymous")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("articleBody")) {
			sb.append("'");
			sb.append(String.valueOf(messageBoardMessage.getArticleBody()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("creator")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("creatorStatistics")) {
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
							messageBoardMessage.getDateCreated(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							messageBoardMessage.getDateCreated(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(
					_dateFormat.format(messageBoardMessage.getDateCreated()));
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
							messageBoardMessage.getDateModified(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(
							messageBoardMessage.getDateModified(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(
					_dateFormat.format(messageBoardMessage.getDateModified()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("encodingFormat")) {
			sb.append("'");
			sb.append(String.valueOf(messageBoardMessage.getEncodingFormat()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("externalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(messageBoardMessage.getExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("friendlyUrlPath")) {
			sb.append("'");
			sb.append(String.valueOf(messageBoardMessage.getFriendlyUrlPath()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("headline")) {
			sb.append("'");
			sb.append(String.valueOf(messageBoardMessage.getHeadline()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("keywords")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("messageBoardSectionId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("messageBoardThreadId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("numberOfMessageBoardAttachments")) {
			sb.append(
				String.valueOf(
					messageBoardMessage.getNumberOfMessageBoardAttachments()));

			return sb.toString();
		}

		if (entityFieldName.equals("numberOfMessageBoardMessages")) {
			sb.append(
				String.valueOf(
					messageBoardMessage.getNumberOfMessageBoardMessages()));

			return sb.toString();
		}

		if (entityFieldName.equals("parentMessageBoardMessageId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("relatedContents")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("showAsAnswer")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("siteId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("status")) {
			sb.append("'");
			sb.append(String.valueOf(messageBoardMessage.getStatus()));
			sb.append("'");

			return sb.toString();
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

	protected MessageBoardMessage randomMessageBoardMessage() throws Exception {
		return new MessageBoardMessage() {
			{
				anonymous = RandomTestUtil.randomBoolean();
				articleBody = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				encodingFormat = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				friendlyUrlPath = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				headline = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				messageBoardSectionId = RandomTestUtil.randomLong();
				messageBoardThreadId = RandomTestUtil.randomLong();
				numberOfMessageBoardAttachments = RandomTestUtil.randomInt();
				numberOfMessageBoardMessages = RandomTestUtil.randomInt();
				parentMessageBoardMessageId = RandomTestUtil.randomLong();
				showAsAnswer = RandomTestUtil.randomBoolean();
				siteId = testGroup.getGroupId();
				status = StringUtil.toLowerCase(RandomTestUtil.randomString());
				subscribed = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected MessageBoardMessage randomIrrelevantMessageBoardMessage()
		throws Exception {

		MessageBoardMessage randomIrrelevantMessageBoardMessage =
			randomMessageBoardMessage();

		randomIrrelevantMessageBoardMessage.setSiteId(
			irrelevantGroup.getGroupId());

		return randomIrrelevantMessageBoardMessage;
	}

	protected MessageBoardMessage randomPatchMessageBoardMessage()
		throws Exception {

		return randomMessageBoardMessage();
	}

	protected Rating randomRating() throws Exception {
		return new Rating() {
			{
				bestRating = RandomTestUtil.randomDouble();
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				id = RandomTestUtil.randomLong();
				ratingValue = RandomTestUtil.randomDouble();
				worstRating = RandomTestUtil.randomDouble();
			}
		};
	}

	protected MessageBoardMessageResource messageBoardMessageResource;
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
		LogFactoryUtil.getLog(BaseMessageBoardMessageResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private
		com.liferay.headless.delivery.resource.v1_0.MessageBoardMessageResource
			_messageBoardMessageResource;

}