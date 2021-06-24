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

<aui:button-row>
	<portlet:renderURL var="createTodoListURL">
		<portlet:param name="controller" value="todo_lists" />
		<portlet:param name="action" value="create" />
	</portlet:renderURL>

	<aui:button href="${createTodoListURL}" icon="icon-plus" value="create-todo-list" />
</aui:button-row>

<liferay-ui:search-container
	emptyResultsMessage="there-are-no-todo-lists"
	iteratorURL="${portletURL}"
	orderByCol="${orderByCol}"
	orderByType="${orderByType}"
>
	<liferay-ui:search-container-results
		results="${todoLists}"
	/>

	<liferay-ui:search-container-row
		className="com.liferay.alloy.mvc.sample.model.TodoList"
		escapedModel="${true}"
		keyProperty="todoListId"
		modelVar="todoList"
	>
		<portlet:renderURL var="viewTodoListURL">
			<portlet:param name="controller" value="todo_lists" />
			<portlet:param name="action" value="view" />
			<portlet:param name="id" value="${todoList.todoListId}" />
		</portlet:renderURL>

		<liferay-ui:search-container-column-text
			href="${viewTodoListURL}"
			orderable="${true}"
			property="name"
		/>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator />
</liferay-ui:search-container>