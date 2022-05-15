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

package com.liferay.depot.service.impl;

import com.liferay.depot.model.DepotAppCustomization;
import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.base.DepotAppCustomizationLocalServiceBaseImpl;
import com.liferay.depot.service.persistence.DepotEntryPersistence;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alicia García
 */
@Component(
	property = "model.class.name=com.liferay.depot.model.DepotAppCustomization",
	service = AopService.class
)
public class DepotAppCustomizationLocalServiceImpl
	extends DepotAppCustomizationLocalServiceBaseImpl {

	@Override
	public DepotAppCustomization fetchDepotAppCustomization(
		long depotEntryId, String portletId) {

		return depotAppCustomizationPersistence.fetchByD_P(
			depotEntryId, portletId);
	}

	@Override
	public List<DepotAppCustomization> getDepotAppCustomizations(
		long depotEntryId) {

		return depotAppCustomizationPersistence.findByDepotEntryId(
			depotEntryId);
	}

	@Override
	public int getDepotAppCustomizationsCount(
		long depotEntryId, boolean enabled) {

		return depotAppCustomizationPersistence.countByD_E(
			depotEntryId, enabled);
	}

	@Override
	public DepotAppCustomization updateDepotAppCustomization(
			long depotEntryId, boolean enabled, String portletId)
		throws PortalException {

		DepotAppCustomization depotAppCustomization =
			depotAppCustomizationPersistence.fetchByD_P(
				depotEntryId, portletId);

		if (depotAppCustomization == null) {
			return _addDepotAppCustomization(depotEntryId, enabled, portletId);
		}

		depotAppCustomization.setEnabled(enabled);

		return depotAppCustomizationPersistence.update(depotAppCustomization);
	}

	private DepotAppCustomization _addDepotAppCustomization(
			long depotEntryId, boolean enabled, String portletId)
		throws PortalException {

		DepotAppCustomization depotAppCustomization =
			depotAppCustomizationPersistence.create(
				counterLocalService.increment());

		DepotEntry depotEntry = _depotEntryPersistence.findByPrimaryKey(
			depotEntryId);

		depotAppCustomization.setCompanyId(depotEntry.getCompanyId());

		depotAppCustomization.setDepotEntryId(depotEntryId);
		depotAppCustomization.setEnabled(enabled);
		depotAppCustomization.setPortletId(portletId);

		return depotAppCustomizationPersistence.update(depotAppCustomization);
	}

	@Reference
	private DepotEntryPersistence _depotEntryPersistence;

}