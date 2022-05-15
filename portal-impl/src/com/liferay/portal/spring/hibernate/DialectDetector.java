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

package com.liferay.portal.spring.hibernate;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.dao.jdbc.util.DBInfo;
import com.liferay.portal.dao.jdbc.util.DBInfoUtil;
import com.liferay.portal.dao.orm.hibernate.DB2Dialect;
import com.liferay.portal.dao.orm.hibernate.HSQLDialect;
import com.liferay.portal.dao.orm.hibernate.MariaDBDialect;
import com.liferay.portal.dao.orm.hibernate.SQLServer2005Dialect;
import com.liferay.portal.dao.orm.hibernate.SQLServer2008Dialect;
import com.liferay.portal.dao.orm.hibernate.SybaseASE157Dialect;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;

import java.sql.Connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.hibernate.dialect.DB2400Dialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.engine.jdbc.dialect.spi.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;

/**
 * @author Brian Wing Shun Chan
 */
public class DialectDetector {

	public static Dialect getDialect(DataSource dataSource) {
		DBInfo dbInfo = DBInfoUtil.getDBInfo(dataSource);

		int dbMajorVersion = dbInfo.getMajorVersion();
		int dbMinorVersion = dbInfo.getMinorVersion();
		String dbName = dbInfo.getName();

		Dialect dialect = null;
		String dialectKey = null;

		try {
			dialectKey = StringBundler.concat(
				dbName, StringPool.COLON, dbMajorVersion, StringPool.COLON,
				dbMinorVersion);

			dialect = _dialects.get(dialectKey);

			if (dialect != null) {
				return dialect;
			}

			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						"Determine dialect for ", dbName, " ", dbMajorVersion,
						".", dbMinorVersion));
			}

			if (dbName.startsWith("HSQL")) {
				dialect = new HSQLDialect();

				if (_log.isWarnEnabled()) {
					_log.warn(
						StringBundler.concat(
							"Liferay is configured to use Hypersonic as its ",
							"database. Do NOT use Hypersonic in production. ",
							"Hypersonic is an embedded database useful for ",
							"development and demonstration purposes. The ",
							"database settings can be changed in ",
							"portal-ext.properties."));
				}
			}
			else if (dbName.equals("Adaptive Server Enterprise") &&
					 (dbMajorVersion >= 15)) {

				dialect = new SybaseASE157Dialect();
			}
			else if (dbName.equals("ASE")) {
				throw new RuntimeException(
					"jTDS is no longer suppported. Please use the Sybase " +
						"JDBC driver to connect to Sybase.");
			}
			else if (dbName.startsWith("DB2") && (dbMajorVersion >= 9)) {
				dialect = new DB2Dialect();
			}
			else if (dbName.startsWith("MariaDB")) {
				dialect = new MariaDBDialect();
			}
			else if (dbName.startsWith("Microsoft") && (dbMajorVersion == 9)) {
				dialect = new SQLServer2005Dialect();
			}
			else if (dbName.startsWith("Microsoft") && (dbMajorVersion >= 10)) {
				dialect = new SQLServer2008Dialect();
			}
			else if (dbName.startsWith("Oracle") && (dbMajorVersion >= 10)) {
				dialect = new Oracle10gDialect();
			}
			else {
				try (Connection connection = dataSource.getConnection()) {
					DialectResolver dialectResolver =
						new StandardDialectResolver();

					dialect = dialectResolver.resolveDialect(
						new DatabaseMetaDataDialectResolutionInfoAdapter(
							connection.getMetaData()));
				}
			}
		}
		catch (Exception exception) {
			String msg = GetterUtil.getString(exception.getMessage());

			if (msg.contains("explicitly set for database: DB2")) {
				dialect = new DB2400Dialect();

				if (_log.isWarnEnabled()) {
					_log.warn(
						"DB2400Dialect was dynamically chosen as the " +
							"Hibernate dialect for DB2. This can be " +
								"overriden in portal.properties");
				}
			}
			else {
				_log.error(exception);
			}
		}

		if (dialect == null) {
			throw new RuntimeException("No dialect found");
		}
		else if (dialectKey != null) {
			if (_log.isInfoEnabled()) {
				Class<?> clazz = dialect.getClass();

				_log.info(
					StringBundler.concat(
						"Using dialect ", clazz.getName(), " for ", dbName, " ",
						dbMajorVersion, ".", dbMinorVersion));
			}

			_dialects.put(dialectKey, dialect);
		}

		return dialect;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DialectDetector.class);

	private static final Map<String, Dialect> _dialects =
		new ConcurrentHashMap<>();

}