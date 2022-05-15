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

package com.liferay.portal.db.partition.messaging.test;

import com.liferay.portal.db.partition.test.BaseDBPartitionTestCase;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationConfiguration;
import com.liferay.portal.kernel.messaging.DestinationFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageBusInterceptor;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.scheduler.SchedulerEngine;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.test.rule.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alberto Chaparro
 */
public abstract class BaseDBPartitionMessageBusInterceptorTestCase
	extends BaseDBPartitionTestCase {

	@AfterClass
	public static void tearDownClass() throws Exception {
		Destination destination = _destinations.remove(_DESTINATION_NAME);

		destination.destroy();

		_companyLocalService.deleteCompany(_company);

		ReflectionTestUtil.setFieldValue(
			_dbPartitionMessageBusInterceptor, "_databasePartitionEnabled",
			_currentDatabasePartitionEnabled);
	}

	@Before
	public void setUp() {
		_currentExcludedMessageBusDestinationNames =
			ReflectionTestUtil.getFieldValue(
				_dbPartitionMessageBusInterceptor,
				"_excludedMessageBusDestinationNames");

		_currentExcludedSchedulerJobNames = ReflectionTestUtil.getFieldValue(
			_dbPartitionMessageBusInterceptor, "_excludedSchedulerJobNames");

		_currentCompanyId = CompanyThreadLocal.getCompanyId();

		CompanyThreadLocal.setCompanyId(CompanyConstants.SYSTEM);
	}

	@After
	public void tearDown() {
		ReflectionTestUtil.setFieldValue(
			_dbPartitionMessageBusInterceptor,
			"_excludedMessageBusDestinationNames",
			_currentExcludedMessageBusDestinationNames);

		ReflectionTestUtil.setFieldValue(
			_dbPartitionMessageBusInterceptor, "_excludedSchedulerJobNames",
			_currentExcludedSchedulerJobNames);

		CompanyThreadLocal.setCompanyId(_currentCompanyId);
	}

	@Test
	public void testSendMessage() throws InterruptedException {

		// Test 1

		_countDownLatch = new CountDownLatch(_activeCompanyIds.length);

		_messageBus.sendMessage(_DESTINATION_NAME, new Message());

		_testDBPartitionMessageListener.assertCollected(_activeCompanyIds);

		// Test 2

		CompanyThreadLocal.setCompanyId(_company.getCompanyId());

		_countDownLatch = new CountDownLatch(1);

		_messageBus.sendMessage(_DESTINATION_NAME, new Message());

		_testDBPartitionMessageListener.assertCollected(
			_company.getCompanyId());
	}

	@Test
	public void testSendMessageExcludingDestination()
		throws InterruptedException {

		_countDownLatch = new CountDownLatch(_activeCompanyIds.length);

		_messageBus.sendMessage(_DESTINATION_NAME, new Message());

		_testDBPartitionMessageListener.assertCollected(_activeCompanyIds);

		ReflectionTestUtil.setFieldValue(
			_dbPartitionMessageBusInterceptor,
			"_excludedMessageBusDestinationNames",
			Collections.singleton(_DESTINATION_NAME));

		_countDownLatch = new CountDownLatch(1);

		_messageBus.sendMessage(_DESTINATION_NAME, new Message());

		_testDBPartitionMessageListener.assertCollected(
			CompanyConstants.SYSTEM);
	}

	@Test
	public void testSendMessageExcludingScheduledJob()
		throws InterruptedException {

		Message message = new Message();

		message.put(
			SchedulerEngine.JOB_NAME,
			TestDBPartitionMessageListener.class.getName());

		_countDownLatch = new CountDownLatch(_activeCompanyIds.length);

		_messageBus.sendMessage(_DESTINATION_NAME, message.clone());

		_testDBPartitionMessageListener.assertCollected(_activeCompanyIds);

		ReflectionTestUtil.setFieldValue(
			_dbPartitionMessageBusInterceptor, "_excludedSchedulerJobNames",
			Collections.singleton(
				TestDBPartitionMessageListener.class.getName()));

		_countDownLatch = new CountDownLatch(1);

		_messageBus.sendMessage(_DESTINATION_NAME, message.clone());

		_testDBPartitionMessageListener.assertCollected(
			CompanyConstants.SYSTEM);
	}

	@Test
	public void testSendMessageWithCompanyId() throws InterruptedException {

		// Test 1

		Message message = new Message();

		message.put("companyId", CompanyConstants.SYSTEM);

		_countDownLatch = new CountDownLatch(_activeCompanyIds.length);

		_messageBus.sendMessage(_DESTINATION_NAME, message);

		_testDBPartitionMessageListener.assertCollected(_activeCompanyIds);

		// Test 2

		CompanyThreadLocal.setCompanyId(_company.getCompanyId());

		message = new Message();

		message.put("companyId", CompanyConstants.SYSTEM);

		_countDownLatch = new CountDownLatch(_activeCompanyIds.length);

		_messageBus.sendMessage(_DESTINATION_NAME, message);

		_testDBPartitionMessageListener.assertCollected(_activeCompanyIds);

		// Test 3

		CompanyThreadLocal.setCompanyId(_company.getCompanyId());

		message = new Message();

		message.put("companyId", _company.getCompanyId());

		_countDownLatch = new CountDownLatch(1);

		_messageBus.sendMessage(_DESTINATION_NAME, message);

		_testDBPartitionMessageListener.assertCollected(
			_company.getCompanyId());
	}

	protected static void setUpClass(String destinationType) throws Exception {
		_company = CompanyTestUtil.addCompany();

		Set<Long> companyIds = new TreeSet<>();

		_companyLocalService.forEachCompany(
			company -> {
				if (company.isActive()) {
					companyIds.add(company.getCompanyId());
				}
			});

		_activeCompanyIds = companyIds.toArray(new Long[0]);

		_currentDatabasePartitionEnabled =
			ReflectionTestUtil.getAndSetFieldValue(
				_dbPartitionMessageBusInterceptor, "_databasePartitionEnabled",
				true);

		_testDBPartitionMessageListener = new TestDBPartitionMessageListener();

		Destination destination = DestinationFactoryUtil.createDestination(
			new DestinationConfiguration(destinationType, _DESTINATION_NAME));

		destination.register(_testDBPartitionMessageListener);

		destination.open();

		_destinations = ReflectionTestUtil.getFieldValue(
			_messageBus, "_destinations");

		_destinations.put(_DESTINATION_NAME, destination);
	}

	private static final String _DESTINATION_NAME = "liferay/test_dbpartition";

	private static Long[] _activeCompanyIds;
	private static Company _company;

	@Inject
	private static CompanyLocalService _companyLocalService;

	private static volatile CountDownLatch _countDownLatch;
	private static boolean _currentDatabasePartitionEnabled;

	@Inject(
		filter = "component.name=com.liferay.portal.db.partition.internal.messaging.DBPartitionMessageBusInterceptor"
	)
	private static MessageBusInterceptor _dbPartitionMessageBusInterceptor;

	private static Map<String, Destination> _destinations;

	@Inject
	private static MessageBus _messageBus;

	private static TestDBPartitionMessageListener
		_testDBPartitionMessageListener;

	private long _currentCompanyId;
	private Set<String> _currentExcludedMessageBusDestinationNames;
	private Set<String> _currentExcludedSchedulerJobNames;

	private static class TestDBPartitionMessageListener
		extends BaseMessageListener {

		public void assertCollected(Long... companyIds)
			throws InterruptedException {

			_countDownLatch.await(1, TimeUnit.MINUTES);

			List<Long> companyIdsList = new ArrayList<>(_companyIds);

			Collections.sort(companyIdsList);

			Assert.assertArrayEquals(
				companyIds, companyIdsList.toArray(new Long[0]));

			_companyIds.clear();
		}

		@Override
		protected void doReceive(Message message) {
			_companyIds.add(CompanyThreadLocal.getCompanyId());
			_countDownLatch.countDown();
		}

		private final Collection<Long> _companyIds =
			new CopyOnWriteArraySet<>();

	}

}