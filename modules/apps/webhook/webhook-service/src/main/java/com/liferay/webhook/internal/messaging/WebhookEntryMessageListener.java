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

package com.liferay.webhook.internal.messaging;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.webhook.model.WebhookEntry;

/**
 * @author Eduardo García
 * @author Brian Wing Shun Chan
 */
public class WebhookEntryMessageListener extends BaseMessageListener {

	public WebhookEntryMessageListener(WebhookEntry webhookEntry) {
		_webhookEntry = webhookEntry;
	}

	@Override
	protected void doReceive(Message message) throws Exception {
		long companyId = message.getLong("companyId");

		if (_webhookEntry.getCompanyId() != companyId) {
			return;
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Processing message " + message);
		}

		Http.Options options = new Http.Options();

		options.addHeader(
			HttpHeaders.CONTENT_TYPE, ContentTypes.APPLICATION_JSON);
		options.setBody(
			String.valueOf(message.getPayload()), ContentTypes.APPLICATION_JSON,
			StringPool.UTF8);
		options.setLocation(_webhookEntry.getURL());
		options.setPost(true);

		HttpUtil.URLtoString(options);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		WebhookEntryMessageListener.class);

	private final WebhookEntry _webhookEntry;

}