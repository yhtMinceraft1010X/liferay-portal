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

package com.liferay.headless.admin.address.internal.resource.v1_0;

import com.liferay.headless.admin.address.dto.v1_0.Country;
import com.liferay.headless.admin.address.internal.dto.v1_0.converter.CountryResourceDTOConverter;
import com.liferay.headless.admin.address.resource.v1_0.CountryResource;
import com.liferay.portal.kernel.model.CountryTable;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.CountryLocalService;
import com.liferay.portal.kernel.service.CountryService;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.odata.entity.DoubleEntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.entity.StringEntityField;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 * @author Drew Brokke
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/country.properties",
	scope = ServiceScope.PROTOTYPE, service = CountryResource.class
)
public class CountryResourceImpl extends BaseCountryResourceImpl {

	@Override
	public void deleteCountry(Long countryId) throws Exception {
		_countryService.deleteCountry(countryId);
	}

	@Override
	public Page<Country> getCountriesPage(
			Boolean active, String search, Pagination pagination, Sort[] sorts)
		throws Exception {

		BaseModelSearchResult<com.liferay.portal.kernel.model.Country>
			baseModelSearchResult = _countryService.searchCountries(
				contextCompany.getCompanyId(), active, search,
				pagination.getStartPosition(), pagination.getEndPosition(),
				_toOrderByComparator(sorts));

		return Page.of(
			transform(baseModelSearchResult.getBaseModels(), this::_toCountry),
			pagination, baseModelSearchResult.getLength());
	}

	@Override
	public Country getCountry(Long countryId) throws Exception {
		return _toCountry(_countryService.getCountry(countryId));
	}

	@Override
	public Country getCountryByA2(String a2) throws Exception {
		return _toCountry(
			_countryService.getCountryByA2(contextCompany.getCompanyId(), a2));
	}

	@Override
	public Country getCountryByA3(String a3) throws Exception {
		return _toCountry(
			_countryService.getCountryByA3(contextCompany.getCompanyId(), a3));
	}

	@Override
	public Country getCountryByName(String name) throws Exception {
		return _toCountry(
			_countryService.getCountryByName(
				contextCompany.getCompanyId(), name));
	}

	@Override
	public Country getCountryByNumber(Integer number) throws Exception {
		return _toCountry(
			_countryService.getCountryByNumber(
				contextCompany.getCompanyId(), String.valueOf(number)));
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap)
		throws Exception {

		return _entityModel;
	}

	@Override
	public Country postCountry(Country country) throws Exception {
		com.liferay.portal.kernel.model.Country serviceBuilderCountry =
			_countryService.addCountry(
				country.getA2(), country.getA3(),
				GetterUtil.getBoolean(country.getActive(), true),
				GetterUtil.getBoolean(country.getBillingAllowed(), true),
				String.valueOf(country.getIdd()), country.getName(),
				String.valueOf(country.getNumber()),
				GetterUtil.getDouble(country.getPosition()),
				GetterUtil.getBoolean(country.getShippingAllowed(), true),
				GetterUtil.getBoolean(country.getSubjectToVAT()),
				GetterUtil.getBoolean(country.getZipRequired(), true),
				ServiceContextFactory.getInstance(
					Country.class.getName(), contextHttpServletRequest));

		return _toCountry(
			_countryLocalService.updateGroupFilterEnabled(
				serviceBuilderCountry.getCountryId(),
				GetterUtil.getBoolean(country.getGroupFilterEnabled())));
	}

	public Country putCountry(Long countryId, Country country)
		throws Exception {

		com.liferay.portal.kernel.model.Country serviceBuilderCountry =
			_countryService.updateCountry(
				countryId, country.getA2(), country.getA3(),
				GetterUtil.getBoolean(country.getActive(), true),
				GetterUtil.getBoolean(country.getBillingAllowed(), true),
				String.valueOf(country.getIdd()), country.getName(),
				String.valueOf(country.getNumber()),
				GetterUtil.getDouble(country.getPosition()),
				GetterUtil.getBoolean(country.getShippingAllowed(), true),
				GetterUtil.getBoolean(country.getSubjectToVAT()));

		return _toCountry(
			_countryService.updateGroupFilterEnabled(
				serviceBuilderCountry.getCountryId(),
				GetterUtil.getBoolean(country.getGroupFilterEnabled())));
	}

	private Country _toCountry(
			com.liferay.portal.kernel.model.Country serviceBuilderCountry)
		throws Exception {

		return _countryResourceDTOConverter.toDTO(serviceBuilderCountry);
	}

	private OrderByComparator<com.liferay.portal.kernel.model.Country>
		_toOrderByComparator(Sort[] sorts) {

		if (ArrayUtil.isEmpty(sorts)) {
			return null;
		}

		List<Object> objects = new ArrayList<>();

		for (Sort sort : sorts) {
			objects.add(sort.getFieldName());
			objects.add(!sort.isReverse());
		}

		return OrderByComparatorFactoryUtil.create(
			CountryTable.INSTANCE.getTableName(),
			objects.toArray(new Object[0]));
	}

	private static final EntityModel _entityModel =
		() -> EntityModel.toEntityFieldsMap(
			new StringEntityField("name", locale -> "name"),
			new DoubleEntityField("position", locale -> "position"));

	@Reference
	private CountryLocalService _countryLocalService;

	@Reference
	private CountryResourceDTOConverter _countryResourceDTOConverter;

	@Reference
	private CountryService _countryService;

}