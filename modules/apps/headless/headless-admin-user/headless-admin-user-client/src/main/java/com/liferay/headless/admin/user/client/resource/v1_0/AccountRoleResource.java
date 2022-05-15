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

package com.liferay.headless.admin.user.client.resource.v1_0;

import com.liferay.headless.admin.user.client.dto.v1_0.AccountRole;
import com.liferay.headless.admin.user.client.http.HttpInvoker;
import com.liferay.headless.admin.user.client.pagination.Page;
import com.liferay.headless.admin.user.client.pagination.Pagination;
import com.liferay.headless.admin.user.client.problem.Problem;
import com.liferay.headless.admin.user.client.serdes.v1_0.AccountRoleSerDes;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public interface AccountRoleResource {

	public static Builder builder() {
		return new Builder();
	}

	public void
			deleteAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCode(
				String accountExternalReferenceCode, Long accountRoleId,
				String userAccountExternalReferenceCode)
		throws Exception;

	public HttpInvoker.HttpResponse
			deleteAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCodeHttpResponse(
				String accountExternalReferenceCode, Long accountRoleId,
				String userAccountExternalReferenceCode)
		throws Exception;

	public void
			postAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCode(
				String accountExternalReferenceCode, Long accountRoleId,
				String userAccountExternalReferenceCode)
		throws Exception;

	public HttpInvoker.HttpResponse
			postAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCodeHttpResponse(
				String accountExternalReferenceCode, Long accountRoleId,
				String userAccountExternalReferenceCode)
		throws Exception;

	public Page<AccountRole>
			getAccountByExternalReferenceCodeUserAccountByExternalReferenceCodeAccountRolesPage(
				String accountExternalReferenceCode,
				String userAccountExternalReferenceCode)
		throws Exception;

	public HttpInvoker.HttpResponse
			getAccountByExternalReferenceCodeUserAccountByExternalReferenceCodeAccountRolesPageHttpResponse(
				String accountExternalReferenceCode,
				String userAccountExternalReferenceCode)
		throws Exception;

	public Page<AccountRole> getAccountAccountRolesByExternalReferenceCodePage(
			String externalReferenceCode, String keywords, String filterString,
			Pagination pagination, String sortString)
		throws Exception;

	public HttpInvoker.HttpResponse
			getAccountAccountRolesByExternalReferenceCodePageHttpResponse(
				String externalReferenceCode, String keywords,
				String filterString, Pagination pagination, String sortString)
		throws Exception;

	public AccountRole postAccountAccountRoleByExternalReferenceCode(
			String externalReferenceCode, AccountRole accountRole)
		throws Exception;

	public HttpInvoker.HttpResponse
			postAccountAccountRoleByExternalReferenceCodeHttpResponse(
				String externalReferenceCode, AccountRole accountRole)
		throws Exception;

	public void
			deleteAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddress(
				String externalReferenceCode, Long accountRoleId,
				String emailAddress)
		throws Exception;

	public HttpInvoker.HttpResponse
			deleteAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddressHttpResponse(
				String externalReferenceCode, Long accountRoleId,
				String emailAddress)
		throws Exception;

	public void
			postAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddress(
				String externalReferenceCode, Long accountRoleId,
				String emailAddress)
		throws Exception;

	public HttpInvoker.HttpResponse
			postAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddressHttpResponse(
				String externalReferenceCode, Long accountRoleId,
				String emailAddress)
		throws Exception;

	public Page<AccountRole>
			getAccountByExternalReferenceCodeUserAccountByEmailAddressAccountRolesPage(
				String externalReferenceCode, String emailAddress)
		throws Exception;

	public HttpInvoker.HttpResponse
			getAccountByExternalReferenceCodeUserAccountByEmailAddressAccountRolesPageHttpResponse(
				String externalReferenceCode, String emailAddress)
		throws Exception;

	public Page<AccountRole> getAccountAccountRolesPage(
			Long accountId, String keywords, String filterString,
			Pagination pagination, String sortString)
		throws Exception;

	public HttpInvoker.HttpResponse getAccountAccountRolesPageHttpResponse(
			Long accountId, String keywords, String filterString,
			Pagination pagination, String sortString)
		throws Exception;

	public AccountRole postAccountAccountRole(
			Long accountId, AccountRole accountRole)
		throws Exception;

	public HttpInvoker.HttpResponse postAccountAccountRoleHttpResponse(
			Long accountId, AccountRole accountRole)
		throws Exception;

	public void postAccountAccountRoleBatch(
			Long accountId, String callbackURL, Object object)
		throws Exception;

	public HttpInvoker.HttpResponse postAccountAccountRoleBatchHttpResponse(
			Long accountId, String callbackURL, Object object)
		throws Exception;

	public void deleteAccountAccountRoleUserAccountAssociation(
			Long accountId, Long accountRoleId, Long userAccountId)
		throws Exception;

	public HttpInvoker.HttpResponse
			deleteAccountAccountRoleUserAccountAssociationHttpResponse(
				Long accountId, Long accountRoleId, Long userAccountId)
		throws Exception;

	public void postAccountAccountRoleUserAccountAssociation(
			Long accountId, Long accountRoleId, Long userAccountId)
		throws Exception;

	public HttpInvoker.HttpResponse
			postAccountAccountRoleUserAccountAssociationHttpResponse(
				Long accountId, Long accountRoleId, Long userAccountId)
		throws Exception;

	public static class Builder {

		public Builder authentication(String login, String password) {
			_login = login;
			_password = password;

			return this;
		}

		public AccountRoleResource build() {
			return new AccountRoleResourceImpl(this);
		}

		public Builder endpoint(String host, int port, String scheme) {
			_host = host;
			_port = port;
			_scheme = scheme;

			return this;
		}

		public Builder header(String key, String value) {
			_headers.put(key, value);

			return this;
		}

		public Builder locale(Locale locale) {
			_locale = locale;

			return this;
		}

		public Builder parameter(String key, String value) {
			_parameters.put(key, value);

			return this;
		}

		public Builder parameters(String... parameters) {
			if ((parameters.length % 2) != 0) {
				throw new IllegalArgumentException(
					"Parameters length is not an even number");
			}

			for (int i = 0; i < parameters.length; i += 2) {
				String parameterName = String.valueOf(parameters[i]);
				String parameterValue = String.valueOf(parameters[i + 1]);

				_parameters.put(parameterName, parameterValue);
			}

			return this;
		}

		private Builder() {
		}

		private Map<String, String> _headers = new LinkedHashMap<>();
		private String _host = "localhost";
		private Locale _locale;
		private String _login = "";
		private String _password = "";
		private Map<String, String> _parameters = new LinkedHashMap<>();
		private int _port = 8080;
		private String _scheme = "http";

	}

	public static class AccountRoleResourceImpl implements AccountRoleResource {

		public void
				deleteAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCode(
					String accountExternalReferenceCode, Long accountRoleId,
					String userAccountExternalReferenceCode)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				deleteAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCodeHttpResponse(
					accountExternalReferenceCode, accountRoleId,
					userAccountExternalReferenceCode);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}

			try {
				return;
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse
				deleteAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCodeHttpResponse(
					String accountExternalReferenceCode, Long accountRoleId,
					String userAccountExternalReferenceCode)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.DELETE);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/by-external-reference-code/{accountExternalReferenceCode}/account-roles/{accountRoleId}/user-accounts/by-external-reference-code/{userAccountExternalReferenceCode}");

			httpInvoker.path(
				"accountExternalReferenceCode", accountExternalReferenceCode);
			httpInvoker.path("accountRoleId", accountRoleId);
			httpInvoker.path(
				"userAccountExternalReferenceCode",
				userAccountExternalReferenceCode);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public void
				postAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCode(
					String accountExternalReferenceCode, Long accountRoleId,
					String userAccountExternalReferenceCode)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				postAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCodeHttpResponse(
					accountExternalReferenceCode, accountRoleId,
					userAccountExternalReferenceCode);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}

			try {
				return;
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse
				postAccountByExternalReferenceCodeAccountRoleUserAccountByExternalReferenceCodeHttpResponse(
					String accountExternalReferenceCode, Long accountRoleId,
					String userAccountExternalReferenceCode)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/by-external-reference-code/{accountExternalReferenceCode}/account-roles/{accountRoleId}/user-accounts/by-external-reference-code/{userAccountExternalReferenceCode}");

			httpInvoker.path(
				"accountExternalReferenceCode", accountExternalReferenceCode);
			httpInvoker.path("accountRoleId", accountRoleId);
			httpInvoker.path(
				"userAccountExternalReferenceCode",
				userAccountExternalReferenceCode);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Page<AccountRole>
				getAccountByExternalReferenceCodeUserAccountByExternalReferenceCodeAccountRolesPage(
					String accountExternalReferenceCode,
					String userAccountExternalReferenceCode)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				getAccountByExternalReferenceCodeUserAccountByExternalReferenceCodeAccountRolesPageHttpResponse(
					accountExternalReferenceCode,
					userAccountExternalReferenceCode);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}

			try {
				return Page.of(content, AccountRoleSerDes::toDTO);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse
				getAccountByExternalReferenceCodeUserAccountByExternalReferenceCodeAccountRolesPageHttpResponse(
					String accountExternalReferenceCode,
					String userAccountExternalReferenceCode)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/by-external-reference-code/{accountExternalReferenceCode}/user-accounts/by-external-reference-code/{userAccountExternalReferenceCode}/account-roles");

			httpInvoker.path(
				"accountExternalReferenceCode", accountExternalReferenceCode);
			httpInvoker.path(
				"userAccountExternalReferenceCode",
				userAccountExternalReferenceCode);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Page<AccountRole>
				getAccountAccountRolesByExternalReferenceCodePage(
					String externalReferenceCode, String keywords,
					String filterString, Pagination pagination,
					String sortString)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				getAccountAccountRolesByExternalReferenceCodePageHttpResponse(
					externalReferenceCode, keywords, filterString, pagination,
					sortString);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}

			try {
				return Page.of(content, AccountRoleSerDes::toDTO);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse
				getAccountAccountRolesByExternalReferenceCodePageHttpResponse(
					String externalReferenceCode, String keywords,
					String filterString, Pagination pagination,
					String sortString)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

			if (keywords != null) {
				httpInvoker.parameter("keywords", String.valueOf(keywords));
			}

			if (filterString != null) {
				httpInvoker.parameter("filter", filterString);
			}

			if (pagination != null) {
				httpInvoker.parameter(
					"page", String.valueOf(pagination.getPage()));
				httpInvoker.parameter(
					"pageSize", String.valueOf(pagination.getPageSize()));
			}

			if (sortString != null) {
				httpInvoker.parameter("sort", sortString);
			}

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/by-external-reference-code/{externalReferenceCode}/account-roles");

			httpInvoker.path("externalReferenceCode", externalReferenceCode);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public AccountRole postAccountAccountRoleByExternalReferenceCode(
				String externalReferenceCode, AccountRole accountRole)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				postAccountAccountRoleByExternalReferenceCodeHttpResponse(
					externalReferenceCode, accountRole);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}

			try {
				return AccountRoleSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse
				postAccountAccountRoleByExternalReferenceCodeHttpResponse(
					String externalReferenceCode, AccountRole accountRole)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(accountRole.toString(), "application/json");

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/by-external-reference-code/{externalReferenceCode}/account-roles");

			httpInvoker.path("externalReferenceCode", externalReferenceCode);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public void
				deleteAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddress(
					String externalReferenceCode, Long accountRoleId,
					String emailAddress)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				deleteAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddressHttpResponse(
					externalReferenceCode, accountRoleId, emailAddress);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}

			try {
				return;
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse
				deleteAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddressHttpResponse(
					String externalReferenceCode, Long accountRoleId,
					String emailAddress)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.DELETE);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/by-external-reference-code/{externalReferenceCode}/account-roles/{accountRoleId}/user-accounts/by-email-address/{emailAddress}");

			httpInvoker.path("externalReferenceCode", externalReferenceCode);
			httpInvoker.path("accountRoleId", accountRoleId);
			httpInvoker.path("emailAddress", emailAddress);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public void
				postAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddress(
					String externalReferenceCode, Long accountRoleId,
					String emailAddress)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				postAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddressHttpResponse(
					externalReferenceCode, accountRoleId, emailAddress);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}

			try {
				return;
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse
				postAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddressHttpResponse(
					String externalReferenceCode, Long accountRoleId,
					String emailAddress)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/by-external-reference-code/{externalReferenceCode}/account-roles/{accountRoleId}/user-accounts/by-email-address/{emailAddress}");

			httpInvoker.path("externalReferenceCode", externalReferenceCode);
			httpInvoker.path("accountRoleId", accountRoleId);
			httpInvoker.path("emailAddress", emailAddress);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Page<AccountRole>
				getAccountByExternalReferenceCodeUserAccountByEmailAddressAccountRolesPage(
					String externalReferenceCode, String emailAddress)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				getAccountByExternalReferenceCodeUserAccountByEmailAddressAccountRolesPageHttpResponse(
					externalReferenceCode, emailAddress);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}

			try {
				return Page.of(content, AccountRoleSerDes::toDTO);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse
				getAccountByExternalReferenceCodeUserAccountByEmailAddressAccountRolesPageHttpResponse(
					String externalReferenceCode, String emailAddress)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/by-external-reference-code/{externalReferenceCode}/user-accounts/by-email-address/{emailAddress}/account-roles");

			httpInvoker.path("externalReferenceCode", externalReferenceCode);
			httpInvoker.path("emailAddress", emailAddress);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Page<AccountRole> getAccountAccountRolesPage(
				Long accountId, String keywords, String filterString,
				Pagination pagination, String sortString)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				getAccountAccountRolesPageHttpResponse(
					accountId, keywords, filterString, pagination, sortString);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}

			try {
				return Page.of(content, AccountRoleSerDes::toDTO);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse getAccountAccountRolesPageHttpResponse(
				Long accountId, String keywords, String filterString,
				Pagination pagination, String sortString)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

			if (keywords != null) {
				httpInvoker.parameter("keywords", String.valueOf(keywords));
			}

			if (filterString != null) {
				httpInvoker.parameter("filter", filterString);
			}

			if (pagination != null) {
				httpInvoker.parameter(
					"page", String.valueOf(pagination.getPage()));
				httpInvoker.parameter(
					"pageSize", String.valueOf(pagination.getPageSize()));
			}

			if (sortString != null) {
				httpInvoker.parameter("sort", sortString);
			}

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/{accountId}/account-roles");

			httpInvoker.path("accountId", accountId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public AccountRole postAccountAccountRole(
				Long accountId, AccountRole accountRole)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				postAccountAccountRoleHttpResponse(accountId, accountRole);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}

			try {
				return AccountRoleSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse postAccountAccountRoleHttpResponse(
				Long accountId, AccountRole accountRole)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(accountRole.toString(), "application/json");

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/{accountId}/account-roles");

			httpInvoker.path("accountId", accountId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public void postAccountAccountRoleBatch(
				Long accountId, String callbackURL, Object object)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				postAccountAccountRoleBatchHttpResponse(
					accountId, callbackURL, object);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}
		}

		public HttpInvoker.HttpResponse postAccountAccountRoleBatchHttpResponse(
				Long accountId, String callbackURL, Object object)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(object.toString(), "application/json");

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

			if (callbackURL != null) {
				httpInvoker.parameter(
					"callbackURL", String.valueOf(callbackURL));
			}

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/{accountId}/account-roles/batch");

			httpInvoker.path("accountId", accountId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public void deleteAccountAccountRoleUserAccountAssociation(
				Long accountId, Long accountRoleId, Long userAccountId)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				deleteAccountAccountRoleUserAccountAssociationHttpResponse(
					accountId, accountRoleId, userAccountId);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}

			try {
				return;
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse
				deleteAccountAccountRoleUserAccountAssociationHttpResponse(
					Long accountId, Long accountRoleId, Long userAccountId)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.DELETE);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/{accountId}/account-roles/{accountRoleId}/user-accounts/{userAccountId}");

			httpInvoker.path("accountId", accountId);
			httpInvoker.path("accountRoleId", accountRoleId);
			httpInvoker.path("userAccountId", userAccountId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public void postAccountAccountRoleUserAccountAssociation(
				Long accountId, Long accountRoleId, Long userAccountId)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				postAccountAccountRoleUserAccountAssociationHttpResponse(
					accountId, accountRoleId, userAccountId);

			String content = httpResponse.getContent();

			if ((httpResponse.getStatusCode() / 100) != 2) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response content: " + content);
				_logger.log(
					Level.WARNING,
					"HTTP response message: " + httpResponse.getMessage());
				_logger.log(
					Level.WARNING,
					"HTTP response status code: " +
						httpResponse.getStatusCode());

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
			else {
				_logger.fine("HTTP response content: " + content);
				_logger.fine(
					"HTTP response message: " + httpResponse.getMessage());
				_logger.fine(
					"HTTP response status code: " +
						httpResponse.getStatusCode());
			}

			try {
				return;
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw new Problem.ProblemException(Problem.toDTO(content));
			}
		}

		public HttpInvoker.HttpResponse
				postAccountAccountRoleUserAccountAssociationHttpResponse(
					Long accountId, Long accountRoleId, Long userAccountId)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-admin-user/v1.0/accounts/{accountId}/account-roles/{accountRoleId}/user-accounts/{userAccountId}");

			httpInvoker.path("accountId", accountId);
			httpInvoker.path("accountRoleId", accountRoleId);
			httpInvoker.path("userAccountId", userAccountId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		private AccountRoleResourceImpl(Builder builder) {
			_builder = builder;
		}

		private static final Logger _logger = Logger.getLogger(
			AccountRoleResource.class.getName());

		private Builder _builder;

	}

}