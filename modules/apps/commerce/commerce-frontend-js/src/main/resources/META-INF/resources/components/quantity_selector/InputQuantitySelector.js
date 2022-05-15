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

import ClayForm, {ClayInput} from '@clayui/form';
import {useIsMounted} from '@liferay/frontend-js-react-web';
import classNames from 'classnames';
import {debounce} from 'frontend-js-web';
import React, {forwardRef, useEffect, useRef, useState} from 'react';

import {
	getMinQuantity,
	getProductMaxQuantity,
} from '../../utilities/quantities';
import RulesPopover from './RulesPopover';

const getErrors = (value, min, max, step) => {
	const errors = [];

	if (!value || value < min) {
		errors.push('min');
	}

	if (max && value > max) {
		errors.push('max');
	}

	if (step > 1 && value % step) {
		errors.push('multiple');
	}

	return errors;
};

const InputQuantitySelector = forwardRef(
	(
		{
			alignment,
			className,
			disabled,
			max,
			min,
			name,
			onUpdate,
			quantity,
			step,
		},
		inputRef
	) => {
		const [showPopover, setShowPopover] = useState(false);
		const [visibleErrors, setVisibleErrors] = useState(() =>
			getErrors(quantity, min, max, step)
		);
		const isMounted = useIsMounted();
		const debouncedSetVisibleErrorsRef = useRef(
			debounce((newErrors) => {
				if (isMounted()) {
					setVisibleErrors(newErrors);
				}
			}, 500)
		);

		useEffect(() => {
			debouncedSetVisibleErrorsRef.current(() =>
				getErrors(quantity, min, max, step)
			);
		}, [quantity, min, max, step]);

		const inputMax = getProductMaxQuantity(max, step);
		const inputMin = getMinQuantity(min, step);

		return (
			<ClayForm.Group
				className={classNames({
					'has-error': visibleErrors.length,
					'mb-0': true,
				})}
			>
				<ClayInput
					className={className}
					disabled={disabled}
					max={inputMax}
					min={inputMin}
					name={name}
					onBlur={() => {
						setShowPopover(false);
					}}
					onChange={({target}) => {
						const numValue = Number(target.value);

						const errors = getErrors(numValue, min, max, step);

						onUpdate({
							errors,
							value: numValue,
						});
					}}
					onFocus={() => setShowPopover(true)}
					ref={inputRef}
					step={step > 1 ? step : ''}
					type="number"
					value={String(quantity || '')}
				/>

				{showPopover &&
					(step > 1 || min > 1 || visibleErrors.includes('max')) && (
						<RulesPopover
							alignment={alignment}
							errors={visibleErrors}
							inputRef={inputRef}
							max={max || ''}
							min={min}
							multiple={step}
						/>
					)}
			</ClayForm.Group>
		);
	}
);

InputQuantitySelector.defaultProps = {
	max: null,
	min: 1,
	step: 1,
};

export default InputQuantitySelector;
