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

import {DeleteRequirement} from '../../../graphql/mutations';
import {TestrayRequirement} from '../../../graphql/queries';
import useFormModal from '../../../hooks/useFormModal';
import i18n from '../../../i18n';

const useRequirementActions = () => {
	const [onDeleteRequirement] = useMutation(DeleteRequirement);

	const formModal = useFormModal();
	const modal = formModal.modal;

	return {
		actions: [
			{
				action: (requirement: TestrayRequirement) =>
					modal.open({
						...requirement,
						componentId: requirement.component?.id,
					}),
				name: i18n.translate('edit'),
			},
			{
				action: ({id}: TestrayRequirement) =>
					onDeleteRequirement({
						variables: {id},
					})
						.then(() => modal.onSave())
						.catch(modal.onError),
				name: i18n.translate('delete'),
			},
		],
		formModal,
	};
};

export default useRequirementActions;
