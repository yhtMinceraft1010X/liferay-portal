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

package com.liferay.portal.verify;

import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.db.BaseDBProcess;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ReleaseConstants;
import com.liferay.portal.kernel.util.ClassUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This abstract class should be extended for startup processes that verify the
 * integrity of the database. They can be added as part of
 * <code>VerifyProcessSuite</code> or be executed independently by being set in
 * the portal.properties file. Each of these processes should not cause any
 * problems if run multiple times.
 *
 * @author Alexander Chow
 * @author Hugo Huijser
 */
public abstract class VerifyProcess extends BaseDBProcess {

	public static final int ALWAYS = -1;

	public static final int NEVER = 0;

	public static final int ONCE = 1;

	public void verify() throws VerifyException {
		long start = System.currentTimeMillis();

		try (Connection connection = getConnection()) {
			this.connection = connection;

			process(
				companyId -> {
					if (_log.isInfoEnabled()) {
						String info =
							"Verifying " + ClassUtil.getClassName(this);

						if (Validator.isNotNull(companyId)) {
							info += "#" + companyId;
						}

						_log.info(info);
					}

					doVerify();
				});
		}
		catch (Exception exception) {
			throw new VerifyException(exception);
		}
		finally {
			this.connection = null;

			if (_log.isInfoEnabled()) {
				_log.info(
					StringBundler.concat(
						"Completed verification process ",
						ClassUtil.getClassName(this), " in ",
						System.currentTimeMillis() - start, " ms"));
			}
		}
	}

	public void verify(VerifyProcess verifyProcess) throws VerifyException {
		verifyProcess.verify();
	}

	protected void doVerify() throws Exception {
	}

	/**
	 * @deprecated As of Cavanaugh (7.4.x), replaced by {@link
	 *             #processConcurrently(Object[], UnsafeConsumer, String)}
	 */
	@Deprecated
	protected void doVerify(Collection<? extends Callable<Void>> callables)
		throws Exception {

		try {
			ExecutorService executorService = Executors.newFixedThreadPool(
				callables.size());

			List<Future<Void>> futures = executorService.invokeAll(callables);

			executorService.shutdown();

			UnsafeConsumer.accept(futures, Future::get);
		}
		catch (Throwable throwable) {
			Class<?> clazz = getClass();

			throw new Exception(
				"Verification error: " + clazz.getName(), throwable);
		}
	}

	/**
	 * @return the portal build number before {@link
	 *         com.liferay.portal.tools.DBUpgrader} has a chance to update it to
	 *         the value in {@link
	 *         com.liferay.portal.kernel.util.ReleaseInfo#getBuildNumber}
	 */
	protected int getBuildNumber() throws Exception {
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"select buildNumber from Release_ where servletContextName = " +
					"?")) {

			preparedStatement.setString(
				1, ReleaseConstants.DEFAULT_SERVLET_CONTEXT_NAME);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				resultSet.next();

				return resultSet.getInt(1);
			}
		}
	}

	protected Set<String> getPortalTableNames() throws Exception {
		if (_portalTableNames != null) {
			return _portalTableNames;
		}

		Thread currentThread = Thread.currentThread();

		String sql = StringUtil.read(
			currentThread.getContextClassLoader(),
			"com/liferay/portal/tools/sql/dependencies/portal-tables.sql");

		Matcher matcher = _createTablePattern.matcher(sql);

		Set<String> tableNames = new HashSet<>();

		while (matcher.find()) {
			String match = matcher.group(1);

			tableNames.add(StringUtil.toLowerCase(match));
		}

		_portalTableNames = tableNames;

		return tableNames;
	}

	protected boolean isForceConcurrent(
		Collection<? extends Callable<Void>> callables) {

		return false;
	}

	protected boolean isPortalTableName(String tableName) throws Exception {
		Set<String> portalTableNames = getPortalTableNames();

		return portalTableNames.contains(StringUtil.toLowerCase(tableName));
	}

	private static final Log _log = LogFactoryUtil.getLog(VerifyProcess.class);

	private static final Pattern _createTablePattern = Pattern.compile(
		"create table (\\S*) \\(");

	private Set<String> _portalTableNames;

}