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

import ClayDropDown from '@clayui/drop-down';
import React, {useState} from 'react';

import CustomSelect from '../CustomSelect/CustomSelect';
import FieldBase from './FieldBase';

import './AutoComplete.scss';

interface IAutoCompleteProps extends React.HTMLAttributes<HTMLElement> {
	children: (item: any) => React.ReactNode;
	contentRight?: React.ReactNode;
	emptyStateMessage: string;
	error?: string;
	feedbackMessage?: string;
	items: any[];
	label: string;
	onChangeQuery: (value: string) => void;
	onSelectItem: (item: any) => void;
	query: string;
	required?: boolean;
	value?: string;
}

const AutoComplete: React.FC<IAutoCompleteProps> = ({
	children,
	className,
	contentRight,
	emptyStateMessage,
	error,
	feedbackMessage,
	id,
	items,
	label,
	onChangeQuery,
	onSelectItem,
	query,
	required = false,
	value,
}) => {
	const [active, setActive] = useState<boolean>(false);

	return (
		<FieldBase
			className={className}
			errorMessage={error}
			helpMessage={feedbackMessage}
			id={id}
			label={label}
			required={required}
		>
			<ClayDropDown
				active={active}
				onActiveChange={setActive}
				trigger={
					<CustomSelect
						contentRight={<>{value && contentRight}</>}
						placeholder={Liferay.Language.get('choose-an-option')}
						value={value}
					/>
				}
			>
				<ClayDropDown.Search
					onChange={({target: {value}}) => onChangeQuery(value)}
					placeholder={Liferay.Language.get('search')}
					value={query}
				/>

				{items.length ? (
					<ClayDropDown.ItemList>
						{items.map((item, index) => {
							return (
								<ClayDropDown.Item
									key={index}
									onClick={() => {
										setActive(false);
										onSelectItem(item);
									}}
								>
									{children && children(item)}
								</ClayDropDown.Item>
							);
						})}
					</ClayDropDown.ItemList>
				) : (
					<ClayDropDown.ItemList>
						<ClayDropDown.Item>
							{emptyStateMessage}
						</ClayDropDown.Item>
					</ClayDropDown.ItemList>
				)}
			</ClayDropDown>
		</FieldBase>
	);
};

export default AutoComplete;
