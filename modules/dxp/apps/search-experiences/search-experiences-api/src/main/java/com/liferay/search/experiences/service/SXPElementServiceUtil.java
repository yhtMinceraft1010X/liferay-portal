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

package com.liferay.search.experiences.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.search.experiences.model.SXPElement;

import java.util.List;
import java.util.Map;

/**
 * Provides the remote service utility for SXPElement. This utility wraps
 * <code>com.liferay.search.experiences.service.impl.SXPElementServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see SXPElementService
 * @generated
 */
public class SXPElementServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.search.experiences.service.impl.SXPElementServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static SXPElement addSXPElement(
			Map<java.util.Locale, String> descriptionMap,
			String elementDefinitionJSON,
			Map<java.util.Locale, String> titleMap, int type,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addSXPElement(
			descriptionMap, elementDefinitionJSON, titleMap, type,
			serviceContext);
	}

	public static SXPElement deleteSXPElement(long sxpElementId)
		throws PortalException {

		return getService().deleteSXPElement(sxpElementId);
	}

	public static List<SXPElement> getGroupSXPElements(
		long companyId, int type, int start, int end) {

		return getService().getGroupSXPElements(companyId, type, start, end);
	}

	public static List<SXPElement> getGroupSXPElements(
		long groupId, int status, int type, int start, int end) {

		return getService().getGroupSXPElements(
			groupId, status, type, start, end);
	}

	public static int getGroupSXPElementsCount(
		long companyId, int status, int type) {

		return getService().getGroupSXPElementsCount(companyId, status, type);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static SXPElement getSXPElement(long sxpElementId)
		throws PortalException {

		return getService().getSXPElement(sxpElementId);
	}

	public static SXPElement updateSXPElement(
			long sxpElementId, Map<java.util.Locale, String> descriptionMap,
			String elementDefinitionJSON, boolean hidden,
			Map<java.util.Locale, String> titleMap,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateSXPElement(
			sxpElementId, descriptionMap, elementDefinitionJSON, hidden,
			titleMap, serviceContext);
	}

	public static SXPElementService getService() {
		return _service;
	}

	private static volatile SXPElementService _service;

}