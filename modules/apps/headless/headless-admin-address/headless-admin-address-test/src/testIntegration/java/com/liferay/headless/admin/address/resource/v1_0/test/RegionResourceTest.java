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

package com.liferay.headless.admin.address.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.headless.admin.address.client.dto.v1_0.Region;
import com.liferay.headless.admin.address.client.http.HttpInvoker;
import com.liferay.headless.admin.address.client.pagination.Page;
import com.liferay.headless.admin.address.client.pagination.Pagination;
import com.liferay.headless.admin.address.client.serdes.v1_0.RegionSerDes;
import com.liferay.petra.function.UnsafeTriConsumer;
import com.liferay.portal.kernel.exception.DuplicateRegionException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Country;
import com.liferay.portal.kernel.service.CountryLocalService;
import com.liferay.portal.kernel.service.RegionLocalService;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.test.rule.Inject;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Drew Brokke
 */
@RunWith(Arquillian.class)
public class RegionResourceTest extends BaseRegionResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_country = _countryLocalService.addCountry(
			"a1", "a11", true, RandomTestUtil.randomBoolean(),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), RandomTestUtil.randomDouble(),
			RandomTestUtil.randomBoolean(), RandomTestUtil.randomBoolean(),
			RandomTestUtil.randomBoolean(),
			ServiceContextTestUtil.getServiceContext());
	}

	@Override
	@Test
	public void testGetRegionsPage() throws Exception {
		String keywords = RandomTestUtil.randomString();

		Page<Region> page = regionResource.getRegionsPage(
			null, keywords, Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		Region region1 = _addRegion(keywords);
		Region region2 = _addRegion(keywords);

		page = regionResource.getRegionsPage(
			null, keywords, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(region1, (List<Region>)page.getItems());
		assertContains(region2, (List<Region>)page.getItems());
		assertValid(page);
	}

	@Override
	@Test
	public void testGraphQLGetRegionsPage() throws Exception {
		GraphQLField graphQLField = new GraphQLField(
			"regions",
			HashMapBuilder.<String, Object>put(
				"page", 1
			).put(
				"pageSize", 10
			).put(
				"sort", "\"position:desc\""
			).build(),
			new GraphQLField("items", getGraphQLFields()),
			new GraphQLField("page"), new GraphQLField("totalCount"));

		JSONObject regionsJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/regions");

		long totalCount = regionsJSONObject.getLong("totalCount");

		Region region1 = testGraphQLGetRegionsPage_addRegion();
		Region region2 = testGraphQLGetRegionsPage_addRegion();

		regionsJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/regions");

		Assert.assertEquals(
			totalCount + 2, regionsJSONObject.getLong("totalCount"));

		assertContains(
			region1,
			Arrays.asList(
				RegionSerDes.toDTOs(regionsJSONObject.getString("items"))));
		assertContains(
			region2,
			Arrays.asList(
				RegionSerDes.toDTOs(regionsJSONObject.getString("items"))));
	}

	@Override
	@Test
	public void testPostCountryRegion() throws Exception {
		super.testPostCountryRegion();

		Region existingRegion = _addRegion(randomRegion());

		Region region = randomRegion();

		region.setName((String)null);

		_testPostCountryRegionProblem(region, null);

		region.setName("");

		_testPostCountryRegionProblem(region, null);

		region = randomRegion();

		region.setRegionCode((String)null);

		_testPostCountryRegionProblem(region, null);

		region.setRegionCode("");

		_testPostCountryRegionProblem(region, null);

		region.setRegionCode(existingRegion.getRegionCode());

		_testPostCountryRegionProblem(region, DuplicateRegionException.class);
	}

	@Override
	@Test
	public void testPutRegion() throws Exception {
		super.testPutRegion();

		Region region1 = _addRegion(randomRegion());

		Region randomRegion = randomRegion();

		randomRegion.setName((String)null);

		_testPutRegionProblem(region1.getId(), randomRegion, null);

		randomRegion.setName("");

		_testPutRegionProblem(region1.getId(), randomRegion, null);

		randomRegion = randomRegion();

		randomRegion.setRegionCode((String)null);

		_testPutRegionProblem(region1.getId(), randomRegion, null);

		randomRegion.setRegionCode("");

		_testPutRegionProblem(region1.getId(), randomRegion, null);

		Region region2 = _addRegion(randomRegion());

		randomRegion.setRegionCode(region2.getRegionCode());

		_testPutRegionProblem(
			region1.getId(), randomRegion, DuplicateRegionException.class);
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"name", "position", "regionCode"};
	}

	@Override
	protected Region randomRegion() throws Exception {
		Region region = super.randomRegion();

		region.setCountryId(_country.getCountryId());

		return region;
	}

	@Override
	protected Region testDeleteRegion_addRegion() throws Exception {
		return _addRegion(randomRegion());
	}

	@Override
	protected Region testGetCountryRegionByRegionCode_addRegion()
		throws Exception {

		return _addRegion(randomRegion());
	}

	@Override
	protected Long testGetCountryRegionsPage_getCountryId() throws Exception {
		return _country.getCountryId();
	}

	@Override
	protected Long testGetCountryRegionsPage_getIrrelevantCountryId()
		throws Exception {

		Country country = _countryLocalService.addCountry(
			"a2", "a22", true, RandomTestUtil.randomBoolean(),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), RandomTestUtil.randomDouble(),
			RandomTestUtil.randomBoolean(), RandomTestUtil.randomBoolean(),
			RandomTestUtil.randomBoolean(),
			ServiceContextTestUtil.getServiceContext());

		return country.getCountryId();
	}

	@Override
	protected Region testGetRegion_addRegion() throws Exception {
		return _addRegion(randomRegion());
	}

	@Override
	protected Region testGetRegionsPage_addRegion(Region region)
		throws Exception {

		return _addRegion(region);
	}

	@Override
	protected void testGetRegionsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Region, Region, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Region region1 = randomRegion();
		Region region2 = randomRegion();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, region1, region2);
		}

		String keywords = RandomTestUtil.randomString();

		region1.setName(keywords + region1.getName());

		region1 = testGetRegionsPage_addRegion(region1);

		region2.setName(keywords + RandomTestUtil.randomString());

		region2 = testGetRegionsPage_addRegion(region2);

		for (EntityField entityField : entityFields) {
			Page<Region> ascPage = regionResource.getRegionsPage(
				null, keywords, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(region1, region2),
				(List<Region>)ascPage.getItems());

			Page<Region> descPage = regionResource.getRegionsPage(
				null, keywords, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(region2, region1),
				(List<Region>)descPage.getItems());
		}
	}

	@Override
	protected Region testGraphQLRegion_addRegion() throws Exception {
		Region region = randomRegion();

		region.setPosition(Double.MAX_VALUE);

		return _addRegion(region);
	}

	@Override
	protected Region testPatchRegion_addRegion() throws Exception {
		return _addRegion(randomRegion());
	}

	@Override
	protected Region testPutRegion_addRegion() throws Exception {
		return _addRegion(randomRegion());
	}

	private Region _addRegion(Region region) throws Exception {
		return regionResource.postCountryRegion(
			_country.getCountryId(), region);
	}

	private Region _addRegion(String keyword) throws Exception {
		Region region = randomRegion();

		region.setName(keyword + RandomTestUtil.randomString());

		return _addRegion(region);
	}

	private <T extends Exception> void _testPostCountryRegionProblem(
			Region region, Class<T> exceptionClass)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			regionResource.postCountryRegionHttpResponse(
				_country.getCountryId(), region);

		Assert.assertEquals(
			Response.Status.BAD_REQUEST.getStatusCode(),
			httpResponse.getStatusCode());

		if (exceptionClass != null) {
			JSONObject jsonObject = _jsonFactory.createJSONObject(
				httpResponse.getContent());

			Assert.assertEquals(
				exceptionClass.getSimpleName(), jsonObject.get("type"));
		}
	}

	private <T extends Exception> void _testPutRegionProblem(
			Long regionId, Region region, Class<T> exceptionClass)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			regionResource.putRegionHttpResponse(regionId, region);

		Assert.assertEquals(
			Response.Status.BAD_REQUEST.getStatusCode(),
			httpResponse.getStatusCode());

		if (exceptionClass != null) {
			JSONObject jsonObject = _jsonFactory.createJSONObject(
				httpResponse.getContent());

			Assert.assertEquals(
				exceptionClass.getSimpleName(), jsonObject.get("type"));
		}
	}

	@DeleteAfterTestRun
	private Country _country;

	@Inject
	private CountryLocalService _countryLocalService;

	@Inject
	private JSONFactory _jsonFactory;

	@Inject
	private RegionLocalService _regionLocalService;

}