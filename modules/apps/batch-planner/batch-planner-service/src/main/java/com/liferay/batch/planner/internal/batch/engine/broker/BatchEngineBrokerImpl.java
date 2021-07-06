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

package com.liferay.batch.planner.internal.batch.engine.broker;

import com.liferay.batch.planner.batch.engine.broker.BatchEngineBroker;
import com.liferay.batch.planner.exception.BatchPlannerMappingExternalFieldNameException;
import com.liferay.batch.planner.internal.jaxrs.uri.EmptyUriInfo;
import com.liferay.batch.planner.model.BatchPlannerMapping;
import com.liferay.batch.planner.model.BatchPlannerPlan;
import com.liferay.batch.planner.model.BatchPlannerPolicy;
import com.liferay.batch.planner.service.BatchPlannerLogLocalService;
import com.liferay.batch.planner.service.BatchPlannerMappingLocalService;
import com.liferay.batch.planner.service.BatchPlannerPlanLocalService;
import com.liferay.batch.planner.service.BatchPlannerPolicyLocalService;
import com.liferay.headless.batch.engine.dto.v1_0.ImportTask;
import com.liferay.headless.batch.engine.resource.v1_0.ImportTaskResource;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.vulcan.multipart.BinaryFile;
import com.liferay.portal.vulcan.multipart.MultipartBody;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;

import java.net.URI;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Igor Beslic
 */
@Component(service = BatchEngineBroker.class)
public class BatchEngineBrokerImpl implements BatchEngineBroker {

	public void submit(long batchPlannerPlanId) {
		try {
			_submit(batchPlannerPlanId);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to execute batch planner plan ID " + batchPlannerPlanId,
				exception);
		}
	}

	private void _submit(long batchPlannerPlanId) throws Exception {
		BatchPlannerPlan batchPlannerPlan =
			_batchPlannerPlanLocalService.getBatchPlannerPlan(
				batchPlannerPlanId);

		File file = _getJSONLineFile(batchPlannerPlanId);

		_importTaskResource.setContextCompany(
			_companyLocalService.getCompany(
				batchPlannerPlan.getCompanyId()));
		_importTaskResource.setContextUriInfo(new EmptyUriInfo());
		_importTaskResource.setContextUser(
			_userLocalService.getUser(batchPlannerPlan.getUserId()));

		ImportTask importTask = _importTaskResource.postImportTask(
			batchPlannerPlan.getInternalClassName(), null, null,
			"batch-planner-plan-" + batchPlannerPlanId,
			MultipartBody.of(
				Collections.singletonMap(
					"file",
					new BinaryFile(
						"application/json", file.getName(),
						new FileInputStream(file), file.length())),
				null, Collections.emptyMap()));

		_batchPlannerLogLocalService.addBatchPlannerLog(
			batchPlannerPlan.getUserId(), batchPlannerPlanId, null,
			String.valueOf(importTask.getId()), null, (int)file.length(),
			1);

		_batchPlannerPlanLocalService.updateActive(
			batchPlannerPlanId, true);
	}

	private String _asJSON(
		String[] columns, Map<Integer, BatchPlannerMapping> map) {

		StringBundler sb = new StringBundler();

		sb.append(CharPool.OPEN_CURLY_BRACE);

		Set<Integer> indexes = map.keySet();

		Iterator<Integer> iterator = indexes.iterator();

		while (iterator.hasNext()) {
			Integer idx = iterator.next();

			BatchPlannerMapping batchPlannerMapping = map.get(idx);

			sb.append(CharPool.QUOTE);
			sb.append(batchPlannerMapping.getInternalFieldName());
			sb.append("\": \"");
			sb.append(columns[idx]);
			sb.append(CharPool.QUOTE);

			if (iterator.hasNext()) {
				sb.append(CharPool.COMMA);
			}
		}

		sb.append(CharPool.CLOSE_CURLY_BRACE);

		return sb.toString();
	}

	private Map<String, String> _asMap(
		List<BatchPlannerPolicy> batchPlannerPolicies) {

		Map<String, String> map = new HashMap<>();

		for (BatchPlannerPolicy batchPlannerPolicy : batchPlannerPolicies) {
			map.put(
				batchPlannerPolicy.getName(), batchPlannerPolicy.getValue());
		}

		return map;
	}

	private Map<Integer, BatchPlannerMapping> _asMap(
			List<BatchPlannerMapping> batchPlannerMappings, String delimiter,
			String headersString)
		throws PortalException {

		Map<Integer, BatchPlannerMapping> map = new HashMap<>();

		String[] headers = _getHeaders(
			batchPlannerMappings, delimiter, headersString);

		for (BatchPlannerMapping batchPlannerMapping : batchPlannerMappings) {
			for (int i = 0; i < headers.length; i++) {
				if (Objects.equals(
						headers[i],
						batchPlannerMapping.getExternalFieldName())) {

					map.put(i, batchPlannerMapping);

					break;
				}
			}
		}

		if (map.isEmpty()) {
			throw new BatchPlannerMappingExternalFieldNameException(
				"Unable to match external field names with header names");
		}

		return map;
	}

	private String[] _getHeaders(
		List<BatchPlannerMapping> batchPlannerMappings) {

		return TransformUtil.transformToArray(
			batchPlannerMappings,
			batchPlannerMapping -> batchPlannerMapping.getExternalFieldName(),
			String.class);
	}

	private String[] _getHeaders(
		List<BatchPlannerMapping> batchPlannerMappings, String delimiter,
		String headersString) {

		if (Validator.isNull(headersString)) {
			return _getHeaders(batchPlannerMappings);
		}

		String[] headers = headersString.split(delimiter);

		if (batchPlannerMappings.size() != headers.length) {
			return _getHeaders(batchPlannerMappings);
		}

		for (BatchPlannerMapping batchPlannerMapping : batchPlannerMappings) {
			if (!ArrayUtil.contains(
					headers, batchPlannerMapping.getExternalFieldName())) {

				return _getHeaders(batchPlannerMappings);
			}
		}

		return headers;
	}

	private File _getJSONLineFile(long batchPlannerPlanId) throws Exception {
		BatchPlannerPlan batchPlannerPlan =
			_batchPlannerPlanLocalService.getBatchPlannerPlan(
				batchPlannerPlanId);

		File jsonlFile = FileUtil.createTempFile(
			String.valueOf(batchPlannerPlanId), "jsonl");
		Map<String, String> policies = _asMap(
			_batchPlannerPolicyLocalService.getBatchPlannerPolicies(
				batchPlannerPlanId));

		try (FileReader fileReader = new FileReader(
				new File(new URI(batchPlannerPlan.getExternalURL())));
			FileWriter fileWriter = new FileWriter(jsonlFile)) {

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line = null;

			if (GetterUtil.getBoolean(policies.get("hasColumnHeaders"))) {
				line = bufferedReader.readLine();
			}

			String delimiter = GetterUtil.getString(
				policies.get("delimiter"), StringPool.SEMICOLON);

			Map<Integer, BatchPlannerMapping> map = _asMap(
				_batchPlannerMappingLocalService.getBatchPlannerMappings(
					batchPlannerPlanId),
				delimiter, line);

			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			line = bufferedReader.readLine();

			while (line != null) {
				String[] columns = line.split(delimiter);

				bufferedWriter.append(_asJSON(columns, map));

				bufferedWriter.newLine();

				line = bufferedReader.readLine();
			}

			bufferedWriter.flush();

			return jsonlFile;
		}
		catch (Exception exception) {
			FileUtil.delete(jsonlFile);

			throw exception;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BatchEngineBrokerImpl.class);

	@Reference
	private BatchPlannerLogLocalService _batchPlannerLogLocalService;

	@Reference
	private BatchPlannerMappingLocalService _batchPlannerMappingLocalService;

	@Reference
	private BatchPlannerPlanLocalService _batchPlannerPlanLocalService;

	@Reference
	private BatchPlannerPolicyLocalService _batchPlannerPolicyLocalService;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ImportTaskResource _importTaskResource;

	@Reference
	private UserLocalService _userLocalService;

}