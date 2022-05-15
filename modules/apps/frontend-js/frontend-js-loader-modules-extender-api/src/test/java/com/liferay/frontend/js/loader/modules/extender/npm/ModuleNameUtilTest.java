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

package com.liferay.frontend.js.loader.modules.extender.npm;

import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Iván Zaera Avellón
 */
public class ModuleNameUtilTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testGetDependencyPath() {
		Assert.assertEquals(
			"dep", ModuleNameUtil.getDependencyPath("a-dir/a-module", "dep"));
		Assert.assertEquals(
			"dep",
			ModuleNameUtil.getDependencyPath("a-dir/a-module", "../dep"));
		Assert.assertEquals(
			"a-dir/dep",
			ModuleNameUtil.getDependencyPath("a-dir/a-module", "./dep"));
		Assert.assertEquals(
			"a-dir/dep",
			ModuleNameUtil.getDependencyPath(
				"a-dir/other-dir/a-module", "../dep"));
		Assert.assertEquals(
			"a-dir/other-dir",
			ModuleNameUtil.getDependencyPath("a-dir/other-dir/a-module", "."));
		Assert.assertEquals(
			"a-dir",
			ModuleNameUtil.getDependencyPath("a-dir/other-dir/a-module", ".."));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDependencyPathWithInvalidDependency() {
		ModuleNameUtil.getDependencyPath("a-module", "../dep");
	}

	@Test
	public void testGetPackageName() {
		Assert.assertEquals(
			"a-package", ModuleNameUtil.getPackageName("a-package"));

		Assert.assertEquals(
			"a-package",
			ModuleNameUtil.getPackageName("a-package/a-folder/a-module"));

		Assert.assertEquals(
			"a-package",
			ModuleNameUtil.getPackageName("a-package/a-folder/a-module.js"));

		Assert.assertNull(ModuleNameUtil.getPackageName("./a-module"));
	}

	@Test
	public void testGetPackageNameNoModule() throws Exception {
		Assert.assertEquals(
			"mypackage", ModuleNameUtil.getPackageName("mypackage"));
	}

	@Test
	public void testGetPackageNameScoped() throws Exception {
		Assert.assertEquals(
			"@myscope/mypackage",
			ModuleNameUtil.getPackageName("@myscope/mypackage/lib/mymodule"));
	}

	@Test
	public void testGetPackageNameScopedNoModule() throws Exception {
		Assert.assertEquals(
			"@myscope/mypackage",
			ModuleNameUtil.getPackageName("@myscope/mypackage"));
	}

	@Test
	public void testGetPackagePath() {
		Assert.assertNull(ModuleNameUtil.getPackagePath("a-package"));

		Assert.assertEquals(
			"a-module", ModuleNameUtil.getPackagePath("a-package/a-module"));

		Assert.assertEquals(
			"a-module.js",
			ModuleNameUtil.getPackagePath("a-package/a-module.js"));

		Assert.assertEquals(
			"a-folder/a-module",
			ModuleNameUtil.getPackagePath("a-package/a-folder/a-module"));

		Assert.assertEquals(
			"a-folder/a-module.js",
			ModuleNameUtil.getPackagePath("a-package/a-folder/a-module.js"));

		Assert.assertNull(ModuleNameUtil.getPackagePath("./a-module"));
	}

	@Test
	public void testGetPackagePathNoModule() throws Exception {
		Assert.assertNull(ModuleNameUtil.getPackagePath("mypackage"));
	}

	@Test
	public void testGetPackagePathScoped() throws Exception {
		Assert.assertEquals(
			"lib/mymodule",
			ModuleNameUtil.getPackagePath("@myscope/mypackage/lib/mymodule"));
	}

	@Test
	public void testGetPackagePathScopedNoModule() throws Exception {
		Assert.assertNull(ModuleNameUtil.getPackagePath("@myscope/mypackage"));
	}

	@Test
	public void testToModuleName() {
		Assert.assertEquals(
			"a-module", ModuleNameUtil.toModuleName("a-module"));

		Assert.assertEquals(
			"a-module", ModuleNameUtil.toModuleName("a-module.js"));

		Assert.assertEquals(
			"a-package/a-module",
			ModuleNameUtil.toModuleName("a-package/a-module"));

		Assert.assertEquals(
			"a-package/a-module",
			ModuleNameUtil.toModuleName("a-package/a-module.js"));

		Assert.assertEquals(
			"a-package/a-folder/a-module",
			ModuleNameUtil.toModuleName("a-package/a-folder/a-module"));

		Assert.assertEquals(
			"a-package/a-folder/a-module",
			ModuleNameUtil.toModuleName("a-package/a-folder/a-module.js"));
	}

	@Test
	public void testToModuleNameWithNonjsExtensions() {
		Assert.assertEquals(
			"a-module.es", ModuleNameUtil.toModuleName("a-module.es"));

		Assert.assertEquals(
			"a-folder/a-module.es",
			ModuleNameUtil.toModuleName("a-folder/a-module.es"));
	}

	@Test
	public void testToModuleNameWithRelativePaths() {
		Assert.assertEquals(
			"./a-module", ModuleNameUtil.toModuleName("./a-module"));

		Assert.assertEquals(
			"./a-module", ModuleNameUtil.toModuleName("./a-module.js"));

		Assert.assertEquals(
			"../a-module", ModuleNameUtil.toModuleName("../a-module"));

		Assert.assertEquals(
			"../a-module", ModuleNameUtil.toModuleName("../a-module.js"));
	}

}