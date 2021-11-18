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

import ClayButton from '@clayui/button';
import ClayForm from '@clayui/form';
import ClayModal from '@clayui/modal';
import ClayProgressBar from '@clayui/progress-bar';
import {useIsMounted} from '@liferay/frontend-js-react-web';
import PropTypes from 'prop-types';
import React, {useState} from 'react';

const ImportModal = ({
	closeModal,

	// formDataQuerySelector,
	// formSubmitURL,

	namespace,
	observer,
}) => {
	const [percentage] = useState();
	const isMounted = useIsMounted();
	const [errorMessage, setErrorMessage] = useState();
	const [loadingResponse, setLoadingResponse] = useState(false);

	const _handleSubmit = async (event) => {
		event.preventDefault();

		try {
			const saveTemplateResponse = {ciao: 'test'};

			if (isMounted()) {
				if (saveTemplateResponse.error) {
					setLoadingResponse(false);
					setErrorMessage(saveTemplateResponse.error);
				}
				else {
					closeModal();
				}
			}
		}
		catch (error) {
			setErrorMessage(Liferay.Language.get('unexpected-error'));
		}
		finally {
			setLoadingResponse(false);
		}
	};

	return (
		<ClayModal observer={observer} size="md">
			<ClayModal.Header>
				{Liferay.Language.get('save-as-template')}
			</ClayModal.Header>

			<form id={`${namespace}form`} onSubmit={_handleSubmit}>
				<ClayModal.Body>
					<ClayForm.Group className={errorMessage ? 'has-error' : ''}>
						<div
							className="progress-container"
							data-percentage={percentage}
							data-title={
								percentage === 100
									? Liferay.Language.get('completed')
									: Liferay.Language.get('in-progress')
							}
						>
							<ClayProgressBar
								value={percentage}
								warn={!!errorMessage}
							/>
						</div>

						{errorMessage && (
							<ClayForm.FeedbackGroup>
								<ClayForm.FeedbackItem>
									<ClayForm.FeedbackIndicator symbol="exclamation-full" />

									{errorMessage}
								</ClayForm.FeedbackItem>
							</ClayForm.FeedbackGroup>
						)}
					</ClayForm.Group>
				</ClayModal.Body>

				<ClayModal.Footer
					last={
						<ClayButton.Group spaced>
							<ClayButton
								displayType="secondary"
								onClick={closeModal}
							>
								{Liferay.Language.get('cancel')}
							</ClayButton>

							<ClayButton
								disabled={loadingResponse}
								displayType="primary"
								type="submit"
							>
								{loadingResponse && (
									<span className="inline-item inline-item-before">
										<span
											aria-hidden="true"
											className="loading-animation"
										></span>
									</span>
								)}

								{Liferay.Language.get('save')}
							</ClayButton>
						</ClayButton.Group>
					}
				/>
			</form>
		</ClayModal>
	);
};

ImportModal.propTypes = {
	closeModal: PropTypes.func.isRequired,

	// formDataQuerySelector: PropTypes.string.isRequired,
	// formSubmitURL: PropTypes.string.isRequired,

	namespace: PropTypes.string.isRequired,
	observer: PropTypes.object,
};

export default ImportModal;
