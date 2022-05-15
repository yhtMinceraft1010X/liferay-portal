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
import React, {useState} from 'react';

import Modal from '../../../../components/Modal';
import {FormModalOptions} from '../../../../hooks/useFormModal';
import i18n from '../../../../i18n';
import SelectCase from './SelectCase';
import SelectCaseParameters from './SelectCaseParameters';

type SuiteSelectCasesModalProps = {
	modal: FormModalOptions;
	type: 'select-cases' | 'select-case-parameters';
};

const SuiteFormSelectModal: React.FC<SuiteSelectCasesModalProps> = ({
	modal: {observer, onClose, onSave, visible},
	type,
}) => {
	const [state, setState] = useState<any>({});

	return (
		<Modal
			last={
				<ClayButton.Group spaced>
					<ClayButton displayType="secondary" onClick={onClose}>
						{i18n.translate('close')}
					</ClayButton>

					<ClayButton
						displayType="primary"
						onClick={() => onSave(state)}
					>
						{i18n.translate('save')}
					</ClayButton>
				</ClayButton.Group>
			}
			observer={observer}
			size="full-screen"
			title={i18n.translate(type)}
			visible={visible}
		>
			{type === 'select-case-parameters' && (
				<SelectCaseParameters setState={setState} state={state} />
			)}

			{type === 'select-cases' && <SelectCase setState={setState} />}
		</Modal>
	);
};

export default SuiteFormSelectModal;
