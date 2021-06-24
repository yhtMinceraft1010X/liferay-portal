create table AlloyMVCSample_TodoItem (
	mvccVersion LONG default 0 not null,
	todoItemId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	todoListId LONG,
	description VARCHAR(75) null,
	priority INTEGER,
	status INTEGER
);

create table AlloyMVCSample_TodoList (
	mvccVersion LONG default 0 not null,
	todoListId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(75) null
);