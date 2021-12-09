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
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;
import com.liferay.search.experiences.rest.dto.v1_0.SXPElement;
import com.liferay.search.experiences.rest.dto.v1_0.util.SXPElementUtil;
import com.liferay.search.experiences.service.SXPElementLocalService;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

/**
 * @author André de Oliveira
 * @author Petteri Karttunen
 */
@Component(
	enabled = true, immediate = true,
	service = PortalInstanceLifecycleListener.class
)
public class SXPPortalInstanceLifecycleListener
	extends BasePortalInstanceLifecycleListener {

	public SXPPortalInstanceLifecycleListener() throws IOException {
		Reflections reflections = new Reflections(
			"com.liferay.search.experiences.internal.instance.lifecycle." +
				"dependencies",
			Scanners.Resources);

		for (String resourceName : reflections.getResources(".*\\.json")) {
			Class<?> clazz = getClass();

			sxpElements.add(
				SXPElementUtil.toSXPElement(
					StringUtil.read(clazz.getClassLoader(), resourceName)));
		}
	}

	@Override
	public void portalInstanceRegistered(Company company) throws Exception {

		// TODO Move to an upgrade process for existing companies. For new
		// companies, use a model listener.

		_addSXPElements(company);
	}

	protected final List<SXPElement> sxpElements = new ArrayList<>();

	private void _addSXPElements(Company company) throws Exception {
		Set<String> titles = new HashSet<>();

		for (com.liferay.search.experiences.model.SXPElement sxpPElement :
				_sxpElementLocalService.getSXPElements(
					company.getCompanyId(), true)) {

			titles.add(sxpPElement.getTitle(LocaleUtil.US));
		}

		for (SXPElement sxpElement : sxpElements) {

			// TODO Should this be en_US or en-US?

			if (titles.contains(
					MapUtil.getString(sxpElement.getTitle_i18n(), "en_US"))) {

				continue;
			}

			User user = company.getDefaultUser();

			_sxpElementLocalService.addSXPElement(
				user.getUserId(),
				LocalizedMapUtil.getLocalizedMap(
					sxpElement.getDescription_i18n()),
				String.valueOf(sxpElement.getElementDefinition()), true,
				LocalizedMapUtil.getLocalizedMap(sxpElement.getTitle_i18n()), 0,
				new ServiceContext() {
					{
						setAddGroupPermissions(true);
						setAddGuestPermissions(true);
						setCompanyId(company.getCompanyId());
						setScopeGroupId(company.getGroupId());
						setUserId(user.getUserId());
					}
				});
		}
	}

	@Reference
	private SXPElementLocalService _sxpElementLocalService;

}