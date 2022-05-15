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

package com.liferay.source.formatter.check;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.tools.ToolsUtil;
import com.liferay.source.formatter.check.util.JSPSourceUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hugo Huijser
 */
public class JSPStylingCheck extends BaseStylingCheck {

	@Override
	protected String doProcess(
		String fileName, String absolutePath, String content) {

		content = _combineJavaSourceBlocks(fileName, content);

		content = _formatLineBreak(fileName, content);

		content = _fixEmptyJavaSourceTag(content);

		content = _fixIncorrectBacktick(content);

		content = _fixIncorrectClosingTag(content);

		content = _fixIncorrectSingleLineJavaSource(content);

		content = StringUtil.replace(
			content,
			new String[] {
				"alert('<%= LanguageUtil.", "alert(\"<%= LanguageUtil.",
				"confirm('<%= LanguageUtil.", "confirm(\"<%= LanguageUtil.",
				";;\n"
			},
			new String[] {
				"alert('<%= UnicodeLanguageUtil.",
				"alert(\"<%= UnicodeLanguageUtil.",
				"confirm('<%= UnicodeLanguageUtil.",
				"confirm(\"<%= UnicodeLanguageUtil.", ";\n"
			});

		content = content.replaceAll("'<%= (\"((?!\").)*?\") %>'", "$1");

		content = content.replaceAll(
			"((['\"])<%= ((?<!%>).)*?)\\\\(\".+?)\\\\(\".*?%>\\2)", "$1$4$5");

		Matcher matcher = _portletNamespacePattern.matcher(content);

		while (matcher.find()) {
			String s = matcher.group(2);

			if (s.endsWith(StringPool.CLOSE_PARENTHESIS) &&
				(getLevel(s) == 0)) {

				return StringUtil.insert(
					content, StringPool.SEMICOLON, matcher.end() - 1);
			}
		}

		return formatStyling(content);
	}

	private String _combineJavaSourceBlocks(String fileName, String content) {
		int x = -1;

		while (true) {
			x = content.indexOf("<%!", x + 1);

			if ((x == -1) || !ToolsUtil.isInsideQuotes(content, x)) {
				break;
			}
		}

		int y = x;

		while (true) {
			y = content.indexOf("<%\n", y + 1);

			if ((y == -1) || !ToolsUtil.isInsideQuotes(content, y)) {
				break;
			}
		}

		if ((x != -1) && (y != -1) && (x < y)) {
			addMessage(
				fileName, "'<%!...%>' block should come after <%...%> blcok",
				getLineNumber(content, x));

			return content;
		}

		y = x;

		while (true) {
			y = content.indexOf("<%!", y + 1);

			if (y == -1) {
				break;
			}

			if (!ToolsUtil.isInsideQuotes(content, y)) {
				addMessage(
					fileName,
					StringBundler.concat(
						"Combine <%!...%> blocks at line '",
						getLineNumber(content, x), "' and '",
						getLineNumber(content, y), "'"));

				return content;
			}
		}

		Matcher matcher = _adjacentJavaBlocksPattern.matcher(content);

		if (matcher.find()) {
			return StringUtil.replaceFirst(
				content, matcher.group(), "\n\n", matcher.start() - 1);
		}

		return content;
	}

	private String _fixEmptyJavaSourceTag(String content) {
		Matcher matcher = _emptyJavaSourceTagPattern.matcher(content);

		if (matcher.find()) {
			return StringUtil.removeSubstring(content, matcher.group());
		}

		return content;
	}

	private String _fixIncorrectBacktick(String content) {
		Matcher matcher = _incorrectBacktickPattern.matcher(content);

		if (matcher.find() &&
			JSPSourceUtil.isJSSource(content, matcher.start())) {

			return StringUtil.replaceFirst(
				content, matcher.group(), "'" + matcher.group(1) + "'",
				matcher.start());
		}

		return content;
	}

	private String _fixIncorrectClosingTag(String content) {
		Matcher matcher = _incorrectClosingTagPattern.matcher(content);

		if (matcher.find()) {
			return StringUtil.replaceFirst(
				content, " />\n", "\n" + matcher.group(1) + "/>\n",
				matcher.end(1));
		}

		return content;
	}

	private String _fixIncorrectSingleLineJavaSource(String content) {
		Matcher matcher = _incorrectSingleLineJavaSourcePattern.matcher(
			content);

		while (matcher.find()) {
			String javaSource = matcher.group(3);

			if (javaSource.contains("<%")) {
				continue;
			}

			String indent = matcher.group(1);

			StringBundler sb = new StringBundler(6);

			sb.append("<%\n");
			sb.append(indent);
			sb.append(StringUtil.trim(javaSource));
			sb.append("\n");
			sb.append(indent);
			sb.append("%>");

			return StringUtil.replaceFirst(
				content, matcher.group(2), sb.toString(), matcher.start());
		}

		return content;
	}

	private String _formatLineBreak(String fileName, String content) {
		Matcher matcher = _incorrectLineBreakPattern1.matcher(content);

		while (matcher.find()) {
			if (!JSPSourceUtil.isJSSource(content, matcher.start(1))) {
				addMessage(
					fileName, "There should be a line break after '}'",
					getLineNumber(content, matcher.start(1)));
			}
		}

		matcher = _incorrectLineBreakPattern2.matcher(content);

		while (matcher.find()) {
			if (isJavaSource(content, matcher.start())) {
				return StringUtil.replaceFirst(
					content, matcher.group(1), StringPool.SPACE,
					matcher.start());
			}
		}

		matcher = _incorrectLineBreakPattern3.matcher(content);

		while (matcher.find()) {
			addMessage(
				fileName, "There should be a line break after '<%='",
				getLineNumber(content, matcher.start()));
		}

		matcher = _incorrectLineBreakPattern4.matcher(content);

		return matcher.replaceAll("$1\n\t$2$4\n$2$5");
	}

	private static final Pattern _adjacentJavaBlocksPattern = Pattern.compile(
		"\n\t*%>\n+\t*<%\n");
	private static final Pattern _emptyJavaSourceTagPattern = Pattern.compile(
		"\n\t*<%\\!?\n+\t*%>(\n|\\Z)");
	private static final Pattern _incorrectBacktickPattern = Pattern.compile(
		"`((.(?!\\{.+\\}))*)`");
	private static final Pattern _incorrectClosingTagPattern = Pattern.compile(
		"\n(\t*)\t((?!<\\w).)* />\n");
	private static final Pattern _incorrectLineBreakPattern1 = Pattern.compile(
		"[\n\t]\\} ?(catch|else|finally) ");
	private static final Pattern _incorrectLineBreakPattern2 = Pattern.compile(
		"=(\n\\s*).*;\n");
	private static final Pattern _incorrectLineBreakPattern3 = Pattern.compile(
		"<%= *\\S((?!%>).)*\n");
	private static final Pattern _incorrectLineBreakPattern4 = Pattern.compile(
		"(\n(\t*)<(\\w+)>)(<\\w+>.*)(</\\3>\n)");
	private static final Pattern _incorrectSingleLineJavaSourcePattern =
		Pattern.compile("(\t*)(<% (.*) %>)\n");
	private static final Pattern _portletNamespacePattern = Pattern.compile(
		"=([\"'])<portlet:namespace />(\\w+\\(.*?)\\1");

}