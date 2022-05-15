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

package com.liferay.commerce.product.service;

import com.liferay.commerce.product.model.CPAttachmentFileEntry;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.portal.kernel.change.tracking.CTAware;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.portal.kernel.service.BaseService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.io.Serializable;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the remote service interface for CPDefinition. Methods of this
 * service are expected to have security checks based on the propagated JAAS
 * credentials because this service can be accessed remotely.
 *
 * @author Marco Leo
 * @see CPDefinitionServiceUtil
 * @generated
 */
@AccessControlled
@CTAware
@JSONWebService
@OSGiBeanProperties(
	property = {
		"json.web.service.context.name=commerce",
		"json.web.service.context.path=CPDefinition"
	},
	service = CPDefinitionService.class
)
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface CPDefinitionService extends BaseService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>com.liferay.commerce.product.service.impl.CPDefinitionServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the cp definition remote service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link CPDefinitionServiceUtil} if injection and service tracking are not available.
	 */
	public CPDefinition addCPDefinition(
			String externalReferenceCode, long groupId,
			Map<Locale, String> nameMap,
			Map<Locale, String> shortDescriptionMap,
			Map<Locale, String> descriptionMap, Map<Locale, String> urlTitleMap,
			Map<Locale, String> metaTitleMap,
			Map<Locale, String> metaDescriptionMap,
			Map<Locale, String> metaKeywordsMap, String productTypeName,
			boolean ignoreSKUCombinations, boolean shippable,
			boolean freeShipping, boolean shipSeparately,
			double shippingExtraPrice, double width, double height,
			double depth, double weight, long cpTaxCategoryId,
			boolean taxExempt, boolean telcoOrElectronics,
			String ddmStructureKey, boolean published, int displayDateMonth,
			int displayDateDay, int displayDateYear, int displayDateHour,
			int displayDateMinute, int expirationDateMonth,
			int expirationDateDay, int expirationDateYear,
			int expirationDateHour, int expirationDateMinute,
			boolean neverExpire, String defaultSku, boolean subscriptionEnabled,
			int subscriptionLength, String subscriptionType,
			UnicodeProperties subscriptionTypeSettingsUnicodeProperties,
			long maxSubscriptionCycles, boolean deliverySubscriptionEnabled,
			int deliverySubscriptionLength, String deliverySubscriptionType,
			UnicodeProperties deliverySubscriptionTypeSettingsUnicodeProperties,
			long deliveryMaxSubscriptionCycles, int status,
			ServiceContext serviceContext)
		throws PortalException;

	public CPDefinition addCPDefinition(
			String externalReferenceCode, long groupId,
			Map<Locale, String> nameMap,
			Map<Locale, String> shortDescriptionMap,
			Map<Locale, String> descriptionMap, Map<Locale, String> urlTitleMap,
			Map<Locale, String> metaTitleMap,
			Map<Locale, String> metaDescriptionMap,
			Map<Locale, String> metaKeywordsMap, String productTypeName,
			boolean ignoreSKUCombinations, boolean shippable,
			boolean freeShipping, boolean shipSeparately,
			double shippingExtraPrice, double width, double height,
			double depth, double weight, long cpTaxCategoryId,
			boolean taxExempt, boolean telcoOrElectronics,
			String ddmStructureKey, boolean published, int displayDateMonth,
			int displayDateDay, int displayDateYear, int displayDateHour,
			int displayDateMinute, int expirationDateMonth,
			int expirationDateDay, int expirationDateYear,
			int expirationDateHour, int expirationDateMinute,
			boolean neverExpire, String defaultSku, boolean subscriptionEnabled,
			int subscriptionLength, String subscriptionType,
			UnicodeProperties subscriptionTypeSettingsUnicodeProperties,
			long maxSubscriptionCycles, int status,
			ServiceContext serviceContext)
		throws PortalException;

	public CPDefinition addOrUpdateCPDefinition(
			String externalReferenceCode, long groupId,
			Map<Locale, String> nameMap,
			Map<Locale, String> shortDescriptionMap,
			Map<Locale, String> descriptionMap, Map<Locale, String> urlTitleMap,
			Map<Locale, String> metaTitleMap,
			Map<Locale, String> metaDescriptionMap,
			Map<Locale, String> metaKeywordsMap, String productTypeName,
			boolean ignoreSKUCombinations, boolean shippable,
			boolean freeShipping, boolean shipSeparately,
			double shippingExtraPrice, double width, double height,
			double depth, double weight, long cpTaxCategoryId,
			boolean taxExempt, boolean telcoOrElectronics,
			String ddmStructureKey, boolean published, int displayDateMonth,
			int displayDateDay, int displayDateYear, int displayDateHour,
			int displayDateMinute, int expirationDateMonth,
			int expirationDateDay, int expirationDateYear,
			int expirationDateHour, int expirationDateMinute,
			boolean neverExpire, String defaultSku, boolean subscriptionEnabled,
			int subscriptionLength, String subscriptionType,
			UnicodeProperties subscriptionTypeSettingsUnicodeProperties,
			long maxSubscriptionCycles, boolean deliverySubscriptionEnabled,
			int deliverySubscriptionLength, String deliverySubscriptionType,
			UnicodeProperties deliverySubscriptionTypeSettingsUnicodeProperties,
			long deliveryMaxSubscriptionCycles, int status,
			ServiceContext serviceContext)
		throws PortalException;

	public CPDefinition addOrUpdateCPDefinition(
			String externalReferenceCode, long groupId,
			Map<Locale, String> nameMap,
			Map<Locale, String> shortDescriptionMap,
			Map<Locale, String> descriptionMap, Map<Locale, String> urlTitleMap,
			Map<Locale, String> metaTitleMap,
			Map<Locale, String> metaDescriptionMap,
			Map<Locale, String> metaKeywordsMap, String productTypeName,
			boolean ignoreSKUCombinations, boolean shippable,
			boolean freeShipping, boolean shipSeparately,
			double shippingExtraPrice, double width, double height,
			double depth, double weight, long cpTaxCategoryId,
			boolean taxExempt, boolean telcoOrElectronics,
			String ddmStructureKey, boolean published, int displayDateMonth,
			int displayDateDay, int displayDateYear, int displayDateHour,
			int displayDateMinute, int expirationDateMonth,
			int expirationDateDay, int expirationDateYear,
			int expirationDateHour, int expirationDateMinute,
			boolean neverExpire, String defaultSku, boolean subscriptionEnabled,
			int subscriptionLength, String subscriptionType,
			UnicodeProperties subscriptionTypeSettingsUnicodeProperties,
			long maxSubscriptionCycles, int status,
			ServiceContext serviceContext)
		throws PortalException;

	public CPDefinition copyCPDefinition(long cpDefinitionId, long groupId)
		throws PortalException;

	public void deleteAssetCategoryCPDefinition(
			long cpDefinitionId, long categoryId, ServiceContext serviceContext)
		throws PortalException;

	public void deleteCPDefinition(long cpDefinitionId) throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CPDefinition fetchCPDefinition(long cpDefinitionId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CPDefinition fetchCPDefinitionByCProductExternalReferenceCode(
			String externalReferenceCode, long companyId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CPDefinition fetchCPDefinitionByCProductId(long cProductId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CPDefinition getCPDefinition(long cpDefinitionId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<CPDefinition> getCPDefinitions(
			long groupId, int status, int start, int end,
			OrderByComparator<CPDefinition> orderByComparator)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getCPDefinitionsCount(long groupId, int status)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CPAttachmentFileEntry getDefaultImageCPAttachmentFileEntry(
			long cpDefinitionId)
		throws PortalException;

	/**
	 * @deprecated As of Cavanaugh (7.4.x), with no direct replacement
	 */
	@Deprecated
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String getLayoutUuid(long cpDefinitionId) throws PortalException;

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<Locale, String> getUrlTitleMap(long cpDefinitionId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String getUrlTitleMapAsXML(long cpDefinitionId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public BaseModelSearchResult<CPDefinition> searchCPDefinitions(
			long companyId, String keywords, int status, int start, int end,
			Sort sort)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public BaseModelSearchResult<CPDefinition> searchCPDefinitions(
			long companyId, String keywords, String filterFields,
			String filterValues, int start, int end, Sort sort)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public BaseModelSearchResult<CPDefinition>
			searchCPDefinitionsByChannelGroupId(
				long companyId, long commerceChannelGroupId, String keywords,
				int status, int start, int end, Sort sort)
		throws PortalException;

	public CPDefinition updateCPDefinition(
			long cpDefinitionId, Map<Locale, String> nameMap,
			Map<Locale, String> shortDescriptionMap,
			Map<Locale, String> descriptionMap, Map<Locale, String> urlTitleMap,
			Map<Locale, String> metaTitleMap,
			Map<Locale, String> metaDescriptionMap,
			Map<Locale, String> metaKeywordsMap, boolean ignoreSKUCombinations,
			String ddmStructureKey, boolean published, int displayDateMonth,
			int displayDateDay, int displayDateYear, int displayDateHour,
			int displayDateMinute, int expirationDateMonth,
			int expirationDateDay, int expirationDateYear,
			int expirationDateHour, int expirationDateMinute,
			boolean neverExpire, ServiceContext serviceContext)
		throws PortalException;

	public CPDefinition updateCPDefinitionAccountGroupFilter(
			long cpDefinitionId, boolean enable)
		throws PortalException;

	public CPDefinition updateCPDefinitionCategorization(
			long cpDefinitionId, ServiceContext serviceContext)
		throws PortalException;

	public CPDefinition updateCPDefinitionChannelFilter(
			long cpDefinitionId, boolean enable)
		throws PortalException;

	/**
	 * @deprecated As of Cavanaugh (7.4.x), with no direct replacement
	 */
	@Deprecated
	public void updateCPDisplayLayout(
			long cpDefinitionId, String layoutUuid,
			ServiceContext serviceContext)
		throws PortalException;

	public CPDefinition updateExternalReferenceCode(
			String externalReferenceCode, long cpDefinitionId)
		throws PortalException;

	public CPDefinition updateShippingInfo(
			long cpDefinitionId, boolean shippable, boolean freeShipping,
			boolean shipSeparately, double shippingExtraPrice, double width,
			double height, double depth, double weight,
			ServiceContext serviceContext)
		throws PortalException;

	public CPDefinition updateStatus(
			long cpDefinitionId, int status, ServiceContext serviceContext,
			Map<String, Serializable> workflowContext)
		throws PortalException;

	public CPDefinition updateSubscriptionInfo(
			long cpDefinitionId, boolean subscriptionEnabled,
			int subscriptionLength, String subscriptionType,
			UnicodeProperties subscriptionTypeSettingsUnicodeProperties,
			long maxSubscriptionCycles, boolean deliverySubscriptionEnabled,
			int deliverySubscriptionLength, String deliverySubscriptionType,
			UnicodeProperties deliverySubscriptionTypeSettingsUnicodeProperties,
			long deliveryMaxSubscriptionCycles)
		throws PortalException;

	/**
	 * @deprecated As of Athanasius (7.3.x)
	 */
	@Deprecated
	public CPDefinition updateSubscriptionInfo(
			long cpDefinitionId, boolean subscriptionEnabled,
			int subscriptionLength, String subscriptionType,
			UnicodeProperties subscriptionTypeSettingsUnicodeProperties,
			long maxSubscriptionCycles, ServiceContext serviceContext)
		throws PortalException;

	public CPDefinition updateTaxCategoryInfo(
			long cpDefinitionId, long cpTaxCategoryId, boolean taxExempt,
			boolean telcoOrElectronics)
		throws PortalException;

}