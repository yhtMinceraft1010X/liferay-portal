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

import ClayLink from '@clayui/link';
import PropTypes from 'prop-types';
import React, {useEffect, useMemo, useRef, useState} from 'react';

import SaveTemplate from '../SaveTemplate';
import {
	FILE_SCHEMA_EVENT,
	SCHEMA_SELECTED_EVENT,
	TEMPLATE_SELECTED_EVENT,
	TEMPLATE_SOILED,
} from '../constants';
import getFieldsFromSchema from '../getFieldsFromSchema';
import ImportMappingItem from './ImportMappingItem';
import ImportSubmit from './ImportSubmit';

function ImportForm({
	backUrl,
	formDataQuerySelector,
	formImportURL,
	formSaveAsTemplateURL,
	mappedFields,
	portletNamespace,
}) {
	const [dbFields, setDbFields] = useState();
	const [formEvaluated, setFormEvaluated] = useState(false);
	const [fileFields, setFileFields] = useState();
	const [fieldsSelections, setFieldsSelections] = useState({});
	const useTemplateMappingRef = useRef();

	const formIsValid = useMemo(() => {
		if (!Object.keys(fieldsSelections).length) {
			return false;
		}

		const requiredFieldNotFound = dbFields.some(
			(dbField) => dbField.required && !fieldsSelections[dbField.name]
		);

		return !requiredFieldNotFound;
	}, [fieldsSelections, dbFields]);

	const updateFieldMapping = (fileField, dbFieldName) => {
		setFieldsSelections((prevSelections) => ({
			...prevSelections,
			[dbFieldName]: fileField,
		}));

		Liferay.fire(TEMPLATE_SOILED);
	};

	useEffect(() => {
		if (dbFields && fileFields && !useTemplateMappingRef.current) {
			const newFieldsSelection = {};

			dbFields?.forEach((dbField) => {
				newFieldsSelection[dbField.name] = null;

				if (fileFields.includes(dbField.name)) {
					newFieldsSelection[dbField.name] = dbField.name;
				}
			});

			setFieldsSelections(newFieldsSelection);
		}
	}, [dbFields, fileFields]);

	useEffect(() => {
		function handleSchemaUpdated(event) {
			const newSchema = event.schema;

			if (newSchema) {
				const newDBFields = getFieldsFromSchema(newSchema);

				setDbFields(newDBFields);
			}
		}

		function handleFileSchemaUpdate({schema}) {
			setFileFields(schema);
		}

		function handleTemplateSelect(event) {
			const {template} = event;

			if (template) {
				useTemplateMappingRef.current = true;

				setFieldsSelections(template.mapping);
			}
			else {
				useTemplateMappingRef.current = false;
			}
		}

		const handleTemplateDirty = () => {
			useTemplateMappingRef.current = false;
		};

		Liferay.on(FILE_SCHEMA_EVENT, handleFileSchemaUpdate);
		Liferay.on(SCHEMA_SELECTED_EVENT, handleSchemaUpdated);
		Liferay.on(TEMPLATE_SELECTED_EVENT, handleTemplateSelect);
		Liferay.on(TEMPLATE_SOILED, handleTemplateDirty);

		return () => {
			Liferay.detach(FILE_SCHEMA_EVENT, handleFileSchemaUpdate);
			Liferay.detach(SCHEMA_SELECTED_EVENT, handleSchemaUpdated);
			Liferay.detach(TEMPLATE_SELECTED_EVENT, handleTemplateSelect);
			Liferay.detach(TEMPLATE_SOILED, handleTemplateDirty);
		};
	}, []);

	useEffect(() => {
		if (mappedFields) {
			setFileFields(Object.keys(mappedFields));

			setDbFields(Object.values(mappedFields));
		}
	}, [mappedFields]);

	return (
		<>
			{fileFields?.length > 0 && dbFields?.length > 0 && (
				<div className="card import-mapping-table">
					<h4 className="card-header">
						{Liferay.Language.get('import-mappings')}
					</h4>

					<div className="card-body">
						<div className="lfr-form-content">
							<div className="autofit-section">
								{dbFields?.map((dbField) => (
									<ImportMappingItem
										dbField={dbField}
										fileFields={fileFields}
										formEvaluated={formEvaluated}
										key={dbField.name}
										portletNamespace={portletNamespace}
										selectedFileField={
											fieldsSelections[dbField.name] || ''
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
							</div>
						</div>
					</div>
				</div>
			)}

			<div className="mt-4" id="formButtons">
				<div className="sheet-footer">
					<ClayLink className="btn btn-secondary" href={backUrl}>
						{Liferay.Language.get('cancel')}
					</ClayLink>

					<SaveTemplate
						evaluateForm={() => setFormEvaluated(true)}
						formIsValid={formIsValid}
						formSaveAsTemplateDataQuerySelector={
							formDataQuerySelector
						}
						formSaveAsTemplateURL={formSaveAsTemplateURL}
						portletNamespace={portletNamespace}
					/>

					<ImportSubmit
						evaluateForm={() => setFormEvaluated(true)}
						formDataQuerySelector={formDataQuerySelector}
						formImportURL={formImportURL}
						formIsValid={formIsValid}
						portletNamespace={portletNamespace}
					/>
				</div>
			</div>
		</>
	);
}

ImportForm.propTypes = {
	backUrl: PropTypes.string.isRequired,
	formDataQuerySelector: PropTypes.string.isRequired,
	formImportURL: PropTypes.string.isRequired,
	formSaveAsTemplateURL: PropTypes.string.isRequired,
	portletNamespace: PropTypes.string.isRequired,
};

export default ImportForm;
