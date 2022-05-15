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

package com.liferay.portal.workflow.kaleo.runtime.integration.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.model.WorkflowDefinitionLink;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalService;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.uuid.PortalUUID;
import com.liferay.portal.kernel.workflow.RequiredWorkflowDefinitionException;
import com.liferay.portal.kernel.workflow.WorkflowDefinition;
import com.liferay.portal.kernel.workflow.WorkflowDefinitionManager;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.comparator.WorkflowComparatorFactory;
import com.liferay.portal.lock.service.LockLocalService;
import com.liferay.portal.workflow.constants.WorkflowDefinitionConstants;
import com.liferay.portal.workflow.kaleo.KaleoWorkflowModelConverter;
import com.liferay.portal.workflow.kaleo.definition.parser.WorkflowModelParser;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinition;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion;
import com.liferay.portal.workflow.kaleo.runtime.WorkflowEngine;
import com.liferay.portal.workflow.kaleo.runtime.integration.internal.util.WorkflowLockUtil;
import com.liferay.portal.workflow.kaleo.runtime.util.comparator.KaleoDefinitionOrderByComparator;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionVersionLocalService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 * @author Eduardo Lundgren
 */
@Component(
	immediate = true, property = "proxy.bean=false",
	service = WorkflowDefinitionManager.class
)
public class WorkflowDefinitionManagerImpl
	implements WorkflowDefinitionManager {

	@Override
	public WorkflowDefinition deployWorkflowDefinition(
			long companyId, long userId, String title, String name,
			byte[] bytes)
		throws WorkflowException {

		return deployWorkflowDefinition(
			companyId, userId, title, name,
			WorkflowDefinitionConstants.SCOPE_ALL, bytes);
	}

	@Override
	public WorkflowDefinition deployWorkflowDefinition(
			long companyId, long userId, String title, String name,
			String scope, byte[] bytes)
		throws WorkflowException {

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setCompanyId(companyId);
		serviceContext.setUserId(userId);

		return _workflowEngine.deployWorkflowDefinition(
			title, name, scope, new UnsyncByteArrayInputStream(bytes),
			serviceContext);
	}

	@Override
	public List<WorkflowDefinition> getActiveWorkflowDefinitions(
			long companyId, int start, int end,
			OrderByComparator<WorkflowDefinition> orderByComparator)
		throws WorkflowException {

		try {
			if (orderByComparator == null) {
				orderByComparator =
					_workflowComparatorFactory.getDefinitionNameComparator(
						true);
			}

			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setCompanyId(companyId);

			List<KaleoDefinition> kaleoDefinitions =
				_kaleoDefinitionLocalService.getScopeKaleoDefinitions(
					WorkflowDefinitionConstants.SCOPE_ALL, true, start, end,
					KaleoDefinitionOrderByComparator.getOrderByComparator(
						orderByComparator, _kaleoWorkflowModelConverter),
					serviceContext);

			int size = kaleoDefinitions.size();

			return _toWorkflowDefinitions(
				kaleoDefinitions.toArray(new KaleoDefinition[size]),
				orderByComparator);
		}
		catch (Exception exception) {
			throw new WorkflowException(exception);
		}
	}

	@Override
	public List<WorkflowDefinition> getActiveWorkflowDefinitions(
			long companyId, String name, int start, int end,
			OrderByComparator<WorkflowDefinition> orderByComparator)
		throws WorkflowException {

		try {
			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setCompanyId(companyId);

			List<KaleoDefinition> kaleoDefinitions = new ArrayList<>();

			KaleoDefinition kaleoDefinition =
				_kaleoDefinitionLocalService.getKaleoDefinition(
					name, serviceContext);

			if (kaleoDefinition.isActive()) {
				kaleoDefinitions.add(kaleoDefinition);
			}

			int size = kaleoDefinitions.size();

			return _toWorkflowDefinitions(
				kaleoDefinitions.toArray(new KaleoDefinition[size]),
				orderByComparator);
		}
		catch (WorkflowException workflowException) {
			throw workflowException;
		}
		catch (Exception exception) {
			throw new WorkflowException(exception);
		}
	}

	@Override
	public int getActiveWorkflowDefinitionsCount(long companyId)
		throws WorkflowException {

		try {
			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setCompanyId(companyId);

			return _kaleoDefinitionLocalService.getScopeKaleoDefinitionsCount(
				WorkflowDefinitionConstants.SCOPE_ALL, true, serviceContext);
		}
		catch (Exception exception) {
			throw new WorkflowException(exception);
		}
	}

	@Override
	public WorkflowDefinition getLatestWorkflowDefinition(
			long companyId, String name)
		throws WorkflowException {

		try {
			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setCompanyId(companyId);

			return _kaleoWorkflowModelConverter.toWorkflowDefinition(
				_kaleoDefinitionLocalService.getKaleoDefinition(
					name, serviceContext));
		}
		catch (WorkflowException workflowException) {
			throw workflowException;
		}
		catch (Exception exception) {
			throw new WorkflowException(exception);
		}
	}

	@Override
	public List<WorkflowDefinition> getLatestWorkflowDefinitions(
			Boolean active, long companyId, int start, int end,
			OrderByComparator<WorkflowDefinition> orderByComparator)
		throws WorkflowException {

		try {
			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setCompanyId(companyId);

			List<KaleoDefinition> kaleoDefinitions = null;

			if (active == null) {
				kaleoDefinitions =
					_kaleoDefinitionLocalService.getScopeKaleoDefinitions(
						WorkflowDefinitionConstants.SCOPE_ALL, start, end,
						KaleoDefinitionOrderByComparator.getOrderByComparator(
							orderByComparator, _kaleoWorkflowModelConverter),
						serviceContext);
			}
			else {
				kaleoDefinitions =
					_kaleoDefinitionLocalService.getScopeKaleoDefinitions(
						WorkflowDefinitionConstants.SCOPE_ALL, active, start,
						end,
						KaleoDefinitionOrderByComparator.getOrderByComparator(
							orderByComparator, _kaleoWorkflowModelConverter),
						serviceContext);
			}

			int size = kaleoDefinitions.size();

			return _toWorkflowDefinitions(
				kaleoDefinitions.toArray(new KaleoDefinition[size]),
				orderByComparator);
		}
		catch (Exception exception) {
			throw new WorkflowException(exception);
		}
	}

	@Override
	public int getLatestWorkflowDefinitionsCount(Boolean active, long companyId)
		throws WorkflowException {

		try {
			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setCompanyId(companyId);

			if (active == null) {
				return _kaleoDefinitionLocalService.
					getScopeKaleoDefinitionsCount(
						WorkflowDefinitionConstants.SCOPE_ALL, serviceContext);
			}

			return _kaleoDefinitionLocalService.getScopeKaleoDefinitionsCount(
				WorkflowDefinitionConstants.SCOPE_ALL, active, serviceContext);
		}
		catch (Exception exception) {
			throw new WorkflowException(exception);
		}
	}

	@Override
	public WorkflowDefinition getWorkflowDefinition(
			long companyId, String name, int version)
		throws WorkflowException {

		try {
			return _kaleoWorkflowModelConverter.toWorkflowDefinition(
				_kaleoDefinitionVersionLocalService.getKaleoDefinitionVersion(
					companyId, name, getVersion(version)));
		}
		catch (WorkflowException workflowException) {
			throw workflowException;
		}
		catch (Exception exception) {
			throw new WorkflowException(exception);
		}
	}

	@Override
	public List<WorkflowDefinition> getWorkflowDefinitions(
			long companyId, String name, int start, int end,
			OrderByComparator<WorkflowDefinition> orderByComparator)
		throws WorkflowException {

		try {
			List<KaleoDefinitionVersion> kaleoDefinitionVersions =
				_kaleoDefinitionVersionLocalService.getKaleoDefinitionVersions(
					companyId, name);

			int size = kaleoDefinitionVersions.size();

			return _toWorkflowDefinitions(
				kaleoDefinitionVersions.toArray(
					new KaleoDefinitionVersion[size]),
				orderByComparator);
		}
		catch (WorkflowException workflowException) {
			throw workflowException;
		}
		catch (Exception exception) {
			throw new WorkflowException(exception);
		}
	}

	@Override
	public int getWorkflowDefinitionsCount(long companyId, String name)
		throws WorkflowException {

		try {
			return _kaleoDefinitionVersionLocalService.
				getKaleoDefinitionVersionsCount(companyId, name);
		}
		catch (Exception exception) {
			throw new WorkflowException(exception);
		}
	}

	@Override
	public WorkflowDefinition saveWorkflowDefinition(
			long companyId, long userId, String title, String name,
			byte[] bytes)
		throws WorkflowException {

		return saveWorkflowDefinition(
			companyId, userId, title, name,
			WorkflowDefinitionConstants.SCOPE_ALL, bytes);
	}

	@Override
	public WorkflowDefinition saveWorkflowDefinition(
			long companyId, long userId, String title, String name,
			String scope, byte[] bytes)
		throws WorkflowException {

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setCompanyId(companyId);
		serviceContext.setUserId(userId);

		return _workflowEngine.saveWorkflowDefinition(
			title, name, scope, bytes, serviceContext);
	}

	@Override
	public void undeployWorkflowDefinition(
			long companyId, long userId, String name, int version)
		throws WorkflowException {

		String className = WorkflowDefinition.class.getName();
		String key = WorkflowLockUtil.encodeKey(name, version);

		if (_lockLocalService.isLocked(className, key)) {
			throw new WorkflowException(
				StringBundler.concat(
					"Workflow definition name ", name, " and version ", version,
					" is being undeployed"));
		}

		try {
			_lockLocalService.lock(
				userId, className, key, String.valueOf(userId), false,
				Time.HOUR);

			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setCompanyId(companyId);
			serviceContext.setUserId(userId);

			_workflowEngine.deleteWorkflowDefinition(
				name, version, serviceContext);
		}
		catch (WorkflowException workflowException) {
			throw workflowException;
		}
		catch (Exception exception) {
			throw new WorkflowException(exception);
		}
		finally {
			_lockLocalService.unlock(className, key);
		}
	}

	@Override
	public WorkflowDefinition updateActive(
			long companyId, long userId, String name, int version,
			boolean active)
		throws WorkflowException {

		try {
			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setCompanyId(companyId);
			serviceContext.setUserId(userId);

			if (active) {
				_kaleoDefinitionLocalService.activateKaleoDefinition(
					name, version, serviceContext);
			}
			else {
				List<WorkflowDefinitionLink> workflowDefinitionLinks =
					_workflowDefinitionLinkLocalService.
						getWorkflowDefinitionLinks(companyId, name, version);

				if (!workflowDefinitionLinks.isEmpty()) {
					throw new RequiredWorkflowDefinitionException(
						workflowDefinitionLinks);
				}

				_kaleoDefinitionLocalService.deactivateKaleoDefinition(
					name, version, serviceContext);
			}

			return getWorkflowDefinition(companyId, name, version);
		}
		catch (WorkflowException workflowException) {
			throw workflowException;
		}
		catch (Exception exception) {
			throw new WorkflowException(exception);
		}
	}

	@Override
	public void validateWorkflowDefinition(byte[] bytes)
		throws WorkflowException {

		_workflowEngine.validateWorkflowDefinition(
			new UnsyncByteArrayInputStream(bytes));
	}

	protected String getVersion(int version) {
		return version + StringPool.PERIOD + 0;
	}

	@Reference
	protected PortalUUID portalUUID;

	private List<WorkflowDefinition> _toWorkflowDefinitions(
		KaleoDefinition[] kaleoDefinitions,
		OrderByComparator<WorkflowDefinition> orderByComparator) {

		List<WorkflowDefinition> workflowDefinitions = new ArrayList<>(
			kaleoDefinitions.length);

		for (KaleoDefinition kaleoDefinition : kaleoDefinitions) {
			WorkflowDefinition workflowDefinition =
				_kaleoWorkflowModelConverter.toWorkflowDefinition(
					kaleoDefinition);

			workflowDefinitions.add(workflowDefinition);
		}

		if (orderByComparator != null) {
			Collections.sort(workflowDefinitions, orderByComparator);
		}

		return workflowDefinitions;
	}

	private List<WorkflowDefinition> _toWorkflowDefinitions(
			KaleoDefinitionVersion[] kaleoDefinitionVersions,
			OrderByComparator<WorkflowDefinition> orderByComparator)
		throws PortalException {

		List<WorkflowDefinition> workflowDefinitions = new ArrayList<>(
			kaleoDefinitionVersions.length);

		for (KaleoDefinitionVersion kaleoDefinitionVersion :
				kaleoDefinitionVersions) {

			WorkflowDefinition workflowDefinition =
				_kaleoWorkflowModelConverter.toWorkflowDefinition(
					kaleoDefinitionVersion);

			workflowDefinitions.add(workflowDefinition);
		}

		if (orderByComparator != null) {
			Collections.sort(workflowDefinitions, orderByComparator);
		}

		return workflowDefinitions;
	}

	@Reference
	private KaleoDefinitionLocalService _kaleoDefinitionLocalService;

	@Reference
	private KaleoDefinitionVersionLocalService
		_kaleoDefinitionVersionLocalService;

	@Reference
	private KaleoWorkflowModelConverter _kaleoWorkflowModelConverter;

	@Reference
	private LockLocalService _lockLocalService;

	@Reference
	private WorkflowComparatorFactory _workflowComparatorFactory;

	@Reference
	private WorkflowDefinitionLinkLocalService
		_workflowDefinitionLinkLocalService;

	@Reference
	private WorkflowEngine _workflowEngine;

	@Reference
	private WorkflowModelParser _workflowModelParser;

}