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

import {useIsMounted} from '@liferay/frontend-js-react-web';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import React, {useCallback, useEffect, useState} from 'react';

import {config} from '../../config/index';
import {
	useGetContent,
	useGetFieldValue,
	useToControlsId,
} from '../../contexts/CollectionItemContext';
import {useIsProcessorEnabled} from '../../contexts/EditableProcessorContext';
import {useGlobalContext} from '../../contexts/GlobalContext';
import {
	useDispatch,
	useSelector,
	useSelectorCallback,
} from '../../contexts/StoreContext';
import selectCanConfigureWidgets from '../../selectors/selectCanConfigureWidgets';
import selectLanguageId from '../../selectors/selectLanguageId';
import selectSegmentsExperienceId from '../../selectors/selectSegmentsExperienceId';
import checkStylesFF from '../../utils/checkStylesFF';
import resolveEditableConfig from '../../utils/editable-value/resolveEditableConfig';
import resolveEditableValue from '../../utils/editable-value/resolveEditableValue';
import {getCommonStyleByName} from '../../utils/getCommonStyleByName';
import {getFrontendTokenValue} from '../../utils/getFrontendTokenValue';
import getLayoutDataItemUniqueClassName from '../../utils/getLayoutDataItemUniqueClassName';
import {getResponsiveConfig} from '../../utils/getResponsiveConfig';
import hasInnerCommonStyles from '../../utils/hasInnerCustomStyles';
import {isValidSpacingOption} from '../../utils/isValidSpacingOption';
import useBackgroundImageValue from '../../utils/useBackgroundImageValue';
import {useId} from '../../utils/useId';
import UnsafeHTML from '../UnsafeHTML';
import FragmentContentInteractionsFilter from './FragmentContentInteractionsFilter';
import FragmentContentProcessor from './FragmentContentProcessor';
import getAllEditables from './getAllEditables';

const FragmentContent = ({
	className,
	elementRef,
	fragmentEntryLinkId,
	getPortals,
	item,
	withinTopper = false,
}) => {
	const dispatch = useDispatch();
	const isMounted = useIsMounted();
	const isProcessorEnabled = useIsProcessorEnabled();
	const globalContext = useGlobalContext();
	const toControlsId = useToControlsId();
	const getFieldValue = useGetFieldValue();

	const canConfigureWidgets = useSelector(selectCanConfigureWidgets);

	const [editables, setEditables] = useState([]);

	/**
	 * Updates editables array for the rendered fragment.
	 * @param {HTMLElement} [nextFragmentElement] Fragment element
	 *  If not specified, fragmentElement state is used instead.
	 * @return {Array} Updated editables array
	 */
	const onRender = useCallback(
		(fragmentElement) => {
			let nextEditables = [];

			if (isMounted()) {
				nextEditables = getAllEditables(fragmentElement).map(
					(editable) => ({
						...editable,
						fragmentEntryLinkId,
						itemId: `${fragmentEntryLinkId}-${editable.editableId}`,
						parentId: item.itemId,
					})
				);
			}

			setEditables(nextEditables);

			return nextEditables;
		},
		[isMounted, fragmentEntryLinkId, item]
	);

	const fragmentEntryLink = useSelectorCallback(
		(state) => state.fragmentEntryLinks[fragmentEntryLinkId],
		[fragmentEntryLinkId]
	);

	const languageId = useSelector(selectLanguageId);
	const segmentsExperienceId = useSelector(selectSegmentsExperienceId);
	const selectedViewportSize = useSelector(
		(state) => state.selectedViewportSize
	);

	const defaultContent = useGetContent(
		fragmentEntryLink,
		languageId,
		segmentsExperienceId
	);
	const [content, setContent] = useState(defaultContent);

	/* eslint-disable-next-line react-hooks/exhaustive-deps */
	const editableValues = fragmentEntryLink
		? fragmentEntryLink.editableValues
		: {};

	const fragmentEntryLinkError = fragmentEntryLink?.error;

	useEffect(() => {
		if (fragmentEntryLinkError) {
			throw new Error(fragmentEntryLinkError);
		}
	}, [fragmentEntryLinkError]);

	const isBeingEdited = editables.some((editable) =>
		isProcessorEnabled(toControlsId(editable.itemId))
	);

	/**
	 * fragmentElement keeps a copy of the fragment real HTML,
	 * we perform editableValues replacements over this copy
	 * to avoid multiple re-renders, when every replacement has
	 * finished, this function must be called.
	 *
	 * Synchronizes fragmentElement's content to the real fragment
	 * content. When this happens, the real re-render is performed.
	 */
	useEffect(() => {
		let fragmentElement = document.createElement('div');

		if (!isBeingEdited) {
			fragmentElement.innerHTML = defaultContent;

			Promise.all(
				getAllEditables(fragmentElement).map((editable) => {
					const editableValue =
						editableValues[editable.editableValueNamespace][
							editable.editableId
						];

					return Promise.all([
						resolveEditableValue(
							editableValue,
							languageId,
							getFieldValue
						),
						resolveEditableConfig(
							editableValue?.config || {},
							languageId,
							getFieldValue
						),
					]).then(([value, editableConfig]) => {
						editable.processor.render(
							editable.element,
							value,
							editableConfig,
							languageId
						);

						editable.element.classList.add('page-editor__editable');
					});
				})
			).then(() => {
				if (isMounted() && fragmentElement) {
					setContent(fragmentElement.innerHTML);
				}
			});
		}

		return () => {
			fragmentElement = null;
		};
	}, [
		defaultContent,
		dispatch,
		editableValues,
		fragmentEntryLink,
		fragmentEntryLinkId,
		getFieldValue,
		isBeingEdited,
		isMounted,
		isProcessorEnabled,
		languageId,
		segmentsExperienceId,
		toControlsId,
	]);

	const responsiveConfig = getResponsiveConfig(
		item.config,
		selectedViewportSize
	);

	const {
		backgroundColor,
		backgroundImage,
		borderColor,
		borderRadius,
		borderWidth,
		display,
		fontFamily,
		fontSize,
		fontWeight,
		height,
		marginBottom,
		marginLeft,
		marginRight,
		marginTop,
		maxHeight,
		maxWidth,
		minHeight,
		minWidth,
		opacity,
		overflow,
		paddingBottom,
		paddingLeft,
		paddingRight,
		paddingTop,
		shadow,
		textAlign,
		textColor,
		width,
	} = responsiveConfig.styles;

	const elementId = useId();
	const backgroundImageValue = useBackgroundImageValue(
		elementId,
		backgroundImage,
		getFieldValue
	);

	const style = {};

	style.backgroundColor = getFrontendTokenValue(backgroundColor);
	style.borderColor = getFrontendTokenValue(borderColor);
	style.borderRadius = getFrontendTokenValue(borderRadius);
	style.color = getFrontendTokenValue(textColor);
	style.fontFamily = getFrontendTokenValue(fontFamily);
	style.fontSize = getFrontendTokenValue(fontSize);
	style.fontWeight = getFrontendTokenValue(fontWeight);
	style.height = height;
	style.maxHeight = maxHeight;
	style.minHeight = minHeight;
	style.opacity = opacity ? opacity / 100 : null;
	style.overflow = overflow;

	if (borderWidth) {
		style.borderWidth = `${borderWidth}px`;
		style.borderStyle = 'solid';
	}

	if (!withinTopper) {
		style.boxShadow = getFrontendTokenValue(shadow);
		style.display = display;
		style.maxWidth = maxWidth;
		style.minWidth = minWidth;
		style.width = width;
	}

	if (backgroundImageValue.url) {
		style.backgroundImage = `url(${backgroundImageValue.url})`;
		style.backgroundPosition = '50% 50%';
		style.backgroundRepeat = 'no-repeat';
		style.backgroundSize = 'cover';

		if (backgroundImage?.fileEntryId) {
			style['--background-image-file-entry-id'] =
				backgroundImage.fileEntryId;
		}
	}

	const textAlignDefaultValue = getCommonStyleByName('textAlign')
		.defaultValue;

	return (
		<>
			<FragmentContentInteractionsFilter
				editables={editables}
				fragmentEntryLinkId={fragmentEntryLinkId}
				itemId={item.itemId}
			>
				<UnsafeHTML
					className={classNames(
						className,
						'page-editor__fragment-content',
						{
							[`${fragmentEntryLink?.cssClass}`]: config.featureFlagLps132571,
							[getLayoutDataItemUniqueClassName(item.itemId)]:
								config.featureFlagLps132571 &&
								!hasInnerCommonStyles(fragmentEntryLink),
							'page-editor__fragment-content--portlet-topper-hidden': !canConfigureWidgets,
							[`mb-${marginBottom}`]:
								isValidSpacingOption(marginBottom) &&
								!withinTopper,
							[`ml-${marginLeft}`]:
								isValidSpacingOption(marginLeft) &&
								!withinTopper,
							[`mr-${marginRight}`]:
								isValidSpacingOption(marginRight) &&
								!withinTopper,
							[`mt-${marginTop}`]:
								isValidSpacingOption(marginTop) &&
								!withinTopper,
							[`pb-${paddingBottom}`]: isValidSpacingOption(
								paddingBottom
							),
							[`pl-${paddingLeft}`]: isValidSpacingOption(
								paddingLeft
							),
							[`pr-${paddingRight}`]: isValidSpacingOption(
								paddingRight
							),
							[`pt-${paddingTop}`]: isValidSpacingOption(
								paddingTop
							),
							[textAlign
								? textAlign.startsWith('text-')
									? textAlign
									: `text-${textAlign}`
								: `text-${textAlignDefaultValue}`]:
								!config.featureFlagLps132571 &&
								textAlignDefaultValue,
						}
					)}
					contentRef={elementRef}
					data={{fragmentEntryLinkId}}
					getPortals={getPortals}
					globalContext={globalContext}
					id={elementId}
					markup={content}
					onRender={withinTopper ? onRender : () => {}}
					style={checkStylesFF(item.itemId, style)}
				/>

				{backgroundImageValue.mediaQueries ? (
					<style>{backgroundImageValue.mediaQueries}</style>
				) : null}
			</FragmentContentInteractionsFilter>

			<FragmentContentProcessor
				editables={editables}
				fragmentEntryLinkId={fragmentEntryLinkId}
			/>
		</>
	);
};

FragmentContent.propTypes = {
	className: PropTypes.string,
	fragmentEntryLinkId: PropTypes.string.isRequired,
	getPortals: PropTypes.func.isRequired,
	item: PropTypes.object.isRequired,
	withinTopper: PropTypes.bool,
};

export default React.memo(FragmentContent);
