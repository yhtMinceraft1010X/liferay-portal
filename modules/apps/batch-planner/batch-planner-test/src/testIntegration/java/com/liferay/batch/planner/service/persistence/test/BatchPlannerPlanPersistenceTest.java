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

package com.liferay.batch.planner.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.batch.planner.exception.NoSuchPlanException;
import com.liferay.batch.planner.model.BatchPlannerPlan;
import com.liferay.batch.planner.service.BatchPlannerPlanLocalServiceUtil;
import com.liferay.batch.planner.service.persistence.BatchPlannerPlanPersistence;
import com.liferay.batch.planner.service.persistence.BatchPlannerPlanUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
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
public class BatchPlannerPlanPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.batch.planner.service"));

	@Before
	public void setUp() {
		_persistence = BatchPlannerPlanUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<BatchPlannerPlan> iterator = _batchPlannerPlans.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		BatchPlannerPlan batchPlannerPlan = _persistence.create(pk);

		Assert.assertNotNull(batchPlannerPlan);

		Assert.assertEquals(batchPlannerPlan.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		BatchPlannerPlan newBatchPlannerPlan = addBatchPlannerPlan();

		_persistence.remove(newBatchPlannerPlan);

		BatchPlannerPlan existingBatchPlannerPlan =
			_persistence.fetchByPrimaryKey(newBatchPlannerPlan.getPrimaryKey());

		Assert.assertNull(existingBatchPlannerPlan);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addBatchPlannerPlan();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		BatchPlannerPlan newBatchPlannerPlan = _persistence.create(pk);

		newBatchPlannerPlan.setMvccVersion(RandomTestUtil.nextLong());

		newBatchPlannerPlan.setCompanyId(RandomTestUtil.nextLong());

		newBatchPlannerPlan.setUserId(RandomTestUtil.nextLong());

		newBatchPlannerPlan.setUserName(RandomTestUtil.randomString());

		newBatchPlannerPlan.setCreateDate(RandomTestUtil.nextDate());

		newBatchPlannerPlan.setModifiedDate(RandomTestUtil.nextDate());

		newBatchPlannerPlan.setActive(RandomTestUtil.randomBoolean());

		newBatchPlannerPlan.setExport(RandomTestUtil.randomBoolean());

		newBatchPlannerPlan.setExternalType(RandomTestUtil.randomString());

		newBatchPlannerPlan.setExternalURL(RandomTestUtil.randomString());

		newBatchPlannerPlan.setInternalClassName(RandomTestUtil.randomString());

		newBatchPlannerPlan.setName(RandomTestUtil.randomString());

		newBatchPlannerPlan.setSize(RandomTestUtil.nextInt());

		newBatchPlannerPlan.setTaskItemDelegateName(
			RandomTestUtil.randomString());

		newBatchPlannerPlan.setTotal(RandomTestUtil.nextInt());

		newBatchPlannerPlan.setTemplate(RandomTestUtil.randomBoolean());

		newBatchPlannerPlan.setStatus(RandomTestUtil.nextInt());

		_batchPlannerPlans.add(_persistence.update(newBatchPlannerPlan));

		BatchPlannerPlan existingBatchPlannerPlan =
			_persistence.findByPrimaryKey(newBatchPlannerPlan.getPrimaryKey());

		Assert.assertEquals(
			existingBatchPlannerPlan.getMvccVersion(),
			newBatchPlannerPlan.getMvccVersion());
		Assert.assertEquals(
			existingBatchPlannerPlan.getBatchPlannerPlanId(),
			newBatchPlannerPlan.getBatchPlannerPlanId());
		Assert.assertEquals(
			existingBatchPlannerPlan.getCompanyId(),
			newBatchPlannerPlan.getCompanyId());
		Assert.assertEquals(
			existingBatchPlannerPlan.getUserId(),
			newBatchPlannerPlan.getUserId());
		Assert.assertEquals(
			existingBatchPlannerPlan.getUserName(),
			newBatchPlannerPlan.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(existingBatchPlannerPlan.getCreateDate()),
			Time.getShortTimestamp(newBatchPlannerPlan.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(existingBatchPlannerPlan.getModifiedDate()),
			Time.getShortTimestamp(newBatchPlannerPlan.getModifiedDate()));
		Assert.assertEquals(
			existingBatchPlannerPlan.isActive(),
			newBatchPlannerPlan.isActive());
		Assert.assertEquals(
			existingBatchPlannerPlan.isExport(),
			newBatchPlannerPlan.isExport());
		Assert.assertEquals(
			existingBatchPlannerPlan.getExternalType(),
			newBatchPlannerPlan.getExternalType());
		Assert.assertEquals(
			existingBatchPlannerPlan.getExternalURL(),
			newBatchPlannerPlan.getExternalURL());
		Assert.assertEquals(
			existingBatchPlannerPlan.getInternalClassName(),
			newBatchPlannerPlan.getInternalClassName());
		Assert.assertEquals(
			existingBatchPlannerPlan.getName(), newBatchPlannerPlan.getName());
		Assert.assertEquals(
			existingBatchPlannerPlan.getSize(), newBatchPlannerPlan.getSize());
		Assert.assertEquals(
			existingBatchPlannerPlan.getTaskItemDelegateName(),
			newBatchPlannerPlan.getTaskItemDelegateName());
		Assert.assertEquals(
			existingBatchPlannerPlan.getTotal(),
			newBatchPlannerPlan.getTotal());
		Assert.assertEquals(
			existingBatchPlannerPlan.isTemplate(),
			newBatchPlannerPlan.isTemplate());
		Assert.assertEquals(
			existingBatchPlannerPlan.getStatus(),
			newBatchPlannerPlan.getStatus());
	}

	@Test
	public void testCountByCompanyId() throws Exception {
		_persistence.countByCompanyId(RandomTestUtil.nextLong());

		_persistence.countByCompanyId(0L);
	}

	@Test
	public void testCountByC_U() throws Exception {
		_persistence.countByC_U(
			RandomTestUtil.nextLong(), RandomTestUtil.nextLong());

		_persistence.countByC_U(0L, 0L);
	}

	@Test
	public void testCountByC_E() throws Exception {
		_persistence.countByC_E(
			RandomTestUtil.nextLong(), RandomTestUtil.randomBoolean());

		_persistence.countByC_E(0L, RandomTestUtil.randomBoolean());
	}

	@Test
	public void testCountByC_N() throws Exception {
		_persistence.countByC_N(RandomTestUtil.nextLong(), "");

		_persistence.countByC_N(0L, "null");

		_persistence.countByC_N(0L, (String)null);
	}

	@Test
	public void testCountByC_T() throws Exception {
		_persistence.countByC_T(
			RandomTestUtil.nextLong(), RandomTestUtil.randomBoolean());

		_persistence.countByC_T(0L, RandomTestUtil.randomBoolean());
	}

	@Test
	public void testCountByC_E_T() throws Exception {
		_persistence.countByC_E_T(
			RandomTestUtil.nextLong(), RandomTestUtil.randomBoolean(),
			RandomTestUtil.randomBoolean());

		_persistence.countByC_E_T(
			0L, RandomTestUtil.randomBoolean(), RandomTestUtil.randomBoolean());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		BatchPlannerPlan newBatchPlannerPlan = addBatchPlannerPlan();

		BatchPlannerPlan existingBatchPlannerPlan =
			_persistence.findByPrimaryKey(newBatchPlannerPlan.getPrimaryKey());

		Assert.assertEquals(existingBatchPlannerPlan, newBatchPlannerPlan);
	}

	@Test(expected = NoSuchPlanException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<BatchPlannerPlan> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"BatchPlannerPlan", "mvccVersion", true, "batchPlannerPlanId", true,
			"companyId", true, "userId", true, "userName", true, "createDate",
			true, "modifiedDate", true, "active", true, "export", true,
			"externalType", true, "externalURL", true, "internalClassName",
			true, "name", true, "size", true, "taskItemDelegateName", true,
			"total", true, "template", true, "status", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		BatchPlannerPlan newBatchPlannerPlan = addBatchPlannerPlan();

		BatchPlannerPlan existingBatchPlannerPlan =
			_persistence.fetchByPrimaryKey(newBatchPlannerPlan.getPrimaryKey());

		Assert.assertEquals(existingBatchPlannerPlan, newBatchPlannerPlan);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		BatchPlannerPlan missingBatchPlannerPlan =
			_persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingBatchPlannerPlan);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		BatchPlannerPlan newBatchPlannerPlan1 = addBatchPlannerPlan();
		BatchPlannerPlan newBatchPlannerPlan2 = addBatchPlannerPlan();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newBatchPlannerPlan1.getPrimaryKey());
		primaryKeys.add(newBatchPlannerPlan2.getPrimaryKey());

		Map<Serializable, BatchPlannerPlan> batchPlannerPlans =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, batchPlannerPlans.size());
		Assert.assertEquals(
			newBatchPlannerPlan1,
			batchPlannerPlans.get(newBatchPlannerPlan1.getPrimaryKey()));
		Assert.assertEquals(
			newBatchPlannerPlan2,
			batchPlannerPlans.get(newBatchPlannerPlan2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, BatchPlannerPlan> batchPlannerPlans =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(batchPlannerPlans.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		BatchPlannerPlan newBatchPlannerPlan = addBatchPlannerPlan();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newBatchPlannerPlan.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, BatchPlannerPlan> batchPlannerPlans =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, batchPlannerPlans.size());
		Assert.assertEquals(
			newBatchPlannerPlan,
			batchPlannerPlans.get(newBatchPlannerPlan.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, BatchPlannerPlan> batchPlannerPlans =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(batchPlannerPlans.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		BatchPlannerPlan newBatchPlannerPlan = addBatchPlannerPlan();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newBatchPlannerPlan.getPrimaryKey());

		Map<Serializable, BatchPlannerPlan> batchPlannerPlans =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, batchPlannerPlans.size());
		Assert.assertEquals(
			newBatchPlannerPlan,
			batchPlannerPlans.get(newBatchPlannerPlan.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			BatchPlannerPlanLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<BatchPlannerPlan>() {

				@Override
				public void performAction(BatchPlannerPlan batchPlannerPlan) {
					Assert.assertNotNull(batchPlannerPlan);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		BatchPlannerPlan newBatchPlannerPlan = addBatchPlannerPlan();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			BatchPlannerPlan.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"batchPlannerPlanId",
				newBatchPlannerPlan.getBatchPlannerPlanId()));

		List<BatchPlannerPlan> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		BatchPlannerPlan existingBatchPlannerPlan = result.get(0);

		Assert.assertEquals(existingBatchPlannerPlan, newBatchPlannerPlan);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			BatchPlannerPlan.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"batchPlannerPlanId", RandomTestUtil.nextLong()));

		List<BatchPlannerPlan> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		BatchPlannerPlan newBatchPlannerPlan = addBatchPlannerPlan();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			BatchPlannerPlan.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("batchPlannerPlanId"));

		Object newBatchPlannerPlanId =
			newBatchPlannerPlan.getBatchPlannerPlanId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"batchPlannerPlanId", new Object[] {newBatchPlannerPlanId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingBatchPlannerPlanId = result.get(0);

		Assert.assertEquals(existingBatchPlannerPlanId, newBatchPlannerPlanId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			BatchPlannerPlan.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("batchPlannerPlanId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"batchPlannerPlanId",
				new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected BatchPlannerPlan addBatchPlannerPlan() throws Exception {
		long pk = RandomTestUtil.nextLong();

		BatchPlannerPlan batchPlannerPlan = _persistence.create(pk);

		batchPlannerPlan.setMvccVersion(RandomTestUtil.nextLong());

		batchPlannerPlan.setCompanyId(RandomTestUtil.nextLong());

		batchPlannerPlan.setUserId(RandomTestUtil.nextLong());

		batchPlannerPlan.setUserName(RandomTestUtil.randomString());

		batchPlannerPlan.setCreateDate(RandomTestUtil.nextDate());

		batchPlannerPlan.setModifiedDate(RandomTestUtil.nextDate());

		batchPlannerPlan.setActive(RandomTestUtil.randomBoolean());

		batchPlannerPlan.setExport(RandomTestUtil.randomBoolean());

		batchPlannerPlan.setExternalType(RandomTestUtil.randomString());

		batchPlannerPlan.setExternalURL(RandomTestUtil.randomString());

		batchPlannerPlan.setInternalClassName(RandomTestUtil.randomString());

		batchPlannerPlan.setName(RandomTestUtil.randomString());

		batchPlannerPlan.setSize(RandomTestUtil.nextInt());

		batchPlannerPlan.setTaskItemDelegateName(RandomTestUtil.randomString());

		batchPlannerPlan.setTotal(RandomTestUtil.nextInt());

		batchPlannerPlan.setTemplate(RandomTestUtil.randomBoolean());

		batchPlannerPlan.setStatus(RandomTestUtil.nextInt());

		_batchPlannerPlans.add(_persistence.update(batchPlannerPlan));

		return batchPlannerPlan;
	}

	private List<BatchPlannerPlan> _batchPlannerPlans =
		new ArrayList<BatchPlannerPlan>();
	private BatchPlannerPlanPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}