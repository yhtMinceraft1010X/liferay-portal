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

package com.liferay.portal.search.tuning.rankings.web.internal.display.context;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kevin Tan
 */
public class EditRankingDisplayBuilder {

	public EditRankingDisplayBuilder(
		HttpServletRequest httpServletRequest, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		_httpServletRequest = httpServletRequest;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;

		_themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public EditRankingDisplayContext build() {
		EditRankingDisplayContext editRankingDisplayContext =
			new EditRankingDisplayContext();

		_setBackURL(editRankingDisplayContext);
		_setCompanyId(editRankingDisplayContext);
		_setData(editRankingDisplayContext);
		_setFormName(editRankingDisplayContext);
		_setInactive(editRankingDisplayContext);
		_setKeywords(editRankingDisplayContext);
		_setRedirect(editRankingDisplayContext);
		_setResultsRankingUid(editRankingDisplayContext);

		return editRankingDisplayContext;
	}

	private String[] _getAliases() {
		return StringUtil.split(
			ParamUtil.getString(_httpServletRequest, "aliases"),
			StringPool.COMMA_AND_SPACE);
	}

	private Map<String, Object> _getConstants() {
		return HashMapBuilder.<String, Object>put(
			"WORKFLOW_ACTION_PUBLISH", WorkflowConstants.ACTION_PUBLISH
		).put(
			"WORKFLOW_ACTION_SAVE_DRAFT", WorkflowConstants.ACTION_SAVE_DRAFT
		).build();
	}

	private Map<String, Object> _getContext() {
		return HashMapBuilder.<String, Object>put(
			"companyId", String.valueOf(_themeDisplay.getCompanyId())
		).put(
			"constants", _getConstants()
		).put(
			"namespace", _renderResponse.getNamespace()
		).put(
			"spritemap", _themeDisplay.getPathThemeImages() + "/clay/icons.svg"
		).build();
	}

	private String _getFormName() {
		return "editResultRankingsFm";
	}

	private String _getHiddenResultRankingsResourceURL() {
		ResourceURL resourceURL = _renderResponse.createResourceURL();

		resourceURL.setParameter(
			"companyId", String.valueOf(_themeDisplay.getCompanyId()));
		resourceURL.setParameter(Constants.CMD, "getHiddenResultsJSONObject");
		resourceURL.setParameter("resultsRankingUid", _getResultsRankingUid());
		resourceURL.setResourceID("/result_rankings/get_results");

		return resourceURL.toString();
	}

	private String _getKeywords() {
		return ParamUtil.getString(_httpServletRequest, "keywords");
	}

	private Map<String, Object> _getProps() {
		return HashMapBuilder.<String, Object>put(
			"cancelUrl", HtmlUtil.escape(_getRedirect())
		).put(
			"fetchDocumentsHiddenUrl", _getHiddenResultRankingsResourceURL()
		).put(
			"fetchDocumentsSearchUrl", _getSearchResultRankingsResourceURL()
		).put(
			"fetchDocumentsVisibleUrl", _getVisibleResultRankingsResourceURL()
		).put(
			"formName", _renderResponse.getNamespace() + _getFormName()
		).put(
			"initialAliases", _getAliases()
		).put(
			"initialInactive", _isInactive()
		).put(
			"resultsRankingUid", _getResultsRankingUid()
		).put(
			"searchQuery", _getKeywords()
		).put(
			"validateFormUrl", _getValidateResultRankingsResourceURL()
		).build();
	}

	private String _getRedirect() {
		String redirect = ParamUtil.getString(_httpServletRequest, "redirect");

		if (Validator.isNull(redirect)) {
			redirect = String.valueOf(_renderResponse.createRenderURL());
		}

		return redirect;
	}

	private String _getResultsRankingUid() {
		return ParamUtil.getString(_httpServletRequest, "resultsRankingUid");
	}

	private String _getSearchResultRankingsResourceURL() {
		ResourceURL resourceURL = _renderResponse.createResourceURL();

		resourceURL.setParameter(
			"companyId", String.valueOf(_themeDisplay.getCompanyId()));
		resourceURL.setParameter(Constants.CMD, "getSearchResultsJSONObject");
		resourceURL.setResourceID("/result_rankings/get_results");

		return resourceURL.toString();
	}

	private String _getValidateResultRankingsResourceURL() {
		ResourceURL resourceURL = _renderResponse.createResourceURL();

		resourceURL.setResourceID("/result_rankings/validate_ranking");

		return resourceURL.toString();
	}

	private String _getVisibleResultRankingsResourceURL() {
		ResourceURL resourceURL = _renderResponse.createResourceURL();

		resourceURL.setParameter(
			"companyId", String.valueOf(_themeDisplay.getCompanyId()));
		resourceURL.setParameter(Constants.CMD, "getVisibleResultsJSONObject");
		resourceURL.setParameter("resultsRankingUid", _getResultsRankingUid());
		resourceURL.setResourceID("/result_rankings/get_results");

		return resourceURL.toString();
	}

	private boolean _isInactive() {
		return ParamUtil.getBoolean(_httpServletRequest, "inactive");
	}

	private void _setBackURL(
		EditRankingDisplayContext editRankingDisplayContext) {

		editRankingDisplayContext.setBackURL(
			ParamUtil.getString(
				_httpServletRequest, "backURL", _getRedirect()));
	}

	private void _setCompanyId(
		EditRankingDisplayContext editRankingDisplayContext) {

		editRankingDisplayContext.setCompanyId(_themeDisplay.getCompanyId());
	}

	private void _setData(EditRankingDisplayContext editRankingDisplayContext) {
		editRankingDisplayContext.setData(
			HashMapBuilder.<String, Object>put(
				"context", _getContext()
			).put(
				"props", _getProps()
			).build());
	}

	private void _setFormName(
		EditRankingDisplayContext editRankingDisplayContext) {

		editRankingDisplayContext.setFormName(_getFormName());
	}

	private void _setInactive(
		EditRankingDisplayContext editRankingDisplayContext) {

		editRankingDisplayContext.setInactive(_isInactive());
	}

	private void _setKeywords(
		EditRankingDisplayContext editRankingDisplayContext) {

		editRankingDisplayContext.setKeywords(_getKeywords());
	}

	private void _setRedirect(
		EditRankingDisplayContext editRankingDisplayContext) {

		editRankingDisplayContext.setRedirect(_getRedirect());
	}

	private void _setResultsRankingUid(
		EditRankingDisplayContext editRankingDisplayContext) {

		editRankingDisplayContext.setResultsRankingUid(_getResultsRankingUid());
	}

	private final HttpServletRequest _httpServletRequest;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final ThemeDisplay _themeDisplay;

}