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

package com.liferay.asset.vocabulary.item.selector;

import com.liferay.item.selector.ItemSelectorReturnType;

/**
 * This return type should return the following information of a vocabulary as a
 * JSON object:
 *
 * <ul>
 * <li>
 * <code>assetVocabularyId</code>: The vocabularyId of the selected vocabulary
 * </li>
 * <li>
 * <code>groupId</code>: The vocabularyId of the selected vocabulary
 * </li>
 * <li>
 * <code>title</code>: The localized title of the selected vocabulary
 * </li>
 * <li>
 * <code>uuid</code>: The uuid of the selected vocabulary
 * </li>
 * </ul>
 *
 * @author Lourdes Fernández Besada
 */
public class AssetVocabularyItemSelectorReturnType
	implements ItemSelectorReturnType {
}