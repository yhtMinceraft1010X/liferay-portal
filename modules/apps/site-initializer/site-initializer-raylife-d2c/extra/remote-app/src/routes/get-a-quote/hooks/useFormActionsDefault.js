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
import {useCallback, useContext, useEffect, useState} from 'react';
import {useFormContext} from 'react-hook-form';
import {STORAGE_KEYS, Storage} from '../../../common/services/liferay/storage';
import {RAYLIFE_PAGES} from '../../../common/utils/constants';
import {clearExitAlert} from '../../../common/utils/exitAlert';
import {redirectTo} from '../../../common/utils/liferay';
import {smoothScroll} from '../../../common/utils/scroll';
import {AppContext} from '../context/AppContextProvider';
import {createOrUpdateRaylifeApplication} from '../services/RaylifeApplication';
import {APPLICATION_STATUS, AVAILABLE_STEPS} from '../utils/constants';
import {verifyInputAgentPage} from '../utils/contact-agent';
import {useStepWizard} from './useStepWizard';

/**
 *
 * @param {String} form <useWatch>
 * @param {String?} previousSection
 * @param {String?} nextSection
 * @param {String?} errorMessage
 * @returns
 */

const useFormActions = ({
	errorMessage = 'Unable to save your information. Please try again.',
	form,
	nextSection,
	previousSection,
	saveData = false,
}) => {
	const [applicationId, setApplicationId] = useState();
	const {setError, setValue} = useFormContext();
	const {setSection} = useStepWizard();
	const {
		state: {selectedStep},
	} = useContext(AppContext);

	/**
	 * @description When the application is created, we set the value to Form Context
	 * We tried to use setValue directly on goToPrevious and goToNextForm
	 * and for reasons unknowns, the section is not called.
	 */

	useEffect(() => {
		if (applicationId) {
			setValue('basics.applicationId', applicationId);

			Storage.setItem(STORAGE_KEYS.APPLICATION_ID, applicationId);
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [applicationId]);

	useEffect(() => {
		Storage.setItem(STORAGE_KEYS.APPLICATION_FORM, JSON.stringify(form));
	}, [form]);

	const _onValidation = useCallback(() => {
		const phraseAgentPage = verifyInputAgentPage(form, nextSection);
		let validated = true;

		if (phraseAgentPage) {
			Storage.setItem(STORAGE_KEYS.CONTEXTUAL_MESSAGE, phraseAgentPage);
			redirectTo(RAYLIFE_PAGES.GET_IN_TOUCH);
			validated = false;
		}
		else {
			Storage.removeItem(STORAGE_KEYS.CONTEXTUAL_MESSAGE);
		}

		return validated;
	}, [form, nextSection]);

	const onSave = useCallback(
		async (status = {}) => {
			if (!saveData) {
				return;
			}

			setError('continueButton', {});

			try {
				const response = await createOrUpdateRaylifeApplication(
					form,
					status
				);

				setApplicationId(response.data.id);

				return response;
			}
			catch (error) {
				setError('continueButton', {
					message:
						errorMessage ||
						'There was an error processing your request. Please try again.',
					type: 'manual',
				});

				throw error;
			}
		},
		[errorMessage, form, saveData, setError]
	);

	const onPrevious = useCallback(async () => {
		await onSave();

		if (previousSection) {
			setSection(previousSection);
		}

		smoothScroll();
	}, [onSave, previousSection, setSection]);

	/**
	 * @state disabled for now
	 * @param {*} data
	 */
	const onNext = useCallback(async () => {
		let status = APPLICATION_STATUS.OPEN;

		if (AVAILABLE_STEPS.PROPERTY.index === selectedStep.index) {
			status = APPLICATION_STATUS.QUOTED;
		}
		await onSave(status);

		clearExitAlert();

		const validated = _onValidation();

		if (validated) {
			if (nextSection) {
				setSection(nextSection);

				return smoothScroll();
			}

			redirectTo(RAYLIFE_PAGES.HANG_TIGHT);
		}
	}, [_onValidation, nextSection, selectedStep, onSave, setSection]);

	return {onNext, onPrevious, onSave};
};

export default useFormActions;
