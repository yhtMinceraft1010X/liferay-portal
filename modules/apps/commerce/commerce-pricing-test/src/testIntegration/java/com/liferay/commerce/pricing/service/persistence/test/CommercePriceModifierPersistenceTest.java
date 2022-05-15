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

package com.liferay.commerce.pricing.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.pricing.exception.NoSuchPriceModifierException;
import com.liferay.commerce.pricing.model.CommercePriceModifier;
import com.liferay.commerce.pricing.service.CommercePriceModifierLocalServiceUtil;
import com.liferay.commerce.pricing.service.persistence.CommercePriceModifierPersistence;
import com.liferay.commerce.pricing.service.persistence.CommercePriceModifierUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.test.AssertUtils;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @generated
 */
@RunWith(Arquillian.class)
public class CommercePriceModifierPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.commerce.pricing.service"));

	@Before
	public void setUp() {
		_persistence = CommercePriceModifierUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<CommercePriceModifier> iterator =
			_commercePriceModifiers.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		CommercePriceModifier commercePriceModifier = _persistence.create(pk);

		Assert.assertNotNull(commercePriceModifier);

		Assert.assertEquals(commercePriceModifier.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		CommercePriceModifier newCommercePriceModifier =
			addCommercePriceModifier();

		_persistence.remove(newCommercePriceModifier);

		CommercePriceModifier existingCommercePriceModifier =
			_persistence.fetchByPrimaryKey(
				newCommercePriceModifier.getPrimaryKey());

		Assert.assertNull(existingCommercePriceModifier);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addCommercePriceModifier();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		CommercePriceModifier newCommercePriceModifier = _persistence.create(
			pk);

		newCommercePriceModifier.setMvccVersion(RandomTestUtil.nextLong());

		newCommercePriceModifier.setCtCollectionId(RandomTestUtil.nextLong());

		newCommercePriceModifier.setUuid(RandomTestUtil.randomString());

		newCommercePriceModifier.setExternalReferenceCode(
			RandomTestUtil.randomString());

		newCommercePriceModifier.setGroupId(RandomTestUtil.nextLong());

		newCommercePriceModifier.setCompanyId(RandomTestUtil.nextLong());

		newCommercePriceModifier.setUserId(RandomTestUtil.nextLong());

		newCommercePriceModifier.setUserName(RandomTestUtil.randomString());

		newCommercePriceModifier.setCreateDate(RandomTestUtil.nextDate());

		newCommercePriceModifier.setModifiedDate(RandomTestUtil.nextDate());

		newCommercePriceModifier.setCommercePriceListId(
			RandomTestUtil.nextLong());

		newCommercePriceModifier.setTitle(RandomTestUtil.randomString());

		newCommercePriceModifier.setTarget(RandomTestUtil.randomString());

		newCommercePriceModifier.setModifierAmount(
			new BigDecimal(RandomTestUtil.nextDouble()));

		newCommercePriceModifier.setModifierType(RandomTestUtil.randomString());

		newCommercePriceModifier.setPriority(RandomTestUtil.nextDouble());

		newCommercePriceModifier.setActive(RandomTestUtil.randomBoolean());

		newCommercePriceModifier.setDisplayDate(RandomTestUtil.nextDate());

		newCommercePriceModifier.setExpirationDate(RandomTestUtil.nextDate());

		newCommercePriceModifier.setLastPublishDate(RandomTestUtil.nextDate());

		newCommercePriceModifier.setStatus(RandomTestUtil.nextInt());

		newCommercePriceModifier.setStatusByUserId(RandomTestUtil.nextLong());

		newCommercePriceModifier.setStatusByUserName(
			RandomTestUtil.randomString());

		newCommercePriceModifier.setStatusDate(RandomTestUtil.nextDate());

		_commercePriceModifiers.add(
			_persistence.update(newCommercePriceModifier));

		CommercePriceModifier existingCommercePriceModifier =
			_persistence.findByPrimaryKey(
				newCommercePriceModifier.getPrimaryKey());

		Assert.assertEquals(
			existingCommercePriceModifier.getMvccVersion(),
			newCommercePriceModifier.getMvccVersion());
		Assert.assertEquals(
			existingCommercePriceModifier.getCtCollectionId(),
			newCommercePriceModifier.getCtCollectionId());
		Assert.assertEquals(
			existingCommercePriceModifier.getUuid(),
			newCommercePriceModifier.getUuid());
		Assert.assertEquals(
			existingCommercePriceModifier.getExternalReferenceCode(),
			newCommercePriceModifier.getExternalReferenceCode());
		Assert.assertEquals(
			existingCommercePriceModifier.getCommercePriceModifierId(),
			newCommercePriceModifier.getCommercePriceModifierId());
		Assert.assertEquals(
			existingCommercePriceModifier.getGroupId(),
			newCommercePriceModifier.getGroupId());
		Assert.assertEquals(
			existingCommercePriceModifier.getCompanyId(),
			newCommercePriceModifier.getCompanyId());
		Assert.assertEquals(
			existingCommercePriceModifier.getUserId(),
			newCommercePriceModifier.getUserId());
		Assert.assertEquals(
			existingCommercePriceModifier.getUserName(),
			newCommercePriceModifier.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingCommercePriceModifier.getCreateDate()),
			Time.getShortTimestamp(newCommercePriceModifier.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingCommercePriceModifier.getModifiedDate()),
			Time.getShortTimestamp(newCommercePriceModifier.getModifiedDate()));
		Assert.assertEquals(
			existingCommercePriceModifier.getCommercePriceListId(),
			newCommercePriceModifier.getCommercePriceListId());
		Assert.assertEquals(
			existingCommercePriceModifier.getTitle(),
			newCommercePriceModifier.getTitle());
		Assert.assertEquals(
			existingCommercePriceModifier.getTarget(),
			newCommercePriceModifier.getTarget());
		Assert.assertEquals(
			existingCommercePriceModifier.getModifierAmount(),
			newCommercePriceModifier.getModifierAmount());
		Assert.assertEquals(
			existingCommercePriceModifier.getModifierType(),
			newCommercePriceModifier.getModifierType());
		AssertUtils.assertEquals(
			existingCommercePriceModifier.getPriority(),
			newCommercePriceModifier.getPriority());
		Assert.assertEquals(
			existingCommercePriceModifier.isActive(),
			newCommercePriceModifier.isActive());
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingCommercePriceModifier.getDisplayDate()),
			Time.getShortTimestamp(newCommercePriceModifier.getDisplayDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingCommercePriceModifier.getExpirationDate()),
			Time.getShortTimestamp(
				newCommercePriceModifier.getExpirationDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingCommercePriceModifier.getLastPublishDate()),
			Time.getShortTimestamp(
				newCommercePriceModifier.getLastPublishDate()));
		Assert.assertEquals(
			existingCommercePriceModifier.getStatus(),
			newCommercePriceModifier.getStatus());
		Assert.assertEquals(
			existingCommercePriceModifier.getStatusByUserId(),
			newCommercePriceModifier.getStatusByUserId());
		Assert.assertEquals(
			existingCommercePriceModifier.getStatusByUserName(),
			newCommercePriceModifier.getStatusByUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingCommercePriceModifier.getStatusDate()),
			Time.getShortTimestamp(newCommercePriceModifier.getStatusDate()));
	}

	@Test
	public void testCountByUuid() throws Exception {
		_persistence.countByUuid("");

		_persistence.countByUuid("null");

		_persistence.countByUuid((String)null);
	}

	@Test
	public void testCountByUUID_G() throws Exception {
		_persistence.countByUUID_G("", RandomTestUtil.nextLong());

		_persistence.countByUUID_G("null", 0L);

		_persistence.countByUUID_G((String)null, 0L);
	}

	@Test
	public void testCountByUuid_C() throws Exception {
		_persistence.countByUuid_C("", RandomTestUtil.nextLong());

		_persistence.countByUuid_C("null", 0L);

		_persistence.countByUuid_C((String)null, 0L);
	}

	@Test
	public void testCountByCompanyId() throws Exception {
		_persistence.countByCompanyId(RandomTestUtil.nextLong());

		_persistence.countByCompanyId(0L);
	}

	@Test
	public void testCountByCommercePriceListId() throws Exception {
		_persistence.countByCommercePriceListId(RandomTestUtil.nextLong());

		_persistence.countByCommercePriceListId(0L);
	}

	@Test
	public void testCountByC_T() throws Exception {
		_persistence.countByC_T(RandomTestUtil.nextLong(), "");

		_persistence.countByC_T(0L, "null");

		_persistence.countByC_T(0L, (String)null);
	}

	@Test
	public void testCountByLtD_S() throws Exception {
		_persistence.countByLtD_S(
			RandomTestUtil.nextDate(), RandomTestUtil.nextInt());

		_persistence.countByLtD_S(RandomTestUtil.nextDate(), 0);
	}

	@Test
	public void testCountByLtE_S() throws Exception {
		_persistence.countByLtE_S(
			RandomTestUtil.nextDate(), RandomTestUtil.nextInt());

		_persistence.countByLtE_S(RandomTestUtil.nextDate(), 0);
	}

	@Test
	public void testCountByG_C_S() throws Exception {
		_persistence.countByG_C_S(
			RandomTestUtil.nextLong(), RandomTestUtil.nextLong(),
			RandomTestUtil.nextInt());

		_persistence.countByG_C_S(0L, 0L, 0);
	}

	@Test
	public void testCountByG_C_SArrayable() throws Exception {
		_persistence.countByG_C_S(
			new long[] {RandomTestUtil.nextLong(), 0L},
			RandomTestUtil.nextLong(), RandomTestUtil.nextInt());
	}

	@Test
	public void testCountByG_C_NotS() throws Exception {
		_persistence.countByG_C_NotS(
			RandomTestUtil.nextLong(), RandomTestUtil.nextLong(),
			RandomTestUtil.nextInt());

		_persistence.countByG_C_NotS(0L, 0L, 0);
	}

	@Test
	public void testCountByG_C_NotSArrayable() throws Exception {
		_persistence.countByG_C_NotS(
			new long[] {RandomTestUtil.nextLong(), 0L},
			RandomTestUtil.nextLong(), RandomTestUtil.nextInt());
	}

	@Test
	public void testCountByC_ERC() throws Exception {
		_persistence.countByC_ERC(RandomTestUtil.nextLong(), "");

		_persistence.countByC_ERC(0L, "null");

		_persistence.countByC_ERC(0L, (String)null);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		CommercePriceModifier newCommercePriceModifier =
			addCommercePriceModifier();

		CommercePriceModifier existingCommercePriceModifier =
			_persistence.findByPrimaryKey(
				newCommercePriceModifier.getPrimaryKey());

		Assert.assertEquals(
			existingCommercePriceModifier, newCommercePriceModifier);
	}

	@Test(expected = NoSuchPriceModifierException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<CommercePriceModifier> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"CommercePriceModifier", "mvccVersion", true, "ctCollectionId",
			true, "uuid", true, "externalReferenceCode", true,
			"commercePriceModifierId", true, "groupId", true, "companyId", true,
			"userId", true, "userName", true, "createDate", true,
			"modifiedDate", true, "commercePriceListId", true, "title", true,
			"target", true, "modifierAmount", true, "modifierType", true,
			"priority", true, "active", true, "displayDate", true,
			"expirationDate", true, "lastPublishDate", true, "status", true,
			"statusByUserId", true, "statusByUserName", true, "statusDate",
			true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		CommercePriceModifier newCommercePriceModifier =
			addCommercePriceModifier();

		CommercePriceModifier existingCommercePriceModifier =
			_persistence.fetchByPrimaryKey(
				newCommercePriceModifier.getPrimaryKey());

		Assert.assertEquals(
			existingCommercePriceModifier, newCommercePriceModifier);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		CommercePriceModifier missingCommercePriceModifier =
			_persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingCommercePriceModifier);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		CommercePriceModifier newCommercePriceModifier1 =
			addCommercePriceModifier();
		CommercePriceModifier newCommercePriceModifier2 =
			addCommercePriceModifier();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newCommercePriceModifier1.getPrimaryKey());
		primaryKeys.add(newCommercePriceModifier2.getPrimaryKey());

		Map<Serializable, CommercePriceModifier> commercePriceModifiers =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, commercePriceModifiers.size());
		Assert.assertEquals(
			newCommercePriceModifier1,
			commercePriceModifiers.get(
				newCommercePriceModifier1.getPrimaryKey()));
		Assert.assertEquals(
			newCommercePriceModifier2,
			commercePriceModifiers.get(
				newCommercePriceModifier2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, CommercePriceModifier> commercePriceModifiers =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(commercePriceModifiers.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		CommercePriceModifier newCommercePriceModifier =
			addCommercePriceModifier();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newCommercePriceModifier.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, CommercePriceModifier> commercePriceModifiers =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, commercePriceModifiers.size());
		Assert.assertEquals(
			newCommercePriceModifier,
			commercePriceModifiers.get(
				newCommercePriceModifier.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, CommercePriceModifier> commercePriceModifiers =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(commercePriceModifiers.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		CommercePriceModifier newCommercePriceModifier =
			addCommercePriceModifier();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newCommercePriceModifier.getPrimaryKey());

		Map<Serializable, CommercePriceModifier> commercePriceModifiers =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, commercePriceModifiers.size());
		Assert.assertEquals(
			newCommercePriceModifier,
			commercePriceModifiers.get(
				newCommercePriceModifier.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			CommercePriceModifierLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<CommercePriceModifier>() {

				@Override
				public void performAction(
					CommercePriceModifier commercePriceModifier) {

					Assert.assertNotNull(commercePriceModifier);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		CommercePriceModifier newCommercePriceModifier =
			addCommercePriceModifier();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CommercePriceModifier.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"commercePriceModifierId",
				newCommercePriceModifier.getCommercePriceModifierId()));

		List<CommercePriceModifier> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		CommercePriceModifier existingCommercePriceModifier = result.get(0);

		Assert.assertEquals(
			existingCommercePriceModifier, newCommercePriceModifier);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CommercePriceModifier.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"commercePriceModifierId", RandomTestUtil.nextLong()));

		List<CommercePriceModifier> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		CommercePriceModifier newCommercePriceModifier =
			addCommercePriceModifier();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CommercePriceModifier.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("commercePriceModifierId"));

		Object newCommercePriceModifierId =
			newCommercePriceModifier.getCommercePriceModifierId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"commercePriceModifierId",
				new Object[] {newCommercePriceModifierId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingCommercePriceModifierId = result.get(0);

		Assert.assertEquals(
			existingCommercePriceModifierId, newCommercePriceModifierId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CommercePriceModifier.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("commercePriceModifierId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"commercePriceModifierId",
				new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		CommercePriceModifier newCommercePriceModifier =
			addCommercePriceModifier();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(
				newCommercePriceModifier.getPrimaryKey()));
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromDatabase()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(true);
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromSession()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(false);
	}

	private void _testResetOriginalValuesWithDynamicQuery(boolean clearSession)
		throws Exception {

		CommercePriceModifier newCommercePriceModifier =
			addCommercePriceModifier();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CommercePriceModifier.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"commercePriceModifierId",
				newCommercePriceModifier.getCommercePriceModifierId()));

		List<CommercePriceModifier> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(
		CommercePriceModifier commercePriceModifier) {

		Assert.assertEquals(
			commercePriceModifier.getUuid(),
			ReflectionTestUtil.invoke(
				commercePriceModifier, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "uuid_"));
		Assert.assertEquals(
			Long.valueOf(commercePriceModifier.getGroupId()),
			ReflectionTestUtil.<Long>invoke(
				commercePriceModifier, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "groupId"));

		Assert.assertEquals(
			Long.valueOf(commercePriceModifier.getCompanyId()),
			ReflectionTestUtil.<Long>invoke(
				commercePriceModifier, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "companyId"));
		Assert.assertEquals(
			commercePriceModifier.getExternalReferenceCode(),
			ReflectionTestUtil.invoke(
				commercePriceModifier, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "externalReferenceCode"));
	}

	protected CommercePriceModifier addCommercePriceModifier()
		throws Exception {

		long pk = RandomTestUtil.nextLong();

		CommercePriceModifier commercePriceModifier = _persistence.create(pk);

		commercePriceModifier.setMvccVersion(RandomTestUtil.nextLong());

		commercePriceModifier.setCtCollectionId(RandomTestUtil.nextLong());

		commercePriceModifier.setUuid(RandomTestUtil.randomString());

		commercePriceModifier.setExternalReferenceCode(
			RandomTestUtil.randomString());

		commercePriceModifier.setGroupId(RandomTestUtil.nextLong());

		commercePriceModifier.setCompanyId(RandomTestUtil.nextLong());

		commercePriceModifier.setUserId(RandomTestUtil.nextLong());

		commercePriceModifier.setUserName(RandomTestUtil.randomString());

		commercePriceModifier.setCreateDate(RandomTestUtil.nextDate());

		commercePriceModifier.setModifiedDate(RandomTestUtil.nextDate());

		commercePriceModifier.setCommercePriceListId(RandomTestUtil.nextLong());

		commercePriceModifier.setTitle(RandomTestUtil.randomString());

		commercePriceModifier.setTarget(RandomTestUtil.randomString());

		commercePriceModifier.setModifierAmount(
			new BigDecimal(RandomTestUtil.nextDouble()));

		commercePriceModifier.setModifierType(RandomTestUtil.randomString());

		commercePriceModifier.setPriority(RandomTestUtil.nextDouble());

		commercePriceModifier.setActive(RandomTestUtil.randomBoolean());

		commercePriceModifier.setDisplayDate(RandomTestUtil.nextDate());

		commercePriceModifier.setExpirationDate(RandomTestUtil.nextDate());

		commercePriceModifier.setLastPublishDate(RandomTestUtil.nextDate());

		commercePriceModifier.setStatus(RandomTestUtil.nextInt());

		commercePriceModifier.setStatusByUserId(RandomTestUtil.nextLong());

		commercePriceModifier.setStatusByUserName(
			RandomTestUtil.randomString());

		commercePriceModifier.setStatusDate(RandomTestUtil.nextDate());

		_commercePriceModifiers.add(_persistence.update(commercePriceModifier));

		return commercePriceModifier;
	}

	private List<CommercePriceModifier> _commercePriceModifiers =
		new ArrayList<CommercePriceModifier>();
	private CommercePriceModifierPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}