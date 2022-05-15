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

package com.liferay.google.places.util;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.PrefsProps;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import javax.portlet.PortletPreferences;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * @author Rodrigo Paulino
 */
public class GooglePlacesUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		_setUpPrefsPropsUtil();
	}

	@Test
	public void testGetCompanyGooglePlacesAPIKey() {
		Assert.assertEquals(
			_COMPANY_GOOGLE_PLACES_API_KEY,
			GooglePlacesUtil.getGooglePlacesAPIKey(0));
	}

	@Test
	public void testGetGroupGooglePlacesAPIKey1() {
		Assert.assertEquals(
			_COMPANY_GOOGLE_PLACES_API_KEY,
			GooglePlacesUtil.getGooglePlacesAPIKey(
				0, 0, _mockGroupLocalService(false, false)));
	}

	@Test
	public void testGetGroupGooglePlacesAPIKey2() {
		Assert.assertEquals(
			_GROUP_GOOGLE_PLACES_API_KEY,
			GooglePlacesUtil.getGooglePlacesAPIKey(
				0, 0, _mockGroupLocalService(true, false)));
	}

	@Test
	public void testGetGroupGooglePlacesAPIKey3() {
		Assert.assertEquals(
			_LIVE_GROUP_GOOGLE_PLACES_API_KEY,
			GooglePlacesUtil.getGooglePlacesAPIKey(
				0, 0, _mockGroupLocalService(true, true)));
	}

	private static PortletPreferences _mockPortletPreferences() {
		PortletPreferences portletPreferences = Mockito.mock(
			PortletPreferences.class);

		Mockito.when(
			portletPreferences.getValue(
				Matchers.anyString(), Matchers.anyString())
		).thenReturn(
			_COMPANY_GOOGLE_PLACES_API_KEY
		);

		return portletPreferences;
	}

	private static void _setUpPrefsPropsUtil() {
		PrefsPropsUtil prefsPropsUtil = new PrefsPropsUtil();

		PrefsProps prefsProps = Mockito.mock(PrefsProps.class);

		PortletPreferences portletPreferences = _mockPortletPreferences();

		Mockito.when(
			prefsProps.getPreferences(Matchers.anyLong())
		).thenReturn(
			portletPreferences
		);

		prefsPropsUtil.setPrefsProps(prefsProps);
	}

	private Group _mockGroup(boolean stagingGroup, String googlePlacesAPIKey) {
		Group group = Mockito.mock(Group.class);

		if (!_LIVE_GROUP_GOOGLE_PLACES_API_KEY.equals(googlePlacesAPIKey)) {
			Mockito.when(
				group.isStagingGroup()
			).thenReturn(
				stagingGroup
			);
		}

		if (stagingGroup) {
			Group liveGroup = _mockGroup(
				false, _LIVE_GROUP_GOOGLE_PLACES_API_KEY);

			Mockito.when(
				group.getLiveGroup()
			).thenReturn(
				liveGroup
			);
		}
		else {
			Mockito.when(
				group.getTypeSettingsProperty(Matchers.anyString())
			).thenReturn(
				googlePlacesAPIKey
			);
		}

		return group;
	}

	private GroupLocalService _mockGroupLocalService(
		boolean mockGroup, boolean stagingGroup) {

		GroupLocalService groupLocalService = Mockito.mock(
			GroupLocalService.class);

		if (mockGroup) {
			Group group = _mockGroup(
				stagingGroup, _GROUP_GOOGLE_PLACES_API_KEY);

			Mockito.when(
				groupLocalService.fetchGroup(Matchers.anyLong())
			).thenReturn(
				group
			);
		}

		return groupLocalService;
	}

	private static final String _COMPANY_GOOGLE_PLACES_API_KEY =
		"companyGooglePlacesAPIKey";

	private static final String _GROUP_GOOGLE_PLACES_API_KEY =
		"groupGooglePlacesAPIKey";

	private static final String _LIVE_GROUP_GOOGLE_PLACES_API_KEY =
		"liveGroupGooglePlacesAPIKey";

}