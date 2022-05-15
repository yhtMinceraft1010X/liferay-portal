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

package com.liferay.mail.reader.internal.search.spi.model.index.contributor;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.mail.reader.model.Account;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.expando.ExpandoBridgeIndexer;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Scott Lee
 * @author Peter Fellwock
 * @author Luan Maoski
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.liferay.mail.reader.model.Account",
	service = ModelDocumentContributor.class
)
public class AccountModelDocumentContributor
	implements ModelDocumentContributor<Account> {

	@Override
	public void contribute(Document document, Account account) {
		ExpandoBridge expandoBridge = account.getExpandoBridge();

		document.addKeyword("accountId", account.getAccountId());
		document.addText(Field.NAME, account.getAddress());

		_expandoBridgeIndexer.addAttributes(document, expandoBridge);
	}

	@Reference
	private ExpandoBridgeIndexer _expandoBridgeIndexer;

}