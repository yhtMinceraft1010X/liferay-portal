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

package com.liferay.site.util.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletActionRequest;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletURL;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.usersadmin.search.GroupSearch;
import com.liferay.site.util.GroupSearchProvider;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lourdes Fernández Besada
 */
@RunWith(Arquillian.class)
public class GroupSearchProviderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@BeforeClass
	public static void setUpClass() {
		_originalGroupsComplexSQLClassNames =
			PropsValues.GROUPS_COMPLEX_SQL_CLASS_NAMES;
		_originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();
	}

	@AfterClass
	public static void tearDownClass() {
		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "GROUPS_COMPLEX_SQL_CLASS_NAMES",
			_originalGroupsComplexSQLClassNames);

		PermissionThreadLocal.setPermissionChecker(_originalPermissionChecker);
	}

	@Test
	public void testSearchPrivateMembershipGroups() throws Exception {
		Group parentGroup = GroupTestUtil.addGroup();

		Group childGroup1 = GroupTestUtil.addGroup(parentGroup.getGroupId());

		childGroup1.setType(GroupConstants.TYPE_SITE_PRIVATE);

		_groupLocalService.updateGroup(childGroup1);

		Group childGroup2 = GroupTestUtil.addGroup(parentGroup.getGroupId());

		childGroup2.setType(GroupConstants.TYPE_SITE_PRIVATE);

		_groupLocalService.updateGroup(childGroup2);

		Group childGroup3 = GroupTestUtil.addGroup(parentGroup.getGroupId());

		childGroup3.setType(GroupConstants.TYPE_SITE_PRIVATE);

		_groupLocalService.updateGroup(childGroup3);

		User user = UserTestUtil.addGroupUser(
			parentGroup, RoleConstants.SITE_MEMBER);

		Role role = RoleLocalServiceUtil.getRole(
			childGroup1.getCompanyId(), RoleConstants.SITE_MEMBER);

		UserGroupRoleLocalServiceUtil.addUserGroupRoles(
			new long[] {user.getUserId()}, childGroup1.getGroupId(),
			role.getRoleId());

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user));

		MockLiferayPortletActionRequest mockLiferayPortletActionRequest =
			new MockLiferayPortletActionRequest();

		mockLiferayPortletActionRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay(parentGroup, user));
		mockLiferayPortletActionRequest.setParameter(
			"groupId", String.valueOf(parentGroup.getGroupId()));

		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "GROUPS_COMPLEX_SQL_CLASS_NAMES",
			new String[] {"com.liferay.portal.kernel.model.User"});

		_assertGroupSearch(
			childGroup1,
			_groupSearchProvider.getGroupSearch(
				mockLiferayPortletActionRequest, new MockLiferayPortletURL()));

		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "GROUPS_COMPLEX_SQL_CLASS_NAMES",
			new String[] {
				"com.liferay.portal.kernel.model.User",
				"com.liferay.portal.kernel.model.Organization",
				"com.liferay.portal.kernel.model.UserGroup",
				"com.liferay.portal.kernel.model.Company"
			});

		GroupSearch complexSQLGroupSearch = _groupSearchProvider.getGroupSearch(
			mockLiferayPortletActionRequest, new MockLiferayPortletURL());

		_assertGroupSearch(childGroup1, complexSQLGroupSearch);
	}

	private void _assertGroupSearch(
		Group childGroup1, GroupSearch groupSearch) {

		List<Group> results = groupSearch.getResults();

		Assert.assertEquals(results.toString(), 1, results.size());

		Group group = results.get(0);

		Assert.assertEquals(childGroup1.getGroupId(), group.getGroupId());
	}

	private ThemeDisplay _getThemeDisplay(Group group, User user)
		throws Exception {

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(
			_companyLocalService.getCompany(group.getCompanyId()));
		themeDisplay.setLocale(LocaleUtil.getDefault());
		themeDisplay.setPermissionChecker(
			PermissionThreadLocal.getPermissionChecker());
		themeDisplay.setScopeGroupId(group.getGroupId());
		themeDisplay.setUser(user);

		return themeDisplay;
	}

	private static String[] _originalGroupsComplexSQLClassNames;
	private static PermissionChecker _originalPermissionChecker;

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private GroupLocalService _groupLocalService;

	@Inject
	private GroupSearchProvider _groupSearchProvider;

}