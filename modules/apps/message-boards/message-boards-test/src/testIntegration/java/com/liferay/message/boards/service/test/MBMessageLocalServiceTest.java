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

package com.liferay.message.boards.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.message.boards.constants.MBCategoryConstants;
import com.liferay.message.boards.constants.MBMessageConstants;
import com.liferay.message.boards.exception.DuplicateMessageExternalReferenceCodeException;
import com.liferay.message.boards.model.MBMessage;
import com.liferay.message.boards.model.MBThread;
import com.liferay.message.boards.service.MBMessageLocalServiceUtil;
import com.liferay.message.boards.test.util.MBTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PropsValues;

import java.io.File;
import java.io.InputStream;

import java.text.DateFormat;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jonathan McCann
 * @author Sergio González
 */
@RunWith(Arquillian.class)
public class MBMessageLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testAddBBCodeSubjectWithEmptyBodyMessageInBBCode()
		throws Exception {

		String subject = "[i]subject[/i]";
		String body = StringPool.BLANK;
		List<ObjectValuePair<String, InputStream>> inputStreamOVPs =
			Collections.emptyList();

		MBMessage message = MBMessageLocalServiceUtil.addMessage(
			TestPropsValues.getUserId(), RandomTestUtil.randomString(),
			_group.getGroupId(), MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID,
			subject, body, "bbcode", inputStreamOVPs, false, 0.0, false,
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		Assert.assertEquals(subject, message.getSubject());
		Assert.assertEquals(subject, message.getBody());
	}

	@Test
	public void testAddHtmlSubjectWithEmptyBodyMessage() throws Exception {
		String subject = "<u>subject</u>";
		String body = StringPool.BLANK;
		List<ObjectValuePair<String, InputStream>> inputStreamOVPs =
			Collections.emptyList();

		MBMessage message = MBMessageLocalServiceUtil.addMessage(
			TestPropsValues.getUserId(), RandomTestUtil.randomString(),
			_group.getGroupId(), MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID,
			subject, body, "html", inputStreamOVPs, false, 0.0, false,
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		Assert.assertEquals(subject, message.getSubject());
		Assert.assertEquals(HtmlUtil.escape(subject), message.getBody());
	}

	@Test
	public void testAddHtmlSubjectWithEmptyBodyMessageInBBCode()
		throws Exception {

		String subject = "<u>subject</u>";
		String body = StringPool.BLANK;
		List<ObjectValuePair<String, InputStream>> inputStreamOVPs =
			Collections.emptyList();

		MBMessage message = MBMessageLocalServiceUtil.addMessage(
			TestPropsValues.getUserId(), RandomTestUtil.randomString(),
			_group.getGroupId(), MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID,
			subject, body, "bbcode", inputStreamOVPs, false, 0.0, false,
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		Assert.assertEquals(subject, message.getSubject());
		Assert.assertEquals(subject, message.getBody());
	}

	@Test
	public void testAddMessageAttachment() throws Exception {
		MBMessage message = addMessage(null, false);

		byte[] fileBytes = FileUtil.getBytes(
			getClass(), "dependencies/company_logo.png");

		File file = FileUtil.createTempFile(fileBytes);

		MBMessageLocalServiceUtil.addMessageAttachment(
			TestPropsValues.getUserId(), message.getMessageId(), "test", file,
			"image/png");

		Assert.assertEquals(1, message.getAttachmentsFileEntriesCount());
	}

	@Test
	public void testAddMessageWithEmptyBody() throws Exception {
		User user = TestPropsValues.getUser();
		String subject = StringUtil.randomString();

		MBMessage mbMessage = MBMessageLocalServiceUtil.addMessage(
			user.getUserId(), user.getFullName(), TestPropsValues.getGroupId(),
			MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID, subject,
			StringPool.BLANK, ServiceContextTestUtil.getServiceContext());

		Assert.assertEquals(subject, mbMessage.getBody());
	}

	@Test(expected = DuplicateMessageExternalReferenceCodeException.class)
	public void testAddMessageWithExistingExternalReferenceCode()
		throws Exception {

		User user = TestPropsValues.getUser();

		MBMessage message = addMessage();

		MBMessageLocalServiceUtil.addMessage(
			message.getExternalReferenceCode(), user.getUserId(),
			user.getFullName(), _group.getGroupId(),
			MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID, 0, 0,
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			MBMessageConstants.DEFAULT_FORMAT, Collections.emptyList(), false,
			0.0, false, ServiceContextTestUtil.getServiceContext());
	}

	@Test
	public void testAddMessageWithExternalReferenceCode() throws Exception {
		String externalReferenceCode = RandomTestUtil.randomString();
		User user = TestPropsValues.getUser();

		MBMessage mbMessage = MBMessageLocalServiceUtil.addMessage(
			externalReferenceCode, user.getUserId(), user.getFullName(),
			_group.getGroupId(), MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID,
			0, 0, RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			MBMessageConstants.DEFAULT_FORMAT, Collections.emptyList(), false,
			0.0, false, ServiceContextTestUtil.getServiceContext());

		Assert.assertEquals(
			externalReferenceCode, mbMessage.getExternalReferenceCode());
	}

	@Test
	public void testAddMessageWithNullBody() throws Exception {
		User user = TestPropsValues.getUser();
		String subject = StringUtil.randomString();

		MBMessage mbMessage = MBMessageLocalServiceUtil.addMessage(
			user.getUserId(), user.getFullName(), TestPropsValues.getGroupId(),
			MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID, subject, null,
			ServiceContextTestUtil.getServiceContext());

		Assert.assertEquals(subject, mbMessage.getBody());
	}

	@Test
	public void testAddMessageWithOnlyBlanksInBody() throws Exception {
		User user = TestPropsValues.getUser();
		String subject = StringUtil.randomString();
		String body = StringPool.SPACE;

		MBMessage mbMessage = MBMessageLocalServiceUtil.addMessage(
			user.getUserId(), user.getFullName(), TestPropsValues.getGroupId(),
			MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID, subject, body,
			ServiceContextTestUtil.getServiceContext());

		Assert.assertEquals(body, mbMessage.getBody());
	}

	@Test
	public void testAddMessageWithoutExternalReferenceCode() throws Exception {
		User user = TestPropsValues.getUser();

		MBMessage mbMessage = MBMessageLocalServiceUtil.addMessage(
			user.getUserId(), user.getFullName(), _group.getGroupId(),
			MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID,
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			ServiceContextTestUtil.getServiceContext());

		Assert.assertEquals(
			String.valueOf(mbMessage.getMessageId()),
			mbMessage.getExternalReferenceCode());
	}

	@Test
	public void testAddXSSMessageWithInvalidFormat() throws Exception {
		String subject = "<script>alert(1)</script>";
		String body = "<script>alert(2)</script>";
		String format = "text/plain";
		List<ObjectValuePair<String, InputStream>> inputStreamOVPs =
			Collections.emptyList();

		MBMessage message = MBMessageLocalServiceUtil.addMessage(
			TestPropsValues.getUserId(), RandomTestUtil.randomString(),
			_group.getGroupId(), MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID,
			subject, body, format, inputStreamOVPs, false, 0.0, false,
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		Assert.assertEquals(subject, message.getSubject());
		Assert.assertEquals(StringPool.BLANK, message.getBody());
		Assert.assertEquals("html", message.getFormat());
	}

	@Test
	public void testAddXSSSubjectWithEmptyBodyMessage() throws Exception {
		String subject = "<script>alert(1)</script>";
		String body = StringPool.BLANK;
		List<ObjectValuePair<String, InputStream>> inputStreamOVPs =
			Collections.emptyList();

		MBMessage message = MBMessageLocalServiceUtil.addMessage(
			TestPropsValues.getUserId(), RandomTestUtil.randomString(),
			_group.getGroupId(), MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID,
			subject, body, "html", inputStreamOVPs, false, 0.0, false,
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		Assert.assertEquals(subject, message.getSubject());
		Assert.assertEquals(HtmlUtil.escape(subject), message.getBody());
	}

	@Test(expected = PortalException.class)
	public void testCannotReplyToAnUnapprovedMessage() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		serviceContext.setWorkflowAction(WorkflowConstants.STATUS_APPROVED);

		MBMessage parentMessage = MBMessageLocalServiceUtil.addMessage(
			TestPropsValues.getUserId(), RandomTestUtil.randomString(),
			_group.getGroupId(), MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID,
			RandomTestUtil.randomString(), StringPool.BLANK, "bbcode",
			Collections.emptyList(), false, 0.0, false, serviceContext);

		MBMessageLocalServiceUtil.addMessage(
			TestPropsValues.getUserId(), RandomTestUtil.randomString(),
			_group.getGroupId(), MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID,
			parentMessage.getThreadId(), parentMessage.getMessageId(),
			RandomTestUtil.randomString(), StringPool.BLANK, "bbcode", null,
			false, 0.0, false, serviceContext);
	}

	@Test
	public void testDeleteMessageAttachment() throws Exception {
		MBMessage message = addMessage(null, false);

		byte[] fileBytes = FileUtil.getBytes(
			getClass(), "dependencies/company_logo.png");

		File file = FileUtil.createTempFile(fileBytes);

		MBMessageLocalServiceUtil.addMessageAttachment(
			TestPropsValues.getUserId(), message.getMessageId(), "test", file,
			"image/png");

		Assert.assertEquals(1, message.getAttachmentsFileEntriesCount());

		MBMessageLocalServiceUtil.deleteMessageAttachment(
			message.getMessageId(), "test");

		Assert.assertEquals(0, message.getAttachmentsFileEntriesCount());
	}

	@Test
	public void testGetChildMessagesWithStatusAny() throws Exception {
		MBMessage parentMessage = addMessage();

		MBMessage childMessage1 = addMessage(
			parentMessage, WorkflowConstants.ACTION_PUBLISH);
		MBMessage childMessage2 = addMessage(
			parentMessage, WorkflowConstants.ACTION_SAVE_DRAFT);

		Assert.assertEquals(
			2,
			MBMessageLocalServiceUtil.getChildMessagesCount(
				parentMessage.getMessageId(), WorkflowConstants.STATUS_ANY));

		List<MBMessage> childMessages =
			MBMessageLocalServiceUtil.getChildMessages(
				parentMessage.getMessageId(), WorkflowConstants.STATUS_ANY);

		Assert.assertEquals(childMessages.toString(), 2, childMessages.size());

		Assert.assertTrue(childMessages.contains(childMessage1));
		Assert.assertTrue(childMessages.contains(childMessage2));

		childMessages = MBMessageLocalServiceUtil.getChildMessages(
			parentMessage.getMessageId(), WorkflowConstants.STATUS_ANY, 0, 100);

		Assert.assertEquals(childMessages.toString(), 2, childMessages.size());

		Assert.assertTrue(childMessages.contains(childMessage1));
		Assert.assertTrue(childMessages.contains(childMessage2));
	}

	@Test
	public void testThreadLastPostDate() throws Exception {
		Date date = new Date();

		MBMessage parentMessage = addMessage(null, false, date);

		MBMessage firstReplyMessage = addMessage(
			parentMessage, false, new Date(date.getTime() + Time.SECOND));

		MBMessage secondReplyMessage = addMessage(
			parentMessage, false, new Date(date.getTime() + (Time.SECOND * 2)));

		DateFormat dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			PropsValues.INDEX_DATE_FORMAT_PATTERN);

		MBThread mbThread = parentMessage.getThread();

		Assert.assertEquals(
			dateFormat.format(mbThread.getLastPostDate()),
			dateFormat.format(secondReplyMessage.getModifiedDate()));

		MBMessageLocalServiceUtil.deleteMessage(
			secondReplyMessage.getMessageId());

		mbThread = parentMessage.getThread();

		Assert.assertEquals(
			dateFormat.format(mbThread.getLastPostDate()),
			dateFormat.format(firstReplyMessage.getModifiedDate()));
	}

	@Test
	public void testUpdateThreadAndMessageWhenBodyChanges() throws Exception {
		Date date = new Date();

		MBMessage message = addMessage(null, false, date);

		MBThread mbThread = message.getThread();

		Assert.assertEquals(mbThread.getModifiedDate(), date);

		MBMessageLocalServiceUtil.updateMessage(
			message.getUserId(), message.getMessageId(), message.getSubject(),
			RandomTestUtil.randomString(), Collections.emptyList(), 0, false,
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		mbThread = message.getThread();

		Assert.assertNotEquals(mbThread.getModifiedDate(), date);
	}

	@Test
	public void testUpdateThreadAndMessageWhenSubjectChanges()
		throws Exception {

		Date date = new Date();

		MBMessage message = addMessage(null, false, date);

		MBThread mbThread = message.getThread();

		Assert.assertEquals(mbThread.getModifiedDate(), date);

		MBMessageLocalServiceUtil.updateMessage(
			message.getUserId(), message.getMessageId(),
			RandomTestUtil.randomString(), message.getBody(),
			Collections.emptyList(), 0, false,
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		mbThread = message.getThread();

		Assert.assertNotEquals(mbThread.getModifiedDate(), date);
	}

	protected MBMessage addMessage() throws Exception {
		return addMessage(null, false);
	}

	protected MBMessage addMessage(
			MBMessage parentMessage, boolean addAttachments)
		throws Exception {

		return addMessage(
			parentMessage, addAttachments, new Date(),
			WorkflowConstants.ACTION_PUBLISH);
	}

	protected MBMessage addMessage(
			MBMessage parentMessage, boolean addAttachments, Date date)
		throws Exception {

		return addMessage(
			parentMessage, addAttachments, date,
			WorkflowConstants.ACTION_PUBLISH);
	}

	protected MBMessage addMessage(
			MBMessage parentMessage, boolean addAttachments, Date date,
			int workflowAction)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		serviceContext.setCreateDate(date);
		serviceContext.setModifiedDate(date);
		serviceContext.setWorkflowAction(workflowAction);

		long categoryId = MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID;
		long parentMessageId = MBMessageConstants.DEFAULT_PARENT_MESSAGE_ID;
		long threadId = 0;

		if (parentMessage != null) {
			categoryId = parentMessage.getCategoryId();
			parentMessageId = parentMessage.getMessageId();
			threadId = parentMessage.getThreadId();
		}

		List<ObjectValuePair<String, InputStream>> inputStreamOVPs =
			Collections.emptyList();

		if (addAttachments) {
			inputStreamOVPs = MBTestUtil.getInputStreamOVPs(
				"attachment.txt", getClass(), StringPool.BLANK);
		}

		return MBMessageLocalServiceUtil.addMessage(
			TestPropsValues.getUserId(), RandomTestUtil.randomString(),
			_group.getGroupId(), categoryId, threadId, parentMessageId,
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			MBMessageConstants.DEFAULT_FORMAT, inputStreamOVPs, false, 0.0,
			false, serviceContext);
	}

	protected MBMessage addMessage(MBMessage parentMessage, int workflowAction)
		throws Exception {

		return addMessage(parentMessage, false, new Date(), workflowAction);
	}

	@DeleteAfterTestRun
	private Group _group;

}