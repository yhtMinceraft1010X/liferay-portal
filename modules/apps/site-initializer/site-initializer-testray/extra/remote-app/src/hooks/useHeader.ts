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

import {useCallback, useContext, useEffect} from 'react';

import {
	Dropdown,
	HeaderContext,
	HeaderTabs,
	HeaderTitle,
	HeaderTypes,
	initialState,
} from '../context/HeaderContext';

type UseHeader = {
	shouldUpdate?: boolean;
	timeout?: number;
	useAction?: Dropdown;
	useDropdown?: Dropdown;
	useHeading?: HeaderTitle[];
	useIcon?: string;
	useTabs?: HeaderTabs[];
};

const DEFAULT_TIMEOUT = 0;

const useHeader = ({
	shouldUpdate = true,
	timeout = DEFAULT_TIMEOUT,
	useHeading = initialState.heading,
	useAction,
	useIcon = initialState.symbol,
	useDropdown,
	useTabs = initialState.tabs,
}: UseHeader = {}) => {
	const [, dispatch] = useContext(HeaderContext);

	const useActionString = JSON.stringify(useAction);
	const useDropdownString = JSON.stringify(useDropdown);
	const useHeadingString = JSON.stringify(useHeading);
	const useTabsString = JSON.stringify(useTabs);
	const useDropdownIcon = JSON.stringify(useIcon);

	const setActions = useCallback(
		(newActions: Dropdown) => {
			dispatch({payload: newActions, type: HeaderTypes.SET_ACTIONS});
		},
		[dispatch]
	);

	const setDropdown = useCallback(
		(newDropdown: Dropdown) => {
			dispatch({payload: newDropdown, type: HeaderTypes.SET_DROPDOWN});
		},
		[dispatch]
	);

	const setDropdownIcon = useCallback(
		(newSymbol: string) => {
			dispatch({
				payload: newSymbol,
				type: HeaderTypes.SET_SYMBOL,
			});
		},
		[dispatch]
	);

	const setHeading = useCallback(
		(newHeading: HeaderTitle[] = [], append?: boolean) => {
			dispatch({
				payload: {append, heading: newHeading},
				type: HeaderTypes.SET_HEADING,
			});
		},
		[dispatch]
	);

	const setTabs = useCallback(
		(newTabs: HeaderTabs[] = []) =>
			dispatch({payload: newTabs, type: HeaderTypes.SET_TABS}),
		[dispatch]
	);

	useEffect(() => {
		if (shouldUpdate && useHeadingString) {
			setTimeout(() => {
				setHeading(JSON.parse(useHeadingString));
			}, timeout);
		}
	}, [setHeading, shouldUpdate, timeout, useHeadingString]);

	useEffect(() => {
		if (shouldUpdate && useIcon) {
			setTimeout(() => {
				setDropdownIcon(JSON.parse(useDropdownIcon));
			}, timeout);
		}
	}, [setDropdownIcon, shouldUpdate, timeout, useDropdownIcon, useIcon]);

	useEffect(() => {
		if (shouldUpdate && useTabsString) {
			setTimeout(() => {
				setTabs(JSON.parse(useTabsString));
			}, timeout);
		}
	}, [setTabs, shouldUpdate, timeout, useTabsString]);

	useEffect(() => {
		if (useActionString) {
			setActions(JSON.parse(useActionString));
		}
	}, [setActions, useActionString]);

	useEffect(() => {
		if (useDropdownString) {
			setDropdown(JSON.parse(useDropdownString));
		}
	}, [setDropdown, useDropdownString]);

	return {
		dispatch,
		setActions,
		setDropdown,
		setDropdownIcon,
		setHeading,
		setTabs,
	};
};

export {useHeader};

export default useHeader;
