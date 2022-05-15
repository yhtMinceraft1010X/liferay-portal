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

package com.liferay.batch.engine.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;BatchEngineExportTask&quot; database table.
 *
 * @author Shuyang Zhou
 * @see BatchEngineExportTask
 * @generated
 */
public class BatchEngineExportTaskTable
	extends BaseTable<BatchEngineExportTaskTable> {

	public static final BatchEngineExportTaskTable INSTANCE =
		new BatchEngineExportTaskTable();

	public final Column<BatchEngineExportTaskTable, Long> mvccVersion =
		createColumn(
			"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<BatchEngineExportTaskTable, String> uuid = createColumn(
		"uuid_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, String>
		externalReferenceCode = createColumn(
			"externalReferenceCode", String.class, Types.VARCHAR,
			Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, Long>
		batchEngineExportTaskId = createColumn(
			"batchEngineExportTaskId", Long.class, Types.BIGINT,
			Column.FLAG_PRIMARY);
	public final Column<BatchEngineExportTaskTable, Long> companyId =
		createColumn(
			"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, Long> userId = createColumn(
		"userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, Date> createDate =
		createColumn(
			"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, Date> modifiedDate =
		createColumn(
			"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, String> callbackURL =
		createColumn(
			"callbackURL", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, String> className =
		createColumn(
			"className", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, Blob> content =
		createColumn("content", Blob.class, Types.BLOB, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, String> contentType =
		createColumn(
			"contentType", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, Date> endTime =
		createColumn(
			"endTime", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, Clob> errorMessage =
		createColumn(
			"errorMessage", Clob.class, Types.CLOB, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, String> fieldNames =
		createColumn(
			"fieldNames", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, String> executeStatus =
		createColumn(
			"executeStatus", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, Clob> parameters =
		createColumn("parameters", Clob.class, Types.CLOB, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, Integer>
		processedItemsCount = createColumn(
			"processedItemsCount", Integer.class, Types.INTEGER,
			Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, Date> startTime =
		createColumn(
			"startTime", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, String>
		taskItemDelegateName = createColumn(
			"taskItemDelegateName", String.class, Types.VARCHAR,
			Column.FLAG_DEFAULT);
	public final Column<BatchEngineExportTaskTable, Integer> totalItemsCount =
		createColumn(
			"totalItemsCount", Integer.class, Types.INTEGER,
			Column.FLAG_DEFAULT);

	private BatchEngineExportTaskTable() {
		super("BatchEngineExportTask", BatchEngineExportTaskTable::new);
	}

}