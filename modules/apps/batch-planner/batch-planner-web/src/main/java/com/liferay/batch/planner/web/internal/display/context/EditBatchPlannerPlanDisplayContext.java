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

package com.liferay.batch.planner.web.internal.display.context;

import com.liferay.batch.engine.BatchEngineTaskContentType;
import com.liferay.batch.planner.model.BatchPlannerMapping;
import com.liferay.batch.planner.model.BatchPlannerPlan;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.SelectOption;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Igor Beslic
 * @author Matija Petanjek
 */
public class EditBatchPlannerPlanDisplayContext {

	public EditBatchPlannerPlanDisplayContext(
			List<BatchPlannerPlan> batchPlannerPlans,
			Set<String> internalClassNames,
			BatchPlannerPlan selectedBatchPlannerPlan)
		throws PortalException {

		_internalClassNameSelectOptions = _getInternalClassNameSelectOptions(
			internalClassNames);

		if (selectedBatchPlannerPlan == null) {
			_selectedBatchPlannerMappings = new HashMap<>();
			_selectedBatchPlannerPlanId = 0;
			_selectedBatchPlannerPlanName = StringPool.BLANK;
			_selectedExternalType = StringPool.BLANK;
			_selectedInternalClassName = StringPool.BLANK;
		}
		else {
			_selectedBatchPlannerMappings = _getSelectedBatchPlannerMappings(
				selectedBatchPlannerPlan);
			_selectedBatchPlannerPlanId =
				selectedBatchPlannerPlan.getBatchPlannerPlanId();
			_selectedBatchPlannerPlanName = selectedBatchPlannerPlan.getName();
			_selectedExternalType = selectedBatchPlannerPlan.getExternalType();
			_selectedInternalClassName =
				selectedBatchPlannerPlan.getInternalClassName();
		}

		_templateSelectOptions = _getTemplateSelectOptions(batchPlannerPlans);
	}

	public List<SelectOption> getExternalTypeSelectOptions() {
		List<SelectOption> selectOptions = new ArrayList<>();

		for (BatchEngineTaskContentType batchEngineTaskContentType :
				BatchEngineTaskContentType.values()) {

			if ((batchEngineTaskContentType ==
					BatchEngineTaskContentType.XLS) ||
				(batchEngineTaskContentType ==
					BatchEngineTaskContentType.XLSX)) {

				continue;
			}

			selectOptions.add(
				new SelectOption(
					batchEngineTaskContentType.toString(),
					batchEngineTaskContentType.toString()));
		}

		return selectOptions;
	}

	public List<SelectOption> getInternalClassNameSelectOptions() {
		return _internalClassNameSelectOptions;
	}

	public long getSelectedBatchPlannerPlanId() {
		return _selectedBatchPlannerPlanId;
	}

	public Map<String, String> getSelectedBatchPlannerPlanMappings() {
		return _selectedBatchPlannerMappings;
	}

	public String getSelectedBatchPlannerPlanName() {
		return _selectedBatchPlannerPlanName;
	}

	public String getSelectedExternalType() {
		return _selectedExternalType;
	}

	public String getSelectedInternalClassName() {
		return _selectedInternalClassName;
	}

	public List<SelectOption> getTemplateSelectOptions() {
		return _templateSelectOptions;
	}

	private List<SelectOption> _getInternalClassNameSelectOptions(
		Set<String> internalClassNames) {

		List<SelectOption> internalClassNameSelectOptions = new ArrayList<>();

		internalClassNameSelectOptions.add(
			new SelectOption(StringPool.BLANK, StringPool.BLANK));

		for (String internalClassName : internalClassNames) {
			String[] internalClassNameParts = StringUtil.split(
				internalClassName, StringPool.PERIOD);

			internalClassNameSelectOptions.add(
				new SelectOption(
					String.format(
						"%s (%s)",
						internalClassNameParts
							[internalClassNameParts.length - 1],
						internalClassNameParts
							[internalClassNameParts.length - 2]),
					internalClassName));
		}

		internalClassNameSelectOptions.sort(
			Comparator.comparing(SelectOption::getLabel));

		return internalClassNameSelectOptions;
	}

	private Map<String, String> _getSelectedBatchPlannerMappings(
		BatchPlannerPlan selectedBatchPlannerPlan) {

		Map<String, String> selectedBatchPlannerMappings = new HashMap<>();

		for (BatchPlannerMapping batchPlannerMapping :
				selectedBatchPlannerPlan.getBatchPlannerMappings()) {

			selectedBatchPlannerMappings.put(
				batchPlannerMapping.getInternalFieldName(),
				batchPlannerMapping.getExternalFieldName());
		}

		return selectedBatchPlannerMappings;
	}

	private List<SelectOption> _getTemplateSelectOptions(
		List<BatchPlannerPlan> batchPlannerPlans) {

		List<SelectOption> templateSelectOptions = new ArrayList<>();

		for (BatchPlannerPlan batchPlannerPlan : batchPlannerPlans) {
			boolean selected = false;

			if (batchPlannerPlan.getBatchPlannerPlanId() ==
					_selectedBatchPlannerPlanId) {

				selected = true;
			}

			templateSelectOptions.add(
				new SelectOption(
					batchPlannerPlan.getName(),
					String.valueOf(batchPlannerPlan.getBatchPlannerPlanId()),
					selected));
		}

		return templateSelectOptions;
	}

	private final List<SelectOption> _internalClassNameSelectOptions;
	private final Map<String, String> _selectedBatchPlannerMappings;
	private final long _selectedBatchPlannerPlanId;
	private final String _selectedBatchPlannerPlanName;
	private final String _selectedExternalType;
	private final String _selectedInternalClassName;
	private final List<SelectOption> _templateSelectOptions;

}