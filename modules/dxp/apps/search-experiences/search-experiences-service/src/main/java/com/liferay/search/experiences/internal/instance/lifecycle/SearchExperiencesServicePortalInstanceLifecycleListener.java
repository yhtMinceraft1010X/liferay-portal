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
		long groupId = company.getGroupId();
		User user = company.getDefaultUser();

		Stream<String> stream = _getSXPElementJSONStringsStream();

		RuntimeException runtimeException = new RuntimeException();

		stream.forEach(
			json -> {
				try {
					_importSXPElement(
						company.getCompanyId(), groupId, json,
						user.getUserId());
				}
				catch (Exception exception) {
					runtimeException.addSuppressed(exception);
				}
			});

		if (!ArrayUtil.isEmpty(runtimeException.getSuppressed())) {
			throw runtimeException;
		}
	}

	public interface SXPElementJSONStringsLookup {

		public Stream<String> getSXPElementJSONStrings();

	}

	public interface SXPElementLookup {

		public boolean exists(SXPElement sxpElement);

	}

	protected static final String ELEMENTS_PATH = "/META-INF/elements";

	private ServiceContext _createServiceContext(
		long companyId, long groupId, long userId) {

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setCompanyId(companyId);
		serviceContext.setScopeGroupId(groupId);
		serviceContext.setUserId(userId);

		return serviceContext;
	}

	private boolean _exists(long companyId, SXPElement sxpElement) {
		if (_sxpElementLookup != null) {
			return _sxpElementLookup.exists(sxpElement);
		}

		// TODO Fix performance issue

		return ListUtil.exists(
			_sxpElementLocalService.getSXPElements(companyId),
			modelSXPElement -> Objects.equals(
				MapUtil.getString(sxpElement.getTitle_i18n(), "en_US"),
				modelSXPElement.getTitle(LocaleUtil.US)));
	}

	private Stream<String> _getSXPElementJSONStringsStream() {
		if (_sxpElementJSONStringsLookup != null) {
			return _sxpElementJSONStringsLookup.getSXPElementJSONStrings();
		}

		Bundle bundle = FrameworkUtil.getBundle(getClass());

		Enumeration<URL> enumeration = bundle.findEntries(
			ELEMENTS_PATH, "*.json", false);

		if (enumeration == null) {
			return Stream.empty();
		}

		List<URL> urls = Collections.list(enumeration);

		return urls.stream(
		).map(
			url -> StringUtil.read(getClass(), StringPool.SLASH + url.getPath())
		);
	}

	private void _importSXPElement(
		long companyId, long groupId, String json, long userId) {

		SXPElement sxpElement = SXPElementUtil.toSXPElement(json);

		if (_exists(companyId, sxpElement)) {
			return;
		}

		try {
			_sxpElementLocalService.addSXPElement(
				userId,
				LocalizedMapUtil.getLocalizedMap(
					sxpElement.getDescription_i18n()),
				String.valueOf(sxpElement.getElementDefinition()), true,
				LocalizedMapUtil.getLocalizedMap(sxpElement.getTitle_i18n()), 0,
				_createServiceContext(companyId, groupId, userId));
		}
		catch (PortalException portalException) {
			ReflectionUtil.throwException(portalException);
		}
	}

	@Reference
	private JSONFactory _jsonFactory;

	private SXPElementJSONStringsLookup _sxpElementJSONStringsLookup;

	@Reference
	private SXPElementLocalService _sxpElementLocalService;

	private SXPElementLookup _sxpElementLookup;

}