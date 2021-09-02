create table WebhookEntry (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	webhookEntryId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	active_ BOOLEAN,
	messageBusDestinationName VARCHAR(75) null,
	name VARCHAR(75) null,
	url VARCHAR(75) null
);