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

<%@ include file="/alloy_mvc/jsp/todo/controllers/init.jspf" %>

<%!
public static class AlloyControllerImpl extends BaseAlloyControllerImpl {

	public AlloyControllerImpl() {
		setAlloyServiceInvokerClass(TodoItem.class);
		setPermissioned(true);
	}

	public void add() throws Exception {
		_validateAdd();

		TodoItem todoItem = TodoItemLocalServiceUtil.createTodoItem(0);

		updateModel(todoItem, "status", 0);

		addSuccessMessage();

		String redirect = ParamUtil.getString(portletRequest, "redirect");

		redirectTo(redirect);
	}

	public void create() throws Exception {
		renderRequest.setAttribute("TodoItemConstants", getConstantsBean(TodoItemConstants.class));

		TodoItem todoItem = TodoItemLocalServiceUtil.createTodoItem(0);

		renderRequest.setAttribute("todoItem", todoItem);

		long todoListId = ParamUtil.getLong(portletRequest, "todoListId");

		renderRequest.setAttribute("todoListId", todoListId);
	}

	public void delete() throws Exception {
		TodoItem todoItem = _fetchTodoItem();

		_validateDelete(todoItem);

		TodoItemLocalServiceUtil.deleteTodoItem(todoItem);

		addSuccessMessage();

		String redirect = ParamUtil.getString(portletRequest, "redirect");

		redirectTo(redirect);
	}

	public void edit() throws Exception {
		TodoItem todoItem = _fetchTodoItem();

		_validateEdit(todoItem);

		renderRequest.setAttribute("TodoItemConstants", getConstantsBean(TodoItemConstants.class));

		renderRequest.setAttribute("todoItem", todoItem);

		renderRequest.setAttribute("todoListId", todoItem.getTodoListId());

		AlloyServiceInvoker todoListAlloyServiceInvoker = new AlloyServiceInvoker(TodoList.class.getName());

		List<TodoList> todoLists = todoListAlloyServiceInvoker.executeDynamicQuery(new Object[] {"userId", themeDisplay.getUserId()});

		renderRequest.setAttribute("todoLists", todoLists);
	}

	public void update() throws Exception {
		TodoItem todoItem = _fetchTodoItem();

		_validateUpdate(todoItem);

		updateModel(todoItem);

		addSuccessMessage();

		String redirect = ParamUtil.getString(portletRequest, "redirect");

		redirectTo(redirect);
	}

	public void view() throws Exception {
		TodoItem todoItem = _fetchTodoItem();

		_validateView(todoItem);

		renderRequest.setAttribute("TodoItemConstants", getConstantsBean(TodoItemConstants.class));
		renderRequest.setAttribute("TodoItemConstantsMethods", new TodoItemConstants());

		renderRequest.setAttribute("todoItem", todoItem);

		TodoList todoList = TodoListLocalServiceUtil.fetchTodoList(todoItem.getTodoListId());

		renderRequest.setAttribute("todoList", todoList);
	}

	private TodoItem _fetchTodoItem() throws Exception {
		long todoItemId = ParamUtil.getLong(portletRequest, "id");

		return TodoItemLocalServiceUtil.fetchTodoItem(todoItemId);
	}

	private void _validateAdd() throws Exception {
		_validateDescription();
	}

	private void _validateDelete(TodoItem todoItem) throws Exception {
		_validateTodoItem(todoItem);
	}

	private void _validateDescription() throws Exception {
		String description = ParamUtil.getString(portletRequest, "description");

		if (Validator.isNull(description)) {
			throw new AlloyException("the-todo-item-description-is-invalid", false);
		}

		List<TodoItem> todoItems = alloyServiceInvoker.executeDynamicQuery(new Object[] {"userId", themeDisplay.getUserId(), "description", description});

		if (!todoItems.isEmpty()) {
			TodoItem todoItem = todoItems.get(0);

			long todoItemId = ParamUtil.getLong(portletRequest, "id");

			if (todoItem.getTodoItemId() != todoItemId) {
				throw new AlloyException("the-todo-item-already-exists", false);
			}
		}
	}

	private void _validateEdit(TodoItem todoItem) throws Exception {
		_validateTodoItem(todoItem);
	}

	private void _validateTodoItem(TodoItem todoItem) throws Exception {
		if ((todoItem == null) || todoItem.isNew()) {
			long todoItemId = ParamUtil.getLong(portletRequest, "id");

			throw new AlloyException("the-todo-item-with-id-x-does-not-exist", new Object[] {todoItemId}, false);
		}
	}

	private void _validateUpdate(TodoItem todoItem) throws Exception {
		_validateTodoItem(todoItem);

		_validateDescription();
	}

	private void _validateView(TodoItem todoItem) throws Exception {
		_validateTodoItem(todoItem);
	}

}
%>