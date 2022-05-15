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

package com.liferay.exportimport.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.exportimport.kernel.exception.LARTypeException;
import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.PortletDataHandlerKeys;
import com.liferay.exportimport.test.util.lar.BaseExportImportTestCase;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutPrototype;
import com.liferay.portal.kernel.model.LayoutSetPrototype;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutSetPrototypeLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eduardo García
 */
@RunWith(Arquillian.class)
public class LayoutExportImportTest extends BaseExportImportTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		UserTestUtil.setUser(TestPropsValues.getUser());
	}

	@Test
	public void testDeleteMissingLayouts() throws Exception {
		Layout layout1 = LayoutTestUtil.addTypePortletLayout(group);
		Layout layout2 = LayoutTestUtil.addTypePortletLayout(group);

		long[] layoutIds = ExportImportHelperUtil.getLayoutIds(
			LayoutLocalServiceUtil.getLayouts(group.getGroupId(), false));

		exportImportLayouts(layoutIds, getImportParameterMap());

		Assert.assertEquals(
			LayoutLocalServiceUtil.getLayoutsCount(group, false),
			LayoutLocalServiceUtil.getLayoutsCount(importedGroup, false));

		LayoutTestUtil.addTypePortletLayout(importedGroup);

		Map<String, String[]> parameterMap = getImportParameterMap();

		parameterMap.put(
			PortletDataHandlerKeys.DELETE_MISSING_LAYOUTS,
			new String[] {Boolean.TRUE.toString()});

		layoutIds = new long[] {layout1.getLayoutId()};

		exportImportLayouts(layoutIds, getImportParameterMap());

		Assert.assertEquals(
			LayoutLocalServiceUtil.getLayoutsCount(group, false),
			LayoutLocalServiceUtil.getLayoutsCount(importedGroup, false));

		Layout importedLayout1 =
			LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
				layout1.getUuid(), importedGroup.getGroupId(), false);

		Assert.assertNotNull(importedLayout1);

		Layout importedLayout2 =
			LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
				layout2.getUuid(), importedGroup.getGroupId(), false);

		Assert.assertNotNull(importedLayout2);
	}

	@Test
	public void testExportImportCompanyGroupInvalidLARType() throws Exception {

		// Import a layout set to a company layout set

		Group originalImportedGroup = importedGroup;
		Group originalGroup = group;

		Company company = CompanyLocalServiceUtil.getCompany(
			TestPropsValues.getCompanyId());

		importedGroup = company.getGroup();

		long[] layoutIds = new long[0];

		try {
			exportImportLayouts(layoutIds, getImportParameterMap(), true);

			Assert.fail();
		}
		catch (LARTypeException larTypeException) {
			if (_log.isDebugEnabled()) {
				_log.debug(larTypeException);
			}
		}
		finally {
			importedGroup = originalImportedGroup;
		}

		// Import a company layout set to a layout set

		group = company.getGroup();
		importedGroup = originalGroup;

		try {
			exportImportLayouts(layoutIds, getImportParameterMap(), true);

			Assert.fail();
		}
		catch (LARTypeException larTypeException) {
			if (_log.isDebugEnabled()) {
				_log.debug(larTypeException);
			}
		}
		finally {
			importedGroup = originalImportedGroup;
			group = originalGroup;
		}
	}

	@Test
	public void testExportImportLayoutPrototypeInvalidLARType()
		throws Exception {

		// Import a layout prototype to a layout set

		LayoutPrototype layoutPrototype = LayoutTestUtil.addLayoutPrototype(
			RandomTestUtil.randomString());

		group = layoutPrototype.getGroup();

		importedGroup = GroupTestUtil.addGroup();

		long[] layoutIds = new long[0];

		try {
			exportImportLayouts(layoutIds, getImportParameterMap(), true);

			Assert.fail();
		}
		catch (LARTypeException larTypeException) {
			if (_log.isDebugEnabled()) {
				_log.debug(larTypeException);
			}
		}

		// Import a layout prototype to a layout set pototype

		LayoutSetPrototype layoutSetPrototype =
			LayoutTestUtil.addLayoutSetPrototype(RandomTestUtil.randomString());

		importedGroup = layoutSetPrototype.getGroup();

		try {
			exportImportLayouts(layoutIds, getImportParameterMap(), true);

			Assert.fail();
		}
		catch (LARTypeException larTypeException) {
			if (_log.isDebugEnabled()) {
				_log.debug(larTypeException);
			}
		}
		finally {
			LayoutSetPrototypeLocalServiceUtil.deleteLayoutSetPrototype(
				layoutSetPrototype);

			importedGroup = null;
		}
	}

	@Test
	public void testExportImportLayouts() throws Exception {
		LayoutTestUtil.addTypePortletLayout(group);

		exportImportLayouts(
			ExportImportHelperUtil.getLayoutIds(
				LayoutLocalServiceUtil.getLayouts(group.getGroupId(), false)),
			getImportParameterMap());

		Assert.assertEquals(
			LayoutLocalServiceUtil.getLayoutsCount(group, false),
			LayoutLocalServiceUtil.getLayoutsCount(importedGroup, false));
	}

	@Test
	public void testExportImportLayoutSetInvalidLARType() throws Exception {

		// Import a layout set to a layout prototype

		LayoutPrototype layoutPrototype = LayoutTestUtil.addLayoutPrototype(
			RandomTestUtil.randomString());

		importedGroup = layoutPrototype.getGroup();

		long[] layoutIds = new long[0];

		try {
			exportImportLayouts(layoutIds, getImportParameterMap(), true);

			Assert.fail();
		}
		catch (LARTypeException larTypeException) {
			if (_log.isDebugEnabled()) {
				_log.debug(larTypeException);
			}
		}

		// Import a layout set to a layout set prototype

		LayoutSetPrototype layoutSetPrototype =
			LayoutTestUtil.addLayoutSetPrototype(RandomTestUtil.randomString());

		importedGroup = layoutSetPrototype.getGroup();

		try {
			exportImportLayouts(layoutIds, getImportParameterMap(), true);

			Assert.fail();
		}
		catch (LARTypeException larTypeException) {
			if (_log.isDebugEnabled()) {
				_log.debug(larTypeException);
			}
		}
		finally {
			LayoutSetPrototypeLocalServiceUtil.deleteLayoutSetPrototype(
				layoutSetPrototype);

			importedGroup = null;
		}
	}

	@Test
	public void testExportImportLayoutSetPrototypeInvalidLARType()
		throws Exception {

		// Import a layout set prototype to a layout set

		LayoutSetPrototype layoutSetPrototype =
			LayoutTestUtil.addLayoutSetPrototype(RandomTestUtil.randomString());

		try {
			group = layoutSetPrototype.getGroup();
			importedGroup = GroupTestUtil.addGroup();

			long[] layoutIds = new long[0];

			try {
				exportImportLayouts(layoutIds, getImportParameterMap(), true);

				Assert.fail();
			}
			catch (LARTypeException larTypeException) {
				if (_log.isDebugEnabled()) {
					_log.debug(larTypeException);
				}
			}

			// Import a layout set prototype to a layout prototyope

			LayoutPrototype layoutPrototype = LayoutTestUtil.addLayoutPrototype(
				RandomTestUtil.randomString());

			importedGroup = layoutPrototype.getGroup();

			try {
				exportImportLayouts(layoutIds, getImportParameterMap(), true);

				Assert.fail();
			}
			catch (LARTypeException larTypeException) {
				if (_log.isDebugEnabled()) {
					_log.debug(larTypeException);
				}
			}
		}
		finally {
			LayoutSetPrototypeLocalServiceUtil.deleteLayoutSetPrototype(
				layoutSetPrototype);

			group = null;
		}
	}

	@Test
	public void testExportImportLayoutsInvalidAvailableLocales()
		throws Exception {

		testAvailableLocales(
			Arrays.asList(LocaleUtil.US, LocaleUtil.SPAIN),
			Arrays.asList(LocaleUtil.US, LocaleUtil.GERMANY), true);
	}

	@Test
	public void testExportImportLayoutsPriorities() throws Exception {
		Layout layout1 = LayoutTestUtil.addTypePortletLayout(group);
		Layout layout2 = LayoutTestUtil.addTypePortletLayout(group);
		Layout layout3 = LayoutTestUtil.addTypePortletLayout(group);

		int priority = layout1.getPriority();

		layout1.setPriority(layout3.getPriority());

		layout3.setPriority(priority);

		layout1 = LayoutLocalServiceUtil.updateLayout(layout1);
		layout3 = LayoutLocalServiceUtil.updateLayout(layout3);

		long[] layoutIds = {layout1.getLayoutId(), layout2.getLayoutId()};

		exportImportLayouts(layoutIds, getImportParameterMap());

		Layout importedLayout1 =
			LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
				layout1.getUuid(), importedGroup.getGroupId(), false);

		Assert.assertNotEquals(
			layout1.getPriority(), importedLayout1.getPriority());

		Layout importedLayout2 =
			LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
				layout2.getUuid(), importedGroup.getGroupId(), false);

		Assert.assertNotEquals(
			layout2.getPriority(), importedLayout2.getPriority());

		exportImportLayouts(
			ExportImportHelperUtil.getLayoutIds(
				LayoutLocalServiceUtil.getLayouts(group.getGroupId(), false)),
			getImportParameterMap());

		importedLayout1 = LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
			layout1.getUuid(), importedGroup.getGroupId(), false);

		Assert.assertEquals(
			layout1.getPriority(), importedLayout1.getPriority());

		importedLayout2 = LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
			layout2.getUuid(), importedGroup.getGroupId(), false);

		Assert.assertEquals(
			layout2.getPriority(), importedLayout2.getPriority());

		Layout importedLayout3 =
			LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
				layout3.getUuid(), importedGroup.getGroupId(), false);

		Assert.assertEquals(
			layout3.getPriority(), importedLayout3.getPriority());
	}

	@Test
	public void testExportImportLayoutsValidAvailableLocales()
		throws Exception {

		testAvailableLocales(
			Arrays.asList(LocaleUtil.US, LocaleUtil.US),
			Arrays.asList(LocaleUtil.US, LocaleUtil.SPAIN, LocaleUtil.US),
			false);
	}

	@Test
	public void testExportImportSelectedLayouts() throws Exception {
		Layout layout = LayoutTestUtil.addTypePortletLayout(group);

		long[] layoutIds = {layout.getLayoutId()};

		exportImportLayouts(layoutIds, getImportParameterMap());

		Assert.assertEquals(
			layoutIds.length,
			LayoutLocalServiceUtil.getLayoutsCount(importedGroup, false));

		importedLayout = LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
			layout.getUuid(), importedGroup.getGroupId(), false);

		Assert.assertNotNull(importedLayout);
	}

	@Test
	public void testExportImportUnselectedChildLayouts() throws Exception {
		Layout layout = LayoutTestUtil.addTypePortletLayout(group);

		Layout childLayout = LayoutTestUtil.addTypePortletLayout(
			group, layout.getPlid());

		Map<Long, Boolean> selectedLayouts = HashMapBuilder.put(
			LayoutConstants.DEFAULT_PLID, true
		).put(
			layout.getPlid(), false
		).build();

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
			group.getGroupId(), false);

		Map<String, String[]> exportParameterMap = getExportParameterMap();

		exportParameterMap.put(Constants.CMD, new String[] {Constants.EXPORT});

		exportLayouts(
			ExportImportHelperUtil.getLayoutIds(selectedLayouts),
			exportParameterMap);

		importLayouts(getImportParameterMap());

		Assert.assertNotEquals(
			layouts.size(),
			LayoutLocalServiceUtil.getLayoutsCount(importedGroup, false));

		importedLayout = LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
			childLayout.getUuid(), importedGroup.getGroupId(), false);

		Assert.assertNull(importedLayout);
	}

	@Ignore
	@Test
	public void testFriendlyURLCollision() throws Exception {
		String defaultLanguageId = LocaleUtil.toLanguageId(
			LocaleUtil.getDefault());

		Layout layoutA = LayoutTestUtil.addTypePortletLayout(group);

		String friendlyURLA = layoutA.getFriendlyURL();

		layoutA = LayoutLocalServiceUtil.updateFriendlyURL(
			layoutA.getUserId(), layoutA.getPlid(), friendlyURLA + "-de", "de");

		Layout layoutB = LayoutTestUtil.addTypePortletLayout(group);

		String friendlyURLB = layoutB.getFriendlyURL();

		layoutB = LayoutLocalServiceUtil.updateFriendlyURL(
			layoutB.getUserId(), layoutB.getPlid(), friendlyURLB + "-de", "de");

		long[] layoutIds = {layoutA.getLayoutId(), layoutB.getLayoutId()};

		exportImportLayouts(layoutIds, getImportParameterMap());

		layoutA = LayoutLocalServiceUtil.updateFriendlyURL(
			layoutA.getUserId(), layoutA.getPlid(), "/temp", defaultLanguageId);

		layoutA = LayoutLocalServiceUtil.updateFriendlyURL(
			layoutA.getUserId(), layoutA.getPlid(), "/temp-de", "de");

		layoutB = LayoutLocalServiceUtil.updateFriendlyURL(
			layoutB.getUserId(), layoutB.getPlid(), friendlyURLA,
			defaultLanguageId);

		LayoutLocalServiceUtil.updateFriendlyURL(
			layoutB.getUserId(), layoutB.getPlid(), friendlyURLA + "-de", "de");

		layoutA = LayoutLocalServiceUtil.updateFriendlyURL(
			layoutA.getUserId(), layoutA.getPlid(), friendlyURLB,
			defaultLanguageId);

		LayoutLocalServiceUtil.updateFriendlyURL(
			layoutA.getUserId(), layoutA.getPlid(), friendlyURLB + "-de", "de");

		exportImportLayouts(layoutIds, getImportParameterMap());
	}

	protected void testAvailableLocales(
			Collection<Locale> sourceAvailableLocales,
			Collection<Locale> targetAvailableLocales, boolean expectFailure)
		throws Exception {

		group = GroupTestUtil.updateDisplaySettings(
			group.getGroupId(), sourceAvailableLocales, null);
		importedGroup = GroupTestUtil.updateDisplaySettings(
			importedGroup.getGroupId(), targetAvailableLocales, null);

		LayoutTestUtil.addTypePortletLayout(group);

		long[] layoutIds = new long[0];

		try {
			exportImportLayouts(layoutIds, getImportParameterMap(), true);

			Assert.assertFalse(expectFailure);
		}
		catch (LocaleException localeException) {
			if (_log.isDebugEnabled()) {
				_log.debug(localeException);
			}

			Assert.assertTrue(expectFailure);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutExportImportTest.class);

}