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
ViewObjectDefinitionsDisplayContext viewObjectDefinitionsDisplayContext = (ViewObjectDefinitionsDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<frontend-data-set:headless-display
	apiURL="<%= viewObjectDefinitionsDisplayContext.getAPIURL() %>"
	creationMenu="<%= viewObjectDefinitionsDisplayContext.getCreationMenu() %>"
	fdsActionDropdownItems="<%= viewObjectDefinitionsDisplayContext.getFDSActionDropdownItems() %>"
	formName="fm"
	id="<%= ObjectDefinitionsFDSNames.OBJECT_DEFINITIONS %>"
	style="fluid"
/>

<div id="<portlet:namespace />addObjectDefinition">
	<react:component
		module="js/components/ModalAddObjectDefinition"
		props='<%=
			HashMapBuilder.<String, Object>put(
				"apiURL", viewObjectDefinitionsDisplayContext.getAPIURL()
			).build()
		%>'
	/>
</div>