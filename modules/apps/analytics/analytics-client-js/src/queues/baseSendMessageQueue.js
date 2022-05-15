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

import ClientAdapter from '../clientAdapter';
import BaseQueue from './baseQueue';

class BaseSendMessageQueue extends BaseQueue {
	constructor({analyticsInstance, flushTo, name}) {
		super({analyticsInstance, name});

		this.clientAdapter = new ClientAdapter({
			endpointUrl: flushTo,
			projectId: analyticsInstance.config.projectId,
		});
	}

	onFlush() {
		return this.getItems().map((message) =>
			this.clientAdapter
				.sendWithTimeout(message)
				.then(() => {
					this._dequeue(message.id);
				})
				.catch((error) => {
					if (error.status === 400) {
						this._dequeue(message.id);
					}

					return Promise.reject(error);
				})
		);
	}
}

export {BaseSendMessageQueue};
export default BaseSendMessageQueue;
