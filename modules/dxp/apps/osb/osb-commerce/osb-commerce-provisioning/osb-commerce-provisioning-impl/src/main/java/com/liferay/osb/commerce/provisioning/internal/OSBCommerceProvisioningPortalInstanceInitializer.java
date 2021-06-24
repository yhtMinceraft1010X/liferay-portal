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

package com.liferay.osb.commerce.provisioning.internal;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.constants.CommerceSubscriptionEntryConstants;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.model.CommerceSubscriptionEntry;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.service.CommerceSubscriptionEntryLocalService;
import com.liferay.osb.commerce.provisioning.OSBCommercePortalInstanceStatus;
import com.liferay.osb.commerce.provisioning.constants.OSBCommercePortalInstanceConstants;
import com.liferay.osb.commerce.provisioning.internal.cloud.client.DXPCloudClientClientFactory;
import com.liferay.osb.commerce.provisioning.internal.cloud.client.DXPCloudProvisioningClient;
import com.liferay.osb.commerce.provisioning.internal.cloud.client.dto.PortalInstance;
import com.liferay.petra.string.CharPool;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 */
@Component(service = OSBCommerceProvisioningPortalInstanceInitializer.class)
public class OSBCommerceProvisioningPortalInstanceInitializer {

	@Transactional(
		propagation = Propagation.REQUIRED, rollbackFor = Exception.class
	)
	public void initializePortalInstance(long commerceOrderId)
		throws Exception {

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.getCommerceOrder(commerceOrderId);

		if (commerceOrder == null) {
			return;
		}

		List<CommerceOrderItem> commerceOrderItems =
			commerceOrder.getCommerceOrderItems();

		CommerceOrderItem commerceOrderItem = commerceOrderItems.get(0);

		CommerceSubscriptionEntry commerceSubscriptionEntry =
			_commerceSubscriptionEntryLocalService.
				fetchCommerceSubscriptionEntryByCommerceOrderItemId(
					commerceOrderItem.getCommerceOrderItemId());

		if (commerceSubscriptionEntry == null) {
			return;
		}

		if (commerceSubscriptionEntry.getSubscriptionStatus() !=
				CommerceSubscriptionEntryConstants.SUBSCRIPTION_STATUS_ACTIVE) {

			return;
		}

		commerceSubscriptionEntry = _updateSubscriptionTypeSettingsProperties(
			commerceSubscriptionEntry,
			OSBCommercePortalInstanceConstants.PORTAL_INSTANCE_STATUS,
			String.valueOf(
				OSBCommercePortalInstanceStatus.IN_PROGRESS.getStatus()));

		try {
			_provisionPortalInstance(commerceOrder, commerceSubscriptionEntry);
		}
		catch (Exception exception) {
			_updateSubscriptionTypeSettingsProperties(
				commerceSubscriptionEntry,
				OSBCommercePortalInstanceConstants.PORTAL_INSTANCE_STATUS,
				String.valueOf(
					OSBCommercePortalInstanceStatus.FAILED.getStatus()));

			throw exception;
		}
	}

	@Activate
	protected void activate() {
		_dxpCloudProvisioningClient =
			_dxpCloudClientClientFactory.getDXPCloudClient();
	}

	@Deactivate
	protected void deactivate() {
		_dxpCloudProvisioningClient.destroy();
	}

	private void _provisionPortalInstance(
			CommerceOrder commerceOrder,
			CommerceSubscriptionEntry commerceSubscriptionEntry)
		throws Exception {

		CommerceAccount commerceAccount = commerceOrder.getCommerceAccount();

		PortalInstance portalInstance =
			_dxpCloudProvisioningClient.postPortalInstance(
				_toPortalInstance(
					commerceAccount.getEmail(), commerceOrder.getUserId()));

		_updateSubscriptionTypeSettingsProperties(
			commerceSubscriptionEntry,
			OSBCommercePortalInstanceConstants.PORTAL_INSTANCE_STATUS,
			String.valueOf(OSBCommercePortalInstanceStatus.ACTIVE.getStatus()),
			OSBCommercePortalInstanceConstants.PORTAL_INSTANCE_VIRTUAL_HOSTNAME,
			portalInstance.getVirtualHost(),
			OSBCommercePortalInstanceConstants.PORTAL_INSTANCE_WEB_ID,
			portalInstance.getPortalInstanceId());
	}

	private PortalInstance _toPortalInstance(String email, long userId)
		throws Exception {

		return new PortalInstance() {
			{
				setDomain(email.substring(email.indexOf(CharPool.AT) + 1));
				setPortalInstanceInitializerKey(
					"osb-commerce-portal-instance-initializer");
				setPortalInstanceInitializerPayload(
					_toPortalInstanceInitializerPayload(userId));
			}
		};
	}

	private HashMap<String, String> _toPortalInstanceInitializerPayload(
			long userId)
		throws Exception {

		User user = _userLocalService.getUser(userId);

		return HashMapBuilder.put(
			"birthDate",
			() -> {
				DateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss'Z'");

				return dateFormat.format(user.getBirthday());
			}
		).put(
			"emailAddress", user.getEmailAddress()
		).put(
			"firstName", user.getFirstName()
		).put(
			"jobTitle", user.getJobTitle()
		).put(
			"languageId", user.getLanguageId()
		).put(
			"lastName", user.getLastName()
		).put(
			"middleName", user.getMiddleName()
		).put(
			"screenName", user.getScreenName()
		).build();
	}

	private CommerceSubscriptionEntry _updateSubscriptionTypeSettingsProperties(
		CommerceSubscriptionEntry commerceSubscriptionEntry, String... values) {

		UnicodeProperties subscriptionTypeSettingsUnicodeProperties =
			commerceSubscriptionEntry.getSubscriptionTypeSettingsProperties();

		for (int i = 0; i < values.length; i += 2) {
			subscriptionTypeSettingsUnicodeProperties.setProperty(
				values[i], values[i + 1]);
		}

		commerceSubscriptionEntry.setSubscriptionTypeSettingsProperties(
			subscriptionTypeSettingsUnicodeProperties);

		return _commerceSubscriptionEntryLocalService.
			updateCommerceSubscriptionEntry(commerceSubscriptionEntry);
	}

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private CommerceSubscriptionEntryLocalService
		_commerceSubscriptionEntryLocalService;

	@Reference
	private DXPCloudClientClientFactory _dxpCloudClientClientFactory;

	private DXPCloudProvisioningClient _dxpCloudProvisioningClient;

	@Reference
	private UserLocalService _userLocalService;

}