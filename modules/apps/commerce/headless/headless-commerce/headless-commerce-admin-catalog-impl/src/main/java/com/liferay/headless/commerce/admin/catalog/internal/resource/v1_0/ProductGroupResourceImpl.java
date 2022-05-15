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

package com.liferay.headless.commerce.admin.catalog.internal.resource.v1_0;

import com.liferay.commerce.pricing.exception.NoSuchPricingClassException;
import com.liferay.commerce.pricing.model.CommercePricingClass;
import com.liferay.commerce.pricing.model.CommercePricingClassCPDefinitionRel;
import com.liferay.commerce.pricing.service.CommercePricingClassCPDefinitionRelService;
import com.liferay.commerce.pricing.service.CommercePricingClassService;
import com.liferay.commerce.product.exception.NoSuchCProductException;
import com.liferay.commerce.product.model.CProduct;
import com.liferay.commerce.product.service.CProductLocalService;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductGroup;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductGroupProduct;
import com.liferay.headless.commerce.admin.catalog.internal.dto.v1_0.converter.ProductGroupDTOConverter;
import com.liferay.headless.commerce.admin.catalog.internal.odata.entity.v1_0.ProductGroupEntityModel;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductGroupProductUtil;
import com.liferay.headless.commerce.admin.catalog.resource.v1_0.ProductGroupResource;
import com.liferay.headless.commerce.core.util.ExpandoUtil;
import com.liferay.headless.commerce.core.util.LanguageUtils;
import com.liferay.headless.commerce.core.util.ServiceContextHelper;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.portal.kernel.change.tracking.CTAware;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.SearchUtil;

import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Riccardo Alberti
 */
@Component(
	enabled = false,
	properties = "OSGI-INF/liferay/rest/v1_0/product-group.properties",
	scope = ServiceScope.PROTOTYPE, service = ProductGroupResource.class
)
@CTAware
public class ProductGroupResourceImpl
	extends BaseProductGroupResourceImpl implements EntityModelResource {

	@Override
	public void deleteProductGroup(Long id) throws Exception {
		_commercePricingClassService.deleteCommercePricingClass(id);
	}

	@Override
	public void deleteProductGroupByExternalReferenceCode(
			String externalReferenceCode)
		throws Exception {

		CommercePricingClass commercePricingClass =
			_commercePricingClassService.fetchByExternalReferenceCode(
				externalReferenceCode, contextCompany.getCompanyId());

		if (commercePricingClass == null) {
			throw new NoSuchPricingClassException(
				"Unable to find product group with external reference code " +
					externalReferenceCode);
		}

		_commercePricingClassService.deleteCommercePricingClass(
			commercePricingClass.getCommercePricingClassId());
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap)
		throws Exception {

		return _entityModel;
	}

	@Override
	public ProductGroup getProductGroup(Long id) throws Exception {
		return _toProductGroup(GetterUtil.getLong(id));
	}

	@Override
	public ProductGroup getProductGroupByExternalReferenceCode(
			String externalReferenceCode)
		throws Exception {

		CommercePricingClass commercePricingClass =
			_commercePricingClassService.fetchByExternalReferenceCode(
				externalReferenceCode, contextCompany.getCompanyId());

		if (commercePricingClass == null) {
			throw new NoSuchPricingClassException(
				"Unable to find product group with external reference code " +
					externalReferenceCode);
		}

		return _toProductGroup(
			commercePricingClass.getCommercePricingClassId());
	}

	@Override
	public Page<ProductGroup> getProductGroupsPage(
			String search, Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception {

		return SearchUtil.search(
			null, booleanQuery -> booleanQuery.getPreBooleanFilter(), filter,
			CommercePricingClass.class.getName(), search, pagination,
			queryConfig -> queryConfig.setSelectedFieldNames(
				Field.ENTRY_CLASS_PK),
			new UnsafeConsumer() {

				public void accept(Object object) throws Exception {
					SearchContext searchContext = (SearchContext)object;

					searchContext.setCompanyId(contextCompany.getCompanyId());
				}

			},
			sorts,
			document -> _toProductGroup(
				GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK))));
	}

	@Override
	public Response patchProductGroup(Long id, ProductGroup productGroup)
		throws Exception {

		_updateProductGroup(
			_commercePricingClassService.getCommercePricingClass(id),
			productGroup);

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	public Response patchProductGroupByExternalReferenceCode(
			String externalReferenceCode, ProductGroup productGroup)
		throws Exception {

		CommercePricingClass commercePricingClass =
			_commercePricingClassService.fetchByExternalReferenceCode(
				externalReferenceCode, contextCompany.getCompanyId());

		if (commercePricingClass == null) {
			throw new NoSuchPricingClassException(
				"Unable to find product group with external reference code " +
					externalReferenceCode);
		}

		_updateProductGroup(commercePricingClass, productGroup);

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	public ProductGroup postProductGroup(ProductGroup productGroup)
		throws Exception {

		CommercePricingClass commercePricingClass = _addOrUpdateProductGroup(
			productGroup);

		return _toProductGroup(
			commercePricingClass.getCommercePricingClassId());
	}

	private CommercePricingClass _addOrUpdateProductGroup(
			ProductGroup productGroup)
		throws Exception {

		CommercePricingClass commercePricingClass =
			_commercePricingClassService.addOrUpdateCommercePricingClass(
				productGroup.getExternalReferenceCode(), 0L,
				LanguageUtils.getLocalizedMap(productGroup.getTitle()),
				LanguageUtils.getLocalizedMap(productGroup.getDescription()),
				_serviceContextHelper.getServiceContext());

		// Update nested resources

		return _updateNestedResources(productGroup, commercePricingClass);
	}

	private ProductGroup _toProductGroup(Long commercePricingClassId)
		throws Exception {

		return _productGroupDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				commercePricingClassId,
				contextAcceptLanguage.getPreferredLocale()));
	}

	private CommercePricingClass _updateNestedResources(
			ProductGroup productGroup,
			CommercePricingClass commercePricingClass)
		throws Exception {

		// Product group products

		ProductGroupProduct[] productGroupProducts = productGroup.getProducts();

		if (productGroupProducts != null) {
			for (ProductGroupProduct productGroupProduct :
					productGroupProducts) {

				CProduct cProduct = _cProductLocalService.fetchCProduct(
					productGroupProduct.getProductId());

				if (cProduct == null) {
					cProduct =
						_cProductLocalService.
							fetchCProductByExternalReferenceCode(
								contextCompany.getCompanyId(),
								productGroupProduct.
									getProductExternalReferenceCode());
				}

				if (cProduct == null) {
					String productExternalReferenceCode =
						productGroupProduct.getProductExternalReferenceCode();

					throw new NoSuchCProductException(
						"Unable to find product with external reference code " +
							productExternalReferenceCode);
				}

				CommercePricingClassCPDefinitionRel
					commercePricingClassCPDefinitionRel =
						_commercePricingClassCPDefinitionRelService.
							fetchCommercePricingClassCPDefinitionRel(
								commercePricingClass.
									getCommercePricingClassId(),
								cProduct.getPublishedCPDefinitionId());

				if (commercePricingClassCPDefinitionRel != null) {
					continue;
				}

				ProductGroupProductUtil.addCommercePricingClassCPDefinitionRel(
					_cProductLocalService,
					_commercePricingClassCPDefinitionRelService,
					productGroupProduct, commercePricingClass,
					_serviceContextHelper);
			}
		}

		return commercePricingClass;
	}

	private CommercePricingClass _updateProductGroup(
			CommercePricingClass commercePricingClass,
			ProductGroup productGroup)
		throws Exception {

		commercePricingClass =
			_commercePricingClassService.updateCommercePricingClass(
				commercePricingClass.getCommercePricingClassId(),
				LanguageUtils.getLocalizedMap(productGroup.getTitle()),
				LanguageUtils.getLocalizedMap(productGroup.getDescription()),
				_serviceContextHelper.getServiceContext());

		// Expando

		Map<String, ?> customFields = productGroup.getCustomFields();

		if ((customFields != null) && !customFields.isEmpty()) {
			ExpandoUtil.updateExpando(
				contextCompany.getCompanyId(), CommercePricingClass.class,
				commercePricingClass.getPrimaryKey(), customFields);
		}

		// Update nested resources

		return _updateNestedResources(productGroup, commercePricingClass);
	}

	private static final EntityModel _entityModel =
		new ProductGroupEntityModel();

	@Reference
	private CommercePricingClassCPDefinitionRelService
		_commercePricingClassCPDefinitionRelService;

	@Reference
	private CommercePricingClassService _commercePricingClassService;

	@Reference
	private CProductLocalService _cProductLocalService;

	@Reference
	private ProductGroupDTOConverter _productGroupDTOConverter;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

}