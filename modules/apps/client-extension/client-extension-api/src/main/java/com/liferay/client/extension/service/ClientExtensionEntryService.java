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

package com.liferay.client.extension.service;

import com.liferay.client.extension.model.ClientExtensionEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.portal.kernel.service.BaseService;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;

import java.util.Locale;
import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the remote service interface for ClientExtensionEntry. Methods of this
 * service are expected to have security checks based on the propagated JAAS
 * credentials because this service can be accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see ClientExtensionEntryServiceUtil
 * @generated
 */
@AccessControlled
@JSONWebService
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface ClientExtensionEntryService extends BaseService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>com.liferay.client.extension.service.impl.ClientExtensionEntryServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the client extension entry remote service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link ClientExtensionEntryServiceUtil} if injection and service tracking are not available.
	 */
	public ClientExtensionEntry addCustomElementClientExtensionEntry(
			String externalReferenceCode, String customElementCSSURLs,
			String customElementHTMLElementName, String customElementURLs,
			boolean customElementUseESM, String description,
			String friendlyURLMapping, boolean instanceable,
			Map<Locale, String> nameMap, String portletCategoryName,
			String properties, String sourceCodeURL)
		throws PortalException;

	public ClientExtensionEntry addIFrameClientExtensionEntry(
			String description, String friendlyURLMapping, String iFrameURL,
			boolean instanceable, Map<Locale, String> nameMap,
			String portletCategoryName, String properties, String sourceCodeURL)
		throws PortalException;

	public ClientExtensionEntry deleteClientExtensionEntry(
			long clientExtensionEntryId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ClientExtensionEntry getClientExtensionEntry(
			long clientExtensionEntryId)
		throws PortalException;

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	public ClientExtensionEntry updateCustomElementClientExtensionEntry(
			long clientExtensionEntryId, String customElementCSSURLs,
			String customElementHTMLElementName, String customElementURLs,
			boolean customElementUseESM, String description,
			String friendlyURLMapping, Map<Locale, String> nameMap,
			String portletCategoryName, String properties, String sourceCodeURL)
		throws PortalException;

	public ClientExtensionEntry updateIFrameClientExtensionEntry(
			long clientExtensionEntryId, String description,
			String friendlyURLMapping, String iFrameURL,
			Map<Locale, String> nameMap, String portletCategoryName,
			String properties, String sourceCodeURL)
		throws PortalException;

}