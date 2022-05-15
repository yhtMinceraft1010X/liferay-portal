create index IX_EAC5DE50 on BatchEngineExportTask (companyId, externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_DADA545C on BatchEngineExportTask (executeStatus[$COLUMN_LENGTH:75$]);
create index IX_822E7A2F on BatchEngineExportTask (uuid_[$COLUMN_LENGTH:75$], companyId);

create index IX_2BBBB941 on BatchEngineImportTask (companyId, externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_ABC8050B on BatchEngineImportTask (executeStatus[$COLUMN_LENGTH:75$]);
create index IX_BE725720 on BatchEngineImportTask (uuid_[$COLUMN_LENGTH:75$], companyId);

create index IX_863EDEA9 on BatchEngineImportTaskError (batchEngineImportTaskId);