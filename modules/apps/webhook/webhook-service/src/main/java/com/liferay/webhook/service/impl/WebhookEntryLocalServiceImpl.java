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

package com.liferay.webhook.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.cluster.Clusterable;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.webhook.internal.messaging.WebhookEntryMessageListener;
import com.liferay.webhook.model.WebhookEntry;
import com.liferay.webhook.service.base.WebhookEntryLocalServiceBaseImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.liferay.webhook.model.WebhookEntry",
	service = AopService.class
)
public class WebhookEntryLocalServiceImpl
	extends WebhookEntryLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public WebhookEntry addWebhookEntry(
			long userId, boolean active, String messageBusDestinationName,
			String name, String url, ServiceContext serviceContext)
		throws PortalException {

		long webhookEntryId = counterLocalService.increment();

		WebhookEntry webhookEntry = webhookEntryPersistence.create(
			webhookEntryId);

		webhookEntry.setUuid(serviceContext.getUuid());

		User user = _userLocalService.getUser(userId);

		webhookEntry.setCompanyId(user.getCompanyId());
		webhookEntry.setUserId(user.getUserId());
		webhookEntry.setUserName(user.getFullName());

		webhookEntry.setActive(active);
		webhookEntry.setMessageBusDestinationName(messageBusDestinationName);
		webhookEntry.setName(name);
		webhookEntry.setURL(url);

		webhookEntry = webhookEntryPersistence.update(webhookEntry);

		if (active) {
			webhookEntryLocalService.deployWebhookEntry(webhookEntry);
		}

		return webhookEntry;
	}

	@Override
	public WebhookEntry deleteWebhookEntry(long webhookEntryId)
		throws PortalException {

		WebhookEntry webhookEntry = webhookEntryPersistence.findByPrimaryKey(
			webhookEntryId);

		return deleteWebhookEntry(webhookEntry);
	}

	@Override
	public WebhookEntry deleteWebhookEntry(WebhookEntry webhookEntry)
		throws PortalException {

		webhookEntryPersistence.remove(webhookEntry);

		webhookEntryLocalService.undeployWebhookEntry(webhookEntry);

		return webhookEntry;
	}

	@Clusterable
	@Override
	public void deployWebhookEntry(WebhookEntry webhookEntry) {
		undeployWebhookEntry(webhookEntry);

		_serviceRegistrations.put(
			webhookEntry.getWebhookEntryId(),
			_bundleContext.registerService(
				MessageListener.class,
				new WebhookEntryMessageListener(webhookEntry),
				HashMapDictionaryBuilder.<String, Object>put(
					"destination.name",
					webhookEntry.getMessageBusDestinationName()
				).build()));
	}

	@Override
	public List<WebhookEntry> search(
			long companyId, String keywords, int start, int end, Sort sort)
		throws PortalException {

		SearchContext searchContext = _buildSearchContext(
			companyId, keywords, start, end, sort);

		Indexer<WebhookEntry> indexer = IndexerRegistryUtil.nullSafeGetIndexer(
			WebhookEntry.class);

		for (int i = 0; i < 10; i++) {
			Hits hits = indexer.search(searchContext);

			List<WebhookEntry> webhookEntries = _getWebhookEntries(hits);

			if (webhookEntries != null) {
				return webhookEntries;
			}
		}

		throw new SearchException(
			"Unable to fix the search index after 10 attempts");
	}

	@Override
	public int searchCount(long companyId, String keywords)
		throws SearchException {

		Indexer<WebhookEntry> indexer = IndexerRegistryUtil.nullSafeGetIndexer(
			WebhookEntry.class);

		SearchContext searchContext = _buildSearchContext(
			companyId, keywords, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		return GetterUtil.getInteger(indexer.searchCount(searchContext));
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		super.setAopProxy(aopProxy);

		List<WebhookEntry> webhookEntries =
			webhookEntryLocalService.getWebhookEntries(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (WebhookEntry webhookEntry : webhookEntries) {
			deployWebhookEntry(webhookEntry);
		}
	}

	@Clusterable
	@Override
	public void undeployWebhookEntry(WebhookEntry webhookEntry) {
		ServiceRegistration<MessageListener> serviceRegistration =
			_serviceRegistrations.remove(webhookEntry.getWebhookEntryId());

		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public WebhookEntry updateWebhookEntry(
			long webhookEntryId, boolean active,
			String messageBusDestinationName, String name, String url)
		throws PortalException {

		WebhookEntry webhookEntry = webhookEntryPersistence.findByPrimaryKey(
			webhookEntryId);

		webhookEntry.setActive(active);
		webhookEntry.setMessageBusDestinationName(messageBusDestinationName);
		webhookEntry.setName(name);
		webhookEntry.setURL(url);

		webhookEntry = webhookEntryPersistence.update(webhookEntry);

		if (active) {
			webhookEntryLocalService.deployWebhookEntry(webhookEntry);
		}
		else {
			webhookEntryLocalService.undeployWebhookEntry(webhookEntry);
		}

		return webhookEntry;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	private SearchContext _buildSearchContext(
		long companyId, String keywords, int start, int end, Sort sort) {

		SearchContext searchContext = new SearchContext();

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		searchContext.setAttributes(
			HashMapBuilder.<String, Serializable>put(
				Field.NAME, keywords
			).put(
				Field.URL, keywords
			).build());
		searchContext.setCompanyId(companyId);
		searchContext.setEnd(end);
		searchContext.setKeywords(keywords);

		if (sort != null) {
			searchContext.setSorts(sort);
		}

		searchContext.setStart(start);

		return searchContext;
	}

	private List<WebhookEntry> _getWebhookEntries(Hits hits)
		throws PortalException {

		List<Document> documents = hits.toList();

		List<WebhookEntry> webhookEntries = new ArrayList<>(documents.size());

		for (Document document : documents) {
			long webhookEntryId = GetterUtil.getLong(
				document.get(Field.ENTRY_CLASS_PK));

			WebhookEntry webhookEntry =
				webhookEntryPersistence.fetchByPrimaryKey(webhookEntryId);

			if (webhookEntry == null) {
				webhookEntries = null;

				Indexer<WebhookEntry> indexer = IndexerRegistryUtil.getIndexer(
					WebhookEntry.class);

				long companyId = GetterUtil.getLong(
					document.get(Field.COMPANY_ID));

				indexer.delete(companyId, document.getUID());
			}
			else {
				webhookEntries.add(webhookEntry);
			}
		}

		return webhookEntries;
	}

	private BundleContext _bundleContext;
	private final Map<Long, ServiceRegistration<MessageListener>>
		_serviceRegistrations = new ConcurrentHashMap<>();

	@Reference
	private UserLocalService _userLocalService;

}