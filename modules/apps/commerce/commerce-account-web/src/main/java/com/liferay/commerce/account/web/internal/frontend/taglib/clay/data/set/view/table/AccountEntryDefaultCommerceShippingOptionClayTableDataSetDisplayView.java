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

package com.liferay.commerce.account.web.internal.frontend.taglib.clay.data.set.view.table;

import com.liferay.commerce.account.web.internal.frontend.constants.CommerceAccountDataSetConstants;
import com.liferay.frontend.taglib.clay.data.set.ClayDataSetDisplayView;
import com.liferay.frontend.taglib.clay.data.set.view.table.BaseTableClayDataSetDisplayView;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchema;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchemaBuilder;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchemaBuilderFactory;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Sbarra
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false, immediate = true,
	property = "clay.data.set.display.name=" + CommerceAccountDataSetConstants.COMMERCE_DATA_SET_KEY_ACCOUNT_ENTRY_DEFAULT_SHIPPING_OPTIONS,
	service = ClayDataSetDisplayView.class
)
public class
	AccountEntryDefaultCommerceShippingOptionClayTableDataSetDisplayView
		extends BaseTableClayDataSetDisplayView {

	@Override
	public ClayTableSchema getClayTableSchema(Locale locale) {
		ClayTableSchemaBuilder clayTableSchemaBuilder =
			_clayTableSchemaBuilderFactory.create();

		clayTableSchemaBuilder.addClayTableSchemaField(
			"channelName", "channel");

		clayTableSchemaBuilder.addClayTableSchemaField(
			"shippingMethodName", "shipping-method");

		clayTableSchemaBuilder.addClayTableSchemaField(
			"shippingOptionName", "default-shipping-option");

		clayTableSchemaBuilder.addClayTableSchemaField("active", "active");

		return clayTableSchemaBuilder.build();
	}

	@Reference
	private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;

}