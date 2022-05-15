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

ObjectDefinitionsLayoutsDisplayContext objectDefinitionsLayoutsDisplayContext = (ObjectDefinitionsLayoutsDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(backURL);

renderResponse.setTitle(objectDefinition.getLabel(locale, true));
%>

<frontend-data-set:headless-display
	apiURL="<%= objectDefinitionsLayoutsDisplayContext.getAPIURL() %>"
	creationMenu="<%= objectDefinitionsLayoutsDisplayContext.getCreationMenu() %>"
	fdsActionDropdownItems="<%= objectDefinitionsLayoutsDisplayContext.getFDSActionDropdownItems() %>"
	formName="fm"
	id="<%= ObjectDefinitionsFDSNames.OBJECT_LAYOUTS %>"
	style="fluid"
/>

<div id="<portlet:namespace />AddObjectLayout">
	<react:component
		module="js/components/ModalAddObjectLayout"
		props='<%=
			HashMapBuilder.<String, Object>put(
				"apiURL", objectDefinitionsLayoutsDisplayContext.getAPIURL()
			).build()
		%>'
	/>
</div>