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

package com.liferay.commerce.service.persistence.impl;

import com.liferay.commerce.exception.NoSuchShipmentItemException;
import com.liferay.commerce.model.CommerceShipmentItem;
import com.liferay.commerce.model.CommerceShipmentItemTable;
import com.liferay.commerce.model.impl.CommerceShipmentItemImpl;
import com.liferay.commerce.model.impl.CommerceShipmentItemModelImpl;
import com.liferay.commerce.service.persistence.CommerceShipmentItemPersistence;
import com.liferay.commerce.service.persistence.CommerceShipmentItemUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The persistence implementation for the commerce shipment item service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @generated
 */
public class CommerceShipmentItemPersistenceImpl
	extends BasePersistenceImpl<CommerceShipmentItem>
	implements CommerceShipmentItemPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>CommerceShipmentItemUtil</code> to access the commerce shipment item persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		CommerceShipmentItemImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByGroupId;
	private FinderPath _finderPathWithoutPaginationFindByGroupId;
	private FinderPath _finderPathCountByGroupId;

	/**
	 * Returns all the commerce shipment items where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByGroupId(long groupId) {
		return findByGroupId(
			groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce shipment items where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @return the range of matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByGroupId(
		long groupId, int start, int end) {

		return findByGroupId(groupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce shipment items where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		return findByGroupId(groupId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce shipment items where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<CommerceShipmentItem> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByGroupId;
				finderArgs = new Object[] {groupId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByGroupId;
			finderArgs = new Object[] {groupId, start, end, orderByComparator};
		}

		List<CommerceShipmentItem> list = null;

		if (useFinderCache) {
			list = (List<CommerceShipmentItem>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommerceShipmentItem commerceShipmentItem : list) {
					if (groupId != commerceShipmentItem.getGroupId()) {
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

			sb.append(_SQL_SELECT_COMMERCESHIPMENTITEM_WHERE);

			sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommerceShipmentItemModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				list = (List<CommerceShipmentItem>)QueryUtil.list(
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
	 * Returns the first commerce shipment item in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce shipment item
	 * @throws NoSuchShipmentItemException if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem findByGroupId_First(
			long groupId,
			OrderByComparator<CommerceShipmentItem> orderByComparator)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = fetchByGroupId_First(
			groupId, orderByComparator);

		if (commerceShipmentItem != null) {
			return commerceShipmentItem;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append("}");

		throw new NoSuchShipmentItemException(sb.toString());
	}

	/**
	 * Returns the first commerce shipment item in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce shipment item, or <code>null</code> if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByGroupId_First(
		long groupId,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		List<CommerceShipmentItem> list = findByGroupId(
			groupId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce shipment item in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce shipment item
	 * @throws NoSuchShipmentItemException if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem findByGroupId_Last(
			long groupId,
			OrderByComparator<CommerceShipmentItem> orderByComparator)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = fetchByGroupId_Last(
			groupId, orderByComparator);

		if (commerceShipmentItem != null) {
			return commerceShipmentItem;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append("}");

		throw new NoSuchShipmentItemException(sb.toString());
	}

	/**
	 * Returns the last commerce shipment item in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce shipment item, or <code>null</code> if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByGroupId_Last(
		long groupId,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		int count = countByGroupId(groupId);

		if (count == 0) {
			return null;
		}

		List<CommerceShipmentItem> list = findByGroupId(
			groupId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce shipment items before and after the current commerce shipment item in the ordered set where groupId = &#63;.
	 *
	 * @param commerceShipmentItemId the primary key of the current commerce shipment item
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce shipment item
	 * @throws NoSuchShipmentItemException if a commerce shipment item with the primary key could not be found
	 */
	@Override
	public CommerceShipmentItem[] findByGroupId_PrevAndNext(
			long commerceShipmentItemId, long groupId,
			OrderByComparator<CommerceShipmentItem> orderByComparator)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = findByPrimaryKey(
			commerceShipmentItemId);

		Session session = null;

		try {
			session = openSession();

			CommerceShipmentItem[] array = new CommerceShipmentItemImpl[3];

			array[0] = getByGroupId_PrevAndNext(
				session, commerceShipmentItem, groupId, orderByComparator,
				true);

			array[1] = commerceShipmentItem;

			array[2] = getByGroupId_PrevAndNext(
				session, commerceShipmentItem, groupId, orderByComparator,
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

	protected CommerceShipmentItem getByGroupId_PrevAndNext(
		Session session, CommerceShipmentItem commerceShipmentItem,
		long groupId, OrderByComparator<CommerceShipmentItem> orderByComparator,
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

		sb.append(_SQL_SELECT_COMMERCESHIPMENTITEM_WHERE);

		sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

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
			sb.append(CommerceShipmentItemModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commerceShipmentItem)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommerceShipmentItem> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce shipment items where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	@Override
	public void removeByGroupId(long groupId) {
		for (CommerceShipmentItem commerceShipmentItem :
				findByGroupId(
					groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(commerceShipmentItem);
		}
	}

	/**
	 * Returns the number of commerce shipment items where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching commerce shipment items
	 */
	@Override
	public int countByGroupId(long groupId) {
		FinderPath finderPath = _finderPathCountByGroupId;

		Object[] finderArgs = new Object[] {groupId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMERCESHIPMENTITEM_WHERE);

			sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

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

	private static final String _FINDER_COLUMN_GROUPID_GROUPID_2 =
		"commerceShipmentItem.groupId = ?";

	private FinderPath _finderPathWithPaginationFindByCommerceShipmentId;
	private FinderPath _finderPathWithoutPaginationFindByCommerceShipmentId;
	private FinderPath _finderPathCountByCommerceShipmentId;

	/**
	 * Returns all the commerce shipment items where commerceShipmentId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @return the matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByCommerceShipmentId(
		long commerceShipmentId) {

		return findByCommerceShipmentId(
			commerceShipmentId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce shipment items where commerceShipmentId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @return the range of matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByCommerceShipmentId(
		long commerceShipmentId, int start, int end) {

		return findByCommerceShipmentId(commerceShipmentId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce shipment items where commerceShipmentId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByCommerceShipmentId(
		long commerceShipmentId, int start, int end,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		return findByCommerceShipmentId(
			commerceShipmentId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce shipment items where commerceShipmentId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByCommerceShipmentId(
		long commerceShipmentId, int start, int end,
		OrderByComparator<CommerceShipmentItem> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByCommerceShipmentId;
				finderArgs = new Object[] {commerceShipmentId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCommerceShipmentId;
			finderArgs = new Object[] {
				commerceShipmentId, start, end, orderByComparator
			};
		}

		List<CommerceShipmentItem> list = null;

		if (useFinderCache) {
			list = (List<CommerceShipmentItem>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommerceShipmentItem commerceShipmentItem : list) {
					if (commerceShipmentId !=
							commerceShipmentItem.getCommerceShipmentId()) {

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

			sb.append(_SQL_SELECT_COMMERCESHIPMENTITEM_WHERE);

			sb.append(_FINDER_COLUMN_COMMERCESHIPMENTID_COMMERCESHIPMENTID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommerceShipmentItemModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commerceShipmentId);

				list = (List<CommerceShipmentItem>)QueryUtil.list(
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
	 * Returns the first commerce shipment item in the ordered set where commerceShipmentId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce shipment item
	 * @throws NoSuchShipmentItemException if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem findByCommerceShipmentId_First(
			long commerceShipmentId,
			OrderByComparator<CommerceShipmentItem> orderByComparator)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem =
			fetchByCommerceShipmentId_First(
				commerceShipmentId, orderByComparator);

		if (commerceShipmentItem != null) {
			return commerceShipmentItem;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("commerceShipmentId=");
		sb.append(commerceShipmentId);

		sb.append("}");

		throw new NoSuchShipmentItemException(sb.toString());
	}

	/**
	 * Returns the first commerce shipment item in the ordered set where commerceShipmentId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce shipment item, or <code>null</code> if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByCommerceShipmentId_First(
		long commerceShipmentId,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		List<CommerceShipmentItem> list = findByCommerceShipmentId(
			commerceShipmentId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce shipment item in the ordered set where commerceShipmentId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce shipment item
	 * @throws NoSuchShipmentItemException if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem findByCommerceShipmentId_Last(
			long commerceShipmentId,
			OrderByComparator<CommerceShipmentItem> orderByComparator)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem =
			fetchByCommerceShipmentId_Last(
				commerceShipmentId, orderByComparator);

		if (commerceShipmentItem != null) {
			return commerceShipmentItem;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("commerceShipmentId=");
		sb.append(commerceShipmentId);

		sb.append("}");

		throw new NoSuchShipmentItemException(sb.toString());
	}

	/**
	 * Returns the last commerce shipment item in the ordered set where commerceShipmentId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce shipment item, or <code>null</code> if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByCommerceShipmentId_Last(
		long commerceShipmentId,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		int count = countByCommerceShipmentId(commerceShipmentId);

		if (count == 0) {
			return null;
		}

		List<CommerceShipmentItem> list = findByCommerceShipmentId(
			commerceShipmentId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce shipment items before and after the current commerce shipment item in the ordered set where commerceShipmentId = &#63;.
	 *
	 * @param commerceShipmentItemId the primary key of the current commerce shipment item
	 * @param commerceShipmentId the commerce shipment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce shipment item
	 * @throws NoSuchShipmentItemException if a commerce shipment item with the primary key could not be found
	 */
	@Override
	public CommerceShipmentItem[] findByCommerceShipmentId_PrevAndNext(
			long commerceShipmentItemId, long commerceShipmentId,
			OrderByComparator<CommerceShipmentItem> orderByComparator)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = findByPrimaryKey(
			commerceShipmentItemId);

		Session session = null;

		try {
			session = openSession();

			CommerceShipmentItem[] array = new CommerceShipmentItemImpl[3];

			array[0] = getByCommerceShipmentId_PrevAndNext(
				session, commerceShipmentItem, commerceShipmentId,
				orderByComparator, true);

			array[1] = commerceShipmentItem;

			array[2] = getByCommerceShipmentId_PrevAndNext(
				session, commerceShipmentItem, commerceShipmentId,
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

	protected CommerceShipmentItem getByCommerceShipmentId_PrevAndNext(
		Session session, CommerceShipmentItem commerceShipmentItem,
		long commerceShipmentId,
		OrderByComparator<CommerceShipmentItem> orderByComparator,
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

		sb.append(_SQL_SELECT_COMMERCESHIPMENTITEM_WHERE);

		sb.append(_FINDER_COLUMN_COMMERCESHIPMENTID_COMMERCESHIPMENTID_2);

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
			sb.append(CommerceShipmentItemModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(commerceShipmentId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commerceShipmentItem)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommerceShipmentItem> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce shipment items where commerceShipmentId = &#63; from the database.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 */
	@Override
	public void removeByCommerceShipmentId(long commerceShipmentId) {
		for (CommerceShipmentItem commerceShipmentItem :
				findByCommerceShipmentId(
					commerceShipmentId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(commerceShipmentItem);
		}
	}

	/**
	 * Returns the number of commerce shipment items where commerceShipmentId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @return the number of matching commerce shipment items
	 */
	@Override
	public int countByCommerceShipmentId(long commerceShipmentId) {
		FinderPath finderPath = _finderPathCountByCommerceShipmentId;

		Object[] finderArgs = new Object[] {commerceShipmentId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMERCESHIPMENTITEM_WHERE);

			sb.append(_FINDER_COLUMN_COMMERCESHIPMENTID_COMMERCESHIPMENTID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commerceShipmentId);

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
		_FINDER_COLUMN_COMMERCESHIPMENTID_COMMERCESHIPMENTID_2 =
			"commerceShipmentItem.commerceShipmentId = ?";

	private FinderPath _finderPathWithPaginationFindByCommerceOrderItemId;
	private FinderPath _finderPathWithoutPaginationFindByCommerceOrderItemId;
	private FinderPath _finderPathCountByCommerceOrderItemId;

	/**
	 * Returns all the commerce shipment items where commerceOrderItemId = &#63;.
	 *
	 * @param commerceOrderItemId the commerce order item ID
	 * @return the matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByCommerceOrderItemId(
		long commerceOrderItemId) {

		return findByCommerceOrderItemId(
			commerceOrderItemId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce shipment items where commerceOrderItemId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param commerceOrderItemId the commerce order item ID
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @return the range of matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByCommerceOrderItemId(
		long commerceOrderItemId, int start, int end) {

		return findByCommerceOrderItemId(commerceOrderItemId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce shipment items where commerceOrderItemId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param commerceOrderItemId the commerce order item ID
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByCommerceOrderItemId(
		long commerceOrderItemId, int start, int end,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		return findByCommerceOrderItemId(
			commerceOrderItemId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce shipment items where commerceOrderItemId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param commerceOrderItemId the commerce order item ID
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByCommerceOrderItemId(
		long commerceOrderItemId, int start, int end,
		OrderByComparator<CommerceShipmentItem> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByCommerceOrderItemId;
				finderArgs = new Object[] {commerceOrderItemId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCommerceOrderItemId;
			finderArgs = new Object[] {
				commerceOrderItemId, start, end, orderByComparator
			};
		}

		List<CommerceShipmentItem> list = null;

		if (useFinderCache) {
			list = (List<CommerceShipmentItem>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommerceShipmentItem commerceShipmentItem : list) {
					if (commerceOrderItemId !=
							commerceShipmentItem.getCommerceOrderItemId()) {

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

			sb.append(_SQL_SELECT_COMMERCESHIPMENTITEM_WHERE);

			sb.append(_FINDER_COLUMN_COMMERCEORDERITEMID_COMMERCEORDERITEMID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommerceShipmentItemModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commerceOrderItemId);

				list = (List<CommerceShipmentItem>)QueryUtil.list(
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
	 * Returns the first commerce shipment item in the ordered set where commerceOrderItemId = &#63;.
	 *
	 * @param commerceOrderItemId the commerce order item ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce shipment item
	 * @throws NoSuchShipmentItemException if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem findByCommerceOrderItemId_First(
			long commerceOrderItemId,
			OrderByComparator<CommerceShipmentItem> orderByComparator)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem =
			fetchByCommerceOrderItemId_First(
				commerceOrderItemId, orderByComparator);

		if (commerceShipmentItem != null) {
			return commerceShipmentItem;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("commerceOrderItemId=");
		sb.append(commerceOrderItemId);

		sb.append("}");

		throw new NoSuchShipmentItemException(sb.toString());
	}

	/**
	 * Returns the first commerce shipment item in the ordered set where commerceOrderItemId = &#63;.
	 *
	 * @param commerceOrderItemId the commerce order item ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce shipment item, or <code>null</code> if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByCommerceOrderItemId_First(
		long commerceOrderItemId,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		List<CommerceShipmentItem> list = findByCommerceOrderItemId(
			commerceOrderItemId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce shipment item in the ordered set where commerceOrderItemId = &#63;.
	 *
	 * @param commerceOrderItemId the commerce order item ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce shipment item
	 * @throws NoSuchShipmentItemException if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem findByCommerceOrderItemId_Last(
			long commerceOrderItemId,
			OrderByComparator<CommerceShipmentItem> orderByComparator)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem =
			fetchByCommerceOrderItemId_Last(
				commerceOrderItemId, orderByComparator);

		if (commerceShipmentItem != null) {
			return commerceShipmentItem;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("commerceOrderItemId=");
		sb.append(commerceOrderItemId);

		sb.append("}");

		throw new NoSuchShipmentItemException(sb.toString());
	}

	/**
	 * Returns the last commerce shipment item in the ordered set where commerceOrderItemId = &#63;.
	 *
	 * @param commerceOrderItemId the commerce order item ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce shipment item, or <code>null</code> if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByCommerceOrderItemId_Last(
		long commerceOrderItemId,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		int count = countByCommerceOrderItemId(commerceOrderItemId);

		if (count == 0) {
			return null;
		}

		List<CommerceShipmentItem> list = findByCommerceOrderItemId(
			commerceOrderItemId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce shipment items before and after the current commerce shipment item in the ordered set where commerceOrderItemId = &#63;.
	 *
	 * @param commerceShipmentItemId the primary key of the current commerce shipment item
	 * @param commerceOrderItemId the commerce order item ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce shipment item
	 * @throws NoSuchShipmentItemException if a commerce shipment item with the primary key could not be found
	 */
	@Override
	public CommerceShipmentItem[] findByCommerceOrderItemId_PrevAndNext(
			long commerceShipmentItemId, long commerceOrderItemId,
			OrderByComparator<CommerceShipmentItem> orderByComparator)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = findByPrimaryKey(
			commerceShipmentItemId);

		Session session = null;

		try {
			session = openSession();

			CommerceShipmentItem[] array = new CommerceShipmentItemImpl[3];

			array[0] = getByCommerceOrderItemId_PrevAndNext(
				session, commerceShipmentItem, commerceOrderItemId,
				orderByComparator, true);

			array[1] = commerceShipmentItem;

			array[2] = getByCommerceOrderItemId_PrevAndNext(
				session, commerceShipmentItem, commerceOrderItemId,
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

	protected CommerceShipmentItem getByCommerceOrderItemId_PrevAndNext(
		Session session, CommerceShipmentItem commerceShipmentItem,
		long commerceOrderItemId,
		OrderByComparator<CommerceShipmentItem> orderByComparator,
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

		sb.append(_SQL_SELECT_COMMERCESHIPMENTITEM_WHERE);

		sb.append(_FINDER_COLUMN_COMMERCEORDERITEMID_COMMERCEORDERITEMID_2);

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
			sb.append(CommerceShipmentItemModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(commerceOrderItemId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commerceShipmentItem)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommerceShipmentItem> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce shipment items where commerceOrderItemId = &#63; from the database.
	 *
	 * @param commerceOrderItemId the commerce order item ID
	 */
	@Override
	public void removeByCommerceOrderItemId(long commerceOrderItemId) {
		for (CommerceShipmentItem commerceShipmentItem :
				findByCommerceOrderItemId(
					commerceOrderItemId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(commerceShipmentItem);
		}
	}

	/**
	 * Returns the number of commerce shipment items where commerceOrderItemId = &#63;.
	 *
	 * @param commerceOrderItemId the commerce order item ID
	 * @return the number of matching commerce shipment items
	 */
	@Override
	public int countByCommerceOrderItemId(long commerceOrderItemId) {
		FinderPath finderPath = _finderPathCountByCommerceOrderItemId;

		Object[] finderArgs = new Object[] {commerceOrderItemId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMERCESHIPMENTITEM_WHERE);

			sb.append(_FINDER_COLUMN_COMMERCEORDERITEMID_COMMERCEORDERITEMID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commerceOrderItemId);

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
		_FINDER_COLUMN_COMMERCEORDERITEMID_COMMERCEORDERITEMID_2 =
			"commerceShipmentItem.commerceOrderItemId = ?";

	private FinderPath _finderPathWithPaginationFindByC_C;
	private FinderPath _finderPathWithoutPaginationFindByC_C;
	private FinderPath _finderPathCountByC_C;

	/**
	 * Returns all the commerce shipment items where commerceShipmentId = &#63; and commerceOrderItemId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @return the matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByC_C(
		long commerceShipmentId, long commerceOrderItemId) {

		return findByC_C(
			commerceShipmentId, commerceOrderItemId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce shipment items where commerceShipmentId = &#63; and commerceOrderItemId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @return the range of matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByC_C(
		long commerceShipmentId, long commerceOrderItemId, int start, int end) {

		return findByC_C(
			commerceShipmentId, commerceOrderItemId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce shipment items where commerceShipmentId = &#63; and commerceOrderItemId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByC_C(
		long commerceShipmentId, long commerceOrderItemId, int start, int end,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		return findByC_C(
			commerceShipmentId, commerceOrderItemId, start, end,
			orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce shipment items where commerceShipmentId = &#63; and commerceOrderItemId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findByC_C(
		long commerceShipmentId, long commerceOrderItemId, int start, int end,
		OrderByComparator<CommerceShipmentItem> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByC_C;
				finderArgs = new Object[] {
					commerceShipmentId, commerceOrderItemId
				};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByC_C;
			finderArgs = new Object[] {
				commerceShipmentId, commerceOrderItemId, start, end,
				orderByComparator
			};
		}

		List<CommerceShipmentItem> list = null;

		if (useFinderCache) {
			list = (List<CommerceShipmentItem>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommerceShipmentItem commerceShipmentItem : list) {
					if ((commerceShipmentId !=
							commerceShipmentItem.getCommerceShipmentId()) ||
						(commerceOrderItemId !=
							commerceShipmentItem.getCommerceOrderItemId())) {

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

			sb.append(_SQL_SELECT_COMMERCESHIPMENTITEM_WHERE);

			sb.append(_FINDER_COLUMN_C_C_COMMERCESHIPMENTID_2);

			sb.append(_FINDER_COLUMN_C_C_COMMERCEORDERITEMID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommerceShipmentItemModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commerceShipmentId);

				queryPos.add(commerceOrderItemId);

				list = (List<CommerceShipmentItem>)QueryUtil.list(
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
	 * Returns the first commerce shipment item in the ordered set where commerceShipmentId = &#63; and commerceOrderItemId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce shipment item
	 * @throws NoSuchShipmentItemException if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem findByC_C_First(
			long commerceShipmentId, long commerceOrderItemId,
			OrderByComparator<CommerceShipmentItem> orderByComparator)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = fetchByC_C_First(
			commerceShipmentId, commerceOrderItemId, orderByComparator);

		if (commerceShipmentItem != null) {
			return commerceShipmentItem;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("commerceShipmentId=");
		sb.append(commerceShipmentId);

		sb.append(", commerceOrderItemId=");
		sb.append(commerceOrderItemId);

		sb.append("}");

		throw new NoSuchShipmentItemException(sb.toString());
	}

	/**
	 * Returns the first commerce shipment item in the ordered set where commerceShipmentId = &#63; and commerceOrderItemId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce shipment item, or <code>null</code> if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByC_C_First(
		long commerceShipmentId, long commerceOrderItemId,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		List<CommerceShipmentItem> list = findByC_C(
			commerceShipmentId, commerceOrderItemId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce shipment item in the ordered set where commerceShipmentId = &#63; and commerceOrderItemId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce shipment item
	 * @throws NoSuchShipmentItemException if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem findByC_C_Last(
			long commerceShipmentId, long commerceOrderItemId,
			OrderByComparator<CommerceShipmentItem> orderByComparator)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = fetchByC_C_Last(
			commerceShipmentId, commerceOrderItemId, orderByComparator);

		if (commerceShipmentItem != null) {
			return commerceShipmentItem;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("commerceShipmentId=");
		sb.append(commerceShipmentId);

		sb.append(", commerceOrderItemId=");
		sb.append(commerceOrderItemId);

		sb.append("}");

		throw new NoSuchShipmentItemException(sb.toString());
	}

	/**
	 * Returns the last commerce shipment item in the ordered set where commerceShipmentId = &#63; and commerceOrderItemId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce shipment item, or <code>null</code> if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByC_C_Last(
		long commerceShipmentId, long commerceOrderItemId,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		int count = countByC_C(commerceShipmentId, commerceOrderItemId);

		if (count == 0) {
			return null;
		}

		List<CommerceShipmentItem> list = findByC_C(
			commerceShipmentId, commerceOrderItemId, count - 1, count,
			orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce shipment items before and after the current commerce shipment item in the ordered set where commerceShipmentId = &#63; and commerceOrderItemId = &#63;.
	 *
	 * @param commerceShipmentItemId the primary key of the current commerce shipment item
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce shipment item
	 * @throws NoSuchShipmentItemException if a commerce shipment item with the primary key could not be found
	 */
	@Override
	public CommerceShipmentItem[] findByC_C_PrevAndNext(
			long commerceShipmentItemId, long commerceShipmentId,
			long commerceOrderItemId,
			OrderByComparator<CommerceShipmentItem> orderByComparator)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = findByPrimaryKey(
			commerceShipmentItemId);

		Session session = null;

		try {
			session = openSession();

			CommerceShipmentItem[] array = new CommerceShipmentItemImpl[3];

			array[0] = getByC_C_PrevAndNext(
				session, commerceShipmentItem, commerceShipmentId,
				commerceOrderItemId, orderByComparator, true);

			array[1] = commerceShipmentItem;

			array[2] = getByC_C_PrevAndNext(
				session, commerceShipmentItem, commerceShipmentId,
				commerceOrderItemId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommerceShipmentItem getByC_C_PrevAndNext(
		Session session, CommerceShipmentItem commerceShipmentItem,
		long commerceShipmentId, long commerceOrderItemId,
		OrderByComparator<CommerceShipmentItem> orderByComparator,
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

		sb.append(_SQL_SELECT_COMMERCESHIPMENTITEM_WHERE);

		sb.append(_FINDER_COLUMN_C_C_COMMERCESHIPMENTID_2);

		sb.append(_FINDER_COLUMN_C_C_COMMERCEORDERITEMID_2);

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
			sb.append(CommerceShipmentItemModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(commerceShipmentId);

		queryPos.add(commerceOrderItemId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commerceShipmentItem)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommerceShipmentItem> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce shipment items where commerceShipmentId = &#63; and commerceOrderItemId = &#63; from the database.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 */
	@Override
	public void removeByC_C(long commerceShipmentId, long commerceOrderItemId) {
		for (CommerceShipmentItem commerceShipmentItem :
				findByC_C(
					commerceShipmentId, commerceOrderItemId, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(commerceShipmentItem);
		}
	}

	/**
	 * Returns the number of commerce shipment items where commerceShipmentId = &#63; and commerceOrderItemId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @return the number of matching commerce shipment items
	 */
	@Override
	public int countByC_C(long commerceShipmentId, long commerceOrderItemId) {
		FinderPath finderPath = _finderPathCountByC_C;

		Object[] finderArgs = new Object[] {
			commerceShipmentId, commerceOrderItemId
		};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMERCESHIPMENTITEM_WHERE);

			sb.append(_FINDER_COLUMN_C_C_COMMERCESHIPMENTID_2);

			sb.append(_FINDER_COLUMN_C_C_COMMERCEORDERITEMID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commerceShipmentId);

				queryPos.add(commerceOrderItemId);

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

	private static final String _FINDER_COLUMN_C_C_COMMERCESHIPMENTID_2 =
		"commerceShipmentItem.commerceShipmentId = ? AND ";

	private static final String _FINDER_COLUMN_C_C_COMMERCEORDERITEMID_2 =
		"commerceShipmentItem.commerceOrderItemId = ?";

	private FinderPath _finderPathFetchByC_C_C;
	private FinderPath _finderPathCountByC_C_C;

	/**
	 * Returns the commerce shipment item where commerceShipmentId = &#63; and commerceOrderItemId = &#63; and commerceInventoryWarehouseId = &#63; or throws a <code>NoSuchShipmentItemException</code> if it could not be found.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param commerceInventoryWarehouseId the commerce inventory warehouse ID
	 * @return the matching commerce shipment item
	 * @throws NoSuchShipmentItemException if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem findByC_C_C(
			long commerceShipmentId, long commerceOrderItemId,
			long commerceInventoryWarehouseId)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = fetchByC_C_C(
			commerceShipmentId, commerceOrderItemId,
			commerceInventoryWarehouseId);

		if (commerceShipmentItem == null) {
			StringBundler sb = new StringBundler(8);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("commerceShipmentId=");
			sb.append(commerceShipmentId);

			sb.append(", commerceOrderItemId=");
			sb.append(commerceOrderItemId);

			sb.append(", commerceInventoryWarehouseId=");
			sb.append(commerceInventoryWarehouseId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchShipmentItemException(sb.toString());
		}

		return commerceShipmentItem;
	}

	/**
	 * Returns the commerce shipment item where commerceShipmentId = &#63; and commerceOrderItemId = &#63; and commerceInventoryWarehouseId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param commerceInventoryWarehouseId the commerce inventory warehouse ID
	 * @return the matching commerce shipment item, or <code>null</code> if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByC_C_C(
		long commerceShipmentId, long commerceOrderItemId,
		long commerceInventoryWarehouseId) {

		return fetchByC_C_C(
			commerceShipmentId, commerceOrderItemId,
			commerceInventoryWarehouseId, true);
	}

	/**
	 * Returns the commerce shipment item where commerceShipmentId = &#63; and commerceOrderItemId = &#63; and commerceInventoryWarehouseId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param commerceInventoryWarehouseId the commerce inventory warehouse ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce shipment item, or <code>null</code> if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByC_C_C(
		long commerceShipmentId, long commerceOrderItemId,
		long commerceInventoryWarehouseId, boolean useFinderCache) {

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {
				commerceShipmentId, commerceOrderItemId,
				commerceInventoryWarehouseId
			};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(_finderPathFetchByC_C_C, finderArgs);
		}

		if (result instanceof CommerceShipmentItem) {
			CommerceShipmentItem commerceShipmentItem =
				(CommerceShipmentItem)result;

			if ((commerceShipmentId !=
					commerceShipmentItem.getCommerceShipmentId()) ||
				(commerceOrderItemId !=
					commerceShipmentItem.getCommerceOrderItemId()) ||
				(commerceInventoryWarehouseId !=
					commerceShipmentItem.getCommerceInventoryWarehouseId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(5);

			sb.append(_SQL_SELECT_COMMERCESHIPMENTITEM_WHERE);

			sb.append(_FINDER_COLUMN_C_C_C_COMMERCESHIPMENTID_2);

			sb.append(_FINDER_COLUMN_C_C_C_COMMERCEORDERITEMID_2);

			sb.append(_FINDER_COLUMN_C_C_C_COMMERCEINVENTORYWAREHOUSEID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commerceShipmentId);

				queryPos.add(commerceOrderItemId);

				queryPos.add(commerceInventoryWarehouseId);

				List<CommerceShipmentItem> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByC_C_C, finderArgs, list);
					}
				}
				else {
					CommerceShipmentItem commerceShipmentItem = list.get(0);

					result = commerceShipmentItem;

					cacheResult(commerceShipmentItem);
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
			return (CommerceShipmentItem)result;
		}
	}

	/**
	 * Removes the commerce shipment item where commerceShipmentId = &#63; and commerceOrderItemId = &#63; and commerceInventoryWarehouseId = &#63; from the database.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param commerceInventoryWarehouseId the commerce inventory warehouse ID
	 * @return the commerce shipment item that was removed
	 */
	@Override
	public CommerceShipmentItem removeByC_C_C(
			long commerceShipmentId, long commerceOrderItemId,
			long commerceInventoryWarehouseId)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = findByC_C_C(
			commerceShipmentId, commerceOrderItemId,
			commerceInventoryWarehouseId);

		return remove(commerceShipmentItem);
	}

	/**
	 * Returns the number of commerce shipment items where commerceShipmentId = &#63; and commerceOrderItemId = &#63; and commerceInventoryWarehouseId = &#63;.
	 *
	 * @param commerceShipmentId the commerce shipment ID
	 * @param commerceOrderItemId the commerce order item ID
	 * @param commerceInventoryWarehouseId the commerce inventory warehouse ID
	 * @return the number of matching commerce shipment items
	 */
	@Override
	public int countByC_C_C(
		long commerceShipmentId, long commerceOrderItemId,
		long commerceInventoryWarehouseId) {

		FinderPath finderPath = _finderPathCountByC_C_C;

		Object[] finderArgs = new Object[] {
			commerceShipmentId, commerceOrderItemId,
			commerceInventoryWarehouseId
		};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_COMMERCESHIPMENTITEM_WHERE);

			sb.append(_FINDER_COLUMN_C_C_C_COMMERCESHIPMENTID_2);

			sb.append(_FINDER_COLUMN_C_C_C_COMMERCEORDERITEMID_2);

			sb.append(_FINDER_COLUMN_C_C_C_COMMERCEINVENTORYWAREHOUSEID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commerceShipmentId);

				queryPos.add(commerceOrderItemId);

				queryPos.add(commerceInventoryWarehouseId);

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

	private static final String _FINDER_COLUMN_C_C_C_COMMERCESHIPMENTID_2 =
		"commerceShipmentItem.commerceShipmentId = ? AND ";

	private static final String _FINDER_COLUMN_C_C_C_COMMERCEORDERITEMID_2 =
		"commerceShipmentItem.commerceOrderItemId = ? AND ";

	private static final String
		_FINDER_COLUMN_C_C_C_COMMERCEINVENTORYWAREHOUSEID_2 =
			"commerceShipmentItem.commerceInventoryWarehouseId = ?";

	private FinderPath _finderPathFetchByC_ERC;
	private FinderPath _finderPathCountByC_ERC;

	/**
	 * Returns the commerce shipment item where companyId = &#63; and externalReferenceCode = &#63; or throws a <code>NoSuchShipmentItemException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching commerce shipment item
	 * @throws NoSuchShipmentItemException if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem findByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = fetchByC_ERC(
			companyId, externalReferenceCode);

		if (commerceShipmentItem == null) {
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

			throw new NoSuchShipmentItemException(sb.toString());
		}

		return commerceShipmentItem;
	}

	/**
	 * Returns the commerce shipment item where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching commerce shipment item, or <code>null</code> if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByC_ERC(
		long companyId, String externalReferenceCode) {

		return fetchByC_ERC(companyId, externalReferenceCode, true);
	}

	/**
	 * Returns the commerce shipment item where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce shipment item, or <code>null</code> if a matching commerce shipment item could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByC_ERC(
		long companyId, String externalReferenceCode, boolean useFinderCache) {

		externalReferenceCode = Objects.toString(externalReferenceCode, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {companyId, externalReferenceCode};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(_finderPathFetchByC_ERC, finderArgs);
		}

		if (result instanceof CommerceShipmentItem) {
			CommerceShipmentItem commerceShipmentItem =
				(CommerceShipmentItem)result;

			if ((companyId != commerceShipmentItem.getCompanyId()) ||
				!Objects.equals(
					externalReferenceCode,
					commerceShipmentItem.getExternalReferenceCode())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_COMMERCESHIPMENTITEM_WHERE);

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

				List<CommerceShipmentItem> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByC_ERC, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {
									companyId, externalReferenceCode
								};
							}

							_log.warn(
								"CommerceShipmentItemPersistenceImpl.fetchByC_ERC(long, String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					CommerceShipmentItem commerceShipmentItem = list.get(0);

					result = commerceShipmentItem;

					cacheResult(commerceShipmentItem);
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
			return (CommerceShipmentItem)result;
		}
	}

	/**
	 * Removes the commerce shipment item where companyId = &#63; and externalReferenceCode = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the commerce shipment item that was removed
	 */
	@Override
	public CommerceShipmentItem removeByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = findByC_ERC(
			companyId, externalReferenceCode);

		return remove(commerceShipmentItem);
	}

	/**
	 * Returns the number of commerce shipment items where companyId = &#63; and externalReferenceCode = &#63;.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the number of matching commerce shipment items
	 */
	@Override
	public int countByC_ERC(long companyId, String externalReferenceCode) {
		externalReferenceCode = Objects.toString(externalReferenceCode, "");

		FinderPath finderPath = _finderPathCountByC_ERC;

		Object[] finderArgs = new Object[] {companyId, externalReferenceCode};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMERCESHIPMENTITEM_WHERE);

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

	private static final String _FINDER_COLUMN_C_ERC_COMPANYID_2 =
		"commerceShipmentItem.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_2 =
		"commerceShipmentItem.externalReferenceCode = ?";

	private static final String _FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_3 =
		"(commerceShipmentItem.externalReferenceCode IS NULL OR commerceShipmentItem.externalReferenceCode = '')";

	public CommerceShipmentItemPersistenceImpl() {
		setModelClass(CommerceShipmentItem.class);

		setModelImplClass(CommerceShipmentItemImpl.class);
		setModelPKClass(long.class);

		setTable(CommerceShipmentItemTable.INSTANCE);
	}

	/**
	 * Caches the commerce shipment item in the entity cache if it is enabled.
	 *
	 * @param commerceShipmentItem the commerce shipment item
	 */
	@Override
	public void cacheResult(CommerceShipmentItem commerceShipmentItem) {
		entityCache.putResult(
			CommerceShipmentItemImpl.class,
			commerceShipmentItem.getPrimaryKey(), commerceShipmentItem);

		finderCache.putResult(
			_finderPathFetchByC_C_C,
			new Object[] {
				commerceShipmentItem.getCommerceShipmentId(),
				commerceShipmentItem.getCommerceOrderItemId(),
				commerceShipmentItem.getCommerceInventoryWarehouseId()
			},
			commerceShipmentItem);

		finderCache.putResult(
			_finderPathFetchByC_ERC,
			new Object[] {
				commerceShipmentItem.getCompanyId(),
				commerceShipmentItem.getExternalReferenceCode()
			},
			commerceShipmentItem);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the commerce shipment items in the entity cache if it is enabled.
	 *
	 * @param commerceShipmentItems the commerce shipment items
	 */
	@Override
	public void cacheResult(List<CommerceShipmentItem> commerceShipmentItems) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (commerceShipmentItems.size() >
				 _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (CommerceShipmentItem commerceShipmentItem :
				commerceShipmentItems) {

			if (entityCache.getResult(
					CommerceShipmentItemImpl.class,
					commerceShipmentItem.getPrimaryKey()) == null) {

				cacheResult(commerceShipmentItem);
			}
		}
	}

	/**
	 * Clears the cache for all commerce shipment items.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(CommerceShipmentItemImpl.class);

		finderCache.clearCache(CommerceShipmentItemImpl.class);
	}

	/**
	 * Clears the cache for the commerce shipment item.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(CommerceShipmentItem commerceShipmentItem) {
		entityCache.removeResult(
			CommerceShipmentItemImpl.class, commerceShipmentItem);
	}

	@Override
	public void clearCache(List<CommerceShipmentItem> commerceShipmentItems) {
		for (CommerceShipmentItem commerceShipmentItem :
				commerceShipmentItems) {

			entityCache.removeResult(
				CommerceShipmentItemImpl.class, commerceShipmentItem);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(CommerceShipmentItemImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				CommerceShipmentItemImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		CommerceShipmentItemModelImpl commerceShipmentItemModelImpl) {

		Object[] args = new Object[] {
			commerceShipmentItemModelImpl.getCommerceShipmentId(),
			commerceShipmentItemModelImpl.getCommerceOrderItemId(),
			commerceShipmentItemModelImpl.getCommerceInventoryWarehouseId()
		};

		finderCache.putResult(_finderPathCountByC_C_C, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByC_C_C, args, commerceShipmentItemModelImpl);

		args = new Object[] {
			commerceShipmentItemModelImpl.getCompanyId(),
			commerceShipmentItemModelImpl.getExternalReferenceCode()
		};

		finderCache.putResult(_finderPathCountByC_ERC, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByC_ERC, args, commerceShipmentItemModelImpl);
	}

	/**
	 * Creates a new commerce shipment item with the primary key. Does not add the commerce shipment item to the database.
	 *
	 * @param commerceShipmentItemId the primary key for the new commerce shipment item
	 * @return the new commerce shipment item
	 */
	@Override
	public CommerceShipmentItem create(long commerceShipmentItemId) {
		CommerceShipmentItem commerceShipmentItem =
			new CommerceShipmentItemImpl();

		commerceShipmentItem.setNew(true);
		commerceShipmentItem.setPrimaryKey(commerceShipmentItemId);

		commerceShipmentItem.setCompanyId(CompanyThreadLocal.getCompanyId());

		return commerceShipmentItem;
	}

	/**
	 * Removes the commerce shipment item with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commerceShipmentItemId the primary key of the commerce shipment item
	 * @return the commerce shipment item that was removed
	 * @throws NoSuchShipmentItemException if a commerce shipment item with the primary key could not be found
	 */
	@Override
	public CommerceShipmentItem remove(long commerceShipmentItemId)
		throws NoSuchShipmentItemException {

		return remove((Serializable)commerceShipmentItemId);
	}

	/**
	 * Removes the commerce shipment item with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the commerce shipment item
	 * @return the commerce shipment item that was removed
	 * @throws NoSuchShipmentItemException if a commerce shipment item with the primary key could not be found
	 */
	@Override
	public CommerceShipmentItem remove(Serializable primaryKey)
		throws NoSuchShipmentItemException {

		Session session = null;

		try {
			session = openSession();

			CommerceShipmentItem commerceShipmentItem =
				(CommerceShipmentItem)session.get(
					CommerceShipmentItemImpl.class, primaryKey);

			if (commerceShipmentItem == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchShipmentItemException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(commerceShipmentItem);
		}
		catch (NoSuchShipmentItemException noSuchEntityException) {
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
	protected CommerceShipmentItem removeImpl(
		CommerceShipmentItem commerceShipmentItem) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(commerceShipmentItem)) {
				commerceShipmentItem = (CommerceShipmentItem)session.get(
					CommerceShipmentItemImpl.class,
					commerceShipmentItem.getPrimaryKeyObj());
			}

			if (commerceShipmentItem != null) {
				session.delete(commerceShipmentItem);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (commerceShipmentItem != null) {
			clearCache(commerceShipmentItem);
		}

		return commerceShipmentItem;
	}

	@Override
	public CommerceShipmentItem updateImpl(
		CommerceShipmentItem commerceShipmentItem) {

		boolean isNew = commerceShipmentItem.isNew();

		if (!(commerceShipmentItem instanceof CommerceShipmentItemModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(commerceShipmentItem.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					commerceShipmentItem);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in commerceShipmentItem proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom CommerceShipmentItem implementation " +
					commerceShipmentItem.getClass());
		}

		CommerceShipmentItemModelImpl commerceShipmentItemModelImpl =
			(CommerceShipmentItemModelImpl)commerceShipmentItem;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (commerceShipmentItem.getCreateDate() == null)) {
			if (serviceContext == null) {
				commerceShipmentItem.setCreateDate(date);
			}
			else {
				commerceShipmentItem.setCreateDate(
					serviceContext.getCreateDate(date));
			}
		}

		if (!commerceShipmentItemModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				commerceShipmentItem.setModifiedDate(date);
			}
			else {
				commerceShipmentItem.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(commerceShipmentItem);
			}
			else {
				commerceShipmentItem = (CommerceShipmentItem)session.merge(
					commerceShipmentItem);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			CommerceShipmentItemImpl.class, commerceShipmentItemModelImpl,
			false, true);

		cacheUniqueFindersCache(commerceShipmentItemModelImpl);

		if (isNew) {
			commerceShipmentItem.setNew(false);
		}

		commerceShipmentItem.resetOriginalValues();

		return commerceShipmentItem;
	}

	/**
	 * Returns the commerce shipment item with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce shipment item
	 * @return the commerce shipment item
	 * @throws NoSuchShipmentItemException if a commerce shipment item with the primary key could not be found
	 */
	@Override
	public CommerceShipmentItem findByPrimaryKey(Serializable primaryKey)
		throws NoSuchShipmentItemException {

		CommerceShipmentItem commerceShipmentItem = fetchByPrimaryKey(
			primaryKey);

		if (commerceShipmentItem == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchShipmentItemException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return commerceShipmentItem;
	}

	/**
	 * Returns the commerce shipment item with the primary key or throws a <code>NoSuchShipmentItemException</code> if it could not be found.
	 *
	 * @param commerceShipmentItemId the primary key of the commerce shipment item
	 * @return the commerce shipment item
	 * @throws NoSuchShipmentItemException if a commerce shipment item with the primary key could not be found
	 */
	@Override
	public CommerceShipmentItem findByPrimaryKey(long commerceShipmentItemId)
		throws NoSuchShipmentItemException {

		return findByPrimaryKey((Serializable)commerceShipmentItemId);
	}

	/**
	 * Returns the commerce shipment item with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commerceShipmentItemId the primary key of the commerce shipment item
	 * @return the commerce shipment item, or <code>null</code> if a commerce shipment item with the primary key could not be found
	 */
	@Override
	public CommerceShipmentItem fetchByPrimaryKey(long commerceShipmentItemId) {
		return fetchByPrimaryKey((Serializable)commerceShipmentItemId);
	}

	/**
	 * Returns all the commerce shipment items.
	 *
	 * @return the commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce shipment items.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @return the range of commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce shipment items.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findAll(
		int start, int end,
		OrderByComparator<CommerceShipmentItem> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce shipment items.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceShipmentItemModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce shipment items
	 * @param end the upper bound of the range of commerce shipment items (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of commerce shipment items
	 */
	@Override
	public List<CommerceShipmentItem> findAll(
		int start, int end,
		OrderByComparator<CommerceShipmentItem> orderByComparator,
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

		List<CommerceShipmentItem> list = null;

		if (useFinderCache) {
			list = (List<CommerceShipmentItem>)finderCache.getResult(
				finderPath, finderArgs);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_COMMERCESHIPMENTITEM);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_COMMERCESHIPMENTITEM;

				sql = sql.concat(CommerceShipmentItemModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<CommerceShipmentItem>)QueryUtil.list(
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
	 * Removes all the commerce shipment items from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (CommerceShipmentItem commerceShipmentItem : findAll()) {
			remove(commerceShipmentItem);
		}
	}

	/**
	 * Returns the number of commerce shipment items.
	 *
	 * @return the number of commerce shipment items
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(
					_SQL_COUNT_COMMERCESHIPMENTITEM);

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
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "commerceShipmentItemId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_COMMERCESHIPMENTITEM;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return CommerceShipmentItemModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the commerce shipment item persistence.
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

		_finderPathWithPaginationFindByGroupId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"groupId"}, true);

		_finderPathWithoutPaginationFindByGroupId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupId",
			new String[] {Long.class.getName()}, new String[] {"groupId"},
			true);

		_finderPathCountByGroupId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupId",
			new String[] {Long.class.getName()}, new String[] {"groupId"},
			false);

		_finderPathWithPaginationFindByCommerceShipmentId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCommerceShipmentId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"commerceShipmentId"}, true);

		_finderPathWithoutPaginationFindByCommerceShipmentId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByCommerceShipmentId", new String[] {Long.class.getName()},
			new String[] {"commerceShipmentId"}, true);

		_finderPathCountByCommerceShipmentId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByCommerceShipmentId", new String[] {Long.class.getName()},
			new String[] {"commerceShipmentId"}, false);

		_finderPathWithPaginationFindByCommerceOrderItemId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCommerceOrderItemId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"commerceOrderItemId"}, true);

		_finderPathWithoutPaginationFindByCommerceOrderItemId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByCommerceOrderItemId", new String[] {Long.class.getName()},
			new String[] {"commerceOrderItemId"}, true);

		_finderPathCountByCommerceOrderItemId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByCommerceOrderItemId", new String[] {Long.class.getName()},
			new String[] {"commerceOrderItemId"}, false);

		_finderPathWithPaginationFindByC_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByC_C",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"commerceShipmentId", "commerceOrderItemId"}, true);

		_finderPathWithoutPaginationFindByC_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByC_C",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"commerceShipmentId", "commerceOrderItemId"}, true);

		_finderPathCountByC_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_C",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"commerceShipmentId", "commerceOrderItemId"}, false);

		_finderPathFetchByC_C_C = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByC_C_C",
			new String[] {
				Long.class.getName(), Long.class.getName(), Long.class.getName()
			},
			new String[] {
				"commerceShipmentId", "commerceOrderItemId",
				"commerceInventoryWarehouseId"
			},
			true);

		_finderPathCountByC_C_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_C_C",
			new String[] {
				Long.class.getName(), Long.class.getName(), Long.class.getName()
			},
			new String[] {
				"commerceShipmentId", "commerceOrderItemId",
				"commerceInventoryWarehouseId"
			},
			false);

		_finderPathFetchByC_ERC = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByC_ERC",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"companyId", "externalReferenceCode"}, true);

		_finderPathCountByC_ERC = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_ERC",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"companyId", "externalReferenceCode"}, false);

		_setCommerceShipmentItemUtilPersistence(this);
	}

	public void destroy() {
		_setCommerceShipmentItemUtilPersistence(null);

		entityCache.removeCache(CommerceShipmentItemImpl.class.getName());
	}

	private void _setCommerceShipmentItemUtilPersistence(
		CommerceShipmentItemPersistence commerceShipmentItemPersistence) {

		try {
			Field field = CommerceShipmentItemUtil.class.getDeclaredField(
				"_persistence");

			field.setAccessible(true);

			field.set(null, commerceShipmentItemPersistence);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;

	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_COMMERCESHIPMENTITEM =
		"SELECT commerceShipmentItem FROM CommerceShipmentItem commerceShipmentItem";

	private static final String _SQL_SELECT_COMMERCESHIPMENTITEM_WHERE =
		"SELECT commerceShipmentItem FROM CommerceShipmentItem commerceShipmentItem WHERE ";

	private static final String _SQL_COUNT_COMMERCESHIPMENTITEM =
		"SELECT COUNT(commerceShipmentItem) FROM CommerceShipmentItem commerceShipmentItem";

	private static final String _SQL_COUNT_COMMERCESHIPMENTITEM_WHERE =
		"SELECT COUNT(commerceShipmentItem) FROM CommerceShipmentItem commerceShipmentItem WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS =
		"commerceShipmentItem.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No CommerceShipmentItem exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No CommerceShipmentItem exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceShipmentItemPersistenceImpl.class);

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

}