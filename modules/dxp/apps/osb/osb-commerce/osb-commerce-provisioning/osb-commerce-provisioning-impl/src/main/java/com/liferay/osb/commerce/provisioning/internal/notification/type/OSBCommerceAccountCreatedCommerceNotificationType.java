/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.commerce.provisioning.internal.notification.type;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.notification.type.CommerceNotificationType;
import com.liferay.osb.commerce.provisioning.constants.OSBCommerceNotificationConstants;
import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

/**
 * @author Ivica Cardic
 */
@Component(
	immediate = true,
	property = {
		"commerce.notification.type.key=" + OSBCommerceNotificationConstants.OSB_COMMERCE_PROVISIONING_ACCOUNT_CREATED,
		"commerce.notification.type.order:Integer=300"
	},
	service = CommerceNotificationType.class
)
public class OSBCommerceAccountCreatedCommerceNotificationType
	implements CommerceNotificationType {

	@Override
	public String getClassName(Object object) {
		if (!(object instanceof CommerceAccount)) {
			return null;
		}

		return CommerceAccount.class.getName();
	}

	@Override
	public long getClassPK(Object object) {
		if (!(object instanceof CommerceAccount)) {
			return 0;
		}

		CommerceAccount commerceAccount = (CommerceAccount)object;

		return commerceAccount.getCommerceAccountId();
	}

	@Override
	public String getKey() {
		return OSBCommerceNotificationConstants.
			OSB_COMMERCE_PROVISIONING_ACCOUNT_CREATED;
	}

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(
			locale,
			OSBCommerceNotificationConstants.
				OSB_COMMERCE_PROVISIONING_ACCOUNT_CREATED);
	}

}