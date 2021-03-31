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

package com.liferay.osb.commerce.provisioning.internal.cloud.client.dto;

import com.liferay.petra.string.StringBundler;

/**
 * @author Ivica Cardic
 */
public class PortalInstance {

	public long getCompanyId() {
		return _companyId;
	}

	public String getDomain() {
		return _domain;
	}

	public String getPortalInstanceId() {
		return _portalInstanceId;
	}

	public String getVirtualHost() {
		return _virtualHost;
	}

	public boolean isActive() {
		return _active;
	}

	public void setActive(boolean active) {
		_active = active;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public void setDomain(String domain) {
		_domain = domain;
	}

	public void setPortalInstanceId(String portalInstanceId) {
		_portalInstanceId = portalInstanceId;
	}

	public void setVirtualHost(String virtualHost) {
		_virtualHost = virtualHost;
	}

	@Override
	public String toString() {
		return StringBundler.concat(
			"{\"active\":\"", _active, "\", \"companyId\":\"", _companyId,
			"\",\"domain\":\"", _domain, "\",\"portalInstanceId\":\"",
			_portalInstanceId, "\", \"virtualHost\":\"", _virtualHost, "\"}");
	}

	private boolean _active;
	private long _companyId;
	private String _domain;
	private String _portalInstanceId;
	private String _virtualHost;

}