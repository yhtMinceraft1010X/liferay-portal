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

package com.liferay.document.library.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileEntryMetadata;
import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.document.library.kernel.model.DLFileShortcut;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.service.DLFileEntryTypeService;
import com.liferay.document.library.kernel.service.DLFolderService;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.document.library.kernel.util.comparator.RepositoryModelReadCountComparator;
import com.liferay.document.library.kernel.util.comparator.RepositoryModelTitleComparator;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileShortcut;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.view.count.ViewCountManager;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Samuel Trong Tran
 */
@RunWith(Arquillian.class)
public class DLFolderServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_parentFolder = _dlAppService.addFolder(
			_group.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			"Test Folder", RandomTestUtil.randomString(),
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		_ddmStructure = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), DLFileEntryMetadata.class.getName());

		_dlFileEntryType = _dlFileEntryTypeService.addFileEntryType(
			_group.getGroupId(), StringUtil.randomString(),
			StringUtil.randomString(),
			new long[] {_ddmStructure.getStructureId()},
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		_alternativeGroup = GroupTestUtil.addGroup();
	}

	@Test
	public void testShouldNotReturnContentOfFolderNotInGroup()
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		serviceContext.setAttribute(
			"fileEntryTypeId", _dlFileEntryType.getFileEntryTypeId());

		_dlAppService.addFileEntry(
			null, _group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString() + ".jpg", ContentTypes.IMAGE_JPEG,
			"title1", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);
		_dlAppService.addFileEntry(
			null, _group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title2", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		Assert.assertEquals(
			0,
			_dlFolderService.getFoldersAndFileEntriesAndFileShortcutsCount(
				_alternativeGroup.getGroupId(), _parentFolder.getFolderId(),
				WorkflowConstants.STATUS_APPROVED,
				ArrayUtil.toStringArray(DLUtil.getAllMediaGalleryMimeTypes()),
				false));
	}

	@Test
	public void testShouldReturnOnlyAcceptedMimeTypes() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		serviceContext.setAttribute(
			"fileEntryTypeId", _dlFileEntryType.getFileEntryTypeId());

		List<FileEntry> expectedFileEntries = new ArrayList<>();

		_dlAppService.addFileEntry(
			null, _group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title2", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);
		_dlAppService.addFileEntry(
			null, _group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title1", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		FileEntry fileEntry = _dlAppService.addFileEntry(
			null, _group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString() + ".jpg", ContentTypes.IMAGE_JPEG,
			"title3", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		expectedFileEntries.add(fileEntry);

		List<Object> actualFoldersAndFileEntriesAndFileShortcuts =
			_dlFolderService.getFoldersAndFileEntriesAndFileShortcuts(
				_group.getGroupId(), _parentFolder.getFolderId(),
				ArrayUtil.toStringArray(DLUtil.getAllMediaGalleryMimeTypes()),
				_dlFileEntryType.getFileEntryTypeId(), false,
				WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS,
				new RepositoryModelTitleComparator<FileEntry>(true));

		Assert.assertEquals(
			actualFoldersAndFileEntriesAndFileShortcuts.toString(), 1,
			actualFoldersAndFileEntriesAndFileShortcuts.size());

		Assert.assertEquals(
			actualFoldersAndFileEntriesAndFileShortcuts.toString(),
			expectedFileEntries.size(),
			actualFoldersAndFileEntriesAndFileShortcuts.size());

		FileEntry expectedFileEntry = expectedFileEntries.get(0);
		DLFileEntry actualDLFileEntry =
			(DLFileEntry)actualFoldersAndFileEntriesAndFileShortcuts.get(0);

		Assert.assertEquals(
			expectedFileEntry.getFileEntryId(),
			actualDLFileEntry.getFileEntryId());
	}

	@Test
	public void testShouldReturnOnlyAcceptedMimeTypesAndFolders()
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		List<FileEntry> expectedFileEntries = new ArrayList<>();

		FileEntry fileEntry = _dlAppService.addFileEntry(
			null, _group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString() + ".jpg", ContentTypes.IMAGE_JPEG,
			"title", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		expectedFileEntries.add(fileEntry);

		Folder folder = _dlAppService.addFolder(
			_group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString(), RandomTestUtil.randomString(),
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		_dlAppService.addFileEntry(
			null, _group.getGroupId(), folder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title2", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);
		_dlAppService.addFileEntry(
			null, _group.getGroupId(), folder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title1", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);
		_dlAppService.addFileEntry(
			null, _group.getGroupId(), folder.getFolderId(),
			StringUtil.randomString() + ".jpg", ContentTypes.IMAGE_JPEG,
			"title3", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		List<Object> actualFoldersAndFileEntriesAndFileShortcuts =
			_dlFolderService.getFoldersAndFileEntriesAndFileShortcuts(
				_group.getGroupId(), _parentFolder.getFolderId(),
				ArrayUtil.toStringArray(DLUtil.getAllMediaGalleryMimeTypes()),
				false, WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS,
				new RepositoryModelTitleComparator<FileEntry>(true));

		Assert.assertEquals(
			actualFoldersAndFileEntriesAndFileShortcuts.toString(), 2,
			actualFoldersAndFileEntriesAndFileShortcuts.size());

		List<DLFileEntry> actualDLFileEntries = new ArrayList<>();
		int actualDLFoldersCount = 0;

		for (Object actualFoldersAndFileEntriesAndFileShortcut :
				actualFoldersAndFileEntriesAndFileShortcuts) {

			if (actualFoldersAndFileEntriesAndFileShortcut instanceof
					DLFileEntry) {

				actualDLFileEntries.add(
					(DLFileEntry)actualFoldersAndFileEntriesAndFileShortcut);
			}
			else if (actualFoldersAndFileEntriesAndFileShortcut instanceof
						DLFolder) {

				actualDLFoldersCount++;
			}
		}

		Assert.assertEquals(actualDLFoldersCount, 1, actualDLFoldersCount);

		Assert.assertEquals(
			actualDLFileEntries.size(), 1, actualDLFileEntries.size());

		Assert.assertEquals(
			expectedFileEntries.toString(), expectedFileEntries.size(),
			actualDLFileEntries.size());

		FileEntry expectedFileEntry = expectedFileEntries.get(0);
		DLFileEntry actualDLFileEntry = actualDLFileEntries.get(0);

		Assert.assertEquals(
			expectedFileEntry.getFileEntryId(),
			actualDLFileEntry.getFileEntryId());
	}

	@Test
	public void testShouldReturnOrderedByReadCount() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		serviceContext.setAttribute(
			"fileEntryTypeId", _dlFileEntryType.getFileEntryTypeId());

		List<Object> expectedFoldersAndFileEntriesAndFileShortcuts =
			new ArrayList<>();

		FileEntry fileEntry1 = _dlAppService.addFileEntry(
			null, _group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title1", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		_viewCountManager.incrementViewCount(
			fileEntry1.getCompanyId(),
			_classNameLocalService.getClassNameId(DLFileEntry.class),
			fileEntry1.getFileEntryId(), 2);

		FileEntry fileEntry2 = _dlAppService.addFileEntry(
			null, _group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title2", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		_viewCountManager.incrementViewCount(
			fileEntry2.getCompanyId(),
			_classNameLocalService.getClassNameId(DLFileEntry.class),
			fileEntry2.getFileEntryId(), 1);

		FileEntry fileEntry3 = _dlAppService.addFileEntry(
			null, _group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title3", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		_viewCountManager.incrementViewCount(
			fileEntry3.getCompanyId(),
			_classNameLocalService.getClassNameId(DLFileEntry.class),
			fileEntry3.getFileEntryId(), 4);

		Folder hiddenFolder = _dlAppService.addFolder(
			_group.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			"Hidden Folder", RandomTestUtil.randomString(),
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		FileEntry fileEntry4 = _dlAppService.addFileEntry(
			null, _group.getGroupId(), hiddenFolder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title4", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		_viewCountManager.incrementViewCount(
			fileEntry4.getCompanyId(),
			_classNameLocalService.getClassNameId(DLFileEntry.class),
			fileEntry4.getFileEntryId(), 5);

		FileShortcut fileShortcut1 = _dlAppService.addFileShortcut(
			_group.getGroupId(), _parentFolder.getFolderId(),
			fileEntry4.getFileEntryId(), serviceContext);

		FileEntry fileEntry5 = _dlAppService.addFileEntry(
			null, _group.getGroupId(), hiddenFolder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title5", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		_viewCountManager.incrementViewCount(
			fileEntry5.getCompanyId(),
			_classNameLocalService.getClassNameId(DLFileEntry.class),
			fileEntry5.getFileEntryId(), 3);

		FileShortcut fileShortcut2 = _dlAppService.addFileShortcut(
			_group.getGroupId(), _parentFolder.getFolderId(),
			fileEntry5.getFileEntryId(), serviceContext);

		expectedFoldersAndFileEntriesAndFileShortcuts.add(fileEntry2);
		expectedFoldersAndFileEntriesAndFileShortcuts.add(fileEntry1);
		expectedFoldersAndFileEntriesAndFileShortcuts.add(fileShortcut2);
		expectedFoldersAndFileEntriesAndFileShortcuts.add(fileEntry3);
		expectedFoldersAndFileEntriesAndFileShortcuts.add(fileShortcut1);

		List<Object> actualFoldersAndFileEntriesAndFileShortcuts =
			_dlFolderService.getFoldersAndFileEntriesAndFileShortcuts(
				_group.getGroupId(), _parentFolder.getFolderId(), null,
				_dlFileEntryType.getFileEntryTypeId(), false,
				WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS,
				new RepositoryModelReadCountComparator<>(true));

		Assert.assertEquals(
			actualFoldersAndFileEntriesAndFileShortcuts.toString(), 5,
			actualFoldersAndFileEntriesAndFileShortcuts.size());

		for (int i = 0; i < 5; i++) {
			if (expectedFoldersAndFileEntriesAndFileShortcuts.get(i) instanceof
					FileEntry) {

				FileEntry expectedFileEntry =
					(FileEntry)
						expectedFoldersAndFileEntriesAndFileShortcuts.get(i);
				DLFileEntry actualDLFileEntry =
					(DLFileEntry)
						actualFoldersAndFileEntriesAndFileShortcuts.get(i);

				Assert.assertEquals(
					expectedFileEntry.getFileEntryId(),
					actualDLFileEntry.getFileEntryId());
			}
			else {
				DLFileShortcut actualDLFileShortcut =
					(DLFileShortcut)
						actualFoldersAndFileEntriesAndFileShortcuts.get(i);
				FileShortcut expectedFileShortcut =
					(FileShortcut)
						expectedFoldersAndFileEntriesAndFileShortcuts.get(i);

				Assert.assertEquals(
					expectedFileShortcut.getFileShortcutId(),
					actualDLFileShortcut.getFileShortcutId());
			}
		}
	}

	@Test
	public void testShouldReturnOrderedByTitle() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		serviceContext.setAttribute(
			"fileEntryTypeId", _dlFileEntryType.getFileEntryTypeId());

		List<FileEntry> expectedFileEntries = new ArrayList<>();

		FileEntry fileEntry1 = _dlAppService.addFileEntry(
			null, _group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title2", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		FileEntry fileEntry2 = _dlAppService.addFileEntry(
			null, _group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title1", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		expectedFileEntries.add(fileEntry2);

		expectedFileEntries.add(fileEntry1);

		FileEntry fileEntry3 = _dlAppService.addFileEntry(
			null, _group.getGroupId(), _parentFolder.getFolderId(),
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			"title3", StringUtil.randomString(), StringUtil.randomString(),
			StringPool.BLANK, (byte[])null, null, null, serviceContext);

		expectedFileEntries.add(fileEntry3);

		List<Object> actualFoldersAndFileEntriesAndFileShortcuts =
			_dlFolderService.getFoldersAndFileEntriesAndFileShortcuts(
				_group.getGroupId(), _parentFolder.getFolderId(), null,
				_dlFileEntryType.getFileEntryTypeId(), false,
				WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS,
				new RepositoryModelTitleComparator<FileEntry>(true));

		Assert.assertEquals(
			actualFoldersAndFileEntriesAndFileShortcuts.toString(), 3,
			actualFoldersAndFileEntriesAndFileShortcuts.size());

		for (int i = 0; i < 3; i++) {
			FileEntry expectedFileEntry = expectedFileEntries.get(i);
			DLFileEntry actualDLFileEntry =
				(DLFileEntry)actualFoldersAndFileEntriesAndFileShortcuts.get(i);

			Assert.assertEquals(
				expectedFileEntry.getFileEntryId(),
				actualDLFileEntry.getFileEntryId());
		}
	}

	@Inject
	private static ClassNameLocalService _classNameLocalService;

	@Inject
	private static DLAppService _dlAppService;

	@Inject
	private static DLFileEntryTypeService _dlFileEntryTypeService;

	@Inject
	private static DLFolderService _dlFolderService;

	@Inject
	private static ViewCountManager _viewCountManager;

	@DeleteAfterTestRun
	private Group _alternativeGroup;

	@DeleteAfterTestRun
	private DDMStructure _ddmStructure;

	@DeleteAfterTestRun
	private DLFileEntryType _dlFileEntryType;

	@DeleteAfterTestRun
	private Group _group;

	private Folder _parentFolder;

}