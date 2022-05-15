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

package com.liferay.commerce.internal.upgrade.v2_0_0;

import com.liferay.commerce.internal.upgrade.base.BaseCommerceServiceUpgradeProcess;
import com.liferay.commerce.model.impl.CommerceOrderImpl;
import com.liferay.commerce.model.impl.CommerceOrderPaymentImpl;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * @author Luca Pellizzon
 */
public class CommercePaymentMethodUpgradeProcess
	extends BaseCommerceServiceUpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		if (!hasColumn(CommerceOrderImpl.TABLE_NAME, "transactionId")) {
			addColumn("CommerceOrder", "transactionId", "VARCHAR(75)");
		}

		if (hasColumn(
				CommerceOrderImpl.TABLE_NAME, "commercePaymentMethodId")) {

			addColumn(
				"CommerceOrder", "commercePaymentMethodKey", "VARCHAR(75)");

			String template = StringUtil.read(
				CommercePaymentMethodUpgradeProcess.class.getResourceAsStream(
					"dependencies/CommerceOrderUpgradeProcess.sql"));

			runSQLTemplateString(template, false);

			alterTableDropColumn("CommerceOrder", "commercePaymentMethodId");
		}

		if (hasColumn(
				CommerceOrderPaymentImpl.TABLE_NAME,
				"commercePaymentMethodId")) {

			addColumn(
				"CommerceOrderPayment", "commercePaymentMethodKey",
				"VARCHAR(75)");

			String template = StringUtil.read(
				CommercePaymentMethodUpgradeProcess.class.getResourceAsStream(
					"dependencies/CommerceOrderPaymentUpgradeProcess.sql"));

			runSQLTemplateString(template, false);

			alterTableDropColumn(
				"CommerceOrderPayment", "commercePaymentMethodId");
		}

		if (hasTable("CommercePaymentMethod")) {
			String template = StringUtil.read(
				CommercePaymentMethodUpgradeProcess.class.getResourceAsStream(
					"dependencies/CommercePaymentMethodUpgradeProcess.sql"));

			runSQLTemplateString(template, false);
		}
	}

}