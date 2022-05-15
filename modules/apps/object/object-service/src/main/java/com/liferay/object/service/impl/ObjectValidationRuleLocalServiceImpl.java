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

package com.liferay.object.service.impl;

import com.liferay.object.exception.ObjectValidationRuleEngineException;
import com.liferay.object.exception.ObjectValidationRuleNameException;
import com.liferay.object.exception.ObjectValidationRuleScriptException;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectValidationRule;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.base.ObjectValidationRuleLocalServiceBaseImpl;
import com.liferay.object.validation.rule.ObjectValidationRuleEngine;
import com.liferay.object.validation.rule.ObjectValidationRuleEngineServicesTracker;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(
	property = "model.class.name=com.liferay.object.model.ObjectValidationRule",
	service = AopService.class
)
public class ObjectValidationRuleLocalServiceImpl
	extends ObjectValidationRuleLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public ObjectValidationRule addObjectValidationRule(
			long userId, long objectDefinitionId, boolean active, String engine,
			Map<Locale, String> errorLabelMap, Map<Locale, String> nameMap,
			String script)
		throws PortalException {

		_validateEngine(engine);
		_validateName(nameMap);
		_validateScript(script);

		ObjectValidationRule objectValidationRule =
			objectValidationRulePersistence.create(
				counterLocalService.increment());

		User user = _userLocalService.getUser(userId);

		objectValidationRule.setCompanyId(user.getCompanyId());
		objectValidationRule.setUserId(user.getUserId());
		objectValidationRule.setUserName(user.getFullName());

		objectValidationRule.setObjectDefinitionId(objectDefinitionId);
		objectValidationRule.setActive(active);
		objectValidationRule.setEngine(engine);
		objectValidationRule.setErrorLabelMap(errorLabelMap);
		objectValidationRule.setNameMap(nameMap);
		objectValidationRule.setScript(script);

		return objectValidationRulePersistence.update(objectValidationRule);
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	public ObjectValidationRule deleteObjectValidationRule(
			long objectValidationRuleId)
		throws PortalException {

		ObjectValidationRule objectValidationRule =
			objectValidationRulePersistence.findByPrimaryKey(
				objectValidationRuleId);

		return deleteObjectValidationRule(objectValidationRule);
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public ObjectValidationRule deleteObjectValidationRule(
		ObjectValidationRule objectValidationRule) {

		return objectValidationRulePersistence.remove(objectValidationRule);
	}

	@Override
	public void deleteObjectValidationRules(Long objectDefinitionId)
		throws PortalException {

		for (ObjectValidationRule objectValidationRule :
				objectValidationRulePersistence.findByObjectDefinitionId(
					objectDefinitionId)) {

			objectValidationRuleLocalService.deleteObjectValidationRule(
				objectValidationRule);
		}
	}

	@Override
	public ObjectValidationRule getObjectValidationRule(
			long objectValidationRuleId)
		throws PortalException {

		return objectValidationRulePersistence.findByPrimaryKey(
			objectValidationRuleId);
	}

	@Override
	public List<ObjectValidationRule> getObjectValidationRules(
		long objectDefinitionId) {

		return objectValidationRulePersistence.findByObjectDefinitionId(
			objectDefinitionId);
	}

	@Override
	public List<ObjectValidationRule> getObjectValidationRules(
		long objectDefinitionId, boolean active) {

		return objectValidationRulePersistence.findByODI_A(
			objectDefinitionId, active);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public ObjectValidationRule updateObjectValidationRule(
			long objectValidationRuleId, boolean active, String engine,
			Map<Locale, String> errorLabelMap, Map<Locale, String> nameMap,
			String script)
		throws PortalException {

		_validateEngine(engine);
		_validateName(nameMap);
		_validateScript(script);

		ObjectValidationRule objectValidationRule =
			objectValidationRulePersistence.findByPrimaryKey(
				objectValidationRuleId);

		objectValidationRule.setActive(active);
		objectValidationRule.setEngine(engine);
		objectValidationRule.setErrorLabelMap(errorLabelMap);
		objectValidationRule.setNameMap(nameMap);
		objectValidationRule.setScript(script);

		return objectValidationRulePersistence.update(objectValidationRule);
	}

	@Override
	public void validate(ObjectEntry objectEntry) throws PortalException {
		if (objectEntry == null) {
			return;
		}

		Map<String, Serializable> values = _objectEntryLocalService.getValues(
			objectEntry);

		HashMapBuilder.HashMapWrapper<String, Object> hashMapWrapper =
			HashMapBuilder.<String, Object>putAll(
				objectEntry.getModelAttributes());

		if (values != null) {
			hashMapWrapper.putAll(values);
		}

		if (PrincipalThreadLocal.getUserId() > 0) {
			hashMapWrapper.put(
				"currentUserId", PrincipalThreadLocal.getUserId());
		}

		List<ObjectValidationRule> objectValidationRules =
			objectValidationRuleLocalService.getObjectValidationRules(
				objectEntry.getObjectDefinitionId(), true);

		for (ObjectValidationRule objectValidationRule :
				objectValidationRules) {

			ObjectValidationRuleEngine objectValidationRuleEngine =
				_objectValidationRuleEngineServicesTracker.
					getObjectValidationRuleEngine(
						objectValidationRule.getEngine());

			Map<String, Object> results = objectValidationRuleEngine.execute(
				hashMapWrapper.build(), objectValidationRule.getScript());

			if (GetterUtil.getBoolean(results.get("invalidScript"))) {
				throw new ObjectValidationRuleScriptException(
					"Script is invalid");
			}

			if (GetterUtil.getBoolean(results.get("invalidFields"))) {
				throw new ObjectValidationRuleEngineException(
					objectValidationRule.getErrorLabel(
						LocaleUtil.getMostRelevantLocale()));
			}
		}
	}

	private void _validateEngine(String engine) throws PortalException {
		if (Validator.isNull(engine)) {
			throw new ObjectValidationRuleEngineException("Engine is null");
		}

		ObjectValidationRuleEngine objectValidationRuleEngine =
			_objectValidationRuleEngineServicesTracker.
				getObjectValidationRuleEngine(engine);

		if (objectValidationRuleEngine == null) {
			throw new ObjectValidationRuleEngineException(
				"Engine \"" + engine + "\" does not exist");
		}
	}

	private void _validateName(Map<Locale, String> nameMap)
		throws PortalException {

		Locale locale = LocaleUtil.getSiteDefault();

		if ((nameMap == null) || Validator.isNull(nameMap.get(locale))) {
			throw new ObjectValidationRuleNameException(
				"Name is null for locale " + locale.getDisplayName());
		}
	}

	private void _validateScript(String script) throws PortalException {
		if (Validator.isNull(script)) {
			throw new ObjectValidationRuleScriptException("Script is null");
		}
	}

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

	@Reference
	private ObjectValidationRuleEngineServicesTracker
		_objectValidationRuleEngineServicesTracker;

	@Reference
	private UserLocalService _userLocalService;

}