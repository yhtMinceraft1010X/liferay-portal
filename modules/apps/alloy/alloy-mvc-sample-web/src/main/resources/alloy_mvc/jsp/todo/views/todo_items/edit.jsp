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

<aui:model-context bean="${todoItem}" model="<%= TodoItem.class %>" />

<portlet:actionURL var="updateTodoItemURL">
	<portlet:param name="controller" value="todo_items" />
	<portlet:param name="action" value="update" />
	<portlet:param name="id" value="${todoItem.todoItemId}" />
</portlet:actionURL>

<aui:form action="${updateTodoItemURL}" method="post">
	<portlet:renderURL var="viewTodoItemURL">
		<portlet:param name="controller" value="todo_items" />
		<portlet:param name="action" value="view" />
		<portlet:param name="id" value="${todoItem.todoItemId}" />
	</portlet:renderURL>

	<aui:input name="redirect" type="hidden" value="${viewTodoItemURL}" />
	<aui:input name="todoItemId" type="hidden" value="${todoItem.todoItemId}" />

	<aui:select label="todo-lists" name="todoListId">
		<c:forEach items="${todoLists}" var="todoList">
			<aui:option label="${todoList.name}" selected="${todoItem.todoListId == todoList.todoListId}" value="${todoList.todoListId}" />
		</c:forEach>
	</aui:select>

	<aui:select label="status" name="status" showEmptyOption="${false}">
		<aui:option label="${TodoItemConstants.LABEL_COMPLETE}" value="${TodoItemConstants.STATUS_COMPLETE}" />
		<aui:option label="${TodoItemConstants.LABEL_INCOMPLETE}" value="${TodoItemConstants.STATUS_INCOMPLETE}" />
	</aui:select>

	<aui:select label="priority" name="priority" showEmptyOption="${false}">
		<aui:option label="${TodoItemConstants.LABEL_LOW}" value="${TodoItemConstants.PRIORITY_LOW}" />
		<aui:option label="${TodoItemConstants.LABEL_MODERATE}" value="${TodoItemConstants.PRIORITY_MODERATE}" />
		<aui:option label="${TodoItemConstants.LABEL_HIGH}" value="${TodoItemConstants.PRIORITY_HIGH}" />
	</aui:select>

	<aui:input name="description" />

	<aui:button-row>
		<portlet:renderURL var="todoListURL">
			<portlet:param name="controller" value="todo_lists" />
			<portlet:param name="action" value="view" />
			<portlet:param name="id" value="${todoListId}" />
		</portlet:renderURL>

		<portlet:actionURL var="deleteTodoItemURL">
			<portlet:param name="controller" value="todo_items" />
			<portlet:param name="action" value="delete" />
			<portlet:param name="id" value="${todoItem.todoItemId}" />
			<portlet:param name="redirect" value="${todoListURL}" />
		</portlet:actionURL>

		<aui:button href="${deleteTodoItemURL}" icon="icon-trash" value="delete" />

		<aui:button icon="icon-ok" type="submit" value="update" />

		<aui:button href="${viewTodoItemURL}" icon="icon-remove" value="cancel" />
	</aui:button-row>
</aui:form>