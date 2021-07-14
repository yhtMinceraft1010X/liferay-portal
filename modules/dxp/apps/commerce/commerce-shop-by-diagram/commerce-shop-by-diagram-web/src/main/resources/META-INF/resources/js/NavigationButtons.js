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

import ClayIcon from '@clayui/icon';
import PropTypes from 'prop-types';
import React from 'react';

const NavigationButtons = ({moveController, position}) => {
	return (
		<div
			className="move-controller"
			style={{
				bottom: position.bottom,
				left: position.left,
				right: position.right,
				top: position.top,
			}}
		>
			<button className="box top" onClick={() => moveController('up')}>
				<ClayIcon className="icon" symbol="angle-left" />
			</button>
			<button
				className="box right"
				onClick={() => moveController('right')}
			>
				<ClayIcon className="icon" symbol="angle-up" />
			</button>
			<button className="box left" onClick={() => moveController('left')}>
				<ClayIcon className="icon" symbol="angle-down" />
			</button>
			<button
				className="bottom box"
				onClick={() => moveController('down')}
			>
				<ClayIcon className="icon" symbol="angle-down" />
			</button>
		</div>
	);
};

export default NavigationButtons;

NavigationButtons.propTypes = {
	moveController: PropTypes.func,
};
