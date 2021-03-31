/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.commerce.provisioning.internal.cloud.client;

import com.liferay.osb.commerce.provisioning.internal.cloud.client.dto.PortalInstance;
import com.liferay.portal.instances.initializer.PortalInstanceInitializer;
import com.liferay.portal.instances.initializer.PortalInstanceInitializerRegistry;
import com.liferay.portal.instances.service.PortalInstancesLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Portal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivica Cardic
 */
public class DXPCloudProvisioningClientMockImpl
	implements DXPCloudProvisioningClient {

	public DXPCloudProvisioningClientMockImpl(
		CompanyLocalService companyLocalService, Portal portal,
		PortalInstanceInitializerRegistry portalInstanceInitializerRegistry,
		PortalInstancesLocalService portalInstancesLocalService,
		RoleLocalService roleLocalService, UserLocalService userLocalService) {

		_companyLocalService = companyLocalService;
		_portal = portal;
		_portalInstanceInitializerRegistry = portalInstanceInitializerRegistry;
		_portalInstancesLocalService = portalInstancesLocalService;
		_roleLocalService = roleLocalService;
		_userLocalService = userLocalService;
	}

	@Override
	public void deletePortalInstance(String portalInstanceId) {
		try {
			Company company = _companyLocalService.getCompanyByWebId(
				portalInstanceId);

			_portalInstancesLocalService.removeCompany(company.getCompanyId());
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public PortalInstance getPortalInstance(String portalInstanceId) {
		try {
			return _getPortalInstance(portalInstanceId);
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	@Override
	public List<PortalInstance> getPortalInstances() {
		List<PortalInstance> portalInstances = new ArrayList<>();

		long[] companyIds = _portalInstancesLocalService.getCompanyIds();

		for (long companyId : companyIds) {
			try {
				Company company = _companyLocalService.getCompany(companyId);

				portalInstances.add(_getPortalInstance(company.getWebId()));
			}
			catch (PortalException portalException) {
				throw new SystemException(portalException);
			}
		}

		return portalInstances;
	}

	@Override
	public PortalInstance postPortalInstance(
		String domain, String portalInitializerKey) {

		String webId = _generateWebId();

		String virtualHostname = _toVirtualHostname(webId);

		try {
			_addCompany(domain, webId, virtualHostname);

			PortalInstanceInitializer portalInstanceInitializer =
				_portalInstanceInitializerRegistry.getPortalInstanceInitializer(
					portalInitializerKey);

			portalInstanceInitializer.initialize(
				webId, virtualHostname, domain);

			return _getPortalInstance(webId);
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	@Override
	public PortalInstance updatePortalInstance(
		String domain, String portalInstanceId) {

		try {
			Company company = _companyLocalService.getCompanyByWebId(
				portalInstanceId);

			company.setMx(domain);

			_companyLocalService.updateCompany(company);

			return _toPortalInstance(company);
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	private void _addCompany(String mx, String webId, String virtualHostname)
		throws PortalException {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_getAdministratorUser()));

		try {
			_companyLocalService.addCompany(
				webId, virtualHostname, mx, false, 0, true);
		}
		finally {
			PermissionThreadLocal.setPermissionChecker(permissionChecker);
		}
	}

	private String _generateWebId() {
		return "commerce" + (_companyLocalService.getCompaniesCount() + 1);
	}

	private User _getAdministratorUser() throws PortalException {
		Role role = _roleLocalService.getRole(
			_portal.getDefaultCompanyId(), RoleConstants.ADMINISTRATOR);

		for (User user : _userLocalService.getRoleUsers(role.getRoleId())) {
			return user;
		}

		throw new IllegalStateException("Administrator user does not exist");
	}

	private PortalInstance _getPortalInstance(String webId)
		throws PortalException {

		Company company = _companyLocalService.getCompanyByWebId(webId);

		return _toPortalInstance(company);
	}

	private PortalInstance _toPortalInstance(Company company) {
		PortalInstance portalInstance = new PortalInstance();

		portalInstance.setDomain(company.getMx());
		portalInstance.setVirtualHost(company.getVirtualHostname());
		portalInstance.setPortalInstanceId(company.getWebId());

		return portalInstance;
	}

	private String _toVirtualHostname(String webId) {
		return webId + ".test";
	}

	private final CompanyLocalService _companyLocalService;
	private final Portal _portal;
	private final PortalInstanceInitializerRegistry
		_portalInstanceInitializerRegistry;
	private final PortalInstancesLocalService _portalInstancesLocalService;
	private final RoleLocalService _roleLocalService;
	private final UserLocalService _userLocalService;

}