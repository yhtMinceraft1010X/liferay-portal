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

package com.liferay.portal.image;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.ImageResolutionException;
import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageTool;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Image;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.impl.ImageImpl;
import com.liferay.portal.module.framework.ModuleFrameworkUtil;
import com.liferay.portal.util.FileImpl;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URL;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.launch.Framework;

/**
 * @author Brian Wing Shun Chan
 * @author Alexander Chow
 * @author Shuyang Zhou
 */
public class ImageToolImpl implements ImageTool {

	public static ImageTool getInstance() {
		return _instance;
	}

	@Override
	public BufferedImage convertImageType(BufferedImage sourceImage, int type) {
		BufferedImage targetImage = new BufferedImage(
			sourceImage.getWidth(), sourceImage.getHeight(), type);

		Graphics2D graphics2D = targetImage.createGraphics();

		graphics2D.drawRenderedImage(sourceImage, null);

		graphics2D.dispose();

		return targetImage;
	}

	@Override
	public RenderedImage crop(
		RenderedImage renderedImage, int height, int width, int x, int y) {

		Rectangle rectangle = new Rectangle(x, y, width, height);

		Rectangle croppedRectangle = rectangle.intersection(
			new Rectangle(renderedImage.getWidth(), renderedImage.getHeight()));

		BufferedImage bufferedImage = getBufferedImage(renderedImage);

		return bufferedImage.getSubimage(
			croppedRectangle.x, croppedRectangle.y, croppedRectangle.width,
			croppedRectangle.height);
	}

	@Override
	public void encodeGIF(
			RenderedImage renderedImage, OutputStream outputStream)
		throws IOException {

		ImageIO.write(renderedImage, TYPE_GIF, outputStream);
	}

	@Override
	public void encodeWBMP(
			RenderedImage renderedImage, OutputStream outputStream)
		throws IOException {

		BufferedImage bufferedImage = getBufferedImage(renderedImage);

		SampleModel sampleModel = bufferedImage.getSampleModel();

		int type = sampleModel.getDataType();

		if ((bufferedImage.getType() != BufferedImage.TYPE_BYTE_BINARY) ||
			(type < DataBuffer.TYPE_BYTE) || (type > DataBuffer.TYPE_INT) ||
			(sampleModel.getNumBands() != 1) ||
			(sampleModel.getSampleSize(0) != 1)) {

			BufferedImage binaryImage = new BufferedImage(
				bufferedImage.getWidth(), bufferedImage.getHeight(),
				BufferedImage.TYPE_BYTE_BINARY);

			Graphics graphics = binaryImage.getGraphics();

			graphics.drawImage(bufferedImage, 0, 0, null);

			renderedImage = binaryImage;
		}

		if (!ImageIO.write(renderedImage, "wbmp", outputStream)) {

			// See http://www.jguru.com/faq/view.jsp?EID=127723

			outputStream.write(0);
			outputStream.write(0);
			outputStream.write(toMultiByte(bufferedImage.getWidth()));
			outputStream.write(toMultiByte(bufferedImage.getHeight()));

			Raster data = bufferedImage.getData();

			DataBuffer dataBuffer = data.getDataBuffer();

			int size = dataBuffer.getSize();

			for (int i = 0; i < size; i++) {
				outputStream.write((byte)dataBuffer.getElem(i));
			}
		}
	}

	@Override
	public RenderedImage flipHorizontal(RenderedImage renderedImage) {
		BufferedImage bufferedImage = getBufferedImage(renderedImage);

		AffineTransform affineTransform = AffineTransform.getScaleInstance(
			-1.0, 1.0);

		affineTransform.translate(-bufferedImage.getWidth(), 0);

		AffineTransformOp affineTransformOp = new AffineTransformOp(
			affineTransform, null);

		return affineTransformOp.filter(bufferedImage, null);
	}

	@Override
	public RenderedImage flipVertical(RenderedImage renderedImage) {
		BufferedImage bufferedImage = getBufferedImage(renderedImage);

		AffineTransform affineTransform = AffineTransform.getScaleInstance(
			1.0, -1.0);

		affineTransform.translate(0, -bufferedImage.getHeight());

		AffineTransformOp affineTransformOp = new AffineTransformOp(
			affineTransform, null);

		return affineTransformOp.filter(bufferedImage, null);
	}

	@Override
	public BufferedImage getBufferedImage(RenderedImage renderedImage) {
		if (renderedImage instanceof BufferedImage) {
			return (BufferedImage)renderedImage;
		}

		ColorModel colorModel = renderedImage.getColorModel();

		WritableRaster writableRaster =
			colorModel.createCompatibleWritableRaster(
				renderedImage.getWidth(), renderedImage.getHeight());

		Hashtable<String, Object> properties = new Hashtable<>();

		String[] keys = renderedImage.getPropertyNames();

		if (!ArrayUtil.isEmpty(keys)) {
			for (String key : keys) {
				properties.put(key, renderedImage.getProperty(key));
			}
		}

		BufferedImage bufferedImage = new BufferedImage(
			colorModel, writableRaster, colorModel.isAlphaPremultiplied(),
			properties);

		renderedImage.copyData(writableRaster);

		return bufferedImage;
	}

	@Override
	public byte[] getBytes(RenderedImage renderedImage, String contentType)
		throws IOException {

		UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
			new UnsyncByteArrayOutputStream();

		write(renderedImage, contentType, unsyncByteArrayOutputStream);

		return unsyncByteArrayOutputStream.toByteArray();
	}

	@Override
	public Image getDefaultCompanyLogo() {
		if (_defaultCompanyLogo != null) {
			return _defaultCompanyLogo;
		}

		ClassLoader classLoader = ImageTool.class.getClassLoader();

		try {
			InputStream inputStream = null;

			String imageDefaultCompanyLogo = PropsUtil.get(
				PropsKeys.IMAGE_DEFAULT_COMPANY_LOGO);

			int index = imageDefaultCompanyLogo.indexOf(CharPool.SEMICOLON);

			if (index == -1) {
				inputStream = classLoader.getResourceAsStream(
					PropsUtil.get(PropsKeys.IMAGE_DEFAULT_COMPANY_LOGO));
			}
			else {
				String bundleIdString = imageDefaultCompanyLogo.substring(
					0, index);

				int bundleId = GetterUtil.getInteger(bundleIdString, -1);

				String name = imageDefaultCompanyLogo.substring(index + 1);

				if (bundleId < 0) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							"Fallback to portal class loader because of " +
								"invalid bundle ID " + bundleIdString);
					}

					inputStream = classLoader.getResourceAsStream(name);
				}
				else {
					Framework framework = ModuleFrameworkUtil.getFramework();

					BundleContext bundleContext = framework.getBundleContext();

					Bundle bundle = bundleContext.getBundle(bundleId);

					if (bundle != null) {
						URL url = bundle.getResource(name);

						inputStream = url.openStream();
					}
				}
			}

			if (inputStream == null) {
				_log.error("Default company logo is not available");
			}

			_defaultCompanyLogo = getImage(inputStream);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to configure the default company logo: " +
					exception.getMessage());
		}

		return _defaultCompanyLogo;
	}

	@Override
	public Image getDefaultOrganizationLogo() {
		if (_defaultOrganizationLogo != null) {
			return _defaultOrganizationLogo;
		}

		ClassLoader classLoader = ImageTool.class.getClassLoader();

		try {
			InputStream inputStream = classLoader.getResourceAsStream(
				PropsUtil.get(PropsKeys.IMAGE_DEFAULT_ORGANIZATION_LOGO));

			if (inputStream == null) {
				_log.error("Default organization logo is not available");
			}

			_defaultOrganizationLogo = getImage(inputStream);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to configure the default organization logo: " +
					exception.getMessage());
		}

		return _defaultOrganizationLogo;
	}

	@Override
	public Image getDefaultSpacer() {
		if (_defaultSpacer != null) {
			return _defaultSpacer;
		}

		ClassLoader classLoader = ImageTool.class.getClassLoader();

		try {
			InputStream inputStream = classLoader.getResourceAsStream(
				PropsUtil.get(PropsKeys.IMAGE_DEFAULT_SPACER));

			if (inputStream == null) {
				_log.error("Default spacer is not available");
			}

			_defaultSpacer = getImage(inputStream);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to configure the default spacer: " +
					exception.getMessage());
		}

		return _defaultSpacer;
	}

	@Override
	public Image getDefaultUserFemalePortrait() {
		if (_defaultUserFemalePortrait != null) {
			return _defaultUserFemalePortrait;
		}

		ClassLoader classLoader = ImageTool.class.getClassLoader();

		try {
			InputStream inputStream = classLoader.getResourceAsStream(
				PropsUtil.get(PropsKeys.IMAGE_DEFAULT_USER_FEMALE_PORTRAIT));

			if (inputStream == null) {
				_log.error("Default user female portrait is not available");
			}

			_defaultUserFemalePortrait = getImage(inputStream);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to configure the default user female portrait: " +
					exception.getMessage());
		}

		return _defaultUserFemalePortrait;
	}

	@Override
	public Image getDefaultUserMalePortrait() {
		if (_defaultUserMalePortrait != null) {
			return _defaultUserMalePortrait;
		}

		ClassLoader classLoader = ImageTool.class.getClassLoader();

		try {
			InputStream inputStream = classLoader.getResourceAsStream(
				PropsUtil.get(PropsKeys.IMAGE_DEFAULT_USER_MALE_PORTRAIT));

			if (inputStream == null) {
				_log.error("Default user male portrait is not available");
			}

			_defaultUserMalePortrait = getImage(inputStream);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to configure the default user male portrait: " +
					exception.getMessage());
		}

		return _defaultUserMalePortrait;
	}

	@Override
	public Image getDefaultUserPortrait() {
		if (_defaultUserPortrait != null) {
			return _defaultUserPortrait;
		}

		ClassLoader classLoader = ImageTool.class.getClassLoader();

		try {
			InputStream inputStream = classLoader.getResourceAsStream(
				PropsUtil.get(PropsKeys.IMAGE_DEFAULT_USER_PORTRAIT));

			if (inputStream == null) {
				_log.error("Default user portrait is not available");
			}

			_defaultUserPortrait = getImage(inputStream);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to configure the default user portrait: " +
					exception.getMessage());
		}

		return _defaultUserPortrait;
	}

	@Override
	public Image getImage(byte[] bytes)
		throws ImageResolutionException, IOException {

		if (bytes == null) {
			return null;
		}

		ImageBag imageBag = read(bytes);

		RenderedImage renderedImage = imageBag.getRenderedImage();

		if (renderedImage == null) {
			throw new IOException("Unable to decode image");
		}

		int size = bytes.length;

		Image image = new ImageImpl();

		image.setTextObj(bytes);
		image.setType(imageBag.getType());
		image.setHeight(renderedImage.getHeight());
		image.setWidth(renderedImage.getWidth());
		image.setSize(size);

		return image;
	}

	@Override
	public Image getImage(File file)
		throws ImageResolutionException, IOException {

		return getImage(_fileImpl.getBytes(file));
	}

	@Override
	public Image getImage(InputStream inputStream)
		throws ImageResolutionException, IOException {

		return getImage(_fileImpl.getBytes(inputStream, -1, true));
	}

	@Override
	public Image getImage(InputStream inputStream, boolean cleanUpStream)
		throws ImageResolutionException, IOException {

		return getImage(_fileImpl.getBytes(inputStream, -1, cleanUpStream));
	}

	@Override
	public boolean isNullOrDefaultSpacer(byte[] bytes) {
		if (ArrayUtil.isEmpty(bytes) ||
			Arrays.equals(bytes, getDefaultSpacer().getTextObj())) {

			return true;
		}

		return false;
	}

	@Override
	public ImageBag read(byte[] bytes)
		throws ImageResolutionException, IOException {

		String formatName = null;
		ImageInputStream imageInputStream = null;
		Queue<ImageReader> imageReaders = new LinkedList<>();
		RenderedImage renderedImage = null;

		try {
			imageInputStream = ImageIO.createImageInputStream(
				new ByteArrayInputStream(bytes));

			Iterator<ImageReader> iterator = ImageIO.getImageReaders(
				imageInputStream);

			while ((renderedImage == null) && iterator.hasNext()) {
				ImageReader imageReader = iterator.next();

				imageReaders.offer(imageReader);

				try {
					imageReader.setInput(imageInputStream);

					int height = imageReader.getHeight(0);
					int width = imageReader.getWidth(0);

					if (((PropsValues.IMAGE_TOOL_IMAGE_MAX_HEIGHT > 0) &&
						 (height > PropsValues.IMAGE_TOOL_IMAGE_MAX_HEIGHT)) ||
						((PropsValues.IMAGE_TOOL_IMAGE_MAX_WIDTH > 0) &&
						 (width > PropsValues.IMAGE_TOOL_IMAGE_MAX_WIDTH))) {

						throw new ImageResolutionException(
							StringBundler.concat(
								"Image's dimensions (", height, " px high and ",
								width, " px wide) exceed max dimensions (",
								PropsValues.IMAGE_TOOL_IMAGE_MAX_HEIGHT,
								" px high and ",
								PropsValues.IMAGE_TOOL_IMAGE_MAX_WIDTH,
								" px wide)"));
					}

					renderedImage = imageReader.read(0);
				}
				catch (ArrayIndexOutOfBoundsException | IOException exception) {
					if (_log.isDebugEnabled()) {
						_log.debug(exception);
					}

					continue;
				}

				formatName = StringUtil.toLowerCase(
					imageReader.getFormatName());
			}

			if (renderedImage == null) {
				throw new IOException("Unsupported image type");
			}
		}
		finally {
			while (!imageReaders.isEmpty()) {
				ImageReader imageReader = imageReaders.poll();

				imageReader.dispose();
			}

			if (imageInputStream != null) {
				imageInputStream.close();
			}
		}

		String type = TYPE_JPEG;

		if (formatName.contains(TYPE_BMP)) {
			type = TYPE_BMP;
		}
		else if (formatName.contains(TYPE_GIF)) {
			type = TYPE_GIF;
		}
		else if (formatName.contains("jpeg") ||
				 StringUtil.equalsIgnoreCase(type, "jpeg")) {

			type = TYPE_JPEG;
		}
		else if (formatName.contains(TYPE_PNG)) {
			type = TYPE_PNG;
		}
		else if (formatName.contains(TYPE_TIFF)) {
			type = TYPE_TIFF;
		}
		else {
			throw new IllegalArgumentException(type + " is not supported");
		}

		return new ImageBag(renderedImage, type);
	}

	@Override
	public ImageBag read(File file)
		throws ImageResolutionException, IOException {

		return read(_fileImpl.getBytes(file));
	}

	@Override
	public ImageBag read(InputStream inputStream)
		throws ImageResolutionException, IOException {

		return read(_fileImpl.getBytes(inputStream));
	}

	@Override
	public RenderedImage rotate(RenderedImage renderedImage, int degrees) {
		BufferedImage bufferedImage = getBufferedImage(renderedImage);

		int imageWidth = bufferedImage.getWidth();
		int imageHeight = bufferedImage.getHeight();

		double radians = Math.toRadians(degrees);

		double absoluteSin = Math.abs(Math.sin(radians));
		double absoluteCos = Math.abs(Math.cos(radians));

		int rotatedImageWidth = (int)Math.floor(
			(imageWidth * absoluteCos) + (imageHeight * absoluteSin));
		int rotatedImageHeight = (int)Math.floor(
			(imageHeight * absoluteCos) + (imageWidth * absoluteSin));

		BufferedImage rotatedBufferedImage = new BufferedImage(
			rotatedImageWidth, rotatedImageHeight, bufferedImage.getType());

		AffineTransform affineTransform = new AffineTransform();

		affineTransform.translate(
			rotatedImageWidth / 2, rotatedImageHeight / 2);
		affineTransform.rotate(radians);
		affineTransform.translate(imageWidth / -2, imageHeight / -2);

		Graphics2D graphics2D = rotatedBufferedImage.createGraphics();

		graphics2D.drawImage(bufferedImage, affineTransform, null);

		graphics2D.dispose();

		return rotatedBufferedImage;
	}

	@Override
	public RenderedImage scale(RenderedImage renderedImage, int width) {
		if (width <= 0) {
			return renderedImage;
		}

		int imageHeight = renderedImage.getHeight();

		int imageWidth = renderedImage.getWidth();

		double factor = (double)width / imageWidth;

		int scaledHeight = (int)Math.round(factor * imageHeight);

		int scaledWidth = width;

		return doScale(renderedImage, scaledHeight, scaledWidth);
	}

	@Override
	public RenderedImage scale(
		RenderedImage renderedImage, int maxHeight, int maxWidth) {

		int imageHeight = renderedImage.getHeight();
		int imageWidth = renderedImage.getWidth();

		if (maxHeight == 0) {
			maxHeight = imageHeight;
		}

		if (maxWidth == 0) {
			maxWidth = imageWidth;
		}

		if ((imageHeight <= maxHeight) && (imageWidth <= maxWidth)) {
			return renderedImage;
		}

		double factor = Math.min(
			(double)maxHeight / imageHeight, (double)maxWidth / imageWidth);

		int scaledHeight = Math.max(1, (int)Math.round(factor * imageHeight));
		int scaledWidth = Math.max(1, (int)Math.round(factor * imageWidth));

		return doScale(renderedImage, scaledHeight, scaledWidth);
	}

	@Override
	public void write(
			RenderedImage renderedImage, String contentType,
			OutputStream outputStream)
		throws IOException {

		if (contentType.contains(TYPE_BMP)) {
			ImageIO.write(renderedImage, "bmp", outputStream);
		}
		else if (contentType.contains(TYPE_GIF)) {
			encodeGIF(renderedImage, outputStream);
		}
		else if (contentType.contains(TYPE_JPEG) ||
				 contentType.contains("jpeg")) {

			ImageIO.write(renderedImage, "jpeg", outputStream);
		}
		else if (contentType.contains(TYPE_PNG)) {
			ImageIO.write(renderedImage, TYPE_PNG, outputStream);
		}
		else if (contentType.contains(TYPE_TIFF) ||
				 contentType.contains("tif")) {

			ImageIO.write(renderedImage, "tiff", outputStream);
		}
	}

	protected RenderedImage doScale(
		RenderedImage renderedImage, int scaledHeight, int scaledWidth) {

		// See http://www.oracle.com/technetwork/java/index-137037.html

		BufferedImage originalBufferedImage = getBufferedImage(renderedImage);

		int type = originalBufferedImage.getType();

		if ((type == BufferedImage.TYPE_BYTE_INDEXED) ||
			(type == BufferedImage.TYPE_CUSTOM)) {

			type = BufferedImage.TYPE_INT_ARGB;
		}

		BufferedImage scaledBufferedImage = new BufferedImage(
			scaledWidth, scaledHeight, type);

		int originalHeight = originalBufferedImage.getHeight();
		int originalWidth = originalBufferedImage.getWidth();

		if (((scaledHeight * 2) >= originalHeight) &&
			((scaledWidth * 2) >= originalWidth)) {

			Graphics2D scaledGraphics2D = scaledBufferedImage.createGraphics();

			scaledGraphics2D.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
			scaledGraphics2D.setRenderingHint(
				RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			scaledGraphics2D.setRenderingHint(
				RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

			scaledGraphics2D.drawImage(
				originalBufferedImage, 0, 0, scaledWidth, scaledHeight, null);

			scaledGraphics2D.dispose();

			return scaledBufferedImage;
		}

		BufferedImage tempBufferedImage = new BufferedImage(
			originalWidth, originalHeight, scaledBufferedImage.getType());

		Graphics2D tempGraphics2D = tempBufferedImage.createGraphics();

		RenderingHints renderingHints = new RenderingHints(
			RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		tempGraphics2D.setRenderingHints(renderingHints);

		ColorModel originalColorModel = originalBufferedImage.getColorModel();

		if (originalColorModel.hasAlpha()) {
			tempGraphics2D.setComposite(AlphaComposite.Src);
		}

		int startHeight = scaledHeight;
		int startWidth = scaledWidth;

		while ((startHeight < originalHeight) && (startWidth < originalWidth)) {
			startHeight *= 2;
			startWidth *= 2;
		}

		originalHeight = startHeight / 2;
		originalWidth = startWidth / 2;

		tempGraphics2D.drawImage(
			originalBufferedImage, 0, 0, originalWidth, originalHeight, null);

		while ((originalHeight >= (scaledHeight * 2)) &&
			   (originalWidth >= (scaledWidth * 2))) {

			originalHeight /= 2;

			if (originalHeight < scaledHeight) {
				originalHeight = scaledHeight;
			}

			originalWidth /= 2;

			if (originalWidth < scaledWidth) {
				originalWidth = scaledWidth;
			}

			tempGraphics2D.drawImage(
				tempBufferedImage, 0, 0, originalWidth, originalHeight, 0, 0,
				originalWidth * 2, originalHeight * 2, null);
		}

		tempGraphics2D.dispose();

		Graphics2D scaledGraphics2D = scaledBufferedImage.createGraphics();

		scaledGraphics2D.drawImage(
			tempBufferedImage, 0, 0, scaledWidth, scaledHeight, 0, 0,
			originalWidth, originalHeight, null);

		scaledGraphics2D.dispose();

		return scaledBufferedImage;
	}

	protected byte[] toMultiByte(int intValue) {
		int numBits = 32;
		int mask = 0x80000000;

		while ((mask != 0) && ((intValue & mask) == 0)) {
			numBits--;
			mask >>>= 1;
		}

		int numBitsLeft = numBits;

		byte[] multiBytes = new byte[(numBitsLeft + 6) / 7];

		int maxIndex = multiBytes.length - 1;

		for (int b = 0; b <= maxIndex; b++) {
			multiBytes[b] = (byte)((intValue >>> ((maxIndex - b) * 7)) & 0x7f);

			if (b != maxIndex) {
				multiBytes[b] |= (byte)0x80;
			}
		}

		return multiBytes;
	}

	private ImageToolImpl() {
		ImageIO.setUseCache(PropsValues.IMAGE_IO_USE_DISK_CACHE);
	}

	private static final Log _log = LogFactoryUtil.getLog(ImageToolImpl.class);

	private static final ImageTool _instance = new ImageToolImpl();

	private static final FileImpl _fileImpl = FileImpl.getInstance();

	private Image _defaultCompanyLogo;
	private Image _defaultOrganizationLogo;
	private Image _defaultSpacer;
	private Image _defaultUserFemalePortrait;
	private Image _defaultUserMalePortrait;
	private Image _defaultUserPortrait;

}