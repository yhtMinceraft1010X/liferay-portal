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

import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetTagService;
import com.liferay.commerce.account.model.CommerceAccountGroup;
import com.liferay.commerce.account.service.CommerceAccountGroupRelService;
import com.liferay.commerce.account.service.CommerceAccountGroupService;
import com.liferay.commerce.product.constants.CPAttachmentFileEntryConstants;
import com.liferay.commerce.product.exception.NoSuchCPDefinitionException;
import com.liferay.commerce.product.exception.NoSuchCatalogException;
import com.liferay.commerce.product.model.CPAttachmentFileEntry;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CPAttachmentFileEntryService;
import com.liferay.commerce.product.service.CPDefinitionLinkService;
import com.liferay.commerce.product.service.CPDefinitionOptionRelService;
import com.liferay.commerce.product.service.CPDefinitionOptionValueRelService;
import com.liferay.commerce.product.service.CPDefinitionService;
import com.liferay.commerce.product.service.CPDefinitionSpecificationOptionValueService;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.commerce.product.service.CPOptionService;
import com.liferay.commerce.product.service.CPSpecificationOptionService;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.service.CommerceChannelRelService;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.product.type.CPType;
import com.liferay.commerce.product.type.CPTypeServicesTracker;
import com.liferay.commerce.service.CPDefinitionInventoryService;
import com.liferay.commerce.shop.by.diagram.constants.CSDiagramCPTypeConstants;
import com.liferay.commerce.shop.by.diagram.service.CSDiagramEntryService;
import com.liferay.commerce.shop.by.diagram.service.CSDiagramPinService;
import com.liferay.commerce.shop.by.diagram.service.CSDiagramSettingService;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Attachment;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Category;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Diagram;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.MappedProduct;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Pin;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Product;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductAccountGroup;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductChannel;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductConfiguration;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductOption;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductOptionValue;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductShippingConfiguration;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductSpecification;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductSubscriptionConfiguration;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductTaxConfiguration;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.RelatedProduct;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Sku;
import com.liferay.headless.commerce.admin.catalog.internal.dto.v1_0.converter.ProductDTOConverter;
import com.liferay.headless.commerce.admin.catalog.internal.dto.v1_0.util.CustomFieldsUtil;
import com.liferay.headless.commerce.admin.catalog.internal.helper.v1_0.ProductHelper;
import com.liferay.headless.commerce.admin.catalog.internal.odata.entity.v1_0.ProductEntityModel;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.AttachmentUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.DiagramUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.MappedProductUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.PinUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductConfigurationUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductOptionUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductOptionValueUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductShippingConfigurationUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductSpecificationUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductSubscriptionConfigurationUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductTaxConfigurationUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.RelatedProductUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.SkuUtil;
import com.liferay.headless.commerce.admin.catalog.resource.v1_0.ProductResource;
import com.liferay.headless.commerce.core.util.DateConfig;
import com.liferay.headless.commerce.core.util.ExpandoUtil;
import com.liferay.headless.commerce.core.util.LanguageUtils;
import com.liferay.headless.commerce.core.util.ServiceContextHelper;
import com.liferay.portal.kernel.change.tracking.CTAware;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.upload.UniqueFileNameProvider;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Zoltán Takács
 * @author Alessio Antonio Rendina
 * @author Igor Beslic
 */
@Component(
	enabled = false,
	properties = "OSGI-INF/liferay/rest/v1_0/product.properties",
	scope = ServiceScope.PROTOTYPE, service = ProductResource.class
)
@CTAware
public class ProductResourceImpl
	extends BaseProductResourceImpl implements EntityModelResource {

	@Override
	public void delete(
			Collection<Product> products, Map<String, Serializable> parameters)
		throws Exception {

		for (Product product : products) {
			deleteProduct(product.getProductId());
		}
	}

	@Override
	public Response deleteProduct(Long id) throws Exception {
		CPDefinition cpDefinition =
			_cpDefinitionService.fetchCPDefinitionByCProductId(id);

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find Product with ID: " + id);
		}

		_cpDefinitionService.deleteCPDefinition(
			cpDefinition.getCPDefinitionId());

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	public Response deleteProductByExternalReferenceCode(
			String externalReferenceCode)
		throws Exception {

		CPDefinition cpDefinition =
			_cpDefinitionService.
				fetchCPDefinitionByCProductExternalReferenceCode(
					externalReferenceCode, contextCompany.getCompanyId());

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find product with external reference code " +
					externalReferenceCode);
		}

		_cpDefinitionService.deleteCPDefinition(
			cpDefinition.getCPDefinitionId());

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap) {
		return _entityModel;
	}

	@Override
	public Product getProduct(Long id) throws Exception {
		CPDefinition cpDefinition =
			_cpDefinitionService.fetchCPDefinitionByCProductId(id);

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find Product with ID: " + id);
		}

		return _toProduct(cpDefinition.getCPDefinitionId());
	}

	@Override
	public Product getProductByExternalReferenceCode(
			String externalReferenceCode)
		throws Exception {

		CPDefinition cpDefinition =
			_cpDefinitionService.
				fetchCPDefinitionByCProductExternalReferenceCode(
					externalReferenceCode, contextCompany.getCompanyId());

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find product with external reference code " +
					externalReferenceCode);
		}

		return _toProduct(cpDefinition.getCPDefinitionId());
	}

	@Override
	public Page<Product> getProductsPage(
			String search, Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception {

		return _productHelper.getProductsPage(
			contextCompany.getCompanyId(), search, filter, pagination, sorts,
			document -> _toProduct(
				GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK))),
			contextAcceptLanguage.getPreferredLocale());
	}

	@Override
	public Response patchProduct(Long id, Product product) throws Exception {
		CPDefinition cpDefinition =
			_cpDefinitionService.fetchCPDefinitionByCProductId(id);

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find Product with ID: " + id);
		}

		_updateProduct(cpDefinition, product);

		if (!Validator.isBlank(product.getExternalReferenceCode())) {
			_cpDefinitionService.updateExternalReferenceCode(
				product.getExternalReferenceCode(),
				cpDefinition.getCPDefinitionId());
		}

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	public Response patchProductByExternalReferenceCode(
			String externalReferenceCode, Product product)
		throws Exception {

		CPDefinition cpDefinition =
			_cpDefinitionService.
				fetchCPDefinitionByCProductExternalReferenceCode(
					externalReferenceCode, contextCompany.getCompanyId());

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find product with external reference code " +
					externalReferenceCode);
		}

		_updateProduct(cpDefinition, product);

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	public Product postProduct(Product product) throws Exception {
		CPDefinition cpDefinition = _addOrUpdateProduct(product);

		return _toProduct(cpDefinition.getCPDefinitionId());
	}

	@Override
	public Product postProductByExternalReferenceCodeClone(
			String externalReferenceCode, String catalogExternalReferenceCode)
		throws Exception {

		CPDefinition cpDefinition =
			_cpDefinitionService.
				fetchCPDefinitionByCProductExternalReferenceCode(
					externalReferenceCode, contextCompany.getCompanyId());

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException();
		}

		CommerceCatalog commerceCatalog = cpDefinition.getCommerceCatalog();

		if (catalogExternalReferenceCode != null) {
			commerceCatalog =
				_commerceCatalogLocalService.
					fetchCommerceCatalogByExternalReferenceCode(
						contextCompany.getCompanyId(),
						catalogExternalReferenceCode);
		}

		if (commerceCatalog == null) {
			throw new NoSuchCatalogException();
		}

		cpDefinition = _cpDefinitionService.copyCPDefinition(
			cpDefinition.getCPDefinitionId(), commerceCatalog.getGroupId());

		return _toProduct(cpDefinition.getCPDefinitionId());
	}

	@Override
	public Product postProductClone(Long id, Long catalogId) throws Exception {
		CPDefinition cpDefinition =
			_cpDefinitionService.fetchCPDefinitionByCProductId(id);

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find Product with ID: " + id);
		}

		CommerceCatalog commerceCatalog = cpDefinition.getCommerceCatalog();

		if (catalogId != null) {
			commerceCatalog = _commerceCatalogLocalService.getCommerceCatalog(
				catalogId);
		}

		cpDefinition = _cpDefinitionService.copyCPDefinition(
			cpDefinition.getCPDefinitionId(), commerceCatalog.getGroupId());

		return _toProduct(cpDefinition.getCPDefinitionId());
	}

	@Override
	public void update(
			Collection<Product> products, Map<String, Serializable> parameters)
		throws Exception {

		for (Product product : products) {
			patchProduct(product.getProductId(), product);
		}
	}

	private CPDefinition _addOrUpdateProduct(Product product) throws Exception {
		CommerceCatalog commerceCatalog =
			_commerceCatalogLocalService.getCommerceCatalog(
				product.getCatalogId());

		ServiceContext serviceContext = _serviceContextHelper.getServiceContext(
			commerceCatalog.getGroupId());

		String[] assetTagNames = new String[0];

		if (product.getTags() != null) {
			assetTagNames = product.getTags();
		}

		serviceContext.setAssetTagNames(assetTagNames);

		Map<String, Serializable> expandoBridgeAttributes =
			_getExpandoBridgeAttributes(product);

		if (expandoBridgeAttributes != null) {
			serviceContext.setExpandoBridgeAttributes(expandoBridgeAttributes);
		}

		DateConfig displayDateConfig = DateConfig.toDisplayDateConfig(
			product.getDisplayDate(), serviceContext.getTimeZone());
		DateConfig expirationDateConfig = DateConfig.toExpirationDateConfig(
			product.getExpirationDate(), serviceContext.getTimeZone());

		ProductShippingConfiguration shippingConfiguration =
			_getProductShippingConfiguration(product);
		ProductSubscriptionConfiguration subscriptionConfiguration =
			_getProductSubscriptionConfiguration(product);
		ProductTaxConfiguration taxConfiguration = _getProductTaxConfiguration(
			product);

		CPDefinition cpDefinition =
			_cpDefinitionService.
				fetchCPDefinitionByCProductExternalReferenceCode(
					product.getExternalReferenceCode(),
					contextCompany.getCompanyId());

		Category[] categories = product.getCategories();

		if (categories != null) {
			List<Long> assetCategoryIds = new ArrayList<>();

			for (Category category : categories) {
				if (category.getId() != null) {
					assetCategoryIds.add(category.getId());
				}
			}

			serviceContext.setAssetCategoryIds(
				ArrayUtil.toLongArray(assetCategoryIds));
		}
		else if (cpDefinition != null) {
			serviceContext.setAssetCategoryIds(
				_assetCategoryLocalService.getCategoryIds(
					cpDefinition.getModelClassName(),
					cpDefinition.getCPDefinitionId()));
		}

		Map<String, String> nameMap = product.getName();

		if ((cpDefinition != null) && (nameMap == null)) {
			nameMap = LanguageUtils.getLanguageIdMap(cpDefinition.getNameMap());
		}

		Map<String, String> shortDescriptionMap = product.getShortDescription();

		if ((cpDefinition != null) && (shortDescriptionMap == null)) {
			shortDescriptionMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getShortDescriptionMap());
		}

		Map<String, String> descriptionMap = product.getDescription();

		if ((cpDefinition != null) && (descriptionMap == null)) {
			descriptionMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getDescriptionMap());
		}

		Map<String, String> urlTitleMap = product.getUrls();

		if ((cpDefinition != null) && (urlTitleMap == null)) {
			urlTitleMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getUrlTitleMap());
		}

		boolean ignoreSKUCombinations = true;

		if (cpDefinition != null) {
			ignoreSKUCombinations = cpDefinition.isIgnoreSKUCombinations();
		}

		int productStatus = GetterUtil.getInteger(product.getProductStatus());

		if (productStatus != WorkflowConstants.STATUS_APPROVED) {
			serviceContext.setWorkflowAction(
				WorkflowConstants.ACTION_SAVE_DRAFT);
		}

		cpDefinition = _cpDefinitionService.addOrUpdateCPDefinition(
			product.getExternalReferenceCode(), commerceCatalog.getGroupId(),
			LanguageUtils.getLocalizedMap(nameMap),
			LanguageUtils.getLocalizedMap(shortDescriptionMap),
			LanguageUtils.getLocalizedMap(descriptionMap),
			LanguageUtils.getLocalizedMap(urlTitleMap),
			LanguageUtils.getLocalizedMap(product.getMetaTitle()),
			LanguageUtils.getLocalizedMap(product.getMetaDescription()),
			LanguageUtils.getLocalizedMap(product.getMetaKeyword()),
			product.getProductType(), ignoreSKUCombinations,
			GetterUtil.getBoolean(shippingConfiguration.getShippable(), true),
			GetterUtil.getBoolean(
				shippingConfiguration.getFreeShipping(), true),
			GetterUtil.getBoolean(
				shippingConfiguration.getShippingSeparately(), true),
			GetterUtil.getDouble(shippingConfiguration.getShippingExtraPrice()),
			GetterUtil.getDouble(shippingConfiguration.getWidth()),
			GetterUtil.getDouble(shippingConfiguration.getHeight()),
			GetterUtil.getDouble(shippingConfiguration.getDepth()),
			GetterUtil.getDouble(shippingConfiguration.getWeight()),
			GetterUtil.getLong(taxConfiguration.getId()),
			ProductUtil.isTaxExempt(null, taxConfiguration), false, null, true,
			displayDateConfig.getMonth(), displayDateConfig.getDay(),
			displayDateConfig.getYear(), displayDateConfig.getHour(),
			displayDateConfig.getMinute(), expirationDateConfig.getMonth(),
			expirationDateConfig.getDay(), expirationDateConfig.getYear(),
			expirationDateConfig.getHour(), expirationDateConfig.getMinute(),
			GetterUtil.getBoolean(product.getNeverExpire(), true),
			product.getDefaultSku(),
			GetterUtil.getBoolean(subscriptionConfiguration.getEnable()),
			GetterUtil.getInteger(subscriptionConfiguration.getLength(), 1),
			GetterUtil.getString(
				subscriptionConfiguration.getSubscriptionTypeAsString()),
			null,
			GetterUtil.getLong(subscriptionConfiguration.getNumberOfLength()),
			productStatus, serviceContext);

		if ((product.getActive() != null) && !product.getActive()) {
			Map<String, Serializable> workflowContext = new HashMap<>();

			_cpDefinitionService.updateStatus(
				cpDefinition.getCPDefinitionId(),
				WorkflowConstants.STATUS_INACTIVE, serviceContext,
				workflowContext);
		}

		Map<String, ?> expando = product.getExpando();

		if ((expando != null) && !expando.isEmpty()) {
			ExpandoUtil.updateExpando(
				serviceContext.getCompanyId(), CPDefinition.class,
				cpDefinition.getPrimaryKey(), expando);
		}

		_updateNestedResources(product, cpDefinition, serviceContext);

		return cpDefinition;
	}

	private Map<String, Map<String, String>> _getActions(
		CPDefinition cpDefinition) {

		return HashMapBuilder.<String, Map<String, String>>put(
			"delete",
			addAction(
				"UPDATE", cpDefinition.getCPDefinitionId(), "deleteProduct",
				_cpDefinitionModelResourcePermission)
		).put(
			"get",
			addAction(
				"VIEW", cpDefinition.getCPDefinitionId(), "getProduct",
				_cpDefinitionModelResourcePermission)
		).put(
			"update",
			addAction(
				"UPDATE", cpDefinition.getCPDefinitionId(), "patchProduct",
				_cpDefinitionModelResourcePermission)
		).build();
	}

	private String[] _getAssetTags(CPDefinition cpDefinition) {
		List<AssetTag> assetEntryAssetTags = _assetTagService.getTags(
			cpDefinition.getModelClassName(), cpDefinition.getCPDefinitionId());

		Stream<AssetTag> stream = assetEntryAssetTags.stream();

		return stream.map(
			AssetTag::getName
		).toArray(
			String[]::new
		);
	}

	private Map<String, Serializable> _getExpandoBridgeAttributes(
		Attachment attachment) {

		return CustomFieldsUtil.toMap(
			CPAttachmentFileEntry.class.getName(),
			contextCompany.getCompanyId(), attachment.getCustomFields(),
			contextAcceptLanguage.getPreferredLocale());
	}

	private Map<String, Serializable> _getExpandoBridgeAttributes(
		Product product) {

		return CustomFieldsUtil.toMap(
			CPDefinition.class.getName(), contextCompany.getCompanyId(),
			product.getCustomFields(),
			contextAcceptLanguage.getPreferredLocale());
	}

	private ProductShippingConfiguration _getProductShippingConfiguration(
		Product product) {

		ProductShippingConfiguration shippingConfiguration =
			product.getShippingConfiguration();

		if (shippingConfiguration != null) {
			return shippingConfiguration;
		}

		return new ProductShippingConfiguration();
	}

	private ProductSubscriptionConfiguration
		_getProductSubscriptionConfiguration(Product product) {

		ProductSubscriptionConfiguration subscriptionConfiguration =
			product.getSubscriptionConfiguration();

		if (subscriptionConfiguration != null) {
			return subscriptionConfiguration;
		}

		return new ProductSubscriptionConfiguration();
	}

	private ProductTaxConfiguration _getProductTaxConfiguration(
		Product product) {

		ProductTaxConfiguration taxConfiguration =
			product.getTaxConfiguration();

		if (taxConfiguration != null) {
			return taxConfiguration;
		}

		return new ProductTaxConfiguration();
	}

	private Product _toProduct(Long cpDefinitionId) throws Exception {
		CPDefinition cpDefinition = _cpDefinitionService.getCPDefinition(
			cpDefinitionId);

		return _productDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				contextAcceptLanguage.isAcceptAllLanguages(),
				_getActions(cpDefinition), _dtoConverterRegistry,
				cpDefinitionId, contextAcceptLanguage.getPreferredLocale(),
				contextUriInfo, contextUser));
	}

	private CPDefinition _updateNestedResources(
			Product product, CPDefinition cpDefinition,
			ServiceContext serviceContext)
		throws Exception {

		serviceContext.setExpandoBridgeAttributes(null);

		// Product configuration

		ProductConfiguration productConfiguration = product.getConfiguration();

		if (productConfiguration != null) {
			ProductConfigurationUtil.updateCPDefinitionInventory(
				_cpDefinitionInventoryService, productConfiguration,
				cpDefinition.getCPDefinitionId());
		}

		// Product shipping configuration

		ProductShippingConfiguration productShippingConfiguration =
			product.getShippingConfiguration();

		if (productShippingConfiguration != null) {
			cpDefinition =
				ProductShippingConfigurationUtil.updateCPDefinitionShippingInfo(
					_cpDefinitionService, productShippingConfiguration,
					cpDefinition, serviceContext);
		}

		// Product subscription configuration

		ProductSubscriptionConfiguration productSubscriptionConfiguration =
			product.getSubscriptionConfiguration();

		if (productSubscriptionConfiguration != null) {
			cpDefinition =
				ProductSubscriptionConfigurationUtil.
					updateCPDefinitionSubscriptionInfo(
						_cpDefinitionService, productSubscriptionConfiguration,
						cpDefinition, serviceContext);
		}

		// Product tax configuration

		ProductTaxConfiguration productTaxConfiguration =
			product.getTaxConfiguration();

		if (productTaxConfiguration != null) {
			cpDefinition =
				ProductTaxConfigurationUtil.updateCPDefinitionTaxCategoryInfo(
					_cpDefinitionService, productTaxConfiguration,
					cpDefinition);
		}

		// Product specifications

		ProductSpecification[] productSpecifications =
			product.getProductSpecifications();

		if (productSpecifications != null) {
			_cpDefinitionSpecificationOptionValueService.
				deleteCPDefinitionSpecificationOptionValues(
					cpDefinition.getCPDefinitionId());

			for (ProductSpecification productSpecification :
					productSpecifications) {

				ProductSpecificationUtil.
					addCPDefinitionSpecificationOptionValue(
						_cpDefinitionSpecificationOptionValueService,
						_cpSpecificationOptionService,
						cpDefinition.getCPDefinitionId(), productSpecification,
						serviceContext);
			}
		}

		// Product options

		ProductOption[] productOptions = product.getProductOptions();

		if (productOptions != null) {
			for (ProductOption productOption : productOptions) {
				CPDefinitionOptionRel cpDefinitionOptionRel =
					ProductOptionUtil.addOrUpdateCPDefinitionOptionRel(
						_cpDefinitionOptionRelService, _cpOptionService,
						productOption, cpDefinition.getCPDefinitionId(),
						serviceContext);

				ProductOptionValue[] productOptionValues =
					productOption.getProductOptionValues();

				if (productOptionValues != null) {
					for (ProductOptionValue productOptionValue :
							productOptionValues) {

						ProductOptionValueUtil.
							addOrUpdateCPDefinitionOptionValueRel(
								_cpDefinitionOptionValueRelService,
								productOptionValue,
								cpDefinitionOptionRel.
									getCPDefinitionOptionRelId(),
								serviceContext);
					}
				}
			}
		}

		// Related products

		RelatedProduct[] relatedProducts = product.getRelatedProducts();

		if (relatedProducts != null) {
			for (RelatedProduct relatedProduct : relatedProducts) {
				RelatedProductUtil.addOrUpdateCPDefinitionLink(
					_cpDefinitionLinkService, _cpDefinitionService,
					relatedProduct, cpDefinition.getCPDefinitionId(),
					_serviceContextHelper.getServiceContext(
						cpDefinition.getGroupId()));
			}
		}

		// Skus

		Sku[] skus = product.getSkus();

		if (skus != null) {
			for (Sku sku : skus) {
				SkuUtil.addOrUpdateCPInstance(
					_cpInstanceService, sku, cpDefinition, serviceContext);
			}
		}

		// Images

		Attachment[] images = product.getImages();

		if (images != null) {
			for (Attachment attachment : images) {
				Map<String, Serializable> expandoBridgeAttributes =
					_getExpandoBridgeAttributes(attachment);

				if (expandoBridgeAttributes != null) {
					serviceContext.setExpandoBridgeAttributes(
						expandoBridgeAttributes);
				}

				AttachmentUtil.addOrUpdateCPAttachmentFileEntry(
					cpDefinition.getGroupId(), _cpAttachmentFileEntryService,
					_uniqueFileNameProvider, attachment,
					_classNameLocalService.getClassNameId(
						cpDefinition.getModelClassName()),
					cpDefinition.getCPDefinitionId(),
					CPAttachmentFileEntryConstants.TYPE_IMAGE, serviceContext);
			}
		}

		// Attachments

		Attachment[] attachments = product.getAttachments();

		if (attachments != null) {
			for (Attachment attachment : attachments) {
				Map<String, Serializable> expandoBridgeAttributes =
					_getExpandoBridgeAttributes(attachment);

				if (expandoBridgeAttributes != null) {
					serviceContext.setExpandoBridgeAttributes(
						expandoBridgeAttributes);
				}

				AttachmentUtil.addOrUpdateCPAttachmentFileEntry(
					cpDefinition.getGroupId(), _cpAttachmentFileEntryService,
					_uniqueFileNameProvider, attachment,
					_classNameLocalService.getClassNameId(
						cpDefinition.getModelClassName()),
					cpDefinition.getCPDefinitionId(),
					CPAttachmentFileEntryConstants.TYPE_OTHER, serviceContext);
			}
		}

		// Channels visibility

		_cpDefinitionService.updateCPDefinitionChannelFilter(
			cpDefinition.getCPDefinitionId(),
			GetterUtil.getBoolean(
				product.getProductChannelFilter(),
				cpDefinition.isChannelFilterEnabled()));

		ProductChannel[] productChannels = product.getProductChannels();

		if (productChannels != null) {
			_commerceChannelRelService.deleteCommerceChannelRels(
				CPDefinition.class.getName(), cpDefinition.getCPDefinitionId());

			Stream<ProductChannel> stream = Arrays.stream(productChannels);

			List<Long> channelIds = stream.map(
				productChannel -> {
					if (productChannel.getExternalReferenceCode() == null) {
						return productChannel.getChannelId();
					}

					CommerceChannel commerceChannel = null;

					try {
						commerceChannel =
							_commerceChannelService.
								fetchByExternalReferenceCode(
									productChannel.getExternalReferenceCode(),
									contextCompany.getCompanyId());
					}
					catch (PortalException portalException) {
						if (_log.isDebugEnabled()) {
							_log.debug(portalException);
						}
					}

					if (commerceChannel == null) {
						return null;
					}

					return commerceChannel.getCommerceChannelId();
				}
			).collect(
				Collectors.toList()
			);

			for (Long commerceChannelId : channelIds) {
				if (commerceChannelId == null) {
					continue;
				}

				_commerceChannelRelService.addCommerceChannelRel(
					CPDefinition.class.getName(),
					cpDefinition.getCPDefinitionId(), commerceChannelId,
					serviceContext);
			}
		}

		// Account groups visibility

		_cpDefinitionService.updateCPDefinitionAccountGroupFilter(
			cpDefinition.getCPDefinitionId(),
			GetterUtil.getBoolean(
				product.getProductAccountGroupFilter(),
				cpDefinition.isAccountGroupFilterEnabled()));

		ProductAccountGroup[] productAccountGroups =
			product.getProductAccountGroups();

		if (productAccountGroups != null) {
			_commerceAccountGroupRelService.deleteCommerceAccountGroupRels(
				CPDefinition.class.getName(), cpDefinition.getCPDefinitionId());

			Stream<ProductAccountGroup> productAccountGroupStream =
				Arrays.stream(productAccountGroups);

			List<Long> accountGroupIds = productAccountGroupStream.map(
				productAccountGroup -> {
					if (productAccountGroup.getExternalReferenceCode() ==
							null) {

						return productAccountGroup.getAccountGroupId();
					}

					CommerceAccountGroup commerceAccountGroup = null;

					try {
						commerceAccountGroup =
							_commerceAccountGroupService.
								fetchByExternalReferenceCode(
									contextCompany.getCompanyId(),
									productAccountGroup.
										getExternalReferenceCode());
					}
					catch (PortalException portalException) {
						if (_log.isDebugEnabled()) {
							_log.debug(portalException);
						}
					}

					if (commerceAccountGroup == null) {
						return null;
					}

					return commerceAccountGroup.getCommerceAccountGroupId();
				}
			).collect(
				Collectors.toList()
			);

			for (Long accountGroupId : accountGroupIds) {
				if (accountGroupId == null) {
					continue;
				}

				_commerceAccountGroupRelService.addCommerceAccountGroupRel(
					CPDefinition.class.getName(),
					cpDefinition.getCPDefinitionId(), accountGroupId,
					serviceContext);
			}
		}

		// Diagram

		CPType cpType = _cpTypeServicesTracker.getCPType(
			cpDefinition.getProductTypeName());

		if ((cpType != null) &&
			CSDiagramCPTypeConstants.NAME.equals(cpType.getName())) {

			Diagram diagram = product.getDiagram();

			if (diagram != null) {
				DiagramUtil.addOrUpdateCSDiagramSetting(
					contextCompany.getCompanyId(),
					_cpAttachmentFileEntryService,
					cpDefinition.getCPDefinitionId(), _csDiagramSettingService,
					diagram, cpDefinition.getGroupId(),
					contextAcceptLanguage.getPreferredLocale(),
					_serviceContextHelper, _uniqueFileNameProvider);
			}

			MappedProduct[] mappedProducts = product.getMappedProducts();

			if (mappedProducts != null) {
				_csDiagramEntryService.deleteCSDiagramEntries(
					cpDefinition.getCPDefinitionId());

				for (MappedProduct mappedProduct : mappedProducts) {
					MappedProductUtil.addOrUpdateCSDiagramEntry(
						contextCompany.getCompanyId(),
						cpDefinition.getCPDefinitionId(), _cpDefinitionService,
						_cpInstanceService, _csDiagramEntryService,
						cpDefinition.getGroupId(),
						contextAcceptLanguage.getPreferredLocale(),
						mappedProduct, _serviceContextHelper);
				}
			}

			Pin[] pins = product.getPins();

			if (pins != null) {
				_csDiagramPinService.deleteCSDiagramPins(
					cpDefinition.getCPDefinitionId());

				for (Pin pin : pins) {
					PinUtil.addOrUpdateCSDiagramPin(
						cpDefinition.getCPDefinitionId(), _csDiagramPinService,
						pin);
				}
			}
		}

		return cpDefinition;
	}

	private CPDefinition _updateProduct(
			CPDefinition cpDefinition, Product product)
		throws Exception {

		ServiceContext serviceContext = _serviceContextHelper.getServiceContext(
			cpDefinition.getGroupId());

		String[] assetTags = product.getTags();

		if (product.getTags() == null) {
			assetTags = _getAssetTags(cpDefinition);
		}

		serviceContext.setAssetTagNames(assetTags);

		Map<String, Serializable> expandoBridgeAttributes =
			_getExpandoBridgeAttributes(product);

		if (expandoBridgeAttributes != null) {
			serviceContext.setExpandoBridgeAttributes(expandoBridgeAttributes);
		}

		Calendar displayCalendar = CalendarFactoryUtil.getCalendar(
			serviceContext.getTimeZone());

		DateConfig displayDateConfig = new DateConfig(displayCalendar);

		Calendar expirationCalendar = CalendarFactoryUtil.getCalendar(
			serviceContext.getTimeZone());

		expirationCalendar.add(Calendar.MONTH, 1);

		DateConfig expirationDateConfig = new DateConfig(expirationCalendar);

		Category[] categories = product.getCategories();

		if (categories == null) {
			serviceContext.setAssetCategoryIds(
				_assetCategoryLocalService.getCategoryIds(
					cpDefinition.getModelClassName(),
					cpDefinition.getCPDefinitionId()));
		}
		else {
			List<Long> assetCategoryIds = new ArrayList<>();

			for (Category category : categories) {
				if (category.getId() != null) {
					assetCategoryIds.add(category.getId());
				}
			}

			serviceContext.setAssetCategoryIds(
				ArrayUtil.toLongArray(assetCategoryIds));
		}

		Map<String, String> nameMap = product.getName();

		if ((cpDefinition != null) && (nameMap == null)) {
			nameMap = LanguageUtils.getLanguageIdMap(cpDefinition.getNameMap());
		}

		Map<String, String> shortDescriptionMap = product.getShortDescription();

		if ((cpDefinition != null) && (shortDescriptionMap == null)) {
			shortDescriptionMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getShortDescriptionMap());
		}

		Map<String, String> descriptionMap = product.getDescription();

		if ((cpDefinition != null) && (descriptionMap == null)) {
			descriptionMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getDescriptionMap());
		}

		cpDefinition = _cpDefinitionService.updateCPDefinition(
			cpDefinition.getCPDefinitionId(),
			LanguageUtils.getLocalizedMap(nameMap),
			LanguageUtils.getLocalizedMap(shortDescriptionMap),
			LanguageUtils.getLocalizedMap(descriptionMap),
			cpDefinition.getUrlTitleMap(), cpDefinition.getMetaTitleMap(),
			cpDefinition.getMetaDescriptionMap(),
			cpDefinition.getMetaKeywordsMap(),
			cpDefinition.isIgnoreSKUCombinations(),
			cpDefinition.getDDMStructureKey(), true,
			displayDateConfig.getMonth(), displayDateConfig.getDay(),
			displayDateConfig.getYear(), displayDateConfig.getHour(),
			displayDateConfig.getMinute(), expirationDateConfig.getMonth(),
			expirationDateConfig.getDay(), expirationDateConfig.getYear(),
			expirationDateConfig.getHour(), expirationDateConfig.getMinute(),
			GetterUtil.getBoolean(product.getNeverExpire(), true),
			serviceContext);

		if ((product.getActive() != null) && !product.getActive()) {
			Map<String, Serializable> workflowContext = new HashMap<>();

			_cpDefinitionService.updateStatus(
				cpDefinition.getCPDefinitionId(),
				WorkflowConstants.STATUS_INACTIVE, serviceContext,
				workflowContext);
		}

		Map<String, ?> expando = product.getExpando();

		if ((expando != null) && !expando.isEmpty()) {
			ExpandoUtil.updateExpando(
				serviceContext.getCompanyId(), CPDefinition.class,
				cpDefinition.getPrimaryKey(), expando);
		}

		return _updateNestedResources(product, cpDefinition, serviceContext);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ProductResourceImpl.class);

	@Reference
	private AssetCategoryLocalService _assetCategoryLocalService;

	@Reference
	private AssetTagService _assetTagService;

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CommerceAccountGroupRelService _commerceAccountGroupRelService;

	@Reference
	private CommerceAccountGroupService _commerceAccountGroupService;

	@Reference
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	@Reference
	private CommerceChannelRelService _commerceChannelRelService;

	@Reference
	private CommerceChannelService _commerceChannelService;

	@Reference
	private CPAttachmentFileEntryService _cpAttachmentFileEntryService;

	@Reference
	private CPDefinitionInventoryService _cpDefinitionInventoryService;

	@Reference
	private CPDefinitionLinkService _cpDefinitionLinkService;

	@Reference(
		target = "(model.class.name=com.liferay.commerce.product.model.CPDefinition)"
	)
	private ModelResourcePermission<CPDefinition>
		_cpDefinitionModelResourcePermission;

	@Reference
	private CPDefinitionOptionRelService _cpDefinitionOptionRelService;

	@Reference
	private CPDefinitionOptionValueRelService
		_cpDefinitionOptionValueRelService;

	@Reference
	private CPDefinitionService _cpDefinitionService;

	@Reference
	private CPDefinitionSpecificationOptionValueService
		_cpDefinitionSpecificationOptionValueService;

	@Reference
	private CPInstanceService _cpInstanceService;

	@Reference
	private CPOptionService _cpOptionService;

	@Reference
	private CPSpecificationOptionService _cpSpecificationOptionService;

	@Reference
	private CPTypeServicesTracker _cpTypeServicesTracker;

	@Reference
	private CSDiagramEntryService _csDiagramEntryService;

	@Reference
	private CSDiagramPinService _csDiagramPinService;

	@Reference
	private CSDiagramSettingService _csDiagramSettingService;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

	private final EntityModel _entityModel = new ProductEntityModel();

	@Reference
	private ProductDTOConverter _productDTOConverter;

	@Reference
	private ProductHelper _productHelper;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

	@Reference
	private UniqueFileNameProvider _uniqueFileNameProvider;

}