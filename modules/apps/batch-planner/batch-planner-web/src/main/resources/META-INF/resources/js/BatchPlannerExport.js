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

import {fetch} from 'frontend-js-web';

import {EXPORT_PROCESS_COMPLETED, EXPORT_PROCESS_FAILED} from './constants';

const HEADERS = new Headers({
	'Accept': 'application/json',
	'Accept-Language': Liferay.ThemeDisplay.getBCP47LanguageId(),
});

const HEADLESS_BATCH_ENGINE_URL = '/o/headless-batch-engine/v1.0/export-task/';

export function getExportTaskStatusURL(taskId) {
	return `${HEADLESS_BATCH_ENGINE_URL}${taskId}`;
}

export function getExportFileURL(taskId) {
	return `${HEADLESS_BATCH_ENGINE_URL}${taskId}/content`;
}

export async function saveTemplateAPI(formDataQuerySelector, updateData, url) {
	const mainFormData = document.querySelector(formDataQuerySelector);
	Liferay.Util.setFormValues(mainFormData, updateData);

	const formData = new FormData(mainFormData);
	const response = await fetch(url, {
		body: formData,
		headers: HEADERS,
		method: 'POST',
	});

	return await response.json();
}

export async function startExport(formDataQuerySelector, url) {
	const mainFormData = document.querySelector(formDataQuerySelector);

	const formData = new FormData(mainFormData);

	const response = await fetch(url, {
		body: formData,
		headers: HEADERS,
		method: 'POST',
	});

	return await response.json();
}

export async function exportStatus(exportTaskId) {
	const response = await fetch(getExportTaskStatusURL(exportTaskId), {
		headers: HEADERS,
	});

	return await response.json();
}

export async function getExportStatus({onFail, onProgress, onSuccess, taskId}) {
	try {
		const {
			contentType,
			errorMessage,
			executeStatus,
			processedItemsCount,
			totalItemsCount,
		} = await exportStatus(taskId);

		switch (executeStatus) {
			case EXPORT_PROCESS_FAILED:
				onFail(
					errorMessage || Liferay.Language.get('unexpected-error')
				);
				break;
			case EXPORT_PROCESS_COMPLETED:
				onSuccess(contentType);
				break;
			default:
				onProgress(
					contentType,
					Math.round((processedItemsCount / totalItemsCount) * 100)
				);
		}
	}
	catch (error) {
		onFail(Liferay.Language.get('unexpected-error'));
	}
}

export async function fetchExportedFile(taskId) {
	const response = await fetch(getExportFileURL(taskId));
	const blob = await response.blob();

	return URL.createObjectURL(blob);
}
