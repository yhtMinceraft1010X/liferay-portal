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

import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.webhook.model.WebhookEntry;

import java.io.Serializable;

import java.util.List;

/**
 * Provides the local service utility for WebhookEntry. This utility wraps
 * <code>com.liferay.webhook.service.impl.WebhookEntryLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see WebhookEntryLocalService
 * @generated
 */
public class WebhookEntryLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.webhook.service.impl.WebhookEntryLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static WebhookEntry addWebhookEntry(
			long userId, boolean active, String destinationName,
			String destinationWebhookEventKeys, String name, String secret,
			String url,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addWebhookEntry(
			userId, active, destinationName, destinationWebhookEventKeys, name,
			secret, url, serviceContext);
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
	public static WebhookEntry addWebhookEntry(WebhookEntry webhookEntry) {
		return getService().addWebhookEntry(webhookEntry);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel createPersistedModel(
			Serializable primaryKeyObj)
		throws PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new webhook entry with the primary key. Does not add the webhook entry to the database.
	 *
	 * @param webhookEntryId the primary key for the new webhook entry
	 * @return the new webhook entry
	 */
	public static WebhookEntry createWebhookEntry(long webhookEntryId) {
		return getService().createWebhookEntry(webhookEntryId);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel deletePersistedModel(
			PersistedModel persistedModel)
		throws PortalException {

		return getService().deletePersistedModel(persistedModel);
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
	public static WebhookEntry deleteWebhookEntry(long webhookEntryId)
		throws PortalException {

		return getService().deleteWebhookEntry(webhookEntryId);
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
	public static WebhookEntry deleteWebhookEntry(WebhookEntry webhookEntry)
		throws PortalException {

		return getService().deleteWebhookEntry(webhookEntry);
	}

	public static void deployWebhookEntry(WebhookEntry webhookEntry) {
		getService().deployWebhookEntry(webhookEntry);
	}

	public static <T> T dslQuery(DSLQuery dslQuery) {
		return getService().dslQuery(dslQuery);
	}

	public static int dslQueryCount(DSLQuery dslQuery) {
		return getService().dslQueryCount(dslQuery);
	}

	public static DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return getService().dynamicQuery(dynamicQuery);
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
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
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
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static WebhookEntry fetchWebhookEntry(long webhookEntryId) {
		return getService().fetchWebhookEntry(webhookEntryId);
	}

	/**
	 * Returns the webhook entry with the matching UUID and company.
	 *
	 * @param uuid the webhook entry's UUID
	 * @param companyId the primary key of the company
	 * @return the matching webhook entry, or <code>null</code> if a matching webhook entry could not be found
	 */
	public static WebhookEntry fetchWebhookEntryByUuidAndCompanyId(
		String uuid, long companyId) {

		return getService().fetchWebhookEntryByUuidAndCompanyId(
			uuid, companyId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return getService().getExportActionableDynamicQuery(portletDataContext);
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return getService().getPersistedModel(primaryKeyObj);
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
	public static List<WebhookEntry> getWebhookEntries(int start, int end) {
		return getService().getWebhookEntries(start, end);
	}

	/**
	 * Returns the number of webhook entries.
	 *
	 * @return the number of webhook entries
	 */
	public static int getWebhookEntriesCount() {
		return getService().getWebhookEntriesCount();
	}

	/**
	 * Returns the webhook entry with the primary key.
	 *
	 * @param webhookEntryId the primary key of the webhook entry
	 * @return the webhook entry
	 * @throws PortalException if a webhook entry with the primary key could not be found
	 */
	public static WebhookEntry getWebhookEntry(long webhookEntryId)
		throws PortalException {

		return getService().getWebhookEntry(webhookEntryId);
	}

	/**
	 * Returns the webhook entry with the matching UUID and company.
	 *
	 * @param uuid the webhook entry's UUID
	 * @param companyId the primary key of the company
	 * @return the matching webhook entry
	 * @throws PortalException if a matching webhook entry could not be found
	 */
	public static WebhookEntry getWebhookEntryByUuidAndCompanyId(
			String uuid, long companyId)
		throws PortalException {

		return getService().getWebhookEntryByUuidAndCompanyId(uuid, companyId);
	}

	public static List<WebhookEntry> search(
			long companyId, String keywords, int start, int end,
			com.liferay.portal.kernel.search.Sort sort)
		throws PortalException {

		return getService().search(companyId, keywords, start, end, sort);
	}

	public static int searchCount(long companyId, String keywords)
		throws com.liferay.portal.kernel.search.SearchException {

		return getService().searchCount(companyId, keywords);
	}

	public static void undeployWebhookEntry(WebhookEntry webhookEntry) {
		getService().undeployWebhookEntry(webhookEntry);
	}

	public static WebhookEntry updateWebhookEntry(
			long webhookEntryId, boolean active, String destinationName,
			String destinationWebhookEventKeys, String name, String secret,
			String url)
		throws PortalException {

		return getService().updateWebhookEntry(
			webhookEntryId, active, destinationName,
			destinationWebhookEventKeys, name, secret, url);
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
	public static WebhookEntry updateWebhookEntry(WebhookEntry webhookEntry) {
		return getService().updateWebhookEntry(webhookEntry);
	}

	public static WebhookEntryLocalService getService() {
		return _service;
	}

	private static volatile WebhookEntryLocalService _service;

}