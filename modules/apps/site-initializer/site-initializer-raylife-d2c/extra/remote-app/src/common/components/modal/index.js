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
import {useEffect} from 'react';
import useImperativeDisableScroll from '../../hooks/useImperativeDisableScroll';

const MODAL_BACKDROP_ID = 'modal-backdrop';

const Modal = ({
	backdropLight = false,
	closeable = true,
	children,
	footer,
	onClose = () => {},
	size = 'medium',
	show,
}) => {
	useImperativeDisableScroll({disabled: show, element: document.body});

	useEffect(() => {
		const clickOutsideEventListener = (event) => {
			const [firstPath] = event.path ||
				(event.composedPath && event.composedPath()) || [{}];

			if (firstPath.id === MODAL_BACKDROP_ID) {
				onClose();
			}
		};

		if (show) {
			document.addEventListener('mousedown', clickOutsideEventListener);
			document.addEventListener('touchstart', clickOutsideEventListener);
		}

		return () => {
			document.removeEventListener(
				'mousedown',
				clickOutsideEventListener
			);
			document.removeEventListener(
				'touchstart',
				clickOutsideEventListener
			);
		};
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [show]);

	if (!show) {
		return null;
	}

	return (
		<>
			<div
				className={classNames(
					'h-100 w-100 overflow-auto position-fixed backdrop',
					{
						'd-block': show,
						'd-none': !show,
						'dark': !backdropLight,
						'light': backdropLight,
					}
				)}
				id={MODAL_BACKDROP_ID}
			/>

			<div
				className={classNames(
					'align-items-stretch bg-neutral-0 d-flex flex-column m-auto modal-content-body position-fixed pb-4 pt-3 px-3 rounded',
					{
						'modal-large': size === 'large',
						'modal-medium': size === 'medium',
						'modal-small': size === 'small',
						'modal-small-mobile': size === 'small-mobile',
					}
				)}
			>
				{closeable && (
					<div className="align-items-center border-bottom-0 d-flex justify-content-end p-0">
						<div className="close-modal" onClick={onClose}>
							<ClayIcon symbol="times" />
						</div>
					</div>
				)}

				{children}

				{footer}
			</div>
		</>
	);
};

export default Modal;
