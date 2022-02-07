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

import React, {useEffect} from 'react';
import {useFormContext} from 'react-hook-form';
import {NumberControlledInput} from '../../../../../common/components/connectors/Controlled/Input/Number';
import {SquareFeatControlledInput} from '../../../../../common/components/connectors/Controlled/Input/WithMask/SquareFeet';
import {YearControlledInput} from '../../../../../common/components/connectors/Controlled/Input/WithMask/Year';
import {ControlledSwitch} from '../../../../../common/components/connectors/Controlled/Switch';
import {TIP_EVENT} from '../../../../../common/utils/events';
import useMobileContainer from '../../../hooks/useMobileContainer';
import {useTriggerContext} from '../../../hooks/useTriggerContext';
import {SUBSECTION_KEYS} from '../../../utils/constants';
import {isHabitational, isThereSwimming} from '../../../utils/propertyFields';
import MobileContainer from '../../mobile/MobileContainer';

const setFormPath = (value) => `property.${value}`;

export function FormProperty({form}) {
	const {control, getValues, setValue} = useFormContext();

	const {
		getMobileSubSection,
		mobileContainerProps,
		nextStep,
	} = useMobileContainer();

	const forceValidation = () => {
		setValue(
			setFormPath('doOwnBuildingAtAddress'),
			getValues(setFormPath('doOwnBuildingAtAddress')),
			{shouldValidate: true}
		);
	};
	useEffect(() => {
		forceValidation();
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	const {isSelected, updateState} = useTriggerContext();

	return (
		<div className="card-content">
			<MobileContainer
				 {...mobileContainerProps}
				 hasAddress={`${form.basics.businessInformation.business.location.address}?`}
				 mobileSubSection={getMobileSubSection(
					 SUBSECTION_KEYS.DO_YOU_OWN_THE_BUILDING_AT
				 )}
			>	 
				<ControlledSwitch
					control={control}
					label={`Do you own the building at ${form.basics.businessInformation.business.location.address}?`}
					name={setFormPath('doOwnBuildingAtAddress')}
					onSelect={nextStep}
					rules={{required: true}}
				/>
			</MobileContainer>

			<MobileContainer
				 {...mobileContainerProps}
				 mobileSubSection={getMobileSubSection(
					 SUBSECTION_KEYS.HOW_MANY_STORIES_IS_THIS_BUILDING
				 )}
			>	
				<NumberControlledInput
					control={control}
					label={SUBSECTION_KEYS.HOW_MANY_STORIES_IS_THIS_BUILDING}
					name={setFormPath('stories')}
					rules={{
						min: {
							message: 'Must be equal or greater than 0.',
							value: 0,
						},
						required: 'This field is required',
					}}
				/>
			</MobileContainer>
			
			<MobileContainer
				 {...mobileContainerProps}
				 mobileSubSection={getMobileSubSection(
					 SUBSECTION_KEYS.HOW_MANY_SQUARE_FEET_OF_THE_BUILDING
				 )}
			>	
				<SquareFeatControlledInput
					control={control}
					label={SUBSECTION_KEYS.HOW_MANY_SQUARE_FEET_OF_THE_BUILDING}
					moreInfoProps={{
						callback: () =>
							updateState(setFormPath('buildingSquareFeetOccupied')),
						event: TIP_EVENT,
						selected: isSelected(
							setFormPath('buildingSquareFeetOccupied')
						),
						value: {
							inputName: setFormPath('buildingSquareFeetOccupied'),
							templateName: 'building-square-footage',
							value: form?.property?.buildingSquareFeetOccupied,
						},
					}}
					name={setFormPath('buildingSquareFeetOccupied')}
					rules={{
						required: 'This field is required',
					}}
				/>
			</MobileContainer>

			<MobileContainer
				 {...mobileContainerProps}
				 mobileSubSection={getMobileSubSection(
					 SUBSECTION_KEYS.HOW_MANY_TOTAL_SQUARE_FEET_IS_THE_BUILDING
				 )}
			>
				<SquareFeatControlledInput
					control={control}
					label={SUBSECTION_KEYS.HOW_MANY_TOTAL_SQUARE_FEET_IS_THE_BUILDING}
					name={setFormPath('totalBuildingSquareFeet')}
					rules={{
						required: 'This field is required',
					}}
				/>
			</MobileContainer>
			
			<MobileContainer
				 {...mobileContainerProps}
				 mobileSubSection={getMobileSubSection(
					 SUBSECTION_KEYS.WHAT_YEAR_WAS_THE_BUILDING_CONSTRUCTED
				 )}
			>
				<YearControlledInput
					control={control}
					label={SUBSECTION_KEYS.WHAT_YEAR_WAS_THE_BUILDING_CONSTRUCTED}
					moreInfoProps={{
						callback: () => updateState(setFormPath('yearBuilding')),
						event: TIP_EVENT,
						selected: isSelected(setFormPath('yearBuilding')),
						value: {
							inputName: setFormPath('yearBuilding'),
							templateName: 'year-constructed',
							value: form?.property?.yearBuilding,
						},
					}}
					name={setFormPath('yearBuilding')}
					rules={{
						required: 'This field is required',
					}}
				/>
			</MobileContainer>
			
			<MobileContainer
				 {...mobileContainerProps}
				 mobileSubSection={getMobileSubSection(
					 SUBSECTION_KEYS.IS_THIS_THE_PRIMARY_LOCATION
				 )}
			>
				<ControlledSwitch
					control={control}
					label={SUBSECTION_KEYS.IS_THIS_THE_PRIMARY_LOCATION}
					moreInfoProps={{
						callback: () =>
							updateState(setFormPath('isPrimaryBusinessLocation')),
						event: TIP_EVENT,
						selected: isSelected(
							setFormPath('isPrimaryBusinessLocation')
						),
						value: {
							inputName: setFormPath('isPrimaryBusinessLocation'),
							templateName: 'primary-location',
							value: form?.property?.isPrimaryBusinessLocation,
						},
					}}
					name={setFormPath('isPrimaryBusinessLocation')}
					rules={{required: true}}
				/>
			</MobileContainer>

			{isHabitational(
				form?.basics?.properties?.segment.toLowerCase()
			) && (
				<ControlledSwitch
					control={control}
					inputProps={{
						onChange: (value) => {
							setValue(setFormPath('isThereSwimming'), value, {
								shouldValidate: true,
							});

							if (value === 'false') {
								setValue(
									setFormPath('isThereDivingBoards'),
									''
								);
							}
						},
					}}
					label="Are there swimming pool(s) on the premises?"
					name={setFormPath('isThereSwimming')}
					rules={{required: true}}
				/>
			)}

			{isThereSwimming(form?.property?.isThereSwimming) && (
				<ControlledSwitch
					control={control}
					label="Are there diving boards or slides?"
					name={setFormPath('isThereDivingBoards')}
					rules={{required: true}}
				/>
			)}
		</div>
	);
}
