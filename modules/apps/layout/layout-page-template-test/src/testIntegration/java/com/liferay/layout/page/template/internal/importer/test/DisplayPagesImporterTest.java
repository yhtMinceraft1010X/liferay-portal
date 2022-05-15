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

package com.liferay.layout.page.template.internal.importer.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.layout.page.template.constants.LayoutPageTemplateExportImportConstants;
import com.liferay.layout.page.template.importer.LayoutPageTemplatesImporter;
import com.liferay.layout.page.template.importer.LayoutPageTemplatesImporterResultEntry;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.util.structure.CollectionStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactoryUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Rubén Pulido
 */
@RunWith(Arquillian.class)
public class DisplayPagesImporterTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_bundle = FrameworkUtil.getBundle(getClass());

		_group = GroupTestUtil.addGroup();

		_user = TestPropsValues.getUser();
	}

	@Test
	public void testImportDisplayPage() throws Exception {
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_importLayoutPageTemplateEntry("display-page-template-one");

		String className =
			"com.liferay.portal.kernel.repository.model.FileEntry";

		Assert.assertEquals(className, layoutPageTemplateEntry.getClassName());

		Assert.assertEquals(
			"Display Page Template One", layoutPageTemplateEntry.getName());

		Assert.assertEquals(0, layoutPageTemplateEntry.getClassTypeId());

		_validateLayoutPageTemplateStructure(
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(
					_group.getGroupId(), layoutPageTemplateEntry.getPlid()));
	}

	@Test
	public void testImportDisplayPageExistingNameNoOvewrite() throws Exception {
		String testCaseName = "display-page-template-one";

		_importLayoutPageTemplateEntry(testCaseName);

		List<LayoutPageTemplatesImporterResultEntry>
			layoutPageTemplatesImporterResultEntries =
				_getLayoutPageTemplatesImporterResultEntries(testCaseName);

		LayoutPageTemplatesImporterResultEntry
			layoutPageTemplatesImporterResultEntry =
				layoutPageTemplatesImporterResultEntries.get(0);

		Assert.assertEquals(
			LayoutPageTemplatesImporterResultEntry.Status.IGNORED,
			layoutPageTemplatesImporterResultEntry.getStatus());
		Assert.assertEquals(
			String.format(
				"%s/display-page-templates/%s/display-page-template.json was " +
					"ignored because a display page template with the same " +
						"key already exists.",
				testCaseName, testCaseName),
			layoutPageTemplatesImporterResultEntry.getErrorMessage());
	}

	@Test
	public void testImportDisplayPages() throws Exception {
		List<LayoutPageTemplatesImporterResultEntry>
			layoutPageTemplatesImporterResultEntries =
				_getLayoutPageTemplatesImporterResultEntries(
					"display-page-template-multiple");

		Assert.assertEquals(
			layoutPageTemplatesImporterResultEntries.toString(), 2,
			layoutPageTemplatesImporterResultEntries.size());

		LayoutPageTemplateEntry layoutPageTemplateEntry1 =
			_getLayoutPageTemplateEntry(
				layoutPageTemplatesImporterResultEntries, 0);
		LayoutPageTemplateEntry layoutPageTemplateEntry2 =
			_getLayoutPageTemplateEntry(
				layoutPageTemplatesImporterResultEntries, 1);

		List<String> actualLayoutPageTemplateEntryNames = ListUtil.sort(
			new ArrayList() {
				{
					add(layoutPageTemplateEntry1.getName());
					add(layoutPageTemplateEntry2.getName());
				}
			});

		Assert.assertArrayEquals(
			new String[] {
				"Display Page Template One", "Display Page Template Two"
			},
			actualLayoutPageTemplateEntryNames.toArray(new String[0]));
	}

	@Test
	public void testImportDisplayPageTemplateCollection() throws Exception {
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_importLayoutPageTemplateEntry("display-page-template-collection");

		Assert.assertEquals(
			"com.liferay.portal.kernel.repository.model.FileEntry",
			layoutPageTemplateEntry.getClassName());

		Assert.assertEquals(
			"Display Page Template Collection",
			layoutPageTemplateEntry.getName());

		Assert.assertEquals(0, layoutPageTemplateEntry.getClassTypeId());

		LayoutPageTemplateStructure layoutPageTemplateStructure =
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(
					_group.getGroupId(), layoutPageTemplateEntry.getPlid());

		Assert.assertNotNull(layoutPageTemplateStructure);

		LayoutStructure layoutStructure = LayoutStructure.of(
			layoutPageTemplateStructure.getDefaultSegmentsExperienceData());

		LayoutStructureItem mainLayoutStructureItem =
			layoutStructure.getMainLayoutStructureItem();

		List<String> childrenItemIds =
			mainLayoutStructureItem.getChildrenItemIds();

		Assert.assertEquals(
			childrenItemIds.toString(), 1, childrenItemIds.size());

		String childItemId = childrenItemIds.get(0);

		LayoutStructureItem layoutStructureItem =
			layoutStructure.getLayoutStructureItem(childItemId);

		Assert.assertTrue(
			layoutStructureItem instanceof CollectionStyledLayoutStructureItem);

		CollectionStyledLayoutStructureItem
			collectionStyledLayoutStructureItem =
				(CollectionStyledLayoutStructureItem)layoutStructureItem;

		Assert.assertNotNull(collectionStyledLayoutStructureItem);

		JSONObject collectionJSONObject =
			collectionStyledLayoutStructureItem.getCollectionJSONObject();

		Assert.assertNotNull(collectionJSONObject);
		Assert.assertEquals(
			"com.liferay.asset.kernel.model.AssetCategory",
			collectionJSONObject.getString("itemType"));
		Assert.assertEquals(
			"com.liferay.asset.categories.admin.web.internal.info.collection." +
				"provider.AssetCategoriesForAssetEntryRelatedInfoItem" +
					"CollectionProvider",
			collectionJSONObject.getString("key"));
	}

	private void _addZipWriterEntry(ZipWriter zipWriter, URL url)
		throws IOException {

		String entryPath = url.getPath();

		String zipPath = StringUtil.removeSubstring(entryPath, _BASE_PATH);

		zipWriter.addEntry(zipPath, url.openStream());
	}

	private File _generateZipFile(String testCaseName) throws Exception {
		ZipWriter zipWriter = ZipWriterFactoryUtil.getZipWriter();

		Enumeration<URL> enumeration = _bundle.findEntries(
			StringBundler.concat(
				_BASE_PATH + testCaseName,
				StringPool.FORWARD_SLASH + _ROOT_FOLDER,
				StringPool.FORWARD_SLASH),
			LayoutPageTemplateExportImportConstants.
				FILE_NAME_DISPLAY_PAGE_TEMPLATE,
			true);

		try {
			while (enumeration.hasMoreElements()) {
				URL url = enumeration.nextElement();

				_populateZipWriter(zipWriter, url);
			}

			return zipWriter.getFile();
		}
		catch (Exception exception) {
			throw new Exception(exception);
		}
	}

	private LayoutPageTemplateEntry _getLayoutPageTemplateEntry(
		List<LayoutPageTemplatesImporterResultEntry>
			layoutPageTemplatesImporterResultEntries,
		int index) {

		LayoutPageTemplatesImporterResultEntry
			layoutPageTemplatesImporterResultEntry =
				layoutPageTemplatesImporterResultEntries.get(index);

		Assert.assertEquals(
			LayoutPageTemplatesImporterResultEntry.Status.IMPORTED,
			layoutPageTemplatesImporterResultEntry.getStatus());

		String layoutPageTemplateEntryKey = StringUtil.toLowerCase(
			layoutPageTemplatesImporterResultEntry.getName());

		layoutPageTemplateEntryKey = StringUtil.replace(
			layoutPageTemplateEntryKey, CharPool.SPACE, CharPool.DASH);

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_layoutPageTemplateEntryLocalService.fetchLayoutPageTemplateEntry(
				_group.getGroupId(), layoutPageTemplateEntryKey);

		Assert.assertNotNull(layoutPageTemplateEntry);

		return layoutPageTemplateEntry;
	}

	private List<LayoutPageTemplatesImporterResultEntry>
			_getLayoutPageTemplatesImporterResultEntries(String testCaseName)
		throws Exception {

		File file = _generateZipFile(testCaseName);

		List<LayoutPageTemplatesImporterResultEntry>
			layoutPageTemplatesImporterResultEntries = null;

		ServiceContextThreadLocal.pushServiceContext(
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		try {
			layoutPageTemplatesImporterResultEntries =
				_layoutPageTemplatesImporter.importFile(
					_user.getUserId(), _group.getGroupId(), 0, file, false);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		Assert.assertNotNull(layoutPageTemplatesImporterResultEntries);

		return layoutPageTemplatesImporterResultEntries;
	}

	private LayoutPageTemplateEntry _importLayoutPageTemplateEntry(
			String testCaseName)
		throws Exception {

		List<LayoutPageTemplatesImporterResultEntry>
			layoutPageTemplatesImporterResultEntries =
				_getLayoutPageTemplatesImporterResultEntries(testCaseName);

		Assert.assertEquals(
			layoutPageTemplatesImporterResultEntries.toString(), 1,
			layoutPageTemplatesImporterResultEntries.size());

		return _getLayoutPageTemplateEntry(
			layoutPageTemplatesImporterResultEntries, 0);
	}

	private void _populateZipWriter(ZipWriter zipWriter, URL url)
		throws IOException {

		String zipPath = StringUtil.removeSubstring(url.getFile(), _BASE_PATH);

		zipWriter.addEntry(zipPath, url.openStream());

		String path = FileUtil.getPath(url.getPath());

		Enumeration<URL> enumeration = _bundle.findEntries(
			path,
			LayoutPageTemplateExportImportConstants.
				FILE_NAME_DISPLAY_PAGE_TEMPLATE,
			true);

		while (enumeration.hasMoreElements()) {
			URL elementURL = enumeration.nextElement();

			_addZipWriterEntry(zipWriter, elementURL);
		}

		enumeration = _bundle.findEntries(
			path,
			LayoutPageTemplateExportImportConstants.FILE_NAME_PAGE_DEFINITION,
			true);

		while (enumeration.hasMoreElements()) {
			URL elementURL = enumeration.nextElement();

			_addZipWriterEntry(zipWriter, elementURL);
		}
	}

	private void _validateLayoutPageTemplateStructure(
		LayoutPageTemplateStructure layoutPageTemplateStructure) {

		Assert.assertNotNull(layoutPageTemplateStructure);

		LayoutStructure layoutStructure = LayoutStructure.of(
			layoutPageTemplateStructure.getDefaultSegmentsExperienceData());

		Assert.assertNotNull(layoutStructure.getMainLayoutStructureItem());
	}

	private static final String _BASE_PATH =
		"com/liferay/layout/page/template/internal/importer/test/dependencies/";

	private static final String _ROOT_FOLDER = "display-page-templates";

	private Bundle _bundle;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Inject
	private LayoutPageTemplatesImporter _layoutPageTemplatesImporter;

	@Inject
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	private User _user;

}