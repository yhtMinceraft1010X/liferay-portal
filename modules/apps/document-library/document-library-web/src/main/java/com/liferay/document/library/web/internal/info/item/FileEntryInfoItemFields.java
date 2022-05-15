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

package com.liferay.document.library.web.internal.info.item;

import com.liferay.info.field.InfoField;
import com.liferay.info.field.type.DateInfoFieldType;
import com.liferay.info.field.type.ImageInfoFieldType;
import com.liferay.info.field.type.TextInfoFieldType;
import com.liferay.info.field.type.URLInfoFieldType;
import com.liferay.info.localized.InfoLocalizedValue;
import com.liferay.portal.kernel.repository.model.FileEntry;

/**
 * @author Alejandro Tardín
 * @author Jorge Ferrer
 */
public class FileEntryInfoItemFields {

	public static final InfoField<TextInfoFieldType> authorNameInfoField =
		BuilderHolder._builder.infoFieldType(
			TextInfoFieldType.INSTANCE
		).name(
			"authorName"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "author-name")
		).build();
	public static final InfoField<ImageInfoFieldType>
		authorProfileImageInfoField = BuilderHolder._builder.infoFieldType(
			ImageInfoFieldType.INSTANCE
		).name(
			"authorProfileImage"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "author-profile-image")
		).build();
	public static final InfoField<DateInfoFieldType> createDateInfoField =
		BuilderHolder._builder.infoFieldType(
			DateInfoFieldType.INSTANCE
		).name(
			"createDate"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "create-date")
		).build();
	public static final InfoField<TextInfoFieldType> descriptionInfoField =
		BuilderHolder._builder.infoFieldType(
			TextInfoFieldType.INSTANCE
		).name(
			"description"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "description")
		).build();
	public static final InfoField<URLInfoFieldType> displayPageURLInfoField =
		BuilderHolder._builder.infoFieldType(
			URLInfoFieldType.INSTANCE
		).name(
			"displayPageURL"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				"com.liferay.asset.info.display.impl", "display-page-url")
		).build();
	public static final InfoField<URLInfoFieldType> downloadURLInfoField =
		BuilderHolder._builder.infoFieldType(
			URLInfoFieldType.INSTANCE
		).name(
			"downloadURL"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "download-url")
		).build();
	public static final InfoField<TextInfoFieldType> fileNameInfoField =
		BuilderHolder._builder.infoFieldType(
			TextInfoFieldType.INSTANCE
		).name(
			"fileName"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "file-name")
		).build();
	public static final InfoField<ImageInfoFieldType> fileURLInfoField =
		BuilderHolder._builder.infoFieldType(
			ImageInfoFieldType.INSTANCE
		).name(
			"fileURL"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "file-url")
		).build();
	public static final InfoField<TextInfoFieldType> mimeTypeInfoField =
		BuilderHolder._builder.infoFieldType(
			TextInfoFieldType.INSTANCE
		).name(
			"mimeType"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "mime-type")
		).build();
	public static final InfoField<DateInfoFieldType> modifiedDateInfoField =
		BuilderHolder._builder.infoFieldType(
			DateInfoFieldType.INSTANCE
		).name(
			"modifiedDate"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "modified-date")
		).build();
	public static final InfoField<ImageInfoFieldType> previewImageInfoField =
		BuilderHolder._builder.infoFieldType(
			ImageInfoFieldType.INSTANCE
		).name(
			"previewImage"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "preview-image")
		).build();
	public static final InfoField<URLInfoFieldType> previewURLInfoField =
		BuilderHolder._builder.infoFieldType(
			URLInfoFieldType.INSTANCE
		).name(
			"previewURL"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "preview-url")
		).build();
	public static final InfoField<DateInfoFieldType> publishDateInfoField =
		BuilderHolder._builder.infoFieldType(
			DateInfoFieldType.INSTANCE
		).name(
			"publishDate"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "publish-date")
		).build();
	public static final InfoField<TextInfoFieldType> sizeInfoField =
		BuilderHolder._builder.infoFieldType(
			TextInfoFieldType.INSTANCE
		).name(
			"size"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(FileEntryInfoItemFields.class, "size")
		).build();
	public static final InfoField<TextInfoFieldType> titleInfoField =
		BuilderHolder._builder.infoFieldType(
			TextInfoFieldType.INSTANCE
		).name(
			"title"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(FileEntryInfoItemFields.class, "title")
		).build();
	public static final InfoField<TextInfoFieldType> versionInfoField =
		BuilderHolder._builder.infoFieldType(
			TextInfoFieldType.INSTANCE
		).name(
			"version"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				FileEntryInfoItemFields.class, "version")
		).build();

	private static class BuilderHolder {

		private static final InfoField.NamespacedBuilder _builder =
			InfoField.builder(FileEntry.class.getSimpleName());

	}

}