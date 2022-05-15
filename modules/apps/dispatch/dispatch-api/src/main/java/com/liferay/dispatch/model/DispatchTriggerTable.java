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

package com.liferay.dispatch.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Clob;
import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;DispatchTrigger&quot; database table.
 *
 * @author Matija Petanjek
 * @see DispatchTrigger
 * @generated
 */
public class DispatchTriggerTable extends BaseTable<DispatchTriggerTable> {

	public static final DispatchTriggerTable INSTANCE =
		new DispatchTriggerTable();

	public final Column<DispatchTriggerTable, Long> mvccVersion = createColumn(
		"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<DispatchTriggerTable, String> externalReferenceCode =
		createColumn(
			"externalReferenceCode", String.class, Types.VARCHAR,
			Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, Long> dispatchTriggerId =
		createColumn(
			"dispatchTriggerId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<DispatchTriggerTable, Long> companyId = createColumn(
		"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, Long> userId = createColumn(
		"userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, String> userName = createColumn(
		"userName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, Date> createDate = createColumn(
		"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, Date> modifiedDate = createColumn(
		"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, Boolean> active = createColumn(
		"active_", Boolean.class, Types.BOOLEAN, Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, String> cronExpression =
		createColumn(
			"cronExpression", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, Integer> dispatchTaskClusterMode =
		createColumn(
			"dispatchTaskClusterMode", Integer.class, Types.INTEGER,
			Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, String> dispatchTaskExecutorType =
		createColumn(
			"dispatchTaskExecutorType", String.class, Types.VARCHAR,
			Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, Clob> dispatchTaskSettings =
		createColumn(
			"dispatchTaskSettings", Clob.class, Types.CLOB,
			Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, Date> endDate = createColumn(
		"endDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, String> name = createColumn(
		"name", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, Boolean> overlapAllowed =
		createColumn(
			"overlapAllowed", Boolean.class, Types.BOOLEAN,
			Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, Date> startDate = createColumn(
		"startDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<DispatchTriggerTable, Boolean> system = createColumn(
		"system_", Boolean.class, Types.BOOLEAN, Column.FLAG_DEFAULT);

	private DispatchTriggerTable() {
		super("DispatchTrigger", DispatchTriggerTable::new);
	}

}