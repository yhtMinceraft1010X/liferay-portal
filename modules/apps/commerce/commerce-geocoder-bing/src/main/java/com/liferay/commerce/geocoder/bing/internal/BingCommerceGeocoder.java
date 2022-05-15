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

package com.liferay.commerce.geocoder.bing.internal;

import com.liferay.commerce.exception.CommerceGeocoderException;
import com.liferay.commerce.geocoder.bing.internal.configuration.BingCommerceGeocoderConfiguration;
import com.liferay.commerce.model.CommerceGeocoder;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Country;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.service.CountryLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RegionLocalService;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.URLCodec;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * @author Andrea Di Giorgi
 * @author Luca Pellizzon
 */
@Component(
	configurationPid = "com.liferay.commerce.geocoder.bing.internal.configuration.BingCommerceGeocoderConfiguration",
	enabled = false, immediate = true, service = CommerceGeocoder.class
)
public class BingCommerceGeocoder implements CommerceGeocoder {

	@Override
	public double[] getCoordinates(
			long groupId, String street, String city, String zip,
			String regionCode, String countryA2)
		throws CommerceGeocoderException {

		try {
			Group group = _groupLocalService.getGroup(groupId);

			Country country = _countryLocalService.getCountryByA2(
				group.getCompanyId(), countryA2);

			return _getCoordinates(
				street, city, zip,
				_regionLocalService.getRegion(
					country.getCountryId(), regionCode),
				country);
		}
		catch (CommerceGeocoderException commerceGeocoderException) {
			throw commerceGeocoderException;
		}
		catch (Exception exception) {
			throw new CommerceGeocoderException(exception);
		}
	}

	@Override
	public double[] getCoordinates(
			String street, String city, String zip, Region region,
			Country country)
		throws CommerceGeocoderException {

		try {
			return _getCoordinates(street, city, zip, region, country);
		}
		catch (CommerceGeocoderException commerceGeocoderException) {
			throw commerceGeocoderException;
		}
		catch (Exception exception) {
			throw new CommerceGeocoderException(exception);
		}
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		BingCommerceGeocoderConfiguration bingCommerceGeocoderConfiguration =
			ConfigurableUtil.createConfigurable(
				BingCommerceGeocoderConfiguration.class, properties);

		_apiKey = bingCommerceGeocoderConfiguration.apiKey();
	}

	@Deactivate
	protected void deactivate() {
		_apiKey = null;
	}

	private void _addParameter(StringBundler sb, String name, String value) {
		if (Validator.isNull(value)) {
			return;
		}

		sb.append(name);
		sb.append(CharPool.EQUAL);
		sb.append(URLCodec.encodeURL(value));
		sb.append(CharPool.AMPERSAND);
	}

	private double[] _getCoordinates(
			String street, String city, String zip, Region region,
			Country country)
		throws Exception {

		if (Validator.isNull(_apiKey)) {
			throw new CommerceGeocoderException(
				"Bing commerce geocoder is not configured properly");
		}

		Http.Options options = new Http.Options();

		options.setLocation(_getUrl(street, city, zip, region, country));

		String json = _http.URLtoString(options);

		Http.Response response = options.getResponse();

		int responseCode = response.getResponseCode();

		if (responseCode != HttpServletResponse.SC_OK) {
			throw new CommerceGeocoderException(
				"Bing commerce geocoder returned an error code (" +
					responseCode + StringPool.CLOSE_PARENTHESIS);
		}

		String xMsBmWsInfo = response.getHeader("X-MS-BM-WS-INFO");

		if (Validator.isNotNull(xMsBmWsInfo) && xMsBmWsInfo.equals("1")) {
			throw new CommerceGeocoderException(
				"Bing commerce geocoder is temporarily unavailable");
		}

		JSONObject jsonObject = _jsonFactory.createJSONObject(json);

		JSONArray resourceSetsJSONArray = jsonObject.getJSONArray(
			"resourceSets");

		JSONObject resourceSetJSONObject = resourceSetsJSONArray.getJSONObject(
			0);

		JSONArray resourcesJSONArray = resourceSetJSONObject.getJSONArray(
			"resources");

		if (resourcesJSONArray.length() == 0) {
			throw new CommerceGeocoderException(
				"Bing commerce geocoder did not return any result");
		}

		JSONObject resourceJSONObject = resourcesJSONArray.getJSONObject(0);

		JSONObject pointJSONObject = resourceJSONObject.getJSONObject("point");

		JSONArray coordinatesJSONArray = pointJSONObject.getJSONArray(
			"coordinates");

		return new double[] {
			coordinatesJSONArray.getDouble(0), coordinatesJSONArray.getDouble(1)
		};
	}

	private String _getUrl(
		String street, String city, String zip, Region region,
		Country country) {

		StringBundler sb = new StringBundler(28);

		sb.append("https://dev.virtualearth.net/REST/v1/Locations?");

		_addParameter(sb, "key", _apiKey);
		_addParameter(sb, "addressLine", street);
		_addParameter(sb, "locality", city);
		_addParameter(sb, "postalCode", zip);

		if (region != null) {
			_addParameter(sb, "adminDistrict", region.getRegionCode());
		}

		if (country != null) {
			_addParameter(sb, "countryRegion", country.getA2());
		}

		sb.setIndex(sb.index() - 1);

		return sb.toString();
	}

	private volatile String _apiKey;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL)
	private volatile CountryLocalService _countryLocalService;

	@Reference
	private volatile GroupLocalService _groupLocalService;

	@Reference
	private Http _http;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL)
	private volatile RegionLocalService _regionLocalService;

}