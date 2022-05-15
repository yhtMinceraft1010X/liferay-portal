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

package com.liferay.template.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
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
import com.liferay.template.exception.NoSuchTemplateEntryException;
import com.liferay.template.model.TemplateEntry;
import com.liferay.template.service.TemplateEntryLocalServiceUtil;
import com.liferay.template.service.persistence.TemplateEntryPersistence;
import com.liferay.template.service.persistence.TemplateEntryUtil;

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
public class TemplateEntryPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.template.service"));

	@Before
	public void setUp() {
		_persistence = TemplateEntryUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<TemplateEntry> iterator = _templateEntries.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TemplateEntry templateEntry = _persistence.create(pk);

		Assert.assertNotNull(templateEntry);

		Assert.assertEquals(templateEntry.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		TemplateEntry newTemplateEntry = addTemplateEntry();

		_persistence.remove(newTemplateEntry);

		TemplateEntry existingTemplateEntry = _persistence.fetchByPrimaryKey(
			newTemplateEntry.getPrimaryKey());

		Assert.assertNull(existingTemplateEntry);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addTemplateEntry();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TemplateEntry newTemplateEntry = _persistence.create(pk);

		newTemplateEntry.setMvccVersion(RandomTestUtil.nextLong());

		newTemplateEntry.setCtCollectionId(RandomTestUtil.nextLong());

		newTemplateEntry.setUuid(RandomTestUtil.randomString());

		newTemplateEntry.setGroupId(RandomTestUtil.nextLong());

		newTemplateEntry.setCompanyId(RandomTestUtil.nextLong());

		newTemplateEntry.setUserId(RandomTestUtil.nextLong());

		newTemplateEntry.setUserName(RandomTestUtil.randomString());

		newTemplateEntry.setCreateDate(RandomTestUtil.nextDate());

		newTemplateEntry.setModifiedDate(RandomTestUtil.nextDate());

		newTemplateEntry.setDDMTemplateId(RandomTestUtil.nextLong());

		newTemplateEntry.setInfoItemClassName(RandomTestUtil.randomString());

		newTemplateEntry.setInfoItemFormVariationKey(
			RandomTestUtil.randomString());

		newTemplateEntry.setLastPublishDate(RandomTestUtil.nextDate());

		_templateEntries.add(_persistence.update(newTemplateEntry));

		TemplateEntry existingTemplateEntry = _persistence.findByPrimaryKey(
			newTemplateEntry.getPrimaryKey());

		Assert.assertEquals(
			existingTemplateEntry.getMvccVersion(),
			newTemplateEntry.getMvccVersion());
		Assert.assertEquals(
			existingTemplateEntry.getCtCollectionId(),
			newTemplateEntry.getCtCollectionId());
		Assert.assertEquals(
			existingTemplateEntry.getUuid(), newTemplateEntry.getUuid());
		Assert.assertEquals(
			existingTemplateEntry.getTemplateEntryId(),
			newTemplateEntry.getTemplateEntryId());
		Assert.assertEquals(
			existingTemplateEntry.getGroupId(), newTemplateEntry.getGroupId());
		Assert.assertEquals(
			existingTemplateEntry.getCompanyId(),
			newTemplateEntry.getCompanyId());
		Assert.assertEquals(
			existingTemplateEntry.getUserId(), newTemplateEntry.getUserId());
		Assert.assertEquals(
			existingTemplateEntry.getUserName(),
			newTemplateEntry.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(existingTemplateEntry.getCreateDate()),
			Time.getShortTimestamp(newTemplateEntry.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(existingTemplateEntry.getModifiedDate()),
			Time.getShortTimestamp(newTemplateEntry.getModifiedDate()));
		Assert.assertEquals(
			existingTemplateEntry.getDDMTemplateId(),
			newTemplateEntry.getDDMTemplateId());
		Assert.assertEquals(
			existingTemplateEntry.getInfoItemClassName(),
			newTemplateEntry.getInfoItemClassName());
		Assert.assertEquals(
			existingTemplateEntry.getInfoItemFormVariationKey(),
			newTemplateEntry.getInfoItemFormVariationKey());
		Assert.assertEquals(
			Time.getShortTimestamp(existingTemplateEntry.getLastPublishDate()),
			Time.getShortTimestamp(newTemplateEntry.getLastPublishDate()));
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
	public void testCountByGroupId() throws Exception {
		_persistence.countByGroupId(RandomTestUtil.nextLong());

		_persistence.countByGroupId(0L);
	}

	@Test
	public void testCountByGroupIdArrayable() throws Exception {
		_persistence.countByGroupId(new long[] {RandomTestUtil.nextLong(), 0L});
	}

	@Test
	public void testCountByDDMTemplateId() throws Exception {
		_persistence.countByDDMTemplateId(RandomTestUtil.nextLong());

		_persistence.countByDDMTemplateId(0L);
	}

	@Test
	public void testCountByG_IICN() throws Exception {
		_persistence.countByG_IICN(RandomTestUtil.nextLong(), "");

		_persistence.countByG_IICN(0L, "null");

		_persistence.countByG_IICN(0L, (String)null);
	}

	@Test
	public void testCountByG_IICN_IIFVK() throws Exception {
		_persistence.countByG_IICN_IIFVK(RandomTestUtil.nextLong(), "", "");

		_persistence.countByG_IICN_IIFVK(0L, "null", "null");

		_persistence.countByG_IICN_IIFVK(0L, (String)null, (String)null);
	}

	@Test
	public void testCountByG_IICN_IIFVKArrayable() throws Exception {
		_persistence.countByG_IICN_IIFVK(
			new long[] {RandomTestUtil.nextLong(), 0L},
			RandomTestUtil.randomString(), RandomTestUtil.randomString());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		TemplateEntry newTemplateEntry = addTemplateEntry();

		TemplateEntry existingTemplateEntry = _persistence.findByPrimaryKey(
			newTemplateEntry.getPrimaryKey());

		Assert.assertEquals(existingTemplateEntry, newTemplateEntry);
	}

	@Test(expected = NoSuchTemplateEntryException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<TemplateEntry> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"TemplateEntry", "mvccVersion", true, "ctCollectionId", true,
			"uuid", true, "templateEntryId", true, "groupId", true, "companyId",
			true, "userId", true, "userName", true, "createDate", true,
			"modifiedDate", true, "ddmTemplateId", true, "infoItemClassName",
			true, "infoItemFormVariationKey", true, "lastPublishDate", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		TemplateEntry newTemplateEntry = addTemplateEntry();

		TemplateEntry existingTemplateEntry = _persistence.fetchByPrimaryKey(
			newTemplateEntry.getPrimaryKey());

		Assert.assertEquals(existingTemplateEntry, newTemplateEntry);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TemplateEntry missingTemplateEntry = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingTemplateEntry);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		TemplateEntry newTemplateEntry1 = addTemplateEntry();
		TemplateEntry newTemplateEntry2 = addTemplateEntry();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newTemplateEntry1.getPrimaryKey());
		primaryKeys.add(newTemplateEntry2.getPrimaryKey());

		Map<Serializable, TemplateEntry> templateEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, templateEntries.size());
		Assert.assertEquals(
			newTemplateEntry1,
			templateEntries.get(newTemplateEntry1.getPrimaryKey()));
		Assert.assertEquals(
			newTemplateEntry2,
			templateEntries.get(newTemplateEntry2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, TemplateEntry> templateEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(templateEntries.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		TemplateEntry newTemplateEntry = addTemplateEntry();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newTemplateEntry.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, TemplateEntry> templateEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, templateEntries.size());
		Assert.assertEquals(
			newTemplateEntry,
			templateEntries.get(newTemplateEntry.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, TemplateEntry> templateEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(templateEntries.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		TemplateEntry newTemplateEntry = addTemplateEntry();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newTemplateEntry.getPrimaryKey());

		Map<Serializable, TemplateEntry> templateEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, templateEntries.size());
		Assert.assertEquals(
			newTemplateEntry,
			templateEntries.get(newTemplateEntry.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			TemplateEntryLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<TemplateEntry>() {

				@Override
				public void performAction(TemplateEntry templateEntry) {
					Assert.assertNotNull(templateEntry);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		TemplateEntry newTemplateEntry = addTemplateEntry();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TemplateEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"templateEntryId", newTemplateEntry.getTemplateEntryId()));

		List<TemplateEntry> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		TemplateEntry existingTemplateEntry = result.get(0);

		Assert.assertEquals(existingTemplateEntry, newTemplateEntry);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TemplateEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"templateEntryId", RandomTestUtil.nextLong()));

		List<TemplateEntry> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		TemplateEntry newTemplateEntry = addTemplateEntry();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TemplateEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("templateEntryId"));

		Object newTemplateEntryId = newTemplateEntry.getTemplateEntryId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"templateEntryId", new Object[] {newTemplateEntryId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingTemplateEntryId = result.get(0);

		Assert.assertEquals(existingTemplateEntryId, newTemplateEntryId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TemplateEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("templateEntryId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"templateEntryId", new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		TemplateEntry newTemplateEntry = addTemplateEntry();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(newTemplateEntry.getPrimaryKey()));
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

		TemplateEntry newTemplateEntry = addTemplateEntry();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TemplateEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"templateEntryId", newTemplateEntry.getTemplateEntryId()));

		List<TemplateEntry> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(TemplateEntry templateEntry) {
		Assert.assertEquals(
			templateEntry.getUuid(),
			ReflectionTestUtil.invoke(
				templateEntry, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "uuid_"));
		Assert.assertEquals(
			Long.valueOf(templateEntry.getGroupId()),
			ReflectionTestUtil.<Long>invoke(
				templateEntry, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "groupId"));

		Assert.assertEquals(
			Long.valueOf(templateEntry.getDDMTemplateId()),
			ReflectionTestUtil.<Long>invoke(
				templateEntry, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "ddmTemplateId"));
	}

	protected TemplateEntry addTemplateEntry() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TemplateEntry templateEntry = _persistence.create(pk);

		templateEntry.setMvccVersion(RandomTestUtil.nextLong());

		templateEntry.setCtCollectionId(RandomTestUtil.nextLong());

		templateEntry.setUuid(RandomTestUtil.randomString());

		templateEntry.setGroupId(RandomTestUtil.nextLong());

		templateEntry.setCompanyId(RandomTestUtil.nextLong());

		templateEntry.setUserId(RandomTestUtil.nextLong());

		templateEntry.setUserName(RandomTestUtil.randomString());

		templateEntry.setCreateDate(RandomTestUtil.nextDate());

		templateEntry.setModifiedDate(RandomTestUtil.nextDate());

		templateEntry.setDDMTemplateId(RandomTestUtil.nextLong());

		templateEntry.setInfoItemClassName(RandomTestUtil.randomString());

		templateEntry.setInfoItemFormVariationKey(
			RandomTestUtil.randomString());

		templateEntry.setLastPublishDate(RandomTestUtil.nextDate());

		_templateEntries.add(_persistence.update(templateEntry));

		return templateEntry;
	}

	private List<TemplateEntry> _templateEntries =
		new ArrayList<TemplateEntry>();
	private TemplateEntryPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}