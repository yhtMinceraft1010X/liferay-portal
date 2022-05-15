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

import java.util.Map;

/**
 * Provides utility methods for escaping, replacing, and stripping
 * HTML text. This class uses XSS recommendations from <a
 * href="http://www.owasp.org/index.php/Cross_Site_Scripting#How_to_Protect_Yourself">http://www.owasp.org/index.php/Cross_Site_Scripting#How_to_Protect_Yourself</a>
 * when escaping HTML text.
 *
 * @author Brian Wing Shun Chan
 * @author Clarence Shen
 * @author Harry Mark
 * @author Samuel Kong
 */
public class HtmlUtil {

	public static String buildData(Map<String, Object> data) {
		return _html.buildData(data);
	}

	/**
	 * Escapes the text so that it is safe to use in an HTML context.
	 *
	 * @param  text the text to escape
	 * @return the escaped HTML text, or <code>null</code> if the text is
	 *         <code>null</code>
	 */
	public static String escape(String text) {
		return _html.escape(text);
	}

	/**
	 * Escapes the attribute value so that it is safe to use as an attribute
	 * value.
	 *
	 * @param  attribute the attribute to escape
	 * @return the escaped attribute value, or <code>null</code> if the
	 *         attribute value is <code>null</code>
	 */
	public static String escapeAttribute(String attribute) {
		return _html.escapeAttribute(attribute);
	}

	/**
	 * Escapes the CSS value so that it is safe to use in a CSS context.
	 *
	 * @param  css the CSS value to escape
	 * @return the escaped CSS value, or <code>null</code> if the CSS value is
	 *         <code>null</code>
	 */
	public static String escapeCSS(String css) {
		return _html.escapeCSS(css);
	}

	/**
	 * Escapes the HREF attribute so that it is safe to use as an HREF
	 * attribute.
	 *
	 * @param  href the HREF attribute to escape
	 * @return the escaped HREF attribute, or <code>null</code> if the HREF
	 *         attribute is <code>null</code>
	 */
	public static String escapeHREF(String href) {
		return _html.escapeHREF(href);
	}

	/**
	 * Escapes the JavaScript value so that it is safe to use in a JavaScript
	 * context.
	 *
	 * @param  js the JavaScript value to escape
	 * @return the escaped JavaScript value, or <code>null</code> if the
	 *         JavaScript value is <code>null</code>
	 */
	public static String escapeJS(String js) {
		return _html.escapeJS(js);
	}

	public static String escapeJSLink(String link) {
		return _html.escapeJSLink(link);
	}

	/**
	 * Escapes the URL value so that it is safe to use as a URL.
	 *
	 * @param  url the URL value to escape
	 * @return the escaped URL value, or <code>null</code> if the URL value is
	 *         <code>null</code>
	 */
	public static String escapeURL(String url) {
		return _html.escapeURL(url);
	}

	public static String escapeXPath(String xPath) {
		return _html.escapeXPath(xPath);
	}

	public static String escapeXPathAttribute(String xPathAttribute) {
		return _html.escapeXPathAttribute(xPathAttribute);
	}

	public static String fromInputSafe(String text) {
		return _html.fromInputSafe(text);
	}

	public static String getAUICompatibleId(String html) {
		return _html.getAUICompatibleId(html);
	}

	public static Html getHtml() {
		return _html;
	}

	/**
	 * Replaces all new lines or carriage returns with the <code><br /></code>
	 * HTML tag.
	 *
	 * @param  html the text
	 * @return the converted text, or <code>null</code> if the HTML text is
	 *         <code>null</code>
	 */
	public static String replaceNewLine(String html) {
		return _html.replaceNewLine(html);
	}

	/**
	 * Strips all content delimited by the tag out of the text.
	 *
	 * <p>
	 * If the tag appears multiple times, all occurrences (including the tag)
	 * are stripped. The tag may have attributes. In order for this method to
	 * recognize the tag, it must consist of a separate opening and closing tag.
	 * Self-closing tags remain in the result.
	 * </p>
	 *
	 * @param  text the text
	 * @param  tag the tag used for delimiting, which should only be the tag's
	 *         name (e.g. no &lt;)
	 * @return the text, without the stripped tag and its contents, or
	 *         <code>null</code> if the text is <code>null</code>
	 */
	public static String stripBetween(String text, String tag) {
		return _html.stripBetween(text, tag);
	}

	/**
	 * Strips all XML comments out of the text.
	 *
	 * @param  text the text
	 * @return the text, without the stripped XML comments, or <code>null</code>
	 *         if the text is <code>null</code>
	 */
	public static String stripComments(String text) {
		return _html.stripComments(text);
	}

	public static String stripHtml(String text) {
		return _html.stripHtml(text);
	}

	/**
	 * Encodes the text so that it's safe to use as an HTML input field value.
	 *
	 * <p>
	 * For example, the <code>&</code> character is replaced by
	 * <code>&amp;amp;</code>.
	 * </p>
	 *
	 * @param  text the text
	 * @return the encoded text that is safe to use as an HTML input field
	 *         value, or <code>null</code> if the text is <code>null</code>
	 */
	public static String toInputSafe(String text) {
		return _html.toInputSafe(text);
	}

	public static String unescape(String text) {
		return _html.unescape(text);
	}

	public static String unescapeCDATA(String text) {
		return _html.unescapeCDATA(text);
	}

	public static String wordBreak(String text, int columns) {
		return _html.wordBreak(text, columns);
	}

	public void setHtml(Html html) {
		_html = html;
	}

	private static Html _html;

}