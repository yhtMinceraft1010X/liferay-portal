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

package com.liferay.osb.commerce.provisioning.internal.cloud.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author Ivica Cardic
 */
public abstract class BaseClientImpl implements Client {

	public BaseClientImpl() {
		_closeableHttpClient = _createCloseableHttpClient();
	}

	@Override
	public void destroy() {
		try {
			_closeableHttpClient.close();
		}
		catch (IOException ioException) {
			throw new SystemException(ioException);
		}
	}

	protected <T> T convert(String content, Class<T> valueType) {
		try {
			return _objectMapper.readValue(content, valueType);
		}
		catch (JsonProcessingException jsonProcessingException) {
			throw new SystemException(jsonProcessingException);
		}
	}

	protected <T> T convert(String content, TypeReference<T> valueTypeRef) {
		try {
			return _objectMapper.readValue(content, valueTypeRef);
		}
		catch (JsonProcessingException jsonProcessingException) {
			throw new SystemException(jsonProcessingException);
		}
	}

	protected String execute(
		String authorizationHeader, HttpUriRequest httpUriRequest) {

		if (authorizationHeader != null) {
			httpUriRequest.setHeader(
				HttpHeaders.AUTHORIZATION, authorizationHeader);
		}

		try (CloseableHttpResponse closeableHttpResponse =
				_closeableHttpClient.execute(httpUriRequest)) {

			return getResponseContent(closeableHttpResponse);
		}
		catch (IOException ioException) {
			throw new SystemException(ioException);
		}
	}

	protected void executeDelete(String authorizationHeader, String uri) {
		execute(authorizationHeader, new HttpDelete(uri));
	}

	protected <T> T executeGet(
		String authorizationHeader, String uri, Class<T> valueType) {

		return convert(
			execute(authorizationHeader, new HttpGet(uri)), valueType);
	}

	protected <T> T executeGet(
		String authorizationHeader, TypeReference<T> valueTypeReference,
		String uri) {

		return convert(
			execute(authorizationHeader, new HttpGet(uri)), valueTypeReference);
	}

	protected <T> T executePost(
		String authorizationHeader, Object content, String uri,
		Class<T> valueType) {

		HttpPost httpPost = new HttpPost(uri);

		httpPost.setEntity(
			new StringEntity(
				writeValueAsString(content), ContentType.APPLICATION_JSON));

		return convert(execute(authorizationHeader, httpPost), valueType);
	}

	protected <T> T executeUpdate(
		String authorizationHeader, Object content, Class<T> valueType,
		String uri) {

		HttpPatch httpPatch = new HttpPatch(uri);

		httpPatch.setEntity(
			new StringEntity(
				writeValueAsString(content), ContentType.APPLICATION_JSON));

		return convert(execute(authorizationHeader, httpPatch), valueType);
	}

	protected String getBasicAuthorizationHeader(
		String password, String userName) {

		String authorization = userName + ":" + password;

		String encodedAuthorization = Base64.encode(
			authorization.getBytes(StandardCharsets.ISO_8859_1));

		return "Basic " + encodedAuthorization;
	}

	protected String getBearerAuthorizationHeader(
		String clientId, String clientSecret, String virtualHostURI) {

		HttpPost httpPost = new HttpPost(virtualHostURI + "/o/oauth2/token");

		try {
			httpPost.setEntity(
				new UrlEncodedFormEntity(
					new ArrayList<NameValuePair>() {
						{
							add(new BasicNameValuePair("client_id", clientId));
							add(
								new BasicNameValuePair(
									"client_secret", clientSecret));
							add(
								new BasicNameValuePair(
									"grant_type", "client_credentials"));
						}
					},
					"UTF-8"));
		}
		catch (UnsupportedEncodingException unsupportedEncodingException) {
			throw new SystemException(unsupportedEncodingException);
		}

		Map<String, String> result = convert(
			execute(null, httpPost), Map.class);

		return "Bearer: " + result.get("access_token");
	}

	protected String getResponseContent(
			CloseableHttpResponse closeableHttpResponse)
		throws IOException {

		StatusLine statusLine = closeableHttpResponse.getStatusLine();

		if (statusLine.getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
			throw new SystemException(statusLine.toString());
		}

		return EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
	}

	protected String writeValueAsString(Object value) {
		try {
			return _objectMapper.writeValueAsString(value);
		}
		catch (JsonProcessingException jsonProcessingException) {
			throw new SystemException(jsonProcessingException);
		}
	}

	private CloseableHttpClient _createCloseableHttpClient() {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
			new PoolingHttpClientConnectionManager();

		httpClientBuilder.setConnectionManager(
			poolingHttpClientConnectionManager);

		poolingHttpClientConnectionManager.setMaxTotal(100);

		httpClientBuilder.useSystemProperties();

		return httpClientBuilder.build();
	}

	private static final ObjectMapper _objectMapper = new ObjectMapper() {
		{
			configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			SimpleFilterProvider simpleFilterProvider =
				new SimpleFilterProvider();

			setFilterProvider(simpleFilterProvider.setFailOnUnknownId(false));
		}
	};

	private final CloseableHttpClient _closeableHttpClient;

}