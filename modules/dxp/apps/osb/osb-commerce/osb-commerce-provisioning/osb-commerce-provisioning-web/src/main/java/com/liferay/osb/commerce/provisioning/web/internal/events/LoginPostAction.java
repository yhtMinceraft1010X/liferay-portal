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

package com.liferay.osb.commerce.provisioning.web.internal.events;

import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.osb.commerce.provisioning.constants.OSBCommerceProvisioningConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.CookieKeys;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica Cardic
 */
@Component(
	immediate = true, property = "key=login.events.post",
	service = LifecycleAction.class
)
public class LoginPostAction extends Action {

	@Override
	public void run(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		try {
			Group group = _groupLocalService.fetchFriendlyURLGroup(
				_portal.getDefaultCompanyId(),
				OSBCommerceProvisioningConstants.
					FRIENDLY_URL_OSB_COMMERCE_PROVISIONING);

			if (group == null) {
				return;
			}

			String cookieValue = CookieKeys.getCookie(
				httpServletRequest,
				_getCookieName(
					_commerceChannelLocalService.
						getCommerceChannelGroupIdBySiteGroupId(
							group.getGroupId())));

			if (Validator.isNull(cookieValue)) {
				_checkCompanyAdmin(httpServletRequest, httpServletResponse);

				return;
			}

			httpServletResponse.sendRedirect(
				StringBundler.concat(
					PropsValues.
						LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING,
					OSBCommerceProvisioningConstants.
						FRIENDLY_URL_OSB_COMMERCE_PROVISIONING,
					OSBCommerceProvisioningConstants.
						OSB_COMMERCE_CHECKOUT_URL));
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	private void _checkCompanyAdmin(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, PortalException {

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(
				_portal.getUser(httpServletRequest));

		if (!permissionChecker.isCompanyAdmin()) {
			httpServletResponse.sendRedirect(
				PropsValues.LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING +
					OSBCommerceProvisioningConstants.
						FRIENDLY_URL_OSB_COMMERCE_PROVISIONING);
		}
	}

	private String _getCookieName(long groupId) {
		return CommerceOrder.class.getName() + StringPool.POUND + groupId;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LoginPostAction.class);

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private Portal _portal;

}