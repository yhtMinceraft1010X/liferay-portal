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

import com.liferay.commerce.product.model.CPTaxCategory;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.persistence.change.tracking.CTPersistence;

/**
 * Provides a wrapper for {@link CPTaxCategoryLocalService}.
 *
 * @author Marco Leo
 * @see CPTaxCategoryLocalService
 * @generated
 */
public class CPTaxCategoryLocalServiceWrapper
	implements CPTaxCategoryLocalService,
			   ServiceWrapper<CPTaxCategoryLocalService> {

	public CPTaxCategoryLocalServiceWrapper() {
		this(null);
	}

	public CPTaxCategoryLocalServiceWrapper(
		CPTaxCategoryLocalService cpTaxCategoryLocalService) {

		_cpTaxCategoryLocalService = cpTaxCategoryLocalService;
	}

	/**
	 * Adds the cp tax category to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CPTaxCategoryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param cpTaxCategory the cp tax category
	 * @return the cp tax category that was added
	 */
	@Override
	public CPTaxCategory addCPTaxCategory(CPTaxCategory cpTaxCategory) {
		return _cpTaxCategoryLocalService.addCPTaxCategory(cpTaxCategory);
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 #addCPTaxCategory(String, Map, Map, ServiceContext)}
	 */
	@Deprecated
	@Override
	public CPTaxCategory addCPTaxCategory(
			java.util.Map<java.util.Locale, String> nameMap,
			java.util.Map<java.util.Locale, String> descriptionMap,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _cpTaxCategoryLocalService.addCPTaxCategory(
			nameMap, descriptionMap, serviceContext);
	}

	@Override
	public CPTaxCategory addCPTaxCategory(
			String externalReferenceCode,
			java.util.Map<java.util.Locale, String> nameMap,
			java.util.Map<java.util.Locale, String> descriptionMap,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _cpTaxCategoryLocalService.addCPTaxCategory(
			externalReferenceCode, nameMap, descriptionMap, serviceContext);
	}

	@Override
	public int countCPTaxCategoriesByCompanyId(long companyId, String keyword) {
		return _cpTaxCategoryLocalService.countCPTaxCategoriesByCompanyId(
			companyId, keyword);
	}

	/**
	 * Creates a new cp tax category with the primary key. Does not add the cp tax category to the database.
	 *
	 * @param CPTaxCategoryId the primary key for the new cp tax category
	 * @return the new cp tax category
	 */
	@Override
	public CPTaxCategory createCPTaxCategory(long CPTaxCategoryId) {
		return _cpTaxCategoryLocalService.createCPTaxCategory(CPTaxCategoryId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _cpTaxCategoryLocalService.createPersistedModel(primaryKeyObj);
	}

	@Override
	public void deleteCPTaxCategories(long companyId) {
		_cpTaxCategoryLocalService.deleteCPTaxCategories(companyId);
	}

	/**
	 * Deletes the cp tax category from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CPTaxCategoryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param cpTaxCategory the cp tax category
	 * @return the cp tax category that was removed
	 * @throws PortalException
	 */
	@Override
	public CPTaxCategory deleteCPTaxCategory(CPTaxCategory cpTaxCategory)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _cpTaxCategoryLocalService.deleteCPTaxCategory(cpTaxCategory);
	}

	/**
	 * Deletes the cp tax category with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CPTaxCategoryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param CPTaxCategoryId the primary key of the cp tax category
	 * @return the cp tax category that was removed
	 * @throws PortalException if a cp tax category with the primary key could not be found
	 */
	@Override
	public CPTaxCategory deleteCPTaxCategory(long CPTaxCategoryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _cpTaxCategoryLocalService.deleteCPTaxCategory(CPTaxCategoryId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _cpTaxCategoryLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _cpTaxCategoryLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _cpTaxCategoryLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _cpTaxCategoryLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _cpTaxCategoryLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.product.model.impl.CPTaxCategoryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _cpTaxCategoryLocalService.dynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.product.model.impl.CPTaxCategoryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _cpTaxCategoryLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _cpTaxCategoryLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _cpTaxCategoryLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public CPTaxCategory fetchCPTaxCategory(long CPTaxCategoryId) {
		return _cpTaxCategoryLocalService.fetchCPTaxCategory(CPTaxCategoryId);
	}

	/**
	 * Returns the cp tax category with the matching external reference code and company.
	 *
	 * @param companyId the primary key of the company
	 * @param externalReferenceCode the cp tax category's external reference code
	 * @return the matching cp tax category, or <code>null</code> if a matching cp tax category could not be found
	 */
	@Override
	public CPTaxCategory fetchCPTaxCategoryByExternalReferenceCode(
		long companyId, String externalReferenceCode) {

		return _cpTaxCategoryLocalService.
			fetchCPTaxCategoryByExternalReferenceCode(
				companyId, externalReferenceCode);
	}

	/**
	 * @deprecated As of Cavanaugh (7.4.x), replaced by {@link #fetchCPTaxCategoryByExternalReferenceCode(long, String)}
	 */
	@Deprecated
	@Override
	public CPTaxCategory fetchCPTaxCategoryByReferenceCode(
		long companyId, String externalReferenceCode) {

		return _cpTaxCategoryLocalService.fetchCPTaxCategoryByReferenceCode(
			companyId, externalReferenceCode);
	}

	@Override
	public java.util.List<CPTaxCategory> findCPTaxCategoriesByCompanyId(
		long companyId, String keyword, int start, int end) {

		return _cpTaxCategoryLocalService.findCPTaxCategoriesByCompanyId(
			companyId, keyword, start, end);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _cpTaxCategoryLocalService.getActionableDynamicQuery();
	}

	/**
	 * Returns a range of all the cp tax categories.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.product.model.impl.CPTaxCategoryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of cp tax categories
	 * @param end the upper bound of the range of cp tax categories (not inclusive)
	 * @return the range of cp tax categories
	 */
	@Override
	public java.util.List<CPTaxCategory> getCPTaxCategories(
		int start, int end) {

		return _cpTaxCategoryLocalService.getCPTaxCategories(start, end);
	}

	@Override
	public java.util.List<CPTaxCategory> getCPTaxCategories(long companyId) {
		return _cpTaxCategoryLocalService.getCPTaxCategories(companyId);
	}

	@Override
	public java.util.List<CPTaxCategory> getCPTaxCategories(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CPTaxCategory>
			orderByComparator) {

		return _cpTaxCategoryLocalService.getCPTaxCategories(
			companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of cp tax categories.
	 *
	 * @return the number of cp tax categories
	 */
	@Override
	public int getCPTaxCategoriesCount() {
		return _cpTaxCategoryLocalService.getCPTaxCategoriesCount();
	}

	@Override
	public int getCPTaxCategoriesCount(long companyId) {
		return _cpTaxCategoryLocalService.getCPTaxCategoriesCount(companyId);
	}

	/**
	 * Returns the cp tax category with the primary key.
	 *
	 * @param CPTaxCategoryId the primary key of the cp tax category
	 * @return the cp tax category
	 * @throws PortalException if a cp tax category with the primary key could not be found
	 */
	@Override
	public CPTaxCategory getCPTaxCategory(long CPTaxCategoryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _cpTaxCategoryLocalService.getCPTaxCategory(CPTaxCategoryId);
	}

	/**
	 * Returns the cp tax category with the matching external reference code and company.
	 *
	 * @param companyId the primary key of the company
	 * @param externalReferenceCode the cp tax category's external reference code
	 * @return the matching cp tax category
	 * @throws PortalException if a matching cp tax category could not be found
	 */
	@Override
	public CPTaxCategory getCPTaxCategoryByExternalReferenceCode(
			long companyId, String externalReferenceCode)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _cpTaxCategoryLocalService.
			getCPTaxCategoryByExternalReferenceCode(
				companyId, externalReferenceCode);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _cpTaxCategoryLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _cpTaxCategoryLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _cpTaxCategoryLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the cp tax category in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CPTaxCategoryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param cpTaxCategory the cp tax category
	 * @return the cp tax category that was updated
	 */
	@Override
	public CPTaxCategory updateCPTaxCategory(CPTaxCategory cpTaxCategory) {
		return _cpTaxCategoryLocalService.updateCPTaxCategory(cpTaxCategory);
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 #updateCPTaxCategory(String, long, Map, Map)}
	 */
	@Deprecated
	@Override
	public CPTaxCategory updateCPTaxCategory(
			long cpTaxCategoryId,
			java.util.Map<java.util.Locale, String> nameMap,
			java.util.Map<java.util.Locale, String> descriptionMap)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _cpTaxCategoryLocalService.updateCPTaxCategory(
			cpTaxCategoryId, nameMap, descriptionMap);
	}

	@Override
	public CPTaxCategory updateCPTaxCategory(
			String externalReferenceCode, long cpTaxCategoryId,
			java.util.Map<java.util.Locale, String> nameMap,
			java.util.Map<java.util.Locale, String> descriptionMap)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _cpTaxCategoryLocalService.updateCPTaxCategory(
			externalReferenceCode, cpTaxCategoryId, nameMap, descriptionMap);
	}

	@Override
	public CTPersistence<CPTaxCategory> getCTPersistence() {
		return _cpTaxCategoryLocalService.getCTPersistence();
	}

	@Override
	public Class<CPTaxCategory> getModelClass() {
		return _cpTaxCategoryLocalService.getModelClass();
	}

	@Override
	public <R, E extends Throwable> R updateWithUnsafeFunction(
			UnsafeFunction<CTPersistence<CPTaxCategory>, R, E>
				updateUnsafeFunction)
		throws E {

		return _cpTaxCategoryLocalService.updateWithUnsafeFunction(
			updateUnsafeFunction);
	}

	@Override
	public CPTaxCategoryLocalService getWrappedService() {
		return _cpTaxCategoryLocalService;
	}

	@Override
	public void setWrappedService(
		CPTaxCategoryLocalService cpTaxCategoryLocalService) {

		_cpTaxCategoryLocalService = cpTaxCategoryLocalService;
	}

	private CPTaxCategoryLocalService _cpTaxCategoryLocalService;

}