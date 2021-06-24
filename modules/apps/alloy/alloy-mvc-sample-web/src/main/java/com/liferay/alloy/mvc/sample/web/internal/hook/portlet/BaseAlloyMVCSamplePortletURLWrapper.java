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

package com.liferay.alloy.mvc.sample.web.internal.hook.portlet;

import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLWrapper;

import java.io.IOException;
import java.io.Writer;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.portlet.MutableResourceParameters;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSecurityException;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

/**
 * @author Brian Wing Shun Chan
 */
public abstract class BaseAlloyMVCSamplePortletURLWrapper
	extends PortletURLWrapper implements LiferayPortletURL {

	public BaseAlloyMVCSamplePortletURLWrapper(
		LiferayPortletURL liferayPortletURL) {

		super(liferayPortletURL);

		this.liferayPortletURL = liferayPortletURL;
	}

	@Override
	public void addParameterIncludedInPath(String name) {
		liferayPortletURL.addParameterIncludedInPath(name);
	}

	@Override
	public void addProperty(String key, String value) {
		liferayPortletURL.addProperty(key, value);
	}

	@Override
	public String getCacheability() {
		return liferayPortletURL.getCacheability();
	}

	@Override
	public String getLifecycle() {
		return liferayPortletURL.getLifecycle();
	}

	@Override
	public String getParameter(String name) {
		return liferayPortletURL.getParameter(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return liferayPortletURL.getParameterMap();
	}

	@Override
	public Set<String> getParametersIncludedInPath() {
		return liferayPortletURL.getParametersIncludedInPath();
	}

	@Override
	public long getPlid() {
		return liferayPortletURL.getPlid();
	}

	@Override
	public String getPortletId() {
		return liferayPortletURL.getPortletId();
	}

	@Override
	public PortletMode getPortletMode() {
		return liferayPortletURL.getPortletMode();
	}

	@Override
	public Set<String> getRemovedParameterNames() {
		return liferayPortletURL.getRemovedParameterNames();
	}

	@Override
	public String getResourceID() {
		return liferayPortletURL.getResourceID();
	}

	@Override
	public MutableResourceParameters getResourceParameters() {
		return liferayPortletURL.getResourceParameters();
	}

	@Override
	public WindowState getWindowState() {
		return liferayPortletURL.getWindowState();
	}

	@Override
	public boolean isAnchor() {
		return liferayPortletURL.isAnchor();
	}

	@Override
	public boolean isCopyCurrentRenderParameters() {
		return liferayPortletURL.isCopyCurrentRenderParameters();
	}

	@Override
	public boolean isEncrypt() {
		return liferayPortletURL.isEncrypt();
	}

	@Override
	public boolean isEscapeXml() {
		return liferayPortletURL.isEscapeXml();
	}

	@Override
	public boolean isParameterIncludedInPath(String name) {
		return liferayPortletURL.isParameterIncludedInPath(name);
	}

	@Override
	public boolean isSecure() {
		return liferayPortletURL.isSecure();
	}

	@Override
	public void removePublicRenderParameter(String name) {
		liferayPortletURL.removePublicRenderParameter(name);
	}

	@Override
	public void setAnchor(boolean anchor) {
		liferayPortletURL.setAnchor(anchor);
	}

	@Override
	public void setCacheability(String cacheLevel) {
		liferayPortletURL.setCacheability(cacheLevel);
	}

	@Override
	public void setCopyCurrentRenderParameters(
		boolean copyCurrentRenderParameters) {

		liferayPortletURL.setCopyCurrentRenderParameters(
			copyCurrentRenderParameters);
	}

	@Override
	public void setDoAsGroupId(long doAsGroupId) {
		liferayPortletURL.setDoAsGroupId(doAsGroupId);
	}

	@Override
	public void setDoAsUserId(long doAsUserId) {
		liferayPortletURL.setDoAsUserId(doAsUserId);
	}

	@Override
	public void setDoAsUserLanguageId(String doAsUserLanguageId) {
		liferayPortletURL.setDoAsUserLanguageId(doAsUserLanguageId);
	}

	@Override
	public void setEncrypt(boolean encrypt) {
		liferayPortletURL.setEncrypt(encrypt);
	}

	@Override
	public void setEscapeXml(boolean escapeXml) {
		liferayPortletURL.setEscapeXml(escapeXml);
	}

	@Override
	public void setLifecycle(String lifecycle) {
		liferayPortletURL.setLifecycle(lifecycle);
	}

	@Override
	public void setParameter(String name, String value) {
		liferayPortletURL.setParameter(name, value);
	}

	@Override
	public void setParameter(String name, String value, boolean append) {
		liferayPortletURL.setParameter(name, value, append);
	}

	@Override
	public void setParameter(String name, String[] values) {
		liferayPortletURL.setParameter(name, values);
	}

	@Override
	public void setParameter(String name, String[] values, boolean append) {
		liferayPortletURL.setParameter(name, values, append);
	}

	@Override
	public void setParameters(Map<String, String[]> parameters) {
		liferayPortletURL.setParameters(parameters);
	}

	@Override
	public void setPlid(long plid) {
		liferayPortletURL.setPlid(plid);
	}

	@Override
	public void setPortletId(String portletId) {
		liferayPortletURL.setPortletId(portletId);
	}

	@Override
	public void setPortletMode(PortletMode portletMode)
		throws PortletModeException {

		liferayPortletURL.setPortletMode(portletMode);
	}

	@Override
	public void setProperty(String key, String value) {
		liferayPortletURL.setProperty(key, value);
	}

	@Override
	public void setRefererGroupId(long refererGroupId) {
		liferayPortletURL.setRefererGroupId(refererGroupId);
	}

	@Override
	public void setRefererPlid(long refererPlid) {
		liferayPortletURL.setRefererPlid(refererPlid);
	}

	@Override
	public void setRemovedParameterNames(Set<String> removedParamNames) {
		liferayPortletURL.setRemovedParameterNames(removedParamNames);
	}

	@Override
	public void setResourceID(String resourceID) {
		liferayPortletURL.setResourceID(resourceID);
	}

	@Override
	public void setSecure(boolean secure) throws PortletSecurityException {
		liferayPortletURL.setSecure(secure);
	}

	@Override
	public void setWindowState(WindowState windowState)
		throws WindowStateException {

		liferayPortletURL.setWindowState(windowState);
	}

	public void setWindowStateRestoreCurrentView(
		boolean windowStateRestoreCurrentView) {

		BeanPropertiesUtil.setProperty(
			liferayPortletURL, "windowStateRestoreCurrentView",
			windowStateRestoreCurrentView);
	}

	public void visitReservedParameters(BiConsumer<String, String> biConsumer) {
	}

	@Override
	public void write(Writer out) throws IOException {
		liferayPortletURL.write(out);
	}

	@Override
	public void write(Writer out, boolean escapeXML) throws IOException {
		liferayPortletURL.write(out, escapeXML);
	}

	protected LiferayPortletURL liferayPortletURL;

}