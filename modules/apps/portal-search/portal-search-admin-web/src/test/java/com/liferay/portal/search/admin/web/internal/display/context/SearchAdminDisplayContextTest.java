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

package com.liferay.portal.search.admin.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemList;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.language.LanguageImpl;
import com.liferay.portal.search.admin.web.internal.display.context.builder.SearchAdminDisplayContextBuilder;
import com.liferay.portal.search.index.IndexInformation;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portletmvc4spring.test.mock.web.portlet.MockRenderRequest;
import com.liferay.portletmvc4spring.test.mock.web.portlet.MockRenderResponse;

import javax.portlet.RenderRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * @author Adam Brandizzi
 */
public class SearchAdminDisplayContextTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		setUpIndexInformation();
		_setUpLanguage();
		setUpPortalUtil();
	}

	@Test
	public void testGetNavigationItemListWithIndexInformation() {
		SearchAdminDisplayContextBuilder searchAdminDisplayContextBuilder =
			new SearchAdminDisplayContextBuilder(
				_language, _portal, new MockRenderRequest(),
				new MockRenderResponse());

		searchAdminDisplayContextBuilder.setIndexInformation(
			Mockito.mock(IndexInformation.class));

		SearchAdminDisplayContext searchAdminDisplayContext =
			searchAdminDisplayContextBuilder.build();

		NavigationItemList navigationItemList =
			searchAdminDisplayContext.getNavigationItemList();

		Assert.assertEquals(
			navigationItemList.toString(), 3, navigationItemList.size());
	}

	@Test
	public void testGetNavigationItemListWithoutIndexInformation() {
		SearchAdminDisplayContextBuilder searchAdminDisplayContextBuilder =
			new SearchAdminDisplayContextBuilder(
				_language, _portal, new MockRenderRequest(),
				new MockRenderResponse());

		searchAdminDisplayContextBuilder.setIndexInformation(null);

		SearchAdminDisplayContext searchAdminDisplayContext =
			searchAdminDisplayContextBuilder.build();

		NavigationItemList navigationItemList =
			searchAdminDisplayContext.getNavigationItemList();

		Assert.assertEquals(
			navigationItemList.toString(), 2, navigationItemList.size());
	}

	@Test
	public void testGetTabConnections() {
		SearchAdminDisplayContextBuilder searchAdminDisplayContextBuilder =
			new SearchAdminDisplayContextBuilder(
				_language, _portal,
				_getRenderRequestWithSelectedTab("connections"),
				new MockRenderResponse());

		searchAdminDisplayContextBuilder.setIndexInformation(
			Mockito.mock(IndexInformation.class));

		SearchAdminDisplayContext searchAdminDisplayContext =
			searchAdminDisplayContextBuilder.build();

		Assert.assertEquals(
			"connections", searchAdminDisplayContext.getSelectedTab());
	}

	@Test
	public void testGetTabDefault() {
		SearchAdminDisplayContextBuilder searchAdminDisplayContextBuilder =
			new SearchAdminDisplayContextBuilder(
				_language, _portal, new MockRenderRequest(),
				new MockRenderResponse());

		searchAdminDisplayContextBuilder.setIndexInformation(
			Mockito.mock(IndexInformation.class));

		SearchAdminDisplayContext searchAdminDisplayContext =
			searchAdminDisplayContextBuilder.build();

		Assert.assertEquals(
			"connections", searchAdminDisplayContext.getSelectedTab());
	}

	@Test
	public void testGetTabFieldMappings() {
		SearchAdminDisplayContextBuilder searchAdminDisplayContextBuilder =
			new SearchAdminDisplayContextBuilder(
				_language, _portal,
				_getRenderRequestWithSelectedTab("field-mappings"),
				new MockRenderResponse());

		searchAdminDisplayContextBuilder.setIndexInformation(
			Mockito.mock(IndexInformation.class));

		SearchAdminDisplayContext searchAdminDisplayContext =
			searchAdminDisplayContextBuilder.build();

		Assert.assertEquals(
			"field-mappings", searchAdminDisplayContext.getSelectedTab());
	}

	@Test
	public void testGetTabFieldMappingsNoIndexInformation() {
		SearchAdminDisplayContextBuilder searchAdminDisplayContextBuilder =
			new SearchAdminDisplayContextBuilder(
				_language, _portal,
				_getRenderRequestWithSelectedTab("field-mappings"),
				new MockRenderResponse());

		searchAdminDisplayContextBuilder.setIndexInformation(null);

		SearchAdminDisplayContext searchAdminDisplayContext =
			searchAdminDisplayContextBuilder.build();

		Assert.assertEquals(
			"connections", searchAdminDisplayContext.getSelectedTab());
	}

	@Test
	public void testGetTabIndexActions() {
		SearchAdminDisplayContextBuilder searchAdminDisplayContextBuilder =
			new SearchAdminDisplayContextBuilder(
				_language, _portal,
				_getRenderRequestWithSelectedTab("index-actions"),
				new MockRenderResponse());

		searchAdminDisplayContextBuilder.setIndexInformation(
			Mockito.mock(IndexInformation.class));

		SearchAdminDisplayContext searchAdminDisplayContext =
			searchAdminDisplayContextBuilder.build();

		Assert.assertEquals(
			"index-actions", searchAdminDisplayContext.getSelectedTab());
	}

	@Test
	public void testGetTabUnavailable() {
		SearchAdminDisplayContextBuilder searchAdminDisplayContextBuilder =
			new SearchAdminDisplayContextBuilder(
				_language, _portal,
				_getRenderRequestWithSelectedTab(RandomTestUtil.randomString()),
				new MockRenderResponse());

		searchAdminDisplayContextBuilder.setIndexInformation(
			Mockito.mock(IndexInformation.class));

		SearchAdminDisplayContext searchAdminDisplayContext =
			searchAdminDisplayContextBuilder.build();

		Assert.assertEquals(
			"connections", searchAdminDisplayContext.getSelectedTab());
	}

	protected void setUpIndexInformation() {
		indexInformation = Mockito.mock(IndexInformation.class);

		Mockito.when(
			indexInformation.getIndexNames()
		).thenReturn(
			new String[] {"index1", "index2"}
		);

		Mockito.when(
			indexInformation.getCompanyIndexName(Matchers.anyLong())
		).thenAnswer(
			invocation -> "index" + invocation.getArguments()[0]
		);
	}

	protected void setUpPortalUtil() {
		_portal = Mockito.mock(Portal.class);

		Mockito.doAnswer(
			invocation -> new String[] {
				invocation.getArgumentAt(0, String.class), StringPool.BLANK
			}
		).when(
			_portal
		).stripURLAnchor(
			Mockito.anyString(), Mockito.anyString()
		);

		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(_portal);
	}

	protected IndexInformation indexInformation;

	private RenderRequest _getRenderRequestWithSelectedTab(String selectedTab) {
		MockRenderRequest mockRenderRequest = new MockRenderRequest();

		mockRenderRequest.setParameter("tabs1", selectedTab);

		return mockRenderRequest;
	}

	private void _setUpLanguage() {
		_language = new LanguageImpl();
	}

	private Language _language;
	private Portal _portal;

}