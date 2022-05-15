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

package com.liferay.segments.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.segments.constants.SegmentsEntryConstants;
import com.liferay.segments.constants.SegmentsExperienceConstants;
import com.liferay.segments.constants.SegmentsExperimentConstants;
import com.liferay.segments.exception.LockedSegmentsExperimentException;
import com.liferay.segments.exception.RequiredSegmentsExperienceException;
import com.liferay.segments.exception.SegmentsExperienceNameException;
import com.liferay.segments.exception.SegmentsExperiencePriorityException;
import com.liferay.segments.model.SegmentsEntry;
import com.liferay.segments.model.SegmentsExperience;
import com.liferay.segments.model.SegmentsExperiment;
import com.liferay.segments.service.SegmentsExperienceLocalService;
import com.liferay.segments.service.SegmentsExperimentLocalService;
import com.liferay.segments.service.persistence.SegmentsExperiencePersistence;
import com.liferay.segments.test.util.SegmentsTestUtil;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author David Arques
 */
@RunWith(Arquillian.class)
public class SegmentsExperienceLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		UserTestUtil.setUser(TestPropsValues.getUser());

		_group = GroupTestUtil.addGroup();

		_classNameId = _classNameLocalService.getClassNameId(
			Layout.class.getName());

		Layout layout = LayoutTestUtil.addTypeContentLayout(_group);

		_classPK = layout.getPlid();
	}

	@Test
	public void testAddSegmentsExperienceActive() throws Exception {
		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());
		Map<Locale, String> nameMap = RandomTestUtil.randomLocaleStringMap();
		int priority = RandomTestUtil.randomInt();

		SegmentsExperience segmentsExperience =
			_segmentsExperienceLocalService.addSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				segmentsEntry.getSegmentsEntryId(), _classNameId, _classPK,
				nameMap, priority, true, new UnicodeProperties(true),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(
			segmentsEntry.getSegmentsEntryId(),
			segmentsExperience.getSegmentsEntryId());
		Assert.assertEquals(_classNameId, segmentsExperience.getClassNameId());
		Assert.assertEquals(_classPK, segmentsExperience.getClassPK());
		Assert.assertEquals(nameMap, segmentsExperience.getNameMap());
		Assert.assertTrue(segmentsExperience.isActive());
		Assert.assertEquals(
			StringPool.BLANK, segmentsExperience.getTypeSettings());

		Assert.assertEquals(
			2,
			_segmentsExperienceLocalService.getSegmentsExperiencesCount(
				_group.getGroupId(), _classNameId, _classPK, true));
	}

	@Test
	public void testAddSegmentsExperienceActiveWithoutPriority()
		throws Exception {

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		SegmentsExperience segmentsExperience =
			_segmentsExperienceLocalService.addSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				segmentsEntry.getSegmentsEntryId(), _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				new UnicodeProperties(true),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(-1, segmentsExperience.getPriority());

		Assert.assertEquals(
			2,
			_segmentsExperienceLocalService.getSegmentsExperiencesCount(
				_group.getGroupId(), _classNameId, _classPK, true));
	}

	@Test
	public void testAddSegmentsExperienceInactive() throws Exception {
		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());
		Map<Locale, String> nameMap = RandomTestUtil.randomLocaleStringMap();
		int priority = RandomTestUtil.randomInt();

		SegmentsExperience segmentsExperience =
			_segmentsExperienceLocalService.addSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				segmentsEntry.getSegmentsEntryId(), _classNameId, _classPK,
				nameMap, priority, false, new UnicodeProperties(true),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(
			segmentsEntry.getSegmentsEntryId(),
			segmentsExperience.getSegmentsEntryId());
		Assert.assertEquals(_classNameId, segmentsExperience.getClassNameId());
		Assert.assertEquals(_classPK, segmentsExperience.getClassPK());
		Assert.assertEquals(nameMap, segmentsExperience.getNameMap());
		Assert.assertFalse(segmentsExperience.isActive());
		Assert.assertEquals(
			StringPool.BLANK, segmentsExperience.getTypeSettings());

		Assert.assertEquals(
			1,
			_segmentsExperienceLocalService.getSegmentsExperiencesCount(
				_group.getGroupId(), _classNameId, _classPK, false));
	}

	@Test
	public void testAddSegmentsExperienceInactiveWithoutPriority()
		throws Exception {

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		SegmentsExperience segmentsExperience =
			_segmentsExperienceLocalService.addSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				segmentsEntry.getSegmentsEntryId(), _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), false,
				new UnicodeProperties(true),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(-1, segmentsExperience.getPriority());

		Assert.assertEquals(
			1,
			_segmentsExperienceLocalService.getSegmentsExperiencesCount(
				_group.getGroupId(), _classNameId, _classPK, false));
	}

	@Test
	public void testAddSegmentsExperiencesWithTheSameSegmentsEntry()
		throws Exception {

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		SegmentsTestUtil.addSegmentsExperience(
			_group.getGroupId(), segmentsEntry.getSegmentsEntryId(),
			_classNameId, _classPK);
		SegmentsTestUtil.addSegmentsExperience(
			_group.getGroupId(), segmentsEntry.getSegmentsEntryId(),
			_classNameId, _classPK);

		List<SegmentsExperience> segmentsExperiences =
			_segmentsExperienceLocalService.getSegmentsExperiences(
				_group.getGroupId(), _classNameId, _classPK, true,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Assert.assertEquals(
			segmentsExperiences.toString(), 3, segmentsExperiences.size());
	}

	@Test(expected = SegmentsExperiencePriorityException.class)
	public void testAddSegmentsExperienceWithInvalidPriority()
		throws Exception {

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		SegmentsExperience segmentsExperience =
			SegmentsTestUtil.addSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK);

		_segmentsExperienceLocalService.addSegmentsExperience(
			TestPropsValues.getUserId(), _group.getGroupId(),
			segmentsEntry.getSegmentsEntryId(), _classNameId, _classPK,
			RandomTestUtil.randomLocaleStringMap(),
			segmentsExperience.getPriority(), true, new UnicodeProperties(true),
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));
	}

	@Test(expected = SegmentsExperienceNameException.class)
	public void testAddSegmentsExperienceWithoutName() throws Exception {
		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		_segmentsExperienceLocalService.addSegmentsExperience(
			TestPropsValues.getUserId(), _group.getGroupId(),
			segmentsEntry.getSegmentsEntryId(), _classNameId, _classPK,
			Collections.emptyMap(), RandomTestUtil.randomInt(),
			RandomTestUtil.randomBoolean(), new UnicodeProperties(true),
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));
	}

	@Test
	public void testAddSegmentsExperienceWithTypeSetting() throws Exception {
		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		SegmentsExperience segmentsExperience =
			_segmentsExperienceLocalService.addSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				segmentsEntry.getSegmentsEntryId(), _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(),
				RandomTestUtil.randomInt(), RandomTestUtil.randomBoolean(),
				UnicodePropertiesBuilder.create(
					true
				).put(
					"property", "value"
				).build(),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		UnicodeProperties actualTypeSettingsUnicodeProperties =
			segmentsExperience.getTypeSettingsUnicodeProperties();

		Assert.assertEquals(
			"value",
			actualTypeSettingsUnicodeProperties.getProperty("property"));
	}

	@Test
	public void testAppendSegmentsExperience() throws Exception {
		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());
		Map<Locale, String> nameMap = RandomTestUtil.randomLocaleStringMap();
		boolean active = RandomTestUtil.randomBoolean();

		SegmentsExperience segmentsExperience =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				segmentsEntry.getSegmentsEntryId(), _classNameId, _classPK,
				nameMap, active,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(
			segmentsEntry.getSegmentsEntryId(),
			segmentsExperience.getSegmentsEntryId());
		Assert.assertEquals(_classNameId, segmentsExperience.getClassNameId());
		Assert.assertEquals(_classPK, segmentsExperience.getClassPK());
		Assert.assertEquals(nameMap, segmentsExperience.getNameMap());
		Assert.assertEquals(active, segmentsExperience.isActive());
		Assert.assertEquals(1, segmentsExperience.getPriority());
		Assert.assertEquals(
			StringPool.BLANK, segmentsExperience.getTypeSettings());
	}

	@Test
	public void testAppendSegmentsExperienceWithoutTypeSettings()
		throws Exception {

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		SegmentsExperience segmentsExperience =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				segmentsEntry.getSegmentsEntryId(), _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(),
				RandomTestUtil.randomBoolean(),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(
			StringPool.BLANK, segmentsExperience.getTypeSettings());
	}

	@Test
	public void testDeleteSegmentsExperience() throws Exception {
		SegmentsExperience segmentsExperience =
			SegmentsTestUtil.addSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK);

		_segmentsExperienceLocalService.deleteSegmentsExperience(
			segmentsExperience.getSegmentsExperienceId());

		Assert.assertNull(
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience.getSegmentsExperienceId()));
	}

	@Test(
		expected = RequiredSegmentsExperienceException.MustNotDeleteSegmentsExperienceReferencedBySegmentsExperiments.class
	)
	public void testDeleteSegmentsExperienceReferencedBySegmentsExperiments()
		throws PortalException {

		SegmentsExperience segmentsExperience =
			SegmentsTestUtil.addSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK);

		SegmentsExperiment segmentsExperiment =
			SegmentsTestUtil.addSegmentsExperiment(
				_group.getGroupId(),
				segmentsExperience.getSegmentsExperienceId(),
				segmentsExperience.getClassNameId(),
				segmentsExperience.getClassPK());

		_segmentsExperimentLocalService.updateSegmentsExperimentStatus(
			segmentsExperiment.getSegmentsExperimentId(),
			SegmentsExperimentConstants.STATUS_RUNNING);

		_segmentsExperienceLocalService.deleteSegmentsExperience(
			segmentsExperience.getSegmentsExperienceId());
	}

	@Test
	public void testDeleteSegmentsExperienceWithHighestPriority()
		throws Exception {

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		SegmentsExperience segmentsExperience1 =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				segmentsEntry.getSegmentsEntryId(), _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(),
				RandomTestUtil.randomBoolean(),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(1, segmentsExperience1.getPriority());

		SegmentsExperience segmentsExperience2 =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				segmentsEntry.getSegmentsEntryId(), _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(),
				RandomTestUtil.randomBoolean(),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(2, segmentsExperience2.getPriority());

		_segmentsExperienceLocalService.deleteSegmentsExperience(
			segmentsExperience1.getSegmentsExperienceId());

		segmentsExperience2 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience2.getSegmentsExperienceId());

		Assert.assertEquals(1, segmentsExperience2.getPriority());
	}

	@Test
	public void testDeleteSegmentsExperienceWithMidrangeNegativePriority()
		throws Exception {

		SegmentsExperience segmentsExperience1 =
			_segmentsExperienceLocalService.addSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				new UnicodeProperties(true),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));
		SegmentsExperience segmentsExperience2 =
			_segmentsExperienceLocalService.addSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				new UnicodeProperties(true),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));
		SegmentsExperience segmentsExperience3 =
			_segmentsExperienceLocalService.addSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				new UnicodeProperties(true),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		_segmentsExperienceLocalService.deleteSegmentsExperience(
			segmentsExperience2.getSegmentsExperienceId());

		segmentsExperience1 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience1.getSegmentsExperienceId());

		Assert.assertEquals(-1, segmentsExperience1.getPriority());

		segmentsExperience2 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience2.getSegmentsExperienceId());

		Assert.assertNull(segmentsExperience2);

		segmentsExperience3 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience3.getSegmentsExperienceId());

		Assert.assertEquals(-2, segmentsExperience3.getPriority());
	}

	@Test
	public void testDeleteSegmentsExperienceWithMidrangePriority()
		throws Exception {

		SegmentsExperience segmentsExperience1 =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));
		SegmentsExperience segmentsExperience2 =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));
		SegmentsExperience segmentsExperience3 =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));
		SegmentsExperience segmentsExperience4 =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));
		SegmentsExperience segmentsExperience5 =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		_segmentsExperienceLocalService.deleteSegmentsExperience(
			segmentsExperience2.getSegmentsExperienceId());

		segmentsExperience1 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience1.getSegmentsExperienceId());

		Assert.assertEquals(1, segmentsExperience1.getPriority());

		segmentsExperience2 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience2.getSegmentsExperienceId());

		Assert.assertNull(segmentsExperience2);

		segmentsExperience3 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience3.getSegmentsExperienceId());

		Assert.assertEquals(2, segmentsExperience3.getPriority());

		segmentsExperience4 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience4.getSegmentsExperienceId());

		Assert.assertEquals(3, segmentsExperience4.getPriority());

		segmentsExperience5 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience5.getSegmentsExperienceId());

		Assert.assertEquals(4, segmentsExperience5.getPriority());
	}

	@Test
	public void testFetchDefaultSegmentsExperienceId() throws PortalException {
		long defaultSegmentsExperienceId =
			_segmentsExperienceLocalService.fetchDefaultSegmentsExperienceId(
				_classPK);

		_segmentsExperienceLocalService.deleteSegmentsExperience(
			defaultSegmentsExperienceId);

		SegmentsExperience segmentsExperience =
			_segmentsExperienceLocalService.addDefaultSegmentsExperience(
				TestPropsValues.getUserId(), _classPK,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(
			segmentsExperience.getSegmentsExperienceId(),
			_segmentsExperienceLocalService.fetchDefaultSegmentsExperienceId(
				_classPK));
	}

	@Test
	public void testFetchDefaultSegmentsExperienceIdWithNonexistingDefaultExperience()
		throws PortalException {

		long defaultSegmentsExperienceId =
			_segmentsExperienceLocalService.fetchDefaultSegmentsExperienceId(
				_classPK);

		_segmentsExperienceLocalService.deleteSegmentsExperience(
			defaultSegmentsExperienceId);

		Assert.assertEquals(
			SegmentsExperienceConstants.ID_DEFAULT,
			_segmentsExperienceLocalService.fetchDefaultSegmentsExperienceId(
				_classPK));
	}

	@Test
	public void testFetchDefaultSegmentsExperienceIdWithNonexistingPlid()
		throws PortalException {

		long defaultSegmentsExperienceId =
			_segmentsExperienceLocalService.fetchDefaultSegmentsExperienceId(
				_classPK);

		_segmentsExperienceLocalService.deleteSegmentsExperience(
			defaultSegmentsExperienceId);

		Assert.assertEquals(
			SegmentsExperienceConstants.ID_DEFAULT,
			_segmentsExperienceLocalService.fetchDefaultSegmentsExperienceId(
				0));
	}

	@Test
	public void testFetchSegmentsExperience() throws Exception {
		SegmentsExperience segmentsExperience =
			SegmentsTestUtil.addSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK);

		Assert.assertEquals(
			segmentsExperience,
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK,
				segmentsExperience.getPriority()));
		Assert.assertNotNull(
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK,
				segmentsExperience.getPriority() + 1));
	}

	@Test(expected = LockedSegmentsExperimentException.class)
	public void testUpdatePrioritySegmentsExperienceWithSegmentsExperimentInStatusRunning()
		throws Exception {

		SegmentsExperience segmentsExperience =
			SegmentsTestUtil.addSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK);

		SegmentsExperiment segmentsExperiment =
			SegmentsTestUtil.addSegmentsExperiment(
				_group.getGroupId(),
				segmentsExperience.getSegmentsExperienceId(), _classNameId,
				_classPK);

		_segmentsExperimentLocalService.updateSegmentsExperimentStatus(
			segmentsExperiment.getSegmentsExperimentId(),
			SegmentsExperimentConstants.STATUS_RUNNING);

		_segmentsExperienceLocalService.updateSegmentsExperiencePriority(
			segmentsExperience.getSegmentsExperienceId(), -1);
	}

	@Test
	public void testUpdateSegmentsExperience() throws Exception {
		SegmentsExperience segmentsExperience =
			SegmentsTestUtil.addSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());
		Map<Locale, String> nameMap = RandomTestUtil.randomLocaleStringMap();
		boolean active = RandomTestUtil.randomBoolean();

		SegmentsExperience updatedSegmentsExperience =
			_segmentsExperienceLocalService.updateSegmentsExperience(
				segmentsExperience.getSegmentsExperienceId(),
				segmentsEntry.getSegmentsEntryId(), nameMap, active,
				UnicodePropertiesBuilder.create(
					true
				).put(
					"property", "value"
				).build());

		Assert.assertEquals(
			segmentsEntry.getSegmentsEntryId(),
			updatedSegmentsExperience.getSegmentsEntryId());
		Assert.assertEquals(nameMap, updatedSegmentsExperience.getNameMap());
		Assert.assertEquals(active, updatedSegmentsExperience.isActive());

		UnicodeProperties actualTypeSettingsUnicodeProperties =
			updatedSegmentsExperience.getTypeSettingsUnicodeProperties();

		Assert.assertEquals(
			"value",
			actualTypeSettingsUnicodeProperties.getProperty("property"));
	}

	@Test
	public void testUpdateSegmentsExperiencePriority() throws Exception {
		SegmentsExperience segmentsExperience1 =
			SegmentsTestUtil.addSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK);
		SegmentsExperience segmentsExperience2 =
			SegmentsTestUtil.addSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK);

		int priority1 = segmentsExperience1.getPriority();
		int priority2 = segmentsExperience2.getPriority();

		SegmentsExperience movedSegmentsExperience =
			_segmentsExperienceLocalService.updateSegmentsExperiencePriority(
				segmentsExperience1.getSegmentsExperienceId(),
				segmentsExperience2.getPriority());

		segmentsExperience1 =
			_segmentsExperienceLocalService.getSegmentsExperience(
				segmentsExperience1.getSegmentsExperienceId());

		segmentsExperience2 =
			_segmentsExperienceLocalService.getSegmentsExperience(
				segmentsExperience2.getSegmentsExperienceId());

		Assert.assertEquals(movedSegmentsExperience, segmentsExperience1);
		Assert.assertEquals(priority1, segmentsExperience2.getPriority());
		Assert.assertEquals(priority2, segmentsExperience1.getPriority());
	}

	@Test
	public void testUpdateSegmentsExperienceToZero() throws Exception {
		SegmentsExperience segmentsExperience =
			SegmentsTestUtil.addSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK);

		Assert.assertEquals(-1, segmentsExperience.getPriority());

		segmentsExperience =
			_segmentsExperienceLocalService.updateSegmentsExperiencePriority(
				segmentsExperience.getSegmentsExperienceId(), 1);

		Assert.assertEquals(1, segmentsExperience.getPriority());
	}

	@Test
	public void testUpdateSegmentsExperienceToZeroWithAboveSegmentsExperiences()
		throws Exception {

		SegmentsExperience segmentsExperience1 =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(1, segmentsExperience1.getPriority());

		SegmentsExperience segmentsExperience2 =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(2, segmentsExperience2.getPriority());

		SegmentsExperience segmentsExperience3 =
			_segmentsExperienceLocalService.addSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				new UnicodeProperties(true),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(segmentsExperience3.getPriority(), -1);

		segmentsExperience3 =
			_segmentsExperienceLocalService.updateSegmentsExperiencePriority(
				segmentsExperience3.getSegmentsExperienceId(),
				segmentsExperience3.getPriority() + 1);

		Assert.assertEquals(1, segmentsExperience3.getPriority());

		segmentsExperience1 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience1.getSegmentsExperienceId());

		Assert.assertEquals(2, segmentsExperience1.getPriority());

		segmentsExperience2 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience2.getSegmentsExperienceId());

		Assert.assertEquals(3, segmentsExperience2.getPriority());
	}

	@Test
	public void testUpdateSegmentsExperienceToZeroWithBelowSegmentsExperiences()
		throws Exception {

		SegmentsExperience segmentsExperience1 =
			SegmentsTestUtil.addSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK);

		Assert.assertEquals(-1, segmentsExperience1.getPriority());

		SegmentsExperience segmentsExperience2 =
			SegmentsTestUtil.addSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK);

		Assert.assertEquals(-2, segmentsExperience2.getPriority());

		segmentsExperience1 =
			_segmentsExperienceLocalService.updateSegmentsExperiencePriority(
				segmentsExperience1.getSegmentsExperienceId(),
				segmentsExperience1.getPriority() + 1);

		Assert.assertEquals(1, segmentsExperience1.getPriority());

		segmentsExperience2 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience2.getSegmentsExperienceId());

		Assert.assertEquals(-1, segmentsExperience2.getPriority());
	}

	@Test
	public void testUpdateSegmentsExperienceUnderZero() throws Exception {
		SegmentsExperience segmentsExperience1 =
			_segmentsExperienceLocalService.addSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				new UnicodeProperties(true),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(-1, segmentsExperience1.getPriority());

		segmentsExperience1 =
			_segmentsExperienceLocalService.updateSegmentsExperiencePriority(
				segmentsExperience1.getSegmentsExperienceId(),
				segmentsExperience1.getPriority() + 1);

		Assert.assertEquals(1, segmentsExperience1.getPriority());
	}

	@Test
	public void testUpdateSegmentsExperienceUnderZeroWithAboveSegmentsExperiences()
		throws Exception {

		SegmentsExperience segmentsExperience1 =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(1, segmentsExperience1.getPriority());

		SegmentsExperience segmentsExperience2 =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(2, segmentsExperience2.getPriority());

		segmentsExperience1 =
			_segmentsExperienceLocalService.updateSegmentsExperiencePriority(
				segmentsExperience1.getSegmentsExperienceId(),
				segmentsExperience1.getPriority() - 1);

		Assert.assertEquals(-1, segmentsExperience1.getPriority());

		segmentsExperience2 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience2.getSegmentsExperienceId());

		Assert.assertEquals(1, segmentsExperience2.getPriority());
	}

	@Test
	public void testUpdateSegmentsExperienceUnderZeroWithBelowSegmentsExperiences()
		throws Exception {

		SegmentsExperience segmentsExperience1 =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(1, segmentsExperience1.getPriority());

		SegmentsExperience segmentsExperience2 =
			_segmentsExperienceLocalService.addSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				new UnicodeProperties(true),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(-1, segmentsExperience2.getPriority());

		segmentsExperience1 =
			_segmentsExperienceLocalService.updateSegmentsExperiencePriority(
				segmentsExperience1.getSegmentsExperienceId(),
				segmentsExperience1.getPriority() - 1);

		Assert.assertEquals(-1, segmentsExperience1.getPriority());

		segmentsExperience2 =
			_segmentsExperienceLocalService.fetchSegmentsExperience(
				segmentsExperience2.getSegmentsExperienceId());

		Assert.assertEquals(-2, segmentsExperience2.getPriority());
	}

	@Test
	public void testUpdateSegmentsExperienceWithoutTypeSettings()
		throws Exception {

		SegmentsExperience segmentsExperience =
			_segmentsExperienceLocalService.appendSegmentsExperience(
				TestPropsValues.getUserId(), _group.getGroupId(),
				SegmentsEntryConstants.ID_DEFAULT, _classNameId, _classPK,
				RandomTestUtil.randomLocaleStringMap(), true,
				UnicodePropertiesBuilder.create(
					true
				).put(
					"property", "value"
				).build(),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		SegmentsExperience updatedSegmentsExperience =
			_segmentsExperienceLocalService.updateSegmentsExperience(
				segmentsExperience.getSegmentsExperienceId(),
				SegmentsEntryConstants.ID_DEFAULT,
				RandomTestUtil.randomLocaleStringMap(),
				RandomTestUtil.randomBoolean());

		UnicodeProperties actualTypeSettingsUnicodeProperties =
			updatedSegmentsExperience.getTypeSettingsUnicodeProperties();

		Assert.assertEquals(
			"value",
			actualTypeSettingsUnicodeProperties.getProperty("property"));
	}

	@Test(expected = LockedSegmentsExperimentException.class)
	public void testUpdateSegmentsExperienceWithSegmentsExperimentInStatusRunning()
		throws Exception {

		SegmentsExperience segmentsExperience =
			SegmentsTestUtil.addSegmentsExperience(
				_group.getGroupId(), _classNameId, _classPK);

		SegmentsExperiment segmentsExperiment =
			SegmentsTestUtil.addSegmentsExperiment(
				_group.getGroupId(),
				segmentsExperience.getSegmentsExperienceId(), _classNameId,
				_classPK);

		_segmentsExperimentLocalService.updateSegmentsExperimentStatus(
			segmentsExperiment.getSegmentsExperimentId(),
			SegmentsExperimentConstants.STATUS_RUNNING);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		_segmentsExperienceLocalService.updateSegmentsExperience(
			segmentsExperience.getSegmentsExperienceId(),
			segmentsEntry.getSegmentsEntryId(),
			RandomTestUtil.randomLocaleStringMap(),
			RandomTestUtil.randomBoolean());
	}

	private long _classNameId;

	@Inject
	private ClassNameLocalService _classNameLocalService;

	private long _classPK;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private LayoutLocalService _layoutLocalService;

	@Inject
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

	@Inject
	private SegmentsExperiencePersistence _segmentsExperiencePersistence;

	@Inject
	private SegmentsExperimentLocalService _segmentsExperimentLocalService;

}