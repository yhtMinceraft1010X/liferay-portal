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

import com.liferay.headless.delivery.client.dto.v1_0.Comment;
import com.liferay.headless.delivery.client.dto.v1_0.Field;
import com.liferay.headless.delivery.client.http.HttpInvoker;
import com.liferay.headless.delivery.client.pagination.Page;
import com.liferay.headless.delivery.client.pagination.Pagination;
import com.liferay.headless.delivery.client.resource.v1_0.CommentResource;
import com.liferay.headless.delivery.client.serdes.v1_0.CommentSerDes;
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
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseCommentResourceTestCase {

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

		_commentResource.setContextCompany(testCompany);

		CommentResource.Builder builder = CommentResource.builder();

		commentResource = builder.authentication(
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

		Comment comment1 = randomComment();

		String json = objectMapper.writeValueAsString(comment1);

		Comment comment2 = CommentSerDes.toDTO(json);

		Assert.assertTrue(equals(comment1, comment2));
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

		Comment comment = randomComment();

		String json1 = objectMapper.writeValueAsString(comment);
		String json2 = CommentSerDes.toJSON(comment);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Comment comment = randomComment();

		comment.setExternalReferenceCode(regex);
		comment.setText(regex);

		String json = CommentSerDes.toJSON(comment);

		Assert.assertFalse(json.contains(regex));

		comment = CommentSerDes.toDTO(json);

		Assert.assertEquals(regex, comment.getExternalReferenceCode());
		Assert.assertEquals(regex, comment.getText());
	}

	@Test
	public void testGetBlogPostingCommentsPage() throws Exception {
		Long blogPostingId = testGetBlogPostingCommentsPage_getBlogPostingId();
		Long irrelevantBlogPostingId =
			testGetBlogPostingCommentsPage_getIrrelevantBlogPostingId();

		Page<Comment> page = commentResource.getBlogPostingCommentsPage(
			blogPostingId, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantBlogPostingId != null) {
			Comment irrelevantComment =
				testGetBlogPostingCommentsPage_addComment(
					irrelevantBlogPostingId, randomIrrelevantComment());

			page = commentResource.getBlogPostingCommentsPage(
				irrelevantBlogPostingId, null, null, null, Pagination.of(1, 2),
				null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantComment),
				(List<Comment>)page.getItems());
			assertValid(page);
		}

		Comment comment1 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		Comment comment2 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		page = commentResource.getBlogPostingCommentsPage(
			blogPostingId, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(comment1, comment2), (List<Comment>)page.getItems());
		assertValid(page);

		commentResource.deleteComment(comment1.getId());

		commentResource.deleteComment(comment2.getId());
	}

	@Test
	public void testGetBlogPostingCommentsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long blogPostingId = testGetBlogPostingCommentsPage_getBlogPostingId();

		Comment comment1 = randomComment();

		comment1 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, comment1);

		for (EntityField entityField : entityFields) {
			Page<Comment> page = commentResource.getBlogPostingCommentsPage(
				blogPostingId, null, null,
				getFilterString(entityField, "between", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetBlogPostingCommentsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long blogPostingId = testGetBlogPostingCommentsPage_getBlogPostingId();

		Comment comment1 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment2 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		for (EntityField entityField : entityFields) {
			Page<Comment> page = commentResource.getBlogPostingCommentsPage(
				blogPostingId, null, null,
				getFilterString(entityField, "eq", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetBlogPostingCommentsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long blogPostingId = testGetBlogPostingCommentsPage_getBlogPostingId();

		Comment comment1 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment2 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		for (EntityField entityField : entityFields) {
			Page<Comment> page = commentResource.getBlogPostingCommentsPage(
				blogPostingId, null, null,
				getFilterString(entityField, "eq", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetBlogPostingCommentsPageWithPagination()
		throws Exception {

		Long blogPostingId = testGetBlogPostingCommentsPage_getBlogPostingId();

		Comment comment1 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		Comment comment2 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		Comment comment3 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		Page<Comment> page1 = commentResource.getBlogPostingCommentsPage(
			blogPostingId, null, null, null, Pagination.of(1, 2), null);

		List<Comment> comments1 = (List<Comment>)page1.getItems();

		Assert.assertEquals(comments1.toString(), 2, comments1.size());

		Page<Comment> page2 = commentResource.getBlogPostingCommentsPage(
			blogPostingId, null, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<Comment> comments2 = (List<Comment>)page2.getItems();

		Assert.assertEquals(comments2.toString(), 1, comments2.size());

		Page<Comment> page3 = commentResource.getBlogPostingCommentsPage(
			blogPostingId, null, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(comment1, comment2, comment3),
			(List<Comment>)page3.getItems());
	}

	@Test
	public void testGetBlogPostingCommentsPageWithSortDateTime()
		throws Exception {

		testGetBlogPostingCommentsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, comment1, comment2) -> {
				BeanTestUtil.setProperty(
					comment1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetBlogPostingCommentsPageWithSortDouble()
		throws Exception {

		testGetBlogPostingCommentsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, comment1, comment2) -> {
				BeanTestUtil.setProperty(comment1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(comment2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetBlogPostingCommentsPageWithSortInteger()
		throws Exception {

		testGetBlogPostingCommentsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, comment1, comment2) -> {
				BeanTestUtil.setProperty(comment1, entityField.getName(), 0);
				BeanTestUtil.setProperty(comment2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetBlogPostingCommentsPageWithSortString()
		throws Exception {

		testGetBlogPostingCommentsPageWithSort(
			EntityField.Type.STRING,
			(entityField, comment1, comment2) -> {
				Class<?> clazz = comment1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						comment1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						comment2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						comment1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						comment2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						comment1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						comment2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetBlogPostingCommentsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Comment, Comment, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long blogPostingId = testGetBlogPostingCommentsPage_getBlogPostingId();

		Comment comment1 = randomComment();
		Comment comment2 = randomComment();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, comment1, comment2);
		}

		comment1 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, comment1);

		comment2 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, comment2);

		for (EntityField entityField : entityFields) {
			Page<Comment> ascPage = commentResource.getBlogPostingCommentsPage(
				blogPostingId, null, null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(comment1, comment2),
				(List<Comment>)ascPage.getItems());

			Page<Comment> descPage = commentResource.getBlogPostingCommentsPage(
				blogPostingId, null, null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(comment2, comment1),
				(List<Comment>)descPage.getItems());
		}
	}

	protected Comment testGetBlogPostingCommentsPage_addComment(
			Long blogPostingId, Comment comment)
		throws Exception {

		return commentResource.postBlogPostingComment(blogPostingId, comment);
	}

	protected Long testGetBlogPostingCommentsPage_getBlogPostingId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetBlogPostingCommentsPage_getIrrelevantBlogPostingId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostBlogPostingComment() throws Exception {
		Comment randomComment = randomComment();

		Comment postComment = testPostBlogPostingComment_addComment(
			randomComment);

		assertEquals(randomComment, postComment);
		assertValid(postComment);
	}

	protected Comment testPostBlogPostingComment_addComment(Comment comment)
		throws Exception {

		return commentResource.postBlogPostingComment(
			testGetBlogPostingCommentsPage_getBlogPostingId(), comment);
	}

	@Test
	public void testDeleteComment() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment = testDeleteComment_addComment();

		assertHttpResponseStatusCode(
			204, commentResource.deleteCommentHttpResponse(comment.getId()));

		assertHttpResponseStatusCode(
			404, commentResource.getCommentHttpResponse(comment.getId()));

		assertHttpResponseStatusCode(
			404, commentResource.getCommentHttpResponse(0L));
	}

	protected Comment testDeleteComment_addComment() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteComment() throws Exception {
		Comment comment = testGraphQLDeleteComment_addComment();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteComment",
						new HashMap<String, Object>() {
							{
								put("commentId", comment.getId());
							}
						})),
				"JSONObject/data", "Object/deleteComment"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"comment",
					new HashMap<String, Object>() {
						{
							put("commentId", comment.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected Comment testGraphQLDeleteComment_addComment() throws Exception {
		return testGraphQLComment_addComment();
	}

	@Test
	public void testGetComment() throws Exception {
		Comment postComment = testGetComment_addComment();

		Comment getComment = commentResource.getComment(postComment.getId());

		assertEquals(postComment, getComment);
		assertValid(getComment);
	}

	protected Comment testGetComment_addComment() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetComment() throws Exception {
		Comment comment = testGraphQLGetComment_addComment();

		Assert.assertTrue(
			equals(
				comment,
				CommentSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"comment",
								new HashMap<String, Object>() {
									{
										put("commentId", comment.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/comment"))));
	}

	@Test
	public void testGraphQLGetCommentNotFound() throws Exception {
		Long irrelevantCommentId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"comment",
						new HashMap<String, Object>() {
							{
								put("commentId", irrelevantCommentId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Comment testGraphQLGetComment_addComment() throws Exception {
		return testGraphQLComment_addComment();
	}

	@Test
	public void testPutComment() throws Exception {
		Comment postComment = testPutComment_addComment();

		Comment randomComment = randomComment();

		Comment putComment = commentResource.putComment(
			postComment.getId(), randomComment);

		assertEquals(randomComment, putComment);
		assertValid(putComment);

		Comment getComment = commentResource.getComment(putComment.getId());

		assertEquals(randomComment, getComment);
		assertValid(getComment);
	}

	protected Comment testPutComment_addComment() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetCommentCommentsPage() throws Exception {
		Long parentCommentId = testGetCommentCommentsPage_getParentCommentId();
		Long irrelevantParentCommentId =
			testGetCommentCommentsPage_getIrrelevantParentCommentId();

		Page<Comment> page = commentResource.getCommentCommentsPage(
			parentCommentId, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantParentCommentId != null) {
			Comment irrelevantComment = testGetCommentCommentsPage_addComment(
				irrelevantParentCommentId, randomIrrelevantComment());

			page = commentResource.getCommentCommentsPage(
				irrelevantParentCommentId, null, null, null,
				Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantComment),
				(List<Comment>)page.getItems());
			assertValid(page);
		}

		Comment comment1 = testGetCommentCommentsPage_addComment(
			parentCommentId, randomComment());

		Comment comment2 = testGetCommentCommentsPage_addComment(
			parentCommentId, randomComment());

		page = commentResource.getCommentCommentsPage(
			parentCommentId, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(comment1, comment2), (List<Comment>)page.getItems());
		assertValid(page);

		commentResource.deleteComment(comment1.getId());

		commentResource.deleteComment(comment2.getId());
	}

	@Test
	public void testGetCommentCommentsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentCommentId = testGetCommentCommentsPage_getParentCommentId();

		Comment comment1 = randomComment();

		comment1 = testGetCommentCommentsPage_addComment(
			parentCommentId, comment1);

		for (EntityField entityField : entityFields) {
			Page<Comment> page = commentResource.getCommentCommentsPage(
				parentCommentId, null, null,
				getFilterString(entityField, "between", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetCommentCommentsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentCommentId = testGetCommentCommentsPage_getParentCommentId();

		Comment comment1 = testGetCommentCommentsPage_addComment(
			parentCommentId, randomComment());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment2 = testGetCommentCommentsPage_addComment(
			parentCommentId, randomComment());

		for (EntityField entityField : entityFields) {
			Page<Comment> page = commentResource.getCommentCommentsPage(
				parentCommentId, null, null,
				getFilterString(entityField, "eq", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetCommentCommentsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentCommentId = testGetCommentCommentsPage_getParentCommentId();

		Comment comment1 = testGetCommentCommentsPage_addComment(
			parentCommentId, randomComment());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment2 = testGetCommentCommentsPage_addComment(
			parentCommentId, randomComment());

		for (EntityField entityField : entityFields) {
			Page<Comment> page = commentResource.getCommentCommentsPage(
				parentCommentId, null, null,
				getFilterString(entityField, "eq", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetCommentCommentsPageWithPagination() throws Exception {
		Long parentCommentId = testGetCommentCommentsPage_getParentCommentId();

		Comment comment1 = testGetCommentCommentsPage_addComment(
			parentCommentId, randomComment());

		Comment comment2 = testGetCommentCommentsPage_addComment(
			parentCommentId, randomComment());

		Comment comment3 = testGetCommentCommentsPage_addComment(
			parentCommentId, randomComment());

		Page<Comment> page1 = commentResource.getCommentCommentsPage(
			parentCommentId, null, null, null, Pagination.of(1, 2), null);

		List<Comment> comments1 = (List<Comment>)page1.getItems();

		Assert.assertEquals(comments1.toString(), 2, comments1.size());

		Page<Comment> page2 = commentResource.getCommentCommentsPage(
			parentCommentId, null, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<Comment> comments2 = (List<Comment>)page2.getItems();

		Assert.assertEquals(comments2.toString(), 1, comments2.size());

		Page<Comment> page3 = commentResource.getCommentCommentsPage(
			parentCommentId, null, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(comment1, comment2, comment3),
			(List<Comment>)page3.getItems());
	}

	@Test
	public void testGetCommentCommentsPageWithSortDateTime() throws Exception {
		testGetCommentCommentsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, comment1, comment2) -> {
				BeanTestUtil.setProperty(
					comment1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetCommentCommentsPageWithSortDouble() throws Exception {
		testGetCommentCommentsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, comment1, comment2) -> {
				BeanTestUtil.setProperty(comment1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(comment2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetCommentCommentsPageWithSortInteger() throws Exception {
		testGetCommentCommentsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, comment1, comment2) -> {
				BeanTestUtil.setProperty(comment1, entityField.getName(), 0);
				BeanTestUtil.setProperty(comment2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetCommentCommentsPageWithSortString() throws Exception {
		testGetCommentCommentsPageWithSort(
			EntityField.Type.STRING,
			(entityField, comment1, comment2) -> {
				Class<?> clazz = comment1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						comment1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						comment2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						comment1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						comment2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						comment1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						comment2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetCommentCommentsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Comment, Comment, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long parentCommentId = testGetCommentCommentsPage_getParentCommentId();

		Comment comment1 = randomComment();
		Comment comment2 = randomComment();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, comment1, comment2);
		}

		comment1 = testGetCommentCommentsPage_addComment(
			parentCommentId, comment1);

		comment2 = testGetCommentCommentsPage_addComment(
			parentCommentId, comment2);

		for (EntityField entityField : entityFields) {
			Page<Comment> ascPage = commentResource.getCommentCommentsPage(
				parentCommentId, null, null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(comment1, comment2),
				(List<Comment>)ascPage.getItems());

			Page<Comment> descPage = commentResource.getCommentCommentsPage(
				parentCommentId, null, null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(comment2, comment1),
				(List<Comment>)descPage.getItems());
		}
	}

	protected Comment testGetCommentCommentsPage_addComment(
			Long parentCommentId, Comment comment)
		throws Exception {

		return commentResource.postCommentComment(parentCommentId, comment);
	}

	protected Long testGetCommentCommentsPage_getParentCommentId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetCommentCommentsPage_getIrrelevantParentCommentId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostCommentComment() throws Exception {
		Comment randomComment = randomComment();

		Comment postComment = testPostCommentComment_addComment(randomComment);

		assertEquals(randomComment, postComment);
		assertValid(postComment);
	}

	protected Comment testPostCommentComment_addComment(Comment comment)
		throws Exception {

		return commentResource.postCommentComment(
			testGetCommentCommentsPage_getParentCommentId(), comment);
	}

	@Test
	public void testGetDocumentCommentsPage() throws Exception {
		Long documentId = testGetDocumentCommentsPage_getDocumentId();
		Long irrelevantDocumentId =
			testGetDocumentCommentsPage_getIrrelevantDocumentId();

		Page<Comment> page = commentResource.getDocumentCommentsPage(
			documentId, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantDocumentId != null) {
			Comment irrelevantComment = testGetDocumentCommentsPage_addComment(
				irrelevantDocumentId, randomIrrelevantComment());

			page = commentResource.getDocumentCommentsPage(
				irrelevantDocumentId, null, null, null, Pagination.of(1, 2),
				null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantComment),
				(List<Comment>)page.getItems());
			assertValid(page);
		}

		Comment comment1 = testGetDocumentCommentsPage_addComment(
			documentId, randomComment());

		Comment comment2 = testGetDocumentCommentsPage_addComment(
			documentId, randomComment());

		page = commentResource.getDocumentCommentsPage(
			documentId, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(comment1, comment2), (List<Comment>)page.getItems());
		assertValid(page);

		commentResource.deleteComment(comment1.getId());

		commentResource.deleteComment(comment2.getId());
	}

	@Test
	public void testGetDocumentCommentsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long documentId = testGetDocumentCommentsPage_getDocumentId();

		Comment comment1 = randomComment();

		comment1 = testGetDocumentCommentsPage_addComment(documentId, comment1);

		for (EntityField entityField : entityFields) {
			Page<Comment> page = commentResource.getDocumentCommentsPage(
				documentId, null, null,
				getFilterString(entityField, "between", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetDocumentCommentsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long documentId = testGetDocumentCommentsPage_getDocumentId();

		Comment comment1 = testGetDocumentCommentsPage_addComment(
			documentId, randomComment());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment2 = testGetDocumentCommentsPage_addComment(
			documentId, randomComment());

		for (EntityField entityField : entityFields) {
			Page<Comment> page = commentResource.getDocumentCommentsPage(
				documentId, null, null,
				getFilterString(entityField, "eq", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetDocumentCommentsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long documentId = testGetDocumentCommentsPage_getDocumentId();

		Comment comment1 = testGetDocumentCommentsPage_addComment(
			documentId, randomComment());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment2 = testGetDocumentCommentsPage_addComment(
			documentId, randomComment());

		for (EntityField entityField : entityFields) {
			Page<Comment> page = commentResource.getDocumentCommentsPage(
				documentId, null, null,
				getFilterString(entityField, "eq", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetDocumentCommentsPageWithPagination() throws Exception {
		Long documentId = testGetDocumentCommentsPage_getDocumentId();

		Comment comment1 = testGetDocumentCommentsPage_addComment(
			documentId, randomComment());

		Comment comment2 = testGetDocumentCommentsPage_addComment(
			documentId, randomComment());

		Comment comment3 = testGetDocumentCommentsPage_addComment(
			documentId, randomComment());

		Page<Comment> page1 = commentResource.getDocumentCommentsPage(
			documentId, null, null, null, Pagination.of(1, 2), null);

		List<Comment> comments1 = (List<Comment>)page1.getItems();

		Assert.assertEquals(comments1.toString(), 2, comments1.size());

		Page<Comment> page2 = commentResource.getDocumentCommentsPage(
			documentId, null, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<Comment> comments2 = (List<Comment>)page2.getItems();

		Assert.assertEquals(comments2.toString(), 1, comments2.size());

		Page<Comment> page3 = commentResource.getDocumentCommentsPage(
			documentId, null, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(comment1, comment2, comment3),
			(List<Comment>)page3.getItems());
	}

	@Test
	public void testGetDocumentCommentsPageWithSortDateTime() throws Exception {
		testGetDocumentCommentsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, comment1, comment2) -> {
				BeanTestUtil.setProperty(
					comment1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetDocumentCommentsPageWithSortDouble() throws Exception {
		testGetDocumentCommentsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, comment1, comment2) -> {
				BeanTestUtil.setProperty(comment1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(comment2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetDocumentCommentsPageWithSortInteger() throws Exception {
		testGetDocumentCommentsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, comment1, comment2) -> {
				BeanTestUtil.setProperty(comment1, entityField.getName(), 0);
				BeanTestUtil.setProperty(comment2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetDocumentCommentsPageWithSortString() throws Exception {
		testGetDocumentCommentsPageWithSort(
			EntityField.Type.STRING,
			(entityField, comment1, comment2) -> {
				Class<?> clazz = comment1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						comment1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						comment2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						comment1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						comment2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						comment1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						comment2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetDocumentCommentsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Comment, Comment, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long documentId = testGetDocumentCommentsPage_getDocumentId();

		Comment comment1 = randomComment();
		Comment comment2 = randomComment();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, comment1, comment2);
		}

		comment1 = testGetDocumentCommentsPage_addComment(documentId, comment1);

		comment2 = testGetDocumentCommentsPage_addComment(documentId, comment2);

		for (EntityField entityField : entityFields) {
			Page<Comment> ascPage = commentResource.getDocumentCommentsPage(
				documentId, null, null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(comment1, comment2),
				(List<Comment>)ascPage.getItems());

			Page<Comment> descPage = commentResource.getDocumentCommentsPage(
				documentId, null, null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(comment2, comment1),
				(List<Comment>)descPage.getItems());
		}
	}

	protected Comment testGetDocumentCommentsPage_addComment(
			Long documentId, Comment comment)
		throws Exception {

		return commentResource.postDocumentComment(documentId, comment);
	}

	protected Long testGetDocumentCommentsPage_getDocumentId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetDocumentCommentsPage_getIrrelevantDocumentId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostDocumentComment() throws Exception {
		Comment randomComment = randomComment();

		Comment postComment = testPostDocumentComment_addComment(randomComment);

		assertEquals(randomComment, postComment);
		assertValid(postComment);
	}

	protected Comment testPostDocumentComment_addComment(Comment comment)
		throws Exception {

		return commentResource.postDocumentComment(
			testGetDocumentCommentsPage_getDocumentId(), comment);
	}

	@Test
	public void testDeleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment =
			testDeleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		assertHttpResponseStatusCode(
			204,
			commentResource.
				deleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCodeHttpResponse(
					testDeleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testDeleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode(),
					comment.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			commentResource.
				getSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCodeHttpResponse(
					testDeleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testDeleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode(),
					comment.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			commentResource.
				getSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCodeHttpResponse(
					testDeleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testDeleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode(),
					comment.getExternalReferenceCode()));
	}

	protected Long
			testDeleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testDeleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment
			testDeleteSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		Comment postComment =
			testGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		Comment getComment =
			commentResource.
				getSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode(
					testGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode(),
					postComment.getExternalReferenceCode());

		assertEquals(postComment, getComment);
		assertValid(getComment);
	}

	protected Long
			testGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment
			testGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		Comment comment =
			testGraphQLGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		Assert.assertTrue(
			equals(
				comment,
				CommentSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"blogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode",
								new HashMap<String, Object>() {
									{
										put(
											"siteKey",
											"\"" +
												testGraphQLGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId() +
													"\"");

										put(
											"blogPostingExternalReferenceCode",
											"\"" +
												testGraphQLGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode() +
													"\"");
										put(
											"externalReferenceCode",
											"\"" +
												comment.
													getExternalReferenceCode() +
														"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/blogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode"))));
	}

	protected Long
			testGraphQLGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGraphQLGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCodeNotFound()
		throws Exception {

		String irrelevantBlogPostingExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";
		String irrelevantExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"blogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode",
						new HashMap<String, Object>() {
							{
								put(
									"siteKey",
									"\"" + irrelevantGroup.getGroupId() + "\"");
								put(
									"blogPostingExternalReferenceCode",
									irrelevantBlogPostingExternalReferenceCode);
								put(
									"externalReferenceCode",
									irrelevantExternalReferenceCode);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Comment
			testGraphQLGetSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		return testGraphQLComment_addComment();
	}

	@Test
	public void testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		Comment postComment =
			testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		Comment randomComment = randomComment();

		Comment putComment =
			commentResource.
				putSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode(),
					postComment.getExternalReferenceCode(), randomComment);

		assertEquals(randomComment, putComment);
		assertValid(putComment);

		Comment getComment =
			commentResource.
				getSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode(),
					putComment.getExternalReferenceCode());

		assertEquals(randomComment, getComment);
		assertValid(getComment);

		Comment newComment =
			testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_createComment();

		putComment =
			commentResource.
				putSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode(),
					newComment.getExternalReferenceCode(), newComment);

		assertEquals(newComment, putComment);
		assertValid(putComment);

		getComment =
			commentResource.
				getSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode(),
					putComment.getExternalReferenceCode());

		assertEquals(newComment, getComment);

		Assert.assertEquals(
			newComment.getExternalReferenceCode(),
			putComment.getExternalReferenceCode());
	}

	protected Long
			testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_getBlogPostingExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment
			testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_createComment()
		throws Exception {

		return randomComment();
	}

	protected Comment
			testPutSiteBlogPostingByExternalReferenceCodeBlogPostingExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment =
			testDeleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		assertHttpResponseStatusCode(
			204,
			commentResource.
				deleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCodeHttpResponse(
					testDeleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testDeleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode(),
					comment.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			commentResource.
				getSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCodeHttpResponse(
					testDeleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testDeleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode(),
					comment.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			commentResource.
				getSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCodeHttpResponse(
					testDeleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testDeleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode(),
					comment.getExternalReferenceCode()));
	}

	protected Long
			testDeleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testDeleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment
			testDeleteSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		Comment postComment =
			testGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		Comment getComment =
			commentResource.
				getSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode(
					testGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode(),
					postComment.getExternalReferenceCode());

		assertEquals(postComment, getComment);
		assertValid(getComment);
	}

	protected Long
			testGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment
			testGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		Comment comment =
			testGraphQLGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		Assert.assertTrue(
			equals(
				comment,
				CommentSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"commentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode",
								new HashMap<String, Object>() {
									{
										put(
											"siteKey",
											"\"" +
												testGraphQLGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId() +
													"\"");

										put(
											"parentCommentExternalReferenceCode",
											"\"" +
												testGraphQLGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode() +
													"\"");
										put(
											"externalReferenceCode",
											"\"" +
												comment.
													getExternalReferenceCode() +
														"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/commentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode"))));
	}

	protected Long
			testGraphQLGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGraphQLGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCodeNotFound()
		throws Exception {

		String irrelevantParentCommentExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";
		String irrelevantExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"commentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode",
						new HashMap<String, Object>() {
							{
								put(
									"siteKey",
									"\"" + irrelevantGroup.getGroupId() + "\"");
								put(
									"parentCommentExternalReferenceCode",
									irrelevantParentCommentExternalReferenceCode);
								put(
									"externalReferenceCode",
									irrelevantExternalReferenceCode);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Comment
			testGraphQLGetSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		return testGraphQLComment_addComment();
	}

	@Test
	public void testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		Comment postComment =
			testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		Comment randomComment = randomComment();

		Comment putComment =
			commentResource.
				putSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode(),
					postComment.getExternalReferenceCode(), randomComment);

		assertEquals(randomComment, putComment);
		assertValid(putComment);

		Comment getComment =
			commentResource.
				getSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode(),
					putComment.getExternalReferenceCode());

		assertEquals(randomComment, getComment);
		assertValid(getComment);

		Comment newComment =
			testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_createComment();

		putComment =
			commentResource.
				putSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode(),
					newComment.getExternalReferenceCode(), newComment);

		assertEquals(newComment, putComment);
		assertValid(putComment);

		getComment =
			commentResource.
				getSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode(),
					putComment.getExternalReferenceCode());

		assertEquals(newComment, getComment);

		Assert.assertEquals(
			newComment.getExternalReferenceCode(),
			putComment.getExternalReferenceCode());
	}

	protected Long
			testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_getParentCommentExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment
			testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_createComment()
		throws Exception {

		return randomComment();
	}

	protected Comment
			testPutSiteCommentByExternalReferenceCodeParentCommentExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment =
			testDeleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		assertHttpResponseStatusCode(
			204,
			commentResource.
				deleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCodeHttpResponse(
					testDeleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testDeleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode(),
					comment.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			commentResource.
				getSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCodeHttpResponse(
					testDeleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testDeleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode(),
					comment.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			commentResource.
				getSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCodeHttpResponse(
					testDeleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testDeleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode(),
					comment.getExternalReferenceCode()));
	}

	protected Long
			testDeleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testDeleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment
			testDeleteSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		Comment postComment =
			testGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		Comment getComment =
			commentResource.
				getSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode(
					testGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode(),
					postComment.getExternalReferenceCode());

		assertEquals(postComment, getComment);
		assertValid(getComment);
	}

	protected Long
			testGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment
			testGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		Comment comment =
			testGraphQLGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		Assert.assertTrue(
			equals(
				comment,
				CommentSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"documentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode",
								new HashMap<String, Object>() {
									{
										put(
											"siteKey",
											"\"" +
												testGraphQLGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId() +
													"\"");

										put(
											"documentExternalReferenceCode",
											"\"" +
												testGraphQLGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode() +
													"\"");
										put(
											"externalReferenceCode",
											"\"" +
												comment.
													getExternalReferenceCode() +
														"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/documentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode"))));
	}

	protected Long
			testGraphQLGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGraphQLGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCodeNotFound()
		throws Exception {

		String irrelevantDocumentExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";
		String irrelevantExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"documentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode",
						new HashMap<String, Object>() {
							{
								put(
									"siteKey",
									"\"" + irrelevantGroup.getGroupId() + "\"");
								put(
									"documentExternalReferenceCode",
									irrelevantDocumentExternalReferenceCode);
								put(
									"externalReferenceCode",
									irrelevantExternalReferenceCode);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Comment
			testGraphQLGetSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		return testGraphQLComment_addComment();
	}

	@Test
	public void testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		Comment postComment =
			testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		Comment randomComment = randomComment();

		Comment putComment =
			commentResource.
				putSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode(),
					postComment.getExternalReferenceCode(), randomComment);

		assertEquals(randomComment, putComment);
		assertValid(putComment);

		Comment getComment =
			commentResource.
				getSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode(),
					putComment.getExternalReferenceCode());

		assertEquals(randomComment, getComment);
		assertValid(getComment);

		Comment newComment =
			testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_createComment();

		putComment =
			commentResource.
				putSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode(),
					newComment.getExternalReferenceCode(), newComment);

		assertEquals(newComment, putComment);
		assertValid(putComment);

		getComment =
			commentResource.
				getSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode(),
					putComment.getExternalReferenceCode());

		assertEquals(newComment, getComment);

		Assert.assertEquals(
			newComment.getExternalReferenceCode(),
			putComment.getExternalReferenceCode());
	}

	protected Long
			testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_getDocumentExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment
			testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_createComment()
		throws Exception {

		return randomComment();
	}

	protected Comment
			testPutSiteDocumentByExternalReferenceCodeDocumentExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment =
			testDeleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		assertHttpResponseStatusCode(
			204,
			commentResource.
				deleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCodeHttpResponse(
					testDeleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testDeleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode(),
					comment.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			commentResource.
				getSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCodeHttpResponse(
					testDeleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testDeleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode(),
					comment.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			commentResource.
				getSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCodeHttpResponse(
					testDeleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testDeleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode(),
					comment.getExternalReferenceCode()));
	}

	protected Long
			testDeleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testDeleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment
			testDeleteSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		Comment postComment =
			testGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		Comment getComment =
			commentResource.
				getSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode(
					testGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode(),
					postComment.getExternalReferenceCode());

		assertEquals(postComment, getComment);
		assertValid(getComment);
	}

	protected Long
			testGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment
			testGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		Comment comment =
			testGraphQLGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		Assert.assertTrue(
			equals(
				comment,
				CommentSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"structuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode",
								new HashMap<String, Object>() {
									{
										put(
											"siteKey",
											"\"" +
												testGraphQLGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId() +
													"\"");

										put(
											"structuredContentExternalReferenceCode",
											"\"" +
												testGraphQLGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode() +
													"\"");
										put(
											"externalReferenceCode",
											"\"" +
												comment.
													getExternalReferenceCode() +
														"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/structuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode"))));
	}

	protected Long
			testGraphQLGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGraphQLGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCodeNotFound()
		throws Exception {

		String irrelevantStructuredContentExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";
		String irrelevantExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"structuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode",
						new HashMap<String, Object>() {
							{
								put(
									"siteKey",
									"\"" + irrelevantGroup.getGroupId() + "\"");
								put(
									"structuredContentExternalReferenceCode",
									irrelevantStructuredContentExternalReferenceCode);
								put(
									"externalReferenceCode",
									irrelevantExternalReferenceCode);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected Comment
			testGraphQLGetSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		return testGraphQLComment_addComment();
	}

	@Test
	public void testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode()
		throws Exception {

		Comment postComment =
			testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_addComment();

		Comment randomComment = randomComment();

		Comment putComment =
			commentResource.
				putSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode(),
					postComment.getExternalReferenceCode(), randomComment);

		assertEquals(randomComment, putComment);
		assertValid(putComment);

		Comment getComment =
			commentResource.
				getSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode(),
					putComment.getExternalReferenceCode());

		assertEquals(randomComment, getComment);
		assertValid(getComment);

		Comment newComment =
			testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_createComment();

		putComment =
			commentResource.
				putSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode(),
					newComment.getExternalReferenceCode(), newComment);

		assertEquals(newComment, putComment);
		assertValid(putComment);

		getComment =
			commentResource.
				getSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode(
					testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId(),
					testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode(),
					putComment.getExternalReferenceCode());

		assertEquals(newComment, getComment);

		Assert.assertEquals(
			newComment.getExternalReferenceCode(),
			putComment.getExternalReferenceCode());
	}

	protected Long
			testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getSiteId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_getStructuredContentExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment
			testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_createComment()
		throws Exception {

		return randomComment();
	}

	protected Comment
			testPutSiteStructuredContentByExternalReferenceCodeStructuredContentExternalReferenceCodeCommentByExternalReferenceCode_addComment()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetStructuredContentCommentsPage() throws Exception {
		Long structuredContentId =
			testGetStructuredContentCommentsPage_getStructuredContentId();
		Long irrelevantStructuredContentId =
			testGetStructuredContentCommentsPage_getIrrelevantStructuredContentId();

		Page<Comment> page = commentResource.getStructuredContentCommentsPage(
			structuredContentId, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantStructuredContentId != null) {
			Comment irrelevantComment =
				testGetStructuredContentCommentsPage_addComment(
					irrelevantStructuredContentId, randomIrrelevantComment());

			page = commentResource.getStructuredContentCommentsPage(
				irrelevantStructuredContentId, null, null, null,
				Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantComment),
				(List<Comment>)page.getItems());
			assertValid(page);
		}

		Comment comment1 = testGetStructuredContentCommentsPage_addComment(
			structuredContentId, randomComment());

		Comment comment2 = testGetStructuredContentCommentsPage_addComment(
			structuredContentId, randomComment());

		page = commentResource.getStructuredContentCommentsPage(
			structuredContentId, null, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(comment1, comment2), (List<Comment>)page.getItems());
		assertValid(page);

		commentResource.deleteComment(comment1.getId());

		commentResource.deleteComment(comment2.getId());
	}

	@Test
	public void testGetStructuredContentCommentsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long structuredContentId =
			testGetStructuredContentCommentsPage_getStructuredContentId();

		Comment comment1 = randomComment();

		comment1 = testGetStructuredContentCommentsPage_addComment(
			structuredContentId, comment1);

		for (EntityField entityField : entityFields) {
			Page<Comment> page =
				commentResource.getStructuredContentCommentsPage(
					structuredContentId, null, null,
					getFilterString(entityField, "between", comment1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetStructuredContentCommentsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long structuredContentId =
			testGetStructuredContentCommentsPage_getStructuredContentId();

		Comment comment1 = testGetStructuredContentCommentsPage_addComment(
			structuredContentId, randomComment());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment2 = testGetStructuredContentCommentsPage_addComment(
			structuredContentId, randomComment());

		for (EntityField entityField : entityFields) {
			Page<Comment> page =
				commentResource.getStructuredContentCommentsPage(
					structuredContentId, null, null,
					getFilterString(entityField, "eq", comment1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetStructuredContentCommentsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long structuredContentId =
			testGetStructuredContentCommentsPage_getStructuredContentId();

		Comment comment1 = testGetStructuredContentCommentsPage_addComment(
			structuredContentId, randomComment());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment2 = testGetStructuredContentCommentsPage_addComment(
			structuredContentId, randomComment());

		for (EntityField entityField : entityFields) {
			Page<Comment> page =
				commentResource.getStructuredContentCommentsPage(
					structuredContentId, null, null,
					getFilterString(entityField, "eq", comment1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetStructuredContentCommentsPageWithPagination()
		throws Exception {

		Long structuredContentId =
			testGetStructuredContentCommentsPage_getStructuredContentId();

		Comment comment1 = testGetStructuredContentCommentsPage_addComment(
			structuredContentId, randomComment());

		Comment comment2 = testGetStructuredContentCommentsPage_addComment(
			structuredContentId, randomComment());

		Comment comment3 = testGetStructuredContentCommentsPage_addComment(
			structuredContentId, randomComment());

		Page<Comment> page1 = commentResource.getStructuredContentCommentsPage(
			structuredContentId, null, null, null, Pagination.of(1, 2), null);

		List<Comment> comments1 = (List<Comment>)page1.getItems();

		Assert.assertEquals(comments1.toString(), 2, comments1.size());

		Page<Comment> page2 = commentResource.getStructuredContentCommentsPage(
			structuredContentId, null, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<Comment> comments2 = (List<Comment>)page2.getItems();

		Assert.assertEquals(comments2.toString(), 1, comments2.size());

		Page<Comment> page3 = commentResource.getStructuredContentCommentsPage(
			structuredContentId, null, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(comment1, comment2, comment3),
			(List<Comment>)page3.getItems());
	}

	@Test
	public void testGetStructuredContentCommentsPageWithSortDateTime()
		throws Exception {

		testGetStructuredContentCommentsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, comment1, comment2) -> {
				BeanTestUtil.setProperty(
					comment1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetStructuredContentCommentsPageWithSortDouble()
		throws Exception {

		testGetStructuredContentCommentsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, comment1, comment2) -> {
				BeanTestUtil.setProperty(comment1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(comment2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetStructuredContentCommentsPageWithSortInteger()
		throws Exception {

		testGetStructuredContentCommentsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, comment1, comment2) -> {
				BeanTestUtil.setProperty(comment1, entityField.getName(), 0);
				BeanTestUtil.setProperty(comment2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetStructuredContentCommentsPageWithSortString()
		throws Exception {

		testGetStructuredContentCommentsPageWithSort(
			EntityField.Type.STRING,
			(entityField, comment1, comment2) -> {
				Class<?> clazz = comment1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						comment1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						comment2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						comment1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						comment2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						comment1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						comment2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetStructuredContentCommentsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Comment, Comment, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long structuredContentId =
			testGetStructuredContentCommentsPage_getStructuredContentId();

		Comment comment1 = randomComment();
		Comment comment2 = randomComment();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, comment1, comment2);
		}

		comment1 = testGetStructuredContentCommentsPage_addComment(
			structuredContentId, comment1);

		comment2 = testGetStructuredContentCommentsPage_addComment(
			structuredContentId, comment2);

		for (EntityField entityField : entityFields) {
			Page<Comment> ascPage =
				commentResource.getStructuredContentCommentsPage(
					structuredContentId, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(comment1, comment2),
				(List<Comment>)ascPage.getItems());

			Page<Comment> descPage =
				commentResource.getStructuredContentCommentsPage(
					structuredContentId, null, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(comment2, comment1),
				(List<Comment>)descPage.getItems());
		}
	}

	protected Comment testGetStructuredContentCommentsPage_addComment(
			Long structuredContentId, Comment comment)
		throws Exception {

		return commentResource.postStructuredContentComment(
			structuredContentId, comment);
	}

	protected Long testGetStructuredContentCommentsPage_getStructuredContentId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetStructuredContentCommentsPage_getIrrelevantStructuredContentId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostStructuredContentComment() throws Exception {
		Comment randomComment = randomComment();

		Comment postComment = testPostStructuredContentComment_addComment(
			randomComment);

		assertEquals(randomComment, postComment);
		assertValid(postComment);
	}

	protected Comment testPostStructuredContentComment_addComment(
			Comment comment)
		throws Exception {

		return commentResource.postStructuredContentComment(
			testGetStructuredContentCommentsPage_getStructuredContentId(),
			comment);
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected Comment testGraphQLComment_addComment() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(Comment comment, List<Comment> comments) {
		boolean contains = false;

		for (Comment item : comments) {
			if (equals(comment, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(comments + " does not contain " + comment, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Comment comment1, Comment comment2) {
		Assert.assertTrue(
			comment1 + " does not equal " + comment2,
			equals(comment1, comment2));
	}

	protected void assertEquals(
		List<Comment> comments1, List<Comment> comments2) {

		Assert.assertEquals(comments1.size(), comments2.size());

		for (int i = 0; i < comments1.size(); i++) {
			Comment comment1 = comments1.get(i);
			Comment comment2 = comments2.get(i);

			assertEquals(comment1, comment2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<Comment> comments1, List<Comment> comments2) {

		Assert.assertEquals(comments1.size(), comments2.size());

		for (Comment comment1 : comments1) {
			boolean contains = false;

			for (Comment comment2 : comments2) {
				if (equals(comment1, comment2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				comments2 + " does not contain " + comment1, contains);
		}
	}

	protected void assertValid(Comment comment) throws Exception {
		boolean valid = true;

		if (comment.getDateCreated() == null) {
			valid = false;
		}

		if (comment.getDateModified() == null) {
			valid = false;
		}

		if (comment.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (comment.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("creator", additionalAssertFieldName)) {
				if (comment.getCreator() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (comment.getExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("numberOfComments", additionalAssertFieldName)) {
				if (comment.getNumberOfComments() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("parentCommentId", additionalAssertFieldName)) {
				if (comment.getParentCommentId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("text", additionalAssertFieldName)) {
				if (comment.getText() == null) {
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

	protected void assertValid(Page<Comment> page) {
		boolean valid = false;

		java.util.Collection<Comment> comments = page.getItems();

		int size = comments.size();

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
					com.liferay.headless.delivery.dto.v1_0.Comment.class)) {

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

	protected boolean equals(Comment comment1, Comment comment2) {
		if (comment1 == comment2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)comment1.getActions(),
						(Map)comment2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("creator", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						comment1.getCreator(), comment2.getCreator())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateCreated", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						comment1.getDateCreated(), comment2.getDateCreated())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						comment1.getDateModified(),
						comment2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						comment1.getExternalReferenceCode(),
						comment2.getExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(comment1.getId(), comment2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("numberOfComments", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						comment1.getNumberOfComments(),
						comment2.getNumberOfComments())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("parentCommentId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						comment1.getParentCommentId(),
						comment2.getParentCommentId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("text", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						comment1.getText(), comment2.getText())) {

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

		if (!(_commentResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_commentResource;

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
		EntityField entityField, String operator, Comment comment) {

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

		if (entityFieldName.equals("creator")) {
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
						DateUtils.addSeconds(comment.getDateCreated(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(comment.getDateCreated(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(comment.getDateCreated()));
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
						DateUtils.addSeconds(comment.getDateModified(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(comment.getDateModified(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(comment.getDateModified()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("externalReferenceCode")) {
			sb.append("'");
			sb.append(String.valueOf(comment.getExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("numberOfComments")) {
			sb.append(String.valueOf(comment.getNumberOfComments()));

			return sb.toString();
		}

		if (entityFieldName.equals("parentCommentId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("text")) {
			sb.append("'");
			sb.append(String.valueOf(comment.getText()));
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

	protected Comment randomComment() throws Exception {
		return new Comment() {
			{
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				numberOfComments = RandomTestUtil.randomInt();
				parentCommentId = RandomTestUtil.randomLong();
				text = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected Comment randomIrrelevantComment() throws Exception {
		Comment randomIrrelevantComment = randomComment();

		return randomIrrelevantComment;
	}

	protected Comment randomPatchComment() throws Exception {
		return randomComment();
	}

	protected CommentResource commentResource;
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
		LogFactoryUtil.getLog(BaseCommentResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.delivery.resource.v1_0.CommentResource
		_commentResource;

}