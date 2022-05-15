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
import com.liferay.jenkins.results.parser.job.property.JobPropertyFactory;
import com.liferay.jenkins.results.parser.test.clazz.group.AxisTestClassGroup;
import com.liferay.jenkins.results.parser.test.clazz.group.BatchTestClassGroup;
import com.liferay.jenkins.results.parser.test.clazz.group.FunctionalBatchTestClassGroup;
import com.liferay.jenkins.results.parser.test.clazz.group.FunctionalSegmentTestClassGroup;
import com.liferay.jenkins.results.parser.test.clazz.group.SegmentTestClassGroup;
import com.liferay.jenkins.results.parser.test.clazz.group.TestClassGroupFactory;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public abstract class BaseJob implements Job {

	@Override
	public int getAxisCount() {
		List<AxisTestClassGroup> axisTestClassGroups = getAxisTestClassGroups();

		if (axisTestClassGroups == null) {
			return 0;
		}

		return axisTestClassGroups.size();
	}

	@Override
	public List<AxisTestClassGroup> getAxisTestClassGroups() {
		List<AxisTestClassGroup> axisTestClassGroups = new ArrayList<>();

		for (BatchTestClassGroup batchTestClassGroup :
				getBatchTestClassGroups()) {

			axisTestClassGroups.addAll(
				batchTestClassGroup.getAxisTestClassGroups());
		}

		return axisTestClassGroups;
	}

	@Override
	public Set<String> getBatchNames() {
		Set<String> batchNames = new TreeSet<>();

		for (BatchTestClassGroup batchTestClassGroup :
				getBatchTestClassGroups()) {

			batchNames.add(batchTestClassGroup.getBatchName());
		}

		return batchNames;
	}

	@Override
	public List<BatchTestClassGroup> getBatchTestClassGroups() {
		if (_batchTestClassGroups != null) {
			return _batchTestClassGroups;
		}

		if ((jsonObject != null) && jsonObject.has("batches")) {
			_batchTestClassGroups = new ArrayList<>();

			JSONArray batchesJSONArray = jsonObject.getJSONArray("batches");

			for (int i = 0; i < batchesJSONArray.length(); i++) {
				JSONObject batchJSONObject = batchesJSONArray.getJSONObject(i);

				if (batchJSONObject == null) {
					continue;
				}

				_batchTestClassGroups.add(
					TestClassGroupFactory.newBatchTestClassGroup(
						this, batchJSONObject));
			}

			return _batchTestClassGroups;
		}

		_batchTestClassGroups = getBatchTestClassGroups(getRawBatchNames());

		if (jsonObject != null) {
			JSONArray batchesJSONArray = new JSONArray();

			for (BatchTestClassGroup batchTestClassGroup :
					_batchTestClassGroups) {

				batchesJSONArray.put(batchTestClassGroup.getJSONObject());
			}

			jsonObject.put("batches", batchesJSONArray);
		}

		return _batchTestClassGroups;
	}

	@Override
	public List<Build> getBuildHistory(JenkinsMaster jenkinsMaster) {
		JSONObject jobJSONObject = getJobJSONObject(
			jenkinsMaster, "builds[number]");

		JSONArray buildsJSONArray = jobJSONObject.getJSONArray("builds");

		List<Build> builds = new ArrayList<>(buildsJSONArray.length());

		for (int i = 0; i < buildsJSONArray.length(); i++) {
			JSONObject buildJSONObject = buildsJSONArray.getJSONObject(i);

			builds.add(
				BuildFactory.newBuild(
					JenkinsResultsParserUtil.combine(
						jenkinsMaster.getURL(), "/job/", getJobName(), "/",
						String.valueOf(buildJSONObject.getInt("number"))),
					null));
		}

		return builds;
	}

	@Override
	public BuildProfile getBuildProfile() {
		return _buildProfile;
	}

	@Override
	public List<AxisTestClassGroup> getDependentAxisTestClassGroups() {
		List<AxisTestClassGroup> axisTestClassGroups = new ArrayList<>();

		for (BatchTestClassGroup batchTestClassGroup :
				getDependentBatchTestClassGroups()) {

			axisTestClassGroups.addAll(
				batchTestClassGroup.getAxisTestClassGroups());
		}

		return axisTestClassGroups;
	}

	@Override
	public Set<String> getDependentBatchNames() {
		Set<String> batchNames = new TreeSet<>();

		for (BatchTestClassGroup batchTestClassGroup :
				getDependentBatchTestClassGroups()) {

			batchNames.add(batchTestClassGroup.getBatchName());
		}

		return batchNames;
	}

	@Override
	public List<BatchTestClassGroup> getDependentBatchTestClassGroups() {
		if (_dependentBatchTestClassGroups != null) {
			return _dependentBatchTestClassGroups;
		}

		if ((jsonObject != null) && jsonObject.has("smoke_batches")) {
			_dependentBatchTestClassGroups = new ArrayList<>();

			JSONArray smokeBatchesJSONArray = jsonObject.getJSONArray(
				"smoke_batches");

			for (int i = 0; i < smokeBatchesJSONArray.length(); i++) {
				JSONObject smokeBatchJSONObject =
					smokeBatchesJSONArray.getJSONObject(i);

				if (smokeBatchJSONObject == null) {
					continue;
				}

				_dependentBatchTestClassGroups.add(
					TestClassGroupFactory.newBatchTestClassGroup(
						this, smokeBatchJSONObject));
			}

			return _dependentBatchTestClassGroups;
		}

		_dependentBatchTestClassGroups = getBatchTestClassGroups(
			getRawDependentBatchNames());

		if (jsonObject != null) {
			JSONArray smokeBatchesJSONArray = new JSONArray();

			for (BatchTestClassGroup batchTestClassGroup :
					_dependentBatchTestClassGroups) {

				smokeBatchesJSONArray.put(batchTestClassGroup.getJSONObject());
			}

			jsonObject.put("smoke_batches", smokeBatchesJSONArray);
		}

		return _dependentBatchTestClassGroups;
	}

	@Override
	public Set<String> getDependentSegmentNames() {
		Set<String> segmentNames = new TreeSet<>();

		for (SegmentTestClassGroup segmentTestClassGroup :
				getDependentSegmentTestClassGroups()) {

			segmentNames.add(segmentTestClassGroup.getSegmentName());
		}

		return segmentNames;
	}

	@Override
	public List<SegmentTestClassGroup> getDependentSegmentTestClassGroups() {
		List<SegmentTestClassGroup> segmentTestClassGroups = new ArrayList<>();

		for (BatchTestClassGroup batchTestClassGroup :
				getDependentBatchTestClassGroups()) {

			segmentTestClassGroups.addAll(
				batchTestClassGroup.getSegmentTestClassGroups());
		}

		return segmentTestClassGroups;
	}

	@Override
	public List<String> getDistNodes() {
		try {
			List<JenkinsMaster> jenkinsMasters =
				JenkinsResultsParserUtil.getJenkinsMasters(
					JenkinsResultsParserUtil.getBuildProperties(),
					_getSlaveRAMMinimumDefault(), _getSlavesPerHostDefault(),
					JenkinsResultsParserUtil.getCohortName());

			int axisCount = getAxisCount();
			int distNodeAxisCount = _getDistNodeAxisCount();

			int distNodeCount = axisCount / distNodeAxisCount;

			if ((axisCount % distNodeAxisCount) > 0) {
				distNodeCount++;
			}

			distNodeCount = Math.min(distNodeCount, jenkinsMasters.size());

			distNodeCount = Math.max(distNodeCount, _getDistNodeCountMinimum());

			List<JenkinsSlave> jenkinsSlaves =
				JenkinsResultsParserUtil.getReachableJenkinsSlaves(
					jenkinsMasters, distNodeCount);

			List<String> distNodes = new ArrayList<>();

			for (JenkinsSlave jenkinsSlave : jenkinsSlaves) {
				distNodes.add(jenkinsSlave.getName());
			}

			return distNodes;
		}
		catch (IOException ioException) {
			return new ArrayList<>();
		}
	}

	@Override
	public DistType getDistType() {
		return DistType.CI;
	}

	@Override
	public Set<String> getDistTypes() {
		JobProperty jobProperty = getJobProperty("test.batch.dist.app.servers");

		return getSetFromString(jobProperty.getValue());
	}

	@Override
	public Set<String> getDistTypesExcludingTomcat() {
		Set<String> distTypesExcludingTomcat = new TreeSet<>(getDistTypes());

		distTypesExcludingTomcat.remove("tomcat");

		return distTypesExcludingTomcat;
	}

	@Override
	public String getJobName() {
		return _jobName;
	}

	@Override
	public List<File> getJobPropertiesFiles() {
		return jobPropertiesFiles;
	}

	@Override
	public List<String> getJobPropertyOptions() {
		List<String> jobPropertyOptions = new ArrayList<>();

		jobPropertyOptions.add(String.valueOf(getBuildProfile()));

		String jobName = getJobName();

		jobPropertyOptions.add(jobName);

		if (jobName.contains("(")) {
			jobPropertyOptions.add(jobName.substring(0, jobName.indexOf("(")));
		}

		jobPropertyOptions.removeAll(Collections.singleton(null));

		return jobPropertyOptions;
	}

	@Override
	public String getJobURL(JenkinsMaster jenkinsMaster) {
		return JenkinsResultsParserUtil.combine(
			jenkinsMaster.getURL(), "/job/", _jobName);
	}

	@Override
	public JSONObject getJSONObject() {
		if (jsonObject != null) {
			return jsonObject;
		}

		jsonObject = new JSONObject();

		List<BatchTestClassGroup> batchTestClassGroups =
			getBatchTestClassGroups();

		if ((batchTestClassGroups != null) && !batchTestClassGroups.isEmpty()) {
			JSONArray batchesJSONArray = new JSONArray();

			for (BatchTestClassGroup batchTestClassGroup :
					batchTestClassGroups) {

				batchesJSONArray.put(batchTestClassGroup.getJSONObject());
			}

			jsonObject.put("batches", batchesJSONArray);
		}

		jsonObject.put("build_profile", String.valueOf(getBuildProfile()));
		jsonObject.put("job_name", getJobName());
		jsonObject.put("job_properties", _getJobPropertiesMap());
		jsonObject.put("job_property_options", getJobPropertyOptions());

		List<BatchTestClassGroup> dependentBatchTestClassGroups =
			getDependentBatchTestClassGroups();

		if ((dependentBatchTestClassGroups != null) &&
			!dependentBatchTestClassGroups.isEmpty()) {

			JSONArray smokeBatchesJSONArray = new JSONArray();

			for (BatchTestClassGroup batchTestClassGroup :
					dependentBatchTestClassGroups) {

				smokeBatchesJSONArray.put(batchTestClassGroup.getJSONObject());
			}

			jsonObject.put("smoke_batches", smokeBatchesJSONArray);
		}

		if (this instanceof TestSuiteJob) {
			TestSuiteJob testSuiteJob = (TestSuiteJob)this;

			jsonObject.put("test_suite_name", testSuiteJob.getTestSuiteName());
		}

		return jsonObject;
	}

	@Override
	public Set<String> getSegmentNames() {
		Set<String> segmentNames = new TreeSet<>();

		for (SegmentTestClassGroup segmentTestClassGroup :
				getSegmentTestClassGroups()) {

			segmentNames.add(segmentTestClassGroup.getSegmentName());
		}

		return segmentNames;
	}

	@Override
	public List<SegmentTestClassGroup> getSegmentTestClassGroups() {
		List<SegmentTestClassGroup> segmentTestClassGroups = new ArrayList<>();

		for (BatchTestClassGroup batchTestClassGroup :
				getBatchTestClassGroups()) {

			segmentTestClassGroups.addAll(
				batchTestClassGroup.getSegmentTestClassGroups());
		}

		return segmentTestClassGroups;
	}

	@Override
	public String getTestPropertiesContent() {
		Map<String, Properties> propertiesMap = new HashMap<>();

		List<BatchTestClassGroup> batchTestClassGroups = new ArrayList<>(
			getBatchTestClassGroups());

		batchTestClassGroups.addAll(getDependentBatchTestClassGroups());

		for (BatchTestClassGroup batchTestClassGroup : batchTestClassGroups) {
			Properties batchProperties = new Properties();

			batchProperties.setProperty(
				"test.batch.cohort.name", batchTestClassGroup.getCohortName());
			batchProperties.setProperty(
				"test.batch.job.name", batchTestClassGroup.getBatchJobName());
			batchProperties.setProperty(
				"test.batch.maximum.slaves.per.host",
				String.valueOf(batchTestClassGroup.getMaximumSlavesPerHost()));
			batchProperties.setProperty(
				"test.batch.minimum.slave.ram",
				String.valueOf(batchTestClassGroup.getMinimumSlaveRAM()));
			batchProperties.setProperty(
				"test.batch.slave.label", batchTestClassGroup.getSlaveLabel());

			if (batchTestClassGroup instanceof FunctionalBatchTestClassGroup) {
				FunctionalBatchTestClassGroup functionalBatchTestClassGroup =
					(FunctionalBatchTestClassGroup)batchTestClassGroup;

				String testBatchRunPropertyQuery =
					functionalBatchTestClassGroup.
						getTestBatchRunPropertyQuery();

				if (testBatchRunPropertyQuery != null) {
					batchProperties.setProperty(
						"test.batch.run.property.query",
						testBatchRunPropertyQuery);
				}
			}
			else {
				batchProperties.setProperty(
					"test.batch.size",
					String.valueOf(batchTestClassGroup.getAxisCount()));
			}

			if (isDownstreamEnabled()) {
				batchProperties.setProperty(
					"test.downstream.job.name",
					batchTestClassGroup.getDownstreamJobName());
			}

			propertiesMap.put(
				batchTestClassGroup.getBatchName(), batchProperties);

			for (int i = 0; i < batchTestClassGroup.getSegmentCount(); i++) {
				Properties segmentProperties = new Properties();

				SegmentTestClassGroup segmentTestClassGroup =
					batchTestClassGroup.getSegmentTestClassGroup(i);

				segmentProperties.setProperty(
					"test.batch.cohort.name",
					segmentTestClassGroup.getCohortName());
				segmentProperties.setProperty(
					"test.batch.job.name",
					segmentTestClassGroup.getBatchJobName());
				segmentProperties.setProperty(
					"test.batch.maximum.slaves.per.host",
					String.valueOf(
						segmentTestClassGroup.getMaximumSlavesPerHost()));
				segmentProperties.setProperty(
					"test.batch.minimum.slave.ram",
					String.valueOf(segmentTestClassGroup.getMinimumSlaveRAM()));
				segmentProperties.setProperty(
					"test.batch.name", segmentTestClassGroup.getBatchName());
				segmentProperties.setProperty(
					"test.batch.size",
					String.valueOf(segmentTestClassGroup.getAxisCount()));
				segmentProperties.setProperty(
					"test.batch.slave.label",
					segmentTestClassGroup.getSlaveLabel());

				String testCasePropertiesContent =
					segmentTestClassGroup.getTestCasePropertiesContent();

				if (testCasePropertiesContent != null) {
					testCasePropertiesContent =
						testCasePropertiesContent.replaceAll(
							"\n", "\\${line.separator}");

					segmentProperties.setProperty(
						"test.case.properties", testCasePropertiesContent);
				}

				if (isDownstreamEnabled()) {
					segmentProperties.setProperty(
						"test.downstream.job.name",
						segmentTestClassGroup.getDownstreamJobName());
				}

				if (segmentTestClassGroup instanceof
						FunctionalSegmentTestClassGroup) {

					segmentProperties.setProperty(
						"run.test.case.method.group", String.valueOf(i));
				}

				propertiesMap.put(
					segmentTestClassGroup.getSegmentName(), segmentProperties);
			}
		}

		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, Properties> propertiesEntry :
				propertiesMap.entrySet()) {

			Properties properties = propertiesEntry.getValue();

			for (String propertyName : properties.stringPropertyNames()) {
				sb.append(propertyName);
				sb.append("[");
				sb.append(propertiesEntry.getKey());
				sb.append("]=");
				sb.append(properties.getProperty(propertyName));
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	@Override
	public boolean isDownstreamEnabled() {
		JobProperty jobProperty = getJobProperty(
			"test.batch.downstream.enabled");

		String downstreamEnabled = jobProperty.getValue();

		if ((downstreamEnabled != null) && downstreamEnabled.equals("true")) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isSegmentEnabled() {
		JobProperty jobProperty = getJobProperty("test.batch.segment.enabled");

		String segmentEnabled = jobProperty.getValue();

		if ((segmentEnabled != null) && segmentEnabled.equals("true")) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isValidationRequired() {
		return false;
	}

	@Override
	public boolean testReleaseBundle() {
		JobProperty jobProperty = getJobProperty("test.release.bundle");

		if (jobProperty != null) {
			recordJobProperty(jobProperty);

			return Boolean.parseBoolean(jobProperty.getValue());
		}

		return false;
	}

	@Override
	public boolean testRelevantChanges() {
		JobProperty jobProperty = getJobProperty("test.relevant.changes");

		if (jobProperty != null) {
			recordJobProperty(jobProperty);

			return Boolean.parseBoolean(jobProperty.getValue());
		}

		return false;
	}

	protected BaseJob(BuildProfile buildProfile, String jobName) {
		_buildProfile = buildProfile;
		_jobName = jobName;
	}

	protected BaseJob(JSONObject jsonObject) {
		this.jsonObject = jsonObject;

		_buildProfile = BuildProfile.getByString(
			jsonObject.getString("build_profile"));
		_jobName = jsonObject.getString("job_name");
	}

	protected List<BatchTestClassGroup> getBatchTestClassGroups(
		Set<String> rawBatchNames) {

		if ((rawBatchNames == null) || rawBatchNames.isEmpty()) {
			return new ArrayList<>();
		}

		long start = JenkinsResultsParserUtil.getCurrentTimeMillis();

		System.out.println(
			JenkinsResultsParserUtil.combine(
				"Started creating ", String.valueOf(rawBatchNames.size()),
				" batch test class groups at ",
				JenkinsResultsParserUtil.toDateString(new Date(start))));

		List<Callable<BatchTestClassGroup>> callables = new ArrayList<>();

		final Job job = this;

		for (final String batchName : rawBatchNames) {
			callables.add(
				new Callable<BatchTestClassGroup>() {

					@Override
					public BatchTestClassGroup call() throws Exception {
						for (int i = 0; i < _pauseRetryCount; i++) {
							try {
								return _call();
							}
							catch (Exception exception) {
								System.out.println(
									JenkinsResultsParserUtil.combine(
										"Retry creating a test class group in ",
										String.valueOf(
											_pauseRetryDuration / 1000),
										" seconds."));

								JenkinsResultsParserUtil.sleep(
									_pauseRetryDuration);
							}
						}

						return _call();
					}

					private BatchTestClassGroup _call() throws Exception {
						long start =
							JenkinsResultsParserUtil.getCurrentTimeMillis();

						System.out.println(
							JenkinsResultsParserUtil.combine(
								"[", batchName, "] Started batch test class ",
								"group at ",
								JenkinsResultsParserUtil.toDateString(
									new Date(start))));

						BatchTestClassGroup batchTestClassGroup =
							TestClassGroupFactory.newBatchTestClassGroup(
								batchName, job);

						long duration =
							JenkinsResultsParserUtil.getCurrentTimeMillis() -
								start;

						System.out.println(
							JenkinsResultsParserUtil.combine(
								"[", batchName, "] Completed batch test class ",
								"group in ",
								JenkinsResultsParserUtil.toDurationString(
									duration),
								" at ",
								JenkinsResultsParserUtil.toDateString(
									new Date())));

						if (batchTestClassGroup.getAxisCount() <= 0) {
							return null;
						}

						return batchTestClassGroup;
					}

					private final Integer _pauseRetryCount = 2;
					private final Integer _pauseRetryDuration = 5000;

				});
		}

		ParallelExecutor<BatchTestClassGroup> parallelExecutor =
			new ParallelExecutor<>(callables, _executorService);

		List<BatchTestClassGroup> batchTestClassGroups =
			parallelExecutor.execute();

		batchTestClassGroups.removeAll(Collections.singleton(null));

		System.out.println(
			JenkinsResultsParserUtil.combine(
				"Completed creating ",
				String.valueOf(batchTestClassGroups.size()),
				" batch test class groups in ",
				JenkinsResultsParserUtil.toDurationString(
					JenkinsResultsParserUtil.getCurrentTimeMillis() - start),
				" at ", JenkinsResultsParserUtil.toDateString(new Date())));

		return batchTestClassGroups;
	}

	protected JSONObject getJobJSONObject(
		JenkinsMaster jenkinsMaster, String tree) {

		if (getJobURL(jenkinsMaster) == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();

		sb.append(
			JenkinsResultsParserUtil.getLocalURL(getJobURL(jenkinsMaster)));
		sb.append("/api/json?pretty");

		if (tree != null) {
			sb.append("&tree=");
			sb.append(tree);
		}

		try {
			return JenkinsResultsParserUtil.toJSONObject(sb.toString(), false);
		}
		catch (IOException ioException) {
			throw new RuntimeException("Unable to get job JSON", ioException);
		}
	}

	protected JobProperty getJobProperty(String basePropertyName) {
		return JobPropertyFactory.newJobProperty(
			basePropertyName, null, null, this, null, null, true);
	}

	protected JobProperty getJobProperty(
		String basePropertyName, boolean useBasePropertyName) {

		return JobPropertyFactory.newJobProperty(
			basePropertyName, null, null, this, null, null,
			useBasePropertyName);
	}

	protected Set<String> getRawBatchNames() {
		JobProperty jobProperty = getJobProperty("test.batch.names");

		recordJobProperty(jobProperty);

		return getSetFromString(jobProperty.getValue());
	}

	protected Set<String> getRawDependentBatchNames() {
		JobProperty jobProperty = getJobProperty("test.batch.names.smoke");

		recordJobProperty(jobProperty);

		return getSetFromString(jobProperty.getValue());
	}

	protected Set<String> getSetFromString(String string) {
		Set<String> set = new TreeSet<>();

		if (JenkinsResultsParserUtil.isNullOrEmpty(string)) {
			return set;
		}

		for (String item : StringUtils.split(string, ",")) {
			if (item.startsWith("#")) {
				continue;
			}

			set.add(item.trim());
		}

		return set;
	}

	protected void recordJobProperty(JobProperty jobProperty) {
		if ((jobProperty == null) || _jobProperties.contains(jobProperty)) {
			return;
		}

		_jobProperties.add(jobProperty);
	}

	protected final List<File> jobPropertiesFiles = new ArrayList<>();
	protected JSONObject jsonObject;

	private int _getDistNodeAxisCount() {
		try {
			String distNodeAxisCount =
				JenkinsResultsParserUtil.getBuildProperty(
					"dist.node.axis.count");

			if (JenkinsResultsParserUtil.isInteger(distNodeAxisCount)) {
				return Integer.parseInt(distNodeAxisCount);
			}
		}
		catch (IOException ioException) {
		}

		return 25;
	}

	private int _getDistNodeCountMinimum() {
		try {
			String distNodeCountMinimum =
				JenkinsResultsParserUtil.getBuildProperty(
					"dist.node.count.minimum");

			if (JenkinsResultsParserUtil.isInteger(distNodeCountMinimum)) {
				return Integer.parseInt(distNodeCountMinimum);
			}
		}
		catch (IOException ioException) {
		}

		return 3;
	}

	private Map<String, Properties> _getJobPropertiesMap() {
		synchronized (_jobProperties) {
			if (!_initializeJobProperties) {
				getBatchTestClassGroups();

				getDependentBatchTestClassGroups();

				_initializeJobProperties = true;
			}
		}

		Map<String, Properties> jobPropertiesMap = new TreeMap<>();

		for (JobProperty jobProperty : _jobProperties) {
			if (jobProperty == null) {
				continue;
			}

			String jobPropertyValue = jobProperty.getValue();

			if (jobPropertyValue == null) {
				continue;
			}

			String propertiesFilePath = jobProperty.getPropertiesFilePath();

			Properties properties = jobPropertiesMap.get(propertiesFilePath);

			if (properties == null) {
				properties = new Properties();
			}

			properties.setProperty(jobProperty.getName(), jobPropertyValue);

			jobPropertiesMap.put(propertiesFilePath, properties);
		}

		return jobPropertiesMap;
	}

	private int _getSlaveRAMMinimumDefault() {
		try {
			String slaveRAMMinimumDefault =
				JenkinsResultsParserUtil.getBuildProperty(
					"slave.ram.minimum.default");

			if (JenkinsResultsParserUtil.isInteger(slaveRAMMinimumDefault)) {
				return Integer.parseInt(slaveRAMMinimumDefault);
			}
		}
		catch (IOException ioException) {
		}

		return 16;
	}

	private int _getSlavesPerHostDefault() {
		try {
			String slavesPerHostDefault =
				JenkinsResultsParserUtil.getBuildProperty(
					"slaves.per.host.default");

			if (JenkinsResultsParserUtil.isInteger(slavesPerHostDefault)) {
				return Integer.parseInt(slavesPerHostDefault);
			}
		}
		catch (IOException ioException) {
		}

		return 2;
	}

	private static final Integer _THREAD_COUNT = 20;

	private static final ExecutorService _executorService =
		JenkinsResultsParserUtil.getNewThreadPoolExecutor(_THREAD_COUNT, true);

	private List<BatchTestClassGroup> _batchTestClassGroups;
	private final BuildProfile _buildProfile;
	private List<BatchTestClassGroup> _dependentBatchTestClassGroups;
	private boolean _initializeJobProperties;
	private final String _jobName;
	private final List<JobProperty> _jobProperties = new ArrayList<>();

}