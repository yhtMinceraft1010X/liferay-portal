create index IX_2193A458 on FriendlyURLEntry (groupId, classNameId);
create index IX_23FDB0B4 on FriendlyURLEntry (groupId, companyId, classNameId, classPK, main);
create index IX_5B329CA4 on FriendlyURLEntry (groupId, companyId, classNameId, classPK, urlTitle[$COLUMN_LENGTH:75$]);
create index IX_B5982CB9 on FriendlyURLEntry (groupId, companyId, classNameId, urlTitle[$COLUMN_LENGTH:75$]);
create index IX_20861768 on FriendlyURLEntry (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_1C7E10E0 on FriendlyURLEntry (uuid_[$COLUMN_LENGTH:75$], groupId);