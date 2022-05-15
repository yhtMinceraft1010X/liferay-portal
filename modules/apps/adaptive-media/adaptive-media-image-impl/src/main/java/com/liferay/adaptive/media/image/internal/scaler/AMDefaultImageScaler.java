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

package com.liferay.adaptive.media.image.internal.scaler;

import com.liferay.adaptive.media.exception.AMRuntimeException;
import com.liferay.adaptive.media.image.configuration.AMImageConfigurationEntry;
import com.liferay.adaptive.media.image.internal.processor.util.TiffOrientationTransformer;
import com.liferay.adaptive.media.image.internal.util.RenderedImageUtil;
import com.liferay.adaptive.media.image.scaler.AMImageScaledImage;
import com.liferay.adaptive.media.image.scaler.AMImageScaler;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.image.ImageTool;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.util.GetterUtil;

import java.awt.image.RenderedImage;

import java.io.InputStream;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	immediate = true, property = "mime.type=*", service = AMImageScaler.class
)
public class AMDefaultImageScaler implements AMImageScaler {

	@Override
	public AMImageScaledImage scaleImage(
		FileVersion fileVersion,
		AMImageConfigurationEntry amImageConfigurationEntry) {

		try {
			RenderedImage renderedImage = TiffOrientationTransformer.transform(
				() -> _getInputStream(fileVersion));

			Map<String, String> properties =
				amImageConfigurationEntry.getProperties();

			int maxHeight = GetterUtil.getInteger(properties.get("max-height"));
			int maxWidth = GetterUtil.getInteger(properties.get("max-width"));

			RenderedImage scaledRenderedImage = _imageTool.scale(
				renderedImage, maxHeight, maxWidth);

			return new AMImageScaledImageImpl(
				RenderedImageUtil.getRenderedImageContentStream(
					scaledRenderedImage, fileVersion.getMimeType()),
				scaledRenderedImage.getHeight(), fileVersion.getMimeType(),
				scaledRenderedImage.getWidth());
		}
		catch (AMRuntimeException.IOException | PortalException exception) {
			throw new AMRuntimeException.IOException(
				StringBundler.concat(
					"Unable to scale file entry ", fileVersion.getFileEntryId(),
					" to match adaptive media configuration ",
					amImageConfigurationEntry.getUUID()),
				exception);
		}
	}

	private InputStream _getInputStream(FileVersion fileVersion) {
		try {
			return fileVersion.getContentStream(false);
		}
		catch (PortalException portalException) {
			throw new AMRuntimeException.IOException(portalException);
		}
	}

	@Reference
	private ImageTool _imageTool;

}