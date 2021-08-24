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

package com.liferay.object.internal.info.collection.provider;

import com.liferay.info.collection.provider.CollectionQuery;
import com.liferay.info.collection.provider.SingleFormVariationInfoCollectionProvider;
import com.liferay.info.pagination.InfoPage;
import com.liferay.info.pagination.Pagination;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.scope.ObjectScopeProvider;
import com.liferay.object.scope.ObjectScopeProviderRegistry;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;

import java.util.Locale;

/**
 * @author Jorge Ferrer
 */
public class ObjectEntrySingleFormVariationInfoCollectionProvider
	implements SingleFormVariationInfoCollectionProvider<ObjectEntry> {

	public ObjectEntrySingleFormVariationInfoCollectionProvider(
		ObjectDefinition objectDefinition,
		ObjectEntryLocalService objectEntryLocalService,
		ObjectScopeProviderRegistry objectScopeProviderRegistry) {

		_objectDefinition = objectDefinition;
		_objectEntryLocalService = objectEntryLocalService;
		_objectScopeProviderRegistry = objectScopeProviderRegistry;
	}

	@Override
	public InfoPage<ObjectEntry> getCollectionInfoPage(
		CollectionQuery collectionQuery) {

		Pagination pagination = collectionQuery.getPagination();

		try {
			return InfoPage.of(
				_objectEntryLocalService.getObjectEntries(
					_getGroupId(), _objectDefinition.getObjectDefinitionId(),
					pagination.getStart(), pagination.getEnd()),
				collectionQuery.getPagination(),
				_objectEntryLocalService.getObjectEntriesCount(
					_getGroupId(), _objectDefinition.getObjectDefinitionId()));
		}
		catch (PortalException portalException) {
			throw new RuntimeException(
				"Unable to get object entries for object definition " +
					_objectDefinition.getObjectDefinitionId(),
				portalException);
		}
	}

	@Override
	public String getFormVariationKey() {
		return String.valueOf(_objectDefinition.getObjectDefinitionId());
	}

	@Override
	public String getKey() {
		return StringBundler.concat(
			SingleFormVariationInfoCollectionProvider.super.getKey(), "_",
			_objectDefinition.getName());
	}

	@Override
	public String getLabel(Locale locale) {
		return _objectDefinition.getName();
	}

	private long _getGroupId() throws PortalException {
		ObjectScopeProvider objectScopeProvider =
			_objectScopeProviderRegistry.getObjectScopeProvider(
				_objectDefinition.getScope());

		if (!objectScopeProvider.isGroupAware()) {
			return 0;
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		return objectScopeProvider.getGroupId(serviceContext.getRequest());
	}

	private final ObjectDefinition _objectDefinition;
	private final ObjectEntryLocalService _objectEntryLocalService;
	private final ObjectScopeProviderRegistry _objectScopeProviderRegistry;

}