/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.vldap.server.internal.directory.builder;

import com.liferay.vldap.server.internal.BaseVLDAPTestCase;
import com.liferay.vldap.server.internal.directory.FilterConstraint;
import com.liferay.vldap.server.internal.directory.ldap.Directory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

/**
 * @author William Newbury
 */
public abstract class BaseDirectoryBuilderTestCase extends BaseVLDAPTestCase {

	protected void doTestBuildDirectories() throws Exception {
		_testBuildDirectoriesWithDefaultFilterConstraints();
		_testBuildDirectoriesWithInvalidFilterConstraints();
		_testBuildDirectoriesWithNullFilterConstraints();
	}

	protected void doTestValidAttributes(String name, String... values) {
		for (String value : values) {
			Assert.assertTrue(directoryBuilder.isValidAttribute(name, value));
		}
	}

	protected DirectoryBuilder directoryBuilder;

	private void _testBuildDirectoriesWithDefaultFilterConstraints()
		throws Exception {

		List<Directory> directories = directoryBuilder.buildDirectories(
			searchBase, Arrays.asList(new FilterConstraint()));

		Directory directory = directories.get(0);

		Assert.assertNotNull(directory);
	}

	private void _testBuildDirectoriesWithInvalidFilterConstraints()
		throws Exception {

		List<FilterConstraint> filterConstraints = new ArrayList<>();

		FilterConstraint filterConstraint = new FilterConstraint();

		filterConstraint.addAttribute("test", "test");

		filterConstraints.add(filterConstraint);

		List<Directory> directories = directoryBuilder.buildDirectories(
			searchBase, filterConstraints);

		Assert.assertTrue(directories.isEmpty());
	}

	private void _testBuildDirectoriesWithNullFilterConstraints()
		throws Exception {

		List<Directory> directories = directoryBuilder.buildDirectories(
			searchBase, new ArrayList<FilterConstraint>());

		Directory directory = directories.get(0);

		Assert.assertNotNull(directory);
	}

}