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

import ClayTable from '@clayui/table';
import {openToast} from 'frontend-js-web';
import PropTypes from 'prop-types';
import React, {useCallback, useEffect, useMemo, useRef, useState} from 'react';

import SaveTemplate from '../SaveTemplate';
import {
	FILE_SCHEMA_EVENT,
	SCHEMA_SELECTED_EVENT,
	TEMPLATE_SELECTED_EVENT,
	TEMPLATE_SOILED_EVENT,
} from '../constants';
import getFieldsFromSchema from '../getFieldsFromSchema';
import {getAvailableMappings} from '../utilities/mappings';
import ImportMappingItem from './ImportMappingItem';
import ImportSubmit from './ImportSubmit';

const TableFieldsHeader = () => (
	<ClayTable.Head>
		<ClayTable.Row>
			<ClayTable.Cell headingCell headingTitle>
				{Liferay.Language.get('destination-field')}
			</ClayTable.Cell>

			<ClayTable.Cell headingCell headingTitle>
				{Liferay.Language.get('source-file-field')}
			</ClayTable.Cell>

			<ClayTable.Cell headingCell headingTitle>
				{Liferay.Language.get('preview')}
			</ClayTable.Cell>
		</ClayTable.Row>
	</ClayTable.Head>
);
function ImportForm({
	formDataQuerySelector,
	formImportURL,
	formSaveAsTemplateURL,
	mappedFields,
	portletNamespace,
}) {
	const [dbFields, setDbFields] = useState({
		optional: [],
		required: [],
	});
	const [formEvaluated, setFormEvaluated] = useState(false);
	const [fileFields, setFileFields] = useState();
	const [fileContent, setFileContent] = useState();
	const [fieldsSelections, setFieldsSelections] = useState({});
	const [mappingsToBeEvaluated, setMappingsToBeEvaluated] = useState(
		mappedFields
	);
	const useTemplateMappingRef = useRef();

	const formIsValid = useMemo(() => {
		if (
			!Object.keys(fieldsSelections).length ||
			!(dbFields.optional.length + dbFields.required.length)
		) {
			return false;
		}

		const requiredFieldNotFilled = dbFields.required.some(
			(dbField) => !fieldsSelections[dbField.name]
		);

		return !requiredFieldNotFilled;
	}, [fieldsSelections, dbFields]);

	const updateFieldMapping = (fileField, dbFieldName) => {
		setFieldsSelections((prevSelections) => {
			if (fileField) {
				return {
					...prevSelections,
					[dbFieldName]: fileField,
				};
			}

			const updatedDbFields = {...prevSelections};

			delete updatedDbFields[dbFieldName];

			return updatedDbFields;
		});

		Liferay.fire(TEMPLATE_SOILED_EVENT);
	};

	useEffect(() => {
		const dbFieldsUnordered = [...dbFields.optional, ...dbFields.required];

		if (
			dbFields.optional.length + dbFields.required.length &&
			fileFields &&
			!useTemplateMappingRef.current
		) {
			const availableMappings = getAvailableMappings(
				mappingsToBeEvaluated,
				fileFields,
				dbFieldsUnordered
			);

			setFieldsSelections(availableMappings);
		}
	}, [dbFields, fileFields, mappingsToBeEvaluated]);

	useEffect(() => {
		function handleSchemaUpdated({schema}) {
			const newDBFields = getFieldsFromSchema(schema);

			setDbFields(newDBFields);
		}

		function handleFileSchemaUpdate({fileContent, schema}) {
			setFileContent(fileContent);

			setFileFields(schema);
		}

		function handleTemplateSelect({template}) {
			if (template) {
				setMappingsToBeEvaluated(template.mappings);
			}
		}

		Liferay.on(FILE_SCHEMA_EVENT, handleFileSchemaUpdate);
		Liferay.on(SCHEMA_SELECTED_EVENT, handleSchemaUpdated);
		Liferay.on(TEMPLATE_SELECTED_EVENT, handleTemplateSelect);

		return () => {
			Liferay.detach(FILE_SCHEMA_EVENT, handleFileSchemaUpdate);
			Liferay.detach(SCHEMA_SELECTED_EVENT, handleSchemaUpdated);
			Liferay.detach(TEMPLATE_SELECTED_EVENT, handleTemplateSelect);
		};
	}, []);

	const formIsVisible = !!(
		dbFields.optional.length + dbFields.required.length
	);

	const handleEvaluateForm = useCallback(() => {
		setFormEvaluated(true);

		if (!formIsVisible) {
			openToast({
				message: Liferay.Language.get(
					'please-upload-a-file-and-select-the-required-columns-before-continuing'
				),
				type: 'danger',
			});

			return;
		}

		if (!formIsValid) {
			openToast({
				message: Liferay.Language.get(
					'you-must-map-all-required-fields-before-continuing'
				),
				title: Liferay.Language.get('error'),
				type: 'danger',
			});
		}
	}, [formIsValid, formIsVisible]);

	return (
		<>
			{formIsVisible && (
				<div className="card import-mapping-table">
					<h4 className="card-header">
						{Liferay.Language.get('import-mappings')}
					</h4>

					<div className="card-body p-0">
						<ClayTable borderless hover={false}>
							<TableFieldsHeader />

							<ClayTable.Body>
								{!!dbFields.required.length && (
									<>
										<ClayTable.Row divider>
											<ClayTable.Cell
												className="text-uppercase"
												colSpan={3}
											>
												{Liferay.Language.get(
													'required-fields'
												)}
											</ClayTable.Cell>
										</ClayTable.Row>

										{dbFields.required.map((dbField) => (
											<ImportMappingItem
												dbField={dbField}
												fileFields={fileFields}
												formEvaluated={formEvaluated}
												key={dbField.name}
												portletNamespace={
													portletNamespace
												}
												previewValue={
													fieldsSelections[
														dbField.name
													] &&
													fileContent?.length &&
													fileContent[0][
														fieldsSelections[
															dbField.name
														]
													]
												}
												required={true}
												selectedFileField={
													fieldsSelections[
														dbField.name
													] || ''
												}
												updateFieldMapping={(
													selectedFileField
												) =>
													updateFieldMapping(
														selectedFileField,
														dbField.name
													)
												}
											/>
										))}
									</>
								)}

								{!!dbFields.optional.length && (
									<>
										<ClayTable.Row divider>
											<ClayTable.Cell
												className="text-uppercase"
												colSpan={3}
											>
												{Liferay.Language.get(
													'optional-fields'
												)}
											</ClayTable.Cell>
										</ClayTable.Row>

										{dbFields.optional.map((dbField) => (
											<ImportMappingItem
												dbField={dbField}
												fileFields={fileFields}
												formEvaluated={formEvaluated}
												key={dbField.name}
												portletNamespace={
													portletNamespace
												}
												previewValue={
													fieldsSelections[
														dbField.name
													] &&
													fileContent?.length &&
													fileContent[0][
														fieldsSelections[
															dbField.name
														]
													]
												}
												required={false}
												selectedFileField={
													fieldsSelections[
														dbField.name
													] || ''
												}
												updateFieldMapping={(
													selectedFileField
												) =>
													updateFieldMapping(
														selectedFileField,
														dbField.name
													)
												}
											/>
										))}
									</>
								)}
							</ClayTable.Body>
						</ClayTable>
					</div>
				</div>
			)}

			<div className="mt-4 sheet-footer">
				<SaveTemplate
					evaluateForm={handleEvaluateForm}
					formIsValid={formIsValid}
					formIsVisible={formIsVisible}
					formSaveAsTemplateDataQuerySelector={formDataQuerySelector}
					formSaveAsTemplateURL={formSaveAsTemplateURL}
					portletNamespace={portletNamespace}
					type="import"
				/>

				<ImportSubmit
					evaluateForm={handleEvaluateForm}
					fieldsSelections={fieldsSelections}
					fileContent={fileContent}
					formDataQuerySelector={formDataQuerySelector}
					formImportURL={formImportURL}
					formIsValid={formIsValid}
					formIsVisible={formIsVisible}
				/>
			</div>
		</>
	);
}

ImportForm.defaultProps = {
	mappedFields: {},
};

ImportForm.propTypes = {
	formDataQuerySelector: PropTypes.string.isRequired,
	formImportURL: PropTypes.string.isRequired,
	formSaveAsTemplateURL: PropTypes.string.isRequired,
	portletNamespace: PropTypes.string.isRequired,
};

export default ImportForm;
