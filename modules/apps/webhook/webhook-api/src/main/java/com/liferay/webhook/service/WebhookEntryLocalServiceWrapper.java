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

package com.liferay.webhook.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link WebhookEntryLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see WebhookEntryLocalService
 * @generated
 */
public class WebhookEntryLocalServiceWrapper
	implements ServiceWrapper<WebhookEntryLocalService>,
			   WebhookEntryLocalService {

	public WebhookEntryLocalServiceWrapper(
		WebhookEntryLocalService webhookEntryLocalService) {

		_webhookEntryLocalService = webhookEntryLocalService;
	}

	@Override
	public com.liferay.webhook.model.WebhookEntry addWebhookEntry(
			long userId, boolean active, String messageBusDestinationName,
			String name, String url,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _webhookEntryLocalService.addWebhookEntry(
			userId, active, messageBusDestinationName, name, url,
			serviceContext);
	}

	/**
	 * Adds the webhook entry to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect WebhookEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param webhookEntry the webhook entry
	 * @return the webhook entry that was added
	 */
	@Override
	public com.liferay.webhook.model.WebhookEntry addWebhookEntry(
		com.liferay.webhook.model.WebhookEntry webhookEntry) {

		return _webhookEntryLocalService.addWebhookEntry(webhookEntry);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _webhookEntryLocalService.createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new webhook entry with the primary key. Does not add the webhook entry to the database.
	 *
	 * @param webhookEntryId the primary key for the new webhook entry
	 * @return the new webhook entry
	 */
	@Override
	public com.liferay.webhook.model.WebhookEntry createWebhookEntry(
		long webhookEntryId) {

		return _webhookEntryLocalService.createWebhookEntry(webhookEntryId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _webhookEntryLocalService.deletePersistedModel(persistedModel);
	}

	/**
	 * Deletes the webhook entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect WebhookEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param webhookEntryId the primary key of the webhook entry
	 * @return the webhook entry that was removed
	 * @throws PortalException if a webhook entry with the primary key could not be found
	 */
	@Override
	public com.liferay.webhook.model.WebhookEntry deleteWebhookEntry(
			long webhookEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _webhookEntryLocalService.deleteWebhookEntry(webhookEntryId);
	}

	/**
	 * Deletes the webhook entry from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect WebhookEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param webhookEntry the webhook entry
	 * @return the webhook entry that was removed
	 * @throws PortalException
	 */
	@Override
	public com.liferay.webhook.model.WebhookEntry deleteWebhookEntry(
			com.liferay.webhook.model.WebhookEntry webhookEntry)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _webhookEntryLocalService.deleteWebhookEntry(webhookEntry);
	}

	@Override
	public void deployWebhookEntry(
		com.liferay.webhook.model.WebhookEntry webhookEntry) {

		_webhookEntryLocalService.deployWebhookEntry(webhookEntry);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _webhookEntryLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _webhookEntryLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _webhookEntryLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _webhookEntryLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.webhook.model.impl.WebhookEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _webhookEntryLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.webhook.model.impl.WebhookEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _webhookEntryLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _webhookEntryLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _webhookEntryLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.webhook.model.WebhookEntry fetchWebhookEntry(
		long webhookEntryId) {

		return _webhookEntryLocalService.fetchWebhookEntry(webhookEntryId);
	}

	/**
	 * Returns the webhook entry with the matching UUID and company.
	 *
	 * @param uuid the webhook entry's UUID
	 * @param companyId the primary key of the company
	 * @return the matching webhook entry, or <code>null</code> if a matching webhook entry could not be found
	 */
	@Override
	public com.liferay.webhook.model.WebhookEntry
		fetchWebhookEntryByUuidAndCompanyId(String uuid, long companyId) {

		return _webhookEntryLocalService.fetchWebhookEntryByUuidAndCompanyId(
			uuid, companyId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _webhookEntryLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return _webhookEntryLocalService.getExportActionableDynamicQuery(
			portletDataContext);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _webhookEntryLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _webhookEntryLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _webhookEntryLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns a range of all the webhook entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.webhook.model.impl.WebhookEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of webhook entries
	 * @param end the upper bound of the range of webhook entries (not inclusive)
	 * @return the range of webhook entries
	 */
	@Override
	public java.util.List<com.liferay.webhook.model.WebhookEntry>
		getWebhookEntries(int start, int end) {

		return _webhookEntryLocalService.getWebhookEntries(start, end);
	}

	/**
	 * Returns the number of webhook entries.
	 *
	 * @return the number of webhook entries
	 */
	@Override
	public int getWebhookEntriesCount() {
		return _webhookEntryLocalService.getWebhookEntriesCount();
	}

	/**
	 * Returns the webhook entry with the primary key.
	 *
	 * @param webhookEntryId the primary key of the webhook entry
	 * @return the webhook entry
	 * @throws PortalException if a webhook entry with the primary key could not be found
	 */
	@Override
	public com.liferay.webhook.model.WebhookEntry getWebhookEntry(
			long webhookEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _webhookEntryLocalService.getWebhookEntry(webhookEntryId);
	}

	/**
	 * Returns the webhook entry with the matching UUID and company.
	 *
	 * @param uuid the webhook entry's UUID
	 * @param companyId the primary key of the company
	 * @return the matching webhook entry
	 * @throws PortalException if a matching webhook entry could not be found
	 */
	@Override
	public com.liferay.webhook.model.WebhookEntry
			getWebhookEntryByUuidAndCompanyId(String uuid, long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _webhookEntryLocalService.getWebhookEntryByUuidAndCompanyId(
			uuid, companyId);
	}

	@Override
	public java.util.List<com.liferay.webhook.model.WebhookEntry> search(
			long companyId, String keywords, int start, int end,
			com.liferay.portal.kernel.search.Sort sort)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _webhookEntryLocalService.search(
			companyId, keywords, start, end, sort);
	}

	@Override
	public int searchCount(long companyId, String keywords)
		throws com.liferay.portal.kernel.search.SearchException {

		return _webhookEntryLocalService.searchCount(companyId, keywords);
	}

	@Override
	public void undeployWebhookEntry(
		com.liferay.webhook.model.WebhookEntry webhookEntry) {

		_webhookEntryLocalService.undeployWebhookEntry(webhookEntry);
	}

	@Override
	public com.liferay.webhook.model.WebhookEntry updateWebhookEntry(
			long webhookEntryId, boolean active,
			String messageBusDestinationName, String name, String url)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _webhookEntryLocalService.updateWebhookEntry(
			webhookEntryId, active, messageBusDestinationName, name, url);
	}

	/**
	 * Updates the webhook entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect WebhookEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param webhookEntry the webhook entry
	 * @return the webhook entry that was updated
	 */
	@Override
	public com.liferay.webhook.model.WebhookEntry updateWebhookEntry(
		com.liferay.webhook.model.WebhookEntry webhookEntry) {

		return _webhookEntryLocalService.updateWebhookEntry(webhookEntry);
	}

	@Override
	public WebhookEntryLocalService getWrappedService() {
		return _webhookEntryLocalService;
	}

	@Override
	public void setWrappedService(
		WebhookEntryLocalService webhookEntryLocalService) {

		_webhookEntryLocalService = webhookEntryLocalService;
	}

	private WebhookEntryLocalService _webhookEntryLocalService;

}