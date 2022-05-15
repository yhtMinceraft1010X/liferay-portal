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

package com.liferay.portal.workflow.kaleo.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceAction;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.workflow.kaleo.definition.Assignment;
import com.liferay.portal.workflow.kaleo.definition.AssignmentType;
import com.liferay.portal.workflow.kaleo.definition.ResourceActionAssignment;
import com.liferay.portal.workflow.kaleo.definition.RoleAssignment;
import com.liferay.portal.workflow.kaleo.definition.ScriptAssignment;
import com.liferay.portal.workflow.kaleo.definition.ScriptLanguage;
import com.liferay.portal.workflow.kaleo.definition.UserAssignment;
import com.liferay.portal.workflow.kaleo.internal.util.RoleUtil;
import com.liferay.portal.workflow.kaleo.model.KaleoTask;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignment;
import com.liferay.portal.workflow.kaleo.service.base.KaleoTaskAssignmentLocalServiceBaseImpl;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignment",
	service = AopService.class
)
public class KaleoTaskAssignmentLocalServiceImpl
	extends KaleoTaskAssignmentLocalServiceBaseImpl {

	@Override
	public KaleoTaskAssignment addKaleoTaskAssignment(
			String kaleoClassName, long kaleoClassPK, long kaleoDefinitionId,
			long kaleoDefinitionVersionId, Assignment assignment,
			ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(serviceContext.getGuestOrUserId());
		Date date = new Date();

		long kaleoTaskAssignmentId = counterLocalService.increment();

		KaleoTaskAssignment kaleoTaskAssignment =
			kaleoTaskAssignmentPersistence.create(kaleoTaskAssignmentId);

		kaleoTaskAssignment.setCompanyId(user.getCompanyId());
		kaleoTaskAssignment.setUserId(user.getUserId());
		kaleoTaskAssignment.setUserName(user.getFullName());
		kaleoTaskAssignment.setCreateDate(date);
		kaleoTaskAssignment.setModifiedDate(date);
		kaleoTaskAssignment.setKaleoClassName(kaleoClassName);
		kaleoTaskAssignment.setKaleoClassPK(kaleoClassPK);
		kaleoTaskAssignment.setKaleoDefinitionId(kaleoDefinitionId);
		kaleoTaskAssignment.setKaleoDefinitionVersionId(
			kaleoDefinitionVersionId);
		kaleoTaskAssignment.setKaleoNodeId(
			kaleoTaskAssignment.getKaleoNodeId());

		setAssignee(kaleoTaskAssignment, assignment, serviceContext);

		return kaleoTaskAssignmentPersistence.update(kaleoTaskAssignment);
	}

	@Override
	public void deleteCompanyKaleoTaskAssignments(long companyId) {
		kaleoTaskAssignmentPersistence.removeByCompanyId(companyId);
	}

	@Override
	public void deleteKaleoDefinitionVersionKaleoTaskAssignments(
		long kaleoDefinitionVersionId) {

		kaleoTaskAssignmentPersistence.removeByKaleoDefinitionVersionId(
			kaleoDefinitionVersionId);
	}

	@Override
	public List<KaleoTaskAssignment> getKaleoTaskAssignments(long kaleoTaskId) {
		return kaleoTaskAssignmentPersistence.findByKCN_KCPK(
			KaleoTask.class.getName(), kaleoTaskId);
	}

	@Override
	public List<KaleoTaskAssignment> getKaleoTaskAssignments(
		long kaleoTaskId, String assigneeClassName) {

		return kaleoTaskAssignmentPersistence.findByKCN_KCPK_ACN(
			KaleoTask.class.getName(), kaleoTaskId, assigneeClassName);
	}

	@Override
	public List<KaleoTaskAssignment> getKaleoTaskAssignments(
		String kaleoClassName, long kaleoClassPK) {

		return kaleoTaskAssignmentPersistence.findByKCN_KCPK(
			kaleoClassName, kaleoClassPK);
	}

	@Override
	public int getKaleoTaskAssignmentsCount(long kaleoTaskId) {
		return kaleoTaskAssignmentPersistence.countByKCN_KCPK(
			KaleoTask.class.getName(), kaleoTaskId);
	}

	@Override
	public int getKaleoTaskAssignmentsCount(
		long kaleoTaskId, String assigneeClassName) {

		return kaleoTaskAssignmentPersistence.countByKCN_KCPK_ACN(
			KaleoTask.class.getName(), kaleoTaskId, assigneeClassName);
	}

	protected void setAssignee(
			KaleoTaskAssignment kaleoTaskAssignment, Assignment assignment,
			ServiceContext serviceContext)
		throws PortalException {

		AssignmentType assignmentType = assignment.getAssignmentType();

		if (assignmentType.equals(AssignmentType.RESOURCE_ACTION)) {
			kaleoTaskAssignment.setAssigneeClassName(
				ResourceAction.class.getName());

			ResourceActionAssignment resourceActionAssignment =
				(ResourceActionAssignment)assignment;

			kaleoTaskAssignment.setAssigneeActionId(
				resourceActionAssignment.getActionId());
		}
		else if (assignmentType.equals(AssignmentType.ROLE)) {
			kaleoTaskAssignment.setAssigneeClassName(Role.class.getName());

			RoleAssignment roleAssignment = (RoleAssignment)assignment;

			Role role = null;

			if (Validator.isNotNull(roleAssignment.getRoleName())) {
				role = RoleUtil.getRole(
					roleAssignment.getRoleName(),
					RoleUtil.getRoleType(roleAssignment.getRoleType()),
					roleAssignment.isAutoCreate(), serviceContext);
			}
			else {
				role = _roleLocalService.getRole(roleAssignment.getRoleId());
			}

			kaleoTaskAssignment.setAssigneeClassPK(role.getRoleId());
		}
		else if (assignmentType.equals(AssignmentType.SCRIPT)) {
			kaleoTaskAssignment.setAssigneeClassName(
				AssignmentType.SCRIPT.name());

			ScriptAssignment scriptAssignment = (ScriptAssignment)assignment;

			kaleoTaskAssignment.setAssigneeScript(scriptAssignment.getScript());

			ScriptLanguage scriptLanguage =
				scriptAssignment.getScriptLanguage();

			kaleoTaskAssignment.setAssigneeScriptLanguage(
				scriptLanguage.getValue());

			kaleoTaskAssignment.setAssigneeScriptRequiredContexts(
				scriptAssignment.getScriptRequiredContexts());
		}
		else if (assignmentType.equals(AssignmentType.USER)) {
			kaleoTaskAssignment.setAssigneeClassName(User.class.getName());

			UserAssignment userAssignment = (UserAssignment)assignment;

			User user = null;

			if (userAssignment.getUserId() > 0) {
				user = userLocalService.getUser(userAssignment.getUserId());
			}
			else if (Validator.isNotNull(userAssignment.getEmailAddress())) {
				user = userLocalService.getUserByEmailAddress(
					serviceContext.getCompanyId(),
					userAssignment.getEmailAddress());
			}
			else if (Validator.isNotNull(userAssignment.getScreenName())) {
				user = userLocalService.getUserByScreenName(
					serviceContext.getCompanyId(),
					userAssignment.getScreenName());
			}

			if (user != null) {
				kaleoTaskAssignment.setAssigneeClassPK(user.getUserId());
			}
			else {
				kaleoTaskAssignment.setAssigneeClassPK(0);
			}
		}
	}

	@Reference
	private RoleLocalService _roleLocalService;

}