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

package com.liferay.commerce.product.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.math.BigDecimal;

import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;CPDefinitionOptionValueRel&quot; database table.
 *
 * @author Marco Leo
 * @see CPDefinitionOptionValueRel
 * @generated
 */
public class CPDefinitionOptionValueRelTable
	extends BaseTable<CPDefinitionOptionValueRelTable> {

	public static final CPDefinitionOptionValueRelTable INSTANCE =
		new CPDefinitionOptionValueRelTable();

	public final Column<CPDefinitionOptionValueRelTable, Long> mvccVersion =
		createColumn(
			"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<CPDefinitionOptionValueRelTable, Long> ctCollectionId =
		createColumn(
			"ctCollectionId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<CPDefinitionOptionValueRelTable, String> uuid =
		createColumn("uuid_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, Long>
		CPDefinitionOptionValueRelId = createColumn(
			"CPDefinitionOptionValueRelId", Long.class, Types.BIGINT,
			Column.FLAG_PRIMARY);
	public final Column<CPDefinitionOptionValueRelTable, Long> groupId =
		createColumn("groupId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, Long> companyId =
		createColumn(
			"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, Long> userId =
		createColumn("userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, String> userName =
		createColumn(
			"userName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, Date> createDate =
		createColumn(
			"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, Date> modifiedDate =
		createColumn(
			"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, Long>
		CPDefinitionOptionRelId = createColumn(
			"CPDefinitionOptionRelId", Long.class, Types.BIGINT,
			Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, String>
		CPInstanceUuid = createColumn(
			"CPInstanceUuid", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, Long> CProductId =
		createColumn(
			"CProductId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, String> name =
		createColumn("name", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, Double> priority =
		createColumn(
			"priority", Double.class, Types.DOUBLE, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, String> key =
		createColumn("key_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, Integer> quantity =
		createColumn(
			"quantity", Integer.class, Types.INTEGER, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, Boolean> preselected =
		createColumn(
			"preselected", Boolean.class, Types.BOOLEAN, Column.FLAG_DEFAULT);
	public final Column<CPDefinitionOptionValueRelTable, BigDecimal> price =
		createColumn(
			"price", BigDecimal.class, Types.DECIMAL, Column.FLAG_DEFAULT);

	private CPDefinitionOptionValueRelTable() {
		super(
			"CPDefinitionOptionValueRel", CPDefinitionOptionValueRelTable::new);
	}

}