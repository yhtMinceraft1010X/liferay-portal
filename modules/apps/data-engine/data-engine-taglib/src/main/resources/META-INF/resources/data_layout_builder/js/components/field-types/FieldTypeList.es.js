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

import ClayEmptyState from '@clayui/empty-state';
import classNames from 'classnames';
import {
	EVENT_TYPES as CORE_EVENT_TYPES,
	useConfig,
	useForm,
	useFormState,
} from 'data-engine-js-components-web';
import React from 'react';

import {sub} from '../../utils/lang.es';
import {getSearchRegex} from '../../utils/search.es';
import CollapsablePanel from '../collapsable-panel/CollapsablePanel.es';
import FieldType from './FieldType.es';

const FieldTypeWrapper = ({
	expanded,
	fieldType,
	fieldTypes,
	showArrows,
	...otherProps
}) => {
	const dispatch = useForm();
	const {activePage, pages} = useFormState();

	const getIcon = () => {
		if (showArrows) {
			return expanded ? 'angle-down' : 'angle-right';
		}

		return fieldType.icon;
	};

	return (
		<FieldType
			{...otherProps}
			{...fieldType}
			icon={getIcon()}
			onDoubleClick={({name}) => {
				dispatch({
					payload: {
						fieldType: {
							...fieldTypes.find(
								({name: typeName}) => typeName === name
							),
							editable: true,
						},
						indexes: {
							columnIndex: 0,
							pageIndex: activePage,
							rowIndex: pages[activePage].rows.length,
						},
					},
					type: CORE_EVENT_TYPES.FIELD.ADD,
				});
			}}
		/>
	);
};

const FieldTypeList = ({
	dataDefinition,
	deleteLabel,
	keywords,
	onClick,
	onDelete,
	showEmptyState = true,
}) => {
	const {fieldTypes} = useConfig();
	const regex = getSearchRegex(keywords);

	const filteredFieldTypes = fieldTypes
		.filter(({description, label, system}) => {
			if (system) {
				return false;
			}
			if (!keywords) {
				return true;
			}

			return regex.test(description) || regex.test(label);
		})
		.sort(({displayOrder: a}, {displayOrder: b}) => a - b);

	if (showEmptyState && !filteredFieldTypes.length) {
		return (
			<ClayEmptyState
				description={sub(
					Liferay.Language.get('there-are-no-results-for-x'),
					[keywords]
				)}
				imgSrc={`${themeDisplay.getPathThemeImages()}/states/search_state.gif`}
				small
				title={Liferay.Language.get('no-results-found')}
			/>
		);
	}

	return filteredFieldTypes.map((fieldType, index) => {
		const {isFieldSet, nestedDataDefinitionFields = []} = fieldType;

		const handleOnClick = (props) => {
			if (fieldType.disabled || !onClick) {
				return;
			}

			onClick(props);
		};

		if (nestedDataDefinitionFields.length) {
			const Header = ({expanded, setExpanded}) => (
				<FieldTypeWrapper
					dataDefinition={dataDefinition}
					deleteLabel={deleteLabel}
					expanded={expanded}
					fieldType={{
						...fieldType,
						className: `${fieldType.className} field-type-header`,
					}}
					fieldTypes={filteredFieldTypes}
					onClick={(props) => {
						setExpanded(!expanded);

						handleOnClick(props);
					}}
					onDelete={onDelete}
					setExpanded={setExpanded}
					showArrows
				/>
			);

			return (
				<div className="field-type-list" key={index}>
					<CollapsablePanel
						Header={Header}
						className={classNames({
							'field-type-fieldgroup': !isFieldSet,
							'field-type-fieldset': isFieldSet,
						})}
					>
						<div className="field-type-item position-relative">
							{nestedDataDefinitionFields.map(
								(nestedFieldType) => (
									<FieldTypeWrapper
										dataDefinition={dataDefinition}
										draggable={false}
										fieldType={{
											...nestedFieldType,
											disabled: fieldType.disabled,
										}}
										fieldTypes={filteredFieldTypes}
										key={`${nestedFieldType.name}_${index}`}
									/>
								)
							)}
						</div>
					</CollapsablePanel>
				</div>
			);
		}

		return (
			<FieldTypeWrapper
				dataDefinition={dataDefinition}
				deleteLabel={deleteLabel}
				fieldType={fieldType}
				fieldTypes={filteredFieldTypes}
				key={index}
				onClick={handleOnClick}
				onDelete={onDelete}
			/>
		);
	});
};

FieldTypeList.displayName = 'FieldTypeList';
export default FieldTypeList;
