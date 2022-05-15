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

package com.liferay.object.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Clob;
import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;ObjectValidationRule&quot; database table.
 *
 * @author Marco Leo
 * @see ObjectValidationRule
 * @generated
 */
public class ObjectValidationRuleTable
	extends BaseTable<ObjectValidationRuleTable> {

	public static final ObjectValidationRuleTable INSTANCE =
		new ObjectValidationRuleTable();

	public final Column<ObjectValidationRuleTable, Long> mvccVersion =
		createColumn(
			"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<ObjectValidationRuleTable, String> uuid = createColumn(
		"uuid_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<ObjectValidationRuleTable, Long>
		objectValidationRuleId = createColumn(
			"objectValidationRuleId", Long.class, Types.BIGINT,
			Column.FLAG_PRIMARY);
	public final Column<ObjectValidationRuleTable, Long> companyId =
		createColumn(
			"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<ObjectValidationRuleTable, Long> userId = createColumn(
		"userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<ObjectValidationRuleTable, String> userName =
		createColumn(
			"userName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<ObjectValidationRuleTable, Date> createDate =
		createColumn(
			"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<ObjectValidationRuleTable, Date> modifiedDate =
		createColumn(
			"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<ObjectValidationRuleTable, Long> objectDefinitionId =
		createColumn(
			"objectDefinitionId", Long.class, Types.BIGINT,
			Column.FLAG_DEFAULT);
	public final Column<ObjectValidationRuleTable, Boolean> active =
		createColumn(
			"active_", Boolean.class, Types.BOOLEAN, Column.FLAG_DEFAULT);
	public final Column<ObjectValidationRuleTable, String> engine =
		createColumn(
			"engine", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<ObjectValidationRuleTable, String> errorLabel =
		createColumn(
			"errorLabel", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<ObjectValidationRuleTable, String> name = createColumn(
		"name", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<ObjectValidationRuleTable, Clob> script = createColumn(
		"script", Clob.class, Types.CLOB, Column.FLAG_DEFAULT);

	private ObjectValidationRuleTable() {
		super("ObjectValidationRule", ObjectValidationRuleTable::new);
	}

}