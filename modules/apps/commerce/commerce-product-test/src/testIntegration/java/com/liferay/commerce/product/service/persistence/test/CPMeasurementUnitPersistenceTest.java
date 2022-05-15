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

package com.liferay.commerce.product.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.product.exception.NoSuchCPMeasurementUnitException;
import com.liferay.commerce.product.model.CPMeasurementUnit;
import com.liferay.commerce.product.service.CPMeasurementUnitLocalServiceUtil;
import com.liferay.commerce.product.service.persistence.CPMeasurementUnitPersistence;
import com.liferay.commerce.product.service.persistence.CPMeasurementUnitUtil;
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
public class CPMeasurementUnitPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.commerce.product.service"));

	@Before
	public void setUp() {
		_persistence = CPMeasurementUnitUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<CPMeasurementUnit> iterator = _cpMeasurementUnits.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		CPMeasurementUnit cpMeasurementUnit = _persistence.create(pk);

		Assert.assertNotNull(cpMeasurementUnit);

		Assert.assertEquals(cpMeasurementUnit.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		CPMeasurementUnit newCPMeasurementUnit = addCPMeasurementUnit();

		_persistence.remove(newCPMeasurementUnit);

		CPMeasurementUnit existingCPMeasurementUnit =
			_persistence.fetchByPrimaryKey(
				newCPMeasurementUnit.getPrimaryKey());

		Assert.assertNull(existingCPMeasurementUnit);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addCPMeasurementUnit();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		CPMeasurementUnit newCPMeasurementUnit = _persistence.create(pk);

		newCPMeasurementUnit.setMvccVersion(RandomTestUtil.nextLong());

		newCPMeasurementUnit.setCtCollectionId(RandomTestUtil.nextLong());

		newCPMeasurementUnit.setUuid(RandomTestUtil.randomString());

		newCPMeasurementUnit.setExternalReferenceCode(
			RandomTestUtil.randomString());

		newCPMeasurementUnit.setGroupId(RandomTestUtil.nextLong());

		newCPMeasurementUnit.setCompanyId(RandomTestUtil.nextLong());

		newCPMeasurementUnit.setUserId(RandomTestUtil.nextLong());

		newCPMeasurementUnit.setUserName(RandomTestUtil.randomString());

		newCPMeasurementUnit.setCreateDate(RandomTestUtil.nextDate());

		newCPMeasurementUnit.setModifiedDate(RandomTestUtil.nextDate());

		newCPMeasurementUnit.setName(RandomTestUtil.randomString());

		newCPMeasurementUnit.setKey(RandomTestUtil.randomString());

		newCPMeasurementUnit.setRate(RandomTestUtil.nextDouble());

		newCPMeasurementUnit.setPrimary(RandomTestUtil.randomBoolean());

		newCPMeasurementUnit.setPriority(RandomTestUtil.nextDouble());

		newCPMeasurementUnit.setType(RandomTestUtil.nextInt());

		newCPMeasurementUnit.setLastPublishDate(RandomTestUtil.nextDate());

		_cpMeasurementUnits.add(_persistence.update(newCPMeasurementUnit));

		CPMeasurementUnit existingCPMeasurementUnit =
			_persistence.findByPrimaryKey(newCPMeasurementUnit.getPrimaryKey());

		Assert.assertEquals(
			existingCPMeasurementUnit.getMvccVersion(),
			newCPMeasurementUnit.getMvccVersion());
		Assert.assertEquals(
			existingCPMeasurementUnit.getCtCollectionId(),
			newCPMeasurementUnit.getCtCollectionId());
		Assert.assertEquals(
			existingCPMeasurementUnit.getUuid(),
			newCPMeasurementUnit.getUuid());
		Assert.assertEquals(
			existingCPMeasurementUnit.getExternalReferenceCode(),
			newCPMeasurementUnit.getExternalReferenceCode());
		Assert.assertEquals(
			existingCPMeasurementUnit.getCPMeasurementUnitId(),
			newCPMeasurementUnit.getCPMeasurementUnitId());
		Assert.assertEquals(
			existingCPMeasurementUnit.getGroupId(),
			newCPMeasurementUnit.getGroupId());
		Assert.assertEquals(
			existingCPMeasurementUnit.getCompanyId(),
			newCPMeasurementUnit.getCompanyId());
		Assert.assertEquals(
			existingCPMeasurementUnit.getUserId(),
			newCPMeasurementUnit.getUserId());
		Assert.assertEquals(
			existingCPMeasurementUnit.getUserName(),
			newCPMeasurementUnit.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(existingCPMeasurementUnit.getCreateDate()),
			Time.getShortTimestamp(newCPMeasurementUnit.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(existingCPMeasurementUnit.getModifiedDate()),
			Time.getShortTimestamp(newCPMeasurementUnit.getModifiedDate()));
		Assert.assertEquals(
			existingCPMeasurementUnit.getName(),
			newCPMeasurementUnit.getName());
		Assert.assertEquals(
			existingCPMeasurementUnit.getKey(), newCPMeasurementUnit.getKey());
		AssertUtils.assertEquals(
			existingCPMeasurementUnit.getRate(),
			newCPMeasurementUnit.getRate());
		Assert.assertEquals(
			existingCPMeasurementUnit.isPrimary(),
			newCPMeasurementUnit.isPrimary());
		AssertUtils.assertEquals(
			existingCPMeasurementUnit.getPriority(),
			newCPMeasurementUnit.getPriority());
		Assert.assertEquals(
			existingCPMeasurementUnit.getType(),
			newCPMeasurementUnit.getType());
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingCPMeasurementUnit.getLastPublishDate()),
			Time.getShortTimestamp(newCPMeasurementUnit.getLastPublishDate()));
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
	public void testCountByC_K() throws Exception {
		_persistence.countByC_K(RandomTestUtil.nextLong(), "");

		_persistence.countByC_K(0L, "null");

		_persistence.countByC_K(0L, (String)null);
	}

	@Test
	public void testCountByC_T() throws Exception {
		_persistence.countByC_T(
			RandomTestUtil.nextLong(), RandomTestUtil.nextInt());

		_persistence.countByC_T(0L, 0);
	}

	@Test
	public void testCountByC_P_T() throws Exception {
		_persistence.countByC_P_T(
			RandomTestUtil.nextLong(), RandomTestUtil.randomBoolean(),
			RandomTestUtil.nextInt());

		_persistence.countByC_P_T(0L, RandomTestUtil.randomBoolean(), 0);
	}

	@Test
	public void testCountByC_ERC() throws Exception {
		_persistence.countByC_ERC(RandomTestUtil.nextLong(), "");

		_persistence.countByC_ERC(0L, "null");

		_persistence.countByC_ERC(0L, (String)null);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		CPMeasurementUnit newCPMeasurementUnit = addCPMeasurementUnit();

		CPMeasurementUnit existingCPMeasurementUnit =
			_persistence.findByPrimaryKey(newCPMeasurementUnit.getPrimaryKey());

		Assert.assertEquals(existingCPMeasurementUnit, newCPMeasurementUnit);
	}

	@Test(expected = NoSuchCPMeasurementUnitException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<CPMeasurementUnit> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"CPMeasurementUnit", "mvccVersion", true, "ctCollectionId", true,
			"uuid", true, "externalReferenceCode", true, "CPMeasurementUnitId",
			true, "groupId", true, "companyId", true, "userId", true,
			"userName", true, "createDate", true, "modifiedDate", true, "name",
			true, "key", true, "rate", true, "primary", true, "priority", true,
			"type", true, "lastPublishDate", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		CPMeasurementUnit newCPMeasurementUnit = addCPMeasurementUnit();

		CPMeasurementUnit existingCPMeasurementUnit =
			_persistence.fetchByPrimaryKey(
				newCPMeasurementUnit.getPrimaryKey());

		Assert.assertEquals(existingCPMeasurementUnit, newCPMeasurementUnit);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		CPMeasurementUnit missingCPMeasurementUnit =
			_persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingCPMeasurementUnit);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		CPMeasurementUnit newCPMeasurementUnit1 = addCPMeasurementUnit();
		CPMeasurementUnit newCPMeasurementUnit2 = addCPMeasurementUnit();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newCPMeasurementUnit1.getPrimaryKey());
		primaryKeys.add(newCPMeasurementUnit2.getPrimaryKey());

		Map<Serializable, CPMeasurementUnit> cpMeasurementUnits =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, cpMeasurementUnits.size());
		Assert.assertEquals(
			newCPMeasurementUnit1,
			cpMeasurementUnits.get(newCPMeasurementUnit1.getPrimaryKey()));
		Assert.assertEquals(
			newCPMeasurementUnit2,
			cpMeasurementUnits.get(newCPMeasurementUnit2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, CPMeasurementUnit> cpMeasurementUnits =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(cpMeasurementUnits.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		CPMeasurementUnit newCPMeasurementUnit = addCPMeasurementUnit();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newCPMeasurementUnit.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, CPMeasurementUnit> cpMeasurementUnits =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, cpMeasurementUnits.size());
		Assert.assertEquals(
			newCPMeasurementUnit,
			cpMeasurementUnits.get(newCPMeasurementUnit.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, CPMeasurementUnit> cpMeasurementUnits =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(cpMeasurementUnits.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		CPMeasurementUnit newCPMeasurementUnit = addCPMeasurementUnit();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newCPMeasurementUnit.getPrimaryKey());

		Map<Serializable, CPMeasurementUnit> cpMeasurementUnits =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, cpMeasurementUnits.size());
		Assert.assertEquals(
			newCPMeasurementUnit,
			cpMeasurementUnits.get(newCPMeasurementUnit.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			CPMeasurementUnitLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<CPMeasurementUnit>() {

				@Override
				public void performAction(CPMeasurementUnit cpMeasurementUnit) {
					Assert.assertNotNull(cpMeasurementUnit);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		CPMeasurementUnit newCPMeasurementUnit = addCPMeasurementUnit();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CPMeasurementUnit.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"CPMeasurementUnitId",
				newCPMeasurementUnit.getCPMeasurementUnitId()));

		List<CPMeasurementUnit> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		CPMeasurementUnit existingCPMeasurementUnit = result.get(0);

		Assert.assertEquals(existingCPMeasurementUnit, newCPMeasurementUnit);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CPMeasurementUnit.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"CPMeasurementUnitId", RandomTestUtil.nextLong()));

		List<CPMeasurementUnit> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		CPMeasurementUnit newCPMeasurementUnit = addCPMeasurementUnit();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CPMeasurementUnit.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("CPMeasurementUnitId"));

		Object newCPMeasurementUnitId =
			newCPMeasurementUnit.getCPMeasurementUnitId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"CPMeasurementUnitId", new Object[] {newCPMeasurementUnitId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingCPMeasurementUnitId = result.get(0);

		Assert.assertEquals(
			existingCPMeasurementUnitId, newCPMeasurementUnitId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CPMeasurementUnit.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("CPMeasurementUnitId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"CPMeasurementUnitId",
				new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		CPMeasurementUnit newCPMeasurementUnit = addCPMeasurementUnit();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(
				newCPMeasurementUnit.getPrimaryKey()));
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

		CPMeasurementUnit newCPMeasurementUnit = addCPMeasurementUnit();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			CPMeasurementUnit.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"CPMeasurementUnitId",
				newCPMeasurementUnit.getCPMeasurementUnitId()));

		List<CPMeasurementUnit> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(CPMeasurementUnit cpMeasurementUnit) {
		Assert.assertEquals(
			cpMeasurementUnit.getUuid(),
			ReflectionTestUtil.invoke(
				cpMeasurementUnit, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "uuid_"));
		Assert.assertEquals(
			Long.valueOf(cpMeasurementUnit.getGroupId()),
			ReflectionTestUtil.<Long>invoke(
				cpMeasurementUnit, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "groupId"));

		Assert.assertEquals(
			Long.valueOf(cpMeasurementUnit.getCompanyId()),
			ReflectionTestUtil.<Long>invoke(
				cpMeasurementUnit, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "companyId"));
		Assert.assertEquals(
			cpMeasurementUnit.getKey(),
			ReflectionTestUtil.invoke(
				cpMeasurementUnit, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "key_"));

		Assert.assertEquals(
			Long.valueOf(cpMeasurementUnit.getCompanyId()),
			ReflectionTestUtil.<Long>invoke(
				cpMeasurementUnit, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "companyId"));
		Assert.assertEquals(
			cpMeasurementUnit.getExternalReferenceCode(),
			ReflectionTestUtil.invoke(
				cpMeasurementUnit, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "externalReferenceCode"));
	}

	protected CPMeasurementUnit addCPMeasurementUnit() throws Exception {
		long pk = RandomTestUtil.nextLong();

		CPMeasurementUnit cpMeasurementUnit = _persistence.create(pk);

		cpMeasurementUnit.setMvccVersion(RandomTestUtil.nextLong());

		cpMeasurementUnit.setCtCollectionId(RandomTestUtil.nextLong());

		cpMeasurementUnit.setUuid(RandomTestUtil.randomString());

		cpMeasurementUnit.setExternalReferenceCode(
			RandomTestUtil.randomString());

		cpMeasurementUnit.setGroupId(RandomTestUtil.nextLong());

		cpMeasurementUnit.setCompanyId(RandomTestUtil.nextLong());

		cpMeasurementUnit.setUserId(RandomTestUtil.nextLong());

		cpMeasurementUnit.setUserName(RandomTestUtil.randomString());

		cpMeasurementUnit.setCreateDate(RandomTestUtil.nextDate());

		cpMeasurementUnit.setModifiedDate(RandomTestUtil.nextDate());

		cpMeasurementUnit.setName(RandomTestUtil.randomString());

		cpMeasurementUnit.setKey(RandomTestUtil.randomString());

		cpMeasurementUnit.setRate(RandomTestUtil.nextDouble());

		cpMeasurementUnit.setPrimary(RandomTestUtil.randomBoolean());

		cpMeasurementUnit.setPriority(RandomTestUtil.nextDouble());

		cpMeasurementUnit.setType(RandomTestUtil.nextInt());

		cpMeasurementUnit.setLastPublishDate(RandomTestUtil.nextDate());

		_cpMeasurementUnits.add(_persistence.update(cpMeasurementUnit));

		return cpMeasurementUnit;
	}

	private List<CPMeasurementUnit> _cpMeasurementUnits =
		new ArrayList<CPMeasurementUnit>();
	private CPMeasurementUnitPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}