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

package com.liferay.dynamic.data.mapping.data.provider.instance.internal;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderRequest;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponse;
import com.liferay.dynamic.data.mapping.storage.DDMStorageAdapterTracker;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Leonardo Barros
 */
public class DDMStorageTypesDataProviderTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		_ddmStorageTypesDataProvider = new DDMStorageTypesDataProvider();

		_ddmStorageTypesDataProvider.ddmStorageAdapterTracker =
			_ddmStorageAdapterTracker;
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetSettings() {
		_ddmStorageTypesDataProvider.getSettings();
	}

	@Test
	public void testMultipleStorageAdapter() {
		Set<String> expectedSet = new TreeSet<String>() {
			{
				add("json");
				add("txt");
				add("xml");
			}
		};

		_testStorageTypes(expectedSet);
	}

	@Test
	public void testSingleStorageAdapter() {
		Set<String> expectedSet = new TreeSet<String>() {
			{
				add("json");
			}
		};

		_testStorageTypes(expectedSet);
	}

	private void _testStorageTypes(Set<String> expectedSet) {
		Mockito.when(
			_ddmStorageAdapterTracker.getDDMStorageAdapterTypes()
		).thenReturn(
			expectedSet
		);

		List<KeyValuePair> keyValuePairs = new ArrayList<>();

		Stream<String> stream = expectedSet.stream();

		stream.filter(
			type -> !type.equals("json")
		).map(
			type -> new KeyValuePair(type, type)
		).forEach(
			keyValuePairs::add
		);

		DDMDataProviderRequest.Builder builder =
			DDMDataProviderRequest.Builder.newBuilder();

		DDMDataProviderResponse ddmDataProviderResponse =
			_ddmStorageTypesDataProvider.getData(builder.build());

		Assert.assertTrue(ddmDataProviderResponse.hasOutput("Default-Output"));

		Optional<List<KeyValuePair>> optional =
			ddmDataProviderResponse.getOutputOptional(
				"Default-Output", List.class);

		Assert.assertTrue(optional.isPresent());

		Assert.assertEquals(keyValuePairs, optional.get());
	}

	private static final DDMStorageAdapterTracker _ddmStorageAdapterTracker =
		Mockito.mock(DDMStorageAdapterTracker.class);
	private static DDMStorageTypesDataProvider _ddmStorageTypesDataProvider;

}