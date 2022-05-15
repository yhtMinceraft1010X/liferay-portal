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
ViewListTypeDefinitionsDisplayContext viewListTypeDefinitionsDisplayContext = (ViewListTypeDefinitionsDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<frontend-data-set:headless-display
	apiURL="<%= viewListTypeDefinitionsDisplayContext.getAPIURL() %>"
	creationMenu="<%= viewListTypeDefinitionsDisplayContext.getCreationMenu() %>"
	fdsActionDropdownItems="<%= viewListTypeDefinitionsDisplayContext.getFDSActionDropdownItems() %>"
	formName="fm"
	id="<%= ListTypeFDSNames.LIST_TYPE_DEFINITIONS %>"
	style="fluid"
/>

<div id="<portlet:namespace />addListTypeDefinition">
	<react:component
		module="js/components/ModalAddListTypeDefinition"
		props='<%=
			HashMapBuilder.<String, Object>put(
				"apiURL", viewListTypeDefinitionsDisplayContext.getAPIURL()
			).build()
		%>'
	/>
</div>