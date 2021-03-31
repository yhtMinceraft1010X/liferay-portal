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

package com.liferay.osb.saml.internal.util;

import com.liferay.portal.kernel.security.SecureRandom;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.DigesterUtil;

import java.nio.ByteBuffer;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Marta Medio
 */
public class SymmetricEncryptor {

	public static String decryptData(String preSharedKey, String data)
		throws Exception {

		ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.decode(data));

		byte[] gmcParameterSpecSrc = new byte[_GCM_NONCE_LENGTH];

		byteBuffer.get(gmcParameterSpecSrc);

		byte[] cipherInput = new byte[byteBuffer.remaining()];

		byteBuffer.get(cipherInput);

		Cipher cipher = _getCipher(
			Cipher.DECRYPT_MODE, gmcParameterSpecSrc, preSharedKey);

		return new String(cipher.doFinal(cipherInput));
	}

	public static String encryptData(String preSharedKey, String data)
		throws Exception {

		byte[] gmcParameterSpecSrc = new byte[_GCM_NONCE_LENGTH];

		SecureRandom secureRandom = new SecureRandom();

		secureRandom.nextBytes(gmcParameterSpecSrc);

		Cipher cipher = _getCipher(
			Cipher.ENCRYPT_MODE, gmcParameterSpecSrc, preSharedKey);

		byte[] cipherOutput = cipher.doFinal(data.getBytes());

		ByteBuffer byteBuffer = ByteBuffer.allocate(
			gmcParameterSpecSrc.length + cipherOutput.length);

		byteBuffer.put(gmcParameterSpecSrc);

		byteBuffer.put(cipherOutput);

		return Base64.encode(byteBuffer.array());
	}

	private static Cipher _getCipher(
			int encryptMode, byte[] gmcParameterSpecSrc, String preSharedKey)
		throws Exception {

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

		cipher.init(
			encryptMode,
			new SecretKeySpec(
				DigesterUtil.digestRaw("SHA-256", preSharedKey), "AES"),
			new GCMParameterSpec(_GCM_TAG_LENGTH, gmcParameterSpecSrc));

		return cipher;
	}

	private static final int _GCM_NONCE_LENGTH = 12;

	private static final int _GCM_TAG_LENGTH = 128;

}