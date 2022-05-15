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

package com.liferay.headless.delivery.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.DepotEntryLocalServiceUtil;
import com.liferay.headless.delivery.client.dto.v1_0.ContentElement;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.petra.function.UnsafeTriConsumer;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.odata.entity.EntityField;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Javier Gamarra
 */
@RunWith(Arquillian.class)
public class ContentElementResourceTest
	extends BaseContentElementResourceTestCase {

	@Override
	@Test
	public void testGetAssetLibraryContentElementsPageWithSortDouble()
		throws Exception {

		testGetAssetLibraryContentElementsPageWithSort(
			EntityField.Type.DOUBLE, _getUnsafeTriConsumer());
	}

	@Override
	@Test
	public void testGetSiteContentElementsPageWithSortDouble()
		throws Exception {

		testGetSiteContentElementsPageWithSort(
			EntityField.Type.DOUBLE, _getUnsafeTriConsumer());
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"title"};
	}

	@Override
	protected String getFilterString(
		EntityField entityField, String operator,
		ContentElement contentElement) {

		String entityFieldName = entityField.getName();

		if (entityFieldName.equals("priority")) {
			StringBundler sb = new StringBundler(5);

			sb.append(entityFieldName);
			sb.append(" ");
			sb.append(operator);
			sb.append(" ");
			sb.append(String.valueOf(_get(contentElement, entityFieldName)));

			return sb.toString();
		}

		return super.getFilterString(entityField, operator, contentElement);
	}

	@Override
	protected String[] getIgnoredEntityFieldNames() {
		return new String[] {
			"contentType", "creatorId", "dateCreated", "dateModified"
		};
	}

	@Override
	protected ContentElement randomContentElement() throws Exception {
		ContentElement contentElement = super.randomContentElement();

		_put(contentElement, "priority", RandomTestUtil.randomDouble());

		return contentElement;
	}

	@Override
	protected ContentElement
			testGetAssetLibraryContentElementsPage_addContentElement(
				Long assetLibraryId, ContentElement contentElement)
		throws Exception {

		DepotEntry depotEntry = DepotEntryLocalServiceUtil.getDepotEntry(
			assetLibraryId);

		return _addContentElement(
			contentElement, (Double)_get(contentElement, "priority"),
			depotEntry.getGroupId());
	}

	@Override
	protected ContentElement testGetSiteContentElementsPage_addContentElement(
			Long siteId, ContentElement contentElement)
		throws Exception {

		return _addContentElement(
			contentElement, (Double)_get(contentElement, "priority"), siteId);
	}

	@Override
	protected ContentElement testGraphQLContentElement_addContentElement()
		throws Exception {

		return _addContentElement(
			randomContentElement(), null, testGroup.getGroupId());
	}

	private ContentElement _addContentElement(
			ContentElement contentElement, Double priority, Long siteId)
		throws Exception {

		contentElement = _toContentElement(
			JournalTestUtil.addArticle(
				siteId, 0L, String.valueOf(contentElement.getId()),
				contentElement.getTitle(), contentElement.getTitle(),
				contentElement.getTitle(), priority));

		_put(contentElement, "priority", priority);

		return contentElement;
	}

	private Object _get(ContentElement contentElement, String fieldName) {
		Map<String, Object> fieldValueMap = _fieldValueMaps.get(contentElement);

		if (fieldValueMap == null) {
			return null;
		}

		return fieldValueMap.getOrDefault(fieldName, null);
	}

	private UnsafeTriConsumer
		<EntityField, ContentElement, ContentElement, Exception>
			_getUnsafeTriConsumer() {

		return (entityField, contentElement1, contentElement2) -> {
			BeanUtils.setProperty(contentElement1, entityField.getName(), 0.1);
			_put(contentElement1, entityField.getName(), 0.1);
			BeanUtils.setProperty(contentElement2, entityField.getName(), 0.5);
			_put(contentElement2, entityField.getName(), 0.5);
		};
	}

	private void _put(
		ContentElement contentElement, String fieldName, Object fieldValue) {

		_fieldValueMaps.computeIfAbsent(
			contentElement, key -> new HashMap<>()
		).put(
			fieldName, fieldValue
		);
	}

	private ContentElement _toContentElement(JournalArticle journalArticle) {
		return new ContentElement() {
			{
				id = journalArticle.getId();
				title = journalArticle.getTitle();
			}
		};
	}

	private final Map<ContentElement, Map<String, Object>> _fieldValueMaps =
		new IdentityHashMap<>();

}