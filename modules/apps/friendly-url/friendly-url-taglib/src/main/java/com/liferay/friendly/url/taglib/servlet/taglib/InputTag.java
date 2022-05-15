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

package com.liferay.friendly.url.taglib.servlet.taglib;

import com.liferay.friendly.url.exception.NoSuchFriendlyURLEntryMappingException;
import com.liferay.friendly.url.model.FriendlyURLEntry;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalServiceUtil;
import com.liferay.friendly.url.taglib.internal.servlet.ServletContextUtil;
import com.liferay.friendly.url.taglib.util.InfoItemObjectProviderUtil;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistryUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.taglib.util.IncludeTag;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Adolfo Pérez
 */
public class InputTag extends IncludeTag {

	public String getClassName() {
		return _className;
	}

	public long getClassPK() {
		return _classPK;
	}

	public String getInputAddon() {
		return _inputAddon;
	}

	public String getName() {
		return _name;
	}

	public boolean isDisabled() {
		return _disabled;
	}

	public boolean isLocalizable() {
		return _localizable;
	}

	public boolean isShowHistory() {
		return _showHistory;
	}

	public boolean isShowLabel() {
		return _showLabel;
	}

	public void setClassName(String className) {
		_className = className;
	}

	public void setClassPK(long classPK) {
		_classPK = classPK;
	}

	public void setDisabled(boolean disabled) {
		_disabled = disabled;
	}

	public void setInputAddon(String inputAddon) {
		_inputAddon = inputAddon;
	}

	public void setLocalizable(boolean localizable) {
		_localizable = localizable;
	}

	public void setName(String name) {
		_name = name;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		setServletContext(ServletContextUtil.getServletContext());
	}

	public void setShowHistory(boolean showHistory) {
		_showHistory = showHistory;
	}

	public void setShowLabel(boolean showLabel) {
		_showLabel = showLabel;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_className = null;
		_classPK = 0;
		_disabled = false;
		_inputAddon = null;
		_localizable = true;
		_name = _DEFAULT_NAME;
		_showHistory = true;
		_showLabel = true;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest httpServletRequest) {
		super.setAttributes(httpServletRequest);

		httpServletRequest.setAttribute(
			"liferay-friendly-url:input:className", getClassName());
		httpServletRequest.setAttribute(
			"liferay-friendly-url:input:classPK", getClassPK());
		httpServletRequest.setAttribute(
			"liferay-friendly-url:input:disabled", isDisabled());
		httpServletRequest.setAttribute(
			"liferay-friendly-url:input:friendlyURLMaxLength",
			_FRIENDLY_URL_MAX_LENGTH);
		httpServletRequest.setAttribute(
			"liferay-friendly-url:input:inputAddon", getInputAddon());
		httpServletRequest.setAttribute(
			"liferay-friendly-url:input:localizable", isLocalizable());
		httpServletRequest.setAttribute(
			"liferay-friendly-url:input:name", getName());
		httpServletRequest.setAttribute(
			"liferay-friendly-url:input:showHistory", _isShowHistory());
		httpServletRequest.setAttribute(
			"liferay-friendly-url:input:showLabel", isShowLabel());
		httpServletRequest.setAttribute(
			"liferay-friendly-url:input:value", _getValue());
	}

	private String _getActualClassName() throws PortalException {
		if (!Objects.equals(getClassName(), Layout.class.getName())) {
			return getClassName();
		}

		Layout layout = LayoutLocalServiceUtil.getLayout(getClassPK());

		return getClassName() + StringPool.DASH + layout.isPrivateLayout();
	}

	private String _getFallbackValue() {
		try {
			if (Objects.equals(
					_getActualClassName(), FileEntry.class.getName())) {

				return StringPool.BLANK;
			}

			String urlTitle = BeanPropertiesUtil.getString(
				_getModel(), "urlTitle");

			if (Validator.isNull(urlTitle)) {
				return StringPool.BLANK;
			}

			if (!isLocalizable()) {
				return urlTitle;
			}

			String languageId = LanguageUtil.getLanguageId(
				LocaleUtil.getDefault());

			return LocalizationUtil.getXml(
				HashMapBuilder.put(
					languageId, urlTitle
				).build(),
				languageId, "UrlTitle");
		}
		catch (Exception exception) {
			return StringPool.BLANK;
		}
	}

	private Object _getModel() throws PortalException {
		Object model = InfoItemObjectProviderUtil.getInfoItem(
			getClassName(), getClassPK());

		if (model != null) {
			return model;
		}

		PersistedModelLocalService persistedModelLocalService =
			PersistedModelLocalServiceRegistryUtil.
				getPersistedModelLocalService(getClassName());

		return persistedModelLocalService.getPersistedModel(getClassPK());
	}

	private String _getValue() {
		try {
			if (getClassPK() == 0) {
				return StringPool.BLANK;
			}

			FriendlyURLEntry mainFriendlyURLEntry =
				FriendlyURLEntryLocalServiceUtil.getMainFriendlyURLEntry(
					PortalUtil.getClassNameId(_getActualClassName()),
					getClassPK());

			if (isLocalizable()) {
				return HtmlUtil.escapeURL(
					mainFriendlyURLEntry.getUrlTitleMapAsXML());
			}

			return HtmlUtil.escapeURL(mainFriendlyURLEntry.getUrlTitle());
		}
		catch (NoSuchFriendlyURLEntryMappingException
					noSuchFriendlyURLEntryMappingException) {

			return _getFallbackValue();
		}
		catch (PortalException portalException) {
			return ReflectionUtil.throwException(portalException);
		}
	}

	private boolean _isShowHistory() {
		if (isShowHistory() && (getClassPK() != 0)) {
			return true;
		}

		return false;
	}

	private static final String _DEFAULT_NAME = "friendlyURL";

	private static final int _FRIENDLY_URL_MAX_LENGTH = 255;

	private static final String _PAGE = "/input/page.jsp";

	private String _className;
	private long _classPK;
	private boolean _disabled;
	private String _inputAddon;
	private boolean _localizable = true;
	private String _name = _DEFAULT_NAME;
	private boolean _showHistory = true;
	private boolean _showLabel = true;

}