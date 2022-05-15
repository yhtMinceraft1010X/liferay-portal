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

package com.liferay.portal.search.similar.results.web.internal.builder;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.similar.results.web.spi.contributor.SimilarResultsContributor;
import com.liferay.portal.search.similar.results.web.spi.contributor.helper.RouteHelper;

import java.util.Optional;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Wade Cao
 * @author André de Oliveira
 */
@Component(service = SimilarResultsContributorsRegistry.class)
public class SimilarResultsContributorsRegistryImpl
	implements SimilarResultsContributorsRegistry {

	@Override
	public Optional<SimilarResultsRoute> detectRoute(String urlString) {
		if (Validator.isBlank(urlString)) {
			return Optional.empty();
		}

		Stream<SimilarResultsContributor> stream =
			_similarResultsContributorsHolder.stream();

		return stream.map(
			similarResultsContributor -> _detectRoute(
				similarResultsContributor, urlString)
		).filter(
			Optional::isPresent
		).map(
			Optional::get
		).findFirst();
	}

	@Reference(unbind = "-")
	public void setSimilarResultsContributorsHolder(
		SimilarResultsContributorsHolder similarResultsContributorsHolder) {

		_similarResultsContributorsHolder = similarResultsContributorsHolder;
	}

	private Optional<SimilarResultsRoute> _detectRoute(
		SimilarResultsContributor similarResultsContributor, String urlString) {

		RouteBuilderImpl routeBuilderImpl = new RouteBuilderImpl();

		RouteHelper routeHelper = () -> urlString;

		try {
			similarResultsContributor.detectRoute(
				routeBuilderImpl, routeHelper);
		}
		catch (RuntimeException runtimeException) {
			if (_log.isDebugEnabled()) {
				_log.debug(runtimeException);
			}

			return Optional.empty();
		}

		if (routeBuilderImpl.hasNoAttributes()) {
			return Optional.empty();
		}

		routeBuilderImpl.contributor(similarResultsContributor);

		return Optional.of(routeBuilderImpl.build());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SimilarResultsContributorsRegistryImpl.class);

	private SimilarResultsContributorsHolder _similarResultsContributorsHolder;

}