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

package com.liferay.commerce.price.list.service.persistence.impl;

import com.liferay.commerce.price.list.exception.NoSuchPriceListException;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.model.CommercePriceListTable;
import com.liferay.commerce.price.list.model.impl.CommercePriceListImpl;
import com.liferay.commerce.price.list.model.impl.CommercePriceListModelImpl;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListPersistence;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.change.tracking.CTColumnResolutionType;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.permission.InlineSQLHelperUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.change.tracking.helper.CTPersistenceHelper;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUID;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The persistence implementation for the commerce price list service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @generated
 */
public class CommercePriceListPersistenceImpl
	extends BasePersistenceImpl<CommercePriceList>
	implements CommercePriceListPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>CommercePriceListUtil</code> to access the commerce price list persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		CommercePriceListImpl.class.getName();

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
	 * Returns all the commerce price lists where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price lists where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByUuid(String uuid, int start, int end) {
		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByUuid;
				finderArgs = new Object[] {uuid};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByUuid;
			finderArgs = new Object[] {uuid, start, end, orderByComparator};
		}

		List<CommercePriceList> list = null;

		if (useFinderCache && productionMode) {
			list = (List<CommercePriceList>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceList commercePriceList : list) {
					if (!uuid.equals(commercePriceList.getUuid())) {
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

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

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
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
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

				list = (List<CommercePriceList>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
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
	 * Returns the first commerce price list in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByUuid_First(
			String uuid, OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByUuid_First(
			uuid, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the first commerce price list in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByUuid_First(
		String uuid, OrderByComparator<CommercePriceList> orderByComparator) {

		List<CommercePriceList> list = findByUuid(
			uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price list in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByUuid_Last(
			String uuid, OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByUuid_Last(
			uuid, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the last commerce price list in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByUuid_Last(
		String uuid, OrderByComparator<CommercePriceList> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<CommercePriceList> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price lists before and after the current commerce price list in the ordered set where uuid = &#63;.
	 *
	 * @param commercePriceListId the primary key of the current commerce price list
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList[] findByUuid_PrevAndNext(
			long commercePriceListId, String uuid,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		uuid = Objects.toString(uuid, "");

		CommercePriceList commercePriceList = findByPrimaryKey(
			commercePriceListId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceList[] array = new CommercePriceListImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, commercePriceList, uuid, orderByComparator, true);

			array[1] = commercePriceList;

			array[2] = getByUuid_PrevAndNext(
				session, commercePriceList, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommercePriceList getByUuid_PrevAndNext(
		Session session, CommercePriceList commercePriceList, String uuid,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

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
			sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
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
					orderByComparator.getOrderByConditionValues(
						commercePriceList)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceList> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price lists where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (CommercePriceList commercePriceList :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(commercePriceList);
		}
	}

	/**
	 * Returns the number of commerce price lists where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByUuid;

			finderArgs = new Object[] {uuid};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

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

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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
		"commercePriceList.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(commercePriceList.uuid IS NULL OR commercePriceList.uuid = '')";

	private FinderPath _finderPathFetchByUUID_G;
	private FinderPath _finderPathCountByUUID_G;

	/**
	 * Returns the commerce price list where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchPriceListException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByUUID_G(String uuid, long groupId)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByUUID_G(uuid, groupId);

		if (commercePriceList == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("uuid=");
			sb.append(uuid);

			sb.append(", groupId=");
			sb.append(groupId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchPriceListException(sb.toString());
		}

		return commercePriceList;
	}

	/**
	 * Returns the commerce price list where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByUUID_G(String uuid, long groupId) {
		return fetchByUUID_G(uuid, groupId, true);
	}

	/**
	 * Returns the commerce price list where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		Object[] finderArgs = null;

		if (useFinderCache && productionMode) {
			finderArgs = new Object[] {uuid, groupId};
		}

		Object result = null;

		if (useFinderCache && productionMode) {
			result = finderCache.getResult(
				_finderPathFetchByUUID_G, finderArgs);
		}

		if (result instanceof CommercePriceList) {
			CommercePriceList commercePriceList = (CommercePriceList)result;

			if (!Objects.equals(uuid, commercePriceList.getUuid()) ||
				(groupId != commercePriceList.getGroupId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_G_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_G_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(groupId);

				List<CommercePriceList> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache && productionMode) {
						finderCache.putResult(
							_finderPathFetchByUUID_G, finderArgs, list);
					}
				}
				else {
					CommercePriceList commercePriceList = list.get(0);

					result = commercePriceList;

					cacheResult(commercePriceList);
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
			return (CommercePriceList)result;
		}
	}

	/**
	 * Removes the commerce price list where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the commerce price list that was removed
	 */
	@Override
	public CommercePriceList removeByUUID_G(String uuid, long groupId)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = findByUUID_G(uuid, groupId);

		return remove(commercePriceList);
	}

	/**
	 * Returns the number of commerce price lists where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByUUID_G(String uuid, long groupId) {
		uuid = Objects.toString(uuid, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByUUID_G;

			finderArgs = new Object[] {uuid, groupId};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_G_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_G_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(groupId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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

	private static final String _FINDER_COLUMN_UUID_G_UUID_2 =
		"commercePriceList.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_G_UUID_3 =
		"(commercePriceList.uuid IS NULL OR commercePriceList.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_G_GROUPID_2 =
		"commercePriceList.groupId = ?";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the commerce price lists where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByUuid_C(String uuid, long companyId) {
		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price lists where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByUuid_C;
				finderArgs = new Object[] {uuid, companyId};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByUuid_C;
			finderArgs = new Object[] {
				uuid, companyId, start, end, orderByComparator
			};
		}

		List<CommercePriceList> list = null;

		if (useFinderCache && productionMode) {
			list = (List<CommercePriceList>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceList commercePriceList : list) {
					if (!uuid.equals(commercePriceList.getUuid()) ||
						(companyId != commercePriceList.getCompanyId())) {

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

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

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
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
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

				list = (List<CommercePriceList>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
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
	 * Returns the first commerce price list in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByUuid_C_First(
			uuid, companyId, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the first commerce price list in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<CommercePriceList> orderByComparator) {

		List<CommercePriceList> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price list in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the last commerce price list in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<CommercePriceList> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<CommercePriceList> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price lists before and after the current commerce price list in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param commercePriceListId the primary key of the current commerce price list
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList[] findByUuid_C_PrevAndNext(
			long commercePriceListId, String uuid, long companyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		uuid = Objects.toString(uuid, "");

		CommercePriceList commercePriceList = findByPrimaryKey(
			commercePriceListId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceList[] array = new CommercePriceListImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, commercePriceList, uuid, companyId, orderByComparator,
				true);

			array[1] = commercePriceList;

			array[2] = getByUuid_C_PrevAndNext(
				session, commercePriceList, uuid, companyId, orderByComparator,
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

	protected CommercePriceList getByUuid_C_PrevAndNext(
		Session session, CommercePriceList commercePriceList, String uuid,
		long companyId, OrderByComparator<CommercePriceList> orderByComparator,
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

		sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

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
			sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
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
					orderByComparator.getOrderByConditionValues(
						commercePriceList)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceList> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price lists where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (CommercePriceList commercePriceList :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(commercePriceList);
		}
	}

	/**
	 * Returns the number of commerce price lists where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByUuid_C;

			finderArgs = new Object[] {uuid, companyId};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

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

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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
		"commercePriceList.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(commercePriceList.uuid IS NULL OR commercePriceList.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"commercePriceList.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByCompanyId;
	private FinderPath _finderPathWithoutPaginationFindByCompanyId;
	private FinderPath _finderPathCountByCompanyId;

	/**
	 * Returns all the commerce price lists where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByCompanyId(long companyId) {
		return findByCompanyId(
			companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price lists where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByCompanyId(
		long companyId, int start, int end) {

		return findByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		return findByCompanyId(companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByCompanyId;
				finderArgs = new Object[] {companyId};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByCompanyId;
			finderArgs = new Object[] {
				companyId, start, end, orderByComparator
			};
		}

		List<CommercePriceList> list = null;

		if (useFinderCache && productionMode) {
			list = (List<CommercePriceList>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceList commercePriceList : list) {
					if (companyId != commercePriceList.getCompanyId()) {
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

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				list = (List<CommercePriceList>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
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
	 * Returns the first commerce price list in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByCompanyId_First(
			long companyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByCompanyId_First(
			companyId, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the first commerce price list in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByCompanyId_First(
		long companyId,
		OrderByComparator<CommercePriceList> orderByComparator) {

		List<CommercePriceList> list = findByCompanyId(
			companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price list in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByCompanyId_Last(
			long companyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByCompanyId_Last(
			companyId, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the last commerce price list in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByCompanyId_Last(
		long companyId,
		OrderByComparator<CommercePriceList> orderByComparator) {

		int count = countByCompanyId(companyId);

		if (count == 0) {
			return null;
		}

		List<CommercePriceList> list = findByCompanyId(
			companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price lists before and after the current commerce price list in the ordered set where companyId = &#63;.
	 *
	 * @param commercePriceListId the primary key of the current commerce price list
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList[] findByCompanyId_PrevAndNext(
			long commercePriceListId, long companyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = findByPrimaryKey(
			commercePriceListId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceList[] array = new CommercePriceListImpl[3];

			array[0] = getByCompanyId_PrevAndNext(
				session, commercePriceList, companyId, orderByComparator, true);

			array[1] = commercePriceList;

			array[2] = getByCompanyId_PrevAndNext(
				session, commercePriceList, companyId, orderByComparator,
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

	protected CommercePriceList getByCompanyId_PrevAndNext(
		Session session, CommercePriceList commercePriceList, long companyId,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

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
			sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceList)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceList> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price lists where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	@Override
	public void removeByCompanyId(long companyId) {
		for (CommercePriceList commercePriceList :
				findByCompanyId(
					companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(commercePriceList);
		}
	}

	/**
	 * Returns the number of commerce price lists where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByCompanyId(long companyId) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByCompanyId;

			finderArgs = new Object[] {companyId};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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

	private static final String _FINDER_COLUMN_COMPANYID_COMPANYID_2 =
		"commercePriceList.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByCommerceCurrencyId;
	private FinderPath _finderPathWithoutPaginationFindByCommerceCurrencyId;
	private FinderPath _finderPathCountByCommerceCurrencyId;

	/**
	 * Returns all the commerce price lists where commerceCurrencyId = &#63;.
	 *
	 * @param commerceCurrencyId the commerce currency ID
	 * @return the matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByCommerceCurrencyId(
		long commerceCurrencyId) {

		return findByCommerceCurrencyId(
			commerceCurrencyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price lists where commerceCurrencyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param commerceCurrencyId the commerce currency ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByCommerceCurrencyId(
		long commerceCurrencyId, int start, int end) {

		return findByCommerceCurrencyId(commerceCurrencyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where commerceCurrencyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param commerceCurrencyId the commerce currency ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByCommerceCurrencyId(
		long commerceCurrencyId, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		return findByCommerceCurrencyId(
			commerceCurrencyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where commerceCurrencyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param commerceCurrencyId the commerce currency ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByCommerceCurrencyId(
		long commerceCurrencyId, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath =
					_finderPathWithoutPaginationFindByCommerceCurrencyId;
				finderArgs = new Object[] {commerceCurrencyId};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByCommerceCurrencyId;
			finderArgs = new Object[] {
				commerceCurrencyId, start, end, orderByComparator
			};
		}

		List<CommercePriceList> list = null;

		if (useFinderCache && productionMode) {
			list = (List<CommercePriceList>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceList commercePriceList : list) {
					if (commerceCurrencyId !=
							commercePriceList.getCommerceCurrencyId()) {

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

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_COMMERCECURRENCYID_COMMERCECURRENCYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commerceCurrencyId);

				list = (List<CommercePriceList>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
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
	 * Returns the first commerce price list in the ordered set where commerceCurrencyId = &#63;.
	 *
	 * @param commerceCurrencyId the commerce currency ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByCommerceCurrencyId_First(
			long commerceCurrencyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByCommerceCurrencyId_First(
			commerceCurrencyId, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("commerceCurrencyId=");
		sb.append(commerceCurrencyId);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the first commerce price list in the ordered set where commerceCurrencyId = &#63;.
	 *
	 * @param commerceCurrencyId the commerce currency ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByCommerceCurrencyId_First(
		long commerceCurrencyId,
		OrderByComparator<CommercePriceList> orderByComparator) {

		List<CommercePriceList> list = findByCommerceCurrencyId(
			commerceCurrencyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price list in the ordered set where commerceCurrencyId = &#63;.
	 *
	 * @param commerceCurrencyId the commerce currency ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByCommerceCurrencyId_Last(
			long commerceCurrencyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByCommerceCurrencyId_Last(
			commerceCurrencyId, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("commerceCurrencyId=");
		sb.append(commerceCurrencyId);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the last commerce price list in the ordered set where commerceCurrencyId = &#63;.
	 *
	 * @param commerceCurrencyId the commerce currency ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByCommerceCurrencyId_Last(
		long commerceCurrencyId,
		OrderByComparator<CommercePriceList> orderByComparator) {

		int count = countByCommerceCurrencyId(commerceCurrencyId);

		if (count == 0) {
			return null;
		}

		List<CommercePriceList> list = findByCommerceCurrencyId(
			commerceCurrencyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price lists before and after the current commerce price list in the ordered set where commerceCurrencyId = &#63;.
	 *
	 * @param commercePriceListId the primary key of the current commerce price list
	 * @param commerceCurrencyId the commerce currency ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList[] findByCommerceCurrencyId_PrevAndNext(
			long commercePriceListId, long commerceCurrencyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = findByPrimaryKey(
			commercePriceListId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceList[] array = new CommercePriceListImpl[3];

			array[0] = getByCommerceCurrencyId_PrevAndNext(
				session, commercePriceList, commerceCurrencyId,
				orderByComparator, true);

			array[1] = commercePriceList;

			array[2] = getByCommerceCurrencyId_PrevAndNext(
				session, commercePriceList, commerceCurrencyId,
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

	protected CommercePriceList getByCommerceCurrencyId_PrevAndNext(
		Session session, CommercePriceList commercePriceList,
		long commerceCurrencyId,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

		sb.append(_FINDER_COLUMN_COMMERCECURRENCYID_COMMERCECURRENCYID_2);

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
			sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(commerceCurrencyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceList)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceList> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price lists where commerceCurrencyId = &#63; from the database.
	 *
	 * @param commerceCurrencyId the commerce currency ID
	 */
	@Override
	public void removeByCommerceCurrencyId(long commerceCurrencyId) {
		for (CommercePriceList commercePriceList :
				findByCommerceCurrencyId(
					commerceCurrencyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(commercePriceList);
		}
	}

	/**
	 * Returns the number of commerce price lists where commerceCurrencyId = &#63;.
	 *
	 * @param commerceCurrencyId the commerce currency ID
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByCommerceCurrencyId(long commerceCurrencyId) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByCommerceCurrencyId;

			finderArgs = new Object[] {commerceCurrencyId};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_COMMERCECURRENCYID_COMMERCECURRENCYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commerceCurrencyId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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
		_FINDER_COLUMN_COMMERCECURRENCYID_COMMERCECURRENCYID_2 =
			"commercePriceList.commerceCurrencyId = ?";

	private FinderPath _finderPathFetchByParentCommercePriceListId;
	private FinderPath _finderPathCountByParentCommercePriceListId;

	/**
	 * Returns the commerce price list where parentCommercePriceListId = &#63; or throws a <code>NoSuchPriceListException</code> if it could not be found.
	 *
	 * @param parentCommercePriceListId the parent commerce price list ID
	 * @return the matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByParentCommercePriceListId(
			long parentCommercePriceListId)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByParentCommercePriceListId(
			parentCommercePriceListId);

		if (commercePriceList == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("parentCommercePriceListId=");
			sb.append(parentCommercePriceListId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchPriceListException(sb.toString());
		}

		return commercePriceList;
	}

	/**
	 * Returns the commerce price list where parentCommercePriceListId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param parentCommercePriceListId the parent commerce price list ID
	 * @return the matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByParentCommercePriceListId(
		long parentCommercePriceListId) {

		return fetchByParentCommercePriceListId(
			parentCommercePriceListId, true);
	}

	/**
	 * Returns the commerce price list where parentCommercePriceListId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param parentCommercePriceListId the parent commerce price list ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByParentCommercePriceListId(
		long parentCommercePriceListId, boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		Object[] finderArgs = null;

		if (useFinderCache && productionMode) {
			finderArgs = new Object[] {parentCommercePriceListId};
		}

		Object result = null;

		if (useFinderCache && productionMode) {
			result = finderCache.getResult(
				_finderPathFetchByParentCommercePriceListId, finderArgs);
		}

		if (result instanceof CommercePriceList) {
			CommercePriceList commercePriceList = (CommercePriceList)result;

			if (parentCommercePriceListId !=
					commercePriceList.getParentCommercePriceListId()) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			sb.append(
				_FINDER_COLUMN_PARENTCOMMERCEPRICELISTID_PARENTCOMMERCEPRICELISTID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(parentCommercePriceListId);

				List<CommercePriceList> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache && productionMode) {
						finderCache.putResult(
							_finderPathFetchByParentCommercePriceListId,
							finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!productionMode || !useFinderCache) {
								finderArgs = new Object[] {
									parentCommercePriceListId
								};
							}

							_log.warn(
								"CommercePriceListPersistenceImpl.fetchByParentCommercePriceListId(long, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					CommercePriceList commercePriceList = list.get(0);

					result = commercePriceList;

					cacheResult(commercePriceList);
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
			return (CommercePriceList)result;
		}
	}

	/**
	 * Removes the commerce price list where parentCommercePriceListId = &#63; from the database.
	 *
	 * @param parentCommercePriceListId the parent commerce price list ID
	 * @return the commerce price list that was removed
	 */
	@Override
	public CommercePriceList removeByParentCommercePriceListId(
			long parentCommercePriceListId)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = findByParentCommercePriceListId(
			parentCommercePriceListId);

		return remove(commercePriceList);
	}

	/**
	 * Returns the number of commerce price lists where parentCommercePriceListId = &#63;.
	 *
	 * @param parentCommercePriceListId the parent commerce price list ID
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByParentCommercePriceListId(
		long parentCommercePriceListId) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByParentCommercePriceListId;

			finderArgs = new Object[] {parentCommercePriceListId};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			sb.append(
				_FINDER_COLUMN_PARENTCOMMERCEPRICELISTID_PARENTCOMMERCEPRICELISTID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(parentCommercePriceListId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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
		_FINDER_COLUMN_PARENTCOMMERCEPRICELISTID_PARENTCOMMERCEPRICELISTID_2 =
			"commercePriceList.parentCommercePriceListId = ?";

	private FinderPath _finderPathWithPaginationFindByG_C;
	private FinderPath _finderPathWithoutPaginationFindByG_C;
	private FinderPath _finderPathCountByG_C;
	private FinderPath _finderPathWithPaginationCountByG_C;

	/**
	 * Returns all the commerce price lists where groupId = &#63; and companyId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @return the matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C(long groupId, long companyId) {
		return findByG_C(
			groupId, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price lists where groupId = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C(
		long groupId, long companyId, int start, int end) {

		return findByG_C(groupId, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where groupId = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C(
		long groupId, long companyId, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		return findByG_C(
			groupId, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where groupId = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C(
		long groupId, long companyId, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByG_C;
				finderArgs = new Object[] {groupId, companyId};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByG_C;
			finderArgs = new Object[] {
				groupId, companyId, start, end, orderByComparator
			};
		}

		List<CommercePriceList> list = null;

		if (useFinderCache && productionMode) {
			list = (List<CommercePriceList>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceList commercePriceList : list) {
					if ((groupId != commercePriceList.getGroupId()) ||
						(companyId != commercePriceList.getCompanyId())) {

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

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_G_C_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(companyId);

				list = (List<CommercePriceList>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
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
	 * Returns the first commerce price list in the ordered set where groupId = &#63; and companyId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByG_C_First(
			long groupId, long companyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByG_C_First(
			groupId, companyId, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the first commerce price list in the ordered set where groupId = &#63; and companyId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByG_C_First(
		long groupId, long companyId,
		OrderByComparator<CommercePriceList> orderByComparator) {

		List<CommercePriceList> list = findByG_C(
			groupId, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price list in the ordered set where groupId = &#63; and companyId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByG_C_Last(
			long groupId, long companyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByG_C_Last(
			groupId, companyId, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the last commerce price list in the ordered set where groupId = &#63; and companyId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByG_C_Last(
		long groupId, long companyId,
		OrderByComparator<CommercePriceList> orderByComparator) {

		int count = countByG_C(groupId, companyId);

		if (count == 0) {
			return null;
		}

		List<CommercePriceList> list = findByG_C(
			groupId, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price lists before and after the current commerce price list in the ordered set where groupId = &#63; and companyId = &#63;.
	 *
	 * @param commercePriceListId the primary key of the current commerce price list
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList[] findByG_C_PrevAndNext(
			long commercePriceListId, long groupId, long companyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = findByPrimaryKey(
			commercePriceListId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceList[] array = new CommercePriceListImpl[3];

			array[0] = getByG_C_PrevAndNext(
				session, commercePriceList, groupId, companyId,
				orderByComparator, true);

			array[1] = commercePriceList;

			array[2] = getByG_C_PrevAndNext(
				session, commercePriceList, groupId, companyId,
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

	protected CommercePriceList getByG_C_PrevAndNext(
		Session session, CommercePriceList commercePriceList, long groupId,
		long companyId, OrderByComparator<CommercePriceList> orderByComparator,
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

		sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

		sb.append(_FINDER_COLUMN_G_C_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_C_COMPANYID_2);

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
			sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceList)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceList> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the commerce price lists that the user has permission to view where groupId = &#63; and companyId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @return the matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C(
		long groupId, long companyId) {

		return filterFindByG_C(
			groupId, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price lists that the user has permission to view where groupId = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C(
		long groupId, long companyId, int start, int end) {

		return filterFindByG_C(groupId, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists that the user has permissions to view where groupId = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C(
		long groupId, long companyId, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByG_C(groupId, companyId, start, end, orderByComparator);
		}

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(5);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_COMMERCEPRICELIST_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_G_C_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_C_COMPANYID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, CommercePriceListImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, CommercePriceListImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			queryPos.add(companyId);

			return (List<CommercePriceList>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the commerce price lists before and after the current commerce price list in the ordered set of commerce price lists that the user has permission to view where groupId = &#63; and companyId = &#63;.
	 *
	 * @param commercePriceListId the primary key of the current commerce price list
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList[] filterFindByG_C_PrevAndNext(
			long commercePriceListId, long groupId, long companyId,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByG_C_PrevAndNext(
				commercePriceListId, groupId, companyId, orderByComparator);
		}

		CommercePriceList commercePriceList = findByPrimaryKey(
			commercePriceListId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceList[] array = new CommercePriceListImpl[3];

			array[0] = filterGetByG_C_PrevAndNext(
				session, commercePriceList, groupId, companyId,
				orderByComparator, true);

			array[1] = commercePriceList;

			array[2] = filterGetByG_C_PrevAndNext(
				session, commercePriceList, groupId, companyId,
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

	protected CommercePriceList filterGetByG_C_PrevAndNext(
		Session session, CommercePriceList commercePriceList, long groupId,
		long companyId, OrderByComparator<CommercePriceList> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_COMMERCEPRICELIST_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_G_C_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_C_COMPANYID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByConditionFields[i],
							true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByConditionFields[i],
							true));
				}

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
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByFields[i], true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByFields[i], true));
				}

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
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(
				_FILTER_ENTITY_ALIAS, CommercePriceListImpl.class);
		}
		else {
			sqlQuery.addEntity(
				_FILTER_ENTITY_TABLE, CommercePriceListImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		queryPos.add(groupId);

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceList)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceList> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the commerce price lists that the user has permission to view where groupId = any &#63; and companyId = &#63;.
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @return the matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C(
		long[] groupIds, long companyId) {

		return filterFindByG_C(
			groupIds, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price lists that the user has permission to view where groupId = any &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C(
		long[] groupIds, long companyId, int start, int end) {

		return filterFindByG_C(groupIds, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists that the user has permission to view where groupId = any &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C(
		long[] groupIds, long companyId, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(groupIds)) {
			return findByG_C(
				groupIds, companyId, start, end, orderByComparator);
		}

		if (groupIds == null) {
			groupIds = new long[0];
		}
		else if (groupIds.length > 1) {
			groupIds = ArrayUtil.sortedUnique(groupIds);
		}

		StringBundler sb = new StringBundler();

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_COMMERCEPRICELIST_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_1);
		}

		if (groupIds.length > 0) {
			sb.append("(");

			sb.append(_FINDER_COLUMN_G_C_GROUPID_7);

			sb.append(StringUtil.merge(groupIds));

			sb.append(")");

			sb.append(")");

			sb.append(WHERE_AND);
		}

		sb.append(_FINDER_COLUMN_G_C_COMPANYID_2);

		sb.setStringAt(
			removeConjunction(sb.stringAt(sb.index() - 1)), sb.index() - 1);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupIds);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, CommercePriceListImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, CommercePriceListImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(companyId);

			return (List<CommercePriceList>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns all the commerce price lists where groupId = any &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @return the matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C(long[] groupIds, long companyId) {
		return findByG_C(
			groupIds, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price lists where groupId = any &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C(
		long[] groupIds, long companyId, int start, int end) {

		return findByG_C(groupIds, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where groupId = any &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C(
		long[] groupIds, long companyId, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		return findByG_C(
			groupIds, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where groupId = &#63; and companyId = &#63;, optionally using the finder cache.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C(
		long[] groupIds, long companyId, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean useFinderCache) {

		if (groupIds == null) {
			groupIds = new long[0];
		}
		else if (groupIds.length > 1) {
			groupIds = ArrayUtil.sortedUnique(groupIds);
		}

		if (groupIds.length == 1) {
			return findByG_C(
				groupIds[0], companyId, start, end, orderByComparator);
		}

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderArgs = new Object[] {
					StringUtil.merge(groupIds), companyId
				};
			}
		}
		else if (useFinderCache && productionMode) {
			finderArgs = new Object[] {
				StringUtil.merge(groupIds), companyId, start, end,
				orderByComparator
			};
		}

		List<CommercePriceList> list = null;

		if (useFinderCache && productionMode) {
			list = (List<CommercePriceList>)finderCache.getResult(
				_finderPathWithPaginationFindByG_C, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceList commercePriceList : list) {
					if (!ArrayUtil.contains(
							groupIds, commercePriceList.getGroupId()) ||
						(companyId != commercePriceList.getCompanyId())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = new StringBundler();

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			if (groupIds.length > 0) {
				sb.append("(");

				sb.append(_FINDER_COLUMN_G_C_GROUPID_7);

				sb.append(StringUtil.merge(groupIds));

				sb.append(")");

				sb.append(")");

				sb.append(WHERE_AND);
			}

			sb.append(_FINDER_COLUMN_G_C_COMPANYID_2);

			sb.setStringAt(
				removeConjunction(sb.stringAt(sb.index() - 1)), sb.index() - 1);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				list = (List<CommercePriceList>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(
						_finderPathWithPaginationFindByG_C, finderArgs, list);
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
	 * Removes all the commerce price lists where groupId = &#63; and companyId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 */
	@Override
	public void removeByG_C(long groupId, long companyId) {
		for (CommercePriceList commercePriceList :
				findByG_C(
					groupId, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(commercePriceList);
		}
	}

	/**
	 * Returns the number of commerce price lists where groupId = &#63; and companyId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByG_C(long groupId, long companyId) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByG_C;

			finderArgs = new Object[] {groupId, companyId};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_G_C_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_C_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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

	/**
	 * Returns the number of commerce price lists where groupId = any &#63; and companyId = &#63;.
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByG_C(long[] groupIds, long companyId) {
		if (groupIds == null) {
			groupIds = new long[0];
		}
		else if (groupIds.length > 1) {
			groupIds = ArrayUtil.sortedUnique(groupIds);
		}

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderArgs = new Object[] {StringUtil.merge(groupIds), companyId};

			count = (Long)finderCache.getResult(
				_finderPathWithPaginationCountByG_C, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler();

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			if (groupIds.length > 0) {
				sb.append("(");

				sb.append(_FINDER_COLUMN_G_C_GROUPID_7);

				sb.append(StringUtil.merge(groupIds));

				sb.append(")");

				sb.append(")");

				sb.append(WHERE_AND);
			}

			sb.append(_FINDER_COLUMN_G_C_COMPANYID_2);

			sb.setStringAt(
				removeConjunction(sb.stringAt(sb.index() - 1)), sb.index() - 1);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(
						_finderPathWithPaginationCountByG_C, finderArgs, count);
				}
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

	/**
	 * Returns the number of commerce price lists that the user has permission to view where groupId = &#63; and companyId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @return the number of matching commerce price lists that the user has permission to view
	 */
	@Override
	public int filterCountByG_C(long groupId, long companyId) {
		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return countByG_C(groupId, companyId);
		}

		StringBundler sb = new StringBundler(3);

		sb.append(_FILTER_SQL_COUNT_COMMERCEPRICELIST_WHERE);

		sb.append(_FINDER_COLUMN_G_C_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_C_COMPANYID_2);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			queryPos.add(companyId);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the number of commerce price lists that the user has permission to view where groupId = any &#63; and companyId = &#63;.
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @return the number of matching commerce price lists that the user has permission to view
	 */
	@Override
	public int filterCountByG_C(long[] groupIds, long companyId) {
		if (!InlineSQLHelperUtil.isEnabled(groupIds)) {
			return countByG_C(groupIds, companyId);
		}

		if (groupIds == null) {
			groupIds = new long[0];
		}
		else if (groupIds.length > 1) {
			groupIds = ArrayUtil.sortedUnique(groupIds);
		}

		StringBundler sb = new StringBundler();

		sb.append(_FILTER_SQL_COUNT_COMMERCEPRICELIST_WHERE);

		if (groupIds.length > 0) {
			sb.append("(");

			sb.append(_FINDER_COLUMN_G_C_GROUPID_7);

			sb.append(StringUtil.merge(groupIds));

			sb.append(")");

			sb.append(")");

			sb.append(WHERE_AND);
		}

		sb.append(_FINDER_COLUMN_G_C_COMPANYID_2);

		sb.setStringAt(
			removeConjunction(sb.stringAt(sb.index() - 1)), sb.index() - 1);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupIds);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(companyId);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String _FINDER_COLUMN_G_C_GROUPID_2 =
		"commercePriceList.groupId = ? AND ";

	private static final String _FINDER_COLUMN_G_C_GROUPID_7 =
		"commercePriceList.groupId IN (";

	private static final String _FINDER_COLUMN_G_C_COMPANYID_2 =
		"commercePriceList.companyId = ?";

	private FinderPath _finderPathFetchByG_CatalogBasePriceList;
	private FinderPath _finderPathCountByG_CatalogBasePriceList;

	/**
	 * Returns the commerce price list where groupId = &#63; and catalogBasePriceList = &#63; or throws a <code>NoSuchPriceListException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param catalogBasePriceList the catalog base price list
	 * @return the matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByG_CatalogBasePriceList(
			long groupId, boolean catalogBasePriceList)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByG_CatalogBasePriceList(
			groupId, catalogBasePriceList);

		if (commercePriceList == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("groupId=");
			sb.append(groupId);

			sb.append(", catalogBasePriceList=");
			sb.append(catalogBasePriceList);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchPriceListException(sb.toString());
		}

		return commercePriceList;
	}

	/**
	 * Returns the commerce price list where groupId = &#63; and catalogBasePriceList = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param catalogBasePriceList the catalog base price list
	 * @return the matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByG_CatalogBasePriceList(
		long groupId, boolean catalogBasePriceList) {

		return fetchByG_CatalogBasePriceList(
			groupId, catalogBasePriceList, true);
	}

	/**
	 * Returns the commerce price list where groupId = &#63; and catalogBasePriceList = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param catalogBasePriceList the catalog base price list
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByG_CatalogBasePriceList(
		long groupId, boolean catalogBasePriceList, boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		Object[] finderArgs = null;

		if (useFinderCache && productionMode) {
			finderArgs = new Object[] {groupId, catalogBasePriceList};
		}

		Object result = null;

		if (useFinderCache && productionMode) {
			result = finderCache.getResult(
				_finderPathFetchByG_CatalogBasePriceList, finderArgs);
		}

		if (result instanceof CommercePriceList) {
			CommercePriceList commercePriceList = (CommercePriceList)result;

			if ((groupId != commercePriceList.getGroupId()) ||
				(catalogBasePriceList !=
					commercePriceList.isCatalogBasePriceList())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_G_CATALOGBASEPRICELIST_GROUPID_2);

			sb.append(
				_FINDER_COLUMN_G_CATALOGBASEPRICELIST_CATALOGBASEPRICELIST_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(catalogBasePriceList);

				List<CommercePriceList> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache && productionMode) {
						finderCache.putResult(
							_finderPathFetchByG_CatalogBasePriceList,
							finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!productionMode || !useFinderCache) {
								finderArgs = new Object[] {
									groupId, catalogBasePriceList
								};
							}

							_log.warn(
								"CommercePriceListPersistenceImpl.fetchByG_CatalogBasePriceList(long, boolean, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					CommercePriceList commercePriceList = list.get(0);

					result = commercePriceList;

					cacheResult(commercePriceList);
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
			return (CommercePriceList)result;
		}
	}

	/**
	 * Removes the commerce price list where groupId = &#63; and catalogBasePriceList = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param catalogBasePriceList the catalog base price list
	 * @return the commerce price list that was removed
	 */
	@Override
	public CommercePriceList removeByG_CatalogBasePriceList(
			long groupId, boolean catalogBasePriceList)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = findByG_CatalogBasePriceList(
			groupId, catalogBasePriceList);

		return remove(commercePriceList);
	}

	/**
	 * Returns the number of commerce price lists where groupId = &#63; and catalogBasePriceList = &#63;.
	 *
	 * @param groupId the group ID
	 * @param catalogBasePriceList the catalog base price list
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByG_CatalogBasePriceList(
		long groupId, boolean catalogBasePriceList) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByG_CatalogBasePriceList;

			finderArgs = new Object[] {groupId, catalogBasePriceList};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_G_CATALOGBASEPRICELIST_GROUPID_2);

			sb.append(
				_FINDER_COLUMN_G_CATALOGBASEPRICELIST_CATALOGBASEPRICELIST_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(catalogBasePriceList);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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
		_FINDER_COLUMN_G_CATALOGBASEPRICELIST_GROUPID_2 =
			"commercePriceList.groupId = ? AND ";

	private static final String
		_FINDER_COLUMN_G_CATALOGBASEPRICELIST_CATALOGBASEPRICELIST_2 =
			"commercePriceList.catalogBasePriceList = ?";

	private FinderPath _finderPathWithPaginationFindByLtD_S;
	private FinderPath _finderPathWithPaginationCountByLtD_S;

	/**
	 * Returns all the commerce price lists where displayDate &lt; &#63; and status = &#63;.
	 *
	 * @param displayDate the display date
	 * @param status the status
	 * @return the matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByLtD_S(Date displayDate, int status) {
		return findByLtD_S(
			displayDate, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price lists where displayDate &lt; &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param displayDate the display date
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByLtD_S(
		Date displayDate, int status, int start, int end) {

		return findByLtD_S(displayDate, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where displayDate &lt; &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param displayDate the display date
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByLtD_S(
		Date displayDate, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		return findByLtD_S(
			displayDate, status, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where displayDate &lt; &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param displayDate the display date
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByLtD_S(
		Date displayDate, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = _finderPathWithPaginationFindByLtD_S;
		finderArgs = new Object[] {
			_getTime(displayDate), status, start, end, orderByComparator
		};

		List<CommercePriceList> list = null;

		if (useFinderCache && productionMode) {
			list = (List<CommercePriceList>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceList commercePriceList : list) {
					if ((displayDate.getTime() <=
							commercePriceList.getDisplayDate(
							).getTime()) ||
						(status != commercePriceList.getStatus())) {

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

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			boolean bindDisplayDate = false;

			if (displayDate == null) {
				sb.append(_FINDER_COLUMN_LTD_S_DISPLAYDATE_1);
			}
			else {
				bindDisplayDate = true;

				sb.append(_FINDER_COLUMN_LTD_S_DISPLAYDATE_2);
			}

			sb.append(_FINDER_COLUMN_LTD_S_STATUS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindDisplayDate) {
					queryPos.add(new Timestamp(displayDate.getTime()));
				}

				queryPos.add(status);

				list = (List<CommercePriceList>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
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
	 * Returns the first commerce price list in the ordered set where displayDate &lt; &#63; and status = &#63;.
	 *
	 * @param displayDate the display date
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByLtD_S_First(
			Date displayDate, int status,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByLtD_S_First(
			displayDate, status, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("displayDate<");
		sb.append(displayDate);

		sb.append(", status=");
		sb.append(status);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the first commerce price list in the ordered set where displayDate &lt; &#63; and status = &#63;.
	 *
	 * @param displayDate the display date
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByLtD_S_First(
		Date displayDate, int status,
		OrderByComparator<CommercePriceList> orderByComparator) {

		List<CommercePriceList> list = findByLtD_S(
			displayDate, status, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price list in the ordered set where displayDate &lt; &#63; and status = &#63;.
	 *
	 * @param displayDate the display date
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByLtD_S_Last(
			Date displayDate, int status,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByLtD_S_Last(
			displayDate, status, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("displayDate<");
		sb.append(displayDate);

		sb.append(", status=");
		sb.append(status);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the last commerce price list in the ordered set where displayDate &lt; &#63; and status = &#63;.
	 *
	 * @param displayDate the display date
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByLtD_S_Last(
		Date displayDate, int status,
		OrderByComparator<CommercePriceList> orderByComparator) {

		int count = countByLtD_S(displayDate, status);

		if (count == 0) {
			return null;
		}

		List<CommercePriceList> list = findByLtD_S(
			displayDate, status, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price lists before and after the current commerce price list in the ordered set where displayDate &lt; &#63; and status = &#63;.
	 *
	 * @param commercePriceListId the primary key of the current commerce price list
	 * @param displayDate the display date
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList[] findByLtD_S_PrevAndNext(
			long commercePriceListId, Date displayDate, int status,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = findByPrimaryKey(
			commercePriceListId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceList[] array = new CommercePriceListImpl[3];

			array[0] = getByLtD_S_PrevAndNext(
				session, commercePriceList, displayDate, status,
				orderByComparator, true);

			array[1] = commercePriceList;

			array[2] = getByLtD_S_PrevAndNext(
				session, commercePriceList, displayDate, status,
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

	protected CommercePriceList getByLtD_S_PrevAndNext(
		Session session, CommercePriceList commercePriceList, Date displayDate,
		int status, OrderByComparator<CommercePriceList> orderByComparator,
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

		sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

		boolean bindDisplayDate = false;

		if (displayDate == null) {
			sb.append(_FINDER_COLUMN_LTD_S_DISPLAYDATE_1);
		}
		else {
			bindDisplayDate = true;

			sb.append(_FINDER_COLUMN_LTD_S_DISPLAYDATE_2);
		}

		sb.append(_FINDER_COLUMN_LTD_S_STATUS_2);

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
			sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindDisplayDate) {
			queryPos.add(new Timestamp(displayDate.getTime()));
		}

		queryPos.add(status);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceList)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceList> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price lists where displayDate &lt; &#63; and status = &#63; from the database.
	 *
	 * @param displayDate the display date
	 * @param status the status
	 */
	@Override
	public void removeByLtD_S(Date displayDate, int status) {
		for (CommercePriceList commercePriceList :
				findByLtD_S(
					displayDate, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(commercePriceList);
		}
	}

	/**
	 * Returns the number of commerce price lists where displayDate &lt; &#63; and status = &#63;.
	 *
	 * @param displayDate the display date
	 * @param status the status
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByLtD_S(Date displayDate, int status) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathWithPaginationCountByLtD_S;

			finderArgs = new Object[] {_getTime(displayDate), status};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			boolean bindDisplayDate = false;

			if (displayDate == null) {
				sb.append(_FINDER_COLUMN_LTD_S_DISPLAYDATE_1);
			}
			else {
				bindDisplayDate = true;

				sb.append(_FINDER_COLUMN_LTD_S_DISPLAYDATE_2);
			}

			sb.append(_FINDER_COLUMN_LTD_S_STATUS_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindDisplayDate) {
					queryPos.add(new Timestamp(displayDate.getTime()));
				}

				queryPos.add(status);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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

	private static final String _FINDER_COLUMN_LTD_S_DISPLAYDATE_1 =
		"commercePriceList.displayDate IS NULL AND ";

	private static final String _FINDER_COLUMN_LTD_S_DISPLAYDATE_2 =
		"commercePriceList.displayDate < ? AND ";

	private static final String _FINDER_COLUMN_LTD_S_STATUS_2 =
		"commercePriceList.status = ?";

	private FinderPath _finderPathWithPaginationFindByG_C_S;
	private FinderPath _finderPathWithoutPaginationFindByG_C_S;
	private FinderPath _finderPathCountByG_C_S;
	private FinderPath _finderPathWithPaginationCountByG_C_S;

	/**
	 * Returns all the commerce price lists where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @return the matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_S(
		long groupId, long companyId, int status) {

		return findByG_C_S(
			groupId, companyId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the commerce price lists where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_S(
		long groupId, long companyId, int status, int start, int end) {

		return findByG_C_S(groupId, companyId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_S(
		long groupId, long companyId, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		return findByG_C_S(
			groupId, companyId, status, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_S(
		long groupId, long companyId, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByG_C_S;
				finderArgs = new Object[] {groupId, companyId, status};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByG_C_S;
			finderArgs = new Object[] {
				groupId, companyId, status, start, end, orderByComparator
			};
		}

		List<CommercePriceList> list = null;

		if (useFinderCache && productionMode) {
			list = (List<CommercePriceList>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceList commercePriceList : list) {
					if ((groupId != commercePriceList.getGroupId()) ||
						(companyId != commercePriceList.getCompanyId()) ||
						(status != commercePriceList.getStatus())) {

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
					5 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(5);
			}

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_G_C_S_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_C_S_COMPANYID_2);

			sb.append(_FINDER_COLUMN_G_C_S_STATUS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(companyId);

				queryPos.add(status);

				list = (List<CommercePriceList>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
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
	 * Returns the first commerce price list in the ordered set where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByG_C_S_First(
			long groupId, long companyId, int status,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByG_C_S_First(
			groupId, companyId, status, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append(", status=");
		sb.append(status);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the first commerce price list in the ordered set where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByG_C_S_First(
		long groupId, long companyId, int status,
		OrderByComparator<CommercePriceList> orderByComparator) {

		List<CommercePriceList> list = findByG_C_S(
			groupId, companyId, status, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price list in the ordered set where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByG_C_S_Last(
			long groupId, long companyId, int status,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByG_C_S_Last(
			groupId, companyId, status, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append(", status=");
		sb.append(status);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the last commerce price list in the ordered set where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByG_C_S_Last(
		long groupId, long companyId, int status,
		OrderByComparator<CommercePriceList> orderByComparator) {

		int count = countByG_C_S(groupId, companyId, status);

		if (count == 0) {
			return null;
		}

		List<CommercePriceList> list = findByG_C_S(
			groupId, companyId, status, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price lists before and after the current commerce price list in the ordered set where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param commercePriceListId the primary key of the current commerce price list
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList[] findByG_C_S_PrevAndNext(
			long commercePriceListId, long groupId, long companyId, int status,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = findByPrimaryKey(
			commercePriceListId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceList[] array = new CommercePriceListImpl[3];

			array[0] = getByG_C_S_PrevAndNext(
				session, commercePriceList, groupId, companyId, status,
				orderByComparator, true);

			array[1] = commercePriceList;

			array[2] = getByG_C_S_PrevAndNext(
				session, commercePriceList, groupId, companyId, status,
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

	protected CommercePriceList getByG_C_S_PrevAndNext(
		Session session, CommercePriceList commercePriceList, long groupId,
		long companyId, int status,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

		sb.append(_FINDER_COLUMN_G_C_S_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_C_S_COMPANYID_2);

		sb.append(_FINDER_COLUMN_G_C_S_STATUS_2);

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
			sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		queryPos.add(companyId);

		queryPos.add(status);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceList)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceList> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the commerce price lists that the user has permission to view where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @return the matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C_S(
		long groupId, long companyId, int status) {

		return filterFindByG_C_S(
			groupId, companyId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the commerce price lists that the user has permission to view where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C_S(
		long groupId, long companyId, int status, int start, int end) {

		return filterFindByG_C_S(groupId, companyId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists that the user has permissions to view where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C_S(
		long groupId, long companyId, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByG_C_S(
				groupId, companyId, status, start, end, orderByComparator);
		}

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(6);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_COMMERCEPRICELIST_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_G_C_S_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_C_S_COMPANYID_2);

		sb.append(_FINDER_COLUMN_G_C_S_STATUS_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, CommercePriceListImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, CommercePriceListImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			queryPos.add(companyId);

			queryPos.add(status);

			return (List<CommercePriceList>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the commerce price lists before and after the current commerce price list in the ordered set of commerce price lists that the user has permission to view where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param commercePriceListId the primary key of the current commerce price list
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList[] filterFindByG_C_S_PrevAndNext(
			long commercePriceListId, long groupId, long companyId, int status,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByG_C_S_PrevAndNext(
				commercePriceListId, groupId, companyId, status,
				orderByComparator);
		}

		CommercePriceList commercePriceList = findByPrimaryKey(
			commercePriceListId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceList[] array = new CommercePriceListImpl[3];

			array[0] = filterGetByG_C_S_PrevAndNext(
				session, commercePriceList, groupId, companyId, status,
				orderByComparator, true);

			array[1] = commercePriceList;

			array[2] = filterGetByG_C_S_PrevAndNext(
				session, commercePriceList, groupId, companyId, status,
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

	protected CommercePriceList filterGetByG_C_S_PrevAndNext(
		Session session, CommercePriceList commercePriceList, long groupId,
		long companyId, int status,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				7 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(6);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_COMMERCEPRICELIST_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_G_C_S_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_C_S_COMPANYID_2);

		sb.append(_FINDER_COLUMN_G_C_S_STATUS_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByConditionFields[i],
							true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByConditionFields[i],
							true));
				}

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
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByFields[i], true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByFields[i], true));
				}

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
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(
				_FILTER_ENTITY_ALIAS, CommercePriceListImpl.class);
		}
		else {
			sqlQuery.addEntity(
				_FILTER_ENTITY_TABLE, CommercePriceListImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		queryPos.add(groupId);

		queryPos.add(companyId);

		queryPos.add(status);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceList)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceList> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the commerce price lists that the user has permission to view where groupId = any &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @return the matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C_S(
		long[] groupIds, long companyId, int status) {

		return filterFindByG_C_S(
			groupIds, companyId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the commerce price lists that the user has permission to view where groupId = any &#63; and companyId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C_S(
		long[] groupIds, long companyId, int status, int start, int end) {

		return filterFindByG_C_S(groupIds, companyId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists that the user has permission to view where groupId = any &#63; and companyId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C_S(
		long[] groupIds, long companyId, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(groupIds)) {
			return findByG_C_S(
				groupIds, companyId, status, start, end, orderByComparator);
		}

		if (groupIds == null) {
			groupIds = new long[0];
		}
		else if (groupIds.length > 1) {
			groupIds = ArrayUtil.sortedUnique(groupIds);
		}

		StringBundler sb = new StringBundler();

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_COMMERCEPRICELIST_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_1);
		}

		if (groupIds.length > 0) {
			sb.append("(");

			sb.append(_FINDER_COLUMN_G_C_S_GROUPID_7);

			sb.append(StringUtil.merge(groupIds));

			sb.append(")");

			sb.append(")");

			sb.append(WHERE_AND);
		}

		sb.append(_FINDER_COLUMN_G_C_S_COMPANYID_2);

		sb.append(_FINDER_COLUMN_G_C_S_STATUS_2);

		sb.setStringAt(
			removeConjunction(sb.stringAt(sb.index() - 1)), sb.index() - 1);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupIds);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, CommercePriceListImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, CommercePriceListImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(companyId);

			queryPos.add(status);

			return (List<CommercePriceList>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns all the commerce price lists where groupId = any &#63; and companyId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @return the matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_S(
		long[] groupIds, long companyId, int status) {

		return findByG_C_S(
			groupIds, companyId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the commerce price lists where groupId = any &#63; and companyId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_S(
		long[] groupIds, long companyId, int status, int start, int end) {

		return findByG_C_S(groupIds, companyId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where groupId = any &#63; and companyId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_S(
		long[] groupIds, long companyId, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		return findByG_C_S(
			groupIds, companyId, status, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where groupId = &#63; and companyId = &#63; and status = &#63;, optionally using the finder cache.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_S(
		long[] groupIds, long companyId, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean useFinderCache) {

		if (groupIds == null) {
			groupIds = new long[0];
		}
		else if (groupIds.length > 1) {
			groupIds = ArrayUtil.sortedUnique(groupIds);
		}

		if (groupIds.length == 1) {
			return findByG_C_S(
				groupIds[0], companyId, status, start, end, orderByComparator);
		}

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderArgs = new Object[] {
					StringUtil.merge(groupIds), companyId, status
				};
			}
		}
		else if (useFinderCache && productionMode) {
			finderArgs = new Object[] {
				StringUtil.merge(groupIds), companyId, status, start, end,
				orderByComparator
			};
		}

		List<CommercePriceList> list = null;

		if (useFinderCache && productionMode) {
			list = (List<CommercePriceList>)finderCache.getResult(
				_finderPathWithPaginationFindByG_C_S, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceList commercePriceList : list) {
					if (!ArrayUtil.contains(
							groupIds, commercePriceList.getGroupId()) ||
						(companyId != commercePriceList.getCompanyId()) ||
						(status != commercePriceList.getStatus())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = new StringBundler();

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			if (groupIds.length > 0) {
				sb.append("(");

				sb.append(_FINDER_COLUMN_G_C_S_GROUPID_7);

				sb.append(StringUtil.merge(groupIds));

				sb.append(")");

				sb.append(")");

				sb.append(WHERE_AND);
			}

			sb.append(_FINDER_COLUMN_G_C_S_COMPANYID_2);

			sb.append(_FINDER_COLUMN_G_C_S_STATUS_2);

			sb.setStringAt(
				removeConjunction(sb.stringAt(sb.index() - 1)), sb.index() - 1);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				queryPos.add(status);

				list = (List<CommercePriceList>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(
						_finderPathWithPaginationFindByG_C_S, finderArgs, list);
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
	 * Removes all the commerce price lists where groupId = &#63; and companyId = &#63; and status = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 */
	@Override
	public void removeByG_C_S(long groupId, long companyId, int status) {
		for (CommercePriceList commercePriceList :
				findByG_C_S(
					groupId, companyId, status, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(commercePriceList);
		}
	}

	/**
	 * Returns the number of commerce price lists where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByG_C_S(long groupId, long companyId, int status) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByG_C_S;

			finderArgs = new Object[] {groupId, companyId, status};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_G_C_S_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_C_S_COMPANYID_2);

			sb.append(_FINDER_COLUMN_G_C_S_STATUS_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(companyId);

				queryPos.add(status);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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

	/**
	 * Returns the number of commerce price lists where groupId = any &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByG_C_S(long[] groupIds, long companyId, int status) {
		if (groupIds == null) {
			groupIds = new long[0];
		}
		else if (groupIds.length > 1) {
			groupIds = ArrayUtil.sortedUnique(groupIds);
		}

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderArgs = new Object[] {
				StringUtil.merge(groupIds), companyId, status
			};

			count = (Long)finderCache.getResult(
				_finderPathWithPaginationCountByG_C_S, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler();

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			if (groupIds.length > 0) {
				sb.append("(");

				sb.append(_FINDER_COLUMN_G_C_S_GROUPID_7);

				sb.append(StringUtil.merge(groupIds));

				sb.append(")");

				sb.append(")");

				sb.append(WHERE_AND);
			}

			sb.append(_FINDER_COLUMN_G_C_S_COMPANYID_2);

			sb.append(_FINDER_COLUMN_G_C_S_STATUS_2);

			sb.setStringAt(
				removeConjunction(sb.stringAt(sb.index() - 1)), sb.index() - 1);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				queryPos.add(status);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(
						_finderPathWithPaginationCountByG_C_S, finderArgs,
						count);
				}
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

	/**
	 * Returns the number of commerce price lists that the user has permission to view where groupId = &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @return the number of matching commerce price lists that the user has permission to view
	 */
	@Override
	public int filterCountByG_C_S(long groupId, long companyId, int status) {
		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return countByG_C_S(groupId, companyId, status);
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_FILTER_SQL_COUNT_COMMERCEPRICELIST_WHERE);

		sb.append(_FINDER_COLUMN_G_C_S_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_C_S_COMPANYID_2);

		sb.append(_FINDER_COLUMN_G_C_S_STATUS_2);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			queryPos.add(companyId);

			queryPos.add(status);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the number of commerce price lists that the user has permission to view where groupId = any &#63; and companyId = &#63; and status = &#63;.
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @return the number of matching commerce price lists that the user has permission to view
	 */
	@Override
	public int filterCountByG_C_S(long[] groupIds, long companyId, int status) {
		if (!InlineSQLHelperUtil.isEnabled(groupIds)) {
			return countByG_C_S(groupIds, companyId, status);
		}

		if (groupIds == null) {
			groupIds = new long[0];
		}
		else if (groupIds.length > 1) {
			groupIds = ArrayUtil.sortedUnique(groupIds);
		}

		StringBundler sb = new StringBundler();

		sb.append(_FILTER_SQL_COUNT_COMMERCEPRICELIST_WHERE);

		if (groupIds.length > 0) {
			sb.append("(");

			sb.append(_FINDER_COLUMN_G_C_S_GROUPID_7);

			sb.append(StringUtil.merge(groupIds));

			sb.append(")");

			sb.append(")");

			sb.append(WHERE_AND);
		}

		sb.append(_FINDER_COLUMN_G_C_S_COMPANYID_2);

		sb.append(_FINDER_COLUMN_G_C_S_STATUS_2);

		sb.setStringAt(
			removeConjunction(sb.stringAt(sb.index() - 1)), sb.index() - 1);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupIds);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(companyId);

			queryPos.add(status);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String _FINDER_COLUMN_G_C_S_GROUPID_2 =
		"commercePriceList.groupId = ? AND ";

	private static final String _FINDER_COLUMN_G_C_S_GROUPID_7 =
		"commercePriceList.groupId IN (";

	private static final String _FINDER_COLUMN_G_C_S_COMPANYID_2 =
		"commercePriceList.companyId = ? AND ";

	private static final String _FINDER_COLUMN_G_C_S_STATUS_2 =
		"commercePriceList.status = ?";

	private FinderPath _finderPathWithPaginationFindByG_C_NotS;
	private FinderPath _finderPathWithPaginationCountByG_C_NotS;

	/**
	 * Returns all the commerce price lists where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @return the matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_NotS(
		long groupId, long companyId, int status) {

		return findByG_C_NotS(
			groupId, companyId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the commerce price lists where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_NotS(
		long groupId, long companyId, int status, int start, int end) {

		return findByG_C_NotS(groupId, companyId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_NotS(
		long groupId, long companyId, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		return findByG_C_NotS(
			groupId, companyId, status, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_NotS(
		long groupId, long companyId, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = _finderPathWithPaginationFindByG_C_NotS;
		finderArgs = new Object[] {
			groupId, companyId, status, start, end, orderByComparator
		};

		List<CommercePriceList> list = null;

		if (useFinderCache && productionMode) {
			list = (List<CommercePriceList>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceList commercePriceList : list) {
					if ((groupId != commercePriceList.getGroupId()) ||
						(companyId != commercePriceList.getCompanyId()) ||
						(status == commercePriceList.getStatus())) {

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
					5 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(5);
			}

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_G_C_NOTS_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_C_NOTS_COMPANYID_2);

			sb.append(_FINDER_COLUMN_G_C_NOTS_STATUS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(companyId);

				queryPos.add(status);

				list = (List<CommercePriceList>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
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
	 * Returns the first commerce price list in the ordered set where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByG_C_NotS_First(
			long groupId, long companyId, int status,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByG_C_NotS_First(
			groupId, companyId, status, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append(", status!=");
		sb.append(status);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the first commerce price list in the ordered set where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByG_C_NotS_First(
		long groupId, long companyId, int status,
		OrderByComparator<CommercePriceList> orderByComparator) {

		List<CommercePriceList> list = findByG_C_NotS(
			groupId, companyId, status, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price list in the ordered set where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByG_C_NotS_Last(
			long groupId, long companyId, int status,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByG_C_NotS_Last(
			groupId, companyId, status, orderByComparator);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append(", status!=");
		sb.append(status);

		sb.append("}");

		throw new NoSuchPriceListException(sb.toString());
	}

	/**
	 * Returns the last commerce price list in the ordered set where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByG_C_NotS_Last(
		long groupId, long companyId, int status,
		OrderByComparator<CommercePriceList> orderByComparator) {

		int count = countByG_C_NotS(groupId, companyId, status);

		if (count == 0) {
			return null;
		}

		List<CommercePriceList> list = findByG_C_NotS(
			groupId, companyId, status, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price lists before and after the current commerce price list in the ordered set where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param commercePriceListId the primary key of the current commerce price list
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList[] findByG_C_NotS_PrevAndNext(
			long commercePriceListId, long groupId, long companyId, int status,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = findByPrimaryKey(
			commercePriceListId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceList[] array = new CommercePriceListImpl[3];

			array[0] = getByG_C_NotS_PrevAndNext(
				session, commercePriceList, groupId, companyId, status,
				orderByComparator, true);

			array[1] = commercePriceList;

			array[2] = getByG_C_NotS_PrevAndNext(
				session, commercePriceList, groupId, companyId, status,
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

	protected CommercePriceList getByG_C_NotS_PrevAndNext(
		Session session, CommercePriceList commercePriceList, long groupId,
		long companyId, int status,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

		sb.append(_FINDER_COLUMN_G_C_NOTS_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_C_NOTS_COMPANYID_2);

		sb.append(_FINDER_COLUMN_G_C_NOTS_STATUS_2);

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
			sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		queryPos.add(companyId);

		queryPos.add(status);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceList)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceList> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the commerce price lists that the user has permission to view where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @return the matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C_NotS(
		long groupId, long companyId, int status) {

		return filterFindByG_C_NotS(
			groupId, companyId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the commerce price lists that the user has permission to view where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C_NotS(
		long groupId, long companyId, int status, int start, int end) {

		return filterFindByG_C_NotS(
			groupId, companyId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists that the user has permissions to view where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C_NotS(
		long groupId, long companyId, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByG_C_NotS(
				groupId, companyId, status, start, end, orderByComparator);
		}

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(6);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_COMMERCEPRICELIST_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_G_C_NOTS_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_C_NOTS_COMPANYID_2);

		sb.append(_FINDER_COLUMN_G_C_NOTS_STATUS_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, CommercePriceListImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, CommercePriceListImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			queryPos.add(companyId);

			queryPos.add(status);

			return (List<CommercePriceList>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the commerce price lists before and after the current commerce price list in the ordered set of commerce price lists that the user has permission to view where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param commercePriceListId the primary key of the current commerce price list
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList[] filterFindByG_C_NotS_PrevAndNext(
			long commercePriceListId, long groupId, long companyId, int status,
			OrderByComparator<CommercePriceList> orderByComparator)
		throws NoSuchPriceListException {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByG_C_NotS_PrevAndNext(
				commercePriceListId, groupId, companyId, status,
				orderByComparator);
		}

		CommercePriceList commercePriceList = findByPrimaryKey(
			commercePriceListId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceList[] array = new CommercePriceListImpl[3];

			array[0] = filterGetByG_C_NotS_PrevAndNext(
				session, commercePriceList, groupId, companyId, status,
				orderByComparator, true);

			array[1] = commercePriceList;

			array[2] = filterGetByG_C_NotS_PrevAndNext(
				session, commercePriceList, groupId, companyId, status,
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

	protected CommercePriceList filterGetByG_C_NotS_PrevAndNext(
		Session session, CommercePriceList commercePriceList, long groupId,
		long companyId, int status,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				7 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(6);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_COMMERCEPRICELIST_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_G_C_NOTS_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_C_NOTS_COMPANYID_2);

		sb.append(_FINDER_COLUMN_G_C_NOTS_STATUS_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByConditionFields[i],
							true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByConditionFields[i],
							true));
				}

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
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByFields[i], true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByFields[i], true));
				}

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
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(
				_FILTER_ENTITY_ALIAS, CommercePriceListImpl.class);
		}
		else {
			sqlQuery.addEntity(
				_FILTER_ENTITY_TABLE, CommercePriceListImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		queryPos.add(groupId);

		queryPos.add(companyId);

		queryPos.add(status);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceList)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceList> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the commerce price lists that the user has permission to view where groupId = any &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @return the matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C_NotS(
		long[] groupIds, long companyId, int status) {

		return filterFindByG_C_NotS(
			groupIds, companyId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the commerce price lists that the user has permission to view where groupId = any &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C_NotS(
		long[] groupIds, long companyId, int status, int start, int end) {

		return filterFindByG_C_NotS(
			groupIds, companyId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists that the user has permission to view where groupId = any &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists that the user has permission to view
	 */
	@Override
	public List<CommercePriceList> filterFindByG_C_NotS(
		long[] groupIds, long companyId, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(groupIds)) {
			return findByG_C_NotS(
				groupIds, companyId, status, start, end, orderByComparator);
		}

		if (groupIds == null) {
			groupIds = new long[0];
		}
		else if (groupIds.length > 1) {
			groupIds = ArrayUtil.sortedUnique(groupIds);
		}

		StringBundler sb = new StringBundler();

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_COMMERCEPRICELIST_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_1);
		}

		if (groupIds.length > 0) {
			sb.append("(");

			sb.append(_FINDER_COLUMN_G_C_NOTS_GROUPID_7);

			sb.append(StringUtil.merge(groupIds));

			sb.append(")");

			sb.append(")");

			sb.append(WHERE_AND);
		}

		sb.append(_FINDER_COLUMN_G_C_NOTS_COMPANYID_2);

		sb.append(_FINDER_COLUMN_G_C_NOTS_STATUS_2);

		sb.setStringAt(
			removeConjunction(sb.stringAt(sb.index() - 1)), sb.index() - 1);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupIds);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, CommercePriceListImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, CommercePriceListImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(companyId);

			queryPos.add(status);

			return (List<CommercePriceList>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns all the commerce price lists where groupId = any &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @return the matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_NotS(
		long[] groupIds, long companyId, int status) {

		return findByG_C_NotS(
			groupIds, companyId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the commerce price lists where groupId = any &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_NotS(
		long[] groupIds, long companyId, int status, int start, int end) {

		return findByG_C_NotS(groupIds, companyId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where groupId = any &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_NotS(
		long[] groupIds, long companyId, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		return findByG_C_NotS(
			groupIds, companyId, status, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price lists where groupId = &#63; and companyId = &#63; and status &ne; &#63;, optionally using the finder cache.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price lists
	 */
	@Override
	public List<CommercePriceList> findByG_C_NotS(
		long[] groupIds, long companyId, int status, int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean useFinderCache) {

		if (groupIds == null) {
			groupIds = new long[0];
		}
		else if (groupIds.length > 1) {
			groupIds = ArrayUtil.sortedUnique(groupIds);
		}

		if (groupIds.length == 1) {
			return findByG_C_NotS(
				groupIds[0], companyId, status, start, end, orderByComparator);
		}

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderArgs = new Object[] {
					StringUtil.merge(groupIds), companyId, status
				};
			}
		}
		else if (useFinderCache && productionMode) {
			finderArgs = new Object[] {
				StringUtil.merge(groupIds), companyId, status, start, end,
				orderByComparator
			};
		}

		List<CommercePriceList> list = null;

		if (useFinderCache && productionMode) {
			list = (List<CommercePriceList>)finderCache.getResult(
				_finderPathWithPaginationFindByG_C_NotS, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceList commercePriceList : list) {
					if (!ArrayUtil.contains(
							groupIds, commercePriceList.getGroupId()) ||
						(companyId != commercePriceList.getCompanyId()) ||
						(status == commercePriceList.getStatus())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = new StringBundler();

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			if (groupIds.length > 0) {
				sb.append("(");

				sb.append(_FINDER_COLUMN_G_C_NOTS_GROUPID_7);

				sb.append(StringUtil.merge(groupIds));

				sb.append(")");

				sb.append(")");

				sb.append(WHERE_AND);
			}

			sb.append(_FINDER_COLUMN_G_C_NOTS_COMPANYID_2);

			sb.append(_FINDER_COLUMN_G_C_NOTS_STATUS_2);

			sb.setStringAt(
				removeConjunction(sb.stringAt(sb.index() - 1)), sb.index() - 1);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				queryPos.add(status);

				list = (List<CommercePriceList>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(
						_finderPathWithPaginationFindByG_C_NotS, finderArgs,
						list);
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
	 * Removes all the commerce price lists where groupId = &#63; and companyId = &#63; and status &ne; &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 */
	@Override
	public void removeByG_C_NotS(long groupId, long companyId, int status) {
		for (CommercePriceList commercePriceList :
				findByG_C_NotS(
					groupId, companyId, status, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(commercePriceList);
		}
	}

	/**
	 * Returns the number of commerce price lists where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByG_C_NotS(long groupId, long companyId, int status) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathWithPaginationCountByG_C_NotS;

			finderArgs = new Object[] {groupId, companyId, status};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_G_C_NOTS_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_C_NOTS_COMPANYID_2);

			sb.append(_FINDER_COLUMN_G_C_NOTS_STATUS_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(companyId);

				queryPos.add(status);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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

	/**
	 * Returns the number of commerce price lists where groupId = any &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByG_C_NotS(long[] groupIds, long companyId, int status) {
		if (groupIds == null) {
			groupIds = new long[0];
		}
		else if (groupIds.length > 1) {
			groupIds = ArrayUtil.sortedUnique(groupIds);
		}

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderArgs = new Object[] {
				StringUtil.merge(groupIds), companyId, status
			};

			count = (Long)finderCache.getResult(
				_finderPathWithPaginationCountByG_C_NotS, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler();

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			if (groupIds.length > 0) {
				sb.append("(");

				sb.append(_FINDER_COLUMN_G_C_NOTS_GROUPID_7);

				sb.append(StringUtil.merge(groupIds));

				sb.append(")");

				sb.append(")");

				sb.append(WHERE_AND);
			}

			sb.append(_FINDER_COLUMN_G_C_NOTS_COMPANYID_2);

			sb.append(_FINDER_COLUMN_G_C_NOTS_STATUS_2);

			sb.setStringAt(
				removeConjunction(sb.stringAt(sb.index() - 1)), sb.index() - 1);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				queryPos.add(status);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(
						_finderPathWithPaginationCountByG_C_NotS, finderArgs,
						count);
				}
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

	/**
	 * Returns the number of commerce price lists that the user has permission to view where groupId = &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param groupId the group ID
	 * @param companyId the company ID
	 * @param status the status
	 * @return the number of matching commerce price lists that the user has permission to view
	 */
	@Override
	public int filterCountByG_C_NotS(long groupId, long companyId, int status) {
		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return countByG_C_NotS(groupId, companyId, status);
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_FILTER_SQL_COUNT_COMMERCEPRICELIST_WHERE);

		sb.append(_FINDER_COLUMN_G_C_NOTS_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_C_NOTS_COMPANYID_2);

		sb.append(_FINDER_COLUMN_G_C_NOTS_STATUS_2);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			queryPos.add(companyId);

			queryPos.add(status);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the number of commerce price lists that the user has permission to view where groupId = any &#63; and companyId = &#63; and status &ne; &#63;.
	 *
	 * @param groupIds the group IDs
	 * @param companyId the company ID
	 * @param status the status
	 * @return the number of matching commerce price lists that the user has permission to view
	 */
	@Override
	public int filterCountByG_C_NotS(
		long[] groupIds, long companyId, int status) {

		if (!InlineSQLHelperUtil.isEnabled(groupIds)) {
			return countByG_C_NotS(groupIds, companyId, status);
		}

		if (groupIds == null) {
			groupIds = new long[0];
		}
		else if (groupIds.length > 1) {
			groupIds = ArrayUtil.sortedUnique(groupIds);
		}

		StringBundler sb = new StringBundler();

		sb.append(_FILTER_SQL_COUNT_COMMERCEPRICELIST_WHERE);

		if (groupIds.length > 0) {
			sb.append("(");

			sb.append(_FINDER_COLUMN_G_C_NOTS_GROUPID_7);

			sb.append(StringUtil.merge(groupIds));

			sb.append(")");

			sb.append(")");

			sb.append(WHERE_AND);
		}

		sb.append(_FINDER_COLUMN_G_C_NOTS_COMPANYID_2);

		sb.append(_FINDER_COLUMN_G_C_NOTS_STATUS_2);

		sb.setStringAt(
			removeConjunction(sb.stringAt(sb.index() - 1)), sb.index() - 1);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), CommercePriceList.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupIds);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(companyId);

			queryPos.add(status);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String _FINDER_COLUMN_G_C_NOTS_GROUPID_2 =
		"commercePriceList.groupId = ? AND ";

	private static final String _FINDER_COLUMN_G_C_NOTS_GROUPID_7 =
		"commercePriceList.groupId IN (";

	private static final String _FINDER_COLUMN_G_C_NOTS_COMPANYID_2 =
		"commercePriceList.companyId = ? AND ";

	private static final String _FINDER_COLUMN_G_C_NOTS_STATUS_2 =
		"commercePriceList.status != ?";

	private FinderPath _finderPathFetchByG_C_T;
	private FinderPath _finderPathCountByG_C_T;

	/**
	 * Returns the commerce price list where groupId = &#63; and catalogBasePriceList = &#63; and type = &#63; or throws a <code>NoSuchPriceListException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param catalogBasePriceList the catalog base price list
	 * @param type the type
	 * @return the matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByG_C_T(
			long groupId, boolean catalogBasePriceList, String type)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByG_C_T(
			groupId, catalogBasePriceList, type);

		if (commercePriceList == null) {
			StringBundler sb = new StringBundler(8);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("groupId=");
			sb.append(groupId);

			sb.append(", catalogBasePriceList=");
			sb.append(catalogBasePriceList);

			sb.append(", type=");
			sb.append(type);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchPriceListException(sb.toString());
		}

		return commercePriceList;
	}

	/**
	 * Returns the commerce price list where groupId = &#63; and catalogBasePriceList = &#63; and type = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param catalogBasePriceList the catalog base price list
	 * @param type the type
	 * @return the matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByG_C_T(
		long groupId, boolean catalogBasePriceList, String type) {

		return fetchByG_C_T(groupId, catalogBasePriceList, type, true);
	}

	/**
	 * Returns the commerce price list where groupId = &#63; and catalogBasePriceList = &#63; and type = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param catalogBasePriceList the catalog base price list
	 * @param type the type
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByG_C_T(
		long groupId, boolean catalogBasePriceList, String type,
		boolean useFinderCache) {

		type = Objects.toString(type, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		Object[] finderArgs = null;

		if (useFinderCache && productionMode) {
			finderArgs = new Object[] {groupId, catalogBasePriceList, type};
		}

		Object result = null;

		if (useFinderCache && productionMode) {
			result = finderCache.getResult(_finderPathFetchByG_C_T, finderArgs);
		}

		if (result instanceof CommercePriceList) {
			CommercePriceList commercePriceList = (CommercePriceList)result;

			if ((groupId != commercePriceList.getGroupId()) ||
				(catalogBasePriceList !=
					commercePriceList.isCatalogBasePriceList()) ||
				!Objects.equals(type, commercePriceList.getType())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(5);

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_G_C_T_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_C_T_CATALOGBASEPRICELIST_2);

			boolean bindType = false;

			if (type.isEmpty()) {
				sb.append(_FINDER_COLUMN_G_C_T_TYPE_3);
			}
			else {
				bindType = true;

				sb.append(_FINDER_COLUMN_G_C_T_TYPE_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(catalogBasePriceList);

				if (bindType) {
					queryPos.add(type);
				}

				List<CommercePriceList> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache && productionMode) {
						finderCache.putResult(
							_finderPathFetchByG_C_T, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!productionMode || !useFinderCache) {
								finderArgs = new Object[] {
									groupId, catalogBasePriceList, type
								};
							}

							_log.warn(
								"CommercePriceListPersistenceImpl.fetchByG_C_T(long, boolean, String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					CommercePriceList commercePriceList = list.get(0);

					result = commercePriceList;

					cacheResult(commercePriceList);
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
			return (CommercePriceList)result;
		}
	}

	/**
	 * Removes the commerce price list where groupId = &#63; and catalogBasePriceList = &#63; and type = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param catalogBasePriceList the catalog base price list
	 * @param type the type
	 * @return the commerce price list that was removed
	 */
	@Override
	public CommercePriceList removeByG_C_T(
			long groupId, boolean catalogBasePriceList, String type)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = findByG_C_T(
			groupId, catalogBasePriceList, type);

		return remove(commercePriceList);
	}

	/**
	 * Returns the number of commerce price lists where groupId = &#63; and catalogBasePriceList = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param catalogBasePriceList the catalog base price list
	 * @param type the type
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByG_C_T(
		long groupId, boolean catalogBasePriceList, String type) {

		type = Objects.toString(type, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByG_C_T;

			finderArgs = new Object[] {groupId, catalogBasePriceList, type};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_G_C_T_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_C_T_CATALOGBASEPRICELIST_2);

			boolean bindType = false;

			if (type.isEmpty()) {
				sb.append(_FINDER_COLUMN_G_C_T_TYPE_3);
			}
			else {
				bindType = true;

				sb.append(_FINDER_COLUMN_G_C_T_TYPE_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(catalogBasePriceList);

				if (bindType) {
					queryPos.add(type);
				}

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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

	private static final String _FINDER_COLUMN_G_C_T_GROUPID_2 =
		"commercePriceList.groupId = ? AND ";

	private static final String _FINDER_COLUMN_G_C_T_CATALOGBASEPRICELIST_2 =
		"commercePriceList.catalogBasePriceList = ? AND ";

	private static final String _FINDER_COLUMN_G_C_T_TYPE_2 =
		"commercePriceList.type = ?";

	private static final String _FINDER_COLUMN_G_C_T_TYPE_3 =
		"(commercePriceList.type IS NULL OR commercePriceList.type = '')";

	private FinderPath _finderPathFetchByC_ERC;
	private FinderPath _finderPathCountByC_ERC;

	/**
	 * Returns the commerce price list where companyId = &#63; and externalReferenceCode = &#63; or throws a <code>NoSuchPriceListException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching commerce price list
	 * @throws NoSuchPriceListException if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList findByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByC_ERC(
			companyId, externalReferenceCode);

		if (commercePriceList == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("companyId=");
			sb.append(companyId);

			sb.append(", externalReferenceCode=");
			sb.append(externalReferenceCode);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchPriceListException(sb.toString());
		}

		return commercePriceList;
	}

	/**
	 * Returns the commerce price list where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByC_ERC(
		long companyId, String externalReferenceCode) {

		return fetchByC_ERC(companyId, externalReferenceCode, true);
	}

	/**
	 * Returns the commerce price list where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce price list, or <code>null</code> if a matching commerce price list could not be found
	 */
	@Override
	public CommercePriceList fetchByC_ERC(
		long companyId, String externalReferenceCode, boolean useFinderCache) {

		externalReferenceCode = Objects.toString(externalReferenceCode, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		Object[] finderArgs = null;

		if (useFinderCache && productionMode) {
			finderArgs = new Object[] {companyId, externalReferenceCode};
		}

		Object result = null;

		if (useFinderCache && productionMode) {
			result = finderCache.getResult(_finderPathFetchByC_ERC, finderArgs);
		}

		if (result instanceof CommercePriceList) {
			CommercePriceList commercePriceList = (CommercePriceList)result;

			if ((companyId != commercePriceList.getCompanyId()) ||
				!Objects.equals(
					externalReferenceCode,
					commercePriceList.getExternalReferenceCode())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_C_ERC_COMPANYID_2);

			boolean bindExternalReferenceCode = false;

			if (externalReferenceCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_3);
			}
			else {
				bindExternalReferenceCode = true;

				sb.append(_FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindExternalReferenceCode) {
					queryPos.add(externalReferenceCode);
				}

				List<CommercePriceList> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache && productionMode) {
						finderCache.putResult(
							_finderPathFetchByC_ERC, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!productionMode || !useFinderCache) {
								finderArgs = new Object[] {
									companyId, externalReferenceCode
								};
							}

							_log.warn(
								"CommercePriceListPersistenceImpl.fetchByC_ERC(long, String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					CommercePriceList commercePriceList = list.get(0);

					result = commercePriceList;

					cacheResult(commercePriceList);
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
			return (CommercePriceList)result;
		}
	}

	/**
	 * Removes the commerce price list where companyId = &#63; and externalReferenceCode = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the commerce price list that was removed
	 */
	@Override
	public CommercePriceList removeByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = findByC_ERC(
			companyId, externalReferenceCode);

		return remove(commercePriceList);
	}

	/**
	 * Returns the number of commerce price lists where companyId = &#63; and externalReferenceCode = &#63;.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the number of matching commerce price lists
	 */
	@Override
	public int countByC_ERC(long companyId, String externalReferenceCode) {
		externalReferenceCode = Objects.toString(externalReferenceCode, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByC_ERC;

			finderArgs = new Object[] {companyId, externalReferenceCode};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMERCEPRICELIST_WHERE);

			sb.append(_FINDER_COLUMN_C_ERC_COMPANYID_2);

			boolean bindExternalReferenceCode = false;

			if (externalReferenceCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_3);
			}
			else {
				bindExternalReferenceCode = true;

				sb.append(_FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindExternalReferenceCode) {
					queryPos.add(externalReferenceCode);
				}

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
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

	private static final String _FINDER_COLUMN_C_ERC_COMPANYID_2 =
		"commercePriceList.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_2 =
		"commercePriceList.externalReferenceCode = ?";

	private static final String _FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_3 =
		"(commercePriceList.externalReferenceCode IS NULL OR commercePriceList.externalReferenceCode = '')";

	public CommercePriceListPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");
		dbColumnNames.put("type", "type_");

		setDBColumnNames(dbColumnNames);

		setModelClass(CommercePriceList.class);

		setModelImplClass(CommercePriceListImpl.class);
		setModelPKClass(long.class);

		setTable(CommercePriceListTable.INSTANCE);
	}

	/**
	 * Caches the commerce price list in the entity cache if it is enabled.
	 *
	 * @param commercePriceList the commerce price list
	 */
	@Override
	public void cacheResult(CommercePriceList commercePriceList) {
		if (commercePriceList.getCtCollectionId() != 0) {
			return;
		}

		entityCache.putResult(
			CommercePriceListImpl.class, commercePriceList.getPrimaryKey(),
			commercePriceList);

		finderCache.putResult(
			_finderPathFetchByUUID_G,
			new Object[] {
				commercePriceList.getUuid(), commercePriceList.getGroupId()
			},
			commercePriceList);

		finderCache.putResult(
			_finderPathFetchByParentCommercePriceListId,
			new Object[] {commercePriceList.getParentCommercePriceListId()},
			commercePriceList);

		finderCache.putResult(
			_finderPathFetchByG_CatalogBasePriceList,
			new Object[] {
				commercePriceList.getGroupId(),
				commercePriceList.isCatalogBasePriceList()
			},
			commercePriceList);

		finderCache.putResult(
			_finderPathFetchByG_C_T,
			new Object[] {
				commercePriceList.getGroupId(),
				commercePriceList.isCatalogBasePriceList(),
				commercePriceList.getType()
			},
			commercePriceList);

		finderCache.putResult(
			_finderPathFetchByC_ERC,
			new Object[] {
				commercePriceList.getCompanyId(),
				commercePriceList.getExternalReferenceCode()
			},
			commercePriceList);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the commerce price lists in the entity cache if it is enabled.
	 *
	 * @param commercePriceLists the commerce price lists
	 */
	@Override
	public void cacheResult(List<CommercePriceList> commercePriceLists) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (commercePriceLists.size() >
				 _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (CommercePriceList commercePriceList : commercePriceLists) {
			if (commercePriceList.getCtCollectionId() != 0) {
				continue;
			}

			if (entityCache.getResult(
					CommercePriceListImpl.class,
					commercePriceList.getPrimaryKey()) == null) {

				cacheResult(commercePriceList);
			}
		}
	}

	/**
	 * Clears the cache for all commerce price lists.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(CommercePriceListImpl.class);

		finderCache.clearCache(CommercePriceListImpl.class);
	}

	/**
	 * Clears the cache for the commerce price list.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(CommercePriceList commercePriceList) {
		entityCache.removeResult(
			CommercePriceListImpl.class, commercePriceList);
	}

	@Override
	public void clearCache(List<CommercePriceList> commercePriceLists) {
		for (CommercePriceList commercePriceList : commercePriceLists) {
			entityCache.removeResult(
				CommercePriceListImpl.class, commercePriceList);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(CommercePriceListImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(CommercePriceListImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		CommercePriceListModelImpl commercePriceListModelImpl) {

		Object[] args = new Object[] {
			commercePriceListModelImpl.getUuid(),
			commercePriceListModelImpl.getGroupId()
		};

		finderCache.putResult(_finderPathCountByUUID_G, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByUUID_G, args, commercePriceListModelImpl);

		args = new Object[] {
			commercePriceListModelImpl.getParentCommercePriceListId()
		};

		finderCache.putResult(
			_finderPathCountByParentCommercePriceListId, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByParentCommercePriceListId, args,
			commercePriceListModelImpl);

		args = new Object[] {
			commercePriceListModelImpl.getGroupId(),
			commercePriceListModelImpl.isCatalogBasePriceList()
		};

		finderCache.putResult(
			_finderPathCountByG_CatalogBasePriceList, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByG_CatalogBasePriceList, args,
			commercePriceListModelImpl);

		args = new Object[] {
			commercePriceListModelImpl.getGroupId(),
			commercePriceListModelImpl.isCatalogBasePriceList(),
			commercePriceListModelImpl.getType()
		};

		finderCache.putResult(_finderPathCountByG_C_T, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByG_C_T, args, commercePriceListModelImpl);

		args = new Object[] {
			commercePriceListModelImpl.getCompanyId(),
			commercePriceListModelImpl.getExternalReferenceCode()
		};

		finderCache.putResult(_finderPathCountByC_ERC, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByC_ERC, args, commercePriceListModelImpl);
	}

	/**
	 * Creates a new commerce price list with the primary key. Does not add the commerce price list to the database.
	 *
	 * @param commercePriceListId the primary key for the new commerce price list
	 * @return the new commerce price list
	 */
	@Override
	public CommercePriceList create(long commercePriceListId) {
		CommercePriceList commercePriceList = new CommercePriceListImpl();

		commercePriceList.setNew(true);
		commercePriceList.setPrimaryKey(commercePriceListId);

		String uuid = _portalUUID.generate();

		commercePriceList.setUuid(uuid);

		commercePriceList.setCompanyId(CompanyThreadLocal.getCompanyId());

		return commercePriceList;
	}

	/**
	 * Removes the commerce price list with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePriceListId the primary key of the commerce price list
	 * @return the commerce price list that was removed
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList remove(long commercePriceListId)
		throws NoSuchPriceListException {

		return remove((Serializable)commercePriceListId);
	}

	/**
	 * Removes the commerce price list with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the commerce price list
	 * @return the commerce price list that was removed
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList remove(Serializable primaryKey)
		throws NoSuchPriceListException {

		Session session = null;

		try {
			session = openSession();

			CommercePriceList commercePriceList =
				(CommercePriceList)session.get(
					CommercePriceListImpl.class, primaryKey);

			if (commercePriceList == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchPriceListException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(commercePriceList);
		}
		catch (NoSuchPriceListException noSuchEntityException) {
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
	protected CommercePriceList removeImpl(
		CommercePriceList commercePriceList) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(commercePriceList)) {
				commercePriceList = (CommercePriceList)session.get(
					CommercePriceListImpl.class,
					commercePriceList.getPrimaryKeyObj());
			}

			if ((commercePriceList != null) &&
				ctPersistenceHelper.isRemove(commercePriceList)) {

				session.delete(commercePriceList);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (commercePriceList != null) {
			clearCache(commercePriceList);
		}

		return commercePriceList;
	}

	@Override
	public CommercePriceList updateImpl(CommercePriceList commercePriceList) {
		boolean isNew = commercePriceList.isNew();

		if (!(commercePriceList instanceof CommercePriceListModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(commercePriceList.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					commercePriceList);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in commercePriceList proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom CommercePriceList implementation " +
					commercePriceList.getClass());
		}

		CommercePriceListModelImpl commercePriceListModelImpl =
			(CommercePriceListModelImpl)commercePriceList;

		if (Validator.isNull(commercePriceList.getUuid())) {
			String uuid = _portalUUID.generate();

			commercePriceList.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (commercePriceList.getCreateDate() == null)) {
			if (serviceContext == null) {
				commercePriceList.setCreateDate(date);
			}
			else {
				commercePriceList.setCreateDate(
					serviceContext.getCreateDate(date));
			}
		}

		if (!commercePriceListModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				commercePriceList.setModifiedDate(date);
			}
			else {
				commercePriceList.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (ctPersistenceHelper.isInsert(commercePriceList)) {
				if (!isNew) {
					session.evict(
						CommercePriceListImpl.class,
						commercePriceList.getPrimaryKeyObj());
				}

				session.save(commercePriceList);
			}
			else {
				commercePriceList = (CommercePriceList)session.merge(
					commercePriceList);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (commercePriceList.getCtCollectionId() != 0) {
			if (isNew) {
				commercePriceList.setNew(false);
			}

			commercePriceList.resetOriginalValues();

			return commercePriceList;
		}

		entityCache.putResult(
			CommercePriceListImpl.class, commercePriceListModelImpl, false,
			true);

		cacheUniqueFindersCache(commercePriceListModelImpl);

		if (isNew) {
			commercePriceList.setNew(false);
		}

		commercePriceList.resetOriginalValues();

		return commercePriceList;
	}

	/**
	 * Returns the commerce price list with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce price list
	 * @return the commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList findByPrimaryKey(Serializable primaryKey)
		throws NoSuchPriceListException {

		CommercePriceList commercePriceList = fetchByPrimaryKey(primaryKey);

		if (commercePriceList == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchPriceListException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return commercePriceList;
	}

	/**
	 * Returns the commerce price list with the primary key or throws a <code>NoSuchPriceListException</code> if it could not be found.
	 *
	 * @param commercePriceListId the primary key of the commerce price list
	 * @return the commerce price list
	 * @throws NoSuchPriceListException if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList findByPrimaryKey(long commercePriceListId)
		throws NoSuchPriceListException {

		return findByPrimaryKey((Serializable)commercePriceListId);
	}

	/**
	 * Returns the commerce price list with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce price list
	 * @return the commerce price list, or <code>null</code> if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList fetchByPrimaryKey(Serializable primaryKey) {
		if (ctPersistenceHelper.isProductionMode(CommercePriceList.class)) {
			return super.fetchByPrimaryKey(primaryKey);
		}

		CommercePriceList commercePriceList = null;

		Session session = null;

		try {
			session = openSession();

			commercePriceList = (CommercePriceList)session.get(
				CommercePriceListImpl.class, primaryKey);

			if (commercePriceList != null) {
				cacheResult(commercePriceList);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return commercePriceList;
	}

	/**
	 * Returns the commerce price list with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commercePriceListId the primary key of the commerce price list
	 * @return the commerce price list, or <code>null</code> if a commerce price list with the primary key could not be found
	 */
	@Override
	public CommercePriceList fetchByPrimaryKey(long commercePriceListId) {
		return fetchByPrimaryKey((Serializable)commercePriceListId);
	}

	@Override
	public Map<Serializable, CommercePriceList> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		if (ctPersistenceHelper.isProductionMode(CommercePriceList.class)) {
			return super.fetchByPrimaryKeys(primaryKeys);
		}

		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, CommercePriceList> map =
			new HashMap<Serializable, CommercePriceList>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			CommercePriceList commercePriceList = fetchByPrimaryKey(primaryKey);

			if (commercePriceList != null) {
				map.put(primaryKey, commercePriceList);
			}

			return map;
		}

		if ((databaseInMaxParameters > 0) &&
			(primaryKeys.size() > databaseInMaxParameters)) {

			Iterator<Serializable> iterator = primaryKeys.iterator();

			while (iterator.hasNext()) {
				Set<Serializable> page = new HashSet<>();

				for (int i = 0;
					 (i < databaseInMaxParameters) && iterator.hasNext(); i++) {

					page.add(iterator.next());
				}

				map.putAll(fetchByPrimaryKeys(page));
			}

			return map;
		}

		StringBundler sb = new StringBundler((primaryKeys.size() * 2) + 1);

		sb.append(getSelectSQL());
		sb.append(" WHERE ");
		sb.append(getPKDBName());
		sb.append(" IN (");

		for (Serializable primaryKey : primaryKeys) {
			sb.append((long)primaryKey);

			sb.append(",");
		}

		sb.setIndex(sb.index() - 1);

		sb.append(")");

		String sql = sb.toString();

		Session session = null;

		try {
			session = openSession();

			Query query = session.createQuery(sql);

			for (CommercePriceList commercePriceList :
					(List<CommercePriceList>)query.list()) {

				map.put(
					commercePriceList.getPrimaryKeyObj(), commercePriceList);

				cacheResult(commercePriceList);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the commerce price lists.
	 *
	 * @return the commerce price lists
	 */
	@Override
	public List<CommercePriceList> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price lists.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @return the range of commerce price lists
	 */
	@Override
	public List<CommercePriceList> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price lists.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of commerce price lists
	 */
	@Override
	public List<CommercePriceList> findAll(
		int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price lists.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce price lists
	 * @param end the upper bound of the range of commerce price lists (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of commerce price lists
	 */
	@Override
	public List<CommercePriceList> findAll(
		int start, int end,
		OrderByComparator<CommercePriceList> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<CommercePriceList> list = null;

		if (useFinderCache && productionMode) {
			list = (List<CommercePriceList>)finderCache.getResult(
				finderPath, finderArgs);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_COMMERCEPRICELIST);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_COMMERCEPRICELIST;

				sql = sql.concat(CommercePriceListModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<CommercePriceList>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
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
	 * Removes all the commerce price lists from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (CommercePriceList commercePriceList : findAll()) {
			remove(commercePriceList);
		}
	}

	/**
	 * Returns the number of commerce price lists.
	 *
	 * @return the number of commerce price lists
	 */
	@Override
	public int countAll() {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			CommercePriceList.class);

		Long count = null;

		if (productionMode) {
			count = (Long)finderCache.getResult(
				_finderPathCountAll, FINDER_ARGS_EMPTY);
		}

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_COMMERCEPRICELIST);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(
						_finderPathCountAll, FINDER_ARGS_EMPTY, count);
				}
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
		return "commercePriceListId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_COMMERCEPRICELIST;
	}

	@Override
	public Set<String> getCTColumnNames(
		CTColumnResolutionType ctColumnResolutionType) {

		return _ctColumnNamesMap.getOrDefault(
			ctColumnResolutionType, Collections.emptySet());
	}

	@Override
	public List<String> getMappingTableNames() {
		return _mappingTableNames;
	}

	@Override
	public Map<String, Integer> getTableColumnsMap() {
		return CommercePriceListModelImpl.TABLE_COLUMNS_MAP;
	}

	@Override
	public String getTableName() {
		return "CommercePriceList";
	}

	@Override
	public List<String[]> getUniqueIndexColumnNames() {
		return _uniqueIndexColumnNames;
	}

	private static final Map<CTColumnResolutionType, Set<String>>
		_ctColumnNamesMap = new EnumMap<CTColumnResolutionType, Set<String>>(
			CTColumnResolutionType.class);
	private static final List<String> _mappingTableNames =
		new ArrayList<String>();
	private static final List<String[]> _uniqueIndexColumnNames =
		new ArrayList<String[]>();

	static {
		Set<String> ctControlColumnNames = new HashSet<String>();
		Set<String> ctIgnoreColumnNames = new HashSet<String>();
		Set<String> ctStrictColumnNames = new HashSet<String>();

		ctControlColumnNames.add("mvccVersion");
		ctControlColumnNames.add("ctCollectionId");
		ctStrictColumnNames.add("uuid_");
		ctStrictColumnNames.add("externalReferenceCode");
		ctStrictColumnNames.add("groupId");
		ctStrictColumnNames.add("companyId");
		ctStrictColumnNames.add("userId");
		ctStrictColumnNames.add("userName");
		ctStrictColumnNames.add("createDate");
		ctIgnoreColumnNames.add("modifiedDate");
		ctStrictColumnNames.add("commerceCurrencyId");
		ctStrictColumnNames.add("parentCommercePriceListId");
		ctStrictColumnNames.add("catalogBasePriceList");
		ctStrictColumnNames.add("netPrice");
		ctStrictColumnNames.add("type_");
		ctStrictColumnNames.add("name");
		ctStrictColumnNames.add("priority");
		ctStrictColumnNames.add("displayDate");
		ctStrictColumnNames.add("expirationDate");
		ctStrictColumnNames.add("lastPublishDate");
		ctStrictColumnNames.add("status");
		ctStrictColumnNames.add("statusByUserId");
		ctStrictColumnNames.add("statusByUserName");
		ctStrictColumnNames.add("statusDate");

		_ctColumnNamesMap.put(
			CTColumnResolutionType.CONTROL, ctControlColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.IGNORE, ctIgnoreColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.PK,
			Collections.singleton("commercePriceListId"));
		_ctColumnNamesMap.put(
			CTColumnResolutionType.STRICT, ctStrictColumnNames);

		_uniqueIndexColumnNames.add(new String[] {"uuid_", "groupId"});
	}

	/**
	 * Initializes the commerce price list persistence.
	 */
	public void afterPropertiesSet() {
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

		_finderPathFetchByUUID_G = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByUUID_G",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "groupId"}, true);

		_finderPathCountByUUID_G = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUUID_G",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "groupId"}, false);

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

		_finderPathWithPaginationFindByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCompanyId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"companyId"}, true);

		_finderPathWithoutPaginationFindByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCompanyId",
			new String[] {Long.class.getName()}, new String[] {"companyId"},
			true);

		_finderPathCountByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCompanyId",
			new String[] {Long.class.getName()}, new String[] {"companyId"},
			false);

		_finderPathWithPaginationFindByCommerceCurrencyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCommerceCurrencyId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"commerceCurrencyId"}, true);

		_finderPathWithoutPaginationFindByCommerceCurrencyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByCommerceCurrencyId", new String[] {Long.class.getName()},
			new String[] {"commerceCurrencyId"}, true);

		_finderPathCountByCommerceCurrencyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByCommerceCurrencyId", new String[] {Long.class.getName()},
			new String[] {"commerceCurrencyId"}, false);

		_finderPathFetchByParentCommercePriceListId = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByParentCommercePriceListId",
			new String[] {Long.class.getName()},
			new String[] {"parentCommercePriceListId"}, true);

		_finderPathCountByParentCommercePriceListId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByParentCommercePriceListId",
			new String[] {Long.class.getName()},
			new String[] {"parentCommercePriceListId"}, false);

		_finderPathWithPaginationFindByG_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_C",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"groupId", "companyId"}, true);

		_finderPathWithoutPaginationFindByG_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_C",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"groupId", "companyId"}, true);

		_finderPathCountByG_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_C",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"groupId", "companyId"}, false);

		_finderPathWithPaginationCountByG_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByG_C",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"groupId", "companyId"}, false);

		_finderPathFetchByG_CatalogBasePriceList = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByG_CatalogBasePriceList",
			new String[] {Long.class.getName(), Boolean.class.getName()},
			new String[] {"groupId", "catalogBasePriceList"}, true);

		_finderPathCountByG_CatalogBasePriceList = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByG_CatalogBasePriceList",
			new String[] {Long.class.getName(), Boolean.class.getName()},
			new String[] {"groupId", "catalogBasePriceList"}, false);

		_finderPathWithPaginationFindByLtD_S = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByLtD_S",
			new String[] {
				Date.class.getName(), Integer.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"displayDate", "status"}, true);

		_finderPathWithPaginationCountByLtD_S = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByLtD_S",
			new String[] {Date.class.getName(), Integer.class.getName()},
			new String[] {"displayDate", "status"}, false);

		_finderPathWithPaginationFindByG_C_S = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_C_S",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"groupId", "companyId", "status"}, true);

		_finderPathWithoutPaginationFindByG_C_S = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_C_S",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName()
			},
			new String[] {"groupId", "companyId", "status"}, true);

		_finderPathCountByG_C_S = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_C_S",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName()
			},
			new String[] {"groupId", "companyId", "status"}, false);

		_finderPathWithPaginationCountByG_C_S = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByG_C_S",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName()
			},
			new String[] {"groupId", "companyId", "status"}, false);

		_finderPathWithPaginationFindByG_C_NotS = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_C_NotS",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"groupId", "companyId", "status"}, true);

		_finderPathWithPaginationCountByG_C_NotS = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByG_C_NotS",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName()
			},
			new String[] {"groupId", "companyId", "status"}, false);

		_finderPathFetchByG_C_T = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByG_C_T",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				String.class.getName()
			},
			new String[] {"groupId", "catalogBasePriceList", "type_"}, true);

		_finderPathCountByG_C_T = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_C_T",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				String.class.getName()
			},
			new String[] {"groupId", "catalogBasePriceList", "type_"}, false);

		_finderPathFetchByC_ERC = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByC_ERC",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"companyId", "externalReferenceCode"}, true);

		_finderPathCountByC_ERC = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_ERC",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"companyId", "externalReferenceCode"}, false);

		_setCommercePriceListUtilPersistence(this);
	}

	public void destroy() {
		_setCommercePriceListUtilPersistence(null);

		entityCache.removeCache(CommercePriceListImpl.class.getName());
	}

	private void _setCommercePriceListUtilPersistence(
		CommercePriceListPersistence commercePriceListPersistence) {

		try {
			Field field = CommercePriceListUtil.class.getDeclaredField(
				"_persistence");

			field.setAccessible(true);

			field.set(null, commercePriceListPersistence);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@ServiceReference(type = CTPersistenceHelper.class)
	protected CTPersistenceHelper ctPersistenceHelper;

	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;

	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;

	private static Long _getTime(Date date) {
		if (date == null) {
			return null;
		}

		return date.getTime();
	}

	private static final String _SQL_SELECT_COMMERCEPRICELIST =
		"SELECT commercePriceList FROM CommercePriceList commercePriceList";

	private static final String _SQL_SELECT_COMMERCEPRICELIST_WHERE =
		"SELECT commercePriceList FROM CommercePriceList commercePriceList WHERE ";

	private static final String _SQL_COUNT_COMMERCEPRICELIST =
		"SELECT COUNT(commercePriceList) FROM CommercePriceList commercePriceList";

	private static final String _SQL_COUNT_COMMERCEPRICELIST_WHERE =
		"SELECT COUNT(commercePriceList) FROM CommercePriceList commercePriceList WHERE ";

	private static final String _FILTER_ENTITY_TABLE_FILTER_PK_COLUMN =
		"commercePriceList.commercePriceListId";

	private static final String _FILTER_SQL_SELECT_COMMERCEPRICELIST_WHERE =
		"SELECT DISTINCT {commercePriceList.*} FROM CommercePriceList commercePriceList WHERE ";

	private static final String
		_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_1 =
			"SELECT {CommercePriceList.*} FROM (SELECT DISTINCT commercePriceList.commercePriceListId FROM CommercePriceList commercePriceList WHERE ";

	private static final String
		_FILTER_SQL_SELECT_COMMERCEPRICELIST_NO_INLINE_DISTINCT_WHERE_2 =
			") TEMP_TABLE INNER JOIN CommercePriceList ON TEMP_TABLE.commercePriceListId = CommercePriceList.commercePriceListId";

	private static final String _FILTER_SQL_COUNT_COMMERCEPRICELIST_WHERE =
		"SELECT COUNT(DISTINCT commercePriceList.commercePriceListId) AS COUNT_VALUE FROM CommercePriceList commercePriceList WHERE ";

	private static final String _FILTER_ENTITY_ALIAS = "commercePriceList";

	private static final String _FILTER_ENTITY_TABLE = "CommercePriceList";

	private static final String _ORDER_BY_ENTITY_ALIAS = "commercePriceList.";

	private static final String _ORDER_BY_ENTITY_TABLE = "CommercePriceList.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No CommercePriceList exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No CommercePriceList exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		CommercePriceListPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid", "type"});

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

	@ServiceReference(type = PortalUUID.class)
	private PortalUUID _portalUUID;

}