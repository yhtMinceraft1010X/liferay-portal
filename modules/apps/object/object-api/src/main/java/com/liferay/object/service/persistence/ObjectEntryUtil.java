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

package com.liferay.object.service.persistence;

import com.liferay.object.model.ObjectEntry;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence utility for the object entry service. This utility wraps <code>com.liferay.object.service.persistence.impl.ObjectEntryPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Marco Leo
 * @see ObjectEntryPersistence
 * @generated
 */
public class ObjectEntryUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(ObjectEntry objectEntry) {
		getPersistence().clearCache(objectEntry);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, ObjectEntry> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<ObjectEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<ObjectEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<ObjectEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static ObjectEntry update(ObjectEntry objectEntry) {
		return getPersistence().update(objectEntry);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static ObjectEntry update(
		ObjectEntry objectEntry, ServiceContext serviceContext) {

		return getPersistence().update(objectEntry, serviceContext);
	}

	/**
	 * Returns all the object entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching object entries
	 */
	public static List<ObjectEntry> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
	}

	/**
	 * Returns a range of all the object entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @return the range of matching object entries
	 */
	public static List<ObjectEntry> findByUuid(
		String uuid, int start, int end) {

		return getPersistence().findByUuid(uuid, start, end);
	}

	/**
	 * Returns an ordered range of all the object entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the object entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first object entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByUuid_First(
			String uuid, OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first object entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByUuid_First(
		String uuid, OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByUuid_Last(
			String uuid, OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByUuid_Last(
		String uuid, OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the object entries before and after the current object entry in the ordered set where uuid = &#63;.
	 *
	 * @param objectEntryId the primary key of the current object entry
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next object entry
	 * @throws NoSuchObjectEntryException if a object entry with the primary key could not be found
	 */
	public static ObjectEntry[] findByUuid_PrevAndNext(
			long objectEntryId, String uuid,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByUuid_PrevAndNext(
			objectEntryId, uuid, orderByComparator);
	}

	/**
	 * Removes all the object entries where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of object entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching object entries
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns the object entry where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchObjectEntryException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByUUID_G(String uuid, long groupId)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the object entry where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByUUID_G(String uuid, long groupId) {
		return getPersistence().fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the object entry where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		return getPersistence().fetchByUUID_G(uuid, groupId, useFinderCache);
	}

	/**
	 * Removes the object entry where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the object entry that was removed
	 */
	public static ObjectEntry removeByUUID_G(String uuid, long groupId)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().removeByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the number of object entries where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching object entries
	 */
	public static int countByUUID_G(String uuid, long groupId) {
		return getPersistence().countByUUID_G(uuid, groupId);
	}

	/**
	 * Returns all the object entries where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching object entries
	 */
	public static List<ObjectEntry> findByUuid_C(String uuid, long companyId) {
		return getPersistence().findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of all the object entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @return the range of matching object entries
	 */
	public static List<ObjectEntry> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().findByUuid_C(uuid, companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the object entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the object entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first object entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the first object entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the object entries before and after the current object entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param objectEntryId the primary key of the current object entry
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next object entry
	 * @throws NoSuchObjectEntryException if a object entry with the primary key could not be found
	 */
	public static ObjectEntry[] findByUuid_C_PrevAndNext(
			long objectEntryId, String uuid, long companyId,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByUuid_C_PrevAndNext(
			objectEntryId, uuid, companyId, orderByComparator);
	}

	/**
	 * Removes all the object entries where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public static void removeByUuid_C(String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of object entries where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching object entries
	 */
	public static int countByUuid_C(String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
	}

	/**
	 * Returns all the object entries where objectDefinitionId = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @return the matching object entries
	 */
	public static List<ObjectEntry> findByObjectDefinitionId(
		long objectDefinitionId) {

		return getPersistence().findByObjectDefinitionId(objectDefinitionId);
	}

	/**
	 * Returns a range of all the object entries where objectDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @return the range of matching object entries
	 */
	public static List<ObjectEntry> findByObjectDefinitionId(
		long objectDefinitionId, int start, int end) {

		return getPersistence().findByObjectDefinitionId(
			objectDefinitionId, start, end);
	}

	/**
	 * Returns an ordered range of all the object entries where objectDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByObjectDefinitionId(
		long objectDefinitionId, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().findByObjectDefinitionId(
			objectDefinitionId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the object entries where objectDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByObjectDefinitionId(
		long objectDefinitionId, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByObjectDefinitionId(
			objectDefinitionId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first object entry in the ordered set where objectDefinitionId = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByObjectDefinitionId_First(
			long objectDefinitionId,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByObjectDefinitionId_First(
			objectDefinitionId, orderByComparator);
	}

	/**
	 * Returns the first object entry in the ordered set where objectDefinitionId = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByObjectDefinitionId_First(
		long objectDefinitionId,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByObjectDefinitionId_First(
			objectDefinitionId, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where objectDefinitionId = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByObjectDefinitionId_Last(
			long objectDefinitionId,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByObjectDefinitionId_Last(
			objectDefinitionId, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where objectDefinitionId = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByObjectDefinitionId_Last(
		long objectDefinitionId,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByObjectDefinitionId_Last(
			objectDefinitionId, orderByComparator);
	}

	/**
	 * Returns the object entries before and after the current object entry in the ordered set where objectDefinitionId = &#63;.
	 *
	 * @param objectEntryId the primary key of the current object entry
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next object entry
	 * @throws NoSuchObjectEntryException if a object entry with the primary key could not be found
	 */
	public static ObjectEntry[] findByObjectDefinitionId_PrevAndNext(
			long objectEntryId, long objectDefinitionId,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByObjectDefinitionId_PrevAndNext(
			objectEntryId, objectDefinitionId, orderByComparator);
	}

	/**
	 * Removes all the object entries where objectDefinitionId = &#63; from the database.
	 *
	 * @param objectDefinitionId the object definition ID
	 */
	public static void removeByObjectDefinitionId(long objectDefinitionId) {
		getPersistence().removeByObjectDefinitionId(objectDefinitionId);
	}

	/**
	 * Returns the number of object entries where objectDefinitionId = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @return the number of matching object entries
	 */
	public static int countByObjectDefinitionId(long objectDefinitionId) {
		return getPersistence().countByObjectDefinitionId(objectDefinitionId);
	}

	/**
	 * Returns all the object entries where groupId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @return the matching object entries
	 */
	public static List<ObjectEntry> findByG_ODI(
		long groupId, long objectDefinitionId) {

		return getPersistence().findByG_ODI(groupId, objectDefinitionId);
	}

	/**
	 * Returns a range of all the object entries where groupId = &#63; and objectDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @return the range of matching object entries
	 */
	public static List<ObjectEntry> findByG_ODI(
		long groupId, long objectDefinitionId, int start, int end) {

		return getPersistence().findByG_ODI(
			groupId, objectDefinitionId, start, end);
	}

	/**
	 * Returns an ordered range of all the object entries where groupId = &#63; and objectDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByG_ODI(
		long groupId, long objectDefinitionId, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().findByG_ODI(
			groupId, objectDefinitionId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the object entries where groupId = &#63; and objectDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByG_ODI(
		long groupId, long objectDefinitionId, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByG_ODI(
			groupId, objectDefinitionId, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first object entry in the ordered set where groupId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByG_ODI_First(
			long groupId, long objectDefinitionId,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByG_ODI_First(
			groupId, objectDefinitionId, orderByComparator);
	}

	/**
	 * Returns the first object entry in the ordered set where groupId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByG_ODI_First(
		long groupId, long objectDefinitionId,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByG_ODI_First(
			groupId, objectDefinitionId, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where groupId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByG_ODI_Last(
			long groupId, long objectDefinitionId,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByG_ODI_Last(
			groupId, objectDefinitionId, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where groupId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByG_ODI_Last(
		long groupId, long objectDefinitionId,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByG_ODI_Last(
			groupId, objectDefinitionId, orderByComparator);
	}

	/**
	 * Returns the object entries before and after the current object entry in the ordered set where groupId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param objectEntryId the primary key of the current object entry
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next object entry
	 * @throws NoSuchObjectEntryException if a object entry with the primary key could not be found
	 */
	public static ObjectEntry[] findByG_ODI_PrevAndNext(
			long objectEntryId, long groupId, long objectDefinitionId,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByG_ODI_PrevAndNext(
			objectEntryId, groupId, objectDefinitionId, orderByComparator);
	}

	/**
	 * Removes all the object entries where groupId = &#63; and objectDefinitionId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 */
	public static void removeByG_ODI(long groupId, long objectDefinitionId) {
		getPersistence().removeByG_ODI(groupId, objectDefinitionId);
	}

	/**
	 * Returns the number of object entries where groupId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @return the number of matching object entries
	 */
	public static int countByG_ODI(long groupId, long objectDefinitionId) {
		return getPersistence().countByG_ODI(groupId, objectDefinitionId);
	}

	/**
	 * Returns all the object entries where userId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param userId the user ID
	 * @param objectDefinitionId the object definition ID
	 * @return the matching object entries
	 */
	public static List<ObjectEntry> findByU_ODI(
		long userId, long objectDefinitionId) {

		return getPersistence().findByU_ODI(userId, objectDefinitionId);
	}

	/**
	 * Returns a range of all the object entries where userId = &#63; and objectDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param objectDefinitionId the object definition ID
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @return the range of matching object entries
	 */
	public static List<ObjectEntry> findByU_ODI(
		long userId, long objectDefinitionId, int start, int end) {

		return getPersistence().findByU_ODI(
			userId, objectDefinitionId, start, end);
	}

	/**
	 * Returns an ordered range of all the object entries where userId = &#63; and objectDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param objectDefinitionId the object definition ID
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByU_ODI(
		long userId, long objectDefinitionId, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().findByU_ODI(
			userId, objectDefinitionId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the object entries where userId = &#63; and objectDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param objectDefinitionId the object definition ID
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByU_ODI(
		long userId, long objectDefinitionId, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByU_ODI(
			userId, objectDefinitionId, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first object entry in the ordered set where userId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param userId the user ID
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByU_ODI_First(
			long userId, long objectDefinitionId,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByU_ODI_First(
			userId, objectDefinitionId, orderByComparator);
	}

	/**
	 * Returns the first object entry in the ordered set where userId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param userId the user ID
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByU_ODI_First(
		long userId, long objectDefinitionId,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByU_ODI_First(
			userId, objectDefinitionId, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where userId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param userId the user ID
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByU_ODI_Last(
			long userId, long objectDefinitionId,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByU_ODI_Last(
			userId, objectDefinitionId, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where userId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param userId the user ID
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByU_ODI_Last(
		long userId, long objectDefinitionId,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByU_ODI_Last(
			userId, objectDefinitionId, orderByComparator);
	}

	/**
	 * Returns the object entries before and after the current object entry in the ordered set where userId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param objectEntryId the primary key of the current object entry
	 * @param userId the user ID
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next object entry
	 * @throws NoSuchObjectEntryException if a object entry with the primary key could not be found
	 */
	public static ObjectEntry[] findByU_ODI_PrevAndNext(
			long objectEntryId, long userId, long objectDefinitionId,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByU_ODI_PrevAndNext(
			objectEntryId, userId, objectDefinitionId, orderByComparator);
	}

	/**
	 * Removes all the object entries where userId = &#63; and objectDefinitionId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @param objectDefinitionId the object definition ID
	 */
	public static void removeByU_ODI(long userId, long objectDefinitionId) {
		getPersistence().removeByU_ODI(userId, objectDefinitionId);
	}

	/**
	 * Returns the number of object entries where userId = &#63; and objectDefinitionId = &#63;.
	 *
	 * @param userId the user ID
	 * @param objectDefinitionId the object definition ID
	 * @return the number of matching object entries
	 */
	public static int countByU_ODI(long userId, long objectDefinitionId) {
		return getPersistence().countByU_ODI(userId, objectDefinitionId);
	}

	/**
	 * Returns all the object entries where objectDefinitionId = &#63; and status &ne; &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @return the matching object entries
	 */
	public static List<ObjectEntry> findByODI_NotS(
		long objectDefinitionId, int status) {

		return getPersistence().findByODI_NotS(objectDefinitionId, status);
	}

	/**
	 * Returns a range of all the object entries where objectDefinitionId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @return the range of matching object entries
	 */
	public static List<ObjectEntry> findByODI_NotS(
		long objectDefinitionId, int status, int start, int end) {

		return getPersistence().findByODI_NotS(
			objectDefinitionId, status, start, end);
	}

	/**
	 * Returns an ordered range of all the object entries where objectDefinitionId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByODI_NotS(
		long objectDefinitionId, int status, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().findByODI_NotS(
			objectDefinitionId, status, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the object entries where objectDefinitionId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByODI_NotS(
		long objectDefinitionId, int status, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByODI_NotS(
			objectDefinitionId, status, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first object entry in the ordered set where objectDefinitionId = &#63; and status &ne; &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByODI_NotS_First(
			long objectDefinitionId, int status,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByODI_NotS_First(
			objectDefinitionId, status, orderByComparator);
	}

	/**
	 * Returns the first object entry in the ordered set where objectDefinitionId = &#63; and status &ne; &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByODI_NotS_First(
		long objectDefinitionId, int status,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByODI_NotS_First(
			objectDefinitionId, status, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where objectDefinitionId = &#63; and status &ne; &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByODI_NotS_Last(
			long objectDefinitionId, int status,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByODI_NotS_Last(
			objectDefinitionId, status, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where objectDefinitionId = &#63; and status &ne; &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByODI_NotS_Last(
		long objectDefinitionId, int status,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByODI_NotS_Last(
			objectDefinitionId, status, orderByComparator);
	}

	/**
	 * Returns the object entries before and after the current object entry in the ordered set where objectDefinitionId = &#63; and status &ne; &#63;.
	 *
	 * @param objectEntryId the primary key of the current object entry
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next object entry
	 * @throws NoSuchObjectEntryException if a object entry with the primary key could not be found
	 */
	public static ObjectEntry[] findByODI_NotS_PrevAndNext(
			long objectEntryId, long objectDefinitionId, int status,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByODI_NotS_PrevAndNext(
			objectEntryId, objectDefinitionId, status, orderByComparator);
	}

	/**
	 * Removes all the object entries where objectDefinitionId = &#63; and status &ne; &#63; from the database.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 */
	public static void removeByODI_NotS(long objectDefinitionId, int status) {
		getPersistence().removeByODI_NotS(objectDefinitionId, status);
	}

	/**
	 * Returns the number of object entries where objectDefinitionId = &#63; and status &ne; &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @return the number of matching object entries
	 */
	public static int countByODI_NotS(long objectDefinitionId, int status) {
		return getPersistence().countByODI_NotS(objectDefinitionId, status);
	}

	/**
	 * Returns the object entry where groupId = &#63; and companyId = &#63; and externalReferenceCode = &#63; or throws a <code>NoSuchObjectEntryException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByG_C_ERC(
			long groupId, long companyId, String externalReferenceCode)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByG_C_ERC(
			groupId, companyId, externalReferenceCode);
	}

	/**
	 * Returns the object entry where groupId = &#63; and companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByG_C_ERC(
		long groupId, long companyId, String externalReferenceCode) {

		return getPersistence().fetchByG_C_ERC(
			groupId, companyId, externalReferenceCode);
	}

	/**
	 * Returns the object entry where groupId = &#63; and companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByG_C_ERC(
		long groupId, long companyId, String externalReferenceCode,
		boolean useFinderCache) {

		return getPersistence().fetchByG_C_ERC(
			groupId, companyId, externalReferenceCode, useFinderCache);
	}

	/**
	 * Removes the object entry where groupId = &#63; and companyId = &#63; and externalReferenceCode = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the object entry that was removed
	 */
	public static ObjectEntry removeByG_C_ERC(
			long groupId, long companyId, String externalReferenceCode)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().removeByG_C_ERC(
			groupId, companyId, externalReferenceCode);
	}

	/**
	 * Returns the number of object entries where groupId = &#63; and companyId = &#63; and externalReferenceCode = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the number of matching object entries
	 */
	public static int countByG_C_ERC(
		long groupId, long companyId, String externalReferenceCode) {

		return getPersistence().countByG_C_ERC(
			groupId, companyId, externalReferenceCode);
	}

	/**
	 * Returns all the object entries where groupId = &#63; and objectDefinitionId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @return the matching object entries
	 */
	public static List<ObjectEntry> findByG_ODI_S(
		long groupId, long objectDefinitionId, int status) {

		return getPersistence().findByG_ODI_S(
			groupId, objectDefinitionId, status);
	}

	/**
	 * Returns a range of all the object entries where groupId = &#63; and objectDefinitionId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @return the range of matching object entries
	 */
	public static List<ObjectEntry> findByG_ODI_S(
		long groupId, long objectDefinitionId, int status, int start, int end) {

		return getPersistence().findByG_ODI_S(
			groupId, objectDefinitionId, status, start, end);
	}

	/**
	 * Returns an ordered range of all the object entries where groupId = &#63; and objectDefinitionId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByG_ODI_S(
		long groupId, long objectDefinitionId, int status, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().findByG_ODI_S(
			groupId, objectDefinitionId, status, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the object entries where groupId = &#63; and objectDefinitionId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching object entries
	 */
	public static List<ObjectEntry> findByG_ODI_S(
		long groupId, long objectDefinitionId, int status, int start, int end,
		OrderByComparator<ObjectEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByG_ODI_S(
			groupId, objectDefinitionId, status, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first object entry in the ordered set where groupId = &#63; and objectDefinitionId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByG_ODI_S_First(
			long groupId, long objectDefinitionId, int status,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByG_ODI_S_First(
			groupId, objectDefinitionId, status, orderByComparator);
	}

	/**
	 * Returns the first object entry in the ordered set where groupId = &#63; and objectDefinitionId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByG_ODI_S_First(
		long groupId, long objectDefinitionId, int status,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByG_ODI_S_First(
			groupId, objectDefinitionId, status, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where groupId = &#63; and objectDefinitionId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry
	 * @throws NoSuchObjectEntryException if a matching object entry could not be found
	 */
	public static ObjectEntry findByG_ODI_S_Last(
			long groupId, long objectDefinitionId, int status,
			OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByG_ODI_S_Last(
			groupId, objectDefinitionId, status, orderByComparator);
	}

	/**
	 * Returns the last object entry in the ordered set where groupId = &#63; and objectDefinitionId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object entry, or <code>null</code> if a matching object entry could not be found
	 */
	public static ObjectEntry fetchByG_ODI_S_Last(
		long groupId, long objectDefinitionId, int status,
		OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().fetchByG_ODI_S_Last(
			groupId, objectDefinitionId, status, orderByComparator);
	}

	/**
	 * Returns the object entries before and after the current object entry in the ordered set where groupId = &#63; and objectDefinitionId = &#63; and status = &#63;.
	 *
	 * @param objectEntryId the primary key of the current object entry
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next object entry
	 * @throws NoSuchObjectEntryException if a object entry with the primary key could not be found
	 */
	public static ObjectEntry[] findByG_ODI_S_PrevAndNext(
			long objectEntryId, long groupId, long objectDefinitionId,
			int status, OrderByComparator<ObjectEntry> orderByComparator)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByG_ODI_S_PrevAndNext(
			objectEntryId, groupId, objectDefinitionId, status,
			orderByComparator);
	}

	/**
	 * Removes all the object entries where groupId = &#63; and objectDefinitionId = &#63; and status = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 */
	public static void removeByG_ODI_S(
		long groupId, long objectDefinitionId, int status) {

		getPersistence().removeByG_ODI_S(groupId, objectDefinitionId, status);
	}

	/**
	 * Returns the number of object entries where groupId = &#63; and objectDefinitionId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param objectDefinitionId the object definition ID
	 * @param status the status
	 * @return the number of matching object entries
	 */
	public static int countByG_ODI_S(
		long groupId, long objectDefinitionId, int status) {

		return getPersistence().countByG_ODI_S(
			groupId, objectDefinitionId, status);
	}

	/**
	 * Caches the object entry in the entity cache if it is enabled.
	 *
	 * @param objectEntry the object entry
	 */
	public static void cacheResult(ObjectEntry objectEntry) {
		getPersistence().cacheResult(objectEntry);
	}

	/**
	 * Caches the object entries in the entity cache if it is enabled.
	 *
	 * @param objectEntries the object entries
	 */
	public static void cacheResult(List<ObjectEntry> objectEntries) {
		getPersistence().cacheResult(objectEntries);
	}

	/**
	 * Creates a new object entry with the primary key. Does not add the object entry to the database.
	 *
	 * @param objectEntryId the primary key for the new object entry
	 * @return the new object entry
	 */
	public static ObjectEntry create(long objectEntryId) {
		return getPersistence().create(objectEntryId);
	}

	/**
	 * Removes the object entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param objectEntryId the primary key of the object entry
	 * @return the object entry that was removed
	 * @throws NoSuchObjectEntryException if a object entry with the primary key could not be found
	 */
	public static ObjectEntry remove(long objectEntryId)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().remove(objectEntryId);
	}

	public static ObjectEntry updateImpl(ObjectEntry objectEntry) {
		return getPersistence().updateImpl(objectEntry);
	}

	/**
	 * Returns the object entry with the primary key or throws a <code>NoSuchObjectEntryException</code> if it could not be found.
	 *
	 * @param objectEntryId the primary key of the object entry
	 * @return the object entry
	 * @throws NoSuchObjectEntryException if a object entry with the primary key could not be found
	 */
	public static ObjectEntry findByPrimaryKey(long objectEntryId)
		throws com.liferay.object.exception.NoSuchObjectEntryException {

		return getPersistence().findByPrimaryKey(objectEntryId);
	}

	/**
	 * Returns the object entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param objectEntryId the primary key of the object entry
	 * @return the object entry, or <code>null</code> if a object entry with the primary key could not be found
	 */
	public static ObjectEntry fetchByPrimaryKey(long objectEntryId) {
		return getPersistence().fetchByPrimaryKey(objectEntryId);
	}

	/**
	 * Returns all the object entries.
	 *
	 * @return the object entries
	 */
	public static List<ObjectEntry> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the object entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @return the range of object entries
	 */
	public static List<ObjectEntry> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the object entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of object entries
	 */
	public static List<ObjectEntry> findAll(
		int start, int end, OrderByComparator<ObjectEntry> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the object entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of object entries
	 * @param end the upper bound of the range of object entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of object entries
	 */
	public static List<ObjectEntry> findAll(
		int start, int end, OrderByComparator<ObjectEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the object entries from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of object entries.
	 *
	 * @return the number of object entries
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static ObjectEntryPersistence getPersistence() {
		return _persistence;
	}

	private static volatile ObjectEntryPersistence _persistence;

}