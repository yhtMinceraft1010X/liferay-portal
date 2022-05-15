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

package com.liferay.jenkins.results.parser.testray;

import com.liferay.jenkins.results.parser.Build;
import com.liferay.jenkins.results.parser.DownstreamBuild;
import com.liferay.jenkins.results.parser.JenkinsMaster;
import com.liferay.jenkins.results.parser.JenkinsResultsParserUtil;
import com.liferay.jenkins.results.parser.Job;
import com.liferay.jenkins.results.parser.QAWebsitesGitRepositoryJob;
import com.liferay.jenkins.results.parser.RemoteExecutor;
import com.liferay.jenkins.results.parser.TopLevelBuild;
import com.liferay.jenkins.results.parser.job.property.JobProperty;
import com.liferay.jenkins.results.parser.job.property.JobPropertyFactory;
import com.liferay.jenkins.results.parser.test.clazz.group.AxisTestClassGroup;

import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.WordUtils;

/**
 * @author Michael Hashimoto
 */
public class BatchTestrayCaseResult extends TestrayCaseResult {

	public BatchTestrayCaseResult(
		TestrayBuild testrayBuild, TopLevelBuild topLevelBuild,
		AxisTestClassGroup axisTestClassGroup) {

		super(testrayBuild, topLevelBuild);

		_axisTestClassGroup = axisTestClassGroup;

		String workspace = System.getenv("WORKSPACE");

		if (JenkinsResultsParserUtil.isNullOrEmpty(workspace)) {
			throw new RuntimeException("Please set WORKSPACE");
		}

		_testrayUploadBaseDir = new File(
			workspace,
			"testray/" + JenkinsResultsParserUtil.getDistinctTimeStamp());
	}

	public String getAxisName() {
		return _axisTestClassGroup.getAxisName();
	}

	public String getBatchName() {
		return _axisTestClassGroup.getBatchName();
	}

	public Build getBuild() {
		TopLevelBuild topLevelBuild = getTopLevelBuild();

		DownstreamBuild downstreamBuild = topLevelBuild.getDownstreamBuild(
			getAxisName());

		if (downstreamBuild != null) {
			return downstreamBuild;
		}

		return topLevelBuild.getDownstreamAxisBuild(getAxisName());
	}

	@Override
	public String getComponentName() {
		try {
			return JenkinsResultsParserUtil.getProperty(
				JenkinsResultsParserUtil.getBuildProperties(),
				"testray.case.component", getBatchName());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	@Override
	public String getErrors() {
		Build build = getBuild();

		if (build == null) {
			return "Failed to run on CI";
		}

		if (!build.isFailing()) {
			return null;
		}

		String result = build.getResult();

		if (result == null) {
			return "Failed to finish build on CI";
		}

		if (result.equals("ABORTED")) {
			return "Aborted prior to running test";
		}

		String errorMessage = build.getFailureMessage();

		if (JenkinsResultsParserUtil.isNullOrEmpty(errorMessage)) {
			return "Failed for unknown reason";
		}

		if (errorMessage.contains("\n")) {
			errorMessage = errorMessage.substring(
				0, errorMessage.indexOf("\n"));
		}

		errorMessage = errorMessage.trim();

		if (JenkinsResultsParserUtil.isNullOrEmpty(errorMessage)) {
			return "Failed for unknown reason";
		}

		return errorMessage;
	}

	@Override
	public String getName() {
		return getAxisName();
	}

	@Override
	public int getPriority() {
		try {
			String testrayCasePriority = JenkinsResultsParserUtil.getProperty(
				JenkinsResultsParserUtil.getBuildProperties(),
				"testray.case.priority", getBatchName());

			if ((testrayCasePriority != null) &&
				testrayCasePriority.matches("\\d+")) {

				return Integer.parseInt(testrayCasePriority);
			}

			return 5;
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	@Override
	public Status getStatus() {
		Build build = getBuild();

		if (build == null) {
			return Status.UNTESTED;
		}

		if (build.isFailing()) {
			return Status.FAILED;
		}

		return Status.PASSED;
	}

	@Override
	public String getTeamName() {
		JobProperty teamNamesJobProperty = _getJobProperty(
			"testray.team.names");

		String teamNames = teamNamesJobProperty.getValue();

		if (JenkinsResultsParserUtil.isNullOrEmpty(teamNames)) {
			try {
				return JenkinsResultsParserUtil.getProperty(
					JenkinsResultsParserUtil.getBuildProperties(),
					"testray.case.team", getBatchName());
			}
			catch (IOException ioException) {
				throw new RuntimeException(ioException);
			}
		}

		String componentName = getComponentName();

		for (String teamName : teamNames.split(",")) {
			JobProperty teamComponentNamesJobProperty = _getJobProperty(
				"testray.team." + teamName + ".component.names");

			String teamComponentNames =
				teamComponentNamesJobProperty.getValue();

			if (JenkinsResultsParserUtil.isNullOrEmpty(teamComponentNames)) {
				continue;
			}

			for (String teamComponentName : teamComponentNames.split(",")) {
				if (teamComponentName.equals(componentName)) {
					teamName = teamName.replace("-", " ");

					return WordUtils.capitalize(teamName);
				}
			}
		}

		try {
			return JenkinsResultsParserUtil.getProperty(
				JenkinsResultsParserUtil.getBuildProperties(),
				"testray.case.team", getBatchName());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	@Override
	public List<TestrayAttachment> getTestrayAttachments() {
		List<TestrayAttachment> testrayAttachments = new ArrayList<>();

		testrayAttachments.add(_getBuildResultTopLevelTestrayAttachment());
		testrayAttachments.add(_getJobSummaryTestrayAttachment());
		testrayAttachments.add(_getJenkinsConsoleTestrayAttachment());
		testrayAttachments.add(_getJenkinsConsoleTopLevelTestrayAttachment());
		testrayAttachments.add(_getJenkinsReportTestrayAttachment());

		testrayAttachments.removeAll(Collections.singleton(null));

		return testrayAttachments;
	}

	@Override
	public String getType() {
		try {
			return JenkinsResultsParserUtil.getProperty(
				JenkinsResultsParserUtil.getBuildProperties(),
				"testray.case.type", getBatchName());
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	@Override
	public String[] getWarnings() {
		return null;
	}

	protected String getAxisBuildURLPath() {
		return JenkinsResultsParserUtil.combine(
			_getTopLevelBuildURLPath(), "/", getAxisName());
	}

	protected AxisTestClassGroup getAxisTestClassGroup() {
		return _axisTestClassGroup;
	}

	protected TestrayAttachment getTestrayAttachment(
		Build build, String name, String key) {

		if ((build == null) || JenkinsResultsParserUtil.isNullOrEmpty(key) ||
			JenkinsResultsParserUtil.isNullOrEmpty(name)) {

			return null;
		}

		for (URL testrayAttachmentURL : build.getTestrayAttachmentURLs()) {
			String testrayAttachmentURLString = String.valueOf(
				testrayAttachmentURL);

			if (!testrayAttachmentURLString.contains(key)) {
				continue;
			}

			return new DefaultTestrayAttachment(
				this, name, key, testrayAttachmentURL);
		}

		if (TestrayS3Bucket.googleCredentialsAvailable()) {
			for (URL testrayS3AttachmentURL :
					build.getTestrayS3AttachmentURLs()) {

				String testrayS3AttachmentURLString = String.valueOf(
					testrayS3AttachmentURL);

				if (!testrayS3AttachmentURLString.contains(key)) {
					continue;
				}

				return new S3TestrayAttachment(this, name, key);
			}
		}

		return null;
	}

	private TestrayAttachment _getBuildResultTopLevelTestrayAttachment() {
		return getTestrayAttachment(
			getTopLevelBuild(), "Build Result (Top Level)",
			_getTopLevelBuildURLPath() + "/build-result.json.gz");
	}

	private TestrayAttachment _getJenkinsConsoleTestrayAttachment() {
		String name = "Jenkins Console";
		String key = getAxisBuildURLPath() + "/jenkins-console.txt.gz";

		TestrayAttachment testrayAttachment = getTestrayAttachment(
			getBuild(), name, key);

		if (testrayAttachment != null) {
			return testrayAttachment;
		}

		Build build = getBuild();

		if (build == null) {
			return null;
		}

		File jenkinsConsoleFile = new File(
			_testrayUploadBaseDir, "jenkins-console.txt");
		File jenkinsConsoleGzFile = new File(
			_testrayUploadBaseDir, "jenkins-console.txt.gz");

		try {
			JenkinsResultsParserUtil.write(
				jenkinsConsoleFile, build.getConsoleText());

			JenkinsResultsParserUtil.gzip(
				jenkinsConsoleFile, jenkinsConsoleGzFile);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
		finally {
			JenkinsResultsParserUtil.delete(jenkinsConsoleFile);
		}

		if (!jenkinsConsoleGzFile.exists()) {
			return null;
		}

		if (_testrayAttachments.containsKey(key)) {
			return _testrayAttachments.get(key);
		}

		testrayAttachment = _uploadDefaultTestrayAttachment(
			name, key, jenkinsConsoleGzFile);

		if (testrayAttachment == null) {
			testrayAttachment = _uploadS3TestrayAttachment(
				name, key, jenkinsConsoleGzFile);
		}

		if (testrayAttachment == null) {
			return testrayAttachment;
		}

		_testrayAttachments.put(key, testrayAttachment);

		return testrayAttachment;
	}

	private TestrayAttachment _getJenkinsConsoleTopLevelTestrayAttachment() {
		return getTestrayAttachment(
			getTopLevelBuild(), "Jenkins Console (Top Level)",
			_getTopLevelBuildURLPath() + "/jenkins-console.txt.gz");
	}

	private TestrayAttachment _getJenkinsReportTestrayAttachment() {
		return getTestrayAttachment(
			getTopLevelBuild(), "Jenkins Report (Top Level)",
			_getTopLevelBuildURLPath() + "/jenkins-report.html.gz");
	}

	private JobProperty _getJobProperty(String basePropertyName) {
		TopLevelBuild topLevelBuild = getTopLevelBuild();

		Job job = topLevelBuild.getJob();

		if (job instanceof QAWebsitesGitRepositoryJob) {
			AxisTestClassGroup axisTestClassGroup = getAxisTestClassGroup();

			return JobPropertyFactory.newJobProperty(
				basePropertyName, job, axisTestClassGroup.getTestBaseDir(),
				JobProperty.Type.QA_WEBSITES_TEST_DIR);
		}

		return JobPropertyFactory.newJobProperty(basePropertyName, job);
	}

	private TestrayAttachment _getJobSummaryTestrayAttachment() {
		return getTestrayAttachment(
			getTopLevelBuild(), "Job Summary (Top Level)",
			_getTopLevelBuildURLPath() + "/job-summary/index.html.gz");
	}

	private String _getMasterHostname() {
		Build build = getBuild();

		JenkinsMaster jenkinsMaster = build.getJenkinsMaster();

		return jenkinsMaster.getName();
	}

	private String _getTestrayMountDirPath() {
		try {
			return JenkinsResultsParserUtil.getBuildProperty(
				"testray.server.mount.dir[testray-1]");
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private String _getTopLevelBuildURLPath() {
		TopLevelBuild topLevelBuild = getTopLevelBuild();

		if (topLevelBuild == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		Date date = new Date(topLevelBuild.getStartTime());

		sb.append(
			JenkinsResultsParserUtil.toDateString(
				date, "yyyy-MM", "America/Los_Angeles"));

		sb.append("/");

		JenkinsMaster jenkinsMaster = topLevelBuild.getJenkinsMaster();

		sb.append(jenkinsMaster.getName());

		sb.append("/");
		sb.append(topLevelBuild.getJobName());
		sb.append("/");
		sb.append(topLevelBuild.getBuildNumber());

		return sb.toString();
	}

	private TestrayAttachment _uploadDefaultTestrayAttachment(
		String name, String key, File file) {

		if (!file.exists()) {
			return null;
		}

		String parentKey = key.replaceAll("(.+)/[^/]+", "$1");

		RemoteExecutor remoteExecutor = new RemoteExecutor();

		try {
			remoteExecutor.execute(
				1, new String[] {"root@" + _getMasterHostname()},
				new String[] {
					JenkinsResultsParserUtil.combine(
						"mkdir -p \"", _getTestrayMountDirPath(),
						"/jenkins/testray-results/production/logs/", parentKey,
						"\"")
				});
		}
		catch (Exception exception) {
			return null;
		}

		try {
			JenkinsResultsParserUtil.executeBashCommands(
				JenkinsResultsParserUtil.combine(
					"rsync -aqz --chmod=go=rx \"",
					JenkinsResultsParserUtil.getCanonicalPath(file), "\" \"",
					_getMasterHostname(), "::testray-results/production/logs/",
					parentKey, "/\""));
		}
		catch (IOException | TimeoutException exception) {
			return null;
		}

		try {
			TestrayServer testrayServer = getTestrayServer();

			URL url = new URL(
				JenkinsResultsParserUtil.combine(
					String.valueOf(testrayServer.getURL()),
					"/reports/production/logs/", key));

			System.out.println("Uploaded " + url);

			return new DefaultTestrayAttachment(this, name, key, url);
		}
		catch (MalformedURLException malformedURLException) {
			return null;
		}
	}

	private TestrayAttachment _uploadS3TestrayAttachment(
		String name, String key, File file) {

		if (!file.exists()) {
			return null;
		}

		try {
			TestrayS3Bucket testrayS3Bucket = TestrayS3Bucket.getInstance();

			testrayS3Bucket.createTestrayS3Object(key, file);

			return new S3TestrayAttachment(this, name, key);
		}
		catch (Exception exception) {
			return null;
		}
	}

	private static final Map<String, TestrayAttachment> _testrayAttachments =
		new HashMap<>();

	private final AxisTestClassGroup _axisTestClassGroup;
	private final File _testrayUploadBaseDir;

}