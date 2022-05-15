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

package com.liferay.segments.asah.connector.internal.configuration.provider;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.segments.asah.connector.internal.configuration.SegmentsAsahCompanyConfiguration;
import com.liferay.segments.asah.connector.internal.configuration.SegmentsAsahConfiguration;

import java.io.IOException;

import java.util.Map;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Cristina González
 */
@Component(
	configurationPid = "com.liferay.segments.asah.connector.internal.configuration.SegmentsAsahConfiguration",
	service = SegmentsAsahConfigurationProvider.class
)
public class SegmentsAsahConfigurationProvider {

	public int getAnonymousUserSegmentsCacheExpirationTime(long companyId)
		throws ConfigurationException {

		if (!_isSegmentsAsahCompanyConfigurationDefined(companyId)) {
			return _segmentsAsahConfiguration.
				anonymousUserSegmentsCacheExpirationTime();
		}

		SegmentsAsahCompanyConfiguration segmentsAsahCompanyConfiguration =
			_configurationProvider.getCompanyConfiguration(
				SegmentsAsahCompanyConfiguration.class, companyId);

		return segmentsAsahCompanyConfiguration.
			anonymousUserSegmentsCacheExpirationTime();
	}

	public int getInterestTermsCacheExpirationTime(long companyId)
		throws ConfigurationException {

		if (!_isSegmentsAsahCompanyConfigurationDefined(companyId)) {
			return _segmentsAsahConfiguration.
				interestTermsCacheExpirationTime();
		}

		SegmentsAsahCompanyConfiguration segmentsAsahCompanyConfiguration =
			_configurationProvider.getCompanyConfiguration(
				SegmentsAsahCompanyConfiguration.class, companyId);

		return segmentsAsahCompanyConfiguration.
			interestTermsCacheExpirationTime();
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_segmentsAsahConfiguration = ConfigurableUtil.createConfigurable(
			SegmentsAsahConfiguration.class, properties);
	}

	private boolean _isSegmentsAsahCompanyConfigurationDefined(long companyId)
		throws ConfigurationException {

		try {
			String filterString = StringBundler.concat(
				"(&(", ConfigurationAdmin.SERVICE_FACTORYPID, StringPool.EQUAL,
				SegmentsAsahConfiguration.class.getName(), ".scoped",
				")(companyId=", companyId, "))");

			Configuration[] configuration =
				_configurationAdmin.listConfigurations(filterString);

			if (configuration != null) {
				return true;
			}

			return false;
		}
		catch (InvalidSyntaxException | IOException exception) {
			throw new ConfigurationException(exception);
		}
	}

	@Reference
	private ConfigurationAdmin _configurationAdmin;

	@Reference
	private ConfigurationProvider _configurationProvider;

	private volatile SegmentsAsahConfiguration _segmentsAsahConfiguration;

}