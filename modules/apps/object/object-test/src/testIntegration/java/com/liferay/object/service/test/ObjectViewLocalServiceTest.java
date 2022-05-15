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

package com.liferay.object.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.exception.DefaultObjectViewException;
import com.liferay.object.exception.ObjectViewColumnFieldNameException;
import com.liferay.object.exception.ObjectViewSortColumnException;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectView;
import com.liferay.object.model.ObjectViewColumn;
import com.liferay.object.model.ObjectViewSortColumn;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.service.ObjectViewLocalService;
import com.liferay.object.service.persistence.ObjectViewColumnPersistence;
import com.liferay.object.service.persistence.ObjectViewSortColumnPersistence;
import com.liferay.object.service.test.util.ObjectDefinitionTestUtil;
import com.liferay.object.util.LocalizedMapUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Gabriel Albuquerque
 */
@RunWith(Arquillian.class)
public class ObjectViewLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_objectDefinition = ObjectDefinitionTestUtil.addObjectDefinition(
			_objectDefinitionLocalService);
	}

	@Test
	public void testAddObjectView() throws Exception {
		ObjectView objectView = _objectViewLocalService.addObjectView(
			TestPropsValues.getUserId(),
			_objectDefinition.getObjectDefinitionId(), true,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			Arrays.asList(
				_createObjectViewColumn("Able", "able"),
				_createObjectViewColumn("Baker", "baker")),
			Collections.emptyList(),
			Arrays.asList(
				_createObjectViewSortColumn("able", "asc"),
				_createObjectViewSortColumn("baker", "asc")));

		try {
			_objectViewLocalService.addObjectView(
				TestPropsValues.getUserId(),
				_objectDefinition.getObjectDefinitionId(), true,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				Arrays.asList(
					_createObjectViewColumn("Easy", "easy"),
					_createObjectViewColumn("Fox", "fox")),
				Collections.emptyList(),
				Arrays.asList(
					_createObjectViewSortColumn("easy", "asc"),
					_createObjectViewSortColumn("fox", "asc")));

			Assert.fail();
		}
		catch (DefaultObjectViewException defaultObjectViewException) {
			String message = defaultObjectViewException.getMessage();

			Assert.assertTrue(
				message.contains("There can only be one default object view"));
		}

		try {
			_objectViewLocalService.addObjectView(
				TestPropsValues.getUserId(),
				_objectDefinition.getObjectDefinitionId(), false,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				Arrays.asList(
					_createObjectViewColumnWithNonexistentObjectFieldName()),
				Collections.emptyList(), Collections.emptyList());
		}
		catch (ObjectViewColumnFieldNameException
					objectViewColumnFieldNameException) {

			Assert.assertEquals(
				"There is no object field with the name: zebra",
				objectViewColumnFieldNameException.getMessage());
		}

		try {
			_objectViewLocalService.addObjectView(
				TestPropsValues.getUserId(),
				_objectDefinition.getObjectDefinitionId(), false,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				_createObjectViewColumnsWithDuplicateObjectFieldName(),
				Collections.emptyList(), Collections.emptyList());
		}
		catch (ObjectViewColumnFieldNameException
					objectViewColumnFieldNameException) {

			Assert.assertEquals(
				"There is already an object view column with the object " +
					"field name: roger",
				objectViewColumnFieldNameException.getMessage());
		}

		_objectViewLocalService.addObjectView(
			TestPropsValues.getUserId(),
			_objectDefinition.getObjectDefinitionId(), false,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			_createObjectViewColumnsWithoutLabel(), Collections.emptyList(),
			Collections.emptyList());

		try {
			_objectViewLocalService.addObjectView(
				TestPropsValues.getUserId(),
				_objectDefinition.getObjectDefinitionId(), false,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				Arrays.asList(_createObjectViewColumn("Item", "item")),
				Collections.emptyList(),
				Arrays.asList(
					_createObjectViewSortColumnWithWrongObjectFieldName()));
		}
		catch (ObjectViewSortColumnException objectViewSortColumnException) {
			Assert.assertEquals(
				"There is no object view column with the name: zulu",
				objectViewSortColumnException.getMessage());
		}

		try {
			_objectViewLocalService.addObjectView(
				TestPropsValues.getUserId(),
				_objectDefinition.getObjectDefinitionId(), false,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				Arrays.asList(_createObjectViewColumn("King", "king")),
				Collections.emptyList(),
				Arrays.asList(_createObjectViewSortColumn("king", "zulu")));
		}
		catch (ObjectViewSortColumnException objectViewSortColumnException) {
			Assert.assertEquals(
				"There is no sort order of type: zulu",
				objectViewSortColumnException.getMessage());
		}

		_deleteObjectFields();

		_objectViewLocalService.deleteObjectView(objectView.getObjectViewId());

		objectView = _addObjectView();

		_assertObjectView(objectView);

		_deleteObjectFields();

		_objectViewLocalService.deleteObjectView(objectView.getObjectViewId());
	}

	@Test
	public void testGetObjectView() throws Exception {
		ObjectView objectView = _addObjectView();

		objectView = _objectViewLocalService.getObjectView(
			objectView.getObjectViewId());

		_assertObjectView(objectView);

		_deleteObjectFields();

		_objectViewLocalService.deleteObjectView(objectView.getObjectViewId());
	}

	@Test
	public void testUpdateObjectView() throws Exception {
		ObjectView objectView = _addObjectView();

		objectView = _objectViewLocalService.updateObjectView(
			objectView.getObjectViewId(), objectView.isDefaultObjectView(),
			objectView.getNameMap(),
			Collections.singletonList(_createObjectViewColumn("Fox", "fox")),
			Collections.emptyList(),
			Collections.singletonList(
				_createObjectViewSortColumn("fox", "desc")));

		List<ObjectViewColumn> objectViewColumns =
			objectView.getObjectViewColumns();

		Assert.assertEquals(
			objectViewColumns.toString(), 1, objectViewColumns.size());

		List<ObjectViewSortColumn> objectViewSortColumns =
			objectView.getObjectViewSortColumns();

		Assert.assertEquals(
			objectViewSortColumns.toString(), 1, objectViewSortColumns.size());

		try {
			_objectViewLocalService.updateObjectView(
				objectView.getObjectViewId(), objectView.isDefaultObjectView(),
				objectView.getNameMap(),
				Collections.singletonList(
					_createObjectViewColumnWithNonexistentObjectFieldName()),
				Collections.emptyList(), Collections.emptyList());
		}
		catch (ObjectViewColumnFieldNameException
					objectViewColumnFieldNameException) {

			Assert.assertEquals(
				"There is no object field with the name: zebra",
				objectViewColumnFieldNameException.getMessage());
		}

		try {
			_objectViewLocalService.updateObjectView(
				objectView.getObjectViewId(), objectView.isDefaultObjectView(),
				objectView.getNameMap(),
				_createObjectViewColumnsWithDuplicateObjectFieldName(),
				Collections.emptyList(), Collections.emptyList());
		}
		catch (ObjectViewColumnFieldNameException
					objectViewColumnFieldNameException) {

			Assert.assertEquals(
				"There is already an object view column with the object " +
					"field name: roger",
				objectViewColumnFieldNameException.getMessage());
		}

		try {
			_objectViewLocalService.updateObjectView(
				objectView.getObjectViewId(), objectView.isDefaultObjectView(),
				objectView.getNameMap(),
				Collections.singletonList(
					_createObjectViewColumn("Jig", "jig")),
				Collections.emptyList(),
				Collections.singletonList(
					_createObjectViewSortColumn("jig", "desc")));
		}
		catch (ObjectViewSortColumnException objectViewSortColumnException) {
			Assert.assertEquals(
				"There is no object view column with the name: king",
				objectViewSortColumnException.getMessage());
		}

		try {
			_objectViewLocalService.updateObjectView(
				objectView.getObjectViewId(), objectView.isDefaultObjectView(),
				objectView.getNameMap(),
				Collections.singletonList(
					_createObjectViewColumn("Love", "love")),
				Collections.emptyList(),
				Collections.singletonList(
					_createObjectViewSortColumn("love", "zulu")));
		}
		catch (ObjectViewSortColumnException objectViewSortColumnException) {
			Assert.assertEquals(
				"There is no sort order of type: zulu",
				objectViewSortColumnException.getMessage());
		}

		objectView = _objectViewLocalService.updateObjectView(
			objectView.getObjectViewId(), objectView.isDefaultObjectView(),
			objectView.getNameMap(), Collections.emptyList(),
			Collections.emptyList(), Collections.emptyList());

		objectViewColumns = objectView.getObjectViewColumns();

		Assert.assertEquals(
			objectViewColumns.toString(), 0, objectViewColumns.size());

		objectViewSortColumns = objectView.getObjectViewSortColumns();

		Assert.assertEquals(
			objectViewSortColumns.toString(), 0, objectViewSortColumns.size());

		_objectViewLocalService.updateObjectView(
			objectView.getObjectViewId(), objectView.isDefaultObjectView(),
			objectView.getNameMap(), _createObjectViewColumnsWithoutLabel(),
			Collections.emptyList(), Collections.emptyList());

		_deleteObjectFields();

		_objectViewLocalService.deleteObjectView(objectView.getObjectViewId());
	}

	private String _addObjectField(
			String objectFieldLabel, String objectFieldName)
		throws Exception {

		ObjectField objectField = _objectFieldLocalService.addCustomObjectField(
			TestPropsValues.getUserId(), 0,
			_objectDefinition.getObjectDefinitionId(), "Text", "String", false,
			false, null, LocalizedMapUtil.getLocalizedMap(objectFieldLabel),
			objectFieldName, true, Collections.emptyList());

		return objectField.getName();
	}

	private ObjectView _addObjectView() throws Exception {
		return _objectViewLocalService.addObjectView(
			TestPropsValues.getUserId(),
			_objectDefinition.getObjectDefinitionId(), true,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			Arrays.asList(
				_createObjectViewColumn("Able", "able"),
				_createObjectViewColumn("Baker", "baker")),
			Collections.emptyList(),
			Arrays.asList(
				_createObjectViewSortColumn("able", "asc"),
				_createObjectViewSortColumn("baker", "asc")));
	}

	private void _assertObjectView(ObjectView objectView) {
		List<ObjectViewColumn> objectViewColumns =
			objectView.getObjectViewColumns();

		Assert.assertEquals(
			objectViewColumns.toString(), 2, objectViewColumns.size());

		List<ObjectViewSortColumn> objectViewSortColumns =
			objectView.getObjectViewSortColumns();

		Assert.assertEquals(
			objectViewSortColumns.toString(), 2, objectViewSortColumns.size());
	}

	private ObjectViewColumn _createObjectViewColumn(
			String objectFieldLabel, String objectFieldName)
		throws Exception {

		ObjectViewColumn objectViewColumn = _objectViewColumnPersistence.create(
			0);

		objectViewColumn.setLabelMap(
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()));
		objectViewColumn.setObjectFieldName(
			_addObjectField(objectFieldLabel, objectFieldName));
		objectViewColumn.setPriority(0);

		return objectViewColumn;
	}

	private List<ObjectViewColumn>
			_createObjectViewColumnsWithDuplicateObjectFieldName()
		throws Exception {

		ObjectViewColumn objectViewColumn1 = _createObjectViewColumn(
			"Roger", "roger");

		ObjectViewColumn objectViewColumn2 = _createObjectViewColumn(
			RandomTestUtil.randomString(),
			StringUtil.toLowerCase(RandomStringUtils.randomAlphabetic(5)));

		objectViewColumn2.setObjectFieldName("roger");

		return ListUtil.fromArray(objectViewColumn1, objectViewColumn2);
	}

	private List<ObjectViewColumn> _createObjectViewColumnsWithoutLabel()
		throws Exception {

		ObjectViewColumn objectViewColumn = _createObjectViewColumn(
			RandomTestUtil.randomString(),
			StringUtil.toLowerCase(RandomStringUtils.randomAlphabetic(5)));

		objectViewColumn.setLabelMap(LocalizedMapUtil.getLocalizedMap(""));

		return ListUtil.fromArray(objectViewColumn);
	}

	private ObjectViewColumn
			_createObjectViewColumnWithNonexistentObjectFieldName()
		throws Exception {

		ObjectViewColumn objectViewColumn = _createObjectViewColumn(
			RandomTestUtil.randomString(),
			StringUtil.toLowerCase(RandomStringUtils.randomAlphabetic(5)));

		objectViewColumn.setObjectFieldName("zebra");

		return objectViewColumn;
	}

	private ObjectViewSortColumn _createObjectViewSortColumn(
		String objectFieldName, String sortOrder) {

		ObjectViewSortColumn objectViewSortColumn =
			_objectViewSortColumnPersistence.create(0);

		objectViewSortColumn.setObjectFieldName(objectFieldName);
		objectViewSortColumn.setPriority(0);
		objectViewSortColumn.setSortOrder(sortOrder);

		return objectViewSortColumn;
	}

	private ObjectViewSortColumn
		_createObjectViewSortColumnWithWrongObjectFieldName() {

		ObjectViewSortColumn objectViewSortColumn = _createObjectViewSortColumn(
			"item", "asc");

		objectViewSortColumn.setObjectFieldName("zulu");

		return objectViewSortColumn;
	}

	private void _deleteObjectFields() throws Exception {
		List<ObjectField> objectFields =
			_objectFieldLocalService.getObjectFields(
				_objectDefinition.getObjectDefinitionId());

		for (ObjectField objectField : objectFields) {
			_objectFieldLocalService.deleteObjectField(objectField);
		}
	}

	@DeleteAfterTestRun
	private ObjectDefinition _objectDefinition;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Inject
	private ObjectFieldLocalService _objectFieldLocalService;

	@Inject
	private ObjectViewColumnPersistence _objectViewColumnPersistence;

	@Inject
	private ObjectViewLocalService _objectViewLocalService;

	@Inject
	private ObjectViewSortColumnPersistence _objectViewSortColumnPersistence;

}