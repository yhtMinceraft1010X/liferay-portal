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

<%@ include file="/alloy_mvc/jsp/todo/views/init.jsp" %>

<aui:model-context bean="${todoList}" model="<%= TodoList.class %>" />

<portlet:actionURL var="addTodoListURL">
	<portlet:param name="controller" value="todo_lists" />
	<portlet:param name="action" value="add" />
</portlet:actionURL>

<aui:form action="${addTodoListURL}" method="post">
	<portlet:renderURL var="todoListsURL">
		<portlet:param name="controller" value="todo_lists" />
		<portlet:param name="action" value="index" />
	</portlet:renderURL>

	<aui:input name="redirect" type="hidden" value="${todoListsURL}" />

	<aui:input name="name" />

	<aui:button-row>
		<aui:button icon="icon-plus" type="submit" value="add" />

		<aui:button href="${todoListsURL}" icon="icon-remove" value="cancel" />
	</aui:button-row>
</aui:form>