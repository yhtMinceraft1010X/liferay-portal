/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import {ClayToggle} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import PropTypes from 'prop-types';
import React, {useState} from 'react';

import SidebarPanel from '../../SidebarPanel';
import TimerFields from './TimerFields';

const TimerDuration = ({
	duration: durationValue,
	durationScale: durationScaleValue,
	recurrence: recurrenceValue,
	recurrenceScale: recurrenceScaleValue,
	selectedItem,
	setTimerSections,
	timerIdentifier,
	timersIndex,
}) => {
	const [recurrence, setRecurrence] = useState(
		!!recurrenceValue && !!recurrenceScaleValue
	);

	const handleToggle = () => {
		if (recurrence) {
			setRecurrence(false);
			setTimerSections((previousSections) => {
				const updatedSections = [...previousSections];

				delete updatedSections[timersIndex].recurrence;
				delete updatedSections[timersIndex].recurrenceScale;

				return updatedSections;
			});
		}
		else {
			setRecurrence(true);
		}
	};

	return (
		<SidebarPanel panelTitle={Liferay.Language.get('duration')}>
			<TimerFields
				durationScaleValue={durationScaleValue}
				durationValue={durationValue}
				selectedItem={selectedItem}
				setTimerSections={setTimerSections}
				timerIdentifier={timerIdentifier}
				timersIndex={timersIndex}
			/>

			<div className="timers-duration-toggle">
				<ClayToggle
					label={Liferay.Language.get('recurrence')}
					onToggle={handleToggle}
					toggled={recurrence}
				/>

				<span
					className="ml-2"
					title={Liferay.Language.get(
						'repeat-the-action-at-a-given-duration-until-the-workflow-task-is-completed'
					)}
				>
					<ClayIcon
						className="text-muted"
						symbol="question-circle-full"
					/>
				</span>
			</div>

			{recurrence && (
				<TimerFields
					durationScaleValue={recurrenceScaleValue}
					durationValue={recurrenceValue}
					recurrence
					scaleHelpText={Liferay.Language.get('recurrence')}
					selectedItem={selectedItem}
					setTimerSections={setTimerSections}
					timerIdentifier={timerIdentifier}
					timersIndex={timersIndex}
				/>
			)}
		</SidebarPanel>
	);
};

TimerDuration.propTypes = {
	selectedItem: PropTypes.object.isRequired,
	setTimerSections: PropTypes.func.isRequired,
	timerIdentifier: PropTypes.string.isRequired,
	timersIndex: PropTypes.func.isRequired,
};

export default TimerDuration;
