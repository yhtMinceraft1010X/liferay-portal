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

package com.liferay.content.dashboard.document.library.internal.item.action;

import com.liferay.content.dashboard.item.action.ContentDashboardItemAction;
import com.liferay.document.library.constants.DLContentTypes;
import com.liferay.info.field.InfoFieldValue;
import com.liferay.info.item.InfoItemFieldValues;
import com.liferay.info.item.provider.InfoItemFieldValuesProvider;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.repository.model.FileEntry;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Cristina González
 */
public class DownloadFileEntryContentDashboardItemAction
	implements ContentDashboardItemAction {

	public DownloadFileEntryContentDashboardItemAction(
		FileEntry fileEntry,
		InfoItemFieldValuesProvider<FileEntry> infoItemFieldValuesProvider,
		Language language) {

		_fileEntry = fileEntry;
		_infoItemFieldValuesProvider = infoItemFieldValuesProvider;
		_language = language;
	}

	@Override
	public String getIcon() {
		return "download";
	}

	@Override
	public String getLabel(Locale locale) {
		return _language.get(locale, "download");
	}

	@Override
	public String getName() {
		return "download";
	}

	@Override
	public Type getType() {
		return Type.DOWNLOAD;
	}

	@Override
	public String getURL() {
		if (Objects.equals(
				_fileEntry.getMimeType(),
				DLContentTypes.VIDEO_EXTERNAL_SHORTCUT)) {

			return StringPool.BLANK;
		}

		InfoItemFieldValues infoItemFieldValues =
			_infoItemFieldValuesProvider.getInfoItemFieldValues(_fileEntry);

		return Optional.ofNullable(
			infoItemFieldValues.getInfoFieldValue("downloadURL")
		).map(
			InfoFieldValue::getValue
		).orElse(
			StringPool.BLANK
		).toString();
	}

	@Override
	public String getURL(Locale locale) {
		return getURL();
	}

	private final FileEntry _fileEntry;
	private final InfoItemFieldValuesProvider<FileEntry>
		_infoItemFieldValuesProvider;
	private final Language _language;

}