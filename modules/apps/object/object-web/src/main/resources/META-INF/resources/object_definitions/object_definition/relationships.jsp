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
String backURL = ParamUtil.getString(request, "backURL", String.valueOf(renderResponse.createRenderURL()));

ObjectDefinition objectDefinition = (ObjectDefinition)request.getAttribute(ObjectWebKeys.OBJECT_DEFINITION);

ObjectDefinitionsRelationshipsDisplayContext objectDefinitionsRelationshipsDisplayContext = (ObjectDefinitionsRelationshipsDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(backURL);

renderResponse.setTitle(objectDefinition.getLabel(locale, true));
%>

<frontend-data-set:headless-display
	apiURL="<%= objectDefinitionsRelationshipsDisplayContext.getAPIURL() %>"
	creationMenu="<%= objectDefinitionsRelationshipsDisplayContext.getCreationMenu() %>"
	fdsActionDropdownItems="<%= objectDefinitionsRelationshipsDisplayContext.getFDSActionDropdownItems() %>"
	formName="fm"
	id="<%= ObjectDefinitionsFDSNames.OBJECT_RELATIONSHIPS %>"
	itemsPerPage="<%= 20 %>"
	namespace="<%= liferayPortletResponse.getNamespace() %>"
	pageNumber="<%= 1 %>"
	portletURL="<%= liferayPortletResponse.createRenderURL() %>"
	propsTransformer="js/ObjectDefinitionsRelationshipsFDSPropsTransformer"
	style="fluid"
/>

<div id="<portlet:namespace />AddObjectRelationship">
	<react:component
		module="js/components/ModalAddObjectRelationship"
		props='<%=
			HashMapBuilder.<String, Object>put(
				"apiURL", objectDefinitionsRelationshipsDisplayContext.getAPIURL()
			).put(
				"ffOneToManyRelationshipCustomAndNativeObjects", GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-151676"))
			).put(
				"ffOneToOneRelationshipConfigurationEnabled", objectDefinitionsRelationshipsDisplayContext.isFFOneToOneRelationshipConfigurationEnabled()
			).put(
				"objectDefinitionId", objectDefinition.getObjectDefinitionId()
			).put(
				"system", objectDefinition.isSystem()
			).build()
		%>'
	/>
</div>

<div id="<portlet:namespace />DeleteObjectRelationship">
	<react:component
		module="js/components/ModalDeleteObjectRelationship"
		props='<%=
			HashMapBuilder.<String, Object>put(
				"isApproved", objectDefinition.isApproved()
			).build()
		%>'
	/>
</div>