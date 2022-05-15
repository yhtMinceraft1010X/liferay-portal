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

package com.liferay.object.service.persistence.impl;

import com.liferay.object.exception.NoSuchObjectFieldException;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectFieldTable;
import com.liferay.object.model.impl.ObjectFieldImpl;
import com.liferay.object.model.impl.ObjectFieldModelImpl;
import com.liferay.object.service.persistence.ObjectFieldPersistence;
import com.liferay.object.service.persistence.ObjectFieldUtil;
import com.liferay.object.service.persistence.impl.constants.ObjectPersistenceConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUID;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the object field service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Marco Leo
 * @generated
 */
@Component(service = {ObjectFieldPersistence.class, BasePersistence.class})
public class ObjectFieldPersistenceImpl
	extends BasePersistenceImpl<ObjectField> implements ObjectFieldPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>ObjectFieldUtil</code> to access the object field persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		ObjectFieldImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByUuid;
	private FinderPath _finderPathWithoutPaginationFindByUuid;
	private FinderPath _finderPathCountByUuid;

	/**
	 * Returns all the object fields where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching object fields
	 */
	@Override
	public List<ObjectField> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the object fields where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @return the range of matching object fields
	 */
	@Override
	public List<ObjectField> findByUuid(String uuid, int start, int end) {
		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the object fields where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching object fields
	 */
	@Override
	public List<ObjectField> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<ObjectField> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the object fields where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching object fields
	 */
	@Override
	public List<ObjectField> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<ObjectField> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid;
				finderArgs = new Object[] {uuid};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid;
			finderArgs = new Object[] {uuid, start, end, orderByComparator};
		}

		List<ObjectField> list = null;

		if (useFinderCache) {
			list = (List<ObjectField>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (ObjectField objectField : list) {
					if (!uuid.equals(objectField.getUuid())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_OBJECTFIELD_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ObjectFieldModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				list = (List<ObjectField>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first object field in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object field
	 * @throws NoSuchObjectFieldException if a matching object field could not be found
	 */
	@Override
	public ObjectField findByUuid_First(
			String uuid, OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		ObjectField objectField = fetchByUuid_First(uuid, orderByComparator);

		if (objectField != null) {
			return objectField;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchObjectFieldException(sb.toString());
	}

	/**
	 * Returns the first object field in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public ObjectField fetchByUuid_First(
		String uuid, OrderByComparator<ObjectField> orderByComparator) {

		List<ObjectField> list = findByUuid(uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last object field in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object field
	 * @throws NoSuchObjectFieldException if a matching object field could not be found
	 */
	@Override
	public ObjectField findByUuid_Last(
			String uuid, OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		ObjectField objectField = fetchByUuid_Last(uuid, orderByComparator);

		if (objectField != null) {
			return objectField;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchObjectFieldException(sb.toString());
	}

	/**
	 * Returns the last object field in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public ObjectField fetchByUuid_Last(
		String uuid, OrderByComparator<ObjectField> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<ObjectField> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the object fields before and after the current object field in the ordered set where uuid = &#63;.
	 *
	 * @param objectFieldId the primary key of the current object field
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next object field
	 * @throws NoSuchObjectFieldException if a object field with the primary key could not be found
	 */
	@Override
	public ObjectField[] findByUuid_PrevAndNext(
			long objectFieldId, String uuid,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		uuid = Objects.toString(uuid, "");

		ObjectField objectField = findByPrimaryKey(objectFieldId);

		Session session = null;

		try {
			session = openSession();

			ObjectField[] array = new ObjectFieldImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, objectField, uuid, orderByComparator, true);

			array[1] = objectField;

			array[2] = getByUuid_PrevAndNext(
				session, objectField, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected ObjectField getByUuid_PrevAndNext(
		Session session, ObjectField objectField, String uuid,
		OrderByComparator<ObjectField> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_OBJECTFIELD_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(ObjectFieldModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(objectField)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ObjectField> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the object fields where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (ObjectField objectField :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(objectField);
		}
	}

	/**
	 * Returns the number of object fields where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching object fields
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_OBJECTFIELD_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_UUID_2 =
		"objectField.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(objectField.uuid IS NULL OR objectField.uuid = '')";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the object fields where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching object fields
	 */
	@Override
	public List<ObjectField> findByUuid_C(String uuid, long companyId) {
		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the object fields where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @return the range of matching object fields
	 */
	@Override
	public List<ObjectField> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the object fields where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching object fields
	 */
	@Override
	public List<ObjectField> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<ObjectField> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the object fields where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching object fields
	 */
	@Override
	public List<ObjectField> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<ObjectField> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid_C;
				finderArgs = new Object[] {uuid, companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid_C;
			finderArgs = new Object[] {
				uuid, companyId, start, end, orderByComparator
			};
		}

		List<ObjectField> list = null;

		if (useFinderCache) {
			list = (List<ObjectField>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (ObjectField objectField : list) {
					if (!uuid.equals(objectField.getUuid()) ||
						(companyId != objectField.getCompanyId())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_OBJECTFIELD_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ObjectFieldModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				list = (List<ObjectField>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first object field in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object field
	 * @throws NoSuchObjectFieldException if a matching object field could not be found
	 */
	@Override
	public ObjectField findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		ObjectField objectField = fetchByUuid_C_First(
			uuid, companyId, orderByComparator);

		if (objectField != null) {
			return objectField;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchObjectFieldException(sb.toString());
	}

	/**
	 * Returns the first object field in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public ObjectField fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<ObjectField> orderByComparator) {

		List<ObjectField> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last object field in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object field
	 * @throws NoSuchObjectFieldException if a matching object field could not be found
	 */
	@Override
	public ObjectField findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		ObjectField objectField = fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);

		if (objectField != null) {
			return objectField;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchObjectFieldException(sb.toString());
	}

	/**
	 * Returns the last object field in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public ObjectField fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<ObjectField> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<ObjectField> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the object fields before and after the current object field in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param objectFieldId the primary key of the current object field
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next object field
	 * @throws NoSuchObjectFieldException if a object field with the primary key could not be found
	 */
	@Override
	public ObjectField[] findByUuid_C_PrevAndNext(
			long objectFieldId, String uuid, long companyId,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		uuid = Objects.toString(uuid, "");

		ObjectField objectField = findByPrimaryKey(objectFieldId);

		Session session = null;

		try {
			session = openSession();

			ObjectField[] array = new ObjectFieldImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, objectField, uuid, companyId, orderByComparator, true);

			array[1] = objectField;

			array[2] = getByUuid_C_PrevAndNext(
				session, objectField, uuid, companyId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected ObjectField getByUuid_C_PrevAndNext(
		Session session, ObjectField objectField, String uuid, long companyId,
		OrderByComparator<ObjectField> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_OBJECTFIELD_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(ObjectFieldModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(objectField)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ObjectField> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the object fields where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (ObjectField objectField :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(objectField);
		}
	}

	/**
	 * Returns the number of object fields where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching object fields
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid_C;

		Object[] finderArgs = new Object[] {uuid, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_OBJECTFIELD_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_C_UUID_2 =
		"objectField.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(objectField.uuid IS NULL OR objectField.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"objectField.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByListTypeDefinitionId;
	private FinderPath _finderPathWithoutPaginationFindByListTypeDefinitionId;
	private FinderPath _finderPathCountByListTypeDefinitionId;

	/**
	 * Returns all the object fields where listTypeDefinitionId = &#63;.
	 *
	 * @param listTypeDefinitionId the list type definition ID
	 * @return the matching object fields
	 */
	@Override
	public List<ObjectField> findByListTypeDefinitionId(
		long listTypeDefinitionId) {

		return findByListTypeDefinitionId(
			listTypeDefinitionId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the object fields where listTypeDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param listTypeDefinitionId the list type definition ID
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @return the range of matching object fields
	 */
	@Override
	public List<ObjectField> findByListTypeDefinitionId(
		long listTypeDefinitionId, int start, int end) {

		return findByListTypeDefinitionId(
			listTypeDefinitionId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the object fields where listTypeDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param listTypeDefinitionId the list type definition ID
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching object fields
	 */
	@Override
	public List<ObjectField> findByListTypeDefinitionId(
		long listTypeDefinitionId, int start, int end,
		OrderByComparator<ObjectField> orderByComparator) {

		return findByListTypeDefinitionId(
			listTypeDefinitionId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the object fields where listTypeDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param listTypeDefinitionId the list type definition ID
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching object fields
	 */
	@Override
	public List<ObjectField> findByListTypeDefinitionId(
		long listTypeDefinitionId, int start, int end,
		OrderByComparator<ObjectField> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByListTypeDefinitionId;
				finderArgs = new Object[] {listTypeDefinitionId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByListTypeDefinitionId;
			finderArgs = new Object[] {
				listTypeDefinitionId, start, end, orderByComparator
			};
		}

		List<ObjectField> list = null;

		if (useFinderCache) {
			list = (List<ObjectField>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (ObjectField objectField : list) {
					if (listTypeDefinitionId !=
							objectField.getListTypeDefinitionId()) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_OBJECTFIELD_WHERE);

			sb.append(
				_FINDER_COLUMN_LISTTYPEDEFINITIONID_LISTTYPEDEFINITIONID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ObjectFieldModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(listTypeDefinitionId);

				list = (List<ObjectField>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first object field in the ordered set where listTypeDefinitionId = &#63;.
	 *
	 * @param listTypeDefinitionId the list type definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object field
	 * @throws NoSuchObjectFieldException if a matching object field could not be found
	 */
	@Override
	public ObjectField findByListTypeDefinitionId_First(
			long listTypeDefinitionId,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		ObjectField objectField = fetchByListTypeDefinitionId_First(
			listTypeDefinitionId, orderByComparator);

		if (objectField != null) {
			return objectField;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("listTypeDefinitionId=");
		sb.append(listTypeDefinitionId);

		sb.append("}");

		throw new NoSuchObjectFieldException(sb.toString());
	}

	/**
	 * Returns the first object field in the ordered set where listTypeDefinitionId = &#63;.
	 *
	 * @param listTypeDefinitionId the list type definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public ObjectField fetchByListTypeDefinitionId_First(
		long listTypeDefinitionId,
		OrderByComparator<ObjectField> orderByComparator) {

		List<ObjectField> list = findByListTypeDefinitionId(
			listTypeDefinitionId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last object field in the ordered set where listTypeDefinitionId = &#63;.
	 *
	 * @param listTypeDefinitionId the list type definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object field
	 * @throws NoSuchObjectFieldException if a matching object field could not be found
	 */
	@Override
	public ObjectField findByListTypeDefinitionId_Last(
			long listTypeDefinitionId,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		ObjectField objectField = fetchByListTypeDefinitionId_Last(
			listTypeDefinitionId, orderByComparator);

		if (objectField != null) {
			return objectField;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("listTypeDefinitionId=");
		sb.append(listTypeDefinitionId);

		sb.append("}");

		throw new NoSuchObjectFieldException(sb.toString());
	}

	/**
	 * Returns the last object field in the ordered set where listTypeDefinitionId = &#63;.
	 *
	 * @param listTypeDefinitionId the list type definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public ObjectField fetchByListTypeDefinitionId_Last(
		long listTypeDefinitionId,
		OrderByComparator<ObjectField> orderByComparator) {

		int count = countByListTypeDefinitionId(listTypeDefinitionId);

		if (count == 0) {
			return null;
		}

		List<ObjectField> list = findByListTypeDefinitionId(
			listTypeDefinitionId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the object fields before and after the current object field in the ordered set where listTypeDefinitionId = &#63;.
	 *
	 * @param objectFieldId the primary key of the current object field
	 * @param listTypeDefinitionId the list type definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next object field
	 * @throws NoSuchObjectFieldException if a object field with the primary key could not be found
	 */
	@Override
	public ObjectField[] findByListTypeDefinitionId_PrevAndNext(
			long objectFieldId, long listTypeDefinitionId,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		ObjectField objectField = findByPrimaryKey(objectFieldId);

		Session session = null;

		try {
			session = openSession();

			ObjectField[] array = new ObjectFieldImpl[3];

			array[0] = getByListTypeDefinitionId_PrevAndNext(
				session, objectField, listTypeDefinitionId, orderByComparator,
				true);

			array[1] = objectField;

			array[2] = getByListTypeDefinitionId_PrevAndNext(
				session, objectField, listTypeDefinitionId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected ObjectField getByListTypeDefinitionId_PrevAndNext(
		Session session, ObjectField objectField, long listTypeDefinitionId,
		OrderByComparator<ObjectField> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_OBJECTFIELD_WHERE);

		sb.append(_FINDER_COLUMN_LISTTYPEDEFINITIONID_LISTTYPEDEFINITIONID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(ObjectFieldModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(listTypeDefinitionId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(objectField)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ObjectField> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the object fields where listTypeDefinitionId = &#63; from the database.
	 *
	 * @param listTypeDefinitionId the list type definition ID
	 */
	@Override
	public void removeByListTypeDefinitionId(long listTypeDefinitionId) {
		for (ObjectField objectField :
				findByListTypeDefinitionId(
					listTypeDefinitionId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(objectField);
		}
	}

	/**
	 * Returns the number of object fields where listTypeDefinitionId = &#63;.
	 *
	 * @param listTypeDefinitionId the list type definition ID
	 * @return the number of matching object fields
	 */
	@Override
	public int countByListTypeDefinitionId(long listTypeDefinitionId) {
		FinderPath finderPath = _finderPathCountByListTypeDefinitionId;

		Object[] finderArgs = new Object[] {listTypeDefinitionId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_OBJECTFIELD_WHERE);

			sb.append(
				_FINDER_COLUMN_LISTTYPEDEFINITIONID_LISTTYPEDEFINITIONID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(listTypeDefinitionId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_LISTTYPEDEFINITIONID_LISTTYPEDEFINITIONID_2 =
			"objectField.listTypeDefinitionId = ?";

	private FinderPath _finderPathWithPaginationFindByObjectDefinitionId;
	private FinderPath _finderPathWithoutPaginationFindByObjectDefinitionId;
	private FinderPath _finderPathCountByObjectDefinitionId;

	/**
	 * Returns all the object fields where objectDefinitionId = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @return the matching object fields
	 */
	@Override
	public List<ObjectField> findByObjectDefinitionId(long objectDefinitionId) {
		return findByObjectDefinitionId(
			objectDefinitionId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the object fields where objectDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @return the range of matching object fields
	 */
	@Override
	public List<ObjectField> findByObjectDefinitionId(
		long objectDefinitionId, int start, int end) {

		return findByObjectDefinitionId(objectDefinitionId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the object fields where objectDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching object fields
	 */
	@Override
	public List<ObjectField> findByObjectDefinitionId(
		long objectDefinitionId, int start, int end,
		OrderByComparator<ObjectField> orderByComparator) {

		return findByObjectDefinitionId(
			objectDefinitionId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the object fields where objectDefinitionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching object fields
	 */
	@Override
	public List<ObjectField> findByObjectDefinitionId(
		long objectDefinitionId, int start, int end,
		OrderByComparator<ObjectField> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByObjectDefinitionId;
				finderArgs = new Object[] {objectDefinitionId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByObjectDefinitionId;
			finderArgs = new Object[] {
				objectDefinitionId, start, end, orderByComparator
			};
		}

		List<ObjectField> list = null;

		if (useFinderCache) {
			list = (List<ObjectField>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (ObjectField objectField : list) {
					if (objectDefinitionId !=
							objectField.getObjectDefinitionId()) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_OBJECTFIELD_WHERE);

			sb.append(_FINDER_COLUMN_OBJECTDEFINITIONID_OBJECTDEFINITIONID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ObjectFieldModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(objectDefinitionId);

				list = (List<ObjectField>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first object field in the ordered set where objectDefinitionId = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object field
	 * @throws NoSuchObjectFieldException if a matching object field could not be found
	 */
	@Override
	public ObjectField findByObjectDefinitionId_First(
			long objectDefinitionId,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		ObjectField objectField = fetchByObjectDefinitionId_First(
			objectDefinitionId, orderByComparator);

		if (objectField != null) {
			return objectField;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("objectDefinitionId=");
		sb.append(objectDefinitionId);

		sb.append("}");

		throw new NoSuchObjectFieldException(sb.toString());
	}

	/**
	 * Returns the first object field in the ordered set where objectDefinitionId = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public ObjectField fetchByObjectDefinitionId_First(
		long objectDefinitionId,
		OrderByComparator<ObjectField> orderByComparator) {

		List<ObjectField> list = findByObjectDefinitionId(
			objectDefinitionId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last object field in the ordered set where objectDefinitionId = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object field
	 * @throws NoSuchObjectFieldException if a matching object field could not be found
	 */
	@Override
	public ObjectField findByObjectDefinitionId_Last(
			long objectDefinitionId,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		ObjectField objectField = fetchByObjectDefinitionId_Last(
			objectDefinitionId, orderByComparator);

		if (objectField != null) {
			return objectField;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("objectDefinitionId=");
		sb.append(objectDefinitionId);

		sb.append("}");

		throw new NoSuchObjectFieldException(sb.toString());
	}

	/**
	 * Returns the last object field in the ordered set where objectDefinitionId = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public ObjectField fetchByObjectDefinitionId_Last(
		long objectDefinitionId,
		OrderByComparator<ObjectField> orderByComparator) {

		int count = countByObjectDefinitionId(objectDefinitionId);

		if (count == 0) {
			return null;
		}

		List<ObjectField> list = findByObjectDefinitionId(
			objectDefinitionId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the object fields before and after the current object field in the ordered set where objectDefinitionId = &#63;.
	 *
	 * @param objectFieldId the primary key of the current object field
	 * @param objectDefinitionId the object definition ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next object field
	 * @throws NoSuchObjectFieldException if a object field with the primary key could not be found
	 */
	@Override
	public ObjectField[] findByObjectDefinitionId_PrevAndNext(
			long objectFieldId, long objectDefinitionId,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		ObjectField objectField = findByPrimaryKey(objectFieldId);

		Session session = null;

		try {
			session = openSession();

			ObjectField[] array = new ObjectFieldImpl[3];

			array[0] = getByObjectDefinitionId_PrevAndNext(
				session, objectField, objectDefinitionId, orderByComparator,
				true);

			array[1] = objectField;

			array[2] = getByObjectDefinitionId_PrevAndNext(
				session, objectField, objectDefinitionId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected ObjectField getByObjectDefinitionId_PrevAndNext(
		Session session, ObjectField objectField, long objectDefinitionId,
		OrderByComparator<ObjectField> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_OBJECTFIELD_WHERE);

		sb.append(_FINDER_COLUMN_OBJECTDEFINITIONID_OBJECTDEFINITIONID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(ObjectFieldModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(objectDefinitionId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(objectField)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ObjectField> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the object fields where objectDefinitionId = &#63; from the database.
	 *
	 * @param objectDefinitionId the object definition ID
	 */
	@Override
	public void removeByObjectDefinitionId(long objectDefinitionId) {
		for (ObjectField objectField :
				findByObjectDefinitionId(
					objectDefinitionId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(objectField);
		}
	}

	/**
	 * Returns the number of object fields where objectDefinitionId = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @return the number of matching object fields
	 */
	@Override
	public int countByObjectDefinitionId(long objectDefinitionId) {
		FinderPath finderPath = _finderPathCountByObjectDefinitionId;

		Object[] finderArgs = new Object[] {objectDefinitionId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_OBJECTFIELD_WHERE);

			sb.append(_FINDER_COLUMN_OBJECTDEFINITIONID_OBJECTDEFINITIONID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(objectDefinitionId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_OBJECTDEFINITIONID_OBJECTDEFINITIONID_2 =
			"objectField.objectDefinitionId = ?";

	private FinderPath _finderPathWithPaginationFindByODI_DTN;
	private FinderPath _finderPathWithoutPaginationFindByODI_DTN;
	private FinderPath _finderPathCountByODI_DTN;

	/**
	 * Returns all the object fields where objectDefinitionId = &#63; and dbTableName = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param dbTableName the db table name
	 * @return the matching object fields
	 */
	@Override
	public List<ObjectField> findByODI_DTN(
		long objectDefinitionId, String dbTableName) {

		return findByODI_DTN(
			objectDefinitionId, dbTableName, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the object fields where objectDefinitionId = &#63; and dbTableName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param dbTableName the db table name
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @return the range of matching object fields
	 */
	@Override
	public List<ObjectField> findByODI_DTN(
		long objectDefinitionId, String dbTableName, int start, int end) {

		return findByODI_DTN(objectDefinitionId, dbTableName, start, end, null);
	}

	/**
	 * Returns an ordered range of all the object fields where objectDefinitionId = &#63; and dbTableName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param dbTableName the db table name
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching object fields
	 */
	@Override
	public List<ObjectField> findByODI_DTN(
		long objectDefinitionId, String dbTableName, int start, int end,
		OrderByComparator<ObjectField> orderByComparator) {

		return findByODI_DTN(
			objectDefinitionId, dbTableName, start, end, orderByComparator,
			true);
	}

	/**
	 * Returns an ordered range of all the object fields where objectDefinitionId = &#63; and dbTableName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param dbTableName the db table name
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching object fields
	 */
	@Override
	public List<ObjectField> findByODI_DTN(
		long objectDefinitionId, String dbTableName, int start, int end,
		OrderByComparator<ObjectField> orderByComparator,
		boolean useFinderCache) {

		dbTableName = Objects.toString(dbTableName, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByODI_DTN;
				finderArgs = new Object[] {objectDefinitionId, dbTableName};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByODI_DTN;
			finderArgs = new Object[] {
				objectDefinitionId, dbTableName, start, end, orderByComparator
			};
		}

		List<ObjectField> list = null;

		if (useFinderCache) {
			list = (List<ObjectField>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (ObjectField objectField : list) {
					if ((objectDefinitionId !=
							objectField.getObjectDefinitionId()) ||
						!dbTableName.equals(objectField.getDBTableName())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_OBJECTFIELD_WHERE);

			sb.append(_FINDER_COLUMN_ODI_DTN_OBJECTDEFINITIONID_2);

			boolean bindDBTableName = false;

			if (dbTableName.isEmpty()) {
				sb.append(_FINDER_COLUMN_ODI_DTN_DBTABLENAME_3);
			}
			else {
				bindDBTableName = true;

				sb.append(_FINDER_COLUMN_ODI_DTN_DBTABLENAME_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(ObjectFieldModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(objectDefinitionId);

				if (bindDBTableName) {
					queryPos.add(dbTableName);
				}

				list = (List<ObjectField>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first object field in the ordered set where objectDefinitionId = &#63; and dbTableName = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param dbTableName the db table name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object field
	 * @throws NoSuchObjectFieldException if a matching object field could not be found
	 */
	@Override
	public ObjectField findByODI_DTN_First(
			long objectDefinitionId, String dbTableName,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		ObjectField objectField = fetchByODI_DTN_First(
			objectDefinitionId, dbTableName, orderByComparator);

		if (objectField != null) {
			return objectField;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("objectDefinitionId=");
		sb.append(objectDefinitionId);

		sb.append(", dbTableName=");
		sb.append(dbTableName);

		sb.append("}");

		throw new NoSuchObjectFieldException(sb.toString());
	}

	/**
	 * Returns the first object field in the ordered set where objectDefinitionId = &#63; and dbTableName = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param dbTableName the db table name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public ObjectField fetchByODI_DTN_First(
		long objectDefinitionId, String dbTableName,
		OrderByComparator<ObjectField> orderByComparator) {

		List<ObjectField> list = findByODI_DTN(
			objectDefinitionId, dbTableName, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last object field in the ordered set where objectDefinitionId = &#63; and dbTableName = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param dbTableName the db table name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object field
	 * @throws NoSuchObjectFieldException if a matching object field could not be found
	 */
	@Override
	public ObjectField findByODI_DTN_Last(
			long objectDefinitionId, String dbTableName,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		ObjectField objectField = fetchByODI_DTN_Last(
			objectDefinitionId, dbTableName, orderByComparator);

		if (objectField != null) {
			return objectField;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("objectDefinitionId=");
		sb.append(objectDefinitionId);

		sb.append(", dbTableName=");
		sb.append(dbTableName);

		sb.append("}");

		throw new NoSuchObjectFieldException(sb.toString());
	}

	/**
	 * Returns the last object field in the ordered set where objectDefinitionId = &#63; and dbTableName = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param dbTableName the db table name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public ObjectField fetchByODI_DTN_Last(
		long objectDefinitionId, String dbTableName,
		OrderByComparator<ObjectField> orderByComparator) {

		int count = countByODI_DTN(objectDefinitionId, dbTableName);

		if (count == 0) {
			return null;
		}

		List<ObjectField> list = findByODI_DTN(
			objectDefinitionId, dbTableName, count - 1, count,
			orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the object fields before and after the current object field in the ordered set where objectDefinitionId = &#63; and dbTableName = &#63;.
	 *
	 * @param objectFieldId the primary key of the current object field
	 * @param objectDefinitionId the object definition ID
	 * @param dbTableName the db table name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next object field
	 * @throws NoSuchObjectFieldException if a object field with the primary key could not be found
	 */
	@Override
	public ObjectField[] findByODI_DTN_PrevAndNext(
			long objectFieldId, long objectDefinitionId, String dbTableName,
			OrderByComparator<ObjectField> orderByComparator)
		throws NoSuchObjectFieldException {

		dbTableName = Objects.toString(dbTableName, "");

		ObjectField objectField = findByPrimaryKey(objectFieldId);

		Session session = null;

		try {
			session = openSession();

			ObjectField[] array = new ObjectFieldImpl[3];

			array[0] = getByODI_DTN_PrevAndNext(
				session, objectField, objectDefinitionId, dbTableName,
				orderByComparator, true);

			array[1] = objectField;

			array[2] = getByODI_DTN_PrevAndNext(
				session, objectField, objectDefinitionId, dbTableName,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected ObjectField getByODI_DTN_PrevAndNext(
		Session session, ObjectField objectField, long objectDefinitionId,
		String dbTableName, OrderByComparator<ObjectField> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_OBJECTFIELD_WHERE);

		sb.append(_FINDER_COLUMN_ODI_DTN_OBJECTDEFINITIONID_2);

		boolean bindDBTableName = false;

		if (dbTableName.isEmpty()) {
			sb.append(_FINDER_COLUMN_ODI_DTN_DBTABLENAME_3);
		}
		else {
			bindDBTableName = true;

			sb.append(_FINDER_COLUMN_ODI_DTN_DBTABLENAME_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(ObjectFieldModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(objectDefinitionId);

		if (bindDBTableName) {
			queryPos.add(dbTableName);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(objectField)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ObjectField> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the object fields where objectDefinitionId = &#63; and dbTableName = &#63; from the database.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param dbTableName the db table name
	 */
	@Override
	public void removeByODI_DTN(long objectDefinitionId, String dbTableName) {
		for (ObjectField objectField :
				findByODI_DTN(
					objectDefinitionId, dbTableName, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(objectField);
		}
	}

	/**
	 * Returns the number of object fields where objectDefinitionId = &#63; and dbTableName = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param dbTableName the db table name
	 * @return the number of matching object fields
	 */
	@Override
	public int countByODI_DTN(long objectDefinitionId, String dbTableName) {
		dbTableName = Objects.toString(dbTableName, "");

		FinderPath finderPath = _finderPathCountByODI_DTN;

		Object[] finderArgs = new Object[] {objectDefinitionId, dbTableName};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_OBJECTFIELD_WHERE);

			sb.append(_FINDER_COLUMN_ODI_DTN_OBJECTDEFINITIONID_2);

			boolean bindDBTableName = false;

			if (dbTableName.isEmpty()) {
				sb.append(_FINDER_COLUMN_ODI_DTN_DBTABLENAME_3);
			}
			else {
				bindDBTableName = true;

				sb.append(_FINDER_COLUMN_ODI_DTN_DBTABLENAME_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(objectDefinitionId);

				if (bindDBTableName) {
					queryPos.add(dbTableName);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_ODI_DTN_OBJECTDEFINITIONID_2 =
		"objectField.objectDefinitionId = ? AND ";

	private static final String _FINDER_COLUMN_ODI_DTN_DBTABLENAME_2 =
		"objectField.dbTableName = ?";

	private static final String _FINDER_COLUMN_ODI_DTN_DBTABLENAME_3 =
		"(objectField.dbTableName IS NULL OR objectField.dbTableName = '')";

	private FinderPath _finderPathFetchByODI_N;
	private FinderPath _finderPathCountByODI_N;

	/**
	 * Returns the object field where objectDefinitionId = &#63; and name = &#63; or throws a <code>NoSuchObjectFieldException</code> if it could not be found.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param name the name
	 * @return the matching object field
	 * @throws NoSuchObjectFieldException if a matching object field could not be found
	 */
	@Override
	public ObjectField findByODI_N(long objectDefinitionId, String name)
		throws NoSuchObjectFieldException {

		ObjectField objectField = fetchByODI_N(objectDefinitionId, name);

		if (objectField == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("objectDefinitionId=");
			sb.append(objectDefinitionId);

			sb.append(", name=");
			sb.append(name);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchObjectFieldException(sb.toString());
		}

		return objectField;
	}

	/**
	 * Returns the object field where objectDefinitionId = &#63; and name = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param name the name
	 * @return the matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public ObjectField fetchByODI_N(long objectDefinitionId, String name) {
		return fetchByODI_N(objectDefinitionId, name, true);
	}

	/**
	 * Returns the object field where objectDefinitionId = &#63; and name = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param name the name
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public ObjectField fetchByODI_N(
		long objectDefinitionId, String name, boolean useFinderCache) {

		name = Objects.toString(name, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {objectDefinitionId, name};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(_finderPathFetchByODI_N, finderArgs);
		}

		if (result instanceof ObjectField) {
			ObjectField objectField = (ObjectField)result;

			if ((objectDefinitionId != objectField.getObjectDefinitionId()) ||
				!Objects.equals(name, objectField.getName())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_OBJECTFIELD_WHERE);

			sb.append(_FINDER_COLUMN_ODI_N_OBJECTDEFINITIONID_2);

			boolean bindName = false;

			if (name.isEmpty()) {
				sb.append(_FINDER_COLUMN_ODI_N_NAME_3);
			}
			else {
				bindName = true;

				sb.append(_FINDER_COLUMN_ODI_N_NAME_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(objectDefinitionId);

				if (bindName) {
					queryPos.add(name);
				}

				List<ObjectField> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByODI_N, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {
									objectDefinitionId, name
								};
							}

							_log.warn(
								"ObjectFieldPersistenceImpl.fetchByODI_N(long, String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					ObjectField objectField = list.get(0);

					result = objectField;

					cacheResult(objectField);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (ObjectField)result;
		}
	}

	/**
	 * Removes the object field where objectDefinitionId = &#63; and name = &#63; from the database.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param name the name
	 * @return the object field that was removed
	 */
	@Override
	public ObjectField removeByODI_N(long objectDefinitionId, String name)
		throws NoSuchObjectFieldException {

		ObjectField objectField = findByODI_N(objectDefinitionId, name);

		return remove(objectField);
	}

	/**
	 * Returns the number of object fields where objectDefinitionId = &#63; and name = &#63;.
	 *
	 * @param objectDefinitionId the object definition ID
	 * @param name the name
	 * @return the number of matching object fields
	 */
	@Override
	public int countByODI_N(long objectDefinitionId, String name) {
		name = Objects.toString(name, "");

		FinderPath finderPath = _finderPathCountByODI_N;

		Object[] finderArgs = new Object[] {objectDefinitionId, name};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_OBJECTFIELD_WHERE);

			sb.append(_FINDER_COLUMN_ODI_N_OBJECTDEFINITIONID_2);

			boolean bindName = false;

			if (name.isEmpty()) {
				sb.append(_FINDER_COLUMN_ODI_N_NAME_3);
			}
			else {
				bindName = true;

				sb.append(_FINDER_COLUMN_ODI_N_NAME_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(objectDefinitionId);

				if (bindName) {
					queryPos.add(name);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_ODI_N_OBJECTDEFINITIONID_2 =
		"objectField.objectDefinitionId = ? AND ";

	private static final String _FINDER_COLUMN_ODI_N_NAME_2 =
		"objectField.name = ?";

	private static final String _FINDER_COLUMN_ODI_N_NAME_3 =
		"(objectField.name IS NULL OR objectField.name = '')";

	public ObjectFieldPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

		setDBColumnNames(dbColumnNames);

		setModelClass(ObjectField.class);

		setModelImplClass(ObjectFieldImpl.class);
		setModelPKClass(long.class);

		setTable(ObjectFieldTable.INSTANCE);
	}

	/**
	 * Caches the object field in the entity cache if it is enabled.
	 *
	 * @param objectField the object field
	 */
	@Override
	public void cacheResult(ObjectField objectField) {
		entityCache.putResult(
			ObjectFieldImpl.class, objectField.getPrimaryKey(), objectField);

		finderCache.putResult(
			_finderPathFetchByODI_N,
			new Object[] {
				objectField.getObjectDefinitionId(), objectField.getName()
			},
			objectField);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the object fields in the entity cache if it is enabled.
	 *
	 * @param objectFields the object fields
	 */
	@Override
	public void cacheResult(List<ObjectField> objectFields) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (objectFields.size() > _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (ObjectField objectField : objectFields) {
			if (entityCache.getResult(
					ObjectFieldImpl.class, objectField.getPrimaryKey()) ==
						null) {

				cacheResult(objectField);
			}
		}
	}

	/**
	 * Clears the cache for all object fields.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(ObjectFieldImpl.class);

		finderCache.clearCache(ObjectFieldImpl.class);
	}

	/**
	 * Clears the cache for the object field.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(ObjectField objectField) {
		entityCache.removeResult(ObjectFieldImpl.class, objectField);
	}

	@Override
	public void clearCache(List<ObjectField> objectFields) {
		for (ObjectField objectField : objectFields) {
			entityCache.removeResult(ObjectFieldImpl.class, objectField);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(ObjectFieldImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(ObjectFieldImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		ObjectFieldModelImpl objectFieldModelImpl) {

		Object[] args = new Object[] {
			objectFieldModelImpl.getObjectDefinitionId(),
			objectFieldModelImpl.getName()
		};

		finderCache.putResult(_finderPathCountByODI_N, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByODI_N, args, objectFieldModelImpl);
	}

	/**
	 * Creates a new object field with the primary key. Does not add the object field to the database.
	 *
	 * @param objectFieldId the primary key for the new object field
	 * @return the new object field
	 */
	@Override
	public ObjectField create(long objectFieldId) {
		ObjectField objectField = new ObjectFieldImpl();

		objectField.setNew(true);
		objectField.setPrimaryKey(objectFieldId);

		String uuid = _portalUUID.generate();

		objectField.setUuid(uuid);

		objectField.setCompanyId(CompanyThreadLocal.getCompanyId());

		return objectField;
	}

	/**
	 * Removes the object field with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param objectFieldId the primary key of the object field
	 * @return the object field that was removed
	 * @throws NoSuchObjectFieldException if a object field with the primary key could not be found
	 */
	@Override
	public ObjectField remove(long objectFieldId)
		throws NoSuchObjectFieldException {

		return remove((Serializable)objectFieldId);
	}

	/**
	 * Removes the object field with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the object field
	 * @return the object field that was removed
	 * @throws NoSuchObjectFieldException if a object field with the primary key could not be found
	 */
	@Override
	public ObjectField remove(Serializable primaryKey)
		throws NoSuchObjectFieldException {

		Session session = null;

		try {
			session = openSession();

			ObjectField objectField = (ObjectField)session.get(
				ObjectFieldImpl.class, primaryKey);

			if (objectField == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchObjectFieldException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(objectField);
		}
		catch (NoSuchObjectFieldException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected ObjectField removeImpl(ObjectField objectField) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(objectField)) {
				objectField = (ObjectField)session.get(
					ObjectFieldImpl.class, objectField.getPrimaryKeyObj());
			}

			if (objectField != null) {
				session.delete(objectField);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (objectField != null) {
			clearCache(objectField);
		}

		return objectField;
	}

	@Override
	public ObjectField updateImpl(ObjectField objectField) {
		boolean isNew = objectField.isNew();

		if (!(objectField instanceof ObjectFieldModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(objectField.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(objectField);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in objectField proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom ObjectField implementation " +
					objectField.getClass());
		}

		ObjectFieldModelImpl objectFieldModelImpl =
			(ObjectFieldModelImpl)objectField;

		if (Validator.isNull(objectField.getUuid())) {
			String uuid = _portalUUID.generate();

			objectField.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (objectField.getCreateDate() == null)) {
			if (serviceContext == null) {
				objectField.setCreateDate(date);
			}
			else {
				objectField.setCreateDate(serviceContext.getCreateDate(date));
			}
		}

		if (!objectFieldModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				objectField.setModifiedDate(date);
			}
			else {
				objectField.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(objectField);
			}
			else {
				objectField = (ObjectField)session.merge(objectField);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			ObjectFieldImpl.class, objectFieldModelImpl, false, true);

		cacheUniqueFindersCache(objectFieldModelImpl);

		if (isNew) {
			objectField.setNew(false);
		}

		objectField.resetOriginalValues();

		return objectField;
	}

	/**
	 * Returns the object field with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the object field
	 * @return the object field
	 * @throws NoSuchObjectFieldException if a object field with the primary key could not be found
	 */
	@Override
	public ObjectField findByPrimaryKey(Serializable primaryKey)
		throws NoSuchObjectFieldException {

		ObjectField objectField = fetchByPrimaryKey(primaryKey);

		if (objectField == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchObjectFieldException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return objectField;
	}

	/**
	 * Returns the object field with the primary key or throws a <code>NoSuchObjectFieldException</code> if it could not be found.
	 *
	 * @param objectFieldId the primary key of the object field
	 * @return the object field
	 * @throws NoSuchObjectFieldException if a object field with the primary key could not be found
	 */
	@Override
	public ObjectField findByPrimaryKey(long objectFieldId)
		throws NoSuchObjectFieldException {

		return findByPrimaryKey((Serializable)objectFieldId);
	}

	/**
	 * Returns the object field with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param objectFieldId the primary key of the object field
	 * @return the object field, or <code>null</code> if a object field with the primary key could not be found
	 */
	@Override
	public ObjectField fetchByPrimaryKey(long objectFieldId) {
		return fetchByPrimaryKey((Serializable)objectFieldId);
	}

	/**
	 * Returns all the object fields.
	 *
	 * @return the object fields
	 */
	@Override
	public List<ObjectField> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the object fields.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @return the range of object fields
	 */
	@Override
	public List<ObjectField> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the object fields.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of object fields
	 */
	@Override
	public List<ObjectField> findAll(
		int start, int end, OrderByComparator<ObjectField> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the object fields.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of object fields
	 */
	@Override
	public List<ObjectField> findAll(
		int start, int end, OrderByComparator<ObjectField> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<ObjectField> list = null;

		if (useFinderCache) {
			list = (List<ObjectField>)finderCache.getResult(
				finderPath, finderArgs);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_OBJECTFIELD);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_OBJECTFIELD;

				sql = sql.concat(ObjectFieldModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<ObjectField>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the object fields from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (ObjectField objectField : findAll()) {
			remove(objectField);
		}
	}

	/**
	 * Returns the number of object fields.
	 *
	 * @return the number of object fields
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_OBJECTFIELD);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "objectFieldId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_OBJECTFIELD;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return ObjectFieldModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the object field persistence.
	 */
	@Activate
	public void activate() {
		_valueObjectFinderCacheListThreshold = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD));

		_finderPathWithPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"uuid_"}, true);

		_finderPathWithoutPaginationFindByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] {String.class.getName()}, new String[] {"uuid_"},
			true);

		_finderPathCountByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] {String.class.getName()}, new String[] {"uuid_"},
			false);

		_finderPathWithPaginationFindByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C",
			new String[] {
				String.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"uuid_", "companyId"}, true);

		_finderPathWithoutPaginationFindByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "companyId"}, true);

		_finderPathCountByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "companyId"}, false);

		_finderPathWithPaginationFindByListTypeDefinitionId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByListTypeDefinitionId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"listTypeDefinitionId"}, true);

		_finderPathWithoutPaginationFindByListTypeDefinitionId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByListTypeDefinitionId", new String[] {Long.class.getName()},
			new String[] {"listTypeDefinitionId"}, true);

		_finderPathCountByListTypeDefinitionId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByListTypeDefinitionId", new String[] {Long.class.getName()},
			new String[] {"listTypeDefinitionId"}, false);

		_finderPathWithPaginationFindByObjectDefinitionId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByObjectDefinitionId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"objectDefinitionId"}, true);

		_finderPathWithoutPaginationFindByObjectDefinitionId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByObjectDefinitionId", new String[] {Long.class.getName()},
			new String[] {"objectDefinitionId"}, true);

		_finderPathCountByObjectDefinitionId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByObjectDefinitionId", new String[] {Long.class.getName()},
			new String[] {"objectDefinitionId"}, false);

		_finderPathWithPaginationFindByODI_DTN = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByODI_DTN",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"objectDefinitionId", "dbTableName"}, true);

		_finderPathWithoutPaginationFindByODI_DTN = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByODI_DTN",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"objectDefinitionId", "dbTableName"}, true);

		_finderPathCountByODI_DTN = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByODI_DTN",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"objectDefinitionId", "dbTableName"}, false);

		_finderPathFetchByODI_N = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByODI_N",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"objectDefinitionId", "name"}, true);

		_finderPathCountByODI_N = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByODI_N",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"objectDefinitionId", "name"}, false);

		_setObjectFieldUtilPersistence(this);
	}

	@Deactivate
	public void deactivate() {
		_setObjectFieldUtilPersistence(null);

		entityCache.removeCache(ObjectFieldImpl.class.getName());
	}

	private void _setObjectFieldUtilPersistence(
		ObjectFieldPersistence objectFieldPersistence) {

		try {
			Field field = ObjectFieldUtil.class.getDeclaredField(
				"_persistence");

			field.setAccessible(true);

			field.set(null, objectFieldPersistence);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Override
	@Reference(
		target = ObjectPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = ObjectPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = ObjectPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_OBJECTFIELD =
		"SELECT objectField FROM ObjectField objectField";

	private static final String _SQL_SELECT_OBJECTFIELD_WHERE =
		"SELECT objectField FROM ObjectField objectField WHERE ";

	private static final String _SQL_COUNT_OBJECTFIELD =
		"SELECT COUNT(objectField) FROM ObjectField objectField";

	private static final String _SQL_COUNT_OBJECTFIELD_WHERE =
		"SELECT COUNT(objectField) FROM ObjectField objectField WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "objectField.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No ObjectField exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No ObjectField exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		ObjectFieldPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid"});

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

	@Reference
	private PortalUUID _portalUUID;

	@Reference
	private ObjectFieldModelArgumentsResolver
		_objectFieldModelArgumentsResolver;

}