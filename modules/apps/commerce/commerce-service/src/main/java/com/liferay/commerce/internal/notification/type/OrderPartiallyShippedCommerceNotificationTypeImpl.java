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

package com.liferay.commerce.internal.notification.type;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.notification.type.CommerceNotificationType;
import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

/**
 * @author Luca Pellizzon
 */
@Component(
	enabled = false, immediate = true,
	property = {
		"commerce.notification.type.key=" + CommerceOrderConstants.ORDER_NOTIFICATION_PARTIALLY_SHIPPED,
		"commerce.notification.type.order:Integer=60"
	},
	service = CommerceNotificationType.class
)
public class OrderPartiallyShippedCommerceNotificationTypeImpl
	implements CommerceNotificationType {

	@Override
	public String getClassName(Object object) {
		if (!(object instanceof CommerceOrder)) {
			return null;
		}

		return CommerceOrder.class.getName();
	}

	@Override
	public long getClassPK(Object object) {
		if (!(object instanceof CommerceOrder)) {
			return 0;
		}

		CommerceOrder commerceOrder = (CommerceOrder)object;

		return commerceOrder.getCommerceOrderId();
	}

	@Override
	public String getKey() {
		return CommerceOrderConstants.ORDER_NOTIFICATION_PARTIALLY_SHIPPED;
	}

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(
			locale,
			CommerceOrderConstants.ORDER_NOTIFICATION_PARTIALLY_SHIPPED);
	}

}