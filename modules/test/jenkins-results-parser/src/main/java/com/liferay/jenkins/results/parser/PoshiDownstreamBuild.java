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

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public class PoshiDownstreamBuild extends DownstreamBuild {

	@Override
	public List<TestResult> getTestResults() {
		String status = getStatus();

		if ((status == null) || !status.equals("completed")) {
			return Collections.emptyList();
		}

		List<TestResult> testResults = new ArrayList<>();

		String result = getResult();

		if (result.equals("SUCCESS") || result.equals("UNSTABLE")) {
			testResults.addAll(super.getTestResults());
		}

		List<String> existingTestNames = new ArrayList<>();

		for (TestResult testResult : testResults) {
			String testName = testResult.getTestName();

			String testNameRegex = "test\\[([^\\]]+)\\]";

			if (!testName.matches(testNameRegex)) {
				continue;
			}

			existingTestNames.add(testName.replaceAll(testNameRegex, "$1"));
		}

		for (String poshiTestName : _getPoshiTestNames()) {
			if (existingTestNames.contains(poshiTestName)) {
				continue;
			}

			JSONObject caseJSONObject = new JSONObject();

			caseJSONObject.put(
				"className", "com.liferay.poshi.runner.PoshiRunner");
			caseJSONObject.put("duration", getDuration());
			caseJSONObject.put(
				"errorDetails", "The build failed prior to running the test.");
			caseJSONObject.put("errorStackTrace", "");
			caseJSONObject.put("name", "test[" + poshiTestName + "]");
			caseJSONObject.put("status", "FAILED");

			testResults.add(
				TestResultFactory.newTestResult(this, caseJSONObject));
		}

		return testResults;
	}

	protected PoshiDownstreamBuild(String url, TopLevelBuild topLevelBuild) {
		super(url, topLevelBuild);
	}

	private List<String> _getPoshiTestNames() {
		BuildDatabase buildDatabase = BuildDatabaseUtil.getBuildDatabase(
			getTopLevelBuild());

		Properties startProperties = buildDatabase.getProperties(
			getJobVariant() + "/start.properties");

		String runTestCaseMethodGroup = JenkinsResultsParserUtil.getProperty(
			startProperties, "RUN_TEST_CASE_METHOD_GROUP");

		String poshiTestNamesKey = JenkinsResultsParserUtil.combine(
			"RUN_TEST_CASE_METHOD_GROUP_", runTestCaseMethodGroup, "_",
			getAxisVariable());

		String poshiTestNames = JenkinsResultsParserUtil.getProperty(
			startProperties, poshiTestNamesKey);

		if (JenkinsResultsParserUtil.isNullOrEmpty(poshiTestNames)) {
			return new ArrayList<>();
		}

		return Lists.newArrayList(poshiTestNames.split(","));
	}

}