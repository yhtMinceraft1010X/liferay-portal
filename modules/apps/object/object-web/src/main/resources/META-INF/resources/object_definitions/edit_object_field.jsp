<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
ObjectDefinition objectDefinition = (ObjectDefinition)request.getAttribute(ObjectWebKeys.OBJECT_DEFINITION);
ObjectDefinitionsFieldsDisplayContext objectDefinitionsFieldsDisplayContext = (ObjectDefinitionsFieldsDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
ObjectField objectField = (ObjectField)request.getAttribute(ObjectWebKeys.OBJECT_FIELD);
%>

<react:component
	module="js/components/EditObjectField"
	props='<%=
		HashMapBuilder.<String, Object>put(
			"forbiddenChars", PropsUtil.getArray(PropsKeys.DL_CHAR_BLACKLIST)
		).put(
			"forbiddenLastChars", objectDefinitionsFieldsDisplayContext.getForbiddenLastCharacters()
		).put(
			"forbiddenNames", PropsUtil.getArray(PropsKeys.DL_NAME_BLACKLIST)
		).put(
			"isApproved", objectDefinition.isApproved()
		).put(
			"objectField", objectDefinitionsFieldsDisplayContext.getObjectFieldJSONObject(objectField)
		).put(
			"objectFieldTypes", objectDefinitionsFieldsDisplayContext.getObjectFieldBusinessTypeMaps(Validator.isNotNull(objectField.getRelationshipType()), locale)
		).put(
			"objectName", objectDefinition.getShortName()
		).put(
			"readOnly", !objectDefinitionsFieldsDisplayContext.hasUpdateObjectDefinitionPermission()
		).build()
	%>'
/>