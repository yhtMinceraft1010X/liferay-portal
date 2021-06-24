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

<portlet:actionURL var="addTodoItemURL">
	<portlet:param name="controller" value="todo_items" />
	<portlet:param name="action" value="add" />
	<portlet:param name="todoListId" value="${param.todoListId}" />
</portlet:actionURL>

<aui:form action="${addTodoItemURL}" method="post">
	<portlet:renderURL var="todoListURL">
		<portlet:param name="controller" value="todo_lists" />
		<portlet:param name="action" value="view" />
		<portlet:param name="id" value="${param.todoListId}" />
	</portlet:renderURL>

	<aui:input name="redirect" type="hidden" value="${todoListURL}" />

	<aui:select label="priority" name="priority">
		<aui:option label="${TodoItemConstants.LABEL_LOW}" selected="${true}" value="${TodoItemConstants.PRIORITY_LOW}" />
		<aui:option label="${TodoItemConstants.LABEL_MODERATE}" value="${TodoItemConstants.PRIORITY_MODERATE}" />
		<aui:option label="${TodoItemConstants.LABEL_HIGH}" value="${TodoItemConstants.PRIORITY_HIGH}" />
	</aui:select>

	<aui:input name="description" />

	<aui:button-row>
		<aui:button icon="icon-plus" type="submit" value="add" />

		<aui:button href="${todoListURL}" icon="icon-remove" value="cancel" />
	</aui:button-row>
</aui:form>