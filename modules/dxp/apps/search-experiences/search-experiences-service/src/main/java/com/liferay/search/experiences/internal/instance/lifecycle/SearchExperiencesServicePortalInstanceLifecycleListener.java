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

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;
import com.liferay.search.experiences.rest.dto.v1_0.SXPElement;
import com.liferay.search.experiences.rest.dto.v1_0.util.SXPElementUtil;
import com.liferay.search.experiences.service.SXPElementLocalService;

import java.net.URL;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author André de Oliveira
 */
@Component(
	enabled = true, immediate = true,
	service = PortalInstanceLifecycleListener.class
)
public class SearchExperiencesServicePortalInstanceLifecycleListener
	extends BasePortalInstanceLifecycleListener {

	@Override
	public void portalInstanceRegistered(Company company) throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(getClass());

		Enumeration<URL> enumeration = bundle.findEntries(
			"/META-INF/elements", "*.json", false);

		if (enumeration == null) {
			return;
		}

		while (enumeration.hasMoreElements()) {
			_addSXPElement(company, enumeration.nextElement());
		}
	}

	private void _addSXPElement(Company company, URL url)
		throws Exception {

		String json = StringUtil.read(
			getClass(), StringPool.SLASH + url.getPath());

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
					setUserId(defaultUser.getUserId());
				}
			});
	}

	@Reference
	private SXPElementLocalService _sxpElementLocalService;

}