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

import com.liferay.alloy.mvc.sample.exception.NoSuchTodoListException;
import com.liferay.alloy.mvc.sample.model.TodoList;
import com.liferay.alloy.mvc.sample.service.TodoListLocalServiceUtil;
import com.liferay.alloy.mvc.sample.service.persistence.TodoListPersistence;
import com.liferay.alloy.mvc.sample.service.persistence.TodoListUtil;
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
public class TodoListPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.alloy.mvc.sample.service"));

	@Before
	public void setUp() {
		_persistence = TodoListUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<TodoList> iterator = _todoLists.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TodoList todoList = _persistence.create(pk);

		Assert.assertNotNull(todoList);

		Assert.assertEquals(todoList.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		TodoList newTodoList = addTodoList();

		_persistence.remove(newTodoList);

		TodoList existingTodoList = _persistence.fetchByPrimaryKey(
			newTodoList.getPrimaryKey());

		Assert.assertNull(existingTodoList);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addTodoList();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TodoList newTodoList = _persistence.create(pk);

		newTodoList.setMvccVersion(RandomTestUtil.nextLong());

		newTodoList.setCompanyId(RandomTestUtil.nextLong());

		newTodoList.setUserId(RandomTestUtil.nextLong());

		newTodoList.setUserName(RandomTestUtil.randomString());

		newTodoList.setCreateDate(RandomTestUtil.nextDate());

		newTodoList.setModifiedDate(RandomTestUtil.nextDate());

		newTodoList.setName(RandomTestUtil.randomString());

		_todoLists.add(_persistence.update(newTodoList));

		TodoList existingTodoList = _persistence.findByPrimaryKey(
			newTodoList.getPrimaryKey());

		Assert.assertEquals(
			existingTodoList.getMvccVersion(), newTodoList.getMvccVersion());
		Assert.assertEquals(
			existingTodoList.getTodoListId(), newTodoList.getTodoListId());
		Assert.assertEquals(
			existingTodoList.getCompanyId(), newTodoList.getCompanyId());
		Assert.assertEquals(
			existingTodoList.getUserId(), newTodoList.getUserId());
		Assert.assertEquals(
			existingTodoList.getUserName(), newTodoList.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(existingTodoList.getCreateDate()),
			Time.getShortTimestamp(newTodoList.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(existingTodoList.getModifiedDate()),
			Time.getShortTimestamp(newTodoList.getModifiedDate()));
		Assert.assertEquals(existingTodoList.getName(), newTodoList.getName());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		TodoList newTodoList = addTodoList();

		TodoList existingTodoList = _persistence.findByPrimaryKey(
			newTodoList.getPrimaryKey());

		Assert.assertEquals(existingTodoList, newTodoList);
	}

	@Test(expected = NoSuchTodoListException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<TodoList> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"AlloyMVCSample_TodoList", "mvccVersion", true, "todoListId", true,
			"companyId", true, "userId", true, "userName", true, "createDate",
			true, "modifiedDate", true, "name", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		TodoList newTodoList = addTodoList();

		TodoList existingTodoList = _persistence.fetchByPrimaryKey(
			newTodoList.getPrimaryKey());

		Assert.assertEquals(existingTodoList, newTodoList);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TodoList missingTodoList = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingTodoList);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		TodoList newTodoList1 = addTodoList();
		TodoList newTodoList2 = addTodoList();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newTodoList1.getPrimaryKey());
		primaryKeys.add(newTodoList2.getPrimaryKey());

		Map<Serializable, TodoList> todoLists = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertEquals(2, todoLists.size());
		Assert.assertEquals(
			newTodoList1, todoLists.get(newTodoList1.getPrimaryKey()));
		Assert.assertEquals(
			newTodoList2, todoLists.get(newTodoList2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, TodoList> todoLists = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertTrue(todoLists.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		TodoList newTodoList = addTodoList();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newTodoList.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, TodoList> todoLists = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertEquals(1, todoLists.size());
		Assert.assertEquals(
			newTodoList, todoLists.get(newTodoList.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, TodoList> todoLists = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertTrue(todoLists.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		TodoList newTodoList = addTodoList();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newTodoList.getPrimaryKey());

		Map<Serializable, TodoList> todoLists = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertEquals(1, todoLists.size());
		Assert.assertEquals(
			newTodoList, todoLists.get(newTodoList.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			TodoListLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<TodoList>() {

				@Override
				public void performAction(TodoList todoList) {
					Assert.assertNotNull(todoList);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		TodoList newTodoList = addTodoList();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TodoList.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"todoListId", newTodoList.getTodoListId()));

		List<TodoList> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		TodoList existingTodoList = result.get(0);

		Assert.assertEquals(existingTodoList, newTodoList);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TodoList.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"todoListId", RandomTestUtil.nextLong()));

		List<TodoList> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		TodoList newTodoList = addTodoList();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TodoList.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("todoListId"));

		Object newTodoListId = newTodoList.getTodoListId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"todoListId", new Object[] {newTodoListId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingTodoListId = result.get(0);

		Assert.assertEquals(existingTodoListId, newTodoListId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TodoList.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("todoListId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"todoListId", new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected TodoList addTodoList() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TodoList todoList = _persistence.create(pk);

		todoList.setMvccVersion(RandomTestUtil.nextLong());

		todoList.setCompanyId(RandomTestUtil.nextLong());

		todoList.setUserId(RandomTestUtil.nextLong());

		todoList.setUserName(RandomTestUtil.randomString());

		todoList.setCreateDate(RandomTestUtil.nextDate());

		todoList.setModifiedDate(RandomTestUtil.nextDate());

		todoList.setName(RandomTestUtil.randomString());

		_todoLists.add(_persistence.update(todoList));

		return todoList;
	}

	private List<TodoList> _todoLists = new ArrayList<TodoList>();
	private TodoListPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}