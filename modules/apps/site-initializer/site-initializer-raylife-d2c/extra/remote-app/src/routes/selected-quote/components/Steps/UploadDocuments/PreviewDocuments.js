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
import classNames from 'classnames';
import ProgressBar from '../../../../../common/components/progress-bar';
import DropArea from '../../DropArea';

const PreviewBody = ({
	file,
	onRemoveFile,
	showCloseButton = true,
	showName = true,
}) => (
	<>
		{showName && (
			<span
				className="c-mt-1 ellipsis mb-3 text-center text-neutral-7 text-paragraph-sm"
				title={file.name}
			>
				{file.name}
			</span>
		)}

		{showCloseButton && (
			<div
				className="align-items-center bg-neutral-0 close-icon d-flex justify-content-center position-relative rounded-circle"
				onClick={() => onRemoveFile(file)}
			>
				<ClayIcon symbol="times" />
			</div>
		)}
	</>
);

const PreviewDocument = ({
	file,
	onRemoveFile,
	showCloseButton = true,
	showName = true,
	type = 'image',
}) => (
	<div className="d-flex flex-column mr-1 mr-sm-3 view-file-document">
		<div
			className="d-flex div-document flex-column text-center"
			title={file.name}
		>
			<div className="align-items-center bg-neutral-1 content d-flex justify-content-center rounded-xl">
				{type === 'image' ? (
					<div className="d-flex flex-column image text-center">
						<img
							alt={file.name}
							className="rounded-xl"
							src={file.fileURL}
						/>
					</div>
				) : (
					<ClayIcon className={file.icon} symbol={file.icon} />
				)}
			</div>

			<PreviewBody
				file={file}
				onRemoveFile={onRemoveFile}
				showCloseButton={showCloseButton}
				showName={showName}
			/>
		</div>
	</div>
);

const PreviewDocuments = ({
	dropAreaProps,
	files = [],
	onRemoveFile,
	setFiles,
	setShowBadgeInfo,
	type,
}) => (
	<div className="d-flex flex-wrap view-file">
		{files.map((file, index) => {
			if (file.progress < 100) {
				return (
					<div
						className={classNames('flex-column', {
							'mr-1 mr-sm-3': index < 3,
						})}
						title={file.name}
					>
						<div className="align-items-center bg-brand-primary-lighten-6 c-mb-1 card d-flex flex-column justify-content-center rounded-xl">
							<p className="font-weight-normal text-neutral-8 text-paragraph">
								Uploading...
							</p>

							<ProgressBar
								height="4"
								progress={file.progress}
								width="144"
							/>
						</div>

						<PreviewBody
							file={file}
							onRemoveFile={onRemoveFile}
							showCloseButton={false}
						/>
					</div>
				);
			}

			return (
				<PreviewDocument
					file={file}
					key={index}
					onRemoveFile={onRemoveFile}
					type={type}
				/>
			);
		})}

		<DropArea
			dropAreaProps={dropAreaProps}
			files={files}
			setFiles={setFiles}
			setShowBadgeInfo={setShowBadgeInfo}
		/>
	</div>
);

export default PreviewDocuments;
