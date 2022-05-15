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

import {createContext, useContext, useState} from 'react';
import {useFormContext} from 'react-hook-form';
import ProgressSavedModal from '../components/containers/Forms/Modal/ProgressSaved';
import useFormActions from '../hooks/useFormActions';
import {AppContext} from './AppContextProvider';

export const FormActionContext = createContext();

const FormActionProvider = ({children, form}) => {
	const [showProgressModal, setShowProgressModal] = useState(false);
	const [errorModal, setErrorModal] = useState();
	const {
		formState: {errors, isValid},
		getValues,
		setValue,
	} = useFormContext();

	const [loading, setLoading] = useState(false);

	const {
		state: {
			activeMobileSubSection,
			dimensions: {
				device: {isMobile},
			},
			selectedStep: {index: currentStepIndex = 0},
			steps,
		},
	} = useContext(AppContext);

	const emailHasError = !!errors?.basics?.businessInformation?.business
		?.email;

	const email = getValues('basics.businessInformation.business.email');
	const productQuote = getValues('basics.productQuoteName');

	const {onNext, onPrevious, onSave} = useFormActions({
		form,
		nextSection: steps[currentStepIndex + 1],
		previousSection: steps[currentStepIndex - 1],
		saveData: currentStepIndex > 1,
	});

	const onClickSaveAndExit = () => {
		setLoading(true);

		onSave()
			.then(() => setShowProgressModal(true))
			.catch((error) => console.error(error))
			.finally(() => setLoading(false));
	};

	const isContinueButtonVisible = () => {
		if (!isMobile) {
			return true;
		}

		return activeMobileSubSection?.hideContinueButton ? false : true;
	};

	return (
		<FormActionContext.Provider
			value={{
				actionProps: {
					onClickSaveAndExit,
					onNext,
					onPrevious,
					onSave,
					onSaveDisabled: !email || emailHasError || loading,
					showContinueButton: isContinueButtonVisible(),
					showSaveAndExit: onNext && currentStepIndex >= 2,
				},
				errorModal,
				isMobileDevice: isMobile,
				isValid,
				setShowProgressModal,
			}}
		>
			{children}

			<ProgressSavedModal
				email={email}
				isMobileDevice={isMobile}
				onClose={() => {
					setShowProgressModal(false);

					setValue(
						'basics.businessInformation.business.email',
						email,
						{
							shouldValidate: true,
						}
					);
				}}
				productQuote={productQuote}
				setError={(message) => setErrorModal(message)}
				show={showProgressModal}
			/>
		</FormActionContext.Provider>
	);
};

export default FormActionProvider;
