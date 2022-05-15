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

package com.liferay.account.internal.object.system;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.model.AccountEntryTable;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.model.ObjectField;
import com.liferay.object.system.BaseSystemObjectDefinitionMetadata;
import com.liferay.object.system.SystemObjectDefinitionMetadata;
import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.Table;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Gabriel Albuquerque
 */
@Component(service = SystemObjectDefinitionMetadata.class)
public class AccountEntrySystemObjectDefinitionMetadata
	extends BaseSystemObjectDefinitionMetadata {

	@Override
	public Map<Locale, String> getLabelMap() {
		return createLabelMap("account");
	}

	@Override
	public Class<?> getModelClass() {
		return AccountEntry.class;
	}

	@Override
	public List<ObjectField> getObjectFields() {
		return Arrays.asList(
			createObjectField("Text", "String", "name", "name", true),
			createObjectField(
				"Text", "String", "description", "description", false),
			createObjectField("Text", "String", "type", "type", true));
	}

	@Override
	public Map<Locale, String> getPluralLabelMap() {
		return createLabelMap("accounts");
	}

	@Override
	public Column<?, Long> getPrimaryKeyColumn() {
		return AccountEntryTable.INSTANCE.accountEntryId;
	}

	@Override
	public String getRESTContextPath() {
		return "headless-admin-user/v1.0/accounts";
	}

	@Override
	public String getScope() {
		return ObjectDefinitionConstants.SCOPE_COMPANY;
	}

	@Override
	public Table getTable() {
		return AccountEntryTable.INSTANCE;
	}

	@Override
	public int getVersion() {
		return 1;
	}

}