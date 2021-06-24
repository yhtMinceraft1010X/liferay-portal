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

package com.liferay.alloy.mvc.sample.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;AlloyMVCSample_TodoItem&quot; database table.
 *
 * @author Brian Wing Shun Chan
 * @see TodoItem
 * @generated
 */
public class TodoItemTable extends BaseTable<TodoItemTable> {

	public static final TodoItemTable INSTANCE = new TodoItemTable();

	public final Column<TodoItemTable, Long> mvccVersion = createColumn(
		"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<TodoItemTable, Long> todoItemId = createColumn(
		"todoItemId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<TodoItemTable, Long> companyId = createColumn(
		"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<TodoItemTable, Long> userId = createColumn(
		"userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<TodoItemTable, String> userName = createColumn(
		"userName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<TodoItemTable, Date> createDate = createColumn(
		"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<TodoItemTable, Date> modifiedDate = createColumn(
		"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<TodoItemTable, Long> todoListId = createColumn(
		"todoListId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<TodoItemTable, String> description = createColumn(
		"description", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<TodoItemTable, Integer> priority = createColumn(
		"priority", Integer.class, Types.INTEGER, Column.FLAG_DEFAULT);
	public final Column<TodoItemTable, Integer> status = createColumn(
		"status", Integer.class, Types.INTEGER, Column.FLAG_DEFAULT);

	private TodoItemTable() {
		super("AlloyMVCSample_TodoItem", TodoItemTable::new);
	}

}