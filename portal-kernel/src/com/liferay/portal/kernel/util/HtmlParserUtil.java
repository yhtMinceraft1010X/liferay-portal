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

package com.liferay.portal.kernel.util;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Provides utility methods for rendering HTML text.
 * This class uses XSS recommendations from <a
 * href="http://www.owasp.org/index.php/Cross_Site_Scripting#How_to_Protect_Yourself">http://www.owasp.org/index.php/Cross_Site_Scripting#How_to_Protect_Yourself</a>
 * when escaping HTML text.
 *
 * @author Brian Wing Shun Chan
 * @author Clarence Shen
 * @author Harry Mark
 * @author Samuel Kong
 */
public class HtmlParserUtil {

	/**
	 * Extracts the raw text from the HTML input, compressing its whitespace and
	 * removing all attributes, scripts, and styles.
	 *
	 * <p>
	 * For example, raw text returned by this method can be stored in a search
	 * index.
	 * </p>
	 *
	 * @param  html the HTML text
	 * @return the raw text from the HTML input, or <code>null</code> if the
	 *         HTML input is <code>null</code>
	 */
	public static String extractText(String html) {
		return _htmlParser.extractText(html);
	}

	public static String findAttributeValue(
		Predicate<Function<String, String>> findValuePredicate,
		Function<Function<String, String>, String> returnValueFunction,
		String html, String startTagName) {

		return _htmlParser.findAttributeValue(
			findValuePredicate, returnValueFunction, html, startTagName);
	}

	/**
	 * Renders the HTML content into text. This provides a human readable
	 * version of the segment content that is modeled on the way Mozilla
	 * Thunderbird&reg; and other email clients provide an automatic conversion
	 * of HTML content to text in their alternative MIME encoding of emails.
	 *
	 * <p>
	 * Using the default settings, the output complies with the
	 * <code>Text/Plain; Format=Flowed (DelSp=No)</code> protocol described in
	 * <a href="http://tools.ietf.org/html/rfc3676">RFC-3676</a>.
	 * </p>
	 *
	 * @param  html the HTML text
	 * @return the rendered HTML text, or <code>null</code> if the HTML text is
	 *         <code>null</code>
	 */
	public static String render(String html) {
		return _htmlParser.render(html);
	}

	private static volatile HtmlParser _htmlParser =
		ServiceProxyFactory.newServiceTrackedInstance(
			HtmlParser.class, HtmlParserUtil.class, "_htmlParser", true);

}