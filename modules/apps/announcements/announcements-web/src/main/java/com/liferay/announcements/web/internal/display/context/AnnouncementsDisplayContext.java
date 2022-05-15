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

package com.liferay.announcements.web.internal.display.context;

import com.liferay.announcements.kernel.model.AnnouncementsEntry;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.display.context.DisplayContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.UserGroup;

import java.text.Format;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Adolfo Pérez
 */
public interface AnnouncementsDisplayContext extends DisplayContext {

	public LinkedHashMap<Long, long[]> getAnnouncementScopes()
		throws PortalException;

	public Format getDateFormatDate();

	public List<Group> getGroups() throws PortalException;

	public List<Organization> getOrganizations() throws PortalException;

	public int getPageDelta();

	public List<Role> getRoles() throws PortalException;

	public SearchContainer<AnnouncementsEntry> getSearchContainer()
		throws PortalException;

	public String getTabs1Names();

	public String getTabs1PortletURL();

	public List<UserGroup> getUserGroups() throws PortalException;

	public boolean isCustomizeAnnouncementsDisplayed();

	public boolean isScopeGroupSelected(Group scopeGroup);

	public boolean isScopeOrganizationSelected(Organization organization);

	public boolean isScopeRoleSelected(Role role);

	public boolean isScopeUserGroupSelected(UserGroup userGroup);

	public boolean isShowReadEntries();

	public boolean isShowScopeName();

	public boolean isTabs1Visible();

}