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

package com.liferay.portal.reports.engine.console.web.internal.admin.search;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.portlet.SearchOrderByUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.reports.engine.console.constants.ReportsEngineConsolePortletKeys;
import com.liferay.portal.reports.engine.console.model.Entry;
import com.liferay.portal.reports.engine.console.util.comparator.EntryCreateDateComparator;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

/**
 * @author Rafael Praxedes
 */
public class EntrySearch extends SearchContainer<Entry> {

	public static final String EMPTY_RESULTS_MESSAGE = "there-are-no-reports";

	public static List<String> headerNames = new ArrayList<String>() {
		{
			add("definition-name");
			add("requested-by");
			add("create-date");
		}
	};

	public EntrySearch(PortletRequest portletRequest, PortletURL iteratorURL) {
		super(
			portletRequest, new EntryDisplayTerms(portletRequest),
			new EntrySearchTerms(portletRequest), DEFAULT_CUR_PARAM,
			DEFAULT_DELTA, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

		EntryDisplayTerms entryDisplayTerms =
			(EntryDisplayTerms)getDisplayTerms();

		iteratorURL.setParameter(
			EntryDisplayTerms.DEFINITION_NAME,
			entryDisplayTerms.getDefinitionName());
		iteratorURL.setParameter(
			EntryDisplayTerms.END_DATE_DAY,
			String.valueOf(entryDisplayTerms.getEndDateDay()));
		iteratorURL.setParameter(
			EntryDisplayTerms.END_DATE_MONTH,
			String.valueOf(entryDisplayTerms.getEndDateMonth()));
		iteratorURL.setParameter(
			EntryDisplayTerms.END_DATE_YEAR,
			String.valueOf(entryDisplayTerms.getEndDateYear()));
		iteratorURL.setParameter(
			EntryDisplayTerms.START_DATE_DAY,
			String.valueOf(entryDisplayTerms.getStartDateDay()));
		iteratorURL.setParameter(
			EntryDisplayTerms.START_DATE_MONTH,
			String.valueOf(entryDisplayTerms.getStartDateMonth()));
		iteratorURL.setParameter(
			EntryDisplayTerms.START_DATE_YEAR,
			String.valueOf(entryDisplayTerms.getStartDateYear()));
		iteratorURL.setParameter(
			EntryDisplayTerms.USER_NAME, entryDisplayTerms.getUserName());

		setOrderByCol(
			SearchOrderByUtil.getOrderByCol(
				portletRequest, ReportsEngineConsolePortletKeys.REPORTS_ADMIN,
				"create-date"));

		String orderByType = SearchOrderByUtil.getOrderByType(
			portletRequest, ReportsEngineConsolePortletKeys.REPORTS_ADMIN,
			"asc");

		setOrderByComparator(_getEntryOrderByComparator(orderByType));
		setOrderByType(orderByType);
	}

	private OrderByComparator<Entry> _getEntryOrderByComparator(
		String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		return new EntryCreateDateComparator(orderByAsc);
	}

}