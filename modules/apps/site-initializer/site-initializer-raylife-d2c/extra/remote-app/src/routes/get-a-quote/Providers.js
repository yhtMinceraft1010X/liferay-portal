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

import React, {useEffect, useState} from 'react';
import {FormProvider, useForm} from 'react-hook-form';
import {Template} from '../../common/components/Template';
import ClayIconProvider from '../../common/context/ClayIconProvider';
import {LiferayAdapt} from '../../common/services/liferay/adapter';
import {STORAGE_KEYS, Storage} from '../../common/services/liferay/storage';
import {AppContextProvider} from './context/AppContextProvider';
import WebContentProvider from './context/WebContentProvider';
import {getRaylifeApplicationById} from './services/RaylifeApplication';
import {getLoadedContentFlag} from './utils/util';

const contentFlag = getLoadedContentFlag();

const getDefaultValues = () => {
	const applicationForm = Storage.getItem(STORAGE_KEYS.APPLICATION_FORM);

	if (contentFlag.backToEdit && applicationForm) {
		return JSON.parse(applicationForm);
	}

	return {};
};

const Providers = ({children, initialValues}) => {
	const defaultValues = contentFlag.applicationId
		? initialValues
		: getDefaultValues();

	const form = useForm({
		defaultValues,
		mode: 'onChange',
	});

	return (
		<ClayIconProvider>
			<AppContextProvider>
				<WebContentProvider>
					<FormProvider {...form}>
						<Template>{children}</Template>
					</FormProvider>
				</WebContentProvider>
			</AppContextProvider>
		</ClayIconProvider>
	);
};

const InitProvider = ({children}) => {
	const [initialValues, setInitialValues] = useState(null);
	const [loading, setLoading] = useState(false);
	const applicationId = contentFlag.applicationId;

	const getInitialData = async () => {
		setLoading(true);

		let initialData = {};

		try {
			if (applicationId) {
				const {data} = await getRaylifeApplicationById(applicationId);

				initialData = LiferayAdapt.adaptToRaylifeApplicationToForm(
					data
				);
			}
		}
		catch (error) {
			console.error(error.message);
		}

		setInitialValues(initialData);
		setLoading(false);
	};

	useEffect(() => {
		getInitialData();
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	if (loading) {
		return null;
	}

	return <Providers initialValues={initialValues}>{children}</Providers>;
};

export default InitProvider;
