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

package com.liferay.commerce.service.impl;

import com.liferay.commerce.context.CommerceGroupThreadLocal;
import com.liferay.commerce.exception.CommerceOrderTypeDisplayDateException;
import com.liferay.commerce.exception.CommerceOrderTypeExpirationDateException;
import com.liferay.commerce.exception.CommerceOrderTypeNameException;
import com.liferay.commerce.model.CommerceOrderType;
import com.liferay.commerce.model.CommerceOrderTypeRelTable;
import com.liferay.commerce.model.CommerceOrderTypeTable;
import com.liferay.commerce.service.base.CommerceOrderTypeLocalServiceBaseImpl;
import com.liferay.expando.kernel.service.ExpandoRowLocalService;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.petra.sql.dsl.query.FromStep;
import com.liferay.petra.sql.dsl.query.GroupByStep;
import com.liferay.petra.sql.dsl.query.JoinStep;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.security.permission.InlineSQLHelper;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.WorkflowInstanceLinkLocalService;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Alessio Antonio Rendina
 */
public class CommerceOrderTypeLocalServiceImpl
	extends CommerceOrderTypeLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceOrderType addCommerceOrderType(
			String externalReferenceCode, long userId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			boolean active, int displayDateMonth, int displayDateDay,
			int displayDateYear, int displayDateHour, int displayDateMinute,
			int displayOrder, int expirationDateMonth, int expirationDateDay,
			int expirationDateYear, int expirationDateHour,
			int expirationDateMinute, boolean neverExpire,
			ServiceContext serviceContext)
		throws PortalException {

		if (Validator.isBlank(externalReferenceCode)) {
			externalReferenceCode = null;
		}

		_validate(nameMap);

		long commerceOrderTypeId = counterLocalService.increment();

		CommerceOrderType commerceOrderType =
			commerceOrderTypePersistence.create(commerceOrderTypeId);

		commerceOrderType.setExternalReferenceCode(externalReferenceCode);

		User user = userLocalService.getUser(userId);

		commerceOrderType.setCompanyId(user.getCompanyId());
		commerceOrderType.setUserId(user.getUserId());
		commerceOrderType.setUserName(user.getFullName());

		commerceOrderType.setNameMap(nameMap);
		commerceOrderType.setDescriptionMap(descriptionMap);
		commerceOrderType.setActive(active);

		Date date = new Date();

		Date displayDate = PortalUtil.getDate(
			displayDateMonth, displayDateDay, displayDateYear, displayDateHour,
			displayDateMinute, user.getTimeZone(),
			CommerceOrderTypeDisplayDateException.class);

		Date expirationDate = null;

		if (!neverExpire) {
			expirationDate = PortalUtil.getDate(
				expirationDateMonth, expirationDateDay, expirationDateYear,
				expirationDateHour, expirationDateMinute, user.getTimeZone(),
				CommerceOrderTypeExpirationDateException.class);
		}

		commerceOrderType.setDisplayDate(displayDate);
		commerceOrderType.setDisplayOrder(displayOrder);
		commerceOrderType.setExpirationDate(expirationDate);

		if ((expirationDate == null) || expirationDate.after(date)) {
			commerceOrderType.setStatus(WorkflowConstants.STATUS_DRAFT);
		}
		else {
			commerceOrderType.setStatus(WorkflowConstants.STATUS_EXPIRED);
		}

		commerceOrderType.setStatusByUserId(user.getUserId());
		commerceOrderType.setStatusDate(serviceContext.getModifiedDate(date));
		commerceOrderType.setExpandoBridgeAttributes(serviceContext);

		commerceOrderType = commerceOrderTypePersistence.update(
			commerceOrderType);

		resourceLocalService.addModelResources(
			commerceOrderType, serviceContext);

		return _startWorkflowInstance(
			user.getUserId(), commerceOrderType, serviceContext);
	}

	@Override
	public void checkCommerceOrderTypes() throws PortalException {
		_checkCommerceOrderTypesByDisplayDate();
		_checkCommerceOrderTypesByExpirationDate();
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public CommerceOrderType deleteCommerceOrderType(
			CommerceOrderType commerceOrderType)
		throws PortalException {

		commerceOrderTypePersistence.remove(commerceOrderType);

		resourceLocalService.deleteResource(
			commerceOrderType.getCompanyId(), CommerceOrderType.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			commerceOrderType.getCommerceOrderTypeId());

		commerceOrderTypeRelLocalService.deleteCommerceOrderTypeRels(
			commerceOrderType.getCommerceOrderTypeId());

		_expandoRowLocalService.deleteRows(
			commerceOrderType.getCommerceOrderTypeId());

		_workflowInstanceLinkLocalService.deleteWorkflowInstanceLinks(
			commerceOrderType.getCompanyId(), 0L,
			CommerceOrderType.class.getName(),
			commerceOrderType.getCommerceOrderTypeId());

		return commerceOrderType;
	}

	@Override
	public CommerceOrderType deleteCommerceOrderType(long commerceOrderTypeId)
		throws PortalException {

		CommerceOrderType commerceOrderType =
			commerceOrderTypePersistence.findByPrimaryKey(commerceOrderTypeId);

		return commerceOrderTypeLocalService.deleteCommerceOrderType(
			commerceOrderType);
	}

	@Override
	public CommerceOrderType fetchByExternalReferenceCode(
		String externalReferenceCode, long companyId) {

		if (Validator.isBlank(externalReferenceCode)) {
			return null;
		}

		return commerceOrderTypePersistence.fetchByC_ERC(
			companyId, externalReferenceCode);
	}

	@Override
	public List<CommerceOrderType> getCommerceOrderTypes(
			long companyId, String className, long classPK, boolean active,
			int start, int end)
		throws PortalException {

		return dslQuery(
			_getGroupByStep(
				DSLQueryFactoryUtil.selectDistinct(
					CommerceOrderTypeTable.INSTANCE),
				companyId, className, classPK, active
			).orderBy(
				CommerceOrderTypeTable.INSTANCE.displayOrder.ascending()
			).limit(
				start, end
			));
	}

	@Override
	public int getCommerceOrderTypesCount(long companyId, boolean active) {
		return commerceOrderTypePersistence.countByC_A(companyId, active);
	}

	@Override
	public int getCommerceOrderTypesCount(
			long companyId, String className, long classPK, boolean active)
		throws PortalException {

		return dslQueryCount(
			_getGroupByStep(
				DSLQueryFactoryUtil.countDistinct(
					CommerceOrderTypeTable.INSTANCE.commerceOrderTypeId),
				companyId, className, classPK, active));
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceOrderType updateCommerceOrderType(
			String externalReferenceCode, long userId, long commerceOrderTypeId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			boolean active, int displayDateMonth, int displayDateDay,
			int displayDateYear, int displayDateHour, int displayDateMinute,
			int displayOrder, int expirationDateMonth, int expirationDateDay,
			int expirationDateYear, int expirationDateHour,
			int expirationDateMinute, boolean neverExpire,
			ServiceContext serviceContext)
		throws PortalException {

		_validate(nameMap);

		CommerceOrderType commerceOrderType =
			commerceOrderTypePersistence.findByPrimaryKey(commerceOrderTypeId);

		commerceOrderType.setExternalReferenceCode(externalReferenceCode);

		commerceOrderType.setNameMap(nameMap);
		commerceOrderType.setDescriptionMap(descriptionMap);
		commerceOrderType.setActive(active);

		Date date = new Date();

		User user = userLocalService.getUser(userId);

		commerceOrderType.setDisplayDate(
			PortalUtil.getDate(
				displayDateMonth, displayDateDay, displayDateYear,
				displayDateHour, displayDateMinute, user.getTimeZone(),
				CommerceOrderTypeDisplayDateException.class));

		commerceOrderType.setDisplayOrder(displayOrder);

		Date expirationDate = null;

		if (!neverExpire) {
			expirationDate = PortalUtil.getDate(
				expirationDateMonth, expirationDateDay, expirationDateYear,
				expirationDateHour, expirationDateMinute, user.getTimeZone(),
				CommerceOrderTypeExpirationDateException.class);
		}

		commerceOrderType.setExpirationDate(expirationDate);

		if ((expirationDate == null) || expirationDate.after(date)) {
			commerceOrderType.setStatus(WorkflowConstants.STATUS_DRAFT);
		}
		else {
			commerceOrderType.setStatus(WorkflowConstants.STATUS_EXPIRED);
		}

		commerceOrderType.setStatusByUserId(user.getUserId());
		commerceOrderType.setStatusDate(serviceContext.getModifiedDate(date));
		commerceOrderType.setExpandoBridgeAttributes(serviceContext);

		commerceOrderType = commerceOrderTypePersistence.update(
			commerceOrderType);

		return _startWorkflowInstance(
			user.getUserId(), commerceOrderType, serviceContext);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceOrderType updateCommerceOrderTypeExternalReferenceCode(
			String externalReferenceCode, long commerceOrderTypeId)
		throws PortalException {

		CommerceOrderType commerceOrderType =
			commerceOrderTypeLocalService.getCommerceOrderType(
				commerceOrderTypeId);

		commerceOrderType.setExternalReferenceCode(externalReferenceCode);

		return commerceOrderTypePersistence.update(commerceOrderType);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceOrderType updateStatus(
			long userId, long commerceOrderTypeId, int status,
			ServiceContext serviceContext,
			Map<String, Serializable> workflowContext)
		throws PortalException {

		CommerceOrderType commerceOrderType =
			commerceOrderTypePersistence.findByPrimaryKey(commerceOrderTypeId);

		Date date = new Date();

		if ((status == WorkflowConstants.STATUS_APPROVED) &&
			(commerceOrderType.getDisplayDate() != null) &&
			date.before(commerceOrderType.getDisplayDate())) {

			commerceOrderType.setActive(false);

			status = WorkflowConstants.STATUS_SCHEDULED;
		}

		if (status == WorkflowConstants.STATUS_APPROVED) {
			Date expirationDate = commerceOrderType.getExpirationDate();

			if ((expirationDate != null) && expirationDate.before(date)) {
				commerceOrderType.setExpirationDate(null);
			}

			if (commerceOrderType.getStatus() ==
					WorkflowConstants.STATUS_SCHEDULED) {

				commerceOrderType.setActive(true);
			}
		}

		if (status == WorkflowConstants.STATUS_EXPIRED) {
			commerceOrderType.setActive(false);
			commerceOrderType.setExpirationDate(date);
		}

		commerceOrderType.setStatus(status);

		User user = userLocalService.getUser(userId);

		commerceOrderType.setStatusByUserId(user.getUserId());
		commerceOrderType.setStatusByUserName(user.getFullName());

		commerceOrderType.setStatusDate(serviceContext.getModifiedDate(date));

		return commerceOrderTypePersistence.update(commerceOrderType);
	}

	private void _checkCommerceOrderTypesByDisplayDate()
		throws PortalException {

		List<CommerceOrderType> commerceOrderTypes =
			commerceOrderTypePersistence.findByLtD_S(
				new Date(), WorkflowConstants.STATUS_SCHEDULED);

		for (CommerceOrderType commerceOrderType : commerceOrderTypes) {
			long userId = PortalUtil.getValidUserId(
				commerceOrderType.getCompanyId(),
				commerceOrderType.getUserId());

			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setCommand(Constants.UPDATE);

			commerceOrderTypeLocalService.updateStatus(
				userId, commerceOrderType.getCommerceOrderTypeId(),
				WorkflowConstants.STATUS_APPROVED, serviceContext,
				new HashMap<String, Serializable>());
		}
	}

	private void _checkCommerceOrderTypesByExpirationDate()
		throws PortalException {

		List<CommerceOrderType> commerceOrderTypes =
			commerceOrderTypePersistence.findByLtE_S(
				new Date(), WorkflowConstants.STATUS_APPROVED);

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Expiring " + commerceOrderTypes.size() +
					" commerce order types");
		}

		for (CommerceOrderType commerceOrderType : commerceOrderTypes) {
			long userId = PortalUtil.getValidUserId(
				commerceOrderType.getCompanyId(),
				commerceOrderType.getUserId());

			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setCommand(Constants.UPDATE);

			commerceOrderTypeLocalService.updateStatus(
				userId, commerceOrderType.getCommerceOrderTypeId(),
				WorkflowConstants.STATUS_EXPIRED, serviceContext,
				new HashMap<String, Serializable>());
		}
	}

	private GroupByStep _getGroupByStep(
			FromStep fromStep, long companyId, String className, long classPK,
			boolean active)
		throws PortalException {

		JoinStep joinStep = fromStep.from(
			CommerceOrderTypeTable.INSTANCE
		).leftJoinOn(
			CommerceOrderTypeRelTable.INSTANCE,
			CommerceOrderTypeRelTable.INSTANCE.commerceOrderTypeId.eq(
				CommerceOrderTypeTable.INSTANCE.commerceOrderTypeId)
		);

		return joinStep.where(
			() -> {
				Predicate predicate =
					CommerceOrderTypeTable.INSTANCE.companyId.eq(
						companyId
					).and(
						CommerceOrderTypeTable.INSTANCE.active.eq(active)
					);

				PermissionChecker permissionChecker =
					PermissionThreadLocal.getPermissionChecker();

				Group group = CommerceGroupThreadLocal.get();

				if ((permissionChecker != null) && (group != null)) {
					predicate = predicate.and(
						_inlineSQLHelper.getPermissionWherePredicate(
							CommerceOrderType.class.getName(),
							CommerceOrderTypeTable.INSTANCE.commerceOrderTypeId,
							group.getGroupId()));
				}

				Predicate commerceOrderTypeRelPredicate =
					Predicate.withParentheses(
						CommerceOrderTypeRelTable.INSTANCE.classNameId.eq(
							classNameLocalService.getClassNameId(className)
						).and(
							CommerceOrderTypeRelTable.INSTANCE.classPK.eq(
								classPK)
						));

				commerceOrderTypeRelPredicate =
					commerceOrderTypeRelPredicate.or(
						CommerceOrderTypeRelTable.INSTANCE.
							commerceOrderTypeRelId.isNull()
					).withParentheses();

				return predicate.and(commerceOrderTypeRelPredicate);
			});
	}

	private CommerceOrderType _startWorkflowInstance(
			long userId, CommerceOrderType commerceOrderType,
			ServiceContext serviceContext)
		throws PortalException {

		Map<String, Serializable> workflowContext = new HashMap<>();

		return WorkflowHandlerRegistryUtil.startWorkflowInstance(
			commerceOrderType.getCompanyId(), 0L, userId,
			CommerceOrderType.class.getName(),
			commerceOrderType.getCommerceOrderTypeId(), commerceOrderType,
			serviceContext, workflowContext);
	}

	private void _validate(Map<Locale, String> nameMap) throws PortalException {
		if ((nameMap == null) || nameMap.isEmpty()) {
			throw new CommerceOrderTypeNameException();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceOrderTypeLocalServiceImpl.class);

	@ServiceReference(type = ExpandoRowLocalService.class)
	private ExpandoRowLocalService _expandoRowLocalService;

	@ServiceReference(type = InlineSQLHelper.class)
	private InlineSQLHelper _inlineSQLHelper;

	@ServiceReference(type = WorkflowInstanceLinkLocalService.class)
	private WorkflowInstanceLinkLocalService _workflowInstanceLinkLocalService;

}