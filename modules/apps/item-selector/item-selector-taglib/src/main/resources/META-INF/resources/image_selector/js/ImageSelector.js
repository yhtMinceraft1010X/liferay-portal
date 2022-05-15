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

import ClayIcon from '@clayui/icon';
import {State} from '@liferay/frontend-js-state-web';
import classNames from 'classnames';
import {STATUS_CODE} from 'frontend-js-web';
import PropTypes from 'prop-types';
import React, {useEffect, useRef, useState} from 'react';

import imageSelectorImageAtom, {
	STR_NULL_IMAGE_FILE_ENTRY_ID,
} from '../../atoms/imageSelectorImageAtom';
import DropHereInfo from '../../drop_here_info/js/DropHereInfo';
import BrowseImage from './BrowseImage';
import ChangeImageControls from './ChangeImageControls';
import ErrorAlert from './ErrorAlert';
import ImageSelectorImage from './ImageSelectorImage';
import ProgressWrapper from './ProgressWrapper';

const CSS_DROP_ACTIVE = 'drop-active';
const CSS_PROGRESS_ACTIVE = 'progress-active';

const STR_SPACE = ' ';

const TPL_PROGRESS_DATA =
	'<strong>{0}</strong> {1} of <strong>{2}</strong> {3}';

const ImageSelector = ({
	fileEntryId = STR_NULL_IMAGE_FILE_ENTRY_ID,
	imageCropDirection,
	imageCropRegion: initialImageCropRegion,
	imageURL,
	isDraggable,
	itemSelectorEventName,
	itemSelectorURL,
	maxFileSize = Liferay.PropsValues.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE,
	portletNamespace,
	paramName,
	uploadURL,
	validExtensions,
}) => {
	const [errorMessage, setErrorMessage] = useState('');
	const [fileName, setFileName] = useState('');
	const [image, setImage] = useState({
		fileEntryId,
		src: imageURL,
	});
	const [imageCropRegion, setImageCropRegion] = useState(
		initialImageCropRegion
	);
	const [progressData, setProgressData] = useState();
	const [progressValue, setProgressValue] = useState(0);

	const rootNodeRef = useRef(null);
	const uploaderRef = useRef(null);
	const uploaderStatusStoppedRef = useRef(null);

	const getErrorMessage = (errorObj) => {
		let message = Liferay.Language.get(
			'an-unexpected-error-occurred-while-uploading-your-file'
		);

		const errorType = errorObj.errorType;

		if (
			errorType === STATUS_CODE.SC_FILE_ANTIVIRUS_EXCEPTION ||
			errorType === STATUS_CODE.SC_FILE_CUSTOM_EXCEPTION
		) {
			message = errorObj.message;
		}
		else if (errorType === STATUS_CODE.SC_FILE_EXTENSION_EXCEPTION) {
			if (validExtensions) {
				message = Liferay.Util.sub(
					Liferay.Language.get(
						'please-enter-a-file-with-a-valid-extension-x'
					),
					[validExtensions]
				);
			}
			else {
				message = Liferay.Util.sub(
					Liferay.Language.get(
						'please-enter-a-file-with-a-valid-file-type'
					)
				);
			}
		}
		else if (errorType === STATUS_CODE.SC_FILE_NAME_EXCEPTION) {
			message = Liferay.Language.get(
				'please-enter-a-file-with-a-valid-file-name'
			);
		}
		else if (errorType === STATUS_CODE.SC_FILE_SIZE_EXCEPTION) {
			message = Liferay.Util.sub(
				Liferay.Language.get(
					'please-enter-a-file-with-a-valid-file-size-no-larger-than-x'
				),
				[Liferay.Util.formatStorage(parseInt(maxFileSize, 10))]
			);
		}
		else if (errorType === STATUS_CODE.SC_UPLOAD_REQUEST_SIZE_EXCEPTION) {
			const maxUploadRequestSize =
				Liferay.PropsValues.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE;

			message = Liferay.Util.sub(
				Liferay.Language.get(
					'request-is-larger-than-x-and-could-not-be-processed'
				),
				[Liferay.Util.formatStorage(maxUploadRequestSize)]
			);
		}

		return message;
	};

	const handleDeleteImageClick = () => {
		setImage({
			fileEntryId: STR_NULL_IMAGE_FILE_ENTRY_ID,
			src: '',
		});
	};

	const handleFileSelect = (event) => {
		rootNodeRef.current.classList.remove(CSS_DROP_ACTIVE);

		const file = event.fileList[0];

		setFileName(file.get('name'));

		showImagePreview(file.get('file'));

		const uploader = uploaderRef.current;
		const queue = uploader.queue;

		if (queue && queue._currentState === uploaderStatusStoppedRef.current) {
			queue.startUpload();
		}

		uploader.uploadThese(event.fileList);
	};

	const handleImageCropped = (cropRegion) => {
		setImageCropRegion(JSON.stringify(cropRegion));
	};

	const handleSelectFileClick = () => {
		Liferay.Util.openSelectionModal({
			onSelect: (selectedItem) => {
				if (selectedItem) {
					const itemValue = JSON.parse(selectedItem.value);

					setImage({
						fileEntryId:
							itemValue.fileEntryId ||
							STR_NULL_IMAGE_FILE_ENTRY_ID,
						src: itemValue.url || '',
					});
				}
			},
			selectEventName: itemSelectorEventName,
			title: Liferay.Language.get('select-file'),
			url: itemSelectorURL,
		});
	};

	const handleUploadCancel = () => {
		uploaderRef.current.queue.cancelUpload();
		stopProgress();
	};

	const handleUploadComplete = (event) => {
		stopProgress();

		const data = JSON.parse(event.data);

		const image = data.file;
		const success = data.success;

		if (success) {
			setImage({
				fileEntryId: image.fileEntryId,
				src: image.url,
			});
		}
		else {
			setImage({
				fileEntryId: STR_NULL_IMAGE_FILE_ENTRY_ID,
				src: '',
			});

			setErrorMessage(getErrorMessage(data.error));
		}
	};

	const handleUploadProgressChange = (event) => {
		const percentLoaded = Math.round(event.percentLoaded);

		setProgressValue(Math.ceil(percentLoaded));

		const bytesLoaded = Liferay.Util.formatStorage(event.bytesLoaded);

		const bytesTotal = Liferay.Util.formatStorage(event.bytesTotal);

		const bytesLoadedSpaceIndex = bytesLoaded.indexOf(STR_SPACE);

		const bytesTotalSpaceIndex = bytesTotal.indexOf(STR_SPACE);

		setProgressData(
			Liferay.Util.sub(
				TPL_PROGRESS_DATA,
				bytesLoaded.substring(0, bytesLoadedSpaceIndex),
				bytesLoaded.substring(bytesLoadedSpaceIndex + 1),
				bytesTotal.substring(0, bytesTotalSpaceIndex),
				bytesTotal.substring(bytesTotalSpaceIndex + 1)
			)
		);
	};

	const showImagePreview = (file) => {
		const reader = new FileReader();

		reader.addEventListener('loadend', () => {
			if (progressValue < 100) {
				setImage({
					fileEntryId: '-1',
					src: reader.result,
				});
			}
		});

		reader.readAsDataURL(file);
	};

	const stopProgress = () => {
		rootNodeRef.current.classList.remove(CSS_PROGRESS_ACTIVE);

		setProgressValue(0);
	};

	useEffect(() => {
		State.write(imageSelectorImageAtom, {
			...image,
			paramName,
		});
	}, [image, paramName]);

	useEffect(() => {
		AUI().use('uploader', (A) => {
			const rootNode = rootNodeRef.current;

			uploaderRef.current = new A.Uploader({
				boundingBox: rootNode,
				dragAndDropArea: rootNode,
				fileFieldName: 'imageSelectorFileName',
				on: {
					dragleave() {
						rootNode.classList.remove(CSS_DROP_ACTIVE);
					},
					dragover() {
						rootNode.classList.add(CSS_DROP_ACTIVE);
					},
					fileselect: handleFileSelect,
					uploadcomplete: handleUploadComplete,
					uploadprogress: handleUploadProgressChange,
					uploadstart() {
						rootNode.classList.add(CSS_PROGRESS_ACTIVE);
					},
				},
				uploadURL,
			}).render();

			uploaderStatusStoppedRef.current = A.Uploader.Queue.STOPPED;
		});
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	useEffect(() => {
		if (image.fileEntryId !== STR_NULL_IMAGE_FILE_ENTRY_ID) {
			setErrorMessage('');
		}
	}, [image]);

	return (
		<div
			className={classNames(
				'drop-zone',
				{'draggable-image': isDraggable},
				{[`${imageCropDirection}`]: isDraggable},
				{
					'drop-enabled':
						image.fileEntryId === STR_NULL_IMAGE_FILE_ENTRY_ID,
				},
				'taglib-image-selector'
			)}
			ref={rootNodeRef}
		>
			<input
				id={`${portletNamespace}${paramName}Id`}
				name={`${portletNamespace}${paramName}Id`}
				type="hidden"
				value={image.fileEntryId}
			/>

			<input
				id={`${portletNamespace}${paramName}CropRegion`}
				name={`${portletNamespace}${paramName}CropRegion`}
				type="hidden"
				value={imageCropRegion}
			/>

			{image.src && (
				<ImageSelectorImage
					direction={imageCropDirection}
					imageSrc={image.src}
					isCroppable={isDraggable}
					onImageCrop={handleImageCropped}
					portletNamespace={portletNamespace}
				/>
			)}

			{image.fileEntryId === STR_NULL_IMAGE_FILE_ENTRY_ID && (
				<BrowseImage
					handleClick={handleSelectFileClick}
					itemSelectorEventName={itemSelectorEventName}
					itemSelectorURL={itemSelectorURL}
					maxFileSize={maxFileSize}
					validExtensions={validExtensions}
				/>
			)}

			<span className="selection-status">
				<ClayIcon symbol="check" />
			</span>

			{image.fileEntryId !== STR_NULL_IMAGE_FILE_ENTRY_ID && (
				<ChangeImageControls
					handleClickDelete={handleDeleteImageClick}
					handleClickPicture={handleSelectFileClick}
				/>
			)}

			<DropHereInfo />

			<ProgressWrapper
				fileName={fileName}
				onCancel={handleUploadCancel}
				progressData={progressData}
				progressValue={progressValue}
			/>

			{errorMessage && (
				<ErrorAlert
					handleClick={handleSelectFileClick}
					handleClose={() => setErrorMessage('')}
					itemSelectorEventName={itemSelectorEventName}
					itemSelectorURL={itemSelectorURL}
					message={errorMessage}
				/>
			)}
		</div>
	);
};

ImageSelector.propTypes = {
	cropRegion: PropTypes.string,
	fileEntryId: PropTypes.string.isRequired,
	imageCropDirection: PropTypes.string,
	imageURL: PropTypes.string,
	isDraggable: PropTypes.bool,
	itemSelectorEventName: PropTypes.string,
	itemSelectorURL: PropTypes.string,
	maxFileSize: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
	paramName: PropTypes.string.isRequired,
	portletNamespace: PropTypes.string.isRequired,
	validExtensions: PropTypes.string,
};

export default ImageSelector;
