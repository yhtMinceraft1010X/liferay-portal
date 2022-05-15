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
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.segments.constants.SegmentsEntryConstants;
import com.liferay.segments.criteria.Criteria;
import com.liferay.segments.criteria.CriteriaSerializer;
import com.liferay.segments.criteria.contributor.SegmentsCriteriaContributor;
import com.liferay.segments.exception.RequiredSegmentsEntryException;
import com.liferay.segments.exception.SegmentsEntryKeyException;
import com.liferay.segments.exception.SegmentsEntryNameException;
import com.liferay.segments.model.SegmentsEntry;
import com.liferay.segments.model.SegmentsEntryRel;
import com.liferay.segments.service.SegmentsEntryLocalService;
import com.liferay.segments.service.SegmentsEntryRelLocalService;
import com.liferay.segments.service.SegmentsEntryRoleLocalService;
import com.liferay.segments.service.SegmentsExperienceLocalService;
import com.liferay.segments.test.util.SegmentsTestUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

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
public class SegmentsEntryLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		UserTestUtil.setUser(TestPropsValues.getUser());

		_group = GroupTestUtil.addGroup();

		_groups.add(_group);

		_role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);
	}

	@Test
	public void testAddSegmentsEntry() throws PortalException {
		String segmentsEntryKey = RandomTestUtil.randomString();
		String name = RandomTestUtil.randomString();
		String description = RandomTestUtil.randomString();
		String criteria = CriteriaSerializer.serialize(new Criteria());
		String type = RandomTestUtil.randomString();

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), segmentsEntryKey, name, description, criteria,
			type);

		Assert.assertEquals(
			StringUtil.toUpperCase(segmentsEntryKey.trim()),
			segmentsEntry.getSegmentsEntryKey());
		Assert.assertEquals(
			name, segmentsEntry.getName(LocaleUtil.getDefault()));
		Assert.assertEquals(
			description, segmentsEntry.getDescription(LocaleUtil.getDefault()));
		Assert.assertTrue(segmentsEntry.isActive());
		Assert.assertEquals(criteria, segmentsEntry.getCriteria());
		Assert.assertEquals(type, segmentsEntry.getType());

		Assert.assertEquals(
			1,
			_segmentsEntryLocalService.getSegmentsEntriesCount(
				_group.getGroupId(), false));
	}

	@Test
	public void testAddSegmentsEntryClassPKs() throws PortalException {
		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		long[] classPKs = {
			RandomTestUtil.nextLong(), RandomTestUtil.nextLong()
		};

		_segmentsEntryLocalService.addSegmentsEntryClassPKs(
			segmentsEntry.getSegmentsEntryId(), classPKs,
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		List<SegmentsEntryRel> segmentsEntryRels =
			_segmentsEntryRelLocalService.getSegmentsEntryRels(
				segmentsEntry.getSegmentsEntryId());

		Stream<SegmentsEntryRel> stream = segmentsEntryRels.stream();

		long[] actualClassPKs = stream.mapToLong(
			SegmentsEntryRel::getClassPK
		).toArray();

		Assert.assertTrue(ArrayUtil.containsAll(actualClassPKs, classPKs));
	}

	@Test(expected = SegmentsEntryKeyException.class)
	public void testAddSegmentsEntryWithExistingKey() throws PortalException {
		String segmentsEntryKey = RandomTestUtil.randomString();

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), segmentsEntryKey);
		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), segmentsEntryKey);
	}

	@Test(expected = SegmentsEntryKeyException.class)
	public void testAddSegmentsEntryWithExistingKeyInAncestorGroup()
		throws Exception {

		String segmentsEntryKey = RandomTestUtil.randomString();

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), segmentsEntryKey);

		Group childGroup = GroupTestUtil.addGroup(_group.getGroupId());

		_groups.add(0, childGroup);

		SegmentsTestUtil.addSegmentsEntry(
			childGroup.getGroupId(), segmentsEntryKey);
	}

	@Test(expected = SegmentsEntryNameException.class)
	public void testAddSegmentsEntryWithoutName() throws Exception {
		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), RandomTestUtil.randomString(),
			StringPool.BLANK, StringPool.BLANK,
			CriteriaSerializer.serialize(new Criteria()),
			RandomTestUtil.randomString());
	}

	@Test
	public void testAddSegmentsEntryWithoutSource() throws PortalException {
		SegmentsEntry segmentsEntry =
			_segmentsEntryLocalService.addSegmentsEntry(
				RandomTestUtil.randomString(),
				RandomTestUtil.randomLocaleStringMap(),
				RandomTestUtil.randomLocaleStringMap(),
				RandomTestUtil.randomBoolean(),
				CriteriaSerializer.serialize(new Criteria()),
				User.class.getName(),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(
			SegmentsEntryConstants.SOURCE_DEFAULT, segmentsEntry.getSource());
	}

	@Test
	public void testAddSegmentsEntryWithReferredSource()
		throws PortalException {

		Criteria criteria = new Criteria();

		_segmentsEntrySegmentsCriteriaContributor.contribute(
			criteria,
			String.format(
				"(segmentsEntryIds eq '%s') and (segmentsEntryIds eq '%s')",
				RandomTestUtil.nextLong(), RandomTestUtil.nextLong()),
			Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Assert.assertEquals(
			SegmentsEntryConstants.SOURCE_REFERRED, segmentsEntry.getSource());
	}

	@Test
	public void testDeleteSegmentsEntry() throws PortalException {
		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		_segmentsEntryLocalService.deleteSegmentsEntry(
			segmentsEntry.getSegmentsEntryId());

		Assert.assertEquals(
			0,
			_segmentsEntryLocalService.getSegmentsEntriesCount(
				_group.getGroupId(), false));
	}

	@Test
	public void testDeleteSegmentsEntryByGroupId() throws PortalException {
		int count = 5;

		for (int i = 0; i < count; i++) {
			SegmentsTestUtil.addSegmentsEntry(_group.getGroupId());
		}

		Assert.assertEquals(
			count,
			_segmentsEntryLocalService.getSegmentsEntriesCount(
				_group.getGroupId(), false));

		_segmentsEntryLocalService.deleteSegmentsEntries(_group.getGroupId());

		Assert.assertEquals(
			0,
			_segmentsEntryLocalService.getSegmentsEntriesCount(
				_group.getGroupId(), false));
	}

	@Test
	public void testDeleteSegmentsEntryClassPKs() throws PortalException {
		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		long[] classPKs = {
			RandomTestUtil.nextLong(), RandomTestUtil.nextLong()
		};

		_segmentsEntryLocalService.addSegmentsEntryClassPKs(
			segmentsEntry.getSegmentsEntryId(), classPKs,
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		_segmentsEntryLocalService.deleteSegmentsEntryClassPKs(
			segmentsEntry.getSegmentsEntryId(), classPKs);

		Assert.assertEquals(
			0,
			_segmentsEntryRelLocalService.getSegmentsEntryRelsCount(
				segmentsEntry.getSegmentsEntryId()));
	}

	@Test(
		expected = RequiredSegmentsEntryException.MustNotDeleteSegmentsEntryReferencedBySegmentsExperiences.class
	)
	public void testDeleteSegmentsEntryReferencedBySegmentsExperiences()
		throws PortalException {

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		_segmentsExperienceLocalService.addSegmentsExperience(
			TestPropsValues.getUserId(), _group.getGroupId(),
			segmentsEntry.getSegmentsEntryId(), 0, 0,
			RandomTestUtil.randomLocaleStringMap(), RandomTestUtil.randomInt(),
			false, new UnicodeProperties(true),
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		_segmentsEntryLocalService.deleteSegmentsEntry(
			segmentsEntry.getSegmentsEntryId());
	}

	@Test
	public void testDeleteSegmentsEntryWithSegmentsEntryRels()
		throws PortalException {

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		int count = 5;

		for (int i = 0; i < count; i++) {
			_segmentsEntryRelLocalService.addSegmentsEntryRel(
				segmentsEntry.getSegmentsEntryId(), 0, i, serviceContext);
		}

		List<SegmentsEntryRel> segmentsEntryRels =
			_segmentsEntryRelLocalService.getSegmentsEntryRels(
				segmentsEntry.getSegmentsEntryId());

		Assert.assertEquals(
			segmentsEntryRels.toString(), count, segmentsEntryRels.size());

		_segmentsEntryLocalService.deleteSegmentsEntry(segmentsEntry);

		segmentsEntryRels = _segmentsEntryRelLocalService.getSegmentsEntryRels(
			segmentsEntry.getSegmentsEntryId());

		Assert.assertEquals(
			segmentsEntryRels.toString(), 0, segmentsEntryRels.size());
	}

	@Test
	public void testGetSegmentsEntriesCountWithIncludeAncestorSegmentsEntries()
		throws Exception {

		SegmentsTestUtil.addSegmentsEntry(_group.getGroupId());

		Group childGroup = GroupTestUtil.addGroup(_group.getGroupId());

		_groups.add(0, childGroup);

		int segmentsEntriesCount =
			_segmentsEntryLocalService.getSegmentsEntriesCount(
				childGroup.getGroupId(), true);

		Assert.assertTrue(segmentsEntriesCount > 0);

		Assert.assertEquals(
			0,
			_segmentsEntryLocalService.getSegmentsEntriesCount(
				childGroup.getGroupId(), false));
	}

	@Test
	public void testGetSegmentsEntriesWithIncludeAncestorSegmentsEntries()
		throws Exception {

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		Group childGroup = GroupTestUtil.addGroup(_group.getGroupId());

		_groups.add(0, childGroup);

		List<SegmentsEntry> segmentsEntries =
			_segmentsEntryLocalService.getSegmentsEntries(
				childGroup.getGroupId(), true, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null);

		Assert.assertTrue(segmentsEntries.contains(segmentsEntry));

		segmentsEntries = _segmentsEntryLocalService.getSegmentsEntries(
			childGroup.getGroupId(), false, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);

		Assert.assertFalse(segmentsEntries.contains(segmentsEntry));
	}

	@Test
	public void testSearchSegmentsEntries() throws PortalException {
		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		BaseModelSearchResult<SegmentsEntry> baseModelSearchResult =
			_segmentsEntryLocalService.searchSegmentsEntries(
				segmentsEntry.getCompanyId(), segmentsEntry.getGroupId(),
				segmentsEntry.getNameCurrentValue(), true,
				new LinkedHashMap<>(), 0, 1, null);

		List<SegmentsEntry> segmentsEntries =
			baseModelSearchResult.getBaseModels();

		Assert.assertEquals(
			segmentsEntries.toString(), 1, segmentsEntries.size());
		Assert.assertEquals(segmentsEntry, segmentsEntries.get(0));
	}

	@Test
	public void testSearchSegmentsEntriesWithExcludedSegmentsEntryIds()
		throws PortalException {

		SegmentsEntry segmentsEntry1 = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());
		SegmentsEntry segmentsEntry2 = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		BaseModelSearchResult<SegmentsEntry> baseModelSearchResult =
			_segmentsEntryLocalService.searchSegmentsEntries(
				segmentsEntry1.getCompanyId(), segmentsEntry1.getGroupId(),
				null, true,
				LinkedHashMapBuilder.<String, Object>put(
					"excludedSegmentsEntryIds",
					new long[] {segmentsEntry1.getSegmentsEntryId()}
				).build(),
				0, 1, null);

		List<SegmentsEntry> segmentsEntries =
			baseModelSearchResult.getBaseModels();

		Assert.assertEquals(
			segmentsEntries.toString(), 1, segmentsEntries.size());
		Assert.assertEquals(segmentsEntry2, segmentsEntries.get(0));
	}

	@Test
	public void testSearchSegmentsEntriesWithExcludedSources()
		throws PortalException {

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		SegmentsTestUtil.addSegmentsEntry(
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(),
			CriteriaSerializer.serialize(new Criteria()),
			SegmentsEntryConstants.SOURCE_ASAH_FARO_BACKEND,
			RandomTestUtil.randomString(),
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		BaseModelSearchResult<SegmentsEntry> baseModelSearchResult =
			_segmentsEntryLocalService.searchSegmentsEntries(
				segmentsEntry.getCompanyId(), segmentsEntry.getGroupId(), null,
				true,
				LinkedHashMapBuilder.<String, Object>put(
					"excludedSources",
					new String[] {
						SegmentsEntryConstants.SOURCE_ASAH_FARO_BACKEND
					}
				).build(),
				0, 1, null);

		List<SegmentsEntry> segmentsEntries =
			baseModelSearchResult.getBaseModels();

		Assert.assertEquals(
			segmentsEntries.toString(), 1, segmentsEntries.size());
		Assert.assertEquals(segmentsEntry, segmentsEntries.get(0));
	}

	@Test
	public void testSearchSegmentsEntriesWithRoleIds() throws PortalException {
		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		_segmentsEntryRoleLocalService.addSegmentsEntryRole(
			segmentsEntry.getSegmentsEntryId(), _role.getRoleId(),
			ServiceContextTestUtil.getServiceContext());

		BaseModelSearchResult<SegmentsEntry> baseModelSearchResult =
			_segmentsEntryLocalService.searchSegmentsEntries(
				segmentsEntry.getCompanyId(), segmentsEntry.getGroupId(), null,
				true,
				LinkedHashMapBuilder.<String, Object>put(
					"roleIds", new long[] {_role.getRoleId()}
				).build(),
				0, 1, null);

		List<SegmentsEntry> segmentsEntries =
			baseModelSearchResult.getBaseModels();

		Assert.assertEquals(
			segmentsEntries.toString(), 1, segmentsEntries.size());
		Assert.assertEquals(segmentsEntry, segmentsEntries.get(0));
	}

	@Test
	public void testUpdateSegmentsEntry() throws PortalException {
		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		String segmentsEntryKey = RandomTestUtil.randomString();

		Map<Locale, String> nameMap = HashMapBuilder.put(
			LocaleUtil.getDefault(), RandomTestUtil.randomString()
		).build();

		Map<Locale, String> descriptionMap = HashMapBuilder.put(
			LocaleUtil.getDefault(), RandomTestUtil.randomString()
		).build();

		String criteria = CriteriaSerializer.serialize(new Criteria());

		SegmentsEntry updatedSegmentsEntry =
			_segmentsEntryLocalService.updateSegmentsEntry(
				segmentsEntry.getSegmentsEntryId(), segmentsEntryKey, nameMap,
				descriptionMap, false, criteria,
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(
			StringUtil.toUpperCase(segmentsEntryKey.trim()),
			updatedSegmentsEntry.getSegmentsEntryKey());
		Assert.assertEquals(
			nameMap.get(LocaleUtil.getDefault()),
			updatedSegmentsEntry.getName(LocaleUtil.getDefault()));
		Assert.assertEquals(
			descriptionMap.get(LocaleUtil.getDefault()),
			updatedSegmentsEntry.getDescription(LocaleUtil.getDefault()));
		Assert.assertFalse(updatedSegmentsEntry.isActive());
		Assert.assertEquals(criteria, updatedSegmentsEntry.getCriteria());

		Assert.assertEquals(
			1,
			_segmentsEntryLocalService.getSegmentsEntriesCount(
				_group.getGroupId(), false));
	}

	@Test
	public void testUpdateSegmentsEntryWithAsahFaroBackendSource()
		throws PortalException {

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(),
			CriteriaSerializer.serialize(new Criteria()),
			SegmentsEntryConstants.SOURCE_ASAH_FARO_BACKEND,
			RandomTestUtil.randomString(),
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		SegmentsEntry updatedSegmentsEntry =
			_segmentsEntryLocalService.updateSegmentsEntry(
				segmentsEntry.getSegmentsEntryId(),
				segmentsEntry.getSegmentsEntryKey(), segmentsEntry.getNameMap(),
				segmentsEntry.getDescriptionMap(), segmentsEntry.isActive(),
				segmentsEntry.getCriteria(),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(
			SegmentsEntryConstants.SOURCE_ASAH_FARO_BACKEND,
			updatedSegmentsEntry.getSource());
	}

	@Test(expected = SegmentsEntryKeyException.class)
	public void testUpdateSegmentsEntryWithExistingKey()
		throws PortalException {

		String segmentsEntryKey = RandomTestUtil.randomString();

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), segmentsEntryKey);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), RandomTestUtil.randomString());

		_segmentsEntryLocalService.updateSegmentsEntry(
			segmentsEntry.getSegmentsEntryId(), segmentsEntryKey,
			segmentsEntry.getNameMap(), segmentsEntry.getDescriptionMap(),
			segmentsEntry.isActive(), segmentsEntry.getCriteria(),
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));
	}

	@Test
	public void testUpdateSegmentsEntryWithOutdatedReferredSource()
		throws PortalException {

		Criteria criteria = new Criteria();

		_segmentsEntrySegmentsCriteriaContributor.contribute(
			criteria,
			String.format(
				"(segmentsEntryIds eq '%s')", RandomTestUtil.nextLong()),
			Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		SegmentsEntry updatedSegmentsEntry =
			_segmentsEntryLocalService.updateSegmentsEntry(
				segmentsEntry.getSegmentsEntryId(),
				segmentsEntry.getSegmentsEntryKey(), segmentsEntry.getNameMap(),
				segmentsEntry.getDescriptionMap(), false,
				CriteriaSerializer.serialize(new Criteria()),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(
			SegmentsEntryConstants.SOURCE_DEFAULT,
			updatedSegmentsEntry.getSource());
	}

	@Test
	public void testUpdateSegmentsEntryWithReferredSource()
		throws PortalException {

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId());

		Criteria criteria = new Criteria();

		_segmentsEntrySegmentsCriteriaContributor.contribute(
			criteria,
			String.format(
				"(segmentsEntryIds eq '%s') and (segmentsEntryIds eq '%s')",
				RandomTestUtil.nextLong(), RandomTestUtil.nextLong()),
			Criteria.Conjunction.AND);

		SegmentsEntry updatedSegmentsEntry =
			_segmentsEntryLocalService.updateSegmentsEntry(
				segmentsEntry.getSegmentsEntryId(),
				segmentsEntry.getSegmentsEntryKey(), segmentsEntry.getNameMap(),
				segmentsEntry.getDescriptionMap(), false,
				CriteriaSerializer.serialize(criteria),
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(
			SegmentsEntryConstants.SOURCE_REFERRED,
			updatedSegmentsEntry.getSource());
	}

	private Group _group;

	@DeleteAfterTestRun
	private final List<Group> _groups = new ArrayList<>();

	@DeleteAfterTestRun
	private Role _role;

	@Inject
	private SegmentsEntryLocalService _segmentsEntryLocalService;

	@Inject
	private SegmentsEntryRelLocalService _segmentsEntryRelLocalService;

	@Inject
	private SegmentsEntryRoleLocalService _segmentsEntryRoleLocalService;

	@Inject(
		filter = "segments.criteria.contributor.key=segments",
		type = SegmentsCriteriaContributor.class
	)
	private SegmentsCriteriaContributor
		_segmentsEntrySegmentsCriteriaContributor;

	@Inject
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

}