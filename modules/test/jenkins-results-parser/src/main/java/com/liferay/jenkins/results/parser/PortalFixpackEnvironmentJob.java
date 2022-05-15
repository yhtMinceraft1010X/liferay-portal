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

package com.liferay.jenkins.results.parser;

import com.liferay.jenkins.results.parser.job.property.JobProperty;

import java.util.Set;

import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public class PortalFixpackEnvironmentJob extends PortalEnvironmentJob {

	public PortalFixpackEnvironmentJob(JSONObject jsonObject) {
		super(jsonObject);
	}

	protected PortalFixpackEnvironmentJob(
		BuildProfile buildProfile, String jobName,
		String portalUpstreamBranchName) {

		super(buildProfile, jobName, portalUpstreamBranchName);
	}

	@Override
	protected Set<String> getRawBatchNames() {
		JobProperty jobProperty = getJobProperty(
			"fixpack.environment.job.names");

		recordJobProperty(jobProperty);

		return getSetFromString(jobProperty.getValue());
	}

}