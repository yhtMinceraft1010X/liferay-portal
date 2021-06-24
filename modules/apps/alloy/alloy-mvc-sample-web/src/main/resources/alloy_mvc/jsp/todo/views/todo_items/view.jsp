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

<aui:fieldset>
	<aui:field-wrapper name="todo-list">
		${todoList.name}
	</aui:field-wrapper>

	<aui:field-wrapper name="status">
		<liferay-ui:message key="${TodoItemConstantsMethods.getStatusLabel(todoItem.status)}" />
	</aui:field-wrapper>

	<aui:field-wrapper name="priority">
		<liferay-ui:message key="${TodoItemConstantsMethods.getPriorityLabel(todoItem.priority)}" />
	</aui:field-wrapper>

	<aui:field-wrapper name="description">
		${todoItem.description}
	</aui:field-wrapper>
</aui:fieldset>

<aui:button-row>
	<portlet:renderURL var="todoListURL">
		<portlet:param name="controller" value="todo_lists" />
		<portlet:param name="action" value="view" />
		<portlet:param name="id" value="${todoItem.todoListId}" />
	</portlet:renderURL>

	<aui:button href="${todoListURL}" icon="icon-arrow-left" value="back" />

	<portlet:renderURL var="editTodoItemURL">
		<portlet:param name="controller" value="todo_items" />
		<portlet:param name="action" value="edit" />
		<portlet:param name="id" value="${todoItem.todoItemId}" />
	</portlet:renderURL>

	<aui:button href="${editTodoItemURL}" icon="icon-pencil" value="edit" />
</aui:button-row>