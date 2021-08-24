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

import com.liferay.custom.elements.exception.NoSuchCustomElementsPortletDescriptorException;
import com.liferay.custom.elements.model.CustomElementsPortletDescriptor;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the custom elements portlet descriptor service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CustomElementsPortletDescriptorUtil
 * @generated
 */
@ProviderType
public interface CustomElementsPortletDescriptorPersistence
	extends BasePersistence<CustomElementsPortletDescriptor> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link CustomElementsPortletDescriptorUtil} to access the custom elements portlet descriptor persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the custom elements portlet descriptors where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching custom elements portlet descriptors
	 */
	public java.util.List<CustomElementsPortletDescriptor> findByUuid(
		String uuid);

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
	public java.util.List<CustomElementsPortletDescriptor> findByUuid(
		String uuid, int start, int end);

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
	public java.util.List<CustomElementsPortletDescriptor> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CustomElementsPortletDescriptor> orderByComparator);

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
	public java.util.List<CustomElementsPortletDescriptor> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CustomElementsPortletDescriptor> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first custom elements portlet descriptor in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a matching custom elements portlet descriptor could not be found
	 */
	public CustomElementsPortletDescriptor findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator
				<CustomElementsPortletDescriptor> orderByComparator)
		throws NoSuchCustomElementsPortletDescriptorException;

	/**
	 * Returns the first custom elements portlet descriptor in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching custom elements portlet descriptor, or <code>null</code> if a matching custom elements portlet descriptor could not be found
	 */
	public CustomElementsPortletDescriptor fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator
			<CustomElementsPortletDescriptor> orderByComparator);

	/**
	 * Returns the last custom elements portlet descriptor in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a matching custom elements portlet descriptor could not be found
	 */
	public CustomElementsPortletDescriptor findByUuid_Last(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator
				<CustomElementsPortletDescriptor> orderByComparator)
		throws NoSuchCustomElementsPortletDescriptorException;

	/**
	 * Returns the last custom elements portlet descriptor in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching custom elements portlet descriptor, or <code>null</code> if a matching custom elements portlet descriptor could not be found
	 */
	public CustomElementsPortletDescriptor fetchByUuid_Last(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator
			<CustomElementsPortletDescriptor> orderByComparator);

	/**
	 * Returns the custom elements portlet descriptors before and after the current custom elements portlet descriptor in the ordered set where uuid = &#63;.
	 *
	 * @param customElementsPortletDescriptorId the primary key of the current custom elements portlet descriptor
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a custom elements portlet descriptor with the primary key could not be found
	 */
	public CustomElementsPortletDescriptor[] findByUuid_PrevAndNext(
			long customElementsPortletDescriptorId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator
				<CustomElementsPortletDescriptor> orderByComparator)
		throws NoSuchCustomElementsPortletDescriptorException;

	/**
	 * Removes all the custom elements portlet descriptors where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of custom elements portlet descriptors where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching custom elements portlet descriptors
	 */
	public int countByUuid(String uuid);

	/**
	 * Returns all the custom elements portlet descriptors where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching custom elements portlet descriptors
	 */
	public java.util.List<CustomElementsPortletDescriptor> findByUuid_C(
		String uuid, long companyId);

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
	public java.util.List<CustomElementsPortletDescriptor> findByUuid_C(
		String uuid, long companyId, int start, int end);

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
	public java.util.List<CustomElementsPortletDescriptor> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CustomElementsPortletDescriptor> orderByComparator);

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
	public java.util.List<CustomElementsPortletDescriptor> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CustomElementsPortletDescriptor> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first custom elements portlet descriptor in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a matching custom elements portlet descriptor could not be found
	 */
	public CustomElementsPortletDescriptor findByUuid_C_First(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CustomElementsPortletDescriptor> orderByComparator)
		throws NoSuchCustomElementsPortletDescriptorException;

	/**
	 * Returns the first custom elements portlet descriptor in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching custom elements portlet descriptor, or <code>null</code> if a matching custom elements portlet descriptor could not be found
	 */
	public CustomElementsPortletDescriptor fetchByUuid_C_First(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CustomElementsPortletDescriptor> orderByComparator);

	/**
	 * Returns the last custom elements portlet descriptor in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a matching custom elements portlet descriptor could not be found
	 */
	public CustomElementsPortletDescriptor findByUuid_C_Last(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CustomElementsPortletDescriptor> orderByComparator)
		throws NoSuchCustomElementsPortletDescriptorException;

	/**
	 * Returns the last custom elements portlet descriptor in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching custom elements portlet descriptor, or <code>null</code> if a matching custom elements portlet descriptor could not be found
	 */
	public CustomElementsPortletDescriptor fetchByUuid_C_Last(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CustomElementsPortletDescriptor> orderByComparator);

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
	public CustomElementsPortletDescriptor[] findByUuid_C_PrevAndNext(
			long customElementsPortletDescriptorId, String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CustomElementsPortletDescriptor> orderByComparator)
		throws NoSuchCustomElementsPortletDescriptorException;

	/**
	 * Removes all the custom elements portlet descriptors where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public void removeByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of custom elements portlet descriptors where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching custom elements portlet descriptors
	 */
	public int countByUuid_C(String uuid, long companyId);

	/**
	 * Caches the custom elements portlet descriptor in the entity cache if it is enabled.
	 *
	 * @param customElementsPortletDescriptor the custom elements portlet descriptor
	 */
	public void cacheResult(
		CustomElementsPortletDescriptor customElementsPortletDescriptor);

	/**
	 * Caches the custom elements portlet descriptors in the entity cache if it is enabled.
	 *
	 * @param customElementsPortletDescriptors the custom elements portlet descriptors
	 */
	public void cacheResult(
		java.util.List<CustomElementsPortletDescriptor>
			customElementsPortletDescriptors);

	/**
	 * Creates a new custom elements portlet descriptor with the primary key. Does not add the custom elements portlet descriptor to the database.
	 *
	 * @param customElementsPortletDescriptorId the primary key for the new custom elements portlet descriptor
	 * @return the new custom elements portlet descriptor
	 */
	public CustomElementsPortletDescriptor create(
		long customElementsPortletDescriptorId);

	/**
	 * Removes the custom elements portlet descriptor with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param customElementsPortletDescriptorId the primary key of the custom elements portlet descriptor
	 * @return the custom elements portlet descriptor that was removed
	 * @throws NoSuchCustomElementsPortletDescriptorException if a custom elements portlet descriptor with the primary key could not be found
	 */
	public CustomElementsPortletDescriptor remove(
			long customElementsPortletDescriptorId)
		throws NoSuchCustomElementsPortletDescriptorException;

	public CustomElementsPortletDescriptor updateImpl(
		CustomElementsPortletDescriptor customElementsPortletDescriptor);

	/**
	 * Returns the custom elements portlet descriptor with the primary key or throws a <code>NoSuchCustomElementsPortletDescriptorException</code> if it could not be found.
	 *
	 * @param customElementsPortletDescriptorId the primary key of the custom elements portlet descriptor
	 * @return the custom elements portlet descriptor
	 * @throws NoSuchCustomElementsPortletDescriptorException if a custom elements portlet descriptor with the primary key could not be found
	 */
	public CustomElementsPortletDescriptor findByPrimaryKey(
			long customElementsPortletDescriptorId)
		throws NoSuchCustomElementsPortletDescriptorException;

	/**
	 * Returns the custom elements portlet descriptor with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param customElementsPortletDescriptorId the primary key of the custom elements portlet descriptor
	 * @return the custom elements portlet descriptor, or <code>null</code> if a custom elements portlet descriptor with the primary key could not be found
	 */
	public CustomElementsPortletDescriptor fetchByPrimaryKey(
		long customElementsPortletDescriptorId);

	/**
	 * Returns all the custom elements portlet descriptors.
	 *
	 * @return the custom elements portlet descriptors
	 */
	public java.util.List<CustomElementsPortletDescriptor> findAll();

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
	public java.util.List<CustomElementsPortletDescriptor> findAll(
		int start, int end);

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
	public java.util.List<CustomElementsPortletDescriptor> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CustomElementsPortletDescriptor> orderByComparator);

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
	public java.util.List<CustomElementsPortletDescriptor> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CustomElementsPortletDescriptor> orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the custom elements portlet descriptors from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of custom elements portlet descriptors.
	 *
	 * @return the number of custom elements portlet descriptors
	 */
	public int countAll();

}