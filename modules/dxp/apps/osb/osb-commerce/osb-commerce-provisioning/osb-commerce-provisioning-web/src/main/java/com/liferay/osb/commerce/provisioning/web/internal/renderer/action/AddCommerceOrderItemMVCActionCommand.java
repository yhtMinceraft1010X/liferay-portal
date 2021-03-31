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

package com.liferay.osb.commerce.provisioning.web.internal.renderer.action;

import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.order.CommerceOrderHttpHelper;
import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CPPortletKeys.CP_PUBLISHER_WEB,
		"mvc.command.name=addCommerceOrderItem"
	},
	service = MVCActionCommand.class
)
public class AddCommerceOrderItemMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long cpInstanceId = ParamUtil.getLong(actionRequest, "CPInstanceId");

		try {
			HttpServletRequest httpServletRequest =
				_portal.getHttpServletRequest(actionRequest);

			CommerceOrder commerceOrder =
				_commerceOrderHttpHelper.getCurrentCommerceOrder(
					httpServletRequest);

			if (commerceOrder == null) {
				commerceOrder = _commerceOrderHttpHelper.addCommerceOrder(
					httpServletRequest);
			}

			List<CommerceOrderItem> commerceOrderItems =
				commerceOrder.getCommerceOrderItems();

			for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
				if (commerceOrderItem.getCPInstanceId() == cpInstanceId) {
					continue;
				}

				_commerceOrderItemService.deleteCommerceOrderItem(
					commerceOrderItem.getCommerceOrderItemId());
			}

			if (commerceOrder.isEmpty()) {
				CommerceContext commerceContext =
					(CommerceContext)httpServletRequest.getAttribute(
						CommerceWebKeys.COMMERCE_CONTEXT);

				ServiceContext serviceContext =
					ServiceContextFactory.getInstance(
						CommerceOrderItem.class.getName(), httpServletRequest);

				_commerceOrderItemService.addCommerceOrderItem(
					commerceOrder.getCommerceOrderId(), cpInstanceId, 1, 0,
					null, commerceContext, serviceContext);
			}

			PortletURL commerceCheckoutPortletURL =
				_commerceOrderHttpHelper.getCommerceCheckoutPortletURL(
					httpServletRequest);

			sendRedirect(
				actionRequest, actionResponse,
				commerceCheckoutPortletURL.toString());
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AddCommerceOrderItemMVCActionCommand.class);

	@Reference
	private CommerceOrderHttpHelper _commerceOrderHttpHelper;

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

	@Reference
	private Portal _portal;

}