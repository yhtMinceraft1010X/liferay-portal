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
		setAlloyServiceInvokerClass(TodoList.class);
		setPermissioned(true);
	}

	public void add() throws Exception {
		_validateAdd();

		TodoList todoList = TodoListLocalServiceUtil.createTodoList(0);

		updateModel(todoList);

		addSuccessMessage();

		String redirect = ParamUtil.getString(portletRequest, "redirect");

		redirectTo(redirect);
	}

	public void create() throws Exception {
		TodoList todoList = TodoListLocalServiceUtil.createTodoList(0);

		renderRequest.setAttribute("todoList", todoList);
	}

	public void delete() throws Exception {
		TodoList todoList = _fetchTodoList();

		_validateDelete(todoList);

		TodoListLocalServiceUtil.deleteTodoList(todoList);

		addSuccessMessage();

		String redirect = ParamUtil.getString(portletRequest, "redirect");

		redirectTo(redirect);
	}

	public void edit() throws Exception {
		TodoList todoList = _fetchTodoList();

		_validateEdit(todoList);

		renderRequest.setAttribute("todoList", todoList);
	}

	public void index() throws Exception {
		renderRequest.setAttribute("portletURL", portletURL);

		SearchContainer<TodoList> searchContainer = new SearchContainer<TodoList>(portletRequest, portletURL, null, null);

		String orderByCol = ParamUtil.getString(portletRequest, "orderByCol", "name");

		renderRequest.setAttribute("orderByCol", orderByCol);

		String orderByType = ParamUtil.getString(portletRequest, "orderByType", "asc");

		renderRequest.setAttribute("orderByType", orderByType);

		OrderByComparator obc = OrderByComparatorFactoryUtil.create(TodoListModelImpl.TABLE_NAME, orderByCol, orderByType.equals("asc"));

		List<TodoList> todoLists = alloyServiceInvoker.executeDynamicQuery(new Object[] {"userId", themeDisplay.getUserId()}, searchContainer.getStart(), searchContainer.getEnd(), obc);

		renderRequest.setAttribute("todoLists", todoLists);
	}

	public void update() throws Exception {
		TodoList todoList = _fetchTodoList();

		_validateUpdate(todoList);

		updateModel(todoList);

		addSuccessMessage();

		String redirect = ParamUtil.getString(portletRequest, "redirect");

		redirectTo(redirect);
	}

	public void view() throws Exception {
		TodoList todoList = _fetchTodoList();

		_validateView(todoList);

		renderRequest.setAttribute("todoList", todoList);

		portletURL.setParameter("todoListId", String.valueOf(todoList.getTodoListId()));

		renderRequest.setAttribute("portletURL", portletURL);

		renderRequest.setAttribute("TodoItemConstantsMethods", new TodoItemConstants());

		SearchContainer<TodoItem> todoItemSearchContainer = new SearchContainer<TodoItem>(portletRequest, portletURL, null, null);

		String todoItemsOrderByCol = ParamUtil.getString(portletRequest, "todoItemsOrderByCol", "priority");

		renderRequest.setAttribute("todoItemsOrderByCol", todoItemsOrderByCol);

		String todoItemsOrderByType = ParamUtil.getString(portletRequest, "todoItemsOrderByType", "asc");

		renderRequest.setAttribute("todoItemsOrderByType", todoItemsOrderByType);

		OrderByComparator todoItemsOBC = OrderByComparatorFactoryUtil.create(TodoListModelImpl.TABLE_NAME, todoItemsOrderByCol, todoItemsOrderByType.equals("asc"));

		AlloyServiceInvoker todoItemAlloyServiceInvoker = new AlloyServiceInvoker(TodoItem.class.getName());

		List<TodoItem> todoItems = todoItemAlloyServiceInvoker.executeDynamicQuery(new Object[] {"todoListId", todoList.getTodoListId()}, todoItemSearchContainer.getStart(), todoItemSearchContainer.getEnd(), todoItemsOBC);

		renderRequest.setAttribute("todoItems", todoItems);
	}

	private TodoList _fetchTodoList() throws Exception {
		long todoListId = ParamUtil.getLong(portletRequest, "id");

		return TodoListLocalServiceUtil.fetchTodoList(todoListId);
	}

	private void _validateAdd() throws Exception {
		_validateName();
	}

	private void _validateDelete(TodoList todoList) throws Exception {
		_validateTodoList(todoList);

		AlloyServiceInvoker todoItemAlloyServiceInvoker = new AlloyServiceInvoker(TodoItem.class.getName());

		List<TodoItem> todoItems = todoItemAlloyServiceInvoker.executeDynamicQuery(new Object[] {"todoListId", todoList.getTodoListId()});

		if (!todoItems.isEmpty()) {
			throw new AlloyException("the-todo-list-is-not-empty", false);
		}
	}

	private void _validateEdit(TodoList todoList) throws Exception {
		_validateTodoList(todoList);
	}

	private void _validateName() throws Exception {
		String name = ParamUtil.getString(portletRequest, "name");

		if (Validator.isNull(name)) {
			throw new AlloyException("the-todo-list-name-is-invalid", false);
		}

		List<TodoList> todoLists = alloyServiceInvoker.executeDynamicQuery(new Object[] {"userId", themeDisplay.getUserId(), "name", name});

		if (!todoLists.isEmpty()) {
			long todoListId = ParamUtil.getLong(portletRequest, "id");

			TodoList todoList = todoLists.get(0);

			if (todoList.getTodoListId() != todoListId) {
				throw new AlloyException("the-todo-list-already-exists", false);
			}
		}
	}

	private void _validateTodoList(TodoList todoList) throws Exception {
		if ((todoList == null) || todoList.isNew()) {
			long todoListId = ParamUtil.getLong(portletRequest, "id");

			throw new AlloyException("the-todo-list-with-id-x-does-not-exist", new Object[] {todoListId}, false);
		}
	}

	private void _validateUpdate(TodoList todoList) throws Exception {
		_validateTodoList(todoList);

		_validateName();
	}

	private void _validateView(TodoList todoList) throws Exception {
		_validateTodoList(todoList);
	}

}
%>