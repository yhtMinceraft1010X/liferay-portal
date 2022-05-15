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

package com.liferay.sharing.internal.model.listener.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.test.rule.SynchronousMailTestRule;
import com.liferay.sharing.model.SharingEntry;
import com.liferay.sharing.security.permission.SharingEntryAction;
import com.liferay.sharing.service.SharingEntryLocalService;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Sergio González
 */
@RunWith(Arquillian.class)
public class UserModelListenerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE,
			SynchronousMailTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_company = CompanyTestUtil.addCompany();

		_user = UserTestUtil.addCompanyAdminUser(_company);

		_group = GroupTestUtil.addGroup(
			_company.getCompanyId(), _user.getUserId(),
			GroupConstants.DEFAULT_PARENT_GROUP_ID);

		_groupUser = UserTestUtil.addGroupUser(
			_group, RoleConstants.POWER_USER);
	}

	@Test
	public void testDeletingUserSharedDeletesSharingEntries() throws Exception {
		long classNameId = _classNameLocalService.getClassNameId(
			Group.class.getName());
		long classPK = _group.getGroupId();

		_sharingEntryLocalService.addSharingEntry(
			_user.getUserId(), _groupUser.getUserId(), classNameId, classPK,
			_group.getGroupId(), true, Arrays.asList(SharingEntryAction.VIEW),
			null,
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _user.getUserId()));

		List<SharingEntry> toUserSharingEntries =
			_sharingEntryLocalService.getToUserSharingEntries(
				_groupUser.getUserId());

		Assert.assertEquals(
			toUserSharingEntries.toString(), 1, toUserSharingEntries.size());

		_userLocalService.deleteUser(_groupUser.getUserId());

		toUserSharingEntries =
			_sharingEntryLocalService.getToUserSharingEntries(
				_groupUser.getUserId());

		Assert.assertEquals(
			toUserSharingEntries.toString(), 0, toUserSharingEntries.size());
	}

	@Test
	public void testDeletingUserSharingDoesNotDeleteSharingEntries()
		throws Exception {

		long classNameId = _classNameLocalService.getClassNameId(
			Group.class.getName());
		long classPK = _group.getGroupId();

		_sharingEntryLocalService.addSharingEntry(
			_user.getUserId(), _groupUser.getUserId(), classNameId, classPK,
			_group.getGroupId(), true, Arrays.asList(SharingEntryAction.VIEW),
			null,
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _user.getUserId()));

		List<SharingEntry> toUserSharingEntries =
			_sharingEntryLocalService.getToUserSharingEntries(
				_groupUser.getUserId());

		Assert.assertEquals(
			toUserSharingEntries.toString(), 1, toUserSharingEntries.size());

		_userLocalService.deleteUser(_user.getUserId());

		toUserSharingEntries =
			_sharingEntryLocalService.getToUserSharingEntries(
				_groupUser.getUserId());

		Assert.assertEquals(
			toUserSharingEntries.toString(), 1, toUserSharingEntries.size());
	}

	@Inject
	private ClassNameLocalService _classNameLocalService;

	@DeleteAfterTestRun
	private Company _company;

	private Group _group;
	private User _groupUser;

	@Inject
	private SharingEntryLocalService _sharingEntryLocalService;

	private User _user;

	@Inject
	private UserLocalService _userLocalService;

}