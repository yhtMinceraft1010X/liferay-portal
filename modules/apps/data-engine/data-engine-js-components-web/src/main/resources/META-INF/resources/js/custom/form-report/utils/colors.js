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

const COLORS = [
	'#4B9FFF',
	'#FFB46E',
	'#FF5F5F',
	'#50D2A0',
	'#FF73C3',
	'#9CE269',
	'#AF78FF',
	'#FFD76E',
	'#5FC8FF',
	'#7785FF',
];

const NAMED_COLORS = {
	blueDark: '#272833',
	gray: '#cdced9',
	lightBlue: '#4b9bff',
	white: '#ffffff',
};

export {NAMED_COLORS};

export default function colors(index) {
	return COLORS[index % COLORS.length];
}
