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

package com.liferay.alloy.mvc.sample.service.persistence.test;

import com.liferay.alloy.mvc.sample.exception.NoSuchTodoItemException;
import com.liferay.alloy.mvc.sample.model.TodoItem;
import com.liferay.alloy.mvc.sample.service.TodoItemLocalServiceUtil;
import com.liferay.alloy.mvc.sample.service.persistence.TodoItemPersistence;
import com.liferay.alloy.mvc.sample.service.persistence.TodoItemUtil;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
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
public class TodoItemPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.alloy.mvc.sample.service"));

	@Before
	public void setUp() {
		_persistence = TodoItemUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<TodoItem> iterator = _todoItems.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TodoItem todoItem = _persistence.create(pk);

		Assert.assertNotNull(todoItem);

		Assert.assertEquals(todoItem.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		TodoItem newTodoItem = addTodoItem();

		_persistence.remove(newTodoItem);

		TodoItem existingTodoItem = _persistence.fetchByPrimaryKey(
			newTodoItem.getPrimaryKey());

		Assert.assertNull(existingTodoItem);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addTodoItem();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TodoItem newTodoItem = _persistence.create(pk);

		newTodoItem.setMvccVersion(RandomTestUtil.nextLong());

		newTodoItem.setCompanyId(RandomTestUtil.nextLong());

		newTodoItem.setUserId(RandomTestUtil.nextLong());

		newTodoItem.setUserName(RandomTestUtil.randomString());

		newTodoItem.setCreateDate(RandomTestUtil.nextDate());

		newTodoItem.setModifiedDate(RandomTestUtil.nextDate());

		newTodoItem.setTodoListId(RandomTestUtil.nextLong());

		newTodoItem.setDescription(RandomTestUtil.randomString());

		newTodoItem.setPriority(RandomTestUtil.nextInt());

		newTodoItem.setStatus(RandomTestUtil.nextInt());

		_todoItems.add(_persistence.update(newTodoItem));

		TodoItem existingTodoItem = _persistence.findByPrimaryKey(
			newTodoItem.getPrimaryKey());

		Assert.assertEquals(
			existingTodoItem.getMvccVersion(), newTodoItem.getMvccVersion());
		Assert.assertEquals(
			existingTodoItem.getTodoItemId(), newTodoItem.getTodoItemId());
		Assert.assertEquals(
			existingTodoItem.getCompanyId(), newTodoItem.getCompanyId());
		Assert.assertEquals(
			existingTodoItem.getUserId(), newTodoItem.getUserId());
		Assert.assertEquals(
			existingTodoItem.getUserName(), newTodoItem.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(existingTodoItem.getCreateDate()),
			Time.getShortTimestamp(newTodoItem.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(existingTodoItem.getModifiedDate()),
			Time.getShortTimestamp(newTodoItem.getModifiedDate()));
		Assert.assertEquals(
			existingTodoItem.getTodoListId(), newTodoItem.getTodoListId());
		Assert.assertEquals(
			existingTodoItem.getDescription(), newTodoItem.getDescription());
		Assert.assertEquals(
			existingTodoItem.getPriority(), newTodoItem.getPriority());
		Assert.assertEquals(
			existingTodoItem.getStatus(), newTodoItem.getStatus());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		TodoItem newTodoItem = addTodoItem();

		TodoItem existingTodoItem = _persistence.findByPrimaryKey(
			newTodoItem.getPrimaryKey());

		Assert.assertEquals(existingTodoItem, newTodoItem);
	}

	@Test(expected = NoSuchTodoItemException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<TodoItem> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"AlloyMVCSample_TodoItem", "mvccVersion", true, "todoItemId", true,
			"companyId", true, "userId", true, "userName", true, "createDate",
			true, "modifiedDate", true, "todoListId", true, "description", true,
			"priority", true, "status", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		TodoItem newTodoItem = addTodoItem();

		TodoItem existingTodoItem = _persistence.fetchByPrimaryKey(
			newTodoItem.getPrimaryKey());

		Assert.assertEquals(existingTodoItem, newTodoItem);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TodoItem missingTodoItem = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingTodoItem);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		TodoItem newTodoItem1 = addTodoItem();
		TodoItem newTodoItem2 = addTodoItem();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newTodoItem1.getPrimaryKey());
		primaryKeys.add(newTodoItem2.getPrimaryKey());

		Map<Serializable, TodoItem> todoItems = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertEquals(2, todoItems.size());
		Assert.assertEquals(
			newTodoItem1, todoItems.get(newTodoItem1.getPrimaryKey()));
		Assert.assertEquals(
			newTodoItem2, todoItems.get(newTodoItem2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, TodoItem> todoItems = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertTrue(todoItems.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		TodoItem newTodoItem = addTodoItem();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newTodoItem.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, TodoItem> todoItems = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertEquals(1, todoItems.size());
		Assert.assertEquals(
			newTodoItem, todoItems.get(newTodoItem.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, TodoItem> todoItems = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertTrue(todoItems.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		TodoItem newTodoItem = addTodoItem();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newTodoItem.getPrimaryKey());

		Map<Serializable, TodoItem> todoItems = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertEquals(1, todoItems.size());
		Assert.assertEquals(
			newTodoItem, todoItems.get(newTodoItem.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			TodoItemLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<TodoItem>() {

				@Override
				public void performAction(TodoItem todoItem) {
					Assert.assertNotNull(todoItem);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		TodoItem newTodoItem = addTodoItem();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TodoItem.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"todoItemId", newTodoItem.getTodoItemId()));

		List<TodoItem> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		TodoItem existingTodoItem = result.get(0);

		Assert.assertEquals(existingTodoItem, newTodoItem);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TodoItem.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"todoItemId", RandomTestUtil.nextLong()));

		List<TodoItem> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		TodoItem newTodoItem = addTodoItem();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TodoItem.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("todoItemId"));

		Object newTodoItemId = newTodoItem.getTodoItemId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"todoItemId", new Object[] {newTodoItemId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingTodoItemId = result.get(0);

		Assert.assertEquals(existingTodoItemId, newTodoItemId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TodoItem.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("todoItemId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"todoItemId", new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected TodoItem addTodoItem() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TodoItem todoItem = _persistence.create(pk);

		todoItem.setMvccVersion(RandomTestUtil.nextLong());

		todoItem.setCompanyId(RandomTestUtil.nextLong());

		todoItem.setUserId(RandomTestUtil.nextLong());

		todoItem.setUserName(RandomTestUtil.randomString());

		todoItem.setCreateDate(RandomTestUtil.nextDate());

		todoItem.setModifiedDate(RandomTestUtil.nextDate());

		todoItem.setTodoListId(RandomTestUtil.nextLong());

		todoItem.setDescription(RandomTestUtil.randomString());

		todoItem.setPriority(RandomTestUtil.nextInt());

		todoItem.setStatus(RandomTestUtil.nextInt());

		_todoItems.add(_persistence.update(todoItem));

		return todoItem;
	}

	private List<TodoItem> _todoItems = new ArrayList<TodoItem>();
	private TodoItemPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}