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

package com.liferay.headless.commerce.admin.shipment.internal.graphql.mutation.v1_0;

import com.liferay.headless.commerce.admin.shipment.dto.v1_0.Shipment;
import com.liferay.headless.commerce.admin.shipment.dto.v1_0.ShipmentItem;
import com.liferay.headless.commerce.admin.shipment.dto.v1_0.ShippingAddress;
import com.liferay.headless.commerce.admin.shipment.resource.v1_0.ShipmentItemResource;
import com.liferay.headless.commerce.admin.shipment.resource.v1_0.ShipmentResource;
import com.liferay.headless.commerce.admin.shipment.resource.v1_0.ShippingAddressResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResource;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;

import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public class Mutation {

	public static void setShipmentResourceComponentServiceObjects(
		ComponentServiceObjects<ShipmentResource>
			shipmentResourceComponentServiceObjects) {

		_shipmentResourceComponentServiceObjects =
			shipmentResourceComponentServiceObjects;
	}

	public static void setShipmentItemResourceComponentServiceObjects(
		ComponentServiceObjects<ShipmentItemResource>
			shipmentItemResourceComponentServiceObjects) {

		_shipmentItemResourceComponentServiceObjects =
			shipmentItemResourceComponentServiceObjects;
	}

	public static void setShippingAddressResourceComponentServiceObjects(
		ComponentServiceObjects<ShippingAddressResource>
			shippingAddressResourceComponentServiceObjects) {

		_shippingAddressResourceComponentServiceObjects =
			shippingAddressResourceComponentServiceObjects;
	}

	@GraphQLField
	public Shipment createShipment(@GraphQLName("shipment") Shipment shipment)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource -> shipmentResource.postShipment(shipment));
	}

	@GraphQLField
	public Response createShipmentBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource -> shipmentResource.postShipmentBatch(
				callbackURL, object));
	}

	@GraphQLField
	public boolean deleteShipmentByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource ->
				shipmentResource.deleteShipmentByExternalReferenceCode(
					externalReferenceCode));

		return true;
	}

	@GraphQLField
	public Shipment patchShipmentByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("shipment") Shipment shipment)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource ->
				shipmentResource.patchShipmentByExternalReferenceCode(
					externalReferenceCode, shipment));
	}

	@GraphQLField
	public Shipment updateShipmentByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("shipment") Shipment shipment)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource ->
				shipmentResource.putShipmentByExternalReferenceCode(
					externalReferenceCode, shipment));
	}

	@GraphQLField
	public Shipment createShipmentByExternalReferenceCodeStatusDelivered(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource ->
				shipmentResource.
					postShipmentByExternalReferenceCodeStatusDelivered(
						externalReferenceCode));
	}

	@GraphQLField
	public Shipment createShipmentByExternalReferenceCodeStatusFinishProcessing(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource ->
				shipmentResource.
					postShipmentByExternalReferenceCodeStatusFinishProcessing(
						externalReferenceCode));
	}

	@GraphQLField
	public Shipment createShipmentByExternalReferenceCodeStatusShipped(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource ->
				shipmentResource.
					postShipmentByExternalReferenceCodeStatusShipped(
						externalReferenceCode));
	}

	@GraphQLField
	public boolean deleteShipment(@GraphQLName("shipmentId") Long shipmentId)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource -> shipmentResource.deleteShipment(shipmentId));

		return true;
	}

	@GraphQLField
	public Response deleteShipmentBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource -> shipmentResource.deleteShipmentBatch(
				callbackURL, object));
	}

	@GraphQLField
	public Shipment patchShipment(
			@GraphQLName("shipmentId") Long shipmentId,
			@GraphQLName("shipment") Shipment shipment)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource -> shipmentResource.patchShipment(
				shipmentId, shipment));
	}

	@GraphQLField
	public Shipment createShipmentStatusDelivered(
			@GraphQLName("shipmentId") Long shipmentId)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource -> shipmentResource.postShipmentStatusDelivered(
				shipmentId));
	}

	@GraphQLField
	public Shipment createShipmentStatusFinishProcessing(
			@GraphQLName("shipmentId") Long shipmentId)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource ->
				shipmentResource.postShipmentStatusFinishProcessing(
					shipmentId));
	}

	@GraphQLField
	public Shipment createShipmentStatusShipped(
			@GraphQLName("shipmentId") Long shipmentId)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentResource -> shipmentResource.postShipmentStatusShipped(
				shipmentId));
	}

	@GraphQLField
	public boolean deleteShipmentItemByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_shipmentItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentItemResource ->
				shipmentItemResource.deleteShipmentItemByExternalReferenceCode(
					externalReferenceCode));

		return true;
	}

	@GraphQLField
	public ShipmentItem patchShipmentItemByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("shipmentItem") ShipmentItem shipmentItem)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentItemResource ->
				shipmentItemResource.patchShipmentItemByExternalReferenceCode(
					externalReferenceCode, shipmentItem));
	}

	@GraphQLField
	public boolean deleteShipmentItem(
			@GraphQLName("shipmentItemId") Long shipmentItemId)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_shipmentItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentItemResource -> shipmentItemResource.deleteShipmentItem(
				shipmentItemId));

		return true;
	}

	@GraphQLField
	public Response deleteShipmentItemBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentItemResource ->
				shipmentItemResource.deleteShipmentItemBatch(
					callbackURL, object));
	}

	@GraphQLField
	public ShipmentItem patchShipmentItem(
			@GraphQLName("shipmentItemId") Long shipmentItemId,
			@GraphQLName("shipmentItem") ShipmentItem shipmentItem)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentItemResource -> shipmentItemResource.patchShipmentItem(
				shipmentItemId, shipmentItem));
	}

	@GraphQLField
	public ShipmentItem updateShipmentByExternalReferenceCodeItem(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("shipmentItem") ShipmentItem shipmentItem)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentItemResource ->
				shipmentItemResource.putShipmentByExternalReferenceCodeItem(
					externalReferenceCode, shipmentItem));
	}

	@GraphQLField
	public ShipmentItem createShipmentItem(
			@GraphQLName("shipmentId") Long shipmentId,
			@GraphQLName("shipmentItem") ShipmentItem shipmentItem)
		throws Exception {

		return _applyComponentServiceObjects(
			_shipmentItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			shipmentItemResource -> shipmentItemResource.postShipmentItem(
				shipmentId, shipmentItem));
	}

	@GraphQLField
	public ShippingAddress patchShipmentByExternalReferenceCodeShippingAddress(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("shippingAddress") ShippingAddress shippingAddress)
		throws Exception {

		return _applyComponentServiceObjects(
			_shippingAddressResourceComponentServiceObjects,
			this::_populateResourceContext,
			shippingAddressResource ->
				shippingAddressResource.
					patchShipmentByExternalReferenceCodeShippingAddress(
						externalReferenceCode, shippingAddress));
	}

	@GraphQLField
	public ShippingAddress patchShipmentShippingAddress(
			@GraphQLName("shipmentId") Long shipmentId,
			@GraphQLName("shippingAddress") ShippingAddress shippingAddress)
		throws Exception {

		return _applyComponentServiceObjects(
			_shippingAddressResourceComponentServiceObjects,
			this::_populateResourceContext,
			shippingAddressResource ->
				shippingAddressResource.patchShipmentShippingAddress(
					shipmentId, shippingAddress));
	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private <T, E1 extends Throwable, E2 extends Throwable> void
			_applyVoidComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeConsumer<T, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			unsafeFunction.accept(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(ShipmentResource shipmentResource)
		throws Exception {

		shipmentResource.setContextAcceptLanguage(_acceptLanguage);
		shipmentResource.setContextCompany(_company);
		shipmentResource.setContextHttpServletRequest(_httpServletRequest);
		shipmentResource.setContextHttpServletResponse(_httpServletResponse);
		shipmentResource.setContextUriInfo(_uriInfo);
		shipmentResource.setContextUser(_user);
		shipmentResource.setGroupLocalService(_groupLocalService);
		shipmentResource.setRoleLocalService(_roleLocalService);

		shipmentResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			ShipmentItemResource shipmentItemResource)
		throws Exception {

		shipmentItemResource.setContextAcceptLanguage(_acceptLanguage);
		shipmentItemResource.setContextCompany(_company);
		shipmentItemResource.setContextHttpServletRequest(_httpServletRequest);
		shipmentItemResource.setContextHttpServletResponse(
			_httpServletResponse);
		shipmentItemResource.setContextUriInfo(_uriInfo);
		shipmentItemResource.setContextUser(_user);
		shipmentItemResource.setGroupLocalService(_groupLocalService);
		shipmentItemResource.setRoleLocalService(_roleLocalService);

		shipmentItemResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			ShippingAddressResource shippingAddressResource)
		throws Exception {

		shippingAddressResource.setContextAcceptLanguage(_acceptLanguage);
		shippingAddressResource.setContextCompany(_company);
		shippingAddressResource.setContextHttpServletRequest(
			_httpServletRequest);
		shippingAddressResource.setContextHttpServletResponse(
			_httpServletResponse);
		shippingAddressResource.setContextUriInfo(_uriInfo);
		shippingAddressResource.setContextUser(_user);
		shippingAddressResource.setGroupLocalService(_groupLocalService);
		shippingAddressResource.setRoleLocalService(_roleLocalService);
	}

	private static ComponentServiceObjects<ShipmentResource>
		_shipmentResourceComponentServiceObjects;
	private static ComponentServiceObjects<ShipmentItemResource>
		_shipmentItemResourceComponentServiceObjects;
	private static ComponentServiceObjects<ShippingAddressResource>
		_shippingAddressResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private GroupLocalService _groupLocalService;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private RoleLocalService _roleLocalService;
	private BiFunction<Object, String, Sort[]> _sortsBiFunction;
	private UriInfo _uriInfo;
	private com.liferay.portal.kernel.model.User _user;
	private VulcanBatchEngineImportTaskResource
		_vulcanBatchEngineImportTaskResource;

}