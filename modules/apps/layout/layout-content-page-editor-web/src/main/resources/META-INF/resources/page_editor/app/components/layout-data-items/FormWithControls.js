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

import classNames from 'classnames';
import React from 'react';

import {useSelectorCallback} from '../../contexts/StoreContext';
import {formIsMapped} from '../../utils/formIsMapped';
import isItemEmpty from '../../utils/isItemEmpty';
import ContainerWithControls from './ContainerWithControls';

const FormWithControls = React.forwardRef(({children, item, ...rest}, ref) => {
	const isMapped = formIsMapped(item);

	const isEmpty = useSelectorCallback(
		(state) =>
			isItemEmpty(item, state.layoutData, state.selectedViewportSize),
		[item]
	);

	return (
		<form onSubmit={(event) => event.preventDefault()} ref={ref}>
			<ContainerWithControls {...rest} item={item}>
				{isEmpty || !isMapped ? (
					<FormEmptyState isMapped={isMapped} />
				) : (
					children
				)}
			</ContainerWithControls>
		</form>
	);
});

export default FormWithControls;

function FormEmptyState({isMapped}) {
	return (
		<div
			className={classNames('page-editor__no-fragments-message', {
				'bg-lighter': !isMapped,
			})}
		>
			<div className="page-editor__no-fragments-message__title">
				{isMapped
					? Liferay.Language.get('place-fragments-here')
					: Liferay.Language.get(
							'select-a-content-type-to-start-creating-the-form'
					  )}
			</div>
		</div>
	);
}
