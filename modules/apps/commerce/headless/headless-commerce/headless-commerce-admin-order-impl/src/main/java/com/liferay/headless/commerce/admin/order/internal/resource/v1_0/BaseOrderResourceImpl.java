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

package com.liferay.headless.commerce.admin.order.internal.resource.v1_0;

import com.liferay.headless.commerce.admin.order.dto.v1_0.Order;
import com.liferay.headless.commerce.admin.order.resource.v1_0.OrderResource;
import com.liferay.petra.function.UnsafeBiConsumer;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.filter.ExpressionConvert;
import com.liferay.portal.odata.filter.FilterParser;
import com.liferay.portal.odata.filter.FilterParserProvider;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.batch.engine.VulcanBatchEngineTaskItemDelegate;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResource;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.ActionUtil;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.io.Serializable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author Alessio Antonio Rendina
 * @generated
 */
@Generated("")
@javax.ws.rs.Path("/v1.0")
public abstract class BaseOrderResourceImpl
	implements EntityModelResource, OrderResource,
			   VulcanBatchEngineTaskItemDelegate<Order> {

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "search"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "filter"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "page"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "pageSize"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "sort"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {@io.swagger.v3.oas.annotations.tags.Tag(name = "Order")}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path("/orders")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Page<Order> getOrdersPage(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.ws.rs.QueryParam("search")
			String search,
			@javax.ws.rs.core.Context Filter filter,
			@javax.ws.rs.core.Context Pagination pagination,
			@javax.ws.rs.core.Context Sort[] sorts)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders' -d $'{"accountExternalReferenceCode": ___, "accountId": ___, "advanceStatus": ___, "billingAddress": ___, "billingAddressId": ___, "channelExternalReferenceCode": ___, "channelId": ___, "couponCode": ___, "createDate": ___, "currencyCode": ___, "customFields": ___, "deliveryTermId": ___, "externalReferenceCode": ___, "id": ___, "lastPriceUpdateDate": ___, "modifiedDate": ___, "orderDate": ___, "orderItems": ___, "orderStatus": ___, "orderTypeExternalReferenceCode": ___, "orderTypeId": ___, "paymentMethod": ___, "paymentStatus": ___, "paymentTermId": ___, "printedNote": ___, "purchaseOrderNumber": ___, "requestedDeliveryDate": ___, "shippingAddress": ___, "shippingAddressId": ___, "shippingAmount": ___, "shippingAmountFormatted": ___, "shippingAmountValue": ___, "shippingDiscountAmount": ___, "shippingDiscountAmountFormatted": ___, "shippingDiscountAmountValue": ___, "shippingDiscountPercentageLevel1": ___, "shippingDiscountPercentageLevel1WithTaxAmount": ___, "shippingDiscountPercentageLevel2": ___, "shippingDiscountPercentageLevel2WithTaxAmount": ___, "shippingDiscountPercentageLevel3": ___, "shippingDiscountPercentageLevel3WithTaxAmount": ___, "shippingDiscountPercentageLevel4": ___, "shippingDiscountPercentageLevel4WithTaxAmount": ___, "shippingDiscountWithTaxAmount": ___, "shippingDiscountWithTaxAmountFormatted": ___, "shippingMethod": ___, "shippingOption": ___, "shippingWithTaxAmount": ___, "shippingWithTaxAmountFormatted": ___, "shippingWithTaxAmountValue": ___, "subtotal": ___, "subtotalDiscountAmount": ___, "subtotalDiscountAmountFormatted": ___, "subtotalDiscountPercentageLevel1": ___, "subtotalDiscountPercentageLevel1WithTaxAmount": ___, "subtotalDiscountPercentageLevel2": ___, "subtotalDiscountPercentageLevel2WithTaxAmount": ___, "subtotalDiscountPercentageLevel3": ___, "subtotalDiscountPercentageLevel3WithTaxAmount": ___, "subtotalDiscountPercentageLevel4": ___, "subtotalDiscountPercentageLevel4WithTaxAmount": ___, "subtotalDiscountWithTaxAmount": ___, "subtotalDiscountWithTaxAmountFormatted": ___, "subtotalFormatted": ___, "subtotalWithTaxAmount": ___, "subtotalWithTaxAmountFormatted": ___, "taxAmount": ___, "taxAmountFormatted": ___, "taxAmountValue": ___, "total": ___, "totalDiscountAmount": ___, "totalDiscountAmountFormatted": ___, "totalDiscountAmountValue": ___, "totalDiscountPercentageLevel1": ___, "totalDiscountPercentageLevel1WithTaxAmount": ___, "totalDiscountPercentageLevel2": ___, "totalDiscountPercentageLevel2WithTaxAmount": ___, "totalDiscountPercentageLevel3": ___, "totalDiscountPercentageLevel3WithTaxAmount": ___, "totalDiscountPercentageLevel4": ___, "totalDiscountPercentageLevel4WithTaxAmount": ___, "totalDiscountWithTaxAmount": ___, "totalDiscountWithTaxAmountFormatted": ___, "totalDiscountWithTaxAmountValue": ___, "totalFormatted": ___, "totalWithTaxAmount": ___, "totalWithTaxAmountFormatted": ___, "transactionId": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {@io.swagger.v3.oas.annotations.tags.Tag(name = "Order")}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.Path("/orders")
	@javax.ws.rs.POST
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Order postOrder(Order order) throws Exception {
		return new Order();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/batch'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "callbackURL"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {@io.swagger.v3.oas.annotations.tags.Tag(name = "Order")}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.Path("/orders/batch")
	@javax.ws.rs.POST
	@javax.ws.rs.Produces("application/json")
	@Override
	public Response postOrderBatch(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.ws.rs.QueryParam("callbackURL")
			String callbackURL,
			Object object)
		throws Exception {

		vulcanBatchEngineImportTaskResource.setContextAcceptLanguage(
			contextAcceptLanguage);
		vulcanBatchEngineImportTaskResource.setContextCompany(contextCompany);
		vulcanBatchEngineImportTaskResource.setContextHttpServletRequest(
			contextHttpServletRequest);
		vulcanBatchEngineImportTaskResource.setContextUriInfo(contextUriInfo);
		vulcanBatchEngineImportTaskResource.setContextUser(contextUser);

		Response.ResponseBuilder responseBuilder = Response.accepted();

		return responseBuilder.entity(
			vulcanBatchEngineImportTaskResource.postImportTask(
				Order.class.getName(), callbackURL, null, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/by-externalReferenceCode/{externalReferenceCode}'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "externalReferenceCode"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {@io.swagger.v3.oas.annotations.tags.Tag(name = "Order")}
	)
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path(
		"/orders/by-externalReferenceCode/{externalReferenceCode}"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Response deleteOrderByExternalReferenceCode(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("externalReferenceCode")
			String externalReferenceCode)
		throws Exception {

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/by-externalReferenceCode/{externalReferenceCode}'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "externalReferenceCode"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {@io.swagger.v3.oas.annotations.tags.Tag(name = "Order")}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path(
		"/orders/by-externalReferenceCode/{externalReferenceCode}"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Order getOrderByExternalReferenceCode(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("externalReferenceCode")
			String externalReferenceCode)
		throws Exception {

		return new Order();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PATCH' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/by-externalReferenceCode/{externalReferenceCode}' -d $'{"accountExternalReferenceCode": ___, "accountId": ___, "advanceStatus": ___, "billingAddress": ___, "billingAddressId": ___, "channelExternalReferenceCode": ___, "channelId": ___, "couponCode": ___, "createDate": ___, "currencyCode": ___, "customFields": ___, "deliveryTermId": ___, "externalReferenceCode": ___, "id": ___, "lastPriceUpdateDate": ___, "modifiedDate": ___, "orderDate": ___, "orderItems": ___, "orderStatus": ___, "orderTypeExternalReferenceCode": ___, "orderTypeId": ___, "paymentMethod": ___, "paymentStatus": ___, "paymentTermId": ___, "printedNote": ___, "purchaseOrderNumber": ___, "requestedDeliveryDate": ___, "shippingAddress": ___, "shippingAddressId": ___, "shippingAmount": ___, "shippingAmountFormatted": ___, "shippingAmountValue": ___, "shippingDiscountAmount": ___, "shippingDiscountAmountFormatted": ___, "shippingDiscountAmountValue": ___, "shippingDiscountPercentageLevel1": ___, "shippingDiscountPercentageLevel1WithTaxAmount": ___, "shippingDiscountPercentageLevel2": ___, "shippingDiscountPercentageLevel2WithTaxAmount": ___, "shippingDiscountPercentageLevel3": ___, "shippingDiscountPercentageLevel3WithTaxAmount": ___, "shippingDiscountPercentageLevel4": ___, "shippingDiscountPercentageLevel4WithTaxAmount": ___, "shippingDiscountWithTaxAmount": ___, "shippingDiscountWithTaxAmountFormatted": ___, "shippingMethod": ___, "shippingOption": ___, "shippingWithTaxAmount": ___, "shippingWithTaxAmountFormatted": ___, "shippingWithTaxAmountValue": ___, "subtotal": ___, "subtotalDiscountAmount": ___, "subtotalDiscountAmountFormatted": ___, "subtotalDiscountPercentageLevel1": ___, "subtotalDiscountPercentageLevel1WithTaxAmount": ___, "subtotalDiscountPercentageLevel2": ___, "subtotalDiscountPercentageLevel2WithTaxAmount": ___, "subtotalDiscountPercentageLevel3": ___, "subtotalDiscountPercentageLevel3WithTaxAmount": ___, "subtotalDiscountPercentageLevel4": ___, "subtotalDiscountPercentageLevel4WithTaxAmount": ___, "subtotalDiscountWithTaxAmount": ___, "subtotalDiscountWithTaxAmountFormatted": ___, "subtotalFormatted": ___, "subtotalWithTaxAmount": ___, "subtotalWithTaxAmountFormatted": ___, "taxAmount": ___, "taxAmountFormatted": ___, "taxAmountValue": ___, "total": ___, "totalDiscountAmount": ___, "totalDiscountAmountFormatted": ___, "totalDiscountAmountValue": ___, "totalDiscountPercentageLevel1": ___, "totalDiscountPercentageLevel1WithTaxAmount": ___, "totalDiscountPercentageLevel2": ___, "totalDiscountPercentageLevel2WithTaxAmount": ___, "totalDiscountPercentageLevel3": ___, "totalDiscountPercentageLevel3WithTaxAmount": ___, "totalDiscountPercentageLevel4": ___, "totalDiscountPercentageLevel4WithTaxAmount": ___, "totalDiscountWithTaxAmount": ___, "totalDiscountWithTaxAmountFormatted": ___, "totalDiscountWithTaxAmountValue": ___, "totalFormatted": ___, "totalWithTaxAmount": ___, "totalWithTaxAmountFormatted": ___, "transactionId": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "externalReferenceCode"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {@io.swagger.v3.oas.annotations.tags.Tag(name = "Order")}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.PATCH
	@javax.ws.rs.Path(
		"/orders/by-externalReferenceCode/{externalReferenceCode}"
	)
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Response patchOrderByExternalReferenceCode(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull
			@javax.ws.rs.PathParam("externalReferenceCode")
			String externalReferenceCode,
			Order order)
		throws Exception {

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/{id}'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "id"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {@io.swagger.v3.oas.annotations.tags.Tag(name = "Order")}
	)
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path("/orders/{id}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Response deleteOrder(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull @javax.ws.rs.PathParam("id")
			Long id)
		throws Exception {

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/batch'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "id"
			),
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY,
				name = "callbackURL"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {@io.swagger.v3.oas.annotations.tags.Tag(name = "Order")}
	)
	@javax.ws.rs.Consumes("application/json")
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path("/orders/batch")
	@javax.ws.rs.Produces("application/json")
	@Override
	public Response deleteOrderBatch(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull @javax.ws.rs.PathParam("id")
			Long id,
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.ws.rs.QueryParam("callbackURL")
			String callbackURL,
			Object object)
		throws Exception {

		vulcanBatchEngineImportTaskResource.setContextAcceptLanguage(
			contextAcceptLanguage);
		vulcanBatchEngineImportTaskResource.setContextCompany(contextCompany);
		vulcanBatchEngineImportTaskResource.setContextHttpServletRequest(
			contextHttpServletRequest);
		vulcanBatchEngineImportTaskResource.setContextUriInfo(contextUriInfo);
		vulcanBatchEngineImportTaskResource.setContextUser(contextUser);

		Response.ResponseBuilder responseBuilder = Response.accepted();

		return responseBuilder.entity(
			vulcanBatchEngineImportTaskResource.deleteImportTask(
				Order.class.getName(), callbackURL, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/{id}'  -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "id"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {@io.swagger.v3.oas.annotations.tags.Tag(name = "Order")}
	)
	@javax.ws.rs.GET
	@javax.ws.rs.Path("/orders/{id}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Order getOrder(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull @javax.ws.rs.PathParam("id")
			Long id)
		throws Exception {

		return new Order();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PATCH' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/{id}' -d $'{"accountExternalReferenceCode": ___, "accountId": ___, "advanceStatus": ___, "billingAddress": ___, "billingAddressId": ___, "channelExternalReferenceCode": ___, "channelId": ___, "couponCode": ___, "createDate": ___, "currencyCode": ___, "customFields": ___, "deliveryTermId": ___, "externalReferenceCode": ___, "id": ___, "lastPriceUpdateDate": ___, "modifiedDate": ___, "orderDate": ___, "orderItems": ___, "orderStatus": ___, "orderTypeExternalReferenceCode": ___, "orderTypeId": ___, "paymentMethod": ___, "paymentStatus": ___, "paymentTermId": ___, "printedNote": ___, "purchaseOrderNumber": ___, "requestedDeliveryDate": ___, "shippingAddress": ___, "shippingAddressId": ___, "shippingAmount": ___, "shippingAmountFormatted": ___, "shippingAmountValue": ___, "shippingDiscountAmount": ___, "shippingDiscountAmountFormatted": ___, "shippingDiscountAmountValue": ___, "shippingDiscountPercentageLevel1": ___, "shippingDiscountPercentageLevel1WithTaxAmount": ___, "shippingDiscountPercentageLevel2": ___, "shippingDiscountPercentageLevel2WithTaxAmount": ___, "shippingDiscountPercentageLevel3": ___, "shippingDiscountPercentageLevel3WithTaxAmount": ___, "shippingDiscountPercentageLevel4": ___, "shippingDiscountPercentageLevel4WithTaxAmount": ___, "shippingDiscountWithTaxAmount": ___, "shippingDiscountWithTaxAmountFormatted": ___, "shippingMethod": ___, "shippingOption": ___, "shippingWithTaxAmount": ___, "shippingWithTaxAmountFormatted": ___, "shippingWithTaxAmountValue": ___, "subtotal": ___, "subtotalDiscountAmount": ___, "subtotalDiscountAmountFormatted": ___, "subtotalDiscountPercentageLevel1": ___, "subtotalDiscountPercentageLevel1WithTaxAmount": ___, "subtotalDiscountPercentageLevel2": ___, "subtotalDiscountPercentageLevel2WithTaxAmount": ___, "subtotalDiscountPercentageLevel3": ___, "subtotalDiscountPercentageLevel3WithTaxAmount": ___, "subtotalDiscountPercentageLevel4": ___, "subtotalDiscountPercentageLevel4WithTaxAmount": ___, "subtotalDiscountWithTaxAmount": ___, "subtotalDiscountWithTaxAmountFormatted": ___, "subtotalFormatted": ___, "subtotalWithTaxAmount": ___, "subtotalWithTaxAmountFormatted": ___, "taxAmount": ___, "taxAmountFormatted": ___, "taxAmountValue": ___, "total": ___, "totalDiscountAmount": ___, "totalDiscountAmountFormatted": ___, "totalDiscountAmountValue": ___, "totalDiscountPercentageLevel1": ___, "totalDiscountPercentageLevel1WithTaxAmount": ___, "totalDiscountPercentageLevel2": ___, "totalDiscountPercentageLevel2WithTaxAmount": ___, "totalDiscountPercentageLevel3": ___, "totalDiscountPercentageLevel3WithTaxAmount": ___, "totalDiscountPercentageLevel4": ___, "totalDiscountPercentageLevel4WithTaxAmount": ___, "totalDiscountWithTaxAmount": ___, "totalDiscountWithTaxAmountFormatted": ___, "totalDiscountWithTaxAmountValue": ___, "totalFormatted": ___, "totalWithTaxAmount": ___, "totalWithTaxAmountFormatted": ___, "transactionId": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@io.swagger.v3.oas.annotations.Parameters(
		value = {
			@io.swagger.v3.oas.annotations.Parameter(
				in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
				name = "id"
			)
		}
	)
	@io.swagger.v3.oas.annotations.tags.Tags(
		value = {@io.swagger.v3.oas.annotations.tags.Tag(name = "Order")}
	)
	@javax.ws.rs.Consumes({"application/json", "application/xml"})
	@javax.ws.rs.PATCH
	@javax.ws.rs.Path("/orders/{id}")
	@javax.ws.rs.Produces({"application/json", "application/xml"})
	@Override
	public Response patchOrder(
			@io.swagger.v3.oas.annotations.Parameter(hidden = true)
			@javax.validation.constraints.NotNull @javax.ws.rs.PathParam("id")
			Long id,
			Order order)
		throws Exception {

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	@SuppressWarnings("PMD.UnusedLocalVariable")
	public void create(
			java.util.Collection<Order> orders,
			Map<String, Serializable> parameters)
		throws Exception {

		UnsafeConsumer<Order, Exception> orderUnsafeConsumer = null;

		String createStrategy = (String)parameters.getOrDefault(
			"createStrategy", "INSERT");

		if ("INSERT".equalsIgnoreCase(createStrategy)) {
			orderUnsafeConsumer = order -> postOrder(order);
		}

		if (orderUnsafeConsumer == null) {
			throw new NotSupportedException(
				"Create strategy \"" + createStrategy +
					"\" is not supported for Order");
		}

		if (contextBatchUnsafeConsumer != null) {
			contextBatchUnsafeConsumer.accept(orders, orderUnsafeConsumer);
		}
		else {
			for (Order order : orders) {
				orderUnsafeConsumer.accept(order);
			}
		}
	}

	@Override
	public void delete(
			java.util.Collection<Order> orders,
			Map<String, Serializable> parameters)
		throws Exception {

		for (Order order : orders) {
			deleteOrder(order.getId());
		}
	}

	@Override
	public EntityModel getEntityModel(Map<String, List<String>> multivaluedMap)
		throws Exception {

		return getEntityModel(
			new MultivaluedHashMap<String, Object>(multivaluedMap));
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap)
		throws Exception {

		return null;
	}

	public String getVersion() {
		return "v1.0";
	}

	@Override
	public Page<Order> read(
			Filter filter, Pagination pagination, Sort[] sorts,
			Map<String, Serializable> parameters, String search)
		throws Exception {

		return getOrdersPage(search, filter, pagination, sorts);
	}

	@Override
	public void setLanguageId(String languageId) {
		this.contextAcceptLanguage = new AcceptLanguage() {

			@Override
			public List<Locale> getLocales() {
				return null;
			}

			@Override
			public String getPreferredLanguageId() {
				return languageId;
			}

			@Override
			public Locale getPreferredLocale() {
				return LocaleUtil.fromLanguageId(languageId);
			}

		};
	}

	@Override
	public void update(
			java.util.Collection<Order> orders,
			Map<String, Serializable> parameters)
		throws Exception {

		UnsafeConsumer<Order, Exception> orderUnsafeConsumer = null;

		String updateStrategy = (String)parameters.getOrDefault(
			"updateStrategy", "UPDATE");

		if ("PARTIAL_UPDATE".equalsIgnoreCase(updateStrategy)) {
			orderUnsafeConsumer = order -> patchOrder(
				order.getId() != null ? order.getId() :
					Long.parseLong((String)parameters.get("orderId")),
				order);
		}

		if (orderUnsafeConsumer == null) {
			throw new NotSupportedException(
				"Update strategy \"" + updateStrategy +
					"\" is not supported for Order");
		}

		if (contextBatchUnsafeConsumer != null) {
			contextBatchUnsafeConsumer.accept(orders, orderUnsafeConsumer);
		}
		else {
			for (Order order : orders) {
				orderUnsafeConsumer.accept(order);
			}
		}
	}

	public void setContextAcceptLanguage(AcceptLanguage contextAcceptLanguage) {
		this.contextAcceptLanguage = contextAcceptLanguage;
	}

	public void setContextBatchUnsafeConsumer(
		UnsafeBiConsumer
			<java.util.Collection<Order>, UnsafeConsumer<Order, Exception>,
			 Exception> contextBatchUnsafeConsumer) {

		this.contextBatchUnsafeConsumer = contextBatchUnsafeConsumer;
	}

	public void setContextCompany(
		com.liferay.portal.kernel.model.Company contextCompany) {

		this.contextCompany = contextCompany;
	}

	public void setContextHttpServletRequest(
		HttpServletRequest contextHttpServletRequest) {

		this.contextHttpServletRequest = contextHttpServletRequest;
	}

	public void setContextHttpServletResponse(
		HttpServletResponse contextHttpServletResponse) {

		this.contextHttpServletResponse = contextHttpServletResponse;
	}

	public void setContextUriInfo(UriInfo contextUriInfo) {
		this.contextUriInfo = contextUriInfo;
	}

	public void setContextUser(
		com.liferay.portal.kernel.model.User contextUser) {

		this.contextUser = contextUser;
	}

	public void setExpressionConvert(
		ExpressionConvert<Filter> expressionConvert) {

		this.expressionConvert = expressionConvert;
	}

	public void setFilterParserProvider(
		FilterParserProvider filterParserProvider) {

		this.filterParserProvider = filterParserProvider;
	}

	public void setGroupLocalService(GroupLocalService groupLocalService) {
		this.groupLocalService = groupLocalService;
	}

	public void setResourceActionLocalService(
		ResourceActionLocalService resourceActionLocalService) {

		this.resourceActionLocalService = resourceActionLocalService;
	}

	public void setResourcePermissionLocalService(
		ResourcePermissionLocalService resourcePermissionLocalService) {

		this.resourcePermissionLocalService = resourcePermissionLocalService;
	}

	public void setRoleLocalService(RoleLocalService roleLocalService) {
		this.roleLocalService = roleLocalService;
	}

	public void setVulcanBatchEngineImportTaskResource(
		VulcanBatchEngineImportTaskResource
			vulcanBatchEngineImportTaskResource) {

		this.vulcanBatchEngineImportTaskResource =
			vulcanBatchEngineImportTaskResource;
	}

	@Override
	public Filter toFilter(
		String filterString, Map<String, List<String>> multivaluedMap) {

		try {
			EntityModel entityModel = getEntityModel(multivaluedMap);

			FilterParser filterParser = filterParserProvider.provide(
				entityModel);

			com.liferay.portal.odata.filter.Filter oDataFilter =
				new com.liferay.portal.odata.filter.Filter(
					filterParser.parse(filterString));

			return expressionConvert.convert(
				oDataFilter.getExpression(),
				contextAcceptLanguage.getPreferredLocale(), entityModel);
		}
		catch (Exception exception) {
			_log.error("Invalid filter " + filterString, exception);
		}

		return null;
	}

	protected Map<String, String> addAction(
		String actionName, GroupedModel groupedModel, String methodName) {

		return ActionUtil.addAction(
			actionName, getClass(), groupedModel, methodName,
			contextScopeChecker, contextUriInfo);
	}

	protected Map<String, String> addAction(
		String actionName, Long id, String methodName, Long ownerId,
		String permissionName, Long siteId) {

		return ActionUtil.addAction(
			actionName, getClass(), id, methodName, contextScopeChecker,
			ownerId, permissionName, siteId, contextUriInfo);
	}

	protected Map<String, String> addAction(
		String actionName, Long id, String methodName,
		ModelResourcePermission modelResourcePermission) {

		return ActionUtil.addAction(
			actionName, getClass(), id, methodName, contextScopeChecker,
			modelResourcePermission, contextUriInfo);
	}

	protected Map<String, String> addAction(
		String actionName, String methodName, String permissionName,
		Long siteId) {

		return addAction(
			actionName, siteId, methodName, null, permissionName, siteId);
	}

	protected <T, R> List<R> transform(
		java.util.Collection<T> collection,
		UnsafeFunction<T, R, Exception> unsafeFunction) {

		return TransformUtil.transform(collection, unsafeFunction);
	}

	protected <T, R> R[] transform(
		T[] array, UnsafeFunction<T, R, Exception> unsafeFunction,
		Class<?> clazz) {

		return TransformUtil.transform(array, unsafeFunction, clazz);
	}

	protected <T, R> R[] transformToArray(
		java.util.Collection<T> collection,
		UnsafeFunction<T, R, Exception> unsafeFunction, Class<?> clazz) {

		return TransformUtil.transformToArray(
			collection, unsafeFunction, clazz);
	}

	protected <T, R> List<R> transformToList(
		T[] array, UnsafeFunction<T, R, Exception> unsafeFunction) {

		return TransformUtil.transformToList(array, unsafeFunction);
	}

	protected AcceptLanguage contextAcceptLanguage;
	protected UnsafeBiConsumer
		<java.util.Collection<Order>, UnsafeConsumer<Order, Exception>,
		 Exception> contextBatchUnsafeConsumer;
	protected com.liferay.portal.kernel.model.Company contextCompany;
	protected HttpServletRequest contextHttpServletRequest;
	protected HttpServletResponse contextHttpServletResponse;
	protected Object contextScopeChecker;
	protected UriInfo contextUriInfo;
	protected com.liferay.portal.kernel.model.User contextUser;
	protected ExpressionConvert<Filter> expressionConvert;
	protected FilterParserProvider filterParserProvider;
	protected GroupLocalService groupLocalService;
	protected ResourceActionLocalService resourceActionLocalService;
	protected ResourcePermissionLocalService resourcePermissionLocalService;
	protected RoleLocalService roleLocalService;
	protected VulcanBatchEngineImportTaskResource
		vulcanBatchEngineImportTaskResource;

	private static final com.liferay.portal.kernel.log.Log _log =
		LogFactoryUtil.getLog(BaseOrderResourceImpl.class);

}