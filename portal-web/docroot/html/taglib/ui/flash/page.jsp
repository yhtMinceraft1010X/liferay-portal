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

<%@ include file="/html/taglib/init.jsp" %>

<%
String randomNamespace = StringUtil.randomId() + StringPool.UNDERLINE;

String height = (String)request.getAttribute("liferay-ui:flash:height");
String version = (String)request.getAttribute("liferay-ui:flash:version");
String width = (String)request.getAttribute("liferay-ui:flash:width");
%>

<div id="<%= randomNamespace %>flashcontent" style="height: <%= height %>; width: <%= width %>;"></div>

<aui:script use="aui-swf-deprecated">
	new A.SWF(
		{
			boundingBox: '#<%= randomNamespace %>flashcontent',
			fixedAttributes: {
				allowFullScreen: '<%= (String)request.getAttribute("liferay-ui:flash:allowFullScreen") %>',
				allowScriptAccess: '<%= (String)request.getAttribute("liferay-ui:flash:allowScriptAccess") %>',
				base: '<%= (String)request.getAttribute("liferay-ui:flash:base") %>',
				bgcolor: '<%= (String)request.getAttribute("liferay-ui:flash:bgcolor") %>',
				devicefont: '<%= (String)request.getAttribute("liferay-ui:flash:devicefont") %>',
				loop: '<%= (String)request.getAttribute("liferay-ui:flash:loop") %>',
				menu: '<%= (String)request.getAttribute("liferay-ui:flash:menu") %>',
				play: '<%= (String)request.getAttribute("liferay-ui:flash:play") %>',
				quality: '<%= (String)request.getAttribute("liferay-ui:flash:quality") %>',
				salign: '<%= (String)request.getAttribute("liferay-ui:flash:salign") %>',
				scale: '<%= (String)request.getAttribute("liferay-ui:flash:scale") %>',
				swliveconnect: '<%= (String)request.getAttribute("liferay-ui:flash:swliveconnect") %>',
				wmode: '<%= (String)request.getAttribute("liferay-ui:flash:wmode") %>'
			},
			flashVars: '<%= (String)request.getAttribute("liferay-ui:flash:flashvars") %>',
			height: '<%= height %>',
			id: '<%= (String)request.getAttribute("liferay-ui:flash:id") %>',
			url: '<%= (String)request.getAttribute("liferay-ui:flash:movie") %>',
			version: <%= version %>,
			width: '<%= width %>'
		}
	).render();
</aui:script>