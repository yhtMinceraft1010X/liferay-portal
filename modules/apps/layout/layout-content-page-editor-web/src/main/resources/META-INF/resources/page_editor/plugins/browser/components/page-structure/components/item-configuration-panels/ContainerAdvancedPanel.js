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

import React from 'react';

import {HideFromSearchField} from '../../../../../../app/components/fragment-configuration-fields/HideFromSearchField';
import {SelectField} from '../../../../../../app/components/fragment-configuration-fields/SelectField';
import {
	useDispatch,
	useSelector,
} from '../../../../../../app/contexts/StoreContext';
import selectSegmentsExperienceId from '../../../../../../app/selectors/selectSegmentsExperienceId';
import updateItemConfig from '../../../../../../app/thunks/updateItemConfig';
import {getLayoutDataItemPropTypes} from '../../../../../../prop-types/index';

const HTML_TAGS = [
	'div',
	'header',
	'nav',
	'section',
	'article',
	'main',
	'aside',
	'footer',
];

export default function ContainerAdvancedPanel({item}) {
	const dispatch = useDispatch();
	const segmentsExperienceId = useSelector(selectSegmentsExperienceId);

	return (
		<>
			<SelectField
				className="mb-1"
				field={{
					label: Liferay.Language.get('html-tag'),
					name: 'htmlTag',
					typeOptions: {
						validValues: HTML_TAGS.map((tag) => ({
							label: tag,
							value: tag,
						})),
					},
				}}
				onValueSelect={(name, value) => {
					const itemConfig = {[name]: value === 'div' ? '' : value};

					dispatch(
						updateItemConfig({
							itemConfig,
							itemId: item.itemId,
							segmentsExperienceId,
						})
					);
				}}
				value={item.config.htmlTag}
			/>
			<p className="small text-secondary">
				{Liferay.Language.get('misusing-this-setup-might-impact-seo')}
			</p>

			<HideFromSearchField item={item} />
		</>
	);
}

ContainerAdvancedPanel.propTypes = {
	item: getLayoutDataItemPropTypes().isRequired,
};
