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

package com.liferay.portal.store.gcs;

import com.google.api.gax.paging.Page;
import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import com.liferay.document.library.kernel.exception.NoSuchFileException;
import com.liferay.document.library.kernel.store.Store;
import com.liferay.document.library.kernel.util.comparator.VersionNumberComparator;
import com.liferay.petra.io.StreamUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.store.gcs.configuration.GCSStoreConfiguration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.channels.Channels;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import org.threeten.bp.Duration;

/**
 * @author Shanon Mathai
 * @author Alicia García
 * @author Adolfo Pérez
 */
@Component(
	configurationPid = "com.liferay.portal.store.gcs.configuration.GCSStoreConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true,
	property = "store.type=com.liferay.portal.store.gcs.GCSStore",
	service = Store.class
)
public class GCSStore implements Store {

	@Override
	public void addFile(
			long companyId, long repositoryId, String fileName,
			String versionLabel, InputStream inputStream)
		throws PortalException {

		if (hasFile(companyId, repositoryId, fileName, versionLabel)) {
			deleteFile(companyId, repositoryId, fileName, versionLabel);
		}

		String path = _getFileVersionKey(
			companyId, repositoryId, fileName, versionLabel);

		BlobInfo blobInfo = BlobInfo.newBuilder(
			_getBucketInfo(), path
		).build();

		try (WriteChannel writeChannel = _getWriteChannel(blobInfo)) {
			StreamUtil.transfer(
				inputStream, Channels.newOutputStream(writeChannel));
		}
		catch (IOException ioException) {
			throw new PortalException("Unable to add file", ioException);
		}
	}

	@Override
	public void deleteDirectory(
		long companyId, long repositoryId, String dirName) {

		String path = _getDirectoryKey(companyId, repositoryId, dirName);

		Page<Blob> blobPage = _gcsStore.list(
			_gcsStoreConfiguration.bucketName(),
			Storage.BlobListOption.prefix(path));

		Iterable<Blob> blobs = blobPage.iterateAll();

		blobs.forEach(this::_deleteBlob);
	}

	@Override
	public void deleteFile(
		long companyId, long repositoryId, String fileName,
		String versionLabel) {

		_gcsStore.delete(
			BlobId.of(
				_gcsStoreConfiguration.bucketName(),
				_getHeadVersionLabel(
					companyId, repositoryId, fileName, versionLabel)));
	}

	@Override
	public InputStream getFileAsStream(
			long companyId, long repositoryId, String fileName,
			String versionLabel)
		throws NoSuchFileException {

		Blob blob = _gcsStore.get(
			BlobId.of(
				_gcsStoreConfiguration.bucketName(),
				_getHeadVersionLabel(
					companyId, repositoryId, fileName, versionLabel)));

		if (blob == null) {
			throw new NoSuchFileException(
				companyId, repositoryId, fileName, versionLabel);
		}

		return Channels.newInputStream(_getReadChannel(blob));
	}

	@Override
	public String[] getFileNames(
		long companyId, long repositoryId, String dirName) {

		Stream<String> stream = Arrays.stream(
			_getFilePaths(companyId, repositoryId, dirName));

		String prefix = StringBundler.concat(
			companyId, StringPool.SLASH, repositoryId, StringPool.SLASH);

		return stream.map(
			filePath -> filePath.substring(
				filePath.indexOf(prefix) + prefix.length(),
				filePath.lastIndexOf(StringPool.SLASH))
		).toArray(
			String[]::new
		);
	}

	@Override
	public long getFileSize(
			long companyId, long repositoryId, String fileName,
			String versionLabel)
		throws PortalException {

		String headVersionLabel = _getHeadVersionLabel(
			companyId, repositoryId, fileName, versionLabel);

		Blob blob = _gcsStore.get(
			BlobId.of(_gcsStoreConfiguration.bucketName(), headVersionLabel));

		if (blob == null) {
			throw new NoSuchFileException(
				"No file exists for " + headVersionLabel);
		}

		return blob.getSize();
	}

	@Override
	public String[] getFileVersions(
		long companyId, long repositoryId, String fileName) {

		Stream<String> stream = Arrays.stream(
			_getFilePaths(companyId, repositoryId, fileName));

		return stream.map(
			path -> {
				String[] parts = StringUtil.split(path, CharPool.SLASH);

				return parts[parts.length - 1];
			}
		).toArray(
			String[]::new
		);
	}

	@Override
	public boolean hasFile(
		long companyId, long repositoryId, String fileName,
		String versionLabel) {

		String path = _getFileVersionKey(
			companyId, repositoryId, fileName, versionLabel);

		Page<Blob> blobPage = _gcsStore.list(
			_gcsStoreConfiguration.bucketName(),
			Storage.BlobListOption.pageSize(1),
			Storage.BlobListOption.prefix(path));

		Iterable<Blob> filesFoundIterable = blobPage.getValues();

		Iterator<Blob> filesFoundIterator = filesFoundIterable.iterator();

		return filesFoundIterator.hasNext();
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		try {
			_gcsStoreConfiguration = ConfigurableUtil.createConfigurable(
				GCSStoreConfiguration.class, properties);

			_initEncryption();

			_initGCSStore();
		}
		catch (PortalException portalException) {
			throw new IllegalStateException(
				"Unable to initialize GCS store", portalException);
		}
	}

	private void _deleteBlob(Blob blob) {
		if (_blobDecryptSourceOption == null) {
			blob.delete();
		}
		else {
			blob.delete(_blobDecryptSourceOption);
		}
	}

	private BucketInfo _getBucketInfo() {
		if (_bucketInfo == null) {
			_bucketInfo = BucketInfo.newBuilder(
				_gcsStoreConfiguration.bucketName()
			).build();
		}

		return _bucketInfo;
	}

	private String _getDirectoryKey(
		long companyId, long repositoryId, String folderName) {

		return _getFileKey(companyId, repositoryId, folderName);
	}

	private String _getFileKey(
		long companyId, long repositoryId, String fileName) {

		return StringBundler.concat(
			companyId, StringPool.SLASH, repositoryId, StringPool.SLASH,
			fileName);
	}

	private String[] _getFilePaths(
		long companyId, long repositoryId, String dirName) {

		Bucket bucket = _gcsStore.get(_gcsStoreConfiguration.bucketName());

		String path = null;

		if (Validator.isNull(dirName) ||
			dirName.equals(StringPool.FORWARD_SLASH)) {

			path = _getRepositoryKey(companyId, repositoryId);
		}
		else {
			path = _getDirectoryKey(companyId, repositoryId, dirName);
		}

		Page<Blob> blobPage = bucket.list(Storage.BlobListOption.prefix(path));

		Iterable<Blob> blobs = blobPage.iterateAll();

		Stream<Blob> blobStream = StreamSupport.stream(
			blobs.spliterator(), false);

		return blobStream.map(
			BlobInfo::getName
		).toArray(
			String[]::new
		);
	}

	private String _getFileVersionKey(
		long companyId, long repositoryId, String fileName,
		String versionLabel) {

		return StringBundler.concat(
			companyId, StringPool.SLASH, repositoryId, StringPool.SLASH,
			fileName, StringPool.SLASH, versionLabel);
	}

	private String _getHeadVersionLabel(
		long companyId, long repositoryId, String fileName,
		String versionLabel) {

		if (Validator.isNotNull(versionLabel)) {
			return _getFileVersionKey(
				companyId, repositoryId, fileName, versionLabel);
		}

		String path = _getFileKey(companyId, repositoryId, fileName);

		String[] fileNames = _getFilePaths(companyId, repositoryId, path);

		if ((fileNames == null) || (fileNames.length == 0)) {
			if (_log.isDebugEnabled()) {
				_log.debug("Using default version for " + path);
			}

			return _getFileVersionKey(
				companyId, repositoryId, fileName, VERSION_DEFAULT);
		}

		List<String> fileNamesList = Arrays.asList(fileNames);

		fileNamesList.sort(new VersionNumberComparator());

		return fileNamesList.get(fileNamesList.size() - 1);
	}

	private ReadChannel _getReadChannel(Blob blob) {
		if (_blobDecryptSourceOption == null) {
			return blob.reader();
		}

		return blob.reader(_blobDecryptSourceOption);
	}

	private String _getRepositoryKey(long companyId, long repositoryId) {
		return companyId + StringPool.SLASH + repositoryId;
	}

	private WriteChannel _getWriteChannel(BlobInfo blobInfo) {
		if (_blobEncryptWriteOption == null) {
			return _gcsStore.writer(blobInfo);
		}

		return _gcsStore.writer(blobInfo, _blobEncryptWriteOption);
	}

	private void _initEncryption() {
		String aes256Key = _gcsStoreConfiguration.aes256Key();

		if (Validator.isNull(aes256Key)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Files are not encrypted because the portal property " +
						"\"dl.store.gcs.aes256.key\" is not set");
			}

			_blobDecryptSourceOption = null;
			_blobEncryptWriteOption = null;
		}
		else {
			_blobDecryptSourceOption = Blob.BlobSourceOption.decryptionKey(
				aes256Key);
			_blobEncryptWriteOption = Storage.BlobWriteOption.encryptionKey(
				aes256Key);
		}
	}

	private void _initGCSStore() throws PortalException {
		String serviceAccountKey = _gcsStoreConfiguration.serviceAccountKey();

		try (InputStream inputStream = new ByteArrayInputStream(
				serviceAccountKey.getBytes())) {

			_googleCredentials = ServiceAccountCredentials.fromStream(
				inputStream);
		}
		catch (IOException ioException) {
			throw new PortalException(
				"Unable to authenticate with GCS", ioException);
		}

		StorageOptions storageOptions = StorageOptions.newBuilder(
		).setCredentials(
			_googleCredentials
		).setRetrySettings(
			RetrySettings.newBuilder(
			).setInitialRetryDelay(
				Duration.ofMillis(_gcsStoreConfiguration.initialRetryDelay())
			).setInitialRpcTimeout(
				Duration.ofMillis(_gcsStoreConfiguration.initialRPCTimeout())
			).setJittered(
				_gcsStoreConfiguration.retryJitter()
			).setMaxAttempts(
				_gcsStoreConfiguration.maxRetryAttempts()
			).setMaxRetryDelay(
				Duration.ofMillis(_gcsStoreConfiguration.maxRetryDelay())
			).setMaxRpcTimeout(
				Duration.ofMillis(_gcsStoreConfiguration.maxRPCTimeout())
			).setRetryDelayMultiplier(
				_gcsStoreConfiguration.retryDelayMultiplier()
			).setRpcTimeoutMultiplier(
				_gcsStoreConfiguration.rpcTimeoutMultiplier()
			).build()
		).build();

		_gcsStore = storageOptions.getService();
	}

	private static final Log _log = LogFactoryUtil.getLog(GCSStore.class);

	private Blob.BlobSourceOption _blobDecryptSourceOption;
	private Storage.BlobWriteOption _blobEncryptWriteOption;
	private BucketInfo _bucketInfo;
	private Storage _gcsStore;
	private volatile GCSStoreConfiguration _gcsStoreConfiguration;
	private GoogleCredentials _googleCredentials;

}