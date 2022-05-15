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

package com.liferay.portal.search.internal.indexer;

import com.liferay.portal.search.spi.model.query.contributor.KeywordQueryContributor;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author André de Oliveira
 */
public class ModelKeywordQueryContributorsHolderImpl
	implements ModelKeywordQueryContributorsHolder {

	public ModelKeywordQueryContributorsHolderImpl(
		Iterable<KeywordQueryContributor> keywordQueryContributors) {

		_keywordQueryContributors = keywordQueryContributors;
	}

	@Override
	public Stream<KeywordQueryContributor> stream(
		Collection<String> excludes, Collection<String> includes) {

		return IncludeExcludeUtil.stream(
			StreamSupport.stream(
				_keywordQueryContributors.spliterator(), false),
			includes, excludes, object -> getClassName(object));
	}

	protected String getClassName(Object object) {
		Class<?> clazz = object.getClass();

		return clazz.getName();
	}

	private final Iterable<KeywordQueryContributor> _keywordQueryContributors;

}