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

package com.liferay.portlet.configuration.web.internal.display.context;

import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.ResourcePrimKeyException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletConstants;
import com.liferay.portal.kernel.model.Resource;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourceLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.configuration.web.internal.configuration.RoleVisibilityConfiguration;
import com.liferay.portlet.configuration.web.internal.constants.PortletConfigurationPortletKeys;
import com.liferay.portlet.rolesadmin.search.RoleSearch;
import com.liferay.portlet.rolesadmin.search.RoleSearchTerms;
import com.liferay.roles.admin.role.type.contributor.RoleTypeContributor;
import com.liferay.roles.admin.role.type.contributor.provider.RoleTypeContributorProvider;
import com.liferay.sites.kernel.util.SitesUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Eudaldo Alonso
 */
public class PortletConfigurationPermissionsDisplayContext {

	public PortletConfigurationPermissionsDisplayContext(
			HttpServletRequest httpServletRequest, RenderRequest renderRequest,
			RoleTypeContributorProvider roleTypeContributorProvider)
		throws PortalException {

		_httpServletRequest = httpServletRequest;
		_renderRequest = renderRequest;
		_roleTypeContributorProvider = roleTypeContributorProvider;

		long groupId = _getResourceGroupId();

		Group group = GroupLocalServiceUtil.getGroup(groupId);

		Layout selLayout = null;

		if (Objects.equals(getModelResource(), Layout.class.getName())) {
			selLayout = LayoutLocalServiceUtil.getLayout(
				GetterUtil.getLong(getResourcePrimKey()));

			group = selLayout.getGroup();

			groupId = group.getGroupId();
		}

		_selLayout = selLayout;
		_group = group;
		_groupId = groupId;
	}

	public List<String> getActions() throws PortalException {
		if (_actions != null) {
			return _actions;
		}

		List<String> resourceActions = ResourceActionsUtil.getResourceActions(
			_getPortletResource(), getModelResource());

		if (Objects.equals(getModelResource(), Group.class.getName())) {
			Group modelResourceGroup = GroupLocalServiceUtil.getGroup(
				GetterUtil.getLong(getResourcePrimKey()));

			if (modelResourceGroup.isLayoutPrototype() ||
				modelResourceGroup.isLayoutSetPrototype() ||
				modelResourceGroup.isUserGroup()) {

				resourceActions = new ArrayList<>(resourceActions);

				resourceActions.remove(ActionKeys.ADD_LAYOUT_BRANCH);
				resourceActions.remove(ActionKeys.ADD_LAYOUT_SET_BRANCH);
				resourceActions.remove(ActionKeys.ASSIGN_MEMBERS);
				resourceActions.remove(ActionKeys.ASSIGN_USER_ROLES);
				resourceActions.remove(ActionKeys.MANAGE_ANNOUNCEMENTS);
				resourceActions.remove(ActionKeys.MANAGE_STAGING);
				resourceActions.remove(ActionKeys.MANAGE_TEAMS);
				resourceActions.remove(ActionKeys.PUBLISH_STAGING);
				resourceActions.remove(ActionKeys.VIEW_MEMBERS);
				resourceActions.remove(ActionKeys.VIEW_STAGING);
			}
		}
		else if (Objects.equals(getModelResource(), Role.class.getName())) {
			Role modelResourceRole = RoleLocalServiceUtil.getRole(
				GetterUtil.getLong(getResourcePrimKey()));

			String name = modelResourceRole.getName();

			if (name.equals(RoleConstants.GUEST) ||
				name.equals(RoleConstants.USER)) {

				resourceActions = new ArrayList<>(resourceActions);

				resourceActions.remove(ActionKeys.ASSIGN_MEMBERS);
				resourceActions.remove(ActionKeys.DELETE);
				resourceActions.remove(ActionKeys.UPDATE);
			}
		}

		_actions = resourceActions;

		return _actions;
	}

	public String getClearResultsURL() throws Exception {
		return PortletURLBuilder.create(
			getIteratorURL()
		).setKeywords(
			StringPool.BLANK
		).buildString();
	}

	public PortletURL getDefinePermissionsURL() throws Exception {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		LiferayPortletURL liferayPortletURL =
			(LiferayPortletURL)PortletProviderUtil.getPortletURL(
				_httpServletRequest, Role.class.getName(),
				PortletProvider.Action.MANAGE);

		liferayPortletURL.setParameter(Constants.CMD, Constants.VIEW);
		liferayPortletURL.setParameter("backURL", themeDisplay.getURLCurrent());
		liferayPortletURL.setPortletMode(PortletMode.VIEW);
		liferayPortletURL.setRefererPlid(themeDisplay.getPlid());

		liferayPortletURL.setWindowState(LiferayWindowState.POP_UP);

		return liferayPortletURL;
	}

	public String getGroupDescriptiveName() throws PortalException {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return _group.getDescriptiveName(themeDisplay.getLocale());
	}

	public long getGroupId() {
		return _groupId;
	}

	public List<String> getGuestUnsupportedActions() {
		if (_guestUnsupportedActions != null) {
			return _guestUnsupportedActions;
		}

		List<String> guestUnsupportedActions =
			ResourceActionsUtil.getResourceGuestUnsupportedActions(
				_getPortletResource(), getModelResource());

		// LPS-32515

		if ((_selLayout != null) && _group.isGuest() &&
			SitesUtil.isFirstLayout(
				_selLayout.getGroupId(), _selLayout.isPrivateLayout(),
				_selLayout.getLayoutId())) {

			guestUnsupportedActions = new ArrayList<>(guestUnsupportedActions);

			guestUnsupportedActions.add(ActionKeys.VIEW);
		}

		_guestUnsupportedActions = guestUnsupportedActions;

		return _guestUnsupportedActions;
	}

	public PortletURL getIteratorURL() throws Exception {
		return PortletURLBuilder.create(
			PortletURLFactoryUtil.create(
				_httpServletRequest,
				PortletConfigurationPortletKeys.PORTLET_CONFIGURATION,
				PortletRequest.RENDER_PHASE)
		).setMVCPath(
			"/edit_permissions.jsp"
		).setPortletResource(
			_getPortletResource()
		).setParameter(
			"modelResource", getModelResource()
		).setParameter(
			"portletConfiguration", true
		).setParameter(
			"resourceGroupId", _getResourceGroupId()
		).setParameter(
			"resourcePrimKey", getResourcePrimKey()
		).setParameter(
			"returnToFullPageURL", _getReturnToFullPageURL()
		).setParameter(
			"roleTypes", _getRoleTypesParam()
		).setWindowState(
			LiferayWindowState.POP_UP
		).buildPortletURL();
	}

	public String getModelResource() {
		if (_modelResource != null) {
			return _modelResource;
		}

		_modelResource = ParamUtil.getString(
			_httpServletRequest, "modelResource");

		return _modelResource;
	}

	public String getModelResourceDescription() {
		if (_modelResourceDescription != null) {
			return _modelResourceDescription;
		}

		_modelResourceDescription = ParamUtil.getString(
			_httpServletRequest, "modelResourceDescription");

		return _modelResourceDescription;
	}

	public Resource getResource() throws PortalException {
		if (_resource != null) {
			return _resource;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		int count =
			ResourcePermissionLocalServiceUtil.getResourcePermissionsCount(
				themeDisplay.getCompanyId(), getSelResource(),
				ResourceConstants.SCOPE_INDIVIDUAL, getResourcePrimKey());

		if (count == 0) {
			boolean portletActions = Validator.isNull(getModelResource());

			ResourceLocalServiceUtil.addResources(
				themeDisplay.getCompanyId(), getGroupId(), 0, getSelResource(),
				getResourcePrimKey(), portletActions, true, true);
		}

		_resource = ResourceLocalServiceUtil.getResource(
			themeDisplay.getCompanyId(), getSelResource(),
			ResourceConstants.SCOPE_INDIVIDUAL, getResourcePrimKey());

		return _resource;
	}

	public String getResourcePrimKey() throws ResourcePrimKeyException {
		if (_resourcePrimKey != null) {
			return _resourcePrimKey;
		}

		_resourcePrimKey = ParamUtil.getString(
			_httpServletRequest, "resourcePrimKey");

		if (Validator.isNull(_resourcePrimKey)) {
			throw new ResourcePrimKeyException();
		}

		return _resourcePrimKey;
	}

	public SearchContainer<Role> getRoleSearchContainer() throws Exception {
		if (_roleSearchContainer != null) {
			return _roleSearchContainer;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		SearchContainer<Role> roleSearchContainer = new RoleSearch(
			_renderRequest, getIteratorURL());

		RoleSearchTerms searchTerms =
			(RoleSearchTerms)roleSearchContainer.getSearchTerms();

		boolean filterGroupRoles = !ResourceActionsUtil.isPortalModelResource(
			getModelResource());

		if (Objects.equals(getModelResource(), Role.class.getName())) {
			Role modelResourceRole = RoleLocalServiceUtil.getRole(
				GetterUtil.getLong(getResourcePrimKey()));

			RoleTypeContributor roleTypeContributor =
				_roleTypeContributorProvider.getRoleTypeContributor(
					modelResourceRole.getType());

			if (ArrayUtil.isNotEmpty(
					roleTypeContributor.getExcludedRoleNames())) {

				filterGroupRoles = true;
			}
		}

		long modelResourceRoleId = 0;

		if (Objects.equals(getModelResource(), Role.class.getName())) {
			modelResourceRoleId = GetterUtil.getLong(getResourcePrimKey());
		}

		boolean filterGuestRole = false;

		if (Objects.equals(getModelResource(), Layout.class.getName())) {
			Layout resourceLayout = LayoutLocalServiceUtil.getLayout(
				GetterUtil.getLong(getResourcePrimKey()));

			if (resourceLayout.isPrivateLayout()) {
				Group resourceLayoutGroup = resourceLayout.getGroup();

				if (!resourceLayoutGroup.isLayoutSetPrototype() &&
					!PropsValues.PERMISSIONS_CHECK_GUEST_ENABLED) {

					filterGuestRole = true;
				}
			}
		}
		else if (Validator.isNotNull(_getPortletResource())) {
			String resourcePrimKey = getResourcePrimKey();

			int pos = resourcePrimKey.indexOf(
				PortletConstants.LAYOUT_SEPARATOR);

			if (pos > 0) {
				Layout resourceLayout = LayoutLocalServiceUtil.getLayout(
					GetterUtil.getLong(resourcePrimKey.substring(0, pos)));

				if (resourceLayout.isPrivateLayout()) {
					Group resourceLayoutGroup = resourceLayout.getGroup();

					if (!resourceLayoutGroup.isLayoutPrototype() &&
						!resourceLayoutGroup.isLayoutSetPrototype() &&
						!PropsValues.PERMISSIONS_CHECK_GUEST_ENABLED) {

						filterGuestRole = true;
					}
				}
			}
		}

		Set<String> excludedRoleNamesSet = new HashSet<String>() {
			{
				add(RoleConstants.ADMINISTRATOR);
			}
		};

		if (filterGroupRoles) {
			for (RoleTypeContributor roleTypeContributor :
					_roleTypeContributorProvider.getRoleTypeContributors()) {

				Collections.addAll(
					excludedRoleNamesSet,
					roleTypeContributor.getExcludedRoleNames());
			}
		}

		if (filterGuestRole) {
			excludedRoleNamesSet.add(RoleConstants.GUEST);
		}

		List<String> excludedRoleNames = ListUtil.fromCollection(
			excludedRoleNamesSet);

		long teamGroupId = _group.getGroupId();

		if (_group.isLayout()) {
			teamGroupId = _group.getParentGroupId();
		}

		long roleModelResourceRoleId = modelResourceRoleId;

		long roleTeamGroupId = teamGroupId;

		RoleVisibilityConfiguration stricterRoleVisibilityConfiguration =
			ConfigurationProviderUtil.getCompanyConfiguration(
				RoleVisibilityConfiguration.class, themeDisplay.getCompanyId());

		if (Validator.isNull(searchTerms.getKeywords())) {
			if (stricterRoleVisibilityConfiguration.
					restrictPermissionSelectorRoleVisibility()) {

				roleSearchContainer.setResultsAndTotal(
					() -> RoleServiceUtil.getGroupRolesAndTeamRoles(
						themeDisplay.getCompanyId(), searchTerms.getKeywords(),
						excludedRoleNames, getRoleTypes(),
						roleModelResourceRoleId, roleTeamGroupId,
						roleSearchContainer.getStart(),
						roleSearchContainer.getEnd()),
					RoleServiceUtil.getGroupRolesAndTeamRolesCount(
						themeDisplay.getCompanyId(), searchTerms.getKeywords(),
						excludedRoleNames, getRoleTypes(),
						roleModelResourceRoleId, roleTeamGroupId));
			}
			else {
				roleSearchContainer.setResultsAndTotal(
					() -> RoleLocalServiceUtil.getGroupRolesAndTeamRoles(
						themeDisplay.getCompanyId(), searchTerms.getKeywords(),
						excludedRoleNames, getRoleTypes(),
						roleModelResourceRoleId, roleTeamGroupId,
						roleSearchContainer.getStart(),
						roleSearchContainer.getEnd()),
					RoleLocalServiceUtil.getGroupRolesAndTeamRolesCount(
						themeDisplay.getCompanyId(), searchTerms.getKeywords(),
						excludedRoleNames, getRoleTypes(),
						roleModelResourceRoleId, roleTeamGroupId));
			}
		}
		else {
			List<Role> roles = null;

			if (stricterRoleVisibilityConfiguration.
					restrictPermissionSelectorRoleVisibility()) {

				roles = RoleServiceUtil.getGroupRolesAndTeamRoles(
					themeDisplay.getCompanyId(), searchTerms.getKeywords(),
					excludedRoleNames, getRoleTypes(), modelResourceRoleId,
					teamGroupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
			}
			else {
				roles = RoleLocalServiceUtil.getGroupRolesAndTeamRoles(
					themeDisplay.getCompanyId(), searchTerms.getKeywords(),
					excludedRoleNames, getRoleTypes(), modelResourceRoleId,
					teamGroupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
			}

			roleSearchContainer.setResultsAndTotal(
				ListUtil.filter(
					roles,
					role -> {
						String roleName = StringUtil.toLowerCase(
							role.getTitle(themeDisplay.getLocale()),
							themeDisplay.getLocale());

						return (roleName != null) &&
							   roleName.contains(
								   StringUtil.toLowerCase(
									   searchTerms.getKeywords(),
									   themeDisplay.getLocale()));
					}));
		}

		_roleSearchContainer = roleSearchContainer;

		return _roleSearchContainer;
	}

	public int[] getRoleTypes() {
		if (_roleTypes != null) {
			return _roleTypes;
		}

		String roleTypesParam = _getRoleTypesParam();

		if (Validator.isNotNull(roleTypesParam)) {
			_roleTypes = StringUtil.split(roleTypesParam, 0);
		}

		if (_roleTypes != null) {
			return _roleTypes;
		}

		_roleTypes = RoleConstants.TYPES_REGULAR_AND_SITE;

		if (_group.isDepot()) {
			_roleTypes = _TYPES_DEPOT_AND_REGULAR;
		}

		if (ResourceActionsUtil.isPortalModelResource(getModelResource())) {
			if (Objects.equals(
					getModelResource(), Organization.class.getName()) ||
				Objects.equals(getModelResource(), User.class.getName())) {

				_roleTypes = RoleConstants.TYPES_ORGANIZATION_AND_REGULAR;
			}
			else {
				_roleTypes = RoleConstants.TYPES_REGULAR;
			}

			return _roleTypes;
		}

		if (_group == null) {
			return _roleTypes;
		}

		Group parentGroup = null;

		if (_group.isLayout()) {
			parentGroup = GroupLocalServiceUtil.fetchGroup(
				_group.getParentGroupId());
		}

		if (parentGroup != null) {
			_roleTypes = _getGroupRoleTypes(parentGroup, _roleTypes);
		}
		else {
			_roleTypes = _getGroupRoleTypes(_group, _roleTypes);
		}

		return _roleTypes;
	}

	public String getSearchActionURL() throws Exception {
		PortletURL searchActionURL = getIteratorURL();

		return searchActionURL.toString();
	}

	public String getSelResource() {
		_selResource = getModelResource();

		if (Validator.isNotNull(_selResource)) {
			return _selResource;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			themeDisplay.getCompanyId(), _getPortletResource());

		_selResource = portlet.getRootPortletId();

		return _selResource;
	}

	public String getSelResourceDescription() {
		_selResourceDescription = getModelResourceDescription();

		if (Validator.isNotNull(_selResourceDescription)) {
			return _selResourceDescription;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		HttpSession httpSession = _httpServletRequest.getSession();

		_selResourceDescription = PortalUtil.getPortletTitle(
			PortletLocalServiceUtil.getPortletById(
				themeDisplay.getCompanyId(), _getPortletResource()),
			httpSession.getServletContext(), themeDisplay.getLocale());

		return _selResourceDescription;
	}

	public PortletURL getUpdateRolePermissionsURL()
		throws ResourcePrimKeyException, WindowStateException {

		return PortletURLBuilder.create(
			PortletURLFactoryUtil.create(
				_httpServletRequest,
				PortletConfigurationPortletKeys.PORTLET_CONFIGURATION,
				PortletRequest.ACTION_PHASE)
		).setActionName(
			"updateRolePermissions"
		).setMVCPath(
			"/edit_permissions.jsp"
		).setPortletResource(
			_getPortletResource()
		).setParameter(
			"cur",
			ParamUtil.getInteger(
				_httpServletRequest, SearchContainer.DEFAULT_CUR_PARAM)
		).setParameter(
			"delta",
			ParamUtil.getInteger(
				_httpServletRequest, SearchContainer.DEFAULT_DELTA_PARAM)
		).setParameter(
			"modelResource", getModelResource()
		).setParameter(
			"modelResourceDescription", getModelResourceDescription()
		).setParameter(
			"portletConfiguration", true
		).setParameter(
			"resourceGroupId", _getResourceGroupId()
		).setParameter(
			"resourcePrimKey", getResourcePrimKey()
		).setParameter(
			"returnToFullPageURL", _getReturnToFullPageURL()
		).setParameter(
			"roleTypes", _getRoleTypesParam()
		).setWindowState(
			LiferayWindowState.POP_UP
		).buildPortletURL();
	}

	private int[] _getGroupRoleTypes(Group group, int[] defaultRoleTypes) {
		if (group == null) {
			return defaultRoleTypes;
		}

		if (group.isOrganization()) {
			return RoleConstants.TYPES_ORGANIZATION_AND_REGULAR_AND_SITE;
		}

		if (group.isCompany() || group.isUser() || group.isUserGroup()) {
			return RoleConstants.TYPES_REGULAR;
		}

		return defaultRoleTypes;
	}

	private String _getPortletResource() {
		if (_portletResource != null) {
			return _portletResource;
		}

		_portletResource = ParamUtil.getString(
			_httpServletRequest, "portletResource");

		return _portletResource;
	}

	private long _getResourceGroupId() {
		if (_resourceGroupId != null) {
			return _resourceGroupId;
		}

		_resourceGroupId = ParamUtil.getLong(
			_httpServletRequest, "resourceGroupId");

		if (_resourceGroupId == 0) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)_httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			_resourceGroupId = themeDisplay.getScopeGroupId();
		}

		return _resourceGroupId;
	}

	private String _getReturnToFullPageURL() {
		if (_returnToFullPageURL != null) {
			return _returnToFullPageURL;
		}

		_returnToFullPageURL = ParamUtil.getString(
			_httpServletRequest, "returnToFullPageURL");

		return _returnToFullPageURL;
	}

	private String _getRoleTypesParam() {
		if (_roleTypesParam != null) {
			return _roleTypesParam;
		}

		_roleTypesParam = ParamUtil.getString(_httpServletRequest, "roleTypes");

		return _roleTypesParam;
	}

	private static final int[] _TYPES_DEPOT_AND_REGULAR = {
		RoleConstants.TYPE_DEPOT, RoleConstants.TYPE_REGULAR
	};

	private List<String> _actions;
	private Group _group;
	private final long _groupId;
	private List<String> _guestUnsupportedActions;
	private final HttpServletRequest _httpServletRequest;
	private String _modelResource;
	private String _modelResourceDescription;
	private String _portletResource;
	private final RenderRequest _renderRequest;
	private Resource _resource;
	private Long _resourceGroupId;
	private String _resourcePrimKey;
	private String _returnToFullPageURL;
	private SearchContainer<Role> _roleSearchContainer;
	private final RoleTypeContributorProvider _roleTypeContributorProvider;
	private int[] _roleTypes;
	private String _roleTypesParam;
	private final Layout _selLayout;
	private String _selResource;
	private String _selResourceDescription;

}