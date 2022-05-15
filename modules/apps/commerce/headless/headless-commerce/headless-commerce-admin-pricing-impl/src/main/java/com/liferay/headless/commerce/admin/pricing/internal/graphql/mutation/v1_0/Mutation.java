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

package com.liferay.headless.commerce.admin.pricing.internal.graphql.mutation.v1_0;

import com.liferay.headless.commerce.admin.pricing.dto.v1_0.Discount;
import com.liferay.headless.commerce.admin.pricing.dto.v1_0.DiscountAccountGroup;
import com.liferay.headless.commerce.admin.pricing.dto.v1_0.DiscountCategory;
import com.liferay.headless.commerce.admin.pricing.dto.v1_0.DiscountProduct;
import com.liferay.headless.commerce.admin.pricing.dto.v1_0.DiscountRule;
import com.liferay.headless.commerce.admin.pricing.dto.v1_0.PriceEntry;
import com.liferay.headless.commerce.admin.pricing.dto.v1_0.PriceList;
import com.liferay.headless.commerce.admin.pricing.dto.v1_0.PriceListAccountGroup;
import com.liferay.headless.commerce.admin.pricing.dto.v1_0.TierPrice;
import com.liferay.headless.commerce.admin.pricing.resource.v1_0.DiscountAccountGroupResource;
import com.liferay.headless.commerce.admin.pricing.resource.v1_0.DiscountCategoryResource;
import com.liferay.headless.commerce.admin.pricing.resource.v1_0.DiscountProductResource;
import com.liferay.headless.commerce.admin.pricing.resource.v1_0.DiscountResource;
import com.liferay.headless.commerce.admin.pricing.resource.v1_0.DiscountRuleResource;
import com.liferay.headless.commerce.admin.pricing.resource.v1_0.PriceEntryResource;
import com.liferay.headless.commerce.admin.pricing.resource.v1_0.PriceListAccountGroupResource;
import com.liferay.headless.commerce.admin.pricing.resource.v1_0.PriceListResource;
import com.liferay.headless.commerce.admin.pricing.resource.v1_0.TierPriceResource;
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
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
public class Mutation {

	public static void setDiscountResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountResource>
			discountResourceComponentServiceObjects) {

		_discountResourceComponentServiceObjects =
			discountResourceComponentServiceObjects;
	}

	public static void setDiscountAccountGroupResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountAccountGroupResource>
			discountAccountGroupResourceComponentServiceObjects) {

		_discountAccountGroupResourceComponentServiceObjects =
			discountAccountGroupResourceComponentServiceObjects;
	}

	public static void setDiscountCategoryResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountCategoryResource>
			discountCategoryResourceComponentServiceObjects) {

		_discountCategoryResourceComponentServiceObjects =
			discountCategoryResourceComponentServiceObjects;
	}

	public static void setDiscountProductResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountProductResource>
			discountProductResourceComponentServiceObjects) {

		_discountProductResourceComponentServiceObjects =
			discountProductResourceComponentServiceObjects;
	}

	public static void setDiscountRuleResourceComponentServiceObjects(
		ComponentServiceObjects<DiscountRuleResource>
			discountRuleResourceComponentServiceObjects) {

		_discountRuleResourceComponentServiceObjects =
			discountRuleResourceComponentServiceObjects;
	}

	public static void setPriceEntryResourceComponentServiceObjects(
		ComponentServiceObjects<PriceEntryResource>
			priceEntryResourceComponentServiceObjects) {

		_priceEntryResourceComponentServiceObjects =
			priceEntryResourceComponentServiceObjects;
	}

	public static void setPriceListResourceComponentServiceObjects(
		ComponentServiceObjects<PriceListResource>
			priceListResourceComponentServiceObjects) {

		_priceListResourceComponentServiceObjects =
			priceListResourceComponentServiceObjects;
	}

	public static void setPriceListAccountGroupResourceComponentServiceObjects(
		ComponentServiceObjects<PriceListAccountGroupResource>
			priceListAccountGroupResourceComponentServiceObjects) {

		_priceListAccountGroupResourceComponentServiceObjects =
			priceListAccountGroupResourceComponentServiceObjects;
	}

	public static void setTierPriceResourceComponentServiceObjects(
		ComponentServiceObjects<TierPriceResource>
			tierPriceResourceComponentServiceObjects) {

		_tierPriceResourceComponentServiceObjects =
			tierPriceResourceComponentServiceObjects;
	}

	@GraphQLField
	public Discount createDiscount(@GraphQLName("discount") Discount discount)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountResource -> discountResource.postDiscount(discount));
	}

	@GraphQLField
	public Response createDiscountBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountResource -> discountResource.postDiscountBatch(
				callbackURL, object));
	}

	@GraphQLField
	public Response deleteDiscountByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountResource ->
				discountResource.deleteDiscountByExternalReferenceCode(
					externalReferenceCode));
	}

	@GraphQLField
	public Response patchDiscountByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("discount") Discount discount)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountResource ->
				discountResource.patchDiscountByExternalReferenceCode(
					externalReferenceCode, discount));
	}

	@GraphQLField
	public Response deleteDiscount(@GraphQLName("id") Long id)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountResource -> discountResource.deleteDiscount(id));
	}

	@GraphQLField
	public Response deleteDiscountBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountResource -> discountResource.deleteDiscountBatch(
				id, callbackURL, object));
	}

	@GraphQLField
	public Response patchDiscount(
			@GraphQLName("id") Long id,
			@GraphQLName("discount") Discount discount)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountResource -> discountResource.patchDiscount(id, discount));
	}

	@GraphQLField
	public Response deleteDiscountAccountGroup(@GraphQLName("id") Long id)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountAccountGroupResource ->
				discountAccountGroupResource.deleteDiscountAccountGroup(id));
	}

	@GraphQLField
	public Response deleteDiscountAccountGroupBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountAccountGroupResource ->
				discountAccountGroupResource.deleteDiscountAccountGroupBatch(
					id, callbackURL, object));
	}

	@GraphQLField
	public DiscountAccountGroup
			createDiscountByExternalReferenceCodeDiscountAccountGroup(
				@GraphQLName("externalReferenceCode") String
					externalReferenceCode,
				@GraphQLName("discountAccountGroup") DiscountAccountGroup
					discountAccountGroup)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountAccountGroupResource ->
				discountAccountGroupResource.
					postDiscountByExternalReferenceCodeDiscountAccountGroup(
						externalReferenceCode, discountAccountGroup));
	}

	@GraphQLField
	public DiscountAccountGroup createDiscountIdDiscountAccountGroup(
			@GraphQLName("id") Long id,
			@GraphQLName("discountAccountGroup") DiscountAccountGroup
				discountAccountGroup)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountAccountGroupResource ->
				discountAccountGroupResource.postDiscountIdDiscountAccountGroup(
					id, discountAccountGroup));
	}

	@GraphQLField
	public Response createDiscountIdDiscountAccountGroupBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountAccountGroupResource ->
				discountAccountGroupResource.
					postDiscountIdDiscountAccountGroupBatch(
						id, callbackURL, object));
	}

	@GraphQLField
	public Response deleteDiscountCategory(@GraphQLName("id") Long id)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountCategoryResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountCategoryResource ->
				discountCategoryResource.deleteDiscountCategory(id));
	}

	@GraphQLField
	public Response deleteDiscountCategoryBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountCategoryResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountCategoryResource ->
				discountCategoryResource.deleteDiscountCategoryBatch(
					id, callbackURL, object));
	}

	@GraphQLField
	public DiscountCategory
			createDiscountByExternalReferenceCodeDiscountCategory(
				@GraphQLName("externalReferenceCode") String
					externalReferenceCode,
				@GraphQLName("discountCategory") DiscountCategory
					discountCategory)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountCategoryResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountCategoryResource ->
				discountCategoryResource.
					postDiscountByExternalReferenceCodeDiscountCategory(
						externalReferenceCode, discountCategory));
	}

	@GraphQLField
	public DiscountCategory createDiscountIdDiscountCategory(
			@GraphQLName("id") Long id,
			@GraphQLName("discountCategory") DiscountCategory discountCategory)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountCategoryResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountCategoryResource ->
				discountCategoryResource.postDiscountIdDiscountCategory(
					id, discountCategory));
	}

	@GraphQLField
	public Response createDiscountIdDiscountCategoryBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountCategoryResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountCategoryResource ->
				discountCategoryResource.postDiscountIdDiscountCategoryBatch(
					id, callbackURL, object));
	}

	@GraphQLField
	public Response deleteDiscountProduct(@GraphQLName("id") Long id)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountProductResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountProductResource ->
				discountProductResource.deleteDiscountProduct(id));
	}

	@GraphQLField
	public Response deleteDiscountProductBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountProductResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountProductResource ->
				discountProductResource.deleteDiscountProductBatch(
					id, callbackURL, object));
	}

	@GraphQLField
	public DiscountProduct createDiscountByExternalReferenceCodeDiscountProduct(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("discountProduct") DiscountProduct discountProduct)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountProductResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountProductResource ->
				discountProductResource.
					postDiscountByExternalReferenceCodeDiscountProduct(
						externalReferenceCode, discountProduct));
	}

	@GraphQLField
	public DiscountProduct createDiscountIdDiscountProduct(
			@GraphQLName("id") Long id,
			@GraphQLName("discountProduct") DiscountProduct discountProduct)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountProductResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountProductResource ->
				discountProductResource.postDiscountIdDiscountProduct(
					id, discountProduct));
	}

	@GraphQLField
	public Response createDiscountIdDiscountProductBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountProductResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountProductResource ->
				discountProductResource.postDiscountIdDiscountProductBatch(
					id, callbackURL, object));
	}

	@GraphQLField
	public DiscountRule createDiscountByExternalReferenceCodeDiscountRule(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("discountRule") DiscountRule discountRule)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountRuleResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountRuleResource ->
				discountRuleResource.
					postDiscountByExternalReferenceCodeDiscountRule(
						externalReferenceCode, discountRule));
	}

	@GraphQLField
	public Response deleteDiscountRule(@GraphQLName("id") Long id)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountRuleResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountRuleResource -> discountRuleResource.deleteDiscountRule(
				id));
	}

	@GraphQLField
	public Response deleteDiscountRuleBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountRuleResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountRuleResource ->
				discountRuleResource.deleteDiscountRuleBatch(
					id, callbackURL, object));
	}

	@GraphQLField
	public Response patchDiscountRule(
			@GraphQLName("id") Long id,
			@GraphQLName("discountRule") DiscountRule discountRule)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountRuleResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountRuleResource -> discountRuleResource.patchDiscountRule(
				id, discountRule));
	}

	@GraphQLField
	public DiscountRule createDiscountIdDiscountRule(
			@GraphQLName("id") Long id,
			@GraphQLName("discountRule") DiscountRule discountRule)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountRuleResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountRuleResource ->
				discountRuleResource.postDiscountIdDiscountRule(
					id, discountRule));
	}

	@GraphQLField
	public Response createDiscountIdDiscountRuleBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_discountRuleResourceComponentServiceObjects,
			this::_populateResourceContext,
			discountRuleResource ->
				discountRuleResource.postDiscountIdDiscountRuleBatch(
					id, callbackURL, object));
	}

	@GraphQLField
	public Response deletePriceEntryByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceEntryResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceEntryResource ->
				priceEntryResource.deletePriceEntryByExternalReferenceCode(
					externalReferenceCode));
	}

	@GraphQLField
	public Response patchPriceEntryByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("priceEntry") PriceEntry priceEntry)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceEntryResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceEntryResource ->
				priceEntryResource.patchPriceEntryByExternalReferenceCode(
					externalReferenceCode, priceEntry));
	}

	@GraphQLField
	public Response deletePriceEntry(@GraphQLName("id") Long id)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceEntryResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceEntryResource -> priceEntryResource.deletePriceEntry(id));
	}

	@GraphQLField
	public Response deletePriceEntryBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceEntryResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceEntryResource -> priceEntryResource.deletePriceEntryBatch(
				id, callbackURL, object));
	}

	@GraphQLField
	public Response patchPriceEntry(
			@GraphQLName("id") Long id,
			@GraphQLName("priceEntry") PriceEntry priceEntry)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceEntryResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceEntryResource -> priceEntryResource.patchPriceEntry(
				id, priceEntry));
	}

	@GraphQLField
	public PriceEntry createPriceListByExternalReferenceCodePriceEntry(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("priceEntry") PriceEntry priceEntry)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceEntryResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceEntryResource ->
				priceEntryResource.
					postPriceListByExternalReferenceCodePriceEntry(
						externalReferenceCode, priceEntry));
	}

	@GraphQLField
	public PriceEntry createPriceListIdPriceEntry(
			@GraphQLName("id") Long id,
			@GraphQLName("priceEntry") PriceEntry priceEntry)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceEntryResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceEntryResource -> priceEntryResource.postPriceListIdPriceEntry(
				id, priceEntry));
	}

	@GraphQLField
	public Response createPriceListIdPriceEntryBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceEntryResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceEntryResource ->
				priceEntryResource.postPriceListIdPriceEntryBatch(
					id, callbackURL, object));
	}

	@GraphQLField
	public PriceList createPriceList(
			@GraphQLName("priceList") PriceList priceList)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListResource -> priceListResource.postPriceList(priceList));
	}

	@GraphQLField
	public Response createPriceListBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListResource -> priceListResource.postPriceListBatch(
				callbackURL, object));
	}

	@GraphQLField
	public Response deletePriceListByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListResource ->
				priceListResource.deletePriceListByExternalReferenceCode(
					externalReferenceCode));
	}

	@GraphQLField
	public Response patchPriceListByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("priceList") PriceList priceList)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListResource ->
				priceListResource.patchPriceListByExternalReferenceCode(
					externalReferenceCode, priceList));
	}

	@GraphQLField
	public Response deletePriceList(@GraphQLName("id") Long id)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListResource -> priceListResource.deletePriceList(id));
	}

	@GraphQLField
	public Response deletePriceListBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListResource -> priceListResource.deletePriceListBatch(
				id, callbackURL, object));
	}

	@GraphQLField
	public Response patchPriceList(
			@GraphQLName("id") Long id,
			@GraphQLName("priceList") PriceList priceList)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListResource -> priceListResource.patchPriceList(
				id, priceList));
	}

	@GraphQLField
	public Response deletePriceListAccountGroup(@GraphQLName("id") Long id)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListAccountGroupResource ->
				priceListAccountGroupResource.deletePriceListAccountGroup(id));
	}

	@GraphQLField
	public Response deletePriceListAccountGroupBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListAccountGroupResource ->
				priceListAccountGroupResource.deletePriceListAccountGroupBatch(
					id, callbackURL, object));
	}

	@GraphQLField
	public PriceListAccountGroup
			createPriceListByExternalReferenceCodePriceListAccountGroup(
				@GraphQLName("externalReferenceCode") String
					externalReferenceCode,
				@GraphQLName("priceListAccountGroup") PriceListAccountGroup
					priceListAccountGroup)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListAccountGroupResource ->
				priceListAccountGroupResource.
					postPriceListByExternalReferenceCodePriceListAccountGroup(
						externalReferenceCode, priceListAccountGroup));
	}

	@GraphQLField
	public PriceListAccountGroup createPriceListIdPriceListAccountGroup(
			@GraphQLName("id") Long id,
			@GraphQLName("priceListAccountGroup") PriceListAccountGroup
				priceListAccountGroup)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListAccountGroupResource ->
				priceListAccountGroupResource.
					postPriceListIdPriceListAccountGroup(
						id, priceListAccountGroup));
	}

	@GraphQLField
	public Response createPriceListIdPriceListAccountGroupBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_priceListAccountGroupResourceComponentServiceObjects,
			this::_populateResourceContext,
			priceListAccountGroupResource ->
				priceListAccountGroupResource.
					postPriceListIdPriceListAccountGroupBatch(
						id, callbackURL, object));
	}

	@GraphQLField
	public TierPrice createPriceEntryByExternalReferenceCodeTierPrice(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("tierPrice") TierPrice tierPrice)
		throws Exception {

		return _applyComponentServiceObjects(
			_tierPriceResourceComponentServiceObjects,
			this::_populateResourceContext,
			tierPriceResource ->
				tierPriceResource.
					postPriceEntryByExternalReferenceCodeTierPrice(
						externalReferenceCode, tierPrice));
	}

	@GraphQLField
	public TierPrice createPriceEntryIdTierPrice(
			@GraphQLName("id") Long id,
			@GraphQLName("tierPrice") TierPrice tierPrice)
		throws Exception {

		return _applyComponentServiceObjects(
			_tierPriceResourceComponentServiceObjects,
			this::_populateResourceContext,
			tierPriceResource -> tierPriceResource.postPriceEntryIdTierPrice(
				id, tierPrice));
	}

	@GraphQLField
	public Response createPriceEntryIdTierPriceBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_tierPriceResourceComponentServiceObjects,
			this::_populateResourceContext,
			tierPriceResource ->
				tierPriceResource.postPriceEntryIdTierPriceBatch(
					id, callbackURL, object));
	}

	@GraphQLField
	public Response deleteTierPriceByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_tierPriceResourceComponentServiceObjects,
			this::_populateResourceContext,
			tierPriceResource ->
				tierPriceResource.deleteTierPriceByExternalReferenceCode(
					externalReferenceCode));
	}

	@GraphQLField
	public Response patchTierPriceByExternalReferenceCode(
			@GraphQLName("externalReferenceCode") String externalReferenceCode,
			@GraphQLName("tierPrice") TierPrice tierPrice)
		throws Exception {

		return _applyComponentServiceObjects(
			_tierPriceResourceComponentServiceObjects,
			this::_populateResourceContext,
			tierPriceResource ->
				tierPriceResource.patchTierPriceByExternalReferenceCode(
					externalReferenceCode, tierPrice));
	}

	@GraphQLField
	public Response deleteTierPrice(@GraphQLName("id") Long id)
		throws Exception {

		return _applyComponentServiceObjects(
			_tierPriceResourceComponentServiceObjects,
			this::_populateResourceContext,
			tierPriceResource -> tierPriceResource.deleteTierPrice(id));
	}

	@GraphQLField
	public Response deleteTierPriceBatch(
			@GraphQLName("id") Long id,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_tierPriceResourceComponentServiceObjects,
			this::_populateResourceContext,
			tierPriceResource -> tierPriceResource.deleteTierPriceBatch(
				id, callbackURL, object));
	}

	@GraphQLField
	public Response patchTierPrice(
			@GraphQLName("id") Long id,
			@GraphQLName("tierPrice") TierPrice tierPrice)
		throws Exception {

		return _applyComponentServiceObjects(
			_tierPriceResourceComponentServiceObjects,
			this::_populateResourceContext,
			tierPriceResource -> tierPriceResource.patchTierPrice(
				id, tierPrice));
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

	private void _populateResourceContext(DiscountResource discountResource)
		throws Exception {

		discountResource.setContextAcceptLanguage(_acceptLanguage);
		discountResource.setContextCompany(_company);
		discountResource.setContextHttpServletRequest(_httpServletRequest);
		discountResource.setContextHttpServletResponse(_httpServletResponse);
		discountResource.setContextUriInfo(_uriInfo);
		discountResource.setContextUser(_user);
		discountResource.setGroupLocalService(_groupLocalService);
		discountResource.setRoleLocalService(_roleLocalService);

		discountResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			DiscountAccountGroupResource discountAccountGroupResource)
		throws Exception {

		discountAccountGroupResource.setContextAcceptLanguage(_acceptLanguage);
		discountAccountGroupResource.setContextCompany(_company);
		discountAccountGroupResource.setContextHttpServletRequest(
			_httpServletRequest);
		discountAccountGroupResource.setContextHttpServletResponse(
			_httpServletResponse);
		discountAccountGroupResource.setContextUriInfo(_uriInfo);
		discountAccountGroupResource.setContextUser(_user);
		discountAccountGroupResource.setGroupLocalService(_groupLocalService);
		discountAccountGroupResource.setRoleLocalService(_roleLocalService);

		discountAccountGroupResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			DiscountCategoryResource discountCategoryResource)
		throws Exception {

		discountCategoryResource.setContextAcceptLanguage(_acceptLanguage);
		discountCategoryResource.setContextCompany(_company);
		discountCategoryResource.setContextHttpServletRequest(
			_httpServletRequest);
		discountCategoryResource.setContextHttpServletResponse(
			_httpServletResponse);
		discountCategoryResource.setContextUriInfo(_uriInfo);
		discountCategoryResource.setContextUser(_user);
		discountCategoryResource.setGroupLocalService(_groupLocalService);
		discountCategoryResource.setRoleLocalService(_roleLocalService);

		discountCategoryResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			DiscountProductResource discountProductResource)
		throws Exception {

		discountProductResource.setContextAcceptLanguage(_acceptLanguage);
		discountProductResource.setContextCompany(_company);
		discountProductResource.setContextHttpServletRequest(
			_httpServletRequest);
		discountProductResource.setContextHttpServletResponse(
			_httpServletResponse);
		discountProductResource.setContextUriInfo(_uriInfo);
		discountProductResource.setContextUser(_user);
		discountProductResource.setGroupLocalService(_groupLocalService);
		discountProductResource.setRoleLocalService(_roleLocalService);

		discountProductResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			DiscountRuleResource discountRuleResource)
		throws Exception {

		discountRuleResource.setContextAcceptLanguage(_acceptLanguage);
		discountRuleResource.setContextCompany(_company);
		discountRuleResource.setContextHttpServletRequest(_httpServletRequest);
		discountRuleResource.setContextHttpServletResponse(
			_httpServletResponse);
		discountRuleResource.setContextUriInfo(_uriInfo);
		discountRuleResource.setContextUser(_user);
		discountRuleResource.setGroupLocalService(_groupLocalService);
		discountRuleResource.setRoleLocalService(_roleLocalService);

		discountRuleResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(PriceEntryResource priceEntryResource)
		throws Exception {

		priceEntryResource.setContextAcceptLanguage(_acceptLanguage);
		priceEntryResource.setContextCompany(_company);
		priceEntryResource.setContextHttpServletRequest(_httpServletRequest);
		priceEntryResource.setContextHttpServletResponse(_httpServletResponse);
		priceEntryResource.setContextUriInfo(_uriInfo);
		priceEntryResource.setContextUser(_user);
		priceEntryResource.setGroupLocalService(_groupLocalService);
		priceEntryResource.setRoleLocalService(_roleLocalService);

		priceEntryResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(PriceListResource priceListResource)
		throws Exception {

		priceListResource.setContextAcceptLanguage(_acceptLanguage);
		priceListResource.setContextCompany(_company);
		priceListResource.setContextHttpServletRequest(_httpServletRequest);
		priceListResource.setContextHttpServletResponse(_httpServletResponse);
		priceListResource.setContextUriInfo(_uriInfo);
		priceListResource.setContextUser(_user);
		priceListResource.setGroupLocalService(_groupLocalService);
		priceListResource.setRoleLocalService(_roleLocalService);

		priceListResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			PriceListAccountGroupResource priceListAccountGroupResource)
		throws Exception {

		priceListAccountGroupResource.setContextAcceptLanguage(_acceptLanguage);
		priceListAccountGroupResource.setContextCompany(_company);
		priceListAccountGroupResource.setContextHttpServletRequest(
			_httpServletRequest);
		priceListAccountGroupResource.setContextHttpServletResponse(
			_httpServletResponse);
		priceListAccountGroupResource.setContextUriInfo(_uriInfo);
		priceListAccountGroupResource.setContextUser(_user);
		priceListAccountGroupResource.setGroupLocalService(_groupLocalService);
		priceListAccountGroupResource.setRoleLocalService(_roleLocalService);

		priceListAccountGroupResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(TierPriceResource tierPriceResource)
		throws Exception {

		tierPriceResource.setContextAcceptLanguage(_acceptLanguage);
		tierPriceResource.setContextCompany(_company);
		tierPriceResource.setContextHttpServletRequest(_httpServletRequest);
		tierPriceResource.setContextHttpServletResponse(_httpServletResponse);
		tierPriceResource.setContextUriInfo(_uriInfo);
		tierPriceResource.setContextUser(_user);
		tierPriceResource.setGroupLocalService(_groupLocalService);
		tierPriceResource.setRoleLocalService(_roleLocalService);

		tierPriceResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private static ComponentServiceObjects<DiscountResource>
		_discountResourceComponentServiceObjects;
	private static ComponentServiceObjects<DiscountAccountGroupResource>
		_discountAccountGroupResourceComponentServiceObjects;
	private static ComponentServiceObjects<DiscountCategoryResource>
		_discountCategoryResourceComponentServiceObjects;
	private static ComponentServiceObjects<DiscountProductResource>
		_discountProductResourceComponentServiceObjects;
	private static ComponentServiceObjects<DiscountRuleResource>
		_discountRuleResourceComponentServiceObjects;
	private static ComponentServiceObjects<PriceEntryResource>
		_priceEntryResourceComponentServiceObjects;
	private static ComponentServiceObjects<PriceListResource>
		_priceListResourceComponentServiceObjects;
	private static ComponentServiceObjects<PriceListAccountGroupResource>
		_priceListAccountGroupResourceComponentServiceObjects;
	private static ComponentServiceObjects<TierPriceResource>
		_tierPriceResourceComponentServiceObjects;

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