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

package com.liferay.portal.search.tuning.rankings.web.internal.portlet.action;

import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.portal.search.tuning.rankings.web.internal.configuration.DefaultResultRankingsConfiguration;
import com.liferay.portal.search.tuning.rankings.web.internal.configuration.ResultRankingsConfiguration;
import com.liferay.portal.search.tuning.rankings.web.internal.constants.ResultRankingsConstants;
import com.liferay.portal.search.tuning.rankings.web.internal.constants.ResultRankingsPortletKeys;
import com.liferay.portal.search.tuning.rankings.web.internal.exception.DuplicateAliasStringException;
import com.liferay.portal.search.tuning.rankings.web.internal.exception.DuplicateQueryStringException;
import com.liferay.portal.search.tuning.rankings.web.internal.index.DuplicateQueryStringsDetector;
import com.liferay.portal.search.tuning.rankings.web.internal.index.Ranking;
import com.liferay.portal.search.tuning.rankings.web.internal.index.RankingIndexReader;
import com.liferay.portal.search.tuning.rankings.web.internal.index.name.RankingIndexName;
import com.liferay.portal.search.tuning.rankings.web.internal.index.name.RankingIndexNameBuilder;
import com.liferay.portal.search.tuning.rankings.web.internal.storage.RankingStorageAdapter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Kevin Tan
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ResultRankingsPortletKeys.RESULT_RANKINGS,
		"mvc.command.name=/result_rankings/edit_ranking"
	},
	service = MVCActionCommand.class
)
public class EditRankingMVCActionCommand extends BaseMVCActionCommand {

	@Override
	public void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		_companyId = portal.getCompanyId(actionRequest);

		EditRankingMVCActionRequest editRankingMVCActionRequest =
			new EditRankingMVCActionRequest(actionRequest);

		if (editRankingMVCActionRequest.isCmd(Constants.ADD)) {
			_add(actionRequest, actionResponse, editRankingMVCActionRequest);
		}
		else if (editRankingMVCActionRequest.isCmd(Constants.UPDATE)) {
			_update(actionRequest, actionResponse, editRankingMVCActionRequest);
		}
		else if (editRankingMVCActionRequest.isCmd(Constants.DELETE)) {
			_delete(actionRequest, actionResponse, editRankingMVCActionRequest);
		}
		else if (editRankingMVCActionRequest.isCmd(
					ResultRankingsConstants.DEACTIVATE)) {

			_deactivate(
				actionRequest, actionResponse, editRankingMVCActionRequest,
				true);
		}
		else if (editRankingMVCActionRequest.isCmd(
					ResultRankingsConstants.ACTIVATE)) {

			_deactivate(
				actionRequest, actionResponse, editRankingMVCActionRequest,
				false);
		}
	}

	protected String getIndexName(ActionRequest actionRequest) {
		return indexNameBuilder.getIndexName(
			portal.getCompanyId(actionRequest));
	}

	protected RankingIndexName getRankingIndexName() {
		return rankingIndexNameBuilder.getRankingIndexName(_companyId);
	}

	@Reference
	protected DuplicateQueryStringsDetector duplicateQueryStringsDetector;

	@Reference
	protected IndexNameBuilder indexNameBuilder;

	@Reference
	protected Portal portal;

	@Reference
	protected RankingIndexNameBuilder rankingIndexNameBuilder;

	@Reference
	protected RankingIndexReader rankingIndexReader;

	@Reference
	protected RankingStorageAdapter rankingStorageAdapter;

	private void _add(
			ActionRequest actionRequest, ActionResponse actionResponse,
			EditRankingMVCActionRequest editRankingMVCActionRequest)
		throws Exception {

		try {
			Ranking ranking = _add(actionRequest, editRankingMVCActionRequest);

			String redirect = _getSaveAndContinueRedirect(
				actionRequest, ranking,
				editRankingMVCActionRequest.getRedirect());

			sendRedirect(actionRequest, actionResponse, redirect);
		}
		catch (Exception exception) {
			actionRequest.setAttribute(
				WebKeys.REDIRECT,
				PortletURLBuilder.createRenderURL(
					portal.getLiferayPortletResponse(actionResponse)
				).setMVCRenderCommandName(
					"/result_rankings/add_results_rankings"
				).setRedirect(
					editRankingMVCActionRequest.getRedirect()
				).buildString());

			SessionErrors.add(actionRequest, exception.getClass());

			hideDefaultErrorMessage(actionRequest);

			sendRedirect(actionRequest, actionResponse);
		}
	}

	private Ranking _add(
		ActionRequest actionRequest,
		EditRankingMVCActionRequest editRankingMVCActionRequest) {

		Ranking.RankingBuilder rankingBuilder = new Ranking.RankingBuilder();

		String resultActionCmd = ParamUtil.getString(
			actionRequest, "resultActionCmd");
		String resultActionUid = ParamUtil.getString(
			actionRequest, "resultActionUid");

		if (!resultActionCmd.isEmpty() && !resultActionUid.isEmpty()) {
			if (resultActionCmd.equals("pin")) {
				rankingBuilder.pins(
					Arrays.asList(new Ranking.Pin(0, resultActionUid)));
			}
			else {
				rankingBuilder.hiddenDocumentIds(
					ListUtil.fromString(resultActionUid));
			}
		}

		rankingBuilder.indexName(
			getIndexName(actionRequest)
		).name(
			editRankingMVCActionRequest.getQueryString()
		).queryString(
			editRankingMVCActionRequest.getQueryString()
		);

		Ranking ranking = rankingBuilder.build();

		_guardDuplicateQueryStrings(editRankingMVCActionRequest, ranking);

		RankingIndexName rankingIndexName = getRankingIndexName();

		String id = rankingStorageAdapter.create(rankingIndexName, ranking);

		Optional<Ranking> optional = rankingIndexReader.fetchOptional(
			rankingIndexName, id);

		return optional.get();
	}

	private void _deactivate(
			ActionRequest actionRequest, ActionResponse actionResponse,
			EditRankingMVCActionRequest editRankingMVCActionRequest,
			boolean inactive)
		throws Exception {

		try {
			_deactivate(actionRequest, editRankingMVCActionRequest, inactive);

			sendRedirect(
				actionRequest, actionResponse,
				editRankingMVCActionRequest.getRedirect());
		}
		catch (Exception exception) {
			if (exception instanceof DuplicateAliasStringException) {
				SessionErrors.add(
					actionRequest, DuplicateAliasStringException.class);
			}
			else if (exception instanceof DuplicateQueryStringException) {
				SessionErrors.add(
					actionRequest, DuplicateQueryStringException.class);
			}
			else {
				SessionErrors.add(actionRequest, Exception.class);
			}

			hideDefaultErrorMessage(actionRequest);

			sendRedirect(actionRequest, actionResponse);
		}
	}

	private void _deactivate(
			ActionRequest actionRequest,
			EditRankingMVCActionRequest editRankingMVCActionRequest,
			boolean inactive)
		throws PortalException {

		List<Ranking> rankings = _getRankings(
			actionRequest, editRankingMVCActionRequest);

		if (!inactive) {
			_guardDuplicateQueryStrings(editRankingMVCActionRequest, rankings);
		}

		for (Ranking ranking : rankings) {
			Ranking.RankingBuilder rankingBuilder = new Ranking.RankingBuilder(
				ranking);

			rankingBuilder.inactive(inactive);

			rankingStorageAdapter.update(
				getRankingIndexName(), rankingBuilder.build());
		}
	}

	private void _delete(
			ActionRequest actionRequest, ActionResponse actionResponse,
			EditRankingMVCActionRequest editRankingMVCActionRequest)
		throws Exception {

		_delete(actionRequest, editRankingMVCActionRequest);

		sendRedirect(
			actionRequest, actionResponse,
			editRankingMVCActionRequest.getRedirect());
	}

	private void _delete(
			ActionRequest actionRequest,
			EditRankingMVCActionRequest editRankingMVCActionRequest)
		throws Exception {

		String[] rankingDocumentIds = _getRankingDocumentIds(
			actionRequest, editRankingMVCActionRequest);

		for (String rankingDocumentId : rankingDocumentIds) {
			rankingStorageAdapter.delete(
				getRankingIndexName(), rankingDocumentId);
		}
	}

	private boolean _detectedDuplicateQueryStrings(
		Ranking ranking, Collection<String> queryStrings) {

		List<String> duplicateQueryStrings =
			duplicateQueryStringsDetector.detect(
				duplicateQueryStringsDetector.builder(
				).index(
					_getCompanyIndexName()
				).queryStrings(
					queryStrings
				).rankingIndexName(
					getRankingIndexName()
				).unlessRankingDocumentId(
					ranking.getRankingDocumentId()
				).build());

		return ListUtil.isNotEmpty(duplicateQueryStrings);
	}

	private List<String> _getAliases(
		EditRankingMVCActionRequest editRankingMVCActionRequest) {

		List<String> strings = new ArrayList<>(
			editRankingMVCActionRequest.getAliases());

		Stream<String> stream = strings.stream();

		Predicate<String> predicate = this::_isUpdateSpecial;

		return stream.filter(
			predicate.negate()
		).collect(
			Collectors.toList()
		);
	}

	private String _getCompanyIndexName() {
		return indexNameBuilder.getIndexName(_companyId);
	}

	private String _getNameForUpdate(
		String oldName,
		EditRankingMVCActionRequest editRankingMVCActionRequest) {

		List<String> strings = editRankingMVCActionRequest.getAliases();

		Stream<String> stream = strings.stream();

		return stream.filter(
			this::_isUpdateSpecial
		).map(
			this::_stripUpdateSpecial
		).findAny(
		).orElse(
			oldName
		);
	}

	private String[] _getRankingDocumentIds(
		ActionRequest actionRequest,
		EditRankingMVCActionRequest editRankingMVCActionRequest) {

		String[] rankingDocumentIds = null;

		String resultsRankingUid =
			editRankingMVCActionRequest.getResultsRankingUid();

		if (Validator.isNotNull(resultsRankingUid)) {
			rankingDocumentIds = new String[] {resultsRankingUid};
		}
		else {
			rankingDocumentIds = ParamUtil.getStringValues(
				actionRequest, "rowIds");
		}

		return rankingDocumentIds;
	}

	private List<Ranking> _getRankings(
		ActionRequest actionRequest,
		EditRankingMVCActionRequest editRankingMVCActionRequest) {

		List<Ranking> rankings = new ArrayList<>();

		String[] rankingDocumentIds = _getRankingDocumentIds(
			actionRequest, editRankingMVCActionRequest);

		RankingIndexName rankingIndexName = getRankingIndexName();

		for (String rankingDocumentId : rankingDocumentIds) {
			Optional<Ranking> optional = rankingIndexReader.fetchOptional(
				rankingIndexName, rankingDocumentId);

			if (optional.isPresent()) {
				Ranking ranking = optional.get();

				rankings.add(ranking);
			}
		}

		return rankings;
	}

	private String _getSaveAndContinueRedirect(
			ActionRequest actionRequest, Ranking ranking, String redirect)
		throws Exception {

		PortletConfig portletConfig = (PortletConfig)actionRequest.getAttribute(
			JavaConstants.JAVAX_PORTLET_CONFIG);

		LiferayPortletURL portletURL = PortletURLFactoryUtil.create(
			actionRequest, portletConfig.getPortletName(),
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter(
			"mvcRenderCommandName", "/result_rankings/edit_results_rankings");
		portletURL.setParameter(Constants.CMD, Constants.UPDATE, false);
		portletURL.setParameter("redirect", redirect, false);
		portletURL.setParameter(
			"resultsRankingUid", ranking.getRankingDocumentId(), false);
		portletURL.setParameter(
			EditRankingMVCActionRequest.PARAM_ALIASES,
			StringUtil.merge(ranking.getAliases(), StringPool.COMMA), false);
		portletURL.setParameter(
			EditRankingMVCActionRequest.PARAM_KEYWORDS,
			ranking.getQueryString(), false);
		portletURL.setWindowState(actionRequest.getWindowState());

		return portletURL.toString();
	}

	private void _guardDuplicateQueryStrings(
		EditRankingMVCActionRequest editRankingMVCActionRequest,
		List<Ranking> rankings) {

		List<String> queryStrings = new ArrayList<>();

		for (Ranking ranking : rankings) {
			queryStrings.addAll(ranking.getQueryStrings());
		}

		List<String> uniqueQueryStrings = ListUtil.unique(queryStrings);

		if (queryStrings.size() != uniqueQueryStrings.size()) {
			throw new DuplicateQueryStringException();
		}

		for (Ranking ranking : rankings) {
			_guardDuplicateQueryStrings(editRankingMVCActionRequest, ranking);
		}
	}

	private void _guardDuplicateQueryStrings(
		EditRankingMVCActionRequest editRankingMVCActionRequest,
		Ranking ranking) {

		if (_resultRankingsConfiguration.allowDuplicateQueryStrings() ||
			_isInactive(editRankingMVCActionRequest) ||
			editRankingMVCActionRequest.isCmd(
				ResultRankingsConstants.DEACTIVATE)) {

			return;
		}

		Collection<String> queryStrings = ranking.getQueryStrings();

		if (editRankingMVCActionRequest.isCmd(Constants.UPDATE)) {
			List<String> aliases = _getAliases(editRankingMVCActionRequest);

			queryStrings = Stream.concat(
				Stream.of(ranking.getQueryString()), aliases.stream()
			).filter(
				string -> !Validator.isBlank(string)
			).distinct(
			).sorted(
			).collect(
				Collectors.toList()
			);
		}

		if (_detectedDuplicateQueryStrings(ranking, queryStrings)) {
			throw new DuplicateQueryStringException();
		}
	}

	private boolean _isInactive(
		EditRankingMVCActionRequest editRankingMVCActionRequest) {

		return editRankingMVCActionRequest.getInactive();
	}

	private boolean _isUpdateSpecial(String string) {
		return string.startsWith(_UPDATE_SPECIAL);
	}

	private String _stripUpdateSpecial(String string) {
		return string.substring(_UPDATE_SPECIAL.length());
	}

	private void _update(
			ActionRequest actionRequest, ActionResponse actionResponse,
			EditRankingMVCActionRequest editRankingMVCActionRequest)
		throws IOException {

		try {
			_update(actionRequest, editRankingMVCActionRequest);

			sendRedirect(
				actionRequest, actionResponse,
				editRankingMVCActionRequest.getRedirect());
		}
		catch (Exception exception) {
			if (exception instanceof DuplicateAliasStringException) {
				SessionErrors.add(actionRequest, Exception.class);

				actionResponse.setRenderParameter(
					"mvcRenderCommandName",
					"/result_rankings/edit_results_rankings");
			}
			else {
				SessionErrors.add(actionRequest, Exception.class);

				actionResponse.setRenderParameter("mvcPath", "/error.jsp");
			}
		}
	}

	private void _update(
			ActionRequest actionRequest,
			EditRankingMVCActionRequest editRankingMVCActionRequest)
		throws PortalException {

		String id = editRankingMVCActionRequest.getResultsRankingUid();

		Optional<Ranking> optional = rankingIndexReader.fetchOptional(
			rankingIndexNameBuilder.getRankingIndexName(
				portal.getCompanyId(actionRequest)),
			id);

		if (!optional.isPresent()) {
			return;
		}

		Ranking ranking = optional.get();

		_guardDuplicateQueryStrings(editRankingMVCActionRequest, ranking);

		Ranking.RankingBuilder rankingBuilder = new Ranking.RankingBuilder(
			ranking);

		String[] hiddenIdsAdded = ParamUtil.getStringValues(
			actionRequest, "hiddenIdsAdded");
		String[] hiddenIdsRemoved = ParamUtil.getStringValues(
			actionRequest, "hiddenIdsRemoved");

		rankingBuilder.aliases(
			_getAliases(editRankingMVCActionRequest)
		).hiddenDocumentIds(
			_update(
				ranking.getHiddenDocumentIds(), hiddenIdsAdded,
				hiddenIdsRemoved)
		).inactive(
			_isInactive(editRankingMVCActionRequest)
		).indexName(
			getIndexName(actionRequest)
		).name(
			_getNameForUpdate(ranking.getName(), editRankingMVCActionRequest)
		);

		List<Ranking.Pin> pins = new ArrayList<>();

		String[] pinnedIds = ParamUtil.getStringValues(
			actionRequest, "pinnedIds");

		for (int i = 0; i < pinnedIds.length; i++) {
			pins.add(new Ranking.Pin(i, pinnedIds[i]));
		}

		if (ListUtil.isNotEmpty(pins)) {
			rankingBuilder.pins(pins);
		}
		else {
			rankingBuilder.pins(null);
		}

		rankingStorageAdapter.update(
			getRankingIndexName(), rankingBuilder.build());
	}

	private List<String> _update(
		List<String> strings, String[] addStrings, String[] removeStrings) {

		List<String> newStrings;

		if (ListUtil.isEmpty(strings)) {
			newStrings = Arrays.asList(addStrings);
		}
		else {
			newStrings = new ArrayList<>(strings);

			Collections.addAll(newStrings, addStrings);
		}

		newStrings.removeAll(Arrays.asList(removeStrings));

		return newStrings;
	}

	private static final String _UPDATE_SPECIAL = StringPool.GREATER_THAN;

	private long _companyId;
	private final ResultRankingsConfiguration _resultRankingsConfiguration =
		new DefaultResultRankingsConfiguration();

	private class EditRankingMVCActionRequest {

		public static final String PARAM_ALIASES = "aliases";

		public static final String PARAM_KEYWORDS = "keywords";

		public EditRankingMVCActionRequest(ActionRequest actionRequest) {
			_cmd = ParamUtil.getString(actionRequest, Constants.CMD);
			_redirect = ParamUtil.getString(actionRequest, "redirect");
			_inactive = ParamUtil.getBoolean(actionRequest, "inactive");
			_queryString = ParamUtil.getString(actionRequest, PARAM_KEYWORDS);
			_resultsRankingUid = ParamUtil.getString(
				actionRequest, "resultsRankingUid");

			_aliases = Arrays.asList(
				ParamUtil.getStringValues(actionRequest, PARAM_ALIASES));
		}

		public List<String> getAliases() {
			return Collections.unmodifiableList(_aliases);
		}

		public boolean getInactive() {
			return _inactive;
		}

		public String getQueryString() {
			return _queryString;
		}

		public String getRedirect() {
			return _redirect;
		}

		public String getResultsRankingUid() {
			return _resultsRankingUid;
		}

		public boolean isCmd(String cmd) {
			return Objects.equals(cmd, _cmd);
		}

		private final List<String> _aliases;
		private final String _cmd;
		private final boolean _inactive;
		private final String _queryString;
		private final String _redirect;
		private final String _resultsRankingUid;

	}

}