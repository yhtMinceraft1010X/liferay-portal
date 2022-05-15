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

package com.liferay.dynamic.data.mapping.form.web.internal.display.context.util;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecordVersion;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceSettings;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordVersionLocalService;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * @author Carolina Barbosa
 */
public class DDMFormInstanceSubmissionLimitStatusUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testIsLimitToOneSubmissionPerUser() throws Exception {
		Assert.assertFalse(
			DDMFormInstanceSubmissionLimitStatusUtil.
				isLimitToOneSubmissionPerUser(
					_mockDDMFormInstance(_mockDDMFormInstanceSettings(false))));
		Assert.assertTrue(
			DDMFormInstanceSubmissionLimitStatusUtil.
				isLimitToOneSubmissionPerUser(
					_mockDDMFormInstance(_mockDDMFormInstanceSettings(true))));
	}

	@Test
	public void testIsSubmissionLimitReached() throws Exception {
		Assert.assertTrue(
			DDMFormInstanceSubmissionLimitStatusUtil.isSubmissionLimitReached(
				_mockDDMFormInstance(_mockDDMFormInstanceSettings(true)),
				_mockDDMFormInstanceRecordVersionLocalService(
					Collections.singletonList(
						Mockito.mock(DDMFormInstanceRecordVersion.class))),
				Mockito.mock(User.class)));
	}

	@Test
	public void testIsSubmissionLimitReachedWithDefaultUser() throws Exception {
		User user = Mockito.mock(User.class);

		Mockito.when(
			user.isDefaultUser()
		).thenReturn(
			true
		);

		Assert.assertFalse(
			DDMFormInstanceSubmissionLimitStatusUtil.isSubmissionLimitReached(
				_mockDDMFormInstance(_mockDDMFormInstanceSettings(true)),
				_mockDDMFormInstanceRecordVersionLocalService(
					Collections.singletonList(
						Mockito.mock(DDMFormInstanceRecordVersion.class))),
				user));
	}

	@Test
	public void testIsSubmissionLimitReachedWithNoRecord() throws Exception {
		Assert.assertFalse(
			DDMFormInstanceSubmissionLimitStatusUtil.isSubmissionLimitReached(
				_mockDDMFormInstance(_mockDDMFormInstanceSettings(true)),
				_mockDDMFormInstanceRecordVersionLocalService(
					Collections.emptyList()),
				Mockito.mock(User.class)));
	}

	@Test
	public void testIsSubmissionLimitReachedWithStatusDraft() throws Exception {
		DDMFormInstanceRecordVersion ddmFormInstanceRecordVersion =
			Mockito.mock(DDMFormInstanceRecordVersion.class);

		Mockito.when(
			ddmFormInstanceRecordVersion.getStatus()
		).thenReturn(
			WorkflowConstants.STATUS_DRAFT
		);

		Assert.assertFalse(
			DDMFormInstanceSubmissionLimitStatusUtil.isSubmissionLimitReached(
				_mockDDMFormInstance(_mockDDMFormInstanceSettings(true)),
				_mockDDMFormInstanceRecordVersionLocalService(
					Collections.singletonList(ddmFormInstanceRecordVersion)),
				Mockito.mock(User.class)));
	}

	private DDMFormInstance _mockDDMFormInstance(
			DDMFormInstanceSettings ddmFormInstanceSettings)
		throws Exception {

		DDMFormInstance ddmFormInstance = Mockito.mock(DDMFormInstance.class);

		Mockito.when(
			ddmFormInstance.getSettingsModel()
		).thenReturn(
			ddmFormInstanceSettings
		);

		return ddmFormInstance;
	}

	private DDMFormInstanceRecordVersionLocalService
		_mockDDMFormInstanceRecordVersionLocalService(
			List<DDMFormInstanceRecordVersion> ddmFormInstanceRecordVersions) {

		DDMFormInstanceRecordVersionLocalService
			ddmFormInstanceRecordVersionLocalService = Mockito.mock(
				DDMFormInstanceRecordVersionLocalService.class);

		Mockito.when(
			ddmFormInstanceRecordVersionLocalService.
				getFormInstanceRecordVersions(
					Matchers.anyLong(), Matchers.anyLong())
		).thenReturn(
			ddmFormInstanceRecordVersions
		);

		return ddmFormInstanceRecordVersionLocalService;
	}

	private DDMFormInstanceSettings _mockDDMFormInstanceSettings(
		boolean limitToOneSubmissionPerUser) {

		DDMFormInstanceSettings ddmFormInstanceSettings = Mockito.mock(
			DDMFormInstanceSettings.class);

		Mockito.when(
			ddmFormInstanceSettings.limitToOneSubmissionPerUser()
		).thenReturn(
			limitToOneSubmissionPerUser
		);

		return ddmFormInstanceSettings;
	}

}