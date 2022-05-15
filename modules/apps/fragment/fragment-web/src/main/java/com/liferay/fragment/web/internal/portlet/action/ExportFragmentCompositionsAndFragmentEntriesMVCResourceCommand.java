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

package com.liferay.fragment.web.internal.portlet.action;

import com.liferay.fragment.constants.FragmentPortletKeys;
import com.liferay.fragment.model.FragmentComposition;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.service.FragmentCompositionService;
import com.liferay.fragment.service.FragmentEntryService;
import com.liferay.fragment.web.internal.portlet.helper.ExportHelper;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Time;

import java.io.File;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + FragmentPortletKeys.FRAGMENT,
		"mvc.command.name=/fragment/export_fragment_compositions_and_fragment_entries"
	},
	service = MVCResourceCommand.class
)
public class ExportFragmentCompositionsAndFragmentEntriesMVCResourceCommand
	implements MVCResourceCommand {

	@Override
	public boolean serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws PortletException {

		long[] exportFragmentCompositionIds = null;
		long[] exportFragmentEntryIds = null;

		long fragmentCompositionId = ParamUtil.getLong(
			resourceRequest, "fragmentCompositionId");
		long fragmentEntryId = ParamUtil.getLong(
			resourceRequest, "fragmentEntryId");

		if (fragmentCompositionId > 0) {
			exportFragmentCompositionIds = new long[] {fragmentCompositionId};
		}
		else if (fragmentEntryId > 0) {
			exportFragmentEntryIds = new long[] {fragmentEntryId};
		}
		else {
			exportFragmentCompositionIds = ParamUtil.getLongValues(
				resourceRequest, "rowIdsFragmentComposition");
			exportFragmentEntryIds = ParamUtil.getLongValues(
				resourceRequest, "rowIdsFragmentEntry");
		}

		try {
			List<FragmentComposition> fragmentCompositions = new ArrayList<>();

			if (ArrayUtil.isNotEmpty(exportFragmentCompositionIds)) {
				for (long exportFragmentCompositionId :
						exportFragmentCompositionIds) {

					fragmentCompositions.add(
						_fragmentCompositionService.fetchFragmentComposition(
							exportFragmentCompositionId));
				}
			}

			List<FragmentEntry> fragmentEntries = new ArrayList<>();

			if (ArrayUtil.isNotEmpty(exportFragmentEntryIds)) {
				for (long exportFragmentEntryId : exportFragmentEntryIds) {
					fragmentEntries.add(
						_fragmentEntryService.fetchFragmentEntry(
							exportFragmentEntryId));
				}
			}

			File file =
				_exportHelper.exportFragmentCompositionsAndFragmentEntries(
					fragmentCompositions, fragmentEntries);

			PortletResponseUtil.sendFile(
				resourceRequest, resourceResponse,
				"entries-" + Time.getTimestamp() + ".zip",
				new FileInputStream(file), ContentTypes.APPLICATION_ZIP);
		}
		catch (Exception exception) {
			throw new PortletException(exception);
		}

		return false;
	}

	@Reference
	private ExportHelper _exportHelper;

	@Reference
	private FragmentCompositionService _fragmentCompositionService;

	@Reference
	private FragmentEntryService _fragmentEntryService;

}