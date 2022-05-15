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

package com.liferay.commerce.payment.internal.engine;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.context.CommerceGroupThreadLocal;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderType;
import com.liferay.commerce.order.engine.CommerceOrderEngine;
import com.liferay.commerce.payment.engine.CommercePaymentEngine;
import com.liferay.commerce.payment.method.CommercePaymentMethod;
import com.liferay.commerce.payment.method.CommercePaymentMethodRegistry;
import com.liferay.commerce.payment.model.CommercePaymentMethodGroupRel;
import com.liferay.commerce.payment.model.CommercePaymentMethodGroupRelQualifier;
import com.liferay.commerce.payment.request.CommercePaymentRequest;
import com.liferay.commerce.payment.result.CommercePaymentResult;
import com.liferay.commerce.payment.service.CommercePaymentMethodGroupRelLocalService;
import com.liferay.commerce.payment.service.CommercePaymentMethodGroupRelQualifierLocalService;
import com.liferay.commerce.payment.util.CommercePaymentUtils;
import com.liferay.commerce.payment.util.comparator.CommercePaymentMethodPriorityComparator;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.service.CommerceOrderPaymentLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import java.math.BigDecimal;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 */
@Component(
	enabled = false, immediate = true, service = CommercePaymentEngine.class
)
public class CommercePaymentEngineImpl implements CommercePaymentEngine {

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED, readOnly = false,
		rollbackFor = Exception.class
	)
	public CommercePaymentResult cancelPayment(
			long commerceOrderId, String transactionId,
			HttpServletRequest httpServletRequest)
		throws Exception {

		CommercePaymentMethod commercePaymentMethod =
			_commercePaymentUtils.getCommercePaymentMethod(commerceOrderId);

		if ((commercePaymentMethod == null) ||
			!commercePaymentMethod.isCancelEnabled()) {

			return _commercePaymentUtils.emptyResult(
				commerceOrderId, transactionId);
		}

		CommercePaymentRequest commercePaymentRequest =
			_commercePaymentUtils.getCommercePaymentRequest(
				_commerceOrderLocalService.getCommerceOrder(commerceOrderId),
				_portal.getLocale(httpServletRequest), transactionId, null,
				httpServletRequest, commercePaymentMethod);

		CommercePaymentResult commercePaymentResult =
			commercePaymentMethod.cancelPayment(commercePaymentRequest);

		updateOrderPaymentStatus(
			commerceOrderId, commercePaymentResult.getNewPaymentStatus(),
			commercePaymentResult.getAuthTransactionId(),
			String.valueOf(commercePaymentResult.getResultMessages()));

		return commercePaymentResult;
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED, readOnly = false,
		rollbackFor = Exception.class
	)
	public CommercePaymentResult capturePayment(
			long commerceOrderId, String transactionId,
			HttpServletRequest httpServletRequest)
		throws Exception {

		CommercePaymentMethod commercePaymentMethod =
			_commercePaymentUtils.getCommercePaymentMethod(commerceOrderId);

		if ((commercePaymentMethod == null) ||
			!commercePaymentMethod.isCaptureEnabled()) {

			return _commercePaymentUtils.emptyResult(
				commerceOrderId, transactionId);
		}

		CommercePaymentRequest commercePaymentRequest =
			_commercePaymentUtils.getCommercePaymentRequest(
				_commerceOrderLocalService.getCommerceOrder(commerceOrderId),
				_portal.getLocale(httpServletRequest), transactionId, null,
				httpServletRequest, commercePaymentMethod);

		CommercePaymentResult commercePaymentResult =
			commercePaymentMethod.capturePayment(commercePaymentRequest);

		updateOrderPaymentStatus(
			commerceOrderId, commercePaymentResult.getNewPaymentStatus(),
			commercePaymentResult.getAuthTransactionId(),
			String.valueOf(commercePaymentResult.getResultMessages()));

		return commercePaymentResult;
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED, readOnly = false,
		rollbackFor = Exception.class
	)
	public CommercePaymentResult completePayment(
			long commerceOrderId, String transactionId,
			HttpServletRequest httpServletRequest)
		throws Exception {

		CommercePaymentMethod commercePaymentMethod =
			_commercePaymentUtils.getCommercePaymentMethod(commerceOrderId);

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.getCommerceOrder(commerceOrderId);

		if (BigDecimal.ZERO.compareTo(commerceOrder.getTotal()) == 0) {
			updateOrderPaymentStatus(
				commerceOrderId, CommerceOrderConstants.PAYMENT_STATUS_PAID,
				transactionId, StringPool.BLANK);

			return _commercePaymentUtils.emptyResult(
				commerceOrderId, transactionId);
		}

		if ((commercePaymentMethod == null) ||
			!commercePaymentMethod.isCompleteEnabled()) {

			_completeOrderWithoutPaymentMethod(
				commerceOrderId, httpServletRequest);

			return _commercePaymentUtils.emptyResult(
				commerceOrderId, transactionId);
		}

		CommercePaymentRequest commercePaymentRequest =
			_commercePaymentUtils.getCommercePaymentRequest(
				commerceOrder, _portal.getLocale(httpServletRequest),
				transactionId, null, httpServletRequest, commercePaymentMethod);

		CommercePaymentResult commercePaymentResult =
			commercePaymentMethod.completePayment(commercePaymentRequest);

		updateOrderPaymentStatus(
			commerceOrderId, commercePaymentResult.getNewPaymentStatus(),
			commercePaymentResult.getAuthTransactionId(),
			String.valueOf(commercePaymentResult.getResultMessages()));

		return commercePaymentResult;
	}

	@Override
	public String getCommerceOrderPaymentMethodName(
			CommerceOrder commerceOrder, HttpServletRequest httpServletRequest,
			Locale locale)
		throws PortalException {

		String commercePaymentMethodKey =
			commerceOrder.getCommercePaymentMethodKey();

		if (Validator.isNull(commercePaymentMethodKey)) {
			return StringPool.BLANK;
		}

		CommercePaymentMethodGroupRel commercePaymentMethod =
			_commercePaymentMethodGroupRelLocalService.
				getCommercePaymentMethodGroupRel(
					commerceOrder.getGroupId(), commercePaymentMethodKey);

		if (commercePaymentMethod == null) {
			return StringPool.BLANK;
		}

		String name = commercePaymentMethod.getName(locale);

		if (!commercePaymentMethod.isActive()) {
			name = StringBundler.concat(
				name, " (", LanguageUtil.get(httpServletRequest, "inactive"),
				StringPool.CLOSE_PARENTHESIS);
		}

		return name;
	}

	@Override
	public int getCommercePaymentMethodGroupRelsCount(long groupId) {
		return _commercePaymentMethodGroupRelLocalService.
			getCommercePaymentMethodGroupRelsCount(groupId, true);
	}

	@Override
	public int getCommercePaymentMethodType(long commerceOrderId)
		throws PortalException {

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.getCommerceOrder(commerceOrderId);

		String commercePaymentMethodKey =
			commerceOrder.getCommercePaymentMethodKey();

		if (commercePaymentMethodKey.isEmpty()) {
			return -1;
		}

		CommercePaymentMethod commercePaymentMethod =
			_commercePaymentMethodRegistry.getCommercePaymentMethod(
				commercePaymentMethodKey);

		return commercePaymentMethod.getPaymentType();
	}

	@Override
	public List<CommercePaymentMethod> getEnabledCommercePaymentMethodsForOrder(
			long groupId, long commerceOrderId)
		throws PortalException {

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.getCommerceOrder(commerceOrderId);

		boolean subscriptionOrder = commerceOrder.isSubscriptionOrder();

		CommerceAddress commerceAddress = commerceOrder.getBillingAddress();

		if (commerceAddress == null) {
			commerceAddress = commerceOrder.getShippingAddress();
		}

		if (commerceAddress != null) {
			return _getCommercePaymentMethodsList(
				_commercePaymentMethodGroupRelLocalService.
					getCommercePaymentMethodGroupRels(
						groupId, commerceAddress.getCountryId(), true),
				commerceOrder.getCommerceOrderTypeId(), subscriptionOrder);
		}

		return _getCommercePaymentMethodsList(
			_commercePaymentMethodGroupRelLocalService.
				getCommercePaymentMethodGroupRels(groupId, true),
			commerceOrder.getCommerceOrderTypeId(), subscriptionOrder);
	}

	@Override
	public String getPaymentMethodImageURL(
			long groupId, String paymentMethodKey, ThemeDisplay themeDisplay)
		throws PortalException {

		CommercePaymentMethodGroupRel commercePaymentMethodGroupRel =
			_commercePaymentMethodGroupRelLocalService.
				getCommercePaymentMethodGroupRel(groupId, paymentMethodKey);

		return commercePaymentMethodGroupRel.getImageURL(themeDisplay);
	}

	@Override
	public String getPaymentMethodName(String paymentMethodKey, Locale locale) {
		CommercePaymentMethod commercePaymentMethod =
			_commercePaymentMethodRegistry.getCommercePaymentMethod(
				paymentMethodKey);

		return commercePaymentMethod.getName(locale);
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED, readOnly = false,
		rollbackFor = Exception.class
	)
	public CommercePaymentResult partiallyRefundPayment(
			long commerceOrderId, String transactionId,
			HttpServletRequest httpServletRequest)
		throws Exception {

		CommercePaymentMethod commercePaymentMethod =
			_commercePaymentUtils.getCommercePaymentMethod(commerceOrderId);

		if ((commercePaymentMethod == null) ||
			!commercePaymentMethod.isPartialRefundEnabled()) {

			return _commercePaymentUtils.emptyResult(
				commerceOrderId, transactionId);
		}

		CommercePaymentRequest commercePaymentRequest =
			_commercePaymentUtils.getCommercePaymentRequest(
				_commerceOrderLocalService.getCommerceOrder(commerceOrderId),
				_portal.getLocale(httpServletRequest), null, null,
				httpServletRequest, commercePaymentMethod);

		return commercePaymentMethod.partiallyRefundPayment(
			commercePaymentRequest);
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED, readOnly = false,
		rollbackFor = Exception.class
	)
	public CommercePaymentResult postProcessPayment(
			long commerceOrderId, String transactionId,
			HttpServletRequest httpServletRequest)
		throws Exception {

		return _commercePaymentUtils.emptyResult(
			commerceOrderId, transactionId);
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED, readOnly = false,
		rollbackFor = Exception.class
	)
	public CommercePaymentResult processPayment(
			long commerceOrderId, String nextUrl,
			HttpServletRequest httpServletRequest)
		throws Exception {

		CommercePaymentMethod commercePaymentMethod =
			_commercePaymentUtils.getCommercePaymentMethod(commerceOrderId);

		if ((commercePaymentMethod == null) ||
			!commercePaymentMethod.isProcessPaymentEnabled()) {

			return _commercePaymentUtils.emptyResult(
				commerceOrderId, StringPool.BLANK);
		}

		CommercePaymentRequest commercePaymentRequest =
			_commercePaymentUtils.getCommercePaymentRequest(
				_commerceOrderLocalService.getCommerceOrder(commerceOrderId),
				_portal.getLocale(httpServletRequest), null, nextUrl,
				httpServletRequest, commercePaymentMethod);

		CommercePaymentResult commercePaymentResult =
			commercePaymentMethod.processPayment(commercePaymentRequest);

		updateOrderPaymentStatus(
			commerceOrderId, commercePaymentResult.getNewPaymentStatus(),
			commercePaymentResult.getAuthTransactionId(),
			String.valueOf(commercePaymentResult.getResultMessages()));

		return commercePaymentResult;
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED, readOnly = false,
		rollbackFor = Exception.class
	)
	public CommercePaymentResult refundPayment(
			long commerceOrderId, String transactionId,
			HttpServletRequest httpServletRequest)
		throws Exception {

		CommercePaymentMethod commercePaymentMethod =
			_commercePaymentUtils.getCommercePaymentMethod(commerceOrderId);

		if ((commercePaymentMethod == null) ||
			!commercePaymentMethod.isRefundEnabled()) {

			return _commercePaymentUtils.emptyResult(
				commerceOrderId, transactionId);
		}

		CommercePaymentRequest commercePaymentRequest =
			_commercePaymentUtils.getCommercePaymentRequest(
				_commerceOrderLocalService.getCommerceOrder(commerceOrderId),
				_portal.getLocale(httpServletRequest), transactionId, null,
				httpServletRequest, commercePaymentMethod);

		return commercePaymentMethod.refundPayment(commercePaymentRequest);
	}

	@Override
	public CommerceOrder updateOrderPaymentStatus(
			long commerceOrderId, int paymentStatus, String transactionId,
			String result)
		throws PortalException {

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.getCommerceOrder(commerceOrderId);

		CommerceGroupThreadLocal.set(
			_groupLocalService.fetchGroup(commerceOrder.getGroupId()));

		commerceOrder =
			_commerceOrderLocalService.updatePaymentStatusAndTransactionId(
				commerceOrder.getUserId(), commerceOrderId, paymentStatus,
				transactionId);

		_commerceOrderPaymentLocalService.addCommerceOrderPayment(
			commerceOrderId, paymentStatus, result);

		if ((paymentStatus == CommerceOrderConstants.PAYMENT_STATUS_PAID) &&
			(commerceOrder.getOrderStatus() !=
				CommerceOrderConstants.ORDER_STATUS_PENDING)) {

			long userId = commerceOrder.getUserId();

			PermissionChecker permissionChecker =
				PermissionThreadLocal.getPermissionChecker();

			if (permissionChecker != null) {
				userId = permissionChecker.getUserId();
			}

			commerceOrder = _commerceOrderEngine.transitionCommerceOrder(
				commerceOrder, CommerceOrderConstants.ORDER_STATUS_PENDING,
				userId);
		}

		return commerceOrder;
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED, readOnly = false,
		rollbackFor = Exception.class
	)
	public CommercePaymentResult voidTransaction(
			long commerceOrderId, String transactionId,
			HttpServletRequest httpServletRequest)
		throws Exception {

		CommercePaymentMethod commercePaymentMethod =
			_commercePaymentUtils.getCommercePaymentMethod(commerceOrderId);

		if ((commercePaymentMethod == null) ||
			!commercePaymentMethod.isVoidEnabled()) {

			return _commercePaymentUtils.emptyResult(
				commerceOrderId, transactionId);
		}

		CommercePaymentRequest commercePaymentRequest =
			_commercePaymentUtils.getCommercePaymentRequest(
				_commerceOrderLocalService.getCommerceOrder(commerceOrderId),
				_portal.getLocale(httpServletRequest), null, null,
				httpServletRequest, commercePaymentMethod);

		return commercePaymentMethod.voidTransaction(commercePaymentRequest);
	}

	private void _completeOrderWithoutPaymentMethod(
			long commerceOrderId, HttpServletRequest httpServletRequest)
		throws Exception {

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.getCommerceOrder(commerceOrderId);

		CommerceGroupThreadLocal.set(
			_groupLocalService.fetchGroup(commerceOrder.getGroupId()));

		_commerceOrderLocalService.updatePaymentStatusAndTransactionId(
			_portal.getUserId(httpServletRequest), commerceOrderId,
			CommerceOrderConstants.PAYMENT_STATUS_PAID, StringPool.BLANK);

		_commerceOrderPaymentLocalService.addCommerceOrderPayment(
			commerceOrderId, CommerceOrderConstants.PAYMENT_STATUS_PAID,
			StringPool.BLANK);
	}

	private List<CommercePaymentMethod> _getCommercePaymentMethodsList(
		List<CommercePaymentMethodGroupRel> commercePaymentMethodGroupRels,
		long commerceOrderTypeId, boolean subscriptionOrder) {

		ListUtil.sort(
			commercePaymentMethodGroupRels,
			new CommercePaymentMethodPriorityComparator());

		List<CommercePaymentMethod> commercePaymentMethods = new LinkedList<>();

		for (CommercePaymentMethodGroupRel commercePaymentMethodGroupRel :
				commercePaymentMethodGroupRels) {

			List<CommercePaymentMethodGroupRelQualifier>
				commercePaymentMethodGroupRelQualifiers =
					_commercePaymentMethodGroupRelQualifierLocalService.
						getCommercePaymentMethodGroupRelQualifiers(
							CommerceOrderType.class.getName(),
							commercePaymentMethodGroupRel.
								getCommercePaymentMethodGroupRelId());

			if ((commerceOrderTypeId > 0) &&
				ListUtil.isNotEmpty(commercePaymentMethodGroupRelQualifiers) &&
				!ListUtil.exists(
					commercePaymentMethodGroupRelQualifiers,
					commercePaymentMethodGroupRelQualifier -> {
						long classPK =
							commercePaymentMethodGroupRelQualifier.getClassPK();

						return classPK == commerceOrderTypeId;
					})) {

				continue;
			}

			CommercePaymentMethod commercePaymentMethod =
				_commercePaymentMethodRegistry.getCommercePaymentMethod(
					commercePaymentMethodGroupRel.getEngineKey());

			if (subscriptionOrder &&
				!commercePaymentMethod.isProcessRecurringEnabled()) {

				continue;
			}

			commercePaymentMethods.add(commercePaymentMethod);
		}

		return commercePaymentMethods;
	}

	@Reference
	private CommerceOrderEngine _commerceOrderEngine;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private CommerceOrderPaymentLocalService _commerceOrderPaymentLocalService;

	@Reference
	private CommercePaymentMethodGroupRelLocalService
		_commercePaymentMethodGroupRelLocalService;

	@Reference
	private CommercePaymentMethodGroupRelQualifierLocalService
		_commercePaymentMethodGroupRelQualifierLocalService;

	@Reference
	private CommercePaymentMethodRegistry _commercePaymentMethodRegistry;

	@Reference
	private CommercePaymentUtils _commercePaymentUtils;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private Portal _portal;

}