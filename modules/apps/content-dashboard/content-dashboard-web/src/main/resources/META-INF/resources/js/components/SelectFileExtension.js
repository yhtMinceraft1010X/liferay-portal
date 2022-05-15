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

import PropTypes from 'prop-types';
import React from 'react';

import TreeFilter from './TreeFilter/TreeFilter';
import {nodeTreeArrayMapper} from './TreeFilter/treeUtils';

const SelectFileExtension = ({
	fileExtensionGroups,
	itemSelectorSaveEvent,
	portletNamespace,
}) => {
	return (
		<TreeFilter
			childrenPropertyKey="fileExtensions"
			itemSelectorSaveEvent={itemSelectorSaveEvent}
			mandatoryFieldsForFiltering={['id']}
			namePropertyKey="fileExtension"
			nodes={nodeTreeArrayMapper({
				childrenPropertyKey: 'fileExtensions',
				namePropertyKey: 'fileExtension',
				nodeArray: fileExtensionGroups,
			})}
			portletNamespace={portletNamespace}
		/>
	);
};

SelectFileExtension.propTypes = {
	fileExtensionGroups: PropTypes.array.isRequired,
	itemSelectorSaveEvent: PropTypes.string.isRequired,
	portletNamespace: PropTypes.string.isRequired,
};

export default SelectFileExtension;
