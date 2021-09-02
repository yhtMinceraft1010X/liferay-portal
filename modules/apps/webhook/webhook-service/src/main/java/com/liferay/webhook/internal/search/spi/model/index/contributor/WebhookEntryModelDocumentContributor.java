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

package com.liferay.webhook.internal.search.spi.model.index.contributor;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import com.liferay.webhook.model.WebhookEntry;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "indexer.class.name=com.liferay.webhook.model.WebhookEntry",
	service = ModelDocumentContributor.class
)
public class WebhookEntryModelDocumentContributor
	implements ModelDocumentContributor<WebhookEntry> {

	@Override
	public void contribute(Document document, WebhookEntry webhookEntry) {
		if (_log.isDebugEnabled()) {
			_log.debug("Indexing webhook entry " + webhookEntry);
		}

		document.addText(Field.NAME, webhookEntry.getName());

		if (_log.isDebugEnabled()) {
			_log.debug("Document " + webhookEntry + " indexed successfully");
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		WebhookEntryModelDocumentContributor.class);

}