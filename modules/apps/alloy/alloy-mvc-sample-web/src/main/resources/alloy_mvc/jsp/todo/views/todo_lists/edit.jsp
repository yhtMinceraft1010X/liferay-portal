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

<portlet:actionURL var="updateTodoListURL">
	<portlet:param name="controller" value="todo_lists" />
	<portlet:param name="action" value="update" />
</portlet:actionURL>

<aui:form action="${updateTodoListURL}" method="post">
	<portlet:renderURL var="viewTodoListURL">
		<portlet:param name="controller" value="todo_lists" />
		<portlet:param name="action" value="view" />
		<portlet:param name="id" value="${todoList.todoListId}" />
	</portlet:renderURL>

	<aui:input name="redirect" type="hidden" value="${viewTodoListURL}" />
	<aui:input name="id" type="hidden" value="${todoList.todoListId}" />

	<aui:input name="name" />

	<aui:button-row>
		<portlet:renderURL var="todoListsURL">
			<portlet:param name="controller" value="todo_lists" />
			<portlet:param name="action" value="index" />
		</portlet:renderURL>

		<portlet:actionURL var="deleteTodoListURL">
			<portlet:param name="controller" value="todo_lists" />
			<portlet:param name="action" value="delete" />
			<portlet:param name="id" value="${todoList.todoListId}" />
			<portlet:param name="redirect" value="${todoListsURL}" />
		</portlet:actionURL>

		<aui:button href="${deleteTodoListURL}" icon="icon-trash" value="delete" />

		<aui:button icon="icon-ok" type="submit" value="update" />

		<aui:button href="${viewTodoListURL}" icon="icon-remove" value="cancel" />
	</aui:button-row>
</aui:form>