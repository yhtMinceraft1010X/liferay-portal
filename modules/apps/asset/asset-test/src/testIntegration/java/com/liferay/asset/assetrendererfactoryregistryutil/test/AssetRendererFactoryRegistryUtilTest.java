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

package com.liferay.asset.assetrendererfactoryregistryutil.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.test.util.asset.renderer.factory.TestAssetRendererFactory;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PortalImpl;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Peter Fellwock
 */
@RunWith(Arquillian.class)
public class AssetRendererFactoryRegistryUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testGetAssetRendererFactories() {
		String className = TestAssetRendererFactory.class.getName();

		List<AssetRendererFactory<?>> assetRendererFactories =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactories(1);

		Stream<AssetRendererFactory<?>> assetRendererFactoriesStream =
			assetRendererFactories.stream();

		Assert.assertEquals(
			1,
			assetRendererFactoriesStream.map(
				AssetRendererFactory::getClassName
			).filter(
				className::equals
			).count());
	}

	@Test
	public void testGetAssetRendererFactoryByClassName() {
		String className = TestAssetRendererFactory.class.getName();

		AssetRendererFactory<?> assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				className);

		Assert.assertEquals(className, assetRendererFactory.getClassName());
	}

	@Test
	public void testGetAssetRendererFactoryByClassNameId() {
		PortalImpl portalImpl = new PortalImpl();

		AssetRendererFactory<?> assetRendererFactory =
			AssetRendererFactoryRegistryUtil.
				getAssetRendererFactoryByClassNameId(
					portalImpl.getClassNameId(TestAssetRendererFactory.class));

		Assert.assertEquals(
			TestAssetRendererFactory.class.getName(),
			assetRendererFactory.getClassName());
	}

	@Test
	public void testGetAssetRendererFactoryByType() {
		AssetRendererFactory<?> assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByType(
				TestAssetRendererFactory.class.getName());

		Assert.assertEquals(
			TestAssetRendererFactory.class.getName(),
			assetRendererFactory.getClassName());
	}

	@Test
	public void testGetClassNameIds1() {
		List<Long> classNameIdsList = ListUtil.fromArray(
			AssetRendererFactoryRegistryUtil.getClassNameIds(1));

		Assert.assertTrue(
			classNameIdsList.toString(),
			classNameIdsList.contains(Long.valueOf(1234567890)));
	}

	@Test
	public void testGetClassNameIds2() {
		List<Long> classNameIdsList = ListUtil.fromArray(
			AssetRendererFactoryRegistryUtil.getClassNameIds(1, true));

		Assert.assertTrue(
			classNameIdsList.toString(),
			classNameIdsList.contains(Long.valueOf(1234567890)));
	}

	@Test
	public void testGetClassNameIds3() {
		List<Long> classNameIdsList = ListUtil.fromArray(
			AssetRendererFactoryRegistryUtil.getClassNameIds(1, false));

		Assert.assertTrue(
			classNameIdsList.toString(),
			classNameIdsList.contains(Long.valueOf(1234567890)));
	}

}