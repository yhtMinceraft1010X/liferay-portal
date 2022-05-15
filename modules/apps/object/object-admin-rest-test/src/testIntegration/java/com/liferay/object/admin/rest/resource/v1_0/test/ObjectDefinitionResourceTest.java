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

package com.liferay.object.admin.rest.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.admin.rest.client.dto.v1_0.ObjectDefinition;
import com.liferay.object.admin.rest.client.dto.v1_0.ObjectField;
import com.liferay.object.admin.rest.client.pagination.Page;
import com.liferay.object.admin.rest.client.problem.Problem;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.exception.NoSuchObjectDefinitionException;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.util.PropsUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Javier Gamarra
 */
@RunWith(Arquillian.class)
public class ObjectDefinitionResourceTest
	extends BaseObjectDefinitionResourceTestCase {

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();

		if (_objectDefinition != null) {
			try {
				_objectDefinitionLocalService.deleteObjectDefinition(
					_objectDefinition.getId());
			}
			catch (NoSuchObjectDefinitionException
						noSuchObjectDefinitionException) {

				if (_log.isDebugEnabled()) {
					_log.debug(noSuchObjectDefinitionException);
				}
			}
		}
	}

	@Override
	@Test
	public void testGetObjectDefinitionsPageWithSortString() throws Exception {
		ObjectDefinition objectDefinition1 = randomObjectDefinition();

		objectDefinition1.setName("A" + objectDefinition1.getName());

		objectDefinition1 = testGetObjectDefinitionsPage_addObjectDefinition(
			objectDefinition1);

		ObjectDefinition objectDefinition2 = randomObjectDefinition();

		objectDefinition2.setName("B" + objectDefinition2.getName());

		objectDefinition2 = testGetObjectDefinitionsPage_addObjectDefinition(
			objectDefinition2);

		Page<ObjectDefinition> ascPage =
			objectDefinitionResource.getObjectDefinitionsPage(
				null, null, null, null, "name:asc");

		List<ObjectDefinition> objectDefinitions =
			(List<ObjectDefinition>)ascPage.getItems();

		assertEquals(
			Arrays.asList(objectDefinition1, objectDefinition2),
			objectDefinitions.subList(1, 3));

		Page<ObjectDefinition> descPage =
			objectDefinitionResource.getObjectDefinitionsPage(
				null, null, null, null, "name:desc");

		objectDefinitions = (List<ObjectDefinition>)descPage.getItems();

		assertEquals(
			Arrays.asList(objectDefinition2, objectDefinition1),
			objectDefinitions.subList(
				objectDefinitions.size() - 3, objectDefinitions.size() - 1));
	}

	@Ignore
	@Override
	@Test
	public void testGraphQLGetObjectDefinitionNotFound() {
	}

	@Test
	public void testPutObjectDefinitionWithStorageType() throws Exception {
		ObjectDefinition postObjectDefinition =
			testPutObjectDefinition_addObjectDefinition();

		ObjectDefinition randomObjectDefinition = randomObjectDefinition();

		randomObjectDefinition.setStorageType(
			ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT);

		try {
			objectDefinitionResource.putObjectDefinition(
				postObjectDefinition.getId(), randomObjectDefinition);

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Problem problem = problemException.getProblem();

			Assert.assertEquals("BAD_REQUEST", problem.getStatus());
		}
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"name"};
	}

	@Override
	protected String[] getIgnoredEntityFieldNames() {
		return new String[] {"dateCreated", "dateModified", "userId"};
	}

	@Override
	protected ObjectDefinition randomObjectDefinition() throws Exception {
		ObjectDefinition objectDefinition = super.randomObjectDefinition();

		objectDefinition.setActive(false);
		objectDefinition.setLabel(
			Collections.singletonMap(
				"en_US", "O" + objectDefinition.getName()));
		objectDefinition.setName("O" + objectDefinition.getName());
		objectDefinition.setPluralLabel(
			Collections.singletonMap(
				"en_US", "O" + objectDefinition.getName()));
		objectDefinition.setObjectFields(
			new ObjectField[] {
				new ObjectField() {
					{
						setBusinessType(BusinessType.TEXT);
						setDBType(ObjectField.DBType.create("String"));
						setLabel(Collections.singletonMap("en_US", "Column"));
						setName("column");
					}
				}
			});
		objectDefinition.setScope(ObjectDefinitionConstants.SCOPE_COMPANY);

		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-135430"))) {
			objectDefinition.setStorageType(StringPool.BLANK);
		}

		objectDefinition.setTitleObjectFieldId(Long.valueOf(0));

		return objectDefinition;
	}

	@Override
	protected ObjectDefinition testDeleteObjectDefinition_addObjectDefinition()
		throws Exception {

		return _addObjectDefinition(randomObjectDefinition());
	}

	@Override
	protected ObjectDefinition testGetObjectDefinition_addObjectDefinition()
		throws Exception {

		return _addObjectDefinition(randomObjectDefinition());
	}

	@Override
	protected ObjectDefinition testGetObjectDefinitionsPage_addObjectDefinition(
			ObjectDefinition objectDefinition)
		throws Exception {

		return _addObjectDefinition(objectDefinition);
	}

	@Override
	protected ObjectDefinition testGraphQLObjectDefinition_addObjectDefinition()
		throws Exception {

		return _addObjectDefinition(randomObjectDefinition());
	}

	@Override
	protected ObjectDefinition testPatchObjectDefinition_addObjectDefinition()
		throws Exception {

		return _addObjectDefinition(randomObjectDefinition());
	}

	@Override
	protected ObjectDefinition testPostObjectDefinition_addObjectDefinition(
			ObjectDefinition objectDefinition)
		throws Exception {

		return _addObjectDefinition(objectDefinition);
	}

	@Override
	protected ObjectDefinition
			testPostObjectDefinitionPublish_addObjectDefinition()
		throws Exception {

		return _addObjectDefinition(randomObjectDefinition());
	}

	@Override
	protected ObjectDefinition testPutObjectDefinition_addObjectDefinition()
		throws Exception {

		return _addObjectDefinition(randomObjectDefinition());
	}

	private ObjectDefinition _addObjectDefinition(
			ObjectDefinition objectDefinition)
		throws Exception {

		_objectDefinition = objectDefinitionResource.postObjectDefinition(
			objectDefinition);

		return _objectDefinition;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ObjectDefinitionResourceTest.class);

	private ObjectDefinition _objectDefinition;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

}