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
import com.liferay.object.exception.NoSuchObjectFieldSettingException;
import com.liferay.object.model.ObjectFieldSetting;
import com.liferay.object.service.ObjectFieldSettingLocalServiceUtil;
import com.liferay.object.service.persistence.ObjectFieldSettingPersistence;
import com.liferay.object.service.persistence.ObjectFieldSettingUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
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
public class ObjectFieldSettingPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.object.service"));

	@Before
	public void setUp() {
		_persistence = ObjectFieldSettingUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<ObjectFieldSetting> iterator = _objectFieldSettings.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ObjectFieldSetting objectFieldSetting = _persistence.create(pk);

		Assert.assertNotNull(objectFieldSetting);

		Assert.assertEquals(objectFieldSetting.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		ObjectFieldSetting newObjectFieldSetting = addObjectFieldSetting();

		_persistence.remove(newObjectFieldSetting);

		ObjectFieldSetting existingObjectFieldSetting =
			_persistence.fetchByPrimaryKey(
				newObjectFieldSetting.getPrimaryKey());

		Assert.assertNull(existingObjectFieldSetting);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addObjectFieldSetting();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ObjectFieldSetting newObjectFieldSetting = _persistence.create(pk);

		newObjectFieldSetting.setMvccVersion(RandomTestUtil.nextLong());

		newObjectFieldSetting.setUuid(RandomTestUtil.randomString());

		newObjectFieldSetting.setCompanyId(RandomTestUtil.nextLong());

		newObjectFieldSetting.setUserId(RandomTestUtil.nextLong());

		newObjectFieldSetting.setUserName(RandomTestUtil.randomString());

		newObjectFieldSetting.setCreateDate(RandomTestUtil.nextDate());

		newObjectFieldSetting.setModifiedDate(RandomTestUtil.nextDate());

		newObjectFieldSetting.setObjectFieldId(RandomTestUtil.nextLong());

		newObjectFieldSetting.setName(RandomTestUtil.randomString());

		newObjectFieldSetting.setValue(RandomTestUtil.randomString());

		_objectFieldSettings.add(_persistence.update(newObjectFieldSetting));

		ObjectFieldSetting existingObjectFieldSetting =
			_persistence.findByPrimaryKey(
				newObjectFieldSetting.getPrimaryKey());

		Assert.assertEquals(
			existingObjectFieldSetting.getMvccVersion(),
			newObjectFieldSetting.getMvccVersion());
		Assert.assertEquals(
			existingObjectFieldSetting.getUuid(),
			newObjectFieldSetting.getUuid());
		Assert.assertEquals(
			existingObjectFieldSetting.getObjectFieldSettingId(),
			newObjectFieldSetting.getObjectFieldSettingId());
		Assert.assertEquals(
			existingObjectFieldSetting.getCompanyId(),
			newObjectFieldSetting.getCompanyId());
		Assert.assertEquals(
			existingObjectFieldSetting.getUserId(),
			newObjectFieldSetting.getUserId());
		Assert.assertEquals(
			existingObjectFieldSetting.getUserName(),
			newObjectFieldSetting.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(existingObjectFieldSetting.getCreateDate()),
			Time.getShortTimestamp(newObjectFieldSetting.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingObjectFieldSetting.getModifiedDate()),
			Time.getShortTimestamp(newObjectFieldSetting.getModifiedDate()));
		Assert.assertEquals(
			existingObjectFieldSetting.getObjectFieldId(),
			newObjectFieldSetting.getObjectFieldId());
		Assert.assertEquals(
			existingObjectFieldSetting.getName(),
			newObjectFieldSetting.getName());
		Assert.assertEquals(
			existingObjectFieldSetting.getValue(),
			newObjectFieldSetting.getValue());
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
	public void testCountByObjectFieldId() throws Exception {
		_persistence.countByObjectFieldId(RandomTestUtil.nextLong());

		_persistence.countByObjectFieldId(0L);
	}

	@Test
	public void testCountByOFI_N() throws Exception {
		_persistence.countByOFI_N(RandomTestUtil.nextLong(), "");

		_persistence.countByOFI_N(0L, "null");

		_persistence.countByOFI_N(0L, (String)null);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		ObjectFieldSetting newObjectFieldSetting = addObjectFieldSetting();

		ObjectFieldSetting existingObjectFieldSetting =
			_persistence.findByPrimaryKey(
				newObjectFieldSetting.getPrimaryKey());

		Assert.assertEquals(existingObjectFieldSetting, newObjectFieldSetting);
	}

	@Test(expected = NoSuchObjectFieldSettingException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<ObjectFieldSetting> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"ObjectFieldSetting", "mvccVersion", true, "uuid", true,
			"objectFieldSettingId", true, "companyId", true, "userId", true,
			"userName", true, "createDate", true, "modifiedDate", true,
			"objectFieldId", true, "name", true, "value", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		ObjectFieldSetting newObjectFieldSetting = addObjectFieldSetting();

		ObjectFieldSetting existingObjectFieldSetting =
			_persistence.fetchByPrimaryKey(
				newObjectFieldSetting.getPrimaryKey());

		Assert.assertEquals(existingObjectFieldSetting, newObjectFieldSetting);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ObjectFieldSetting missingObjectFieldSetting =
			_persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingObjectFieldSetting);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		ObjectFieldSetting newObjectFieldSetting1 = addObjectFieldSetting();
		ObjectFieldSetting newObjectFieldSetting2 = addObjectFieldSetting();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newObjectFieldSetting1.getPrimaryKey());
		primaryKeys.add(newObjectFieldSetting2.getPrimaryKey());

		Map<Serializable, ObjectFieldSetting> objectFieldSettings =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, objectFieldSettings.size());
		Assert.assertEquals(
			newObjectFieldSetting1,
			objectFieldSettings.get(newObjectFieldSetting1.getPrimaryKey()));
		Assert.assertEquals(
			newObjectFieldSetting2,
			objectFieldSettings.get(newObjectFieldSetting2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, ObjectFieldSetting> objectFieldSettings =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(objectFieldSettings.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		ObjectFieldSetting newObjectFieldSetting = addObjectFieldSetting();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newObjectFieldSetting.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, ObjectFieldSetting> objectFieldSettings =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, objectFieldSettings.size());
		Assert.assertEquals(
			newObjectFieldSetting,
			objectFieldSettings.get(newObjectFieldSetting.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, ObjectFieldSetting> objectFieldSettings =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(objectFieldSettings.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		ObjectFieldSetting newObjectFieldSetting = addObjectFieldSetting();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newObjectFieldSetting.getPrimaryKey());

		Map<Serializable, ObjectFieldSetting> objectFieldSettings =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, objectFieldSettings.size());
		Assert.assertEquals(
			newObjectFieldSetting,
			objectFieldSettings.get(newObjectFieldSetting.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			ObjectFieldSettingLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<ObjectFieldSetting>() {

				@Override
				public void performAction(
					ObjectFieldSetting objectFieldSetting) {

					Assert.assertNotNull(objectFieldSetting);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		ObjectFieldSetting newObjectFieldSetting = addObjectFieldSetting();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ObjectFieldSetting.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"objectFieldSettingId",
				newObjectFieldSetting.getObjectFieldSettingId()));

		List<ObjectFieldSetting> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		ObjectFieldSetting existingObjectFieldSetting = result.get(0);

		Assert.assertEquals(existingObjectFieldSetting, newObjectFieldSetting);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ObjectFieldSetting.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"objectFieldSettingId", RandomTestUtil.nextLong()));

		List<ObjectFieldSetting> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		ObjectFieldSetting newObjectFieldSetting = addObjectFieldSetting();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ObjectFieldSetting.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("objectFieldSettingId"));

		Object newObjectFieldSettingId =
			newObjectFieldSetting.getObjectFieldSettingId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"objectFieldSettingId",
				new Object[] {newObjectFieldSettingId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingObjectFieldSettingId = result.get(0);

		Assert.assertEquals(
			existingObjectFieldSettingId, newObjectFieldSettingId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ObjectFieldSetting.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("objectFieldSettingId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"objectFieldSettingId",
				new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		ObjectFieldSetting newObjectFieldSetting = addObjectFieldSetting();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(
				newObjectFieldSetting.getPrimaryKey()));
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

		ObjectFieldSetting newObjectFieldSetting = addObjectFieldSetting();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ObjectFieldSetting.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"objectFieldSettingId",
				newObjectFieldSetting.getObjectFieldSettingId()));

		List<ObjectFieldSetting> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(ObjectFieldSetting objectFieldSetting) {
		Assert.assertEquals(
			Long.valueOf(objectFieldSetting.getObjectFieldId()),
			ReflectionTestUtil.<Long>invoke(
				objectFieldSetting, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "objectFieldId"));
		Assert.assertEquals(
			objectFieldSetting.getName(),
			ReflectionTestUtil.invoke(
				objectFieldSetting, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "name"));
	}

	protected ObjectFieldSetting addObjectFieldSetting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ObjectFieldSetting objectFieldSetting = _persistence.create(pk);

		objectFieldSetting.setMvccVersion(RandomTestUtil.nextLong());

		objectFieldSetting.setUuid(RandomTestUtil.randomString());

		objectFieldSetting.setCompanyId(RandomTestUtil.nextLong());

		objectFieldSetting.setUserId(RandomTestUtil.nextLong());

		objectFieldSetting.setUserName(RandomTestUtil.randomString());

		objectFieldSetting.setCreateDate(RandomTestUtil.nextDate());

		objectFieldSetting.setModifiedDate(RandomTestUtil.nextDate());

		objectFieldSetting.setObjectFieldId(RandomTestUtil.nextLong());

		objectFieldSetting.setName(RandomTestUtil.randomString());

		objectFieldSetting.setValue(RandomTestUtil.randomString());

		_objectFieldSettings.add(_persistence.update(objectFieldSetting));

		return objectFieldSetting;
	}

	private List<ObjectFieldSetting> _objectFieldSettings =
		new ArrayList<ObjectFieldSetting>();
	private ObjectFieldSettingPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}