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

import com.liferay.object.model.ObjectEntry;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

/**
 * Provides the local service utility for ObjectEntry. This utility wraps
 * <code>com.liferay.object.service.impl.ObjectEntryLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Marco Leo
 * @see ObjectEntryLocalService
 * @generated
 */
public class ObjectEntryLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.object.service.impl.ObjectEntryLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static ObjectEntry addObjectEntry(
			long userId, long groupId, long objectDefinitionId,
			Map<String, Serializable> values,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addObjectEntry(
			userId, groupId, objectDefinitionId, values, serviceContext);
	}

	/**
	 * Adds the object entry to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectEntry the object entry
	 * @return the object entry that was added
	 */
	public static ObjectEntry addObjectEntry(ObjectEntry objectEntry) {
		return getService().addObjectEntry(objectEntry);
	}

	public static ObjectEntry addOrUpdateObjectEntry(
			String externalReferenceCode, long userId, long groupId,
			long objectDefinitionId, Map<String, Serializable> values,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addOrUpdateObjectEntry(
			externalReferenceCode, userId, groupId, objectDefinitionId, values,
			serviceContext);
	}

	/**
	 * Creates a new object entry with the primary key. Does not add the object entry to the database.
	 *
	 * @param objectEntryId the primary key for the new object entry
	 * @return the new object entry
	 */
	public static ObjectEntry createObjectEntry(long objectEntryId) {
		return getService().createObjectEntry(objectEntryId);
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
	 * Deletes the object entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectEntryId the primary key of the object entry
	 * @return the object entry that was removed
	 * @throws PortalException if a object entry with the primary key could not be found
	 */
	public static ObjectEntry deleteObjectEntry(long objectEntryId)
		throws PortalException {

		return getService().deleteObjectEntry(objectEntryId);
	}

	/**
	 * Deletes the object entry from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectEntry the object entry
	 * @return the object entry that was removed
	 * @throws PortalException
	 */
	public static ObjectEntry deleteObjectEntry(ObjectEntry objectEntry)
		throws PortalException {

		return getService().deleteObjectEntry(objectEntry);
	}

	public static ObjectEntry deleteObjectEntry(
			String externalReferenceCode, long companyId, long groupId)
		throws PortalException {

		return getService().deleteObjectEntry(
			externalReferenceCode, companyId, groupId);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel deletePersistedModel(
			PersistedModel persistedModel)
		throws PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static void deleteRelatedObjectEntries(
			long groupId, long objectDefinitionId, long primaryKey)
		throws PortalException {

		getService().deleteRelatedObjectEntries(
			groupId, objectDefinitionId, primaryKey);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.object.model.impl.ObjectEntryModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.object.model.impl.ObjectEntryModelImpl</code>.
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

	public static ObjectEntry fetchObjectEntry(long objectEntryId) {
		return getService().fetchObjectEntry(objectEntryId);
	}

	/**
	 * Returns the object entry matching the UUID and group.
	 *
	 * @param uuid the object entry's UUID
	 * @param groupId the primary key of the group
	 * @return the matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchObjectEntryByUuidAndGroupId(
		String uuid, long groupId) {

		return getService().fetchObjectEntryByUuidAndGroupId(uuid, groupId);
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

	public static List<ObjectEntry> getManyToManyRelatedObjectEntries(
			long groupId, long objectRelationshipId, long primaryKey,
			boolean reverse, int start, int end)
		throws PortalException {

		return getService().getManyToManyRelatedObjectEntries(
			groupId, objectRelationshipId, primaryKey, reverse, start, end);
	}

	public static int getManyToManyRelatedObjectEntriesCount(
			long groupId, long objectRelationshipId, long primaryKey,
			boolean reverse)
		throws PortalException {

		return getService().getManyToManyRelatedObjectEntriesCount(
			groupId, objectRelationshipId, primaryKey, reverse);
	}

	/**
	 * Returns a range of all the object entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.object.model.impl.ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @return the range of object entries
	 */
	public static List<ObjectEntry> getObjectEntries(int start, int end) {
		return getService().getObjectEntries(start, end);
	}

	public static List<ObjectEntry> getObjectEntries(
			long groupId, long objectDefinitionId, int start, int end)
		throws PortalException {

		return getService().getObjectEntries(
			groupId, objectDefinitionId, start, end);
	}

	public static List<ObjectEntry> getObjectEntries(
			long groupId, long objectDefinitionId, int status, int start,
			int end)
		throws PortalException {

		return getService().getObjectEntries(
			groupId, objectDefinitionId, status, start, end);
	}

	/**
	 * Returns all the object entries matching the UUID and company.
	 *
	 * @param uuid the UUID of the object entries
	 * @param companyId the primary key of the company
	 * @return the matching object entries, or an empty list if no matches were found
	 */
	public static List<ObjectEntry> getObjectEntriesByUuidAndCompanyId(
		String uuid, long companyId) {

		return getService().getObjectEntriesByUuidAndCompanyId(uuid, companyId);
	}

	/**
	 * Returns a range of object entries matching the UUID and company.
	 *
	 * @param uuid the UUID of the object entries
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching object entries, or an empty list if no matches were found
	 */
	public static List<ObjectEntry> getObjectEntriesByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getService().getObjectEntriesByUuidAndCompanyId(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of object entries.
	 *
	 * @return the number of object entries
	 */
	public static int getObjectEntriesCount() {
		return getService().getObjectEntriesCount();
	}

	public static int getObjectEntriesCount(
		long groupId, long objectDefinitionId) {

		return getService().getObjectEntriesCount(groupId, objectDefinitionId);
	}

	/**
	 * Returns the object entry with the primary key.
	 *
	 * @param objectEntryId the primary key of the object entry
	 * @return the object entry
	 * @throws PortalException if a object entry with the primary key could not be found
	 */
	public static ObjectEntry getObjectEntry(long objectEntryId)
		throws PortalException {

		return getService().getObjectEntry(objectEntryId);
	}

	public static ObjectEntry getObjectEntry(
			String externalReferenceCode, long companyId, long groupId)
		throws PortalException {

		return getService().getObjectEntry(
			externalReferenceCode, companyId, groupId);
	}

	/**
	 * Returns the object entry matching the UUID and group.
	 *
	 * @param uuid the object entry's UUID
	 * @param groupId the primary key of the group
	 * @return the matching object entry
	 * @throws PortalException if a matching object entry could not be found
	 */
	public static ObjectEntry getObjectEntryByUuidAndGroupId(
			String uuid, long groupId)
		throws PortalException {

		return getService().getObjectEntryByUuidAndGroupId(uuid, groupId);
	}

	public static List<ObjectEntry> getOneToManyRelatedObjectEntries(
			long groupId, long objectRelationshipId, long primaryKey, int start,
			int end)
		throws PortalException {

		return getService().getOneToManyRelatedObjectEntries(
			groupId, objectRelationshipId, primaryKey, start, end);
	}

	public static int getOneToManyRelatedObjectEntriesCount(
			long groupId, long objectRelationshipId, long primaryKey)
		throws PortalException {

		return getService().getOneToManyRelatedObjectEntriesCount(
			groupId, objectRelationshipId, primaryKey);
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

	public static Map<String, Object> getSystemModelAttributes(
			com.liferay.object.model.ObjectDefinition objectDefinition,
			long objectEntryId)
		throws PortalException {

		return getService().getSystemModelAttributes(
			objectDefinition, objectEntryId);
	}

	public static Map<String, Serializable> getValues(long objectEntryId)
		throws PortalException {

		return getService().getValues(objectEntryId);
	}

	public static Map<String, Serializable> getValues(ObjectEntry objectEntry)
		throws PortalException {

		return getService().getValues(objectEntry);
	}

	public static List<Map<String, Serializable>> getValuesList(
			long objectDefinitionId, int[] statuses, int start, int end)
		throws PortalException {

		return getService().getValuesList(
			objectDefinitionId, statuses, start, end);
	}

	public static void insertIntoOrUpdateExtensionTable(
			long objectDefinitionId, long primaryKey,
			Map<String, Serializable> values)
		throws PortalException {

		getService().insertIntoOrUpdateExtensionTable(
			objectDefinitionId, primaryKey, values);
	}

	public static com.liferay.portal.kernel.search.BaseModelSearchResult
		<ObjectEntry> searchObjectEntries(
				long groupId, long objectDefinitionId, String keywords, int cur,
				int delta)
			throws PortalException {

		return getService().searchObjectEntries(
			groupId, objectDefinitionId, keywords, cur, delta);
	}

	public static void updateAsset(
			long userId, ObjectEntry objectEntry, long[] assetCategoryIds,
			String[] assetTagNames, long[] assetLinkEntryIds, Double priority)
		throws PortalException {

		getService().updateAsset(
			userId, objectEntry, assetCategoryIds, assetTagNames,
			assetLinkEntryIds, priority);
	}

	public static ObjectEntry updateObjectEntry(
			long userId, long objectEntryId, Map<String, Serializable> values,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateObjectEntry(
			userId, objectEntryId, values, serviceContext);
	}

	/**
	 * Updates the object entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectEntry the object entry
	 * @return the object entry that was updated
	 */
	public static ObjectEntry updateObjectEntry(ObjectEntry objectEntry) {
		return getService().updateObjectEntry(objectEntry);
	}

	public static ObjectEntry updateStatus(
			long userId, long objectEntryId, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateStatus(
			userId, objectEntryId, status, serviceContext);
	}

	public static ObjectEntryLocalService getService() {
		return _service;
	}

	private static volatile ObjectEntryLocalService _service;

}