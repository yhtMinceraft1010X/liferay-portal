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

package com.liferay.marketplace.store.web.internal.portlet;

import com.liferay.marketplace.store.web.internal.oauth.util.OAuthManager;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * @author Ryan Park
 * @author Joan Kim
 * @author Douglas Wong
 * @author Haote Chou
 */
public class RemoteMVCPortlet extends MVCPortlet {

	public void authorize(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		OAuthService oAuthService = oAuthManager.getOAuthService();

		Token requestToken = oAuthService.getRequestToken();

		oAuthManager.updateRequestToken(themeDisplay.getUser(), requestToken);

		String redirect = oAuthService.getAuthorizationUrl(requestToken);

		String callbackURL = ParamUtil.getString(actionRequest, "callbackURL");

		redirect = HttpComponentsUtil.addParameter(
			redirect, OAuthConstants.CALLBACK, callbackURL);

		actionResponse.sendRedirect(redirect);
	}

	public void deauthorize(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		oAuthManager.deleteAccessToken(themeDisplay.getUser());

		actionResponse.sendRedirect(
			PortletURLBuilder.createRenderURL(
				PortalUtil.getLiferayPortletResponse(actionResponse)
			).setMVCPath(
				"/view.jsp"
			).buildString());
	}

	@Override
	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

		_checkOmniAdmin();

		try {
			String actionName = ParamUtil.getString(
				actionRequest, ActionRequest.ACTION_NAME);

			getActionMethod(actionName);

			super.processAction(actionRequest, actionResponse);

			return;
		}
		catch (NoSuchMethodException noSuchMethodException) {
			if (_log.isDebugEnabled()) {
				_log.debug(noSuchMethodException);
			}
		}

		try {
			_remoteProcessAction(actionRequest, actionResponse);
		}
		catch (IOException ioException) {
			throw ioException;
		}
		catch (Exception exception) {
			throw new PortletException(exception);
		}
	}

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		_checkOmniAdmin();

		try {
			HttpServletRequest httpServletRequest =
				PortalUtil.getHttpServletRequest(renderRequest);

			httpServletRequest = PortalUtil.getOriginalServletRequest(
				httpServletRequest);

			String oAuthVerifier = httpServletRequest.getParameter(
				OAuthConstants.VERIFIER);

			if (oAuthVerifier != null) {
				_updateAccessToken(renderRequest, oAuthVerifier);
			}

			String remoteMVCPath = renderRequest.getParameter("remoteMVCPath");

			if (remoteMVCPath != null) {
				_remoteRender(renderRequest, renderResponse);

				return;
			}
		}
		catch (IOException ioException) {
			throw ioException;
		}
		catch (Exception exception) {
			throw new PortletException(exception);
		}

		super.render(renderRequest, renderResponse);
	}

	@Override
	public void serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		_checkOmniAdmin();

		try {
			_remoteServeResource(resourceRequest, resourceResponse);
		}
		catch (IOException ioException) {
			throw ioException;
		}
		catch (Exception exception) {
			throw new PortletException(exception);
		}
	}

	protected void addOAuthParameter(
		OAuthRequest oAuthRequest, String key, String value) {

		if (oAuthRequest.getVerb() == Verb.GET) {
			oAuthRequest.addQuerystringParameter(key, value);
		}
		else if (oAuthRequest.getVerb() == Verb.POST) {
			oAuthRequest.addBodyParameter(key, value);
		}
	}

	protected String getClientPortletId() {
		return StringPool.BLANK;
	}

	protected Response getResponse(User user, OAuthRequest oAuthRequest)
		throws Exception {

		Token token = oAuthManager.getAccessToken(user);

		if (token != null) {
			OAuthService oAuthService = oAuthManager.getOAuthService();

			oAuthService.signRequest(token, oAuthRequest);
		}

		oAuthRequest.setFollowRedirects(false);

		return oAuthRequest.send();
	}

	protected String getServerNamespace() {
		return PortalUtil.getPortletNamespace(getServerPortletId());
	}

	protected String getServerPortletId() {
		return StringPool.BLANK;
	}

	protected String getServerPortletURL() {
		return StringPool.BLANK;
	}

	protected void processPortletParameterMap(
		PortletRequest portletRequest, PortletResponse portletResponse,
		Map<String, String[]> parameterMap) {
	}

	protected void setBaseRequestParameters(
		PortletRequest portletRequest, PortletResponse portletResponse,
		OAuthRequest oAuthRequest) {

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		String clientAuthToken = AuthTokenUtil.getToken(httpServletRequest);

		addOAuthParameter(oAuthRequest, "clientAuthToken", clientAuthToken);

		addOAuthParameter(
			oAuthRequest, "clientPortletId", getClientPortletId());
		addOAuthParameter(
			oAuthRequest, "clientURL",
			PortalUtil.getCurrentCompleteURL(httpServletRequest));
		addOAuthParameter(oAuthRequest, "p_p_id", getServerPortletId());
	}

	protected void setOAuthManager(OAuthManager oAuthManager) {
		this.oAuthManager = oAuthManager;
	}

	protected OAuthManager oAuthManager;

	private void _checkOmniAdmin() throws PortletException {
		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (!permissionChecker.isOmniadmin()) {
			PrincipalException principalException =
				new PrincipalException.MustBeCompanyAdmin(
					permissionChecker.getUserId());

			throw new PortletException(principalException);
		}
	}

	private String _getFileName(String contentDisposition) {
		int pos = contentDisposition.indexOf("filename=\"");

		if (pos == -1) {
			return StringPool.BLANK;
		}

		return contentDisposition.substring(
			pos + 10, contentDisposition.length() - 1);
	}

	private void _remoteProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		OAuthRequest oAuthRequest = new OAuthRequest(
			Verb.POST, getServerPortletURL());

		_setRequestParameters(actionRequest, actionResponse, oAuthRequest);

		addOAuthParameter(oAuthRequest, "p_p_lifecycle", "1");
		addOAuthParameter(
			oAuthRequest, "p_p_state", WindowState.NORMAL.toString());

		Response response = getResponse(themeDisplay.getUser(), oAuthRequest);

		if (response.getCode() == HttpServletResponse.SC_FOUND) {
			String redirectLocation = response.getHeader(HttpHeaders.LOCATION);

			actionResponse.sendRedirect(redirectLocation);
		}
		else {
			HttpServletResponse httpServletResponse =
				PortalUtil.getHttpServletResponse(actionResponse);

			httpServletResponse.setContentType(
				response.getHeader(HttpHeaders.CONTENT_TYPE));

			ServletResponseUtil.write(
				httpServletResponse, response.getStream());
		}
	}

	private void _remoteRender(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		OAuthRequest oAuthRequest = new OAuthRequest(
			Verb.GET, getServerPortletURL());

		_setRequestParameters(renderRequest, renderResponse, oAuthRequest);

		Response response = getResponse(themeDisplay.getUser(), oAuthRequest);

		renderResponse.setContentType(ContentTypes.TEXT_HTML);

		PrintWriter printWriter = renderResponse.getWriter();

		printWriter.write(response.getBody());
	}

	private void _remoteServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		OAuthRequest oAuthRequest = new OAuthRequest(
			Verb.GET, getServerPortletURL());

		_setRequestParameters(resourceRequest, resourceResponse, oAuthRequest);

		addOAuthParameter(oAuthRequest, "p_p_lifecycle", "2");
		addOAuthParameter(
			oAuthRequest, "p_p_resource_id", resourceRequest.getResourceID());

		Response response = getResponse(themeDisplay.getUser(), oAuthRequest);

		String contentType = response.getHeader(HttpHeaders.CONTENT_TYPE);

		if (contentType.startsWith(ContentTypes.APPLICATION_OCTET_STREAM)) {
			String contentDisposition = response.getHeader(
				HttpHeaders.CONTENT_DISPOSITION);
			int contentLength = GetterUtil.getInteger(
				response.getHeader(HttpHeaders.CONTENT_LENGTH));

			PortletResponseUtil.sendFile(
				resourceRequest, resourceResponse,
				_getFileName(contentDisposition), response.getStream(),
				contentLength, contentType,
				HttpHeaders.CONTENT_DISPOSITION_ATTACHMENT);
		}
		else {
			resourceResponse.setContentType(contentType);

			PortletResponseUtil.write(resourceResponse, response.getStream());
		}
	}

	private void _setRequestParameters(
		PortletRequest portletRequest, PortletResponse portletResponse,
		OAuthRequest oAuthRequest) {

		setBaseRequestParameters(portletRequest, portletResponse, oAuthRequest);

		Map<String, String[]> parameterMap = new HashMap<>();

		MapUtil.copy(portletRequest.getParameterMap(), parameterMap);

		processPortletParameterMap(
			portletRequest, portletResponse, parameterMap);

		String serverNamespace = getServerNamespace();

		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();

			if (key.equals("remoteWindowState")) {
				key = "p_p_state";
			}
			else {
				key = serverNamespace.concat(key);
			}

			if (ArrayUtil.isEmpty(values) || Validator.isNull(values[0])) {
				continue;
			}

			addOAuthParameter(oAuthRequest, key, values[0]);
		}
	}

	private void _updateAccessToken(
			RenderRequest renderRequest, String oAuthVerifier)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Token requestToken = oAuthManager.getRequestToken(
			themeDisplay.getUser());

		OAuthService oAuthService = oAuthManager.getOAuthService();

		oAuthManager.updateAccessToken(
			themeDisplay.getUser(),
			oAuthService.getAccessToken(
				requestToken, new Verifier(oAuthVerifier)));

		oAuthManager.deleteRequestToken(themeDisplay.getUser());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RemoteMVCPortlet.class);

}