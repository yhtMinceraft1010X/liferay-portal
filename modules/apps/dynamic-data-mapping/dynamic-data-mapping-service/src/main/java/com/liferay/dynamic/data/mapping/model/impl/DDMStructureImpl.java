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

package com.liferay.dynamic.data.mapping.model.impl;

import com.liferay.dynamic.data.mapping.exception.StructureFieldException;
import com.liferay.dynamic.data.mapping.internal.io.DDMFormJSONDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.service.DDMStructureLayoutLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMStructureVersionLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.petra.function.UnsafeBiFunction;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.cache.CacheField;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Brian Wing Shun Chan
 */
public class DDMStructureImpl extends DDMStructureBaseImpl {

	@Override
	public DDMForm createFullHierarchyDDMForm() throws PortalException {
		DDMForm fullHierarchyDDMForm = getDDMForm();

		DDMStructure parentDDMStructure = getParentDDMStructure();

		if (parentDDMStructure != null) {
			DDMForm ancestorsDDMForm =
				parentDDMStructure.createFullHierarchyDDMForm();

			List<DDMFormField> ancestorsDDMFormFields =
				ancestorsDDMForm.getDDMFormFields();

			for (DDMFormField ancestorsDDMFormField : ancestorsDDMFormFields) {
				ancestorsDDMFormField.setDDMForm(fullHierarchyDDMForm);
			}

			List<DDMFormField> ddmFormFields =
				fullHierarchyDDMForm.getDDMFormFields();

			ddmFormFields.addAll(0, ancestorsDDMFormFields);
		}

		return fullHierarchyDDMForm;
	}

	@Override
	public DDMStructureLayout fetchDDMStructureLayout() {
		try {
			DDMStructureVersion ddmStructureVersion =
				getLatestStructureVersion();

			return DDMStructureLayoutLocalServiceUtil.
				getStructureLayoutByStructureVersionId(
					ddmStructureVersion.getStructureVersionId());
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}

		return null;
	}

	@Override
	public String[] getAvailableLanguageIds() {
		DDMForm ddmForm = _getDDMForm();

		Set<Locale> availableLocales = ddmForm.getAvailableLocales();

		return LocaleUtil.toLanguageIds(
			availableLocales.toArray(new Locale[0]));
	}

	@Override
	public List<String> getChildrenFieldNames(String fieldName)
		throws PortalException {

		DDMFormField ddmFormField = _getDDMFormField(fieldName);

		return getDDMFormFieldNames(ddmFormField.getNestedDDMFormFields());
	}

	@Override
	public String getClassName() {
		if (_className == null) {
			_className = PortalUtil.getClassName(getClassNameId());
		}

		return _className;
	}

	@Override
	public DDMForm getDDMForm() {
		return new DDMForm(_getDDMForm());
	}

	@Override
	public DDMFormField getDDMFormField(String fieldName)
		throws PortalException {

		return new DDMFormField(_getDDMFormField(fieldName));
	}

	@Override
	public DDMFormField getDDMFormFieldByFieldReference(String fieldReference)
		throws PortalException {

		return new DDMFormField(
			_getDDMFormFieldByFieldReference(fieldReference));
	}

	@Override
	public List<DDMFormField> getDDMFormFields(boolean includeTransientFields) {
		Map<String, DDMFormField> ddmFormFieldsMap =
			getFullHierarchyDDMFormFieldsMap(true);

		List<DDMFormField> ddmFormFields = new ArrayList<>(
			ddmFormFieldsMap.values());

		if (includeTransientFields) {
			return ddmFormFields;
		}

		return filterTransientDDMFormFields(ddmFormFields);
	}

	@Override
	public DDMFormLayout getDDMFormLayout() throws PortalException {
		DDMStructureVersion structureVersion = getStructureVersion();

		DDMStructureLayout ddmStructureLayout =
			DDMStructureLayoutLocalServiceUtil.
				getStructureLayoutByStructureVersionId(
					structureVersion.getStructureVersionId());

		return ddmStructureLayout.getDDMFormLayout();
	}

	@Override
	public long getDefaultDDMStructureLayoutId() {
		DDMStructureLayout ddmStructureLayout = fetchDDMStructureLayout();

		if (ddmStructureLayout != null) {
			return ddmStructureLayout.getStructureLayoutId();
		}

		return 0;
	}

	@Override
	public String getDefaultLanguageId() {
		DDMForm ddmForm = _getDDMForm();

		return LocaleUtil.toLanguageId(ddmForm.getDefaultLocale());
	}

	@Override
	public String getFieldDataType(String fieldName) throws PortalException {
		DDMFormField ddmFormField = _getDDMFormField(fieldName);

		return ddmFormField.getDataType();
	}

	@Override
	public String getFieldLabel(String fieldName, Locale locale)
		throws PortalException {

		DDMFormField ddmFormField = _getDDMFormField(fieldName);

		LocalizedValue label = ddmFormField.getLabel();

		return label.getString(locale);
	}

	@Override
	public String getFieldLabel(String fieldName, String locale)
		throws PortalException {

		return getFieldLabel(fieldName, LocaleUtil.fromLanguageId(locale));
	}

	@Override
	public Set<String> getFieldNames() {
		return SetUtil.fromList(getDDMFormFieldNames(getDDMFormFields(false)));
	}

	@Override
	public String getFieldProperty(String fieldName, String property)
		throws PortalException {

		return BeanPropertiesUtil.getString(
			_getDDMFormField(fieldName), property);
	}

	@Override
	public String getFieldPropertyByFieldReference(
			String fieldReference, String property)
		throws PortalException {

		return BeanPropertiesUtil.getString(
			_getDDMFormFieldByFieldReference(fieldReference), property);
	}

	@Override
	public boolean getFieldRepeatable(String fieldName) throws PortalException {
		DDMFormField ddmFormField = _getDDMFormField(fieldName);

		return ddmFormField.isRepeatable();
	}

	@Override
	public boolean getFieldRequired(String fieldName) throws PortalException {
		DDMFormField ddmFormField = _getDDMFormField(fieldName);

		return ddmFormField.isRequired();
	}

	@Override
	public String getFieldTip(String fieldName, Locale locale)
		throws PortalException {

		DDMFormField ddmFormField = _getDDMFormField(fieldName);

		LocalizedValue tip = ddmFormField.getTip();

		return tip.getString(locale);
	}

	@Override
	public String getFieldTip(String fieldName, String locale)
		throws PortalException {

		return getFieldTip(fieldName, LocaleUtil.fromLanguageId(locale));
	}

	@Override
	public String getFieldType(String fieldName) throws PortalException {
		DDMFormField ddmFormField = _getDDMFormField(fieldName);

		return ddmFormField.getType();
	}

	@Override
	public DDMForm getFullHierarchyDDMForm() {
		try {
			return createFullHierarchyDDMForm();
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		return new DDMForm();
	}

	@Override
	public Map<String, DDMFormField> getFullHierarchyDDMFormFieldsMap(
		boolean includeNestedDDMFormFields) {

		DDMForm ddmForm = getFullHierarchyDDMForm();

		return ddmForm.getDDMFormFieldsMap(includeNestedDDMFormFields);
	}

	@Override
	public DDMStructureVersion getLatestStructureVersion()
		throws PortalException {

		return DDMStructureVersionLocalServiceUtil.getLatestStructureVersion(
			getStructureId());
	}

	@Override
	public String getName(Locale locale, boolean useDefault) {
		String name = super.getName(locale, useDefault);

		if (Validator.isNull(name)) {
			return super.getName(getDefaultLanguageId(), useDefault);
		}

		return name;
	}

	@Override
	public List<String> getRootFieldNames() {
		DDMForm ddmForm = getFullHierarchyDDMForm();

		List<DDMFormField> ddmFormFields = ddmForm.getDDMFormFields();

		ddmFormFields.removeIf(
			ddmFormField -> GetterUtil.getBoolean(
				ddmFormField.getProperty("upgradedStructure")));

		return getDDMFormFieldNames(ddmFormFields);
	}

	@Override
	public DDMStructureVersion getStructureVersion() throws PortalException {
		return DDMStructureVersionLocalServiceUtil.getStructureVersion(
			getStructureId(), getVersion());
	}

	@Override
	public List<DDMTemplate> getTemplates() {
		return DDMTemplateLocalServiceUtil.getTemplates(getStructureId());
	}

	@Override
	public String getUnambiguousName(
			List<DDMStructure> structures, long groupId, Locale locale)
		throws PortalException {

		if (getGroupId() == groupId) {
			return getName(locale);
		}

		boolean hasAmbiguousName = ListUtil.exists(
			structures,
			structure -> {
				if (structure.getStructureId() == getStructureId()) {
					return false;
				}

				String name = structure.getName(locale);

				if (name.equals(getName(locale))) {
					return true;
				}

				return false;
			});

		if (hasAmbiguousName) {
			Group group = GroupLocalServiceUtil.getGroup(getGroupId());

			return group.getUnambiguousName(getName(locale), locale);
		}

		return getName(locale);
	}

	/**
	 * Returns the WebDAV URL to access the structure.
	 *
	 * @param  themeDisplay the theme display needed to build the URL. It can
	 *         set HTTPS access, the server name, the server port, the path
	 *         context, and the scope group.
	 * @param  webDAVToken the WebDAV token for the URL
	 * @return the WebDAV URL
	 */
	@Override
	public String getWebDavURL(ThemeDisplay themeDisplay, String webDAVToken) {
		StringBundler sb = new StringBundler(8);

		boolean secure = false;

		if (themeDisplay.isSecure() ||
			PropsValues.WEBDAV_SERVLET_HTTPS_REQUIRED) {

			secure = true;
		}

		sb.append(
			PortalUtil.getPortalURL(
				themeDisplay.getServerName(), themeDisplay.getServerPort(),
				secure));
		sb.append(themeDisplay.getPathContext());
		sb.append("/webdav");

		Group group = themeDisplay.getScopeGroup();

		sb.append(group.getFriendlyURL());

		sb.append(StringPool.SLASH);
		sb.append(webDAVToken);
		sb.append("/Structures/");
		sb.append(getStructureId());

		return sb.toString();
	}

	@Override
	public boolean hasField(String fieldName) {
		return _hasField(
			this::_fetchDDMFormField, DDMStructure::hasField, fieldName);
	}

	@Override
	public boolean hasFieldByFieldReference(String fieldReference) {
		return _hasField(
			this::_fetchDDMFormFieldByFieldReference,
			DDMStructure::hasFieldByFieldReference, fieldReference);
	}

	@Override
	public boolean isFieldRepeatable(String fieldName) throws PortalException {
		DDMFormField ddmFormField = _getDDMFormField(fieldName);

		return ddmFormField.isRepeatable();
	}

	@Override
	public boolean isFieldTransient(String fieldName) throws PortalException {
		DDMFormField ddmFormField = _getDDMFormField(fieldName);

		if (Validator.isNull(ddmFormField.getDataType())) {
			return true;
		}

		return false;
	}

	@Override
	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale)
		throws LocaleException {

		super.prepareLocalizedFieldsForImport(defaultImportLocale);

		try {
			setDefinition(
				DDMStructureLocalServiceUtil.
					prepareLocalizedDefinitionForImport(
						this, defaultImportLocale));
		}
		catch (Exception exception) {
			throw new LocaleException(
				LocaleException.TYPE_EXPORT_IMPORT, exception);
		}
	}

	@Override
	public void setClassName(String className) {
		_className = className;
	}

	@Override
	public void setDDMForm(DDMForm ddmForm) {
		_ddmForm = ddmForm;
	}

	@Override
	public void setDefinition(String definition) {
		super.setDefinition(definition);

		_ddmForm = null;
	}

	protected List<DDMFormField> filterTransientDDMFormFields(
		List<DDMFormField> ddmFormFields) {

		return ListUtil.filter(
			ddmFormFields,
			ddmFormField -> Validator.isNotNull(ddmFormField.getDataType()));
	}

	protected List<String> getDDMFormFieldNames(
		List<DDMFormField> ddmFormFields) {

		List<String> fieldNames = new ArrayList<>();

		for (DDMFormField ddmFormField : ddmFormFields) {
			fieldNames.add(ddmFormField.getName());
		}

		return fieldNames;
	}

	protected DDMStructure getParentDDMStructure() throws PortalException {
		if (getParentStructureId() == 0) {
			return null;
		}

		return DDMStructureLocalServiceUtil.getStructure(
			getParentStructureId());
	}

	private DDMFormField _fetchDDMFormField(
		BiFunction<List<DDMFormField>, String, DDMFormField> biFunction,
		List<DDMFormField> ddmFormFields,
		Function<DDMFormField, String> function, String identifier) {

		for (DDMFormField ddmFormField : ddmFormFields) {
			DDMFormField targetDDMFormField = null;

			if (identifier.equals(function.apply(ddmFormField))) {
				targetDDMFormField = ddmFormField;
			}
			else {
				targetDDMFormField = biFunction.apply(
					ddmFormField.getNestedDDMFormFields(), identifier);
			}

			if (targetDDMFormField != null) {
				return targetDDMFormField;
			}
		}

		return null;
	}

	private DDMFormField _fetchDDMFormField(
		List<DDMFormField> ddmFormFields, String fieldName) {

		return _fetchDDMFormField(
			this::_fetchDDMFormField, ddmFormFields, DDMFormField::getName,
			fieldName);
	}

	private DDMFormField _fetchDDMFormFieldByFieldReference(
		List<DDMFormField> ddmFormFields, String fieldReference) {

		return _fetchDDMFormField(
			this::_fetchDDMFormFieldByFieldReference, ddmFormFields,
			DDMFormField::getFieldReference, fieldReference);
	}

	private DDMForm _getDDMForm() {
		if (_ddmForm == null) {
			DDMFormDeserializerDeserializeRequest.Builder builder =
				DDMFormDeserializerDeserializeRequest.Builder.newBuilder(
					getDefinition());

			DDMFormDeserializerDeserializeResponse
				ddmFormDeserializerDeserializeResponse =
					DDMFormJSONDeserializer.internalDeserialize(
						builder.build());

			_ddmForm = ddmFormDeserializerDeserializeResponse.getDDMForm();

			for (DDMFormField ddmFormField : _ddmForm.getDDMFormFields()) {
				if (_isFieldSet(ddmFormField) &&
					ListUtil.isEmpty(ddmFormField.getNestedDDMFormFields())) {

					_setNestedDDMFormFields(ddmFormField);
				}
			}
		}

		return _ddmForm;
	}

	private DDMFormField _getDDMFormField(
			BiFunction<List<DDMFormField>, String, DDMFormField> biFunction,
			String identifier,
			UnsafeBiFunction
				<DDMStructure, String, DDMFormField, PortalException>
					unsafeBiFunction)
		throws PortalException {

		DDMForm ddmForm = _getDDMForm();

		DDMFormField ddmFormField = biFunction.apply(
			ddmForm.getDDMFormFields(), identifier);

		if (ddmFormField != null) {
			return ddmFormField;
		}

		try {
			DDMStructure parentDDMStructure = getParentDDMStructure();

			if (parentDDMStructure != null) {
				return unsafeBiFunction.apply(parentDDMStructure, identifier);
			}
		}
		catch (PortalException portalException) {
			_log.error(portalException);
		}

		throw new StructureFieldException("Unable to find field " + identifier);
	}

	private DDMFormField _getDDMFormField(String fieldName)
		throws PortalException {

		return _getDDMFormField(
			this::_fetchDDMFormField, fieldName, DDMStructure::getDDMFormField);
	}

	private DDMFormField _getDDMFormFieldByFieldReference(String fieldReference)
		throws PortalException {

		return _getDDMFormField(
			this::_fetchDDMFormFieldByFieldReference, fieldReference,
			DDMStructure::getDDMFormFieldByFieldReference);
	}

	private boolean _hasField(
		BiFunction<List<DDMFormField>, String, DDMFormField> biFunction1,
		BiFunction<DDMStructure, String, Boolean> biFunction2,
		String identifier) {

		DDMForm ddmForm = _getDDMForm();

		DDMFormField ddmFormField = biFunction1.apply(
			ddmForm.getDDMFormFields(), identifier);

		if (ddmFormField != null) {
			return true;
		}

		try {
			DDMStructure parentDDMStructure = getParentDDMStructure();

			if (parentDDMStructure != null) {
				return biFunction2.apply(parentDDMStructure, identifier);
			}
		}
		catch (PortalException portalException) {
			_log.error(portalException);
		}

		return false;
	}

	private boolean _isFieldSet(DDMFormField ddmFormField) {
		if (Objects.equals(ddmFormField.getType(), "fieldset")) {
			return true;
		}

		return false;
	}

	private void _setNestedDDMFormFields(DDMFormField ddmFormField) {
		if (Validator.isNotNull(ddmFormField.getProperty("ddmStructureId"))) {
			try {
				DDMStructure ddmStructure =
					DDMStructureLocalServiceUtil.getStructure(
						GetterUtil.getLong(
							ddmFormField.getProperty("ddmStructureId")));

				DDMForm ddmForm = ddmStructure.getDDMForm();

				ddmFormField.setNestedDDMFormFields(ddmForm.getDDMFormFields());
			}
			catch (PortalException portalException) {
				if (_log.isDebugEnabled()) {
					_log.debug(portalException);
				}
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMStructureImpl.class);

	@CacheField
	private String _className;

	@CacheField(methodName = "DDMForm", propagateToInterface = true)
	private DDMForm _ddmForm;

}