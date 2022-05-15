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

import java.io.File;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public class PortalAcceptancePullRequestJob
	extends PortalAcceptanceTestSuiteJob {

	public boolean isCentralMergePullRequest() {
		if (_centralMergePullRequest != null) {
			return _centralMergePullRequest;
		}

		GitWorkingDirectory gitWorkingDirectory = getGitWorkingDirectory();

		List<File> currentBranchModifiedFiles =
			gitWorkingDirectory.getModifiedFilesList();

		if (currentBranchModifiedFiles.size() == 1) {
			File modifiedFile = currentBranchModifiedFiles.get(0);

			String modifiedFileName = modifiedFile.getName();

			if (modifiedFileName.equals("ci-merge")) {
				_centralMergePullRequest = true;

				return _centralMergePullRequest;
			}
		}

		_centralMergePullRequest = false;

		return _centralMergePullRequest;
	}

	protected PortalAcceptancePullRequestJob(
		BuildProfile buildProfile, String jobName,
		PortalGitWorkingDirectory portalGitWorkingDirectory,
		String testSuiteName, String upstreamBranchName) {

		super(
			buildProfile, jobName, portalGitWorkingDirectory, testSuiteName,
			upstreamBranchName);
	}

	protected PortalAcceptancePullRequestJob(JSONObject jsonObject) {
		super(jsonObject);
	}

	@Override
	protected Set<String> getRawBatchNames() {
		Set<String> batchNames = super.getRawBatchNames();

		if (_isRelevantTestSuite() && _isPortalWebOnly()) {
			String[] portalWebOnlyBatchNameMarkers = {
				"compile-jsp", "functional", "portal-web", "source-format"
			};

			Set<String> portalWebOnlyBatchNames = new TreeSet<>();

			for (String batchName : batchNames) {
				for (String portalWebOnlyBatchNameMarker :
						portalWebOnlyBatchNameMarkers) {

					if (batchName.contains(portalWebOnlyBatchNameMarker)) {
						portalWebOnlyBatchNames.add(batchName);

						break;
					}
				}
			}

			return portalWebOnlyBatchNames;
		}

		return batchNames;
	}

	private boolean _isPortalWebOnly() {
		GitWorkingDirectory gitWorkingDirectory = getGitWorkingDirectory();

		File portalWebDirectory = new File(
			gitWorkingDirectory.getWorkingDirectory(), "portal-web");

		for (File modifiedFile : gitWorkingDirectory.getModifiedFilesList()) {
			if (!JenkinsResultsParserUtil.isFileInDirectory(
					portalWebDirectory, modifiedFile)) {

				return false;
			}
		}

		return true;
	}

	private boolean _isRelevantTestSuite() {
		String testSuiteName = getTestSuiteName();

		return testSuiteName.equals("relevant");
	}

	private Boolean _centralMergePullRequest;

}