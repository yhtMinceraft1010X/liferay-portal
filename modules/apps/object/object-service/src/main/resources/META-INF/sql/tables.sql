create table ObjectAction (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectActionId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectDefinitionId LONG,
	active_ BOOLEAN,
	description VARCHAR(75) null,
	name VARCHAR(75) null,
	objectActionExecutorKey VARCHAR(75) null,
	objectActionTriggerKey VARCHAR(75) null,
	parameters TEXT null
);

create table ObjectDefinition (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectDefinitionId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	descriptionObjectFieldId LONG,
	titleObjectFieldId LONG,
	active_ BOOLEAN,
	dbTableName VARCHAR(75) null,
	label STRING null,
	className VARCHAR(75) null,
	name VARCHAR(75) null,
	panelAppOrder VARCHAR(75) null,
	panelCategoryKey VARCHAR(75) null,
	pkObjectFieldDBColumnName VARCHAR(75) null,
	pkObjectFieldName VARCHAR(75) null,
	pluralLabel STRING null,
	portlet BOOLEAN,
	scope VARCHAR(75) null,
	storageType VARCHAR(75) null,
	system_ BOOLEAN,
	version INTEGER,
	status INTEGER
);

create table ObjectEntry (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectEntryId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	externalReferenceCode VARCHAR(75) null,
	objectDefinitionId LONG,
	lastPublishDate DATE null,
	status INTEGER,
	statusByUserId LONG,
	statusByUserName VARCHAR(75) null,
	statusDate DATE null
);

create table ObjectField (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectFieldId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	listTypeDefinitionId LONG,
	objectDefinitionId LONG,
	businessType VARCHAR(75) null,
	dbColumnName VARCHAR(75) null,
	dbTableName VARCHAR(75) null,
	dbType VARCHAR(75) null,
	indexed BOOLEAN,
	indexedAsKeyword BOOLEAN,
	indexedLanguageId VARCHAR(75) null,
	label STRING null,
	name VARCHAR(75) null,
	relationshipType VARCHAR(75) null,
	required BOOLEAN
);

create table ObjectFieldSetting (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectFieldSettingId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectFieldId LONG,
	name VARCHAR(75) null,
	value VARCHAR(75) null
);

create table ObjectLayout (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectLayoutId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectDefinitionId LONG,
	defaultObjectLayout BOOLEAN,
	name STRING null
);

create table ObjectLayoutBox (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectLayoutBoxId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectLayoutTabId LONG,
	collapsable BOOLEAN,
	name STRING null,
	priority INTEGER
);

create table ObjectLayoutColumn (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectLayoutColumnId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectFieldId LONG,
	objectLayoutRowId LONG,
	priority INTEGER,
	size_ INTEGER
);

create table ObjectLayoutRow (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectLayoutRowId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectLayoutBoxId LONG,
	priority INTEGER
);

create table ObjectLayoutTab (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectLayoutTabId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectLayoutId LONG,
	objectRelationshipId LONG,
	name STRING null,
	priority INTEGER
);

create table ObjectRelationship (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectRelationshipId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectDefinitionId1 LONG,
	objectDefinitionId2 LONG,
	objectFieldId2 LONG,
	deletionType VARCHAR(75) null,
	dbTableName VARCHAR(75) null,
	label STRING null,
	name VARCHAR(75) null,
	reverse BOOLEAN,
	type_ VARCHAR(75) null
);

create table ObjectValidationRule (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectValidationRuleId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectDefinitionId LONG,
	active_ BOOLEAN,
	engine VARCHAR(75) null,
	errorLabel STRING null,
	name STRING null,
	script TEXT null
);

create table ObjectView (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectViewId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectDefinitionId LONG,
	defaultObjectView BOOLEAN,
	name STRING null
);

create table ObjectViewColumn (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectViewColumnId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectViewId LONG,
	label STRING null,
	objectFieldName VARCHAR(75) null,
	priority INTEGER
);

create table ObjectViewFilterColumn (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectViewFilterColumnId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectViewId LONG,
	filterType VARCHAR(75) null,
	json VARCHAR(75) null,
	objectFieldName VARCHAR(75) null
);

create table ObjectViewSortColumn (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	objectViewSortColumnId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	objectViewId LONG,
	objectFieldName VARCHAR(75) null,
	priority INTEGER,
	sortOrder VARCHAR(75) null
);