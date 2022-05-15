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

package com.liferay.asset.auto.tagger.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Alejandro Tardín
 */
@ExtendedObjectClassDefinition(
	category = "assets", scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.asset.auto.tagger.internal.configuration.AssetAutoTaggerCompanyConfiguration",
	localization = "content/Language",
	name = "asset-auto-tagger-company-configuration-name"
)
public interface AssetAutoTaggerCompanyConfiguration {

	/**
	 * Enables asset auto tagging.
	 */
	@Meta.AD(
		deflt = "true", name = "enabled[asset-auto-tagger-service]",
		required = false
	)
	public boolean enabled();

	@Meta.AD(
		deflt = "false", description = "update-auto-tags-description",
		name = "update-auto-tags", required = false
	)
	public boolean updateAutoTags();

	/**
	 * Specifies the maximum number of tags that can be added for a given asset.
	 */
	@Meta.AD(
		description = "company-maximum-number-of-tags-per-asset-description",
		name = "maximum-number-of-tags", required = false
	)
	public int maximumNumberOfTagsPerAsset();

}