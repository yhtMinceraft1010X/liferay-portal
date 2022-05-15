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
import com.liferay.fragment.contributor.FragmentCollectionContributor;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.service.FragmentEntryLocalServiceUtil;
import com.liferay.layout.page.template.constants.LayoutPageTemplateExportImportConstants;
import com.liferay.layout.page.template.importer.LayoutPageTemplatesImporter;
import com.liferay.layout.page.template.importer.LayoutPageTemplatesImporterResultEntry;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.util.structure.DropZoneLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Rubén Pulido
 */
@RunWith(Arquillian.class)
public class MasterLayoutsImporterTest {

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

		Bundle bundle = FrameworkUtil.getBundle(
			MasterLayoutsImporterTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		_serviceRegistration = bundleContext.registerService(
			FragmentCollectionContributor.class,
			new TestMasterPageFragmentCollectionContributor(), null);
	}

	@After
	public void tearDown() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testImportMasterLayoutDropZone() throws Exception {
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_importLayoutPageTemplateEntry("master-page-drop-zone");

		Assert.assertEquals(
			"Master Page Drop Zone", layoutPageTemplateEntry.getName());

		_validateLayoutPageTemplateStructureDropZone(
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(
					_group.getGroupId(), layoutPageTemplateEntry.getPlid()),
			new ArrayList<>(), true);
	}

	@Test
	public void testImportMasterLayoutDropZoneAllowedFragments()
		throws Exception {

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_importLayoutPageTemplateEntry(
				"master-page-drop-zone-allowed-fragments");

		Assert.assertEquals(
			"Master Page Drop Zone Allowed Fragments",
			layoutPageTemplateEntry.getName());

		_validateLayoutPageTemplateStructureDropZone(
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(
					_group.getGroupId(), layoutPageTemplateEntry.getPlid()),
			Arrays.asList(
				TestMasterPageFragmentCollectionContributor.
					TEST_MASTER_PAGE_FRAGMENT_COLLECTION_KEY,
				TestMasterPageFragmentCollectionContributor.
					TEST_MASTER_PAGE_FRAGMENT_COLLECTION_KEY + StringPool.DASH +
						TestMasterPageFragmentCollectionContributor.
							TEST_MASTER_PAGE_FRAGMENT_ENTRY_1,
				TestMasterPageFragmentCollectionContributor.
					TEST_MASTER_PAGE_FRAGMENT_COLLECTION_KEY + StringPool.DASH +
						TestMasterPageFragmentCollectionContributor.
							TEST_MASTER_PAGE_FRAGMENT_ENTRY_2),
			false);
	}

	@Test
	public void testImportMasterLayoutDropZoneUnallowedFragments()
		throws Exception {

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_importLayoutPageTemplateEntry(
				"master-page-drop-zone-unallowed-fragments");

		Assert.assertEquals(
			"Master Page Drop Zone Unallowed Fragments",
			layoutPageTemplateEntry.getName());

		_validateLayoutPageTemplateStructureDropZone(
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(
					_group.getGroupId(), layoutPageTemplateEntry.getPlid()),
			Arrays.asList(
				TestMasterPageFragmentCollectionContributor.
					TEST_MASTER_PAGE_FRAGMENT_COLLECTION_KEY,
				TestMasterPageFragmentCollectionContributor.
					TEST_MASTER_PAGE_FRAGMENT_COLLECTION_KEY + StringPool.DASH +
						TestMasterPageFragmentCollectionContributor.
							TEST_MASTER_PAGE_FRAGMENT_ENTRY_1,
				TestMasterPageFragmentCollectionContributor.
					TEST_MASTER_PAGE_FRAGMENT_COLLECTION_KEY + StringPool.DASH +
						TestMasterPageFragmentCollectionContributor.
							TEST_MASTER_PAGE_FRAGMENT_ENTRY_2),
			true);
	}

	@Test
	public void testImportMasterLayoutExistingNameNoOvewrite()
		throws Exception {

		String testCaseName = "master-page-drop-zone-allowed-fragments";

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
				"%s/master-pages/%s/master-page.json was ignored because a " +
					"master page with the same key already exists.",
				testCaseName, testCaseName),
			layoutPageTemplatesImporterResultEntry.getErrorMessage());
	}

	@Test
	public void testImportMasterLayouts() throws Exception {
		List<LayoutPageTemplatesImporterResultEntry>
			layoutPageTemplatesImporterResultEntries =
				_getLayoutPageTemplatesImporterResultEntries(
					"master-page-multiple");

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
			new String[] {"Master Page One", "Master Page Two"},
			actualLayoutPageTemplateEntryNames.toArray(new String[0]));
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
			LayoutPageTemplateExportImportConstants.FILE_NAME_MASTER_PAGE,
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
			path, LayoutPageTemplateExportImportConstants.FILE_NAME_MASTER_PAGE,
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

	private void _validateLayoutPageTemplateStructureDropZone(
		LayoutPageTemplateStructure layoutPageTemplateStructure,
		List<String> expectedFragmentEntryKeys,
		boolean expectedIsAllowNewFragments) {

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
			layoutStructureItem instanceof DropZoneLayoutStructureItem);

		DropZoneLayoutStructureItem dropZoneLayoutStructureItem =
			(DropZoneLayoutStructureItem)layoutStructureItem;

		Assert.assertNotNull(layoutStructure);

		Assert.assertTrue(
			expectedFragmentEntryKeys.containsAll(
				dropZoneLayoutStructureItem.getFragmentEntryKeys()));

		Assert.assertEquals(
			expectedIsAllowNewFragments,
			dropZoneLayoutStructureItem.isAllowNewFragmentEntries());
	}

	private static final String _BASE_PATH =
		"com/liferay/layout/page/template/internal/importer/test/dependencies/";

	private static final String _ROOT_FOLDER = "master-pages";

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

	private ServiceRegistration<FragmentCollectionContributor>
		_serviceRegistration;
	private User _user;

	private static class TestMasterPageFragmentCollectionContributor
		implements FragmentCollectionContributor {

		public static final String TEST_MASTER_PAGE_FRAGMENT_COLLECTION_KEY =
			"test-master-page-fragment-collection-contributor";

		public static final String TEST_MASTER_PAGE_FRAGMENT_ENTRY_1 =
			"fragment-entry-1";

		public static final String TEST_MASTER_PAGE_FRAGMENT_ENTRY_2 =
			"fragment-entry-2";

		@Override
		public String getFragmentCollectionKey() {
			return TEST_MASTER_PAGE_FRAGMENT_COLLECTION_KEY;
		}

		@Override
		public List<FragmentEntry> getFragmentEntries() {
			return ListUtil.fromArray(
				_getFragmentEntry(TEST_MASTER_PAGE_FRAGMENT_ENTRY_1, 0),
				_getFragmentEntry(TEST_MASTER_PAGE_FRAGMENT_ENTRY_2, 0));
		}

		@Override
		public List<FragmentEntry> getFragmentEntries(int type) {
			return getFragmentEntries();
		}

		@Override
		public List<FragmentEntry> getFragmentEntries(Locale locale) {
			return Collections.emptyList();
		}

		@Override
		public String getName() {
			return "Test Master Page Fragment Collection Contributor";
		}

		@Override
		public Map<Locale, String> getNames() {
			return HashMapBuilder.put(
				LocaleUtil.getSiteDefault(), getName()
			).build();
		}

		private FragmentEntry _getFragmentEntry(String key, int type) {
			FragmentEntry fragmentEntry =
				FragmentEntryLocalServiceUtil.createFragmentEntry(0L);

			fragmentEntry.setFragmentEntryKey(key);
			fragmentEntry.setName(RandomTestUtil.randomString());
			fragmentEntry.setCss(null);
			fragmentEntry.setHtml(RandomTestUtil.randomString());
			fragmentEntry.setJs(null);
			fragmentEntry.setConfiguration(null);
			fragmentEntry.setType(type);

			return fragmentEntry;
		}

	}

}