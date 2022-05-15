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

package com.liferay.object.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.exception.NoSuchObjectValidationRuleException;
import com.liferay.object.model.ObjectValidationRule;
import com.liferay.object.service.ObjectValidationRuleLocalServiceUtil;
import com.liferay.object.service.persistence.ObjectValidationRulePersistence;
import com.liferay.object.service.persistence.ObjectValidationRuleUtil;
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
public class ObjectValidationRulePersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.object.service"));

	@Before
	public void setUp() {
		_persistence = ObjectValidationRuleUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<ObjectValidationRule> iterator =
			_objectValidationRules.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ObjectValidationRule objectValidationRule = _persistence.create(pk);

		Assert.assertNotNull(objectValidationRule);

		Assert.assertEquals(objectValidationRule.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		ObjectValidationRule newObjectValidationRule =
			addObjectValidationRule();

		_persistence.remove(newObjectValidationRule);

		ObjectValidationRule existingObjectValidationRule =
			_persistence.fetchByPrimaryKey(
				newObjectValidationRule.getPrimaryKey());

		Assert.assertNull(existingObjectValidationRule);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addObjectValidationRule();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ObjectValidationRule newObjectValidationRule = _persistence.create(pk);

		newObjectValidationRule.setMvccVersion(RandomTestUtil.nextLong());

		newObjectValidationRule.setUuid(RandomTestUtil.randomString());

		newObjectValidationRule.setCompanyId(RandomTestUtil.nextLong());

		newObjectValidationRule.setUserId(RandomTestUtil.nextLong());

		newObjectValidationRule.setUserName(RandomTestUtil.randomString());

		newObjectValidationRule.setCreateDate(RandomTestUtil.nextDate());

		newObjectValidationRule.setModifiedDate(RandomTestUtil.nextDate());

		newObjectValidationRule.setObjectDefinitionId(
			RandomTestUtil.nextLong());

		newObjectValidationRule.setActive(RandomTestUtil.randomBoolean());

		newObjectValidationRule.setEngine(RandomTestUtil.randomString());

		newObjectValidationRule.setErrorLabel(RandomTestUtil.randomString());

		newObjectValidationRule.setName(RandomTestUtil.randomString());

		newObjectValidationRule.setScript(RandomTestUtil.randomString());

		_objectValidationRules.add(
			_persistence.update(newObjectValidationRule));

		ObjectValidationRule existingObjectValidationRule =
			_persistence.findByPrimaryKey(
				newObjectValidationRule.getPrimaryKey());

		Assert.assertEquals(
			existingObjectValidationRule.getMvccVersion(),
			newObjectValidationRule.getMvccVersion());
		Assert.assertEquals(
			existingObjectValidationRule.getUuid(),
			newObjectValidationRule.getUuid());
		Assert.assertEquals(
			existingObjectValidationRule.getObjectValidationRuleId(),
			newObjectValidationRule.getObjectValidationRuleId());
		Assert.assertEquals(
			existingObjectValidationRule.getCompanyId(),
			newObjectValidationRule.getCompanyId());
		Assert.assertEquals(
			existingObjectValidationRule.getUserId(),
			newObjectValidationRule.getUserId());
		Assert.assertEquals(
			existingObjectValidationRule.getUserName(),
			newObjectValidationRule.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingObjectValidationRule.getCreateDate()),
			Time.getShortTimestamp(newObjectValidationRule.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingObjectValidationRule.getModifiedDate()),
			Time.getShortTimestamp(newObjectValidationRule.getModifiedDate()));
		Assert.assertEquals(
			existingObjectValidationRule.getObjectDefinitionId(),
			newObjectValidationRule.getObjectDefinitionId());
		Assert.assertEquals(
			existingObjectValidationRule.isActive(),
			newObjectValidationRule.isActive());
		Assert.assertEquals(
			existingObjectValidationRule.getEngine(),
			newObjectValidationRule.getEngine());
		Assert.assertEquals(
			existingObjectValidationRule.getErrorLabel(),
			newObjectValidationRule.getErrorLabel());
		Assert.assertEquals(
			existingObjectValidationRule.getName(),
			newObjectValidationRule.getName());
		Assert.assertEquals(
			existingObjectValidationRule.getScript(),
			newObjectValidationRule.getScript());
	}

	@Test
	public void testCountByUuid() throws Exception {
		_persistence.countByUuid("");

		_persistence.countByUuid("null");

		_persistence.countByUuid((String)null);
	}

	@Test
	public void testCountByUuid_C() throws Exception {
		_persistence.countByUuid_C("", RandomTestUtil.nextLong());

		_persistence.countByUuid_C("null", 0L);

		_persistence.countByUuid_C((String)null, 0L);
	}

	@Test
	public void testCountByObjectDefinitionId() throws Exception {
		_persistence.countByObjectDefinitionId(RandomTestUtil.nextLong());

		_persistence.countByObjectDefinitionId(0L);
	}

	@Test
	public void testCountByODI_A() throws Exception {
		_persistence.countByODI_A(
			RandomTestUtil.nextLong(), RandomTestUtil.randomBoolean());

		_persistence.countByODI_A(0L, RandomTestUtil.randomBoolean());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		ObjectValidationRule newObjectValidationRule =
			addObjectValidationRule();

		ObjectValidationRule existingObjectValidationRule =
			_persistence.findByPrimaryKey(
				newObjectValidationRule.getPrimaryKey());

		Assert.assertEquals(
			existingObjectValidationRule, newObjectValidationRule);
	}

	@Test(expected = NoSuchObjectValidationRuleException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<ObjectValidationRule> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"ObjectValidationRule", "mvccVersion", true, "uuid", true,
			"objectValidationRuleId", true, "companyId", true, "userId", true,
			"userName", true, "createDate", true, "modifiedDate", true,
			"objectDefinitionId", true, "active", true, "engine", true,
			"errorLabel", true, "name", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		ObjectValidationRule newObjectValidationRule =
			addObjectValidationRule();

		ObjectValidationRule existingObjectValidationRule =
			_persistence.fetchByPrimaryKey(
				newObjectValidationRule.getPrimaryKey());

		Assert.assertEquals(
			existingObjectValidationRule, newObjectValidationRule);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ObjectValidationRule missingObjectValidationRule =
			_persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingObjectValidationRule);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		ObjectValidationRule newObjectValidationRule1 =
			addObjectValidationRule();
		ObjectValidationRule newObjectValidationRule2 =
			addObjectValidationRule();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newObjectValidationRule1.getPrimaryKey());
		primaryKeys.add(newObjectValidationRule2.getPrimaryKey());

		Map<Serializable, ObjectValidationRule> objectValidationRules =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, objectValidationRules.size());
		Assert.assertEquals(
			newObjectValidationRule1,
			objectValidationRules.get(
				newObjectValidationRule1.getPrimaryKey()));
		Assert.assertEquals(
			newObjectValidationRule2,
			objectValidationRules.get(
				newObjectValidationRule2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, ObjectValidationRule> objectValidationRules =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(objectValidationRules.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		ObjectValidationRule newObjectValidationRule =
			addObjectValidationRule();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newObjectValidationRule.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, ObjectValidationRule> objectValidationRules =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, objectValidationRules.size());
		Assert.assertEquals(
			newObjectValidationRule,
			objectValidationRules.get(newObjectValidationRule.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, ObjectValidationRule> objectValidationRules =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(objectValidationRules.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		ObjectValidationRule newObjectValidationRule =
			addObjectValidationRule();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newObjectValidationRule.getPrimaryKey());

		Map<Serializable, ObjectValidationRule> objectValidationRules =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, objectValidationRules.size());
		Assert.assertEquals(
			newObjectValidationRule,
			objectValidationRules.get(newObjectValidationRule.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			ObjectValidationRuleLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<ObjectValidationRule>() {

				@Override
				public void performAction(
					ObjectValidationRule objectValidationRule) {

					Assert.assertNotNull(objectValidationRule);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		ObjectValidationRule newObjectValidationRule =
			addObjectValidationRule();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ObjectValidationRule.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"objectValidationRuleId",
				newObjectValidationRule.getObjectValidationRuleId()));

		List<ObjectValidationRule> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		ObjectValidationRule existingObjectValidationRule = result.get(0);

		Assert.assertEquals(
			existingObjectValidationRule, newObjectValidationRule);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ObjectValidationRule.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"objectValidationRuleId", RandomTestUtil.nextLong()));

		List<ObjectValidationRule> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		ObjectValidationRule newObjectValidationRule =
			addObjectValidationRule();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ObjectValidationRule.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("objectValidationRuleId"));

		Object newObjectValidationRuleId =
			newObjectValidationRule.getObjectValidationRuleId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"objectValidationRuleId",
				new Object[] {newObjectValidationRuleId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingObjectValidationRuleId = result.get(0);

		Assert.assertEquals(
			existingObjectValidationRuleId, newObjectValidationRuleId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ObjectValidationRule.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("objectValidationRuleId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"objectValidationRuleId",
				new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected ObjectValidationRule addObjectValidationRule() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ObjectValidationRule objectValidationRule = _persistence.create(pk);

		objectValidationRule.setMvccVersion(RandomTestUtil.nextLong());

		objectValidationRule.setUuid(RandomTestUtil.randomString());

		objectValidationRule.setCompanyId(RandomTestUtil.nextLong());

		objectValidationRule.setUserId(RandomTestUtil.nextLong());

		objectValidationRule.setUserName(RandomTestUtil.randomString());

		objectValidationRule.setCreateDate(RandomTestUtil.nextDate());

		objectValidationRule.setModifiedDate(RandomTestUtil.nextDate());

		objectValidationRule.setObjectDefinitionId(RandomTestUtil.nextLong());

		objectValidationRule.setActive(RandomTestUtil.randomBoolean());

		objectValidationRule.setEngine(RandomTestUtil.randomString());

		objectValidationRule.setErrorLabel(RandomTestUtil.randomString());

		objectValidationRule.setName(RandomTestUtil.randomString());

		objectValidationRule.setScript(RandomTestUtil.randomString());

		_objectValidationRules.add(_persistence.update(objectValidationRule));

		return objectValidationRule;
	}

	private List<ObjectValidationRule> _objectValidationRules =
		new ArrayList<ObjectValidationRule>();
	private ObjectValidationRulePersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}