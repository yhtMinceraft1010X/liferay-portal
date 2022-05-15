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

package com.liferay.staging.test;

import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationParameterMapFactoryUtil;
import com.liferay.exportimport.kernel.service.StagingLocalServiceUtil;
import com.liferay.exportimport.kernel.staging.StagingUtil;
import com.liferay.exportimport.kernel.staging.constants.StagingConstants;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;

import java.io.Serializable;

import java.util.Map;

import org.junit.Before;

/**
 * @author Tamas Molnar
 */
public abstract class BaseLocalStagingTestCase {

	@Before
	public void setUp() throws Exception {
		UserTestUtil.setUser(TestPropsValues.getUser());

		liveGroup = GroupTestUtil.addGroup();

		_enableLocalStaging();

		stagingGroup = liveGroup.getStagingGroup();

		stagingLayout = LayoutTestUtil.addTypePortletLayout(stagingGroup);

		publishLayouts();

		liveLayout = LayoutLocalServiceUtil.getLayoutByUuidAndGroupId(
			stagingLayout.getUuid(), liveGroup.getGroupId(),
			stagingLayout.isPrivateLayout());
	}

	protected String[] getNotStagedPortletIds() {
		return new String[0];
	}

	protected void publishLayouts() throws PortalException {
		publishLayouts(
			ExportImportConfigurationParameterMapFactoryUtil.
				buildParameterMap());
	}

	protected void publishLayouts(Map<String, String[]> parameterMap)
		throws PortalException {

		StagingUtil.publishLayouts(
			TestPropsValues.getUserId(), stagingGroup.getGroupId(),
			liveGroup.getGroupId(), false, parameterMap);
	}

	protected void publishPortlet(String portletId) throws PortalException {
		StagingUtil.publishPortlet(
			TestPropsValues.getUserId(), stagingGroup.getGroupId(),
			liveGroup.getGroupId(), stagingLayout.getPlid(),
			liveLayout.getPlid(), portletId,
			ExportImportConfigurationParameterMapFactoryUtil.
				buildParameterMap());
	}

	protected void setPortletStagingEnabled(
		boolean enabled, String portletId, ServiceContext serviceContext) {

		serviceContext.setAttribute(
			StringBundler.concat(
				StagingConstants.STAGED_PREFIX, StagingConstants.STAGED_PORTLET,
				portletId, StringPool.DOUBLE_DASH),
			Boolean.FALSE.toString());
	}

	@DeleteAfterTestRun
	protected Group liveGroup;

	protected Layout liveLayout;
	protected Group stagingGroup;
	protected Layout stagingLayout;

	private void _enableLocalStaging() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(liveGroup.getGroupId());

		Map<String, Serializable> attributes = serviceContext.getAttributes();

		attributes.putAll(
			ExportImportConfigurationParameterMapFactoryUtil.
				buildParameterMap());

		for (String portletId : getNotStagedPortletIds()) {
			setPortletStagingEnabled(false, portletId, serviceContext);
		}

		StagingLocalServiceUtil.enableLocalStaging(
			TestPropsValues.getUserId(), liveGroup, false, false,
			serviceContext);
	}

}