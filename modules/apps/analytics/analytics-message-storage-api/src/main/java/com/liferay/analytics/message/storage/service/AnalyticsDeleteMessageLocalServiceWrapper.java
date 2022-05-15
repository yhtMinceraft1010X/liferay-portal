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

package com.liferay.analytics.message.storage.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link AnalyticsDeleteMessageLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see AnalyticsDeleteMessageLocalService
 * @generated
 */
public class AnalyticsDeleteMessageLocalServiceWrapper
	implements AnalyticsDeleteMessageLocalService,
			   ServiceWrapper<AnalyticsDeleteMessageLocalService> {

	public AnalyticsDeleteMessageLocalServiceWrapper() {
		this(null);
	}

	public AnalyticsDeleteMessageLocalServiceWrapper(
		AnalyticsDeleteMessageLocalService analyticsDeleteMessageLocalService) {

		_analyticsDeleteMessageLocalService =
			analyticsDeleteMessageLocalService;
	}

	/**
	 * Adds the analytics delete message to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AnalyticsDeleteMessageLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param analyticsDeleteMessage the analytics delete message
	 * @return the analytics delete message that was added
	 */
	@Override
	public com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage
		addAnalyticsDeleteMessage(
			com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage
				analyticsDeleteMessage) {

		return _analyticsDeleteMessageLocalService.addAnalyticsDeleteMessage(
			analyticsDeleteMessage);
	}

	@Override
	public com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage
		addAnalyticsDeleteMessage(
			long companyId, java.util.Date createDate, String className,
			long classPK, long userId) {

		return _analyticsDeleteMessageLocalService.addAnalyticsDeleteMessage(
			companyId, createDate, className, classPK, userId);
	}

	/**
	 * Creates a new analytics delete message with the primary key. Does not add the analytics delete message to the database.
	 *
	 * @param analyticsDeleteMessageId the primary key for the new analytics delete message
	 * @return the new analytics delete message
	 */
	@Override
	public com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage
		createAnalyticsDeleteMessage(long analyticsDeleteMessageId) {

		return _analyticsDeleteMessageLocalService.createAnalyticsDeleteMessage(
			analyticsDeleteMessageId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _analyticsDeleteMessageLocalService.createPersistedModel(
			primaryKeyObj);
	}

	/**
	 * Deletes the analytics delete message from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AnalyticsDeleteMessageLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param analyticsDeleteMessage the analytics delete message
	 * @return the analytics delete message that was removed
	 */
	@Override
	public com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage
		deleteAnalyticsDeleteMessage(
			com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage
				analyticsDeleteMessage) {

		return _analyticsDeleteMessageLocalService.deleteAnalyticsDeleteMessage(
			analyticsDeleteMessage);
	}

	/**
	 * Deletes the analytics delete message with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AnalyticsDeleteMessageLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param analyticsDeleteMessageId the primary key of the analytics delete message
	 * @return the analytics delete message that was removed
	 * @throws PortalException if a analytics delete message with the primary key could not be found
	 */
	@Override
	public com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage
			deleteAnalyticsDeleteMessage(long analyticsDeleteMessageId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _analyticsDeleteMessageLocalService.deleteAnalyticsDeleteMessage(
			analyticsDeleteMessageId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _analyticsDeleteMessageLocalService.deletePersistedModel(
			persistedModel);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _analyticsDeleteMessageLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _analyticsDeleteMessageLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _analyticsDeleteMessageLocalService.dynamicQuery();
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

		return _analyticsDeleteMessageLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.analytics.message.storage.model.impl.AnalyticsDeleteMessageModelImpl</code>.
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

		return _analyticsDeleteMessageLocalService.dynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.analytics.message.storage.model.impl.AnalyticsDeleteMessageModelImpl</code>.
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

		return _analyticsDeleteMessageLocalService.dynamicQuery(
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

		return _analyticsDeleteMessageLocalService.dynamicQueryCount(
			dynamicQuery);
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

		return _analyticsDeleteMessageLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage
		fetchAnalyticsDeleteMessage(long analyticsDeleteMessageId) {

		return _analyticsDeleteMessageLocalService.fetchAnalyticsDeleteMessage(
			analyticsDeleteMessageId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _analyticsDeleteMessageLocalService.getActionableDynamicQuery();
	}

	/**
	 * Returns the analytics delete message with the primary key.
	 *
	 * @param analyticsDeleteMessageId the primary key of the analytics delete message
	 * @return the analytics delete message
	 * @throws PortalException if a analytics delete message with the primary key could not be found
	 */
	@Override
	public com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage
			getAnalyticsDeleteMessage(long analyticsDeleteMessageId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _analyticsDeleteMessageLocalService.getAnalyticsDeleteMessage(
			analyticsDeleteMessageId);
	}

	/**
	 * Returns a range of all the analytics delete messages.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.analytics.message.storage.model.impl.AnalyticsDeleteMessageModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of analytics delete messages
	 * @param end the upper bound of the range of analytics delete messages (not inclusive)
	 * @return the range of analytics delete messages
	 */
	@Override
	public java.util.List
		<com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage>
			getAnalyticsDeleteMessages(int start, int end) {

		return _analyticsDeleteMessageLocalService.getAnalyticsDeleteMessages(
			start, end);
	}

	@Override
	public java.util.List
		<com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage>
			getAnalyticsDeleteMessages(
				long companyId, java.util.Date modifiedDate, int start,
				int end) {

		return _analyticsDeleteMessageLocalService.getAnalyticsDeleteMessages(
			companyId, modifiedDate, start, end);
	}

	@Override
	public java.util.List
		<com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage>
			getAnalyticsDeleteMessages(long companyId, int start, int end) {

		return _analyticsDeleteMessageLocalService.getAnalyticsDeleteMessages(
			companyId, start, end);
	}

	/**
	 * Returns the number of analytics delete messages.
	 *
	 * @return the number of analytics delete messages
	 */
	@Override
	public int getAnalyticsDeleteMessagesCount() {
		return _analyticsDeleteMessageLocalService.
			getAnalyticsDeleteMessagesCount();
	}

	@Override
	public int getAnalyticsDeleteMessagesCount(long companyId) {
		return _analyticsDeleteMessageLocalService.
			getAnalyticsDeleteMessagesCount(companyId);
	}

	@Override
	public int getAnalyticsDeleteMessagesCount(
		long companyId, java.util.Date modifiedDate) {

		return _analyticsDeleteMessageLocalService.
			getAnalyticsDeleteMessagesCount(companyId, modifiedDate);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _analyticsDeleteMessageLocalService.
			getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _analyticsDeleteMessageLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _analyticsDeleteMessageLocalService.getPersistedModel(
			primaryKeyObj);
	}

	/**
	 * Updates the analytics delete message in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AnalyticsDeleteMessageLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param analyticsDeleteMessage the analytics delete message
	 * @return the analytics delete message that was updated
	 */
	@Override
	public com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage
		updateAnalyticsDeleteMessage(
			com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage
				analyticsDeleteMessage) {

		return _analyticsDeleteMessageLocalService.updateAnalyticsDeleteMessage(
			analyticsDeleteMessage);
	}

	@Override
	public AnalyticsDeleteMessageLocalService getWrappedService() {
		return _analyticsDeleteMessageLocalService;
	}

	@Override
	public void setWrappedService(
		AnalyticsDeleteMessageLocalService analyticsDeleteMessageLocalService) {

		_analyticsDeleteMessageLocalService =
			analyticsDeleteMessageLocalService;
	}

	private AnalyticsDeleteMessageLocalService
		_analyticsDeleteMessageLocalService;

}