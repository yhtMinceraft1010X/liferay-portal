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

package com.liferay.document.library.internal.helper.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.model.DLVersionNumberIncrease;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.util.DLURLHelper;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.test.util.ConfigurationTemporarySwapper;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Alicia García
 */
@RunWith(Arquillian.class)
public class DLURLHelperTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
		_user = UserTestUtil.addUser();
	}

	@Test
	public void testGetDownloadURLAbsoluteURL() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					"com.liferay.document.library.configuration." +
						"FFFriendlyURLEntryFileEntryConfiguration",
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", false
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, _user.getUserId(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				(byte[])null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			String downloadURL = _dlURLHelper.getDownloadURL(
				fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
				StringPool.BLANK, false, true);

			Assert.assertTrue(
				downloadURL, downloadURL.startsWith("http://localhost:8080"));
		}
	}

	@Test
	public void testGetDownloadURLAbsoluteURLFriendlyURL() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					"com.liferay.document.library.configuration." +
						"FFFriendlyURLEntryFileEntryConfiguration",
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", true
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, _user.getUserId(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				(byte[])null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			String downloadURL = _dlURLHelper.getDownloadURL(
				fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
				StringPool.BLANK, false, true);

			Assert.assertTrue(
				downloadURL, downloadURL.startsWith("http://localhost:8080"));
		}
	}

	@Test
	public void testGetDownloadURLFriendlyURL() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					"com.liferay.document.library.configuration." +
						"FFFriendlyURLEntryFileEntryConfiguration",
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", true
					).build())) {

			String urlTitle = RandomTestUtil.randomString();

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, _user.getUserId(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				RandomTestUtil.randomString(), urlTitle,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				(byte[])null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			String downloadURL = _dlURLHelper.getDownloadURL(
				fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
				StringPool.BLANK, false, false);

			Assert.assertTrue(
				downloadURL,
				downloadURL.contains(StringUtil.toLowerCase(urlTitle)));
		}
	}

	@Test
	public void testGetDownloadURLNotAbsoluteURL() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					"com.liferay.document.library.configuration." +
						"FFFriendlyURLEntryFileEntryConfiguration",
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", false
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, _user.getUserId(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				(byte[])null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			String downloadURL = _dlURLHelper.getDownloadURL(
				fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
				StringPool.BLANK, false, false);

			Assert.assertFalse(
				downloadURL, downloadURL.startsWith("http://localhost:8080"));
		}
	}

	@Test
	public void testGetDownloadURLNotFriendlyURL() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					"com.liferay.document.library.configuration." +
						"FFFriendlyURLEntryFileEntryConfiguration",
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", false
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, _user.getUserId(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				(byte[])null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			String downloadURL = _dlURLHelper.getDownloadURL(
				fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
				StringPool.BLANK, false, false);

			Assert.assertTrue(
				downloadURL, downloadURL.contains(fileEntry.getUuid()));
		}
	}

	@Test
	public void testGetPreviewURLAbsoluteURL() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					"com.liferay.document.library.configuration." +
						"FFFriendlyURLEntryFileEntryConfiguration",
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", false
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, _user.getUserId(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				(byte[])null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			String previewURL = _dlURLHelper.getPreviewURL(
				fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
				StringPool.BLANK, false, true);

			Assert.assertTrue(
				previewURL, previewURL.startsWith("http://localhost:8080"));
		}
	}

	@Test
	public void testGetPreviewURLAbsoluteURLFriendlyURL() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					"com.liferay.document.library.configuration." +
						"FFFriendlyURLEntryFileEntryConfiguration",
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", true
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, _user.getUserId(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				(byte[])null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			String previewURL = _dlURLHelper.getPreviewURL(
				fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
				StringPool.BLANK, false, true);

			Assert.assertTrue(
				previewURL, previewURL.startsWith("http://localhost:8080"));
		}
	}

	@Test
	public void testGetPreviewURLFriendlyURL() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					"com.liferay.document.library.configuration." +
						"FFFriendlyURLEntryFileEntryConfiguration",
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", true
					).build())) {

			String urlTitle = RandomTestUtil.randomString();

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, _user.getUserId(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				RandomTestUtil.randomString(), urlTitle,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				(byte[])null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			String previewURL = _dlURLHelper.getPreviewURL(
				fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
				StringPool.BLANK, false, false);

			Assert.assertTrue(
				previewURL,
				previewURL.contains(StringUtil.toLowerCase(urlTitle)));
		}
	}

	@Test
	public void testGetPreviewURLNotAbsoluteURL() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					"com.liferay.document.library.configuration." +
						"FFFriendlyURLEntryFileEntryConfiguration",
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", false
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, _user.getUserId(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				(byte[])null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			String previewURL = _dlURLHelper.getPreviewURL(
				fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
				StringPool.BLANK, false, false);

			Assert.assertFalse(
				previewURL, previewURL.startsWith("http://localhost:8080"));
		}
	}

	@Test
	public void testGetPreviewURLNotFriendlyURL() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					"com.liferay.document.library.configuration." +
						"FFFriendlyURLEntryFileEntryConfiguration",
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", false
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, _user.getUserId(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				(byte[])null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			String previewURL = _dlURLHelper.getPreviewURL(
				fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
				StringPool.BLANK, false, false);

			Assert.assertTrue(
				previewURL, previewURL.contains(fileEntry.getUuid()));
		}
	}

	@Test
	public void testGetPreviewURLVersioned() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					"com.liferay.document.library.configuration." +
						"FFFriendlyURLEntryFileEntryConfiguration",
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", true
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, _user.getUserId(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				(byte[])null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			String previewURL = _dlURLHelper.getPreviewURL(
				fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
				StringPool.BLANK, true, false);

			Assert.assertTrue(previewURL, previewURL.contains("version="));
		}
	}

	@Test
	public void testGetPreviewURLVersionedOlwaysGivesUuidURL()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					"com.liferay.document.library.configuration." +
						"FFFriendlyURLEntryFileEntryConfiguration",
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", true
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, _user.getUserId(), _group.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				(byte[])null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			String previewURL = _dlURLHelper.getPreviewURL(
				fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
				StringPool.BLANK, true, false);

			Assert.assertTrue(
				previewURL, previewURL.contains(fileEntry.getUuid()));
		}
	}

	@Test
	public void testGetPreviewURLVersionUpdated() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _user.getUserId());

		FileEntry fileEntry = _dlAppLocalService.addFileEntry(
			null, _user.getUserId(), _group.getGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			RandomTestUtil.randomString(),
			ContentTypes.APPLICATION_OCTET_STREAM,
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			(byte[])null, null, null, serviceContext);

		fileEntry = _dlAppLocalService.updateFileEntry(
			_user.getUserId(), fileEntry.getFileEntryId(), StringPool.BLANK,
			ContentTypes.TEXT_PLAIN, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), StringPool.BLANK, null,
			DLVersionNumberIncrease.MAJOR, (byte[])null, null, null,
			serviceContext);

		String previewURL = _dlURLHelper.getPreviewURL(
			fileEntry, fileEntry.getFileVersion(), _getThemeDisplay(),
			StringPool.BLANK, true, false);

		Assert.assertTrue(previewURL, previewURL.contains("version=2"));
	}

	private ThemeDisplay _getThemeDisplay() {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setPortalURL("http://localhost:8080");
		themeDisplay.setRequest(new MockHttpServletRequest());
		themeDisplay.setScopeGroupId(_group.getGroupId());
		themeDisplay.setServerName("localhost");
		themeDisplay.setServerPort(8080);
		themeDisplay.setSiteGroupId(_group.getGroupId());
		themeDisplay.setUser(_user);

		return themeDisplay;
	}

	@Inject
	private DLAppLocalService _dlAppLocalService;

	@Inject
	private DLURLHelper _dlURLHelper;

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private User _user;

}