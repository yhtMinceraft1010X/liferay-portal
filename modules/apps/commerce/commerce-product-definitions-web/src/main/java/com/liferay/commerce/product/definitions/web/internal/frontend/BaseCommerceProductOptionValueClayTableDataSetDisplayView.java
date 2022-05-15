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

package com.liferay.commerce.product.definitions.web.internal.frontend;

import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchemaBuilder;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchemaField;

/**
 * @author Igor Beslic
 */
public abstract class BaseCommerceProductOptionValueClayTableDataSetDisplayView
	extends BaseClayTableDataSetDisplayView {

	@Override
	protected void addActionLinkFields(
		ClayTableSchemaBuilder clayTableSchemaBuilder) {

		ClayTableSchemaField nameClayTableSchemaField =
			clayTableSchemaBuilder.addClayTableSchemaField("name", "name");

		nameClayTableSchemaField.setContentRenderer("actionLink");

		ClayTableSchemaField preselectedClayTableSchemaField =
			clayTableSchemaBuilder.addClayTableSchemaField(
				"preselected", "default");

		preselectedClayTableSchemaField.setActionId("updatePreselected");
		preselectedClayTableSchemaField.setContentRenderer("actionLink");
	}

}