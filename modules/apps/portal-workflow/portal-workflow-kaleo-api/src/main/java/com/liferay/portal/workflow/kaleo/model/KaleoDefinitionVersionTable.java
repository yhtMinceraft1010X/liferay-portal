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

package com.liferay.portal.workflow.kaleo.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Clob;
import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;KaleoDefinitionVersion&quot; database table.
 *
 * @author Brian Wing Shun Chan
 * @see KaleoDefinitionVersion
 * @generated
 */
public class KaleoDefinitionVersionTable
	extends BaseTable<KaleoDefinitionVersionTable> {

	public static final KaleoDefinitionVersionTable INSTANCE =
		new KaleoDefinitionVersionTable();

	public final Column<KaleoDefinitionVersionTable, Long> mvccVersion =
		createColumn(
			"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<KaleoDefinitionVersionTable, Long>
		kaleoDefinitionVersionId = createColumn(
			"kaleoDefinitionVersionId", Long.class, Types.BIGINT,
			Column.FLAG_PRIMARY);
	public final Column<KaleoDefinitionVersionTable, Long> groupId =
		createColumn("groupId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, Long> companyId =
		createColumn(
			"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, Long> userId =
		createColumn("userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, String> userName =
		createColumn(
			"userName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, Date> createDate =
		createColumn(
			"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, Date> modifiedDate =
		createColumn(
			"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, Long> kaleoDefinitionId =
		createColumn(
			"kaleoDefinitionId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, String> name =
		createColumn("name", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, String> title =
		createColumn("title", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, String> description =
		createColumn(
			"description", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, Clob> content =
		createColumn("content", Clob.class, Types.CLOB, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, String> version =
		createColumn(
			"version", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, Long> startKaleoNodeId =
		createColumn(
			"startKaleoNodeId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, Integer> status =
		createColumn(
			"status", Integer.class, Types.INTEGER, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, Long> statusByUserId =
		createColumn(
			"statusByUserId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, String> statusByUserName =
		createColumn(
			"statusByUserName", String.class, Types.VARCHAR,
			Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionVersionTable, Date> statusDate =
		createColumn(
			"statusDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);

	private KaleoDefinitionVersionTable() {
		super("KaleoDefinitionVersion", KaleoDefinitionVersionTable::new);
	}

}