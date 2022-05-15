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

package com.liferay.portal.search.web.internal.suggestions.display.context;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.web.internal.suggestions.display.context.builder.SuggestionsPortletDisplayContextBuilder;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Adam Brandizzi
 * @author André de Oliveira
 */
public class SuggestionsPortletDisplayContextBuilderTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		_setUpHtml();

		_setUpPortalUtil();

		_setUpDisplayContextBuilder();
	}

	@Test
	public void testGetRelatedQueriesSuggestions() {
		List<SuggestionDisplayContext> suggestionDisplayContexts =
			buildRelatedQueriesSuggestions(Arrays.asList("alpha"));

		_assertSuggestion(
			"[alpha] | q=alpha", suggestionDisplayContexts.get(0));
	}

	@Test
	public void testGetRelatedQueriesSuggestionsEmptyByDefault() {
		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		List<SuggestionDisplayContext> suggestionDisplayContexts =
			suggestionsPortletDisplayContext.getRelatedQueriesSuggestions();

		Assert.assertTrue(suggestionDisplayContexts.isEmpty());
	}

	@Test
	public void testGetRelatedQueriesSuggestionsMultiple() {
		_setUpSearchedKeywords("q", "a b");

		List<SuggestionDisplayContext> suggestionDisplayContexts =
			buildRelatedQueriesSuggestions(Arrays.asList("a C", "a b C"));

		Assert.assertEquals(
			suggestionDisplayContexts.toString(), 2,
			suggestionDisplayContexts.size());

		_assertSuggestion("a [C] | q=a+C", suggestionDisplayContexts.get(0));

		_assertSuggestion(
			"a b [C] | q=a+b+C", suggestionDisplayContexts.get(1));
	}

	@Test
	public void testGetRelatedSuggestionsWithEmptyList() {
		_setUpSearchedKeywords("q", "a b");

		List<SuggestionDisplayContext> suggestionDisplayContexts =
			buildRelatedQueriesSuggestions(Collections.emptyList());

		Assert.assertTrue(suggestionDisplayContexts.isEmpty());
	}

	@Test
	public void testGetRelatedSuggestionsWithKeywordsAsSuggestions() {
		_setUpSearchedKeywords("q", "a b");

		List<SuggestionDisplayContext> suggestionDisplayContexts =
			buildRelatedQueriesSuggestions(Arrays.asList("a b", "a b C"));

		Assert.assertEquals(
			suggestionDisplayContexts.toString(), 1,
			suggestionDisplayContexts.size());

		_assertSuggestion(
			"a b [C] | q=a+b+C", suggestionDisplayContexts.get(0));
	}

	@Test
	public void testGetRelatedSuggestionsWithNullSuggestions() {
		_setUpSearchedKeywords("q", "a b");

		List<SuggestionDisplayContext> suggestionDisplayContexts =
			buildRelatedQueriesSuggestions(Arrays.asList(null, "", "a b C"));

		Assert.assertEquals(
			suggestionDisplayContexts.toString(), 1,
			suggestionDisplayContexts.size());

		_assertSuggestion(
			"a b [C] | q=a+b+C", suggestionDisplayContexts.get(0));
	}

	@Test
	public void testGetSpellCheckSuggestion() {
		SuggestionDisplayContext suggestionDisplayContext =
			buildSpellCheckSuggestion("alpha");

		_assertSuggestion("[alpha] | q=alpha", suggestionDisplayContext);
	}

	@Test
	public void testGetSpellCheckSuggestionEqualsToKeywords() {
		_setUpSearchedKeywords("q", "a b");

		Assert.assertNull(buildSpellCheckSuggestion("a b"));
	}

	@Test
	public void testGetSpellCheckSuggestionFormatted() {
		_setUpSearchedKeywords("q", "a b");

		SuggestionDisplayContext suggestionDisplayContext =
			buildSpellCheckSuggestion("a C");

		_assertSuggestion("a [C] | q=a+C", suggestionDisplayContext);
	}

	@Test
	public void testGetSpellCheckSuggestionsNullByDefault() {
		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertNull(
			suggestionsPortletDisplayContext.getSpellCheckSuggestion());
	}

	@Test
	public void testGetSpellCheckSuggestionWithEmptySuggestion() {
		_setUpSearchedKeywords("q", "a b");

		Assert.assertNull(buildSpellCheckSuggestion(""));
	}

	@Test
	public void testGetSpellCheckSuggestionWithNullSuggestion() {
		_setUpSearchedKeywords("q", "a b");

		Assert.assertNull(buildSpellCheckSuggestion(null));
	}

	@Test
	public void testHasRelatedQueriesSuggestionsFalseByDefault() {
		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertFalse(
			suggestionsPortletDisplayContext.hasRelatedQueriesSuggestions());
	}

	@Test
	public void testHasRelatedSuggestionsFalseWithDisabledAndNonemptyList() {
		_displayContextBuilder.setRelatedQueriesSuggestions(
			Arrays.asList(RandomTestUtil.randomString()));
		_displayContextBuilder.setRelatedQueriesSuggestionsEnabled(false);

		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertFalse(
			suggestionsPortletDisplayContext.hasRelatedQueriesSuggestions());
	}

	@Test
	public void testHasRelatedSuggestionsFalseWithEnabledAndEmptyList() {
		_displayContextBuilder.setRelatedQueriesSuggestions(
			Collections.emptyList());
		_displayContextBuilder.setRelatedQueriesSuggestionsEnabled(true);

		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertFalse(
			suggestionsPortletDisplayContext.hasRelatedQueriesSuggestions());
	}

	@Test
	public void testHasRelatedSuggestionsTrueWithEnabledAndNonemptyList() {
		_displayContextBuilder.setRelatedQueriesSuggestions(
			Arrays.asList(RandomTestUtil.randomString()));
		_displayContextBuilder.setRelatedQueriesSuggestionsEnabled(true);

		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertTrue(
			suggestionsPortletDisplayContext.hasRelatedQueriesSuggestions());
	}

	@Test
	public void testHasSpellCheckSuggestionFalseByDefault() {
		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertFalse(
			suggestionsPortletDisplayContext.hasSpellCheckSuggestion());
	}

	@Test
	public void testHasSpellCheckSuggestionsFalseWithDisabledAndSuggestion() {
		_displayContextBuilder.setSpellCheckSuggestion(
			RandomTestUtil.randomString());
		_displayContextBuilder.setSpellCheckSuggestionEnabled(false);

		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertFalse(
			suggestionsPortletDisplayContext.hasSpellCheckSuggestion());
	}

	@Test
	public void testHasSpellCheckSuggestionsFalseWithEnabledAndNull() {
		_displayContextBuilder.setSpellCheckSuggestion(null);
		_displayContextBuilder.setSpellCheckSuggestionEnabled(true);

		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertFalse(
			suggestionsPortletDisplayContext.hasSpellCheckSuggestion());
	}

	@Test
	public void testHasSpellCheckSuggestionsTrueWithEnabledAndNonemptyList() {
		_displayContextBuilder.setSpellCheckSuggestion(
			RandomTestUtil.randomString());
		_displayContextBuilder.setSpellCheckSuggestionEnabled(true);

		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertTrue(
			suggestionsPortletDisplayContext.hasSpellCheckSuggestion());
	}

	@Test
	public void testIsRelatedSuggestions() {
		_displayContextBuilder.setRelatedQueriesSuggestions(
			Arrays.asList(RandomTestUtil.randomString()));
		_displayContextBuilder.setRelatedQueriesSuggestionsEnabled(true);

		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertTrue(
			suggestionsPortletDisplayContext.
				isRelatedQueriesSuggestionsEnabled());
	}

	@Test
	public void testIsRelatedSuggestionsFalseByDefault() {
		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertFalse(
			suggestionsPortletDisplayContext.
				isRelatedQueriesSuggestionsEnabled());
	}

	@Test
	public void testIsSpellCheckSuggestionEnabled() {
		_displayContextBuilder.setSpellCheckSuggestionEnabled(true);

		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertTrue(
			suggestionsPortletDisplayContext.isSpellCheckSuggestionEnabled());
	}

	@Test
	public void testIsSpellCheckSuggestionEnabledFalseByDefault() {
		SuggestionsPortletDisplayContext suggestionsPortletDisplayContext =
			_displayContextBuilder.build();

		Assert.assertFalse(
			suggestionsPortletDisplayContext.isSpellCheckSuggestionEnabled());
	}

	protected List<SuggestionDisplayContext> buildRelatedQueriesSuggestions(
		List<String> suggestions) {

		_displayContextBuilder.setRelatedQueriesSuggestions(suggestions);
		_displayContextBuilder.setRelatedQueriesSuggestionsEnabled(true);

		SuggestionsPortletDisplayContext displayContext =
			_displayContextBuilder.build();

		return displayContext.getRelatedQueriesSuggestions();
	}

	protected SuggestionDisplayContext buildSpellCheckSuggestion(
		String spellCheckSuggestion) {

		_displayContextBuilder.setSpellCheckSuggestion(spellCheckSuggestion);
		_displayContextBuilder.setSpellCheckSuggestionEnabled(true);

		SuggestionsPortletDisplayContext displayContext =
			_displayContextBuilder.build();

		return displayContext.getSpellCheckSuggestion();
	}

	@Mock
	protected Html html;

	@Mock
	protected Portal portal;

	private void _assertSuggestion(
		String expected, SuggestionDisplayContext suggestionDisplayContext) {

		String[] parts = StringUtil.split(expected, CharPool.PIPE);

		String suggestedKeywordsFormatted = _formatSimplifiedSuggestedKeywords(
			StringUtil.trim(parts[0]));

		String fullURL = _toFullURL(StringUtil.trim(parts[1]));

		Assert.assertEquals(
			suggestedKeywordsFormatted + " | " + fullURL,
			suggestionDisplayContext.getSuggestedKeywordsFormatted() + " | " +
				suggestionDisplayContext.getURL());
	}

	private String _formatKeyword(String keyword) {
		if (keyword.charAt(0) == CharPool.OPEN_BRACKET) {
			return "<span class=\"changed-keyword\">" +
				keyword.substring(1, keyword.length() - 1) + "</span>";
		}

		return "<span class=\"unchanged-keyword\">" + keyword + "</span>";
	}

	private String _formatSimplifiedSuggestedKeywords(String simplifiedQuery) {
		return Stream.of(
			StringUtil.split(simplifiedQuery, CharPool.SPACE)
		).map(
			this::_formatKeyword
		).collect(
			Collectors.joining(StringPool.SPACE)
		);
	}

	private void _setUpDisplayContextBuilder() {
		_displayContextBuilder = new SuggestionsPortletDisplayContextBuilder(
			html);

		_setUpSearchedKeywords("q", "X");
	}

	private void _setUpHtml() {
		Mockito.doAnswer(
			AdditionalAnswers.returnsFirstArg()
		).when(
			html
		).escape(
			Mockito.anyString()
		);
	}

	private void _setUpPortalUtil() {
		Mockito.doAnswer(
			invocation -> new String[] {
				invocation.getArgumentAt(0, String.class), StringPool.BLANK
			}
		).when(
			portal
		).stripURLAnchor(
			Mockito.anyString(), Mockito.anyString()
		);

		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(portal);
	}

	private void _setUpSearchedKeywords(
		String keywordsParameterName, String keywords) {

		_displayContextBuilder.setKeywords(keywords);
		_displayContextBuilder.setKeywordsParameterName(keywordsParameterName);
		_displayContextBuilder.setSearchURL(
			StringBundler.concat(
				_URL_PREFIX, keywordsParameterName, StringPool.EQUAL,
				keywords));
	}

	private String _toFullURL(String parameters) {
		return _URL_PREFIX + parameters;
	}

	private static final String _URL_PREFIX = "http://localhost:8080/?";

	private SuggestionsPortletDisplayContextBuilder _displayContextBuilder;

}