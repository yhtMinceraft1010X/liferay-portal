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

package com.liferay.analytics.reports.web.internal.portlet.action;

import com.liferay.analytics.reports.web.internal.constants.AnalyticsReportsPortletKeys;
import com.liferay.analytics.reports.web.internal.data.provider.AnalyticsReportsDataProvider;
import com.liferay.analytics.reports.web.internal.model.TimeRange;
import com.liferay.analytics.reports.web.internal.model.TimeSpan;
import com.liferay.analytics.reports.web.internal.model.TrafficChannel;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Cristina González
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + AnalyticsReportsPortletKeys.ANALYTICS_REPORTS,
		"mvc.command.name=/analytics_reports/get_traffic_sources"
	},
	service = MVCResourceCommand.class
)
public class GetTrafficSourcesMVCResourceCommand
	extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			themeDisplay.getLocale(), getClass());

		try {
			AnalyticsReportsDataProvider analyticsReportsDataProvider =
				new AnalyticsReportsDataProvider(_http);
			String canonicalURL = ParamUtil.getString(
				resourceRequest, "canonicalURL");

			String timeSpanKey = ParamUtil.getString(
				resourceRequest, "timeSpanKey", TimeSpan.defaultTimeSpanKey());

			TimeSpan timeSpan = TimeSpan.of(timeSpanKey);

			int timeSpanOffset = ParamUtil.getInteger(
				resourceRequest, "timeSpanOffset");

			JSONObject jsonObject = JSONUtil.put(
				"trafficSources",
				_getTrafficSourcesJSONArray(
					analyticsReportsDataProvider, canonicalURL,
					themeDisplay.getCompanyId(),
					_portal.getLiferayPortletRequest(resourceRequest),
					_portal.getLiferayPortletResponse(resourceResponse),
					timeSpan.toTimeRange(timeSpanOffset), resourceBundle));

			JSONPortletResponseUtil.writeJSON(
				resourceRequest, resourceResponse, jsonObject);
		}
		catch (Exception exception) {
			_log.error(exception);

			JSONPortletResponseUtil.writeJSON(
				resourceRequest, resourceResponse,
				JSONUtil.put(
					"error",
					ResourceBundleUtil.getString(
						resourceBundle, "an-unexpected-error-occurred")));
		}
	}

	private List<TrafficChannel> _getTrafficChannels(
		AnalyticsReportsDataProvider analyticsReportsDataProvider,
		String canonicalURL, long companyId, TimeRange timeRange) {

		Map<TrafficChannel.Type, TrafficChannel> emptyMap = HashMapBuilder.put(
			TrafficChannel.Type.DIRECT,
			TrafficChannel.newInstance(0, 0.0, TrafficChannel.Type.DIRECT)
		).put(
			TrafficChannel.Type.ORGANIC,
			TrafficChannel.newInstance(0, 0.0, TrafficChannel.Type.ORGANIC)
		).put(
			TrafficChannel.Type.PAID,
			TrafficChannel.newInstance(0, 0.0, TrafficChannel.Type.PAID)
		).put(
			TrafficChannel.Type.REFERRAL,
			TrafficChannel.newInstance(0, 0.0, TrafficChannel.Type.REFERRAL)
		).put(
			TrafficChannel.Type.SOCIAL,
			TrafficChannel.newInstance(0, 0.0, TrafficChannel.Type.SOCIAL)
		).build();

		if (!analyticsReportsDataProvider.isValidAnalyticsConnection(
				companyId)) {

			PortalException portalException = new PortalException(
				"Invalid Analytics Connection");

			return Arrays.asList(
				TrafficChannel.newInstance(
					portalException, TrafficChannel.Type.DIRECT),
				TrafficChannel.newInstance(
					portalException, TrafficChannel.Type.ORGANIC),
				TrafficChannel.newInstance(
					portalException, TrafficChannel.Type.PAID),
				TrafficChannel.newInstance(
					portalException, TrafficChannel.Type.REFERRAL),
				TrafficChannel.newInstance(
					portalException, TrafficChannel.Type.SOCIAL));
		}

		try {
			Map<TrafficChannel.Type, TrafficChannel> trafficChannels =
				analyticsReportsDataProvider.getTrafficChannels(
					companyId, timeRange, canonicalURL);

			emptyMap.forEach(
				(type, trafficChannel) -> trafficChannels.merge(
					type, trafficChannel,
					(trafficChannel1, trafficChannel2) -> trafficChannel1));

			return new ArrayList<>(trafficChannels.values());
		}
		catch (PortalException portalException) {
			_log.error(portalException);

			return Arrays.asList(
				TrafficChannel.newInstance(
					portalException, TrafficChannel.Type.DIRECT),
				TrafficChannel.newInstance(
					portalException, TrafficChannel.Type.ORGANIC),
				TrafficChannel.newInstance(
					portalException, TrafficChannel.Type.PAID),
				TrafficChannel.newInstance(
					portalException, TrafficChannel.Type.REFERRAL),
				TrafficChannel.newInstance(
					portalException, TrafficChannel.Type.SOCIAL));
		}
	}

	private JSONArray _getTrafficSourcesJSONArray(
		AnalyticsReportsDataProvider analyticsReportsDataProvider,
		String canonicalURL, long companyId,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse, TimeRange timeRange,
		ResourceBundle resourceBundle) {

		List<TrafficChannel> trafficChannels = _getTrafficChannels(
			analyticsReportsDataProvider, canonicalURL, companyId, timeRange);

		Stream<TrafficChannel> stream = trafficChannels.stream();

		Comparator<TrafficChannel> comparator = Comparator.comparing(
			TrafficChannel::getTrafficShare);

		return JSONUtil.putAll(
			stream.sorted(
				comparator.reversed()
			).map(
				trafficChannel -> trafficChannel.toJSONObject(
					liferayPortletRequest, liferayPortletResponse,
					resourceBundle)
			).map(
				jsonObject -> {
					if (GetterUtil.getBoolean(
							_props.get("feature.flag.LPS-149255")) &&
						(Objects.equals(
							jsonObject.get("name"),
							TrafficChannel.Type.ORGANIC.toString()) ||
						 Objects.equals(
							 jsonObject.get("name"),
							 TrafficChannel.Type.PAID.toString()))) {

						JSONObject filteredJSONObject =
							JSONFactoryUtil.createJSONObject();

						for (String key : jsonObject.keySet()) {
							if (!key.equals("endpointURL")) {
								filteredJSONObject.put(
									key, jsonObject.get(key));
							}
						}

						return filteredJSONObject;
					}

					return jsonObject;
				}
			).toArray());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		GetTrafficSourcesMVCResourceCommand.class);

	@Reference
	private Http _http;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference
	private Props _props;

}