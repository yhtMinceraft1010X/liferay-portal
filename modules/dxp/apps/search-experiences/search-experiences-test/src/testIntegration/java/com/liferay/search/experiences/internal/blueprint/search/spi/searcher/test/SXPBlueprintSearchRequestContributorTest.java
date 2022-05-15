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

package com.liferay.search.experiences.internal.blueprint.search.spi.searcher.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.petra.function.UnsafeRunnable;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.test.util.ConfigurationTemporarySwapper;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowThreadLocal;
import com.liferay.portal.search.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.searcher.SearchResponse;
import com.liferay.portal.search.searcher.Searcher;
import com.liferay.portal.search.spi.searcher.SearchRequestContributor;
import com.liferay.portal.search.test.util.DocumentsAssert;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.search.experiences.model.SXPBlueprint;
import com.liferay.search.experiences.service.SXPBlueprintLocalService;

import java.util.Collections;
import java.util.Objects;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

/**
 * @author Wade Cao
 * @author André de Oliveira
 */
@RunWith(Arquillian.class)
public class SXPBlueprintSearchRequestContributorTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@BeforeClass
	public static void setUpClass() throws Exception {
		WorkflowThreadLocal.setEnabled(false);
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		WorkflowThreadLocal.setEnabled(true);
	}

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_serviceContext = ServiceContextTestUtil.getServiceContext(
			_group, TestPropsValues.getUserId());

		Class<?> clazz = getClass();

		_sxpBlueprint = _sxpBlueprintLocalService.addSXPBlueprint(
			TestPropsValues.getUserId(),
			StringUtil.read(
				clazz,
				StringBundler.concat(
					"dependencies/", clazz.getSimpleName(), StringPool.PERIOD,
					testName.getMethodName(), ".json")),
			Collections.singletonMap(
				LocaleUtil.US, RandomTestUtil.randomString()),
			"", "",
			Collections.singletonMap(
				LocaleUtil.US, RandomTestUtil.randomString()),
			_serviceContext);
	}

	@Test
	public void testIPStackConfiguration() throws Exception {
		_test(
			new String[] {"diamond bar city", "walnut city"},
			() -> {
				try (ConfigurationTemporarySwapper
						configurationTemporarySwapper =
							_getConfigurationTemporarySwapper(
								"2345", "34.94.32.240", "true")) {

					ReflectionTestUtil.setFieldValue(
						HttpUtil.class, "_http",
						_getHttp(
							JSONUtil.put(
								"city", "diamond bar"
							).toString()));

					_assertSearch("[diamond bar city]", "34.94.32.240", "city");
				}
				finally {
					ReflectionTestUtil.setFieldValue(
						HttpUtil.class, "_http", _http);
				}

				try (ConfigurationTemporarySwapper
						configurationTemporarySwapper =
							_getConfigurationTemporarySwapper(
								"2345", "91.233.116.229", "true")) {

					ReflectionTestUtil.setFieldValue(
						HttpUtil.class, "_http",
						_getHttp(
							JSONUtil.put(
								"city", "walnut"
							).toString()));

					_assertSearch("[walnut city]", "91.233.116.229", "city");
				}
				finally {
					ReflectionTestUtil.setFieldValue(
						HttpUtil.class, "_http", _http);
				}
			});
	}

	@Test
	public void testQueryConfiguration() throws Exception {
		_test(
			new String[] {"alpha", "beta", "charlie"},
			() -> _assertSearch("[beta]", "", "beta"));
	}

	@Test
	public void testSortConfiguration() throws Exception {
		_test(
			new String[] {"alpha delta", "beta delta", "charlie delta"},
			() -> _assertSearch(
				"[charlie delta, beta delta, alpha delta]", "", "delta"));
	}

	@Rule
	public TestName testName = new TestName();

	private void _assertSearch(
			String expected, String ipAddress, String keywords)
		throws Exception {

		SearchResponse searchResponse = _searcher.search(
			_searchRequestContributor.contribute(
				_searchRequestBuilderFactory.builder(
				).companyId(
					TestPropsValues.getCompanyId()
				).queryString(
					keywords
				).withSearchContext(
					_searchContext -> {
						_searchContext.setAttribute(
							"search.experiences.blueprint.id",
							_sxpBlueprint.getSXPBlueprintId());
						_searchContext.setAttribute(
							"search.experiences.ip.address", ipAddress);
						_searchContext.setAttribute(
							"search.experiences.scope.group.id",
							_group.getGroupId());
						_searchContext.setUserId(_serviceContext.getUserId());
					}
				).build()));

		DocumentsAssert.assertValues(
			searchResponse.getRequestString(),
			searchResponse.getDocumentsStream(), "localized_title_en_US",
			expected);
	}

	private ConfigurationTemporarySwapper _getConfigurationTemporarySwapper(
			String apiKey, String apiURL, String enabled)
		throws Exception {

		return new ConfigurationTemporarySwapper(
			"com.liferay.search.experiences.internal.configuration." +
				"IpstackConfiguration",
			HashMapDictionaryBuilder.put(
				"apiKey", (Object)apiKey
			).put(
				"apiURL", apiURL
			).put(
				"enabled", enabled
			).build());
	}

	private Http _getHttp(String urlResponse) {
		return (Http)ProxyUtil.newProxyInstance(
			Http.class.getClassLoader(), new Class<?>[] {Http.class},
			(proxy, method, args) -> {
				if (!Objects.equals("URLtoString", method.getName()) ||
					(args.length != 1) || !(args[0] instanceof String)) {

					return method.invoke(_http, args);
				}

				return urlResponse;
			});
	}

	private void _test(
			String[] titles, UnsafeRunnable<Exception> unsafeRunnable)
		throws Exception {

		for (String title : titles) {
			JournalTestUtil.addArticle(
				_group.getGroupId(), 0,
				PortalUtil.getClassNameId(JournalArticle.class),
				HashMapBuilder.put(
					LocaleUtil.US, title
				).build(),
				null,
				HashMapBuilder.put(
					LocaleUtil.US, ""
				).build(),
				LocaleUtil.getSiteDefault(), false, true,
				ServiceContextTestUtil.getServiceContext());
		}

		unsafeRunnable.run();
	}

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private Http _http;

	@Inject
	private Searcher _searcher;

	@Inject
	private SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Inject
	private SearchRequestContributor _searchRequestContributor;

	private ServiceContext _serviceContext;

	@DeleteAfterTestRun
	private SXPBlueprint _sxpBlueprint;

	@Inject
	private SXPBlueprintLocalService _sxpBlueprintLocalService;

}