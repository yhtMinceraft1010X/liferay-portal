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

package com.liferay.headless.admin.address.internal.graphql.mutation.v1_0;

import com.liferay.headless.admin.address.dto.v1_0.Country;
import com.liferay.headless.admin.address.dto.v1_0.Region;
import com.liferay.headless.admin.address.resource.v1_0.CountryResource;
import com.liferay.headless.admin.address.resource.v1_0.RegionResource;
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
 * @author Drew Brokke
 * @generated
 */
@Generated("")
public class Mutation {

	public static void setCountryResourceComponentServiceObjects(
		ComponentServiceObjects<CountryResource>
			countryResourceComponentServiceObjects) {

		_countryResourceComponentServiceObjects =
			countryResourceComponentServiceObjects;
	}

	public static void setRegionResourceComponentServiceObjects(
		ComponentServiceObjects<RegionResource>
			regionResourceComponentServiceObjects) {

		_regionResourceComponentServiceObjects =
			regionResourceComponentServiceObjects;
	}

	@GraphQLField
	public Country createCountry(@GraphQLName("country") Country country)
		throws Exception {

		return _applyComponentServiceObjects(
			_countryResourceComponentServiceObjects,
			this::_populateResourceContext,
			countryResource -> countryResource.postCountry(country));
	}

	@GraphQLField
	public Response createCountryBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_countryResourceComponentServiceObjects,
			this::_populateResourceContext,
			countryResource -> countryResource.postCountryBatch(
				callbackURL, object));
	}

	@GraphQLField
	public boolean deleteCountry(@GraphQLName("countryId") Long countryId)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_countryResourceComponentServiceObjects,
			this::_populateResourceContext,
			countryResource -> countryResource.deleteCountry(countryId));

		return true;
	}

	@GraphQLField
	public Response deleteCountryBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_countryResourceComponentServiceObjects,
			this::_populateResourceContext,
			countryResource -> countryResource.deleteCountryBatch(
				callbackURL, object));
	}

	@GraphQLField
	public Country patchCountry(
			@GraphQLName("countryId") Long countryId,
			@GraphQLName("country") Country country)
		throws Exception {

		return _applyComponentServiceObjects(
			_countryResourceComponentServiceObjects,
			this::_populateResourceContext,
			countryResource -> countryResource.patchCountry(
				countryId, country));
	}

	@GraphQLField
	public Country updateCountry(
			@GraphQLName("countryId") Long countryId,
			@GraphQLName("country") Country country)
		throws Exception {

		return _applyComponentServiceObjects(
			_countryResourceComponentServiceObjects,
			this::_populateResourceContext,
			countryResource -> countryResource.putCountry(countryId, country));
	}

	@GraphQLField
	public Response updateCountryBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_countryResourceComponentServiceObjects,
			this::_populateResourceContext,
			countryResource -> countryResource.putCountryBatch(
				callbackURL, object));
	}

	@GraphQLField
	public Region createCountryRegion(
			@GraphQLName("countryId") Long countryId,
			@GraphQLName("region") Region region)
		throws Exception {

		return _applyComponentServiceObjects(
			_regionResourceComponentServiceObjects,
			this::_populateResourceContext,
			regionResource -> regionResource.postCountryRegion(
				countryId, region));
	}

	@GraphQLField
	public Response createCountryRegionBatch(
			@GraphQLName("countryId") Long countryId,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_regionResourceComponentServiceObjects,
			this::_populateResourceContext,
			regionResource -> regionResource.postCountryRegionBatch(
				countryId, callbackURL, object));
	}

	@GraphQLField
	public boolean deleteRegion(@GraphQLName("regionId") Long regionId)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_regionResourceComponentServiceObjects,
			this::_populateResourceContext,
			regionResource -> regionResource.deleteRegion(regionId));

		return true;
	}

	@GraphQLField
	public Response deleteRegionBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_regionResourceComponentServiceObjects,
			this::_populateResourceContext,
			regionResource -> regionResource.deleteRegionBatch(
				callbackURL, object));
	}

	@GraphQLField
	public Region patchRegion(
			@GraphQLName("regionId") Long regionId,
			@GraphQLName("region") Region region)
		throws Exception {

		return _applyComponentServiceObjects(
			_regionResourceComponentServiceObjects,
			this::_populateResourceContext,
			regionResource -> regionResource.patchRegion(regionId, region));
	}

	@GraphQLField
	public Region updateRegion(
			@GraphQLName("regionId") Long regionId,
			@GraphQLName("region") Region region)
		throws Exception {

		return _applyComponentServiceObjects(
			_regionResourceComponentServiceObjects,
			this::_populateResourceContext,
			regionResource -> regionResource.putRegion(regionId, region));
	}

	@GraphQLField
	public Response updateRegionBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_regionResourceComponentServiceObjects,
			this::_populateResourceContext,
			regionResource -> regionResource.putRegionBatch(
				callbackURL, object));
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

	private void _populateResourceContext(CountryResource countryResource)
		throws Exception {

		countryResource.setContextAcceptLanguage(_acceptLanguage);
		countryResource.setContextCompany(_company);
		countryResource.setContextHttpServletRequest(_httpServletRequest);
		countryResource.setContextHttpServletResponse(_httpServletResponse);
		countryResource.setContextUriInfo(_uriInfo);
		countryResource.setContextUser(_user);
		countryResource.setGroupLocalService(_groupLocalService);
		countryResource.setRoleLocalService(_roleLocalService);

		countryResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(RegionResource regionResource)
		throws Exception {

		regionResource.setContextAcceptLanguage(_acceptLanguage);
		regionResource.setContextCompany(_company);
		regionResource.setContextHttpServletRequest(_httpServletRequest);
		regionResource.setContextHttpServletResponse(_httpServletResponse);
		regionResource.setContextUriInfo(_uriInfo);
		regionResource.setContextUser(_user);
		regionResource.setGroupLocalService(_groupLocalService);
		regionResource.setRoleLocalService(_roleLocalService);

		regionResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private static ComponentServiceObjects<CountryResource>
		_countryResourceComponentServiceObjects;
	private static ComponentServiceObjects<RegionResource>
		_regionResourceComponentServiceObjects;

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