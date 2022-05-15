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

package com.liferay.depot.internal.model.listener;

import com.liferay.depot.model.DepotAppCustomization;
import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.DepotAppCustomizationLocalService;
import com.liferay.depot.service.DepotEntryGroupRelLocalService;
import com.liferay.depot.service.DepotEntryLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(service = ModelListener.class)
public class GroupModelListener extends BaseModelListener<Group> {

	@Override
	public void onAfterRemove(Group group) throws ModelListenerException {
		if ((group != null) && group.isDepot()) {
			TransactionCommitCallbackUtil.registerCallback(
				() -> {
					DepotEntry depotEntry =
						_depotEntryLocalService.fetchGroupDepotEntry(
							group.getGroupId());

					if (depotEntry != null) {
						_depotEntryLocalService.deleteDepotEntry(
							depotEntry.getDepotEntryId());
					}

					return null;
				});
		}
	}

	@Override
	public void onBeforeCreate(Group group) throws ModelListenerException {
		try {
			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			if (group.isDepot() && _isStaging(serviceContext)) {
				DepotEntry depotEntry = _depotEntryLocalService.addDepotEntry(
					group, serviceContext);

				group.setClassPK(depotEntry.getDepotEntryId());

				Group liveGroup = group.getLiveGroup();

				_copyDepotAppCustomizations(
					depotEntry.getDepotEntryId(), liveGroup.getClassPK());
			}
		}
		catch (PortalException portalException) {
			throw new ModelListenerException(portalException);
		}
	}

	@Override
	public void onBeforeRemove(Group group) throws ModelListenerException {
		if (!group.isSite()) {
			return;
		}

		_depotEntryGroupRelLocalService.deleteToGroupDepotEntryGroupRels(
			group.getGroupId());
	}

	private void _copyDepotAppCustomizations(
			long newDepotEntryId, long oldDepotEntryId)
		throws PortalException {

		List<DepotAppCustomization> depotAppCustomizations =
			_depotAppCustomizationLocalService.getDepotAppCustomizations(
				oldDepotEntryId);

		for (DepotAppCustomization depotAppCustomization :
				depotAppCustomizations) {

			_depotAppCustomizationLocalService.updateDepotAppCustomization(
				newDepotEntryId, depotAppCustomization.getEnabled(),
				depotAppCustomization.getPortletId());
		}
	}

	private boolean _isStaging(ServiceContext serviceContext) {
		if (serviceContext == null) {
			return false;
		}

		return ParamUtil.getBoolean(serviceContext, "staging");
	}

	@Reference
	private DepotAppCustomizationLocalService
		_depotAppCustomizationLocalService;

	@Reference
	private DepotEntryGroupRelLocalService _depotEntryGroupRelLocalService;

	@Reference
	private DepotEntryLocalService _depotEntryLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

}