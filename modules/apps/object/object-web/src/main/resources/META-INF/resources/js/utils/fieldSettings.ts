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

export function normalizeFieldSettings(
	objectFieldSettings: ObjectFieldSetting[] | undefined
) {
	const settings: {
		[key in ObjectFieldSettingName]?: string | number | boolean;
	} = {};

	objectFieldSettings?.forEach(({name, value}) => {
		settings[name] = value;
	});

	return settings;
}

export function updateFieldSettings(
	objectFieldSettings: ObjectFieldSetting[] = [],
	{name, value}: ObjectFieldSetting
) {
	let isNewSetting = true;

	const settings = objectFieldSettings.map((setting) => {
		if (setting.name === name) {
			isNewSetting = false;

			return {...setting, value};
		}

		return setting;
	});

	return isNewSetting ? [...settings, {name, value}] : settings;
}
