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

package com.liferay.object.service;

import com.liferay.object.model.ObjectAction;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;

/**
 * Provides the local service utility for ObjectAction. This utility wraps
 * <code>com.liferay.object.service.impl.ObjectActionLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Marco Leo
 * @see ObjectActionLocalService
 * @generated
 */
public class ObjectActionLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.object.service.impl.ObjectActionLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static ObjectAction addObjectAction(
			long userId, long objectDefinitionId, boolean active,
			String description, String name, String objectActionExecutorKey,
			String objectActionTriggerKey,
			com.liferay.portal.kernel.util.UnicodeProperties
				parametersUnicodeProperties)
		throws PortalException {

		return getService().addObjectAction(
			userId, objectDefinitionId, active, description, name,
			objectActionExecutorKey, objectActionTriggerKey,
			parametersUnicodeProperties);
	}

	/**
	 * Adds the object action to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectActionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectAction the object action
	 * @return the object action that was added
	 */
	public static ObjectAction addObjectAction(ObjectAction objectAction) {
		return getService().addObjectAction(objectAction);
	}

	/**
	 * Creates a new object action with the primary key. Does not add the object action to the database.
	 *
	 * @param objectActionId the primary key for the new object action
	 * @return the new object action
	 */
	public static ObjectAction createObjectAction(long objectActionId) {
		return getService().createObjectAction(objectActionId);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel createPersistedModel(
			Serializable primaryKeyObj)
		throws PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Deletes the object action with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectActionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectActionId the primary key of the object action
	 * @return the object action that was removed
	 * @throws PortalException if a object action with the primary key could not be found
	 */
	public static ObjectAction deleteObjectAction(long objectActionId)
		throws PortalException {

		return getService().deleteObjectAction(objectActionId);
	}

	/**
	 * Deletes the object action from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectActionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectAction the object action
	 * @return the object action that was removed
	 */
	public static ObjectAction deleteObjectAction(ObjectAction objectAction) {
		return getService().deleteObjectAction(objectAction);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel deletePersistedModel(
			PersistedModel persistedModel)
		throws PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static <T> T dslQuery(DSLQuery dslQuery) {
		return getService().dslQuery(dslQuery);
	}

	public static int dslQueryCount(DSLQuery dslQuery) {
		return getService().dslQueryCount(dslQuery);
	}

	public static DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.object.model.impl.ObjectActionModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.object.model.impl.ObjectActionModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static ObjectAction fetchObjectAction(long objectActionId) {
		return getService().fetchObjectAction(objectActionId);
	}

	/**
	 * Returns the object action with the matching UUID and company.
	 *
	 * @param uuid the object action's UUID
	 * @param companyId the primary key of the company
	 * @return the matching object action, or <code>null</code> if a matching object action could not be found
	 */
	public static ObjectAction fetchObjectActionByUuidAndCompanyId(
		String uuid, long companyId) {

		return getService().fetchObjectActionByUuidAndCompanyId(
			uuid, companyId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return getService().getExportActionableDynamicQuery(portletDataContext);
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the object action with the primary key.
	 *
	 * @param objectActionId the primary key of the object action
	 * @return the object action
	 * @throws PortalException if a object action with the primary key could not be found
	 */
	public static ObjectAction getObjectAction(long objectActionId)
		throws PortalException {

		return getService().getObjectAction(objectActionId);
	}

	/**
	 * Returns the object action with the matching UUID and company.
	 *
	 * @param uuid the object action's UUID
	 * @param companyId the primary key of the company
	 * @return the matching object action
	 * @throws PortalException if a matching object action could not be found
	 */
	public static ObjectAction getObjectActionByUuidAndCompanyId(
			String uuid, long companyId)
		throws PortalException {

		return getService().getObjectActionByUuidAndCompanyId(uuid, companyId);
	}

	/**
	 * Returns a range of all the object actions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.object.model.impl.ObjectActionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of object actions
	 * @param end the upper bound of the range of object actions (not inclusive)
	 * @return the range of object actions
	 */
	public static List<ObjectAction> getObjectActions(int start, int end) {
		return getService().getObjectActions(start, end);
	}

	public static List<ObjectAction> getObjectActions(long objectDefinitionId) {
		return getService().getObjectActions(objectDefinitionId);
	}

	public static List<ObjectAction> getObjectActions(
		long objectDefinitionId, String objectActionTriggerKey) {

		return getService().getObjectActions(
			objectDefinitionId, objectActionTriggerKey);
	}

	/**
	 * Returns the number of object actions.
	 *
	 * @return the number of object actions
	 */
	public static int getObjectActionsCount() {
		return getService().getObjectActionsCount();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	public static ObjectAction updateObjectAction(
			long objectActionId, boolean active, String description,
			String name,
			com.liferay.portal.kernel.util.UnicodeProperties
				parametersUnicodeProperties)
		throws PortalException {

		return getService().updateObjectAction(
			objectActionId, active, description, name,
			parametersUnicodeProperties);
	}

	/**
	 * Updates the object action in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectActionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectAction the object action
	 * @return the object action that was updated
	 */
	public static ObjectAction updateObjectAction(ObjectAction objectAction) {
		return getService().updateObjectAction(objectAction);
	}

	public static ObjectActionLocalService getService() {
		return _service;
	}

	private static volatile ObjectActionLocalService _service;

}