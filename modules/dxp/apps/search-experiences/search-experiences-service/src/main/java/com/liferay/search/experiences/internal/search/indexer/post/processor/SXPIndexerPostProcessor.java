/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.internal.search.indexer.post.processor;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.journal.model.JournalArticle;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.search.BaseIndexerPostProcessor;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.wiki.model.WikiPage;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true,
	property = {
		"indexer.class.name=com.liferay.blogs.model.BlogsEntry",
		"indexer.class.name=com.liferay.document.library.kernel.model.DLFileEntry",
		"indexer.class.name=com.liferay.journal.model.JournalArticle",
		"indexer.class.name=com.liferay.knowledge.base.model.KBArticle",
		"indexer.class.name=com.liferay.wiki.model.WikiPage"
	},
	service = IndexerPostProcessor.class
)
public class SXPIndexerPostProcessor extends BaseIndexerPostProcessor {

	@Override
	public void postProcessDocument(Document document, Object object)
		throws Exception {

		_addContentLengths(document);
		_addVersionCount(document, object);
	}

	private void _addContentLengths(Document document) {
		String content = document.get(Field.CONTENT);

		if (!Validator.isBlank(content)) {
			document.addNumber("contentLength", content.length());
		}

		long groupId = GetterUtil.getLong(document.get(Field.GROUP_ID));

		for (Locale locale : _language.getAvailableLocales(groupId)) {
			String localizedContent = document.get(
				StringBundler.concat(
					Field.CONTENT, StringPool.UNDERLINE,
					_language.getLanguageId(locale)));

			if (Validator.isBlank(localizedContent)) {
				continue;
			}

			document.addNumber(
				"contentLength_" + _language.getLanguageId(locale),
				localizedContent.length());
		}
	}

	private void _addVersionCount(Document document, Object object) {
		Class<?> clazz = object.getClass();

		Integer versionCount = null;

		if (DLFileEntry.class.isAssignableFrom(clazz)) {
			DLFileEntry dlFileEntry = (DLFileEntry)object;

			versionCount = _toInteger(dlFileEntry.getVersion());
		}
		else if (JournalArticle.class.isAssignableFrom(clazz)) {
			JournalArticle journalArticle = (JournalArticle)object;

			versionCount = _toInteger(journalArticle.getVersion());
		}
		else if (KBArticle.class.isAssignableFrom(clazz)) {
			KBArticle kbArticle = (KBArticle)object;

			versionCount = kbArticle.getVersion();
		}
		else if (WikiPage.class.isAssignableFrom(clazz)) {
			WikiPage wikiPage = (WikiPage)object;

			versionCount = _toInteger(wikiPage.getVersion());
		}

		if (versionCount != null) {
			document.addNumber("versionCount", versionCount);
		}
	}

	private int _toInteger(double version) {
		return _toInteger(String.valueOf(version));
	}

	private int _toInteger(String version) {

		// TODO 2.5 and 5.2 return the same value

		String[] versionParts = version.split("\\.");

		int major = GetterUtil.getInteger(versionParts[0]);
		int minor = GetterUtil.getInteger(versionParts[1]);

		return major + minor;
	}

	@Reference
	private Language _language;

}