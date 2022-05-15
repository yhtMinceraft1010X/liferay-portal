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

package com.liferay.portal.encryptor;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.encryptor.Encryptor;
import com.liferay.portal.kernel.encryptor.EncryptorException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.Digester;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.SystemProperties;

import java.security.Key;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 * @author Mika Koivisto
 */
@Component(service = Encryptor.class)
public class EncryptorImpl implements Encryptor {

	public static final String ENCODING = Digester.ENCODING;

	public static final String IBM_PROVIDER_CLASS =
		"com.ibm.crypto.provider.IBMJCE";

	public static final String KEY_ALGORITHM = StringUtil.toUpperCase(
		GetterUtil.getString(
			PropsUtil.get(PropsKeys.COMPANY_ENCRYPTION_ALGORITHM)));

	public static final int KEY_SIZE = GetterUtil.getInteger(
		PropsUtil.get(PropsKeys.COMPANY_ENCRYPTION_KEY_SIZE));

	public static final String PROVIDER_CLASS = GetterUtil.getString(
		SystemProperties.get(EncryptorImpl.class.getName() + ".provider.class"),
		EncryptorImpl.SUN_PROVIDER_CLASS);

	public static final String SUN_PROVIDER_CLASS =
		"com.sun.crypto.provider.SunJCE";

	@Override
	public String decrypt(Key key, String encryptedString)
		throws EncryptorException {

		byte[] encryptedBytes = Base64.decode(encryptedString);

		return _decryptUnencodedAsString(key, encryptedBytes);
	}

	@Override
	public byte[] decryptUnencodedAsBytes(Key key, byte[] encryptedBytes)
		throws EncryptorException {

		String algorithm = key.getAlgorithm();

		String cacheKey = algorithm + StringPool.POUND + key.toString();

		Cipher cipher = _decryptCipherMap.get(cacheKey);

		try {
			if (cipher == null) {
				cipher = Cipher.getInstance(algorithm);

				cipher.init(Cipher.DECRYPT_MODE, key);

				_decryptCipherMap.put(cacheKey, cipher);
			}

			synchronized (cipher) {
				return cipher.doFinal(encryptedBytes);
			}
		}
		catch (Exception exception) {
			throw new EncryptorException(exception);
		}
	}

	@Override
	public Key deserializeKey(String base64String) {
		byte[] bytes = Base64.decode(base64String);

		return new SecretKeySpec(bytes, EncryptorImpl.KEY_ALGORITHM);
	}

	@Override
	public String encrypt(Key key, String plainText) throws EncryptorException {
		if (key == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("Skip encrypting based on a null key");
			}

			return plainText;
		}

		byte[] encryptedBytes = encryptUnencoded(key, plainText);

		return Base64.encode(encryptedBytes);
	}

	@Override
	public byte[] encryptUnencoded(Key key, byte[] plainBytes)
		throws EncryptorException {

		String algorithm = key.getAlgorithm();

		String cacheKey = algorithm + StringPool.POUND + key.toString();

		Cipher cipher = _encryptCipherMap.get(cacheKey);

		try {
			if (cipher == null) {
				cipher = Cipher.getInstance(algorithm);

				cipher.init(Cipher.ENCRYPT_MODE, key);

				_encryptCipherMap.put(cacheKey, cipher);
			}

			synchronized (cipher) {
				return cipher.doFinal(plainBytes);
			}
		}
		catch (Exception exception) {
			throw new EncryptorException(exception);
		}
	}

	@Override
	public byte[] encryptUnencoded(Key key, String plainText)
		throws EncryptorException {

		try {
			byte[] decryptedBytes = plainText.getBytes(ENCODING);

			return encryptUnencoded(key, decryptedBytes);
		}
		catch (Exception exception) {
			throw new EncryptorException(exception);
		}
	}

	@Override
	public Key generateKey() throws EncryptorException {
		return _generateKey(KEY_ALGORITHM);
	}

	@Override
	public String serializeKey(Key key) {
		return Base64.encode(key.getEncoded());
	}

	private String _decryptUnencodedAsString(Key key, byte[] encryptedBytes)
		throws EncryptorException {

		try {
			byte[] decryptedBytes = decryptUnencodedAsBytes(
				key, encryptedBytes);

			return new String(decryptedBytes, ENCODING);
		}
		catch (Exception exception) {
			throw new EncryptorException(exception);
		}
	}

	private Key _generateKey(String algorithm) throws EncryptorException {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);

			keyGenerator.init(KEY_SIZE, new SecureRandom());

			return keyGenerator.generateKey();
		}
		catch (Exception exception) {
			throw new EncryptorException(exception);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(EncryptorImpl.class);

	private static final Provider _provider;

	static {
		try {
			Class<?> providerClass = null;

			try {
				providerClass = Class.forName(PROVIDER_CLASS);
			}
			catch (ClassNotFoundException classNotFoundException1) {
				try {
					if (PROVIDER_CLASS.equals(SUN_PROVIDER_CLASS)) {
						providerClass = Class.forName(IBM_PROVIDER_CLASS);
					}
				}
				catch (ClassNotFoundException classNotFoundException2) {
					classNotFoundException1.addSuppressed(
						classNotFoundException2);
				}

				if (providerClass == null) {
					throw new IllegalStateException(
						"Unable to find provider class: " + PROVIDER_CLASS,
						classNotFoundException1);
				}
			}

			_provider = (Provider)providerClass.newInstance();

			Security.addProvider(_provider);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new ExceptionInInitializerError(reflectiveOperationException);
		}
	}

	private final Map<String, Cipher> _decryptCipherMap =
		new ConcurrentHashMap<>(1, 1F, 1);
	private final Map<String, Cipher> _encryptCipherMap =
		new ConcurrentHashMap<>(1, 1F, 1);

}