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

import {useMutation} from '@apollo/client';

import {DeleteRoutine} from '../../../graphql/mutations';
import {TestrayRoutine} from '../../../graphql/queries';
import useFormModal from '../../../hooks/useFormModal';
import i18n from '../../../i18n';

const useRoutineActions = () => {
	const [onDeleteRoutine] = useMutation(DeleteRoutine);

	const formModal = useFormModal();
	const modal = formModal.modal;

	return {
		actions: [
			{
				action: modal.open,
				name: i18n.translate('edit'),
			},
			{
				action: () => alert('Select Default Environment Factors'),
				name: i18n.translate('select-default-environment-factors'),
			},
			{
				action: ({id: routineId}: TestrayRoutine) =>
					onDeleteRoutine({variables: {routineId}})
						.then(() => modal.onSave())
						.catch(modal.onError),
				name: i18n.translate('delete'),
			},
		],
		actionsRoutine: [
			{
				action: () => alert('Archive'),
				name: i18n.translate('archive'),
			},
			{
				action: modal.open,
				name: i18n.translate('edit'),
			},
			{
				action: () => alert('Promote'),
				name: i18n.translate('promote'),
			},
			{
				action: ({id: routineId}: any) =>
					onDeleteRoutine({variables: {routineId}})
						.then(() => modal.onSave())
						.catch(modal.onError),
				name: i18n.translate('delete'),
			},
		],
		formModal,
	};
};

export default useRoutineActions;
