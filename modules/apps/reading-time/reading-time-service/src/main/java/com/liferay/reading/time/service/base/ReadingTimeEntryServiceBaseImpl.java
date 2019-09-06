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

package com.liferay.reading.time.service.base;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseServiceImpl;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;
import com.liferay.reading.time.model.ReadingTimeEntry;
import com.liferay.reading.time.service.ReadingTimeEntryService;
import com.liferay.reading.time.service.persistence.ReadingTimeEntryPersistence;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the reading time entry remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.reading.time.service.impl.ReadingTimeEntryServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.reading.time.service.impl.ReadingTimeEntryServiceImpl
 * @generated
 */
public abstract class ReadingTimeEntryServiceBaseImpl
	extends BaseServiceImpl
	implements ReadingTimeEntryService, IdentifiableOSGiService {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>ReadingTimeEntryService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.reading.time.service.ReadingTimeEntryServiceUtil</code>.
	 */

	/**
	 * Returns the reading time entry local service.
	 *
	 * @return the reading time entry local service
	 */
	public com.liferay.reading.time.service.ReadingTimeEntryLocalService
		getReadingTimeEntryLocalService() {

		return readingTimeEntryLocalService;
	}

	/**
	 * Sets the reading time entry local service.
	 *
	 * @param readingTimeEntryLocalService the reading time entry local service
	 */
	public void setReadingTimeEntryLocalService(
		com.liferay.reading.time.service.ReadingTimeEntryLocalService
			readingTimeEntryLocalService) {

		this.readingTimeEntryLocalService = readingTimeEntryLocalService;
	}

	/**
	 * Returns the reading time entry remote service.
	 *
	 * @return the reading time entry remote service
	 */
	public ReadingTimeEntryService getReadingTimeEntryService() {
		return readingTimeEntryService;
	}

	/**
	 * Sets the reading time entry remote service.
	 *
	 * @param readingTimeEntryService the reading time entry remote service
	 */
	public void setReadingTimeEntryService(
		ReadingTimeEntryService readingTimeEntryService) {

		this.readingTimeEntryService = readingTimeEntryService;
	}

	/**
	 * Returns the reading time entry persistence.
	 *
	 * @return the reading time entry persistence
	 */
	public ReadingTimeEntryPersistence getReadingTimeEntryPersistence() {
		return readingTimeEntryPersistence;
	}

	/**
	 * Sets the reading time entry persistence.
	 *
	 * @param readingTimeEntryPersistence the reading time entry persistence
	 */
	public void setReadingTimeEntryPersistence(
		ReadingTimeEntryPersistence readingTimeEntryPersistence) {

		this.readingTimeEntryPersistence = readingTimeEntryPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService
		getCounterLocalService() {

		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService
			counterLocalService) {

		this.counterLocalService = counterLocalService;
	}

	public void afterPropertiesSet() {
	}

	public void destroy() {
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return ReadingTimeEntryService.class.getName();
	}

	protected Class<?> getModelClass() {
		return ReadingTimeEntry.class;
	}

	protected String getModelClassName() {
		return ReadingTimeEntry.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = readingTimeEntryPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(
		type = com.liferay.reading.time.service.ReadingTimeEntryLocalService.class
	)
	protected com.liferay.reading.time.service.ReadingTimeEntryLocalService
		readingTimeEntryLocalService;

	@BeanReference(type = ReadingTimeEntryService.class)
	protected ReadingTimeEntryService readingTimeEntryService;

	@BeanReference(type = ReadingTimeEntryPersistence.class)
	protected ReadingTimeEntryPersistence readingTimeEntryPersistence;

	@ServiceReference(
		type = com.liferay.counter.kernel.service.CounterLocalService.class
	)
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

}