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

package com.liferay.analytics.reports.web.internal.data.provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.liferay.analytics.reports.web.internal.client.AsahFaroBackendClient;
import com.liferay.analytics.reports.web.internal.model.AcquisitionChannel;
import com.liferay.analytics.reports.web.internal.model.HistoricalMetric;
import com.liferay.analytics.reports.web.internal.model.ReferringSocialMedia;
import com.liferay.analytics.reports.web.internal.model.ReferringURL;
import com.liferay.analytics.reports.web.internal.model.TimeRange;
import com.liferay.analytics.reports.web.internal.model.TimeSpan;
import com.liferay.analytics.reports.web.internal.model.TrafficChannel;
import com.liferay.analytics.reports.web.internal.model.TrafficSource;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Http;

import java.time.format.DateTimeFormatter;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author David Arques
 */
public class AnalyticsReportsDataProvider {

	public AnalyticsReportsDataProvider(Http http) {
		if (http == null) {
			throw new IllegalArgumentException("Http is null");
		}

		_asahFaroBackendClient = new AsahFaroBackendClient(http);
	}

	public Map<String, AcquisitionChannel> getAcquisitionChannels(
			long companyId, TimeRange timeRange, String url)
		throws PortalException {

		try {
			String response = _asahFaroBackendClient.doGet(
				companyId,
				String.format(
					"api/1.0/pages/acquisition-channels?canonicalURL=" +
						"%s&endDate=%s&interval=D&startDate=%s",
					HtmlUtil.escapeURL(url),
					DateTimeFormatter.ISO_DATE.format(
						timeRange.getEndLocalDate()),
					DateTimeFormatter.ISO_DATE.format(
						timeRange.getStartLocalDate())));

			TypeFactory typeFactory = _objectMapper.getTypeFactory();

			Map<String, Long> acquisitionChannels = _objectMapper.readValue(
				response,
				typeFactory.constructMapType(
					Map.class, typeFactory.constructType(String.class),
					typeFactory.constructType(Long.class)));

			Collection<Long> values = acquisitionChannels.values();

			Stream<Long> valuesStream = values.stream();

			Double total = Double.valueOf(valuesStream.reduce(0L, Long::sum));

			Set<Map.Entry<String, Long>> entries =
				acquisitionChannels.entrySet();

			Stream<Map.Entry<String, Long>> entriesStream = entries.stream();

			return entriesStream.map(
				entry -> new AbstractMap.SimpleEntry<>(
					entry.getKey(),
					new AcquisitionChannel(
						entry.getKey(), entry.getValue(),
						(entry.getValue() / total) * 100))
			).collect(
				Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
			);
		}
		catch (Exception exception) {
			throw new PortalException(
				"Unable to get acquisition channels", exception);
		}
	}

	public List<ReferringURL> getDomainReferringURLs(
			long companyId, TimeRange timeRange, String url)
		throws PortalException {

		try {
			String response = _asahFaroBackendClient.doGet(
				companyId,
				String.format(
					"api/1.0/pages/page-referrer-hosts?canonicalURL=" +
						"%s&endDate=%s&interval=D&startDate=%s",
					HtmlUtil.escapeURL(url),
					DateTimeFormatter.ISO_DATE.format(
						timeRange.getEndLocalDate()),
					DateTimeFormatter.ISO_DATE.format(
						timeRange.getStartLocalDate())));

			TypeFactory typeFactory = _objectMapper.getTypeFactory();

			Map<String, Long> pageReferrerHosts = _objectMapper.readValue(
				response,
				typeFactory.constructMapType(
					Map.class, typeFactory.constructType(String.class),
					typeFactory.constructType(Long.class)));

			Set<Map.Entry<String, Long>> entries = pageReferrerHosts.entrySet();

			Stream<Map.Entry<String, Long>> entriesStream = entries.stream();

			return entriesStream.map(
				entry -> new ReferringURL(
					Math.toIntExact(entry.getValue()), entry.getKey())
			).collect(
				Collectors.toList()
			);
		}
		catch (Exception exception) {
			throw new PortalException(
				"Unable to get referring domains", exception);
		}
	}

	public HistoricalMetric getHistoricalReadsHistoricalMetric(
			long companyId, TimeRange timeRange, String url)
		throws PortalException {

		try {
			String response = _asahFaroBackendClient.doGet(
				companyId,
				String.format(
					"api/1.0/pages/read-counts?canonicalURL=%s&endDate=%s&" +
						"interval=D&startDate=%s",
					HtmlUtil.escapeURL(url),
					DateTimeFormatter.ISO_DATE.format(
						timeRange.getEndLocalDate()),
					DateTimeFormatter.ISO_DATE.format(
						timeRange.getStartLocalDate())));

			return _objectMapper.readValue(response, HistoricalMetric.class);
		}
		catch (Exception exception) {
			throw new PortalException(
				"Unable to get historical views", exception);
		}
	}

	public HistoricalMetric getHistoricalViewsHistoricalMetric(
			long companyId, TimeRange timeRange, String url)
		throws PortalException {

		try {
			String response = _asahFaroBackendClient.doGet(
				companyId,
				String.format(
					"api/1.0/pages/view-counts?canonicalURL=%s&endDate=%s&" +
						"interval=D&startDate=%s",
					HtmlUtil.escapeURL(url),
					DateTimeFormatter.ISO_DATE.format(
						timeRange.getEndLocalDate()),
					DateTimeFormatter.ISO_DATE.format(
						timeRange.getStartLocalDate())));

			return _objectMapper.readValue(response, HistoricalMetric.class);
		}
		catch (Exception exception) {
			throw new PortalException(
				"Unable to get historical views", exception);
		}
	}

	public List<ReferringURL> getPageReferringURLs(
			long companyId, TimeRange timeRange, String url)
		throws PortalException {

		try {
			String response = _asahFaroBackendClient.doGet(
				companyId,
				String.format(
					"api/1.0/pages/page-referrers?canonicalURL=%s&endDate=%s&" +
						"interval=D&startDate=%s",
					HtmlUtil.escapeURL(url),
					DateTimeFormatter.ISO_DATE.format(
						timeRange.getEndLocalDate()),
					DateTimeFormatter.ISO_DATE.format(
						timeRange.getStartLocalDate())));

			TypeFactory typeFactory = _objectMapper.getTypeFactory();

			Map<String, Long> pageReferrers = _objectMapper.readValue(
				response,
				typeFactory.constructMapType(
					Map.class, typeFactory.constructType(String.class),
					typeFactory.constructType(Long.class)));

			Set<Map.Entry<String, Long>> entries = pageReferrers.entrySet();

			Stream<Map.Entry<String, Long>> entriesStream = entries.stream();

			return entriesStream.map(
				entry -> new ReferringURL(
					Math.toIntExact(entry.getValue()), entry.getKey())
			).collect(
				Collectors.toList()
			);
		}
		catch (Exception exception) {
			throw new PortalException(
				"Unable to get referring pages", exception);
		}
	}

	public List<ReferringSocialMedia> getReferringSocialMediaList(
			long companyId, TimeRange timeRange, String url)
		throws PortalException {

		try {
			String response = _asahFaroBackendClient.doGet(
				companyId,
				String.format(
					"api/1.0/pages/social-page-referrers?canonicalURL=" +
						"%s&endDate=%s&interval=D&startDate=%s",
					HtmlUtil.escapeURL(url),
					DateTimeFormatter.ISO_DATE.format(
						timeRange.getEndLocalDate()),
					DateTimeFormatter.ISO_DATE.format(
						timeRange.getStartLocalDate())));

			TypeFactory typeFactory = _objectMapper.getTypeFactory();

			Map<String, Long> socialPageReferrers = _objectMapper.readValue(
				response,
				typeFactory.constructMapType(
					Map.class, typeFactory.constructType(String.class),
					typeFactory.constructType(Long.class)));

			Set<Map.Entry<String, Long>> entries =
				socialPageReferrers.entrySet();

			Stream<Map.Entry<String, Long>> entriesStream = entries.stream();

			return entriesStream.map(
				entry -> new ReferringSocialMedia(
					entry.getKey(), Math.toIntExact(entry.getValue()))
			).collect(
				Collectors.toList()
			);
		}
		catch (Exception exception) {
			throw new PortalException(
				"Unable to get referring social media", exception);
		}
	}

	public Long getTotalReads(long companyId, String url)
		throws PortalException {

		try {
			long totalReads = GetterUtil.getLong(
				_asahFaroBackendClient.doGet(
					companyId,
					"api/1.0/pages/read-count?canonicalURL=" +
						HtmlUtil.escapeURL(url)));

			return Math.max(0, totalReads - _getTodayReads(companyId, url));
		}
		catch (Exception exception) {
			throw new PortalException("Unable to get total reads", exception);
		}
	}

	public Long getTotalViews(long companyId, String url)
		throws PortalException {

		try {
			long totalViews = GetterUtil.getLong(
				_asahFaroBackendClient.doGet(
					companyId,
					"api/1.0/pages/view-count?canonicalURL=" +
						HtmlUtil.escapeURL(url)));

			return Math.max(0, totalViews - _getTodayViews(companyId, url));
		}
		catch (Exception exception) {
			throw new PortalException("Unable to get total views", exception);
		}
	}

	public Map<TrafficChannel.Type, TrafficChannel> getTrafficChannels(
			long companyId, TimeRange timeRange, String url)
		throws PortalException {

		try {
			Map<String, AcquisitionChannel> acquisitionChannels =
				getAcquisitionChannels(companyId, timeRange, url);

			Collection<AcquisitionChannel> values =
				acquisitionChannels.values();

			Stream<AcquisitionChannel> stream = values.stream();

			return stream.map(
				TrafficChannel::newInstance
			).map(
				trafficChannel -> new AbstractMap.SimpleEntry<>(
					trafficChannel.getType(), trafficChannel)
			).collect(
				Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
			);
		}
		catch (Exception exception) {
			throw new PortalException(
				"Unable to get acquisition channels", exception);
		}
	}

	public Map<String, TrafficSource> getTrafficSources(
		long companyId, String url) {

		try {
			String response = _asahFaroBackendClient.doGet(
				companyId, "api/seo/1.0/traffic-sources?url=" + url);

			TypeFactory typeFactory = _objectMapper.getTypeFactory();

			List<TrafficSource> trafficSources = _objectMapper.readValue(
				response,
				typeFactory.constructCollectionType(
					List.class, TrafficSource.class));

			Stream<TrafficSource> trafficSourcesStream =
				trafficSources.stream();

			return trafficSourcesStream.map(
				trafficSource -> new AbstractMap.SimpleEntry<>(
					trafficSource.getName(), trafficSource)
			).collect(
				Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
			);
		}
		catch (Exception exception) {
			_log.error("Unable to get traffic sources", exception);

			return Collections.emptyMap();
		}
	}

	public boolean isValidAnalyticsConnection(long companyId) {
		return _asahFaroBackendClient.isValidConnection(companyId);
	}

	private long _getTodayReads(long companyId, String url)
		throws PortalException {

		HistoricalMetric historicalMetric = getHistoricalReadsHistoricalMetric(
			companyId, TimeRange.of(TimeSpan.TODAY, 0), url);

		Double value = historicalMetric.getValue();

		return value.longValue();
	}

	private long _getTodayViews(long companyId, String url)
		throws PortalException {

		HistoricalMetric historicalMetric = getHistoricalViewsHistoricalMetric(
			companyId, TimeRange.of(TimeSpan.TODAY, 0), url);

		Double value = historicalMetric.getValue();

		return value.longValue();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AnalyticsReportsDataProvider.class);

	private static final ObjectMapper _objectMapper = new ObjectMapper() {
		{
			enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
			enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
			configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		}
	};

	private final AsahFaroBackendClient _asahFaroBackendClient;

}