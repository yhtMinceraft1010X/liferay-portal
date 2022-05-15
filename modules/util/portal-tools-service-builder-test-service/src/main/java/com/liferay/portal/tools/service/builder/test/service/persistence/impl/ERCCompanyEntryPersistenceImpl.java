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

package com.liferay.portal.tools.service.builder.test.service.persistence.impl;

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
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;
import com.liferay.portal.tools.service.builder.test.exception.NoSuchERCCompanyEntryException;
import com.liferay.portal.tools.service.builder.test.model.ERCCompanyEntry;
import com.liferay.portal.tools.service.builder.test.model.ERCCompanyEntryTable;
import com.liferay.portal.tools.service.builder.test.model.impl.ERCCompanyEntryImpl;
import com.liferay.portal.tools.service.builder.test.model.impl.ERCCompanyEntryModelImpl;
import com.liferay.portal.tools.service.builder.test.service.persistence.ERCCompanyEntryPersistence;
import com.liferay.portal.tools.service.builder.test.service.persistence.ERCCompanyEntryUtil;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The persistence implementation for the erc company entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class ERCCompanyEntryPersistenceImpl
	extends BasePersistenceImpl<ERCCompanyEntry>
	implements ERCCompanyEntryPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>ERCCompanyEntryUtil</code> to access the erc company entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		ERCCompanyEntryImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathFetchByC_ERC;
	private FinderPath _finderPathCountByC_ERC;

	/**
	 * Returns the erc company entry where companyId = &#63; and externalReferenceCode = &#63; or throws a <code>NoSuchERCCompanyEntryException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching erc company entry
	 * @throws NoSuchERCCompanyEntryException if a matching erc company entry could not be found
	 */
	@Override
	public ERCCompanyEntry findByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchERCCompanyEntryException {

		ERCCompanyEntry ercCompanyEntry = fetchByC_ERC(
			companyId, externalReferenceCode);

		if (ercCompanyEntry == null) {
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

			throw new NoSuchERCCompanyEntryException(sb.toString());
		}

		return ercCompanyEntry;
	}

	/**
	 * Returns the erc company entry where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching erc company entry, or <code>null</code> if a matching erc company entry could not be found
	 */
	@Override
	public ERCCompanyEntry fetchByC_ERC(
		long companyId, String externalReferenceCode) {

		return fetchByC_ERC(companyId, externalReferenceCode, true);
	}

	/**
	 * Returns the erc company entry where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching erc company entry, or <code>null</code> if a matching erc company entry could not be found
	 */
	@Override
	public ERCCompanyEntry fetchByC_ERC(
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

		if (result instanceof ERCCompanyEntry) {
			ERCCompanyEntry ercCompanyEntry = (ERCCompanyEntry)result;

			if ((companyId != ercCompanyEntry.getCompanyId()) ||
				!Objects.equals(
					externalReferenceCode,
					ercCompanyEntry.getExternalReferenceCode())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_ERCCOMPANYENTRY_WHERE);

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

				List<ERCCompanyEntry> list = query.list();

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
								"ERCCompanyEntryPersistenceImpl.fetchByC_ERC(long, String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					ERCCompanyEntry ercCompanyEntry = list.get(0);

					result = ercCompanyEntry;

					cacheResult(ercCompanyEntry);
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
			return (ERCCompanyEntry)result;
		}
	}

	/**
	 * Removes the erc company entry where companyId = &#63; and externalReferenceCode = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the erc company entry that was removed
	 */
	@Override
	public ERCCompanyEntry removeByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchERCCompanyEntryException {

		ERCCompanyEntry ercCompanyEntry = findByC_ERC(
			companyId, externalReferenceCode);

		return remove(ercCompanyEntry);
	}

	/**
	 * Returns the number of erc company entries where companyId = &#63; and externalReferenceCode = &#63;.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the number of matching erc company entries
	 */
	@Override
	public int countByC_ERC(long companyId, String externalReferenceCode) {
		externalReferenceCode = Objects.toString(externalReferenceCode, "");

		FinderPath finderPath = _finderPathCountByC_ERC;

		Object[] finderArgs = new Object[] {companyId, externalReferenceCode};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_ERCCOMPANYENTRY_WHERE);

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
		"ercCompanyEntry.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_2 =
		"ercCompanyEntry.externalReferenceCode = ?";

	private static final String _FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_3 =
		"(ercCompanyEntry.externalReferenceCode IS NULL OR ercCompanyEntry.externalReferenceCode = '')";

	public ERCCompanyEntryPersistenceImpl() {
		setModelClass(ERCCompanyEntry.class);

		setModelImplClass(ERCCompanyEntryImpl.class);
		setModelPKClass(long.class);

		setTable(ERCCompanyEntryTable.INSTANCE);
	}

	/**
	 * Caches the erc company entry in the entity cache if it is enabled.
	 *
	 * @param ercCompanyEntry the erc company entry
	 */
	@Override
	public void cacheResult(ERCCompanyEntry ercCompanyEntry) {
		entityCache.putResult(
			ERCCompanyEntryImpl.class, ercCompanyEntry.getPrimaryKey(),
			ercCompanyEntry);

		finderCache.putResult(
			_finderPathFetchByC_ERC,
			new Object[] {
				ercCompanyEntry.getCompanyId(),
				ercCompanyEntry.getExternalReferenceCode()
			},
			ercCompanyEntry);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the erc company entries in the entity cache if it is enabled.
	 *
	 * @param ercCompanyEntries the erc company entries
	 */
	@Override
	public void cacheResult(List<ERCCompanyEntry> ercCompanyEntries) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (ercCompanyEntries.size() >
				 _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (ERCCompanyEntry ercCompanyEntry : ercCompanyEntries) {
			if (entityCache.getResult(
					ERCCompanyEntryImpl.class,
					ercCompanyEntry.getPrimaryKey()) == null) {

				cacheResult(ercCompanyEntry);
			}
		}
	}

	/**
	 * Clears the cache for all erc company entries.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(ERCCompanyEntryImpl.class);

		finderCache.clearCache(ERCCompanyEntryImpl.class);
	}

	/**
	 * Clears the cache for the erc company entry.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(ERCCompanyEntry ercCompanyEntry) {
		entityCache.removeResult(ERCCompanyEntryImpl.class, ercCompanyEntry);
	}

	@Override
	public void clearCache(List<ERCCompanyEntry> ercCompanyEntries) {
		for (ERCCompanyEntry ercCompanyEntry : ercCompanyEntries) {
			entityCache.removeResult(
				ERCCompanyEntryImpl.class, ercCompanyEntry);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(ERCCompanyEntryImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(ERCCompanyEntryImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		ERCCompanyEntryModelImpl ercCompanyEntryModelImpl) {

		Object[] args = new Object[] {
			ercCompanyEntryModelImpl.getCompanyId(),
			ercCompanyEntryModelImpl.getExternalReferenceCode()
		};

		finderCache.putResult(_finderPathCountByC_ERC, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByC_ERC, args, ercCompanyEntryModelImpl);
	}

	/**
	 * Creates a new erc company entry with the primary key. Does not add the erc company entry to the database.
	 *
	 * @param ercCompanyEntryId the primary key for the new erc company entry
	 * @return the new erc company entry
	 */
	@Override
	public ERCCompanyEntry create(long ercCompanyEntryId) {
		ERCCompanyEntry ercCompanyEntry = new ERCCompanyEntryImpl();

		ercCompanyEntry.setNew(true);
		ercCompanyEntry.setPrimaryKey(ercCompanyEntryId);

		ercCompanyEntry.setCompanyId(CompanyThreadLocal.getCompanyId());

		return ercCompanyEntry;
	}

	/**
	 * Removes the erc company entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param ercCompanyEntryId the primary key of the erc company entry
	 * @return the erc company entry that was removed
	 * @throws NoSuchERCCompanyEntryException if a erc company entry with the primary key could not be found
	 */
	@Override
	public ERCCompanyEntry remove(long ercCompanyEntryId)
		throws NoSuchERCCompanyEntryException {

		return remove((Serializable)ercCompanyEntryId);
	}

	/**
	 * Removes the erc company entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the erc company entry
	 * @return the erc company entry that was removed
	 * @throws NoSuchERCCompanyEntryException if a erc company entry with the primary key could not be found
	 */
	@Override
	public ERCCompanyEntry remove(Serializable primaryKey)
		throws NoSuchERCCompanyEntryException {

		Session session = null;

		try {
			session = openSession();

			ERCCompanyEntry ercCompanyEntry = (ERCCompanyEntry)session.get(
				ERCCompanyEntryImpl.class, primaryKey);

			if (ercCompanyEntry == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchERCCompanyEntryException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(ercCompanyEntry);
		}
		catch (NoSuchERCCompanyEntryException noSuchEntityException) {
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
	protected ERCCompanyEntry removeImpl(ERCCompanyEntry ercCompanyEntry) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(ercCompanyEntry)) {
				ercCompanyEntry = (ERCCompanyEntry)session.get(
					ERCCompanyEntryImpl.class,
					ercCompanyEntry.getPrimaryKeyObj());
			}

			if (ercCompanyEntry != null) {
				session.delete(ercCompanyEntry);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (ercCompanyEntry != null) {
			clearCache(ercCompanyEntry);
		}

		return ercCompanyEntry;
	}

	@Override
	public ERCCompanyEntry updateImpl(ERCCompanyEntry ercCompanyEntry) {
		boolean isNew = ercCompanyEntry.isNew();

		if (!(ercCompanyEntry instanceof ERCCompanyEntryModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(ercCompanyEntry.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					ercCompanyEntry);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in ercCompanyEntry proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom ERCCompanyEntry implementation " +
					ercCompanyEntry.getClass());
		}

		ERCCompanyEntryModelImpl ercCompanyEntryModelImpl =
			(ERCCompanyEntryModelImpl)ercCompanyEntry;

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(ercCompanyEntry);
			}
			else {
				ercCompanyEntry = (ERCCompanyEntry)session.merge(
					ercCompanyEntry);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			ERCCompanyEntryImpl.class, ercCompanyEntryModelImpl, false, true);

		cacheUniqueFindersCache(ercCompanyEntryModelImpl);

		if (isNew) {
			ercCompanyEntry.setNew(false);
		}

		ercCompanyEntry.resetOriginalValues();

		return ercCompanyEntry;
	}

	/**
	 * Returns the erc company entry with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the erc company entry
	 * @return the erc company entry
	 * @throws NoSuchERCCompanyEntryException if a erc company entry with the primary key could not be found
	 */
	@Override
	public ERCCompanyEntry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchERCCompanyEntryException {

		ERCCompanyEntry ercCompanyEntry = fetchByPrimaryKey(primaryKey);

		if (ercCompanyEntry == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchERCCompanyEntryException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return ercCompanyEntry;
	}

	/**
	 * Returns the erc company entry with the primary key or throws a <code>NoSuchERCCompanyEntryException</code> if it could not be found.
	 *
	 * @param ercCompanyEntryId the primary key of the erc company entry
	 * @return the erc company entry
	 * @throws NoSuchERCCompanyEntryException if a erc company entry with the primary key could not be found
	 */
	@Override
	public ERCCompanyEntry findByPrimaryKey(long ercCompanyEntryId)
		throws NoSuchERCCompanyEntryException {

		return findByPrimaryKey((Serializable)ercCompanyEntryId);
	}

	/**
	 * Returns the erc company entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param ercCompanyEntryId the primary key of the erc company entry
	 * @return the erc company entry, or <code>null</code> if a erc company entry with the primary key could not be found
	 */
	@Override
	public ERCCompanyEntry fetchByPrimaryKey(long ercCompanyEntryId) {
		return fetchByPrimaryKey((Serializable)ercCompanyEntryId);
	}

	/**
	 * Returns all the erc company entries.
	 *
	 * @return the erc company entries
	 */
	@Override
	public List<ERCCompanyEntry> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the erc company entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ERCCompanyEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of erc company entries
	 * @param end the upper bound of the range of erc company entries (not inclusive)
	 * @return the range of erc company entries
	 */
	@Override
	public List<ERCCompanyEntry> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the erc company entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ERCCompanyEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of erc company entries
	 * @param end the upper bound of the range of erc company entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of erc company entries
	 */
	@Override
	public List<ERCCompanyEntry> findAll(
		int start, int end,
		OrderByComparator<ERCCompanyEntry> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the erc company entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>ERCCompanyEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of erc company entries
	 * @param end the upper bound of the range of erc company entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of erc company entries
	 */
	@Override
	public List<ERCCompanyEntry> findAll(
		int start, int end,
		OrderByComparator<ERCCompanyEntry> orderByComparator,
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

		List<ERCCompanyEntry> list = null;

		if (useFinderCache) {
			list = (List<ERCCompanyEntry>)finderCache.getResult(
				finderPath, finderArgs);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_ERCCOMPANYENTRY);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_ERCCOMPANYENTRY;

				sql = sql.concat(ERCCompanyEntryModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<ERCCompanyEntry>)QueryUtil.list(
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
	 * Removes all the erc company entries from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (ERCCompanyEntry ercCompanyEntry : findAll()) {
			remove(ercCompanyEntry);
		}
	}

	/**
	 * Returns the number of erc company entries.
	 *
	 * @return the number of erc company entries
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_ERCCOMPANYENTRY);

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
		return "ercCompanyEntryId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_ERCCOMPANYENTRY;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return ERCCompanyEntryModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the erc company entry persistence.
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

		_finderPathFetchByC_ERC = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByC_ERC",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"companyId", "externalReferenceCode"}, true);

		_finderPathCountByC_ERC = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_ERC",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"companyId", "externalReferenceCode"}, false);

		_setERCCompanyEntryUtilPersistence(this);
	}

	public void destroy() {
		_setERCCompanyEntryUtilPersistence(null);

		entityCache.removeCache(ERCCompanyEntryImpl.class.getName());
	}

	private void _setERCCompanyEntryUtilPersistence(
		ERCCompanyEntryPersistence ercCompanyEntryPersistence) {

		try {
			Field field = ERCCompanyEntryUtil.class.getDeclaredField(
				"_persistence");

			field.setAccessible(true);

			field.set(null, ercCompanyEntryPersistence);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;

	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_ERCCOMPANYENTRY =
		"SELECT ercCompanyEntry FROM ERCCompanyEntry ercCompanyEntry";

	private static final String _SQL_SELECT_ERCCOMPANYENTRY_WHERE =
		"SELECT ercCompanyEntry FROM ERCCompanyEntry ercCompanyEntry WHERE ";

	private static final String _SQL_COUNT_ERCCOMPANYENTRY =
		"SELECT COUNT(ercCompanyEntry) FROM ERCCompanyEntry ercCompanyEntry";

	private static final String _SQL_COUNT_ERCCOMPANYENTRY_WHERE =
		"SELECT COUNT(ercCompanyEntry) FROM ERCCompanyEntry ercCompanyEntry WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "ercCompanyEntry.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No ERCCompanyEntry exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No ERCCompanyEntry exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		ERCCompanyEntryPersistenceImpl.class);

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

}