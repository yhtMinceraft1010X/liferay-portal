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

package com.liferay.commerce.price.list.model;

import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.math.BigDecimal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link CommercePriceEntry}.
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @see CommercePriceEntry
 * @generated
 */
public class CommercePriceEntryWrapper
	extends BaseModelWrapper<CommercePriceEntry>
	implements CommercePriceEntry, ModelWrapper<CommercePriceEntry> {

	public CommercePriceEntryWrapper(CommercePriceEntry commercePriceEntry) {
		super(commercePriceEntry);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("ctCollectionId", getCtCollectionId());
		attributes.put("uuid", getUuid());
		attributes.put("externalReferenceCode", getExternalReferenceCode());
		attributes.put("commercePriceEntryId", getCommercePriceEntryId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("commercePriceListId", getCommercePriceListId());
		attributes.put("CPInstanceUuid", getCPInstanceUuid());
		attributes.put("CProductId", getCProductId());
		attributes.put("price", getPrice());
		attributes.put("promoPrice", getPromoPrice());
		attributes.put("discountDiscovery", isDiscountDiscovery());
		attributes.put("discountLevel1", getDiscountLevel1());
		attributes.put("discountLevel2", getDiscountLevel2());
		attributes.put("discountLevel3", getDiscountLevel3());
		attributes.put("discountLevel4", getDiscountLevel4());
		attributes.put("hasTierPrice", isHasTierPrice());
		attributes.put("bulkPricing", isBulkPricing());
		attributes.put("displayDate", getDisplayDate());
		attributes.put("expirationDate", getExpirationDate());
		attributes.put("lastPublishDate", getLastPublishDate());
		attributes.put("status", getStatus());
		attributes.put("statusByUserId", getStatusByUserId());
		attributes.put("statusByUserName", getStatusByUserName());
		attributes.put("statusDate", getStatusDate());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		Long ctCollectionId = (Long)attributes.get("ctCollectionId");

		if (ctCollectionId != null) {
			setCtCollectionId(ctCollectionId);
		}

		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		String externalReferenceCode = (String)attributes.get(
			"externalReferenceCode");

		if (externalReferenceCode != null) {
			setExternalReferenceCode(externalReferenceCode);
		}

		Long commercePriceEntryId = (Long)attributes.get(
			"commercePriceEntryId");

		if (commercePriceEntryId != null) {
			setCommercePriceEntryId(commercePriceEntryId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long commercePriceListId = (Long)attributes.get("commercePriceListId");

		if (commercePriceListId != null) {
			setCommercePriceListId(commercePriceListId);
		}

		String CPInstanceUuid = (String)attributes.get("CPInstanceUuid");

		if (CPInstanceUuid != null) {
			setCPInstanceUuid(CPInstanceUuid);
		}

		Long CProductId = (Long)attributes.get("CProductId");

		if (CProductId != null) {
			setCProductId(CProductId);
		}

		BigDecimal price = (BigDecimal)attributes.get("price");

		if (price != null) {
			setPrice(price);
		}

		BigDecimal promoPrice = (BigDecimal)attributes.get("promoPrice");

		if (promoPrice != null) {
			setPromoPrice(promoPrice);
		}

		Boolean discountDiscovery = (Boolean)attributes.get(
			"discountDiscovery");

		if (discountDiscovery != null) {
			setDiscountDiscovery(discountDiscovery);
		}

		BigDecimal discountLevel1 = (BigDecimal)attributes.get(
			"discountLevel1");

		if (discountLevel1 != null) {
			setDiscountLevel1(discountLevel1);
		}

		BigDecimal discountLevel2 = (BigDecimal)attributes.get(
			"discountLevel2");

		if (discountLevel2 != null) {
			setDiscountLevel2(discountLevel2);
		}

		BigDecimal discountLevel3 = (BigDecimal)attributes.get(
			"discountLevel3");

		if (discountLevel3 != null) {
			setDiscountLevel3(discountLevel3);
		}

		BigDecimal discountLevel4 = (BigDecimal)attributes.get(
			"discountLevel4");

		if (discountLevel4 != null) {
			setDiscountLevel4(discountLevel4);
		}

		Boolean hasTierPrice = (Boolean)attributes.get("hasTierPrice");

		if (hasTierPrice != null) {
			setHasTierPrice(hasTierPrice);
		}

		Boolean bulkPricing = (Boolean)attributes.get("bulkPricing");

		if (bulkPricing != null) {
			setBulkPricing(bulkPricing);
		}

		Date displayDate = (Date)attributes.get("displayDate");

		if (displayDate != null) {
			setDisplayDate(displayDate);
		}

		Date expirationDate = (Date)attributes.get("expirationDate");

		if (expirationDate != null) {
			setExpirationDate(expirationDate);
		}

		Date lastPublishDate = (Date)attributes.get("lastPublishDate");

		if (lastPublishDate != null) {
			setLastPublishDate(lastPublishDate);
		}

		Integer status = (Integer)attributes.get("status");

		if (status != null) {
			setStatus(status);
		}

		Long statusByUserId = (Long)attributes.get("statusByUserId");

		if (statusByUserId != null) {
			setStatusByUserId(statusByUserId);
		}

		String statusByUserName = (String)attributes.get("statusByUserName");

		if (statusByUserName != null) {
			setStatusByUserName(statusByUserName);
		}

		Date statusDate = (Date)attributes.get("statusDate");

		if (statusDate != null) {
			setStatusDate(statusDate);
		}
	}

	@Override
	public CommercePriceEntry cloneWithOriginalValues() {
		return wrap(model.cloneWithOriginalValues());
	}

	/**
	 * Returns the bulk pricing of this commerce price entry.
	 *
	 * @return the bulk pricing of this commerce price entry
	 */
	@Override
	public boolean getBulkPricing() {
		return model.getBulkPricing();
	}

	/**
	 * Returns the commerce price entry ID of this commerce price entry.
	 *
	 * @return the commerce price entry ID of this commerce price entry
	 */
	@Override
	public long getCommercePriceEntryId() {
		return model.getCommercePriceEntryId();
	}

	@Override
	public CommercePriceList getCommercePriceList()
		throws com.liferay.portal.kernel.exception.PortalException {

		return model.getCommercePriceList();
	}

	/**
	 * Returns the commerce price list ID of this commerce price entry.
	 *
	 * @return the commerce price list ID of this commerce price entry
	 */
	@Override
	public long getCommercePriceListId() {
		return model.getCommercePriceListId();
	}

	/**
	 * Returns the company ID of this commerce price entry.
	 *
	 * @return the company ID of this commerce price entry
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	@Override
	public com.liferay.commerce.product.model.CPInstance getCPInstance()
		throws com.liferay.portal.kernel.exception.PortalException {

		return model.getCPInstance();
	}

	/**
	 * Returns the cp instance uuid of this commerce price entry.
	 *
	 * @return the cp instance uuid of this commerce price entry
	 */
	@Override
	public String getCPInstanceUuid() {
		return model.getCPInstanceUuid();
	}

	/**
	 * Returns the c product ID of this commerce price entry.
	 *
	 * @return the c product ID of this commerce price entry
	 */
	@Override
	public long getCProductId() {
		return model.getCProductId();
	}

	/**
	 * Returns the create date of this commerce price entry.
	 *
	 * @return the create date of this commerce price entry
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the ct collection ID of this commerce price entry.
	 *
	 * @return the ct collection ID of this commerce price entry
	 */
	@Override
	public long getCtCollectionId() {
		return model.getCtCollectionId();
	}

	/**
	 * Returns the discount discovery of this commerce price entry.
	 *
	 * @return the discount discovery of this commerce price entry
	 */
	@Override
	public boolean getDiscountDiscovery() {
		return model.getDiscountDiscovery();
	}

	/**
	 * Returns the discount level1 of this commerce price entry.
	 *
	 * @return the discount level1 of this commerce price entry
	 */
	@Override
	public BigDecimal getDiscountLevel1() {
		return model.getDiscountLevel1();
	}

	/**
	 * Returns the discount level2 of this commerce price entry.
	 *
	 * @return the discount level2 of this commerce price entry
	 */
	@Override
	public BigDecimal getDiscountLevel2() {
		return model.getDiscountLevel2();
	}

	/**
	 * Returns the discount level3 of this commerce price entry.
	 *
	 * @return the discount level3 of this commerce price entry
	 */
	@Override
	public BigDecimal getDiscountLevel3() {
		return model.getDiscountLevel3();
	}

	/**
	 * Returns the discount level4 of this commerce price entry.
	 *
	 * @return the discount level4 of this commerce price entry
	 */
	@Override
	public BigDecimal getDiscountLevel4() {
		return model.getDiscountLevel4();
	}

	/**
	 * Returns the display date of this commerce price entry.
	 *
	 * @return the display date of this commerce price entry
	 */
	@Override
	public Date getDisplayDate() {
		return model.getDisplayDate();
	}

	/**
	 * Returns the expiration date of this commerce price entry.
	 *
	 * @return the expiration date of this commerce price entry
	 */
	@Override
	public Date getExpirationDate() {
		return model.getExpirationDate();
	}

	/**
	 * Returns the external reference code of this commerce price entry.
	 *
	 * @return the external reference code of this commerce price entry
	 */
	@Override
	public String getExternalReferenceCode() {
		return model.getExternalReferenceCode();
	}

	/**
	 * Returns the has tier price of this commerce price entry.
	 *
	 * @return the has tier price of this commerce price entry
	 */
	@Override
	public boolean getHasTierPrice() {
		return model.getHasTierPrice();
	}

	/**
	 * Returns the last publish date of this commerce price entry.
	 *
	 * @return the last publish date of this commerce price entry
	 */
	@Override
	public Date getLastPublishDate() {
		return model.getLastPublishDate();
	}

	/**
	 * Returns the modified date of this commerce price entry.
	 *
	 * @return the modified date of this commerce price entry
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the mvcc version of this commerce price entry.
	 *
	 * @return the mvcc version of this commerce price entry
	 */
	@Override
	public long getMvccVersion() {
		return model.getMvccVersion();
	}

	/**
	 * Returns the price of this commerce price entry.
	 *
	 * @return the price of this commerce price entry
	 */
	@Override
	public BigDecimal getPrice() {
		return model.getPrice();
	}

	@Override
	public com.liferay.commerce.currency.model.CommerceMoney
			getPriceCommerceMoney(long commerceCurrencyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return model.getPriceCommerceMoney(commerceCurrencyId);
	}

	/**
	 * Returns the primary key of this commerce price entry.
	 *
	 * @return the primary key of this commerce price entry
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the promo price of this commerce price entry.
	 *
	 * @return the promo price of this commerce price entry
	 */
	@Override
	public BigDecimal getPromoPrice() {
		return model.getPromoPrice();
	}

	@Override
	public com.liferay.commerce.currency.model.CommerceMoney
			getPromoPriceCommerceMoney(long commerceCurrencyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return model.getPromoPriceCommerceMoney(commerceCurrencyId);
	}

	/**
	 * Returns the status of this commerce price entry.
	 *
	 * @return the status of this commerce price entry
	 */
	@Override
	public int getStatus() {
		return model.getStatus();
	}

	/**
	 * Returns the status by user ID of this commerce price entry.
	 *
	 * @return the status by user ID of this commerce price entry
	 */
	@Override
	public long getStatusByUserId() {
		return model.getStatusByUserId();
	}

	/**
	 * Returns the status by user name of this commerce price entry.
	 *
	 * @return the status by user name of this commerce price entry
	 */
	@Override
	public String getStatusByUserName() {
		return model.getStatusByUserName();
	}

	/**
	 * Returns the status by user uuid of this commerce price entry.
	 *
	 * @return the status by user uuid of this commerce price entry
	 */
	@Override
	public String getStatusByUserUuid() {
		return model.getStatusByUserUuid();
	}

	/**
	 * Returns the status date of this commerce price entry.
	 *
	 * @return the status date of this commerce price entry
	 */
	@Override
	public Date getStatusDate() {
		return model.getStatusDate();
	}

	/**
	 * Returns the user ID of this commerce price entry.
	 *
	 * @return the user ID of this commerce price entry
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this commerce price entry.
	 *
	 * @return the user name of this commerce price entry
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this commerce price entry.
	 *
	 * @return the user uuid of this commerce price entry
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * Returns the uuid of this commerce price entry.
	 *
	 * @return the uuid of this commerce price entry
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	/**
	 * Returns <code>true</code> if this commerce price entry is approved.
	 *
	 * @return <code>true</code> if this commerce price entry is approved; <code>false</code> otherwise
	 */
	@Override
	public boolean isApproved() {
		return model.isApproved();
	}

	/**
	 * Returns <code>true</code> if this commerce price entry is bulk pricing.
	 *
	 * @return <code>true</code> if this commerce price entry is bulk pricing; <code>false</code> otherwise
	 */
	@Override
	public boolean isBulkPricing() {
		return model.isBulkPricing();
	}

	/**
	 * Returns <code>true</code> if this commerce price entry is denied.
	 *
	 * @return <code>true</code> if this commerce price entry is denied; <code>false</code> otherwise
	 */
	@Override
	public boolean isDenied() {
		return model.isDenied();
	}

	/**
	 * Returns <code>true</code> if this commerce price entry is discount discovery.
	 *
	 * @return <code>true</code> if this commerce price entry is discount discovery; <code>false</code> otherwise
	 */
	@Override
	public boolean isDiscountDiscovery() {
		return model.isDiscountDiscovery();
	}

	/**
	 * Returns <code>true</code> if this commerce price entry is a draft.
	 *
	 * @return <code>true</code> if this commerce price entry is a draft; <code>false</code> otherwise
	 */
	@Override
	public boolean isDraft() {
		return model.isDraft();
	}

	/**
	 * Returns <code>true</code> if this commerce price entry is expired.
	 *
	 * @return <code>true</code> if this commerce price entry is expired; <code>false</code> otherwise
	 */
	@Override
	public boolean isExpired() {
		return model.isExpired();
	}

	/**
	 * Returns <code>true</code> if this commerce price entry is has tier price.
	 *
	 * @return <code>true</code> if this commerce price entry is has tier price; <code>false</code> otherwise
	 */
	@Override
	public boolean isHasTierPrice() {
		return model.isHasTierPrice();
	}

	/**
	 * Returns <code>true</code> if this commerce price entry is inactive.
	 *
	 * @return <code>true</code> if this commerce price entry is inactive; <code>false</code> otherwise
	 */
	@Override
	public boolean isInactive() {
		return model.isInactive();
	}

	/**
	 * Returns <code>true</code> if this commerce price entry is incomplete.
	 *
	 * @return <code>true</code> if this commerce price entry is incomplete; <code>false</code> otherwise
	 */
	@Override
	public boolean isIncomplete() {
		return model.isIncomplete();
	}

	/**
	 * Returns <code>true</code> if this commerce price entry is pending.
	 *
	 * @return <code>true</code> if this commerce price entry is pending; <code>false</code> otherwise
	 */
	@Override
	public boolean isPending() {
		return model.isPending();
	}

	/**
	 * Returns <code>true</code> if this commerce price entry is scheduled.
	 *
	 * @return <code>true</code> if this commerce price entry is scheduled; <code>false</code> otherwise
	 */
	@Override
	public boolean isScheduled() {
		return model.isScheduled();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets whether this commerce price entry is bulk pricing.
	 *
	 * @param bulkPricing the bulk pricing of this commerce price entry
	 */
	@Override
	public void setBulkPricing(boolean bulkPricing) {
		model.setBulkPricing(bulkPricing);
	}

	/**
	 * Sets the commerce price entry ID of this commerce price entry.
	 *
	 * @param commercePriceEntryId the commerce price entry ID of this commerce price entry
	 */
	@Override
	public void setCommercePriceEntryId(long commercePriceEntryId) {
		model.setCommercePriceEntryId(commercePriceEntryId);
	}

	/**
	 * Sets the commerce price list ID of this commerce price entry.
	 *
	 * @param commercePriceListId the commerce price list ID of this commerce price entry
	 */
	@Override
	public void setCommercePriceListId(long commercePriceListId) {
		model.setCommercePriceListId(commercePriceListId);
	}

	/**
	 * Sets the company ID of this commerce price entry.
	 *
	 * @param companyId the company ID of this commerce price entry
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the cp instance uuid of this commerce price entry.
	 *
	 * @param CPInstanceUuid the cp instance uuid of this commerce price entry
	 */
	@Override
	public void setCPInstanceUuid(String CPInstanceUuid) {
		model.setCPInstanceUuid(CPInstanceUuid);
	}

	/**
	 * Sets the c product ID of this commerce price entry.
	 *
	 * @param CProductId the c product ID of this commerce price entry
	 */
	@Override
	public void setCProductId(long CProductId) {
		model.setCProductId(CProductId);
	}

	/**
	 * Sets the create date of this commerce price entry.
	 *
	 * @param createDate the create date of this commerce price entry
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the ct collection ID of this commerce price entry.
	 *
	 * @param ctCollectionId the ct collection ID of this commerce price entry
	 */
	@Override
	public void setCtCollectionId(long ctCollectionId) {
		model.setCtCollectionId(ctCollectionId);
	}

	/**
	 * Sets whether this commerce price entry is discount discovery.
	 *
	 * @param discountDiscovery the discount discovery of this commerce price entry
	 */
	@Override
	public void setDiscountDiscovery(boolean discountDiscovery) {
		model.setDiscountDiscovery(discountDiscovery);
	}

	/**
	 * Sets the discount level1 of this commerce price entry.
	 *
	 * @param discountLevel1 the discount level1 of this commerce price entry
	 */
	@Override
	public void setDiscountLevel1(BigDecimal discountLevel1) {
		model.setDiscountLevel1(discountLevel1);
	}

	/**
	 * Sets the discount level2 of this commerce price entry.
	 *
	 * @param discountLevel2 the discount level2 of this commerce price entry
	 */
	@Override
	public void setDiscountLevel2(BigDecimal discountLevel2) {
		model.setDiscountLevel2(discountLevel2);
	}

	/**
	 * Sets the discount level3 of this commerce price entry.
	 *
	 * @param discountLevel3 the discount level3 of this commerce price entry
	 */
	@Override
	public void setDiscountLevel3(BigDecimal discountLevel3) {
		model.setDiscountLevel3(discountLevel3);
	}

	/**
	 * Sets the discount level4 of this commerce price entry.
	 *
	 * @param discountLevel4 the discount level4 of this commerce price entry
	 */
	@Override
	public void setDiscountLevel4(BigDecimal discountLevel4) {
		model.setDiscountLevel4(discountLevel4);
	}

	/**
	 * Sets the display date of this commerce price entry.
	 *
	 * @param displayDate the display date of this commerce price entry
	 */
	@Override
	public void setDisplayDate(Date displayDate) {
		model.setDisplayDate(displayDate);
	}

	/**
	 * Sets the expiration date of this commerce price entry.
	 *
	 * @param expirationDate the expiration date of this commerce price entry
	 */
	@Override
	public void setExpirationDate(Date expirationDate) {
		model.setExpirationDate(expirationDate);
	}

	/**
	 * Sets the external reference code of this commerce price entry.
	 *
	 * @param externalReferenceCode the external reference code of this commerce price entry
	 */
	@Override
	public void setExternalReferenceCode(String externalReferenceCode) {
		model.setExternalReferenceCode(externalReferenceCode);
	}

	/**
	 * Sets whether this commerce price entry is has tier price.
	 *
	 * @param hasTierPrice the has tier price of this commerce price entry
	 */
	@Override
	public void setHasTierPrice(boolean hasTierPrice) {
		model.setHasTierPrice(hasTierPrice);
	}

	/**
	 * Sets the last publish date of this commerce price entry.
	 *
	 * @param lastPublishDate the last publish date of this commerce price entry
	 */
	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		model.setLastPublishDate(lastPublishDate);
	}

	/**
	 * Sets the modified date of this commerce price entry.
	 *
	 * @param modifiedDate the modified date of this commerce price entry
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the mvcc version of this commerce price entry.
	 *
	 * @param mvccVersion the mvcc version of this commerce price entry
	 */
	@Override
	public void setMvccVersion(long mvccVersion) {
		model.setMvccVersion(mvccVersion);
	}

	/**
	 * Sets the price of this commerce price entry.
	 *
	 * @param price the price of this commerce price entry
	 */
	@Override
	public void setPrice(BigDecimal price) {
		model.setPrice(price);
	}

	/**
	 * Sets the primary key of this commerce price entry.
	 *
	 * @param primaryKey the primary key of this commerce price entry
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the promo price of this commerce price entry.
	 *
	 * @param promoPrice the promo price of this commerce price entry
	 */
	@Override
	public void setPromoPrice(BigDecimal promoPrice) {
		model.setPromoPrice(promoPrice);
	}

	/**
	 * Sets the status of this commerce price entry.
	 *
	 * @param status the status of this commerce price entry
	 */
	@Override
	public void setStatus(int status) {
		model.setStatus(status);
	}

	/**
	 * Sets the status by user ID of this commerce price entry.
	 *
	 * @param statusByUserId the status by user ID of this commerce price entry
	 */
	@Override
	public void setStatusByUserId(long statusByUserId) {
		model.setStatusByUserId(statusByUserId);
	}

	/**
	 * Sets the status by user name of this commerce price entry.
	 *
	 * @param statusByUserName the status by user name of this commerce price entry
	 */
	@Override
	public void setStatusByUserName(String statusByUserName) {
		model.setStatusByUserName(statusByUserName);
	}

	/**
	 * Sets the status by user uuid of this commerce price entry.
	 *
	 * @param statusByUserUuid the status by user uuid of this commerce price entry
	 */
	@Override
	public void setStatusByUserUuid(String statusByUserUuid) {
		model.setStatusByUserUuid(statusByUserUuid);
	}

	/**
	 * Sets the status date of this commerce price entry.
	 *
	 * @param statusDate the status date of this commerce price entry
	 */
	@Override
	public void setStatusDate(Date statusDate) {
		model.setStatusDate(statusDate);
	}

	/**
	 * Sets the user ID of this commerce price entry.
	 *
	 * @param userId the user ID of this commerce price entry
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this commerce price entry.
	 *
	 * @param userName the user name of this commerce price entry
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this commerce price entry.
	 *
	 * @param userUuid the user uuid of this commerce price entry
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	/**
	 * Sets the uuid of this commerce price entry.
	 *
	 * @param uuid the uuid of this commerce price entry
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	@Override
	public Map<String, Function<CommercePriceEntry, Object>>
		getAttributeGetterFunctions() {

		return model.getAttributeGetterFunctions();
	}

	@Override
	public Map<String, BiConsumer<CommercePriceEntry, Object>>
		getAttributeSetterBiConsumers() {

		return model.getAttributeSetterBiConsumers();
	}

	@Override
	public StagedModelType getStagedModelType() {
		return model.getStagedModelType();
	}

	@Override
	protected CommercePriceEntryWrapper wrap(
		CommercePriceEntry commercePriceEntry) {

		return new CommercePriceEntryWrapper(commercePriceEntry);
	}

}