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

package com.liferay.segments.experiment.web.internal.display.context;

import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.segments.constants.SegmentsExperimentConstants;
import com.liferay.segments.constants.SegmentsPortletKeys;
import com.liferay.segments.experiment.web.internal.configuration.SegmentsExperimentConfiguration;
import com.liferay.segments.experiment.web.internal.util.SegmentsExperimentUtil;
import com.liferay.segments.experiment.web.internal.util.comparator.SegmentsExperimentModifiedDateComparator;
import com.liferay.segments.manager.SegmentsExperienceManager;
import com.liferay.segments.model.SegmentsExperience;
import com.liferay.segments.model.SegmentsExperiment;
import com.liferay.segments.model.SegmentsExperimentRel;
import com.liferay.segments.service.SegmentsExperienceService;
import com.liferay.segments.service.SegmentsExperimentRelService;
import com.liferay.segments.service.SegmentsExperimentService;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eduardo García
 * @author Sarai Díaz
 */
public class SegmentsExperimentDisplayContext {

	public SegmentsExperimentDisplayContext(
		HttpServletRequest httpServletRequest,
		LayoutLocalService layoutLocalService, Portal portal,
		RenderRequest renderRequest, RenderResponse renderResponse,
		SegmentsExperienceService segmentsExperienceService,
		SegmentsExperimentConfiguration segmentsExperimentConfiguration,
		SegmentsExperienceManager segmentsExperienceManager,
		SegmentsExperimentRelService segmentsExperimentRelService,
		SegmentsExperimentService segmentsExperimentService) {

		_httpServletRequest = httpServletRequest;
		_layoutLocalService = layoutLocalService;
		_portal = portal;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_segmentsExperienceService = segmentsExperienceService;
		_segmentsExperimentConfiguration = segmentsExperimentConfiguration;
		_segmentsExperienceManager = segmentsExperienceManager;
		_segmentsExperimentRelService = segmentsExperimentRelService;
		_segmentsExperimentService = segmentsExperimentService;

		_themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public Map<String, Object> getData() throws Exception {
		if (_data != null) {
			return _data;
		}

		_data = HashMapBuilder.<String, Object>put(
			"context", _getContext()
		).put(
			"props", _getProps()
		).build();

		return _data;
	}

	public String getLiferayAnalyticsURL(long companyId) {
		return PrefsPropsUtil.getString(companyId, "liferayAnalyticsURL");
	}

	private Optional<SegmentsExperiment> _getActiveSegmentsExperimentOptional(
			long segmentsExperienceId)
		throws Exception {

		Layout layout = _themeDisplay.getLayout();

		return Optional.ofNullable(
			_segmentsExperimentService.fetchSegmentsExperiment(
				segmentsExperienceId, _portal.getClassNameId(Layout.class),
				layout.getPlid(),
				SegmentsExperimentConstants.Status.getExclusiveStatusValues()));
	}

	private JSONObject _getAnalyticsDataJSONObject(
		long companyId, long groupId) {

		return JSONUtil.put(
			"cloudTrialURL", SegmentsExperimentUtil.ANALYTICS_CLOUD_TRIAL_URL
		).put(
			"isConnected",
			SegmentsExperimentUtil.isAnalyticsConnected(companyId)
		).put(
			"isSynced",
			() -> SegmentsExperimentUtil.isAnalyticsSynced(companyId, groupId)
		).put(
			"url",
			() -> PrefsPropsUtil.getString(companyId, "liferayAnalyticsURL")
		);
	}

	private String _getCalculateSegmentsExperimentEstimatedDurationURL() {
		return _getSegmentsExperimentActionURL(
			"/calculate_segments_experiment_estimated_duration");
	}

	private String _getContentPageEditorActionURL(String action) {
		return HttpComponentsUtil.addParameter(
			PortletURLBuilder.createActionURL(
				_portal.getLiferayPortletResponse(_renderResponse),
				ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET
			).setActionName(
				action
			).buildString(),
			"p_l_mode", Constants.EDIT);
	}

	private String _getContentPageEditorPortletNamespace() {
		return _portal.getPortletNamespace(
			ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET);
	}

	private Map<String, Object> _getContext() throws Exception {
		return HashMapBuilder.<String, Object>put(
			"contentPageEditorNamespace",
			_getContentPageEditorPortletNamespace()
		).put(
			"endpoints", _getEndpoints()
		).put(
			"imagesPath", _getImagesPath()
		).put(
			"namespace", _getSegmentsExperimentPortletNamespace()
		).put(
			"page", _getPage()
		).build();
	}

	private String _getCreateSegmentsExperimentURL() {
		return _getSegmentsExperimentActionURL(
			"/segments_experiment/add_segments_experiment");
	}

	private String _getCreateSegmentsVariantURL() {
		return _getContentPageEditorActionURL(
			"/layout_content_page_editor/add_segments_experience");
	}

	private String _getDeleteSegmentsExperimentURL() {
		return _getSegmentsExperimentActionURL(
			"/segments_experiment/delete_segments_experiment");
	}

	private String _getDeleteSegmentsVariantURL() {
		return _getSegmentsExperimentActionURL(
			"/segments_experiment/delete_segments_experiment_rel");
	}

	private String _getEditSegmentsExperimentStatusURL() {
		return _getSegmentsExperimentActionURL(
			"/segments_experiment/edit_segments_experiment_status");
	}

	private String _getEditSegmentsExperimentURL() {
		return _getSegmentsExperimentActionURL(
			"/segments_experiment/edit_segments_experiment");
	}

	private String _getEditSegmentsVariantLayoutURL() throws Exception {
		Layout draftLayout = _layoutLocalService.fetchDraftLayout(
			_themeDisplay.getPlid());

		if (draftLayout == null) {
			return StringPool.BLANK;
		}

		String layoutFullURL = PortalUtil.getLayoutFullURL(
			draftLayout, _themeDisplay);

		String layoutURL = _portal.getLayoutURL(_themeDisplay);

		long segmentsExperienceId = _getSegmentsExperienceId();

		if (segmentsExperienceId != -1) {
			layoutURL = HttpComponentsUtil.setParameter(
				layoutURL, "segmentsExperienceId", segmentsExperienceId);
		}

		layoutFullURL = HttpComponentsUtil.setParameter(
			layoutFullURL, "p_l_back_url", layoutURL);

		layoutFullURL = HttpComponentsUtil.setParameter(
			layoutFullURL, "p_l_mode", Constants.EDIT);
		layoutFullURL = HttpComponentsUtil.setParameter(
			layoutFullURL, "redirect", layoutFullURL);

		return layoutFullURL;
	}

	private String _getEditSegmentsVariantURL() {
		return _getSegmentsExperimentActionURL(
			"/segments_experiment/edit_segments_experiment_rel");
	}

	private Map<String, Object> _getEndpoints() throws Exception {
		return HashMapBuilder.<String, Object>put(
			"calculateSegmentsExperimentEstimatedDurationURL",
			_getCalculateSegmentsExperimentEstimatedDurationURL()
		).put(
			"createSegmentsExperimentURL", _getCreateSegmentsExperimentURL()
		).put(
			"createSegmentsVariantURL", _getCreateSegmentsVariantURL()
		).put(
			"deleteSegmentsExperimentURL", _getDeleteSegmentsExperimentURL()
		).put(
			"deleteSegmentsVariantURL", _getDeleteSegmentsVariantURL()
		).put(
			"editSegmentsExperimentStatusURL",
			_getEditSegmentsExperimentStatusURL()
		).put(
			"editSegmentsExperimentURL", _getEditSegmentsExperimentURL()
		).put(
			"editSegmentsVariantLayoutURL", _getEditSegmentsVariantLayoutURL()
		).put(
			"editSegmentsVariantURL", _getEditSegmentsVariantURL()
		).put(
			"runSegmentsExperimentURL", _getRunSegmentsExperimenttURL()
		).build();
	}

	private JSONArray _getHistorySegmentsExperimentsJSONArray(Locale locale)
		throws Exception {

		Layout layout = _themeDisplay.getLayout();

		List<SegmentsExperiment> segmentsExperiments =
			_segmentsExperimentService.getSegmentsExperiments(
				_getSegmentsExperienceId(),
				_portal.getClassNameId(Layout.class), layout.getPlid(),
				SegmentsExperimentConstants.Status.
					getNonexclusiveStatusValues(),
				new SegmentsExperimentModifiedDateComparator());

		JSONArray segmentsExperimentsJSONArray =
			JSONFactoryUtil.createJSONArray();

		if (ListUtil.isEmpty(segmentsExperiments)) {
			return segmentsExperimentsJSONArray;
		}

		for (SegmentsExperiment segmentsExperiment : segmentsExperiments) {
			segmentsExperimentsJSONArray.put(
				SegmentsExperimentUtil.toSegmentsExperimentJSONObject(
					locale, segmentsExperiment));
		}

		return segmentsExperimentsJSONArray;
	}

	private String _getImagesPath() {
		return PortalUtil.getPathContext(_httpServletRequest) + "/images";
	}

	private Map<String, Object> _getPage() {
		return HashMapBuilder.<String, Object>put(
			"classNameId", PortalUtil.getClassNameId(Layout.class.getName())
		).put(
			"classPK", _themeDisplay.getPlid()
		).put(
			"type",
			() -> {
				Layout layout = _themeDisplay.getLayout();

				return layout.getType();
			}
		).build();
	}

	private Map<String, Object> _getProps() throws Exception {
		Locale locale = _themeDisplay.getLocale();

		return HashMapBuilder.<String, Object>put(
			"analyticsData",
			_getAnalyticsDataJSONObject(
				_themeDisplay.getCompanyId(), _themeDisplay.getScopeGroupId())
		).put(
			"hideSegmentsExperimentPanelURL",
			PortletURLBuilder.createActionURL(
				_renderResponse
			).setActionName(
				"/segments_experiment/hide_segments_experiment_panel"
			).setRedirect(
				() -> {
					String redirect = ParamUtil.getString(
						_renderRequest, "redirect");

					if (Validator.isNotNull(redirect)) {
						return redirect;
					}

					return _themeDisplay.getLayoutFriendlyURL(
						_themeDisplay.getLayout());
				}
			).buildString()
		).put(
			"historySegmentsExperiments",
			_getHistorySegmentsExperimentsJSONArray(locale)
		).put(
			"initialSegmentsVariants",
			_getSegmentsExperimentRelsJSONArray(locale)
		).put(
			"pathToAssets", _portal.getPathContext(_renderRequest)
		).put(
			"segmentsExperiences", _getSegmentsExperiencesJSONArray(locale)
		).put(
			"segmentsExperiment", _getSegmentsExperimentJSONObject(locale)
		).put(
			"segmentsExperimentGoals",
			_getSegmentsExperimentGoalsJSONArray(locale)
		).put(
			"selectedSegmentsExperienceId", _getSelectedSegmentsExperienceId()
		).put(
			"winnerSegmentsVariantId", _getWinnerSegmentsExperienceId()
		).build();
	}

	private String _getRunSegmentsExperimenttURL() {
		return _getSegmentsExperimentActionURL(
			"/segments_experiment/run_segments_experiment");
	}

	private long _getSegmentsExperienceId() throws Exception {
		SegmentsExperiment segmentsExperiment = _getSegmentsExperiment();

		if (segmentsExperiment != null) {
			return segmentsExperiment.getSegmentsExperienceId();
		}

		return _getSelectedSegmentsExperienceId();
	}

	private JSONArray _getSegmentsExperiencesJSONArray(Locale locale)
		throws Exception {

		List<SegmentsExperience> segmentsExperiences =
			_segmentsExperienceService.getSegmentsExperiences(
				_themeDisplay.getScopeGroupId(),
				_portal.getClassNameId(Layout.class), _themeDisplay.getPlid(),
				true);

		JSONArray segmentsExperiencesJSONArray =
			JSONFactoryUtil.createJSONArray();

		for (SegmentsExperience segmentsExperience : segmentsExperiences) {
			segmentsExperiencesJSONArray.put(
				JSONUtil.put(
					"name", segmentsExperience.getName(locale)
				).put(
					"segmentsExperienceId",
					String.valueOf(segmentsExperience.getSegmentsExperienceId())
				).put(
					"segmentsExperiment",
					SegmentsExperimentUtil.toSegmentsExperimentJSONObject(
						locale,
						_getActiveSegmentsExperimentOptional(
							segmentsExperience.getSegmentsExperienceId()
						).orElse(
							null
						))
				));
		}

		return segmentsExperiencesJSONArray;
	}

	private SegmentsExperiment _getSegmentsExperiment() throws Exception {
		if (_segmentsExperiment != null) {
			return _segmentsExperiment;
		}

		_segmentsExperiment = _getActiveSegmentsExperimentOptional(
			_getSelectedSegmentsExperienceId()
		).orElse(
			null
		);

		return _segmentsExperiment;
	}

	private String _getSegmentsExperimentActionURL(String action) {
		return HttpComponentsUtil.addParameter(
			PortletURLBuilder.createActionURL(
				_renderResponse
			).setActionName(
				action
			).buildString(),
			"p_l_mode", Constants.VIEW);
	}

	private JSONArray _getSegmentsExperimentGoalsJSONArray(Locale locale) {
		JSONArray segmentsExperimentGoalsJSONArray =
			JSONFactoryUtil.createJSONArray();

		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		String[] goalsEnabled = _segmentsExperimentConfiguration.goalsEnabled();

		Stream<SegmentsExperimentConstants.Goal> stream = Arrays.stream(
			SegmentsExperimentConstants.Goal.values());

		stream.filter(
			goal -> ArrayUtil.contains(goalsEnabled, goal.name())
		).forEach(
			goal -> segmentsExperimentGoalsJSONArray.put(
				JSONUtil.put(
					"label", LanguageUtil.get(resourceBundle, goal.getLabel())
				).put(
					"value", goal.getLabel()
				))
		);

		return segmentsExperimentGoalsJSONArray;
	}

	private JSONObject _getSegmentsExperimentJSONObject(Locale locale)
		throws Exception {

		return SegmentsExperimentUtil.toSegmentsExperimentJSONObject(
			locale, _getSegmentsExperiment());
	}

	private String _getSegmentsExperimentPortletNamespace() {
		return _portal.getPortletNamespace(
			SegmentsPortletKeys.SEGMENTS_EXPERIMENT);
	}

	private JSONArray _getSegmentsExperimentRelsJSONArray(Locale locale)
		throws Exception {

		SegmentsExperiment segmentsExperiment = _getSegmentsExperiment();

		JSONArray segmentsExperimentRelsJSONArray =
			JSONFactoryUtil.createJSONArray();

		if (segmentsExperiment == null) {
			return segmentsExperimentRelsJSONArray;
		}

		List<SegmentsExperimentRel> segmentsExperimentRels =
			_segmentsExperimentRelService.getSegmentsExperimentRels(
				segmentsExperiment.getSegmentsExperimentId());

		for (SegmentsExperimentRel segmentsExperimentRel :
				segmentsExperimentRels) {

			segmentsExperimentRelsJSONArray.put(
				SegmentsExperimentUtil.toSegmentsExperimentRelJSONObject(
					locale, segmentsExperimentRel));
		}

		return segmentsExperimentRelsJSONArray;
	}

	private long _getSelectedSegmentsExperienceId() {
		if (Validator.isNotNull(_segmentsExperienceId)) {
			return _segmentsExperienceId;
		}

		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(_httpServletRequest);

		long selectedSegmentsExperienceId = ParamUtil.getLong(
			originalHttpServletRequest, "segmentsExperienceId", -1);

		if (selectedSegmentsExperienceId != -1) {
			_segmentsExperienceId = selectedSegmentsExperienceId;
		}
		else {
			_segmentsExperienceId =
				_segmentsExperienceManager.getSegmentsExperienceId(
					_httpServletRequest);
		}

		return _segmentsExperienceId;
	}

	private String _getWinnerSegmentsExperienceId() {
		if (_segmentsExperiment == null) {
			return StringPool.BLANK;
		}

		long winnerSegmentsExperienceId =
			_segmentsExperiment.getWinnerSegmentsExperienceId();

		if (winnerSegmentsExperienceId == -1) {
			return StringPool.BLANK;
		}

		return String.valueOf(winnerSegmentsExperienceId);
	}

	private Map<String, Object> _data;
	private final HttpServletRequest _httpServletRequest;
	private final LayoutLocalService _layoutLocalService;
	private final Portal _portal;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private Long _segmentsExperienceId;
	private final SegmentsExperienceManager _segmentsExperienceManager;
	private final SegmentsExperienceService _segmentsExperienceService;
	private SegmentsExperiment _segmentsExperiment;
	private final SegmentsExperimentConfiguration
		_segmentsExperimentConfiguration;
	private final SegmentsExperimentRelService _segmentsExperimentRelService;
	private final SegmentsExperimentService _segmentsExperimentService;
	private final ThemeDisplay _themeDisplay;

}