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

package com.liferay.client.extension.service.persistence.impl;

import com.liferay.client.extension.exception.NoSuchClientExtensionEntryException;
import com.liferay.client.extension.model.ClientExtensionEntry;
import com.liferay.client.extension.model.ClientExtensionEntryTable;
import com.liferay.client.extension.model.impl.ClientExtensionEntryImpl;
import com.liferay.client.extension.model.impl.ClientExtensionEntryModelImpl;
import com.liferay.client.extension.service.persistence.ClientExtensionEntryPersistence;
import com.liferay.client.extension.service.persistence.ClientExtensionEntryUtil;
import com.liferay.client.extension.service.persistence.impl.constants.ClientExtensionPersistenceConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.sanitizer.Sanitizer;
import com.liferay.portal.kernel.sanitizer.SanitizerException;
import com.liferay.portal.kernel.sanitizer.SanitizerUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.InlineSQLHelperUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.ContentTypes;
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
 * The persistence implementation for the client extension entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(
	service = {ClientExtensionEntryPersistence.class, BasePersistence.class}
)
public class ClientExtensionEntryPersistenceImpl
	extends BasePersistenceImpl<ClientExtensionEntry>
	implements ClientExtensionEntryPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>ClientExtensionEntryUtil</code> to access the client extension entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		ClientExtensionEntryImpl.class.getName();

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
	 * Returns all the client extension entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching client extension entries
	 */
	@Override
	public List<ClientExtensionEntry> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the client extension entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @return the range of matching client extension entries
	 */
	@Override
	public List<ClientExtensionEntry> findByUuid(
		String uuid, int start, int end) {

		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the client extension entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching client extension entries
	 */
	@Override
	public List<ClientExtensionEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<ClientExtensionEntry> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the client extension entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching client extension entries
	 */
	@Override
	public List<ClientExtensionEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<ClientExtensionEntry> orderByComparator,
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

		List<ClientExtensionEntry> list = null;

		if (useFinderCache) {
			list = (List<ClientExtensionEntry>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (ClientExtensionEntry clientExtensionEntry : list) {
					if (!uuid.equals(clientExtensionEntry.getUuid())) {
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

			sb.append(_SQL_SELECT_CLIENTEXTENSIONENTRY_WHERE);

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
				sb.append(ClientExtensionEntryModelImpl.ORDER_BY_JPQL);
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

				list = (List<ClientExtensionEntry>)QueryUtil.list(
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
	 * Returns the first client extension entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching client extension entry
	 * @throws NoSuchClientExtensionEntryException if a matching client extension entry could not be found
	 */
	@Override
	public ClientExtensionEntry findByUuid_First(
			String uuid,
			OrderByComparator<ClientExtensionEntry> orderByComparator)
		throws NoSuchClientExtensionEntryException {

		ClientExtensionEntry clientExtensionEntry = fetchByUuid_First(
			uuid, orderByComparator);

		if (clientExtensionEntry != null) {
			return clientExtensionEntry;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchClientExtensionEntryException(sb.toString());
	}

	/**
	 * Returns the first client extension entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching client extension entry, or <code>null</code> if a matching client extension entry could not be found
	 */
	@Override
	public ClientExtensionEntry fetchByUuid_First(
		String uuid,
		OrderByComparator<ClientExtensionEntry> orderByComparator) {

		List<ClientExtensionEntry> list = findByUuid(
			uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last client extension entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching client extension entry
	 * @throws NoSuchClientExtensionEntryException if a matching client extension entry could not be found
	 */
	@Override
	public ClientExtensionEntry findByUuid_Last(
			String uuid,
			OrderByComparator<ClientExtensionEntry> orderByComparator)
		throws NoSuchClientExtensionEntryException {

		ClientExtensionEntry clientExtensionEntry = fetchByUuid_Last(
			uuid, orderByComparator);

		if (clientExtensionEntry != null) {
			return clientExtensionEntry;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchClientExtensionEntryException(sb.toString());
	}

	/**
	 * Returns the last client extension entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching client extension entry, or <code>null</code> if a matching client extension entry could not be found
	 */
	@Override
	public ClientExtensionEntry fetchByUuid_Last(
		String uuid,
		OrderByComparator<ClientExtensionEntry> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<ClientExtensionEntry> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the client extension entries before and after the current client extension entry in the ordered set where uuid = &#63;.
	 *
	 * @param clientExtensionEntryId the primary key of the current client extension entry
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next client extension entry
	 * @throws NoSuchClientExtensionEntryException if a client extension entry with the primary key could not be found
	 */
	@Override
	public ClientExtensionEntry[] findByUuid_PrevAndNext(
			long clientExtensionEntryId, String uuid,
			OrderByComparator<ClientExtensionEntry> orderByComparator)
		throws NoSuchClientExtensionEntryException {

		uuid = Objects.toString(uuid, "");

		ClientExtensionEntry clientExtensionEntry = findByPrimaryKey(
			clientExtensionEntryId);

		Session session = null;

		try {
			session = openSession();

			ClientExtensionEntry[] array = new ClientExtensionEntryImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, clientExtensionEntry, uuid, orderByComparator, true);

			array[1] = clientExtensionEntry;

			array[2] = getByUuid_PrevAndNext(
				session, clientExtensionEntry, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected ClientExtensionEntry getByUuid_PrevAndNext(
		Session session, ClientExtensionEntry clientExtensionEntry, String uuid,
		OrderByComparator<ClientExtensionEntry> orderByComparator,
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

		sb.append(_SQL_SELECT_CLIENTEXTENSIONENTRY_WHERE);

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
			sb.append(ClientExtensionEntryModelImpl.ORDER_BY_JPQL);
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
						clientExtensionEntry)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ClientExtensionEntry> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the client extension entries that the user has permission to view where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching client extension entries that the user has permission to view
	 */
	@Override
	public List<ClientExtensionEntry> filterFindByUuid(String uuid) {
		return filterFindByUuid(
			uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the client extension entries that the user has permission to view where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @return the range of matching client extension entries that the user has permission to view
	 */
	@Override
	public List<ClientExtensionEntry> filterFindByUuid(
		String uuid, int start, int end) {

		return filterFindByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the client extension entries that the user has permissions to view where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching client extension entries that the user has permission to view
	 */
	@Override
	public List<ClientExtensionEntry> filterFindByUuid(
		String uuid, int start, int end,
		OrderByComparator<ClientExtensionEntry> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled()) {
			return findByUuid(uuid, start, end, orderByComparator);
		}

		uuid = Objects.toString(uuid, "");

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				3 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(4);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_NO_INLINE_DISTINCT_WHERE_1);
		}

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3_SQL);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2_SQL);
		}

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_NO_INLINE_DISTINCT_WHERE_2);
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
				sb.append(ClientExtensionEntryModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(ClientExtensionEntryModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), ClientExtensionEntry.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, ClientExtensionEntryImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, ClientExtensionEntryImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			if (bindUuid) {
				queryPos.add(uuid);
			}

			return (List<ClientExtensionEntry>)QueryUtil.list(
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
	 * Returns the client extension entries before and after the current client extension entry in the ordered set of client extension entries that the user has permission to view where uuid = &#63;.
	 *
	 * @param clientExtensionEntryId the primary key of the current client extension entry
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next client extension entry
	 * @throws NoSuchClientExtensionEntryException if a client extension entry with the primary key could not be found
	 */
	@Override
	public ClientExtensionEntry[] filterFindByUuid_PrevAndNext(
			long clientExtensionEntryId, String uuid,
			OrderByComparator<ClientExtensionEntry> orderByComparator)
		throws NoSuchClientExtensionEntryException {

		if (!InlineSQLHelperUtil.isEnabled()) {
			return findByUuid_PrevAndNext(
				clientExtensionEntryId, uuid, orderByComparator);
		}

		uuid = Objects.toString(uuid, "");

		ClientExtensionEntry clientExtensionEntry = findByPrimaryKey(
			clientExtensionEntryId);

		Session session = null;

		try {
			session = openSession();

			ClientExtensionEntry[] array = new ClientExtensionEntryImpl[3];

			array[0] = filterGetByUuid_PrevAndNext(
				session, clientExtensionEntry, uuid, orderByComparator, true);

			array[1] = clientExtensionEntry;

			array[2] = filterGetByUuid_PrevAndNext(
				session, clientExtensionEntry, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected ClientExtensionEntry filterGetByUuid_PrevAndNext(
		Session session, ClientExtensionEntry clientExtensionEntry, String uuid,
		OrderByComparator<ClientExtensionEntry> orderByComparator,
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

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_NO_INLINE_DISTINCT_WHERE_1);
		}

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3_SQL);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2_SQL);
		}

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_NO_INLINE_DISTINCT_WHERE_2);
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
				sb.append(ClientExtensionEntryModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(ClientExtensionEntryModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), ClientExtensionEntry.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(
				_FILTER_ENTITY_ALIAS, ClientExtensionEntryImpl.class);
		}
		else {
			sqlQuery.addEntity(
				_FILTER_ENTITY_TABLE, ClientExtensionEntryImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						clientExtensionEntry)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ClientExtensionEntry> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the client extension entries where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (ClientExtensionEntry clientExtensionEntry :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(clientExtensionEntry);
		}
	}

	/**
	 * Returns the number of client extension entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching client extension entries
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_CLIENTEXTENSIONENTRY_WHERE);

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

	/**
	 * Returns the number of client extension entries that the user has permission to view where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching client extension entries that the user has permission to view
	 */
	@Override
	public int filterCountByUuid(String uuid) {
		if (!InlineSQLHelperUtil.isEnabled()) {
			return countByUuid(uuid);
		}

		uuid = Objects.toString(uuid, "");

		StringBundler sb = new StringBundler(2);

		sb.append(_FILTER_SQL_COUNT_CLIENTEXTENSIONENTRY_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3_SQL);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2_SQL);
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), ClientExtensionEntry.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			if (bindUuid) {
				queryPos.add(uuid);
			}

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

	private static final String _FINDER_COLUMN_UUID_UUID_2 =
		"clientExtensionEntry.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(clientExtensionEntry.uuid IS NULL OR clientExtensionEntry.uuid = '')";

	private static final String _FINDER_COLUMN_UUID_UUID_2_SQL =
		"clientExtensionEntry.uuid_ = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3_SQL =
		"(clientExtensionEntry.uuid_ IS NULL OR clientExtensionEntry.uuid_ = '')";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the client extension entries where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching client extension entries
	 */
	@Override
	public List<ClientExtensionEntry> findByUuid_C(
		String uuid, long companyId) {

		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the client extension entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @return the range of matching client extension entries
	 */
	@Override
	public List<ClientExtensionEntry> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the client extension entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching client extension entries
	 */
	@Override
	public List<ClientExtensionEntry> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<ClientExtensionEntry> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the client extension entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching client extension entries
	 */
	@Override
	public List<ClientExtensionEntry> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<ClientExtensionEntry> orderByComparator,
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

		List<ClientExtensionEntry> list = null;

		if (useFinderCache) {
			list = (List<ClientExtensionEntry>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (ClientExtensionEntry clientExtensionEntry : list) {
					if (!uuid.equals(clientExtensionEntry.getUuid()) ||
						(companyId != clientExtensionEntry.getCompanyId())) {

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

			sb.append(_SQL_SELECT_CLIENTEXTENSIONENTRY_WHERE);

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
				sb.append(ClientExtensionEntryModelImpl.ORDER_BY_JPQL);
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

				list = (List<ClientExtensionEntry>)QueryUtil.list(
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
	 * Returns the first client extension entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching client extension entry
	 * @throws NoSuchClientExtensionEntryException if a matching client extension entry could not be found
	 */
	@Override
	public ClientExtensionEntry findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<ClientExtensionEntry> orderByComparator)
		throws NoSuchClientExtensionEntryException {

		ClientExtensionEntry clientExtensionEntry = fetchByUuid_C_First(
			uuid, companyId, orderByComparator);

		if (clientExtensionEntry != null) {
			return clientExtensionEntry;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchClientExtensionEntryException(sb.toString());
	}

	/**
	 * Returns the first client extension entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching client extension entry, or <code>null</code> if a matching client extension entry could not be found
	 */
	@Override
	public ClientExtensionEntry fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<ClientExtensionEntry> orderByComparator) {

		List<ClientExtensionEntry> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last client extension entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching client extension entry
	 * @throws NoSuchClientExtensionEntryException if a matching client extension entry could not be found
	 */
	@Override
	public ClientExtensionEntry findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<ClientExtensionEntry> orderByComparator)
		throws NoSuchClientExtensionEntryException {

		ClientExtensionEntry clientExtensionEntry = fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);

		if (clientExtensionEntry != null) {
			return clientExtensionEntry;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchClientExtensionEntryException(sb.toString());
	}

	/**
	 * Returns the last client extension entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching client extension entry, or <code>null</code> if a matching client extension entry could not be found
	 */
	@Override
	public ClientExtensionEntry fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<ClientExtensionEntry> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<ClientExtensionEntry> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the client extension entries before and after the current client extension entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param clientExtensionEntryId the primary key of the current client extension entry
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next client extension entry
	 * @throws NoSuchClientExtensionEntryException if a client extension entry with the primary key could not be found
	 */
	@Override
	public ClientExtensionEntry[] findByUuid_C_PrevAndNext(
			long clientExtensionEntryId, String uuid, long companyId,
			OrderByComparator<ClientExtensionEntry> orderByComparator)
		throws NoSuchClientExtensionEntryException {

		uuid = Objects.toString(uuid, "");

		ClientExtensionEntry clientExtensionEntry = findByPrimaryKey(
			clientExtensionEntryId);

		Session session = null;

		try {
			session = openSession();

			ClientExtensionEntry[] array = new ClientExtensionEntryImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, clientExtensionEntry, uuid, companyId,
				orderByComparator, true);

			array[1] = clientExtensionEntry;

			array[2] = getByUuid_C_PrevAndNext(
				session, clientExtensionEntry, uuid, companyId,
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

	protected ClientExtensionEntry getByUuid_C_PrevAndNext(
		Session session, ClientExtensionEntry clientExtensionEntry, String uuid,
		long companyId,
		OrderByComparator<ClientExtensionEntry> orderByComparator,
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

		sb.append(_SQL_SELECT_CLIENTEXTENSIONENTRY_WHERE);

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
			sb.append(ClientExtensionEntryModelImpl.ORDER_BY_JPQL);
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
						clientExtensionEntry)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ClientExtensionEntry> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the client extension entries that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching client extension entries that the user has permission to view
	 */
	@Override
	public List<ClientExtensionEntry> filterFindByUuid_C(
		String uuid, long companyId) {

		return filterFindByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the client extension entries that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @return the range of matching client extension entries that the user has permission to view
	 */
	@Override
	public List<ClientExtensionEntry> filterFindByUuid_C(
		String uuid, long companyId, int start, int end) {

		return filterFindByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the client extension entries that the user has permissions to view where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching client extension entries that the user has permission to view
	 */
	@Override
	public List<ClientExtensionEntry> filterFindByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<ClientExtensionEntry> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return findByUuid_C(uuid, companyId, start, end, orderByComparator);
		}

		uuid = Objects.toString(uuid, "");

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(5);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_NO_INLINE_DISTINCT_WHERE_1);
		}

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3_SQL);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2_SQL);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_NO_INLINE_DISTINCT_WHERE_2);
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
				sb.append(ClientExtensionEntryModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(ClientExtensionEntryModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), ClientExtensionEntry.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, ClientExtensionEntryImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, ClientExtensionEntryImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			if (bindUuid) {
				queryPos.add(uuid);
			}

			queryPos.add(companyId);

			return (List<ClientExtensionEntry>)QueryUtil.list(
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
	 * Returns the client extension entries before and after the current client extension entry in the ordered set of client extension entries that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param clientExtensionEntryId the primary key of the current client extension entry
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next client extension entry
	 * @throws NoSuchClientExtensionEntryException if a client extension entry with the primary key could not be found
	 */
	@Override
	public ClientExtensionEntry[] filterFindByUuid_C_PrevAndNext(
			long clientExtensionEntryId, String uuid, long companyId,
			OrderByComparator<ClientExtensionEntry> orderByComparator)
		throws NoSuchClientExtensionEntryException {

		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return findByUuid_C_PrevAndNext(
				clientExtensionEntryId, uuid, companyId, orderByComparator);
		}

		uuid = Objects.toString(uuid, "");

		ClientExtensionEntry clientExtensionEntry = findByPrimaryKey(
			clientExtensionEntryId);

		Session session = null;

		try {
			session = openSession();

			ClientExtensionEntry[] array = new ClientExtensionEntryImpl[3];

			array[0] = filterGetByUuid_C_PrevAndNext(
				session, clientExtensionEntry, uuid, companyId,
				orderByComparator, true);

			array[1] = clientExtensionEntry;

			array[2] = filterGetByUuid_C_PrevAndNext(
				session, clientExtensionEntry, uuid, companyId,
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

	protected ClientExtensionEntry filterGetByUuid_C_PrevAndNext(
		Session session, ClientExtensionEntry clientExtensionEntry, String uuid,
		long companyId,
		OrderByComparator<ClientExtensionEntry> orderByComparator,
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
			sb.append(_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_NO_INLINE_DISTINCT_WHERE_1);
		}

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3_SQL);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2_SQL);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_NO_INLINE_DISTINCT_WHERE_2);
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
				sb.append(ClientExtensionEntryModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(ClientExtensionEntryModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), ClientExtensionEntry.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(
				_FILTER_ENTITY_ALIAS, ClientExtensionEntryImpl.class);
		}
		else {
			sqlQuery.addEntity(
				_FILTER_ENTITY_TABLE, ClientExtensionEntryImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						clientExtensionEntry)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<ClientExtensionEntry> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the client extension entries where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (ClientExtensionEntry clientExtensionEntry :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(clientExtensionEntry);
		}
	}

	/**
	 * Returns the number of client extension entries where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching client extension entries
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid_C;

		Object[] finderArgs = new Object[] {uuid, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_CLIENTEXTENSIONENTRY_WHERE);

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

	/**
	 * Returns the number of client extension entries that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching client extension entries that the user has permission to view
	 */
	@Override
	public int filterCountByUuid_C(String uuid, long companyId) {
		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return countByUuid_C(uuid, companyId);
		}

		uuid = Objects.toString(uuid, "");

		StringBundler sb = new StringBundler(3);

		sb.append(_FILTER_SQL_COUNT_CLIENTEXTENSIONENTRY_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3_SQL);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2_SQL);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), ClientExtensionEntry.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			if (bindUuid) {
				queryPos.add(uuid);
			}

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

	private static final String _FINDER_COLUMN_UUID_C_UUID_2 =
		"clientExtensionEntry.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(clientExtensionEntry.uuid IS NULL OR clientExtensionEntry.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_2_SQL =
		"clientExtensionEntry.uuid_ = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3_SQL =
		"(clientExtensionEntry.uuid_ IS NULL OR clientExtensionEntry.uuid_ = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"clientExtensionEntry.companyId = ?";

	private FinderPath _finderPathFetchByC_ERC;
	private FinderPath _finderPathCountByC_ERC;

	/**
	 * Returns the client extension entry where companyId = &#63; and externalReferenceCode = &#63; or throws a <code>NoSuchClientExtensionEntryException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching client extension entry
	 * @throws NoSuchClientExtensionEntryException if a matching client extension entry could not be found
	 */
	@Override
	public ClientExtensionEntry findByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchClientExtensionEntryException {

		ClientExtensionEntry clientExtensionEntry = fetchByC_ERC(
			companyId, externalReferenceCode);

		if (clientExtensionEntry == null) {
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

			throw new NoSuchClientExtensionEntryException(sb.toString());
		}

		return clientExtensionEntry;
	}

	/**
	 * Returns the client extension entry where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching client extension entry, or <code>null</code> if a matching client extension entry could not be found
	 */
	@Override
	public ClientExtensionEntry fetchByC_ERC(
		long companyId, String externalReferenceCode) {

		return fetchByC_ERC(companyId, externalReferenceCode, true);
	}

	/**
	 * Returns the client extension entry where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching client extension entry, or <code>null</code> if a matching client extension entry could not be found
	 */
	@Override
	public ClientExtensionEntry fetchByC_ERC(
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

		if (result instanceof ClientExtensionEntry) {
			ClientExtensionEntry clientExtensionEntry =
				(ClientExtensionEntry)result;

			if ((companyId != clientExtensionEntry.getCompanyId()) ||
				!Objects.equals(
					externalReferenceCode,
					clientExtensionEntry.getExternalReferenceCode())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_CLIENTEXTENSIONENTRY_WHERE);

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

				List<ClientExtensionEntry> list = query.list();

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
								"ClientExtensionEntryPersistenceImpl.fetchByC_ERC(long, String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					ClientExtensionEntry clientExtensionEntry = list.get(0);

					result = clientExtensionEntry;

					cacheResult(clientExtensionEntry);
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
			return (ClientExtensionEntry)result;
		}
	}

	/**
	 * Removes the client extension entry where companyId = &#63; and externalReferenceCode = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the client extension entry that was removed
	 */
	@Override
	public ClientExtensionEntry removeByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchClientExtensionEntryException {

		ClientExtensionEntry clientExtensionEntry = findByC_ERC(
			companyId, externalReferenceCode);

		return remove(clientExtensionEntry);
	}

	/**
	 * Returns the number of client extension entries where companyId = &#63; and externalReferenceCode = &#63;.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the number of matching client extension entries
	 */
	@Override
	public int countByC_ERC(long companyId, String externalReferenceCode) {
		externalReferenceCode = Objects.toString(externalReferenceCode, "");

		FinderPath finderPath = _finderPathCountByC_ERC;

		Object[] finderArgs = new Object[] {companyId, externalReferenceCode};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_CLIENTEXTENSIONENTRY_WHERE);

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
		"clientExtensionEntry.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_2 =
		"clientExtensionEntry.externalReferenceCode = ?";

	private static final String _FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_3 =
		"(clientExtensionEntry.externalReferenceCode IS NULL OR clientExtensionEntry.externalReferenceCode = '')";

	public ClientExtensionEntryPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");
		dbColumnNames.put("type", "type_");

		setDBColumnNames(dbColumnNames);

		setModelClass(ClientExtensionEntry.class);

		setModelImplClass(ClientExtensionEntryImpl.class);
		setModelPKClass(long.class);

		setTable(ClientExtensionEntryTable.INSTANCE);
	}

	/**
	 * Caches the client extension entry in the entity cache if it is enabled.
	 *
	 * @param clientExtensionEntry the client extension entry
	 */
	@Override
	public void cacheResult(ClientExtensionEntry clientExtensionEntry) {
		entityCache.putResult(
			ClientExtensionEntryImpl.class,
			clientExtensionEntry.getPrimaryKey(), clientExtensionEntry);

		finderCache.putResult(
			_finderPathFetchByC_ERC,
			new Object[] {
				clientExtensionEntry.getCompanyId(),
				clientExtensionEntry.getExternalReferenceCode()
			},
			clientExtensionEntry);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the client extension entries in the entity cache if it is enabled.
	 *
	 * @param clientExtensionEntries the client extension entries
	 */
	@Override
	public void cacheResult(List<ClientExtensionEntry> clientExtensionEntries) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (clientExtensionEntries.size() >
				 _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (ClientExtensionEntry clientExtensionEntry :
				clientExtensionEntries) {

			if (entityCache.getResult(
					ClientExtensionEntryImpl.class,
					clientExtensionEntry.getPrimaryKey()) == null) {

				cacheResult(clientExtensionEntry);
			}
		}
	}

	/**
	 * Clears the cache for all client extension entries.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(ClientExtensionEntryImpl.class);

		finderCache.clearCache(ClientExtensionEntryImpl.class);
	}

	/**
	 * Clears the cache for the client extension entry.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(ClientExtensionEntry clientExtensionEntry) {
		entityCache.removeResult(
			ClientExtensionEntryImpl.class, clientExtensionEntry);
	}

	@Override
	public void clearCache(List<ClientExtensionEntry> clientExtensionEntries) {
		for (ClientExtensionEntry clientExtensionEntry :
				clientExtensionEntries) {

			entityCache.removeResult(
				ClientExtensionEntryImpl.class, clientExtensionEntry);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(ClientExtensionEntryImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				ClientExtensionEntryImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		ClientExtensionEntryModelImpl clientExtensionEntryModelImpl) {

		Object[] args = new Object[] {
			clientExtensionEntryModelImpl.getCompanyId(),
			clientExtensionEntryModelImpl.getExternalReferenceCode()
		};

		finderCache.putResult(_finderPathCountByC_ERC, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByC_ERC, args, clientExtensionEntryModelImpl);
	}

	/**
	 * Creates a new client extension entry with the primary key. Does not add the client extension entry to the database.
	 *
	 * @param clientExtensionEntryId the primary key for the new client extension entry
	 * @return the new client extension entry
	 */
	@Override
	public ClientExtensionEntry create(long clientExtensionEntryId) {
		ClientExtensionEntry clientExtensionEntry =
			new ClientExtensionEntryImpl();

		clientExtensionEntry.setNew(true);
		clientExtensionEntry.setPrimaryKey(clientExtensionEntryId);

		String uuid = _portalUUID.generate();

		clientExtensionEntry.setUuid(uuid);

		clientExtensionEntry.setCompanyId(CompanyThreadLocal.getCompanyId());

		return clientExtensionEntry;
	}

	/**
	 * Removes the client extension entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param clientExtensionEntryId the primary key of the client extension entry
	 * @return the client extension entry that was removed
	 * @throws NoSuchClientExtensionEntryException if a client extension entry with the primary key could not be found
	 */
	@Override
	public ClientExtensionEntry remove(long clientExtensionEntryId)
		throws NoSuchClientExtensionEntryException {

		return remove((Serializable)clientExtensionEntryId);
	}

	/**
	 * Removes the client extension entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the client extension entry
	 * @return the client extension entry that was removed
	 * @throws NoSuchClientExtensionEntryException if a client extension entry with the primary key could not be found
	 */
	@Override
	public ClientExtensionEntry remove(Serializable primaryKey)
		throws NoSuchClientExtensionEntryException {

		Session session = null;

		try {
			session = openSession();

			ClientExtensionEntry clientExtensionEntry =
				(ClientExtensionEntry)session.get(
					ClientExtensionEntryImpl.class, primaryKey);

			if (clientExtensionEntry == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchClientExtensionEntryException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(clientExtensionEntry);
		}
		catch (NoSuchClientExtensionEntryException noSuchEntityException) {
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
	protected ClientExtensionEntry removeImpl(
		ClientExtensionEntry clientExtensionEntry) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(clientExtensionEntry)) {
				clientExtensionEntry = (ClientExtensionEntry)session.get(
					ClientExtensionEntryImpl.class,
					clientExtensionEntry.getPrimaryKeyObj());
			}

			if (clientExtensionEntry != null) {
				session.delete(clientExtensionEntry);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (clientExtensionEntry != null) {
			clearCache(clientExtensionEntry);
		}

		return clientExtensionEntry;
	}

	@Override
	public ClientExtensionEntry updateImpl(
		ClientExtensionEntry clientExtensionEntry) {

		boolean isNew = clientExtensionEntry.isNew();

		if (!(clientExtensionEntry instanceof ClientExtensionEntryModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(clientExtensionEntry.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					clientExtensionEntry);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in clientExtensionEntry proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom ClientExtensionEntry implementation " +
					clientExtensionEntry.getClass());
		}

		ClientExtensionEntryModelImpl clientExtensionEntryModelImpl =
			(ClientExtensionEntryModelImpl)clientExtensionEntry;

		if (Validator.isNull(clientExtensionEntry.getUuid())) {
			String uuid = _portalUUID.generate();

			clientExtensionEntry.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (clientExtensionEntry.getCreateDate() == null)) {
			if (serviceContext == null) {
				clientExtensionEntry.setCreateDate(date);
			}
			else {
				clientExtensionEntry.setCreateDate(
					serviceContext.getCreateDate(date));
			}
		}

		if (!clientExtensionEntryModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				clientExtensionEntry.setModifiedDate(date);
			}
			else {
				clientExtensionEntry.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		long userId = GetterUtil.getLong(PrincipalThreadLocal.getName());

		if (userId > 0) {
			long companyId = clientExtensionEntry.getCompanyId();

			long groupId = 0;

			long clientExtensionEntryId = 0;

			if (!isNew) {
				clientExtensionEntryId = clientExtensionEntry.getPrimaryKey();
			}

			try {
				clientExtensionEntry.setDescription(
					SanitizerUtil.sanitize(
						companyId, groupId, userId,
						ClientExtensionEntry.class.getName(),
						clientExtensionEntryId, ContentTypes.TEXT_HTML,
						Sanitizer.MODE_ALL,
						clientExtensionEntry.getDescription(), null));
			}
			catch (SanitizerException sanitizerException) {
				throw new SystemException(sanitizerException);
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(clientExtensionEntry);
			}
			else {
				clientExtensionEntry = (ClientExtensionEntry)session.merge(
					clientExtensionEntry);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			ClientExtensionEntryImpl.class, clientExtensionEntryModelImpl,
			false, true);

		cacheUniqueFindersCache(clientExtensionEntryModelImpl);

		if (isNew) {
			clientExtensionEntry.setNew(false);
		}

		clientExtensionEntry.resetOriginalValues();

		return clientExtensionEntry;
	}

	/**
	 * Returns the client extension entry with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the client extension entry
	 * @return the client extension entry
	 * @throws NoSuchClientExtensionEntryException if a client extension entry with the primary key could not be found
	 */
	@Override
	public ClientExtensionEntry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchClientExtensionEntryException {

		ClientExtensionEntry clientExtensionEntry = fetchByPrimaryKey(
			primaryKey);

		if (clientExtensionEntry == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchClientExtensionEntryException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return clientExtensionEntry;
	}

	/**
	 * Returns the client extension entry with the primary key or throws a <code>NoSuchClientExtensionEntryException</code> if it could not be found.
	 *
	 * @param clientExtensionEntryId the primary key of the client extension entry
	 * @return the client extension entry
	 * @throws NoSuchClientExtensionEntryException if a client extension entry with the primary key could not be found
	 */
	@Override
	public ClientExtensionEntry findByPrimaryKey(long clientExtensionEntryId)
		throws NoSuchClientExtensionEntryException {

		return findByPrimaryKey((Serializable)clientExtensionEntryId);
	}

	/**
	 * Returns the client extension entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param clientExtensionEntryId the primary key of the client extension entry
	 * @return the client extension entry, or <code>null</code> if a client extension entry with the primary key could not be found
	 */
	@Override
	public ClientExtensionEntry fetchByPrimaryKey(long clientExtensionEntryId) {
		return fetchByPrimaryKey((Serializable)clientExtensionEntryId);
	}

	/**
	 * Returns all the client extension entries.
	 *
	 * @return the client extension entries
	 */
	@Override
	public List<ClientExtensionEntry> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the client extension entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @return the range of client extension entries
	 */
	@Override
	public List<ClientExtensionEntry> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the client extension entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of client extension entries
	 */
	@Override
	public List<ClientExtensionEntry> findAll(
		int start, int end,
		OrderByComparator<ClientExtensionEntry> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the client extension entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ClientExtensionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of client extension entries
	 * @param end the upper bound of the range of client extension entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of client extension entries
	 */
	@Override
	public List<ClientExtensionEntry> findAll(
		int start, int end,
		OrderByComparator<ClientExtensionEntry> orderByComparator,
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

		List<ClientExtensionEntry> list = null;

		if (useFinderCache) {
			list = (List<ClientExtensionEntry>)finderCache.getResult(
				finderPath, finderArgs);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_CLIENTEXTENSIONENTRY);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_CLIENTEXTENSIONENTRY;

				sql = sql.concat(ClientExtensionEntryModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<ClientExtensionEntry>)QueryUtil.list(
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
	 * Removes all the client extension entries from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (ClientExtensionEntry clientExtensionEntry : findAll()) {
			remove(clientExtensionEntry);
		}
	}

	/**
	 * Returns the number of client extension entries.
	 *
	 * @return the number of client extension entries
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
					_SQL_COUNT_CLIENTEXTENSIONENTRY);

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
		return "clientExtensionEntryId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_CLIENTEXTENSIONENTRY;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return ClientExtensionEntryModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the client extension entry persistence.
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

		_finderPathFetchByC_ERC = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByC_ERC",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"companyId", "externalReferenceCode"}, true);

		_finderPathCountByC_ERC = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_ERC",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"companyId", "externalReferenceCode"}, false);

		_setClientExtensionEntryUtilPersistence(this);
	}

	@Deactivate
	public void deactivate() {
		_setClientExtensionEntryUtilPersistence(null);

		entityCache.removeCache(ClientExtensionEntryImpl.class.getName());
	}

	private void _setClientExtensionEntryUtilPersistence(
		ClientExtensionEntryPersistence clientExtensionEntryPersistence) {

		try {
			Field field = ClientExtensionEntryUtil.class.getDeclaredField(
				"_persistence");

			field.setAccessible(true);

			field.set(null, clientExtensionEntryPersistence);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Override
	@Reference(
		target = ClientExtensionPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = ClientExtensionPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = ClientExtensionPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_CLIENTEXTENSIONENTRY =
		"SELECT clientExtensionEntry FROM ClientExtensionEntry clientExtensionEntry";

	private static final String _SQL_SELECT_CLIENTEXTENSIONENTRY_WHERE =
		"SELECT clientExtensionEntry FROM ClientExtensionEntry clientExtensionEntry WHERE ";

	private static final String _SQL_COUNT_CLIENTEXTENSIONENTRY =
		"SELECT COUNT(clientExtensionEntry) FROM ClientExtensionEntry clientExtensionEntry";

	private static final String _SQL_COUNT_CLIENTEXTENSIONENTRY_WHERE =
		"SELECT COUNT(clientExtensionEntry) FROM ClientExtensionEntry clientExtensionEntry WHERE ";

	private static final String _FILTER_ENTITY_TABLE_FILTER_PK_COLUMN =
		"clientExtensionEntry.clientExtensionEntryId";

	private static final String _FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_WHERE =
		"SELECT DISTINCT {clientExtensionEntry.*} FROM ClientExtensionEntry clientExtensionEntry WHERE ";

	private static final String
		_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_NO_INLINE_DISTINCT_WHERE_1 =
			"SELECT {ClientExtensionEntry.*} FROM (SELECT DISTINCT clientExtensionEntry.clientExtensionEntryId FROM ClientExtensionEntry clientExtensionEntry WHERE ";

	private static final String
		_FILTER_SQL_SELECT_CLIENTEXTENSIONENTRY_NO_INLINE_DISTINCT_WHERE_2 =
			") TEMP_TABLE INNER JOIN ClientExtensionEntry ON TEMP_TABLE.clientExtensionEntryId = ClientExtensionEntry.clientExtensionEntryId";

	private static final String _FILTER_SQL_COUNT_CLIENTEXTENSIONENTRY_WHERE =
		"SELECT COUNT(DISTINCT clientExtensionEntry.clientExtensionEntryId) AS COUNT_VALUE FROM ClientExtensionEntry clientExtensionEntry WHERE ";

	private static final String _FILTER_ENTITY_ALIAS = "clientExtensionEntry";

	private static final String _FILTER_ENTITY_TABLE = "ClientExtensionEntry";

	private static final String _ORDER_BY_ENTITY_ALIAS =
		"clientExtensionEntry.";

	private static final String _ORDER_BY_ENTITY_TABLE =
		"ClientExtensionEntry.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No ClientExtensionEntry exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No ClientExtensionEntry exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		ClientExtensionEntryPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid", "type"});

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

	@Reference
	private PortalUUID _portalUUID;

	@Reference
	private ClientExtensionEntryModelArgumentsResolver
		_clientExtensionEntryModelArgumentsResolver;

}