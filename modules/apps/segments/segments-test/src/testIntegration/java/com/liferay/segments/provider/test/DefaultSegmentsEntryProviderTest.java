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

package com.liferay.segments.provider.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.segments.constants.SegmentsEntryConstants;
import com.liferay.segments.context.Context;
import com.liferay.segments.criteria.Criteria;
import com.liferay.segments.criteria.CriteriaSerializer;
import com.liferay.segments.criteria.contributor.SegmentsCriteriaContributor;
import com.liferay.segments.model.SegmentsEntry;
import com.liferay.segments.provider.SegmentsEntryProvider;
import com.liferay.segments.service.SegmentsEntryRelLocalService;
import com.liferay.segments.test.util.SegmentsTestUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eduardo García
 */
@RunWith(Arquillian.class)
public class DefaultSegmentsEntryProviderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testGetSegmentsEntryClassPKsWithMultipleCriterion()
		throws Exception {

		Organization organization = OrganizationTestUtil.addOrganization();

		_organizations.add(organization);

		_user1 = UserTestUtil.addOrganizationUser(
			organization, RoleConstants.ORGANIZATION_USER);

		_user2 = UserTestUtil.addUser();

		Criteria criteria = new Criteria();

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", _user1.getFirstName()),
			Criteria.Conjunction.AND);

		_userOrganizationSegmentsCriteriaContributor.contribute(
			criteria, String.format("(name eq '%s')", organization.getName()),
			Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Assert.assertEquals(
			1,
			_segmentsEntryProvider.getSegmentsEntryClassPKsCount(
				segmentsEntry.getSegmentsEntryId()));
		Assert.assertArrayEquals(
			new long[] {_user1.getUserId()},
			_segmentsEntryProvider.getSegmentsEntryClassPKs(
				segmentsEntry.getSegmentsEntryId(), 0, 1));
	}

	@Test
	public void testGetSegmentsEntryClassPKsWithMultipleCriterionNotMatching()
		throws Exception {

		Organization organization = OrganizationTestUtil.addOrganization();

		_organizations.add(organization);

		_user1 = UserTestUtil.addUser();
		_user2 = UserTestUtil.addUser();

		Criteria criteria = new Criteria();

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", _user1.getFirstName()),
			Criteria.Conjunction.AND);

		_userOrganizationSegmentsCriteriaContributor.contribute(
			criteria, String.format("(name eq '%s')", organization.getName()),
			Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Assert.assertEquals(
			0,
			_segmentsEntryProvider.getSegmentsEntryClassPKsCount(
				segmentsEntry.getSegmentsEntryId()));
	}

	@Test
	public void testGetSegmentsEntryClassPKsWithoutCriteria() throws Exception {
		_user1 = UserTestUtil.addUser(_group.getGroupId());
		_user2 = UserTestUtil.addUser(_group.getGroupId());

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(new Criteria()),
			User.class.getName());

		_segmentsEntryRelLocalService.addSegmentsEntryRel(
			segmentsEntry.getSegmentsEntryId(),
			_portal.getClassNameId(User.class.getName()), _user1.getUserId(),
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertEquals(
			1,
			_segmentsEntryProvider.getSegmentsEntryClassPKsCount(
				segmentsEntry.getSegmentsEntryId()));
		Assert.assertArrayEquals(
			new long[] {_user1.getUserId()},
			_segmentsEntryProvider.getSegmentsEntryClassPKs(
				segmentsEntry.getSegmentsEntryId(), 0, 1));
	}

	@Test
	public void testGetSegmentsEntryClassPKsWithSingleCriterion()
		throws Exception {

		_user1 = UserTestUtil.addUser(_group.getGroupId());
		_user2 = UserTestUtil.addUser(_group.getGroupId());

		Criteria criteria = new Criteria();

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", _user1.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Assert.assertEquals(
			1,
			_segmentsEntryProvider.getSegmentsEntryClassPKsCount(
				segmentsEntry.getSegmentsEntryId()));
		Assert.assertArrayEquals(
			new long[] {_user1.getUserId()},
			_segmentsEntryProvider.getSegmentsEntryClassPKs(
				segmentsEntry.getSegmentsEntryId(), 0, 1));
	}

	@Test
	public void testGetSegmentsEntryIdsWithContextCriterionAndDefaultUser()
		throws Exception {

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "en");
		context.put(Context.SIGNED_IN, false);

		Assert.assertArrayEquals(
			new long[] {segmentsEntry.getSegmentsEntryId()},
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithContextCriterionAndDefaultUserWithoutSignedInContext()
		throws Exception {

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "en");

		Assert.assertArrayEquals(
			new long[] {segmentsEntry.getSegmentsEntryId()},
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithContextCriterionAndModelCriterion()
		throws Exception {

		_user1 = UserTestUtil.addUser(_group.getGroupId());
		_user2 = UserTestUtil.addUser(_group.getGroupId());

		Criteria criteria1 = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria1, "(languageId eq 'en')", Criteria.Conjunction.AND);

		_userSegmentsCriteriaContributor.contribute(
			criteria1,
			String.format("(firstName eq '%s')", _user1.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry1 = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), User.class.getName(), _user1.getUserId());
		SegmentsEntry segmentsEntry2 = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria1),
			User.class.getName());

		Criteria criteria2 = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria2, "(languageId eq 'en')", Criteria.Conjunction.OR);

		_userSegmentsCriteriaContributor.contribute(
			criteria2,
			String.format("(firstName eq '%s')", _user2.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry3 = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria2),
			User.class.getName());

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "en");
		context.put(Context.SIGNED_IN, true);

		long[] segmentsEntryIds = _segmentsEntryProvider.getSegmentsEntryIds(
			_group.getGroupId(), User.class.getName(), _user1.getUserId(),
			context);

		Assert.assertEquals(
			StringUtil.merge(segmentsEntryIds, StringPool.COMMA), 3,
			segmentsEntryIds.length);
		Assert.assertTrue(
			ArrayUtil.containsAll(
				new long[] {
					segmentsEntry1.getSegmentsEntryId(),
					segmentsEntry2.getSegmentsEntryId(),
					segmentsEntry3.getSegmentsEntryId()
				},
				segmentsEntryIds));
	}

	@Test
	public void testGetSegmentsEntryIdsWithContextCriterionAndModelCriterionAndDefaultUser()
		throws Exception {

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.AND);

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", defaultUser.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "en");
		context.put(Context.SIGNED_IN, false);

		Assert.assertArrayEquals(
			new long[0],
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithContextCriterionAndModelCriterionAndDefaultUserWithoutSignedInContext()
		throws Exception {

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.AND);

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", defaultUser.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "en");

		Assert.assertArrayEquals(
			new long[0],
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithContextCriterionOrModelCriterionAndDefaultUser()
		throws Exception {

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.OR);

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", defaultUser.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "en");
		context.put(Context.SIGNED_IN, false);

		Assert.assertArrayEquals(
			new long[] {segmentsEntry.getSegmentsEntryId()},
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithContextCriterionOrModelCriterionAndDefaultUserWithoutSignedInContext()
		throws Exception {

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.OR);

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", defaultUser.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "en");

		Assert.assertArrayEquals(
			new long[] {segmentsEntry.getSegmentsEntryId()},
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithModelCriterionAndDefaultUser()
		throws Exception {

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		Criteria criteria = new Criteria();

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", defaultUser.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Context context = new Context();

		context.put(Context.SIGNED_IN, false);

		Assert.assertArrayEquals(
			new long[0],
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithMultipleModelCriterion()
		throws Exception {

		Organization organization = OrganizationTestUtil.addOrganization();

		_organizations.add(organization);

		_user1 = UserTestUtil.addOrganizationUser(
			organization, RoleConstants.ORGANIZATION_USER);

		_user2 = UserTestUtil.addUser(_group.getGroupId());

		Criteria criteria1 = new Criteria();

		_userSegmentsCriteriaContributor.contribute(
			criteria1,
			String.format("(firstName eq '%s')", _user1.getFirstName()),
			Criteria.Conjunction.AND);

		_userOrganizationSegmentsCriteriaContributor.contribute(
			criteria1, String.format("(name eq '%s')", organization.getName()),
			Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry1 = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), User.class.getName(), _user1.getUserId());
		SegmentsEntry segmentsEntry2 = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria1),
			User.class.getName());

		Criteria criteria2 = new Criteria();

		_userSegmentsCriteriaContributor.contribute(
			criteria2,
			String.format("(firstName eq '%s')", _user2.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), User.class.getName(), _user2.getUserId());
		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria2),
			User.class.getName());

		long[] segmentsEntryIds = _segmentsEntryProvider.getSegmentsEntryIds(
			_group.getGroupId(), User.class.getName(), _user1.getUserId());

		Assert.assertEquals(
			StringUtil.merge(segmentsEntryIds, StringPool.COMMA), 2,
			segmentsEntryIds.length);
		Assert.assertTrue(
			ArrayUtil.containsAll(
				new long[] {
					segmentsEntry1.getSegmentsEntryId(),
					segmentsEntry2.getSegmentsEntryId()
				},
				segmentsEntryIds));
	}

	@Test
	public void testGetSegmentsEntryIdsWithNonmatchingContextCriterionAndDefaultUser()
		throws Exception {

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "es");
		context.put(Context.SIGNED_IN, false);

		Assert.assertArrayEquals(
			new long[0],
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithNonmatchingContextCriterionAndDefaultUserWithoutSignedInContext()
		throws Exception {

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "es");

		Assert.assertArrayEquals(
			new long[0],
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithNonmatchingContextCriterionAndMatchingModelCriterion()
		throws Exception {

		_user1 = UserTestUtil.addUser(_group.getGroupId());

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.AND);

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", _user1.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Context context = new Context();

		context.put("languageId", "es");

		Assert.assertArrayEquals(
			new long[0],
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(), _user1.getUserId(),
				context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithNonmatchingContextCriterionAndModelCriterionAndDefaultUser()
		throws Exception {

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.AND);

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", defaultUser.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "es");
		context.put(Context.SIGNED_IN, false);

		Assert.assertArrayEquals(
			new long[0],
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithNonmatchingContextCriterionAndModelCriterionAndDefaultUserWithoutSignedInContext()
		throws Exception {

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.AND);

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", defaultUser.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "es");

		Assert.assertArrayEquals(
			new long[0],
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithNonmatchingContextCriterionOrModelCriterionAndDefaultUser()
		throws Exception {

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.OR);

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", defaultUser.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "es");
		context.put(Context.SIGNED_IN, false);

		Assert.assertArrayEquals(
			new long[0],
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithNonmatchingContextCriterionOrModelCriterionAndDefaultUserWithoutSignedInContext()
		throws Exception {

		Criteria criteria = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria, "(languageId eq 'en')", Criteria.Conjunction.OR);

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		User defaultUser = company.getDefaultUser();

		_userSegmentsCriteriaContributor.contribute(
			criteria,
			String.format("(firstName eq '%s')", defaultUser.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "es");

		Assert.assertArrayEquals(
			new long[0],
			_segmentsEntryProvider.getSegmentsEntryIds(
				_group.getGroupId(), User.class.getName(),
				defaultUser.getUserId(), context));
	}

	@Test
	public void testGetSegmentsEntryIdsWithSingleContextCriterion()
		throws Exception {

		_user1 = UserTestUtil.addUser(_group.getGroupId());
		_user2 = UserTestUtil.addUser(_group.getGroupId());

		Criteria criteria1 = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria1, "(languageId eq 'en')", Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry1 = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), User.class.getName(), _user1.getUserId());
		SegmentsEntry segmentsEntry2 = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria1),
			User.class.getName());

		Criteria criteria2 = new Criteria();

		_contextSegmentsCriteriaContributor.contribute(
			criteria2, "(languageId eq 'fr')", Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria2),
			User.class.getName());

		Context context = new Context();

		context.put(Context.LANGUAGE_ID, "en");
		context.put(Context.SIGNED_IN, true);

		long[] segmentsEntryIds = _segmentsEntryProvider.getSegmentsEntryIds(
			_group.getGroupId(), User.class.getName(), _user1.getUserId(),
			context);

		Assert.assertEquals(
			StringUtil.merge(segmentsEntryIds, StringPool.COMMA), 2,
			segmentsEntryIds.length);
		Assert.assertTrue(
			ArrayUtil.containsAll(
				segmentsEntryIds,
				new long[] {
					segmentsEntry1.getSegmentsEntryId(),
					segmentsEntry2.getSegmentsEntryId()
				}));
	}

	@Test
	public void testGetSegmentsEntryIdsWithSingleModelCriterion()
		throws Exception {

		_user1 = UserTestUtil.addUser(_group.getGroupId());
		_user2 = UserTestUtil.addUser(_group.getGroupId());

		Criteria criteria1 = new Criteria();

		_userSegmentsCriteriaContributor.contribute(
			criteria1,
			String.format("(firstName eq '%s')", _user1.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry1 = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), User.class.getName(), _user1.getUserId());
		SegmentsEntry segmentsEntry2 = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria1),
			User.class.getName());

		Criteria criteria2 = new Criteria();

		_userSegmentsCriteriaContributor.contribute(
			criteria2,
			String.format("(firstName eq '%s')", _user2.getFirstName()),
			Criteria.Conjunction.AND);

		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), User.class.getName(), _user2.getUserId());
		SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria2),
			User.class.getName());

		long[] segmentsEntryIds = _segmentsEntryProvider.getSegmentsEntryIds(
			_group.getGroupId(), User.class.getName(), _user1.getUserId());

		Assert.assertEquals(
			StringUtil.merge(segmentsEntryIds, StringPool.COMMA), 2,
			segmentsEntryIds.length);
		Assert.assertTrue(
			ArrayUtil.containsAll(
				new long[] {
					segmentsEntry1.getSegmentsEntryId(),
					segmentsEntry2.getSegmentsEntryId()
				},
				segmentsEntryIds));
	}

	@Test
	public void testGetSegmentsEntryIdsWithUserModelCriterionAndUserCreatedAfterSegmentsEntry()
		throws Exception {

		Criteria criteria = new Criteria();

		String firstName = RandomTestUtil.randomString();

		_userSegmentsCriteriaContributor.contribute(
			criteria, String.format("(firstName eq '%s')", firstName),
			Criteria.Conjunction.AND);

		SegmentsEntry segmentsEntry = SegmentsTestUtil.addSegmentsEntry(
			_group.getGroupId(), CriteriaSerializer.serialize(criteria),
			User.class.getName());

		_user1 = UserTestUtil.addUser(
			RandomTestUtil.randomString(), LocaleUtil.US, firstName,
			RandomTestUtil.randomString(), new long[0]);

		long[] segmentsEntryIds = _segmentsEntryProvider.getSegmentsEntryIds(
			_group.getGroupId(), User.class.getName(), _user1.getUserId());

		Assert.assertArrayEquals(
			new long[] {segmentsEntry.getSegmentsEntryId()}, segmentsEntryIds);
		Assert.assertEquals(
			StringUtil.merge(segmentsEntryIds, StringPool.COMMA), 1,
			segmentsEntryIds.length);
	}

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject(
		filter = "segments.criteria.contributor.key=context",
		type = SegmentsCriteriaContributor.class
	)
	private SegmentsCriteriaContributor _contextSegmentsCriteriaContributor;

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private final List<Organization> _organizations = new ArrayList<>();

	@Inject
	private Portal _portal;

	@Inject(
		filter = "segments.entry.provider.source=" + SegmentsEntryConstants.SOURCE_DEFAULT,
		type = SegmentsEntryProvider.class
	)
	private SegmentsEntryProvider _segmentsEntryProvider;

	@Inject
	private SegmentsEntryRelLocalService _segmentsEntryRelLocalService;

	@DeleteAfterTestRun
	private User _user1;

	@DeleteAfterTestRun
	private User _user2;

	@Inject
	private UserLocalService _userLocalService;

	@Inject(
		filter = "segments.criteria.contributor.key=user-organization",
		type = SegmentsCriteriaContributor.class
	)
	private SegmentsCriteriaContributor
		_userOrganizationSegmentsCriteriaContributor;

	@Inject(
		filter = "segments.criteria.contributor.key=user",
		type = SegmentsCriteriaContributor.class
	)
	private SegmentsCriteriaContributor _userSegmentsCriteriaContributor;

}