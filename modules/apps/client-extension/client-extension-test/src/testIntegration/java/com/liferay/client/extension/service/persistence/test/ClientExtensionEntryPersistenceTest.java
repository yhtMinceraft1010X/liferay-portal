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

package com.liferay.client.extension.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.client.extension.exception.NoSuchClientExtensionEntryException;
import com.liferay.client.extension.model.ClientExtensionEntry;
import com.liferay.client.extension.service.ClientExtensionEntryLocalServiceUtil;
import com.liferay.client.extension.service.persistence.ClientExtensionEntryPersistence;
import com.liferay.client.extension.service.persistence.ClientExtensionEntryUtil;
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
public class ClientExtensionEntryPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.client.extension.service"));

	@Before
	public void setUp() {
		_persistence = ClientExtensionEntryUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<ClientExtensionEntry> iterator =
			_clientExtensionEntries.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ClientExtensionEntry clientExtensionEntry = _persistence.create(pk);

		Assert.assertNotNull(clientExtensionEntry);

		Assert.assertEquals(clientExtensionEntry.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		ClientExtensionEntry newClientExtensionEntry =
			addClientExtensionEntry();

		_persistence.remove(newClientExtensionEntry);

		ClientExtensionEntry existingClientExtensionEntry =
			_persistence.fetchByPrimaryKey(
				newClientExtensionEntry.getPrimaryKey());

		Assert.assertNull(existingClientExtensionEntry);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addClientExtensionEntry();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ClientExtensionEntry newClientExtensionEntry = _persistence.create(pk);

		newClientExtensionEntry.setMvccVersion(RandomTestUtil.nextLong());

		newClientExtensionEntry.setUuid(RandomTestUtil.randomString());

		newClientExtensionEntry.setExternalReferenceCode(
			RandomTestUtil.randomString());

		newClientExtensionEntry.setCompanyId(RandomTestUtil.nextLong());

		newClientExtensionEntry.setUserId(RandomTestUtil.nextLong());

		newClientExtensionEntry.setUserName(RandomTestUtil.randomString());

		newClientExtensionEntry.setCreateDate(RandomTestUtil.nextDate());

		newClientExtensionEntry.setModifiedDate(RandomTestUtil.nextDate());

		newClientExtensionEntry.setCustomElementCSSURLs(
			RandomTestUtil.randomString());

		newClientExtensionEntry.setCustomElementHTMLElementName(
			RandomTestUtil.randomString());

		newClientExtensionEntry.setCustomElementURLs(
			RandomTestUtil.randomString());

		newClientExtensionEntry.setCustomElementUseESM(
			RandomTestUtil.randomBoolean());

		newClientExtensionEntry.setDescription(RandomTestUtil.randomString());

		newClientExtensionEntry.setFriendlyURLMapping(
			RandomTestUtil.randomString());

		newClientExtensionEntry.setIFrameURL(RandomTestUtil.randomString());

		newClientExtensionEntry.setInstanceable(RandomTestUtil.randomBoolean());

		newClientExtensionEntry.setName(RandomTestUtil.randomString());

		newClientExtensionEntry.setPortletCategoryName(
			RandomTestUtil.randomString());

		newClientExtensionEntry.setProperties(RandomTestUtil.randomString());

		newClientExtensionEntry.setSourceCodeURL(RandomTestUtil.randomString());

		newClientExtensionEntry.setType(RandomTestUtil.randomString());

		newClientExtensionEntry.setStatus(RandomTestUtil.nextInt());

		newClientExtensionEntry.setStatusByUserId(RandomTestUtil.nextLong());

		newClientExtensionEntry.setStatusByUserName(
			RandomTestUtil.randomString());

		newClientExtensionEntry.setStatusDate(RandomTestUtil.nextDate());

		_clientExtensionEntries.add(
			_persistence.update(newClientExtensionEntry));

		ClientExtensionEntry existingClientExtensionEntry =
			_persistence.findByPrimaryKey(
				newClientExtensionEntry.getPrimaryKey());

		Assert.assertEquals(
			existingClientExtensionEntry.getMvccVersion(),
			newClientExtensionEntry.getMvccVersion());
		Assert.assertEquals(
			existingClientExtensionEntry.getUuid(),
			newClientExtensionEntry.getUuid());
		Assert.assertEquals(
			existingClientExtensionEntry.getExternalReferenceCode(),
			newClientExtensionEntry.getExternalReferenceCode());
		Assert.assertEquals(
			existingClientExtensionEntry.getClientExtensionEntryId(),
			newClientExtensionEntry.getClientExtensionEntryId());
		Assert.assertEquals(
			existingClientExtensionEntry.getCompanyId(),
			newClientExtensionEntry.getCompanyId());
		Assert.assertEquals(
			existingClientExtensionEntry.getUserId(),
			newClientExtensionEntry.getUserId());
		Assert.assertEquals(
			existingClientExtensionEntry.getUserName(),
			newClientExtensionEntry.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingClientExtensionEntry.getCreateDate()),
			Time.getShortTimestamp(newClientExtensionEntry.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingClientExtensionEntry.getModifiedDate()),
			Time.getShortTimestamp(newClientExtensionEntry.getModifiedDate()));
		Assert.assertEquals(
			existingClientExtensionEntry.getCustomElementCSSURLs(),
			newClientExtensionEntry.getCustomElementCSSURLs());
		Assert.assertEquals(
			existingClientExtensionEntry.getCustomElementHTMLElementName(),
			newClientExtensionEntry.getCustomElementHTMLElementName());
		Assert.assertEquals(
			existingClientExtensionEntry.getCustomElementURLs(),
			newClientExtensionEntry.getCustomElementURLs());
		Assert.assertEquals(
			existingClientExtensionEntry.isCustomElementUseESM(),
			newClientExtensionEntry.isCustomElementUseESM());
		Assert.assertEquals(
			existingClientExtensionEntry.getDescription(),
			newClientExtensionEntry.getDescription());
		Assert.assertEquals(
			existingClientExtensionEntry.getFriendlyURLMapping(),
			newClientExtensionEntry.getFriendlyURLMapping());
		Assert.assertEquals(
			existingClientExtensionEntry.getIFrameURL(),
			newClientExtensionEntry.getIFrameURL());
		Assert.assertEquals(
			existingClientExtensionEntry.isInstanceable(),
			newClientExtensionEntry.isInstanceable());
		Assert.assertEquals(
			existingClientExtensionEntry.getName(),
			newClientExtensionEntry.getName());
		Assert.assertEquals(
			existingClientExtensionEntry.getPortletCategoryName(),
			newClientExtensionEntry.getPortletCategoryName());
		Assert.assertEquals(
			existingClientExtensionEntry.getProperties(),
			newClientExtensionEntry.getProperties());
		Assert.assertEquals(
			existingClientExtensionEntry.getSourceCodeURL(),
			newClientExtensionEntry.getSourceCodeURL());
		Assert.assertEquals(
			existingClientExtensionEntry.getType(),
			newClientExtensionEntry.getType());
		Assert.assertEquals(
			existingClientExtensionEntry.getStatus(),
			newClientExtensionEntry.getStatus());
		Assert.assertEquals(
			existingClientExtensionEntry.getStatusByUserId(),
			newClientExtensionEntry.getStatusByUserId());
		Assert.assertEquals(
			existingClientExtensionEntry.getStatusByUserName(),
			newClientExtensionEntry.getStatusByUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingClientExtensionEntry.getStatusDate()),
			Time.getShortTimestamp(newClientExtensionEntry.getStatusDate()));
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
	public void testCountByC_ERC() throws Exception {
		_persistence.countByC_ERC(RandomTestUtil.nextLong(), "");

		_persistence.countByC_ERC(0L, "null");

		_persistence.countByC_ERC(0L, (String)null);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		ClientExtensionEntry newClientExtensionEntry =
			addClientExtensionEntry();

		ClientExtensionEntry existingClientExtensionEntry =
			_persistence.findByPrimaryKey(
				newClientExtensionEntry.getPrimaryKey());

		Assert.assertEquals(
			existingClientExtensionEntry, newClientExtensionEntry);
	}

	@Test(expected = NoSuchClientExtensionEntryException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<ClientExtensionEntry> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"ClientExtensionEntry", "mvccVersion", true, "uuid", true,
			"externalReferenceCode", true, "clientExtensionEntryId", true,
			"companyId", true, "userId", true, "userName", true, "createDate",
			true, "modifiedDate", true, "customElementHTMLElementName", true,
			"customElementUseESM", true, "friendlyURLMapping", true,
			"iFrameURL", true, "instanceable", true, "name", true,
			"portletCategoryName", true, "sourceCodeURL", true, "type", true,
			"status", true, "statusByUserId", true, "statusByUserName", true,
			"statusDate", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		ClientExtensionEntry newClientExtensionEntry =
			addClientExtensionEntry();

		ClientExtensionEntry existingClientExtensionEntry =
			_persistence.fetchByPrimaryKey(
				newClientExtensionEntry.getPrimaryKey());

		Assert.assertEquals(
			existingClientExtensionEntry, newClientExtensionEntry);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ClientExtensionEntry missingClientExtensionEntry =
			_persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingClientExtensionEntry);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		ClientExtensionEntry newClientExtensionEntry1 =
			addClientExtensionEntry();
		ClientExtensionEntry newClientExtensionEntry2 =
			addClientExtensionEntry();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newClientExtensionEntry1.getPrimaryKey());
		primaryKeys.add(newClientExtensionEntry2.getPrimaryKey());

		Map<Serializable, ClientExtensionEntry> clientExtensionEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, clientExtensionEntries.size());
		Assert.assertEquals(
			newClientExtensionEntry1,
			clientExtensionEntries.get(
				newClientExtensionEntry1.getPrimaryKey()));
		Assert.assertEquals(
			newClientExtensionEntry2,
			clientExtensionEntries.get(
				newClientExtensionEntry2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, ClientExtensionEntry> clientExtensionEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(clientExtensionEntries.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		ClientExtensionEntry newClientExtensionEntry =
			addClientExtensionEntry();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newClientExtensionEntry.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, ClientExtensionEntry> clientExtensionEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, clientExtensionEntries.size());
		Assert.assertEquals(
			newClientExtensionEntry,
			clientExtensionEntries.get(
				newClientExtensionEntry.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, ClientExtensionEntry> clientExtensionEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(clientExtensionEntries.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		ClientExtensionEntry newClientExtensionEntry =
			addClientExtensionEntry();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newClientExtensionEntry.getPrimaryKey());

		Map<Serializable, ClientExtensionEntry> clientExtensionEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, clientExtensionEntries.size());
		Assert.assertEquals(
			newClientExtensionEntry,
			clientExtensionEntries.get(
				newClientExtensionEntry.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			ClientExtensionEntryLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<ClientExtensionEntry>() {

				@Override
				public void performAction(
					ClientExtensionEntry clientExtensionEntry) {

					Assert.assertNotNull(clientExtensionEntry);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		ClientExtensionEntry newClientExtensionEntry =
			addClientExtensionEntry();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ClientExtensionEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"clientExtensionEntryId",
				newClientExtensionEntry.getClientExtensionEntryId()));

		List<ClientExtensionEntry> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		ClientExtensionEntry existingClientExtensionEntry = result.get(0);

		Assert.assertEquals(
			existingClientExtensionEntry, newClientExtensionEntry);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ClientExtensionEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"clientExtensionEntryId", RandomTestUtil.nextLong()));

		List<ClientExtensionEntry> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		ClientExtensionEntry newClientExtensionEntry =
			addClientExtensionEntry();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ClientExtensionEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("clientExtensionEntryId"));

		Object newClientExtensionEntryId =
			newClientExtensionEntry.getClientExtensionEntryId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"clientExtensionEntryId",
				new Object[] {newClientExtensionEntryId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingClientExtensionEntryId = result.get(0);

		Assert.assertEquals(
			existingClientExtensionEntryId, newClientExtensionEntryId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ClientExtensionEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("clientExtensionEntryId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"clientExtensionEntryId",
				new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		ClientExtensionEntry newClientExtensionEntry =
			addClientExtensionEntry();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(
				newClientExtensionEntry.getPrimaryKey()));
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

		ClientExtensionEntry newClientExtensionEntry =
			addClientExtensionEntry();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			ClientExtensionEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"clientExtensionEntryId",
				newClientExtensionEntry.getClientExtensionEntryId()));

		List<ClientExtensionEntry> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(
		ClientExtensionEntry clientExtensionEntry) {

		Assert.assertEquals(
			Long.valueOf(clientExtensionEntry.getCompanyId()),
			ReflectionTestUtil.<Long>invoke(
				clientExtensionEntry, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "companyId"));
		Assert.assertEquals(
			clientExtensionEntry.getExternalReferenceCode(),
			ReflectionTestUtil.invoke(
				clientExtensionEntry, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "externalReferenceCode"));
	}

	protected ClientExtensionEntry addClientExtensionEntry() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ClientExtensionEntry clientExtensionEntry = _persistence.create(pk);

		clientExtensionEntry.setMvccVersion(RandomTestUtil.nextLong());

		clientExtensionEntry.setUuid(RandomTestUtil.randomString());

		clientExtensionEntry.setExternalReferenceCode(
			RandomTestUtil.randomString());

		clientExtensionEntry.setCompanyId(RandomTestUtil.nextLong());

		clientExtensionEntry.setUserId(RandomTestUtil.nextLong());

		clientExtensionEntry.setUserName(RandomTestUtil.randomString());

		clientExtensionEntry.setCreateDate(RandomTestUtil.nextDate());

		clientExtensionEntry.setModifiedDate(RandomTestUtil.nextDate());

		clientExtensionEntry.setCustomElementCSSURLs(
			RandomTestUtil.randomString());

		clientExtensionEntry.setCustomElementHTMLElementName(
			RandomTestUtil.randomString());

		clientExtensionEntry.setCustomElementURLs(
			RandomTestUtil.randomString());

		clientExtensionEntry.setCustomElementUseESM(
			RandomTestUtil.randomBoolean());

		clientExtensionEntry.setDescription(RandomTestUtil.randomString());

		clientExtensionEntry.setFriendlyURLMapping(
			RandomTestUtil.randomString());

		clientExtensionEntry.setIFrameURL(RandomTestUtil.randomString());

		clientExtensionEntry.setInstanceable(RandomTestUtil.randomBoolean());

		clientExtensionEntry.setName(RandomTestUtil.randomString());

		clientExtensionEntry.setPortletCategoryName(
			RandomTestUtil.randomString());

		clientExtensionEntry.setProperties(RandomTestUtil.randomString());

		clientExtensionEntry.setSourceCodeURL(RandomTestUtil.randomString());

		clientExtensionEntry.setType(RandomTestUtil.randomString());

		clientExtensionEntry.setStatus(RandomTestUtil.nextInt());

		clientExtensionEntry.setStatusByUserId(RandomTestUtil.nextLong());

		clientExtensionEntry.setStatusByUserName(RandomTestUtil.randomString());

		clientExtensionEntry.setStatusDate(RandomTestUtil.nextDate());

		_clientExtensionEntries.add(_persistence.update(clientExtensionEntry));

		return clientExtensionEntry;
	}

	private List<ClientExtensionEntry> _clientExtensionEntries =
		new ArrayList<ClientExtensionEntry>();
	private ClientExtensionEntryPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}