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

package com.liferay.custom.elements.service.persistence;

import com.liferay.custom.elements.model.CustomElementsPortletDescriptor;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the custom elements portlet descriptor service. This utility wraps <code>com.liferay.custom.elements.service.persistence.impl.CustomElementsPortletDescriptorPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CustomElementsPortletDescriptorPersistence
 * @generated
 */
public class CustomElementsPortletDescriptorUtil {

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
	public static void clearCache(
		CustomElementsPortletDescriptor customElementsPortletDescriptor) {

		getPersistence().clearCache(customElementsPortletDescriptor);
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
	public static Map<Serializable, CustomElementsPortletDescriptor>
		fetchByPrimaryKeys(Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<CustomElementsPortletDescriptor> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<CustomElementsPortletDescriptor> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<CustomElementsPortletDescriptor> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<CustomElementsPortletDescriptor> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static CustomElementsPortletDescriptor update(
		CustomElementsPortletDescriptor customElementsPortletDescriptor) {

		return getPersistence().update(customElementsPortletDescriptor);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static CustomElementsPortletDescriptor update(
		CustomElementsPortletDescriptor customElementsPortletDescriptor,
		ServiceContext serviceContext) {

		return getPersistence().update(
			customElementsPortletDescriptor, serviceContext);
	}

	/**
	 * Returns all the custom elements portlet descriptors where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching custom elements portlet descriptors
	 */
	public static List<CustomElementsPortletDescriptor> findByUuid(
		String uuid) {

		return getPersistence().findByUuid(uuid);
	}

	/**
	 * Returns a range of all the custom elements portlet descriptors where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CustomElementsPortletDescriptorModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of custom elements portlet descriptors
	 * @param end the upper bound of the range of custom elements portlet descriptors (not inclusive)
	 * @return the range of matching custom elements portlet descriptors
	 */
	public static List<CustomElementsPortletDescriptor> findByUuid(
		String uuid, int start, int end) {

		return getPersistence().findByUuid(uuid, start, end);
	}

	/**
	 * Returns an ordered range of all the custom elements portlet descriptors where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CustomElementsPortletDescriptorModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of custom elements portlet descriptors
	 * @param end the upper bound of the range of custom elements portlet descriptors (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching custom elements portlet descriptors
	 */
	public static List<CustomElementsPortletDescriptor> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<CustomElementsPortletDescriptor> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the custom elements portlet descriptors where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CustomElementsPortletDescriptorModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of custom elements portlet descriptors
	 * @param end the upper bound of the range of custom elements portlet descriptors (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching custom elements portlet descriptors
	 */
	public static List<CustomElementsPortletDescriptor> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<CustomElementsPortletDescriptor> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first custom elements portlet descriptor in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a matching custom elements portlet descriptor could not be found
	 */
	public static CustomElementsPortletDescriptor findByUuid_First(
			String uuid,
			OrderByComparator<CustomElementsPortletDescriptor>
				orderByComparator)
		throws com.liferay.custom.elements.exception.
			NoSuchCustomElementsPortletDescriptorException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first custom elements portlet descriptor in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching custom elements portlet descriptor, or <code>null</code> if a matching custom elements portlet descriptor could not be found
	 */
	public static CustomElementsPortletDescriptor fetchByUuid_First(
		String uuid,
		OrderByComparator<CustomElementsPortletDescriptor> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last custom elements portlet descriptor in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a matching custom elements portlet descriptor could not be found
	 */
	public static CustomElementsPortletDescriptor findByUuid_Last(
			String uuid,
			OrderByComparator<CustomElementsPortletDescriptor>
				orderByComparator)
		throws com.liferay.custom.elements.exception.
			NoSuchCustomElementsPortletDescriptorException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last custom elements portlet descriptor in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching custom elements portlet descriptor, or <code>null</code> if a matching custom elements portlet descriptor could not be found
	 */
	public static CustomElementsPortletDescriptor fetchByUuid_Last(
		String uuid,
		OrderByComparator<CustomElementsPortletDescriptor> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the custom elements portlet descriptors before and after the current custom elements portlet descriptor in the ordered set where uuid = &#63;.
	 *
	 * @param customElementsPortletDescriptorId the primary key of the current custom elements portlet descriptor
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a custom elements portlet descriptor with the primary key could not be found
	 */
	public static CustomElementsPortletDescriptor[] findByUuid_PrevAndNext(
			long customElementsPortletDescriptorId, String uuid,
			OrderByComparator<CustomElementsPortletDescriptor>
				orderByComparator)
		throws com.liferay.custom.elements.exception.
			NoSuchCustomElementsPortletDescriptorException {

		return getPersistence().findByUuid_PrevAndNext(
			customElementsPortletDescriptorId, uuid, orderByComparator);
	}

	/**
	 * Removes all the custom elements portlet descriptors where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of custom elements portlet descriptors where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching custom elements portlet descriptors
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns all the custom elements portlet descriptors where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching custom elements portlet descriptors
	 */
	public static List<CustomElementsPortletDescriptor> findByUuid_C(
		String uuid, long companyId) {

		return getPersistence().findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of all the custom elements portlet descriptors where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CustomElementsPortletDescriptorModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of custom elements portlet descriptors
	 * @param end the upper bound of the range of custom elements portlet descriptors (not inclusive)
	 * @return the range of matching custom elements portlet descriptors
	 */
	public static List<CustomElementsPortletDescriptor> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().findByUuid_C(uuid, companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the custom elements portlet descriptors where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CustomElementsPortletDescriptorModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of custom elements portlet descriptors
	 * @param end the upper bound of the range of custom elements portlet descriptors (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching custom elements portlet descriptors
	 */
	public static List<CustomElementsPortletDescriptor> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<CustomElementsPortletDescriptor> orderByComparator) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the custom elements portlet descriptors where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CustomElementsPortletDescriptorModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of custom elements portlet descriptors
	 * @param end the upper bound of the range of custom elements portlet descriptors (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching custom elements portlet descriptors
	 */
	public static List<CustomElementsPortletDescriptor> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<CustomElementsPortletDescriptor> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first custom elements portlet descriptor in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a matching custom elements portlet descriptor could not be found
	 */
	public static CustomElementsPortletDescriptor findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<CustomElementsPortletDescriptor>
				orderByComparator)
		throws com.liferay.custom.elements.exception.
			NoSuchCustomElementsPortletDescriptorException {

		return getPersistence().findByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the first custom elements portlet descriptor in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching custom elements portlet descriptor, or <code>null</code> if a matching custom elements portlet descriptor could not be found
	 */
	public static CustomElementsPortletDescriptor fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<CustomElementsPortletDescriptor> orderByComparator) {

		return getPersistence().fetchByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last custom elements portlet descriptor in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a matching custom elements portlet descriptor could not be found
	 */
	public static CustomElementsPortletDescriptor findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<CustomElementsPortletDescriptor>
				orderByComparator)
		throws com.liferay.custom.elements.exception.
			NoSuchCustomElementsPortletDescriptorException {

		return getPersistence().findByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last custom elements portlet descriptor in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching custom elements portlet descriptor, or <code>null</code> if a matching custom elements portlet descriptor could not be found
	 */
	public static CustomElementsPortletDescriptor fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<CustomElementsPortletDescriptor> orderByComparator) {

		return getPersistence().fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the custom elements portlet descriptors before and after the current custom elements portlet descriptor in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param customElementsPortletDescriptorId the primary key of the current custom elements portlet descriptor
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a custom elements portlet descriptor with the primary key could not be found
	 */
	public static CustomElementsPortletDescriptor[] findByUuid_C_PrevAndNext(
			long customElementsPortletDescriptorId, String uuid, long companyId,
			OrderByComparator<CustomElementsPortletDescriptor>
				orderByComparator)
		throws com.liferay.custom.elements.exception.
			NoSuchCustomElementsPortletDescriptorException {

		return getPersistence().findByUuid_C_PrevAndNext(
			customElementsPortletDescriptorId, uuid, companyId,
			orderByComparator);
	}

	/**
	 * Removes all the custom elements portlet descriptors where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public static void removeByUuid_C(String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of custom elements portlet descriptors where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching custom elements portlet descriptors
	 */
	public static int countByUuid_C(String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
	}

	/**
	 * Caches the custom elements portlet descriptor in the entity cache if it is enabled.
	 *
	 * @param customElementsPortletDescriptor the custom elements portlet descriptor
	 */
	public static void cacheResult(
		CustomElementsPortletDescriptor customElementsPortletDescriptor) {

		getPersistence().cacheResult(customElementsPortletDescriptor);
	}

	/**
	 * Caches the custom elements portlet descriptors in the entity cache if it is enabled.
	 *
	 * @param customElementsPortletDescriptors the custom elements portlet descriptors
	 */
	public static void cacheResult(
		List<CustomElementsPortletDescriptor>
			customElementsPortletDescriptors) {

		getPersistence().cacheResult(customElementsPortletDescriptors);
	}

	/**
	 * Creates a new custom elements portlet descriptor with the primary key. Does not add the custom elements portlet descriptor to the database.
	 *
	 * @param customElementsPortletDescriptorId the primary key for the new custom elements portlet descriptor
	 * @return the new custom elements portlet descriptor
	 */
	public static CustomElementsPortletDescriptor create(
		long customElementsPortletDescriptorId) {

		return getPersistence().create(customElementsPortletDescriptorId);
	}

	/**
	 * Removes the custom elements portlet descriptor with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param customElementsPortletDescriptorId the primary key of the custom elements portlet descriptor
	 * @return the custom elements portlet descriptor that was removed
	 * @throws NoSuchCustomElementsPortletDescriptorException if a custom elements portlet descriptor with the primary key could not be found
	 */
	public static CustomElementsPortletDescriptor remove(
			long customElementsPortletDescriptorId)
		throws com.liferay.custom.elements.exception.
			NoSuchCustomElementsPortletDescriptorException {

		return getPersistence().remove(customElementsPortletDescriptorId);
	}

	public static CustomElementsPortletDescriptor updateImpl(
		CustomElementsPortletDescriptor customElementsPortletDescriptor) {

		return getPersistence().updateImpl(customElementsPortletDescriptor);
	}

	/**
	 * Returns the custom elements portlet descriptor with the primary key or throws a <code>NoSuchCustomElementsPortletDescriptorException</code> if it could not be found.
	 *
	 * @param customElementsPortletDescriptorId the primary key of the custom elements portlet descriptor
	 * @return the custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a custom elements portlet descriptor with the primary key could not be found
	 */
	public static CustomElementsPortletDescriptor findByPrimaryKey(
			long customElementsPortletDescriptorId)
		throws com.liferay.custom.elements.exception.
			NoSuchCustomElementsPortletDescriptorException {

		return getPersistence().findByPrimaryKey(
			customElementsPortletDescriptorId);
	}

	/**
	 * Returns the custom elements portlet descriptor with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param customElementsPortletDescriptorId the primary key of the custom elements portlet descriptor
	 * @return the custom elements portlet descriptor, or <code>null</code> if a custom elements portlet descriptor with the primary key could not be found
	 */
	public static CustomElementsPortletDescriptor fetchByPrimaryKey(
		long customElementsPortletDescriptorId) {

		return getPersistence().fetchByPrimaryKey(
			customElementsPortletDescriptorId);
	}

	/**
	 * Returns all the custom elements portlet descriptors.
	 *
	 * @return the custom elements portlet descriptors
	 */
	public static List<CustomElementsPortletDescriptor> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the custom elements portlet descriptors.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CustomElementsPortletDescriptorModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of custom elements portlet descriptors
	 * @param end the upper bound of the range of custom elements portlet descriptors (not inclusive)
	 * @return the range of custom elements portlet descriptors
	 */
	public static List<CustomElementsPortletDescriptor> findAll(
		int start, int end) {

		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the custom elements portlet descriptors.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CustomElementsPortletDescriptorModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of custom elements portlet descriptors
	 * @param end the upper bound of the range of custom elements portlet descriptors (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of custom elements portlet descriptors
	 */
	public static List<CustomElementsPortletDescriptor> findAll(
		int start, int end,
		OrderByComparator<CustomElementsPortletDescriptor> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the custom elements portlet descriptors.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CustomElementsPortletDescriptorModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of custom elements portlet descriptors
	 * @param end the upper bound of the range of custom elements portlet descriptors (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of custom elements portlet descriptors
	 */
	public static List<CustomElementsPortletDescriptor> findAll(
		int start, int end,
		OrderByComparator<CustomElementsPortletDescriptor> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the custom elements portlet descriptors from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of custom elements portlet descriptors.
	 *
	 * @return the number of custom elements portlet descriptors
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static CustomElementsPortletDescriptorPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<CustomElementsPortletDescriptorPersistence,
		 CustomElementsPortletDescriptorPersistence> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			CustomElementsPortletDescriptorPersistence.class);

		ServiceTracker
			<CustomElementsPortletDescriptorPersistence,
			 CustomElementsPortletDescriptorPersistence> serviceTracker =
				new ServiceTracker
					<CustomElementsPortletDescriptorPersistence,
					 CustomElementsPortletDescriptorPersistence>(
						 bundle.getBundleContext(),
						 CustomElementsPortletDescriptorPersistence.class,
						 null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}