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

package com.liferay.search.experiences.internal.instance.lifecycle;

import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;
import com.liferay.search.experiences.rest.dto.v1_0.SXPElement;
import com.liferay.search.experiences.rest.dto.v1_0.util.SXPElementUtil;
import com.liferay.search.experiences.service.SXPElementLocalService;

import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author André de Oliveira
 */
@Component(
	enabled = true, immediate = true,
	service = PortalInstanceLifecycleListener.class
)
public class SXPPortalInstanceLifecycleListener
	extends BasePortalInstanceLifecycleListener {

	@Override
	public void portalInstanceRegistered(Company company) throws Exception {

		// TODO Remove the method _addSXPElement and inline the logic if
		// paste_any_elasticsearch_query is the only search experiences
		// element that needs to be added per company

		_addSXPElement(company, "paste_any_elasticsearch_query");
	}

	private void _addSXPElement(Company company, String fileName)
		throws Exception {

		String json = StringUtil.read(
			getClass(), "dependencies/" + fileName + ".json");

		SXPElement sxpElement = SXPElementUtil.toSXPElement(json);

		if (ListUtil.exists(
				_sxpElementLocalService.getSXPElements(company.getCompanyId()),
				serviceBuilderSXPElement -> Objects.equals(
					MapUtil.getString(sxpElement.getTitle_i18n(), "en_US"),
					serviceBuilderSXPElement.getTitle(LocaleUtil.US)))) {

			// TODO Fix performance issue with getting every SXP element

			return;
		}

		User defaultUser = company.getDefaultUser();

		_sxpElementLocalService.addSXPElement(
			defaultUser.getUserId(),
			LocalizedMapUtil.getLocalizedMap(sxpElement.getDescription_i18n()),
			String.valueOf(sxpElement.getElementDefinition()), true,
			LocalizedMapUtil.getLocalizedMap(sxpElement.getTitle_i18n()), 0,
			new ServiceContext() {
				{
					setAddGroupPermissions(true);
					setAddGuestPermissions(true);
					setCompanyId(company.getCompanyId());
					setScopeGroupId(company.getGroupId());
					setUserId(defaultUser.getUserId());
				}
			});
	}

	@Reference
	private SXPElementLocalService _sxpElementLocalService;

}